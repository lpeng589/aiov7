package com.menyi.aio.bean;

import java.util.List;

public class ReatailBean {

	private String id ;			/*���ݱ��*/
	private String cardNO ;		/*Vip�����*/
	private String cardType ;	/*������*/
	private String money ;		/*�ϼƽ��*/
	private String discountMoney ;	/*���ѻ���*/
	private String accountMoney ;	/*�˻����*/
	private String consumeIntegral ;	/*���۽��*/
	private String playMoney ;		/*������*/
	private String outMoney ;		/*������*/
	private String DiscountAmount; 	/*�Żݽ��*/
	private String employeeId ;		/*����Ա���*/
	private String shopId ;			/*�ŵ���*/
	private String createDate ;		/*��������*/
	private String sendFlag ;		/*�Ƿ��ϴ�*/
	private String tax ;
	private String taxMoney ;		
	private String salesperson ;	/*������Ա*/
	private String companyCode ;    /*������λ*/
	private String stockCode ;		/*�ֿ�*/
	private String terminal ;		/*�ն˺�*/
	private String remark;			/*��ע*/
	private List<ReatailDetBean> listDet ;	
	private List<ReatailAccountBean> accountList ;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCardNO() {
		return cardNO;
	}
	public void setCardNO(String cardNO) {
		this.cardNO = cardNO;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDiscountMoney() {
		return discountMoney;
	}
	public void setDiscountMoney(String discountMoney) {
		this.discountMoney = discountMoney;
	}
	public String getAccountMoney() {
		return accountMoney;
	}
	public void setAccountMoney(String accountMoney) {
		this.accountMoney = accountMoney;
	}
	public String getConsumeIntegral() {
		return consumeIntegral;
	}
	public void setConsumeIntegral(String consumeIntegral) {
		this.consumeIntegral = consumeIntegral;
	}
	public String getPlayMoney() {
		return playMoney;
	}
	public void setPlayMoney(String playMoney) {
		this.playMoney = playMoney;
	}
	public String getOutMoney() {
		return outMoney;
	}
	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	public List<ReatailDetBean> getListDet() {
		return listDet;
	}
	public void setListDet(List<ReatailDetBean> listDet) {
		this.listDet = listDet;
	}
	public List<ReatailAccountBean> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<ReatailAccountBean> accountList) {
		this.accountList = accountList;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getTaxMoney() {
		return taxMoney;
	}
	public void setTaxMoney(String taxMoney) {
		this.taxMoney = taxMoney;
	}
	public String getSalesperson() {
		return salesperson;
	}
	public void setSalesperson(String salesperson) {
		this.salesperson = salesperson;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getDiscountAmount() {
		return DiscountAmount;
	}
	public void setDiscountAmount(String discountAmount) {
		DiscountAmount = discountAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
