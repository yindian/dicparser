/*****************************************************************************
 * 
 * @(#)AddHexValueToFile.java  2009/03
 *
 *  Copyright (C) 2009  Tim Bron<jinxingquan@gmail.com>
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
package org.jin.dic.data.test.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddHexValueToFile {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    addHex("D:\\Jin\\Data\\KingSoft\\Dict\\Data\\PWPECJS\\time.txt");
  }
  private static Pattern pattern = Pattern.compile("\\d+");

  private static void addHex(String fileName) throws IOException{
    FileInputStream fis = new FileInputStream(fileName);
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String line;
    while((line = br.readLine()) != null){
      Matcher matcher = pattern.matcher(line);
      String[] strings = line.split("\\d+");
      List numberList = new ArrayList();
      List stringList = new ArrayList();

      while(matcher.find()){
        numberList.add(matcher.group());
      }

      if(numberList.size() == 0) stringList.add(strings[0]);
      for(int i = 0; i < numberList.size(); i++){
        String s = (String) numberList.get(i);
        stringList.add(strings[i]);
        stringList.add(s + "(" + Integer.toHexString(Integer.valueOf(s).intValue()).toUpperCase()
            + ")");
      }
      for(int i = 0; i < stringList.size(); i++){
        System.out.print((String) stringList.get(i));
      }
      System.out.println();
    }
    br.close();
  }

}
