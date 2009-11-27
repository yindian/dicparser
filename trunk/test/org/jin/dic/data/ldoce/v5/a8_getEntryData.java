package org.jin.dic.data.ldoce.v5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jxl.write.biff.RowsExceededException;

import org.jin.dic.data.sk.FSFile;
import org.jin.dic.data.sk.FileSystem;
import org.jin.util.Logger;

public class a8_getEntryData {

  /**
   * @param args
   */
  public static void main(String[] args) throws RowsExceededException, Exception{
    Logger.printStack = true;
    String[] names = new String[] { "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/config.cff" };
    File desFld = new File("D:/Jin/Alpha/ldoce5.data/entry[fs]");
    for(int i = 0; i < names.length; i++){
      File fil = new File(names[i]);
      FileSystem fs = new FileSystem();
      fs.setConfigFileName(fil.getAbsolutePath());
      fs.bind();
      if(!fs.isValid()) continue;
      toLocal(desFld, fs.getRootDir());
      fs.unBind(false);
    }
  }
  private static void toLocal(File fld, FSFile f) throws IOException{
    if(!fld.exists()){
      if(f.isDirectory()) fld.mkdir();
      else fld.createNewFile();
    }
    if(f.isFile()){
      File file = new File(fld, f.getName());
      if(!file.exists()) file.createNewFile();
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(f.getContent());
      fos.close();
    }else{
      FileSystem fs = f.getFileSystem();
      int[] dirs = f.getDirs();
      int[] files = f.getFiles();
      for(int i = 0; i < dirs.length; i++){
        FSFile fd = fs.getDir(dirs[i]);
        toLocal(new File(fld, fd.getName()), fd);
      }
      for(int i = 0; i < files.length; i++){
        toLocal(fld, fs.getFile(files[i]));
      }
    }
  }

}
