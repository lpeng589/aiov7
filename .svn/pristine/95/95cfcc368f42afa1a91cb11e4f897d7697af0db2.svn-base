package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:任务分派
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-12-4
 * @Copyright: 科荣软件
 * @Author 徐杰俊
 * @preserve all
 */

@Entity
@Table(name = "CRMTaskAssign")
public class CRMTaskAssignBean {
	
	@Id
	private String id;

	private String title;//标题
	
	private String ref_id;//客户ID

	private String userId;//执行人

	private String content;//内容、描述

	private String taskStatus;//任务状态 0:处理 -1：未处理
	
	private String finishTime;//任务完成时间
	
	private String priority;//优先级 high:高 middle:中 :low:低
	
	private String summary;//总结

	private String createBy;//创建人

	private String createTime;//创建时间
	
	private String lastUpdateBy;//最后修改人
	
	private String lastUpdateTime;//最后修改时间
	
	private String taskType;//任务分类

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRef_id() {
		return ref_id;
	}

	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	
	
}
