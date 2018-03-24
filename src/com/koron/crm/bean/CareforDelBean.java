package com.koron.crm.bean;

import java.io.Serializable;

import javax.persistence.*;



/**
 * 客户关怀方案明细tblCareforDel的实体Bean
 * @version CRM4.3
 * @author 徐磊
 * @preserve all
 */
@Entity
@Table(name = "tblCareforDel")
public class CareforDelBean implements Serializable{

	@Id
    @Column(nullable = false, length = 50)
	private String id;

	@Column(nullable = false, length = 50)
	private String ref_id;

	@Column(nullable = true, length = 10)
	private String dateType;

	@Column(nullable = true, length = 10)
	private String startDate;

	@Column(nullable = true, length = 10)
	private String endDate;

	@Column(nullable = true)
	private int baselineDate;

	@Column(nullable = true)
	private int runDates;
	
	@Column(nullable = true)
	private int space ;
	
	@Column(nullable = true)
	private int runTimes;

	@Column(nullable = true, length = 1)
	private String actionType;

	@Column(nullable = true, length = 1000)
	private String eventContent;

	@Column(nullable = true, length = 20)
	private String smsTime;

	@Column(nullable = true, length = 500)
	private String smsContent;

	@Column(nullable = true, length = 1)
	private String ccSelf;

	@Column(nullable = true, length = 1)
	private String addressType;

	@Column(nullable = true, length = 200)
	private String mailTitle;

	@Column(nullable = true, length = 2000)
	private String mailContent;
	
	
	@Column(nullable = false, length = 50)
	private String actionName;



	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRef_id() {
		return this.ref_id;
	}

	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}

	public String getDateType() {
		return this.dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
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

	public int getBaselineDate() {
		return this.baselineDate;
	}

	public void setBaselineDate(int baselineDate) {
		this.baselineDate = baselineDate;
	}

	public int getRunDates() {
		return this.runDates;
	}

	public void setRunDates(int runDates) {
		this.runDates = runDates;
	}

	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getEventContent() {
		return this.eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public String getSmsTime() {
		return this.smsTime;
	}

	public void setSmsTime(String smsTime) {
		this.smsTime = smsTime;
	}

	public String getSmsContent() {
		return this.smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getCcSelf() {
		return this.ccSelf;
	}

	public void setCcSelf(String ccSelf) {
		this.ccSelf = ccSelf;
	}

	public String getAddressType() {
		return this.addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getMailTitle() {
		return this.mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return this.mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public int getRunTimes() {
		return runTimes;
	}

	public void setRunTimes(int runTimes) {
		this.runTimes = runTimes;
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}


}