package com.koron.oa.publicMsg.newsInfo;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dbfactory.Result;
import com.koron.oa.bbs.forum.OABBSForumMgt;
import com.koron.oa.bean.Department;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.OANewsInfoReplyBean;
import com.koron.oa.message.MessageMgt;
import com.koron.oa.publicMsg.advice.OAAdviceMgt;
import com.koron.oa.util.EmployeeMgt;
import com.menyi.web.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;
import java.util.List;

import com.menyi.aio.bean.AdviceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;

/**
 * <p>
 * Title: ���ſ�����
 * </p>
 */
public class OANewsInfoAction extends MgtBaseAction {

	OANewsInfoMgt mgt = new OANewsInfoMgt();
	OAAdviceMgt adviceMgt = new OAAdviceMgt();
	OABBSForumMgt bbsMgt = new OABBSForumMgt() ;
	MessageMgt mmgt = new MessageMgt();
	EmployeeMgt empMgt = new EmployeeMgt();
	private static Gson gson ;//mj
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
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
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// ���ܷ���
		String replyId = request.getParameter("replyId");
		
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = add_NesInfo(mapping, form, request, response);
			break;
		// ��������ǰ��׼��
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		// �޸�
		case OperationConst.OP_UPDATE:
			String type_upd = request.getParameter("type_upd");
			if (!"".equals(type_upd) && type_upd != null) {
				if (type_upd.equals("upd_newsInfo")) { // �޸�����
					forward = upd_newsInfo(mapping, form, request, response);
				} else if (type_upd.equals("upd_addNewsInfo")) { // ��δ���������ŷ���
					forward = upd_addNewsInfo(mapping, form, request, response);
				} else if("agreeReply".equals(type_upd)) {
					//�� ���� ����
					forward = addAgreeNumOfReply(mapping, form, request, response);//
				}
				break;
			}

			// //��ѯ
		case OperationConst.OP_QUERY:
			String type_query = request.getParameter("type_query");
			String type_update = request.getParameter("type_upd");
			
			if (("".equals(type_query) || type_query == null)&&type_update == null) { // ��������ѯ
				forward = news_condition(mapping, form, request, response);
			} else if ("agreeReply".equals(type_query)&&"agreeReply".equals(type_update)) {
				// ��ѯ
				forward = addAgreeNumOfReply(mapping, form, request, response);
				break;
			} else {
				if ("to_updNewsInfo".equals(type_query)) { // ��ѯ�޸�����
					forward = to_updNewsInfo(mapping, form, request, response);
				} else {
					forward = showAllReplys(mapping, form, request, response);
				}
			}
			
			break;
		// ������ϸ
		case OperationConst.OP_DETAIL:
			forward = to_particularNews(mapping, form, request, response);
			break;

