package com.koron.oa.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:字段设置
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-11-12
 * @Copyright: 科荣软件
 * @Author 徐杰俊
 * @preserve all
 */

@Entity
@Table(name = "OAWorkLogDet")
public class OAWorkLogDetBean {
	
	@Id
	private String id; 

	private String contents;//内容

	private String contentType;// 内容类型，1:总结,2:计划

	private String relationType;// 关联的类型 todo:待办,task:任务: calendar:日程

	private String relationId;//关联ID

	private String workLogId;// 主表外键
	
	private String schedule;// 完成进度(%)
	
	private String createTime;//创建时间
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getWorkLogId() {
		return workLogId;
	}

	public void setWorkLogId(String workLogId) {
		this.workLogId = workLogId;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
