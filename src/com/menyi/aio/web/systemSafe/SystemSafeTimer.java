package com.menyi.aio.web.systemSafe;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.optimize.OptimizeMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConnectionEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.SystemState;

public class SystemSafeTimer {
	static SystemSafeMgt mgt = new SystemSafeMgt();
	static OptimizeMgt optmgt = new OptimizeMgt();
	public static Timer timer = new Timer();
	
	public static String backPath=""; 
	public static long backDay=0;

	public static boolean init() {
		
		
		Result rs = mgt.querySafeValues();
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return false;			
		}
		timer.cancel();
		timer = new Timer();
		HashMap<String,String> map = (HashMap)rs.retVal;
		
		
		String hour1 = map.get("hour1"); //���ݱ��ݵ�һʱ��
		String mult1 = map.get("mult1"); //���ݱ��ݵ�һ����
		String hour2 = map.get("hour2"); //���ݱ��ݵڶ�ʱ��
		String mult2 = map.get("mult2"); //���ݱ��ݵڶ�����
		if(map.get("backDay") != null && !map.get("backDay").equals("") ){ //���ݱ��ݱ�������
			try{
			backDay = Long.parseLong(map.get("backDay"));
			}catch(Exception ee){}
		}
		backPath = map.get("backPath"); //���ݱ��ݱ���·��
		if(backPath==null || backPath.length()  == 0){
			String userDir = System.getProperty("user.dir");
			String defDisk = userDir.substring(0,userDir.indexOf(":")+1);
			backPath = defDisk+"\\AioDefDbBakup";
		}
		String dp =map.get("dayPiece"); //��Ƭ��������;
		String[] dayPiece = dp==null?new String[]{}:dp.split(",");
		String hourPiece = map.get("hourPiece"); //��Ƭ����ʱ��
		String multPiece = map.get("multPiece"); //��Ƭ�������
		
		String lastEBak = map.get("lastEBak"); //��󱸷�ʱ�䣬�������û�б��ݣ�������ִ�б��ݣ�����е��б��ݣ���ִ��
		boolean todayback= true;
		if(!BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd).equals(lastEBak)){
			todayback = false;
		}
		
		Calendar b1 = Calendar.getInstance();		
		b1.set(Calendar.HOUR_OF_DAY, hour1==null || hour1.length() ==0 ?0: Integer.parseInt(hour1));
		b1.set(Calendar.MINUTE, mult1==null || mult1.length() ==0 ?0: Integer.parseInt(mult1));
		b1.set(Calendar.SECOND, 0);
		if(todayback && b1.getTime().before(new Date())){
			b1.set(Calendar.DATE, b1.get(Calendar.DATE)+1);
		}
		Date d1 = b1.getTime(); //���ݱ��ݵ�һʱ��
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				getdbback();
			};
		}, d1, 24*60*60000);
		BaseEnv.log.info("SAFE ���ݿ��һ����������������ʱ��"+ BaseDateFormat.format(d1, BaseDateFormat.yyyyMMddHHmmss));
		if(hour2 != null && hour2.length()>0){
			Calendar b2 = Calendar.getInstance();
			b2.set(Calendar.HOUR_OF_DAY, hour2==null || hour2.length() ==0 ?0: Integer.parseInt(hour2));
			b2.set(Calendar.MINUTE, mult2==null || mult2.length() ==0 ?0: Integer.parseInt(mult2));
			b2.set(Calendar.SECOND, 0);			
			if(todayback && b2.getTime().before(new Date())){
				b2.set(Calendar.DATE, b2.get(Calendar.DATE)+1);
			}
			Date d2 = b2.getTime(); //���ݱ��ݵڶ�ʱ��
			timer.scheduleAtFixedRate(new TimerTask(){
				public void run(){
					getdbback();
				};
			}, d2, 24*60*60000);
			BaseEnv.log.info("SAFE ���ݿ�ڶ�����������������ʱ��"+ BaseDateFormat.format(d2, BaseDateFormat.yyyyMMddHHmmss));
		}
		String lastEPiece = map.get("lastEPiece"); //��󱸷�ʱ�䣬�������û�б��ݣ�������ִ�б��ݣ�����е��б��ݣ���ִ��
		boolean todaypie= true;
		if(!BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd).equals(lastEPiece)){
			todaypie = false;
		}
		for(String weekd :dayPiece){
			if(weekd.length() > 0){
				Calendar b2 = Calendar.getInstance();
				int td = b2.get(Calendar.DAY_OF_WEEK)-1;
				td = Integer.parseInt(weekd) - td; 
				if(td<0) {
					td += 7;
				}
				b2.set(Calendar.DATE, b2.get(Calendar.DATE)+td);				
				b2.set(Calendar.HOUR_OF_DAY, hourPiece==null || hourPiece.length() ==0 ?0: Integer.parseInt(hourPiece));
				b2.set(Calendar.MINUTE, multPiece==null || multPiece.length() ==0 ?0: Integer.parseInt(multPiece));
				b2.set(Calendar.SECOND, 0);
				if(todayback && b2.getTime().before(new Date())){
					b2.set(Calendar.DATE, b2.get(Calendar.DATE)+7);
				}
				Date d2 = b2.getTime(); //���ݱ��ݵڶ�ʱ��
				timer.scheduleAtFixedRate(new TimerTask(){
					public void run(){
						BaseEnv.log.info("SAFE ��Ƭ����ʼִ�У�ʱ��"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
						optmgt.indexFragmentation();
						
						mgt.updateSafeValue("lastEPiece",BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
						
						BaseEnv.log.info("SAFE ��Ƭ�������ִ�У�ʱ��"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					};
				}, d2, 7*24*60*60000);
				BaseEnv.log.info("SAFE ��Ƭ����������ʱ��"+ BaseDateFormat.format(d2, BaseDateFormat.yyyyMMddHHmmss));
			}
		}
		
		return true;
	}
	public static void getdbback(){
		BaseEnv.log.info("SAFE ��������ʼִ�У�ʱ��"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		
		Result rs = SysAccMgt.backupDataBase(backPath);
		if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
			BaseEnv.log.info("SAFE ��������ִ�гɹ���ʱ��"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			mgt.updateSafeValue("lastEBak",BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
			SystemState.bakState = ErrorCanst.DEFAULT_SUCCESS;
			//ɾ����������
			if(backDay > 0){
				File path = new File(backPath);
				File fs[] =path.listFiles();
				for(File file :fs){
					String fn = file.getName();
					if((fn.indexOf(".bak")> 0 || fn.indexOf(".zip")> 0) && fn.indexOf("_")>0){
						try {
							String date = fn.substring(fn.lastIndexOf("_")+1,fn.lastIndexOf("."));
							Date d = BaseDateFormat.parse(date, BaseDateFormat.yyyyMMddHHmmss2);
							if((new Date().getTime() - d.getTime()) > backDay * 24 * 60 * 60000){
								file.delete();
								BaseEnv.log.info("SAFE ��������ɾ�������ļ�"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+"::File="+file.getAbsolutePath());
							}
						} catch (Exception e) {
							BaseEnv.log.error("SystemSafeTimer.getdbback ɾ�������ļ�ʱ���ڸ�ʽ����",e);
						}
					}
				}
			}
		}else{
			SystemState.bakState = ErrorCanst.DEFAULT_FAILURE;
			SystemState.bakStateStr = "ϵͳ�����쳣��Ϊ��֤��ȫ����������\\n"+rs.retVal;			
			BaseEnv.log.error("SAFE ��������ִ��ʧ�ܣ�ʱ��"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		}		
	}
}
