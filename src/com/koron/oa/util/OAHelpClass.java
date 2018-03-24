package com.koron.oa.util;

public class OAHelpClass {

    public String toString(Object obj){
      if(!"".equals(obj)&&null!=obj){
         return obj.toString();
        }
       return null;
    }
}
