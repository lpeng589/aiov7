package com.koron.oa.OACalendar;

import java.io.Serializable;
import java.util.List;

import com.menyi.web.util.BaseSearchForm;

public class OACalendarForm extends BaseSearchForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String year;
	private String month;
	private String day;
	private String type;
	// calendarList
	private String listTitle;
	private String createTime;
	private String endTime;
	
	private String crmEnter;//true表示CRM日程进入
	private String clientName;
	
	private String finishStatus;//1完成0未完成
	private String userId;

	
	public String getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}

	

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getListTitle() {
		return listTitle;
	}

	public void setListTitle(String listTitle) {
		this.listTitle = listTitle;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCrmEnter() {
		return crmEnter;
	}

	public void setCrmEnter(String crmEnter) {
		this.crmEnter = crmEnter;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
}
