/*****************************************************************************
 * 
 * @(#)ZipEntryTest.java  2009/03
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jin.dic.data.ks.TestFolderConfig;
import org.jin.util.BytesUtil;
import org.jin.util.StringUtil;
import org.jin.util.io._ByteArrayOutputStream;


public class ZipEntryTest {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    String dictName = "PWDEEAHD.DIC";
    String fileName = TestFolderConfig.d_myFld + dictName + ".zip";
    File file = new File(fileName);
    ZipFile zFile = new ZipFile(file);
    ZipEntry dataEntry = null;
    dataEntry = zFile.getEntry("114487");

    _ByteArrayOutputStream bos = new _ByteArrayOutputStream(2048);
    int b;
    InputStream in = zFile.getInputStream(dataEntry);
    while((b = in.read()) != -1)
      bos.write(b);

    byte[] data = bos.toByteArray();
    System.out.println(StringUtil.valueOf(data));

    data = new byte[(int) dataEntry.getSize() + 100];
    in = zFile.getInputStream(dataEntry);
    in.read(data);
    System.out.println(BytesUtil.convert(data));

  }

}
