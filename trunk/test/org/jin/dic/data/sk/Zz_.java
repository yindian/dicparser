/*****************************************************************************
 * 
 * @(#)Zz_.java  2009/03
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

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IField;
import org.jin.dic.data.sk.i.IFieldCollection;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.Logger;

public class Zz_ {

  /**
   * @param args
   */
  public static void main(String[] args){
    // TODO Auto-generated method stub
    String[] names = new String[] {
        "D:/Jin/Data/KingSoft/Dict/Data/sk/skfind-2.0.1/src/backend/native/test/singletda/singletda2.cft",
        "D:/Jin/Data/KingSoft/Dict/Data/sk/skfind-2.0.1/src/backend/native/test/lin/lin.cft",
        "D:/Jin/Data/KingSoft/Dict/Data/sk/skfind-2.0.1/src/backend/native/test/simple/simple.cft",
        "D:/Program Files/Longman/ldoce4v2/data/package/entry/files.skn/config.cft",
        "D:/Program Files/Longman/ldoce4v2/data/index/doc.skn/r_avatar.skn/config.cft",
        "D:/Program Files/Longman/ldoce4v2/data/index/phrlist.skn/config.cft",
        "D:/Program Files/Longman/ldoce4v2/data/index/doc.skn/config.cft" };
   
    Logger.printStack = false;

    long b = System.currentTimeMillis();
    Table table;
    for(int i = 0; i < names.length; i++){
      table = new Table();
      table.setConfigFileName(names[i]);
      table.bind();
      pt(table);
      table.unBind(true);
    }
    System.out.println(System.currentTimeMillis() - b);
  }
  public static void pt(IRecordSet table){
    IRecord record;
    IFieldCollection fldColection;
    IField field;

    fldColection = table.getFldCollection();
    int fldCount = fldColection.getFieldCount();
    int recordCount = table.getRecordCount();

    System.out.println();
    System.out.print("C:" + recordCount);
    System.out.println("\tS:" + fldColection.getSize());
    for(int j = -1; j < fldCount; j++){
      System.out.print("--------");
    }
    System.out.println();
    System.out.print("#\t");

    for(int j = 0; j < fldCount; j++){
      field = fldColection.getField(j);
      System.out.print(field.getName());
      System.out.print("\t");
    }
    System.out.println();

    for(int i = 0; i < recordCount; i++){
      record = table.getRecord(i);
      if(i > 100) break;
      System.out.print(i + 1 + "\t");
      for(int j = 0; j < fldCount; j++){
        field = fldColection.getField(j);
        if(field.isNum()){
          System.out.print(record.getNumFieldValue(j));
        }else if(field.isData()){
          System.out.print(new String(record.getDataFieldValue(j)));
        }else if(field.isLink()){
          System.out.print(record.getLinkFieldValue(j).getRecordCount());
          pt(record.getLinkFieldValue(j));
        }
        System.out.print("\t");
      }
      System.out.println();
    }
  }

}
