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
@Table(name="tblScope")
public class ScopeBean implements Serializable {

    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userModuleId")
    private UserModuleBean userModuleBean; //对应主表 */

    @Column(nullable = true)
    private String value;

    @Column(nullable = true)
    private String tableName;

    @Column(nullable = true)
    private String fieldName;

   @Column(nullable = true)
    private String createBy;

    @Column(nullable = true)
    private String createDatetime;

    @Column(nullable = true)
    private String lstUpdateBy;

    @Column(nullable = true)
    private String lstUpdateDatetime;

    @Column(nullable = true)
    private String scopeFlag;

    public ScopeBean(){}

    public ScopeBean(String tableName,String fieldName,String value,String scopeFlag){

        this.value = value;
        this.fieldName = fieldName;
        this.tableName = tableName;
        this.scopeFlag = scopeFlag;
    }

    public String getScopeFlag(){
        return scopeFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public String getId() {
        return id;
    }

    public String getLstUpdateBy() {
        return lstUpdateBy;
    }

    public String getLstUpdateDatetime() {
        return lstUpdateDatetime;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getValue() {
        return value;
    }

    public void setScopeFlag(String scopeFlag){

        this.scopeFlag = scopeFlag;
    }

    public UserModuleBean getUserModuleBean() {
        return userModuleBean;
    }


    public void setLstUpdateDatetime(String lstUpdateDatetime) {
        this.lstUpdateDatetime = lstUpdateDatetime;
    }

    public void setLstUpdateBy(String lstUpdateBy) {
        this.lstUpdateBy = lstUpdateBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public void setUserModuleBean(UserModuleBean userModuleBean) {
        this.userModuleBean = userModuleBean;
    }
}

