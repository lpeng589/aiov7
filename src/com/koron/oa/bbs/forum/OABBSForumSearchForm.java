package com.koron.oa.bbs.forum;

import com.menyi.web.util.BaseSearchForm;
/**
 * 
 * <p>Title:论坛 帖子管理</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-12
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class OABBSForumSearchForm extends BaseSearchForm{

	private static final long serialVersionUID = 1L;
	private String forumTitle ;
	private String forumContent ;
	private String userName ;
	private String beginTime ;
	private String endTime ;
	private String topicType ;
	private String seltime;
	private String orderby;
	private String elite;
	private String topic;
	private String keyword;
	
	
	
	
	public String getElite() {
		return elite;
	}
	public void setElite(String elite) {
		this.elite = elite;
	}
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	public String getSeltime() {
		return seltime;
	}
	public void setSeltime(String seltime) {
		this.seltime = seltime;
	}
	public String getForumContent() {
		return forumContent;
	}
	public void setForumContent(String forumContent) {
		this.forumContent = forumContent;
	}
	public String getTopicType() {
		return topicType;
	}
	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getForumTitle() {
		return forumTitle;
	}
	public void setForumTitle(String forumTitle) {
		this.forumTitle = forumTitle;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
