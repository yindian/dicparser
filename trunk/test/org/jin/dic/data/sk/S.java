package org.jin.dic.data.sk;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;

import org.jin.util.BytesUtil;


public class S {

  /**
   * @param args
   * @throws UnsupportedEncodingException
   */
  public static void main(String[] args) throws UnsupportedEncodingException{
    // TODO Auto-generated method stub
   Object o = (Integer)null;
    byte[] data;// 5AC3BC7269636800
    String s = "Z¨¹rich";
    data = s.getBytes("iso8859-1");// utf-8
    System.out.println(BytesUtil.convert(data));
    File fld = new File("C:/Program Files/Longman/ldoce4v2/data\\");
    System.out.println(fld.getAbsolutePath());

    String srcFld = "D:/Jin/Data/Longman/ldoce/skin/ldoce/data";
    fld = new File(srcFld);
    File[] files = fld.listFiles();
    for(int i= 0; i < files.length ; i++){
      System.out.println("<link rel=\"stylesheet\" href=\""+files[i].getAbsolutePath()+"\" />");
    }
  }

}
