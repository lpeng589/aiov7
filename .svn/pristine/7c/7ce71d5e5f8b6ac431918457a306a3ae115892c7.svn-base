package com.koron.oa.office.goods.backed;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dom4j.tree.AbstractEntity;

import com.koron.oa.office.goods.applyUse.OAApplyGoodsDetBean;

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
@Table(name="OABackedGoodsInfoDet")
public class OABackedGoodsDetBean extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;              		
	private double backedQty;		
	private String B_remark;
	private String backId;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="b_ref")
	private OABackedGoodsBean backedGoodsBean;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="backId",insertable=false,updatable=false)
	private OAApplyGoodsDetBean applyDetBean;
	
	
	public OAApplyGoodsDetBean getApplyDetBean() {
		return applyDetBean;
	}
	public void setApplyDetBean(OAApplyGoodsDetBean applyDetBean) {
		this.applyDetBean = applyDetBean;
	}
	public String getBackId() {
		return backId;
	}
	public void setBackId(String backId) {
		this.backId = backId;
	}
	public OABackedGoodsBean getBackedGoodsBean() {
		return backedGoodsBean;
	}
	public void setBackedGoodsBean(OABackedGoodsBean backedGoodsBean) {
		this.backedGoodsBean = backedGoodsBean;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
	public double getBackedQty() {
		return backedQty;
	}
	public void setBackedQty(double backedQty) {
		this.backedQty = backedQty;
	}
	public String getB_remark() {
		return B_remark;
	}
	public void setB_remark(String bRemark) {
		B_remark = bRemark;
	}			
	
	
	
}
