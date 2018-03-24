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
@Table(name = "OAJoboddAuditing")
public class OAJobAuditingBean implements Serializable {
	@Id
	@Column(nullable = false, length = 30)
	private String id;   //����
	
	@Column(nullable = false, length = 30)
	private String oajoboddId;  //���������oajobodd���ID
	
	@Column (nullable = true, length = 50)
	private String assessor;    //���Ա
	
	@Column (nullable = true, length = 200)
	private String auditing;    //���˵��
	
	@Column(nullable = true, length = 100)
	private String auditingTime;// ���ʱ��
	
	@Column (nullable = true, length = 10)
	private String state;      //״̬����������Ƿ�ͨ��
	
	@Column (nullable = true, length = 100)
	private String attaches;      //����

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

	public String getAuditingTime() {
		return auditingTime;
	}

	public void setAuditingTime(String auditingTime) {
		this.auditingTime = auditingTime;
	}

	public String getAttaches() {
		return attaches;
	}

	public void setAttaches(String attaches) {
		this.attaches = attaches;
	}
}
