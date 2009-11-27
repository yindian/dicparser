/*****************************************************************************
 * 
 * @(#)CommonConstants.java  2009/11
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

import org.jin.util.StringUtil;

public class CommonConstants {

  public static final String ENCODING       = "unicode";
  public static final String LS             = "\r\n";
  public static final String SEPARATOR      = "\t";
  public static final String NEWLINE        = "<br>";
  public static final String HBAR           = "<hr>";
  public static final String KSDICB         = "<CK><JX><![CDATA[";
  public static final String KSDICE         = "]]></JX></CK>";

  public static final byte[] LSBYTES        = StringUtil.getBytesNoBom(LS, ENCODING);
  public static final byte[] SEPARATORBYTES = StringUtil.getBytesNoBom(SEPARATOR, ENCODING);
  public static final byte[] NEWLINEBYTES   = StringUtil.getBytesNoBom(NEWLINE, ENCODING);
  public static final byte[] HBARBYTES      = StringUtil.getBytesNoBom(HBAR, ENCODING);
  public static final byte[] KSDICBBYTES    = StringUtil.getBytesNoBom(KSDICB, ENCODING);
  public static final byte[] KSDICEBYTES    = StringUtil.getBytesNoBom(KSDICE, ENCODING);

}
