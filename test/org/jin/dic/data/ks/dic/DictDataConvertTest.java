/*****************************************************************************
 * 
 * @(#)DictDataConvertTest.java  2009/03
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
import org.jin.dic.data.ks.dic.KSDictDataEngine;

import junit.framework.TestCase;

public class DictDataConvertTest extends TestCase {

  public void testConvert() throws BadFormatException, IOException, DataFormatException{
    String dictName = "1#00002";
    String fileName = TestFolderConfig.d_myFld + dictName + "/" + dictName + ".DIC";

    KSDictDataEngine document = new KSDictDataEngine(fileName);
//    document.convert(TestFolderConfig.d_myFld + dictName + "/");
    document.dump(TestFolderConfig.d_myFld + dictName + "/");
    
//    CSDictDataEngine csengine = new CSDictDataEngine(fileName);
//    csengine.convert(TestFolderConfig.d_myFld + dictName + "/");
  }

}
