package org.jin.dic.data.ldoce.v5;

import java.io.IOException;

import org.jin.dic.data.alpha.make.DeflatedDataSourceMaker;

public class aa_g {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    String[] fileNames = new String[51604];
    for(int i = 0; i < 51604; i++){
      fileNames[i] = getFile("D:/Jin/Alpha/alpha/entry[fs]2", i);
    }
    DeflatedDataSourceMaker.setFRACTIONSIZE(0x8000);
    DeflatedDataSourceMaker.make(fileNames, "alpha/entry_trim(32k).dat");
  }
  private static String getFile(String fld, int num) throws IOException{
    StringBuffer a = new StringBuffer();
    a.append(fld);
    a.append("/");
    int mark;

    mark = a.length();
    a.append(num / 10000);
    while(a.length() - mark < 5)
      a.insert(mark, "0");
    a.append("/");

    mark = a.length();
    a.append(num / 100);
    while(a.length() - mark < 7)
      a.insert(mark, "0");
    a.append("/");

    mark = a.length();
    a.append(num);
    while(a.length() - mark < 9)
      a.insert(mark, "0");

    a.append(".xml");
    return a.toString();
  }
}
