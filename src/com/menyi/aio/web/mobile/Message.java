package com.menyi.aio.web.mobile;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 22, 2012
 * @Copyright: 科荣软件
 * @Author 周新宇
 * @preserve all
 */
public class Message {

	private String code;
	private String msg;
	private Object obj;
	
	public Message(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	public Message(String code,String msg,Object obj){
		this.code = code;
		this.msg=msg;
		this.obj = obj;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
	
	
}
