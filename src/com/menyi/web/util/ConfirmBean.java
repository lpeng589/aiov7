package com.menyi.web.util;

/**
 * <p>Title: ҳ�浯���Ƿ�ѡ����� </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: ����</p>
 *
 * @author��ѩ
 * @version 1.0
 */
public class ConfirmBean {
	String yesDefine;
	String noDefine;
	String msgContent;
	String formerDefine;
	
	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getNoDefine() {
		return noDefine;
	}

	public void setNoDefine(String noDefine) {
		this.noDefine = noDefine;
	}

	public String getYesDefine() {
		return yesDefine;
	}

	public void setYesDefine(String yesDefine) {
		this.yesDefine = yesDefine;
	}

	public ConfirmBean(String yesDefine,String noDefine,String msgContent){
		this.yesDefine=yesDefine;
		this.noDefine=noDefine;
		this.msgContent=msgContent;
	}

	public String getFormerDefine() {
		return formerDefine;
	}

	public void setFormerDefine(String formerDefine) {
		this.formerDefine = formerDefine;
	}
}
