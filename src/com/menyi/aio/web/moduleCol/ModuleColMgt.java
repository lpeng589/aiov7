package com.menyi.aio.web.moduleCol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.ModulePopupDisplay;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class ModuleColMgt extends DynDBManager {

	/**
	 * 添加模块字段
	 * @param moduleId
	 * @param moduleName
	 * @param fieldName
	 * @param fieldLanague
	 * @return
	 */
	public Result add(final String moduleId,final String moduleName,final String moduleDisplay,
				final String[] fieldName,final String[] fieldLanague){
		final Result result = new Result() ;
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "" ;
							Statement pss = conn.createStatement() ;
							for(int i=0;i<fieldName.length;i++){
								String[] fields = fieldName[i].split("-") ;
								sql = "insert into tblModelColLanguage(id,moduleId,moduleName,moduleDisplay,tableName,fieldName,popFieldName,fieldType,languageId,popupName) " +
										"values('"+IDGenerater.getId()+"','"+moduleId+"','"+moduleName+"','"+moduleDisplay+"','"+fields[0]+"','"+fields[1]+"','"+fields[2]+"','"+fields[4]+"','"+fieldLanague[i]+"','"+fields[3]+"')" ;
								pss.addBatch(sql) ;
							}
							pss.executeBatch() ;
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
						} catch (Exception ex) {
							BaseEnv.log.error("ModuleColMgt-------add",ex) ;
							ex.printStackTrace() ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retVal);
		return result ;
	}
	
	/**
	 * 修改模块字段
	 * @param moduleId
	 * @param moduleName
	 * @param fieldName
	 * @param fieldLanague
	 * @return
	 */
	public Result update(final String moduleId,final String moduleName,final String moduleDisplay,
				final String[] fieldName,final String[] fieldLanague){
		final Result result = new Result() ;
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from tblModelColLanguage where moduleId='"+moduleId+"'" ;
							Statement pss = conn.createStatement() ;
							pss.executeUpdate(sql) ;
							for(int i=0;i<fieldName.length;i++){
								String[] fields = fieldName[i].split("-") ;
								sql = "insert into tblModelColLanguage(id,moduleId,moduleName,moduleDisplay,tableName,fieldName,popFieldName,fieldType,languageId,popupName) " +
										"values('"+IDGenerater.getId()+"','"+moduleId+"','"+moduleName+"','"+moduleDisplay+"','"+fields[0]+"','"+fields[1]+"','"+fields[2]+"','"+fields[4]+"','"+fieldLanague[i]+"','"+fields[3]+"')" ;
								pss.addBatch(sql) ;
							}
							pss.executeBatch() ;
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
						} catch (Exception ex) {
							BaseEnv.log.error("ModuleColMgt-------update",ex) ;
							ex.printStackTrace() ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retVal);
		return result ;
	}
	
	/**
	 * 查询模所有块字段
	 * @param moduleName
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public Result queryAllModuleCol(final String moduleName){
		final Result result = new Result() ;
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select moduleId,moduleDisplay from tblModelColLanguage where moduleDisplay like '%"+moduleName+"%' group by moduleId,moduleDisplay" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rss = pss.executeQuery() ;
							ArrayList<String[]> popupList = new ArrayList<String[]>() ;
							while (rss.next()){
								String[] module = new String[2] ;
								module[0] = rss.getString("moduleId") ;
								module[1] = rss.getString("moduleDisplay") ;
								popupList.add(module) ;
							}
							result.setRetVal(popupList) ;
						} catch (Exception ex) {
							BaseEnv.log.error("ModuleColMgt-------queryAllModuleCol ",ex) ;
							ex.printStackTrace() ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retVal);
		return result ;
	}
	
	/**
	 * 删除该模块所有弹出窗口字段
	 * @param moduleName
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public Result delModulePopupField(final String moduleId){
		final Result result = new Result() ;
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from tblModelColLanguage where moduleId in ("+moduleId+")" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							int num = pss.executeUpdate() ;
							if(num>0){
								result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ModuleColMgt-------delModulePopupField ",ex) ;
							ex.printStackTrace() ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retVal);
		return result ;
	}
	
	/**
	 * 查询是否存在该模块的弹出窗口
	 * @param moduleName
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public Result queryIsExistModulePopupById(final String moduleId){
		final Result result = new Result() ;
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblModelColLanguage where moduleId = ?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, moduleId) ;
							ResultSet rs = pss.executeQuery() ;
							if(rs.next()){
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							}else{
								result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ModuleColMgt-------delModulePopupField ",ex) ;
							ex.printStackTrace() ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retVal);
		return result ;
	}
	
	/**
	 * 查询该模块所有自定义的弹出窗口字段
	 * @param moduleName
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public Result queryModulePopField(final String moduleId){
		final Result result = new Result() ;
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblModelColLanguage where moduleId = ?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, moduleId) ;
							ResultSet rss = pss.executeQuery() ;
							ArrayList<ModulePopupDisplay> popupList = new ArrayList<ModulePopupDisplay>() ;
							while(rss.next()){
								ModulePopupDisplay popup = new ModulePopupDisplay() ;
								popup.setModuleId(rss.getString("moduleId")) ;
								popup.setModuleName(rss.getString("moduleName")) ;
								popup.setFieldName(rss.getString("fieldName")) ;
								popup.setPopupFieldName(rss.getString("popFieldName")) ;
								popup.setFieldType(rss.getString("fieldType")) ;
								popup.setLanguage(rss.getString("languageId")) ;
								popup.setModuleDisplay(rss.getString("moduleDisplay")) ;
								popup.setPopupName(rss.getString("popupName")) ;
								popup.setTableName(rss.getString("tableName")) ;
								popupList.add(popup) ;
							}
							result.setRetVal(popupList) ;
						} catch (Exception ex) {
							BaseEnv.log.error("ModuleColMgt-------queryModulePopField ",ex) ;
							ex.printStackTrace() ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retVal);
		return result ;
	}
}
