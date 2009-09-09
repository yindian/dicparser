/*****************************************************************************
 * 
 * @(#)FieldType.java  2009/03
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
package com.jin.dic.sk.i;

public class FieldType {

  public static final int SKFT_UNKNOWN = 0;
  public static final int SKFT_LINK    = 0x08;
  public static final int SKFT_DATA    = 0x09;
  public static final int SKFT_SBYTE   = 0x10;
  public static final int SKFT_SSHORT  = 0x11;
  public static final int SKFT_SLONG   = 0x12;
  public static final int SKFT_S24     = 0x13;
  public static final int SKFT_UBYTE   = 0x18;
  public static final int SKFT_USHORT  = 0x19;
  public static final int SKFT_ULONG   = 0x20;
  public static final int SKFT_U24     = 0x21;

  private int             intValue;
  private int             size;

  public FieldType(String name) {
    if(name.equals("LINK")) intValue = SKFT_LINK;
    else if(name.equals("DATA")) intValue = SKFT_DATA;
    else if(name.equals("SBYTE")) intValue = SKFT_SBYTE;
    else if(name.equals("SSHORT")) intValue = SKFT_SSHORT;
    else if(name.equals("SLONG")) intValue = SKFT_SLONG;
    else if(name.equals("S24")) intValue = SKFT_S24;
    else if(name.equals("UBYTE")) intValue = SKFT_UBYTE;
    else if(name.equals("USHORT")) intValue = SKFT_USHORT;
    else if(name.equals("ULONG")) intValue = SKFT_ULONG;
    else if(name.equals("U24")) intValue = SKFT_U24;
    else intValue = SKFT_UNKNOWN;

    computeSize();
  }

  private void computeSize(){
    if(intValue == FieldType.SKFT_UNKNOWN) size = 0;
    else if(intValue == FieldType.SKFT_ULONG || intValue == FieldType.SKFT_SLONG) size = 4;
    else if(intValue == FieldType.SKFT_U24 || intValue == FieldType.SKFT_S24) size = 3;
    else if(intValue == FieldType.SKFT_USHORT || intValue == FieldType.SKFT_SSHORT) size = 2;
    else if(intValue == FieldType.SKFT_UBYTE || intValue == FieldType.SKFT_SBYTE) size = 1;
    else if(intValue == FieldType.SKFT_LINK) size = 0;
    else if(intValue == FieldType.SKFT_DATA) size = 0;
  }

  public int getIntValue(){
    return intValue;
  }

  public int getSize(){
    return size;
  }

  public boolean isSigned(){
    return((intValue >= SKFT_SBYTE) && (intValue < SKFT_UBYTE));
  }

  public boolean isUnSigned(){
    return((intValue >= SKFT_UBYTE) && (intValue <= SKFT_U24));
  }

  public boolean isNumber(){
    return((intValue >= SKFT_SBYTE) && (intValue <= SKFT_U24));
  }

  public boolean isComposite(){
    return((intValue == SKFT_LINK) || (intValue == SKFT_DATA));
  }
  public boolean isData(){
    return intValue == SKFT_DATA;
  }
  public boolean isLink(){
    return intValue == SKFT_LINK;
  }

  public String toString(){
    String s;
    if(intValue == SKFT_LINK) s = "LINK";
    else if(intValue == SKFT_DATA) s = "DATA";
    else if(intValue == SKFT_SBYTE) s = "SBYTE";
    else if(intValue == SKFT_SSHORT) s = "SSHORT";
    else if(intValue == SKFT_SLONG) s = "SLONG";
    else if(intValue == SKFT_S24) s = "S24";
    else if(intValue == SKFT_UBYTE) s = "UBYTE";
    else if(intValue == SKFT_USHORT) s = "USHORT";
    else if(intValue == SKFT_ULONG) s = "ULONG";
    else if(intValue == SKFT_U24) s = "U24";
    else s = "SKFT_UNKNOWN";
    return s;
  }

}
