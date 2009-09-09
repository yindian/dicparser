package com.jin.dic.ldoce;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.jin.dic.sk.Table;
import com.jin.dic.sk.i.IRecord;
import com.jin.dic.sk.i.IRecordSet;

public class Doc {

  /**
   * @param args
   */
  public static void main(String[] args){
    Doc doc = new Doc();
    doc.run();
  }
  public void run(){
    long b = System.currentTimeMillis();

    String path = "D:/Program Files/Longman/ldoce4v2/data/index/doc.skn/config.cft";
    Table table;
    table = new Table();
    table.setConfigFileName(path);
    table.bind();
    IRecord record, sRecord;
    IRecordSet st;
    int packageid, ftype, ptype, fileid;
    Map packageidMap = new HashMap();
    PackageInfo info;
    for(int i = 0; i < table.getRecordCount(); i++){
      record = table.getRecord(i);
      st = record.getLinkFieldValue("r_avatar");
      for(int j = 0; j < st.getRecordCount(); j++){
        sRecord = st.getRecord(j);
        ftype = (int) sRecord.getNumFieldValue("ftype");
        ptype = (int) sRecord.getNumFieldValue("ptype");
        packageid = (int) sRecord.getNumFieldValue("packageid");
        fileid = (int) sRecord.getNumFieldValue("fileid");
        if(packageid == 0 && fileid == 16640) System.out.println(new String(record.getDataFieldValue("doc")));
        info = (PackageInfo) packageidMap.get(new Integer(packageid));
        if(info == null){
          info = new PackageInfo();
          info.firstId = i;
          info.count = 1;
          info.addFtype(ftype);
          info.addPtype(ptype);
          packageidMap.put(new Integer(packageid), info);
        }else{
          info.count++;
          info.addFtype(ftype);
          info.addPtype(ptype);
        }
      }
    }
    Iterator i = packageidMap.entrySet().iterator();
    while(i.hasNext()){
      Entry e = (Entry) i.next();
      System.out.print(e.getKey());
      System.out.println(e.getValue());
    }
    table.unBind(false);
    System.out.println(System.currentTimeMillis() - b);
  }
  class PackageInfo {
    int firstId  = 0;
    int count    = 0;
    Set ptypeSet = null;
    Set ftypeSet = null;
    void addPtype(int ptype){
      if(ptypeSet == null) ptypeSet = new HashSet();
      ptypeSet.add(new Integer(ptype));
    }
    void addFtype(int ftype){
      if(ftypeSet == null) ftypeSet = new HashSet();
      ftypeSet.add(new Integer(ftype));
    }
    public String toString(){
      return ptypeSet.toString() + " " + ftypeSet.toString() + " " + firstId + " " + count;
    }
  }
}
