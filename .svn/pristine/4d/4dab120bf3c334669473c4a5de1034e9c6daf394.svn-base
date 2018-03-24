package com.menyi.aio.bean;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name="tblUserModule")
public class UserModuleBean implements Serializable {


    @Id
    @Column(nullable = false,length=30)
    private String id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "userid")
    private EmployeeBean employeeBean; //对应主表 */

    @Column(nullable = false ,length=30)
    private String moduleOpId;

    @Column(nullable = true ,length=1)
    private String scopeFlag;

    @Column(nullable = true ,length=1)
    private String displayFlag;

    @OneToMany(mappedBy="userModuleBean",cascade = CascadeType.ALL,fetch= FetchType.LAZY)
    private List<ScopeBean> scopeinfos = new ArrayList<ScopeBean>();

    @OneToMany(mappedBy="userModuleBean",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<DiscontrolBean> discontrolinfos = new ArrayList<DiscontrolBean>();




    public String getId() {
        return id;
    }



    public String getDisplayFlag() {
        return displayFlag;
    }

    public String getScopeFlag() {
        return scopeFlag;
    }





    public List getDiscontrolinfos() {
        return discontrolinfos;
    }

    public List getScopeinfos() {
        return scopeinfos;
    }

    public String getModuleOpId() {
        return moduleOpId;
    }

    public EmployeeBean getEmployeeBean() {

        return employeeBean;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setDisplayFlag(String displayFlag) {
        this.displayFlag = displayFlag;
    }

    public void setScopeFlag(String scopeFlag) {
        this.scopeFlag = scopeFlag;
    }





    public void setDiscontrolinfos(List discontrolinfos) {
        this.discontrolinfos = discontrolinfos;
    }

    public void setScopeinfos(List scopeinfos) {
        this.scopeinfos = scopeinfos;
    }

    public void setModuleOpId(String moduleOpId) {
        this.moduleOpId = moduleOpId;
    }

    public void setEmployeeBean(EmployeeBean employeeBean) {

        this.employeeBean = employeeBean;
    }
}

