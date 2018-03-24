package com.menyi.aio.bean;

import java.io.Serializable;

import javax.persistence.*;


/**
 *
 * <p>Title:MRP������ </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ��־��</p>
 *
 * @author  ��־��
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name="tblProductMRP")
public class ProductMrpBean implements Serializable {

    @Id
    private String id;
    
    private String productId;//��Ʒid
    
    private String mrpFrom;//��Դ
    
    private String relationId;//��������id.
    
    private double productQty;//Ͷ������
    
    private double storeQty;//���
    
    private double safeStoreQty;//��ȫ���
    
    private double storingQty;//��;��
    
    private double productingQty;//�ڲ���
    
    private double usedQty;//�ѷ���
    
    private String employeeId;//������id
    
    private String createDate;//��������
    
    private String departmentCode;//����

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
