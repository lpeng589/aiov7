package com.koron.crm.setting;

import com.menyi.web.util.BaseForm;
import com.menyi.web.util.GlobalsTool;

public class ClientModuleForm extends BaseForm {
	private static final long serialVersionUID = 1L;

	private String viewName;

	private String viewDesc;
	
	private String moduleName;

	private String moduleDesc;

	private String keyFields;

	private String searchFields;

	private String listFields;

	private String pageFields;

	private String groupNames;

	private String detailFields;

	private String isAlonePopedmon;

	private String popedomUserIds;

	private String popedomDeptIds;

	private String popedomEmpGroupIds;

	private String brotherTables;

	private String isUserLabel;
	
	private String workflow;
	
	private String keyWordStatus;//关键字是否启用 0不启用 1启用
	
	private String swotStatus;//SWOT分析是否启用 0不启用 1启用
	

	private int pageNo;
	private String isUserTransfer;
	private int pageSize = GlobalsTool.getPageSize();

	private String defPageSize;

	
	
	public String getIsUserTransfer() {
		return isUserTransfer;
	}

	public void setIsUserTransfer(String isUserTransfer) {
		this.isUserTransfer = isUserTransfer;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getBrotherTables() {
		return brotherTables;
	}

	public void setBrotherTables(String brotherTables) {
		this.brotherTables = brotherTables;
	}

	public String getDefPageSize() {
		return defPageSize;
	}

	public void setDefPageSize(String defPageSize) {
		this.defPageSize = defPageSize;
	}

	public String getDetailFields() {
		return detailFields;
	}

	public void setDetailFields(String detailFields) {
		this.detailFields = detailFields;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public String getIsAlonePopedmon() {
		return isAlonePopedmon;
	}

	public void setIsAlonePopedmon(String isAlonePopedmon) {
		this.isAlonePopedmon = isAlonePopedmon;
	}

	public String getIsUserLabel() {
		return isUserLabel;
	}

	public void setIsUserLabel(String isUserLabel) {
		this.isUserLabel = isUserLabel;
	}

	public String getKeyFields() {
		return keyFields;
	}

	public void setKeyFields(String keyFields) {
		this.keyFields = keyFields;
	}

	public String getListFields() {
		return listFields;
	}

	public void setListFields(String listFields) {
		this.listFields = listFields;
	}

	public String getPageFields() {
		return pageFields;
	}

	public void setPageFields(String pageFields) {
		this.pageFields = pageFields;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPopedomDeptIds() {
		return popedomDeptIds;
	}

	public void setPopedomDeptIds(String popedomDeptIds) {
		this.popedomDeptIds = popedomDeptIds;
	}

	public String getPopedomEmpGroupIds() {
		return popedomEmpGroupIds;
	}

	public void setPopedomEmpGroupIds(String popedomEmpGroupIds) {
		this.popedomEmpGroupIds = popedomEmpGroupIds;
	}

	public String getPopedomUserIds() {
		return popedomUserIds;
	}

	public void setPopedomUserIds(String popedomUserIds) {
		this.popedomUserIds = popedomUserIds;
	}

	public String getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields;
	}

	public String getViewDesc() {
		return viewDesc;
	}

	public void setViewDesc(String viewDesc) {
		this.viewDesc = viewDesc;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
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

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
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
