package com.koron.oa.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:字段设置
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-11-5
 * @Copyright: 科荣软件
 * @Author 徐杰俊
 * @preserve all
 */

@Entity
@Table(name = "OATask")
public class OATaskBean {
	@Id
	private String id;// ID存放的是表名

	private String title;// 项目标题

	private String executor;// 执行人（负责人）

	private String beginTime;// 开始时间

	private String endTime;// 结束时间

	private String participant;// 参与人

	private String remark;// 项目描述

	private String affix;// 附件

	private String status;// 状态 1=进行中，2=完成

	private String createBy;// 创建人

	private String createTime;// 创建时间

	private String lastUpdateBy;// 最后修改人

	private String lastUpdateTime;// 最后修改时间

	private String schedule;// 完成进度(%)

	private String itemId;// 关联项目ID

	private String taskId;// 关联任务ID

	private String isCatalog;// 是否根目录 0没下级,1有下级

	private String surveyor;// 验收人
	
	private String nextAlertTime;//提醒时间
	
	private String finishTime;//完成时间
	
	private String clientId;//客户Id
	
	private int taskOrder;//任务编号
	
	private String emergencyLevel;//紧急程度
	
	private String taskType;//任务分类

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
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

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAffix() {
		return affix;
	}

	public void setAffix(String affix) {
		this.affix = affix;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getIsCatalog() {
		return isCatalog;
	}

	public String getSurveyor() {
		return surveyor;
	}

	public void setSurveyor(String surveyor) {
		this.surveyor = surveyor;
	}

	public void setIsCatalog(String isCatalog) {
		this.isCatalog = isCatalog;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getNextAlertTime() {
		return nextAlertTime;
	}

	public void setNextAlertTime(String nextAlertTime) {
		this.nextAlertTime = nextAlertTime;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public int getTaskOrder() {
		return taskOrder;
	}

	public void setTaskOrder(int taskOrder) {
		this.taskOrder = taskOrder;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getEmergencyLevel() {
		return emergencyLevel;
	}

	public void setEmergencyLevel(String emergencyLevel) {
		this.emergencyLevel = emergencyLevel;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	
}
