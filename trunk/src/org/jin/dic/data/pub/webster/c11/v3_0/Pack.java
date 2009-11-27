/*****************************************************************************
 * 
 * @(#)Pack.java  2009/11
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

import java.io.File;
import java.io.IOException;

import org.jin.dic.data.pub.CommonConstants;
import org.jin.util.Logger;
import org.jin.util.io.FileUtil;
import org.jin.util.io._ZipFile;

public class Pack {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    String fldName = args[0];
    String zipName = args[1];
    _ZipFile zFile = new _ZipFile(zipName, "w");
    File fld = new File(fldName);
    Pack pack = new Pack();
    pack.write(fld, zFile);
    zFile.close();
  }
  private int count = 0;
  private void write(File fld, _ZipFile zFile) throws IOException{
    File[] files = fld.listFiles();
    File file;
    for(int i = 0; i < files.length; i++){
      file = files[i];
      if(file.isDirectory()){
        write(file, zFile);
      }else{
        zFile.write(file.getName(), 0xff);
        zFile.write(0xfe);
        zFile.write(CommonConstants.KSDICBBYTES);
        zFile.write(FileUtil.getBytesFromFile(file));
        zFile.write(CommonConstants.KSDICEBYTES);
        zFile.write(0);
        zFile.write(0);
        count++;
        if(count % 100 == 0) Logger.info(count);
      }
    }
  }

}
