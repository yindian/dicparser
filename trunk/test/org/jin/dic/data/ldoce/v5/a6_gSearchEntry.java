package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jin.dic.data.alpha.make.SearchEntryMaker;

public class a6_gSearchEntry {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    BufferedReader br = null;
    List alphaList = new ArrayList();
    List dataOfstList = new ArrayList();
    String desFileName = "alpha/publisher_eId.srh";
    try{
      FileInputStream fis = new FileInputStream("alpha/publisher_eId_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      int count = 0;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 2){
          continue;
        }
        count++;
        alphaList.add(info[0]);
        dataOfstList.add(info[1]);
      }
      SearchEntryMaker.make(alphaList, dataOfstList, desFileName);
    }finally{
      if(br != null) br.close();
    }
  }

}
