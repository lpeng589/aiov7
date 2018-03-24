package com.menyi.aio.bean;

import javax.persistence.*;

/**
 * 
 * <p>Title:期初调汇历史表Bean</p> 
 * <p>Description: </p>
 * @Date: 2013-04-03
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
@Entity
@Table(name="tblExchangeHistory")
public class ExchangeHistoryBean {

	@Id
	private String id;								//id
	private String date;							//日期
	private Integer periodYear;						//会计期间年
	private Integer periodMonth;					//会计期间月
	private String attachItem; 						//科目编号
	private Double FCMarg;					//外币差额
	private Double FCAmount;						//原币余额
	private Double amount;							//本位币差额
	private Double rate;							//调整前汇率
	private Double adjustmentRate;					//调整后汇率
	private String createBy;						//创建者
	private String createTime;						//创建时间
	private Integer jdFlag;							//借贷方向
	private Integer period;							//会计期间
	private String currency;							//币种名称
	
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getAdjustmentRate() {
		return adjustmentRate;
	}
	public void setAdjustmentRate(Double adjustmentRate) {
		this.adjustmentRate = adjustmentRate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getAttachItem() {
		return attachItem;
	}
	public void setAttachItem(String attachItem) {
		this.attachItem = attachItem;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getPeriodMonth() {
		return periodMonth;
	}
	public void setPeriodMonth(Integer periodMonth) {
		this.periodMonth = periodMonth;
	}
	public Integer getPeriodYear() {
		return periodYear;
	}
	public void setPeriodYear(Integer periodYear) {
		this.periodYear = periodYear;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Integer getJdFlag() {
		return jdFlag;
	}
	public void setJdFlag(Integer jdFlag) {
		this.jdFlag = jdFlag;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Double getFCAmount() {
		return FCAmount;
	}
	public void setFCAmount(Double amount) {
		FCAmount = amount;
	}

	public Double getFCMarg() {
		return FCMarg;
	}
	public void setFCMarg(Double marg) {
		FCMarg = marg;
	}
	
	
}
