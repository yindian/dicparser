/*****************************************************************************
 * 
 * @(#)EngineTest.java  2009/03
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
package com.jin.dic.sk;

import com.jin.dic.sk.Table;
import com.jin.dic.sk.i.IRecord;

public class EngineTest {

  /**
   * @param args
   */
  public static void main(String[] args){
    String index = "C:/Program Files/Longman/ldoce4v2/data/index/doc.skn/config.cft";
    Table table = new Table();
    table.setConfigFileName(index);
    table.bind();
    IRecord record = table.getRecord(48937);
    System.out.println(new String(record.getDataFieldValue("doc")));
    table.unBind(false);
  }

}
