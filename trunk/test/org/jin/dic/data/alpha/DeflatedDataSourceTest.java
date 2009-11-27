package org.jin.dic.data.alpha;

import java.io.File;
import java.io.IOException;

import org.jin.dic.data.alpha.make.DeflatedDataSourceMaker;
import org.jin.dic.data.alpha.read.DeflatedDataSource;
import org.jin.util.io._RandomAccessFile;

public class DeflatedDataSourceTest {

  public static void main(String[] args) throws IOException{

    String[] fileNames = new String[] { "D:/Jin/Alpha/tmp/NewalphaId.srh" };

    DeflatedDataSourceMaker.setFRACTIONSIZE(0x1000);
    DeflatedDataSourceMaker.make(fileNames, "D:/Jin/Alpha/tmp/NewalphaId(c).srh");
//    DeflatedDataSource dds = new DeflatedDataSource();
//    dds.setFile(new _RandomAccessFile("alpha/search_tree(c).dat", "r"));
//    dds.setDataSize(getSize(fileNames));
//    dds.setFractionSize(DeflatedDataSourceMaker.getFRACTIONSIZE());
//    _RandomAccessFile o = new _RandomAccessFile("allin1", "rw");
//    o.write(dds.getData(0, dds.getDataSize()));
//    o.close();
  }
  public static int getSize(String[] fileNames){
    int len = 0;
    for(int i = 0; i < fileNames.length; i++){
      len += new File(fileNames[i]).length();
    }
    return len;
  }

}
