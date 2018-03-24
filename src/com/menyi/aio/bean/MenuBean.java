package com.menyi.aio.bean;

import java.io.Serializable;
import java.util.*;


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

public class MenuBean implements Serializable{

    private Integer id;

    // 名字（显示名）
    private String menuName;

    // 代码（根据代码判断是几级菜单及属于哪个菜单的子菜单）
    private String code;

    //上一级菜单
    private MenuBean superMenu;

    // 子菜单（自关联的项）
    private List<MenuBean> childMenuList = new ArrayList<MenuBean>();

    // 对应所要操作的表
    private String tableName;

    private String linkAddress;

    public MenuBean() {}

    public MenuBean(Integer id,String menuName,String code,
                    String tableName,String linkAddress){

        this.id = id;
        this.menuName = menuName;
        this.code = code;
        this.tableName = tableName;
        this.linkAddress = linkAddress;
    }

    public void setId(Integer id){

        this.id = id;
    }

    public Integer getId(){

        return this.id;
    }

    public void setMenuName(String menuName){

        this.menuName = menuName;
    }

    public String getMenuName(){

        return this.menuName;
    }

    public void setCode(String code){

        this.code = code;
    }

    public String getCode(){

        return this.code;
    }

    public void setSuperMenu(MenuBean superMenu){

        this.superMenu = superMenu;
    }

    public MenuBean getSuperMenu(){

        return this.superMenu;
    }

    public void setChildMenuList(List<MenuBean> childMenuList){

        this.childMenuList = childMenuList;
    }

    public List<MenuBean> getChildMenuList()
    {
        return this.childMenuList;
    }

    public void setTableName(String tableName){

        this.tableName = tableName;
    }

    public String getTableName(){

        return this.tableName;
    }

    public void setLinkAddress(String linkAddress){

        this.linkAddress = linkAddress;
    }

    public String getLinkAddress(){

        return this.linkAddress;
    }
}
