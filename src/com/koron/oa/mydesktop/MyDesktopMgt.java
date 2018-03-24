package com.koron.oa.mydesktop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.MyDeskBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
/**
 * 
 * <p>Title:我的桌面</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 12, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class MyDesktopMgt extends AIODBManager {
	
	/**
	 * 设置倒记时
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result updateCountDown(final String type,final String title,
			final String date) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update CRMCountdownDate set CountdownType=?,CountdownTitle=?,CountdownDate=?" ;
							
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, type);
							pss.setString(2, title);
							pss.setString(3, date);
							pss.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("MyDesktopMgt queryMyYearGoal:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	
	/**
	 * 更改我的头像
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result updateEmployeeImage(final String userId,final String image) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update tblEmployee set photo=? where id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, image) ;
							pss.setString(2, userId) ;
							pss.executeUpdate() ;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("MyDesktopMgt updateEmployeeImage:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
   
	/**
     * 查询桌面应用程序
     * @param loginId String
     * @return Result
     */
    public Result selectDesktopProgram(final String userId) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "select a.programeName,a.programePath,a.programeType,a.Logo from tblOtherProgramePaths a " +
                            			 "left join tblOtherProgramePathsDet b on a.id=b.f_ref where (a.createBy=? and b.id is null) or b.userName=? or (a.createBy=? "+
                            			 "and b.id=(select top 1 id from tblOtherProgramePathsDet group by f_ref,id))" ;
                            PreparedStatement pss = conn.prepareStatement(sql) ;
                            pss.setString(1, userId) ;
                            pss.setString(2, userId) ;
                            pss.setString(3, userId) ;
                            ResultSet rss = pss.executeQuery() ;
                            ArrayList<String[]> listPrograme = new ArrayList<String[]>() ;
                            while(rss.next()){
                            	String[] str = new String[4] ;
                            	str[0] = rss.getString("programeName") ;
                            	str[1] = rss.getString("programePath") ;
                            	str[2] = rss.getString("logo") ;
                            	str[3] = rss.getString("programeType") ;
                            	listPrograme.add(str) ;
                            }
                            rs.setRetVal(listPrograme) ;
                            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                            BaseEnv.log.error("MyDesktopMgt selectDesktopProgram :",ex) ;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    /**
     * 查询桌面应用程序
     * @param loginId String
     * @return Result
     */
    public Result deleteDeskModule(final String moduleId,final String colType,final String userId) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "delete from tblMydeskConfig where createBy=? and moduleId=? and colType=?" ;
                            PreparedStatement pss = conn.prepareStatement(sql) ;
                            pss.setString(1, userId) ;
                            pss.setString(2, moduleId) ;
                            pss.setString(3, colType) ;
                            pss.executeUpdate() ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                            BaseEnv.log.error("MyDesktopMgt deleteDeskModule :",ex) ;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    /**
     * 修改我的桌面的模块显示记录数
     * @param loginId String
     * @return Result
     */
    public Result updateDeskRowCount(final String moduleId,final String rowCount,final String userId) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "update tblMydeskConfig set dataRowCount=? where createBy=? and moduleId=?" ;
                            PreparedStatement pss = conn.prepareStatement(sql) ;
                            pss.setString(1, rowCount) ;
                            pss.setString(2, userId) ;
                            pss.setString(3, moduleId) ;
                            pss.executeUpdate() ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                            BaseEnv.log.error("MyDesktopMgt updateDeskModule :",ex) ;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    /**
     * 查询默认工作台模块
     * @param userId
     * @return
     */
    public Result queryDefaultDesk(final String userId,final String colType) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String strUserId = userId ;
							String sql = "select modulName from tblMydeskConfig where createBy=? and colType=? and status=0" ;
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, userId) ;
							ps.setString(2, colType) ;
							ResultSet rss = ps.executeQuery();
							if(!rss.next()){
								strUserId = "0" ;
							}
							sql = "select * from tblMyDeskConfig where createBy='0' and status=0 and moduleId not in (select moduleId from tblMydeskConfig where createBy=? and colType=?)";
							ps = conn.prepareStatement(sql);
							ps.setString(1, strUserId) ;
							ps.setString(2, colType) ;
							rss = ps.executeQuery();
							List<MyDeskBean> allDesk = new ArrayList<MyDeskBean>() ;
							while(rss.next()) {
								MyDeskBean bean = new MyDeskBean();
								bean.setId(rss.getString("id"));
								bean.setDataRowCount(rss.getInt("dataRowCount"));
								bean.setLinkAddress(rss.getString("linkAddress"));
								bean.setMoreLinkAddress(rss.getString("moreLinkAddress"));
								bean.setModulOrderBy(rss.getInt("modulOrderBy"));
								bean.setModulName(rss.getString("modulName"));
								bean.setModulSql(rss.getString("modulSql"));
								bean.setStatus(rss.getInt("status"));
								bean.setModuleType(rss.getString("moduleType")) ;
								bean.setColNumber(rss.getInt("colNumber")) ;
								allDesk.add(bean) ;
							}
							rs.setRetVal(allDesk);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		return rs;
	}
    
    /**
     * 设置我的工作台模块
     * @param userId
     * @return
     */
    public Result addMyDesk(final String column1,final String column2,final String column3,
    		final double width1,final double width2,final double width3,
    		final String userId,final String colType) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from tblMyDeskConfig where createBy=? and colType=?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId) ;
							pss.setString(2, colType) ;
							pss.executeUpdate() ;
							String[] array1 = column1.split("\\|") ;
							for(int i=0;i<array1.length;i++){
								if(array1[i].trim().length()==0) continue ;
								sql = "insert into tblMyDeskConfig(id,modulOrderBy,createBy,createTime,colNumber,moduleId,colType,modulName,dataRowCount,status,colWidth) values(?,?,?,?,?,?,?,'',5,0,?)" ;
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, IDGenerater.getId()) ;
								pss.setInt(2, i) ;
								pss.setString(3, userId) ;
								pss.setString(4, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)) ;
								pss.setInt(5, 1) ;
								pss.setString(6, array1[i]) ;
								pss.setString(7, colType) ;
								pss.setDouble(8, width1) ;
								pss.executeUpdate() ;
							}
							String[] array2 = column2.split("\\|") ;
							for(int i=0;i<array2.length;i++){
								if(array2[i].trim().length()==0) continue ;
								sql = "insert into tblMyDeskConfig(id,modulOrderBy,createBy,createTime,colNumber,moduleId,colType,modulName,dataRowCount,status,colWidth) values(?,?,?,?,?,?,?,'',5,0,?)" ;
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, IDGenerater.getId()) ;
								pss.setInt(2, i) ;
								pss.setString(3, userId) ;
								pss.setString(4, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)) ;
								pss.setInt(5, 2) ;
								pss.setString(6, array2[i]) ;
								pss.setString(7, colType) ;
								pss.setDouble(8, width2) ;
								pss.executeUpdate() ;
							}
							String[] array3 = column3.split("\\|") ;
							for(int i=0;i<array3.length;i++){
								if(array3[i].trim().length()==0) continue ;
								sql = "insert into tblMyDeskConfig(id,modulOrderBy,createBy,createTime,colNumber,moduleId,colType,modulName,dataRowCount,status,colWidth) values(?,?,?,?,?,?,?,'',5,0,?)" ;
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, IDGenerater.getId()) ;
								pss.setInt(2, i) ;
								pss.setString(3, userId) ;
								pss.setString(4, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)) ;
								pss.setInt(5, 3) ;
								pss.setString(6, array3[i]) ;
								pss.setString(7, colType) ;
								pss.setDouble(8, width3) ;
								pss.executeUpdate() ;
							}
							
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		return rs;
	}
    
    /**
     * 查询工作台模块
     * @param userId
     * @return
     */
    public Result queryMyDesk(final String userId,final String colType) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String strUserId = userId ;
							String sql = "select * from tblMydeskConfig where createBy=? and colType=?" ;
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, userId);
							ps.setString(2, colType) ;
							ResultSet rss = ps.executeQuery();
							if(!rss.next()){
								strUserId = "0" ;
							}
							sql = " select config1.id,config2.dataRowCount,config1.linkAddress,config1.moreLinkAddress,config1.modulName,config2.colNumber,config2.colWidth,config1.moduleType"
								+ " from tblMyDeskConfig config1 left join tblMyDeskConfig config2 on config1.id=config2.moduleId "
							    + " where config2.createBy = ? and config2.colType=? and config1.status=0"
							    + " order by config2.colNumber,config2.modulOrderBy asc ";
							ps = conn.prepareStatement(sql);
							ps.setString(1, strUserId);
							ps.setString(2, colType) ;
							rss = ps.executeQuery();
							HashMap<Integer, List<MyDeskBean>> map = new HashMap<Integer, List<MyDeskBean>>() ;
							List<MyDeskBean> allDesk = new ArrayList<MyDeskBean>() ;
							while(rss.next()) {
								MyDeskBean bean = new MyDeskBean();
								bean.setId(rss.getString("id"));
								bean.setDataRowCount(rss.getInt("dataRowCount"));
								bean.setLinkAddress(rss.getString("linkAddress"));
								bean.setMoreLinkAddress(rss.getString("moreLinkAddress"));
								//bean.setModulOrderBy(rss.getInt("modulOrderBy"));
								bean.setModulName(rss.getString("modulName"));
								//bean.setModulSql(rss.getString("modulSql"));
								//bean.setStatus(rss.getInt("status"));
								bean.setModuleType(rss.getString("moduleType")) ;
								bean.setColNumber(rss.getInt("colNumber")) ;
								bean.setWidth(rss.getInt("colWidth")) ;
								if(map.get(bean.getColNumber())==null){
									List<MyDeskBean> deskList = new ArrayList<MyDeskBean>();
									deskList.add(bean) ;
									map.put(bean.getColNumber(), deskList) ;
								}else{
									List<MyDeskBean> deskList = map.get(bean.getColNumber()) ;
									deskList.add(bean);
									map.put(bean.getColNumber(), deskList) ;
								}
								allDesk.add(bean) ;
							}
							map.put(0, allDesk) ;
							rs.setRetVal(map);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		return rs;
	}
    
    // 注意，如果要自定义sql语句，当以@userId结尾时，sql语句后需加一个空格
    public Result queryDesk(final String deskId, final int row, final String userId) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "select modulSql,modulName from tblMyDeskConfig where id = ?" ;
                            PreparedStatement pss = conn.prepareStatement(sql) ;
                            pss.setString(1, deskId) ;
                            ResultSet rss = pss.executeQuery() ;
                            if(rss.next()){
                            	sql = rss.getString("modulSql") ;
                            	String moduleName = rss.getString("modulName") ;
	                            List<Object[]> ls = new ArrayList<Object[]>();
	                            //先通过@userId得到要添加几个参数
	                            int numOfParam = 0;
	                    		if(sql.contains("@userId")){
	                    			numOfParam = sql.split("@userId").length-1;
	                    		}
	                    		final String newsql;	                    		
	                    		//if("OAWorkPlanAction".equals(sql.substring(9,25))){
	                    		//	newsql = "select top " + row + sql.substring(6,29)+"dsFlag=dsflag&"+sql.substring(30);
	                    			//newsql = "select "+ sql.substring(6,136)+" from tblDayWorkPlan where employeeId=@userId and statusid <> 1 order by beginDate asc";
	                    		//}else{
	                    			newsql = "select top " + row + sql.substring(6);	                    			
	                    		//}	                            
	                    		final String lastSql = newsql.replace("@userId", "?");//把语句中的@userId改为?
	                    		
	                            PreparedStatement ps = conn.prepareStatement(lastSql) ;
	                            for (int i = 0; i < numOfParam; i++) {
									ps.setObject(i+1, userId);
								}
	                            
	                            rss = ps.executeQuery() ;
	                            int num = rss.getMetaData().getColumnCount();
	                            while(rss.next()){
	                            	Object[] objs = new Object[num+1];
	                            	objs[0] = moduleName ;
	                            	for (int i = 1; i <= num; i++) {
	                            		String column = rss.getMetaData().getColumnName(i);
	                            		if("visitCount".equals(column)){
	                            			String visitCount = rss.getString(i);
	                            			objs[i] = (visitCount!=null && Integer.parseInt(visitCount)>0)?"yes":"no";
	                            		}else{
	                            			objs[i] = GlobalsTool.replaceHTML((String)rss.getObject(i));
	                            		}
									}
	                            	ls.add(objs) ;
	                            	
	                            }
	                            rs.setRetVal(ls);
	                            rs.setRealTotal(ls.size());
	                            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    /**
     * 查询 最新图片新闻
     * @param loginId String
     * @return Result
     */
    public Result getPicFromNews(final String deskId,final String userId) {
        final Result result = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "select modulSql from tblMyDeskConfig where id = ?" ;
                            PreparedStatement pss = conn.prepareStatement(sql) ;
                            pss.setString(1, deskId) ;
                            ResultSet rss = pss.executeQuery() ;
                            if(rss.next()){
                            	sql = rss.getString("modulSql") ;
                            	int numOfParam = 0;
                        		if(sql.contains("@userId")){
                        			numOfParam = sql.split("@userId").length-1;
                        		}
                                String newsql = "select top 5"  + sql.substring(6);
                                newsql = newsql.replace("@userId", "?"); //把语句中的@userId改为?
                            	pss = conn.prepareStatement(newsql) ;
                                for (int i = 1; i <= numOfParam; i++) {
    								pss.setObject(i, userId);
    							}
                            	rss = pss.executeQuery() ;
                            	List<HashMap<String, String>> newsList = new ArrayList<HashMap<String,String>>() ;
                            	while(rss.next()){
                            		HashMap<String, String> newsMap = new HashMap<String, String>() ;
                            		newsMap.put("url", rss.getString(1)) ;
                            		newsMap.put("title",rss.getString(2)) ;
                            		newsMap.put("time", rss.getString(3)) ;
                            		newsMap.put("createBy", rss.getString(4)) ;
                            		String picture = rss.getString("picFiles") ;
                            		if(picture!=null && picture.indexOf(";")!=-1){
                            			picture = picture.substring(0, picture.indexOf(";")) ;
                            		}
                            		newsMap.put("pic", picture) ;
                            		String visitCount = rss.getString(6);
                            		newsMap.put("read", (visitCount!=null && Integer.parseInt(visitCount)>0)?"yes":"no");
                            		newsList.add(newsMap) ;
                            	}
                            	result.setRetVal(newsList) ;
                            }
                        } catch (SQLException ex) {
                        	result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                            return;
                        }
                    }
                });
                return result.getRetCode();
            }
        });
        result.retCode = retCode;
        return result;
    }
    
    /**
     * 查询最新未读通知,未读消息，未读邮件，未读待审
     * @param loginId String
     * @return Result
     */
    public Result queryNoReadMsg(final String userId) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
						try {
							String returnValue = "";
							/* 未读邮件 */
							String sql = "select count(*) ascount from OAMailInfo a "
									+ "left join MailInfoSetting b on a.userId = b.createBy "
									+ "where a.toUserId=? and a.state=0 and a.groupId='1' and b.statusId = 1" ;
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, userId);
							ResultSet rss = ps.executeQuery();
							if (rss.next()) {
								returnValue += rss.getInt("ascount") + ":";
							}
							/* 未读消息 */
							sql = "select count(id) ascount from OAMessage where receive = ? and status = 'noRead'";
							ps = conn.prepareStatement(sql);
							ps.setString(1, userId);
							rss = ps.executeQuery();
							if (rss.next()) {
								returnValue += rss.getInt("ascount") + ":";
							}
							/* 未读通知 */
							sql = "select type,count(id) ascount from tblAdvice where receive = ? and  status = 'noRead' group by type";
							ps = conn.prepareStatement(sql);
							ps.setString(1, userId);
							rss = ps.executeQuery();
							int notApprove = 0;
							int Other = 0;

							while (rss.next()) {
								String type = rss.getString("type");
								int count = rss.getInt("ascount");
								if ("notApprove".equals(type)) {
									notApprove += count;
								} else {
									Other += count;
								}
							}
							returnValue += notApprove + ":" + Other;
							rs.setRetVal(returnValue) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    /**
     * 查询倒计时
     * @return
     */
    public Result getCountDown() {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select top 1 CountdownType,CountdownTitle,CountdownDate from CRMCountdownDate" ;
							
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet prs = pss.executeQuery();
							if(prs.next()){
								result.retVal = new String[]{prs.getString("CountdownType"),prs.getString("CountdownTitle"),prs.getString("CountdownDate")};
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt getCountDown:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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

    /**
	 * 查询我的关注
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result getAttention(final String userId,final String title) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblattention where empId=? " ;
							if(title != null && !"".equals(title)){
								sql += " and title like '%"+title+"%'";
							}
							sql+=" ORDER BY lastUpdateTime DESC ";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId);
							ResultSet prs = pss.executeQuery();
							ArrayList<String[]> list = new ArrayList();
							while(prs.next()){
								String label = prs.getString("title") ;
								if(label.indexOf(":")>0){
									label = label.substring(0,label.indexOf(":")) ;
								}
								list.add(new String[]{prs.getString("id"),prs.getString("title"),prs.getString("url"),prs.getString("lastUpdateTime"),label});
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt getAttention:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	
	/**
	 * 查询光荣榜所有详情信息
	 * @author fjj
	 */
	public Result queryAllfameTop(final Integer status) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select tblfameTop.id,tblfameTop.caption,tblfameTop.EmpFullName,tblfameTop.photo,tblfameTop.EmployeeID,tblfameTop.title,tblfameTop.statusId,tblfameTop.stopDate,tblfameTop.createBy,tblfameTop.createTime,tblfameTop.eval,tblfameTopWish.appcount," +
									"tblfameTopWish2.appcount2,tblfameTopWish3.appcount3 from (select ftd.id,ftd.caption,tEmp.EmpFullName,tEmp.photo,ftd.EmployeeID,tTop.title,tTop.statusId,tTop.stopDate,tTop.createBy,tTop.createTime,ftd.eval from tblfameTop tTop,tblfameTopDet ftd," +
									"tblEmployee tEmp where ftd.EmployeeID=tEmp.id and tTop.statusId=? and tTop.id=ftd.f_ref) as tblfameTop " +
									"left join (select count(tblfameTopWish.galaType) as appcount,topid,galaType from tblfameTopWish  where tblfameTopWish.galaType=1 group by topid,galaType) as tblfameTopWish on tblfameTop.id=tblfameTopWish.topid " +
									"left join (select count(tblfameTopWish.galaType) as appcount2,topid,galaType from tblfameTopWish  where tblfameTopWish.galaType=2  group by topid,galaType) as tblfameTopWish2 on tblfameTop.id=tblfameTopWish2.topid " +
									"left join (select count(tblfameTopWish.galaType) as appcount3,topid,galaType from tblfameTopWish  where tblfameTopWish.galaType=3 group by topid,galaType) as tblfameTopWish3 on tblfameTop.id=tblfameTopWish3.topid " ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setInt(1, status);
							ResultSet rss=pss.executeQuery();
							ArrayList<String[]> listPlan = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] paln = new String[13] ;
								paln[0] = rss.getString("id");
								paln[1] = rss.getString("caption") ;
								paln[2] = rss.getString("EmpFullName") ;
								String icon = "";
								if(null!=rss.getString("Photo")&&!"".equals(rss.getString("Photo"))){
									if(rss.getString("Photo").indexOf(":") > 0){
										icon = rss.getString("Photo").substring(0,rss.getString("Photo").indexOf(":"));
									}else{
										icon = rss.getString("Photo");
									}									
									//icon = icon;
								}
								paln[3] = icon;
								paln[4] = rss.getString("EmployeeID") ;
								paln[5] = rss.getString("caption") ;
								paln[6] = rss.getString("stopDate") ;
								paln[7] = rss.getString("createBy") ;
								paln[8] = rss.getString("createTime") ;
								paln[9] = rss.getString("eval") ;
								/*鼓励*/
								paln[10] = rss.getString("appcount");
								/*鲜花*/
								paln[11] = rss.getString("appcount2");
								/*挑战*/
								paln[12] = rss.getString("appcount3");
								listPlan.add(paln);
							}
							result.setRetVal(listPlan) ;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt queryAllfameTop:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 根据TopID查询哪些用户添加祝贺，时间，类型
	 * 
	 */	
	public Result queryAllfameTopWish() {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select tblfameTopWish.topId,tblfameTopWish.EmployeeID,tblEmployee.EmpFullName,tblfameTopWish.galaType,tblfameTopWish.createTime from tblfameTopWish,tblEmployee where tblEmployee.id=tblfameTopWish.EmployeeID" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rss=pss.executeQuery();
							ArrayList<String[]> listPlan = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] paln = new String[5] ;
								paln[0] = rss.getString("topId");
								paln[1] = rss.getString("EmployeeID") ;
								paln[2] = rss.getString("EmpFullName") ;
								paln[3] = rss.getString("galaType") ;
								paln[4] = rss.getString("createTime") ;
								listPlan.add(paln);
							}
							result.setRetVal(listPlan) ;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt queryAllfameTopWish:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 查询目标
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result queryGoalMS2Line(final String deptCode,final String userId,
			final String goalClass,final String year,final String goalType,final String locale) {
		String sql = " select a.statusId,a.dataType,a.dataSql,a.cNum,a.id,b.zh_CN from tblGoalType a join tbllanguage b on a.name=b.id where a.id = ?  ";
		ArrayList param = new ArrayList();
		if("2".equals(goalType)){//回款目标
			param.add("huikuanmb");
		}else{//合同目标
			param.add("hetongmb");
		}
		Result rs = this.sqlList(sql, param);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS || rs.retVal == null){
			return rs;
		}
		Object[] os = ((ArrayList<Object[]>)rs.retVal).get(0);
		final String statusId = os[0].toString();
		final String dataType = os[1].toString();
		final String dataSql = os[2].toString();
		final String cNum = os[3].toString();
		final String typeId = os[4].toString();
		final String typeName=os[5].toString();
		if(statusId.equals("1")){
			rs.retVal = "stop";
			return rs;
		}
		
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							ArrayList<String> categories = new ArrayList<String>() ;
							HashMap<String,ArrayList<Double>> goalMap = new HashMap();
							
							//查询目标级别启用情况
							String sql ="select id,name from tblGoalGrade where statusId = 0";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rset = pss.executeQuery();
							ArrayList<String> gradeList = new ArrayList();
							HashMap<String,String> goalName = new HashMap();
							String gradeStr = "";
							while(rset.next()){
								gradeList.add(rset.getString(1));
								gradeStr +="b."+rset.getString(1)+",";
								goalName.put(rset.getString(1), rset.getString(2));
							}
							if(gradeStr.length() == 0){
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								result.retVal = "gradeStop";
								return ;
							}else{
								gradeStr = gradeStr.substring(0,gradeStr.length() -1);
							}
							
							goalName.put("complete", "com.desk.goal2");
							
							
							sql = " select b.month,"+gradeStr+" from tblEmployeeGoal a " +
								"join tblEmployeeGoalDet b on a.id = b.f_ref where a.goalType=? and year=? and EmployeeId=? order by b.month ";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, cNum) ;
							pss.setString(2, year) ;
							pss.setString(3, userId) ;
							rset = pss.executeQuery();
							while(rset.next()){
								categories.add(rset.getString(1));
								for(String goal:gradeList){
									ArrayList<Double> list =  goalMap.get(goal);
									if(list == null){
										list = new ArrayList<Double>();
										goalMap.put(goal, list);
									}
									list.add(rset.getDouble(goal));
								}
							}
							HashMap<Integer,Double> compMap = new HashMap();
							//计算完成数。
							if(dataType.equals("0")){
								//从业绩录入表中取数据								
								sql = " select sum(score),substring(BillDate,6,2) from tblEmployeeScore a join tblEmployeeScoreDet b on a.id = b.f_ref where  a.workFlowNodeName='finish' and  a.goalType=? and b.employeeId=? and BillDate like ?+'%' group by substring(BillDate,6,2) ";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, cNum) ;
								pss.setString(2, userId) ;
								pss.setString(3, year) ;
								rset = pss.executeQuery();
								while(rset.next()){
									compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
								}
							}else if(dataType.equals("1")){
								//工作计划录入
								sql = " select sum(score),substring(BillDate,6,2) from tblPlanGoalItem where goalType=? and employeeId=? and BillDate like ?+'%'  group by substring(BillDate,6,2)  ";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, typeId) ;
								pss.setString(2, userId) ;
								pss.setString(3, year) ;
								rset = pss.executeQuery();
								while(rset.next()){
									compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
								}
							}else if(dataType.equals("3")){
								//ERP收款单
								if("huikuanmb".equals(typeId)){
									//从ERP收款单中取
									sql = "select sum(fi),m from(" +
										"select sum(FactIncome) fi,substring(BillDate,6,2) m from tblSaleReceive where workFlowNodeName='finish' and EmployeeID=? and BillDate like ?+'%' and AcceptTypeID in ('PreReceive','Receive','OtherReceive')  group by substring(BillDate,6,2) " +
										" union select sum(-FactIncome) fi,substring(BillDate,6,2) m from tblSaleReceive where workFlowNodeName='finish' and  EmployeeID=? and BillDate like ?+'%' and AcceptTypeID in ('RetrunReceive','ReturnPay')   group by substring(BillDate,6,2) ) a group by m";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year) ;
									pss.setString(3, userId) ;
									pss.setString(4, year) ;
									rset = pss.executeQuery();
									while(rset.next()){
										compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
									}
								}else if("hetongmb".equals(typeId)){
									//从ERP订单中取
									sql = " select sum(TotalTaxAmount),substring(BillDate,6,2) from tblSalesOrder where workFlowNodeName='finish' and employeeId=? and BillDate like ?+'%'  group by substring(BillDate,6,2)  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year) ;
									rset = pss.executeQuery();
									while(rset.next()){
										compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
									}
								}
							}else if(dataType.equals("4")){
								//CRM收款单
								if("huikuanmb".equals(typeId)){
									//从CRM收款单中取
									sql = " select sum(ExeBalAmt),substring(BillDate,6,2) from CRMSaleReceive where  workFlowNodeName='finish' and  employeeId=? and BillDate like ?+'%'  group by substring(BillDate,6,2)  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year) ;
									rset = pss.executeQuery();
									while(rset.next()){
										compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
									}
								}else if("hetongmb".equals(typeId)){
									//从CRM订单中取
									sql = " select sum(TotalAmount),substring(SignUpDate,6,2) from CRMSaleContract where  workFlowNodeName='finish' and  employeeId=? and SignUpDate like ?+'%'  group by substring(SignUpDate,6,2)  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year) ;
									rset = pss.executeQuery();
									while(rset.next()){
										compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
									}
								}
							}else if(dataType.equals("2")){
								//,合同目标和回款目标不支持其它
								
							}
							
							
							TreeMap<String, ArrayList<Double>> values = new TreeMap<String, ArrayList<Double>>() ;
							for(String goal:gradeList){
								values.put(goalName.get(goal), goalMap.get(goal));
							}
							ArrayList<Double> comp = new ArrayList();
							for(String c:categories){
								if(compMap.get(Integer.parseInt(c))==null){
									comp.add(0D);
								}else{
									comp.add(compMap.get(Integer.parseInt(c)));
								}
							}
							values.put(goalName.get("complete"), comp);
							if(categories.size()>0){
								//暂时来说，CRM桌面的柱状图和折线图都是同一数据来源，因此没必要执行2次，故而在这里一次性反回查询结果
								result.setRetVal(GlobalsTool.getMultiMS2Line(categories, values,locale)+"::::"+GlobalsTool.getMultiBarChart(categories, values,locale)) ;
							}else{
								result.setRetVal("no") ;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt queryGoal:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	
	/**
	 * 查询我的月目标
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result queryMyMonthGoal(final String goalType,final String userId,
			final String goalClass,final String month,final int year) {
		String sql = " select statusId,dataType,dataSql,cNum,id from tblGoalType where id = ?  ";
		ArrayList param = new ArrayList();
		if("2".equals(goalType)){//回款目标
			param.add("huikuanmb");
		}else{//合同目标
			param.add("hetongmb");
		}
		Result rs = this.sqlList(sql, param);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS || rs.retVal == null){
			return rs;
		}
		Object[] os = ((ArrayList<Object[]>)rs.retVal).get(0);
		final String statusId = os[0].toString();
		final String dataType = os[1].toString();
		final String dataSql = os[2].toString();
		final String cNum = os[3].toString();
		final String typeId = os[4].toString();
		if(statusId.equals("1")){
			rs.retVal = "stop";
			return rs;
		}
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//查询目标级别启用情况
							String sql ="select id from tblGoalGrade where statusId = 0";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rset = pss.executeQuery();
							ArrayList<String> gradeList = new ArrayList();
							String gradeStr = "";
							while(rset.next()){
								gradeList.add(rset.getString(1));
								gradeStr +="b."+rset.getString(1)+",";
							}
							if(gradeStr.length() == 0){
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								result.retVal = "gradeStop";
								return;
							}else{
								gradeStr = gradeStr.substring(0,gradeStr.length() -1);
							}
							
							int[] goal = new int[gradeList.size()+1] ;//记录反回结果
							
							//查询目标
							sql = " select "+gradeStr+" from tblEmployeeGoal a " +
									"join tblEmployeeGoalDet b on a.id = b.f_ref where a.goalType=? and year=? and EmployeeId=? and b.month=? ";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, cNum) ;
							pss.setString(2, year+"") ;
							pss.setString(3, userId) ;
							pss.setString(4, month) ;
							rset = pss.executeQuery();
							if(rset.next()){								
								goal[0] = 0 ;
								for(int i = 0;i<gradeList.size();i++){
									String gs = gradeList.get(i);
									goal[i+1] = (int)rset.getDouble(gs);
								}
								result.setRetVal(goal) ;								
							}else{
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							}
							
							//计算完成数。
							if(dataType.equals("0")){
								//从业绩录入表中取数据								
								sql = " select sum(score) from tblEmployeeScore a join tblEmployeeScoreDet b on a.id = b.f_ref where  a.workFlowNodeName='finish' and  a.goalType=? and b.employeeId=? and BillDate like ?+'%'  ";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, cNum) ;
								pss.setString(2, userId) ;
								pss.setString(3, year+"-"+(month.length()==1?"0"+month:month)) ;
								rset = pss.executeQuery();
								if(rset.next()){
									goal[0] = (int)rset.getDouble(1);
								}
							}else if(dataType.equals("1")){
								//工作计划录入
								sql = " select sum(score) from tblPlanGoalItem where goalType=? and employeeId=? and BillDate like ?+'%'  ";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, typeId) ;
								pss.setString(2, userId) ;
								pss.setString(3, year+"-"+(month.length()==1?"0"+month:month)) ;
								rset = pss.executeQuery();
								if(rset.next()){
									goal[0] = (int)rset.getDouble(1);
								}
							}else if(dataType.equals("3")){
								//ERP收款单
								if("huikuanmb".equals(typeId)){
									//从ERP收款单中取
									sql = "select sum(fi) from(" +
										"select sum(FactIncome) fi from tblSaleReceive where workFlowNodeName='finish' and  EmployeeID=? and BillDate like ?+'%' and AcceptTypeID in ('PreReceive','Receive','OtherReceive')" +
										" union select sum(-FactIncome) fi from tblSaleReceive where workFlowNodeName='finish' and  EmployeeID=? and BillDate like ?+'%' and AcceptTypeID in ('RetrunReceive','ReturnPay')) a";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year+"-"+(month.length()==1?"0"+month:month)) ;
									pss.setString(3, userId) ;
									pss.setString(4, year+"-"+(month.length()==1?"0"+month:month)) ;
									rset = pss.executeQuery();
									if(rset.next()){
										goal[0] = (int)rset.getDouble(1);
									}
								}else if("hetongmb".equals(typeId)){
									//从ERP订单中取
									sql = " select sum(TotalTaxAmount) from tblSalesOrder where workFlowNodeName='finish' and  employeeId=? and BillDate like ?+'%'  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year+"-"+(month.length()==1?"0"+month:month)) ;
									rset = pss.executeQuery();
									if(rset.next()){
										goal[0] = (int)rset.getDouble(1);
									}
								}
							}else if(dataType.equals("4")){
								//CRM收款单
								if("huikuanmb".equals(typeId)){
									//从ERP收款单中取
									sql = " select sum(ExeBalAmt) from CRMSaleReceive where workFlowNodeName='finish' and  employeeId=? and BillDate like ?+'%'  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year+"-"+(month.length()==1?"0"+month:month)) ;
									rset = pss.executeQuery();
									if(rset.next()){
										goal[0] = (int)rset.getDouble(1);
									}
								}else if("hetongmb".equals(typeId)){
									//从ERP订单中取
									sql = " select sum(TotalAmount) from CRMSaleContract where workFlowNodeName='finish' and  employeeId=? and SignUpDate like ?+'%'  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year+"-"+(month.length()==1?"0"+month:month)) ;
									rset = pss.executeQuery();
									if(rset.next()){
										goal[0] = (int)rset.getDouble(1);
									}
								}
							}else if(dataType.equals("2")){
								//,合同目标和回款目标不支持其它
								
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt queryMyMonthGoal:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	
	/**
	 * 查询我的年目标
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result queryMyYearGoal(final String userId,final String goalClass,
			final int year,final int month) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "" ;
							if("tblDepartmentGoal".equals(goalClass)){
								sql = "select ReceiveAmt,ReComplete,substring(workDate,6,2) asMonth from tblDayWorkPlan "  
									+ "where planType='month' and departmentCode='"+userId+"' and substring(workDate,1,7)=?" ;
							}else if("tblCompanyGoal".equals(goalClass)){
								sql = "select ReceiveAmt,ReComplete,substring(workDate,6,2) asMonth from tblDayWorkPlan "  
									+ "where planType='month'  and substring(workDate,1,7)=?" ;
							}else{
								sql = "select ReceiveAmt,ReComplete,substring(workDate,6,2) asMonth from tblDayWorkPlan "  
									+ "where planType='month' and createBy='"+userId+"' and substring(workDate,1,7)=?" ;
							}
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, year+"-"+month) ;
							ResultSet rss = pss.executeQuery() ;
							if(rss.next()){
								double[] goal = new double[4] ;
								goal[0] = rss.getDouble("complete") ;
								goal[1] = rss.getDouble("lowGoal") ;
								goal[2] = rss.getDouble("goalTotal") ;
								goal[3] = rss.getDouble("highGoal") ;
								result.setRetVal(goal) ;
							}else{
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt queryMyYearGoal:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	
	/**
	 * 查询目标
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Result queryGoal(final String deptCode,final String userId,final String goalClass,
			final String year,final String goalType,final String locale) {
		String sql = " select a.statusId,a.dataType,a.dataSql,a.cNum,a.id,b.zh_CN from tblGoalType a join tbllanguage b on a.name=b.id where a.id = ?  ";
		ArrayList param = new ArrayList();
		if("2".equals(goalType)){//回款目标
			param.add("huikuanmb");
		}else{//合同目标
			param.add("hetongmb");
		}
		Result rs = this.sqlList(sql, param);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS || rs.retVal == null){
			return rs;
		}
		Object[] os = ((ArrayList<Object[]>)rs.retVal).get(0);
		final String statusId = os[0].toString();
		final String dataType = os[1].toString();
		final String dataSql = os[2].toString();
		final String cNum = os[3].toString();
		final String typeId = os[4].toString();
		final String typeName=os[5].toString();
		if(statusId.equals("1")){
			rs.retVal = "stop";
			return rs;
		}
		
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							ArrayList<String> categories = new ArrayList<String>() ;
							HashMap<String,ArrayList<Double>> goalMap = new HashMap();
							
							//查询目标级别启用情况
							String sql ="select id,name from tblGoalGrade where statusId = 0";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rset = pss.executeQuery();
							ArrayList<String> gradeList = new ArrayList();
							HashMap<String,String> goalName = new HashMap();
							String gradeStr = "";
							while(rset.next()){
								gradeList.add(rset.getString(1));
								gradeStr +="b."+rset.getString(1)+",";
								goalName.put(rset.getString(1), rset.getString(2));
							}
							if(gradeStr.length() == 0){
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								result.retVal = "gradeStop";
								return ;
							}else{
								gradeStr = gradeStr.substring(0,gradeStr.length() -1);
							}
							
							goalName.put("complete", "com.desk.goal2");
							
							
							sql = " select b.month,"+gradeStr+" from tblEmployeeGoal a " +
								"join tblEmployeeGoalDet b on a.id = b.f_ref where a.goalType=? and year=? and EmployeeId=? order by b.month ";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, cNum) ;
							pss.setString(2, year) ;
							pss.setString(3, userId) ;
							rset = pss.executeQuery();
							while(rset.next()){
								categories.add(rset.getString(1));
								for(String goal:gradeList){
									ArrayList<Double> list =  goalMap.get(goal);
									if(list == null){
										list = new ArrayList<Double>();
										goalMap.put(goal, list);
									}
									list.add(rset.getDouble(goal));
								}
							}
							HashMap<Integer,Double> compMap = new HashMap();
							//计算完成数。
							if(dataType.equals("0")){
								//从业绩录入表中取数据								
								sql = " select sum(score),substring(BillDate,6,2) from tblEmployeeScore a join tblEmployeeScoreDet b on a.id = b.f_ref where a.goalType=? and b.employeeId=? and BillDate like ?+'%' group by substring(BillDate,6,2) ";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, cNum) ;
								pss.setString(2, userId) ;
								pss.setString(3, year) ;
								rset = pss.executeQuery();
								while(rset.next()){
									compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
								}
							}else if(dataType.equals("1")){
								//工作计划录入
								sql = " select sum(score),substring(BillDate,6,2) from tblPlanGoalItem where goalType=? and employeeId=? and BillDate like ?+'%'  group by substring(BillDate,6,2)  ";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, typeId) ;
								pss.setString(2, userId) ;
								pss.setString(3, year) ;
								rset = pss.executeQuery();
								while(rset.next()){
									compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
								}
							}else if(dataType.equals("3")){
								//ERP收款单
								if("huikuanmb".equals(typeId)){
									//从ERP收款单中取
									sql = "select sum(fi),m from(" +
										"select sum(FactIncome) fi,substring(BillDate,6,2) m from tblSaleReceive where EmployeeID=? and BillDate like ?+'%' and AcceptTypeID in ('PreReceive','Receive','OtherReceive')  group by substring(BillDate,6,2) " +
										" union select sum(-FactIncome) fi,substring(BillDate,6,2) m from tblSaleReceive where EmployeeID=? and BillDate like ?+'%' and AcceptTypeID in ('RetrunReceive','ReturnPay')   group by substring(BillDate,6,2) ) a group by m";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year) ;
									pss.setString(3, userId) ;
									pss.setString(4, year) ;
									rset = pss.executeQuery();
									while(rset.next()){
										compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
									}
								}else if("hetongmb".equals(typeId)){
									//从ERP订单中取
									sql = " select sum(TotalTaxAmount),substring(BillDate,6,2) from tblSalesOrder where employeeId=? and BillDate like ?+'%'  group by substring(BillDate,6,2)  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, cNum) ;
									pss.setString(2, userId) ;
									pss.setString(3, year) ;
									rset = pss.executeQuery();
									while(rset.next()){
										compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
									}
								}
							}else if(dataType.equals("4")){
								//CRM收款单
								if("huikuanmb".equals(typeId)){
									//从ERP收款单中取
									sql = " select sum(ExeBalAmt),substring(BillDate,6,2) from CRMSaleReceive where employeeId=? and BillDate like ?+'%'  group by substring(BillDate,6,2)  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year) ;
									rset = pss.executeQuery();
									while(rset.next()){
										compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
									}
								}else if("hetongmb".equals(typeId)){
									//从ERP订单中取
									sql = " select sum(TotalAmount),substring(BillDate,6,2) from CRMSaleContract where employeeId=? and BillDate like ?+'%'  group by substring(BillDate,6,2)  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, userId) ;
									pss.setString(2, year) ;
									rset = pss.executeQuery();
									while(rset.next()){
										compMap.put(Integer.parseInt(rset.getString(2)), rset.getDouble(1));
									}
								}
							}else if(dataType.equals("2")){
								//,合同目标和回款目标不支持其它
								
							}
							
							
							TreeMap<String, ArrayList<Double>> values = new TreeMap<String, ArrayList<Double>>() ;
							for(String goal:gradeList){
								values.put(goalName.get(goal), goalMap.get(goal));
							}
							ArrayList<Double> comp = new ArrayList();
							for(String c:categories){
								if(compMap.get(Integer.parseInt(c))==null){
									comp.add(0D);
								}else{
									comp.add(compMap.get(Integer.parseInt(c)));
								}
							}
							values.put(goalName.get("complete"), comp);
							if(categories.size()>0){
								result.setRetVal(GlobalsTool.getMultiBarChart(categories, values,locale)) ;
							}else{
								result.setRetVal("no") ;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt queryGoal:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	
	/**
	 * 添加祝贺
	 * @param userid	明细表的ID
	 * @param typeid	祝贺类型
	 * @param createby	光荣榜创建者
	 * @param Employeeid	当前登录用户
	 * 
	 */
	public Result addfameTopWish(final String topId,final String typeid,final String Employeeid,final String userId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "insert into tblfameTopWish(id,topId,createtime,galaType,EmployeeID,createBy,wishTime) values(?,?,?,?,?,?,?)" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, IDGenerater.getId()) ;
							pss.setString(2, topId) ;
							pss.setString(3, BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
							pss.setString(4, typeid) ;
							pss.setString(5, Employeeid) ;
							pss.setString(6, userId) ;
							pss.setString(7, BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
							pss.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt addfameTopWish:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 根据获称号人查询有多少人对他进行鼓掌鼓励
	 * @author fjj
	 */
	public Result queryApplause(final Integer typeid,final String Employeid){
		final Result result=new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select count(tblfameTopWish.galaType) as appcount from tblfameTopWish,tblfameTopDet where tblfameTopWish.galaType=? and tblfameTopDet.id=tblfameTopWish.topid and tblfameTopDet.id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setInt(1, typeid);
							pss.setString(2, Employeid);
							ResultSet rss=pss.executeQuery();
							int number = 0;
							while(rss.next()){
								number=rss.getInt("appcount");
							}
							result.setRetVal(number) ;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt queryApplause:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 查询评语
	 */
	
	public Result queryeval(final String Employeeid){
		final Result result=new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select eval from tblfameTopDet where id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, Employeeid);
							ResultSet rss=pss.executeQuery();
							String eval="";
							while(rss.next()){
								eval=rss.getString("eval");
							}
							result.setRetVal(eval) ;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt queryeval:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
}
