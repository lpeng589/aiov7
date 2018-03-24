package com.koron.crm.weixin.bean;

import java.io.Serializable;

/**
 * 
 * @preserve all
 * 
 */

public class ClientTokenBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String tokenid ;

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}
	
}