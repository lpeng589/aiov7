package com.menyi.aio.bean;

import javax.persistence.*;

/**
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
@Table(name = "tblAccPeriod")
public class AccPeriodBean {
    public AccPeriodBean() {
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getId() {
        return id;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getSCompanyID() {
        return SCompanyID;
    }

    public int getAccMonth() {
        return AccMonth;
    }

    public int getAccPeriod() {
        return AccPeriod;
    }

    public int getAccYear() {
        return AccYear;
    }

    public int getIsBegin() {
        return IsBegin;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setSCompanyID(String SCompanyID) {
        this.SCompanyID = SCompanyID;
    }

    public void setAccYear(int AccYear) {
        this.AccYear = AccYear;
    }

    public void setAccPeriod(int AccPeriod) {
        this.AccPeriod = AccPeriod;
    }

    public void setAccMonth(int AccMonth) {
        this.AccMonth = AccMonth;
    }

    public void setIsBegin(int IsBegin) {
        this.IsBegin = IsBegin;
    }
    public String getPeriodBegin() {
		return periodBegin;
	}

	public void setPeriodBegin(String periodBegin) {
		this.periodBegin = periodBegin;
	}

	public String getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}

    @Id
    @Column(nullable = false, length = 30)
    private String id;

    @Column(nullable = true)
    private int AccYear;

    @Column(nullable = true)
    private int AccMonth;

    @Column(nullable = true)
    private int AccPeriod;

    @Column(nullable = true)
    private int statusId;
    
    @Column(nullable = true)
    private int AccOverYear;

    @Column(nullable = true)
    private int IsBegin;

    @Column(nullable = true, length = 30)
    private String createBy;

    @Column(nullable = true, length = 30)
    private String lastUpdateBy;

    @Column(nullable = true, length = 19)
    private String createTime;

    @Column(nullable = true, length = 19)
    private String lastUpdateTime;

    @Column(nullable = false, length = 50)
    private String SCompanyID;
    @Column(nullable = true, length = 10)
    private String periodBegin;
    @Column(nullable = true, length = 10)
    private String periodEnd;
	public int getAccOverYear() {
		return AccOverYear;
	}

	public void setAccOverYear(int accOverYear) {
		AccOverYear = accOverYear;
	}
	
}
