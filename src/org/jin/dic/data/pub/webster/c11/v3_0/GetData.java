/*****************************************************************************
 * 
 * @(#)GetData.java  2009/11
 *
 *  Copyright (C) 2009  Tim Bron<jinxingquan@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *****************************************************************************/
package org.jin.dic.data.pub.webster.c11.v3_0;

import java.io.File;
import java.io.IOException;

import org.jin.util.io.FileUtil;
import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._RandomAccessFile;

import com.jcraft.jzlib.ZInputStream;

public class GetData {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    String fileName = args[0];
    String outFileName = args[1];

    int len;
    _RandomAccessFile file = new _RandomAccessFile(fileName, "r", false);
    _RandomAccessFile out = new _RandomAccessFile(outFileName, "rw");
    ZInputStream zIn = null;
    file.skipBytes(2);
    int fileLen = (int) file.length();
    byte[] outBuf = new byte[0x4000];
    byte[] inBuf = new byte[0x8000];
    int readLen = -1;
    long pos;
    int skipPos = 0x145054a;
    int skipCount = 20;
    while(file.getFilePointer() < fileLen){
      len = file.readChar();
      file.readFully(inBuf, 0, len);
      zIn = new ZInputStream(new _ByteArrayInputStream(inBuf), true);
      try{
        while(zIn.available() > 0){
          readLen = zIn.read(outBuf);
          if(readLen >= 0){
            pos = out.getFilePointer();
            if(pos <= skipPos && pos + readLen >= skipPos){
              out.write(outBuf, 0, skipPos - (int) pos);
              out.write(outBuf, skipPos - (int) pos + skipCount, readLen - skipCount - (skipPos - (int) pos));
            }else if(out.getFilePointer() == 0){
              out.write(outBuf, 1, readLen - 1);
            }else out.write(outBuf, 0, readLen);
          }else break;
        }
      }catch(Exception e){
        if(file.getFilePointer() == 0x934112){
          out.write(FileUtil.getBytesFromFile(new File(args[2])));
        }else throw new RuntimeException(file.getFilePointer() + "\t" + out.getFilePointer() + e);
      }
    }
    out.close();
    zIn.close();
  }
}
