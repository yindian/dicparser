/*****************************************************************************
 * 
 * @(#)DeflatedDataSourceMaker.java  2009/11
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
package org.jin.dic.data.alpha.make;

import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import org.jin.util.io._ByteArrayOutputStream;
import org.jin.util.io._RandomAccessFile;

public class DeflatedDataSourceMaker {

  private static int FRACTIONSIZE = 0x800;

  private int        fractionSize = FRACTIONSIZE;
  private int[]      blocks       = null;

  public static int getFRACTIONSIZE(){
    return FRACTIONSIZE;
  }

  public static void setFRACTIONSIZE(int fractionsize){
    FRACTIONSIZE = fractionsize;
  }

  public void setFractionSize(int fractionSize){
    this.fractionSize = fractionSize;
  }

  public int getFractionSize(){
    return fractionSize;
  }

  public static void make(String[] fileNames, String desFileName) throws IOException{
    new DeflatedDataSourceMaker().domake(fileNames, desFileName);
  }
  private void domake(String[] fileNames, String desFileName) throws IOException{
    _RandomAccessFile os = new _RandomAccessFile(desFileName, "rw");
    blocks = new int[(getSize(fileNames) - 1) / fractionSize + 1];
    os.write(new byte[blocks.length << 2]);
    FileInputStream is;
    byte[] buf = new byte[0x1000];
    int read;
    for(int i = 0; i < fileNames.length; i++){
      is = new FileInputStream(fileNames[i]);
      while((read = is.read(buf)) == 0x1000){
        writeDeflatedData(os, buf, read);
      }
      if(read != 0) writeDeflatedData(os, buf, read);
    }
    writeDeflatedData(os, null, 0);
    os.seek(0);
    for(int i = 0; i < blocks.length; i++){
      os.writeInt(blocks[i]);
    }
    os.close();
  }
  private static int getSize(String[] fileNames){
    int len = 0;
    for(int i = 0; i < fileNames.length; i++){
      len += new File(fileNames[i]).length();
    }
    return len;
  }

  _ByteArrayOutputStream baos  = new _ByteArrayOutputStream(fractionSize);
  int                    block = 0;
  private void writeDeflatedData(DataOutput out, byte[] data, int len) throws IOException{
    if(data != null){
      int toWrite = baos.size() + len;
      int dataPos = 0;
      while(toWrite > 0){
        while(dataPos < len && baos.size() != fractionSize){
          baos.write(data, dataPos++, 1);
          toWrite--;
        }
        if(baos.size() == fractionSize){
          writeDeflatedFragment(out, baos.toByteArray(true));
          baos.reset();
        }else break;
      }
    }else{
      writeDeflatedFragment(out, baos.toByteArray());
    }
  }

  byte[]   tempBuf  = new byte[fractionSize];
  Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
  private void writeDeflatedFragment(DataOutput out, byte[] data) throws IOException{
    deflater.reset();
    deflater.setInput(data);
    deflater.finish();
    int deflatedLen = 0;
    int count = 0;
    while(!deflater.finished()){
      count = deflater.deflate(tempBuf);
      out.write(tempBuf, 0, count);
      deflatedLen += count;
    }
    blocks[block] = deflatedLen;
    block++;
  }

}
