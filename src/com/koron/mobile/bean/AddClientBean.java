package com.koron.mobile.bean;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-06-05
 * @Copyright: �������
 * @Author pwy
 * @preserve all
 */
public class AddClientBean {
	private String ClientName;	// �ͻ�����

	private String UserName;	// ��ϵ��

	private String Telephone;	// ��ϵ�˵绰

	private String ClientRemark;	// ��ע

	private String[] locImage;	// ����

	private String Attachment;	// ����

	private String Address;	// ��ַ

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getAttachment() {
		return Attachment;
	}

	public void setAttachment(String attachment) {
		Attachment = attachment;
	}

	public String getClientName() {
		return ClientName;
	}

	public void setClientName(String clientName) {
		ClientName = clientName;
	}

	public String getClientRemark() {
		return ClientRemark;
	}

	public void setClientRemark(String clientRemark) {
		ClientRemark = clientRemark;
	}

	public String[] getLocImage() {
		return locImage;
	}

	public void setLocImage(String[] locImage) {
		this.locImage = locImage;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

}
