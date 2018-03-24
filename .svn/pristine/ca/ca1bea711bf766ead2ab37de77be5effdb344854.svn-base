package com.menyi.email;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.crm.client.CRMClientMgt;
import com.koron.oa.bean.*;
import com.koron.oa.communicationNote.CommunicationNoteMgt;
import com.menyi.web.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;
import java.util.ArrayList;

import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;

import java.util.Map;
import java.io.File;

import java.util.*;

import com.menyi.aio.bean.BaseDateFormat;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import com.menyi.email.emailFilter.EmailFilterMgt;
import com.menyi.email.util.AIOEMail;
import com.menyi.email.util.EMailMessage;

import java.util.Date;
import java.io.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;

public class EMailAction extends MgtBaseAction {
	EMailMgt mgt = new EMailMgt();
	EmailFilterMgt efMgt = new EmailFilterMgt();
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// wordType=2为导出 1为批量删除
		String hide = BaseEnv.systemSet.get("HideEmail").getSetting(); // 是否隐藏邮件地址
		// (false为不隐藏)
		request.setAttribute("hide", hide);
		//如果获得的clearSerchForm其值是"yes"的话，清空form中的内容
        if("yes".equals(request.getParameter("clearSerchForm"))){
        	form=null;
        	request.setAttribute("clearSerchForm", "yes");
        }
        String noback=request.getParameter("noback");//不能返回
        String collectionMail=request.getParameter("collectionMail");//是否收藏进入
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		int operation = getOperation(request);
		String type = request.getParameter("type");
		String userId = ((LoginBean) request.getSession().getAttribute("LoginBean"))
				.getId();
		request.setAttribute("LoginId", userId);

		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			String importMail = request.getParameter("importMail");
			if("prepare".equals(importMail)){
				forward = importPrepare(mapping, form, request, response);
			}else if("exec".equals(importMail)){
				forward = importMail(mapping, form, request, response);
			}else{
				forward = addPrepare(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			if (("undo").equals(type)) {
				// 撤销邮件
				forward = dels(mapping, form, request, response);
			} else if("delSendHistory".equals(type)){
				//删除发送邮件的历史记录
				forward = delSendHistory(mapping, form, request, response);
			}else {
				forward = delete(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			if("collection".equals(collectionMail)){
				forward = collectionMail(mapping, form, request, response);
			}else if("dealRemark".equals(collectionMail)){
				//添加或者修改，删除邮件备注
				forward = dealRemark(mapping, form, request, response);
			}else{
				forward = update(mapping, form, request, response);
			}
			break;

		// 根据条件查询
		case OperationConst.OP_DETAIL:			
			forward = mailDetail(mapping, form, request, response);
			break;
		// 根据条件查询
		case OperationConst.OP_QUERY:

			if (null != type && type.equals("receive")) {
				// 收信采用OP_QUERY一样的权限控制
				forward = receiveMail(mapping, form, request, response);
			} else if (null != type && type.equals("groupmanager")) {
				// 文件夹管理， 文件夹管理放在OP_QUERY,原因是文件夹管理不需权限限制，能访问邮件就能管理文件夹
				forward = groupManage(mapping, form, request, response);
			}else if (null != type && type.equals("mailCRM")) {
				// 文件夹管理， 文件夹管理放在OP_QUERY,原因是文件夹管理不需权限限制，能访问邮件就能管理文件夹
				forward = mailCRM(mapping, form, request, response);
			}else if ("readStatus".equals(type)) {
				forward = readStatus(mapping, form, request, response);
			}else if("queryUserData".equals(type)){
				//查询用户的数据（组织架构，通讯录，客户）
				forward = queryUserData(mapping, form, request, response);
			}else if("queryTo".equals(type)){
				//查询收件人的数据
				forward = queryTo(mapping, form, request, response);
			}else {
				forward = query(mapping, form, request, response);
			}
			break;
		// 导出
		case OperationConst.OP_EXPORT:
			forward = export(mapping, form, request, response);
			break;
		// 添加提醒设置
//		case OperationConst.OP_SET_ALERT:
//			forward = setAlert(mapping, form, request, response) ;
//			break ;
//		case OperationConst.OP_CANCEL_ALERT:
//			forward = cancelAlert(mapping, form, request, response) ;
//			break ;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 导出
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward export(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LoginBean user = (LoginBean) request.getSession().getAttribute(
				"LoginBean");
		String userId = user.getId();
		String emailType = request.getParameter("emailType"); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		request.setAttribute("groupId", groupId);

		ActionForward forward = null;
		String nstr[] = request.getParameterValues("keyId");
		if (nstr != null && nstr.length != 0) {
			String str = GlobalsTool.getMessage(request, "oa.email.emailTimeFormatError");
			Result rs = new Result();
			if("stadexport".equals(request.getParameter("exportType"))){
				rs = mgt.exportToEml(nstr, userId);
			}else{
				rs = mgt.exportToAIO(nstr, userId);
			}
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.info()
				.add(this.getMessage(request, "oa.email.exportEnter"))
				.setBackUrl(
						"/EMailQueryAction.do?operation="
								+ OperationConst.OP_QUERY + "&emailType="
								+ emailType + "&type=main&groupId=" + groupId)
				.setAlertRequest(request);
				request.setAttribute("fileName", rs.retVal);
				return getForward(request,mapping,"export");
			}
		}
		EchoMessage.error()
		.add(this.getMessage(request, "email.msg.mailexportfail"))
		.setBackUrl(
				"/EMailQueryAction.do?operation="
						+ OperationConst.OP_QUERY + "&emailType="
						+ emailType + "&type=main&groupId=" + groupId)
		.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * 删除
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
	 * @throws Exception
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String emailType = request.getParameter("emailType"); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		request.setAttribute("groupId", groupId);

		ActionForward forward = null;
		String nstr[] = request.getParameterValues("keyId");
		if (nstr != null && nstr.length != 0) {
			if ("2".equals(groupId) || "5".equals(groupId) || "true".equals(request.getParameter("deleteReal"))) {
				// 执行删除
				mgt.delMail(nstr);
			} else {
				// 执行转移到废弃箱
				mgt.moveMail(nstr, "5");
			}
		}
		// 添加成功
		/**
		EchoMessage.success().add(
				this.getMessage(request, "common.msg.delSuccess")).setBackUrl(
				"/EMailQueryAction.do?operation=" + OperationConst.OP_QUERY
						+ "&emailType=" + emailType + "&type=main&groupId="
						+ groupId).setAlertRequest(request);

		return getForward(request, mapping, "alert");
          **/
		request.setAttribute("msg", "ok");
		return getForward(request, mapping, "blank");
	}

	/**
	 * 修改
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
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String emailType = request.getParameter("emailType"); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		request.setAttribute("groupId", groupId);

		String moveGroup = request.getParameter("moveGroup");
		String assignUser = request.getParameter("assignUser");
		String signStr = this.getParameter("sign", request);
		String noBegReplay = this.getParameter("noBegReplay", request);
		String begReplay = this.getParameter("begReplay", request);
		
		String setLabel = getParameter("setLabel",request);
		String clearLabel = getParameter("clearLabel",request);
		String userId = this.getLoginBean(request).getId();
		
		int sign = -1;
		try{ sign = Integer.parseInt(signStr);}catch(Exception e){};
		
		

		ActionForward forward = null;
		String nstr[] = request.getParameterValues("keyId");
		if (nstr != null && nstr.length != 0) {
			if(assignUser!=null && !"".equals(assignUser)){
				//邮件分配
				Result rs =mgt.mailAssign(nstr, emailType,this.getLoginBean(request).getId(),assignUser);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					NotifyFashion notify = new NotifyFashion() ; 
					for(String s:nstr){
						notify.sendAdvice(userId,
							"RES<mail.msg.assignnewMail>",
							"<a href=\"javascript:mdiwin('/EMailAction.do?operation=5&detailType=true&emailType=assign&noback=true&keyId="+s+"&newOpen=true','RES<oa.mail.myMail>')\">"+"RES<mail.msg.assignnewMail></a>",
							assignUser,"","email");
					}
					
				EchoMessage.success().add(this.getMessage(request, "email.msg.assignsucc"))
						.setBackUrl(
								"/EMailQueryAction.do?operation="
										+ OperationConst.OP_QUERY + "&emailType="
										+ emailType + "&type=main&groupId=" + groupId)
						.setAlertRequest(request);
				
				}else{
					EchoMessage.error().add(
							this.getMessage(request, "email.msg.assignfail"))
							.setBackUrl(
									"/EMailQueryAction.do?operation="
											+ OperationConst.OP_QUERY + "&emailType="
											+ emailType + "&type=main&groupId=" + groupId)
							.setAlertRequest(request);
				}
			}
			if(moveGroup!=null && !"-1".equals(moveGroup)){
				int count = mgt.moveMail(nstr, moveGroup);
				if(count ==0){
					EchoMessage.success().add(
							this.getMessage(request, "oa.mail.msg.transferSuccess"))
							.setBackUrl(
									"/EMailQueryAction.do?operation="
											+ OperationConst.OP_QUERY + "&emailType="
											+ emailType + "&type=main&groupId=" + groupId)
							.setAlertRequest(request);
				}else{
					EchoMessage.success().add(
							this.getMessage(request, "oa.mail.msg.transferSuccess")+","+this.getMessage(request, "email.msg.noremove",count+""))
							.setBackUrl(
									"/EMailQueryAction.do?operation="
											+ OperationConst.OP_QUERY + "&emailType="
											+ emailType + "&type=main&groupId=" + groupId)
							.setAlertRequest(request);
				}
			}
			if(-1 !=(sign)){
				mgt.signMail(nstr, sign);
				EchoMessage.success().add(
						this.getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl(
								"/EMailQueryAction.do?operation="
										+ OperationConst.OP_QUERY + "&emailType="
										+ emailType + "&type=main&groupId=" + groupId)
						.setAlertRequest(request);
			}
			if ( setLabel != null && !"-1".equals(setLabel)) {
					// 标记邮件标签
					String keyIds[] = request.getParameterValues("keyId");
					if(keyIds != null && keyIds.length > 0){
						for(String key:keyIds){
							Result rs = mgt.setMailLabel(key,setLabel);
						}
					}
					String msg = "email.lb.label.success";
					if(setLabel.equals("")){
						msg = "email.lb.label.clear";
					}
					
					
					EchoMessage.success().add(
							this.getMessage(request, msg))
							.setBackUrl(
									"/EMailQueryAction.do?operation="
											+ OperationConst.OP_QUERY + "&emailType="
											+ emailType + "&type=main&groupId=" + groupId)
							.setAlertRequest(request);
			}  
			if (clearLabel != null && clearLabel.length() > 0) {
				// 标记邮件标签
				String keyId = request.getParameter("keyId");
				clearLabel = new String(clearLabel.getBytes("ISO8859-1"),"UTF-8");
				Result rs = new Result();
				if(keyId != null && keyId.length() > 0){
						rs = mgt.delMailLabel(keyId,clearLabel);
				}
				if(rs.retCode ==ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(
						this.getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl(
								"/EMailAction.do?operation=5&emailType="+emailType+"&iframe=true&keyId="+keyId)
						.setAlertRequest(request);
				}else{
					EchoMessage.error().add(
							this.getMessage(request, "common.msg.updateFailture"))
							.setBackUrl(
									"/EMailAction.do?operation=5&emailType="+emailType+"&iframe=true&keyId="+keyId)
							.setAlertRequest(request);
				}
			} 
			if("true".equals(noBegReplay)){
				//不发送邮件回执
				String key = request.getParameter("keyId");
				
				Result rs = mgt.setMailNoBegReplay(key);
						
				EchoMessage.success().add(
						this.getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl(
								"/EMailQueryAction.do?operation="
										+ OperationConst.OP_QUERY + "&emailType="
										+ emailType + "&type=main&groupId=" + groupId)
						.setAlertRequest(request);
			}
			if("true".equals(begReplay)){
				//发送邮件回执
				String key = request.getParameter("keyId");
				
				String content="This is a receipt for the mail you sent to:"+"bean.getMailTo" +"\r\n"+
	    		" The original subject is: "+"bean.getMailTitle" +"\r\n"+
	    		" This receipt verifies that the message has been displayed on the recipient's computer at "+"mailInfo.getMailTime";
				if(!this.getLocale(request).toString().equals("en")){
					content +="<br/>"+this.getMessage(request, "email.msg.mailreplaymsg","bean.getMailTo","bean.getMailTitle","mailInfo.getMailTime") ;
				}
				
				Result rs = mgt.setMailBegReplay(key,this.getLoginBean(request).getId(),this.getLoginBean(request).getEmpFullName(),content);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("msg","OK");
				}else if(rs.retCode == ErrorCanst.RET_NO_RIGHT_ERROR)		{
					request.setAttribute("msg", "ERROR:"
							+ this.getMessage(request,"oa.mail.send.AuthenticationFailture")+","+this.getMessage(request,
							"email.msg.savetodraft"));
				}else{
					request.setAttribute("msg", "ERROR:"
							+ this.getMessage(request,"oa.mail.receive.ConnFailture")+","+this.getMessage(request,
							"email.msg.savetodraft"));
				}
				return getForward(request, mapping, "blank");
			}
		}

		return getForward(request, mapping, "alert");
	}

	/**
	 * 添加前的准备
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
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String newClient = getMessage(request, "com.new.client");
		LoginBean lg = this.getLoginBean(request);
		String emailType = request.getParameter("emailType");      				//帐号类型(空表示内部邮箱，不为空代表外部邮箱)
		if(emailType == null || "alert".equals(emailType)){
			emailType = "";
		}
		String moreMail = request.getParameter("moreMail");
		if(moreMail == null || moreMail.length() ==0){
			moreMail = emailType;
		}
		request.setAttribute("moreMail", moreMail);
		String groupId = request.getParameter("groupId");
		request.setAttribute("groupId", groupId);
		
		/* 其它路径进入发送邮件界面时，会指定收件人。如:客户列表，通讯录，组织架构 */
		String sendPerson = request.getParameter("sendPerson");
		if(sendPerson != null && sendPerson.length() > 0){
			//查询用户设置的邮箱账户
			Result rs = mgt.selectAccountByUser(lg.getId());
			MailinfoSettingBean obj = null;
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList list = (ArrayList) rs.retVal;
				if(list.size() > 0){				
					for (int i = 0; i < list.size(); i++) {
						MailinfoSettingBean a = (MailinfoSettingBean) list.get(i);
						if (a.getDefaultUser().equals("1")) {
							//如果用户设置了默认邮箱账户，则取该账户
							obj = a;
							break;
						}
					}
					if(obj == null){
						//未设置时取第一个账户
						obj = (MailinfoSettingBean) list.get(0);
					}
				}
			}
			if(obj == null){
				//未存在账户时报错
				if("true".equals(getParameter("noback", request))){
					EchoMessage.info().add(this.getMessage(request, "oa.mail.msg.setemail")).setNotAutoBack()
						.setAlertRequest(request);
				}else{
					EchoMessage.info().add(this.getMessage(request, "oa.mail.msg.setemail")).setClose()
					.setAlertRequest(request);
				}
				return getForward(request, mapping, "alert");
			}
			emailType = obj.getId();
			
			//查询出匹配的职员
			Result result=null;
			String msgType = getParameter("msgType", request); 
			if(!msgType.contains("detail")){ //客户列表页面发送邮件
				if("txl".equals(getParameter("txl",request))){
					//通讯录进入发送邮件界面
					result = new CommunicationNoteMgt().selectClientDetById(sendPerson.split(","),"clientEmail");
				}else{
					result = new CRMClientMgt().selectClientDetById(sendPerson.split(","),"clientEmail");
				}
			}else{   //客户详情页面发送邮件
				result = new CRMClientMgt().detailClientDetById(sendPerson);	
				request.setAttribute("sendType", "detailPage");
			}
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//保存需要收件人的数据
				request.setAttribute("sendPeriodList", result.retVal) ;
				request.setAttribute("sendPeriod", sendPerson);
				
			}
		}
		//以邮箱代号为路径
		String attachPath = emailType;
		if (attachPath == null || attachPath.length() == 0 || attachPath.equals("inner")) {
			attachPath = "inner" + lg.getId();
		}
		request.setAttribute("path", "/email/"+attachPath); //附件保存的路径
		
		// 邮件类型为空，则显示内部邮件
		if (emailType == null || "inner".equals(emailType)) {
			emailType = "";
		}
		
		//如果邮箱超限直接提示
		if(!mgt.checkMailSizeList(emailType.equals("")?0:1, emailType, lg.getId())){
			throw new BusinessException(this.getMessage(request,"common.msg.mailSizeLimit"), "/EMailQueryAction.do?operation="
			+ OperationConst.OP_QUERY + "&emailType=" + moreMail + "&type=main&groupId=1");
		}

		request.setAttribute("emailType", emailType); 
		String mailAddress = "";
		MailinfoSettingBean accountInfoBean =null;
		if (emailType.length() > 0) {
			Result rs = mgt.loadAccount(emailType);
			//用户的邮箱
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				accountInfoBean = (MailinfoSettingBean)rs.retVal;
				request.setAttribute("accountInfo", rs.retVal);
				mailAddress = ((MailinfoSettingBean) rs.retVal).getMailaddress();
			}
		} else {
			mailAddress = lg.getEmpFullName();
		}
		request.removeAttribute("operation");

