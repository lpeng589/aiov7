package com.koron.oa.OACalendar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;

/**
 * 
 * <p>Title:我的日程</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: 深圳市科荣软件有限公司
 * @Author wyy
 */
public class OACalendarAction extends BaseAction{	
	OACalendaMgt mgt = new OACalendaMgt();
	AdviceMgt admgt = new AdviceMgt();
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);		
		ActionForward forward = null;
		String type = getParameter("type", request);
		
		//获取所有职员信息,用于textBox控件
		ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
		request.setAttribute("textBoxValues",gson.toJson(list));
		
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		switch (operation) {		
		case OperationConst.OP_ADD:
			forward = addCalendar(mapping, form, request, response);
			break;
		/*异步 添加分类*/	
		case OperationConst.OP_ADD_PREPARE:
			if("addCalendar".equals(type)){
				forward = addCalendarPrepare(mapping, form, request, response);
			}else{
				forward = addType(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUOTE:
			forward = updateColor(mapping, form, request, response);
			break; 
			
		
		case OperationConst.OP_UPDATE:	
			String updateType = request.getParameter("updateType");
			if("status".equals(updateType)){
				/*完成状态*/
				forward = updateStatus(mapping, form, request, response);	
			}else if("batchOperation".equals(updateType)){
				forward = updBatchOperation(mapping, form, request, response);	
			}else{
				forward = updateCalendar(mapping, form, request, response);	
			}											
			break;
		case OperationConst.OP_DETAIL:			
			forward = detailCalendar(mapping, form, request, response);				
			break;
		case OperationConst.OP_QUERY:
			String queryFlag = request.getParameter("queryFlag");
			if("List".equals(queryFlag)){
				/* 日程列表查询*/
				forward = calendarList(mapping, form, request, response);
			}else{
				forward = queryCalendar(mapping, form, request, response);
			}						           	           															
			break;
		
		case OperationConst.OP_DELETE:	
			String delFlag = request.getParameter("delFlag");
			if("TYPE".equals(delFlag)){
				forward = delType(mapping, form, request, response);
			}else{
				forward = delCalendar(mapping, form, request, response);
			}
			break;
		
		default:	
				forward = queryCalendar(mapping, form, request, response); 
		}
		return forward;
	}
	
	/**
	 * 批量操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updBatchOperation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String finishStatusVal =getParameter("finishStatusVal",request);
		String ids =getParameter("ids",request);
		Result rs = mgt.updBatchOperation(finishStatusVal, ids);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	private ActionForward updateStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id =request.getParameter("id");		
		String finishStatus = request.getParameter("finishStatus");
		if(id != null && !"".equals(id)){
			OACalendarBean bean = (OACalendarBean)mgt.loadCalendar(id).retVal;
			if(finishStatus == null || "".equals(finishStatus)){
				finishStatus = "0";
			}
			bean.setFinishStatus(finishStatus);
			Result rs = mgt.updateCalendar(bean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg", "3");//成功
			}else{
				request.setAttribute("msg", "-1");
			}
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 详情页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detailCalendar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String keyId = getParameter("keyId",request);//主键id
		String relationId = getParameter("relationId",request);//关联id
		OACalendarBean bean = mgt.getCalendarByRelationId(keyId,relationId);

		if(bean!=null && bean.getClientId()!=null && !"".equals(bean.getClientId())){
			String clientName = mgt.getClientNameById(bean.getClientId());
			request.setAttribute("calendarClientName",clientName);
		}
		request.setAttribute("bean",bean);
		return getForward(request, mapping, "detail");
	}

	/**
	 * 添加日程前
	 * @param mapping
	 * @param form 
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException, 
	 */
	private ActionForward addCalendarPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ParseException, Exception{
		String crmEnter = getParameter("crmEnter",request);
		String calendarFlag = request.getParameter("calendarFlag");
		String dateTime = request.getParameter("dateTime");
		//分类
		
		Result res = mgt.selectType(getLoginBean(request).getId());		
		request.setAttribute("typeNames", res.retVal);
		if("true".equals(crmEnter)){
			request.setAttribute("onlyShow", getParameter("onlyShow", request));			
			/*客户日程的*/
			if("detail".equals(calendarFlag)){
				String keyId = getParameter("eventId",request);//主键id
				String relationId = getParameter("relationId",request);//关联id
				OACalendarBean bean = mgt.getCalendarByRelationId(keyId,null);

				if(bean!=null && bean.getClientId()!=null && !"".equals(bean.getClientId())){
					String clientName = mgt.getClientNameById(bean.getClientId());
					request.setAttribute("calendarClientName",clientName);
				}
				request.setAttribute("calendarFlag", calendarFlag);
				request.setAttribute("bean",bean);			
				return getForward(request, mapping, "detail");
			}else{
				String clientId = getParameter("calendarClientId",request);
				if(clientId!=null && !"".equals(clientId)){
					String clientName = mgt.getClientNameById(clientId);
					request.setAttribute("calendarClientName",clientName);
				}
				request.setAttribute("calendarFlag", "");
				request.setAttribute("calendarStratTime", dateTime);
				request.setAttribute("calendarFinishTime", dateTime);
				request.setAttribute("calendarClientId",clientId);
			}			
		}else{
			if("new".equals(calendarFlag)){		
				
				request.setAttribute("calendarFlag", calendarFlag);
				request.setAttribute("calendarStratTime", dateTime);
				request.setAttribute("calendarFinishTime", dateTime);
			}else if("detail".equals(calendarFlag)){
				String eventId = request.getParameter("eventId");
				if(eventId != null && !"".equals(eventId)){
					Result rs = mgt.eventCalendar(eventId);
					ArrayList rsRs = (ArrayList)rs.retVal;
					if(rsRs!=null && rsRs.size()>0){
						request.setAttribute("calendarList", rsRs.get(0));
						//提醒时间
						if(((Object[])rsRs.get(0))[7]!=null && !"".equals(((Object[])rsRs.get(0))[7])){
							Calendar cal = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							cal.setTime(sdf.parse(((Object[])rsRs.get(0))[7].toString()));				
							int hours = cal.get(Calendar.HOUR_OF_DAY);
							int mitues = cal.get(Calendar.MINUTE);	
							
							request.setAttribute("dayTimes", ((Object[])rsRs.get(0))[7].toString());
							request.setAttribute("hours", hours);
							request.setAttribute("mitues", mitues);
						}
					}
					
				}		
				request.setAttribute("calendarFlag", calendarFlag);	
			}	
		}					
		return getForward(request, mapping, "addCalendarPrepare");
	}

	private ActionForward calendarList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			
			OACalendarForm lvForm = (OACalendarForm)form;
			request.setAttribute("userId", lvForm.getUserId());
			request.setAttribute("listTitle", lvForm.getListTitle());
			request.setAttribute("type", lvForm.getType());
			request.setAttribute("createTime", lvForm.getCreateTime());
			request.setAttribute("endTime", lvForm.getEndTime());
			request.setAttribute("clientName", lvForm.getClientName());
			request.setAttribute("finishStatus", lvForm.getFinishStatus());
			
			
			String pageNo = request.getParameter("pageNo");
			
			//客户日程进入的只能选择客户日程类型
			if(request.getParameter("crmEnter")==null || "".equals(request.getParameter("crmEnter"))){
				Result res = mgt.queryTypeList(getLoginBean(request).getId());
				if(res.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("typeList", res.retVal);			
				}
			}else{
				lvForm.setType("客户日程");
				lvForm.setCrmEnter("true");
				request.setAttribute("crmEnter", lvForm.getCrmEnter());
			}
			
			String clientId = getParameter("clientId",request);
			request.setAttribute("clientId",clientId);
			StringBuffer condition=new StringBuffer("");
			//获取人员范围权限
			ArrayList allScopeList = getLoginBean(request).getAllScopeRight();
			for (Object o : allScopeList) {
				String strUserIds = "" ;
				LoginScopeBean lsb = (LoginScopeBean) o;
				if(lsb!=null && "1".equals(lsb.getFlag())){
					for(String strId : lsb.getScopeValue().split(";")){
						strUserIds += "'"+strId+"'," ;
					}
					strUserIds = strUserIds.substring(0, strUserIds.length()-1) ;
					condition.append(" or ").append("D.userId in (").append(strUserIds).append(")");
				}
			}
			
			Result rs = mgt.queryList(lvForm, getLoginBean(request).getId(),clientId,condition.toString());
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("rsList", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
			}
			request.setAttribute("lvForm",lvForm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getForward(request, mapping, "calendarList");
	}

	/**
	 * 手动添加日程，别的功能页面不能调这方法导入日程
	 * @return
	 */
	public static String addCalendar(OACalendarBean bean) {

		if (null == bean.getId() || "".equals(bean.getId())) {
			bean.setId(IDGenerater.getId());
		}
		if (null == bean.getUserId() || "".equals(bean.getUserId())) {
			return "创建人不能为空";
		}
		if (null == bean.getTitle() || "".equals(bean.getId())) {
			return "标题不能为空";
		}
		if (null == bean.getType() || "".equals(bean.getType())) {
			bean.setType("默认分类");
		}
		if (null == bean.getStratTime() || "".equals(bean.getStratTime())) {
			return "开始时间不能为空";
		}
		if (null == bean.getFinishTime() || "".equals(bean.getFinishTime())) {
			return "结束时间不能为空";
		}
		bean.setDelStatus("0");// 可删除
		Result rs = new OACalendaMgt().addCalendar(bean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 发通知消息
			return "";
		} else {
			return "插入数据库失败";
		}
	}
	
	/**
	 * 添加日程
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addCalendar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException{
		String title = request.getParameter("title");
		String finishTime = request.getParameter("finishTime");		
		String stratTime = request.getParameter("stratTime");
		String type = request.getParameter("type");		
		String alertTime = request.getParameter("alertTime");
		String clientId = request.getParameter("clientId");
		String relationId = request.getParameter("relationId");//关联ID
		String finishStatus = request.getParameter("finishStatus");
		String taskId = request.getParameter("taskId");
		
		String userId = getLoginBean(request).getId();
		String id = IDGenerater.getId();
		
		OACalendarBean bean = new OACalendarBean();
		bean.setUserId(userId);
		bean.setId(id);
		bean.setTitle(title);
		bean.setType(type);
		bean.setStratTime(stratTime);
		bean.setFinishTime(finishTime);
		bean.setAlertTime(alertTime);
		bean.setClientId(clientId);
		bean.setRelationId(relationId);
		bean.setFinishStatus(finishStatus);
		bean.setTaskId(taskId);
		
		String err = addCalendar(bean);
		if ("".equals(err)) {
			if(relationId!=null && !"".equals(relationId)){
				String  relationCount = mgt.queryRelationCount(bean.getRelationId(), bean.getType());			
				request.setAttribute("msg",relationCount);
			}else{
				//String dataList = getStr(id, userId);				
				request.setAttribute("msg",id+";"+getColorByType(type,userId));
			}
		} else {		
			request.setAttribute("msg", "err:" + err);
		}
		
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 修改日程
	 * @param userId	登录者ID
	 * @param title		标题
	 * @param type		类型
	 * @param beginTime	开始时间
	 * @param endTimd	结束时间
	 * @param alertTime	提醒时间
	 * @return
	 */
	public static String updateCalendar(OACalendarBean bean) {

		if (null == bean.getId() || "".equals(bean.getId())) {
			bean.setId(IDGenerater.getId());
		}
		if (null == bean.getUserId() || "".equals(bean.getUserId())) {
			return "创建人不能为空";
		}
		if (null == bean.getTitle() || "".equals(bean.getId())) {
			return "标题不能为空";
		}
		if (null == bean.getType() || "".equals(bean.getType())) {
			bean.setType("默认分类");
		}
		if (null == bean.getStratTime() || "".equals(bean.getStratTime())) {
			return "开始时间不能为空";
		}
		if (null == bean.getFinishTime() || "".equals(bean.getFinishTime())) {
			return "结束时间不能为空";
		}

		Result rs = new OACalendaMgt().updateCalendar(bean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return "";
		} else {
			return "修改失败";
		}
	}
	
	/**
	 * 更新日程
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException 
	 */
	private ActionForward updateCalendar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		String id =request.getParameter("id");
		String title = request.getParameter("title");
		String finishTime = request.getParameter("finishTime");		
		String stratTime = request.getParameter("stratTime");
		String alertTime = request.getParameter("alertTime");	
		String type = request.getParameter("type");		
		String clientId = request.getParameter("clientId");
		String userId = getLoginBean(request).getId();
		//String finishStatus = request.getParameter("finishStatus");
		String taskId = request.getParameter("taskId");
		// 加载原来数据
		OACalendarBean bean = (OACalendarBean) mgt.loadCalendar(id).retVal;
		// 更新 更新的人不是创建人 返回错
		if (!bean.getUserId().equals(userId)
				|| !"0".equals(bean.getDelStatus())) {
			System.out.println(userId);
			request.setAttribute("msg", "no");
		} else {
			bean.setTitle(title);
			bean.setType(type);
			bean.setStratTime(stratTime);
			bean.setFinishTime(finishTime);
			bean.setAlertTime(alertTime);
			bean.setClientId(clientId);
			//bean.setFinishStatus(finishStatus);
			bean.setTaskId(taskId);
			String err = updateCalendar(bean);
			if ("".equals(err)) {				
				request.setAttribute("msg", id+";"+getColorByType(type,userId));
				// 发通知消息
				// 改变通知
			} else {
				request.setAttribute("msg", "err:" + err);
			}
		}
		return getForward(request, mapping, "blank");
	}
	/**
	 * 更改颜色
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateColor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String typeId = request.getParameter("typeId");
		String color=request.getParameter("color");
		Result rs = mgt.changeColor(typeId,color,getLoginBean(request).getId());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", 3);
		}
		return getForward(request, mapping, "blank");		
	}
	/**
	 * 添加分类判断唯一性
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type =request.getParameter("typeName");
		String color = request.getParameter("color");
		
		//判断唯一
		Result uqion = mgt.getTypeColor(type,getLoginBean(request).getId());
		ArrayList uqArr = (ArrayList)uqion.retVal;
		if(uqArr!=null && uqArr.size()>0){					
			request.setAttribute("msg", "");									
		}else{			
			if(!"".equals(type)){
				String id = IDGenerater.getId();
				Result rs = mgt.addType(id,getLoginBean(request).getId(), type,color);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("msg", id+";"+color);
				}	
			}
		}		
		return getForward(request, mapping, "blank");
	}
	/**
	 * 删除分类 默认不能删除，日程中有该分类不能删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {		
		String type = GlobalsTool.toChinseChar(request.getParameter("type"));
		String typeId = GlobalsTool.toChinseChar(request.getParameter("typeId"));
		//判断日程中有该分类
		Result isext = mgt.getExist(type,getLoginBean(request).getId());
		ArrayList isRs = (ArrayList)isext.retVal;
		if(isRs !=null && isRs.size() >0){
			request.setAttribute("msg", "no");
		}else{			
			Result rs = mgt.delType(typeId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg", 3);
			}
		}
		return getForward(request, mapping, "blank");		
	}
	
	public static boolean delCalendar(String id) {
		Result rs = new OACalendaMgt().delCalendar(id);
		return rs.retCode == ErrorCanst.DEFAULT_SUCCESS;
	}
	
	/**
	 * 删除自己添加的日程 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delCalendar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String msg = "3";
		if(id !=null && !"".equals(id)){
			for (String keyId : id.split(";")) {
				if(!"".equals(keyId)){
					OACalendarBean bean = (OACalendarBean)mgt.loadCalendar(keyId).retVal; 
					//不是本人登陆或者是导入数据
					if(!bean.getUserId().equals(getLoginBean(request).getId()) || !"0".equals(bean.getDelStatus())){
						msg = "该日程是生成数据或不是用户自己创建，不能删除";
					}
				}
			}
		}
		OACalendarBean bean = (OACalendarBean)mgt.loadCalendar(id).retVal; 
		//不是本人登陆或者是导入数据
		if("3".equals(msg)){		
			Result rs = mgt.delCalendar(id);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){				
				request.setAttribute("msg", msg);
				//取消通知
				
			}
		}else{
			request.setAttribute("msg", msg);
		}		
		return getForward(request, mapping, "blank");
		//return getForward(request, mapping, "alert");
	}
	/**
	 * 查询日程 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryCalendar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String typename = GlobalsTool.toChinseChar(request.getParameter("typename"));
		request.setAttribute("typename",typename);
		//查询分类
		try{
			Result res = mgt.selectType(getLoginBean(request).getId()); 
			request.setAttribute("typeNames", res.retVal);
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(getParameter("crmEnter",request));
		request.setAttribute("crmEnter",getParameter("crmEnter",request));//true表示从CRM日程模块进入
		request.setAttribute("clientId",getParameter("clientId",request));//CRM客户ID
		return getForward(request, mapping, "queryCalendar");
	}
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean login = getLoginBean(req);
		if(login == null){
			BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
		}
		//进行唯一用户验证，如果有生复登陆的，则后进入用户踢出前进入用户
        if (!OnlineUserInfo.checkUser(req)) {
            //需踢出
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }
		return null;
	}

	/**
	 * 获取type和颜色的map
	 * @param id
	 * @param loginId
	 * @return
	 * @throws ParseException
	 */
	public String getColorByType(String type,String loginId) throws ParseException{		
		HashMap<String, String> map = new HashMap<String, String>();
		try {			
			Result rs = mgt.selectType(loginId);
			ArrayList rsArr = (ArrayList)rs.retVal;
			if(rsArr !=null && rsArr.size()>0){
				for (int i = 0; i < rsArr.size(); i++){
					String key = ((Object[])rsArr.get(i))[1].toString();
					String value = ((Object[])rsArr.get(i))[2].toString();
					map.put(key, value);
				}
			}
			map.put("客户日程", "b1846f");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map.get(type);
	}	
	/*转化 */
	public String calendarTostrs(List<Object> list,HashMap<String, String> map){		
		String mycalendar_str = "";
		int num = 1;
		if (list != null) {
			int i=0;
			for (Object obj : list) {
				String dayClendar = "";	
				String color = map.get(((Object[])list.get(i))[2].toString()) == null || "".equals(map.get(((Object[])list.get(i))[2].toString()))?"":map.get(((Object[])list.get(i))[2].toString()); 
				if("默认分类".equals(((Object[])list.get(i))[2].toString())){
					color = "438ab4";
				}
				if (obj != null) {
					dayClendar = "{\"id\": \""+((Object[])list.get(i))[0].toString()+"\",\"title\": \"" 
					+ ((Object[])list.get(i))[1].toString()+"\",\"start\": \""+((Object[])list.get(i))[4].toString()+"\",\"end\": \""
					+((Object[])list.get(i))[5].toString()+"\",\"color\": \""+color+"\"}";
				}
				if(list.size() != num){
					dayClendar += ";";
				}
				mycalendar_str += dayClendar;
				num++;
				i++;
			}
			mycalendar_str += "";
		}
		
		return mycalendar_str;
	}
}
