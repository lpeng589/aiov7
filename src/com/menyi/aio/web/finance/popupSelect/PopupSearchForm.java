package com.menyi.aio.web.finance.popupSelect;

import com.menyi.web.util.BaseSearchForm;

public class PopupSearchForm extends BaseSearchForm{

	private String selectType;			//查询方式（keyword关键字查询，group组查询，choose已选数据查询，all所有信息）
	private String chooseType;			//选择类型（chooseChild只可以选择最下级或者，all全部，choosePerent只可以选择父级不可以选择最下级）
	private String selectValue;			//查询的值（不同查询方式此值不同）
	private String isCease;				//是否显示不启用的科目（默认不显示，显示设为true）
	private String inputType;			//类型（复选框checkbox，单选框radio）
	private String returnName;			//弹出框回调函数名称
	private String isCheckItem;			//是否显示核算项(false不显示，true显示 默认true)
	
	
	public String getChooseType() {
		return chooseType;
	}
	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getIsCease() {
		return isCease;
	}
	public void setIsCease(String isCease) {
		this.isCease = isCease;
	}
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	public String getSelectValue() {
		return selectValue;
	}
	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}
	public String getReturnName() {
		return returnName;
	}
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}
	public String getIsCheckItem() {
		return isCheckItem;
	}
	public void setIsCheckItem(String isCheckItem) {
		this.isCheckItem = isCheckItem;
	}
	
	
}
