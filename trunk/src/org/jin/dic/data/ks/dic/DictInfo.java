/*****************************************************************************
 * 
 * @(#)DictInfo.java  2009/03
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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.dic.data.ks.Element;


public class DictInfo implements Element {
  private int    length = 0;
  private byte[] data   = new byte[0];
  private String string = null;

  public void read(DataInput in, int para) throws BadFormatException, IOException{
    data = new byte[para];
    in.readFully(data);
    length = para;
    string = DictDataEngine.getString(data);
  }

  public void write(DataOutput out) throws IOException{
    out.write(data);
  }

  public byte[] getData(){
    return data;
  }
  public int getLength(){
    return length;
  }
  public int getZippedLength(){
    return length;
  }
  public String getString(){
    return string;
  }

}
