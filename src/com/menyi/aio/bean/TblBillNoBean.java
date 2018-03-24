package com.menyi.aio.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:单据编号规格</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
@Entity
@Table(name="tblBillNo")
public class TblBillNoBean {
	
	@Id
    private Integer id;						//id
	private String key;						//现在定规则为表名加字段名用下划线连接。例如：tblrole_id
	private String billName;				//单据名称
	private String pattern;					//用来生成帐单号格式的字符串
	private Integer billNO;					//系列号当前已使用数字
	private Integer start;					//起始流水号
	private Integer step;					//流水号步长
	private Boolean isfillback;				//是否实现连号
	private Integer reset;					//重置周期
	private long laststamp;					//上次获取数值的时间
	private String explain;					//用来生成帐单号格式的字符串转换为文字
	private Boolean isAddbeform;			//是否是在添加前添加单据
	private String statusId;				//状态（0显示，-1隐藏）
	private String isDefaultLoginPerson;	//默认经手人（0是，-1否）
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getIsfillback() {
		return isfillback;
	}
	public void setIsfillback(Boolean isfillback) {
		this.isfillback = isfillback;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getLaststamp() {
		return laststamp;
	}
	public void setLaststamp(long laststamp) {
		this.laststamp = laststamp;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public Integer getReset() {
		return reset;
	}
	public void setReset(Integer reset) {
		this.reset = reset;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public Integer getBillNO() {
		return billNO;
	}
	public void setBillNO(Integer billNO) {
		this.billNO = billNO;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public Boolean getIsAddbeform() {
		return isAddbeform;
	}
	public void setIsAddbeform(Boolean isAddbeform) {
		this.isAddbeform = isAddbeform;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getIsDefaultLoginPerson() {
		return isDefaultLoginPerson;
	}
	public void setIsDefaultLoginPerson(String isDefaultLoginPerson) {
		this.isDefaultLoginPerson = isDefaultLoginPerson;
	}
	
}
