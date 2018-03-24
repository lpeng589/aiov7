package com.koron.oa.individual.workPlan;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.Department;
import com.koron.oa.bean.DirectorySetting;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.OAWorkPopedomeBean;
import com.koron.oa.publicMsg.advice.OAAdviceMgt;
import com.koron.oa.util.EmployeeMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class OAWorkPlanPopedomeAction extends MgtBaseAction {
	
	OAWorkPopedomeMgt mgt=new OAWorkPopedomeMgt();
	private OAAdviceMgt adviceMgt = new OAAdviceMgt();
	EmployeeMgt empMgt = new EmployeeMgt();
	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response) ;
			break;
		// 新增前的准备
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response) ;
			break;
		// 修改前的准备
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response) ;
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response) ;
			break;
		// 删除
		case OperationConst.OP_DELETE:
			 forward = deletes(mapping, form, request, response);
			break;
		default:
			//forward = goMain(mapping, form, request, response); // 我的工作计划首页
			if("type".equals(request.getParameter("type"))){
				forward=getDeptTree(mapping, form, request, response);
			}else if("eventWorkList".equals(request.getParameter("opType"))){
				forward = eventWorkList(mapping, form, request, response);//事件计划能查看的人。
			}else if("detail".equals(request.getParameter("detail"))){
				forward =updatePrepare(mapping, form, request, response) ;//详情
			}else{
				forward=query(mapping, form, request, response);
			}
		}
		return forward;
	
	}
	
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// Result seePersons= mgt.querySeePopedome();
		Result rs= mgt.query();
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mopSetting = (MOperation) loginBean.getOperationMap().get("/OAWorkPopedomeActon.do");
		request.setAttribute("add",mopSetting == null ? false : mopSetting.add()); // 增加权限
		request.setAttribute("del",mopSetting == null ? false : mopSetting.delete()); // 删权限
		request.setAttribute("upd",mopSetting == null ? false : mopSetting.update()); // 改权限
		List<OAWorkPopedomeBean>   workpopedomes=null;
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		 workpopedomes = (List<OAWorkPopedomeBean>) rs.getRetVal();
			for (OAWorkPopedomeBean wp : workpopedomes) {
				String classCodes = wp.getBySeeDeptOfClassCode();
				if (StringUtils.isNotBlank(classCodes)) {
					// 查询通知部门
					List<Department> targetDept = new ArrayList<Department>();
					String[] popedomDeptIds = classCodes.split(",");
					for (String classCode : popedomDeptIds) {
						Department dept = adviceMgt
								.getDepartmentByClassCode(classCode);

						if (dept != null) {
							targetDept.add(dept);
						}
					}
					wp.setBySeeDepts(targetDept);
				}

				String usersId = wp.getBySeeUserID();
				if (StringUtils.isNotBlank(usersId)) {
					// 查询通知员工
					List<Employee> targetUsers = new ArrayList<Employee>();
					String[] userArr = usersId.split(",");

					for (String uId : userArr) {
						Employee employee = empMgt.getEmployeeById(uId);
						if (employee != null) {
							targetUsers.add(employee);
						}
					}
					wp.setBySeeUserNames(targetUsers);
				}
				String seeId = wp.getSeePersonId();
				if (StringUtils.isNotBlank(seeId)) {
					// 查询通知员工
					List<Employee> targetUsers = new ArrayList<Employee>();
					String[] userArr = seeId.split(",");

					for (String uId : userArr) {
						Employee employee = empMgt.getEmployeeById(uId);
						if (employee != null) {
							targetUsers.add(employee);
						}
					}
					wp.setSeePersonNames(targetUsers);
				}

				String empGroups = wp.getBySeeEmpGroup();
				// 查找职员分组
				if (StringUtils.isNotBlank(empGroups)) {
					List<String[]> listEmpGroup = new ArrayList<String[]>();
					String[] popedomEmpGroupIds = empGroups.split(",");
					for (String empGroup : popedomEmpGroupIds) {
						Result r = empMgt.selectEmpGroupById(empGroup);
						if (r.getRetVal() != null) {
							listEmpGroup.add((String[]) r.getRetVal());
						}
					}
					wp.setBySeeEmpGroups(listEmpGroup);
				}
			}
		}
		request.setAttribute("workpopedomes",workpopedomes);
		return getForward(request, mapping, "showList");
	}
	
	
	//添加之前
	private ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("operation", OperationConst.OP_ADD);
		return getForward(request, mapping, "addPrepare");
	}
	
	//添加
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String seePersons=request.getParameter("seePersonIds");
		String desContent=request.getParameter("desContent");
		String seeType=request.getParameter("seeType");
		String popedomUserIds=request.getParameter("popedomUserIds");
		String popedomDeptIds=request.getParameter("popedomDeptIds");
		String empGroupId=request.getParameter("empGroupId");
		
		OAWorkPopedomeBean bean=new OAWorkPopedomeBean(); 
		bean.setId(IDGenerater.getId());
		bean.setSeePersonId(seePersons);
		bean.setDesContent(desContent);
		bean.setSeeType(seeType);
		bean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		bean.setBySeeUserID(popedomUserIds);
		bean.setBySeeDeptOfClassCode(popedomDeptIds);
		bean.setBySeeEmpGroup(empGroupId);
		Result rs = mgt.addSeePerson(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
	        request.setAttribute("noAlert", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	//修改之前
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("operation", OperationConst.OP_UPDATE);
		String seeId = request.getParameter("seeId");
		String detail=request.getParameter("detail"); //如果是详情。
		Result rs =mgt.load(seeId, OAWorkPopedomeBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {

			OAWorkPopedomeBean wp = (OAWorkPopedomeBean) rs.getRetVal();
				String classCodes = wp.getBySeeDeptOfClassCode();
	
				if (StringUtils.isNotBlank(classCodes)) {
					// 查询通知部门
					List<Department> targetDept = new ArrayList<Department>();
					String[] popedomDeptIds = classCodes.split(",");
					for (String classCode : popedomDeptIds) {
						Department dept = adviceMgt.getDepartmentByClassCode(classCode);
						if (dept != null) {
							targetDept.add(dept);
						}
					}
					wp.setBySeeDepts(targetDept);
				}
				String seeUserId = wp.getSeePersonId();
				if (StringUtils.isNotBlank(seeUserId)) {
					// 查询通知员工
					List<Employee> targetUsers = new ArrayList<Employee>();
					String[] userArr = seeUserId.split(",");

					for (String uId : userArr) {
						Employee employee = empMgt.getEmployeeById(uId);
						if (employee != null) {
							targetUsers.add(employee);
						}
					}
					wp.setSeePersonNames(targetUsers);
				}
				String usersId = wp.getBySeeUserID();
				if (StringUtils.isNotBlank(usersId)) {
					// 查询通知员工
					List<Employee> targetUsers = new ArrayList<Employee>();
					String[] userArr = usersId.split(",");
	
					for (String uId : userArr) {
						Employee employee = empMgt.getEmployeeById(uId);
						if (employee != null) {
							targetUsers.add(employee);
						}
					}
					wp.setBySeeUserNames(targetUsers);
				}
	
				String empGroups = wp.getBySeeEmpGroup();
				// 查找职员分组
				if (StringUtils.isNotBlank(empGroups)) {
					List<String[]> listEmpGroup = new ArrayList<String[]>();
					String[] popedomEmpGroupIds = empGroups.split(",");
					for (String empGroup : popedomEmpGroupIds) {
						Result r = empMgt.selectEmpGroupById(empGroup);
						if (r.getRetVal() != null) {
							listEmpGroup.add((String[]) r.getRetVal());
						}
					}
					wp.setBySeeEmpGroups(listEmpGroup);
				}
				request.setAttribute("workpopedomes", wp);
				request.setAttribute("seeId", seeId);
				request.setAttribute("detail", detail);
			} else {
				EchoMessage.error().add(
						getMessage(request, "com.revert.to.failure"))
						.setAlertRequest(request);
				return getForward(request, mapping, "message");
			}	
		return getForward(request, mapping, "addPrepare");
	}
	
	//修改
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String seePersons=request.getParameter("seePersonIds");
		String desContent=request.getParameter("desContent");
		String seeType=request.getParameter("seeType");
		String popedomUserIds=request.getParameter("popedomUserIds");
		String popedomDeptIds=request.getParameter("popedomDeptIds");
		String empGroupId=request.getParameter("empGroupId");
		String seeId = request.getParameter("seeId");
		// 给bean赋值
		OAWorkPopedomeBean bean = (OAWorkPopedomeBean) mgt.load(seeId,
				OAWorkPopedomeBean.class).getRetVal();
		bean.setSeePersonId(seePersons);
		bean.setDesContent(desContent);
		bean.setSeeType(seeType);
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		bean.setBySeeUserID(popedomUserIds);
		bean.setBySeeDeptOfClassCode(popedomDeptIds);
		bean.setBySeeEmpGroup(empGroupId);
		Result rs_update = mgt.updateSeePerson(bean);
		String success="";
		if (rs_update.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			success="success";
			System.out.println("修改成功！");
			request.setAttribute("dealAsyn", "true");
		    request.setAttribute("noAlert", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		} else {
			System.out.println("修改失败！");
		}
		request.setAttribute("msg", success);
		return getForward(request, mapping, "alert");
	}
	
	//删除
	private ActionForward deletes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seeId = request.getParameter("seeId");
		String seeIds[]=seeId.split(",");
		Result  rs=mgt.deleteBean(seeIds);
		String success="";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			success="success";
		}
		request.setAttribute("msg", success);
		return getForward(request, mapping, "blank");
		//return query(mapping, form, request, response);
	}
	
	public ActionForward getDeptTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		try{
			String	UserId = this.getLoginBean(request).getId();
			Result popedomeList=mgt.queryDeptTree(UserId,"1");
			LoginBean loginBean = this.getLoginBean(request);
			MOperation mopSetting = (MOperation) loginBean.getOperationMap().get("/OAWorkPopedomeActon.do");
			request.setAttribute("add",mopSetting == null ? false : mopSetting.add()); // 增加权限
			request.setAttribute("del",mopSetting == null ? false : mopSetting.delete()); // 删权限
			request.setAttribute("upd",mopSetting == null ? false : mopSetting.update()); // 改权限
			String query=(mopSetting == null ? false : mopSetting.query())+"";
			String add= (mopSetting == null ? false : mopSetting.add())+"";
			String del= (mopSetting == null ? false : mopSetting.delete())+"";
			String upd= (mopSetting == null ? false : mopSetting.update())+"";
			//List<String[]> groupCode=mgt.getGroupClassCode(by);
			List<OAWorkPopedomeBean>  bySeeList=(List<OAWorkPopedomeBean>)popedomeList.getRetVal();
			//List<String[]> groupCode=mgt.getGroupClassCode(by);
			if(popedomeList.retCode==ErrorCanst.DEFAULT_SUCCESS){
				String strSQL="";
				String empSQL="";
				String groupSQL="";
				String deptSQL="";
				StringBuffer strTree=null;
				if(bySeeList!=null && bySeeList.size()>0 && !"1".equals(UserId)){
					strSQL = " and (" ;
					for (OAWorkPopedomeBean wp : bySeeList) {
						if(wp.getBySeeUserID()!=null && wp.getBySeeUserID().length()>0){
							empSQL += " b.id in (" ;
							for(String strId : wp.getBySeeUserID().split(",")){
								empSQL += "'"+strId+"'," ;
							}
							empSQL = empSQL.substring(0, empSQL.length()-1) ;
							empSQL += ") or " ;
							strSQL += empSQL ;
						}
						if(wp.getBySeeDeptOfClassCode()!=null && wp.getBySeeDeptOfClassCode().length()>0 ){
							for(String strId : wp.getBySeeDeptOfClassCode().split(",")){
								deptSQL += " a.classCode like '"+strId+"%' or " ;
							}
							//deptSQL = deptSQL.substring(0, deptSQL.length()-3) ;
							strSQL += deptSQL ;
						}
						if(wp.getBySeeEmpGroup()!=null && wp.getBySeeEmpGroup().length()>0){
							for(String strId : wp.getBySeeEmpGroup().split(",")){
								List<String> groupCode=(List<String>)mgt.getGroupClassCode(strId).getRetVal();
								deptSQL+="(";
								for(int i=0;i<groupCode.size();i++){
									String strList[]=groupCode.get(i).split("&&");
									deptSQL += " a.classCode like '"+strList[0]+"%' or " ;
								}
								if(deptSQL.endsWith("or ")){
									deptSQL = deptSQL.substring(0,deptSQL.length()-3) ;
								}
								deptSQL+=") and b.id in (";
								for(int i=0;i<groupCode.size();i++){
									String strList[]=groupCode.get(i).split("&&");
									deptSQL += " '"+strList[1]+"'," ;
								}
								if(deptSQL.endsWith(",")){
									deptSQL = deptSQL.substring(0,deptSQL.length()-1) ;
								}
								deptSQL+=") or ";
								
							}
							strSQL += deptSQL ;
						}
					}
					if(strSQL.endsWith("or ")){
						strSQL = strSQL.substring(0,strSQL.length()-3) ;
					}
					strSQL += ")" ;
				}
				if((strSQL!=null && strSQL.trim().length()>0) || "1".equals(getLoginBean(request).getId())){
					Result rss = new OADateWorkPlanMgt().queryEmployeeByDept(strSQL);
					 strTree = new StringBuffer();
					if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						HashMap<String, List<String[]>> deptMap = (HashMap<String, List<String[]>>) rss.retVal ;
						strTree.append("[") ;				
						int i=0;
						for(String key : deptMap.keySet()){		
							
							List<String[]> empList = deptMap.get(key) ;							
							if(empList==null || empList.size()==0) continue ;																
							strTree.append("{id:'',name:'"+key+"',isParent:true,nodes:[") ;							
							for(String[] emp : empList){							
								strTree.append("{id:'"+emp[3]+"',name:'"+emp[2]+"'},") ;
							}
							strTree.delete(strTree.length()-1, strTree.length()) ;
							strTree.append("]},");
							i++;
						}
						if(strTree.toString().endsWith(",")){
							strTree.delete(strTree.length()-1, strTree.length()) ;
						}
						strTree = strTree.append("]");
					}
					//reponse(request, response, strTree.toString());
					request.setAttribute("msg", strTree.toString());
				}
				if(strTree == null){
					request.setAttribute("msg", "");
					request.getSession().setAttribute("msg", "");
				}else{
					
					String str=strTree.toString()+"||"+query+"||"+add+"||"+del+"||"+upd;
					request.setAttribute("msg", str);
					request.getSession().setAttribute("msg", strTree.toString());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return getForward(request, mapping, "blank");
	}
	
	private ActionForward eventWorkList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		OADayWorkPlanSearchForm myForm = (OADayWorkPlanSearchForm)form;
		String	UserId = this.getLoginBean(request).getId();
		Result popedomeList=mgt.queryDeptTree(UserId,"2");
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPlanQueryAction.do?operation=4&opType=eventWorkList");
		/*
		MOperation mopSetting = (MOperation) loginBean.getOperationMap().get("/OAWorkPopedomeActon.do");
		request.setAttribute("add",mopSetting == null ? false : mopSetting.add()); // 增加权限
		request.setAttribute("del",mopSetting == null ? false : mopSetting.delete()); // 删权限
		request.setAttribute("upd",mopSetting == null ? false : mopSetting.update()); // 改权限
		String query=(mopSetting == null ? false : mopSetting.query())+"";
		String add= (mopSetting == null ? false : mopSetting.add())+"";
		String del= (mopSetting == null ? false : mopSetting.delete())+"";
		String upd= (mopSetting == null ? false : mopSetting.update())+"";
		*/
	 
		List<OAWorkPopedomeBean>  bySeeList=(List<OAWorkPopedomeBean>)popedomeList.getRetVal();
	 
		 String scopeSQL="";
		if(popedomeList.retCode==ErrorCanst.DEFAULT_SUCCESS){
			
			 if(bySeeList!=null && bySeeList.size()>0 && !"1".equals(UserId)){
				scopeSQL = " or " ;
				for (OAWorkPopedomeBean wp : bySeeList) {
					if(wp.getBySeeUserID()!=null && wp.getBySeeUserID().length()>0){
						String empSQL = "" ;
						for(String strId : wp.getBySeeUserID().split(",")){
							empSQL += "'"+strId+"'," ;
						}
						empSQL = empSQL.substring(0, empSQL.length()-1) ;
						scopeSQL += " a.employeeId in ("+empSQL+") or " ;
					}
					if(wp.getBySeeDeptOfClassCode()!=null && wp.getBySeeDeptOfClassCode().length()>0 ){
						
						String deptSQL = "" ;
						for(String strId : wp.getBySeeDeptOfClassCode().split(",")){
							deptSQL += " a.departmentCode like '"+strId+"%' or " ;
						}
						if(deptSQL.endsWith("or ")){
							deptSQL = deptSQL.substring(0,deptSQL.length()-3) ;
						}
						scopeSQL += deptSQL +" or ";
					}
					if(wp.getBySeeEmpGroup()!=null && wp.getBySeeEmpGroup().length()>0){
						String deptSQL = "" ;
						for(String strId : wp.getBySeeEmpGroup().split(",")){
							List<String> groupCode=(List<String>)mgt.getGroupClassCode(strId).getRetVal();
							
							for(int i=0;i<groupCode.size();i++){
								System.out.println(groupCode.get(i));
								deptSQL += " a.departmentCode like '"+groupCode.get(i)+"%' or " ;
							}
						}
						scopeSQL += deptSQL ;
					}
				}
				if(scopeSQL.endsWith("or ")){
					scopeSQL = scopeSQL.substring(0,scopeSQL.length()-3) ;
				}
			}
		}
		Result rs =  new OADateWorkPlanMgt().getEventWorkPlan(myForm.getDepartment(), myForm.getEmployee(), myForm.getTitle(), 
				myForm.getStatusId(), myForm.getBeginDate(), myForm.getEndDate(),getLoginBean(request).getId(),scopeSQL,myForm.getTypeId());
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //查询成功
            request.setAttribute("result", rs.retVal);
            request.setAttribute("pageBar", pageBar(rs, request));
            
            rs =  new OADateWorkPlanMgt().getPlanAssociate();
			request.setAttribute("associate", rs.retVal);
        } else {
            //查询失败
            EchoMessage.error().add(getMessage(request, "common.msg.error")).
                setRequest(request);
            return getForward(request, mapping, "message");
        }
		request.setAttribute("MOID", mop.moduleId);	
		request.setAttribute("OADayWorkPlanSearchForm", myForm);
		return getForward(request, mapping, "EventWorkList");
	}
	
	
	
	//添加之前
	private ActionForward settingIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		  //System.err.println("Hello");
		return getForward(request, mapping, "index");
	}
	
}

