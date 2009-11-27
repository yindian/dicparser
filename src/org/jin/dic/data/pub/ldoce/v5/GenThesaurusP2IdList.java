/*****************************************************************************
 * 
 * @(#)GenThesaurusP2IdList.java  2009/11
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jin.dic.data.pub.CommonConstants;
import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class GenThesaurusP2IdList {

  public static void main(String[] args) throws IOException{
    String tableCfgName = args[0];
    String outFileName = args[1];
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
        os.write(StringUtil.getBytesNoBom(arl, CommonConstants.ENCODING));
        os.write(CommonConstants.SEPARATORBYTES);
        os.write(StringUtil.getBytesNoBom(String.valueOf(record.getNumFieldValue("idm_id")), CommonConstants.ENCODING));
        os.write(CommonConstants.LSBYTES);
      }
      os.close();
    }
    table.unBind(false);
  }

}
