package example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jin.util.io._ByteArrayOutputStream;

import com.jcraft.jzlib.ZInputStream;

public class InflateTest {
  public static void main(String[] args) throws Exception{
    String fileName = "C11/5555258,12800.cmp";
    File inFile = new File(fileName);
    InputStream in = new BufferedInputStream(new FileInputStream(inFile));
    ZInputStream zIn = new ZInputStream(in, true);

    _ByteArrayOutputStream baos = new _ByteArrayOutputStream(0x4000);
    byte[] tempBuf = new byte[0x4000];
    int readCount = 0;
    while(zIn.available() > 0){
      readCount = zIn.read(tempBuf);
      if(readCount <= 0) {
        System.out.println(zIn.getTotalIn());
        System.out.println(zIn.getTotalOut());
//        break;
      }
      else
      baos.write(tempBuf, 0, readCount);
    }
    System.out.println(zIn.getTotalIn());
    System.out.println(zIn.getTotalOut());
    // write data
    File outFile = new File(fileName + ".txt");
    OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
    out.write(baos.toByteArray());
    out.close();
  }
}
