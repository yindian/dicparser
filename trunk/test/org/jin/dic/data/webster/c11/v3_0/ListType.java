package org.jin.dic.data.webster.c11.v3_0;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jin.util.io.InputStreamUtil;

public class ListType {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{

    int searchType = 0xd4;
    String search = Integer.toHexString(searchType);
    InputStream in = new BufferedInputStream(new FileInputStream("C11/C11.inflate.cmp"));
    Parse parse = new Parse();
    int type, len;
    byte[] buf = new byte[2048];
    String temp;
    LinkedList valueList = new LinkedList();
    LinkedList typeList = new LinkedList();
    int size = 2;
    while(in.available() > 0){
      type = in.read();
      if(type == 0){
        valueList.clear();
        typeList.clear();
        continue;
      }
      len = in.read();
      if((len & 0x80) != 0) len = in.read() + (0x80 * (len & 0x7f));
      InputStreamUtil.readFully(in, buf, 0, len);
      temp = parse.getString(type, buf, len);

      valueList.add(temp);
      while(valueList.size() > size)
        valueList.removeFirst();

      typeList.add(Integer.toHexString(type));
      while(typeList.size() > size)
        typeList.removeFirst();

      if(typeList.size() == size && typeList.get(0).equals(search)){
        System.out.print(parse.getKeyWord());
        for(int i = 0; i < typeList.size(); i++){
          System.out.print("\t");
          System.out.print("\t");
//          System.out.print(typeList.get(i));
//          System.out.print("[");
          System.out.print(valueList.get(i));
//          System.out.print("]");
        }
        System.out.print("\n");
      }
    }
    in.close();
  }

}
