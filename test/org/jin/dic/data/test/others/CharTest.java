/*****************************************************************************
 * 
 * @(#)CharTest.java  2009/03
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
package org.jin.dic.data.test.others;

public class CharTest {

  /**
   * @param args
   */
  public static void main(String[] args){
    String word = "ÖÐÎÄ";
    byte b = (byte) 0x80;
    byte b1 = (byte) 0x80;
    int a = b << 8 | b1;

    System.out.println(Integer.toHexString(a));
    System.out.println(word.length());

    word = "$aoeu";
    System.out.println(word.substring(2));

    word = "field,offset";
    word = "field";
    String[] temp = word.split(",");

    System.out.println(temp[0]);
    System.out.println(temp[1]);
  }

}
