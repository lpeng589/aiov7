package com.koron.crm.taskAssign;

import com.menyi.web.util.BaseSearchForm;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: ��ܿ�
 * </p>
 * 
 * @author ��ܿ�
 * @version 1.0
 */
public class CRMTaskAssignSearchForm extends BaseSearchForm {
	
	private String searchTitle;//����
	
	private String clientName;//�ͻ�����
	
	private String searchTaskStatus;//����״̬
	
	private String searchStartTime;//��ʼʱ��
	
	private String searchEndTime;//����ʱ��
	
	private String searchUserId;//������
	

	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getSearchTaskStatus() {
		return searchTaskStatus;
	}

	public void setSearchTaskStatus(String searchTaskStatus) {
		this.searchTaskStatus = searchTaskStatus;
	}

	public String getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(String searchStartTime) {
		this.searchStartTime = searchStartTime;
	}

	public String getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(String searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	public String getSearchUserId() {
		return searchUserId;
	}

	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
	
}
