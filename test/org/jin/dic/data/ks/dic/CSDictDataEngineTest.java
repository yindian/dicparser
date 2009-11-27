/*****************************************************************************
 * 
 * @(#)CSDictDataEngineTest.java  2009/03
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
package org.jin.dic.data.ks.dic;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.dic.data.ks.TestFolderConfig;
import org.jin.dic.data.ks.dic.CSDictDataEngine;
import org.jin.util.StringUtil;

import junit.framework.TestCase;


public class CSDictDataEngineTest extends TestCase {
  public void testRead() throws IOException, BadFormatException, DataFormatException{
    String dictName = "1#520";
    String fileName = TestFolderConfig.d_myFld + dictName + "/" + dictName + ".DIC";

    CSDictDataEngine document = new CSDictDataEngine(fileName);
    // 亢奋了快两周， 金山词霸字典数据总算基本弄清楚了
    String word = "tim";
    byte[] data;

    data = document.getContent(word);
    String s = StringUtil.valueOf(data);
    System.out.println(s);
    // document.convert(TestFolderConfig.d_myFld);

  }
}
