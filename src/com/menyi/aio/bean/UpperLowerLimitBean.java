package com.menyi.aio.bean;

public class UpperLowerLimitBean {
	private String id;
    private int upperLimit;
    private int lowerLimit;
    private String GoodsCode;
    private String StockCode;
    private String BatchNo;
    private String Hue;
    private String yearNo;
    private String Inch;
    

    
	public String getBatchNo() {
		return BatchNo;
	}
	public void setBatchNo(String batchNo) {
		BatchNo = batchNo;
	}

	public String getHue() {
		return Hue;
	}
	public void setHue(String hue) {
		Hue = hue;
	}
	public String getInch() {
		return Inch;
	}
	public void setInch(String inch) {
		Inch = inch;
	}

	public String getYearNo() {
		return yearNo;
	}
	public void setYearNo(String yearNo) {
		this.yearNo = yearNo;
	}
	

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(int lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	
	public String getGoodsCode() {
		return GoodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		GoodsCode = goodsCode;
	}
	public String getStockCode() {
		return StockCode;
	}
	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}
	public int getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(int upperLimit) {
		this.upperLimit = upperLimit;
	}

}
