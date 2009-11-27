package org.jin.dic.data.ldoce.v5;

import org.jin.dic.data.sk.Table;

public class LookupTime {

  /**
   * @param args
   */
  public static void main(String[] args){
    String simTokenCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/alpha_index.skn/lc_SRCH.skn/config.cft";

    Table simTokTable;
    simTokTable = new Table();
    simTokTable.setConfigFileName(simTokenCfgName);
    simTokTable.bind();

    int offset;
    long b = System.nanoTime();// ldoce5.data
    offset = simTokTable.lookUpText("source", null);
    offset = simTokTable.lookUpText("sun", null);
    offset = simTokTable.lookUpText("sometime", null);
    offset = simTokTable.lookUpText("something", null);
    offset = simTokTable.lookUpText("sound", null);
    long a = System.nanoTime();

    System.out.println(offset);
    System.out.println((a - b) / 1000000.0 + "ms");

    simTokTable.unBind(false);
  }

}
