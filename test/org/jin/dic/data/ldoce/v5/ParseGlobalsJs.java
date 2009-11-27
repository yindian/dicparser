package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jin.util.Logger;

public class ParseGlobalsJs {
  static Pattern getText = Pattern.compile("\"[^\"]*\" : \"?([^\"]*)\"?");

  Matcher        matcher = null;
  BufferedReader br      = null;
  String         line    = null;
  String         text    = null;
  public void doParse(File globaljs) throws FileNotFoundException{
    try{
      br = new BufferedReader(new InputStreamReader(new FileInputStream(globaljs)));
      while((line = br.readLine()) != null){
        if(line.indexOf("\"label\"") != -1){
          matcher = getText.matcher(line);
          if(matcher.find()) text = matcher.group(1);
          System.out.print(text);
        }
        if(line.indexOf("\"type\"") != -1){
          System.out.println();
          System.out.println();
        }
        if(line.indexOf("\"code\"") != -1){
          matcher = getText.matcher(line);
          if(matcher.find()) text = matcher.group(1);
          System.out.println("\t" + text);
        }
      }
    }catch(IOException e){
      e.printStackTrace();
    }finally{
      if(br != null) try{
        br.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
  }
  public static void main(String[] arg) throws FileNotFoundException{
    ParseGlobalsJs a = new ParseGlobalsJs();
    a.doParse(new File("D:/Jin/Work/WorkSpace/Dictionary/globals.js"));
  }
}
