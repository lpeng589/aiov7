package com.menyi.web.util;


import com.dbfactory.Result;
import com.menyi.aio.web.sysAcc.ReCalcucateMgt;

/**
 * 
 * <p>Title:单个任务执行行线程 主要负责执行定制写Define文件</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: 科荣软件
 * @Author 文小钱
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
			//如果是重算成本操作，则执行java中重算成本代码
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
