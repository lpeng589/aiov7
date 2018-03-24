package com.koron.hr.review;

import com.menyi.web.util.BaseSearchForm;

public class HRReviewSearchForm extends BaseSearchForm{

	private String beginTime ;
	private String endTime ;
	private String empFullName ;
	private String userId ;
	
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEmpFullName() {
		return empFullName;
	}
	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
