/*****************************************************************************
 * 
 * @(#)Converter.java  2009/03
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

import java.text.DecimalFormat;

import com.jin.dic.ConvertListener;
import com.jin.util.Logger;
import com.jin.util.TimeUtil;

public class Converter implements ConvertListener {

  int           count     = 0;
  DecimalFormat df_p      = new DecimalFormat("00%");
  String        last      = "";
  int           cStage    = 0;
  TimeUtil      time      = new TimeUtil();
  long          beginTime = 0;
  
  public void update(int stages, int curStage, int total, int processed, String info){
    if(beginTime == 0) beginTime = System.currentTimeMillis();
    String percentage = df_p.format(Math.floor(((float) processed * 100 / total)) / 100);
    StringBuffer sb;
    if(!last.equalsIgnoreCase(percentage)){
      last = percentage;
      sb = new StringBuffer();
      if(cStage != curStage){
        sb.append("[");
        sb.append(curStage);
        sb.append("/");
        sb.append(stages);
        sb.append("] ");
        sb.append(info);
        sb.append(" ");

        if(curStage != 1) Logger.info("");
        cStage = curStage;
      }else{
        sb.append(" ");
      }
      sb.append(percentage);
      Logger.info_(sb.toString());
    }
    if(stages == curStage && total == processed){
      Logger.info("");
      Logger.info_("Time used: ");
      Logger.info_(""+(System.currentTimeMillis() - beginTime) / (float) 1000);
      Logger.info("s");
    }
  }

}
