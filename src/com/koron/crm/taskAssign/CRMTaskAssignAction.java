package com.koron.crm.taskAssign;


import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.bean.CRMTaskAssignBean;
import com.koron.crm.brother.CRMBrotherMgt;
import com.koron.crm.client.CRMClientAction;
import com.koron.crm.client.CRMClientMgt;
import com.koron.oa.OACalendar.OACalendaMgt;
import com.koron.oa.OACalendar.OACalendarBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:CRM�������</p> 
 * <p>Description: </p>
 *
 * @Date:Jun 17, 2013
 * @Copyright: �����п���������޹�˾
 * @Author ��ܿ�
 */
public class CRMTaskAssignAction extends MgtBaseAction{
	CRMTaskAssignMgt mgt = new CRMTaskAssignMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// ���ܷ���
		String type = getParameter("type", request);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		
		switch (operation) {
			case OperationConst.OP_ADD_PREPARE:
				forward = addPrepare(mapping, form, request, response);
				break;
			case OperationConst.OP_ADD:
				forward = add(mapping, form, request, response);
				break;
			case OperationConst.OP_UPDATE_PREPARE:
				forward = updatePrepare(mapping, form, request, response);
				break;
			case OperationConst.OP_UPDATE:
				forward = update(mapping, form, request, response);
				break;
			case OperationConst.OP_DELETE:
				forward = del(mapping, form, request, response);
				break;
			
			case OperationConst.OP_DETAIL:
				forward = detail(mapping, form, request, response);
				break;
			
			case OperationConst.OP_QUERY:
				forward = query(mapping, form, request, response);
				break;
			default:
				forward = query(mapping, form, request, response);
				break;	
		}
		return forward;
	}

	private ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] keyIds = getParameters("keyId", request) ;
		
		Result rs = mgt.delTaskAssign(keyIds);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/CRMTaskAssignQueryAction.do").setAlertRequest(request);
		}else{
            EchoMessage.error().add("ɾ��ʧ��").setAlertRequest(request);
		}   
		return getForward(request, mapping, "message") ;
	}

	private ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String clientId = getParameter("clientId",request);
		
		//���пͻ�id��ʾCRM�������
		if(clientId!=null && !"".equals(clientId)){
			String clientName = new CRMClientMgt().getClientNameById(clientId);
			request.setAttribute("clientId",clientId);
			request.setAttribute("clientName",clientName);
		}
		
		return getForward(request, mapping, "add");
	}

	private ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String taskAssignId = getParameter("taskAssignId",request);
		Result rs = mgt.loadTaskAssign(taskAssignId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			CRMTaskAssignBean taskAssignBean = (CRMTaskAssignBean)rs.retVal;
			request.setAttribute("taskAssignBean",taskAssignBean);
			
			String clientName = new CRMClientMgt().getClientNameById(taskAssignBean.getRef_id());
			request.setAttribute("clientName",clientName);
		}
		return getForward(request, mapping, "detal");
	}

	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LoginBean loginBean = getLoginBean(request);
		CRMTaskAssignForm taskAssignForm = (CRMTaskAssignForm)form;
		CRMTaskAssignBean taskAssignBean = new CRMTaskAssignBean();
		
		read(taskAssignForm, taskAssignBean);
		
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		String userId = loginBean.getId();
		taskAssignBean.setLastUpdateBy(userId);
		taskAssignBean.setLastUpdateTime(nowTime);
		Result rs =mgt.updateTaskAssign(taskAssignBean);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
        }
		return getForward(request, mapping, "alert");
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
		String taskAssignId = getParameter("taskAssignId",request);
		Result rs = mgt.loadTaskAssign(taskAssignId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			CRMTaskAssignBean taskAssignBean = (CRMTaskAssignBean)rs.retVal;
			request.setAttribute("taskAssignBean",taskAssignBean);
			
			String clientName = new CRMClientMgt().getClientNameById(taskAssignBean.getRef_id());
			request.setAttribute("clientName",clientName);
		}
		request.setAttribute("isDetail",getParameter("isDetail",request));//true��ʾ�������
		request.setAttribute("loginBean",getLoginBean(request));
		
		
		String finishStatus = getParameter("finishStatus",request);
		if(finishStatus!=null && "true".equals(finishStatus)){
			return getForward(request, mapping, "finishStatus");	
		}
		
		OACalendarBean calendarBean = new CRMBrotherMgt().getCalendarByRelationId(taskAssignId);
		
		request.setAttribute("calendarBean",calendarBean);
		
		return getForward(request, mapping, "update");
	}

	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		LoginBean loginBean = getLoginBean(request);
		CRMTaskAssignForm taskAssignForm = (CRMTaskAssignForm)form;
		CRMTaskAssignBean taskAssignBean = new CRMTaskAssignBean();
		read(taskAssignForm, taskAssignBean);
		
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		String userId = loginBean.getId();
		taskAssignBean.setId(IDGenerater.getId());
		taskAssignBean.setCreateBy(userId);
		taskAssignBean.setCreateTime(nowTime);
		taskAssignBean.setLastUpdateBy(userId);
		taskAssignBean.setLastUpdateTime(nowTime);
		taskAssignBean.setTaskStatus("-1");
		Result rs =mgt.addTaskAssign(taskAssignBean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
			if(!loginBean.getId().equals(taskAssignBean.getUserId())){
				//������֪ͨ��Ϣ
				String title = loginBean.getEmpFullName()+"ָ��������"+taskAssignBean.getContent()+"������";
				String url = "/CRMTaskAssignQueryAction.do?operation=4";
				String content = "<a href=\"javascript:mdiwin('" + url + "','"+taskAssignBean.getContent()+"')\">"+title+"</a>";//����
				new AdviceMgt().add(loginBean.getId(), title, content, taskAssignBean.getUserId(), taskAssignBean.getId(), "CRMTaskAssign");
			}
		}else{
			EchoMessage.error().add(rs.retCode, request).setAlertRequest(request);
        }
		return getForward(request, mapping, "alert");
	}

	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		CRMTaskAssignSearchForm searchForm = (CRMTaskAssignSearchForm)form;
		StringBuilder condition = new StringBuilder();//�������
		LoginBean loginBean = getLoginBean(request);
		
		//��ҳ��Ϣ
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
    		pageSize = 30;
        }
        
        
        
      //�鿴����Ȩ��
        if(!"1".equals(loginBean.getId())){
        	String scopeDeptCode = "";//���˿ͻ����ϱ��沿��Ȩ���ַ���
			/*�鿴ĳ�ֶ�ֵ����*/
			String fieldValueSQL = "" ;
			
			condition.append(" and (CRMTaskAssign.userId  ='").append(loginBean.getId()).append("' ");
			
			//��ȡȨ��·��
			String mopUrl = "/CRMTaskAssignQueryAction.do";
			MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get(mopUrl);
			if(mop!=null){
				ArrayList scopeRight = new CRMClientAction().getAllMopByType(mop, loginBean, MOperation.M_QUERY);
				if (scopeRight != null && scopeRight.size()>0) {
					for (Object o : scopeRight) {
						String strUserIds = "" ;
						String strDeptIds = "" ;
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strUserIds += "'"+strId+"'," ;
							}
							strUserIds = strUserIds.substring(0, strUserIds.length()-1) ;
							condition.append(" or CRMTaskAssign.userId in (").append(strUserIds).append(")");
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								//�Ѳ������д������
								scopeDeptCode += "classcode like '" + strId + "%' or ";
							}
						}
						
						/*
						if(lsb!=null && "6".equals(lsb.getFlag()) && tableName.equals(lsb.getTableName())){
							if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
								if(lsb.getScopeValue().contains(";")){
									String[] scopes = lsb.getScopeValue().split(";") ;
									String scopeSQL = "(" ;
									for(String str : scopes){
										scopeSQL += "'" + str + "'," ; 
									}
									scopeSQL = scopeSQL.substring(0, scopeSQL.length()-1) ;
									scopeSQL += ")" ;
									fieldValueSQL = lsb.getTableName() + "." + lsb.getFieldName() + " in " +scopeSQL ;
								}
							}
						}
						*/
					}
					
					//������˿ͻ�����,�������Ĳ���Ȩ��
					if(scopeDeptCode.endsWith("or ")){
						scopeDeptCode = scopeDeptCode.substring(0,scopeDeptCode.length()-3);
					}
					if(!"".equals(scopeDeptCode)){
						condition.append(" or ((select DepartmentCode from tblEmployee where tblEmployee.id=CRMTaskAssign.userId) in (select classcode from tblDepartment where ").append(scopeDeptCode).append(" ))");
					}

					
				}
			}
			condition.append(")") ;
			if(fieldValueSQL.length()>0){
				condition.append(" and (").append(fieldValueSQL).append(")") ;
			}
		}
        
        
        
        //�ͻ�����
        if(searchForm.getClientName()!=null && !"".equals(searchForm.getClientName())){
        	condition.append(" and CRMClientInfo.clientName like '%").append(searchForm.getClientName()).append("%' ");
        }
        
        //������
        if(searchForm.getSearchUserId()!=null && !"".equals(searchForm.getSearchUserId())){
        	
        	String userIds ="" ;
        	for(String str : searchForm.getSearchUserId().split(",")){
        		if(!"".equals(str)){
        			userIds+="'"+str+"',";
        		}
        	}
        	
        	if(userIds.endsWith(",")){
        		userIds = userIds.substring(0,userIds.length()-1);
        	}
        	if(!"".equals(userIds)){
        		condition.append(" and CRMTaskAssign.userId in (").append(userIds).append(")");
        	}
        }
        
        //״̬
        if(searchForm.getSearchTaskStatus()==null){
        	searchForm.setSearchTaskStatus("-1");
        }
        if(!"all".equals(searchForm.getSearchTaskStatus())){
        	condition.append(" and CRMTaskAssign.taskStatus = '").append(searchForm.getSearchTaskStatus()).append("' ");
        }
        
        //>����ʱ��
        if(searchForm.getSearchStartTime()!=null && !"".equals(searchForm.getSearchStartTime())){
        	condition.append(" and CRMTaskAssign.createTime >= '").append(searchForm.getSearchStartTime()).append("' ");
        }
        
        //<����ʱ��
        if(searchForm.getSearchEndTime()!=null && !"".equals(searchForm.getSearchEndTime())){
        	condition.append(" and CRMTaskAssign.createTime <= '").append(searchForm.getSearchEndTime()).append("' ");
        }
		
        String clientId = getParameter("clientId",request);
        if(clientId!=null && !"".equals(clientId)){
        	condition.append(" and CRMClientInfo.id ='").append(clientId).append("' ");
        	request.setAttribute("clientId",clientId);
        }
        
        
        
        
        
		Result rs = mgt.queryTaskAssign(condition.toString(), pageSize, pageNo);
		
		
		request.setAttribute("searchForm",searchForm);
		request.setAttribute("loginBean",getLoginBean(request));
		request.setAttribute("taskAssignList",rs.retVal);
		request.setAttribute("pageBar",pageBar2(rs, request)) ;
		
		return getForward(request, mapping, "query");
	}

}