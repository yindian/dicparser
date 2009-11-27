/*****************************************************************************
 * 
 * @(#)CatalogedDataBlock.java  2009/11
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

public class CatalogedDataBlock {

  private DataSource dataSource;
  private int        baseOffset;
  private int        dataCount = -1;

  public void setBaseOffset(int baseOffset){
    this.baseOffset = baseOffset;
  }
  public void setDataSource(DataSource dataSource){
    this.dataSource = dataSource;
    ini();
  }
  public void setDataCount(int dataCount){
    this.dataCount = dataCount;
    ini();
  }

  private int dataOffset;
  private void ini(){
    if(dataSource == null || dataCount == -1) return;
    dataOffset = dataCount << 2;
  }
  public byte[] getData(int index){
    if(dataSource == null || index < 0 || index >= dataCount) return new byte[0];
    byte[] result;
    try{
      int offset = readInt(baseOffset + (index << 2));
      int absDataOffset = baseOffset + dataOffset + offset;
      result = dataSource.getData(absDataOffset + 4, readInt(absDataOffset));
    }catch(IOException e){
      Logger.err(e);
      result = new byte[0];
    }
    return result;
  }

  private _ByteArrayInputStream buf = new _ByteArrayInputStream(new byte[0]);
  private _DataInput            di  = new _DataInputStream(buf, true);
  private int readInt(int offset) throws IOException{
    buf.reset(dataSource.getData(offset, 4));
    return di.readInt();
  }

}
