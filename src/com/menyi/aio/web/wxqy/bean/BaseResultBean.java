package com.menyi.aio.web.wxqy.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信企业号返回json
 * @author pwy
 *
 */
public class BaseResultBean {
	private String errcode="0";
	private String errmsg="ok";
	private Map<String, Object> data=new HashMap<String, Object>();
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public void SetData(Map<String, Object> data){
		this.data=data;
	}
	public  Map<String, Object> getData(){
		return data;
	}
	public void addData(String name,Object value) {
		data.put(name, value);
	}
}
