/*****************************************************************************
 * 
 * @(#)GenAlphabetList.java  2009/11
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
package org.jin.dic.data.pub.ldoce.v5;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jin.dic.data.pub.CommonConstants;
import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class GenAlphabetList {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    String tableCfgName = args[0];
    String outFileName = args[1];
    File file = new File(outFileName);
    OutputStream os = null;
    os = new BufferedOutputStream(new FileOutputStream(file));

    Table table;
    table = new Table();
    table.setConfigFileName(tableCfgName);
    table.bind();
    if(!table.isValid()){
      Logger.info("Error: " + table.getCfgFileName());
    }else{
      int from = 0;
      int to = table.getRecordCount() - 1;
      IRecord record;
      String arl;
      String label;
      String[] rslt;
      os.write(0xff);
      os.write(0xfe);
      for(int i = from; i <= to; i++){
        record = table.getRecord(i);
        arl = StringUtil.valueOf(BytesUtil.trimData(record.getDataFieldValue("ARL")), "utf-8");
        label = StringUtil.valueOf(BytesUtil.trimData(record.getDataFieldValue("LABEL")), "utf-8");
        rslt = getP(label);
        os.write(StringUtil.getBytesNoBom(arl, CommonConstants.ENCODING));
        os.write(CommonConstants.SEPARATORBYTES);
        os.write(StringUtil.getBytesNoBom(rslt[0], CommonConstants.ENCODING));
        os.write(CommonConstants.SEPARATORBYTES);
        os.write(StringUtil.getBytesNoBom(rslt[1], CommonConstants.ENCODING));
        os.write(CommonConstants.SEPARATORBYTES);
        os.write(StringUtil.getBytesNoBom(rslt[2], CommonConstants.ENCODING));
        os.write(CommonConstants.SEPARATORBYTES);
        os.write(StringUtil.getBytesNoBom(String.valueOf(record.getNumFieldValue("ID")), CommonConstants.ENCODING));
        os.write(CommonConstants.LSBYTES);
      }
      os.close();
    }
    table.unBind(false);
  }
  static Pattern toc  = Pattern.compile("<span class=\"(\\w*)\">");
  static Pattern type = Pattern.compile("<span class=\"TOC1\"><DATA class=\"(\\w*)\">");
  static Pattern pos  = Pattern.compile("<DATA class=\"(?:keyword)?+pos\">(\\w*)</DATA>");
  static Pattern hom  = Pattern.compile("<DATA class=\"(?:keyword)?+hom\">(\\d*)</DATA>");

  private static String[] getP(String label){
    String[] rslt = new String[3];
    Matcher matcher;

    matcher = type.matcher(label);
    if(matcher.find()){
      rslt[0] = String.valueOf(type2Int(matcher.group(1)));
    }else{
      Logger.info(label);
    }

    matcher = pos.matcher(label);
    if(matcher.find()){
      rslt[1] = String.valueOf(pos2Int(matcher.group(1)));
    }else{
      rslt[1] = "0";
    }

    matcher = hom.matcher(label);
    if(matcher.find()){
      rslt[2] = matcher.group(1);
    }else{
      rslt[2] = "0";
    }
    return rslt;
  }
  private static int type2Int(String type){
    if(type.equals("hwd")){
      return 1;
    }else if(type.equals("keywordhwd")){
      return 3;
    }else{
      Logger.info(type);
      return 0;
    }
  }
  private static int pos2Int(String pos){
    if(pos.equals("adjective")){
      return 334;
    }else if(pos.equals("adverb")){
      return 335;
    }else if(pos.equals("auxiliary verb")){
      return 336;
    }else if(pos.equals("conjunction")){
      return 337;
    }else if(pos.equals("determiner")){
      return 338;
    }else if(pos.equals("interjection")){
      return 339;
    }else if(pos.equals("modal verb")){
      return 340;
    }else if(pos.equals("noun")){
      return 341;
    }else if(pos.equals("number")){
      return 342;
    }else if(pos.equals("phrasal verb")){
      return 343;
    }else if(pos.equals("predeterminer")){
      return 344;
    }else if(pos.equals("prefix")){
      return 345;
    }else if(pos.equals("preposition")){
      return 346;
    }else if(pos.equals("pronoun")){
      return 347;
    }else if(pos.equals("suffix")){
      return 348;
    }else if(pos.equals("verb")){
      return 349;
    }else{
      Logger.info(pos);
      return 0;
    }
  }

}
