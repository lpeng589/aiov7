package com.koron.oa.bean;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @preserve all
 * @author Administrator
 *
 */

@Entity
@Table(name = "OAJoboddRestore")
public class OAjobRestoreBean implements Serializable {
	
	@Id
	@Column(nullable = false, length = 30)
	private String id;   //����
	
	@Column(nullable = false, length = 30)
	private String oajoboddId;  //���������oajobodd���ID
	
	@Column (nullable = true, length = 50)
	private String participantPerson;  //������
	
	@Column (nullable = true, length = 200)
	private String participantRestore;  //�ظ�����
	
	@Column(nullable = true, length = 100)
	private String restoreTime;// �ظ�ʱ��
	
	@Column (nullable = true, length = 100)
	private String attaches;      //����

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

	public String getAttaches() {
		return attaches;
	}

	public void setAttaches(String attaches) {
		this.attaches = attaches;
	}

	
}
