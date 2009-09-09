/*****************************************************************************
 * 
 * @(#)Permutation.java  2009/03
 *
 *  Copyright (C) 2009  Tim Bron<jinxingquan@google.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *****************************************************************************/
package com.jin.test.java;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
  public static void main(String[] args){
    List testData = new ArrayList();
    testData.add("a");
    testData.add("b");
    testData.add("c");
    perumtate(testData);
  }

  public static void perumtate(List src){
    perumtate(src, new ArrayList(src.size()));
  }

  private static void perumtate(List src, List des){
    int currentPos = des.size();
    for(int i = 0; i < src.size(); i++){
      Object element = src.get(i);
      if(element == null) continue;
      src.set(i, null);
      des.add(element);
      perumtate(src, des);
      des.remove(currentPos);
      src.set(i, element);
    }
    if(des.size() == src.size()) printList(des);
  }
  
  private static void printList(List list){
    for(int i = 0; i < list.size(); i++)
      System.out.print((String) list.get(i));
    System.out.println();
  }

}
