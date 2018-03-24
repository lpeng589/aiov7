package com.menyi.aio.bean;

import java.math.BigDecimal;

import javax.persistence.*;

/**
 * 
 * <p>Title:凭证子表Bean</p> 
 * <p>Description: </p>
 * @Date:2013-03-20
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
@Entity
@Table(name = "tblAccDetail")
public class AccDetailBean {
	
	@Id
    @Column(nullable=false,length=30)
	private String id;
	private String RefBillType;
	private String CompanyCode;
	private String DepartmentCode;
	private String ProjectCode;
	private String AccCode;
	private BigDecimal DebitAmount;
	private BigDecimal LendAmount;
	private BigDecimal DebitCurrencyAmount;
	private BigDecimal LendCurrencyAmount;
	private BigDecimal CurrencyRate;
	private Integer RecordOrder;
	private Integer PeriodYear;
	private Integer PeriodMonth;
	private String createBy;
	private String lastUpdateBy;
	private String createTime;
	private String lastUpdateTime;
	private Integer statusId;
	private String RecordComment;
	private String RefBillID;
	private String AccDate;
	private String SCompanyID;
	private String Currency;
	private String classCode;
	private String RowON;
	private String workFlowNodeName;
	private String workFlowNode;
	private String EmployeeID;
	private Integer detOrderNo;
	private String StockCode;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="f_ref")
	
	private AccMainBean accMainBean;

	public String getAccCode() {
		return AccCode;
	}

	public void setAccCode(String accCode) {
		AccCode = accCode;
	}

	public String getAccDate() {
		return AccDate;
	}

	public void setAccDate(String accDate) {
		AccDate = accDate;
	}

	public AccMainBean getAccMainBean() {
		return accMainBean;
	}

	public void setAccMainBean(AccMainBean accMainBean) {
		this.accMainBean = accMainBean;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getCompanyCode() {
		return CompanyCode;
	}

	public void setCompanyCode(String companyCode) {
		CompanyCode = companyCode;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getDepartmentCode() {
		return DepartmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		DepartmentCode = departmentCode;
	}

	public Integer getDetOrderNo() {
		return detOrderNo;
	}

	public void setDetOrderNo(Integer detOrderNo) {
		this.detOrderNo = detOrderNo;
	}

	public String getEmployeeID() {
		return EmployeeID;
	}

	public void setEmployeeID(String employeeID) {
		EmployeeID = employeeID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public BigDecimal getCurrencyRate() {
		return CurrencyRate;
	}

	public void setCurrencyRate(BigDecimal currencyRate) {
		CurrencyRate = currencyRate;
	}

	public BigDecimal getDebitAmount() {
		return DebitAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		DebitAmount = debitAmount;
	}

	public BigDecimal getDebitCurrencyAmount() {
		return DebitCurrencyAmount;
	}

	public void setDebitCurrencyAmount(BigDecimal debitCurrencyAmount) {
		DebitCurrencyAmount = debitCurrencyAmount;
	}

	public BigDecimal getLendAmount() {
		return LendAmount;
	}

	public void setLendAmount(BigDecimal lendAmount) {
		LendAmount = lendAmount;
	}

	public BigDecimal getLendCurrencyAmount() {
		return LendCurrencyAmount;
	}

	public void setLendCurrencyAmount(BigDecimal lendCurrencyAmount) {
		LendCurrencyAmount = lendCurrencyAmount;
	}

	public Integer getPeriodMonth() {
		return PeriodMonth;
	}

	public void setPeriodMonth(Integer periodMonth) {
		PeriodMonth = periodMonth;
	}

	public Integer getPeriodYear() {
		return PeriodYear;
	}

	public void setPeriodYear(Integer periodYear) {
		PeriodYear = periodYear;
	}

	public String getProjectCode() {
		return ProjectCode;
	}

	public void setProjectCode(String projectCode) {
		ProjectCode = projectCode;
	}

	public String getRecordComment() {
		return RecordComment;
	}

	public void setRecordComment(String recordComment) {
		RecordComment = recordComment;
	}

	public Integer getRecordOrder() {
		return RecordOrder;
	}

	public void setRecordOrder(Integer recordOrder) {
		RecordOrder = recordOrder;
	}

	public String getRefBillID() {
		return RefBillID;
	}

	public void setRefBillID(String refBillID) {
		RefBillID = refBillID;
	}

	public String getRefBillType() {
		return RefBillType;
	}

	public void setRefBillType(String refBillType) {
		RefBillType = refBillType;
	}

	public String getRowON() {
		return RowON;
	}

	public void setRowON(String rowON) {
		RowON = rowON;
	}

	public String getSCompanyID() {
		return SCompanyID;
	}

	public void setSCompanyID(String companyID) {
		SCompanyID = companyID;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStockCode() {
		return StockCode;
	}

	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}

	public String getWorkFlowNode() {
		return workFlowNode;
	}

	public void setWorkFlowNode(String workFlowNode) {
		this.workFlowNode = workFlowNode;
	}

	public String getWorkFlowNodeName() {
		return workFlowNodeName;
	}

	public void setWorkFlowNodeName(String workFlowNodeName) {
		this.workFlowNodeName = workFlowNodeName;
	}

	
	
}
