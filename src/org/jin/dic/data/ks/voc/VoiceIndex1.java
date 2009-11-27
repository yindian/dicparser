/*****************************************************************************
 * 
 * @(#)VoiceIndex1.java  2009/03
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
package org.jin.dic.data.ks.voc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.dic.data.ks.Index;
import org.jin.dic.data.ks.IndexItem;


class VoiceIndex1 implements Index {

  private List itemsList = new ArrayList();
  private int  size      = 0;

  public int getSize(){
    return size;
  }

  public void addItem(IndexItem item1){
    if(!(item1 instanceof VoiceIndex1Item)) return;
    itemsList.add(item1);
    item1.setOffset(size << 2);
    this.size = itemsList.size();
  }

  public void noMoreItems(){
    VoiceIndex1Item item1 = new VoiceIndex1Item();
    item1.setKey(0xfff);
    item1.setIndex2Offset(0xfff);
    addItem(item1);
  }

  public IndexItem getItem(int index){
    return (IndexItem) itemsList.get(index);
  }

  public int offsetToIndex(int offset){
    return offset >> 2;
  }

  public int getLength(){
    return size << 2;
  }

  public void read(DataInput in, int para) throws IOException, BadFormatException{
    if(in == null) throw new BadFormatException("no input");
    size = para;
    itemsList.clear();
    VoiceIndex1Item item1;
    int offset = 0;
    int temp;
    for(int i = 0; i < size; i++){
      temp = in.readInt();
      item1 = new VoiceIndex1Item();
      item1.setKey(temp & 0x00000fff);
      item1.setIndex2Offset((temp & 0xfffff000) >> 12);
      item1.setOffset(offset);
      itemsList.add(item1);
      offset += 4;
    }
  }

  public void write(DataOutput os) throws IOException{
    if(os == null) throw new IOException("no output");

    VoiceIndex1Item item1;
    int size = itemsList.size();
    int temp;
    for(int i = 0; i < size; i++){
      item1 = (VoiceIndex1Item) getItem(i);
      temp = item1.getKey() | (item1.getIndex2Offset() << 12);
      os.writeInt(temp);
    }
  }

  public void clear(){
    itemsList.clear();
    size = 0;
  }

  public IndexItem getItemByOffset(int offset){
    return getItem(offsetToIndex(offset));
  }

}
