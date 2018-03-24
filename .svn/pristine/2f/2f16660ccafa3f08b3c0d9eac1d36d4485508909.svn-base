package com.koron.crm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.Employee;
import com.koron.oa.util.EmployeeMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.advice.AdviceForm;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
/**
 * 	座席台
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Dec 21, 2010
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class ClientServiceAction extends MgtBaseAction{

	ClientServiceMgt mgt = new ClientServiceMgt() ;
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null ;
		
		
        String type = request.getParameter("type") ;
    	if("right".equals(type)){
    		forward = goRight(mapping, form, request, response) ;
    	}else if("left".equals(type)){
    		forward = goLeft(mapping, form, request, response) ;
    	}else if("bottom".equals(type)){
    		forward = goBottom(mapping, form, request, response) ;
    	}else if("query".equals(type)){
    		forward = queryClient(mapping, form, request, response) ;
    	}else if("task".equals(type)){
    		forward = assignTask(mapping, form, request, response) ;
    	}else if("empty".equals(type)){
    		return getForward(request, mapping, "empty") ;
    	}  else{
    		String keyWord= request.getParameter("keyWord");
    		if(keyWord == null || keyWord.length() == 0){
    			request.getSession().removeAttribute("ClientServiceKeyWord");
    		}else{
    			request.getSession().setAttribute("ClientServiceKeyWord",keyWord);
    		}
    		forward = goIndex(mapping, form, request, response) ;
    	}
    	return forward ;
	}
	


	/**
	 * 座席台 首页
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward goIndex(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		//查询呼叫中心设置
		
		HashMap map = new HashMap();
		
		request.setAttribute("callCenter", map);
		return mapping.findForward("index");
	}
	
	/**
	 * 座席台 分配任务
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward assignTask(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String clientId    = getParameter("clientId", request) ;    /*客户Id*/ 
		String taskType    = getParameter("taskType", request) ;	/*任务类型*/
		String taskPerson  = getParameter("taskPerson", request) ;	/*分配任务人*/
		String taskContent = getParameter("content", request) ;	    /*内容*/
		String[] crmAffix  = getParameters("crmAffix", request) ;	/*附件*/
		String task = getMessage(request, "crm.service.task") ;		/*任务*/
		
		String affix = "";
		for (int k = 0;crmAffix != null && k < crmAffix.length;k++) {
            affix += crmAffix[k] + ";";
            //拷贝到正式目录
            FileHandler.copy("CRMTaskAssign", FileHandler.TYPE_AFFIX,crmAffix[k],crmAffix[k]);
        }
        if (affix.endsWith(";")) {
            affix = affix.substring(0, affix.length() - 1);
        }
        String depes= "";
        EmployeeMgt employee_mgt = new EmployeeMgt();
		Result rs = employee_mgt.queryEmployeeByUserId(taskPerson);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
				&& rs.getRetVal() != null) {
			Employee bean = (Employee) rs.getRetVal();
			if (bean != null) {
				depes=bean.getDepartmentCode();
			}
		}
		/*添加任务*/
		String taskId = IDGenerater.getId() ;
		Result result = mgt.addTask(taskId, clientId, taskType, taskContent, taskPerson, depes,
							getLoginBean(request).getId(),getLoginBean(request).getDepartCode(),
							getLoginBean(request).getSunCmpClassCode(),affix,task) ;
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			 ArrayList listAffix = (ArrayList) request.getSession().getAttribute( "AFFIX_UPLOAD");
			 new UserFunctionMgt().deleteTempImage(crmAffix, listAffix) ;
			if(taskPerson!=null && taskPerson.length()>0){
	            new AdviceMgt().add(
	            		getLoginBean(request).getId(), 
	            		getMessage(request, "crm.service.task")+"："+taskContent, 
	            		"<a href=\"javascript:mdiwin('/UserFunctionQueryAction.do?tableName=CRMTaskAssign','"+getMessage(request, "crm.service.task")+"')\">"+getMessage(request, "crm.service.task")+"："+taskContent+"</a>", 
	            		taskPerson, 
	            		taskId, 
	            		"");
			}
			EchoMessage.success().add(getMessage(request, "crm.tast.ok")).setBackUrl(
					"/ClientServiceAction.do?type=left").setAlertRequest(request);
		}
		return mapping.findForward("message");
	}
	
	/**
	 * 座席台 右边
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward goRight(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String question = getParameter("question", request) ;/*问题或答案*/
		request.setAttribute("question", question) ;
		
		Result result = mgt.queryQA(question) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("listQA", result.getRetVal()) ;
		}
		return mapping.findForward("right");
	}
	
	/**
	 * 座席台 右边
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryClient(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		/*关键字*/
		String keyWord = getParameter("keyWord", request) ;
		keyWord = GlobalsTool.toChinseChar_GBK(keyWord) ;
		int pageSize = getParameterInt("pageSize", request);
        int pageNo = getParameterInt("pageNo", request);
        String noPageSize = (String) request.getAttribute("NoPageSize") ;
        if (pageNo == 0 || "OK".equals(noPageSize)) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = GlobalsTool.getPageSize();
        }
        long start = System.currentTimeMillis() ;
		Result result = mgt.queryClientByKeyWord(keyWord,pageSize,pageNo) ;
		long end = System.currentTimeMillis() ;
		System.out.println(end-start);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("listClient", result.retVal) ;
			request.setAttribute("pageBar", pageBar(result, request));
		}
		return mapping.findForward("clientList");
	}
	
	/**
	 * 座席台 左边
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward goLeft(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		return mapping.findForward("left");
	}
	
	/**
	 * 座席台 底部
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward goBottom(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		return mapping.findForward("bottom");
	}
}
