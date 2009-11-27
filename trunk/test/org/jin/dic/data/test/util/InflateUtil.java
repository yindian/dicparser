/*****************************************************************************
 * 
 * @(#)InflateUtil.java  2009/03
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
package org.jin.dic.data.test.util;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.jin.util.BytesUtil;
import org.jin.util.io._ByteArrayOutputStream;


public class InflateUtil {

  static String dS   = "76DB75F75D78E395775E7AEB85B9F78A17028B37C68378102CDE20DF2243166F64710FA22752106FC610AF8C7C58BC312D4CF10666F6207AAE8817287B100F92C51BE75B8C7C8E4EF34604F1CA0C44C55B345B70E4B5BAAF05475E89455E11C6C82BD1C82BE216792520794524212FD5A4692CF24E38F28A5C475E9AE40E79230879230A79450222EF84242F91786F91978465F25238F2D62477C83B3229794518232F910779959297E45DF25A7910965C21EF150CA74CF24E40F24A40F34A4CF38ED12249C8E695A0E695B8E60D4B84A489CC2B531831AFCC2AD3BC42109662DE98368279239E79657C4B7EE89510E69521859957E404F30EA12AA97967AAA4E61559C5BC14CCBC226C9957C89122CD2B51CD3B41CC3BC2987947DC316FD1A4459A77429A97A29897629AB7A63063DE118AD05BE54106AB02BD66E84D5A187A6B4C65E81DB105BD1112BD31752CF45264F48AB1421329E89568E81D6919BD4210A682DE983B027A81B407F1201ABD31C165F44A40F4CEC022D11B5302432FCDAB13BD272CA35746C880DE4A0F32E49B33B9835E21972842AF887CE8159914BD51E9E870D02BC30642EF104BCFA157C40DF44A40F44E7990F148412F4985EA9D8145AA5782AA37EA9114867A859A4845BD22AEA857E2A9776C95028B54EF1C7B278DC0D47B32A97A2DB27A279A7AA93C082B6EAA3762AA37AE8E81924BEA0DD15A";
  static byte[] data = BytesUtil.revert(dS);
  public static void main(String[] args) throws DataFormatException{
    _ByteArrayOutputStream bos = new _ByteArrayOutputStream(0x4000);
    Inflater a = new Inflater();
    a.setInput(data);

    byte[] tempBuf = new byte[0x400];
    int readCount = 0;
    while(!a.finished()){
      readCount = a.inflate(tempBuf);
      bos.write(tempBuf, 0, readCount);
    }

    System.out.println(BytesUtil.convert(bos.toByteArray()));
  }

}
