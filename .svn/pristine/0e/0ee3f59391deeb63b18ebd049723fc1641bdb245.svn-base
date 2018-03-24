/**
 * 
 */
package com.menyi.web.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;

/**
 * <p>Title:任务管理线程 数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:Nov 16, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class TaskMgt extends AIODBManager{

	/**
	 * 查询任务管理中所有任务
	 * @return
	 */
	public Result queryAllTask(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblTaskManage where statusId=0 and " +
										"(getdate()>ActionNextTime or len(ActionNextTime)=0 or ActionNextTime is null)" ;
							Statement state = conn.createStatement() ;
							ResultSet rss = state.executeQuery(sql) ;
							ArrayList<String[]> taskList = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] tasks = new String[6] ;
								tasks[0] = rss.getString("DefineName") ;
								tasks[1] = rss.getString("ActionTime") ;
								tasks[2] = rss.getString("FrequencyType") ;
								tasks[3] = rss.getString("ActionFrequency") ;
								tasks[4] = rss.getString("ActionNextTime") ;
								tasks[5] = rss.getString("id") ;
								taskList.add(tasks) ;
							}
							result.setRetVal(taskList) ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("queryAllTask queryAllTask mehtod",ex);
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
	 * 更新任务的下次执行时间
	 * @return
	 */
	public Result updateTaskNextTime(final String nextTime,final String keyId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update tblTaskManage set ActionNextTime=? where id=? " ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, nextTime) ;
							pss.setString(2, keyId) ;
							pss.executeUpdate() ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("queryAllTask updateTaskNextTime mehtod",ex);
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
	 * 更新任务的下次执行时间
	 * @return
	 */
	public Result updateTaskStatus(final String status,final String taskId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update tblTaskManage set ActionLastTime=?,ActionStatus=? where id=? " ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
							pss.setString(2, status) ;
							pss.setString(3, taskId) ;
							pss.executeUpdate() ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("queryAllTask updateTaskStatus mehtod",ex);
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
	 * 执行define文件
	 * @param defineName
	 * @param paramList
	 * @param userId
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result defineSql(final String defineName) {
    	final Result rs = new Result();
    	int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
                        if (defineSqlBean == null) {
                            BaseEnv.log.error("TaskMgt Define Sql Not Exist :Name = " + defineName);
                            rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
                            return  ;
                        }
                        HashMap map = new HashMap();
                        Result ret = defineSqlBean.execute(conn, map, null,null,null,"");
                        if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
                        	rs.retCode=ret.getRetCode();
                        	rs.retVal=ret.getRetVal();
                        	return ;
                        }
                    }});
                return rs.getRetCode();
            }
        });
    	rs.retCode = retVal ;
    	return rs;
    }
}
