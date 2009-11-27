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
package org.jin.dic.data.pub.ldoce.v5;

import org.jin.dic.data.pub.CleanUp;
import org.jin.dic.data.pub.Main;

public class Make {

  public static void main(String[] args) throws Exception{
    String base = args[0];
    String des = args[1];
    String dictName = args[2];

    String classList = des + "/className_list.txt";
    String data0 = des + "/entry[fs]0";
    String data1 = des + "/entry[fs]1";
    String data2 = des + "/entry[fs]2";
    String data3 = des + "/entry[fs]3";
    String thesaurusP2IdList = args[1] + "/thesaurus_[P-Id].txt";
    String alphabet = des + "/alphabet.txt";
    String entryP2IdList = des + "/entry_[P-Id].txt";
    String refP2Numlist = des + "/[p-num].txt";

    String indexName = des + "/" + dictName + ".DIC.txt";
    // String infoName = des + "/" + dictName + ".DIC.inf";
    String dataName = des + "/" + dictName + ".DIC.zip";

    CleanUp.main(new String[] { data0 });
    CleanUp.main(new String[] { data1 });
    CleanUp.main(new String[] { data2 });
    CleanUp.main(new String[] { data3 });

    GenAlphabetList.main(new String[] { base + "/ldoce5.data/fs.skn/alpha_index.skn", alphabet });
    GenEntryP2IdList.main(new String[] { base + "/ldoce5.data/fs.skn/mapping.skn", entryP2IdList });
    GenThesaurusP2IdList.main(new String[] { base + "/ldoce5.data/thesaurus.skn/mapping.skn", thesaurusP2IdList });
    GetEntryData.main(new String[] { base + "/ldoce5.data/fs.skn/config.cff", data0 });
    TrimEntryData.main(new String[] { data0, data1, entryP2IdList, refP2Numlist });
    ReplaceP2Id.main(new String[] { data1, data2, refP2Numlist, thesaurusP2IdList });
    Convert2Html.main(new String[] { data2, data3, classList });
    Pack.main(new String[] { alphabet, data3, indexName, dataName });
    Main.main(new String[] { "-g", args[1], args[1] + "/" + dictName + ".DIC" });

    CleanUp.main(new String[] { data0, data0 });
    CleanUp.main(new String[] { data1, data1 });
    CleanUp.main(new String[] { data2, data2 });
    CleanUp.main(new String[] { data3, data3 });
  }
}
