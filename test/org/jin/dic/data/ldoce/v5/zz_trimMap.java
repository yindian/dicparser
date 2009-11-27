package org.jin.dic.data.ldoce.v5;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jin.util.StringUtil;

public class zz_trimMap {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    // TODO Auto-generated method stub
    getMap();
    saveMap();
  }
  static Map oldMap   = new LinkedHashMap();
  static Map finalMap = new LinkedHashMap();
  private static void getMap() throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream("alpha/bak/Copy (2) of className_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 2){
          continue;
        }
        oldMap.put(info[0], info[1]);
      }
      fis = new FileInputStream("alpha/className_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String oldValue;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 2){
          continue;
        }
        oldValue = (String) oldMap.get(info[0]);
        if(oldValue == null) finalMap.put(info[0], info[1]);
        else finalMap.put(info[0], oldValue);
      }
    }finally{
      if(br != null) br.close();
    }
  }

  private static void saveMap() throws IOException{
    String encoding = "unicode";
    String outFileName = "alpha/good.txt";
    File file = new File(outFileName);
    OutputStream os = null;
    os = new BufferedOutputStream(new FileOutputStream(file));
    os.write(0xff);
    os.write(0xfe);
    Set s = finalMap.entrySet();
    Iterator i = s.iterator();
    Entry entry;
    while(i.hasNext()){
      entry = (Entry) i.next();
      os.write(StringUtil.getBytesNoBom((String) entry.getKey(), encoding));
      os.write(StringUtil.getBytesNoBom("\t", encoding));
      os.write(StringUtil.getBytesNoBom((String) entry.getValue(), encoding));
      os.write(StringUtil.getBytesNoBom("\r\n", encoding));
    }
    os.close();
  }
}
