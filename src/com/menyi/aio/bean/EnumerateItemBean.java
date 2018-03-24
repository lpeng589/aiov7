package com.menyi.aio.bean;

import javax.persistence.*;



/**
 *
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
@Table(name="tblDBEnumerationItem")
public class EnumerateItemBean {
    @Id
    private String id;

    @Column(nullable=false,length=100)
    private String enumValue;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="enumId")
    private EnumerateBean enumerateBean;

    @Column(nullable = true, length = 30)
    private String languageId;

    @Column(nullable = true)
    private int enumOrder ;
    
    @Transient
    private KRLanguage display;

    public KRLanguage getDisplay() {
        return display;
    }

    public String getEnumValue() {
        return enumValue;
    }


    public String getId() {
        return id;
    }

    public EnumerateBean getEnumerateBean() {
        return enumerateBean;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public void setDisplay(KRLanguage display) {
        this.display = display;
    }

    public void setEnumerateBean(EnumerateBean enumerateBean) {
        this.enumerateBean = enumerateBean;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String handerDisplay() {
        return display.toString();
    }
	public int getEnumOrder() {
		return enumOrder;
	}

	public void setEnumOrder(int enumOrder) {
		this.enumOrder = enumOrder;
	}
}
