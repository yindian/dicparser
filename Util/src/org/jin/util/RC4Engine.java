/*****************************************************************************
 * 
 * @(#)RC4Engine.java  2009/03
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
package org.jin.util;

public class RC4Engine {

  private final static int STATE_LENGTH = 256;
  private byte[]           engineState  = null;
  private int              x            = 0;
  private int              y            = 0;
  private byte[]           workingKey   = null;

  public void init(byte[] key){
    workingKey = new byte[key.length];
    System.arraycopy(key, 0, workingKey, 0, key.length);
    setKey(workingKey);
  }

  public void setXY(int xy){
    x = xy & 0xff;
    y = (xy >> 8) & 0xff;
  }

  public void processBytes(byte[] data){
    processBytes(data, 0);
  }
  public void processBytes(byte[] data, int offset){
    processBytes(data, offset, data.length - offset);
  }
  public void processBytes(byte[] data, int offset, int len){
    processBytes(data, offset, len - offset, data, offset);
  }
  public void processBytes(byte[] in, int inOff, int len, byte[] out, int outOff){
    if(in == null || out == null) return;
    if((inOff + len) > in.length) return;// input buffer too short
    if((outOff + len) > out.length) return;// output buffer too short

    for(int i = 0; i < len; i++)
      out[i + outOff] = returnByte(in[i + inOff]);
  }
  public final byte returnByte(byte in){
    x = (x + 1) & 0xff;
    y = (engineState[x] + y) & 0xff;

    // swap
    byte tmp = engineState[x];
    engineState[x] = engineState[y];
    engineState[y] = tmp;

    // xor
    return (byte) (in ^ engineState[(engineState[x] + engineState[y]) & 0xff]);
  }

  public void reset(){
    setKey(workingKey);
  }

  private void setKey(byte[] keyBytes){
    workingKey = keyBytes;
    x = 0;
    y = 0;
    if(engineState == null) engineState = new byte[STATE_LENGTH];

    for(int i = 0; i < STATE_LENGTH; i++)
      engineState[i] = (byte) i;

    int i1 = 0;
    int i2 = 0;

    for(int i = 0; i < STATE_LENGTH; i++){
      i2 = ((keyBytes[i1] & 0xff) + engineState[i] + i2) & 0xff;

      byte tmp = engineState[i];
      engineState[i] = engineState[i2];
      engineState[i2] = tmp;
      i1 = (i1 + 1) % keyBytes.length;
    }
  }

}
