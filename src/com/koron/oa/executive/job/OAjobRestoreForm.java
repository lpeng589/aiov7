package com.koron.oa.executive.job;

import java.util.Date;

import javax.persistence.Column;

import com.menyi.web.util.BaseForm;

public class OAjobRestoreForm extends BaseForm {

	private String id;   //主键
	private String oajoboddId;  //外键，保存oajobodd表的ID
	private String participantPerson;  //参与者
	private String participantRestore;  //回复内容
	private String restoreTime;// 回复时间
	
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
	public String getParticipantPerson() {
		return participantPerson;
	}
	public void setParticipantPerson(String participantPerson) {
		this.participantPerson = participantPerson;
	}
	public String getParticipantRestore() {
		return participantRestore;
	}
	public void setParticipantRestore(String participantRestore) {
		this.participantRestore = participantRestore;
	}
	public String getRestoreTime() {
		return restoreTime;
	}
	public void setRestoreTime(String restoreTime) {
		this.restoreTime = restoreTime;
	}
}
