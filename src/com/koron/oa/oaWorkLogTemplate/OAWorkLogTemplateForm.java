package com.koron.oa.oaWorkLogTemplate;

import com.menyi.web.util.BaseSearchForm;

public class OAWorkLogTemplateForm extends BaseSearchForm{
	
	private String name;//����

	private String toplanType;//week,day

	private String content;//ģ������

	private String userIds;// ְԱids

	private String deptIds;// ����ids

	private String statusId;// �Ƿ�����0���ã�-1������

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

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	
	
}
