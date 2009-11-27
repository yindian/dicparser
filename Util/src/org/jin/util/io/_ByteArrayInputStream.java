/*
 * @(#)ByteArrayInputStream.java	1.47 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.jin.util.io;

import java.io.IOException;
import java.io.InputStream;

public class _ByteArrayInputStream extends InputStream {

  protected byte buf[];

  protected int  pos;
  protected int  mark = 0;
  protected int  count;

  public _ByteArrayInputStream(byte buf[]) {
    this.buf = buf;
    this.pos = 0;
    this.count = buf.length;
  }

  public _ByteArrayInputStream(byte buf[], int offset, int length) {
    this.buf = buf;
    this.pos = offset;
    this.count = Math.min(offset + length, buf.length);
    this.mark = offset;
  }

  public int read(){
    return (pos < count) ? (buf[pos++] & 0xff) : -1;
  }

  public int read(byte b[], int off, int len){
    if(b == null){
      throw new NullPointerException();
    }else if(off < 0 || len < 0 || len > b.length - off){
      throw new IndexOutOfBoundsException();
    }
    if(pos >= count){
      return -1;
    }
    if(pos + len > count){
      len = count - pos;
    }
    if(len <= 0){
      return 0;
    }
    System.arraycopy(buf, pos, b, off, len);
    pos += len;
    return len;
  }

  public long skip(long n){
    if(pos + n > count){
      n = count - pos;
    }
    if(n < 0){
      return 0;
    }
    pos += n;
    return n;
  }

  public int available(){
    return count - pos;
  }

  public boolean markSupported(){
    return true;
  }

  public void mark(int readAheadLimit){
    mark = pos;
  }

  public void reset(){
    pos = mark;
  }

  public void close() throws IOException{
  }
  
  public void reset(byte[] buf){
    this.buf = buf;
    pos = 0;
    mark = 0;
    this.count = buf.length;
  }

}
