package org.jin.dic.data.ldoce.v5;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.dic.data.sk.i.IRecordSet;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class ae_gTokenList {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    String encoding = "unicode";
    String tableCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/arl.skn/idx_index.skn/wordlist/tok.skn/config.cft";
    String outFileName = "alpha/token_list.txt";
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
      IRecordSet tokenOcc;

      String token;
      int arlIndex;
      os.write(0xff);
      os.write(0xfe);
      for(int i = from; i <= to; i++){
        record = table.getRecord(i);
        tokenOcc = record.getLinkFieldValue("OCC");
        token = StringUtil.valueOf(BytesUtil.trimData(record.getDataFieldValue("TOKEN")), "utf-8");
        os.write(StringUtil.getBytesNoBom(token, encoding));
        os.write(StringUtil.getBytesNoBom("\t", encoding));

        for(int j = 0; j < tokenOcc.getRecordCount(); j++){
          arlIndex = (int) tokenOcc.getRecord(j).getNumFieldValue("a_id");
          if(j != 0) os.write(StringUtil.getBytesNoBom(",", encoding));
          os.write(StringUtil.getBytesNoBom(String.valueOf(arlIndex), encoding));
        }
        
        os.write(StringUtil.getBytesNoBom("\r\n", encoding));
      }
      os.close();
    }
    table.unBind(false);
  }
}
