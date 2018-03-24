package com.menyi.aio.bean;

import java.io.Serializable;

import javax.persistence.*;

/**
 * <p>Title: 币种管理的Model-Bean层</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 王强</p>
 *
 * @author 王强
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name="tblSetExchange")
public class SetExchangeBean implements Serializable {

    /** identifier field */
    @Id
    @Column(nullable = false, length = 30)
    private String id;
    /** persistent field */
    @Column(nullable = false, length = 50)
    private String CurrencyName;

    @Column(nullable = false ,length=100)
    private String CurrencySign;

    @Column(nullable = false ,length=30)
    private String Currency;

    @Column(nullable = false ,length=30)
    private String SCompanyID;

    @Column(nullable = false)
    private Integer Period;

    @Column(nullable = false)
    private Integer PeriodYear;
    
    @Column(nullable = true)
    private float RecordExchange;

    @Column(nullable = true)
    private float AdjustExchange;

    @Column(nullable = true ,length=225)
    private String Remark;

    @Column(nullable = true,length=30 )
    private String createBy;

    @Column(nullable = true,length=30 )
    private String lastUpdateBy;

    @Column(nullable = true,length=19 )
    private String createTime;
    @Column(nullable = true,length=19 )
    private String lastUpdateTime;

    public float getAdjustExchange() {
        return AdjustExchange;
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

    public String getSCompanyID() {
        return SCompanyID;
    }

    public String getRemark() {
        return Remark;
    }

    public float getRecordExchange() {
        return RecordExchange;
    }

    public Integer getPeriod() {
        return Period;
    }

    public String getCurrencySign() {
        return CurrencySign;
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setSCompanyID(String SCompanyID) {
        this.SCompanyID = SCompanyID;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public void setRecordExchange(float RecordExchange) {
        this.RecordExchange = RecordExchange;
    }

    public void setPeriod(Integer Period) {
        this.Period = Period;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
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

    public void setAdjustExchange(float AdjustExchange) {
        this.AdjustExchange = AdjustExchange;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public void setCurrencyName(String CurrencyName) {
        this.CurrencyName = CurrencyName;
    }

    public void setCurrencySign(String CurrencySign) {
        this.CurrencySign = CurrencySign;
    }

	public Integer getPeriodYear() {
		return PeriodYear;
	}

	public void setPeriodYear(Integer periodYear) {
		PeriodYear = periodYear;
	}


}
