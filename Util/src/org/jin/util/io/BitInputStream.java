/*****************************************************************************
 * 
 * @(#)BitInputStream.java  2009/11
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

import java.io.*;

public class BitInputStream extends InputStream {
  private InputStream      myInput;
  private int              myBitCount;
  private int              myBuffer;

  private static final int BITS_PER_BYTE = 8;
  private static final int bmask[]       = { 0x00, 0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff, 0x1ff, 0x3ff, 0x7ff, 0xfff, 0x1fff, 0x3fff,
      0x7fff, 0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff,
      0x1fffffff, 0x3fffffff, 0x7fffffff, 0xffffffff };

  public BitInputStream(InputStream in) {
    myInput = in;
  }

  public boolean markSupported(){
    return myInput.markSupported();
  }

  public void reset() throws IOException{
    if(!markSupported()){
      throw new IOException("not resettable");
    }
    myInput.reset();
    myBuffer = myBitCount = 0;
  }

  public void close() throws IOException{
    if(myInput != null) myInput.close();
  }

  /**
   * returns the number of bits requested as rightmost bits in returned value, returns -1 if not enough bits available to satisfy the request
   * 
   * @param howManyBits is the number of bits to read and return
   * @return the value read, only rightmost <code>howManyBits</code> are valid, returns -1 if not enough bits left
   */

  public int read(int howManyBits) throws IOException{
    int retval = 0;
    if(myInput == null){
      return -1;
    }

    while(howManyBits > myBitCount){
      retval |= (myBuffer << (howManyBits - myBitCount));
      howManyBits -= myBitCount;
      try{
        if((myBuffer = myInput.read()) == -1){
          return -1;
        }
      }catch(IOException ioe){
        throw new IOException("bitreading trouble " + ioe);
      }
      myBitCount = BITS_PER_BYTE;
    }

    if(howManyBits > 0){
      retval |= myBuffer >> (myBitCount - howManyBits);
      myBuffer &= bmask[myBitCount - howManyBits];
      myBitCount -= howManyBits;
    }
    return retval;
  }

  /**
   * Required by classes extending InputStream, returns the next byte from this stream as an int value.
   * 
   * @return the next byte from this stream
   */
  public int read() throws IOException{
    return read(8);
  }
}
