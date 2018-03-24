package com.menyi.web.util;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletContext;

import com.dbfactory.Result;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;

/**
 * 
 * <p>Title:��������߳� ÿ1����ִ��һ��</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: �������
 * @Author ��СǮ
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
				try { //ÿ1����ִ��һ�� 
					Thread.sleep(60*1000L);
				} catch (InterruptedException ex) {
				}
			} catch (Exception ex1) {
				BaseEnv.log.error("WorkFlowThread error:", ex1) ;
			}
		}
	}
}
