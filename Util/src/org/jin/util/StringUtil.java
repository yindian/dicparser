/*****************************************************************************
 * 
 * @(#)StringUtil.java  2009/03
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
package org.jin.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {

  public static boolean equals(byte[] s1, byte[] s2, boolean ignoreCase){
    if(s1 == s2) return true;
    if(s1 == null || s2 == null) return false;
    int length = Math.min(s1.length, s2.length);
    int maxLen = Math.max(s1.length, s2.length);
    if(maxLen - length > 1) return false;
    for(int i = 0; i < length; i++)
      if(s1[i] != s2[i] && ignoreCase && Character.toLowerCase((char) s1[i]) != Character.toLowerCase((char) s2[i])){
        return false;
      }
    return true;
  }

  public static String valueOf(byte[] bytes, String encoding){
    if(bytes == null) return null;
    else return valueOf(bytes, 0, bytes.length, encoding);
  }

  public static String valueOf(byte[] bytes, int offset, int len, String encoding){
    if(bytes == null || bytes.length == 0) return null;
    String s;
    int count = len;
    try{
      if(count > 0) count = bytes[len + offset - 1] == 0 ? count - 1 : count;
      s = new String(bytes, offset, count, encoding);
    }catch(UnsupportedEncodingException e){
      e.printStackTrace();
      s = null;
    }
    return s;
  }
  public static byte[] getBytes(String s, String encoding){
    byte[] stringbytes = new byte[0];
    try{
      stringbytes = s.getBytes(encoding);
    }catch(UnsupportedEncodingException e){
      Logger.err(e);
    }
    return stringbytes;
  }

  /**
   * bytes without bom
   */
  public static byte[] getBytesNoBom(String s, String encoding){
    byte[] stringbytes = new byte[0];
    try{
      if(s != null && s.length() > 0){
        stringbytes = BytesUtil.BE2LE(s.getBytes(encoding));
        byte newbuf[] = new byte[stringbytes.length - 2];
        System.arraycopy(stringbytes, 2, newbuf, 0, newbuf.length);
        stringbytes = newbuf;
      }
    }catch(UnsupportedEncodingException e){
      Logger.err(e);
    }
    return stringbytes;
  }

  /**
   * @param bytes unicode le bytes without bom
   * @return string
   */
  public static String valueOf(byte[] bytes){
    if(bytes == null) return null;
    byte[] stringbytes = new byte[bytes.length + 2];
    stringbytes[0] = (byte) 0xff;
    stringbytes[1] = (byte) 0xfe;
    System.arraycopy(bytes, 0, stringbytes, 2, bytes.length);
    try{
      return new String(stringbytes, "UNICODE");
    }catch(UnsupportedEncodingException e){
      return null;
    }
  }

}
