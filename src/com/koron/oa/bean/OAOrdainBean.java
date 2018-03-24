package com.koron.oa.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * <p>Title:新闻中心</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-5
 * @Copyright: 科荣软件
 * @Author 李文祥
 * @preserve all
 */
@Entity
@Table(name="OAOrdainInfo")
public class OAOrdainBean {
	
	@Id
	private String id;
	private String groupId;
	private String ordainTitle;
	private String content;
	private String accessories;
	private String createBy;
	private String lastupDateBy;
	private String createTime;
	private String lastupDateTime;
	private int statusId;
	private String scompanyId;
	private String wakeupType;
	private String isAlonePopedmon;
	private String popedomUserIds;
	private String popedomDeptIds;
	private String popedomEmpGroupIds;
	private String isSaveReading;
	private String oldGroupId;
/*	@ManyToOne //(cascade=CascadeType.ALL)
	@JoinColumn(name="GroupID",unique=true,referencedColumnName="classCode")
	private OAOrdainGroupBean grBean ; 所属组*/
	
	
	public String getAccessories() {
		return accessories;
	}
	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}
	
    
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getLastupDateBy() {
		return lastupDateBy;
	}
	public void setLastupDateBy(String lastupDateBy) {
		this.lastupDateBy = lastupDateBy;
	}
	public String getLastupDateTime() {
		return lastupDateTime;
	}
	public void setLastupDateTime(String lastupDateTime) {
		this.lastupDateTime = lastupDateTime;
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
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getWakeupType() {
		return wakeupType;
	}
	public void setWakeupType(String wakeupType) {
		this.wakeupType = wakeupType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
/*	public OAOrdainGroupBean getGrBean() {
		return grBean;
	}
	public void setGrBean(OAOrdainGroupBean grBean) {
		this.grBean = grBean;
	}*/
			
}
