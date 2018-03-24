package com.menyi.aio.web.tablemapped;

import com.menyi.web.util.BaseForm;

/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class TableMappedForm extends BaseForm {

    private String  mostlyTable;
    private String  mostlyTableField;
    private String childTable;
    private String childTableField;
    public TableMappedForm() {
    }

    public String getChildTable() {
        return childTable;
    }

    public String getChildTableField() {
        return childTableField;
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

    public void setMostlyTable(String mostlyTable) {
        this.mostlyTable = mostlyTable;
    }

    public void setMostlyTableField(String mostlyTableField) {
        this.mostlyTableField = mostlyTableField;
    }
}
