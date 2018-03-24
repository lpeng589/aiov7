package com.koron.oa.oaWorkLog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.OAWorkLogBean;
import com.koron.oa.bean.OAWorkLogDetBean;
import com.koron.oa.oaItems.OAItemsMgt;
import com.koron.oa.oaWorkLogTemplate.OAWorkLogTemplateMgt;
import com.koron.oa.util.DateUtil;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.logManage.LoginlogSearchForm;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;
/**
 * 
 * <p>Title:我的日志</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/12
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class OAWorkLogAction extends BaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	OAWorkLogMgt mgt = new OAWorkLogMgt();
	OAItemsMgt itemMgt = new OAItemsMgt();
	DateUtil dateUtil = new DateUtil();
	int personalFrameCount =0;//用于存放个人框架次数
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
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		switch (operation) {
			case OperationConst.OP_ADD_PREPARE:
					forward = addWorkLogDetPrepare(mapping, form, request, response);//添加日志
				break;
			case OperationConst.OP_ADD:
				if("addQuote".equals(type)){
					forward = addQuote(mapping, form, request, response);//添加引用
				}else if("workLogDet".equals(type)){
					forward = addWorkLogDet(mapping, form, request, response);//添加引用
				}else if("warmToWriteLog".equals(type)){
					forward = warmToWriteLog(mapping, form, request, response);//添加提醒写日志
				}else{
					forward = add(mapping, form, request, response);//添加日志
				}
				
				
				break;
			case OperationConst.OP_UPDATE_PREPARE:
				forward = updatePrepare(mapping, form, request, response);//添加日志
				break;
			case OperationConst.OP_UPDATE:
				if("updateDet".equals(type)){
					forward = updateDetRelation(mapping, form, request, response);//更新日志明细
				}else if("followEmp".equals(type)){
					forward = updateFollowEmp(mapping, form, request, response);//更新关注信息
				}else if("workLogDet".equals(type)){
					forward = updateWorkLogDet(mapping, form, request, response);//更新关注信息
				}else{
					forward = update(mapping, form, request, response);//更新日志
				}
				break;
			case OperationConst.OP_DETAIL:
				forward = detail(mapping, form, request, response);//日志详情	
				break;
			case OperationConst.OP_DELETE:
				forward = delete(mapping, form, request, response);//删除日志
				break;
			case OperationConst.OP_QUERY:
				if("quote".equals(type)){
					forward = quoteQuery(mapping, form, request, response);//引用查询
				}else if("isTHWorkLog".equals(type)){
					forward = queryTH(mapping, form, request, response);//首页TH
				}else if("WorkLogList".equals(type)){
					forward = queryLoginLogList(mapping, form, request, response);//日志列表查询
				}else{
					forward = query(mapping, form, request, response);//首页查询
				}
				
				break;
			default:
				forward = query(mapping, form, request, response);//首页查询
				break;
		}
		return forward;
	}

	private ActionForward queryTH(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String condition = " and isPlanTemplate <> 'true' and createBy = '"+getLoginBean(request).getId()+"'";
		Result rs = mgt.workLogQuery(condition, "TH");
		String workIds = "";
		ArrayList<OAWorkLogBean> workLogBeanList = new ArrayList<OAWorkLogBean>();
		if(rs!=null && rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<OAWorkLogBean> rsRs = (ArrayList<OAWorkLogBean>)rs.retVal;
			if(rsRs.size() > 5){
				for(int i=0;i<5;i++){	
					OAWorkLogBean workLogBean = (OAWorkLogBean)mgt.loadWorkLogBean(rsRs.get(i).getId()).retVal;
					if(workLogBean!=null){						
						workLogBeanList.add(workLogBean);					
					}
					workIds += "'"+rsRs.get(i).getId()+"',";
				}
			}else{
				for(int i=0;i<rsRs.size();i++){
					OAWorkLogBean workLogBean = (OAWorkLogBean)mgt.loadWorkLogBean(rsRs.get(i).getId()).retVal;
					if(workLogBean!=null){						
						workLogBeanList.add(workLogBean);					
					}
					workIds += "'"+rsRs.get(i).getId()+"',";
				}
			}
			
		}
		//request.setAttribute("msg",workIds);
		/*封装数据*/
		
		if(workIds.endsWith(",")){
			workIds = workIds.substring(0,workIds.length()-1);
		}
		Result detRs = mgt.workLogDetQuery(workIds);
		request.setAttribute("workLogBeanList",workLogBeanList);
		request.setAttribute("workLogDetMap",detRs.retVal);
		request.setAttribute("loginBean",getLoginBean(request));
		
		return getForward(request, mapping, "oaworklog");
	}

	/**
	 * 提醒写日志
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward warmToWriteLog(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String followId = getParameter("followId",request);//提醒职员ID
		String teamLogDate = getParameter("teamLogDate",request);//日期
		String teamLogType = getParameter("teamLogType",request);//类型week 周,day日
		
		LoginBean loginBean = getLoginBean(request);
		
		String content = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"提醒你写"+teamLogDate+"的日计划";
		if(teamLogType!=null && "week".equals(teamLogType)){
			String monday;
			try {
				monday = new DateUtil().getMondayBySelTime(teamLogDate);//获取某个时间周一的时间
				//获取周日日期
				Calendar ca = Calendar.getInstance();
				ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
				ca.add(Calendar.DAY_OF_MONTH,6);
				String sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//存放周日
				
				content = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"提醒你写"+monday+"至"+sunday+"的周计划";//内容
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		String url = "<a href=\"javascript:mdiwin('/OAWorkLogAction.do?operation=4','我的日志')\">"+content+"</a>";
		new AdviceMgt().add(loginBean.getId(), content, url, followId, IDGenerater.getId(), "OALogPoint");
		
		request.setAttribute("msg","success");
		
		return getForward(request, mapping, "blank");
	}

	private ActionForward updateWorkLogDet(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		
		String detType = getParameter("detType",request);//表示明细类型 plan：计划 summary总结
		String workLogId = getParameter("workLogId",request);//日志ID
		String type = getParameter("workLogType",request);//日志类型 day,week
		String workLogDate = getParameter("workLogDate",request);//日志日期
		String clientId = getParameter("clientId",request);//客户ID
		String shareBy = getParameter("shareBy",request);//客户ID
		String affix = getParameter("affix",request);//附件
		String contents = getParameter("contents",request);//计划内容
		LoginBean loginBean = getLoginBean(request);
		String isPlanTemplate = getParameter("isPlanTemplate",request);//true 表示模板
		String contentType = "2";
		if("summary".equals(detType)){
			contentType = "1";
		}else{
			
		}
		
		String delSql = "DELETE FROM OAWorkLogDet WHERE workLogId = ? and contentType= ?";
		ArrayList param = new ArrayList();
		param.add(workLogId);
		param.add(contentType);
		Result rs = mgt.operationSql(delSql,param);
		
		rs = mgt.loadWorkLogBean(workLogId);
		OAWorkLogBean workLogBean = (OAWorkLogBean)rs.retVal;
		
		String beforeShareBy = workLogBean.getShareBy();//记录原来的分享人
		
		String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		String userId = loginBean.getId();
		workLogBean.setId(workLogId);
		workLogBean.setType(type);
		workLogBean.setWorkLogDate(workLogDate);
		workLogBean.setClientId(clientId);
		workLogBean.setLastUpdateTime(nowDate);
		workLogBean.setShareBy(shareBy);

		//处理附件
		if("summary".equals(detType)){
			workLogBean.setSummaryAffix(affix);//总结附件
		}else{
			workLogBean.setAffix(affix);//计划附件
		}
		
		
		ArrayList<String[]> updateTaskList = new ArrayList<String[]>();//存放需要UPDATE的任务信息
		
		//获取明细信息
		ArrayList<OAWorkLogDetBean> detList = new ArrayList<OAWorkLogDetBean>();
		detList.addAll(this.getDetBeanList(contents, contentType, workLogId,null,isPlanTemplate));
		
		rs = mgt.updateWorkLog(workLogBean, detList,updateTaskList);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//this.sendMsgByShareBy(loginBean, workLogBean, "update",beforeShareBy);//处理分享人通知
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}

	private ActionForward addWorkLogDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		
		String detType = getParameter("detType",request);//表示明细类型 plan：计划 summary总结
		String workLogId = getParameter("workLogId",request);//日志id,有就不插入主表
		String type = getParameter("workLogType",request);//日志类型 day,week
		String workLogDate = getParameter("workLogDate",request);//日志日期
		String isPlanTemplate = getParameter("isPlanTemplate",request);//true 表示模板
		String contents = getParameter("contents",request);//内容
		String shareBy = getParameter("shareBy",request);//内容
		String affix = getParameter("affix",request);//附件
		
		String existNextWorkLog = getParameter("existNextWorkLog",request);//true 表示存在日志
		String nextWorkLogDate = getParameter("nextWorkLogDate",request);//下期时间
		String nextPlanContents = getParameter("nextPlanContents",request);//下期计划内容
		String nextPlanAffix = getParameter("nextPlanAffix",request);//下期附件
		
		
		
		LoginBean loginBean = getLoginBean(request);
		String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		
		Boolean existWorkLogId = false;//存放是否存在主表ID
		OAWorkLogBean workLogBean = new OAWorkLogBean();
		String userId = loginBean.getId();
		if(workLogId==null || "".equals(workLogId)){
			if(workLogDate==null || "".equals(workLogDate) || "null".equals(workLogDate)){
				workLogDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
			}
			workLogId =IDGenerater.getId(); 
			workLogBean.setId(workLogId);
			workLogBean.setType(type);
			workLogBean.setWorkLogDate(workLogDate);
			workLogBean.setCreateBy(userId);
			workLogBean.setLastUpdateBy(userId);
			workLogBean.setCreateTime(nowDate);
			workLogBean.setLastUpdateTime(nowDate);
			workLogBean.setIsPlanTemplate(isPlanTemplate);
			workLogBean.setShareBy(shareBy);
			
		}else{
			Result rs = mgt.loadWorkLogBean(workLogId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				workLogBean = (OAWorkLogBean)rs.retVal;
				existWorkLogId = true;
			}
			
		}

		String contentType = "2";
		if("summary".equals(detType)){
			contentType = "1";
			workLogBean.setSummaryAffix(affix);//总结附件
		}else{
			workLogBean.setAffix(affix);//计划附件
		}
		//获取明细信息
		ArrayList<OAWorkLogDetBean> detList = new ArrayList<OAWorkLogDetBean>();
		detList.addAll(this.getDetBeanList(contents, contentType, workLogId,null,isPlanTemplate));
		
		
		//总结处理下期计划
		OAWorkLogBean nextWorkLogBean = new OAWorkLogBean();
		if(nextPlanContents!=null && !"".equals(nextPlanContents) && "summary".equals(detType) && (existNextWorkLog==null || "".equals(existNextWorkLog))){
			try {
				PropertyUtils.copyProperties(nextWorkLogBean, workLogBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(nextWorkLogBean!=null){
				nextWorkLogBean.setId(IDGenerater.getId());	
				if("day".equals(type)){
					nextWorkLogBean.setWorkLogDate(nextWorkLogDate);
				}else{
					Calendar ca = Calendar.getInstance();
					try {
						ca.setTime(BaseDateFormat.parse(workLogDate, BaseDateFormat.yyyyMMdd));
					}catch (Exception e) {
						e.printStackTrace();
					} 
					ca.add(Calendar.DATE,6);
					nextWorkLogBean.setWorkLogDate(BaseDateFormat.format(ca.getTime(), BaseDateFormat.yyyyMMdd));
				}
				nextWorkLogBean.setAffix(nextPlanAffix);
				nextWorkLogBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			}
			
			if(nextPlanContents!=null && !"".equals(nextPlanContents)){
				detList.addAll(this.getDetBeanList(nextPlanContents, "2", nextWorkLogBean.getId(),null,null));
			}
		}
		
		Result rs = mgt.addWorkLog(workLogBean, detList,existWorkLogId,nextWorkLogBean);
		mgt.synWorkLog(loginBean,workLogBean.getCreateTime());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
			//处理附件
			if(affix!=null && !"".equals(affix)){
				for(String str :affix.split(";")){
					FileHandler.copy("OAWorkLog", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			
			String followMeIds = mgt.getFollowMeEmpIds(loginBean);//获取关注我的职员IDS
			this.sendMsgByFollowMe(workLogBean, followMeIds, loginBean);
			
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}

	private ActionForward addWorkLogDetPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String workLogType =  getParameter("workLogType",request);
		String detType = getParameter("detType",request);
		String workLogDate = getParameter("workLogDate",request); 
		String isWorkTH = getParameter("isWorkTH",request);//是否天华的
		request.setAttribute("detType",detType);//添加类型 plan:计划 summary:总结
		request.setAttribute("workLogType",workLogType);//日志类型 day ,week
		request.setAttribute("workLogDate",workLogDate);//日期
		request.setAttribute("workLogId", getParameter("workLogId",request));//日志ID
		request.setAttribute("opertaionFlag", getParameter("opertaionFlag",request));//operation 1:添加 2:表示新增
		LoginBean loginBean = getLoginBean(request);
		
		String planTemplateContent = new OAWorkLogTemplateMgt().getPlanTemplateContent(getLoginBean(request),workLogType);
		if(planTemplateContent!=null && !"".equals(planTemplateContent)){
			//表示启用模板
			request.setAttribute("isPlanTemplate","true");
		}
		
		
		Calendar ca = Calendar.getInstance();
		try {
			ca.setTime(BaseDateFormat.parse(workLogDate, BaseDateFormat.yyyyMMdd));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果是新增总结，处理明天的工作计划
		if("summary".equals(detType)){
			if("week".equals(workLogType)){
				ca.add(Calendar.DATE,6);
				String[] mondayAndSunday = DateUtil.getMondayAndSunday(BaseDateFormat.format(ca.getTime(), BaseDateFormat.yyyyMMdd));
				request.setAttribute("existNextWorkLog", mgt.existNextWorkLog(null, loginBean,"week",mondayAndSunday));//是否存在下周日志
				request.setAttribute("nextWorkLogDate",BaseDateFormat.format(ca.getTime(), BaseDateFormat.yyyyMMdd));
				request.setAttribute("mondayAndSunday",mondayAndSunday);
			}else{
				int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK); 
				if(dayOfWeek>5){
					LinkedHashMap<String,String> nextThreeDateMap = mgt.getNextWorkLogDate(ca, loginBean);
					request.setAttribute("nextThreeDateMap",nextThreeDateMap);
				}else{
					ca.add(Calendar.DATE,1);
					String tomorrow = BaseDateFormat.format(ca.getTime(), BaseDateFormat.yyyyMMdd);
					request.setAttribute("existNextWorkLog", mgt.existNextWorkLog(tomorrow, loginBean,"day",null));//是否存在明天日志
					request.setAttribute("nextWorkLogDate",tomorrow);
					request.setAttribute("nextWorkLogDateCN", dateUtil.getCurrentWeekOfMonth(ca));
				}
			}
			
		}
		
		request.setAttribute("planTemplateContent",planTemplateContent);
		if("true".equals(isWorkTH)){
			return getForward(request, mapping, "addoaworklog");
		}
		return getForward(request, mapping, "addWorkLogDet");
	}

	/**
	 * 更新关注人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFollowEmp(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String employeeId = getParameter("employeeId",request);//职员ID
		String followIds = getParameter("followIds",request);//选中的关注人
		String sql = "UPDATE OAWorkLogFollow SET followIds=? WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(followIds);
		param.add(employeeId);
		Result rs = mgt.updateFollowInfo(employeeId, followIds);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String msgInfo = "";
			for(String userId : followIds.split(",")){
				if(userId!=null && !"".equals(userId)){
					msgInfo += "<li empId='"+userId+"'><span class='followQuery' >"+GlobalsTool.getEmpFullNameByUserId(userId)+"</span><b class='icons delFollow delFollowEmp'/></li>";
				}
			}
			request.setAttribute("msg",msgInfo);
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	
	
	private ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String workLogId = getParameter("workLogId",request);//日志id
		String isTHWorkLog = getParameter("isTHWorkLog",request);//是否是天华主页的
		OAWorkLogBean workLogBean = (OAWorkLogBean)mgt.loadWorkLogBean(workLogId).retVal;
		if(workLogBean!=null){
			ArrayList<OAWorkLogBean> workLogBeanList = new ArrayList<OAWorkLogBean>();
			workLogBeanList.add(workLogBean);
			
			//封装查询ID
			String ids = "'"+workLogId+"',";
			
			//LinkedHashMap<String,String>  weekScopeMap = this.getWeekScopeMapByMonday(monday);//存放本周时间段map
			String nextCondition = " AND workLogDate ";
			Calendar ca = Calendar.getInstance();
			//天华主页不需要下面 所以
			if(!"true".equals(isTHWorkLog)){
				if("week".equals(workLogBean.getType())){
					String monday = new DateUtil().getMondayBySelTime(workLogBean.getWorkLogDate());//获取周一的时间
					String sunday = "";//获取周日
					String nextMonday="";//下周一
					String nextSunday="";//下周日
					
					//获取周日日期
					ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
					ca.add(Calendar.DAY_OF_MONTH,6);
					sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);
					ca.add(Calendar.DAY_OF_MONTH,1);
					nextMonday=BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);
					ca.add(Calendar.DAY_OF_MONTH,6);
					nextSunday=BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);
					
					request.setAttribute("monday",monday);
					request.setAttribute("sunday",sunday);
					request.setAttribute("nextMonday",nextMonday);
					request.setAttribute("nextSunday",nextSunday);
					
					nextCondition += " BETWEEN '"+nextMonday+"'"+" AND '"+nextSunday+"' ";//查询条件
				}else{
					ca.setTime(BaseDateFormat.parse(workLogBean.getWorkLogDate(), BaseDateFormat.yyyyMMdd));
					request.setAttribute("workLogDateName",dateUtil.getCurrentWeekOfMonth(ca));
					ca.add(Calendar.DAY_OF_MONTH,1);
					
					String nextDay=BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//明天
					request.setAttribute("nextDay",nextDay);
					request.setAttribute("nextDayName",dateUtil.getCurrentWeekOfMonth(ca));
					
					nextCondition += " ='"+nextDay+"'";//查询条件
				}	
				//获取下期workLogBean
				OAWorkLogBean nextWorkLogBean = mgt.workLogBeanQuery(workLogBean, nextCondition);
				if(nextWorkLogBean!=null){
					ids += "'"+nextWorkLogBean.getId()+"',"; 
					
					workLogBeanList.add(nextWorkLogBean);
				}
			}
						
			if(ids.endsWith(",")){
				ids = ids.substring(0,ids.length()-1);
			}
			Result rs = mgt.workLogDetQuery(ids);
			
			//获取客户名称
			String clientName =this.getClientName(workLogBean.getClientId()); 
			request.setAttribute("clientName",clientName);
			
			//request.setAttribute("weekScopeMap",weekScopeMap);
			request.setAttribute("discussCountMap",mgt.queryDiscussCount(workLogBeanList));
			request.setAttribute("workLogBeanList",workLogBeanList);
			request.setAttribute("workLogDetMap",rs.retVal);
			request.setAttribute("loginBean",getLoginBean(request));
		}
		if("true".equals(isTHWorkLog)){
			return getForward(request, mapping, "oaworklog");
		}
		
		//获取所有职员信息,用于textBox控件
		ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
		request.setAttribute("textBoxValues",gson.toJson(list));
		
		return getForward(request, mapping, "detail");
	}

	/**
	 * 更新明细
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateDetRelation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String detId = getParameter("detId",request);//明细ID
		String relationId = getParameter("relationId",request);//关联ID
		
		String sql = "UPDATE OAWorkLogDet SET relationId = ?,relationType='OATask' WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(relationId);
		param.add(detId);
		Result rs = mgt.operationSql(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 添加引用
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addQuote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String workLogId = getParameter("workLogId",request);//日志ID
		String quoteInfo = getParameter("quoteInfo",request);//引用信息;
		
		if(quoteInfo !=null && !"".equals(quoteInfo)){
			ArrayList<OAWorkLogDetBean> detBeanList = new ArrayList<OAWorkLogDetBean>();
			for(String info : quoteInfo.split(";")){
				OAWorkLogDetBean bean = new OAWorkLogDetBean();
				bean.setId(IDGenerater.getId());
				bean.setContents(info.split(":")[1]);
				bean.setContentType("1");
				bean.setRelationType("OACalendar");
				bean.setRelationId(info.split(":")[0]);
				bean.setWorkLogId(workLogId);
				bean.setSchedule("100");
				detBeanList.add(bean);
			}
			Result rs = mgt.addWorkLogDet(detBeanList);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 查询引用
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	private ActionForward quoteQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tableName = getParameter("tableName",request);//表名
		String workLogType = getParameter("workLogType",request);//类型 day or week
		String workLogDate = getParameter("workLogDate",request);//引用日期
		String backfillName = getParameter("backfillName",request);//回填的DIV名称,如果是计划,根据引用日期的下一天
		LoginBean loginbean = getLoginBean(request);
		String isOperation = getParameter("isOperation",request);//判断是否添加修改页面引用
		String workLogId = getParameter("workLogId",request);//日志ID，用于总结引用计划
		
		String sql = "";
		ArrayList param = new ArrayList();
		
		String divTitle = "";//存放弹出框标题
		String checked = "";//默认是否checked
		
		
		//类型是周获取某个时间周一的时间
		String monday = "";
		String sunday = "";
		if("week".equals(workLogType)){
			//获取周日日期
			Calendar ca = Calendar.getInstance();
			try {
				monday = new DateUtil().getMondayBySelTime(workLogDate);
				ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
				ca.add(Calendar.DAY_OF_MONTH,6);
				sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//存放周日
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if("OAToDo".equals(tableName)){
			divTitle = "引用的待办";
			//引用待办
			sql = "SELECT id,title FROM OAToDo WHERE  status = '0' and createBy = ? ORDER BY createTime DESC";
			param.add(loginbean.getId());
		}else if("OATask".equals(tableName)){
			divTitle = "引用的任务";
			//引用任务
			sql = "SELECT id,title FROM OATask WHERE  status <> '2' and ( createBy=? or executor=? or surveyor=? or ','+participant like '%,"+loginbean.getId()+",%') ";
			param.add(loginbean.getId());
			param.add(loginbean.getId());
			param.add(loginbean.getId());
			
			/*
			if("day".equals(workLogType)){
				sql += " and ? between beginTime and endTime " ; 
				param.add(workLogDate);
			}else{
				sql += " and ( beginTime between ? and ? or endTime between ? and ?)";
				param.add(monday);
				param.add(sunday);
				param.add(monday);
				param.add(sunday);
			}*/
			
			sql += " ORDER BY createTime DESC ";
			
		}else if("OAWorkLogDet".equals(tableName)){
			
			sql = "SELECT OAWorkLogDet.id,OAWorkLogDet.contents FROM OAWorkLogDet LEFT JOIN OAWorkLog on OAWorkLogDet.workLogId = OAWorkLog.id WHERE contentType='2' ";
			if(workLogId!=null && !"".equals(workLogId)){
				sql +=" and workLogId = ?";
				param.add(workLogId);
			}
			
			if("day".equals(workLogType)){
				divTitle = workLogDate+"的计划";
			}else{
				sql += " and OAWorkLog.workLogDate between ? and ?";
				param.add(monday);
				param.add(sunday);
				divTitle = monday+"至"+sunday+"周计划";
			}
			
		}else{
			sql = "SELECT id,title FROM OACalendar WHERE userId = ? ";
			param.add(loginbean.getId());
			if("day".equals(workLogType)){
				sql += " and ? between stratTime and finishTime";	
				param.add(workLogDate);
				divTitle = workLogDate+"的日程";
			}else{
				sql += " and ( stratTime between ? and ? or finishTime between ? and ?)";
				param.add(monday);
				param.add(sunday);
				param.add(monday);
				param.add(sunday);
				divTitle = monday+"至"+sunday+"周日程";
			}
			sql += " ORDER BY stratTime DESC";
		}
		
		Result rs = mgt.publicSqlQuery(sql,param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String msgInfo = "";//返回数据
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				
				//默认列表引用的div
				String submitId = "quoteSubmit";
				if(isOperation!=null && "true".equals(isOperation)){
					//表示添加修改引用
					submitId = "quoteBackfillSubmit";
				}
				
				//数据封装
				msgInfo = "<div id='quoteDiv' class='add-calendar' tableName='"+tableName+"' ><input class='inp-txt' type='text' placeholder='"+divTitle+"' readonly='readonly'/><div class='point-block-s'>";
				String id ="";
				String value ="";
				int i=1;
				for(Object obj : list){
					id = String.valueOf(GlobalsTool.get(obj,0));
					value = String.valueOf(GlobalsTool.get(obj,1));
					msgInfo +="<div class='point-block'  ><input type='checkbox' class='quoteBox' value='"+id+"' id='"+id+"' displayStr='"+value+"' "+checked+"/><label class='lf' style='cursor: pointer;' for='"+id+"'>"+i+"."+value+"</label></div>";
					i++;
				}
				msgInfo +="</div><div class='point-block'><input class='lf btn-add' type='button' value='确认' id='"+submitId+"' /><input class='lf btn-add' type='button' value='取消' id='quoteCancel'/></div></div>";
			}
			request.setAttribute("msg",msgInfo);
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 更新日志
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String workLogId = getParameter("workLogId",request);//日志ID
		String type = getParameter("workLogType",request);//日志类型 day,week
		String workLogDate = getParameter("workLogDate",request);//日志日期
		String affix = getParameter("affix",request);//附件
		String shareBy = getParameter("shareBy",request);//分享人
		String clientId = getParameter("clientId",request);//客户ID
		String planContents = getParameter("planContents",request);//计划内容
		String summaryContents = getParameter("summaryContents",request);//总结内容
		LoginBean loginBean = getLoginBean(request);

		
		String delSql = "DELETE FROM OAWorkLogDet WHERE workLogId = ?";
		ArrayList param = new ArrayList();
		param.add(workLogId);
		Result rs = mgt.operationSql(delSql,param);
		
		rs = mgt.loadWorkLogBean(workLogId);
		OAWorkLogBean workLogBean = (OAWorkLogBean)rs.retVal;
		
		String beforeShareBy = workLogBean.getShareBy();//记录原来的分享人
		
		String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		String userId = loginBean.getId();
		workLogBean.setId(workLogId);
		workLogBean.setType(type);
		workLogBean.setWorkLogDate(workLogDate);
		workLogBean.setClientId(clientId);
		workLogBean.setAffix(affix);
		workLogBean.setShareBy(shareBy);
		workLogBean.setLastUpdateTime(nowDate);

		ArrayList<String[]> updateTaskList = new ArrayList<String[]>();//存放需要UPDATE的任务信息
		
		//获取明细信息
		ArrayList<OAWorkLogDetBean> detList = new ArrayList<OAWorkLogDetBean>();
		detList.addAll(this.getDetBeanList(summaryContents, "1", workLogId,updateTaskList,null));
		detList.addAll(this.getDetBeanList(planContents, "2", workLogId,updateTaskList,null));
		
		rs = mgt.updateWorkLog(workLogBean, detList,updateTaskList);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
			//处理附件
			if(affix!=null && !"".equals(affix)){
				for(String str :affix.split(";")){
					FileHandler.copy("OAWorkLog", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			
			this.sendMsgByShareBy(loginBean, workLogBean, "update",beforeShareBy);//处理分享人通知
			
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 日志更新前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String workLogId = getParameter("workLogId",request);//日志ID
		Result rs = mgt.loadWorkLogBean(workLogId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			OAWorkLogBean workLogBean = (OAWorkLogBean)rs.retVal;
			rs = mgt.workLogDetQuery("'"+workLogId+"'");
			HashMap<String,HashMap<String,ArrayList<String[]>>> workLogDetMap = (HashMap<String,HashMap<String,ArrayList<String[]>>>)rs.retVal;
			//ArrayList<String[]> workLogDetList = (ArrayList<String[]>)rs.retVal; 
			
			
			if(workLogBean.getShareBy()!=null && !"".equals(workLogBean.getShareBy())){
				String shareByInfo = "";//存放分享信息id:name
				for(String userId : workLogBean.getShareBy().split(",")){
					if(userId!=null && !"".equals(userId)){
						shareByInfo += userId+":"+GlobalsTool.getEmpFullNameByUserId(userId)+";";
					}
				}
				request.setAttribute("shareByInfo",shareByInfo);
			}
			/*
			String monday = new DateUtil().getMondayBySelTime(workLogBean.getWorkLogDate());//获取某个时间周一的时间
			LinkedHashMap<String,String>  weekScopeMap = this.getWeekScopeMapByMonday(monday);//存放本周时间段map
			HashMap<String,String> existDateMap = new HashMap<String, String>();//存放本周已有的日期
			
			Calendar ca = Calendar.getInstance();
			ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
			ca.add(Calendar.DAY_OF_MONTH,7);
			String sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//存放周日
			
			//查询已有的日期放入MAP中用于过滤
			String sql = "SELECT workLogDate FROM OAWorkLog WHERE type='day' and workLogDate <> '"+workLogBean.getWorkLogDate()+"' and  workLogDate between '"+monday+"' and '"+sunday+"'";
			Result rest = mgt.publicSqlQuery(sql, new ArrayList());
			if(rest.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> list = (ArrayList<Object>)rest.retVal;
				if(list!=null && list.size()>0){
					for(Object obj : list){
						existDateMap.put(String.valueOf(GlobalsTool.get(obj,0)),"1");
					}
				}
			}
			
			String optionInfo = "";//存放指定日期的select option
			Set keys = weekScopeMap.keySet( );
			if(keys != null) {
				Iterator iterator = keys.iterator();
				while(iterator.hasNext()) {
					Object key = iterator.next();
					if(existDateMap.get(String.valueOf(key))==null){
						optionInfo +="<option ";
						if(workLogBean.getWorkLogDate().equals(String.valueOf(key))){
							optionInfo +=" selected='selected' ";
						}	
						optionInfo +=" value='"+key+"'>"+key+"("+weekScopeMap.get(String.valueOf(key))+")</option>";
					}
				}
			}
			
			request.setAttribute("optionInfo",optionInfo);
			*/
			
			//获取客户名称
			String clientName =this.getClientName(workLogBean.getClientId()); 
			request.setAttribute("clientName",clientName);
			
			
			request.setAttribute("detType",getParameter("detType",request));//修改明细类型
			request.setAttribute("workLogBean",workLogBean);
			request.setAttribute("workLogDetMap",workLogDetMap);
		}
		return getForward(request, mapping, "update");
	}

	/**
	 * 删除日志
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String workLogId = getParameter("workLogId",request);
		
		Result rs = mgt.delWorkLog(workLogId);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}


	/**
	 * 添加日志
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String type = getParameter("workLogType",request);//日志类型 day,week
		String workLogDate = getParameter("workLogDate",request);//日志日期
		String affix = getParameter("affix",request);//附件
		String shareBy = getParameter("shareBy",request);//分享人
		String clientId = getParameter("clientId",request);//客户ID
		String planContents = getParameter("planContents",request);//计划内容
		String summaryContents = getParameter("summaryContents",request);//总结内容
		LoginBean loginBean = getLoginBean(request);
		String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		
		
		if(workLogDate==null || "".equals(workLogDate) || "null".equals(workLogDate)){
			workLogDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
		}
		
		OAWorkLogBean workLogBean = new OAWorkLogBean();
		String userId = loginBean.getId();
		String workLogId =IDGenerater.getId(); 
		workLogBean.setId(workLogId);
		workLogBean.setType(type);
		workLogBean.setWorkLogDate(workLogDate);
		workLogBean.setClientId(clientId);
		workLogBean.setAffix(affix);
		workLogBean.setShareBy(shareBy);
		workLogBean.setCreateBy(userId);
		workLogBean.setLastUpdateBy(userId);
		workLogBean.setCreateTime(nowDate);
		workLogBean.setLastUpdateTime(nowDate);

		
		//获取明细信息
		ArrayList<OAWorkLogDetBean> detList = new ArrayList<OAWorkLogDetBean>();
		detList.addAll(this.getDetBeanList(summaryContents, "1", workLogId,null,null));
		detList.addAll(this.getDetBeanList(planContents, "2", workLogId,null,null));
		
		Result rs = mgt.addWorkLog(workLogBean, detList,false,null);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//处理附件
			if(affix!=null && !"".equals(affix)){
				for(String str :affix.split(";")){
					FileHandler.copy("OAWorkLog", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			
			this.sendMsgByShareBy(loginBean, workLogBean, "add", null);//处理分享人通知
			
			
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}


	/**
	 * 首页查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ParseException {
		
		OAWorkLogSearchForm workLogSearchForm = (OAWorkLogSearchForm)form;
		StringBuilder pubCondition = new StringBuilder();//条件
		StringBuilder createByCondition = new StringBuilder();//条件
		StringBuilder followByCondition = new StringBuilder();//我关注的
		LoginBean loginBean = getLoginBean(request);
		String userId = loginBean.getId();
		
		if(workLogSearchForm.getFollowEmpId()!=null && !"".equals(workLogSearchForm.getFollowEmpId())){
			userId = workLogSearchForm.getFollowEmpId();//表示是点击关注人进入
		}
		
		if(workLogSearchForm.getTabSelectName()==null || "".equals(workLogSearchForm.getTabSelectName())){
			workLogSearchForm.setTabSelectName("tabCreateBy");
		}
		
		if(workLogSearchForm.getSearchWorkLogDate()==null || "".equals(workLogSearchForm.getSearchWorkLogDate())){
			workLogSearchForm.setSearchWorkLogDate(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd));
		}
		
		//查询关注人
		String  followIds = mgt.getFollowIdsById(userId); 
		request.setAttribute("followIds",followIds);
		
		String followIdsStr = "";
		if(followIds!=null && !"".equals(followIds)){
			for(String empId : followIds.split(",")){
				followIdsStr += "'"+empId+"',";
			}
			if(followIdsStr.endsWith(",")){
				followIdsStr += followIdsStr.substring(0,followIdsStr.length()-1);
			}
		}
		
		//获取个人关注架构信息
		personalFrameCount=0;//统计个人关注架构次数,超过10次退出
		String personalFrameAllEmp = ","+userId+",";//存放所有下属与自己的职员。用于页面判断已选的关注人在不在里面
		String personalEmpStr ="";//查询所有框架的任务日志信息
		ArrayList<Object> empList = new ArrayList<Object>();//存放当前用户下属所有职员List
		this.getPersonalFrame(empList, userId);//调用递归方法获取所有下属职员
		String personalFrameTree = "[{id:'"+userId+"',pId:'0',name:'"+GlobalsTool.getEmpFullNameByUserId(userId)+"',open:'true'},";
		if(empList!=null && empList.size()>0){
			for(Object obj : empList){
				String empId = String.valueOf(GlobalsTool.get(obj,0));
				personalFrameTree += "{id:'"+empId+"',pId:'"+GlobalsTool.get(obj,1)+"',name:'"+GlobalsTool.get(obj,2)+"',open:'true'},";
				personalFrameAllEmp += empId+",";
				personalEmpStr +="'"+empId+"',";
			}
			if(personalFrameTree.endsWith(",")){
				personalFrameTree = personalFrameTree.substring(0,personalFrameTree.length()-1);
			}
		}
		personalFrameTree+="]";

		request.setAttribute("personalFrameTree",personalFrameTree);
		request.setAttribute("personalFrameAllEmp",personalFrameAllEmp);
		request.setAttribute("personalFrameCount",personalFrameCount);
		
		//本周时间查询
		String monday = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
		
		if(workLogSearchForm.getWeekStartTime()!=null && !"".equals(workLogSearchForm.getWeekStartTime())){
			monday = workLogSearchForm.getWeekStartTime();
		}else{
			workLogSearchForm.setWeekStartTime(monday);
		}
		
		monday = new DateUtil().getMondayBySelTime(monday);//获取某个时间周一的时间
		

		//获取周日日期
		Calendar ca = Calendar.getInstance();
		ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
		ca.add(Calendar.DAY_OF_MONTH,6);
		String sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//存放周日
		LinkedHashMap<String,String>  weekScopeMap = this.getWeekScopeMapByMonday(monday);//存放本周时间段map
		
		if("tabCreateBy".equals(workLogSearchForm.getTabSelectName()) || (workLogSearchForm.getFollowEmpId()!=null && !"".equals(workLogSearchForm.getFollowEmpId()))){
			//个人日志
			pubCondition.append(" and workLogDate between '").append(monday).append("' and '").append(sunday).append("' ");
			
			createByCondition.append(" and createBy = '").append(userId).append("' ").append(pubCondition.toString());//创建人条件
			
			Result rs = mgt.workLogQuery(createByCondition.toString(),null);
			
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<OAWorkLogBean> workLogList = (ArrayList<OAWorkLogBean>)rs.retVal;
				HashMap<String,OAWorkLogBean> existWorkLogMap = new HashMap<String, OAWorkLogBean>();//存放存在的日期
				HashMap<String,String> existDateMap = new LinkedHashMap<String, String>();//存放存在的日期
				OAWorkLogBean weekWorkLogBean = null;//存放周报日期
				if(workLogList!=null && workLogList.size()>0){
					String workLogIds = "";//存放主表IDS
					for(OAWorkLogBean bean : workLogList){
						workLogIds +="'"+bean.getId()+"',";
						
						if(userId.equals(bean.getCreateBy())){
							//创建人是我才算我建的，不算分享的
							if("week".equals(bean.getType())){
								weekWorkLogBean = bean;
								existWorkLogMap.put("week",bean);
							}else{
								existDateMap.put(bean.getWorkLogDate(), "true");
								existWorkLogMap.put(bean.getWorkLogDate(),bean);
							}
						}
					}
					if(workLogIds.endsWith(",")){
						workLogIds = workLogIds.substring(0,workLogIds.length()-1);
					}
					
					//查询明细
					if(!"".equals(workLogIds)){
						rs = mgt.workLogDetQuery(workLogIds);
						request.setAttribute("workLogDetMap",rs.retVal);
					}
				}
				request.setAttribute("discussCountMap",mgt.queryDiscussCount(workLogList));
				request.setAttribute("workLogList",workLogList);
				request.setAttribute("existWorkLogMap",existWorkLogMap);
				request.setAttribute("existDateMap",existDateMap);
				request.setAttribute("weekWorkLogBean",weekWorkLogBean);
				
			}
			
			//获取包含week的maP用于循环个人一周时间
			LinkedHashMap<String,String> fullWeekScopeMap = new LinkedHashMap<String, String>();
			fullWeekScopeMap.put("week","week");
			fullWeekScopeMap.putAll(weekScopeMap);
			request.setAttribute("fullWeekScopeMap",fullWeekScopeMap);

			//查询当前用户能选择的项目,用于生成任务
			ArrayList<Object> itemList = itemMgt.queryItemsByUserId(userId,true);
			request.setAttribute("itemList",itemList);
			
		}else{
			//共享日志
			if(workLogSearchForm.getTeamLogDate()==null || "".equals(workLogSearchForm.getTeamLogDate())){
				workLogSearchForm.setTeamLogDate(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd));
			}
			
			if(workLogSearchForm.getTeamLogType()==null || "".equals(workLogSearchForm.getTeamLogType())){
				workLogSearchForm.setTeamLogType("day");
			}
			
			if(personalEmpStr.endsWith(",")){
				personalEmpStr = personalEmpStr.substring(0,personalEmpStr.length()-1);
			}
			
			followByCondition.append(" and createBy in (").append(personalEmpStr).append(") and type = '").append(workLogSearchForm.getTeamLogType()).append("'");
			
			if("day".equals(workLogSearchForm.getTeamLogType())){
				followByCondition.append(" and workLogDate = '").append(workLogSearchForm.getTeamLogDate()).append("'");
			}else{
				followByCondition.append(" and workLogDate between '").append(monday).append("' and '").append(sunday).append("' ");
			}
			
			//我关注人信息
			Result rs = mgt.workLogQuery(followByCondition.toString(),followIds);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<OAWorkLogBean> followByList = (ArrayList<OAWorkLogBean>)rs.retVal;
				HashMap<String,String> existFollowMap = new HashMap<String, String>();
				for(OAWorkLogBean bean : followByList){
					existFollowMap.put(bean.getCreateBy(),"true");
				}
				rs = this.getWorkLogDet(followByList);
				request.setAttribute("discussCountMap",mgt.queryDiscussCount(followByList));
				request.setAttribute("followByDetMap",rs.retVal);
				request.setAttribute("followByList",followByList);
				request.setAttribute("existFollowMap",existFollowMap);
			}
			
		}
		
		
		//获取所有职员信息,用于textBox控件
		ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
		request.setAttribute("textBoxValues",gson.toJson(list));
		
		request.setAttribute("weekScopeMap",weekScopeMap);
		request.setAttribute("monday",monday);
		request.setAttribute("sunday",sunday);
		request.setAttribute("workLogSearchForm",workLogSearchForm);
		request.setAttribute("loginBean",loginBean);
		request.setAttribute("userId",userId);
		
		if(workLogSearchForm.getFollowEmpId()!=null && !"".equals(workLogSearchForm.getFollowEmpId())){
			return getForward(request, mapping,"queryByFollow");
		}
		
		request.setAttribute("templateWeekContent", new OAWorkLogTemplateMgt().getPlanTemplateContent(getLoginBean(request),"week"));
		request.setAttribute("templatedayContent", new OAWorkLogTemplateMgt().getPlanTemplateContent(getLoginBean(request),"day"));
		return getForward(request, mapping,"query");
	}

	
	/**
	 * 获取detBeanList
	 * @param contents 内容
	 * @param contentType 类型 1:总结 2:计划
	 * @param workLogId 日志ID
	 * @param updateTaskList 更新任务标题list
	 * @param isPlanTemplate 是否模板
	 * @return
	 */
	public ArrayList<OAWorkLogDetBean> getDetBeanList(String contents,String contentType,String workLogId,ArrayList<String[]> updateTaskList,String isPlanTemplate){
		ArrayList<OAWorkLogDetBean> list = new ArrayList<OAWorkLogDetBean>();
		String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		if(contents!=null && !"".equals(contents)){
			if(isPlanTemplate!=null && "true".equals(isPlanTemplate)){
				//启用模板
				OAWorkLogDetBean detBean = new OAWorkLogDetBean();
				detBean.setId(IDGenerater.getId());
				detBean.setContentType("2");
				detBean.setWorkLogId(workLogId);
				detBean.setContents(contents);
				detBean.setCreateTime(nowDate);
				list.add(detBean);
			}else{
				for(String str : contents.split("@")){
					if(!"".equals(str)){
						OAWorkLogDetBean detBean = new OAWorkLogDetBean();
						detBean.setId(IDGenerater.getId());
						detBean.setContentType(contentType);
						detBean.setWorkLogId(workLogId);
						detBean.setCreateTime(nowDate);
						if("1".equals(contentType)){
							//总结加上进度
							detBean.setContents(str.split("&&")[0]);
							detBean.setSchedule(str.split("&&")[1]);
						}else{
							detBean.setContents(str.split("&&")[0]);
						}
						
						String quoteInfo = str.split("&&")[2];
						if(quoteInfo!=null && !"".equals(quoteInfo)){
							if(!"empty".equals(quoteInfo.split(",")[0])){
								detBean.setRelationType(quoteInfo.split(",")[0]);
								detBean.setRelationId(quoteInfo.split(",")[1]);
							}
							
							//关联任务类型的update标题
							if(updateTaskList!=null && "OATask".equals(quoteInfo.split(",")[0])){
								String[] arrStr = new String[2];
								arrStr[0] = str.split("&&")[0];
								arrStr[1] = quoteInfo.split(",")[1];
								updateTaskList.add(arrStr);
							}
							if(updateTaskList!=null && "relationClient".equals(quoteInfo.split(",")[0])){
								String[] arrStr = new String[2];
								arrStr[0] = str.split("&&")[0];
								arrStr[1] = quoteInfo.split(",")[1];
								updateTaskList.add(arrStr);
							}
						}
						list.add(detBean);
					}
				}
			}
			
		}
		return list;
	}
	
	/**
	 * 根据周一获取本周信息map
	 * @param monday
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 */
	public LinkedHashMap<String,String> getWeekScopeMapByMonday(String monday) {
		LinkedHashMap<String,String> weekScopeMap = new LinkedHashMap<String, String>();//存放本周时间段map
		Calendar ca = Calendar.getInstance();
		try {
			ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//获取一周时间
		for(int i=1;i<8;i++){
			weekScopeMap.put(BaseDateFormat.format(ca.getTime(), BaseDateFormat.yyyyMMdd), GlobalsTool.getWeekCN(i));
			ca.add(Calendar.DAY_OF_MONTH,1);
		}
		return weekScopeMap;
	}
	
	/**
	 * 递归获取个人组织架构
	 * @param empList list存放所有匹配的职员
	 * @param departmentCodes 查询的deptIds
	 * @param levelLen 层次的长度
	 */
	public void getPersonalFrame(ArrayList<Object> empList,String empIds){
		if(empIds==null || "".equals(empIds) || personalFrameCount>10){
			return ;
		}else{
			//根据是否有上属上司判断
			String sql = "SELECT id,directBoss,EmpFullName from tblEmployee WHERE statusId = '0' " ;
			String empIdsStr = "";
			for(String empId : empIds.split(",")){
				if(!"".equals(empId)){
					empIdsStr +="'"+empId+"',";
				}
			}
			
			if(empIdsStr.endsWith(",")){
				empIdsStr = empIdsStr.substring(0,empIdsStr.length()-1);
			}
			
			if(!"".equals(empIdsStr)){
				sql +=" and directBoss IN ("+empIdsStr+") ";
			}
			
			Result rs = mgt.publicSqlQuery(sql, new ArrayList());
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
				if(list !=null && list.size()>0){
					empList.addAll(list);
					String selEmpIds = "";//存放递归调用的employeeIds
					for(Object obj : list){
						selEmpIds += GlobalsTool.get(obj,0)+",";
					}
					personalFrameCount++;
					getPersonalFrame(empList,selEmpIds);
				}else{
					return ;
				}
			}
		}
	}
	
	/**
	 * 发送信息给分享人
	 * @param loginBean
	 * @param workLogBean
	 * @param operationFlag add:表示添加,update表示更新
	 * @param beforeShareBy 更新前分享人ids
	 */
	public void sendMsgByShareBy(LoginBean loginBean,OAWorkLogBean workLogBean,String operationFlag,String beforeShareBy){
		if(workLogBean.getShareBy()!=null && !"".equals(workLogBean.getShareBy())){
			String title = loginBean.getEmpFullName()+"分享了";
			String typeContent = "";//连接标题内容
			if("day".equals(workLogBean.getType())){
				typeContent = workLogBean.getWorkLogDate()+"日志";
			}else{
				//获取周一与周日时间
				String monday =workLogBean.getWorkLogDate();
				String sunday ="";
				try {
					monday = new DateUtil().getMondayBySelTime(monday);
					Calendar ca = Calendar.getInstance();
					ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
					ca.add(Calendar.DAY_OF_MONTH,6);
					sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//存放周日
				} catch (ParseException e) {
					e.printStackTrace();
				}
				typeContent = monday+"至"+sunday+"周报";
			}
			title+=typeContent+"给你";
			
			String receiveIds = "";//发送职员ids
			
			if("add".equals(operationFlag) || beforeShareBy==null || "".equals(beforeShareBy)){
				//添加时
				receiveIds = workLogBean.getShareBy();
			}else{
				//更新时处理
				String newReceiveIds = "";//存放过滤发送给职员的ids
				String beforeShareByIds = ","+beforeShareBy;
				for(String empId : workLogBean.getShareBy().split(",")){
					if(beforeShareByIds.indexOf(","+empId+",") == -1){
						newReceiveIds += empId+",";
					}
				}
				receiveIds = newReceiveIds;
			}
			
			String url = "/OAWorkLogAction.do?operation=5&workLogId="+workLogBean.getId();
			String content = "<a href=\"javascript:mdiwin('" + url + "','"+typeContent+"')\">"+title+"</a>";//内容
			new AdviceMgt().add(loginBean.getId(), title, content, receiveIds, workLogBean.getId(), "OAWorkLogShareBy");
		}
		
	}
	
	/**
	 * 发送信息给关注我的人
	 * @param workLogBean 日志bean
	 * @param followMeIds 关注我的IDS
	 * @param loginBean 
	 */
	public void sendMsgByFollowMe(OAWorkLogBean workLogBean,String followMeIds,LoginBean loginBean){
		if(followMeIds!=null && !"".equals(followMeIds)){
			String title = loginBean.getEmpFullName()+"分享了";
			String typeContent = "";//连接标题内容
			if("day".equals(workLogBean.getType())){
				typeContent = workLogBean.getWorkLogDate()+"日志";
			}else{
				//获取周一与周日时间
				String monday =workLogBean.getWorkLogDate();
				String sunday ="";
				try {
					monday = new DateUtil().getMondayBySelTime(monday);
					Calendar ca = Calendar.getInstance();
					ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
					ca.add(Calendar.DAY_OF_MONTH,6);
					sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//存放周日
				} catch (ParseException e) {
					e.printStackTrace();
				}
				typeContent = monday+"至"+sunday+"周报";
			}
			title+=typeContent+"给你";
			
			String url = "/OAWorkLogAction.do?operation=5&workLogId="+workLogBean.getId();
			String content = "<a href=\"javascript:mdiwin('" + url + "','"+typeContent+"')\">"+title+"</a>";//内容
			new AdviceMgt().add(loginBean.getId(), title, content, followMeIds, workLogBean.getId(), "OAWorkLogFollowBy");
		}
	}
	
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean login = getLoginBean(req);
		if(login == null){
			BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
		}
		
		return null;
	}
	
	/**
	 * 获取日志明细Result
	 * @param worklogList
	 * @return
	 */
	public Result getWorkLogDet(ArrayList<OAWorkLogBean> worklogList){
		Result rs = new Result();
		if(worklogList!=null && worklogList.size()>0){
			String workLogIds = "";//存放主表IDS
			for(OAWorkLogBean bean : worklogList){
				workLogIds +="'"+bean.getId()+"',";
			}
			if(workLogIds.endsWith(",")){
				workLogIds = workLogIds.substring(0,workLogIds.length()-1);
			}
			if(!"".equals(workLogIds)){
				rs =  mgt.workLogDetQuery(workLogIds);
			}
		}
		
		return rs;
	}
	
	/**
	 * 获取客户名称
	 * @param clientId
	 * @return
	 */
	public String getClientName(String clientId){
		String clientName = "";
		if(clientId!=null && !"".equals(clientId)){
			Result result = mgt.findClient(clientId);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> rsList = (ArrayList<Object>)result.retVal;
				if(rsList!=null && rsList.size()>0){
					clientName = String.valueOf(GlobalsTool.get(rsList.get(0),0));
				}
			}
			
		}
		return clientName;
	}
	/**
	 * 日志列表查询
	 * @param mapping
	 * @param form 
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryLoginLogList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		LoginBean loginBean = (LoginBean)request.getSession().getAttribute("LoginBean");
		String clientId=request.getParameter("keyId");
		Result rs = mgt.queryLoginLogList(clientId, loginBean);
		if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("worklogList", rs.getRetVal());
		}
		return getForward(request, mapping, "workLogList");
	}
}
