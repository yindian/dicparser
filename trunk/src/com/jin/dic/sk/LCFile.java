/*****************************************************************************
 * 
 * @(#)LCFile.java  2009/03
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
package com.jin.dic.sk;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jin.dic.sk.i.IField;
import com.jin.util.Logger;

class LCFile extends File {

  private boolean oldFormat   = false; // TODO what dose old format looks like?
  private List    items;
  private IField  lookupField = null;

  public void setLookupField(IField lookupField){
    this.lookupField = lookupField;
  }

  // configuration --------------------------------------------------------
  public void configureItem(String section, String name, String value){
    if(name.equals("OLDFORMAT")){
      oldFormat = !value.equalsIgnoreCase("FALSE");
      if(oldFormat) Logger.err(new Exception("Old Format!!!"));
    }else super.configureItem(section, name, value);
  }

  // load -----------------------------------------------------------------
  public void open(){
    if(isValid) return;
    java.io.File f = environment.getFile(fileName);
    try{
      FileInputStream fis = new FileInputStream(f);
      BufferedReader br = new BufferedReader(new InputStreamReader(fis, "ISO8859-1"));
      items = new ArrayList();
      LCItem item;
      String line;
      String[] tmp;
      boolean isNum = lookupField.isNum();
      while((line = br.readLine()) != null){
        tmp = line.split("\t");
        if(tmp == null || tmp.length != 4){
          continue;
        }
        item = new LCItem();
        if(!isNum){
          item.firstKey = tmp[0];
          item.nextKey = tmp[1];
        }else{
          item.firstLong = Long.valueOf(tmp[0]).longValue();
          item.nextLong = Long.valueOf(tmp[1]).longValue();
        }
        item.pageIndex = items.size();
        items.add(item);
      }
      br.close();
      fileSize = (int) f.length();
      isValid = true;
    }catch(IOException e){
      Logger.err(e);
      isValid = false;
    }
  }
  public boolean isOpened(){
    return isValid;
  }
  public void close(){
  }

  // ready to work --------------------------------------------------------
  public int lookUp(String search){
    if(!isValid) throw new IllegalStateException(INVALID);
    boolean isNum = lookupField.isNum();
    LCItem item;
    long lSearch = 0;
    if(isNum){
      try{
        lSearch = Long.valueOf(search).longValue();
      }catch(NumberFormatException e){
        return -1;
      }
    }
    for(int i = 0; i < items.size(); i++){
      item = (LCItem) items.get(i);
      if(isNum){
        if(lSearch >= item.firstLong && lSearch <= item.nextLong) return item.pageIndex;
      }else{
        if(search.compareToIgnoreCase(item.firstKey) >= 0
            && search.compareToIgnoreCase(item.nextKey) <= 0){
          return item.pageIndex;
        }
      }
    }
    return -1;
  }

  private class LCItem {
    String firstKey;
    long   firstLong;
    String nextKey;
    long   nextLong;
    int    pageIndex;
  }

}
