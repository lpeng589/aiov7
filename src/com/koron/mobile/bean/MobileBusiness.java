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
public class MobileBusiness {

	private String[] headTitle ;
	private List<String[]> body ;
	
	public String[] getHeadTitle() {
		return headTitle;
	}
	public void setHeadTitle(String[] headTitle) {
		this.headTitle = headTitle;
	}
	public List<String[]> getBody() {
		return body;
	}
	public void setBody(List<String[]> body) {
		this.body = body;
	}
}
