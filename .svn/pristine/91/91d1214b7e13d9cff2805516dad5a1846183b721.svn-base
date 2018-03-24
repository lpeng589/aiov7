package com.koron.oa.oaTask;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OATaskBean;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;

/**
 * 
 * <p>Title:我的任务数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/2
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class OATaskMgt extends AIODBManager{


	/**
	 * 添加任务
	 * @param bean
	 * @return
	 */
	public Result addTaskBean(final OATaskBean taskBean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				addBean(taskBean,session);
				session.flush();//表示强制处理addBean().不然下面UPDATE无效
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "";
							//若是子任务更新主任务的isCatalog
							if(taskBean.getTaskId()!=null && !"".equals(taskBean.getTaskId())){
								sql = "UPDATE OATask SET isCatalog='1' WHERE id=?";
								PreparedStatement pstmt = connection.prepareStatement(sql);
								pstmt.setString(1,taskBean.getTaskId());
								pstmt.executeUpdate();
							}
							
							//添加时默认添加任务编号(最大值+1)
							sql = "UPDATE OATask SET taskOrder = ((SELECT MAX(taskOrder) FROM OATask)+1) WHERE id=?";
							PreparedStatement pstmt1 = connection.prepareStatement(sql);
							pstmt1.setString(1,taskBean.getId());
							pstmt1.executeUpdate();
						} catch (SQLException ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OATaskMgt---addChildTask method :" + ex);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		
		
		return rst;
	}
	
	/**
	 * 查询任务BEAN
	 * @param taskId
	 * @return
	 */
	public Result loadTaskBean(String taskId){
		return loadBean(taskId,OATaskBean.class);
	}
	
	/**
	 * 修改任务BEAN
	 * @param taskId
	 * @return
	 */
	public Result updateTaskBean(OATaskBean bean){
		return this.updateBean(bean);
	}
	
	/**
	 * 更新任务信息
	 * @param moduleId
	 * @return
	 */
	public Result updateTaskInfo(final OATaskBean taskBean,final String reExecutorId,final String reSurveyorId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				updateBean(taskBean, session);
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							//删除原负责人通知
							if(!"".equals(reExecutorId)){
								String sql = "DELETE FROM tblAdvice WHERE type='OATaskPoint' and  Receive=? and relationId = ? ";
								PreparedStatement pstmt = connection.prepareStatement(sql);
								pstmt.setString(1,reExecutorId);
								pstmt.setString(2,taskBean.getId());
								pstmt.executeUpdate();
							}
							
							//删除原验收人通知
							if(!"".equals(reSurveyorId)){
								String sql = "DELETE FROM tblAdvice WHERE type='OATaskSurveyor' and  Receive=? and relationId = ? ";
								PreparedStatement pstmt = connection.prepareStatement(sql);
								pstmt.setString(1,reSurveyorId);
								pstmt.setString(2,taskBean.getId());
								pstmt.executeUpdate();
							}
							
							//更新关联了任务的日志
							String oaWorkLogSql = "UPDATE OAWorkLogDet SET contents=? WHERE relationId=?";
							PreparedStatement pstmtDet = connection.prepareStatement(oaWorkLogSql);
							pstmtDet.setString(1,taskBean.getTitle());
							pstmtDet.setString(2,taskBean.getId());
							pstmtDet.executeUpdate();
							
							
							//删除日程
							String oaCaSql = "DELETE FROM OACalendar WHERE relationId=?";
							PreparedStatement pstmtCa = connection.prepareStatement(oaCaSql);
							pstmtCa.setString(1,taskBean.getId());
							pstmtCa.executeUpdate();
							
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 公共sql查询
	 * @param sql
	 * @param param
	 * @return
	*/
	public Result publicSqlQuery(String sql,ArrayList param){
		return sqlList(sql,param);
	}
	
	/**
	 * jdbc调用公共新增修改删除方法
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result operationSql(final String sql, final List param) {
      final Result rst = new Result();
      int retCode = DBUtil.execute(new IfDB() {
          public int exec(Session session) {
              session.doWork(new Work() {
                  public void execute(Connection conn) throws
                          SQLException {
                      try {
                           PreparedStatement pstmt = conn.prepareStatement(sql);
                           for(int i = 1;i<=param.size();i++){
                               pstmt.setObject(i,param.get(i-1));
                           }
                          int row = pstmt.executeUpdate();
                          if (row > 0) {
                             rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                             rst.setRealTotal(row);
                          }
                     } catch (Exception ex) {
                          ex.printStackTrace();
                          
                          rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                          return;
                     }
                 }
             });
             return rst.getRetCode();
         }
     });
     rst.setRetCode(retCode);
     return rst;
	}
	
	public Result tasksQuery(String condition,int pageNo,int pageSize){
		
		String hql = "FROM OATaskBean WHERE 1=1 "+condition+" ORDER BY status asc ,lastUpdateTime desc ";
		//String hql = "FROM OATaskBean WHERE (isNull(taskId,'')='' or isCatalog = '1' )"+condition+" ORDER BY lastUpdateTime desc ";
		System.out.println("hql:"+hql);
		return this.list(hql, new ArrayList(), pageNo, pageSize, true);
	}
	
	/**
	 * 删除OATask
	 * @param moduleId
	 * @return
	 */
	public Result delTask(final String taskId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "";
							Statement stmt = connection.createStatement();
							
							sql = "DELETE FROM OATaskLog WHERE f_ref = '"+taskId+"'";//删除任务的进展
							stmt.addBatch(sql);
							sql = "DELETE FROM OATask WHERE id='"+taskId+"' or taskId='"+taskId+"'";//删除项目下的任务
							stmt.addBatch(sql);
							sql = "DELETE FROM tblAdvice WHERE relationId='"+taskId+"'";//删除项目下的任务
							stmt.addBatch(sql);
							
							sql = "DELETE FROM OACalendar WHERE relationId='"+taskId+"'";//删除日程
							stmt.addBatch(sql);
							
							stmt.executeBatch();
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 删除OATaskBean
	 * @param moduleId
	 * @return
	 */
	public Result addWarn(final AlertBean alertBean ,final String taskId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				
				addBean(alertBean, session);
				
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "UPDATE OATask SET nextAlertTime = ? WHERE id = ?";
							PreparedStatement pStmt = connection.prepareStatement(sql);
							pStmt.setString(1,alertBean.getNextAlertTime());
							pStmt.setString(2,taskId);
							pStmt.executeUpdate();
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 删除OAItemBean
	 * @param moduleId
	 * @return
	 */
	public Result delParticipant(final String taskId,final String employeeId,final String participants){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							//update参与人
							String sql = "UPDATE OATask SET participant = ? where id =?";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							pstmt.setString(1, participants);
							pstmt.setString(2, taskId);
							pstmt.executeUpdate();
							
							//删除参与人通知消息
							sql = "DELETE FROM tblAdvice WHERE relationId=? and Receive =?";
							PreparedStatement pstmt1 = connection.prepareStatement(sql);
							pstmt1.setString(1, taskId);
							pstmt1.setString(2, employeeId);
							pstmt1.executeUpdate();
							
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 更新任务信息
	 * @param moduleId
	 * @return
	 */
	public Result updateSingleByExecutor(final HashMap<String,String> conMap){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							//删除原负责人通知
							String sql = "DELETE FROM tblAdvice WHERE type='OATaskPoint' and  Receive=? and relationId = ? ";
							PreparedStatement pstmt1 = connection.prepareStatement(sql);
							pstmt1.setString(1,conMap.get("reExecutor"));
							pstmt1.setString(2,conMap.get("taskId"));
							pstmt1.executeUpdate();
							
							
							sql = "UPDATE OATask SET "+conMap.get("fieldName")+" = ?,lastUpdateBy=?,lastUpdateTime=? WHERE id = ?";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							pstmt.setString(1,conMap.get("fieldVal"));
							pstmt.setString(2,conMap.get("userId"));
							pstmt.setString(3,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
							pstmt.setString(4,conMap.get("taskId"));
							pstmt.executeUpdate();
							
							
							
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 更新任务信息
	 * @param moduleId
	 * @return
	 */
	public int getSurveyorMsgCount(final String taskId){
		 ArrayList<String> param = new ArrayList<String>() ;
		String hql = "from AdviceBean  where type = 'OATaskSurveyor' and relationId = ?";
		param.add(taskId);
		Result result = list(hql, param);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)result.retVal;
			if(list!=null && list.size()>0){
				return 1;
			}else{
				return 0;
			}
		}else{
			return 0;
		}	
	}
	
	/**
	 * 根据任务ID查询子任务
	 * @param taskId
	 * @return
	 */
	public Result queryChildTasksByTaskId(String taskId){
		String sql = "SELECT OATask.id,OATask.title,OATask.executor,OATask.status,OATask.beginTime,OATask.endTime," +
				"(select count(OATaskLog.id) from OATaskLog where OATaskLog.f_ref=OATask.id) as logCount,OATask.surveyor,OATask.isCatalog,OAItems.id," +
				"OAItems.title,OATask.createBy FROM OATask LEFT JOIN OAItems on OATask.itemId=OAItems.id WHERE taskId = ? ORDER BY OATask.createTime DESC";
		ArrayList param = new ArrayList();
		param.add(taskId);
		return sqlList(sql, param);
	}
	
	/**
	 * 根据项目ID查找关联任务
	 * @param itemId
	 * @return
	 */
	public Result queryTasksByItemId(String itemId){
		String sql = "SELECT OATask.id,OATask.title,OATask.executor,OATask.status,OATask.beginTime,OATask.endTime," +
				"(select count(OATaskLog.id) from OATaskLog where OATaskLog.f_ref=OATask.id) as logCount,OATask.surveyor,OATask.createBy " +
				"FROM OATask WHERE isNull(taskId,'') ='' and ItemId = ? ORDER BY createTime DESC";
		ArrayList param = new ArrayList();
		param.add(itemId);
		return sqlList(sql, param);
	}
	
	/**
	 * 更新参与人
	 * @param employeeIds  参与人
	 * @param loginBean
	 * @param taskId 任务ID
	 * @return
	 */
	public Result updateParticipants(String employeeIds,LoginBean loginBean,String taskId){
		String sql = "UPDATE OATask SET  participant= isNull(participant,'')+'"+employeeIds+"',lastUpdateBy=?,lastUpdateTime=? WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(loginBean.getId());
		param.add(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		param.add(taskId);
		
		return operationSql(sql, param);
	}
	
	/**
	 * 获取进行中TAB头部数量
	 * @return
	 */
	public HashMap<String,String> getRunTabCount(LoginBean loginBean,String countCondition){
		HashMap<String,String> countMap = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder();//条件语句
		String publicSql = " from OATask WHERE 1=1 ";
		
		sql.append("SELECT count(id) as count,'executor' as tabName ").append(publicSql).append(" and executor='").append(loginBean.getId()).append("' ").append(countCondition).append(" union ")
		.append(" select count(id) as count,'createBy' as tabName ").append(publicSql).append(" and createBy = '").append(loginBean.getId()).append("' and executor <> '").append(loginBean.getId()).append("' ").append(countCondition).append(" union ")
		.append(" select count(id) as count,'surveyor' as tabName ").append(publicSql).append(" and surveyor = '").append(loginBean.getId()).append("' ").append(countCondition).append(" union ")
		.append(" select count(id) as count,'participant' as tabName ").append(publicSql).append(" and ( ','+ participant like '%,").append(loginBean.getId()).append(",%' or createBy='").append(loginBean.getId()).append("' or surveyor='").append(loginBean.getId()).append("') ").append(countCondition);
		
		Result rs = sqlList(sql.toString(),new ArrayList());
		
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				for(Object obj : list){
					countMap.put(String.valueOf(GlobalsTool.get(obj,1)),String.valueOf(GlobalsTool.get(obj,0)));
				}
			}
		}
		return countMap;
	}
	
	/**
	 * 删除提醒
	 * @param moduleId
	 * @return
	 */
	public Result delAlert(final String taskId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							//删除提醒
							String sql = "DELETE FROM tblAlert WHERE relationId=?";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							pstmt.setString(1,taskId);
							pstmt.executeUpdate();
							
							
							//修改OATASKBEAN
							sql = "UPDATE OATask SET nextAlertTime = '' WHERE id = ?";
							PreparedStatement pstmt1 = connection.prepareStatement(sql);
							pstmt1.setString(1,taskId);
							pstmt1.executeUpdate();
							
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 根据客户Id获取客户名称
	 */
	public String findClientNameById(String keyId){
		String clientName = "";
		if(keyId!=null && !"".equals(keyId)){
			List<String> param = new ArrayList<String>();
			String sql = "SELECT clientName FROM CRMClientInfo WHERE id=?";
			param.add(keyId);
			Result rs = this.sqlList(sql, param);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
				if(list!=null && list.size()>0){
					clientName = String.valueOf(GlobalsTool.get(list.get(0),0));
				}
			}
		}
		return clientName;
	}
	
	/**
	 * 根据ID获取客户MAP
	 * @param clientIds
	 * @return
	 */
	public HashMap<String,String> queryClientMapByIds(String clientIds){
		HashMap<String,String> clientsMap = new HashMap<String, String>();
		String sql ="SELECT id,clientName FROM CRMClientInfo WHERE id in("+clientIds+")";
		Result rs = sqlList(sql, new ArrayList());
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			for(Object obj : list){
				clientsMap.put(String.valueOf(GlobalsTool.get(obj,0)),String.valueOf(GlobalsTool.get(obj,1)));
			}
		}
		return clientsMap;
	}
}

