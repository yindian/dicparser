/*****************************************************************************
 * 
 * @(#)FSFile.java  2009/03
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
package org.jin.dic.data.sk;

import org.jin.util.BytesUtil;
import org.jin.util.StringUtil;

public class FSFile {

  private FileSystem fileSystem      = null;
  private boolean    isDirectory     = false;
  private String     name            = null;
  private int        id              = -1;
  private int        parentId        = -1;

  private String     title           = null;
  private byte[]     content         = null;

  private int[]      chirdrenDirIds  = null;
  private int[]      chirdrenfileIds = null;

  FSFile() {
  }

  void setFileSystem(FileSystem fileSystem){
    this.fileSystem = fileSystem;
  }
  void setId(int id){
    this.id = id;
  }
  void setParentId(int parentId){
    this.parentId = parentId;
  }
  void setDirectory(boolean isDirectory){
    this.isDirectory = isDirectory;
  }
  void setName(byte[] data){
    name = StringUtil.valueOf(BytesUtil.trimData(data), "ISO-8859-1");// FIXME maybe UTF-8
  }
  void setTitle(byte[] data){
    title = StringUtil.valueOf(BytesUtil.trimData(data), "ISO-8859-1");// FIXME maybe UTF-8
  }
  int[] getChirdrenDirIds(){
    return chirdrenDirIds;
  }
  int[] getChirdrenFileIds(){
    return chirdrenfileIds;
  }
  void setChirdrenDirIds(int[] chirdrenDirIds){
    this.chirdrenDirIds = chirdrenDirIds;
  }
  void setChirdrenFileIds(int[] chirdrenfileIds){
    this.chirdrenfileIds = chirdrenfileIds;
  }
  void setContent(byte[] content){
    this.content = BytesUtil.trimData(content);
    // this.content = content;
  }

  public FileSystem getFileSystem(){
    return fileSystem;
  }
  public boolean isDirectory(){
    return isDirectory;
  }
  public boolean isFile(){
    return !isDirectory;
  }
  public int getParentId(){
    return parentId;
  }
  public int getId(){
    return id;
  }
  public String getName(){
    return name;
  }
  public String getTitle(){
    return title;
  }
  public byte[] getContent(){
    return content;
  }
  public int[] getDirs(){
    return chirdrenDirIds;
  }
  public int[] getFiles(){
    return chirdrenfileIds;
  }
}
