package org.jin.dic.data.ldoce.v5;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class a3_gPublisher2eIdList {

  public static void main(String[] args) throws IOException{
    String encoding = "unicode";
    String tableCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/mapping.skn";
    String outFileName = "alpha/publisher_eId_list.txt";
    File file = new File(outFileName);
    OutputStream os = null;
    os = new BufferedOutputStream(new FileOutputStream(file));

    Table table;
    table = new Table();
    table.setConfigFileName(tableCfgName);
    table.bind();
    if(!table.isValid()){
      Logger.info("Error: " + table.getCfgFileName());
    }else{
      int from = 0;
      int to = table.getRecordCount() - 1;
      IRecord record;
      String arl;
      os.write(0xff);
      os.write(0xfe);
      for(int i = from; i <= to; i++){
        record = table.getRecord(i);
        arl = StringUtil.valueOf(BytesUtil.trimData(record.getDataFieldValue("publisher_id")), "utf-8");
        os.write(StringUtil.getBytesNoBom(arl, encoding));
        os.write(StringUtil.getBytesNoBom("\t", encoding));
        os.write(StringUtil.getBytesNoBom(String.valueOf(record.getNumFieldValue("idm_id")), encoding));
        os.write(StringUtil.getBytesNoBom("\r\n", encoding));
      }
      os.close();
    }
    table.unBind(false);
  }
}
