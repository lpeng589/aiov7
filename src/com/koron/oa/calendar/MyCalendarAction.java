package com.koron.oa.calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.OAMyCalendar;
import com.koron.oa.individual.workPlan.OADateWorkPlanMgt;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
public class MyCalendarAction extends MgtBaseAction {

    public MyCalendarAction() {
    }

    OAMyCalendarMgt mgt = new OAMyCalendarMgt();
    BaseDateFormat format = new BaseDateFormat() ;
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        int operation = getOperation(request);
        ActionForward forward = null;
        String noback=request.getParameter("noback");//涓嶈兘杩斿洖
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		request.setAttribute("MOID",((MOperation)getLoginBean(request).getOperationMap().get("/OAMyCalendar.do")).getModuleId()) ;
        String type = request.getParameter("type");
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
            forward = addPrepareCalendar(mapping, form, request,response);
            break;
        case OperationConst.OP_ADD:
            forward = addCalendar(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE:
            forward = deleteCalendar(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE_PREPARE:
            forward = updatePrepareCalendar(mapping, form, request,response);
            break;
        case OperationConst.OP_UPDATE:
            forward = updateCalendar(mapping, form, request, response);
            break;
        case OperationConst.OP_QUERY:
            forward = queryCalendar(mapping, form, request, response);
            break;
        case OperationConst.OP_OA_VIEW_SINLE:
            forward = querySingleCalendar(mapping, form, request, response);
            break;
        case OperationConst.OP_CANCEL_ALERT:
			forward = cancelAlert(mapping, form, request, response) ;
			break ;
        default:
            if (null != type && !type.equals("")) {
                if (type.equals("MyCalender")) {
                    forward = queryCalendar(mapping, form, request,response);
                }
                if (type.equals("list")) {
                    forward = calendarList(mapping, form, request, response);
                }
            } else {
                forward = goCalendar(mapping, form, request, response);
            }
        }
        return forward;
    }
    
    /**
	 * 取消 提醒设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward cancelAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String calendarId = getParameter("calendarId", request) ;
		String alertId    = getParameter("alertId", request) ;
		String listType   = getParameter("listType", request) ;
		String month  = getParameter("month", request) ;
		String year   = getParameter("year", request) ;
		
		Result result = mgt.deleteAlert(alertId) ; 
		if(result.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add("取消提醒设置成功")
					.setBackUrl("/OAMyCalendar.do?operation=77&listType="+listType
							  +"&year="+year+"&month="+month+"&id="+calendarId)
					.setAlertRequest(request);
		}else{
			EchoMessage.error().add("取消提醒设置失败")
					.setBackUrl("/OAMyCalendar.do?operation=77&listType="+listType
							  +"&year="+year+"&month="+month+"&id="+calendarId)
					.setAlertRequest(request);
		}
		return getForward(request, mapping, "alert") ;
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

        String id = request.getParameter("id");
        String keyWord = request.getParameter("keyWord");
        String desktop=request.getParameter("desktop");
        String pageNo=request.getParameter("pageNo");
        String listType = request.getParameter("listType") ;
        
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("desktop", desktop);
        request.setAttribute("listType", listType) ;
        String isEspecial=request.getParameter("isEspecial")==null?"":request.getParameter("isEspecial");
        request.setAttribute("isEspecial", isEspecial);
        
 	   	if("GET".equals(request.getMethod())){
 	   		keyWord=keyWord==null?"":GlobalsTool.toChinseChar(keyWord);
 	   	}
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        OAMyCalendar obj = (OAMyCalendar) mgt.getMyCalendar(id).getRetVal();
        String calendarDate = obj.getCalendarDate();
        String day ="";
        if(calendarDate!=null&&calendarDate!=""){
        	day =calendarDate.substring(0, 10);
        }
        Date date_calendar = new Date();
        try {
            date_calendar = BaseDateFormat.parse(calendarDate,BaseDateFormat.yyyyMMddHHmmss);
        } catch (Exception ex) {
        }
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("day", day);
        request.setAttribute("keyWord", keyWord);
        if(date_calendar!=null){
        	request.setAttribute("hour", date_calendar.getHours());
            request.setAttribute("minute", date_calendar.getMinutes());
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
        String wakeupTypes = obj.getWakeUpType();
        String array_wakeup[]=null;
        if(wakeupTypes!=null){
        	array_wakeup = wakeupTypes.split(",");
        }
        
        /*判断是否存提醒设置*/
//		Result result = new EMailMgt().loadAlertByEamilId(id) ;
//		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
//			request.setAttribute("alert", result.retVal) ;
//		}
		
