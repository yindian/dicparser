package org.jin.dic.data.ldoce.v4;

import java.io.UnsupportedEncodingException;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;


public class Hwd {

  /**
   * @param args
   * @throws UnsupportedEncodingException 
   */
  public static void main(String[] args) throws UnsupportedEncodingException{
    Hwd doc = new Hwd();
    doc.run();
  }
  public void run() throws UnsupportedEncodingException{
    long b = System.currentTimeMillis();
    String docPath = "D:/Program Files/Longman/ldoce4v2/data/index/doc.skn/config.cft";
    String hwdPath = "D:/Program Files/Longman/ldoce4v2/data/index/hwd.skn/config.cft";
    Table doc, hwd;
    doc = new Table();
    hwd = new Table();
    doc.setConfigFileName(docPath);
    hwd.setConfigFileName(hwdPath);
    doc.bind();
    hwd.bind();
    IRecord hwdRecord, stRecord, docRecord;
    IRecordSet st;
    System.out.println(hwd.getRecordCount());
    int tid;
    for(int i = 0; i < hwd.getRecordCount(); i++){
      hwdRecord = hwd.getRecord(i);
      System.out.print(hwdRecord.getNumFieldValue("lookup"));
      System.out.print("\t");
      st = hwdRecord.getLinkFieldValue("r_target");
      for(int j = 0; j < st.getRecordCount(); j++){
        stRecord = st.getRecord(j);
        tid = (int) stRecord.getNumFieldValue("targetid");
        docRecord = doc.getRecord(tid);

       if(j !=0) System.out.print(" \t");
       System.out.print(tid+" \t");
        System.out.println(getString(docRecord.getDataFieldValue("doc")));
      }
    }
    doc.unBind(false);
    hwd.unBind(false);
    System.out.println(System.currentTimeMillis() - b);
  }
  public static String getString(byte[] data) throws UnsupportedEncodingException{
    byte[] b = new byte[data.length < 1 ? data.length : data.length - 1];
    System.arraycopy(data, 0, b, 0, b.length);
    return new String(b,"utf8");
  }
  
}
