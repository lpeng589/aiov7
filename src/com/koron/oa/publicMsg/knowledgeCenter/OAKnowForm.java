package com.koron.oa.publicMsg.knowledgeCenter;

import com.menyi.web.util.BaseForm;


/**
 * 
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-13
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class OAKnowForm extends BaseForm{
		
	private String id;
	private String classCode;
	private String folderId;
	private String description;
	private String fileName;
	private String filePath;
	private int isAlonePopedom;
	private String popedomUserIds;
	private int statusId;
	private String popedomDeptIds;
	private String fileTitle;
	private String popedomEmpGroupIds;
	private String isSaveReading;
	private String oldFolderId;
	private String createBy;
	private String createTime;
	
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getIsAlonePopedom() {
		return isAlonePopedom;
	}
	public void setIsAlonePopedom(int isAlonePopedom) {
		this.isAlonePopedom = isAlonePopedom;
	}
	public String getIsSaveReading() {
		return isSaveReading;
	}
	public void setIsSaveReading(String isSaveReading) {
		this.isSaveReading = isSaveReading;
	}
	public String getOldFolderId() {
		return oldFolderId;
	}
	public void setOldFolderId(String oldFolderId) {
		this.oldFolderId = oldFolderId;
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
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
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
	
	
}
