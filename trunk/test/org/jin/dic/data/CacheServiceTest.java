package org.jin.dic.data;

import java.io.IOException;

public class CacheServiceTest {
  public static void main(String[] args) throws IOException{
    CacheService cs = new CacheService();
    cs.putCache(11, "1");
    cs.putCache(22, "2");
    cs.putCache(33, "3");
    cs.putCache(44, "4");
    cs.putCache(55, "5");

    cs.debug();

    cs.getCache(11);
    cs.getCache(11);
    cs.getCache(22);
    cs.getCache(22);
    cs.getCache(33);
    cs.getCache(33);
    cs.getCache(44);
    cs.getCache(44);
    cs.getCache(55);

    cs.debug();

    cs.putCache(66, "6");
    cs.putCache(77, "7");
    cs.debug();
  }
}
