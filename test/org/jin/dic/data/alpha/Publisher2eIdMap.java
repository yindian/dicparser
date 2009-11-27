package org.jin.dic.data.alpha;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Publisher2eIdMap {

  static Map p2eId = new HashMap();
  public static void main(String[] args) throws Exception{
    getMap();
    String offset;
    long b = System.nanoTime();
    offset = (String) p2eId.get("u2fc098491a42200a.262cc60a.1180415e23b.2a9e");
    offset = (String) p2eId.get("u2fc098491a42200a.262cc60a.1180415e23b.2aac");
    offset = (String) p2eId.get("u2fc098491a42200a.6e2b450a.11503730847.6fc");
    long a = System.nanoTime();

    System.out.println(" " + offset);
    System.out.println((a - b) / 1000000.0 + "ms");
  }
  public static void getMap() throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream("alpha/publisher_eId_list.txt");
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
    }finally{
      if(br != null) br.close();
    }
  }

}
