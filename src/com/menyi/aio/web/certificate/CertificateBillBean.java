package com.menyi.aio.web.certificate;

import java.util.ArrayList;

public class CertificateBillBean {
	private String id;
	private String tableName;	
	private String tempNumber; //ģ����
	private String tempName; //ģ����
	private String credTypeID ; //ƾ֤��
	
	private String popupName;
	
	private String type; //0:���ݣ�1�½�
	
	private ArrayList<CertificateTemplateBean> tempList = new ArrayList<CertificateTemplateBean>();
	
	
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<CertificateTemplateBean> getTempList() {
		return tempList;
	}
	public void setTempList(ArrayList<CertificateTemplateBean> tempList) {
		this.tempList = tempList;
	}
	public String getCredTypeID() {
		return credTypeID;
	}
	public void setCredTypeID(String credTypeID) {
		this.credTypeID = credTypeID;
	}
	public String getTempName() {
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	public String getTempNumber() {
		return tempNumber;
	}
	public void setTempNumber(String tempNumber) {
		this.tempNumber = tempNumber;
	}
	public String getPopupName() {
		return popupName;
	}
	public void setPopupName(String popupName) {
		this.popupName = popupName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
