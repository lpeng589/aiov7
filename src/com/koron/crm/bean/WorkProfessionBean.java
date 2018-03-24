package com.koron.crm.bean;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 
 * <p>Title:行业职业表</p> 
 * <p>Description: </p>
 * @Date:2012-10-24
 * @Copyright: 科荣软件
 * @Author:徐洁俊
 * @preserve all
 */
@Entity
@Table(name="CRMWorkProfession")
public class WorkProfessionBean {

	@Id
	private String id;

	private String name;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="workTradeId")
    private WorkTradeBean workTradeBean; //对应主表
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WorkTradeBean getWorkTradeBean() {
		return workTradeBean;
	}

	public void setWorkTradeBean(WorkTradeBean workTradeBean) {
		this.workTradeBean = workTradeBean;
	}


	
	
}