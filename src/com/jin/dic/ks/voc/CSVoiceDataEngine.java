/*****************************************************************************
 * 
 * @(#)CSVoiceDataEngine.java  2009/03
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
package com.jin.dic.ks.voc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jin.dic.ks.BadFormatException;
import com.jin.util.Logger;
import com.jin.util.io._RandomAccessFile;
import com.jin.util.io._ZipFile;

public final class CSVoiceDataEngine extends VoiceDataEngine {

  private Map wordIdMap;

  public CSVoiceDataEngine(String srcFolder) throws IOException, BadFormatException {
    super(srcFolder);
  }

  protected void ini(){
    index0 = new VoiceIndex0();
    index1 = new VoiceIndex1();
    index2 = new VoiceIndex2();
    wordIdMap = new LinkedHashMap();
  }

  protected void checkFolder() throws FileNotFoundException{
    File fld = new File(srcFolder);
    if(fld.exists() && fld.isDirectory()){
      List files = new ArrayList();
      files.add(CSINDEXFILE);
      files.add(CSDATAFILE);
      File f;
      for(int i = 0; i < files.size(); i++){
        f = new File(fld, (String) files.get(i));
        if(!f.exists()) throw new FileNotFoundException(f.getPath());
      }
      return;
    }
    throw new FileNotFoundException("No such folder:" + srcFolder);
  }

  protected void load() throws IOException{
    buildWordIdMap();
  }

  public byte[] getData(String word){
    return getVoiceData((String) wordIdMap.get(word));
  }

  private void buildWordIdMap() throws IOException{
    FileInputStream fis = new FileInputStream(srcFolder + CSINDEXFILE);
    BufferedReader br = new BufferedReader(new InputStreamReader(fis, ENCODING));
    String line;
    String[] info;
    while((line = br.readLine()) != null){
      info = line.split("\\t");
      if(info == null || info.length != 2) continue;
      if(!wordIdMap.containsKey(info[0])) wordIdMap.put(info[0], info[1]);
    }
    br.close();
  }

  public void convert(String desFolder) throws FileNotFoundException{
    desFolder = getFolderString(desFolder);
    List fileList = getDataFileList();
    sortList(fileList);
    clearIndex();
    generateIndex2(fileList);
    generateIndex1And0();

    deleteExistingFile(desFolder, KSINDEXFILE);
    deleteExistingFile(desFolder, RA4DATAFILE);
    deleteExistingFile(desFolder, RA3DATAFILE);

    writeData(fileList, desFolder);
    writeIndex(desFolder);
  }

  private List getDataFileList(){
    List fileList = new ArrayList();
    VoiceDataFile dataFile = null;
    Iterator i = wordIdMap.entrySet().iterator();
    Entry entry;
    String word, id;
    while(i.hasNext()){
      entry = (Entry) i.next();
      word = (String) entry.getKey();
      id = (String) entry.getValue();
      dataFile = new VoiceDataFile();
      dataFile.setFileName(word);
      dataFile.setWord(word);
      dataFile.setId(id);
      fileList.add(dataFile);
    }
    return fileList;
  }

  private void sortList(List fileList){
    Collections.sort(fileList, new Comparator() {
      public int compare(Object o1, Object o2){
        VoiceDataFile w1 = (VoiceDataFile) o1;
        VoiceDataFile w2 = (VoiceDataFile) o2;
        return w1.getWord().compareToIgnoreCase(w2.getWord());
      }
    });
  }

  private void clearIndex(){
    index0.clear();
    index1.clear();
    index2.clear();
  }

  private void generateIndex2(List fileList){
    VoiceIndex2Item item2;
    VoiceDataFile dataFile;
    byte[] buf = null;
    for(int i = 0; i < fileList.size(); i++){
      dataFile = (VoiceDataFile) fileList.get(i);
      buf = dataFile.getWord().getBytes();
      item2 = new VoiceIndex2Item();
      item2.setDataIndex(0);
      item2.setData(buf);
      index2.addItem(item2);
    }
    index2.noMoreItems();
  }
  private void generateIndex1And0(){
    int[] wordKeys;
    VoiceIndex2Item item2;
    VoiceIndex1Item item1;
    VoiceIndex0Item item0;

    int index2Size = index2.getSize();
    int lastKey0 = -1;
    int lastKey1 = -1;
    for(int i = 0; i < index2Size; i++){
      item2 = (VoiceIndex2Item) index2.getItem(i);
      item1 = new VoiceIndex1Item();
      item0 = new VoiceIndex0Item();

      wordKeys = getWordKey(item2.getData());

      item1.setIndex2Offset(item2.getOffset());
      item1.setKey(wordKeys[1]);
      if(wordKeys[0] != lastKey0 || wordKeys[1] != lastKey1) index1.addItem(item1);

      item0.setKey(wordKeys[0]);
      item0.setIndex1(index1.offsetToIndex(item1.getOffset()));
      if(wordKeys[0] != lastKey0) index0.addItem(item0);

      lastKey1 = wordKeys[1];
      lastKey0 = wordKeys[0];
    }
    index1.noMoreItems();
    ((VoiceIndex1Item) index1.getItem(index1.getSize() - 1)).setIndex2Offset(index2.getLength());

    item0 = new VoiceIndex0Item();
    item0.setKey(26 * 26);
    item0.setIndex1(index1.getSize() - 1);
    index0.addItem(item0);

    index0.noMoreItems();
  }

  private void writeData(List fileList, String desFolder){
    VoiceIndex2Item item2;
    VoiceDataFile dataFile;
    int dataIndex;
    for(int i = 0; i < fileList.size(); i++){
      dataFile = (VoiceDataFile) fileList.get(i);
      item2 = (VoiceIndex2Item) index2.getItem(i);
      dataIndex = appendData(desFolder, dataFile.getId());
      item2.setDataIndex(dataIndex);
    }
  }

  private void writeIndex(String desFolder){
    _RandomAccessFile out = null;
    try{
      out = new _RandomAccessFile(desFolder + KSINDEXFILE, "rw", true);
      index0.write(out);
      index1.write(out);
      index2.write(out);
    }catch(FileNotFoundException e){
      Logger.err(e);
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

  private void deleteExistingFile(String desFolder, String fileName){
    File f = new File(desFolder + fileName);
    f.delete();
  }

  private int appendData(String desFolder, String id){
    int offset = -1;
    String fileName;
    int type = VoiceDataFile.RA4;
    _RandomAccessFile out = null;
    try{
      byte[] data = getVoiceData(id);
      if(data == null) return offset;
      type = data[5];
      fileName = type == VoiceDataFile.RA3 ? RA3DATAFILE : RA4DATAFILE;
      out = new _RandomAccessFile(desFolder + fileName, "rw", true);
      offset = (int) out.length();
      out.seek(offset);
      cipher.reset();
      cipher.setXY(offset);
      cipher.processBytes(data, 0);
      out.write(data);
    }catch(FileNotFoundException e){
      Logger.err(e);
    }catch(IOException e){
      Logger.err(e);
    }finally{
      try{
        if(out != null) out.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
    if(type == VoiceDataFile.RA3) offset = offset | 0x10000000;
    return offset;
  }

  private byte[] getVoiceData(String id){
    byte[] data = new byte[0];
    try{
      data = getDataZip(id);
    }catch(IOException e){
      Logger.err(e);
    }
    return data;
  }

  _ZipFile zFile = null;
  private byte[] getDataZip(String id) throws IOException{
    byte[] data = new byte[0];
    if(id == null) return data;
    if(zFile == null){
      String fileName = srcFolder + CSDATAFILE;
      zFile = new _ZipFile(fileName, "r");
    }
    return zFile.getData(id);
  }

}
