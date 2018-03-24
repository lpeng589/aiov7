package com.koron.oa.employeeDepartment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.customize.*;
import com.menyi.aio.web.importData.ExportField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.*;
/**
 * 
 * <p>
 * Title:职员部门Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-11-01 下午 13：44
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class EmployeeDepartmentAction extends MgtBaseAction{

	String changeUsed = "";							//允许修改资料
	DynDBManager dbmgt = new DynDBManager();
	EmployeeDepartmentMgt mgt = new EmployeeDepartmentMgt();
	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 启用外币核算 */
		Hashtable<String, SystemSettingBean> base = BaseEnv.systemSet;
		SystemSettingBean bean = base.get("ChangeUsed");
		changeUsed = bean.getSetting();
		
		int operation = getOperation(request);
		ActionForward forward = null;
		
		String opType = request.getParameter("opType");
		
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			if(opType != null && "employeePre".equals(opType)){
				//添加职员前
				forward = addEmployeePre(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_ADD:
			if(opType != null && "employee".equals(opType)){
				//添加职员
				forward = addEmployee(mapping, form, request, response);
			}else if(opType != null && "department".equals(opType)){
				//添加部门
				forward = addDepartment(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			if(opType != null && "updatePreEmployee".equals(opType)){
				//修改职员前
				forward = updatePreEmployee(mapping, form, request, response);
			}else if(opType != null && "updatePreDepartment".equals(opType)){
				//修改前部门
				forward = updatePreDept(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			if(opType != null && "updateEmployee".equals(opType)){
				//修改职员
				forward = updateEmployee(mapping, form, request, response);
			}else if(opType != null && "updateDepartment".equals(opType)){
				//修改部门
				forward = updateDept(mapping, form, request, response);
			}else if(opType != null && "setLeave".equals(opType)){
				//设置为离职或者启用职员
				forward = setEmpStatus(mapping, form, request, response);
			}else if(opType != null && "setDeptStatus".equals(opType)){
				//部门启用停用
				forward = setDeptStatus(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DELETE:
			if(opType != null && "delEmployee".equals(opType)){
				//删除职员
				forward = deleteEmployee(mapping, form, request, response);
			}else if(opType != null && "delDepartment".equals(opType)){
				//删除部门
				forward = deleteDept(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DETAIL:
			if(opType != null && "detailEmployee".equals(opType)){
				//职员详情
				forward = detailEmployee(mapping, form, request, response);
			}
			break;
		default:
			if(opType != null && "loadEmployee".equals(opType)){
				//加载职员
				forward = loadEmployeeData(mapping, form, request, response);
			}else if(opType != null && "queryAccnumber".equals(opType)){
				//获取最新的职员编号或者部门编号
				forward = queryAccNumber(mapping, form, request, response);
			}else if (opType != null && "exportData".equals(opType)) {
				//导出列表
				forward = exportData(mapping, form, request, response);
			}else if(opType != null && "reloadEnum".equals(opType)){
				//重新加载枚举
		    	forward= reloadEnum(mapping,form,request,response);
			}else{
				//加载部门树
				forward = loadDepartmentTree(mapping, form, request, response);
			}
			break;
		}
		return forward;
	}
	
	
	/**
	 * 加载部门树
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward loadDepartmentTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		/* 用户权限 */
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/EmployeeDepartmentAction.do"));
		
		String locale = this.getLocale(request).toString();
		
		
		String status = request.getParameter("status");
		if(status == null || "".equals(status)){
			status = "0";
		}
		
		/**
		 * 查询所有部门
		 */
		Result rs = mgt.loadDepartment(status);
		String folderTree = "[";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			List list = (ArrayList)rs.retVal;
			int num =0;
			//对数据进行处理把数据组合成节点形式
			folderTree += "{ id:\"0\", pId:-1, name:\"";
			if("".equals(GlobalsTool.getCompanyName(locale))){
				folderTree += getMessage(request, "com.framework.from");
			}else{
				folderTree += GlobalsTool.getCompanyName(locale);
			}
			folderTree += "\"}";
			if(list.size()>0){
				folderTree += ",";
			}
			for(int i=0;i<list.size();i++){
				HashMap o = (HashMap)list.get(i);
				if(String.valueOf(o.get("classCode")).length() == 5){
					//父节点
					folderTree += "{ id:\""+o.get("classCode")+"\", pId:0, name:\""+o.get("deptCode")+"-"+o.get("deptFullName")+"\", statusId:\""+o.get("statusId")+"\", number:\""+o.get("deptCode")+"\"}";
				}else{
					String classC = ((String)o.get("classCode")).substring(0, ((String)o.get("classCode")).length()-5);
					folderTree += "{ id:\""+o.get("classCode")+"\", pId:\""+classC+"\", name:\""+o.get("deptCode")+"-"+o.get("deptFullName")+"\", statusId:\""+o.get("statusId")+"\", number:\""+o.get("deptCode")+"\"}";
				}
				if(num != list.size()-1){
					folderTree+=",";
				}
				num ++;
			}
		}
		folderTree += "]";
		request.setAttribute("deptData", folderTree);
		request.setAttribute("status", status);
		
		return getForward(request, mapping, "toTree");
	}
	
	
	/**
	 * 查询职员数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward loadEmployeeData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		/* 分页进行处理 */
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		int pageNos = 1;
		int pageSizes = 20;
		SystemSettingBean bean = BaseEnv.systemSet.get("PageSize");
		if (bean != null) {
			pageSizes = Integer.parseInt(BaseEnv.systemSet.get("PageSize").getSetting());
		}
		if(pageNo != null && !"".equals(pageNo)){
			pageNos = Integer.parseInt(pageNo);
		}
		if(pageSize != null && !"".equals(pageSize)){
			pageSizes = Integer.parseInt(pageSize); 
		}
		
		String employeeType = request.getParameter("employeeType");					//员工类型（在职，离职）		
		String keyword = request.getParameter("keyword");							//关键字
		String departmentCode = request.getParameter("departmentCode");				//部门
		
		Result rs = null;
		//存在部门
		if(departmentCode != null && !"".equals(departmentCode)){
			rs = mgt.queryDept(departmentCode);
			request.setAttribute("dept", rs.getRetVal());
		}
		/**
		 * 查询职员数据
		 */
		rs = mgt.loadEmployeeData(employeeType, keyword, departmentCode, this.getLocale(request).toString(), pageNos, pageSizes);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])rs.getRetVal();
			String sqlval = obj[0].toString();
			sqlval = sqlval.replaceAll("%", "@CentSign:").replaceAll("\\+", "@AddSign:");
			request.setAttribute("saveSql",sqlval);
			List list = (ArrayList)obj[1];
			request.setAttribute("list", list);
			request.setAttribute("pageBar", this.pageBar2(rs, request));
			request.setAttribute("employeeType", employeeType);
			request.setAttribute("keyword", keyword);
			request.setAttribute("departmentCode", departmentCode);
		}
		return getForward(request, mapping, "employeeList");
	}
	
	
	/**
	 * 添加职员前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addEmployeePre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String tableName = request.getParameter("tableName");
		String parentCode = request.getParameter("parentCode");
		String fieldName = request.getParameter("fieldName");
		HashMap empData = new HashMap();
		
		/* 表结构 */
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		request.setAttribute("fieldInfos", tableInfo.getFieldInfos());
		
		/* 获取最新的职员编号 */
		String accnumber = "";
		Result rs=new DynDBManager().getLastValue(tableName, parentCode==null?"":parentCode, fieldName,"");
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			if(rs.getRetVal()!=null){
				accnumber = rs.getRetVal()+"";
			}
		}
    	empData.put("EmpNumber", accnumber);
    	
    	String departmentCode = request.getParameter("departmentCode");
    	if(departmentCode != null && !"".equals(departmentCode)){
    		Result rset = mgt.queryDept(departmentCode);
    		HashMap deptMap = (HashMap)rset.getRetVal();
    		if(deptMap != null && deptMap.size()>0){
    			empData.put("DepartmentCode", departmentCode);
    			empData.put("tblDepartment.DeptFullName", deptMap.get("deptFullName"));
    		}
    	}
    	request.setAttribute("operation", "1");
    	
    	/* 选项数据 */
    	Map map = BaseEnv.enumerationMap;
		EnumerateBean beans[] = (EnumerateBean[]) map.values().toArray(new EnumerateBean[0]);
		HashMap<String,String> enumerMap = new HashMap<String,String>();
		for (int i = 0; i < beans.length; i++) {
			enumerMap.put(beans[i].getEnumName(), beans[i].getId());
		}
    	request.setAttribute("enumerMap", enumerMap);
    	
    	request.setAttribute("empData", empData);
    	
		return getForward(request, mapping, "dealEmployee");
	}
	
	/**
	 * 添加职员
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addEmployee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
	
		LoginBean lg = this.getLoginBean(request);
		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		
		String tableName  = getParameter("tableName", request);
		/*表结构信息*/
        DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
        MOperation mop = (MOperation)lg.getOperationMap().get("/EmployeeDepartmentAction.do");
        HashMap values = new HashMap();
        /*取主表数据*/
        readMainTable(values, tableInfo, request);
        values.put("createBy", lg.getId());
		values.put("lastUpdateBy", lg.getId());
		values.put("createTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		values.put("SCompanyID", "00001");
		values.put("id", IDGenerater.getId());
		values.put("EmpFullNamePYM", CustomizePYM.getFirstLetter((String)values.get("EmpFullName")));
		Result rs = dbmgt.add(tableName, map, values, lg.getId(), "", "", resources, this.getLocale(request), "", lg, null, "", null);
		request.setAttribute("noAlert", "true");
		request.setAttribute("checkFlag", "false");
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//添加成功
			/* 更新数据缓存 */
	        MSGConnectCenter.refreshEmpInfo(new String[]{String.valueOf(values.get("id"))});
			new DynDBManager().addLog(0, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"添加职员："+values.get("EmpNumber")+"-"+values.get("EmpFullName"),"tblEmployee", "职员管理","");
			request.setAttribute("dealAsyn", "addOK;"+values.get("DepartmentCode"));
			EchoMessage.success().add(getMessage(
                    request, "common.msg.addSuccess")).setAlertRequest(request);
		}else{
			request.setAttribute("dealAsyn", "addERROR");
			SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 添加部门
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addDepartment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String deptCode = request.getParameter("deptCode");					//部门编号
		String deptName = request.getParameter("deptName");					//部门名称
		String topClassCode = request.getParameter("topClassCode");			//上级部门
		LoginBean lg = this.getLoginBean(request);
		//Result rs = mgt.queryDeptIsEmp(topClassCode, "exact");
		//if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
		//	int count = Integer.parseInt(rs.getRetVal().toString());
		//	if(count>0){
		//		request.setAttribute("msg", "isEmp");
		//		return getForward(request, mapping, "blank");
		//	}
			/* 获取最新的classCode */
			PublicMgt pcmgt = new PublicMgt() ;
			String newClassCode = pcmgt.getLevelCode("tblDepartment", topClassCode);
			
			Result rs = mgt.dealDepartment("add",deptCode,deptName,newClassCode,this.getLoginBean(request));
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//添加成功
				int count = Integer.parseInt(rs.getRetVal().toString());
				if(count == 1){
					//添加成功
					new DynDBManager().addLog(0, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"添加部门："+deptCode+"-"+deptName,"tblDepartment", "职员管理","");
					
					request.setAttribute("msg", "OK");
				}else{
					//添加失败
					request.setAttribute("msg", "ERROR");
				}
			}
		//}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 修改职员前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updatePreEmployee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String id = request.getParameter("id");
		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		LoginBean lg = this.getLoginBean(request);
		MOperation mop = (MOperation)lg.getOperationMap().get("/EmployeeDepartmentAction.do");
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
	    boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
		DBTableInfoBean tableInfo = (DBTableInfoBean) map.get("tblEmployee");
		request.setAttribute("fieldInfos", tableInfo.getFieldInfos());
		Result rs = dbmgt.detail("tblEmployee", map, id, lg.getSunCmpClassCode(),props,lg.getId(),isLastSunCompany,"");
		/**
		 * 查询职员数据
		 */
