package com.menyi.aio.web.finance.adjustExchange;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.*;
/**
 * 
 * <p>
 * Title:��ĩ�{�RAction
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-03-28 ���� 11:30
 * @Copyright: �Ƙsܛ��
 * @Author fjj
 * @preserve all
 */
public class AdjustExchangeAction extends MgtBaseAction {
	
	AdjustExchangeMgt mgt = new AdjustExchangeMgt();
	
	/**
	 * exe ��������ں���
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ������ͬ������ͷ���o��ͬ����̎��
		int operation = getOperation(request);
		ActionForward forward = null;
		
		String opType = request.getParameter("optype");
		
		switch (operation) {
		case OperationConst.OP_QUERY:
			if(opType != null && "queryExchange".equals(opType)){
				//��ԃ���Ю�ǰ����ŅR��
				forward = queryExchange(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			//��ĩ����
			forward = adjustExchange(mapping, form, request, response);
			break;
		default:
			
		}
		return forward;
	}
	
	
	/**
	 * ��ԃ��ǰ���еąR��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward queryExchange(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		
		/* ����ڼ�*/
		LoginBean loginBean = this.getLoginBean(request);
    	Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable)sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean)hashSession.get("AccPeriodAcc");
		
		/* ��ԃ������ڼ�����еąR��*/
		Result result = mgt.queryExchange(accPeriodBean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			request.setAttribute("exchangeList", result.retVal);
		}
		return getForward(request, mapping, "dealexchange");
	}
	
	/**
	 * ��ĩ����(�޸Ļ��ҵ������ʺ�����ת��ƾ֤���¼������ʷ��)
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward adjustExchange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* ����Id*/
		String[] keyIds = request.getParameterValues("keyId");
		/* ��ǰ����*/
		String[] currentexchange = request.getParameterValues("currentexchange");
		/* ��������*/
		String[] adjustexchange = request.getParameterValues("adjustexchange");
		/* ��ƿ�Ŀ*/
		String accCode = request.getParameter("accCode");
		/* ����ƾ֤���ࣨ1������� 2�����ʧ 3������棩*/
//		String[] accAssort = request.getParameterValues("accAssort");
		/* ƾ֤����*/
		String accTime = request.getParameter("accTime");
		/* ƾ֤��*/
		String credTypeID = request.getParameter("credTypeID");
		/* ժҪ*/
		String recordCommon = request.getParameter("recordCommon");
		
		/* ����ڼ�*/
		LoginBean loginBean = this.getLoginBean(request);
    	Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable)sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean)hashSession.get("AccPeriodAcc");
		
		String profitLoss = this.getMessage(request,"sysAcc.lb.adjustExchange");
		String settle = this.getMessage(request, "SysAcc.lb.settle");
		
		MessageResources resources = null;
        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		
		/* ��ĩ����*/
		Result result = mgt.adjustExchange(keyIds, currentexchange, adjustexchange, accCode, 
				accTime, credTypeID, recordCommon, accPeriodBean, loginBean, profitLoss, settle, this.getLocale(request), resources);
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, loginBean.getId(),loginBean.getEmpFullName(),loginBean.getSunCmpClassCode(), "��ĩ����:"+accPeriodBean.getAccYear()+"."+accPeriodBean.getAccMonth(), "��ĩ����", "�½�","��ĩ����");
			// ��ĩ����ɹ�
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.adjustExchangeSuccess")).setAlertRequest(request);
			/**EchoMessage.success().add(
					getMessage(request, "common.msg.adjustExchangeSuccess"))
					.setBackUrl("/AdjustExchangeAction.do?operation=4&optype=queryExchange")
					.setAlertRequest(request);*/
		} else {
			EchoMessage.success().add("��ĩ����ʧ��");
			/**EchoMessage.error().add(result.getRetCode(), request).setBackUrl(
					"/AdjustExchangeAction.do?operation=4&optype=queryExchange").setRequest(
					request);*/
		}
		return getForward(request, mapping, "alert");
	}
}
