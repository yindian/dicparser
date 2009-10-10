/*****************************************************************************
 * 
 * @(#)Configurer.java  2009/03
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
package com.jin.dic.sk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jin.dic.sk.i.IConfigable;
import com.jin.util.Logger;

class Configurer {

  public void config(File file, IConfigable c) throws IOException{
    IniParser ini = null;
    try{
      StringBuffer section, name, value;

      ini = new IniParser();
      ini.setFileName(file);
      while((section = new StringBuffer()) != null && (name = new StringBuffer()) != null
          && (value = new StringBuffer()) != null && ini.readLine(section, name, value)){
        c.configureItem(section.toString(), name.toString(), value.toString());
      }
    }finally{
      if(ini != null) ini.close();
    }
  }

  private static class IniParser {

    static Pattern removeInLineComments  = Pattern.compile("/\\*.*\\*/");
    static Pattern removeInLineComments_ = Pattern.compile("//.*");
    static Pattern trim                  = Pattern.compile("\\s*");
    static Pattern beginComments         = Pattern.compile("/\\*.*");
    static Pattern endComments           = Pattern.compile(".*\\*/");
    static Pattern getSection            = Pattern.compile("\\[(.*)\\]");

    BufferedReader br                    = null;
    String         line                  = null;
    String         section;
    boolean        inComments            = false;

    public void setFileName(File file) throws FileNotFoundException{
      FileInputStream fis = new FileInputStream(file);
      br = new BufferedReader(new InputStreamReader(fis));
    }

    public boolean readLine(StringBuffer section, StringBuffer name, StringBuffer value)
        throws IOException{
      line = br.readLine();
      if(line == null) return false;

      line = removeInLineComments.matcher(line).replaceAll("");
      line = removeInLineComments_.matcher(line).replaceAll("");
      line = trim.matcher(line).replaceAll("");

      if(beginComments.matcher(line).find()){
        inComments = true;
        line = beginComments.matcher(line).replaceAll("");
      }
      if(inComments && endComments.matcher(line).find()){
        inComments = false;
        line = endComments.matcher(line).replaceAll("");
      }

      Matcher m = getSection.matcher(line);
      boolean isSection = m.find();
      if(isSection) this.section = m.group(1);
      if(line.length() == 0 || isSection || inComments) return readLine(section, name, value);

      String[] temp = line.split("=");
      if(temp.length == 2){
        section.append(this.section);
        name.append(temp[0]);
        value.append(temp[1]);
        return true;
      }else{
        Logger.err("err:" + line);
      }
      return false;
    }

    public void close(){
      if(br != null) try{
        br.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }

  }

}
