package org.jin.dic.data.ldoce.v5;

import org.jin.dic.data.sk.Table;

public class LookupPublisherTime {

  /**
   * @param args
   */
  public static void main(String[] args){
    String simTokenCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/mapping.skn/lc_publisher_id.skn/config.cft";

    Table mappingTable;
    mappingTable = new Table();
    mappingTable.setConfigFileName(simTokenCfgName);
    mappingTable.bind();
    int offset;
    long b = System.nanoTime();// ldoce5.data
    offset = mappingTable.lookUpText("u2fc098491a42200a.262cc60a.1180415e23b.2a9e", null);
    offset = mappingTable.lookUpText("u2fc098491a42200a.262cc60a.1180415e23b.2aac", null);
    offset = mappingTable.lookUpText("u2fc098491a42200a.6e2b450a.11503730847.6fc", null);
    long a = System.nanoTime();

    System.out.println((a - b) / 1000000.0 + "ms");
    System.out.println(offset);
    mappingTable.unBind(false);
  }

}
