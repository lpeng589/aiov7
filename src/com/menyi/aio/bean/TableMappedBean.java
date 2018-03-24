package com.menyi.aio.bean;

import java.io.Serializable;
import javax.persistence.*;

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
@Table(name="tblTableMapped")
public class TableMappedBean implements Serializable {
    @Id
    private String id;
    @Column(nullable = true )
    private String mostlyTable;
    @Column(nullable = true )
    private String mostlyTableField;
    @Column(nullable = true )
    private String childTable;
    @Column(nullable = true )
    private String childTableField;
    @Column(nullable = false )
    private String createBy;
    @Column(nullable = false )
    private String lastUpdateBy;
    @Column(nullable = false )
    private String createTime;
    @Column(nullable = false )
    private String lastUpdateTime;
    @Column(nullable = false )
    private String scompanyId;

    public String getScompanyId() {
		return scompanyId;
	}

	public void setScompanyId(String scompanyId) {
		this.scompanyId = scompanyId;
	}

	public TableMappedBean() {
    }

    public TableMappedBean(String id,String mostlyTable,String mostlyTableField,
                            String childTable,String childTableField){

        this.id = id;
        this.mostlyTable = mostlyTable;
        this.mostlyTableField = mostlyTableField;
        this.childTable = childTable;
        this.childTableField = childTableField;
    }

    public TableMappedBean(String id, String mostlyTable, String mostlyTableField,
             String childTable, String childTableField, String createBy,
             String lastUpdateBy, String createTime, String lastUpdateTime,String scompanyId){

        this.id = id;
        this.mostlyTable = mostlyTable;
        this.mostlyTableField = mostlyTableField;
        this.childTable = childTable;
        this.childTableField = childTableField;
        this.createBy = createBy;
        this.lastUpdateBy = lastUpdateBy;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
        this.scompanyId=scompanyId;
    }

    public String getChildTable() {
        return childTable;
    }

    public String getChildTableField() {
        return childTableField;
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

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getMostlyTable() {
        return mostlyTable;
    }

    public String getMostlyTableField() {
        return mostlyTableField;
    }

    public void setChildTable(String childTable) {
        this.childTable = childTable;
    }

    public void setChildTableField(String childTableField) {
        this.childTableField = childTableField;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setMostlyTable(String mostlyTable) {
        this.mostlyTable = mostlyTable;
    }

    public void setMostlyTableField(String mostlyTableField) {
        this.mostlyTableField = mostlyTableField;
    }
}
