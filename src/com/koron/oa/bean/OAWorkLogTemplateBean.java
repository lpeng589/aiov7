package com.koron.oa.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:�ҵ���־ģ��
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2014-1-7
 * @Copyright: �������
 * @Author ��ܿ�
 * @preserve all
 */

@Entity
@Table(name = "tblPlanTemplate")
public class OAWorkLogTemplateBean {
	@Id
	private String id;// ID��ŵ��Ǳ���

	private String name;//����

	private String toplanType;//week,day

	private String content;//ģ������

	private String userIds;// ְԱids

	private String deptIds;// ����ids

	private String createBy;// ������

	private String createTime;// ����ʱ��

	private String lastUpdateBy;// ����޸���

	private String lastUpdateTime;// ����޸�ʱ��

	private String statusId;// �Ƿ�����0���ã�-1������

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToplanType() {
		return toplanType;
	}

	public void setToplanType(String toplanType) {
		this.toplanType = toplanType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
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

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	
}
