/*****************************************************************************
 * 
 * @(#)Generate.java  2009/11
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
package org.jin.dic.data.pub.webster.c11.v3_0;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.jin.dic.data.pub.CommonConstants;
import org.jin.dic.data.webster.c11.v3_0.Parse;
import org.jin.util.Logger;
import org.jin.util.StringUtil;
import org.jin.util.io._RandomAccessFile;

public class Generate {

  public static void main(String[] args) throws IOException{

    Parse parse = new Parse();
    InputStream in = new BufferedInputStream(new FileInputStream(args[0]));
    _RandomAccessFile data;
    _RandomAccessFile index;
    index = new _RandomAccessFile(args[1], "rw");
    index.write(0xff);
    index.write(0xfe);

    Set entryPointSet = new HashSet();
    StringBuffer entry[] = null;
    String[] variants;
    String fileName;
    String entryPoint;
    int count = 0;
    // boolean found = false;
    while(in.available() > 0){
      entry = parse.getEntry(in);
      // if(!parse.getKeyWord().equals("a")) continue;
      // if(found) break;
      // found = true;
      fileName = parse.getAscKeyWord().toLowerCase();
      fileName = fileName.replaceAll("/|\\?|[<>]|:", "_");
      fileName = fileName.replaceAll("aux", "auxaux");
      fileName = fileName.replaceAll("con", "concon");
      fileName = fileName.replaceAll("prn", "pronplon");
      fileName = fileName + args[2];
      // fileName = Common.getFile(args[3], fileName).getAbsolutePath();

      data = new _RandomAccessFile(args[3] + "/" + fileName, "rw");
      if(data.length() > 0){
        data.skipBytes((int) data.length());
        data.write(CommonConstants.NEWLINEBYTES);
        data.write(CommonConstants.NEWLINEBYTES);
        data.write(CommonConstants.HBARBYTES);
      }else{// Pack.java writes the fffe
        // data.write(0xff);
        // data.write(0xfe);
      }
      data.write(StringUtil.getBytesNoBom(entry[0].toString(), CommonConstants.ENCODING));
      data.close();
      variants = entry[1].toString().split(";;");
      for(int i = -1; i < variants.length; i++){
        entryPoint = i == -1 ? parse.getKeyWord() : variants[i];
        entryPoint = entryPoint.toLowerCase();
        if(entryPoint != null && entryPoint.length() > 0){
          if(!entryPointSet.contains(entryPoint)){
            index.write(StringUtil.getBytesNoBom(entryPoint, CommonConstants.ENCODING));
            index.write(CommonConstants.SEPARATORBYTES);
            index.write(StringUtil.getBytesNoBom(fileName, CommonConstants.ENCODING));
            index.write(CommonConstants.LSBYTES);
            entryPointSet.add(entryPoint);
          }
        }
      }
      if(count++ % 100 == 0) Logger.info(count);
    }
    index.close();
  }
}
