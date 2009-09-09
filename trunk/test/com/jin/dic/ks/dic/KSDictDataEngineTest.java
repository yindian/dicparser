/*****************************************************************************
 * 
 * @(#)KSDictDataEngineTest.java  2009/03
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
package com.jin.dic.ks.dic;

import java.io.IOException;
import java.util.zip.DataFormatException;

import junit.framework.TestCase;

import com.jin.dic.ks.BadFormatException;
import com.jin.dic.ks.TestFolderConfig;
import com.jin.dic.ks.dic.KSDictDataEngine;
import com.jin.util.StringUtil;

public class KSDictDataEngineTest extends TestCase {

  public void testRead() throws IOException, BadFormatException, DataFormatException{
    String dictName = "1#520";
    String fileName = TestFolderConfig.d_myFld + dictName + "/" + dictName + ".DIC";

    KSDictDataEngine document = new KSDictDataEngine(fileName);

    String word = "3-purlin beam";
    byte[] data;

    data = document.getData(word);
    String s = StringUtil.valueOf(data);
    System.out.println(s);
    // document.convert(TestFolderConfig.d_myFld);
  }

}
