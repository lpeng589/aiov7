package com.koron.crm.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class ClientServiceMgt extends DBManager{

	/**
	 * 跟据关键字查询QA问题库
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result queryQA(final String question) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,Ref_id from CRMQA where  ref_id like ? or answer like ?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, '%'+question+'%') ;
							pss.setString(2, '%'+question+'%') ;
							ResultSet rss = pss.executeQuery() ;
							ArrayList<String[]> listQA = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] str = new String[2] ;
								str[0] = rss.getString("id") ;
								String content = rss.getString("ref_id") ;
								if(content!=null && content.length()>10){
									content = content.substring(0,10) ;
								}
								str[1] = content ;
								listQA.add(str) ;
							}
							result.setRetVal(listQA) ;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("ClientServiceMgt queryQA:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 更新QA问题库检索次数
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result updateQATimes(final String keyId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update CRMQA set times=times+1 where id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, keyId) ;
							int num = pss.executeUpdate() ;
							result.setRetVal(num) ;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("ClientServiceMgt updateQATimes:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 分配任务
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result addTask(final String taskId, final String clientId,final String taskType,
			final String content,final String userId,final String personDept ,final String loginId,
			final String deptCode,final String companyId,final String affix,final String task) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "insert into CRMTaskAssign(id,Ref_id,taskType,content,userId,createTime,createBy,taskStatus,assignStatus,SCompanyID,crmAffix) " +
									"values(?,?,?,?,?,?,?,?,?,?,?)" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							String createTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) ;
							pss.setString(1, taskId) ;
							pss.setString(2, clientId) ;
							pss.setString(3, taskType) ;
							pss.setString(4, content);
							pss.setString(5, userId);
							pss.setString(6, createTime);
							pss.setString(7, "".equals(userId)?loginId:userId);
							pss.setString(8, "-1") ;
							pss.setString(9, "".equals(userId)?"-1":"0") ;
							pss.setString(10, companyId) ;
							pss.setString(11, affix) ;
							int number = pss.executeUpdate() ;
							String nowTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd) ;
							if(number>0){
								/**
								//插入工作计划
								sql = "insert into tblDayWorkPlan(id,departmentCode,EmployeeID,workDate,title,content,state,kind,time,assBuss,createBy,createTime,assClient,BeginDate,EndDate)" +
									  "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
								String planId = IDGenerater.getId() ;
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, planId) ;
								pss.setString(2, deptCode) ;
								pss.setString(3, userId) ;
								pss.setString(4, nowTime) ;
								pss.setString(5, task+":"+content) ;
								pss.setString(6, content) ;
								pss.setString(7, "0") ;
								pss.setString(8, "1") ;
								pss.setInt(9, 0) ;
								pss.setString(10, taskId) ;
								pss.setString(11, userId) ;
								pss.setString(12, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
								pss.setString(13, clientId+";") ;
								pss.setString(14, nowTime) ;
								pss.setString(15, nowTime) ;
								pss.executeUpdate() ;
								**/
								if(personDept!=null && personDept.length()>0){
									Statement state = conn.createStatement() ;
									//sql = "delete from CRMClientInfoEmp where f_ref='"+clientId+"' and employeeId='"+loginId+"'" ;
									//state.addBatch(sql) ;
									sql = "insert into CRMClientInfoEmp(id,f_ref,departmentCode,employeeId) values('"+IDGenerater.getId()+"','"+clientId+"','"+personDept+"','"+userId+"')" ;
									state.addBatch(sql) ;
									state.executeBatch() ;
								}
								//result.setRetVal(planId) ;
								if("chance".equals(taskType)){
									/*插入销售跟单*/
									sql = "insert into CRMSaleFollowUp(id,f_brother,FollowNo,FollowPhase,DepartmentCode,EmployeeID,VisitTime,Content,createBy,createTime,FollowStatus,ClientId)" +
											" values(?,?,?,?,?,?,?,?,?,?,?,?)" ;
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, IDGenerater.getId()) ;
									pss.setString(2, clientId) ;
									DBFieldInfoBean field = GlobalsTool.getFieldBean("CRMSaleFollowUp", "FollowNo") ;
									pss.setString(3, field.getDefValue(conn)) ;
									pss.setInt(4, 1) ;
									pss.setString(5, deptCode) ;
									pss.setString(6, userId) ;
									pss.setString(7, nowTime) ;
									pss.setString(8, content) ;
									pss.setString(9, userId) ;
									pss.setString(10, createTime) ;
									pss.setInt(11, 1) ;
									pss.setString(12, clientId) ;
									pss.execute() ;
								}else if("service".equals(taskType)){
									/*插入维护记录*/
									sql = "insert into tblMaintainNote(id,f_brother,ClientId,BillNo,BillDate,DepartmentCode,EmployeeID,createBy,createTime) " +
											"values(?,?,?,?,?,?,?,?,?)" ;
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, IDGenerater.getId()) ;
									pss.setString(2, clientId) ;
									pss.setString(3, clientId) ;
									DBFieldInfoBean field = GlobalsTool.getFieldBean("tblMaintainNote", "BillNo") ;
									pss.setString(4, field.getDefValue(conn)) ;
									pss.setString(5, nowTime) ;
									pss.setString(6, deptCode) ;
									pss.setString(7, userId) ;
									pss.setString(8, userId) ;
									pss.setString(9, createTime) ;
									pss.execute() ;
								}else{
									/*插入客户投诉*/
									sql = "insert into CRMcomplaints(id,f_brother,ClientId,IsVisit,BillNo,BillDate,ComplaintsViscera,createBy,createTime) " +
											"values(?,?,?,?,?,?,?,?,?)" ;
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, IDGenerater.getId()) ;
									pss.setString(2, clientId) ;
									pss.setString(3, clientId) ;
									pss.setInt(4, 0) ;
									DBFieldInfoBean field = GlobalsTool.getFieldBean("CRMcomplaints", "BillNo") ;
									pss.setString(5, field.getDefValue(conn)) ;
									pss.setString(6, nowTime) ;
									pss.setString(7, content) ;
									pss.setString(8, userId) ;
									pss.setString(9, createTime) ;
									pss.execute() ;
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("ClientServiceMgt addTask:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 跟据关键字查询客户
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result queryClientByKeyWord(final String keyWord,int pageSize,int pageNo) {
		String hql = "select distinct bean from CrmClientInfoBean bean left join bean.beanDet det " +
				"where bean.statusId != -1 and (bean.clientName like ? or bean.phone like ? or bean.keywordsPYM like ?" +
				" or bean.email like ? or bean.url like ? or bean.clientNo like ? " +
				" or det.mobile like ? or det.userName like ? or det.clientEmail like ?" +
				" or det.qq like ? or det.hobby like ? or det.msn like ?  or det.telephone like ?)" ;
		ArrayList param = new ArrayList() ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		param.add("%"+keyWord+"%") ;
		return list(hql, param,pageNo,pageSize, true);
	}
	
	/**
	 * 查询是否存在此客户
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result existClient(final String keyWord) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select count(distinct bean.id) as asCount from CrmClientInfo bean left join CRMClientInfoDet det  on bean.id=det.f_ref "
									   + "where bean.statusId != -1 and (bean.clientName like '%"+keyWord+"%' or bean.phone like '%"+keyWord+"%' or bean.keywordsPYM like '%"+keyWord+"%' "
									   + "or bean.email like '%"+keyWord+"%' or bean.url like '%"+keyWord+"%' or bean.clientNo like '%"+keyWord+"%' " 
									   + "or det.mobile like '%"+keyWord+"%' or det.userName like '%"+keyWord+"%' or det.clientEmail like '%"+keyWord+"%' "
									   + "or det.qq like '%"+keyWord+"%' or det.hobby like '%"+keyWord+"%' or det.msn like '%"+keyWord+"%'  or det.telephone like '%"+keyWord+"%')" ;
							Statement state = conn.createStatement() ;
							ResultSet rss = state.executeQuery(sql) ;
							if(rss.next()){
								int asCount = rss.getInt("asCount") ;
								if(asCount>1){
									result.setRetVal("more") ;
								}else if(asCount==1){
									sql = "select distinct bean.id as clientId from CrmClientInfo bean left join CRMClientInfoDet det  on bean.id=det.f_ref "
										   + "where bean.statusId != -1 and (bean.clientName like '%"+keyWord+"%' or bean.phone like '%"+keyWord+"%' or bean.keywordsPYM like '%"+keyWord+"%' "
										   + "or bean.email like '%"+keyWord+"%' or bean.url like '%"+keyWord+"%' or bean.clientNo like '%"+keyWord+"%' " 
										   + "or det.mobile like '%"+keyWord+"%' or det.userName like '%"+keyWord+"%' or det.clientEmail like '%"+keyWord+"%' "
										   + "or det.qq like '%"+keyWord+"%' or det.hobby like '%"+keyWord+"%' or det.msn like '%"+keyWord+"%'  or det.telephone like '%"+keyWord+"%')" ;
									rss = state.executeQuery(sql) ;
									if(rss.next()){
										result.setRetVal(rss.getString("clientId")) ;
									}
								}else{
									result.setRetVal("noData") ;
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("ClientServiceMgt addTask:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
}
