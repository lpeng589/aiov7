package com.koron.crm.qa;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.office.meeting.OAMeetingSearchForm;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class CRMQAAction extends MgtBaseAction{
	private CRMQAMgt mgt=new CRMQAMgt();

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/CRMQASearchAction.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删除权限
		request.setAttribute("update", mop.delete()); // 修改权限
		int operation = getOperation(request);
		
		ActionForward forward = null;
		switch(operation){
		// 新增操作
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		//添加之前准备
		case OperationConst.OP_ADD_PREPARE:			
			forward = addPrepare(mapping, form, request, response);
			break;
			//修改之前准备
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;	
		case OperationConst.OP_DETAIL:
			 forward = detail(mapping, form, request, response);
			break;
//		case OperationConst.OP_QUERY:
//			 forward = query(mapping, form, request, response);
//			break;
		default:
			
		     forward = query(mapping, form, request, response);
		break;
		}
		return forward ;
	}

	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id=this.getParameter("id", request);
		if(id != null){
			Result result=mgt.detail(id);
			 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			    request.setAttribute("qa", result.retVal);
			 }
			 if(result.retCode == ErrorCanst.ER_NO_DATA){
				 request.setAttribute("nodate", "nodate");
			 }
		}		
		return getForward(request, mapping, "to_detail");
	}
	
	
	
	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CRMQAForm  qaForm =(CRMQAForm) form;
		String msg="";
		CRMQABean bean =new CRMQABean();
		read(qaForm, bean);
		bean.setId(IDGenerater.getId());
		
		  //处理附件
		 String file=this.getParameter("attachFiles", request)==null?"":this.getParameter("attachFiles", request);
			bean.setAttachment(file);
	     // 需删除的附件
			String delFiles = getParameter("delFiles",request);
			
			// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
			String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
			for (String del : dels) {
				if (del != null && del.length() > 0
						&& bean.getAttachment().indexOf(del) == -1) {
				
					File aFile = new File(BaseEnv.FILESERVERPATH + "/crm/QA/" + del);
					aFile.delete();
				}
			}
		
		
		
		String userId = this.getLoginBean(request).getId();
		bean.setCreateBy(userId);
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			Result result=mgt.add(bean);
			 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {			
					 msg="<script type='text/javascript' >";
					  msg += "parent.addsuccessbox();";
					 msg += "</script>";
					 request.setAttribute("msg", msg);
					 return getForward(request, mapping, "blank");			 
			 }
	
		EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
			.setBackUrl("/CRMQASearchAction.do").setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return getForward(request, mapping, "to_form");
	}
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id=this.getParameter("id", request);
		if(id != null){
			Result result=mgt.detail(id);
			 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			    request.setAttribute("qa", result.retVal);
			 }
			 if(result.retCode == ErrorCanst.ER_NO_DATA){
				 request.setAttribute("nodate", "nodate");
			 }
		}	
		return getForward(request, mapping, "to_form");
	}
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		CRMQAForm  qaForm =(CRMQAForm) form;
		String msg="";
		String id=this.getParameter("qaid", request);
		if(id != null){
			Result result=mgt.detail(id);
			 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				 CRMQABean bean= (CRMQABean) result.getRetVal();
				 bean.setAnswer(qaForm.getAnswer());
				 bean.setRef_id(qaForm.getRef_id());
				 String userId = this.getLoginBean(request).getId();
				 bean.setLastUpdateBy(userId);
				 bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				  //处理附件
				 String file=this.getParameter("attachFiles", request)==null?"":this.getParameter("attachFiles", request);
					bean.setAttachment(file);
			     // 需删除的附件
					String delFiles = getParameter("delFiles",request);
					
					// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
					String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
					for (String del : dels) {
						if (del != null && del.length() > 0
								&& bean.getAttachment().indexOf(del) == -1) {
						
							File aFile = new File(BaseEnv.FILESERVERPATH + "/crm/QA/" + del);
							aFile.delete();
						}
					}
					
				 
				 
				 
				 
				 result=mgt.update(bean);
				 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					 msg="<script type='text/javascript' >";
					  msg += "parent.successbox();";
					 msg += "</script>";
					 request.setAttribute("msg", msg);
					 return getForward(request, mapping, "blank");
				 }
			 }
			 
		}	
		EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
			.setBackUrl("/CRMQASearchAction.do").setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 String[] ids=this.getParameters("keyId", request);
		 String msg="no";
		 Result result= mgt.deleteById(ids);
		 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			 // 需删除的附件,因为宿主都删了，所以这些随从也要删除
				String delFiles = getParameter("delfile",request);
				delFiles=GlobalsTool.toChinseChar(delFiles);
				// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
				String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
				for (String del : dels) {
					if (del != null && del.length() > 0) {				
						File aFile = new File(BaseEnv.FILESERVERPATH + "/crm/QA/" + del);
						aFile.delete();
					}
				}
				
			 msg="yes";
		 }
		 request.setAttribute("msg", msg);
		 return getForward(request, mapping, "blank");		
	}
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CRMQASearchForm searchForm=(CRMQASearchForm)form;
		//request.setAttribute("logForm", searchForm);
		if ("menu".equals(getParameter("src", request))) {
			request.getSession().setAttribute("CRMQASearchForm", null);
		}
		/* 获得登陆者ID */
		String userId = this.getLoginBean(request).getId();
		Result result =  mgt.query(searchForm);
		 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			 request.setAttribute("list",result.retVal);
		 }
		 request.setAttribute("pageBar", pageBar(result, request));
		return getForward(request, mapping, "to_queryQA");
	}

}
