package com.koron.oa.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 2, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * @preserve all
 * 
 */
@Entity
@Table(name="OAWorkConsignation")
public class OAWorkConsignBean {
	
	@Id
	private String id;
	private String userid;
	private String congignuserid;
	private String beginTime;
	private String endTime;
	private int state;
	private String createby;
	private String createtime;
	private String flowName;
	private String flowNameDis;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getCongignuserid() {
		return congignuserid;
	}
	public void setCongignuserid(String congignuserid) {
		this.congignuserid = congignuserid;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getFlowNameDis() {
		return flowNameDis;
	}
	public void setFlowNameDis(String flowNameDis) {
		this.flowNameDis = flowNameDis;
	}
}
