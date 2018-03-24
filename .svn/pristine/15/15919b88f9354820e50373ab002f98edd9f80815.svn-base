package com.menyi.aio.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="tblDEMainTable")
public class BEMainTableBean {
	@Id
	private String id;
	private String tableName;
	private String startFieldOne;
	private String startFieldTwo;
	@OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="f_ref")
	private BillExportTempBean billExport;
	@OneToOne(mappedBy="mainTable",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private BEChildTableBean childTable;
	private List<BEFieldBean>fields;
	private String sCompanyID;
	public BillExportTempBean getBillExport() {
		return billExport;
	}
	public void setBillExport(BillExportTempBean billExport) {
		this.billExport = billExport;
	}
	public BEChildTableBean getChildTable() {
		return childTable;
	}
	public void setChildTable(BEChildTableBean childTable) {
		this.childTable = childTable;
	}
	public List<BEFieldBean> getFields() {
		return fields;
	}
	public void setFields(List<BEFieldBean> fields) {
		this.fields = fields;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSCompanyID() {
		return sCompanyID;
	}
	public void setSCompanyID(String companyID) {
		sCompanyID = companyID;
	}
	public String getStartFieldOne() {
		return startFieldOne;
	}
	public void setStartFieldOne(String startFieldOne) {
		this.startFieldOne = startFieldOne;
	}
	public String getStartFieldTwo() {
		return startFieldTwo;
	}
	public void setStartFieldTwo(String startFieldTwo) {
		this.startFieldTwo = startFieldTwo;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
