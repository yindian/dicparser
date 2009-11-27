/*****************************************************************************
 * 
 * @(#)TimeUtil.java  2009/03
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
package org.jin.util;

import java.util.ArrayList;
import java.util.List;

/**
 * get average time of the task
 * 
 * @author jinxingquan
 */
public class TimeUtil {

  private static int depth    = Integer.valueOf("128").intValue();

  private long       lastTime = 0;

  private List       queue    = new ArrayList(depth);

  /**
   * begin
   */
  public void begin(){
    lastTime = System.currentTimeMillis();
  }

  /**
   * end
   */
  public void end(){
    while(queue.size() >= depth){
      queue.remove(queue.size() - 1);
    }
    queue.add(0, new Long(System.currentTimeMillis() - lastTime));
  }

  /**
   * @return the averageTime
   */
  public long getAverageTime(){
    long totalTime = 0;
    int count = 0;
    Long phrase = null;
    int size = queue.size();
    for(int i = 0; i < size; i++){
      phrase = (Long) queue.get(i);
      if(phrase != null){
        count++;
        totalTime += phrase.longValue();
      }
    }
    return count != 0 ? totalTime / count : 0;
  }
}
