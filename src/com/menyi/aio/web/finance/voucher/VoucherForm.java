package com.menyi.aio.web.finance.voucher;

import com.menyi.web.util.BaseForm;


/**
 * 凭证 Form
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2013-03-04
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class VoucherForm extends BaseForm{

	private Integer credNo;					//凭证号
	private Integer acceNum;				//附件数
	private String departmentCode;			//部门
	private String employeeID;				//记账员
	private String credTypeID;				//凭证字
	private String remark;					//备注
	
	
	public Integer getCredNo() {
		return credNo;
	}
	public void setCredNo(Integer credNo) {
		this.credNo = credNo;
	}
	public Integer getAcceNum() {
		return acceNum;
	}
	public void setAcceNum(Integer acceNum) {
		this.acceNum = acceNum;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getCredTypeID() {
		return credTypeID;
	}
	public void setCredTypeID(String credTypeID) {
		this.credTypeID = credTypeID;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
