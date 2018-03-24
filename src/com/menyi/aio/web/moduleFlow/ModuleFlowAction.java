package com.menyi.aio.web.moduleFlow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.ModuleFlowDetBean;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;
import java.util.*;

import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;

import javax.servlet.ServletContext;

/**
 * <p>Title: 单位管理控制类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 张雪</p>
 *
 * @author 张雪
 * @version 1.0
 */
public class ModuleFlowAction extends BaseAction {

	ModuleFlowMgt mgt = new ModuleFlowMgt();

	public ModuleFlowAction() {
	}

	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req);
		if (loginBean == null || loginBean.getOperationMap() == null) {
			BaseEnv.log
					.debug("ModuleFlowAction.doAuth() ---------- loginBean or getOperationMap() is null");
			return getForward(req, mapping, "indexPage");
		}
		return null;
	}

	/**
	 * exe 控制器入口函数
	 *
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String operation = this.getParameter("operation", request);

		//跟据不同操作类型分配给不同函数处理
		ActionForward forward = null;
		if (operation != null && operation.equals("left_menu")) {
			forward = queryMyDest(mapping, form, request, response);
		} else if (operation != null && operation.equals("my_dest")) {
			forward = queryMyDest(mapping, form, request, response);
		} else if (operation != null && operation.equals("destSet")) {
			forward = queryMyDest(mapping, form, request, response);
		} else if (operation != null && operation.equals("destDefaultSet")) {
			forward = querySystemDefaultDest(mapping, form, request, response);
		} else if (operation != null && operation.equals("docFlow")) {
			forward = showDocFlow(mapping, form, request, response);
		} else if (operation != null && operation.equals("addMyDest")) {
			forward = addMyDest(mapping, form, request, response);
		} else if (operation != null && operation.equals("cancelMyDest")) {
			forward = cancelMyDest(mapping, form, request, response);
		}else if (operation != null && operation.equals("orderMyDest")) {
			forward = orderMyDest(mapping, form, request, response);
		}else if (operation != null && operation.equals("getAllIcon")) {
			forward = getAllIcon(mapping, form, request, response);
		}else if (operation != null && operation.equals("changePic")) {
			forward = changePic(mapping, form, request, response);
		}else if (operation != null && operation.equals("addClass")) {
			forward = addClass(mapping, form, request, response);
		}else if (operation != null && operation.equals("updateClass")) {
			forward = updateClass(mapping, form, request, response);
		}else if (operation != null && operation.equals("changeClass")) {
			forward = changeClass(mapping, form, request, response);
		}else if (operation != null && operation.equals("classSet")) {
			forward = classSet(mapping, form, request, response);
		}else if (operation != null && operation.equals("delClass")) {
			forward = delClass(mapping, form, request, response);
		}
		return forward;
	}
	
	protected ActionForward delClass(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String id = request.getParameter("id");	
		String defaultSet = request.getParameter("defaultSet");	

		Result rs = mgt.delClass(id);
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/moduleFlow.do?operation=classSet&defaultSet="+defaultSet)
			.setRequest(request);
		}else if (rs.getRetCode() == ErrorCanst.DATA_ALREADY_USED) {
			EchoMessage.error().add(getMessage(request, "common.msg.DATA_ALREADY_USED")).setBackUrl("/moduleFlow.do?operation=classSet&defaultSet="+defaultSet)
			.setRequest(request);
		}else {
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setBackUrl("/moduleFlow.do?operation=classSet&defaultSet="+defaultSet)
			.setRequest(request);
		}
		return getForward(request, mapping, "message");
		
	}
	protected ActionForward classSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String defaultSet = request.getParameter("defaultSet");	
		String loginId = loginBean.getId();
		if("true".equals(defaultSet)){
			loginId = "";
		}
		Result rs = mgt.getMyClass(loginId);
		
		ArrayList flowList = (ArrayList) rs.getRetVal();		
		ActionForward forward = new ActionForward();
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("result", flowList);
			request.setAttribute("defaultSet", defaultSet);
			forward = mapping.findForward("classSet");
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			forward = getForward(request, mapping, "message");
		}
		return forward;
	}
	protected ActionForward changeClass(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String classCode = request.getParameter("classCode");	
		String id = request.getParameter("id");	
		classCode = new String(classCode.getBytes("ISO8859-1"),"UTF-8");
		String loginId= loginBean.getId();
		if("true".equals(request.getParameter("defaultSet"))){
			loginId= "";
		}
		Result rs = mgt.changeClass(id, classCode, loginId, loginBean, locale);
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", getMessage(request, "common.lb.update")+getMessage(request, "common.msg.DEFAULT_SUCCESS"));
		}else {
			request.setAttribute("msg", getMessage(request, "common.lb.update")+getMessage(request, "common.msg.DEFAULT_FAILURE"));
		}
		return getForward(request,mapping,"blank");
	}
	protected ActionForward addClass(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String className = request.getParameter("className");	
		className = new String(className.getBytes("ISO8859-1"),"UTF-8");
		String loginId= loginBean.getId();
		if("true".equals(request.getParameter("defaultSet"))){
			loginId= "";
		}
		Result rs = mgt.addClass(className,loginId);
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", getMessage(request, "common.lb.add")+getMessage(request, "common.msg.DEFAULT_SUCCESS"));
		}else {
			request.setAttribute("msg", getMessage(request, "common.lb.add")+getMessage(request, "common.msg.DEFAULT_FAILURE"));
		}
		return getForward(request,mapping,"blank");
	}
	protected ActionForward updateClass(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String id = request.getParameter("id");	
		String className = request.getParameter("className");	
		className = new String(className.getBytes("ISO8859-1"),"UTF-8");

		Result rs = mgt.updateClass(id,className);
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", getMessage(request, "common.lb.update")+getMessage(request, "common.msg.DEFAULT_SUCCESS"));
		}else {
			request.setAttribute("msg", getMessage(request, "common.lb.update")+getMessage(request, "common.msg.DEFAULT_FAILURE"));
		}
		return getForward(request,mapping,"blank");
	}
	protected ActionForward changePic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String pic = request.getParameter("pic");		
		String id = request.getParameter("id");
		pic = new String(pic.getBytes("ISO8859-1"),"UTF-8");

		Result rs = mgt.changePic(id , pic,"true".equals(request.getParameter("defaultSet"))?"":loginBean.getId(),loginBean,locale);
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", getMessage(request, "common.lb.update")+getMessage(request, "common.msg.DEFAULT_SUCCESS"));
		}else {
			request.setAttribute("msg", getMessage(request, "common.lb.update")+getMessage(request, "common.msg.DEFAULT_FAILURE"));
		}
		return getForward(request,mapping,"blank");
	}
	
	/**
	 * 获取所有的图标
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward getAllIcon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String locale = GlobalsTool.getLocale(request).toString();
		List<KeyPair> list = GlobalsTool.getEnumerationItems("ModuleIcon", locale);
		String msg="";
		for(KeyPair kp:list){
			msg +=kp.getValue()+";";
		}
		request.setAttribute("msg",msg);		
		return getForward(request,mapping,"blank");
	}
	
	protected ActionForward orderMyDest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String ids=request.getParameter("ids");
		Result rs = mgt.orderMyDest(ids,loginBean.getId(),loginBean,locale); 
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", rs.retVal);
			//request.setAttribute("msg", getMessage(request, "common.lb.update")+getMessage(request, "common.msg.DEFAULT_SUCCESS"));
		}else {
			//request.setAttribute("msg", getMessage(request, "common.lb.update")+getMessage(request, "common.msg.DEFAULT_FAILURE"));
		}
		return getForward(request,mapping,"blank");
	}
	protected ActionForward cancelMyDest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String id=request.getParameter("id");
		Result rs = mgt.cancelMyDest(id,"true".equals(request.getParameter("defaultSet"))?"":loginBean.getId(),loginBean,locale); 
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String rsh = "";
			if(rs.retVal != null && rs.retVal.equals("refresh")){
				rsh = "refresh";
			}
			request.setAttribute("msg", rsh+getMessage(request, "common.lb.del")+getMessage(request, "common.msg.DEFAULT_SUCCESS"));
		}else if(rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			request.setAttribute("msg", getMessage(request, "common.lb.del")+getMessage(request, "importData.repeat"));
		}else {
			request.setAttribute("msg", getMessage(request, "common.lb.del")+getMessage(request, "common.msg.DEFAULT_FAILURE"));
		}
		return getForward(request,mapping,"blank");
	}

	protected ActionForward addMyDest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		String title = request.getParameter("title");		
		String url = request.getParameter("url");
		title = new String(title.getBytes("ISO8859-1"),"UTF-8");
		url = new String(url.getBytes("ISO8859-1"),"UTF-8");
		String pic = "/icon/default.png";
		
		if(url!=null) url = url.replace("?src=menu", "").replace("&src=menu", "");
		Result rs = mgt.addToMyDest(title,url , pic, "true".equals(request.getParameter("defaultSet"))?"":loginBean.getId(),loginBean,locale);
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", "ok");
		}else if(rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			request.setAttribute("msg", "two");
		}else {
			request.setAttribute("msg", "no");
		}
		return getForward(request,mapping,"blank");
	}

	protected ActionForward queryMyDest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		LoginBean loginBean = getLoginBean(request);
		String locale = GlobalsTool.getLocale(request).toString();
		Result rs = mgt.getMyDest(loginBean, locale);
		ArrayList<Object[]> flowList = (ArrayList) rs.getRetVal();		
		ActionForward forward = new ActionForward();
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS||rs.getRetCode() == 2) {
			request.setAttribute("result", flowList);
			
			ArrayList<Object[]> classList = new ArrayList();
			for(Object[] os:flowList){
				boolean found = false;
				if(os[5]==null||os[5].toString().length()==0){
					continue;
				}
				for(Object[] os2:classList){
					if(os[5].equals(os2[0])){
						found = true;
						break;
					}
				}
				if(!found){
					classList.add(new Object[]{os[5],os[6]});
				}
			}
			
			if ("my_dest".equals(operation)) {
				forward = mapping.findForward("my_dest");
			}else if ("destSet".equals(operation)) {
				String loginId = "";
				if(rs.getRetCode() == 2){
					loginId = loginBean.getId();
				}
				Result rs2 = mgt.getMyClass(loginId);
				request.setAttribute("allClass", rs2.retVal);
				forward = mapping.findForward("destSet");
			} else {
				request.setAttribute("class", classList);
				forward = mapping.findForward("left_menu");
			}
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			forward = getForward(request, mapping, "message");
		}
		return forward;
	}
	protected ActionForward querySystemDefaultDest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String operation = this.getParameter("operation", request);
		String locale = GlobalsTool.getLocale(request).toString();
		Result rs = mgt.getSystemDefaultDest(locale);
		ArrayList flowList = (ArrayList) rs.getRetVal();		
		ActionForward forward = new ActionForward();
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("result", flowList);
			request.setAttribute("defaultSet", "true");
			Result rs2 = mgt.getMyClass("");
			request.setAttribute("allClass", rs2.getRetVal());
			forward = mapping.findForward("destSet");
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			forward = getForward(request, mapping, "message");
		}
		return forward;
	}
	
	protected ActionForward showDocFlow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String keyId = this.getParameter("keyId", request);
		String name = this.getParameter("name", request);
		name = GlobalsTool.toChinseChar(name);
		
		String tableDisplay = getMessage(request, name) ;
		if(tableDisplay!=null && tableDisplay.length()>0){
			name = tableDisplay ;
		}
		String winCurIndex = getParameter("winCurIndex", request);
		request.setAttribute("name", name);
		request.setAttribute("keyId", keyId);
		ActionForward forward = forward = mapping.findForward("docFlow");
		String locale = GlobalsTool.getLocale(request).toString();

		//获得报表信息
		List reportsList = (List) mgt.getReportList(keyId, locale).getRetVal();
		List statList =(List) mgt.getStatList(keyId, locale).getRetVal();
		List detailList=(List) mgt.getDetailList(keyId, locale).getRetVal();
		//查询是否有权限
		for (int i = 0; i < reportsList.size(); i++) {
			ModuleFlowDetBean flow = (ModuleFlowDetBean) reportsList.get(i);
			MOperation mo = (MOperation) getLoginBean(request)
					.getOperationMap().get(flow.getLink());
			if (mo == null || (mo != null && mo.query() == false)) {
				reportsList.remove(i);
				i--;
			}
		}
		for (int i = 0; i < statList.size(); i++) {
			ModuleFlowDetBean flow = (ModuleFlowDetBean) statList.get(i);
			MOperation mo = (MOperation) getLoginBean(request)
					.getOperationMap().get(flow.getLink());
			if (mo == null || (mo != null && mo.query() == false)) {
				statList.remove(i);
				i--;
			}
		}
		for (int i = 0; i < detailList.size(); i++) {
			ModuleFlowDetBean flow = (ModuleFlowDetBean) detailList.get(i);
			MOperation mo = (MOperation) getLoginBean(request)
					.getOperationMap().get(flow.getLink());
			if (mo == null || (mo != null && mo.query() == false)) {
				detailList.remove(i);
				i--;
			}
		}
		request.setAttribute("reportList", reportsList);
		request.setAttribute("statList", statList);
		request.setAttribute("detailList", detailList);
		return forward;
	}

}
