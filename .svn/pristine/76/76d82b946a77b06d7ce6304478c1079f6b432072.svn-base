package com.menyi.aio.web.systemSafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.bean.ReportsDetBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConnectionEnv;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.ReportField;
import com.menyi.web.util.SystemState;

/**
 * <p>Title: </p>
 *
 * <p>Description: 报表设置的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class SystemSafeMgt extends AIODBManager {
	
	/**
	 * 修改安全值
	 * @param name
	 * @param value
	 * @return
	 */
	public Result updateSafeValue(final ArrayList<String[]> list) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = " delete tblSystemSafe where name not like 'lastE%'";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.execute();
							for(String[] ss: list){
								sql = " insert into tblSystemSafe(name,value) values(?,?) ";
								ps = conn.prepareStatement(sql);
								ps.setString(1, ss[0]);
								ps.setString(2, ss[1]);
								ps.execute();
							}							
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
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
	
	public Result updateSafeValue(final String lastEName,final String lastEBak) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = " delete tblSystemSafe where name = '"+lastEName+"'";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.execute();
							
							sql = " insert into tblSystemSafe(name,value) values(?,?) ";
							ps = conn.prepareStatement(sql);
							ps.setString(1, lastEName);
							ps.setString(2, lastEBak);
							ps.execute();
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
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
	public Result querySafeValues() {
		
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	PreparedStatement pstmt ;
                        ResultSet rs;
                        String sqlStr="";
                        try {  
                        	sqlStr = "select name,value from tblSystemSafe ";
                           
                            pstmt = conn.prepareStatement(sqlStr);
                            HashMap map=new HashMap();
                            rs = pstmt.executeQuery();
                            while (rs.next()) {
                            	map.put(rs.getString(1), rs.getString(2));
                            }
                            rst.setRetVal(map);
                        } catch (Exception ex) {
                            BaseEnv.log.error(sqlStr,ex);
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
