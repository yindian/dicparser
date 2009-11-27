package org.jin.dic.data.sk;

import java.io.FileOutputStream;

public class FSTest {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    String n = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/config.cff";

    FileSystem fs = new FileSystem();
    fs.setConfigFileName(n);
    fs.bind();
    long b = System.currentTimeMillis();
    FSFile f;
//    f = fs.getFile("00000/0000047/000004773.xml");
//    System.out.println(f.getId());
    f = fs.getFile(5251);
    // p(fs, fs.getRootDir());

    System.out.println(fs.getFilePath(f.getId()));
    System.out.println(new String(f.getContent()));

    FileOutputStream fos = new FileOutputStream(f.getName());
    fos.write(f.getContent());
    fos.close();

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
      if(i > 80) break;
      cf = fs.getFile(files[i], false);
      System.out.print(s + cf.getName());
      System.out.print("  ");
      System.out.print(cf.getTitle());
      System.out.print("  ");
      System.out.println();
      // System.out.println(cf.getContent().length);
    }
    System.out.println();
    s.delete(pos, s.length());
  }
}
