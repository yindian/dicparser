/*****************************************************************************
 * 
 * @(#)FSFile.java  2009/03
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

import com.jin.util.StringUtil;

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
    name = StringUtil.valueOf(trimData(data), "ISO-8859-1");
  }
  void setTitle(byte[] data){
    title = StringUtil.valueOf(trimData(data), "ISO-8859-1");
  }
  int[] getChirdrenDirIds(){
    return chirdrenDirIds;
  }
  int[] getChirdrenfileIds(){
    return chirdrenfileIds;
  }
  void setChirdrenDirIds(int[] chirdrenDirIds){
    this.chirdrenDirIds = chirdrenDirIds;
  }
  void setChirdrenfileIds(int[] chirdrenfileIds){
    this.chirdrenfileIds = chirdrenfileIds;
  }
  void setContent(byte[] content){
    this.content = trimData(content);
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
  public FSFile[] getFsFiles(){
    throw new UnsupportedOperationException();// TODO
  }
  private byte[] trimData(byte[] data){
    byte[] b;
    int len = data.length;
    if(len < 1){
      b = new byte[0];
    }else{
      if(data[len - 1] == 0){
        b = new byte[len - 1];
        System.arraycopy(data, 0, b, 0, b.length);
      }else b = data;
    }
    return b;
  }

}
