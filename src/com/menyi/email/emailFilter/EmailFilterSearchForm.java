package com.menyi.email.emailFilter;

import com.menyi.web.util.BaseSearchForm;
/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class EmailFilterSearchForm extends BaseSearchForm {
	
	private String filterCondition;//��������
	private String folder; //ת�Ƶ��ļ���
	private String oaMailinfoSetting; //�����û�
	public String getFilterCondition() {
		return filterCondition;
	}
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public String getOaMailinfoSetting() {
		return oaMailinfoSetting;
	}
	public void setOaMailinfoSetting(String oaMailinfoSetting) {
		this.oaMailinfoSetting = oaMailinfoSetting;
	}
	
}
