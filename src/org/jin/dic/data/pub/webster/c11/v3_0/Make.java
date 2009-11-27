/*****************************************************************************
 * 
 * @(#)Make.java  2009/11
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
package org.jin.dic.data.pub.webster.c11.v3_0;

import java.io.IOException;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.dic.data.pub.CleanUp;
import org.jin.dic.data.pub.Main;

public class Make {

  public static void main(String[] args) throws IOException, BadFormatException{
    String dictName = args[2];
    String fileName = args[0] + "/books/C11/C11.cmp";
    String inflatedFileName = args[1] + "/C11.inflate.cmp";
    String dat9650450 = args[1] + "/9650450.inflate.dat";
    String tempFld = args[1] + "/temp/";
    String indexName = args[1] + "/" + dictName + ".DIC.txt";
    // String infoName = args[1] + "/" + dictName + ".DIC.inf";
    String dataName = args[1] + "/" + dictName + ".DIC.zip";
    String dataSuf = ".html";

    CleanUp.main(new String[] { tempFld, indexName, dataName, inflatedFileName, dictName + ".DIC" });
    
    GetData.main(new String[] { fileName, inflatedFileName, dat9650450 });
    OrderUp.main(new String[] { inflatedFileName });
    Generate.main(new String[] { inflatedFileName, indexName, dataSuf, tempFld });
    Pack.main(new String[] { tempFld, dataName });
    Main.main(new String[] { "-g", args[1], args[1] + "/" + dictName + ".DIC" });
    
    CleanUp.main(new String[] { tempFld, tempFld });
  }

}
