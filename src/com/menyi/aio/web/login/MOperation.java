package com.menyi.aio.web.login;

import java.util.ArrayList;
import java.util.List;

import com.menyi.aio.bean.RoleScopeBean;
import java.io.Serializable;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class MOperation implements Serializable{
    public static final int M_QUERY = 1;
    public static final int M_ADD = 2;
    public static final int M_UPDATE = 3;
    public static final int M_DELETE = 4;
    public static final int M_PRINT = 6;
    public static final int M_SENDMAIL = 8;

    public String moduleUrl = "";
    public String moduleId = "";
    
    public int isMobile =2; //本权限是否可以手机显示， 1显示，其它不显示
    public String icon =""; //本权限显示的图标

    public boolean query = false;
    public boolean add = false;
    public boolean update = false;
    public boolean delete = false;
    public boolean print = false;
    public boolean sendEmail = false;
    public boolean oaWorkFlow=false;
    
    public ArrayList classScope = new ArrayList();
    public ArrayList<LoginScopeBean> queryScope = new ArrayList<LoginScopeBean>();
    public ArrayList addScope = new ArrayList();
    public ArrayList updateScope = new ArrayList();
    public ArrayList deleteScope = new ArrayList();
    public ArrayList printScope = new ArrayList();
    public ArrayList sendEmailScope = new ArrayList();


    private LoginScopeBean converBean(RoleScopeBean sourceBean){
        LoginScopeBean bean = new LoginScopeBean();
        bean.setEscopeValue(sourceBean.getEscopeValue());
        bean.setFieldName(sourceBean.getFieldName());
        bean.setFlag(sourceBean.getFlag());
        bean.setId(sourceBean.getId());
        bean.setRoleId(sourceBean.getRoleId());
        bean.setScopeValue(sourceBean.getScopeValue());
        bean.setTableName(sourceBean.getTableName());
        bean.setIsAddLevel(sourceBean.getIsAddLevel());
        return bean;
    }
    
    public void addQueryScope(List list){
    	for (Object o : list) {
    		LoginScopeBean temp = (LoginScopeBean) o;
            boolean same = false;
            for (Object o2 : queryScope) {
                LoginScopeBean temp2 = (LoginScopeBean) o2;
                if (temp2.getId()==temp.getId()) {
                    same = true;
                    break;
                }
            }
            if (!same) {
                queryScope.add(temp);
            }
        }
    }
    
    public void addClassScope(List list,ArrayList scope){
    	if(list == null || list.size() ==0 ) return;
    	for (Object o : list) {
            RoleScopeBean temp = (RoleScopeBean) o;
            boolean same = false;
	    	if(temp.getFlag().equals("0")){
	        	for (Object o2 : classScope) {
	                LoginScopeBean temp2 = (LoginScopeBean) o2;
	                if (temp2.getId()== temp.getId()) {
	                    same = true;
	                    break;
	                }
	            }
	            if (!same) {
	            	classScope.add(converBean(temp));
	            }
	        }else {
		    	for (Object o2 : scope) {
	                LoginScopeBean temp2 = (LoginScopeBean) o2;
	                if (temp2.getId()== temp.getId()) {
	                    same = true;
	                    break;
	                }
	            }
	            if (!same) {
	            	scope.add(converBean(temp));
	            }
	        }
    	}
    }
    
    public void setScope(int operation, List list) {
    	if(list == null || list.size() ==0 ) return;
        switch (operation) {
        case M_QUERY:
        	addClassScope(list,queryScope);
            break;
        case M_ADD:
        	addClassScope(list,addScope);
            break;
        case M_UPDATE:
        	addClassScope(list,updateScope);
            break;
        case M_DELETE:
        	addClassScope(list,deleteScope);
            break;
        case M_PRINT:
        	addClassScope(list,printScope);
            break;
        case M_SENDMAIL:
        	addClassScope(list,sendEmailScope);
            break;        
        default:
        	addClassScope(list,queryScope);
            break;
        }
    }


    public ArrayList<LoginScopeBean> getScope(int operation) {
        switch (operation) {
        case M_QUERY:
            return queryScope;
        case M_ADD:
            return addScope;
        case M_UPDATE:
            return updateScope;
        case M_DELETE:
            return deleteScope;
        case M_PRINT:
        	return printScope;
        case M_SENDMAIL:
        	return sendEmailScope;
        default:
            return queryScope;
        }
    }
    
    /**
     * 查询帐套时，设置，除查询之外清空所有权限
     *
     */
    public void setQueryOnly(){
    	add = false;
    	update = false;
    	delete = false;
    }

    public void setOperation(int operation) {
        switch (operation) {
        case M_QUERY:
            query = true;
            break;
        case M_ADD:
            add = true;
            break;
        case M_UPDATE:
            update = true;
            break;
        case M_DELETE:
            delete = true;
            break;
        case M_PRINT:
        	print = true;
            break;
        case M_SENDMAIL:
        	sendEmail = true;
        	break ;
        default:
            query = true;
            break;
        }
    }

    public boolean query() {
        return query;
    }

    public boolean add() {
        return add;
    }

    public boolean update() {
        return update;
    }

    public boolean delete() {
        return delete;
    }

    public boolean print() {
        return print;
    }

  
    public boolean sendEmail(){
    	return sendEmail;
    }


    public String getModuleId() {
        return moduleId;
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }


}
