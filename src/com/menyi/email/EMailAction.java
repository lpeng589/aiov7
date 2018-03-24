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
		// wordType=2Ϊ���� 1Ϊ����ɾ��
		String hide = BaseEnv.systemSet.get("HideEmail").getSetting(); // �Ƿ������ʼ���ַ
		// (falseΪ������)
		request.setAttribute("hide", hide);
		//�����õ�clearSerchForm��ֵ��"yes"�Ļ������form�е�����
        if("yes".equals(request.getParameter("clearSerchForm"))){
        	form=null;
        	request.setAttribute("clearSerchForm", "yes");
        }
        String noback=request.getParameter("noback");//���ܷ���
        String collectionMail=request.getParameter("collectionMail");//�Ƿ��ղؽ���
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		/*�Ƿ����body2head.js*/		
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
				// �����ʼ�
				forward = dels(mapping, form, request, response);
			} else if("delSendHistory".equals(type)){
				//ɾ�������ʼ�����ʷ��¼
				forward = delSendHistory(mapping, form, request, response);
			}else {
				forward = delete(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			if("collection".equals(collectionMail)){
				forward = collectionMail(mapping, form, request, response);
			}else if("dealRemark".equals(collectionMail)){
				//��ӻ����޸ģ�ɾ���ʼ���ע
				forward = dealRemark(mapping, form, request, response);
			}else{
				forward = update(mapping, form, request, response);
			}
			break;

		// ����������ѯ
		case OperationConst.OP_DETAIL:			
			forward = mailDetail(mapping, form, request, response);
			break;
		// ����������ѯ
		case OperationConst.OP_QUERY:

			if (null != type && type.equals("receive")) {
				// ���Ų���OP_QUERYһ����Ȩ�޿���
				forward = receiveMail(mapping, form, request, response);
			} else if (null != type && type.equals("groupmanager")) {
				// �ļ��й��� �ļ��й������OP_QUERY,ԭ�����ļ��й�����Ȩ�����ƣ��ܷ����ʼ����ܹ����ļ���
				forward = groupManage(mapping, form, request, response);
			}else if (null != type && type.equals("mailCRM")) {
				// �ļ��й��� �ļ��й������OP_QUERY,ԭ�����ļ��й�����Ȩ�����ƣ��ܷ����ʼ����ܹ����ļ���
				forward = mailCRM(mapping, form, request, response);
			}else if ("readStatus".equals(type)) {
				forward = readStatus(mapping, form, request, response);
			}else if("queryUserData".equals(type)){
				//��ѯ�û������ݣ���֯�ܹ���ͨѶ¼���ͻ���
				forward = queryUserData(mapping, form, request, response);
			}else if("queryTo".equals(type)){
				//��ѯ�ռ��˵�����
				forward = queryTo(mapping, form, request, response);
			}else {
				forward = query(mapping, form, request, response);
			}
			break;
		// ����
		case OperationConst.OP_EXPORT:
			forward = export(mapping, form, request, response);
			break;
		// �����������
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
	 * ����
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
		String emailType = request.getParameter("emailType"); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
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
	 * ɾ��
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
		String emailType = request.getParameter("emailType"); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		request.setAttribute("groupId", groupId);

		ActionForward forward = null;
		String nstr[] = request.getParameterValues("keyId");
		if (nstr != null && nstr.length != 0) {
			if ("2".equals(groupId) || "5".equals(groupId) || "true".equals(request.getParameter("deleteReal"))) {
				// ִ��ɾ��
				mgt.delMail(nstr);
			} else {
				// ִ��ת�Ƶ�������
				mgt.moveMail(nstr, "5");
			}
		}
		// ��ӳɹ�
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
	 * �޸�
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

		String emailType = request.getParameter("emailType"); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
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
				//�ʼ�����
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
					// ����ʼ���ǩ
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
				// ����ʼ���ǩ
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
				//�������ʼ���ִ
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
				//�����ʼ���ִ
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
	 * ���ǰ��׼��
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
		String emailType = request.getParameter("emailType");      				//�ʺ�����(�ձ�ʾ�ڲ����䣬��Ϊ�մ����ⲿ����)
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
		
		/* ����·�����뷢���ʼ�����ʱ����ָ���ռ��ˡ���:�ͻ��б�ͨѶ¼����֯�ܹ� */
		String sendPerson = request.getParameter("sendPerson");
		if(sendPerson != null && sendPerson.length() > 0){
			//��ѯ�û����õ������˻�
			Result rs = mgt.selectAccountByUser(lg.getId());
			MailinfoSettingBean obj = null;
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList list = (ArrayList) rs.retVal;
				if(list.size() > 0){				
					for (int i = 0; i < list.size(); i++) {
						MailinfoSettingBean a = (MailinfoSettingBean) list.get(i);
						if (a.getDefaultUser().equals("1")) {
							//����û�������Ĭ�������˻�����ȡ���˻�
							obj = a;
							break;
						}
					}
					if(obj == null){
						//δ����ʱȡ��һ���˻�
						obj = (MailinfoSettingBean) list.get(0);
					}
				}
			}
			if(obj == null){
				//δ�����˻�ʱ����
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
			
			//��ѯ��ƥ���ְԱ
			Result result=null;
			String msgType = getParameter("msgType", request); 
			if(!msgType.contains("detail")){ //�ͻ��б�ҳ�淢���ʼ�
				if("txl".equals(getParameter("txl",request))){
					//ͨѶ¼���뷢���ʼ�����
					result = new CommunicationNoteMgt().selectClientDetById(sendPerson.split(","),"clientEmail");
				}else{
					result = new CRMClientMgt().selectClientDetById(sendPerson.split(","),"clientEmail");
				}
			}else{   //�ͻ�����ҳ�淢���ʼ�
				result = new CRMClientMgt().detailClientDetById(sendPerson);	
				request.setAttribute("sendType", "detailPage");
			}
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//������Ҫ�ռ��˵�����
				request.setAttribute("sendPeriodList", result.retVal) ;
				request.setAttribute("sendPeriod", sendPerson);
				
			}
		}
		//���������Ϊ·��
		String attachPath = emailType;
		if (attachPath == null || attachPath.length() == 0 || attachPath.equals("inner")) {
			attachPath = "inner" + lg.getId();
		}
		request.setAttribute("path", "/email/"+attachPath); //���������·��
		
		// �ʼ�����Ϊ�գ�����ʾ�ڲ��ʼ�
		if (emailType == null || "inner".equals(emailType)) {
			emailType = "";
		}
		
		//������䳬��ֱ����ʾ
		if(!mgt.checkMailSizeList(emailType.equals("")?0:1, emailType, lg.getId())){
			throw new BusinessException(this.getMessage(request,"common.msg.mailSizeLimit"), "/EMailQueryAction.do?operation="
			+ OperationConst.OP_QUERY + "&emailType=" + moreMail + "&type=main&groupId=1");
		}

		request.setAttribute("emailType", emailType); 
		String mailAddress = "";
		MailinfoSettingBean accountInfoBean =null;
		if (emailType.length() > 0) {
			Result rs = mgt.loadAccount(emailType);
			//�û�������
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				accountInfoBean = (MailinfoSettingBean)rs.retVal;
				request.setAttribute("accountInfo", rs.retVal);
				mailAddress = ((MailinfoSettingBean) rs.retVal).getMailaddress();
			}
		} else {
			mailAddress = lg.getEmpFullName();
		}
		request.removeAttribute("operation");

		// �жϻظ����ظ����У�ת������������
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		if (type != null && (type.equals("revert") || type.equals("revertAll") || type.equals("transmit") 
				|| type.equals("transmitOut") || type.equals("transmitInt"))) {
			String keyId = request.getParameter("keyId");
			//����ǻظ��ͻظ����У���¼�ظ����ţ��������ظ�ʱ��
			if (type.equals("revert") || type.equals("revertAll")) {
				request.setAttribute("replayId", keyId);
			}
			if (type.equals("transmit") || type.equals("transmitOut") || type.equals("transmitInt")) {
				request.setAttribute("revolveId", keyId);
			}
			//������������
			OAMailInfoBean bean = null;
			Result rs = mgt.loadMail(keyId);
			
			Result r = mgt.getMailReplyAccount(emailType);
			Object o = r.getRetVal();
			request.setAttribute("MailReplyAccount", o);
			if("assign".equals(moreMail)){
				//�����ʻ�
				ArrayList al = new ArrayList();
				al.add(new String[]{accountInfoBean.getId(),accountInfoBean.getAccount()});
				request.setAttribute("MailinfoSetting",al);
			}else if(o == null){//û�лظ��˻�
			
				Result rs2 = mgt.getALLMailinfoSetting(lg.getId());
				List mailSettingList = (ArrayList)rs2.getRetVal();
				if(("transmitOut").equals(type)){
					//�ڲ��ʼ�ת�ⲿ�ʼ�ʱ�ж���û���ⲿ����
					if(mailSettingList.size()==0){
						throw new BusinessException("��ǰδ�����ⲿ���䣬�޷������ʼ�ת�ⲿ������", "/EMailQueryAction.do?operation="
								+ OperationConst.OP_QUERY + "&emailType=" + moreMail + "&type=main&groupId="+groupId);
					}
				}
				request.setAttribute("MailinfoSetting",mailSettingList);
			}else{
				Result rst = mgt.getMailReplyAccountDet((String)o);
				//�����Լ�û��Ȩ�޵��ʻ�
				if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null){
					List<String[]> lr = (List<String[]>)rst.retVal;
					//�Լ���Ȩ�޵��ʻ�
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
				//��ѯ�������ݳɹ�
				bean = (OAMailInfoBean) rs.getRetVal();
				String name = "";

//				String hide = BaseEnv.systemSet.get("HideEmail").getSetting(); // �Ƿ������ʼ���ַ
				// (falseΪ������)
//				String emailFrom = "";
//				String emailAddress = "";
//				//��ѯͨѶ¼����
//				Result rp = mgt.getNoteNameAndEmail(lg.getId());
//				HashMap<String, String> adds = (HashMap<String, String>)rp.getRetVal();
//				if (bean.getMailFrom() != null) {
//					//������
//					String oldEmailFrom = bean.getMailFrom();
//					//�����ַ
//					emailAddress = oldEmailFrom.substring(oldEmailFrom.indexOf('<') + 1, oldEmailFrom.length() - 1);
//					if (oldEmailFrom.indexOf('<') != -1) {
//
//						//������ص�ַ������ϵ��Ϊ�գ�����ʾ�¿ͻ���������ʾ��ϵ�ˣ������ϵ��Ϊ�գ�����ʾԭ�ʼ�����
//						emailFrom = oldEmailFrom.substring(0,oldEmailFrom.indexOf('<') );
//						if(adds.get(emailAddress) != null && !"".equals(adds.get(emailAddress))){
//							//����ͨѶ¼�������ƥ��
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

				// �༭�ظ�����
				StringBuffer content = new StringBuffer();
				content.append("<br/><br/><br/><br/><br/><br/><br/>\r\n\r\n\r\n<div><!-- SIGN_START -->"+(accountInfoBean==null||accountInfoBean.getSignature()==null||accountInfoBean.getSignature().length()==0?"":"------------------------------------\r\n<br/>"+accountInfoBean.getSignature()+"<br/>\r\n------------------------------------")+"<!-- SIGN_END --></div><div style='text-align:left;'><b>"
						+ this.getMessage(request, "oa.mail.sendPeople")+ "��</b>" + name);
				Date time = BaseDateFormat.parse(bean.getMailTime(),BaseDateFormat.yyyyMMddHHmmss);
				content.append("<br><b>"
						+ this.getMessage(request, "message.lb.createTime")
						+ "��</b>" + (time.getYear() + 1900)
						+ this.getMessage(request, "com.date.year")
						+ (time.getMonth() + 1)
						+ this.getMessage(request, "oa.calendar.month")
						+ time.getDate()
						+ this.getMessage(request, "data.lb.w0")
						+ time.getHours() + ":" + time.getMinutes());

				content.append("<br><b>" + this.getMessage(request, "oa.mail.addressee") + "��</b>" + bean.getMailTo());
				if (bean.getMailCc() != null && bean.getMailCc().length() > 0) {
					content.append("<br><b>" + this.getMessage(request, "oa.mail.cc") + "��</b>" + bean.getMailCc());
				}

				content.append("<br><b>" + this.getMessage(request, "oa.subjects") + "��</b>" + bean.getMailTitle() +"</div>");
				bean.setMailContent(content.append(bean.getMailContent()).toString());

				if (type.equals("revert")) {
					bean.setMailTitle("Re��" + bean.getMailTitle());
					bean.setMailTo(name);
					bean.setMailAttaches(null);
				} else if (type.equals("revertAll")) {
					bean.setMailTo(name + ";" + bean.getMailTo()); // +";"+bean.getMailCc()+";"+bean.getMailBCc()
					// ȥ���Լ�
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
					bean.setMailTitle("Re��" + bean.getMailTitle());
					bean.setMailAttaches(null);
				} else if (type.startsWith("transmit")) {
					bean.setMailTo("");
					bean.setMailTitle("Fw"
							+ "��" + bean.getMailTitle());
					//�������������eml�ļ����򸽼�����eml��·��
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
		// �������е��Զ����ļ���
		result = mgt.selectGroupByUser(userId);
		request.setAttribute("emailgroup", result.getRetVal());
		String emailType = request.getParameter("emailType"); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		request.setAttribute("groupId", groupId);
		return getForward(request, mapping, "Import");
	}
	
	protected ActionForward importMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean user = (LoginBean) request.getSession().getAttribute("LoginBean");
		String userId = user.getId();
		
		ImportForm myForm = (ImportForm)form;
		
		String emailType = request.getParameter("emailType"); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		request.setAttribute("groupId", groupId);
		
		
		ActionForward forward = null;
		int rs[]=new int[]{0,0,0};
		if(myForm.getImportType().equals("backup")){
			 rs = mgt.importFromAIO(myForm, emailType,groupId,userId);
		}else{
			rs = mgt.importStandand(myForm, emailType,groupId,userId);
		}
		// ����·��
		String backUrl="/EMailQueryAction.do?operation="
			+ OperationConst.OP_QUERY + "&emailType="
			+ emailType + "&type=main&groupId=" + groupId;
		//  �Զ���ת
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
		.add(this.getMessage(request, "mail.msg.importsuccees",rs[0]+"",rs[1]+"",rs[2]+"")+"<br /> ����<label id='timenum'  style='color:#F00; font-size:24px; font-weight:bold;'>5</label>���Զ�����"+javaScript )
		.setBackUrl(backUrl).setNotAutoBack()
		.setAlertRequest(request);
		return getForward(request,mapping,"alert");
		
		
	}

	/**
	 * �����ʼ� 1�����浽�ݸ��䣬��ִ�з��Ͷ����� 2��ֱ�ӷ��ͣ��򱣴浽�����䲢ִ�з��Ͷ�����
	 * 3���Ӳݸ���򿪣��򱣴浽�����䣬��ɾ���ݸ��䣬������ 4���Ӳݸ���򿪣�������Ϊ�ݸ壬��ɾ��ԭ�ݸ�
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
		boolean begReplay = "1".equals(mailForm.getBegReplay());		//�����Ķ�����
		String emailType = request.getParameter("emailType"); 			//�ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		
		String moreMail = request.getParameter("moreMail");
		if(moreMail == null || moreMail.length() ==0){
			moreMail = emailType;
		}
		String saveType = request.getParameter("saveType");				//�ʼ����淽ʽ(saveֱ�ӷ��ͣ�draft��Ϊ�ݸ�)
		
		String isOAEmail = getParameter("isOAEmail", request);
		String userId = this.getLoginBean(request).getId();

		/* �������ݵ�Bean�� */
		OAMailInfoBean mailInfo = new OAMailInfoBean();
		String id = IDGenerater.getId();
		mailInfo.setId(id);
		mailInfo.setBegReplay(mailForm.getBegReplay());
		mailInfo.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		mailInfo.setEmailType((emailType == null || emailType.length() == 0 || emailType.equals("inner")) ? 0 : 1);
		mailInfo.setSendeMailType(request.getParameter("sendEmailType"));
		mailInfo.setFromUserId(userId); //�����˴���
		mailInfo.setCollectionType("0");
		mailInfo.setGroupId("draft".equals(saveType) ? "2" : "3"); //����Ǳ���Ϊ�ݸ壬�����ݸ��䣬����Ϊ������

		String attachPath = emailType;//���������Ϊ·��
		if (emailType == null || emailType.length() == 0 || emailType.equals("inner")) {
			attachPath = "inner" + this.getLoginBean(request).getId();
		}
		mailInfo.setMailSize(mailForm.getMailContent() == null ? 0 : mailForm.getMailContent().length());

		// ����
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;

		//��ɾ���ĸ���
		String delFiles = request.getParameter("delFiles");
		//��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ�� 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/email/" + attachPath + "/" + del);
				aFile.delete();
			}
		}		
		mailInfo.setMailAttaches(mailAttaches);		//���渽��
		
		//���㸽����С
		String sts[] =(mailAttaches ==null?"": mailAttaches).split(";");
		for(String st:sts){
			if(st.indexOf("emlfile") != -1 && st.substring(st.indexOf("emlfile=")+"emlfile=".length(),st.indexOf("&charset")).length() > 0){
				//emlfile=c5daa3c8_1205141821114950004.eml&charset=&attPath=c5daa3c8_1205141821011640001&fileName=QQ��ͼ20120113150732(1).png
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
		
		mailInfo.setMailFrom(this.getLoginBean(request).getEmpFullName());			//�����ʼ�������

		
		String isPart = request.getParameter("isPart");
		if(isPart != null && "1".equals(isPart)){
			//���÷ֱ����ʼ�ʱ���ѽ�����ȫ���ŵ������У������˺ͳ��������
			mailForm.setBcc(mailForm.getTo());
			mailForm.setCc("");
			mailForm.setTo("");
		}
		
		// ������
		mailInfo.setMailCc(mailForm.getCc());
		//������
		mailInfo.setMailTo(mailForm.getTo());
		//������
		mailInfo.setMailBCc(mailForm.getBcc());
		//����
		mailInfo.setMailContent(mailForm.getMailContent());
		//����ʱ��
		mailInfo.setMailTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		//����
		mailInfo.setMailTitle(mailForm.getMailTitle());
		mailInfo.setState(1); // ����ʱ���������ʼ�
		// mailInfo.setToUserId(userId); //���͸����ѵ��ʼ�
		mailInfo.setUserId(userId);
		mailInfo.setAccount(emailType);
        
		//�ڲ��ʼ����ռ��˽����ж��Ƿ���ְԱ��Ͳ��ű��д�����Ӧ�ļ�¼����������ڣ��������ʼ�
		if(mailInfo.getEmailType()==0){
			ArrayList mailList = new ArrayList();			//������Է��͵��ռ���
			//���ռ��˽���ƥ��
			String errorEmail = innerMailParse(mailInfo.getMailTo() + ";" + mailInfo.getMailCc() + ";" + mailInfo.getMailBCc(),mailList);
			if (errorEmail != null && errorEmail.length() > 0) {
				String msg = errorEmail + this.getMessage(request, "oa.mail.receiverError")+","+this.getMessage(request,"email.msg.savetodraft");
				request.setAttribute("MESSAGE_AUTOBACK", "0");
				if("true".equals(request.getParameter("fixUser"))){
					EchoMessage.error().add(msg).setClose().setAlertRequest(request);
				}else{
					//���ʧ��
		 			EchoMessage.error().add(msg).setAlertRequest(request);
		 			return getForward(request, mapping, "alert");
				}
				return getForward(request,mapping,"message");
			}
		}
		
		// ���浽���ݿ���ȥ
		Result rs = mgt.addMail(mailInfo);		
		
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			throw new BusinessException(this.getMessage(request,
					"common.msg.sendError"), "/EMailQueryAction.do?operation="
					+ OperationConst.OP_QUERY + "&emailType=" + moreMail
					+ "&type=main&groupId=1");
		}
		//��¼�ظ�ʱ��
		String replayId = request.getParameter("replayId");
		if(replayId != null && replayId.length() >0){
			mgt.updateReplayDate(replayId);
		}
		//��¼ת��ʱ��
		String revolveId = request.getParameter("revolveId");
		if(revolveId != null && revolveId.length() >0){
			mgt.updateRevolveIdDate(revolveId);
		}
		
		// ����ǴӲݸ���򿪣���ɾ��ԭ�ݸ�
		String oldDraftid = request.getParameter("oldDraftid");
		if (oldDraftid != null && oldDraftid.length() > 0) {
			mgt.delDrafMail(oldDraftid);
		}
		// �������Ϊ�ݸ壬�򷵻زݸ���
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
			// �����ⲿ�ʼ�
			try {
				// ָ���ʺź�����
				EMailMessage smes = new EMailMessage();
				smes.setBegReplay(begReplay);
				// �����ռ���
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

				// ��������
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

				// ���ӳ���
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
					throw new BusinessException("�ʼ���ַ��ʽ"+errorEmail+"����ȷ;�ʼ��Ѵ���ݸ���",backUrl); 
				}
				
				// ���Ӹ���

				if (mailAttaches != null && mailAttaches.length() > 0) {
					for (String of : mailAttaches.split(";")) {
						if (of != null && of.length() > 0){
							///ReadFile?tempFile=email&emlfile=685446d5_1204181739334790045.eml&charset=utf-8&attPath=16498250_1107&fileName=113150732.png
							//"emlfile="+bean.getEmlfile()+"&charset="+bean.getMailcharset()+"&attPath="+bean.getAccount()+"&fileName="+s+";";
							
							
							if(of.indexOf("&fileName=") > 0){
								//���Ǵ�eml�ļ���ȡ����
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
				//�����ʼ�����������˶���10�������첽����
				if(smes.getRecipientList().size()>=10){
					/***************�ʼ��� ��Ϊ�첽����*********************/					
					mgt.sendByThread(smes, sendEmailType, mailInfo.getId(), userId); 
				}else{
					//��ѯ�ʺ���Ϣ
					rs = mgt.loadAccount(sendEmailType);
					if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
						throw new BusinessException(this.getMessage(request,"common.msg.sendError"),
								"/EMailQueryAction.do?operation=" + OperationConst.OP_QUERY + "&emailType=" + moreMail + "&type=main&groupId=1");
					}
					MailinfoSettingBean setting = (MailinfoSettingBean) rs.getRetVal();
					
					AIOEMail sm = new AIOEMail();
					boolean smtpauth = setting.getSmtpAuth() == 1 ? true : false;
					// ָ��Ҫʹ�õ��ʼ�������
					sm.setAccount(setting.getMailaddress(),setting.getPop3server(), setting.getPop3username(),
							setting.getPop3userpassword(), smtpauth, setting.getSmtpserver(), setting.getSmtpusername(),
							setting.getSmtpuserpassword(), setting.getPop3Port(),setting.getSmtpPort(), setting.getDisplayName(),
							attachPath,setting.getCreateby(),setting.getPopssl()==1?true:false,
									setting.getSmtpssl()==1?true:false,setting.getAutoAssign()==1?true:false);
					
					//���÷����ʼ��ӿ�
					sm.send(smes,getLoginBean(request).getId());
				}

			} catch (AuthenticationFailedException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				mailInfo.setGroupId("2");// ��Ϊ�ݸ�
				mgt.updateMail(mailInfo);
				throw new BusinessException(
						this.getMessage(request,"oa.mail.send.AuthenticationFailture")+","+this.getMessage(request,
						"email.msg.savetodraft"), backUrl);
			} catch (MessagingException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				mailInfo.setGroupId("2");// ��Ϊ�ݸ�
				mgt.updateMail(mailInfo);
				throw new BusinessException(this.getMessage(request,"oa.mail.receive.ConnFailture")+","+this.getMessage(request,
				"email.msg.savetodraft"),
						backUrl); 
			}catch(BusinessException ex){ 
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				mailInfo.setGroupId("2");// ��Ϊ�ݸ�
				mgt.updateMail(mailInfo);
				throw new BusinessException(this.getMessage(request,"common.msg.mailsendError")+","+ex.key,
						backUrl); 
			} catch (Exception ex) {
				AIOEMail.emailLog.error("EMailAction.add SendMail Error ", ex);
				mailInfo.setGroupId("2");// ��Ϊ�ݸ�
				mgt.updateMail(mailInfo);
				throw new BusinessException(this.getMessage(request,
						"common.msg.mailsendError")+","+this.getMessage(request,
						"email.msg.savetodraft"), backUrl);
			}
			//��ӷ�������ʷ��¼
			mgt.emailSendHistory(mailInfo);
		} else {
			//�ڲ��ʼ�����
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
			// ��ӳɹ�
			EchoMessage.success().add(msg).setBackUrl(backUrl).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}

	/**
	 * �����ַ����е���Ա��Ϣ�� �����ʼ��������ɹ��ĵ�ַ��Ϣ. �ʼ�ƥ��ɹ�����list�� ƥ���������ƥ�䣬���ţ�ƥ��ɹ����������������Ա������Ϣ
	 * ʧ����ƥ��ְԱ�б�ƥ��ɹ�����ȡְԱ��Ϣ�� ƥ�䲻��ְԱ���򷴻�ƥ�䲻�ɹ�
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
				// �Ӳ��ű��в�ѯ
				Result rs = mgt.selectDeptUser(email);
				if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
						&& ((ArrayList) rs.getRetVal()).size() > 0) {
					for (Object o : (ArrayList) rs.getRetVal()) {
						Object[] os = (Object[]) o;
						map.put(os[0], os[0]);
					}
				} else {
					// ��ְԱ���в�ѯ
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
	 * �����ַ����е��ʼ���Ϣ�� �����ʼ��������ɹ��ĵ�ַ��Ϣ. �ʼ�ƥ��ɹ�����list��
	 * ƥ���������ƥ�䣬��ʽ�Ƿ�Ϊemail��ʽ���ɹ�����Ϊ����ȷ���ʼ� ʧ����ƥ����ϵ���б�ƥ��ɹ�����ȡ��ϵ���ʼ���Ϣ��
	 * ƥ�䲻����ϵ�ˣ��򷴻�ƥ�䲻�ɹ�
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
					// �������˱��в�ѯ
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

	/* �ж��Ƿ����ʼ� */
	public static boolean isMail(String str) {
		if (str == null || str.length() == 0)
			return true;
		Pattern p = Pattern
				.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * ��ϸ
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
		
		//�л�����
		if("charset".equals(request.getParameter("type"))){
			String charset = this.getParameter("charset", request);
			String emailType = this.getParameter("emailType", request);
	
			AIOEMail ae = new AIOEMail();
			File file = new File(BaseEnv.FILESERVERPATH + "/email/" + emailType+"/eml/"+nstr+".eml");
			try{
				EMailMessage em = ae.readFileByCharset(new FileInputStream(file), charset,emailType);
				//ִ�м�����ϸ
				Result rs = mgt.loadMail(nstr);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){				
					//�����ʼ�
					OAMailInfoBean mailInfo = (OAMailInfoBean)rs.retVal;
					//����
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
	
					// ������
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
	
					// ���浽���ݿ���ȥ
					EMailMgt mgt = new EMailMgt();									
					//����emlԭ�ĺ�����
					mailInfo.setMailcharset(charset);
					mgt.updateMail(mailInfo);	
				}
			}catch(Exception e){
			}
		}
		//String emailType = request.getParameter("emailType"); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		String groupId = request.getParameter("groupId");
		//request.setAttribute("emailType", emailType); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		request.setAttribute("groupId", groupId);
		request.setAttribute("keyId", nstr);
		request.setAttribute("iframe", request.getParameter("iframe"));

		if (nstr == null || nstr.length() == 0) {
			//����ʧ��
			throw new BusinessException("common.msg.error","/EMailQueryAction.do?operation="
							+ OperationConst.OP_QUERY + "&emailType="
							+ request.getParameter("emailType") + "&type=main&groupId=" + groupId);
		}
		
		// ִ�м�����ϸ
		Result rs = mgt.loadMail(nstr);
		if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// ��¼�����ڴ���
			String tips="<script language='javascript'> ";
			tips += "alert('���ʼ��Ѿ�����ɾ��');";
			tips += "parent.delselfById('"+nstr+"');";
			tips +="</script>";
			request.setAttribute("msg", tips);
			return getForward(request, mapping, "blank");
//			throw new BusinessException("common.error.nodata",
//					"/EMailQueryAction.do?operation="
//							+ OperationConst.OP_QUERY + "&emailType="
//							+ request.getParameter("emailType") + "&type=main&groupId=" + groupId);
		   
		} else if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			// ����ʧ��
			throw new BusinessException("common.msg.error",
					"/EMailQueryAction.do?operation="
							+ OperationConst.OP_QUERY + "&emailType="
							+ request.getParameter("emailType") + "&type=main&groupId=" + groupId);
		}

		// ���سɹ�
		// �޸��Ѿ��鿴��email��״̬
		OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;
		
		//�������ռ����У����ɾ�����ʼ��������ڳ���ɾ������ʾ
		if("1".equals(groupId)&&bean.getGroupId()!=null && bean.getGroupId().equals("5")){
						String tips="<script language='javascript'> ";
						tips += "alert('���ʼ��Ѿ�ɾ��');";
						tips += "parent.delselfById('"+nstr+"');";
						tips +="</script>";
						request.setAttribute("msg", tips);
						return getForward(request, mapping, "blank");
		}
		request.setAttribute("result", bean);
		if(bean.getState() != 1 && bean.getUserId().equals(this.getLoginBean(request).getId())){
			bean.setState(1);
			bean.setReplayDate(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));////mj��ʶ�鿴���ʼ���ʱ�� �ڲ鿴�ռ��˵�ʱ������
			mgt.updateMail(bean);
			//�޸���Ϣ״̬
