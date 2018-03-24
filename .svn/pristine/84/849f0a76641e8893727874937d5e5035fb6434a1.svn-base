package com.koron.crm.taskAssign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.CRMTaskAssignBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

/**
 * 
 * <p>Title:CRM任务分派</p> 
 * <p>Description: </p>
 *
 * @Date:2013.12.4
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class CRMTaskAssignMgt extends AIODBManager{
	
	/**
	 * 查询方法
	 * @param condition
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	Result queryTaskAssign(String condition,int pageSize,int pageNo){
		
		String sql = "SELECT CRMTaskAssign.id as id,CRMTaskAssign.title,CRMTaskAssign.taskStatus,CRMTaskAssign.content,CRMTaskAssign.userId,CRMTaskAssign.createBy,CRMTaskAssign.priority," +
				"CRMTaskAssign.createTime,CRMTaskAssign.taskType,CRMTaskAssign.summary,CRMClientInfo.id as clientId,CRMClientInfo.clientName,row_number() over(order by  CRMTaskAssign.createTime desc) row_id " +
				"FROM CRMTaskAssign left join CRMClientInfo on CRMTaskAssign.Ref_id = CRMClientInfo.id WHERE 1=1 "+condition;
		return sqlListMaps(sql, new ArrayList(), pageNo, pageSize);
	}
	
	/**
	 * 添加
	 * @param bean
	 * @return
	 */
	Result addTaskAssign(CRMTaskAssignBean bean){
		return addBean(bean);
	}
	
	/**
	 * 获取任务分派BEAN
	 * @param bean
	 * @return
	 */
	Result loadTaskAssign(String id){
		return loadBean(id, CRMTaskAssignBean.class);
	}
	
	/**
	 * 更新任务分派BEAN
	 * @param bean
	 * @return
	 */
	Result updateTaskAssign(CRMTaskAssignBean bean){
		return updateBean(bean);
	}
	
	/**
	 * 删除客户资料
	 * @param keyIds
	 * @return
	 */
    public Result delTaskAssign(final String[] keyIds) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							
							/*删除客户关怀信息*/
							Statement state = conn.createStatement() ;
							for(String str : keyIds){
								String sql = "DELETE FROM CRMTaskAssign WHERE id='"+str+"'";
								state.addBatch(sql) ;
								
								sql = "DELETE FROM OACalendar WHERE relationId='"+str+"'";
								state.addBatch(sql) ;
							}
							
							state.executeBatch() ;
							
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt delete mehtod:", ex) ;
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
}
