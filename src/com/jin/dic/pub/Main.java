/*****************************************************************************
 * 
 * @(#)Main.java  2009/03
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
package com.jin.dic.pub;

import java.io.Console;
import java.io.File;
import java.io.IOException;

import com.jin.dic.ks.BadFormatException;
import com.jin.util.Logger;

public class Main {

  public static void main(String[] args) throws IOException, BadFormatException{
    if(args == null || args.length != 3){
      Logger.info("By Tim<jinxingquan@google.com>\n\n" +
      		"To dump a dic file:\n" +
      		"  -d dictFilePath desFolder\nTo generate a dic file:\n" +
      		"  -g srcFolder dictFilePath\n\n" +
      		"e.g.\n" +
      		"-d \"D:\\1#520.DIC\" \"D:\\desFloder\"\n" +
      		"when job done desFolder should contain the following files:\n" +
      		" 1#520.DIC.zip(data file)\n" +
      		" 1#520.DIC.txt(index file with word and fileName key-value pair)\n" +
      		" 1#520.DIC.inf(dict info)\n\n" +
      		"-g \"D:\\srcFolder\" \"D:\\1#520.DIC\"\n" +
      		"the srcFolder should contain the following files:\n" +
      		" 1#520.DIC.zip\n" +
      		" 1#520.DIC.txt\n" +
      		" 1#520.DIC.inf\n" +
      		"when job done, D:\\1#520.DIC should be generated");
      Console c = System.console();
      if(c != null) c.readLine();
      return;
    }
    String engine;
    String srcFld = args[1];
    String desFld = args[2];
    if(args[0].equalsIgnoreCase("-d")) engine = "com.jin.dic.ks.dic.KSDictDataEngine";
    else if(args[0].equalsIgnoreCase("-g")){
      engine = "com.jin.dic.ks.dic.CSDictDataEngine";
      File dicFile = new File(desFld);
      srcFld = getFld(srcFld) + dicFile.getName();
      desFld = dicFile.getParent();
    }else return;

    ConverterThread thread = new ConverterThread(new Converter());
    thread.setEngine(engine, srcFld);
    thread.setDesFld(desFld);

    thread.start();
  }
  
  private static final String getFld(String fld){
    if(fld.endsWith("\\") || fld.endsWith("/")) return fld;
    else return fld + File.separator;
  }
  
}
