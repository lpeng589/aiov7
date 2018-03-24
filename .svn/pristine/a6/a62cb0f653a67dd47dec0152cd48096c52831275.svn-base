package com.menyi.aio.web.systemSafe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.*;
import com.menyi.web.util.*;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import com.menyi.aio.bean.RoleModuleBean;
import java.text.SimpleDateFormat;
import com.menyi.aio.bean.RoleScopeBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.advance.AdvanceAction;
import com.menyi.aio.web.alert.AlertSetMgt;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;

import java.io.FileNotFoundException;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.aio.web.systemset.SystemSetMgt;

import java.util.List;
import com.menyi.aio.bean.ModuleOperationBean;
import com.menyi.aio.web.role.*;

/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class SystemSafeAction extends MgtBaseAction {

	SystemSafeMgt mgt = new SystemSafeMgt();
	
	public SystemSafeAction() {
	}

	/**
	 *
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	protected ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginBean loginBean = getLoginBean(request);
		if("true".equals(request.getParameter("save"))){
			String hour1 = request.getParameter("hour1"); //数据备份第一时间
			String mult1 = request.getParameter("mult1"); //数据备份第一分钟
			String hour2 = request.getParameter("hour2"); //数据备份第二时间
			String mult2 = request.getParameter("mult2"); //数据备份第二分钟
			String backDay = request.getParameter("backDay"); //数据备份保存天数
			String backPath = request.getParameter("backPath"); //数据备份保存路径
			String[] dayPiece = request.getParameterValues("dayPiece"); //碎片整理日期
			String hourPiece = request.getParameter("hourPiece"); //碎片整理时间
			String multPiece = request.getParameter("multPiece"); //碎片整理分种
			
			String dayPieces = "";
			if(dayPiece != null){
				for(String d:dayPiece){
					dayPieces += d+",";
				}
			}
			ArrayList<String[]> list = new ArrayList();
			list.add(new String[]{"hour1",hour1});
			list.add(new String[]{"mult1",mult1});
			list.add(new String[]{"hour2",hour2});
			list.add(new String[]{"mult2",mult2});
			list.add(new String[]{"backDay",backDay});
			list.add(new String[]{"backPath",backPath});
			list.add(new String[]{"dayPiece",dayPieces});
			list.add(new String[]{"hourPiece",hourPiece});
			list.add(new String[]{"multPiece",multPiece});
			
			Result rs = mgt.updateSafeValue(list);
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg", "保存失败");
				return getForward(request, mapping, "systemSafe");
			}
			SystemSafeTimer.init();
		}	
		Result rs = mgt.querySafeValues();
		String userDir = System.getProperty("user.dir");
		String defDisk = userDir.substring(0,userDir.indexOf(":")+1);
		String defPath = defDisk+"\\AioDefDbBakup";
		request.setAttribute("result", rs.retVal);
		request.setAttribute("defPath", defPath);
		
		
			
		return getForward(request, mapping, "systemSafe");

	}

}
