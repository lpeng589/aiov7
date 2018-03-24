package com.koron.oa.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * <p>Title:论坛帖子</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-13
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
@Entity
@Table(name="OABBSSends")
public class OABBSForumBean {
	@Id
	private String id ;			/*帖子Id*/
	private String topicName ;	/*标题*/
	
	@ManyToOne
	@JoinColumn(name="sortId",unique=true,referencedColumnName="id")
	private OABBSTopicBean topic ;/*版块*/
	
	@ManyToOne  //(cascade=CascadeType.ALL)
	@JoinColumn(name="bbsUserID",unique=true,referencedColumnName="id")
	private OABBSUserBean bbsUser ;	/*发帖人*/
	private int isSetTop = 0 ;	/*是否置顶*/
	private int isElite = 0 ;	/*加精华*/
	private int pointCount = 0 ;	/*点击数*/
	private int returnCount = 0 ;	/*回帖数*/
	private String topicContent ;	/*帖子内容*/
	private String attachment = "";		/*附件*/
	private String createBy ;
	private String createTime ;
	private String forumType ;		/*帖子类型*/
	private String voteId ;			/*投票ID*/
	private String topicTyle;
	@ManyToOne //(cascade=CascadeType.ALL)
	@JoinColumn(name="lastUpdateBy",unique=true,referencedColumnName="id")
	private OABBSUserBean lastUpdateBy ;
	private String lastUpdateTime ;
	private String replayNote;
	 @Transient
	private List<OABBSReplayForumBean> replayList ;
	
	public String getReplayNote() {
		return replayNote;
	}
	public void setReplayNote(String replayNote) {
		this.replayNote = replayNote;
	}
	public OABBSUserBean getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(OABBSUserBean lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getTopicTyle() {
		return topicTyle;
	}
	public void setTopicTyle(String topicTyle) {
		this.topicTyle = topicTyle;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	public OABBSUserBean getBbsUser() {
		return bbsUser;
	}
	public void setBbsUser(OABBSUserBean bbsUser) {
		this.bbsUser = bbsUser;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getIsElite() {
		return isElite;
	}
	public void setIsElite(int isElite) {
		this.isElite = isElite;
	}
	public int getIsSetTop() {
		return isSetTop;
	}
	public void setIsSetTop(int isSetTop) {
		this.isSetTop = isSetTop;
	}
	public int getPointCount() {
		return pointCount;
	}
	public void setPointCount(int pointCount) {
		this.pointCount = pointCount;
	}
	public String getTopicContent() {
		return topicContent;
	}
	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public OABBSTopicBean getTopic() {
		return topic;
	}
	public void setTopic(OABBSTopicBean topic) {
		this.topic = topic;
	}
	public int getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}
	public String getForumType() {
		return forumType;
	}
	public void setForumType(String forumType) {
		this.forumType = forumType;
	}
	public String getVoteId() {
		return voteId;
	}
	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}
	public List<OABBSReplayForumBean> getReplayList() {
		return replayList;
	}
	public void setReplayList(List<OABBSReplayForumBean> replayList) {
		this.replayList = replayList;
	}
	
}
