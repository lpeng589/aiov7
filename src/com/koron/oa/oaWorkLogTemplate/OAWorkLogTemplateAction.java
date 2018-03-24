package com.koron.oa.oaWorkLogTemplate;


import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.OAWorkLogTemplateBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>Title:通用评论功能</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class OAWorkLogTemplateAction extends BaseAction{
	
	OAWorkLogTemplateMgt mgt = new OAWorkLogTemplateMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		String type = getParameter("type", request);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		
		switch (operation) {
			case OperationConst.OP_ADD:	
				forward = add(mapping, form, request, response);//添加
				break;
			case OperationConst.OP_UPDATE_PREPARE:	
				forward = updatePrepare(mapping, form, request, response);//修改前
				break;
			case OperationConst.OP_UPDATE:	
				forward = update(mapping, form, request, response);//修改
				break;
			case OperationConst.OP_DELETE:	
				forward = delete(mapping, form, request, response);//删除
				break;
			
			default:
				forward = query(mapping, form, request, response);//首页查询
				break;
		}
		return forward;
	}

	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String templateId = getParameter("templateId",request);
		Result rs = mgt.delTemplateBean(templateId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String templateId = getParameter("templateId",request);
		OAWorkLogTemplateBean templateBean = mgt.loadTemplateBean(templateId);
		read(form,templateBean);
		
		LoginBean loginBean = getLoginBean(request);
		templateBean.setLastUpdateBy(loginBean.getId());
		templateBean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		
		Result rs = mgt.updateTemplateBean(templateBean);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
		
	}

	/**
	 * 修改前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String templateId = getParameter("templateId",request);
		OAWorkLogTemplateBean templateBean = mgt.loadTemplateBean(templateId);
		request.setAttribute("templateBean",templateBean);
		return getForward(request, mapping, "update");
	}
	
	/*
	 * 添加
	 */
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		OAWorkLogTemplateBean templateBean = new OAWorkLogTemplateBean();
		read(form,templateBean);
		LoginBean loginBean = getLoginBean(request);
		String userId = loginBean.getId();
		String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		templateBean.setId(IDGenerater.getId());
		templateBean.setCreateBy(userId);
		templateBean.setLastUpdateBy(userId);
		templateBean.setCreateTime(nowDate);
		templateBean.setLastUpdateTime(nowDate);
		Result rs = mgt.addTemplateBean(templateBean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}

	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.workLogTemplateQuery(loginBean);
		ArrayList<OAWorkLogTemplateBean> templateBeanList = (ArrayList<OAWorkLogTemplateBean>)rs.retVal;
		request.setAttribute("templateBeanList",templateBeanList);
		
		return getForward(request, mapping, "index");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}
	
	
}
