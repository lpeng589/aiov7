package com.koron.crm.carefor;

import javax.persistence.*;

import com.menyi.web.util.BaseForm;



/**
 * 客户关怀方案明细tblCareforDel的Form
 * @version CRM4.3
 * @author 徐磊
 *
 */
public class CareforDelForm extends BaseForm{

	private String id;
	private String ref_id;
	private String dateType;
	private String startDate;
	private String endDate;
	private Integer baselineDate;
	private Integer runDates;
	private Integer space ;
	private Integer runTimes;
	private String actionType;
	private String eventContent;
	private String smsTime;
	private String smsContent;
	private String ccSelf;
	private String addressType;
	private String mailTitle;
	private String mailContent;
	private Integer sequence;
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

	public Integer getBaselineDate() {
		return this.baselineDate;
	}

	public void setBaselineDate(Integer baselineDate) {
		this.baselineDate = baselineDate;
	}

	public Integer getRunDates() {
		return this.runDates;
	}

	public void setRunDates(Integer runDates) {
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

	public Integer getRunTimes() {
		return runTimes;
	}

	public void setRunTimes(Integer runTimes) {
		this.runTimes = runTimes;
	}

	public Integer getSpace() {
		return space;
	}

	public void setSpace(Integer space) {
		this.space = space;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}


}