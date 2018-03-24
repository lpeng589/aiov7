package com.koron.crm.bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 客户联系人的实体Bean
 * @author 徐磊
 * @preserve all
 */
@Entity
@Table(name = "CRMClientInfoDet")
public class CrmClientInfoBeanDet {

	@Id
	@Column(nullable = false, length = 50)
	private String id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="f_ref")
	private CrmClientInfoBean clientBean;

	@Column(nullable = true, length = 50)
	private String userName;

	@Column(nullable = true, length = 50)
	private String mobile;

	@Column(nullable = true, length = 50)
	private String telephone;

	@Column(nullable = true, length = 50)
	private String clientEmail;

	@Column(nullable = false, length = 50)
	private String msn;

	@Column(nullable = false, length = 50)
	private String qq;

	@Column(nullable = true, length = 500)
	private String hobby;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CrmClientInfoBean getClientBean() {
		return clientBean;
	}

	public void setClientBean(CrmClientInfoBean clientBean) {
		this.clientBean = clientBean;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
}