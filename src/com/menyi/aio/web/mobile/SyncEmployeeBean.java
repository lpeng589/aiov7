package com.menyi.aio.web.mobile;
/**
 * oa用户bean
 * @author Administrator
 *
 */
public class SyncEmployeeBean{
	private String id;
	private String empFullName;
	private String sysName;
	private String password;
	private String departmentCode;
	private String deptFullName;
	private String titleId;
	private String mobile;
	private String email;
	private String tel;
	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getEmpFullName() {
		return empFullName;
	}

	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDeptFullName() {
		return deptFullName;
	}

	public void setDeptFullName(String deptFullName) {
		this.deptFullName = deptFullName;
	}
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * 获得职务
	 * @return
	 */
	public String getPosition(){
		int title=0;
		try {
			title=Integer.valueOf(titleId);
		} catch (Exception e) {
			
		}
		switch (title) {
			case 11: return "职员";
			case 12: return "其它";
			case 1: return "法人";
			case 2: return "总经理";
			case 3: return "部门经理";
			case 4: return "部长";
			case 5: return "副经理";
			case 6: return "副部长";
			case 7: return "主管";
			case 8: return "副主管";
			case 9: return "董事长";
			case 10: return "董事";			
			default:return "其它";
		}
	}
}