package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * <p>Title:CRM标签表</p> 
 * <p>Description: </p>
 * @Date:2013-1-23
 * @Copyright: 科荣软件
 * @Author:徐洁俊
 * @preserve all
 */
@Entity
@Table(name="CRMLabel")
public class CRMLabelBean {

	@Id
	private String id;

	private String labelName;
	
	private String labelColor;
	
	private String createTime;
	
	private String employeeId;
	
	private String moduleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabelColor() {
		return labelColor;
	}

	public void setLabelColor(String labelColor) {
		this.labelColor = labelColor;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	
}