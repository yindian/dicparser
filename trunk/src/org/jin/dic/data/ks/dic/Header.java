/*****************************************************************************
 * 
 * @(#)Header.java  2009/03
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
package org.jin.dic.data.ks.dic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Random;

import org.jin.dic.data.ks.BadFormatException;
import org.jin.dic.data.ks.Element;
import org.jin.util.CRC_CCITT;
import org.jin.util.Logger;
import org.jin.util.io._ByteArrayOutputStream;
import org.jin.util.io._DataOutputStream;


class Header implements Element {

  // magic value
  static final int        HEADERINFOLENGTH   = 0x78;
  static final int        SIGNATURE          = 0x4944534B;

  static final int        _8_                = 8;
  static final int        _1_                = 1;
  static final int        _20001_            = 0x20001;
  static final int        _0__               = 0;
  static final int        c_xxx_             = 0x150;

  // default value
  // min word number to create a item in index0
  public static final int MINWORDS           = 0x20;
  public static final int FRACTIONSIZE       = 0x4000;
  public static final int LCID_CHINESE       = 0x804;
  public static final int LCID_ENGLISH       = 0x409;
  public static final int LCID_GENERAL       = 0;

  // raw data
  private int             signature          = SIGNATURE;
  private int             fileLength         = 0;
  private int             crc16              = 0;
  private int             n_random_          = 0;
  private int             random             = 0;
  private int             _8                 = _8_;
  private int             _1                 = _1_;
  private int             inflatedFileLength = 0;
  private int             fractionSize       = FRACTIONSIZE;
  private int             _20001             = _20001_;
  private int             source_lcid        = LCID_GENERAL;
  private int             target_lcid        = LCID_GENERAL;
  private int             source_lcid_       = LCID_GENERAL;
  private int             wordCount          = 0;
  private int             minWords           = MINWORDS;
  private int             maxChars           = 0;
  private int             c_xxx              = c_xxx_;
  private int             _0                 = _0__;
  private int             headerlength       = HEADERINFOLENGTH;
  private int             dictInfoOffset     = 0;
  private int             dictInfoLength     = 0;
  private int             catalogOffset      = 0;
  private int             index0Length       = 0;
  private int             index1OffsetI      = 0;
  private int             index1Length       = 0;
  private int             index2OffsetI      = 0;
  private int             index2Length       = 0;
  private int             dictDataOffsetI    = 0;
  private int             dictDataLength     = 0;
  private int             _0_                = _0__;
  private DictInfo        dictInfo           = null;

  // offsetI = header + dictInfo + data
  // data = index0 + index1 + index2 + dictData

  // calculated data
  private int             splitedParts_      = 0;
  private int             dataOffset_        = 0;
  private int             dataLength_        = 0;

  public void read(DataInput in, int length) throws BadFormatException, IOException{
    dictInfo = new DictInfo();
    /* 0x00. */signature = in.readInt();
    /* 0x04. */fileLength = in.readInt();
    /* 0x08. */crc16 = in.readInt();
    /* 0x0c? */n_random_ = in.readInt();
    /* 0x10. */random = in.readInt();
    /* 0x14? */_8 = in.readInt();
    /* 0x18? */_1 = in.readInt();
    /* 0x1c. */inflatedFileLength = in.readInt();
    /* 0x20. */fractionSize = in.readInt();
    /* 0x24? */_20001 = in.readInt();
    /* 0x28. */source_lcid = in.readInt();
    /* 0x2c. */target_lcid = in.readInt();
    /* 0x30. */source_lcid_ = in.readInt();
    /* 0x34. */wordCount = in.readInt();
    /* 0x38. */minWords = in.readInt();// refer to DictIndex0
    /* 0x3c. */maxChars = in.readInt();// used while getting word list from index2
    /* 0x40? */c_xxx = in.readInt();
    /* 0x44? */_0 = in.readInt();
    /* 0x48. */headerlength = in.readInt();
    /* 0x4c. */dictInfoOffset = in.readInt();
    /* 0x50. */dictInfoLength = in.readInt();
    /* 0x54. */catalogOffset = in.readInt();
    /* 0x58. */index0Length = in.readInt();// inflated
    /* 0x5c. */index1OffsetI = in.readInt();// inflated file offset
    /* 0x60. */index1Length = in.readInt();// inflated
    /* 0x64. */index2OffsetI = in.readInt();// inflated file offset
    /* 0x68. */index2Length = in.readInt();// inflated
    /* 0x6c. */dictDataOffsetI = in.readInt();// inflated file offset
    /* 0x70. */dictDataLength = in.readInt();// inflated
    /* 0x74? */_0_ = in.readInt();
    /* 0x78. */dictInfo.read(in, dictInfoLength);

    splitedParts_ = (inflatedFileLength - catalogOffset - 1) / fractionSize + 1;
 
    dataOffset_ = (catalogOffset + (splitedParts_ << 2));
    dataLength_ = index0Length + index1Length + index2Length + dictDataLength;
  }

  public void write(DataOutput out) throws IOException{
    // set dependence
    setDependence();

    // write data
    out.write(getData());
  }

  public byte[] getData(){
    _ByteArrayOutputStream baos = null;
    _DataOutputStream os = null;
    byte[] data = new byte[0];
    try{
      baos = new _ByteArrayOutputStream(HEADERINFOLENGTH
          + (dictInfo == null ? 0 : dictInfo.getLength()));
      os = new _DataOutputStream(baos, true);
      os.writeInt(signature);
      os.writeInt(fileLength);
      os.writeInt(crc16);
      os.writeInt(n_random_);
      os.writeInt(random);
      os.writeInt(_8);
      os.writeInt(_1);
      os.writeInt(inflatedFileLength);
      os.writeInt(fractionSize);
      os.writeInt(_20001);
      os.writeInt(source_lcid);
      os.writeInt(target_lcid);
      os.writeInt(source_lcid_);
      os.writeInt(wordCount);
      os.writeInt(minWords);
      os.writeInt(maxChars);
      os.writeInt(c_xxx);
      os.writeInt(_0);
      os.writeInt(headerlength);
      os.writeInt(dictInfoOffset);
      os.writeInt(dictInfoLength);
      os.writeInt(catalogOffset);
      os.writeInt(index0Length);
      os.writeInt(index1OffsetI);
      os.writeInt(index1Length);
      os.writeInt(index2OffsetI);
      os.writeInt(index2Length);
      os.writeInt(dictDataOffsetI);
      os.writeInt(dictDataLength);
      os.writeInt(_0_);
      if(dictInfo != null) os.write(dictInfo.getData());
      data = baos.toByteArray();
    }catch(IOException e){
      Logger.err(e);
    }finally{
      if(os != null) try{
        os.close();
      }catch(IOException e){
        Logger.err(e);
      }
    }
    return data;
  }

  private void setDependence(){
    Random rand = new Random();
    n_random_ = rand.nextInt(0xffff);
    random = rand.nextInt();

    dictInfoLength = dictInfo == null ? 0 : dictInfo.getLength();
    dictInfoOffset = headerlength;
    catalogOffset = dictInfoOffset + dictInfoLength;
    index1OffsetI = dictInfoOffset + dictInfoLength + index0Length;
    index2OffsetI = index1OffsetI + index1Length;
    dictDataOffsetI = index2OffsetI + index2Length;

    inflatedFileLength = dictDataOffsetI + dictDataLength;

    // update crc16
    byte[] data = getData();
    CRC_CCITT crc = new CRC_CCITT();
    crc.update(0, data, 0x10);
    crc16 = crc.getValue();
  }
  public int getLength(){
    return HEADERINFOLENGTH + (dictInfo == null ? 0 : dictInfo.getLength());
  }

  public static int getHEADERINFOLENGTH(){
    return HEADERINFOLENGTH;
  }

  public static int getSIGNATURE(){
    return SIGNATURE;
  }

  public int getSignature(){
    return signature;
  }

  public int getFileLength(){
    return fileLength;
  }

  public int getCrc16(){
    return crc16;
  }

  public int getN_random_(){
    return n_random_;
  }

  public int getRandom(){
    return random;
  }

  public int get_8(){
    return _8;
  }

  public int get_1(){
    return _1;
  }

  public int getInflatedFileLength(){
    return inflatedFileLength;
  }

  public int getFractionSize(){
    return fractionSize;
  }

  public int get_20001(){
    return _20001;
  }

  public int getSource_lcid(){
    return source_lcid;
  }

  public int getTarget_lcid(){
    return target_lcid;
  }

  public int getSource_lcid_(){
    return source_lcid_;
  }

  public int getWordCount(){
    return wordCount;
  }

  public int getBufferedWords(){
    return minWords;
  }

  public int getMaxChars(){
    return maxChars;
  }

  public int getC_xxx(){
    return c_xxx;
  }

  public int get_0(){
    return _0;
  }

  public int getHeaderlength(){
    return headerlength;
  }

  public int getDictInfoOffset(){
    return dictInfoOffset;
  }

  public int getDictInfoLength(){
    return dictInfoLength;
  }

  public int getCatalogOffset(){
    return catalogOffset;
  }

  public int getIndex0Length(){
    return index0Length;
  }

  public int getIndex1OffsetI(){
    return index1OffsetI;
  }

  public int getIndex1Length(){
    return index1Length;
  }

  public int getIndex2OffsetI(){
    return index2OffsetI;
  }

  public int getIndex2Length(){
    return index2Length;
  }

  public int getDictDataOffsetI(){
    return dictDataOffsetI;
  }

  public int getDictDataLength(){
    return dictDataLength;
  }

  public int get_0_(){
    return _0_;
  }

  public DictInfo getDictInfo(){
    return dictInfo;
  }

  public int getSplitedParts_(){
    return splitedParts_;
  }

  public int getDataOffset_(){
    return dataOffset_;
  }

  public int getDataLength_(){
    return dataLength_;
  }

  public void setSignature(int signature){
    this.signature = signature;
  }

  public void setFileLength(int fileLength){
    this.fileLength = fileLength;
  }

  public void setCrc16(int crc16){
    this.crc16 = crc16;
  }

  public void setN_random_(int n_random_){
    this.n_random_ = n_random_;
  }

  public void setRandom(int random){
    this.random = random;
  }

  public void set_8(int _8){
    this._8 = _8;
  }

  public void set_1(int _1){
    this._1 = _1;
  }

  public void setInflatedFileLength(int inflatedFileLength){
    this.inflatedFileLength = inflatedFileLength;
  }

  public void setFractionSize(int fractionSize){
    this.fractionSize = fractionSize;
  }

  public void set_20001(int _20001){
    this._20001 = _20001;
  }

  public void setSource_lcid(int source_lcid){
    this.source_lcid = source_lcid;
  }

  public void setTarget_lcid(int target_lcid){
    this.target_lcid = target_lcid;
  }

  public void setSource_lcid_(int source_lcid_){
    this.source_lcid_ = source_lcid_;
  }

  public void setWordCount(int wordCount){
    this.wordCount = wordCount;
  }

  public void setMinWords(int minWords){
    this.minWords = minWords;
  }

  public void setMaxChars(int maxChars){
    this.maxChars = maxChars;
  }

  public void setC_xxx(int c_xxx){
    this.c_xxx = c_xxx;
  }

  public void set_0(int _0){
    this._0 = _0;
  }

  public void setHeaderlength(int headerlength){
    this.headerlength = headerlength;
  }

  public void setDictInfoLength(int dictInfoLength){
    this.dictInfoLength = dictInfoLength;
  }

  public void setIndex0Length(int index0Length){
    this.index0Length = index0Length;
  }

  public void setIndex1Length(int index1Length){
    this.index1Length = index1Length;
  }

  public void setIndex2Length(int index2Length){
    this.index2Length = index2Length;
  }

  public void setDictDataLength(int dictDataLength){
    this.dictDataLength = dictDataLength;
  }

  public void set_0_(int _0_){
    this._0_ = _0_;
  }

  public void setDictInfo(DictInfo dictInfo){
    this.dictInfo = dictInfo;
  }

  public void setSplitedParts_(int splitedParts_){
    this.splitedParts_ = splitedParts_;
  }

  public void setDataOffset_(int dataOffset_){
    this.dataOffset_ = dataOffset_;
  }

  public void setDataLength_(int dataLength_){
    this.dataLength_ = dataLength_;
  }

}
