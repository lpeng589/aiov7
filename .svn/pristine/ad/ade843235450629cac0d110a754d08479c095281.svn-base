package com.menyi.aio.bean;

import java.util.Map;

import javax.persistence.*;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 * @preserve all
 */

@Entity
@Table(name="tblReports")
public class ReportsBean {	
    @Id
    @Column(nullable=false,length=30)
    private String id;

    @OneToMany(mappedBy="referBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKey(name="id")
    @OrderBy("FormatName")
    private Map<String,ReportsDetBean> reportDetBean;
    @Column(nullable=false,length=100)
    private String ReportName;
    @Column(nullable=false,length=100)
    private String ReportNumber;
    @Column(nullable=true,length=100)
    private String SQLFileName;
    @Column(nullable=true,length=19)
    private String createTime;
    @Column(nullable=true,length=19)
    private String lastUpdateTime;
    @Column(nullable=true)
    private Integer statusId;
    @Column(nullable=false,length=30)
    private String createBy;
    @Column(nullable=true,length=30)
    private String lastUpdateBy;
    @Column(nullable=true,length=10)
    private String newFlag;
    @Column(nullable=true,length=10)
    private String ReportType;
    @Column(nullable=true,length=100)
    private String BillTable;
    @Column(nullable=true,length=100)
    private String ProcName;
    @Column(nullable=true)
    private Integer pageSize;
    @Column(nullable=true)
    private Integer showTotalPage;
    @Column(nullable=true)
    private Integer showTotalStat;
    @Column(nullable=true)
    private Integer auditPrint;
    @Column(nullable=true)
    private Integer showDet;
    @Column(nullable=true)
    private Integer colTitleSort ;
    @Column(nullable=true)
    private Integer fixListTitle ;
    @Column(nullable=true)
    private Integer mainModule ;
    @Column(nullable=true)
    private Integer defaultNoshow ;
    @Column(nullable=true)
    private Integer fixNumberCol ;
    @Column(nullable=true,length=100)
    private String endClassNumber;
    @Column(nullable=true)
    private String listType ;/*checkbox=1 radio=0*/
    @Column(nullable=true)
    private Integer showCondition ;
    @Column(nullable=true)
    private Integer crossColNum ;
    @Column(nullable=true)
    private String moduleType ;
    @Column(nullable=true)
    private Integer showHead ;
    @Column(nullable=true)
    private Integer showRowNumber ;
    
    @Column(nullable=true)
    private String parentLinkReport;
    
    @Column(nullable=true)
    private String extendsBut;
    
    @Transient
    private boolean isEndClassNumber;   
    
    
	public boolean isEndClassNumber() {
		return isEndClassNumber;
	}

	public void setEndClassNumber(boolean isEndClassNumber) {
		this.isEndClassNumber = isEndClassNumber;
	}

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

	public Integer getDefaultNoshow() {
		return defaultNoshow;
	}

	public void setDefaultNoshow(Integer defaultNoshow) {
		this.defaultNoshow = defaultNoshow;
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

    public Map getReportDetBean() {
        return reportDetBean;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getNewFlag() {
        return newFlag;
    }

    public String getProcName() {
        return ProcName;
    }

    public String getBillTable() {
        return BillTable;
    }

    public String getReportType() {
        return ReportType;
    }

    public String getReportNumber() {
        return ReportNumber;
    }

    public String getSQLFileName() {
        return SQLFileName;
    }

    public String getReportName() {
        return ReportName;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public void setReportDetBean(Map reportDetBean) {
        this.reportDetBean = reportDetBean;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setNewFlag(String newFlag) {
        this.newFlag = newFlag;
    }

    public void setProcName(String ProcName) {
        this.ProcName = ProcName;
    }

    public void setBillTable(String BillTable) {
        this.BillTable = BillTable;
    }

    public void setReportType(String ReportType) {
        this.ReportType = ReportType;
    }

    public void setReportNumber(String ReportNumber) {
        this.ReportNumber = ReportNumber;
    }

    public void setReportName(String ReportName) {
        this.ReportName = ReportName;
    }

    public void setSQLFileName(String SQLFileName) {
        this.SQLFileName = SQLFileName;
    }

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getShowTotalPage() {
		return showTotalPage;
	}

	public void setShowTotalPage(Integer showTotalPage) {
		this.showTotalPage = showTotalPage;
	}

	public Integer getShowTotalStat() {
		return showTotalStat;
	}

	public void setShowTotalStat(Integer showTotalStat) {
		this.showTotalStat = showTotalStat;
	}

	public Integer getAuditPrint() {
		return auditPrint;
	}

	public void setAuditPrint(Integer auditPrint) {
		this.auditPrint = auditPrint;
	}

	public Integer getColTitleSort() {
		return colTitleSort;
	}

	public void setColTitleSort(Integer colTitleSort) {
		this.colTitleSort = colTitleSort;
	}

	public Integer getFixListTitle() {
		return fixListTitle;
	}

	public void setFixListTitle(Integer fixListTitle) {
		this.fixListTitle = fixListTitle;
	}

	public Integer getMainModule() {
		return mainModule;
	}

	public void setMainModule(Integer mainModule) {
		this.mainModule = mainModule;
	}

	public Integer getFixNumberCol() {
		return fixNumberCol;
	}

	public void setFixNumberCol(Integer fixNumberCol) {
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

	public Integer getShowCondition() {
		return showCondition;
	}

	public void setShowCondition(Integer showCondition) {
		this.showCondition = showCondition;
	}

	public Integer getShowDet() {
		return showDet;
	}

	public void setShowDet(Integer showDet) {
		this.showDet = showDet;
	}

	public Integer getCrossColNum() {
		return crossColNum;
	}

	public void setCrossColNum(Integer crossColNum) {
		this.crossColNum = crossColNum;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Integer getShowHead() {
		return showHead;
	}

	public void setShowHead(Integer showHead) {
		this.showHead = showHead;
	}

	public Integer getShowRowNumber() {
		return showRowNumber;
	}

	public void setShowRowNumber(Integer showRowNumber) {
		this.showRowNumber = showRowNumber;
	}
	
}
