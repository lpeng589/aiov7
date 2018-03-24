package com.menyi.aio.bean;


/**
*
* <p>Title: 版本数据类</p>
*
* <p>Description: 存储版本和脚本信息</p>
*
* <p>Copyright: Copyright (c) 2008</p>
*
* <p>Company: 范雄开</p>
*
* @author 范雄开
* @version 1.0
* @preserve all
*/

public class UpdateInfo{
	
	private int versionId;					//版本号
	private int orderId;					//脚本号
	private int updateType;					//更新类型
	private String updateInfo;				//更新信息
	private String updatePath;				//路径
	private String tradeInfo;				//贸易信息
	private String remark;					//备注说明
	
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
