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
 * 邮箱过滤 毛晶 Description: <br/>Copyright (C), 2008-2012, Justin.T.Wang <br/>This
 * Program is protected by copyright laws. <br/>Program Name: <br/>Date:
 * 
 * @autor Justin.T.Wang yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class EmailFilterAction extends MgtBaseAction {

	private EmailFilterMgt efMgt = new EmailFilterMgt();
	EMailMgt mgt = new EMailMgt();


	/**
	 * exe 控制器入口函数
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
		// 跟据不同操作类型分配给不同函数处理
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
		
		request.removeAttribute("operation");
		// 调用查询all folder的方法
//		Result folderResult = fMgt.getAllFolder();
//		
//		if (folderResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			request.setAttribute("folderList", folderResult.retVal);
//		} else {
//			// 查询失败
//			EchoMessage.error().add(getMessage(request, "common.msg.error"))
//					.setAlertRequest(request);
//			return getForward(request, mapping, "alert");
//		}
//		
		String userId = this.getLoginBean(request).getId();
		String mainAccount = request.getParameter("mainAccount");
		// 查询个人拥有的邮箱账号
		Result emailRs = efMgt.selectMailAccountByUser(userId, mainAccount);
		if (emailRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("emailList", emailRs.getRetVal());
		} else {
			// 查询失败
			throw new BusinessException(getMessage(request, "common.msg.error"));
		}
		return getForward(request, mapping, "emailFilterAdd");
	}

	/**
	 * 添加
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
			// 添加成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/EmailFilterQueryAction.do?operation=4").setAlertRequest(
					request);
			return getForward(request, mapping, "message");
		} else {
			// 添加失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * 修改前的准备
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
			// 查询需要修改的的emailFilter
			rs = efMgt.getEmailFilterById(emailFilterId);

		} else {
			rs.setRetCode(ErrorCanst.NULL_OBJECT_ERROR);
		}
		

		
		String userId = ((LoginBean) request.getSession().getAttribute(
				"LoginBean")).getId();
		String mainAccount = request.getParameter("mainAccount");
		// 查询个人拥有的所有邮箱
		Result emailRs = efMgt.selectMailAccountByUser(userId, mainAccount);
		if (emailRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("emailList", emailRs.getRetVal());
		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 加载成功 保存
			EmailFilter ef = (EmailFilter) rs.getRetVal();
			if (ef != null) {
				request.setAttribute("filter", ef);
			}
			// 获得所有的个人邮箱文件夹（自定义）
			List folderResult = this.getAllFolderByUid(userId, ef
					.getRefOaMailinfoSettingId());
			request.setAttribute("folderList", folderResult);
		} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// 记录不存在错误
			EchoMessage.error().add(getMessage(request, "common.error.nodata"))
					.setAlertRequest(request);
		} else {
			// 加载失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setAlertRequest(request);
		}

		return getForward(request, mapping, "emailFilterUpdate");
	}

	/**
	 * 修改 mj
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
		// 获得该实体对象
		EmailFilter emailFilter = (EmailFilter) efMgt.getEmailFilterById(id)
				.getRetVal();
		emailFilter.setLastUpdateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		emailFilter.setFilterCondition(filterCondition);
		emailFilter.setRefOaFolderId(folderId);
		emailFilter.setRefOaMailinfoSettingId(mailSettingId);
	
		Result rs = efMgt.update(emailFilter);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 修改成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl("/EmailFilterQueryAction.do?operation=4")
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * 查询
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
		// 查询所有当前登录人的邮件过滤 
		String userId = this.getLoginBean(request).getId();
		
		Result rs = efMgt.queryAllInfoByKeyWord(userId,filterForm.getOaMailinfoSetting(),
				filterForm.getFolder(), filterForm.getFilterCondition(),null,filterForm.getPageNo(),
				filterForm.getPageSize());

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 查询成功
			// 根据用户输入的查询条件过滤用户
			List<EmailFilter> list = (List<EmailFilter>) rs.retVal;
			request.setAttribute("result", list);
			request.setAttribute("pageBar", pageBar(rs, request));

		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "emailFilterList");
	}

	// 返回各个邮箱账号对应的所有个人邮箱文件夹HTML
	protected ActionForward getAllEmailForldToHTML(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 获得邮箱组的对应的邮箱账号ID
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
	 * 详细
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
	 * 查询所有的账号对应的自定义文件夹
	 */
	public List getAllFolderByUid(String userId, String accout) {
		// 查找个人外部帐号
		Result result = null;
		// 查找所有的自定义文件夹
		result = mgt.selectGroupByAccount(userId, accout);
		return (List) result.getRetVal();
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

		ActionForward forward = null;
		String nstr[] = request.getParameterValues("keyId");
		if (nstr != null && nstr.length != 0) {

			Result rs = efMgt.delete(nstr);

			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 删除成功
				request.setAttribute("result", rs.retVal);
				forward = query(mapping, form, request, response);
			} else {
				// 删除失败
				EchoMessage.error().add(
						getMessage(request, "common.msg.delError")).setRequest(
						request);
				forward = getForward(request, mapping, "message");
			}
		}
		return forward;
	}
	

}
