package com.koron.oa.bbs.forum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OABBSForumBean;
import com.koron.oa.bean.OABBSReplayForumBean;
import com.koron.oa.bean.OABBSTopicBean;
import com.koron.oa.bean.OABBSUserBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
/**
 * 
 * <p>Title:论坛 帖子管理 数据操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-12
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class OABBSForumMgt extends AIODBManager {

	/**
	 * 查询 论坛版块
	 * @param topicId
	 * @return
	 */
	public Result queryTopic(final String userId,
			final String loginId,final String deptCode) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {  
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select OABBSTopic.sortname,a.nickname,OABBSTopic.classCode,OABBSTopic.forumCount,OABBSTopic.lastForumId,OABBSTopic.id,b.nickname nickname2,OABBSTopic.BelongMenu,OABBSTopic.photo " +
									"from OABBSTopic left join OABBSUsers a on a.userId=OABBSTopic.bbsUserId " +
									"left join OABBSUsers b on b.userId=OABBSTopic.bbsUserId2 ";
							/*if(!"1".equals(loginId)){
								sql += " where OABBSTopic.bbsUserId='"+loginId+"' or OABBSTopic.bbsUserId2='"+loginId+"' or ("  
								    + "(charindex('1,',OABBSTopic.defaultScope,0)>0 and  not exists (" +
								    " select userID from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and charindex('1,',defaultScope,0)>0 and userID='"+loginId+"') " 
								    	+ " and not exists (select departmentCode from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id "
								    	+ " and len(userId)=0 and charindex('1,',defaultScope,0)>0 and '"+deptCode+"' like departmentCode+'%')) " 
								    +  " or " +
								      "(charindex('1,',OABBSTopic.defaultScope,0)>0 and ( exists (select userID from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and charindex('1,',defaultScope,0)=0 and userID='"+loginId+"') " +
								      " or exists (select departmentCode from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and len(userId)=0 and charindex('1,',defaultScope,0)=0 and '"+deptCode+"' like departmentCode+'%')))" 
									+ ") ";
							}*/
							/*wyy默认权限打钩表示全部能看到*/
							if(!"1".equals(loginId)){
								sql += " where OABBSTopic.bbsUserId='"+loginId+"' or OABBSTopic.bbsUserId2='"+loginId+"' or ("  
								    + "(charindex('1,',OABBSTopic.defaultScope,0)>0 and not exists (" +
								    " select userID from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and charindex('1,',defaultScope,0)>0 and userID='"+loginId+"') " 
								    	+ " and not exists (select departmentCode from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id "
								    	+ " and len(userId)=0 and charindex('1,',defaultScope,0)>0 and '"+deptCode+"' like departmentCode+'%')) " 
								    +  " or " +
								      "(charindex('1,',OABBSTopic.defaultScope,0)=0 and ( exists (select userID from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and charindex('1,',defaultScope,0)=0 and userID='"+loginId+"') " +
								      " or exists (select departmentCode from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and len(userId)=0 and charindex('1,',defaultScope,0)=0 and '"+deptCode+"' like departmentCode+'%')))" 
									+ ") ";						
							} 
							sql+=" order by orders";
							
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery() ;
							ArrayList<String[]> topicList = new ArrayList<String[]>() ;
							while(rss.next()){ 
								String[] topic = new String[9] ;
								topic[0] = rss.getString("sortname") ;
								topic[1] = rss.getString("nickname") ;
								topic[2] = rss.getString("classCode") ;
								topic[3] = String.valueOf(rss.getInt("forumCount")) ;
								topic[4] = rss.getString("lastForumId") ;
								topic[5] = rss.getString("id") ;
								topic[6] = rss.getString("nickname2") ;
								topic[7] = rss.getString("BelongMenu") ;
								topic[8] = rss.getString("photo");
								topicList.add(topic) ;
							}
							result.setRetVal(topicList) ;
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopics : ", ex) ;
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
	 * 查询 论坛版块分类
	 * @param topicId
	 * @return
	 */
	public Result queryTopicType(final String topicId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,BBSLabel from OABBSTopicWDet where f_ref=? order by detOrderNo";
							
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, topicId);
							ResultSet rss = pss.executeQuery() ;
							
							ArrayList<String[]> typeList = new ArrayList<String[]>() ;
							while(rss.next()){ 
								String[] type = new String[2] ;
								type[0] = rss.getString("id") ;
								type[1] = rss.getString("BBSLabel") ;
								typeList.add(type) ;
							}
							result.setRetVal(typeList) ;
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopics : ", ex) ;
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
	 * 查询 是否有该论坛版块相应的权限
	 * @param topicId
	 * @return
	 */
	public Result queryTopicScope(final String topicId,final String userId,
			final String loginId,final String deptCode,final String scope) {		
	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id from OABBSTopic where id='"+topicId+"'"  ;
							if(!"1".equals(loginId)){
								sql += " and ( OABBSTopic.bbsUserId='"
										+ loginId
										+ "' or OABBSTopic.bbsUserId2='"
										+ loginId
										+ "' or ("
										+ "(charindex('"+scope+"',OABBSTopic.defaultScope,0)>0 and  not exists ("
										+ " select userID from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and charindex('"+scope+"',defaultScope,0)>0 and userID='"
										+ loginId
										+ "') "   
										+ " and not exists (select departmentCode from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id "
										+ " and len(userId)=0 and charindex('"+scope+"',defaultScope,0)>0 and '"+deptCode+"' like departmentCode+'%'  )) "
										+ " or "
										+ "(charindex('"+scope+"',OABBSTopic.defaultScope,0)=0 and ( exists (select userID from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and charindex('"+scope+"',defaultScope,0)=0 and userID='"
										+ loginId
										+ "') "
										+ " or exists (select departmentCode from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and len(userId)=0 and charindex('"+scope+"',defaultScope,0)=0 and '"+deptCode+"' like departmentCode+'%'))))) ";
							}
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery() ;
							if(rss.next()){
								result.setRetVal(true) ;
							}else{
								result.setRetVal(false) ;
							}
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopicScope : ", ex) ;
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
	 * 查询 是否有该论坛版块相应的权限 以及模糊查询有权限模块的帖子
	 * @param topicId
	 * @return
	 */
	public Result queryTopicForum(final String userId,
			final String loginId,final String deptCode) {


		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {  
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select OABBSTopic.id " +
									"from OABBSTopic left join OABBSUsers a on a.userId=OABBSTopic.bbsUserId " +
									"left join OABBSUsers b on b.userId=OABBSTopic.bbsUserId2 ";
							if(!"1".equals(loginId)){
								sql += " where OABBSTopic.bbsUserId='"+loginId+"' or OABBSTopic.bbsUserId2='"+loginId+"' or ("  
								    + "(charindex('1,',OABBSTopic.defaultScope,0)=0 and  not exists (" +
								    " select userID from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and charindex('1,',defaultScope,0)=0 and userID='"+loginId+"') " 
								    	+ " and not exists (select departmentCode from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id "
								    	+ " and len(userId)=0 and charindex('1,',defaultScope,0)>0 and '"+deptCode+"' like departmentCode+'%')) " 
								    +  " or " +
								      "(charindex('1,',OABBSTopic.defaultScope,0)=0 and ( exists (select userID from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and charindex('1,',defaultScope,0)=0 and userID='"+loginId+"') " +
								      " or exists (select departmentCode from OABBSTopicUser where OABBSTopicUser.f_ref=OABBSTopic.id and len(userId)=0 and charindex('1,',defaultScope,0)=0 and '"+deptCode+"' like departmentCode+'%')))" 
									+ ") ";
							}
							
							sql+=" order by orders";
							
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery() ;
							ArrayList<String> topicList = new ArrayList<String>() ;
							while(rss.next()){ 
								topicList.add(rss.getString(1));
							}
							result.setRetVal(topicList) ;
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopics : ", ex) ;
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
	 * 加载 论坛版块
	 * @param topicId
	 * @return
	 */
	public Result loadTopic(String topicId){
		return loadBean(topicId, OABBSTopicBean.class) ;
	}
	
//	/**
//	 * 查询 论坛位置
//	 * @param classCode
//	 * @return
//	 */
//	public Result queryParentName(final String classCode){
//		final Result result = new Result();
//		int retCode = DBUtil.execute(new IfDB() {
//			public int exec(Session session) {
//				session.doWork(new Work() {
//					public void execute(Connection conn) throws SQLException {
//						try {
//							String sql = " select sortname,classCode,id from OABBSTopic "  
//									   + "where  '"+classCode+"' like classCode+'%' group by classCode,sortname,id order by classCode" ;
//							PreparedStatement pss = conn.prepareStatement(sql) ;
//							ResultSet rss = pss.executeQuery() ;
//							ArrayList<String[]> parentList = new ArrayList<String[]>() ;
//							while(rss.next()){
//								String[] array = new String[3] ;
//								array[0] = rss.getString("sortname") ;
//								array[1] = rss.getString("classCode") ;
//								array[2] = rss.getString("id") ;
//								parentList.add(array) ;
//							}
//							result.setRetVal(parentList) ;
//						} catch (SQLException ex) {
//							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//							ex.printStackTrace();
//							BaseEnv.log.error("OABBSForumMgt queryTopics : ", ex) ;
//							return;
//						}
//					}
//				});
//				return result.getRetCode();
//			}
//		});
//		result.retCode = retCode;
//		return result ;
//	}
	
	/**
	 * 查询某版块的所有帖子
	 * @param classCode
	 * @return
	 */
	public Result queryForum(String classCode,OABBSForumSearchForm forumForm,boolean viewOther,String userId,String hightype){
		List param = new ArrayList();
		
		String hql = "select bean from OABBSForumBean bean where 1=1" ;
		if(classCode!=null && classCode.length()>0){
			hql+=" and bean.topic.id=?";
			param.add(classCode);
		}
		
		//帖子标题
		if(forumForm.getForumTitle() != null && forumForm.getForumTitle().trim().length()>0){
			hql += " and bean.topicName like ? " ;
			param.add("%"+forumForm.getForumTitle().trim()+"%") ;
			if(classCode==null && hightype==null){
			  return list(hql, param, forumForm.getPageNo(), forumForm.getPageSize(), true) ;
			}	
		}
		//帖子内容
		if(forumForm.getForumContent() != null && forumForm.getForumContent().trim().length()>0){
			hql += " and bean.topicContent like ? " ;
			param.add("%"+forumForm.getForumContent().trim()+"%") ;
		}
		
		//帖子用户
		if(forumForm.getUserName() != null && forumForm.getUserName().trim().length()>0){
			hql += " and ( bean.bbsUser.nickName like ? or bean.bbsUser.fullName like ? )" ;
			param.add("%"+forumForm.getUserName().trim()+"%") ;
			param.add("%"+forumForm.getUserName().trim()+"%") ;
		}
		if("1".equals(forumForm.getSeltime())){
			//查近一天
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date());
			hql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else if("2".equals(forumForm.getSeltime())){
			//查近二天
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date(new Date().getTime() - 24*60*60000));
			hql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else if("3".equals(forumForm.getSeltime())){
			//查近一周
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date(new Date().getTime() - 7*24*60*60000));
			hql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else if("4".equals(forumForm.getSeltime())){
			//查近一月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date(new Date().getTime() - (long)30*24*60*60000));
			hql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else if("5".equals(forumForm.getSeltime())){
			//查近三月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date(new Date().getTime() - (long)90*24*60*60000));
			hql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else{
			if(forumForm.getBeginTime() != null && forumForm.getBeginTime().length()>0){
				hql += " and bean.createTime > ? " ;
				param.add(forumForm.getBeginTime()) ;
			}
			if(forumForm.getEndTime() != null && forumForm.getEndTime().length()>0){
				hql += " and bean.createTime < ? " ;
				param.add(forumForm.getEndTime()+" 23:59:59") ;
			}
		}
		if(forumForm.getTopicType() != null && forumForm.getTopicType().length()>0){
			hql += " and bean.topicTyle = ? " ;
			param.add(forumForm.getTopicType()) ;
		}
		if(!viewOther){
			hql += " and bean.createBy = ? " ;
			param.add(userId) ;
		}
		
		if(forumForm.getElite() != null && "true".equals(forumForm.getElite())){
			hql += " and bean.isElite = ? " ;
			param.add(1) ;
		}
		
		if("1".equals(forumForm.getOrderby())){
			hql +="order by bean.createTime desc";
		}else if("2".equals(forumForm.getOrderby())){
			hql +="order by case when len(isnull(bean.lastUpdateBy,''))=0 then 0 ELSE 1 end desc ,bean.lastUpdateTime desc";
		}else if("3".equals(forumForm.getOrderby())){
			hql +="order by bean.returnCount desc";
		}else{
			hql +="order by bean.isSetTop desc,bean.lastUpdateTime desc,bean.createTime desc";
		}
		return list(hql, param, forumForm.getPageNo(), forumForm.getPageSize(), true) ;
	}
	
	/**
	 * 查询某版块的所有帖子
	 * @param classCode
	 * @return
	 */
	public Result queryPreNextForum(boolean isPre,OABBSForumSearchForm forumForm,String sortId,OABBSForumBean forumBean,String forumId,boolean viewOther,String userId){
		List param = new ArrayList();
		String sql = "select top 1 id from oabbssends bean where bean.sortId=? and bean.Id <> ? ";
		param.add(sortId) ;		
		param.add(forumId) ;
		
		
		if(forumForm != null && forumForm.getForumTitle() != null && forumForm.getForumTitle().trim().length()>0){
			sql += " and bean.topicName like ? " ;
			param.add("%"+forumForm.getForumTitle().trim()+"%") ;
		}
		if(forumForm != null && forumForm.getForumContent() != null && forumForm.getForumContent().trim().length()>0){
			sql += " and bean.topicContent like ? " ;
			param.add("%"+forumForm.getForumContent().trim()+"%") ;
		}
		if(forumForm != null && forumForm.getUserName() != null && forumForm.getUserName().trim().length()>0){
			sql += " and ( bean.bbsUser.nickName like ? or bean.bbsUser.fullName like ? )" ;
			param.add("%"+forumForm.getUserName().trim()+"%") ;
			param.add("%"+forumForm.getUserName().trim()+"%") ;
		}
		if(forumForm != null && "1".equals(forumForm.getSeltime())){
			//查近一天
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date());
			sql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else if(forumForm != null && "2".equals(forumForm.getSeltime())){
			//查近二天
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date(new Date().getTime() - 24*60*60000));
			sql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else if(forumForm != null && "3".equals(forumForm.getSeltime())){
			//查近一周
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date(new Date().getTime() - 7*24*60*60000));
			sql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else if(forumForm != null && "4".equals(forumForm.getSeltime())){
			//查近一月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date(new Date().getTime() - (long)30*24*60*60000));
			sql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else if(forumForm != null && "5".equals(forumForm.getSeltime())){
			//查近三月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date =sdf.format(new Date(new Date().getTime() - (long)90*24*60*60000));
			sql += " and bean.createTime > ? " ;
			param.add(date+" 00:00:00") ;
		}else{
			if(forumForm != null && forumForm.getBeginTime() != null && forumForm.getBeginTime().length()>0){
				sql += " and bean.createTime > ? " ;
				param.add(forumForm.getBeginTime()) ;
			}
			if(forumForm != null && forumForm.getEndTime() != null && forumForm.getEndTime().length()>0){
				sql += " and bean.createTime < ? " ;
				param.add(forumForm.getEndTime()+" 23:59:59") ;
			}
		}
		if(forumForm != null && forumForm.getTopicType() != null && forumForm.getTopicType().length()>0){
			sql += " and bean.topicTyle = ? " ;
			param.add(forumForm.getTopicType()) ;
		}
		if(!viewOther){
			sql += " and bean.createBy = ? " ;
			param.add(userId) ;
		}
		
		if(forumForm != null && forumForm.getElite() != null && "true".equals(forumForm.getElite())){
			sql += " and bean.isElite = ? " ;
			param.add(1) ;
		}
		
		if(isPre){			
			if(forumForm != null && "1".equals(forumForm.getOrderby())){
				sql +=" and bean.createTime <=?";			
				param.add(forumBean.getCreateTime()) ;
				sql +=" order by bean.createTime desc";
			}else if(forumForm != null && "2".equals(forumForm.getOrderby())){
				if(forumBean.getLastUpdateBy()!=null){
					sql +=" and (bean.lastUpdateTime <=? or len(isnull(lastUpdateBy,''))=0 ) order by case when len(isnull(bean.lastUpdateBy,''))=0 then 0 ELSE 1 end desc ,bean.lastUpdateTime desc ";
				 }else{
					sql +=" and (bean.lastUpdateTime <=? and len(isnull(lastUpdateBy,''))=0 ) order by case when len(isnull(bean.lastUpdateBy,''))=0 then 0 ELSE 1 end desc ,bean.lastUpdateTime desc";
				 }
				param.add(forumBean.getLastUpdateTime()) ;
			
	
			}else if(forumForm != null && "3".equals(forumForm.getOrderby())){
				sql +=" and bean.returnCount <=?";			
				param.add(forumBean.getReturnCount()) ;
				sql +=" order by bean.returnCount desc";
			}else{
				if(forumBean.getIsSetTop()>0){
				sql +="AND ((bean.isSetTop=1 AND bean.lastUpdateTime<=?) OR (bean.isSetTop!=1 ))order by bean.isSetTop desc,bean.lastUpdateTime desc,bean.createTime DESC";
				}else{
				sql+="AND (bean.issettop!=1 AND bean.lastUpdateTime<=?) order by bean.isSetTop desc,bean.lastUpdateTime desc,bean.createTime DESC ";	
				}
				param.add(forumBean.getLastUpdateTime()) ;
				 
			}
		}else{
			if(forumForm != null && "1".equals(forumForm.getOrderby())){
				sql +=" and bean.createTime >=?";			
				param.add(forumBean.getCreateTime()) ;
				sql +=" order by bean.createTime ";
			}else if(forumForm != null && "2".equals(forumForm.getOrderby())){
				if(forumBean.getLastUpdateBy()!=null){
					sql +=" and lastupdatetime>=? AND len(isnull(bean.lastUpdateBy,''))>0  order by case when len(isnull(bean.lastUpdateBy,''))=0 then 0 ELSE 1 end ,bean.lastUpdateTime ";
				 }else{
					sql +=" AND (len(isnull(bean.lastUpdateBy,''))>0 or (len(isnull(bean.lastUpdateBy,''))=0 AND lastUpdateTime>=?)) order by case when len(isnull(bean.lastUpdateBy,''))=0 then 0 ELSE 1 end ,bean.lastUpdateTime  ";	
				}	
				param.add(forumBean.getLastUpdateTime()) ;
				
			}else if(forumForm != null && "3".equals(forumForm.getOrderby())){
				sql +=" and bean.returnCount >=?";			
				param.add(forumBean.getReturnCount()) ;
				sql +=" order by bean.returnCount ";
			}else{
				if(forumBean.getIsSetTop()>0){
				sql +=" AND  (bean.isSetTop=1 and bean.lastupdateTime>=?) order by bean.isSetTop ,bean.lastUpdateTime ,bean.createTime ";	
				}else{
				sql +=" AND ((bean.isSetTop=1)OR( bean.isSetTop!=1 AND bean.lastUpdateTime>=?)) order by bean.isSetTop ,bean.lastUpdateTime ,bean.createTime ";
				}
				param.add(forumBean.getLastUpdateTime()) ;
				 
			}
		}
		
		return sqlList(sql, param) ;
	}
	
	/**
	 * 查询某帖子的所有回帖
	 * @param classCode
	 * @return
	 */
	public Result queryReplayForum(String forumId,int pageSize,int pageNo,String auth,String order){
		List<String> param = new ArrayList<String>();
		String hql = "select bean from OABBSReplayForumBean bean where bean.sendId=? ";
		param.add(forumId) ;
		
		if(auth != null && auth.trim().length() > 0 ){
			hql += " and bean.bbsUser.id=? ";
			param.add(auth) ;
		}
		
		if("desc".equals(order)){
			hql += " order by bean.createTime desc";
		}else{
			hql +=" order by bean.createTime asc";
		}
		
		return list(hql, param, pageNo, pageSize, true) ;
	}
	
	/**
	 * @author 陶智申
	 */
	public Result queryAllPhoto (final String forumId){
		final List<EmployeeBean> lis = new ArrayList();	
		final HashMap<String, EmployeeBean> hamap = new HashMap<String, EmployeeBean>();
		final Result result = new Result();
 		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,EmpFullName,sysName,photo from tblEmployee where id in ("
									+ "select userID from OABBSUsers where id in ("
									+ "select bbsRUserID from OABBSReplays where sendID=" + "'" + forumId + "'" + "))";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rss = pss.executeQuery() ;
							EmployeeBean empBean = null;
							while(rss.next()){
								empBean = new EmployeeBean();
								empBean.setId(rss.getString("id"));
								empBean.setEmpFullName(rss.getString("EmpFullName"));
								empBean.setSysName(rss.getString("sysName"));
								String str = rss.getString("photo");
								if (str != null && str != "" && str.indexOf(":") > 0) {
									str = str.substring(0, str.indexOf(":"));
								}
								if (str==null){
									str="";
								}
								empBean.setPhoto(str);
								hamap.put(empBean.getId(), empBean);
//								lis.add(empBean);
							}
							result.setRetVal(hamap);
						} catch (SQLException ex) {
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryAllPhoto : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
				}
			});
			result.retCode = retCode;
			return result ;
		}
	
	/**
	 * 添加帖子
	 * @param forumBean
	 * @return
	 */
	public Result addForum(OABBSForumBean forumBean){
		Result result = addBean(forumBean) ;
		/*更新 版块的最后一条帖子以及 帖子数量*/
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			result = updateTopicInfo(forumBean.getTopic().getId(),forumBean.getId(), forumBean.getBbsUser().getId(),"add") ;
		}
		return result ;
	}
	
	/**
	 * 搬移帖子
	 * @param forumBean
	 * @return
	 */
	public Result moveForum(final String[] forumIds,final String topicId){
		final Result result = new Result() ;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement() ;
							String sql = "" ;
							String strForumIds = "" ;
							for(String forumId : forumIds){
								strForumIds += "'"+forumId+"'," ;
								sql = "update OABBSTopic set forumCount=forumCount-1 where id=(select sortId from OABBSSends where id='"+forumId+"')" ;
								state.addBatch(sql) ;
								sql = "update tblattention set url='/OABBSForumAction.do?operation=70&forumId="+forumId+"&topicId="+topicId+"' where OTopicId='"+forumId+"'";
								state.addBatch(sql);
							}
							if(strForumIds.length()>0){
								strForumIds = strForumIds.substring(0,strForumIds.length()-1) ;
							}
							sql = "update OABBSSends set sortId='"+topicId+"',topicTyle = NULL where id in ("+strForumIds+")" ;
							state.addBatch(sql) ;
							sql = "update OABBSTopic set forumCount=forumCount+"+forumIds.length+", " 
								+ "lastForumId='"+forumIds[forumIds.length-1]+"' where id='"+topicId+"'" ;
							state.addBatch(sql) ;
							state.executeBatch() ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt moveForum : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result ;
	}
	
	/**
	 * 更新 版块帖子数，最后发表帖子，添加用户积分
	 * @param classCode
	 * @return
	 */
	public Result updateTopicInfo(final String topicId ,final String forumId,
			final String userId,final String type){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							if("add".equals(type)){
								String sql = "update OABBSTopic set forumCount=forumCount+1,lastForumId=? " +
										"where id=? " ;
								PreparedStatement pss = conn.prepareStatement(sql) ;
								pss.setString(1, forumId) ;
								pss.setString(2, topicId) ;
								pss.executeUpdate() ;
							}else{
								String sql = "update OABBSTopic set lastForumId=? " +
								"where id=? " ;
								PreparedStatement pss = conn.prepareStatement(sql) ;
								pss.setString(1, forumId) ;
								pss.setString(2, topicId) ;
								pss.executeUpdate() ;
							}
							
							String sql = "" ;
							if("add".equals(type)){
								sql = "update OABBSUsers set userscore=userscore+(select top 1 addNewTopic from OABBSSetting) where id=?" ;
							}else{
								sql = "update OABBSUsers set userscore=userscore+(select top 1 addResponseTopic from OABBSSetting) where id=?" ;
							}
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId) ;
							pss.executeUpdate() ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt updateTopicInfo : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result ;
	}
	
	/**
	 * 加载帖子
	 * @param forumId
	 * @return
	 */
	public Result loadForum(String forumId){
		return loadBean(forumId, OABBSForumBean.class) ;
	}
	
	/**
	 * 修改帖子
	 * @param forumBean
	 * @return
	 */
	public Result updateForum(OABBSForumBean forumBean){
		return updateBean(forumBean) ;
	}
	
	/**
	 * 添加 论坛用户
	 * @param userBean
	 * @return
	 */
	public Result addUser(OABBSUserBean userBean){
		return addBean(userBean) ;
	}
	/**
	 * 置顶，加精华
	 * @param forumBean
	 * @return
	 */
	public Result updateForum(OABBSForumBean forumBean,String updateType,String paramValue){
		Result result = updateBean(forumBean) ;
		/*更新 用户积分*/
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			result = addTopEliteScope(updateType, paramValue, forumBean.getBbsUser().getId()) ;
		}
		return result ;
	}
	
	/**
	 * 置顶，加精华 更新用户积分
	 * @param classCode
	 * @return
	 */
	public Result addTopEliteScope(final String updateType,final String paramValue,final String userId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "" ;
							if("setTop".equals(updateType)){
								if("1".equals(paramValue)){
									sql = "update OABBSUsers set userscore=userscore+(select top 1 addTopTopic from OABBSSetting) where id=?" ;
								}else{
									sql = "update OABBSUsers set userscore=userscore-(select top 1 addTopTopic from OABBSSetting) where id=?" ;
								}
							}else{
								if("1".equals(paramValue)){
									sql = "update OABBSUsers set userscore=userscore+(select top 1 addMainTopic from OABBSSetting) where id=?" ;
								}else{
									sql = "update OABBSUsers set userscore=userscore-(select top 1 addMainTopic from OABBSSetting) where id=?" ;
								}
							}
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId) ;
							pss.executeUpdate() ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt addTopEliteScope : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result ;
	}
	
	/**
	 * 回复 帖子
	 * @param forumBean
	 * @return
	 */
	public Result replayForum(OABBSReplayForumBean replayBean,String topicId,String[] message,String replayNote) {
		Result result = addBean(replayBean) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/*更新回帖数*/
			OABBSForumBean forumBean = (OABBSForumBean) loadBean(replayBean.getSendId(), OABBSForumBean.class).retVal ;
			forumBean.setReturnCount(forumBean.getReturnCount()+1) ;
			forumBean.setLastUpdateBy(replayBean.getBbsUser());
			forumBean.setLastUpdateTime(replayBean.getCreateTime());
			result = updateBean(forumBean) ;
			/*更新用户积分*/
			result = updateTopicInfo(topicId, replayBean.getSendId(), replayBean.getBbsUser().getId(), "replay") ;
			
			if("1".equals(forumBean.getReplayNote())|| "adviceTrue".equals(replayNote)){
				//消息通知发帖人				
				String title = message[0]+"\""+(forumBean.getTopicName().length() > 50?forumBean.getTopicName().substring(0,50):forumBean.getTopicName())+"\""+message[1];
				String msg=title;
				NotifyFashion notify = new NotifyFashion(forumBean.getCreateBy(),msg,"<a href=\"javascript:mdiwin('/OABBSForumAction.do?operation=70&forumId="+forumBean.getId()+"&topicId="+forumBean.getTopic().getId()+"','RES<oa.bbs.innerBBS>')\">"+title+"</a>",forumBean.getCreateBy(),4,"OA","") ; 
				notify.sendAdvice(forumBean.getCreateBy(),msg,"<a href=\"javascript:mdiwin('/OABBSForumAction.do?operation=70&forumId="+forumBean.getId()+"&topicId="+forumBean.getTopic().getId()+"','RES<oa.bbs.innerBBS>')\">"+title+"</a>",forumBean.getCreateBy(),"","other");

			}
		}
		return result ;
	}
	
	/**
	 * 加载 用户信息
	 * @param userId
	 * @return
	 */
	public Result loadUser(String userId){
		return loadBean(userId, OABBSUserBean.class) ;
	}
	
	/**
	 * 修改 用户信息
	 * @param userId
	 * @return
	 */
	public Result updateUser(OABBSUserBean userBean){
		return updateBean(userBean) ;
	}
	
	/**
	 * 加载论坛 用户信息
	 * @param userId
	 * @return
	 */
	public Result loadBBSUser(String userId){
		List<String> param = new ArrayList<String>();
		String hql = "from OABBSUserBean bean where bean.userID=?";
		param.add(userId) ;
		return list(hql, param, 0, 1, true) ;
	}
	
	/**
	 * 查询某个版块 最后一条帖子
	 * @param forumIds
	 * @return
	 */
	public Result queryTodayForumCount(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select sortID,count(s.sortID) as num from OABBSSends s where convert(varchar(10),createTime,120)=convert(varchar(10),getdate(),120) group by s.sortID";
							
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery() ;
							HashMap<String, Integer> countForum = new HashMap<String, Integer>() ;
							while(rss.next()){ 
								countForum.put(rss.getString("sortID"),Integer.parseInt(rss.getString("num")));
							}
							result.setRetVal(countForum) ;
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTodayForumCount : ", ex) ;
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
	 * 查询某个版块 最后一条帖子
	 * @param forumIds
	 * @return
	 */
	public Result queryForums(String forumIds){
		String hql = "from OABBSForumBean bean where bean.id in ("+forumIds+")" ;
		Result result = list(hql, null) ;
		HashMap<String, OABBSForumBean> mapForum = new HashMap<String, OABBSForumBean>() ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List<OABBSForumBean> forumList = (List<OABBSForumBean>) result.retVal ;
			for(OABBSForumBean forum : forumList){
				mapForum.put(forum.getId(), forum) ;
			}
		}
		result.setRetVal(mapForum) ;
		return result ;
	}
	
	/**
	 * 查询今日新帖
	 * @param forumIds
	 * @return
	 */
	public Result queryDayForums(String forumIds,String timeFlag){
		ArrayList param = new ArrayList() ;
		Date time = new Date(); 
		String hql = "from OABBSForumBean bean where bean.topic.id in ("+forumIds+")";
		if("one".equals(timeFlag)){
			hql +=" and bean.createTime>=?";
			param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+" 00:00:00") ;
		}else if("three".equals(timeFlag)){
			Calendar cal=Calendar.getInstance(); 
			cal.add(Calendar.DATE, -2);
			String fTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 00:00:00";
			hql +=" and bean.createTime>='"+fTime+"' and bean.createTime<=?";
			param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+" 23:59:59") ;
		}else if("zhou".equals(timeFlag)){
			Calendar cal=Calendar.getInstance(); 			
			int weekno=cal.get(Calendar.WEEK_OF_YEAR); 
			cal.set(Calendar.DAY_OF_WEEK, weekno); 
			String eTime = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 23:59:59";
			hql +=" and bean.createTime>=? and bean.createTime<='"+eTime+"'";
			cal.add(Calendar.DATE, -6);
			param.add(BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 00:00:00") ;
		}else if("month".equals(timeFlag)){	
			int day = time.getDate();
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.DATE,-day+1);
			String fTime = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 00:00:00";
			cal.add(Calendar.DATE,30);
			String eTime = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 23:59:59";						
			hql +=" and '1'=? and bean.createTime>='"+fTime+"' and bean.createTime<='"+eTime+"'";
			param.add("1") ;
		}				
		return list(hql, param) ;
	}
	
	/**
	 * 删除 帖子
	 * @param forumId
	 * @return
	 */
	public Result deleteForum(final String[] forumId,final String topicId){		
		
		final Result result = new Result() ;
		final ArrayList attachFiles = new ArrayList();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							/*扣除用户积分*/
							Result rs = updateUserScopeConn(forumId,topicId,conn);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt updateUserScope : ", ex) ;
							return;
						}
					}
				});
				ArrayList param = new ArrayList();
				for(String forId:forumId){
					param.clear();
					param.add(forId);
					Result rs = list("select bean from OABBSReplayForumBean bean where bean.sendId=? ", param, session);
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						ArrayList<OABBSReplayForumBean> bList = (ArrayList<OABBSReplayForumBean>)rs.retVal;
						for(OABBSReplayForumBean delBean:bList){
							if(delBean.getAttachment() != null && delBean.getAttachment().length() > 0){
								//记录要删除的附件
								attachFiles.add(delBean.getAttachment());
							}
							Result temp =deleteBean(delBean.getId(), OABBSReplayForumBean.class, "id", session);
							if(temp.retCode != ErrorCanst.DEFAULT_SUCCESS){
								return temp.retCode;
							}
						}
					}
				}
				for(String forId:forumId){
					param.clear();
					param.add(forId);
					Result rs = list("select bean from OABBSForumBean bean where bean.id=? ", param, session);
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						ArrayList<OABBSForumBean> bList = (ArrayList<OABBSForumBean>)rs.retVal;
						for(OABBSForumBean delBean:bList){
							if(delBean.getAttachment() != null && delBean.getAttachment().length() > 0){
								//记录要删除的附件
								attachFiles.add(delBean.getAttachment());
							}
							Result temp =deleteBean(delBean.getId(), OABBSForumBean.class, "id", session);
							if(temp.retCode != ErrorCanst.DEFAULT_SUCCESS){
								return temp.retCode;
							}
						}
					}
				}
				
				
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		result.retVal = 	attachFiles;
		return result ;
	}
	
	/**
	 * 删除 回帖
	 * @param forumId
	 * @return
	 */
	public Result deleteReplay(String replayId){
		return deleteBean(replayId, OABBSReplayForumBean.class, "id") ;
	}
	
	public Result loadPeplay(String replayId){
		return this.loadBean(replayId, OABBSReplayForumBean.class);
	}

	/**
	 * 删除帖子 减小发贴人积分 减小版块的帖子数
	 * @param classCode
	 * @return
	 */
	public Result updateUserScope(final String[] forumIds,final String topicId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						updateUserScopeConn(forumIds,topicId,conn);
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result ;
	}	
	
	/**
	 * 删除帖子 减小发贴人积分 减小版块的帖子数
	 * @param classCode
	 * @return
	 */
	public Result updateUserScopeConn(final String[] forumIds,final String topicId,final Connection conn){
		final Result result = new Result();

		try {
			String strForumId = "" ;
			for(String forumId : forumIds){
				strForumId += "'"+forumId+"'," ;
			}
			if(strForumId.length()>0){
				strForumId = strForumId.substring(0,strForumId.length()-1) ;
				String sql = "select bbsUserID,sortId from OABBSSends where id in ("+strForumId+")" ;
				PreparedStatement pss = conn.prepareStatement(sql) ;
				ResultSet rss = pss.executeQuery() ;
				sql = "" ;
				Statement state = conn.createStatement() ;
				while(rss.next()){
					sql += "update OABBSUsers set userscore=userscore-(select top 1 delScoreTopic from OABBSSetting)" +
							" where id='"+rss.getString("bbsUserID")+"'" ;
					state.addBatch(sql) ;
					//sql += "update OABBSTopic set forumCount=forumCount-1 where id='"+rss.getString("sortId")+"'" ;
					//state.addBatch(sql) ;
				}
				sql = "update OABBSTopic set forumCount=forumCount-"+forumIds.length+" where id='"+topicId+"'" ;
				state.addBatch(sql) ;
				state.executeBatch() ;
			}
		} catch (Exception ex) {
			result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("OABBSForumMgt updateUserScope : ", ex) ;
		}
		return result ;
	}
	
	/**
	 * 查询 积分等级
	 * @param classCode
	 * @return
	 */
	public Result queryBBSGrade(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select gradeName,GradeScopeS,GradeScopeE from OABBSGrade" ;
							Statement state = conn.createStatement() ;
							ResultSet rss = state.executeQuery(sql) ;
							ArrayList<String[]> gradeList = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] grade = new String[3] ;
								grade[0] = rss.getString("gradeName") ;
								grade[1] = rss.getString("GradeScopeS") ;
								grade[2] = rss.getString("GradeScopeE") ;
								gradeList.add(grade) ;
							}
							result.setRetVal(gradeList) ;
						} catch (Exception ex) {
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
		return result ;
	}
}
