package com.menyi.aio.web.alert;

import com.menyi.web.util.BaseForm;

/**
 * 
 * <p>Title:预警设置Form</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class AlertSetForm extends BaseForm {

	
	private String id;										//id
	private String SqlDefineName;							//
	private String alertName;								//预警名称
	private int status;                                     //状态（0启用，1不启用）
	private String createby;								//创建者
	private String lastUpdateBy;							//修改者
	private String createtime;								//创建时间
	private String lastUpdateTime;							//修改时间
	private int statusId;									//statusId
	private String moduleType;								//预警所属组
	private String modelId;									//对应报表模块id
	private int conditionStatus;							//条件状态（显示，隐藏）
	private String bewrite;									//条件概述
	private String condition;								//条件
	private String actionTime;								//时间 如：08代表8点开始提醒
	private int actionFrequency;							//预警执行频率
	private String alertType;								//提醒方式（1短信，2邮件，4通知消息）
	private String nextAlertTime;							//下一个时间
	private String remark;									//描述
	private int isHidden;									//显示、隐藏
	
	public String getSqlDefineName() {
		return SqlDefineName;
	}
	public void setSqlDefineName(String sqlDefineName) {
		SqlDefineName = sqlDefineName;
	}
	public int getActionFrequency() {
		return actionFrequency;
	}
	public void setActionFrequency(int actionFrequency) {
		this.actionFrequency = actionFrequency;
	}
	public String getActionTime() {
		return actionTime;
	}
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}
	public String getAlertName() {
		return alertName;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getNextAlertTime() {
		return nextAlertTime;
	}
	public void setNextAlertTime(String nextAlertTime) {
		this.nextAlertTime = nextAlertTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public int getIsHidden() {
		return isHidden;
	}
	public void setIsHidden(int isHidden) {
		this.isHidden = isHidden;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBewrite() {
		return bewrite;
	}
	public void setBewrite(String bewrite) {
		this.bewrite = bewrite;
	}
	public int getConditionStatus() {
		return conditionStatus;
	}
	public void setConditionStatus(int conditionStatus) {
		this.conditionStatus = conditionStatus;
	}
	
	
	
}
