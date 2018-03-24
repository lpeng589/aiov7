package com.menyi.aio.bean;

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
@Table(name = "tblSysDeploy")
public class SystemSettingBean {

    @Id
    private String id;

    @Column(nullable=false)
    private String sysCode;

    @Column(nullable=false)
    private String sysName;

    @Column(nullable=false)
    private String defaultValue;

    private String setting;

    public SystemSettingBean() {
    }

    public SystemSettingBean(String sysCode, String sysName, String defaultValue, String setting) {

        this.sysCode = sysCode;
        this.sysName = sysName;
        this.defaultValue = defaultValue;
        this.setting = setting;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getId() {
        return id;
    }

    public String getSysCode() {
        return sysCode;
    }

    public String getSysName() {
        return sysName;
    }

    public String getSetting() {
        return setting;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

}
