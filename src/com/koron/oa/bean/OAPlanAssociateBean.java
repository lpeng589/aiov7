package com.koron.oa.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2010-6-17
 * @Copyright: 科荣软件
 * @Author 文小钱
 * 
 * @preserve all
 */
@Entity
@Table(name="tblPlanAssociate")
public class OAPlanAssociateBean implements Serializable{

	@Id
	private String id ;
	private String name ;
	private String isEmployee ;
	private String popSelect ;
	private String linkAddress ;
	private String statusId ;
	private String listOrder;
	
	
	
	public String getListOrder() {
		return listOrder;
	}
	public void setListOrder(String listOrder) {
		this.listOrder = listOrder;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsEmployee() {
		return isEmployee;
	}
	public void setIsEmployee(String isEmployee) {
		this.isEmployee = isEmployee;
	}
	public String getLinkAddress() {
		return linkAddress;
	}
	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPopSelect() {
		return popSelect;
	}
	public void setPopSelect(String popSelect) {
		this.popSelect = popSelect;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	
	
	
}
