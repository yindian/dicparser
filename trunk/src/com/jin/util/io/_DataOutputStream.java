/*****************************************************************************
 * 
 * @(#)_DataOutputStream.java  2009/03
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
package com.jin.util.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class _DataOutputStream implements _DataOutput {
  
  protected DataOutputStream   beDos = null;
  protected LEDataOutputStream leDos = null;
  protected boolean            le    = true;

  public _DataOutputStream(OutputStream out) {
    if(le) leDos = new LEDataOutputStream(out);
    else beDos = new DataOutputStream(out);
  }
  public _DataOutputStream(OutputStream out, boolean le) {
    this.le = le;
    if(le) leDos = new LEDataOutputStream(out);
    else beDos = new DataOutputStream(out);
  }

  public final void close() throws IOException{
    if(le) leDos.close();
    else beDos.close();
  }
  public void flush() throws IOException{
    if(le) leDos.flush();
    else beDos.flush();
  }
  public final int size(){
    if(le) return leDos.size();
    else return beDos.size();
  }
  public final synchronized void write(int ib) throws IOException{
    if(le) leDos.write(ib);
    else beDos.write(ib);
  }
  public final void write(byte ba[]) throws IOException{
    if(le) leDos.write(ba);
    else beDos.write(ba);
  }
  public final synchronized void write(byte ba[], int off, int len) throws IOException{
    if(le) leDos.write(ba, off, len);
    else beDos.write(ba, off, len);
  }
  public final void writeBoolean(boolean v) throws IOException{
    if(le) leDos.writeBoolean(v);
    else beDos.writeBoolean(v);
  }
  public final void writeByte(int v) throws IOException{
    if(le) leDos.writeByte(v);
    else beDos.writeByte(v);
  }
  public final void writeBytes(String s) throws IOException{
    if(le) leDos.writeBytes(s);
    else beDos.writeBytes(s);
  }
  public final void writeChar(int v) throws IOException{
    if(le) leDos.writeChar(v);
    else beDos.writeChar(v);
  }
  public final void writeChars(String s) throws IOException{
    if(le) leDos.writeChars(s);
    else beDos.writeChars(s);
  }
  public final void writeDouble(double v) throws IOException{
    if(le) leDos.writeDouble(v);
    else beDos.writeDouble(v);
  }
  public final void writeFloat(float v) throws IOException{
    if(le) leDos.writeFloat(v);
    else beDos.writeFloat(v);
  }
  public final void writeInt(int v) throws IOException{
    if(le) leDos.writeInt(v);
    else beDos.writeInt(v);
  }
  public final void writeLong(long v) throws IOException{
    if(le) leDos.writeLong(v);
    else beDos.writeLong(v);
  }
  public final void writeShort(int v) throws IOException{
    if(le) leDos.writeShort(v);
    else beDos.writeShort(v);
  }
  public final void writeUTF(String s) throws IOException{
    if(le) leDos.writeUTF(s);
    else beDos.writeUTF(s);
  }
  
  protected byte work[] = new byte[4];
  public final void write24(int v) throws IOException{
    if(le){
      work[0] = (byte) v;
      work[1] = (byte) (v >> 8);
      work[2] = (byte) (v >> 16);
    }else{
      work[2] = (byte) v;
      work[1] = (byte) (v >> 8);
      work[0] = (byte) (v >> 16);
    }
    write(work, 0, 3);
  }
  
}
