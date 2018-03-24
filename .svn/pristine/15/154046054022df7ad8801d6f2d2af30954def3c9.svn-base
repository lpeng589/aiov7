package com.menyi.web.util;

import java.util.ArrayList;
import java.util.List;
import java.text.Collator;
import java.util.Arrays;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class SortUtil {
    public SortUtil() {
    }
    public static List<String[]> getSortList(List <String[]> list) {
        List<String[]> newlist = new ArrayList<String[]>();
        List<String> displayName = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {

            displayName.add(list.get(i)[0]);
        }
        Object[] strs = displayName.toArray();
        Collator d = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(strs, d);
        for (int i = 0; i < strs.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (strs[i].equals(list.get(j)[0])) {
                    newlist.add(list.get(j));
                }
            }
        }
        return newlist;

    }
    public static List<Object[]> getPropSortList(List <Object[]> list) {
       List<Object[]> newlist = new ArrayList<Object[]>();
       List<String> displayName = new ArrayList<String>();
       for (int i = 0; i < list.size(); i++) {

           displayName.add(list.get(i)[1].toString());
       }
       Object[] strs = displayName.toArray();
       Collator d = Collator.getInstance(java.util.Locale.CHINA);
       Arrays.sort(strs, d);
       for (int i = 0; i < strs.length; i++) {
           for (int j = 0; j < list.size(); j++) {
               if (strs[i].equals(list.get(j)[1])) {
                   newlist.add(list.get(j));
               }
           }
       }
       return newlist;

   }

    public static void main(String[] args) {
       List<String[]> list= new ArrayList<String[]>();
       String[] strs1={"a","韩国","1"};
       String[] strs2={"e","阿宝","3"};
       String[] strs3={"c","大哥","0"};
       list.add(strs1);
       list.add(strs2);
       list.add(strs3);
      List<String[]> newlist=getSortList(list);
      for(int i=0;i<newlist.size();i++)
      {
          System.out.println(newlist.get(i)[0]);
      }
    }
}
