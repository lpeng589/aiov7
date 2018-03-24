package com.menyi.aio.web.finance.voucher;

import com.menyi.web.util.BaseSearchForm;

public class VoucherSearchForm extends BaseSearchForm{
	
	private String searchCredYearStart;		//会计期间开始年
	private String searchCredMonthStart;	//会计期间开始月
	private String searchCredYearEnd;		//会计期间结束年
	private String searchCredMonthEnd;		//会计期间结束月
	private String searchStartTime;			//单据开始时间
	private String searchEndTime;			//单据结束时间
	private String searchCredType;			//凭证字
	private String searchOrderNoStart;		//凭证号开始
	private String searchOrderNoEnd;		//凭证号结束
	private String searchAccCodeStart;		//科目代码开始
	private String searchAccCodeEnd;		//科目代码结束
	private String searchIsAuditing;		//审核
	private String searchIsReview;			//复核
	private String searchwName;				//过账
	private String searchDept;				//部门编号
	private String searchDeptName;			//部门名称
	private String searchEmployee;			//经手编号
	private String searchEmployeeName;		//经手名称
	private String searchRefBillNo;			//单据编号
	private String searchRecordComment;		//摘要
	private String searchClient;			//往来单位编号
	private String searchClientName;		//往来单位名称
	private String searchRefBillType;		//单据类型
	private String searchMoneyStart;				//金额查询
	private String searchMoneyEnd;				//金额查询
	private String createBy; 				//创建人
	private String tblEmployee_EmpFullName; //创建人名称
	
	private String searchAccType; //凭证类型
	
	public String getSearchAccType() {
		return searchAccType;
	}
	public void setSearchAccType(String searchAccType) {
		this.searchAccType = searchAccType;
	}
	public String getSearchAccCodeEnd() {
		return searchAccCodeEnd;
	}
	public void setSearchAccCodeEnd(String searchAccCodeEnd) {
		this.searchAccCodeEnd = searchAccCodeEnd;
	}
	public String getSearchAccCodeStart() {
		return searchAccCodeStart;
	}
	public void setSearchAccCodeStart(String searchAccCodeStart) {
		this.searchAccCodeStart = searchAccCodeStart;
	}
	public String getSearchClient() {
		return searchClient;
	}
	public void setSearchClient(String searchClient) {
		this.searchClient = searchClient;
	}
	public String getSearchClientName() {
		return searchClientName;
	}
	public void setSearchClientName(String searchClientName) {
		this.searchClientName = searchClientName;
	}
	public String getSearchCredMonthEnd() {
		return searchCredMonthEnd;
	}
	public void setSearchCredMonthEnd(String searchCredMonthEnd) {
		this.searchCredMonthEnd = searchCredMonthEnd;
	}
	public String getSearchCredMonthStart() {
		return searchCredMonthStart;
	}
	public void setSearchCredMonthStart(String searchCredMonthStart) {
		this.searchCredMonthStart = searchCredMonthStart;
	}
	public String getSearchCredType() {
		return searchCredType;
	}
	public void setSearchCredType(String searchCredType) {
		this.searchCredType = searchCredType;
	}
	public String getSearchCredYearEnd() {
		return searchCredYearEnd;
	}
	public void setSearchCredYearEnd(String searchCredYearEnd) {
		this.searchCredYearEnd = searchCredYearEnd;
	}
	public String getSearchCredYearStart() {
		return searchCredYearStart;
	}
	public void setSearchCredYearStart(String searchCredYearStart) {
		this.searchCredYearStart = searchCredYearStart;
	}
	public String getSearchDept() {
		return searchDept;
	}
	public void setSearchDept(String searchDept) {
		this.searchDept = searchDept;
	}
	public String getSearchDeptName() {
		return searchDeptName;
	}
	public void setSearchDeptName(String searchDeptName) {
		this.searchDeptName = searchDeptName;
	}
	public String getSearchEmployee() {
		return searchEmployee;
	}
	public void setSearchEmployee(String searchEmployee) {
		this.searchEmployee = searchEmployee;
	}
	public String getSearchEmployeeName() {
		return searchEmployeeName;
	}
	public void setSearchEmployeeName(String searchEmployeeName) {
		this.searchEmployeeName = searchEmployeeName;
	}
	public String getSearchEndTime() {
		return searchEndTime;
	}
	public void setSearchEndTime(String searchEndTime) {
		this.searchEndTime = searchEndTime;
	}
	public String getSearchIsAuditing() {
		return searchIsAuditing;
	}
	public void setSearchIsAuditing(String searchIsAuditing) {
		this.searchIsAuditing = searchIsAuditing;
	}
	public String getSearchIsReview() {
		return searchIsReview;
	}
	public void setSearchIsReview(String searchIsReview) {
		this.searchIsReview = searchIsReview;
	}
	public String getSearchOrderNoEnd() {
		return searchOrderNoEnd;
	}
	public void setSearchOrderNoEnd(String searchOrderNoEnd) {
		this.searchOrderNoEnd = searchOrderNoEnd;
	}
	public String getSearchOrderNoStart() {
		return searchOrderNoStart;
	}
	public void setSearchOrderNoStart(String searchOrderNoStart) {
		this.searchOrderNoStart = searchOrderNoStart;
	}
	public String getSearchRecordComment() {
		return searchRecordComment;
	}
	public void setSearchRecordComment(String searchRecordComment) {
		this.searchRecordComment = searchRecordComment;
	}
	public String getSearchRefBillNo() {
		return searchRefBillNo;
	}
	public void setSearchRefBillNo(String searchRefBillNo) {
		this.searchRefBillNo = searchRefBillNo;
	}
	public String getSearchStartTime() {
		return searchStartTime;
	}
	public void setSearchStartTime(String searchStartTime) {
		this.searchStartTime = searchStartTime;
	}
	public String getSearchwName() {
		return searchwName;
	}
	public void setSearchwName(String searchwName) {
		this.searchwName = searchwName;
	}
	public String getSearchRefBillType() {
		return searchRefBillType;
	}
	public void setSearchRefBillType(String searchRefBillType) {
		this.searchRefBillType = searchRefBillType;
	}

	public String getSearchMoneyStart() {
		return searchMoneyStart;
	}
	public void setSearchMoneyStart(String searchMoneyStart) {
		this.searchMoneyStart = searchMoneyStart;
	}
	public String getSearchMoneyEnd() {
		return searchMoneyEnd;
	}
	public void setSearchMoneyEnd(String searchMoneyEnd) {
		this.searchMoneyEnd = searchMoneyEnd;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getTblEmployee_EmpFullName() {
		return tblEmployee_EmpFullName;
	}
	public void setTblEmployee_EmpFullName(String tblEmployee_EmpFullName) {
		this.tblEmployee_EmpFullName = tblEmployee_EmpFullName;
	}
	
	
	
	
	
}
