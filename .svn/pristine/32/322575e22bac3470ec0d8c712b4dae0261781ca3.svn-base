package com.koron.oa.workflow.consignation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.OAWorkConsignBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:工作流委托</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 2, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class OAConsignationAction extends BaseAction {
    
	OAConsignationMgt consignMgt = new OAConsignationMgt();
    UserMgt userMgt = new UserMgt();
    private static Gson gson ;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	int operation = getOperation(request);
        ActionForward forward = null;
        String type = request.getParameter("type");
        switch (operation) {
        //增加前执行方法
        case OperationConst.OP_ADD_PREPARE:
            forward = addPrepare(mapping, form, request,response);
            break;
            //添加委托
        case OperationConst.OP_ADD:
            forward = addWorkCosign(mapping, form, request, response);
            break;
            //修改委托之前
        case OperationConst.OP_UPDATE_PREPARE:
            forward = updatePrepare(mapping, form, request,response);
            break;
            //修改委托
        case OperationConst.OP_UPDATE:
        	forward = updateWorkCosign(mapping, form, request,response);
            break;
            //查询委托
        case OperationConst.OP_QUERY:
            if("list".equals(type)){
                forward = queryList(mapping, form, request, response);
            }else if("selectFlowType".equals(type)){
            	forward = selectFlowType(mapping, form, request, response);
            }else if("cancel".equals(type)){
        		forward = cancelWorkCosign(mapping, form, request,response);
        	}else{
                forward = queryIndex(mapping, form, request, response);
            }
            break;
        default:
             forward = queryIndex(mapping, form, request, response);
        }
        return forward;
    }

    /**
     * 工作流委托主界面
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward queryIndex(ActionMapping mapping,ActionForm form,
               HttpServletRequest request, HttpServletResponse response) {
        return getForward(request, mapping, "workConsignIndex");
    }
    
    /**
     * 查询工作流类型
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward selectFlowType(ActionMapping mapping,ActionForm form,
               HttpServletRequest request, HttpServletResponse response) {
    	
    	//查询工作流类别
    	Result result = consignMgt.queryFlowClass();
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("flowClass", result.retVal);
    	}
    	//查询工作流类型
    	result = consignMgt.queryFlowType(getLoginBean(request));
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("flowJson", gson.toJson(result.retVal));
    		request.setAttribute("flowType", result.retVal);
    	}
    	request.setAttribute("flowIds", getParameter("flowIds", request));
        return getForward(request, mapping, "flowType");
    }
    
    /**
     * 工作流委托 查询
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward queryList(ActionMapping mapping,ActionForm form,
               HttpServletRequest request, HttpServletResponse response) {
        
    	OAConsignSearchForm consignForm = (OAConsignSearchForm) form;
    	Result result = consignMgt.queryConsign(consignForm,getLoginBean(request).getId());
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("consignList", result.retVal);
    		request.setAttribute("pageBar", pageBar(result, request));
    	}
    	return getForward(request, mapping, "workConsignList");
    }

    /**
     * 修改委托
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward updatePrepare(ActionMapping mapping,ActionForm form,
                HttpServletRequest request, HttpServletResponse response) {
        
		String keyId = request.getParameter("keyId");
		OAWorkConsignBean workConsign = consignMgt.loadConsign(keyId);
		request.setAttribute("consign", workConsign);
		return getForward(request, mapping, "addWorkConsign");
    }
    
    
    /**
     * 注销委托
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward cancelWorkCosign(ActionMapping mapping,ActionForm form,
                HttpServletRequest request, HttpServletResponse response) {
        
		String[] keyIds = getParameters("keyId", request);
		
		Result result = consignMgt.cancelConsign(keyIds);
		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add("撤消委托失败").setBackUrl("/WorkConsignAction.do?operation=4&type=list").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}else{
			EchoMessage.success().add("撤消委托成功").setBackUrl("/WorkConsignAction.do?operation=4&type=list").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
    }
    
    
    /**
     * 修改委托
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward updateWorkCosign(ActionMapping mapping,ActionForm form,
                HttpServletRequest request, HttpServletResponse response) {
        
		String keyId = request.getParameter("keyId");
		OAWorkConsignBean workConsign = consignMgt.loadConsign(keyId);
		
		String congignUserID = getParameter("consignUserID", request);
        String beginTime = getParameter("beginTime", request) ;
        String endTime = getParameter("endTime", request) ;
        String flowName = getParameter("flowName", request);
        String flowNameDis = getParameter("flowNameDis", request);
        workConsign.setCongignuserid(congignUserID);
        workConsign.setBeginTime(beginTime);
        workConsign.setEndTime(endTime);
        workConsign.setFlowName(flowName);
        workConsign.setFlowNameDis(flowNameDis);
        
        request.setAttribute("dealAsyn", "true");
		if (workConsign != null) {
			Result rs = consignMgt.updateConsign(workConsign);
			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
		}
		EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		return getForward(request, mapping, "alert");
    }


    /**
     * 添加委托
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward addWorkCosign(ActionMapping mapping, ActionForm form,
                 HttpServletRequest request, HttpServletResponse response) {
        
    	LoginBean loginBean = this.getLoginBean(request);
        
    	String congignUserID = getParameter("consignUserID", request);
        String beginTime = getParameter("beginTime", request) ;
        String endTime = getParameter("endTime", request) ;
        String flowName = getParameter("flowName", request);
        String flowNameDis = getParameter("flowNameDis", request);
        String keyId = IDGenerater.getId(); //自动生成一个ID
        String creatTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
        
        OAWorkConsignBean workConsign = new OAWorkConsignBean();
        workConsign.setId(keyId);
        workConsign.setUserid(loginBean.getId());
        workConsign.setCongignuserid(congignUserID);
        workConsign.setBeginTime(beginTime) ;
        workConsign.setEndTime(endTime) ;
        workConsign.setCreateby(loginBean.getId());
        workConsign.setCreatetime(creatTime);
        workConsign.setState(1);
        workConsign.setFlowName(flowName);
        workConsign.setFlowNameDis(flowNameDis);
        
        Result rs = consignMgt.addConsign(workConsign);
        if(rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
        	EchoMessage.error().add("同一个流程不能委托给多个人！")
    			.setAlertRequest(request);
        	return getForward(request, mapping, "alert");
        }else if(rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
        	EchoMessage.error().add(getMessage(request,"oa.common.addError"))
        		.setAlertRequest(request);
            return getForward(request, mapping, "alert");
        }
        request.setAttribute("dealAsyn", "true");
        EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
        return getForward(request, mapping, "alert");
    }

    /**
     * 添加委托
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward addPrepare(ActionMapping mapping,ActionForm form,
               HttpServletRequest request,HttpServletResponse response) {
        
    	return getForward(request, mapping, "addWorkConsign");
    }

	@Override
	protected ActionForward doAuth(HttpServletRequest request, ActionMapping mapping) {
		
		LoginBean loginBean = getLoginBean(request);
		if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(request, mapping, "indexPage");
        }
		return null;
	}
    
}
