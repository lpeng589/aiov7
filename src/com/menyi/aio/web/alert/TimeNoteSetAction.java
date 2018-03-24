package com.menyi.aio.web.alert;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class TimeNoteSetAction extends MgtBaseAction
{
	private static final TimeNoteSetMgt timeNoteMgt = new TimeNoteSetMgt();
	/**
	 * ��ʱ֪ͨ������ں���
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		int operation = getOperation(request);
		String  type=request.getParameter("type");
		ActionForward forward = null;
		// ���ݲ�ͬ����ִ����Ӧ�ķ���
		switch (operation)
		{
		case OperationConst.OP_QUERY: // ��ѯ��ʱ֪ͨ��������
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			if(type.equals("mode"))// ����֪ͨ��ʽ
				forward = saveMode(mapping, form, request, response);
			else if(type.equals("employee"))// ����֪ͨ����
				forward = saveSetUser(mapping, form, request, response);
			else if(type.equals("time"))//Ԥ��ʱ������
				forward = saveTime(mapping, form, request, response);
			else if(type.equals("status"))//�Ƿ���������
				forward = updateStatus(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			if(type.equals("modepre"))// ����֪ͨ��ʽ
				forward = setPreMode(mapping, form, request, response);
			else if(type.equals("employeepre"))// ����֪ͨ����
				forward = selectUser(mapping, form, request, response);
			else if(type.equals("timepre"))//Ԥ��ʱ������
				forward = setPreTime(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	/**
	 * ��ѯ��ʱ֪ͨģ���б�
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		LoginBean loginBean = getLoginBean(request);
		String moduleType=loginBean.getDefSys();
		String defSys = request.getParameter("defSys");
		if( defSys != null && defSys.length() > 0){
			moduleType = defSys ; 
		}
		Result rs = timeNoteMgt.list(getEnumerationItems("TimingMsg", request),
				getEnumerationItems("IsAble", request), null,moduleType);
		// ��ѯ�ɹ�
		if (ErrorCanst.DEFAULT_SUCCESS == rs.getRetCode())
		{
			request.setAttribute("noteList", rs.getRetVal());
		} else
		// ��ѯʧ��
		{
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}
		return mapping.findForward("timeNoteSetList");
	}

	/**
	 * ���û��߽��ö�ʱ֪ͨģ��
	 */
	public ActionForward updateStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String[] keyIds = request.getParameterValues("keyId");
		StringBuffer sb = new StringBuffer();
		for (String key : keyIds)
		{
			sb.append("'").append(key).append("'").append(",");
		}
		String keyids = sb.substring(0, sb.lastIndexOf(","));
		int status = getParameterInt("status", request);
		Result rs = timeNoteMgt.updateStatus(keyids, status);
		if (ErrorCanst.DEFAULT_SUCCESS != rs.getRetCode()){
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");		 
		}
		return query(mapping, form, request, response);
	}
	
	/**
	 * ����֪ͨʱ�����
	 */
	public ActionForward setPreTime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String alertId = request.getParameter("alertId");
		Result rs = timeNoteMgt.timeNoteModel(alertId) ;
	    String []str=(String[])rs.getRetVal();
	    
		request.setAttribute("alertId", alertId);
		request.setAttribute("sqlDefine", str[0]);
		request.setAttribute("alertTime", str[2]);
		return mapping.findForward("selectTimeNoteTime");
	}

	/**
	 * ����֪ͨ��ʽ����
	 */
	public ActionForward setPreMode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String alertId = request.getParameter("alertId");
		Result rs = timeNoteMgt.timeNoteModel(alertId) ;
	    String []str=(String[])rs.getRetVal();
	    
		request.setAttribute("alertId", alertId);
		request.setAttribute("sqlDefine", str[0]);
		request.setAttribute("alertMethod", str[1]);
		return mapping.findForward("selectTimeNoteModel");
	}

	/**
	 * ����֪ͨ��ʽ
	 */
	public ActionForward saveMode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String[] preModes = request.getParameterValues("preMode");//��ȡѡ���Ԥ����ʽ
		String alertId = request.getParameter("alertId");//��ȡԤ��ģ��id
		
		String modes="";
		if(preModes.length==2){
			modes="sendEmailAndNote";
		}else if(preModes.length==1){
			modes=preModes[0];
		}
		// �ڴ˸���֪ͨ��ʽ
		Result rs = timeNoteMgt.updateParMode(alertId, modes);
		if (ErrorCanst.DEFAULT_SUCCESS == rs.getRetCode())
		{
			request.setAttribute("state", "true");
		} else
		{
			request.setAttribute("state", "false");
		}
		return getForward(request, mapping, "selectTimeNoteModel");
	}
	/**
	 * ����֪ͨʱ��
	 */
	public ActionForward saveTime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String  noteTime = request.getParameter("noteTime");//��ȡѡ���Ԥ����ʽ
		String alertId = request.getParameter("alertId");//��ȡԤ��ģ��id
		// �ڴ˸���֪ͨ��ʽ
		Result rs = timeNoteMgt.updateTime(alertId, noteTime);
		if (ErrorCanst.DEFAULT_SUCCESS == rs.getRetCode())
		{
			request.setAttribute("state", "true");
		} else
		{
			request.setAttribute("state", "false");
		}
		return getForward(request, mapping, "selectTimeNoteModel");
	}
	/**
	 * �û�ѡ�����
	 */
	public ActionForward selectUser(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String alertId = request.getParameter("alertId");
		Result rss = timeNoteMgt.listUser(alertId);//��ȡϵͳ�û�
		Result rss2=timeNoteMgt.timeNoteModel(alertId);
		String []str=(String[])rss2.getRetVal();
		List empList = (List) rss.getRetVal();
		request.setAttribute("alertId", alertId);
		request.setAttribute("sqlDefine", str[0]);
		request.setAttribute("empList", empList);
		return mapping.findForward("selectTimeNoteUser");
	}
	/**
	 * ����Ԥ������
	 */
	public ActionForward saveSetUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
	    String alertId = request.getParameter("alertId");
	    String[] empids = request.getParameterValues("empids");//��ȡѡ����û�id
	    Result rs=timeNoteMgt.updateParUser(alertId, empids);//����Ԥ������
	    
	    if (ErrorCanst.DEFAULT_SUCCESS != rs.getRetCode())
		{
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}else{
			request.setAttribute("state","true" ) ;
		}
		return getForward(request, mapping, "selectTimeNoteUser");
	}
}
