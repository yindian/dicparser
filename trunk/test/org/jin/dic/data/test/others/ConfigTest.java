/*****************************************************************************
 * 
 * @(#)ConfigTest.java  2009/03
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
package org.jin.dic.data.test.others;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jin.util._Base64;


public class ConfigTest {

  public static void main(String[] args) throws IOException{
    try{
//      decode();
       encode();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  public static void decode() throws Exception{
    byte[] data;
    String base64 = "a";
    data = _Base64.decode(base64);
    for(int i = 0; i < data.length; i++){
      data[i] -= 8;
    }
    
    File out = new File("alpha/1252.txt");
    OutputStream os = new BufferedOutputStream(new FileOutputStream(out));

    os.write(data);
    os.close();
  }
  public static void encode() throws Exception{
    File in = new File("alpha/1252.txt");
    InputStream is = new BufferedInputStream(new FileInputStream(in));
    byte[] data = new byte[(int) in.length()];
    is.read(data);
    for(int i = 0; i < data.length; i++){
      data[i] += 8;
    }
    String base64 = _Base64.encode(data);
    data = base64.getBytes();
    File out = new File("alpha/productInfo_1252.conf");
    OutputStream os = new BufferedOutputStream(new FileOutputStream(out));

    os.write(data);
    os.close();

  }

}
