package com.koron.crm.report;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	/**
	 * ����������·ݻ�ñ����ȵĵ�һ��ʱ�������һ��ʱ��
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getSeasonStartDayAndLastDay(int year,int month) {
		int array[][] = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int start_month = array[season - 1][0];
		int end_month = array[season - 1][2];

//		Date date = new Date();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// ���Է�����޸����ڸ�ʽ
//		String years = dateFormat.format(date);
		int years_value = year;

		int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
		int end_days = getLastDayOfMonth(years_value, end_month);
		String seasonDate = years_value+ "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;
		return seasonDate;

	}
	
	/**
	 * ��ȡĳ��ĳ�µ����һ��
	 * @param year ��
	 * @param month ��
	 * @return ���һ��
	 */
	private static int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}
	
	/**
	 * �Ƿ�����
	 * @param year ��
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}
	
	/**
	 * ��ȡʱ��ε�����Ŀ
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getWeekCount(String startTime,String endTime){
	     Calendar c_begin = new GregorianCalendar();
	     Calendar c_end = new GregorianCalendar();
	     DateFormatSymbols dfs = new DateFormatSymbols();
	     String[] weeks = dfs.getWeekdays();
	     c_begin.set(Integer.parseInt(startTime.substring(0,4)),Integer.parseInt(startTime.substring(startTime.indexOf("-")+1,startTime.lastIndexOf("-"))),Integer.parseInt(startTime.substring(startTime.lastIndexOf("-")+1,startTime.length()))); //Calendar���´�0-11������4����3.
	     c_end.set(Integer.parseInt(endTime.substring(0,4)),Integer.parseInt(endTime.substring(endTime.indexOf("-")+1,endTime.lastIndexOf("-"))),Integer.parseInt(endTime.substring(endTime.lastIndexOf("-")+1,endTime.length()))); //Calendar���´�0-11������5����4.
	     
	     int count = 1;
	     c_end.add(Calendar.DAY_OF_YEAR, 1);  //���������¹�һ����Ϊ�˰������һ��
	    
	     while(c_begin.before(c_end)){
	      //System.out.println("��"+count+"��  ���ڣ�"+new java.sql.Date(c_begin.getTime().getTime())+","+weeks[c_begin.get(Calendar.DAY_OF_WEEK)]); 

	      if(c_begin.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
	          count++;
	      }
	      c_begin.add(Calendar.DAY_OF_YEAR, 1);
	     }
	    return count;
	} 
	
//	 ��ȡ���µ�һ��
	public static String getFirstDayOfMonth(int year,int month) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(year, month-1,1);
		lastDate.set(Calendar.DATE, 1);// ��Ϊ��ǰ�µ�1��
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	// ���㵱�����һ��,�����ַ���
	public static String getLastDateMonth(int year,int month) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(year, month-1,1);
		lastDate.set(Calendar.DATE, 1);// ��Ϊ��ǰ�µ�1��
		lastDate.add(Calendar.MONTH, 1);// ��һ���£���Ϊ���µ�1��
		lastDate.add(Calendar.DATE, -1);// ��ȥһ�죬��Ϊ�������һ��

		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * �õ��������ڼ�ļ������
	 */
	public static long getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return 0;
		}
		return day ;
	}
	
//	 ��ñ����һ�������
	public static String getCurrentYearFirst() {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		int yearPlus = getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus);
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = myFormatter.format(yearDay);
		return preYearDay;
	}
	
	public static int getYearPlus() {
		Calendar cd = Calendar.getInstance();
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// ��õ�����һ���еĵڼ���
		cd.set(Calendar.DAY_OF_YEAR, 1);// ��������Ϊ�����һ��
		cd.roll(Calendar.DAY_OF_YEAR, -1);// �����ڻع�һ�졣
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		if (yearOfNumber == 1) {
			return -MaxYear;
		} else {
			return 1 - yearOfNumber;
		}
	}
	
