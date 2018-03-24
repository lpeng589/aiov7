package com.menyi.email;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.MailinfoSettingBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.email.util.AIOEMail;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class EMailAccountAction extends MgtBaseAction {
	EMailMgt mgt = new EMailMgt();	
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        String noback=request.getParameter("noback");//不能返回
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		int operation = getOperation(request);
		String type = request.getParameter("type");
		
		String userId = this.getLoginBean(request).getId();
		request.setAttribute("LoginId", userId);

		ActionForward forward = null;
		
		String dir = request.getParameter("dir");
		if(dir != null && dir.length() > 0){
			request.setAttribute("operation", operation);
			return this.preNext(mapping, form, request, response);
		}
		
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			request.setAttribute("operation", OperationConst.OP_ADD);
			forward = addPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			String op = request.getParameter("op");
			if(op != null && op.length() >0){
				forward = mailSet(mapping, form, request, response);
			}else{
				request.setAttribute("operation", OperationConst.OP_UPDATE);
				forward = updatePrepare(mapping, form, request, response);
			}
			break;	
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	
	private ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();
		MailinfoSettingBean bean = new MailinfoSettingBean();
		bean.setSmtpPort(25);
		bean.setPop3Port(110);
		bean.setSmtpAuth(1);
		bean.setSmtpsamepop(1);
		bean.setRetentDay(-1);
		request.setAttribute("mainAccount", request.getParameter("mainAccount"));
		request.setAttribute("result", bean);
		
		return getForward(request, mapping, "mailAccount");
	}
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		MailinfoSettingBean setting = new MailinfoSettingBean();
		EMailAccountForm myForm = (EMailAccountForm)form;
		readMailSetting(myForm,setting);
		//新增
		String id = IDGenerater.getId();
		setting.setId(id);
		setting.setCreateby(this.getLoginBean(request).getId());
		setting.setCreatetime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		Result rs  = mgt.addMailinfoSetting(setting);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			// 添加成功
			request.setAttribute("dealAsyn", "OK");
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else if (rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			request.setAttribute("dealAsyn", "ERROR");
			EchoMessage.error().add(getMessage(request, "oa.mail.msg.addressDouble")).setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			throw new BusinessException("common.msg.addFailture",
					"/EMailAccountQueryAction.do?mainAccount="+myForm.getMainAccount());
		}
	}
	private void readMailSetting(EMailAccountForm form,MailinfoSettingBean mailSet){
		mailSet.setAccount(form.getAccount());
		mailSet.setDefaultUser("1".equals(form.getDefaultUser())?"1":"2");
		mailSet.setDisplayName(form.getDisplayName());
		mailSet.setId(form.getId());
		mailSet.setMailaddress(form.getMailaddress());
		mailSet.setPop3Port(form.getPop3Port());
		mailSet.setPop3server(form.getPop3server());
		mailSet.setPop3username(form.getPop3username());
		mailSet.setPop3userpassword(form.getPop3userpassword());
		mailSet.setPopssl("1".equals(form.getPopssl())?1:2);
		mailSet.setSmtpAuth("1".equals(form.getSmtpAuth())?1:2);
		mailSet.setSmtpPort(form.getSmtpPort());
		mailSet.setSmtpsamepop("1".equals(form.getSmtpsamepop())?1:2);
		mailSet.setSmtpserver(form.getSmtpserver());
		mailSet.setSmtpssl("1".equals(form.getSmtpssl())?1:2);
		mailSet.setSmtpusername(form.getSmtpusername());
		mailSet.setSmtpuserpassword(form.getSmtpuserpassword());
		mailSet.setAutoReceive(form.getAutoReceive());
		mailSet.setRetentDay(form.getRetentDay());
		mailSet.setAutoAssign(1==form.getAutoAssign()?1:2);
		mailSet.setMainAccount(form.getMainAccount());
	}
	
	/**
	 * 修改前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();
		String id = request.getParameter("accountId");
		Result rs = mgt.loadAccount(id);
		MailinfoSettingBean bean  = (MailinfoSettingBean)rs.retVal;
		bean.setPop3userpassword(AIOEMail.decodePass(bean.getPop3userpassword()));
		bean.setSmtpuserpassword(AIOEMail.decodePass(bean.getSmtpuserpassword()));
		request.setAttribute("result", bean);
		request.setAttribute("mainAccount", request.getParameter("mainAccount"));
		return getForward(request, mapping, "mailAccount");
	}
	
	
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		MailinfoSettingBean setting = new MailinfoSettingBean();
		EMailAccountForm myForm = (EMailAccountForm)form;
		readMailSetting(myForm,setting);
		//修改
		setting.setLastupdateby(this.getLoginBean(request).getId());
		setting.setLastupdatetime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		Result rs  = mgt.updateMailinfoSetting(setting);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("dealAsyn", "OK");
			EchoMessage.success().add(getMessage(
                request, "common.msg.updateSuccess")).setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else if (rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR){
			request.setAttribute("dealAsyn", "ERROR");
			EchoMessage.error().add(getMessage(
	                request, "oa.mail.msg.addressDouble")).setAlertRequest(request);
			return getForward(request, mapping, "message");
		}  else {
			throw new BusinessException("common.msg.updateFailture",
					"/EMailAccountQueryAction.do?mainAccount="+myForm.getMainAccount());
		}
	}
	private ActionForward preNext(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();
		String dir = request.getParameter("dir");
		request.setAttribute("mainAccount", request.getParameter("mainAccount"));	
		if ("next".equals(dir)) {
			//下一步
			MailinfoSettingBean setting = new MailinfoSettingBean();
			readMailSetting((EMailAccountForm)form,setting);
			//这里判断，如果smtp等数据为空，则启动暂能数据处理,如判断用户邮件域名存在智能数据，则自动填充
			if(setting.getSmtpserver() == null || "".equals(setting.getSmtpserver().trim())){
				if(setting.getMailaddress() != null && setting.getMailaddress().indexOf("@")>0){
					mgt.mailMatch(setting);
				}
			}
			request.setAttribute("result", setting);
			request.setAttribute("dir", "next");			
			
			return getForward(request, mapping, "mailAccount");
		}else{
			//上一步
			MailinfoSettingBean setting = new MailinfoSettingBean();
			readMailSetting((EMailAccountForm)form,setting);
			request.setAttribute("result", setting);
			request.setAttribute("dir", "one");			
			
			
			return getForward(request, mapping, "mailAccount");
			
		}
	}


	/**
	 * 个人邮箱设置
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */
	private ActionForward mailSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();
		String op = request.getParameter("op");
		if ("default".equals(op)) {
			String id = request.getParameter("accountId");
			Result rs = mgt.defaultAccount(id,userId);
			return query(mapping,form,request,response);
		}else if ("stop".equals(op)) {
			String ids[] = request.getParameterValues("keyId");
			for(String id:ids){
				Result rs = mgt.setMailSatus(id,2);
			}
			return query(mapping,form,request,response);
		}else if ("start".equals(op)) {
			String ids[] = request.getParameterValues("keyId");
			for(String id:ids){
				Result rs = mgt.setMailSatus(id,1);
			}
			return query(mapping,form,request,response);
		} else if ("signPre".equals(op)) {
			//签名
			String id = request.getParameter("accountId");
			if (id != null && id.length() > 0) {
				Result rs = mgt.loadAccount(id);
				request.setAttribute("result", rs.retVal);
			}	
			request.setAttribute("mainAccount", request.getParameter("mainAccount"));
			return getForward(request, mapping, "mailSign");
		} else if ("sign".equals(op)) {
			//签名
			String id = request.getParameter("id");
			request.setAttribute("mainAccount", request.getParameter("mainAccount"));
			String signature = request.getParameter("signature");
			Result rs = mgt.updateSign(id,signature);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				// 添加成功
				request.setAttribute("dealAsyn", "SignOK");
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
				return getForward(request, mapping, "message");
			}else {
				request.setAttribute("dealAsyn", "SignERROR");
				throw new BusinessException("common.msg.updateFailture",
						"/EMailAccountQueryAction.do?mainAccount="+request.getParameter("mainAccount"));
			}
		}else if ("sharePre".equals(op)) {
			//共享
			String accountId = request.getParameter("accountId");
			if (accountId != null && accountId.length() > 0) {
				Result rs = mgt.loadShareUser(accountId);
				ArrayList flay = (ArrayList)rs.retVal;
				if(flay !=null && flay.size()>0){					
					request.setAttribute("flayy", "true");
				}
				request.setAttribute("result", rs.retVal);
				request.setAttribute("accountId", accountId);
			}	
			return getForward(request, mapping, "mailShare");
		} else if ("share".equals(op)) {
			//共享
			String accountId = request.getParameter("accountId");
			String[] userIds = request.getParameterValues("MailinfoSettingUser_userId");
			Result rs = mgt.updateSare(accountId,userIds,this.getLoginBean(request).getSunCmpClassCode());
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				// 添加成功
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl(
								"/EMailAccountQueryAction.do?reload=true")
						.setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}else {
				throw new BusinessException("common.msg.updateFailture",
						"/EMailAccountQueryAction.do");
			}
		}
		return null;
	}
	
	/**
	 * 查询邮箱账户设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		EMailAccountSearchForm myForm = (EMailAccountSearchForm)form;
		
		String userId = this.getLoginBean(request).getId();
		// 查询个人邮箱
		myForm.setEmailStatus(myForm.getEmailStatus() == null ? "1" : myForm.getEmailStatus());
		Result rs = mgt.selectMailAccountByUser(userId,myForm);
		request.setAttribute("list", rs.getRetVal());
		request.setAttribute("pageBar", pageBar(rs, request));
		return getForward(request, mapping, "mailAccountList");
	}
	
	private ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = ((LoginBean) request.getSession().getAttribute("LoginBean"))
		.getId();
		// 删除邮箱
		String id = request.getParameter("accountId");
		
		final String[] keyIds = request.getParameterValues("keyId");
		Result rs = null;
		//删除邮箱与邮件
		String sure=request.getParameter("delsure");
		if(sure!=null && sure.equals("true")){
			
			rs =mgt.delSureAccount(keyIds);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 重算手机未读邮件数
				mgt.notifyMobile(userId);
				request.setAttribute("msg", "ok");
				return getForward(request, mapping, "blank");
			}else{
				request.setAttribute("msg", "no");
				return getForward(request, mapping, "blank");
			}
		}
		 rs = mgt.delAccount(keyIds);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS
				|| rs.retCode == ErrorCanst.ER_NO_DATA) {
			request.setAttribute("msg", "ok");
			return getForward(request, mapping, "blank");
		}else if (rs.retCode == ErrorCanst.DATA_ALREADY_USED) {
			String title ="oa.mail.msg.mailUse";
			if("mainAccountUse".equals(rs.retVal) ){
				title = "oa.mail.msg.mainAccountUse";
			}
			request.setAttribute("msg", "many");
			return getForward(request, mapping, "blank");
//			throw new BusinessException(title,
//					"/EMailAccountQueryAction.do");
		} else {
			throw new BusinessException("common.msg.delError",
					"/EMailAccountQueryAction.do");
		}
	}
	
}
