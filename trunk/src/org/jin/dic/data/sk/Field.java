/*****************************************************************************
 * 
 * @(#)Field.java  2009/03
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

import java.util.ArrayList;
import java.util.List;

import org.jin.dic.data.Environment;
import org.jin.dic.data.sk.i.FieldType;
import org.jin.dic.data.sk.i.IDataSource;
import org.jin.dic.data.sk.i.IField;
import org.jin.dic.data.sk.i.IRecordSet;


public class Field implements IField {

  static final char   FIELD_ESCAPE_CHAR = '$';

  private Environment environment       = null;
  private FieldType   type              = null;

  private String      name              = null;
  private int         size              = 0;
  private int         offset            = 0;
  private int         position          = 0;

  /* Sub fields for composite field */
  private IField      offsetField       = null;
  private IField      countField        = null;
  private IField      packField         = null; // only DATA fields

  /* Sub files for DATA fields */
  private List        subDataFiles      = null;

  /* Sub recordset for LINK fields */
  private Table       subTable          = null;

  public Field() {
  }
  // getter and setter ----------------------------------------------------
  public void setEnvironment(Environment environment){
    this.environment = environment;
  }
  public List getSubPageFiles(){
    return subDataFiles;
  }
  public boolean check(){
    throw new UnsupportedOperationException();// TODO
  }
  public void setType(FieldType t){
    this.type = t;
    size = t.getSize();
  }
  public int getOffset(){
    return offset;
  }
  public void setOffset(int offset){
    this.offset = offset;
    if(type.isComposite()) computeOffsets();
  }
  public int getPosition(){
    return position;
  }
  public void setPosition(int position){
    this.position = position;
  }
  public void setName(String name){
    this.name = name;
  }
  public int getSize(){
    return size;
  }
  public FieldType getType(){
    return type;
  }
  public String getName(){
    return name;
  }
  public boolean isData(){
    return type.isData();
  }
  public boolean isLink(){
    return type.isLink();
  }
  public boolean isSNum(){
    return type.isSigned();
  }
  public boolean isUNum(){
    return type.isUnSigned();
  }
  public boolean isNum(){
    return type.isNumber();
  }
  public IField getSubOffsetField(){
    return offsetField;
  }
  public IField getSubCountField(){
    return countField;
  }
  public IField getSubPackField(){
    return packField;
  }
  public IDataSource getDataSource(int index){
    return (IDataSource) subDataFiles.get(index);
  }
  public IRecordSet getLinkRecordSet(){
    return subTable;
  }
  public int getDataSourceCount(){
    return subDataFiles == null ? 0 : subDataFiles.size();
  }

  // configuration --------------------------------------------------------
  public void configureItem(String section, String token, String value){
    if(!type.isComposite()) return;
    if(section != null && section.length() != 0){
      configureComposite(Integer.valueOf(section).intValue(), token, value);
      return;
    }
    IField field = null;
    if(token.equals("OFFSET")){
      if(offsetField == null) offsetField = new Field();
      field = offsetField;
    }else if(token.equals("COUNT")){
      if(countField == null) countField = new Field();
      field = countField;
    }else if(type.isData() && !token.equals("PACKID")){
      if(packField == null) packField = new Field();
      field = packField;
    }

    field.setType(new FieldType(value));
    field.setName(this.name + "," + token);

    size = 0;
    if(offsetField != null) size += offsetField.getSize();
    if(countField != null) size += countField.getSize();
    if(packField != null) size += packField.getSize();

    computeOffsets();
  }
  private void configureComposite(int id, String token, String value){
    if(type.isData()) configureFile(id, token, value);
    else if(type.isLink()) configureLink(token, value);
  }
  private void configureLink(String token, String value){
    if(subTable == null){
      subTable = new Table();
      subTable.setRoot(false);
      subTable.setEnvironment((Environment) environment.clone());
    }

    if(token.equals("RSURL")){
      subTable.setConfigFileName(value);
    }else{
      subTable.configureItem("DAT", token, value);
    }

  }
  private void configureFile(int id, String token, String value){
    File dataFile = null;
    if(subDataFiles == null) subDataFiles = new ArrayList();
    if(id >= 0 && id < subDataFiles.size()) dataFile = (File) subDataFiles.get(id);
    if(dataFile == null){
      dataFile = new File();
      dataFile.setEnvironment((Environment) environment.clone());
      subDataFiles.add(id, dataFile);
    }
    dataFile.configureItem(null, token, value);
  }
  private void computeOffsets(){
    if(offsetField == null) return;
    offsetField.setOffset(offset);

    if(countField != null){
      countField.setOffset(offset + offsetField.getSize());
      if(packField != null){
        packField.setOffset(offset + offsetField.getSize() + countField.getSize());
      }
    }else if(packField != null){
      packField.setOffset(offset + offsetField.getSize());
    }
  }

  // common ---------------------------------------------------------------
  public String toString(){
    StringBuffer s = new StringBuffer();
    s.append(name);
    s.append("[");
    s.append(type);
    s.append("]");
    if(offsetField != null){
      s.append("(o:");
      s.append(offsetField);
    }
    if(countField != null){
      s.append(",");
      s.append("c:");
      s.append(countField);
    }
    if(packField != null){
      s.append(",");
      s.append("p:");
      s.append(packField);
    }
    if(subTable != null){
      s.append("st:");
      s.append(subTable);
    }
    if(offsetField != null) s.append(")");
    return s.toString();
  }

}
