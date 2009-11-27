package org.jin.dic.data.sk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Formater {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    // TODO Auto-generated method stub
    Document doc = null;

    SAXReader saxR = null;
    saxR = new SAXReader();
    saxR.setEncoding("utf-8");
    doc = saxR.read(new File("000004773.xml"));

    OutputFormat fmt = OutputFormat.createPrettyPrint();
    fmt.setEncoding("utf-8");
    XMLWriter xmlWriter = new XMLWriter(new FileOutputStream("a.xml"), fmt);
    xmlWriter.write(doc);
    xmlWriter.close();
  }

}
