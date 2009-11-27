/*****************************************************************************
 * 
 * @(#)Cast.java  2009/03
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
package org.jin.dic.data.test.java;

public class Cast {

  /**
   * @param args
   */
  public static void main(String[] args){
    byte b1 = (byte) 0x80;
    byte b2 = (byte) 0x80;
    int i1 = b1;
    int i2 = b1 << 8| b2;
   
    byte[] bytes = new byte[]{b1,b2};
    Object objs = new Object[]{bytes,bytes};
    
    System.out.println(Integer.toHexString(i1));
    System.out.println(Integer.toHexString(i2));
  }

}
