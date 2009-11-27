/*
 * @(#)ByteArrayOutputStream.java 1.53 06/06/07
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.jin.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class _ByteArrayOutputStream extends OutputStream {

  protected byte buf[];
  protected int  count;

  public _ByteArrayOutputStream() {
    this(32);
  }
  public _ByteArrayOutputStream(byte[] buf) {
    this.buf = buf;
  }

  public _ByteArrayOutputStream(int size) {
    if(size < 0){
      throw new IllegalArgumentException("Negative initial size: " + size);
    }
    buf = new byte[size];
  }

  public void close() throws IOException{
  }

  public void reset(){
    count = 0;
  }

  public int size(){
    return count;
  }

  public int skipBytes(int n) throws IOException{
    int newCount = count + n;
    if(newCount > buf.length) return 0;
    count += n;
    return n;
  }

  public byte[] toByteArray(boolean or){
    if(or) return buf;
    else return toByteArray();
  }

  public byte[] toByteArray(){
    byte newbuf[] = new byte[count];
    System.arraycopy(buf, 0, newbuf, 0, count);
    return newbuf;
  }

  public byte[] toByteArray(int len){
    byte newbuf[] = new byte[len];
    System.arraycopy(buf, 0, newbuf, 0, len);
    return newbuf;
  }

  public String toString(){
    return new String(buf, 0, count);
  }

  public String toString(String charsetName) throws UnsupportedEncodingException{
    return new String(buf, 0, count, charsetName);
  }

  public void write(byte b[], int off, int len){
    if((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)){
      throw new IndexOutOfBoundsException();
    }else if(len == 0){
      return;
    }
    int newcount = count + len;
    if(newcount > buf.length){
      byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
      System.arraycopy(buf, 0, newbuf, 0, count);
      buf = newbuf;
    }
    System.arraycopy(b, off, buf, count, len);
    count = newcount;
  }

  public void write(int b){
    int newcount = count + 1;
    if(newcount > buf.length){
      byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
      System.arraycopy(buf, 0, newbuf, 0, count);
      buf = newbuf;
    }
    buf[count] = (byte) b;
    count = newcount;
  }

  public void writeTo(OutputStream out) throws IOException{
    out.write(buf, 0, count);
  }

}
