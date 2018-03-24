package com.menyi.aio.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.*;

/**
 * <p>Title: 日期出理类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class BaseDateFormat {
    public static final String yyyyMMdd="yyyy-MM-dd";
    public static final String yyyyMMddHHmmss="yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmmss2 = "yyyyMMddHHmmss" ;
    
    public static SimpleDateFormat sd = new SimpleDateFormat(yyyyMMdd);
    public static SimpleDateFormat sh = new SimpleDateFormat(yyyyMMddHHmmss);
    public static SimpleDateFormat shh = new SimpleDateFormat(yyyyMMddHHmmss2) ;



    public static Date parse(String str,String type) throws IllegalArgumentException,ParseException
    {
        if(str==null||str.length()==0)
        {
            return null;
        }
        if (type == null ||
            (!type.endsWith(yyyyMMdd) && !type.endsWith(yyyyMMddHHmmss) && !type.endsWith(yyyyMMddHHmmss2))) {
            throw new IllegalArgumentException(
                    "Date Type is not yyyyMMdd or yyyyMMddHHmmss or MMddHHmmss");
        }
        if(type.equals(yyyyMMdd))
        {
            return sd.parse(str);
        }else if(type.equals(yyyyMMddHHmmss)){
            return sh.parse(str);
        }else if(type.equals(yyyyMMddHHmmss2)){
       	 	return shh.parse(str) ;
        }else{
            return null;
        }
    }
    
    public static String getNowTimeLong(){
    	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
    }
    public static String getNowTime(){
    	return format(new Date(),yyyyMMddHHmmss);    	
    }
    
    public static String format(Date date,String type)
    {
        if(date==null)
         {
             return null;
         }
         if (type == null ||
             (!type.endsWith(yyyyMMdd) && !type.endsWith(yyyyMMddHHmmss) && !type.endsWith(yyyyMMddHHmmss2))) {
             throw new IllegalArgumentException(
                     "Date Type is not yyyyMMdd or yyyyMMddHHmmss or MMddHHmmss");
         }
         if(type.equals(yyyyMMdd)){
             return sd.format(date);
         }else if(type.equals(yyyyMMddHHmmss)){
             return sh.format(date);
         }else if(type.equals(yyyyMMddHHmmss2)){
        	 return shh.format(date) ;
         }else{
             return null;
         }
    }

//    public static String conver8to10(String str){
//        if (str == null ||str.length() == 0) {
//            return "";
//        }else{
//            return str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6);
//        }
//    }
//
//    public static String conver10to8(String str) {
//        if (str == null || str.length() == 0) {
//            return "";
//        } else {
//            return str.replaceAll("-","");
//        }
//    }
//
//    public static String conver14to19(String str) {
//        if (str == null || str.length() == 0) {
//            return "";
//        } else {
//            if(str.length() == 8){
//                str = str+"000000";
//            }
//            return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6,8)+
//                " " + str.substring(8,10)+":" + str.substring(10,12)+":" + str.substring(12);
//        }
//    }
//
//    public static String conver19to14(String str) {
//        if (str == null || str.length() == 0) {
//            return "";
//        } else {
//            str = str.replaceAll("-","");
//            str = str.replaceAll(" ","");
//            str = str.replaceAll(":","");
//            if(str.length()==8){
//                str+="000000";
//            }
//            return str;
//        }
//    }


}
