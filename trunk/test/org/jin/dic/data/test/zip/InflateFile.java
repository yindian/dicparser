/*****************************************************************************
 * 
 * @(#)InflateFile.java  2009/03
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
package org.jin.dic.data.test.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Inflater;

import org.jin.util.io._ByteArrayOutputStream;


public class InflateFile {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    // read data
    String fileName = "C:\\Program Files\\Merriam-Webster\\books\\C11\\noname";
    File inFile = new File(fileName);
    byte[] data = new byte[(int) inFile.length()];
    InputStream in = new BufferedInputStream(new FileInputStream(inFile));
    in.read(data);
    in.close();

    // inflate
    _ByteArrayOutputStream baos = new _ByteArrayOutputStream(0x40000);
    byte[] tempBuf = new byte[0x4000];
    Inflater d = new Inflater();
    d.setInput(data);
    int deflatedLen = 0;
    int readCount = 0;
    while(!d.finished()){
      readCount = d.inflate(tempBuf);
      baos.write(tempBuf, 0, readCount);
      deflatedLen += readCount;
    }

    // write data
    File outFile = new File(fileName + ".ift");
    OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
    out.write(baos.toByteArray());
    out.close();
  }

}
