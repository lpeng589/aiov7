package com.menyi.aio.bean;

import java.util.List;

import javax.persistence.*;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: 周新宇
 * </p>
 * 
 * @author 周新宇
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name = "tblEmployee")
public class EmployeeBean {

	@Id
	@Column(nullable = false, length = 30)
	private String id;

	@Column(nullable = false, length = 50)
	private String empFullName;

	@Column(nullable = false, length = 50)
	private String createTime;

	@Column(nullable = true, length = 50)
	private String empName;//简称

	@Column(nullable = true, length = 50)
	private String sysName;

	@Column(nullable = true, length = 50)
	private String defSys;

	@Column(nullable = true, length = 50)
	private String lastUpdateTime;
	
	@Column(nullable = true, length = 50)
	private String lastUpdateBy;
	
	@Column(nullable = true, length = 50)
	private String defLoc;

	@Column(nullable = true, length = 50)
	private String password;

	@Column(nullable = true, length = 50)
	private String DepartmentCode;

	@Column(nullable = true, length = 50)
	private String SCompanyID;

	@Column(nullable = true, length = 50)
	private String MACAddress;

	@Column(nullable = true, length = 50)
	private String Mobile;

	@Column(nullable = true, length = 50)
	private String Email;

	@Column(nullable = true, length = 50)
	private String userKeyId;

	@Column(nullable = true, length = 50)
	private String Gender;
	
	@Column(nullable = true, length = 50)
	private String TitleID ;

	@Column(nullable = true)
	private String empNumber;

	@Column(nullable = true)
	private String photo;

	@Column(nullable = true)
	private String statusId;

	private String loginStartTime;
	private String loginEndTime;
	private String telpop;

	private String viewTopMenu;
	private String viewLeftMenu;

	private int openFlag;

	@Column(nullable = true, length = 100)
	private String defaultPage;// 默认页面

	private String extension;

	private String lbxMonitorCh;// 用户管理的来电通道/本地电话
	private String telPrefix;// 电话前缀，如外线号
	private String telAreaCode;// 用户打电话时所在地的区位码。
	private String shopName;
	private String shopPwd;

	private String ipstart;
	private String ipend;
	
	private String bornDate; //生日
	private String employDate;//入职时间
	private String mailSize;//邮箱大小
	private String tel; //联系电话
	private String graduateId;
	private String moduleId;//默认模板ID
	private String qq;
	
	private int canJxc;
	private int canOa;
	private int canCrm;
	private int canHr;
	private int canOrders;
	private String defDesk;
	
	private String mobileID;	// 允许登录的手机设备
	private int checkMobile;	// 校验手机设备
	private String showDesk;	//显示那个桌面
	
	@Column(nullable = true, length = 100)
	private String hiddenField;
	
	
	private String firstShow;	//第一次显示帮助信息
	
	private int showWebNote;
	
	private String isPublic;
	
	
	
	
	

