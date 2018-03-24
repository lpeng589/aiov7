package com.koron.oa.employeeDepartment;

import java.io.Serializable;

import com.menyi.web.util.BaseForm;

public class EmployeeForm extends BaseForm implements Serializable{

	private String id;								//id
	private String empNumber;						//编号
	private String departmentCode;					//部门
	private String empFullName;						//职员全称
	private String gender;							//性别
	private String titleID;							//职位
	private String empEnglishName;					//英文名
	private String directBoss;						//直接上司
	private String employDate;						//入职日期
	private String mobile;							//手机号码
	private String qq;								//QQ号码
	private String tel;								//办公直线
	private String email;							//邮箱地址
	private String familyAddress;					//家庭地址
	private String responsibility;					//岗位
	private String bornDate;						//生日
	private String leaveDate;						//离职日期
	private String remark;							//备注
	private String stockCode;						//默认仓库
	private String companyCode;						//默认往来单位
	private String account;							//默认收款账户
	private Integer mailSize;						//邮箱空间(兆）
	private String define1;							//自定义1
	private String define2;							//自定义2
	private String define3;							//自定义3
	private String define4;							//自定义4
	private String define5;							//自定义5
	
	public String getBornDate() {
		return bornDate;
	}
	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getDefine1() {
		return define1;
	}
	public void setDefine1(String define1) {
		this.define1 = define1;
	}
	public String getDefine2() {
		return define2;
	}
	public void setDefine2(String define2) {
		this.define2 = define2;
	}
	public String getDefine3() {
		return define3;
	}
	public void setDefine3(String define3) {
		this.define3 = define3;
	}
	public String getDefine4() {
		return define4;
	}
	public void setDefine4(String define4) {
		this.define4 = define4;
	}
	public String getDefine5() {
		return define5;
	}
	public void setDefine5(String define5) {
		this.define5 = define5;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getDirectBoss() {
		return directBoss;
	}
	public void setDirectBoss(String directBoss) {
		this.directBoss = directBoss;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmpEnglishName() {
		return empEnglishName;
	}
	public void setEmpEnglishName(String empEnglishName) {
		this.empEnglishName = empEnglishName;
	}
	public String getEmpFullName() {
		return empFullName;
	}
	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}
	public String getEmployDate() {
		return employDate;
	}
	public void setEmployDate(String employDate) {
		this.employDate = employDate;
	}
	public String getEmpNumber() {
		return empNumber;
	}
	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}
	public String getFamilyAddress() {
		return familyAddress;
	}
	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTitleID() {
		return titleID;
	}
	public void setTitleID(String titleID) {
		this.titleID = titleID;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public Integer getMailSize() {
		return mailSize;
	}
	public void setMailSize(Integer mailSize) {
		this.mailSize = mailSize;
	}
	
	
	
	
}
