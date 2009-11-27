/*****************************************************************************
 * 
 * @(#)VoiceIndex2Item.java  2009/03
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

class VoiceIndex2Item implements IndexItem {

  private int    dataOffset = 0;   // offset in data file
  private byte[] data       = null;
  private int    offset     = 0;

  public int getDataIndex(){
    return dataOffset;
  }
  public void setDataIndex(int wordIndex){
    this.dataOffset = wordIndex;
  }
  public byte[] getData(){
    return data;
  }
  public void setData(byte[] data){
    this.data = data;
  }
  public int getOffset(){
    return offset;
  }
  public void setOffset(int offset){
    this.offset = offset;
  }
  public String toString(){
    StringBuffer s = new StringBuffer();
    s.append(new String(data));
    s.append(" ");
    s.append(Integer.toHexString(dataOffset));
    return s.toString();
  }
}
