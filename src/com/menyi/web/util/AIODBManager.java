package com.menyi.web.util;

import com.dbfactory.hibernate.DBManager;

import java.util.HashMap;
import java.util.List;
import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.sql.SQLException;
import org.hibernate.Session;
import java.sql.PreparedStatement;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author not attributable
 * @version 1.0
 * @preserve all
 */
public class AIODBManager extends DBManager {
	
	public Result sqlListMap(final String sql, final List param,final int pageNo,final int pageSize) {
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
	                        if(pageSize>0){//查询总行数
	                        	sqlStr="select count(0) from ("+sql+") as a";
	                        	pstmt=conn.prepareStatement(sqlStr);
	                        	for(int i = 1;param!=null&&i<=param.size();i++){
	                                pstmt.setObject(i,param.get(i-1));
	                            }
	                            rs=pstmt.executeQuery();                            	
	                            if(rs.next()){
	                            	int totalSize=rs.getInt(1);
	                              	rst.setTotalPage(totalSize%pageSize>0?totalSize/pageSize+1:totalSize/pageSize);
	                            }
	                              	
	                            sqlStr="select * from ("+sql+") as a";
	                            sqlStr+=" where row_id between "+(pageSize*(pageNo-1)+1)+" and "+pageSize*pageNo;    
	                        }else{
	                            sqlStr=sql.toString();
	                        }
                           
                            pstmt = conn.prepareStatement(sqlStr);
                            for(int i = 1;param!=null&&i<=param.size();i++){
                                pstmt.setObject(i,param.get(i-1));
                            }
                            List list = new ArrayList();
                            rs = pstmt.executeQuery();
                            while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
                            }
                            rst.setRetVal(list);
                            rst.setPageNo(pageNo);
                            rst.setPageSize(pageSize);
                            rst.setRealTotal(list.size());
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
	public Result sqlListMap(final String sql, final List param) {
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
	                        
	                        sqlStr=sql.toString();
                           
                            pstmt = conn.prepareStatement(sqlStr);
                            for(int i = 1;param!=null&&i<=param.size();i++){
                                pstmt.setObject(i,param.get(i-1));
                            }
                            List list = new ArrayList();
                            rs = pstmt.executeQuery();
                            while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
                            }
                            rst.setRetVal(list);
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
	
    public Result sqlList(final String sql, final List param) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            for(int i = 1;i<=param.size();i++){
                                pstmt.setObject(i,param.get(i-1));
                            }
                            List list = new ArrayList();
                            ResultSet rs = pstmt.executeQuery();
                            
                            while (rs.next()) {
                                Object[] os = new Object[rs.getMetaData().
                                              getColumnCount()];
                                for (int i = 1; i <= os.length; i++) {
                                   os[i - 1] = rs.getObject(i);
                                }
                                list.add(os);
                            }
                            rst.setRetVal(list);
                        } catch (Exception ex) {
                        	BaseEnv.log.error(sql,ex);
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

    public Result sqlList(final String sql,final List param,final int pageSize,final int pageNo,
                          final boolean ifCount) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;

                            if (ifCount) {
                                /**
                                 * 查询总记录条数
                                 */
                                String countSql = sql;
                                try {
                                    if (countSql.indexOf("order by") > 0) {
                                        countSql = countSql.substring(0,
                                                countSql.indexOf("order by"));
                                    }
                                    countSql =
                                            " select count(*) totalCount from (" +
                                            countSql + ") t_tmp_tab ";
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                PreparedStatement pstmt = conn.prepareStatement(countSql);
                                for (int i = 1; i <= param.size(); i++) {
                                    pstmt.setObject(i, param.get(i - 1));
                                }

                                rst.pageNo = pageNo;
                                int total =0;
                                ResultSet rs = pstmt.executeQuery();
                                if (rs.next()) {
                                    total = rs.getInt(1);
                                }
                                rst.realTotal = total;
                                rst.pageSize = pageSize;
                                //计算总页数
                                rst.totalPage = ((total + pageSize) - 1) /
                                               pageSize;
                                if (rst.pageNo > rst.totalPage)
                                    rst.pageNo = rst.totalPage;
                                if (rst.pageNo < 1)
                                    rst.pageNo = 1;
                            }else{
                                rst.setPageNo(pageNo);
                                rst.setPageSize(pageSize);
                            }

                            String esql = "select top "+(rst.pageNo*rst.pageSize)+sql.substring(sql.toLowerCase().indexOf("select")+6)+"";

                            PreparedStatement pstmt = conn.prepareStatement(esql);
                            for(int i = 1;i<=param.size();i++){
                                pstmt.setObject(i,param.get(i-1));
                            }
                            //pstmt.setf
                            List list = new ArrayList();
                            ResultSet rs = pstmt.executeQuery();
                            int pos = 0;
                            while (rs.next()) {
                                if(pos < (rst.pageNo-1)*rst.pageSize){
                                	pos++;
                                    continue;
                                }
                                Object[] os = new Object[rs.getMetaData().
                                              getColumnCount()];
                                for (int i = 1; i <= os.length; i++) {
                                    os[i - 1] = rs.getObject(i);
                                }
                                pos++;
                                list.add(os);
                            }
                            rst.setRetVal(list);
                        } catch (Exception ex) {
                        	BaseEnv.log.error(sql,ex);
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
    
    
    public Result sqlListMaps(final String sql, final List param,final int pageNo,final int pageSize) {
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	PreparedStatement pstmt ;
                        ResultSet rs;
                        String sqlStr="";
                        int total=0;
                        int pageNo2 = pageNo;
                        if (pageNo2 < 1){
                        	pageNo2 = 1;
                        }
                        try {  
	                        if(pageSize>0){//查询总行数
	                        	sqlStr="select count(0) from ("+sql+") as a";
	                        	pstmt=conn.prepareStatement(sqlStr);
	                        	for(int i = 1;param!=null&&i<=param.size();i++){
	                                pstmt.setObject(i,param.get(i-1));
	                            }
	                            rs=pstmt.executeQuery();                            	
	                            if(rs.next()){
	                            	int totalSize=rs.getInt(1);
	                            	total = totalSize;
	                              	int totalPage = totalSize%pageSize>0?totalSize/pageSize+1:totalSize/pageSize;
	                              	if(pageNo2>totalPage){
	                              		pageNo2 = totalPage;
	                              	}
	                              	rst.setTotalPage(totalPage);
	                            }
	                            sqlStr="select * from ("+sql+") as a";
	                            sqlStr+=" where row_id between "+(pageSize*(pageNo2-1)+1)+" and "+pageSize*pageNo2;    
	                        }else{
	                            sqlStr=sql.toString();
	                        }
                           
                            pstmt = conn.prepareStatement(sqlStr);
                            for(int i = 1;param!=null&&i<=param.size();i++){
                                pstmt.setObject(i,param.get(i-1));
                            }
                            
                            List list = new ArrayList();
                            rs = pstmt.executeQuery();
                            while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
                            }
                            rst.setRetVal(list);
                            rst.setPageNo(pageNo2);
                            rst.setPageSize(pageSize);
                            rst.setRealTotal(total);
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
