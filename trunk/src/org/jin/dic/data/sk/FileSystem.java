/*****************************************************************************
 * 
 * @(#)FileSystem.java  2009/03
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jin.dic.data.Environment;
import org.jin.dic.data.sk.i.IConfigable;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.Logger;

public class FileSystem implements IConfigable {

  static final String DEFAULT_CONFIG_CFF4   = "filesystem.cff";
  static final String DEFAULT_CONFIG_CFF5   = "config.cff";

  static final String INVALID               = "File System invalid !";

  private Environment environment           = null;
  private boolean     isValid               = false;

  private Table       dirTable              = null;
  private String      dNameFieldName        = null;
  private String      dParentFieldName      = null;
  private String      dChildrenFieldName    = null;
  private String      dChildrenSubFieldName = null;
  private String      dFilesFieldName       = null;
  private String      dFilesSubFieldName    = null;

  private Table       fileTable             = null;
  private String      fNameFieldName        = null;
  private String      fParentFieldName      = null;
  private String      fTitleFieldName       = null;
  private String      fContentFieldName     = null;

  private String      cfgFileName           = null;

  public FileSystem() {
  }
  // getter and setter ----------------------------------------------------
  public boolean isValid(){
    return isValid;
  }
  public void setEnvironment(Environment environment){
    this.environment = environment;
  }
  public String getCfgFileName(){
    return cfgFileName;
  }

  // configuration --------------------------------------------------------
  public void setConfigFileName(String cfgFileName){
    try{
      config(cfgFileName);
    }catch(IOException e){
      Logger.err(e);
    }
  }
  public void configureItem(String section, String token, String value){
    // [DIRTABLE]
    if(section.equals("DIRTABLE")){
      if(dirTable == null){
        dirTable = new Table();
        dirTable.setEnvironment((Environment) environment.clone());
      }
      if(token.equals("RSURL")) dirTable.setConfigFileName(value);
      else if(token.equals("NAMEFIELD")) dNameFieldName = value;
      else if(token.equals("PARENTFIELD")) dParentFieldName = value;
      else if(token.equals("CHILDRENFIELD")) dChildrenFieldName = value;
      else if(token.equals("CHILDRENSUBFIELD")) dChildrenSubFieldName = value;
      else if(token.equals("FILESFIELD")) dFilesFieldName = value;
      else if(token.equals("FILESSUBFIELD")) dFilesSubFieldName = value;
    }

    // [FILETABLE]
    else if(section.equals("FILETABLE")){
      if(fileTable == null){
        fileTable = new Table();
        fileTable.setEnvironment((Environment) environment.clone());
      }
      if(token.equals("RSURL")) fileTable.setConfigFileName(value);
      else if(token.equals("NAMEFIELD")) fNameFieldName = value;
      else if(token.equals("PARENTFIELD")) fParentFieldName = value;
      else if(token.equals("TITLEFIELD")) fTitleFieldName = value;
      else if(token.equals("CONTENTFIELD")) fContentFieldName = value;
    }

    // [WILDCARDWORDLIST]
    else if(section.equals("WILDCARDWORDLIST")){
      Logger.err(new Exception("need implement"));// TODO
    }

    else{
      Logger.err("unknown section: " + section);
    }
  }
  private void config(String cfgFileName) throws IOException{
    if(environment == null) environment = new Environment();
    File file = environment.getFile(cfgFileName);
    if(!file.exists() || file.isDirectory()){
      file = environment.getFile(cfgFileName, DEFAULT_CONFIG_CFF4);
    }
    if(!file.exists() || file.isDirectory()){
      file = environment.getFile(cfgFileName, DEFAULT_CONFIG_CFF5);
    }
    if(file.exists()){
      this.cfgFileName = cfgFileName;
      Configurer c = new Configurer();
      c.config(file, this);
    }else Logger.err(new Exception("open file error!:" + cfgFileName));
  }

  // bind to file ---------------------------------------------------------
  public void bind(){
    if(dirTable == null || fileTable == null) return;
    dirTable.bind();
    fileTable.bind();
    isValid = dirTable.isValid() && fileTable.isValid();
  }
  public void unBind(boolean keepDataAvailable){
    if(!isValid) return;
    dirTable.unBind(keepDataAvailable);
    fileTable.unBind(keepDataAvailable);
    isValid = dirTable.isValid() && fileTable.isValid();
  }

  // ready to work --------------------------------------------------------
  public int getDirCount(){
    if(!isValid) throw new IllegalStateException(INVALID);
    return dirTable.getRecordCount();
  }
  public int getFileCount(){
    if(!isValid) throw new IllegalStateException(INVALID);
    return fileTable.getRecordCount();
  }
  public FSFile getRootDir(){
    return getDir(0);
  }
  public FSFile getDir(int id){
    if(!isValid) throw new IllegalStateException(INVALID);
    IRecord record = dirTable.getRecord(id);
    if(record == null) return null;
    IRecordSet dirs, files;

    FSFile file = new FSFile();
    file.setFileSystem(this);
    file.setDirectory(true);
    file.setId(id);
    file.setName(record.getDataFieldValue(dNameFieldName));
    file.setParentId((int) record.getNumFieldValue(dParentFieldName));

    dirs = record.getLinkFieldValue(dChildrenFieldName);
    files = record.getLinkFieldValue(dFilesFieldName);

    int[] chirdrenDirIds = file.getChirdrenDirIds();
    int[] chirdrenfileIds = file.getChirdrenFileIds();
    chirdrenDirIds = new int[dirs.getRecordCount()];
    chirdrenfileIds = new int[files.getRecordCount()];
    for(int i = 0; i < dirs.getRecordCount(); i++){
      chirdrenDirIds[i] = (int) dirs.getRecord(i).getNumFieldValue(dChildrenSubFieldName);
    }
    for(int i = 0; i < files.getRecordCount(); i++){
      chirdrenfileIds[i] = (int) files.getRecord(i).getNumFieldValue(dFilesSubFieldName);
    }
    file.setChirdrenDirIds(chirdrenDirIds);
    file.setChirdrenFileIds(chirdrenfileIds);
    return file;
  }
  public FSFile getFile(int id){
    return getFile(id, true);
  }
  public FSFile getFile(int id, boolean content){
    if(!isValid) throw new IllegalStateException(INVALID);
    IRecord record = fileTable.getRecord(id);
    if(record == null) return null;

    FSFile file = new FSFile();
    file.setFileSystem(this);
    file.setDirectory(false);
    file.setId(id);
    file.setName(record.getDataFieldValue(fNameFieldName));
    file.setTitle(record.getDataFieldValue(fTitleFieldName));
    file.setParentId((int) record.getNumFieldValue(fParentFieldName));
    if(content) file.setContent(record.getDataFieldValue(fContentFieldName));
    return file;
  }
  public int getFileId(String path){
    if(!isValid) throw new IllegalStateException(INVALID);
    if(path == null) return -1;
    String[] nodes = path.split("[//\\\\]");
    FSFile dir = getRootDir();
    int id = -1;
    for(int i = 0; i < nodes.length; i++){
      id = getFileId(nodes[i], i != (nodes.length - 1), dir);
      if(i != (nodes.length - 1)) dir = getDir(id);
    }
    return id;
  }
  public String getFilePath(int id){
    FSFile file = getFile(id, false);
    List nameList = new ArrayList();
    addDirName(nameList, getDir(file.getParentId()));
    StringBuffer path = new StringBuffer();
    for(int i = 0; i < nameList.size(); i++){
      path.append(nameList.get(i));
      path.append("/");
    }
    path.append(file.getName());
    return path.toString();
  }
  private void addDirName(List nameList, FSFile dir){
    if(dir == null || nameList == null || dir.getId() == 0) return;
    nameList.add(0, dir.getName());
    addDirName(nameList, getDir(dir.getParentId()));
  }
  public FSFile getFile(String path){
    return getFile(path, true);
  }
  public FSFile getFile(String path, boolean content){
    int id = getFileId(path);
    return id == -1 ? null : getFile(id, content);
  }
  private int getFileId(String name, boolean isFolderName, FSFile dir){
    if(dir != null && dir.isDirectory()){
      int[] ids;
      FSFile file;
      if(isFolderName){
        ids = dir.getChirdrenDirIds();
      }else{
        ids = dir.getChirdrenFileIds();
      }
      for(int i = 0; i < ids.length; i++){
        if(isFolderName){
          file = getDir(ids[i]);
        }else{
          file = getFile(ids[i], false);
        }
        if(file.getName().equals(name)) return ids[i];
      }
    }
    return -1;
  }

}
