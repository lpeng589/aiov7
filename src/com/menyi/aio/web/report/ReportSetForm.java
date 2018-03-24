package com.menyi.aio.web.report;

import com.menyi.web.util.BaseForm;

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
 * Company: 周新宇
 * </p>
 * 
 * @author 周新宇
 * @version 1.0
 */
public class ReportSetForm extends BaseForm {
	private String id;
	private String reportNumber;
	private String reportName;
	private String SQLFileName;
	private String createTime;
	private String lastUpdateTime;
	private String statusId;
	private String[] formatFileName;
	private String reportType;
	private String billTable;
	private String procName;
	private String endClassNumber;
	private int pageSize;
	private int showTotalPage;
	private int showTotalStat;
	private int auditPrint;
	private int defaultNoshow;
	private int colTitleSort ;
	private int fixListTitle ;
	private int fixNumberCol ;
	private String listType ;
	private int showCondition; 
	private int crossColNum;
	private String moduleType ;
	private int showHead;
	private int showRowNumber;
	private String parentLinkReport;
	 
	private String extendsBut;
	
	
	
	public String getExtendsBut() {
		return extendsBut;
	}

	public void setExtendsBut(String extendsBut) {
		this.extendsBut = extendsBut;
	}

	public String getParentLinkReport() {
		return parentLinkReport;
	}

	public void setParentLinkReport(String parentLinkReport) {
		this.parentLinkReport = parentLinkReport;
	}

	public int getCrossColNum() {
		return crossColNum;
	}

	public void setCrossColNum(int crossColNum) {
		this.crossColNum = crossColNum;
	}

	public int getColTitleSort() {
		return colTitleSort;
	}

	public void setColTitleSort(int colTitleSort) {
		this.colTitleSort = colTitleSort;
	}


	public int getDefaultNoshow() {
		return defaultNoshow;
	}

	public void setDefaultNoshow(int defaultNoshow) {
		this.defaultNoshow = defaultNoshow;
	}

	public int getAuditPrint() {
		return auditPrint;
	}

	public void setAuditPrint(int auditPrint) {
		this.auditPrint = auditPrint;
	}

	public int getShowTotalStat() {
		return showTotalStat;
	}

	public void setShowTotalStat(int showTotalStat) {
		this.showTotalStat = showTotalStat;
	}

	public int getShowTotalPage() {
		return showTotalPage;
	}

	public void setShowTotalPage(int showTotalPage) {
		this.showTotalPage = showTotalPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getId() {
		return id;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public String getStatusId() {
		return statusId;
	}

	public String getSQLFileName() {
		return SQLFileName;
	}

	public String getReportName() {
		return reportName;
	}

	public String[] getFormatFileName() {
		return formatFileName;
	}

	public String getReportNumber() {
		return reportNumber;
	}

	public String getReportType() {
		return reportType;
	}

	public String getBillTable() {
		return billTable;
	}

	public String getProcName() {
		return procName;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public void setSQLFileName(String SQLFileName) {
		this.SQLFileName = SQLFileName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setFormatFileName(String[] formatFileName) {
		this.formatFileName = formatFileName;
	}

	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public void setBillTable(String billTable) {
		this.billTable = billTable;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public int getFixListTitle() {
		return fixListTitle;
	}

	public void setFixListTitle(int fixListTitle) {
		this.fixListTitle = fixListTitle;
	}

	public int getFixNumberCol() {
		return fixNumberCol;
	}

	public void setFixNumberCol(int fixNumberCol) {
		this.fixNumberCol = fixNumberCol;
	}

	public String getEndClassNumber() {
		return endClassNumber;
	}

	public void setEndClassNumber(String endClassNumber) {
		this.endClassNumber = endClassNumber;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public int getShowCondition() {
		return showCondition;
	}

	public void setShowCondition(int showCondition) {
		this.showCondition = showCondition;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public int getShowHead() {
		return showHead;
	}

	public void setShowHead(int showHead) {
		this.showHead = showHead;
	}

	public int getShowRowNumber() {
		return showRowNumber;
	}

	public void setShowRowNumber(int showRowNumber) {
		this.showRowNumber = showRowNumber;
	}
	
	
}
