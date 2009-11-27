/*****************************************************************************
 * 
 * @(#)VoiceIndex0Item.java  2009/03
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

import org.jin.dic.data.ks.IndexItem;

class VoiceIndex0Item implements IndexItem {

  private int key;
  private int index1 = 0;
  private int offset = 0;

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

  public int getIndex1(){
    return index1;
  }

  public void setIndex1(int index1){
    this.index1 = index1;
  }

  public Object clone(){
    VoiceIndex0Item item0 = new VoiceIndex0Item();
    item0.key = key;
    item0.index1 = index1;
    item0.offset = offset;
    return item0;
  }
  public String toString(){
    StringBuffer s = new StringBuffer();
    s.append(Integer.toHexString(key));
    s.append(" ");
    s.append(Integer.toHexString(index1));
    return s.toString();
  }
}
