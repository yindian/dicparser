/*****************************************************************************
 * 
 * @(#)zlibutil.java  2009/03
 *
 *  Copyright (C) 2009  Tim Bron<jinxingquan@google.com>
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
package com.jin.test.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class zlibutil {

  /**
   * @param args
   * @throws DataFormatException
   * @throws IOException
   */
  public static void main(String[] args) throws Exception{
    String fileName = "D:\\Jin\\Data\\KingSoft\\Dict\\Data\\PWPECJS\\PWPECJS.DIC";
    String outFileName = "D:\\Jin\\Data\\KingSoft\\Dict\\Data\\PWPECJS\\inflate.dat";
    int offset = 0x75687;
    int len = 0x2784;
    saveBytesToFile(inflate(fileName, offset, len), outFileName);
  }
  public static byte[] deflate(String fileName) throws Exception{
    File fil = new File(fileName);
    InputStream in = new BufferedInputStream(new FileInputStream(fil));
    Deflater a = new Deflater(Deflater.DEFAULT_COMPRESSION);
    byte[] b = new byte[(int) fil.length()];
    in.read(b);
    a.setInput(b);
    a.finish();
    byte[] inf = new byte[10000];
    // int size = a.deflate(inf);
    return inf;
  }
  public static byte[] inflate(String fileName, int offset, int len) throws Exception{
    File fil = new File(fileName);
    InputStream in = new BufferedInputStream(new FileInputStream(fil));
    Inflater a = new Inflater();
    byte[] b = new byte[len];
    in.skip(offset);
    in.read(b);
    saveBytesToFile(b, fileName + "_deflate.dat");
    a.setInput(b);
    byte[] inf = new byte[20000];// bad!
    int size = a.inflate(inf);
    byte[] data = new byte[size];
    System.arraycopy(inf, 0, data, 0, size);
    return data;
  }
  private static void saveBytesToFile(byte[] data, String fileName) throws IOException{
    File out = new File(fileName);
    OutputStream os = new BufferedOutputStream(new FileOutputStream(out));
    os.write(data, 0, data.length);
    os.close();
  }

}
