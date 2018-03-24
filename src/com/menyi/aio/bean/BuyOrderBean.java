package com.menyi.aio.bean;

public class BuyOrderBean {

	private String companyCode; //������λ
	private String goodsCode; //��Ʒ��
	private int qty; //ȱ������
	private String unit; //��λ��
	private double price; //�۸�
	
	public BuyOrderBean(){}
	
	public BuyOrderBean(String companyCode,String goodsCode,int qty,
			String unit,double price){
		this.companyCode = companyCode;
		this.goodsCode = goodsCode;
		this.qty = qty;
		this.unit = unit;
		this.price = price;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}
	
	
}
