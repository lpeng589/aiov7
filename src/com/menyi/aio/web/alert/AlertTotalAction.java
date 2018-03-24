package com.menyi.aio.web.alert;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:预警汇总Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-08-15 上午 11:30
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class AlertTotalAction extends MgtBaseAction{
	
	AlertTotalMgt mgt = new AlertTotalMgt();
	
	/**
	 * 预警汇总入口函数
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
		ActionForward forward = null;
		// 根据不同操作执行相应的方法
		switch (operation)
		{
		case OperationConst.OP_QUERY: // 查询预警
			forward = query(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	
	
	/**
	 * 预警汇总查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/**
		 * 查询预警数据
		 */
		Result rs = mgt.queryData(request, this.getLoginBean(request), this.getLocale(request).toString());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List list = (ArrayList)rs.retVal;
			request.setAttribute("alertlist", list);
		}else if(rs.retCode == ErrorCanst.RET_NO_RIGHT_ERROR){
			//报表模块权限错误
			request.setAttribute("noback", true) ;
            ActionForward forward = getForward(request, mapping, "message");
            EchoMessage.error().add(String.valueOf(rs.getRetVal())).setRequest(request);
            return forward;
		}
		return getForward(request, mapping, "alertTotal");
	}
}
