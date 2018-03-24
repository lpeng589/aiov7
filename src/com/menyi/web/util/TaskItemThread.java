package com.menyi.web.util;


import com.dbfactory.Result;
import com.menyi.aio.web.sysAcc.ReCalcucateMgt;

/**
 * 
 * <p>Title:��������ִ�����߳� ��Ҫ����ִ�ж���дDefine�ļ�</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: �������
 * @Author ��СǮ
 */
public class TaskItemThread extends Thread {
	
	private TaskMgt mgt = new TaskMgt() ;
	private String defineID = "" ;
	private String taskId = "" ;
	
	public TaskItemThread(String defineID,String taskId){
		this.defineID = defineID ;
		this.taskId = taskId ;
	}
	public void run() {
		try {
			//���������ɱ���������ִ��java������ɱ�����
			Result result= mgt.defineSql(defineID) ;
			
			
			String status = "true" ;
			if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
				status = "false" ;
			}
			mgt.updateTaskStatus(status, taskId) ;
		} catch (Exception ex1) {
			ex1.printStackTrace() ;
			mgt.updateTaskStatus("false", taskId) ;
			BaseEnv.log.error("TaskThread error:", ex1) ;
		}
	}
}