		// 判断回复，回复所有，转发来解析数据
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		if (type != null && (type.equals("revert") || type.equals("revertAll") || type.equals("transmit") 
				|| type.equals("transmitOut") || type.equals("transmitInt"))) {
			String keyId = request.getParameter("keyId");
			//如果是回复和回复所有，记录回复单号，方便回填回复时间
			if (type.equals("revert") || type.equals("revertAll")) {
				request.setAttribute("replayId", keyId);
			}
			if (type.equals("transmit") || type.equals("transmitOut") || type.equals("transmitInt")) {
				request.setAttribute("revolveId", keyId);
			}
			//加载邮箱数据
			OAMailInfoBean bean = null;
			Result rs = mgt.loadMail(keyId);
			
			Result r = mgt.getMailReplyAccount(emailType);
			Object o = r.getRetVal();
			request.setAttribute("MailReplyAccount", o);
			if("assign".equals(moreMail)){
				//分配帐户
				ArrayList al = new ArrayList();
				al.add(new String[]{accountInfoBean.getId(),accountInfoBean.getAccount()});
				request.setAttribute("MailinfoSetting",al);
			}else if(o == null){//没有回复账户
			
				Result rs2 = mgt.getALLMailinfoSetting(lg.getId());
				List mailSettingList = (ArrayList)rs2.getRetVal();
				if(("transmitOut").equals(type)){
					//内部邮件转外部邮件时判断有没有外部邮箱
					if(mailSettingList.size()==0){
						throw new BusinessException("当前未设置外部邮箱，无法进行邮件转外部操作！", "/EMailQueryAction.do?operation="
								+ OperationConst.OP_QUERY + "&emailType=" + moreMail + "&type=main&groupId="+groupId);
					}
				}
				request.setAttribute("MailinfoSetting",mailSettingList);
			}else{
				Result rst = mgt.getMailReplyAccountDet((String)o);
				//除掉自己没有权限的帐户
				if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null){
					List<String[]> lr = (List<String[]>)rst.retVal;
					//自己有权限的帐户
					Result rs2 = mgt.getALLMailinfoSetting(lg.getId());
					List<String[]> lr2 = rs2.retVal !=null? (List<String[]>)rs2.retVal : new ArrayList<String[]>();
					
					List<String[]> lret = new ArrayList<String[]>();
					for(String[] ss :lr){
						for(String[] ss2:lr2){
							if(ss[0].equals(ss2[0])){
								lret.add(ss);
								break;
							}
						}
					}
					request.setAttribute("MailinfoSetting",lret);
				}
			}
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				//查询邮箱数据成功
				bean = (OAMailInfoBean) rs.getRetVal();
				String name = "";

//				String hide = BaseEnv.systemSet.get("HideEmail").getSetting(); // 是否隐藏邮件地址
				// (false为不隐藏)
//				String emailFrom = "";
//				String emailAddress = "";
//				//查询通讯录数据
//				Result rp = mgt.getNoteNameAndEmail(lg.getId());
//				HashMap<String, String> adds = (HashMap<String, String>)rp.getRetVal();
//				if (bean.getMailFrom() != null) {
//					//发送者
//					String oldEmailFrom = bean.getMailFrom();
//					//邮箱地址
//					emailAddress = oldEmailFrom.substring(oldEmailFrom.indexOf('<') + 1, oldEmailFrom.length() - 1);
//					if (oldEmailFrom.indexOf('<') != -1) {
//
//						//如果隐藏地址并且联系人为空，则显示新客户，否则显示联系人，如果联系人为空，则显示原邮件名称
//						emailFrom = oldEmailFrom.substring(0,oldEmailFrom.indexOf('<') );
//						if(adds.get(emailAddress) != null && !"".equals(adds.get(emailAddress))){
//							//根据通讯录邮箱进行匹配
//							emailFrom = adds.get(emailAddress).toString();
//						}else if("true".equals(hide)) {
//							emailFrom = newClient;
//						}	
//
//						if ("true".equals(hide)) {
//							bean.setMailFrom(emailFrom);
//						} else {
//							bean.setMailFrom(emailFrom + oldEmailFrom.substring(oldEmailFrom.indexOf('<'), oldEmailFrom.length()));
//						}
//					}
//				}
				name = bean.getMailFrom();

				// 编辑回复内容
				StringBuffer content = new StringBuffer();
				content.append("<br/><br/><br/><br/><br/><br/><br/>\r\n\r\n\r\n<div><!-- SIGN_START -->"+(accountInfoBean==null||accountInfoBean.getSignature()==null||accountInfoBean.getSignature().length()==0?"":"------------------------------------\r\n<br/>"+accountInfoBean.getSignature()+"<br/>\r\n------------------------------------")+"<!-- SIGN_END --></div><div style='text-align:left;'><b>"
						+ this.getMessage(request, "oa.mail.sendPeople")+ "：</b>" + name);
				Date time = BaseDateFormat.parse(bean.getMailTime(),BaseDateFormat.yyyyMMddHHmmss);
				content.append("<br><b>"
						+ this.getMessage(request, "message.lb.createTime")
						+ "：</b>" + (time.getYear() + 1900)
						+ this.getMessage(request, "com.date.year")
						+ (time.getMonth() + 1)
						+ this.getMessage(request, "oa.calendar.month")
						+ time.getDate()
						+ this.getMessage(request, "data.lb.w0")
						+ time.getHours() + ":" + time.getMinutes());

				content.append("<br><b>" + this.getMessage(request, "oa.mail.addressee") + "：</b>" + bean.getMailTo());
				if (bean.getMailCc() != null && bean.getMailCc().length() > 0) {
					content.append("<br><b>" + this.getMessage(request, "oa.mail.cc") + "：</b>" + bean.getMailCc());
				}

				content.append("<br><b>" + this.getMessage(request, "oa.subjects") + "：</b>" + bean.getMailTitle() +"</div>");
				bean.setMailContent(content.append(bean.getMailContent()).toString());

				if (type.equals("revert")) {
					bean.setMailTitle("Re：" + bean.getMailTitle());
					bean.setMailTo(name);
					bean.setMailAttaches(null);
				} else if (type.equals("revertAll")) {
					bean.setMailTo(name + ";" + bean.getMailTo()); // +";"+bean.getMailCc()+";"+bean.getMailBCc()
					// 去除自己
					if (bean.getMailTo().indexOf(mailAddress) > -1) {
						String pre = bean.getMailTo().substring(0, bean.getMailTo().indexOf(mailAddress));
						String last = bean.getMailTo().substring( bean.getMailTo().indexOf(mailAddress));
						if (pre.indexOf(",") > 0 || pre.indexOf(";") > 0) {
							int pos1 = pre.lastIndexOf(",");
							int pos2 = pre.lastIndexOf(";");
							int pos = pos1 > pos2 ? pos1 : pos2;
							pre = pre.substring(0, pos);
						} else {
							pre = "";
						}
						if (last.indexOf(",") > 0 || last.indexOf(";") > 0) {
							int pos1 = last.indexOf(",");
							int pos2 = last.indexOf(";");
							int pos = pos1;
							if (pos1 > -1 && pos2 > -1) {
								pos = pos1 < pos2 ? pos1 : pos2;
							} else {
								pos = pos1 == -1 ? pos2 : pos1;
							}
							last = last.substring(pos + 1);
						} else {
							last = "";
						}
						bean.setMailTo(pre.length() > 0 ? (pre + ";" + last) : last);
					}
					bean.setMailTitle("Re：" + bean.getMailTitle());
					bean.setMailAttaches(null);
				} else if (type.startsWith("transmit")) {
					bean.setMailTo("");
					bean.setMailTitle("Fw"
							+ "：" + bean.getMailTitle());
					//处理附件，如果有eml文件，则附件加上eml的路径
					///ReadFile?tempFile=email&emlfile=685446d5_1204181739334790045.eml&charset=utf-8&attPath=16498250_1107&fileName=113150732.png
					if(bean.getMailAttaches() != null && bean.getMailAttaches().length() >0 && bean.getEmlfile() != null && bean.getEmlfile().length() > 0){
						String ma = "";
						for(String s: bean.getMailAttaches().split(",|;")){
							ma +="emlfile="+bean.getEmlfile()+"&charset="+bean.getMailcharset()+"&attPath="+bean.getAccount()+"&fileName="+s+";";
						}
						bean.setMailAttaches(ma);
					}
				}

