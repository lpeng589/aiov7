package com.koron.oa.holidaySetting;

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

public class HolidaySettingMgt extends DBManager{
	
	public Result query(){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select id,year(holiday) as yy,month(holiday) as MM,day(holiday) as dd from tblHolidaySetting ";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	List list = new ArrayList();
                        	while (rst.next()) {
								String[] objs = new String[4];
								objs[0]=rst.getString("id");
								objs[1]=rst.getString("yy");
								objs[2]=rst.getString("MM");
								objs[3]=rst.getString("dd");
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
                        	String sql="select count(*) as count from tblHolidaySetting where holiday='"+date+"'";
                        	Statement st = conn.createStatement();
                    		ResultSet rss = st.executeQuery(sql);
                    		if (rss.next()) {
								int count = Integer.parseInt(rss.getString(1));
								rs.setRetCode(count);
								return ;
							}
                        } catch (Exception ex) {
                        	rs.setRetCode(0);
							System.out.println("HolidaySettingMgt.hasSetting() has wrong");
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
	
	public Result add(final List list){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="insert into tblHolidaySetting(id,holiday,createBy,createTime) values(lower(substring(replace(newid(),'-',''),1,28)),?,?,convert(varchar(10),getdate(),120)+' '+convert(varchar(8),getdate(),114))";
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
                        	String sql="update tblHolidaySetting set lastUpdateBy=?,lastUpdateTime=convert(varchar(10),getdate(),120)+' '+convert(varchar(8),getdate(),114) where holiday=?";
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
                        	String sql="delete from tblHolidaySetting where holiday=?";
                        	PreparedStatement ps = conn.prepareStatement(sql) ;
                        	for(Object obj:list){
                        		String[] strs = (String[])obj;
                        		ps.setString(1, strs[0]);
                        		int a = ps.executeUpdate();
                        		a++;
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