        request.setAttribute("array_wakeup", array_wakeup);
        obj.setCalendarTitle(GlobalsTool.encodeHTML(obj.getCalendarTitle()));
        request.setAttribute("myCalendar", obj);      
        String url = request.getRequestURI() ;
		String favoriteURL = java.net.URLEncoder.encode(url+"?operation=77&id="+id+"&year=2010&month=6&isEspecial=1");
		request.setAttribute("favoriteURL", favoriteURL) ;
        return getForward(request, mapping, "singleMyCalendar");
    }

    private ActionForward deleteCalendar(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        
    	String year = request.getParameter("year");
        String month = request.getParameter("month");
        String keyWord = request.getParameter("keyWord");
        String pageNo=request.getParameter("pageNo");
        if("GET".equals(request.getMethod())){
        	keyWord=keyWord==null?"":GlobalsTool.toChinseChar(keyWord);
        }
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        String[] keyIds = getParameters("keyId", request) ;
        
        String strKeyIds = "" ;
        for (String key : keyIds) {
            strKeyIds += "'"+key+"'," ;
        }
        if(strKeyIds.length()>0){
        	strKeyIds = strKeyIds.substring(0, strKeyIds.length()-1) ;
        }
        Result result = mgt.Delete(strKeyIds);
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			    EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
			            .setBackUrl("/OAMyCalendar.do?type=list&year=" + year +"&month=" + month+"&keyWord="+GlobalsTool.encode(keyWord)+"&pageNo="+pageNo).
			            setAlertRequest(request);
		} else {
			    EchoMessage.success().add(getMessage(
			            request, "common.msg.delError"))
			            .setBackUrl("/OAMyCalendar.do?type=list&year=" + year +"&month=" + month+"&keyWord="+GlobalsTool.encode(keyWord)+"&pageNo="+pageNo).
			            setAlertRequest(request);
		}
        return getForward(request, mapping, "message");

    }

    private ActionForward calendarList(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        String userId = this.getLoginBean(request).getId();
        String date_calendar = "";
        try {
            date_calendar = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
        } catch (IllegalArgumentException ex) {
        }
        int year = Integer.parseInt(date_calendar.substring(0, 4));
        int month = Integer.parseInt(date_calendar.substring(5, 7));
        String year_str = request.getParameter("year");
        String month_str = request.getParameter("month");
        String calendar_time = date_calendar.substring(0, 10);
        if (null != year_str && !"".equals(year_str)&&!"null".equals(year_str)) {
            year = Integer.parseInt(year_str);
        }
        if (null != month_str && !"".equals(month_str)&&!"null".equals(month_str)) {
            month = Integer.parseInt(month_str);
        }
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("calendar_time", calendar_time);
        List<OAMyCalendar> list = null;
        try {
            list = mgt.getMyCalendarByMonth(year, month, userId);
        } catch (Exception ex1) {
            EchoMessage.error().add(getMessage(request,"com.currentaccbefbill")).
                    setAlertRequest(request);
            return getForward(request, mapping, "alert");
        }

        String keyWord = request.getParameter("keyWord");
 	   if("GET".equals(request.getMethod()))
       {
       	keyWord=keyWord==null?"":GlobalsTool.toChinseChar(keyWord);
       }
        request.setAttribute("keyWord", keyWord);
        List<OAMyCalendar> newList = new ArrayList<OAMyCalendar>();
        if (null != keyWord && !"".equals(keyWord)) {
            if (null != list) {
                for (OAMyCalendar obj : list) {
                    if (obj.getCalendarTitle().indexOf(keyWord) >= 0 ||
                        obj.getCalendarContext().indexOf(keyWord) >= 0) {
                        newList.add(obj);
                    }
                }
            }
            list = newList;
        }
        request.setAttribute("list", list);
        return getForward(request, mapping, "myCalenderlist");
    }

   /* private ActionForward deleteCalendar(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        String id = request.getParameter("id");
        String calendar_time = request.getParameter("calendar_time");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String userId = this.getLoginBean(request).getId();
        Result rs = mgt.Delete(id);
        boolean talg = true;
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
            talg = true;
            List<OAMyCalendar> list = null;
            try {
                list = (List<OAMyCalendar>) mgt.SelectByCalendarDay(calendar_time, userId).getRetVal();
            } catch (Exception ex) {
                ex.printStackTrace();
                EchoMessage.error().add(getMessage(request,
                        "com.currentaccbefbill")).
                        setAlertRequest(request);
                return getForward(request, mapping, "alert");
            }
            request.setAttribute("calendar_time", calendar_time);
            String LunarCalendar = DateBean.getLunarCalendar(calendar_time);
            request.setAttribute("LunarCalendar", LunarCalendar);
            request.setAttribute("list", list);
        } else {
            talg = false;
        }
        if (talg) {
            EchoMessage.success().add(getMessage(
                    request, "common.msg.delSuccess"))
                    .setBackUrl("/OAMyCalendar.do?type=list&year=" + year +
                                "&month=" + month).
                    setAlertRequest(request);
        } else {
            EchoMessage.success().add(getMessage(
                    request, "common.msg.delError"))
                    .setBackUrl("/OAMyCalendar.do?type=list&year=" + year +
                                "&month=" + month).
                    setAlertRequest(request);
        }
        return getForward(request, mapping, "message");
    }*/

    private ActionForward updatePrepareCalendar(ActionMapping mapping,
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        String id = request.getParameter("id");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String keyWord = request.getParameter("keyWord");
        String pageNo=request.getParameter("pageNo");
 	    if("GET".equals(request.getMethod())){
 	    	keyWord=keyWord==null?"":GlobalsTool.toChinseChar(keyWord);
        }
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("pageNo", pageNo);
        OAMyCalendar obj = (OAMyCalendar) mgt.getMyCalendar(id).getRetVal();
        String calendarDate = obj.getWakeUpBeginDate();
        Date date_calendar = new Date();
        try {
            date_calendar = BaseDateFormat.parse(calendarDate, BaseDateFormat.yyyyMMddHHmmss);
        } catch (Exception ex) {
        	ex.printStackTrace() ;
        	BaseEnv.log.error("MycalendarAction updatePrepare:",ex) ;
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

    private ActionForward updateCalendar(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        
    	String id = request.getParameter("id");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String keyWord = request.getParameter("keyWord");
        String pageNo = request.getParameter("pageNo");
        String assPeople = request.getParameter("assPeople") ;
        String assClient = request.getParameter("assClient") ;
        
        if("GET".equals(request.getMethod())) {
        	keyWord=keyWord==null?"":GlobalsTool.toChinseChar(keyWord);
        }
       
        OAMyCalendar myCalendar = (OAMyCalendar) mgt.getMyCalendar(id).getRetVal();

        String CalendarTitle = request.getParameter("CalendarTitle");
        String CalendarContext = request.getParameter("CalendarContext");
        String userId = this.getLoginBean(request).getId();
        String creatTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
        boolean talg = true;
        if (null != myCalendar) {
            myCalendar.setlastUpdateBy(userId);
            myCalendar.setlastUpdateTime(creatTime);
            myCalendar.setCalendarContext(CalendarContext);
            myCalendar.setCalendarTitle(CalendarTitle);
            myCalendar.setcreateBy(userId) ;
            myCalendar.setAssClient(assClient) ;
            myCalendar.setAssPeople(assPeople) ;
            Result rs = mgt.Update(myCalendar);
            if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
                talg = true;
            } else {
                talg = false;
            }
        } else {
            talg = false;
        }
		if (talg) {
		    EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
		            .setBackUrl("/OAMyCalendar.do?type=list&year=" + year +
		                        "&month=" + month+"&keyWord="+GlobalsTool.encode(keyWord)+"&pageNo="+pageNo).
		            setAlertRequest(request);
		} else {
		    EchoMessage.success().add(getMessage(request, "common.msg.updateErro"))
		            .setBackUrl("/OAMyCalendar.do?type=list&year=" + year +
		                        "&month=" + month+"&keyWord="+GlobalsTool.encode(keyWord)+"&pageNo="+pageNo).
		            setAlertRequest(request);
		}
        return getForward(request, mapping, "message");
    }

    private ActionForward goCalendar(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
    	String userId = this.getLoginBean(request).getId();
    	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
    	String date=sf.format(new Date());
    	String year  = request.getParameter("year");
    	String month = request.getParameter("month");
    	request.setAttribute("year", year);
    	request.setAttribute("month", month);
    	List list = (List<OAMyCalendar>)mgt.SelectAllCalendar(userId).getRetVal();
    	request.setAttribute("list", list);
    	request.setAttribute("len", list.size());
        return getForward(request, mapping, "calendar");
    }

    private ActionForward queryCalendar(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        String calendar_time = request.getParameter("calendar_time");
        String userId = this.getLoginBean(request).getId();
        if (null == calendar_time) {
            calendar_time = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMdd);
        }
        List<OAMyCalendar> list = null;
        try {
            list = (List<OAMyCalendar>) mgt.SelectByCalendarDay(calendar_time, userId).getRetVal();
        } catch (Exception ex) {
            ex.printStackTrace();
            EchoMessage.error().add(getMessage(request,"com.currentaccbefbill")).
                    setAlertRequest(request);
            return getForward(request, mapping, "alert");

        }
        request.setAttribute("calendar_time", calendar_time);
        String LunarCalendar = DateBean.getLunarCalendar(calendar_time);
        request.setAttribute("LunarCalendar", LunarCalendar);
        request.setAttribute("list", list);
        EchoMessage.error().add(getMessage(request,
                        "com.currentaccbefbill")).
         setAlertRequest(request);

        return getForward(request, mapping, "alert");
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
    private ActionForward addCalendar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
    	String year  = getParameter("year",request);
        String month = getParameter("month",request);
        String type2 = getParameter("type2",request);
        
        String assPeople = getParameter("assPeople",request) ;
        String assClient = getParameter("assClient",request) ;
        
        String calendarDate    = getParameter("CalendarDate", request) ;
        String calendarTitle   = getParameter("CalendarTitle",request);
        String calendarContext = getParameter("CalendarContext",request);
        
        /*日历内容*/
        OAMyCalendar myCalendar = new OAMyCalendar();
        String userId = this.getLoginBean(request).getId();
        String calendarId = IDGenerater.getId();
        String creatTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
        myCalendar.setid(calendarId);
        myCalendar.setcreateBy(userId);
        myCalendar.setlastUpdateBy(userId);
        myCalendar.setCalendarDate(calendarDate);
        myCalendar.setcreateTime(creatTime);
        myCalendar.setlastUpdateTime(creatTime);
        myCalendar.setCalendarContext(calendarContext);
        myCalendar.setCalendarTitle(calendarTitle);
        myCalendar.setAssClient(assClient) ;
        myCalendar.setAssPeople(assPeople) ;
        String msgWake = getMessage(request, "com.oa.calendarWake") ;
        
        /*日历提醒*/
        AlertBean alertBean = null;
//		String alertDate = request.getParameter("alertDate"); 		/*提醒日期*/
//		String alertHour = request.getParameter("alertHour"); 		/*提醒小时*/
//		String alertMinute = request.getParameter("alertMinute"); 	/*提醒分钟*/
//		String isLoop = request.getParameter("isLoop"); 		  	/*循环提醒*/
//		String loopType = request.getParameter("loopType"); 		/*循环类型*/
//		int loopTime = Integer.parseInt(request.getParameter("loopTime")); /*循环次数*/
//		String endDate = request.getParameter("endDate"); 			/*结束日期*/
//		String[] alertType = request.getParameterValues("alertType"); /*提醒方式*/
//		if (alertType != null && alertType.length > 0) {
//			String strAlertType = "";
//			for (String strType : alertType) {
//				strAlertType += strType + ",";
//			}
//			String yearn=alertDate.substring(0,3);
//			String monthn=alertDate.substring(5,6);
//			String context="<a href="+'"'+"javascript:read('"+calendarId+"');mdiwin('/OAMyCalendar.do?operation=77&listType=image&id="+calendarId+"&year="+yearn+"&month="+monthn+"','日历提醒通知')"+'"'+">"+msgWake+calendarTitle+"</a>";
//			alertBean = new AlertBean() ;
//			alertBean.setId(IDGenerater.getId());
//			alertBean.setAlertDate(alertDate);
//			alertBean.setAlertHour(Integer.parseInt(alertHour));
//			alertBean.setAlertMinute(Integer.parseInt(alertMinute));
//			alertBean.setAlertContent(context);
//			alertBean.setIsLoop(isLoop);
//			alertBean.setLoopType(loopType);
//			alertBean.setLoopTime(loopTime);
//			alertBean.setEndDate(endDate);
//			alertBean.setAlertType(strAlertType);
//			alertBean.setCreateBy(getLoginBean(request).getId());
//			alertBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
//			alertBean.setRelationId(calendarId);
//			alertBean.setNextAlertTime(alertDate + " " + alertHour + ":" + alertMinute + ":00");
//			alertBean.setStatusId(0);
//			alertBean.setPopedomUserIds(getLoginBean(request).getId());
//		}
        Result rs = mgt.Insert(myCalendar,msgWake,alertBean);
        String year2=calendarDate.substring(0,4);
		String month2=calendarDate.substring(5,7);
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
            EchoMessage.success().add(getMessage(request, "common.msg.addSuccess"))
                    .setBackUrl(type2.equals("1")?"/OAMyCalendar.do?type=list&year=" + year +"&month=" + month : "/OAMyCalendar.do?year="+year2+"&month="+month2).
                    setAlertRequest(request);
        } else {
            EchoMessage.success().add(getMessage(request, "common.msg.addFailture"))
                    .setBackUrl(type2.equals("1")?"/OAMyCalendar.do?type=list&year=" + year +"&month=" + month : "/OAMyCalendar.do?year="+year2+"&month="+month2).
                    setAlertRequest(request);
        }
        return getForward(request, mapping, "message");
    }


    private ActionForward addPrepareCalendar(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
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
}
