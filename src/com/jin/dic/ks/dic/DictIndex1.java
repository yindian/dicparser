/*****************************************************************************
 * 
 * @(#)DictIndex1.java  2009/03
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
package com.jin.dic.ks.dic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jin.dic.ks.BadFormatException;
import com.jin.dic.ks.Index;
import com.jin.dic.ks.IndexItem;

class DictIndex1 implements Index {

  private List itemsList = new ArrayList();
  private int  size      = 0;

  public int getSize(){
    return size;
  }

  public void addItem(IndexItem item1){
    if(!(item1 instanceof DictIndex1Item)) return;
    itemsList.add(item1);
    item1.setOffset(size << 3);
    this.size = itemsList.size();
  }

  public void noMoreItems(){
    // IndexItem item = new IndexItem();
    // item.setKey(new IndexItemData(0xfff));
    // item.setValue(new IndexItemData(0xfff));
    // addItem(item);
  }

  public IndexItem getItem(int index){
    return (DictIndex1Item) itemsList.get(index);
  }

  public int getLength(){
    return size << 3;
  }

  public void read(DataInput in, int para) throws IOException, BadFormatException{
    if(in == null) throw new BadFormatException("no input");
    size = para;
    itemsList.clear();
    DictIndex1Item item1;
    int offset = 0;
    for(int i = 0; i < size; i++){
      item1 = new DictIndex1Item();
      item1.setIndex2Offset(in.readInt());
      item1.setDictDataOffset(in.readInt());
      item1.setOffset(offset);
      itemsList.add(item1);
      offset += 8;
    }
  }

  public void write(DataOutput os) throws IOException{
    if(os == null) throw new IOException("no output");

    DictIndex1Item item1;
    int size = itemsList.size();
    for(int i = 0; i < size; i++){
      item1 = (DictIndex1Item) getItem(i);
      os.writeInt(item1.getIndex2Offset());
      os.writeInt(item1.getDictDataOffset());
    }
  }

  public void clear(){
    itemsList.clear();
    size = 0;
  }

  public int offsetToIndex(int offset){
    return offset >> 3;
  }
  public IndexItem getItemByOffset(int offset){
    return getItem(offsetToIndex(offset));
  }

}
