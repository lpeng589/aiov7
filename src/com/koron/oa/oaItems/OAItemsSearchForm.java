package com.koron.oa.oaItems;

import com.menyi.web.util.BaseSearchForm;

public class OAItemsSearchForm extends BaseSearchForm {

	private String tabSelectName;// tab��ѯ���� executor���Ҹ���� participant:�Ҳ����

	private String searchStatus;// ״̬ 1:ִ���� 2:���
	
	private String searchKeyWord;//�ؼ���

	private String searchBeginStartTime;// ��ʼ�Ŀ�ʼʱ��

	private String searchBeginOverTime;// ��ʼ�Ľ���ʱ��

	private String searchEndStartTime;// �����Ŀ�ʼʱ��

	private String searchEndOverTime;// �����Ľ���ʱ��
	
	private String searchClientId;// �ͻ�ID
	
	private String searchItemOrder;// ���
	
	private String hasSearchCondition;//true��ʾ�в�ѯ����

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

	public String getSearchClientId() {
		return searchClientId;
	}

	public void setSearchClientId(String searchClientId) {
		this.searchClientId = searchClientId;
	}

	public String getSearchKeyWord() {
		return searchKeyWord;
	}

	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	public String getSearchItemOrder() {
		return searchItemOrder;
	}

	public void setSearchItemOrder(String searchItemOrder) {
		this.searchItemOrder = searchItemOrder;
	}

	public String getHasSearchCondition() {
		return hasSearchCondition;
	}

	public void setHasSearchCondition(String hasSearchCondition) {
		this.hasSearchCondition = hasSearchCondition;
	}
	
	

}
