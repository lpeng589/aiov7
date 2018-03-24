/*
 * Created on 2004-4-26
 */
package com.koron.oa.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
*
* <p>Title: </p>
*
* <p>Description: </p>
*
* <p>Copyright: Copyright (c) 2008</p>
*
* <p>Company: 周新宇</p>
*
* @author 毛晶 describe:邮件过滤
* @version 1.0
* @preserve all
*/
@Entity
@Table(name = "EmailFilter")
public class EmailFilter {
	@Id
	@Column(nullable = false, length = 40)
	private String id;

	@Column(length = 3000)
	private String filterCondition;// 过滤条件

	@Column(length = 250)
	private String refOaFolderId; // 转移到文件夹id
	
	@Column(length = 250)
	private String userId;//userId
	
	
	@Column(length = 250)
	private String refOaMailinfoSettingId; // 邮箱用户id

	@Transient
	private String folderName;

	@Transient
	private String addressName;

	private String createTime;

	private String lastUpdateTime;

	public EmailFilter(String id, String filterCondition, String refOaFolderId,
			String refOaMailinfoSettingId) {
		super();
		this.id = id;
		this.filterCondition = filterCondition;
		this.refOaFolderId = refOaFolderId;
		this.refOaMailinfoSettingId = refOaMailinfoSettingId;
	}

	public EmailFilter() {
		super();
	}

	public String getFilterCondition() {
		return filterCondition;
	}

	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	public String getId() {
		return id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRefOaFolderId() {
		return refOaFolderId;
	}

	public void setRefOaFolderId(String refOaFolderId) {
		this.refOaFolderId = refOaFolderId;
	}

	public String getRefOaMailinfoSettingId() {
		return refOaMailinfoSettingId;
	}

	public void setRefOaMailinfoSettingId(String refOaMailinfoSettingId) {
		this.refOaMailinfoSettingId = refOaMailinfoSettingId;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
