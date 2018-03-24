package com.menyi.aio.bean;

import java.util.List;

import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;


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
@Table(name="tblDBEnumeration")
public class EnumerateBean  extends AbstractEntity {
    @Id
    @Column(nullable = false, length = 30)
    private String id;

    @Transient
    private KRLanguage display;


    @OneToMany(mappedBy="enumerateBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("enumOrder,enumValue")
    private List<EnumerateItemBean> enumItem;

    private String enumName;

    @Column(nullable = true, length = 30)
    private String languageId;
    
    @Column(nullable = true)
    private int mainModule;

    @Column(nullable = false, length = 30)
    private String createBy;
    private String createTime;
    @Column(nullable = false, length = 30)
    private String lastUpdateBy;
    private String lastUpdateTime;


    public String getId() {
        return id;
    }

    public List getEnumItem() {
        return enumItem;
    }

    public KRLanguage getDisplay() {
        return display;
    }

    public String getEnumName() {
        return enumName;
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

    public String getLanguageId() {
        return languageId;
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

    public void setEnumName(String enumName) {
        this.enumName = enumName;
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

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String handerDisplay(){
        return display.toString();
    }

	public int getMainModule() {
		return mainModule;
	}

	public void setMainModule(int mainModule) {
		this.mainModule = mainModule;
	}

}
