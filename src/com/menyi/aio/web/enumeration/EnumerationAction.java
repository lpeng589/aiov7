package com.menyi.aio.web.enumeration;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.web.util.*;

import org.apache.struts.action.*;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>Title: 单位管理控制类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class EnumerationAction extends MgtBaseAction {

    EnumerationMgt mgt = new EnumerationMgt();

    public EnumerationAction() {
    }

    /**
     * exe 控制器入口函数
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        //跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
            forward = addPrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_ADD:
            forward = add(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE_PREPARE:
            forward = updatePrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE:
            forward = update(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_QUERY:
            forward = query(mapping, form, request, response);
            break;
        case OperationConst.OP_DETAIL:
            forward = detail(mapping, form, request, response);
            break;
        default:
            forward = query(mapping, form, request, response);
        }
        return forward;
    }

    /**
     * 添加前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
        request.removeAttribute("operation");
        return getForward(request, mapping, "add");
    }

    /**
     * 添加
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward add(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        EnumerationForm myForm = (EnumerationForm) form;
        //执行添加操作
        EnumerateBean bean = new EnumerateBean();
        bean.setId(IDGenerater.getId());
        bean.setEnumName(myForm.getEnumName());

        bean.setCreateBy(getLoginBean(request).getId());
        bean.setLastUpdateBy(getLoginBean(request).getId());
        bean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
        bean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
        String display = myForm.getMaindisplay(); //zh_CN:1;en:2;zh_TW:3;
        String locName = request.getParameter("maindisplay_lan");
        locName = request.getLocale().toString()+":"+locName+";";
        if(display==null|| display.length() ==0){
        	display = locName;
        }else if(display.indexOf(request.getLocale().toString()+":") > -1){
        	display  = display.replaceAll(request.getLocale().toString()+":[^;]*;", locName);
        }else{
        	display  = locName + display;
        }
        bean.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
                                                 (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),display));
        ArrayList eItems = new ArrayList();
        bean.setEnumItem(eItems);
        
        String[] clocNames = request.getParameterValues("displays_lan");
        for (int i = 0; i < myForm.getValues().length; i++) {
            if (myForm.getValues()[i] != null &&
                myForm.getValues()[i].trim().length() > 0) {
                EnumerateItemBean eib = new EnumerateItemBean();
                eib.setId(IDGenerater.getId());
                display = myForm.getDisplays()[i];
                
                locName = request.getLocale().toString()+":"+clocNames[i]+";";
                if(display==null|| display.length() ==0){
                	display = locName;
                }else if(display.indexOf(request.getLocale().toString()+":") > -1){
                	display  = display.replaceAll(request.getLocale().toString()+":[^;]*;", locName);
                }else{
                	display  = locName + display;
                }
                
                eib.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
                                                 (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),display));
                eib.setEnumerateBean(bean);
                eib.setEnumValue(myForm.getValues()[i]);
                eib.setEnumOrder(myForm.getOrderBy()[i]) ;
                eItems.add(eib);
            }
        }
        Result rs = mgt.add(bean);

        ActionForward forward = getForward(request, mapping, "alert");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	String crmPopFlag = getParameter("crmPopFlag",request);//参数crmPopFlag==true,表示从CRM模板设置修改选项数据
        	if(crmPopFlag!=null && "true".equals(crmPopFlag)){
        		request.setAttribute("dealAsyn", "true");//跳转到dealAsyn()自己处理成功结果
        	}
            //添加成功后保存脚本
            String path=request.getSession().getServletContext().getRealPath("Enumration.sql").toString();
            ConvertToSql.saveEnum(path, bean.getId(),bean.getEnumName(),1);
            TestRW.saveToSql(path,ConvertToSql.getLanguageStr(bean.getDisplay()));
            List items=bean.getEnumItem();
            for(int i=0;i<items.size();i++){
            	EnumerateItemBean eib=(EnumerateItemBean)items.get(i);
            	String sql=ConvertToSql.getLanguageStr(eib.getDisplay());
            	TestRW.saveToSql(path, sql);
            }
            //更新内存数据
            InitMenData imd = new InitMenData();
            imd.initEnumerationInformation();
            //添加成功
            EchoMessage.success().add(getMessage(
                    request, "common.msg.addSuccess"))
                    .setBackUrl("/EnumerationQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		bean.getDisplay().get(this.getLocale(request).toString()), "tblDBEnumeration", "枚举管理","");
        } else {
            //添加失败
            EchoMessage.error().add(getMessage(
                    request, "common.msg.addFailture")).
                    setAlertRequest(request);
        }
        return forward;
    }

    /**
     * 修改前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward updatePrepare(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws
            Exception {
        ActionForward forward = null;
        String nstr = request.getParameter("keyId");
        if(nstr == null || nstr.length() ==0){
        	String enumerName = request.getParameter("enumerName");
        	EnumerateBean eb  = (EnumerateBean)BaseEnv.enumerationMap.get(enumerName);
        	nstr = eb.getId();
        }
        if (nstr != null && nstr.length() != 0) {
            //执行查询前的加载
            Result rs = mgt.detail(nstr);
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //加载成功
                request.setAttribute("result", rs.retVal);
                forward = getForward(request, mapping, "update");
            } else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
                //记录不存在错误
                EchoMessage.error().add(getMessage(
                        request, "common.error.nodata")).setRequest(request);
            } else {
                //加载失败
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                        setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;
    }

    /**
     * 修改
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward update(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {

        EnumerationForm myForm = (EnumerationForm) form;
        //执行添加操作
        EnumerateBean bean = new EnumerateBean();
        bean.setId(myForm.getId());
        Result rs = mgt.detail(myForm.getId());
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            //加载失败
            EchoMessage.error().add(getMessage(
                    request, "common.msg.updateErro")).setAlertRequest(
                            request);
            return getForward(request, mapping, "alert");
        }
        EnumerateBean oldbean = (EnumerateBean)rs.retVal;
        bean.setCreateBy(oldbean.getCreateBy());
        bean.setCreateTime(oldbean.getCreateTime());
        bean.setLastUpdateBy(getLoginBean(request).getId());
        bean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));


        bean.setEnumName(myForm.getEnumName());
        String display = myForm.getMaindisplay();
        String locName = request.getParameter("maindisplay_lan");
        locName = request.getLocale().toString()+":"+locName+";";
        if(display==null|| display.length() ==0){
        	display = locName;
        }else if(display.indexOf(request.getLocale().toString()+":") > -1){
        	display  = display.replaceAll(request.getLocale().toString()+":[^;]*;", locName);
        }else{
        	display  = locName + display;
        }
        
        bean.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
                                         (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),display));


        ArrayList eItems = new ArrayList();
        bean.setEnumItem(eItems);
        String[] clocNames = request.getParameterValues("displays_lan");
        for (int i = 0; i < myForm.getValues().length; i++) {
            if (myForm.getValues()[i] != null &&
                myForm.getValues()[i].trim().length() > 0) {
                EnumerateItemBean eib = new EnumerateItemBean();
                eib.setId(IDGenerater.getId());
                display = myForm.getDisplays()[i];
                
                locName = request.getLocale().toString()+":"+clocNames[i]+";";
                if(display==null|| display.length() ==0){
                	display = locName;
                }else if(display.indexOf(request.getLocale().toString()+":") > -1){
                	display  = display.replaceAll(request.getLocale().toString()+":[^;]*;", locName);
                }else{
                	display  = locName + display;
                }
                eib.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
                                                (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),display));
                eib.setEnumerateBean(bean);
                eib.setEnumValue(myForm.getValues()[i]);
                eib.setEnumOrder(myForm.getOrderBy()[i]);
                eItems.add(eib);
            }
        }

        ActionForward forward = null;
        //执行修改
        rs = mgt.update(bean);

        forward = getForward(request, mapping, "alert");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	
        	String crmPopFlag = getParameter("crmPopFlag",request);//参数crmPopFlag==true,表示从CRM模板设置修改选项数据
        	if(crmPopFlag!=null && "true".equals(crmPopFlag)){
        		request.setAttribute("dealAsyn", "true");//跳转到dealAsyn()自己处理成功结果
        	}
        	if("FieldSysType".equals(oldbean.getEnumName())){ //如果枚举名为FieldSysType时，保证字段系统类型枚举值和系统配置，系统参数设置中保持一致
        		List<EnumerateItemBean> newItemList = bean.getEnumItem() ; //旧的字段系统类型枚举值
        		List<EnumerateItemBean> oldItemList = oldbean.getEnumItem() ; //新的字段系统类型枚举值
        		for(EnumerateItemBean newItemBean : newItemList){
        			boolean flag = true ;
        			for(EnumerateItemBean oldItemBean : oldItemList){
        				if(oldItemBean.getEnumValue().equals(newItemBean.getEnumValue())){
        					flag = false ;
        					break ;
        				}
        			}
        			if(flag){ //如果的旧的字段类型枚举值中不包含这个，则往系统参数设置中添加一个
        				mgt.addFieldSysTypeBySysEnum(newItemBean.getEnumValue(),newItemBean.getLanguageId() ) ;
        			}
        		}
        	}
        	//修改成功后保存脚本
            String path=request.getSession().getServletContext().getRealPath("Enumration.sql").toString();
            ConvertToSql.saveEnum(path, bean.getId(),bean.getEnumName(),2);
            TestRW.saveToSql(path,ConvertToSql.getLanguageStr(bean.getDisplay()));
            List items=bean.getEnumItem();
            for(int i=0;i<items.size();i++){
            	EnumerateItemBean eib=(EnumerateItemBean)items.get(i);
            	String sql=ConvertToSql.getLanguageStr(eib.getDisplay());
            	TestRW.saveToSql(path, sql);
            }
            //更新内存数据
            InitMenData imd = new InitMenData();
            //更新内存中枚举值
            imd.initEnumerationInformation();
            //修改成功
            EchoMessage.success().add(getMessage(
                    request, "common.msg.updateSuccess")).setBackUrl("/EnumerationQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		bean.getDisplay().get(this.getLocale(request).toString()), "tblDBEnumeration", "枚举管理","");
        } else {
            //修改失败
            EchoMessage.error().add(getMessage(
                    request, "common.msg.updateErro")).setAlertRequest(
                            request);
        }
        return forward;
    }

    /**
     * 删除
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
        ActionForward forward = null;
        String nstr[] = request.getParameterValues("keyId");
        String winCurIndex = getParameter("winCurIndex", request) ;
        
        if (nstr != null && nstr.length != 0) {
        	ArrayList<EnumerateBean> delList = new ArrayList<EnumerateBean>();
        	for(String id:nstr){
	        	Result rs = mgt.detail(id);
	            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	                delList.add((EnumerateBean)rs.retVal);
	            }
        	}
        	
            Result rs = mgt.delete(nstr);

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //删除成功后保存脚本
                String path=request.getSession().getServletContext().getRealPath("Enumration.sql").toString();
                ConvertToSql.saveDelEnum(path, nstr);
                //更新内存数据
                InitMenData imd = new InitMenData();
                imd.initEnumerationInformation();
                //删除成功
                request.setAttribute("result", rs.retVal);
                EchoMessage.success().add(getMessage(request,"common.msg.delSuccess"))
				   .setBackUrl("/EnumerationQueryAction.do?winCurIndex="+winCurIndex) 
				   .setRequest(request);
                forward = getForward(request, mapping, "message");
                for(EnumerateBean bean: delList){
                	new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
                		bean.getDisplay().get(this.getLocale(request).toString()), "tblDBEnumeration", "枚举管理","");
                }
            } else {
                //删除失败
                EchoMessage.error().add(getMessage(request,"common.msg.delError"))
                				   .setBackUrl("/EnumerationQueryAction.do?winCurIndex="+winCurIndex) 
                				   .setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;

    }

    /**
     * 查询
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
            Exception {
        EnumerationSearchForm searchForm = (EnumerationSearchForm) form;
        if (searchForm != null) {
            //执行查询
            String name = request.getParameter("name");
            name = name==null?"":name.trim();
            
            String mainModule=request.getParameter("mainModule");
            if(mainModule==null||mainModule.length()==0){
            	mainModule=this.getLoginBean(request).getDefSys();
            }
            if(mainModule==null||mainModule.equals("")){
            	mainModule="-1";
            }
            request.setAttribute("search", name) ;
            request.setAttribute("mainModule", mainModule);
            //name=name==null?null:new String(name.getBytes("iso-8859-1"), "UTF-8");
            Result rs = mgt.query(name,searchForm.getPageNo(),searchForm.getPageSize(),mainModule);
            ArrayList list =  new ArrayList();
            if(!(((ArrayList)(rs.retVal)).size()>0)){
            	rs=mgt.queryByDisplay(name,searchForm.getPageNo(),searchForm.getPageSize(),getLocale(request).toString(),mainModule);
            }
            for (int i = 0; i < ((ArrayList)rs.getRetVal()).size(); i++) {
                EnumerateBean eb = (EnumerateBean)((ArrayList)rs.getRetVal()).get(i);
                String str = "";
                for (int j = 0; j < eb.getEnumItem().size(); j++) {
                    EnumerateItemBean eib = (EnumerateItemBean) eb.getEnumItem().
                                            get(j);

                    String lo = eib.getDisplay() == null ? "" : eib.getDisplay().get(getLocale(request).toString());
                    lo = lo == null ? "" : lo;

                    str+=lo+":"+eib.getEnumValue()+";";
                }
                if(str.length()>0){
                    str = str.substring(0,str.length()-1);
                }
                String lo = eb.getDisplay() == null ? "" : eb.getDisplay().get(getLocale(request).toString());
                lo = lo == null ?"":lo;
                list.add(new String[]{eb.getId().toString(),eb.getEnumName(),lo,str});
            }

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //查询成功
            	if("crm".equals(request.getParameter("type"))){ //这是crm界面查询枚举
            		request.setAttribute("result", list);
                    request.setAttribute("pageBar", pageBar(rs, request));
                    return getForward(request, mapping, "search");
            	}
                request.setAttribute("result", list);
                request.setAttribute("pageBar", pageBar(rs, request));
            } else {
                //查询失败
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                        setRequest(request);
                return getForward(request, mapping, "message");
            }
        }
        return getForward(request, mapping, "list");
    }

    /**
     * 明细
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward detail(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {

        String nstr = request.getParameter("keyId");
        if (nstr != null && nstr.length() != 0) {
            //执行加载明细
            Result rs = mgt.detail(nstr);
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //加载成功
                request.setAttribute("result", rs.retVal);
            } else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
                //记录不存在错误
                EchoMessage.error().add(getMessage(
                        request, "common.error.nodata")).setRequest(request);
                return getForward(request, mapping, "message");
            } else {
                //加载失败
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                        setRequest(request);
                return getForward(request, mapping, "message");
            }
        }
        return getForward(request, mapping, "detail");
    }
}
