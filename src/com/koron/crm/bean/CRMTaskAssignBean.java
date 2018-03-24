package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:�������
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-12-4
 * @Copyright: �������
 * @Author ��ܿ�
 * @preserve all
 */

@Entity
@Table(name = "CRMTaskAssign")
public class CRMTaskAssignBean {
	
	@Id
	private String id;

	private String title;//����
	
	private String ref_id;//�ͻ�ID

	private String userId;//ִ����

	private String content;//���ݡ�����

	private String taskStatus;//����״̬ 0:���� -1��δ����
	
	private String finishTime;//�������ʱ��
	
	private String priority;//���ȼ� high:�� middle:�� :low:��
	
	private String summary;//�ܽ�

	private String createBy;//������

	private String createTime;//����ʱ��
	
	private String lastUpdateBy;//����޸���
	
	private String lastUpdateTime;//����޸�ʱ��
	
	private String taskType;//�������

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
