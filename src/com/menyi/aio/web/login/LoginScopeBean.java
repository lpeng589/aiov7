package com.menyi.aio.web.login;

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
public class LoginScopeBean implements Serializable {

    private int id;
    private String roleId;

    @Column(nullable = false)
    private String flag;

    @Column(nullable = false,length=50)
    private String tableName;

    @Column(nullable = false,length=50)
    private String fieldName;

    @Column(nullable = false,length=1000)
    private String scopeValue;

    @Column(nullable = false,length=1000)
    private String escopeValue;
    
    @Column(nullable = true)
    private String isAddLevel;
    
    @Column(nullable = true )
    private String isAllModules;
    
    @Column(nullable = false )
    private int rightDelete;
    
    @Column(nullable = false )
    private int rightUpdate;
    
    @Column(nullable = true )
    private String valuetype;
    
    
    
    
    public String getValuetype() {
		return valuetype;
	}

	public void setValuetype(String valuetype) {
		this.valuetype = valuetype;
	}

	public int getRightDelete() {
		return rightDelete;
	}

	public void setRightDelete(int rightDelete) {
		this.rightDelete = rightDelete;
	}

	public int getRightUpdate() {
		return rightUpdate;
	}

	public void setRightUpdate(int rightUpdate) {
		this.rightUpdate = rightUpdate;
	}

	public String getIsAllModules() {
		return isAllModules;
	}

	public void setIsAllModules(String isAllModules) {
		this.isAllModules = isAllModules;
	}

	public LoginScopeBean(){}
    
    public LoginScopeBean(int id,String roleId,String flag,String tableName,
    		String fieldName,String scopeValue,String escopeValue,String isAddLevel){
    	this.id = id;
    	this.roleId = roleId;
    	this.flag = flag;
    	this.tableName = tableName;
    	this.fieldName = fieldName;
    	this.scopeValue = scopeValue;
    	this.escopeValue = escopeValue;
    	this.isAddLevel = isAddLevel;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFlag() {
        return flag;
    }

    public int getId() {
        return id;
    }

    public String getRoleId() {
        return roleId;
    }



    public String getTableName() {
        return tableName;
    }

    public String getScopeValue() {
        return scopeValue;
    }

    public String getEscopeValue() {
        return escopeValue;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setScopeValue(String scopeValue) {
        this.scopeValue = scopeValue;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setEscopeValue(String escopeValue) {
        this.escopeValue = escopeValue;
    }

	public String getIsAddLevel() {
		return isAddLevel;
	}

	public void setIsAddLevel(String isAddLevel) {
		this.isAddLevel = isAddLevel;
	}


}

