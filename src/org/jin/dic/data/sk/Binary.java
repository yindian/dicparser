/*****************************************************************************
 * 
 * @(#)Binary.java  2009/03
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
package org.jin.dic.data.sk;

import org.jin.dic.data.sk.i.IDataSource;

class Binary {

  private boolean     standAlone = true;
  private boolean     loaded     = false;
  private IDataSource dataFile   = null;
  private int         offset;
  private int         length;
  private byte[]      data       = null;

  Binary() {
    data = new byte[0];
  }
  boolean isStandAlone(){
    return standAlone;
  }
  void setStandAlone(boolean standAlone){
    this.standAlone = standAlone;
  }
  void setDataFile(IDataSource dataFile){
    this.dataFile = dataFile;
  }
  void setOffset(int offset){
    this.offset = offset;
  }
  void setLength(int length){
    this.length = length;
  }
  void load(){
    if(!loaded){
      data = dataFile.getData(offset, length);
      dataFile = null;
      loaded = true;
      standAlone = true;
    }
  }
  int getOffset(){
    return offset;
  }

  public byte[] getData(){
    if(!loaded) load();
    return data;
  }

}
