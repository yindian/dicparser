package org.jin.dic.data.sk;

public class ZZZZZZClassInfoTest {

  /**
   * @param args
   */
  public static void main(String[] args){
    long b = System.nanoTime();
    int count = 100000;
    long mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    Object[] objs = new Object[count];
    for(int i = 0; i < count; i++){
      // objs[i] = new PageFile();
    }
    System.out.println((System.nanoTime() - b) / count);
    System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - mem) / count);
  }
  // File 0.7 ms 51 bytes
  // Table 0.8 ms 51 bytes
  // Field 0.9 ms 59 bytes
  // Record 1.7 ms 107 bytes
  // RecordSet 1.99 ms 107 bytes
  // PageFile 5.79 ms 299 bytes
}
