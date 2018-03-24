package com.koron.oa.individual.workPlan;

import java.util.List;

import org.apache.struts.upload.FormFile;

import com.menyi.web.util.BaseForm;
import com.menyi.web.util.BaseSearchForm;
/**
 * 
 * <p>Title: 表单类，接收客户端传来信息</p> 
 * <p>Description: </p>
 *
 * @Date:2010-5-10
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class OADayWorkPlanSearchForm  extends BaseSearchForm{

	private String department ;
	private String departmentName ;
	private String employee ;
	private String title ;
	private String scompanyID ;
	private String beginDate ;
	private String endDate ;
	private String statusId ="0";
	private String typeId;
	
	
	
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getScompanyID() {
		return scompanyID;
	}
	public void setScompanyID(String scompanyID) {
		this.scompanyID = scompanyID;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	
}
