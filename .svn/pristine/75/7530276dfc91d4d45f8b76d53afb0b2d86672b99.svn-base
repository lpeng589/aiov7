package com.koron.crm.bean;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



/**
 * 客户关怀执行明细tblCareforActionDel的实体Bean
 * @version CRM4.3
 * @author 徐磊
 * @preserve all
 */
@Entity
@Table(name = "tblCareforActionDel")
public class CareforActionDelBean implements Serializable{

	
	@Id
    @Column(nullable = false, length = 50)
	private String id;

	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="ref_id")
	private CareforActionBean careforAction;

	@Column(nullable = false, length = 50)
	private String eventId;
	
	@Column(nullable = false, length = 100)
	private String eventName;

	@Column(nullable = true, length = 10)
	private String startDate;

	@Column(nullable = true, length = 10)
	private String endDate;

	@Column(nullable = true, length = 500)
	private String actor;

	@Column(nullable = true, length = 20)
	private String createTime;

	@Column(nullable = true)
	private Integer status;

	@Column(nullable = true, length = 500)
	private String remark;
	
	private int hasPlan;


	
	public int getHasPlan() {
		return hasPlan;
	}

	public void setHasPlan(int hasPlan) {
		this.hasPlan = hasPlan;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CareforActionBean getCareforAction() {
		return careforAction;
	}

	public void setCareforAction(CareforActionBean careforAction) {
		this.careforAction = careforAction;
	}

	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getActor() {
		return this.actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}



	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
