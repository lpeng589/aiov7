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

		// תҳ��
		case OperationConst.frameLeft:
			forward = frameLeft(mapping, form, request, response);
			break;
		default:
			forward = settingMain(mapping, form, request, response); // �ҵĹ����ƻ���ҳ
		}
		return forward;
	}
	
	
	
	
	/**
	 * ���빤���ƻ�������������߲����˵�
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
		request.setAttribute("planSetting",planSetting == null ? false : planSetting.query()); //   �Ƿ��мƻ�ģ��Ĳ鿴Ȩ��
		
		MOperation  assSetting = (MOperation)getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblPlanAssociate");
		request.setAttribute("assSetting",assSetting == null ? false : assSetting.query());    //  �Ƿ��мƻ�������Ĳ鿴Ȩ��
		
		MOperation  paramSetting = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPlanAction.do?operation=4&opType=param");
		request.setAttribute("paramSetting",paramSetting == null ? false : paramSetting.query());  //  �Ƿ��м��Ӳ������õĲ鿴Ȩ��
		
		MOperation  seeSetting = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPopedomeActon.do");
		request.setAttribute("seeSetting",seeSetting == null ? false : seeSetting.query());    //  �Ƿ��м��Ӳ������õĲ鿴Ȩ��
		
		return getForward(request, mapping, "operationIndex");
	}
	
	/**
	 * �����ҵĹ����ƻ��͹����ܽ���ҳ
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
