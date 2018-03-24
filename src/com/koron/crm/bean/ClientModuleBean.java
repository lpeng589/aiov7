package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:�ͻ�ģ��
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-10-24
 * @Copyright: �������
 * @Author ������
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
	
	private String transferFields;//���CRM��ERP��ת��ӳ���ֶ� erp:crm
	
	private String clientFieldsMapping;//���Ǳ�ڿͻ�����ͻ���Ϣ���ӳ���ֶ� Ǳ�ڿͻ�:�ͻ���Ϣ

	private String keyWordStatus;//�ؼ����Ƿ����� 0������ 1����
	
	private String swotStatus;//SWOT�������� 0������ 1����
	
	
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
