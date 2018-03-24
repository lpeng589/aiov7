package com.menyi.aio.web.systemset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.*;
import com.menyi.web.util.*;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import com.menyi.aio.bean.RoleModuleBean;
import java.text.SimpleDateFormat;
import com.menyi.aio.bean.RoleScopeBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advance.AdvanceAction;
import com.menyi.aio.web.alert.AlertSetMgt;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;

import java.io.FileNotFoundException;
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
public class SystemSetAction extends MgtBaseAction {

	SystemSetMgt mgt = new SystemSetMgt();
	AlertSetMgt alertSetMgt = new AlertSetMgt();
	
	public SystemSetAction() {
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

		request.setAttribute("page", request.getParameter("page"));
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	protected ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginBean loginBean = getLoginBean(request);
		if("true".equals(request.getParameter("save"))){
			MessageResources resources = null;
			Object obj = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			if (obj instanceof MessageResources) {
			    resources = (MessageResources) obj;
			}
			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
			MOperation mop = GlobalsTool.getMOperationMap(request) ;
			Result rs = null;
			String allMsg= "";
			String deploys[] = this.getParameters("sysDeploy", request); 
			//查询修改的参数
			
			boolean hasSuccssfull = false;
			for(String deploy:deploys){
				String sysCode = deploy.substring(0,deploy.indexOf("__"));
				String oldValue= deploy.substring(deploy.indexOf("__")+"__".length());
				String newValue = request.getParameter(sysCode);
				if(newValue==null){
					newValue = "false";
				}
				
				if(!oldValue.equals(newValue)){					
					System.out.println(sysCode+":oldValue="+oldValue+";newValue="+newValue);
					HashMap sv = new HashMap();					
					sv.put("checkPersons", "");					
					sv.put("lastUpdateTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					sv.put("SCompanyID", "");
					sv.put("workFlowNodeName", "finish");
					sv.put("workFlowNode", "-1");
					sv.put("classCode", "");
					sv.put("lastUpdateBy", loginBean.getId());
					sv.put("finishTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					
					//查询老数据
					sv.put("SysCode", sysCode);
					rs = mgt.querySysDeployByCode(sysCode);
					if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS || rs.retVal == null){
						EchoMessage.error().add("修改失败").setAlertRequest(request);
						return getForward(request, mapping, "alert");
					}
					sv.putAll((HashMap)rs.retVal);					
					sv.put("Setting", newValue);
					sv.put("SysName", KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
							(Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),(String)sv.get("SysName")));
					String sysName = ((KRLanguage)sv.get("SysName")).get(this.getLocale(request).toString());
					
					rs = new DynDBManager().update("tblSysDeploy", BaseEnv.tableInfos, sv, loginBean.getId(), 
							"true".equals("false")?"tblSysDeploy_update:tblSysDeploy_System_Restart;":"tblSysDeploy_update:tblSysDeploy_System_NoRestart;", 
									resources, this.getLocale(request), "", loginBean, null, props);
					if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode==ErrorCanst.RET_DEFINE_SQL_ALERT) {
						hasSuccssfull = true;
						allMsg +=sysName+":"+"修改成功<br/>";
						new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),(String)((KRLanguage)sv.get("SysName")).get(this.getLocale(request).toString()), "tblSysDeploy", "系统配置","");
					}else if(rs.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
		            	//自定义配置需要用户确认
						allMsg +=sysName+":"+"修改失败弱，弹出可选框<br/>";
		            }else {
		            	SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
		            	allMsg +=sysName+":"+"修改失败"+saveErrrorObj.getMsg()+"<br/>";
		            }
				}				
			}

			
			/**
			 * 修改预警
			 */
			String[] alertParameter = this.getParameters("sysAlert", request);
			for(String s : alertParameter){
				//s = id
				String status = request.getParameter(s+"_status");													//最新预警状态
				int alertStatus = status == null ? 1 : Integer.parseInt(status);
				String[] alertTypeStr = request.getParameterValues(s+"_alertType");									//提醒方式（邮件，短信，通知消息）
				String alertType = "";
				if(alertTypeStr != null ){
					for(String str : alertTypeStr){
						alertType += str+",";
					}
				}
				String actionTime = request.getParameter(s+"_actionTime");											//执行时间
				String actionFrequency = request.getParameter(s+"_actionFrequency");								//执行频率
				String popedomUserIds = request.getParameter(s+"_popedomUserIds");									//提醒对象
				String condition = request.getParameter(s+"_condition");											//条件
				
				String alert_name = request.getParameter(s+"_name");												//预警名称
				String msg = "修改";
				String oldStatus = request.getParameter(s+"_oldStatus");											//以前的预警状态
				int oldAlertStatus = oldStatus == null ? 1 : Integer.parseInt(oldStatus);
				String oldAlertType = request.getParameter(s+"_oldAlertType");
				String oldActionTime = request.getParameter(s+"_oldActionTime");
				String oldActionFrequency = request.getParameter(s+"_oldActionFrequency");
				String oldPopedomUserIds = request.getParameter(s+"_oldPopedomUserIds");
				boolean falg = false;
				if(alertStatus != oldAlertStatus){
					if(alertStatus == 0){
						msg = "启用";
					}else{
						msg = "停用";
					}
					falg = true;
				}
				if(!alertType.equals(oldAlertType) || !actionTime.equals(oldActionTime) || !actionFrequency.equals(oldActionFrequency)
						|| !popedomUserIds.equals(oldPopedomUserIds)){
					falg = true;
				}
				//做过修改
				if(falg){
					//加载信息
					rs = alertSetMgt.loadBean(s);
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						SysAlertBean bean = (SysAlertBean)rs.retVal;
						bean.setStatus(alertStatus);
						bean.setActionFrequency(actionFrequency==null?0:Integer.parseInt(actionFrequency));
						bean.setActionTime(actionTime);
						bean.setAlertType(alertType);
						if(condition != null && condition.indexOf("=")==-1){
							//不存在格式为（属性名=属性值）
							condition = bean.getAlertCode().substring(bean.getAlertCode().indexOf("_")+1)+"="+condition;
						}
						bean.setCondition(condition);
						/* 提示用户 */
						List detList = new ArrayList(); 
						if(popedomUserIds != null && !"".equals(popedomUserIds)){
							String[] userIds = popedomUserIds.split(",");
							for(String userId : userIds){
								if(!"".equals(userId)){
									SysAlertDetBean detBean = new SysAlertDetBean();
									detBean.setId(IDGenerater.getId());
									detBean.setSysAlertBean(bean);
									detBean.setAlertUser(userId);
									detList.add(detBean);
								}
							}
						}
						/* 修改下次时间 */
						bean.setNextAlertTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+" "+actionTime+":00:00");
						
						bean.setSysAlertDetBeanList(detList);
						rs = alertSetMgt.updateAlertSet(bean,"","","");
						if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
							allMsg +=alert_name+":"+"修改失败"+"<br/>";
						}else{
							hasSuccssfull = true;
							allMsg +=alert_name+":"+"修改成功"+"<br/>";
							new DynDBManager().addLog(1, loginBean.getId(), loginBean.getName(), 
									loginBean.getSunCmpClassCode(),msg+alert_name,
									"tblSysAlert", "预警配置","");
						}
					}else{
						allMsg +=alert_name+":"+"修改失败"+"<br/>";
					}
				}
			}
			if(hasSuccssfull){
                //刷新内存
                AdvanceAction.refreshMem(request);
			}
			if(allMsg == null || allMsg.length() ==0){
				allMsg = "未修改任何配置";
			}
			EchoMessage.success().add(allMsg).setBackUrl("/SystemSetAction.do").setNotAutoBack().setAlertRequest(request);
			
			return getForward(request, mapping, "alert");
		}
		Result rs = new SysAccMgt().getCurrPeriod(loginBean.getSunCmpClassCode());// ////////
		request.getSession().setAttribute("NowPeriod", ((AccPeriodBean) rs.getRetVal()).getAccMonth());
		rs = mgt.querySysDeploy(this.getLocale(request).toString());
		HashMap<String,ArrayList<Object[]>> groupmap = new HashMap();
		if(rs.retVal != null){
			ArrayList<Object[]> list = (ArrayList<Object[]>)rs.retVal;			
			
			//配置数据
			for(Object[] os:list){ 
				if(!os[0].equals("0") && !GlobalsTool.getMainModule().contains(os[0].toString())){
					continue;
				}				
				
				if(groupmap.get(os[1])==null){
					groupmap.put(os[1].toString(), new ArrayList());
				}
				ArrayList<Object[]> gl = groupmap.get(os[1].toString());
				gl.add(os);
			}
			
			//分组数据
			Result groupRs = mgt.querySysGroup(this.getLocale(request).toString());
			if(groupRs.retVal != null){
				ArrayList<Object[]> groupListt = (ArrayList<Object[]>)groupRs.retVal;
				ArrayList<Object[]> groupFList = new ArrayList<Object[]>();
				//取一级分组
				ArrayList<Object[]> group1List = new ArrayList<Object[]>();
				//过滤没有数据的项目
				for(Object[] os:groupListt){ 
					if(os[0].toString().length() == 5){
						group1List.add(os);
					}
					if(groupmap.get(os[0].toString()) != null){
						groupFList.add(os);
					}
				}
				//过滤没有下级分组的数据
				ArrayList<Object[]> delgroup1List = new ArrayList<Object[]>();
				for(Object[] os1:group1List){ 
					boolean found = false;
					for(Object[] os:groupFList){
						if(os[0].toString().startsWith(os1[0].toString()) && os[0].toString().length() >os1[0].toString().length()){
							found = true;
							break;
						}
					}
					if(!found){
						delgroup1List.add(os1);
						
					}
				}
				group1List.removeAll(delgroup1List);
				request.setAttribute("groupOneList", group1List);
				request.setAttribute("groupList", groupFList);
			}
			
		}
		request.setAttribute("groupmap", groupmap);
		
		/**
		 * 查询所有的预警设置
		 */
		rs = alertSetMgt.queryAlertData(this.getLocale(request).toString());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			ArrayList<Object[]> list = (ArrayList<Object[]>)rs.retVal;
			
			if(list.size()==0){
				request.setAttribute("noData", "true");
			}
			/* 查询预警分组 */
			rs = alertSetMgt.queryAlertGroup();
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				List groupList = (ArrayList)rs.retVal;
				for(int i=0;i<groupList.size();i++){
					Object[] groupStr= (Object[])groupList.get(i);
					List alertList = new ArrayList();
					for(int j=0;j<list.size();j++){
						Object[] obj = list.get(j);
						if(obj[3] !=null && groupStr[0].equals(obj[3])){
							alertList.add(obj);
						}
					}
					groupStr[2] = alertList;
				}
				request.setAttribute("alertGroupList", groupList);
			}
		}
		
		//获取企业号配置
		Result wxRs = mgt.getWxSet();
		request.setAttribute("wxset", wxRs.retVal);
		return getForward(request, mapping, "systemSet");
	}

}
