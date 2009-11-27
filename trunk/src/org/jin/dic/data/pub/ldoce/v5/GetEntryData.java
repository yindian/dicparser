/*****************************************************************************
 * 
 * @(#)GetEntryData.java  2009/11
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
package org.jin.dic.data.pub.ldoce.v5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jin.dic.data.sk.FSFile;
import org.jin.dic.data.sk.FileSystem;
import org.jin.util.Logger;

public class GetEntryData {

  /**
   * @param args
   */
  public static void main(String[] args) throws  Exception{
    Logger.printStack = true;
    String[] names = new String[] { args[0] };
    File desFld = new File(args[1]);
    for(int i = 0; i < names.length; i++){
      File fil = new File(names[i]);
      FileSystem fs = new FileSystem();
      fs.setConfigFileName(fil.getAbsolutePath());
      fs.bind();
      if(!fs.isValid()) continue;
      toLocal(desFld, fs.getRootDir());
      fs.unBind(false);
    }
  }
  private static void toLocal(File fld, FSFile f) throws IOException{
    if(!fld.exists()){
      if(f.isDirectory()) fld.mkdir();
      else fld.createNewFile();
    }
    if(f.isFile()){
      File file = new File(fld, f.getName());
      if(!file.exists()) file.createNewFile();
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(f.getContent());
      fos.close();
    }else{
      FileSystem fs = f.getFileSystem();
      int[] dirs = f.getDirs();
      int[] files = f.getFiles();
      for(int i = 0; i < dirs.length; i++){
        FSFile fd = fs.getDir(dirs[i]);
        toLocal(new File(fld, fd.getName()), fd);
      }
      for(int i = 0; i < files.length; i++){
        toLocal(fld, fs.getFile(files[i]));
      }
    }
  }

}
