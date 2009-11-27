/*****************************************************************************
 * 
 * @(#)RecordSet.java  2009/03
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

import org.jin.dic.data.sk.i.IField;
import org.jin.dic.data.sk.i.IFieldCollection;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.dic.data.sk.i.LookUpMode;
import org.jin.util.Logger;


/**
 * stand alone IRecordSet with all records loaded
 */
public class RecordSet implements IRecordSet {

  protected IFieldCollection fldCollection = null;
  protected List             records       = null;
  private IField             lookupField   = null;
  private boolean            isValid       = false;

  RecordSet() {
    records = new ArrayList();
  }
  void setFldCollection(IFieldCollection fldCollection){
    this.fldCollection = fldCollection;
    isValid = true;
  }
  void addRecord(IRecord record){
    records.add(record);
    if(!isValid) isValid = true;
  }

  public IField getLookUpfield(){
    return lookupField;
  }
  public void setLookUpfield(IField lookupField){
    this.lookupField = lookupField;
  }
  public void bind(){
    throw new UnsupportedOperationException("don't bind here");
  }
  public IFieldCollection getFldCollection(){
    return fldCollection;
  }
  public IRecord getRecord(int index){
    return (IRecord) records.get(index);
  }
  public int getRecordCount(){
    return records.size();
  }
  public IRecordSet getSubRecordSet(int off, int count){
    throw new UnsupportedOperationException();// TODO
  }
  public boolean isValid(){
    return isValid;
  }
  public void unBind(boolean keepDataAvailable){
    if(!keepDataAvailable){
      Logger.err(new Exception("you can only unBind with dataAvaliable"));
      return;
    }
    int recordCount = records.size();
    IRecord record;
    IField field;
    int fldCount = fldCollection.getFieldCount();
    for(int i = 0; i < fldCount; i++){
      field = fldCollection.getField(i);
      if(field.isData()){
        for(int j = 0; j < recordCount; j++){
          record = getRecord(j);
          ((Binary) ((Record) record).getValues().get(i)).load();
        }
      }else if(field.isLink()){
        for(int j = 0; j < recordCount; j++){
          record = getRecord(j);
          record.getLinkFieldValue(i).unBind(true);
        }
      }
    }
  }

  public int lookUpNum(long search, LookUpMode mode){
    throw new UnsupportedOperationException();// TODO
  }

  public int lookUpText(String search, LookUpMode mode){
    throw new UnsupportedOperationException();// TODO
  }

}
