package com.menyi.aio.web.logManage;

import com.menyi.web.util.BaseSearchForm;

/**
 * 
 * 
 * <p>Title: ��¼��־SearchForm</p> 
 * <p>Description: </p>
 *
 * @Date:2013-9-22
 * @Copyright: �������
 * @Author ���ҿ�
 */
public class LoginlogSearchForm extends BaseSearchForm{
	
	private String beginTimeSearch;
	private String endTimeSearch;
	private String searchUserId;
	private String searchUserName;
	private String searchCreateTime;
	private String searchDeptName;
	private String searchOperation;									//���������룬�ǳ���
	private String searchTerminal;									//�ն�
	private String searchReason;									//��ע
	private int searchDateType;										//ʱ������
	
	public String getBeginTimeSearch() {
		return beginTimeSearch;
	}
	public void setBeginTimeSearch(String beginTimeSearch) {
		this.beginTimeSearch = beginTimeSearch;
	}
	public String getEndTimeSearch() {
		return endTimeSearch;
	}
	public void setEndTimeSearch(String endTimeSearch) {
		this.endTimeSearch = endTimeSearch;
	}
	public String getSearchCreateTime() {
		return searchCreateTime;
	}
	public void setSearchCreateTime(String searchCreateTime) {
		this.searchCreateTime = searchCreateTime;
	}
	public String getSearchOperation() {
		return searchOperation;
	}
	public void setSearchOperation(String searchOperation) {
		this.searchOperation = searchOperation;
	}
	public String getSearchReason() {
		return searchReason;
	}
	public void setSearchReason(String searchReason) {
		this.searchReason = searchReason;
	}
	public String getSearchTerminal() {
		return searchTerminal;
	}
	public void setSearchTerminal(String searchTerminal) {
		this.searchTerminal = searchTerminal;
	}
	public String getSearchUserId() {
		return searchUserId;
	}
	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
	public int getSearchDateType() {
		return searchDateType;
	}
	public void setSearchDateType(int searchDateType) {
		this.searchDateType = searchDateType;
	}
	public String getSearchUserName() {
		return searchUserName;
	}
	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}
	public String getSearchDeptName() {
		return searchDeptName;
	}
	public void setSearchDeptName(String searchDeptName) {
		this.searchDeptName = searchDeptName;
	}
	
}
