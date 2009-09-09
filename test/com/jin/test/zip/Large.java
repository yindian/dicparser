/*****************************************************************************
 * 
 * @(#)Large.java  2009/03
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
package com.jin.test.zip;

import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.jin.util.BytesUtil;
import com.jin.util.io._ByteArrayOutputStream;

public class Large {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    _ByteArrayOutputStream out = new _ByteArrayOutputStream(0x4000);
    byte[] tempBuf = new byte[0x4000];
    Inflater d = new Inflater();
    d.setInput(data.deData1);
    int deflatedLen = 0;
    int readCount = 0;
    while(!d.finished()){
      readCount = d.inflate(tempBuf);
      out.write(tempBuf, 0, readCount);
      deflatedLen += readCount;
    }
    System.out.println(deflatedLen);
    System.out.println(BytesUtil.convert(out.toByteArray(0x64)));

    Deflater c = new Deflater(Deflater.DEFAULT_COMPRESSION);
    // Deflater.DEFLATED
    // Deflater.BEST_COMPRESSION 78DA2D7B7F5CD5F5F5FF79
    // Deflater.FILTERED
    // Deflater.BEST_SPEED 78012D9B7F5855D79
    // Deflater.DEFAULT_COMPRESSION 789C2D7B7F5CD
    // Deflater.DEFAULT_STRATEGY
    // Deflater.NO_COMPRESSION 78DA010040FFBF

    deflatedLen = 0;
    c.setInput(out.toByteArray());
    c.finish();
    out.reset();
    while(!c.finished()){
      readCount = c.deflate(tempBuf);
      out.write(tempBuf, 0, readCount);
      deflatedLen += readCount;
    }

    System.out.println(BytesUtil.convert(out.toByteArray(0x64)));
    System.out.println(deflatedLen);
    System.out.println(Arrays.equals(out.toByteArray(), data.deData1));
  }

}
