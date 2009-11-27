package example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import com.jcraft.jzlib.ZOutputStream;

public class DeflateTest {
  public static void main(String[] args) throws Exception{
    int level = 9;
    String fileName = "data/aeu.txt";
    File inFile = new File(fileName);
    InputStream in = new BufferedInputStream(new FileInputStream(inFile));

    File outFile = new File(fileName + ".ift");
    OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
    ZOutputStream zOut = new ZOutputStream(out, level,true);

    File outFile2 = new File(fileName + "(S).ift");
    OutputStream out2 = new BufferedOutputStream(new FileOutputStream(outFile2));
    Deflater c = new Deflater(level);
    DeflaterOutputStream dos = new DeflaterOutputStream(out2, c);
    // c.setStrategy(Deflater.FILTERED);
    // c.setDictionary(abyte0)
    byte[] tempBuf = new byte[0x8000];
    int readCount = 0;
    // zOut.setFlushMode(JZlib.Z_SYNC_FLUSH);
    while(true){
      readCount = in.read(tempBuf);
      if(readCount <= 0) break;

      zOut.write(tempBuf, 0, readCount);
      // zOut.flush();
      dos.write(tempBuf, 0, readCount);
    }
    zOut.close();
    dos.close();
  }
}
