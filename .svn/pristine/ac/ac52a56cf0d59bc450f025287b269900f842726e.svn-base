package com.koron.oa.thdesktop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import sun.security.jca.GetInstance;

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
public class THDesktopMgt extends AIODBManager {
	 
    // 注意，如果要自定义sql语句，当以@userId结尾时，sql语句后需加一个空格
    public Result queryDesk(final String deskId, final int row, final String userId) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "select modulSql,modulName from OADeskConfig where id = ?" ;
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
	                    		newsql = "select top " + row + sql.substring(7);	                    				                            
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
    /*查询排序*/
    public String queryOrder(String userId,String pot){
    	StringBuffer msg = new StringBuffer();   
    	String sql1  = "select modulId,modulSql,modulName from OADeskConfig where createBy = '"+userId+"' and dataPosition = '"+pot+"' order by dataOrder asc";
    	Result rs1 = sqlList(sql1, new ArrayList());
    	ArrayList rs1s = (ArrayList)rs1.retVal; 
    	StringBuffer remark = new StringBuffer();
    	if(rs1s.size()>0){
    		for (Object obj : rs1s) {
    			remark.append(GlobalsTool.get(obj,0).toString()+";");
    			msg.append(GlobalsTool.get(obj,0).toString()+";"+GlobalsTool.get(obj,1).toString()+";"+GlobalsTool.get(obj,2).toString()+"|");								
			}
    	}
    	
    	String sql2  = "select modulId,modulSql,modulName from OADeskConfig where createBy = '0' and dataPosition = '"+pot+"' order by dataOrder asc";
    	Result rs2 = sqlList(sql2, new ArrayList());
    	ArrayList rs2s = (ArrayList)rs2.retVal;     
    	if(rs2s.size()>0){
    		for (Object obj : rs2s) {
    			if(remark.indexOf(GlobalsTool.get(obj,0).toString()+";") ==-1){
    				msg.append(GlobalsTool.get(obj,0).toString()+";"+GlobalsTool.get(obj,1).toString()+";"+GlobalsTool.get(obj,2).toString()+"|");
    			}  											
			}
    	}
    	return msg.toString();
    }
    /*原始查询排序*/
    public Result queryOldOrder(String conditions){
    	List<String> param = new ArrayList<String>();    	
    	return sqlList(conditions, param);
    }
    /*修改配置*/
    public Result updateOrder(final String setting,final String userId) {
        final Result result = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {     
							String[] settings = setting.split(";");													
							/*先删除指定位置的所有排序*/
							String delsql = "delete from OADeskConfig where createby=? and dataPosition = ?";
							PreparedStatement delStmt = conn.prepareStatement(delsql);
							delStmt.setString(1,userId);
							delStmt.setString(2,settings[0]);
							delStmt.executeUpdate();
							
							/*再插入指定位置排序*/
							String sql = "insert OADeskConfig(id,dataPosition,dataOrder,createBy,modulSql,modulId,modulName)values(?,?,?,?,?,?,?)";
							PreparedStatement stmt = conn.prepareStatement(sql);
							int i=0;
							for (String str : settings) {
								if(!"".equals(str) && i>0){
									String[] strs = str.split(",");
									String qsql = "select modulSql,modulName from OADeskConfig where id='"+strs[1]+"' and createBy='0'";								
									PreparedStatement qtmt = conn.prepareStatement(qsql);
									ResultSet rssql = qtmt.executeQuery();
									String modulSql="";
									String modulName="";
									if(rssql.next()){
										modulSql = rssql.getString("modulSql");
										modulName = rssql.getString("modulName");
									}									
									stmt.setString(1,IDGenerater.getId());
									stmt.setString(2,settings[0]);
									stmt.setString(3,strs[0]);
									stmt.setString(4,userId);								
									stmt.setString(5,modulSql);
									stmt.setString(6,strs[1]);
									stmt.setString(7,modulName);
									stmt.executeUpdate();
								}
								i++;
							}
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        } catch (Exception ex) {
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
	/*邮件未读查询*/
	public Result selectOutByUser(final String userId) {
		List param = new ArrayList();
		String sql = "select f_ref from MailinfoSettingUser where userId=?";
		param.add(userId);
		Result rs = sqlList(sql, param);

		String ids = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			for (Object o : (List) rs.retVal) {
				Object[] os = (Object[]) o;
				ids += "'" + os[0] + "',";
			}
		}
		param.clear();
		String hql = "";
		if (ids.length() == 0) {
			hql = "select id from MailinfoSetting bean where bean.createby=? and bean.statusid = 1 and (bean.mainAccount = '' or bean.mainAccount is null) ";
		} else {
			hql = "select id from MailinfoSetting bean where bean.statusid = 1  and (bean.mainAccount = '' or bean.mainAccount is null)  and( bean.createby=?   or bean.id in ("
					+ ids.substring(0, ids.length() - 1) + "))";
		}
		param.add(userId);
		return sqlList(hql, param);
	}
	public Result NoreadMail(final String statId, final String userId) {
		Result rs = new Result();
		ArrayList list = new ArrayList();
		List param = new ArrayList();
		// 统计内部
		String sql = "select groupId,count(*) from OAMailInfo where state=0 and account = '' and userId = ? group by groupId ";
		param.add(userId);
		Result rss = sqlList(sql, param);
		if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<Object[]> ls = (List<Object[]>) rss.retVal;
			for (Object[] os : ls) {
				list.add(new Object[] { "", os[0], os[1] });
			}
		}
		param.clear();
		// 统计外部
		if ("all".equals(statId)) {
			sql = " select groupId,count(*) from OAMailInfo bean join mailinfosetting a on bean.account=a.id where bean.state=0 and a.statusid=1 and  ( a.createBy=? or a.id in (select f_ref from MailinfoSettingUser b where b.userId=? )) group by groupId ";
			param.add(userId);
			param.add(userId);
			rss = sqlList(sql, param);
			if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				List<Object[]> ls = (List<Object[]>) rss.retVal;
				for (Object[] os : ls) {
					list.add(new Object[] { "all", os[0], os[1] });
				}
			}
		} else {
			String[] stats = statId.split(":");
			for (String stat : stats) {
				if (stat.length() > 0) {
					param.clear();
					sql = "select groupId,count(*) from OAMailInfo where state=0 and account = ? group by groupId ";
					param.add(stat);
					rss = sqlList(sql, param);
					if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						List<Object[]> ls = (List<Object[]>) rss.retVal;
						for (Object[] os : ls) {
							list.add(new Object[] { stat, os[0], os[1] });
						}
					}
				}
			}
		}
		rs.retVal = list;
		return rs;
	}
	
	//end
}
