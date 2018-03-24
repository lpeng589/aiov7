package com.menyi.aio.web.processOldData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;

import java.io.File;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Hashtable;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.login.LoginBean;
import java.util.*;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.StockDetBean;
import com.menyi.aio.bean.GoodsPropEnumItemBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.goodsProp.GoodsPropMgt;
import com.menyi.aio.web.iniSet.IniAccMgt;
import com.menyi.aio.bean.GoodsPropInfoBean;

/**
 * <p>
 * Title: 单位管理控制类
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: 周新宇
 * </p>
 * 
 * @author 周新宇
 * @version 1.0
 */
public class ProcessOldDataAction extends MgtBaseAction {
	public ProcessOldDataAction() {
	}

	/**
	 * exe 控制器入口函数
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
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_UPDATE_PREPARE:
            forward = updatePrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE:
            forward = update(mapping, form, request, response);
            break;
        default:
            forward = updatePrepare(mapping, form, request, response);
        }
        return forward;
	}

	/**
	 * 处理旧数据的界面
	 */
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return getForward(request, mapping, "processOldData");
	}

	protected ActionForward update(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		LoginBean login = this.getLoginBean(request);
		String proType=request.getParameter("proType");
		Result rs=null;
		ProcessOldDataMgt mgt=new ProcessOldDataMgt();
		if(proType!=null&&proType.equals("affix")){
			rs=mgt.processAffix(request.getSession().getServletContext().getRealPath("/upload"));
		}

		forward = getForward(request, mapping, "message");
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.success().setNotAutoBack().add(
					getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl(
							"/ProcessOldData.do?winCurIndex="
									+ request.getAttribute("winCurIndex"))
					.setAlertRequest(request);
		}else {
			EchoMessage.error().add(rs.getRetCode(), request).setBackUrl(
					"/ProcessOldData.do?winCurIndex="
							+ request.getAttribute("winCurIndex")).setRequest(
					request);
		}
		return forward;
	}
}
