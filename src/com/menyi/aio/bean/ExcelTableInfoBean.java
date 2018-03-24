package com.menyi.aio.bean;

import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class ExcelTableInfoBean {
    private String id;
    private String tableName;
    private String display;
    private int isUserd;
    private String parentName;
    private String parentDispalyName;
    private List<ExcelFieldInfoBean> fieldsInfo;
    public ExcelTableInfoBean() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setFieldsInfo(List fieldsInfo) {
        this.fieldsInfo = fieldsInfo;
    }

    public void setIsUserd(int isUserd) {
        this.isUserd = isUserd;
    }

    public String getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDisplay() {
        return display;
    }

    public List getFieldsInfo() {
        return fieldsInfo;
    }

    public int getIsUserd() {
        return isUserd;
    }

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentDispalyName() {
		return parentDispalyName;
	}

	public void setParentDispalyName(String parentDispalyName) {
		this.parentDispalyName = parentDispalyName;
	}
}