				bean.setId("");
				if(!type.equals("revertAll")){
					bean.setMailBCc("");
					bean.setMailCc("");
				}
				bean.setSendeMailType(emailType);
				if(type.equals("transmitOut")){
					bean.setSendeMailType("111");
				}else if(type.equals("transmitInt")){
					bean.setSendeMailType("");
				}
				request.setAttribute("result", bean);
			}
		}else{
			OAMailInfoBean bean = new OAMailInfoBean();
			StringBuffer content = new StringBuffer();
			content.append("<br/><br/><br/><br/><br/><br/><br/>\r\n\r\n\r\n<div><!-- SIGN_START -->"+(accountInfoBean==null||accountInfoBean.getSignature()==null||accountInfoBean.getSignature().length()==0?"":"------------------------------------\r\n<br/>"+accountInfoBean.getSignature()+"<br/>\r\n------------------------------------")+"<!-- SIGN_END --></div>");
			bean.setMailContent(content.toString());
			bean.setSendeMailType(emailType);
			request.setAttribute("result", bean);
			
			Result rs = mgt.getALLMailinfoSetting(lg.getId());
			request.setAttribute("MailinfoSetting",rs.getRetVal());
		}
		
		return getForward(request, mapping, "MailWrite");
	}
	
	protected ActionForward importPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		String userId = this.getLoginBean(request).getId();
		Result result = mgt.selectAccountByUser(userId);
		request.setAttribute("outteremail", result.getRetVal());
		request.setAttribute("importType", request.getParameter("importType"));
		// 查找所有的自定义文件夹
		result = mgt.selectGroupByUser(userId);
		request.setAttribute("emailgroup", result.getRetVal());
		String emailType = request.getParameter("emailType"); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		request.setAttribute("groupId", groupId);
		return getForward(request, mapping, "Import");
	}
	
	protected ActionForward importMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean user = (LoginBean) request.getSession().getAttribute("LoginBean");
		String userId = user.getId();
		
		ImportForm myForm = (ImportForm)form;
		
		String emailType = request.getParameter("emailType"); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		request.setAttribute("groupId", groupId);
		
		
		ActionForward forward = null;
		int rs[]=new int[]{0,0,0};
		if(myForm.getImportType().equals("backup")){
			 rs = mgt.importFromAIO(myForm, emailType,groupId,userId);
		}else{
			rs = mgt.importStandand(myForm, emailType,groupId,userId);
		}
		// 返回路径
		String backUrl="/EMailQueryAction.do?operation="
			+ OperationConst.OP_QUERY + "&emailType="
			+ emailType + "&type=main&groupId=" + groupId;
		//  自动跳转
		String javaScript="<script type='application/javascript'>"
			             +"var timenum=5;var stop;"
			             +"function counttime(){document.getElementById('timenum').innerHTML=timenum;"
			             +" if(timenum==0){ window.clearInterval(stop);"
			             +" window.open('"+backUrl+"','_self');"
			             +"}timenum=timenum-1;}"
			             +"window.onload=function(){"
			             +" stop=window.setInterval('counttime()',1000);}"
			             +"</script>";
			       
		EchoMessage.success()
		.add(this.getMessage(request, "mail.msg.importsuccees",rs[0]+"",rs[1]+"",rs[2]+"")+"<br /> 还有<label id='timenum'  style='color:#F00; font-size:24px; font-weight:bold;'>5</label>秒自动返回"+javaScript )
		.setBackUrl(backUrl).setNotAutoBack()
		.setAlertRequest(request);
		return getForward(request,mapping,"alert");
		
		
	}

	/**
	 * 发送邮件 1、保存到草稿箱，则不执行发送动作， 2、直接发送，则保存到发件箱并执行发送动作。
	 * 3、从草稿箱打开，则保存到发件箱，并删除草稿箱，并发送 4、从草稿箱打开，并保存为草稿，则删除原草稿
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
	 * @throws Exception
	 */
	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EMailForm mailForm = (EMailForm) form;
		boolean begReplay = "1".equals(mailForm.getBegReplay());		//请求阅读收条
		String emailType = request.getParameter("emailType"); 			//帐号类型,除内部外，表明内部邮箱的帐号代号
		
		String moreMail = request.getParameter("moreMail");
		if(moreMail == null || moreMail.length() ==0){
			moreMail = emailType;
		}
		String saveType = request.getParameter("saveType");				//邮件保存方式(save直接发送，draft存为草稿)
		
		String isOAEmail = getParameter("isOAEmail", request);
		String userId = this.getLoginBean(request).getId();

		/* 保存数据到Bean中 */
		OAMailInfoBean mailInfo = new OAMailInfoBean();
		String id = IDGenerater.getId();
		mailInfo.setId(id);
		mailInfo.setBegReplay(mailForm.getBegReplay());
		mailInfo.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		mailInfo.setEmailType((emailType == null || emailType.length() == 0 || emailType.equals("inner")) ? 0 : 1);
		mailInfo.setSendeMailType(request.getParameter("sendEmailType"));
		mailInfo.setFromUserId(userId); //发件人代号
		mailInfo.setCollectionType("0");
		mailInfo.setGroupId("draft".equals(saveType) ? "2" : "3"); //如果是保存为草稿，则存入草稿箱，否则为发件箱

		String attachPath = emailType;//以邮箱代号为路径
		if (emailType == null || emailType.length() == 0 || emailType.equals("inner")) {
			attachPath = "inner" + this.getLoginBean(request).getId();
		}
		mailInfo.setMailSize(mailForm.getMailContent() == null ? 0 : mailForm.getMailContent().length());

		// 附件
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/email/" + attachPath + "/" + del);
				aFile.delete();
			}
		}		
		mailInfo.setMailAttaches(mailAttaches);		//保存附件
		
		//计算附件大小
		String sts[] =(mailAttaches ==null?"": mailAttaches).split(";");
		for(String st:sts){
			if(st.indexOf("emlfile") != -1 && st.substring(st.indexOf("emlfile=")+"emlfile=".length(),st.indexOf("&charset")).length() > 0){
				//emlfile=c5daa3c8_1205141821114950004.eml&charset=&attPath=c5daa3c8_1205141821011640001&fileName=QQ截图20120113150732(1).png
				int pos = st.indexOf("emlfile=")+"emlfile=".length();
				String emlfile = st.substring(pos,st.indexOf("&",pos));
				pos = st.indexOf("attPath=")+"attPath=".length();
				String attPath = st.substring(pos,st.indexOf("&",pos));
				pos = st.indexOf("fileName=")+"fileName=".length();
				String fileName = st.substring(pos);
				InputStream is = AIOEMail.readFileFormEml(BaseEnv.FILESERVERPATH+"/email/"+attPath+"/eml/"+emlfile,fileName, request.getParameter("charset"));		  
				mailInfo.setMailSize(mailInfo.getMailSize()	+ is.available());
			}else{
				File aFile = new File(BaseEnv.FILESERVERPATH + "/email/" + attachPath + "/" + st);
				mailInfo.setMailSize(mailInfo.getMailSize()	+ (int)aFile.length());
			}
		}
		int mailSize = mailInfo.getMailSize();
		
		mailInfo.setMailFrom(this.getLoginBean(request).getEmpFullName());			//保存邮件发送者

		
		String isPart = request.getParameter("isPart");
		if(isPart != null && "1".equals(isPart)){
			//启用分别发送邮件时，把接收人全部放到密送中，接收人和抄送人清空
			mailForm.setBcc(mailForm.getTo());
			mailForm.setCc("");
			mailForm.setTo("");
		}
		
		// 接收人
		mailInfo.setMailCc(mailForm.getCc());
		//抄送人
		mailInfo.setMailTo(mailForm.getTo());
		//密送人
		mailInfo.setMailBCc(mailForm.getBcc());
		//内容
		mailInfo.setMailContent(mailForm.getMailContent());
		//发件时间
		mailInfo.setMailTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		//主题
		mailInfo.setMailTitle(mailForm.getMailTitle());
		mailInfo.setState(1); // 发件时不存在新邮件
		// mailInfo.setToUserId(userId); //发送给自已的邮件
		mailInfo.setUserId(userId);
		mailInfo.setAccount(emailType);
        
		//内部邮件对收件人进行判断是否在职员表和部门表中存在相应的记录，如果不存在，不发送邮件
		if(mailInfo.getEmailType()==0){
			ArrayList mailList = new ArrayList();			//保存可以发送的收件人
			//对收件人进行匹配
			String errorEmail = innerMailParse(mailInfo.getMailTo() + ";" + mailInfo.getMailCc() + ";" + mailInfo.getMailBCc(),mailList);
			if (errorEmail != null && errorEmail.length() > 0) {
				String msg = errorEmail + this.getMessage(request, "oa.mail.receiverError")+","+this.getMessage(request,"email.msg.savetodraft");
				request.setAttribute("MESSAGE_AUTOBACK", "0");
				if("true".equals(request.getParameter("fixUser"))){
					EchoMessage.error().add(msg).setClose().setAlertRequest(request);
				}else{
					//添加失败
		 			EchoMessage.error().add(msg).setAlertRequest(request);
		 			return getForward(request, mapping, "alert");
				}
				return getForward(request,mapping,"message");
			}
		}
		
		// 保存到数据库中去
		Result rs = mgt.addMail(mailInfo);		
		
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			throw new BusinessException(this.getMessage(request,
					"common.msg.sendError"), "/EMailQueryAction.do?operation="
					+ OperationConst.OP_QUERY + "&emailType=" + moreMail
					+ "&type=main&groupId=1");
		}
		//记录回复时间
		String replayId = request.getParameter("replayId");
		if(replayId != null && replayId.length() >0){
			mgt.updateReplayDate(replayId);
		}
		//记录转发时间
		String revolveId = request.getParameter("revolveId");
		if(revolveId != null && revolveId.length() >0){
			mgt.updateRevolveIdDate(revolveId);
		}
		
		// 如查是从草稿箱打开，则删除原草稿
		String oldDraftid = request.getParameter("oldDraftid");
		if (oldDraftid != null && oldDraftid.length() > 0) {
			mgt.delDrafMail(oldDraftid);
		}
		// 如果保存为草稿，则返回草稿箱
		if ("draft".equals(saveType)) {
			EchoMessage.success().add(
					this.getMessage(request, "oa.mail.saveDraftSuccess"))
					.setBackUrl(
							"/EMailQueryAction.do?operation="
									+ OperationConst.OP_QUERY + "&emailType="
									+ moreMail + "&type=main&groupId=2")
					.setAlertRequest(request);
					/*this.getMessage(request, "oa.mail.saveDraftSuccess"))
					.setClose().setRefresh(true).setAlertRequest(request);*/
			return getForward(request, mapping, "alert");
		}

		String errorEmail = "";
		String tableName = getParameter("tableName", request);
		String backUrl = "";
		if ("no".equals(isOAEmail)) {
			backUrl = "/UserFunctionQueryAction.do?operation="
					+ OperationConst.OP_QUERY + "&emailType=outter&tableName="
					+ tableName + "&checkTab=Y";
		} else {
			backUrl = "/EMailQueryAction.do?operation="
					+ OperationConst.OP_QUERY + "&emailType=" + moreMail
					+ "&type=main&groupId=1&clearSerchForm=yes";
		}

		String fromPage = request.getParameter("fromPage");
		if("addFriend".equals(fromPage)){
			backUrl = "/OAUserGroup.do?type=userManager&handetype=addFirend";
		}else if("myFriend".equals(fromPage)){
			backUrl = "/OAUserGroup.do?type=userManager";
		}
		if ("outter".equals(request.getParameter("sendto"))) {
			// 发送外部邮件
			try {
				// 指定帐号和密码
				EMailMessage smes = new EMailMessage();
				smes.setBegReplay(begReplay);
				// 增加收件人
				ArrayList mailList = new ArrayList();
				if (null != mailForm.getTo() && !"".equals(mailForm.getTo())) {
					errorEmail += this.mailParse(mailForm.getTo(), mailList);
					for (Object toTarget : mailList) {
						Object[] ms = (Object[]) toTarget;
						if (!ms[0].equals("")) {
							smes.addRecipient(EMailMessage.TO,
									ms[0].toString(), ms[1].toString());
						} else {
							errorEmail += ms[1] + ";";
						}
					}
				}

				// 增加密送
				mailList.clear();
				if (null != mailForm.getBcc() && !"".equals(mailForm.getBcc())) {
					errorEmail += this.mailParse(mailForm.getBcc(), mailList);
					for (Object toTarget : mailList) {
						Object[] ms = (Object[]) toTarget;
						if (ms[0] != null && !ms[0].toString().equals("")) {
							smes.addRecipient(EMailMessage.BCC, ms[0]
									.toString(), ms[1].toString());
						} else {
							errorEmail += ms[1].toString() + ";";
						}
					}
				}

				// 增加抄送
				mailList.clear();
				if (null != mailForm.getCc() && !"".equals(mailForm.getCc())) {
					errorEmail += this.mailParse(mailForm.getCc(), mailList);
					for (Object toTarget : mailList) {
						Object[] ms = (Object[]) toTarget;
						if (ms[0] != null && !ms[0].toString().equals("")) {
							smes.addRecipient(EMailMessage.CC,
									ms[0].toString(), ms[1].toString());
						} else {
							errorEmail += ms[1].toString() + ";";
						}
					}
				}

				if (errorEmail != null && errorEmail.length() > 0) {
					throw new BusinessException("邮件地址格式"+errorEmail+"不正确;邮件已存入草稿箱",backUrl); 
				}
				
				// 增加附件

				if (mailAttaches != null && mailAttaches.length() > 0) {
					for (String of : mailAttaches.split(";")) {
						if (of != null && of.length() > 0){
							///ReadFile?tempFile=email&emlfile=685446d5_1204181739334790045.eml&charset=utf-8&attPath=16498250_1107&fileName=113150732.png
							//"emlfile="+bean.getEmlfile()+"&charset="+bean.getMailcharset()+"&attPath="+bean.getAccount()+"&fileName="+s+";";
							
							
							if(of.indexOf("&fileName=") > 0){
								//这是从eml文件中取数据
								int pos = of.indexOf("emlfile=")+"emlfile=".length();
								String emlfile = of.substring(pos,of.indexOf("&",pos));
								pos = of.indexOf("charset=")+"charset=".length();
								String charset = of.substring(pos,of.indexOf("&",pos));
								pos = of.indexOf("attPath=")+"attPath=".length();
								String attPath = of.substring(pos,of.indexOf("&",pos));
								pos = of.indexOf("fileName=")+"fileName=".length();
								String fileName = of.substring(pos);
								InputStream is = AIOEMail.readFileFormEml(BaseEnv.FILESERVERPATH+"/email/"+attPath+"/eml/"+emlfile,fileName,charset);		  
				        		smes.putFile(fileName,is);
							}else{
								smes.putFile(of, new File(BaseEnv.FILESERVERPATH + "/email/" + attachPath + "/" + of));
							}
						}
					}
				}
				smes.setSubject(mailForm.getMailTitle());
				smes.setContent(true, mailForm.getMailContent());
				smes.setDate(new Date());
				
				
				String sendEmailType = request.getParameter("sendEmailType");
				//发送邮件，如果发送人多于10个采用异步发送
				if(smes.getRecipientList().size()>=10){
					/***************邮件都 改为异步发送*********************/					
					mgt.sendByThread(smes, sendEmailType, mailInfo.getId(), userId); 
				}else{
					//查询帐号信息
					rs = mgt.loadAccount(sendEmailType);
					if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
						throw new BusinessException(this.getMessage(request,"common.msg.sendError"),
								"/EMailQueryAction.do?operation=" + OperationConst.OP_QUERY + "&emailType=" + moreMail + "&type=main&groupId=1");
					}
					MailinfoSettingBean setting = (MailinfoSettingBean) rs.getRetVal();
					
					AIOEMail sm = new AIOEMail();
					boolean smtpauth = setting.getSmtpAuth() == 1 ? true : false;
					// 指定要使用的邮件服务器
					sm.setAccount(setting.getMailaddress(),setting.getPop3server(), setting.getPop3username(),
							setting.getPop3userpassword(), smtpauth, setting.getSmtpserver(), setting.getSmtpusername(),
							setting.getSmtpuserpassword(), setting.getPop3Port(),setting.getSmtpPort(), setting.getDisplayName(),
							attachPath,setting.getCreateby(),setting.getPopssl()==1?true:false,
									setting.getSmtpssl()==1?true:false,setting.getAutoAssign()==1?true:false);
					
					//调用发送邮件接口
					sm.send(smes,getLoginBean(request).getId());
				}

			} catch (AuthenticationFailedException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				mailInfo.setGroupId("2");// 改为草稿
				mgt.updateMail(mailInfo);
				throw new BusinessException(
						this.getMessage(request,"oa.mail.send.AuthenticationFailture")+","+this.getMessage(request,
						"email.msg.savetodraft"), backUrl);
			} catch (MessagingException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				mailInfo.setGroupId("2");// 改为草稿
				mgt.updateMail(mailInfo);
				throw new BusinessException(this.getMessage(request,"oa.mail.receive.ConnFailture")+","+this.getMessage(request,
				"email.msg.savetodraft"),
						backUrl); 
			}catch(BusinessException ex){ 
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				mailInfo.setGroupId("2");// 改为草稿
				mgt.updateMail(mailInfo);
				throw new BusinessException(this.getMessage(request,"common.msg.mailsendError")+","+ex.key,
						backUrl); 
			} catch (Exception ex) {
				AIOEMail.emailLog.error("EMailAction.add SendMail Error ", ex);
				mailInfo.setGroupId("2");// 改为草稿
				mgt.updateMail(mailInfo);
				throw new BusinessException(this.getMessage(request,
						"common.msg.mailsendError")+","+this.getMessage(request,
						"email.msg.savetodraft"), backUrl);
			}
			//添加发送人历史记录
			mgt.emailSendHistory(mailInfo);
		} else {
			//内部邮件发送
			Map<String,String> map=new HashMap();
			map.put("sendEmailType", request.getParameter("sendEmailType"));
			map.put("charset", request.getParameter("charset"));
			map.put("mailAttaches",mailAttaches );
			map.put("mailSize",mailSize+"");
			map.put("attachPath",attachPath);
			map.put("oa.mail.email",getMessage(request, "oa.mail.email"));
			map.put("emailType",emailType);
			map.put("oa.mail.myMail",getMessage(request, "oa.mail.myMail"));
			map.put("oa.mail.email",getMessage(request, "oa.mail.email"));
			LoginBean loginBean=this.getLoginBean(request);
			EMailSendThread send=new EMailSendThread(mailInfo,mailForm,loginBean,map);
			send.start();
		}
		String msg = "";
		if (emailType != null && emailType.length() > 0 && !emailType.equals("inner")) {
			if("save".equals(saveType)){
				msg = this.getMessage(request, "oa.mail.sendSuccess");
			}else{
				msg = this.getMessage(request, "oa.mail.acceptsend"); 
			}
		}else{
			msg = this.getMessage(request, "oa.mail.sendSuccess");
		}
		if("true".equals(request.getParameter("fixUser"))){
			EchoMessage.success().add(msg).setClose().setAlertRequest(request);
		}else{
			// 添加成功
			EchoMessage.success().add(msg).setBackUrl(backUrl).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}

	/**
	 * 分析字符串中的人员信息。 返回邮件分析不成功的地址信息. 邮件匹配成功放入list中 匹配规则，是先匹配，部门，匹配成功，则给部门所人有员发送信息
	 * 失败再匹配职员列表，匹配成功，则取职员信息， 匹配不上职员，则反回匹配不成功
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	private String innerMailParse(String str, ArrayList list) {
		String emails[] = str.split(",|;");
		String error = "";
		HashMap map = new HashMap();
		for (String email : emails) {
			if (email != null && email.length() > 0&& !"null".equals(email)) {
				// 从部门表中查询
				Result rs = mgt.selectDeptUser(email);
				if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
						&& ((ArrayList) rs.getRetVal()).size() > 0) {
					for (Object o : (ArrayList) rs.getRetVal()) {
						Object[] os = (Object[]) o;
						map.put(os[0], os[0]);
					}
				} else {
					// 从职员表中查询
					rs = mgt.selectUser(email);
					if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
							&& ((ArrayList) rs.getRetVal()).size() > 0) {
						for (Object o : (ArrayList) rs.getRetVal()) {
							Object[] os = (Object[]) o;
							map.put(os[0], os[0]);
						}
					} else {
						error += email + ";";
					}
				}
			}
		}
		list.addAll(map.keySet());
		return error;
	}

	/**
	 * 分析字符串中的邮件信息。 返回邮件分析不成功的地址信息. 邮件匹配成功放入list中
	 * 匹配规则，是先匹配，格式是否为email格式，成功则认为是正确的邮件 失败再匹配联系人列表，匹配成功，则取联系人邮件信息，
	 * 匹配不上联系人，则反回匹配不成功
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	private String mailParse(String str, ArrayList list) {
		String emails[] = str.split(";");
		String error = "";
		HashMap map = new HashMap();
		for (String email : emails) {
			if (email != null && email.length() > 0) {
				email = email.trim();
				String display = "";
				if (email.indexOf("<") > -1
						&& email.indexOf(">") == email.length() - 1) {
					display = email.substring(0, email.indexOf("<"));
					email = email.substring(email.indexOf("<") + 1, email
							.length() - 1);
				}
				if (isMail(email)) {
					map.put(email, new String[] { email, display });
				} else {
					// 从联第人表中查询
					Result rs = mgt.selectConnector(email);
					if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
							&& ((ArrayList) rs.getRetVal()).size() == 1) {
						for (Object o : (ArrayList) rs.getRetVal()) {
							Object[] os = (Object[]) o;
							map.put(os[0], o);
						}
					}else {
						rs = mgt.selectClientMan(email);
						if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
								&& ((ArrayList) rs.getRetVal()).size() == 1) {
							for (Object o : (ArrayList) rs.getRetVal()) {
								Object[] os = (Object[]) o;
								map.put(os[0], o);
							}
						}else{						
							error += email + ";";
						}
					}
				}
			}
		}
		list.addAll(map.values());
		return error;
	}

	/* 判断是否是邮件 */
	public static boolean isMail(String str) {
		if (str == null || str.length() == 0)
			return true;
		Pattern p = Pattern
				.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 明细
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
	 * @throws Exception
	 */
	protected ActionForward mailDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String nstr = request.getParameter("keyId");
		String detailType = request.getParameter("detailType") !=null ? request.getParameter("detailType") : "false";
		request.setAttribute("detailType", detailType); 
		if("true".equals(request.getParameter("content"))){
			Result rs = mgt.loadMail(nstr);
			String attPath = ((OAMailInfoBean)(rs.retVal)).getAccount();
	    	if(attPath == null || attPath.length() == 0 ){
	    		attPath = "inner"+((OAMailInfoBean)(rs.retVal)).getUserId();
	    	}    	
	    	request.setAttribute("attPath", attPath); 
			request.setAttribute("result", rs.retVal);
			return this.getForward(request, mapping, "mailContent");
		}
		
		//切换语言
		if("charset".equals(request.getParameter("type"))){
			String charset = this.getParameter("charset", request);
			String emailType = this.getParameter("emailType", request);
	
			AIOEMail ae = new AIOEMail();
			File file = new File(BaseEnv.FILESERVERPATH + "/email/" + emailType+"/eml/"+nstr+".eml");
			try{
				EMailMessage em = ae.readFileByCharset(new FileInputStream(file), charset,emailType);
				//执行加载明细
				Result rs = mgt.loadMail(nstr);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){				
					//保存邮件
					OAMailInfoBean mailInfo = (OAMailInfoBean)rs.retVal;
					//附件
					String mailAttaches = "";
					Map map_file = em.getFiles();
					if (map_file != null) {
						Set set = map_file.keySet();
						Iterator iterator = set.iterator();
						while (iterator.hasNext()) {
							String fileName = iterator.next().toString();
							mailAttaches += fileName + ";";
						}
					}
					mailInfo.setMailAttaches(mailAttaches);
	
					// 接收人
					String mto = "";
					String mcc = "";
					String mbcc = "";
					for (EMailMessage.Recipienter rc : em.getRecipientList()) {
						if (rc.type == EMailMessage.TO) {
							mto += rc.toString() + ";";
						} else if (rc.type == EMailMessage.CC) {
							mcc += rc.toString() + ";";
						} else if (rc.type == EMailMessage.BCC) {
							mbcc += rc.toString() + ";";
						}
					}
					mailInfo.setMailCc(mcc);
					mailInfo.setMailTo(mto);
					mailInfo.setMailBCc(mbcc);
	
					mailInfo.setMailContent(em.getContent());
					mailInfo.setMailFrom(em.getSender().toString());
					mailInfo.setMailTitle(em.getSubject());
	
					// 保存到数据库中去
					EMailMgt mgt = new EMailMgt();									
					//保存eml原文和语言
					mailInfo.setMailcharset(charset);
					mgt.updateMail(mailInfo);	
				}
			}catch(Exception e){
			}
		}
		//String emailType = request.getParameter("emailType"); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		String groupId = request.getParameter("groupId");
		//request.setAttribute("emailType", emailType); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		request.setAttribute("groupId", groupId);
		request.setAttribute("keyId", nstr);
		request.setAttribute("iframe", request.getParameter("iframe"));

		if (nstr == null || nstr.length() == 0) {
			//加载失败
			throw new BusinessException("common.msg.error","/EMailQueryAction.do?operation="
							+ OperationConst.OP_QUERY + "&emailType="
							+ request.getParameter("emailType") + "&type=main&groupId=" + groupId);
		}
		
		// 执行加载明细
		Result rs = mgt.loadMail(nstr);
		if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// 记录不存在错误
			String tips="<script language='javascript'> ";
			tips += "alert('该邮件已经彻底删除');";
			tips += "parent.delselfById('"+nstr+"');";
			tips +="</script>";
			request.setAttribute("msg", tips);
			return getForward(request, mapping, "blank");
