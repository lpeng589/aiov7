package com.koron.oa.office.goods.applyUse;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="OAApplyGoodsInfoDet")
public class OAApplyGoodsDetBean extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;              
	private double applyQty;		
	private String a_use;
	private double back_sign;
	private String goodsName;
	private String type;
	private String unit;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="a_ref")
	private OAApplyGoodsBean applyGoodsBean;
	

	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getApplyQty() {
		return applyQty;
	}
	public void setApplyQty(double applyQty) {
		this.applyQty = applyQty;
	}
	public double getBack_sign() {
		return back_sign;
	}
	public void setBack_sign(double backSign) {
		back_sign = backSign;
	}	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getA_use() {
		return a_use;
	}
	public void setA_use(String aUse) {
		a_use = aUse;
	}
	public OAApplyGoodsBean getApplyGoodsBean() {
		return applyGoodsBean;
	}
	public void setApplyGoodsBean(OAApplyGoodsBean applyGoodsBean) {
		this.applyGoodsBean = applyGoodsBean;
	}

}
