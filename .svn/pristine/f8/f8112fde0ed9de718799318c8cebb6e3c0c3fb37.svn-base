package com.menyi.aio.bean;

import java.io.Serializable;

import javax.persistence.*;



/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 * @preserve all
 */
//@Entity
//@Table(name="tblRightType")
public class RightType implements Serializable {

    /** identifier field */
//    @Id
//    @Column(nullable = false,length=30)
    private String id;//与角色表中的角色ID相关联
//    /** persistent field */
//    @Column(nullable = false,length=50 )
    private String rightType;//权限类型(允许负库存过账,允许超订单数出库,允许超订单数入库,允许少于成本价出库)
    private String hasRight;//是否拥有该权限，1表示有，0表示无
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRightType() {
		return rightType;
	}
	public void setRightType(String rightType) {
		this.rightType = rightType;
	}
	public String getHasRight() {
		return hasRight;
	}
	public void setHasRight(String hasRight) {
		this.hasRight = hasRight;
	}
}

