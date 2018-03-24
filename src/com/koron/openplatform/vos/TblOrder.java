 package com.koron.openplatform.vos;

import java.util.ArrayList;
import java.util.List;
/* *
 *类名：TblOrder
 *作者：方志文
 *功能：订单基础实体类
 *详细：设置京东淘宝QQ等订单的数据封装
 *版本：1.0
 *日期：2013-07-19
 *说明：
 *以下代码字段等 有些可能没有使用到。该字段都是根据chinagift项目的实际情况而编写成的字段。
 */
public class TblOrder implements java.io.Serializable{
	Integer id;
	String orderID;
	String orderTime;
	String payMethod;
	String deliverMethod;
	String favourableMoney;
	String deliverPay;
	String pay;
	String amountMoney;
	String nickName;
	String phoneNum;
	String mobile;
	String address;
	String leaveWord;
	String cerCode;
	String orderStatus;
	String inSign;
	String hideSign;
	String remark;
	String receiver;
	String oPeople;
	String oAddress;
	String oMobile;
	String oPhone;
	String post;
	String province;
	String city;
	String section;
	
	
	List<OrderProduct> products = new ArrayList<OrderProduct>();
	
	
	
	public String getoPeople() {
		return oPeople;
	}
	public void setoPeople(String oPeople) {
		this.oPeople = oPeople;
	}
	public String getoAddress() {
		return oAddress;
	}
	public void setoAddress(String oAddress) {
		this.oAddress = oAddress;
	}
	public String getoMobile() {
		return oMobile;
	}
	public void setoMobile(String oMobile) {
		this.oMobile = oMobile;
	}
	public String getoPhone() {
		return oPhone;
	}
	public void setoPhone(String oPhone) {
		this.oPhone = oPhone;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public boolean addProuct(OrderProduct e) {
		return products.add(e);
	}
	public boolean removeProduct(Object o) {
		return products.remove(o);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getDeliverMethod() {
		return deliverMethod;
	}
	public void setDeliverMethod(String deliverMethod) {
		this.deliverMethod = deliverMethod;
	}
	public String getFavourableMoney() {
		return favourableMoney;
	}
	public void setFavourableMoney(String favourableMoney) {
		this.favourableMoney = favourableMoney;
	}
	public String getDeliverPay() {
		return deliverPay;
	}
	public void setDeliverPay(String deliverPay) {
		this.deliverPay = deliverPay;
	}
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}
	public String getAmountMoney() {
		return amountMoney;
	}
	public void setAmountMoney(String amountMoney) {
		this.amountMoney = amountMoney;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLeaveWord() {
		return leaveWord;
	}
	public void setLeaveWord(String leaveWord) {
		this.leaveWord = leaveWord;
	}
	public String getCerCode() {
		return cerCode;
	}
	public void setCerCode(String cerCode) {
		this.cerCode = cerCode;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getInSign() {
		return inSign;
	}
	public void setInSign(String inSign) {
		this.inSign = inSign;
	}
	public String getHideSign() {
		return hideSign;
	}
	public void setHideSign(String hideSign) {
		this.hideSign = hideSign;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<OrderProduct> getProducts() {
		return products;
	}
	public void setProducts(List<OrderProduct> products) {
		this.products = products;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	@Override
	public String toString() {
		return "TblOrder [id=" + id + ", orderID=" + orderID + ", orderTime="
				+ orderTime + ", payMethod=" + payMethod + ", deliverMethod="
				+ deliverMethod + ", favourableMoney=" + favourableMoney
				+ ", deliverPay=" + deliverPay + ", pay=" + pay
				+ ", amountMoney=" + amountMoney + ", nickName=" + nickName
				+ ", phoneNum=" + phoneNum + ", mobile=" + mobile
				+ ", address=" + address + ", leaveWord=" + leaveWord
				+ ", cerCode=" + cerCode + ", orderStatus=" + orderStatus
				+ ", inSign=" + inSign + ", hideSign=" + hideSign + ", remark="
				+ remark + ", receiver=" + receiver + ", oPeople=" + oPeople
				+ ", oAddress=" + oAddress + ", oMobile=" + oMobile
				+ ", oPhone=" + oPhone + ", post=" + post + ", province="
				+ province + ", city=" + city + ", section=" + section
				+ ", products=" + products + "]";
	}
}
