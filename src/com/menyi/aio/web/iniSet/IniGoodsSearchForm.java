package com.menyi.aio.web.iniSet;

import com.menyi.web.util.BaseSearchForm;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class IniGoodsSearchForm extends BaseSearchForm {
    private String id;
    private String goodsNumber;
    private String goodsFullName ;
    private String stockName;
    private String stockCode;
    private String storeStateID;
    private float iniQty;
    private float iniPrice;
    private float iniAmount;
    private String dimQuery;

    private String goodsCode;

    public String getGoodsFullName() {
        return goodsFullName;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }


    public float getIniAmount() {
        return iniAmount;
    }

    public float getIniPrice() {
        return iniPrice;
    }

    public float getIniQty() {
        return iniQty;
    }

    public String getStoreStateID() {
        return storeStateID;
    }

    public String getDimQuery() {
        return dimQuery;
    }

    public String getId() {
        return id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setStoreStateID(String storeStateID) {
        this.storeStateID = storeStateID;
    }

    public void setIniQty(float iniQty) {
        this.iniQty = iniQty;
    }

    public void setIniPrice(float iniPrice) {
        this.iniPrice = iniPrice;
    }

    public void setIniAmount(float iniAmount) {
        this.iniAmount = iniAmount;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public void setGoodsFullName(String goodsFullName) {
        this.goodsFullName = goodsFullName;
    }

    public void setDimQuery(String dimQuery) {
        this.dimQuery = dimQuery;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

}
