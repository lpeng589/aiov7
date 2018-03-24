package com.menyi.email.emailFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dbfactory.Result;
import com.koron.oa.bean.EmailFilter;
import com.menyi.web.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;

import java.util.Date;
import java.util.List;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.email.EMailMgt;


/**
 * ������� ë�� Description: <br/>Copyright (C), 2008-2012, Justin.T.Wang <br/>This
 * Program is protected by copyright laws. <br/>Program Name: <br/>Date:
 * 
 * @autor Justin.T.Wang yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class EmailFilterAction extends MgtBaseAction {

	private EmailFilterMgt efMgt = new EmailFilterMgt();
	EMailMgt mgt = new EMailMgt();


	/**
	 * exe ��������ں���
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
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String type = request.getParameter("type") == null ? "" : request
				.getParameter("type");
		request.setAttribute("type", type);
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
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
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			forward = detail(mapping, form, request, response);
			break;
		case OperationConst.OP_READ_OVER:
			request.getParameter("filterId");
			forward = getAllEmailForldToHTML(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
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
		
		request.removeAttribute("operation");
		// ���ò�ѯall folder�ķ���
//		Result folderResult = fMgt.getAllFolder();
//		
//		if (folderResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			request.setAttribute("folderList", folderResult.retVal);
//		} else {
//			// ��ѯʧ��
//			EchoMessage.error().add(getMessage(request, "common.msg.error"))
//					.setAlertRequest(request);
//			return getForward(request, mapping, "alert");
//		}
//		
		String userId = this.getLoginBean(request).getId();
		String mainAccount = request.getParameter("mainAccount");
		// ��ѯ����ӵ�е������˺�
		Result emailRs = efMgt.selectMailAccountByUser(userId, mainAccount);
		if (emailRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("emailList", emailRs.getRetVal());
		} else {
			// ��ѯʧ��
			throw new BusinessException(getMessage(request, "common.msg.error"));
		}
		return getForward(request, mapping, "emailFilterAdd");
	}

	/**
	 * ���
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

		EmailFilterForm myForm = (EmailFilterForm) form;
		String filterCondition = myForm.getFilterCondition();
		String folderId = myForm.getRefOaFolderId();
		String mailSettingId = myForm.getRefOaMailinfoSettingId();
		EmailFilter emailFilter = new EmailFilter();
		String id = IDGenerater.getId();
		emailFilter.setFilterCondition(filterCondition);
		emailFilter.setCreateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		emailFilter.setLastUpdateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		emailFilter.setRefOaFolderId(folderId);
		emailFilter.setId(id.toString());
		emailFilter.setRefOaMailinfoSettingId(mailSettingId);
		String userId = this.getLoginBean(request).getId();
		emailFilter.setUserId(userId);
		Result rs = efMgt.add(emailFilter);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ��ӳɹ�
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/EmailFilterQueryAction.do?operation=4").setAlertRequest(
					request);
			return getForward(request, mapping, "message");
		} else {
			// ���ʧ��
			EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * �޸�ǰ��׼��
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
	protected ActionForward updatePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Result rs = new Result();
		String emailFilterId = request.getParameter("keyId");
		if (StringUtils.isNotBlank(emailFilterId)) {
			// ��ѯ��Ҫ�޸ĵĵ�emailFilter
			rs = efMgt.getEmailFilterById(emailFilterId);

		} else {
			rs.setRetCode(ErrorCanst.NULL_OBJECT_ERROR);
		}
		

		
		String userId = ((LoginBean) request.getSession().getAttribute(
				"LoginBean")).getId();
		String mainAccount = request.getParameter("mainAccount");
		// ��ѯ����ӵ�е���������
		Result emailRs = efMgt.selectMailAccountByUser(userId, mainAccount);
		if (emailRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("emailList", emailRs.getRetVal());
		} else {
			// ��ѯʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ���سɹ� ����
			EmailFilter ef = (EmailFilter) rs.getRetVal();
			if (ef != null) {
				request.setAttribute("filter", ef);
			}
			// ������еĸ��������ļ��У��Զ��壩
			List folderResult = this.getAllFolderByUid(userId, ef
					.getRefOaMailinfoSettingId());
			request.setAttribute("folderList", folderResult);
		} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// ��¼�����ڴ���
			EchoMessage.error().add(getMessage(request, "common.error.nodata"))
					.setAlertRequest(request);
		} else {
			// ����ʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
		}

		return getForward(request, mapping, "emailFilterUpdate");
	}

	/**
	 * �޸� mj
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

		EmailFilterForm myForm = (EmailFilterForm) form;
		String filterCondition = myForm.getFilterCondition();
		String folderId = myForm.getRefOaFolderId();
		String mailSettingId = myForm.getRefOaMailinfoSettingId();
		String id = myForm.getId();
		// ��ø�ʵ�����
		EmailFilter emailFilter = (EmailFilter) efMgt.getEmailFilterById(id)
				.getRetVal();
		emailFilter.setLastUpdateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		emailFilter.setFilterCondition(filterCondition);
		emailFilter.setRefOaFolderId(folderId);
		emailFilter.setRefOaMailinfoSettingId(mailSettingId);
	
		Result rs = efMgt.update(emailFilter);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// �޸ĳɹ�
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl("/EmailFilterQueryAction.do?operation=4")
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// �޸�ʧ��
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * ��ѯ
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
	@SuppressWarnings("unchecked")
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EmailFilterSearchForm filterForm = (EmailFilterSearchForm) form;
		// ��ѯ���е�ǰ��¼�˵��ʼ����� 
		String userId = this.getLoginBean(request).getId();
		
		Result rs = efMgt.queryAllInfoByKeyWord(userId,filterForm.getOaMailinfoSetting(),
				filterForm.getFolder(), filterForm.getFilterCondition(),null,filterForm.getPageNo(),
				filterForm.getPageSize());

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ��ѯ�ɹ�
			// �����û�����Ĳ�ѯ���������û�
			List<EmailFilter> list = (List<EmailFilter>) rs.retVal;
			request.setAttribute("result", list);
			request.setAttribute("pageBar", pageBar(rs, request));

		} else {
			// ��ѯʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "emailFilterList");
	}

	// ���ظ��������˺Ŷ�Ӧ�����и��������ļ���HTML
	protected ActionForward getAllEmailForldToHTML(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ���������Ķ�Ӧ�������˺�ID
		String account = request.getParameter("account");
		System.out.println("accout="+account);
		String userId = ((LoginBean) request.getSession().getAttribute(
				"LoginBean")).getId();
		List list = getAllFolderByUid(userId, account);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			String name = obj[1].toString();
			String groupId = obj[0].toString();
			String option = "<option value = '" + groupId + "'>" + name
					+ "</option>";
			sb.append(option);
		}
		request.setAttribute("msg", sb.toString());
		return getForward(request, mapping, "blank");
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
	@SuppressWarnings("unchecked")
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id");
		if (StringUtils.isNotBlank(id)) {
			List<EmailFilter> list = (List<EmailFilter>) efMgt
					.queryInfoById(id).retVal;
			EmailFilter filter = list.get(0);
			request.setAttribute("result", filter);
			// EmailFilter ef = (EmailFilter) efMgt.getEmailFilterById(id)
			// .getRetVal();
			// request.setAttribute("folder",(OAFolder)efMgt.getFolderById(ef.getRefOaFolderId()).getRetVal());
			// request.setAttribute("mailinfoSettingBean",(MailinfoSettingBean)efMgt.getMailinfoSettingBeanById(ef.getRefOaMailinfoSettingId()).getRetVal());
			// request.setAttribute("filter", ef);
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");

		}
		return getForward(request, mapping, "emailFilterDetail");
	}

	/**
	 * ��ѯ���е��˺Ŷ�Ӧ���Զ����ļ���
	 */
	public List getAllFolderByUid(String userId, String accout) {
		// ���Ҹ����ⲿ�ʺ�
		Result result = null;
		// �������е��Զ����ļ���
		result = mgt.selectGroupByAccount(userId, accout);
		return (List) result.getRetVal();
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

		ActionForward forward = null;
		String nstr[] = request.getParameterValues("keyId");
		if (nstr != null && nstr.length != 0) {

			Result rs = efMgt.delete(nstr);

			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// ɾ���ɹ�
				request.setAttribute("result", rs.retVal);
				forward = query(mapping, form, request, response);
			} else {
				// ɾ��ʧ��
				EchoMessage.error().add(
						getMessage(request, "common.msg.delError")).setRequest(
						request);
				forward = getForward(request, mapping, "message");
			}
		}
		return forward;
	}
	

}
