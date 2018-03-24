package com.koron.oa.office.car.carInfo;

import java.io.Serializable;

import org.dom4j.tree.AbstractEntity;

import javax.persistence.*;

/**
 * 
 * <p>Title:°æ¿é</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-14
 * @Copyright: ¿ÆÈÙÈí¼þ
 * @Author wyy
 * @preserve all
 */
@Entity
@Table(name="OACarCheck")
public class OACarCheckBean extends AbstractEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String role;
	private String r_ref;
	
	
	public String getR_ref() {
		return r_ref;
	}
	public void setR_ref(String r_ref) {
		this.r_ref = r_ref;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
