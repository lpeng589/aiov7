package com.koron.crm.distributeswork;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.qa.CRMQABean;
import com.koron.crm.qa.CRMQASearchForm;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class CRMDistWorkMgt extends AIODBManager{
	/**
	 * 查询
	 * */
	public Result query(CRMDistWorkSearchForm form) {
		//创建参数
		List param = new ArrayList();
		String sql=" select * from ( select  t.id,t.ref_id,c.ClientName,t.taskType,t.content, t.createBy,t.userId,t.statusId,t.taskStatus,t.createTime,t.finishuser   " ;
		sql += "  from CRMTaskAssign  t inner join  crmclientInfo  c   on   t.ref_id=c.id )  bean where  1=1 ";
		//调用list返回结果
		if(form.getClientName()!=null && !form.getClientName().trim().equals("")){
			sql += "  and   ClientName  like  ?  ";
			param.add("%"+form.getClientName()+"%");
		}
		
		if(form.getTaskType() != null && !form.getTaskType().trim().equals("")){
			sql += "   and   TaskType=? "; 
			param.add(form.getTaskType());
		}
		
		if(form.getContent() !=null  && !form.getContent().trim().equals("")){
			sql += "  and  Content like ?  ";
			param.add("%"+form.getContent()+"%");
		}
		
		if(form.getUserId() != null && !form.getUserId().trim().equals("")){
			sql += "  and   userid like ?  ";
			param.add("%"+form.getUserId()+"%");
		}
		
		if(form.getCreateUserId()!= null && !form.getCreateUserId().trim().equals("")){
			sql += "  and   createBy = ?  ";
			param.add(form.getCreateUserId());
		}
		
		if(form.getAssignStatus() != null && !form.getAssignStatus().trim().equals("")){
			sql += "  and   AssignStatus = ?  ";
			int status=Integer.parseInt(form.getAssignStatus());
			param.add(status);
		}
		
		if(form.getTaskStatus() != null && !form.getTaskStatus().trim().equals("")){
			sql += "  and   TaskStatus = ?  ";
			int status=Integer.parseInt(form.getTaskStatus());
			param.add(status);
		}
		
		
		if(form.getCreateStartTime() != null && !form.getCreateStartTime().trim().equals("")){
			if(form.getCreateEndTime() != null && !form.getCreateEndTime().trim().equals("")){
				sql += "  and ( createTime  between ? and ?  or createTime  like ? )  ";
				param.add(form.getCreateStartTime().trim());
				param.add(form.getCreateEndTime().trim());
				param.add("%"+form.getCreateEndTime().trim()+"%");
			}else{
				sql += "  and  createTime  like ? ";
				param.add("%"+form.getCreateStartTime().trim()+"%");
			}
		}
		
		sql += "  order by  createTime desc ";
		return sqlList(sql, param,  form.getPageSize(),form.getPageNo(), true);
	}
	
	
	public Result deleteById(String[] ids){
		return deleteBean(ids, CRMDistWorkBean.class, "id");
	}
	
	/*
	public Result add(CRMDistWorkBean bean){
		return addBean(bean);
	}
	*/
	
	
	/**
	 * 添加
	 * @param publicScopeBean
	 * @return
	 */
	public Result add(CRMDistWorkBean bean) {
		return addBean(bean);
//		final Result rst = new Result();
//		int retCode = DBUtil.execute(new IfDB() {
//			public int exec(Session session) {
//				addBean(bean,session);
//				session.doWork(new Work() {
//					public void execute(Connection connection) throws SQLException {
//						try {
//							Statement state = connection.createStatement();
//							if(bean.getUserId()!=null && !"".equals(bean.getUserId())){
//								String sql ="";
//								String title=loginBean.getEmpFullName()+"委派任务给你，请查看。";
//								String url = "/CRMDistWorkSearchAction.do?operation=4";
//								String message = "<a href=javascript:mdiwin(''" + url + "'',''委派任务'')>"+title+"</a>";
//								String id = IDGenerater.getId();
//								
//								for(String str : bean.getUserId().split(",")){
//									sql = "insert into tblAdvice(id,relationId,send,title,receive,content,status,exist,createBy,createTime,type)"
//										+ "values('"+id+"','"+bean.getId()+"','"+loginBean.getId()+"','"+title+"','"+str+"','"+message+"','noRead','all','"+loginBean.getId()+"','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"','clientShare')" ;
//									// 给客户端用户发送通知消息
//									MSGConnectCenter.sendAdvice(str, id, title);
//									state.addBatch(sql);
//								}
//								state.executeBatch();
//							}	
//							
//						} catch (SQLException ex) {
//							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//							ex.printStackTrace();
//							BaseEnv.log.error("CRMBrotherSettingMgt---updatePublicScope method :" + ex);
//						}
//					}
//				});
//				return rst.getRetCode();
//			}
//		});
//		rst.setRetCode(retCode);
//		return rst;
	}
	
	public Result update(CRMDistWorkBean bean){
		return updateBean(bean);
	}
	
	public Result detail(String id){
		return loadBean(id, CRMDistWorkBean.class);
	}
}
