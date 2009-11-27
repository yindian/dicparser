/*****************************************************************************
 * 
 * @(#)Table2Xls.java  2009/10
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
package org.jin.dic.data.sk.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IField;
import org.jin.dic.data.sk.i.IFieldCollection;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class Table2Xls {

  protected int from;
  protected int to;
  protected int maxCount;

  List          nodeL           = new ArrayList();
  List          nodeR           = new ArrayList();
  int           typeL;
  int           typeR;
  boolean       equalsCondition = false;
//  protected IRecord foobar(IRecord record){
//    String node;
//    IRecordSet rs;
//    for(int i = 0; i < nodeL.size(); i++){
//      node = (String) nodeL.get(i);
//      if(i == nodeL.size() - 1){
//        return rs.getRecord(1);
//      }else{
//        rs = record.getLinkFieldValue(node);
//      }
//    }
//    return null;
//  }
  protected boolean condition(IRecord record){
//    boolean equals = false;
//
//    return equalsCondition ? equals : !equals;
    //System.out.println(record.getNumFieldValue("idm_id"));
    return record.getNumFieldValue("idm_id") == record.getNumFieldValue("a_id");
  }
  protected Stack nameStack = new Stack();
  protected String getNameFromStack(){
    Iterator i = nameStack.iterator();
    StringBuffer sb = new StringBuffer();
    String name;
    while(i.hasNext()){
      name = (String) i.next();
      if(name != null){
        sb.append(name.replaceAll("\\.skn", ""));
        sb.append(".");
      }
    }
    if(sb.length() != 0 && sb.charAt(sb.length() - 1) == '.') sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  public void convert(String file, int from, int to, int maxCount, String condition){
    this.from = from;
    this.to = to;
    this.maxCount = maxCount;
    File input = new File(file);
    List fileList = new ArrayList();
    List sheetList = new ArrayList();
    File[] files = null;
    String[] sheetNames = null;
    File xls = null;
    if(input.isFile()){
      fileList.add(input);
      sheetList.add(input.getParentFile().getName());
      xls = new File(input.getParentFile().getName() + ".xls");
    }else if(input.isDirectory()){
      nameStack.push(input.getName());// null
      addName(fileList, sheetList, input);
      xls = new File(input.getName() + ".xls");
    }
    files = (File[]) fileList.toArray(new File[fileList.size()]);
    sheetNames = (String[]) sheetList.toArray(new String[sheetList.size()]);
    for(int i = 0; i < files.length; i++){
      Logger.info(files[i].getAbsolutePath());
    }
    for(int i = 0; i < sheetNames.length; i++){
      Logger.info(sheetNames[i]);
    }
    doConvert(files, xls, sheetNames);
  }
  protected void addName(List fileList, List sheetList, File fld){
    File[] children = fld.listFiles();
    File file;
    for(int i = 0; i < children.length; i++){
      file = children[i];
      if(file.isFile()){
        if(file.getName().indexOf("config") != -1){
          fileList.add(file);
          sheetList.add(getNameFromStack());
        }
      }
    }
    for(int i = 0; i < children.length; i++){
      file = children[i];
      if(file.isDirectory()){
        nameStack.push(file.getName());
        addName(fileList, sheetList, file);
      }
    }
    nameStack.pop();
  }

  protected void doConvert(File[] files, File xls, String[] sheetNames){
    Logger.printStack = true;

    WritableWorkbook wbook = null;
    try{
      wbook = Workbook.createWorkbook(xls);
      WritableSheet wsheet;
      Table table;
      for(int i = 0; i < files.length; i++){
        table = new Table();
        table.setConfigFileName(files[i].getAbsolutePath());
        table.bind();
        if(!table.isValid()){
          Logger.info("Error: " + table.getCfgFileName());
          continue;
        }
        wsheet = wbook.createSheet(sheetNames[i], i);
        printToSheet(table, wsheet, 0, 0, true);
        table.unBind(false);
      }
      wbook.write();
    }catch(IOException e){
      Logger.err(e);
    }catch(JXLException e){
      Logger.err(e);
    }finally{
      try{
        if(wbook != null) wbook.close();
      }catch(WriteException e){
        Logger.err(e);
      }catch(IOException e){
        Logger.err(e);
      }
    }

  }

  public int printFldCollection(IRecordSet table, IFieldCollection fldCollection, WritableSheet sheet, int col, int row) throws JXLException{
    int rowsWrote = 0;
    IField field;
    print("#", sheet, col++, row);
    for(int i = 0; i < fldCollection.getFieldCount(); i++){
      field = fldCollection.getField(i);
      print(field.getName(), sheet, col, row);
      int subRowsWrote = 0;
      if(field.isLink()){
        IRecordSet st = field.getLinkRecordSet();
        subRowsWrote = printFldCollection(st, st.getFldCollection(), sheet, col, row + 1);
        sheet.mergeCells(col, row, (col += getFieldCount(field)) - 1, row);
        if(subRowsWrote > rowsWrote) rowsWrote = subRowsWrote;
      }else col++;
    }
    print(table.getRecordCount(), sheet, col, row);
    return rowsWrote + 1;
  }
  public int printToSheet(IRecordSet table, WritableSheet sheet, int col, int row, boolean withFld) throws JXLException{
    int rowsWrote = 0;
    int oldCol = col;

    IRecord record;
    IRecordSet st;
    IFieldCollection fldCollection;
    IField field;
    fldCollection = table.getFldCollection();
    if(withFld) row += rowsWrote += printFldCollection(table, fldCollection, sheet, col, row);

    boolean withNormal = false;
    int subRowsWrote;
    int maxRowsWrote;
    int count = 0;
    if(to == -1) to = table.getRecordCount() - 1;
    if(maxCount == -1) maxCount = to;
    for(int i = from; i <= to; i++){
      if(count > maxCount || i >= table.getRecordCount()) break;
      record = table.getRecord(i);
      if(!condition(record)) continue;
      col = oldCol;
      print(i, sheet, col++, row);
      maxRowsWrote = 0;
      for(int j = 0; j < fldCollection.getFieldCount(); j++){
        subRowsWrote = 0;
        field = fldCollection.getField(j);
        if(field.isLink()){
          st = record.getLinkFieldValue(j);
          subRowsWrote += printToSheet(st, sheet, col, row, false);
          col += getFieldCount(field);
          if(subRowsWrote > maxRowsWrote) maxRowsWrote = subRowsWrote;
        }else{
          if(field.isNum()) print(record.getNumFieldValue(j), sheet, col++, row);
          else if(field.isData()) print(StringUtil.valueOf(BytesUtil.trimData(record.getDataFieldValue(j)),"ISO-8859-1"), sheet, col++, row);
          withNormal = true;
        }
      }
      row += maxRowsWrote + (withNormal ? 1 : 0);
      rowsWrote += maxRowsWrote + (withNormal ? 1 : 0);
      count++;
    }
    return rowsWrote - (withNormal ? 1 : 0);
  }

  protected int getFieldCount(IField field){
    int count = 1;
    IRecordSet st;
    IFieldCollection fc;
    if(field.isLink()){
      st = field.getLinkRecordSet();
      fc = st.getFldCollection();
      for(int i = 0; i < fc.getFieldCount(); i++){
        count += getFieldCount(fc.getField(i));
      }
      // count--;
    }
    return count;
  }

  protected void print(String data, WritableSheet sheet, int col, int row) throws JXLException{
    sheet.addCell(new Label(col, row, data));
  }
  protected void print(long data, WritableSheet sheet, int col, int row) throws JXLException{
    sheet.addCell(new Number(col, row, data));
  }

}
