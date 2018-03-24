package com.menyi.aio.bean;

import java.util.HashMap;

/**
 *  图形报表
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Mar 23, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class FusionBean {

	private String type ;	/*类型*/
	private String caption ;/*顶部标题*/
	private String xAxis ;	/*X轴标题*/
	private String yAxis ;	/*Y轴标题*/
	private String[] categorys ;/*3D*/
	private HashMap values ;
	
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getXAxis() {
		return xAxis;
	}
	public void setXAxis(String axis) {
		xAxis = axis;
	}
	public String getYAxis() {
		return yAxis;
	}
	public void setYAxis(String axis) {
		yAxis = axis;
	}
	public HashMap getValues() {
		return values;
	}
	public void setValues(HashMap values) {
		this.values = values;
	}
	public String[] getCategorys() {
		return categorys;
	}
	public void setCategorys(String[] categorys) {
		this.categorys = categorys;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
