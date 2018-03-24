package com.koron.crm.serviceConsole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
/**
 * 
 * <p>Title:CRM兄弟表</p> 
 * <p>Description: </p>
 *
 * @Date:2013.10.8
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class ServiceConsoleMgt extends AIODBManager{
	
	/**
	 * 模糊查找客户
	 * @param condition
	 * @return
	 */
	/*
	public Result queryClientByKeyWord(final String condition,final boolean isMobileQuery) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							String sql = "";
							if(isMobileQuery){
								sql = "SELECT CRMClientInfo.id,CRMClientInfo.clientName,CRMClientInfoDet.userName FROM CRMClientInfo left join CRMClientInfoDet on CRMClientInfo.id=CRMClientInfoDet.f_ref ";
							}else{
								sql = "SELECT id,clientName FROM CRMClientInfo ";
							}
							sql+= "WHERE status != '1' "+condition;
							System.out.println("sql:"+sql);
							PreparedStatement ps = connection.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							ArrayList<String[]> rsList = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] str=new String[3];
								str[0]=rss.getString("id");
								str[1]=rss.getString("clientName");
								if(isMobileQuery){
									str[2]=rss.getString("userName");
								}
								rsList.add(str) ;
							}
							rst.setRetVal(rsList) ;
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
	*/
	public Result queryClientByKeyWord(String condition,boolean isMobileQuery) {
		String sql = "";
		if(isMobileQuery){
			sql = "SELECT top 11 CRMClientInfo.id,CRMClientInfo.clientName,CRMClientInfoDet.userName FROM CRMClientInfo left join CRMClientInfoDet on CRMClientInfo.id=CRMClientInfoDet.f_ref ";
		}else{
			sql = "SELECT top 11 id,clientName FROM CRMClientInfo ";
		}
		sql+= "WHERE status != '1' "+condition;
		System.out.println("xjj:"+sql);
		return sqlList(sql, new ArrayList());
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
}
