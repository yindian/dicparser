/*****************************************************************************
 * 
 * @(#)VoiceDataFile.java  2009/03
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

public class VoiceDataFile {

  public final static int RA4      = 4;
  public final static int RA3      = 3;

  private String          word     = null;
  private String          fileName = null;
  private String          id       = null;
  private int             fileType = 0;

  public String getWord(){
    return word;
  }
  public void setWord(String word){
    this.word = word;
  }
  public String getFileName(){
    return fileName;
  }
  public void setFileName(String fileName){
    this.fileName = fileName;
  }
  public String getId(){
    return id;
  }
  public void setId(String id){
    this.id = id;
  }
  public int getFileType(){
    return fileType;
  }
  public void setFileType(int fileType){
    this.fileType = fileType;
  }

}
