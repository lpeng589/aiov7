package com.menyi.aio.web.alert;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.*;

/**
 * 
 * <p>Title:预警设置Action</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class AlertSetAction extends MgtBaseAction{
	
	AlertSetMgt mgt = new AlertSetMgt();
	
	/**
	 * exe 控制器入口函数
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		
		String optype = request.getParameter("optype");				//处理一些特殊的操作
		
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			//增加前
			forward = addPreAlert(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			//增加
			forward = addAlert(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			//修改前
			forward = updatePreAlert(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			//修改
			if(optype != null && "openOrStop".equals(optype)){
				//预警显示或者隐藏
				forward = openOrStop(mapping, form, request, response);
			}else{
				forward = updateAlert(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUERY:
			//查询列表
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			//详情
			forward = detailAlert(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			//删除
			forward = deleteAlert(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	
	/**
	 * 查询预警列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String optype = request.getParameter("optype");			//类型处理
		Result rs = null;
		if(optype != null && "save".equals(optype)){
			/**
			 * 修改预警
			 */
			String[] alertParameter = this.getParameters("sysAlert", request);
			for(String s : alertParameter){
				//s = id
				String status = request.getParameter(s+"_status");													//预警状态
				int alertStatus = status ==null?1:Integer.parseInt(status);
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
				//加载信息
				rs = mgt.loadBean(s);
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
					bean.setSysAlertDetBeanList(detList);
					rs = mgt.updateAlertSet(bean,"","","");
					if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
						EchoMessage.error().add("修改失败").setAlertRequest(request);
						return getForward(request, mapping, "alert");
					}
				}else{
					EchoMessage.error().add("修改失败").setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}
			EchoMessage.success().add("修改成功").setBackUrl("/AlertSetAction.do").setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}else if(optype != null && "query".equals(optype)){
			/**
			 * 查询预警高级设置列表
			 */
			String searType = request.getParameter("searType");									//搜索类型
			String searValue = request.getParameter("searValue");								//搜索值
			rs = mgt.queryAlertSet(this.getLocale(request).toString(),searType,searValue);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//查询成功
				request.setAttribute("alertSetList", rs.retVal);
				rs = mgt.queryAlertGroup();
				request.setAttribute("groupList", rs.retVal);
			}
			request.setAttribute("searType", searType);
			request.setAttribute("searValue", searValue);
			return getForward(request, mapping, "alertSetList");
		}
		
		/**
		 * 查询所有的预警设置
		 */
		rs = mgt.queryAlertData(this.getLocale(request).toString());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			ArrayList<Object[]> list = (ArrayList<Object[]>)rs.retVal;
			
			if(list.size()==0){
				request.setAttribute("noData", "true");
			}
			/* 查询预警分组 */
			rs = mgt.queryAlertGroup();
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
				request.setAttribute("groupList", groupList);
			}
		}
		return getForward(request, mapping, "alertSet");
	}
	
	/**
	 * 添加预警设置前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addPreAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/**
		 * 查询所有预警的分组
		 */
		Result rs = mgt.queryAlertGroup();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("groupList", rs.retVal);
		}
		return getForward(request, mapping, "dealAlertSet");
	}
	
	/**
	 * 添加预警设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		LoginBean lg = this.getLoginBean(request);
		
		AlertSetForm alertForm = (AlertSetForm)form;
		String[] alertTypeStr = request.getParameterValues("alertType");
		String alertType = "";
		if(alertTypeStr != null ){
			for(String s : alertTypeStr){
				alertType += s+",";
			}
		}
		//条件
		String condition = alertForm.getCondition();
		String alertCode = "";
		if(condition.length()>0 && condition.indexOf("=")>0){
			alertCode = (int)(Math.random()*100)+"_"+condition.substring(0,condition.indexOf("="));
		}
		
		SysAlertBean bean = new SysAlertBean();
		bean.setSqlDefineName("");
		bean.setId(IDGenerater.getId());																			//id
		bean.setCreateby(lg.getId());																				//创建者
		bean.setCreatetime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		bean.setLastUpdateBy(lg.getId());
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		bean.setStatusId(0);
		bean.setActionFrequency(alertForm.getActionFrequency());													//预警执行频率
		bean.setActionTime(alertForm.getActionTime());																//开始时间
		bean.setAlertName(alertForm.getAlertName());																//预警名称（多语言）
		bean.setAlertType(alertType);																				//提醒方式
		bean.setCondition(condition);																				//条件
		bean.setAlertCode(alertCode);																				//标识
		bean.setModelId(alertForm.getModelId());																	//对应报表的AccNumber
		bean.setModuleType(alertForm.getModuleType());																//预警所属组
		bean.setStatus(alertForm.getStatus());																		//状态（0启用，1不启用）
		bean.setIsHidden(alertForm.getIsHidden());																	//是否显示或者隐藏
		bean.setConditionStatus(alertForm.getConditionStatus());													//条件状态（显示或者隐藏）
		bean.setBewrite(alertForm.getBewrite());																	//条件概述（多语言） 
		bean.setRemark(alertForm.getRemark());																		//描述
		
		//下一次提醒时间
		String nextAlertTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+" "+alertForm.getActionTime()+":00:00";
		bean.setNextAlertTime(nextAlertTime);
		
		/* 提示用户 */
		List detList = new ArrayList(); 
		String popedomUserIds = request.getParameter("popedomUserIds");
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
		bean.setSysAlertDetBeanList(detList);
		/**
		 * 添加
		 */
		Result rs = mgt.addAlertSet(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//添加成功
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(
                    request, "common.msg.addSuccess")).setBackUrl("/AlertSetAction.do?operation=4&optype=query").setAlertRequest(request);
			String alertName =alertForm.getAlertName();
			String locale = this.getLocale(request).toString();
            if(alertName != null &&alertName.length() > 0){
     		   int pos = alertName.indexOf(locale)+locale.length()+1;
     		  alertName = alertName.substring(pos,alertName.indexOf(";",pos));  
     	   }
			new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
					alertName, "tblSysAlert", "预警项目设置","");
		}
		return getForward(request, mapping, "message");
	}
	
	
	/**
	 * 修改预警设置前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updatePreAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/* 预警ID */
		String keyId = request.getParameter("keyId");
		
		/**
		 * 查询详情
		 */
		Result rs = mgt.loadAlertSet(keyId,this.getLocale(request).toString());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("bean", rs.retVal);
			rs = mgt.queryAlertGroup();
			request.setAttribute("groupList", rs.retVal);
		}
		return getForward(request, mapping, "dealAlertSet");
	}
	
	
	/**
	 * 修改预警设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updateAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/* 用户登陆的信息 */
		LoginBean lg = this.getLoginBean(request);
		
		/* 对指定的数据进行特殊处理 */
		AlertSetForm alertForm = (AlertSetForm)form;
		String[] alertTypeStr = request.getParameterValues("alertType");
		String alertType = "";
		if(alertTypeStr != null ){
			for(String s : alertTypeStr){
				alertType += s+",";
			}
		}
		
		/* 把数据保存到Bean中 */
		String condition = alertForm.getCondition();
		String alertCode = "";
		if(condition.length()>0 && condition.indexOf("=")>0){
			alertCode = (int)(Math.random()*100)+"_"+condition.substring(0,condition.indexOf("="));
		}
		
		SysAlertBean bean = new SysAlertBean();
		bean.setSqlDefineName("");
		bean.setId(alertForm.getId());																					//id
		bean.setCreateby(alertForm.getCreateby());
		bean.setCreatetime(alertForm.getCreatetime());
		bean.setLastUpdateBy(lg.getId());
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		bean.setActionFrequency(alertForm.getActionFrequency());													//预警执行频率
		bean.setActionTime(alertForm.getActionTime());																//开始时间
		bean.setAlertName(alertForm.getAlertName());																//预警名称（多语言）
		bean.setAlertType(alertType);																				//提醒方式
		bean.setCondition(condition);																				//条件
		bean.setAlertCode(alertCode);																				//标识
		bean.setModelId(alertForm.getModelId());																	//对应报表的AccNumber
		bean.setModuleType(alertForm.getModuleType());																//预警所属组
		bean.setStatus(alertForm.getStatus());																		//状态（0启用，1不启用）
		bean.setIsHidden(alertForm.getIsHidden());																	//是否显示或者隐藏
		bean.setConditionStatus(alertForm.getConditionStatus());													//条件状态（显示或者隐藏）
		bean.setBewrite(alertForm.getBewrite());																	//条件概述（多语言） 
		bean.setRemark(alertForm.getRemark());																		//描述
		
		String nextAlertTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+" "+alertForm.getActionTime()+":00:00";
		bean.setNextAlertTime(nextAlertTime);
		
		/* 提示用户 */
		List detList = new ArrayList(); 
		String popedomUserIds = request.getParameter("popedomUserIds");
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
		bean.setSysAlertDetBeanList(detList);
		String old_alertName = request.getParameter("old_alertName");
		String old_bewrite = request.getParameter("old_bewrite");
		/* 修改 */
		Result rs = mgt.updateAlertSet(bean,old_alertName,old_bewrite,"alertItemSet");
		request.setAttribute("dealAsyn", "true");
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/* 修改成功 */
			EchoMessage.success().add(getMessage(
	                request, "common.msg.updateSuccess")).setBackUrl("/AlertSetAction.do?operation=4&optype=query").setAlertRequest(request);
			String alertName =alertForm.getAlertName();
			String locale = this.getLocale(request).toString();
            if(alertName != null &&alertName.length() > 0){
     		   int pos = alertName.indexOf(locale)+locale.length()+1;
     		  alertName = alertName.substring(pos,alertName.indexOf(";",pos));  
     	   }
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		alertName, "tblSysAlert", "预警项目设置","");
		}else{
			EchoMessage.success().add(getMessage(
	                request, "common.msg.updateErro")).setBackUrl("/AlertSetAction.do?operation=4&optype=query").setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	
	/**
	 * 预警设置详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward detailAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		/* 预警ID */
		String keyId = request.getParameter("keyId");
		
		/**
		 * 查询详情
		 */
		Result rs = mgt.loadAlertSet(keyId,this.getLocale(request).toString());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("bean", rs.retVal);
			rs = mgt.queryAlertGroup();
			request.setAttribute("groupList", rs.retVal);
		}
		request.setAttribute("detail", "true");
		return getForward(request, mapping, "dealAlertSet");
	}
	
	
	/**
	 * 删除预警设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward deleteAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String keyId = request.getParameter("keyId");
		Result rsd = mgt.loadAlertSet(keyId,this.getLocale(request).toString());
		if(rsd.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
		}
		
		Result rs = mgt.deleteAlertSet(keyId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                  request, "common.msg.delSuccess"))
                  .setBackUrl("/AlertSetAction.do?operation=4&optype=query").setAlertRequest(request);
			HashMap map = (HashMap)rsd.retVal;
			String alertName =(String)map.get("alert_lan");
			String locale = this.getLocale(request).toString();
            if(alertName != null &&alertName.length() > 0){
     		   int pos = alertName.indexOf(locale)+locale.length()+1;
     		  alertName = alertName.substring(pos,alertName.indexOf(";",pos));  
     	   }
			new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
					alertName, "tblSysAlert", "预警项目设置","");
		}else {
			EchoMessage.success().add(getMessage(
                  request, "common.msg.delError"))
                  .setBackUrl("/AlertSetAction.do?operation=4&optype=query").setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 预警显示或者隐藏
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward openOrStop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String[] keyids = request.getParameterValues("keyId");					//记录的id数组
		String statusType = request.getParameter("statusType");					//设置的状态类型（显示，隐藏）
		
		/* 修改状态 */
		Result rs = mgt.openOrStop(keyids, statusType);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/* 修改成功 */
			EchoMessage.success().add(getMessage(
	                request, "common.msg.updateSuccess")).setBackUrl("/AlertSetAction.do?operation=4&optype=query").setAlertRequest(request);
		}else{
			EchoMessage.success().add(getMessage(
	                request, "common.msg.updateErro")).setBackUrl("/AlertSetAction.do?operation=4&optype=query").setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
}
