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
@Table(name = "tblModuleFlowDet")
public class ModuleFlowDetBean {

    public ModuleFlowDetBean()
    {
    }
    
    public ModuleFlowDetBean(String ModuleItemName,String Link){
    	this.ModuleItemName = ModuleItemName;
    	this.Link = Link;
    }

    public String getF_ref() {
        return f_ref;
    }

    public String getId() {
        return id;
    }

    public int getRow() {
        return Row;
    }

    public String getModuleItemName() {
        return ModuleItemName;
    }

    public int getListOrder() {
        return ListOrder;
    }

    public String getLink() {
        return Link;
    }

    public String getIconName() {
        return IconName;
    }

    public void setF_ref(String f_ref) {
        this.f_ref = f_ref;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRow(int Row) {
        this.Row = Row;
    }

    public void setModuleItemName(String ModuleItemName) {
        this.ModuleItemName = ModuleItemName;
    }

    public void setListOrder(int ListOrder) {
        this.ListOrder = ListOrder;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }

    public void setIconName(String IconName) {
        this.IconName = IconName;
    }


    /** identifier field */
    @Id
    @Column(nullable = false,length=30 )
    private String id;
    /** persistent field */
    @Column(nullable = true,length=30 )
    private String f_ref;
    @Column(nullable = true)
    private int ListOrder;
    @Column(nullable = true)
    private int Row;
    @Column(nullable = true,length=200)
    private String IconName;
    @Column(nullable = true,length=200)
    private String Link;
    @Column(nullable= true,length=200)
    private String ModuleItemName;

}
