/*****************************************************************************
 * 
 * @(#)AddLicense.java  2009/03
 *
 *  Copyright (C) 2009  Tim Bron<jinxingquan@google.com>
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
package com.jin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class AddLicense {

  public static void main(String[] args){
    String rootFolder = "D:\\Jin\\Data\\KingSoft\\ks\\src\\com\\jin\\util";
    String licenseFile = "D:\\Jin\\Data\\KingSoft\\ks\\src\\license.txt";

    File root = new File(rootFolder);
    File license = new File(licenseFile);

    getLicenseData(license);
    recurseFiles(root);
  }

  private static byte[] licenseData;
  private static void getLicenseData(File license){
    try{
      licenseData = getBytesFromFile(license);
    }catch(IOException e){
      Logger.err(e);
    }
  }

  private static void recurseFiles(File parent){
    File[] files = parent.listFiles(new AddLicense.FilenameFilterImp());
    File file;
    for(int i = 0; i < files.length; i++){
      file = files[i];
      if(file.isDirectory()) recurseFiles(file);
      if(file.isFile()) addLicense(file, licenseData);
    }
  }

  public static void addLicense(File file, byte[] licenseData){
    OutputStream os = null;
    try{
      byte[] fileData = getBytesFromFile(file);
      os = new FileOutputStream(file);
      os.write(licenseData);
      os.write(fileData);
    }catch(IOException e){
      Logger.err(e);
    }finally{
      if(os != null) try{
        os.close();
        replaceTags(file);
      }catch(IOException e){
        Logger.err(e);
      }
    }
  }

  private static void replaceTags(File file) throws IOException{
    File temp = new File(file.getParent(), file.getName() + ".bak");
    if(!temp.exists()) temp.createNewFile();
    FileInputStream fis = new FileInputStream(file);
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String line;

    FileOutputStream fos = new FileOutputStream(temp);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    while((line = br.readLine()) != null){
      if(line.indexOf("@filename@") != -1) line = line.replaceFirst("@filename@", file.getName());
      if(line.indexOf("@date@") != -1) line = line.replaceFirst("@date@", "2009/03");
      bw.write(line);
      bw.write("\r\n");
    }
    br.close();
    bw.close();
    file.delete();
    temp.renameTo(file);
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

  private static class FilenameFilterImp implements FilenameFilter {

    public boolean accept(File dir, String name){
      return new File(dir, name).isDirectory() || name.endsWith(".java");
    }

  }

}
