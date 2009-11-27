/*****************************************************************************
 * 
 * @(#)FieldCollection.java  2009/03
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jin.dic.data.Environment;
import org.jin.dic.data.sk.i.FieldType;
import org.jin.dic.data.sk.i.IField;
import org.jin.dic.data.sk.i.IFieldCollection;
import org.jin.util.Logger;


public class FieldCollection implements IFieldCollection, Cloneable {

  private Environment environment = null;
  protected int       size        = 0;
  protected List      fieldList;
  protected Map       nameIndex;

  FieldCollection() {
    size = 0;
    fieldList = new ArrayList();
    nameIndex = new HashMap();
  }

  // configuration --------------------------------------------------------
  public void setEnvironment(Environment environment){
    this.environment = environment;
  }
  public void configureItem(String section, String token, String value){
    String[] temp = token.split(",");
    String fieldName = temp[0];
    IField field = null;
    if(temp.length == 1){
      field = new Field();
      ((Field) field).setEnvironment(environment);
      field.setPosition(fieldList.size());
      field.setName(fieldName);
      field.setType(new FieldType(value));
      field.setOffset(size);
      size += field.getSize();
      nameIndex.put(field.getName(), new Integer(field.getPosition()));
      fieldList.add(field);
      // TODO sort() by field->shared name?
    }else{
      field = getField(fieldName);
      if(field == null) Logger.err(new Exception("field unknown: " + fieldName));
      else{
        int oldSize = field.getSize();
        field.configureItem(null, temp[1], value);
        size = size + field.getSize() - oldSize;
      }
    }
  }
  public boolean check(){
    Field field;
    for(int i = 0; i < fieldList.size(); i++){
      field = (Field) fieldList.get(i);
      if(!field.check()) return false;
    }
    return true;
  }

  // ready to work --------------------------------------------------------
  public IFieldCollection getFldCollection(){
    return this;
  }
  public int getFieldCount(){
    return fieldList.size();
  }
  public IField getField(int index){
    return (IField) fieldList.get(index);
  }
  public IField getField(String fieldName){
    Integer index = (Integer) nameIndex.get(fieldName);
    IField field = null;
    if(index.intValue() != -1) field = (IField) fieldList.get(index.intValue());
    return field;
  }
  public int getSize(){
    return size;
  }

  // common ---------------------------------------------------------------
  public Object clone(){
    try{
      return super.clone();
    }catch(CloneNotSupportedException e){
      return null;
    }
  }
  public String toString(){
    StringBuffer s = new StringBuffer();
    int fieldCount = fieldList.size();
    s.append(fieldCount);
    s.append(" [");
    for(int i = 0; i < fieldCount; i++){
      s.append(getField(i));
    }
    s.append("]");
    return s.toString();
  }

}
