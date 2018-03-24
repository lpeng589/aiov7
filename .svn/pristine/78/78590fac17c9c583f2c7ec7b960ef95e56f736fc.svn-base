package com.menyi.aio.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Aioshop entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AIOSHOP")
public class AIOShopBean implements java.io.Serializable {

	// Fields

	private String id;
	private String linkAddr;
	private Integer statusId;
	private String lastUpdateTime ;

	// Constructors

	/** default constructor */
	public AIOShopBean() {
	}

	/** full constructor */
	public AIOShopBean(String linkAddr, Integer statusId) {
		this.linkAddr = linkAddr;
		this.statusId = statusId;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "linkAddr", length = 100)
	public String getLinkAddr() {
		return this.linkAddr;
	}

	public void setLinkAddr(String linkAddr) {
		this.linkAddr = linkAddr;
	}

	@Column(name = "statusId")
	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	@Column(name = "lastUpdateTime")
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	

}