package com.menyi.aio.bean;

public class ApplyGoodsDecBean {

	private String goodsCode;
	private String goodsNo; //商品编号
	private String goodsName; //商品名称
	private int billNumber; //订单数量
	private int stockNumber; // 库存数量
	private int OOSNumber; //缺货数量
	private int companyCondition; //往来查询条件
	
	public ApplyGoodsDecBean(){}
	
	public ApplyGoodsDecBean(String goodsCode,String goodsNo,String goodsName,int billNumber,int stockNumber,
			int OOSNumber,int companyCondition){
		this.goodsCode = goodsCode;
		this.goodsNo = goodsNo;
		this.goodsName = goodsName;
		this.billNumber = billNumber;
		this.stockNumber = stockNumber;
		this.OOSNumber = OOSNumber;
		this.companyCondition = companyCondition;
	}

	public int getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(int billNumber) {
		this.billNumber = billNumber;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public int getCompanyCondition() {
		return companyCondition;
	}

	public void setCompanyCondition(int companyCondition) {
		this.companyCondition = companyCondition;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public int getOOSNumber() {
		return OOSNumber;
	}

	public void setOOSNumber(int number) {
		OOSNumber = number;
	}

	public int getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(int stockNumber) {
		this.stockNumber = stockNumber;
	}
	
	
}
