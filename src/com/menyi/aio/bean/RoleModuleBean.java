package com.menyi.aio.bean;

import java.io.Serializable;
import java.util.List;

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
@Entity
@Table(name="tblRoleModule")
public class RoleModuleBean implements Serializable {


	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "roleid")
    private RoleBean roleBean; //对应主表 */

    //@Column(nullable = false ,length=30)
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "moduleOpId")
    private ModuleOperationBean moduleOpBean;
    //private String moduleOpId;


    @Column(nullable = true ,length=1)
    private String displayFlag;



    public int getId() {
        return id;
    }



    public String getDisplayFlag() {
        return displayFlag;
    }



    public RoleBean getRoleBean() {
        return roleBean;
    }

//    public String getModuleOpId() {
//        return moduleOpId;
//    }


    public ModuleOperationBean getModuleOpBean() {
        return moduleOpBean;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDisplayFlag(String displayFlag) {
        this.displayFlag = displayFlag;
    }



    public void setRoleBean(RoleBean roleBean) {
        this.roleBean = roleBean;
    }

//    public void setModuleOpId(String moduleOpId) {
//        this.moduleOpId = moduleOpId;
//    }


    public void setModuleOpBean(ModuleOperationBean moduleOpBean) {
        this.moduleOpBean = moduleOpBean;
    }

}

