package com.koron.oa.bbs.forum;

import java.util.List;

import org.apache.struts.upload.FormFile;

import com.menyi.web.util.BaseForm;
/**
 * 
 * <p>Title:论坛 帖子管理</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-12
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class OABBSForumForm extends BaseForm{

	private static final long serialVersionUID = 1L;
	private String forumId ;
	private String topicId ;
	private String userId ;//bbsuserId
	private String topicName ;
	private String topicContent ;
	private String[] wakeUpMode ;
	private String isSaveReading ;
	private String oldattach ;
	private String topicTyle;
	private String replayNote="2";
	
	
	
	
	
	public String getReplayNote() {
		return replayNote;
	}
	public void setReplayNote(String replayNote) {
		this.replayNote = replayNote;
	}
	public String getTopicTyle() {
		return topicTyle;
	}
	public void setTopicTyle(String topicTyle) {
		this.topicTyle = topicTyle;
	}
	public String getIsSaveReading() {
		return isSaveReading;
	}
	public void setIsSaveReading(String isSaveReading) {
		this.isSaveReading = isSaveReading;
	}
	public String[] getWakeUpMode() {
		return wakeUpMode;
	}
	public void setWakeUpMode(String[] wakeUpMode) {
		this.wakeUpMode = wakeUpMode;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getForumId() {
		return forumId;
	}
	public void setForumId(String forumId) {
		this.forumId = forumId;
	}
	public String getOldattach() {
		return oldattach;
	}
	public void setOldattach(String oldattach) {
		this.oldattach = oldattach;
	}
	
}
