package com.koron.oa.oaTask;

import com.menyi.web.util.BaseSearchForm;

public class OATaskSearchForm extends BaseSearchForm{
	
	private String tabSelectName;//tab查询名称 executor：我负责的 participant:我参与的,surveyor我验收的,createBy=me&executor!=me:我指派的
	
	private String searchStatus;//状态 1:执行中 2:完成 3:验收中
	
	private String searchBeginStartTime;//开始的开始时间
	
	private String searchBeginOverTime;//开始的结束时间
	
	private String searchEndStartTime;//结束的开始时间
	
	private String searchEndOverTime;//结束的结束时间
	
	private String searchCreateBy;//创建人
	
	private String searchExecutor;//负责人

	private String searchSurveyor;//验收人
	
	private String searchItemId;//关联项目
	
	private String searchKeyWord;//关键字
	
	private String searchClientId;//关联客户
	
	private String searchTaskOrder;//编号
	
	private String hasSearchCondition;//true表示有查询条件
	
	private String searchEmergencyLevel;//紧急程度
	
	private String searchOaTaskType;//任务分类
	
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
