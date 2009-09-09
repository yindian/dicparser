/*****************************************************************************
 * 
 * @(#)IField.java  2009/03
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


public interface IField extends IConfigable {

  public FieldType getType();
  public void setType(FieldType t);
  
  /**
   * 
   * @return bytes needed to store this field 
   */
  public int getSize();
  
  public String getName();
  public void setName(String name);
  public boolean isNum();
  public boolean isUNum();
  public boolean isSNum();
  public boolean isData();
  public boolean isLink();

  public void setOffset(int offset);
  public int getOffset();
  
  /**
   * @return position(index)in field collection
   */
  public int getPosition();
  public void setPosition(int position);

  /**
   * used when field type is data or link
   */
  public IField getSubCountField();
  public IField getSubOffsetField();
  public IField getSubPackField();
  
  public int getDataSourceCount();
  public IDataSource getDataSource(int index);
  
  /**
   * a field may contain a sub table(IRecordSet)
   * @return
   */
  public IRecordSet getLinkRecordSet();

}
