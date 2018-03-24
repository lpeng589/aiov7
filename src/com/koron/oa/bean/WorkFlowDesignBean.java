package com.koron.oa.bean;

import java.util.HashMap;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class WorkFlowDesignBean {
    private HashMap<String,FlowNodeBean> flowNodeMap = new HashMap<String,FlowNodeBean>();

	public HashMap<String, FlowNodeBean> getFlowNodeMap() {
		return flowNodeMap;
	}
	
	public void setFlowNodeMap(HashMap<String, FlowNodeBean> flowNodeMap) {
		this.flowNodeMap = flowNodeMap;
	}
}


