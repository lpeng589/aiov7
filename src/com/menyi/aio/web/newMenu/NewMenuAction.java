package com.menyi.aio.web.newMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.*;

public class NewMenuAction extends BaseAction {
	NewMenuMgt mgt = new NewMenuMgt();

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req);
		if (loginBean == null || loginBean.getOperationMap() == null) {
			BaseEnv.log
					.debug("ModuleFlowAction.doAuth() ---------- loginBean or getOperationMap() is null");
			return getForward(req, mapping, "indexPage");
		}
		return null;
	}

	String keyId = "";
	String name = "";

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		keyId = request.getParameter("keyId");
		name = request.getParameter("name");

		int operation = getOperation(request);
		//int operation=Integer.parseInt(request.getParameter("operation"));

		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			if ("update".equals(request.getParameter("flag"))) {
				forward = update(mapping, form, request, response);
				break;
			}
			if ("add".equals(request.getParameter("flag"))) {
				forward = add(mapping, form, request, response);
				break;
			}
		case OperationConst.OP_ADD_PREPARE:
			name = GlobalsTool.toChinseChar(name);
			forward = query(mapping, form, request, response);
			break;

		default:
			name = GlobalsTool.toChinseChar(name);
			forward = goToQuery(mapping, form, request, response);
			break;
		}
		request.setAttribute("name", name);
		request.setAttribute("keyId", keyId);
		return forward;
	}

	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Result menuList = mgt.getQuery(keyId);
		List list = (List) menuList.getRetVal();
		if (list != null && list.size() > 0) {
			String tdName = list.get(1).toString();

			if (!tdName.trim().equals("")) {
				String tdNameImgs[] = tdName.split(";");

				HashMap<String, String> tdImgsList = new HashMap<String, String>();
				HashMap<String, String> tdImgLinkList = new HashMap<String, String>();
				HashMap<String, String> tdDis = new HashMap<String, String>();
				HashMap<String, String> tdOpType = new HashMap<String, String>();
				String tdNames[] = null;
				String classCodes = "";

				for (int i = 0; i < tdNameImgs.length; i++) {
					tdNames = tdNameImgs[i].split(",");
					tdImgsList.put(tdNames[0], tdNames[1]);
					tdDis.put(tdNames[0], tdNames[3]);
					tdOpType.put(tdNames[0], tdNames[4]);
					if (tdNames.length > 2) {
						if (tdNames[2] != null && !("").equals(tdNames[2])) {
							tdImgLinkList.put(tdNames[0], tdNames[2]);
							classCodes = classCodes + "'" + tdNames[2] + "',";
						}
					}
				}
				if (classCodes != null && !("").equals(classCodes)) {
					int index = classCodes.lastIndexOf(",");
					classCodes = classCodes.substring(0, index);
					Result moduHash = mgt.getLinkQuery(classCodes);
					Map moduH = (HashMap) moduHash.getRetVal();
					request.setAttribute("moduHash", moduH);
				}
				request.setAttribute("tdDis", tdDis);
				request.setAttribute("tdImgsList", tdImgsList);
				request.setAttribute("tdImgLinkList", tdImgLinkList);
				request.setAttribute("tdOpType", tdOpType);
			}
			request.setAttribute("flag", "update");
		} else {
			request.setAttribute("flag", "add");
		}
		return getForward(request, mapping, "add");
	}

	public HttpServletRequest addAndUpAfter(HttpServletRequest request,
			String tdName) {

		if (!("").equals(tdName)) {
			String tdNameImgs[] = tdName.split(";");
			HashMap<String, String> tdImgsList = new HashMap<String, String>();
			HashMap<String, String> tdImgLinkList = new HashMap<String, String>();
			HashMap<String, String> tdDis = new HashMap<String, String>();
			HashMap<String, String> tdOpType = new HashMap<String, String>();
			String tdNames[] = null;
			String classCodes = "";
			for (int i = 0; i < tdNameImgs.length; i++) {
				tdNames = tdNameImgs[i].split(",");
				tdImgsList.put(tdNames[0], tdNames[1]);
				tdDis.put(tdNames[0], tdNames[3]);
				tdOpType.put(tdNames[0], tdNames[4]);
				if (tdNames.length > 2) {
					if (tdNames[2] != null && !("").equals(tdNames[2])) {
						tdImgLinkList.put(tdNames[0], tdNames[2]);
						classCodes = classCodes + "'" + tdNames[2] + "',";
					}
				}
			}
			if (classCodes != null && !("").equals(classCodes)) {
				int index = classCodes.lastIndexOf(",");
				classCodes = classCodes.substring(0, index);
				Result moduHash = mgt.getLinkQuery(classCodes);
				Map moduH = (HashMap) moduHash.getRetVal();
				request.setAttribute("moduHash", moduH);
			}

			request.setAttribute("tdDis", tdDis);
			request.setAttribute("tdImgsList", tdImgsList);
			request.setAttribute("tdImgsList", tdImgsList);
			request.setAttribute("tdImgLinkList", tdImgLinkList);
		}
		request.setAttribute("flag", "update");
		return request;
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tdName = request.getParameter("tdName");

		Result upCode = mgt.updateMenu(tdName, keyId);
		if (upCode.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request = this.addAndUpAfter(request, tdName);
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
					name+"修改图形界面", "tblModuleFlow", "模块导航设置","");
		}

		return query(mapping, form, request, response);
	}

	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String tdName = request.getParameter("tdName");
		Result addCode = mgt.addMenu(tdName, "1", keyId);
		if (addCode.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request = this.addAndUpAfter(request, tdName);
			name = request.getParameter("name");
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
					name+"添加图形界面", "tblModuleFlow", "模块导航设置","");
		} 
		return query(mapping, form, request, response);
	}

	public ActionForward goToQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		
		LoginBean login = getLoginBean(request);
		String locale = getLocale(request).toString();
		
		/* 我的桌面特殊处理 */
		if(keyId!=null && "desk".equals(keyId)){
			String moduleType = getParameter("moduleType", request);
			List<Object> Menus = mgt.getModuleMenu(getLocale(request).toString(),moduleType);
			request.setAttribute("MenuList", Menus);
			request.setAttribute("keyId", "desk");
			request.setAttribute("moduleType", "moduleType");
			request.setAttribute("defSys", moduleType);
			return getForward(request, mapping, "showMenu");
		}
		
		
		String menuType = request.getParameter("menuType");
		String moduleType = request.getParameter("moduleType");
		if(menuType != null && "newMenu".equals(menuType)){
			//从新版菜单进入时，只传了name不存在keyId，从数据库查询keyId
			Result rs = mgt.getMenuKeyId(name, this.getLocale(request).toString(),moduleType);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//查询成功
				keyId = String.valueOf(rs.retVal);
			}
			if(keyId == null || "".equals(keyId)){
				EchoMessage.error().add("查询不到对应的模块！").setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		
		
		Result menuList = mgt.getQuery(keyId);
		List list = (List) menuList.getRetVal();
		if (list != null && list.size() > 0) {
			String tdName = list.get(1).toString();
			if (!tdName.trim().equals("")) {
				String tdNameImgs[] = tdName.split(";");
				HashMap<String, String> tdImgsList = new HashMap<String, String>();
				HashMap<String, String> tdImgLinkList = new HashMap<String, String>();
				HashMap<String, String> tdDis = new HashMap<String, String>();
				HashMap<String, String> tdOpType = new HashMap<String, String>();
				String tdNames[] = null;
				String classCodes = "";
				for (int i = 0; i < tdNameImgs.length; i++) {
					tdNames = tdNameImgs[i].split(",");
					tdImgsList.put(tdNames[0], tdNames[1]);
					tdDis.put(tdNames[0], tdNames[3]);
					tdOpType.put(tdNames[0], tdNames[4]);
					if (tdNames.length > 2) {
						if (tdNames[2] != null && !("").equals(tdNames[2])) {
							tdImgLinkList.put(tdNames[0], tdNames[2]);
							classCodes = classCodes + "'" + tdNames[2] + "',";
						}
					}
				}
				if (classCodes != null && !("").equals(classCodes)) {
					int index = classCodes.lastIndexOf(",");
					classCodes = classCodes.substring(0, index);
					Result moduHash = mgt.getLinkQuery(classCodes);
					Map moduH = (HashMap) moduHash.getRetVal();

					request.setAttribute("moduHash", moduH);
				}
				
				request.setAttribute("tdDis", tdDis);
				request.setAttribute("tdOpType", tdOpType);
				request.setAttribute("tdImgsList", tdImgsList);
				request.setAttribute("tdImgLinkList", tdImgLinkList);
			}
		} else {
			request.setAttribute("flag", "add");
		}
		
		//获取报表
		Result result = mgt.getReportList(keyId,locale,login);
		if (ErrorCanst.DEFAULT_SUCCESS == result.retCode) {
			request.setAttribute("reportList", result.retVal);
		}

		//获取功能列表
		result = mgt.getFunList(keyId,locale,login);
		if (ErrorCanst.DEFAULT_SUCCESS == result.retCode) {
			request.setAttribute("funList", result.retVal);
		}

		//获取所有的导航菜单
		List<Object> Menus = mgt.getAllMenu(getLocale(request).toString(),keyId);
		request.setAttribute("MenuList", Menus);

		request.setAttribute("keyId", keyId);
		return getForward(request, mapping, "showMenu");
	}

}
