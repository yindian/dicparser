/*****************************************************************************
 * 
 * @(#)_ZipFile.java  2009/03
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
package com.jin.util.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class _ZipFile {

  private ZipFile zFile    = null;
  ZipOutputStream zos      = null;
  private String  fileName = null;
  public _ZipFile(String zipFileName, String mode) throws IOException {
    if(mode.equalsIgnoreCase("r")){
      File file = new File(zipFileName);
      zFile = new ZipFile(file, ZipFile.OPEN_READ);
    }
    if(mode.equalsIgnoreCase("w")) fileName = zipFileName;
  }

  public int getFileLength(String name){
    ZipEntry dataEntry = zFile.getEntry(name);
    // FIXME ทþมห entry == null when runs in jre 1.4
    if(dataEntry == null) return -1;
    return (int) dataEntry.getSize();
  }

  public byte[] getData(String name) throws IOException{
    byte[] data = new byte[0];
    ZipEntry dataEntry = zFile.getEntry(name);// TODO what if name is null
    if(dataEntry != null){
      InputStream in = null;
      in = zFile.getInputStream(dataEntry);
      _ByteArrayOutputStream bos = new _ByteArrayOutputStream((int) dataEntry.getSize());
      int b;
      while((b = in.read()) != -1)
        bos.write(b);
      data = bos.toByteArray(true);
    }
    return data;
  }

  public void write(String name, byte[] data, int off, int len) throws IOException{
    if(zos == null){
      zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
      zos.setLevel(Deflater.DEFAULT_COMPRESSION);
    }
    ZipEntry dataEntry = new ZipEntry(name);
    zos.putNextEntry(dataEntry);
    zos.write(data, off, len);
  }

  public void write(String name, byte[] data) throws IOException{
    if(zos == null){
      zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
      zos.setLevel(Deflater.DEFAULT_COMPRESSION);
    }
    ZipEntry dataEntry = new ZipEntry(name);
    zos.putNextEntry(dataEntry);
    zos.write(data);
  }

  public void close() throws IOException{
    if(zos != null) zos.close();
    if(zFile != null) zFile.close();
  }
}