//			throw new BusinessException("common.error.nodata",
//					"/EMailQueryAction.do?operation="
//							+ OperationConst.OP_QUERY + "&emailType="
//							+ request.getParameter("emailType") + "&type=main&groupId=" + groupId);
		   
		} else if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			// 加载失败
			throw new BusinessException("common.msg.error",
					"/EMailQueryAction.do?operation="
							+ OperationConst.OP_QUERY + "&emailType="
							+ request.getParameter("emailType") + "&type=main&groupId=" + groupId);
		}

		// 加载成功
		// 修改已经查看的email的状态
		OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;
		
		//假如在收件箱中，点击删除的邮件（不属于彻底删除）提示
		if("1".equals(groupId)&&bean.getGroupId()!=null && bean.getGroupId().equals("5")){
						String tips="<script language='javascript'> ";
						tips += "alert('该邮件已经删除');";
						tips += "parent.delselfById('"+nstr+"');";
						tips +="</script>";
						request.setAttribute("msg", tips);
						return getForward(request, mapping, "blank");
		}
		request.setAttribute("result", bean);
		if(bean.getState() != 1 && bean.getUserId().equals(this.getLoginBean(request).getId())){
			bean.setState(1);
			bean.setReplayDate(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));////mj标识查看该邮件的时间 在查看收件人的时候有用
			mgt.updateMail(bean);
			//修改消息状态
