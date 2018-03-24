package com.koron.oa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import org.apache.commons.lang.time.DateUtils;

import com.menyi.aio.bean.BaseDateFormat;

public class DateUtil {
	// ��þ���ʱ������һ������
	public String getMondayBySelTime(String selTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int mondayPlus = this.getMondayPlus(selTime);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(sdf.parse(selTime));
		currentDate.add(GregorianCalendar.DATE, mondayPlus);

		Date monday = currentDate.getTime();
		// DateFormat df = DateFormat.getDateInstance();
		String preMonday = sdf.format(monday);
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

	/*
	 * ��ȡ����һʱ��
	 */
	public static Date getLastWeekMonday(Date date) {
		Date a = DateUtils.addDays(date, -1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(a);
		cal.add(Calendar.WEEK_OF_YEAR, -1);// һ��
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return cal.getTime();
	}

	/* ��ȡ������ʱ�� */
	public static Date getLastWeekSunday(Date date) {

		Date a = DateUtils.addDays(date, -1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(a);
		cal.set(Calendar.DAY_OF_WEEK, 1);

		return cal.getTime();
	}

	/**
	 * @author jerry.chen
	 * @param calendar
	 * @return ��ȡ��ǰ�����ڼ�;
	 */
	public static String getCurrentWeekOfMonth(Calendar calendar) {
		String strWeek = "";
		int dw = calendar.get(Calendar.DAY_OF_WEEK);
		if (dw == 1) {
			strWeek = "������";
		} else if (dw == 2) {
			strWeek = "����һ";
		} else if (dw == 3) {
			strWeek = "���ڶ�";
		} else if (dw == 4) {
			strWeek = "������";
		} else if (dw == 5) {
			strWeek = "������";
		} else if (dw == 6) {
			strWeek = "������";
		} else if (dw == 7) {
			strWeek = "������";
		}
		return strWeek;
	}

	/**
	 * ��ȡ��һ������
	 * @param selectDate
	 * @return
	 */
	public static String[] getMondayAndSunday(String selectDate){
		String monday = "";
		Calendar ca = Calendar.getInstance();
		try {
			monday = new DateUtil().getMondayBySelTime(selectDate);
			ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ca.add(Calendar.DAY_OF_MONTH,6);
		String sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//�������
		String str[] = new String[2];
		str[0]=monday;
		str[1]=sunday;
		return str;
	}
	
	/**
	 * ���ݵ��µ�ĳһ���ȡ���µĿ�ʼ���������ں�����
	 * @param dateTime
	 * @return
	 */
	public static String[] getMonthByDate(String dateTime){
		String[] str = new String[3];
		Calendar ca = Calendar.getInstance();
		try {
			ca.setTime(BaseDateFormat.parse(dateTime, BaseDateFormat.yyyyMMdd));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ca.set(Calendar.DATE, 1);
		str[0] = BaseDateFormat.format(ca.getTime(), BaseDateFormat.yyyyMMdd);
		int dayNum = ca.getActualMaximum(Calendar.DAY_OF_MONTH);  
		str[2] = String.valueOf(dayNum);
		ca.set(Calendar.DAY_OF_MONTH,dayNum);
		str[1] = BaseDateFormat.format(ca.getTime(), BaseDateFormat.yyyyMMdd);		
		return str;
	}
}
