/*****************************************************************************
 * 
 * @(#)Table.java  2009/03
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
import com.jin.dic.sk.i.IDataSource;
import com.jin.dic.sk.i.IField;
import com.jin.dic.sk.i.IFieldCollection;
import com.jin.dic.sk.i.IRecord;
import com.jin.dic.sk.i.IRecordSet;
import com.jin.dic.sk.i.LookUpMode;
import com.jin.util.Logger;
import com.jin.util.StringUtil;

public final class Table implements IRecordSet, IConfigable {

  static final String DEFAULT_DIRECTORY_SUFFIX = ".skn";
  static final String DEFAULT_CONFIG_CFT       = "config.cft";

  static final String INVALID                  = "TABLE INVALID";

  private Environment environment              = null;
  private int         firstId                  = 0;
  private int         recordCount              = -1;
  private boolean     isValid                  = false;
  private boolean     isProtected              = false;
  private String      tableName                = null;
  private PageFile    dataFile                 = null;
  private LCFile      lcFile                   = null;
  private IField      lookupField              = null;
  private boolean     unBound                  = false;
  private RecordSet   independentRS            = null;
  private boolean     root                     = true;
  private String      cfgFileName              = null;

  public Table() {
  }
  // getter and setter ----------------------------------------------------
  public String getCfgFileName(){
    return cfgFileName;
  }
  public boolean isRoot(){
    return root;
  }
  public void setRoot(boolean root){
    this.root = root;
  }
  public IField getLookUpfield(){
    return lookupField;
  }
  public boolean isValid(){
    return isValid;
  }
  public int getRecordCount(){
    return recordCount;
  }
  public void setLookUpfield(IField lookupField){
    Logger.err(new Exception("you can't specify table's lookupField, use config memthod"));
  }
  public boolean isProtected(){
    return isProtected;
  }
  public String getTableName(){
    return tableName;
  }
  public PageFile getDataFile(){
    return dataFile;
  }
  public void setEnvironment(Environment environment){
    // should be called by field class with subtable
    this.environment = environment;
  }
  void setFirstId(int firstId){
    this.firstId = firstId;
  }
  void setRecordCount(int recordCount){
    this.recordCount = recordCount;
  }
  int getFirstId(){
    return firstId;
  }

  // configuration --------------------------------------------------------
  public void setConfigFileName(String cfgFileName){
    try{
      dataFile = null;
      lcFile = null;
      config(cfgFileName);
    }catch(IOException e){
      Logger.err(e);
    }
  }
  public void configureItem(String section, String token, String value){
    // [GENERAL]
    if(section.equals("GENERAL")){
      Logger.err(new Exception("err_not_handled: GENERAL"));
    }

    // [RECORDCACHE]
    else if(section.equals("RECORDCACHE")){
      Logger.err(new Exception("need implement: RECORDCACHE"));// TODO
      // SKRecordSet::ConfigureItem(pszSection, pszName, pszValue);
    }

    // [DAT]
    else if(section.equals("DAT")){
      if(dataFile == null){
        dataFile = new PageFile();
        dataFile.setEnvironment((Environment) environment.clone());
      }
      dataFile.configureItem(section, token, value);
    }

    // [LC]
    else if(section.equals("LC")){
      if(lcFile == null){
        lcFile = new LCFile();
        lcFile.setEnvironment(environment);
      }
      if(token.equals("FIELD")){
        if(lookupField != null){
          Logger.err(new Exception("LC file record given twice or more."));
        }else{
          lookupField = dataFile.getField(value);
          lcFile.setLookupField(lookupField);
          if(lookupField == null) Logger.err(new Exception("Field unknown: " + value));
        }
      }else{
        lcFile.configureItem(section, token, value);
      }
    }

    // [$field,1] -> configuration of a subfile
    else if(section.charAt(0) == Field.FIELD_ESCAPE_CHAR){
      String[] temp = section.substring(1).split(",");
      String fieldName = temp[0];
      String dataSourceIndex = "0";
      if(temp.length == 2) dataSourceIndex = temp[1];
      IField field = dataFile.getField(fieldName);
      if(field == null) Logger.err(new Exception("field unknown: " + fieldName));
      else field.configureItem(dataSourceIndex, token, value);
    }

  }
  private void config(String cfgFileName) throws IOException{
    File file = getFile(cfgFileName);
    if(file.exists()){
      this.cfgFileName = cfgFileName;
      Configurer c = new Configurer();
      c.config(file, this);
    }else Logger.err(new Exception("open file error!:" + cfgFileName));
  }
  private File getFile(String fileName){
    if(environment == null) environment = new Environment();
    File file = environment.getFile(fileName);
    if(fileName.indexOf("native:") != -1){
      if(!file.exists()) file = environment.getFile(fileName.replaceFirst("native:", "")
          + DEFAULT_DIRECTORY_SUFFIX, DEFAULT_CONFIG_CFT);
    }else{
      if(!file.exists() || file.isDirectory()){
        file = environment.getFile(fileName, DEFAULT_CONFIG_CFT);
        if(!file.exists() || file.isDirectory()){
          file = environment.getFile(fileName + DEFAULT_DIRECTORY_SUFFIX, DEFAULT_CONFIG_CFT);
        }
      }
    }
    return file;
  }

  // bind to file ---------------------------------------------------------
  public void bind(){
    if(dataFile == null) return;

    dataFile.open();
    if(lcFile != null) lcFile.open();

    IField field;
    IDataSource ds;
    IRecordSet subTable;
    IFieldCollection fldCollection = dataFile.getFldCollection();
    for(int i = 0; i < fldCollection.getFieldCount(); i++){
      field = fldCollection.getField(i);
      subTable = field.getLinkRecordSet();
      if(subTable != null) subTable.bind();
      for(int j = 0; j < field.getDataSourceCount(); j++){
        ds = field.getDataSource(j);
        ds.open();
      }
    }

    recordCount = dataFile.getRecordCount();
    isValid = true;
  }
  public void unBind(boolean keepDataAvailable){
    if(!isValid) return;
    if(keepDataAvailable){
      unBound = true;
      independentRS = (RecordSet) dataFile.getRecordSet(firstId, recordCount);
      recordCount = independentRS.getRecordCount();
      independentRS.unBind(true);
      firstId = 0;
    }else{
      isValid = false;
    }

    if(root){
      IField field;
      IDataSource ds;
      Table subTable;
      IFieldCollection fldCollection = dataFile.getFldCollection();
      for(int i = 0; i < fldCollection.getFieldCount(); i++){
        field = fldCollection.getField(i);
        subTable = (Table) field.getLinkRecordSet();
        if(subTable != null) subTable.dataFile.close();
        for(int j = 0; j < field.getDataSourceCount(); j++){
          ds = field.getDataSource(j);
          ds.close();
        }
      }
      if(lcFile != null) lcFile.close();
      dataFile.close();
    }

  }

  // ready to work --------------------------------------------------------
  public IRecord getRecord(int index){
    if(!isValid) throw new IllegalStateException(INVALID);
    if(unBound) return independentRS.getRecord(index + firstId);
    else return dataFile.getRecord(index + firstId);
  }
  public IRecordSet getSubRecordSet(int off, int count){
    if(!isValid) throw new IllegalStateException(INVALID);
    if(unBound){
      return independentRS.getSubRecordSet(off, count);
    }else{
      Table table = (Table) clone();
      table.setFirstId(off);
      table.setRecordCount(count);
      return table;
    }
  }
  public IFieldCollection getFldCollection(){
    if(dataFile == null) throw new IllegalStateException(INVALID);
    return dataFile.getFldCollection();
  }
  public int lookUpNum(long search, LookUpMode mode){
    return lookUpText(String.valueOf(search), mode);
  }
  public int lookUpText(String search, LookUpMode mode){
    if(!isValid) throw new IllegalStateException(INVALID);
    // TODO when unBound is true
    int index = -1;
    if(lookupField == null) return index;
    int pageIndex = lcFile.lookUp(search);
    if(pageIndex == -1) return index;
    int pageSize = dataFile.getPageSize();
    int pos = lookupField.getPosition();
    byte[] searchBytes = StringUtil.getBytes(search, "ISO-8859-1");
    byte[] dataBytes;
    IRecord[] records = dataFile.getRecords(pageIndex);
    boolean isNum = lookupField.isNum();
    long lSearch = 0;
    if(isNum){
      try{
        lSearch = Long.valueOf(search).longValue();
      }catch(NumberFormatException e){
        return index;
      }
    }
    for(int i = 0; i < records.length; i++){
      index = i + pageIndex * pageSize;
      if(!isNum){
        dataBytes = records[i].getDataFieldValue(pos);
        if(StringUtil.equals(dataBytes, searchBytes, true)) break;
      }else{
        if(lSearch == records[i].getNumFieldValue(pos)) break;
      }
    }

    return index;
  }

  // common ---------------------------------------------------------------
  public String toString(){
    StringBuffer s = new StringBuffer();
    s.append(dataFile.toString());
    return s.toString();
  }
  public Object clone(){//XXX super.clone doesn't work?!
    Table table = new Table();
    table.environment = environment;
    table.firstId = firstId;
    table.recordCount = recordCount;
    table.isValid = isValid;
    table.isProtected = isProtected;
    table.tableName = tableName;
    table.dataFile = dataFile;
    table.lcFile = lcFile;
    table.lookupField = lookupField;
    table.root = root;
    return table;
  }

}
