package com.koron.oa.executive.job;

import java.util.Date;

import javax.persistence.Column;

import com.menyi.web.util.BaseForm;

public class OAJobAuditingForm extends BaseForm {
	
	private String id;   //����
	private String oajoboddId;  //���������oajobodd���ID
	private String assessor;    //���Ա
	private String auditing;    //���˵��
	private String auditingTime;// ���ʱ��
	private String state;      //״̬����������Ƿ�ͨ��
	public String getAssessor() {
		return assessor;
	}
	public void setAssessor(String assessor) {
		this.assessor = assessor;
	}
	public String getAuditing() {
		return auditing;
	}
	public void setAuditing(String auditing) {
		this.auditing = auditing;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOajoboddId() {
		return oajoboddId;
	}
	public void setOajoboddId(String oajoboddId) {
		this.oajoboddId = oajoboddId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
