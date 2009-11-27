package org.jin.dic.data.webster.c11.v3_0;

import java.io.IOException;

import org.jin.util.io._RandomAccessFile;

public class CheckType {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{

    _RandomAccessFile f = new _RandomAccessFile("C11/C11.inflate.cmp", "r");
    Parse parse = new Parse();
    int type;
    int len;
    byte[] buf = new byte[2048];
    String value = null;
    boolean quit = false;
    int count = 2;
    while(f.getFilePointer() < f.length()){
      if(count == 0) break;
      if(quit) count--;
      type = f.read();
      if(type == 0) continue;
      len = f.read();
      if((len & 0x80) != 0) len = f.read() + (0x80 * (len & 0x7f));
      f.readFully(buf, 0, len);
      value = parse.getString(type, buf, len);
      switch(type){
        case 0xa3:// Main Entry:
        case 0x81:// Picutre:(<h>)
        case 0x83:// exp_<i>
        case 0x84:// exp_<capital>
        case 0x85:// Date:
        case 0x89:// "["..."]"
        case 0x8e:// ref_pron_"\"..."\"
        case 0x8f:// <b>_"гн"...
        case 0x9f:// Function:
        case 0x99:// explanation
        case 0x9b:// <b>:
        case 0x9c:// <i>
        case 0x9e:// Etymology:
        case 0xa4:// transform_<b>(";"between them)
        case 0xa5:// Inflected Form:
        case 0xa7:// trans_pron_"\"..."\"
        case 0xaa:// Usage:_<i>(","between them):{Usage:often capitalized ,often attributive }
        case 0xab:// <b>(new line)
        case 0xac:// <b>(syn->synonyms; usage->usage)
        case 0xad:// Pronunciation:
        case 0xae:// usage & synonyms explanation
        case 0xaf:// captical_"<b>synonym</b> see in addition "...
        case 0xb0:// <i>_"; "..." "
        case 0xb1:// "["..."]":{5 [<.>New Latin artium baccalaureus</.>] }
        case 0xb2:// <b>_" "...:{2 <i>also</i> <.>acoustic</.> }
        case 0xb3:// <i>:{2 <.>also</.> <b>acoustic</b> }
        case 0xb5:// explan_num_pron_"\"..."\":{1 am-a-ret-ti \<.>-(.)te </.>\ plural}
        case 0xb7:// <i>:{7 capitalized}
        case 0xb9:// explanation_num:{<.>1 a</.> : the 1st; <.>b</.> : a graphic}
        case 0xba:// explanation_num_<i>:{b <.>plural</.> : the act or action}
        case 0xbb:// explanation_num_"\"..."\":{1 \<.>see AND</.>\ : AND}
        case 0xbd:// explanation_<i>:{1 <.>chiefly dialect</.> : ON, IN, AT}
        case 0xbe:// captical_"<b>synonyms</b> see "...
        case 0xd1:// pronunciation_<b>:{Variant:<i>or</i> <.>an-</.> \(*)an\}
        case 0xd2:// pronunciation_<i>:{Variant:<.>or</.> <b>an-</b> \(*)an\}
        case 0xd3:// pronunciation_"\"..."\":{Variant:<i>or</i> <b>an-</b> \<.>(*)an</.>\}
        case 0xd5:// <i>:{<.>transitive verb</.>\n<b>1 a</b> : to put an end to <abate a nuisance>}
        case 0xd6:// explanation_"гн"...:{ native to the Philippines гн <.>called also Manila hemp</.>}
        case 0xd7:// captical_"гнmore at "...:{English *n one - more at <.>one</.>}
        case 0xc0:// explanation_num_<b>:{2 <i>or</i> <.>ab-or-an-y</.>: ABSORBANCE}
        case 0xc1:// explanation_num__<i>:{2 <.>or</.> <b>ab-or-an-y</b>: ABSORBANCE}
        case 0xc2:// explan_num_pron_"\"..."\":{1 a <i>also</i> all-Amer a \<.>-*-k*</.>\ : selected}
        case 0xc5:// ref_<i>:{гн <b>aah</b> <i>also</i> <b>ah</b> <.>noun</.>}
        case 0xc7:// ref_<i>_", "...:{гн <b>bstract expressionist</b> <i>noun or adjective</i>, <.>often capitalized A&E</.>}
        case 0xc8:// ref_<b>_"гн "...:{гн <.>aah</.> <i>also</i> <b>ah</b> <i>noun</i>}
        case 0xca:// ref_pronunciation_"\"..."\":{гн <b>aban-don-ment</b> \<.>-d*n-m*nt</.>\ <i>noun</i>}
        case 0xcb:// ref_<i>_", "...:{гн <b>Asiatic</b> <i>noun</i>, <.>sometimes offensive</.>}
        case 0xcc:// ref_captical_newline_"<b>usage</b> see ":{<b>usage</b> see 2<.>A</.>}
        case 0xcd:// ref_<b>:{гн <b>aah</b> <i>also</i> <.>ah</.> <i>noun</i>}
        case 0xce:// ref_<i>:{гн <b>aah</b> <.>also</.> <b>ah</b> <i>noun</i>}
        case 0xe1:// ref_"гн"...:{гн <.>see MONTH table</.>}
        case 0xe2:// explanation_num_"гн"...:{1 гн <.>used as a function word before singular</.>}
        case 0xeb:// example
        case 0xec:// example_"гн "...:{гн Psalms 127:3(Authorized Version)}
        case 0xed:// example?
        case 0xf0:// main entry also_", "...:{Main Entry: aestivate, <.>aestivation</.>}
        case 0xf5:// people name_<b>:{Peter French Pierre <.>Ale-lard</.> <i>or</i>}
          System.out.print(Integer.toHexString(type));
          System.out.print("\t");
          System.out.println(value);
        case 0xee:// pic.dmp
        case 0xef:// sound.wav(pre node in blue)
          break;
        case 0x8d:// UNKOWN
        case 0xb4:// UNKOWN
        case 0xbf:// UNKOWN
        case 0xc3:// UNKOWN
        case 0xc9:// UNKOWN
        case 0xd4:// UNKOWN
        case 0xf1:// UNKOWN
        case 0xf2:// UNKOWN
        case 0xf3:// UNKOWN
        case 0xf4:// UNKOWN
          // if(len <=2 )
          break;
        default:
          System.out.print("err:");
          System.out.print(Integer.toHexString(type));
          System.out.print("\t");
          System.out.print(parse.getKeyWord());
          System.out.print("\t");
          System.out.println(value);
          quit = true;
          // System.out.print("\t");
          // System.out.println(BytesUtil.convert(buf, 0, len));
      }
    }
  }

}
