package org.jin.dic.data.alpha;

import org.jin.dic.data.alpha.read.FileDataSource;
import org.jin.dic.data.alpha.read.SearchEntry;
import org.jin.util.io._RandomAccessFile;

public class Search_aIdTest {

  public static void main(String[] args) throws Exception{
    _RandomAccessFile fdsF = new _RandomAccessFile("alpha/alphaPos_aId.srh", "r");

    FileDataSource fds = new FileDataSource();
    fds.setDataSize((int) fdsF.length());
    fds.setFile(fdsF);
    
    SearchEntry search = new SearchEntry();
    search.setDataSource(fds);
    search.setCaseSensitive(false);
    search.setLowerCase(true);
    search.setDataOffset(fds.getDataSize());

    /**
     * !Kung landlady zymurgy zyzzyva
     */
    int offset;
    long b = System.nanoTime();
    offset = search.getDataOffset("book_1");
    long a = System.nanoTime();

    System.out.println(" " + offset);
    System.out.println((a - b) / 1000000.0 + "ms");

    fdsF.close();
  }

}
