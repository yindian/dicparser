/*****************************************************************************
 * 
 * @(#)Parse.java  2009/11
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

package org.jin.dic.data.webster.c11.v3_0;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jin.util.BytesUtil;
import org.jin.util.io.InputStreamUtil;

public class Parse {

  public static String space      = "&nbsp";
  public static String newLine    = "<br>";

  private String       keyWord    = null;   // unicode
  private String       ascKeyWord = null;   // ascii, keyWord = Å, ascKeyWord will be A
  private List         variants   = null;
  private int          lastType   = -1;
  private boolean      dummy      = false;

  public String getKeyWord(){
    return keyWord;
  }
  public String getAscKeyWord(){
    return ascKeyWord;
  }

  private static Pattern cap  = Pattern.compile("<cap>(.*?)</cap>");
  private static Pattern trim = Pattern.compile("^\\s*|\\s*$");
  private static String changeToCap(String data){
    String match = null;
    Matcher matcher = cap.matcher(data);
    while(matcher.find()){
      match = matcher.group(1);
      data = matcher.replaceFirst(match.toUpperCase());
      matcher = cap.matcher(data);
    }
    return data;
  }
  private static String trim(String data){
    return trim.matcher(data).replaceAll("");
  }

  public StringBuffer[] getEntry(InputStream in) throws IOException{
    StringBuffer[] entry = new StringBuffer[] { new StringBuffer(), new StringBuffer(), new StringBuffer() };
    int type, len;
    byte[] buf = new byte[2048];
    String value;
    if(variants != null) variants.clear();

    while(true){
      type = in.read();
      if(type == 0) break;
      len = in.read();
      if((len & 0x80) != 0) len = in.read() + (0x80 * (len & 0x7f));
      InputStreamUtil.readFully(in, buf, 0, len);

      value = getString(type, buf, len);

      dummy = false;

      addVariants(type, buf, len);
      appendLeading(entry[0], type, value);
      appendValue(entry[0], type, value);
      appendEnding(entry[0], type);

      entry[2].append(Integer.toHexString(type));
      entry[2].append("\t");
      entry[2].append(value);
      entry[2].append("\t");
      entry[2].append(BytesUtil.convert(buf, 0, len));
      entry[2].append("\r\n");

      if(!dummy) lastType = type;
    }
    if(variants != null){
      for(int i = 0; i < variants.size(); i++){
        if(i != 0) entry[1].append(";;");
        entry[1].append(variants.get(i));
      }
    }
    return entry;
  }

  private void addVariants(int type, byte[] buf, int len){
    switch(type){// derivatives_variants
      case 0x84:
      case 0xc8:
      case 0xd1:
      case 0xf0:
        add(getString(buf, len, 2));
        break;
    }
  }
  private void add(String word){
    if(variants == null) variants = new ArrayList();
    variants.add(trim(word));
  }
  private void appendLeading(StringBuffer entry, int type, String value){
    switch(type){// leading
      case 0xa3:
        entry.append("Main Entry:");
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        break;
      case 0x84:
        if(type == lastType) entry.append(",");
        else break;
      case 0xaa:
        if(type == lastType){
          if(type == 0xaa) entry.append(", ");
        }else{
          entry.append(newLine);
          entry.append("Usage:");
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
          entry.append(space);// 18
        }
        break;
      case 0xad:
        entry.append(newLine);
        entry.append("Pronunciation:");
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        break;
      case 0xd4:
        if(lastType == 0xc8 || lastType == 0x8f){
          entry.append(space);// 3
          break;
        }
        entry.append(newLine);
        entry.append("Variant:");
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        entry.append(space);// 3
        break;
      case 0x85:
        entry.append(newLine);
        entry.append("Date:");
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        entry.append(space);// 17
        break;
      case 0x9f:
        entry.append(newLine);
        entry.append("Function:");
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        entry.append(space);// 11
        break;
      case 0x9e:
        entry.append(newLine);
        entry.append("Etymology:");
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        entry.append(space);// 8
        break;
      case 0xa4:
      case 0xa5:
        if(lastType == 0xa5) entry.append(" ");
        else if(lastType == 0xa7 || lastType == 0xa4) entry.append("; ");
        else{
          entry.append(newLine);
          entry.append("Inflected Form:");
          entry.append(space);// 2
          entry.append(space);// 2
          entry.append(space);// 2
        }
        break;
      case 0xaf:
        entry.append("<b>synonym</b> see in addition");
        entry.append(space);
        break;
      case 0xd7:
        entry.append("— more at");// — :0x2014
        entry.append(space);
        break;
      case 0xbe:
        entry.append(newLine);
        entry.append("<b>synonyms</b> see");
        entry.append(space);
        break;
      case 0xc7:
      case 0xcb:
      case 0xf0:
        entry.append(", ");
        break;
      case 0x8f:
      case 0xc8:
        entry.append(newLine);
        entry.append(" –");// 0x2003,0x2021
        break;
      case 0xe1:
      case 0xd6:
      case 0xe2:
        entry.append(" — ");// 0x20,0x2014,0x20
        break;
      case 0xb0:
        entry.append("; ");
        break;
      case 0xb2:
      case 0xc5:
      case 0xca:
      case 0xb7:
      case 0xbd:
      case 0xeb:
        entry.append(space);
        break;
      case 0xb9:
        if(!(value.length() > 0 && Character.isDigit(value.charAt(0)))){
          entry.append(space);
          break;
        }
        entry.append(newLine);
        break;
      case 0x99:
        if(lastType == 0x8f) break;
        if(lastType == 0x99) break;
        if(lastType == 0xb1) break;
        if(lastType == 0xb2) break;
        if(lastType == 0xb3) break;
        if(lastType == 0xb5) break;
        if(lastType == 0xb7) break;
        if(lastType == 0xb9) break;
        if(lastType == 0xba) break;
        if(lastType == 0xbb) break;
        if(lastType == 0xbd) break;
        if(lastType == 0xca) break;
        if(lastType == 0xe2) break;
        if(lastType == 0xed) break;
        entry.append(newLine);
        break;
      case 0x83:
        entry.append(newLine);
      case 0xab:
      case 0xac:
      case 0xd5:
        entry.append(newLine);
        break;
      case 0xcc:
        entry.append(newLine);
        entry.append("<b>usage</b> see");
        entry.append(space);
        break;
    }
  }
  private void appendValue(StringBuffer entry, int type, String value){
    switch(type){// value
      case 0xa3:
      case 0xad:
      case 0x85:
      case 0x9f:
      case 0x9e:
      case 0xa5:
      case 0xe1:
      case 0xd6:
      case 0xe2:
      case 0xec:
      case 0x99:
      case 0xf0:
      case 0xeb:
      case 0xed:
      case 0xae:
        entry.append(value);
        break;

      case 0xac:
        if(value.equals("syn")) value = "synonyms";
        else if(value.equals("usage")) value = "usage";
        else throw new RuntimeException(value);
      case 0xb9:
      case 0x9b:
      case 0xa4:
      case 0xab:
      case 0xc0:
      case 0xcd:
      case 0xf5:
      case 0x8f:
      case 0xc8:
      case 0xb2:
      case 0xd1:
        entry.append("<b>");
        entry.append(value);
        entry.append("</b>");
        break;
      case 0x83:
      case 0x9c:
      case 0xaa:
      case 0xb3:
      case 0xb7:
      case 0xba:
      case 0xbd:
      case 0xc5:
      case 0xce:
      case 0xd5:
      case 0xc7:
      case 0xcb:
      case 0xb0:
      case 0xd2:
      case 0xc1:
        entry.append("<i>");
        entry.append(value);
        entry.append("</i>");
        break;
      case 0xd7:
      case 0xaf:
      case 0xbe:
      case 0x84:
      case 0xcc:
        entry.append(value.toUpperCase());
        break;
      case 0x89:
      case 0xb1:
        entry.append("[");
        entry.append(value);
        entry.append("]");
        break;
      case 0x8e:
      case 0xa7:
      case 0xb5:
      case 0xc2:
      case 0xbb:
      case 0xca:
      case 0xd3:
        entry.append("\\");
        entry.append(value);
        entry.append("\\");
        break;
      case 0x81:
      case 0xee:
      case 0xef:
      case 0x8d:
      case 0xb4:
      case 0xbf:
      case 0xc3:
      case 0xc9:
      case 0xd4:
      case 0xf1:
      case 0xf2:
      case 0xf3:
      case 0xf4:
        dummy = true;
        break;
    }
  }
  private static void appendEnding(StringBuffer content, int type){
    switch(type){// ending
      case 0x83:
      case 0xd1:
        content.append(space);
        break;
      case 0xa4:
      case 0xa5:
      case 0xac:
      case 0xb0:
      case 0xed:
      case 0xc8:
        content.append(" ");
        break;
      case 0x85:
        content.append(newLine);
        break;
    }
  }

  String getString(int type, byte[] buf, int len){
    String value = changeToCap(getString(buf, len, 0));// no change
    if(type == 0xa3){
      keyWord = trim(getString(buf, len, 2));// keyword
      ascKeyWord = trim(getString(buf, len, 3));// ascii keyword
      if(!keyWord.equals(ascKeyWord)) add(ascKeyWord);
    }
    return value;
  }
  private String getString(byte[] buf, int len, int style){
    int[] temp = new int[len + 0x400];
    int count = 0;
    int data;
    boolean skip = false;
    int type = -1;
    boolean convert = false;
    int fontType = -1;
    String tag = null, endTag = null;
    int[] unicode;
    for(int i = 0; i < len; i++){
      data = 0xff & buf[i];
      if(skip){
        skip = false;
        continue;
      }
      if(convert || (data > 0x80)){
        // TODO check data > 0x80
        convert = false;
        unicode = convert(type, data, (style & 0x3) == 0x3);
        for(int j = 0; j < unicode.length; j++)
          temp[count++] = unicode[j];
      }else switch(data){
        case '<':
          count += appendString(temp, count, "&lt");
          break;
        case '>':
          count += appendString(temp, count, "&gt");
          break;
        case '&':
          count += appendString(temp, count, "&amp");
          break;
        case 0x15:
          if((style & 0x02) != 0){
            skip = true;
            break;
          }
        case 0x19:
        case 0x1a:
        case 0x1b:
          convert = true;
          type = data;
          break;
        case 0x00:
          break;
        case 0x01:
        case 0x02:
        case 0x03:
        case 0x04:
        case 0x05:
        case 0x06:
        case 0x07:
        case 0x08:
        case 0x09:
        case 0x0a:
          if((style & 0x02) != 0) break;
          if(fontType != -1 && fontType != data){
            count += appendString(temp, count, endTag);
            fontType = data;
          }else fontType = data;
          switch(data){
            case 0x01:
              tag = null;
              endTag = null;
              break;
            case 0x02:
              tag = "<b>";
              endTag = "</b>";
              break;
            case 0x03:
              tag = "<i>";
              endTag = "</i>";
              break;
            case 0x04:
              tag = "<cap>";
              endTag = "</cap>";
              break;
            case 0x05:
              tag = "<sup>";
              endTag = "</sup>";
              break;
            case 0x06:
              tag = "<sub>";
              endTag = "</sub>";
              break;
            case 0x07:
            case 0x09:
              tag = null;
              endTag = null;
              break;
            case 0x08:
            case 0x0a:
              throw new RuntimeException(data + keyWord);
          }
          count += appendString(temp, count, tag);
          break;
        case 0x7e:
          count += appendString(temp, count, "<i>");
          count += appendString(temp, count, keyWord);
          count += appendString(temp, count, "</i>");
          break;
        case '.':
          if((style & 0x02) != 0) break;
          else data = 0xb7;// 0x2027
        default:
          temp[count++] = data;
          break;
      }
    }
    return new String(temp, 0, count);
  }
  private static int appendString(int[] ints, int offset, String s){
    int count = s == null ? 0 : s.length();
    for(int i = 0; i < count; i++)
      ints[offset + i] = 0xffff & s.charAt(i);
    return count;
  }
  private static int[] convert(int type, int data, boolean ascii){
    int[] i = new int[] { data };
    switch(type){
      case 0x15:
        if(data >= '1' && data <= '9') i = map1bh[data - '0' + 0x40];
        else i = null;
        break;
      default:
      case 0x19:
        if(data >= 0x80 && data <= 0xff) i = ascii ? map19h_[data - 0x80] : map19h[data - 0x80];
        // else i = null;
        break;
      case 0x1a:
        if(data >= 0x20 && data <= 0xff) i = ascii ? map1ah_[data - 0x20] : map1ah[data - 0x20];
        else i = null;
        break;
      case 0x1b:
        if(data >= 0x20 && data <= 0xff) i = map1bh[data - 0x20];
        else i = null;
        break;
    }
    if(i == null || i[0] == -1) throw new RuntimeException(Integer.toHexString(type) + ":" + Integer.toHexString(data));
    return i;
  }

  private static final int[][] map19h_ = new int[][] {
    /**       0               1              2              3              4                  5              6                  7              8              9               a              b              c              d               e               f            */
    /** 0x80 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},     new int[]{-1}, new int[]{'a'},    new int[]{-1}, new int[]{'i'},new int[]{-1},  new int[]{-1}, new int[]{'o'},new int[]{-1}, new int[]{-1},  new int[]{-1},  new int[]{-1},
    /** 0x90 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},     new int[]{-1}, new int[]{'-'},    new int[]{-1}, new int[]{-1}, new int[]{-1},  new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},  new int[]{-1},  new int[]{-1},
    /** 0xa0 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'o','e'},new int[]{-1}, new int[]{-1},     new int[]{-1}, new int[]{-1}, new int[]{-1},  new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},  new int[]{-1},  new int[]{-1},
    /** 0xb0 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},     new int[]{-1}, new int[]{-1},     new int[]{-1}, new int[]{-1}, new int[]{'\''},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'\''},new int[]{-1},  new int[]{-1},
    /** 0xc0 */new int[]{'A'},new int[]{'A'},new int[]{-1}, new int[]{-1}, new int[]{-1},     new int[]{'A'},new int[]{'A','E'},new int[]{'C'},new int[]{-1}, new int[]{'E'}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'I'}, new int[]{'I'}, new int[]{-1},
    /** 0xd0 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},     new int[]{'O'},new int[]{'O'},    new int[]{-1}, new int[]{'O'},new int[]{-1},  new int[]{-1}, new int[]{-1}, new int[]{'U'},new int[]{-1},  new int[]{-1},  new int[]{-1},
    /** 0xe0 */new int[]{'a'},new int[]{'a'},new int[]{'a'},new int[]{'a'},new int[]{'a'},    new int[]{'a'},new int[]{'a','e'},new int[]{'c'},new int[]{'e'},new int[]{'e'}, new int[]{'e'},new int[]{'e'},new int[]{'i'},new int[]{'i'}, new int[]{'i'}, new int[]{'i'},
    /** 0xf0 */new int[]{-1}, new int[]{'n'},new int[]{-1}, new int[]{'o'},new int[]{'o'},    new int[]{'o'},new int[]{'o'},    new int[]{-1}, new int[]{'o'},new int[]{'u'}, new int[]{'u'},new int[]{'u'},new int[]{'u'},new int[]{-1},  new int[]{-1},  new int[]{'y'},
  };
  private static final int[][] map1ah_ = new int[][] {
    /**        0              1              2              3              4              5              6              7              8              9              a              b              c              d              e              f            */
    /** 0x20 */new int[]{-1}, new int[]{'A'},new int[]{-1}, new int[]{'a'},new int[]{-1}, new int[]{'a'},new int[]{-1}, new int[]{'c'},new int[]{'C'},new int[]{'c'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'e'},
    /** 0x30 */new int[]{-1}, new int[]{'e'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'h'},new int[]{-1}, new int[]{-1}, new int[]{'I'},new int[]{'a'},new int[]{-1}, new int[]{'l'},new int[]{-1}, new int[]{'n'},new int[]{-1},
    /** 0x40 */new int[]{'n'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'o'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'r'},new int[]{'S'},new int[]{'s'},new int[]{-1}, new int[]{'s'},new int[]{'S'},new int[]{'s'},
    /** 0x50 */new int[]{-1}, new int[]{'t'},new int[]{-1}, new int[]{-1}, new int[]{'u'},new int[]{'U'},new int[]{'u'},new int[]{'U'},new int[]{'u'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0x60 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'Z'},new int[]{'z'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0x70 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'S'},new int[]{-1}, new int[]{'s'},new int[]{-1}, new int[]{-1}, new int[]{'.'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0x80 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'?'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0x90 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0xa0 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0xb0 */new int[]{-1}, new int[]{'H'},new int[]{'H'},new int[]{-1}, new int[]{'T'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0xc0 */new int[]{-1}, new int[]{-1}, new int[]{'n'},new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{'t'},new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0xd0 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0xe0 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
    /** 0xf0 */new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1}, new int[]{-1},
  };
  private static final int[][] map19h = new int[][] {
    /**        0               1               2                      3                      4                 5                      6                 7                 8                 9                    a                                    b                                                            c                                              d                 e                 f               */
    /** 0x80 */new int[]{-1},  new int[]{-1},  new int[]{0x2c8},      new int[]{0x2cc},      new int[]{0x259}, new int[]{0x259,0x307},new int[]{0x101}, new int[]{0x113}, new int[]{0x1fd1},new int[]{0x1e35},   new int[]{0x14b},                    new int[]{0x14d},                                            new int[]{'<','u','>','t','h','<','/','u','>'},new int[]{-1},    new int[]{-1},    new int[]{-1},
    /** 0x90 */new int[]{-1},  new int[]{0xa6},new int[]{'a',0x307},  new int[]{0x1e07},     new int[]{0x1e21},new int[]{'o',0x307},  new int[]{'-'},   new int[]{0x2014},new int[]{0x307}, new int[]{'u',0x307},new int[]{0xfc},                     new int[]{0x2015},                                           new int[]{0x2019},                             new int[]{-1},    new int[]{-1},    new int[]{0x207f},
    /** 0xa0 */new int[]{' '}, new int[]{0xb9},new int[]{0xb2},       new int[]{0x2022},     new int[]{0x153}, new int[]{0x153,0x305},new int[]{0xff5e},new int[]{0xb3},  new int[]{0x2074},new int[]{0x2075},   new int[]{0x2002,0x337,0x2004,0x337},new int[]{'<','s','u','p','>',0x259,'<','/','s','u','p','>'},new int[]{-1},                                 new int[]{0xfc02},new int[]{0x2076},new int[]{0x2077},
    /** 0xb0 */new int[]{0xb0},new int[]{0xb1},new int[]{'&','l','t'},new int[]{'&','g','t'},new int[]{0x2078},new int[]{0x2079},     new int[]{0x2070},new int[]{-1},    new int[]{-1},    new int[]{0x2018},   new int[]{0x2019},                   new int[]{0x201c},                                           new int[]{0x201d},                             new int[]{0x2032},new int[]{0x2033},new int[]{0x77},
    /** 0xc0 */new int[]{0xc0},new int[]{0xc1},new int[]{0xc2},       new int[]{0xc3},       new int[]{0xc4},  new int[]{0xc5},       new int[]{0xc6},  new int[]{0xc7},  new int[]{0xc8},  new int[]{0xc9},     new int[]{0xca},                     new int[]{0xcb},                                             new int[]{0xcc},                               new int[]{0xcd},  new int[]{0xce},  new int[]{0xcf},
    /** 0xd0 */new int[]{0xd0},new int[]{0xd1},new int[]{0xd2},       new int[]{0xd3},       new int[]{0xd4},  new int[]{0xd5},       new int[]{0xd6},  new int[]{0xd7},  new int[]{0xd8},  new int[]{0xd9},     new int[]{0xda},                     new int[]{0xdb},                                             new int[]{0xdc},                               new int[]{0xdd},  new int[]{0xde},  new int[]{0xdf},
    /** 0xe0 */new int[]{0xe0},new int[]{0xe1},new int[]{0xe2},       new int[]{0xe3},       new int[]{0xe4},  new int[]{0xe5},       new int[]{0xe6},  new int[]{0xe7},  new int[]{0xe8},  new int[]{0xe9},     new int[]{0xea},                     new int[]{0xeb},                                             new int[]{0xec},                               new int[]{0xed},  new int[]{0xee},  new int[]{0xef},
    /** 0xf0 */null,           new int[]{0xf1},new int[]{0xf2},       new int[]{0xf3},       new int[]{0xf4},  new int[]{0xf5},       new int[]{0xf6},  new int[]{0xf7},  new int[]{0x0f8}, new int[]{0xf9},     new int[]{0xfa},                     new int[]{0xfb},                                             new int[]{0xfc},                               new int[]{0xfd},  new int[]{0xfe},  new int[]{0xff }
  };
  private static final int[][] map1ah = new int[][] {
    /**        0                     1                    2                    3                    4                    5                      6                    7                    8                      9                    a                     b                 c                     d                    e                    f                  */
    /** 0x20 */new int[]{' '},       new int[]{0x100},    new int[]{0x102},    new int[]{0x103},    new int[]{0x104},    new int[]{0x105},      new int[]{0x106},    new int[]{0x107},    new int[]{0x10c},      new int[]{0x10d},    new int[]{0x112},     new int[]{0x114}, new int[]{0x115},     new int[]{0x117},    new int[]{0x118},    new int[]{0x119},
    /** 0x30 */new int[]{0x114},     new int[]{0x115},    new int[]{0x11c},    new int[]{0x11e},    new int[]{0x11f},    new int[]{0x121},      new int[]{'h',0x323},new int[]{0x12a},    new int[]{0x12d},      new int[]{0x130},    new int[]{0x131},     new int[]{-1},    new int[]{0x142},     new int[]{0x143},    new int[]{0x144},    new int[]{0x147},
    /** 0x40 */new int[]{0x148},     new int[]{0x14c},    new int[]{0x14e},    new int[]{0x14f},    new int[]{0x150},    new int[]{0x151},      new int[]{0x152},    new int[]{0x154},    new int[]{0x158},      new int[]{0x159},    new int[]{0x15a},     new int[]{0x15b}, new int[]{0x15e},     new int[]{0x15f},    new int[]{0x160},    new int[]{0x161},
    /** 0x50 */new int[]{0x162},     new int[]{0x163},    new int[]{0x169},    new int[]{0x16a},    new int[]{0x16b},    new int[]{0x16c},      new int[]{0x16d},    new int[]{0x16e},    new int[]{0x16f},      new int[]{0x171},    new int[]{0x173},     new int[]{0x175}, new int[]{0x176},     new int[]{0x177},    new int[]{0x178},    new int[]{0x179},
    /** 0x60 */new int[]{0x17a},     new int[]{0x17b},    new int[]{0x17c},    new int[]{0x17d},    new int[]{0x17e},    new int[]{0x180},      new int[]{-1},       new int[]{-1},       new int[]{0x1d0},      new int[]{0x1d2},    new int[]{0x1d4},     new int[]{0x1dc}, new int[]{0xe6,0x304},new int[]{0x1e5},    new int[]{-1},       new int[]{-1},
    /** 0x70 */new int[]{'O',0x322}, new int[]{'o',0x322},new int[]{'j',0x30c},new int[]{0x1fd},    new int[]{'s',0x306},new int[]{-1},         new int[]{'s',0x323},new int[]{0x2bd},    new int[]{-1},         new int[]{0xb7},     new int[]{-1},        new int[]{-1},    new int[]{-1},        new int[]{0xf0},     new int[]{-1},       new int[]{-1},
    /** 0x80 */new int[]{-1},        new int[]{-1},       new int[]{'0',0x338},new int[]{-1},       new int[]{0xa2},     new int[]{0xa7},       new int[]{0x2020},   new int[]{0xb6},     new int[]{0xbf},       new int[]{-1},       new int[]{0x2009},    new int[]{-1},    new int[]{-1},        new int[]{-1},       new int[]{-1},       new int[]{-1},
    /** 0x90 */new int[]{-1},        new int[]{-1},       new int[]{-1},       new int[]{0xab},     new int[]{0xbb},     new int[]{0x227a},     new int[]{0x227b},   new int[]{0x2030},   new int[]{0x2264},     new int[]{0x2265},   new int[]{0x2260},    new int[]{0x2229},new int[]{0x222a},    new int[]{-1},       new int[]{-1},       new int[]{0x2297},
    /** 0xa0 */new int[]{-1},        new int[]{-1},       new int[]{'y',0x304},new int[]{'Y',0x304},new int[]{-1},       new int[]{-1},         new int[]{-1},       new int[]{-1},       new int[]{-1},         new int[]{-1},       new int[]{-1},        new int[]{-1},    new int[]{-1},        new int[]{-1},       new int[]{'z',0x327},new int[]{'z',0x331},
    /** 0xb0 */new int[]{-1},        new int[]{'H',0x327},new int[]{'H',0x323},new int[]{-1},       new int[]{'T',0x323},new int[]{-1},         new int[]{-1},       new int[]{-1},       new int[]{0x101,0x303},new int[]{'c',0x307},new int[]{0x64,0x323},new int[]{0x1ebd},new int[]{-1},        new int[]{-1},       new int[]{'h',0x32e},new int[]{'H',0x331},
    /** 0xc0 */new int[]{'l',0x323}, new int[]{'m',0x307},new int[]{'n',0x323},new int[]{'n',0x307},new int[]{-1},       new int[]{-1},         new int[]{-1},       new int[]{'p',0x313},new int[]{0x71,0x307}, new int[]{'r',0x323},new int[]{'r',0x331}, new int[]{-1},    new int[]{'t',0x323}, new int[]{'t',0x331},new int[]{'y',0x307},new int[]{'z',0x323},
    /** 0xd0 */new int[]{-1},        new int[]{-1},       new int[]{-1},       new int[]{-1},       new int[]{-1},       new int[]{-1},         new int[]{-1},       new int[]{'D',0x323},new int[]{0x111},      new int[]{-1},       new int[]{-1},        new int[]{-1},    new int[]{-1},        new int[]{-1},       new int[]{-1},       new int[]{-1},
    /** 0xe0 */new int[]{'h',0x327}, new int[]{'h',0x32c},new int[]{'h',0x331},new int[]{-1},       new int[]{-1},       new int[]{0x12b,0x303},new int[]{'L',0x323},new int[]{'l',0x331},new int[]{'m',0x323},  new int[]{'N',0x307},new int[]{0x4e,0x323},new int[]{-1},    new int[]{-1},        new int[]{'S',0x323},new int[]{-1},       new int[]{-1},
    /** 0xf0 */new int[]{0x52,0x331},new int[]{'R',0x323},new int[]{'c',0x304},new int[]{'i',0x335},new int[]{-1},       new int[]{-1},         new int[]{0x262e},   new int[]{0x211e},   new int[]{0x221a},     new int[]{-1},       new int[]{0x21e},     new int[]{-1},    new int[]{-1},        new int[]{-1},       new int[]{-1},       new int[]{-1}
  };
  private static final int[][] map1bh = new int[][] {
    /**        0                 1                      2                      3                      4                      5                 6                 7                 8                 9                 a                b                 c                      d                 e                 f               */
    /** 0x20 */new int[]{' '},   new int[]{0x250},      new int[]{0x251},      new int[]{0x252},      new int[]{-1},         new int[]{-1},    new int[]{0x2a4}, new int[]{0x259}, new int[]{0x25a}, new int[]{0x25b}, new int[]{0x25d},new int[]{0x261}, new int[]{0x263},      new int[]{0x266}, new int[]{-1},    new int[]{0x268},
    /** 0x30 */new int[]{0x269}, new int[]{0x26a},      new int[]{-1},         new int[]{-1},         new int[]{0x26b},      new int[]{0x26c}, new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},   new int[]{-1},    new int[]{0x254},      new int[]{-1},    new int[]{-1},    new int[]{-1},
    /** 0x40 */new int[]{0x283}, new int[]{0x2a7},      new int[]{-1},         new int[]{-1},         new int[]{-1},         new int[]{-1},    new int[]{0x292}, new int[]{0x294}, new int[]{0x295}, new int[]{-1},    new int[]{-1},   new int[]{-1},    new int[]{0x2d2,0x2be},new int[]{-1},    new int[]{0x127}, new int[]{-1},
    /** 0x50 */new int[]{-1},    new int[]{-1},         new int[]{0x259,0x300},new int[]{0x254,0x301},new int[]{0x259,0x301},new int[]{-1},    new int[]{-1},    new int[]{0x3bc}, new int[]{0xfe},  new int[]{-1},    new int[]{-1},   new int[]{-1},    new int[]{0xff0d},     new int[]{0xff1d},new int[]{0x2261},new int[]{-1},
    /** 0x60 */new int[]{0x2070},new int[]{0xb9},       new int[]{0xb2},       new int[]{0xb3},       new int[]{0x2074},     new int[]{0x2075},new int[]{0x2076},new int[]{0x2077},new int[]{0x2078},new int[]{0x2079},new int[]{-1},   new int[]{0x221e},new int[]{0x222b},     new int[]{0xa9},  new int[]{0xae},  new int[]{-1},
    /** 0x70 */new int[]{0x2080},new int[]{0x2081},     new int[]{0x2082},     new int[]{0x2083},     new int[]{0x2084},     new int[]{0x2085},new int[]{0x2086},new int[]{0x2087},new int[]{0x2088},new int[]{0x2089},new int[]{-1},   new int[]{-1},    new int[]{-1},         new int[]{-1},    new int[]{-1},    new int[]{-1},
    /** 0x80 */new int[]{-1},    new int[]{-1},         new int[]{-1},         new int[]{-1},         new int[]{-1},         new int[]{-1},    new int[]{0x221a},new int[]{0x221b},new int[]{0x203e},new int[]{-1},    new int[]{-1},   new int[]{-1},    new int[]{-1},         new int[]{-1},    new int[]{-1},    new int[]{-1},
    /** 0x90 */new int[]{-1},    new int[]{'`'},        new int[]{0xb4},       new int[]{0x5e},       new int[]{0x7e},       new int[]{0xaf},  new int[]{-1},    new int[]{-1},    new int[]{0xa8},  new int[]{0xb0},  new int[]{0x7e}, new int[]{0x2c7}, new int[]{'^'},        new int[]{0xb8},  new int[]{-1},    new int[]{-1},
    /** 0xa0 */new int[]{' '},   new int[]{0x391},      new int[]{0x392},      new int[]{0x393},      new int[]{0x394},      new int[]{0x395}, new int[]{0x396}, new int[]{0x397}, new int[]{0x398}, new int[]{0x399}, new int[]{0x39a},new int[]{0x39b}, new int[]{0x39c},      new int[]{0x39d}, new int[]{0x39e}, new int[]{0x39f},
    /** 0xb0 */new int[]{0x3a0}, new int[]{0x3a1},      new int[]{0x3a2},      new int[]{0x3a3},      new int[]{0x3a4},      new int[]{0x3a5}, new int[]{0x3a6}, new int[]{0x3a7}, new int[]{0x3a8}, new int[]{0x3b1}, new int[]{0x3b2},new int[]{0x3b3}, new int[]{0x3b4},      new int[]{0x3b5}, new int[]{0x3b6}, new int[]{0x3b7},
    /** 0xc0 */new int[]{0x3b8}, new int[]{0x3b9},      new int[]{0x3ba},      new int[]{0x3bb},      new int[]{0x3bd},      new int[]{0x3be}, new int[]{0x3bf}, new int[]{0x3c0}, new int[]{0x3c1}, new int[]{0x3c2}, new int[]{0x3c3},new int[]{0x3c4}, new int[]{0x3c5},      new int[]{0x3c6}, new int[]{0x3c7}, new int[]{0x3c8},
    /** 0xd0 */new int[]{0x3c9}, new int[]{0x3b1,0x300},new int[]{0x3b5,0x301},new int[]{0x3b5,0x313},new int[]{-1},         new int[]{0x19b}, new int[]{0xa3},  new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},   new int[]{-1},    new int[]{-1},         new int[]{-1},    new int[]{0x203d},new int[]{0x266d},
    /** 0xe0 */new int[]{0x266f},new int[]{0x266e},     new int[]{0xb7,0x311}, new int[]{0x2021},     new int[]{-1},         new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},   new int[]{-1},    new int[]{-1},         new int[]{-1},    new int[]{-1},    new int[]{-1},
    /** 0xf0 */new int[]{-1},    new int[]{-1},         new int[]{-1},         new int[]{-1},         new int[]{-1},         new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},    new int[]{-1},   new int[]{-1},    new int[]{-1},         new int[]{-1},    new int[]{-1},    new int[]{0x2725}
  };
  
  /**
    0xa3: Main Entry:
    0x81: Picutre:(<h>)
    0x83: exp_<i>
    0x84: exp_<capital>
    0x85: Date:
    0x89: "["..."]"
    0x8e: ref_pron_"\"..."\"
    0x8f: <b>_"－"...
    0x9f: Function:
    0x99: explanation
    0x9b: <b>:
    0x9c: <i>
    0x9e: Etymology:
    0xa4: transform_<b>(";"between them)
    0xa5: Inflected Form:
    0xa7: trans_pron_"\"..."\"
    0xaa: Usage:_<i>(","between them):{Usage:often capitalized ,often attributive }
    0xab: <b>(new line)
    0xac: <b>(syn->synonyms; usage->usage)
    0xad: Pronunciation:
    0xae: usage & synonyms explanation
    0xaf: captical_"<b>synonym</b> see in addition "...
    0xb0: <i>_"; "..." "
    0xb1: "["..."]":{5 [<.>New Latin artium baccalaureus</.>] }
    0xb2: <b>_" "...:{2 <i>also</i> <.>acoustic</.> }
    0xb3: <i>:{2 <.>also</.> <b>acoustic</b> }
    0xb5: explan_num_pron_"\"..."\":{1 am-a-ret-ti \<.>-(.)te </.>\ plural}
    0xb7: <i>:{7 capitalized}
    0xb9: explanation_num:{<.>1 a</.> : the 1st; <.>b</.> : a graphic}
    0xba: explanation_num_<i>:{b <.>plural</.> : the act or action}
    0xbb: explanation_num_"\"..."\":{1 \<.>see AND</.>\ : AND}
    0xbd: explanation_<i>:{1 <.>chiefly dialect</.> : ON, IN, AT}
    0xbe: captical_"<b>synonyms</b> see "...
    0xd1: pronunciation_<b>:{Variant:<i>or</i> <.>an-</.> \(*)an\}
    0xd2: pronunciation_<i>:{Variant:<.>or</.> <b>an-</b> \(*)an\}
    0xd3: pronunciation_"\"..."\":{Variant:<i>or</i> <b>an-</b> \<.>(*)an</.>\}
    0xd4: variant:"\"..."\":{Variant:<i>or</i> <b>an-</b> \<.>(*)an</.>\}
    0xd5: <i>:{<.>transitive verb</.>\n<b>1 a</b> : to put an end to <abate a nuisance>}
    0xd6: explanation_"－"...:{ native to the Philippines － <.>called also Manila hemp</.>}
    0xd7: captical_"－more at "...:{English *n one - more at <.>one</.>}
    0xc0: explanation_num_<b>:{2 <i>or</i> <.>ab-or-an-y</.>: ABSORBANCE}
    0xc1: explanation_num__<i>:{2 <.>or</.> <b>ab-or-an-y</b>: ABSORBANCE}
    0xc2: explan_num_pron_"\"..."\":{1 a <i>also</i> all-Amer a \<.>-*-k*</.>\ : selected}
    0xc5: ref_<i>:{－ <b>aah</b> <i>also</i> <b>ah</b> <.>noun</.>}
    0xc7: ref_<i>_", "...:{－ <b>bstract expressionist</b> <i>noun or adjective</i>, <.>often capitalized A&E</.>}
    0xc8: ref_<b>_"－ "...:{－ <.>aah</.> <i>also</i> <b>ah</b> <i>noun</i>}
    0xca: ref_pronunciation_"\"..."\":{－ <b>aban-don-ment</b> \<.>-d*n-m*nt</.>\ <i>noun</i>}
    0xcb: ref_<i>_", "...:{－ <b>Asiatic</b> <i>noun</i>, <.>sometimes offensive</.>}
    0xcc: ref_captical_newline_"<b>usage</b> see ":{<b>usage</b> see 2<.>A</.>}
    0xcd: ref_<b>:{－ <b>aah</b> <i>also</i> <.>ah</.> <i>noun</i>}
    0xce: ref_<i>:{－ <b>aah</b> <.>also</.> <b>ah</b> <i>noun</i>}
    0xe1: ref_"－"...:{－ <.>see MONTH table</.>}
    0xe2: explanation_num_"－"...:{1 － <.>used as a function word before singular</.>}
    0xeb: example
    0xec: example_"－ "...:{－ Psalms 127:3(Authorized Version)}
    0xed: example?
    0xf0: main entry also_", "...:{Main Entry: aestivate, <.>aestivation</.>}
    0xf5: people name_<b>:{Peter French Pierre <.>Ale-lard</.> <i>or</i>}
    0xee: pic.dmp
    0xef: sound.wav(pre node in blue)
    0x8d: UNKOWN
    0xb4: UNKOWN
    0xbf: UNKOWN
    0xc3: UNKOWN
    0xc9: UNKOWN
    0xd4: UNKOWN
    0xf1: UNKOWN
    0xf2: UNKOWN
    0xf3: UNKOWN
    0xf4: UNKOWN 
  */
}
