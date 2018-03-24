package com.menyi.aio.web.colconfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.InitMenData;

public class ColConfigMgt extends DBManager{
	
	/**
	 * 添加
	 * @param colName
	 * @param tableName
	 * @param colType
	 * @param colIndex
	 * @return
	 */
	public Result add(final String fieldName,final String colName,final String display,final String tableName,final String popupName,final int isNull,final String colType,
					  final int colIndex,final String userId){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "insert into tblColConfig(id,fieldName,colName,display,tableName,popupName,colType,colIndex,createBy,isNull) values(?,?,?,?,?,?,?,?,?,?)" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, IDGenerater.getId()) ;
							ps.setString(2, fieldName) ;
							ps.setString(3, colName) ;
							ps.setString(4, display) ;
							ps.setString(5, tableName) ;
							ps.setString(6, popupName) ;
							ps.setString(7, colType) ;
							ps.setInt(8, colIndex) ;
							ps.setString(9, userId) ;
							ps.setInt(10, isNull) ;
							int n = ps.executeUpdate() ;
							if(n>0){
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------add",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}
	
	/**
	 * 删除
	 * @param colName
	 * @param tableName
	 * @param colType
	 * @param colIndex
	 * @return
	 */
	public Result delByTableNameAndColType(final String tableName,final String colType){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from tblColConfig where ? like '%,'+tableName+',%' and colType=?" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, ","+tableName) ;
							ps.setString(2, colType) ;
							int n = ps.executeUpdate() ;
							if(n>0){
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------delByTableNameAndColType",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}
	
	/**
	 * 更新列表的列配置
	 * @param tableName
	 * @param colNames
	 * @param userId
	 * @return
	 */
	public Result updateListConfig(final String tableName,final String colNames,final String userId,final String lockName){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//删除原列配置
							String sql = "delete from tblColConfig where tableName=? and colType=? and userId=? " ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, "list");
							ps.setString(3, userId);
							int n = ps.executeUpdate() ;
							
							sql = "delete from tblUserWidth where tableName=? and colType=?  and userId=? " ;
							ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, "list");
							ps.setString(3, userId);
							ps.executeUpdate() ;
							
							String[] colSelect = colNames.split("\\|");
							for (int i = 0; i < colSelect.length; i++) {
								String[] nameWidth = colSelect[i].split(":");
								if ("autoNum".equals(nameWidth[0])
										|| "update".equals(nameWidth[0])
										|| "detail".equals(nameWidth[0])) {
									continue;
								}
								int lock = 0 ;
								if(nameWidth[0].equals(lockName)){
									lock = 1;
								}
								sql = "insert into tblColConfig(id,colName,tableName,popupName,colType,colIndex,createBy,isNull,lock,userId) values(?,?,?,?,?,?,?,?,?,?)" ;
								ps = conn.prepareStatement(sql) ;
								ps.setString(1, IDGenerater.getId()) ;
								ps.setString(2, nameWidth[0]) ;
								ps.setString(3, tableName) ;
								ps.setString(4, "") ;
								ps.setString(5, "list") ;
								ps.setInt(6, i + 1) ;
								ps.setString(7, userId) ;
								ps.setInt(8, 0) ;
								ps.setInt(9, lock) ;
								ps.setString(10, userId) ;
								
								n = ps.executeUpdate() ;
								
								//添加列宽配置
								
	                    	    sql = "insert into tblUserWidth(id,tableName,colName,colWidth,colType,userId) values(?,?,?,?,?,?)" ;
	                    	    ps = conn.prepareStatement(sql) ;
	                    	    ps.setString(1, IDGenerater.getId()) ;
	                    	    ps.setString(2, tableName);
							    ps.setString(3, nameWidth[0]);
							    ps.setString(4, nameWidth[1]);
							    ps.setString(5, "list") ;
							    ps.setString(6, userId) ;
							    ps.executeUpdate();								
							}
							
							
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------updateListConfig",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;		
	}
	/**
	 * 管理员修改列配置
	 * @param tableName
	 * @param colNames
	 * @return
	 */
	public Result updateListConfigAdmin(final String tableName,final String colNames,final String lockName){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//删除原列配置
							String sql = "delete from tblColConfig where tableName=? and colType=?  " ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, "list");
							int n = ps.executeUpdate() ;
							
							sql = "delete from tblUserWidth where tableName=? and colType=?" ;
							ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, "list");
							ps.executeUpdate() ;
							