//			mgt.delAdvice(bean);
			new AdviceMgt().deleteByRelationId(bean.getId(), "");
		}
		
		String attPath = bean.getAccount();
    
    	if (attPath == null || attPath.length() == 0) {
			attPath = "inner" + bean.getUserId();
			String pId = bean.getId();
			// ���ݸ�id��ѯ�õ����е��ռ����ʼ���Ϣ(�ռ������� �������� �Ķ�ʱ�� �Ƿ��Ķ�) mj
			rs = mgt.queryReceives(pId, "","");// Ĭ��top5 ֻ�ܲ�ѯ���е����ݳ���������Ĭ�ϡ������� �в����и���
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("receives",  rs.getRetVal());
			} else if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				AIOEMail.emailLog.error("��ѯ�ռ�����Ϣ���ʹ���");
			}
		}
    	request.setAttribute("attPath", attPath); 

		String hide = BaseEnv.systemSet.get("HideEmail").getSetting(); // �Ƿ������ʼ���ַ
		request.setAttribute("hide", hide);
		
		String emailType = bean.getAccount();
		request.setAttribute("emailType", emailType); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		
		
		String userId = ((LoginBean) request.getSession().getAttribute("LoginBean")).getId();		
		if (emailType != null && emailType.length() > 0) {// �ⲿ�ʼ�
			Result r = mgt.getNoteNameByEmail(userId,"'"+GlobalsTool.getMailAddress(bean.getMailFrom())+"'");
			request.setAttribute("noteMap", r.getRetVal());
			r = mgt.getClientByEmail("'"+GlobalsTool.getMailAddress(bean.getMailFrom())+"'");
			request.setAttribute("clientMap", r.getRetVal());	
		}
		Result r = mgt.getAssignById("'"+bean.getId()+"'");
		request.setAttribute("assignMap", r.getRetVal());
		if ("2".equals(groupId)) {	
			// ����ǲݸ��䣬��ֱ�Ӵ��޸Ľ���
			Result rs2;		
			request.setAttribute("groupId", "2");
			if (emailType != null && emailType.length() > 0) {// �ⲿ�ʼ�
				rs2 = mgt.selectConnectorName(bean.getMailFrom());
				request.setAttribute("moreMail",emailType );
			} else {// �ڲ��ʼ�
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
			//�޸�ʱȡ������Ϣ
			EMailMgt mgt = new EMailMgt();
			Result rso = mgt.getALLMailinfoSetting(userId);
			Object o = rso.getRetVal();
			if(o != null){
				ArrayList MailinfoSetting =(ArrayList) o;
				request.setAttribute("MailinfoSetting",MailinfoSetting);
			}
			
			//���������Ϊ·��
			String attachPath = emailType;
			if (attachPath == null || attachPath.length() == 0 || attachPath.equals("inner")) {
				attachPath = "inner" + this.getLoginBean(request).getId();
			}
			request.setAttribute("path", "/email/"+attachPath); //���������·��
		}else{
			//��ѯ��һ������һ������
			String createTime = request.getParameter("createTime");									//�����Ĵ���ʱ��
			String emailSql = (String)request.getSession().getAttribute("EmailSql");				//��ѯ��sql���
			List emailParam = (ArrayList)request.getSession().getAttribute("EmailParam"); 			//sql����Ӧ�Ĳ���
			
			if(createTime != null && emailSql != null && emailParam != null){
				/* ��ѯ��һ�� */
				ArrayList preNextData = (ArrayList)mgt.queryPreNextData(emailSql, emailParam, "pre", createTime).retVal;
				if(preNextData != null && preNextData.size() == 1){
					request.setAttribute("preData", preNextData.get(0));
				}
				/* ��ѯ��һ�� */
				preNextData = (ArrayList)mgt.queryPreNextData(emailSql, emailParam, "next", createTime).retVal;
				if(preNextData != null && preNextData.size() == 1){
					request.setAttribute("nextData", preNextData.get(0));
				}
			}
		}

		if (!"2".equals(groupId)) {
			//��Ϊ�ݸ��޸�ʱ,��ѯ���еı�ǩ��ʾ
			//��ǩ
			request.setAttribute("labelMap", EMailSettingMgt.getMailLabelMap());
			request.setAttribute("labelList", EMailSettingMgt.getMailLabels());

		}
		/*�ж��Ƿ����������*/
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
	//�����ʼ���url ,ȫ��Ϊ������ʽ
	private String handUrl(String str){	
		if(str == null || str.length() ==0) return "";
		Pattern pattern = Pattern.compile("<a([^><]*)>[^><]*</a>",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){              
		    String group = matcher.group(1);   
		    if(group.toLowerCase().indexOf("javascript") > -1){
		    	continue; //ִ�к������ʼ����Ӳ����޸�
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
	 * �ļ��й���
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
			//����ļ��л����޸�
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
				// ����ɹ�
				request.setAttribute("dealAsyn", "true");
				EchoMessage.success().add(getMessage(request, "common.msg."+msg+"Success")).setAlertRequest(request);
				return getForward(request, mapping, "message");
			} else {
				throw new BusinessException("common.msg."+msg+"Failture",
						"/EMailAction.do?operation=" + OperationConst.OP_QUERY
								+ "&type=groupmanager");
			}
		} else if ("del".equals(op)) {
			// ɾ���ļ���
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
				// ��ӳɹ�
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
	 * �ļ��й���
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
		// ��ʾ�ļ����б�
		Result rs = mgt.selectGroupByUser(userId);
		request.setAttribute("list", rs.getRetVal());
		return getForward(request, mapping, "mailGroupManage");
	}

	/**
	 * �����ʼ�
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
		// ���emailIdΪ�գ�˵����ϵͳ��ʼ��
		if (mailOp == null || mailOp.length() == 0) {
			
			if("all".equals(emailType)){
				rs = mgt.selectAccountByUser(userId);
			}else{
				rs = mgt.getMailAddress(new String[]{emailType});
			}
			//��ѯ���ʻ�
			rs = mgt.getMailChildMail((ArrayList<MailinfoSettingBean>)rs.retVal);
			request.setAttribute("emailTypeObj", rs.retVal);
			request.setAttribute("emailType", emailType);
			String oldEmailType = request.getParameter("oldEmailType");
			if(!"all".equals(oldEmailType)&&!"all".equals(emailType)){
				oldEmailType = emailType;
			} 
			request.setAttribute("oldEmailType", oldEmailType);
			
			for(MailinfoSettingBean mmB :(List<MailinfoSettingBean>)rs.retVal){
				//������䳬��ֱ����ʾ					
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
		// ���emailIdΪ"connectMailSystem"��˵������������ϵͳ
		String setId = null;		
		if (mailOp.equals("connectMailSystem")) {
			HashMap smMap = (HashMap)request.getSession().getAttribute("AIOEMAILMAP");
			if(smMap == null){
				smMap = new HashMap();
				request.getSession().setAttribute("AIOEMAILMAP", smMap);
			}
			//����Ự�д��ڶ�����Ҫ�رջỰ�������¿�ʼ������
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
				// ָ��Ҫʹ�õ��ʼ�������
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
				setId = setting.getId();//mj ��setId��ֵ
				int count = 1000;
				//��������ʼ�������
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
				
				// ��ø������Ӧ����ϸ������Ϣ mj
				sm.setOfRules = new HashSet<String[]>();//������еĹ�����Ϣ�Լ�����groupID��Ϣ
				Result rsOfFilterInfo = efMgt.getFilterInfoByMsId(setId);
				if (rsOfFilterInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {// ��ѯ�ɹ�

						// �õ����˹�������
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
				 * ����������ʼ�����ɾ�����ڵ����ʼ�
				 * ˼·Ϊ��ÿ���ʼ�����GUIDΨһ���,���ʻ�ID���ʼ���GUID�����ڱ� tblMailGUID�С������� �ʺ���guid����������
				 * ÿ�ν����ʼ��󣬽������ʼ���GUID��ϳ�һ����ѯ��䣬��ѯ����Щ�ʼ��Ѿ����չ������˳����չ����ʼ����ٽ��գ�
				 * �������ѽ��չ����ʼ��Ƿ񳬹�����ʱ�䣬�������������ɾ����־��
				 * ���ﻹ�迼���쳣�����tblMailGUID�����ݲ�������ɾ���������������ݱ�������½������ﻹ��һ������ͬ��ɾ�����������Ѿ������ڵ�GUID,
				 * ����Ӱ�����ܣ�ÿ����һ�����ֹ������У����Լ�¼ÿ���ʺ����⹤����ʱ�䡣
				 */		
				request.setAttribute("msg", sm.newMsgList.size()); //�����ʼ����ش��ʼ�����
				smMap.put(emailType, sm);
				return getForward(request, mapping, "blank");
			} catch (AuthenticationFailedException ex) {
				AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
				request.setAttribute("msg", "ERROR:"
						+ getMessage(request,
								"oa.mail.receive.AuthenticationFailture")+"�Ƿ�����SSL��ȫ����");
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
		// ���emailIdΪ"closeMailSystem"��˵�������ر�ϵͳ
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
		// ����ĳһ�ʼ�ͷ
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
		
		//����ĳһ�ʼ�
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
				// �����ʼ�
				OAMailInfoBean mailInfo = new OAMailInfoBean();
				mailInfo.setId(IDGenerater.getId());
				mailInfo.setCreateTime(BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss));
				mailInfo.setEmailType(1);
				// mailInfo.setFromUserId();
				// ����
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
				
				// ������
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
				mailInfo.setState(0); // ���ʼ�
				mailInfo.setToUserId(userId); // ���͸����ѵ��ʼ�
				mailInfo.setUserId(userId);
				mailInfo.setAccount(emailType);
				mailInfo.setMailSize(em.getMailSize());
				mailInfo.setMailUID(em.getMessageId());
				mailInfo.setBegReplay(em.isBegReplay()?"1":"0");

				mailInfo.setGroupId(mgt.filterMailFrom(mailInfo)?"1":"4"); //���й������
				// ���浽���ݿ���ȥ
				
				boolean mustSave = true;
				
				//���Ҫ�������������ݣ����豣��guid,���ﲻ����û�б�������������,���洢guid,��Ϊ��Щ����������ɾ����ʶ�󣬲������ɾ��
				//if(sm.retentDay != 0){
					Result ru =mgt.addGuid(mailInfo.getAccount(), mailInfo.getMailUID(),mailInfo.getMailTime());
					if(ru.retCode != ErrorCanst.DEFAULT_SUCCESS){
						mustSave = false;
					}
				//}
				if(mustSave){
					//����emlԭ�ĺ�����
					mailInfo.setMailcharset(em.getHeadCharset());
					mailInfo.setEmlfile(mailInfo.getId()+".eml");
					
					if(sm.setOfRules != null){
						String maddr = GlobalsTool.getMailAddress(mailInfo.getMailFrom());
						for (Iterator iterator = sm.setOfRules.iterator(); iterator
								.hasNext();) {
							String[] filter = (String[]) iterator.next();
							// �����ʼ����û��������ַ �Ƿ�����������������							
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
						//����ɹ����ٴ��ļ�
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
						AIOEMail.emailLog.error("�޷������ʼ�ԭ��",e);
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
		
		//ǰ̨ת��̨����
		if (mailOp.equals("backgroud")) {
			HashMap smMap = (HashMap)request.getSession().getAttribute("AIOEMAILMAP");
			AIOEMail sm = (AIOEMail)smMap.get(emailType);
			//����һ���̼߳���ִ�ж���
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
		String emailType = request.getParameter("emailType"); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		String groupId = request.getParameter("groupId");
		request.setAttribute("isprev", request.getParameter("isprev"));
		request.setAttribute("curMailId", request.getParameter("curMailId"));
		//��ǩ
		request.setAttribute("labelMap", EMailSettingMgt.getMailLabelMap());
		request.setAttribute("labelList", EMailSettingMgt.getMailLabels());
		

		EMailSearchForm myForm = (EMailSearchForm) form;
		if (myForm == null)
			myForm = new EMailSearchForm();
		// �ʼ�����Ϊ�գ�����ʾ�ڲ��ʼ�
		if (emailType == null || "inner".equals(emailType)) {
			emailType = "";
		}
		request.setAttribute("emailType", emailType);
		
		// �ļ���Ϊ�գ�����ʾ�ռ���
		if (groupId == null || groupId.length() == 0) {
			groupId = "1";
		}
		request.setAttribute("groupId", groupId);

		String userId = getLoginBean(request).getId();
		
		Result rs;
		if(!"assign".equals(emailType)){
			rs = mgt.selectGroupByAccount(userId, emailType); //Ϊ�ڽ�������ʾ�ƶ����Զ����ļ���
			request.setAttribute("groups", rs.retVal);
			//��ѯ�ʻ���Ϣ
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
		String highKeyword = myForm.getHighKeyword();//�߼����ҵĹؼ���
		String searchType = request.getParameter("searchType");//�����б�ؼ����ǴӸ߼����뻹����ͨ
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
			// ��ѯ�ɹ�
			Object[] obj = (Object[])rs.retVal;
			String noReadSum = String.valueOf(obj[0]);
			request.setAttribute("noReadSum", noReadSum);
			LinkedHashMap map = (LinkedHashMap) obj[1];
			
			/**
			 * ������Ҫ�������е���һ������һ����sql���Ͳ���
			 */
			request.getSession().setAttribute("EmailSql", obj[2]);
			request.getSession().setAttribute("EmailParam", obj[3]);
			
			
			String hide = BaseEnv.systemSet.get("HideEmail").getSetting(); // �Ƿ������ʼ���ַ
			request.setAttribute("hide", hide);
			
			EMailMgt mgt = new EMailMgt();
			
			Result r = null;
			
			if (emailType != null && emailType.length() > 0) {
				//�ⲿ�ʼ�����ѯ���еĸ���ͨѸ¼�������й���ͨѸ¼
				//�������ռ��˵�ַ����һ��׼����ѯ��ϵ��
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
			// ��ѯʧ��
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
			// ��ʾ���ʺ���
			// ���Ҹ����ⲿ�ʺ�
			String userId = getLoginBean(request).getId();
			Result result = null;
			// ������ǹ���Ա,ֻ��ѯ��ǰ�û����ⲿ�����˻�
			//if (!userId.equals("1")) {
			//-------��ʹ�ǹ���Ա Ҳֻ�ܲ鿴�����ʼ�������������,1û������˽��2����ǹ������䣬����Ա��������Ϊ�����Թ����˿���---------
				result = mgt.selectAccountByUser(userId);
			//} else {// ����ǹ���Ա���ѯϵͳ�����û����ⲿ�����˻�
			//	result = mgt.selectAllAccount();
			//}			
			request.setAttribute("accountSelect", request.getParameter("accountSelect"));
			request.setAttribute("mailStyle", request.getParameter("mailStyle"));
			request.setAttribute("outteremail", result.getRetVal());
			// �������е��Զ����ļ���
			result = mgt.selectGroupByUser(userId);
			request.setAttribute("emailgroup", result.getRetVal());
			result = mgt.getMailSize(userId);			
			request.setAttribute("mailSize", result.retVal);

			return getForward(request, mapping, "frameLeft");
		} else if ("main".equals(type)) {
			// ��ʾ����������
			// ת����Ӧ�ʺŵ��ռ���
			return queryEmailList(mapping, form, request, response);
		} else if ("stat".equals(type)) {
			// ��ʾ����������
			// ת����Ӧ�ʺŵ��ռ���
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
			// ȡ�ʻ�ǩ��
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
			// ����Ǵ��ҵ��������ӹ���
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

			// ��ʾ�����ҳ��
			return getForward(request, mapping, "frameIndex");
		}
	}
	
	
	/**
	 * ���� ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
//	private ActionForward setAlert(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		/*�������*/
//		String emailId = getParameter("emailId", request);
//		String emailTitle = getParameter("emailTitle", request) ;
//		
//		AlertBean alertBean = new AlertBean();
//		String alertDate = request.getParameter("alertDate"); 		/*��������*/
//		String alertHour = request.getParameter("alertHour"); 		/*����Сʱ*/
//		String alertMinute = request.getParameter("alertMinute"); 	/*���ѷ���*/
//		String isLoop = request.getParameter("isLoop"); 		  	/*ѭ������*/
//		String loopType = request.getParameter("loopType"); 		/*ѭ������*/
//		int loopTime = Integer.parseInt(request.getParameter("loopTime")); /*ѭ������*/
//		String endDate = request.getParameter("endDate"); 			/*��������*/
//		String[] alertType = request.getParameterValues("alertType"); /*���ѷ�ʽ*/
//		if (alertType != null && alertType.length > 0) {
//			String strAlertType = "";
//			for (String strType : alertType) {
//				strAlertType += strType + ",";
//			}
//			alertBean.setId(IDGenerater.getId());
//			alertBean.setAlertDate(alertDate);
//			alertBean.setAlertHour(Integer.parseInt(alertHour));
//			alertBean.setAlertMinute(Integer.parseInt(alertMinute));
//			alertBean.setAlertContent("�ʼ����ѣ�" + emailTitle);
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
//			EchoMessage.success().add("����������óɹ�")
//					.setBackUrl("/EMailAction.do?operation=5&iframe=true&keyId="+emailId)
//					.setAlertRequest(request);
//		}else{
//				EchoMessage.error().add("�����������ʧ��")
//						.setBackUrl("/EMailAction.do?operation=5&iframe=true&keyId="+emailId)
//						.setAlertRequest(request);
//		}
//		return getForward(request, mapping, "alert") ;
//	}
//	
//	/**
//	 * ȡ�� ��������
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
//			EchoMessage.success().add("ȡ���������óɹ�")
//					.setBackUrl("/EMailAction.do?operation=5&iframe=true&keyId="+emailId)
//					.setAlertRequest(request);
//		}else{
//				EchoMessage.error().add("ȡ����������ʧ��")
//						.setBackUrl("/EMailAction.do?operation=5&iframe=true&keyId="+emailId)
//						.setAlertRequest(request);
//		}
//		return getForward(request, mapping, "alert") ;
//	}
//	
	/**
	 * mj  �ʼ����˹���
	 */
	public String getGroupId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,String setId,OAMailInfoBean mailInfo,String emailType,String userId){
		Result rs;
		//��ø������Ӧ����ϸ������Ϣ
		Result rsOfFilterInfo = efMgt.getFilterInfoByMsId(setId);
		String[] filterArr = null;
		String groupId = "1";
		if (rsOfFilterInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {// ��ѯ�ɹ�
			//�õ����˹�������
			List<EmailFilter> list = (List<EmailFilter>) rsOfFilterInfo.retVal;
			EmailFilter filter = list.get(0);
			String rule = filter.getFilterCondition();
			rsOfFilterInfo = efMgt.getArrayByStrSplit(rule);
			filterArr = (String[])rsOfFilterInfo.getRetVal();
			Boolean isContainFrmEmi = false;
			for (int i = 0; i < filterArr.length; i++) {
				String f = filterArr[i];
				//�����ʼ����û��������ַ �Ƿ����������������� 
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
				//�����ļ��������Ҷ�Ӧ��groupId
				rs = efMgt.getGroupIdMapByFolder(folderName, userId, setId);
				if(rs != null){
					groupId = (String)rs.getRetVal();
				} else {
					 //��ѯʧ��
					EchoMessage.error().add(getMessage(request, "common.msg.error"))
							.setRequest(request);
					//return getForward(request, mapping, "message");
					return null;
				}
				
			}
			return groupId;
		} else {
			// ��ѯʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			//return getForward(request, mapping, "message");
			return null;
		}
		
	}
	
	/**
	 * �鿴�ռ�����ϸ��Ϣ
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
		 * �����ʼ�groupId��չʾ��ͬ����Ϣ���ͻ�,����Ƿ����� �˿� ������ɾ�� ȫѡ �Լ�����֪ͨͨ��
		 * ����ֻ�ܲ鿴�Ƿ��Ѿ��Ķ�����Ϣ
		 */
		String title = getParameter("emailTitle", request);
		if (StringUtils.isNotBlank(title)) {
			title = GlobalsTool.toChinseChar(title);
		}
		String groupId = getParameter("groupId", request);
		//���ݸ�id��ѯ�õ����е��ռ����ʼ���Ϣ(�ռ������� �������� �Ķ�ʱ�� �Ƿ��Ķ�) mj
		String pId = getParameter("pId", request);
		Result rs = mgt.queryReceives(pId, "","");// ��ʾ���� ���Ҳ���ʾɾ����
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("receives", rs.getRetVal());
			request.setAttribute("emailTitle", title);
			request.setAttribute("groupId", groupId);
			request.setAttribute("pId", pId);//���ʼ�
			if (StringUtils.equals(groupId, "3")) {// ������
				request.setAttribute("isOperation", true);// �����и��ֲ���
			} else {
				request.setAttribute("isOperation", false);
			}
		} else if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			// ��ѯʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "readStatus");
	}
	
	/**
	 * �����ʼ�
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
		String emailType = request.getParameter("emailType"); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
		String groupId = request.getParameter("groupId");
		request.setAttribute("emailType", emailType); // �ʺ�����,���ڲ��⣬�����ڲ�������ʺŴ���
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
			// ִ��ɾ��
			for (String s : nstr) {
				String fullName = (String)mgt.getFullNameByEmailId(s).getRetVal();
				if (StringUtils.isNotBlank(fullName)){
					//����������ĵ�ǰ�ռ��� ������ bcc cc to�� Ӧ�ý� bcc  cc to�ж�Ӧ��ɾ���� ���� ������ mailDetail.jsp���治��Ӧ������ �鿴��������
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
			Result rs = mgt.queryReceives(pId, "","");// ��ʾ���� ���Ҳ���ʾɾ����
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
	 * ��дȨ���жϵķ���,��Ϊ��ģ�鲻��ҪȨ���ж�,���Է���null
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
	 * �ղ��ʼ�
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
	 * �����ұ����ݣ���֯�ܹ���ͨѶ¼���ͻ���
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
			//�ⲿ�ʼ���ȡ���ݣ�ͨѶ¼����)
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
		
		//��ѯ��֯�ܹ������ţ�ְԱ��
		rs = mgt.queryDeptOrEmp(keyword);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String folderTree = "";
			//��ѯ�ɹ�
			Object[] obj = (Object[])rs.retVal;
			List deptList = (ArrayList)obj[0];
			List empList = (ArrayList)obj[1];
			//�����ݽ��д����������ϳɽڵ���ʽ
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
	 * ��֯�ܹ���������
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
	 * ��ѯ�ռ�������
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
		/* TextBoxList�ؼ����ݵ�ѡȡ */
		if("".equals(emailType)){
			//�ڲ��ʼ����õ�ְԱ�Ͳ��ţ�
			ArrayList<String[]> list = textBoxUtil.getUsersValues("openSystem");
			ArrayList<String[]> deptList = textBoxUtil.getDeptValues();
			for(int i=0;i<deptList.size();i++){
				list.add(deptList.get(i));
			}
			request.setAttribute("msg",gson.toJson(list));
		}else{
			//�ⲿ����ȡ����email��ַ��ְԱ�Ϳͻ���ϵ��
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
	 * �����ʼ�
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
	 * ɾ���ʼ�������Ա����ʷ��¼
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
