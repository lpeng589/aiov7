package com.koron.crm.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * 
 * <p>Title:行业表</p> 
 * <p>Description: </p>
 * @Date:2012-10-24
 * @Copyright: 科荣软件
 * @Author:徐洁俊
 * @preserve all
 */
@Entity
@Table(name="CRMWorkTrade")
public class WorkTradeBean {

	@Id
	private String id;

	private String name;
	
	@OneToMany(mappedBy="workTradeBean", fetch = FetchType.EAGER)
    private List<WorkProfessionBean> workProfessionBeans;

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

	public List<WorkProfessionBean> getWorkProfessionBeans() {
		return workProfessionBeans;
	}

	public void setWorkProfessionBeans(List<WorkProfessionBean> workProfessionBeans) {
		this.workProfessionBeans = workProfessionBeans;
	}

	
	
	
}