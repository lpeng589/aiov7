package com.koron.crm.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * <p>Title:CRM–÷µ‹±Ì∆¿¬€</p> 
 * <p>Description: </p>
 * @Date:2013-6-19
 * @Copyright: ø∆»Ÿ»Ìº˛
 * @Author:–ÏΩ‡ø°
 * @preserve all
 */
@Entity
@Table(name="CRMBrotherComment")
public class CRMBrotherCommentBean {

	@Id
	private String id;

	private String contents;
	
	private String createTime;
	
	private String employeeId;
	
	private String f_ref;
	
	private String commentId;
	
	private String commentBy;

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getF_ref() {
		return f_ref;
	}

	public void setF_ref(String f_ref) {
		this.f_ref = f_ref;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommentBy() {
		return commentBy;
	}

	public void setCommentBy(String commentBy) {
		this.commentBy = commentBy;
	}

	
	
}