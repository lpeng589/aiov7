package com.koron.oa.bean;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:���Żظ�</p> 
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: �������
 * @Author ë��
 * @preserve all
 */
@Entity
@Table(name="OANewsInfoReply")
public class OANewsInfoReplyBean {
	@Id
	private String id ;			//���Żظ�Id
	private String content ;	//�ظ�����
	
	private String newsId ;		//����Id
	private String attachment ; //����
	private String createTime ;	//�ظ�ʱ��
	private String createBy ;	//������
	private String lastUpdateTime;
	private String fullName;
	
	private Integer agreeNum = 0;//��������
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
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public Integer getAgreeNum() {
		return agreeNum;
	}
	public void setAgreeNum(Integer agreeNum) {
		this.agreeNum = agreeNum;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
