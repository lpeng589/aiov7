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
@Table(name="tblRole")
public class RoleBean implements Serializable {

    /** identifier field */
    @Id
    @Column(nullable = false,length=30)
    private String id;
    /** persistent field */
    @Column(nullable = false,length=50 )
    private String roleName;

    @Column(nullable = true,length=500)
    private String roleDesc;

    @Column(nullable = false,length=30)
    private String createBy;

    @Column(name = "createTime", nullable = false,length=19)
    private String createTime;

    @Column(name = "lastUpdateBy", nullable = true,length=30)
    private String lastUpdateBy;

    @Column(name = "lastUpdateTime", nullable = true,length=19)
    private String lastUpdateTime;

    @Column(name = "SCompanyID", nullable = true)
    private String SCompanyID;
    
    @Column(name = "hiddenField", nullable = true)
    private String hiddenField;

//    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},mappedBy="roles",targetEntity=EmployeeBean.class)
//    private List<EmployeeBean> employees;

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

    public String getRoleDesc() {
        return roleDesc;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getSCompanyID() {
        return SCompanyID;
    }

    //    public List getEmployees() {
//
//        return employees;
//    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
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

    public void setSCompanyID(String SCompanyID) {
        this.SCompanyID = SCompanyID;
    }

	public String getHiddenField() {
		return hiddenField;
	}

	public void setHiddenField(String hiddenField) {
		this.hiddenField = hiddenField;
	}

    //    public void setEmployees(List employees) {
//
//        this.employees = employees;
//    }


}

