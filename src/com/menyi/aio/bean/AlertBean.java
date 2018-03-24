package com.menyi.aio.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:提醒设置</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 * 
 */
@Entity
@Table(name="tblAlert")
public class AlertBean {

	@Id
	private String id ;	
	private String alertDate ;	/*提醒日期*/
	private int alertHour ;		/*提醒小时*/
	private int alertMinute ;	/*提醒分钟*/
	private String alertContent ;	/*提醒内容*/
	private String isLoop ;			/*循环提醒*/
	private String loopType ;		/*循环类型*/
	private int loopTime ;			/*循环次数*/
	private String endDate ;		/*结束日期*/
	private String alertType ;		/*提醒方式*/
	private String nextAlertTime ;	/*下次提醒时间*/
	private String createBy ;		/*建立人*/
	private String createTime ;		/*建立时间*/
	private int statusId ;			/*提醒状态*/
	private String relationId ;		/*关联ID*/
	private String relationTable ;	/*关联表*/
	private String popedomUserIds ;
	private String popedomDeptIds ;
	private String alertUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlertContent() {
		return alertContent;
	}
	public void setAlertContent(String alertContent) {
		this.alertContent = alertContent;
	}
	public String getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(String alertDate) {
		this.alertDate = alertDate;
	}
	public int getAlertHour() {
		return alertHour;
	}
	public void setAlertHour(int alertHour) {
		this.alertHour = alertHour;
	}
	public int getAlertMinute() {
		return alertMinute;
	}
	public void setAlertMinute(int alertMinute) {
		this.alertMinute = alertMinute;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIsLoop() {
		return isLoop;
	}
	public void setIsLoop(String isLoop) {
		this.isLoop = isLoop;
	}
	public int getLoopTime() {
		return loopTime;
	}
	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}
	public String getLoopType() {
		return loopType;
	}
	public void setLoopType(String loopType) {
		this.loopType = loopType;
	}
	public String getNextAlertTime() {
		return nextAlertTime;
	}
	public void setNextAlertTime(String nextAlertTime) {
		this.nextAlertTime = nextAlertTime;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getRelationTable() {
		return relationTable;
	}
	public void setRelationTable(String relationTable) {
		this.relationTable = relationTable;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
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
	public String getAlertUrl() {
		return alertUrl;
	}
	public void setAlertUrl(String alertUrl) {
		this.alertUrl = alertUrl;
	}
	
}
