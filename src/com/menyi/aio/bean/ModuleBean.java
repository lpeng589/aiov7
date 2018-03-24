package com.menyi.aio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;


/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 * @preserve all
 */

@Entity
@Table(name="tblModules")
public class ModuleBean  extends AbstractEntity implements Serializable {

    @Id
    @Column(nullable = false,length=30)
    private String id;

    @OneToMany(mappedBy="moduleBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ModuleOperationBean> moduleoperationinfo;

    @Column(nullable=true,length=50)
    private String classCode;
    @Column(nullable=true,length=50,name="modelName")
    private String modelId;
    @Column(nullable=true,length=50)
    private String mainModule;
    @Column(nullable=true,length=50)
    private String tblName;
    @Column(nullable=true,length=250)
    private String linkAddress = "";
    @Column(nullable=true,length=30)
    private String createBy;
    @Column(nullable=true,length=19)
    private String createTime;
    @Column(nullable=true,length=30)
    private String lastUpdateBy;
    @Column(nullable=true,length=19)
    private String lastUpdateTime;
    @Column(nullable=true)
    private String isCatalog ;
    @Column(nullable=true)
    private int orderBy;
    private int isUsed;
    private int statusId;
    private int isHidden;
    private int isDisplay;
    
    @Transient
    private ModuleBean parentModuleBean;
    @Transient
    private ArrayList childList;
    @Transient
    private KRLanguage modelDisplay;
    
    private Byte isMobile;
    private String icon;

    

    public Byte getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(Byte isMobile) {
		this.isMobile = isMobile;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getClassCode() {
        return classCode;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getId() {
        return id;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getModelId() {

        return modelId;
    }

    public List getModuleoperationinfo() {
        return moduleoperationinfo;
    }

    public String getTblName() {
        return tblName;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public int getStatusId() {
        return statusId;
    }

    public ArrayList getChildList() {
        return childList;
    }

    public ModuleBean getParentModuleBean() {
        return parentModuleBean;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public String getMainModule() {
        return mainModule;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public KRLanguage getModelDisplay() {

        return modelDisplay;
    }


    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(String createDateTime) {
        this.createTime = createDateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public void setLastUpdateBy(String lstUpdateBy) {
        this.lastUpdateBy = lstUpdateBy;
    }

    public void setLastUpdateTime(String lstUpdateTime) {
        this.lastUpdateTime = lstUpdateTime;
    }

    public void setModelId(String modelId) {

        this.modelId = modelId;
    }

    public void setModuleoperationinfo(List moduleoperationinfo) {
        this.moduleoperationinfo = moduleoperationinfo;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setChildList(ArrayList childList) {
        this.childList = childList;
    }

    public void setParentModuleBean(ModuleBean parentModuleBean) {
        this.parentModuleBean = parentModuleBean;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public void setMainModule(String mainModule) {
        this.mainModule = mainModule;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    public void setModelDisplay(KRLanguage modelDisplay) {

        this.modelDisplay = modelDisplay;
    }

	public String getIsCatalog() {
		return isCatalog;
	}

	public void setIsCatalog(String isCatalog) {
		this.isCatalog = isCatalog;
	}
    
}
