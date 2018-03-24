package com.menyi.aio.bean;


import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;

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

@Entity
@Table(name = "tblModelOperations")
public class ModuleOperationBean extends AbstractEntity {


    @Id
    @Column(nullable = false)
    private int id;

    @Column(nullable=false)
    private int operationID;
    
    @Column(nullable=false)
    private int moduleOpId;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="f_ref")
    private ModuleBean moduleBean; //对应主表

    public ModuleOperationBean(){
        super();
    }

    public ModuleOperationBean(int id){
        super();
        this.id = id;
    }



    public int getId() {
        return id;
    }

    public ModuleBean getModuleBean() {
        return moduleBean;
    }

    public int getOperationID() {
        return operationID;
    }


    public void setOperationID(int operationID) {
        this.operationID = operationID;
    }

    public void setModuleBean(ModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }

    public void setId(int id) {
        this.id = id;
    }

	public int getModuleOpId() {
		return moduleOpId;
	}

	public void setModuleOpId(int moduleOpId) {
		this.moduleOpId = moduleOpId;
	}
    
    


}
