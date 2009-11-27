/*****************************************************************************
 * 
 * @(#)KSVoiceDataEngine.java  2009/03
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
package org.jin.dic.data.ks.voc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.util.Logger;
import org.jin.util.io._RandomAccessFile;
import org.jin.util.io._ZipFile;


public final class KSVoiceDataEngine extends VoiceDataEngine {

  public KSVoiceDataEngine(String srcFolder) throws IOException, BadFormatException {
    super(srcFolder);
  }

  protected void ini(){
    index0 = new VoiceIndex0();
    index1 = new VoiceIndex1();
    index2 = new VoiceIndex2();
  }

  protected void checkFolder() throws FileNotFoundException{
    File fld = new File(srcFolder);
    if(fld.exists() && fld.isDirectory()){
      List files = new ArrayList();
      files.add(KSINDEXFILE);
      files.add(RA4DATAFILE);
      files.add(RA3DATAFILE);
      File f;
      for(int i = 0; i < files.size(); i++){
        f = new File(fld, (String) files.get(i));
        if(!f.exists()) throw new FileNotFoundException(f.getPath());
      }
    }
  }

  protected void load() throws IOException, BadFormatException{
    _RandomAccessFile in = null;
    try{
      in = new _RandomAccessFile(srcFolder + KSINDEXFILE, "r", true);
      int size;

      size = 26 * 26;
      index0.read(in, ++size);

      size = ((VoiceIndex0Item) index0.getItem(index0.getSize() - 1)).getIndex1();
      index1.read(in, ++size);

      size = ((VoiceIndex1Item) index1.getItem(index1.getSize() - 1)).getIndex2Offset();
      index2.read(in, size);// size parameter is actually the length

    }finally{
      try{
        if(in != null) in.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
  }

  public int getVoiceDataOffset(String word){
    byte[] wordBytes = getBytes(word,"ISO8859-1");
    int[] keys = getWordKey(word);
    VoiceIndex0Item index0ItemF, index0ItemT;
    VoiceIndex1Item index1ItemF, index1ItemT;
    VoiceIndex2Item index2ItemF;

    int offset = NODATA;
    int f0, t0;
    int f1, t1;
    int f2, t2;

    index0ItemF = (VoiceIndex0Item) index0.getItem(keys[0]);
    index0ItemT = (VoiceIndex0Item) index0.getItem(keys[0] + 1);
    f0 = index0ItemF.getIndex1();
    t0 = index0ItemT.getIndex1();

    for(int i = f0; i <= t0; i++){
      index1ItemF = (VoiceIndex1Item) index1.getItem(i);
      index1ItemT = (VoiceIndex1Item) index1.getItem(i + 1);
      if(index1ItemF.getKey() == keys[1]){

        f1 = index1ItemF.getIndex2Offset();
        t1 = index1ItemT.getIndex2Offset();
        f2 = index2.offsetToIndex(f1);
        t2 = index2.offsetToIndex(t1);
        for(int j = f2; j <= t2; j++){
          index2ItemF = (VoiceIndex2Item) index2.getItem(j);
          if(Arrays.equals(wordBytes, index2ItemF.getData())){
            return index2ItemF.getDataIndex();
          }
        }
      }
    }
    return offset;
  }

  public byte[] getVoiceData(int offset){
    return getVoiceData(srcFolder, offset);
  }

  public byte[] getContent(String word){
    int offset = getVoiceDataOffset(word);
    byte[] data = new byte[0];
    if(offset != NODATA) data = getVoiceData(srcFolder, offset);
    return data;
  }

  private byte[] getVoiceData(String folder, int offset){
    _RandomAccessFile in = null;
    byte[] data = new byte[0];
    int leftDataLen = 0;
    if(offset == -1) return data;
    byte[] head = new byte[HEADERLEN];
    try{
      String fileName = RA4DATAFILE;
      if((offset & 0xf0000000) == 0x10000000){
        offset = offset & 0x0fffffff;
        fileName = RA3DATAFILE;
      }
      in = new _RandomAccessFile(folder + fileName, "r", true);
      in.seek(offset);
      in.readFully(head);

      cipher.reset();
      cipher.setXY(offset);
      cipher.processBytes(head);
      switch(head[5]){
        case 3:// ra3? MVOICE1.DAT
          leftDataLen = ((head[0x14] & 0xff) << 8 | (head[0x15] & 0xff)) + 0x08;
          break;
        case 4:// ra4 MVOICE0.DAT
          leftDataLen = ((head[0x0e] & 0xff) << 8 | (head[0x0f] & 0xff)) + 0x10;
          break;
        default:
          return null;
      }
      data = new byte[leftDataLen + HEADERLEN];
      in.readFully(data, HEADERLEN, leftDataLen);
      cipher.processBytes(data, HEADERLEN);

      System.arraycopy(head, 0, data, 0, HEADERLEN);
    }catch(FileNotFoundException e){
      Logger.err(e);
    }catch(IOException e){
      Logger.err(e);
    }finally{
      try{
        if(in != null) in.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
    return data;
  }

  public void convert(String desFld) throws FileNotFoundException{
    desFld = getFolderString(desFld);
    File idxFile = new File(desFld + CSINDEXFILE);
    _ZipFile zFile = null;
    OutputStream osIdx = null;
    try{
      zFile = new _ZipFile(desFld + CSDATAFILE, "w");
      osIdx = new BufferedOutputStream(new FileOutputStream(idxFile));
      int size = index2.getSize();
      String word, id;
      int offset;
      byte[] data;
      VoiceIndex2Item item2;
      osIdx.write(0xff);
      osIdx.write(0xfe);
      for(int i = 0; i < size; i++){
        item2 = (VoiceIndex2Item) index2.getItem(i);
        word = new String(item2.getData());
        id = String.valueOf(i);
        offset = item2.getDataIndex();
        data = getVoiceData(offset);
        osIdx.write(getBytesNoBom(word));
        osIdx.write(getBytesNoBom("\t"));
        osIdx.write(getBytesNoBom(id));
        osIdx.write(LSBYTES);
        zFile.write(id, data);
      }
    }catch(FileNotFoundException e){
      Logger.err(e);
    }catch(IOException e){
      Logger.err(e);
    }finally{
      try{
        if(zFile != null) zFile.close();
        if(osIdx != null) osIdx.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
  }

}
