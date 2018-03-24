package com.koron.oa.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Aug 10, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * @preserve all
 */
@Entity
@Table(name="OAMyCalendar")
public class OAMyCalendarBean {
	
	@Id
	private String id;
	private String classCode;
	private String calendarDate;
	private String calendarTitle;
	private String calendarContext;
	private String wakeupbeginDate;
	private String nowwakeupbeginDate;
	private int nowwaleuptimes;
	private int setp;
	private String wakeupType;
	private int waleupTimes;
	private String createBy;
	private String lastupdateBy;
	private String createTime;
	private String lastupdateTime;
	private int statusId;
	private String scompanyId;
	private int beforeDay;
	private int beforeHour;
	private int beforeMinute;
	private String assClient ;
	private String assPeople ;
	private String endDate;
	private String calendarLevel;
	
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
	public int getBeforeDay() {
		return beforeDay;
	}
	public void setBeforeDay(int beforeDay) {
		this.beforeDay = beforeDay;
	}
	public int getBeforeHour() {
		return beforeHour;
	}
	public void setBeforeHour(int beforeHour) {
		this.beforeHour = beforeHour;
	}
	public int getBeforeMinute() {
		return beforeMinute;
	}
	public void setBeforeMinute(int beforeMinute) {
		this.beforeMinute = beforeMinute;
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
	
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLastupdateBy() {
		return lastupdateBy;
	}
	public void setLastupdateBy(String lastupdateBy) {
		this.lastupdateBy = lastupdateBy;
	}
	public String getLastupdateTime() {
		return lastupdateTime;
	}
	public void setLastupdateTime(String lastupdateTime) {
		this.lastupdateTime = lastupdateTime;
	}
	public String getNowwakeupbeginDate() {
		return nowwakeupbeginDate;
	}
	public void setNowwakeupbeginDate(String nowwakeupbeginDate) {
		this.nowwakeupbeginDate = nowwakeupbeginDate;
	}
	public int getNowwaleuptimes() {
		return nowwaleuptimes;
	}
	public void setNowwaleuptimes(int nowwaleuptimes) {
		this.nowwaleuptimes = nowwaleuptimes;
	}
	
	public String getScompanyId() {
		return scompanyId;
	}
	public void setScompanyId(String scompanyId) {
		this.scompanyId = scompanyId;
	}
	public int getSetp() {
		return setp;
	}
	public void setSetp(int setp) {
		this.setp = setp;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getWakeupbeginDate() {
		return wakeupbeginDate;
	}
	public void setWakeupbeginDate(String wakeupbeginDate) {
		this.wakeupbeginDate = wakeupbeginDate;
	}
	public String getWakeupType() {
		return wakeupType;
	}
	public void setWakeupType(String wakeupType) {
		this.wakeupType = wakeupType;
	}
	public int getWaleupTimes() {
		return waleupTimes;
	}
	public void setWaleupTimes(int waleupTimes) {
		this.waleupTimes = waleupTimes;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCalendarLevel() {
		return calendarLevel;
	}
	public void setCalendarLevel(String calendarLevel) {
		this.calendarLevel = calendarLevel;
	}
	
	
}
