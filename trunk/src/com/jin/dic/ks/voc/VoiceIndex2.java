/*****************************************************************************
 * 
 * @(#)VoiceIndex2.java  2009/03
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
package com.jin.dic.ks.voc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jin.dic.ks.BadFormatException;
import com.jin.dic.ks.Index;
import com.jin.dic.ks.IndexItem;

class VoiceIndex2 implements Index {

  private int  length      = 0;
  private List itemsList   = new ArrayList();
  private int  size        = 0;

  private Map  offsetIndex = new HashMap();

  private void put(int offset, int index){
    offsetIndex.put(new Integer(offset), new Integer(index));
  }

  public int getSize(){
    return size;
  }

  public void addItem(IndexItem item2){
    if(!(item2 instanceof VoiceIndex2Item)) return;
    item2.setOffset(length);
    put(item2.getOffset(), itemsList.size());
    itemsList.add(item2);
    length += (1 + ((VoiceIndex2Item) item2).getData().length + 4);
  }

  public void noMoreItems(){
    size = itemsList.size();
  }

  public IndexItem getItem(int index){
    return (IndexItem) itemsList.get(index);
  }

  public int offsetToIndex(int offset){
    int index = -1;
    Integer index_ = (Integer) offsetIndex.get(new Integer(offset));
    if(index_ != null) index = index_.intValue();
    return index;
  }

  public int getLength(){
    return length;
  }

  public void read(DataInput in, int para) throws BadFormatException, IOException{
    if(in == null) throw new BadFormatException("no input");
    size = para;
    offsetIndex.clear();
    itemsList.clear();
    VoiceIndex2Item item2;
    int offset = 0;
    int wordLen;
    byte[] buf;
    int index = 0;
    while(offset < size){
      wordLen = in.readUnsignedByte();
      buf = new byte[wordLen];
      in.readFully(buf, 0, wordLen);
      item2 = new VoiceIndex2Item();
      item2.setDataIndex(in.readInt());
      item2.setData(buf);
      item2.setOffset(offset);
      addItem(item2);
      index++;
      offset += (1 + wordLen + 4);
    }
    length = offset;
    put(size, --index);
    size = itemsList.size();
  }

  public void write(DataOutput os) throws IOException{
    if(os == null) throw new IOException("no output");

    VoiceIndex2Item item2;
    int size = itemsList.size();
    int wordLen;
    byte[] buf;
    int index = 0;
    for(int i = 0; i < size; i++){
      item2 = (VoiceIndex2Item) getItem(i);
      buf = item2.getData();
      wordLen = buf.length;
      index = item2.getDataIndex();
      os.write(wordLen);
      os.write(buf);
      os.writeInt(index);
    }
  }

  public void clear(){
    length = 0;
    itemsList.clear();
    size = 0;
    offsetIndex.clear();
  }
  public IndexItem getItemByOffset(int offset){
    return getItem(offsetToIndex(offset));
  }
}
