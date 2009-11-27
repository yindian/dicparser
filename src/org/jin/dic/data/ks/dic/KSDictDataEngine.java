/*****************************************************************************
 * 
 * @(#)KSDictDataEngine.java  2009/03
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
package org.jin.dic.data.ks.dic;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.util.Logger;
import org.jin.util.io._ByteArrayOutputStream;
import org.jin.util.io._DataInputStream;
import org.jin.util.io._DataOutputStream;
import org.jin.util.io._RandomAccessFile;
import org.jin.util.io._ZipFile;

public class KSDictDataEngine extends DictDataEngine {

  private Header    header;
  private Catalog   catalog;

  _RandomAccessFile dictFile = null;
  String            dictName;
  public KSDictDataEngine(String dictFileName) throws BadFormatException, IOException, DataFormatException {
    File fil = new File(dictFileName);
    dictName = fil.getName();
    dictFile = new _RandomAccessFile(fil, "r", true);
    dictFile.seek(0);
    readBaseInfo(dictFile, (int) fil.length());
    loadIndex();
  }

  private void readBaseInfo(DataInput in, int length) throws BadFormatException, IOException{
    header = new Header();
    header.read(in, -1);// the parameter parameter is not used

    catalog = new Catalog();
    catalog.read(in, header.getSplitedParts_());
  }

  private void loadIndex() throws IOException, BadFormatException, DataFormatException{
    int offset;
    byte[] indexData;
    ByteArrayInputStream bais;
    _DataInputStream ledis;

    offset = 0;
    indexData = getInflatedData(offset, header.getIndex0Length());
    bais = new ByteArrayInputStream(indexData);
    ledis = new _DataInputStream(bais, true);
    index0 = new DictIndex0();
    index0.read(ledis, index0.offsetToIndex(header.getIndex0Length()));
    ledis.close();

    offset = header.getIndex0Length();
    indexData = getInflatedData(offset, header.getIndex1Length());
    bais = new ByteArrayInputStream(indexData);
    ledis = new _DataInputStream(bais, true);
    index1 = new DictIndex1();
    index1.read(ledis, index1.offsetToIndex(header.getIndex1Length()));
    ledis.close();

    offset = header.getIndex0Length() + header.getIndex1Length();
    indexData = getInflatedData(offset, header.getIndex2Length());
    bais = new ByteArrayInputStream(indexData);
    ledis = new _DataInputStream(bais, true);
    index2 = new DictIndex2();
    index2.read(ledis, header.getIndex2Length());
    ledis.close();
  }

  private byte[] getDictData(String word) throws IOException, DataFormatException{
    byte[] data = new byte[0];
    int dataOffset, dataLen, index2Offset, wordIndex;

    index2Offset = getIndex2Offset(word);
    if(index2Offset == -1) return data;

    dataOffset = header.getIndex0Length() + header.getIndex1Length() + index2Offset;
    dataLen = ((header.getMaxChars() + 3) * header.getBufferedWords()) << 1;
    byte[] d = getInflatedData(dataOffset, dataLen);
    wordIndex = extractWordIndex(d, word);
    if(wordIndex == -1) return data;

    DictIndex1Item item = (DictIndex1Item) index1.getItem(wordIndex);
    if(item != null) data = getRawData(item.getDictDataOffset());
    return data;
  }

  private int extractWordIndex(byte[] d, String word) throws IOException{
    char c;
    ByteArrayInputStream bais;
    _DataInputStream ledis;
    bais = new ByteArrayInputStream(d);
    ledis = new _DataInputStream(bais, true);

    _ByteArrayOutputStream bos = new _ByteArrayOutputStream((header.getMaxChars() + 3) << 1);
    _DataOutputStream leos = new _DataOutputStream(bos, true);

    int offset = 0;
    int size = d.length;
    int wordIndex = -1;
    while(offset < size){
      bos.reset();
      while(ledis.available() > 2 && (c = ledis.readChar()) != 0){
        leos.writeChar(c);
      }
      leos.close();
      String foundWord = getString(bos.toByteArray());
      if(ledis.available() < 4) break;
      if(foundWord.equals(word)){
        wordIndex = ledis.readInt();
        break;
      }
      ledis.readInt();
      offset += (bos.size() + 4 + 2);
    }
    return wordIndex;
  }

  int getIndex2Offset(String word){
    DictIndex0Item item0;
    item0 = getNextIndex0Item((char) 0, 0, 0);
    int from;
    int size;
    char c;
    int len = word.length();
    for(int i = 0; i <= len; i++){
      if(item0 == null || (item0.getValue() & 0x80000000) != 0) break;
      c = i == len ? 0 : word.charAt(i);
      size = item0.getChildrenSize();
      from = index0.offsetToIndex(item0.getValue());
      item0 = getNextIndex0Item(c, from, from + size);
    }
    return item0 == null ? -1 : item0.getValue() & 0x7fffffff;
  }

  DictIndex0Item getNextIndex0Item(char c, int from, int to){
    DictIndex0Item item0;
    for(int i = from; i <= to; i++){
      item0 = (DictIndex0Item) index0.getItem(i);
      if(equalsIgnoreCase(c, item0.getChar())) return item0;
    }
    return null;
  }

  // inflated offset to dataBase
  private byte[] getInflatedData(int offset, int len) throws IOException, DataFormatException{
    if(offset < 0 || offset + len > header.getDataLength_()) return new byte[0];
    _ByteArrayOutputStream bos = new _ByteArrayOutputStream(len);
    int blockIndex = offset / header.getFractionSize();
    int blockOffset;
    byte[] b;
    int bytesWrited = 0;
    int bytesToWrite;
    while(bytesWrited < len){
      b = getDataFragment(blockIndex);
      blockOffset = offset - blockIndex * header.getFractionSize() + bytesWrited;
      bytesToWrite = b.length - blockOffset;
      bos.write(b, blockOffset, bytesToWrite);
      bytesWrited += bytesToWrite;
      blockIndex++;
    }
    return bos.toByteArray(len);
  }

  private byte[] getDataFragment(int index) throws IOException, DataFormatException{
    CatalogItem block = catalog.getItem(index);
    byte[] data = new byte[0];
    if(block == null) return data;
    byte[] deflatedData = new byte[block.getDeflatedLength()];
    dictFile.seek(header.getDataOffset_() + block.getOffset());
    dictFile.readFully(deflatedData);

    Inflater a = new Inflater();
    a.setInput(deflatedData);

    byte[] tempBuf = new byte[header.getFractionSize()];
    _ByteArrayOutputStream bos = new _ByteArrayOutputStream(header.getFractionSize());

    int readCount = 0;
    while(!a.finished()){
      readCount = a.inflate(tempBuf);
      bos.write(tempBuf, 0, readCount);
    }
    data = bos.toByteArray();
    return data;
  }

  private byte[] getRawData(int offset) throws IOException, DataFormatException{
    ByteArrayInputStream bais;
    _DataInputStream ledis;
    int len = 8;
    int dataOffset = header.getIndex0Length() + header.getIndex1Length() + header.getIndex2Length() + offset;
    byte[] d = getInflatedData(dataOffset, len);
    bais = new ByteArrayInputStream(d);
    ledis = new _DataInputStream(bais, true);
    len = ledis.readInt();
    int magic = ledis.readInt();
    if(magic != 1) System.out.println(magic);
    ledis.close();

    d = getInflatedData(dataOffset, len + 4 + 4);
    return d;
  }

  public void dump(String desFolder) throws IOException, DataFormatException{
    String info = desFolder + dictName + "_info.dat";
    String index0 = desFolder + dictName + "_index0.dat";
    String index1 = desFolder + dictName + "_index1.dat";
    String index2 = desFolder + dictName + "_index2.dat";
    String dictData = desFolder + dictName + "_dictData.dat";
    _RandomAccessFile f;

    Logger.info(header.getDictInfo().getString());

    int offset = 0;
    f = new _RandomAccessFile(info, "rw", true);
    f.writeChars(header.getDictInfo().getString());
    f.close();

    f = new _RandomAccessFile(index0, "rw", true);
    f.write(getInflatedData(offset, header.getIndex0Length()));
    f.close();
    offset += header.getIndex0Length();

    f = new _RandomAccessFile(index1, "rw", true);
    f.write(getInflatedData(offset, header.getIndex1Length()));
    f.close();
    offset += header.getIndex1Length();

    f = new _RandomAccessFile(index2, "rw", true);
    f.write(getInflatedData(offset, header.getIndex2Length()));
    f.close();
    offset += header.getIndex2Length();

    f = new _RandomAccessFile(dictData, "rw", true);
    int blockIndex = offset / header.getFractionSize();
    int blockOffset;
    byte[] b;
    int bytesWrited = 0;
    int bytesToWrite;
    while(bytesWrited < header.getDictDataLength()){
      b = getDataFragment(blockIndex);
      blockOffset = offset - blockIndex * header.getFractionSize() + bytesWrited;
      bytesToWrite = b.length - blockOffset;
      f.write(b, blockOffset, bytesToWrite);
      bytesWrited += bytesToWrite;
      blockIndex++;
    }
    f.close();
  }

  public byte[] getContent(String word){
    byte[] data = new byte[0];
    try{
      data = getDictData(word);
    }catch(IOException e){
      Logger.err(e);
    }catch(DataFormatException e){
      Logger.err(e);
    }
    return data;
  }

  public void convert(String desFld) throws FileNotFoundException{
    if(desFld.indexOf("@") == 0){
      try{
        dump(desFld.substring(1));
      }catch(IOException e){
        Logger.err(e);
      }catch(DataFormatException e){
        Logger.err(e);
      }
      return;
    }
    // for listener
    info = "getting word items...";
    curStage = 1;
    total = index2.getSize();
    processed = 1;
    stages = 1;

    desFld = getFolderString(desFld);
    File idxFile = new File(getIndexFileName(desFld + dictName));
    File infFile = new File(getInfoFileName(desFld + dictName));
    _ZipFile zFile = null;
    OutputStream osIdx = null;
    OutputStream osInf = null;
    try{
      zFile = new _ZipFile(getZipFileName(desFld + dictName), "w");
      osIdx = new BufferedOutputStream(new FileOutputStream(idxFile));
      osInf = new BufferedOutputStream(new FileOutputStream(infFile));
      int size = index2.getSize();
      String id;
      int offset;
      byte[] data;
      DictIndex1Item item1;
      DictIndex2Item item2;
      osIdx.write(0xff);
      osIdx.write(0xfe);
      osInf.write(0xff);
      osInf.write(0xfe);
      osInf.write(header.getDictInfo().getData());
      Set idSet = new HashSet();
      for(int i = 0; i < size; i++){
        item2 = (DictIndex2Item) index2.getItem(i);
        item1 = (DictIndex1Item) index1.getItem(item2.getWordIndex());
        offset = item1.getDictDataOffset();
        id = Integer.toHexString(offset);
        id = getDataFileName(id);
        data = getRawData(offset);
        osIdx.write(item2.getData());
        osIdx.write(SEPARATORBYTES);
        osIdx.write(getBytes(id));
        osIdx.write(LSBYTES);
        if(!idSet.contains(id)){
          data[6] = (byte) 0xff;
          data[7] = (byte) 0xfe;
          zFile.write(id, data, 8 - 2, data.length - 8 + 2);
          idSet.add(id);
        }else{
          // System.out.println(id);
        }
        populate();
        processed++;
      }
    }catch(FileNotFoundException e){
      Logger.err(e);
    }catch(IOException e){
      Logger.err(e);
    }catch(DataFormatException e){
      Logger.err(e);
    }finally{
      try{
        if(zFile != null) zFile.close();
        if(osIdx != null) osIdx.close();
        if(osInf != null) osInf.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
  }

}
