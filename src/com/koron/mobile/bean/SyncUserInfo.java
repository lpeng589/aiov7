package com.koron.mobile.bean;
/**
 * 
 * <p>Title:ͬ���û���ϸ��Ϣ�ֶ�</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: �������
 * @Author ë��
 * @preserve all
 */
public class SyncUserInfo {
	
	private String syncUserId;//�û�ID	
	private String LoginName;//��Ҫ�޸ĵ���Ա��¼����	
	private String UserName;//����	
	private String Password;//������	
	private boolean md5;//�Ƿ�Ϊ�������md5����	true or false
	private String DepartmentID;//�²���	���÷��Ĳ���ID
	private Integer IsAdmin;//�Ƿ�Ϊ����Ա	1Ϊ����Ա 0Ϊ�ǹ���Ա
	private String gender;//�Ա�	
	private String NickName;//����	
	private String Title; //ְλ	
	private String Country;//����	
	private String Province;//ʡ��	
	private String City;//����	
	private String District;//����	
	private String Addr;//��ַ	
	private String TelAreaCode;//�绰����	
	private String Tel;//�绰����	
	private String TelExt;//�绰�ֻ���	
	private String Mobile;//�ֻ�	
	private String FaxAreaCode;//��������	
	private String Fax;//�������	
	private String FaxExt;//����ֻ�	
	private String Email;//�������	
	private String BirthDay;//����(�밴�������ʽ��д)	��ʽ(1982-02-14)
	private String Memo;//��ע	
	private String keycode;//���ܴ��Զ���(Ĭ��Ϊ gzRN53VWRF9BYUXo)
	public String getAddr() {
		return Addr;
	}
	public void setAddr(String addr) {
		Addr = addr;
	}
	public String getBirthDay() {
		return BirthDay;
	}
	public void setBirthDay(String birthDay) {
		BirthDay = birthDay;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getDepartmentID() {
		return DepartmentID;
	}
	public void setDepartmentID(String departmentID) {
		DepartmentID = departmentID;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getFax() {
		return Fax;
	}
	public void setFax(String fax) {
		Fax = fax;
	}
	public String getFaxAreaCode() {
		return FaxAreaCode;
	}
	public void setFaxAreaCode(String faxAreaCode) {
		FaxAreaCode = faxAreaCode;
	}
	public String getFaxExt() {
		return FaxExt;
	}
	public void setFaxExt(String faxExt) {
		FaxExt = faxExt;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getIsAdmin() {
		return IsAdmin;
	}
	public void setIsAdmin(Integer isAdmin) {
		IsAdmin = isAdmin;
	}
	public String getKeycode() {
		return keycode;
	}
	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}
	public String getLoginName() {
		return LoginName;
	}
	public void setLoginName(String loginName) {
		LoginName = loginName;
	}
	public boolean getMd5() {
		return md5;
	}
	public void setMd5(boolean md5) {
		this.md5 = md5;
	}
	public String getMemo() {
		return Memo;
	}
	public void setMemo(String memo) {
		Memo = memo;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String getSyncUserId() {
		return syncUserId;
	}
	public void setSyncUserId(String syncUserId) {
		this.syncUserId = syncUserId;
	}
	public String getTel() {
		return Tel;
	}
	public void setTel(String tel) {
		Tel = tel;
	}
	public String getTelAreaCode() {
		return TelAreaCode;
	}
	public void setTelAreaCode(String telAreaCode) {
		TelAreaCode = telAreaCode;
	}
	public String getTelExt() {
		return TelExt;
	}
	public void setTelExt(String telExt) {
		TelExt = telExt;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	
	
}
