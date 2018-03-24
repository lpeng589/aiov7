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
public class MobileWorkFlow {

	private String id  ;
	private String creator ;
	private String currentNode;
	private String title ;
	private int flag;
	private String type ;
	private String detailURL;
	private String createTime ;
	private boolean cancelEnabled;
	private List<MobileFlowNode> nodes ;
	private List<MobileFlowAction> actions ;
	private List<MobileFlowAction> cancelactions ;
	private List<Properties> attrs ;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public boolean isCancelEnabled() {
		return cancelEnabled;
	}
	public void setCancelEnabled(boolean cancelEnabled) {
		this.cancelEnabled = cancelEnabled;
	}
	public String getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(String currentNode) {
		this.currentNode = currentNode;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public List<MobileFlowNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<MobileFlowNode> nodes) {
		this.nodes = nodes;
	}
	public List<Properties> getAttrs() {
		return attrs;
	}
	public void setAttrs(List<Properties> attrs) {
		this.attrs = attrs;
	}
	public List<MobileFlowAction> getActions() {
		return actions;
	}
	public void setActions(List<MobileFlowAction> actions) {
		this.actions = actions;
	}
	public List<MobileFlowAction> getCancelactions() {
		return cancelactions;
	}
	public void setCancelactions(List<MobileFlowAction> cancelactions) {
		this.cancelactions = cancelactions;
	}
	public String getDetailURL() {
		return detailURL;
	}
	public void setDetailURL(String detailURL) {
		this.detailURL = detailURL;
	}
}
