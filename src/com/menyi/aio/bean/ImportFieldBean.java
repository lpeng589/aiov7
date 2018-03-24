package com.menyi.aio.bean;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ÷‹–¬”Ó</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ImportFieldBean {
    private String tableName;
    private String fieldName;
    private String name;
    private String viewTableName;
    private String viewFieldName;
    private String viewSaveField;
    private int flag;
    private String id;
    public String getTableName() {
        return tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getViewTableName() {
        return viewTableName;
    }

    public String getViewFieldName() {
        return viewFieldName;
    }

    public String getViewSaveField() {
        return viewSaveField;
    }

    public int getFlag() {
        return flag;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setViewTableName(String viewTableName) {
        this.viewTableName = viewTableName;
    }

    public void setViewFieldName(String viewFieldName) {
        this.viewFieldName = viewFieldName;
    }

    public void setViewSaveField(String viewSaveField) {
        this.viewSaveField = viewSaveField;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
