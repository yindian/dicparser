package org.jin.dic.data.alpha;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AlphaListMemoryTest {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{

    BufferedReader br = null;
    String line = null;
    FileInputStream fis = new FileInputStream("aeu.txt");
    br = new BufferedReader(new InputStreamReader(fis,"utf8"));
    List a = new ArrayList();
    while((line = br.readLine()) != null){
      a.add(line);
    }
    br.close();

    int count = 1024;
    Runtime.getRuntime().gc();
    long mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    String[] names = new String[a.size()];
    for(int i = 0; i < a.size(); i++){
      names[i] = (String) a.get(i);
    }

    Pattern match = Pattern.compile("[zZ].+h");//(\\w)\\w{3}\\1
    int count_ = 0;
    long b = System.nanoTime();
    for(int i = 0; i < names.length; i++){
      if(match.matcher((String) a.get(i)).matches()){
        count_++;
        System.out.println((String) a.get(i));
        if(count_ > 1000) break;
      }
    }
    System.out.println((System.nanoTime() - b) / 1000000 + "ms");
    System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - mem) / count + "KB");
  }

}
