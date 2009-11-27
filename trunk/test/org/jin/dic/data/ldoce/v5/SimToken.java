package org.jin.dic.data.ldoce.v5;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class SimToken {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{

    String simTokenCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/arl.skn/idx_index.skn/wordlist/simtok.skn/config.cft";
    String tokenCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/arl.skn/idx_index.skn/wordlist/tok.skn/config.cft";
    String arlCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/arl.skn/config.cft";

    Table simTokTable;
    simTokTable = new Table();
    simTokTable.setConfigFileName(simTokenCfgName);
    simTokTable.bind();

    Table tokTable;
    tokTable = new Table();
    tokTable.setConfigFileName(tokenCfgName);
    tokTable.bind();

    Table arlTable;
    arlTable = new Table();
    arlTable.setConfigFileName(arlCfgName);
    arlTable.bind();

    String lookup = "Z¨¹rich";

    int tokenIndex;
    int arlIndex;
    if(simTokTable.isValid() && tokTable.isValid() && arlTable.isValid()){
      System.out.println(simTokTable.lookUpText(lookup, null));
      IRecord simTokRecord = simTokTable.getRecord(simTokTable.lookUpText(lookup, null));
      IRecordSet rs = simTokRecord.getLinkFieldValue("TOKENS");
      IRecord token;
      IRecord arl;
      IRecordSet tokenOcc;
      for(int i = 0; i < rs.getRecordCount(); i++){
        tokenIndex = (int) rs.getRecord(i).getNumFieldValue("ID_TOKEN");
        token = tokTable.getRecord(tokenIndex);
        tokenOcc = token.getLinkFieldValue("OCC");

        System.out.println(StringUtil.valueOf(BytesUtil.trimData(token.getDataFieldValue("TOKEN")), "UTF-8"));
        for(int j = 0; j < tokenOcc.getRecordCount(); j++){
          // pos = tokenOcc.getRecord(j).getLinkFieldValue("POSITION");
          // for(int k = 0; k < pos.getRecordCount(); k++){
          // System.out.print("[");
          // System.out.print(pos.getRecord(k).getNumFieldValue("POS"));
          // System.out.print("\t]");
          // }
          arlIndex = (int) tokenOcc.getRecord(j).getNumFieldValue("a_id");
          arl = arlTable.getRecord(arlIndex);
          // System.out.print(tokenOcc.getRecord(j).getNumFieldValue("STRUCT"));
          // System.out.print("\t");
          // System.out.print(arlIndex);
          // System.out.print("\t");
          System.out.print(StringUtil.valueOf(BytesUtil.trimData(arl.getDataFieldValue("id")), "utf-8"));
          System.out.print("\t");
          System.out.print(StringUtil.valueOf(BytesUtil.trimData(arl.getDataFieldValue("context")), "utf-8"));
          System.out.print("\t");
          System.out.print(arl.getNumFieldValue("class"));
          System.out.print("\t");
          System.out.print(StringUtil.valueOf(BytesUtil.trimData(arl.getDataFieldValue("index")), "utf-8"));
          System.out.print("\t");
          System.out.print("\t");
          System.out.print("\t");
          System.out.print("\t");
          System.out.print("\t");
          System.out.print("\t");
          System.out.print("\t");
          System.out.print("\t");
          System.out.print("\t");
          System.out.print("\t");
          System.out.print(StringUtil.valueOf(BytesUtil.trimData(arl.getDataFieldValue("display")), "utf-8"));
          System.out.println();

        }
        System.out.print(tokenIndex);
        System.out.println("========================================");
      }
    }else{
      Logger.info("Error: " + simTokTable.getCfgFileName());
    }

    arlTable.unBind(false);
    tokTable.unBind(false);
    simTokTable.unBind(false);
  }

}
