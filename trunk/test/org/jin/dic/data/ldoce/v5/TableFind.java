package org.jin.dic.data.ldoce.v5;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.Logger;

public class TableFind {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    String tableCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/arl.skn/idx_index.skn/wordlist/simtok.skn/config.cft";

    Table table;
    table = new Table();
    table.setConfigFileName(tableCfgName);
    table.bind();
    if(!table.isValid()){
      Logger.info("Error: " + table.getCfgFileName());
    }else{
      IRecord record = table.getRecord(table.lookUpText("book", null));
      IRecordSet rs = record.getLinkFieldValue("TOKENS");
      System.out.println(record.getId());
      System.out.print(rs.getRecordCount());
      for(int i = 0; i < rs.getRecordCount(); i++){
        System.out.println(rs.getRecord(i).getNumFieldValue("ID_TOKEN"));
      }

    }
    table.unBind(false);
  }

}
