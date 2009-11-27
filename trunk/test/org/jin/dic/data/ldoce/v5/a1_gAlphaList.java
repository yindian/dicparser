package org.jin.dic.data.ldoce.v5;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jin.dic.data.sk.Table;
import org.jin.dic.data.sk.i.IRecord;
import org.jin.util.BytesUtil;
import org.jin.util.Logger;
import org.jin.util.StringUtil;

public class a1_gAlphaList {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    String encoding = "unicode";
    String tableCfgName = "D:/Program Files/Longman/LDOCE5/ldoce5.data/fs.skn/alpha_index.skn";
    String outFileName = "alpha/alpha_list.txt";
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
        os.write(StringUtil.getBytesNoBom(arl, encoding));
        os.write(StringUtil.getBytesNoBom("\t", encoding));
        os.write(StringUtil.getBytesNoBom(rslt[0], encoding));
        os.write(StringUtil.getBytesNoBom("\t", encoding));
        os.write(StringUtil.getBytesNoBom(rslt[1], encoding));
        os.write(StringUtil.getBytesNoBom("\t", encoding));
        os.write(StringUtil.getBytesNoBom(rslt[2], encoding));
        os.write(StringUtil.getBytesNoBom("\t", encoding));
        os.write(StringUtil.getBytesNoBom(String.valueOf(record.getNumFieldValue("ID")), encoding));
        os.write(StringUtil.getBytesNoBom("\r\n", encoding));
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
      System.out.println(label);
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
      System.out.println(type);
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
      System.out.println(pos);
      return 0;
    }
  }
  
}
