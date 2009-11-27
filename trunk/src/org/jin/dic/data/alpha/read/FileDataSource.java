/*****************************************************************************
 * 
 * @(#)FileDataSource.java  2009/11
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
import org.jin.util.io._RandomAccessFile;

public class FileDataSource implements DataSource {

  private _RandomAccessFile file       = null;
  private int               dataSize   = -1;
  private int               baseOffset = 0;

  public int getDataSize(){
    return dataSize;
  }
  public void setFile(_RandomAccessFile file){
    this.file = file;
  }
  public void setDataSize(int dataSize){
    this.dataSize = dataSize;
  }
  public void setBaseOffset(int baseOffset){
    this.baseOffset = baseOffset;
  }
  public byte[] getData(int offset, int len){
    byte[] data;
    if(offset < 0 || len < 0 || offset > dataSize || offset + len > dataSize) return new byte[0];
    try{
      data = new byte[len];
      file.seek(baseOffset + offset);
      file.readFully(data);
    }catch(IOException e){
      Logger.err(e);
      data = new byte[0];
    }
    return data;
  }

}
