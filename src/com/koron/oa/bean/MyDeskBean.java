package com.koron.oa.bean;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Jul 11, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * 
 */
public class MyDeskBean {

	private String id;
	private String modulName;
	private int modulOrderBy;
	private int status;
	private String linkAddress;
	private String moreLinkAddress;
	private String modulSql;
	private int dataRowCount;
	private int colNumber ;
	private String moduleType ;
	private int width ;
	
	public int getDataRowCount() {
		return dataRowCount;
	}
	public void setDataRowCount(int dataRowCount) {
		this.dataRowCount = dataRowCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLinkAddress() {
		return linkAddress;
	}
	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}
	public String getModulName() {
		return modulName;
	}
	public void setModulName(String modulName) {
		this.modulName = modulName;
	}
	public int getModulOrderBy() {
		return modulOrderBy;
	}
	public void setModulOrderBy(int modulOrderBy) {
		this.modulOrderBy = modulOrderBy;
	}
	public String getModulSql() {
		return modulSql;
	}
	public void setModulSql(String modulSql) {
		this.modulSql = modulSql;
	}
	public String getMoreLinkAddress() {
		return moreLinkAddress;
	}
	public void setMoreLinkAddress(String moreLinkAddress) {
		this.moreLinkAddress = moreLinkAddress;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getColNumber() {
		return colNumber;
	}
	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
}
