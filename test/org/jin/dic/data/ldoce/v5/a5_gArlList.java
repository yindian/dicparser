package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class a5_gArlList {

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

    String lookup = "shove";

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

  static Pattern bs  = Pattern.compile("<BASE class=\"(\\w*)\">");
  static Pattern hom  = Pattern.compile("<DATA class=\"(?:keyword)?+hom\">(\\d*)</DATA>");
  public static String get_aId(String dis){
    return "A";
  }
  
  
  static Map p2eId = new HashMap();
  public static void a(String[] args) throws Exception{
    getMap();
    String offset;
    long b = System.nanoTime();
    offset = (String) p2eId.get("u2fc098491a42200a.262cc60a.1180415e23b.2a9e");
    offset = (String) p2eId.get("u2fc098491a42200a.262cc60a.1180415e23b.2aac");
    offset = (String) p2eId.get("u2fc098491a42200a.6e2b450a.11503730847.6fc");
    long a = System.nanoTime();

    System.out.println(" " + offset);
    System.out.println((a - b) / 1000000.0 + "ms");
  }
  public static void getMap() throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream("alpha/publisher_eId_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 2){
          continue;
        }
        p2eId.put(info[0], info[1]);
      }
    }finally{
      if(br != null) br.close();
    }
  }
}
