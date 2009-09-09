/*****************************************************************************
 * 
 * @(#)CSDictDataEngine.java  2009/03
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
package com.jin.dic.ks.dic;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.zip.Deflater;

import com.jin.dic.ks.BadFormatException;
import com.jin.dic.ks.ElementUtil;
import com.jin.util.Logger;
import com.jin.util.io._ByteArrayOutputStream;
import com.jin.util.io._DataOutputStream;
import com.jin.util.io._RandomAccessFile;
import com.jin.util.io._ZipFile;

public class CSDictDataEngine extends DictDataEngine {

  private Header  header;
  private Catalog catalog;

  private Map     wordIdMap;
  String          dictName;
  String          srcFolder;
  String          dictFileName;

  public CSDictDataEngine(String dictFileName) throws IOException, BadFormatException {
    this.dictFileName = dictFileName;// TODO weird dictFileName
    File fil = new File(dictFileName);
    dictName = fil.getName();
    srcFolder = getFolderString(fil.getParent());
    checkFolder();
    ini();
    load();
  }
  protected void checkFolder() throws FileNotFoundException{
    File fld = new File(srcFolder);
    if(fld.exists() && fld.isDirectory()){
      List files = new ArrayList();
      files.add(getInfoFileName(dictName));
      files.add(getIndexFileName(dictName));
      files.add(getZipFileName(dictName));
      File f;
      for(int i = 0; i < files.size(); i++){
        f = new File(fld, (String) files.get(i));
        if(!f.exists()) throw new FileNotFoundException(f.getPath());
      }
      return;
    }
    throw new FileNotFoundException("No such folder:" + srcFolder);
  }

  protected void ini(){
    index0 = new DictIndex0();
    index1 = new DictIndex1();
    index2 = new DictIndex2();
    wordIdMap = new TreeMap(new Comparator() {
      public int compare(Object o1, Object o2){
        String w1 = (String) o1;
        String w2 = (String) o2;
        return w1.compareToIgnoreCase(w2);
      }
    });
  }

  protected void load() throws IOException{
    buildWordIdMap();
  }

  public byte[] getData(String word){
    return getDictData((String) wordIdMap.get(word));
  }

  private void buildWordIdMap() throws IOException{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream(getIndexFileName(dictFileName));
      br = new BufferedReader(new InputStreamReader(fis, ENCODING));
      String line;
      String[] info;
      while((line = br.readLine()) != null){
        info = line.split(SEPARATOR);
        if(info == null || info.length != 2){
          continue;
        }// TODO same words may exist simultaneously
        if(!wordIdMap.containsKey(info[0])) wordIdMap.put(info[0], info[1]);
        else{
          // Logger.info(info[0]);
        }
      }
    }finally{
      if(br != null) br.close();
    }
  }

  private byte[] getDictData(String id){
    byte[] data = new byte[0];
    try{
      byte[] tmp = getDataZip(id);
      if(tmp.length != 0){
        data = new byte[tmp.length + 8 - 2];
        _ByteArrayOutputStream baos = new _ByteArrayOutputStream(data);
        _DataOutputStream leos = new _DataOutputStream(baos, true);
        leos.writeInt(tmp.length - 2);
        leos.writeInt(1);
        leos.write(tmp, 2, tmp.length - 2);
        leos.close();
      }
    }catch(IOException e){
      Logger.err(e);
    }
    return data;
  }

  _ZipFile zFile = null;
  private void checkZipFile(){
    if(zFile == null){
      String fileName = getZipFileName(dictFileName);
      try{
        zFile = new _ZipFile(fileName, "r");
      }catch(IOException e){
        Logger.err(e);
        System.exit(1);
      }
    }
  }
  private int getDataLen(String id) throws IOException{
    if(id == null) return -1;
    checkZipFile();
    return zFile.getFileLength(id);
  }

  private byte[] getDataZip(String id) throws IOException{
    checkZipFile();
    return zFile.getData(id);
  }

  public void convert(String desFolder) throws FileNotFoundException{
    stages = 4;
    desFolder = getFolderString(desFolder);
    deleteExistingFile(desFolder);

    header = new Header();
    List fileList = getDataFileList();
    // sortList(fileList); Tree map toggles the sorting job
    clearIndex();
    generateIndex(fileList);
    generateHead();

    writeData(fileList, desFolder);
  }

  private void deleteExistingFile(String desFolder){
    File f = new File(desFolder + dictName);
    if(f.exists()) f.delete();
  }

  private List getDataFileList(){
    // for listener
    info = "getting word items...";
    curStage = 1;
    total = wordIdMap.size();
    processed = 1;

    int dictDataLength = 0;
    int maxChars = 0;
    List fileList = new ArrayList();
    DictDataFile dataFile, temp;
    Iterator i = wordIdMap.entrySet().iterator();
    Entry entry;
    String word, id;
    int offset = 0;
    Map idFileMap = new HashMap();
    while(i.hasNext()){
      entry = (Entry) i.next();
      word = (String) entry.getKey();
      id = (String) entry.getValue();
      maxChars = maxChars < word.length() ? word.length() : maxChars;
      dataFile = new DictDataFile();
      dataFile.setWord(word);
      dataFile.setId(id);
      dataFile.setOffset(offset);
      temp = (DictDataFile) idFileMap.get(id);
      if(temp == null){
        try{
          offset += (getDataLen(id) - 2 + 8);
          idFileMap.put(id, dataFile);
        }catch(IOException e){
          Logger.err(e);
        }
      }else{
        dataFile.setOffset(temp.getOffset());
      }
      fileList.add(dataFile);
      populate();
      processed++;
    }
    dictDataLength = offset;
    header.setDictDataLength(dictDataLength);
    header.setMaxChars(maxChars);
    return fileList;
  }

  // private void sortList(List fileList){
  // KSWordComparator comp = new KSWordComparator();
  // Collections.sort(fileList, comp);
  // }

  private void clearIndex(){
    index0.clear();
    index1.clear();
    index2.clear();
  }

  private void generateIndex(List fileList){
    // for listener
    info = "generating index2 and index1...";
    curStage = 2;
    total = fileList.size();
    processed = 1;

    DictIndex2Item item2;
    DictIndex1Item item1;
    DictIndex0Item item0_root;
    item0_root = new DictIndex0Item();
    item0_root.setChar((char) 0);
    index0.addItem(item0_root);

    DictDataFile dataFile;
    byte[] buf;
    String word;
    for(int i = 0; i < fileList.size(); i++){
      item2 = new DictIndex2Item();
      item1 = new DictIndex1Item();
      dataFile = (DictDataFile) fileList.get(i);
      word = dataFile.getWord();
      buf = getBytes(word);
      item2.setWordIndex(i);
      item2.setData(buf);
      index2.addItem(item2);
      item1.setIndex2Offset(item2.getOffset());
      item1.setDictDataOffset(dataFile.getOffset());
      index1.addItem(item1);
      item0_root.addBranchPath(word, item2.getOffset());

      // for listener
      populate();
      processed++;
    }
    // for listener
    info = "generating index0...";
    curStage = 3;
    total = 1;
    processed = 0;
    populate();

    index2.noMoreItems();
    index1.noMoreItems();
    index0.noMoreItems();

    processed++;
    populate();
  }

  // header and dictInfo
  private void generateHead(){
    DictInfo info = new DictInfo();
    _RandomAccessFile inf = null;
    try{
      inf = new _RandomAccessFile(getInfoFileName(dictFileName), "r");
      if(2 != inf.skipBytes(2)) throw new IOException("SkipBytes failed");
      info.read(inf, (int) inf.length() - 2);
    }catch(IOException e){
      Logger.err(e);
    }catch(BadFormatException e){
      Logger.err(e);
    }finally{
      try{
        if(inf != null) inf.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
    // TODO set info.wordCount...
    header.setWordCount(index1.getSize());
    header.setDictInfo(info);
    header.setHeaderlength(Header.HEADERINFOLENGTH);
    header.setIndex0Length(index0.getLength());
    header.setIndex1Length(index1.getLength());
    header.setIndex2Length(index2.getLength());

    int totalLen = index0.getLength() + index1.getLength() + index2.getLength()
        + header.getDictDataLength();
    int splitedParts_ = totalLen / header.getFractionSize();
    if((totalLen % header.getFractionSize()) != 0) splitedParts_++;
    catalog = new Catalog(splitedParts_);
  }

  private void writeData(List fileList, String desFolder){
    // for listener
    info = "rendering dic file...";
    curStage = 4;
    total = fileList.size();
    processed = 1;

    _RandomAccessFile out = null;
    try{
      out = new _RandomAccessFile(desFolder + dictName, "rw");
      header.write(out);
      catalog.write(out);

      writeDeflatedData(out, ElementUtil.getBytes(index0));
      writeDeflatedData(out, ElementUtil.getBytes(index1));
      writeDeflatedData(out, ElementUtil.getBytes(index2));

      DictDataFile dataFile;
      Map idMap = new HashMap();
      String id;
      for(int i = 0; i < fileList.size(); i++){
        dataFile = (DictDataFile) fileList.get(i);
        id = dataFile.getId();
        if(!idMap.containsKey(id)){
          writeDeflatedData(out, getDictData(id));
          idMap.put(id, null);
        }else{
          // System.out.println(id);
        }

        // for listener
        populate();
        processed++;
      }
      writeDeflatedData(out, null);

      header.setFileLength((int) out.length());
      out.seek(0);
      header.write(out);
      catalog.write(out);
    }catch(IOException e){
      Logger.err(e);
    }finally{
      try{
        if(out != null) out.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
  }

  _ByteArrayOutputStream baos  = new _ByteArrayOutputStream(Header.FRACTIONSIZE);
  int                    block = 0;
  private void writeDeflatedData(DataOutput out, byte[] data) throws IOException{
    if(data != null){
      int toWrite = baos.size() + data.length;
      int dataPos = 0;
      while(toWrite > 0){
        while(dataPos < data.length && baos.size() != Header.FRACTIONSIZE){
          baos.write(data, dataPos++, 1);
          toWrite--;
        }
        if(baos.size() == Header.FRACTIONSIZE){
          writeDeflatedFragment(out, baos.toByteArray(true));
          baos.reset();
        }else break;
      }
    }else{
      writeDeflatedData(out, new byte[4]);
      writeDeflatedFragment(out, baos.toByteArray());
    }
  }

  byte[]   tempBuf = new byte[Header.FRACTIONSIZE];
  Deflater c       = new Deflater(Deflater.DEFAULT_COMPRESSION);
  private void writeDeflatedFragment(DataOutput out, byte[] data) throws IOException{
    c.reset();
    c.setInput(data);
    c.finish();
    int deflatedLen = 0;
    int readCount = 0;
    while(!c.finished()){
      readCount = c.deflate(tempBuf);
      out.write(tempBuf, 0, readCount);
      deflatedLen += readCount;
    }
    ((CatalogItem) catalog.getItem(block)).setDeflatedLength(deflatedLen);
    block++;
  }

}
