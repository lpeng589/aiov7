package com.koron.crm.distributeswork;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:h����
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-09-29
 * @Copyright: ��������
 * @Author kezhiliang
 * @preserve all
 */
@Entity
@Table(name = "CRMTaskAssign")
public class CRMDistWorkBean {
	@Id
	private String id;
	private String topic;
	private String classCode;
	private String workFlowNode;
	private String workFlowNodeName;
	private String checkPersons;
	private String ref_id;
	private String taskType;
	private String content;
	private String userId;
	private String lastDate;
	private String createBy;
	private String lastUpdateBy;
	private String createTime;
	private String lastUpdateTime;
	private int statusId;
	private String sCompanyID;
	private String taskStatus;
	private String assignStatus;
	private String crmAffix;
	private String finishTime;
	private String checkPersont;
	private String finishUser;
	private String action;

	public String getFinishUser() {
		return finishUser;
	}

	public void setFinishUser(String finishUser) {
		this.finishUser = finishUser;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getWorkFlowNode() {
		return workFlowNode;
	}

	public void setWorkFlowNode(String workFlowNode) {
		this.workFlowNode = workFlowNode;
	}

	public String getWorkFlowNodeName() {
		return workFlowNodeName;
	}

	public void setWorkFlowNodeName(String workFlowNodeName) {
		this.workFlowNodeName = workFlowNodeName;
	}

	public String getCheckPersons() {
		return checkPersons;
	}

	public void setCheckPersons(String checkPersons) {
		this.checkPersons = checkPersons;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getsCompanyID() {
		return sCompanyID;
	}

	public void setsCompanyID(String sCompanyID) {
		this.sCompanyID = sCompanyID;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getAssignStatus() {
		return assignStatus;
	}

	public void setAssignStatus(String assignStatus) {
		this.assignStatus = assignStatus;
	}

	public String getCrmAffix() {
		return crmAffix;
	}

	public void setCrmAffix(String crmAffix) {
		this.crmAffix = crmAffix;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getCheckPersont() {
		return checkPersont;
	}

	public void setCheckPersont(String checkPersont) {
		this.checkPersont = checkPersont;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSCompanyID() {
		return sCompanyID;
	}

	public void setSCompanyID(String companyID) {
		sCompanyID = companyID;
	}

}
