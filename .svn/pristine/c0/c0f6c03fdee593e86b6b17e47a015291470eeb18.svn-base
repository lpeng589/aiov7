package com.koron.crm.distributeswork;

import java.io.File;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.client.CRMClientMgt;
import com.koron.crm.qa.CRMQABean;
import com.koron.crm.qa.CRMQAForm;
import com.koron.crm.qa.CRMQASearchForm;
import com.koron.oa.office.meeting.OAMeetingBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceForm;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

public class CRMDistWorkAction extends MgtBaseAction{
private CRMDistWorkMgt mgt=new CRMDistWorkMgt();
private CRMClientMgt  clientmgt=new CRMClientMgt();
private AdviceMgt advicemgt = new AdviceMgt();	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 
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
			String back=this.getParameter("requestType", request);
			back=back==null?"":back;
			if(back.equals("back")){
			  forward =  back(mapping, form, request, response);
			}else{
			 forward = detail(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUERY:
			String type=this.getParameter("requestType", request);
			type=type==null?"":type;
			if(type.equals("handle")){
				forward =handle(mapping, form, request, response);
			} else{
			 forward = query(mapping, form, request, response);
			}
			break;
		default:
			
		     forward = query(mapping, form, request, response);
		break;
		}
		return forward ;
	}

	public ActionForward back(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id=this.getParameter("workid", request);
		if(id != null){
			Result result=mgt.detail(id);
			 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				 CRMDistWorkBean bean= (CRMDistWorkBean) result.getRetVal();
				 String userId = this.getLoginBean(request).getId();
				 String userName=GlobalsTool.getEmpFullNameByUserId(userId);
				 String backuser=this.getParameter("userId", request);
				 String  backuserName=GlobalsTool.getEmpFullNameByUserId(backuser);
				 String date=BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
				 String content=GlobalsTool.toChinseChar(this.getParameter("result", request));
				 String li="<li style='border-bottom:solid 1px #999;clear:both;background-image:none;margin-bottom:5px;'>"+userName+" 于  "+date+"回退了"+backuserName+"的提交！<br />情况："+content+"</li>";
				String action =bean.getAction()==null?"":bean.getAction();
				 bean.setAction(action+li);
			
				 String users=bean.getFinishUser();
				 
				users= users.replace(";"+backuser+";", ";");
				 bean.setFinishUser(users);
				 result=mgt.update(bean);
				 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					 request.setAttribute("msg", "yes");
					
					 /*添加通知消息提醒*/
					 if(!userId.equals(bean.getCreateBy())){
						 String xinxi="任务分派通知："+backuserName+"已回退给你，请须知";
						 advice(bean.getId(),userId+",",backuser,xinxi);
					 }
				 }
			 }
			 
		}	
		 return getForward(request, mapping, "blank");
	
	}

	public  ActionForward handle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id=this.getParameter("workid", request);
		if(id != null){
			Result result=mgt.detail(id);
			 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				 CRMDistWorkBean bean= (CRMDistWorkBean) result.getRetVal();
				 String userId = this.getLoginBean(request).getId();
				 String userName=GlobalsTool.getEmpFullNameByUserId(userId);
				 String date=BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
				 String content=GlobalsTool.toChinseChar(this.getParameter("result", request));
				 String li="<li style='border-bottom:solid 1px #999;clear:both;background-image:none;margin-bottom:5px;'>"+userName+" 于  "+date+" 提交任务！<br />情况："+content+"</li>";
				String action =bean.getAction()==null?"":bean.getAction();
				 bean.setAction(action+li);
				 String finishuser=bean.getFinishUser()==null?";":bean.getFinishUser();
				 bean.setFinishUser(finishuser+userId+";");
				 result=mgt.update(bean);
				 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					 request.setAttribute("msg", "yes");
					
					 if(!bean.getCreateBy().equals(userId)){
					 String xinxi="任务分派："+userName+"已提交任务，请须知";
					 advice(bean.getId(),bean.getCreateBy()+",","",xinxi);
					 }
				}
			 }
			 
		}	
		 return getForward(request, mapping, "blank");
	}

	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CRMDistWorkSearchForm searchForm=(CRMDistWorkSearchForm)form;
		//request.setAttribute("logForm", searchForm);
		if ("menu".equals(getParameter("src", request))) {
			request.getSession().setAttribute("CRMDistWorkSearchForm", null);
		}
		/* 获得登陆者ID */
		String userId = this.getLoginBean(request).getId();
		request.setAttribute("self",userId);
		Result result =  mgt.query(searchForm);
		 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			 List<CRMDistWorkBean> beans=new ArrayList();
			 List<Object[]>  temp=(List<Object[]>)result.retVal;
			 for(Object[] cols : temp){
				 CRMDistWorkBean  bean=new CRMDistWorkBean();
				 bean.setId(cols[0]+"");
				 bean.setRef_id(cols[2]+"");
				 bean.setTaskType(cols[3]+"");
				 bean.setContent(cols[4]+"");
				 bean.setCreateBy(cols[5]+"");
				 bean.setUserId(cols[6]+"");
				 cols[7]=cols[7]==null?0:cols[7];
				 bean.setStatusId(Integer.parseInt(cols[7]+""));
				 bean.setTaskStatus(cols[8]+"");
				 bean.setCreateTime(cols[9]+"");
				 bean.setFinishUser(cols[10]+"");
				 beans.add(bean);
			 }
			 request.setAttribute("list",beans);
		 }
		 request.setAttribute("pageBar", pageBar(result, request));
		return getForward(request, mapping, "to_list");
		
	}

	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ref="";
		String id=this.getParameter("id", request);
		if(id != null){
			Result result=mgt.detail(id);
			 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				 CRMDistWorkBean bean=(CRMDistWorkBean)result.retVal;
				result= clientmgt.findClientById(bean.getRef_id());
				if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List<Object[]> obj=(List<Object[]>)result.retVal;
					
					if(obj!=null){
						ref=""+obj.get(0)[1];
					}
					
				}
				request.setAttribute("clientName", ref);
			    request.setAttribute("distwork", bean);
			 }
			 if(result.retCode == ErrorCanst.ER_NO_DATA){
				 request.setAttribute("nodate", "nodate");
			 }
		}	
		return getForward(request, mapping, "to_detail");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] ids=this.getParameters("keyId", request);
		 String msg="no";
		 Result result= mgt.deleteById(ids);
		 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			 // 需删除的附件,因为宿主都删了，所以这些随从也要删除
