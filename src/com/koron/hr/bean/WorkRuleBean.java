package com.koron.hr.bean;

/**
 * ���ڹ�����
 * @author Administrator
 *
 */
public class WorkRuleBean {

	private String ruleNo;				//������
	private int overdueMinute;			//�ٵ���׼
	private double overdueAmerce;		//�ٵ�����
	private int leaveEarlyMinute;		//���˱�׼
	private double leaveEarlyAmerce;	//���˷���
	private int absentWorkMinute;		//������׼
	private double absentWorkAmerce;	//��������
	private int statusId;				//״̬
	
	
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
