package com.koron.oa.util;

import com.dbfactory.hibernate.DBManager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

import java.sql.ResultSet;
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
 */
public class AttentionMgt extends DBManager {
	
	/**
	 * 查看关注人数
	 * 1,OABBSTopic
	 * 2.OABBSSends
	 * @param topicId
	 * @return
	 */
	public Result queryAttention(final String topicId,final String type){
		final Result result = new Result();
		final ArrayList<String[]> gradeList = new ArrayList<String[]>() ;
		int retCode =DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
						String sql ="select empId,id,title from tblAttention where OTopicId=? and type=?";
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.setString(1, topicId);
						pss.setString(2, type);
						ResultSet rss = pss.executeQuery() ;
						while(rss.next()){
							String[] grade = new String[]{rss.getString("empId"),rss.getString("id"),rss.getString("title")} ;
							gradeList.add(grade) ;
						}
						result.setRetVal(gradeList);
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryBBSGrade : ", ex) ;
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
	 * 增加关注人
	 * @param empId
	 * @param forumId
	 * @return
	 */
	public  Result addAttention(final String empId,final String oTopicId,final String type,final String title,final String url,final String typeName){
		final Result result = new Result();
		int retCode =DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{							
							
							String sql = "insert into tblAttention (id,empId,oTopicId,type,createBy,title,url,createTime,lastUpdateTime) values (?,?,?,?,?,?,?,?,?)";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, IDGenerater.getId());
							pss.setString(2, empId);
							pss.setString(3, oTopicId);
							pss.setString(4, type);
							pss.setString(5, empId);
							pss.setString(6, typeName+":"+title);
							pss.setString(7, url);
							pss.setString(8, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
							pss.setString(9, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
							pss.executeUpdate();
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryBBSGrade : ", ex) ;
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
	 * 刷新关注
	 * @param empId
	 * @param oTopicId
	 * @param type
	 * @return
	 */
	public  Result refreshAttention(final String ids){
		final Result result = new Result();
		int retCode =DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							
							
							String sql = "update tblAttention set lastUpdateTime=? where id in("+ids+") ";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
							pss.executeUpdate();
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryBBSGrade : ", ex) ;
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
	 * 取消关注
	 * * 1,OABBSTopic
	 * 2.OABBSSends
	 * @param empId
	 * @param forumId
	 * @return
	 */
	public Result deleteAttention(final String oTopicId,final String empId,final String type){
		final Result result = new Result();
		int retCode =DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql="delete from tblAttention where empId=? and OTopicId=? and type=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, empId);
							pss.setString(2, oTopicId);
							pss.setString(3, type);
							pss.executeUpdate();
						}catch(Exception ex){
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								ex.printStackTrace();
								BaseEnv.log.error("OABBSForumMgt queryBBSGrade : ", ex) ;
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
	public Result deleteAttention(final String id){
		final Result result = new Result();
		int retCode =DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql="delete from tblAttention where id=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, id);
							pss.executeUpdate();
						}catch(Exception ex){
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								ex.printStackTrace();
								BaseEnv.log.error("OABBSForumMgt queryBBSGrade : ", ex) ;
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
	 * 查询是否关有过注过
	 *  * * 1,OABBSTopic
	 * 2.OABBSSends
	 * @param empId
	 * @param forumId
	 * @return
	 */
	public Result isAttention(final String empId,final String oTopicId,final String type){
		final Result result = new Result();
		int retCode =DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							
							String sql ="select id from tblAttention where empId=? and OTopicId=? and type=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, empId);
							pss.setString(2, oTopicId);
							pss.setString(3, type);
							ResultSet rss = pss.executeQuery() ;
							if(rss.next()){
								result.setRetCode(ErrorCanst.MULTI_VALUE_ERROR);
							}
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryBBSGrade : ", ex) ;
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
	 * 查询是否关有过注过
	 *  * * 1,OABBSTopic
	 * 2.OABBSSends
	 * @param empId
	 * @param forumId
	 * @return
	 */
	public Result attentionMap(final String empId,final List clientList,final String type){
		final Result result = new Result();
		int retCode =DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String condition = "" ;
							for(int i=0;i<clientList.size();i++){
								HashMap clientMap = (HashMap) clientList.get(i) ;
								condition += "'"+clientMap.get("id")+"'," ;
							}
							if(condition.trim().length()>0){
								condition = condition.substring(0,condition.length()-1) ;
							}
							String sql ="select OTopicId from tblAttention where empId=? and OTopicId in ("+condition+") and type=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, empId);
							pss.setString(2, type);
							ResultSet rss = pss.executeQuery() ;
							HashMap<String, Boolean> attentMap = new HashMap<String, Boolean>() ;
							while(rss.next()){
								attentMap.put(rss.getString("OTopicId"), true) ;
							}
							result.setRetVal(attentMap) ;
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("AttentionMgt attentionMap : ", ex) ;
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
	 * 查询客户的地址
	 * @param keyIds
	 * @return
	 */
    public Result getClientAddress(final String keyId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "select Address from CRMClientInfo where id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, keyId);
							ResultSet rss = pss.executeQuery();
							if(rss.next()){
								rst.setRetVal(rss.getString("Address"));
							}
						}catch (Exception ex) {
							BaseEnv.log.error("ClientMgt getClientAddress mehtod:", ex) ;
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
}
