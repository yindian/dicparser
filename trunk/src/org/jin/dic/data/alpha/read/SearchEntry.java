/*****************************************************************************
 * 
 * @(#)SearchEntry.java  2009/11
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
package org.jin.dic.data.alpha.read;

import java.io.IOException;

import org.jin.dic.data.DataSource;
import org.jin.util.Logger;
import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._DataInput;
import org.jin.util.io._DataInputStream;

public class SearchEntry {

  private DataSource dataSource;
  private int        baseOffset;

  private int        dataOffset;
  private boolean    caseSensitive;
  private boolean    lowerCase;

  public void setDataSource(DataSource dataSource){
    this.dataSource = dataSource;
  }
  public void setBaseOffset(int baseOffset){
    this.baseOffset = baseOffset;
  }
  public void setDataOffset(int dataOffset){
    this.dataOffset = dataOffset;
  }
  public void setCaseSensitive(boolean caseSensitive){
    this.caseSensitive = caseSensitive;
  }
  public void setLowerCase(boolean lowerCase){
    this.lowerCase = lowerCase;
  }

  private _ByteArrayInputStream buf = new _ByteArrayInputStream(new byte[0]);
  private _DataInput            di  = new _DataInputStream(buf, true);

  public byte[] search(String input){
    if(dataSource == null) return new byte[0];
    byte[] result;
    try{
      int offset = getDataOffset(input);
      int absDataOffset = baseOffset + dataOffset + offset;
      buf.reset(dataSource.getData(absDataOffset, 4));
      int len = di.readInt();
      result = dataSource.getData(absDataOffset + 4, len);
    }catch(IOException e){
      Logger.err(e);
      result = new byte[0];
    }
    return result;
  }
  public int getDataOffset(String word){
    String searchWrd = caseSensitive ? word : lowerCase ? word.toLowerCase() : word.toUpperCase();
    int searchLen = searchWrd.length();
    int value = -1;
    boolean found;

    int childrenOffset = 0;
    int childrenSize = 0;
    char preNode, srch;
    byte[] data;
    byte[] childrenData;
    data = dataSource.getData(baseOffset, 8);
    buf.reset(data);
    try{
      for(int i = 0; i <= searchLen; i++){
        srch = i == searchLen ? 0 : searchWrd.charAt(i);
        if(data.length == 0) break;
        buf.reset();
        preNode = di.readChar();
        childrenSize = di.readChar();
        childrenOffset = di.readInt();

        if(childrenOffset >= dataOffset) break;
        childrenData = dataSource.getData(baseOffset + childrenOffset, childrenSize << 3);
        buf.reset(childrenData);
        found = false;
        for(int j = 0; j < childrenSize; j++){
          buf.mark(-1);
          preNode = di.readChar();
          buf.skip(2);
          if(preNode == srch || (!caseSensitive && Character.toLowerCase(preNode) == Character.toLowerCase(srch))){
            value = di.readInt();
            if(preNode == 0 && srch == 0 && (value & 0x80000000) != 0){
              return value & 0x7fffffff;
            }
            found = true;
            buf.reset();
            break;
          }
          buf.skip(4);
        }
        if(!found)break;
      }
      return -1;
    }catch(IOException ex){
      ex.printStackTrace();
      Logger.err(ex);
      return -1;
    }
  }

}
