/*****************************************************************************
 * 
 * @(#)DictIndex0Item.java  2009/03
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
package org.jin.dic.data.ks.dic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jin.dic.data.ks.IndexItem;
import org.jin.util.Logger;


class DictIndex0Item implements IndexItem {

  /*****************************************************************************
   * bean properties: childrenSize can be childrenItemMap.size() or items from
   * index2Offset calculated by calling all path numbers
   ****************************************************************************/
  private char c            = 0;
  private int  childrenSize = NO_INDEX;
  private int  index0Offset = 0;

  public void setChar(char c){
    this.c = c;
  }

  public char getChar(){
    return c;
  }

  public int getChildrenSize(){
    if(childrenSize == NO_INDEX) return childrenItemMap.size();
    return childrenSize;
  }

  public void setChildrenSize(int childrenSize){
    this.childrenSize = childrenSize;
  }

  /**
   * if the highest bit is set, the value is the offset in index2 else value is
   * offset in index0
   */
  public int getValue(){
    if(index2Offset != NO_INDEX) return index2Offset | 0x80000000;
    if(firstChildIndex0Offset != NO_CHILDREN) return firstChildIndex0Offset;
    return NO_INDEX;
  }

  public void setValue(int value){
    firstChildIndex0Offset = value;
  }

  public int getOffset(){
    return index0Offset;
  }

  public void setOffset(int offset){
    index0Offset = offset;
  }

  public String toString(){
    StringBuffer s = new StringBuffer();
    s.append(c);
    s.append(" ");
    s.append(Integer.toHexString(getChildrenSize()));
    s.append(" ");
    s.append(Integer.toHexString(getValue()));
    return s.toString();
  }

  /*****************************************************************************
   * really private
   ****************************************************************************/
  private int      minwords               = Header.MINWORDS;
  static final int NO_CHILDREN            = -1;
  static final int NO_INDEX               = -1;
  static final int GET_CHILDREN_NUM       = 0;
  static final int REMOVE_SMALLITEMS      = 1;
  static final int GET_OFFSET             = 2;
  static final int GET_1STCLD_OFFSET      = 3;
  static final int GET_PATH_NUM           = 4;
  static final int GET_LIST               = 5;
  static final int PRINT                  = 8;

  private Map      childrenItemMap        = new LinkedHashMap();
  private int      index2Offset           = NO_INDEX;
  private int      firstChildIndex0Offset = NO_CHILDREN;

  private void removeChildren(){
    index2Offset = getLeaf().index2Offset;
    childrenItemMap.clear();
  }

  private DictIndex0Item getLeaf(){// left or right most single leaf
    if(childrenItemMap.size() == 0) return this;
    else return ((DictIndex0Item) childrenItemMap.values().iterator().next()).getLeaf();
  }

  private DictIndex0Item getItem(char c){// if none in map, put a new one
    Character cr = new Character(Character.toUpperCase(c));
    DictIndex0Item child = (DictIndex0Item) childrenItemMap.get(cr);
    if(child == null){
      child = new DictIndex0Item();
      child.setChar(c);
      childrenItemMap.put(cr, child);
    }
    return child;
  }

  public void addBranchPath(String word, int offset){
    char c;
    DictIndex0Item child = this;
    int len = word.length();
    for(int i = 0; i <= len; i++){
      c = i == len ? 0 : word.charAt(i);
      child = child.getItem(c);
      if(i == len) child.index2Offset = offset;
    }
  }

  int deep;
  int offset_;
  void prepare(){// ready for writing to stream
    offset_ = index0Offset;
    traverse(this, REMOVE_SMALLITEMS, null);
    traverse(this, GET_OFFSET, null);
    traverse(this, GET_1STCLD_OFFSET, null);
  }

  int traverse(DictIndex0Item item, int type, List itemsList){
    int result = 0;
    Iterator i = item.childrenItemMap.values().iterator();
    DictIndex0Item child;
    boolean firstChild = true;
    int childrenNum = item.childrenItemMap.size();
    int pathNum;
    while(i.hasNext()){
      child = (DictIndex0Item) i.next();
      switch(type){
        case GET_CHILDREN_NUM:
          result = childrenNum;
          break;
        case GET_OFFSET:
          offset_ += 8;
          child.index0Offset = offset_;
          break;
        case GET_1STCLD_OFFSET:
          if(firstChild){
            item.firstChildIndex0Offset = child.index0Offset;
            firstChild = false;
          }
          break;
        case GET_PATH_NUM:

          break;
        case GET_LIST:
          itemsList.add(child);
          break;
        case REMOVE_SMALLITEMS:
          pathNum = traverse(child, GET_PATH_NUM, itemsList);
          if(pathNum <= minwords){
            child.childrenSize = pathNum;
            child.removeChildren();
          }
          break;
        case PRINT:
          print(child);
      }
    }

    i = item.childrenItemMap.values().iterator();
    while(i.hasNext()){
      deep++;
      child = (DictIndex0Item) i.next();
      result += traverse(child, type, itemsList);
    }
    if(item.childrenItemMap.size() == 0 && type == GET_PATH_NUM) result = 1;

    deep--;
    return result;
  }

  void print(DictIndex0Item child){
    Logger.info_("[");
    Logger.info_(getPadded(Integer.toHexString(child.index0Offset), 5));
    Logger.info_("]");
    Logger.info_(getPadded("", deep << 1));
    Logger.info_("'");
    Logger.info_(child.c == 0 ? '□' : child.c);
    Logger.info_("'(");
    Logger.info_(getPadded(Integer.toHexString(child.getChildrenSize()), 2));
    Logger.info_(")_[");
    Logger.info_(getPadded(Integer.toHexString(child.getValue()), 8));
    Logger.info("]");
  }

  private String getPadded(String string, int len){
    StringBuffer s = new StringBuffer(string);
    while(s.length() < len)
      s.insert(0, " ");
    return s.toString().toUpperCase(Locale.ENGLISH);
  }

  public void debug(){
    deep = 0;
    traverse(this, PRINT, null);
  }

  public static void main(String[] args){
    DictIndex0Item root = new DictIndex0Item();
    root.minwords = 1;
    root.setChar((char) 0);
    root.addBranchPath("A", 0x00);
    root.addBranchPath("AC", 0x10);
    root.addBranchPath("ACH", 0x20);
    root.addBranchPath("AD", 0x30);
    root.addBranchPath("ADG", 0x40);
    root.addBranchPath("B", 0x50);
    root.addBranchPath("BE", 0x60);
    root.addBranchPath("BF", 0x70);
    root.addBranchPath("BFI", 0x80);
    root.addBranchPath("T", 0x90);
    root.addBranchPath("TI", 0xa0);
    root.prepare();
    root.debug();
  }

}
