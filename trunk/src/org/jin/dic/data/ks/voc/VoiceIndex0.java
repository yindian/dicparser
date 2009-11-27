/*****************************************************************************
 * 
 * @(#)VoiceIndex0.java  2009/03
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


class VoiceIndex0 implements Index {

  private List itemsList = new ArrayList();
  private int  size      = 0;

  public int getSize(){
    return size;
  }

  boolean inied = false;
  private void ini(int size){
    if(inied) return;
    itemsList.clear();
    for(int i = 0; i < size; i++)
      itemsList.add(null);
    inied = true;
    this.size = itemsList.size();
  }

  public void addItem(IndexItem item0){// index0 always update
    if(!(item0 instanceof VoiceIndex0Item)) return;
    ini(26 * 26 + 1);
    int index = ((VoiceIndex0Item) item0).getKey();
    if(index > itemsList.size() - 1) return;

    itemsList.set(index, item0);
    item0.setOffset(index << 1);
  }

  public void noMoreItems(){
    VoiceIndex0Item last = null, item;
    for(int i = itemsList.size() - 1; i > 0; i--){
      item = (VoiceIndex0Item) itemsList.get(i);
      if(item == null && last != null) itemsList.set(i, last.clone());
      if(item != null) last = item;
    }
  }

  public IndexItem getItem(int index){
    return (IndexItem) itemsList.get(index);
  }

  public int offsetToIndex(int offset){
    return offset >> 1;
  }

  public int getLength(){
    return size << 1;
  }

  public void read(DataInput in, int para) throws IOException, BadFormatException{
    if(in == null) throw new BadFormatException("no input");
    size = para;
    itemsList.clear();
    VoiceIndex0Item item0;
    int offset = 0;
    for(int i = 0; i < size; i++){
      item0 = new VoiceIndex0Item();
      item0.setKey(i);
      item0.setIndex1(in.readChar() & 0xffff);
      item0.setOffset(offset);
      itemsList.add(item0);
      offset += 2;
    }
  }

  public void write(DataOutput os) throws IOException{
    if(os == null) throw new IOException("no output");

    VoiceIndex0Item item0;
    int size = itemsList.size();
    for(int i = 0; i < size; i++){
      item0 = (VoiceIndex0Item) getItem(i);
      os.writeChar(item0.getIndex1());
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
