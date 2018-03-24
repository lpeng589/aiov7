package com.menyi.aio.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="tblDEChildTable")
public class BEChildTableBean {

	@Id
	private String id;
	private String tableName;
	private String startField;
	@OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="f_ref")
    private BEMainTableBean mainTable;
    private List<BEFieldBean>fields;
	private String sCompanyID;
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
	public BEMainTableBean getMainTable() {
		return mainTable;
	}
	public void setMainTable(BEMainTableBean mainTable) {
		this.mainTable = mainTable;
	}
	public String getSCompanyID() {
		return sCompanyID;
	}
	public void setSCompanyID(String companyID) {
		sCompanyID = companyID;
	}
	public String getStartField() {
		return startField;
	}
	public void setStartField(String startField) {
		this.startField = startField;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
