package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jin.util.StringUtil;
import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._ByteArrayOutputStream;

public class ay_cnvrt2Html {

  static int count;
  public static void main(String[] args) throws Exception{
    String srcFld = "D:/Jin/Alpha/alpha/entry[fs]2";
    String desFld = "D:/Jin/Alpha/alpha/entry[fs]3";

    BufferedReader br = null;
    FileInputStream fis = null;
    for(int i = 46000; i < 46295; i++){
      try{
        count = i;
        fis = new FileInputStream(getFile(srcFld, i));
        br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
        FileOutputStream fos = new FileOutputStream(getFile(desFld, i));
        fos.write(0xff);
        fos.write(0xfe);
        fos.write(convert(br.readLine()));
        fos.close();
      }catch(FileNotFoundException e){
        e.printStackTrace();
      }catch(UnsupportedEncodingException e){
        e.printStackTrace();
      }catch(IOException e){
        e.printStackTrace();
      }finally{
        try{
          fis.close();
        }catch(IOException e){
          e.printStackTrace();
        }
      }
      if(i % 100 == 0) System.out.println(i);
    }
  }
  private static File getFile(String fld, int num) throws IOException{
    File folder;
    StringBuffer a = new StringBuffer();
    a.append(fld);
    a.append("/");
    int mark;

    mark = a.length();
    a.append(num / 10000);
    while(a.length() - mark < 5)
      a.insert(mark, "0");
    folder = new File(a.toString());
    if(!folder.exists()) folder.mkdir();
    a.append("/");

    mark = a.length();
    a.append(num / 100);
    while(a.length() - mark < 7)
      a.insert(mark, "0");
    folder = new File(a.toString());
    if(!folder.exists()) folder.mkdir();
    a.append("/");

    mark = a.length();
    a.append(num);
    while(a.length() - mark < 9)
      a.insert(mark, "0");

    a.append(".xml");
    File file = new File(a.toString());
    file.createNewFile();
    return file;
  }

  static Pattern rmES         = Pattern.compile("<SE_EntryAssets[^>]*>.*?</SE_EntryAssets>");
  static Pattern rmDummyTag   = Pattern.compile("<[^>]*/>");
  static Pattern rmSpaceTag   = Pattern.compile("<span>\\s*</span>");
  static Pattern rmHeading    = Pattern.compile("(?<=(?:THESAURUS|COLLOCATIONS)</span>)<HEADING>.*?</HEADING>");
  static Pattern rplStartTag  = Pattern
                                  .compile("<((?!span)\\w*)(?:\\s*)(?:id\\s*=\\s*\"\\w*\")?(?:\\s*)(?:class\\s*=\\s*\"(\\w*)\")?(?:\\s*)(?:style\\s*=\\s*\"(\\w*)\")?(?:type\\s*=\\s*\"(\\w*)\")?(?:[^/>]*)>");
  static Pattern rplEndTag    = Pattern.compile("</[^>]*>");
  static Pattern rplSubSenNum = Pattern.compile("(?<=<Subsense><span class=\")sensenum");
  static Pattern rplSpan      = Pattern.compile("(<span[^>]*>)");
  static Pattern rplSpanEnd   = Pattern.compile("(</span>)");

  static Pattern prTID        = Pattern.compile("(?<=topic\\s{0,3}?=\\s{0,3}?\")(p\\w{3}-\\w{9})");

  private static byte[] convert(String data) throws DocumentException, IOException{
    data = rmES.matcher(data).replaceAll("");
    data = rmDummyTag.matcher(data).replaceAll("");
    data = rmHeading.matcher(data).replaceAll("");
    data = rplSubSenNum.matcher(data).replaceAll("Subsensenum");

    data = rplEndTag.matcher(data).replaceAll("</span>");
    data = rplStartTag.matcher(data).replaceAll("<span class=\"$1$2$3\">");

    data = data.replaceAll("\\|", ",");
   // data = "<JX><![CDATA[<head><link href=\"file:///D:/Jin/Work/WorkSpace/Dictionary/alpha/ldoce5.css\" type=\"text/css\" rel=\"stylesheet\"></head>]]></JX>"
     //   + data;
//    data = rplSpan.matcher(data).replaceAll("<JX><![CDATA[$1");
//    data = rplSpanEnd.matcher(data).replaceAll("$1]]></JX>\r\n");
    return StringUtil.getBytesNoBom(data, "unicode");
  }

}
