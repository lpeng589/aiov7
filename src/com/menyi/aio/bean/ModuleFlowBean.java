package com.menyi.aio.bean;

import javax.persistence.*;

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
@Table(name = "tblModuleFlow")
public class ModuleFlowBean {

    public ModuleFlowBean()
    {
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setMainModule(String MainModule) {
        this.MainModule = MainModule;
    }

    public void setModuleName(String ModuleName) {
        this.ModuleName = ModuleName;
    }

    public void setListOrder(int ListOrder) {
        this.ListOrder = ListOrder;
    }

    public void setLinkAddress(String LinkAddress) {
        this.LinkAddress = LinkAddress;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public int getListOrder() {
        return ListOrder;
    }

    public String getLinkAddress() {
        return LinkAddress;
    }

    public String getId() {
        return id;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getMainModule() {
        return MainModule;
    }

    /** identifier field */
    @Id
    @Column(nullable = false,length=30 )
    private String id;
    /** persistent field */
    @Column(nullable = true,length=100 )
    private String ModuleName;
    @Column(nullable = true)
    private int ListOrder;
    @Column(nullable = true,length=100)
    private String Icon;
    @Column(nullable = true,length=100)
    private String LinkAddress;
    @Column(nullable = true)
    private Integer statusId;
    @Column(nullable = true)
    private String MainModule;
    @Column(nullable = true)
    private String createBy;

}
