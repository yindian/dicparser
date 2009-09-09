/*****************************************************************************
 * 
 * @(#)CheckMagicNumber.java  2009/03
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
package com.jin.test.others;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.jin.util.io._DataInputStream;

public class CheckMagicNumber {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    File fil = new File("D:\\Jin\\Data\\KingSoft\\Dict\\Data\\PWDEEAHD\\PWDEEAHD.DIC_dictData.dat");
    InputStream in = new BufferedInputStream(new FileInputStream(fil));
    _DataInputStream le = new _DataInputStream(in);
    int magic;
    int len;
    int count = 0;
    while(le.available() > 0){
      len = le.readInt();
      magic = le.readInt();
      le.skipBytes(len);
      if(magic != 1){
        System.out.print(Integer.toHexString(len));
        System.out.println(Integer.toHexString(magic));
      }
      System.out.println(count++);
    }
  }
}
