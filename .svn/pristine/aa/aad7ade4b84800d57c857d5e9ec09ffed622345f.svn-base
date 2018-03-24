package com.koron.oa.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * <p>工作计划权限查看</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 
 * @Author
 * @preserve all
 */

@Entity
@Table(name = "OAJobPopedomSetting")
public class OAWorkPopedomeBean {
	@Id
	private String id;
	private String seePersonId;			//查看人ID 可以为多个
	private String desContent;	  		//查看描述
	private String createTime;
	private String lastUpdateTime;	
	private String seeType;					//查看类型
	private String bySeeUserID; 		 //被查看人的ID
	private String bySeeDeptOfClassCode; //被查看人的部门ClassCode
	private String bySeeEmpGroup; 		 // 被查看人的分组ID	
	@Transient
	private List<Employee> seePersonNames;
	@Transient
	private List<Employee> bySeeUserNames;
	@Transient
	private List<Department> bySeeDepts;
	@Transient
	private List<String[]> bySeeEmpGroups;
	
	public String getBySeeDeptOfClassCode() {
		return bySeeDeptOfClassCode;
	}
	public void setBySeeDeptOfClassCode(String bySeeDeptOfClassCode) {
		this.bySeeDeptOfClassCode = bySeeDeptOfClassCode;
	}
	public List<Department> getBySeeDepts() {
		return bySeeDepts;
	}
	public void setBySeeDepts(List<Department> bySeeDepts) {
		this.bySeeDepts = bySeeDepts;
	}
	public String getBySeeEmpGroup() {
		return bySeeEmpGroup;
	}
	public void setBySeeEmpGroup(String bySeeEmpGroup) {
		this.bySeeEmpGroup = bySeeEmpGroup;
	}
	public List<String[]> getBySeeEmpGroups() {
		return bySeeEmpGroups;
	}
	public void setBySeeEmpGroups(List<String[]> bySeeEmpGroups) {
		this.bySeeEmpGroups = bySeeEmpGroups;
	}
	public String getBySeeUserID() {
		return bySeeUserID;
	}
	public void setBySeeUserID(String bySeeUserID) {
		this.bySeeUserID = bySeeUserID;
	}
	public List<Employee> getBySeeUserNames() {
		return bySeeUserNames;
	}
	public void setBySeeUserNames(List<Employee> bySeeUserNames) {
		this.bySeeUserNames = bySeeUserNames;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDesContent() {
		return desContent;
	}
	public void setDesContent(String desContent) {
		this.desContent = desContent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getSeePersonId() {
		return seePersonId;
	}
	public void setSeePersonId(String seePersonId) {
		this.seePersonId = seePersonId;
	}
	public List<Employee> getSeePersonNames() {
		return seePersonNames;
	}
	public void setSeePersonNames(List<Employee> seePersonNames) {
		this.seePersonNames = seePersonNames;
	}
	public String getSeeType() {
		return seeType;
	}
	public void setSeeType(String seeType) {
		this.seeType = seeType;
	}
	
}
