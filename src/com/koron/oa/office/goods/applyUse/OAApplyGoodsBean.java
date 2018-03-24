package com.koron.oa.office.goods.applyUse;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

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
@Table(name="OAApplyGoodsInfo")
public class OAApplyGoodsBean extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;    
	@OneToMany(mappedBy="applyGoodsBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OAApplyGoodsDetBean> applyGoodsDetBean;
	private String applyNO;		
	private String applyRole;		
	private String applyDate;
	private double apply_qty;
	private String apply_title;
	

	public double getApply_qty() {
		return apply_qty;
	}
	public void setApply_qty(double applyQty) {
		apply_qty = applyQty;
	}
	public String getApply_title() {
		return apply_title;
	}
	public void setApply_title(String applyTitle) {
		apply_title = applyTitle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplyNO() {
		return applyNO;
	}
	public void setApplyNO(String applyNO) {
		this.applyNO = applyNO;
	}
	public String getApplyRole() {
		return applyRole;
	}
	public void setApplyRole(String applyRole) {
		this.applyRole = applyRole;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public List<OAApplyGoodsDetBean> getApplyGoodsDetBean() {
		return applyGoodsDetBean;
	}
	public void setApplyGoodsDetBean(List<OAApplyGoodsDetBean> applyGoodsDetBean) {
		this.applyGoodsDetBean = applyGoodsDetBean;
	}
	

}
