package org.jin.dic.data.alpha;

import org.jin.dic.data.alpha.read.CatalogedDataBlock;
import org.jin.dic.data.alpha.read.FileDataSource;
import org.jin.util.io._RandomAccessFile;

public class CatalogedDataSetRead {
  public static void main(String[] args) throws Exception{
    String fn = "alpha/entryDataSet.dst";
    _RandomAccessFile fdsF = new _RandomAccessFile(fn, "r");
    FileDataSource fds = new FileDataSource();
    fds.setDataSize((int) fdsF.length());
    fds.setFile(fdsF);
    CatalogedDataBlock a = new CatalogedDataBlock();
    a.setDataSource(fds);
    a.setBaseOffset(0);
    a.setDataCount(51604);

    int index = 5251;
    _RandomAccessFile o = new _RandomAccessFile(index + ".xml", "rw");
    o.write(a.getData(index));
    o.close();
  }
}
