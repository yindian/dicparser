package com.jin.test.others;

import java.util.ArrayList;
import java.util.List;

public class STringtest {

  /**
   * @param args
   */
  public static void main(String[] args){
    List columnList = new ArrayList();
    columnList.add("a");
    columnList.add("b");
    StringBuffer colNames = new StringBuffer();
    for(int i = 0; i < columnList.size(); i++){
      if(i != 0) colNames.append(",");
      colNames.append(columnList.get(i));
    }
    System.out.println(colNames.toString());

  }

}
