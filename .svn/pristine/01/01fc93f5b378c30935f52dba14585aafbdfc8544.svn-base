package com.menyi.aio.web.bol88;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.MailPoolThread;

public class AIOBOL88Mgt {
    /**
     * 查询已接收短信详细
     * @param keyId
     * @return
     */
    public Result queryBol88() {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String sql =
                                    "select * from tblBol88Set ";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                AIOBOL88Bean bean = new AIOBOL88Bean();
                                bean.setUserName( rs.getString("userName"));
                                bean.setPassword(rs.getString("password"));
                                bean.setFlag(rs.getInt("flag"));
                                rst.setRetVal(bean);
                                rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }else{
                                rst.setRetCode(ErrorCanst.ER_NO_DATA);
                            }
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", ex);
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
    public Result updateBol88(final String userName,final String password) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String sql =
                                    "select * from tblBol88Set ";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                sql = "update tblBol88Set set userName='"+
                                      userName+"',password='"+password+"' ";
                            }else{
                                sql = "insert into tblBol88Set(userName,password,flag) values('"+
                                      userName+"','"+password+"',0) ";
                            }
                            pstmt = conn.prepareStatement(sql);
                            pstmt.execute();
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }

    public Result closeBol88(final String flag) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String sql = "update tblBol88Set set flag='"+
                                      flag+"'";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.execute();
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }
     
    public String getKeyword(String keyword) {
		Result rs = new Result();
		try {
			URL url = new URL(BaseEnv.bol88URL + "/MailPool?op=info");
			HttpURLConnection url_con = (HttpURLConnection) url
					.openConnection();
			url_con.setRequestMethod("POST"); 
			url_con.setConnectTimeout(50000);//（单位：毫秒）jdk1.5换成这个,连接超时
			url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时

			url_con.setDoOutput(true);
			String key =  "keyword="+keyword; 
			url_con.getOutputStream().write(key.getBytes("UTF-8"));   
			url_con.getOutputStream().flush(); 
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			byte[] b = new byte[8190];
			byte[] bs = new byte[0];
			int count = 0;
			while ((count = in.read(b)) > -1) {
				byte[] temp = bs;
				bs = new byte[temp.length + count];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, count);
			}
			String ret = new String(bs,"utf-8");
			in.close();
			url_con.disconnect();
			return ret;
			
		}catch (IOException e) {
			BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", e);
		}

		return "";
	}
    
    public int getKeywordNum(String keywordIds) {
		Result rs = new Result();
		try {
			URL url = new URL(BaseEnv.bol88URL + "/MailPool?op=num");
			HttpURLConnection url_con = (HttpURLConnection) url
					.openConnection();
			url_con.setRequestMethod("POST"); 
			url_con.setConnectTimeout(50000);//（单位：毫秒）jdk1.5换成这个,连接超时
			url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时

			url_con.setDoOutput(true);
			String key =  "keywordIds="+keywordIds; 
			url_con.getOutputStream().write(key.getBytes("UTF-8"));   
			url_con.getOutputStream().flush(); 
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			byte[] b = new byte[8190];
			byte[] bs = new byte[0];
			int count = 0;
			while ((count = in.read(b)) > -1) {
				byte[] temp = bs;
				bs = new byte[temp.length + count];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, count);
			}
			String ret = new String(bs,"utf-8");
			in.close();
			url_con.disconnect();
			return Integer.parseInt(ret);
			
		}catch (Exception e) {
			BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", e);
		}

		return 0;
	}
    
    public String getMail(String keywordIds,int sum,String username,String JSESSION) {
		Result rs = new Result();
		try {
			URL url = new URL(BaseEnv.bol88URL + "/MailPool?op=down");
			HttpURLConnection url_con = (HttpURLConnection) url
					.openConnection();
			url_con.setRequestMethod("POST"); 
			url_con.setConnectTimeout(50000);//（单位：毫秒）jdk1.5换成这个,连接超时
			url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时

			url_con.setDoOutput(true);
			String key =  "keywordIds="+keywordIds+"&sum="+sum+"&username="+username+"&JSESSION="+JSESSION; 
			url_con.getOutputStream().write(key.getBytes("UTF-8"));   
			url_con.getOutputStream().flush(); 
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			byte[] b = new byte[8190];
			byte[] bs = new byte[0];
			int count = 0;
			while ((count = in.read(b)) > -1) {
				byte[] temp = bs;
				bs = new byte[temp.length + count];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, count);
			}
			String ret = new String(bs,"utf-8");
			in.close();
			url_con.disconnect();
			return ret;
			
		}catch (Exception e) {
			BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", e);
		}

		return "";
	}
    
    public int checkRight() {
		Result rs = new Result();
		try {
			Result rss = this.queryBol88();
			if(rss.retCode != ErrorCanst.DEFAULT_SUCCESS||rss.retVal ==null){
				return -1;
			}
			AIOBOL88Bean bean  = (AIOBOL88Bean)rss.retVal;
			
			URL url = new URL(BaseEnv.bol88URL + "/MailPool?op=right");
			HttpURLConnection url_con = (HttpURLConnection) url
					.openConnection();
			url_con.setRequestMethod("POST"); 
			url_con.setConnectTimeout(50000);//（单位：毫秒）jdk1.5换成这个,连接超时
			url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时

			url_con.setDoOutput(true);
			String key =  "username="+bean.getUserName()+"&JSESSION="+bean.getPassword(); 
			url_con.getOutputStream().write(key.getBytes("UTF-8"));   
			url_con.getOutputStream().flush(); 
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			byte[] b = new byte[8190];
			byte[] bs = new byte[0];
			int count = 0;
			while ((count = in.read(b)) > -1) {
				byte[] temp = bs;
				bs = new byte[temp.length + count];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, count);
			}
			String ret = new String(bs,"utf-8");
			in.close();
			url_con.disconnect();
			if("NOFEE".equals(ret)){
				return 1;
			}else if("OK".equals(ret)){
				return 0;
			}else if("TIMEOUT".equals(ret)){
				return 2;
			}else{
				return -1;
			}			
		}catch (Exception e) {
			BaseEnv.log.error("AIOBOL88Mgt.checkRight error :", e);
		}
		return -1;
	}
    
    public Result updateMailSet(final String mailaccount,final String title,final String content,final String attach,final String ccMail,
    		final String curKeywordName,final String curKeywordId,final int curKwMailNum,final int totalSend,final int leaveDate,final int startTime,
    		final int endTime,final int status,final String startSendDate,final String endSendDate,final int threadNum,final String sCompanyID,final String from){

    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String sql = "select * from tblMailPoolSet where sCompanyID=? and mfrom=? ";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, sCompanyID);
                            pstmt.setString(2, from);
                            ResultSet rss = pstmt.executeQuery();
                            if(rss.next()){
                            	//存在修改
                            	sql = "update tblMailPoolSet set mailaccount=?,title=?,content=?,attach=?,ccMail=?,curKeywordName=?,"+
                            		"curKeywordId=?,curKwMailNum=?,leaveDate=?,startTime=?,endTime=?,status=?,startSendDate=?,endSendDate=?,threadNum=? where sCompanyID=? and mfrom=? ";
                            }else{
                            	//不存在增加
                            	sql ="insert into tblMailPoolSet(mailaccount,title, content, attach, ccMail,"+
						    		"curKeywordName, curKeywordId, curKwMailNum, leaveDate, startTime, endTime, status, totalSend,startSendDate,endSendDate,threadNum,sCompanyID,mfrom)"+
						    		" values(?,?,?,?,?,?,?,?,?,?,?,?,0,?,?,?,?,?)";
                            }
                            pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, mailaccount);
                            pstmt.setString(2, title);
                            pstmt.setString(3, content);
                            pstmt.setString(4, attach);
                            pstmt.setString(5, ccMail);
                            pstmt.setString(6, curKeywordName);
                            pstmt.setString(7, curKeywordId);
                            pstmt.setInt(8, curKwMailNum);
                            pstmt.setInt(9, leaveDate);
                            pstmt.setInt(10, startTime);
                            pstmt.setInt(11, endTime);
                            pstmt.setInt(12, status);                            
                            pstmt.setString(13, startSendDate);
                            pstmt.setString(14, endSendDate);
                            pstmt.setInt(15, threadNum);
                            pstmt.setString(16, sCompanyID);
                            pstmt.setString(17, from);
                            pstmt.executeUpdate();
                            
                            MailPoolThread mpt = (MailPoolThread)BaseEnv.mailPoolThreadMap.remove(sCompanyID+":"+from);  
                            if(mpt != null){
                            	mpt.stopThread();
                            }
                            mpt = new MailPoolThread();
                            mpt.init(mailaccount, title, content, attach, ccMail, curKeywordId, curKwMailNum, leaveDate, startTime, endTime,startSendDate,endSendDate,threadNum, sCompanyID,from);
                            BaseEnv.mailPoolThreadMap.put(sCompanyID+":"+from, mpt);                                                        
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }
    
    public Result getMailSet(final String sCompanyID,final String from){

    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String sql = "select * from tblMailPoolSet where sCompanyID=? and mfrom=?";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, sCompanyID);
                            pstmt.setString(2, from);
                            ResultSet rss = pstmt.executeQuery();
                            if(rss.next()){
                            	MailPoolForm form  = new MailPoolForm();
                            	form.setAttach(rss.getString("Attach"));
                            	form.setCcMail(rss.getString("CcMail"));
                            	form.setContent(rss.getString("Content"));
                            	form.setCurKeywordId(rss.getString("CurKeywordId"));
                            	form.setCurKeywordName(rss.getString("CurKeywordName"));
                            	form.setCurKwMailNum(rss.getInt("CurKwMailNum"));
                            	form.setEndTime(rss.getInt("EndTime"));
                            	form.setLeaveDate(rss.getInt("LeaveDate"));
                            	form.setMailaccount(rss.getString("Mailaccount"));
                            	form.setSCompanyID(rss.getString("SCompanyID"));
                            	form.setStartTime(rss.getInt("StartTime"));
                            	form.setStatus(rss.getInt("Status"));
                            	form.setTitle(rss.getString("Title"));
                            	form.setTotalSend(rss.getInt("TotalSend"));
                            	form.setStartSendDate(rss.getString("StartSendDate"));
                            	form.setEndSendDate(rss.getString("EndSendDate"));
                            	form.setThreadNum(rss.getInt("ThreadNum"));
                            	form.setFrom(rss.getString("mFrom"));
                            	rst.retVal = form;
                            }
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }
    
    public Result stop(final String sCompanyID,final String from){

    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String sql = "update tblMailPoolSet set status = 0 where sCompanyID=? and mfrom=?";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, sCompanyID);
                            pstmt.setString(2, from);
                            pstmt.executeUpdate();
                            
                            MailPoolThread mpt = (MailPoolThread)BaseEnv.mailPoolThreadMap.remove(sCompanyID+":"+from);
                            if(mpt != null) 
                            	mpt.stopThread();
                            
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.stop Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }
    
    /**
     * 检查该邮件是否已经发送过
     * @param mails
     * @param sCompanyID
     * @return
     */
    public Result checkMailHash(final long[] mails,final String sCompanyID){

    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                        	String sw = "";
                        	for(int i=0;i<mails.length;i++){
                        		if(mails[i] != 0){
                        			sw +=",?";
                        		}
                        	}
                        	if(sw.length() == 0){
                        		return;
                        	}else{
                        		sw = sw.substring(1);
                        	}
                            Connection conn = connection;
                            String sql = "select mailHash from  tblMailPoolHash where sCompanyID=?  and mailHash in ("+sw+") ";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, sCompanyID);
                            int index = 2;
                            for(int i=0;i<mails.length;i++){
                        		if(mails[i] != 0){
                        			pstmt.setLong(index, mails[i]);
                        			index ++;
                        		}
                        	}
                            ResultSet rs =pstmt.executeQuery();
                            while(rs.next()){
                            	long m = rs.getLong(1);
                            	for(int i=0;i<mails.length;i++){
                            		if(m == mails[i]){
                            			mails[i] = 0;
                            		}
                            	}
                            }
                            
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.stop Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }
    /**
     * 保存邮件地址
     * @param mails
     * @param sCompanyID
     * @return
     */
    public Result saveMailHash(final long[] mails,final String sCompanyID,final String from){

    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                        	
                            Connection conn = connection;
                            String sql = "insert into tblMailPoolHash(mailHash,createTime,SCompanyID) values(?,?,?) ";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            
                            String createTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
                            int count = 0;
                            for(int i=0;i<mails.length;i++){
                        		if(mails[i] != 0){
                        			pstmt.setLong(1, mails[i]);
                        			pstmt.setString(2, createTime);
                        			pstmt.setString(3, sCompanyID);
                        			pstmt.addBatch();
                        			count ++;
                        		}
                        	}
                            pstmt.executeBatch();     
                            sql = "update tblMailPoolSet set totalSend = totalSend +? where SCompanyID = ? and mfrom=? ";
                            pstmt = conn.prepareStatement(sql);
                            pstmt.setInt(1,count);
                            pstmt.setString(2, sCompanyID);
                            pstmt.setString(3, from);
                            pstmt.executeUpdate();
                            
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.saveMailHash Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }
    
    public Result delOldMailHash(final int leaveDate,final String sCompanyID){

    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                        	
                            Connection conn = connection;
                            
                            String sql = "delete tblMailPoolHash where createTime < ? and SCompanyID =? ";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            
                            String createTime = BaseDateFormat.format(new Date(new Date().getTime() -leaveDate*24*60*60000),BaseDateFormat.yyyyMMdd);
                            
                            pstmt.setString(1,createTime);
                            pstmt.setString(2,sCompanyID);
                            
                            pstmt.executeUpdate();      
                            
                            System.out.println("----------删除老邮件");
                            
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.delOldMailHash Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }
    
    public Result initMailSetPool(){

    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String sql = "select * from tblMailPoolSet ";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            ResultSet rss = pstmt.executeQuery();
                            if(rss.next()){
                            	String sCompanyID = rss.getString("SCompanyID");
                            	String from  = rss.getString("mfrom");
                            	int status = rss.getInt("Status");
                            	if(status == 1){
	                            	MailPoolThread mpt = new MailPoolThread();
	                                mpt.init(rss.getString("Mailaccount"), rss.getString("Title"), rss.getString("Content"), rss.getString("Attach"), 
	                                		rss.getString("CcMail"), rss.getString("CurKeywordId"), 0, rss.getInt("LeaveDate"), 
	                                		rss.getInt("StartTime"), rss.getInt("EndTime"), rss.getString("startSendDate"), rss.getString("endSendDate"),rss.getInt("threadNum"), sCompanyID,from);
	                                BaseEnv.mailPoolThreadMap.put(sCompanyID+":"+from, mpt); 
                            	}
                            }
                        } catch (Exception ex) {
                        	BaseEnv.log.error("AIOBOL88Mgt.getKeyword Error :", ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            throw new BusinessException("");
        }
        return rst;
    }
    
}
