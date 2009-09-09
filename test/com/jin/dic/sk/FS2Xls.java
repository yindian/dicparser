/*****************************************************************************
 * 
 * @(#)ZFS2Xls.java  2009/03
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

import com.jin.dic.sk.FSFile;
import com.jin.dic.sk.FileSystem;
import com.jin.util.Logger;

public class FS2Xls {

  /**
   * @param args
   * @throws Exception
   * @throws RowsExceededException
   */
  public static void main(String[] args) throws RowsExceededException, Exception{
    Logger.printStack = true;
    String[] names = getNames("D:/Program Files/Longman/ldoce4v2/data/package");

    String outFileName = "package.xls";
    File outFile = new File(outFileName);
    WritableWorkbook wbook = Workbook.createWorkbook(outFile);
    for(int i = 0; i < names.length; i++){
      FileSystem fs = new FileSystem();
      fs.setConfigFileName(names[i]);
      fs.bind();

      File fld = new File(names[i]);
      if(!fs.isValid()) continue;
      WritableSheet wsheet = wbook.createSheet(fld.getParentFile().getName(), i);
      printToSheet(fs.getRootDir(), wsheet, 0, 0);

      fs.unBind(false);
    }
    wbook.write();
    wbook.close();
  }
  public static int printToSheet(FSFile f, WritableSheet sheet, int col, int row)
      throws JXLException{
    FileSystem fs = f.getFileSystem();
    print(f.getId(), sheet, col++, row);
    print(f.getName(), sheet, col++, row);
    if(f.isFile()){
      if(f.getName().contains(".mp3")){
        print("1", sheet, col++, row);
      }else if(f.getName().contains(".jpg")){
        print("1", sheet, col++, row);
      }else if(f.getName().contains(".gif")){
        print("1", sheet, col++, row);
      }else if(f.getName().contains(".png")){
        print("1", sheet, col++, row);
      }else{
        String s = new String(f.getContent());
        print(s, sheet, col++, row);
      }
      return 1;
    }else{
      int oldRow = row;
      int rowsWrote = 0;
      int[] dirs = f.getDirs();
      int[] files = f.getFiles();
      print(dirs.length, sheet, col++, row);
      print(files.length, sheet, col++, row);

      for(int i = 0; i < dirs.length; i++){
        if(i > 5) break;
        rowsWrote = printToSheet(fs.getDir(dirs[i]), sheet, col++ - 3, ++row);
        col--;
        row += rowsWrote;
      }
      for(int i = 0; i < files.length; i++){
        if(i > 5) break;
        printToSheet(fs.getFile(files[i]), sheet, col - 3, ++row);
      }
      return row - oldRow;
    }
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
      if(files[i].isDirectory()) flds.add(files[i].getAbsolutePath() + "/filesystem.cff");
    }
    return (String[]) flds.toArray(new String[flds.size()]);
  }

}
