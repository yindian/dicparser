package com.jin.dic;

import java.util.ResourceBundle;

public class DictIni {

  private static ResourceBundle resource;

  // ks: kingsoft dic file
  // cs: custom dic file
  // l4: ldoce4 system
  // l5: ldoce5 system

  private static String         ksLineSepartor     = null;
  private static String         csLineSepartor     = null;
  private static String         l4LineSepartor     = null;
  private static String         l5LineSepartor     = null;

  private static String         kscsEncoding       = null;
  private static String         ksEncoding         = null;
  private static String         csEncoding         = null;
  private static String         l4Encoding         = null;
  private static String         l5Encoding         = null;
  private static String         ksLookupEncoding   = null;
  private static String         l4LookupEncoding   = null;
  private static String         l5LookupEncoding   = null;

  private static String         csInfoExtention    = null;
  private static String         csDataExtention    = null;
  private static String         csZipExtention     = null;
  private static String         csIndexExtention   = null;
  private static String         csSepartor         = null;

  private static String         csVoiceIndexName   = null;
  private static String         csVoiceZipName     = null;

  private static String         ksVoiceKey         = null;
  private static String         ksVoiceKeyEncoding = null;
  private static String         ksVoiceIndexFile   = null;
  private static String         ksVoiceRA4DataFile = null;
  private static String         ksVoiceRA3DataFile = null;

  static{
    resource = ResourceBundle.getBundle("dic");
    if(resource != null){
      ksLineSepartor = resource.getString("KS_LINE_SEPARTOR");
      csLineSepartor = resource.getString("CS_LINE_SEPARTOR");
      l4LineSepartor = resource.getString("L4_LINE_SEPARTOR");
      l5LineSepartor = resource.getString("L5_LINE_SEPARTOR");

      kscsEncoding = resource.getString("KSCS_ENCODING");
      ksEncoding = resource.getString("KS_ENCODING");
      csEncoding = resource.getString("CS_ENCODING");
      l4Encoding = resource.getString("L4_ENCODING");
      l5Encoding = resource.getString("L5_ENCODING");

      ksLookupEncoding = resource.getString("KS_LOOKUP_ENCODING");
      l4LookupEncoding = resource.getString("L4_LOOKUP_ENCODING");
      l5LookupEncoding = resource.getString("L5_LOOKUP_ENCODING");

      csInfoExtention = resource.getString("CS_INFO_EXT");
      csDataExtention = resource.getString("CS_DATA_EXT");
      csZipExtention = resource.getString("CS_ZIP_EXT");
      csSepartor = resource.getString("CS_SEPARTOR");
      csIndexExtention = resource.getString("CS_INDEX_EXT");

      csVoiceZipName = resource.getString("CS_VOICE_ZIP_NAME");
      csVoiceIndexName = resource.getString("CS_VOICE_INDEX_NAME");

      ksVoiceKey = resource.getString("KS_VOICE_KEY");
      ksVoiceKeyEncoding = resource.getString("KS_VOICE_KEY_ENCODING");
      ksVoiceIndexFile = resource.getString("KS_VOICE_INDEX_FILE_NAME");
      ksVoiceRA4DataFile = resource.getString("KS_VOICE_RA4_FILE_NAME");
      ksVoiceRA3DataFile = resource.getString("KS_VOICE_RA3_FILE_NAME");

    }else{
      throw new IllegalStateException("property file couldn't be loaded!");
    }
  }

  /**
   * @return the ksLineSepartor
   */
  public static final String getKsLineSepartor(){
    return ksLineSepartor;
  }

  /**
   * @return the csLineSepartor
   */
  public static final String getCsLineSepartor(){
    return csLineSepartor;
  }

  /**
   * @return the l4LineSepartor
   */
  public static final String getL4LineSepartor(){
    return l4LineSepartor;
  }

  /**
   * @return the l5LineSepartor
   */
  public static final String getL5LineSepartor(){
    return l5LineSepartor;
  }

  /**
   * @return the ksEncoding
   */
  public static final String getKsEncoding(){
    return ksEncoding;
  }

  /**
   * @return the csEncoding
   */
  public static final String getCsEncoding(){
    return csEncoding;
  }

  /**
   * @return the l4Encoding
   */
  public static final String getL4Encoding(){
    return l4Encoding;
  }

  /**
   * @return the l5Encoding
   */
  public static final String getL5Encoding(){
    return l5Encoding;
  }

  /**
   * @return the l4LookupEncoding
   */
  public static final String getL4LookupEncoding(){
    return l4LookupEncoding;
  }

  /**
   * @return the l5LookupEncoding
   */
  public static final String getL5LookupEncoding(){
    return l5LookupEncoding;
  }

  /**
   * @return the csInfoExtention
   */
  public static final String getCsInfoExtention(){
    return csInfoExtention;
  }

  /**
   * @return the csDataExtention
   */
  public static final String getCsDataExtention(){
    return csDataExtention;
  }

  /**
   * @return the csZipExtention
   */
  public static final String getCsZipExtention(){
    return csZipExtention;
  }

  /**
   * @return the csIndexExtention
   */
  public static final String getCsIndexExtention(){
    return csIndexExtention;
  }

  /**
   * @return the csSepartor
   */
  public static final String getCsSepartor(){
    return csSepartor;
  }

  /**
   * @return the csVoiceIndexName
   */
  public static final String getCsVoiceIndexName(){
    return csVoiceIndexName;
  }

  /**
   * @return the csVoiceZipName
   */
  public static final String getCsVoiceZipName(){
    return csVoiceZipName;
  }

  /**
   * @return the ksVoiceKey
   */
  public static final String getKsVoiceKey(){
    return ksVoiceKey;
  }

  /**
   * @return the ksVoiceKeyEncoding
   */
  public static final String getKsVoiceKeyEncoding(){
    return ksVoiceKeyEncoding;
  }

  /**
   * @return the ksVoiceIndexFile
   */
  public static final String getKsVoiceIndexFile(){
    return ksVoiceIndexFile;
  }

  /**
   * @return the ksVoiceRA4DataFile
   */
  public static final String getKsVoiceRA4DataFile(){
    return ksVoiceRA4DataFile;
  }

  /**
   * @return the ksVoiceRA3DataFile
   */
  public static final String getKsVoiceRA3DataFile(){
    return ksVoiceRA3DataFile;
  }

  /**
   * @return the kscsEncoding
   */
  public static final String getKscsEncoding(){
    return kscsEncoding;
  }

  /**
   * @return the ksLookupEncoding
   */
  public static final String getKsLookupEncoding(){
    return ksLookupEncoding;
  }

}
