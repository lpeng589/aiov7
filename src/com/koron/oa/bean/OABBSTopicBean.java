package com.koron.oa.bean;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:版块</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-14
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
@Entity
@Table(name="OABBSTopic")
public class OABBSTopicBean {

	@Id
	private String id ;
	private String sortName ;
	private String bbsUserId ;
	private String bbsUserId2 ;
	private String classCode ;
	private String forumCount ;
	private String lastForumId ;
	private String isRealname;
	private int hotClick ;
	private int newDay ;
	private String defaultScope;
	
	
	public String getIsRealname() {
		return isRealname;
	}
	public void setIsRealname(String isRealname) {
		this.isRealname = isRealname;
	}
	public String getBbsUserId2() {
		return bbsUserId2;
	}
	public void setBbsUserId2(String bbsUserId2) {
		this.bbsUserId2 = bbsUserId2;
	}
	public String getBbsUserId() {
		return bbsUserId;
	}
	public void setBbsUserId(String bbsUserId) {
		this.bbsUserId = bbsUserId;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getForumCount() {
		return forumCount;
	}
	public void setForumCount(String forumCount) {
		this.forumCount = forumCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLastForumId() {
		return lastForumId;
	}
	public void setLastForumId(String lastForumId) {
		this.lastForumId = lastForumId;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public int getHotClick() {
		return hotClick;
	}
	public void setHotClick(int hotClick) {
		this.hotClick = hotClick;
	}
	public int getNewDay() {
		return newDay;
	}
	public void setNewDay(int newDay) {
		this.newDay = newDay;
	}
	public String getDefaultScope() {
		return defaultScope;
	}
	public void setDefaultScope(String defaultScope) {
		this.defaultScope = defaultScope;
	}
	
}
