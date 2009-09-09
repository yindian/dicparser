/*****************************************************************************
 * 
 * @(#)ZFS2Xls.java  2009/03
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
package com.jin.dic.sk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.write.biff.RowsExceededException;

import com.jin.dic.sk.FSFile;
import com.jin.dic.sk.FileSystem;
import com.jin.util.Logger;

public class FS2Local {

  /**
   * @param args
   * @throws Exception
   * @throws RowsExceededException
   */
  public static void main(String[] args) throws RowsExceededException, Exception{
    Logger.printStack = true;
    String[] names = getNames("C:/Program Files/Longman/ldoce4v2/data/package");
    File desFld = new File("D:/Jin/Data/Longman/package_");
    for(int i = 0; i < names.length; i++){
      File fil = new File(names[i]);
      FileSystem fs = new FileSystem();
      fs.setConfigFileName(fil.getAbsolutePath());
      fs.bind();
      if(!fs.isValid()) continue;
      toLocal(new File(desFld, fil.getParentFile().getName()), fs.getRootDir());
      fs.unBind(false);
    }
  }

  private static void toLocal(File fld, FSFile f) throws IOException{
    if(!fld.exists()){
      if(f.isDirectory()) fld.mkdir();
      else fld.createNewFile();
    }
    if(f.isFile()){
      File file = new File(fld, f.getId() + "_" + f.getName());
      if(!file.exists()) file.createNewFile();
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(f.getContent());
      fos.close();
    }else{
      FileSystem fs = f.getFileSystem();
      int count = fs.getFileCount();
      for(int i = 0; i < count; i++){
        if(i > 500) break;
        toLocal(fld, fs.getFile(i));
      }
    }
  }

  private static String[] getNames(String fldPath){
    File fld = new File(fldPath);
    List flds = new ArrayList();
    File[] files = fld.listFiles();
    for(int i = 0; i < files.length; i++){
      if(files[i].isDirectory()) flds.add(files[i].getAbsolutePath() + "/filesystem.cff");
    }
    return (String[]) flds.toArray(new String[flds.size()]);
  }

}
