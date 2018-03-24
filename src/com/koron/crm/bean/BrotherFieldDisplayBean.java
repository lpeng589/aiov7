package com.koron.crm.bean;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:字段设置
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-7-31
 * @Copyright: 科荣软件
 * @Author 徐洁俊
 * @preserve all
 */

@Entity
@Table(name = "CRMBrotherFieldDisplay")
public class BrotherFieldDisplayBean {
	@Id
	private String id;//ID存放的是表名

	private String keyFields;//模糊查询字段

	private String searchFields;//枚举时间查询字段

	private String listFields;//列表显示字段

	private String pageFields;//主表新增、修改、详情显示字段
	
	private String pageChildFields;//明细新增、修改、详情显示字段

	private String detailFields;//列表展开详情字段

	private String createBy;//创建人
	
	private String createTime;//创建时间
	
	private String lastUpdateBy;//最后修改人
	
	private String lastUpdateTime;//最后更新时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyFields() {
		return keyFields;
	}

	public void setKeyFields(String keyFields) {
		this.keyFields = keyFields;
	}

	public String getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields;
	}

	public String getListFields() {
		return listFields;
	}

	public void setListFields(String listFields) {
		this.listFields = listFields;
	}

	public String getPageFields() {
		return pageFields;
	}

	public void setPageFields(String pageFields) {
		this.pageFields = pageFields;
	}

	public String getDetailFields() {
		return detailFields;
	}

	public void setDetailFields(String detailFields) {
		this.detailFields = detailFields;
	}

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

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getPageChildFields() {
		return pageChildFields;
	}

	public void setPageChildFields(String pageChildFields) {
		this.pageChildFields = pageChildFields;
	}


	

}
