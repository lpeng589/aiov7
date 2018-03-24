package com.menyi.aio.web.alert;

import java.io.Serializable;

public class AlertDataBean implements Serializable
{
	private String alertDataId;
	private String empid;
	private String empFullName;
	public String getAlertDataId()
	{
		return alertDataId;
	}
	public void setAlertDataId(String alertDataId)
	{
		this.alertDataId = alertDataId;
	}
	public String getEmpid()
	{
		return empid;
	}
	public void setEmpid(String empid)
	{
		this.empid = empid;
	}
	public String getEmpFullName()
	{
		return empFullName;
	}
	public void setEmpFullName(String empFullName)
	{
		this.empFullName = empFullName;
	}
	
}
