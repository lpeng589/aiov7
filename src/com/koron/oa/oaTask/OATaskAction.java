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
 * <p>Title:�ҵ�����</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: �����п���������޹�˾
 * @Author ��ܿ�
 */
public class OATaskAction extends BaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	OATaskMgt mgt = new OATaskMgt();
	OAItemsMgt itemMgt = new OAItemsMgt();
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// ���ܷ���
		String type = getParameter("type", request);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		 /*�Ƿ����body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		switch (operation) {
			case OperationConst.OP_ADD:	
				if("addWarn".equals(type)){
					forward = addWran(mapping, form, request, response);
				}else{
					forward = addTask(mapping, form, request, response);//���
				}
				break;
			case OperationConst.OP_ADD_PREPARE:					
					forward = addPrepare(mapping, form, request, response);//���ǰ			
				break;
			case OperationConst.OP_UPDATE_PREPARE:
				forward = updatePrepare(mapping, form, request, response);//����ǰ
				break;
			case OperationConst.OP_UPDATE:
					if("changeStatus".equals(type)){
						forward = changeStatus(mapping, form, request, response);//�ı�����״̬
					}else if("participantsInfo".equals(type)){
						forward = updateParticipantsInfoInfo(mapping, form, request, response);//���²�����
					}else if("updateSingle".equals(type)){
						forward = updateSingleField(mapping, form, request, response);//�����ֶθ���
					}else if ("batchTransfer".equals(type)) {
						forward = updateBatchTransfer(mapping, form, request, response);// ����ת��������
					}else{
						forward = update(mapping, form, request, response);//��������
					}
				break;
			case OperationConst.OP_DETAIL:	
				if("detailByItem".equals(type)){
					forward = detailByItem(mapping, form, request, response);//��Ŀ����-�����ѯ
				}else{
					forward = detail(mapping, form, request, response);//��������
				}
				break;
			case OperationConst.OP_DELETE:	
				if("affix".equals(type)){
					forward = delAffix(mapping, form, request, response);//ɾ������
				}else if("participant".equals(type)){
					forward = delParticipant(mapping, form, request, response);//ɾ��������
				}else if("delAlert".equals(type)){
					forward = delAlert(mapping, form, request, response);//ɾ������
				}else{
					forward = delTask(mapping, form, request, response);//ɾ������
				}
				break;
			case OperationConst.OP_UPLOAD:	
				forward = uploadAffix(mapping, form, request, response);//�ϴ�����
				break;
			case OperationConst.OP_QUERY:
				forward = query(mapping, form, request, response);//�б��ѯ
				break;
			default:
				forward = query(mapping, form, request, response);//�б��ѯ
				break;
		}
		return forward;
	}
	
	/**
	 * ����ת��������
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
	 * ɾ������
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
		String clientId = getParameter("clientId",request);//�ͻ�Id
		
		//��ѯ��ǰ�û���ѡ�����Ŀ
		ArrayList<Object> itemList = new OAItemsMgt().queryItemsByUserId(getLoginBean(request).getId(),true);
		request.setAttribute("itemList",itemList);
		
		//��clientId�����޸Ŀͻ�
		if(clientId!=null && !"".equals(clientId)){
			request.setAttribute("clientId",clientId);
			request.setAttribute("clientName",mgt.findClientNameById(clientId));
		}
		
		//wyy ���Ĭ��
		LoginBean loginBean = getLoginBean(request);
		request.setAttribute("loginBean", loginBean);
		request.setAttribute("crmTaskEnter", request.getParameter("crmTaskEnter"));
		
		//�컪���		
		request.setAttribute("isTHEnter", request.getParameter("isTHEnter"));		
		
		return getForward(request, mapping, "addTask");
	}

	/**
	 * �޸�����
	 * @param userId	��ǰ��¼��
	 * @param taskId	����ID
	 * @param title		�������
	 * @param remark	����
	 * @param status	״̬
	 * @param executor	������ID
	 * @param beginTime	��ʼʱ��
	 * @param endTime	����ʱ��
	 * @param surveryor	������
	 * @param itemId	������ĿID
	 * @param schedule	�������
	 * @param participant ������
	 * @param affix	����
	 * @param emergencyLevel �����̶�
	 * @param taskType	�������
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
			String reExecutorId = "";//���ԭ������ID,��ֵ��ʾ�޸��˸�����Ҫɾ��֪ͨ��Ϣ
			String reSurveyorId = "";//���ԭ������ID,��ֵ��ʾ�޸��˸�����Ҫɾ��֪ͨ��Ϣ
			
			OATaskBean taskBean = (OATaskBean)rs.retVal;
			
			String reStatus = taskBean.getStatus();//ԭ״̬
			String reParticipantStr = ","+taskBean.getParticipant();//ԭ������
			String tempBeforeExecutor = taskBean.getExecutor();//��ʱ����޸�ǰ�ĸ�����id
			String tempBeforeSurveyor = taskBean.getSurveyor();//��ʱ����޸�ǰ��������id
			
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
				//״̬��ɼ������ʱ�䣬
				taskBean.setFinishTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				taskBean.setSchedule("100");
			}else{
				taskBean.setFinishTime("");
			}
			
			//����״̬�Ľ���
			if("2".equals(reStatus) && "1".equals(taskBean.getStatus())){
				taskBean.setSchedule("0");
			}
			
			//�ж��Ƿ�ı��˸�������������
			if(tempBeforeExecutor!=null && taskBean.getExecutor()!=null && !taskBean.getExecutor().equals(tempBeforeExecutor)){
				reExecutorId = tempBeforeExecutor;
			}
			
			if(tempBeforeSurveyor!=null && taskBean.getSurveyor()!=null && !taskBean.getSurveyor().equals(tempBeforeSurveyor)){
				reSurveyorId = tempBeforeSurveyor;
			}
			
			rs = new OATaskMgt().updateTaskInfo(taskBean, reExecutorId,reSurveyorId);
		
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				String url = "/OATaskAction.do?operation=5&taskId="+taskBean.getId();
				
				
				//����ָ����
				if((!"".equals(reExecutorId) || tempBeforeExecutor==null || "".equals(tempBeforeExecutor)) && !userId.equals(executor)){
					String adviceTitle = user.getName()+"ָ��������"+title+"������";
					String content = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+adviceTitle+"</a>";//����
					new AdviceMgt().add(userId, adviceTitle, content, executor, taskBean.getId(), "OATaskPoint");
				}
				
				
				//����״̬
				if(!reStatus.equals(taskBean.getStatus())){
					String titleStr = "";//����
					String contentStr = "";//����
					if("1".equals(reStatus)){
						if("3".equals(taskBean.getStatus())){
							titleStr = "����"+title+"����Ҫ������";
							contentStr = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+titleStr+"</a>";//����
							new AdviceMgt().add(userId, titleStr, contentStr, taskBean.getSurveyor(), taskBean.getId(), "OATaskSurveyor");
						}else if("2".equals(taskBean.getStatus()) && !userId.equals(taskBean.getCreateBy())){
							//�������˷������״̬��Ϣ
							titleStr = GlobalsTool.getEmpFullNameByUserId(userId)+"���������"+title+"��,��鿴";
							contentStr = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+titleStr+"</a>";
							new AdviceMgt().add(userId, titleStr, contentStr, taskBean.getCreateBy(), taskBean.getId(), "OATask");
						}
					}else if("2".equals(reStatus)){
						//�������޲���
					}else{
						String receiveIds = ""; 
						titleStr = "����"+title+"������";
						if("1".equals(taskBean.getStatus())){
							titleStr += "��ͨ����";
						}else if("2".equals(taskBean.getStatus())){
							titleStr += "ͨ����";
						}
						titleStr += "������:"+GlobalsTool.getEmpFullNameByUserId(userId);
						contentStr = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+titleStr+"</a>";//����
						new AdviceMgt().add(userId, titleStr, contentStr, taskBean.getExecutor(), taskBean.getId(), "OATaskSurveyorStatus");
					}
				}
				
				String calendarStatus = "0";//�ճ����״̬
				if("2".equals(taskBean.getStatus())){
					calendarStatus = "1";//����Ϊ���
				}
				
				//�����ճ�
				new OACalendaMgt().outAddCalendar(taskBean.getExecutor(), taskBean.getTitle(), "�ҵ�����", taskBean.getBeginTime(),taskBean.getEndTime(), taskBean.getId(), "","1",calendarStatus,clientId);
				
				
				
				
				//�����޸Ĳ�����
				String newParticipant = "";
				if(reParticipantStr==null || "".equals(reParticipantStr)){
					//ԭ��û�в����ˣ�ֱ���滻
					newParticipant = participant;
				}else{
					//ԭ���в����ˣ�ֻ����֪ͨ���µĲ�����
					if(participant!=null && !"".equals(participant)){
						for(String str : participant.split(",")){
							if(reParticipantStr.indexOf(","+str+",")==-1){
								newParticipant += str+",";
							}
						}
					}
				}
				//������Ϣ
				sendInviteMessage(userId, newParticipant, title, taskBean.getId());
				
				return true;
			}else{
				return false;
			}
		}
		
		return false;
	}

	/**
	 * ��������
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
		String title = getParameter("title",request);//����
		String remark = getParameter("remark",request);//��������
		String executor = getParameter("executor",request);//������
		String beginTime = getParameter("beginTime",request);//��ʼʱ��
		String endTime = getParameter("endTime",request);//����ʱ��
		String surveyor = getParameter("surveyor",request);//������
		String itemId = getParameter("itemId",request);//������
		String status = getParameter("status",request);//״̬
		String schedule = getParameter("schedule",request);//�������
		String clientId = getParameter("clientId",request);//�ͻ�Id
		String participant = getParameter("participant",request);//������Ա
		String affix = getParameter("affix",request);//����
		String emergencyLevel = getParameter("emergencyLevel",request);//�����̶�
		String taskType = getParameter("oaTaskType",request);//�������
		
		LoginBean loginBean = getLoginBean(request);
		String userId = loginBean.getId();
		
		if (updateTask(userId, keyId, title, remark,status, executor, beginTime, endTime, surveyor, itemId,schedule,clientId,participant,affix,emergencyLevel,taskType)) {
			//������
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
	 * ����ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String taskId = getParameter("taskId",request);//����ID
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadTaskBean(taskId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			OATaskBean taskBean = (OATaskBean)rs.retVal;
			request.setAttribute("taskBean",taskBean);
			
			//��ѯ��ǰ�û���ѡ�����Ŀ
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
	 * ���µ����ֶ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateSingleField(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String fieldName = getParameter("fieldName",request);//�ֶ���
		String fieldVal = getParameter("fieldVal",request);//�ֶ�ֵ
		String taskId = getParameter("taskId",request);//��ĿID
		String detailHeadEnter = getParameter("detailHeadEnter",request);//��Ҫ��������ҳ��ͷ�������޸ĸ�����
		LoginBean loginBean = getLoginBean(request);
		
		OATaskBean taskBean = (OATaskBean)mgt.loadTaskBean(taskId).retVal; 
		String reExecutor =taskBean.getExecutor();//���ԭ�еĸ�����
		String content = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"��";//��ŷ���������Ϣ
		
		if("executor".equals(fieldName)){
			content = "�����ˡ�"+GlobalsTool.getEmpFullNameByUserId(taskBean.getExecutor())+"��";	
			taskBean.setExecutor(fieldVal);//������
		}else if("surveyor".equals(fieldName)){
			content = "�����ˡ�"+GlobalsTool.getEmpFullNameByUserId(taskBean.getSurveyor())+"��";	
			taskBean.setSurveyor(fieldVal);//������
		}else if("beginTime".equals(fieldName)){
			content = "��ʼʱ�䡾"+taskBean.getBeginTime()+"��";
			taskBean.setBeginTime(fieldVal);
		}else if("endTime".equals(fieldName)){
			content = "��ֹʱ�䡾"+taskBean.getEndTime()+"��";	
			taskBean.setEndTime(fieldVal);
		}else if("title".equals(fieldName)){
			content = "������⡾"+taskBean.getTitle()+"��";	
			taskBean.setTitle(fieldVal);
		}else if("remark".equals(fieldName)){
			if(taskBean.getRemark()==null || "".equals(taskBean.getRemark())){
				content = "�����������ա�";	
			}else{
				content = "����������"+taskBean.getRemark()+"��";	
			}
			taskBean.setRemark(fieldVal);
		}else if("schedule".equals(fieldName)){
			taskBean.setSchedule(fieldVal);
		}
		
		if("executor".equals(fieldName) || "surveyor".equals(fieldName)){
			content +="�޸�Ϊ��"+GlobalsTool.getEmpFullNameByUserId(fieldVal)+"��";
		}else{
			content +="�޸�Ϊ��"+fieldVal+"��";
		}
		if(this.updateTask(loginBean.getId(), taskId, taskBean.getTitle(), taskBean.getRemark(), taskBean.getStatus(), taskBean.getExecutor(), taskBean.getBeginTime(), taskBean.getEndTime(), taskBean.getSurveyor(), taskBean.getItemId(), taskBean.getSchedule(),taskBean.getClientId(),taskBean.getParticipant(),taskBean.getAffix(),taskBean.getEmergencyLevel(),taskBean.getTaskType())){

			String msg = "success"; 
			if("executor".equals(fieldName) || "surveyor".equals(fieldName)){
				//�����޸ĸ����ˣ�����ʱ���ҳ��
				msg = "<span class='sFinish showAppoint'>"+GlobalsTool.getEmpFullNameByUserId(fieldVal)+" "+taskBean.getEndTime()+"</span>";
				
				String statusVal = "2";
				if(taskBean.getSurveyor() !=null && !"".equals(taskBean.getSurveyor())){
					//����������,״̬Ϊ3
					statusVal = "3";
				}
				
				if(reExecutor==null || "".equals(reExecutor)){
					//��ʾԭ����δָ��,������ɰ�ť
					msg +="<span class='sFinish updTaskStatus' status='"+statusVal+"'>���</span>";
				}
			}
			
			if(!"schedule".equals(fieldName)){
				//Ĭ���޸ĵ����ֶη�һ������
				new DiscussAction().add(loginBean.getId(), null, null, "OATaskLog", taskId, content, null,null,1);
			}
			request.setAttribute("msg",msg);
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * ɾ��
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
	 * ɾ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String taskId = getParameter("taskId",request);//����ID
		Result rs = mgt.delTask(taskId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{  
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ����
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
				//�ж�����ʱ���Ƿ񳬹���ǰ����ɾ��
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
			
			//��ѯ������ĿBEAN
			if(taskBean.getItemId()!=null && !"".equals(taskBean.getItemId())){
				 rs = itemMgt.loadOAItem(taskBean.getItemId());
				 OAItemsBean itemBean = (OAItemsBean)rs.retVal;
				 request.setAttribute("itemBean",itemBean);
			}
			
			//��ѯ������
			if(taskBean.getTaskId()!=null && !"".equals(taskBean.getTaskId())){
				 rs = mgt.loadTaskBean(taskBean.getTaskId());
				 OATaskBean mainTaskBean = (OATaskBean)rs.retVal;
				 request.setAttribute("mainTaskBean",mainTaskBean);
			}
			
			//�����ͻ�
			request.setAttribute("clientName",mgt.findClientNameById(taskBean.getClientId()));
			
			rs = mgt.queryChildTasksByTaskId(taskId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> taskBeanList = (ArrayList<Object>)rs.retVal;
				if(taskBeanList!=null && taskBeanList.size()>0){
					
					ArrayList<Object> finishTaskList = new ArrayList<Object>();//������������list
					ArrayList<Object> runTaskList = new ArrayList<Object>();//��Ž���������list
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
			
			//��������ʱ��
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
			/*�鿴�Ƿ��ղع�����*/
			Result isExist = new AttentionMgt().isAttention(getLoginBean(request).getId(), taskId, "OATask");
			if(isExist.retCode == ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attentionCard", "attentionCard");
			}
			
			//��ȡ����ְԱ��Ϣ,����textBox�ؼ�
			ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
			request.setAttribute("textBoxValues",gson.toJson(list));
			
			request.setAttribute("loginBean",getLoginBean(request));
			return getForward(request, mapping, "detail");
		}else{
			 //���ʧ��
			EchoMessage.error().add(getMessage(request, "bbs.forum.not.find"))
					.setAlertRequest(request);
            return getForward(request, mapping, "alert");
			
		}
		
		
		
		/*
		//��ȡ������ӵ����ڽ�ֹʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		LinkedHashMap<String,String> weekMap = new LinkedHashMap<String, String>();
		String defWeekName = "��";
		int weekIndex = 0;
		for(int i = 0;i<7;i++){
			weekIndex = today.get(Calendar.DAY_OF_WEEK)-1;
			weekMap.put(defWeekName+GlobalsTool.getWeekCN(weekIndex),sdf.format(today.getTime()));
			today.add(Calendar.DATE,1);
			if(weekIndex==0){
				defWeekName = "��";
			}
		}
		request.setAttribute("weekMap",weekMap);
		*/
	}

	/**
	 * �ı�״̬
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String taskId = getParameter("taskId",request);//����ID
		String status = getParameter("status",request);//״ֵ̬ 1=ִ���� 2.��� 3.������
		String feedbackContent = getParameter("feedbackContent",request);//��������
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadTaskBean(taskId);
		OATaskBean taskBean = (OATaskBean)rs.retVal;
		
		if(this.updateTask(loginBean.getId(), taskId, taskBean.getTitle(), taskBean.getRemark(), status, taskBean.getExecutor(), taskBean.getBeginTime(), taskBean.getEndTime(), taskBean.getSurveyor(), taskBean.getItemId(),taskBean.getSchedule(),taskBean.getClientId(),taskBean.getParticipant(),taskBean.getAffix(),taskBean.getEmergencyLevel(),taskBean.getTaskType())){
			
			//���۲���
			String content = loginBean.getEmpFullName()+"��"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"��������Ϊ";
			if("2".equals(taskBean.getStatus())){
				if("1".equals(status)){
					content +="����";
				}
			}else if("3".equals(taskBean.getStatus())){
				if("1".equals(status)){
					content +="�˻�";	
				}else{
					content +="ͨ��";
				}
			}else{
				content +="���";
			}
			content +="���������:"+feedbackContent;
			new DiscussAction().add(loginBean.getId(), null, null, "OATaskLog", taskBean.getId(), content, null,null,1);
			
			
			if("3".equals(status)){
				request.setAttribute("msg","3");
			}else if("1".equals(status)){
				request.setAttribute("msg","1");
			}else{
				//ƴ���������Ϣ,�����ҳ��
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
							msgInfo+="<span class='sFinish'>"+logCount+"������</span>";
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
	 * �������
	 * @param userId	��¼��ID
	 * @param id		����ID����Ϊ�գ�Ҳ�������洫����
	 * @param title		����
	 * @param remark	����
	 * @param executor	������
	 * @param beginTime	��ʼʱ��
	 * @param endTime	����ʱ��
	 * @param surveyor	������
	 * @param itemId	������ĿID
	 * @param parentTaskId	����ID
	 * @param schedule	�������
	 * @param clientId	�ͻ�Id
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
		
		//�����ճ�
		new OACalendaMgt().outAddCalendar(taskBean.getExecutor(), title, "�ҵ�����", beginTime, endTime, id, "","1","0",clientId);
		
		//������Ϣ
		sendInviteMessage(userId, participant, title, id);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * ���������������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String title = request.getParameter("title");//����
		String remark = request.getParameter("remark");//��������
		String executor = request.getParameter("executor");//������
		String beginTime = getParameter("beginTime",request);//��ʼʱ��
		String endTime = getParameter("endTime",request);//����ʱ��
		String itemId = getParameter("itemId",request);//��ĿID
		String surveyor = getParameter("surveyor",request);//������
		String taskId = getParameter("taskId",request);//����ID
		String schedule = getParameter("schedule",request);//�������
		String clientId = getParameter("clientId",request);//�ͻ�ID
		String participant = getParameter("participant",request);//������Ա
		String affix = getParameter("affix",request);//����
		String emergencyLevel = getParameter("emergencyLevel",request);//�����̶�
		String taskType = getParameter("oaTaskType",request);//�������
		
		//String isCatalog = getParameter("isCatalog",request);//�Ƿ����Ŀ¼,1��ʾ������
				
		LoginBean loginBean = getLoginBean(request);
		String userId = loginBean.getId();
		String id = IDGenerater.getId();
		
		if(addTask(userId, id, title, remark, executor, beginTime, endTime, surveyor, itemId, taskId,schedule,clientId,participant,affix,emergencyLevel,taskType)){
			
			//������
			if(affix!=null && !"".equals(affix)){
				for(String str :affix.split(";")){
					FileHandler.copy("OATask", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			
			//ƴ��������Ϣ��������Ŀ����-����tab������չʾ��Ϣ
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
			
			//ָ�����񣬷�����Ϣ(����:������!=������)
			if(executor!=null && !"".equals(executor) && !userId.equals(executor)){
				//������߷���֪ͨ��Ϣ
				String adviceTitle = loginBean.getEmpFullName()+"ָ��������"+title+"������";
				String url = "/OATaskAction.do?operation=5&taskId="+id;
				String content = "<a href=\"javascript:mdiwin('" + url + "','"+title+"')\">"+adviceTitle+"</a>";//����
				new AdviceMgt().add(loginBean.getId(), adviceTitle, content, executor, id, "OATaskPoint");
			}
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ��Ŀ����ҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detailByItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String itemId = getParameter("itemId",request);//��ĿID;
		
		if(itemId!=null && !"".equals(itemId)){
			Result rs = mgt.queryTasksByItemId(itemId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> taskBeanList = (ArrayList<Object>)rs.retVal;
				
				if(taskBeanList!=null && taskBeanList.size()>0){
					ArrayList<Object> finishTaskList = new ArrayList<Object>();//������������list
					ArrayList<Object> runTaskList = new ArrayList<Object>();//��Ž���������list
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
	 * ��ѯҳ��
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
		String isTHTask = getParameter("isTHTask",request);//�Ƿ����컪��ҳ��
		if(clientId!=null && !"".equals(clientId)){
			//�ͻ��б�����ҳ�������࣬�����������ͻ���ID
			oaTaskSearchform.setSearchClientId(clientId);
		}
		LoginBean loginBean = getLoginBean(request);
		StringBuilder condition = new StringBuilder();//��ѯ����
		StringBuilder countCondition = new StringBuilder();//������ѯ����
		
		String crmTaskEnter = getParameter("crmTaskEnter", request);//true ��ʾCRM�������
		
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
        
		//tabͷ����ѯ
        String tabSelectName = oaTaskSearchform.getTabSelectName();
		if(tabSelectName==null || "".equals(tabSelectName)){
			//Ĭ��ȡ�Ҹ����
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
		
		//����ģ����ѯ�Ͳ���״̬��
		if(oaTaskSearchform.getHasSearchCondition()==null || "".equals(oaTaskSearchform.getHasSearchCondition())){
			//״̬��ѯ
			if(oaTaskSearchform.getSearchStatus()==null || "".equals(oaTaskSearchform.getSearchStatus())){
				//Ĭ����״̬==1  Ĭ�ϲ�ѯִ����
				oaTaskSearchform.setSearchStatus("1");
			}
			
			condition.append(" and status = '").append(oaTaskSearchform.getSearchStatus()).append("' ");
			countCondition.append(" and status = '").append(oaTaskSearchform.getSearchStatus()).append("' ");
		}else{
			oaTaskSearchform.setSearchStatus("");
		}
		
		//���
		if(oaTaskSearchform.getSearchTaskOrder()!=null && !"".equals(oaTaskSearchform.getSearchTaskOrder())){
			condition.append(" and taskOrder like '%").append(oaTaskSearchform.getSearchTaskOrder()).append("%' ");
			countCondition.append(" and taskOrder like '%").append(oaTaskSearchform.getSearchTaskOrder()).append("%' ");
		}
		
		//�ؼ���
		if(oaTaskSearchform.getSearchKeyWord()!=null && !"".equals(oaTaskSearchform.getSearchKeyWord())){
			condition.append(" and (title like '%").append(oaTaskSearchform.getSearchKeyWord()).append("%' or remark like '%").append(oaTaskSearchform.getSearchKeyWord()).append("%') ");
			countCondition.append(" and (title like '%").append(oaTaskSearchform.getSearchKeyWord()).append("%' or remark like '%").append(oaTaskSearchform.getSearchKeyWord()).append("%') ");
		}
		
		//ʱ���ѯ
		String beginStartTime = oaTaskSearchform.getSearchBeginStartTime();
		String beginOverTime = oaTaskSearchform.getSearchBeginOverTime();
		String endStartTime = oaTaskSearchform.getSearchEndStartTime();
		String endOverTime = oaTaskSearchform.getSearchEndOverTime();
		
		//����ʼʱ��
		if(beginStartTime!=null && !"".equals(beginStartTime)){
			condition.append(" and beginTime >= '").append(beginStartTime).append("' ");
			countCondition.append(" and beginTime >= '").append(beginStartTime).append("' ");
		}
		
		if(beginOverTime!=null && !"".equals(beginOverTime)){
			condition.append(" and beginTime <= '").append(beginOverTime).append("' ");
			countCondition.append(" and beginTime <= '").append(beginOverTime).append("' ");
		}
		
		//�������ʱ��
		if(endStartTime!=null && !"".equals(endStartTime)){
			condition.append(" and endTime >= '").append(endStartTime).append("' ");
			countCondition.append(" and endTime >= '").append(endStartTime).append("' ");
		}
		
		if(endOverTime!=null && !"".equals(endOverTime)){
			condition.append(" and endTime <= '").append(endOverTime).append("' ");
			countCondition.append(" and endTime <= '").append(endOverTime).append("' ");
		}
		
		//������
		if(oaTaskSearchform.getSearchCreateBy()!=null && !"".equals(oaTaskSearchform.getSearchCreateBy())){
			condition.append(" and createBy = '").append(oaTaskSearchform.getSearchCreateBy()).append("' ");
			countCondition.append(" and createBy = '").append(oaTaskSearchform.getSearchCreateBy()).append("' ");
		}
		
		//������
		if(oaTaskSearchform.getSearchExecutor()!=null && !"".equals(oaTaskSearchform.getSearchExecutor())){
			condition.append(" and executor = '").append(oaTaskSearchform.getSearchExecutor()).append("' ");
			countCondition.append(" and executor = '").append(oaTaskSearchform.getSearchExecutor()).append("' ");
		}
		
		//������
		if(oaTaskSearchform.getSearchSurveyor()!=null && !"".equals(oaTaskSearchform.getSearchSurveyor())){
			condition.append(" and surveyor = '").append(oaTaskSearchform.getSearchSurveyor()).append("' ");
			countCondition.append(" and surveyor = '").append(oaTaskSearchform.getSearchSurveyor()).append("' ");
		}
		
		//�����̶�
		if(oaTaskSearchform.getSearchEmergencyLevel()!=null && !"".equals(oaTaskSearchform.getSearchEmergencyLevel())){
			condition.append(" and emergencyLevel = '").append(oaTaskSearchform.getSearchEmergencyLevel()).append("' ");
			countCondition.append(" and emergencyLevel = '").append(oaTaskSearchform.getSearchEmergencyLevel()).append("' ");
		}
		
		//�������
		if(oaTaskSearchform.getSearchOaTaskType()!=null && !"".equals(oaTaskSearchform.getSearchOaTaskType())){
			condition.append(" and taskType = '").append(oaTaskSearchform.getSearchOaTaskType()).append("' ");
			countCondition.append(" and taskType = '").append(oaTaskSearchform.getSearchOaTaskType()).append("' ");
		}
		
		//������
		if(oaTaskSearchform.getSearchSurveyor()!=null && !"".equals(oaTaskSearchform.getSearchSurveyor())){
			condition.append(" and surveyor = '").append(oaTaskSearchform.getSearchSurveyor()).append("' ");
			countCondition.append(" and surveyor = '").append(oaTaskSearchform.getSearchSurveyor()).append("' ");
		}
		
		//�ͻ�
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

			//��ѯ��ǰ�û���ѡ�����Ŀ
			ArrayList<Object> itemList = itemMgt.queryItemsByUserId(userId,false);
			request.setAttribute("itemList",itemList);
			
			//��ѯtabͷ������
			HashMap<String,String> countMap = mgt.getRunTabCount(loginBean,countCondition.toString());
			request.setAttribute("countMap",countMap);
			
			//��ȡ����ְԱ��Ϣ,����textBox�ؼ�
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
	 * ��ȡ��������Ϣ
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
		String taskTitle = getParameter("taskTitle",request);//��Ŀ����
		String employeeIds = getParameter("employeeIds",request);
		LoginBean loginBean = getLoginBean(request);
		//���²�����Ա

		Result rs = mgt.updateParticipants(employeeIds, loginBean, taskId);
		
		if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
			//��װ�û�ID����ͷ����Ϣ
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
				String msg = "";//���ڴ��ƴװ�Ĳ�������Ϣ
				ArrayList<Object> rsList = (ArrayList<Object>)rs.retVal;
				if(rsList!=null && rsList.size()>0){
					for(Object obj : rsList){
						String id = GlobalsTool.get(obj,0)+"";//ְԱID
						if(id!=null && !"".equals(id)){
							String empFullName = GlobalsTool.get(obj,1)+"";//ְԱȫ��
							String photo = GlobalsTool.checkEmployeePhoto("48",String.valueOf(GlobalsTool.get(obj,0)));//ͷ����Ϣ
							msg += "<li empId='"+id+"' class='mesOnline'><a href='javascript:top.msgCommunicate(\""+id+"\",\""+empFullName+"\")' class='lf Image'><img src='"+photo+"'/></a> <i class='lf'><b>"+empFullName+"</b></i> <b class='icons b-del-t'></b></li>";
						}
					}
					request.setAttribute("msg",msg);
					
					//������߷���֪ͨ��Ϣ
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
	 * �����ϴ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward uploadAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			
		String taskId = getParameter("taskId",request);//����id
		String uploadStrs = getParameter("uploadStrs",request);//������Ϣ
		LoginBean loginBean = getLoginBean(request);
		String discussContent = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"�ϴ��˸���:";//��������
		if(uploadStrs!=null && !"".equals(uploadStrs)){
			int handleType = FileHandler.TYPE_AFFIX;//����
			
			for(String str :uploadStrs.split(";")){
				discussContent += "<a href='/ReadFile?fileName="+str+"&realName="+str+"&tempFile=false&type=AFFIX&tableName=OATask'>"+str+"</a>��";
				FileHandler.copy("OATask", FileHandler.TYPE_AFFIX, str, str);
				FileHandler.deleteTemp(str);
			}
			if(discussContent.endsWith("��")){
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
	 * �첽ɾ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String taskId = getParameter("taskId",request);//����ID
		String fileName = getParameter("fileName",request);//ɾ�����ļ���
		String tempFile = getParameter("tempFile",request);//�Ƿ���ʱ�ļ�,true:��,false:��
		
		if (fileName != null && fileName.length() != 0) {
			
			String sql = "UPDATE OATask SET  affix = replace(affix,'"+fileName+";','') where id = '"+taskId+"'";
			ArrayList param = new ArrayList();
			Result rs = mgt.operationSql(sql, param);
			
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				if("false".equals(tempFile)){
					//ɾ����ʽ��
					FileHandler.delete("OATask",FileHandler.TYPE_AFFIX, fileName);
				}else{
					//ɾ����ʱ�ļ�
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
	 * �������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addWran(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyId = getParameter("keyId",request);//����ID
		String alterDay = getParameter("alterDay",request);//��������
		int alterHour = getParameterInt("alterHour",request);//����Сʱ
		int alterMinutes = getParameterInt("alterMinutes",request);//���ѷ���
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadTaskBean(keyId);
		OATaskBean taskBean = (OATaskBean)rs.retVal;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AlertBean alertBean = new AlertBean();        

		//�Ȱ��Ѿ����ѵ�ɾ��
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
			context = "����"+taskBean.getTitle()+"�����ѣ���鿴��";
		}
		
		alertBean.setAlertContent(context);
		String url = "/OATaskAction.do?operation=5&taskId="+keyId;
		String content = "<a href=\"javascript:mdiwin('" + url + "','"+taskBean.getTitle()+"')\">"+context+"</a>";//����
		alertBean.setAlertUrl(content);                   			
		
		rs = mgt.addWarn(alertBean,keyId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","<span title='����ʱ�䣺"+nextAlertTime+"'>��������</span><b class='delAlert' actionName='OATaskAction' keyId='"+keyId+"'></b>");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * ɾ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delParticipant(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String employeeId = getParameter("employeeId",request);//ְԱID
		String participants = getParameter("participants",request);//�������Ĳ�����
		String taskId = getParameter("taskId",request);//����id
		
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
			 return getForward(req, mapping, "indexPage");//���	
		}

		return null;
	}
	
	/**
	 * ������������֪ͨ
	 * @param loginBean
	 * @param employeeIds
	 * @param taskTitle
	 * @param taskId
	 */
	public static void sendInviteMessage(String userId,String employeeIds,String taskTitle,String taskId){
		//������߷���֪ͨ��Ϣ
		String title = GlobalsTool.getEmpFullNameByUserId(userId)+"��������������"+taskTitle+"��";
		String url = "/OATaskAction.do?operation=5&taskId="+taskId;
		String content = "<a href=\"javascript:mdiwin('" + url + "','"+taskTitle+"')\">"+title+"</a>";//����
		employeeIds = new DiscussAction().filterReceiveIdByLoginId(employeeIds, userId);//���˰����ҵ�֪ͨ��
		new AdviceMgt().add(userId, title, content, employeeIds, taskId, "OATask");
		
	}
}
