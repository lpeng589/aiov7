package com.koron.oa.accredit;

import com.menyi.web.util.BaseSearchForm;

/**
 * 
 * 
 * <p>Title:弹出框SearchForm</p> 
 * <p>Description: </p>
 *
 * @Date:2012-8-28
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class AccreditSearchForm extends BaseSearchForm{

	
	private String popname;			//弹出框的名称（deptGroup=得到部门的数据）
	private String value;			//搜索的参数
	private String inputType;		//input类型（复选框checkbox，单选框redio）
	private String condition;		//外部传入的参数，比如职员表要过滤掉admin帐号，要以要求加参数，noAdmin
	private String keyType;         //查询的方式（1.组 2.关键字 3.字母）
	
	private String parameterCode;		//部门根据classCode检索
	private String userCode;		//人员检索
	
	private String chooseData;		//选中的数据的编号（用","分开）
	
	private boolean chooseParent=true;	//是否允许可以可以选择父级（默认是true）,如：部门单选弹出框，父级是不可以选择的，必须是最下级
	
	private String leavePerson;//是否要显示离职按钮
	
	
	public String getLeavePerson() {
		return leavePerson;
	}
	public void setLeavePerson(String leavePerson) {
		this.leavePerson = leavePerson;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getKeyType() {
		return keyType;
	}
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}
	public String getPopname() {
		return popname;
	}
	public void setPopname(String popname) {
		this.popname = popname;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getChooseData() {
		return chooseData;
	}
	public void setChooseData(String chooseData) {
		this.chooseData = chooseData;
	}
	
	public String getParameterCode() {
		return parameterCode;
	}
	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public boolean isChooseParent() {
		return chooseParent;
	}
	public void setChooseParent(boolean chooseParent) {
		this.chooseParent = chooseParent;
	}
	
}
