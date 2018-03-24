package com.menyi.web.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;

import com.dbfactory.Result;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>Title:提醒线程 每3分钟查询一次</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class AlertThread extends Thread {
	
	private boolean go = true;
	private UserFunctionMgt mgt = new UserFunctionMgt() ;
	ServletContext servletContext;
	
    public AlertThread(ServletContext servletContext){
        this.servletContext = servletContext;
        this.setDaemon(true);
    }
    
	public void stopThread(){
         go = false;
    }
	
	public void run() {
		while (go) {
			Result result = mgt.queryAlert() ;
			try {
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList alertList = (ArrayList) result.retVal ;
					for(int i=0;i<alertList.size();i++){
						AlertBean alertBean = (AlertBean) alertList.get(i) ;
						/*查询某部门下 所有职员*/
						String popedomUserIds = alertBean.getPopedomUserIds() ;
						if(alertBean.getPopedomDeptIds()!=null && alertBean.getPopedomDeptIds().length()>0){
							String[] arrayDept = alertBean.getPopedomDeptIds().split(",") ;
							for(String str : arrayDept){
								ArrayList<OnlineUser> listUser = OnlineUserInfo.getDeptUser(str) ;
								for(OnlineUser onUser : listUser){
									if(popedomUserIds!=null && !popedomUserIds.contains(","+onUser.getId()+",")){
										popedomUserIds += onUser.getId()+"," ;
									}
								}
							}
						}
						
						String[] arrayAlert = alertBean.getAlertType().split(",") ;
						for(String str : arrayAlert){
							NotifyFashion notify = new NotifyFashion(alertBean.getCreateBy(),alertBean.getAlertContent(),alertBean.getAlertUrl(),
									popedomUserIds,Integer.parseInt(str),"yes",alertBean.getRelationId(), null, null, alertBean.getRelationTable()) ;
							notify.start() ;
						}
						if("yes".equals(alertBean.getIsLoop())){
							Date nextAlert = BaseDateFormat.parse(alertBean.getNextAlertTime(), BaseDateFormat.yyyyMMddHHmmss) ;
							/*下次 提醒时间*/
							Calendar calendar = Calendar.getInstance() ;
							calendar.setTime(nextAlert) ;
							/*结束 时间*/
							Date endDate = null ;
							if(alertBean.getEndDate()!=null){
								endDate = BaseDateFormat.parse(alertBean.getEndDate()+" 23:59:59", BaseDateFormat.yyyyMMddHHmmss) ;
							}
							if("day".equals(alertBean.getLoopType())){
								calendar.add(Calendar.DAY_OF_MONTH, alertBean.getLoopTime()) ;
							}else if("week".equals(alertBean.getLoopType())){
								calendar.add(Calendar.WEEK_OF_MONTH, alertBean.getLoopTime()) ;
							}else if("month".equals(alertBean.getLoopType())){
								calendar.add(Calendar.MONTH, alertBean.getLoopTime()) ;
							}else{
								calendar.add(Calendar.YEAR, alertBean.getLoopTime()) ;
							}
							if(endDate==null || calendar.getTime().getTime()<endDate.getTime()){
								alertBean.setNextAlertTime(BaseDateFormat.format(calendar.getTime(), BaseDateFormat.yyyyMMddHHmmss)) ;
							}else{
								alertBean.setStatusId(-1) ;
							}
						}else{
							alertBean.setStatusId(-1) ;
						}
						mgt.updateAlert(alertBean) ;
					}
				}
				try { //每5分钟执行一次 
					Thread.sleep(3*60*1000L);
				} catch (InterruptedException ex) {
					
				}
			} catch (Exception ex1) {				
				BaseEnv.log.error("AlertThread error:", ex1) ;
				try { //每5分钟执行一次 
					Thread.sleep(3*60*1000L);
				} catch (InterruptedException ex) {
					
				}
			}
		}
	}
}
