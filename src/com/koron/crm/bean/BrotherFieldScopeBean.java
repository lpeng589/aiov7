package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:�ֶ�����
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-8-8
 * @Copyright: �������
 * @Author ��࿡
 * @preserve all
 */

@Entity
@Table(name = "CRMBrotherFieldScope")
public class BrotherFieldScopeBean {
	@Id
	private String id;// ID��ŵ��Ǳ���

	private static final long serialVersionUID = 1L;

	private String fieldsName;//�ֶ�����

	private String readDeptIds;//ֻ������IDS

	private String readUserIds;//ֻ������IDS

	private String hideDeptIds;//���ز���IDS

	private String hideUserIds;//���ظ���IDS

	private String tableName;//����
	
	private String createBy;//������
	
	private String createTime;//����ʱ��
	
	private String lastUpdateBy;//����޸���
	
	private String lastUpdateTime;//������ʱ��

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
