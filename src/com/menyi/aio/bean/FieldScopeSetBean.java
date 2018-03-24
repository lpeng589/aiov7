package com.menyi.aio.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:字段权限设置
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-11-1
 * @Copyright: 科荣软件
 * @Author 李文祥
 * @preserve all
 */

@Entity
@Table(name = "CRMFieldScopeSet")
public class FieldScopeSetBean {
	@Id
	private String id;

	private String fieldsName;

	private String readpopedomDeptIds;

	private String readpopedomUserIds;

	private String hiddenpopedomDeptIds;

	private String hiddenpopedomUserIds;

	private String createBy;

	private String createTime;
	
	private String moduleId;

	private String viewId;

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

	public String getHiddenpopedomDeptIds() {
		return hiddenpopedomDeptIds;
	}

	public void setHiddenpopedomDeptIds(String hiddenpopedomDeptIds) {
		this.hiddenpopedomDeptIds = hiddenpopedomDeptIds;
	}

	public String getHiddenpopedomUserIds() {
		return hiddenpopedomUserIds;
	}

	public void setHiddenpopedomUserIds(String hiddenpopedomUserIds) {
		this.hiddenpopedomUserIds = hiddenpopedomUserIds;
	}

	public String getReadpopedomDeptIds() {
		return readpopedomDeptIds;
	}

	public void setReadpopedomDeptIds(String readpopedomDeptIds) {
		this.readpopedomDeptIds = readpopedomDeptIds;
	}

	public String getReadpopedomUserIds() {
		return readpopedomUserIds;
	}

	public void setReadpopedomUserIds(String readpopedomUserIds) {
		this.readpopedomUserIds = readpopedomUserIds;
	}

	public String getFieldsName() {
		return fieldsName;
	}

	public void setFieldsName(String fieldsName) {
		this.fieldsName = fieldsName;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	
}
