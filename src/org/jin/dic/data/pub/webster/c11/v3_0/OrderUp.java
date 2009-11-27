/*****************************************************************************
 * 
 * @(#)OrderUp.java  2009/11
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

import java.io.IOException;

import org.jin.util.io._RandomAccessFile;

public class OrderUp {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    _RandomAccessFile in = new _RandomAccessFile(args[0], "rw");
    int type, len, len85;
    long lastPos = -1;
    byte[] bufd5 = new byte[2048];
    byte[] buf85 = new byte[2048];
    while(in.getFilePointer() < in.length()){
      lastPos = in.getFilePointer();
      type = in.read();
      if(type == 0) continue;
      len = in.read();
      if((len & 0x80) != 0) len = in.read() + (0x80 * (len & 0x7f));
      in.readFully(bufd5, 0, len);
      if(type == 0xd5){
        in.mark();
        if(in.read() == 0x85){
          len85 = in.read();
          in.readFully(buf85, 0, len85);
          in.seek(lastPos);
          in.write(0x85);
          in.write(len85);
          in.write(buf85, 0, len85);
          in.write(0xd5);
          in.write(len);
          in.write(bufd5, 0, len);
        }else in.reset();
      }
    }
    in.close();
  }

}
