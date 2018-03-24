package com.koron.oa.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:新闻中心</p> 
 * <p>Description: </p>
 * @Date:2012-6-5
 * @Copyright: 科荣软件
 * @Author 李文祥
 * @preserve all
 */
@Entity
@Table(name="OACompanyNewsInfo")
public class OANewsBean {
	
	@Id
	private String id;
	private String newsType;
	private String newsTitle;
	private String newsContext;
	private String userName;
	private String releaseTime;
	private String createBy;
	private String lastupDateBy;
	private String createTime;
	private String lastupDateTime;
	private int statusId;
	private String scompanyId;
	private String wakeupType;
	private String isAlonePopedmon;
	private String popedomUserIds;
	private String popedomDeptIds;
	private String popedomEmpGroupIds;
	private String isSaveReading;
	private String picFiles;
	private String whetherAgreeReply;
	
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
	public String getLastupDateBy() {
		return lastupDateBy;
	}
	public void setLastupDateBy(String lastupDateBy) {
		this.lastupDateBy = lastupDateBy;
	}
	public String getLastupDateTime() {
		return lastupDateTime;
	}
	public void setLastupDateTime(String lastupDateTime) {
		this.lastupDateTime = lastupDateTime;
	}
	public String getIsAlonePopedmon() {
		return isAlonePopedmon;
	}
	public void setIsAlonePopedmon(String isAlonePopedmon) {
		this.isAlonePopedmon = isAlonePopedmon;
	}
	public String getIsSaveReading() {
		return isSaveReading;
	}
	public void setIsSaveReading(String isSaveReading) {
		this.isSaveReading = isSaveReading;
	}
	public String getNewsContext() {
		return newsContext;
	}
	public void setNewsContext(String newsContext) {
		this.newsContext = newsContext;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsType() {
		return newsType;
	}
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}
	public String getPicFiles() {
		return picFiles;
	}
	public void setPicFiles(String picFiles) {
		this.picFiles = picFiles;
	}

	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getScompanyId() {
		return scompanyId;
	}
	public void setScompanyId(String scompanyId) {
		this.scompanyId = scompanyId;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWakeupType() {
		return wakeupType;
	}
	public void setWakeupType(String wakeupType) {
		this.wakeupType = wakeupType;
	}
	
	public String getWhetherAgreeReply() {
		return whetherAgreeReply;
	}
	public void setWhetherAgreeReply(String whetherAgreeReply) {
		this.whetherAgreeReply = whetherAgreeReply;
	}
	public String getPopedomDeptIds() {
		return popedomDeptIds;
	}
	public void setPopedomDeptIds(String popedomDeptIds) {
		this.popedomDeptIds = popedomDeptIds;
	}
	public String getPopedomEmpGroupIds() {
		return popedomEmpGroupIds;
	}
	public void setPopedomEmpGroupIds(String popedomEmpGroupIds) {
		this.popedomEmpGroupIds = popedomEmpGroupIds;
	}
	public String getPopedomUserIds() {
		return popedomUserIds;
	}
	public void setPopedomUserIds(String popedomUserIds) {
		this.popedomUserIds = popedomUserIds;
	}

	
	
}
