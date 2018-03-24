package com.menyi.web.util;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletContext;

import com.dbfactory.Result;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
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
public class WorkFlowThread extends Thread {
	
	private boolean go = true;
	private OAMyWorkFlowMgt mgt = new OAMyWorkFlowMgt() ;
	ServletContext servletContext;
	
    public WorkFlowThread(ServletContext servletContext){
        this.servletContext = servletContext;
        this.setDaemon(true);
    }
    
	public void stopThread(){
         go = false;
    }
	
	public void run() {
		while (go) {
			try {
				Result result = mgt.getRelayWorkFlow() ;
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){}
				try { //每1分钟执行一次 
					Thread.sleep(60*1000L);
				} catch (InterruptedException ex) {
				}
			} catch (Exception ex1) {
				BaseEnv.log.error("WorkFlowThread error:", ex1) ;
			}
		}
	}
}
