/*****************************************************************************
 * 
 * @(#)PackageService.java  2009/03
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
package org.jin.dic.data.ldoce.v5;

import java.util.HashMap;
import java.util.Map;

import org.jin.dic.data.Environment;
import org.jin.dic.data.sk.FSFile;
import org.jin.dic.data.sk.FileSystem;


final class PackageService {

  private static Map idNameMap;
  static{
    idNameMap = new HashMap();
    idNameMap.put(new Integer(0), "entry");
    idNameMap.put(new Integer(1), "entryfld");
    idNameMap.put(new Integer(2), "content");
    idNameMap.put(new Integer(4), "verb");
    idNameMap.put(new Integer(5), "phrase");
    idNameMap.put(new Integer(6), "encorpex");
    idNameMap.put(new Integer(7), "phcorpex");
    idNameMap.put(new Integer(8), "prongb");
    idNameMap.put(new Integer(9), "pronus");
    idNameMap.put(new Integer(10), "preview");
    idNameMap.put(new Integer(11), "image");
    idNameMap.put(new Integer(12), "sound");
    idNameMap.put(new Integer(14), "htmlmap");
    idNameMap.put(new Integer(15), "imagemap");
    idNameMap.put(new Integer(16), "concept");
    idNameMap.put(new Integer(17), "cncptfld");
    idNameMap.put(new Integer(18), "section");
    idNameMap.put(new Integer(20), "commonerror");
    idNameMap.put(new Integer(21), "pattern");
    idNameMap.put(new Integer(22), "enlwaactlink");
    idNameMap.put(new Integer(23), "phlwaactlink");
    idNameMap.put(new Integer(24), "wordfamily");
    idNameMap.put(new Integer(25), "collocation");
    idNameMap.put(new Integer(26), "phrvb");
    idNameMap.put(new Integer(27), "lwaconcept");
  }
  private static boolean idLegal(int id){
    return idNameMap.containsKey(new Integer(id));
  }
  private static String getName(int id){
    return (String) idNameMap.get(new Integer(id));
  }

  private Map         idFsMap;
  private Environment environment = null;
  PackageService() {
  }

  void setSrcFolder(String srcFolder){
    if(environment == null) environment = new Environment();
    environment.setCurrentFolder(srcFolder);
  }

  private FileSystem getFS(int id){
    if(idFsMap == null) idFsMap = new HashMap();
    FileSystem fs = (FileSystem) idFsMap.get(new Integer(id));
    if(fs == null){
      fs = new FileSystem();
      fs.setEnvironment((Environment) environment.clone());
      fs.setConfigFileName(getName(id));
      idFsMap.put(new Integer(id), fs);
    }
    return fs;
  }

  public FSFile getFile(Avatar avatar){
    if(!idLegal(avatar.getPackageId())) return null;
    FSFile file = null;
    FileSystem fs = getFS(avatar.getPackageId());
    fs.bind();
    file = fs.getFile(avatar.getFileId());
    fs.unBind(false);
    return file;
  }

  public byte[] getData(Avatar avatar){
    byte[] data;
    FSFile file = getFile(avatar);
    if(file != null) data = file.getContent();
    else data = new byte[0];
    return data;
  }

}
