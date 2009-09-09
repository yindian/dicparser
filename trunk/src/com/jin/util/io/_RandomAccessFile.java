/*****************************************************************************
 * 
 * @(#)_RandomAccessFile.java  2009/03
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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class _RandomAccessFile implements _DataInput, _DataOutput {

  protected RandomAccessFile   beRaf = null;
  protected LERandomAccessFile leRaf = null;
  protected boolean            le    = true;

  public _RandomAccessFile(String file, String rw) throws FileNotFoundException {
    if(le) leRaf = new LERandomAccessFile(file, rw);
    else beRaf = new RandomAccessFile(file, rw);
  }// TODO constructor chain?

  public _RandomAccessFile(File file, String rw) throws FileNotFoundException {
    if(le) leRaf = new LERandomAccessFile(file, rw);
    else beRaf = new RandomAccessFile(file, rw);
  }

  public _RandomAccessFile(File file, String rw, boolean le) throws FileNotFoundException {
    this.le = le;
    if(le) leRaf = new LERandomAccessFile(file, rw);
    else beRaf = new RandomAccessFile(file, rw);
  }
  public _RandomAccessFile(String file, String rw, boolean le) throws FileNotFoundException {
    this.le = le;
    if(le) leRaf = new LERandomAccessFile(file, rw);
    else beRaf = new RandomAccessFile(file, rw);
  }
  public final void close() throws IOException{
    if(le) leRaf.close();
    else beRaf.close();
  }
  public final FileDescriptor getFD() throws IOException{
    if(le) return leRaf.getFD();
    else return beRaf.getFD();
  }
  public final long getFilePointer() throws IOException{
    if(le) return leRaf.getFilePointer();
    else return beRaf.getFilePointer();
  }
  public final long length() throws IOException{
    if(le) return leRaf.length();
    else return beRaf.length();
  }
  public final int read() throws IOException{
    if(le) return leRaf.read();
    else return beRaf.read();
  }
  public final int read(byte ba[]) throws IOException{
    if(le) return leRaf.read(ba);
    else return beRaf.read(ba);
  }

  public final int read(byte ba[], int off, int len) throws IOException{
    if(le) return leRaf.read(ba, off, len);
    else return beRaf.read(ba, off, len);
  }
  public final boolean readBoolean() throws IOException{
    if(le) return leRaf.readBoolean();
    else return beRaf.readBoolean();
  }
  public final byte readByte() throws IOException{
    if(le) return leRaf.readByte();
    else return beRaf.readByte();
  }
  public final char readChar() throws IOException{
    if(le) return leRaf.readChar();
    else return beRaf.readChar();
  }
  public final double readDouble() throws IOException{
    if(le) return leRaf.readDouble();
    else return beRaf.readDouble();
  }
  public final float readFloat() throws IOException{
    if(le) return leRaf.readFloat();
    else return beRaf.readFloat();
  }
  public final void readFully(byte ba[]) throws IOException{
    if(le) leRaf.readFully(ba);
    else beRaf.readFully(ba);
  }
  public final void readFully(byte ba[], int off, int len) throws IOException{
    if(le) leRaf.readFully(ba, off, len);
    else beRaf.readFully(ba, off, len);
  }
  public final int readInt() throws IOException{
    if(le) return leRaf.readInt();
    else return beRaf.readInt();
  }
  public final String readLine() throws IOException{
    if(le) return leRaf.readLine();
    else return beRaf.readLine();
  }
  public final long readLong() throws IOException{
    if(le) return leRaf.readLong();
    else return beRaf.readLong();
  }
  public final short readShort() throws IOException{
    if(le) return leRaf.readShort();
    else return beRaf.readShort();
  }
  public final String readUTF() throws IOException{
    if(le) return leRaf.readUTF();
    else return beRaf.readUTF();
  }
  public final int readUnsignedByte() throws IOException{
    if(le) return leRaf.readUnsignedByte();
    else return beRaf.readUnsignedByte();
  }
  public final int readUnsignedShort() throws IOException{
    if(le) return leRaf.readUnsignedShort();
    else return beRaf.readUnsignedShort();
  }
  public final void seek(long pos) throws IOException{
    if(le) leRaf.seek(pos);
    else beRaf.seek(pos);
  }
  public final int skipBytes(int n) throws IOException{
    if(le) return leRaf.skipBytes(n);
    else return beRaf.skipBytes(n);
  }
  public final synchronized void write(int ib) throws IOException{
    if(le) leRaf.write(ib);
    else beRaf.write(ib);
  }
  public final void write(byte ba[]) throws IOException{
    if(le) leRaf.write(ba);
    else beRaf.write(ba);
  }
  public final synchronized void write(byte ba[], int off, int len) throws IOException{
    if(le) leRaf.write(ba, off, len);
    else beRaf.write(ba, off, len);
  }
  public final void writeBoolean(boolean v) throws IOException{
    if(le) leRaf.writeBoolean(v);
    else beRaf.writeBoolean(v);
  }
  public final void writeByte(int v) throws IOException{
    if(le) leRaf.writeByte(v);
    else beRaf.writeByte(v);
  }
  public final void writeBytes(String s) throws IOException{
    if(le) leRaf.writeBytes(s);
    else beRaf.writeBytes(s);
  }
  public final void writeChar(int v) throws IOException{
    if(le) leRaf.writeChar(v);
    else beRaf.writeChar(v);
  }
  public final void writeChars(String s) throws IOException{
    if(le) leRaf.writeChars(s);
    else beRaf.writeChars(s);
  }
  public final void writeDouble(double v) throws IOException{
    if(le) leRaf.writeDouble(v);
    else beRaf.writeDouble(v);
  }
  public final void writeFloat(float v) throws IOException{
    if(le) leRaf.writeFloat(v);
    else beRaf.writeFloat(v);
  }
  public final void writeInt(int v) throws IOException{
    if(le) leRaf.writeInt(v);
    else beRaf.writeInt(v);
  }
  public final void writeLong(long v) throws IOException{
    if(le) leRaf.writeLong(v);
    else beRaf.writeLong(v);
  }
  public final void writeShort(int v) throws IOException{
    if(le) leRaf.writeShort(v);
    else beRaf.writeShort(v);
  }
  public final void writeUTF(String s) throws IOException{
    if(le) leRaf.writeUTF(s);
    else beRaf.writeUTF(s);
  }

  protected byte work[] = new byte[4];
  public final long readUnsignedInt() throws IOException{
    readFully(work, 0, 4);
    long value;
    if(le) value = (work[3] & 0xff) << 24 | (work[2] & 0xff) << 16 | (work[1] & 0xff) << 8
        | (work[0] & 0xff);
    else value = (work[0] & 0xff) << 24 | (work[1] & 0xff) << 16 | (work[2] & 0xff) << 8
        | (work[3] & 0xff);
    return value;
  }
  public final int readUnsigned24() throws IOException{
    readFully(work, 0, 3);
    int value;
    if(le) value = (work[2] & 0xff) << 16 | (work[1] & 0xff) << 8 | (work[0] & 0xff);
    else value = (work[0] & 0xff) << 16 | (work[1] & 0xff) << 8 | (work[2] & 0xff);
    return value;
  }
  public final int read24() throws IOException{
    readFully(work, 0, 3);
    int value;
    if(le) value = (work[2]) << 16 | (work[1] & 0xff) << 8 | (work[0] & 0xff);
    else value = (work[0]) << 16 | (work[1] & 0xff) << 8 | (work[2] & 0xff);
    return value;
  }
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
