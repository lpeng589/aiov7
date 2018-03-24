package com.koron.oa.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class OAMobileDetBean {
	
	/**
	 * 单据详情字段名和值的HashMap
	 */
	private HashMap values;
	
	/**
	 * 明细表的表名列表
	 */
	private ArrayList childTableList;
	
	/**
	 * 单据审核记录详情
	 */
	private ArrayList flowDepict;
	
	/**
	 * 主表字段列表
	 */
	private ArrayList fieldList;
	
	/**
	 * 明细表字段列表
	 */
	private HashMap childShowField;
	
	/**
	 * 是否有审核权限
	 */
	private boolean checkRight;// 审核权限
	
	/**
	 * 是否有反审核权限
	 */
	private boolean retCheckRight;// 是否有反审核权限
	
	/**
	 * 是否有摧办权限
	 */
	private boolean hurryTrans;// 摧办权限
	
	/**
	 * 是否有撤消权限
	 */
	private boolean hasCancel;// 撤消功能
	
	/**
	 * 是否有模块修改权限（有模块修改单据权限不一定有本条数据的单据修改权限，如本单不是本人做的且没有设置范围权限）
	 */
	private boolean updateRight;// 修改权限
	
	/**
	 * 模块名
	 * @return
	 */
	private String moduleName;
	
	

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public boolean isUpdateRight() {
		return updateRight;
	}

	public void setUpdateRight(boolean updateRight) {
		this.updateRight = updateRight;
	}

	public HashMap getValues() {
		return values;
	}

	public void setValues(HashMap values) {
		this.values = values;
	}

	public ArrayList getChildTableList() {
		return childTableList;
	}

	public void setChildTableList(ArrayList childTableList) {
		this.childTableList = childTableList;
	}

	public ArrayList getFlowDepict() {
		return flowDepict;
	}

	public void setFlowDepict(ArrayList flowDepict) {
		this.flowDepict = flowDepict;
	}

	public ArrayList getFieldList() {
		return fieldList;
	}

	public void setFieldList(ArrayList fieldList) {
		this.fieldList = fieldList;
	}

	public HashMap getChildShowField() {
		return childShowField;
	}

	public void setChildShowField(HashMap childShowField) {
		this.childShowField = childShowField;
	}

	public boolean isCheckRight() {
		return checkRight;
	}

	public void setCheckRight(boolean checkRight) {
		this.checkRight = checkRight;
	}

	public boolean isRetCheckRight() {
		return retCheckRight;
	}

	public void setRetCheckRight(boolean retCheckRight) {
		this.retCheckRight = retCheckRight;
	}

	public boolean isHurryTrans() {
		return hurryTrans;
	}

	public void setHurryTrans(boolean hurryTrans) {
		this.hurryTrans = hurryTrans;
	}

	public boolean isHasCancel() {
		return hasCancel;
	}

	public void setHasCancel(boolean hasCancel) {
		this.hasCancel = hasCancel;
	}
	
	
}
