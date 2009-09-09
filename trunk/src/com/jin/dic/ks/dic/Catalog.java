/*****************************************************************************
 * 
 * @(#)Catalog.java  2009/03
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
import com.jin.dic.ks.Element;

class Catalog implements Element {

  private int  size     = 0;
  private List itemList = null;

  public Catalog() {
  }
  public Catalog(int size) {
    itemList = new ArrayList();
    this.size = size;
    CatalogItem block;
    for(int i = 0; i < size; i++){
      block = new CatalogItem();
      block.setDeflatedLength(0);
      itemList.add(block);
    }
  }

  public int getLength(){
    return itemList.size() << 2;
  }
  public CatalogItem getItem(int index){
    if(itemList == null || index >= itemList.size()) return null;
    return (CatalogItem) itemList.get(index);
  }

  public void read(DataInput in, int length) throws BadFormatException, IOException{
    size = length;
    CatalogItem block = null;
    itemList = new ArrayList();
    int offset = 0;
    int deflatedLength;
    for(int i = 0; i < size; i++){
      deflatedLength = in.readInt();
      block = new CatalogItem();
      block.setDeflatedLength(deflatedLength);
      block.setOffset(offset);
      offset += deflatedLength;
      itemList.add(block);
    }
  }

  public void write(DataOutput out) throws IOException{
    CatalogItem block = null;
    for(int i = 0; i < size; i++){
      block = (CatalogItem) itemList.get(i);
      out.writeInt(block.getDeflatedLength());
    }
  }

  public int getSize(){
    return size;
  }

  public void setSize(int size){
    this.size = size;
  }

}