	@OneToMany(mappedBy = "employeeBean", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserModuleBean> usermoduleinfos;

	// @ManyToMany(targetEntity = RoleBean.class, cascade = {
	// CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE
	// },
	// fetch = FetchType.EAGER)
	// @JoinTable(name = "tblUserRole", joinColumns = @JoinColumn(name =
	// "userid"),
	// inverseJoinColumns = @JoinColumn(name = "roleid"))
	// private List<RoleBean> roles;

	@ManyToMany(targetEntity = SunCompanyBean.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinTable(name = "tblUserSunCompany", joinColumns = @JoinColumn(name = "userid"), inverseJoinColumns = @JoinColumn(name = "sunCompanyid"))
	private List<SunCompanyBean> sunCompanys;

	
	
	public int getShowWebNote() {
		return showWebNote;
	}

	public void setShowWebNote(int showWebNote) {
		this.showWebNote = showWebNote;
	}

	public String getFirstShow() {
		return firstShow;
	}

	public void setFirstShow(String firstShow) {
		this.firstShow = firstShow;
	}

	public String getShowDesk() {
		return showDesk;
	}

	public void setShowDesk(String showDesk) {
		this.showDesk = showDesk;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public int getCanCrm() {
		return canCrm;
	}

	public void setCanCrm(int canCrm) {
		this.canCrm = canCrm;
	}

	public int getCanHr() {
		return canHr;
	}

	public void setCanHr(int canHr) {
		this.canHr = canHr;
	}

	public int getCanJxc() {
		return canJxc;
	}

	public void setCanJxc(int canJxc) {
		this.canJxc = canJxc;
	}

	public int getCanOa() {
		return canOa;
	}

	public void setCanOa(int canOa) {
		this.canOa = canOa;
	}

	public int getCanOrders() {
		return canOrders;
	}

	public void setCanOrders(int canOrders) {
		this.canOrders = canOrders;
	}

	public String getId() {
		return id;
	}

	public String getIpend() {
		return ipend;
	}

	public void setIpend(String ipend) {
		this.ipend = ipend;
	}

	public String getIpstart() {
		return ipstart;
	}

	public void setIpstart(String ipstart) {
		this.ipstart = ipstart;
	}

	public String getViewLeftMenu() {
		return viewLeftMenu;
	}

	public void setViewLeftMenu(String viewLeftMenu) {
		this.viewLeftMenu = viewLeftMenu;
	}

	public String getViewTopMenu() {
		return viewTopMenu;
	}

	public void setViewTopMenu(String viewTopMenu) {
		this.viewTopMenu = viewTopMenu;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getDefaultPage() {
		return defaultPage;
	}

	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}

	public String getLoginEndTime() {
		return loginEndTime;
	}

	public void setLoginEndTime(String loginEndTime) {
		this.loginEndTime = loginEndTime;
	}

	public String getLoginStartTime() {
		return loginStartTime;
	}

	public void setLoginStartTime(String loginStartTime) {
		this.loginStartTime = loginStartTime;
	}

	public String getTelpop() {
		return telpop;
	}

	public void setTelpop(String telpop) {
		this.telpop = telpop;
	}

	public String getEmpFullName() {
		return empFullName;
	}

	public String getEmpName() {
		return empName;
	}

	public int getOpenFlag() {
		return openFlag;
	}

	public String getPassword() {
		return password;
	}

	// public List getRoles() {
	// return roles;
	// }

	public String getSysName() {
		return sysName;
	}

	public List getUsermoduleinfos() {
		return usermoduleinfos;
	}

	public String getDefSys() {

		return defSys;
	}

	public List getSunCompanys() {
		return sunCompanys;
	}

	public String getSCompanyID() {
		return SCompanyID;
	}

	public String getDepartmentCode() {
		return DepartmentCode;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public void setUsermoduleinfos(List usermoduleinfos) {
		this.usermoduleinfos = usermoduleinfos;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	// public void setRoles(List roles) {
	// this.roles = roles;
	// }

	public void setPassword(String password) {
		this.password = password;
	}

	public void setOpenFlag(int openFlag) {
		this.openFlag = openFlag;
	}

	public void setDefSys(String defSys) {

		this.defSys = defSys;
	}

	public String getDefLoc() {
		return defLoc;
	}

	public void setDefLoc(String defLoc) {
		this.defLoc = defLoc;
	}

	public void setSunCompanys(List sunCompanys) {
		this.sunCompanys = sunCompanys;
	}

	public void setSCompanyID(String SCompanyID) {
		this.SCompanyID = SCompanyID;
	}

	public void setDepartmentCode(String DepartmentCode) {
		this.DepartmentCode = DepartmentCode;
	}

	public String getLbxMonitorCh() {
		return lbxMonitorCh;
	}

	public String getUserKeyId() {
		return userKeyId;
	}

	public void setLbxMonitorCh(String lbxMonitorCh) {
		this.lbxMonitorCh = lbxMonitorCh;
	}

	public void setUserKeyId(String userKeyId) {
		this.userKeyId = userKeyId;
	}

	public String getMACAddress() {
		return MACAddress;
	}

	public void setMACAddress(String address) {
		MACAddress = address;
	}

	public String getTelAreaCode() {
		return telAreaCode;
	}

	public void setTelAreaCode(String telAreaCode) {
		this.telAreaCode = telAreaCode;
	}

	public String getTelPrefix() {
		return telPrefix;
	}

	public void setTelPrefix(String telPrefix) {
		this.telPrefix = telPrefix;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getEmpNumber() {
		return empNumber;
	}

	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopPwd() {
		return shopPwd;
	}

	public void setShopPwd(String shopPwd) {
		this.shopPwd = shopPwd;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getTitleID() {
		return TitleID;
	}

	public void setTitleID(String titleID) {
		TitleID = titleID;
	}

	public String getEmployDate() {
		return employDate;
	}

	public void setEmployDate(String employDate) {
		this.employDate = employDate;
	}

	public String getMailSize() {
		return mailSize;
	}

	public void setMailSize(String mailSize) {
		this.mailSize = mailSize;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBornDate() {
		return bornDate;
	}

	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}

	public String getGraduateId() {
		return graduateId;
	}

	public void setGraduateId(String graduateId) {
		this.graduateId = graduateId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getCheckMobile() {
		return checkMobile;
	}

	public void setCheckMobile(int checkMobile) {
		this.checkMobile = checkMobile;
	}

	public String getMobileID() {
		return mobileID;
	}

	public void setMobileID(String mobileID) {
		this.mobileID = mobileID;
	}

	public String getDefDesk() {
		return defDesk;
	}

	public void setDefDesk(String defDesk) {
		this.defDesk = defDesk;
	}

	public String getHiddenField() {
		return hiddenField;
	}

	public void setHiddenField(String hiddenField) {
		this.hiddenField = hiddenField;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}
	
}
