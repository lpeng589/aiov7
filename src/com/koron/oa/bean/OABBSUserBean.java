package com.koron.oa.bean;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:论坛用户</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-14
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
@Entity
@Table(name="OABBSUsers")
public class OABBSUserBean {

	@Id
	private String id ;
	private String userID ;
	private String nickName ;
	private String signature ;
	private int userscore ;
	private int statusId ;
	private String createBy  ;
	private String createTime ;
	private String fullName;
	
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public int getUserscore() {
		return userscore;
	}
	public void setUserscore(int userscore) {
		this.userscore = userscore;
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
