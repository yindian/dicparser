/*****************************************************************************
 * 
 * @(#)Logger.java  2009/03
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

import java.io.PrintStream;

public class Logger {

  private static PrintStream ps         = System.out;
  public static boolean      printStack = false;

  public static void err(Exception e){
    if(printStack){
      // StackTraceElement trace = e.getStackTrace()[0];
      // ps.print(":> ");
      // ps.print(e.getMessage());
      // ps.print(" - ");
      // ps.println(trace);
      e.printStackTrace(ps);
    }else ps.println(e.getMessage());
  }

  public static void err(String e){
    ps.println(e);
  }

  public static void info(){
    ps.println();
  }

  public static void info(long l){
    ps.println(String.valueOf(l));
  }

  public static void info(int i){
    ps.println(String.valueOf(i));
  }

  public static void info(String s){
    ps.println(s);
  }

  public static void info_(String s){
    ps.print(s);
  }

  public static void info_(char c){
    ps.print(c);
  }

}
