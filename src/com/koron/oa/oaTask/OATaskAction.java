package com.koron.oa.oaTask;



import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.OACalendar.OACalendaMgt;
import com.koron.oa.bean.OAItemsBean;
import com.koron.oa.bean.OATaskBean;
import com.koron.oa.discuss.DiscussAction;
import com.koron.oa.oaItems.OAItemsMgt;
import com.koron.oa.util.AttentionMgt;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>Title:我的任务</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class OATaskAction extends BaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	OATaskMgt mgt = new OATaskMgt();
	OAItemsMgt itemMgt = new OAItemsMgt();
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
			case OperationConst.OP_ADD:	
				if("addWarn".equals(type)){
					forward = addWran(mapping, form, request, response);
				}else{
					forward = addTask(mapping, form, request, response);//添加
				}
				break;
			case OperationConst.OP_ADD_PREPARE:					
					forward = addPrepare(mapping, form, request, response);//添加前			
				break;
			case OperationConst.OP_UPDATE_PREPARE:
				forward = updatePrepare(mapping, form, request, response);//更新前
				break;
			case OperationConst.OP_UPDATE:
					if("changeStatus".equals(type)){
						forward = changeStatus(mapping, form, request, response);//改变任务状态
					}else if("participantsInfo".equals(type)){
						forward = updateParticipantsInfoInfo(mapping, form, request, response);//更新参与人
					}else if("updateSingle".equals(type)){
						forward = updateSingleField(mapping, form, request, response);//单个字段更新
					}else if ("batchTransfer".equals(type)) {
						forward = updateBatchTransfer(mapping, form, request, response);// 批量转交负责人
					}else{
						forward = update(mapping, form, request, response);//更新任务
					}
				break;
			case OperationConst.OP_DETAIL:	
				if("detailByItem".equals(type)){
					forward = detailByItem(mapping, form, request, response);//项目详情-任务查询
				}else{
					forward = detail(mapping, form, request, response);//任务详情
				}
				break;
			case OperationConst.OP_DELETE:	
				if("affix".equals(type)){
					forward = delAffix(mapping, form, request, response);//删除附件
				}else if("participant".equals(type)){
					forward = delParticipant(mapping, form, request, response);//删除参与人
				}else if("delAlert".equals(type)){
					forward = delAlert(mapping, form, request, response);//删除提醒
				}else{
					forward = delTask(mapping, form, request, response);//删除任务
				}
				break;
			case OperationConst.OP_UPLOAD:	
				forward = uploadAffix(mapping, form, request, response);//上传附件
				break;
			case OperationConst.OP_QUERY:
				forward = query(mapping, form, request, response);//列表查询
				break;
			default:
				forward = query(mapping, form, request, response);//列表查询
				break;
		}
		return forward;
	}
	
	/**
	 * 批量转交负责人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateBatchTransfer(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String transferTo = getParameter("transferTo", request);
		String transferExecutor = getParameter("transferExecutor", request);
		String transferType = getParameter("transferType", request);
		
		
		String sql = "UPDATE OATASK  SET "+transferType+" = ? WHERE "+transferType+"=? ";
		ArrayList param = new ArrayList();
		param.add(transferTo);
		param.add(transferExecutor);
		
		Result rs = mgt.operationSql(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 删除提醒
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String keyId = getParameter("keyId",request);
		
		Result rs = mgt.delAlert(keyId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	private ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String clientId = getParameter("clientId",request);//客户Id
		
		//查询当前用户能选择的项目
		ArrayList<Object> itemList = new OAItemsMgt().queryItemsByUserId(getLoginBean(request).getId(),true);
		request.setAttribute("itemList",itemList);
		
		//有clientId不能修改客户
		if(clientId!=null && !"".equals(clientId)){
			request.setAttribute("clientId",clientId);
			request.setAttribute("clientName",mgt.findClientNameById(clientId));
		}
		
		//wyy 添加默认
		LoginBean loginBean = getLoginBean(request);
		request.setAttribute("loginBean", loginBean);
		request.setAttribute("crmTaskEnter", request.getParameter("crmTaskEnter"));
		
		//天华添加		
		request.setAttribute("isTHEnter", request.getParameter("isTHEnter"));		
		
		return getForward(request, mapping, "addTask");
	}

	/**
	 * 修改任务
	 * @param userId	当前登录人
	 * @param taskId	任务ID
	 * @param title		任务标题
	 * @param remark	描述
	 * @param status	状态
	 * @param executor	负责人ID
	 * @param beginTime	开始时间
	 * @param endTime	结束时间
	 * @param surveryor	验收人
	 * @param itemId	关联项目ID
	 * @param schedule	任务进度
	 * @param participant 参与人
	 * @param affix	附件
	 * @param emergencyLevel 紧急程度
	 * @param taskType	任务分类
	 * @return
	 */
	public static boolean updateTask(String userId, String taskId, String title, String remark, String status, String executor, 
			String beginTime, String endTime, String surveyor, String itemId,String schedule,String clientId,String participant,String affix,String emergencyLevel,String taskType) {
		OnlineUser user = OnlineUserInfo.getUser(userId);
		if (null == user) {
			return false;
		}
		
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		
		Result rs = new OATaskMgt().loadTaskBean(taskId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String reExecutorId = "";//存放原负责人ID,有值表示修改了负责人要删除通知消息
			String reSurveyorId = "";//存放原验收人ID,有值表示修改了负责人要删除通知消息
			
			OATaskBean taskBean = (OATaskBean)rs.retVal;
			
			String reStatus = taskBean.getStatus();//原状态
			String reParticipantStr = ","+taskBean.getParticipant();//原参与人
			String tempBeforeExecutor = taskBean.getExecutor();//临时存放修改前的负责人id
			String tempBeforeSurveyor = taskBean.getSurveyor();//临时存放修改前的验收人id
			
			taskBean.setTitle(title);
			taskBean.setRemark(remark);
			taskBean.setExecutor(executor);
			taskBean.setBeginTime(beginTime);
			taskBean.setEndTime(endTime);
			taskBean.setSurveyor(surveyor);
			taskBean.setLastUpdateBy(userId);
			taskBean.setLastUpdateTime(nowTime);
			taskBean.setItemId(itemId);
			taskBean.setStatus(status);
			taskBean.setSchedule(schedule);
			taskBean.setClientId(clientId);
			taskBean.setParticipant(participant);
			taskBean.setAffix(affix);
			taskBean.setEmergencyLevel(emergencyLevel);
			taskBean.setTaskType(taskType);
			
			if("2".equals(status)){
				//状态完成加上完成时间，
				taskBean.setFinishTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				taskBean.setSchedule("100");
			}else{
				taskBean.setFinishTime("");
			}
			
			//重启状态改进度
			if("2".equals(reStatus) && "1".equals(taskBean.getStatus())){
				taskBean.setSchedule("0");
			}
			
			//判断是否改变了负责人与验收人
			if(tempBeforeExecutor!=null && taskBean.getExecutor()!=null && !taskBean.getExecutor().equals(tempBeforeExecutor)){
				reExecutorId = tempBeforeExecutor;
			}
			
			if(tempBeforeSurveyor!=null && taskBean.getSurveyor()!=null && !taskBean.getSurveyor().equals(tempBeforeSurveyor)){
				reSurveyorId = tempBeforeSurveyor;
			}
			
			rs = new OATaskMgt().updateTaskInfo(taskBean, reExecutorId,reSurveyorId);
		
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				String url = "/OATaskAction.do?operation=5&taskId="+taskBean.getId();
				
				
				//更换指派人
				if((!"".equals(reExecutorId) || tempBeforeExecutor==null || "".equals(tempBeforeExecutor)) && !userId.equals(executor)){
					String adviceTitle = user.getName()+"指派了任务【"+title+"】给你";
					String content = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+adviceTitle+"</a>";//内容
					new AdviceMgt().add(userId, adviceTitle, content, executor, taskBean.getId(), "OATaskPoint");
				}
				
				
				//处理状态
				if(!reStatus.equals(taskBean.getStatus())){
					String titleStr = "";//标题
					String contentStr = "";//内容
					if("1".equals(reStatus)){
						if("3".equals(taskBean.getStatus())){
							titleStr = "任务【"+title+"】需要你验收";
							contentStr = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+titleStr+"</a>";//内容
							new AdviceMgt().add(userId, titleStr, contentStr, taskBean.getSurveyor(), taskBean.getId(), "OATaskSurveyor");
						}else if("2".equals(taskBean.getStatus()) && !userId.equals(taskBean.getCreateBy())){
							//给创建人发送完成状态信息
							titleStr = GlobalsTool.getEmpFullNameByUserId(userId)+"完成了任务【"+title+"】,请查看";
							contentStr = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+titleStr+"</a>";
							new AdviceMgt().add(userId, titleStr, contentStr, taskBean.getCreateBy(), taskBean.getId(), "OATask");
						}
					}else if("2".equals(reStatus)){
						//重启暂无操作
					}else{
						String receiveIds = ""; 
						titleStr = "任务【"+title+"】验收";
						if("1".equals(taskBean.getStatus())){
							titleStr += "不通过，";
						}else if("2".equals(taskBean.getStatus())){
							titleStr += "通过，";
						}
						titleStr += "验收人:"+GlobalsTool.getEmpFullNameByUserId(userId);
						contentStr = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+titleStr+"</a>";//内容
						new AdviceMgt().add(userId, titleStr, contentStr, taskBean.getExecutor(), taskBean.getId(), "OATaskSurveyorStatus");
					}
				}
				
				String calendarStatus = "0";//日程完成状态
				if("2".equals(taskBean.getStatus())){
					calendarStatus = "1";//设置为完成
				}
				
				//插入日程
				new OACalendaMgt().outAddCalendar(taskBean.getExecutor(), taskBean.getTitle(), "我的任务", taskBean.getBeginTime(),taskBean.getEndTime(), taskBean.getId(), "","1",calendarStatus,clientId);
				
				
				
				
				//处理修改参与人
				String newParticipant = "";
				if(reParticipantStr==null || "".equals(reParticipantStr)){
					//原来没有参与人，直接替换
					newParticipant = participant;
				}else{
					//原来有参与人，只发送通知给新的参与人
					if(participant!=null && !"".equals(participant)){
						for(String str : participant.split(",")){
							if(reParticipantStr.indexOf(","+str+",")==-1){
								newParticipant += str+",";
							}
						}
					}
				}
				//发送信息
				sendInviteMessage(userId, newParticipant, title, taskBean.getId());
				
				return true;
			}else{
				return false;
			}
		}
		
		return false;
	}

	/**
	 * 更新任务
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		
		String keyId = getParameter("keyId",request);//ID
		String title = getParameter("title",request);//标题
		String remark = getParameter("remark",request);//详情内容
		String executor = getParameter("executor",request);//负责人
		String beginTime = getParameter("beginTime",request);//开始时间
		String endTime = getParameter("endTime",request);//结束时间
		String surveyor = getParameter("surveyor",request);//检验人
		String itemId = getParameter("itemId",request);//检验人
		String status = getParameter("status",request);//状态
		String schedule = getParameter("schedule",request);//任务进度
		String clientId = getParameter("clientId",request);//客户Id
		String participant = getParameter("participant",request);//参与人员
		String affix = getParameter("affix",request);//附件
		String emergencyLevel = getParameter("emergencyLevel",request);//紧急程度
		String taskType = getParameter("oaTaskType",request);//任务分类
		
		LoginBean loginBean = getLoginBean(request);
		String userId = loginBean.getId();
		
		if (updateTask(userId, keyId, title, remark,status, executor, beginTime, endTime, surveyor, itemId,schedule,clientId,participant,affix,emergencyLevel,taskType)) {
			//处理附件
			if(affix!=null && !"".equals(affix)){
				for(String str :affix.split(";")){
					FileHandler.copy("OATask", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			request.setAttribute("msg","success");
		} else {
			request.setAttribute("msg","error");
		}		
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 更新前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String taskId = getParameter("taskId",request);//任务ID
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadTaskBean(taskId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			OATaskBean taskBean = (OATaskBean)rs.retVal;
			request.setAttribute("taskBean",taskBean);
			
			//查询当前用户能选择的项目
			ArrayList<Object> itemList = itemMgt.queryItemsByUserId(loginBean.getId(),true);
			request.setAttribute("itemList",itemList);
			
			request.setAttribute("clientName",mgt.findClientNameById(taskBean.getClientId()));
			
			
			if(taskBean.getParticipant()!=null && !"".equals(taskBean.getParticipant())){
				String hideParticipantInfo = "";
				for(String str:taskBean.getParticipant().split(",")){
					if(str!=null && !"".equals(str)){
						hideParticipantInfo += str+":"+GlobalsTool.getEmpFullNameByUserId(str)+";";
					}
				}
				request.setAttribute("hideParticipantInfo",hideParticipantInfo);
			}
			
			
		}
		
		request.setAttribute("crmTaskEnter", request.getParameter("crmTaskEnter"));
		request.setAttribute("loginBean",loginBean);
		return getForward(request, mapping, "update");
	}

	/**
	 * 更新单个字段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateSingleField(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String fieldName = getParameter("fieldName",request);//字段名
		String fieldVal = getParameter("fieldVal",request);//字段值
		String taskId = getParameter("taskId",request);//项目ID
		String detailHeadEnter = getParameter("detailHeadEnter",request);//主要处理详情页面头部进入修改负责人
		LoginBean loginBean = getLoginBean(request);
		
		OATaskBean taskBean = (OATaskBean)mgt.loadTaskBean(taskId).retVal; 
		String reExecutor =taskBean.getExecutor();//存放原有的负责人
		String content = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"把";//存放发送评论信息
		
		if("executor".equals(fieldName)){
			content = "负责人【"+GlobalsTool.getEmpFullNameByUserId(taskBean.getExecutor())+"】";	
			taskBean.setExecutor(fieldVal);//负责人
		}else if("surveyor".equals(fieldName)){
			content = "验收人【"+GlobalsTool.getEmpFullNameByUserId(taskBean.getSurveyor())+"】";	
			taskBean.setSurveyor(fieldVal);//验收人
		}else if("beginTime".equals(fieldName)){
			content = "开始时间【"+taskBean.getBeginTime()+"】";
			taskBean.setBeginTime(fieldVal);
		}else if("endTime".equals(fieldName)){
			content = "截止时间【"+taskBean.getEndTime()+"】";	
			taskBean.setEndTime(fieldVal);
		}else if("title".equals(fieldName)){
			content = "任务标题【"+taskBean.getTitle()+"】";	
			taskBean.setTitle(fieldVal);
		}else if("remark".equals(fieldName)){
			if(taskBean.getRemark()==null || "".equals(taskBean.getRemark())){
				content = "详情描述【空】";	
			}else{
				content = "详情描述【"+taskBean.getRemark()+"】";	
			}
			taskBean.setRemark(fieldVal);
		}else if("schedule".equals(fieldName)){
			taskBean.setSchedule(fieldVal);
		}
		
		if("executor".equals(fieldName) || "surveyor".equals(fieldName)){
			content +="修改为【"+GlobalsTool.getEmpFullNameByUserId(fieldVal)+"】";
		}else{
			content +="修改为【"+fieldVal+"】";
		}
		if(this.updateTask(loginBean.getId(), taskId, taskBean.getTitle(), taskBean.getRemark(), taskBean.getStatus(), taskBean.getExecutor(), taskBean.getBeginTime(), taskBean.getEndTime(), taskBean.getSurveyor(), taskBean.getItemId(), taskBean.getSchedule(),taskBean.getClientId(),taskBean.getParticipant(),taskBean.getAffix(),taskBean.getEmergencyLevel(),taskBean.getTaskType())){

			String msg = "success"; 
			if("executor".equals(fieldName) || "surveyor".equals(fieldName)){
				//若是修改负责人，返回时输出页面
				msg = "<span class='sFinish showAppoint'>"+GlobalsTool.getEmpFullNameByUserId(fieldVal)+" "+taskBean.getEndTime()+"</span>";
				
				String statusVal = "2";
				if(taskBean.getSurveyor() !=null && !"".equals(taskBean.getSurveyor())){
					//若有验收人,状态为3
					statusVal = "3";
				}
				
				if(reExecutor==null || "".equals(reExecutor)){
					//表示原来是未指派,加上完成按钮
					msg +="<span class='sFinish updTaskStatus' status='"+statusVal+"'>完成</span>";
				}
			}
			
			if(!"schedule".equals(fieldName)){
				//默认修改单个字段发一条评论
				new DiscussAction().add(loginBean.getId(), null, null, "OATaskLog", taskId, content, null,null,1);
			}
			request.setAttribute("msg",msg);
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 删除
	 * @param taskId
	 * @return
	 */
	public static boolean delTask(String taskId) {
		Result rs = new OATaskMgt().delTask(taskId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String taskId = getParameter("taskId",request);//任务ID
		Result rs = mgt.delTask(taskId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{  
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String taskId = getParameter("taskId",request);
		
		Result rs = mgt.loadTaskBean(taskId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			OATaskBean taskBean = (OATaskBean)rs.retVal;
			
			if(taskBean.getNextAlertTime()!=null && !"".equals(taskBean.getNextAlertTime())){
				//判断提醒时间是否超过当前。是删除
				String alertTime = taskBean.getNextAlertTime();
				Date alertDate = new Date();
				try {
					alertDate = BaseDateFormat.parse(taskBean.getNextAlertTime(), BaseDateFormat.yyyyMMddHHmmss);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if(alertDate.before(new Date())){
					rs = mgt.delAlert(taskBean.getId());
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						taskBean.setNextAlertTime("");
					}
				}
			}
			
			request.setAttribute("taskBean",taskBean);
			
			//查询关联项目BEAN
			if(taskBean.getItemId()!=null && !"".equals(taskBean.getItemId())){
				 rs = itemMgt.loadOAItem(taskBean.getItemId());
				 OAItemsBean itemBean = (OAItemsBean)rs.retVal;
				 request.setAttribute("itemBean",itemBean);
			}
			
			//查询主任务
			if(taskBean.getTaskId()!=null && !"".equals(taskBean.getTaskId())){
				 rs = mgt.loadTaskBean(taskBean.getTaskId());
				 OATaskBean mainTaskBean = (OATaskBean)rs.retVal;
				 request.setAttribute("mainTaskBean",mainTaskBean);
			}
			
			//关联客户
			request.setAttribute("clientName",mgt.findClientNameById(taskBean.getClientId()));
			
			rs = mgt.queryChildTasksByTaskId(taskId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> taskBeanList = (ArrayList<Object>)rs.retVal;
				if(taskBeanList!=null && taskBeanList.size()>0){
					
					ArrayList<Object> finishTaskList = new ArrayList<Object>();//存放已完成任务list
					ArrayList<Object> runTaskList = new ArrayList<Object>();//存放进行中任务list
					for(Object obj : taskBeanList){
						String status = String.valueOf(GlobalsTool.get(obj,3));
						if("2".equals(status)){
							finishTaskList.add(obj);
						}else{
							runTaskList.add(obj);
						}
					}
					request.setAttribute("finishTaskList",finishTaskList);
					request.setAttribute("runTaskList",runTaskList);
				}
			}
			
			//处理提醒时间
			if(taskBean.getNextAlertTime()!=null && !"".equals(taskBean.getNextAlertTime())){
				String alertDate = "";
				int alertHour = 0;
				int alterMinutes = 0;
				alertDate = taskBean.getNextAlertTime().substring(0,10);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Calendar ca = Calendar.getInstance();
				try {
					ca.setTime(sdf.parse(taskBean.getNextAlertTime()));
					alertHour = ca.get(Calendar.HOUR_OF_DAY);
					alterMinutes = ca.get(Calendar.MINUTE);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				request.setAttribute("alertDate",alertDate);
				request.setAttribute("alertHour",alertHour);
				request.setAttribute("alterMinutes",alterMinutes);
			}
			/*查看是否收藏过帖子*/
			Result isExist = new AttentionMgt().isAttention(getLoginBean(request).getId(), taskId, "OATask");
			if(isExist.retCode == ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attentionCard", "attentionCard");
			}
			
			//获取所有职员信息,用于textBox控件
			ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
			request.setAttribute("textBoxValues",gson.toJson(list));
			
			request.setAttribute("loginBean",getLoginBean(request));
			return getForward(request, mapping, "detail");
		}else{
			 //添加失败
			EchoMessage.error().add(getMessage(request, "bbs.forum.not.find"))
					.setAlertRequest(request);
            return getForward(request, mapping, "alert");
			
		}
		
		
		
		/*
		//获取快速添加的周期截止时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		LinkedHashMap<String,String> weekMap = new LinkedHashMap<String, String>();
		String defWeekName = "本";
		int weekIndex = 0;
		for(int i = 0;i<7;i++){
			weekIndex = today.get(Calendar.DAY_OF_WEEK)-1;
			weekMap.put(defWeekName+GlobalsTool.getWeekCN(weekIndex),sdf.format(today.getTime()));
			today.add(Calendar.DATE,1);
			if(weekIndex==0){
				defWeekName = "下";
			}
		}
		request.setAttribute("weekMap",weekMap);
		*/
	}

	/**
	 * 改变状态
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String taskId = getParameter("taskId",request);//任务ID
		String status = getParameter("status",request);//状态值 1=执行中 2.完成 3.验收中
		String feedbackContent = getParameter("feedbackContent",request);//反馈内容
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadTaskBean(taskId);
		OATaskBean taskBean = (OATaskBean)rs.retVal;
		
		if(this.updateTask(loginBean.getId(), taskId, taskBean.getTitle(), taskBean.getRemark(), status, taskBean.getExecutor(), taskBean.getBeginTime(), taskBean.getEndTime(), taskBean.getSurveyor(), taskBean.getItemId(),taskBean.getSchedule(),taskBean.getClientId(),taskBean.getParticipant(),taskBean.getAffix(),taskBean.getEmergencyLevel(),taskBean.getTaskType())){
			
			//评论操作
			String content = loginBean.getEmpFullName()+"在"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"把任务标记为";
			if("2".equals(taskBean.getStatus())){
				if("1".equals(status)){
					content +="重启";
				}
			}else if("3".equals(taskBean.getStatus())){
				if("1".equals(status)){
					content +="退回";	
				}else{
					content +="通过";
				}
			}else{
				content +="完成";
			}
			content +="，情况描述:"+feedbackContent;
			new DiscussAction().add(loginBean.getId(), null, null, "OATaskLog", taskBean.getId(), content, null,null,1);
			
			
			if("3".equals(status)){
				request.setAttribute("msg","3");
			}else if("1".equals(status)){
				request.setAttribute("msg","1");
			}else{
				//拼接已完成信息,输出到页面
				String sql = "SELECT (select count(OATaskLog.id) from OATaskLog where OATaskLog.f_ref=OATask.id) as logCount FROM OATask WHERE id = ?";
				ArrayList paramNew = new ArrayList();
				paramNew.add(taskId);
				rs = mgt.publicSqlQuery(sql, paramNew);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList<Object> taskList = (ArrayList<Object>)rs.retVal;
					if(taskList!=null && taskList.size()>0){
						String logCount = String.valueOf(GlobalsTool.get(taskList.get(0),0));
						String msgInfo = " <li id='"+taskId+"'><span class='tContent'>"+taskBean.getTitle()+"</span><span class='sFinish'>"+GlobalsTool.getEmpFullNameByUserId(taskBean.getExecutor())+" "+taskBean.getEndTime()+"</span>";
						if(!"0".equals(logCount)){
							msgInfo+="<span class='sFinish'>"+logCount+"条评论</span>";
						}
						msgInfo+=" </li>";
						request.setAttribute("msg",msgInfo);
					}
				}
			}
			
		}
		
		request.setAttribute("loginBean",getLoginBean(request));
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 添加任务
	 * @param userId	登录人ID
	 * @param id		任务ID，可为空，也可以外面传进来
	 * @param title		标题
	 * @param remark	描述
	 * @param executor	负责人
	 * @param beginTime	开始时间
	 * @param endTime	结束时间
	 * @param surveyor	验收人
	 * @param itemId	关联项目ID
	 * @param parentTaskId	父级ID
	 * @param schedule	任务进度
	 * @param clientId	客户Id
	 * @return
	 */
	public static boolean addTask(String userId, String id, String title, String remark, String executor, 
			String beginTime, String endTime, String surveyor, String itemId, String parentTaskId,String schedule,String clientId,String participant,String affix,String emergencyLevel,String taskType) {
		OnlineUser user = OnlineUserInfo.getUser(userId);
		if (null == user) {
			return false;
		}
		
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		if(beginTime==null || "".equals(beginTime)){
			beginTime = nowTime.substring(0,10);
		}
		
		if (null == id || "".equals(id)) {
			id = IDGenerater.getId();
		}
		
		if(emergencyLevel==null || "".equals(emergencyLevel)){
			emergencyLevel = "A";
		}
		
		OATaskBean taskBean = new OATaskBean();
		taskBean.setId(id);
		taskBean.setTitle(title);
		taskBean.setRemark(remark);
		taskBean.setExecutor(executor);
		taskBean.setBeginTime(beginTime);
		taskBean.setEndTime(endTime);
		taskBean.setStatus("1");
		taskBean.setItemId(itemId);
		taskBean.setSurveyor(surveyor);
		taskBean.setIsCatalog("0");
		taskBean.setCreateBy(userId);
		taskBean.setCreateTime(nowTime);
		taskBean.setLastUpdateBy(userId);
		taskBean.setLastUpdateTime(nowTime);
		taskBean.setTaskId(parentTaskId);
		taskBean.setSchedule(schedule);
		taskBean.setClientId(clientId);
		taskBean.setParticipant(participant);
		taskBean.setAffix(affix);
		taskBean.setEmergencyLevel(emergencyLevel);
		taskBean.setTaskType(taskType);
		
		
		
		Result rs = new OATaskMgt().addTaskBean(taskBean);
		
		//插入日程
		new OACalendaMgt().outAddCalendar(taskBean.getExecutor(), title, "我的任务", beginTime, endTime, id, "","1","0",clientId);
		
		//发送信息
		sendInviteMessage(userId, participant, title, id);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 添加与快速添加任务
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String title = request.getParameter("title");//标题
		String remark = request.getParameter("remark");//详情内容
		String executor = request.getParameter("executor");//负责人
		String beginTime = getParameter("beginTime",request);//开始时间
		String endTime = getParameter("endTime",request);//结束时间
		String itemId = getParameter("itemId",request);//项目ID
		String surveyor = getParameter("surveyor",request);//检验人
		String taskId = getParameter("taskId",request);//任务ID
		String schedule = getParameter("schedule",request);//任务进度
		String clientId = getParameter("clientId",request);//客户ID
		String participant = getParameter("participant",request);//参与人员
		String affix = getParameter("affix",request);//附件
		String emergencyLevel = getParameter("emergencyLevel",request);//紧急程度
		String taskType = getParameter("oaTaskType",request);//任务分类
		
		//String isCatalog = getParameter("isCatalog",request);//是否根级目录,1表示子任务
				
		LoginBean loginBean = getLoginBean(request);
		String userId = loginBean.getId();
		String id = IDGenerater.getId();
		
		if(addTask(userId, id, title, remark, executor, beginTime, endTime, surveyor, itemId, taskId,schedule,clientId,participant,affix,emergencyLevel,taskType)){
			
			//处理附件
			if(affix!=null && !"".equals(affix)){
				for(String str :affix.split(";")){
					FileHandler.copy("OATask", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			
			//拼接任务信息，用于项目详情-任务tab新增后展示信息
			String executorName = "";
			if(executor!=null && !"".equals(executor)){
				executorName = GlobalsTool.getEmpFullNameByUserId(executor);
			}
			
			String changeStatus = "2";
			if(surveyor!=null &&!"".equals(surveyor)){
				changeStatus = "3";
			}
			
			String taskInfo = id+";"+title+";"+executorName+";"+endTime+";"+changeStatus;
			request.setAttribute("msg",taskInfo);
			
			//指派任务，发送信息(条件:创建者!=负责人)
			if(executor!=null && !"".equals(executor) && !userId.equals(executor)){
				//向参与者发送通知消息
				String adviceTitle = loginBean.getEmpFullName()+"指派了任务【"+title+"】给你";
				String url = "/OATaskAction.do?operation=5&taskId="+id;
				String content = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+adviceTitle+"</a>";//内容
				new AdviceMgt().add(loginBean.getId(), adviceTitle, content, executor, id, "OATaskPoint");
			}
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 项目详情页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detailByItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String itemId = getParameter("itemId",request);//项目ID;
		
		if(itemId!=null && !"".equals(itemId)){
			Result rs = mgt.queryTasksByItemId(itemId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> taskBeanList = (ArrayList<Object>)rs.retVal;
				
				if(taskBeanList!=null && taskBeanList.size()>0){
					ArrayList<Object> finishTaskList = new ArrayList<Object>();//存放已完成任务list
					ArrayList<Object> runTaskList = new ArrayList<Object>();//存放进行中任务list
					for(Object obj : taskBeanList){
						String status = String.valueOf(GlobalsTool.get(obj,3));
						if("2".equals(status)){
							finishTaskList.add(obj);
						}else{
							runTaskList.add(obj);
						}
					}
					request.setAttribute("finishTaskList",finishTaskList);
					request.setAttribute("runTaskList",runTaskList);
				}
			}
			
			rs = itemMgt.loadOAItem(itemId);
			OAItemsBean itemBean = (OAItemsBean)rs.retVal;
			request.setAttribute("itemBean", itemBean);
			request.setAttribute("itemId", itemId);
		}
		request.setAttribute("loginBean",getLoginBean(request));
		return getForward(request, mapping, "detailByItem");
	}

	/**
	 * 查询页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OATaskSearchForm oaTaskSearchform = (OATaskSearchForm) form;
		String clientId = getParameter("clientId",request);
		String isTHTask = getParameter("isTHTask",request);//是否是天华主页的
		if(clientId!=null && !"".equals(clientId)){
			//客户列表详情页面点击更多，进入过滤这个客户的ID
			oaTaskSearchform.setSearchClientId(clientId);
		}
		LoginBean loginBean = getLoginBean(request);
		StringBuilder condition = new StringBuilder();//查询条件
		StringBuilder countCondition = new StringBuilder();//数量查询条件
		
		String crmTaskEnter = getParameter("crmTaskEnter", request);//true 表示CRM任务进入
		
		request.setAttribute("crmTaskEnter",crmTaskEnter);
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	pageSize = 10;
        }
        
        String userId = loginBean.getId();
        
		//tab头部查询
        String tabSelectName = oaTaskSearchform.getTabSelectName();
		if(tabSelectName==null || "".equals(tabSelectName)){
			//默认取我负责的
			condition.append(" and executor= '").append(userId).append("' ");
			oaTaskSearchform.setTabSelectName("tab_executor");
		}else{
			tabSelectName = tabSelectName.substring(4);
			condition.append(" and ");	
			if("participant".equals(tabSelectName)){
				condition.append("( ','+ ").append(tabSelectName).append(" like '%,").append(userId).append(",%' or createBy='").append(userId).append("' or surveyor='").append(userId).append("') ");
			}else if("createBy".equals(tabSelectName)){
				condition.append(tabSelectName).append(" = '").append(userId).append("' and executor <> '").append(userId).append("' ");
			}else{
				condition.append(tabSelectName).append(" = '").append(userId).append("' ");
			}
		}
        
		
		if(crmTaskEnter!=null && "true".equals(crmTaskEnter)){
			condition.append(" and isNull(clientId,'') <> ''");
			countCondition.append(" and isNull(clientId,'') <> ''");
		}
		
		//若有模糊查询就不管状态了
		if(oaTaskSearchform.getHasSearchCondition()==null || "".equals(oaTaskSearchform.getHasSearchCondition())){
			//状态查询
			if(oaTaskSearchform.getSearchStatus()==null || "".equals(oaTaskSearchform.getSearchStatus())){
				//默认与状态==1  默认查询执行中
				oaTaskSearchform.setSearchStatus("1");
			}
			
			condition.append(" and status = '").append(oaTaskSearchform.getSearchStatus()).append("' ");
			countCondition.append(" and status = '").append(oaTaskSearchform.getSearchStatus()).append("' ");
		}else{
			oaTaskSearchform.setSearchStatus("");
		}
		
		//编号
		if(oaTaskSearchform.getSearchTaskOrder()!=null && !"".equals(oaTaskSearchform.getSearchTaskOrder())){
			condition.append(" and taskOrder like '%").append(oaTaskSearchform.getSearchTaskOrder()).append("%' ");
			countCondition.append(" and taskOrder like '%").append(oaTaskSearchform.getSearchTaskOrder()).append("%' ");
		}
		
		//关键字
		if(oaTaskSearchform.getSearchKeyWord()!=null && !"".equals(oaTaskSearchform.getSearchKeyWord())){
			condition.append(" and (title like '%").append(oaTaskSearchform.getSearchKeyWord()).append("%' or remark like '%").append(oaTaskSearchform.getSearchKeyWord()).append("%') ");
			countCondition.append(" and (title like '%").append(oaTaskSearchform.getSearchKeyWord()).append("%' or remark like '%").append(oaTaskSearchform.getSearchKeyWord()).append("%') ");
		}
		
		//时间查询
		String beginStartTime = oaTaskSearchform.getSearchBeginStartTime();
		String beginOverTime = oaTaskSearchform.getSearchBeginOverTime();
		String endStartTime = oaTaskSearchform.getSearchEndStartTime();
		String endOverTime = oaTaskSearchform.getSearchEndOverTime();
		
		//处理开始时间
		if(beginStartTime!=null && !"".equals(beginStartTime)){
			condition.append(" and beginTime >= '").append(beginStartTime).append("' ");
			countCondition.append(" and beginTime >= '").append(beginStartTime).append("' ");
		}
		
		if(beginOverTime!=null && !"".equals(beginOverTime)){
			condition.append(" and beginTime <= '").append(beginOverTime).append("' ");
			countCondition.append(" and beginTime <= '").append(beginOverTime).append("' ");
		}
		
		//处理结束时间
		if(endStartTime!=null && !"".equals(endStartTime)){
			condition.append(" and endTime >= '").append(endStartTime).append("' ");
			countCondition.append(" and endTime >= '").append(endStartTime).append("' ");
		}
		
		if(endOverTime!=null && !"".equals(endOverTime)){
			condition.append(" and endTime <= '").append(endOverTime).append("' ");
			countCondition.append(" and endTime <= '").append(endOverTime).append("' ");
		}
		
		//创建人
		if(oaTaskSearchform.getSearchCreateBy()!=null && !"".equals(oaTaskSearchform.getSearchCreateBy())){
			condition.append(" and createBy = '").append(oaTaskSearchform.getSearchCreateBy()).append("' ");
			countCondition.append(" and createBy = '").append(oaTaskSearchform.getSearchCreateBy()).append("' ");
		}
		
		//负责人
		if(oaTaskSearchform.getSearchExecutor()!=null && !"".equals(oaTaskSearchform.getSearchExecutor())){
			condition.append(" and executor = '").append(oaTaskSearchform.getSearchExecutor()).append("' ");
			countCondition.append(" and executor = '").append(oaTaskSearchform.getSearchExecutor()).append("' ");
		}
		
		//验收人
		if(oaTaskSearchform.getSearchSurveyor()!=null && !"".equals(oaTaskSearchform.getSearchSurveyor())){
			condition.append(" and surveyor = '").append(oaTaskSearchform.getSearchSurveyor()).append("' ");
			countCondition.append(" and surveyor = '").append(oaTaskSearchform.getSearchSurveyor()).append("' ");
		}
		
		//紧急程度
		if(oaTaskSearchform.getSearchEmergencyLevel()!=null && !"".equals(oaTaskSearchform.getSearchEmergencyLevel())){
			condition.append(" and emergencyLevel = '").append(oaTaskSearchform.getSearchEmergencyLevel()).append("' ");
			countCondition.append(" and emergencyLevel = '").append(oaTaskSearchform.getSearchEmergencyLevel()).append("' ");
		}
		
		//任务分类
		if(oaTaskSearchform.getSearchOaTaskType()!=null && !"".equals(oaTaskSearchform.getSearchOaTaskType())){
			condition.append(" and taskType = '").append(oaTaskSearchform.getSearchOaTaskType()).append("' ");
			countCondition.append(" and taskType = '").append(oaTaskSearchform.getSearchOaTaskType()).append("' ");
		}
		
		//验收人
		if(oaTaskSearchform.getSearchSurveyor()!=null && !"".equals(oaTaskSearchform.getSearchSurveyor())){
			condition.append(" and surveyor = '").append(oaTaskSearchform.getSearchSurveyor()).append("' ");
			countCondition.append(" and surveyor = '").append(oaTaskSearchform.getSearchSurveyor()).append("' ");
		}
		
		//客户
		if(oaTaskSearchform.getSearchClientId()!=null && !"".equals(oaTaskSearchform.getSearchClientId())){
			condition.append(" and clientId = '").append(oaTaskSearchform.getSearchClientId()).append("' ");
			countCondition.append(" and clientId = '").append(oaTaskSearchform.getSearchClientId()).append("' ");
			request.setAttribute("clientName", mgt.findClientNameById(oaTaskSearchform.getSearchClientId()));
		}
		
		if("true".equals(isTHTask)){
			condition = new StringBuilder("");
			if("deptPerson".equals(getParameter("queryType", request))){
				condition.append(" and executor = '"+getLoginBean(request).getId()+"' and isnull(taskId,'') ='' ");
			}else{
				condition.append(" and executor = '"+getLoginBean(request).getId()+"' and isnull(taskId,'') ='' ");
			}			
		}
		
		Result rs = mgt.tasksQuery(condition.toString(),pageNo,pageSize);
		
			ArrayList<OATaskBean> taskList = (ArrayList<OATaskBean>)rs.retVal;
			request.setAttribute("tasksList",taskList);
			request.setAttribute("pageBar",pageBar2(rs, request)) ;
			request.setAttribute("oaTaskform",oaTaskSearchform);
			request.setAttribute("loginBean",getLoginBean(request));

			//查询当前用户能选择的项目
			ArrayList<Object> itemList = itemMgt.queryItemsByUserId(userId,false);
			request.setAttribute("itemList",itemList);
			
			//查询tab头部数量
			HashMap<String,String> countMap = mgt.getRunTabCount(loginBean,countCondition.toString());
			request.setAttribute("countMap",countMap);
			
			//获取所有职员信息,用于textBox控件
			ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
			request.setAttribute("textBoxValues",gson.toJson(list));
			
			if("true".equals(crmTaskEnter)){
				if(taskList!=null && taskList.size()>0){
					String clientIdStr="";
					for(OATaskBean bean : taskList){
						clientIdStr += "'"+bean.getClientId()+"',";
					}
					if(clientIdStr.endsWith(",")){
						clientIdStr = clientIdStr.substring(0,clientIdStr.length()-1);
					}
					HashMap<String,String> clientsMap = mgt.queryClientMapByIds(clientIdStr);
					request.setAttribute("clientsMap",clientsMap);
				}
			}
			
			if("true".equals(isTHTask)){
				if("true".equals(getParameter("isnum", request))){
					request.setAttribute("msg",countMap.get("executor"));
					return getForward(request, mapping, "blank");
				}
				return getForward(request, mapping, "oaTask");
			}
			
			return getForward(request, mapping, "query");
	}

	/**
	 * 获取参与者信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateParticipantsInfoInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String taskId = getParameter("taskId",request);
		String taskTitle = getParameter("taskTitle",request);//项目名称
		String employeeIds = getParameter("employeeIds",request);
		LoginBean loginBean = getLoginBean(request);
		//更新参与人员

		Result rs = mgt.updateParticipants(employeeIds, loginBean, taskId);
		
		if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
			//封装用户ID查找头像信息
			String keyIds = "";
			for(String employeeId : employeeIds.split(",")){
				if(employeeId!=null && !"".equals(employeeId)){
					keyIds +="'"+employeeId+"',";
				}
			}
			if(keyIds.endsWith(",")){
				keyIds = keyIds.substring(0,keyIds.length()-1);
			}
			
			if("".equals(keyIds)){
				return getForward(request, mapping, "blank");
			}
			
			String sql = "SELECT id,empFullName FROM tblEmployee WHERE id in("+keyIds+")";
			rs = mgt.publicSqlQuery(sql, new ArrayList());
			if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
				String msg = "";//用于存放拼装的参与者信息
				ArrayList<Object> rsList = (ArrayList<Object>)rs.retVal;
				if(rsList!=null && rsList.size()>0){
					for(Object obj : rsList){
						String id = GlobalsTool.get(obj,0)+"";//职员ID
						if(id!=null && !"".equals(id)){
							String empFullName = GlobalsTool.get(obj,1)+"";//职员全称
							String photo = GlobalsTool.checkEmployeePhoto("48",String.valueOf(GlobalsTool.get(obj,0)));//头像信息
							msg += "<li empId='"+id+"' class='mesOnline'><a href='javascript:top.msgCommunicate(\""+id+"\",\""+empFullName+"\")' class='lf Image'><img src='"+photo+"'/></a> <i class='lf'><b>"+empFullName+"</b></i> <b class='icons b-del-t'></b></li>";
						}
					}
					request.setAttribute("msg",msg);
					
					//向参与者发送通知消息
					sendInviteMessage(loginBean.getId(), employeeIds, taskTitle, taskId);
				}
			}else{
				request.setAttribute("msg","error");
			}
		}else{
			request.setAttribute("msg","error");
		}
       
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 附件上传
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward uploadAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			
		String taskId = getParameter("taskId",request);//任务id
		String uploadStrs = getParameter("uploadStrs",request);//附件信息
		LoginBean loginBean = getLoginBean(request);
		String discussContent = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"上传了附件:";//评论内容
		if(uploadStrs!=null && !"".equals(uploadStrs)){
			int handleType = FileHandler.TYPE_AFFIX;//附件
			
			for(String str :uploadStrs.split(";")){
				discussContent += "<a href='/ReadFile?fileName="+str+"&realName="+str+"&tempFile=false&type=AFFIX&tableName=OATask'>"+str+"</a>、";
				FileHandler.copy("OATask", FileHandler.TYPE_AFFIX, str, str);
				FileHandler.deleteTemp(str);
			}
			if(discussContent.endsWith("、")){
				discussContent = discussContent.substring(0,discussContent.length()-1);
			}
		}
		
		Result rs = mgt.loadTaskBean(taskId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String newAffix = "";
			OATaskBean taskBean = (OATaskBean)rs.retVal;
			if(taskBean.getAffix() == null || "".equals(taskBean.getAffix())){
				newAffix = uploadStrs;
			}else{
				newAffix = taskBean.getAffix()+uploadStrs;
			}
			
			taskBean.setAffix(newAffix);
			rs = mgt.updateTaskBean(taskBean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
			
			new DiscussAction().add(getLoginBean(request).getId(), null, null, "OATaskLog", taskId, discussContent, null,null,1);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 异步删除附件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String taskId = getParameter("taskId",request);//任务ID
		String fileName = getParameter("fileName",request);//删除的文件名
		String tempFile = getParameter("tempFile",request);//是否临时文件,true:是,false:否
		
		if (fileName != null && fileName.length() != 0) {
			
			String sql = "UPDATE OATask SET  affix = replace(affix,'"+fileName+";','') where id = '"+taskId+"'";
			ArrayList param = new ArrayList();
			Result rs = mgt.operationSql(sql, param);
			
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				if("false".equals(tempFile)){
					//删除正式的
					FileHandler.delete("OATask",FileHandler.TYPE_AFFIX, fileName);
				}else{
					//删除临时文件
					FileHandler.deleteTemp(fileName);
				}
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
			
		}
	   				
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 添加提醒
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addWran(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyId = getParameter("keyId",request);//单据ID
		String alterDay = getParameter("alterDay",request);//提醒日期
		int alterHour = getParameterInt("alterHour",request);//提醒小时
		int alterMinutes = getParameterInt("alterMinutes",request);//提醒分钟
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadTaskBean(keyId);
		OATaskBean taskBean = (OATaskBean)rs.retVal;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AlertBean alertBean = new AlertBean();        

		//先把已经提醒的删除
		String delSql = "DELETE FROM tblAlert WHERE relationId = ?";
		ArrayList param = new ArrayList();
		param.add(keyId);
		mgt.operationSql(delSql, param); 
		
		
		String nextAlertTime = alterDay+" "+alterHour+":"+alterMinutes+":00";
		
		alertBean.setId(IDGenerater.getId());
		alertBean.setAlertDate(alterDay);
		alertBean.setAlertHour(alterHour);        
		alertBean.setAlertMinute(alterMinutes);	
		alertBean.setNextAlertTime(nextAlertTime);
		alertBean.setIsLoop("no");	                   				            					
		alertBean.setLoopTime(0);            			                    			
		alertBean.setAlertType("4,");
		alertBean.setCreateBy(loginBean.getId());
		alertBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		alertBean.setRelationId(keyId);                			
		alertBean.setStatusId(0);
		String popedomUserIds = loginBean.getId()+","+keyId;                   		
		alertBean.setPopedomUserIds(popedomUserIds);
		
		String context = "";
		if(taskBean!=null && !"".equals(taskBean.getTitle())){
			context = "任务【"+taskBean.getTitle()+"】提醒，请查看。";
		}
		
		alertBean.setAlertContent(context);
		String url = "/OATaskAction.do?operation=5&taskId="+keyId;
		String content = "<a href=\"javascript:mdiwin('" + url + "','"+taskBean.getTitle()+"')\">"+context+"</a>";//内容
		alertBean.setAlertUrl(content);                   			
		
		rs = mgt.addWarn(alertBean,keyId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","<span title='提醒时间："+nextAlertTime+"'>已设提醒</span><b class='delAlert' actionName='OATaskAction' keyId='"+keyId+"'></b>");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 删除参与人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delParticipant(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String employeeId = getParameter("employeeId",request);//职员ID
		String participants = getParameter("participants",request);//存放其余的参与者
		String taskId = getParameter("taskId",request);//任务id
		
		Result rs = mgt.delParticipant(taskId, employeeId,participants);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}
	
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
	
		if(getLoginBean(req)==null){
			 return getForward(req, mapping, "indexPage");//提出	
		}

		return null;
	}
	
	/**
	 * 发送邀请任务通知
	 * @param loginBean
	 * @param employeeIds
	 * @param taskTitle
	 * @param taskId
	 */
	public static void sendInviteMessage(String userId,String employeeIds,String taskTitle,String taskId){
		//向参与者发送通知消息
		String title = GlobalsTool.getEmpFullNameByUserId(userId)+"邀请您加入任务【"+taskTitle+"】";
		String url = "/OATaskAction.do?operation=5&taskId="+taskId;
		String content = "<a href=\"javascript:mdiwin('" + url + "','"+taskTitle+"')\">"+title+"</a>";//内容
		employeeIds = new DiscussAction().filterReceiveIdByLoginId(employeeIds, userId);//过滤包含我的通知人
		new AdviceMgt().add(userId, title, content, employeeIds, taskId, "OATask");
		
	}
}
