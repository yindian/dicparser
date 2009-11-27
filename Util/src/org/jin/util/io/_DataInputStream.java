/*****************************************************************************
 * 
 * @(#)_DataInputStream.java  2009/03
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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class _DataInputStream extends InputStream implements _DataInput {
  
  protected DataInputStream   beDis = null;
  protected LEDataInputStream leDis = null;
  protected boolean           le    = true;

  public _DataInputStream(InputStream in) {
    if(le) leDis = new LEDataInputStream(in);
    else beDis = new DataInputStream(in);
  }
  public _DataInputStream(InputStream in, boolean le) {
    this.le = le;
    if(le) leDis = new LEDataInputStream(in);
    else beDis = new DataInputStream(in);
  }
  public int available() throws IOException{
    if(le) return leDis.available();
    else return beDis.available();
  }
  public final int read() throws IOException{
    if(le) return leDis.read();
    else return beDis.read();
  }
  public final int read(byte b[], int off, int len) throws IOException{
    if(le) return leDis.read(b, off, len);
    else return beDis.read(b, off, len);
  }
  public final int skipBytes(int n) throws IOException{
    if(le) return leDis.skipBytes(n);
    else return beDis.skipBytes(n);
  }
  public final boolean readBoolean() throws IOException{
    if(le) return leDis.readBoolean();
    else return beDis.readBoolean();
  }
  public final byte readByte() throws IOException{
    if(le) return leDis.readByte();
    else return beDis.readByte();
  }
  public final char readChar() throws IOException{
    if(le) return leDis.readChar();
    else return beDis.readChar();
  }
  public final double readDouble() throws IOException{
    if(le) return leDis.readDouble();
    else return beDis.readDouble();
  }
  public final float readFloat() throws IOException{
    if(le) return leDis.readFloat();
    else return beDis.readFloat();
  }
  public final void readFully(byte ba[]) throws IOException{
    if(le) leDis.readFully(ba);
    else beDis.readFully(ba);
  }
  public final void readFully(byte ba[], int off, int len) throws IOException{
    if(le) leDis.readFully(ba, off, len);
    else beDis.readFully(ba, off, len);
  }
  public final int readInt() throws IOException{
    if(le) return leDis.readInt();
    else return beDis.readInt();
  }
  /**
   * @deprecated
   */
  public final String readLine() throws IOException{
    if(le) return leDis.readLine();
    else return beDis.readLine();
  }
  public final long readLong() throws IOException{
    if(le) return leDis.readLong();
    else return beDis.readLong();
  }
  public final short readShort() throws IOException{
    if(le) return leDis.readShort();
    else return beDis.readShort();
  }
  public final String readUTF() throws IOException{
    if(le) return leDis.readUTF();
    else return beDis.readUTF();
  }
  public final int readUnsignedByte() throws IOException{
    if(le) return leDis.readUnsignedByte();
    else return beDis.readUnsignedByte();
  }
  public final int readUnsignedShort() throws IOException{
    if(le) return leDis.readUnsignedShort();
    else return beDis.readUnsignedShort();
  }
  public final void close() throws IOException{
    if(le) leDis.close();
    else beDis.close();
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

}