							String[] colSelect = colNames.split("\\|");
							for (int i = 0; i < colSelect.length; i++) {
								String[] nameWidth = colSelect[i].split(":");
								if ("autoNum".equals(nameWidth[0])
										|| "update".equals(nameWidth[0])
										|| "detail".equals(nameWidth[0])) {
									continue;
								}
								int lock = 0 ;
								if(nameWidth[0].equals(lockName)){
									lock = 1;
								}
								sql = "insert into tblColConfig(id,colName,tableName,popupName,colType,colIndex,createBy,isNull,lock) values(?,?,?,?,?,?,?,?,?)" ;
								ps = conn.prepareStatement(sql) ;
								ps.setString(1, IDGenerater.getId()) ;
								ps.setString(2, nameWidth[0]) ;
								ps.setString(3, tableName) ;
								ps.setString(4, "") ;
								ps.setString(5, "list") ;
								ps.setInt(6, i + 1) ;
								ps.setString(7, "1") ;
								ps.setInt(8, 0) ;
								ps.setInt(9, lock) ;
								n = ps.executeUpdate() ;
								
								//添加列宽配置
								
	                    	    sql = "insert into tblUserWidth(id,tableName,colName,colWidth,colType) values(?,?,?,?,?)" ;
	                    	    ps = conn.prepareStatement(sql) ;
	                    	    ps.setString(1, IDGenerater.getId()) ;
	                    	    ps.setString(2, tableName);
							    ps.setString(3, nameWidth[0]);
							    ps.setString(4, nameWidth[1]);
							    ps.setString(5, "list") ;
							    ps.executeUpdate();

								// 更新列宽缓存
								if (BaseEnv.userSettingWidth != null) {
									ColDisplayBean colBean = BaseEnv.userSettingWidth.get(tableName
											+ nameWidth[0]);
									if (colBean != null) {
										colBean.setColWidth(nameWidth[1]);
									} else {
										ColDisplayBean display = new ColDisplayBean();
										display.setTableName(tableName);
										display.setColType("list");
										display.setId(IDGenerater.getId());
										display.setColName(nameWidth[0]);
										display.setColWidth(nameWidth[1]);
										BaseEnv.userSettingWidth.put(tableName + nameWidth[0],
												display);
									}
								}
							}						
							
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------updateListConfig",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;		
	}
	
	/**
	 * 取用户个性自定义
	 * @param tableName
	 * @param userId
	 * @return
	 */
	public Result getUserConfig(final String tableName,final String userId){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//删除原列配置
							String sql = "select a.colName,b.colWidth,a.lock from tblColConfig a left join tblUserWidth b  on a.tableName=b.tableName " +
									" and a.colName = b.colName and a.colType=b.colType and a.userId=b.UserId where a.tableName=? and " +
									" a.userId=? and a.colType='list' order by a.colIndex" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, userId);
							ResultSet rset = ps.executeQuery() ;
							ArrayList list = new ArrayList();
							while(rset.next()){
								list.add(new String[]{rset.getString(1),rset.getString(2),rset.getString(3)});
							}
							rs.retVal = list;
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------getUserConfig",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;		
	}
	
	/**
	 * 删除
	 * @param colName
	 * @param tableName
	 * @param colType
	 * @param colIndex
	 * @return
	 */
	public Result delByTableName(final String tableName,final String colType,final String userId){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement ps;						
							if("1".equals(userId)){
								String sql = "delete from tblColConfig where tableName=? and colType=?" ;
								ps = conn.prepareStatement(sql) ;
								ps.setString(1, tableName) ;
								ps.setString(2, colType) ;
							}else{
								String sql = "delete from tblColConfig where tableName=? and colType=? and userId=?" ;
								ps = conn.prepareStatement(sql) ;
								ps.setString(1, tableName) ;
								ps.setString(2, colType) ;
								ps.setString(3, userId);
							}
							int n = ps.executeUpdate() ;
							if(n>0){
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------delByTableName",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}
	
	/**
	 * 根据表查询用户表和明细表的配置
	 * @param colName
	 * @param tableName
	 * @param colType
	 * @param colIndex
	 * @return
	 */
	public Result queryAllByTableName(final String tableName,final String colType){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblColConfig where ? like '%'+tableName+',%' and colType=? order by colIndex asc" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, colType) ;
							ResultSet rss = ps.executeQuery() ;
							ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
							while(rss.next()){
								ColConfigBean configBean = new ColConfigBean() ;
								configBean.setId(rss.getString("id")) ;
								configBean.setTableName(rss.getString("tableName")) ;
								configBean.setColName(rss.getString("colName")) ;
								configBean.setColType(rss.getString("colType")) ;
								configBean.setColIndex(rss.getInt("colIndex"));
								allConfigList.add(configBean) ;
							}
							if(allConfigList.size()>0){
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
								rs.setRetVal(allConfigList) ;
							}else{
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------queryAllByTableName",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}
	
	/**
	 * 查询
	 * @param colName
	 * @param tableName
	 * @param colType
	 * @param colIndex
	 * @return
	 */
	public Result queryColExistByTableName(final String tableName,final String colType,final String colName){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblColConfig where tableName=? and colType=? and colName=?" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, colType) ;
							ps.setString(3, colName) ;
							ResultSet rss = ps.executeQuery() ;
							if(rss.next()){
								int index = rss.getInt("colIndex") ;
								rs.setRetVal(index) ;
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
							}else{
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------queryColExistByTableName",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}
	
	/**
	 * 查询
	 * @param colName
	 * @param tableName
	 * @param colType
	 * @param colIndex
	 * @return
	 */
	public Result queryConfigExistByTableName(final String tableName,final String colType){
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblColConfig where tableName=? and colType=? order by colIndex asc" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, colType) ;
							ResultSet rss = ps.executeQuery() ;
							ArrayList<ColConfigBean> colList = new ArrayList<ColConfigBean>() ;
							while(rss.next()){
								ColConfigBean colBean = new ColConfigBean() ;
								colBean.setId(rss.getString("id")) ;
								colBean.setTableName(rss.getString("tableName")) ;
								colBean.setColName(rss.getString("colName")) ;
								colBean.setColType(rss.getString("colType")) ;
								colBean.setColIndex(rss.getInt("colIndex")) ;
								colBean.setPopupName(rss.getString("popupName"));
								colList.add(colBean) ;
							}
							if(colList.size()>0){
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
							}else{
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							}
							rs.setRetVal(colList) ;
						} catch (Exception ex) {
							BaseEnv.log.error("ColConfigMgt-------queryConfigExistByTableName",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}
}
