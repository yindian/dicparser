/*****************************************************************************
 * 
 * @(#)DictDataTest.java  2009/03
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
package org.jin.dic.data.test.others;

import java.io.ByteArrayInputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jin.util.BytesUtil;
import org.jin.util.StringUtil;


public class DictDataTest {

  static String dS   = "3C005553CD8B57573E000A003C005553CD8B3E003C0021005B00430044004100540041005B006C0061006E0064006C006100640079005D005D003E003C002F005553CD8B3E000A003C005553CD8BE389CA9157573E000A003C00FA572C67CD8B494E3E000A003C005553CD8B79983E000A003C005553CD8B9F538B573E003C0021005B00430044004100540041005B006C0061006E0064006C006100640079005D005D003E003C002F005553CD8B9F538B573E000A003C00F39782820652B56B3E003C0021005B00430044004100540041005B006C0061006E0064002E006C0061002E00640079005D005D003E003C002F00F39782820652B56B3E000A003C005553CD8BF39707683E000A003C00410048004400F39707683E003C0021005B00430044004100540041005B004100480044003A005B00260032007B006C00B2006E0064001C206C00B3001D206400B6007D005D0020005D005D003E003C002F00410048004400F39707683E000A003C00FD564596F39707683E003C0021005B00430044004100540041005B0044002E004A002E005B00260032007B0036006C0023006E00640037006C00650021006400690038007D005D005D005D003E003C002F00FD564596F39707683E000A003C008E7FFD56F39707683E003C0021005B00430044004100540041005B004B002E004B002E005B00260032007B0036006C0023006E00640037006C006500640069007D005D005D005D003E003C002F008E7FFD56F39707683E000A003C002F005553CD8BF39707683E000A003C005553CD8BCD8B27603E003C0021005B00430044004100540041005B006E002E005D005D003E003C002F005553CD8BCD8B27603E000A003C00E389CA9179983E003C0021005B00430044004100540041005B004100200077006F006D0061006E002000770068006F0020006F0077006E007300200061006E0064002000720065006E007400730020006C0061006E0064002C0020006200750069006C00640069006E00670073002C0020006F00720020006400770065006C006C0069006E006700200075006E006900740073002E005D005D003E003C002F00E389CA9179983E000A003C00E389CA9179983E003C0021005B00430044004100540041005B004100200077006F006D0061006E002000770068006F002000720075006E00730020006100200072006F006F006D0069006E006700200068006F0075007300650020006F007200200061006E00200069006E006E003B00200061006E00200069006E006E006B00650065007000650072002E005D005D003E003C002F00E389CA9179983E000A003C002F005553CD8B79983E000A003C002F00FA572C67CD8B494E3E000A003C002F005553CD8BE389CA9157573E000A003C002F005553CD8B57573E000A000A00";
  static byte[] data = BytesUtil.revert(dS);

  public static void main(String[] args) throws Exception{
    a(data);

  }
  public static void a(byte[] data) throws Exception{
    String s = StringUtil.valueOf(data);
    System.out.println(s);
    s += "<?xml version=\"1.0\" encoding=\"unicode\"?>";
    // String s = xmlResponse.toString().replaceFirst("UTF-8", "ISO-8859-1");
    ByteArrayInputStream bos = new ByteArrayInputStream(s.getBytes("unicode"));
    SAXReader saxR = null;
    Document doc = null;
    Element root = null;
    // Element ele = null;
    saxR = new SAXReader();

    doc = saxR.read(bos);
    // root = (Element) doc.selectSingleNode("/µ¥´Ê¿é");
    // List l = root.selectNodes("leaf");
    // int count = 0;
    // for(int i = 0; i < l.size(); i++){
    // count++;
    // // ele = (Element) (l.get(i));
    // // String folderId = ele.attribute("fid").getText();
    // // String fileId = ele.attribute("id").getText();
    // // String fileName = ele.selectSingleNode("leafText").getText();
    // // String fileLen = ele.selectSingleNode("fileSize").getText();
    // // String creationDate = ele.selectSingleNode("createdOn").getText();
    // // String modifiedDate = ele.selectSingleNode("modifiedOn").getText();
    // }
  }
}
