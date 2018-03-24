package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:CRM ERP转换记录表
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-5-27
 * @Copyright: 科荣软件
 * @Author:徐洁俊
 * @preserve all
 */
@Entity
@Table(name = "CRMTransferRecord")
public class CRMTransferRecordBean {

	@Id
	private String id;

	private String createBy;//创建人

	private String clientId;//客户ID

	private String createTime;//创建时间
	
	private String transferType;//类型 1表示CRM转ERP  2表示ERP转CRM
	
	private String ipAddress;//Ip地址

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	
}