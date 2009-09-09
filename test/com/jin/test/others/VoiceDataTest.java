/*****************************************************************************
 * 
 * @(#)VoiceDataTest.java  2009/03
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
package com.jin.test.others;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.jin.dic.ks.voc.KSVoiceDataEngineSimple;

public class VoiceDataTest {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    File scrFolder = new File("D:\\Work\\JinWord\\Build\\temp");
    File desFolder = new File("D:\\Work\\JinWord\\Build\\java");
    File[] scrFiles = scrFolder.listFiles();
    File desFile;
    OutputStream os;
    KSVoiceDataEngineSimple voice = new KSVoiceDataEngineSimple(
        "D:\\Jin\\Data\\KingSoft\\Voice\\Data\\");
    for(int i = 0; i < scrFiles.length; i++){
      String word = scrFiles[i].getName().split("\\.")[0];
      byte[] data = voice.getData(word);
      desFile = new File(desFolder, word + ".snd");
      os = new BufferedOutputStream(new FileOutputStream(desFile));
      os.write(data);
      os.close();
    }
  }

}
