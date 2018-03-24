package com.menyi.aio.web.usermanage;

import com.menyi.web.util.BaseSearchForm;
/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class UserSearchForm extends BaseSearchForm {
	private String sysName;
	private String roleName;
	
	private String titleid;
	
	private String departMent;
	

	
	public String getDepartMent() {
		return departMent;
	}

	public void setDepartMent(String departMent) {
		this.departMent = departMent;
	}

	public String getTitleid() {
		return titleid;
	}

	public void setTitleid(String titleid) {
		this.titleid = titleid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	
}
