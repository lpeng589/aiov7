package com.koron.oa.bean;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.google.gson.annotations.Expose;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Nov 25, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * @preserve all
 */
@Entity
@Table(name="OAWorkFlowNodeConditionDet")
public class ConditionBean implements Serializable  {

	private static final long serialVersionUID = -4000509261924141416L;
	@Expose
	@Id
	private String id;
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="conditionId")
	private ConditionsBean conditionsBean;
	@Expose
	private String fieldName;
	@Expose
	private String display;
	@Expose
	private String relation;
	@Expose
	private String relationDisplay;
	@Expose
	private String value;
	@Expose
	private String valueDisplay;
	@Expose
	private String andOr;
	@Expose
	private String groupId = "1";
	@Expose
	private String groupType = "||";
	
	public String getAndOr() {
		return andOr;
	}
	public void setAndOr(String andOr) {
		this.andOr = andOr;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getRelationDisplay() {
		return relationDisplay;
	}
	public void setRelationDisplay(String relationDisplay) {
		this.relationDisplay = relationDisplay;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValueDisplay() {
		return valueDisplay;
	}
	public void setValueDisplay(String valueDisplay) {
		this.valueDisplay = valueDisplay;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ConditionsBean getConditionsBean() {
		return conditionsBean;
	}
	public void setConditionBean(ConditionsBean conditionsBean) {
		this.conditionsBean = conditionsBean;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
}
