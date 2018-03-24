package com.menyi.aio.bean;

import java.io.Serializable;

import javax.persistence.*;


/**
 *
 * <p>Title:MRP运算结果 </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 刘志高</p>
 *
 * @author  刘志高
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name="tblProductMRP")
public class ProductMrpBean implements Serializable {

    @Id
    private String id;
    
    private String productId;//商品id
    
    private String mrpFrom;//来源
    
    private String relationId;//关联单据id.
    
    private double productQty;//投产数量
    
    private double storeQty;//库存
    
    private double safeStoreQty;//安全库存
    
    private double storingQty;//在途量
    
    private double productingQty;//在产量
    
    private double usedQty;//已分配
    
    private String employeeId;//经手人id
    
    private String createDate;//创建日期
    
    private String departmentCode;//部门

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMrpFrom() {
		return mrpFrom;
	}

	public void setMrpFrom(String mrpFrom) {
		this.mrpFrom = mrpFrom;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public double getProductingQty() {
		return productingQty;
	}

	public void setProductingQty(double productingQty) {
		this.productingQty = productingQty;
	}

	public double getProductQty() {
		return productQty;
	}

	public void setProductQty(double productQty) {
		this.productQty = productQty;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public double getSafeStoreQty() {
		return safeStoreQty;
	}

	public void setSafeStoreQty(double safeStoreQty) {
		this.safeStoreQty = safeStoreQty;
	}

	public double getStoreQty() {
		return storeQty;
	}

	public void setStoreQty(double storeQty) {
		this.storeQty = storeQty;
	}

	public double getStoringQty() {
		return storingQty;
	}

	public void setStoringQty(double storingQty) {
		this.storingQty = storingQty;
	}

	public double getUsedQty() {
		return usedQty;
	}

	public void setUsedQty(double usedQty) {
		this.usedQty = usedQty;
	}
    
    
}
