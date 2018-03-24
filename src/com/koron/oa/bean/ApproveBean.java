package com.koron.oa.bean;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dom4j.tree.AbstractEntity;

import com.google.gson.annotations.Expose;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Nov 25, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * @preserve all
 */
@Entity
@Table(name="OAWorkFlowNodeApprover")
public class ApproveBean  extends AbstractEntity implements Serializable  {
	
	private static final long serialVersionUID = -4572756330976289315L;
	@Expose
	@Id
	private String id;
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="flowNodeId")
    private FlowNodeBean flowNode2;
	@Expose
	@Column(name="type")
	private String type;
	@Expose
	private String typeName;
	@Expose
	@Column(name="[user]")
	private String user;
	@Expose
	private String userName;
	@Expose
	private String checkType;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public FlowNodeBean getFlowNode2() {
		return flowNode2;
	}
	public void setFlowNode2(FlowNodeBean flowNode2) {
		this.flowNode2 = flowNode2;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	
}
