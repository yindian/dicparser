/*****************************************************************************
 * 
 * @(#)WORD_OFFSET.java  2009/03
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
package org.jin.dic.data.test.others;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.dic.data.ks.voc.KSVoiceDataEngine;
import org.jin.util.io._DataInputStream;


public class WORD_OFFSET {

  /**
   * @param args
   * @throws BadFormatException
   * @throws IoException
   */
  public static void main(String[] args) throws IOException, BadFormatException{
    KSVoiceDataEngine voice = new KSVoiceDataEngine("D:\\Jin\\Data\\KingSoft\\Voice\\Data\\");

    File fil = new File("D:\\Jin\\Data\\KingSoft\\Voice\\Data\\MVOX.IDX");
    InputStream in = new BufferedInputStream(new FileInputStream(fil));
    _DataInputStream le = new _DataInputStream(in);
    File out = new File("D:\\Jin\\Data\\KingSoft\\Voice\\Data\\WORDS_INDEX_RAW.DAT");
    OutputStream os = new BufferedOutputStream(new FileOutputStream(out));

    int index0Length = 26 * 26 * 2 + 2;
    le.skipBytes(index0Length - 2);

    int index1Length = (le.readChar() + 1) << 2;
    le.skipBytes(index1Length);

    int len;
    byte[] buf;
    int rawOffset;
    int offset;
    while(le.available() > 0){
      len = le.read();
      buf = new byte[len];
      le.read(buf, 0, len);
      rawOffset = le.readInt();
      offset = voice.getVoiceDataOffset(new String(buf));
      if(offset != rawOffset){
        System.out.println(new String(buf));
        System.out.println(rawOffset);
        System.out.println(offset);
      }
      os.write(buf);
      os.write(0x09);
      os.write((String.valueOf(rawOffset)).getBytes());
      os.write(0x0d);
      os.write(0x0a);
    }
    in.close();
    os.close();
  }

}
