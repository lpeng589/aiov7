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
 * <p>Title:CRM�������</p> 
 * <p>Description: </p>
 *
 * @Date:2013.12.4
 * @Copyright: �����п���������޹�˾
 * @Author ��ܿ�
 */
public class CRMTaskAssignMgt extends AIODBManager{
	
	/**
	 * ��ѯ����
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
	 * ���
	 * @param bean
	 * @return
	 */
	Result addTaskAssign(CRMTaskAssignBean bean){
		return addBean(bean);
	}
	
	/**
	 * ��ȡ�������BEAN
	 * @param bean
	 * @return
	 */
	Result loadTaskAssign(String id){
		return loadBean(id, CRMTaskAssignBean.class);
	}
	
	/**
	 * �����������BEAN
	 * @param bean
	 * @return
	 */
	Result updateTaskAssign(CRMTaskAssignBean bean){
		return updateBean(bean);
	}
	
	/**
	 * ɾ���ͻ�����
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
							
							/*ɾ���ͻ��ػ���Ϣ*/
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
