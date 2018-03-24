package com.koron.oa.toDo;

import java.io.Serializable;
import java.util.List;

import com.menyi.web.util.BaseSearchForm;

public class ToDoForm extends BaseSearchForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String finishTime;
	private String createBy;
	private String createTime;
	private String type;
	private String alertId;
	private String relationId;
	private String status;
	private String startTime;
	private String endTime;
	private String toId;
	private String alertDay;
	private String alertHours;
	private String alertMinute;
	private String alertTime;
	private String color;
	
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getAlertDay() {
		return alertDay;
	}
	public void setAlertDay(String alertDay) {
		this.alertDay = alertDay;
	}
	public String getAlertHours() {
		return alertHours;
	}
	public void setAlertHours(String alertHours) {
		this.alertHours = alertHours;
	}
	public String getAlertMinute() {
		return alertMinute;
	}
	public void setAlertMinute(String alertMinute) {
		this.alertMinute = alertMinute;
	}
	public String getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlertId() {
		return alertId;
	}
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
}
