package org.jin.dic.data.alpha;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jin.dic.data.alpha.make.NewSearchMaker;

public class Maker {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    BufferedReader br = null;
    List alphaList = new ArrayList();
    List dataOfstList = new ArrayList();
    String desFileName = "D:/Jin/Alpha/tmp/NewalphaId.srh";
    try{
      FileInputStream fis = new FileInputStream("D:/Jin/Alpha/tmp/alpha_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      int count = 0;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null){
          continue;
        }
        alphaList.add(info[0]);
        dataOfstList.add(String.valueOf(count));
        count++;
      }
      NewSearchMaker.make(alphaList, dataOfstList, desFileName);
    }finally{
      if(br != null) br.close();
    }
  }

}
