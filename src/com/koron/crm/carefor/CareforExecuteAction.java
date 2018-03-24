package com.koron.crm.carefor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.bean.CareforActionBean;
import com.koron.crm.bean.CareforActionDelBean;
import com.koron.crm.bean.CareforDelBean;
import com.koron.crm.clientLinkman.ClientLinkmanMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 * 客户关怀执行
 * 
 * @author 徐磊
 * 
 */
public class CareforExecuteAction extends MgtBaseAction {

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {
		// 查询
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:// 添加
			forward = add(mapping, form, request, response);
			break;
		// 新增前的准备
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			String type = request.getParameter("type");
			if("stopPath".equals(type)){
				forward = stopPath(mapping, form, request, response);
			}else{
				forward = update(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE_PREPARE:// 修改前的准备
			forward = updatePrepare(mapping, form, request, response);
			break;
		// 详细
		case OperationConst.OP_DETAIL:
		    forward = detail(mapping, form, request,response);
			break;
		default:
			forward = query(mapping, form, request, response);
			break;
		}
		return forward;
	}

	/**
	 * 事件详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		CareforMgt mgt = new CareforMgt();
	    Result rst = mgt.queryOneCareforAction(id);
	    request.setAttribute("bean", rst.getRetVal());
		
		return mapping.findForward("detail");
	}

	private ActionForward stopPath(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		CareforMgt mgt = new CareforMgt();
		boolean stop = "true".equals(request.getParameter("isstop"));
		Result rst = mgt.stopTheCarefor(id,stop);
		String clientId = request.getParameter("clientId");
		if (rst.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.success().add(
					getMessage(request, "com.auto.succeed")).setBackUrl(
					"/CRMClientAction.do?operation=5&type=contact&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&moduleId=1&viewId=1_1&keyId="+clientId).setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			EchoMessage.error().add(getMessage(request, "com.auto.error"))
					.setBackUrl("/CRMClientAction.do?operation=5&type=contact&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&moduleId=1&viewId=1_1&keyId="+clientId).setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String id = request.getParameter("delId");
		CareforMgt mgt = new CareforMgt();
		Result r = mgt.loadCareforActionDel(id);
		CareforActionDelBean del = (CareforActionDelBean)r.getRetVal();
		request.setAttribute("del", del);
		
		ArrayList<String> names = (ArrayList<String>) mgt.queryActorName(
				del.getActor().split(";")).getRetVal();
		String actor = "";
		for (String s : names) {
			actor += s + ";";
		}
		request.setAttribute("actor", actor);
		// 事件名称
		CareforDelBean action = (CareforDelBean) (mgt
				.loadCareforDel(del.getEventId()).getRetVal());
		request.setAttribute("bean", action);

		return mapping.findForward("addDel");
	}

	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String actionType = request.getParameter("actionType");
		String id = request.getParameter("id");
		CareforMgt mgt = new CareforMgt();
		Result result= null;
		if("action".equals(actionType) || "overleap".equals(actionType)){
			String createTime = request.getParameter("createTime");
			if(createTime==null || createTime.length() ==0){
				createTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
			}
			int status = 2;
			if("overleap".equals(actionType)){
				status = 3;
			}
			
			result= mgt.updateCareforActionDel(id,request.getParameter("startDate"),request.getParameter("endDate"),
					createTime,request.getParameter("remark"), status);			
		}else if("createPlan".equals(actionType)){
			String startDate = getParameter("startDate",request);
			result= mgt.createJobPlan(id,startDate);
		}
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.error().add(getMessage(request, "com.auto.succeed")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		} else if(result.getRetCode() == ErrorCanst.DATA_ALREADY_USED){
			EchoMessage.error().add(getMessage(request, "crm.careforaction.lb.planhas")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}else {
			EchoMessage.error().add(getMessage(request, "com.auto.error")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		
		
		
	}

	private ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String pathId = request.getParameter("pathId");
		String clientId = request.getParameter("clientId");

		CareforMgt mgt = new CareforMgt();
		request.setAttribute("clientId", clientId);
		Result rc = mgt.queryClientName(clientId);
		String[] client = (String[]) rc.getRetVal();
		request.setAttribute("clientName", client[1]);
		Result r = mgt.loadCarefor(pathId);
		request.setAttribute("bean", r.getRetVal());
		ClientLinkmanMgt cmgt = new ClientLinkmanMgt();
		Locale locale = (Locale) request.getSession().getAttribute(
				org.apache.struts.Globals.LOCALE_KEY);
		Result rst = cmgt.queryLinkMan(locale.toString(), null, null,
				client[0], null, null,null);
		request.setAttribute("clients", rst.getRetVal());
		request.setAttribute("time", BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMdd));
		return mapping.findForward("add");
	}

	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CareforMgt mgt = new CareforMgt();
		CareforActionBean bean = new CareforActionBean();
		bean.setBaselineDate(request.getParameter("baseDate"));
		
		String[] linkman = request.getParameterValues("linkman");
		String clientId = getParameter("clientId", request) ;	/*客户Id*/
		String receiver = "";// 事件接收人
		if (linkman == null || linkman.length==0) {
			/*如果不选择事件接收人，默认为所有的人*/			
		}else{
			for(String s : linkman) {
				if(receiver.equals("")) {
					receiver = s+";";
				}else{
					receiver += s+";";
				}
			}
		}
		bean.setReceiver(receiver);
		String actor = request.getParameter("actor");// 执行人
		if(actor==null ||"".equals(actor)){/*如果不选择执行人，默认为自己*/
			actor = getLoginBean(request).getId();
		}
		bean.setActor(actor);
		bean.setClientId(clientId);
		bean.setCareforId(request.getParameter("careforId"));
		bean.setCareforName(request.getParameter("careforName"));
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		
		Result rst = mgt.addCareforAction(bean);
		if (rst.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "com.auto.succeed"))
					   .setAlertRequest(request);
			return getForward(request, mapping, "alert");
		} else {
			EchoMessage.error().add(getMessage(request, "com.auto.error"))
					   .setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
	}

	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String type = request.getParameter("type");
		if ("iframe".equals(type)) {
			return queryIframe(mapping, form, request, response);
		} else {
			return queryAll(mapping, form, request, response);
		}
	}

	private ActionForward queryIframe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CareforMgt mgt = new CareforMgt();
		String clientId = request.getParameter("clientId");
		request.setAttribute("clientId", clientId);
		Result r =  mgt.querySelectedCarefor() ;// 查询所有启用的路径
		request.setAttribute("allCarefor", r.getRetVal());
		Result r1 = mgt.queryCareforAction( "1", clientId);// 正在执行的
		request.setAttribute("r1", r1.getRetVal());
		Result r2 = mgt.queryCareforAction( "2", clientId);// 执行完毕的
		request.setAttribute("r2", r2.getRetVal());
		Result r3 = mgt.queryCareforAction( "3", clientId);// 停止执行的
		request.setAttribute("r3", r3.getRetVal());
		return mapping.findForward("ifarme");
	}

	private ActionForward queryAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String status = request.getParameter("status");
		String clientName = request.getParameter("clientName");
		String clientNo = request.getParameter("clientNo");
		request.setAttribute("clientName", clientName);
		request.setAttribute("clientNo", clientNo);
		request.setAttribute("status", status);
		CareforMgt mgt = new CareforMgt();
		int pageSize = this.getParameterInt("pageSize", request);
		int pageNo = this.getParameterInt("pageNo", request);
		if(pageSize == 0) pageSize = 100;
		if(pageNo == 0) pageNo = 1;
		Result r = mgt.queryCareforActionByClientName( status, clientName,pageSize,pageNo);
		if(r.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List<CareforActionBean> beanList = (List<CareforActionBean>)r.retVal;
			if(beanList != null){
				ArrayList<String> list = new ArrayList<String>();
				for(CareforActionBean bean :beanList){
					list.add(bean.getClientId());
				}
				Result rs = mgt.queryClientNameMap(list);
				request.setAttribute("clientMap", rs.getRetVal());
			}
		}
		request.setAttribute("list", r.getRetVal());
		request.setAttribute("pageBar", pageBar(r, request));
		return mapping.findForward("list");
	}
}
