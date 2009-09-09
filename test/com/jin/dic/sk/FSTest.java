package com.jin.dic.sk;

import com.jin.dic.sk.FSFile;
import com.jin.dic.sk.FileSystem;

public class FSTest {

  /**
   * @param args
   */
  public static void main(String[] args){
    String n = "D:/Program Files/Longman/ldoce4v2/data/package/entry/filesystem.cff";
    FileSystem fs = new FileSystem();
    fs.setConfigFileName(n);
    fs.bind();
    long b = System.currentTimeMillis();
    // FSFile f = fs.getFSFile("00002079.html");
    // System.out.println(f.getId());
    System.out.println(new String(fs.getFile(48067).getContent()));
//    p(fs, fs.getRootDir());
    System.out.println(System.currentTimeMillis() - b);
    fs.unBind(false);
  }
  static StringBuffer s = new StringBuffer();
  public static void p(FileSystem fs, FSFile f){
    System.out.print(s + f.getName());
    if(f.isFile()) return;
    int[] dirs = f.getDirs();
    int[] files = f.getFiles();
    System.out.println(s.toString() + dirs.length + " " + files.length);
    FSFile cf;
    int pos;
    pos = s.length();
    s.append("   ");
    for(int i = 0; i < dirs.length; i++){
      cf = fs.getDir(dirs[i]);
      p(fs, cf);
    }
    for(int i = 0; i < files.length; i++){
      if(i > 50) break;
      cf = fs.getFile(files[i],false);
      System.out.print(s + cf.getName());
      System.out.print("  ");
      System.out.print(cf.getTitle());
      System.out.print("  ");
      System.out.println();
//      System.out.println(cf.getContent().length);
    }
    System.out.println();
    s.delete(pos, s.length());
  }
}
