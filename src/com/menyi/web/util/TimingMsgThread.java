package com.menyi.web.util;

import java.util.*;

import javax.servlet.ServletContext;
import com.dbfactory.Result;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.alert.AlertCenterMgt;
import com.menyi.sms.setting.NoteCenterMgt;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: �����</p>
 *
 * @author �����
 * @version 1.0
 */
public class TimingMsgThread extends Thread{
    private boolean go = true;
    ServletContext servletContext;
     public TimingMsgThread(ServletContext servletContext){
    	 this.setName("TimingMsgThread");
         this.servletContext = servletContext;
         this.setDaemon(true);
     }

     public void stopThread(){
         go = false;
     }

     public void run(){
         while (go) {
             try {
            	 AlertCenterMgt alertMgt=new AlertCenterMgt();
                 /**
                  * Ԥ������֪ͨ��Ϣ�Ͷ��ţ��ʼ����Ѵ���
                  */
                 alertMgt.queryAlertTotals(servletContext);
                 
             } catch (Exception ex1) {
                 BaseEnv.log.error("---TimingMsg run() error -",ex1);
             }
             
             try {
                 Thread.sleep(5*60000);
             } catch (InterruptedException ex) {}
         }
     }
     
     private void sendOAWorkFlowWake(){
    	 
     }

}
