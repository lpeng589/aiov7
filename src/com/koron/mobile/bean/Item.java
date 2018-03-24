package com.koron.mobile.bean;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 22, 2012
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
public class Item {

	private String SeqBarCode ;
	private String GoodsCode ;
	private String Qty ;
	private String Price ;
	private String Amount ;
	
	public String getSeqBarCode() {
		return SeqBarCode;
	}
	public void setSeqBarCode(String seqBarCode) {
		SeqBarCode = seqBarCode;
	}
	public String getGoodsCode() {
		return GoodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		GoodsCode = goodsCode;
	}
	public String getQty() {
		return Qty;
	}
	public void setQty(String qty) {
		Qty = qty;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	
}
