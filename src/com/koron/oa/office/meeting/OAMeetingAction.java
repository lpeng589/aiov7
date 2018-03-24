package com.koron.oa.office.meeting;
/**
 * <p>
 * Title:会议管理
 * <p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-7-24
 * @CopyRight:科荣软件
 * @Author:柯志良
 */
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.OACalendar.OACalendaMgt;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
public class OAMeetingAction extends BaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	private OAMeetingMgt meetingMgt=new OAMeetingMgt();
	private OABoardroomMgt bmgt=new OABoardroomMgt();
	private OANoteMgt noteMgt = new OANoteMgt();
	OACalendaMgt omgt = new OACalendaMgt();
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/Meeting.do?operation=4");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删除权限*/
		int operation = getOperation(request);
		String requestType=getParameter("requestType",request);
		if(requestType == null){
			requestType ="";
		}
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		System.out.println(getParameter("addTHhead", request));
		ActionForward forward = null;
		switch(operation){
		// 新增操作
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		//添加会议之前准备
		case OperationConst.OP_ADD_PREPARE:
			allBoardroom(request);
			forward = addPrepare(mapping, form, request, response);
			break;
			//修改会议之前准备
		case OperationConst.OP_UPDATE_PREPARE:
			allBoardroom(request);
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			if(requestType.equals("WHYBOX")){
				forward = whyBox(mapping, form, request, response);
			}else if(requestType.equals("CANCEL")){
				forward =cancel(mapping, form, request, response);
			}else{
				forward = updateMeeting(mapping, form, request, response);
			}
			break;	
		case OperationConst.OP_DELETE:
			forward = deleteMeeting(mapping, form, request, response);
			break;	
			
	
		//查询
		case OperationConst.OP_QUERY:
			if(requestType.equals("SIGNIN")){
				forward = signin(mapping, form, request, response);
			}else if(requestType.equals("RESETSIGNIN")){
				forward =resetSignin(mapping, form, request, response);
			}else if(requestType.equals("TAKER")){
				forward =taker(mapping, form, request, response);
			}else if(requestType.equals("ADVICE")){
				forward =adviceUser(mapping, form, request, response);
			}else if(requestType.equals("NOTE")){
				allBoardroom(request);
				forward =openNote(mapping, form, request, response);
			}else if(requestType.equals("OCCUPATION")){
				forward = boardroomOccupation(mapping, form, request, response);
			}else if(requestType.equals("BOARDROOMWEEK")){
				allBoardroom(request);
				forward = boardroomWeek(mapping, form, request, response);
			}else if(requestType.equals("ADDNOTE")){
				forward = addOrUpdateNote(mapping, form, request, response);
			}else if(requestType.equals("MEETROOMUSING")){
				//会议使用情况
				forward = meetRoomUsing(mapping, form, request, response);
			}else{
				allBoardroom(request);
				forward = query(mapping, form, request, response);
			}
			break;
			default:
				 allBoardroom(request);
			     forward = query(mapping, form, request, response);
			break;
		}
		
		
		return forward;
	}

	private ActionForward meetRoomUsing(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception, ParseException {
		String dateTime = request.getParameter("dateTime");
		/*只是查询出除例会的会议*/
		Result roomUsing = meetingMgt.meetRoomUsing(dateTime);
		ArrayList roomRs = (ArrayList)roomUsing.retVal;
		HashMap<String, ArrayList> maps = new HashMap<String, ArrayList>();
		if(roomRs != null && roomRs.size()>0){		
			for (int i = 0; i < roomRs.size(); i++) {
				
				if(roomRs.get(i) !=null){	
					ArrayList param = new ArrayList();
					param.add(roomRs.get(i));					
					if(maps.get(((Object[])roomRs.get(i))[0]) == null){						
						maps.put(((Object[])roomRs.get(i))[0].toString(),param);
					}else{
						maps.get(((Object[])roomRs.get(i))[0].toString()).add(roomRs.get(i));					
					}
				}
			}
		}
		/*查询例会的*/
		Result gularUsing = meetingMgt.meetGularUsing();
		ArrayList gularRs = (ArrayList)gularUsing.retVal;
		if(gularRs !=null && gularRs.size()>0){
			//计算重复shuju例会
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
			for (int j = 0; j < gularRs.size(); j++) {				
				Object regularend = ((Object[])gularRs.get(j))[7];
				if(regularend !=null){
					//比较结束时间是否大于现在
					if(dateTime.compareTo(regularend.toString().substring(0,10))<=0){
						if("1".equals(((Object[])gularRs.get(j))[5].toString())){
							//天循环
							long enDate = sdf.parse(dateTime).getTime();
							long stDate = sdf.parse(((Object[])gularRs.get(j))[4].toString().substring(0,10)).getTime();
							
							if((enDate-stDate)>=0 && (enDate-stDate)%(24*60*60) == 0){
								ArrayList param = new ArrayList();
								param.add(gularRs.get(j));					
								if(maps.get(((Object[])gularRs.get(j))[0]) == null){						
									maps.put(((Object[])gularRs.get(j))[0].toString(),param);
								}else{
									maps.get(((Object[])gularRs.get(j))[0].toString()).add(gularRs.get(j));					
								}
							}
							
						}else if("2".equals(((Object[])gularRs.get(j))[5].toString())){
							//周循环
							long enDate = sdf.parse(dateTime).getTime();
							long stDate = sdf.parse(((Object[])gularRs.get(j))[4].toString().substring(0,10)).getTime();
							
							if((enDate-stDate)>=0 && (enDate-stDate)%(24*60*60*7) == 0){
								ArrayList param = new ArrayList();
								param.add(gularRs.get(j));					
								if(maps.get(((Object[])gularRs.get(j))[0]) == null){						
									maps.put(((Object[])gularRs.get(j))[0].toString(),param);
								}else{
									maps.get(((Object[])gularRs.get(j))[0].toString()).add(gularRs.get(j));					
								}
							}
							
						}else if("3".equals(((Object[])gularRs.get(j))[5].toString())){
							//yue循环
							//long enDate = sdf.parse(dateTime).getTime();
							//long stDate = sdf.parse(((Object[])gularRs.get(j))[4].toString().substring(0,10)).getTime();
							String dateTime1 = dateTime.substring(8, 10);
							String dateTime2 = ((Object[])gularRs.get(j))[4].toString().substring(8,10);
							if(dateTime1.compareTo(dateTime2) ==0){
								ArrayList param = new ArrayList();
								param.add(gularRs.get(j));					
								if(maps.get(((Object[])gularRs.get(j))[0]) == null){						
									maps.put(((Object[])gularRs.get(j))[0].toString(),param);
								}else{
									maps.get(((Object[])gularRs.get(j))[0].toString()).add(gularRs.get(j));					
								}
							}
						}
					}
				}else{
					if("1".equals(((Object[])gularRs.get(j))[5].toString())){
						//天循环
						long enDate = sdf.parse(dateTime).getTime();
						long stDate = sdf.parse(((Object[])gularRs.get(j))[4].toString().substring(0,10)).getTime();
						if((enDate-stDate)>=0 && (enDate-stDate)%(24*60*60) == 0){
							ArrayList param = new ArrayList();
							param.add(gularRs.get(j));					
							if(maps.get(((Object[])gularRs.get(j))[0]) == null){						
								maps.put(((Object[])gularRs.get(j))[0].toString(),param);
							}else{
								maps.get(((Object[])gularRs.get(j))[0].toString()).add(gularRs.get(j));					
							}
						}
						
					}else if("2".equals(((Object[])gularRs.get(j))[5].toString())){
						//周循环
						long enDate = sdf.parse(dateTime).getTime();
						long stDate = sdf.parse(((Object[])gularRs.get(j))[4].toString().substring(0,10)).getTime();
						if((enDate-stDate)>=0 && (enDate-stDate)%(24*60*60*7) == 0){
							ArrayList param = new ArrayList();
							param.add(gularRs.get(j));					
							if(maps.get(((Object[])gularRs.get(j))[0]) == null){						
								maps.put(((Object[])gularRs.get(j))[0].toString(),param);
							}else{
								maps.get(((Object[])gularRs.get(j))[0].toString()).add(gularRs.get(j));					
							}
						}
						
					}else if("3".equals(((Object[])gularRs.get(j))[5].toString())){
						//yue循环
						long enDate = sdf.parse(dateTime).getTime();
						long stDate = sdf.parse(((Object[])gularRs.get(j))[4].toString()).getTime();
						if((enDate-stDate)>=0 && (enDate-stDate)%(24*60*60*30) == 0){
							ArrayList param = new ArrayList();
							param.add(gularRs.get(j));					
							if(maps.get(((Object[])gularRs.get(j))[0]) == null){						
								maps.put(((Object[])gularRs.get(j))[0].toString(),param);
							}else{
								maps.get(((Object[])gularRs.get(j))[0].toString()).add(gularRs.get(j));					
							}
						}
					}
					
				}
				
			}
		}
		
		//封装会议的数据
		request.setAttribute("mapsList", maps);
		//获取会议室
		Result room = meetingMgt.getRoom();
		request.setAttribute("roomList", room.retVal);
		request.setAttribute("dateTime", dateTime);
		request.setAttribute("requestType", getParameter("requestType", request));
		return getForward(request, mapping, "roomUsing");
	}

	/**
	 * 新增会议前的准备
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String turnOut = request.getParameter("turnOut");
		request.setAttribute("turnOut", turnOut);
		request.setAttribute("backUrl", request.getParameter("backUrl"));
		
		//获取所有职员信息,用于textBox控件
		ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
		request.setAttribute("textBoxValues",gson.toJson(list));
		
		if("true".equals(getParameter("isMeetTh", request))){
	       	 request.setAttribute("operationType", getParameter("operationType", request));
	       	 return getForward(request, mapping, "oameeting");
        }
		return getForward(request, mapping, "to_addMeeting");
	}
	
	
	/**
	 *修改会议前的准备 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("backUrl", request.getParameter("backUrl"));
		OAMeetingForm  meetingForm=(OAMeetingForm)form;		
		 Result result =  meetingMgt.loadMeeting(meetingForm.getMeetingId());
         if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	 OAMeetingBean  bean=(OAMeetingBean)result.getRetVal();
        	 request.setAttribute("meeting", bean);
        	 request.setAttribute("turnOut", "true");
         }
       //获取所有职员信息,用于textBox控件
 		ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
 		request.setAttribute("textBoxValues",gson.toJson(list));
 		
			return getForward(request, mapping, "to_addMeeting");
		
		
	}
	
	/**
	 *删除会议
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward deleteMeeting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String isOk="no";
		OAMeetingForm  meetingForm=(OAMeetingForm)form;	
		isOk=deleteMeeting(meetingForm.getMeetingId(),request);
		if(!isOk.equals("yes")){
		    isOk="no";
		}
        request.setAttribute("msg", isOk);
		return getForward(request, mapping, "blank");		
	}
	
		public String deleteMeeting(String meetingId,HttpServletRequest request){
			request.setAttribute("deleteMeeting", "true");
			OAMeetingBean  bean=null;
			String isOk="no";
			Result  result =  meetingMgt.loadMeeting(meetingId);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {				 
		        	 bean=(OAMeetingBean)result.getRetVal();
			}
			result =  meetingMgt.deleteMeeting(meetingId);
	         if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	        	 result =noteMgt.deleteNotes(meetingId);
	        		 isOk="yes";
	         }else{
	        	 isOk=meetingId;
	         }
	         if(isOk=="yes"&&bean!=null){
	        	 //删除日程
	        	 omgt.delByRelationId(meetingId);
	        	 
	        	 String wakeUpMode= bean.getWakeUpMode();
	       	  	 String[] wakeType=wakeUpMode.split(",");
	           	 String  toastmasters= bean.getToastmaster();
	        	 String participants=bean.getParticipant();
	    		 String adviceString=utilString(toastmasters+participants+bean.getSponsor()+";",getLoginBean(request).getId());
	    		 new AdviceMgt().deleteByRelationId(meetingId, "");
	    		 advice(bean,adviceString,wakeType,request,"会议通知:"+this.getLoginBean(request).getEmpFullName()+"删除了"+bean.getTitle()+",请须知");
			     
	         }
	         return isOk;
		}
	/**
	 *修改会议
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updateMeeting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String backUrl=request.getParameter("backUrl");
		if(backUrl==null || backUrl.equals("")){
		     backUrl="/OASearchMeeting.do?operation=4&addTHhead="+getParameter("addTHhead", request);
		}else{
			backUrl=backUrl.replace("@", "&");
		}
		OAMeetingForm  meetingForm=(OAMeetingForm)form;		
		String userId = this.getLoginBean(request).getId();
		 Result result =  meetingMgt.loadMeeting(meetingForm.getMeetingId());
         if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	OAMeetingBean  bean=(OAMeetingBean)result.getRetVal();
        	read(form, bean);
        	SimpleDateFormat dateformt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     		Date  startTime=dateformt.parse(meetingForm.getMeetingStartTime()+":00");
     		Date  endTime=dateformt.parse(meetingForm.getMeetingEndTime()+":00");
     		bean.setStartTime(startTime);
     		bean.setEndTime(endTime);
     		
     		bean.setLastUpdateTime(dateformt.format(new Date()));
     		//获取通知方式
    		String[] wakeType = request.getParameterValues("wakeUpMode");
    		// 通知方式
    		String wakeUpMode = "";
    		if (wakeType != null && wakeType.length > 0) {
    			for (String str : wakeType) {
    				wakeUpMode += str + ",";
    			}
    		}
    		bean.setWakeUpMode(wakeUpMode);
    		
    		 //处理附件
    		String file=this.getParameter("attachFiles", request)==null?"":this.getParameter("attachFiles", request);
    		bean.setFilePath(file);
    		// 需删除的附件
    		String delFiles = getParameter("delFiles",request);
    		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
    		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
    		for (String del : dels) {
    			if (del != null && del.length() > 0
    					&& bean.getFilePath().indexOf(del) == -1) {
    				File aFile = new File(BaseEnv.FILESERVERPATH + "/affix/OAMeeting/" + del);
    				aFile.delete();
    			}
    		}
    		
    		String cancel = this.getParameter("isCancel", request);
    		if(cancel!=null){
    			if(cancel.equals("0")){
    				bean.setStatus(null);
    			}
    		}
    		
    		 //修改后的通知问题
    		AlertBean alert=null;
    		if(bean.getStatus()==null){
    		      String sponsor=bean.getSponsor();
    		      String  toastmasters= meetingForm.getToastmaster();
    		      String participants=meetingForm.getParticipant();
    		      String adviceString=utilString(toastmasters+participants+sponsor+";",userId);
    		      String advicestr="会议通知:"+ this.getLoginBean(request).getEmpFullName()+"修改了"+meetingForm.getTitle()+"会议,请查看";
    		      advice(bean,adviceString,wakeType,request,advicestr);
    		      alert=alert(bean,userId,adviceString+userId+",",wakeUpMode);
    		}
		   
        	 Result rs = meetingMgt.update(bean,alert);
        	 if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		 
        		 omgt.updateByRelationId(bean.getTitle(),"我的会议",BaseDateFormat.format(bean.getStartTime(), BaseDateFormat.yyyyMMddHHmmss),
 						BaseDateFormat.format(bean.getEndTime(), BaseDateFormat.yyyyMMddHHmmss),bean.getId(),bean.getParticipant(),null);
        		//添加成功
     			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
     					.setBackUrl(backUrl).setAlertRequest(request);
        	 } 			
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
					.setBackUrl(backUrl).setAlertRequest(request);
		}		
		return getForward(request, mapping, "message");
	}
	
	
	/**
	 * 新增会议
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String backUrl=request.getParameter("backUrl");
		if(backUrl==null || backUrl.equals("")){
		     backUrl="/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek&addTHhead="+getParameter("addTHhead", request);
		}else{
			backUrl=backUrl.replace("@", "&");
		}
		OAMeetingForm  meetingForm=(OAMeetingForm)form;
		if("THnew".equals(getParameter("operationType", request))){
			meetingForm.setBoardroomId(getParameter("boardroomId", request));
			meetingForm.setMeetingContent(getParameter("meetContext", request));
			meetingForm.setMeetingStartTime(getParameter("starttime", request));
			meetingForm.setMeetingEndTime(getParameter("endtime", request));
			meetingForm.setToastmaster(getParameter("toastmaster", request));
			meetingForm.setToastmasterName(getParameter("toastmastername", request));
			meetingForm.setParticipant(getParameter("participant", request));
			meetingForm.setParticipantName(getParameter("participantname", request));
			meetingForm.setSigninTime(5);
			meetingForm.setWarnTime(5);
			meetingForm.setTitle(getParameter("meetTile", request));
		}
		/* 获得登陆者ID */
		String userId = this.getLoginBean(request).getId();
		String loginname =this.getLoginBean(request).getEmpFullName();
		OAMeetingBean meetingBean=new OAMeetingBean();
		read(meetingForm, meetingBean);
		meetingBean.setId(IDGenerater.getId());
		SimpleDateFormat dateformt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date  startTime=dateformt.parse(meetingForm.getMeetingStartTime()+":00");
		Date  endTime=dateformt.parse(meetingForm.getMeetingEndTime()+":00");
		
			meetingBean.setStartTime(startTime);
	        meetingBean.setEndTime(endTime);
		
        meetingBean.setCreateTime(dateformt.format(new Date()));
        meetingBean.setLastUpdateTime(dateformt.format(new Date()));
       
        //处理附件
		String file=this.getParameter("attachFiles", request)==null?"":this.getParameter("attachFiles", request);
		meetingBean.setFilePath(file);
     // 需删除的附件
		String delFiles = getParameter("delFiles",request);
		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0
					&& meetingBean.getFilePath().indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/affix/OAMeeting/" + del);
				aFile.delete();
			}
		}
        
        
        
      //获取通知方式
		String[] wakeType = request.getParameterValues("wakeUpMode");
		// 通知方式
		String wakeUpMode = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeUpMode += str + ",";
			}
		}
		if("THnew".equals(getParameter("operationType", request))){
			wakeUpMode="4,";
		}
		meetingBean.setWakeUpMode(wakeUpMode);
		
		//插入发起人
		meetingBean.setSponsor(userId);
		meetingBean.setTaker(userId);
		//通知阶段
		String  toastmasters= meetingForm.getToastmaster();
	    String participants=meetingForm.getParticipant();
		String adviceString=utilString(toastmasters+participants,userId);
		AlertBean alertBean=alert(meetingBean,userId,adviceString+userId,wakeUpMode);
		
		Result result =meetingMgt.addMeeting(meetingBean,alertBean);
		
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {	
			request.setAttribute("msg", "3");
			if(meetingBean.getRegularMeeting()!=0){
				OASigninBean siginBean=new OASigninBean();
				meetingMgt.setSignin(siginBean, meetingBean.getId(), meetingForm.getMeetingStartTime().split(" ")[0]);
			}
			//添加成功			
			String advicestr="会议通知:"+meetingForm.getTitle()+"请查看";
			advice(meetingBean,adviceString,wakeType,request,advicestr);
			if(!"THnew".equals(getParameter("operationType", request))){
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess"))
						.setBackUrl(backUrl).setAlertRequest(request);
			}
			//导入日程
			omgt.outAddCalendar(meetingBean.getSponsor(),meetingBean.getTitle(),"我的会议",BaseDateFormat.format(meetingBean.getStartTime(),BaseDateFormat.yyyyMMddHHmmss),
					BaseDateFormat.format(meetingBean.getEndTime(),BaseDateFormat.yyyyMMddHHmmss),meetingBean.getId(),meetingBean.getParticipant()+";"+meetingBean.getToastmaster(),null,null,"");
		}else{
			//添加失败
			request.setAttribute("msg", "error");
			if(!"THnew".equals(getParameter("operationType", request))){
				EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
						.setBackUrl(backUrl).setAlertRequest(request);
			}
		}			
		if("THnew".equals(getParameter("operationType", request))){
			return getForward(request, mapping, "blank");
		}					
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 去除重复的人员
	 * */
	public String utilString(String users,String self){
		String[] user=users.split(";");
		String adviceString=",";
		for(String i : user){
			if(!i.trim().equals("") && !i.trim().equals(self) && adviceString.indexOf(","+i.trim()+",")==-1){
				adviceString += i+",";
			}
		}
		return	adviceString.substring(1, adviceString.length());
		
	}
	
	/**
	 * 通知相关人员
	 * 
	 * @return
	 * @throws Exception
	 */
	public void advice(OAMeetingBean meeting,String popedomUserIds,String[] wakeType,HttpServletRequest request,String title){
		BaseEnv.log.error("用户ID:"+popedomUserIds);
        String url = "/Meeting.do";
        String favoriteURL = url + "?noback=true&operation=4&advicEnter=true&requestType=NOTE&meetingId=" + meeting.getId() + "&isEspecial=1";
       
        String content = "<a href=javascript:mdiwin('" + favoriteURL + "','"
					  + GlobalsTool.getMessage(request, "oa.common.adviceList")
					  + "')>" + title 
					  + "</a>"; // 内容
        if(request.getAttribute("deleteMeeting")!=null){
        	content="<a href='javascript:void(0)' >"+title+"</a>";
        }
		
		
        /*查询某部门下 所有职员*/
        String alertIds = "";
		if(popedomUserIds!=null && popedomUserIds.length()>0){
			String[] arrayDept = popedomUserIds.split(",") ;
			for(String str : arrayDept){
				ArrayList<OnlineUser> listUser = OnlineUserInfo.getDeptUser(str) ;
				for(OnlineUser onUser : listUser){
					if(popedomUserIds!=null && !popedomUserIds.contains(","+onUser.getId()+",")){
						alertIds += onUser.getId()+"," ;
					}
				}
			}
		}
		
		//向用户添加提醒方式
		if (wakeType != null && wakeType.length > 0) {
			for (String type : wakeType) {
				new Thread(new NotifyFashion(meeting.getSponsor(), title, content,
						alertIds, Integer.parseInt(type), "yes",
						meeting.getId(),"","","advice")).start();
			}
		}
	}
	
	/**
	 * 通知单人
	 */
	protected ActionForward adviceUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		String msg="no";
		OAMeetingForm  meetingForm=(OAMeetingForm)form;		
		String meetingId=meetingForm.getMeetingId();
		Result result =meetingMgt.loadMeeting(meetingId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {		
			 OAMeetingBean  bean=(OAMeetingBean)result.getRetVal();
			 String userId=request.getParameter("userId");
			 String wakeType=bean.getWakeUpMode();
			 String[] wakeTypes=wakeType.split(",");
			 advice(bean,userId,wakeTypes,request,"会议通知:"+bean.getTitle());
			 msg="yes";
		}
		
		 request.setAttribute("msg", msg);
			return getForward(request, mapping, "blank");
	}   
	
	/**
	 * 查询会议
	 * 
	 * 
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OAMeetingSearchForm searchForm=(OAMeetingSearchForm)form;
		searchForm.setMeetId(getParameter("id", request));
		request.setAttribute("oldForm", searchForm);	
		String isMeetTh = request.getParameter("isMeetTh");//天华主页
		if ("menu".equals(getParameter("src", request))) {
			request.getSession().setAttribute("meetingSearchForm", null);
		}
		/* 获得登陆者ID */
		String userId = this.getLoginBean(request).getId();
         Result result =  meetingMgt.queryMeeting(searchForm,userId);
         if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	 List<Object[]> beans=(List<Object[]>)result.getRetVal();
        	 List<OAMeetingBean> meetings=new ArrayList();
        	 for(Object[] bean : beans){
        		 OAMeetingBean meeting=new OAMeetingBean();
        		 meeting.setId((String)bean[0]);
        		 meeting.setTitle((String)bean[1]);
        		 meeting.setMeetingContent((String)bean[2]);
        		 meeting.setBoardroomId((String)bean[6]);
        		 meeting.setToastmaster((String)bean[16]);
        		 meeting.setToastmasterName((String)bean[14]);
        		 meeting.setParticipant((String)bean[17]);
        		 meeting.setSponsor((String)bean[13]);
        		 meeting.setParticipantName((String)bean[15]);
        		 meeting.setStatus((String)bean[5]);
        		 meeting.setCreateTime((String)bean[9]);
        	
        		 meeting.setStartTime(new Date(((java.sql.Timestamp)bean[7]).getTime()));
        		 meeting.setEndTime(new Date(((java.sql.Timestamp)bean[8]).getTime()));
        		 meeting.setSigninTime((Integer)bean[4]);
        		 meeting.setSignin((String)bean[19]);       		
        		 meetings.add(meeting);
        	 }
        	
        	 request.setAttribute("list",meetings);
        	 request.setAttribute("pageBar", pageBar(result, request));
        	 SimpleDateFormat dateformt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        	 request.setAttribute("formt", dateformt);
        	 SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyy-MM-dd");
        	 request.setAttribute("yyyymmdd", yyyymmdd);
         }
         
         request.setAttribute("today", new Date());
         request.setAttribute("self", userId);
         
         if("true".equals(isMeetTh)){     	
        	 return getForward(request, mapping, "oameeting");
         }
     
         return getForward(request, mapping, "to_queryMeeting");
	}
	
	
	
	/**
	 * 取消会议
	 * 
	 * 
	 */
	protected ActionForward cancel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		OAMeetingForm  meetingForm=(OAMeetingForm)form;	
		/* 获得登陆者ID */
		String userId = this.getLoginBean(request).getId();
		
         Result result =  meetingMgt.loadMeeting(meetingForm.getMeetingId());
         OAMeetingBean  bean=null;
         if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	 bean=(OAMeetingBean)result.getRetVal();
        	  bean.setStatus(meetingForm.getWhy());
     		   AlertBean alert=null;
     		  result = meetingMgt.update(bean,alert);      	     	
         }
         
         if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	 //日程删除
        	 omgt.delByRelationId(meetingForm.getMeetingId());
 			//添加成功
        	 //取消 后的通知问题
        	 String wakeUpMode= bean.getWakeUpMode();
        	  String[] wakeType=wakeUpMode.split(",");
            	 String  toastmasters= bean.getToastmaster();
         	    String participants=bean.getParticipant();
         	   String sponsor= bean.getSponsor();
     			String adviceString=utilString(toastmasters+participants+sponsor+";",userId);
        	 String advicestr="会议通知:"+ this.getLoginBean(request).getEmpFullName()+"取消了"+bean.getTitle()+"会议,请须知";
  			advice(bean,adviceString,wakeType,request,advicestr);
        	request.setAttribute("dealAsyn", "true");
        	request.setAttribute("noAlert", "true");
 			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess"))
 					.setBackUrl("/OASearchMeeting.do?operation=4").setAlertRequest(request);
 		}else{
 			//添加失败
 			EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
 					.setBackUrl("/OASearchMeeting.do?operation=4").setAlertRequest(request);
 		}		
		return getForward(request, mapping, "alert");
	}
	
	
	/**
	 * 打开笔记本 
	 * 
	 * @param mapping
	 * @param form   meetingId   BoardroomName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward openNote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("backUrl", request.getParameter("backUrl"));	
		request.setAttribute("advicEnter", request.getParameter("advicEnter"));
		OAMeetingForm  meetingForm=(OAMeetingForm)form;	
		String userId = this.getLoginBean(request).getId();
		request.setAttribute("loginer", userId);
		String loginname =this.getLoginBean(request).getEmpFullName();
		String andId=meetingForm.getMeetingId()+userId;
		 Result	result= meetingMgt.loadMeeting(meetingForm.getMeetingId());
		
        if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	OAMeetingBean bean=(OAMeetingBean)result.getRetVal();
   	    	
   	    	//人员统计
   	    	int signin=0;
   	    	int sum=0;
   	    	int absent=0;
   	    	int late=0;
   	    	int signinName=0;
   	     //人员卡片
   	    	String userstr=utilString(bean.getToastmaster()+bean.getParticipant(),"");
   	    	//会议发起人默认参与的
   	    	userstr += bean.getSponsor();
   	     Map<String,String> userStatus=new HashMap();
   	    	String[] users=userstr.split(",");
   	    	String signinuserstr=bean.getSignin();
   	    	if(bean.getRegularMeeting()!=0){
   	    		String time=this.getParameter("shijian",request);
   	    		if(time==null||time.equals("")){
   	    			time=BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
   	    		}
   	    		List<Date> meetingTime=meetingMgt.getMeetingTime(time, bean.getId(), bean.getRegularMeeting());
   	    		bean.setStartTime(meetingTime.get(0));
   	    		bean.setEndTime(meetingTime.get(1));
   	    		result=meetingMgt.getSignin(bean.getId(),BaseDateFormat.format(meetingTime.get(0), BaseDateFormat.yyyyMMdd));
   	    		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
   	    			OASigninBean signinBean=(OASigninBean)result.getRetVal();
   	    				signinuserstr=signinBean.getSignin();
   	    				
   	    				//把会议笔录放进去
   	    				bean.setMeetingNote(signinBean.getMeetingNote());
   	    		}else{
   	    			signinuserstr=null;
   	    			OASigninBean addSigninBean=new OASigninBean();
   	    			meetingMgt.setSignin(addSigninBean, bean.getId(), BaseDateFormat.format(meetingTime.get(0), BaseDateFormat.yyyyMMdd));
   	    			
   	    		}
   	    		bean.setSignin(signinuserstr);
   	    	}
   	    	
   	    	String[] signinusers=null;
   	    	for(String user :users){
	    		userStatus.put(user, "");	    		
	   	    }
   	         sum=userStatus.size();	
   	    	if(signinuserstr != null && !signinuserstr.equals("")){
   	    		signinusers=signinuserstr.split(";");
   	    		signinName=signinusers.length;
   	    		for(String i : signinusers){
   	    			String[] temp=i.split("=");
   	    			if(temp[1].equals("late")){
   	    				late++;
   	    			}else if(temp[1].equals("absent")){
   	    				absent++;
   	    				userStatus.put(temp[0]+"WHY","原因："+temp[4]);  
   	    			}
   	    			userStatus.put(temp[0], temp[1]);
   	    			userStatus.put(temp[0]+"detail", "签到人:"+GlobalsTool.getEmpFullNameByUserId(temp[2])+"<br />签到时间:"+temp[3]);  	    		  
   	    		}
   	    	}
   	    	request.setAttribute("meeting", bean);	
   	    request.setAttribute("userMap",userStatus);	   
   	 	request.setAttribute("meetingId",meetingForm.getMeetingId() );    	
       	request.setAttribute("boardroomName", meetingForm.getBoardroomName());
       	
       	request.setAttribute("sum",sum);
       	request.setAttribute("unsignin",sum-signinName);
       	request.setAttribute("absent",absent);       
       	request.setAttribute("signin",signinName-absent);
       	request.setAttribute("today", new Date());
       	if(bean.getStartTime().after(new Date())){
       		late=-1;
       	}
    	request.setAttribute("late",late);
    	 SimpleDateFormat dateformt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	 request.setAttribute("formt", dateformt);
    	 SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyy-MM-dd");
    	 request.setAttribute("yyyymmdd", yyyymmdd);
       	
   	    	Result noteResult = noteMgt.loadNote(andId) ;
       	
       	    if (noteResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
       	     OANoteBean  noteBean=( OANoteBean)noteResult.getRetVal();
       	 	request.setAttribute("note", noteBean);          
       	     }       
       	 return getForward(request, mapping, "to_note");
       	}else{
       		EchoMessage.error().add("此会议已删除")
			.setBackUrl("/OASearchMeeting.do?operation=4").setAlertRequest(request);
       		return getForward(request, mapping, "message");
       	}
		
		
		
	}
	
	
	
	
	
	
	/**
	 * 占用的会议室
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward boardroomOccupation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		int flag = 0;
		OAMeetingForm meetingForm = (OAMeetingForm) form;
		String start = meetingForm.getMeetingStartTime();
		String end = meetingForm.getMeetingEndTime();
		if (start != null && end != null) {
			try {
				SimpleDateFormat dateformt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startTime = dateformt.parse(start + ":00");
				Date endTime = dateformt.parse(end + ":00");
				int isCount = meetingMgt.isOccupation(startTime, endTime,meetingForm.getBoardroomId());
				/*if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					for(Object[] b : (List<Object[]>) result.getRetVal()){
						str += (String)b[0] + ";";
					}
					
				}*/
				flag = isCount;
			} catch (java.text.ParseException ex) {
				ex.printStackTrace();
				flag = 5;//保错
			}

		}
		request.setAttribute("msg", flag);
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 会议室周程表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward boardroomWeek(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OAMeetingForm  meetingForm=(OAMeetingForm)form;				
		request.setAttribute("boardroomIndex", request.getParameter("boardroomIndex"));
		request.setAttribute("conEnter", request.getParameter("conEnter"));
		System.out.println(request.getParameter("conEnter"));
		request.setAttribute("type", request.getParameter("Select"));
		 List<OAMeetingBean> meetings=null;
		Map<String,OABoardroomBean> map=bmgt.getBoardroomMap();
		int week=0;
		String boardroomId="";
		String weekstr= request.getParameter("week");
		request.setAttribute("weekstr", weekstr);
		if(weekstr != null){
			week=Integer.parseInt(weekstr);
		}
		if(meetingForm.getBoardroomId() == null){
			for(String b : map.keySet()){
				boardroomId=b;
				break;
			}
			
		}else{
			boardroomId=meetingForm.getBoardroomId() ;
		}
		Date  startTime=null;
		SimpleDateFormat dateformt=new SimpleDateFormat("yyyy-MM-dd");
		if(meetingForm.getMeetingStartTime()!=null && !"".equals(meetingForm.getMeetingStartTime())){
		startTime=dateformt.parse(meetingForm.getMeetingStartTime());
		}else{
			startTime=new Date();
		}
		Calendar c=Calendar.getInstance();
		c.setTime(startTime);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DAY_OF_YEAR, week*7);
		List<Date> days=dateToWeek(c.getTime());
		
		SimpleDateFormat formt=new SimpleDateFormat("MM/dd");
		SimpleDateFormat pdf=new SimpleDateFormat("yyyy-MM-dd");
		request.setAttribute("formt", formt);
		request.setAttribute("pdf", pdf);
		request.setAttribute("days", days);
		request.setAttribute("today", dateformt.format(days.get(0)));		
		Result result=null;
		String user=request.getParameter("myMeetingWeek");
		if(user!=null && user.equals("myWeek")){
			String userId = this.getLoginBean(request).getId();
			result=meetingMgt.queryweek(null,days,userId,GlobalsTool.getDeptCodeByUserId(userId));
			request.setAttribute("myWeek", "myWeek");
		}else{
		result=meetingMgt.queryweek(boardroomId,days,null,GlobalsTool.getDeptCodeByUserId(getLoginBean(request).getId()));
		request.setAttribute("boardroomId", boardroomId);
		}
		 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			 meetings=new ArrayList();
			 int i =0;
			  for(Object[] bean :(List<Object[]>)result.getRetVal()){
				  
				 
					  OAMeetingBean meeting=new OAMeetingBean();
		        		 meeting.setId((String)bean[0]);
		        		 meeting.setTitle((String)bean[1]);
		        		 meeting.setMeetingContent((String)bean[2]);
		        		 meeting.setBoardroomId((String)bean[3]);
		        		 meeting.setToastmaster((String)bean[4]);
		        		 meeting.setToastmasterName((String)bean[5]);
		        		 meeting.setParticipant((String)bean[6]);
		        		 meeting.setSponsor((String)bean[7]);		        			    
		        		 meeting.setStatus((String)bean[8]);
		        		 meeting.setSignin((String)bean[9]);	        	
		        		 meeting.setStartTime(new Date(((java.sql.Timestamp)bean[10]).getTime()));
		        		 meeting.setEndTime(new Date(((java.sql.Timestamp)bean[11]).getTime()));
				       meetings.add(meeting);
				  
			  }
			  request.setAttribute("meetings", meetings);
		 }
		String todaystr=formt.format(new Date());
	for(Date day : days){
		if(formt.format(day).equals(todaystr)){
			String istoday=""+day.getDate();
			request.setAttribute("istoday", istoday);
		}
	}
	//	 SimpleDateFormat yyyyMMdd=new SimpleDateFormat("yyyy-MM-dd HH:mm");//January 12,2006 22:19:35
		 //MMM dd,yyyy HH:mm   这种写法是解决IE ， 谷歌  浏览器的js内核不同的问题
		 SimpleDateFormat yyyyMMdd=new SimpleDateFormat("MMM dd,yyyy HH:mm",Locale.US);
		 request.setAttribute("timeformat", yyyyMMdd);
			return getForward(request, mapping, "to_boardroomweek");
		
	}
	
	/**
	 * 算出这一周的日历
	 * @param mdate
	 * @return
	 */
	public  List<Date> dateToWeek(Date mdate) {  
	     int b = mdate.getDay()+1;  
	     Date fdate;  
	     List<Date> list = new ArrayList<Date>();  
	     Long fTime = mdate.getTime() - b * 24 * 3600000;  
	     for (int a = 1; a <= 8; a++) {  
	         fdate = new Date();  
	         fdate.setTime(fTime + (a * 24 * 3600000));  
	         list.add(a-1, fdate);  
	     }  
	     return list;  
	 }  
	
	/**
	 * 所有会议室填充到 request    boardrooms
	 * */
	public void allBoardroom(HttpServletRequest request){
		Map<String,OABoardroomBean> map=bmgt.getBoardroomMap();
			request.setAttribute("boardroomMap", map);
	}
	
	
	
	
	
	/**
	 * 新增笔记  或者  修改笔记
	 * 
	 * @param mapping
	 * @param form  meetingId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addOrUpdateNote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OAMeetingForm  meetingForm=(OAMeetingForm)form;	
		String userId = this.getLoginBean(request).getId();
		String meetingId=meetingForm.getMeetingId();
		 Result result = noteMgt.loadNote(meetingId+userId);
      if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {   	 	   
      	 	         OANoteBean note=(OANoteBean)result.getRetVal();
   	    	         read(meetingForm,note);
   	    	          noteMgt.updateNote(note);                    	  
      }else{
      		OANoteBean note=new OANoteBean();
   	    	read(meetingForm,note);
   	    	note.setNoteId(meetingId+userId);
   	    	noteMgt.addNote(note);
      }
      if(this.getParameter("noter", request)!=null&&this.getParameter("noter", request).equals("true")){
      //保存会议笔录
  	  if(meetingForm.getMeetingNote() != null && !meetingForm.getMeetingNote().trim().equals("")){
  	    result=meetingMgt.loadMeeting(meetingId);
  	      if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
  		  OAMeetingBean  meetingBean=(OAMeetingBean)result.getRetVal();
  		  if(meetingBean.getMeetingNote()==null||!meetingForm.getMeetingNote().trim().equals(meetingBean.getMeetingNote().trim())){
  			if(meetingBean.getTaker()!=null && meetingBean.getTaker().equals(userId)){
  				if(meetingBean.getRegularMeeting()==0){
  					meetingBean.setMeetingNote(meetingForm.getMeetingNote());
  					meetingMgt.update(meetingBean);
  				}else{
  					String shijian=this.getParameter("meetingTime",request);
  					if(shijian !=null){
  					result=meetingMgt.getSignin(meetingBean.getId(),shijian);
  	   	    			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
  	   	    				OASigninBean signinBean=(OASigninBean)result.getRetVal();
  	   	    				signinBean.setMeetingNote(meetingForm.getMeetingNote());
  	   	    			    meetingMgt.setSignin(signinBean, meetingId, shijian);
  	   	    			}
  					}
  				}
  			}
  		  }
  	      }
  	  }
      }
      
		EchoMessage.success().add("操作成功")
				.setBackUrl("/OASearchMeeting.do?operation=4").setAlertRequest(request);
	
	return getForward(request, mapping, "message");
	}
	
	/**
	 * 签到
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward signin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String isOk="no";
		boolean isNote=false;
		OAMeetingForm  meetingForm=(OAMeetingForm)form;	
		String userId=request.getParameter("userId");
		if(userId == null){
		userId = this.getLoginBean(request).getId();
		isNote=true;
		}
		String signiner="="+this.getLoginBean(request).getId()+"="+BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+";";
		 Result result =  meetingMgt.loadMeeting(meetingForm.getMeetingId());
         if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	 OAMeetingBean  bean=(OAMeetingBean)result.getRetVal();
           if(bean.getStatus()==null){
        	   String beanSignin=bean.getSignin();
        	   OASigninBean signinBean=new OASigninBean();
        	   if(bean.getRegularMeeting()!=0){
        		 List<Date> meetingTime=meetingMgt.getMeetingTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd), bean.getId(), bean.getRegularMeeting());     	    		
        		 Result  signinResult=meetingMgt.getSignin(bean.getId(),BaseDateFormat.format(meetingTime.get(0), BaseDateFormat.yyyyMMdd));        		
        		 if (signinResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        			   signinBean=(OASigninBean)signinResult.getRetVal();
        		   }
        		   beanSignin=signinBean.getSignin();
        	   }
        	 String temp=beanSignin==null?"":beanSignin;
        	 String signin="";
        	 
        	if(beanSignin == null || (beanSignin != null && ((";"+beanSignin).indexOf(";"+userId+"=") == -1) )){
        	 if(request.getParameter("absent") != null){
        		 signin=userId+"="+"absent"+signiner.substring(0,signiner.length()-1)+"="+GlobalsTool.toChinseChar(this.getParameter("absentText", request))+";";
        	 }else{
        		 Date today=new Date ();
        	 if(bean.getStartTime().after(today)){// 签到
        	 signin=userId+"="+"signin"+signiner;
        	 }else if(bean.getStartTime().before(today)){//迟到
        		 signin=userId+"="+"late"+signiner;
        	 }
        	 }
        	 if(bean.getRegularMeeting()==0){
        	 bean.setSignin(signin+temp);
        	 meetingMgt.update(bean);
        	 }else{
        		 signinBean.setSignin(signin+temp);
        		 meetingMgt.setSignin(signinBean,bean.getId(), BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
        		 bean.setSignin(signin+temp);
            	 meetingMgt.update(bean);
        	 }
        	 if(!isNote){
        	 isOk="签到人:"+GlobalsTool.getEmpFullNameByUserId(this.getLoginBean(request).getId())+"<br />签到时间:"+BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
        	 }else{
        		 isOk="yes";
        	 }
        	}else{
        		String status=";"+beanSignin;	
        		status=status.substring(status.indexOf(";"+userId+"="), status.indexOf(";",status.indexOf(";"+userId+"=")+1));
                String[] shu=status.split("=");
                if(shu!=null && shu.length==2){
                	isOk=shu[1];
                }
        		
        	}
           }else{
        	   isOk="cancel";
           }
         }		
	    request.setAttribute("msg", isOk);
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * 复位签到
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward resetSignin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String isOk="no";
		OAMeetingForm  meetingForm=(OAMeetingForm)form;	
		String userId=request.getParameter("userId");
		if(userId == null){
		userId = this.getLoginBean(request).getId();
		}
		String signin="";
		 Result result =  meetingMgt.loadMeeting(meetingForm.getMeetingId());
         if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	 OAMeetingBean  bean=(OAMeetingBean)result.getRetVal();
           if(bean.getStatus()==null){
        	  
        	   String beanSignin=bean.getSignin();
        	   OASigninBean signinBean=new OASigninBean();
        	   if(bean.getRegularMeeting()!=0){
        		 List<Date> meetingTime=meetingMgt.getMeetingTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd), bean.getId(), bean.getRegularMeeting());     	    		
        		 Result  signinResult=meetingMgt.getSignin(bean.getId(),BaseDateFormat.format(meetingTime.get(0), BaseDateFormat.yyyyMMdd));        		
        		 if (signinResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        			   signinBean=(OASigninBean)signinResult.getRetVal();
        		   }
        		   beanSignin=signinBean.getSignin();
        	   }
        	   
        	   signin=";"+beanSignin;       	   
        	  int index= signin.indexOf(";"+userId+"=");
        	  if(index!=-1){
        	    int strEnd= signin.indexOf(";", index+1);
        	   String userSignin= signin.substring(index+1, strEnd);
        	    signin= beanSignin.replaceAll(userSignin+";", "");
        	    if(bean.getRegularMeeting()==0){
        	    	 bean.setSignin(signin);
             	    meetingMgt.update(bean);
               	 }else{
               		 signinBean.setSignin(signin);
               		 meetingMgt.setSignin(signinBean,bean.getId(), BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
               		bean.setSignin(signin);
             	    meetingMgt.update(bean);
               	 }
        	    isOk="yes";
        	  }
           }else{
        	   isOk="cancel";
           }
         }		
	    request.setAttribute("msg", isOk);
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * 原因准备
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward whyBox(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id=this.getParameter("id", request);
		String whyType=this.getParameter("whyType", request);
	    request.setAttribute("id", id);
	    request.setAttribute("whyType", whyType);
		return getForward(request, mapping, "to_whyBox");
	}
	
	/**
	 * 提醒功能
	 * **/
	public AlertBean alert(OAMeetingBean meeting,String userId,String popedomUserIds,String wakeUpMode){
		SimpleDateFormat dateformt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date  startTime=meeting.getStartTime();
		Calendar c=Calendar.getInstance();
		c.setTime(startTime);
		c.add(Calendar.MINUTE, (0-meeting.getWarnTime()));
		Date warnTime=c.getTime();
		String alertDate=BaseDateFormat.format(warnTime, BaseDateFormat.yyyyMMddHHmmss);
		AlertBean alertBean = new AlertBean();
		alertBean.setId(IDGenerater.getId());
		alertBean.setAlertDate(BaseDateFormat.format(warnTime, BaseDateFormat.yyyyMMdd));
		alertBean.setAlertHour(c.get(Calendar.HOUR_OF_DAY));
		alertBean.setAlertMinute(c.get(Calendar.MINUTE));	
		alertBean.setAlertContent(meeting.getTitle());
		String url = "/Meeting.do";
        String favoriteURL = url + "?noback=true&operation=4&requestType=NOTE&meetingId="
		+ meeting.getId() + "&isEspecial=1";
        String context="<a href=\"javascript:mdiwin('"+favoriteURL+"','会议提醒')\">会议提醒："+meeting.getTitle()+"还有"+meeting.getWarnTime()+"分钟就开始了</a>";
		alertBean.setAlertUrl(context);
		if(meeting.getRegularMeeting()!=0){
			alertBean.setIsLoop("yes");	
			if(meeting.getRegularMeeting()==3){
				alertBean.setLoopType("month");
				alertBean.setLoopTime(1);
			}else if(meeting.getRegularMeeting()==2){
				alertBean.setLoopType("day");
				alertBean.setLoopTime(7);
			}else{
				alertBean.setLoopType("day");
				alertBean.setLoopTime(meeting.getRegularDay());
			}
		}else{
			alertBean.setIsLoop("no");	
		}
		
		alertBean.setAlertType(wakeUpMode);
		alertBean.setCreateBy(userId);
		alertBean.setCreateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		alertBean.setRelationId(meeting.getId());
		alertBean.setNextAlertTime(alertDate);
		alertBean.setStatusId(0);
		alertBean.setPopedomUserIds(popedomUserIds);
		return alertBean;
		
		
	}
	
	
	/**
	 *指定笔录员
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward taker(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String isOk="no";
		Result result =  meetingMgt.loadMeeting(this.getParameter("meetingId", request));
        if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	OAMeetingBean meetingBean=(OAMeetingBean)result.getRetVal();
        	meetingBean.setTaker(this.getParameter("taker", request));
        	result =meetingMgt.update(meetingBean);
        	 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        		 isOk="yes";
        	 }
        }	
            request.setAttribute("msg", isOk);
			return getForward(request, mapping, "blank");
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
	
}
