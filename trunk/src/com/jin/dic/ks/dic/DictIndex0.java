/*****************************************************************************
 * 
 * @(#)DictIndex0.java  2009/03
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

class DictIndex0 implements Index {

  private List itemsList = new ArrayList();
  private int  size      = 0;

  public int getSize(){
    return size;
  }

  public void addItem(IndexItem item0){
    if(itemsList.size() == 0) itemsList.add(item0);
  }

  public void noMoreItems(){
    DictIndex0Item root = (DictIndex0Item) getItem(0);
    root.prepare();
    root.traverse(root, DictIndex0Item.GET_LIST, itemsList);
    size = itemsList.size();
  }

  public IndexItem getItem(int index){
    return (DictIndex0Item) itemsList.get(index);
  }

  public int getLength(){
    return size << 3;
  }

  public void read(DataInput in, int para) throws IOException, BadFormatException{
    if(in == null) throw new BadFormatException("no input");
    size = para;
    itemsList.clear();
    DictIndex0Item item0;
    int offset = 0;
    for(int i = 0; i < size; i++){
      item0 = new DictIndex0Item();
      item0.setChar(in.readChar());
      item0.setChildrenSize(in.readChar());
      item0.setValue(in.readInt());
      item0.setOffset(offset);
      itemsList.add(item0);
      offset += 8;
    }
  }// by Bug_maker

  public void write(DataOutput os) throws IOException{
    if(os == null) throw new IOException("no output");

    DictIndex0Item item0;
    int size = itemsList.size();
    for(int i = 0; i < size; i++){
      item0 = (DictIndex0Item) getItem(i);
      os.writeChar(item0.getChar());
      os.writeChar(item0.getChildrenSize());
      os.writeInt(item0.getValue());
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
