package com.menyi.aio.web.mobile;
/**
 * oa�û�bean
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
	 * ���ְ��
	 * @return
	 */
	public String getPosition(){
		int title=0;
		try {
			title=Integer.valueOf(titleId);
		} catch (Exception e) {
			
		}
		switch (title) {
			case 11: return "ְԱ";
			case 12: return "����";
			case 1: return "����";
			case 2: return "�ܾ���";
			case 3: return "���ž���";
			case 4: return "����";
			case 5: return "������";
			case 6: return "������";
			case 7: return "����";
			case 8: return "������";
			case 9: return "���³�";
			case 10: return "����";			
			default:return "����";
		}
	}
}