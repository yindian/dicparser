package org.jin.dic.data.webster.c11.v3_0;

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
    int level = 6;
    String fileName = "C11/bad.inflate.dat";
    File inFile = new File(fileName);
    InputStream in = new BufferedInputStream(new FileInputStream(inFile));

    File outFile = new File(fileName + ".ift");
    OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
    ZOutputStream zOut = new ZOutputStream(out, level, true);
    byte[] tempBuf = new byte[0x8000];
    int readCount = 0;
    while(true){
      readCount = in.read(tempBuf);
      if(readCount <= 0) break;

      zOut.write(tempBuf, 0, readCount);
    }
  }
}
