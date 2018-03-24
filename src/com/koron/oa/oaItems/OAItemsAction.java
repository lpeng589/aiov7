package com.koron.oa.oaItems;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.OAItemsBean;
import com.koron.oa.discuss.DiscussAction;
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
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;

/**
 * 
 * <p>Title:�ҵ���Ŀ</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/2
 * @Copyright: �����п���������޹�˾
 * @Author ��ܿ�
 */
public class OAItemsAction extends BaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	OAItemsMgt mgt = new OAItemsMgt();
	AdviceMgt adviceMgt = new AdviceMgt();
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
						forward = addItem(mapping, form, request, response);
					}
				break;
			case OperationConst.OP_UPDATE:
				if("changeStatus".equals(type)){
					forward = changeStatus(mapping, form, request, response);//�첽�ı�״̬
				}else if("participantsInfo".equals(type)){
					forward = updateParticipantsInfoInfo(mapping, form, request, response);
				}else if("updateSingle".equals(type)){
					forward = updateSingleField(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_DETAIL:	
				forward = detailItem(mapping, form, request, response);
				break;
			case OperationConst.OP_DELETE:	
				if("affix".equals(type)){
					forward = delAffix(mapping, form, request, response);
				}else if("participant".equals(type)){
					forward = delParticipant(mapping, form, request, response);
				}else if("delAlert".equals(type)){
					forward = delAlert(mapping, form, request, response);
				}else{
					forward = delItem(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_UPLOAD:	
				forward = uploadAffix(mapping, form, request, response);
				break;
			case OperationConst.OP_QUERY:	
					forward = query(mapping, form, request, response);
				break;
			default:
				forward = query(mapping, form, request, response);//��ҳ��ѯ
				break;
		}
		return forward;
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
		String itemId = getParameter("itemId",request);//��ĿID
		
		Result rs = mgt.delParticipant(itemId, employeeId,participants);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
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
		String itemId = getParameter("itemId",request);//��ĿID
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadOAItem(itemId);
		OAItemsBean itemBean = (OAItemsBean)rs.retVal;
		
		String sql = "UPDATE OAItems SET "+fieldName+" = ?,lastUpdateBy=?,lastUpdateTime=? WHERE id = ?";
		ArrayList param = new ArrayList();
		param.add(fieldVal);
		param.add(loginBean.getId());
		param.add(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		param.add(itemId);
		
		rs = mgt.operationSql(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			if(!"schedule".equals(fieldName)){
				String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
				String content = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"��";
				if("beginTime".equals(fieldName)){
					content +="��ʼʱ�䡾"+itemBean.getBeginTime()+"��";
				}else if("endTime".equals(fieldName)){
					content +="��ֹʱ�䡾"+itemBean.getEndTime()+"��";
				}else if("remark".equals(fieldName)){
					content +="��������";
					
					if(itemBean.getRemark()==null || "".equals(itemBean.getRemark())){
						content +="���ա�";
					}else{
						content +="��"+itemBean.getRemark()+"��";
					}
				}else{
					content +="���⡾"+itemBean.getTitle()+"��";
				}
				content +="�޸�Ϊ��"+fieldVal+"��";
				new DiscussAction().add(loginBean.getId(), null, null, "OAItemsLog", itemId, content, null,null,1);
			}
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
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
		//String isSetTime = getParameter("isSetTime",request);//�Ƿ��Զ���ʱ��
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadOAItem(keyId);
		OAItemsBean itemBean = (OAItemsBean)rs.retVal;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AlertBean alertBean = new AlertBean();        

		//�Ȱ��Ѿ����ѵ�ɾ��
		String delSql = "DELETE FROM tblAlert WHERE relationId = ?";
		ArrayList param = new ArrayList();
		param.add(keyId);
		mgt.operationSql(delSql, param); 
		
		
		String nextAlertTime = alterDay+" "+alterHour+":"+alterMinutes+":00";
		
		String alertContent = "��Ŀ���ѣ���鿴";
		if(itemBean!=null && !"".equals(itemBean.getTitle())){
			alertContent = "��Ŀ��"+itemBean.getTitle()+"�����ѣ���鿴��";
		}
		
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
		alertBean.setLoopType("day");
		alertBean.setLoopTime(1);
		String popedomUserIds = loginBean.getId();                   		
		alertBean.setPopedomUserIds(popedomUserIds);
		alertBean.setAlertContent(alertContent);
		
		String url = "/OAItemsAction.do?operation=5&itemId="+keyId;
		String content = "<a href=\"javascript:mdiwin('" + url + "','"+itemBean.getTitle()+"')\">"+alertContent+"</a>";//����
		alertBean.setAlertUrl(content);                   			
		
		rs = mgt.addWarn(alertBean,keyId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","<span title='����ʱ�䣺"+nextAlertTime+"'>��������</span>");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
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
		String itemId = getParameter("itemId",request);
		String itemTitle = getParameter("itemTitle",request);//��Ŀ����
		String employeeIds = getParameter("employeeIds",request);
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.updateParticipants(employeeIds, loginBean, itemId);
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
			
			String sql = "SELECT id,empFullName,photo FROM tblEmployee WHERE id in("+keyIds+")";
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
							msg += "<li empId='"+id+"' class='mesOnline'><a href='javascript:top.msgCommunicate(\""+id+"\",\""+empFullName+"\")' class='lf Image'><img src='"+photo+"' /></a> <i class='lf'><b>"+empFullName+"</b></i><b class='icons b-del-t'></b></li>";
						}
					}
					request.setAttribute("msg",msg);
					
					//������߷���֪ͨ��Ϣ
					String title = loginBean.getEmpFullName()+"������������Ŀ��"+itemTitle+"��";
					String url = "/OAItemsAction.do?operation=5&itemId="+itemId;
					String content = "<a href=\"javascript:mdiwin('" + url + "','"+itemTitle+"')\">"+title+"</a>";//����
					
					employeeIds = new DiscussAction().filterReceiveIdByLoginId(employeeIds, loginBean.getId());//���˰����ҵ�֪ͨ��
					adviceMgt.add(loginBean.getId(), title, content, employeeIds, itemId, "OAItemsParticipant");
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
	 * �첽ɾ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	private ActionForward delAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String itemId = getParameter("itemId",request);//��ĿID
		String fileName = getParameter("fileName",request);//ɾ�����ļ���
		String tempFile = getParameter("tempFile",request);//�Ƿ���ʱ�ļ�,true:��,false:��
		
		if (fileName != null && fileName.length() != 0) {
			
			String sql = "UPDATE oaitems SET  affix = replace(affix,'"+fileName+";','') where id = '"+itemId+"'";
			ArrayList param = new ArrayList();
			Result rs = mgt.operationSql(sql, param);
			
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				if("false".equals(tempFile)){
					//ɾ����ʽ��
					FileHandler.delete("OAItems",FileHandler.TYPE_AFFIX, fileName);
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
	 * �����ϴ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward uploadAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			
		String itemId = getParameter("itemId",request);//��ĿID
		String uploadStrs = getParameter("uploadStrs",request);//ͼƬ��Ϣ
		LoginBean loginBean = getLoginBean(request);
		String discussContent = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"�ϴ��˸���:";//��������
		if(uploadStrs!=null && !"".equals(uploadStrs)){
			int handleType = FileHandler.TYPE_AFFIX;//����
			
			for(String str :uploadStrs.split(";")){
				discussContent += "<a href='/ReadFile?fileName="+str+"&realName="+str+"&tempFile=false&type=AFFIX&tableName=OAItems'>"+str+"</a>��";	
				FileHandler.copy("OAItems", FileHandler.TYPE_AFFIX, str, str);
				FileHandler.deleteTemp(str);
			}
			
			if(discussContent.endsWith("��")){
				discussContent = discussContent.substring(0,discussContent.length()-1);
			}
		}
		
		Result rs = mgt.loadOAItem(itemId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String newAffix = "";
			OAItemsBean itemBean = (OAItemsBean)rs.retVal;
			if(itemBean.getAffix() == null || "".equals(itemBean.getAffix())){
				newAffix = uploadStrs;
			}else{
				newAffix = itemBean.getAffix()+uploadStrs;
			}
			
			itemBean.setAffix(newAffix);
			rs = mgt.updateOAItem(itemBean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
			
			new DiscussAction().add(getLoginBean(request).getId(), null, null, "OAItemsLog", itemId, discussContent, null,null,1);
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ����ҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detailItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String itemId = getParameter("itemId",request);//��ĿID
		Result rs = mgt.loadOAItem(itemId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			OAItemsBean itemBean = (OAItemsBean)rs.retVal;
			
			if(itemBean.getNextAlertTime()!=null && !"".equals(itemBean.getNextAlertTime())){
				//�ж�����ʱ���Ƿ񳬹���ǰ����ɾ��
				String alertTime = itemBean.getNextAlertTime();
				Date alertDate = new Date();
				try {
					alertDate = BaseDateFormat.parse(itemBean.getNextAlertTime(), BaseDateFormat.yyyyMMddHHmmss);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if(alertDate.before(new Date())){
					rs = mgt.delAlert(itemBean.getId());
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						itemBean.setNextAlertTime("");
					}
				}
			}
			
			//��������ʱ��
			if(itemBean.getNextAlertTime()!=null && !"".equals(itemBean.getNextAlertTime())){
				String alertDate = "";
				int alertHour = 0;
				int alterMinutes = 0;
				alertDate = itemBean.getNextAlertTime().substring(0,10);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Calendar ca = Calendar.getInstance();
				try {
					ca.setTime(sdf.parse(itemBean.getNextAlertTime()));
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
			Result isExist = new AttentionMgt().isAttention(getLoginBean(request).getId(), itemId, "OAItems");
			if(isExist.retCode == ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attentionCard", "attentionCard");
			}
			
			//��ȡ����ְԱ��Ϣ,����textBox�ؼ�
			ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
			request.setAttribute("textBoxValues",gson.toJson(list));
			
			
			request.setAttribute("clientName",mgt.findClientNameById(itemBean.getClientId()));
			request.setAttribute("itemBean",itemBean);
			request.setAttribute("loginBean", getLoginBean(request));
			return getForward(request, mapping, "detail");
		}else{
			request.setAttribute("noAlert", "true");
			
            EchoMessage.error().add("��ѯ�������!").setAlertRequest(request);
            return getForward(request, mapping, "alert");
		}
		
	}

	/**
	 * ɾ����Ŀ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String itemId = getParameter("itemId",request);//��ĿID
		Result rs = mgt.delItem(itemId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{  
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * �첽�ı�״̬
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String id = getParameter("id",request);//��ĿID
		String status = getParameter("status",request);//�ı��״ֵ̬
		String feedbackContent = getParameter("feedbackContent",request);//�������
		
		LoginBean loginBean = getLoginBean(request);
		Result rs = mgt.loadOAItem(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			OAItemsBean itemBean = (OAItemsBean)rs.retVal;
			itemBean.setStatus(status);
			if("2".equals(status)){
				//״̬��ɼ������ʱ�䣬
				itemBean.setFinishTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				itemBean.setSchedule("100");
			}else{
				itemBean.setFinishTime("");
				itemBean.setSchedule("0");
			}
			rs = mgt.updateOAItem(itemBean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//���۲���
				String content = loginBean.getEmpFullName()+"��"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"����Ŀ���Ϊ";
				if("2".equals(status)){
					content+="���";
				}else{
					content+="����";
				}
				content +="���������:"+feedbackContent;
				new DiscussAction().add(loginBean.getId(), null, null, "OAItemsLog", itemBean.getId(), content, null,null,1);
				
				request.setAttribute("msg",status);
			}else{
				request.setAttribute("msg","error");
			}
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ��ѯ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		OAItemsSearchForm oaItemsform = (OAItemsSearchForm) form;
		LoginBean loginBean = getLoginBean(request);
		request.setAttribute("loginBean", loginBean);
		StringBuilder condition = new StringBuilder();
		String isTHItems = getParameter("isTHItems",request);//�Ƿ����컪��ҳ��
		
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	pageSize = 4;
        }
        
        String userId = loginBean.getId();

		//tabͷ����ѯ
        String tabSelectName = oaItemsform.getTabSelectName();
		if(tabSelectName==null || "".equals(tabSelectName)){
			//Ĭ��ȡ�Ҹ����
			condition.append(" and executor= '").append(userId).append("' ");
			oaItemsform.setTabSelectName("executor");
		}else{
			condition.append(" and ");	
			if("participant".equals(tabSelectName)){
				condition.append(" ','+ ").append(tabSelectName).append(" like '%,").append(userId).append(",%' ");
			}else{
				condition.append(tabSelectName).append(" = '").append(userId).append("' ");
			}
		}
        
		
		//����ģ����ѯ�Ͳ���״̬��
		if(oaItemsform.getHasSearchCondition()==null || "".equals(oaItemsform.getHasSearchCondition())){
			//״̬��ѯ
			if(oaItemsform.getSearchStatus()==null || "".equals(oaItemsform.getSearchStatus()) || "1".equals(oaItemsform.getSearchStatus())){
				//Ĭ����״̬==1  Ĭ�ϲ�ѯִ����
				condition.append(" and status = '1' ");
				oaItemsform.setSearchStatus("1");
			}else if("all".equals(oaItemsform.getSearchStatus())){
				//ȫ��
			}else{
				//���������
				condition.append(" and status = '").append(oaItemsform.getSearchStatus()).append("' ");
			}
		}else{
			oaItemsform.setSearchStatus("");
		}
		
		
		//���
		if(oaItemsform.getSearchItemOrder()!=null && !"".equals(oaItemsform.getSearchItemOrder())){
			condition.append(" and itemOrder like '%").append(oaItemsform.getSearchItemOrder()).append("%' ");
		}
		
		
		//�ؼ���
		if(oaItemsform.getSearchKeyWord()!=null && !"".equals(oaItemsform.getSearchKeyWord())){
			condition.append(" and (title like '%").append(oaItemsform.getSearchKeyWord()).append("%' or remark like '%").append(oaItemsform.getSearchKeyWord()).append("%') ");
		}
		
		//ʱ���ѯ
		String beginStartTime = oaItemsform.getSearchBeginStartTime();
		String beginOverTime = oaItemsform.getSearchBeginOverTime();
		String endStartTime = oaItemsform.getSearchEndStartTime();
		String endOverTime = oaItemsform.getSearchEndOverTime();
		
		//����ʼʱ��
		if(beginStartTime!=null && !"".equals(beginStartTime)){
			condition.append(" and beginTime >= '").append(beginStartTime).append("' ");
		}
		
		if(beginOverTime!=null && !"".equals(beginOverTime)){
			condition.append(" and beginTime <= '").append(beginOverTime).append("' ");
		}
		
		//�������ʱ��
		if(endStartTime!=null && !"".equals(endStartTime)){
			condition.append(" and endTime >= '").append(endStartTime).append("' ");
		}
		
		if(endOverTime!=null && !"".equals(endOverTime)){
			condition.append(" and endTime <= '").append(endOverTime).append("' ");
		}
		
		//�ͻ�
		if(oaItemsform.getSearchClientId()!=null && !"".equals(oaItemsform.getSearchClientId())){
			condition.append(" and clientId = '").append(oaItemsform.getSearchClientId()).append("' ");
			request.setAttribute("clientName", mgt.findClientNameById(oaItemsform.getSearchClientId()));
		}
		
		Result rs = mgt.itemsQuery(condition.toString(),pageNo,pageSize);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("itemsList", rs.retVal);
			request.setAttribute("pageBar",pageBar2(rs, request)) ;
			request.setAttribute("oaItemsform",oaItemsform);
			if("true".equals(isTHItems)){
				return getForward(request, mapping, "oaitems");
			}
			return getForward(request, mapping, "query");
		}else{
			request.setAttribute("noAlert", "true");
            EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
            return getForward(request, mapping, "alert");
		}
	}
	
	/**
	 * �����Ŀ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		OAItemsBean itemBean = new OAItemsBean();
		String beginTime = getParameter("beginTime",request);//��ʼʱ��
		String endTime = getParameter("endTime",request);//����ʱ��
		LoginBean loginBean = getLoginBean(request);
		
		
		String title = request.getParameter("title");//����
		String remark = request.getParameter("remark");//��������
		String schedule = request.getParameter("schedule");//��Ŀ����
		String clientId = request.getParameter("clientId");//�ͻ�id
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		String userId = loginBean.getId();
		String participant = userId+",";//Ĭ�ϲ�����
		
		itemBean.setId(IDGenerater.getId());
		itemBean.setTitle(title);
		itemBean.setRemark(remark);
		itemBean.setBeginTime(beginTime);
		itemBean.setEndTime(endTime);
		itemBean.setExecutor(userId);//Ĭ�ϸ������Ǵ�����
		itemBean.setStatus("1");//Ĭ��״̬Ϊִ����
		itemBean.setCreateBy(userId);
		itemBean.setCreateTime(nowTime);
		itemBean.setLastUpdateBy(userId);
		itemBean.setLastUpdateTime(nowTime);
		itemBean.setSchedule(schedule);
		itemBean.setClientId(clientId);
		//itemBean.setParticipant(participant);
		
		Result rs = mgt.addOAItem(itemBean);
		
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
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
	
	
}
