package com.koron.crm.bean;

import java.io.Serializable;

import javax.persistence.*;

/**
 * 客户关怀方案tblCarefor的实体Bean
 * @version CRM4.3
 * @author 徐磊
 * @preserve all
 */
@Entity
@Table(name = "tblCarefor")
public class CareforBean implements Serializable{

	@Id
    @Column(nullable = false, length = 50)
	private String id = "";
	
	@Column(nullable = false, length = 100)
	private String name = "";

	@Column(nullable = false,length=2)
	private String status ;

	
	public String getId() {
		return this.id;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}