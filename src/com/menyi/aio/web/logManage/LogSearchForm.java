package com.menyi.aio.web.logManage;

import com.menyi.web.util.BaseSearchForm;

/**
 * 
 * 
 * <p>Title: 操作日志SearchForm</p> 
 * <p>Description: </p>
 *
 * @Date:2013-9-22
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class LogSearchForm extends BaseSearchForm{
	
	private String beginTimeSearch;
	private String endTimeSearch;
	private String searchUserId;
	private String searchUserName;
	private String searchDuan;
	private String searchCreateTime;
	private String searchOperation;									//操作（新增，删除，修改，转交，暂存，草稿，草稿过帐，列配置，导入，零售上传，手机上传）
	private String searchBill;										//单据搜索（编号，名称等）
	private String searchOpResult;									//操作结果
	private String searchReason;									//备注
	private String searchContent;									//内容
	private int searchDateType;										//时间类型（全部,一天以内，一周以内，一个月以内，三个月以内，三个月以外）
	
	
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
	public String getSearchDuan() {
		return searchDuan;
	}
	public void setSearchDuan(String searchDuan) {
		this.searchDuan = searchDuan;
	}
	public String getSearchOperation() {
		return searchOperation;
	}
	public void setSearchOperation(String searchOperation) {
		this.searchOperation = searchOperation;
	}
	public String getSearchOpResult() {
		return searchOpResult;
	}
	public void setSearchOpResult(String searchOpResult) {
		this.searchOpResult = searchOpResult;
	}
	public String getSearchReason() {
		return searchReason;
	}
	public void setSearchReason(String searchReason) {
		this.searchReason = searchReason;
	}
	public String getSearchUserId() {
		return searchUserId;
	}
	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
	}
	public String getSearchUserName() {
		return searchUserName;
	}
	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}
	public String getSearchContent() {
		return searchContent;
	}
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	public int getSearchDateType() {
		return searchDateType;
	}
	public void setSearchDateType(int searchDateType) {
		this.searchDateType = searchDateType;
	}
	public String getSearchBill() {
		return searchBill;
	}
	public void setSearchBill(String searchBill) {
		this.searchBill = searchBill;
	}
	
	
}
