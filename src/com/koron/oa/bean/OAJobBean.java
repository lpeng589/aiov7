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
@Table(name = "OAJobodd")
public class OAJobBean implements Serializable {

	@Id
	@Column(nullable = false, length = 30)
	private String id;

	@Column(nullable = true, length = 50)
	private String createPerson;// 创建人

	@Column(nullable = true, length = 50)
	private String jobtheme;// 主题

	@Column(nullable = true, length = 100)
	private String createTime;// 创建时间
	
	@Column(nullable = true, length = 100)
	private String jobBeginTime;// 开始时间

	@Column(nullable = true, length = 100)
	private String jobEndTime;// 结束时间

	@Column(nullable = true, length = 50)
	private String jobType; // 类型

	@Column(nullable = true, length = 50)
	private String assessor; // 审核员

	@Column(nullable = true, length = 50)
	private String intterfixServer;// 相关客户

	@Column(nullable = true, length = 200)
	private String elaborateOn;// 详细说明

	@Column(nullable = true, length = 50)
	private String attaches;// 附件

	@Column(nullable = true, length = 50)
	private String participant;// 参与者
	
	@Column(nullable = true, length = 10)
	private String state;//审核状态
	
	@Column(nullable = true, length = 30)
	private String isSaveReading;//是否保存阅读痕迹
	
	@Column(nullable = true, length = 19)
	private String lastUpdateTime;//
	
	
	public String getIsSaveReading() {
		return isSaveReading;
	}

	public void setIsSaveReading(String isSaveReading) {
		this.isSaveReading = isSaveReading;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAttaches() {
		return attaches;
	}

	public void setAttaches(String attaches) {
		this.attaches = attaches;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getElaborateOn() {
		return elaborateOn;
	}

	public void setElaborateOn(String elaborateOn) {
		this.elaborateOn = elaborateOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIntterfixServer() {
		return intterfixServer;
	}

	public void setIntterfixServer(String intterfixServer) {
		this.intterfixServer = intterfixServer;
	}

	public String getJobBeginTime() {
		return jobBeginTime;
	}

	public void setJobBeginTime(String jobBeginTime) {
		this.jobBeginTime = jobBeginTime;
	}

	public String getJobEndTime() {
		return jobEndTime;
	}

	public void setJobEndTime(String jobEndTime) {
		this.jobEndTime = jobEndTime;
	}

	public String getJobtheme() {
		return jobtheme;
	}

	public void setJobtheme(String jobtheme) {
		this.jobtheme = jobtheme;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getAssessor() {
		return assessor;
	}

	public void setAssessor(String assessor) {
		this.assessor = assessor;
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

}
