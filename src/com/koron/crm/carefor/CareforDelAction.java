package com.koron.crm.carefor;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.bean.CareforDelBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class CareforDelAction extends MgtBaseAction {

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// ���ܷ���
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		//��������	dateType  ������(0)����������(1)���������(2)��ѭ��(3)
		//��������	actionType	�����¼� (0)��������(1)�����ʼ�(2)
		//�����Լ�	ccSelf	  ��(1)����(0)
		switch (operation) {
		case OperationConst.OP_ADD:// ���
			forward = add(mapping, form, request, response);
			break;
		// ����ǰ��׼��
		case OperationConst.OP_ADD_PREPARE:
		    forward = addPrepare(mapping, form, request,response);
			break;
		// �޸�
		case OperationConst.OP_UPDATE:
			String type = request.getParameter("type");
			if("setMail".equals(type)){
				forward = emailSetting(mapping, form, request,response);
			}else{
				forward = update(mapping, form, request,response);
			}
			
			break;
		case OperationConst.OP_UPDATE_PREPARE:// �޸�ǰ��׼��
			 forward = updatePrepare(mapping, form, request,response);
			break;
		case OperationConst.OP_DELETE:// ɾ��
			forward = delete(mapping, form, request,response);
			break;
		}

		return forward;
	}
	private ActionForward emailSetting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		EMailMgt mgt = new EMailMgt();
		if("query".equals(action)){
			Result rst = mgt.getALLMailinfoSetting();
			ArrayList<String[]> list = (ArrayList<String[]>) rst.getRetVal();
			request.setAttribute("list",list);
			return mapping.findForward("email");
		}else{
			String id = request.getParameter("id");
			Result rst = mgt.setCorporationEmail(id);
			if (rst.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				EchoMessage.error().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.updateFailture")).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
		}
		
	}


	/**
	 * ɾ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String keyId = request.getParameter("KeyId");
		CareforMgt mgt = new CareforMgt();
		Result result = mgt.isQuoteCareforDel(id) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS && result.realTotal>0){
			EchoMessage.error().add(getMessage(request, "crm.carefor.affairCannotDelete")).
			setBackUrl("/CareforAction.do?operation="+OperationConst.OP_UPDATE_PREPARE+"&KeyId="+keyId
					+"&winCurIndex="+request.getParameter("winCurIndex")).
            setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		result = mgt.deleteCareforDel(id);
		if(result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, "common.msg.delSuccess"))
                    .setBackUrl("/CareforAction.do?operation="+OperationConst.OP_UPDATE_PREPARE+"&KeyId="+keyId+"&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.delFailture")).
			setBackUrl("/CareforAction.do?operation="+OperationConst.OP_UPDATE_PREPARE+"&KeyId="+keyId+"&winCurIndex="+request.getParameter("winCurIndex")).
            setAlertRequest(request);
            return getForward(request, mapping, "alert");
		}
	}

	/**
	 * �޸�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		CareforDelBean bean = new CareforDelBean();
		read(form,bean);
		CareforMgt mgt = new CareforMgt();
		String keyId = request.getParameter("KeyId");
		Result r = mgt.updateCareforDel(bean);
		if(r.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			EchoMessage.error().add(getMessage(
                    request, "crm.carefor.lb.eventname")+getMessage(
                    request, "crm.sys.isExist"))
                    .setBackUrl("/CareforAction.do?operation=7&KeyId="+keyId+"&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else if(r.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, "common.msg.updateSuccess"))
                    .setBackUrl("/CareforAction.do?operation=7&KeyId="+keyId+"&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.updateFailture")).
			setBackUrl("/CareforAction.do?operation=7&KeyId="+keyId+"&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
            return getForward(request, mapping, "alert");
		}
	}

	/**
	 * �޸�ǰ��׼��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String id 	  = getParameter("id",request);			/*id*/
		String detail = getParameter("detail", request) ;	/*����*/
		request.setAttribute("detail", detail) ;
		
		CareforMgt mgt = new CareforMgt();
		Result r = mgt.loadCareforDel(id);
		if(r.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("bean", r.getRetVal());
		}
		return mapping.findForward("update");
	}
	
	/**
	 * ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		CareforDelBean bean = new CareforDelBean();
		read(form,bean);
		CareforMgt mgt = new CareforMgt();
		String keyId = request.getParameter("KeyId");
		Result r = mgt.addCareforDel(bean);
		if(r.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			EchoMessage.error().add(getMessage(request, "crm.carefor.lb.eventname")+getMessage(request, "crm.sys.isExist"))
			.setBackUrl("/CareforAction.do?operation=7&KeyId="+keyId+"&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}else if(r.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, "common.msg.addSuccess"))
                    .setBackUrl("/CareforAction.do?operation=7&KeyId="+keyId+"&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
			.setBackUrl("/CareforAction.do?operation=7&KeyId="+keyId+"&winCurIndex="+request.getParameter("winCurIndex")).
			setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
	}

	/**
	 * ���ǰ��׼��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");//����ID
		String nextSeq = request.getParameter("nextSeq");//��ϸ���
		request.setAttribute("ref_id", id);
		request.setAttribute("sequence", nextSeq);
		return mapping.findForward("add");
	}

}
