package com.koron.crm.weixin.bean;

import java.io.Serializable;

/**
 * 
 * @preserve all
 * 
 */

public class ClientMessageBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int messageCode;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}
	
}