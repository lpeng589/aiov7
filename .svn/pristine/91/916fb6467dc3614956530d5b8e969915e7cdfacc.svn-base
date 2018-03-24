package com.koron.oa.oaReadingRecord;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dbfactory.Result;
import com.koron.oa.bean.OAReadingRecord;
import com.menyi.web.util.*;
import org.apache.struts.action.*;
import java.util.List;

/**
 * <p>
 * Title: 阅读痕迹控制类
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * 
 * @author 汤晶
 * @version 4.0
 */
public class OAReadingRecordAction extends BaseAction {

	OAReadingRecordMgt mgt = new OAReadingRecordMgt();

	public OAReadingRecordAction() {
	}

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

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {

		// 根据条件查询
		case OperationConst.OP_QUERY:
			forward = queryByConditions(mapping, form, request, response);
			break;
		// 删除
		case OperationConst.OP_DELETE:
			forward = delReadingRecord(mapping, form, request, response);
			break;
		default:
			// 查询所有记录
			forward = query(mapping, form, request, response);

		}
		return forward;
	}

	/*
	 * 查出对应内容的所有阅读记录
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String readingInfoTable = request.getParameter("readingInfoTable");// 获取信息内容对应表
		String readingInfoId = request.getParameter("readingInfoId"); // 获取信息内容对应ID
		String url = request.getParameter("url");// 链接地址
		request.setAttribute("readingInfoTable", readingInfoTable);
		request.setAttribute("readingInfoId", readingInfoId);
		request.setAttribute("url", url );
		
		Result listInfo = null;
		List<OAReadingRecord> listOAReadingRecord = new ArrayList<OAReadingRecord>();
		// 根据表名和id获得该内容的所有阅读记录
		listInfo = mgt.selectRecordByContent(readingInfoTable, readingInfoId);
		if (listInfo.getRetVal() != null) {
			listOAReadingRecord = (List<OAReadingRecord>) listInfo.getRetVal();
		}
		// 获取所有的职位
		Result re = mgt.selectAllJobName(request);
		if (re.getRetVal() != null) {
			List listReadingRecord = (List) re.getRetVal();
			request.getSession().setAttribute("list_ReadingInfo",
					listReadingRecord);
		}

		request.setAttribute("listOAReadingRecord", listOAReadingRecord);
		return getForward(request, mapping, "showReadingInfo");

	}

	/*
	 * 根据传入要删除的ID组进行批量删除
	 */
	protected ActionForward delReadingRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 获取查询参数
		String jobName = request.getParameter("jobName");// 职位
		String department = request.getParameter("department"); // 部门
		String userName = request.getParameter("userName"); // 职员姓名
		String readingInfoTable = request.getParameter("readingInfoTable");// 获取信息内容对应表
		String readingInfoId = request.getParameter("readingInfoId"); // 获取信息内容对应ID
		String url = request.getParameter("url");// 链接地址
		// 中文参数转码
		if ("GET".equals(request.getMethod())) {
			userName = userName == null ? "" : GlobalsTool
					.toChinseChar(userName);
			department = department == null ? "" : GlobalsTool
					.toChinseChar(department);
		}
		String fileIds = request.getParameter("fileIds");
		String fileIds_array[] = fileIds.split(",");
		boolean talg = true;
		for (String fileId : fileIds_array) {
			// 调Mgt删除数据
			Result rs = mgt.deleltOAReadingRecord(fileId);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				talg = true;

			} else {
				talg = false;
			}
		}

		// 删除成功
		if (talg) {
			EchoMessage.success().add(
					getMessage(request, "common.msg.delSuccess")).setBackUrl(
					"/OAReadingRecordAction.do?operation=4&url="
							+ GlobalsTool.encode(url) + "&readingInfoId="
							+ readingInfoId + "&readingInfoTable="
							+ readingInfoTable + "&department="
							+ GlobalsTool.encode(department) + "&jobName="
							+ GlobalsTool.encode(jobName) + "&userName="
							+ GlobalsTool.encode(userName)).setAlertRequest(
					request);
		} else {
			EchoMessage.success().add(
					getMessage(request, "common.msg.delError")).setBackUrl(
					"/OAReadingRecordAction.do?operation=4&url=" + url
							+ "&readingInfoId=" + readingInfoId
							+ "&readingInfoTable=" + readingInfoTable
							+ "&department=" + GlobalsTool.encode(department)
							+ "&jobName=" + GlobalsTool.encode(jobName)
							+ "&userName=" + GlobalsTool.encode(userName))
					.setAlertRequest(request);
		}

		return getForward(request, mapping, "message");
	}

	/*
	 * 根据阅读者所属部门&职位&职员姓名查找对应信息,以便更明了的指导具体哪些人看过此阅读内容
	 */
	protected ActionForward queryByConditions(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 获取查询参数
		String jobName = request.getParameter("jobName");// 职位
		String department = request.getParameter("department"); // 部门
		String userName = request.getParameter("userName"); // 职员姓名
		String readingInfoTable = request.getParameter("readingInfoTable");// 获取信息内容对应表
		String readingInfoId = request.getParameter("readingInfoId"); // 获取信息内容对应ID
		String url = request.getParameter("url");// 链接地址
		// 中文参数转码
		if ("GET".equals(request.getMethod())) {
			userName = userName == null ? "" : GlobalsTool
					.toChinseChar(userName);
			department = department == null ? "" : GlobalsTool
					.toChinseChar(department);
		}
		request.setAttribute("jobName", jobName);
		request.setAttribute("department", department);
		request.setAttribute("userName", userName);
		request.setAttribute("readingInfoTable", readingInfoTable);
		request.setAttribute("readingInfoId", readingInfoId);
		request.setAttribute("url", url);

		Result listInfo = null;
		List<OAReadingRecord> listOAReadingRecord = new ArrayList<OAReadingRecord>();
		// 根据查询条件获得该内容的阅读记录
		listInfo = mgt.selectRecordByConditions(readingInfoTable,
				readingInfoId, jobName, department, userName);
		if (listInfo.getRetVal() != null) {
			listOAReadingRecord = (List<OAReadingRecord>) listInfo.getRetVal();
		}
		request.setAttribute("listOAReadingRecord", listOAReadingRecord);
		return getForward(request, mapping, "showReadingInfo");

	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String[] args) {
		List<String[]> list = new ArrayList<String[]>();
		String[] s = new String[3];
		s[0] = "a";
		Calendar cal = Calendar.getInstance();
		cal.get(Calendar.YEAR);
		//s[1] = 
		
	}
}