//			mgt.delAdvice(bean);
			new AdviceMgt().deleteByRelationId(bean.getId(), "");
		}
		
		String attPath = bean.getAccount();
    
    	if (attPath == null || attPath.length() == 0) {
			attPath = "inner" + bean.getUserId();
			String pId = bean.getId();
			// 根据该id查询得到多有的收件人邮件信息(收件人姓名 所属部门 阅读时间 是否阅读) mj
			rs = mgt.queryReceives(pId, "","");// 默认top5 只能查询所有的数据出来，不能默认。发送人 有部门有个人
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("receives",  rs.getRetVal());
			} else if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				AIOEMail.emailLog.error("查询收件人信息发送错误！");
			}
		}
    	request.setAttribute("attPath", attPath); 

		String hide = BaseEnv.systemSet.get("HideEmail").getSetting(); // 是否隐藏邮件地址
		request.setAttribute("hide", hide);
		
		String emailType = bean.getAccount();
		request.setAttribute("emailType", emailType); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		
		
		String userId = ((LoginBean) request.getSession().getAttribute("LoginBean")).getId();		
		if (emailType != null && emailType.length() > 0) {// 外部邮件
			Result r = mgt.getNoteNameByEmail(userId,"'"+GlobalsTool.getMailAddress(bean.getMailFrom())+"'");
			request.setAttribute("noteMap", r.getRetVal());
			r = mgt.getClientByEmail("'"+GlobalsTool.getMailAddress(bean.getMailFrom())+"'");
			request.setAttribute("clientMap", r.getRetVal());	
		}
		Result r = mgt.getAssignById("'"+bean.getId()+"'");
		request.setAttribute("assignMap", r.getRetVal());
		if ("2".equals(groupId)) {	
			// 如果是草稿箱，则直接打开修改界面
			Result rs2;		
			request.setAttribute("groupId", "2");
			if (emailType != null && emailType.length() > 0) {// 外部邮件
				rs2 = mgt.selectConnectorName(bean.getMailFrom());
				request.setAttribute("moreMail",emailType );
			} else {// 内部邮件
				rs2 = mgt.selectUserName(bean.getFromUserId());
				request.setAttribute("moreMail", "inner");
			}
			if (rs2.retCode == ErrorCanst.DEFAULT_SUCCESS
					&& rs2.retVal != null && ((List) rs2.retVal).size() > 0) {
				bean.setMailFrom(((Object[]) ((List) rs2.retVal).get(0))[0]
								.toString());
			}
			if (bean.getMailFrom() == null	|| bean.getMailFrom().length() == 0) {
				bean.setMailFrom(this.getLoginBean(request)
						.getEmpFullName());
			}
			//修改时取邮箱信息
			EMailMgt mgt = new EMailMgt();
			Result rso = mgt.getALLMailinfoSetting(userId);
			Object o = rso.getRetVal();
			if(o != null){
				ArrayList MailinfoSetting =(ArrayList) o;
				request.setAttribute("MailinfoSetting",MailinfoSetting);
			}
			
			//以邮箱代号为路径
			String attachPath = emailType;
			if (attachPath == null || attachPath.length() == 0 || attachPath.equals("inner")) {
				attachPath = "inner" + this.getLoginBean(request).getId();
			}
			request.setAttribute("path", "/email/"+attachPath); //附件保存的路径
		}else{
			//查询上一条和下一条数据
			String createTime = request.getParameter("createTime");									//当条的创建时间
			String emailSql = (String)request.getSession().getAttribute("EmailSql");				//查询的sql语句
			List emailParam = (ArrayList)request.getSession().getAttribute("EmailParam"); 			//sql语句对应的参数
			
			if(createTime != null && emailSql != null && emailParam != null){
				/* 查询上一条 */
				ArrayList preNextData = (ArrayList)mgt.queryPreNextData(emailSql, emailParam, "pre", createTime).retVal;
				if(preNextData != null && preNextData.size() == 1){
					request.setAttribute("preData", preNextData.get(0));
				}
				/* 查询下一条 */
				preNextData = (ArrayList)mgt.queryPreNextData(emailSql, emailParam, "next", createTime).retVal;
				if(preNextData != null && preNextData.size() == 1){
					request.setAttribute("nextData", preNextData.get(0));
				}
			}
		}

		if (!"2".equals(groupId)) {
			//不为草稿修改时,查询所有的标签显示
			//标签
			request.setAttribute("labelMap", EMailSettingMgt.getMailLabelMap());
			request.setAttribute("labelList", EMailSettingMgt.getMailLabels());

		}
		/*判断是否存提醒设置*/
		Result result = mgt.loadAlertByEamilId(nstr) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("alert", result.retVal) ;
		}
		
		if ("2".equals(groupId)) {
			return getForward(request, mapping, "MailWrite");
		} else {
			if("true".equals(request.getParameter("iframe"))){
				request.setAttribute("iframe", true);
			}
			if("true".equals(request.getParameter("newOpen"))){
				request.setAttribute("newOpen", true);
			}
			bean.setMailContent(handUrl(bean.getMailContent()));
			return getForward(request, mapping, "mailDetail");
		}
	}
	//处理邮件的url ,全部为弹出方式
	private String handUrl(String str){	
		if(str == null || str.length() ==0) return "";
		Pattern pattern = Pattern.compile("<a([^><]*)>[^><]*</a>",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){              
		    String group = matcher.group(1);   
		    if(group.toLowerCase().indexOf("javascript") > -1){
		    	continue; //执行函数的邮件链接不能修改
		    }
		    String oldGroup = group;
		    if(group.toLowerCase().indexOf("target") >0){
		    	Pattern p1 = Pattern.compile("target[\\s]*=[\\s]*['|\"]{0,1}[\\S]*['|\"]{0,1}[\\s]*",Pattern.CASE_INSENSITIVE);
		    	Matcher m1 = p1.matcher(group);		    	
				group =m1.replaceAll(" target=_blank ");
		    }else{
		    	group +=" target=_blank ";
		    }
		    int pos = str.indexOf("<a"+oldGroup);
		    if(pos == -1){
		    	pos = str.indexOf("<A"+oldGroup);
		    }
		    if(pos > -1){
		    	str = str.substring(0,pos)+"<a t=t"+group+str.substring(pos+("<a"+oldGroup).length());
		    }
		    
//		    Pattern p1 = Pattern.compile("<a"+oldGroup,Pattern.CASE_INSENSITIVE);
//	    	Matcher m1 = p1.matcher(str);
//			str = m1.replaceFirst("<a t=t"+group);
		}
		 
		return str;
	}

	private String displayAddress(String str) {
		if (str == null)
			return "";
		str = str.replace(";", "&*");
		String[] strs = str.split(";|,");
		String rs = "";
		for (String s : strs) {
			if (s != null && s.length() > 0) {
				rs += ";<a title='" + GlobalsTool.encodeHTML(s) + "'>" + s
						+ "</a>";
			}
		}
		rs = rs.replace("&*", ";");
		if (rs.length() > 0)
			return rs.substring(1);
		else
			return rs;
	}

	private String displayAddress(String str, String emailAddress,String userId) {

		if (!"1".equals(userId)) {
			return displayAddress(str);
		}
		if (str == null)
			return "";
		str = str.replace(";", "&*");
		String[] strs = str.split(";|,");
		String rs = "";
		for (String s : strs) {
			if (s != null && s.length() > 0) {
				rs += ";<a href='/OACommunication.do?operation=4&type=Communication&optiontype=addPrepare&emailAddress="
						+ emailAddress
						+ "' title='"
						+ GlobalsTool.encodeHTML(s) + "'>" + s + "</a>";
			}
		}
		rs = rs.replace("&*", ";");
		if (rs.length() > 0)
			return rs.substring(1);
		else
			return rs;
	}

	/**
	 * 文件夹管理
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
	private ActionForward groupManage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();

		String op = request.getParameter("op");
		if ("prepare".equals(op)) {
			String id = request.getParameter("groupId");
			if (id != null && id.length() > 0) {
				Result rs = mgt.loadGroup(id);
				request.setAttribute("result", rs.retVal);
				request.setAttribute("op", "update");
			} else {
				request.setAttribute("op", "add");
			}
			Result result = mgt.selectAccountByUser(userId);
			request.setAttribute("outteremail", result.getRetVal());
			return getForward(request, mapping, "mailGroup");
		}
		if ("add".equals(op) || "update".equals(op)) {
			//添加文件夹或者修改
			String account = request.getParameter("account");
			String groupName = request.getParameter("groupName");
			String id = request.getParameter("id");
			Result rs = null;
			String msg = "add";
			if ("add".equals(op)) {
				id = IDGenerater.getId();
				rs = mgt.addGroup(id, groupName, account, userId);
			}else{
				rs = mgt.updateGroup(id, groupName, account, userId);
				msg= "update";
			}
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				// 处理成功
				request.setAttribute("dealAsyn", "true");
				EchoMessage.success().add(getMessage(request, "common.msg."+msg+"Success")).setAlertRequest(request);
				return getForward(request, mapping, "message");
			} else {
				throw new BusinessException("common.msg."+msg+"Failture",
						"/EMailAction.do?operation=" + OperationConst.OP_QUERY
								+ "&type=groupmanager");
			}
		} else if ("del".equals(op)) {
			// 删除文件夹
			String groupId = request.getParameter("groupId");
			Result rs = mgt.delGroup(groupId);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS
					|| rs.retCode == ErrorCanst.ER_NO_DATA) {
				request.setAttribute("msg", "OK");
			} else if (rs.retCode == ErrorCanst.DATA_ALREADY_USED) {
				request.setAttribute("msg", "EXIST");
			} else {
				request.setAttribute("msg", "ERROR");
			}
			return getForward(request, mapping, "blank");
		} else {
			return groupManageQuery(mapping, form, request, response);
		}
	}
	
	private ActionForward mailCRM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();

		String op = request.getParameter("op");
		if ("prepare".equals(op)) {			
			String conName = request.getParameter("conName");
			try{
			conName = conName==null?"":new String(conName.getBytes("ISO8859-1"),"UTF-8");
			}catch(Exception e ){}
			request.setAttribute("linkman",conName );
			String email = request.getParameter("emailAddress");
			if(email != null){
				if(">".equals(email.substring(email.length()-1))){
					request.setAttribute("email",email.substring(0, email.length()-1));
				}else{
					request.setAttribute("email", email);
				}
			}
				
			return getForward(request, mapping, "MailCRM");
		}
		if ("add".equals(op)) {
			String clientId = request.getParameter("clientId");
			String linkman = request.getParameter("linkman");
			String email = request.getParameter("email");
			Result rs = mgt.addMailCRM(clientId, linkman, email, this.getLoginBean(request).getSunCmpClassCode());
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				// 添加成功
				EchoMessage.success().add(
						getMessage(request, "common.msg.addSuccess"))
						.setClose()
						.setAlertRequest(request);
				return getForward(request, mapping, "alert");
			} else {
				EchoMessage.error().add(
						getMessage(request, "common.msg.addFailture"))
						.setClose()
						.setAlertRequest(request);
			}
		} 
		return getForward(request, mapping, "MailCRM");
	}
	

	
	private void readMailSetting(HttpServletRequest request,MailinfoSettingBean mailSet){
		mailSet.setAccount(request.getParameter("account"));
		mailSet.setDefaultUser("1".equals(request.getParameter("defaultUser"))?"1":"2");
		mailSet.setDisplayName(request.getParameter("displayName"));
		mailSet.setId(request.getParameter("id"));
		mailSet.setMailaddress(request.getParameter("mailaddress"));
		mailSet.setPop3Port(Integer.parseInt(request.getParameter("pop3Port")));
		mailSet.setPop3server(request.getParameter("pop3server"));
		mailSet.setPop3username(request.getParameter("pop3username"));
		mailSet.setPop3userpassword(request.getParameter("pop3userpassword"));
		mailSet.setPopssl("1".equals(request.getParameter("popssl"))?1:2);
		mailSet.setSmtpAuth("1".equals(request.getParameter("smtpAuth"))?1:2);
		mailSet.setSmtpPort(Integer.parseInt(request.getParameter("smtpPort")));
		mailSet.setSmtpsamepop("1".equals(request.getParameter("smtpsamepop"))?1:2);
		mailSet.setSmtpserver(request.getParameter("smtpserver"));
		mailSet.setSmtpssl("1".equals(request.getParameter("smtpssl"))?1:2);
		mailSet.setSmtpusername(request.getParameter("smtpusername"));
		mailSet.setSmtpuserpassword(request.getParameter("smtpuserpassword"));
		try{
			mailSet.setAutoReceive(Integer.parseInt(request.getParameter("autoReceive")));
		}catch (Exception e) {
			mailSet.setAutoReceive(0);
		}
		try{
			mailSet.setRetentDay(Integer.parseInt(request.getParameter("retentDay")));
		}catch (Exception e) {
			mailSet.setRetentDay(0);
		}
	}

	/**
	 * 文件夹管理
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
	private ActionForward groupManageQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId();
		// 显示文件夹列表
		Result rs = mgt.selectGroupByUser(userId);
		request.setAttribute("list", rs.getRetVal());
		return getForward(request, mapping, "mailGroupManage");
	}

	/**
	 * 接收邮件
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
	private ActionForward receiveMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String emailType = request.getParameter("emailType");
		String userId = this.getLoginBean(request).getId();
		Result rs;
		
		String mailOp = request.getParameter("mailOp");
		// 如果emailId为空，说明是系统初始化
		if (mailOp == null || mailOp.length() == 0) {
			
			if("all".equals(emailType)){
				rs = mgt.selectAccountByUser(userId);
			}else{
				rs = mgt.getMailAddress(new String[]{emailType});
			}
			//查询子帐户
			rs = mgt.getMailChildMail((ArrayList<MailinfoSettingBean>)rs.retVal);
			request.setAttribute("emailTypeObj", rs.retVal);
			request.setAttribute("emailType", emailType);
			String oldEmailType = request.getParameter("oldEmailType");
			if(!"all".equals(oldEmailType)&&!"all".equals(emailType)){
				oldEmailType = emailType;
			} 
			request.setAttribute("oldEmailType", oldEmailType);
			
			for(MailinfoSettingBean mmB :(List<MailinfoSettingBean>)rs.retVal){
				//如果邮箱超限直接提示					
				if(!mgt.checkMailSizeList(1, mmB.getId(), userId)){
					throw new BusinessException("("+mmB.getMailaddress()+")"+this.getMessage(request,
					"common.msg.mailSizeLimit"), "/EMailQueryAction.do?operation="
					+ OperationConst.OP_QUERY + "&emailType=" + emailType
					+ "&type=main&groupId=1");
				}
			}
					
			request.setAttribute("emailType", emailType);
			
			return getForward(request, mapping, "MailReceive");
		}
		// 如果emailId为"connectMailSystem"，说明是正在连接系统
		String setId = null;		
		if (mailOp.equals("connectMailSystem")) {
			HashMap smMap = (HashMap)request.getSession().getAttribute("AIOEMAILMAP");
			if(smMap == null){
				smMap = new HashMap();
				request.getSession().setAttribute("AIOEMAILMAP", smMap);
			}
			//如果会话中存在对象，先要关闭会话，再重新开始，连接
			try{
				AIOEMail am = (AIOEMail)smMap.get(emailType);
				if(am !=null){
					am.closeInbox();
				}
			}catch(Exception e){}

			rs = mgt.loadAccount(emailType);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request, "oa.mail.msg.receiveFailture"));
				return getForward(request, mapping, "blank");
			}
			MailinfoSettingBean setting = (MailinfoSettingBean) rs.getRetVal();
			try {
				AIOEMail sm = new AIOEMail();
				boolean smtpauth = setting.getSmtpAuth() == 1 ? true : false;
				// 指定要使用的邮件服务器
				sm.setAccount(setting.getMailaddress(),
						setting.getPop3server(), setting.getPop3username(),
						setting.getPop3userpassword(), smtpauth, setting
								.getSmtpserver(), setting.getSmtpusername(),
						setting.getSmtpuserpassword(), setting.getPop3Port(),
						setting.getSmtpPort(), setting.getDisplayName(),
						setting.getId(),setting.getCreateby(),
						setting.getPopssl()==1?true:false,setting.getSmtpssl()==1?true:false,setting.getAutoAssign()==1?true:false);				

				sm.retentDay = setting.getRetentDay();	
				String[] uids = sm.connect();
				setId = setting.getId();//mj 给setId赋值
				int count = 1000;
				//这里进行邮件，过滤
				while(uids !=null && uids.length > 0){
					String[] tempUids = uids;
					String[] uid = new String[0];
					if(uids.length > count){
						uids = new String[tempUids.length -count];
						uid = new String[count];
						System.arraycopy(tempUids, count, uids, 0, tempUids.length -count);
						System.arraycopy(tempUids, 0, uid, 0, count);
					}else{
						uid = uids;
						uids = new String[0];
					}
					Result result= mgt.handNewMail1000(uid, sm, emailType, setting.getRetentDay());
					
					if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
						AIOEMail.emailLog.info("EMailAction.run receiveMail handNewMail Error "+result.retCode);
						throw new Exception(this.getMessage(request, "email.msg.filterError"));
					}
				}
				
				// 获得该邮箱对应的详细规则信息 mj
				sm.setOfRules = new HashSet<String[]>();//存放所有的规则信息以及分组groupID信息
				Result rsOfFilterInfo = efMgt.getFilterInfoByMsId(setId);
				if (rsOfFilterInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {// 查询成功

						// 得到过滤规则数组
						List<EmailFilter> list = (List<EmailFilter>) rsOfFilterInfo.retVal;
						for (int i = 0; i < list.size(); i++) {
							EmailFilter ef = (EmailFilter) list.get(i);
							String rule = ef.getFilterCondition();
							String groupId = ef.getRefOaFolderId();
							String[] filterArr = rule.split(",|;| |\\n");							
							for (int j = 0; j < filterArr.length; j++) {
								String f = filterArr[j].trim();
								sm.setOfRules.add(new String[]{f,groupId});
							}
						}

				}
				
				/**
				 * 这里计算新邮件，并删除过期的老邮件
				 * 思路为，每封邮件都有GUID唯一编号,将帐户ID和邮件的GUID保留在表 tblMailGUID中。并建立 帐号与guid的联合索引
				 * 每次接收邮件后，将所有邮件的GUID组合成一个查询语句，查询出哪些邮件已经接收过，过滤出接收过的邮件不再接收，
				 * 并运算已接收过的邮件是否超过保留时间，如果超过，则置删除标志。
				 * 这里还需考虑异常情况下tblMailGUID中数据不能正常删除，导致垃圾数据变多性能下降，这里还需一个机制同步删除服务器上已经不存在的GUID,
				 * 但这影响性能，每天做一次这种工作就行，所以记录每个帐号做这工作的时间。
				 */		
				request.setAttribute("msg", sm.newMsgList.size()); //连接邮件，回传邮件数量
				smMap.put(emailType, sm);
				return getForward(request, mapping, "blank");
			} catch (AuthenticationFailedException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request,
								"oa.mail.receive.AuthenticationFailture")+"是否启用SSL安全连接");
				return getForward(request, mapping, "blank");
			} catch (MessagingException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request, "oa.mail.receive.ConnFailture"));
				return getForward(request, mapping, "blank");
			} catch (Exception ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request, "oa.mail.msg.receiveFailture"));
				return getForward(request, mapping, "blank");
			}
		}
		// 如果emailId为"closeMailSystem"，说明是正关闭系统
		if (mailOp.equals("closeMailSystem")) {
			HashMap smMap = (HashMap)request.getSession().getAttribute("AIOEMAILMAP");
			AIOEMail sm = (AIOEMail)smMap.get(emailType);
			if (sm != null) {
				try {
					sm.closeInbox();
					request.setAttribute("msg", "OK");
					AIOEMail.emailLog.debug("EMailAction.receiveMail closeInbox ");
					return getForward(request, mapping, "blank");
				} catch (Exception ex) {
					AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
					request.setAttribute("msg",
							"ERROR:"
									+ getMessage(request,
											"oa.mail.msg.receiveFailture"));
					return getForward(request, mapping, "blank");
				}
			} else {
				request.setAttribute("msg", "ERROR:NULL");
				return getForward(request, mapping, "blank");
			}
		}
		// 接收某一邮件头
		if (mailOp.equals("header")) {
			HashMap smMap = (HashMap)request.getSession().getAttribute("AIOEMAILMAP");
			AIOEMail sm = (AIOEMail)smMap.get(emailType);
			try {
				EMailMessage em = sm.receiveNextHeader();
				if(em == null) {
					request.setAttribute("msg", "ERROR:NULL");
					return getForward(request, mapping, "blank");
				}
				request.setAttribute("msg", "isNew:true;"+em.getMailSize()+";" + em.getSubject());
				return getForward(request, mapping, "blank");
			} catch (AuthenticationFailedException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request,
								"oa.mail.receive.AuthenticationFailture"));
				return getForward(request, mapping, "blank");
			} catch (MessagingException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request, "oa.mail.receive.ConnFailture"));
				return getForward(request, mapping, "blank");
			} catch (Exception ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request, "oa.mail.msg.receiveFailture"));
				return getForward(request, mapping, "blank");
			}
		}
		
		//接收某一邮件
		if (mailOp.equals("body")) {
			HashMap smMap = (HashMap)request.getSession().getAttribute("AIOEMAILMAP");
			AIOEMail sm = (AIOEMail)smMap.get(emailType);
			try {
				long cur = System.currentTimeMillis();
				EMailMessage em = sm.receiveBody(false,"");
				if(em == null) {
					request.setAttribute("msg", "ERROR:NULL");
					return getForward(request, mapping, "blank");
				}
				long time = (System.currentTimeMillis() - cur);
				// 保存邮件
				OAMailInfoBean mailInfo = new OAMailInfoBean();
				mailInfo.setId(IDGenerater.getId());
				mailInfo.setCreateTime(BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss));
				mailInfo.setEmailType(1);
				// mailInfo.setFromUserId();
				// 附件
				String mailAttaches = "";
				Map map_file = em.getFiles();
				if (map_file != null) {
					Set set = map_file.keySet();
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						String fileName = iterator.next().toString();
						mailAttaches += fileName + ";";
					}
				}
				mailInfo.setMailAttaches(mailAttaches);

				String innerAttaches = "";
				if (em.getContentIdList() != null) {
					for (Object o : em.getContentIdList()) {
						String[] strs = (String[]) o;
						innerAttaches += strs[1];
					}
				}
				mailInfo.setInnerAttaches(innerAttaches);
				
				// 接收人
				String mto = "";
				String mcc = "";
				String mbcc = "";
				for (EMailMessage.Recipienter rc : em.getRecipientList()) {
					if (rc.type == EMailMessage.TO) {
						mto += rc.toString() + ";";
					} else if (rc.type == EMailMessage.CC) {
						mcc += rc.toString() + ";";
					} else if (rc.type == EMailMessage.BCC) {
						mbcc += rc.toString() + ";";
					}
				}
				mailInfo.setMailCc(mcc);
				mailInfo.setMailTo(mto);
				mailInfo.setMailBCc(mbcc);

				mailInfo.setMailContent(em.getContent());
				mailInfo.setMailFrom(em.getSender().toString());
				mailInfo.setMailTime(BaseDateFormat.format(em.getDate()==null?new Date():em.getDate(),
						BaseDateFormat.yyyyMMddHHmmss));
				mailInfo.setMailTitle(em.getSubject());
				mailInfo.setState(0); // 新邮件
				mailInfo.setToUserId(userId); // 发送给自已的邮件
				mailInfo.setUserId(userId);
				mailInfo.setAccount(emailType);
				mailInfo.setMailSize(em.getMailSize());
				mailInfo.setMailUID(em.getMessageId());
				mailInfo.setBegReplay(em.isBegReplay()?"1":"0");

				mailInfo.setGroupId(mgt.filterMailFrom(mailInfo)?"1":"4"); //进行规则过滤
				// 保存到数据库中去
				
				boolean mustSave = true;
				
				//如果要保留服务器备份，则需保存guid,这里不管有没有保留服务器备份,都存储guid,因为有些服务器设置删除标识后，不会真的删除
				//if(sm.retentDay != 0){
					Result ru =mgt.addGuid(mailInfo.getAccount(), mailInfo.getMailUID(),mailInfo.getMailTime());
					if(ru.retCode != ErrorCanst.DEFAULT_SUCCESS){
						mustSave = false;
					}
				//}
				if(mustSave){
					//保存eml原文和语言
					mailInfo.setMailcharset(em.getHeadCharset());
					mailInfo.setEmlfile(mailInfo.getId()+".eml");
					
					if(sm.setOfRules != null){
						String maddr = GlobalsTool.getMailAddress(mailInfo.getMailFrom());
						for (Iterator iterator = sm.setOfRules.iterator(); iterator
								.hasNext();) {
							String[] filter = (String[]) iterator.next();
							// 发送邮件的用户的邮箱地址 是否包含在这个规则里面							
							if (StringUtils.isNotBlank(maddr)
									&& maddr.toLowerCase().endsWith(
											filter[0].toLowerCase())) {
								mailInfo.setGroupId(filter[1]);
								break;
							}
	
						}
					}					
					
					Result rsa = mgt.addMail(mailInfo);			
					if(rsa.retCode != ErrorCanst.DEFAULT_SUCCESS){
						//保存成功后，再存文件
						request.setAttribute("msg", "ERROR:"
								+ getMessage(request, "oa.mail.msg.receiveFailture"));
						return getForward(request, mapping, "blank");
					}
					
					File file = new File(BaseEnv.FILESERVERPATH + "/email/" + sm.accountId+"/eml/"+mailInfo.getId()+".eml");
					if(!file.getParentFile().exists()){
						file.getParentFile().mkdirs();
					}
					try{
						sm.curMsg.writeTo(new FileOutputStream(file));
					}catch(Exception e){
						AIOEMail.emailLog.error("无法保存邮件原文",e);
						//e.printStackTrace();
					}
					if(sm.autoAssign){
						String mf = mailInfo.getMailFrom();
						if(mf.indexOf("<")> 0){
							mf = mf.substring(mf.indexOf("<")+1,mf.indexOf(">"));
						}
						String assignusers="";
						Result ownerrs = mgt.getClientOwnerByEmail("'"+mf+"'");
						if(ownerrs.retCode == ErrorCanst.DEFAULT_SUCCESS){
							ArrayList list = (ArrayList)ownerrs.retVal;
							for(int i=0;list != null && i<list.size();i++){
								String uId = list.get(i).toString();
								assignusers += list.get(i)+",";								
							}
						}
						if(assignusers.length() > 0){
							mgt.mailAssign(new String[]{mailInfo.getId()}, emailType, userId, assignusers);
							NotifyFashion notify = new NotifyFashion() ; 
							notify.sendAdvice(sm.accountUserId,
									"RES<mail.msg.assignnewMail>",
									"<a href=\"javascript:mdiwin('/EMailAction.do?operation=5&emailType=assign&noback=true&keyId="+mailInfo.getId()+"&newOpen=true','RES<mail.msg.assignnewMail>')\">"+"RES<mail.msg.assignnewMail></a>",
									assignusers,"","email");
						}
					}
				}				
				
				if (time == 0)
					time = 1;
				long rate = (em.getMailSize() / time);
