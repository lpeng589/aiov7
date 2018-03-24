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
@Table(name = "tblStock")
public class StockBean {

    public StockBean() {
    }

    public String getId() {
        return id;
    }

    public String getStockNumber() {
        return StockNumber;
    }

    public String getStockFullName() {
        return StockFullName;
    }

    public String getStockCode() {
        return StockCode;
    }

    public void setStockNumber(String StockNumber) {
        this.StockNumber = StockNumber;
    }

    public void setStockFullName(String StockFullName) {
        this.StockFullName = StockFullName;
    }

    public void setStockCode(String StockCode) {
        this.StockCode = StockCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;
    @Column(nullable = false,length=50 )
    private String  StockCode;
    @Column(nullable = false,length=256 )
    private String  StockFullName;
    @Column(nullable = false,length=256 )
    private String  StockNumber;
}
