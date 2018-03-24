package com.menyi.aio.bean;

import java.io.Serializable;

/**
 * �򿨼�¼��
 * @author Administrator
 *
 */
public class BrushCardAnnalBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String employeeNo;
	private String cardAnnalTime;
	private String dutyDate;
	
	public BrushCardAnnalBean() {}
	
	public BrushCardAnnalBean(String cardAnnalTime,String dutyDate){
		this.cardAnnalTime = cardAnnalTime;
		this.dutyDate = dutyDate;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCardAnnalTime() {
		return cardAnnalTime;
	}

	public void setCardAnnalTime(String cardAnnalTime) {
		this.cardAnnalTime = cardAnnalTime;
	}

	public String getDutyDate() {
		return dutyDate;
	}

	public void setDutyDate(String dutyDate) {
		this.dutyDate = dutyDate;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	
}
