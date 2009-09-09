/*****************************************************************************
 * 
 * @(#)KSVoiceDataEngineTest.java  2009/03
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
package com.jin.dic.ks.voc;

import java.io.IOException;

import junit.framework.TestCase;

import com.jin.dic.ks.BadFormatException;
import com.jin.dic.ks.TestFolderConfig;
import com.jin.dic.ks.voc.KSVoiceDataEngine;

public class KSVoiceDataEngineTest extends TestCase {

  KSVoiceDataEngine ksVoice = null;
  public void testGetVoiceDataOffset() throws IOException, BadFormatException{
    if(ksVoice == null) ksVoice = new KSVoiceDataEngine(TestFolderConfig.v_original);
    String word;
    int offset;
    for(int i = 0; i < VoiceDataBase.words.length; i++){
      word = VoiceDataBase.words[i];
      offset = ksVoice.getVoiceDataOffset(word);
      assertTrue(word + offset, offset == VoiceDataBase.offsets[i]);
    }
  }

  public void testGetVoiceData() throws IOException, BadFormatException{
    if(ksVoice == null) ksVoice = new KSVoiceDataEngine(TestFolderConfig.v_original);
    assertTrue(!VoiceDataTest.testGetData(ksVoice));
  }

}
