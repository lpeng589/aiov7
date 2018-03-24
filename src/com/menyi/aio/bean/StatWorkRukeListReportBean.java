package com.menyi.aio.bean;

import java.io.Serializable;

/**
 * ͳ����ϸ������
 * @author Administrator
 *
 */
public class StatWorkRukeListReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String employeeNo;
	private String empFullName;
	private int lateSum;
	private double lateMinuteSum;
	private double lateAmerce;
	private int leaveEarlySum;
	private double leaveEarlyMinuteSum;
	private double leaveEarlyAmerce;
	private int absentWorkSum;
	private double absentWorkDaySum;
	private double absentAmerce;
	private int leaveSum;
	private double leaveDaySum;
	private int evectionSum;
	private double evectionDaySum;
	
	public StatWorkRukeListReportBean() {}
	
	public StatWorkRukeListReportBean(String employeeNo,String empFullName,int lateSum,double lateMinuteSum,double lateAmerce,
			int leaveEarlySum,double leaveEarlyMinuteSum,double leaveEarlyAmerce,int absentWorkSum,double absentWorkDaySum,double absentAmerce,
			int leaveSum,double leaveDaySum,int evectionSum,double evectionDaySum) {
		this.employeeNo = employeeNo;
		this.empFullName = empFullName;
		this.lateSum = lateSum;
		this.lateMinuteSum = lateMinuteSum;
		this.lateAmerce = lateAmerce;
		this.leaveEarlySum = leaveEarlySum;
		this.leaveEarlyMinuteSum = leaveEarlyMinuteSum;
		this.leaveEarlyAmerce = leaveEarlyAmerce;
		this.absentWorkSum = absentWorkSum;
		this.absentWorkDaySum = absentWorkDaySum;
		this.absentAmerce = absentAmerce;
		this.leaveSum = leaveSum;
		this.leaveDaySum = leaveDaySum;
		this.evectionSum = evectionSum;
		this.evectionDaySum = evectionDaySum;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public double getAbsentWorkDaySum() {
		return absentWorkDaySum;
	}

	public void setAbsentWorkDaySum(double absentWorkDaySum) {
		this.absentWorkDaySum = absentWorkDaySum;
	}

	public int getAbsentWorkSum() {
		return absentWorkSum;
	}

	public void setAbsentWorkSum(int absentWorkSum) {
		this.absentWorkSum = absentWorkSum;
	}

	public String getEmpFullName() {
		return empFullName;
	}

	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public double getEvectionDaySum() {
		return evectionDaySum;
	}

	public void setEvectionDaySum(double evectionDaySum) {
		this.evectionDaySum = evectionDaySum;
	}

	public int getEvectionSum() {
		return evectionSum;
	}

	public void setEvectionSum(int evectionSum) {
		this.evectionSum = evectionSum;
	}

	public double getLateMinuteSum() {
		return lateMinuteSum;
	}

	public void setLateMinuteSum(double lateMinuteSum) {
		this.lateMinuteSum = lateMinuteSum;
	}

	public int getLateSum() {
		return lateSum;
	}

	public void setLateSum(int lateSum) {
		this.lateSum = lateSum;
	}

	public double getLeaveDaySum() {
		return leaveDaySum;
	}

	public void setLeaveDaySum(double leaveDaySum) {
		this.leaveDaySum = leaveDaySum;
	}

	public double getLeaveEarlyMinuteSum() {
		return leaveEarlyMinuteSum;
	}

	public void setLeaveEarlyMinuteSum(double leaveEarlyMinuteSum) {
		this.leaveEarlyMinuteSum = leaveEarlyMinuteSum;
	}

	public int getLeaveEarlySum() {
		return leaveEarlySum;
	}

	public void setLeaveEarlySum(int leaveEarlySum) {
		this.leaveEarlySum = leaveEarlySum;
	}

	public int getLeaveSum() {
		return leaveSum;
	}

	public void setLeaveSum(int leaveSum) {
		this.leaveSum = leaveSum;
	}

	public double getAbsentAmerce() {
		return absentAmerce;
	}

	public void setAbsentAmerce(double absentAmerce) {
		this.absentAmerce = absentAmerce;
	}

	public double getLateAmerce() {
		return lateAmerce;
	}

	public void setLateAmerce(double lateAmerce) {
		this.lateAmerce = lateAmerce;
	}

	public double getLeaveEarlyAmerce() {
		return leaveEarlyAmerce;
	}

	public void setLeaveEarlyAmerce(double leaveEarlyAmerce) {
		this.leaveEarlyAmerce = leaveEarlyAmerce;
	}


	
}
