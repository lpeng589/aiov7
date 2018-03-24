package com.menyi.email;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class EMailSettingAction extends MgtBaseAction {
	EMailSettingMgt mgt = new EMailSettingMgt();	
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        String noback=request.getParameter("noback");//不能返回
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		int operation = getOperation(request);
		String type = request.getParameter("type");
		
		String userId = this.getLoginBean(request).getId();
		request.setAttribute("LoginId", userId);

		ActionForward forward = null;
		
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	
	private ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();
		return getForward(request, mapping, "mailBlack");
	}
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("email");
		String userId = this.getLoginBean(request).getId();
		//新增
		Result rs  = mgt.addBlack(email,userId);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			// 添加成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess"))
					.setBackUrl(
							"/EMailBlackQueryAction.do?reload=true")
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		} else if (rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			EchoMessage.error().add(
					getMessage(request, "oa.mail.msg.addressDouble"))
					.setBackUrl("/EMailBlackQueryAction.do")
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		} else {
			throw new BusinessException("common.msg.addFailture",
					"/EMailBlackQueryAction.do");
		}
	}
	
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();
		Result rs = mgt.listParam();
		HashMap map = new HashMap();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null)
		for(Object o:(ArrayList)rs.retVal){
			Object os[] = (Object[])o;
			map.put(os[0], os[1]);
		}
		request.setAttribute("result", map);
		return getForward(request, mapping, "mailSetting");
	}
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String filterDate = request.getParameter("filterDate");
		String filterDomain = request.getParameter("filterDomain");
		String mailLabel = request.getParameter("mailLabel");
		Result rs = new Result();
		if(filterDate !=null && filterDate.trim().length() >0){
			rs = mgt.updateParam("filterDate", filterDate);
		}
	//	if(filterDomain !=null && filterDomain.trim().length() >0){
			rs = mgt.updateParam("filterDomain", filterDomain);
		//}
		if(mailLabel !=null && mailLabel.trim().length() >0){
			rs = mgt.updateParam("mailLabel", mailLabel);
		}
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			// 添加成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl(
							"/EMailSettingAction.do?operation=7")
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		} else {
			throw new BusinessException("common.msg.updateFailture",
					"/EMailSettingAction.do?operation=7");
		}
	}
	
	
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		EMaiBlackSearchForm myForm = (EMaiBlackSearchForm)form;
		String userId = ((LoginBean) request.getSession().getAttribute("LoginBean"))
		.getId();
		// 查询个人邮箱
		Result rs = mgt.listBlack(myForm.getEmail(),myForm.getEmpName(),myForm.getPageSize(),myForm.getPageNo());
		request.setAttribute("list", rs.getRetVal());
		request.setAttribute("pageBar", pageBar(rs, request));
		return getForward(request, mapping, "mailBlackList");
	}
	
	private ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = ((LoginBean) request.getSession().getAttribute("LoginBean"))
		.getId();
		// 删除邮箱
		String email = request.getParameter("keyId");
		Result rs = mgt.deleteBlack(email);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS
				|| rs.retCode == ErrorCanst.ER_NO_DATA) {
			return query(mapping,form,request,response);
		}else {
			throw new BusinessException("common.msg.delError",
					"/EMailBlackQueryAction.do");
		}
	}
	
}
