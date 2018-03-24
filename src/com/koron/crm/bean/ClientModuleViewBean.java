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
 * @Author 徐洁俊
 * @preserve all
 */

@Entity
@Table(name = "CRMClientModuleView")
public class ClientModuleViewBean {
	@Id
	private String id;

	private String viewName;

	private String viewDesc;

	private String keyFields;

	private String searchFields;

	private String listFields;

	private String pageFields;

	private String detailFields;

	private String isAlonePopedmon;

	private String popedomUserIds;

	private String popedomDeptIds;

	private String popedomEmpGroupIds;

	private String brotherTables;

	private String createTime;

	private String moduleId;
	
	private String childTableInfo;//明细表
	
	private String childDisplayFields;//明细显示字段

	public String getBrotherTables() {
		return brotherTables;
	}

	public void setBrotherTables(String brotherTables) {
		this.brotherTables = brotherTables;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDetailFields() {
		return detailFields;
	}

	public void setDetailFields(String detailFields) {
		this.detailFields = detailFields;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsAlonePopedmon() {
		return isAlonePopedmon;
	}

	public void setIsAlonePopedmon(String isAlonePopedmon) {
		this.isAlonePopedmon = isAlonePopedmon;
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

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getPageFields() {
		return pageFields;
	}

	public void setPageFields(String pageFields) {
		this.pageFields = pageFields;
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

	public String getChildTableInfo() {
		return childTableInfo;
	}

	public void setChildTableInfo(String childTableInfo) {
		this.childTableInfo = childTableInfo;
	}

	public String getChildDisplayFields() {
		return childDisplayFields;
	}

	public void setChildDisplayFields(String childDisplayFields) {
		this.childDisplayFields = childDisplayFields;
	}

		
}
