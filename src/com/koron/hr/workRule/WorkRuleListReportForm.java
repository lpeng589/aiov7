package com.koron.hr.workRule;

import java.io.Serializable;

import com.menyi.web.util.BaseForm;


public class WorkRuleListReportForm extends BaseForm implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String reportNo;
	private String employeeNo;
	private String employeeName;
	private String lateCount;
	private String lateMinute;
	private String lateAmerce;
	private String LeaveEarlyCount;
	private String leaveEarlyMinute;
	private String leaveEarlyAmerce;
	private String absentWorkCount;
	private String absentWorkDay;
	private String absentAmerce;
	private String leaveCount;
	private String leaveDay;
	private String evectionCount;
	private String evectionDay;
	private String workDate;
	private String startDate;
	private String endDare;
	private String createBy;
	
	public WorkRuleListReportForm() {}
	
	public WorkRuleListReportForm(String reportNo,String employeeName,String lateCount,String lateMinute,String lateAmerce,String LeaveEarlyCount,
			String leaveEarlyMinute,String leaveEarlyAmerce,String absentWorkCount,String absentWorkDay,String absentAmerce,String leaveCount,String leaveDay,
			String evectionCount,String evectionDay,String workDate) {
		this.reportNo = reportNo;
		this.employeeName = employeeName;
		this.lateCount = lateCount;
		this.lateMinute = lateMinute;
		this.lateAmerce = lateAmerce;
		this.LeaveEarlyCount = LeaveEarlyCount;
		this.leaveEarlyMinute = leaveEarlyMinute;
		this.leaveEarlyAmerce = leaveEarlyAmerce;
		this.absentWorkCount = absentWorkCount;
		this.absentWorkDay = absentWorkDay;
		this.absentAmerce = absentAmerce;
		this.leaveCount = leaveCount;
		this.leaveDay = leaveDay;
		this.evectionCount = evectionCount;
		this.evectionDay = evectionDay;
		this.workDate = workDate;
	}
	
	public WorkRuleListReportForm(String employeeNo,String lateCount,String lateMinute,String LeaveEarlyCount,
			String leaveEarlyMinute,String absentWorkCount,String absentWorkDay,String leaveCount,String leaveDay,
			String evectionCount,String evectionDay,String workDate) {
		this.employeeNo = employeeNo;
		this.lateCount = lateCount;
		this.lateMinute = lateMinute;
		this.LeaveEarlyCount = LeaveEarlyCount;
		this.leaveEarlyMinute = leaveEarlyMinute;
		this.absentWorkCount = absentWorkCount;
		this.absentWorkDay = absentWorkDay;
		this.leaveCount = leaveCount;
		this.leaveDay = leaveDay;
		this.evectionCount = evectionCount;
		this.evectionDay = evectionDay;
		this.workDate = workDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getAbsentWorkCount() {
		return absentWorkCount;
	}

	public void setAbsentWorkCount(String absentWorkCount) {
		this.absentWorkCount = absentWorkCount;
	}

	public String getAbsentWorkDay() {
		return absentWorkDay;
	}

	public void setAbsentWorkDay(String absentWorkDay) {
		this.absentWorkDay = absentWorkDay;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getEvectionCount() {
		return evectionCount;
	}

	public void setEvectionCount(String evectionCount) {
		this.evectionCount = evectionCount;
	}

	public String getEvectionDay() {
		return evectionDay;
	}

	public void setEvectionDay(String evectionDay) {
		this.evectionDay = evectionDay;
	}

	public String getLateCount() {
		return lateCount;
	}

	public void setLateCount(String lateCount) {
		this.lateCount = lateCount;
	}

	public String getLateMinute() {
		return lateMinute;
	}

	public void setLateMinute(String lateMinute) {
		this.lateMinute = lateMinute;
	}

	public String getLeaveCount() {
		return leaveCount;
	}

	public void setLeaveCount(String leaveCount) {
		this.leaveCount = leaveCount;
	}

	public String getLeaveDay() {
		return leaveDay;
	}

	public void setLeaveDay(String leaveDay) {
		this.leaveDay = leaveDay;
	}

	public String getLeaveEarlyCount() {
		return LeaveEarlyCount;
	}

	public void setLeaveEarlyCount(String leaveEarlyCount) {
		LeaveEarlyCount = leaveEarlyCount;
	}

	public String getLeaveEarlyMinute() {
		return leaveEarlyMinute;
	}

	public void setLeaveEarlyMinute(String leaveEarlyMinute) {
		this.leaveEarlyMinute = leaveEarlyMinute;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEndDare() {
		return endDare;
	}

	public void setEndDare(String endDare) {
		this.endDare = endDare;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getAbsentAmerce() {
		return absentAmerce;
	}

	public void setAbsentAmerce(String absentAmerce) {
		this.absentAmerce = absentAmerce;
	}

	public String getLateAmerce() {
		return lateAmerce;
	}

	public void setLateAmerce(String lateAmerce) {
		this.lateAmerce = lateAmerce;
	}

	public String getLeaveEarlyAmerce() {
		return leaveEarlyAmerce;
	}

	public void setLeaveEarlyAmerce(String leaveEarlyAmerce) {
		this.leaveEarlyAmerce = leaveEarlyAmerce;
	}

	
}
