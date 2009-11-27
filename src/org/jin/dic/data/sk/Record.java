/*****************************************************************************
 * 
 * @(#)Record.java  2009/03
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

import org.jin.dic.data.sk.i.IFieldCollection;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;


public class Record implements IRecord {
  private List             values;
  private int              id;
  private int              size;
  private IFieldCollection fldCollection;

  Record() {
    values = new ArrayList();
  }
  void setFldCollection(IFieldCollection fldCollection){
    size = fldCollection.getSize();
    this.fldCollection = fldCollection;
  }
  public int getSize(){
    return size;
  }
  public int getId(){
    return id;
  }
  void setId(int id){
    this.id = id;
  }
  public IFieldCollection getFldCollection(){
    return fldCollection;
  }
  public List getValues(){
    return values;
  }

  public long getNumFieldValue(String fieldName){
    Field field = (Field) fldCollection.getField(fieldName);
    Object value = values.get(field.getPosition());
    return ((Long) value).longValue();
  }
  public byte[] getDataFieldValue(String fieldName){
    Field field = (Field) fldCollection.getField(fieldName);
    Binary value = (Binary) values.get(field.getPosition());
    return value.getData();
  }
  public long getNumFieldValue(int pos){
    Object value = values.get(pos);
    return ((Long) value).longValue();
  }
  public byte[] getDataFieldValue(int pos){
    return ((Binary) values.get(pos)).getData();
  }
  public IRecordSet getLinkFieldValue(String fieldName){
    Field field = (Field) fldCollection.getField(fieldName);
    Object value = values.get(field.getPosition());
    return (IRecordSet) value;
  }
  public IRecordSet getLinkFieldValue(int pos){
    return (IRecordSet) values.get(pos);
  }

}
