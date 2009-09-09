/*****************************************************************************
 * 
 * @(#)DictIndex2Item.java  2009/03
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

import com.jin.dic.ks.IndexItem;
import com.jin.util.StringUtil;

class DictIndex2Item implements IndexItem {

  private int    wordIndex = 0;   // index1 index
  private byte[] data      = null;
  private int    offset    = 0;

  public int getWordIndex(){
    return wordIndex;
  }
  public void setWordIndex(int wordIndex){
    this.wordIndex = wordIndex;
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
    s.append(Integer.toHexString(wordIndex));
    s.append(" ");
    s.append(StringUtil.valueOf(data));
    return s.toString();
  }
}
