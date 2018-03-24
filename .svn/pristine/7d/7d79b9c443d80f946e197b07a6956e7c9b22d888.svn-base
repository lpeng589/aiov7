package com.koron.oa.oaReadingRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAReadingRecord;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
/**
 * <p>
 * Title:阅读痕迹.DAO实现类
 * </p>
 * 
 * <p>
 * Description:用于保存用户阅读内容记录
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * 
 * <p>
 * Company: 科荣软件
 * </p>
 * 
 * @author 汤晶
 * @version 4.0
 */
public class OAReadingRecordMgt extends DBManager {
	public OAReadingRecordMgt() {
	}
	 //添加阅读记录
    public Result addOAReadingRecord(final OAReadingRecord oaReadingRecord) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            final PreparedStatement pstmt = conn.
                                    prepareStatement("insert into OAReadingRecord (id,readingInfoTable,readingInfoId,readingTime,reader,visitMachine,ip,visitCount) values(?,?,?,?,?,?,?,?)");
                            pstmt.setString(1, oaReadingRecord.getId());
                            pstmt.setString(2, oaReadingRecord.getReadingInfoTable());
                            pstmt.setString(3, oaReadingRecord.getReadingInfoId());
                            pstmt.setString(4, oaReadingRecord.getReadingTime());
                            pstmt.setString(5, oaReadingRecord.getReader());
                            pstmt.setString(6, oaReadingRecord.getVisitMachine());
                            pstmt.setString(7, oaReadingRecord.getIp());
                            pstmt.setInt(8, oaReadingRecord.getVisitCount());
                            int row = pstmt.executeUpdate();
                            if (row > 0) {
                                rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
                        } catch (Exception ex) {
                            rst.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
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

    //修改阅读痕迹
    public Result updateOAReadingRecord(final OAReadingRecord oaReadingRecord) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            PreparedStatement pstmt = conn.
                                    prepareStatement("update OAReadingRecord set readingInfoTable=?,readingInfoId=?,readingTime=?,reader=?,visitMachine=?,ip=?,visitCount=? where id=?");
                            pstmt.setString(1, oaReadingRecord.getReadingInfoTable());
                            pstmt.setString(2, oaReadingRecord.getReadingInfoId());
                            pstmt.setString(3, oaReadingRecord.getReadingTime());
                            pstmt.setString(4, oaReadingRecord.getReader());
                            pstmt.setString(5, oaReadingRecord.getVisitMachine());
                            pstmt.setString(6, oaReadingRecord.getIp());
                            pstmt.setInt(7, oaReadingRecord.getVisitCount());
                            pstmt.setString(8, oaReadingRecord.getId());
                            int row = pstmt.executeUpdate();
                            if (row > 0) {
                                rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
                        } catch (Exception ex) {
                            rst.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
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

    //删除阅读痕迹
    public Result deleltOAReadingRecord(final String id) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        String sqlStr =  "delete from OAReadingRecord where id='"+id+"'";
                        try {
                            Connection conn = connection;
                            Statement s = conn.createStatement();
                            int row = s.executeUpdate(sqlStr);
                            if (row > 0) {
                                rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
                        } catch (Exception ex) {
                            rst.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
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
    //根据ID获取某个阅读记录
    public Result getOAReadingRecord(final String id) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            PreparedStatement pstmt = conn.prepareStatement(
                                    "select * from OAReadingRecord where id='"+id+"'");
                            ResultSet rs = pstmt.executeQuery();
                            OAReadingRecord obj = new OAReadingRecord();
                            if (rs.next()) {
                                obj.setId(rs.getString("id"));
                                obj.setIp(rs.getString("ip"));
                                obj.setReader(rs.getString("reader"));
                                obj.setReadingInfoId(rs.getString("readingInfoId"));
                                obj.setReadingInfoTable(rs.getString("readingInfoTable"));
                                obj.setReadingTime(rs.getString("readingTime"));
                                obj.setVisitCount(rs.getInt("visitCount"));
                                obj.setVisitMachine(rs.getString("visitMachine"));
                            }
                            rst.setRetVal(obj);
                        } catch (Exception ex) {
                            rst.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error :", ex);
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
    //根据信息内容查找此内容有关的阅读痕迹
    public Result selectRecordByContent(final String readingInfoTable,final String readingInfoId) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            PreparedStatement pstmt = conn.prepareStatement(
                                    "select * from OAReadingRecord where readingInfoTable='"+readingInfoTable+"' and readingInfoId='"+readingInfoId+"' order by readingTime desc");
                            ResultSet rs = pstmt.executeQuery();
                            List<OAReadingRecord> listRecord=new ArrayList<OAReadingRecord>();
                            while (rs.next()) {
                            	OAReadingRecord obj = new OAReadingRecord();
                                obj.setId(rs.getString("id"));
                                obj.setIp(rs.getString("ip"));
                                obj.setReader(rs.getString("reader"));
                                obj.setReadingInfoId(rs.getString("readingInfoId"));
                                obj.setReadingInfoTable(rs.getString("readingInfoTable"));
                                obj.setReadingTime(rs.getString("readingTime"));
                                obj.setVisitCount(rs.getInt("visitCount"));
                                obj.setVisitMachine(rs.getString("visitMachine"));
                                listRecord.add(obj);
                            }
                            rst.setRetVal(listRecord);
                        } catch (Exception ex) {
                            rst.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error :", ex);
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
    
    //根据用户ID查找某用户所有有关阅读痕迹
    public Result selectRecordByUserId(final String reader) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            PreparedStatement pstmt = conn.prepareStatement(
                                    "select * from OAReadingRecord where reader='"+reader+"'");
                            ResultSet rs = pstmt.executeQuery();
                            List<OAReadingRecord> listRecord=new ArrayList<OAReadingRecord>();
                            while (rs.next()) {
                            	OAReadingRecord obj = new OAReadingRecord();
                                obj.setId(rs.getString("id"));
                                obj.setIp(rs.getString("ip"));
                                obj.setReader(rs.getString("reader"));
                                obj.setReadingInfoId(rs.getString("readingInfoId"));
                                obj.setReadingInfoTable(rs.getString("readingInfoTable"));
                                obj.setReadingTime(rs.getString("readingTime"));
                                obj.setVisitCount(rs.getInt("visitCount"));
                                obj.setVisitMachine(rs.getString("visitMachine"));
                                listRecord.add(obj);
                            }
                            rst.setRetVal(listRecord);
                        } catch (Exception ex) {
                            rst.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error :", ex);
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
    //根据信息内容查找此内容有关的阅读痕迹
    public Result selectRecordByConditions(final String readingInfoTable,final String readingInfoId,final String jobName,final String department,final String userName) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select r.id,r.readingInfoId,r.readingInfoTable,r.reader,r.visitCount,r.readingTime,r.visitMachine,r.ip from OAReadingRecord r left join tblEmployee l on l.id=r.reader left join tblDepartment d on d.classCode=l.DepartmentCode where r.readingInfoTable='"+readingInfoTable+"' and r.readingInfoId='"+readingInfoId+"'";
                            if(jobName!=null&&jobName!="")
                            {
                            	sql+=" and l.TitleID='"+jobName+"'";
                            }
                            if(userName!=null&&userName!="")
                            {
                            	sql+=" and l.EmpFullName like '%"+userName+"%'";
                            }
                            if(department!=null&&department!="")
                            {
                            	sql+=" and d.DeptFullName like '%"+department+"%'";
                            }
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            ResultSet rs = pstmt.executeQuery();
                            List<OAReadingRecord> listRecord=new ArrayList<OAReadingRecord>();
                            while (rs.next()) {
                            	OAReadingRecord obj = new OAReadingRecord();
                                obj.setId(rs.getString("id"));
                                obj.setIp(rs.getString("ip"));
                                obj.setReader(rs.getString("reader"));
                                obj.setReadingInfoId(rs.getString("readingInfoId"));
                                obj.setReadingInfoTable(rs.getString("readingInfoTable"));
                                obj.setReadingTime(rs.getString("readingTime"));
                                obj.setVisitCount(rs.getInt("visitCount"));
                                obj.setVisitMachine(rs.getString("visitMachine"));
                                listRecord.add(obj);
                            }
                            rst.setRetVal(listRecord);
                        } catch (Exception ex) {
                            rst.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error :", ex);
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
    //获取所有职位枚举值
    public Result selectAllJobName(final HttpServletRequest req) {
        final Result rst = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        Locale locale = (Locale) req.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY);
                        
                        try {
                        	String sql = "select e.enumName,l."+locale+",ei.id,ei.enumValue from tblDBEnumerationItem ei left join tblLanguage l on l.id=ei.languageId left join tblDBEnumeration e on e.id=ei.enumId where e.enumName='duty'";
                            System.out.println(sql);
                        	PreparedStatement pstmt = conn.prepareStatement(sql);
                            ResultSet rss = pstmt.executeQuery();
                            List ls = new ArrayList();
                            while (rss.next()) {
                            	 String value[] = new String[4];
                                value[0] = rss.getString("id"); //将id
                                value[1] = rss.getString("enumName"); //名称
                                value[2] = rss.getString("enumValue"); //枚举值
                                value[3] = rss.getString(locale.toString()); //枚举显示值
                                ls.add(value);
                            }
                            rst.setRetVal(ls);
                            rst.setRealTotal(ls.size());
                            rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        } catch (Exception ex) {
                            rst.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error :", ex);
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
