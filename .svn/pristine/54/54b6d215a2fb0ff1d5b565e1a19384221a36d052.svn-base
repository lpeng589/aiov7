package com.menyi.web.util;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletContext;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;

/**
 * 
 * <p>Title:任务管理线程 每1分钟执行一次</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class TaskThread extends Thread {
	
	private boolean go = true;
	private TaskMgt mgt = new TaskMgt() ;
	ServletContext servletContext;
	
    public TaskThread(ServletContext servletContext){
        this.servletContext = servletContext;
        this.setDaemon(true);
    }
    
	public void stopThread(){
         go = false;
    }
	
	public void run() {
		while (go) {
			try {
				Result result = mgt.queryAllTask() ;
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList<String[]> taskList = (ArrayList<String[]>) result.retVal ;
					/*
					tasks[0] = rss.getString("DefineName") ;
					tasks[1] = rss.getString("ActionTime") ; 计 划 执 行 时 间
					tasks[2] = rss.getString("FrequencyType") ;  执 行 频 率
					tasks[3] = rss.getString("ActionFrequency") ; 频 率 时 间
					tasks[4] = rss.getString("ActionNextTime") ;
					tasks[5] = rss.getString("id") ; */
					for(String[] array : taskList){
						String actionNextTime = "" ;
						Calendar calendar = Calendar.getInstance() ;
						if(array[4]==null || array[4].length()==0){
							int nowHour = calendar.get(Calendar.HOUR_OF_DAY) ;
							/*开始时间小于当前时间*/
							if(nowHour>Integer.parseInt(array[1])){
								calendar.add(Calendar.DAY_OF_MONTH, 1) ;
							}
							actionNextTime = BaseDateFormat.format(calendar.getTime(), BaseDateFormat.yyyyMMdd)+" " + array[1] +":00:00" ; 
						}else{
							//启动一个新的线程 执行define操作
							new TaskItemThread(array[0],array[5]).start() ;
							
							if("day".equals(array[2])){
								calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(array[3])) ;
							}else if("hour".equals(array[2])){
								calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(array[3])) ;
							}else if("minute".equals(array[2])){
								calendar.add(Calendar.MINUTE, Integer.parseInt(array[3])) ;
							}
							actionNextTime = BaseDateFormat.format(calendar.getTime(), BaseDateFormat.yyyyMMddHHmmss) ;
						}
						mgt.updateTaskNextTime(actionNextTime, array[5]) ;
					}
				}
				try { //每1分钟执行一次 
					Thread.sleep(60*1000L);
				} catch (InterruptedException ex) {
				}
			} catch (Exception ex1) {
				ex1.printStackTrace() ;
				BaseEnv.log.error("TaskThread error:", ex1) ;
			}
		}
	}
}
