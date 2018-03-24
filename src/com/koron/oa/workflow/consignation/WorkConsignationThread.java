package com.koron.oa.workflow.consignation;

/**
 * 
 * <p>Title:委托的线程，用来监控委托超时设置过期</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 2, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class WorkConsignationThread extends Thread{
	private boolean go = true;
	private OAConsignationMgt consignMgt = new OAConsignationMgt();
	public void stopThread(){
        go = false;
    }
	
	public void run() {
		while (go) {
			//每天晚上1到3点以后执行
			//System.out.println(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
			//if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>0 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY)<3){
				try {
					consignMgt.updateExpireConsign();
				} catch (Exception e) {
					e.printStackTrace();
					stopThread();
				}
			//}
			try { //每60分钟执行一次 
				Thread.sleep(60*60*1000L);
			} catch (InterruptedException ex) {
				stopThread();
			}
		}
	}
}