//		Result rs = mgt.queryEmployee(id, locale);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			request.setAttribute("empData", rs.getRetVal());
			request.setAttribute("operation", "2");
			/* 选项数据 */
	    	Map enumerMaps = BaseEnv.enumerationMap;
			EnumerateBean beans[] = (EnumerateBean[]) enumerMaps.values().toArray(new EnumerateBean[0]);
			HashMap<String,String> enumerMap = new HashMap<String,String>();
			for (int i = 0; i < beans.length; i++) {
				enumerMap.put(beans[i].getEnumName(), beans[i].getId());
			}
	    	request.setAttribute("enumerMap", enumerMap);
		}
		
		return getForward(request, mapping, "dealEmployee");
	}
	
	/**
	 * 设置是否离职还是启用
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward setEmpStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String status = request.getParameter("status");			//状态（-1离职，0启用）
		String id = request.getParameter("id");					//用户的Id
		LoginBean lg = this.getLoginBean(request);
		Result rs = mgt.setStatus(id,status);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			int count = Integer.parseInt(rs.getRetVal().toString());
			if(count == 1){
				//设置成功
				rs = mgt.queryEmployee(id, this.getLocale(request).toString());
				HashMap empMap = (HashMap)rs.retVal;
				String ms = "职员启用";
				if("-1".equals(status)){
					ms = "职员离职";
				}
				new DynDBManager().addLog(14, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"修改职员状态："+empMap.get("empNumber")+"-"+empMap.get("empFullName"),"tblEmployee","职员管理",ms);
				request.setAttribute("msg", "OK");
			}else{
				//设置失败
				request.setAttribute("msg", "ERROR");
			}
		}else{
			//设置失败
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 部门启用停用
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward setDeptStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String status = request.getParameter("status");					//状态（-1停用，0启用）
		String classCode = request.getParameter("classCode");			//部门的classCode
		LoginBean lg = this.getLoginBean(request);
		Result rs = mgt.setDeptStatus(classCode,status);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			int count = Integer.parseInt(rs.getRetVal().toString());
			if(count == 1){
				//设置成功
				String ms = "启用部门";
				if("-1".equals(status)){
					ms = "停用部门";
				}
				rs = mgt.queryDept(classCode);
				HashMap deptMap = (HashMap)rs.retVal;
				new DynDBManager().addLog(14, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "修改部门状态："+deptMap.get("deptCode")+"-"+deptMap.get("deptFullName"),"tblDepartment","职员管理",ms);
				request.setAttribute("msg", "OK");
			}else{
				//设置失败
				request.setAttribute("msg", "ERROR");
			}
		}else{
			//设置失败
			request.setAttribute("msg", "ERROR"+rs.getRetVal());
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 修改部门前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updatePreDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String classCode = request.getParameter("classCode");
		
		/**
		 * 查询部门数据
		 */
		Result rs = mgt.queryDept(classCode);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			request.setAttribute("result", rs.getRetVal());
		}
		return getForward(request, mapping, "");
	}
	
	/**
	 * 修改职员
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updateEmployee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		LoginBean lg = this.getLoginBean(request);
		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		String tableName  = getParameter("tableName", request);
		/*表结构信息*/
        DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
        MOperation mop = (MOperation)lg.getOperationMap().get("/EmployeeDepartmentAction.do");
        HashMap values = new HashMap();
        /*取主表数据*/
        readMainTable(values, tableInfo, request);
        values.put("lastUpdateBy", lg.getId());
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		
		request.setAttribute("noAlert", "true");
		request.setAttribute("checkFlag", "false");
		Result rs = mgt.queryEmployee(String.valueOf(values.get("id")),this.getLocale(request).toString());
		HashMap empMap = null;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			empMap = (HashMap)rs.retVal;
			if("false".equals(changeUsed) && (!values.get("EmpNumber").equals(empMap.get("empNumber")) || !values.get("EmpFullName").equals(empMap.get("empFullName")))){
				//修改过任何一项数据时
				rs = mgt.isDealDeptOrEmployee("EmployeeID", String.valueOf(empMap.get("id")), this.getLocale(request).toString());
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("dealAsyn", "updateNo;"+rs.getRetVal()+",使用过此职员,不能修改编号、名称等信息");
					EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}
		}
		values.put("EmpFullNamePYM", CustomizePYM.getFirstLetter((String)values.get("EmpFullName")));
		rs = dbmgt.update(tableName, map, values, lg.getId(),"",resources,this.getLocale(request),"update",lg,null,null);
		/**
		 * 保存数据
		 */
