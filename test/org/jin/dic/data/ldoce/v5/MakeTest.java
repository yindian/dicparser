package org.jin.dic.data.ldoce.v5;

import org.jin.dic.data.pub.ldoce.v5.Make;
import org.jin.util.Logger;

public class MakeTest {

  public static void main(String[] args) throws Exception{
    args = new String[] { "D:/Program Files/Longman/LDOCE5", "D:/Jin/Alpha/ldoce5", "1#00001" };
    long beginTime = System.currentTimeMillis();
    Make.main(args);
    Logger.info("");
    Logger.info_("Time used: ");
    Logger.info_("" + (System.currentTimeMillis() - beginTime) / (float) 1000);
    Logger.info("s");
  }

}
