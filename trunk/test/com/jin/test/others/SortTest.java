/*****************************************************************************
 * 
 * @(#)SortTest.java  2009/03
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.jin.dic.ks.voc.VoiceDataFile;

public class SortTest {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{
    saveList("D:\\Jin\\Data\\KingSoft\\Voice\\Data\\SORT\\SORT.DAT",
        sortList(getList("D:\\Jin\\Data\\KingSoft\\Voice\\Data\\SORT\\RAND.DAT")));
  }
  public static List getList(String scrFile){
    List fileList = new ArrayList();
    VoiceDataFile dataFile = null;
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream(scrFile);
      br = new BufferedReader(new InputStreamReader(fis));
      String line;
      while((line = br.readLine()) != null){
        dataFile = new VoiceDataFile();
        dataFile.setWord(line);
        fileList.add(dataFile);
      }
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }catch(IOException e){
      e.printStackTrace();
    }finally{
      try{
        if(br != null) br.close();
      }catch(IOException e){
        e.printStackTrace();
      }
    }
    return fileList;
  }

  public static List sortList(List fileList){
    // KSWordComparator comp = new KSWordComparator();
    // Collections.sort(fileList, comp);
    return fileList;
  }

  public static void saveList(String desFile, List fileList) throws IOException{
    File out = new File(desFile);
    OutputStream os = new BufferedOutputStream(new FileOutputStream(out));
    for(int i = 0; i < fileList.size(); i++){
      // os.write(((WordBean) fileList.get(i)).getWord().getBytes());
      os.write(0x0d);
      os.write(0x0a);
    }
    os.close();
  }
}
