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
	private String id;   //主键
	
	@Column(nullable = false, length = 30)
	private String oajoboddId;  //外键，保存oajobodd表的ID
	
	@Column (nullable = true, length = 50)
	private String assessor;    //审核员
	
	@Column (nullable = true, length = 200)
	private String auditing;    //审核说明
	
	@Column(nullable = true, length = 100)
	private String auditingTime;// 审核时间
	
	@Column (nullable = true, length = 10)
	private String state;      //状态，保存审核是否通过
	
	@Column (nullable = true, length = 100)
	private String attaches;      //附件

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
