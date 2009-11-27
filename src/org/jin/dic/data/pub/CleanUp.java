/*****************************************************************************
 * 
 * @(#)CleanUp.java  2009/11
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

import java.io.File;
import java.io.IOException;

public class CleanUp {
  
  public static void main(String[] args) throws IOException{
    File temp = new File(args[0]);
    if(temp.exists()){
      delete(temp);
    }else temp.mkdir();

    for(int i = 1; i < args.length; i++)
      new File(args[i]).delete();
  }
  private static void delete(File fld){
    File[] files = fld.listFiles();
    for(int i = 0; i < files.length; i++){
      if(files[i].isDirectory()) delete(files[i]);
      files[i].delete();
    }
  }

}
