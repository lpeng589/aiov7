package com.koron.oa.publicMsg.newsInfo;

import com.menyi.web.util.BaseForm;

public class OANewsForm extends BaseForm {
	
	private static final long serialVersionUID = 1L;
	private String newsId;
	private String newsType;
	private String newsTitle;
	private String newsContext;
	private String userName;
	private String releaseTime; 
	private int isUsed;
	private int statusId;
	private String scompanyId;
	private String wakeupType;
	private String isAlonePopedmon;
	private String popedomUserIds; 
	private String popedomDeptIds;
	private String popedomEmpGroupIds;  
	private String isSaveReading;  //是否保存阅读痕迹
	private String picFiles;	//上传图片
	private String whetherAgreeReply;//是否允许评论
	
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

	public String getPopedomEmpGroupIds() {
		return popedomEmpGroupIds;
	}
	public void setPopedomEmpGroupIds(String popedomEmpGroupIds) {
		this.popedomEmpGroupIds = popedomEmpGroupIds;
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
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public int getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}
	public String getPopedomDeptIds() {
		return popedomDeptIds;
	}
	public void setPopedomDeptIds(String popedomDeptIds) {
		this.popedomDeptIds = popedomDeptIds;
	}
	public String getPopedomUserIds() {
		return popedomUserIds;
	}
	public void setPopedomUserIds(String popedomUserIds) {
		this.popedomUserIds = popedomUserIds;
	}
}
