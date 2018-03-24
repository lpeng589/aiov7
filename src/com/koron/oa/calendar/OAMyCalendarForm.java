package com.koron.oa.calendar;

import com.menyi.web.util.BaseForm;

public class OAMyCalendarForm  extends BaseForm{
	
	private String id;
	private String calendarDate;
	private String endDate;
	private String calendarTitle;
	private String calendarContext;
	private String assClient ;
	private String assPeople ;
	private String calendarLevel;
	private String createBy;
	private String createTime;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAssClient() {
		return assClient;
	}
	public void setAssClient(String assClient) {
		this.assClient = assClient;
	}
	public String getAssPeople() {
		return assPeople;
	}
	public void setAssPeople(String assPeople) {
		this.assPeople = assPeople;
	}
	public String getCalendarContext() {
		return calendarContext;
	}
	public void setCalendarContext(String calendarContext) {
		this.calendarContext = calendarContext;
	}
	public String getCalendarDate() {
		return calendarDate;
	}
	public void setCalendarDate(String calendarDate) {
		this.calendarDate = calendarDate;
	}
	public String getCalendarTitle() {
		return calendarTitle;
	}
	public void setCalendarTitle(String calendarTitle) {
		this.calendarTitle = calendarTitle;
	}
	public String getCalendarLevel() {
		return calendarLevel;
	}
	public void setCalendarLevel(String calendarLevel) {
		this.calendarLevel = calendarLevel;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
