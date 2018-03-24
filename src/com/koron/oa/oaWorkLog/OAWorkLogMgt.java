package com.koron.oa.oaWorkLog;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAWorkLogBean;
import com.koron.oa.bean.OAWorkLogDetBean;
import com.koron.oa.util.DateUtil;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.logManage.LoginlogSearchForm;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 
 * <p>Title:我的日志数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/12
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class OAWorkLogMgt extends AIODBManager{
	
	/**
	 * 日志查询
	 * @param condition
	 * @param statusType 状态查询 statusType=shareBy 加上条件
	 * @return
	 */
	
	public Result workLogQuery(String condition,String orderByInfo){
		String hql = "FROM OAWorkLogBean WHERE 1=1 "+condition; 
		
		if(orderByInfo == null || "".equals(orderByInfo)){
			hql+=" ORDER BY type desc, workLogDate ";
		}else if("TH".equals(orderByInfo)){
			hql+=" ORDER BY workLogDate desc ";
		}else{
			//按照关注人ID排序
			hql+=" order by CHARINDEX(','+CONVERT(varchar(50),createBy)+',','"+orderByInfo+"') ";
		}
		return list(hql, new ArrayList());
	}

	/**
	 * 日志明细查询
	 * @param ids
	 * @return
	 */
	public Result workLogDetQuery(final String ids) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "SELECT id,contents,contentType,relationType,relationId,workLogId,schedule,createTime From OAWorkLogDet WHERE workLogId in("+ids+")";
							PreparedStatement ps = connection.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							
							//存放结果HashMap<workLogId,HashMap<relationType,List<String[]>>>
							HashMap<String,HashMap<String,ArrayList<String[]>>> rsMap = new LinkedHashMap<String, HashMap<String,ArrayList<String[]>>>();
							while(rss.next()){
								String[] str=new String[8];
								str[0]=rss.getString("id");
								str[1]=rss.getString("contents");
								str[2]=rss.getString("contentType");
								str[3]=rss.getString("relationType");
								str[4]=rss.getString("relationId");
								str[5]=rss.getString("workLogId");
								str[6]=rss.getString("schedule");
								str[7]=rss.getString("createTime");
								
								if(rsMap.get(str[5])==null){
									HashMap<String,ArrayList<String[]>> map = new LinkedHashMap<String, ArrayList<String[]>>();
									ArrayList<String[]> list = new ArrayList<String[]>();
									list.add(str);
									map.put(str[2], list);
									rsMap.put(str[5], map);
								}else{
									if(rsMap.get(str[5]).get(str[2]) == null){
										ArrayList<String[]> list = new ArrayList<String[]>();
										list.add(str);
										rsMap.get(str[5]).put(str[2], list);
									}else{
										rsMap.get(str[5]).get(str[2]).add(str);
									}
								}
							}
							rst.setRetVal(rsMap) ;
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	 * 删除日志
	 * @param moduleId
	 * @return
	 */
	public Result delWorkLog(final String workLogId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							//明细
//							String sql = "DELETE FROM OAWorkLogDet WHERE workLogId = ?";
//							PreparedStatement stmt = connection.prepareStatement(sql);
//							stmt.setString(1,workLogId);
//							stmt.executeUpdate();
//							
//							
//							
//							//主表
//							String sql1 = "DELETE FROM OAWorkLog WHERE id = ?";
//							PreparedStatement stmt1 = connection.prepareStatement(sql1);
//							stmt1.setString(1,workLogId);
//							stmt1.executeUpdate();
							
							Statement stmt = connection.createStatement();
							
							String sql = "DELETE FROM OAWorkLogDet WHERE workLogId = '"+workLogId+"'";//明细
							stmt.addBatch(sql);
							
							sql = "DELETE FROM OAWorkLogDiscuss WHERE f_ref = '"+workLogId+"'";//评论
							stmt.addBatch(sql);
							
							sql = "DELETE FROM OAWorkLog WHERE id = '"+workLogId+"'";//明细
							stmt.addBatch(sql);
							
							sql = "DELETE FROM tblAdvice WHERE relationId = '"+workLogId+"'";//明细
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
	 * 更新关注信息
	 * @param moduleId
	 * @return
	 */
	public Result updateFollowInfo(final String employeeId,final String followIds){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							//删除信息
							String sql = "DELETE FROM OAWorkLogFollow WHERE id=?";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							pstmt.setString(1,employeeId);
							pstmt.executeUpdate();
							
							//插入信息
							sql = "INSERT INTO OAWorkLogFollow(id,followIds) VALUES(?,?)";
							PreparedStatement pstmt1 = connection.prepareStatement(sql);
							pstmt1.setString(1,employeeId);
							pstmt1.setString(2,followIds);
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
	
	/**
	 * 添加日志方法
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result addWorkLog(final OAWorkLogBean workLogBean, final ArrayList<OAWorkLogDetBean> detList,final Boolean existWorkLogId,final OAWorkLogBean nextWorkLogBean) {
      final Result rst = new Result();
      int retCode = DBUtil.execute(new IfDB() {
          public int exec(Session session) {
        	  
        	  if(!existWorkLogId){
        		  addBean(workLogBean, session);
        	  }else{
        		  updateBean(workLogBean,session);
        	  }
        	  
        	  if(nextWorkLogBean.getId()!=null && !"".equals(nextWorkLogBean.getId())){
        		  addBean(nextWorkLogBean, session);
        	  }
        	  
        	  
        	  for(OAWorkLogDetBean detBean : detList){
        		  addBean(detBean, session);
        	  }
             return rst.getRetCode();
             
             
         }
     });
     rst.setRetCode(retCode);
     return rst;
	}
	
	
	
	
	/**
	 * 添加日志明细
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result addWorkLogDet(final ArrayList<OAWorkLogDetBean> detList) {
      final Result rst = new Result();
      int retCode = DBUtil.execute(new IfDB() {
          public int exec(Session session) {
        	  for(OAWorkLogDetBean detBean : detList){
        		  addBean(detBean, session);
        	  }
             return rst.getRetCode();
         }
     });
     rst.setRetCode(retCode);
     return rst;
	}
	
	/**
	 * 修改日志方法
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result updateWorkLog(final OAWorkLogBean workLogBean, final ArrayList<OAWorkLogDetBean> detList,final ArrayList<String[]> updateTaskList){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				updateBean(workLogBean, session);
	        	for(OAWorkLogDetBean detBean : detList){
	        		addBean(detBean, session);
	        	}
	        	  
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "UPDATE OATASK SET title=? WHERE id=?";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							for(String[] str : updateTaskList){
								pstmt.setString(1,str[0]);
								pstmt.setString(2,str[1]);
								pstmt.addBatch();
							}
							pstmt.executeBatch();
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
	 * 获取BEAN
	 * @param id
	 * @return
	 */
	public Result loadWorkLogBean(String id){
		return loadBean(id,OAWorkLogBean.class);
		
	}
	
	/**
	 * 根据职员获取关注人信息
	 * @param id
	 * @return
	 */
	public String getFollowIdsById(String userId){
		String followIds ="";
		
		String sql ="SELECT id,followIds FROM OAWorkLogFollow WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(userId);
		Result rs = new Result();
		rs = sqlList(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> followIdsList = (ArrayList<Object>)rs.retVal;
			if(followIdsList!=null && followIdsList.size()>0){
				followIds = String.valueOf(GlobalsTool.get(followIdsList.get(0),1));
			}
		}
		return followIds;
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
	 * 根据ID查找客户
	 * @param id
	 * @return
	 */
	public Result findClient(String clientId){
		List list = new ArrayList();
		String sql = "select clientName from CRMClientInfo where id= '" + clientId + "'";
		return this.sqlList(sql, list);
	}
	
	/**
	 * 获取关注我的职员ID
	 * @param loginBean
	 * @return
	 */
	public String getFollowMeEmpIds(LoginBean loginBean){
		String ids = "";
		String sql = "select id from OAWorkLogFollow WHERE ','+followIds like '%,"+loginBean.getId()+",%'";
		Result rs = sqlList(sql,new ArrayList());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				for(Object obj : list){
					ids += GlobalsTool.get(obj,0)+",";
				}
			}
		}
		return ids;
	}
	
	/**
	 * 根据日志ID获取评论数量
	 * @param workLogList
	 * @return
	 */
	public HashMap<String,String> queryDiscussCount(ArrayList<OAWorkLogBean> workLogList){
		HashMap<String,String> rsMap = new HashMap<String, String>();
		String workLogIds = "";
		if(workLogList!=null && workLogList.size()>0){
			for(OAWorkLogBean bean : workLogList){
				workLogIds += "'"+bean.getId()+"',";
			}
			if(workLogIds.endsWith(",")){
				workLogIds = workLogIds.substring(0,workLogIds.length()-1);
			}
			String sql = "SELECT f_ref,count(id) FROM OAWorkLogDiscuss WHERE f_ref in ("+workLogIds+") Group BY f_ref";
			Result rs = sqlList(sql, new ArrayList());
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
				if(list!=null && list.size()>0){
					for(Object obj : list){
						rsMap.put(String.valueOf(GlobalsTool.get(obj,0)), String.valueOf(GlobalsTool.get(obj,1)));
					}
				}
			}
		}
		return rsMap;
	}
	
	/*
	 * 存在明天的日志
	 */
	public String existNextWorkLog(String workLogDate,LoginBean loginBean,String workLogType,String[] mondayAndSunday){
		String existNextWorkLog = "";
		ArrayList param = new ArrayList();
		String sql = "SELECT id from OAWorkLog WHERE createBy=?";
		param.add(loginBean.getId());
		if("day".equals(workLogType)){
			sql +=" and worklogDate =? and type=?";
			param.add(workLogDate);
			param.add("day");
		}else{
			sql +=" and worklogDate between ? and ? and type=?";
			param.add(mondayAndSunday[0]);
			param.add(mondayAndSunday[1]);
			param.add("week");
		}
		
		Result rs = sqlList(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			if(list!=null && list.size()>0){
				existNextWorkLog = "true";
			}
		}
		
		return existNextWorkLog;
	}
	
	/*
	 * 过滤后三天的日志
	 */
	public LinkedHashMap<String,String> getNextWorkLogDate(Calendar ca,LoginBean loginBean){
		LinkedHashMap<String,String>  map = new LinkedHashMap<String, String>(); 
		String strCondition="";
		String dateStr = "";
		for(int i=0;i<3;i++){
			ca.add(Calendar.DATE,1);
			dateStr = BaseDateFormat.format(ca.getTime(), BaseDateFormat.yyyyMMdd);
			strCondition += "'"+dateStr+"',";
			map.put(dateStr, new DateUtil().getCurrentWeekOfMonth(ca));
		}
		if(strCondition.endsWith(",")){
			strCondition = strCondition.substring(0,strCondition.length()-1);
		}
		String sql = "SELECT workLogDate from OAWorkLog WHERE type='day' and workLogDate in("+strCondition+") and createBy=?";
		ArrayList param = new ArrayList();
		param.add(loginBean.getId());
		Result rs = sqlList(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				for(Object obj : list){
					map.remove(String.valueOf(GlobalsTool.get(obj,0)));
				}
			}
		}
		
		return map;
	}
	
	public OAWorkLogBean workLogBeanQuery(OAWorkLogBean workLogBean,String condition){
		OAWorkLogBean bean = null;
		String hql = "FROM OAWorkLogBean WHERE createBy=? and type=? "+condition;
		ArrayList param = new ArrayList();
		param.add(workLogBean.getCreateBy());
		param.add(workLogBean.getType());
		Result rs = list(hql,param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<OAWorkLogBean> list = (ArrayList<OAWorkLogBean>)rs.retVal;
			if(list!=null && list.size()>0){
				bean = list.get(0);
			}
		}
		return bean;
	}
	
	/**
	 * 日志列表数据 表tblLoginLog
	 * @param searchForm
	 * @return
	 */
	public Result queryLoginLogList(final String clientid,final LoginBean loginbean){
		String sql = "SELECT a.id id,b.contents contents,c.contents contents1,c.schedule schedule,a.workLogDate workLogDate " +
				"FROM OAWorkLog a left join OAWorkLogDet b on a.id=b.workLogId and b.contentType=2 left join OAWorkLogDet c on b.id=c.relationId and c.contentType=1 " +
				"WHERE a.createBy=? and b.relationId=? ORDER BY a.workLogDate DESC";
		AIODBManager manager=new AIODBManager();
		ArrayList param = new ArrayList();
		String id=loginbean.getId();
		param.add(id);
		param.add(clientid);
		return manager.sqlList(sql, param);
	}
	/**
	 * 同步日志到日程
	 * @param mapping
	 * @param form 
	 * @param request
	 * @param response
	 * @return
	 */
	public void synWorkLog(LoginBean loginBean,String createTime) {
      Result rst = new Result();
      ArrayList param = new ArrayList();
      	String userId=loginBean.getId();
		String worklogid;
		String time;
		String tiele;
		String relationId;
		String sql="select b.id,b.contents,b.relationId,b.createTime from OAWorkLog a left join OAWorkLogDet b on a.id=b.workLogId where a.createBy=? and a.createTime >=?";
		param.add(userId);
		param.add(createTime);
		AIODBManager manager=new AIODBManager();
		Result rs = manager.sqlList(sql, param);
		if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			List<Object[]> list=(List<Object[]>)rs.getRetVal();
			int count=list.size();
			for(int i=0;i<count;i++){
				try {
					worklogid=(String)list.get(i)[0];
					tiele=(String)list.get(i)[1];
					relationId=(String)list.get(i)[2];
					time=(String)list.get(i)[3];
					sql="select id from OACalendar where userId=? and relationId=?";
					param = new ArrayList();
					param.add(userId);
					param.add(worklogid);
					rs = manager.sqlList(sql, param);
					if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
						List<Object[]> list2=(List<Object[]>)rs.getRetVal();
						if(list2.size()>0){
							continue;
						}else{
							String id = IDGenerater.getId();
							sql="insert into OACalendar(id,title,type,stratTime,finishTime,userId,delStatus,relationId,clientId) values(?,?,?,?,?,?,?,?,?)";
							param = new ArrayList();
							param.add(id);
							param.add(tiele);
							param.add("我的日志");
							param.add(time);
							param.add(time);
							param.add(userId);
							param.add("0");
							param.add(worklogid);
							param.add(relationId);
							rs = operationSql(sql, param);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
	}
	
}

