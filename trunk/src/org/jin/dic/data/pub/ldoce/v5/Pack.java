/*****************************************************************************
 * 
 * @(#)Pack.java  2009/11
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
package org.jin.dic.data.pub.ldoce.v5;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jin.dic.data.pub.CommonConstants;
import org.jin.util.Logger;
import org.jin.util.StringUtil;
import org.jin.util.io.FileUtil;
import org.jin.util.io._ZipFile;

public class Pack {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    getMap(args[0]);
    String srcFld = args[1];
    File idxFile = new File(args[2]);
    _ZipFile zFile = new _ZipFile(args[3], "w");
    OutputStream osIdx = null;
    osIdx = new BufferedOutputStream(new FileOutputStream(idxFile));

    String word;
    String name;
    String[] names;
    Iterator it = fileNameMap.entrySet().iterator();
    int count = 0;
    osIdx.write(0xff);
    osIdx.write(0xfe);
    while(it.hasNext()){
      Entry entry = (Entry) it.next();
      word = (String) entry.getKey();
      name = (String) entry.getValue();
      names = name.split(",");
      osIdx.write(StringUtil.getBytesNoBom(word, CommonConstants.ENCODING));
      osIdx.write(CommonConstants.SEPARATORBYTES);
      osIdx.write(StringUtil.getBytesNoBom(name, CommonConstants.ENCODING));
      osIdx.write(CommonConstants.LSBYTES);
      if(!nameMap.contains(name)){
        nameMap.add(name);
        zFile.write(name, 0xff);
        zFile.write(0xfe);
        zFile.write(StringUtil.getBytesNoBom("<CK>", CommonConstants.ENCODING));
        zFile.write(StringUtil.getBytesNoBom("<XX></XX>", CommonConstants.ENCODING));
        zFile.write(StringUtil.getBytesNoBom("<JX><![CDATA[", CommonConstants.ENCODING));
        for(int i = 0; i < names.length; i++){
          if(i != 0) zFile.write(CommonConstants.HBARBYTES);
          zFile.write(FileUtil.getBytesFromFile(Common.getFile(srcFld, Integer.valueOf(names[i]).intValue())));
        }
        zFile.write(CommonConstants.KSDICEBYTES);
        zFile.write(0);
        zFile.write(0);
      }
      if(count % 100 == 0){
        Logger.info(count);
      }
      count++;
    }

    zFile.close();
    osIdx.close();
  }

  static Set nameMap     = new HashSet();
  static Map fileNameMap = new LinkedHashMap();
  private static void getMap(String file) throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream(file);
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      String name;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 5){
          continue;
        }
        name = (String) fileNameMap.get(info[0].toLowerCase());
        if(name == null) fileNameMap.put(info[0].toLowerCase(), info[4]);
        else fileNameMap.put(info[0].toLowerCase(), name + "," + info[4]);
      }
    }finally{
      if(br != null) br.close();
    }
  }

}
