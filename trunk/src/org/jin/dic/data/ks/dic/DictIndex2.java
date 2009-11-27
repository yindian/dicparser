/*****************************************************************************
 * 
 * @(#)DictIndex2.java  2009/03
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
package org.jin.dic.data.ks.dic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.dic.data.ks.Index;
import org.jin.dic.data.ks.IndexItem;
import org.jin.util.io._ByteArrayOutputStream;
import org.jin.util.io._DataOutputStream;


class DictIndex2 implements Index {

  private int  length      = 0;
  private List itemsList   = new ArrayList();
  private int  size        = 0;
  private int  maxChars    = 0;

  private Map  offsetIndex = new HashMap();
  private void put(int offset, int index){
    offsetIndex.put(new Integer(offset), new Integer(index));
  }
  public int getMaxChars(){
    return maxChars;
  }
  public int getSize(){
    return size;
  }

  public void addItem(IndexItem item2){
    if(!(item2 instanceof DictIndex2Item)) return;
    item2.setOffset(length);
    put(item2.getOffset(), itemsList.size());
    itemsList.add(item2);
    int charLen = ((DictIndex2Item) item2).getData().length;
    length += (charLen + 2 + 4);
    maxChars = maxChars < charLen ? charLen : maxChars;
  }
  public void noMoreItems(){
    size = itemsList.size();
  }

  public IndexItem getItem(int index){
    return (DictIndex2Item) itemsList.get(index);
  }

  public int getLength(){
    return length;
  }

  public void read(DataInput in, int para) throws BadFormatException, IOException{
    if(in == null) throw new BadFormatException("no input");
    size = para;
    offsetIndex.clear();
    itemsList.clear();
    DictIndex2Item item2;
    int offset = 0;
    int index = 0;
    char c;
    _ByteArrayOutputStream bos = new _ByteArrayOutputStream(0x40);
    _DataOutputStream leos = new _DataOutputStream(bos, true);
    while(offset < size - 6){
      bos.reset();
      while((c = in.readChar()) != 0){
        leos.writeChar(c);
      }
      leos.close();
      item2 = new DictIndex2Item();
      item2.setWordIndex(in.readInt());
      item2.setData(bos.toByteArray());
      item2.setOffset(offset);
      itemsList.add(item2);
      index++;
      offset += (bos.size() + 4 + 2);
    }
    length = offset;
    size = itemsList.size();
  }

  public void write(DataOutput os) throws IOException{
    if(os == null) throw new IOException("no output");

    DictIndex2Item item2;
    int size = itemsList.size();
    for(int i = 0; i < size; i++){
      item2 = (DictIndex2Item) getItem(i);
      os.write(item2.getData());
      os.writeChar(0);
      os.writeInt(item2.getWordIndex());
    }
  }

  public void clear(){
    length = 0;
    itemsList.clear();
    size = 0;
    offsetIndex.clear();
  }

  public int offsetToIndex(int offset){
    int index = -1;
    Integer index_ = (Integer) offsetIndex.get(new Integer(offset));
    if(index_ != null) index = index_.intValue();
    return index;
  }

  public IndexItem getItemByOffset(int offset){
    return getItem(offsetToIndex(offset));
  }

}
