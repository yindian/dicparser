/*****************************************************************************
 * 
 * @(#)BytesUtil.java  2009/03
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Locale;

import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._ByteArrayOutputStream;
import org.jin.util.io._DataInputStream;
import org.jin.util.io._DataOutputStream;

public class BytesUtil {

  public static String convert(byte[] data){
    if(data == null) return null;
    else return convert(data, 0, data.length);
  }
  public static String convert(byte[] data, int offset, int len){
    if(data == null) return null;
    StringBuffer result = new StringBuffer();
    int start;
    for(int i = offset; i < len; i++){
      start = result.length();
      result.append("0");
      result.append(Integer.toHexString(data[i]).toUpperCase(Locale.ENGLISH));
      result.replace(start, result.length() - 2, "");
    }
    return result.toString();
  }

  public static byte[] revert(String data){
    if(data == null) return new byte[0];
    int size = data.length() / 2;
    byte[] result = new byte[size];
    for(int i = 0; i < size; i++){
      result[i] = (byte) Integer.parseInt(data.substring(i << 1, (i << 1) + 2), 16);
    }
    return result;
  }

  public static void print(byte[] data){
    if(data == null) return;
    StringBuffer result = new StringBuffer();
    int start;
    int lines = 0;
    for(int i = 0; i < data.length; i++){
      result.append(" ");
      start = result.length();
      result.append("0");
      result.append(Integer.toHexString(data[i]).toUpperCase(Locale.ENGLISH));
      result.replace(start, result.length() - 2, "");
      if(result.length() % 24 == 0){
        System.out.print(Integer.toHexString(lines << 4).toUpperCase());
        System.out.print(":");
        System.out.println(result);
        result = new StringBuffer();
        lines++;
      }
    }
  }

  /**
   * be bytes 2 le bytes
   */
  public static byte[] BE2LE(byte[] beData){
    byte[] leData = new byte[0];
    try{
      ByteArrayInputStream bais = new ByteArrayInputStream(beData);
      _DataInputStream beDis = new _DataInputStream(bais, false);
      _ByteArrayOutputStream baos = new _ByteArrayOutputStream(beData.length);
      _DataOutputStream leDos = new _DataOutputStream(baos, true);

      while(beDis.available() >= 2){
        leDos.writeChar(beDis.readChar());
      }
      leData = baos.toByteArray(true);
    }catch(IOException e){
      Logger.err(e);
    }
    return leData;
  }

  /**
   * le bytes 2 be bytes
   */
  public static byte[] LE2BE(byte[] leData){
    byte[] beData = new byte[0];
    try{
      _ByteArrayInputStream bais = new _ByteArrayInputStream(leData);
      _DataInputStream leDis = new _DataInputStream(bais);
      _ByteArrayOutputStream baos = new _ByteArrayOutputStream(leData.length);
      _DataOutputStream beDos = new _DataOutputStream(baos, false);

      while(leDis.available() >= 2){
        beDos.writeChar(leDis.readChar());
      }
      beData = baos.toByteArray(true);
    }catch(IOException e){
      Logger.err(e);
    }
    return beData;
  }

  /**
   * remove the ending 0
   * 
   * @param data
   * @return
   */
  public static byte[] trimData(byte[] data){
    byte[] b;
    int len = data.length;
    if(len < 1){
      b = new byte[0];
    }else{
      if(data[len - 1] == 0){
        b = new byte[len - 1];
        System.arraycopy(data, 0, b, 0, b.length);
      }else b = data;
    }
    return b;
  }

}
