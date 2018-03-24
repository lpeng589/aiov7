package com.koron.oa.holidaySetting;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class HolidaySettingAction extends MgtBaseAction{
	
	private HolidaySettingMgt mgt = new HolidaySettingMgt();
	static String backsy="0",backsm="0";
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = del(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String sy = (String)request.getParameter("SY");
		String sm = (String)request.getParameter("SM");
		String loginId = getLoginBean(request).getId();
		backsy = sy;
		backsm = sm;
		String[] objs = request.getParameterValues("selectDay");
		String[] dates = new String[objs.length];
		for (int i = 0; i < objs.length; i++) {
			dates[i] = sy+"-"+sm+"-"+objs[i];
		}
		List addList = new ArrayList();
		List updateList = new ArrayList();
		//把要执行增加的数据和执行修改的数据分别放入集合，减少数据传输的次数
		for (int i = 0; i < objs.length; i++) {
			String[] strs = new String[2];
			String strDate = BaseDateFormat.format(BaseDateFormat.parse(dates[i], 
								BaseDateFormat.yyyyMMdd), BaseDateFormat.yyyyMMdd) ;
			strs[0]=strDate;
			strs[1]=loginId;
			//判断此日期在数据库是否已经存在数据,如果存在就执行修改操作
			int count = mgt.hasSetting(strs[0]).getRetCode();
			if(count>0){
				updateList.add(strs);
			}else{
				addList.add(strs);
			}
		}
		Result result1 = mgt.add(addList);
		Result result2 = mgt.update(updateList);
		if(result1.getRetCode()==ErrorCanst.DEFAULT_SUCCESS && result2.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			 //添加成功
            EchoMessage.success().add(getMessage(
                request, "common.msg.addSuccess"))
                .setBackUrl("/HolidaySettingAction.do?operation="+OperationConst.OP_QUERY).
                setAlertRequest(request);
		}else{
			//添加失败
            EchoMessage.error().add(getMessage(
                request, "common.msg.addFailture")).
                setAlertRequest(request);
		}
		return mapping.findForward("alert");
	}
	
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return null;
	}
	
	protected ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String sy = (String)request.getParameter("SY");
		String sm = (String)request.getParameter("SM");
		backsy = sy;
		backsm = sm;
		String[] objs = request.getParameterValues("selectDay");
		String[] dates = new String[objs.length];
		for (int i = 0; i < objs.length; i++) {
			dates[i] = sy+"-"+sm+"-"+objs[i];
		}
		List list = new ArrayList();
		for (int i = 0; i < objs.length; i++) {
			String[] strs = new String[1];
			String strDate = BaseDateFormat.format(BaseDateFormat.parse(dates[i], 
					BaseDateFormat.yyyyMMdd), BaseDateFormat.yyyyMMdd) ;
			strs[0]=strDate;
			list.add(strs);
		}
		Result result = mgt.del(list);
		if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			 //添加成功
            EchoMessage.success().add(getMessage(
                request, "common.msg.delSuccess"))
                .setBackUrl("/HolidaySettingAction.do?operation="+OperationConst.OP_QUERY).
                setAlertRequest(request);
		}else{
			//添加失败
            EchoMessage.error().add(getMessage(
                request, "common.msg.delError")).
                setAlertRequest(request);
		}
		return mapping.findForward("alert");
	}
	
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Result rsHolidays = mgt.query();
		List holidayList = (List)rsHolidays.getRetVal();
		request.setAttribute("holidayList", holidayList);
		List dateList = new ArrayList();
		dateList.add(backsy);
		dateList.add(backsm);
		request.setAttribute("dateList", dateList);
		backsy = "0";
		backsm = "0";
		return getForward(request, mapping, "main");
	}
	
}
