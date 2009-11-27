package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.File;
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

public class ac_rplP2Id {

  static int count;
  public static void main(String[] args) throws Exception{
    String srcFld = "D:/Jin/Alpha/alpha/entry[fs]";
    String desFld = "D:/Jin/Alpha/alpha/entry[fs]2";

    getMap();

    BufferedReader br = null;
    FileInputStream fis = null;
    for(int i = 0; i < 51604; i++){
      try{
        count = i;
        fis = new FileInputStream(getFile(srcFld, i));
        br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
        FileOutputStream fos = new FileOutputStream(getFile(desFld, i));
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
      if(i % 100 == 0) System.out.println(i);
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
  private static void getMap() throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream("alpha/p2num_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 2){
          continue;
        }
        p2eId.put(info[0], info[1]);
      }
      if(br != null) br.close();

      fis = new FileInputStream("alpha/thesaurusP_num_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      while((line = br.readLine()) != null){
        info = line.split("\t");
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
