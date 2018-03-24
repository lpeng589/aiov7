package com.koron.crm.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


/**
 * 客户关怀执行tblCareforAction的实体Bean
 * @version CRM4.3
 * @author 徐磊
 * @preserve all
 */
@Entity
@Table(name = "tblCareforAction")
public class CareforActionBean implements Serializable{
	
	@Id
    @Column(nullable = false, length = 50)
	private String id;

	@Column(nullable = false, length = 50)
	private String clientId;

	@Column(nullable = false, length = 50)
	private String careforId;
	
	@Column(nullable = false, length = 100)
	private String careforName;

	@Column(nullable = false, length = 10)
	private String baselineDate;

	@Column(nullable = false, length = 500)
	private String actor;

	@Column(nullable = false, length = 4000)
	private String receiver;

	@Column(nullable = true)
	private String status;

	@Column(nullable = true, length = 20)
	private String createTime;
	
	@OneToMany(mappedBy = "careforAction", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("startDate")
    private List<CareforActionDelBean> careforActionDels;	
	
	public String getCareforName() {
		return careforName;
	}

	public void setCareforName(String careforName) {
		this.careforName = careforName;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCareforId() {
		return this.careforId;
	}

	public void setCareforId(String careforId) {
		this.careforId = careforId;
	}

	public String getBaselineDate() {
		return this.baselineDate;
	}

	public void setBaselineDate(String baselineDate) {
		this.baselineDate = baselineDate;
	}

	public String getActor() {
		return this.actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<CareforActionDelBean> getCareforActionDels() {
		return careforActionDels;
	}

	public void setCareforActionDels(List<CareforActionDelBean> careforActionDels) {
		this.careforActionDels = careforActionDels;
	}

}