//		rs = mgt.dealEmployeeData(empForm,lg,resources,this.getLocale(request),map);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//修改成功
			/* 更新数据缓存 */
	        MSGConnectCenter.refreshEmpInfo(new String[]{String.valueOf(values.get("id"))});
			new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"职员修改前："+empMap.get("empNumber")+"-"+empMap.get("empFullName")+";修改后："+values.get("EmpNumber")+"-"+values.get("EmpFullName")+";","tblEmployee", "职员管理","");
			request.setAttribute("dealAsyn", "updateOK;"+values.get("DepartmentCode"));
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		}else{
			request.setAttribute("dealAsyn", "updateERROR");
			SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 修改部门
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updateDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String deptCode = request.getParameter("deptCode");					//部门编号
		String deptName = request.getParameter("deptName");					//部门名称
		String classCode = request.getParameter("topClassCode");			//classCode
		LoginBean lg = this.getLoginBean(request);
		Result rs = mgt.queryDept(classCode);
		HashMap deptMap = null;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			deptMap = (HashMap)rs.retVal;
			if("false".equals(changeUsed) && (!deptCode.equals(deptMap.get("deptCode")) || !deptName.equals(deptMap.get("deptFullName")))){
				//修改过任何一项数据时
				rs = mgt.isDealDeptOrEmployee("DepartmentCode", deptCode, this.getLocale(request).toString());
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("msg", rs.getRetVal()+",使用过此部门,不能修改编号、名称等信息");
					return getForward(request, mapping, "blank");
				}
			}
		}
		
		/**
		 * 保存数据
		 */
		rs = mgt.dealDepartment("update", deptCode, deptName, classCode, this.getLoginBean(request));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			int count = Integer.parseInt(rs.getRetVal().toString());
			if(count == 1){
				//修改成功
				new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"部门修改前："+deptMap.get("deptCode")+"-"+deptMap.get("deptFullName")+";修改后:"+deptCode+"-"+deptName+";","tblDepartment", "职员管理","");
				
				request.setAttribute("msg", "OK");
			}else{
				//修改失败
				request.setAttribute("msg", "ERROR");
			}
		}else{
			request.setAttribute("msg", rs.retVal);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 删除职员
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward deleteEmployee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String id = request.getParameter("id");				//职员Id
		//删除操作
		Result rs = mgt.deleteEmployee(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//删除成功
			request.setAttribute("msg", "OK");
		}else{
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 删除部门
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String classCode = request.getParameter("classCode");							//部门classCode
		LoginBean lg = this.getLoginBean(request);
		/* 查询部门下是否存在职员 */
		Result rs = mgt.queryDeptIsEmp(classCode,"");
		if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			int count = Integer.parseInt(rs.getRetVal().toString());
			if(count>0){
				request.setAttribute("msg", "isEmp");
				return getForward(request, mapping, "blank");
			}
			
			/* 查询部门下是否存在下级部门，存在时要先删除下级部门才可以删除此部门 */
			rs = mgt.queryIsNextDept(classCode);
			if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
				count = Integer.parseInt(rs.getRetVal().toString());
				if(count>0){
					request.setAttribute("msg", "isDept");
					return getForward(request, mapping, "blank");
				}
			}
			rs = mgt.queryDept(classCode);
			HashMap olddeptMap = (HashMap)rs.retVal;
			/* 删除部门 */
			rs = mgt.deleteDept(classCode);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//删除成功
				HashMap<String,String> deptMap = (HashMap)request.getSession().getServletContext().getAttribute("deptMap");
				deptMap.remove(classCode);
				request.getSession().getServletContext().setAttribute("deptMap", deptMap);
				new DynDBManager().addLog(2, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "删除部门："+olddeptMap.get("deptCode")+"-"+olddeptMap.get("deptFullName"),"tblDepartment","职员管理","");
				request.setAttribute("msg", "OK");
			}else{
				request.setAttribute("msg", "ERROR");
			}
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 职员详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward detailEmployee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String id = request.getParameter("id");
		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		LoginBean lg = this.getLoginBean(request);
		MOperation mop = (MOperation)lg.getOperationMap().get("/EmployeeDepartmentAction.do");
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
	    boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
		DBTableInfoBean tableInfo = (DBTableInfoBean) map.get("tblEmployee");
		request.setAttribute("fieldInfos", tableInfo.getFieldInfos());
		Result rs = dbmgt.detail("tblEmployee", map, id, lg.getSunCmpClassCode(),props,lg.getId(),isLastSunCompany,"");
		/**
		 * 查询职员数据
		 */
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			request.setAttribute("empData", rs.getRetVal());
			request.setAttribute("operation","5");
			/* 选项数据 */
	    	Map enumerMaps = BaseEnv.enumerationMap;
			EnumerateBean beans[] = (EnumerateBean[]) enumerMaps.values().toArray(new EnumerateBean[0]);
			HashMap<String,String> enumerMap = new HashMap<String,String>();
			for (int i = 0; i < beans.length; i++) {
				enumerMap.put(beans[i].getEnumName(), beans[i].getId());
			}
	    	request.setAttribute("enumerMap", enumerMap);
		}
		
		return getForward(request, mapping, "dealEmployee");
	}
	
	
	/**
	 * 获取最新的职员编号或者部门编号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAccNumber(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String tableName = request.getParameter("tableName");
		String parentCode = request.getParameter("parentCode");
		String fieldName = request.getParameter("fieldName");
		
		String accnumber = "";
		
		Result rs=new DynDBManager().getLastValue(tableName, parentCode, fieldName,"");
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			if(rs.getRetVal()!=null){
				accnumber = rs.getRetVal()+"";
			}
		}
    	request.setAttribute("msg", accnumber);
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 加载选项数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward reloadEnum(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InitMenData imd = new InitMenData();
		//更新内存中枚举值
		imd.initEnumerationInformation();
		String enumerName = request.getParameter("enumerName");
		String firstId = "";
		List<KeyPair> enumList = GlobalsTool.getEnumerationItems(enumerName, this.getLocale(request).toString());
		String str = "";
		String enumIds = "";
		for(KeyPair keyPair : enumList){
			str += "<option value='"+keyPair.getValue()+"' >"+keyPair.getName()+"</option>";
			enumIds += keyPair.getValue() + ",";
			if("".equals(firstId)){
				firstId = keyPair.getValue();
			}
		}
		request.setAttribute("msg", str);
		return mapping.findForward("blank");
	}
	
	/**
	 * 导出职员
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String[] keyIds = getParameters("keyId", request);
		String tableName = request.getParameter("tableName");
		String accName = GlobalsTool.getSysSetting("DefBackPath");
		File file = new File("..\\..\\AIOBillExport");
		if (!file.exists()) {
			file.mkdirs();
		}
		String tableDisplay = GlobalsTool.getTableInfoBean(tableName).getDisplay().get(getLocale(request).toString());
		String fileName = file.getCanonicalPath() + "\\" + tableDisplay + ".xls";
		Result result = new Result();
		ArrayList<ExportField> exportList = new ArrayList<ExportField>();
		ArrayList<HashMap> exportValuesList = new ArrayList<HashMap>();
		LoginBean lg = getLoginBean(request);
		String sunClassCode = lg.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
        boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
        MOperation mop = GlobalsTool.getMOperationMap(request) ;
        //执行加载明细
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        DBTableInfoBean dbTable = GlobalsTool.getTableInfoBean(tableName);
        ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(tableName, map);
        request.setAttribute("mainTable",dbTable);
        request.setAttribute("fieldInfos", dbTable.getFieldInfos()) ;
        request.setAttribute("childTableList", childTableList) ;
        ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
        ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();
        Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;//子表的列配置
        Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)request.getSession().getServletContext().getAttribute("userSettingColConfig") ;
        request.setAttribute("configList", configList);
        request.setAttribute("allConfigList",allConfigList);
        request.setAttribute("childTableConfigList",childTableConfigList);
        request.setAttribute("tableName", tableName);
        
        /* 未选择记录时,查询所有数据 */
        String export = request.getParameter("export");
        if(export != null && "All".equals(export)){
        	Result rs = mgt.queryIds(tableName);
        	keyIds = (String[])rs.getRetVal();
        }
        
		for (int z = 0; keyIds != null && z < keyIds.length; z++) {
			String keyex = keyIds[z];
			request.setAttribute("detail", "detail");
			request.setAttribute("detailOk", "1");
			Result rs = new DynDBManager().detail(tableName, map, keyex, sunClassCode,props,lg.getId(),isLastSunCompany,"");
			HashMap hm = (HashMap) rs.retVal;
			request.setAttribute("values", hm);
			if("1".equals(hm.get("isPublic")+"")){
				continue;
			}
			if(hm.get("tblAccTypeInfo.AccName") != null && hm.get("LANGUAGEQUERY") != null){
				KRLanguage loc = (KRLanguage)((HashMap)hm.get("LANGUAGEQUERY")).get(hm.get("tblAccTypeInfo.AccName"));
				hm.put("tblAccTypeInfo.AccName",loc.get(this.getLocale(request).toString()));
			}
			/**
			 * 处理子表的数据
			 */
			ArrayList rowsList = new ArrayList();
			request.setAttribute("result",rowsList);
			HashMap exportValues = new HashMap();
			exportValuesList.add(exportValues);
			exportList = new ArrayList<ExportField>();
			ExportData.export(request, exportList, exportValues);
		}

		FileOutputStream fos = new FileOutputStream(fileName);
		result = ExportData.billExport(fos, tableDisplay, exportList, exportValuesList, null);
		fos.close();
		/* 导出成功 */
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String backURL = "/EmployeeDepartmentAction.do";
			EchoMessage.success().add(
					getMessage(request, "com.export.success") + "<a href='/ReadFile?tempFile=export&fileName=" + GlobalsTool.encode(tableDisplay + ".xls")
							+ "'>" + getMessage(request, "com.export.success.download") + "</a>" + getMessage(request, "com.export.success.over") + fileName)
					.setBackUrl(backURL).setNotAutoBack().setAlertRequest(request);
		} else {
			EchoMessage.error().add(getMessage(request, "com.export.failure")).setNotAutoBack().setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
}
