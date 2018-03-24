package com.koron.openplatform.vos;

import java.io.Serializable;

public class OrderProduct implements Serializable{
	String productId;
	String price;
	String total;
	String pid;
	
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "OrderProduct [productId=" + productId + ", price=" + price
				+ ", total=" + total + ", pid=" + pid + "]";
	}

}
