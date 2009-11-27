/*****************************************************************************
 * 
 * @(#)ConverterThread.java  2009/03
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
package org.jin.dic.data.pub;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jin.dic.data.ConvertListener;
import org.jin.dic.data.Engine;
import org.jin.util.Logger;


public class ConverterThread extends Thread {

  ConvertListener listener;
  Engine          engine = null;
  String          desFld = null;
  
  public ConverterThread(ConvertListener listener) {
    this.listener = listener;
  }
  
  public void setEngine(String engineName, String srcFld){
    try{
      Class c = Thread.currentThread().getContextClassLoader().loadClass(engineName);
      Constructor con = c.getConstructor(new Class[] { String.class });
      if(con == null) engine = (Engine) c.newInstance();
      else engine = (Engine) con.newInstance(new Object[] { (Object) srcFld });
    }catch(InstantiationException e){
      Logger.err(e);
    }catch(IllegalAccessException e){
      Logger.err(e);
    }catch(ClassNotFoundException e){
      Logger.err(e);
    }catch(SecurityException e){
      Logger.err(e);
    }catch(NoSuchMethodException e){
      Logger.err(e);
    }catch(IllegalArgumentException e){
      Logger.err(e);
    }catch(InvocationTargetException e){
      Logger.err(e);
    }
  }
  public void setEngine(Engine engine){
    this.engine = engine;
  }
  public void setDesFld(String desFld){
    this.desFld = desFld;
  }
  public void run(){
    if(engine == null || desFld == null) return;
    try{
      engine.addConverListener(listener);
      engine.convert(desFld);
    }catch(IOException e){
      Logger.info(e.getMessage());
    }
  }

}
