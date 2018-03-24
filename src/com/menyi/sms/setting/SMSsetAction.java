package com.menyi.sms.setting;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.NoteSetBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIOTelecomCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.SecurityLock;
import com.menyi.web.util.SystemState;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 *
 * <p>
 * Company: 付湘鄂
 * </p>
 *
 * @author 付湘鄂
 * @version 1.0
 */
public class SMSsetAction extends MgtBaseAction {
	public SMSsetAction() {
	}

	SMSsetMgt mgt = new SMSsetMgt();


	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		// 跟据不同操作类型分配给不同函数处理
		String choice = request.getParameter("choice");
		if (choice == null) { 
			forward = load(mapping, form, request, response);// 通过菜单跳转过来时
		}
		return forward;
	}

	protected ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean lg = new LoginBean();
		lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		if (SystemState.instance.dogState == SystemState.DOG_FORMAL) {
			Result rs = mgt.SelectAll();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				NoteSetBean nsb = new NoteSetBean();
				int flag = 1;
				if (((ArrayList) rs.getRetVal()).size() > 0) {
					nsb = (NoteSetBean) ((ArrayList) rs.getRetVal()).get(0);
					flag = nsb.getStatusId();
				} else {
					nsb.setUrl(BaseEnv.bol88URL+"/services");
					nsb.setGouPass("123456");
					nsb.setStatusId(1);
					nsb.setId(IDGenerater.getId());
					nsb.setSmsSign("AIO");
					nsb.setCreateBy(lg.getId());
					nsb.setLastUpdateBy(lg.getId());
					nsb.setCreateTime(BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss));
					nsb.setLastUpdateTime(BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss));
					nsb.setSCompanyID(lg.getSunCmpClassCode());
					rs = mgt.Insert(nsb);
					flag = 1;
				}
				
				request.setAttribute("display", 1);
				request.setAttribute("flag", flag);
				request.setAttribute("noteSet", nsb);
			}
		} else {
			request.setAttribute("display", 2);
		}

		return getForward(request, mapping, "toSMSset");

	}

	
}
