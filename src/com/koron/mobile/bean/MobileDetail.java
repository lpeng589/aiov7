package com.koron.mobile.bean;

import java.util.List;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2012-4-13
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * @preserve all
 */
public class MobileDetail {

	private List<MobileAttrs> data ;
	private int type ;

	public List<MobileAttrs> getData() {
		return data;
	}

	public void setData(List<MobileAttrs> data) {
		this.data = data;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
