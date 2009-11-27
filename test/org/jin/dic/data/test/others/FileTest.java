/*****************************************************************************
 * 
 * @(#)FileTest.java  2009/03
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
import java.io.RandomAccessFile;

public class FileTest {
  public static void main(String[] args) throws Exception{
    File a = new File("D:\\srcFolder\\dic.dic");
    // System.out.println(a.getParent());

    a = new File("D:\\Jin\\Data\\KingSoft\\ks\\src\\com\\jin\\Copy of ks\\Element.java");
    replaceTags(a);
  }

  private static void replaceTags(File file) throws IOException{
    RandomAccessFile raf = new RandomAccessFile(file, "rw");
    String line;
    long pos = 0;
    while((pos = raf.getFilePointer()) >= 0 && (line = raf.readLine()) != null){
      if(line.indexOf("@file                                    name@") != -1){
        System.out.println(line);
        line = line.replaceAll("@file                                    name@", file.getName());
        raf.seek(pos);
        raf.writeBytes(line);
      }
    }
    raf.close();
  }

}
