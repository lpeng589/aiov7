package com.menyi.web.util;

import javax.servlet.ServletContext;

import com.koron.crm.carefor.CareforMgt;

public class CareforThread extends Thread {
	private boolean go = true;
	
	ServletContext servletContext;
    public CareforThread(ServletContext servletContext){
        this.servletContext = servletContext;
        this.setDaemon(true);
    }
	 public void stopThread(){
         go = false;
     }
	public void run() {
		while (go) {
			try {

				CareforMgt mgt = new CareforMgt();
				
				mgt.autoSend();
				
				try { //每半小时执行一次 
					Thread.sleep(30*60*1000L);
				} catch (InterruptedException ex) {
					
				}

			} catch (Exception ex1) {
				BaseEnv.log.error("---CareforThread run() error -",ex1);
			}
		}
	}
}
