package com.koron.oa.individual.planWorkSetting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class OAPlanWorkSettingAction extends MgtBaseAction{

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int operation = getOperation(request);
		System.out.println(request.getParameter("winCurIndex"));
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		ActionForward forward = null;
		switch (operation) {

		// 转页面
		case OperationConst.frameLeft:
			forward = frameLeft(mapping, form, request, response);
			break;
		default:
			forward = settingMain(mapping, form, request, response); // 我的工作计划首页
		}
		return forward;
	}
	
	
	
	
	/**
	 * 进入工作计划管理设置中左边操作菜单
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward frameLeft(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		  
		MOperation  planSetting = (MOperation)getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblPlanTemplate");
		request.setAttribute("planSetting",planSetting == null ? false : planSetting.query()); //   是否有计划模板的查看权限
		
		MOperation  assSetting = (MOperation)getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblPlanAssociate");
		request.setAttribute("assSetting",assSetting == null ? false : assSetting.query());    //  是否有计划关联项的查看权限
		
		MOperation  paramSetting = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPlanAction.do?operation=4&opType=param");
		request.setAttribute("paramSetting",paramSetting == null ? false : paramSetting.query());  //  是否有检视参数设置的查看权限
		
		MOperation  seeSetting = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPopedomeActon.do");
		request.setAttribute("seeSetting",seeSetting == null ? false : seeSetting.query());    //  是否有检视参数设置的查看权限
		
		return getForward(request, mapping, "operationIndex");
	}
	
	/**
	 * 进入我的工作计划和工作总结主页
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward settingMain(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return getForward(request, mapping, "setIndex");
	}
	
	private ActionForward menu1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return getForward(request, mapping, "menu1");
	}
	
	private ActionForward menu2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return getForward(request, mapping, "menu2");
	}

}
