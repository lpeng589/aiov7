package com.menyi.aio.web.role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.bean.RightType;
import com.menyi.aio.bean.RoleBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import com.menyi.aio.bean.RoleModuleBean;
import java.text.SimpleDateFormat;
import com.menyi.aio.bean.RoleScopeBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;

import java.io.FileNotFoundException;
import java.net.URLDecoder;

import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
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
public class RoleAction extends MgtBaseAction {

	private static final int Object = 0;

	private static final int ArrayList = 0;

	SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

	SimpleDateFormat sdf14 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	RoleMgt mgt = new RoleMgt();

	public RoleAction() {
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
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_SCOPE_RIGHT_ADD_PREPARE:
			String type=this.getParameter("type", request);
			if("4".equals(type) || "6".equals(type)){
				forward = scopeAddFieldPrepare(mapping, form, request, response);
			}else{
				forward = scopeAddPrepare(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			String updateType = this.getParameter("updateType", request);
			if (("updateSingleRole").equals(updateType)) //角色分配，单个角色
				forward = updateSingleRole(mapping, form, request, response);
			else if (("updateRoleModule").equals(updateType)) { //在系统模块中，可以多选角色赋权限
				forward = updateRoleModule(mapping, form, request, response);
			} else
				forward = update(mapping, form, request, response);

			break;
		case OperationConst.OP_DELETE:

			forward = delete(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			String showtype = this.getParameter("showType", request);
			if ("showUser".equals(showtype))
				forward = showUser(mapping, request, response);
			else
				forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_SCOPE_RIGHT_QUERY:
			String method = request.getParameter("method");

			if ("scopeMain".equals(method))
				forward = scopeMain(mapping, form, request, response); //进入具体某个系统(ERP、CRM、HR)的某个高级权限设置的页面
			
			
			break;
		case OperationConst.OP_SCOPE_RIGHT_ADD:
			String addType = this.getParameter("addType", request);
			if ("getScope".equals(addType)) { //获取选中的表的权限
				forward = getScope(mapping, form, request, response);
			} else
				forward = scopeAdd(mapping, form, request, response);
			break;
		case OperationConst.OP_SCOPE_RIGHT_DELETE:
			forward = scopeDel(mapping, form, request, response);
			break;
		case OperationConst.OP_SCOPE_RIGHT_UPDATE_PREPARE:
			forward = scopeUpdatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_SCOPE_RIGHT_UPDATE:
			forward = scopeUpdate(mapping, form, request, response);
			break;
		case OperationConst.OP_MODULE_RIGHT_PREPARE:
			forward = modulePrepare(mapping, form, request, response);
			break;	
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	
	
	/**
	 * 模塊權限設置時，要按分類顯示模塊
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward modulePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = null;
		String roleId = request.getParameter("keyId");
		Result roleModules;
		LoginBean lgBean = this.getLoginBean(request);
		if (roleId == null || roleId.length() == 0) {
			roleModules = new Result();
			roleModules.setRetVal(new ArrayList());
		} else {
			roleModules = mgt.queryRoleModuleByRoleid(roleId, lgBean.getId());
		}
		
		String moduleId = request.getParameter("moduleId");		
		Result modulesList = mgt.queryModuleById(moduleId);

		if (roleModules.retCode == ErrorCanst.DEFAULT_SUCCESS && modulesList.retCode == ErrorCanst.DEFAULT_SUCCESS) {

			HashMap uMap = new HashMap();
			for (Object o : (ArrayList) roleModules.retVal) {
				RoleModuleBean umb = (RoleModuleBean) o;
				uMap.put(umb.getModuleOpBean().getModuleOpId(), umb.getModuleOpBean().getModuleOpId());
			}
			request.setAttribute("roleModules", uMap);
			request.setAttribute("row", modulesList.retVal);
			request.setAttribute("roleId", roleId);
			forward = getForward(request, mapping, "roleModuleAdd");

		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
		}

		return forward;
	}

	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		Result rs;
		
		request.setAttribute("allModule", BaseEnv.allModule);
		request.setAttribute("operation", OperationConst.OP_ADD);
			
		return getForward(request, mapping, "roleAdminUpdate");
	}

	/**
	 * 在模块管理中将选中的模块应用于指定的角色
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updateRoleModule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String[] roleIds = this.getParameters("roleIds", request);//取所有角色
		String moduleIds = request.getParameter("moduleIds");
		//取选择的所有操作项
		ArrayList list = new ArrayList();
		if (roleIds != null) {
			for (String role : roleIds) {
				String[] roleOps = this.getParameters("operationId_" + role, request);
				if (roleOps != null) {
					for (String op : roleOps) {
						list.add(new String[] { role, op });
					}
				}
			}
		}
		Result rs = mgt.updateRoleModuleByModule(moduleIds, list);

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.success().add("授权成功").setBackUrl("/RoleQueryAction.do?moduleId=" + moduleIds).setAlertRequest(request);
		} else {
			EchoMessage.error().add("授权失败").setClose().setAlertRequest(request);
		}

		return getForward(request, mapping, "alert");
	}



	/**
	 * 获取选中的表的权限
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward getScope(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String roleId = this.getParameter("roleId", request);
		String moduleId = this.getParameter("moduleId", request);
		String oblist = mgt.getScope(roleId, moduleId);
		request.setAttribute("msg", oblist);
		return getForward(request, mapping, "blank");
	}

	

	/**
	 * 单个角色用户修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updateSingleRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleId = this.getParameter("roleId", request); //角色Id
		String userIds = this.getParameter("allUser", request); //角色现在拥有的用户
		String oldUser = this.getParameter("oldUser", request); //角色原来拥有的用户
		boolean flag = false;
		if ("".equals(userIds)) { //撤销所有用户
			flag = mgt.updateSingleRole(oldUser, roleId, "del");
			if (!flag) {
				log.error("撤销所有用户失败!");
			}
		} else {
			String[] oldList = oldUser.split(",");
			String[] newList = userIds.split(",");
			String addIds = ""; //添加的用户
			String delIds = ""; //删除的用户
			for (String news : newList) {
				if (!("," + oldUser).contains("," + news + ",")) {
					if (!news.equals("1"))
						addIds += news + ",";
				}
			}
			for (String old : oldList) {
				if (!("," + userIds).contains("," + old + ",")) {
					delIds += old + ",";
				}
			}
			if (!"".equals(delIds)) {
				flag = mgt.updateSingleRole(delIds, roleId, "del");
				if (!flag) {
					log.debug("撤销角色用户失败!");
				}
			}
			if (!"".equals(addIds)) {
				flag = mgt.updateSingleRole(addIds, roleId, "add");
				if (!flag) {
					log.debug("添加用户失败!");
				}
			}
		}
		if (flag) { 
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
			//记录系统日志
			if ("".equals(userIds)) {
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
					"撤销所有用户", "tblRole", "权限分组","");
			}else{
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
						"分配用户", "tblRole", "权限分组","");
			}
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");

	}

	/**
	 * 根据角色ID获取使用该角色的用户
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward showUser(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String roleId = this.getParameter("keyId", request);
		List<Object[]> ebean = (List<Object[]>) mgt.getUser(roleId).retVal;
		String userStr = "";
		String allUserId = "";
		for (int i = 0; i < ebean.size(); i++) {
			Object[] e = ebean.get(i);
			allUserId += e[0].toString() + ",";
			userStr += "<li>" + e[1] + "<img class='delImg' name='" + e[0] + "'  src='/style/images/del.gif' alt='撤销用户'/></li>";
		}

		if (userStr.length() == 0)
			userStr = "";
		request.setAttribute("allUser", allUserId);
		request.setAttribute("roleId", roleId);
		request.setAttribute("msg", userStr);
		return getForward(request, mapping, "showRoleUser");
	}
	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward scopeAddPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String type=this.getParameter("type", request);
		request.setAttribute("type", type);
		String desys=this.getParameter("desys", request);
    	request.setAttribute("desys", desys);
    	String fromUser = request.getParameter("fromUser");
		request.setAttribute("fromUser", fromUser);
    	
		ActionForward forward = null;
		String roleId = request.getParameter("roleId");
		if (roleId == null || roleId.length() == 0) {

			EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		String roleName = request.getParameter("roleName");
		roleName = new String(roleName.getBytes("iso-8859-1"), "UTF-8");
		request.setAttribute("roleName", roleName);
		LoginBean lgBean = this.getLoginBean(request); 
		Result roleModules = mgt.queryRoleModuleByRoleid(roleId, lgBean.getId());
		
		//分类资料tableName记录分类资料的名字
		String tableName = request.getParameter("tableName");
		request.setAttribute("tableName", tableName);
		if("0".equals(type)){
			//设置分类资料的名字
			String tableDisplay = tableName;
			ArrayList allTables = (ArrayList) request.getSession()
				.getServletContext().getAttribute("scopeCata");
			for (int i = 0; i < allTables.size(); i++) {
				Object[] cTab = (Object[]) allTables.get(i);
				if (cTab[0].equals(tableName)) {
					tableDisplay = ((KRLanguage) cTab[3]).get(this.getLocale(
							request).toString());
				}
			}
			request.setAttribute("tableDisplay", tableDisplay);
		}

		if (roleModules.retCode == ErrorCanst.DEFAULT_SUCCESS ) {
			HashMap uMap = new HashMap();
			for (Object o : (ArrayList) roleModules.retVal) {
				RoleModuleBean umb = (RoleModuleBean) o;
				uMap.put(umb.getModuleOpBean().getId(), umb.getModuleOpBean().getModuleOpId());
			}
			request.setAttribute("roleModules", uMap);		
			
			
			HashMap modulesMap = new HashMap();
			for(ModuleBean mb:BaseEnv.allModule){
				modulesMap.put(mb.getId().trim(), mgt.queryModuleById(mb.getId()).retVal)  ;
			}
			
			request.setAttribute("allModule", BaseEnv.allModule);			
			request.setAttribute("modulesMap", modulesMap);  
			
			request.setAttribute("roleId", roleId);
			
			String forwardName = "roleScopePubMgt";
			if("5".equals(type)||"1".equals(type)||"0".equals(type)){
				forwardName = "roleScopePubMgt";
			}
			forward = getForward(request, mapping, forwardName);

		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
		}

		return forward;
	}
	
	/**
	 * 专门针对隐藏字段和字段范围值设置
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward scopeAddFieldPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String type=this.getParameter("type", request);
		request.setAttribute("type", type);
		String fromUser = request.getParameter("fromUser");
		request.setAttribute("fromUser", fromUser);
		    	
		ActionForward forward = null;
		String roleId = request.getParameter("roleId");
		if (roleId == null || roleId.length() == 0) {

			EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		String roleName = request.getParameter("roleName");
		roleName = new String(roleName.getBytes("iso-8859-1"), "UTF-8");
		request.setAttribute("roleName", roleName);
		LoginBean lgBean = this.getLoginBean(request);
		String tableName = request.getParameter("tableName");
		String locale = this.getLocale(request).toString();
		if (tableName == null || tableName.length() ==0) {
			//zNodes=[{ id:"00006", pId:0, name:"华北客户"},{ id:"0000600003", pId:"00006", name:"北京客户"},{ id:"00007", pId:0, name:"华东客户"}];
			
			StringBuffer sb = new StringBuffer();
			boolean hasData =mgt.queryModuleType(sb,locale);
			
			
			String zNodes=sb.toString();
			if(zNodes.length() >0){
				zNodes = zNodes.substring(1);
			}
			zNodes="["+zNodes+"]";
			request.setAttribute("zNodes", zNodes);			
			
			
			request.setAttribute("roleId", roleId);			
			String forwardName = "roleScopeAdd_Field";
			forward = getForward(request, mapping, forwardName);
		} else if(!"NO".equals(tableName)){
			//进入显示字段界面
			String tbtype = request.getParameter("tbtype");
			request.setAttribute("tbtype", tbtype);
			
			String tableDisplay = request.getParameter("tableDisplay");
			tableDisplay = new String(tableDisplay.getBytes("ISO8859-1"),"UTF-8");
			request.setAttribute("tableDisplay", tableDisplay);
			
			ArrayList<String[]> list = new ArrayList<String[]>();
			
			//当单据列控制且为单据时，要读取单据的所有表字段来控件，
			//当查看范围值权限时，只可能对报表，而不需要查表字段
			if("TABLE".equals(tbtype) && "4".equals(type)){
				
				//这是主表字段
				DBTableInfoBean mtb= GlobalsTool.getTableInfoBean(tableName);
				for(DBFieldInfoBean fb:mtb.getFieldInfos()){
					//$row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime" && "$row.fieldName"!="moduleType"
					if(fb.getInputType() != 100 && !fb.getFieldName().equals("id") && fb.getInputType() != 3 && fb.getInputType() != 2 ){
						list.add(new String[]{mtb.getTableName(),fb.getFieldName(),fb.getDisplay().get(locale)});
					}
				}
				for(DBTableInfoBean ctb :DDLOperation.getChildTables(tableName, BaseEnv.tableInfos)){
					for(DBFieldInfoBean fb:ctb.getFieldInfos()){
						if(fb.getInputType() != 100 && !fb.getFieldName().equals("id") && fb.getInputType() != 3 && fb.getInputType() != 2 && 
								!fb.getFieldName().equals("f_ref") ){
							list.add(new String[]{ctb.getTableName(),fb.getFieldName(),ctb.getDisplay().get(locale)+"_"+fb.getDisplay().get(locale)});
						}
					}
				}
			}else if("6".equals(type)){
				//查看范围值控制，暂时只控件，时间，数值，枚举，下拉选择类
				ReportSetMgt rsmgt = new ReportSetMgt();
				ReportsBean reportSetBean = (ReportsBean) rsmgt.getReportSetInfo(tableName, locale).getRetVal();
				DefineReportBean defBean = DefineReportBean.parse(reportSetBean.getSQLFileName(), GlobalsTool.getLocale(request).toString(),lgBean.getId());
				ArrayList<ReportField> disFields = defBean.getDisFields2();
				for(ReportField rf:disFields){
					if(true){
						
						String enumStr="";
						if(rf.getInputType()==DBFieldInfoBean.INPUT_ENUMERATE){
							//传递枚举值
							List el = GlobalsTool.getEnumerationItems(rf.getPopUpName(), locale);
							for(int i=0;el != null && i<el.size();i++){
								KeyPair kp = (KeyPair)el.get(i);
								enumStr +=kp.getValue()+"_;_"+kp.getName()+"_:_";
							}
						}
						if(rf.getInputType()==DBFieldInfoBean.INPUT_DOWNLOAD_SELECT){
							//传递枚举值
							PublicMgt pmgt= new PublicMgt();
							enumStr  = pmgt.downSelect(rf.getSubSQL());
						}
						
						list.add(new String[]{tableName,rf.getFieldName(),rf.getDisplay(),rf.getFieldType()+"",enumStr});
					}
					
				}
			}else{
				//单据列控制，显示报表所有字段
				ReportSetMgt rsmgt = new ReportSetMgt();
				ReportsBean reportSetBean = (ReportsBean) rsmgt.getReportSetInfo(tableName, locale).getRetVal();
				DefineReportBean defBean = DefineReportBean.parse(reportSetBean.getSQLFileName(), GlobalsTool.getLocale(request).toString(),lgBean.getId());
				ArrayList<ReportField> disFields = defBean.getDisFields2();
				for(ReportField rf:disFields){
					list.add(new String[]{tableName,rf.getFieldName(),rf.getDisplay()});
					
				}
			}
			request.setAttribute("result", list);
			request.setAttribute("roleId", roleId);			
			String forwardName = "roleScopeAdd_FieldValue";
			forward = getForward(request, mapping, forwardName);
		}else{
			String forwardName = "roleScopeAdd_FieldValue";
			forward = getForward(request, mapping, forwardName);
		}

		return forward;
	}

	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward scopeUpdatePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = null;
		String roleId = request.getParameter("roleId");
		String keyId = request.getParameter("keyId");
		String fromUser = request.getParameter("fromUser");
		request.setAttribute("fromUser", fromUser);
		
		if (roleId == null || roleId.length() == 0) {
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		LoginBean lgBean = this.getLoginBean(request);
		Result roleModules = mgt.queryRoleModuleByRoleid(roleId, lgBean.getId());
		
		String type = request.getParameter("type");
		
		//分类资料tableName记录分类资料的名字
		String tableName = request.getParameter("tableName");
		request.setAttribute("tableName", tableName);
		if("0".equals(type)){
			//设置分类资料的名字
			String tableDisplay = tableName;
			ArrayList allTables = (ArrayList) request.getSession()
				.getServletContext().getAttribute("scopeCata");
			for (int i = 0; i < allTables.size(); i++) {
				Object[] cTab = (Object[]) allTables.get(i);
				if (cTab[0].equals(tableName)) {
					tableDisplay = ((KRLanguage) cTab[3]).get(this.getLocale(
							request).toString());
				}
			}
			request.setAttribute("tableDisplay", tableDisplay);
		}
		
		Result scope = mgt.loadScope(Integer.parseInt(keyId));
		RoleScopeBean roleScopeBean = (RoleScopeBean) scope.retVal;
		//取所有范围权限对应的模块操作ID
		Result moduleScopes = mgt.getScopeModuleByScopeId(roleScopeBean.getId());
		List moduleScopeList = (List)moduleScopes.getRetVal();
		
		
		
		ArrayList scopeValueList = null;
		if (roleScopeBean != null && roleScopeBean.getFlag().equals("0")) {
			//分类资料查询分类资料的名字
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			tableName = roleScopeBean.getTableName();
			if (tableName.equals("tblCompany1") || tableName.equals("tblCompany2") || tableName.equals("tblCompany3")) {
				tableName = "tblCompany";
			}
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);

			String existFields = null;
			boolean isMoreLanguage = false;
			List fields = tableInfo.getFieldInfos();
			for (Object o : fields) {
				DBFieldInfoBean field = (DBFieldInfoBean) o;
				if (field.getFieldSysType() != null && field.getFieldSysType().equals("RowMarker")) {
					if (field.getInputType() == 4) {
						isMoreLanguage = true;
					}
					existFields = field.getFieldName();
					break;
				}
			}
			
			//这里要将要查询的信息传入函数查询
			String[] values = roleScopeBean.getScopeValue().split(";");
			String qStr = "";
			for (int i = 0; i < values.length; i++) {
				qStr += "'"+values[i] + "',";
			}
			if(qStr.length() > 0){
				qStr = qStr.substring(0,qStr.length() -1);
			}

			Result rs3 = mgt.queryScopeValues(existFields, tableName, isMoreLanguage,qStr);

			if (rs3.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				scopeValueList = new ArrayList();
				HashMap map = (HashMap) rs3.getRetVal();
				String[] vals = roleScopeBean.getScopeValue().split(";");
				for (int i = 0; i < vals.length; i++) {
					Object obj = map.get(vals[i]);
					if (obj != null) {
						KeyPair kp = new KeyPair();
						kp.setName(obj.toString());
						kp.setValue(vals[i]);
						scopeValueList.add(kp);
					}
				}
			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
		} 

		if (roleScopeBean != null && (roleScopeBean.getFlag().equals("5") || roleScopeBean.getFlag().equals("1") || roleScopeBean.getFlag().equals("0"))) {
			
			List<String> selectModuleIds = (List<String>) mgt.getModuleIdsByScope(roleScopeBean.getId() + "").getRetVal();
			request.setAttribute("selectModuleIds", selectModuleIds);
			
			
			HashMap modulesMap = new HashMap();
			for(ModuleBean mb:BaseEnv.allModule){
				modulesMap.put(mb.getId().trim(), mgt.queryModuleById(mb.getId()).retVal)  ;
			}
			
			request.setAttribute("allModule", BaseEnv.allModule);			
			request.setAttribute("modulesMap", modulesMap);  
			
			
			Result nameRs= new Result();
			if(roleScopeBean.getFlag().equals("5")){
				nameRs = mgt.getDeptName(roleScopeBean.getScopeValue());
			}else if(roleScopeBean.getFlag().equals("1")){
				nameRs = mgt.getUserName(roleScopeBean.getScopeValue());
			}
			if (roleScopeBean.getFlag().equals("5") || roleScopeBean.getFlag().equals("1")) {
				if (nameRs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					scopeValueList = (ArrayList) nameRs.getRetVal();
				} else {
					EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}
		}

		if (roleModules.retCode == ErrorCanst.DEFAULT_SUCCESS && scope.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			HashMap uMap = new HashMap();
			for (Object o : (ArrayList) roleModules.retVal) {
				RoleModuleBean umb = (RoleModuleBean) o;
				uMap.put(umb.getModuleOpBean().getId(), umb.getModuleOpBean().getModuleOpId());
			}
			HashMap scopeMap = new HashMap();

			for (Object o : moduleScopeList) {
				
				scopeMap.put(o, o);
			}
			request.setAttribute("scopeValueList", scopeValueList);
			request.setAttribute("roleModules", uMap);
			request.setAttribute("roleId", roleId);
			request.setAttribute("scopeBean", roleScopeBean);
			request.setAttribute("scopeRoleModules", scopeMap);
			request.setAttribute("roleScopeId", keyId);
			String roleName = request.getParameter("roleName");
			roleName = new String(roleName.getBytes("iso-8859-1"), "UTF-8");
			request.setAttribute("roleName", roleName);
			request.setAttribute("type", request.getParameter("type"));
			request.setAttribute("desys", request.getParameter("desys"));
			
			forward = getForward(request, mapping, "roleScopePubMgt");
			
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
		}

		return forward;
	}

	/**

	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = getForward(request, mapping, "alert");
		RoleForm myForm = (RoleForm) form;
		
		Result rs = mgt.addRole(this.getLoginBean(request).getSunCmpClassCode(), this.getLoginBean(request).getId(), myForm.getRoleName(), 
				myForm.getRoleDesc(), request.getParameterValues("hidField"), request.getParameterValues("hidField1"), 
				request.getParameter("moduleOperations"));

		forward = getForward(request, mapping, "alert");
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (request.getParameter("deliverTo") != null && request.getParameter("deliverTo").length() > 0) {
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl(
						"/RoleAction.do?operation=" + OperationConst.OP_ADD_PREPARE + "&winCurIndex=" + request.getParameter("winCurIndex") + "&defSys=" + request.getParameter("deliverTo"))
						.setAlertRequest(request);
			} else {
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/RoleQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex")).setAlertRequest(request);
			}
			/*添加系统日志*/
			new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), myForm.getRoleName(), "tblRole", this
					.getMessage(request, "system.right.manage"),"");
		}else if (rs.retCode == ErrorCanst.NULL_OBJECT_ERROR) {
			EchoMessage.error().add("用户或分组不存在").setAlertRequest(request);				
		}else if (rs.retCode == ErrorCanst.MULTI_VALUE_ERROR) {
			EchoMessage.error().add("分组名称重复").setAlertRequest(request);				
		}else {
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return forward;
	}

	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		String nstr = request.getParameter("keyId");
		request.setAttribute("roleId", nstr);
		String fromUser = request.getParameter("fromUser");
		request.setAttribute("fromUser", fromUser);
		String updatePage = request.getParameter("updatePage"); //用户详情界面
		request.setAttribute("updatePage", updatePage);
		if (nstr != null && nstr.length() != 0) {
			Result rs;
			if(!"true".equals(fromUser)){ //如果是角色，要查询角色。
				rs = mgt.detail(nstr);
			}else{
				//查询职员，界面要用到 hiddenField ，隐藏成本价一部分放在这里
				rs = mgt.detailEmployee(nstr);				
			}
			
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {					
				request.setAttribute("result", rs.retVal);
				request.setAttribute("allModule", BaseEnv.allModule);
				request.setAttribute("operation", OperationConst.OP_UPDATE);
				
			} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
				EchoMessage.error().add(getMessage(request, "common.error.nodata")).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.error")).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
			
			//根据用户角色的id在tblRightType表中查询该用户的该用户的权限类型(允许负库存过账,允许超订单数出库,允许超订单数入库,允许少于成本价出库)			
			HashMap map = (HashMap) mgt.queryRightTypeById(nstr).retVal;
			request.setAttribute("rightType", map);
			
			forward = getForward(request, mapping, "roleAdminUpdate");
		}
		return forward;
	}

	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = getForward(request, mapping, "alert");
		RoleForm myForm = (RoleForm) form;
		request.setAttribute("roleId", myForm.getId());
		Result rs = new Result();

