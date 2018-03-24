package com.koron.oa.calendar;
import java.util.Timer;

import com.menyi.web.util.NotifyFashion;

public class WakeUpCalendarThread extends Thread{
	
	OAMyCalendarMgt mgt = new OAMyCalendarMgt();
	private Timer timer ;
	private  int count = 0 ;
	private long beginTime = 0 ;
	private int waitTime = 0 ;
	private int times = 0 ;
	private String title = "" ;
	private String content = "" ;
	private String userId = "" ;
	private String wakeUp = "" ;
	private String calendarId = "" ;
	
	public WakeUpCalendarThread(long beginTime,int waitTime,int times,String userId
							,String title,String content,String wakeUp,String calendarId){
		this.beginTime = beginTime ;
		this.waitTime = waitTime ;
		this.times = times ;
		this.userId = userId ;
		this.title = title ;
		this.content = content ;
		this.wakeUp = wakeUp ;
		this.calendarId = calendarId ;
	}
	
	public void run(){
		timer = new Timer();
        timer.schedule(new MyTask(userId,title,content,wakeUp,calendarId), beginTime, waitTime);
	}
	
	class MyTask extends java.util.TimerTask{
    	private String userId = "" ;
    	private String title = "" ;
    	private String content = "" ;
    	private String wakeType = "" ;
    	private String calendarId = "" ;
    	
    	public MyTask(String userId,String title,String content,String wakeType,String calendarId){
    		this.userId = userId ;
    		this.title = title ;
    		this.content = content ;
    		this.wakeType = wakeType ;
    		this.calendarId = calendarId ;
    	}
    	
        public void run(){
        	if(count>times-1){
        		try {
                    timer.cancel();
                } catch (Exception e) {
    				e.printStackTrace();
    			}
        	}else{
        		count++ ;
	        	String[] str = wakeType.split(",") ;
	        	for(String s : str){
	        		int wakeup = Integer.parseInt(s) ;
	        		NotifyFashion notify = new NotifyFashion(userId,title,content,userId,wakeup,"no",calendarId) ;
	        		notify.start() ;
	        	}
	        	mgt.updateWakeUpTimes(calendarId) ;
        	}
        }
    }
}