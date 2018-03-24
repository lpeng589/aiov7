package com.koron.oa.publicMsg.newordain;

import com.menyi.web.util.BaseForm;

public class OAOrdainForm extends BaseForm {
	
	private static final long serialVersionUID = 1L;
	private String ordainId;
	private String groupId;
	private String ordainTitle;
	private String content;
	private String accessories;
	private String lastUpdateBy;
	private String wakeupType;
	private String scompanyId;
	private String isAlonePopedmon;
	private String popedomUserIds; 
	private String popedomDeptIds;
	private String popedomEmpGroupIds;  
	private String isSaveReading;
	private String oldGroupId;

	public String getAccessories() {
		return accessories;
	}
	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}
		
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getIsAlonePopedmon() {
		return isAlonePopedmon;
	}
	public void setIsAlonePopedmon(String isAlonePopedmon) {
		this.isAlonePopedmon = isAlonePopedmon;
	}
	public String getIsSaveReading() {
		return isSaveReading;
	}
	public void setIsSaveReading(String isSaveReading) {
		this.isSaveReading = isSaveReading;
	}
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public String getOldGroupId() {
		return oldGroupId;
	}
	public void setOldGroupId(String oldGroupId) {
		this.oldGroupId = oldGroupId;
	}
	public String getOrdainTitle() {
		return ordainTitle;
	}
	public void setOrdainTitle(String ordainTitle) {
		this.ordainTitle = ordainTitle;
	}
	public String getPopedomDeptIds() {
		return popedomDeptIds;
	}
	public void setPopedomDeptIds(String popedomDeptIds) {
		this.popedomDeptIds = popedomDeptIds;
	}
	public String getPopedomEmpGroupIds() {
		return popedomEmpGroupIds;
	}
	public void setPopedomEmpGroupIds(String popedomEmpGroupIds) {
		this.popedomEmpGroupIds = popedomEmpGroupIds;
	}
	public String getPopedomUserIds() {
		return popedomUserIds;
	}
	public void setPopedomUserIds(String popedomUserIds) {
		this.popedomUserIds = popedomUserIds;
	}
	public String getScompanyId() {
		return scompanyId;
	}
	public void setScompanyId(String scompanyId) {
		this.scompanyId = scompanyId;
	}

	public String getWakeupType() {
		return wakeupType;
	}
	public void setWakeupType(String wakeupType) {
		this.wakeupType = wakeupType;
	}
	public String getOrdainId() {
		return ordainId;
	}
	public void setOrdainId(String ordainId) {
		this.ordainId = ordainId;
	}
	
	
	
}
