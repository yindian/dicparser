package org.jin.dic.data.test.java;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jin.util.io._RandomAccessFile;

public class CharTest {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    // TODO Auto-generated method stub
    String a = "Z¨¹rich";
    int i = a.charAt(1) & 0x0000ffff;
    System.out.println(i);

    byte[] bs = new byte[] { (byte) 0x00, (byte) 0xf1 };
    String b = new String(bs, "unicode");
    i = b.charAt(0) & 0x000000ff;
    System.out.println(i);

    System.out.println(b);

    // _RandomAccessFile f = new _RandomAccessFile("uao.txt","rw");
    // f.seek(1);
    // System.out.println(f.readChar());
    // String b = f.readLine();
    // System.out.println(b);
  }

}
