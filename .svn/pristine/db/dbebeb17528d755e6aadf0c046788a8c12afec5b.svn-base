package com.koron.oa.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;



/**
 * 
 * <p>Title:图片设置 硬盘设置</p> 
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */

@Entity
@Table(name = "directorySetting")
public class DirectorySetting {
	@Id
	@Column(nullable = false, length = 60)
	private String id;
	private String userId;
	private String path; //目录path
	private String name;//目录名称
	private String createTime ;
	private String lastUpdateTime ;
	private Integer treeNo ;//菜单编号,
	private Integer isRoot ;//是否是通过设置创建的
	private String shareuserId;//员工编号集合
	private String shareDeptOfClassCode;//部门编号 集合
	private String shareEmpGroup;//--职员分组 集合
	
	private String downLoadUserId;
	private String downLoadDeptOfClassCode;
	private String downLoadEmpGroup;
	@Transient
	private List<Employee> shareUserNames;
	@Transient
	private List<Department> shareDepts;
	@Transient
	private List<String[]> shareEmpGroups;
	@Transient
	private List<Employee> downLoadUserNames;
	@Transient
	private List<Department> downLoadDepts;
	@Transient
	private List<String[]> downLoadGroups;
	
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIsRoot() {
		return isRoot;
	}
	public void setIsRoot(Integer isRoot) {
		this.isRoot = isRoot;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getShareDeptOfClassCode() {
		return shareDeptOfClassCode;
	}
	public void setShareDeptOfClassCode(String shareDeptOfClassCode) {
		this.shareDeptOfClassCode = shareDeptOfClassCode;
	}
	public String getShareEmpGroup() {
		return shareEmpGroup;
	}
	public void setShareEmpGroup(String shareEmpGroup) {
		this.shareEmpGroup = shareEmpGroup;
	}
	public String getShareuserId() {
		return shareuserId;
	}
	public void setShareuserId(String shareuserId) {
		this.shareuserId = shareuserId;
	}
	public Integer getTreeNo() {
		return treeNo;
	}
	public void setTreeNo(Integer treeNo) {
		this.treeNo = treeNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public List<String[]> getShareEmpGroups() {
		return shareEmpGroups;
	}
	public void setShareEmpGroups(List<String[]> shareEmpGroups) {
		this.shareEmpGroups = shareEmpGroups;
	}
	public List<Department> getShareDepts() {
		return shareDepts;
	}
	public void setShareDepts(List<Department> shareDepts) {
		this.shareDepts = shareDepts;
	}
	public List<Employee> getShareUserNames() {
		return shareUserNames;
	}
	public void setShareUserNames(List<Employee> shareUserNames) {
		this.shareUserNames = shareUserNames;
	}
	public String getDownLoadDeptOfClassCode() {
		return downLoadDeptOfClassCode;
	}
	public void setDownLoadDeptOfClassCode(String downLoadDeptOfClassCode) {
		this.downLoadDeptOfClassCode = downLoadDeptOfClassCode;
	}
	public List<Department> getDownLoadDepts() {
		return downLoadDepts;
	}
	public void setDownLoadDepts(List<Department> downLoadDepts) {
		this.downLoadDepts = downLoadDepts;
	}
	public String getDownLoadEmpGroup() {
		return downLoadEmpGroup;
	}
	public void setDownLoadEmpGroup(String downLoadEmpGroup) {
		this.downLoadEmpGroup = downLoadEmpGroup;
	}
	public List<String[]> getDownLoadGroups() {
		return downLoadGroups;
	}
	public void setDownLoadGroups(List<String[]> downLoadGroups) {
		this.downLoadGroups = downLoadGroups;
	}
	public String getDownLoadUserId() {
		return downLoadUserId;
	}
	public void setDownLoadUserId(String downLoadUserId) {
		this.downLoadUserId = downLoadUserId;
	}
	public List<Employee> getDownLoadUserNames() {
		return downLoadUserNames;
	}
	public void setDownLoadUserNames(List<Employee> downLoadUserNames) {
		this.downLoadUserNames = downLoadUserNames;
	}

	
}
