/*****************************************************************************
 * 
 * @(#)InputStreamUtil.java  2009/03
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

import java.io.IOException;
import java.io.InputStream;

public class InputStreamUtil {

  public synchronized final static byte[] getBytes(InputStream is, int len) throws IOException{
    byte[] buf = new byte[len];
    int offset = 0;
    int numRead = 0;
    while(offset < buf.length && (numRead = is.read(buf, offset, buf.length - offset)) >= 0){
      offset += numRead;
    }
    if(buf.length != len){
      throw new IOException("Could not completely read bytes " + len + " " + buf.length);
    }
    return buf;
  }
  public synchronized final static void readFully(InputStream is, byte[] buf, int offset, int len) throws IOException{
    int numRead = 0;
    while(offset < len && (numRead = is.read(buf, offset, len - offset)) >= 0)
      offset += numRead;
    if(numRead != len) throw new IOException("Could not completely read bytes " + len + " " + numRead);
  }

}
