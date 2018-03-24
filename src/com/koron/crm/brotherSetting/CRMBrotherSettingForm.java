package com.koron.crm.brotherSetting;

import com.menyi.web.util.BaseForm;
import com.menyi.web.util.GlobalsTool;

public class CRMBrotherSettingForm extends BaseForm {
	
	private static final long serialVersionUID = 1L;

	private String keyFields;// 模糊查询字段

	private String searchFields;// 枚举时间查询字段

	private String listFields;// 列表显示字段

	private String pageFields;// 主表新增、修改、详情显示字段
	
	private String pageChildFields;// 明细新增、修改、详情显示字段

	private String detailFields;// 列表展开详情字段

	private int pageNo;

	private int pageSize = GlobalsTool.getPageSize();

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

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageChildFields() {
		return pageChildFields;
	}

	public void setPageChildFields(String pageChildFields) {
		this.pageChildFields = pageChildFields;
	}
	
	
}
