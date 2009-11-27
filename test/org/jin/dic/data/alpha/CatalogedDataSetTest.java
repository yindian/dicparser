package org.jin.dic.data.alpha;

import org.jin.dic.data.alpha.make.CatalogedDataSetMaker;

public class CatalogedDataSetTest {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    String[] fileNames = new String[] { "000004773.xml", "000000908.xml", "000000097.xml" };

    CatalogedDataSetMaker.make(fileNames, "dataset");
  }

}
