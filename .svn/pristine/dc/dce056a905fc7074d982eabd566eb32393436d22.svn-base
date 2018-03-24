package com.koron.oa.bbs.vote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bbs.forum.OABBSForumForm;
import com.koron.oa.bbs.forum.OABBSForumMgt;
import com.koron.oa.bean.OABBSForumBean;
import com.koron.oa.bean.OABBSVoteAnswerBean;
import com.koron.oa.bean.OABBSVoteBean;
import com.koron.oa.bean.OABBSVoteUserBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;

/**
 * 
 * <p>Title:投票管理 数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-18
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class OABBSVoteMgt extends DBManager{

	/**
	 * 添加投票
	 * @param voteBean
	 * @return
	 */
	public Result addVote(final OABBSVoteBean voteBean,final OABBSForumBean forumBean){
		final Result result = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	try{
            		addBean(forumBean, session) ;
                	addBean(voteBean,session) ;
            	}catch (Exception ex) {
            		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
            		BaseEnv.log.error("OABBSVoteMgt addVote method", ex) ;
            		ex.printStackTrace() ;
				}
            	return result.getRetCode();
            }
        });
        result.setRetCode(retCode);
        
        if(retCode == ErrorCanst.DEFAULT_SUCCESS){
        	/*更新 版块的最后一条帖子以及 帖子数量*/
    		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			new OABBSForumMgt(). updateTopicInfo(forumBean.getTopic().getId(),forumBean.getId(), forumBean.getBbsUser().getId(),"add") ;
    		}
        }
        
        return result;
	}
	
	
	/**
	 * 修改投票 查询voteId
	 * 
	 */
	public Result getVoteId(String forumId){
		String hql="select bean from OABBSForumBean bean where bean.id=?";
		ArrayList param=new ArrayList();
		param.add(forumId);
		return this.list(hql, param);
	}
	
	 
	
	public Result updateVote(final OABBSVoteBean vote,final OABBSForumBean forum){
			 
			final Result result = new Result();
	        int retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	            	try{
	            		session.doWork(new Work() {
	    					public void execute(Connection conn) throws SQLException {
								String sql = "DELETE FROM OABBSVoteAnswer WHERE voteId=?";
								PreparedStatement pss = conn.prepareStatement(sql);
								pss.setString(1, vote.getId());
								pss.executeUpdate() ;
	    					}
	    				});
            		updateBean(forum, session);
            		updateBean(vote,session);
                	
            	}catch (Exception ex) {
            		result.setRetCode(ErrorCanst.DEFAULT_FAILURE ) ;
            		BaseEnv.log.error("OABBSVoteMgt addVote method", ex) ;
            		ex.printStackTrace() ;
				}
            	return result.getRetCode();
            }
        });
		
		result.setRetCode(retCode);
		return result;
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
	 * 加载 投票信息
	 * @param voteId
	 * @return
	 */
	public Result loadVote(String voteId){
		return loadBean(voteId, OABBSVoteBean.class) ;
	}
	
	public Result loadVoteUser(String voteId){
		String hql = "select bean from OABBSVoteUserBean bean where bean.voteId = ? ";
		ArrayList param = new ArrayList();
		param.add(voteId);
		return this.list(hql, param) ;
	}
	
	/**
	 * 加载 投票选项
	 * @param answerId
	 * @return
	 */
	public Result loadVoteAnswer(String answerId) {
		return loadBean(answerId, OABBSVoteAnswerBean.class) ;
	}
	
	/**
	 * 修改 投票选项
	 * @param answerBean
	 * @return
	 */
	public Result updateVoteAnswer(OABBSVoteAnswerBean answerBean) {
		return updateBean(answerBean) ;
	}

	public Result addVoteUser(OABBSVoteUserBean answerBean) {
		return addBean(answerBean) ;
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
}
