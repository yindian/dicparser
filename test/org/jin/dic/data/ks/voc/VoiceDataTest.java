/*****************************************************************************
 * 
 * @(#)VoiceDataTest.java  2009/03
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
package org.jin.dic.data.ks.voc;

import java.util.Arrays;

import org.jin.dic.data.ks.voc.VoiceDataEngine;
import org.jin.util.BytesUtil;


public abstract class VoiceDataTest {

  static boolean testGetData(VoiceDataEngine voice){
    boolean err = false;
    String word;
    byte[] data;
    boolean condition;
    for(int i = 0; i < VoiceDataBase.words.length; i++){
      word = VoiceDataBase.words[i];
      data = voice.getContent(word);
      condition = Arrays.equals(VoiceDataBase.datas[i], data);
      if(!condition){
        System.out.println(word);
        System.out.println("Got  : " + BytesUtil.convert(data));
        System.out.println("Wants: " + BytesUtil.convert(VoiceDataBase.datas[i]));
      }
      err = err ? err : !condition;
    }
    return err;
  }

}
