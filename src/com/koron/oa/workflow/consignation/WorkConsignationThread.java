package com.koron.oa.workflow.consignation;

/**
 * 
 * <p>Title:ί�е��̣߳��������ί�г�ʱ���ù���</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 2, 2013
 * @Copyright: �����п���������޹�˾
 * @Author ǮС��
 */
public class WorkConsignationThread extends Thread{
	private boolean go = true;
	private OAConsignationMgt consignMgt = new OAConsignationMgt();
	public void stopThread(){
        go = false;
    }
	
	public void run() {
		while (go) {
			//ÿ������1��3���Ժ�ִ��
			//System.out.println(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
			//if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>0 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY)<3){
				try {
					consignMgt.updateExpireConsign();
				} catch (Exception e) {
					e.printStackTrace();
					stopThread();
				}
			//}
			try { //ÿ60����ִ��һ�� 
				Thread.sleep(60*60*1000L);
			} catch (InterruptedException ex) {
				stopThread();
			}
		}
	}
}
