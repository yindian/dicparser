/*****************************************************************************
 * 
 * @(#)ReplaceP2Id.java  2009/11
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jin.dic.data.pub.CommonConstants;
import org.jin.util.Logger;

public class ReplaceP2Id {

  static int count;
  public static void main(String[] args) throws Exception{
    String srcFld = args[0];
    String desFld = args[1];

    getMap(args[2], args[3]);

    BufferedReader br = null;
    FileInputStream fis = null;
    for(int i = 0; i < 51604; i++){
      try{
        count = i;
        fis = new FileInputStream(Common.getFile(srcFld, i));
        br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
        FileOutputStream fos = new FileOutputStream(Common.getFile(desFld, i));
        fos.write(trim(br.readLine()).getBytes("utf-8"));
        fos.close();
      }catch(FileNotFoundException e){
        e.printStackTrace();
      }catch(UnsupportedEncodingException e){
        e.printStackTrace();
      }catch(IOException e){
        e.printStackTrace();
      }finally{
        try{
          fis.close();
        }catch(IOException e){
          e.printStackTrace();
        }
      }
      if(i % 100 == 0) Logger.info(i);
    }
  }

  static Pattern rmGUID  = Pattern.compile("(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern rmDummy = Pattern.compile("<[^>\\s]*\\s*/>");
  static Pattern prTID   = Pattern.compile("(?<=topic\\s{0,3}?=\\s{0,3}?\")(p\\w{3}-\\w{9})");

  private static String trim(String data){
    Matcher matcher;
    String value;
    matcher = rmGUID.matcher(data);
    while(matcher.find()){
      value = (String) p2eId.get(matcher.group(1));
      if(value == null){
        System.err.println(count + "_keyNexists:" + matcher.group(1));
        return data;
      }else{
        data = matcher.replaceFirst(value);
        matcher = rmGUID.matcher(data);
      }
    }
    matcher = prTID.matcher(data);
    while(matcher.find()){
      value = (String) p2eId.get(matcher.group(1));
      if(value == null){
        System.err.println(count + "_keyNexists:" + matcher.group(1));
        return data;
      }else{
        data = matcher.replaceFirst(value);
        matcher = prTID.matcher(data);
      }
    }
    data = rmDummy.matcher(data).replaceAll("");
    return data;
  }

  static Map p2eId = new HashMap();
  private static void getMap(String p2num, String tP2num) throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream(p2num);
      br = new BufferedReader(new InputStreamReader(fis, CommonConstants.ENCODING));
      String line;
      String[] info;
      while((line = br.readLine()) != null){
        info = line.split(CommonConstants.SEPARATOR);
        if(info == null || info.length != 2){
          continue;
        }
        p2eId.put(info[0], info[1]);
      }
      if(br != null) br.close();

      fis = new FileInputStream(tP2num);
      br = new BufferedReader(new InputStreamReader(fis, CommonConstants.ENCODING));
      while((line = br.readLine()) != null){
        info = line.split(CommonConstants.SEPARATOR);
        if(info == null || info.length != 2){
          continue;
        }
        if(p2eId.containsKey(info[0])){
          System.err.println(count + "_keyexists:" + info[0]);
        }else{
          p2eId.put(info[0], info[1]);
        }
      }
      if(br != null) br.close();
    }finally{
    }
  }

}
