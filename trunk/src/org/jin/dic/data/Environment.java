/*****************************************************************************
 * 
 * @(#)Environment  2009/03
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
package org.jin.dic.data;

import java.io.File;

public class Environment implements Cloneable {

  private String currentFolder = null;

  public File getFile(String folder, String fileName){
    File fld = new File(folder);
    if(!fld.exists()) fld = new File(currentFolder, folder);
    File file = new File(fld, fileName);
    if(file.exists() && file.isFile()){
      currentFolder = file.getParentFile().getAbsolutePath();
    }
    return file;
  }

  public File getFile(String fileName){
    File file;
    if(currentFolder == null) file = new File(fileName);
    else file = new File(currentFolder, fileName);
    if(file.exists() && file.isFile() && currentFolder == null){
      currentFolder = file.getParentFile().getAbsolutePath();
    }
    return file;
  }

  public String getCurrentFolder(){
    return currentFolder;
  }

  public void setCurrentFolder(String currentFolder){
    File fld = new File(currentFolder);
    if(fld.exists() && fld.isDirectory()) this.currentFolder = fld.getAbsolutePath();
  }

  public Object clone(){
    try{
      return super.clone();
    }catch(CloneNotSupportedException e){
      return null;
    }
  }

}
