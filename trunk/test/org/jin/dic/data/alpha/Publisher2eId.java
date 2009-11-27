package org.jin.dic.data.alpha;

import org.jin.dic.data.alpha.read.FileDataSource;
import org.jin.dic.data.alpha.read.SearchEntry;
import org.jin.util.io._RandomAccessFile;

public class Publisher2eId {

  public static void main(String[] args) throws Exception{
    SearchEntry search = new SearchEntry();
    _RandomAccessFile fdsF = new _RandomAccessFile("alpha/publisher_eId.srh", "r");

    FileDataSource fds = new FileDataSource();
    fds.setDataSize((int) fdsF.length());
    fds.setFile(fdsF);

    search.setDataSource(fds);

    search.setCaseSensitive(false);
    search.setLowerCase(true);
    search.setDataOffset(fds.getDataSize());
    /**
     * !Kung landlady zymurgy zyzzyva
     */
    int offset;
    long b = System.nanoTime();
    offset = search.getDataOffset("u2fc098491a42200a.262cc60a.1180415e23b.2a9e");
    offset = search.getDataOffset("u2fc098491a42200a.262cc60a.1180415e23b.2aac");
    offset = search.getDataOffset("u2fc098491a42200a.6e2b450a.115043df8ee.-1261");
    long a = System.nanoTime();

    System.out.println(" " + offset);
    System.out.println((a - b) / 1000000.0 + "ms");

    fdsF.close();
  }

}
