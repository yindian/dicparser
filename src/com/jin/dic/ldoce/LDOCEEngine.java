/*****************************************************************************
 * 
 * @(#)LDOCEEngine.java  2009/03
 *
 *  Copyright (C) 2009  Tim Bron<jinxingquan@google.com>
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
package com.jin.dic.ldoce;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.jin.dic.ConvertListener;
import com.jin.dic.Engine;
import com.jin.dic.Environment;
import com.jin.dic.sk.FSFile;
import com.jin.dic.sk.Table;
import com.jin.dic.sk.i.IRecord;
import com.jin.dic.sk.i.IRecordSet;
import com.jin.util.StringUtil;

public class LDOCEEngine implements Engine {

  protected Environment    environment = null;
  protected PackageService service     = null;
  protected Table          doc         = null;
  protected Table          hwd         = null;

  public LDOCEEngine() {
  }

  public void setSrcFolder(String srcFolder){
    if(environment == null) environment = new Environment();
    environment.setCurrentFolder(srcFolder);
    ini();
    load();
  }
  protected void ini(){
    doc = new Table();
    hwd = new Table();
  }
  protected void load(){
    doc.setEnvironment((Environment) environment.clone());
    doc.setConfigFileName("index/doc");
    hwd.setEnvironment((Environment) environment.clone());
    hwd.setConfigFileName("index/hwd");
  }

  public void addConverListener(ConvertListener listener){
  }

  public void convert(String desFld) throws FileNotFoundException{
  }

  public List getAvatars(String word){
    doc.bind();
    hwd.bind();

    List avatars = new ArrayList();
    int tid = -1;
    int cat = -1;
    byte[] bytes = StringUtil.getBytes(word, "UTF-8");

    IRecord hwdRecord, hwdStRecord, docRecord, docStRecord;
    IRecordSet hwdSt, docSt;
    Avatar avatar;

    cat = hwd.lookUpNum(getIntWordKey(word), null);
    if(cat == -1) return avatars;
    hwdRecord = hwd.getRecord(cat);
    hwdSt = hwdRecord.getLinkFieldValue("r_target");
    for(int i = 0; i < hwdSt.getRecordCount(); i++){
      hwdStRecord = hwdSt.getRecord(i);
      tid = (int) hwdStRecord.getNumFieldValue("targetid");
      docRecord = doc.getRecord(tid);
      if(StringUtil.equals(bytes, docRecord.getDataFieldValue("doc"), true)){
        docSt = docRecord.getLinkFieldValue("r_avatar");
        for(int j = 0; j < docSt.getRecordCount(); j++){
          docStRecord = docSt.getRecord(j);
          avatar = new Avatar();
          avatar.setPType((int) docStRecord.getNumFieldValue("ptype"));
          avatar.setFType((int) docStRecord.getNumFieldValue("ftype"));
          avatar.setPackageId((int) docStRecord.getNumFieldValue("packageid"));
          avatar.setFileId((int) docStRecord.getNumFieldValue("fileid"));
          avatars.add(avatar);
        }
        break;
      }else tid = -1;
    }

    doc.unBind(false);
    hwd.unBind(false);
    return avatars;
  }

  public FSFile[] getFile(String word){
    if(service == null){
      service = new PackageService();
      service.setSrcFolder(environment.getCurrentFolder() + "/package/");
    }
    List avatars = getAvatars(word);
    FSFile[] files = new FSFile[avatars.size()];
    for(int i = 0; i < avatars.size(); i++){
      files[i] = service.getFile((Avatar) avatars.get(i));
    }
    return files;
  }

  public byte[] getData(String word){
    if(service == null){
      service = new PackageService();
      service.setSrcFolder(environment.getCurrentFolder() + "/package/");
    }
    List avatars = getAvatars(word);
    for(int i = 0; i < avatars.size(); i++){
      System.out.print(i);
      System.out.print(" ");
      System.out.println(service.getData((Avatar) avatars.get(i)).length);
    }
    System.out.println();
    return null;
  }

  private synchronized static int getIntWordKey(String word){
    byte[] b = StringUtil.getBytes(word.toLowerCase(Locale.ENGLISH), "ISO-8859-1");
    return (b[0] & 0xff) << 16 | (b.length < 2 ? 0x20 : (b[1] & 0xff));
  }

}
