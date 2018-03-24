package com.menyi.aio.web.usermanage;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.RoleBean;
import com.menyi.aio.bean.UserModuleBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * Description: <br/>Copyright (C), 2008-2012, Justin.T.Wang <br/>This Program
 * is protected by copyright laws. <br/>Program Name: <br/>Date:
 *
 * @autor Justin.T.Wang yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class UserManageAction extends MgtBaseAction {

	private UserMgt userMgt = new UserMgt();
	private RoleMgt roleMgt = new RoleMgt();

	public UserManageAction() {}
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

		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		request.setAttribute("type", type);
		
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			String copyRight = request.getParameter("copyRight");
			if("true".equals(copyRight)){ //点击界面的复制权限按扭
				forward = copyRight(mapping, form, request, response);
			}else{
				forward = update(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		case OperationConst.OP_SCOPE_RIGHT_ADD_PREPARE:
			forward = beforAddScopPurview(mapping, form, request, response);
			break;
		case OperationConst.OP_SCOPE_RIGHT_ADD:
			forward = scopePurviewAdd(mapping, form, request, response);
			break;
		case OperationConst.OP_MODULE_RIGHT_PREPARE:
			forward = modulePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_MODULE_RIGHT:
			forward = module(mapping, form, request, response);
			break;
		case OperationConst.OP_ERROR_LIST:
			forward = errorList(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PWD_PREPARE:
			forward = updatePwdPrepare(mapping, form, request, response) ;
			break ;
		case OperationConst.OP_UPDATE_PWD:
			forward = updatePwd(mapping, form, request, response) ;
			break ;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	// 为用户增加范围权限前的准备工作
	public ActionForward beforAddScopPurview(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		return null;
	}

	public Result getFieldValue(String tableName, String fieldName, String id) {

		final String field = fieldName;
		final Result rs = new Result();
		final String sql = "select " + fieldName + " from " + tableName
				+ " where id='" + id + "'";
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							BaseEnv.log.debug(sql);
							ResultSet rset = cs.executeQuery(sql);
							if (rset.next()) {
								rs.retVal = rset.getObject(field).toString();
							} else {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}

	// 为用户增加范围权限
	public ActionForward scopePurviewAdd(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// UserForm userForm = (UserForm) form;
		// String sysName = userForm.getSysName();
		// String passWord = userForm.getPassword();
		//
		// String[] peronScopes = request.getParameterValues("employeeID");
		// String[] departmentScopes =
		// request.getParameterValues("departmentID");
		// String[] dataScopes = request.getParameterValues("dataID");
		// UserBean userBean = null;
		// // UserBean userBean = (UserBean) ((ArrayList)
		// (userMgt.queryByNameAndPass(sysName, passWord).getRetVal())).get(0);
		// Result rs = userMgt.queryUserModuleByUserid(userBean.getId());
		// //添加之前先把所有的前纪录删除
		// if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)
		// (rs.retVal)).size() > 0) {
		// for (UserModuleBean userModule : (ArrayList<UserModuleBean>)
		// rs.retVal) {
		// rs = scopeMgt.queryByUserModule(userModule);
		// if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		// List<ScopeBean> scop = (ArrayList<ScopeBean>) rs.retVal;
		// for (ScopeBean scope : scop) {
		// if (scope != null) {
		// scopeMgt.delete(scope);
		// }
		// }
		// }
		// }
		// }
		//
		// LoginBean loginBean = (LoginBean)
		// request.getSession().getAttribute("LoginBean");
		// rs = new Result();
		// rs.retCode = ErrorCanst.DEFAULT_FAILURE;
		// boolean flag = true;
		// if (peronScopes != null && peronScopes.length != 0) {
		// for (String str : peronScopes) {
		// String[] aa = str.split("@");
		// if (aa != null && aa.length != 0 && !aa[0].trim().equals("")) {
		//
		// rs = addScope(aa, userBean, loginBean, "P", "EmpFullName");
		// flag = false;
		// }
		// }
		// }
		// if ((rs.retCode == ErrorCanst.DEFAULT_SUCCESS || flag) &&
		// departmentScopes != null
		// && departmentScopes.length != 0) {
		// for (String str : departmentScopes) {
		// String[] aa = str.split("@");
		// if (aa != null && aa.length != 0 && !aa[0].trim().equals("")) {
		// rs = addScope(aa, userBean, loginBean, "M", "DeptFullName");
		// flag = false;
		// }
		// }
		//
		// }
		// if ((rs.retCode == ErrorCanst.DEFAULT_SUCCESS || flag) && dataScopes
		// != null
		// && dataScopes.length != 0) {
		//
		// for (String str : dataScopes) {
		// String[] aa = str.split("@");
		// if (aa != null && aa.length != 0 && !aa[0].trim().equals("")) {
		// rs = addScope(aa, userBean, loginBean, "D", "id");
		// }
		// }
		// }
		//
		// ActionForward forward = getForward(request, mapping, "alert");
		// if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		// EchoMessage.success().add(getMessage(
		// request, "common.msg.addSuccess"))
		// .setBackUrl("/UserQueryAction.do").
		// setAlertRequest(request);
		//
		// } else {
		//
		// EchoMessage.error().add(getMessage(
		// request, "common.msg.addFailture")).
		// setAlertRequest(request);
		// }
		// return forward;
		return null;
	}

	public Result addScope(String[] scopes, EmployeeBean userBean,
			LoginBean loginBean, String scopeFlag, String fieldName) {

		Result rs = new Result();
		// rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		// for (String string : scopes) {
		// /*24_tblEmployee_employee_1*/
		// if (string != null && !string.equals("")) {
		// String[] strs = string.split("#");
		// String moduleId = strs[0];
		// String tableName = strs[1];
		// String value = strs[3];
		// ModuleBean moduleBean = null;
		// // 先找出该模块
		// Result moduleRs = moduleMgt.load(moduleId);
		// if (moduleRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		// moduleBean = (ModuleBean) moduleRs.retVal;
		// // 查出该模块的所有操作的ModuleOperationBean
		// Result moduleOperationRs =
		// moduleOperationMgt.queryOperationByModule(moduleBean);
		// if (moduleOperationRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		// if (((ArrayList) moduleOperationRs.retVal).size() > 0) {
		// List<ModuleOperationBean> moduleOperationBeans = (ArrayList)
		// moduleOperationRs.retVal;
		// outter:
		// for (ModuleOperationBean moduleOperationBean : moduleOperationBeans)
		// {
		// // 查出UserModuleBean
		// Result userModuleRs =
		// userModuleMgt.queryUserModuleByUserAndModuleOperation
		// (userBean, moduleOperationBean.getId());
		// if (userModuleRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		//
		// if (((ArrayList) userModuleRs.retVal).size() > 0) {
		//
		// UserModuleBean userModule = (UserModuleBean) (((
		// ArrayList) userModuleRs.retVal).get(0));
		//
		// String sysTime = BaseDateFormat.format(new Date(),
		// BaseDateFormat.yyyyMMddHHmmss);
		// ScopeBean scopeBean = new ScopeBean(tableName, fieldName, value,
		// scopeFlag);
		// scopeBean.setId(IDGenerater.getId());
		// scopeBean.setCreateBy(loginBean.getId());
		// scopeBean.setCreateDatetime(sysTime);
		// scopeBean.setLstUpdateBy(loginBean.getId());
		// scopeBean.setLstUpdateDatetime(sysTime);
		// scopeBean.setUserModuleBean(userModule);
		// rs = scopeMgt.add(scopeBean);
		// if (rs.retCode !=
		// ErrorCanst.DEFAULT_SUCCESS) {
		// return rs;
		// }
		// }
		// } else {
		// return userModuleRs;
		// }
		// }
		// }
		// } else {
		// return moduleOperationRs;
		// }
		// } else {
		// return moduleRs;
		// }
		// }
		// }
		return rs;
	}

	public Map queryModule() {
		//
		// List stairModuleList = (ArrayList)
		// moduleMgt.queryStairModule().retVal;
		// List childModuleList = (ArrayList)
		// moduleMgt.queryChildModule().retVal;
		// Map<ModuleBean, Map<ModuleBean, List<ModuleOperationBean>>> moduleMap
		// = new HashMap<ModuleBean, Map<ModuleBean,
		// List<ModuleOperationBean>>>();
		// for (Object obj : stairModuleList) {
		// ModuleBean stairBean = (ModuleBean) obj;
		// Map<ModuleBean, List<ModuleOperationBean>> childList
		// = new HashMap<ModuleBean, List<ModuleOperationBean>>();
		// for (Object object : childModuleList) {
		//
		// ModuleBean module = (ModuleBean) object;
		// if (module.getClassCode().startsWith(stairBean.getClassCode())) {
		// List moduleOperations = (ArrayList) moduleOperationMgt.
		// queryOperationByModule(module).retVal;
		// childList.put(module, moduleOperations);
		// }
		// }
		// moduleMap.put(stairBean, childList);
		// }
		// return moduleMap;
		return null;
	}

	/**
	 * 添加用户前的准备
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
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.removeAttribute("operation");
		LoginBean loginBean = getLoginBean(request);

		// 调用查询role的方法
		Result roleResult = roleMgt.queryAllRoleByClassCode(loginBean
				.getSunCmpClassCode());

		// 启用分支机构时，查出当前所有级别子分支机构，否则只查出默认分支机构
		String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
		boolean openSunCompany = Boolean.parseBoolean(openValue);
		Result sunCompanyResult;
		if (openSunCompany) {
			sunCompanyResult = roleMgt.queryAllSubSunCompany(loginBean
					.getSunCmpClassCode());
		} else {
			sunCompanyResult = roleMgt.queryDefaultSunCompany();
		}

		if (roleResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			MOperation mop = (MOperation) getLoginBean(request)
					.getOperationMap().get("/UserQueryAction.do");
			request.setAttribute("MOID", mop.getModuleId());
			request.setAttribute("result_role", roleResult.retVal);
			
			/* 根据用户Id查询数据 */
			String userId = request.getParameter("keyId");
			if (userId != null && userId.length() != 0) {
				// 查询要修改的UserBean
				Result rs = userMgt.queryEmp(userId);
				request.setAttribute("result",((ArrayList)rs.getRetVal()).get(0));
			}
		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		if (sunCompanyResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("result_sunCompany", sunCompanyResult.retVal);
		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}

		return getForward(request, mapping, "userAdminAdd");

	}

	/**
	 * 添加失败用户列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward errorList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		String messageError = request.getParameter("messageError");
		List<String[]> list = getErrorList(messageError,request);
		request.setAttribute("list", list);
		
		return mapping.findForward("errorList");
	}
	
	private List<String[]> getErrorList(String messageError,HttpServletRequest request){
		String[] messages = messageError.split(",");
		List<String[]> list = new ArrayList<String[]>();
		for(String message:messages){
			String[] oneMessage = message.split(";");
			String employeeId = oneMessage[0];
			String mess = oneMessage[1];
			String[] l = new String[3];//0职员名称 1职员编号 2登录名称 3失败原因
			int messIndex = Integer.parseInt(mess);
			Result result = userMgt.detail(employeeId);
			EmployeeBean employbean = (EmployeeBean) result.getRetVal();
			String nbsp="&nbsp";
			switch (messIndex) {
			case 1:
				l[0]= "";
				l[1]= "";
				
				l[2]= getMessage(request, "com.not.find.info");
				break;
			case 2:
				l[0]= employbean.getEmpFullName();
				l[1]= employbean.getEmpNumber();
				
				l[2]= getMessage(request, "common.username.exist");
				break;
			case 3:
				l[0]= employbean.getEmpFullName();
				l[1]= employbean.getEmpNumber();
				
				l[2]= getMessage(request, "common.userInfo.exist");
				break;
			case 4:
				l[0]= employbean.getEmpFullName();
				l[1]= employbean.getEmpNumber();
				
				l[2]= "ERP用户数超过最大限制，请升级用户数，或停止操作";//getMessage(request, "user.msg.limitFailture");
				break;
			case 5:
				l[0]= employbean.getEmpFullName();
				l[1]= employbean.getEmpNumber();
				
				l[2]= getMessage(request, "oa.common.info.unknow");
				break;
			case 6:
				l[0]= employbean.getEmpFullName();
				l[1]= employbean.getEmpNumber();
				
				l[2]= "OA用户数超过最大限制，请升级用户数，或停止操作";
				break;
			case 7:
				l[0]= employbean.getEmpFullName();
				l[1]= employbean.getEmpNumber();
				
				l[2]= "CRM用户数超过最大限制，请升级用户数，或停止操作";
				break;
			case 8:
				l[0]= employbean.getEmpFullName();
				l[1]= employbean.getEmpNumber();
				
				l[2]= "HR用户数超过最大限制，请升级用户数，或停止操作";
				break;
			case 9:
				l[0]= employbean.getEmpFullName();
				l[1]= employbean.getEmpNumber();
				
				l[2]= "下单系统用户数超过最大限制，请升级用户数，或停止操作";
				break;	
			default:
				break;
			}
			for(int i = 0;i<l.length;i++){
				if(l[i] == null||"".equals(l[i])){
					l[i]=nbsp;
				}
			}
			list.add(l);
			
		}
		return list;		
	}
	
	
	/**
	 * 添加用户
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
	 */
	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserForm myForm = (UserForm) form;
		ActionForward forward = getForward(request, mapping, "alert");

		String[] roleIds = request.getParameterValues("roleIds");
		String[] sunCompanyIds = request.getParameterValues("sunCompanyIds");
		String employeeString = myForm.getEmployeeId();
		String sysNamesString = myForm.getSysName();
		String[] employees = employeeString.split(";");
		String[] sysNames = sysNamesString.split(";");
		if(employees.length != sysNames.length){
			EchoMessage.error().add(getMessage(request, "com.num.error"))
					.setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
			return forward;
		}
		int successNum = 0;//成功数
		int errorNum = 0 ;//失败数
		String split = "";
		StringBuffer messageError = new StringBuffer("");//失败信息
		//失败原因：1.没有找到对应的职员的信息 2.登录名称已经存在 3.该职员的系统用户已经存在 4.用户数超过最大限制，请升级用户数，或停止操作 5.未知原因
		for(int n =0;n<employees.length;n++){
			String employeeId = employees[n];
			String sysName = sysNames[n];
			if("".equals(messageError.toString())){
				split = "";
			}else{
				split = ",";
			}
			
			String lbxMonitorCh = request.getParameter("lbxMonitorCh");
			lbxMonitorCh = lbxMonitorCh == null ? "-1" : lbxMonitorCh.trim();// 默认为-1
			Result result = userMgt.detail(employeeId);
			if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				messageError.append(split+employeeId+";"+1);//1.没有找到系统用户的信息
				errorNum++;
				continue;
			}
			EmployeeBean employbean = (EmployeeBean) result.getRetVal();
			Result res_user = userMgt.queryEmployeeBySysName(sysName);// 查询此登录名称是否存在
			if (res_user.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && res_user.getRealTotal() > 0) {
				messageError.append(split+employeeId+";"+2);//2.登录名称已经存在
				errorNum++;
				continue;
			}
	        //判断该职员的系统用户是否已经存在！
			if(employbean!=null){
				Result rst = userMgt.queryEmployeeById(employeeId);
				if (rst.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && rst.getRealTotal() > 0) {
					messageError.append(split+employeeId+";"+3);//3.该职员的系统用户已经存在
					errorNum++;
					continue;
				}
			}

			// 根据选中的角色，创建UserRoleBean实例
			// ArrayList roleList = new ArrayList();
			// if (roleIds != null) {
			// for (String roleId : roleIds) {
			// if (roleId == null || roleId.trim().equals("")) {
			// continue;
			// }
			// RoleBean userrolebean = new RoleBean();
			// userrolebean.setId(roleId);
			// roleList.add(userrolebean);
			// }
			// }

			// 根据对此用户添加的分支机构，逐条插入记录
			// ArrayList sunCompanyList = new ArrayList();
			boolean checkedOneSunCompany = false;
			for (String sunCompanyId : sunCompanyIds) {
				if (!"".equals(sunCompanyId.trim())) {
					checkedOneSunCompany = true;
					break;
				}
			}
			if (!checkedOneSunCompany) {
				// 如果没选分支机构，则将用户加入默认分支机构（id为1的分支机构）
				addUserSunCompanyRoles(employeeId, "1", "");
			} else {
				for (int i = 0; i < sunCompanyIds.length; i++) {
					String sunCompanyId = sunCompanyIds[i];
					String roleId = roleIds[i];
					if (sunCompanyId == null || sunCompanyId.trim().equals("")) {
						continue;
					}
					// Result rsSunCmps = this.getsunCmps(sunCompanyId);
					// List lsSunCmps = (List) rsSunCmps.getRetVal();
					// for (int j = 0; j < lsSunCmps.size(); j++) {
					// String strSunCmpId = lsSunCmps.get(j).toString();
					// SunCompanyBean userSunCompany = new SunCompanyBean();
					// userSunCompany.setId(strSunCmpId);
					// sunCompanyList.add(userSunCompany);
					// }
					addUserSunCompanyRoles(employeeId, sunCompanyId, roleId);
				}
			}
			employbean.setOpenFlag(1);
			employbean.setSysName(sysName);
			employbean.setPassword(myForm.getPassword());
			employbean.setDefSys(myForm.getDefSys());
			employbean.setLbxMonitorCh(lbxMonitorCh);
			employbean.setMACAddress(myForm.getMACAddress());
			employbean.setUserKeyId(myForm.getUserKeyId());
			employbean.setLoginEndTime(myForm.getLoginEndTime());
			employbean.setLoginStartTime(myForm.getLoginStartTime());
			employbean.setTelpop(myForm.getTelpop());
			employbean.setDefaultPage(myForm.getDefaultPage());
			employbean.setExtension(myForm.getExtension());
			// employbean.setRoles(roleList);
			// employbean.setSunCompanys(sunCompanyList);
			employbean.setShopName(myForm.getShopName()) ;
			employbean.setShopPwd(myForm.getShopPwd()) ;
			employbean.setViewLeftMenu(myForm.getViewLeftMenu()) ;
			employbean.setViewTopMenu(myForm.getViewTopMenu()) ;
			employbean.setIpstart(myForm.getIpstart()) ;
			employbean.setIpend(myForm.getIpend()) ;
			employbean.setCanJxc(myForm.getCanJxc());
			employbean.setCanOa(myForm.getCanOa());
			employbean.setCanCrm(myForm.getCanCrm());
			employbean.setCanHr(myForm.getCanHr());
			employbean.setCanOrders(myForm.getCanOrders());
			employbean.setLastUpdateTime(BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
			employbean.setShowDesk(myForm.getShowDesk());
			// 添加用户
			Result rs = userMgt.add(employbean);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				//同步资料到即时通讯客户端
	    		
	    		MSGConnectCenter.refreshEmpInfo(employees);
				new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
						employbean.getEmpFullName()+"", "tblEmployee", "系统用户","");
				// 添加成功
				successNum++;
				continue;
			} else if (rs.retCode == ErrorCanst.RET_USER_LIMIT_ERROR) {
				// 添加失败
				messageError.append(split+employeeId+";"+4);//4.用户数超过最大限制，请升级用户数，或停止操作
				errorNum++;
				continue;
			} else if (rs.retCode == ErrorCanst.RET_OAUSER_LIMIT_ERROR) {
				// 添加失败
				messageError.append(split+employeeId+";"+6);//4.用户数超过最大限制，请升级用户数，或停止操作
				errorNum++;
				continue;
			} else if (rs.retCode == ErrorCanst.RET_CRMUSER_LIMIT_ERROR) {
				// 添加失败
				messageError.append(split+employeeId+";"+7);//4.用户数超过最大限制，请升级用户数，或停止操作
				errorNum++;
				continue;
			} else if (rs.retCode == ErrorCanst.RET_HRUSER_LIMIT_ERROR) {
				// 添加失败
				messageError.append(split+employeeId+";"+8);//4.用户数超过最大限制，请升级用户数，或停止操作
				errorNum++;
				continue;
			} else if (rs.retCode == ErrorCanst.RET_ORDERSUSER_LIMIT_ERROR) {
				// 添加失败
				messageError.append(split+employeeId+";"+9);//4.用户数超过最大限制，请升级用户数，或停止操作
				errorNum++;
				continue;
			} else {
				// 添加失败
				messageError.append(split+employeeId+";"+5);//5.未知原因
				errorNum++;
				continue;
			}
			
		}
		if(errorNum==0){
			EchoMessage.success().add(
					getMessage(request, "com.emp.login.success")//添加成功数量：
					+successNum+"<br>"
					+getMessage(request, "com.emp.login.error")//添加失败数量：
					+errorNum+"<br>")
					.setBackUrl("/UserQueryAction.do?winCurIndex="+ request.getParameter("winCurIndex"))
					.setNotAutoBack()
					.setAlertRequest(request);
		}else if(errorNum==1){
			List<String[]> list = getErrorList(messageError.toString(),request);
			
			EchoMessage.error().add(list.size() >0?list.get(0)[2]:"添加失败")
					.setBackUrl("/UserQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex"))
					.setAlertRequest(request);
		}else{
			EchoMessage.success().add(
					getMessage(request, "com.emp.login.success")//添加成功数量：
					+successNum+"<br>"
					+getMessage(request, "com.emp.login.error")//添加失败数量：
					+errorNum+"<br>"
					+"<a href='/UserManageAction.do?winCurIndex="+request.getParameter("winCurIndex")+"&operation=97&messageError="+messageError+"'>"+getMessage(request, "com.error.list")+"</a>")
					.setBackUrl("/UserQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex"))
					.setNotAutoBack()
					.setAlertRequest(request);
		}
		
		return forward;
	}

	// 添加一条用户对应的分支机构，及在此分支机构下拥有的角色
	public Result addUserSunCompanyRoles(final String employeeId,
			final String sunCompanyId, final String roleId) {
		final Result res = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql = "insert into tblUserSunCompany (userId,sunCompanyID,roleids) values('"
								+ employeeId
								+ "','"
								+ sunCompanyId
								+ "','"
								+ roleId + "')";
						try {
							PreparedStatement cs = conn.prepareStatement(sql);
							BaseEnv.log.debug(sql);
							int count = cs.executeUpdate();
							res.setRetVal(count);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error(
									"Insert data into tblUserSunCompany Error :"
											+ sql, ex);
							return;
						}
					}
				});
				return res.getRetCode();
			}
		});
		return res;
	}

	// 删除此用户所有分支机构及分支机构角色（对应的tblUserSunCompany表中记录）
	public Result delUserSunCompanyRolesByUserId(final String userId) {
		final Result res = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql = "delete from tblUserSunCompany where userid='"
								+ userId + "'";
						try {
							PreparedStatement cs = conn.prepareStatement(sql);
							BaseEnv.log.debug(sql);
							int count = cs.executeUpdate();
							res.setRetVal(count);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error(
									"delete data from tblUserSunCompany Error :"
											+ sql, ex);
							return;
						}
					}
				});
				return res.getRetCode();
			}
		});
		return res;
	}

	// 根据所选用户id删除此用户对应的分支机构及分支机构角色(只删除当前操作用户有权管理的记录)
	public Result delUserSunCompanyRolesByUserId(final String userId,
			final String classCode) {
		final Result res = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql = "delete from tblUserSunCompany where userid='"
								+ userId
								+ "' and sunCompanyid in (select tblSunCompanys.id "
								+ "from tblSunCompanys where classCode like '"
								+ classCode + "%')";
						try {
							PreparedStatement cs = conn.prepareStatement(sql);
							BaseEnv.log.debug(sql);
							int count = cs.executeUpdate();
							res.setRetVal(count);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error(
									"delete data from tblUserSunCompany Error :"
											+ sql, ex);
							return;
						}
					}
				});
				return res.getRetCode();
			}
		});
		return res;
	}

	/**
	 * 修改前的准备
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
	 */
	protected ActionForward updatePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward = getForward(request, mapping, "alert");
		Result rs = new Result();
		String userId = request.getParameter("keyId");
		LoginBean loginBean = getLoginBean(request);
		EmployeeBean user = null;
		if (userId != null && userId.length() != 0) {
			// 查询要修改的UserBean
			rs = userMgt.queryEmp(userId);
			request.setAttribute("userId", userId);

			RoleMgt roleMgt = new RoleMgt();
			Result rsSun = roleMgt.queryAllSunCompany();
			String defaultSunCmpName = "";
			for (Object o : (ArrayList) rsSun.getRetVal()) {
				String[] str = (String[]) o;
				if ("1".equals(str[0])) {
					defaultSunCmpName = str[1];
					break;
				}
			}
			RoleBean admin = (RoleBean) ((ArrayList) roleMgt.queryRoleById("1")
					.getRetVal()).get(0);
			request.setAttribute("defaultSunCmpName", defaultSunCmpName
					+ admin.getRoleName());

			// 查询要修改的分支机构
			List ls = (List) rs.retVal;
			ArrayList nls = new ArrayList();
			for(EmployeeBean eb:(ArrayList<EmployeeBean>)ls){
				nls.add(new Object[]{eb.getId()});
			}
			
			Result rss = this.getSunCompanys(nls);

			request.setAttribute("sunCompanys", rss.retVal);

			// 启用分支机构时，查出当前所有级别子分支机构，否则只查出默认分支机构
			String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
			boolean openSunCompany = Boolean.parseBoolean(openValue);
			Result sunCompanyResult;
			if (openSunCompany) {
				sunCompanyResult = roleMgt.queryAllSubSunCompany(loginBean
						.getSunCmpClassCode());
			} else {
				sunCompanyResult = roleMgt.queryDefaultSunCompany();
			}

			request.setAttribute("result_sunCompany", sunCompanyResult.retVal);
			// 查出所有角色
			Result allRolesResult = roleMgt.queryAllRoleByClassCode(loginBean
					.getSunCmpClassCode());
			request.setAttribute("all_roles", allRolesResult.getRetVal());
			
		} else {
			rs.setRetCode(ErrorCanst.NULL_OBJECT_ERROR);
		}
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			user = (EmployeeBean) ((ArrayList) rs.getRetVal()).get(0);		
			if(user!=null && user.getDefaultPage()!=null 
					&& user.getDefaultPage().length()>0){
				Result defPage=userMgt.getDefPageName(user.getDefaultPage(), this.getLocale(request).toString());
				request.setAttribute("defaultPageName", defPage.getRetVal());
			}
			rs = roleMgt.queryUserSunCompanyRoles(user.getId());
		}
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 加载成功
			request.setAttribute("user", user);
			String type = request.getParameter("type");
			MOperation mop = null;
			if (type != null && !"".equals(type)) {
				mop = (MOperation) getLoginBean(request).getOperationMap().get(
						"/UserQueryPassAction.do?type="
								+ request.getParameter("type"));
			} else {
				mop = (MOperation) getLoginBean(request).getOperationMap().get(
						"/UserQueryAction.do");
			}

			request.setAttribute("MOID", mop.getModuleId());
			request.setAttribute("result_role", rs.retVal);
			request.setAttribute("result", user);
			forward = getForward(request, mapping, "userAdminUpdate");

		} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// 记录不存在错误
			EchoMessage.error().add(getMessage(request, "common.error.nodata"))
					.setAlertRequest(request);
		} else {
			// 加载失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
		}

		return forward;
	}

	/**
	 * 修改前的准备
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
	 */
	protected ActionForward modulePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionForward forward = null;
		String userId = request.getParameter("keyId");
		if (userId == null && userId.length() == 0) {
			// 加载失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");

		}
		// 执行查询前的加载
		// 查询要修改的UserBean

		Result userModules = userMgt.queryUserModuleByUserid(userId);

		Result modules = userMgt.queryAll();

		if (userModules.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& modules.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 将userModule改成hashMap形式，方便在布面调用，避免频繁的循环
			HashMap uMap = new HashMap();
			for (Object o : (ArrayList) userModules.retVal) {
				UserModuleBean umb = (UserModuleBean) o;
				uMap.put(umb.getModuleOpId(), umb.getModuleOpId());
			}
			request.setAttribute("userModules", uMap);
			request.setAttribute("moduleList", ((Object[]) modules.retVal)[0]);
			request.setAttribute("modules", ((Object[]) modules.retVal)[1]);
			request.setAttribute("userId", userId);
			forward = getForward(request, mapping, "userModuleAdd");

		} else {
			// 加载失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
		}

		return forward;
	}
	
	protected ActionForward copyRight(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String sourceId = request.getParameter("sourceId"); //复制的原ID
		String dirId = request.getParameter("dirId"); //目录ID
		
		String msg="复制成功";
		if(sourceId==null||sourceId.length()==0){
			msg="原用户不能为空";
			request.setAttribute("msg", msg);
			return getForward(request,mapping,"blank");
		}
		ArrayList<String> dirIds= new ArrayList<String>();
		if(dirId != null){
			String[] dirs = dirId.split(";");
			for(String d:dirs){
				if(d!=null &&d.length() >0 && !d.equals(sourceId)){
					dirIds.add(d);
				}
			}
		}
		if(dirIds.size()==0){
			msg="目标用户不有为空";
			request.setAttribute("msg", msg);
			return getForward(request,mapping,"blank");
		}
		
		Result rs = userMgt.copyRight(sourceId, dirIds);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			msg="复制失败";			
		}else{
			//记录日志
			Result srs =userMgt.getEmployee(sourceId);
			if(srs.retVal != null){
				String sourceName = ((EmployeeBean)srs.retVal).getEmpFullName();
				for(String dId:dirIds){
					Result drs = userMgt.getEmployee(dId);
					if(drs.retVal != null){
						String dirName = ((EmployeeBean)drs.retVal).getEmpFullName();
						/*添加系统日志*/
						new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), "复制权限从"+sourceName+"至"+dirName, "tblEmployee", "系统用户","");
					}
				}
			}
		}
		request.setAttribute("msg", msg);
		return getForward(request,mapping,"blank");
	}

	/**
	 * 用户修改，周新宇
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
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String type = request.getParameter("type") == null ? "" : request
				.getParameter("type");

		ActionForward forward = null;
        String newpwd=request.getParameter("newpassword");
		UserForm myForm = (UserForm) form;
		String id = myForm.getEmployeeId();
        String uid=request.getParameter("id");
		Result result = userMgt.detail(uid); // 根据用户的ID查找用户表
		if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) { // 如果查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
			return forward;
		}
		EmployeeBean userbean = (EmployeeBean) (result).getRetVal(); // 获得用户信息
		if("modifyPass".equals(type)){
			String oldpassword=request.getParameter("oldpassword");
			if(!userbean.getPassword().equals(oldpassword)){
				EchoMessage.error().add(getMessage(request, "role.msg.oldPassError"))
				.setAlertRequest(request);
				forward = getForward(request, mapping, "alert");
				return forward;
			}
		}
		Result res_user = userMgt.queryEmployeeBySysName(myForm.getSysName());// 查询此登录名称是否存在
		if (!userbean.getSysName().equals(myForm.getSysName()) && res_user.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
				&& res_user.getRealTotal() > 0) {
			EchoMessage.error().add(
					getMessage(request, "common.username.exist"))
					.setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
			return forward;
		}
		String[] roleIds = request.getParameterValues("roleIds");
		String[] sunCompanyIds = request.getParameterValues("sunCompanyIds"); // 获得该用户所拥有的分支机构Id
		if(sunCompanyIds==null) sunCompanyIds = new String[]{}; //admin取的分支id为空
		String employeeId = userbean.getId();
		String lbxMonitorCh = request.getParameter("lbxMonitorCh");
		String telPrefix = request.getParameter("telPrefix");// 电话前缀
		String telAreaCode = request.getParameter("telAreaCode");// 用户所在地区位码
		// 默认来电通道-1
		lbxMonitorCh = lbxMonitorCh == null ? "-1" : lbxMonitorCh.trim();
		// 先删除此用户的对应分支机构角色记录（只删除当前操作用户有权管理的分支机构记录）
		String classCode = getLoginBean(request).getSunCmpClassCode();
		delUserSunCompanyRolesByUserId(employeeId, classCode);

		// 再按界面所选添加此用户的分支机构角色记录
		boolean checkedOneSunCompany = false;
		for (String sunCompanyId : sunCompanyIds) {
			if (!"".equals(sunCompanyId.trim())) {
				checkedOneSunCompany = true;
				break;
			}
		}
		if (!checkedOneSunCompany) { // 如果没有选分支机构
			this.addUserSunCompanyRoles(employeeId, "1", "");
		} else {
			for (int i = 0; i < sunCompanyIds.length; i++) {
				if (!"".equals(sunCompanyIds[i].trim())) {
					addUserSunCompanyRoles(employeeId, sunCompanyIds[i],
							roleIds[i]);
				}
			}
		}
		
		if (!type.equals("modifyPass")) {
			userbean.setSysName(myForm.getSysName());
			userbean.setMACAddress(myForm.getMACAddress());
			userbean.setUserKeyId(myForm.getUserKeyId());
			userbean.setLoginEndTime(myForm.getLoginEndTime());
			userbean.setLoginStartTime(myForm.getLoginStartTime());
			userbean.setDefaultPage(myForm.getDefaultPage());
			userbean.setExtension(myForm.getExtension());
			userbean.setTelpop(myForm.getTelpop());
			userbean.setDefSys(myForm.getDefSys());
			userbean.setLbxMonitorCh(lbxMonitorCh);
			userbean.setTelPrefix(telPrefix);
			userbean.setTelAreaCode(telAreaCode);
			
			userbean.setShopName(myForm.getShopName());
			userbean.setShopPwd(myForm.getShopPwd());
			userbean.setViewLeftMenu(myForm.getViewLeftMenu()) ;
			userbean.setViewTopMenu(myForm.getViewTopMenu()) ;
			userbean.setIpstart(myForm.getIpstart()) ;
			userbean.setIpend(myForm.getIpend()) ;
			userbean.setCanJxc(myForm.getCanJxc());
			userbean.setCanOa(myForm.getCanOa());
			userbean.setCanCrm(myForm.getCanCrm());
			userbean.setCanHr(myForm.getCanHr());
			userbean.setCanOrders(myForm.getCanOrders());
			userbean.setShowDesk(myForm.getShowDesk());
			//userbean.setRoles(userRolelist);
			// userbean.setSunCompanys(sunCompanyList);

			userbean.setMobileID(myForm.getMobileID());
			userbean.setCheckMobile(myForm.getCheckMobile());
			if(myForm.getPassword() != null && myForm.getPassword().length() > 0){
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("MD5");
					md.update(myForm.getPassword().trim().getBytes()) ;
					String md5Pwd = UserMgt.toHex(md.digest()) ;
					userbean.setPassword(md5Pwd);
				} catch (NoSuchAlgorithmException e) {
					BaseEnv.log.error("UserMgt.add Password MD5 Error :",e);
				}
				
			}
			
		}else if (type.equals("modifyPass")) {
			userbean.setPassword(newpwd);
		}
		Result rs = userMgt.update(userbean);

		forward = getForward(request, mapping, "alert");
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (type.equals("modifyPass")) {
				// 添加成功
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess")).setBackUrl(
							"/UserQueryPassAction.do?type=" + type+ "&winCurIndex="+ request.getParameter("winCurIndex"))
						.setAlertRequest(request);
				/*添加系统日志*/
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), userbean.getEmpFullName()+"修改密码", "tblEmployee", "系统用户","");
			} else {
				//更新内存用户信息
				UserMgt.initOnlineUser() ;
				// 添加成功
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl("/UserQueryAction.do?type=" + type
									+ "&winCurIndex="+ request.getParameter("winCurIndex"))
						.setAlertRequest(request);
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), userbean.getEmpFullName(), "tblEmployee", "系统用户","");
			}
		}else if (rs.retCode == ErrorCanst.RET_USER_LIMIT_ERROR) {
			// 添加失败
			EchoMessage.error().add("ERP用户数超过最大限制，请升级用户数，或停止操作")
					.setAlertRequest(request);
		} else if (rs.retCode == ErrorCanst.RET_OAUSER_LIMIT_ERROR) {
			// 添加失败
			EchoMessage.error().add("OA用户数超过最大限制，请升级用户数，或停止操作")
			.setAlertRequest(request);
		} else if (rs.retCode == ErrorCanst.RET_CRMUSER_LIMIT_ERROR) {
			// 添加失败
			EchoMessage.error().add("CRM用户数超过最大限制，请升级用户数，或停止操作")
			.setAlertRequest(request);
		} else if (rs.retCode == ErrorCanst.RET_HRUSER_LIMIT_ERROR) {
			// 添加失败
			EchoMessage.error().add("HR用户数超过最大限制，请升级用户数，或停止操作")
			.setAlertRequest(request);
		} else if (rs.retCode == ErrorCanst.RET_ORDERSUSER_LIMIT_ERROR) {
			// 添加失败
			EchoMessage.error().add("下单系统用户数超过最大限制，请升级用户数，或停止操作")
			.setAlertRequest(request);
		} else {
			// 添加失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateFailture"))
					.setAlertRequest(request);
		}
		//int num= Integer.parseInt(BaseEnv.systemSet.get("isOperUser").getSetting());
		//if(num==0){
		//	userMgt.updateLoginStatus();
		//}
		return forward;
	}
	
	/**
	 * 修改密码
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updatePwdPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "updatePwd") ;
		return forward;
	}
	
	/**
	 * 修改密码
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updatePwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String oldpassword = getParameter("oldpassword", request) ;
		String newpassword = getParameter("newpassword", request) ;
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(oldpassword.trim().getBytes()) ;
			String md5Pwd = UserMgt.toHex(md.digest()) ;
			oldpassword =md5Pwd;
			
			md = MessageDigest.getInstance("MD5");
			md.update(newpassword.trim().getBytes()) ;
			md5Pwd = UserMgt.toHex(md.digest()) ;
			newpassword =md5Pwd;
		} catch (NoSuchAlgorithmException e) {
		}
		
		LoginBean login = getLoginBean(request) ;
		EmployeeBean employee = (EmployeeBean) userMgt.detail(login.getId()).getRetVal() ;
		if(!employee.getPassword().equalsIgnoreCase(oldpassword)){
			EchoMessage.error().add(getMessage(request, "role.msg.oldPassError"))
						.setAlertRequest(request);
		}else{
			employee.setPassword(newpassword) ;
			Result result = userMgt.updatePwd(employee) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl("/UserManageAction.do?operation=101")
						.setAlertRequest(request);
				/*添加系统日志*/
				new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), employee.getEmpFullName()+"修改密码", "tblEmployee", "系统用户","");
			}else{
				EchoMessage.error().add(getMessage(request, "common.msg.updateFailture"))
						.setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 特殊权限，周新宇
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
	 */
	protected ActionForward module(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		String[] moduleOperations = request
				.getParameterValues("moduleOperations");
		String userId = request.getParameter("userId");
		ArrayList userModuleList = new ArrayList();
		for (String moduleOperation : moduleOperations) {
			if (moduleOperation != null && moduleOperation.length() > 0) {

				UserModuleBean userModuleBean = new UserModuleBean();
				userModuleBean.setId(IDGenerater.getId());
				EmployeeBean ub = new EmployeeBean();
				ub.setId(userId);
				userModuleBean.setEmployeeBean(ub);
				userModuleBean.setModuleOpId(moduleOperation);
				userModuleList.add(userModuleBean);
			}
		}
		Result rs = userMgt.updateUserModule(userId, userModuleList);

		forward = getForward(request, mapping, "alert");
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 添加成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl(
							"/UserQueryAction.do?winCurIndex="
									+ request.getParameter("winCurIndex"))
					.setAlertRequest(request);
		} else {
			// 添加失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateFailture"))
					.setAlertRequest(request);
		}
		return forward;
	}

	/**
	 * 查询
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
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String deptFullName=request.getParameter("deptFullName");
		request.setAttribute("deptFullName", deptFullName);
		
		UserSearchForm searchForm = (UserSearchForm) form;
		if (searchForm != null) {
			
			//外部连接到列表页面时，进行转码
			String paramStr = request.getQueryString();
	        String pStr = "paramValue=true";
	        if(paramStr!=null && paramStr.indexOf(pStr) != -1){
	        	//存在
	        	String paramVal = paramStr.substring(paramStr.indexOf(pStr)+pStr.length());
	        	String[] paramSplit = paramVal.split("&");
	        	for(String s : paramSplit){
	        		if(s != null && !"".equals(s) && s.indexOf("=")!=-1){
	        			if("departMent".equals(s.split("=")[0])){
	        				searchForm.setDepartMent(URLDecoder.decode(s.split("=")[1],"UTF-8"));
	        			}else if("name".equals(s.split("=")[0])){
	        				searchForm.setName(URLDecoder.decode(s.split("=")[1],"UTF-8"));
	        			}
	        		}
	        	}
	        }
			
			// 执行查询
			String SCompanyID = this.getLoginBean(request).getSunCmpClassCode();
			String userId = getLoginBean(request).getId();
			String type = (String) request.getAttribute("type");
			Result rs = userMgt.query(searchForm.getName(),
					searchForm.getPageNo(), searchForm.getPageSize(),
					SCompanyID, searchForm.getSysName(),searchForm.getDepartMent(),searchForm.getTitleid(),searchForm.getRoleName());
			
			Result rst=userMgt.queryUserSunCompanyRoles();
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS&&rst.retCode==ErrorCanst.DEFAULT_SUCCESS) {
				// 查询成功
				//根据用户输入的查询条件过滤用户
				ArrayList<EmployeeBean> list=(ArrayList<EmployeeBean>)rs.retVal;
				HashMap<String,String[]> map=(HashMap<String,String []>)rst.retVal;
				
				request.setAttribute("result", list);
				List ls = (List) rs.retVal;
				Result rss = this.getSunCompanys(ls);
				request.setAttribute("sunCompanys", rss.retVal);
				Result rsss = this.getUserSunCompanyByUserId(null,SCompanyID);
				request.setAttribute("userSunCompanys", rsss.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
				request.setAttribute("roleName",map);

			} else {
				// 查询失败
				EchoMessage.error()
						.add(getMessage(request, "common.msg.error"))
						.setRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		return getForward(request, mapping, "userAdminlist");
	}

	/**
	 * 删除
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
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws  Exception {

        ActionForward forward = null;
        String nstr[] = request.getParameterValues("keyId");
        if (nstr != null && nstr.length != 0) {
            Result rs = userMgt.delete(nstr);

            //同步删除用户到客户端
            MSGConnectCenter.deleteObj(nstr, "employee");
            // 同时删除此用户在tblUserSunCompany表中的记录
            for (String str : nstr) {
                delUserSunCompanyRolesByUserId(str);
            }
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                // 删除成功
            	ArrayList<EmployeeBean> list = (ArrayList<EmployeeBean>)rs.retVal;
            	for(EmployeeBean eb:list){
            		new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),
            				eb.getEmpFullName(), "tblEmployee", "系统用户","");
            	}
                request.setAttribute("result", rs.retVal);
                forward = query(mapping, form, request, response);
            } else {
                // 删除失败
                EchoMessage.error().add(getMessage(request,"common.msg.delError")). setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;
    }

	/**
	 * 查找分支机构 赵健
	 *
	 */
	public Result getSunCompanys(List list) {
		final Result rs = new Result();
		final String sql = "";
		final List ls = list;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List sunCompany = new ArrayList(); // 存放分支机构的信息

							for (int i = 0; i < ls.size(); i++) {
								Object[] e = (Object[]) ls.get(i);
								String userId = (String)e[0]; // 获得已查出来的用户ID
								// 根据用户ID，查询中间表的分支机构ID
								String selSunCompanyid = "select distinct(sunCompanyid) from tblUserSunCompany where userid ='"
										+ userId + "'";
								Statement stSunCompanyId = conn
										.createStatement();
								ResultSet rsSunCompanyId = stSunCompanyId
										.executeQuery(selSunCompanyid);
								while (rsSunCompanyId.next()) { // 循环取出分支机构ID
									String sunCompanysId = rsSunCompanyId
											.getString(1);

									String selSunCompany = "select * from dbo.tblSunCompanys where id ='"
											+ sunCompanysId + "'";
									Statement stSunCompany = conn
											.createStatement();
									ResultSet rsSunCompany = stSunCompany
											.executeQuery(selSunCompany);
									if (rsSunCompany.next()) {
										String[] suncompanys = new String[4];
										suncompanys[0] = rsSunCompany
												.getString(1);
										suncompanys[1] = rsSunCompany
												.getString(2);
										suncompanys[2] = rsSunCompany
												.getString(3);
										suncompanys[3] = userId; // 将获得的用户ID也一起放到这里
										sunCompany.add(suncompanys);
									}
								}

							}
							rs.retVal = sunCompany;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}

	/***************************************************************************
	 * 查找用户与分支机构的中间表
	 */
	public Result getUserSunCompanyByUserId(final String userId,final String sunCompanyId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select userid,sunCompanyid,roleids from tblUserSunCompany";
							if(userId != null){
								sql += " where userId='" + userId + "' and sunCompanyid='"+sunCompanyId+"'";
							}
							Statement st = conn.createStatement();
							ResultSet rss = st.executeQuery(sql);
							List<String[]> ls = new ArrayList<String[]>();
							while (rss.next()) {
								String[] userSunC = new String[3];
								userSunC[0] = rss.getString(1);
								userSunC[1] = rss.getString(2);
								userSunC[2] = rss.getString(3);
								ls.add(userSunC);
							}
							rs.retVal = ls;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		return rs;
	}

	
	/***************************************************************************
	 * 赵健 根据登陆名查询用户
	 */
	public Result getUser(final String loginName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "" ;
						try {
							sql = "select * from dbo.tblEmployee where sysName = ?";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, loginName) ;
							ResultSet rss = pss.executeQuery() ;
							List ls = new ArrayList();
							while (rss.next()) {
								String uEmployee = rss.getString(1);
								ls.add(uEmployee);
							}
							rs.retVal = ls;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}

	/***************************************************************************
	 * 赵健 根据分支机构ID查询该分支机构下所有的小分支机构
	 */
	public Result getsunCmps(String sunCompanysId) {
		final String sunCmpId = sunCompanysId;
		final Result rs = new Result();
		final String sql = "";
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String str_sel = "select id from tblSunCompanys where classCode like ((select classCode from tblSunCompanys where id='"
									+ sunCmpId + "')+'%')";
							Statement st = conn.createStatement();
							ResultSet rss = st.executeQuery(str_sel);
							List ls = new ArrayList();
							while (rss.next()) {
								String uEmployee = rss.getString(1);
								ls.add(uEmployee);
							}
							rs.retVal = ls;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}

	/***************************************************************************
	 * 赵健 根据分支机构ID查询该分支机构的ClassCode
	 */
	public Result getsunCmpsClassCode(String sunCompanysId) {
		final String sunCmpId = sunCompanysId;
		final Result rs = new Result();
		final String sql = "";
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String str_sel = "select classCode from tblSunCompanys where id = '"
									+ sunCmpId + "'";
							Statement st = conn.createStatement();
							ResultSet rss = st.executeQuery(str_sel);
							List ls = new ArrayList();
							while (rss.next()) {
								String cmpClassCode = rss.getString(1);
								ls.add(cmpClassCode);
							}
							rs.retVal = ls;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}


	
	/**
	 * 修改密码不做具体的权限判断 只要有查询权限就可以修改自己的密码
	 */
	protected ActionForward doAuth(HttpServletRequest request, ActionMapping mapping) {
		
		String operation = getParameter("operation", request) ;
		if("102".equals(operation) || "101".equals(operation)){
			return null ;
		}
		return super.doAuth(request, mapping);
	}
	
	
}
