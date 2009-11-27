package org.jin.dic.data.alpha;

import org.jin.dic.data.alpha.make.DeflatedDataSourceMaker;
import org.jin.dic.data.alpha.read.DeflatedDataSource;
import org.jin.dic.data.alpha.read.FileDataSource;
import org.jin.dic.data.alpha.read.NewSearchEntry;
import org.jin.dic.data.alpha.read.SearchEntry;
import org.jin.util.io._RandomAccessFile;

public class SearchEntryTest {

  public static void main(String[] args) throws Exception{
    NewSearchEntry newSearch = new NewSearchEntry();

    String nfn, fn, ndfn, dfn;
    if(true){
      nfn = "D:/Jin/Alpha/tmp/NewalphaId.srh";
      ndfn = "D:/Jin/Alpha/tmp/NewalphaId(c).srh";
      fn = "D:/Jin/Alpha/tmp/alphaId.srh";
      dfn = "D:/Jin/Alpha/tmp/alphaId(c).srh";
    }else{
      nfn = "Z:/Temp/alpha/NewalphaId.srh";
      ndfn = "Z:/Temp/alpha/NewalphaId(c).srh";
      fn = "Z:/Temp/alpha/alphaId.srh";
      dfn = "Z:/Temp/alpha/alphaId(c).srh";
    }
    _RandomAccessFile dsF = new _RandomAccessFile(dfn, "r");
    _RandomAccessFile fdsF = new _RandomAccessFile(fn, "r");

    DeflatedDataSource dds = new DeflatedDataSource();
    dds.setFractionSize(DeflatedDataSourceMaker.getFRACTIONSIZE());
    dds.setDataSize((int) fdsF.length());
    dds.setFile(dsF);

    FileDataSource fds = new FileDataSource();
    fds.setDataSize((int) fdsF.length());
    fds.setFile(fdsF);

    SearchEntry search = new SearchEntry();
    search.setDataSource(fds);
    // search.setDataSource(dds);
    search.setCaseSensitive(false);
    search.setDataOffset(fds.getDataSize());

    // nfn = "D:/Jin/Alpha/tmp/test.srh";
    _RandomAccessFile ndsF = new _RandomAccessFile(ndfn, "r");
    _RandomAccessFile nfdsF = new _RandomAccessFile(nfn, "r");

    DeflatedDataSource ndds = new DeflatedDataSource();
    ndds.setFractionSize(0x1000);
    ndds.setDataSize((int) nfdsF.length());
    ndds.setFile(ndsF);

    FileDataSource nfds = new FileDataSource();
    nfds.setDataSize((int) nfdsF.length());
    nfds.setFile(nfdsF);

    newSearch.setDataSource(nfds);
    // newSearch.setDataSource(ndds);
    newSearch.setDataIsCaseSensitive(true);

    String word = "c";

    int offset;
    int[] offsets;
    long a, b;

    b = System.nanoTime();
    offset = search.getDataOffset(word);
    a = System.nanoTime();
    System.out.print("search\t:\t");
    System.out.print((a - b) / 1000000.0 + "ms");
    System.out.print("\t");
    System.out.println(offset);

    b = System.nanoTime();
    offset = newSearch.getFirstDataOffset(word);
    a = System.nanoTime();
    System.out.print("offset\t:\t");
    System.out.print((a - b) / 1000000.0 + "ms");
    System.out.print("\t");
    System.out.println(offset);

    b = System.nanoTime();
    offsets = newSearch.search(word, true);
    a = System.nanoTime();
    System.out.print("ofsts\t:\t");
    System.out.print((a - b) / 1000000.0 + "ms\t");
    for(int i = 0; i < offsets.length; i++){
      if(i > 0) System.out.print(",");
      System.out.print(offsets[i]);
    }
    System.out.println();

    fdsF.close();
    dsF.close();
    nfdsF.close();
    ndsF.close();
  }

}
