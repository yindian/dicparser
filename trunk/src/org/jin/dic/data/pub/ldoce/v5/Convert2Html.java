/*****************************************************************************
 * 
 * @(#)Convert2Html.java  2009/11
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jin.util.Logger;
import org.jin.util.io._ByteArrayInputStream;
import org.jin.util.io._ByteArrayOutputStream;

public class Convert2Html {

  static int count;
  public static void main(String[] args) throws Exception{
    String srcFld = args[0];
    String desFld = args[1];

    getMap(args[2]);
    byte[] data;
    BufferedReader br = null;
    FileInputStream fis = null;
    for(int i = 0; i < 51604; i++){
      try{
        count = i;
        fis = new FileInputStream(Common.getFile(srcFld, i));
        br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
        FileOutputStream fos = new FileOutputStream(Common.getFile(desFld, i));
        fos.write(0xff);
        fos.write(0xfe);
        data = convert(br.readLine());
        fos.write(data, 84, data.length - 84);
        fos.close();
        // convert(br.readLine());
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
      if(i % 100 == 0) Logger.info(i);
    }
    // saveMap();
  }
  static Pattern rmES       = Pattern.compile("<SE_EntryAssets[^>]*>.*?</SE_EntryAssets>");
  static Pattern rmDummyTag = Pattern.compile("<[^>]*/>");
  private static byte[] convert(String data) throws DocumentException, IOException{
    data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + rmES.matcher(rmDummyTag.matcher(data).replaceAll("")).replaceAll("");
    data = data.replaceAll("\\|", ",");
    _ByteArrayOutputStream bos = new _ByteArrayOutputStream();
    _ByteArrayInputStream bis = new _ByteArrayInputStream(data.getBytes("utf-8"));
    SAXReader saxR = null;
    Document doc = null, des = null;
    Element root = null, desRoot = null;
    XMLWriter xmlWriter = null;
    OutputFormat fmt = null;
    saxR = new SAXReader();
    doc = saxR.read(bis);
    root = doc.getRootElement();

    des = DocumentHelper.createDocument();
    desRoot = DocumentHelper.createElement("span");
    desRoot.addAttribute("class", getClass(root));
    Element child;
    List children = root.elements();
    for(int i = 0; i < children.size(); i++){
      child = (Element) children.get(i);
      addChildren(child, desRoot);
    }
    des.setRootElement(desRoot);

    fmt = OutputFormat.createCompactFormat();
    fmt.setEncoding("utf-16le");
    fmt.setTrimText(false);
    xmlWriter = new XMLWriter(bos, fmt);
    xmlWriter.write(des);
    xmlWriter.close();
    return bos.toByteArray();
  }

  private static void addChildren(Element s, Element d){
    Element span = DocumentHelper.createElement("span");
    d.add(span);
    if(!s.getName().equalsIgnoreCase("base")){
      String c;
      if(s.getName().equals("span")){
        c = s.attributeValue("class");
        if(c == null || c.length() == 0){
          c = null;
        }else{
          c = getClass(s);
        }
      }else{
        c = getClass(s);
      }
      if(c != null && c.length() > 0) span.addAttribute("class", c);
    }
    Iterator i = s.nodeIterator();
    Node node;
    while(i.hasNext()){
      node = (Node) i.next();
      if(node instanceof Element) addChildren((Element) node, span);
      else span.add((Node) node.clone());
    }

  }
  // private static void addChildren(Element s, Element d){
  // Element span = DocumentHelper.createElement("span");
  // if(!s.getName().equalsIgnoreCase("base") && !s.getName().equalsIgnoreCase("span")){
  // if(getClass(s) != null) span.addAttribute("class", getClass(s));
  // }
  // if(s.getText() != null && s.getText().length() > 0) span.setText(s.getText());
  // d.add(span);
  // Element child;
  // List children = s.elements();
  // for(int i = 0; i < children.size(); i++){
  // child = (Element) children.get(i);
  // addChildren(child, span);
  // }
  // }

  static Map classNameMap = new LinkedHashMap();
  private static void getMap(String file) throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream(file);
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 2){
          continue;
        }
        classNameMap.put(info[0], info[1]);
      }
    }finally{
      if(br != null) br.close();
    }
  }
  // private static void saveMap() throws IOException{
  // String encoding = "unicode";
  // String outFileName = "alpha/className_list.txt";
  // File file = new File(outFileName);
  // OutputStream os = null;
  // os = new BufferedOutputStream(new FileOutputStream(file));
  // os.write(0xff);
  // os.write(0xfe);
  // Set s = classNameMap.entrySet();
  // Iterator i = s.iterator();
  // Entry entry;
  // while(i.hasNext()){
  // entry = (Entry) i.next();
  // os.write(StringUtil.getBytesNoBom((String) entry.getKey(), encoding));
  // os.write(StringUtil.getBytesNoBom("\t", encoding));
  // os.write(StringUtil.getBytesNoBom((String) entry.getValue(), encoding));
  // os.write(StringUtil.getBytesNoBom("\r\n", encoding));
  // }
  // os.close();
  // }

  private static String getClass(Element e){
    String name = getFullClassName(new StringBuffer(), e);
    // classNameMap.put(name, name);
    name = (String) classNameMap.get(name);
    if(name == null) Logger.info(getFullClassName(new StringBuffer(), e));
    return name;
  }
  private static String getFullClassName(StringBuffer s, Element e){
    if(e != null){
      if(s.length() > 0) s.insert(0, "_");
      s.insert(0, getElementStyleName(e));
      getFullClassName(s, e.getParent());
    }
    return s.toString();
  }
  private static String getElementStyleName(Element e){
    StringBuffer s = new StringBuffer();
    s.append(e.getName());
    if(e.attributeValue("class") != null){
      s.append("_");
      s.append(e.attributeValue("class"));
    }
    if(e.attributeValue("style") != null){
      s.append("_");
      s.append(e.attributeValue("style"));
    }
    if(e.attributeValue("type") != null){
      s.append("_");
      s.append(e.attributeValue("type"));
    }
    return s.toString();
  }
}
