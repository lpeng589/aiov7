package com.koron.oa.office.car.carOut;

import java.io.Serializable;


import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;

/**
 * 
 * <p>Title:版块</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-14
 * @Copyright: 科荣软件
 * @Author wyy
 * @preserve all
 */
@Entity
@Table(name="OACarOperate")
public class OACarOperateBean extends AbstractEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String userCarPerson;
	private String outCarDate;
	private String overCarDate;
	private String userCarReason;    /*用车事由*/
	private String approver;   /*审批人*/
	private String destination;/*目的地*/
	private String aboutDistance;/*大约里程数*/
	private String backDate;
	private String backRole;
	private String carNo;
	private String status;//归还和派车状态 0为等待审批，1为派车，2为为通过，3为已完成
	private String dynamic;
	private String reason;
	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDynamic() {
		return dynamic;
	}
	public void setDynamic(String dynamic) {
		this.dynamic = dynamic;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserCarPerson() {
		return userCarPerson;
	}
	public void setUserCarPerson(String userCarPerson) {
		this.userCarPerson = userCarPerson;
	}

	public String getOutCarDate() {
		return outCarDate;
	}
	public void setOutCarDate(String outCarDate) {
		this.outCarDate = outCarDate;
	}
	public String getOverCarDate() {
		return overCarDate;
	}
	public void setOverCarDate(String overCarDate) {
		this.overCarDate = overCarDate;
	}
	public String getUserCarReason() {
		return userCarReason;
	}
	public void setUserCarReason(String userCarReason) {
		this.userCarReason = userCarReason;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getAboutDistance() {
		return aboutDistance;
	}
	public void setAboutDistance(String aboutDistance) {
		this.aboutDistance = aboutDistance;
	}
	public String getBackDate() {
		return backDate;
	}
	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}
	public String getBackRole() {
		return backRole;
	}
	public void setBackRole(String backRole) {
		this.backRole = backRole;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	
	
}