		String copy = request.getParameter("copy");
		if (copy != null && copy.equals("copy")) {
			String sourceRoleId = request.getParameter("sourceRoleId");
			String roleName = request.getParameter("saveName");

			String roleDesc = request.getParameter("saveDesc");
			roleName = new String(roleName.getBytes("ISO8859-1"), "UTF-8");
			roleDesc = new String(roleDesc.getBytes("ISO8859-1"), "UTF-8");
			String roleId = IDGenerater.getId();
			RoleBean bean = new RoleBean();
			bean.setId(roleId);
			bean.setRoleName(roleName);
			bean.setRoleDesc(roleDesc);
			bean.setCreateBy(getLoginBean(request).getId());
			bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			bean.setLastUpdateBy(getLoginBean(request).getId());
			bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			bean.setSCompanyID(getLoginBean(request).getSunCmpClassCode());

			rs = mgt.add(bean, sourceRoleId);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				EchoMessage.success().add("另存成功").setBackUrl("/RoleQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex")).setAlertRequest(request);
				new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), bean.getRoleName()+"另存", "tblRole", this
						.getMessage(request, "system.right.manage"),"");
			} else {
				EchoMessage.error().add(getMessage(request, "另存失败")).setAlertRequest(request);
			}
		} else {
			
			rs = mgt.updateRole(request.getParameter("fromUser"), this.getLoginBean(request).getId(), myForm.getRoleName(), 
					myForm.getRoleDesc(), request.getParameterValues("hidField"), request.getParameterValues("hidField1"), 
					myForm.getId(), request.getParameter("moduleOperations"));

			forward = getForward(request, mapping, "alert");
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if ("true".equals(request.getParameter("fromUser"))){
					if("true".equals(request.getParameter("updatePage"))){
						EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setClosePopWin("setScopediv").setAlertRequest(request);
					}else{
						EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/UserQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex"))
							.setAlertRequest(request);
					}
					new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), myForm.getRoleName()+"修改权限", "tblEmployee", "系统用户","");
				}else{
					EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/RoleQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex"))
					.setAlertRequest(request);
					new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), myForm.getRoleName()+"修改权限", "tblRole", this
						.getMessage(request, "system.right.manage"),"");
				}
			}else if (rs.retCode == ErrorCanst.NULL_OBJECT_ERROR) {
				EchoMessage.error().add("用户或分组不存在").setAlertRequest(request);				
			}else if (rs.retCode == ErrorCanst.MULTI_VALUE_ERROR) {
				EchoMessage.error().add("分组名称重复").setAlertRequest(request);				
			}else {
				EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
			}
		}
		return forward;
	}

	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		String[] roleIds = request.getParameterValues("keyId");
		if (roleIds != null && roleIds.length != 0) {
			Result rs = mgt.queryUserByRole(roleIds);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if (rs.retVal != null && Integer.parseInt(rs.retVal.toString()) > 0) {
					EchoMessage.error().add(getMessage(request, "common.msg.delRoleExist")).setRequest(request);
					return forward = getForward(request, mapping, "message");
				}
			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.delError")).setRequest(request);
				return forward = getForward(request, mapping, "message");
			}
			DynDBManager dbmgt = new DynDBManager();
			ArrayList<RoleBean> delList = new ArrayList<RoleBean>();
			for (String roleId : roleIds) {
				Result rst = mgt.detail(roleId);
				if (rst != null) {
					RoleBean bean = (RoleBean) rst.getRetVal();
					delList.add(bean);
					

				}
			}

			rs = mgt.delete(roleIds);

			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				for (String roleId : roleIds) {
					//mgt.deleteUserSunCompanyByRoleId(roleId) ;
					mgt.deleteRightType(roleId);
				}
				for (RoleBean bean : delList) {
					new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), bean.getRoleName(), "tblRole",
							this.getMessage(request, "system.right.manage"),"");
				}
				
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/RoleQueryAction.do").setAlertRequest(request);
				forward = getForward(request, mapping, "message");
				/* request.setAttribute("result", rs.retVal);
				 forward = query(mapping, form, request, response);*/
			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.delError")).setRequest(request);
				forward = getForward(request, mapping, "message");
			}
		}
		return forward;

	}

	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward scopeDel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		String roleId = request.getParameter("roleId");
		String[] roleScopeIds = request.getParameterValues("keyId");
		String type = this.getParameter("type", request);
		String desys = this.getParameter("desys", request);
		String roleName = this.getParameter("roleName", request);
		request.setAttribute("roleName", roleName);
		String tableName = this.getParameter("tableName", request);
		request.setAttribute("tableName", tableName);

		if (roleScopeIds != null && roleScopeIds.length != 0) {

			Result rs = mgt.deleteScope(roleScopeIds);

			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {

				if("true".equals(request.getParameter("fromUser"))){
					EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl(
							"/RoleAction.do?operation=31&method=scopeMain&desys=" + desys + "&roleId=" + roleId + "&roleName=" + roleName + "&type=" + type+ "&tableName=" + tableName).setAlertRequest(request);
					
					
					new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),  roleName+"删除管辖范围权限",
						"tblEmployee", "系统用户","");
				}else{
					EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl(
							"/RoleAction.do?operation=31&method=scopeMain&desys=" + desys + "&roleId=" + roleId + "&roleName=" + roleName + "&type=" + type+ "&tableName=" + tableName).setAlertRequest(request);
					
					
					new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),  roleName+"删除管辖范围权限",
						"tblRole", this.getMessage(request, "system.right.manage"),"");
				}
				request.setAttribute("dealAsyn", "refresh");	
				return getForward(request, mapping, "message");

			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.delError")).setBackUrl(
						"/RoleAction.do?operation=31&method=scopeMain&desys=" + desys + "&roleId=" + roleId + "&roleName=" + roleName + "&type=" + type+ "&tableName=" + tableName).setRequest(request);
				forward = getForward(request, mapping, "message");
			}
		}
		return forward;

	}


	/**模块高级权限页面
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward scopeMain(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleId = request.getParameter("roleId");
		String roleName = request.getParameter("roleName");
		if ("GET".equals(request.getMethod())) {
			roleName = new String(roleName.getBytes("iso-8859-1"), "UTF-8");
		}
		if (request.getAttribute("roleName") == null || request.getAttribute("roleName").toString().length() == 0) {
			request.setAttribute("roleName", roleName);
		}
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		String type = request.getParameter("type") == null ? "1" : request.getParameter("type");
		request.setAttribute("type", type);
		String fromUser = request.getParameter("fromUser");
		request.setAttribute("fromUser", fromUser==null?"":fromUser);
		
		
		//分类资料tableName记录分类资料的名字
		String tableName = request.getParameter("tableName");
		request.setAttribute("tableName", tableName);
		if("0".equals(type)){
			//设置分类资料的名字
			String tableDisplay = tableName;
			ArrayList allTables = (ArrayList) request.getSession()
				.getServletContext().getAttribute("scopeCata");
			for (int i = 0; i < allTables.size(); i++) {
				Object[] cTab = (Object[]) allTables.get(i);
				if (cTab[0].equals(tableName)) {
					tableDisplay = ((KRLanguage) cTab[3]).get(this.getLocale(
							request).toString());
				}
			}
			request.setAttribute("tableDisplay", tableDisplay);
		}

		String desys = request.getParameter("desys");
		request.setAttribute("desys", desys);

		Result rs = mgt.queryScope(roleId, desys, type,tableName);

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			for (Object o : (ArrayList) rs.getRetVal()) {
				Object[] os = (Object[]) o;
				Object moduleField = os[3];
				if ("0".equals(os[1].toString())) { //分类资料
					//根据分类资料对应表的RowMarker字段来显示分类资料信息
					Hashtable maptable = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
					String cataTab = (String) os[2];
					if (cataTab.equals("tblCompany1") || cataTab.equals("tblCompany2") || cataTab.equals("tblCompany3")) {
						cataTab = "tblCompany";
					}
					DBTableInfoBean tableInfo = (DBTableInfoBean) maptable.get(cataTab);

					String existFields = null;
					boolean isMoreLanguage = false;
					List fields = tableInfo.getFieldInfos();
					for (Object f : fields) {
						DBFieldInfoBean field = (DBFieldInfoBean) f;
						if (field.getFieldSysType() != null && field.getFieldSysType().equals("RowMarker")) {
							if (field.getInputType() == 4) {
								isMoreLanguage = true;
							}
							existFields = field.getFieldName();
							break;
						}
					}
					//这里要将要查询的信息传入函数查询
					String[] values = os[4].toString().split(";");
					String qStr = "";
					for (int i = 0; i < values.length; i++) {
						qStr += "'"+values[i] + "',";
					}
					if(qStr.length() > 0){
						qStr = qStr.substring(0,qStr.length() -1);
					}
					Result rs3 = mgt.queryScopeValues(existFields, cataTab, isMoreLanguage,qStr);
					if (rs3.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						String str = "";
						HashMap map = (HashMap) rs3.getRetVal();						
						for (int i = 0; i < values.length; i++) {
							str = str + map.get(values[i]) + ";";
						}
						os[4] = str;
					}
					


				} else if ("5".equals(os[1].toString())) { //部门管辖范围
					//如果os[4]=DEPT 为按部门。ALL为全公司
					if("DEPT".equals(os[4])){
						request.setAttribute("deptRightType", "1");
						request.setAttribute("deptRightTypeDelete", os[7]);
						request.setAttribute("deptRightTypeUpdate", os[8]);						
					}else if("ALL".equals(os[4])){
						request.setAttribute("deptRightType", "2");
						request.setAttribute("deptRightTypeDelete", os[7]);
						request.setAttribute("deptRightTypeUpdate", os[8]);
					}else {
						request.setAttribute("deptRightType", "3");				
					
						Result userNameRs = null;
						//os[4]即scopeValue，保存的是，用户ID，或部门classCode,这里翻译成名称
						userNameRs = mgt.getDeptName(os[4].toString());
						if (userNameRs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							String str = "";
							ArrayList scopeValueList = (ArrayList) userNameRs.getRetVal();
							for (Object valueO : scopeValueList) {
								KeyPair kp = (KeyPair) valueO;
								str += kp.getName() + ";";
							}
							os[4] = str;
						} else {
							EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
							return getForward(request, mapping, "message");
						}
					}
					if("1".equals(os[6])){
						os[2]="应用于所有模块";
					}else{
						os[2]="指定模块";
					}
//					if (!os[2].equals("")) {
//						String tabDis = getTableDisplay(request, os[2].toString());
//						if (tabDis.length() > 0) {
//							os[2] = tabDis;
//						}
//					}

				} else if ("1".equals(os[1].toString()) ) { //职员管辖范围
					Result userNameRs = null;
					//os[4]即scopeValue，保存的是，用户ID，或部门classCode,这里翻译成名称
					userNameRs = mgt.getUserName(os[4].toString());
					if (userNameRs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						String str = "";
						ArrayList scopeValueList = (ArrayList) userNameRs.getRetVal();
						for (Object valueO : scopeValueList) {
							KeyPair kp = (KeyPair) valueO;
							str += kp.getName() + ";";
						}
						os[4] = str;
					} else {
						EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
						return getForward(request, mapping, "message");
					}
					if("1".equals(os[6])){
						os[2]="应用于所有模块";
					}else{
						os[2]="指定模块";
					}
//					if (!os[2].equals("")) {
//						String tabDis = getTableDisplay(request, os[2].toString());
//						if (tabDis.length() > 0) {
//							os[2] = tabDis;
//						}
//					}
				} else if ("4".equals(os[1].toString()) || "6".equals(os[1].toString())) {
					//翻译字段名称为中文名
					String tabField = "";
					//如果是查看范围值控制，内容为枚举时，要显示枚举值，
					if(os[12] != null && !"".equals(os[12])){
						os[4] = os[12];
					}

				} 
			}
			
			if("1".equals(request.getAttribute("deptRightType"))|| "2".equals(request.getAttribute("deptRightType"))){
				//-----部门范围权限查看部门和全公司时，不给result值
			}else{
				request.setAttribute("result", rs.retVal);
			}
			request.setAttribute("roleId", roleId);
			if ("5".equals(type)) { //部门范围权限
				return getForward(request, mapping, "roleScopeDeptMain");
			}else if ("1".equals(type)) { //职员范围权限
				return getForward(request, mapping, "roleScopePersonMain");
			}else if ("0".equals(type)) { //职员范围权限
				return getForward(request, mapping, "roleScopeClassMain");
			}else if ("4".equals(type)) { //单据列控制
				return getForward(request, mapping, "roleScopeFieldMain");
			}else if ("6".equals(type)) { //查看字段值范围
				return getForward(request, mapping, "roleScopeFieldValueMain");
			}else { //
				return null;
			}

		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	

	
	/**
	 * 得到表的中文名
	 * @param request
	 * @param tableNames
	 * @return
	 */
	private String getTableDisplay(HttpServletRequest request, String tableNames) {
		String[] allTableName = tableNames.split(";");
		String tableName = "";
		ArrayList allList = (ArrayList)mgt.queryAll().retVal;
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		for (int i = 0; i < allTableName.length; i++) {
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(allTableName[i]);
			if (tableInfo != null) {
				tableName += tableInfo.getDisplay().get(getLocale(request).toString()).toString() + ";";
			} else {
				for (int j = 0; j < allList.size(); j++) {
					ModuleBean bean = (ModuleBean) allList.get(j);						
					if (bean.getLinkAddress().indexOf(allTableName[i])>0) {
						tableName += bean.getModelDisplay().get(request.getLocale().toString()) + ";";
						break;
					}
				}

			}

		}
		return tableName;
	}
	
	
	/**
	 * 获取分类资料的名称
	 * @param request
	 * @param tableName
	 * @return
	 */
	private String getCataTableDisplay(HttpServletRequest request, String tableName) {

		ArrayList allTables = (ArrayList) request.getSession().getServletContext().getAttribute("scopeCata");
		for (int i = 0; i < allTables.size(); i++) {
			Object[] cataTab = (Object[]) allTables.get(i);
			if (cataTab[0].equals(tableName)) {
				tableName = ((KRLanguage) cataTab[3]).get(this.getLocale(request).toString());
			}
		}
		return tableName;
	}

	

	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String moduleId = this.getParameter("moduleId", request);

		RoleSearchForm searchForm = (RoleSearchForm) form;
		if("关键字搜索...".equals(searchForm.getName())){
			searchForm.setName("");
		}
		
		if (null == moduleId) { //从权限分组界面进入
			if (searchForm != null) {
				String sunCmpClassCode = getLoginBean(request).getSunCmpClassCode();
				Result rs = mgt.query(searchForm.getName(), searchForm.getPageNo(), searchForm.getPageSize(), sunCmpClassCode);
				ArrayList list = (ArrayList) rs.getRetVal();
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						Object[] str = (Object[]) list.get(i);
						String language = (String) mgt.queryLanguageById(str[3].toString()).getRetVal();
						str[3] = language;
						//查询每个角色分配的用户信息
						Result urs = mgt.getUser((String)str[0]);
						String us = "";
						if(urs.retCode == ErrorCanst.DEFAULT_SUCCESS && urs.retVal != null){
							List<Object[]> los = (List<Object[]>)urs.retVal;
							for(Object[] os:los){
								us+=os[1]+";";
							}
						}
						str[4] = us;
					}
				}
				Result rss = mgt.queryAllSunCompany();
				String defaultSunCmpName = "";
				for (Object o : (ArrayList) rss.getRetVal()) {
					String[] str = (String[]) o;
					if ("1".equals(str[0])) {
						defaultSunCmpName = str[1];
						break;
					}
				}

				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					request.setAttribute("result", list);
					request.setAttribute("defaultSunCmpName", defaultSunCmpName);
					request.setAttribute("pageBar", pageBar(rs, request));
					request.setAttribute("moduleIds", moduleId);
				} else {
					EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
					return getForward(request, mapping, "message");
				}
			}
			return getForward(request, mapping, "roleAdminlist");
		} else {//从模块管理中，直接赋值给角色时的显示界面
			if (searchForm != null) {
				String sunCmpClassCode = getLoginBean(request).getSunCmpClassCode();
				Result rs = mgt.queryRoleUser(sunCmpClassCode);
				ArrayList list = (ArrayList) rs.getRetVal();

				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					request.setAttribute("result", list);
					request.setAttribute("pageBar", pageBar(rs, request));
					request.setAttribute("moduleIds", moduleId);
				} else {
					EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
					return getForward(request, mapping, "message");
				}
			}
			
			
			Result rs = mgt.queryRoleModuleByModule(moduleId);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				HashMap rop = new HashMap();
				for (Object o : (ArrayList) rs.getRetVal()) {
					String[] str = (String[]) o;
					rop.put(str[0] + "_" + str[1], str[0] + "_" + str[1]);
				}
				
				request.setAttribute("rop", rop);
			}
			rs = mgt.queryModule(moduleId, this.getLocale(request).toString());
			request.setAttribute("moname", rs.retVal);

			rs = mgt.queryModuleOperationByModule(moduleId);
			request.setAttribute("mopeation", rs.retVal);
			return getForward(request, mapping, "showRoleDesc");
		}

	}

	

	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward scopeAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		String scopeFlag = request.getParameter("flag");
		String desys = request.getParameter("desys");
		Result rs = null;
		String roleId = request.getParameter("roleId");
		if ("4".equals(scopeFlag)) { //字段列控制 增加隐藏字段或隐藏字段值
			String[] field = request.getParameterValues("field");	
			String tableDisplay = request.getParameter("tableDisplay");
			ArrayList<RoleScopeBean> beans = new ArrayList<RoleScopeBean>();
			for (int i = 0; i < field.length; i++) {
				String readOnly = request.getParameter("readOnly_"+field[i]);
				String hidden = request.getParameter("hidden_"+field[i]);
				if("1".equals(readOnly) || "1".equals(hidden)){
					String tableN = field[i].substring(0,field[i].indexOf("_;_"));
					String fieldN = field[i].substring(field[i].indexOf("_;_")+3);
					String fieldNd = request.getParameter("name_"+field[i]);					
					
					RoleScopeBean r = new RoleScopeBean();					
					r.setFieldName(fieldN);
					r.setFlag(scopeFlag);
					r.setRoleId(roleId);
					r.setTableName(tableN);
					r.setTableNameDisplay(tableDisplay);
					r.setFieldNameDisplay(fieldNd);
					r.setScopeValue("1".equals(hidden)?"1":"0");
					r.setEscopeValue("1".equals(readOnly)?"1":"0");
					r.setIsAllModules("1");
					String[] mOperations = null;
					beans.add(r);
				}
			}
			rs = mgt.addRoleScope(beans);
		}else if ("6".equals(scopeFlag)) { //查看范围值控制
			String[] field = request.getParameterValues("field");	
			String tableDisplay = request.getParameter("tableDisplay");
			ArrayList<RoleScopeBean> beans = new ArrayList<RoleScopeBean>();
			for (int i = 0; i < field.length; i++) {
				String scopeValue = request.getParameter("scopeValue_"+field[i]);
				String escopeValue = request.getParameter("escopeValue_"+field[i]);
				if((scopeValue != null && !"".equals(scopeValue)) || (escopeValue != null && !"".equals(escopeValue))){
					String tableN = field[i].substring(0,field[i].indexOf("_;_"));
					String fieldN = field[i].substring(field[i].indexOf("_;_")+3);
					String fieldNd = request.getParameter("name_"+field[i]);					
					String typeN = request.getParameter("type_"+field[i]);		
					
					RoleScopeBean r = new RoleScopeBean();					
					r.setFieldName(fieldN);
					r.setFlag(scopeFlag);
					r.setRoleId(roleId);
					r.setTableName(tableN);
					r.setTableNameDisplay(tableDisplay);
					r.setFieldNameDisplay(fieldNd);
					r.setValuetype(typeN);
					r.setIsAllModules("1");
					scopeValue = scopeValue==null?"":scopeValue;
					r.setScopeValueDisplay(scopeValue);
					
					if(scopeValue.indexOf("_;_")>0){
						//如果是枚举值，则会连同枚举名和值一起回传
						r.setScopeValue(scopeValue.substring(0,scopeValue.indexOf("_;_")));
						r.setScopeValueDisplay(scopeValue.substring(scopeValue.indexOf("_;_")+3));
					}else{
						r.setScopeValue(scopeValue==null?"":scopeValue);
					}
					r.setEscopeValue(escopeValue==null?"":escopeValue);
					String[] mOperations = null;					
					beans.add(r);
				}
			}
			rs = mgt.addRoleScope(beans);
		} else {
			String[] moduleOperations = null;
			if (("0".equals(scopeFlag)|| "5".equals(scopeFlag) || "1".equals(scopeFlag)) && "1".equals(request.getParameter("isAllModules")))
				moduleOperations = null;
			else
				moduleOperations = request.getParameterValues("moduleOperations");

			RoleScopeBean roleScope = new RoleScopeBean();
			roleScope.setFieldName(request.getParameter("fieldName"));
			roleScope.setFlag(scopeFlag);
			roleScope.setRoleId(roleId);
			roleScope.setTableName(request.getParameter("tableName"));
			roleScope.setScopeValue(request.getParameter("scopeValue"));
			roleScope.setEscopeValue(request.getParameter("escopeValue"));
			roleScope.setRightDelete("true".equals(request.getParameter("rightDelete"))?1:0);
			roleScope.setRightUpdate("true".equals(request.getParameter("rightUpdate"))?1:0);
			if ("0".equals(scopeFlag)|| "5".equals(scopeFlag)|| "1".equals(scopeFlag)) {
				roleScope.setIsAllModules("1".equals(request.getParameter("isAllModules"))?"1":"0");
			}

			if (scopeFlag.equals("1") || scopeFlag.equals("5")) {
//				String[] moduleIds = request.getParameter("tableName").split(";");
//				String tableName = "";
//				for (int i = 0; i < moduleIds.length; i++) {
//					String tempName = (String) mgt.getTableNameByModuleId(moduleIds[i]).getRetVal();
//					if (tempName != null && !"".equals(tempName)) {
//						tableName += tempName + ";";
//					}
//				}
//				roleScope.setTableName(tableName);
			} else if (scopeFlag.equals("0")) {
				roleScope.setFieldName("classCode");
				roleScope.setIsAddLevel("1".equals(request.getParameter("isAddLevel"))?"1":"0");
			}

			rs = mgt.addRoleScope(roleScope,moduleOperations);
			System.out.println("-----------");
		}
		forward = getForward(request, mapping, "alert");
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			String roleName = request.getParameter("roleName");
			RoleScopeBean rb = (RoleScopeBean)rs.getRetVal();
			request.setAttribute("dealAsyn", "true");	
			
			if("true".equals(request.getParameter("fromUser"))){
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl(
						"/RoleAction.do?operation="+OperationConst.OP_SCOPE_RIGHT_UPDATE_PREPARE+"&keyId="+(rb==null?"":rb.getId())+"&roleId="+roleId+"&roleName="+roleName+"&winCurIndex="
						+ request.getParameter("winCurIndex")).setAlertRequest(request);
				
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),  roleName+"添加范围权限",
					"tblEmployee", "系统用户","");
			}else{
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl(
						"/RoleAction.do?operation="+OperationConst.OP_SCOPE_RIGHT_UPDATE_PREPARE+"&keyId="+(rb==null?"":rb.getId())+"&roleId="+roleId+"&roleName="+roleName+"&winCurIndex="
						+ request.getParameter("winCurIndex")).setAlertRequest(request);
				
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),  roleName+"添加范围权限",
					"tblRole", this.getMessage(request, "system.right.manage"),"");
			}
		}else if (rs.retCode == ErrorCanst.MULTI_VALUE_ERROR) {
			EchoMessage.error().add("添加重复").setAlertRequest(request);
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return forward;
	}
	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward scopeUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		String scopeFlag = request.getParameter("flag");
		String desys = this.getParameter("desys", request);
		String roleId = request.getParameter("roleId");
		String roleName = request.getParameter("roleName");
		RoleScopeBean roleScope = new RoleScopeBean();
		int id=0;
		if(request.getParameter("roleScopeId") != null){
			id = Integer.parseInt(request.getParameter("roleScopeId"));
			roleScope.setId(id);
		}
		roleScope.setFieldName(request.getParameter("fieldName"));
		roleScope.setFlag(scopeFlag);
		roleScope.setRoleId(roleId);
		roleScope.setTableName(request.getParameter("tableName"));
		roleScope.setScopeValue(request.getParameter("scopeValue"));
		roleScope.setEscopeValue(request.getParameter("escopeValue"));
		
		Result rs = new Result();
		
		if("true".equals(request.getParameter("departPubSet"))){
			//设置部门管理范围的快速设置，如查看本部门单据。
			String deptRightType = request.getParameter("deptRightType");
			String deptRightTypeUpdate=request.getParameter("deptRightTypeUpdate"+deptRightType);
			String deptRightTypeDelete=request.getParameter("deptRightTypeDelete"+deptRightType);
			rs = mgt.updateDeptRoleScope(roleId,deptRightType,deptRightTypeUpdate,deptRightTypeDelete);
			
		}else{ 
			//除设置部门管理范围的快速设置以外的所有范围权限的修改。
			String[] moduleOperations = null;
			if (("0".equals(scopeFlag)|| "5".equals(scopeFlag) || "1".equals(scopeFlag) ) && "1".equals(request.getParameter("isAllModules")))
				moduleOperations = null;
			else
				moduleOperations = request.getParameterValues("moduleOperations");
	
			roleScope.setRightDelete("true".equals(request.getParameter("rightDelete"))?1:0);
			roleScope.setRightUpdate("true".equals(request.getParameter("rightUpdate"))?1:0);
			if ("0".equals(scopeFlag)|| "5".equals(scopeFlag)|| "1".equals(scopeFlag)) {
				roleScope.setIsAllModules("1".equals(request.getParameter("isAllModules"))?"1":"0");
			}
	
			if (scopeFlag.equals("1") || scopeFlag.equals("5")) {
				String[] moduleIds = request.getParameter("tableName").split(";");
				String tableName = "";
				for (int i = 0; i < moduleIds.length; i++) {
					String tempName = (String) mgt.getTableNameByModuleId(moduleIds[i]).getRetVal();
					if (tempName != null && !"".equals(tempName)) {
						tableName += tempName + ";";
					}
				}
				roleScope.setTableName(tableName);
			} else if (scopeFlag.equals("0")) {
				roleScope.setFieldName("classCode");
				roleScope.setIsAddLevel("1".equals(request.getParameter("isAddLevel"))?"1":"0");
			}
	
			rs = mgt.updateRoleScope(roleScope,moduleOperations);
		}
		
		

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("dealAsyn", "true");
			if("true".equals(request.getParameter("fromUser"))){
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/RoleAction.do?operation="+OperationConst.OP_SCOPE_RIGHT_UPDATE_PREPARE+"&keyId="+id+"&roleId="+roleId+"&roleName="+roleName+"&winCurIndex="
						+ request.getParameter("winCurIndex")).setAlertRequest(request);
				
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),  roleName+"修改范围权限",
					"tblEmployee", "系统用户","");
			}else{
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/RoleAction.do?operation="+OperationConst.OP_SCOPE_RIGHT_UPDATE_PREPARE+"&keyId="+id+"&roleId="+roleId+"&roleName="+roleName+"&winCurIndex="
						+ request.getParameter("winCurIndex")).setAlertRequest(request);
				
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),  roleName+"修改范围权限",
					"tblRole", this.getMessage(request, "system.right.manage"),"");
			}

			return getForward(request, mapping, "alert");
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.updateFailture")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
	}

}
