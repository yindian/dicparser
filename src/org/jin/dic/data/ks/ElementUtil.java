/*****************************************************************************
 * 
 * @(#)ElementUtil.java  2009/03
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
package org.jin.dic.data.ks;

import java.io.IOException;

import org.jin.util.io._ByteArrayOutputStream;
import org.jin.util.io._DataOutputStream;


public class ElementUtil {

  static public byte[] getBytes(Element element) throws IOException{
    _ByteArrayOutputStream baos = new _ByteArrayOutputStream();
    _DataOutputStream ledos = new _DataOutputStream(baos,true);
    element.write(ledos);
    ledos.close();
    return baos.toByteArray();
  }

}
