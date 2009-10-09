/*****************************************************************************
 * 
 * @(#)VoiceDataEngine.java  2009/03
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.jin.dic.ConvertListener;
import com.jin.dic.DictIni;
import com.jin.dic.Engine;
import com.jin.dic.ks.BadFormatException;
import com.jin.dic.ks.Index;
import com.jin.util.Logger;
import com.jin.util.RC4Engine;
import com.jin.util.StringUtil;

public abstract class VoiceDataEngine implements Engine {

  public static final String       ENCODING    = DictIni.getKscsEncoding();
  public static final byte[]       LSBYTES     = StringUtil.getBytesNoBom(DictIni.getCsLineSepartor(), ENCODING);

  public static final int          NODATA      = 1;
  public static final String       KSINDEXFILE = DictIni.getKsVoiceIndexFile();
  public static final String       RA4DATAFILE = DictIni.getKsVoiceRA4DataFile();
  public static final String       RA3DATAFILE = DictIni.getKsVoiceRA3DataFile();
  public static final String       CSINDEXFILE = DictIni.getCsVoiceIndexName();
  public static final String       CSDATAFILE  = DictIni.getCsVoiceZipName();

  protected static final int       HEADERLEN   = 0x18;
  protected static final String    keyString   = DictIni.getKsVoiceKey();
  protected static byte[]          keyBytes    = null;
  protected static final RC4Engine cipher      = new RC4Engine();
  static{
    try{
      keyBytes = keyString.getBytes(DictIni.getKsVoiceKeyEncoding());
      keyBytes[keyBytes.length - 1] = 0;
      cipher.init(keyBytes);
    }catch(UnsupportedEncodingException e){
      Logger.err(e);
    }
  }

  protected Index                  index0      = null;
  protected Index                  index1      = null;
  protected Index                  index2      = null;
  protected String                 srcFolder   = null;
  public VoiceDataEngine(String srcFolder) throws IOException, BadFormatException {
    this.srcFolder = getFolderString(srcFolder);
    ini();
    checkFolder();
    load();
  }
  protected abstract void ini();
  protected abstract void checkFolder() throws FileNotFoundException;
  protected abstract void load() throws IOException, BadFormatException;

  protected List   listenerList = new ArrayList();
  protected int    stages       = 0;
  protected int    curStage     = 0;
  protected int    total        = 0;
  protected int    processed    = 0;
  protected String info         = null;
  protected void populate(){
    ConvertListener listener;
    for(int i = 0; i < listenerList.size(); i++){
      listener = (ConvertListener) listenerList.get(i);
      listener.update(stages, curStage, total, processed, info);
    }
  }
  public void addConverListener(ConvertListener listener){
    listenerList.add(listener);
  }

  public static final String getFolderString(String fld) throws FileNotFoundException{
    File folder = new File(fld);
    if(!folder.exists()) throw new FileNotFoundException("No such folder: " + fld);
    if(fld.endsWith("\\") || fld.endsWith("/")) return fld;
    else return fld + File.separator;
  }

  public static final int[] getWordKey(byte[] bytes){
    return getWordKey(StringUtil.valueOf(bytes, DictIni.getKsLookupEncoding()));
  }

  public static final int[] getWordKey(String word){
    int[] offset = new int[] { 0, 0 };
    int len = word.length();
    int[] tmp = new int[4];
    for(int i = 0; i < 4; i++){
      if(len - 1 < i) tmp[i] = 0;
      else{
        tmp[i] = Character.toLowerCase(word.charAt(i));
        if(tmp[i] <= 'z' && tmp[i] >= 'a') tmp[i] -= ('a' - (i > 1 ? 1 : 0));
        else tmp[i] = 0;
      }
    }
    offset[0] = tmp[0] * 26 + tmp[1];
    offset[1] = (tmp[3] << 6) + tmp[2];
    return offset;
  }

  public static final int getIntWordKey(int[] offset){
    return (offset[0] << 12) | offset[1];
  }

  public static final byte[] getBytesNoBom(String s){
    return StringUtil.getBytesNoBom(s, ENCODING);
  }

  public static final byte[] getBytes(String s, String encoding){
    return StringUtil.getBytes(s, encoding);
  }

}