//				String delFiles = getParameter("delfile",request);
//				delFiles=GlobalsTool.toChinseChar(delFiles);
//				// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
//				String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
//				for (String del : dels) {
//					if (del != null && del.length() > 0) {				
//						File aFile = new File(BaseEnv.FILESERVERPATH + "/crm/QA/" + del);
//						aFile.delete();
//					}
//				}
				
			 msg="yes";
		 }
		 request.setAttribute("msg", msg);
		 return getForward(request, mapping, "blank");	
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CRMDistWorkForm  workForm =(CRMDistWorkForm) form;
		String msg="";
		String id=this.getParameter("keyId", request);
		Result result=mgt.detail(id);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			CRMDistWorkBean bean= (CRMDistWorkBean) result.getRetVal();
			read(workForm, bean);
			String users= bean.getUserId();
			if(users != null && !users.trim().equals("")){
				bean.setAssignStatus("0");
			}
			bean.setLastUpdateBy(this.getLoginBean(request).getId());
			bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			
			
			String[] uploadBtn = request.getParameterValues("uploadBtn");//上传的图片
			String[] uploadDelPic = request.getParameterValues("uploadDelPic");//删除的的图片
			String uploadStrs = "";//存放上传信息
			if(uploadBtn!=null && uploadBtn.length>0){
				uploadStrs = this.uploadStr(uploadBtn);
			}

			bean.setCrmAffix(uploadStrs);
		 
			result=mgt.update(bean);
			 
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				
				//处理附件
				if(uploadStrs!=null && !"".equals(uploadStrs)){
					for(String str :uploadStrs.split(";")){
						FileHandler.copy("CRMTaskAssign", FileHandler.TYPE_AFFIX, str, str);
						FileHandler.deleteTemp(str);
					}
				}
			
				if(uploadDelPic!=null && uploadDelPic.length>0){
					for(String delPic : uploadDelPic){
						FileHandler.delete("CRMTaskAssign", FileHandler.TYPE_PIC, delPic);
					}
				}
			}
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
			
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
			.setBackUrl("/CRMDistWorkSearchAction.do").setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
		
	}

	public ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return getForward(request, mapping, "to_form");
	}
	
	/**
	 * 添加方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CRMDistWorkForm  workForm =(CRMDistWorkForm) form;
		CRMDistWorkBean bean =new CRMDistWorkBean();
		read(workForm, bean);
		LoginBean loginBean = getLoginBean(request);
		bean.setId(IDGenerater.getId());
		bean.setAssignStatus("-1");
		bean.setTaskStatus("-1");
		
		bean.setCreateBy(this.getLoginBean(request).getId());
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		
		//附件
		String[] uploadBtn = request.getParameterValues("uploadBtn");//上传附件或图片信息;
		String uploadStrs = "";//存放上传信息
		if(uploadBtn!=null && uploadBtn.length>0){
			uploadStrs = this.uploadStr(uploadBtn);
		}
		bean.setCrmAffix(uploadStrs);
		
		Result result=mgt.add(bean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
			if(bean.getUserId()!=null && !"".equals(bean.getUserId())){
				String sql ="";
				String title=loginBean.getEmpFullName()+"委派任务给你，请查看。";
				String url = "/CRMDistWorkSearchAction.do?operation=4";
				String message = "<a href=javascript:mdiwin(''" + url + "'',''委派任务'')>"+title+"</a>";
				String id = IDGenerater.getId();
				
				new AdviceMgt().add(loginBean.getId(), title, message, bean.getUserId(), bean.getId(), "");
				
			}	
			
		
			
			if(uploadStrs!=null && !"".equals(uploadStrs)){
				for(String str :uploadStrs.split(";")){
					FileHandler.copy("CRMTaskAssign", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
			//advice(bean.getId(),bean.getUserId(),bean.getCreateBy(),msgInfo);
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
			.setBackUrl("/CRMDistWorkSearchAction.do").setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
		
	}

	public void advice(String workId,String userIds,String self,String msg){
	//	BaseEnv.log.error("用户ID:"+popedomUserIds);
        String url = "/CRMDistWorkAction.do";
        String favoriteURL = url + "?noback=true&operation=5&id=" + workId+ "&isEspecial=1";
       
        String content = "<a href=javascript:mdiwin('" + favoriteURL + "','"
					  + "通知"
					  + "')>" +msg
					  + "</a>"; // 内容
        

		
		//向用户通知
        if(userIds==null || userIds.indexOf(",")==-1){
        	return;
        }
        String users="";
        for(String user : userIds.split(",")){
        	if(user!=null && !user.equals("")){
        	users +=user+",";
        	}
        }
		advicemgt.add(self, "任务分派", content, users, workId, "");
        
	}
	
	
	
	
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String clientName="";
		String id=this.getParameter("id", request);
		if(id != null){
			Result result=mgt.detail(id);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				CRMDistWorkBean bean=(CRMDistWorkBean)result.retVal;
				result= clientmgt.findClientById(bean.getRef_id());
				if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List<Object[]> obj=(List<Object[]>)result.retVal;
					
					if(obj!=null && obj.size()>0){
						clientName=""+obj.get(0)[1];
					}
					
				}
				request.setAttribute("clientName", clientName);
			    request.setAttribute("distwork", bean);
			}
		}	
		return getForward(request, mapping, "update");
	}
	
	/**
	 * 获取上传图片的字符串 
	 * @param picbutton
	 * @return
	 */
	public String uploadStr(String[] picbutton){
		String allPic ="";
		if(picbutton!=null && picbutton.length>0){
			for(String pic:picbutton){
				allPic += pic +";";
			}
			if(allPic.endsWith(";")){
				allPic = allPic.substring(0,allPic.length()-1);
			}
		}
		return allPic;
	}
}
