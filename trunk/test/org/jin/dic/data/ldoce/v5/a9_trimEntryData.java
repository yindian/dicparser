package org.jin.dic.data.ldoce.v5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jin.util.StringUtil;
import org.jin.util.io._RandomAccessFile;

public class a9_trimEntryData {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception{
    String srcFld = "D:/Jin/Alpha/ldoce5.data/entry[fs]";
    String desFld = "D:/Jin/Alpha/alpha/entry[fs]";

    String outFileName = "alpha/p2num_list.txt";

    getMap();

    BufferedReader br = null;
    FileInputStream fis = null;
    for(int i = 0; i < 51604; i++){
      try{
        if(i % 100 == 0){
          System.out.println(i);
          saveMap(outFileName);
          p2num.clear();
        }
        fis = new FileInputStream(getFile(srcFld, i));
        br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
        FileOutputStream fos = new FileOutputStream(getFile(desFld, i));
        fos.write(trim(br.readLine()).getBytes("utf-8"));
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
    }
    saveMap(outFileName);
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

  static Pattern mvIdm_tp = Pattern.compile("\\A<Entry\\s*(id=\"-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5}\")\\s*(type=\"encyc\")");
  static Pattern rmIdm_id = Pattern.compile("(?<=\\A<Entry .{0,20})id=\"-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5}\" idm_id=\"");
  static Pattern rmP_P    = Pattern.compile("<EntryAssets type=\"pronunciation\"[^>]*>.*?</EntryAssets>");
  static Pattern rmU_N    = Pattern.compile("<EntryAssets type=\"usernote\"[^>]*>.*?</EntryAssets>");
  static Pattern rmFilter = Pattern.compile("\\s*as_filter=\"[^\"]*\"");
  static Pattern rmIfx    = Pattern.compile("<INFLX>\\w*?</INFLX>");
  static Pattern rmFreq   = Pattern.compile("(?<=<FREQ )resource=\"comm3000\"");
  static Pattern rmDummy  = Pattern.compile("<[^>\\s]*\\s*/>");

  static Pattern ep_id    = Pattern.compile("p\\d{3}_\\d{3}/p\\d{3}_\\d{5}/p\\d{3}-(\\d{9})\\.mp3");
  static Pattern ref_id   = Pattern.compile("(?<=<Ref topic=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");

  static Pattern s_id     = Pattern.compile("(?<=<Sense id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern e_id     = Pattern.compile("(?<=<EXAMPLE id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern pv_id    = Pattern.compile("(?<=<PhrVbEntry id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern pvh_id   = Pattern.compile("(?<=<PHRVBHWD id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern lex_id   = Pattern.compile("(?<=<LEXVAR id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern clo_id   = Pattern.compile("(?<=<COLLO id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern pf_id    = Pattern.compile("(?<=<PROPFORM id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern lu_id    = Pattern.compile("(?<=<LEXUNIT id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern exp_id   = Pattern.compile("(?<=<Exponent id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern clt_id   = Pattern.compile("(?<=<Collocate id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern clc_id   = Pattern.compile("(?<=<COLLOC id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern pfr_id   = Pattern.compile("(?<=<PROPFORMPREP id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern ovr_id   = Pattern.compile("(?<=<ORTHVAR id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");
  static Pattern deriv_id = Pattern.compile("(?<=<DERIV id=\")(-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5})");

  static Pattern rmGUID   = Pattern.compile("-?\\w{15,19}\\.-?\\w{6,10}\\.-?\\w{9,13}\\.-?\\w{2,5}");

  private static String trim(String data){
    data = mvIdm_tp.matcher(data).replaceAll("<Entry $2 $1");
    data = rmIdm_id.matcher(data).replaceAll("eId=\"");
    data = ep_id.matcher(data).replaceAll("$1");
    data = rmP_P.matcher(data).replaceAll("");
    data = rmU_N.matcher(data).replaceAll("");
    data = rmFilter.matcher(data).replaceAll("");
    data = rmIfx.matcher(data).replaceAll("");
    data = rmFreq.matcher(data).replaceAll("");
    data = rmDummy.matcher(data).replaceAll("");

    int count = 0;
    int value;
    Matcher matcher;

    matcher = ref_id.matcher(data);
    while(matcher.find()){
      data = matcher.replaceFirst((String) p2eId.get(matcher.group(1)));
      matcher = ref_id.matcher(data);
    }

    matcher = s_id.matcher(data);
    while(matcher.find()){
      value = (0x01000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = s_id.matcher(data);
    }

    matcher = e_id.matcher(data);
    while(matcher.find()){
      value = (0x02000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = e_id.matcher(data);
    }

    matcher = pv_id.matcher(data);
    while(matcher.find()){
      value = (0x03000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = pv_id.matcher(data);
    }

    matcher = pvh_id.matcher(data);
    while(matcher.find()){
      value = (0x04000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = pvh_id.matcher(data);
    }

    matcher = lex_id.matcher(data);
    while(matcher.find()){
      value = (0x05000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = lex_id.matcher(data);
    }

    matcher = clo_id.matcher(data);
    while(matcher.find()){
      value = (0x06000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = clo_id.matcher(data);
    }

    matcher = pf_id.matcher(data);
    while(matcher.find()){
      value = (0x07000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = pf_id.matcher(data);
    }

    matcher = lu_id.matcher(data);
    while(matcher.find()){
      value = (0x08000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = lu_id.matcher(data);
    }

    matcher = exp_id.matcher(data);
    while(matcher.find()){
      value = (0x09000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = exp_id.matcher(data);
    }

    matcher = clt_id.matcher(data);
    while(matcher.find()){
      value = (0x0a000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = clt_id.matcher(data);
    }

    matcher = clc_id.matcher(data);
    while(matcher.find()){
      value = (0x0b000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = clc_id.matcher(data);
    }

    matcher = pfr_id.matcher(data);
    while(matcher.find()){
      value = (0x0c000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = pfr_id.matcher(data);
    }

    matcher = ovr_id.matcher(data);
    while(matcher.find()){
      value = (0x0d000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = ovr_id.matcher(data);
    }

    matcher = deriv_id.matcher(data);
    while(matcher.find()){
      value = (0x0e000000) | (0x00ffffff & count++);
      p2num.put(matcher.group(1), String.valueOf(value));
      data = matcher.replaceFirst(String.valueOf(value));
      matcher = deriv_id.matcher(data);
    }

    return data;
  }

  static Map p2num = new HashMap();
  private static void saveMap(String outFileName) throws Exception{
    _RandomAccessFile os = new _RandomAccessFile(outFileName, "rw");
    os.seek(os.length());
    Set es = p2num.entrySet();
    Iterator i = es.iterator();
    if(os.length() == 0){
      os.write(0xff);
      os.write(0xfe);
    }
    while(i.hasNext()){
      Entry e = (Entry) i.next();
      os.write(StringUtil.getBytesNoBom((String) e.getKey(), "unicode"));
      os.write(StringUtil.getBytesNoBom("\t", "unicode"));
      os.write(StringUtil.getBytesNoBom((String) e.getValue(), "unicode"));
      os.write(StringUtil.getBytesNoBom("\r\n", "unicode"));
      e.getKey();
    }
    os.close();
  }
  static Map p2eId = new HashMap();
  private static void getMap() throws Exception{
    BufferedReader br = null;
    try{
      FileInputStream fis = new FileInputStream("alpha/publisher_eId_list.txt");
      br = new BufferedReader(new InputStreamReader(fis, "unicode"));
      String line;
      String[] info;
      while((line = br.readLine()) != null){
        info = line.split("\t");
        if(info == null || info.length != 2){
          continue;
        }
        p2eId.put(info[0], info[1]);
      }
    }finally{
      if(br != null) br.close();
    }
  }

}
