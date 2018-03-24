package com.koron.oa.bean;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * <p>Title:回复帖子</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-13
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
@Entity
@Table(name="OABBSReplays")
public class OABBSReplayForumBean {
	@Id
	private String id ;			/*帖子Id*/
	private String content ;	/*回复内容*/
	@ManyToOne //(cascade=CascadeType.ALL)
	@JoinColumn(name="bbsRUserID",unique=true,referencedColumnName="id")
	private OABBSUserBean bbsUser ; /*回帖人*/
	private String sendId ;		/*发帖Id*/
	private String attachment ; /*附件*/
	private String createTime ;	/*回复时间*/
	private String createBy ;	/*回帖人*/
	
	
	public OABBSUserBean getBbsUser() {
		return bbsUser;
	}
	public void setBbsUser(OABBSUserBean bbsUser) {
		this.bbsUser = bbsUser;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSendId() {
		return sendId;
	}
	public void setSendId(String sendId) {
		this.sendId = sendId;
	}
}
