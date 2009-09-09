/*****************************************************************************
 * 
 * @(#)KSVoiceDataEngineSimple.java  2009/03
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jin.dic.ks.BadFormatException;
import com.jin.util.Logger;
import com.jin.util.io._RandomAccessFile;

public class KSVoiceDataEngineSimple extends VoiceDataEngine {

  private static int index0Length = 26 * 26 * 2 + 2;
  private int        index1Length = 0;

  public KSVoiceDataEngineSimple(String srcFolder) throws IOException, BadFormatException {
    super(srcFolder);
  }

  protected void ini(){

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
      return;
    }
  }

  protected void load(){

  }

  public int getVoiceDataOffset(String word){
    String indexFileName = srcFolder + KSINDEXFILE;
    _RandomAccessFile in = null;
    int index = NODATA;
    try{
      if(word == null || word.length() == 0) return index;
      in = new _RandomAccessFile(indexFileName, "r", true);

      int[] offset = getWordKey(word);

      int temp;
      in.seek(index0Length - 2);
      temp = in.readChar() + 1;
      index1Length = temp << 2;

      int fjFrom, fjTo;
      int sjFromData, sjFromOffset;
      int sjToData, sjToOffset;

      in.seek(offset[0] << 1);
      fjFrom = in.readChar();
      fjTo = in.readChar();
      in.seek((fjFrom << 2) + index0Length);
      long cur;
      for(int n = 0; n < (fjTo - fjFrom); n++){
        temp = in.readInt();
        sjFromData = temp & 0x00000fff;
        sjFromOffset = (temp & 0xfffff000) >> 12;
        cur = in.getFilePointer();
        if(offset[1] == sjFromData){
          temp = in.readInt();
          sjToData = temp & 0x00000fff;
          sjToOffset = (temp & 0xfffff000) >> 12;
          temp = sjToData;// useless,just place holding

          sjFromOffset += (index0Length + index1Length);
          sjToOffset += (index0Length + index1Length);

          int nn = 0;
          byte[] buf;
          int indexTemp;
          in.seek(sjFromOffset);
          while(in.getFilePointer() < sjToOffset){
            nn = in.read();
            buf = new byte[nn];
            in.readFully(buf, 0, nn);
            indexTemp = in.readInt();
            if(word.equals(new String(buf))) return indexTemp;
          }
          in.seek(cur);
        }
      }
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
    return index;
  }

  public byte[] getVoiceData(int offset){
    return getVoiceData(srcFolder, offset);
  }

  public byte[] getData(String word){
    int offset = getVoiceDataOffset(word);
    byte[] data = new byte[0];
    if(offset != NODATA) data = getVoiceData(srcFolder, offset);
    return data;
  }

  private byte[] getVoiceData(String folder, int offset){
    _RandomAccessFile in = null;
    byte[] data = new byte[0];
    if(offset == -1) return data;
    byte[] head = new byte[HEADERLEN];
    int leftDataLen = 0;
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
      cipher.processBytes(head, 0);
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

  public void convert(String desFld){

  }

}
