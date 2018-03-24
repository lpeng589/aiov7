package com.menyi.aio.web.mobile;

import java.util.ArrayList;
import java.util.HashMap;

import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;

/**
 * 深圳市科荣软件股份有限公司自定义平台接口
 * 
 * @version 1.0<br/>
 * 创建历史：2016-03-23<br/>
 * 修改历史：<br/>
 * 
 * @author 周新宇
 * 自定义单据详情对象<br/>
 * 
 *
 * @preserve all
 */
public class DetailBean {
	
	/**
	 * 取数结果
	 */
	private int code;

	/**
	 * 取数结果提示
	 */
	private String msg;

	/**
	 * 是否有反审核权限
	 */
	private boolean retCheckRight;// 是否有反审核权限
	/**
	 * 是否有审核权限
	 */
	private boolean checkRight;// 审核权限
	/**
	 * 是否有摧办权限
	 */
	private boolean hurryTrans;// 摧办权限
	/**
	 * 是否有撤消权限
	 */
	private boolean hasCancel;// 撤消功能
	/**
	 * 操作类型add:添加界面（添加准备过程用），update:有修改权限,detail：只有查看详情权限（这个参数会告诉当前这个单据当前登陆人是否有权限修改，还是只有查看权限）
	 */
	private String opType;// 操作类型 add,update,detail
	/**
	 * 是否有模块修改权限（有模块修改单据权限不一定有本条数据的单据修改权限，如本单不是本人做的且没有设置范围权限）
	 */
	private boolean updateRight;// 修改权限
	/**
	 * 是否有模块删除权限
	 */
	private boolean deleteRight;// 删除权限
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

	public boolean isUpdateRight() {
		return updateRight;
	}

	public boolean isDeleteRight() {
		return deleteRight;
	}

	public void setDeleteRight(boolean deleteRight) {
		this.deleteRight = deleteRight;
	}

	public void setUpdateRight(boolean updateRight) {
		this.updateRight = updateRight;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	public HashMap getChildShowField() {
		return childShowField;
	}

	public void setChildShowField(HashMap childShowField) {
		this.childShowField = childShowField;
	}

	public ArrayList getFieldList() {
		return fieldList;
	}

	public void setFieldList(ArrayList fieldList) {
		this.fieldList = fieldList;
	}

	public boolean isCheckRight() {
		return checkRight;
	}

	public void setCheckRight(boolean checkRight) {
		this.checkRight = checkRight;
	}

	public boolean isHasCancel() {
		return hasCancel;
	}

	public void setHasCancel(boolean hasCancel) {
		this.hasCancel = hasCancel;
	}

	public boolean isHurryTrans() {
		return hurryTrans;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public void setHurryTrans(boolean hurryTrans) {
		this.hurryTrans = hurryTrans;
	}

	public boolean isRetCheckRight() {
		return retCheckRight;
	}

	public void setRetCheckRight(boolean retCheckRight) {
		this.retCheckRight = retCheckRight;
	}

}
