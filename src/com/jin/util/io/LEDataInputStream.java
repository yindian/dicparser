package com.jin.util.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

final class LEDataInputStream extends InputStream implements DataInput {

  private DataInputStream dataInputStream;

  private InputStream     inputStream;

  private byte            workingBytes[];

  public LEDataInputStream(InputStream in) {
    this.inputStream = in;
    this.dataInputStream = new DataInputStream(in);
    workingBytes = new byte[8];
  }

  public int available() throws IOException{
    return dataInputStream.available();
  }

  public final short readShort() throws IOException{
    dataInputStream.readFully(workingBytes, 0, 2);
    return (short) ((workingBytes[1] & 0xff) << 8 | (workingBytes[0] & 0xff));
  }

  /**
   * Note, returns int even though it reads a short.
   */
  public final int readUnsignedShort() throws IOException{
    dataInputStream.readFully(workingBytes, 0, 2);
    return((workingBytes[1] & 0xff) << 8 | (workingBytes[0] & 0xff));
  }

  public final char readChar() throws IOException{
    dataInputStream.readFully(workingBytes, 0, 2);
    return (char) ((workingBytes[1] & 0xff) << 8 | (workingBytes[0] & 0xff));
  }

  public final int readInt() throws IOException{
    dataInputStream.readFully(workingBytes, 0, 4);
    return (workingBytes[3]) << 24 | (workingBytes[2] & 0xff) << 16 | (workingBytes[1] & 0xff) << 8
        | (workingBytes[0] & 0xff);
  }

  public final long readLong() throws IOException{
    dataInputStream.readFully(workingBytes, 0, 8);
    return (long) (workingBytes[7]) << 56 | (long) (workingBytes[6] & 0xff) << 48
        | (long) (workingBytes[5] & 0xff) << 40 | (long) (workingBytes[4] & 0xff) << 32
        | (long) (workingBytes[3] & 0xff) << 24 | (long) (workingBytes[2] & 0xff) << 16
        | (long) (workingBytes[1] & 0xff) << 8 | (long) (workingBytes[0] & 0xff);
  }

  public final float readFloat() throws IOException{
    return Float.intBitsToFloat(readInt());
  }

  public final double readDouble() throws IOException{
    return Double.longBitsToDouble(readLong());
  }

  public final int read(byte b[], int off, int len) throws IOException{
    return inputStream.read(b, off, len);
  }

  public final void readFully(byte b[]) throws IOException{
    dataInputStream.readFully(b, 0, b.length);
  }

  public final void readFully(byte b[], int off, int len) throws IOException{
    dataInputStream.readFully(b, off, len);
  }

  public final int skipBytes(int n) throws IOException{
    return dataInputStream.skipBytes(n);
  }

  public final boolean readBoolean() throws IOException{
    return dataInputStream.readBoolean();
  }

  public final byte readByte() throws IOException{
    return dataInputStream.readByte();
  }

  public int read() throws IOException{
    return inputStream.read();
  }

  public final int readUnsignedByte() throws IOException{
    return dataInputStream.readUnsignedByte();
  }

  /**
   * @deprecated
   */
  public final String readLine() throws IOException{
    return dataInputStream.readLine();
  }

  public final String readUTF() throws IOException{
    return dataInputStream.readUTF();
  }

  public final void close() throws IOException{
    dataInputStream.close();
  }
}
