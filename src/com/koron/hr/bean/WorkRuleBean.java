package com.koron.hr.bean;

/**
 * 考勤规则类
 * @author Administrator
 *
 */
public class WorkRuleBean {

	private String ruleNo;				//规则编号
	private int overdueMinute;			//迟到标准
	private double overdueAmerce;		//迟到罚款
	private int leaveEarlyMinute;		//早退标准
	private double leaveEarlyAmerce;	//早退罚款
	private int absentWorkMinute;		//旷工标准
	private double absentWorkAmerce;	//旷工罚款
	private int statusId;				//状态
	
	
	public WorkRuleBean(){}
	
	public WorkRuleBean(String ruleNo,int overdueMinute,double overdueAmerce,
			int leaveEarlyMinute,double leaveEarlyAmerce,int absentWorkMinute,
			double absentWorkAmerce,int statusId){
		this.ruleNo = ruleNo;
		this.overdueMinute = overdueMinute;
		this.overdueAmerce = overdueAmerce;
		this.leaveEarlyMinute = leaveEarlyMinute;
		this.leaveEarlyAmerce = leaveEarlyAmerce;
		this.absentWorkMinute = absentWorkMinute;
		this.absentWorkAmerce = absentWorkAmerce;
		this.statusId = statusId;
	}
	
	public double getAbsentWorkAmerce() {
		return absentWorkAmerce;
	}
	public void setAbsentWorkAmerce(double absentWorkAmerce) {
		this.absentWorkAmerce = absentWorkAmerce;
	}
	public int getAbsentWorkMinute() {
		return absentWorkMinute;
	}
	public void setAbsentWorkMinute(int absentWorkMinute) {
		this.absentWorkMinute = absentWorkMinute;
	}
	public double getLeaveEarlyAmerce() {
		return leaveEarlyAmerce;
	}
	public void setLeaveEarlyAmerce(double leaveEarlyAmerce) {
		this.leaveEarlyAmerce = leaveEarlyAmerce;
	}
	public int getLeaveEarlyMinute() {
		return leaveEarlyMinute;
	}
	public void setLeaveEarlyMinute(int leaveEarlyMinute) {
		this.leaveEarlyMinute = leaveEarlyMinute;
	}
	public double getOverdueAmerce() {
		return overdueAmerce;
	}
	public void setOverdueAmerce(double overdueAmerce) {
		this.overdueAmerce = overdueAmerce;
	}
	public int getOverdueMinute() {
		return overdueMinute;
	}
	public void setOverdueMinute(int overdueMinute) {
		this.overdueMinute = overdueMinute;
	}
	public String getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	
}
