package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:字段设置
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-8-8
 * @Copyright: 科荣软件
 * @Author 徐洁俊
 * @preserve all
 */

@Entity
@Table(name = "CRMBrotherFieldScope")
public class BrotherFieldScopeBean {
	@Id
	private String id;// ID存放的是表名

	private static final long serialVersionUID = 1L;

	private String fieldsName;//字段名称

	private String readDeptIds;//只读部门IDS

	private String readUserIds;//只读个人IDS

	private String hideDeptIds;//隐藏部门IDS

	private String hideUserIds;//隐藏个人IDS

	private String tableName;//表名
	
	private String createBy;//创建人
	
	private String createTime;//创建时间
	
	private String lastUpdateBy;//最后修改人
	
	private String lastUpdateTime;//最后更新时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFieldsName() {
		return fieldsName;
	}

	public void setFieldsName(String fieldsName) {
		this.fieldsName = fieldsName;
	}

	public String getReadDeptIds() {
		return readDeptIds;
	}

	public void setReadDeptIds(String readDeptIds) {
		this.readDeptIds = readDeptIds;
	}

	public String getReadUserIds() {
		return readUserIds;
	}

	public void setReadUserIds(String readUserIds) {
		this.readUserIds = readUserIds;
	}

	public String getHideDeptIds() {
		return hideDeptIds;
	}

	public void setHideDeptIds(String hideDeptIds) {
		this.hideDeptIds = hideDeptIds;
	}

	public String getHideUserIds() {
		return hideUserIds;
	}

	public void setHideUserIds(String hideUserIds) {
		this.hideUserIds = hideUserIds;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	
	
}
