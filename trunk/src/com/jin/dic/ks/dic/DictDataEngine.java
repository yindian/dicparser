/*****************************************************************************
 * 
 * @(#)DictDataEngine.java  2009/03
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jin.dic.ConvertListener;
import com.jin.dic.Engine;
import com.jin.dic.ks.Index;
import com.jin.util.StringUtil;

public abstract class DictDataEngine implements Engine {

  public static final String ENCODING     = "UTF-16";

  protected Index            index0       = null;
  protected Index            index1       = null;
  protected Index            index2       = null;

  // for listeners
  protected List             listenerList = new ArrayList();
  protected int              stages       = 0;
  protected int              curStage     = 0;
  protected int              total        = 0;
  protected int              processed    = 0;
  protected String           info         = null;

  protected final void populate(){
    ConvertListener listener;
    for(int i = 0; i < listenerList.size(); i++){
      listener = (ConvertListener) listenerList.get(i);
      listener.update(stages, curStage, total, processed, info);
    }
  }

  public final void addConverListener(ConvertListener listener){
    listenerList.add(listener);
  }

  // for converter
  static final byte[] LSBYTES        = StringUtil.getBytesNoBom("\r\n", ENCODING);
  static final String SEPARATOR      = "\t";
  static final byte[] SEPARATORBYTES = StringUtil.getBytesNoBom(SEPARATOR, ENCODING);

  public static final String getInfoFileName(String dictName){
    return dictName + ".inf";
  }

  public static final String getIndexFileName(String dictName){
    return dictName + ".txt";
  }

  public static final String getDataFileName(String id){
    return id + ".txt";
  }

  public static final String getZipFileName(String dictName){
    return dictName + ".zip";
  }

  // utility
  public static final String getFolderString(String fld) throws FileNotFoundException{
    File folder = new File(fld);
    if(!folder.exists()) throw new FileNotFoundException("No such folder: " + fld);
    if(fld.endsWith("\\") || fld.endsWith("/")) return fld;
    else return fld + File.separator;
  }

  public static final byte[] getBytes(String s){
    return StringUtil.getBytesNoBom(s, ENCODING);
  }

  public static final String getString(byte[] bytes){
    return StringUtil.valueOf(bytes);
  }

  public static final boolean equals(byte[] data0, byte[] data1){
    return Arrays.equals(data0, data1);
  }

  public static final boolean equalsIgnoreCase(char c0, char c1){
    return c0 == c1 || Character.toLowerCase(c0) == Character.toLowerCase(c1);
  }

}
