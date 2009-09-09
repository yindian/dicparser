/*****************************************************************************
 * 
 * @(#)ZTable2Xls.java  2009/03
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.biff.RowsExceededException;

import com.jin.dic.sk.Table;
import com.jin.dic.sk.i.IField;
import com.jin.dic.sk.i.IFieldCollection;
import com.jin.dic.sk.i.IRecord;
import com.jin.dic.sk.i.IRecordSet;
import com.jin.util.Logger;

public class Table2Xls {

  /**
   * @param args
   * @throws Exception
   * @throws RowsExceededException
   */
  public static void main(String[] args) throws RowsExceededException, Exception{
    Logger.printStack = true;
    String[] names = getNames("C:/Program Files/Longman/ldoce4v2/data/index");

    String outFileName = "index.xls";
    File outFile = new File(outFileName);
    WritableWorkbook wbook = Workbook.createWorkbook(outFile);
    Table table;
    for(int i = 0; i < names.length; i++){
//if(!names[i].contains("doc.skn"))continue;
      table = new Table();
      table.setConfigFileName(names[i]);
      table.bind();
      File fld = new File(names[i]);
      if(!table.isValid()) continue;
      WritableSheet wsheet = wbook.createSheet(fld.getParentFile().getName(), i);
      printToSheet(table, wsheet, 0, 0, true);

      table.unBind(false);
    }
    wbook.write();
    wbook.close();
  }
  public static int printFldCollection(IRecordSet table,IFieldCollection fldCollection, WritableSheet sheet,
      int col, int row) throws JXLException{
    int rowsWrote = 0;
    IField field;
    print("#", sheet, col++, row);
    for(int i = 0; i < fldCollection.getFieldCount(); i++){
      field = fldCollection.getField(i);
      print(field.getName(), sheet, col, row);
      int subRowsWrote = 0;
      if(field.isLink()){
        IRecordSet st = field.getLinkRecordSet();
        subRowsWrote = printFldCollection(st,st.getFldCollection(), sheet, col,
            row + 1);
        sheet.mergeCells(col, row, (col += getFieldCount(field)) - 1, row);
        if(subRowsWrote > rowsWrote) rowsWrote = subRowsWrote;
      }else col++;
    }
    print(table.getRecordCount(), sheet, col, row);
    return rowsWrote + 1;
  }
  public static int printToSheet(IRecordSet table, WritableSheet sheet, int col, int row,
      boolean withFld) throws JXLException{
    int rowsWrote = 0;
    int oldCol = col;

    IRecord record;
    IFieldCollection fldCollection;
    IField field;
    fldCollection = table.getFldCollection();
    if(withFld) row += rowsWrote += printFldCollection(table,fldCollection, sheet, col, row);

    // if(fldCollection != null) System.out.println(row);
    boolean withNormal = false;
    for(int i = 0; i < table.getRecordCount(); i++){
      if(i > 100) break;
      record = table.getRecord(i);
      col = oldCol;
      print(i, sheet, col++, row);
      int maxRowsWrote = 0;
      for(int j = 0; j < fldCollection.getFieldCount(); j++){
        int subRowsWrote = 0;
        field = fldCollection.getField(j);
        if(field.isLink()){
          IRecordSet st = record.getLinkFieldValue(j);
          subRowsWrote += printToSheet(st, sheet, col, row, false);
          col += getFieldCount(field);
          if(subRowsWrote > maxRowsWrote) maxRowsWrote = subRowsWrote;
        }else{
          if(field.isNum()) print(record.getNumFieldValue(j), sheet, col++, row);
          else if(field.isData()) print(new String(record.getDataFieldValue(j)), sheet, col++, row);
          withNormal = true;
        }
      }
      row += maxRowsWrote + (withNormal ? 1 : 0);
      rowsWrote += maxRowsWrote + (withNormal ? 1 : 0);
    }

    return rowsWrote - (withNormal ? 1 : 0);
  }
  private static int getFieldCount(IField field){
    int count = 1;
    if(field.isLink()){
      IRecordSet st = field.getLinkRecordSet();
      IFieldCollection fc = st.getFldCollection();
      for(int i = 0; i < fc.getFieldCount(); i++){
        count += getFieldCount(fc.getField(i));
      }
      // count--;
    }
    return count;
  }

  private static void print(String data, WritableSheet sheet, int col, int row) throws JXLException{
    sheet.addCell(new Label(col, row, data));
  }
  private static void print(long data, WritableSheet sheet, int col, int row) throws JXLException{
    sheet.addCell(new Number(col, row, data));
  }
  private static String[] getNames(String fldPath){
    File fld = new File(fldPath);
    List flds = new ArrayList();
    File[] files = fld.listFiles();
    for(int i = 0; i < files.length; i++){
      if(files[i].isDirectory()) flds.add(files[i].getAbsolutePath() + "/config.cft");
    }
    return (String[]) flds.toArray(new String[flds.size()]);
  }
}
