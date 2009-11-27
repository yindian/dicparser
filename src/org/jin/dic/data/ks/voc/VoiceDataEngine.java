/*****************************************************************************
 * 
 * @(#)VoiceDataEngine.java  2009/03
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jin.dic.data.ConvertListener;
import org.jin.dic.data.Engine;
import org.jin.dic.data.ks.BadFormatException;
import org.jin.dic.data.ks.Index;
import org.jin.util.Logger;
import org.jin.util.RC4Engine;
import org.jin.util.StringUtil;


public abstract class VoiceDataEngine implements Engine {

  public static final String       ENCODING    = "UTF-16";
  public static final byte[]       LSBYTES     = StringUtil.getBytesNoBom("\r\n", ENCODING);

  public static final int          NODATA      = 1;
  public static final String       KSINDEXFILE = "MVOX.IDX";
  public static final String       RA4DATAFILE = "MVOICE0.DAT";
  public static final String       RA3DATAFILE = "MVOICE1.DAT";
  public static final String       CSINDEXFILE = "index.txt";
  public static final String       CSDATAFILE  = "voicedata.zip";

  protected static final int       HEADERLEN   = 0x18;
  protected static final String    keyString   = "(C) Copyright by Kingsoft , 1997.(C) Portion by Dex.?";
  protected static byte[]          keyBytes    = null;
  protected static final RC4Engine cipher      = new RC4Engine();
  static{
    try{
      keyBytes = keyString.getBytes("ISO-8859-1");
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
    return getWordKey(StringUtil.valueOf(bytes, "ISO-8859-1"));
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