		// ɾ��
		case OperationConst.OP_DELETE:
			if (StringUtils.isNotBlank(replyId)) {//mj
				forward = delReplyNewsInfo(mapping, form, request, response,replyId);
			} else {
				forward = delNewsInfo(mapping, form, request, response);
			}	
			break;
		//�ظ�
		case OperationConst.OP_SEND:
			forward =  replyNewsInfo(mapping, form, request, response);
			break;
		case OperationConst.OP_URL_TO:
			forward = showAllReplys(mapping,form,request,response);
			break;
		case OperationConst.OP_READ_OVER:
			forward = addAgreeNumOfReply(mapping, form, request, response);//OAadviceAction����� ʹ��ajax
			break;
		default:
			forward = query(mapping, form, request, response); // ������ҳ
		}
		return forward;
	}

	/**
	 * ��������ѯ
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
	protected ActionForward news_condition(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// ȡ���û��ڸ�ģ���е�Ȩ��
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/OACompanyNewsInfo.do");
		request.setAttribute("add", mop.add()); // ����Ȩ��
		request.setAttribute("del", mop.delete()); // ɾȨ��
		request.setAttribute("upd", mop.update()); // ��Ȩ��

		String newsTitle = request.getParameter("newsTitle"); // ������ű���
		String newsType = request.getParameter("newsType"); // ����������
		String beginTime = request.getParameter("beginTime"); // ����ʱ��ǰ
		String endTime = request.getParameter("endTime"); // ����ʱ���
		String proUserId = request.getParameter("proUserId");// ������id
		String proUserName = request.getParameter("proUserName");// ������
		// ���Ĳ���ת��
		if ("GET".equals(request.getMethod())) {
			newsTitle = newsTitle == null ? "" : GlobalsTool
					.toChinseChar(newsTitle);
			proUserName = proUserName == null ? "" : GlobalsTool
					.toChinseChar(proUserName);
		}
		// ������������request�У����ظ��û�
		request.setAttribute("newsTitle", newsTitle);
		request.setAttribute("newsType", newsType);
		request.setAttribute("beginTime", beginTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("proUserId", proUserId);
		request.setAttribute("proUserName", proUserName);

		String userId = getLoginBean(request).getId();
		String empGroup = empMgt.selectEmpGroupByUid(userId);// ְԱ����
		int length = 0;// �ж��Ƿ��ְԱ�ڶ���鵱��
		String temp = null;
		if (empGroup != null && empGroup != "") {
			length = empGroup.length();
			temp = "'" + empGroup + "'";
		} else {
			empGroup = null;
		}
		if (length > 29) {
			temp = "";
			String[] array = empGroup.split(",");
			for (int i = 0; i < array.length; i++) {
				if (i == array.length - 1) {
					temp += "'" + array[0] + ",'";
				} else {
					temp += "'" + array[0] + ",',";
				}
			}
		}
		String classCode = adviceMgt.queryClassCodeByUserId(userId);
		Result rs_newsInfo = mgt.sel_CompanyNewsInfo(userId, classCode,
				newsType, newsTitle, beginTime, endTime, proUserName, empGroup,
				temp);
		List ls_newsInfo = (List) rs_newsInfo.getRetVal();
		List ls_news = new ArrayList();
		if (!"".equals(ls_newsInfo) && null != ls_newsInfo) {
			for (int i = 0; i < ls_newsInfo.size(); i++) {
				String[] arr_new = new String[9];
				String[] arr_news = (String[]) ls_newsInfo.get(i);
				Result rs_user = mgt.sel_employee(arr_news[5]);
				String arr_user[] = (String[]) rs_user.getRetVal();
				arr_new[0] = arr_news[0];
				arr_new[1] = arr_news[1];
				arr_new[2] = arr_news[2];
				arr_new[3] = arr_news[3];
				arr_new[4] = arr_news[4];
				arr_new[5] = arr_user[1];
				arr_new[6] = arr_news[6];
				arr_new[7] = (i + 1) + "";
				arr_new[8] = arr_news[7];
				ls_news.add(arr_new);
			}
		}
		// ��ҳ
		request.setAttribute("ls_newsInfo", ls_news);
		request.setAttribute("loginBean", loginBean);
		// request.setAttribute("ls_newsInfo", ls_news);
		return getForward(request, mapping, "newsIndex");
	}

	/**
	 * ��δ���������ŷ���
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
	protected ActionForward upd_addNewsInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ������ͨ����������ѯ���������ݽ����޸ĵģ�Ӧ�������ѿͻ���ѯ��������
		String newsType1 = request.getParameter("newsType1") == null ? ""
				: request.getParameter("newsType1");
		String newsTitle1 = request.getParameter("newsTitle1") == null ? ""
				: request.getParameter("newsTitle1");
		String proUserName1 = request.getParameter("proUserName1") == null ? ""
				: request.getParameter("proUserName1");
		String beginTime1 = request.getParameter("beginTime1") == null ? ""
				: request.getParameter("beginTime1");
		String endTime1 = request.getParameter("endTime1") == null ? ""
				: request.getParameter("endTime1");
		String pageNo = request.getParameter("pageNo") == null ? "" : request
				.getParameter("pageNo");
		String userId = this.getLoginBean(request).getId(); // ��õ�½��ID
		String newsId = request.getParameter("newsId"); // �������ID
		//mj ֪ͨͨ�� ��� �����޸�Ϊδ���� ֪ͨͨ������ȴ���Կ���
		//�޸� �����Ŷ�Ӧ��֪ͨͨ�� �Ƿ���� ���ڸı�
		AdviceMgt avMgt = new AdviceMgt();
		Result rs = avMgt.getBeanByReId(newsId);
		List<AdviceBean> list = (List<AdviceBean>)rs.getRetVal();
		if (list.size() > 0 ) {
			AdviceBean bean =  list.get(0);
			if (bean != null){
				bean.setExist("all");
				rs = avMgt.updateIsUsedById(bean.getId(),"all");
			}

		}
				
		
		Result rs_news = mgt.upd_newsInfo_used(newsId, userId, userId);
		if (rs_news.retCode == ErrorCanst.DEFAULT_SUCCESS) {

			// ������ѷ�ʽ
			Result rs_new = mgt.sel_CompanyNewsInfo_By_newsId(newsId); // ��������Id��ѯ���������Ϣ
			String[] str_news = (String[]) rs_new.getRetVal();
			String isAlonePopedmon = str_news[10];
			String popedomDeptId = str_news[9];
			String popedomUserId = str_news[8];

			String popedomUserIds = ""; // ֪ͨ����
			if ("1".equals(isAlonePopedmon)) { // ����������
				// ��֪ͨ���͸�ÿһ��֪ͨ����
				if (null != popedomDeptId && !"".equals(popedomDeptId)) {
					String[] deptIds = popedomDeptId.split(","); // ���ݲ��ű�Ų��Ҳ�����Ա
					for (String departId : deptIds) {
						List<Employee> list_emp = adviceMgt
								.queryAllEmployeeByClassCode(departId);
						for (Employee emp : list_emp) {
							popedomUserIds += emp.getid() + ",";
						}
					}
				}
				popedomUserIds += popedomUserId;

			} else {
				List listEmp = (List) adviceMgt.sel_allEmployee().getRetVal();
				for (int i = 0; i < listEmp.size(); i++) { // ѭ������
					popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
				}
			}

			String title = GlobalsTool.getMessage(request, "oa.new.newCenter")
					+ "(" + str_news[2] + ")"; // ����

			String url = request.getRequestURI();
			String favoriteURL = java.net.URLEncoder.encode(url
					+ "?noback=true&operation=5&newsId=" + newsId
					+ "&isEspecial=1");
			String content = "<a href=javascript:mdiwin('" + favoriteURL
					+ "','"
					+ GlobalsTool.getMessage(request, "oa.common.newList")
					+ "')>" + title + getMessage(request, "com.click.see")
					+ "</a>"; // ����

			String[] wakeType = null; // ���ѷ�ʽ
			if (str_news[7] != null && !"".equals(str_news[7])) {
				wakeType = str_news[7].split(",");
			}
			if (wakeType != null && wakeType.length > 0) {
				for (String type : wakeType) {
					new Thread(new NotifyFashion(userId, title, content,
							popedomUserIds, Integer.parseInt(type), "yes",
							newsId)).start(); // ������֪�߳�
				}
			}
			// �����ɹ�
			EchoMessage.success().add(
					getMessage(request, "oa.publicMsg.newsInfo.ReleaseSucc"))
					.setBackUrl(
							"/OACompanyNewsInfo.do?operation=4&newsType="
									+ newsType1 + "&newsTitle=" + newsTitle1
									+ "&proUserName=" + proUserName1
									+ "&beginTime=" + beginTime1 + "&endTime="
									+ endTime1 + "&pageNo=" + pageNo)
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// ����ʧ��
			EchoMessage.error().add(
					getMessage(request, "oa.publicMsg.newsInfo.ReleaseFaile"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	/**
	 * ��������
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
	protected ActionForward add_NesInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String userId = this.getLoginBean(request).getId(); // ��õ�½��ID
		String newsTitle = request.getParameter("newsTitle"); // ������ű���
		String newsType = request.getParameter("newsType"); // ����������
		String newsContent = request.getParameter("Notepadcontent"); // ������Ń���
		String isUsed = request.getParameter("isUsed"); // ����Ƿ񷢲�
		String[] wakeType = request.getParameterValues("wakeUpMode"); // ���ѷ�ʽ
		String popedomUserId = request.getParameter("popedomUserIds"); // ���֪ͨ����
		String popedomDeptId = request.getParameter("popedomDeptIds"); // �����֪����
		String empGroupId = request.getParameter("EmpGroupId");// ���֪ͨ ְԱ����
		String isAlonePopedmon = request.getParameter("isAlonePopedmon");// �Ƿ񵥶���Ȩ
		String isSaveReading = request.getParameter("isSaveReading");// �Ƿ񱣴��Ķ��ۼ�
		String picFiles = request.getParameter("picFiles");// �Ƿ񱣴��Ķ��ۼ�
		picFiles = picFiles == null ? "" : picFiles;
		String whetherAgreeReply = request.getParameter("whetherAgreeReply");//�Ƿ�����ظ�

		// ��ɾ���ĸ���
		String delFiles = request.getParameter("delPicFiles");
		// ��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ��
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0 && picFiles.indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		// ���ѷ�ʽ
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}
		if (popedomUserId != null && !popedomUserId.contains(userId)
				&& !"0".equals(isAlonePopedmon)) {// ���Լ���Ȩ
			popedomUserId += userId + ",";
		}

		Result rs_news = new Result();
		String popedomUserIds = ""; // ֪ͨ����
		String id = IDGenerater.getId(); // �Զ�����һ��ID
		// �жϸ������Ƿ�Ϊ������������
		if ("1".equals(isUsed)) { // �����
			rs_news = mgt.ins_used_News(id, newsTitle, newsType, newsContent,
					userId, userId, isAlonePopedmon, popedomUserId,
					popedomDeptId, wakeupType, empGroupId, isSaveReading,
					picFiles,whetherAgreeReply);
			if (rs_news.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if ("0".equals(isAlonePopedmon)) { // �ж��Ƿ񵥶���Ȩ
					// ����֪ͨ����Ϊ������
					List listEmp = (List) adviceMgt.sel_allEmployee()
							.getRetVal();
					for (int i = 0; i < listEmp.size(); i++) { // ѭ������
						// ------------�Ժ����ʾ��Ϣ-------------
						popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
					}
				} else {
					if (null != popedomDeptId && !"".equals(popedomDeptId)) {
						String[] deptIds = popedomDeptId.split(","); // ���ݲ��ű�Ų��Ҳ�����Ա
						for (String departId : deptIds) {
							List<Employee> list_emp = adviceMgt
									.queryAllEmployeeByClassCode(departId);
							for (Employee emp : list_emp) {
								if (!popedomUserId.contains(emp.getid())) {// �ж��Ƿ��Ѿ�������Ȩ
									popedomUserIds += emp.getid() + ",";
								}
							}
						}
					}
					// ����ְԱ���������Ա
					if (null != empGroupId && !"".equals(empGroupId)) {
						String[] empGroupIds = empGroupId.split(","); // ���ݷ�����ҷ�����Ա
						for (String empGroup : empGroupIds) {
							List list = new OAAdviceMgt()
									.queryAllEmployeeByGroup(empGroup);
							for (int i = 0; i < list.size(); i++) {
								if (!popedomUserIds.contains(list.get(i)
										.toString())) {// �ж��Ƿ��Ѿ�������Ȩ
									popedomUserIds += list.get(i).toString()
											+ ",";
								}
							}
						}
					}
					popedomUserIds += popedomUserId;
				}
				// ������ѷ�ʽ
				String title = GlobalsTool.getMessage(request,"oa.new.newCenter")+newsTitle+"-"+this.getLoginBean(request).getEmpFullName(); // ����
				
				String url = request.getRequestURI();
				String favoriteURL = java.net.URLEncoder.encode(url
						+ "?noback=true&operation=5&newsId=" + id
						+ "&isEspecial=1");
				String content = "<a href=javascript:mdiwin('" + favoriteURL
						+ "','"
						+ GlobalsTool.getMessage(request, "oa.common.newList")
						+ "')>" + title + getMessage(request, "com.click.see")
						+ "</a>"; // ����
				if (wakeType != null && wakeType.length > 0) {
					for (String type : wakeType) {
						new Thread(new NotifyFashion(userId, title, content,
								popedomUserIds, Integer.parseInt(type), "yes",
								(String) rs_news.getRetVal())).start();
					}
				}
			}
		} else { // ��������δ��������

			rs_news = mgt.ins_notUsed_News(newsTitle, newsType, newsContent,
					userId, wakeupType, isAlonePopedmon, popedomUserId,
					popedomDeptId, empGroupId, isSaveReading, picFiles,whetherAgreeReply);
		}
		if (rs_news.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ��ӳɹ�
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/OACompanyNewsInfo.do").setAlertRequest(request);
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
	 * ������ҳ
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
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OANewsInfoSearchForm thisForm = (OANewsInfoSearchForm) form;
		// ȡ���û��ڸ�ģ���е�Ȩ��
		LoginBean loginBean = this.getLoginBean(request);
		String pageNo = request.getParameter("pageNo");
		if("1".equals(pageNo)){
			thisForm.setPageNo(1);
		}
		String userId = loginBean.getId();// �û�ID
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/OACompanyNewsInfo.do");
		request.setAttribute("add", mop.add()); // ����Ȩ��
		request.setAttribute("del", mop.delete()); // ɾȨ��
		request.setAttribute("upd", mop.update()); // ��Ȩ��
		String empGroup = empMgt.selectEmpGroupByUid(userId);// ְԱ����
		int length = 0;// �ж��Ƿ��ְԱ�ڶ���鵱��
		String temp = null;
		if (empGroup != null && empGroup != "") {
			length = empGroup.length();
			temp = "'" + empGroup + "'";
		} else {
			empGroup = null;
		}
		if (length > 29) {
			temp = "";
			String[] array = empGroup.split(",");
			for (int i = 0; i < array.length; i++) {
				if (i == array.length - 1) {
					temp += "'" + array[0] + ",'";
				} else {
					temp += "'" + array[0] + ",',";
				}
			}
		}
		// ���������Ϣ
		Result rs_newsInfo = null;
		if ("1".equals(loginBean.getId())) {
			// �������������Ϣ
			//�����޸� Ȼ���ٵ����� ���� ��ӳɹ���ת����ҳ��ʱ�� û���� ����pageNo����0 
			System.out.println(thisForm.getPageNo()+"_"+thisForm.getPageSize());
			rs_newsInfo = mgt.sel_CompanyNewsInfo(thisForm.getPageNo()<=0?1:thisForm.getPageNo(),
					thisForm.getPageSize()<=0?1:thisForm.getPageSize());
		} else {
			String classCode = adviceMgt.queryClassCodeByUserId(userId);
			rs_newsInfo = mgt.sel_CompanyNewsInfoByUserId(userId, classCode,
					empGroup, temp, thisForm.getPageNo()<=0?1:thisForm.getPageNo(), thisForm
							.getPageSize());
		}
		List ls_news = new ArrayList();
		if (rs_newsInfo.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			List ls_newsInfo = (List) rs_newsInfo.getRetVal();
			if (!"".equals(ls_newsInfo) && null != ls_newsInfo) {
				for (int i = 0; i < ls_newsInfo.size(); i++) {
					String[] arr_new = new String[9];
					String[] arr_news = (String[]) ls_newsInfo.get(i);
					// Result rs_user = mgt.sel_employee(arr_news[5]);
					// String arr_user[] = (String[]) rs_user.getRetVal();
					arr_new[0] = arr_news[0];
					arr_new[1] = arr_news[1];
					arr_new[2] = arr_news[2];
					arr_new[3] = arr_news[3];
					arr_new[4] = arr_news[4];
					arr_new[5] = arr_news[5];
					arr_new[6] = arr_news[6];
					arr_new[7] = (i + 1) + "";
					arr_new[8] = arr_news[7];
					ls_news.add(arr_new);
				}
			}
		}
		request.setAttribute("ls_newsInfo", ls_news);
		request.setAttribute("loginBean", loginBean);
		request.setAttribute("pageBar", this.pageBar(rs_newsInfo, request));
		return getForward(request, mapping, "newsIndex");
	}

	/**
	 * ��ϸ����
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
	protected ActionForward to_particularNews(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		
			String orderAttribute = (String)request.getSession().
			getAttribute("orderAttribute");
			// ������ͨ����������ѯ���������ݽ����޸ĵģ�Ӧ�������ѿͻ���ѯ��������
			String newsType1 = request.getParameter("newsType1");
			String newsTitle1 = request.getParameter("newsTitle1");
			String proUserName1 = request.getParameter("proUserName1");
			String beginTime1 = request.getParameter("beginTime1");
			String endTime1 = request.getParameter("endTime1");
			String pageNo = request.getParameter("pageNo");// ��ǰҳ��
			System.out.println("��ǰҳ�� = "+pageNo);
			if ("GET".equals(request.getMethod())) {
				newsTitle1 = newsTitle1 == null ? "" : GlobalsTool
						.toChinseChar(newsTitle1);
				proUserName1 = proUserName1 == null ? "" : GlobalsTool
						.toChinseChar(proUserName1);
			}
			if (StringUtils.isBlank(orderAttribute)) {
				orderAttribute = "createTime";
			}
			
			
			String pageSize = request.getParameter("pageSize");
			
			request.setAttribute("newsType1", newsType1);
			request.setAttribute("newsTitle1", newsTitle1);
			request.setAttribute("proUserName1", proUserName1);
			request.setAttribute("beginTime1", beginTime1);
			request.setAttribute("endTime1", endTime1);
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("pageSize", pageSize);
		
			// ���淵��
			String desktop = getParameter("desktop", request) == null ? ""
					: getParameter("desktop", request);
			// �ж��Ƿ��ǵ���ղ�������ݻ������Ķ���(ֵΪ1����)
			String isEspecial = request.getParameter("isEspecial") == null ? ""
					: request.getParameter("isEspecial");
			request.setAttribute("isEspecial", isEspecial);
			request.setAttribute("desktop", desktop);
			String newsId = request.getParameter("newsId"); // ����ID
			System.out.println("����id" + newsId);
			// ȡ���û��ڸ�ģ���е�Ȩ��
			LoginBean loginBean = this.getLoginBean(request);
			MOperation mop = (MOperation) loginBean.getOperationMap().get(
					"/OACompanyNewsInfo.do");
			request.setAttribute("del", mop.delete()); // ɾȨ��
			request.setAttribute("upd", mop.update()); // ɾȨ��
			// ����ID���������Ϣ
			Result rs_newsInfo = mgt.sel_CompanyNewsInfo_By_newsId(newsId);
			String arr_newInfo[] = (String[]) rs_newsInfo.getRetVal();
			//�鿴�Ƿ�����ظ� mj
			String whetherAgreeReply = arr_newInfo[15];
			request.setAttribute("whetherAgreeReply", whetherAgreeReply);

			request.setAttribute("isSaveReading", arr_newInfo[13]);
			Result rs_publisher = mgt.sel_employee(arr_newInfo[11]);
			String arr_publisher[] = (String[]) rs_publisher.getRetVal();
			request.setAttribute("publisher", arr_publisher[1]);
			request.setAttribute("arr_newInfo", arr_newInfo);
			String classCode = adviceMgt.queryClassCodeByUserId(loginBean.getId());
			String empGroup = empMgt.selectEmpGroupByUid(loginBean.getId());// ְԱ����
			int length = 0;// �ж��Ƿ��ְԱ�ڶ���鵱��
			String temp = null;
			if (empGroup != null && empGroup != "") {
				length = empGroup.length();
				temp = "'" + empGroup + "'";
			} else {
				empGroup = null;
			}
			if (length > 29) {
				temp = "";
				String[] array = empGroup.split(",");
				for (int i = 0; i < array.length; i++) {
					if (i == array.length - 1) {
						temp += "'" + array[0] + ",'";
					} else {
						temp += "'" + array[0] + ",',";
					}
				}
			}

			List ls_news = new ArrayList();
			if (mop.delete() || mop.update() || mop.add()) { // ���������ͨ�û�����
				// �������������Ϣ
				Result rs_newsInfos = mgt.sel_CompanyNewsInfoByUserId(loginBean
						.getId(), classCode, empGroup, temp, 0, 0);
				List ls_newsInfo = (List) rs_newsInfos.getRetVal();
				if (!"".equals(ls_newsInfo) && null != ls_newsInfo) {
					for (int i = 0; i < ls_newsInfo.size(); i++) {
						String[] arr_new = new String[9];
						String[] arr_news = (String[]) ls_newsInfo.get(i);
						Result rs_user = mgt.sel_employee(arr_news[5]);
						String arr_user[] = (String[]) rs_user.getRetVal();
						arr_new[0] = arr_news[0];
						arr_new[1] = arr_news[1];
						arr_new[2] = arr_news[2];
						arr_new[3] = arr_news[3];
						arr_new[4] = arr_news[4];
						arr_new[5] = arr_user[1];
						arr_new[6] = arr_news[6];
						arr_new[7] = (i + 1) + "";
						arr_new[8] = arr_news[7];
						ls_news.add(arr_new);
					}
				}
			} else { // ��ͨ�û�������ѷ�����������Ϣ
				// ��ѯ�ѷ���������
				Result rs_newsInfos = mgt.sel_CompanyNewsInfoByUserId(loginBean
						.getId(), classCode, empGroup, temp, 0, 0);
				List ls_newsInfo = (List) rs_newsInfos.getRetVal();
				if (!"".equals(ls_newsInfo) && null != ls_newsInfo) {
					for (int i = 0; i < ls_newsInfo.size(); i++) {
						String[] arr_new = new String[9];
						String[] arr_news = (String[]) ls_newsInfo.get(i);
						Result rs_user = mgt.sel_employee(arr_news[5]);
						String arr_user[] = (String[]) rs_user.getRetVal();
						arr_new[0] = arr_news[0];
						arr_new[1] = arr_news[1];
						arr_new[2] = arr_news[2];
						arr_new[3] = arr_news[3];
						arr_new[4] = arr_news[4];
						arr_new[5] = arr_user[1];
						arr_new[6] = arr_news[6];
						arr_new[7] = (i + 1) + "";
						arr_new[8] = arr_news[7];
						ls_news.add(arr_new);
					}
				}
			}

			// �����û�IDѭ����ѯ�û���Ϣ
			String[] accepterId = arr_newInfo[8].split(","); // �����Ų��
			List<Employee> targetUsers = new ArrayList<Employee>();
			for (String targetUser : accepterId) {
				Employee employee = empMgt.getEmployeeById(targetUser);
				if (null != employee) {
					targetUsers.add(employee);
				}
			}
			// ��ѯ֪ͨ����
			List<Department> targetDept = new ArrayList<Department>();
			if (arr_newInfo[9] != null && !"".equals(arr_newInfo[9])) {
				String[] popedomDeptIds = arr_newInfo[9].split(",");
				for (String c : popedomDeptIds) {
					Department dept = adviceMgt.getDepartmentByClassCode(c);
					if (null != dept) {
						targetDept.add(dept);
					}
				}
			}

			// ����ְԱ����
			List<String[]> listEmpGroup = new ArrayList<String[]>();
			if (arr_newInfo[12] != null && !"".equals(arr_newInfo[12])) {
				String[] popedomEmpGroupIds = arr_newInfo[12].split(",");
				for (String e : popedomEmpGroupIds) {
					Result r = empMgt.selectEmpGroupById(e);
					if (r.getRetVal() != null) {
						listEmpGroup.add((String[]) r.getRetVal());
					}
				}
			}

			request.setAttribute("targetUsers", targetUsers);
			request.setAttribute("targetDept", targetDept);
			request.setAttribute("targetEmpGroup", listEmpGroup);
			String url = request.getRequestURI();
			String favoriteURL = java.net.URLEncoder.encode(url
					+ "?noback=true&operation=5&newsId=" + newsId
					+ "&isEspecial=1");
			request.setAttribute("favoriteURL", favoriteURL);
			String uri = java.net.URLEncoder.encode(url	+ "?operation=5&newsId=" + newsId);
			request.setAttribute("uri", uri);
			request
					.setAttribute("messageTitle", this.getLoginBean(request)
							.getEmpFullName()
							+ getMessage(request, "request.read.news")
							+ arr_newInfo[2]);
			request.setAttribute("ls_news", ls_news);
			request.setAttribute("currentUser", loginBean.getId());
	
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "to_newInfo"); // ��ת����ϸ����ҳ
	}

	
	/**
	 * ��������ǰ��׼��
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
		return getForward(request, mapping, "to_addNewsInfo");
	}

	/**
	 * ��ѯ�޸�����ҳ
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
	protected ActionForward to_updNewsInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��������id��ѯ������Ϣ
		String newsId = request.getParameter("newsId"); // �������ID
		// tj+������ͨ����ѯ�����Ľ�����ݽ����޸ģ�����Ҫ�����ѯ����
		String newsType1 = request.getParameter("newsType1");
		String newsTitle1 = request.getParameter("newsTitle1");
		String proUserName1 = request.getParameter("proUserName1");
		String beginTime1 = request.getParameter("beginTime1");
		String endTime1 = request.getParameter("endTime1");
		String pageNo = request.getParameter("pageNo");// ��ǰҳ��
		if ("GET".equals(request.getMethod())) {
			newsTitle1 = GlobalsTool.toChinseChar(newsTitle1);
			proUserName1 = GlobalsTool.toChinseChar(proUserName1);
		}
		request.setAttribute("newsType1", newsType1);
		request.setAttribute("newsTitle1", newsTitle1);
		request.setAttribute("proUserName1", proUserName1);
		request.setAttribute("beginTime1", beginTime1);
		request.setAttribute("endTime1", endTime1);
		request.setAttribute("pageNo", pageNo);
		Result rs_newsInfo = mgt.sel_CompanyNewsInfo_By_newsId(newsId);
		String[] arr_newsInfo = (String[]) rs_newsInfo.getRetVal();

		// �����û�IDѭ����ѯ�û���Ϣ
		String[] accepterId = arr_newsInfo[8].split(","); // �����Ų��
		List<Employee> targetUsers = new ArrayList<Employee>();
		for (String targetUser : accepterId) {
			Employee employee = empMgt.getEmployeeById(targetUser);
			if (null != employee) {
				targetUsers.add(employee);
			}
		}
		// ��ѯ֪ͨ����
		List<Department> targetDept = new ArrayList<Department>();
		if (arr_newsInfo[9] != null && !"".equals(arr_newsInfo[9])) {
			String[] popedomDeptIds = arr_newsInfo[9].split(",");
			for (String classCode : popedomDeptIds) {
				Department dept = adviceMgt.getDepartmentByClassCode(classCode);
				if (null != dept) {
					targetDept.add(dept);
				}
			}
		}

		// ����ְԱ����
		List<String[]> listEmpGroup = new ArrayList<String[]>();
		if (arr_newsInfo[12] != null && !"".equals(arr_newsInfo[12])) {
			String[] popedomEmpGroupIds = arr_newsInfo[12].split(",");
			for (String empGroup : popedomEmpGroupIds) {
				Result r = empMgt.selectEmpGroupById(empGroup);
				if (r.getRetVal() != null) {
					listEmpGroup.add((String[]) r.getRetVal());
				}
			}
		}

		String[] wakeUpType = null;// ���ѷ�ʽ
		if (arr_newsInfo[7] != null && !"".equals(arr_newsInfo[7])) {
			wakeUpType = arr_newsInfo[7].split(",");
		}
		request.setAttribute("targetUsers", targetUsers);
		request.setAttribute("targetDept", targetDept);
		request.setAttribute("targetEmpGroup", listEmpGroup);
		request.setAttribute("arr_newsInfo", arr_newsInfo);
		request.setAttribute("wakeUpType", wakeUpType); // ��ʾ��ʽ
		return getForward(request, mapping, "to_updNewsInfo");
	}

	/**
	 * ɾ������
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
	protected ActionForward delNewsInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��ѯ����
		String newsTitle = request.getParameter("newsTitle") == null ? ""
				: request.getParameter("newsTitle"); // ������ű���
		String newsType = request.getParameter("newsType") == null ? ""
				: request.getParameter("newsType"); // ����������
		String beginTime = request.getParameter("beginTime") == null ? ""
				: request.getParameter("beginTime"); // ����ʱ��ǰ
		String endTime = request.getParameter("endTime") == null ? "" : request
				.getParameter("endTime"); // ����ʱ���
		String proUserId = request.getParameter("proUserId") == null ? ""
				: request.getParameter("proUserId");// ������id
		String proUserName = request.getParameter("proUserName") == null ? ""
				: request.getParameter("proUserName");// ������id
		String pageNo = request.getParameter("pageNo") == null ? "" : request
				.getParameter("pageNo");// ��ǰҳ��
		if ("GET".equals(request.getMethod())) {
			newsTitle = GlobalsTool.toChinseChar(newsTitle);
			proUserName = GlobalsTool.toChinseChar(proUserName);
		}
		request.setAttribute("newsTitle", newsTitle);
		request.setAttribute("newsType", newsType);
		request.setAttribute("beginTime", beginTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("proUserId", proUserId);
		request.setAttribute("proUserName", proUserName);
		request.setAttribute("pageNo", pageNo);
		// ���ҳ�����-->��Ҫɾ����ID
		String newsInfoId[] = request.getParameterValues("keyId");
		Result rs_newsInfo = null;
		for (int i = 0; i < newsInfoId.length; i++) {
			// ���Ҫɾ�������ŵ���Ϣ���Ա�Ӽ�ʱ��Ϣ��MessageBean����ɾ����¼
			Result r = mgt.sel_CompanyNewsInfo_By_newsId(newsInfoId[i]);
			// tangjing+ɾ������
			rs_newsInfo = mgt.del_newsInfo(newsInfoId[i]);

			if (r.getRealTotal() >= 1) {
				// �Ӳ�ѯ��Ϣ�л�ȡ��������Ϣ
				String[] arr_newsInfo = (String[]) r.getRetVal();
				// �ڼ�ʱ��Ϣ�в�ѯ�йش������ŵķ��ͳ�ȥ����Ϣ
				// Result result1=mmgt.queryByCondition(arr_newsInfo[5],"send",
				// arr_newsInfo[2],arr_newsInfo[2]);
				// List list=(List)result1.getRetVal();
				// String ids[]=new String[10];//�漴ʱ��ϢId
				// ��������йؼ�ʱ��Ϣ
				// if(list.size()>0){
				// for(int p=0;p<list.size();p++)
				// {
				// //ѭ����ȡ��ʱ��Ϣ������id��������
				// MessageBean messageBean=(MessageBean)list.get(p);
				// mmgt.del_message(messageBean.getId());
				// }
				// }
				// ����ɾ����ʱ��Ϣ��¼
				// Result rk= mmgt.delete(ids);
				// System.out.println(rk.getRealTotal());
			}
			// �Ӽ�ʱ��Ϣ��ɾ��������¼
		}
		if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ɾ���ɹ�
			EchoMessage.success().add(
					getMessage(request, "common.msg.delSuccess")).setBackUrl(
					"/OACompanyNewsInfo.do?operation=4&newsType=" + newsType
							+ "&newsTitle=" + GlobalsTool.encode(newsTitle)
							+ "&proUserId=" + proUserId + "&proUserName="
							+ GlobalsTool.encode(proUserName) + "&beginTime="
							+ beginTime + "&endTime=" + endTime + "&pageNo="
							+ pageNo).setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// ���ʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}
	
	
	/**
	 * ɾ�����Żظ�mj
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
	protected ActionForward delReplyNewsInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,String replyId)
			throws Exception {
		
		Result result = null;
		
		String newsId = request.getParameter("newsId");
		
		result = mgt.delReplyNewsInfoById(replyId);
		
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(request, "com.revert.to.succeed"))
                     .setBackUrl("/OACompanyNewsInfo.do?operation=5&newsId="+newsId)
                     .setAlertRequest(request);
		}else{
			EchoMessage.success().add(getMessage(request, "com.revert.to.failure"))
            		.setBackUrl("/OACompanyNewsInfo.do.do").setAlertRequest(request);
			
		}
		return getForward(request, mapping, "message") ;
		
	}
	


	
	/**
	 * �޸�����
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
	protected ActionForward upd_newsInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String userId = this.getLoginBean(request).getId(); // ��õ�½��ID
		String newsId = request.getParameter("newsId"); // �������ID
		String newsTitle = request.getParameter("newsTitle"); // ������ű���
		String newsType = request.getParameter("newsType"); // ����������
		String newsContent = request.getParameter("Notepadcontent"); // ������Ń���
		String isUsed = request.getParameter("isUsed"); // ����Ƿ񷢲�
		String[] wakeupType = request.getParameterValues("wakeUpMode");// ���ѷ�ʽ
		String popedomUserId = request.getParameter("popedomUserIds"); // ���֪ͨ����
		String popedomDeptId = request.getParameter("popedomDeptIds"); // �����֪����
		String empGroupId = request.getParameter("EmpGroupId");// ���֪ͨ ְԱ����
		String isAlonePopedmon = request.getParameter("isAlonePopedmon");// �Ƿ񵥶���Ȩ
		String isSaveReading = request.getParameter("isSaveReading");
		String picFiles = request.getParameter("picFiles");
		picFiles = picFiles == null ? "" : picFiles;
		String whetherAgreeReply = request.getParameter("whetherAgreeReply");//�Ƿ�����ظ�

		// ��ɾ���ĸ���
		String delFiles = request.getParameter("delPicFiles");
		// ��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ��
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0 && picFiles.indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		String pageNo = request.getParameter("pageNo");
		if (popedomUserId != null && !"".equals(popedomUserId)
				&& !popedomUserId.contains(userId)) {// ���Լ���Ȩ
			popedomUserId += userId + ",";
		}
		// ������ͨ����������ѯ���������ݽ����޸ĵģ�Ӧ�������ѿͻ���ѯ��������
		String newsType1 = request.getParameter("newsType1");
		String newsTitle1 = request.getParameter("newsTitle1");
		String proUserName1 = request.getParameter("proUserName1");
		String beginTime1 = request.getParameter("beginTime1");
		String endTime1 = request.getParameter("endTime1");

		// ���ѷ�ʽ
		String wakeTypes = "";
		if (wakeupType != null && wakeupType.length > 0) {
			for (String str : wakeupType) {
				wakeTypes += str + ",";
			}
		}
		// ������ǵ�����Ȩ�����popedomUserId,popedomDeptId��ա�
		if (isAlonePopedmon == "0") {
			popedomUserId = "";
			popedomDeptId = "";
		}
		//mj ֪ͨͨ�� ��� �����޸�Ϊδ���� ֪ͨͨ������ȴ���Կ���
		Result rs = mgt.getNewsById(newsId);
		String[] isUseds = (String[])rs.getRetVal();
		String used = isUseds[0];
		if (!isUsed.equals(used))  {//
			//�޸� �����Ŷ�Ӧ��֪ͨͨ�� �Ƿ���� ���ڸı�
			AdviceMgt avMgt = new AdviceMgt();
			rs = avMgt.getBeanByReId(newsId);
			List<AdviceBean> list = (List<AdviceBean>)rs.getRetVal();
			int i = list.size();
			if ( i > 0) {
				AdviceBean bean =  list.get(0);
				if (bean != null){
					if (isUsed.equals("1")) {
						bean.setExist("all");
					} else {
						bean.setExist("noPub");//û���������� �Ժ󷢲�
					}
					String isExist = bean.getExist();
					rs = avMgt.updateIsUsedById(bean.getId(),isExist);
				}
			}
			
			
		}
		// �޸�
		Result rs_newsInfo = mgt.upd_NewsInfo(newsId, newsTitle, newsType,
				newsContent, isUsed, userId, userId, wakeTypes,
				isAlonePopedmon, popedomUserId, popedomDeptId, empGroupId,
				isSaveReading, picFiles,whetherAgreeReply);

		// ������ѷ�ʽ
		String title =OnlineUserInfo.getUser(userId).getName()+GlobalsTool.getMessage(request, "oa.news.newsUpdate") + ":" + newsTitle; // ����

		String url = request.getRequestURI();
		String favoriteURL = java.net.URLEncoder
				.encode(url + "?noback=true&operation=5&newsId=" + newsId + "&isEspecial=1");

		// �޸ķ���֪ͨ���������
		newsContent = "<a href=javascript:mdiwin('" + favoriteURL + "','"
				+ GlobalsTool.getMessage(request, "oa.common.newList") + "')>"
				+ title + getMessage(request, "com.click.see") + "</a>"; // ����

		if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// �޸ĳɹ�

			if ("1".equals(isUsed)) { // ����Ƿ�����
				// ֪ͨ����
				String popedomUserIds = "";
				if (!"".equals(newsId) && null != newsId) {
					// ��֪ͨ���͸�ÿһ��֪ͨ����
					if ("0".equals(isAlonePopedmon)) { // �ж��Ƿ񵥶���Ȩ
						List listEmp = (List) adviceMgt.sel_allEmployee()
								.getRetVal();
						for (int i = 0; i < listEmp.size(); i++) { // ѭ������
							popedomUserIds += String.valueOf(listEmp.get(i))
									+ ",";
						}
					} else {
						if (null != popedomDeptId && !"".equals(popedomDeptId)) {
							String[] deptIds = popedomDeptId.split(","); // ���ݲ��ű�Ų��Ҳ�����Ա
							for (String departId : deptIds) {
								List<Employee> list_emp = adviceMgt
										.queryAllEmployeeByClassCode(departId);
								for (Employee emp : list_emp) {
									if (!popedomUserId.contains(emp.getid())) {// �ж��Ƿ��Ѿ�������Ȩ
										popedomUserIds += emp.getid() + ",";
									}
								}
							}
						}
						if (!"".equals(popedomUserId) && null != popedomUserId) { // ������ܶ���Ϊ��
							popedomUserIds += popedomUserId;
						}
					}
					if (null != wakeupType && wakeupType.length > 0
							&& !"".equals(popedomUserIds)) {
						for (String wakeUp : wakeupType) {
							new NotifyFashion(userId, newsTitle, newsContent,
									popedomUserIds, Integer.parseInt(wakeUp),
									"yes", newsId).start();
						}
					}
				}
			}
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl(
							"/OACompanyNewsInfo.do?operation=4&newsType="
									+ newsType1 + "&newsTitle="
									+ GlobalsTool.encode(newsTitle1)
									+ "&proUserName="
									+ GlobalsTool.encode(proUserName1)
									+ "&beginTime=" + beginTime1 + "&endTime="
									+ endTime1 + "&pageNo=" + pageNo)
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
	 * �ظ����� mj
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward replyNewsInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
//		OABBSUserBean bbsUser = (OABBSUserBean) request.getSession().getAttribute("BBSUser");
//		OABBSUserBean bUser= new OABBSUserBean();
		OANewsInfoSearchForm replyForm = (OANewsInfoSearchForm) form ;
		OANewsInfoReplyBean replyBean = new OANewsInfoReplyBean();
		String replyType = request.getParameter("replyType");
		replyBean.setId(IDGenerater.getId());
		//bUser.setId(bbsUser.getId());
		String content = replyForm.getContent();
		if(StringUtils.isBlank(content)){
			content = request.getParameter("content");
		}
		if(!"advice".equals(replyType)&&!"photoInfoDetail".equals(replyType)&&!"noencode".equals(replyType)) {
			content = GlobalsTool.toChinseChar(content) ;
		}
		
		replyBean.setContent(content);
		replyBean.setNewsId(replyForm.getNewsId());
		//replyBean.setBbsUser(bUser);
		replyBean.setFullName(getLoginBean(request).getEmpFullName());
		replyBean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		replyBean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss ));
		/*replyBean.setCreateBy(getLoginBean(request).getId());*/
		Result result = mgt.replyNewsInfo(replyBean);
		
		
		request.getSession().setAttribute("orderAttribute","createTime");
		request.getSession().setAttribute("orderType","desc");
		String pageSize =  request.getParameter("pageSize");
		request.setAttribute("pageSize",pageSize);
		String pageNo = request.getParameter("pageNo");
		

		
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			if ("advice".equals(replyType)){//��ת��֪ͨ��ϸҳ��
				EchoMessage.success().add(getMessage(request, "com.reply.to.succeed"))
                .setBackUrl("/OAAdvice.do?operation=5&adviceId="+replyForm.getNewsId()+"&pageSize="+pageSize)
                .setAlertRequest(request);
			} else if ("replyInfo".equals(replyType)){//��ת���ظ�ҳ��
			
				to_particularNews(mapping, form, request, response);
			} else if ("photoInfoDetail".equals(replyType)) {
				String albumName = request.getParameter("albumName");
				String albumId = request.getParameter("albumSelectId");
				EchoMessage.success().add(getMessage(request, "oa.clickReply.to.succeed"))
                .setBackUrl("/PhotoAction.do?operation=5&albumName="+GlobalsTool.encode(albumName)+"&pId="+replyForm.getNewsId()+"&albumId="+albumId+"&replyType="+replyType)
                .setAlertRequest(request);
			}else {
				if("noencode".equals(replyType)){
					request.setAttribute("noback",false);
					EchoMessage.success().add(getMessage(request, "com.reply.to.succeed"))
	                .setBackUrl("/OACompanyNewsInfo.do?operation=5&newsId="+replyForm.getNewsId()+"&pageSize="+pageSize+"&pageNo="+pageNo)
	                .setAlertRequest(request);	
				} else {
					System.out.println(replyForm.getNewsId());
					EchoMessage.success().add(getMessage(request, "com.reply.to.succeed"))
	                .setBackUrl("/OACompanyNewsInfo.do?operation=5&newsId="+replyForm.getNewsId())
	                .setAlertRequest(request);
				}
			}
		}else{
			if ("advice".equals(replyType)) {// ��ת��֪ͨ��ϸҳ��
				EchoMessage.success().add(
						getMessage(request, "com.reply.to.failure"))
						.setBackUrl("/OAAdvice.do").setAlertRequest(request);
			} else if ("replyInfo".equals(replyType)) {// ��ת���ظ�ҳ��
				EchoMessage.success().add(
						getMessage(request, "com.reply.to.failure"))
						.setBackUrl(
								"/OACompanyNewsInfo.do")
						.setAlertRequest(request);
			}  else if ("photoInfoDetail".equals(replyType)) {
				String albumName = request.getParameter("albumName");
				request.setAttribute("showPhotosOfAlbum",request.getParameter("showPhotosOfAlbum"));
				EchoMessage.success().add(getMessage(request, "com.reply.to.failure"))
                .setBackUrl("/PhotoAction.do?operation=4&albumName="+GlobalsTool.encode(albumName)+"&pId="+replyForm.getNewsId())
                .setAlertRequest(request);
			} else {
				EchoMessage.success().add(
						getMessage(request, "com.reply.to.failure"))
						.setBackUrl("/OACompanyNewsInfo.do").setAlertRequest(
								request);
			}
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * ��java����ת���ɵ�json����,
	 * @param obj
	 * @param response
	 * @throws IOException 
	 */
	private void writeJson(Object obj,HttpServletResponse response) 
		throws Exception{
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(obj) ;
		out.println(json) ;
		System.out.println(json);
		out.flush();
		out.close();
	}
	/**
	 * ��ȡ���лظ���Ϣ��������ģ����ţ�֪ͨ��mj
	 * 
	 */
	protected ActionForward showAllReplys(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String pageNo = request.getParameter("pageNo");// ��ǰҳ��
			System.out.println("��ǰҳ�� = "+pageNo);
			request.setAttribute("pageNo", pageNo);
			String newsId = request.getParameter("newsId"); // ����ID
			System.out.println("����id" + newsId);
			request.setAttribute("newsId", newsId);
			int pageNo2 = getParameterInt("pageNo", request) ;
			System.out.println("��ǰҳ��2 = "+pageNo2);
			/*��ѯ�ظ�����*/
			
			int pageSize = getParameterInt("pageSize", request) ;
			if(pageSize==0){
				pageSize = GlobalsTool.getPageSize() ;
			}
		
			
			//�Ƿ�ֻ�鿴���û���
			String auth = request.getParameter("auth");
			String orderAttribute = (String)request.getSession().getAttribute("orderAttribute");
			String orderType = (String)request.getSession().getAttribute("orderType");
			
			if (StringUtils.isBlank(orderAttribute)) {
				orderAttribute = "createTime";
			}
			
			if (StringUtils.isBlank(orderType)) {
				orderType = "desc";
			}
			Result result = mgt.queryReplyNewsInfo(newsId,auth,orderAttribute,orderType,pageNo2<=0?1:pageNo2,pageSize) ;
			//mj end
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("replayList", result.retVal) ;
				if(result.realTotal >0){
					request.setAttribute("pageBar", pageBar(result, request)) ;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "toReplyInfo");//��ת����ϸ�����б�ҳ��
	}

	/**
	 * �ظ���һ�»��߲�ѯ���� ���� ���ȵĻظ����� mj
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
	protected ActionForward addAgreeNumOfReply(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String orderAttribute = request.getParameter("orderAttribute");
			if (StringUtils.isBlank(orderAttribute)) {
				orderAttribute = "createTime";
			}
			String replyId = request.getParameter("replyId"); // ���Żظ�ID
			String newsId = request.getParameter("newsId");
			Result	rs = null;	
			if (StringUtils.isNotBlank(replyId)){//��һ��
				rs = mgt.getReplyById(replyId);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					OANewsInfoReplyBean reply = (OANewsInfoReplyBean)rs.getRetVal();
					reply.setAgreeNum(reply.getAgreeNum()+1);
					rs = mgt.updateReply(reply);
				}else{	//ʧ��
					EchoMessage.error().add(
							getMessage(request, "com.revert.to.failure"))
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}
			
			int pageNo2 = getParameterInt("pageNo", request) ;
			System.out.println("��ǰҳ��2 = "+pageNo2);
			int pageSize = getParameterInt("pageSize", request) ;
			if(pageSize==0){
				pageSize = GlobalsTool.getPageSize() ;
			}
			//�Ƿ�ֻ�鿴���û���
			String auth = request.getParameter("auth");
			String orderType = request.getParameter("orderType");
			if (StringUtils.isBlank(orderType)) {
				orderType = "desc";
			}
			//�鿴���еĻظ�����
			rs = mgt.queryReplyNewsInfo(newsId,auth,orderAttribute,orderType,pageNo2<=0?1:pageNo2,pageSize<=0?100:pageSize) ;
			//���������Ƿ��õ�ajax���ж� �Ƿ񷵻�json(֪ͨͨ�����ajax�������õ�iframe��
			String requestType = request.getParameter("requestType");
			List<OANewsInfoReplyBean> list = new ArrayList<OANewsInfoReplyBean>();
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				list = (List<OANewsInfoReplyBean>)rs.retVal ;	
			}
			//����Щֵ�������� ������ ͨ��form�� hidden���ݸ� action���ּ��书�� ���� �ڲ���session������Ϣ ÿ�λ�ȡ ��ͬ���� ���� ��Ӧ�� ����һҳ�޼��书��
			request.getSession().setAttribute("orderAttribute", orderAttribute);
			request.getSession().setAttribute("orderType", orderType);
			if(StringUtils.equals("noAjax",requestType)) {
				//ajax newsInfo(replyInfo)
				/*
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					// ����ID���������Ϣ
					Result rs_newsInfo = mgt.sel_CompanyNewsInfo_By_newsId(newsId);
					if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						String arr_newInfo[] = (String[]) rs_newsInfo.getRetVal();
						request.setAttribute("arr_newInfo", arr_newInfo);
						//�鿴�Ƿ�����ظ� mj
						String whetherAgreeReply = arr_newInfo[15];
						request.setAttribute("whetherAgreeReply", whetherAgreeReply);
						request.setAttribute("arr_newInfo", arr_newInfo);
					}
					request.setAttribute("replayList", rs.retVal) ;
					if(rs.realTotal >0){
						request.setAttribute("pageBar", pageBar(rs, request)) ;
					}
				}
				return getForward(request, mapping, "toReplyInfo");//��ת����ϸ�����б�ҳ��
				*/
				return showAllReplys(mapping, form, request, response);
			} else {
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					writeJson(list, response) ;	
				}
				Gson json = OANewsInfoAction.gson;
				
				request.setAttribute("msg", json);
				return getForward(request, mapping, "blank");
				
			}
		
	
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	
	}
	
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {

		LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("OANewInfoAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
        //����Ψһ�û���֤��������ظ���½�ģ��������û��߳�ǰ�����û�
        if (!OnlineUserInfo.checkUser(req)) {
            //���߳�
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }        
        int operation = getOperation(req);
        if(operation == OperationConst.OP_SEND||operation == OperationConst.OP_URL_TO||operation == OperationConst.OP_READ_OVER){
        	return null;
        }
		return super.doAuth(req, mapping) ;
	}
	
	
//	@Override
//	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
//		LoginBean loginBean = getLoginBean(req) ;
//        if (loginBean == null) {
//            BaseEnv.log.debug("OANewInfoAction.doAuth() ---------- loginBean is null");
//            return getForward(req, mapping, "indexPage");
//        }
//        //����Ψһ�û���֤��������ظ���½�ģ��������û��߳�ǰ�����û�
//        if (!OnlineUserInfo.checkUser(req)) {
//            //���߳�
//            EchoMessage.error().setAlertRequest(req);
//            return getForward(req, mapping, "doubleOnline");
//        }        
//        OABBSUserBean userBean = (OABBSUserBean)req.getSession().getAttribute("BBSUser") ;
//        if(userBean == null || !loginBean.getId().equals(userBean.getUserID())){
//        	//û���û���Ϣ�������û���Ϣ�͵�½�û���һ�¡�
//			/*������̳ �û���Ϣ,ÿ�θ����û���Ϣ����Ҫ���½�����̳*/
//			Result result = bbsMgt.loadBBSUser(loginBean.getId()) ;
//			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.totalPage>0){
//				userBean = ((List<OABBSUserBean>)result.retVal).get(0) ;
//				req.getSession().setAttribute("BBSUser", userBean) ;
//			}
//			/*����û���ע�� ��ע��*/
//			if(userBean==null){
//				userBean = new OABBSUserBean() ;
//				userBean.setId(IDGenerater.getId()) ;
//				userBean.setNickName(loginBean.getEmpFullName());
//				userBean.setFullName(loginBean.getEmpFullName());
//				userBean.setUserID(loginBean.getId()) ;
//				userBean.setCreateBy(loginBean.getId()) ;
//				result = bbsMgt.addUser(userBean) ;
//				req.getSession().setAttribute("BBSUser", userBean) ;
//			}
//        }
//		return null;
//	}
}
