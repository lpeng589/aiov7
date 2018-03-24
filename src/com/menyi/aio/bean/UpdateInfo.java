package com.menyi.aio.bean;


/**
*
* <p>Title: �汾������</p>
*
* <p>Description: �洢�汾�ͽű���Ϣ</p>
*
* <p>Copyright: Copyright (c) 2008</p>
*
* <p>Company: ���ۿ�</p>
*
* @author ���ۿ�
* @version 1.0
* @preserve all
*/

public class UpdateInfo{
	
	private int versionId;					//�汾��
	private int orderId;					//�ű���
	private int updateType;					//��������
	private String updateInfo;				//������Ϣ
	private String updatePath;				//·��
	private String tradeInfo;				//ó����Ϣ
	private String remark;					//��ע˵��
	
	public UpdateInfo(){}
	
	public UpdateInfo(int versionId,int orderId,String remark){
		this.versionId = versionId;
		this.orderId = orderId;
		this.remark = remark;
	}
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTradeInfo() {
		return tradeInfo;
	}
	public void setTradeInfo(String tradeInfo) {
		this.tradeInfo = tradeInfo;
	}
	public String getUpdateInfo() {
		return updateInfo;
	}
	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}
	public String getUpdatePath() {
		return updatePath;
	}
	public void setUpdatePath(String updatePath) {
		this.updatePath = updatePath;
	}
	public int getUpdateType() {
		return updateType;
	}
	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}
	public int getVersionId() {
		return versionId;
	}
	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

}
