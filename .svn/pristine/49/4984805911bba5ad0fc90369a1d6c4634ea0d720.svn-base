package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:客户模版
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-10-24
 * @Copyright: 科荣软件
 * @Author 李文祥
 * @preserve all
 */

@Entity
@Table(name = "CRMClientModule")
public class ClientModuleBean {
	
	@Id
	private String id;

	private String moduleName;

	private String moduleDesc;

	private String defPageSize;

	private String tableInfo;

	private String createTime;

	private String isUserLabel;
	
	private String isUserTransfer;
	
	private String workflow;
	
	private String transferFields;//存放CRM与ERP互转的映射字段 erp:crm
	
	private String clientFieldsMapping;//存放潜在客户表与客户信息表的映射字段 潜在客户:客户信息

	private String keyWordStatus;//关键字是否启用 0不启用 1启用
	
	private String swotStatus;//SWOT分析启用 0不启用 1启用
	
	
	public String getCreateTime() {
		return createTime;
	}

	
	public String getIsUserTransfer() {
		return isUserTransfer;
	}


	public void setIsUserTransfer(String isUserTransfer) {
		this.isUserTransfer = isUserTransfer;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDefPageSize() {
		return defPageSize;
	}

	public void setDefPageSize(String defPageSize) {
		this.defPageSize = defPageSize;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsUserLabel() {
		return isUserLabel;
	}

	public void setIsUserLabel(String isUserLabel) {
		this.isUserLabel = isUserLabel;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(String tableInfo) {
		this.tableInfo = tableInfo;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getTransferFields() {
		return transferFields;
	}

	public void setTransferFields(String transferFields) {
		this.transferFields = transferFields;
	}


	public String getClientFieldsMapping() {
		return clientFieldsMapping;
	}


	public void setClientFieldsMapping(String clientFieldsMapping) {
		this.clientFieldsMapping = clientFieldsMapping;
	}

	public String getKeyWordStatus() {
		return keyWordStatus;
	}


	public void setKeyWordStatus(String keyWordStatus) {
		this.keyWordStatus = keyWordStatus;
	}


	public String getSwotStatus() {
		return swotStatus;
	}


	public void setSwotStatus(String swotStatus) {
		this.swotStatus = swotStatus;
	}
	
	
}
