/*****************************************************************************
 * 
 * @(#)Config.java  2009/11
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
package org.jin.dic.data.pub;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.jin.util.Logger;
import org.jin.util._Base64;

public class Config {
  public static void main(String[] args) throws IOException{
    try{
      if(args == null || args.length != 2){
        Logger.info("By Tim<jinxingquan@gmail.com>\n\n" + "To encode file:\n" + "  -d dictFilePath desFolder\nTo generate a dic file:\n"
            + "  -e srcFile desFile\n"
            + "To decode file:\n"
            + "  -d srcFile desFile\n");
        Console c = System.console();
        if(c != null) c.readLine();
        return;
      }
      if(args[0].equalsIgnoreCase("-e")) encode(args[0], args[1]);
      else if(args[0].equalsIgnoreCase("-d")) decode(args[0], args[1]);
    }catch(Exception e){
      Logger.err(e);
    }
  }
  public static void decode(String src, String des) throws Exception{
    FileInputStream fis = new FileInputStream(src);
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String line = br.readLine();
    br.close();
    byte[] data;
    String base64 = line;
    data = _Base64.decode(base64);
    for(int i = 0; i < data.length; i++)
      data[i] -= 8;

    File out = new File(des);
    OutputStream os = new BufferedOutputStream(new FileOutputStream(out));

    os.write(data);
    os.close();
  }
  public static void encode(String src, String des) throws Exception{
    File in = new File(src);
    InputStream is = new BufferedInputStream(new FileInputStream(in));
    byte[] data = new byte[(int) in.length()];
    is.read(data);
    for(int i = 0; i < data.length; i++)
      data[i] += 8;
    String base64 = _Base64.encode(data);
    data = base64.getBytes();
    File out = new File(des);
    OutputStream os = new BufferedOutputStream(new FileOutputStream(out));

    os.write(data);
    os.close();
  }
  
}
