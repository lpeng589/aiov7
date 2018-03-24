package com.koron.crm.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * 客户资料的实体Bean
 * @author 徐磊
 * @preserve all
 */
@Entity
@Table(name = "CRMClientInfo")
public class CrmClientInfoBean {

	@Id
	@Column(nullable = false, length = 50)
	private String id;

	@OneToMany(mappedBy="clientBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CrmClientInfoBeanDet> beanDet ;
	
	@Column(nullable = true, length = 50)
	private String classCode;

	@Column(nullable = true, length = 50)
	private String workFlowNode;

	@Column(nullable = true, length = 50)
	private String workFlowNodeName;

	@Column(nullable = true, length = 50)
	private String checkPersons;

	@Column(nullable = false, length = 50)
	private String clientName;

	@Column(nullable = false, length = 50)
	private String clientNo;

	@Column(nullable = true, length = 50)
	private String keywords;

	@Column(nullable = true, length = 50)
	private String otherName;

	@Column(nullable = true, length = 50)
	private String englishName;

	@Column(nullable = true, length = 50)
	private String property;

	@Column(nullable = true, length = 50)
	private String trade;

	@Column(nullable = true, length = 50)
	private String introduce;

	@Column(nullable = true, length = 50)
	private Integer status;

	@Column(nullable = false, length = 50)
	private String clientType;

	@Column(nullable = true, length = 50)
	private String transferDate;

	@Column(nullable = true, length = 50)
	private String level;

	@Column(nullable = true, length = 50)
	private String phone;

	@Column(nullable = true, length = 50)
	private String fax;

	@Column(nullable = true, length = 50)
	private String email;

	@Column(nullable = true, length = 50)
	private String postCode;

	@Column(nullable = true, length = 50)
	private String url;

	@Column(nullable = true, length = 50)
	private String district;

	@Column(nullable = true, length = 50)
	private String address;

	@Column(nullable = true, length = 50)
	private String scale;

	@Column(nullable = false, length = 50)
	private String emergency;

	@Column(nullable = true, length = 50)
	private String emergentWhy;

	@Column(nullable = false, length = 50)
	private String createBy;

	@Column(nullable = true, length = 50)
	private String lastUpdateBy;

	@Column(nullable = true, length = 50)
	private String createTime;

	@Column(nullable = true, length = 50)
	private String lastUpdateTime;

	@Column(nullable = true, length = 50)
	private Integer statusId;

	@Column(nullable = true, length = 50)
	private String scompanyId;

	@Column(nullable = true, length = 50)
	private String keywordsPYM ;
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getWorkFlowNode() {
		return this.workFlowNode;
	}

	public void setWorkFlowNode(String workFlowNode) {
		this.workFlowNode = workFlowNode;
	}

	public String getWorkFlowNodeName() {
		return this.workFlowNodeName;
	}

	public void setWorkFlowNodeName(String workFlowNodeName) {
		this.workFlowNodeName = workFlowNodeName;
	}

	public String getCheckPersons() {
		return this.checkPersons;
	}

	public void setCheckPersons(String checkPersons) {
		this.checkPersons = checkPersons;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientNo() {
		return this.clientNo;
	}

	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}

	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getOtherName() {
		return this.otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getEnglishName() {
		return this.englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTrade() {
		return this.trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getIntroduce() {
		return this.introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getTransferDate() {
		return this.transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getScale() {
		return this.scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getEmergency() {
		return this.emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getEmergentWhy() {
		return this.emergentWhy;
	}

	public void setEmergentWhy(String emergentWhy) {
		this.emergentWhy = emergentWhy;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastUpdateBy() {
		return this.lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getScompanyId() {
		return this.scompanyId;
	}

	public void setScompanyId(String scompanyId) {
		this.scompanyId = scompanyId;
	}

	public String getKeywordsPYM() {
		return keywordsPYM;
	}

	public void setKeywordsPYM(String keywordsPYM) {
		this.keywordsPYM = keywordsPYM;
	}

	public List<CrmClientInfoBeanDet> getBeanDet() {
		return beanDet;
	}

	public void setBeanDet(List<CrmClientInfoBeanDet> beanDet) {
		this.beanDet = beanDet;
	}
}