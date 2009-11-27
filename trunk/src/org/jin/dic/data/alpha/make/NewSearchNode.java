/*****************************************************************************
 * 
 * @(#)NewSearchNode.java  2009/11
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
package org.jin.dic.data.alpha.make;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jin.util.StringUtil;
import org.jin.util.io._DataOutput;
import org.jin.util.io._RandomAccessFile;

class NewSearchNode {
  static final int   MAXCHILDRENSIZE = 0xffff;
  static final int   MAXCHILDRENLEN  = 0xffff;

  private String     string          = "\0";
  private int        childrenSize    = 0;
  private int        value           = -1;
  private int        childrenOffset  = -1;
  private int        childrenLen     = -1;

  private NewSearchNode prarent         = null;
  private Map        childrenItemMap = null;

  int getLen(){
    return 1 + (string.length() << 1) + 2 + (childrenSize == 0 ? 1 + getLen(value) : 5);
  }
  int getChildrenCount(int level){
    return getChildrenCount(this, level);
  }
  int getChildrenLen(int level){
    return getChildrenLen(this, level);
  }
  List getList(){
    return getList(this);
  }
  void addBranchPath(String word, int value){
    addBranchPath(this, word, value);
  }
  void write(_DataOutput out) throws IOException{
    write(this, out);
  }
  void removeSingleChild(){
    removeSingleChild(this);
    List childrenList;
    Iterator ci;
    if(childrenSize > 0){
      ci = childrenItemMap.values().iterator();
      childrenList = new ArrayList();
      while(ci.hasNext())
        childrenList.add(ci.next());
      for(int i = 0; i < childrenList.size(); i++)
        ((NewSearchNode) childrenList.get(i)).removeSingleChild();
    }
  }
  void enlargeChildrenSize(){
    enlargeChildrenSize(this);
    if(childrenSize > 0){
      Iterator ti = childrenItemMap.values().iterator();
      while(ti.hasNext())
        ((NewSearchNode) ti.next()).enlargeChildrenSize();
    }
  }
  void sort(){
    sort(this);
    if(childrenSize > 0){
      Iterator ti = childrenItemMap.values().iterator();
      while(ti.hasNext())
        ((NewSearchNode) ti.next()).sort();
    }
  }

  private void addNode(NewSearchNode node){
    if(node == null) return;
    if(childrenItemMap == null) childrenItemMap = new LinkedHashMap();
    node.prarent = this;
    childrenItemMap.put(node.string, node);
    childrenSize = childrenItemMap.size();
  }
  private NewSearchNode getNode(String string){// if none in map, put a new one
    if(childrenItemMap == null) childrenItemMap = new LinkedHashMap();
    NewSearchNode child = (NewSearchNode) childrenItemMap.get(string);
    if(child == null){
      child = new NewSearchNode();
      child.string = string;
      child.prarent = this;
      childrenItemMap.put(child.string, child);
      childrenSize = childrenItemMap.size();
    }
    return child;
  }
  private static void addBranchPath(NewSearchNode node, String word, int value){
    NewSearchNode temp = node;
    int len = word.length();
    for(int i = 0; i <= len; i++){
      temp = temp.getNode(i == len ? "\0" : word.substring(i, i + 1));
      if(i == len && temp.value == -1) temp.value = value;
    }
  }
  private static int getChildrenLen(NewSearchNode node, int level){
    int len = 0;
    if(level == 0){
      len = node.getLen();
    }else if(node.childrenSize > 0){
      Iterator i = node.childrenItemMap.values().iterator();
      NewSearchNode child;
      while(i.hasNext()){
        child = (NewSearchNode) i.next();
        if(child.childrenSize != 0){
          if(level != 1){
            len += ((child.string.length() << 1) * getChildrenCount(child, level - 1));
          }
          len += getChildrenLen(child, level - 1);
        }else{
          len += child.getLen();
        }
      }
    }
    return len;
  }
  private static int getChildrenCount(NewSearchNode node, int level){
    int count = 0;
    if(level == -1){
      if(node.childrenSize == 0) count = 1;
      else{
        Iterator i = node.childrenItemMap.values().iterator();
        NewSearchNode child;
        while(i.hasNext()){
          child = (NewSearchNode) i.next();
          count += getChildrenCount(child, level);
        }
      }
    }else{
      if(level == 0 || node.childrenSize == 0) count = 1;
      else if(node.childrenSize > 0){
        Iterator i = node.childrenItemMap.values().iterator();
        NewSearchNode child;
        while(i.hasNext()){
          child = (NewSearchNode) i.next();
          count += getChildrenCount(child, level - 1);
        }
      }
    }
    return count;
  }
  private static void removeSingleChild(NewSearchNode node){
    if(node.prarent != null && node.childrenSize == 1){
      NewSearchNode child;
      while(node.childrenSize == 1){
        child = (NewSearchNode) node.childrenItemMap.values().iterator().next();
        node.string = append(node.string, child.string);
        node.childrenItemMap.clear();
        node.childrenSize = 0;
        if(child.childrenSize > 0){
          List grandChildrenList = new ArrayList();
          Iterator ti = child.childrenItemMap.values().iterator();
          while(ti.hasNext())
            grandChildrenList.add((NewSearchNode) ti.next());
          for(int i = 0; i < grandChildrenList.size(); i++)
            node.addNode((NewSearchNode) grandChildrenList.get(i));
        }else node.value = child.value;
      }
      NewSearchNode parent = node.prarent;
      if(parent != null){
        List brotherList = new ArrayList();
        Iterator ni = parent.childrenItemMap.values().iterator();
        while(ni.hasNext())
          brotherList.add((NewSearchNode) ni.next());
        parent.childrenItemMap.clear();
        parent.childrenSize = 0;
        parent.childrenOffset = -1;
        for(int i = 0; i < brotherList.size(); i++)
          parent.addNode((NewSearchNode) brotherList.get(i));
      }
    }
  }
  private static void enlargeChildrenSize(NewSearchNode node){
    if(node.childrenSize > 0){
      NewSearchNode child, grandChild, temp;
      List childrenList, brotherList;
      Iterator ni, ci, gi;
      int pos, count;
      int ifDoneChildrenSize, ifDoneChildrenLen;

      ci = node.childrenItemMap.values().iterator();
      childrenList = new ArrayList();
      while(ci.hasNext())
        childrenList.add(ci.next());
      for(int i = 0; i < childrenList.size(); i++){
        child = (NewSearchNode) childrenList.get(i);
        if(child.childrenSize > 0){
          ifDoneChildrenSize = node.childrenSize + child.childrenSize - 1;
          ifDoneChildrenLen = node.getChildrenLen(1) + child.getChildrenLen(1) - child.getLen() + (child.childrenSize * child.string.length() << 1);
          if(ifDoneChildrenSize > MAXCHILDRENSIZE || ifDoneChildrenLen > MAXCHILDRENLEN) continue;

          // TODO controls the conditions that the children should be enlarged
          if(child.childrenSize * child.string.length() << 1 > child.getLen()) continue;
          // if(child.childrenSize > 10) continue;
          // if(child.childrenSize - 1 + node.childrenSize > 50) continue;

          brotherList = new ArrayList();
          ni = node.childrenItemMap.values().iterator();
          pos = 0;
          count = 0;
          while(ni.hasNext()){
            temp = (NewSearchNode) ni.next();
            brotherList.add(temp);
            if(temp == child) pos = count;
            count++;
          }
          node.childrenItemMap.clear();
          node.childrenSize = 0;
          node.childrenOffset = -1;
          for(int j = 0; j < brotherList.size(); j++){
            if(j != pos){
              node.addNode((NewSearchNode) brotherList.get(j));
            }else{
              gi = child.childrenItemMap.values().iterator();
              while(gi.hasNext()){
                grandChild = (NewSearchNode) gi.next();
                grandChild.string = append(child.string, grandChild.string);
                node.addNode(grandChild);
              }
            }
          }
        }
      }
    }
  }
  private static void getList(NewSearchNode node, List list){
    if(node.childrenSize > 0){
      Iterator ti = node.childrenItemMap.values().iterator();
      while(ti.hasNext())
        list.add(ti.next());
      ti = node.childrenItemMap.values().iterator();
      while(ti.hasNext())
        getList((NewSearchNode) ti.next(), list);
    }
  }
  private static List getList(NewSearchNode node){
    List list = new ArrayList();
    list.add(node);
    getList(node, list);
    NewSearchNode temp, parent;
    int pos = 0;
    for(int i = 0; i < list.size(); i++){
      temp = (NewSearchNode) list.get(i);
      parent = temp.prarent;
      if(parent != null){
        if(parent.childrenOffset == -1) parent.childrenOffset = pos;
        parent.childrenLen = pos + temp.getLen() - parent.childrenOffset;
      }
      pos += temp.getLen();
    }
    return list;
  }
  private static void write(NewSearchNode node, _DataOutput out) throws IOException{
    List list = getList(node);
    NewSearchNode temp;
    int valueLen;
    for(int i = 0; i < list.size(); i++){
      temp = (NewSearchNode) list.get(i);
      out.write(temp.string.length());
      out.write(StringUtil.getBytesNoBom(temp.string, "unicode"));
      out.writeChar(temp.childrenSize);
      if(temp.childrenSize != 0){
        out.write24(temp.childrenOffset);
        out.writeChar(temp.childrenLen);
      }else{
        valueLen = getLen(temp.value);
        out.write(valueLen);
        switch(valueLen){
          case 0:
            break;
          case 1:
            out.write(temp.value);
            break;
          case 2:
            out.writeChar(temp.value);
            break;
          case 3:
            out.write24(temp.value);
            break;
          case 4:
            out.writeInt(temp.value);
            break;
        }
      }
    }
  }
  private static void sort(NewSearchNode node){
    Iterator ti;
    NewSearchNode[] childrer = new NewSearchNode[node.childrenSize];
    int count = 0;
    if(node.childrenSize > 0){
      ti = node.childrenItemMap.values().iterator();
      while(ti.hasNext()){
        childrer[count++] = (NewSearchNode) ti.next();
      }
      node.childrenItemMap.clear();
      node.childrenSize = 0;
    }

    Arrays.sort(childrer, new Comparator() {
      public int compare(Object o1, Object o2){
        NewSearchNode w1 = (NewSearchNode) o1;
        NewSearchNode w2 = (NewSearchNode) o2;
        int count1 = w1.getChildrenCount(-1);
        int count2 = w2.getChildrenCount(-1);
        return count1 == count2 ? w2.getLen() - w1.getLen() : count2 - count1;
      }
    });
    for(int i = 0; i < childrer.length; i++){
      node.addNode(childrer[i]);
    }
  }
  private String getPadded(String string, int len){
    StringBuffer s = new StringBuffer(string);
    while(s.length() < len)
      s.insert(0, " ");
    return s.toString().toUpperCase(Locale.ENGLISH);
  }
  private static String append(String s1, String s2){
    StringBuffer temp = new StringBuffer();
    temp.append(s1);
    temp.append(s2);
    return temp.toString();
  }
  private static int getLen(int value){
    int len = 4;
    while((value & 0xff000000) == 0 && len > 0){
      value = value << 8;
      len--;
    }
    return len;
  }
  public String toString(){
    StringBuffer s = new StringBuffer();
    s.append("\"");
    s.append(string);
    s.append("\"(");
    s.append(getPadded(Integer.toHexString(childrenSize), 2));
    s.append(")_[");
    s.append(getPadded(Integer.toHexString(value == -1 ? childrenOffset : value), 8));
    s.append("]");
    return s.toString();
  }
  public static void main(String[] args) throws IOException{
    NewSearchNode root = new NewSearchNode();
    root.addBranchPath("123a", 0x01);
    root.addBranchPath("123aaa", 0x02);
    root.addBranchPath("123aaalooong", 0x03);
    root.addBranchPath("123aab", 0x04);
    root.addBranchPath("123ab", 0x05);
    root.addBranchPath("123ba", 0x06);
    root.addBranchPath("123baa", 0x07);
    root.addBranchPath("123bb", 0x08);
    root.addBranchPath("123c", 0x09);
    root.addBranchPath("123AaA", 0x0a);

    root.removeSingleChild();
    root.enlargeChildrenSize();
    System.out.println(getChildrenCount(root, 2));
    System.out.println(getChildrenLen(root, 1));

    String desFileName = "D:/Jin/Alpha/tmp/test.srh";
    File file = new File(desFileName);
    if(file.exists()) file.delete();
    _RandomAccessFile f = new _RandomAccessFile(desFileName, "rw", true);
    root.write(f);
    f.close();
  }

}
