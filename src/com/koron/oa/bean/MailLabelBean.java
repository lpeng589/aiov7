package com.koron.oa.bean;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
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
 * @preserve all
 */
@Entity
@Table(name = "tblMailLabel")
public class MailLabelBean {
    /** identifier field */
    @Id
    private int id;
 
    private String name;
    private String pic;
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}   



}
