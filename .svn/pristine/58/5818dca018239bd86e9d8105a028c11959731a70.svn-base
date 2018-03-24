package com.menyi.aio.bean;

import java.io.Serializable;
import java.util.List;

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
@Table(name="tblSunCompanys")
public class SunCompanyBean implements Serializable {

    /** identifier field */
    @Id
    @Column(nullable = false,length=30)
    private String id;
    /** persistent field */
    @Column(nullable = true,length=50 )
    private String sunCompanyName;

    @Column(nullable = true,length=100)
    private String remark;

    @Column(nullable = false,length=30)
    private String createBy;

    @Column(name = "createTime", nullable = false,length=19)
    private String createTime;

    @Column(name = "lastUpdateBy", nullable = true,length=30)
    private String lastUpdateBy;

    @Column(name = "lastUpdateTime", nullable = true,length=19)
    private String lastUpdateTime;

    @Column(name = "classCode", nullable = true)
    private String classCode;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},mappedBy="sunCompanys",targetEntity=EmployeeBean.class)
    private List<EmployeeBean> employees;

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

    public String getSunCompanyName() {
        return sunCompanyName;
    }

    public String getRemark() {
        return remark;
    }

    public List getEmployees() {
        return employees;
    }

    public String getClassCode() {
        return classCode;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSunCompanyName(String sunCompanyName) {
        this.sunCompanyName = sunCompanyName;
    }

    public void setLastUpdateTime(String lastUpdateTime) {

        this.lastUpdateTime = lastUpdateTime;
    }

    public void setLastUpdateBy(String lastUpdateBy) {

        this.lastUpdateBy = lastUpdateBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateTime(String createTime) {

        this.createTime = createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setEmployees(List employees) {
        this.employees = employees;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }


}

