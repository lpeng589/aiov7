package com.menyi.web.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.dbfactory.Result;
import com.koron.oa.bean.OAMyCalendar;
import com.koron.oa.calendar.OAMyCalendarMgt;



/**
 * 日历提醒线程 每隔2分钟刷新一次
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 19, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class CalendarThread extends Thread{
	
	private boolean go = true;
	private OAMyCalendarMgt mgt = new OAMyCalendarMgt() ;
	private String beginTime = "" ;
	
    public CalendarThread(){
        this.setDaemon(true);
    }
    
	public void stopThread(){
         go = false;
    }
	 
	public void run() {
		while (go) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;
				String endTime = dateFormat.format(new Date())+":00";
				if("".equals(beginTime)){
					beginTime = endTime ;
				}
				Result result = mgt.selectCalendarWakeUp(beginTime, endTime) ;
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList<OAMyCalendar> listCalendar = (ArrayList<OAMyCalendar>) result.getRetVal() ;
					for(OAMyCalendar calendar : listCalendar){
						String[] str = calendar.getWakeUpType().split(",") ;
			        	for(String s : str){
			        		int wakeup = Integer.parseInt(s) ;
			        		NotifyFashion notify = new NotifyFashion(calendar.getcreateBy(),"日历提醒："+calendar.getCalendarTitle(),
			        					calendar.getCalendarContext(),calendar.getcreateBy(),wakeup,"no",calendar.getid()) ;
			        		notify.start() ;
			        	}
					}
				}
				beginTime = endTime ;
				try { //每2分钟执行一次 
					Thread.sleep(2*60*1000L);
				} catch (InterruptedException ex) {
					BaseEnv.log.error("--CalendarThread run() error --",ex) ;
				}
			} catch (Exception ex1) {
				BaseEnv.log.error("--CalendarThread run() error --",ex1);
			}
		}
	}
}
