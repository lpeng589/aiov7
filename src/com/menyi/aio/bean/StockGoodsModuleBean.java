package com.menyi.aio.bean;

import javax.persistence.*;


/**
 * 
 * <p>
 * Title:库存商品模版
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-08-08
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */

@Entity
@Table(name = "tblStockGoodsModule")
public class StockGoodsModuleBean {
	
	@Id
	private String id;
	
	private String moduleName;
	
	private String moduleDesc;
	
	private String searchFields;
	
	private String showFields;
	
	private String createTime;
	
	private String createBy;

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields;
	}

	public String getShowFields() {
		return showFields;
	}

	public void setShowFields(String showFields) {
		this.showFields = showFields;
	}
	
	
	
}