//	 ��ȡ����ʱ��
	public static String getNowTime(String dateformat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// ���Է�����޸����ڸ�ʽ
		String hehe = dateFormat.format(now);
		return hehe;
	}
	
	 /** 
     * @param date1 ��Ҫ�Ƚϵ�ʱ�� ����Ϊ��(null),��Ҫ��ȷ�����ڸ�ʽ ,�磺2009-09-12 
     * @param date2 ���Ƚϵ�ʱ��  Ϊ��(null)��Ϊ��ǰʱ��  
     * @param stype ����ֵ����   0Ϊ�����죬1Ϊ���ٸ��£�2Ϊ������  
     * @return  
     * ������
     * compareDate("2009-09-12", null, 0);//�Ƚ���
     * compareDate("2009-09-12", null, 1);//�Ƚ���
     * compareDate("2009-09-12", null, 2);//�Ƚ���
     */ 
   public static int compareDate(String startDay,String endDay,int stype){  
       int n = 0;  
       String[] u = {"��","��","��"};  
       String formatStyle = stype==1?"yyyy-MM":"yyyy-MM-dd";  
       endDay = endDay==null?getCurrentDate("yyyy-MM-dd"):endDay;  
       DateFormat df = new SimpleDateFormat(formatStyle);  
       Calendar c1 = Calendar.getInstance();  
       Calendar c2 = Calendar.getInstance();  
       try {  
           c1.setTime(df.parse(startDay));  
           c2.setTime(df.parse(endDay));  
       } catch (Exception e3) {  
           System.out.println("wrong occured");  
       }  
       //List list = new ArrayList();  
       while (!c1.after(c2)) {                   // ѭ���Աȣ�ֱ����ȣ�n ������Ҫ�Ľ��  
           //list.add(df.format(c1.getTime()));    // ������԰Ѽ�������ڴ浽������ ��ӡ����  
           n++;  
           if(stype==1){  
               c1.add(Calendar.MONTH, 1);          // �Ƚ��·ݣ��·�+1  
           }  
           else{  
               c1.add(Calendar.DATE, 1);           // �Ƚ�����������+1  
           }  
       }  
       n = n-1;  
       if(stype==2){  
           n = (int)n/365;  
       }     
       System.out.println(startDay+" -- "+endDay+" ������"+u[stype]+":"+n);        
       return n;  
   }   

   
   public static String getCurrentDate(String format){
	   Calendar day=Calendar.getInstance(); 
	   day.add(Calendar.DATE,0); 
	   SimpleDateFormat sdf=new SimpleDateFormat(format);//"yyyy-MM-dd"
	   String date = sdf.format(day.getTime());
	   return date;
   } 
   
   /**
    * ����ѡ��ʱ���ȡ������Ϣ��:2012-02--2012��Q1
    * @param selTime
    * @return
    */
   public static String getSeasonInfo(String selTime){
	   Calendar day=Calendar.getInstance(); 
	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//"yyyy-MM-dd"
	   try {
		   day.setTime(sdf.parse(selTime));
	   } catch (ParseException e) {
		   e.printStackTrace();
	   }
	   //System.out.println(sdf.format(day.getTime()));
	   int year = day.get(Calendar.YEAR);
	   int month = day.get(Calendar.MONTH)+1;
	   String str = year +"��"; 
	   if(month == 1 || month ==2 || month ==3){
		   str+="Q1";
	   }else if(month == 4 || month ==5 || month ==6){
		   str+="Q2";
	   }else if(month == 7 || month ==8 || month ==9){
		   str+="Q3";
	   }else if(month == 10 || month ==11 || month ==12){
		   str+="Q4";
	   }   
	   return str;
   } 
   
   // ��þ���ʱ������һ������
   public String getMondayBySelTime(String selTime) throws ParseException {
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	   int mondayPlus = this.getMondayPlus(selTime);
       GregorianCalendar currentDate = new GregorianCalendar();
       currentDate.setTime(sdf.parse(selTime));
       currentDate.add(GregorianCalendar.DATE, mondayPlus);
       
       
       
       Date monday = currentDate.getTime();
       DateFormat df = DateFormat.getDateInstance();
       String preMonday = df.format(monday);
       return preMonday;
   }

   // ��õ�ǰ�����뱾��һ��������
   private int getMondayPlus(String selTime) throws ParseException {
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
       Calendar cd = Calendar.getInstance();
       cd.setTime(sdf.parse(selTime));
       // ��ý�����һ�ܵĵڼ��죬�������ǵ�һ�죬���ڶ��ǵڶ���......
       int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
       if (dayOfWeek == 1) {
           return -6;
       } else {
           return 2 - dayOfWeek;
       }
   }

}
