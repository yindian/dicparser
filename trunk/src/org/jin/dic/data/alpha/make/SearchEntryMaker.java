/*****************************************************************************
 * 
 * @(#)SearchEntryMaker.java  2009/11
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
package org.jin.dic.data.alpha.make;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jin.util.io._RandomAccessFile;

public class SearchEntryMaker {

  public static void make(List alphaList, List dataOfstList, String desFileName) throws IOException{
    new SearchEntryMaker().domake(alphaList, dataOfstList, desFileName);
  }
  private void domake(List alphaList, List dataOfstList, String desFileName) throws IOException{
    List itemsList = new ArrayList();
    SearchEntryItem root;
    root = new SearchEntryItem();
    root.setChar((char) 0);
    itemsList.add(root);

    for(int i = 0; i < alphaList.size(); i++){
      root.addBranchPath((String) alphaList.get(i), Integer.valueOf((String) dataOfstList.get(i)).intValue());
    }
    root.prepare();
    root.traverse(root, SearchEntryItem.GET_LIST, itemsList);
    
    File file = new File(desFileName);
    if(file.exists()) file.delete();
    _RandomAccessFile f = new _RandomAccessFile(desFileName, "rw", true);

    SearchEntryItem item0;
    int size = itemsList.size();
    for(int i = 0; i < size; i++){
      item0 = (SearchEntryItem) itemsList.get(i);
      f.writeChar(item0.getChar());
      f.writeChar(item0.getChildrenSize());
      f.writeInt(item0.getValue());
    }
    f.close();
  }
}
