package com.koron.oa.calendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.OAMyCalendarBean;
import com.koron.oa.individual.workPlan.OADateWorkPlanMgt;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 * 
 * 
 * <p>Title:我的日历</p> 
 * <p>Description: </p>
 *
 * @Date:2012-5-25
 * @Copyright: 科荣软件
 * @Author 方佳�?

 */
public class MyCalendarsAction extends MgtBaseAction{	
	
	OAMyCalendarsMgt mgt = new OAMyCalendarsMgt();
	protected ActionForward exe(ActionMapping mapping, ActionForm form, 
								HttpServletRequest request, 
								HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
        	forward = addPreCalendar(mapping, form, request, response);
        	break;
        case OperationConst.OP_ADD:
            forward = addCalendar(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE_PREPARE:
            forward = updatePrepareCalendar(mapping, form, request,response);
            break;
        case OperationConst.OP_UPDATE:
        	forward = updateCalendar(mapping, form, request, response);
        	break;
        case OperationConst.OP_OA_VIEW_SINLE:
        	forward = querySingleCalendar(mapping, form, request, response);
        	break;
        case OperationConst.OP_DELETE:
        	String types = request.getParameter("types");
        	if(types != null && "types".equals(types)){
        		forward = deleteCalendar(mapping, form, request, response);
        	}else{
        		forward = deleteCalendarList(mapping, form, request,response);
        	}
        	
        	break;
        case OperationConst.OP_DETAIL:
        	forward = querySingleCalendar(mapping, form, request, response);
        	break;
        default:
        	String type=request.getParameter("type");
        	if (null != type && !type.equals("")) {
        		if("list".equals(type)){
        			forward = calendarList(mapping, form, request, response);
        		}
        	}else{
        		forward = goCalendar(mapping, form, request, response);
        	}
        }
        return forward;
	}
	
	/**
	 * 日历首页
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward goCalendar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
		
		return getForward(request, mapping, "calendar");
	}
	
	/**
	 * 添加前的准备
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward addPreCalendar(ActionMapping mapping, ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response) throws Exception{
		String calendar_time = getParameter("strDate", request) ;
    	String type2 = getParameter("type2", request) ;
    	request.setAttribute("type2", type2);
    	if(calendar_time==null || calendar_time.length()==0){
    		calendar_time = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
    	}
    	String calendardate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
        request.setAttribute("calendar_time", calendar_time);
        request.setAttribute("calendardate", calendardate);
        return getForward(request, mapping, "addPrepareCalendar");
	}
	
	/**
	 * 添加日历
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward addCalendar(ActionMapping mapping,ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response) throws Exception{
		OAMyCalendarForm calendarform = (OAMyCalendarForm) form;

        String type2 = getParameter("type2",request);
        String id = IDGenerater.getId();
        String userId = this.getLoginBean(request).getId();
        String creatTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
        OAMyCalendarBean myCalendar = new OAMyCalendarBean();
        read(calendarform, myCalendar);
        myCalendar.setId(id);
        myCalendar.setCreateBy(userId);
        myCalendar.setLastupdateBy(userId);
        myCalendar.setCreateTime(creatTime);
        myCalendar.setLastupdateTime(creatTime);
        Result rs = mgt.addMyCalendar(myCalendar);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	request.setAttribute("dealAsyn", "true");
         	request.setAttribute("noAlert", "true");
			//添加成功
        	String flo = request.getParameter("flo");
        	//添加提醒
        	if(flo != null && "1".equals(flo)){
        		addAlert(request, response,id,myCalendar.getCalendarDate(),myCalendar.getCalendarTitle());
        	}
        	if(type2 != null && !"".equals(type2)){
        		EchoMessage.success().add(getMessage(request, "common.msg.addSuccess"))
        					.setBackUrl(type2.equals("1")?"/OAMyCalendarList.do?type=list" : "/OAMyCalendar.do").
        					setAlertRequest(request);
        		return getForward(request, mapping, "message");
        	}else{
            	EchoMessage.success().add(getMessage(request,"common.msg.addSuccess"))
    	    				.setBackUrl("/OAMyCalendar.do").
    	    				setAlertRequest(request) ;
            	return getForward(request, mapping, "alert");
        	}
        	
		} else {
			//添加失败
			if(type2 != null && !"".equals(type2)){
					
				 EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
				 		.setBackUrl(type2.equals("1")?"/OAMyCalendarList.do?type=list" : "/OAMyCalendar.do?").
				 		setAlertRequest(request);
				 return getForward(request, mapping, "message");
			}else{
				EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
						.setBackUrl(type2.equals("1")?"/OAMyCalendarList.do?type=list" : "/OAMyCalendar.do?").setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
			
		}
	}
	
	/**
	 * 添加提醒
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void addAlert(HttpServletRequest request,
			HttpServletResponse response,String keyid,String calendarDate,String calendarTitle) throws IOException, ServletException {

		PrintWriter out = response.getWriter();
		LoginBean loginBean = getLoginBean(request);
		String alertDate = request.getParameter("alertDate"); /* 提醒日期 */
		String alertHour = request.getParameter("alertHour"); /* 提醒小时 */
		String alertMinute = request.getParameter("alertMinute"); /* 提醒分钟 */
		String isLoop = request.getParameter("isLoop"); /* 循环提醒 */
		String loopType = request.getParameter("loopType"); /* 循环类型 */
		int loopTime = Integer.parseInt(request.getParameter("loopTime")); /* 循环次数 */
		String endDate = request.getParameter("endDate"); /* 结束日期 */
		String[] alertType = request.getParameterValues("alertType"); /* 提醒方式 */
		String popedomUserIds = request.getParameter("popedomUserIds"); /* 提醒用户ID */
		String popedomDeptIds = request.getParameter("popedomDeptIds"); /* 提醒部门ID */

