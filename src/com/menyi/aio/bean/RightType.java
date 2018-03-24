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
    private String id;//���ɫ���еĽ�ɫID�����
//    /** persistent field */
//    @Column(nullable = false,length=50 )
    private String rightType;//Ȩ������(����������,��������������,�������������,�������ڳɱ��۳���)
    private String hasRight;//�Ƿ�ӵ�и�Ȩ�ޣ�1��ʾ�У�0��ʾ��
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

