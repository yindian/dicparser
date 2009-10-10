/*****************************************************************************
 * 
 * @(#)FileSystem.java  2009/03
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
import java.io.IOException;

import com.jin.dic.Environment;
import com.jin.dic.sk.i.IConfigable;
import com.jin.dic.sk.i.IRecord;
import com.jin.dic.sk.i.IRecordSet;
import com.jin.util.Logger;

public class FileSystem implements IConfigable {

  static final String DEFAULT_CONFIG_CFT    = "filesystem.cff";

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
      file = environment.getFile(cfgFileName, DEFAULT_CONFIG_CFT);
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
    int[] chirdrenfileIds = file.getChirdrenfileIds();
    chirdrenDirIds = new int[dirs.getRecordCount()];
    chirdrenfileIds = new int[files.getRecordCount()];
    for(int i = 0; i < dirs.getRecordCount(); i++){
      chirdrenDirIds[i] = (int) dirs.getRecord(i).getNumFieldValue(dChildrenSubFieldName);
    }
    for(int i = 0; i < files.getRecordCount(); i++){
      chirdrenfileIds[i] = (int) files.getRecord(i).getNumFieldValue(dFilesSubFieldName);
    }
    file.setChirdrenDirIds(chirdrenDirIds);
    file.setChirdrenfileIds(chirdrenfileIds);
    return file;
  }
  public FSFile getFile(int id, boolean content){
    if(!isValid) throw new IllegalStateException(INVALID);
    IRecord record = fileTable.getRecord(id);

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
  public FSFile getFile(int id){
    return getFile(id, true);
  }
  public FSFile getFSFile(String path){
    if(!isValid) throw new IllegalStateException(INVALID);
    FSFile dir = getDir(6);

    int id = getFile(path, dir.getChirdrenfileIds());
    return id == -1 ? null : getFile(id, true);
  }
  private int getFile(String fileName, int[] ids){
    FSFile file;
    for(int i = 0; i < ids.length; i++){
      file = getFile(ids[i], false);
      if(file.getName().equals(fileName)) return ids[i];
    }
    return -1;
  }

}
