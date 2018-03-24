package com.koron.crm.carefor;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.bean.CareforBean;
import com.koron.crm.bean.CareforDelBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
/**
 * 客户关怀路径Action
 * 包括关怀路径和关怀明细的操作
 * @author 徐磊
 *
 */
public class CareforAction extends MgtBaseAction {

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		
		switch (operation) {
		// 查询
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:// 添加
			 forward = add(mapping, form, request, response);
			break;
		// 新增前的准备
		case OperationConst.OP_ADD_PREPARE:
		    forward = addPrepare(mapping, form, request,response);
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			String type=request.getParameter("type");
			if("stopOrEnable".equals(type)){
				forward = stopOrEnable(mapping, form, request, response);
			}else if("stopPath".equals(type)){
				forward = stopPath(mapping, form, request, response);
			}else{
				forward = update(mapping, form, request,response);
			}
			
			break;
		case OperationConst.OP_UPDATE_PREPARE:// 修改前的准备
			 forward = updatePrepare(mapping, form, request,response);
			break;
		case OperationConst.OP_DELETE:// 删除
			forward = delete(mapping, form, request,response);
			break;
		default:
			forward = query(mapping, form, request, response);
			break;
		}

		return forward;
	}


	
	/**
	 * 停止执行所有此方案
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward stopPath(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = request.getParameterValues("keyId");
		
		CareforMgt mgt = new CareforMgt();
		
		Result r = mgt.stopAll(keyIds);
		if(r.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, "com.auto.succeed"))
                    .setBackUrl("/CareforQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else{
			EchoMessage.error().add(getMessage(
                    request, "com.auto.error"))
                    .setBackUrl("/CareforQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "alert");
		}
	}





	/**
	 * 更新
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		
		CareforBean bean = new CareforBean();
		read(form,bean);
		CareforMgt mgt = new CareforMgt();
		Result r = mgt.updateCarefor(bean);
		if(r.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			throw new BusinessException(getMessage(request, "crm.carfor.lb.path")+
					getMessage(request, "crm.sys.isExist"));
		}else if(r.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
            EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess"))
                    .setBackUrl("/CareforQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else{
			throw new BusinessException(getMessage(
                    request, "crm.update.error"));
		}
	}

	/**
	 * 更新前的准备
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	
		String keyId = request.getParameter("KeyId");
		CareforMgt mgt = new CareforMgt();
		Result rs = mgt.loadCarefor(keyId);
		CareforBean bean = (CareforBean)rs.getRetVal();
		request.setAttribute("detail", request.getParameter("detail")); //如果是详情，不显示添加保存按扭
		request.setAttribute("bean", bean);
		
		
		Result rdel = mgt.queryCareforDelForCareforId(keyId,null);
		if (rdel.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {// 成功
			request.setAttribute("careforDels", rdel.getRetVal());
		}
		
		
		
		return mapping.findForward("update");
	}

	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String[] keyIds = request.getParameterValues("keyId");
		CareforMgt mgt = new CareforMgt();
		Result result = mgt.isQuoteCarefor(keyIds) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS && result.realTotal>0){
			EchoMessage.error().add(getMessage(request, "crm.carefor.clientCareforPathCannotdelete")).
			setBackUrl("/CareforQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
            setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		result = mgt.deleteCarefor(keyIds);
		if(result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			return query(mapping, form, request, response);
		}else if(result.getRetCode() == ErrorCanst.DATA_ALREADY_USED){
			EchoMessage.error().add(getMessage(
                    request, "crm.carefor.msg.careforDelUsed"))
                    .setBackUrl("/CareforQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else{
			EchoMessage.error().add(getMessage(
                    request, "common.msg.delError"))
                    .setBackUrl("/CareforQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}		
	}

	/**
	 * 添加前的准备
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		return mapping.findForward("add");
	}

	/**
	 * 添加
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		CareforBean bean = new CareforBean();
		read(form, bean);
		CareforMgt mgt = new CareforMgt();
		Result r = mgt.addCarefor(bean);
		if(r.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			throw new BusinessException(getMessage(request, "crm.carfor.lb.path")+
					getMessage(request, "crm.sys.isExist"));
		}else if(r.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess"))
                    .setBackUrl("/CareforAction.do?operation="+OperationConst.OP_UPDATE_PREPARE+"&KeyId="+r.retVal+"&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else{
			throw new BusinessException(getMessage(
                    request, "common.msg.addFailture"));
		}
	}

	/**
	 * 启用或停用
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward stopOrEnable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = request.getParameterValues("keyId");
		String status = request.getParameter("statusTo");
		CareforMgt mgt = new CareforMgt();
		Result r = mgt.stopOrEnable(status, keyIds);
		if(r.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, "common.msg.updateSuccess"))
                    .setBackUrl("/CareforQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else{
			EchoMessage.error().add(getMessage(
                    request, "common.msg.updateFailture"))
                    .setBackUrl("/CareforQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}
	}

	/**
	 * 查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//status 状态 0启用 -1停用
		CareforSearchForm careforForm = (CareforSearchForm) form;
		
		CareforMgt mgt = new CareforMgt();
		
		Result rs = mgt.queryCarefor(careforForm.getName(),careforForm.getStatus());

		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {// 成功
			request.setAttribute("carefors", rs.getRetVal());
		}
		return mapping.findForward("list");
	}

}
