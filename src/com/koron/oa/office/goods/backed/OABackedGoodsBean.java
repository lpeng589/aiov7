package com.koron.oa.office.goods.backed;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.dom4j.tree.AbstractEntity;

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
@Table(name="OABackedGoodsInfo")
public class OABackedGoodsBean extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;              
	private String backedRole;	
	private String backNO;		
	private String backDate;
	private double back_qty;
	private String back_title;
	@OneToMany(mappedBy="backedGoodsBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OABackedGoodsDetBean> backedGoodsDetBean;
	
	
	public double getBack_qty() {
		return back_qty;
	}
	public void setBack_qty(double backQty) {
		back_qty = backQty;
	}
	public String getBack_title() {
		return back_title;
	}
	public void setBack_title(String backTitle) {
		back_title = backTitle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBackedRole() {
		return backedRole;
	}
	public void setBackedRole(String backedRole) {
		this.backedRole = backedRole;
	}
	public String getBackNO() {
		return backNO;
	}
	public void setBackNO(String backNO) {
		this.backNO = backNO;
	}
	public String getBackDate() {
		return backDate;
	}
	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}
	public List<OABackedGoodsDetBean> getBackedGoodsDetBean() {
		return backedGoodsDetBean;
	}
	public void setBackedGoodsDetBean(List<OABackedGoodsDetBean> backedGoodsDetBean) {
		this.backedGoodsDetBean = backedGoodsDetBean;
	}
	
	
}
