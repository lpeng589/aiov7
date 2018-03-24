package com.menyi.email.emailFilter;

import java.io.Serializable;

import com.menyi.web.util.BaseForm;

/**
 * Description: <br/>Copyright (C), 2012 maojing<br/>This Program
 * is protected by copyright laws. <br/>Program Name: <br/>Date:
 * 
 * @autor mao.jing maojing914@gmail.com
 * @version 1.0
 */
public class EmailFilterForm extends BaseForm implements Serializable {
	private String id;

	private String filterCondition;// ��������

	private String refOaFolderId; // ת�Ƶ��ļ���

	private String refOaMailinfoSettingId; // �����û�

	public String getFilterCondition() {
		return filterCondition;
	}

	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	public String getId() {
		return id;
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
}
