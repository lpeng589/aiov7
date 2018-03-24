package com.menyi.aio.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 登录日志
 * </p>
 * 
 * @Date:2012-9-18
 * @Copyright: 科荣软件
 * @Author 方家俊
 * @preserve all
 */

@Entity
@Table(name = "tblLoginLog")
public class LoginLogBean {

	/** identifier field */
	@Id
	private String id;

	private String userId;

	private String userName; //界面显示时直接取userName,因为用户名可能会变

	private String createTime;

	private String ip;

	private String operation;//登入：LOGIN,登出：LOGOUT

	private String terminal;//直接显示

	private String opResult;//操作结果:成功：SUCCESS,失败：FAIL

	private String reason;//失败原因
	

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOpResult() {
		return opResult;
	}

	public void setOpResult(String opResult) {
		this.opResult = opResult;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
