package org.jin.dic.data.ldoce.v5;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jin.util.StringUtil;
import org.jin.util.io.FileUtil;
import org.jin.util.io._ZipFile;

public class zz_gKSDic {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    getMap();
    save();
  }
  private static void save() throws Exception{
    _ZipFile zFile = null;
    zFile = new _ZipFile("alpha/test.zip", "w");
    String srcFld = "D:/Jin/Alpha/alpha/entry[fs]3";
    String encoding = "unicode";
    File idxFile = new File("alpha/index.txt");
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
      osIdx.write(StringUtil.getBytesNoBom(word, encoding));
      osIdx.write(StringUtil.getBytesNoBom("\t", encoding));
      osIdx.write(StringUtil.getBytesNoBom(name, encoding));
      osIdx.write(StringUtil.getBytesNoBom("\r\n", encoding));
      if(!nameMap.contains(name)){
        nameMap.add(name);
        zFile.write(name, 0xff);
        zFile.write(0xfe);
        zFile.write(StringUtil.getBytesNoBom("<CK>", encoding));
        zFile.write(StringUtil.getBytesNoBom("<XX></XX>", encoding));
        zFile.write(StringUtil.getBytesNoBom("<JX><![CDATA[", encoding));
        for(int i = 0; i < names.length; i++){
          zFile.write(FileUtil.getBytesFromFile(getFile(srcFld, Integer.valueOf(names[i]).intValue())));
        }
        zFile.write(StringUtil.getBytesNoBom("]]></JX></CK>", encoding));
      }
      if(count % 100 == 0){
        System.out.println(count);
      }
      count++;
    }

    zFile.close();
    osIdx.close();
  }

  static Set nameMap     = new HashSet();
  static Map fileNameMap = new LinkedHashMap();
  private static void getMap() throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream("alpha/bak/alpha_list.txt");
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
  private static File getFile(String fld, int num) throws IOException{
    File folder;
    StringBuffer a = new StringBuffer();
    a.append(fld);
    a.append("/");
    int mark;

    mark = a.length();
    a.append(num / 10000);
    while(a.length() - mark < 5)
      a.insert(mark, "0");
    folder = new File(a.toString());
    if(!folder.exists()) folder.mkdir();
    a.append("/");

    mark = a.length();
    a.append(num / 100);
    while(a.length() - mark < 7)
      a.insert(mark, "0");
    folder = new File(a.toString());
    if(!folder.exists()) folder.mkdir();
    a.append("/");

    mark = a.length();
    a.append(num);
    while(a.length() - mark < 9)
      a.insert(mark, "0");

    a.append(".xml");
    File file = new File(a.toString());
    file.createNewFile();
    return file;
  }
  
}
