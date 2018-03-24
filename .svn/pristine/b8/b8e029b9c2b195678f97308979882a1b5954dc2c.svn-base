package com.koron.crm.memoryDay;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.clientLinkman.ClientLinkmanMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class ClientLinkmanMemoryDayAction  extends MgtBaseAction{

	ClientLinkmanMemoryDayMgt mgt = new ClientLinkmanMemoryDayMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {
		/* 查询*/
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
			break;
		}
		return forward;
	}
	
	/**
	 * 删除联系人的纪念日
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = request.getParameterValues("keyId");		/*纪念Id*/
		Result r = mgt.delete(keyIds);
		if(r.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
                    		   .setBackUrl("/ClientLinkmanMemoryDayAction.do?operation=4&type=main")
                    		   .setNotAutoBack().setAlertRequest(request);
            return getForward(request, mapping, "message");
		}
		return queryList(mapping, form, request, response) ;
	}

	private ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		if ("left".equals(type)) {
			return queryLeft(mapping, form, request, response);
		} else if("delete".equals(type)){
			return delete(mapping, form, request, response) ;
		}else if("main".equals(type)){
			return queryList(mapping, form, request, response);
		}
		return mapping.findForward("LinkmanMemoryDayIndex");
	}
	
	/**
	 * 查询列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String firstName = getParameter("firstName", request);
		if("GET".equals(request.getMethod()) && firstName!=null){
	   	 	firstName = GlobalsTool.toChinseChar(firstName) ;
	    }
	    String ClientName = getParameter("ClientName", request) ; 
	    String ClientNo = getParameter("ClientNo", request) ; 
	    String telephone = getParameter("telephone", request) ;
	    String userName = getParameter("userName", request) ;
	    if(userName != null && !"".equals(userName)){
	    	firstName = null;
	    }
	    request.setAttribute("firstName", firstName);
	    request.setAttribute("ClientName", ClientName);
	    request.setAttribute("ClientNo", ClientNo);
	    request.setAttribute("telephone", telephone);
	    request.setAttribute("userName", userName);
	    
	    int pageSize = getParameterInt("pageSize", request);
        int pageNo = getParameterInt("pageNo", request);
        String noPageSize = (String) request.getAttribute("NoPageSize") ;
        if (pageNo == 0 || "OK".equals(noPageSize)) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = GlobalsTool.getPageSize();
        }
        MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/ClientLinkmanMemoryDayAction.do");
	    Result result = mgt.queryLinkManMemoryDay(getLocale(request).toString(), firstName, 
	    							ClientName, userName,getLoginBean(request).getId(),pageSize,pageNo,mop);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("pageBar", pageBar(result, request)) ;
			request.setAttribute("list", result.retVal) ;
			request.setAttribute("MOID", mop.getModuleId()) ;
		}
		return mapping.findForward("LinkmanMemoryDayMain");
	}
	
	/**
	 * 百家姓
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryLeft(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		ClientLinkmanMgt mgt = new ClientLinkmanMgt();
		if("tidy".equals(request.getParameter("tidy"))){
			mgt.queryFamilyName();
		}
		Result rst = mgt.queryBJX();
		request.setAttribute("valueMap", rst.getRetVal());
		return mapping.findForward("LinkmanMemoryDayLeft");	
	}

}
