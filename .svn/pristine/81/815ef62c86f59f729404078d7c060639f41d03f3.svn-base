package com.koron.oa.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name="OAWorkFlowNodeCondition")
public class ConditionsBean implements Serializable {
	
	private static final long serialVersionUID = -4972361531144816615L;
	@Expose
	@Id
	private String id;
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="flowNodeId")
    private FlowNodeBean flowNode3;
	@Column(name="[to]")
	@Expose
	private String to;
	@Expose
	private String display;
	@Expose
	@OneToMany(mappedBy="conditionsBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ConditionBean> conditions=new ArrayList<ConditionBean>();

	public List<ConditionBean> getConditions() {
		return conditions;
	}
	public void setConditions(List<ConditionBean> conditions) {
		this.conditions = conditions;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public FlowNodeBean getFlowNode3() {
		return flowNode3;
	}
	public void setFlowNode3(FlowNodeBean flowNode3) {
		this.flowNode3 = flowNode3;
	}
	
}
