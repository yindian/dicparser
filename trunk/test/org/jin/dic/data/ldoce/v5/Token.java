package org.jin.dic.data.ldoce.v5;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class Token {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{

    String tokenCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/arl.skn/idx_index.skn/wordlist/tok.skn/config.cft";
    String arlCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/arl.skn/config.cft";

    Table tokTable;
    tokTable = new Table();
    tokTable.setConfigFileName(tokenCfgName);
    tokTable.bind();

    Table arlTable;
    arlTable = new Table();
    arlTable.setConfigFileName(arlCfgName);
    arlTable.bind();

    String lookup = "tim";

    int arlIndex;
    if(tokTable.isValid() && arlTable.isValid()){
      IRecord token = tokTable.getRecord(tokTable.lookUpText(lookup, null));
      IRecordSet tokenOcc = token.getLinkFieldValue("OCC");
      IRecord arl;
      for(int j = 0; j < tokenOcc.getRecordCount(); j++){
        arlIndex = (int) tokenOcc.getRecord(j).getNumFieldValue("a_id");
        arl = arlTable.getRecord(arlIndex);
        System.out.print(StringUtil.valueOf(BytesUtil.trimData(arl.getDataFieldValue("context")),"utf-8"));
        System.out.print("\t");
        System.out.print(arl.getNumFieldValue("class"));
        System.out.print("\t");
        // System.out.print(new String(arl.getDataFieldValue("id")));
        // System.out.print("\t");
        System.out.print(StringUtil.valueOf(BytesUtil.trimData(arl.getDataFieldValue("index")),"utf-8"));
        System.out.println();
      }
    }else{
    }

    arlTable.unBind(false);
    tokTable.unBind(false);
  }
}
