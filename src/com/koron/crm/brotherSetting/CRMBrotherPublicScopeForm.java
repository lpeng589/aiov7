package com.koron.crm.brotherSetting;

import com.menyi.web.util.BaseForm;

public class CRMBrotherPublicScopeForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	private String importScopeFlag;// �����Ƿ�Ȩ�޿���

	private String importDeptIds;// ���벿��IDS

	private String importUserIds;// �������IDS

	private String importGroupIds;// �������IDS

	private String exportScopeFlag;// �����Ƿ�Ȩ�޿���

	private String exportDeptIds;// ���벿��IDS

	private String exportUserIds;// �������IDS

	private String exportGroupIds;// �������IDS

	private String printScopeFlag;// �����Ƿ�Ȩ�޿���

	private String printDeptIds;// ���벿��IDS

	private String printUserIds;// �������IDS

	private String printGroupIds;// �������IDS

	private String tableName; // ����

	public String getImportScopeFlag() {
		return importScopeFlag;
	}

	public void setImportScopeFlag(String importScopeFlag) {
		this.importScopeFlag = importScopeFlag;
	}

	public String getImportDeptIds() {
		return importDeptIds;
	}

	public void setImportDeptIds(String importDeptIds) {
		this.importDeptIds = importDeptIds;
	}

	public String getImportUserIds() {
		return importUserIds;
	}

	public void setImportUserIds(String importUserIds) {
		this.importUserIds = importUserIds;
	}

	public String getImportGroupIds() {
		return importGroupIds;
	}

	public void setImportGroupIds(String importGroupIds) {
		this.importGroupIds = importGroupIds;
	}

	public String getExportScopeFlag() {
		return exportScopeFlag;
	}

	public void setExportScopeFlag(String exportScopeFlag) {
		this.exportScopeFlag = exportScopeFlag;
	}

	public String getExportDeptIds() {
		return exportDeptIds;
	}

	public void setExportDeptIds(String exportDeptIds) {
		this.exportDeptIds = exportDeptIds;
	}

	public String getExportUserIds() {
		return exportUserIds;
	}

	public void setExportUserIds(String exportUserIds) {
		this.exportUserIds = exportUserIds;
	}

	public String getExportGroupIds() {
		return exportGroupIds;
	}

	public void setExportGroupIds(String exportGroupIds) {
		this.exportGroupIds = exportGroupIds;
	}

	public String getPrintScopeFlag() {
		return printScopeFlag;
	}

	public void setPrintScopeFlag(String printScopeFlag) {
		this.printScopeFlag = printScopeFlag;
	}

	public String getPrintDeptIds() {
		return printDeptIds;
	}

	public void setPrintDeptIds(String printDeptIds) {
		this.printDeptIds = printDeptIds;
	}

	public String getPrintUserIds() {
		return printUserIds;
	}

	public void setPrintUserIds(String printUserIds) {
		this.printUserIds = printUserIds;
	}

	public String getPrintGroupIds() {
		return printGroupIds;
	}

	public void setPrintGroupIds(String printGroupIds) {
		this.printGroupIds = printGroupIds;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
