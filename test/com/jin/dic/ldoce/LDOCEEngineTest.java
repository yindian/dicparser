package com.jin.dic.ldoce;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

import com.jin.dic.ldoce.v4.LDOCEEngine;
import com.jin.dic.sk.FSFile;
import com.jin.util.Logger;

public class LDOCEEngineTest extends TestCase {

  public void atestDump() throws IOException{
    String[] words = { "a", "fusion", "G-spot", "abuse", "guardsman", "Z¨¹rich", "clean"};
    for(int i = 0; i < words.length; i++){
      atestGetFiles(words[i]);
    }
  }
  public void atestGetFiles(String word) throws IOException{
    String src = "D:/Program Files/Longman/ldoce4v2/data/";
    String des = "D:/Jin/Work/WorkSpace/Dictionary/dump";
    LDOCEEngine engine = new LDOCEEngine();
    engine.setSrcFolder(src);
    FSFile[] files = engine.getFile(word);
    File file, fld;
    OutputStream os;
    for(int i = 0; i < files.length; i++){
      fld = new File(des, word);
      if(!fld.exists()) fld.mkdir();
      file = new File(fld, files[i].getFileSystem().getCfgFileName() + "_" + files[i].getName());
      if(!file.exists()) file.createNewFile();
      os = new FileOutputStream(file);
      os.write(files[i].getContent());
      os.close();
    }

  }

  public void testGetData() throws IOException{
    Logger.printStack = true;
    LDOCEEngine engine = new LDOCEEngine();
    engine.setSrcFolder("D:/Program Files/Longman/ldoce4v2/data/");
    long b = System.currentTimeMillis();
    engine.getData("a");// 6357024
    engine.getData("fusion");// 6750253
    engine.getData("G-spot");// 6750253
    engine.getData("abuse");// 6357024
    engine.getData("guardsman");// 6750325
    engine.getData("zucchini");// 7995509
    engine.getData("Z¨¹rich");// 7995644
    engine.getData("xajin");// 7995644
    engine.getData("a");// 6357024
    engine.getData("fusion");// 6750253
    engine.getData("G-spot");// 6750253
    engine.getData("abuse");// 6357024
    engine.getData("guardsman");// 6750325
    engine.getData("zucchini");// 7995509
    engine.getData("Z¨¹rich");// 7995644
    engine.getData("xajin");// 7995644
    engine.getData("a");// 6357024
    engine.getData("fusion");// 6750253
    engine.getData("G-spot");// 6750253
    engine.getData("abuse");// 6357024
    engine.getData("guardsman");// 6750325
    engine.getData("zucchini");// 7995509
    engine.getData("Z¨¹rich");// 7995644
    engine.getData("xajin");// 7995644
    engine.getData("a");// 6357024
    engine.getData("fusion");// 6750253
    engine.getData("G-spot");// 6750253
    engine.getData("abuse");// 6357024
    engine.getData("guardsman");// 6750325
    engine.getData("zucchini");// 7995509
    engine.getData("Z¨¹rich");// 7995644
    engine.getData("xajin");// 7995644
    engine.getData("a");// 6357024
    engine.getData("fusion");// 6750253
    engine.getData("G-spot");// 6750253
    engine.getData("abuse");// 6357024
    engine.getData("guardsman");// 6750325
    engine.getData("zucchini");// 7995509
    engine.getData("Z¨¹rich");// 7995644
    engine.getData("xajin");// 7995644
    engine.getData("a");// 6357024
    engine.getData("fusion");// 6750253
    engine.getData("G-spot");// 6750253
    engine.getData("abuse");// 6357024
    engine.getData("guardsman");// 6750325
    engine.getData("zucchini");// 7995509
    engine.getData("Z¨¹rich");// 7995644
    engine.getData("xajin");// 7995644
    System.out.println(System.currentTimeMillis() - b);
  }

}
