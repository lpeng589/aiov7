package com.menyi.aio.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 
 * <p>Title:标签打印bean</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-29
 * @Copyright: 科荣软件
 * @Author 方家俊
 * @preserve all
 */

@Entity
@Table(name = "tblLabel")
public class LabelBean {
	
	@Id
	private String id;
	private String goodsCode;
	private String unit;
	private String seq;
	private String batchNo;
	private String design;
	private String color;
	private String colorBit;
	private Double coil;
	private Double meter;
	private String createBy;
	private String createTime;
	private Double qty;
	private String colorName;
	private String gram;
	private String breadth;
	private String density;
	private String user1;
	private String user2;
	private String trackNo;
	private String procedures;
	
	public Double getCoil() {
		return coil;
	}
	public void setCoil(Double coil) {
		this.coil = coil;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getMeter() {
		return meter;
	}
	public void setMeter(Double meter) {
		this.meter = meter;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	public String getBreadth() {
		return breadth;
	}
	public void setBreadth(String breadth) {
		this.breadth = breadth;
	}
	public String getDensity() {
		return density;
	}
	public void setDensity(String density) {
		this.density = density;
	}
	public String getGram() {
		return gram;
	}
	public void setGram(String gram) {
		this.gram = gram;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getColorBit() {
		return colorBit;
	}
	public void setColorBit(String colorBit) {
		this.colorBit = colorBit;
	}
	public String getDesign() {
		return design;
	}
	public void setDesign(String design) {
		this.design = design;
	}
	public String getProcedures() {
		return procedures;
	}
	public void setProcedures(String procedures) {
		this.procedures = procedures;
	}
	public String getTrackNo() {
		return trackNo;
	}
	public void setTrackNo(String trackNo) {
		this.trackNo = trackNo;
	}
	
	
}
