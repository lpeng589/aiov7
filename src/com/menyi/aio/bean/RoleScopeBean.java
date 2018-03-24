package com.menyi.aio.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * Description:
 * flag =5:���Ź�Ͻ��Χ
 * tableName���ڼ�¼��Ӧ�õ�ģ��ı������ڸ߼�Ȩ������ʱ���������������ʾӦ�õ�ģ�顣���������,��
 * fieldNameΪ��
 * scopeValue��¼����classCode,������Ŵ�,��,��ΪDEPTʱ�������ţ�ALLʱ����ȫ��˾
 * isAllModules Ϊ1Ӧ��������ģ��
 * rightDelete Ϊ1��ʾӵ��ɾ��Ȩ�ޣ�isAllModulesΪ1ʱ��Ч��
 * rightUpdate Ϊ1��ʾӵ���޸�Ȩ�ޣ�isAllModulesΪ1ʱ��Ч����
 * ֻҪisAllModulesΪ1 �Զ�ӵ������ģ��Ĳ�ѯȨ�ޡ�
 * ��isAllModules Ϊ0ʱ����tblRoleModuleScopeָ����Ȩ��
 * 
 * flag=1:ְԱ��Ͻ��Χ 
 * tableName���ڼ�¼��Ӧ�õ�ģ��ı������ڸ߼�Ȩ������ʱ���������������ʾӦ�õ�ģ�顣���������,��
 * fieldNameΪ��
 * scopeValue��¼ְԱID,���ְԱ��,��
 * isAllModules Ϊ1Ӧ��������ģ��
 * rightDelete Ϊ1��ʾӵ��ɾ��Ȩ�ޣ�isAllModulesΪ1ʱ��Ч��
 * rightUpdate Ϊ1��ʾӵ���޸�Ȩ�ޣ�isAllModulesΪ1ʱ��Ч����
 * ֻҪisAllModulesΪ1 �Զ�ӵ������ģ��Ĳ�ѯȨ�ޡ�
 * ��isAllModules Ϊ0ʱ����tblRoleModuleScopeָ����Ȩ��
 * 
 * flag=0:��������
 * tableName���ڼ�¼���������ϱ�����
 * fieldName���������ֶ���
 * scopeValue��¼��������classCode
 * isAllModules Ϊ1Ӧ��������ģ��
 * ֻҪisAllModulesΪ1 �Զ�ӵ������ģ��Ĳ�ѯȨ�ޡ�
 * ��isAllModules Ϊ0ʱ����tblRoleModuleScopeָ����Ȩ��
 * 
 * flag=4:�����п���
 * scopeValue ����
 * escopeValue ֻ��
 * 
 * 
 * flag=6:�鿴��Χֵ����
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

