package com.koron.crm.salesFlow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

/**
 * 
 * <p>Title:CRM兄弟表</p> 
 * <p>Description: </p>
 *
 * @Date:2013.8.27
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class CRMSalesFlowMgt extends AIODBManager{
	
	/**
	 * 根据表名查看流程
	 * @param tableName
	 * @return
	 */
	public Result flowQueryByTableName(String tableName){
		ArrayList param = new ArrayList();
		String sql = "SELECT flowName,orderNo,brotherTables FROM CRMSalesFlow WHERE tableName = ? order by orderNo";
		param.add(tableName);
		return this.sqlList(sql, param);
	}
	
	public Result addFlow(final String flowInfo,final String tableName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							//删除流程
							String delSql = "DELETE FROM CRMSalesFlow WHERE tableName=?";
							PreparedStatement ps = connection.prepareStatement(delSql);
							ps.setString(1, tableName);
							ps.executeUpdate();
							
							//插入
							if(flowInfo !=null && !"".equals(flowInfo)){
								String sql = "INSERT INTO CRMSalesFlow(id,flowName,orderNo,brotherTables,tableName) VALUES(?,?,?,?,?)";
								PreparedStatement ps1 = connection.prepareStatement(sql);
								int orderNo = 1;
								for(String str : flowInfo.split(";")){
									String[] flow = str.split(":");
									ps1.setString(1, IDGenerater.getId());
									ps1.setString(2, flow[0]);
									ps1.setString(3, orderNo+"");
									ps1.setString(4, flow[1]);
									ps1.setString(5, tableName);
									orderNo++;
									ps1.addBatch();
								}
								ps1.executeBatch();
							}
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
}
