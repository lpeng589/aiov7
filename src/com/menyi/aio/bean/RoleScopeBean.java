package com.menyi.aio.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * Description:
 * flag =5:部门管辖范围
 * tableName用于记录，应用的模块的表名，在高级权限设置时翻译成中文用于显示应用的模块。多个表名打,号
 * fieldName为空
 * scopeValue记录部门classCode,多个部门打,号,当为DEPT时代表本部门，ALL时代表全公司
 * isAllModules 为1应用于所有模块
 * rightDelete 为1表示拥有删除权限（isAllModules为1时生效）
 * rightUpdate 为1表示拥有修改权限（isAllModules为1时生效），
 * 只要isAllModules为1 自动拥有所有模块的查询权限。
 * 当isAllModules 为0时，由tblRoleModuleScope指定的权限
 * 
 * flag=1:职员管辖范围 
 * tableName用于记录，应用的模块的表名，在高级权限设置时翻译成中文用于显示应用的模块。多个表名打,号
 * fieldName为空
 * scopeValue记录职员ID,多个职员打,号
 * isAllModules 为1应用于所有模块
 * rightDelete 为1表示拥有删除权限（isAllModules为1时生效）
 * rightUpdate 为1表示拥有修改权限（isAllModules为1时生效），
 * 只要isAllModules为1 自动拥有所有模块的查询权限。
 * 当isAllModules 为0时，由tblRoleModuleScope指定的权限
 * 
 * flag=0:分类资料
 * tableName用于记录，分类资料表名，
 * fieldName分类资料字段名
 * scopeValue记录分类资料classCode
 * isAllModules 为1应用于所有模块
 * 只要isAllModules为1 自动拥有所有模块的查询权限。
 * 当isAllModules 为0时，由tblRoleModuleScope指定的权限
 * 
 * flag=4:单据列控制
 * scopeValue 隐藏
 * escopeValue 只读
 * 
 * 
 * flag=6:查看范围值控制
 * 
 * 
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name="tblRoleScope")
public class RoleScopeBean implements Serializable {

	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
    @Column(nullable = false,length=30)
    private String roleId;


    @Column(nullable = false)
    private String flag;

    @Column(nullable = false,length=50)
    private String tableName;

    @Column(nullable = false,length=50)
    private String fieldName;
    
    @Column(nullable = true,length=100)
    private String tableNameDisplay;

    @Column(nullable = true,length=100)
    private String fieldNameDisplay;

    @Column(nullable = false,length=8000)
    private String scopeValue;

    @Column(nullable = false,length=8000)
    private String escopeValue;
    
    @Column(nullable = true)
    private String isAddLevel;
    
    @Column(nullable = true )
    private String isAllModules;
    
    @Column(nullable = false )
    private int rightDelete;
    
    @Column(nullable = false )
    private int rightUpdate;
    
    @Column(nullable = true,length=8000)
    private String scopeValueDisplay;
    
    @Column(nullable = true )
    private String valuetype;
    


    
    
	public String getValuetype() {
		return valuetype;
	}
	public void setValuetype(String valuetype) {
		this.valuetype = valuetype;
	}
	public String getScopeValueDisplay() {
		return scopeValueDisplay;
	}
	public void setScopeValueDisplay(String scopeValueDisplay) {
		this.scopeValueDisplay = scopeValueDisplay;
	}
	public String getFieldNameDisplay() {
		return fieldNameDisplay;
	}
	public void setFieldNameDisplay(String fieldNameDisplay) {
		this.fieldNameDisplay = fieldNameDisplay;
	}
	public String getTableNameDisplay() {
		return tableNameDisplay;
	}
	public void setTableNameDisplay(String tableNameDisplay) {
		this.tableNameDisplay = tableNameDisplay;
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
	public RoleScopeBean(){}
    public RoleScopeBean(int id,String roleId,String flag,String tableName,
    		String fieldName,String scopeValue,String escopeValue,String isAddLevel,String isAllModules ){
    	this.id = id;
    	this.roleId = roleId;
    	this.flag = flag;
    	this.tableName = tableName;
    	this.fieldName = fieldName;
    	this.scopeValue = scopeValue;
    	this.escopeValue = escopeValue;
    	this.isAddLevel = isAddLevel;
    	this.isAllModules = isAllModules;
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
	public String getIsAllModules() {
		return isAllModules;
	}
	public void setIsAllModules(String isAllModules) {
		this.isAllModules = isAllModules;
	}
}

