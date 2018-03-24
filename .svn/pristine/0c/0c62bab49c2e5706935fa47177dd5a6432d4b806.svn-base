package com.menyi.aio.web.tab;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

/**
 * <p>Title: </p>
 *
 * <p>Description:TAB配置的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 吴志雄</p>
 *
 * @author 吴志雄
 * @version 1.0
 */
public class TabMgt extends DBManager {

    /**
    *
    * @param brotherTablesList ArrayList
    * @param mainTabKeyId String
    * @return Result
    */
   public Result getBrotherDefine(final String tableName,final String f_brother) {
       final Result rs = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                           SQLException {
                       Connection conn = connection;
                       String sql = "select sql from tblBrotherDefine where tableName=?" ;
                       try {
                    	   PreparedStatement cs = conn.prepareStatement(sql);
                    	   cs.setString(1, tableName);
                           ResultSet rss = cs.executeQuery();                           
                           if (rss.next()) {
                               sql  = rss.getString(1);
                               sql = sql.replaceAll("@ValueofDB:id", f_brother);
                               cs = conn.prepareStatement(sql);
                               rss = cs.executeQuery();
                               ArrayList list = new ArrayList();
                               while(rss.next()){
                            	   list.add(rss.getString(1));
                               }
                               rs.retVal = list;
                           }
                       } catch (SQLException ex) {
                           BaseEnv.log.error("TabMgt.getBrotherDefine Error :" +
                                             sql, ex);
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
     * 得到邻居表行数
     * @param brotherTablesList ArrayList
     * @param mainTabKeyId String
     * @return Result
     */
    public Result getBrotherRow(final String tableName,final String brotherId) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String sql = "select top 2 id from "+ tableName + " where f_brother = ?";
                        try {
                        	PreparedStatement cs = conn.prepareStatement(sql);
                        	cs.setString(1, brotherId);
                            ResultSet rss = null;
                            ArrayList values = new ArrayList();
                            ResultSet rset = cs.executeQuery();
                            while(rset.next()){
                            	values.add(rset.getString(1));
                            }
                            rs.setRetVal(values);
                        } catch (SQLException ex) {
                            BaseEnv.log.error("Query data Error :" +
                                              sql, ex);
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
     * 得到副TAB的主健ID
     * @param brotherTablesList ArrayList
     * @param mainTabKeyId String
     * @return Result
     */
    public Result getChildTabKeyId(ArrayList brotherTablesList,
                                   String mainTabKeyId) {
        final Result rs = new Result();
        final ArrayList brotherTables = brotherTablesList;
        final String relationTabID = mainTabKeyId;
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String sql = "";
                        try {
                            Statement cs = conn.createStatement();
                            ResultSet rss = null;
                            ArrayList values = new ArrayList();
                            for (int i = 0; i < brotherTables.size(); i++) {
                                DBTableInfoBean ti = (DBTableInfoBean)
                                        brotherTables.get(i);
                                Object[] value = new Object[2];
                                value[0] = ti.getTableName(); //表名
                                value[1] = null; //TAB表keyId
                                if (null != relationTabID &&
                                    !"".equals(relationTabID)) {
                                    sql = " select id from " + ti.getTableName() +
                                          "  where f_brother= '" +
                                          relationTabID + "'";
                                    rss = cs.executeQuery(sql);
                                    if (rss.next()) {
                                        value[1] = rss.getString(1);
                                    }
                                }
                                values.add(value);
                            }
                            rs.setRetVal(values);
                        } catch (SQLException ex) {
                            BaseEnv.log.error("Query data Error :" +
                                              sql, ex);
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
     *
     * @param brotherTablesList ArrayList
     * @param mainTabKeyId String
     * @return Result
     */
    public Result getMainTableFirstId(final String tableName) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String sql = "select top 1 id from " + tableName;
                        try {
                            Statement cs = conn.createStatement();
                            ResultSet rss = null;
                            rss = cs.executeQuery(sql);
                            if (rss.next()) {
                                rs.setRetVal(rss.getString("id"));
                            }else{
                            	rs.setRetVal("");
                            }

                        } catch (SQLException ex) {
                            BaseEnv.log.error("Query data Error :" +
                                              sql, ex);
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
     * 调用存储过程把往来单位的全部数据导入到CRM目标客户中
     * @param tableName
     * @return
     */
    public Result transferCRM() {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;  
                        try {
                            CallableStatement cs = conn.prepareCall("{call proc_transferCRM}") ;
                            cs.execute() ;
                            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        } catch (SQLException ex) {   
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
     * 更新CRM中目标，意向客户...归属人。
     * @param tableName 主表
     * @param childTableName 子表
     * @param keyIds  要改变客户的Id
     * @param createBy 归属人
     * @return
     */
    public Result updateCRMNewCreateBy(final String tableName, final ArrayList<DBTableInfoBean> brotherTable,
			final String keyIds,final String createBy) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement() ;
							String sql = "update "+tableName+" set createBy='"+createBy+"' where id in ("+keyIds+")" ;
							st.executeUpdate(sql) ;
							for(DBTableInfoBean bean : brotherTable){
								if(bean.getIsView()==0){
									sql = "update "+bean.getTableName()+" set createBy='"+createBy+"' where f_brother in ("+keyIds+")" ;
									st.executeUpdate(sql) ;
								}
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("tabMgt---updateCRMNewCreateBy method :"+ ex);
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
     * 查询所要移交的客户是否已审核
     * @param tableName 主表
     * @param childTableName 子表
     * @param keyIds  要改变客户的Id
     * @param createBy 归属人
     * @return
     */
    public Result selectCRMCustomerIsApproved(final String tableName, final String keyIds) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from "+tableName+" where workFlowNodeName='notApprove' and id in ("+keyIds+")" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rss = ps.executeQuery() ;
							if(rss.next()){
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
							}else{
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							}
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("tabMgt---selectCRMCustomerIsApproved method :"+ ex);
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
     * 查询客户的Email,手机号码
     * @param tableName 主表
     * @param childTableName 子表
     * @param keyIds  要改变客户的Id
     * @param createBy 归属人
     * @return
     */
    public Result selectSMSorEmailById(final String tableName, final String keyIds,final String type) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select "+type+" from ViewCRMCompanyAll where CompanyCategory='"+tableName+"' and id in ("+keyIds+")" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rss = ps.executeQuery() ;
							ArrayList<String> strList = new ArrayList<String>() ;
							while(rss.next()){
								strList.add(rss.getString(type)) ;
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("tabMgt---selectSMSorEmailById method :"+ ex);
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
     * 查询所有的兄弟表配置
     * @param allTables
     * @param tableName
     * @return
     */
    public Result selectHavingBrothertable(final Hashtable allTables,final String tableName){
    	final Result rs= new Result();
    	int retCode = DBUtil.execute(new IfDB(){
    		public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							String sql="select tableName from tblDBTableInfo" +
									" where tblDBTableInfo.perantTableName like "+"'%"+tableName+"%'"+" and tblDBTableInfo.tableType=2";
							PreparedStatement ps=conn.prepareStatement(sql);
							ResultSet rss=ps.executeQuery();
							ArrayList<DBTableInfoBean> strList = new ArrayList<DBTableInfoBean>() ;
							while(rss.next()){
								String brotherName = rss.getString("tableName");
								DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, brotherName) ;
								if(tableInfo!=null){
									strList.add(tableInfo) ;
								}
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("tabMgt---selectHavingBrothertable method :"+ ex);
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
     * 查询兄弟表配置
     * @param tableName 主表
     * @return
     */
    public Result selectBrotherTable(final Hashtable allTables,final String tableName,final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select detailName from tblNeighbourDetail where f_ref =("+
											"select id from tblNeighbourMain where MainName =? and createBy=?) order by orderBy" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, userId);
							ResultSet rss = ps.executeQuery() ;
							
							ArrayList<DBTableInfoBean> strList = new ArrayList<DBTableInfoBean>() ;
							while(rss.next()){
								String brotherName = rss.getString("detailName");
								DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, brotherName) ;
								if(tableInfo!=null){
									strList.add(tableInfo) ;
								}
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("tabMgt---selectBrotherTable method :"+ ex);
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
     * 根据客户id和视图Id获取兄弟表
     * @param allTables
     * @param tableName
     * @param userId
     * @return
     */
    public Result selectBroTabByViewId(final Hashtable allTables,final String tableName,final String userId,final String viewId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select detailName from tblNeighbourDetail where f_ref =("+
											"select id from tblNeighbourMain where MainName =? and createBy=? and viewId=?) order by orderBy" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, userId);
							ps.setString(3, viewId);
							ResultSet rss = ps.executeQuery() ;
							
							ArrayList<DBTableInfoBean> strList = new ArrayList<DBTableInfoBean>() ;
							while(rss.next()){
								String brotherName = rss.getString("detailName");
								DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, brotherName) ;
								if(tableInfo!=null){
									strList.add(tableInfo) ;
								}
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("tabMgt---selectBrotherTable method :"+ ex);
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
     * 查询兄弟表配置
     * @param tableName 主表
     * @return
     */
    public Result selectBrotherTableName(final Hashtable allTables,final String tableName,final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select detailName from tblNeighbourDetail where f_ref =("+
											"select id from tblNeighbourMain where MainName =? and createBy=?) order by orderBy" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, userId);
							ResultSet rss = ps.executeQuery() ;
							
							List<String> strList = new ArrayList<String>() ;
							while(rss.next()){
								String brotherName = rss.getString("detailName");
								strList.add(brotherName) ;
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("tabMgt---selectBrotherTable method :"+ ex);
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
     * 根据用户查询兄弟表配置
     * @param tableName 主表
     * @return
     */
    public Result selectNeTable2(final String tableName,final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select statusId from tblNeighbourMain where MainName =? and createBy=?" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, userId);
							ResultSet rss = ps.executeQuery() ;
							if(rss.next()){
								rs.setRetVal(rss.getInt("statusId")) ;
							}else{
								rs.setRetVal(3) ;
							}
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("tabMgt---selectTable2 method :"+ ex);
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
     * 根据用户查询兄弟表配置
     * @param tableName 主表
     * @return
     */
    public Result selectNeTable(final Hashtable allTables,final String tableName,final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select detailName,orderby from tblNeighbourDetail where f_ref =("+
											"select id from tblNeighbourMain where MainName =? and createBy=?) order by orderBy" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, tableName) ;
							ps.setString(2, userId);
							ResultSet rss = ps.executeQuery() ;
							
							ArrayList<String[]> strList = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] str=new String[2];
								str[0] = rss.getString("detailName");
								str[1] = rss.getString("orderby");
								strList.add(str) ;
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("tabMgt---selectTable method :"+ ex);
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
     * 添加邻居表详细
     * @param detailname
     * @param parentid
     * @param i
     * @return
     */
    public Result insertNeighbourDetail(final String userId,final String tableName, 
    		final String[] detailname,final Integer[] orders,final int tabStatus){
    	final Result rst = new Result();
    	
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	String parentid=null;
                        	String sql = "select id from tblNeighbourMain where createBy=? and MainName=?" ;
							PreparedStatement pstmt = conn.prepareStatement(sql) ;
							pstmt.setString(1, userId);
							pstmt.setString(2, tableName);
							ResultSet rss = pstmt.executeQuery() ;
							if(!rss.next()){
									sql = "insert into tblNeighbourMain(id,MainName,createBy,createTime,statusId) values(?,?,?,?,?)";
									String creatTime = BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss);
		                            pstmt = conn.prepareStatement(sql);
		                            parentid=IDGenerater.getId();
		                            pstmt.setString(1, parentid);
		                            pstmt.setString(2, tableName);
		                            pstmt.setString(3, userId);
		                            pstmt.setString(4, creatTime);
		                            pstmt.setInt(5, tabStatus) ;
		                            pstmt.executeUpdate() ;
							}else{
								parentid=rss.getString("id");
							}
							sql = "delete from tblNeighbourDetail where f_ref=?" ;
							pstmt = conn.prepareStatement(sql) ;
							pstmt.setString(1, parentid) ;
							pstmt.executeUpdate() ;
							sql = "update tblNeighbourMain set statusId=? where createBy=? and MainName=?" ;
							pstmt = conn.prepareStatement(sql) ;
							pstmt.setInt(1, tabStatus);
							pstmt.setString(2, userId);
							pstmt.setString(3, tableName);
							pstmt.executeUpdate();
							Statement state = conn.createStatement() ;
							for(int i=0;i<detailname.length;i++){
								sql = "insert into tblNeighbourDetail(id, f_ref, DetailName, OrderBy) values('"+IDGenerater.getId()+"','"+parentid+"','"+detailname[i]+"','"+orders[i]+"')";
								state.addBatch(sql) ;
							}
                            state.executeBatch() ;
                            
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
    
}
