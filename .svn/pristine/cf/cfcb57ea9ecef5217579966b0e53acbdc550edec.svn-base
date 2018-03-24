package com.menyi.aio.bean;

import java.io.Serializable;

import javax.persistence.*;

/**
 * 
 * <p>Title:预警设置明细-提示用户Bean</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
@Entity
@Table(name = "tblSysAlertDet")
public class SysAlertDetBean implements Serializable{

	@Id
	private String id;
	
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="f_ref")
	private SysAlertBean sysAlertBean;
	
	private String alertUser;
	private String sCompanyID;
	private String classCode;
	
	
	public String getAlertUser() {
		return alertUser;
	}
	public void setAlertUser(String alertUser) {
		this.alertUser = alertUser;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSCompanyID() {
		return sCompanyID;
	}
	public void setSCompanyID(String companyID) {
		sCompanyID = companyID;
	}
	public SysAlertBean getSysAlertBean() {
		return sysAlertBean;
	}
	public void setSysAlertBean(SysAlertBean sysAlertBean) {
		this.sysAlertBean = sysAlertBean;
	}
	
	
	
}
