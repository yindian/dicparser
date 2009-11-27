package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jin.dic.data.alpha.make.SearchEntryMaker;

public class a4_gAlphaHom2aId {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    BufferedReader br = null;
    List alphaList = new ArrayList();
    List dataOfstList = new ArrayList();
    String desFileName = "alpha/alphaPos_aId.srh";
    try{
      FileInputStream fis = new FileInputStream("alpha/alpha_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      int count = 0;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 5){
          continue;
        }
        alphaList.add(info[0] + "_" + info[3]);
        dataOfstList.add(String.valueOf(count));
        count++;
      }
      SearchEntryMaker.make(alphaList, dataOfstList, desFileName);
    }finally{
      if(br != null) br.close();
    }
  }
}
