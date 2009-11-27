/*****************************************************************************
 * 
 * @(#)DeflatedDataSource.java  2009/11
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
package org.jin.dic.data.alpha.read;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.jin.dic.data.CacheService;
import org.jin.dic.data.DataSource;
import org.jin.util.Logger;
import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._ByteArrayOutputStream;
import org.jin.util.io._DataInput;
import org.jin.util.io._DataInputStream;
import org.jin.util.io._RandomAccessFile;

public class DeflatedDataSource implements DataSource {

  static final String         INVALID           = "FILE INVALID";

  protected _RandomAccessFile file              = null;
  protected boolean           isValid           = false;

  protected int               dataSize          = -1;
  protected int               fractionSize      = -1;
  protected int               baseOffset        = 0;

  protected int               blockCount        = 0;
  protected List              blockList         = null;

  protected CacheService      dataFragmentCache = null;

  // configuration --------------------------------------------------------
  public void setFile(_RandomAccessFile file){
    this.file = file;
    ini();
  }
  public void setDataSize(int dataSize){
    this.dataSize = dataSize;
    ini();
  }
  public void setFractionSize(int fractionSize){
    this.fractionSize = fractionSize;
    ini();
  }
  public void setBaseOffset(int baseOffset){
    this.baseOffset = baseOffset;
  }
  public int getDataSize(){
    return dataSize;
  }

  protected void ini(){
    try{
      if(file == null || dataSize == -1 || fractionSize == -1) return;
      blockCount = (dataSize - 1) / fractionSize + 1;
      byte[] buf = new byte[blockCount << 2];
      file.seek(baseOffset);
      file.readFully(buf);
      _DataInput di = new _DataInputStream(new _ByteArrayInputStream(buf), true);
      Block block = null;
      blockList = new ArrayList();
      int offset = 0;
      int deflatedLength;
      for(int i = 0; i < blockCount; i++){
        deflatedLength = di.readInt();
        block = new Block();
        block.deflatedLength = deflatedLength;
        block.offset = offset;
        offset += deflatedLength;
        blockList.add(block);
      }
      isValid = true;

      // // testing on load full cache
      // dataFragmentCache = new CacheService();
      // dataFragmentCache.setSize(blockCount);
      // for(int i = 0; i < blockCount; i++){
      // try{
      // this.getInflatedDataBlock(i);
      // }catch(DataFormatException e){
      // e.printStackTrace();
      // }
      // }

    }catch(IOException e){
      Logger.err(e);
    }
  }

  // ready to work --------------------------------------------------------
  public byte[] getData(int offset, int len){
    if(!isValid) throw new IllegalStateException(INVALID);
    byte[] data = new byte[0];
    if(offset < 0 || len < 0 || offset > dataSize || offset + len > dataSize) return data;
    try{
      _ByteArrayOutputStream bos = new _ByteArrayOutputStream(len);
      int blockIndex = offset / fractionSize;
      int blockOffset;
      byte[] blockData;
      int bytesWrited = 0;
      int bytesToWrite;
      while(bytesWrited < len){
        blockData = getInflatedDataBlock(blockIndex);
        blockOffset = offset - blockIndex * fractionSize + bytesWrited;
        bytesToWrite = blockData.length - blockOffset;
        bos.write(blockData, blockOffset, bytesToWrite);
        bytesWrited += bytesToWrite;
        blockIndex++;
      }
      data = bos.toByteArray(len);
    }catch(IOException e){
      Logger.err(e);
    }catch(DataFormatException e){
      Logger.err(e);
    }
    return data;
  }

  Inflater inflater = null;
  private byte[] getInflatedDataBlock(int index) throws IOException, DataFormatException{
    if(dataFragmentCache == null) dataFragmentCache = new CacheService();
    byte[] data = (byte[]) dataFragmentCache.getCache(index);
    data = null;// FIXME test no cache
    if(data == null){
      Block block = (Block) blockList.get(index);
      byte[] deflatedData = new byte[block.deflatedLength];
      file.seek(baseOffset + (blockList.size() << 2) + block.offset);
      file.readFully(deflatedData);

      if(inflater == null) inflater = new Inflater();
      inflater.reset();
      inflater.setInput(deflatedData);

      byte[] tempBuf = new byte[fractionSize];
      _ByteArrayOutputStream bos = new _ByteArrayOutputStream(fractionSize);

      int readCount = 0;
      while(!inflater.finished()){
        readCount = inflater.inflate(tempBuf);
        bos.write(tempBuf, 0, readCount);
      }
      data = bos.toByteArray();
      dataFragmentCache.putCache(index, data);
    }
    return data;
  }

  private class Block {
    int offset;
    int deflatedLength;
  }

}
