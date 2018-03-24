package com.menyi.aio.dyndb;

public class SaveErrorObj {
	private String msg;
	private String backUrl;
	
	public SaveErrorObj(String msg){
		this.msg = msg;
	}
	public SaveErrorObj(String msg ,String backUrl){
		this.msg = msg;
		this.backUrl = backUrl;
	}
	
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
