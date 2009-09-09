/*****************************************************************************
 * 
 * @(#)File.java  2009/03
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
package com.jin.dic.sk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import com.jin.dic.CacheService;
import com.jin.dic.Environment;
import com.jin.dic.sk.i.IConfigable;
import com.jin.dic.sk.i.IDataSource;
import com.jin.util.Logger;
import com.jin.util.io._ByteArrayInputStream;
import com.jin.util.io._ByteArrayOutputStream;
import com.jin.util.io._DataInput;
import com.jin.util.io._DataInputStream;
import com.jin.util.io._RandomAccessFile;

class File implements IDataSource, IConfigable {

  static final int            DEFAULTFRAGMENTSIZE = 0x1000;
  static final String         INVALID             = "FILE INVALID";

  protected _RandomAccessFile file                = null;
  protected Environment       environment         = null;
  protected String            fileName            = null;
  protected String            catalogFileName     = null;
  protected List              catalogList         = null;
  protected int               dataFragmentSize    = DEFAULTFRAGMENTSIZE;
  protected CacheService      dataFragmentCache   = null;

  protected boolean           isValid             = false;
  protected boolean           encrypted           = false;
  protected boolean           compressed          = false;
  protected int               fileSize            = -1;

  File() {
  }
  // getter and setter ----------------------------------------------------
  public void setEnvironment(Environment environment){
    this.environment = environment;
  }
  public void setDataFragmentCache(CacheService dataFragmentCache){
    this.dataFragmentCache = dataFragmentCache;
  }
  public CacheService getDataFragmentCache(){
    return dataFragmentCache;
  }
  public String getCatalogFileName(){
    return catalogFileName;
  }
  public int getFileSize(){
    return fileSize;
  }
  public String getFileName(){
    return fileName;
  }
  public boolean isValid(){
    return isValid;
  }
  public boolean isEncrypted(){
    return encrypted;
  }
  public boolean isCompressed(){
    return compressed;
  }

  // configuration --------------------------------------------------------
  public void configureItem(String section, String token, String value){
    if(token.equals("PATH")){
      fileName = value;
    }else if(token.equals("COMPRESSION")){
      if(value.equals("ZIP")) compressed = true;
    }else if(token.equals("CATALOG")){
      catalogFileName = value;
    }else if(token.equals("OPENMODE")){
      Logger.err(new Exception("OPENMODE not imp"));
    }else if(token.equals("CRYPT")){
      encrypted = value.equals("YES");
    }
  }

  // load -----------------------------------------------------------------
  public void open(){
    if(file != null){
      Logger.err(new Exception("file open more than once " + fileName));
      return;
    }
    if(fileName == null) return;
    if(environment == null) environment = new Environment();
    isValid = true;
    try{
      file = new _RandomAccessFile(environment.getFile(fileName), "r", true);
      if(compressed){
        if(catalogFileName != null) loadCatalog();
        else{
          Logger.err(new Exception("compressed file without catalog file " + fileName));
          isValid = false;
        }
      }else fileSize = (int) file.length();
    }catch(IOException e){
      Logger.err(e);
      isValid = false;
    }
  }
  public boolean isOpened(){
    return file == null;
  }
  public void close(){
    if(file == null){
      Logger.err(new Exception("file closed more than once " + fileName));
      return;
    }
    try{
      file.close();
      file = null;
    }catch(IOException e){
      Logger.err(e);
    }
  }
  private void loadCatalog(){
    if(catalogFileName == null){
      isValid = false;
      return;
    }
    if(catalogList != null) return;
    _RandomAccessFile c = null;
    try{
      c = new _RandomAccessFile(environment.getFile(catalogFileName), "r", true);
      byte[] buf = new byte[(int) c.length()];
      c.readFully(buf);
      _DataInput di = new _DataInputStream(new _ByteArrayInputStream(buf), true);

      Catalog catalog = null;
      catalogList = new ArrayList();
      int count = (int) (c.length() / 8);
      int offset = 0;
      int compOffset = 0;
      for(int i = 0; i <= count; i++){
        catalog = new Catalog();
        if(i != 0){
          offset += di.readUnsignedInt();
          compOffset += di.readUnsignedInt();
          catalog.offset = offset;
          catalog.comp_offset = compOffset;
        }else{
          catalog.offset = 0;
          catalog.comp_offset = 0;
        }
        catalogList.add(catalog);
      }
      fileSize = (int) catalog.offset;
    }catch(IOException e){
      Logger.err(e);
    }finally{
      if(c != null) try{
        c.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
  }

  // ready to work --------------------------------------------------------
  public byte[] getData(int off, int len){
    if(!isValid) throw new IllegalStateException(INVALID);
    byte[] buf = new byte[0];
    if(off < 0 || len < 0 || off > fileSize || off + len > fileSize) return buf;
    try{
      buf = compressed ? getInflatedData(off, len) : getRawData(off, len);
    }catch(IOException e){
      Logger.err(e);
    }catch(DataFormatException e){
      Logger.err(e);
    }
    return buf;
  }
  private byte[] getRawData(int off, int len) throws IOException{
    _ByteArrayOutputStream bos = new _ByteArrayOutputStream(len);
    byte[] buf;
    int blockIndex = off / dataFragmentSize;
    int blockOffset;
    int bytesWrited = 0;
    int bytesToWrite;
    while(bytesWrited < len){
      buf = getRawDataFragment(blockIndex);
      blockOffset = off - blockIndex * dataFragmentSize + bytesWrited;
      bytesToWrite = buf.length - blockOffset;
      bos.write(buf, blockOffset, bytesToWrite);
      bytesWrited += bytesToWrite;
      blockIndex++;
    }
    return bos.toByteArray(len);
  }
  private byte[] getRawDataFragment(int index) throws IOException{
    if(dataFragmentCache == null) dataFragmentCache = new CacheService();
    byte[] data = (byte[]) dataFragmentCache.getCache(index);
    if(data == null){
      int offset = index * dataFragmentSize;
      int len = dataFragmentSize;
      if(len + offset > fileSize) len = fileSize - offset;
      data = new byte[len];
      file.seek(offset);
      file.readFully(data);
      dataFragmentCache.putCache(index, data);
    }
    return data;
  }
  private byte[] getInflatedData(int off, int len) throws IOException, DataFormatException{
    _ByteArrayOutputStream bos = new _ByteArrayOutputStream(len);
    int blockIndex = 0;
    int blockOffset;
    Catalog catalog, next;
    for(int i = 0; i < catalogList.size() - 1; i++){
      catalog = (Catalog) catalogList.get(i);
      next = (Catalog) catalogList.get(i + 1);
      if(off <= next.offset && off >= catalog.offset){
        blockIndex = i;
        blockOffset = (int) (off - catalog.offset);
        break;
      }
    }

    byte[] b;
    int bytesWrited = 0;
    int bytesToWrite;
    while(bytesWrited < len){
      b = getInflatedDataFragment(blockIndex);
      catalog = (Catalog) catalogList.get(blockIndex);
      blockOffset = (int) (off - catalog.offset + bytesWrited);
      bytesToWrite = b.length - blockOffset;
      bos.write(b, blockOffset, bytesToWrite);
      bytesWrited += bytesToWrite;
      blockIndex++;
    }
    return bos.toByteArray(len);
  }
  private byte[] getInflatedDataFragment(int index) throws IOException, DataFormatException{
    if(dataFragmentCache == null) dataFragmentCache = new CacheService();
    byte[] data = (byte[]) dataFragmentCache.getCache(index);
    if(data == null){
      Catalog catalog = (Catalog) catalogList.get(index);
      Catalog next = (Catalog) catalogList.get(index + 1);
      byte[] deflatedData = new byte[(int) (next.comp_offset - catalog.comp_offset)];
      file.seek(catalog.comp_offset);
      file.readFully(deflatedData);

      Inflater a = new Inflater();
      a.setInput(deflatedData);

      byte[] tempBuf = new byte[(int) (next.offset - catalog.offset)];
      _ByteArrayOutputStream bos = new _ByteArrayOutputStream((int) (next.offset - catalog.offset));

      int readCount = 0;
      while(!a.finished()){
        readCount = a.inflate(tempBuf);
        bos.write(tempBuf, 0, readCount);
      }
      data = bos.toByteArray();
      dataFragmentCache.putCache(index, data);
    }
    return data;
  }

  // common ---------------------------------------------------------------
  public String toString(){
    StringBuffer s = new StringBuffer();
    s.append(isValid);
    s.append("|");
    s.append(fileName);
    s.append(":");
    s.append(fileSize);
    return s.toString();
  }

  private class Catalog {
    long offset;
    long comp_offset;
  }

}
