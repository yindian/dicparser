/*****************************************************************************
 * 
 * @(#)IRecordSet.java  2009/03
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

public interface IRecordSet {

  public IFieldCollection getFldCollection();

  public int getRecordCount();
  public IRecord getRecord(int index);
  public IRecordSet getSubRecordSet(int off, int count);

  public IField getLookUpfield();
  public void setLookUpfield(IField lookupField);
  public int lookUpText(String sSearch, LookUpMode mode);
  public int lookUpNum(long nSearch, LookUpMode mode);

  /**
   * in general, to get IRecords from IRecordSet it should be bind to a
   * IDataSource first
   * 
   * @see IDataSource
   */
  public void bind();

  /**
   * @return whether IRecordSet is ready for getting IRecords
   */
  public boolean isValid();

  /**
   * break the bind to IDataSource
   * 
   * @see IDataSource
   * @param keepDataAvailable if <code>true</code> IRecordSet will be still
   * valid, but with only IRecords whose index range from 0 to
   * <code>MAXCACHESIZE - 1</code> in cache, and those IRecords are still
   * available to get methods. if <code>false</code> IRecordSet will be
   * invalid
   */
  public void unBind(boolean keepDataAvailable);

}
