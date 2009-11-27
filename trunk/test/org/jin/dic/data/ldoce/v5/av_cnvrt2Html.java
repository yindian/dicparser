package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._ByteArrayOutputStream;

public class av_cnvrt2Html {

  static int count;
  public static void main(String[] args) throws Exception{
    String srcFld = "D:/Jin/Alpha/alpha/entry[fs]3";
    String desFld = "D:/Jin/Alpha/alpha/entry[fs]5";

    BufferedReader br = null;
    FileInputStream fis = null;
    for(int i = 46000; i < 46290; i++){
      try{
        count = i;
        fis = new FileInputStream(getFile(srcFld, i));
        br = new BufferedReader(new InputStreamReader(fis, "unicode"));
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

  static Pattern rmGUID  = Pattern.compile("(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern rmDummy = Pattern.compile("<[^>\\s]*\\s*/>");
  static Pattern prTID   = Pattern.compile("(?<=topic\\s{0,3}?=\\s{0,3}?\")(p\\w{3}-\\w{9})");

  private static byte[] convert(String data) throws DocumentException, IOException{
    data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + data;
    _ByteArrayOutputStream bos = new _ByteArrayOutputStream();
    _ByteArrayInputStream bis = new _ByteArrayInputStream(data.getBytes("utf-8"));
    SAXReader saxR = null;
    Document doc = null;
    Element root = null;
    XMLWriter xmlWriter = null;
    OutputFormat fmt = null;

    saxR = new SAXReader();
    doc = saxR.read(bis);
    root = doc.getRootElement();

    fmt = OutputFormat.createPrettyPrint();
    fmt.setEncoding("utf-16le");
    xmlWriter = new XMLWriter(bos, fmt);
    xmlWriter.write(doc);
    xmlWriter.close();

    return bos.toByteArray();
  }

}
