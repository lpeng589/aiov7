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
@Table(name="tblCurrency")
public class CurrencyBean implements Serializable {

    /** identifier field */
    @Id
    @Column(nullable = false, length = 30)
    private String id;
    /** persistent field */
    @Column(nullable = false, length = 50)
    private String CurrencyName;

    @Column(nullable = false)
    private byte IsBaseCurrency;

    @Column(nullable = false ,length=100)
    private String CurrencySign;

    @Column(nullable = false ,length=30)
    private String SCompanyID;

    @Column(nullable = true, length = 256 )
    private String Remark;

    @Column(nullable = true,length=30 )
    private String createBy;

    @Column(nullable = true,length=30 )
    private String lastUpdateBy;

    @Column(nullable = true,length=19 )
    private String createTime;
    @Column(nullable = true,length=19 )
    private String lastUpdateTime;

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

    public byte getIsBaseCurrency() {
        return IsBaseCurrency;
    }

    public String getCurrencySign() {
        return CurrencySign;
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    public void setSCompanyID(String SCompanyID) {
        this.SCompanyID = SCompanyID;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
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

    public void setCurrencyName(String CurrencyName) {
        this.CurrencyName = CurrencyName;
    }

    public void setCurrencySign(String CurrencySign) {
        this.CurrencySign = CurrencySign;
    }

    public void setIsBaseCurrency(byte IsBaseCurrency) {
        this.IsBaseCurrency = IsBaseCurrency;
    }


}
