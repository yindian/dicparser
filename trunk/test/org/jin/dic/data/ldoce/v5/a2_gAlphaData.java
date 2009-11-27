package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jin.dic.data.alpha.make.CatalogedDataSetMaker;
import org.jin.util.StringUtil;
import org.jin.util.io._ByteArrayOutputStream;
import org.jin.util.io._DataOutputStream;

public class a2_gAlphaData {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    // TODO Auto-generated method stub
    CatalogedDataSetMaker maker = new CatalogedDataSetMaker("alpha/alpha_list");
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream("alpha/alpha_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      _ByteArrayOutputStream baos = new _ByteArrayOutputStream();
      _DataOutputStream os = new _DataOutputStream(baos, true);
      String arl;
      String type;
      String pos;
      String hom;
      String eId;
      int len;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 5){
          continue;
        }
        baos.reset();
        arl = info[0];
        type = info[1];
        pos = info[2];
        hom = info[3];
        eId = info[4];
        len = (arl.length() << 1) + 9;
        os.write(arl.length() << 1);
        os.write(StringUtil.getBytesNoBom(arl, "unicode"));
        os.write(Integer.valueOf(type).intValue());
        os.write(Integer.valueOf(hom).intValue());
        os.writeChar(Integer.valueOf(pos).intValue());
        os.writeInt(Integer.valueOf(eId).intValue());

        maker.addData(baos.toByteArray(len));
      }
    }finally{
      if(br != null) br.close();
      if(maker != null) maker.close();
    }
  }

}
