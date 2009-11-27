/*****************************************************************************
 * 
 * @(#)CatalogedDataSetMaker.java  2009/11
 *
 *  Copyright (C) 2009  Tim Bron<jinxingquan@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *****************************************************************************/
package org.jin.dic.data.alpha.make;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jin.util.io._RandomAccessFile;

public class CatalogedDataSetMaker {

  public static void make(String[] fileNames, String desFileName) throws IOException{
    new CatalogedDataSetMaker(desFileName).domake(fileNames);
  }

  _RandomAccessFile os;
  _RandomAccessFile map;
  public CatalogedDataSetMaker(String desFileName) throws IOException {
    os = new _RandomAccessFile(desFileName+".dat", "rw");
    map = new _RandomAccessFile(desFileName + ".mng", "rw");
    os.seek(os.length());
    map.seek(map.length());
  }
  private void domake(String[] fileNames) throws IOException{
    File file;
    for(int i = 0; i < fileNames.length; i++){
      file = new File(fileNames[i]);
      addData(getBytesFromFile(file));
    }
    close();
  }

  private static byte[] getBytesFromFile(File file) throws IOException{
    InputStream is = new FileInputStream(file);
    // Get the size of the file
    long length = file.length();
    // You cannot create an array using a long type.
    // It needs to be an int type.
    // Before converting to an int type, check
    // to ensure that file is not larger than Integer.MAX_VALUE.
    if(length > Integer.MAX_VALUE){
      // File is too large
    }
    // Create the byte array to hold the data
    byte[] bytes = new byte[(int) length];
    // Read in the bytes
    int offset = 0;
    int numRead = 0;
    while(offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0){
      offset += numRead;
    }
    try{
      // Ensure all the bytes have been read in
      if(offset < bytes.length){
        throw new IOException("Could not completely read file " + file.getName());
      }
    }finally{
      is.close();
    }
    return bytes;
  }

  public void close() throws IOException{
    os.close();
    map.close();
  }
  
  public void addData(byte[] data) throws IOException{
    map.writeInt((int) os.getFilePointer());
    os.writeInt(data.length);
    os.write(data);
  }

}
