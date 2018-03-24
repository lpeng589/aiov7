package com.menyi.aio.web.billNumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.TblBillNoBean;
import com.menyi.aio.bean.TblBillNoHistoryBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

/**
 * 
 * <p>Title:单据编号Mgt</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class BillNoMgt extends AIODBManager {

	
	/**
	 * 查询单据规则数据
	 * @param form
	 * @return
	 */
	public Result query(final BillNoSearchForm form){
		
		//SQL语句
		StringBuffer sql = new StringBuffer("SELECT [ID],[KEY],[PATTERN],[START],[STEP]");
		sql.append(",[ISFILLBACK],[RESET],[LASTSTAMP],[EXPLAIN],[BILLNAME],[ISADDBEFORM],[STATUSID],ROW_NUMBER() OVER(ORDER BY [ID] DESC)");
		sql.append(" AS row_id FROM tblBillNo WHERE ISNULL([STATUSID],0) != -1");
		/* key条件搜索*/
		if(form.getKeySearch() != null && !"".equals(form.getKeySearch())){
			sql.append(" AND ([KEY] like '%" + form.getKeySearch() + "%' or [EXPLAIN] like '%" + form.getKeySearch() + "%' or [BILLNAME] like '%"+form.getKeySearch()+"%')");
		}
		BaseEnv.log.debug("BillNoMgt.query "+sql.toString());
		ArrayList param=new ArrayList();
		return sqlListMaps(sql.toString(), param, form.getPageNo(), form.getPageSize());
	}
	
	
	/**
	 * 增加单据编号规则
	 * @param bean
	 * @return
	 */
	public Result addBillNumber(final TblBillNoBean bean){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	try{
                    		StringBuffer sql = new StringBuffer("INSERT INTO tblBillNo([KEY],[PATTERN],");
                    		sql.append("[START],[STEP],[ISFILLBACK],[RESET],[LASTSTAMP],[BILLNO],[EXPLAIN],[BILLNAME],[ISADDBEFORM],[STATUSID],[ISDEFAULTLOGINPERSON])");
                    		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    		PreparedStatement ps = conn.prepareStatement(sql.toString());
                    		ps.setString(1, bean.getKey());
                    		ps.setString(2, bean.getPattern());
                    		ps.setInt(3, bean.getStart());
                    		ps.setInt(4, bean.getStep());
                    		ps.setBoolean(5, bean.getIsfillback());
                    		ps.setInt(6, bean.getReset());
                    		ps.setLong(7, bean.getLaststamp());
                    		ps.setInt(8, bean.getBillNO());
                    		ps.setString(9, bean.getExplain());
                    		ps.setString(10, bean.getBillName());
                    		ps.setBoolean(11, bean.getIsAddbeform());
                    		ps.setString(12, bean.getStatusId());
                    		ps.setString(13, bean.getIsDefaultLoginPerson());
                    		ps.executeUpdate();
                    		dealDefaultLogin(conn,bean.getKey().substring(0,bean.getKey().indexOf("_")),Integer.parseInt(bean.getIsDefaultLoginPerson()));
                    	} catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	/**
	 * 根据ID加载单据规则数据
	 * @param id
	 * @return
	 */
	public Result onLoad(final String key){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	Result re = onLoad(key,conn);
                    	rs.setRetVal(re.getRetVal());
                    	rs.setRetCode(re.getRetCode());
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	/**
	 * 修改单据规则
	 * @param bean
	 * @return
	 */
	public Result updateBillNumber(final TblBillNoBean bean){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	try{
                    		StringBuffer sql = new StringBuffer("UPDATE tblBillNo SET [KEY]=?,[PATTERN]=?,");
                    		sql.append("[START]=?,[STEP]=?,[ISFILLBACK]=?,[RESET]=?,[LASTSTAMP]=?,[EXPLAIN]=?,[BILLNAME]=?,[ISADDBEFORM]=?,[STATUSID]=?,[ISDEFAULTLOGINPERSON]=? WHERE [ID]=?");
                    		PreparedStatement ps = conn.prepareStatement(sql.toString());
                    		ps.setString(1, bean.getKey());
                    		ps.setString(2, bean.getPattern());
                    		ps.setInt(3, bean.getStart());
                    		ps.setInt(4, bean.getStep());
                    		ps.setBoolean(5, bean.getIsfillback());
                    		ps.setInt(6, bean.getReset());
                    		ps.setLong(7, bean.getLaststamp());
                    		ps.setString(8, bean.getExplain());
                    		ps.setString(9, bean.getBillName());
                    		ps.setBoolean(10, bean.getIsAddbeform());
                    		ps.setString(11, bean.getStatusId());
                    		ps.setString(12, bean.getIsDefaultLoginPerson());
                    		ps.setInt(13, bean.getId());
                    		ps.executeUpdate();
                    		dealDefaultLogin(conn,bean.getKey().substring(0,bean.getKey().indexOf("_")),Integer.parseInt(bean.getIsDefaultLoginPerson()));
                    	} catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	/**
	 * 删除单据规则
	 * @param keyIds
	 * @return
	 */
    public Result delete(final String[] ids) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String ss = "";
                            for (int i = 0; i < ids.length; i++) {
                                ss += "'" + ids[i] + "',";
                            }
                            ss = ss.substring(0, ss.length() - 1);
                            String sqlString =
                                    "DELETE tblBillNo WHERE ID IN (" + ss +
                                    ")";
                            Statement pstmt = conn.createStatement();
                            int row = pstmt.executeUpdate(sqlString);
                            if (row > 0) {
                                rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
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
    
    /**
     * 根据条件查询是否存在记录 历史记录表
     * @param varStr
     * @return
     */
    public Result queryBillNoHistory(final TblBillNoHistoryBean bean){
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	Result re = queryBillNoHis(bean, conn);
                    	rs.setRetVal(re.getRetVal());
                    	rs.setRetCode(re.getRetCode());
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
    }
    
    
    /**
     * 修改tblBillNoHistory remove 数据
     * @param remove   0已使 1空出来 2已填空
     * @return
     */
    public Result updateRemoved(final Integer remove, final String formatedstring, final String key){
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	Result re = updateRemoved(remove, formatedstring, key, conn);
                    	rs.setRetCode(re.getRetCode());
                    	rs.setRetVal(re.getRetVal());
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
    }
    
    /**
     * 修改tblBillNoHistory remove 数据
     * @param remove   0已使 1空出来 2已填空
     * @return
     */
    public Result updateRemoved(final Integer remove, final String formatedstring, final String key, final Connection conn){
    	final Result rs=new Result();
    	if(conn == null){
    		return updateRemoved(remove, formatedstring, key);
    	}
    	try{
    		StringBuffer sql = new StringBuffer("UPDATE tblBillNoHistory SET [REMOVED]=?");
    		sql.append(" WHERE [FORMATEDSTRING]=? AND [KEY]=?");
    		PreparedStatement ps = conn.prepareStatement(sql.toString());
    		ps.setInt(1, remove);
    		ps.setString(2, formatedstring);
    		ps.setString(3, key);
    		ps.executeUpdate();
    	} catch (Exception ex) {
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            ex.printStackTrace();
        }
        return rs ;
    }
    
    
    /**
     * 修改tblBillNoHistory remove 数据
     * @param key
     * @param formatedstring       
     * 修改timestamp最大的一个
     * @return
     */
    public Result updateBillNoHistoryRemove(final String key, final String formatedstring, final Integer removed){
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	Result re = updateBillNoHistoryRemove(key, formatedstring, removed, conn);
                    	rs.setRetVal(re.getRetVal());
                    	rs.setRetCode(re.getRetCode());
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
    }
    
    /**
     * 修改单据编号规则   billNO,laststamp
     * @param billNO
     * @param laststamp
     * @param key			条件
     * @return
     */
	public Result updateBillNo(final Integer billNO, final long laststamp, final String key){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	Result re = updateBillNo(billNO, laststamp, key, conn);
                    	rs.setRetVal(re.getRetVal());
                    	rs.setRetCode(re.getRetCode());
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	/**
	 * 修改单据编号规则   billNO,laststamp
	 * @param billNO
	 * @param laststamp
	 * @param key
	 * @param conn
	 * @return
	 */
	public Result updateBillNo(final Integer billNO, final long laststamp, final String key, final Connection conn){
		final Result rs=new Result();
		if(conn == null){
			return updateBillNo(billNO, laststamp, key);
		}
		try{
    		StringBuffer sql = new StringBuffer("UPDATE tblBillNo SET [BILLNO]=?,[LASTSTAMP]=?");
    		sql.append(" WHERE [KEY]=?");
    		PreparedStatement ps = conn.prepareStatement(sql.toString());
    		ps.setInt(1, billNO);
    		ps.setLong(2, laststamp);
    		ps.setString(3, key);
    		ps.executeUpdate();
    	} catch (Exception ex) {
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            ex.printStackTrace();
        }
    	return rs;
	}
	
	/**
	 * 增加历史记录
	 * @param bean
	 * @return
	 */
	public Result addBillNoHistory(final TblBillNoHistoryBean bean){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	addBillNoHistory(bean, conn);
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	
	/**
	 * 增加历史记录
	 * @param bean
	 * @return
	 */
	public Result addBillNoHistory(final TblBillNoHistoryBean bean, final Connection conn){
		final Result rs=new Result();
		if(conn == null){
			return addBillNoHistory(bean);
		}
    	try{
    		StringBuffer sql = new StringBuffer("INSERT INTO tblBillNoHistory([KEY],[VALUE],");
    		sql.append("[FORMATEDSTRING],[TIMESTAMP],[REMOVED])");
    		sql.append(" VALUES(?,?,?,?,?)");
    		PreparedStatement ps = conn.prepareStatement(sql.toString());
    		ps.setString(1, bean.getKey());
    		ps.setInt(2, bean.getValue());
    		ps.setString(3, bean.getFormatedString());
    		ps.setLong(4, bean.getTimestamp());
    		ps.setInt(5, bean.getRemoved());
    		ps.executeUpdate();
    	} catch (Exception ex) {
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            ex.printStackTrace();
        }
        return rs ;
	}
	
	/**
	 * 根据KEY删除所有满足条件的历史记录
	 * @param bean
	 * @return
	 */
	public Result updateBillNoHistory(final String key){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	updateBillNoHistory(key, conn);
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	
	/**
	 * 根据KEY删除所有满足条件的历史记录
	 * @param bean
	 * @return
	 */
	public Result updateBillNoHistory(final String key,final Connection conn){
		final Result rs=new Result();
		if(conn == null){
			return updateBillNoHistory(key);
		}
    	try{
    		StringBuffer sql = new StringBuffer("DELETE tblBillNoHistory");
    		sql.append(" WHERE [KEY]=?");
    		PreparedStatement ps = conn.prepareStatement(sql.toString());
    		ps.setString(1, key);
    		int count = ps.executeUpdate();
    		if(count>0){
    			rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
    		}else{
    			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
    		}
    	} catch (Exception ex) {
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            ex.printStackTrace();
        }
        return rs ;
	}
	
	
	/**
     * 修改tblBillNoHistory remove 数据
     * @param key
     * @param formatedstring       
     * 修改timestamp最大的一个
     * @return
     */
    public Result updateBillNoHistoryRemove(final String key, final String formatedstring, final Integer removed, final Connection conn){
    	final Result rs=new Result();
    	if(conn == null){
			return updateBillNoHistoryRemove(key, formatedstring, removed);
		}
        try{
        	StringBuffer sql = new StringBuffer("UPDATE tblBillNoHistory SET [REMOVED]=?");
            sql.append(" WHERE [FORMATEDSTRING]=? AND [KEY]=? AND [REMOVED]=?");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setInt(1, removed);
            ps.setString(2, formatedstring);
            ps.setString(3, key);
            ps.setInt(4, 0);
            ps.executeUpdate();
         } catch (Exception ex) {
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            ex.printStackTrace();
         }
        return rs ;
    }
    
    
    /**
     * 根据条件查询是否存在记录 历史记录表
     * @param varStr
     * @return
     */
    public Result queryBillNoHis(final TblBillNoHistoryBean bean, final Connection conn){
    	final Result rs=new Result();
    	if(conn == null){
    		return queryBillNoHistory(bean);
    	}
    	try{
    		StringBuffer sql = new StringBuffer("SELECT TOP 1 [ID],[KEY],[VALUE],[FORMATEDSTRING], ");
    		sql.append("[TIMESTAMP],[REMOVED] FROM tblBillNoHistory");
    		sql.append(" WHERE 1=1 ");
    		
    		if(bean.getFormatedString() != null && !"".equals(bean.getFormatedString())){
    			sql.append(" AND [FORMATEDSTRING]='"+bean.getFormatedString()+"'");
    		}
    		if(bean.getRemoved() != null){
    			sql.append(" AND [REMOVED]="+bean.getRemoved());
    		}
    		if(bean.getKey() != null && !"".equals(bean.getKey())){
    			sql.append(" AND [KEY]='"+bean.getKey()+"'");
    		}
    		sql.append(" ORDER BY [VALUE] ASC");
    		BaseEnv.log.debug("BillNoMgt.queryBillNoHis 查询补号编号:"+sql);
    		Statement st = conn.createStatement();
    		ResultSet rset = st.executeQuery(sql.toString());
    		TblBillNoHistoryBean hisBean = null;
    		if(rset.next()){
    			hisBean = new TblBillNoHistoryBean();
    			hisBean.setId(rset.getInt("ID"));
    			hisBean.setKey(rset.getString("KEY"));
    			hisBean.setValue(rset.getInt("VALUE"));
    			hisBean.setFormatedString(rset.getString("FORMATEDSTRING"));
    			hisBean.setTimestamp(rset.getLong("TIMESTAMP"));
    			hisBean.setRemoved(rset.getInt("REMOVED"));
    		}
    		rs.setRetVal(hisBean);
    	} catch (Exception ex) {
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            ex.printStackTrace();
        }
        return rs ;
    }
    
    /**
     * 根据key中的值查询表中数据，以判断单据编号是否重复
     * @param key
     * @return
     */
    public Result queryTableData(final String key, final String valueStr){
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	try{
                    		String tablename=key.substring(0,key.indexOf("_"));
                    		String fieldname=key.substring(key.indexOf("_")+1);
                    		StringBuffer sql = new StringBuffer("select "+fieldname+" from "+tablename);
                    		sql.append(" WHERE "+fieldname+"="+valueStr);
                    		Statement st = conn.createStatement();
                    		ResultSet rse = st.executeQuery(sql.toString());
                    		if(rse.next()){
                    			rs.setRetVal("YES");
                    		}
                    	} catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
    }
    
    
    /**
	 * 根据ID加载单据规则数据
	 * @param id
	 * @return
	 */
    public Result onLoad(final String key, final Connection conn){
		final Result rs=new Result();
		if(conn == null){
			return onLoad(key);
		}
        try{
        	StringBuffer sql = new StringBuffer("SELECT [ID],[KEY],[PATTERN],");
            sql.append("[START],[STEP],[ISFILLBACK],[RESET],[LASTSTAMP],[BILLNO],[EXPLAIN],[BILLNAME],[ISADDBEFORM],[STATUSID],[ISDEFAULTLOGINPERSON] FROM tblBillNo");
            sql.append(" WHERE [KEY]=?");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setString(1, key);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
	            TblBillNoBean bean = new TblBillNoBean();
	            bean.setId(rset.getInt("ID"));
	            bean.setKey(rset.getString("KEY"));
	            bean.setPattern(rset.getString("PATTERN"));
				bean.setStart(rset.getInt("START"));
				bean.setStep(rset.getInt("STEP"));
				bean.setIsfillback(rset.getBoolean("ISFILLBACK"));
				bean.setReset(rset.getInt("RESET"));
				bean.setLaststamp(rset.getLong("LASTSTAMP"));
				bean.setBillNO(rset.getInt("BILLNO"));
				bean.setExplain(rset.getString("EXPLAIN"));
				bean.setBillName(rset.getString("BILLNAME")); 
				bean.setIsAddbeform(rset.getBoolean("ISADDBEFORM"));
				bean.setStatusId(rset.getString("STATUSID"));
				bean.setIsDefaultLoginPerson(rset.getString("ISDEFAULTLOGINPERSON"));
				rs.setRetVal(bean);
				rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
			}else{
		        rs.setRetCode(ErrorCanst.ER_NO_DATA);
		    }
		} catch (Exception ex) {
		    rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		    ex.printStackTrace();
		}
        return rs ;
	}
    
    public int queryBillNo(final String key, final Connection conn){
    	int ret = -1;
		final Result rs=new Result();
        try{
        	StringBuffer sql = new StringBuffer("SELECT [BILLNO] FROM tblBillNo");
            sql.append(" WHERE [KEY]=?");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setString(1, key);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
				rs.setRetVal(rset.getInt("BILLNO"));
				rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
			}else{
		        rs.setRetCode(ErrorCanst.ER_NO_DATA);
		    }
		} catch (Exception ex) {
		    rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		    ex.printStackTrace();
		}
        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
        	ret = Integer.parseInt(""+rs.retVal);
        }
        return ret ;
	}
    
    /**
	 * 增加单据编号规则
	 * @param bean
	 * @return
	 */
	public Result updateDbFieldIdentityStr(final String key, final byte inputType){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	try{
                    		StringBuffer sql = new StringBuffer("update tbldbfieldInfo set inputType=? ");
                    		sql.append("where tableid in (select id from tbldbtableinfo where tablename=?)");
                    		sql.append("and fieldName=?");
                    		PreparedStatement ps = conn.prepareStatement(sql.toString());
                    		ps.setByte(1, inputType);
                    		ps.setString(2, key.substring(0,key.indexOf("_")));
                    		ps.setString(3, key.substring(key.indexOf("_")+1));
                    		ps.executeUpdate();
                    	} catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	/**
	 * 查询所有的表
	 * @param searchName  搜索条件
	 * @return
	 */
	public Result selectTable(final String searchName){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	try{
                    		StringBuffer sql = new StringBuffer("SELECT tab.id,l.zh_CN,tab.tableName FROM tblDBTableInfo tab");
                    		sql.append(" LEFT JOIN tblLanguage l ON tab.languageId = l.id WHERE tab.id IN ");
                    		sql.append("(SELECT tableId FROM tblDBFieldInfo field WHERE fieldIdentityStr='BillNo')");
                    		
                    		if(searchName != null && !"".equals(searchName)){
                    			sql.append(" and (l.zh_CN like '%"+searchName+"%' or tab.tableName like '%"+searchName+"%')");
                    		}
                    		PreparedStatement ps = conn.prepareStatement(sql.toString());
                    		ResultSet rset = ps.executeQuery();
                    		List<String[]> list = new ArrayList<String[]>();
                    		while (rset.next()) {
                    			String[] str = new String[3];
                    			str[0] = rset.getString("id");
                    			str[1] = rset.getString("zh_CN");
                    			str[2] = rset.getString("tableName");
                    			list.add(str);
							}
                    		rs.setRetVal(list);
                    		rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                    	} catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	/**
	 * 根据表查询满足条件的字段
	 * @param searchName
	 * @return
	 */
	public Result selectField(final String tableId){
		final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	try{
                    		StringBuffer sql = new StringBuffer("SELECT t1.zh_CN as table_cn,t.zh_CN as field_cn,tab.tableName,t2.fieldName");
                    		sql.append(" FROM tblLanguage t,tblDBFieldInfo t2,tblDBTableInfo tab,");
                    		sql.append(" tblLanguage t1 WHERE t.id=t2.languageId and t2.tableId = tab.id");
                    		sql.append(" AND t1.id=tab.languageId and t2.tableId=? AND t2.fieldIdentityStr='BillNo'");
                    		PreparedStatement ps = conn.prepareStatement(sql.toString());
                    		ps.setString(1, tableId);
                    		ResultSet rset = ps.executeQuery();
                    		List<String[]> list = new ArrayList<String[]>();
                    		while (rset.next()) {
                    			String[] str = new String[4];
                    			str[0] = rset.getString("table_cn");
                    			str[1] = rset.getString("field_cn");
                    			str[2] = rset.getString("tableName");
                    			str[3] = rset.getString("fieldName");
                    			list.add(str);
							}
                    		rs.setRetVal(list);
                    		rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                    	} catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
            	});
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	}
	
	
	/**
	 * 处理  经手人默认为登录者
	 * @param conn
	 * @param tableName
	 * @param isDefault
	 * @return
	 * @throws Exception
	 */
	public Result dealDefaultLogin(Connection conn,String tableName,Integer isDefault) throws Exception{
		final Result rs=new Result();
		try{
			Statement st = conn.createStatement();
			String sql = "update tblDBFieldInfo set ";
			if(isDefault == 0){
				sql += "defaultValue='@'+'Sess:UserId;'+'@'+'Sess:UserName' ";
			}else{
				sql += "defaultValue='' ";
			}
			sql += " from tblDBFieldInfo where tableId in (select id from tblDBTableInfo where tableName='"+tableName+"') and fieldName='EmployeeID'";
			st.addBatch(sql);
			
			sql = "update tblDBFieldInfo set ";
			if(isDefault == 0){
				sql += "defaultValue='@'+'Sess:DepartmentCode;'+'@'+'Sess:DepartmentName' ";
			}else{
				sql += "defaultValue='' ";
			}
			sql += " from tblDBFieldInfo where tableId in (select id from tblDBTableInfo where tableName='"+tableName+"') and fieldName='DepartmentCode'";
			st.addBatch(sql);
			st.executeBatch();
			rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
    	} catch (Exception ex) {
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            ex.printStackTrace();
        }
		return rs;
	}
}
