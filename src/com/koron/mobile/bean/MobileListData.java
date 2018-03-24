package com.koron.mobile.bean;

import java.util.List;
import java.util.Properties;
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
public class MobileListData {

	private int type = 1 ;
	private int layout ;
	private String parameterKey ;
	private String parameterKey2 ;
	private String valuename1 ;
	private String valuename2 ;
	private String valuename3 ;
	private List<Properties> data ;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLayout() {
		return layout;
	}
	public void setLayout(int layout) {
		this.layout = layout;
	}
	public String getParameterKey() {
		return parameterKey;
	}
	public void setParameterKey(String parameterKey) {
		this.parameterKey = parameterKey;
	}
	public List<Properties> getData() {
		return data;
	}
	public void setData(List<Properties> data) {
		this.data = data;
	}
	public String getValuename1() {
		return valuename1;
	}
	public void setValuename1(String valuename1) {
		this.valuename1 = valuename1;
	}
	public String getValuename2() {
		return valuename2;
	}
	public void setValuename2(String valuename2) {
		this.valuename2 = valuename2;
	}
	public String getValuename3() {
		return valuename3;
	}
	public void setValuename3(String valuename3) {
		this.valuename3 = valuename3;
	}
	public String getParameterKey2() {
		return parameterKey2;
	}
	public void setParameterKey2(String parameterKey2) {
		this.parameterKey2 = parameterKey2;
	}
	
}
