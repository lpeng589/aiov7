package com.menyi.aio.web.service;

import java.util.*;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.ConnectionEnv;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.DBUtil;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;

import org.hibernate.Session;
import java.sql.PreparedStatement;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import java.text.SimpleDateFormat;
import com.menyi.web.util.SecurityLock;
import com.menyi.web.util.BaseEnv;

/**
 * <p>Title: </p>
 *
 * <p>Description: 单位管理的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */ 
public class ServiceMgt extends DBManager {
	
	/**
	 * 记录客服日志
	 * @param workNo
	 * @param name
	 * @param company
	 * @param type 0:sql,1更新文件，2，恢复文件,3:删除文件，4，增加文件,5,创建文件夹
	 * @param content
	 * @return
	 */
    public Result addLog(final String workNo,final String name,final String company,final int type,final String content,final String fileName,final String time) {
        final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        
                        try{	
	                        //判断是否是查询语句
                        	String sql = "insert into tblServiceLog(workNo,name,company,type,content,fileName,createTime) values(?,?,?,?,?,?,?)";
	                                                
                        	PreparedStatement psmt = conn.prepareStatement(sql);
                        	psmt.setString(1, workNo);
                        	psmt.setString(2, name);
                        	psmt.setString(3, company);
                        	psmt.setInt(4, type);
                        	psmt.setString(5, content);
                        	psmt.setString(6, fileName);
                        	psmt.setString(7, time);
	                        psmt.executeUpdate();
                        }catch(Exception e){
                        	res.retCode = ErrorCanst.DEFAULT_FAILURE;
                        	res.retVal = "执行失败"+e.getMessage();
                        	BaseEnv.log.error("ServiceMgt.addLog ",e);
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }  
    
	/**
	 * 记录客服日志
	 * @param workNo
	 * @param name
	 * @param company
	 * @param type 0:sql,1更新文件，2，恢复文件,3:删除文件，4，增加文件,5,创建文件夹
	 * @param content
	 * @return
	 */
    public Result logList(final String workNo,final String name,final String company,final int type,final String content,final String fileName,final String startTime,final String endTime) {
        final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        
                        try{	
	                        //判断是否是查询语句
                        	String sql = "select id,workNo,name,company,type,createTime,content,fileName from  tblServiceLog where 1=1 ";
                        	if(workNo != null && workNo.length() > 0){
                        		sql += " and workNo='"+workNo+"' ";
                        	}
                        	if(name != null && name.length() > 0){
                        		sql += " and name like '%"+name+"%' ";
                        	}
                        	if(company != null && company.length() > 0){
                        		sql += " and company like '%"+company+"%' ";
                        	}
                        	if(content != null && content.length() > 0){
                        		sql += " and content like '%"+content+"%' ";
                        	}
                        	if(fileName != null && fileName.length() > 0){
                        		sql += " and fileName = '"+fileName+"' ";
                        	}
                        	if(type  != -1){
                        		sql += " and type = "+type+" ";
                        	}
                        	if(startTime != null && startTime.length() > 0){
                        		sql += " and createTime >='"+startTime+"' ";
                        	}
                        	if(endTime != null && endTime.length() > 0){
                        		sql += " and createTime <='"+endTime+"' ";
                        	}
	                        sql += " order by createTime desc ";                        
                        	PreparedStatement psmt = conn.prepareStatement(sql);
                        	System.out.println(sql);
	                        ResultSet rset =psmt.executeQuery();
	                        ArrayList list = new ArrayList();
	                        while(rset.next()){
	                        	String[] r = new String[8];
	                        	r[0] = rset.getString(1);
	                        	r[1] = rset.getString(2);
	                        	r[2] = rset.getString(3);
	                        	r[3] = rset.getString(4);
	                        	r[4] = rset.getString(5);
	                        	r[5] = rset.getString(6);
	                        	r[6] = rset.getString(7);
	                        	r[7] = rset.getString(8);
	                        	list.add(r);
	                        }
	                        res.setRetVal(list);
                        }catch(Exception e){
                        	res.retCode = ErrorCanst.DEFAULT_FAILURE;
                        	res.retVal = "执行失败"+e.getMessage();
                        	BaseEnv.log.error("ServiceMgt.addLog ",e);
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    } 

    public Result exec(final String mysql) {
        final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        
                        try{	
	                        //判断是否是查询语句
                        	String sql = mysql.trim();
	                        boolean isSelect=sql.toLowerCase().startsWith("select ");
	                        if(isSelect){
	                        	sql = sql.substring(6).trim();
	                        	if(!sql.toLowerCase().startsWith("top ")){
	                        		sql = "select top 1001 "+sql;
	                        	}else{
	                        		sql = "select "+ sql;
	                        	}
	                        }
	                        
                        	PreparedStatement psmt = conn.prepareStatement(sql);

	                        
	                        if(!isSelect){
	                        	if(sql.toLowerCase().indexOf("tblservicelog") > 0){
	                        		
	                        		res.retCode = ErrorCanst.DEFAULT_FAILURE;
	                            	res.retVal = "不允许操作关键表tblServiceLog";
	                            	return;
	                        	}
	                        	int r =psmt.executeUpdate();
	                        	res.retVal = "执行成功，影响行数"+r;
	                        	
	                        	
	                        }else{
		                        ResultSet rst = psmt.executeQuery();
		                        ArrayList list = new ArrayList();
		                        res.retVal = list;
		                        ArrayList title = new ArrayList();								
								for (int i = 1; i <= rst.getMetaData().getColumnCount(); i++) {
									title.add(rst.getMetaData().getColumnName(i));
								}
								list.add(title);
		                        while (rst.next()) {
		                        	ArrayList data = new ArrayList();	
									for (int i = 1; i <= rst.getMetaData().getColumnCount(); i++) {
										if(rst.getMetaData().getColumnType(i)==java.sql.Types.NUMERIC){
											data.add(""+rst.getDouble(i));
										}else{										
											data.add(rst.getString(i));
										}
									}
									list.add(data);
								}
		                        res.retVal = list;
	                        }
                        }catch(Exception e){
                        	res.retCode = ErrorCanst.DEFAULT_FAILURE;
                        	res.retVal = "执行失败"+e.getMessage();
                        	BaseEnv.log.error("ServiceMgt.exec ",e);
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }    
}
