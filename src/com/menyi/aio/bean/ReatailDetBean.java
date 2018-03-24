package com.menyi.aio.bean;

public class ReatailDetBean {

	private String id ;			/*单据编号*/
	private String orderId ;	/*编号*/
	private String goodsCode ;	/*商品编号*/
	private String qty ;		/*销售数量*/
	private String price ;		/*单价*/
	private String money ;		/*金额*/
	private String discount ;	/*折扣*/
	private String discountPrice ;	/*折扣单价*/
	private String discountMoney ;	/*折扣金额*/
	private String taxPrice ;		/*含税单价*/
	private String taxMoney ;		/*含税金额*/
	private String createDate ;		/*日期*/
	private String batchNo ;
	private String yearNO ;
	private String color ;
	private String seq ;
	private String inch ;
	private String hue ;
	private String proDate ;		/*生产日期*/
	private String availably;		/*保持期限*/
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getDiscountMoney() {
		return discountMoney;
	}
	public void setDiscountMoney(String discountMoney) {
		this.discountMoney = discountMoney;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getYearNO() {
		return yearNO;
	}
	public void setYearNO(String yearNO) {
		this.yearNO = yearNO;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getInch() {
		return inch;
	}
	public void setInch(String inch) {
		this.inch = inch;
	}
	public String getHue() {
		return hue;
	}
	public void setHue(String hue) {
		this.hue = hue;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice;
	}
	public String getTaxMoney() {
		return taxMoney;
	}
	public void setTaxMoney(String taxMoney) {
		this.taxMoney = taxMoney;
	}
	public String getProDate() {
		return proDate;
	}
	public void setProDate(String proDate) {
		this.proDate = proDate;
	}
	public String getAvailably() {
		return availably;
	}
	public void setAvailably(String availably) {
		this.availably = availably;
	}
	
}