		String falg = request.getParameter("falg");
		String alertUrl = "";
		String typestr = request.getParameter("typestr")+calendarDate+"["+calendarTitle+"]"; /* 连接的显示文�? */
		String urls = GlobalsTool.toChinseChar(request.getParameter("urls")); /* 连接的路�? */
		if(urls != null && !"".equals(urls)){
			urls += "&id="+keyid;
		}
		if ("false".equals(falg)) {
			popedomUserIds = loginBean.getId() + ",";
		}
		String title = request.getParameter("title"); /* 连接的标�? */
		alertUrl = "<a href=\"javascript:mdiwin('" + urls + "','" + title
					+ "')\">" + typestr+ "</a>"; /* 得到总的连接路径 */
		String strAlertType = "";
		for (String strType : alertType) {
			strAlertType += strType + ",";
		}
		if (strAlertType.length() == 0) {
			out.print("");
		}
		if (popedomDeptIds == null || popedomDeptIds == null) {
			popedomUserIds = loginBean.getId();
		}

		AlertBean alertBean = new AlertBean();
		alertBean.setId(IDGenerater.getId());
		alertBean.setAlertDate(alertDate);
		alertBean.setAlertHour(Integer.parseInt(alertHour));
		alertBean.setAlertMinute(Integer.parseInt(alertMinute));
		alertBean.setIsLoop(isLoop);
		alertBean.setLoopType(loopType);
		alertBean.setLoopTime(loopTime);
		alertBean.setEndDate(endDate);
		alertBean.setAlertContent(typestr);
		alertBean.setAlertType(strAlertType);
		alertBean.setCreateBy(loginBean.getId());
		alertBean.setCreateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		alertBean.setRelationId(keyid);
		alertBean.setNextAlertTime(alertDate + " " + alertHour + ":"
				+ alertMinute + ":00");
		alertBean.setStatusId(0);
		alertBean.setPopedomUserIds(popedomUserIds);
		alertBean.setPopedomDeptIds(popedomDeptIds);
		alertBean.setAlertUrl(alertUrl);
		Result result = new EMailMgt().addAlert(alertBean);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//添加成功
			
		}
	}
	
	
	/**
	 * 修改日历
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateCalendar(ActionMapping mapping,ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response) throws Exception{
		OAMyCalendarForm calendarform = (OAMyCalendarForm) form;
		String type2 = getParameter("type2",request);
		String userId = this.getLoginBean(request).getId();
		String time = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
		OAMyCalendarBean calendarbean=new OAMyCalendarBean();
		read(calendarform, calendarbean);
		calendarbean.setLastupdateBy(userId);
		calendarbean.setLastupdateTime(time);
		Result rs = mgt.updateMyCalendar(calendarbean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("dealAsyn", "true");
			request.setAttribute("noAlert", "true");
			//修改成功
			String flo = request.getParameter("flo");
			//添加提醒
        	if(flo != null && "1".equals(flo)){
        		addAlert(request, response,calendarbean.getId(),calendarbean.getCalendarDate(),calendarbean.getCalendarTitle());
        	}
			if(type2 != null && !"".equals(type2) && "1".equals(type2)){
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
		 		.setBackUrl("/OAMyCalendarList.do?type=list").
		 		setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
        	EchoMessage.success().add(getMessage(request,"common.msg.updateSuccess"))
	    	.setBackUrl("/OAMyCalendar.do").setAlertRequest(request);
        	
        	return getForward(request, mapping, "alert");
		} else {
			//修改失败
			if(type2 != null && !"".equals(type2) && "1".equals(type2)){
				EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
		 		.setBackUrl("/OAMyCalendarList.do?type=list").
		 		setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
					.setBackUrl("/OAMyCalendar.do").setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
	}
	
	/**
	 * 删除日历
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteCalendar(ActionMapping mapping,ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response) throws Exception{
		String eventId=request.getParameter("id");
		Result rs=mgt.deleteMyCalendar(eventId);
		String msg="";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//删除成功
        	msg="success";
		} else {
			//删除失败
			msg="failure";
		}
		request.setAttribute("msg", msg);
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 列表中删�?

	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward deleteCalendarList(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) {
        String[] keyIds = request.getParameterValues("keyId");
        
       Result result = mgt.deleteCalaendars(keyIds);
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			    EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
			            .setBackUrl("/OAMyCalendarList.do?type=list").
			            setAlertRequest(request);
		} else {
			    EchoMessage.success().add(getMessage(
			            request, "common.msg.delError"))
			            .setBackUrl("/OAMyCalendarList.do?type=list").
			            setAlertRequest(request);
		}
        return getForward(request, mapping, "message");
	}
	/**
	 * 日历详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
    private ActionForward querySingleCalendar(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) {

    	try{
    	
	        String id = request.getParameter("id");
	        OAMyCalendarBean obj = (OAMyCalendarBean) mgt.queryCalendar(id).getRetVal();
	        if(obj == null){
	        	EchoMessage.error().add(getMessage(request, "news.not.find"))
				.setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
	        }
	        String calendarDate = obj.getCalendarDate();
	        String day ="";
	        if(calendarDate!=null&&calendarDate!=""){
	        	day =calendarDate.substring(0, 10);
	        }
	        if(obj.getAssPeople()!=null && obj.getAssPeople().indexOf(";")!=-1){
				String keyIds = "(" ;
					for(String str : obj.getAssPeople().split(";")){
						keyIds += "'" + str +"'," ;
					}
				keyIds = keyIds.substring(0,keyIds.length()-1) ;
				keyIds += ")" ;
				Result result = new OADateWorkPlanMgt().queryAssPeopleName(keyIds) ;
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("assPeopleName", result.retVal) ;
				}
			}
			if(obj.getAssClient()!=null && obj.getAssClient().indexOf(";")!=-1){
				String clientIds = "(" ;
				for(String str : obj.getAssClient().split(";")){
					clientIds += "'" + str +"'," ;
				}
				clientIds = clientIds.substring(0,clientIds.length()-1) ;
				clientIds += ")" ;
				Result result = new OADateWorkPlanMgt().queryAssClient(clientIds) ;
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("assClientName", result.retVal) ;
				}
			}
			String isEspecial=request.getParameter("isEspecial")==null?"":request.getParameter("isEspecial");
	        request.setAttribute("isEspecial", isEspecial);
			request.setAttribute("listType", request.getParameter("listType"));
	        request.setAttribute("day", day);
	        String url = request.getRequestURI() ;
	        String favoriteURL = java.net.URLEncoder.encode(url+"?operation=5&id="+id+"&isEspecial=1");
	        request.setAttribute("favoriteURL", favoriteURL) ;
	        obj.setCalendarTitle(GlobalsTool.encodeHTML(obj.getCalendarTitle()));
	        request.setAttribute("myCalendar", obj);      
    	}catch (Exception e) {
			// TODO: handle exception
    		EchoMessage.error().add(getMessage(request, "news.not.find"))
			.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
			
		}
        return getForward(request, mapping, "singleMyCalendar");
    }
	
    /**
     * 修改�?

     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward updatePrepareCalendar(ActionMapping mapping,
								            ActionForm form,
								            HttpServletRequest request,
								            HttpServletResponse response) {
		String id = request.getParameter("id");
		Result res = mgt.queryCalendar(id);
		OAMyCalendarBean obj =new OAMyCalendarBean();
		if(res.retCode == ErrorCanst.DEFAULT_SUCCESS){
			obj = (OAMyCalendarBean)res.retVal;
		}
		
		if(obj.getAssPeople()!=null && obj.getAssPeople().indexOf(";")!=-1){
			String keyIds = "(" ;
				for(String str : obj.getAssPeople().split(";")){
					keyIds += "'" + str +"'," ;
				}
			keyIds = keyIds.substring(0,keyIds.length()-1) ;
			keyIds += ")" ;
			Result result = new OADateWorkPlanMgt().queryAssPeopleName(keyIds) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("assPeopleName", result.retVal) ;
			}
		}
		if(obj.getAssClient()!=null && obj.getAssClient().indexOf(";")!=-1){
			String clientIds = "(" ;
			for(String str : obj.getAssClient().split(";")){
				clientIds += "'" + str +"'," ;
			}
			clientIds = clientIds.substring(0,clientIds.length()-1) ;
			clientIds += ")" ;
			Result result = new OADateWorkPlanMgt().queryAssClient(clientIds) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("assClientName", result.retVal) ;
			}
		}
		request.setAttribute("myCalendar", obj);
		return getForward(request, mapping, "updatePrepareCalendar");
    }
    
    
    
    /**
     * 日历列表
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    
    private ActionForward calendarList(ActionMapping mapping, 
    									ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response){
    	OAMyCalendarSearchForm oaForm = (OAMyCalendarSearchForm)form;
    	if(oaForm.getKeyWord() != null && !"".equals(oaForm.getKeyWord())){
    		oaForm.setKeyWord(GlobalsTool.toChinseChar(oaForm.getKeyWord()));
    	}
    	String userId = this.getLoginBean(request).getId();
    	Result result = mgt.queryCalendar(userId, oaForm);
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("list", result.retVal);
    		request.setAttribute("pageBar", pageBar(result, request));
    	}
    	return getForward(request, mapping, "myCalenderlist");
    }
    
	/**
	 * 重写权限判断的方�?,因为此模块不�?要权限判�?,�?以返回null
	 */
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
	    LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
		return null;
	}
}
