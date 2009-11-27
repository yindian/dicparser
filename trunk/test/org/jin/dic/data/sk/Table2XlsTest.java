/*****************************************************************************
 * 
 * @(#)ZTable2Xls.java  2009/03
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
package org.jin.dic.data.sk;

import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.util.Table2Xls;
import org.jin.util.Logger;

public class Table2XlsTest extends Table2Xls{

  protected boolean condition(IRecord record){
//   return record.getNumFieldValue("ID")>4773-20 && record.getNumFieldValue("ID") <4773+20;
    return true; 
  }

  public static void main(String[] args) throws Exception{
    Logger.printStack = true;
    Table2XlsTest a = new Table2XlsTest();
    String tableCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/mapping.skn/lc_publisher_id.skn/config.cft";
    a.convert(tableCfgName,0,-1,-1,"");
  }

}
