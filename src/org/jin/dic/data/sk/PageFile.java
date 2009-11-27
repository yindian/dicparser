/*****************************************************************************
 * 
 * @(#)PageFile.java  2009/03
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

import java.io.IOException;
import java.util.List;

import org.jin.dic.data.CacheService;
import org.jin.dic.data.Environment;
import org.jin.dic.data.sk.i.FieldType;
import org.jin.dic.data.sk.i.IField;
import org.jin.dic.data.sk.i.IFieldCollection;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordDataSource;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.Logger;
import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._DataInput;
import org.jin.util.io._DataInputStream;


class PageFile extends File implements IRecordDataSource {

  static final int         DEFAULTPAGESIZE = 256;

  private IFieldCollection fldCollection   = new FieldCollection();
  // the super file has already cached data, useless here
  private CacheService     recordsCache    = null;

  private int              maxCacheSize    = DEFAULTPAGESIZE;
  private int              pageSize        = DEFAULTPAGESIZE;
  private int              pageCount       = -1;
  private int              recordSize      = -1;
  private int              recordCount     = -1;

  private boolean          standAlone      = false;

  PageFile() {
  }
  // getter and setter ----------------------------------------------------
  public IFieldCollection getFldCollection(){
    return fldCollection;
  }
  public int getRecordCount(){
    return recordCount;
  }
  public int getPageSize(){
    return pageSize;
  }
  public int getPageCount(){
    return pageCount;
  }
  public int getRecordSize(){
    return recordSize;
  }

  // configuration --------------------------------------------------------
  public void setEnvironment(Environment envirment){
    super.environment = envirment;
    ((FieldCollection) fldCollection).setEnvironment(envirment);
  }
  public void configureItem(String section, String name, String value){
    if(name.equals("PAGESIZE")){
      pageSize = Integer.valueOf(value).intValue();
    }else if(name.equals("MAXCACHESIZE")){
      maxCacheSize = Integer.valueOf(value).intValue();
      if(recordsCache == null) recordsCache = new CacheService();
      recordsCache.setSize(maxCacheSize);// TODO
    }else if(name.charAt(0) == Field.FIELD_ESCAPE_CHAR){
      ((FieldCollection) fldCollection).configureItem(null, name.substring(1), value);
    }else super.configureItem(section, name, value);
  }
  public IField getField(String fieldName){
    return fldCollection.getField(fieldName);
  }

  // load -----------------------------------------------------------------
  public void open(){
    super.open();
    if(!isValid) return;
    recordSize = fldCollection.getSize();
    if(recordSize != 0){
      recordCount = fileSize / recordSize;
      pageCount = (recordCount - 1) / pageSize + 1;
      dataFragmentSize = recordSize * pageSize;
      if(recordsCache == null) recordsCache = new CacheService();
    }
  }
  // ready to work --------------------------------------------------------
  public IRecord getRecord(int index){
    if(!isValid) throw new IllegalStateException(INVALID);
    if(index >= recordCount) return null;
    Record record = (Record) getRecordPrvt(index);
    Record recordN = (Record) getRecordPrvt(index + 1);
    IField field;
    Binary data, dataN;
    for(int i = 0; i < fldCollection.getFieldCount(); i++){
      field = fldCollection.getField(i);
      int first, next;
      if(field.isData() && field.getSubCountField() == null){
        File subDataFile = (File) field.getDataSource(0);// FIXME 0?
        data = (Binary) record.getValues().get(i);
        first = data.getOffset();
        if(recordN == null) next = subDataFile.fileSize;
        else{
          dataN = (Binary) recordN.getValues().get(i);
          next = dataN.getOffset();
        }
        data.setLength(next - first);
        if(standAlone) data.load();
      }else if(field.isLink() && field.getSubCountField() == null){
        Table st = (Table) record.getValues().get(i);
        first = st.getFirstId();
        if(recordN == null) next = st.getDataFile().recordCount;
        else next = ((Table) recordN.getValues().get(i)).getFirstId();
        st.setRecordCount(next - first);
      }
    }
    record.setFldCollection(fldCollection);
    return record;
  }
  public IRecordSet getRecordSet(int index, int count){
    if(!isValid) throw new IllegalStateException(INVALID);
    if(index < 0 || count < 0 || index > recordCount || index + count > recordCount) return null;
    IFieldCollection fldCollection = (IFieldCollection) this.fldCollection.clone();
    RecordSet recordSet = new RecordSet();
    recordSet.setFldCollection(fldCollection);

    int maxCount = recordCount - index;
    count = count > maxCount ? maxCount : count;
    if(count == 0) count = maxCount;
    if(count > maxCacheSize) count = maxCacheSize;
    Record record;
    for(int i = 0; i < count; i++){
      record = (Record) getRecord(index + i);
      record.setFldCollection(fldCollection);
      record.setId(index + i);
      recordSet.addRecord(record);
    }

    return recordSet;
  }
  public IRecord[] getRecords(int pageIndex){
    IRecord[] records;
    int index = pageIndex * pageSize;
    int count = pageSize;
    if(count + index >= recordCount) count = recordCount - index;
    if(count < 0) count = 0;
    records = new Record[count];
    for(int i = 0; i < count; i++){
      records[i] = (Record) getRecord(index + i);
    }
    return records;
  }
  private IRecord getRecordPrvt(int index){
    int pageNum = index / pageSize;
    int pageOffset = index % pageSize;
    IRecord[] page = getPage(pageNum);
    if(page == null || pageOffset >= page.length) return null;
    else return page[pageOffset];
  }
  private IRecord[] getPage(int pageIndex){
    IRecord[] page = (IRecord[]) recordsCache.getCache(pageIndex);
    if(page == null){
      page = loadPage(pageIndex);
      recordsCache.putCache(pageIndex, page);
    }
    return page;
  }
  private IRecord[] loadPage(int pageIndex){
    IRecord[] page = getRecords(pageIndex * pageSize, pageSize);
    return page;
  }
  private IRecord[] getRecords(int index, int count){
    IRecord[] records = new IRecord[0];
    if(index >= recordCount) return records;
    try{
      int maxCount = recordCount - index;
      count = count > maxCount ? maxCount : count;
      if(count == 0) count = maxCount;
      byte[] buf = getData(index * recordSize, count * recordSize);

      _DataInput di = new _DataInputStream(new _ByteArrayInputStream(buf), true);
      records = new Record[count];
      Record record;
      for(int i = 0; i < count; i++){
        record = new Record();
        record.setId(index + i);
        List values = ((Record) record).getValues();
        Field field;
        Object value = null;
        for(int j = 0; j < fldCollection.getFieldCount(); j++){
          field = (Field) fldCollection.getField(j);
          value = getValue(di, field);
          values.add(value);
        }
        records[i] = record;
      }
    }catch(IOException e){
      Logger.err(e);
      records = null;
    }
    return records;
  }
  private Object getValue(_DataInput di, IField field) throws IOException{
    Object value = null;
    if(field != null){
      switch(field.getType().getIntValue()){
        case FieldType.SKFT_SBYTE:
          value = new Long(di.readByte());
          break;
        case FieldType.SKFT_SSHORT:
          value = new Long(di.readShort());
          break;
        case FieldType.SKFT_S24:
          value = new Long(di.read24());
          break;
        case FieldType.SKFT_SLONG:
          value = new Long(di.readInt());
          break;
        case FieldType.SKFT_UBYTE:
          value = new Long(di.readUnsignedByte());
          break;
        case FieldType.SKFT_USHORT:
          value = new Long(di.readUnsignedShort());
          break;
        case FieldType.SKFT_U24:
          value = new Long(di.readUnsigned24());
          break;
        case FieldType.SKFT_ULONG:
          value = new Long(di.readUnsignedInt());
          break;
        case FieldType.SKFT_DATA: {
          Long offsetL = (Long) getValue(di, field.getSubOffsetField());
          Long countL = (Long) getValue(di, field.getSubCountField());
          Long packL = (Long) getValue(di, field.getSubPackField());
          if(offsetL == null){
            Logger.err(new Exception("DATA filed without offset! ") + field.getName());
          }else{
            if(packL != null){
              Logger.err(new Exception("need implement pack field:") + field.getName());// TODO
            }
            checkValue(field, offsetL);
            File subDataFile = (File) field.getDataSource(0);// FIXME 0?
            Binary data = new Binary();
            data.setDataFile(subDataFile);
            data.setOffset(offsetL.intValue());
            data.setStandAlone(false);
            data.setLength(countL == null ? 0 : countL.intValue());
            value = data;
          }
          break;
        }
        case FieldType.SKFT_LINK: {
          Long offsetL = (Long) getValue(di, field.getSubOffsetField());
          Long countL = (Long) getValue(di, field.getSubCountField());
          if(offsetL == null){
            Logger.err(new Exception("DATA filed without offset! ") + field.getName());
          }else{
            checkValue(field, offsetL);
            Table table = (Table) ((Table) field.getLinkRecordSet()).clone();
            table.setFirstId(offsetL.intValue());
            if(countL != null){
              checkValue(field, countL);
              table.setRecordCount(countL.intValue());
            }else table.setRecordCount(0);
            value = table;
          }
          break;
        }
      }
    }
    return value;
  }
  private void checkValue(IField field, Long value){
    if(value.longValue() > Integer.MAX_VALUE){
      Logger.err(new Exception("value overflows: ") + field.getName() + ":" + value.longValue());
    }
  }

}
