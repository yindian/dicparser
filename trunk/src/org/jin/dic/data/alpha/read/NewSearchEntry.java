/*****************************************************************************
 * 
 * @(#)NewSearchEntry.java  2009/11
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
import java.util.ArrayList;
import java.util.List;

import org.jin.dic.data.DataSource;
import org.jin.util.Logger;
import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._DataInput;
import org.jin.util.io._DataInputStream;

public class NewSearchEntry {

  private DataSource dataSource;
  private int        baseOffset;
  private boolean    dataIsCaseSensitive;

  public void setDataSource(DataSource dataSource){
    this.dataSource = dataSource;
  }
  public void setBaseOffset(int baseOffset){
    this.baseOffset = baseOffset;
  }
  public void setDataIsCaseSensitive(boolean dataIsCaseSensitive){
    this.dataIsCaseSensitive = dataIsCaseSensitive;
  }

  private _ByteArrayInputStream buf = new _ByteArrayInputStream(new byte[0]);
  private _DataInput            di  = new _DataInputStream(buf, true);

  public int[] search(String word){
    return search(word, false);
  }
  public int[] search(String word, boolean caseSensitive){
    List values = new ArrayList();
    byte[] data;
    char[] searchChars = new char[word.length() + 1];
    word.getChars(0, word.length(), searchChars, 0);

    data = dataSource.getData(baseOffset + 0x05, 0x05);
    buf.reset(data);
    try{
      data = dataSource.getData(baseOffset + di.read24(), di.readChar());
      goToNextNode(data, searchChars, 0, values, caseSensitive);
    }catch(IOException ex){
      Logger.err(ex);
      values.clear();
    }

    int[] rslt = new int[values.size()];
    Integer[] itg = new Integer[values.size()];
    values.toArray(itg);
    for(int i = 0; i < values.size(); i++)
      rslt[i] = itg[i].intValue();
    return rslt;
  }
  private void goToNextNode(byte[] data, char[] searchChars, int currPos, List values, boolean caseSensitive) throws IOException{
    int value = -1, charSize, childrenOffset = 0, childrenLen = 0, childrenSize = 0;
    boolean found;
    char[] chars;
    _ByteArrayInputStream buf = new _ByteArrayInputStream(data);
    _DataInput di = new _DataInputStream(buf, true);

    while(buf.available() > 0){
      found = false;
      charSize = di.readByte();
      chars = new char[charSize];
      while(charSize > 0)
        chars[chars.length - charSize--] = di.readChar();
      childrenSize = di.readChar();
      if(childrenSize != 0){
        childrenOffset = di.read24();
        childrenLen = di.readChar();
      }else value = getValue(di);
      if(equals(searchChars, currPos, chars, caseSensitive)){
        found = true;
        if(currPos + chars.length == searchChars.length && childrenSize == 0){
          values.add(new Integer(value));
        }else if(childrenSize != 0){
          data = dataSource.getData(baseOffset + childrenOffset, childrenLen);
          goToNextNode(data, searchChars, currPos + chars.length, values, caseSensitive);
        }
      }
      if(found && ((caseSensitive && dataIsCaseSensitive) || !dataIsCaseSensitive)) return;
    }
  }
  public int getFirstDataOffset(String word){// FIXME FULE!!!
    char[] searchChars = new char[word.length() + 1];
    word.getChars(0, word.length(), searchChars, 0);

    int value = 0;
    boolean further;// continues to match
    boolean justGo = false;// no matching, just get the first leaf(no children)
    char[] chars;
    byte[] data;

    data = dataSource.getData(baseOffset, 0x0a);
    buf.reset(data);
    try{
      int charSize = di.readByte();
      di.skipBytes(charSize << 1);
      int childrenSize = di.readChar();
      int childrenOffset = di.read24();
      int childrenLen = di.readChar();

      int pos = 0;// current match position in search word
      int temp;// to hold the childrenSize
      int matchLen, maxMatchLen;
      int pValue = 0;
      int pChildrenOffset = 0;
      int pChildrenLen = -1;
      int tempOffset = 0;// to keep from Infinite loop
      while(true){
        if(tempOffset == childrenOffset) justGo = true;
        tempOffset = childrenOffset;

        data = dataSource.getData(baseOffset + tempOffset, childrenLen);
        buf.reset(data);

        maxMatchLen = 0;
        further = false;
        for(int j = 0; j < childrenSize; j++){
          // gets current search_node info
          charSize = di.readByte();
          chars = new char[charSize];
          while(charSize > 0)
            chars[chars.length - charSize--] = di.readChar();
          temp = di.readChar();
          if(temp != 0){
            childrenOffset = di.read24();
            childrenLen = di.readChar();
          }else value = getValue(di);

          // begins to match...
          if(justGo){
            if(temp == 0) return value;
            else{
              further = true;
              break;
            }
          }else{
            matchLen = getMatchLen(searchChars, pos, chars);
            if(matchLen > 0){// matches something
              if(matchLen > maxMatchLen){
                maxMatchLen = matchLen;
                if(temp == 0){
                  pValue = value;
                  pChildrenOffset = -1;
                }else{
                  pChildrenOffset = childrenOffset;
                  pChildrenLen = childrenLen;
                  pValue = -1;
                }
              }
              if(matchLen == chars.length){// current node matches
                if(temp == 0) return value;
                else{// continues to find
                  if(matchLen == searchChars.length - pos) justGo = true;
                  else pos += matchLen;
                  childrenSize = temp;
                  further = true;
                  break;
                }
              }
            }
          }
        }
        if(!further){
          if(maxMatchLen > 0 && pChildrenOffset == -1) return pValue;
          else{// rolls back
            if(pChildrenLen == -1) return 0;
            childrenOffset = pChildrenOffset;
            childrenLen = pChildrenLen;
            justGo = true;
          }
        }
      }
    }catch(IOException ex){
      Logger.err(ex);
      value = 0;
    }
    return value;
  }
  
  private static final int getMatchLen(char[] c1, int offset, char[] c2){
    for(int i = 0; i < c1.length - offset; i++){
      if(i == c2.length) return i;
      if(c1[offset + i] != c2[i] && Character.toLowerCase(c1[offset + i]) != Character.toLowerCase(c2[i])) return i;
    }
    return c1.length - offset;
  }
  private static final boolean equals(char[] c1, int offset, char[] c2, boolean caseSensitive){
    if(c1 == null || c2 == null || offset >= c1.length || c2.length + offset > c1.length) return false;
    boolean equals = true;
    for(int i = 0; i < c2.length; i++){
      if(c1[offset + i] == c2[i]) continue;
      else if(caseSensitive || Character.toLowerCase(c1[offset + i]) != Character.toLowerCase(c2[i])){
        equals = false;
        break;
      }
    }
    return equals;
  }
  private static final int getValue(_DataInput di) throws IOException{
    int valueLen = di.readByte();
    switch(valueLen){
      case 0:
        return 0;
      case 1:
        return 0x000000ff & di.readByte();
      case 2:
        return 0x0000ffff & di.readChar();
      case 3:
        return di.read24();
      case 4:
        return di.readInt();
    }
    return -1;
  }

}
