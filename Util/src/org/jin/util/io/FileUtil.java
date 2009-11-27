/*****************************************************************************
 * 
 * @(#)FileUtil.java  2009/11
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
package org.jin.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

  public static int getBytesFromFile(File file, byte[] buf, int offset) throws IOException{
    int readCount = 0;
    int numRead = 0;
    int fileLen = (int) file.length();
    int maxOffset = fileLen + offset;
    int len = fileLen + offset;
    if(buf == null || buf.length < maxOffset) throw new IOException("buf eror");

    InputStream in = new FileInputStream(file);
    while(offset < maxOffset && (numRead = in.read(buf, offset, len)) > 0){
      len -= numRead;
      offset += numRead;
      readCount += numRead;
    }
    if(readCount != fileLen) throw new IOException("Could not completely read bytes " + fileLen + " " + readCount);
    return readCount;
  }
  public static byte[] getBytesFromFile(File file) throws IOException{
    InputStream is = new FileInputStream(file);
    long length = file.length();
    if(length > Integer.MAX_VALUE){
    }
    byte[] bytes = new byte[(int) length];
    int offset = 0;
    int numRead = 0;
    while(offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
      offset += numRead;
    try{
      if(offset < bytes.length) throw new IOException("Could not completely read file " + file.getName());
    }finally{
      is.close();
    }
    return bytes;
  }

}
