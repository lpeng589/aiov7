package com.koron.oa.calendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.koron.oa.OACalendar.OACalendaMgt;
import com.koron.oa.OACalendar.OACalendarBean;
import com.koron.oa.bean.OAMyCalendarBean;
import com.koron.oa.individual.workPlan.OADateWorkPlanMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.OperationConst;

/**
 * 获取本月所有的日历
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 21, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class MyCalendarAjax extends HttpServlet {

	OAMyCalendarsMgt mgt = new OAMyCalendarsMgt();
	OACalendaMgt cmgt = new OACalendaMgt();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		
		String type=request.getParameter("type");
		if("move".equals(type)){
			moveCalendar(request, response);
		}else if("pro".equals(type)){
			addpage(request, response);
		}else if("queryMonth".equals(type)){			
			queryMonth(request, response);
		}else if("load".equals(type)){
			try {
				loadMonth(request, response);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 跳转到添加界面
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void addpage(HttpServletRequest request , HttpServletResponse response)
			throws IOException , ServletException{
		response.setContentType("text/html;charset=utf-8");
		String date = request.getParameter("date");
		String eventId = request.getParameter("eventId");

		if(eventId != null){
			OAMyCalendarBean obj = (OAMyCalendarBean) mgt.queryCalendar(eventId).getRetVal();
			if(obj == null || "".equals(obj)){
				request.setAttribute("error", "Error");
			}else{
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
			}
			
			request.setAttribute("eventId", eventId);
			request.setAttribute("calendarInfo", obj);
			request.setAttribute("operation",OperationConst.OP_UPDATE);
		}else{
			request.setAttribute("operation",OperationConst.OP_ADD);
		}
		
		Result rea = new EMailMgt().loadAlertByEamilId(eventId) ;
		if(rea.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("alert", rea.retVal) ;
		}
		request.setAttribute("date", date);
		
		request.getRequestDispatcher("/vm/oa/calendar/commonCalendar.jsp").forward(request, response) ;
	}
	
	
	/**
	 * 得到一个月的日历数据
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void queryMonth(HttpServletRequest request, HttpServletResponse response)
			throws IOException{
		
		response.setCharacterEncoding("utf-8");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		PrintWriter out = response.getWriter();
		List<OAMyCalendarBean> list = null;
		try {
			list = (List<OAMyCalendarBean>) mgt.queryMonth(start, end , loginBean.getId()).getRetVal();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		StringBuffer mycalendar_str = new StringBuffer();
		mycalendar_str.append(myCalendar_strs(list));
		out.print(mycalendar_str);
	}
	

	public String myCalendar_strs(List<OAMyCalendarBean> list){
		OAMyCalendarBean myCalendar= null;
		String mycalendar_str = "";
		int num = 1;
		if (list != null) {
			for (OAMyCalendarBean obj : list) {
				String dayClendar = "";
//				String str = obj.getCalendarTitle();
//				if (str != null && str != "") {
//					if (GlobalsTool.strLength(str) > 16) {
//						str = GlobalsTool.subTitle(str, 30) + "...";
//					}
//					obj.setCalendarTitle(str);
//				}
				myCalendar = obj;
				if (myCalendar != null) {
					dayClendar = "{\"id\": \""+myCalendar.getId()+"\",\"title\": \"" + myCalendar.getCalendarTitle()+"\",\"start\": \""+myCalendar.getCalendarDate()+"\",\"end\": \""+myCalendar.getEndDate()+"\",\"className\": \""+myCalendar.getCalendarLevel()+"\"}";
				}
				if(list.size() != num){
					dayClendar += ";";
				}
				mycalendar_str += dayClendar;
				num++;
			}
			mycalendar_str += "";
		}
		
		return mycalendar_str;
	}
	
	
	/**
	 * 日历移动/改变大小
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void moveCalendar(HttpServletRequest request,HttpServletResponse response) 
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String eventId= request.getParameter("eventId");
		Result rs = mgt.updateCalendar(start,end,eventId);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			out.print("false");
		} else {
			out.print("true");
		}
	}
	
	public String getMessageResources(HttpServletRequest request, String key) {
		String value = "";
		try {
			Object o = request.getSession().getServletContext().getAttribute(
					"userResource");
			if (o instanceof MessageResources) {
				MessageResources resources = (MessageResources) o;
				value = resources.getMessage(this.getLocale(request), key);
			}
			if (value == null || value == "") {
				o = request.getSession().getServletContext().getAttribute(
						org.apache.struts.Globals.MESSAGES_KEY);
				if (o instanceof MessageResources) {
					MessageResources resources = (MessageResources) o;
					value = resources.getMessage(this.getLocale(request), key);
				}
			}
		} catch (Exception e) {
		}
		return value;
	}

	private Locale getLocale(HttpServletRequest req) {
		Locale loc = null;
		Object obj = req.getSession(true).getAttribute(
				org.apache.struts.Globals.LOCALE_KEY);
		if (obj != null) {
			loc = (Locale) obj;
			return loc;
		}
		if (loc == null) {
			loc = req.getLocale();
		}
		if (loc == null) {
			loc = loc.getDefault();
		}
		return loc;

	}
	/**
	 * wyy
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public void loadMonth(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		response.setCharacterEncoding("utf-8");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String typename = GlobalsTool.toChinseChar(request.getParameter("typename"));
		String keyId = request.getParameter("id");
		String crmEnter = request.getParameter("crmEnter");//true表示CRM日程进入
		String clientId = request.getParameter("clientId");//CRM客户ID
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		
		
		PrintWriter out = response.getWriter();
	
		List<Object> list = null;
		HashMap<String, String> map = new HashMap<String, String>();
		//List<OACalendarBean> replist = new ArrayList<OACalendarBean>();
		try {
			list = (List<Object>) cmgt.queryCalendar(start, end , loginBean.getId(),GlobalsTool.getDeptCodeByUserId(loginBean.getId()),typename,keyId,crmEnter,clientId).getRetVal();
			Result rs = cmgt.selectType(loginBean.getId());
			ArrayList rsArr = (ArrayList)rs.retVal;
			if(rsArr !=null && rsArr.size()>0){
				for (int i = 0; i < rsArr.size(); i++){
					String key = ((Object[])rsArr.get(i))[1].toString();
					String value = ((Object[])rsArr.get(i))[2].toString();
					map.put(key, value);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		/*重复数据*/
		/*int i =0;
		for (Object bean : list) {
			
			if("week".equals(((Object[])list.get(i))[3].toString())){
				//重复结束时间大于结束时间
				Calendar cal2 = Calendar.getInstance();
				Calendar cal3 = Calendar.getInstance();
				cal2.setTime(sdf.parse(((Object[])list.get(i))[7].toString()));
				cal3.setTime(sdf.parse(((Object[])list.get(i))[6].toString()));
				cal.setTime(sdf.parse(((Object[])list.get(i))[5].toString()));
				int day1 = cal3.get(Calendar.DAY_OF_YEAR);
				int day2 = cal.get(Calendar.DAY_OF_YEAR);
				if((day1-day2) < 6){				
					cal.add(Calendar.DATE, 7);
					cal3.add(Calendar.DATE, 7);
					while(cal.compareTo(cal2)<=0){
						String color = ((Object[])list.get(i))[8]==null?"":((Object[])list.get(i))[8].toString();
						OACalendarBean newBean = new OACalendarBean();
						newBean.setId(((Object[])list.get(i))[0].toString());
						newBean.setUserId(((Object[])list.get(i))[4].toString());
						newBean.setTitle(((Object[])list.get(i))[1].toString());
						newBean.setType(((Object[])list.get(i))[2].toString()+";"+color);			
						newBean.setStatus(((Object[])list.get(i))[3].toString());	
						newBean.setRepeatFinish(((Object[])list.get(i))[7].toString());						
						newBean.setStratTime(sdf.format(cal.getTime()));						
						if(cal3.compareTo(cal2) >0){
							newBean.setFinishTime(((Object[])list.get(i))[7].toString());
						}else{
							newBean.setFinishTime(sdf.format(cal3.getTime()));
						}
						cal.add(Calendar.DATE, 7);
						cal3.add(Calendar.DATE, 7);
						replist.add(newBean);
					}	
				};												
			}
			if("month".equals(((Object[])list.get(i))[3].toString())){
				//重复结束时间大于结束时间
				Calendar cal2 = Calendar.getInstance();
				Calendar cal3 = Calendar.getInstance();
				cal2.setTime(sdf.parse(((Object[])list.get(i))[7].toString()));				
				cal.setTime(sdf.parse(((Object[])list.get(i))[5].toString()));
				cal3.setTime(sdf.parse(((Object[])list.get(i))[6].toString()));
				int num3 = cal.getActualMaximum(Calendar.DAY_OF_MONTH); 
				cal.add(Calendar.DATE, num3);
				cal3.add(Calendar.DATE, num3);
				while(cal.compareTo(cal2)<=0){
					OACalendarBean newBean = new OACalendarBean();
					String color = ((Object[])list.get(i))[8]==null?"":((Object[])list.get(i))[8].toString();
					newBean.setId(((Object[])list.get(i))[0].toString());
					newBean.setUserId(((Object[])list.get(i))[4].toString());
					newBean.setTitle(((Object[])list.get(i))[1].toString());
					newBean.setType(((Object[])list.get(i))[2].toString()+";"+color);			
					newBean.setStatus(((Object[])list.get(i))[3].toString());	
					newBean.setRepeatFinish(((Object[])list.get(i))[7].toString());											
					newBean.setStratTime(sdf.format(cal.getTime()));
					if(cal3.compareTo(cal2) >0){
						newBean.setFinishTime(((Object[])list.get(i))[7].toString());
					}else{
						newBean.setFinishTime(sdf.format(cal3.getTime()));
					}
					cal.add(Calendar.DATE, num3);
					cal3.add(Calendar.DATE, num3);
					replist.add(newBean);
				}
			}
			i++;
		}*/
		//获取数量
		Result numRs = cmgt.getNumByType(loginBean.getId(),start, end);
		HashMap<String, String> mapNum = new HashMap<String, String>();
		mapNum = (HashMap<String, String>) numRs.retVal;
		request.setAttribute("mapList", mapNum);
		
		StringBuffer mycalendar_str = new StringBuffer();	
		mycalendar_str.append(calendarTostrs(list,map)+"|"+mapNum);		
		out.print(mycalendar_str);
	}
		
	public String calendarTostr(List<OACalendarBean> list){
		OACalendarBean myCalendar= null;
		String mycalendar_str = "";
		int num = 1;
		if (list != null) {
			for (OACalendarBean obj : list) {
				String dayClendar = "";			
				myCalendar = obj;
				String[] colors = myCalendar.getType().split(";");
				String color = "";
				if(colors.length>1){
					color = colors[1];
				}
				if (myCalendar != null) {
					dayClendar = "{\"id\": \""+myCalendar.getId()+"\",\"title\": \"" 
					+ myCalendar.getTitle()+"\",\"start\": \""+myCalendar.getStratTime()+"\",\"end\": \""
					+myCalendar.getFinishTime()+"\",\"color\": \""+color+"\"}";
				}
				if(list.size() != num){
					dayClendar += ";";
				}
				mycalendar_str += dayClendar;
				num++;
			}
			mycalendar_str += "";
		}
		
		return mycalendar_str;
	}
	public String calendarTostrs(List<Object> list,HashMap<String, String> map){		
		String mycalendar_str = "";		
		int num = 1;
		if (list != null) {
			int i=0;
			for (Object obj : list) {				
				String dayClendar = "";	
				String color = map.get(((Object[])list.get(i))[2].toString()) == null || "".equals(map.get(((Object[])list.get(i))[2].toString()))?"":map.get(((Object[])list.get(i))[2].toString()); 
				String tilte = "";
				if(((Object[])list.get(i))[2].toString().indexOf("默认")>-1){
					color = "438ab4";
					tilte = ((Object[])list.get(i))[1].toString();
				}else if(((Object[])list.get(i))[2].toString().indexOf("待办")>-1){
					color = "9670c7";
					tilte = "待办:"+((Object[])list.get(i))[1].toString();
				}else if(((Object[])list.get(i))[2].toString().indexOf("会议")>-1){
					color = "cb9e5d";
					tilte = "会议:"+((Object[])list.get(i))[1].toString();
				}else if(((Object[])list.get(i))[2].toString().indexOf("日志")>-1){
					color = "a8bb48";
					tilte = "日志:"+((Object[])list.get(i))[1].toString();
				}else if(((Object[])list.get(i))[2].toString().indexOf("任务")>-1){
					color = "68aa63";
					tilte = "任务:"+((Object[])list.get(i))[1].toString();
				}else if(((Object[])list.get(i))[2].toString().indexOf("项目")>-1){
					color = "358560";
					tilte = "项目:"+((Object[])list.get(i))[1].toString();
				}else if(((Object[])list.get(i))[2].toString().indexOf("客户")>-1){
					color = "b1846f";
					tilte = "客户:"+((Object[])list.get(i))[1].toString();
				}
				String relationId = ((Object[])list.get(i))[7]==null?"":((Object[])list.get(i))[7].toString();	
				String finishStatus = ((Object[])list.get(i))[9]==null?"0":((Object[])list.get(i))[9].toString();
				if (obj != null) {
					dayClendar = "{\"id\": \""+((Object[])list.get(i))[0].toString()+"\",\"title\": \"" 
					+tilte+"\",\"start\": \""+((Object[])list.get(i))[4].toString()+"\",\"end\": \""
					+((Object[])list.get(i))[5].toString()+"\",\"color\": \""+color+"\",\"delstatus\": \""
					+((Object[])list.get(i))[6].toString()+"\",\"relationId\": \""+relationId+"\",\"type\": \""+((Object[])list.get(i))[2].toString()+"\",\"finishStatus\": \""+finishStatus+"\"}";
				}
				if(list.size() != num){
					dayClendar += ";";
				}
				dayClendar = dayClendar.replaceAll("\\r\\n", "").replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
				mycalendar_str += dayClendar;			
				num++;
				i++;
			}
			mycalendar_str += "";
		}
		
		return mycalendar_str;
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
