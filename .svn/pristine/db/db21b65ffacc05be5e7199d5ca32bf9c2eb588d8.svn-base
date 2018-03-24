package com.menyi.aio.web.coldisplay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.GlobalsTool;

public class ColDisplayMgt extends DBManager{

	public Result add(final String tableName,final String colName,final KRLanguage language){
		final Result rs=new Result();
		   int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try {
	                    	   String selSql = "select id,languageId from tblUserLanguage where tableName=? and colName=?" ;
	                    	   PreparedStatement ps2 = conn.prepareStatement(selSql) ;
	                    	   ps2.setString(1, tableName) ;
	                    	   ps2.setString(2, colName) ;
	                    	   ResultSet rss = ps2.executeQuery() ;
	                    	   if(rss.next()){
	                    		   //String delsql = "delete from tblLanguage where id=?" ;//删除多语言
	                    		   //PreparedStatement ps3 = conn.prepareStatement(delsql) ;
	                    		   //ps3.setString(1, rss.getString("languageId")) ;
	                    		   //ps3.executeUpdate() ;
	                    		   String delsql2 = "delete from tblUserLanguage where id=?" ;//删除用户以前已定义的列名
	                    		   PreparedStatement ps4 = conn.prepareStatement(delsql2) ;
	                    		   ps4.setString(1, rss.getString("id")) ;
	                    		   ps4.executeUpdate() ;
	                    	   }
	                    	   
	                    	   KRLanguageQuery.saveToDB(language.map, language.getId(), conn);
	                    	   String sql = "insert into tblUserLanguage(id,tableName,colName,languageId) values(?,?,?,?)" ;
	                    	   PreparedStatement ps = conn.prepareStatement(sql) ;
	                    	   ps.setString(1, IDGenerater.getId()) ;
	                    	   ps.setString(2, tableName) ;
	                    	   ps.setString(3, colName) ;
	                    	   ps.setString(4, language.getId()) ;
	                    	   int n = ps.executeUpdate() ;
	                    	   if(n>0){
	                    		   rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
	                    	   }
	                       } catch (Exception ex) {
	                    	   BaseEnv.log.error("ColDisplayMgt-------add",ex) ;
	                           rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
	                           return;
	                       }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
	       return rs;
	}
	
	public Result updateTableName(final String lanStr, final String locale) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String[] userLanguages = lanStr.split("#");
							for (int i = 0; i < userLanguages.length; i++) {
								String[] fieldName = userLanguages[i].split("@");
								if (fieldName.length != 3) {
									continue;
								}
								String sql = "update tblLanguage set " + locale
										+ "=? where id=(select languageid from tbldbfieldinfo where fieldName=? and tableId=(select id from tbldbtableinfo where tablename=?))";
								PreparedStatement ps = conn.prepareStatement(sql);
								ps.setString(1, fieldName[2]);
								ps.setString(2, fieldName[0]);
								ps.setString(3, fieldName[1]);
								ps.executeUpdate();
								GlobalsTool.getFieldBean(fieldName[1],fieldName[0]).getDisplay().putLanguage(locale, fieldName[2]);
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ColDisplayMgt-------updateTableName", ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	
	/**
	 * 加入用户自定义的列宽设置
	 * 
	 * @param tableName
	 * @param colName
	 * @return
	 */
	public Result addUserSetColWidth(final ColDisplayBean display){
		final Result rs=new Result();
		   int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try { 
	                    	   String sql = "delete from tblUserWidth where tableName=? and colName=? and colType=?" ;
	                    	   PreparedStatement ps_del = conn.prepareStatement(sql) ;
	                    	   ps_del.setString(1, display.getTableName()) ;
	                    	   ps_del.setString(2, display.getColName()) ;
	                    	   ps_del.setString(3, display.getColType()) ;
	                    	   ps_del.executeUpdate() ;
	                    	   sql = "insert into tblUserWidth(id,tableName,colName,colWidth,colType) values(?,?,?,?,?)" ;
	                    	   PreparedStatement ps = conn.prepareStatement(sql) ;
	                    	   ps.setString(1, IDGenerater.getId()) ;
	                    	   ps.setString(2, display.getTableName());
							   ps.setString(3, display.getColName());
							   ps.setString(4, display.getColWidth());
							   ps.setString(5, display.getColType()) ;
							   ps.executeUpdate();
	                    	   rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
	                       } catch (Exception ex) {
	                    	   BaseEnv.log.error("ColDisplayMgt-------addUserSetColWidth",ex) ;
	                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                           return;
	                       }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
	       return rs;
	}
	
	/**
	 * 还原报表缺省列宽
	 * @param tableName
	 * @param colName
	 * @return
	 */
	public Result defaultColWidth(final String tableName,final String colType,final String userId){
		final Result rs=new Result();
		   int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try { 
	                    	   PreparedStatement ps_del;
	                    	   if("1".equals(userId)){
		                    	   String sql = "delete from tblUserWidth where tableName=? and colType=?" ;
		                    	   ps_del = conn.prepareStatement(sql) ;
		                    	   ps_del.setString(1, tableName) ;
		                    	   ps_del.setString(2, colType) ;
	                    	   }else{
	                    		   String sql = "delete from tblUserWidth where tableName=? and colType=? and userId=?" ;
		                    	   ps_del = conn.prepareStatement(sql) ;
		                    	   ps_del.setString(1, tableName) ;
		                    	   ps_del.setString(2, colType) ;
		                    	   ps_del.setString(3, userId) ;
	                    	   }
	                    	   ps_del.executeUpdate() ;
	                    	   rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
	                       } catch (Exception ex) {
	                    	   BaseEnv.log.error("ColDisplayMgt-------defaultColWidth",ex) ;
	                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                           return;
	                       }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
	       return rs;
	}
	
	

	
	/**
	 * 还原新增缺省列宽
	 * @param tableName
	 * @param colName
	 * @return
	 */
	public Result defaultColWidth2(final String tableName,final String colType){
		final Result rs=new Result();
		   int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try { 
	                    	   String sql = "delete from tblUserWidth where tableName+',' like ? and colType=?" ;
	                    	   PreparedStatement ps_del = conn.prepareStatement(sql) ;
	                    	   ps_del.setString(1, "%"+tableName+"%") ;
	                    	   ps_del.setString(2, colType) ;
	                    	   ps_del.executeUpdate() ;
	                    	   rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
	                       } catch (Exception ex) {
	                    	   BaseEnv.log.error("ColDisplayMgt-------defaultColWidth2",ex) ;
	                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                           return;
	                       }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
	       return rs;
	}
}
