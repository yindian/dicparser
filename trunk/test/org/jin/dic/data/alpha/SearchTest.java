package org.jin.dic.data.alpha;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.jin.util.StringUtil;
import org.jin.util.io._RandomAccessFile;

public class SearchTest {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    List wordList = new ArrayList();
    FileInputStream fis = new FileInputStream("alpha/alpha_list.txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(fis, "unicode"));
    String line;
    String[] info;
    int count = 0;
    while((line = br.readLine()) != null){
      info = line.split("\t");
      if(info == null){
        continue;
      }
      wordList.add(info[0]);
      count++;
    }

    _RandomAccessFile os = new _RandomAccessFile("alpha/aou..txt", "rw");
    os.write(0xff);
    os.write(0xfe);
    int hash;
    char h;
    for(int i = 0; i < wordList.size(); i++){
      hash = wordList.get(i).hashCode();
      h = (char)((hash & 0xffff) ^(hash & 0xffff0000));
      os.write(StringUtil.getBytesNoBom(String.valueOf((int)h), "unicode"));
//      os.write(StringUtil.getBytesNoBom("\t", "unicode"));
//      os.write(StringUtil.getBytesNoBom((String) wordList.get(i), "unicode"));
      os.write(StringUtil.getBytesNoBom("\r\n", "unicode"));
    }
    os.close();

  }

}
