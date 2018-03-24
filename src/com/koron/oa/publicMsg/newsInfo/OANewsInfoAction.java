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
 * Title: 新闻控制类
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
		// 新增新闻前的准备
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			String type_upd = request.getParameter("type_upd");
			if (!"".equals(type_upd) && type_upd != null) {
				if (type_upd.equals("upd_newsInfo")) { // 修改新闻
					forward = upd_newsInfo(mapping, form, request, response);
				} else if (type_upd.equals("upd_addNewsInfo")) { // 将未发布的新闻发布
					forward = upd_addNewsInfo(mapping, form, request, response);
				} else if("agreeReply".equals(type_upd)) {
					//顶 最新 最早
					forward = addAgreeNumOfReply(mapping, form, request, response);//
				}
				break;
			}

			// //查询
		case OperationConst.OP_QUERY:
			String type_query = request.getParameter("type_query");
			String type_update = request.getParameter("type_upd");
			
			if (("".equals(type_query) || type_query == null)&&type_update == null) { // 按条件查询
				forward = news_condition(mapping, form, request, response);
			} else if ("agreeReply".equals(type_query)&&"agreeReply".equals(type_update)) {
				// 查询
				forward = addAgreeNumOfReply(mapping, form, request, response);
				break;
			} else {
				if ("to_updNewsInfo".equals(type_query)) { // 查询修改新闻
					forward = to_updNewsInfo(mapping, form, request, response);
				} else {
					forward = showAllReplys(mapping, form, request, response);
				}
			}
			
			break;
		// 新闻详细
		case OperationConst.OP_DETAIL:
			forward = to_particularNews(mapping, form, request, response);
			break;

		// 删除
		case OperationConst.OP_DELETE:
			if (StringUtils.isNotBlank(replyId)) {//mj
				forward = delReplyNewsInfo(mapping, form, request, response,replyId);
			} else {
				forward = delNewsInfo(mapping, form, request, response);
			}	
			break;
		//回复
		case OperationConst.OP_SEND:
			forward =  replyNewsInfo(mapping, form, request, response);
			break;
		case OperationConst.OP_URL_TO:
			forward = showAllReplys(mapping,form,request,response);
			break;
		case OperationConst.OP_READ_OVER:
			forward = addAgreeNumOfReply(mapping, form, request, response);//OAadviceAction会调用 使用ajax
			break;
		default:
			forward = query(mapping, form, request, response); // 新闻首页
		}
		return forward;
	}

	/**
	 * 按条件查询
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

		// 取出用户在该模块中的权限
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/OACompanyNewsInfo.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删权限
		request.setAttribute("upd", mop.update()); // 改权限

		String newsTitle = request.getParameter("newsTitle"); // 获得新闻标题
		String newsType = request.getParameter("newsType"); // 获得新闻类别
		String beginTime = request.getParameter("beginTime"); // 条件时间前
		String endTime = request.getParameter("endTime"); // 条件时间后
		String proUserId = request.getParameter("proUserId");// 发布人id
		String proUserName = request.getParameter("proUserName");// 发布人
		// 中文参数转码
		if ("GET".equals(request.getMethod())) {
			newsTitle = newsTitle == null ? "" : GlobalsTool
					.toChinseChar(newsTitle);
			proUserName = proUserName == null ? "" : GlobalsTool
					.toChinseChar(proUserName);
		}
		// 将条件保存在request中，返回给用户
		request.setAttribute("newsTitle", newsTitle);
		request.setAttribute("newsType", newsType);
		request.setAttribute("beginTime", beginTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("proUserId", proUserId);
		request.setAttribute("proUserName", proUserName);

		String userId = getLoginBean(request).getId();
		String empGroup = empMgt.selectEmpGroupByUid(userId);// 职员分组
		int length = 0;// 判断是否该职员在多个组当中
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
		// 分页
		request.setAttribute("ls_newsInfo", ls_news);
		request.setAttribute("loginBean", loginBean);
		// request.setAttribute("ls_newsInfo", ls_news);
		return getForward(request, mapping, "newsIndex");
	}

	/**
	 * 将未发布的新闻发布
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
		// 假如是通过按条件查询出来的数据进行修改的，应需求必须把客户查询条件保存
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
		String userId = this.getLoginBean(request).getId(); // 获得登陆者ID
		String newsId = request.getParameter("newsId"); // 获得新闻ID
		//mj 通知通告 解决 新闻修改为未发布 通知通告里面却可以看见
		//修改 该新闻对应的通知通告 是否存在 存在改变
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

			// 添加提醒方式
			Result rs_new = mgt.sel_CompanyNewsInfo_By_newsId(newsId); // 根据新闻Id查询新闻相关信息
			String[] str_news = (String[]) rs_new.getRetVal();
			String isAlonePopedmon = str_news[10];
			String popedomDeptId = str_news[9];
			String popedomUserId = str_news[8];

			String popedomUserIds = ""; // 通知对象
			if ("1".equals(isAlonePopedmon)) { // 不公开发布
				// 将通知发送给每一个通知对象
				if (null != popedomDeptId && !"".equals(popedomDeptId)) {
					String[] deptIds = popedomDeptId.split(","); // 根据部门编号查找部门人员
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
				for (int i = 0; i < listEmp.size(); i++) { // 循环发送
					popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
				}
			}

			String title = GlobalsTool.getMessage(request, "oa.new.newCenter")
					+ "(" + str_news[2] + ")"; // 标题

			String url = request.getRequestURI();
			String favoriteURL = java.net.URLEncoder.encode(url
					+ "?noback=true&operation=5&newsId=" + newsId
					+ "&isEspecial=1");
			String content = "<a href=javascript:mdiwin('" + favoriteURL
					+ "','"
					+ GlobalsTool.getMessage(request, "oa.common.newList")
					+ "')>" + title + getMessage(request, "com.click.see")
					+ "</a>"; // 内容

			String[] wakeType = null; // 提醒方式
			if (str_news[7] != null && !"".equals(str_news[7])) {
				wakeType = str_news[7].split(",");
			}
			if (wakeType != null && wakeType.length > 0) {
				for (String type : wakeType) {
					new Thread(new NotifyFashion(userId, title, content,
							popedomUserIds, Integer.parseInt(type), "yes",
							newsId)).start(); // 启动能知线程
				}
			}
			// 发布成功
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
			// 发布失败
			EchoMessage.error().add(
					getMessage(request, "oa.publicMsg.newsInfo.ReleaseFaile"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	/**
	 * 新增新闻
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
		String userId = this.getLoginBean(request).getId(); // 获得登陆者ID
		String newsTitle = request.getParameter("newsTitle"); // 获得新闻标题
		String newsType = request.getParameter("newsType"); // 获得新闻类别
		String newsContent = request.getParameter("Notepadcontent"); // 获得新闻內容
		String isUsed = request.getParameter("isUsed"); // 获得是否发布
		String[] wakeType = request.getParameterValues("wakeUpMode"); // 提醒方式
		String popedomUserId = request.getParameter("popedomUserIds"); // 获得通知对象
		String popedomDeptId = request.getParameter("popedomDeptIds"); // 获得能知部门
		String empGroupId = request.getParameter("EmpGroupId");// 获得通知 职员分组
		String isAlonePopedmon = request.getParameter("isAlonePopedmon");// 是否单独授权
		String isSaveReading = request.getParameter("isSaveReading");// 是否保存阅读痕迹
		String picFiles = request.getParameter("picFiles");// 是否保存阅读痕迹
		picFiles = picFiles == null ? "" : picFiles;
		String whetherAgreeReply = request.getParameter("whetherAgreeReply");//是否允许回复

		// 需删除的附件
		String delFiles = request.getParameter("delPicFiles");
		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0 && picFiles.indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		// 提醒方式
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}
		if (popedomUserId != null && !popedomUserId.contains(userId)
				&& !"0".equals(isAlonePopedmon)) {// 给自己授权
			popedomUserId += userId + ",";
		}

		Result rs_news = new Result();
		String popedomUserIds = ""; // 通知对象
		String id = IDGenerater.getId(); // 自动生成一个ID
		// 判断该新闻是否为立即发布新闻
		if ("1".equals(isUsed)) { // 如果是
			rs_news = mgt.ins_used_News(id, newsTitle, newsType, newsContent,
					userId, userId, isAlonePopedmon, popedomUserId,
					popedomDeptId, wakeupType, empGroupId, isSaveReading,
					picFiles,whetherAgreeReply);
			if (rs_news.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if ("0".equals(isAlonePopedmon)) { // 判断是否单独授权
					// 设置通知对象为所有人
					List listEmp = (List) adviceMgt.sel_allEmployee()
							.getRetVal();
					for (int i = 0; i < listEmp.size(); i++) { // 循环发送
						// ------------以后加提示信息-------------
						popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
					}
				} else {
					if (null != popedomDeptId && !"".equals(popedomDeptId)) {
						String[] deptIds = popedomDeptId.split(","); // 根据部门编号查找部门人员
						for (String departId : deptIds) {
							List<Employee> list_emp = adviceMgt
									.queryAllEmployeeByClassCode(departId);
							for (Employee emp : list_emp) {
								if (!popedomUserId.contains(emp.getid())) {// 判断是否已经单独授权
									popedomUserIds += emp.getid() + ",";
								}
							}
						}
					}
					// 根据职员分组查找人员
					if (null != empGroupId && !"".equals(empGroupId)) {
						String[] empGroupIds = empGroupId.split(","); // 根据分组查找分组人员
						for (String empGroup : empGroupIds) {
							List list = new OAAdviceMgt()
									.queryAllEmployeeByGroup(empGroup);
							for (int i = 0; i < list.size(); i++) {
								if (!popedomUserIds.contains(list.get(i)
										.toString())) {// 判断是否已经单独授权
									popedomUserIds += list.get(i).toString()
											+ ",";
								}
							}
						}
					}
					popedomUserIds += popedomUserId;
				}
				// 添加提醒方式
				String title = GlobalsTool.getMessage(request,"oa.new.newCenter")+newsTitle+"-"+this.getLoginBean(request).getEmpFullName(); // 标题
				
				String url = request.getRequestURI();
				String favoriteURL = java.net.URLEncoder.encode(url
						+ "?noback=true&operation=5&newsId=" + id
						+ "&isEspecial=1");
				String content = "<a href=javascript:mdiwin('" + favoriteURL
						+ "','"
						+ GlobalsTool.getMessage(request, "oa.common.newList")
						+ "')>" + title + getMessage(request, "com.click.see")
						+ "</a>"; // 内容
				if (wakeType != null && wakeType.length > 0) {
					for (String type : wakeType) {
						new Thread(new NotifyFashion(userId, title, content,
								popedomUserIds, Integer.parseInt(type), "yes",
								(String) rs_news.getRetVal())).start();
					}
				}
			}
		} else { // 否则新增未发送新闻

			rs_news = mgt.ins_notUsed_News(newsTitle, newsType, newsContent,
					userId, wakeupType, isAlonePopedmon, popedomUserId,
					popedomDeptId, empGroupId, isSaveReading, picFiles,whetherAgreeReply);
		}
		if (rs_news.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 添加成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/OACompanyNewsInfo.do").setAlertRequest(request);
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
	 * 新闻首页
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
		// 取出用户在该模块中的权限
		LoginBean loginBean = this.getLoginBean(request);
		String pageNo = request.getParameter("pageNo");
		if("1".equals(pageNo)){
			thisForm.setPageNo(1);
		}
		String userId = loginBean.getId();// 用户ID
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/OACompanyNewsInfo.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删权限
		request.setAttribute("upd", mop.update()); // 改权限
		String empGroup = empMgt.selectEmpGroupByUid(userId);// 职员分组
		int length = 0;// 判断是否该职员在多个组当中
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
		// 获得新闻信息
		Result rs_newsInfo = null;
		if ("1".equals(loginBean.getId())) {
			// 获得所有新闻信息
			//当先修改 然后再点击添加 发现 添加成功跳转到首页的时候 没数据 发现pageNo等于0 
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
	 * 详细新闻
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
			// 假如是通过按条件查询出来的数据进行修改的，应需求必须把客户查询条件保存
			String newsType1 = request.getParameter("newsType1");
			String newsTitle1 = request.getParameter("newsTitle1");
			String proUserName1 = request.getParameter("proUserName1");
			String beginTime1 = request.getParameter("beginTime1");
			String endTime1 = request.getParameter("endTime1");
			String pageNo = request.getParameter("pageNo");// 当前页数
			System.out.println("当前页数 = "+pageNo);
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
		
			// 桌面返回
			String desktop = getParameter("desktop", request) == null ? ""
					: getParameter("desktop", request);
			// 判断是否是点击收藏里的内容或邀请阅读等(值为1则是)
			String isEspecial = request.getParameter("isEspecial") == null ? ""
					: request.getParameter("isEspecial");
			request.setAttribute("isEspecial", isEspecial);
			request.setAttribute("desktop", desktop);
			String newsId = request.getParameter("newsId"); // 新闻ID
			System.out.println("新闻id" + newsId);
			// 取出用户在该模块中的权限
			LoginBean loginBean = this.getLoginBean(request);
			MOperation mop = (MOperation) loginBean.getOperationMap().get(
					"/OACompanyNewsInfo.do");
			request.setAttribute("del", mop.delete()); // 删权限
			request.setAttribute("upd", mop.update()); // 删权限
			// 根据ID获得新闻信息
			Result rs_newsInfo = mgt.sel_CompanyNewsInfo_By_newsId(newsId);
			String arr_newInfo[] = (String[]) rs_newsInfo.getRetVal();
			//查看是否允许回复 mj
			String whetherAgreeReply = arr_newInfo[15];
			request.setAttribute("whetherAgreeReply", whetherAgreeReply);

			request.setAttribute("isSaveReading", arr_newInfo[13]);
			Result rs_publisher = mgt.sel_employee(arr_newInfo[11]);
			String arr_publisher[] = (String[]) rs_publisher.getRetVal();
			request.setAttribute("publisher", arr_publisher[1]);
			request.setAttribute("arr_newInfo", arr_newInfo);
			String classCode = adviceMgt.queryClassCodeByUserId(loginBean.getId());
			String empGroup = empMgt.selectEmpGroupByUid(loginBean.getId());// 职员分组
			int length = 0;// 判断是否该职员在多个组当中
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
			if (mop.delete() || mop.update() || mop.add()) { // 如果不是普通用户。。
				// 获得所有新闻信息
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
			} else { // 普通用户，获得已发布的新闻信息
				// 查询已发布的新闻
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

			// 根据用户ID循环查询用户信息
			String[] accepterId = arr_newInfo[8].split(","); // 按逗号拆分
			List<Employee> targetUsers = new ArrayList<Employee>();
			for (String targetUser : accepterId) {
				Employee employee = empMgt.getEmployeeById(targetUser);
				if (null != employee) {
					targetUsers.add(employee);
				}
			}
			// 查询通知部门
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

			// 查找职员分组
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
		return getForward(request, mapping, "to_newInfo"); // 跳转到详细新闻页
	}

	
	/**
	 * 新增新闻前的准备
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
	 * 查询修改新闻页
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
		// 根据新闻id查询新闻信息
		String newsId = request.getParameter("newsId"); // 获得新闻ID
		// tj+假如是通过查询出来的结果数据进行修改，则需要保存查询条件
		String newsType1 = request.getParameter("newsType1");
		String newsTitle1 = request.getParameter("newsTitle1");
		String proUserName1 = request.getParameter("proUserName1");
		String beginTime1 = request.getParameter("beginTime1");
		String endTime1 = request.getParameter("endTime1");
		String pageNo = request.getParameter("pageNo");// 当前页数
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

		// 根据用户ID循环查询用户信息
		String[] accepterId = arr_newsInfo[8].split(","); // 按逗号拆分
		List<Employee> targetUsers = new ArrayList<Employee>();
		for (String targetUser : accepterId) {
			Employee employee = empMgt.getEmployeeById(targetUser);
			if (null != employee) {
				targetUsers.add(employee);
			}
		}
		// 查询通知部门
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

		// 查找职员分组
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

		String[] wakeUpType = null;// 提醒方式
		if (arr_newsInfo[7] != null && !"".equals(arr_newsInfo[7])) {
			wakeUpType = arr_newsInfo[7].split(",");
		}
		request.setAttribute("targetUsers", targetUsers);
		request.setAttribute("targetDept", targetDept);
		request.setAttribute("targetEmpGroup", listEmpGroup);
		request.setAttribute("arr_newsInfo", arr_newsInfo);
		request.setAttribute("wakeUpType", wakeUpType); // 提示方式
		return getForward(request, mapping, "to_updNewsInfo");
	}

	/**
	 * 删除新闻
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
		// 查询条件
		String newsTitle = request.getParameter("newsTitle") == null ? ""
				: request.getParameter("newsTitle"); // 获得新闻标题
		String newsType = request.getParameter("newsType") == null ? ""
				: request.getParameter("newsType"); // 获得新闻类别
		String beginTime = request.getParameter("beginTime") == null ? ""
				: request.getParameter("beginTime"); // 条件时间前
		String endTime = request.getParameter("endTime") == null ? "" : request
				.getParameter("endTime"); // 条件时间后
		String proUserId = request.getParameter("proUserId") == null ? ""
				: request.getParameter("proUserId");// 发布人id
		String proUserName = request.getParameter("proUserName") == null ? ""
				: request.getParameter("proUserName");// 发布人id
		String pageNo = request.getParameter("pageNo") == null ? "" : request
				.getParameter("pageNo");// 当前页数
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
		// 获得页面参数-->需要删除到ID
		String newsInfoId[] = request.getParameterValues("keyId");
		Result rs_newsInfo = null;
		for (int i = 0; i < newsInfoId.length; i++) {
			// 查出要删除的新闻的信息，以便从即时消息（MessageBean）中删除记录
			Result r = mgt.sel_CompanyNewsInfo_By_newsId(newsInfoId[i]);
			// tangjing+删除新闻
			rs_newsInfo = mgt.del_newsInfo(newsInfoId[i]);

			if (r.getRealTotal() >= 1) {
				// 从查询信息中获取到新闻信息
				String[] arr_newsInfo = (String[]) r.getRetVal();
				// 在即时消息中查询有关此条新闻的发送出去的消息
				// Result result1=mmgt.queryByCondition(arr_newsInfo[5],"send",
				// arr_newsInfo[2],arr_newsInfo[2]);
				// List list=(List)result1.getRetVal();
				// String ids[]=new String[10];//存即时消息Id
				// 如果存在有关即时消息
				// if(list.size()>0){
				// for(int p=0;p<list.size();p++)
				// {
				// //循环获取即时消息，保存id到集合中
				// MessageBean messageBean=(MessageBean)list.get(p);
				// mmgt.del_message(messageBean.getId());
				// }
				// }
				// 批量删除即时消息记录
				// Result rk= mmgt.delete(ids);
				// System.out.println(rk.getRealTotal());
			}
			// 从即时消息中删除此条记录
		}
		if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 删除成功
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
			// 添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}
	
	
	/**
	 * 删除新闻回复mj
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
	 * 修改新闻
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

		String userId = this.getLoginBean(request).getId(); // 获得登陆者ID
		String newsId = request.getParameter("newsId"); // 获得新闻ID
		String newsTitle = request.getParameter("newsTitle"); // 获得新闻标题
		String newsType = request.getParameter("newsType"); // 获得新闻类别
		String newsContent = request.getParameter("Notepadcontent"); // 获得新闻內容
		String isUsed = request.getParameter("isUsed"); // 获得是否发布
		String[] wakeupType = request.getParameterValues("wakeUpMode");// 提醒方式
		String popedomUserId = request.getParameter("popedomUserIds"); // 获得通知对象
		String popedomDeptId = request.getParameter("popedomDeptIds"); // 获得能知部门
		String empGroupId = request.getParameter("EmpGroupId");// 获得通知 职员分组
		String isAlonePopedmon = request.getParameter("isAlonePopedmon");// 是否单独授权
		String isSaveReading = request.getParameter("isSaveReading");
		String picFiles = request.getParameter("picFiles");
		picFiles = picFiles == null ? "" : picFiles;
		String whetherAgreeReply = request.getParameter("whetherAgreeReply");//是否允许回复

		// 需删除的附件
		String delFiles = request.getParameter("delPicFiles");
		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0 && picFiles.indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		String pageNo = request.getParameter("pageNo");
		if (popedomUserId != null && !"".equals(popedomUserId)
				&& !popedomUserId.contains(userId)) {// 给自己授权
			popedomUserId += userId + ",";
		}
		// 假如是通过按条件查询出来的数据进行修改的，应需求必须把客户查询条件保存
		String newsType1 = request.getParameter("newsType1");
		String newsTitle1 = request.getParameter("newsTitle1");
		String proUserName1 = request.getParameter("proUserName1");
		String beginTime1 = request.getParameter("beginTime1");
		String endTime1 = request.getParameter("endTime1");

		// 提醒方式
		String wakeTypes = "";
		if (wakeupType != null && wakeupType.length > 0) {
			for (String str : wakeupType) {
				wakeTypes += str + ",";
			}
		}
		// 如果不是单独授权，则把popedomUserId,popedomDeptId清空。
		if (isAlonePopedmon == "0") {
			popedomUserId = "";
			popedomDeptId = "";
		}
		//mj 通知通告 解决 新闻修改为未发布 通知通告里面却可以看见
		Result rs = mgt.getNewsById(newsId);
		String[] isUseds = (String[])rs.getRetVal();
		String used = isUseds[0];
		if (!isUsed.equals(used))  {//
			//修改 该新闻对应的通知通告 是否存在 存在改变
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
						bean.setExist("noPub");//没有立即发布 稍后发布
					}
					String isExist = bean.getExist();
					rs = avMgt.updateIsUsedById(bean.getId(),isExist);
				}
			}
			
			
		}
		// 修改
		Result rs_newsInfo = mgt.upd_NewsInfo(newsId, newsTitle, newsType,
				newsContent, isUsed, userId, userId, wakeTypes,
				isAlonePopedmon, popedomUserId, popedomDeptId, empGroupId,
				isSaveReading, picFiles,whetherAgreeReply);

		// 添加提醒方式
		String title =OnlineUserInfo.getUser(userId).getName()+GlobalsTool.getMessage(request, "oa.news.newsUpdate") + ":" + newsTitle; // 标题

		String url = request.getRequestURI();
		String favoriteURL = java.net.URLEncoder
				.encode(url + "?noback=true&operation=5&newsId=" + newsId + "&isEspecial=1");

		// 修改发布通知里面的内容
		newsContent = "<a href=javascript:mdiwin('" + favoriteURL + "','"
				+ GlobalsTool.getMessage(request, "oa.common.newList") + "')>"
				+ title + getMessage(request, "com.click.see") + "</a>"; // 内容

		if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 修改成功

			if ("1".equals(isUsed)) { // 如果是发布的
				// 通知对象
				String popedomUserIds = "";
				if (!"".equals(newsId) && null != newsId) {
					// 将通知发送给每一个通知对象
					if ("0".equals(isAlonePopedmon)) { // 判断是否单独授权
						List listEmp = (List) adviceMgt.sel_allEmployee()
								.getRetVal();
						for (int i = 0; i < listEmp.size(); i++) { // 循环发送
							popedomUserIds += String.valueOf(listEmp.get(i))
									+ ",";
						}
					} else {
						if (null != popedomDeptId && !"".equals(popedomDeptId)) {
							String[] deptIds = popedomDeptId.split(","); // 根据部门编号查找部门人员
							for (String departId : deptIds) {
								List<Employee> list_emp = adviceMgt
										.queryAllEmployeeByClassCode(departId);
								for (Employee emp : list_emp) {
									if (!popedomUserId.contains(emp.getid())) {// 判断是否已经单独授权
										popedomUserIds += emp.getid() + ",";
									}
								}
							}
						}
						if (!"".equals(popedomUserId) && null != popedomUserId) { // 如果接受对象不为空
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
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}
	/**
	 * 回复新闻 mj
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
			if ("advice".equals(replyType)){//跳转到通知详细页面
				EchoMessage.success().add(getMessage(request, "com.reply.to.succeed"))
                .setBackUrl("/OAAdvice.do?operation=5&adviceId="+replyForm.getNewsId()+"&pageSize="+pageSize)
                .setAlertRequest(request);
			} else if ("replyInfo".equals(replyType)){//跳转到回复页面
			
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
			if ("advice".equals(replyType)) {// 跳转到通知详细页面
				EchoMessage.success().add(
						getMessage(request, "com.reply.to.failure"))
						.setBackUrl("/OAAdvice.do").setAlertRequest(request);
			} else if ("replyInfo".equals(replyType)) {// 跳转到回复页面
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
	 * 把java对象转化成的json数据,
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
	 * 获取所有回复信息关于主题的（新闻，通知）mj
	 * 
	 */
	protected ActionForward showAllReplys(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String pageNo = request.getParameter("pageNo");// 当前页数
			System.out.println("当前页数 = "+pageNo);
			request.setAttribute("pageNo", pageNo);
			String newsId = request.getParameter("newsId"); // 新闻ID
			System.out.println("新闻id" + newsId);
			request.setAttribute("newsId", newsId);
			int pageNo2 = getParameterInt("pageNo", request) ;
			System.out.println("当前页数2 = "+pageNo2);
			/*查询回复帖子*/
			
			int pageSize = getParameterInt("pageSize", request) ;
			if(pageSize==0){
				pageSize = GlobalsTool.getPageSize() ;
			}
		
			
			//是否只查看该用户的
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
		return getForward(request, mapping, "toReplyInfo");//跳转到详细评论列表页面
	}

	/**
	 * 回复顶一下或者查询最新 最早 最热的回复帖子 mj
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
			String replyId = request.getParameter("replyId"); // 新闻回复ID
			String newsId = request.getParameter("newsId");
			Result	rs = null;	
			if (StringUtils.isNotBlank(replyId)){//顶一下
				rs = mgt.getReplyById(replyId);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					OANewsInfoReplyBean reply = (OANewsInfoReplyBean)rs.getRetVal();
					reply.setAgreeNum(reply.getAgreeNum()+1);
					rs = mgt.updateReply(reply);
				}else{	//失败
					EchoMessage.error().add(
							getMessage(request, "com.revert.to.failure"))
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}
			
			int pageNo2 = getParameterInt("pageNo", request) ;
			System.out.println("当前页数2 = "+pageNo2);
			int pageSize = getParameterInt("pageSize", request) ;
			if(pageSize==0){
				pageSize = GlobalsTool.getPageSize() ;
			}
			//是否只查看该用户的
			String auth = request.getParameter("auth");
			String orderType = request.getParameter("orderType");
			if (StringUtils.isBlank(orderType)) {
				orderType = "desc";
			}
			//查看所有的回复数据
			rs = mgt.queryReplyNewsInfo(newsId,auth,orderAttribute,orderType,pageNo2<=0?1:pageNo2,pageSize<=0?100:pageSize) ;
			//根据请求是否用到ajax来判断 是否返回json(通知通告等用ajax，新闻用的iframe）
			String requestType = request.getParameter("requestType");
			List<OANewsInfoReplyBean> list = new ArrayList<OANewsInfoReplyBean>();
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				list = (List<OANewsInfoReplyBean>)rs.retVal ;	
			}
			//将这些值保存起来 到界面 通过form下 hidden传递给 action保持记忆功能 或者 在才用session保存信息 每次获取 不同更新 否则 对应的 上下一页无记忆功能
			request.getSession().setAttribute("orderAttribute", orderAttribute);
			request.getSession().setAttribute("orderType", orderType);
			if(StringUtils.equals("noAjax",requestType)) {
				//ajax newsInfo(replyInfo)
				/*
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					// 根据ID获得新闻信息
					Result rs_newsInfo = mgt.sel_CompanyNewsInfo_By_newsId(newsId);
					if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						String arr_newInfo[] = (String[]) rs_newsInfo.getRetVal();
						request.setAttribute("arr_newInfo", arr_newInfo);
						//查看是否允许回复 mj
						String whetherAgreeReply = arr_newInfo[15];
						request.setAttribute("whetherAgreeReply", whetherAgreeReply);
						request.setAttribute("arr_newInfo", arr_newInfo);
					}
					request.setAttribute("replayList", rs.retVal) ;
					if(rs.realTotal >0){
						request.setAttribute("pageBar", pageBar(rs, request)) ;
					}
				}
				return getForward(request, mapping, "toReplyInfo");//跳转到详细评论列表页面
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
        //进行唯一用户验证，如果有重复登陆的，则后进入用户踢出前进入用户
        if (!OnlineUserInfo.checkUser(req)) {
            //需踢出
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
//        //进行唯一用户验证，如果有重复登陆的，则后进入用户踢出前进入用户
//        if (!OnlineUserInfo.checkUser(req)) {
//            //需踢出
//            EchoMessage.error().setAlertRequest(req);
//            return getForward(req, mapping, "doubleOnline");
//        }        
//        OABBSUserBean userBean = (OABBSUserBean)req.getSession().getAttribute("BBSUser") ;
//        if(userBean == null || !loginBean.getId().equals(userBean.getUserID())){
//        	//没有用户信息，或者用户信息和登陆用户不一致。
//			/*加载论坛 用户信息,每次更新用户信息，需要重新进入论坛*/
//			Result result = bbsMgt.loadBBSUser(loginBean.getId()) ;
//			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.totalPage>0){
//				userBean = ((List<OABBSUserBean>)result.retVal).get(0) ;
//				req.getSession().setAttribute("BBSUser", userBean) ;
//			}
//			/*如果用户还注册 先注册*/
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
