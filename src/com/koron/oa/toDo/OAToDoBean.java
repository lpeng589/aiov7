package com.koron.oa.toDo;

import java.io.Serializable;
import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;
/**
 * 
 * <p>Title:版块</p> 
 * <p>Description: </p>
 *
 * @Date:2013-11-10
 * @Copyright: 科荣软件
 * @Author wyy
 * @preserve all
 */
@Entity
@Table(name="OAToDo")
public class OAToDoBean extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String title;
	private String finishTime;
	private String createBy;
	private String createTime;
	private String type;
	private String alertId;
	private String relationId;
	private String status;
	private String ref_taskId;//任务Id
	private String alertTime="";
	private String uploadFile;
	
	public OAToDoBean(){
		
	}
	
	
	public String getUploadFile() {
		return uploadFile;
	}


	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}


	public String getRef_taskId() {
		return ref_taskId;
	}

	public void setRef_taskId(String ref_taskId) {
		this.ref_taskId = ref_taskId;
	}

	public String getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlertId() {
		return alertId;
	}
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
		
}
