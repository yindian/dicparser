package org.jin.dic.data.webster.c11.v3_0;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jin.util.StringUtil;
import org.jin.util.io._RandomAccessFile;

public class ExtractData {
  public static void main(String[] args) throws IOException{

    String searchKeyWord = "convert";// //type:0xbdÅ
    // String searchKeyWord = "eye";// //variant: (dummy)
    // String searchKeyWord = "a";// //type:0xbd
    // String searchKeyWord = "caecal";// //type:0xf0,0x84
    // String searchKeyWord = "A horizon";// //type:0x83
    // String searchKeyWord = "abide";////type:0x8f
    // String searchKeyWord = "tetherball";////TODO bad value:0x1995
    // String searchKeyWord = "tetherball";//value:0x198c
    // String searchKeyWord = "Tamil";//ẓ
    // String searchKeyWord = "befuddle";//type:0xc5; value:0x19ab
    // String searchKeyWord = "glucose-1-phosphate";//<sub></sub>
    // String searchKeyWord = "aboriginal";//type:0xbe
    boolean found = false;
    Parse parse = new Parse();
    InputStream in = new BufferedInputStream(new FileInputStream("C11/C11.inflate.cmp"));
    _RandomAccessFile html;
    _RandomAccessFile debug;
    String encoding = "unicode";
    // in.skip(0x8c22de);
    // in.skip(0x16170f8);
    StringBuffer entry[] = null;
    String fileName;
    int count = 0;
    while(in.available() > 0){
      try{
        entry = parse.getEntry(in);
        // if(true)continue;
      }catch(RuntimeException e){
        System.out.println(parse.getKeyWord());
        System.out.println(e);
        break;
      }
      if((parse.getKeyWord() != null && parse.getKeyWord().indexOf(searchKeyWord) == 0)
          || (parse.getAscKeyWord() != null && parse.getAscKeyWord().indexOf(searchKeyWord) == 0)){
        found = true;
        fileName = parse.getAscKeyWord();
        fileName = fileName.replaceAll("/|\\?|:", "_");

        html = new _RandomAccessFile("C11/temp/" + fileName + ".html", "rw");
        if(html.length() > 0){
          html.skipBytes((int) html.length());
          html.write(StringUtil.getBytesNoBom(Parse.newLine, encoding));
          html.write(StringUtil.getBytesNoBom(Parse.newLine, encoding));
          html.write(StringUtil.getBytesNoBom("#############", encoding));
          html.write(StringUtil.getBytesNoBom(Parse.newLine, encoding));
        }
        html.write(0xff);
        html.write(0xfe);
        html.write(StringUtil.getBytesNoBom(entry[0].toString(), encoding));
        html.close();

        debug = new _RandomAccessFile("C11/temp/" + fileName + ".txt", "rw");
        if(debug.length() > 0){
          debug.skipBytes((int) debug.length());
          debug.write(StringUtil.getBytesNoBom("\r\n\r\n#############", encoding));
        }
        debug.write(0xff);
        debug.write(0xfe);
        debug.write(StringUtil.getBytesNoBom(entry[2].toString(), encoding));
        debug.close();
        System.out.println(entry[0]);
        System.out.println(entry[1]);
        System.out.println(entry[2]);
      }else if(found) break;
      else System.out.println(count++);
    }
  }
}
