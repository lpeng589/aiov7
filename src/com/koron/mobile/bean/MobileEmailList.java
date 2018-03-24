package com.koron.mobile.bean;

import java.util.List;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 22, 2012
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
public class MobileEmailList {
	private String id;
	private String name;
	private boolean intranet;
	private String accounts;
	private List<MobileFolder> folders;
	
	public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isIntranet() {
		return intranet;
	}
	public void setIntranet(boolean intranet) {
		this.intranet = intranet;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MobileFolder> getFolders() {
		return folders;
	}
	public void setFolders(List<MobileFolder> folders) {
		this.folders = folders;
	}
	
}
