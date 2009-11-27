/*****************************************************************************
 * 
 * @(#)CacheService.java  2009/10
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
package org.jin.dic.data;

import java.util.HashMap;
import java.util.Map;

public class CacheService {

  private Map       valueMap;
  private Map       idIndexMap;
  private int       size = 5;
  private ClockNode clock[];
  private int       currentPos;

  public int getSize(){
    return size;
  }

  public void setSize(int size){
    this.size = size;
  }

  public int getCount(){
    return valueMap == null ? 0 : valueMap.size();
  }

  public Object getCache(int id){
    if(valueMap == null || idIndexMap == null) return null;
    if(clock == null) clock = new ClockNode[size];

    Integer itr = new Integer(id);
    Object obj = valueMap.get(itr);
    if(obj == null) return null;

    Integer idx = (Integer) idIndexMap.get(itr);
    clock[idx.intValue()].count++;
    return obj;
  }
  public void putCache(int id, Object cache){
    if(clock == null) clock = new ClockNode[size];
    if(valueMap == null) valueMap = new HashMap();
    if(idIndexMap == null) idIndexMap = new HashMap();

    ClockNode node;
    int size = clock.length;
    while(true){
      node = clock[currentPos];
      if(node == null) break;
      if(node.count == 0) break;
      node.count--;
      currentPos++;
      if(currentPos == size) currentPos = 0;
    }

    Integer itr = new Integer(id);
    if(node == null){
      node = (clock[currentPos] = new ClockNode());
      node.index = currentPos;
      node.id = itr;
      idIndexMap.put(itr, new Integer(currentPos));
    }else{
      valueMap.remove(node.id);
      idIndexMap.remove(node.id);
      node.id = itr;
      idIndexMap.put(itr, new Integer(currentPos));
    }
    currentPos++;
    if(currentPos == size) currentPos = 0;
    valueMap.put(itr, cache);
  }

  private class ClockNode {
    int     index;
    int     count;
    Integer id;
  }

  public void debug(){
    System.out.print("[");
    System.out.print(currentPos);
    System.out.print("/");
    System.out.print(size);
    System.out.print(",");
    System.out.print(clock.length);
    System.out.print("]");
    for(int i = 0; i < clock.length; i++){
      if(clock[i]==null)break;
      if(i != clock.length - 1) System.out.print(",");
      System.out.print("(");
      System.out.print(clock[i].count);
      System.out.print(")");
      System.out.print(valueMap.get(clock[i]==null?null:clock[i].id));
    }
    System.out.println();
  }
}
