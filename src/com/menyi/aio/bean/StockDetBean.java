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
@Table(name = "tblStockDet")
public class StockDetBean {
    public StockDetBean() {
    }

    public String getGoodsCode() {
        return GoodsCode;
    }

    public String getId() {
        return id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public String getStoreState() {
        return storeState;
    }

    public int getPeriodYear() {
        return periodYear;
    }

    public int getPeriodMonth() {
        return periodMonth;
    }

    public int getPeriod() {
        return period;
    }

    public int getItemNo() {
        return itemNo;
    }

    public String getSourceID() {
        return SourceID;
    }

    public Double getOutstoreQty() {
        return OutstoreQty;
    }

    public Double getOutstorePrice() {
        return OutstorePrice;
    }

    public Double getOutstoreAmount() {
        return OutstoreAmount;
    }

    public Double getInstoreQty() {
        return InstoreQty;
    }

    public Double getInstorePrice() {
        return InstorePrice;
    }

    public Double getInstoreAmount() {
        return InstoreAmount;
    }

    public String getBillType() {
        return BillType;
    }

    public String getBillID() {
        return BillID;
    }

    public String getBillDate() {
        return BillDate;
    }

    public Double getLastPrice() {
        return LastPrice;
    }

    public Double getTotalQty() {
        return TotalQty;
    }

    public Double getTotalAmt() {
        return TotalAmt;
    }

    public Double getIniQty() {
        return IniQty;
    }

    public Double getIniPrice() {
        return IniPrice;
    }

    public Double getIniAmount() {
        return IniAmount;
    }

    public void setGoodsCode(String GoodsCode) {
        this.GoodsCode = GoodsCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setPeriodMonth(int periodMonth) {
        this.periodMonth = periodMonth;
    }

    public void setPeriodYear(int periodYear) {
        this.periodYear = periodYear;
    }

    public void setSourceID(String SourceID) {
        this.SourceID = SourceID;
    }

    public void setOutstoreQty(Double OutstoreQty) {
        this.OutstoreQty = OutstoreQty;
    }

    public void setOutstorePrice(Double OutstorePrice) {
        this.OutstorePrice = OutstorePrice;
    }

    public void setOutstoreAmount(Double OutstoreAmount) {
        this.OutstoreAmount = OutstoreAmount;
    }

    public void setInstoreQty(Double InstoreQty) {
        this.InstoreQty = InstoreQty;
    }

    public void setInstorePrice(Double InstorePrice) {
        this.InstorePrice = InstorePrice;
    }

    public void setInstoreAmount(Double InstoreAmount) {
        this.InstoreAmount = InstoreAmount;
    }

    public void setBillType(String BillType) {
        this.BillType = BillType;
    }

    public void setBillID(String BillID) {
        this.BillID = BillID;
    }

    public void setBillDate(String BillDate) {
        this.BillDate = BillDate;
    }

    public void setLastPrice(Double LastPrice) {
        this.LastPrice = LastPrice;
    }

    public void setTotalAmt(Double TotalAmt) {
        this.TotalAmt = TotalAmt;
    }

    public void setTotalQty(Double TotalQty) {
        this.TotalQty = TotalQty;
    }

    public void setIniQty(Double IniQty) {
        this.IniQty = IniQty;
    }

    public void setIniPrice(Double IniPrice) {
        this.IniPrice = IniPrice;
    }

    public void setIniAmount(Double IniAmount) {
        this.IniAmount = IniAmount;
    }

    @Id
    @Column(nullable = false,length=30 )
    private String id;
    @Column(nullable = true )
    private String GoodsCode;
    @Column(nullable = true )
    private Double   IniQty;
    @Column(nullable = true )
    private Double   IniPrice;
    @Column(nullable = true )
    private Double   IniAmount;

    private int period;
    private int periodYear;
    private int periodMonth;
    @Column(nullable = true,length=50 )
    private String stockCode;
    private String storeState;
    private Double TotalQty;
    private Double TotalAmt;
    private Double LastPrice;
    private int itemNo;
    private Double InstoreQty;
    private Double OutstoreQty;
    private Double InstorePrice;
    private Double OutstorePrice;
    private Double InstoreAmount;
    private Double OutstoreAmount;
    @Column(nullable = true,length=50 )
    private String BillType;
    @Column(nullable = true,length=30 )
    private String BillID;
    @Column(nullable = true,length=10 )
    private String BillDate;
    @Column(nullable = true,length=30 )
    private String SourceID;
    @Column(nullable = true,length=30 )
    private String BillNo;

    private String goodHashNoSeq;
    
    private String goodHash;
    private String costComSign;
    private String createTime;
    private String goodHashNoStock;
    private String batchNO;
    
    private int orderNo;

    private int itemOrder;
    
    
	public String getBatchNO() {
		return batchNO;
	}

	public void setBatchNO(String batchNO) {
		this.batchNO = batchNO;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getGoodHash() {
		return goodHash;
	}

	public void setGoodHash(String allProp) {
		this.goodHash = allProp;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getGoodHashNoSeq() {
		return goodHashNoSeq;
	}

	public void setGoodHashNoSeq(String prop) {
		this.goodHashNoSeq = prop;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCostComSign() {
		return costComSign;
	}

	public void setCostComSign(String costComSign) {
		this.costComSign = costComSign;
	}

	public String getBillNo() {
		return BillNo;
	}

	public void setBillNo(String billNo) {
		BillNo = billNo;
	}

	public String getGoodHashNoStock() {
		return goodHashNoStock;
	}

	public void setGoodHashNoStock(String allPropNoStock) {
		this.goodHashNoStock = allPropNoStock;
	}
}