//				System.out.println(" messageSize=" + em.getMailSize()
//						+ ":time=" + time + ":allTime="
//						+ (System.currentTimeMillis() - cur) + ":rate=" + rate);
				request.setAttribute("msg", "rate:" + rate);
				return getForward(request, mapping, "blank");
			} catch (AuthenticationFailedException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request,
								"oa.mail.receive.AuthenticationFailture"));
				return getForward(request, mapping, "blank");
			} catch (MessagingException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request, "oa.mail.receive.ConnFailture"));
				return getForward(request, mapping, "blank");
			} catch (Exception ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request, "oa.mail.msg.receiveFailture"));
				return getForward(request, mapping, "blank");
			}
		}
		
		//前台转后台运行
		if (mailOp.equals("backgroud")) {
			HashMap smMap = (HashMap)request.getSession().getAttribute("AIOEMAILMAP");
			AIOEMail sm = (AIOEMail)smMap.get(emailType);
			//新启一个线程继续执行动作
			new BackgroundThread(sm,userId,emailType);
			request.setAttribute("msg", "OK");
			return getForward(request, mapping, "blank");			
		}
		
		
		request.setAttribute("msg", "ERROR:NULL");
		return getForward(request, mapping, "blank");
	}

	/**
	 * queryWokFlow
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
	private ActionForward queryEmailList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String newClient = getMessage(request, "com.new.client");
		String emailType = request.getParameter("emailType"); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		String groupId = request.getParameter("groupId");
		request.setAttribute("isprev", request.getParameter("isprev"));
		request.setAttribute("curMailId", request.getParameter("curMailId"));
		//标签
		request.setAttribute("labelMap", EMailSettingMgt.getMailLabelMap());
		request.setAttribute("labelList", EMailSettingMgt.getMailLabels());
		

		EMailSearchForm myForm = (EMailSearchForm) form;
		if (myForm == null)
			myForm = new EMailSearchForm();
		// 邮件类型为空，则显示内部邮件
		if (emailType == null || "inner".equals(emailType)) {
			emailType = "";
		}
		request.setAttribute("emailType", emailType);
		
		// 文件夹为空，则显示收件箱
		if (groupId == null || groupId.length() == 0) {
			groupId = "1";
		}
		request.setAttribute("groupId", groupId);

		String userId = getLoginBean(request).getId();
		
		Result rs;
		if(!"assign".equals(emailType)){
			rs = mgt.selectGroupByAccount(userId, emailType); //为在界面上显示移动至自定义文件夹
			request.setAttribute("groups", rs.retVal);
			//查询帐户信息
			if (emailType != null && !"".equals(emailType)) {
				rs = mgt.loadAccount(emailType);
				request.getSession().setAttribute("accountInfo", rs.retVal);
			}
			if (groupId != null && groupId.length() > 1) {
				rs = mgt.loadGroup(groupId);
				request.setAttribute("groupInfo", rs.retVal);
			}
		}

		String beginTime = myForm.getBeginTime();
		String endTime = myForm.getEndTime();
		String keyword = myForm.getKeyword();
		String highKeyword = myForm.getHighKeyword();//高级查找的关键字
		String searchType = request.getParameter("searchType");//用于判别关键字是从高级进入还是普通
		request.setAttribute("searchType",searchType);
		if("true".equals(searchType)){
			keyword = highKeyword;
			myForm.setKeyword(highKeyword);
		}
		if(keyword !=null){
			keyword = keyword.trim();
		}
		rs = mgt.queryMail(userId, emailType, groupId, keyword,
						myForm.getEmail(),myForm.getView(),myForm.getOrderby(),myForm.getIsdesc(),myForm.getLabelId(), myForm.getPageNo(), myForm
								.getPageSize(),beginTime,endTime,searchType);	
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 查询成功
			Object[] obj = (Object[])rs.retVal;
			String noReadSum = String.valueOf(obj[0]);
			request.setAttribute("noReadSum", noReadSum);
			LinkedHashMap map = (LinkedHashMap) obj[1];
			
			/**
			 * 保存需要在详情中的上一条和下一条的sql语句和参数
			 */
			request.getSession().setAttribute("EmailSql", obj[2]);
			request.getSession().setAttribute("EmailParam", obj[3]);
			
			
			String hide = BaseEnv.systemSet.get("HideEmail").getSetting(); // 是否隐藏邮件地址
			request.setAttribute("hide", hide);
			
			EMailMgt mgt = new EMailMgt();
			
			Result r = null;
			
			if (emailType != null && emailType.length() > 0) {
				//外部邮件，查询所有的个人通迅录，和所有公共通迅录
				//将所有收件人地址合在一起，准备查询联系人
				String mailFrom = "";
				String mailIds = "";
				
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()){
					Entry entry = (Entry)iter.next();
					List list = (ArrayList)entry.getValue();
					for (int i = 0; i < list.size(); i++) {
						OAMailInfoBean bean = (OAMailInfoBean) list.get(i);
						String ad ;
						if("2".equals(groupId) || "3".equals(groupId)){
							ad= GlobalsTool.getMailAddress(bean.getMailTo());
						}else{
							ad= GlobalsTool.getMailAddress(bean.getMailFrom());
						}					
						if(ad != null && ad.trim().length() >0){
							mailFrom += "'" + ad + "',";
						}
						mailIds += "'"+bean.getId()+"',";
					}
				}
				if (mailFrom.length() > 0) {
					mailFrom = mailFrom.substring(0, mailFrom.length() - 1);
				}	
				if (mailIds.length() > 0) {
					mailIds = mailIds.substring(0, mailIds.length() - 1);
				}	
				r = mgt.getNoteNameByEmail(userId,mailFrom);
				request.setAttribute("noteMap", r.getRetVal());
				r = mgt.getClientByEmail(mailFrom);
				request.setAttribute("clientMap", r.getRetVal());
				r = mgt.getAssignById(mailIds);
				request.setAttribute("assignMap", r.getRetVal());
			}
			request.setAttribute("totalPage", rs.getTotalPage());
			request.setAttribute("result", map);
			request.setAttribute("pageBar", pageBar2(rs, request));
		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}

		return getForward(request, mapping, "list");

	}

	/**
	 * queryWokFlow
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
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		if ("left".equals(type)) {
			// 显示左帐号树
			// 查找个人外部帐号
			String userId = getLoginBean(request).getId();
			Result result = null;
			// 如果不是管理员,只查询当前用户的外部邮箱账户
			//if (!userId.equals("1")) {
			//-------即使是管理员 也只能查看见的邮件，否则乱套了,1没有了隐私，2如果是公共邮箱，管理员可以设置为共享，对共享人开放---------
				result = mgt.selectAccountByUser(userId);
			//} else {// 如果是管理员则查询系统所有用户的外部邮箱账户
			//	result = mgt.selectAllAccount();
			//}			
			request.setAttribute("accountSelect", request.getParameter("accountSelect"));
			request.setAttribute("mailStyle", request.getParameter("mailStyle"));
			request.setAttribute("outteremail", result.getRetVal());
			// 查找所有的自定义文件夹
			result = mgt.selectGroupByUser(userId);
			request.setAttribute("emailgroup", result.getRetVal());
			result = mgt.getMailSize(userId);			
			request.setAttribute("mailSize", result.retVal);

			return getForward(request, mapping, "frameLeft");
		} else if ("main".equals(type)) {
			// 显示主操作界面
			// 转入相应帐号的收件箱
			return queryEmailList(mapping, form, request, response);
		} else if ("stat".equals(type)) {
			// 显示主操作界面
			// 转入相应帐号的收件箱
			String statId= request.getParameter("statId");
			Result rs = mgt.statMailByGroup(statId,this.getLoginBean(request).getId());
			String msg = "";
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				for (Object o : (List) rs.getRetVal()) {
					Object os[] = (Object[]) o;
					msg += os[0] + "_" + os[1] + ":" + os[2] + ";";
				}
			}
			request.setAttribute("msg", msg);
			return getForward(request, mapping, "blank");
		} else if ("sign".equals(type)) {
			// 取帐户签名
			String emailType = request.getParameter("emailType");
			Result rs = mgt.loadAccount(emailType);
			String msg = "";
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				MailinfoSettingBean bean = (MailinfoSettingBean)rs.retVal;
				msg = bean.getSignature();
			}
			request.setAttribute("msg", msg);
			return getForward(request, mapping, "blank");
		}  else {
			// 如果是从我的桌面连接过来
			String isFromDeskTop = request.getParameter("isFromDeskTop") == null ? ""
					: request.getParameter("isFromDeskTop");
			if (isFromDeskTop.equals("trues")) {
				request.setAttribute("isFromDeskTop", "trues");
				request.setAttribute("url",
						"/EMailQueryAction.do?operation=4&type=main&curMailId="
								+ request.getParameter("keyId") + "&groupId="
								+ request.getParameter("groupId")
								+ "&emailType="
								+ request.getParameter("emailType"));
			}

			// 显示主框架页面
			return getForward(request, mapping, "frameIndex");
		}
	}
	
	
	/**
	 * 设置 提醒
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
//	private ActionForward setAlert(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		/*添加提醒*/
//		String emailId = getParameter("emailId", request);
//		String emailTitle = getParameter("emailTitle", request) ;
//		
//		AlertBean alertBean = new AlertBean();
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
//			alertBean.setId(IDGenerater.getId());
//			alertBean.setAlertDate(alertDate);
//			alertBean.setAlertHour(Integer.parseInt(alertHour));
//			alertBean.setAlertMinute(Integer.parseInt(alertMinute));
//			alertBean.setAlertContent("邮件提醒：" + emailTitle);
//			alertBean.setIsLoop(isLoop);
//			alertBean.setLoopType(loopType);
//			alertBean.setLoopTime(loopTime);
//			alertBean.setEndDate(endDate);
//			alertBean.setAlertType(strAlertType);
//			alertBean.setCreateBy(getLoginBean(request).getId());
//			alertBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
//			alertBean.setRelationId(emailId);
//			alertBean.setNextAlertTime(alertDate + " " + alertHour + ":" + alertMinute + ":00");
//			alertBean.setStatusId(0);
//			alertBean.setPopedomUserIds(getLoginBean(request).getId());
//		}
//		Result result = mgt.addAlert(alertBean) ;
//		if(result.retCode ==ErrorCanst.DEFAULT_SUCCESS){
//			EchoMessage.success().add("添加提醒设置成功")
//					.setBackUrl("/EMailAction.do?operation=5&iframe=true&keyId="+emailId)
//					.setAlertRequest(request);
//		}else{
//				EchoMessage.error().add("添加提醒设置失败")
//						.setBackUrl("/EMailAction.do?operation=5&iframe=true&keyId="+emailId)
//						.setAlertRequest(request);
//		}
//		return getForward(request, mapping, "alert") ;
//	}
//	
//	/**
//	 * 取消 提醒设置
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	private ActionForward cancelAlert(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		String emailId = getParameter("emailId", request) ;
//		String alertId = getParameter("alertId", request) ;
//		Result result = mgt.deleteAlert(alertId) ; 
//		if(result.retCode ==ErrorCanst.DEFAULT_SUCCESS){
//			EchoMessage.success().add("取消提醒设置成功")
//					.setBackUrl("/EMailAction.do?operation=5&iframe=true&keyId="+emailId)
//					.setAlertRequest(request);
//		}else{
//				EchoMessage.error().add("取消提醒设置失败")
//						.setBackUrl("/EMailAction.do?operation=5&iframe=true&keyId="+emailId)
//						.setAlertRequest(request);
//		}
//		return getForward(request, mapping, "alert") ;
//	}
//	
	/**
	 * mj  邮件过滤规则
	 */
	public String getGroupId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,String setId,OAMailInfoBean mailInfo,String emailType,String userId){
		Result rs;
		//获得该邮箱对应的详细规则信息
		Result rsOfFilterInfo = efMgt.getFilterInfoByMsId(setId);
		String[] filterArr = null;
		String groupId = "1";
		if (rsOfFilterInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {// 查询成功
			//得到过滤规则数组
			List<EmailFilter> list = (List<EmailFilter>) rsOfFilterInfo.retVal;
			EmailFilter filter = list.get(0);
			String rule = filter.getFilterCondition();
			rsOfFilterInfo = efMgt.getArrayByStrSplit(rule);
			filterArr = (String[])rsOfFilterInfo.getRetVal();
			Boolean isContainFrmEmi = false;
			for (int i = 0; i < filterArr.length; i++) {
				String f = filterArr[i];
				//发送邮件的用户的邮箱地址 是否包含在这个规则里面 
				rs = efMgt.load(emailType);
				MailinfoSettingBean ms = (MailinfoSettingBean)rs.getRetVal();
				String mailAdd = ms.getMailaddress();
				if(StringUtils.equals(f,mailAdd)) {
					isContainFrmEmi = true;
					break;
				}
			}
			
			if(isContainFrmEmi){
				String folderName = filter.getFolderName();
				//根据文件夹名查找对应的groupId
				rs = efMgt.getGroupIdMapByFolder(folderName, userId, setId);
				if(rs != null){
					groupId = (String)rs.getRetVal();
				} else {
					 //查询失败
					EchoMessage.error().add(getMessage(request, "common.msg.error"))
							.setRequest(request);
					//return getForward(request, mapping, "message");
					return null;
				}
				
			}
			return groupId;
		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			//return getForward(request, mapping, "message");
			return null;
		}
		
	}
	
	/**
	 * 查看收件人详细信息
	 * 
	 * @param mapping
	 * ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	
	protected ActionForward readStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		 * 根据邮件groupId来展示不同的信息给客户,如果是发件箱 此刻 可以有删除 全选 以及发送通知通告
		 * 否则只能查看是否已经阅读等信息
		 */
		String title = getParameter("emailTitle", request);
		if (StringUtils.isNotBlank(title)) {
			title = GlobalsTool.toChinseChar(title);
		}
		String groupId = getParameter("groupId", request);
		//根据该id查询得到多有的收件人邮件信息(收件人姓名 所属部门 阅读时间 是否阅读) mj
		String pId = getParameter("pId", request);
		Result rs = mgt.queryReceives(pId, "","");// 显示所有 并且不显示删除的
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("receives", rs.getRetVal());
			request.setAttribute("emailTitle", title);
			request.setAttribute("groupId", groupId);
			request.setAttribute("pId", pId);//父邮件
			if (StringUtils.equals(groupId, "3")) {// 发件箱
				request.setAttribute("isOperation", true);// 可以有各种操作
			} else {
				request.setAttribute("isOperation", false);
			}
		} else if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "readStatus");
	}
	
	/**
	 * 撤销邮件
	 * mj
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward dels(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String emailType = request.getParameter("emailType"); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // 帐号类型,除内部外，表明内部邮箱的帐号代号
		request.setAttribute("groupId", groupId);
	
		ActionForward forward = null;
		String nstr[] = request.getParameterValues("pho");
		String pId = getParameter("pId", request);
		Result rss = mgt.loadMail(pId);
		OAMailInfoBean pMailBean  = ((OAMailInfoBean)(rss.retVal));
		String bcc = null;
		String cc = null;
		String to = null;
		boolean isUpdate = false;
		if (pMailBean != null){
			 bcc = pMailBean.getMailBCc();
			 cc = pMailBean.getMailCc();
			 to = pMailBean.getMailTo();
		}
		
		if (nstr != null && nstr.length != 0) {
			// 执行删除
			for (String s : nstr) {
				String fullName = (String)mgt.getFullNameByEmailId(s).getRetVal();
				if (StringUtils.isNotBlank(fullName)){
					//如果被撤销的当前收件人 包含在 bcc cc to等 应该将 bcc  cc to中对应的删除掉 否则 将导致 mailDetail.jsp里面不对应出不来 查看详情字样
					if (StringUtils.contains(bcc, fullName)) {
						bcc = bcc.replace(fullName + ";", "");
						isUpdate = true;
					}
					if (StringUtils.contains(cc, fullName)) {
						cc = cc.replace(fullName + ";", "");
						isUpdate = true;
					}
					if (StringUtils.contains(to, fullName)) {
						to = to.replace(fullName + ";", "");
						isUpdate = true;
					}
				}
				mgt.delMail(nstr);
			}
			if (isUpdate){
				pMailBean.setMailBCc(bcc);
				pMailBean.setMailTo(to);
				pMailBean.setMailCc(cc);
				mgt.updateMail(pMailBean);
					
			}
			Result rs = mgt.queryReceives(pId, "","");// 显示所有 并且不显示删除的
			request.setAttribute("emailTitle", getParameter("title", request));
			request.setAttribute("receives", rs.getRetVal());
			request.setAttribute("pId", pId);
		} else {
			EchoMessage.success().add(
					getMessage(request, "com.revert.to.failure")).setBackUrl(
					"/PhotoAction.do").setAlertRequest(request);
		}
		return getForward(request, mapping, "readStatus");
	}
	
	/**
	 * 重写权限判断的方法,因为此模块不需要权限判断,所以返回null
	 */
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
	    LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            AIOEMail.emailLog.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
		return null;
	}
	
	/**
	 * 收藏邮件
	 */
	protected ActionForward collectionMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String mailId = (String)request.getParameter("mailId");
		String collectionType = (String)request.getParameter("collectionType");
		
		Result rs = mgt.loadMail(mailId);
		OAMailInfoBean oaMailInfoBean = (OAMailInfoBean)(rs.retVal);
		
		oaMailInfoBean.setCollectionType(collectionType);
		rs = mgt.updateMail(oaMailInfoBean);
		String json = "no"; 
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			json = "ok";
		}
		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 加载右边数据（组织架构，通讯录，客户）
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward queryUserData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		LoginBean lg = this.getLoginBean(request);
		String opType = request.getParameter("opType");
		String keyword = request.getParameter("keyword");
		keyword = GlobalsTool.toChinseChar(keyword);
		Result rs = null;
		if(opType != null && "outside".equals(opType)){
			//外部邮件获取数据（通讯录数据)
			String str = "";
			String pageNo = request.getParameter("pageNo");
			str += "{\"address\":[";
			if(pageNo == null || "".equals(pageNo)){
				rs = mgt.queryAddressList(keyword);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					List list = (ArrayList)rs.retVal;
					for(int i=0;i<list.size();i++){
						HashMap map = (HashMap)list.get(i);
						str += "{\"id\": \""+map.get("id")+"\",\"name\": \"" + map.get("name")+"\",\"email\": \""+map.get("email")+"\"}";
						if(i<list.size()-1){
							str += ",";
						}
					}
				}
			}
			str += "]";
			rs = mgt.queryClientData(keyword, lg, pageNo);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				Object[] obj = (Object[])rs.getRetVal();
				str += ",\"count\":[\""+obj[0].toString()+"\"]";
				List list = (ArrayList)obj[1];
				str += ",\"client\":[";
				for(int i=0;i<list.size();i++){
					HashMap map = (HashMap)list.get(i);
					String clientName = map.get("clientName").toString();

					str += "{\"id\": \""+map.get("id")+"\",\"name\": \"" + map.get("name")+"\",\"email\": \""+map.get("email")+"\",\"clientName\":\""+clientName+"\"}";
					if(i<list.size()-1){
						str += ",";
					}
				}
				str += "]";
			}
			str += "}";
			request.setAttribute("msg", str);
			return getForward(request, mapping, "blank");
		}
		
		//查询组织架构（部门，职员）
		rs = mgt.queryDeptOrEmp(keyword);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String folderTree = "";
			//查询成功
			Object[] obj = (Object[])rs.retVal;
			List deptList = (ArrayList)obj[0];
			List empList = (ArrayList)obj[1];
			//对数据进行处理把数据组合成节点形式
			for(int i=0;i<deptList.size();i++){
				HashMap o = (HashMap)deptList.get(i);
				String classCode = String.valueOf(o.get("classCode"));
				if(keyword == null || "".equals(keyword)){
					if(classCode.length() == 5){
						folderTree += this.folderTree(deptList, empList, classCode, String.valueOf(o.get("name")), Integer.parseInt(o.get("isCatalog").toString()), String.valueOf(o.get("id")));
					}
				}else{
					folderTree += "<li><span><a style=\"color:#000000\" href=\"javascript:void(0)\" onclick=\"addSend('"+o.get("name")+"','"+classCode+"')\"><font style=\"color: black\" id=\"type_"+classCode+"\">"+o.get("name")+"</font></a><font id=\"_1\"></font></span></li>";
				}
			}
			if(keyword != null && !"".equals(keyword)){
				for(int i=0;i<empList.size();i++){
					HashMap o = (HashMap)empList.get(i);
					folderTree += "<li><span><a style=\"color:#000000\" href=\"javascript:void(0)\" onclick=\"addSend('"+o.get("name")+"','"+o.get("id")+"')\"><font style=\"color: black\" id=\"type_"+o.get("classCode")+"\">"+o.get("name")+"</font></a><font id=\"_1\"></font></span></li>";
				}
			}
			request.setAttribute("msg", folderTree);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 组织架构数据整理
	 * @param deptList
	 * @param empList
	 * @param classCode
	 * @param name
	 * @param isCatalog
	 * @param id
	 * @return
	 */
	private String folderTree(List deptList,List empList,String classCode,String name,Integer isCatalog,String id){
    	String folderTree="";
    	if(isCatalog!=null && isCatalog == 1){
    		folderTree="<li><span><a style=\"color:#000000\" href=\"javascript:void(0)\" onclick=\"addSend('"+name+"','"+classCode+"')\"><font style=\"color: black\" id=\"type_"+classCode+"\">"+name+"</font></a><font id=\"_1\"></font></span><ul>";
    		for(int i=0;i<deptList.size();i++){
    			HashMap o = (HashMap)deptList.get(i);
    			String classC = String.valueOf(o.get("classCode"));
    			if(classC.substring(0,classC.length()-5).equals(classCode)){
    				folderTree+=this.folderTree(deptList, empList, classC, String.valueOf(o.get("name")), Integer.parseInt(o.get("isCatalog").toString()), String.valueOf(o.get("id")));
    			}
    		}
    		if(empList != null && empList.size()>0){
    			for(int i=0;i<empList.size();i++){
    				HashMap empMap = (HashMap)empList.get(i);
    				if(empMap.get("classCode").equals(classCode)){
    					folderTree += "<li><span><a style=\"color:#000000\" href=\"javascript:void(0)\" onclick=\"addSend('"+empMap.get("name")+"','"+empMap.get("id")+"')\"><font style=\"color: black\" id=\"type_"+empMap.get("classCode")+"\">"+empMap.get("name")+"</font></a><font id=\"_1\"></font></span></li>";
    				}
    			}
    		}
    		folderTree+="</ul></li>";
    	}else{
    		folderTree = "<li><span><a style=\"color:#000000\" href=\"javascript:void(0)\" onclick=\"addSend('"+name+"','"+classCode+"')\"><font style=\"color: black\" id=\"type_"+classCode+"\">"+name+"</font></a><font id=\"_1\"></font></span>";
    		if(empList != null && empList.size()>0){
    			boolean falg = false;
    			folderTree += "<ul>";
    			for(int i=0;i<empList.size();i++){
    				HashMap empMap = (HashMap)empList.get(i);
    				if(empMap.get("classCode").equals(classCode)){
    					falg = true;
    					folderTree += "<li><span><a style=\"color:#000000\" href=\"javascript:void(0)\" onclick=\"addSend('"+empMap.get("name")+"','"+empMap.get("id")+"')\"><font style=\"color: black\" id=\"type_"+empMap.get("classCode")+"\">"+empMap.get("name")+"</font></a><font id=\"_1\"></font></span></li>";
    				}
    			}
    			folderTree += "</ul>";
    			if(!falg){
    				folderTree = folderTree.replace("<ul></ul>", "");
    			}
    		}
    		folderTree += "</li>";
    	}
    	return folderTree;
    }
	
	
	/**
	 * 查询收件人数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward queryTo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String emailType = request.getParameter("emailType");
		TextBoxUtil textBoxUtil = new TextBoxUtil();
		/* TextBoxList控件数据的选取 */
		if("".equals(emailType)){
			//内部邮件（得到职员和部门）
			ArrayList<String[]> list = textBoxUtil.getUsersValues("openSystem");
			ArrayList<String[]> deptList = textBoxUtil.getDeptValues();
			for(int i=0;i<deptList.size();i++){
				list.add(deptList.get(i));
			}
			request.setAttribute("msg",gson.toJson(list));
		}else{
			//外部邮箱取存在email地址的职员和客户联系人
			ArrayList<String[]> list = textBoxUtil.getUsersEmailValues();
			ArrayList<String[]> clientList = textBoxUtil.getClientValues();
			for(int i=0;i<clientList.size();i++){
				list.add(clientList.get(i));
			}
			request.setAttribute("msg",gson.toJson(list));
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 处理邮件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward dealRemark(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String mailId = request.getParameter("id");
		String content = request.getParameter("content");
		content = GlobalsTool.toChinseChar(content);
		Result rs = mgt.dealRemark(mailId, content);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			int count = Integer.parseInt(rs.retVal.toString());
			request.setAttribute("msg", count);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 删除邮件发送人员的历史记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward delSendHistory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String id = request.getParameter("id");
		Result rs = mgt.delHistory(id,this.getLoginBean(request).getId());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", "OK");
		}
		return getForward(request, mapping, "blank");
	}
	
}
