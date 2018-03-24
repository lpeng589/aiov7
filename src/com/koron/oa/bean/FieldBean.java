package com.koron.oa.bean;

import java.io.Serializable;

import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;

import com.google.gson.annotations.Expose;


/**
 * 
 * <p>Title:结点的字段设置</p> 
 * <p>Description: </p>
 *
 * @Date:Nov 13, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * @preserve all
 */

@Entity
@Table(name="OAWorkFlowNodeField")
public class FieldBean  extends AbstractEntity implements Serializable  {
	
	private static final long serialVersionUID = -4722878268431977994L;
	@Expose
	@Id
	private String id ;
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="flowNodeId")
	private FlowNodeBean flowNode;
	@Expose
	private String fieldName;	//字段名称
	@Expose
	private String fieldType;
	@Expose
	private byte inputType=0;	//字段设置类型
	@Expose
	private boolean isNotNull;	//是否必填
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public FlowNodeBean getFlowNode() {
		return flowNode;
	}
	public void setFlowNode(FlowNodeBean flowNode) {
		this.flowNode = flowNode;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public byte getInputType() {
		return inputType;
	}
	public void setInputType(byte inputType) {
		this.inputType = inputType;
	}
	public boolean isNotNull() {
		return isNotNull;
	}
	public void setNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
	
	
	
}
