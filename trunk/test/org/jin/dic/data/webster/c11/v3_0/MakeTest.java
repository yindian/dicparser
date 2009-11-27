package org.jin.dic.data.webster.c11.v3_0;

import org.jin.dic.data.pub.webster.c11.v3_0.Make;
import org.jin.util.Logger;

public class MakeTest {

  public static void main(String[] args) throws Exception{
    long beginTime;

    args = new String[] { "C:/Program Files/Merriam-Webster", "D:/Jin/Alpha/mwc11", "1#00002" };
    beginTime = System.currentTimeMillis();
    Make.main(args);
    Logger.info("");
    Logger.info_("Time used: ");
    Logger.info_("" + (System.currentTimeMillis() - beginTime) / (float) 1000);
    Logger.info("s");

    Thread.sleep(500000);
    
    args = new String[] { "D:/Program Files/Longman/LDOCE5", "D:/Jin/Alpha/ldoce5", "1#00001" };
    beginTime = System.currentTimeMillis();
    org.jin.dic.data.pub.ldoce.v5.Make.main(args);
    Logger.info("");
    Logger.info_("Time used: ");
    Logger.info_("" + (System.currentTimeMillis() - beginTime) / (float) 1000);
    Logger.info("s");
  }

}
