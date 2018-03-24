package com.menyi.aio.bean;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="tblBillExportTemp")
public class BillExportTempBean {
	@Id
	private String id;
    private int isUsed;
    @OneToOne(mappedBy="billExport",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BEMainTableBean mainTable;
    @Transient
    private KRLanguage display;
    
    @Column(nullable=false,length=30)
    private String createBy;
    @Column(nullable=false,length=14)
    private String createTime;
    @Column(nullable=true,length=30)
    private String lastUpdateBy;
    @Column(nullable=true,length=14)
    private String lastUpdateTime;
    private String languageId;
    private String sCompanyID;
	public String getSCompanyID() {
		return sCompanyID;
	}

	public void setSCompanyID(String companyID) {
		sCompanyID = companyID;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public KRLanguage getDisplay() {
		return display;
	}

	public void setDisplay(KRLanguage display) {
		this.display = display;
	}

	public int getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
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

	public BEMainTableBean getMainTable() {
		return mainTable;
	}

	public void setMainTable(BEMainTableBean mainTable) {
		this.mainTable = mainTable;
	}
	
}
