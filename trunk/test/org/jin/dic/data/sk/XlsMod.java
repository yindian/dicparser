package org.jin.dic.data.sk;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class XlsMod {

  /**
   * @param args
   * @throws IOException
   * @throws BiffException
   * @throws WriteException
   */
  public static void main(String[] args) throws IOException, BiffException, WriteException{
    Workbook wb = Workbook.getWorkbook(new File(
        "D:/Jin/Work/WorkSpace/KingSoft/Copy of index_doc.xls"));
    WritableWorkbook book = Workbook.createWorkbook(new File(
        "D:/Jin/Work/WorkSpace/KingSoft/Copy of index_doc.xls"), wb);
    WritableSheet sheet = book.getSheet(0);
    WritableCell cell = null;
    int count = sheet.getRows();
    String l = "";
    String l2 = "";
    for(int i = 0; i < count; i++){
      cell = sheet.getWritableCell(1, i);
      if(cell.getContents().length() != 0){
        l = cell.getContents();
      }else{
        sheet.addCell(new Label(1, i, l));
      }
      cell = sheet.getWritableCell(0, i);
      if(cell.getContents().length() != 0){
        l2 = cell.getContents().replaceAll("\\..*", "");
      }else{
        sheet.addCell(new Label(0, i, l2));
      }

    }
    book.write();
    book.close();
  }

}
