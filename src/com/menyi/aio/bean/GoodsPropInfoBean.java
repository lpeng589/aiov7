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
@Table(name = "tblGoodsPropInfo")
public class GoodsPropInfoBean extends AbstractEntity {
    @Id
    @Column(nullable = false, length = 30)
    private String id;

    @Transient
    private KRLanguage display;


    @OneToMany(mappedBy = "propBean", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("enumValue")
    private List<GoodsPropEnumItemBean> enumItem;

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

    @Column(nullable = false, length = 4)
    private int joinTable;

    private String alertName;
    @Column(nullable = false, length = 4)
    private int inputBill;

    @Column(nullable = false, length = 4)
    private int twoUnit;

    @Column(nullable = false, length = 30)
    private String SCompanyID;

    @Column(nullable = true, length = 30)
    private String languageId;
    
    @Column(nullable = false, length = 4)
    private int isSequence;

    @Column(nullable = false, length = 4)
    private int sepAppoint;
    @Column(nullable = false, length = 4)
    private int groupOrNot;
    @Column(nullable = false, length = 4)
    private int nameAndValue;
    
    @Column(nullable = true)
    private int orderBy;
    
    public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	public int getSepAppoint() {
		return sepAppoint;
	}

	public void setSepAppoint(int sepAppoint) {
		this.sepAppoint = sepAppoint;
	}

	public String getId() {
        return id;
    }

    public List getEnumItem() {
        return enumItem;
    }

    public KRLanguage getDisplay() {
        return display;
    }

    public String getPropName() {
        return propName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public int getIsCalculate() {
        return isCalculate;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public int getJoinTable() {
        return joinTable;
    }

    public String getAlertName() {
        return alertName;
    }

    public int getInputBill() {
        return inputBill;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setEnumItem(List enumItem) {
        this.enumItem = enumItem;
    }

    public void setDisplay(KRLanguage display) {
        this.display = display;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }


    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setIsCalculate(int isCalculate) {
        this.isCalculate = isCalculate;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public void setJoinTable(int joinTable) {
        this.joinTable = joinTable;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public void setInputBill(int inputBill) {
        this.inputBill = inputBill;
    }

    public String handerDisplay() {
        return display.toString();
    }

    public String getSCompanyID() {
        return SCompanyID;
    }

    public void setSCompanyID(String companyID) {
        SCompanyID = companyID;
    }

    public int getTwoUnit() {
        return twoUnit;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setTwoUnit(int twoUnit) {
        this.twoUnit = twoUnit;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

	public int getIsSequence() {
		return isSequence;
	}

	public void setIsSequence(int isSequence) {
		this.isSequence = isSequence;
	}

	public int getGroupOrNot() {
		return groupOrNot;
	}

	public void setGroupOrNot(int groupOrNot) {
		this.groupOrNot = groupOrNot;
	}

	public int getNameAndValue() {
		return nameAndValue;
	}

	public void setNameAndValue(int nameAndValue) {
		this.nameAndValue = nameAndValue;
	}



}
