/*****************************************************************************
 * 
 * @(#)CacheService.java  2009/03
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
package com.jin.dic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CacheService {

  private int size = 5;
  private Map df;

  public int getSize(){
    return size;
  }

  public void setSize(int size){
    this.size = size;
    trimCache(size);
  }

  public int getCount(){
    return df == null ? 0 : df.size();
  }

  public Iterator getIterator(){
    return df == null ? null : df.entrySet().iterator();
  }

  public Object getCache(int id){
    if(df == null) return null;
    return df.get(new Integer(id));
  }

  public void putCache(int id, Object cache){
    if(df == null) df = new LinkedHashMap();
    df.put(new Integer(id), cache);
    trimCache(size);
  }

  private void trimCache(int newSize){
    if(df == null) return;
    if(newSize < 0) df.clear();
    else{
      while(df.size() > newSize){
        Iterator i = df.entrySet().iterator();
        if(i.hasNext()){
          i.next();
          i.remove();
        }
      }
    }
  }

}
