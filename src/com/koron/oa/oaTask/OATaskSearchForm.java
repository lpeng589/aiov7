package com.koron.oa.oaTask;

import com.menyi.web.util.BaseSearchForm;

public class OATaskSearchForm extends BaseSearchForm{
	
	private String tabSelectName;//tab��ѯ���� executor���Ҹ���� participant:�Ҳ����,surveyor�����յ�,createBy=me&executor!=me:��ָ�ɵ�
	
	private String searchStatus;//״̬ 1:ִ���� 2:��� 3:������
	
	private String searchBeginStartTime;//��ʼ�Ŀ�ʼʱ��
	
	private String searchBeginOverTime;//��ʼ�Ľ���ʱ��
	
	private String searchEndStartTime;//�����Ŀ�ʼʱ��
	
	private String searchEndOverTime;//�����Ľ���ʱ��
	
	private String searchCreateBy;//������
	
	private String searchExecutor;//������

	private String searchSurveyor;//������
	
	private String searchItemId;//������Ŀ
	
	private String searchKeyWord;//�ؼ���
	
	private String searchClientId;//�����ͻ�
	
	private String searchTaskOrder;//���
	
	private String hasSearchCondition;//true��ʾ�в�ѯ����
	
	private String searchEmergencyLevel;//�����̶�
	
	private String searchOaTaskType;//�������
	
	public String getTabSelectName() {
		return tabSelectName;
	}

	public void setTabSelectName(String tabSelectName) {
		this.tabSelectName = tabSelectName;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public String getSearchBeginStartTime() {
		return searchBeginStartTime;
	}

	public void setSearchBeginStartTime(String searchBeginStartTime) {
		this.searchBeginStartTime = searchBeginStartTime;
	}

	public String getSearchBeginOverTime() {
		return searchBeginOverTime;
	}

	public void setSearchBeginOverTime(String searchBeginOverTime) {
		this.searchBeginOverTime = searchBeginOverTime;
	}

	public String getSearchEndStartTime() {
		return searchEndStartTime;
	}

	public void setSearchEndStartTime(String searchEndStartTime) {
		this.searchEndStartTime = searchEndStartTime;
	}

	public String getSearchEndOverTime() {
		return searchEndOverTime;
	}

	public void setSearchEndOverTime(String searchEndOverTime) {
		this.searchEndOverTime = searchEndOverTime;
	}

	public String getSearchCreateBy() {
		return searchCreateBy;
	}

	public void setSearchCreateBy(String searchCreateBy) {
		this.searchCreateBy = searchCreateBy;
	}

	public String getSearchExecutor() {
		return searchExecutor;
	}

	public void setSearchExecutor(String searchExecutor) {
		this.searchExecutor = searchExecutor;
	}

	public String getSearchSurveyor() {
		return searchSurveyor;
	}

	public void setSearchSurveyor(String searchSurveyor) {
		this.searchSurveyor = searchSurveyor;
	}

	public String getSearchItemId() {
		return searchItemId;
	}

	public void setSearchItemId(String searchItemId) {
		this.searchItemId = searchItemId;
	}

	public String getSearchKeyWord() {
		return searchKeyWord;
	}

	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	public String getSearchClientId() {
		return searchClientId;
	}

	public void setSearchClientId(String searchClientId) {
		this.searchClientId = searchClientId;
	}

	public String getSearchTaskOrder() {
		return searchTaskOrder;
	}

	public void setSearchTaskOrder(String searchTaskOrder) {
		this.searchTaskOrder = searchTaskOrder;
	}

	public String getHasSearchCondition() {
		return hasSearchCondition;
	}

	public void setHasSearchCondition(String hasSearchCondition) {
		this.hasSearchCondition = hasSearchCondition;
	}

	public String getSearchEmergencyLevel() {
		return searchEmergencyLevel;
	}

	public void setSearchEmergencyLevel(String searchEmergencyLevel) {
		this.searchEmergencyLevel = searchEmergencyLevel;
	}

	public String getSearchOaTaskType() {
		return searchOaTaskType;
	}

	public void setSearchOaTaskType(String searchOaTaskType) {
		this.searchOaTaskType = searchOaTaskType;
	}

}
