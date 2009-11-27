/*****************************************************************************
 * 
 * @(#)LEDataOutputStream.java  2009/11
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
package org.jin.util.io;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Copyright (c) 2009 Canadian Mind Products.
 */
final class LEDataOutputStream implements DataOutput {

  protected final DataOutputStream dataOutputStream;

  protected final byte[]           workingBytes;

  public LEDataOutputStream(OutputStream out) {
    this.dataOutputStream = new DataOutputStream(out);
    workingBytes = new byte[8];
  }

  public final void close() throws IOException{
    dataOutputStream.close();
  }

  public void flush() throws IOException{
    dataOutputStream.flush();
  }

  public final int size(){
    return dataOutputStream.size();
  }

  public final synchronized void write(int ib) throws IOException{
    dataOutputStream.write(ib);
  }

  public final void write(byte ba[]) throws IOException{
    dataOutputStream.write(ba, 0, ba.length);
  }

  public final synchronized void write(byte ba[], int off, int len) throws IOException{
    dataOutputStream.write(ba, off, len);
  }

  public final void writeBoolean(boolean v) throws IOException{
    dataOutputStream.writeBoolean(v);
  }

  public final void writeByte(int v) throws IOException{
    dataOutputStream.writeByte(v);
  }

  public final void writeBytes(String s) throws IOException{
    dataOutputStream.writeBytes(s);
  }

  public final void writeChar(int v) throws IOException{
    workingBytes[0] = (byte) v;
    workingBytes[1] = (byte) (v >> 8);
    dataOutputStream.write(workingBytes, 0, 2);
  }

  public final void writeChars(String s) throws IOException{
    int len = s.length();
    for(int i = 0; i < len; i++){
      writeChar(s.charAt(i));
    }
  }

  public final void writeDouble(double v) throws IOException{
    writeLong(Double.doubleToLongBits(v));
  }

  public final void writeFloat(float v) throws IOException{
    writeInt(Float.floatToIntBits(v));
  }

  public final void writeInt(int v) throws IOException{
    workingBytes[0] = (byte) v;
    workingBytes[1] = (byte) (v >> 8);
    workingBytes[2] = (byte) (v >> 16);
    workingBytes[3] = (byte) (v >> 24);
    dataOutputStream.write(workingBytes, 0, 4);
  }

  public final void writeLong(long v) throws IOException{
    workingBytes[0] = (byte) v;
    workingBytes[1] = (byte) (v >> 8);
    workingBytes[2] = (byte) (v >> 16);
    workingBytes[3] = (byte) (v >> 24);
    workingBytes[4] = (byte) (v >> 32);
    workingBytes[5] = (byte) (v >> 40);
    workingBytes[6] = (byte) (v >> 48);
    workingBytes[7] = (byte) (v >> 56);
    dataOutputStream.write(workingBytes, 0, 8);
  }

  public final void writeShort(int v) throws IOException{
    workingBytes[0] = (byte) v;
    workingBytes[1] = (byte) (v >> 8);
    dataOutputStream.write(workingBytes, 0, 2);
  }

  public final void writeUTF(String s) throws IOException{
    dataOutputStream.writeUTF(s);
  }
}
