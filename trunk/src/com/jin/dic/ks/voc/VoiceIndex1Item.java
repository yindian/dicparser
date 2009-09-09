/*****************************************************************************
 * 
 * @(#)VoiceIndex1Item.java  2009/03
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

import com.jin.dic.ks.IndexItem;

class VoiceIndex1Item implements IndexItem {

  private int key;
  private int index2Offset = 0;
  private int offset       = 0;

  public int getOffset(){
    return offset;
  }

  public void setOffset(int offset){
    this.offset = offset;
  }

  public int getKey(){
    return key;
  }

  public void setKey(int key){
    this.key = key;
  }

  public int getIndex2Offset(){
    return index2Offset;
  }

  public void setIndex2Offset(int index2Offset){
    this.index2Offset = index2Offset;
  }

  public Object clone(){
    VoiceIndex1Item item1 = new VoiceIndex1Item();
    item1.key = key;
    item1.index2Offset = index2Offset;
    item1.offset = offset;
    return item1;
  }
  public String toString(){
    StringBuffer s = new StringBuffer();
    s.append(Integer.toHexString(key));
    s.append(" ");
    s.append(Integer.toHexString(index2Offset));
    return s.toString();
  }
}
