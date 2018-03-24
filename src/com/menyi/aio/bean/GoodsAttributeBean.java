package com.menyi.aio.bean;

import java.util.*;

import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name = "tblGoodsAttribute")
public class GoodsAttributeBean extends AbstractEntity {
    @Id
    @Column(nullable = false, length = 30)
    private String id;

    @Transient
    private KRLanguage display;

    private String propName;

    @Column(nullable = false, length = 30)
    private String createBy;

    private String createTime;
    @Column(nullable = false, length = 30)
    private String lastUpdateBy;

    private String lastUpdateTime;
    @Column(nullable = false, length = 4)
    private int isUsed;

    @Column(nullable = false, length = 4)
    private int isCalculate;

    
    @Column(nullable = false, length = 30)
    private String SCompanyID;

    @Column(nullable = true, length = 30)
    private String languageId;
    
    @Column(nullable = true)
    private int orderBy;

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

	public KRLanguage getDisplay() {
		return display;
	}

	public void setDisplay(KRLanguage display) {
		this.display = display;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIsCalculate() {
		return isCalculate;
	}

	public void setIsCalculate(int isCalculate) {
		this.isCalculate = isCalculate;
	}

	public int getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getSCompanyID() {
		return SCompanyID;
	}

	public void setSCompanyID(String companyID) {
		SCompanyID = companyID;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
    
    
}
