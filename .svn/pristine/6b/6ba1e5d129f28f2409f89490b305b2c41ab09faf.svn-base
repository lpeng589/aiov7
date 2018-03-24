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
	 * 定时通知设置入口函数
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		int operation = getOperation(request);
		String  type=request.getParameter("type");
		ActionForward forward = null;
		// 根据不同操作执行相应的方法
		switch (operation)
		{
		case OperationConst.OP_QUERY: // 查询定时通知设置类型
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			if(type.equals("mode"))// 设置通知方式
				forward = saveMode(mapping, form, request, response);
			else if(type.equals("employee"))// 设置通知对象
				forward = saveSetUser(mapping, form, request, response);
			else if(type.equals("time"))//预警时间设置
				forward = saveTime(mapping, form, request, response);
			else if(type.equals("status"))//是否启用设置
				forward = updateStatus(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			if(type.equals("modepre"))// 设置通知方式
				forward = setPreMode(mapping, form, request, response);
			else if(type.equals("employeepre"))// 设置通知对象
				forward = selectUser(mapping, form, request, response);
			else if(type.equals("timepre"))//预警时间设置
				forward = setPreTime(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	/**
	 * 查询定时通知模板列表
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
		// 查询成功
		if (ErrorCanst.DEFAULT_SUCCESS == rs.getRetCode())
		{
			request.setAttribute("noteList", rs.getRetVal());
		} else
		// 查询失败
		{
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}
		return mapping.findForward("timeNoteSetList");
	}

	/**
	 * 启用或者禁用定时通知模板
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
	 * 设置通知时间界面
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
	 * 设置通知方式界面
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
	 * 设置通知方式
	 */
	public ActionForward saveMode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String[] preModes = request.getParameterValues("preMode");//获取选择的预警方式
		String alertId = request.getParameter("alertId");//获取预警模板id
		
		String modes="";
		if(preModes.length==2){
			modes="sendEmailAndNote";
		}else if(preModes.length==1){
			modes=preModes[0];
		}
		// 在此更新通知方式
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
	 * 设置通知时间
	 */
	public ActionForward saveTime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String  noteTime = request.getParameter("noteTime");//获取选择的预警方式
		String alertId = request.getParameter("alertId");//获取预警模板id
		// 在此更新通知方式
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
	 * 用户选择界面
	 */
	public ActionForward selectUser(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String alertId = request.getParameter("alertId");
		Result rss = timeNoteMgt.listUser(alertId);//获取系统用户
		Result rss2=timeNoteMgt.timeNoteModel(alertId);
		String []str=(String[])rss2.getRetVal();
		List empList = (List) rss.getRetVal();
		request.setAttribute("alertId", alertId);
		request.setAttribute("sqlDefine", str[0]);
		request.setAttribute("empList", empList);
		return mapping.findForward("selectTimeNoteUser");
	}
	/**
	 * 设置预警对象
	 */
	public ActionForward saveSetUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
	    String alertId = request.getParameter("alertId");
	    String[] empids = request.getParameterValues("empids");//获取选择的用户id
	    Result rs=timeNoteMgt.updateParUser(alertId, empids);//更新预警对象
	    
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
