package com.menyi.aio.web.upgrade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.UpdateInfo;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class UpgradeMgt extends AIODBManager {
	
	public Result findLastInfo(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
		
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
				
					public void execute(Connection conn) throws SQLException {
						
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("select version_id,order_id,remark from tblUpdateInfo");
						stringBuffer.append(" where version_id = ");
						stringBuffer.append("(select max(version_id) from tblUpdateInfo)");
						stringBuffer.append(" and order_id = ") ;
						stringBuffer.append(" (select top 1 order_id from tblUpdateInfo order by version_id desc,order_id desc)") ;
						try{
							Statement statement = conn.createStatement();
							ResultSet rs = statement.executeQuery(stringBuffer.toString());
							while(rs.next()){
								UpdateInfo info = new UpdateInfo(
										rs.getInt("version_id"),
										rs.getInt("order_id"),
										rs.getString("remark")
								);
								result.setRetVal(info);
							}
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
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
	
	
	public Result setUpgrade(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
				
					public void execute(Connection conn) throws SQLException {
						
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("update tblUpgradeSystem set state = 1 ") ;
						try{
							Statement statement = conn.createStatement();
							statement.executeUpdate(stringBuffer.toString());
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
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
	
	public Result setRestart(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
				
					public void execute(Connection conn) throws SQLException {
						
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("update tblUpgradeSystem set state = 2 ") ;
						try{
							Statement statement = conn.createStatement();
							statement.executeUpdate(stringBuffer.toString());
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
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
	
	public Result queryAccount(final String dogId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
		
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
				
					public void execute(Connection conn) throws SQLException {
						try{
							Statement statement = conn.createStatement();
							ResultSet rs = statement.executeQuery("select dogId,spath from master.dbo.tblServerAccount where dogid like '"+dogId+"_%'");
							ArrayList list = new ArrayList();
							while(rs.next()){								
								list.add(new String[]{rs.getString("dogId"),rs.getString("spath")});
							}
							result.retVal = list;
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
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
	
	public Result defaultLang(final String defLang){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
		
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {				
					public void execute(Connection conn) throws SQLException {
						try{
							Statement statement = conn.createStatement();
							String sql = "update tblSysDeploy set Setting='"+defLang+"' where SysCode='defaultlanguage'";
							statement.executeUpdate(sql);
							sql ="update tblSysSetting set Setting='"+defLang+"' where SysCode='defaultlanguage'";							
							statement.executeUpdate(sql);							
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		if(retCode== ErrorCanst.DEFAULT_SUCCESS){
			BaseEnv.systemSet.get("defaultlanguage").setSetting(defLang);
		}
		return result;
	}
	
	public Result saveLang(final String lang[]){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
		
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {				
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "delete tblLcaleSet";
							PreparedStatement statement = conn.prepareStatement(sql);							
							statement.executeUpdate();
							sql ="insert into  tblLcaleSet(locale) values(?)";							
							statement = conn.prepareStatement(sql);		
							for(int i=0;i<lang.length;i++){
								statement.setString(1, lang[i]);
								statement.executeUpdate();
							}
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
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
	
	public Result queryLanuage(){
		String sql = "select locale from tblLcaleSet";
		ArrayList param = new ArrayList();
		return this.sqlList(sql, param);		
	}

}
