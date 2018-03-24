package com.menyi.aio.web.vip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.web.util.ErrorCanst;

public class VIPMgt extends DBManager{
	
	public Result query(){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select v.id,year(date) as yy,month(date) as MM,day(date) as dd,settingId,ruleName from tblVipPointRule v,tblIntegralSeting s where v.settingId=s.id";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	List list = new ArrayList();
                        	while (rst.next()) {
								String[] objs = new String[6];
								objs[0]=rst.getString("id");
								objs[1]=rst.getString("yy");
								objs[2]=rst.getString("MM");
								objs[3]=rst.getString("dd");
								objs[4]=rst.getString("settingId");
								objs[5]=rst.getString("ruleName");
								list.add(objs);
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(list) ;
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
	
	public Result hasSetting(final String date){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select count(*) as count from tblVipPointRule where date='"+date+"'";
                        	Statement st = conn.createStatement();
                    		ResultSet rss = st.executeQuery(sql);
                    		if (rss.next()) {
								int count = Integer.parseInt(rss.getString(1));
								rs.setRetCode(count);
								return ;
							}
                        } catch (Exception ex) {
                        	rs.setRetCode(0);
							System.out.println("VIPMgt.hasSetting() has wrong");
							return ;
						}
                    }
                });
                return rs.getRetCode();
            }
        });
		rs.setRetCode(retCode);
        return rs;
	}
	
	public Result querySetting(){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select id,ruleName from tblIntegralSeting";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	List list = new ArrayList();
                        	while (rst.next()) {
								String[] objs = new String[2];
								objs[0]=rst.getString("id");
								objs[1]=rst.getString("ruleName");
								list.add(objs);
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(list) ;
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
	
	public Result add(final List list){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="insert into tblVipPointRule(date,settingId) values(?,?)";
                        	PreparedStatement ps = conn.prepareStatement(sql) ;
                        	for(Object obj:list){
                        		String[] strs = (String[])obj;
                        		ps.setString(1, strs[0]);
                        		ps.setString(2, strs[1]);
                        		ps.executeUpdate();
                        	}
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
	
	public Result update(final List list){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="update tblVipPointRule set settingId=? where date=?";
                        	PreparedStatement ps = conn.prepareStatement(sql) ;
                        	for(Object obj:list){
                        		String[] strs = (String[])obj;
                        		ps.setString(1, strs[1]);
                        		ps.setString(2, strs[0]);
                        		ps.executeUpdate();
                        	}
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
	
	public Result del(final List list){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="delete from tblVipPointRule where date=?";
                        	PreparedStatement ps = conn.prepareStatement(sql) ;
                        	for(Object obj:list){
                        		String[] strs = (String[])obj;
                        		ps.setString(1, strs[0]);
                        		ps.executeUpdate();
                        	}
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
