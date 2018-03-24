package com.koron.oa.executive.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.dbfactory.Result;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.FileBean;
import com.koron.oa.bean.MailinfoSettingBean;
import com.koron.oa.bean.OAJobAuditingBean;
import com.koron.oa.bean.OAJobBean;
import com.koron.oa.bean.OAjobRestoreBean;
import com.koron.oa.message.MessageForm;
import com.koron.oa.message.MessageMgt;
import com.koron.oa.util.EmployeeMgt;
import com.menyi.aio.bean.AdviceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.advice.AdviceForm;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;

public class OAJobAction extends MgtBaseAction {
	OAJobMgt mgt = new OAJobMgt();
	AdviceMgt amgt=new AdviceMgt();
	EmployeeMgt emgt = new EmployeeMgt();

	public OAJobAction() {
	}

	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 根据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		String a=GlobalsTool.getStringDate(request.getAttribute("winCurIndex"));
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
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			String type=request.getParameter("type");
			if(type!=null&&type.equals("sendEmail")){
				forward = sendEmailPre(mapping, form, request, response);
			}else{
				forward = detail(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_CHECK:
			forward = auditing(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 添加前的准备,协助单页面点击添加会执行此方法
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
		request.getParameter("winCurIndex");
		return getForward(request, mapping, "addjob");
	}

	/**
	 * 添加,在添加页面里点添加后会执行此方法
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
		OAJobBean oaJob = new OAJobBean();
		String id = IDGenerater.getId(); // 自动生成一个ID
		oaJob.setId(id);
		LoginBean loginBean = this.getLoginBean(request);// 获取登录用户
		oaJob.setCreatePerson(loginBean.getId());// 添加当前用户为创建人
		// 获取当前时间为创建时间
		String createTime = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		oaJob.setCreateTime(createTime);
		String winCurIndex=GlobalsTool.getStringDate(request
				.getAttribute("winCurIndex1"));
		request.setAttribute("winCurIndex", winCurIndex);
		// 获取参数
		String jobtheme = request.getParameter("jobtheme");
		String jobType = request.getParameter("jobType");
		String participant = request.getParameter("participant");
		String jobBeginTime =request.getParameter("jobBeginTime");
		int jobBeginTimeHour =Integer.parseInt(request.getParameter("jobBeginTimeHour"));
		int jobBeginTimeMinute = Integer.parseInt(request.getParameter("jobBeginTimeMinute"));
		String jobEndTime = request.getParameter("jobEndTime");
		int jobEndTimeHour = Integer.parseInt(request.getParameter("jobEndTimeHour"));
		int jobEndTimeMinute = Integer.parseInt(request.getParameter("jobEndTimeMinute"));
		String assessor = request.getParameter("assessor");
		String intterfixServer = request.getParameter("intterfixServer");
		String elaborateOn =request.getParameter("elaborateOn");
		String state = request.getParameter("state");
		//附件
		String accessories = request.getParameter("attachFiles");
		accessories = accessories== null?"":accessories;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && accessories.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/job/" + del);
				aFile.delete();
			}
		}	
		String isSaveReading = GlobalsTool.getStringDate(request.getParameter("isSaveReading"));//是否保存阅读痕迹
		// 处理时间
		jobBeginTime = jobBeginTime
				+ " "
				+ ((jobBeginTimeHour < 10) ? "0" + jobBeginTimeHour + ""
						: jobBeginTimeHour + "")
				+ ":"
				+ ((jobBeginTimeMinute < 10) ? "0" + jobBeginTimeMinute + ""
						: jobBeginTimeMinute + "") + ":" + "00";

		jobEndTime = jobEndTime
				+ " "
				+ ((jobEndTimeHour < 10) ? "0" + jobEndTimeHour + ""
						: jobEndTimeHour + "")
				+ ":"
				+ ((jobEndTimeMinute < 10) ? "0" + jobEndTimeMinute + ""
						: jobEndTimeMinute + "") + ":" + "00";

		oaJob.setAttaches(accessories);
		oaJob.setAssessor(assessor);
		oaJob.setElaborateOn(elaborateOn);
		oaJob.setLastUpdateTime(createTime);
		oaJob.setIntterfixServer(intterfixServer);
		oaJob.setJobBeginTime(jobBeginTime);
		oaJob.setJobEndTime(jobEndTime);
		oaJob.setJobtheme(jobtheme);
		oaJob.setJobType(jobType);
		oaJob.setParticipant(participant);
		oaJob.setState(state);
        oaJob.setIsSaveReading(isSaveReading);
		Result rs_jobadd = new Result();
		rs_jobadd = mgt.addJob(oaJob);
		ActionForward forward = getForward(request, mapping, "alert");
		// 如果工作协助单添加成功，给指定的参与人，审核人发送即时消息
		if (rs_jobadd.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 给审核者发送通知提醒
			// 获取审核人ID
			String receive = oaJob.getAssessor();
			String title = loginBean.getEmpFullName() + getMessage(request, "oa.job.createAssistBill")+":" + oaJob.getJobtheme() + "  "+getMessage(request, "oa.job.approve");
			amgt.add(loginBean.getId(), 
					title, 
					"<a href=\"javascript:mdiwin('/OAJob.do?noback=true&operation=5&userType=assessor&keyId=" + id + "','" + getMessage(request, "oa.job.jobDetail") +"')\">"+title+"</a>", 
					receive, 
					id, 
					"notApprove");

			// 给参与者发送即时消息
			// 获取参与者
			receive = oaJob.getParticipant();
			title = loginBean.getEmpFullName() + getMessage(request, "oa.job.createAssistBill") + ":" + oaJob.getJobtheme()
					+ "  "+getMessage(request, "oa.job.plesrestore");

			amgt.add(loginBean.getId(), 
					title, 
					"<a href=\"javascript:mdiwin('/OAJob.do?noback=true&operation=5&userType=assessor&keyId=" + id + "','" + getMessage(request, "oa.job.jobDetail") +"')\">"+title+"</a>", 
					receive, 
					id, 
					"other");
			
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess"))
					.setBackUrl("/OAJob.do?winCurIndex=" + winCurIndex)
					.setAlertRequest(request);
		} else {
			// 添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
					.setAlertRequest(request);
		}
		return forward;
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
		// 获取查查询参数
		String createPerson = request.getParameter("createPerson");
		String createPersonName = request.getParameter("createPersonName");
		String jobtheme = request.getParameter("jobtheme");
		String jobBeginTime = request.getParameter("jobBeginTime");
		String jobEndTime = request.getParameter("jobEndTime");
		   if("GET".equals(request.getMethod()))
	        {
	    	   createPersonName=createPersonName==null?"":GlobalsTool.toChinseChar(createPersonName);
	    	   jobtheme=jobtheme==null?"":GlobalsTool.toChinseChar(jobtheme);
	        }
		request.setAttribute("createPerson", createPerson);
		request.setAttribute("createPersonName", createPersonName);
		request.setAttribute("jobtheme", jobtheme);
		String winCurIndex=request.getParameter("winCurIndex");
		request.setAttribute("winCurIndex", winCurIndex);
		ActionForward forward = null;
		String nstr = request.getParameter("keyId");
		Result rs = mgt.detail(nstr, OAJobBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 加载成功
			OAJobBean jobBean = (OAJobBean) rs.retVal;
			// 获取所有的参与者
			String participantNames = this.getParticipantNames(jobBean);
			request.setAttribute("participantNames", participantNames);
			// 获取所有审核者
			String assessorNames = this.getassessorNames(jobBean);
			request.setAttribute("assessorNames", assessorNames);
			// 获取关联单位
			String IntterfixServerName = mgt.findClientNameById(jobBean.getIntterfixServer());
			request.setAttribute("IntterfixServerName", IntterfixServerName);

			request.setAttribute("result", rs.retVal);
			jobBeginTime = jobBean.getJobBeginTime();
			jobEndTime = jobBean.getJobEndTime();
			request.setAttribute("beginTime", jobBeginTime.substring(0, 10));
			int beginHour = Integer.parseInt(jobBeginTime.substring(11, 13));
			int beginMin = Integer.parseInt(jobBeginTime.substring(14, 16));
			request.setAttribute("jobBeginTimeHour", beginHour);
			request.setAttribute("jobBeginTimeMin", beginMin);
			request.setAttribute("endTime", jobEndTime.substring(0, 10));
			int endHour = Integer.parseInt(jobEndTime.substring(11, 13));
			int endMin = Integer.parseInt(jobEndTime.substring(14, 16));
			request.setAttribute("jobEndTimeHour", endHour);
			request.setAttribute("jobEndTimeMin", endMin);
			forward = getForward(request, mapping, "updatejob");
		} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// 记录不存在错误
			EchoMessage.error().add(getMessage(request, "common.error.nodata"))
					.setRequest(request);
		} else {
			// 加载失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			forward = getForward(request, mapping, "updatejob");
		}
		return forward;
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
		// 获取查查询参数
		String createPerson1 =request.getParameter("createPerson1");
		String createPersonName1 = request.getParameter("createPersonName1");
		String jobtheme1 = request.getParameter("jobtheme1");
		String jobBeginTime1 =request.getParameter("jobBeginTime1");
		String jobEndTime1 = request.getParameter("jobEndTime1");
		String winCurIndex=GlobalsTool.getStringDate(request.getAttribute("winCurIndex1"));
		ActionForward forward = null;
		// 获取要修改的对象的主键，在修改页的隐藏表单域里
		String oaid = request.getParameter("oaid") == null ? "" : request.getParameter("oaid").toString();
		OAJobBean oaJob = new OAJobBean(); 
		oaJob.setId(oaid);// 设置要修改的ID
		oaJob = (OAJobBean) mgt.getJob(oaid).getRetVal(); 
		String jobtheme = request.getParameter("jobtheme");
		String jobType = request.getParameter("jobType");
		String participant = request.getParameter("participant");
		String jobBeginTime = request.getParameter("jobBeginTime");
		String jobEndTime = request.getParameter("jobEndTime");
		int jobBeginTimeHour =Integer.parseInt(request.getParameter("jobBeginTimeHour"));
		int jobBeginTimeMinute = Integer.parseInt(request.getParameter("jobBeginTimeMinute"));
		int jobEndTimeHour = Integer.parseInt(request.getParameter("jobEndTimeHour"));
		int jobEndTimeMinute = Integer.parseInt(request.getParameter("jobEndTimeMinute"));
		String assessor = request.getParameter("assessor");
		String intterfixServer = request.getParameter("intterfixServer");
		String elaborateOn = request.getParameter("elaborateOn");
		String state =request.getParameter("state");
		String isSaveReading = GlobalsTool.getStringDate(request.getParameter("isSaveReading"));//是否保存阅读痕迹
		oaJob.setIsSaveReading(isSaveReading);

		//处理时间
		jobBeginTime = jobBeginTime
				+ " "
				+ ((jobBeginTimeHour < 10) ? "0" + jobBeginTimeHour + ""
						: jobBeginTimeHour + "")
				+ ":"
				+ ((jobBeginTimeMinute < 10) ? "0" + jobBeginTimeMinute + ""
						: jobBeginTimeMinute + "") + ":" + "00";

		jobEndTime = jobEndTime
				+ " "
				+ ((jobEndTimeHour < 10) ? "0" + jobEndTimeHour + ""
						: jobEndTimeHour + "")
				+ ":"
				+ ((jobEndTimeMinute < 10) ? "0" + jobEndTimeMinute + ""
						: jobEndTimeMinute + "") + ":" + "00";
		//附件
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/job/" + del);
				aFile.delete();
			}
		}	

		oaJob.setAssessor(assessor);
		oaJob.setElaborateOn(elaborateOn);
		oaJob.setIntterfixServer(intterfixServer);
		oaJob.setJobBeginTime(jobBeginTime);
		oaJob.setJobEndTime(jobEndTime);
		oaJob.setJobtheme(jobtheme);
		oaJob.setJobType(jobType);
		oaJob.setParticipant(participant);
		oaJob.setState(state);
		oaJob.setAttaches(mailAttaches);
		
//		1.根据工作协助单删除数据库中原有的相关通知消息的记录
		amgt.deleteByRelationId(oaid, "");	
		
        // 2.根据相关参数，给审批者发送即时消息
		LoginBean loginBean = this.getLoginBean(request);// 获取登录用户
		
		String receive = oaJob.getAssessor();
		String title = loginBean.getEmpFullName() + getMessage(request, "oa.job.createAssistBill")+":" + oaJob.getJobtheme() + "  "+getMessage(request, "oa.job.approve");
		amgt.add(loginBean.getId(), 
				title, 
				"<a href=\"javascript:mdiwin('/OAJob.do?noback=true&operation=5&userType=assessor&keyId=" + oaid + "','" + getMessage(request, "oa.job.jobDetail") +"')\">"+title+"</a>", 
				receive, 
				oaid, 
				"notApprove");

		// 给参与者发送即时消息
		// 获取参与者
		receive = oaJob.getParticipant();
		title = loginBean.getEmpFullName() + getMessage(request, "oa.job.createAssistBill") + ":" + oaJob.getJobtheme()
				+ "  "+getMessage(request, "oa.job.plesrestore");

		amgt.add(loginBean.getId(), 
				title, 
				"<a href=\"javascript:mdiwin('/OAJob.do?noback=true&operation=5&userType=assessor&keyId=" + oaid + "','" + getMessage(request, "oa.job.jobDetail") +"')\">"+title+"</a>", 
				receive, 
				oaid, 
				"other");

		// 执行修改
		Result rs = mgt.updateOAJob(oaJob);
		forward = getForward(request, mapping, "alert");
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 修改成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl(
							"/OAJob.do?winCurIndex="
									+ winCurIndex
									+ "&createPerson=" + createPerson1
									+ "&createPersonName=" +GlobalsTool.encode(createPersonName1)
									+ "&jobtheme=" + GlobalsTool.encode(jobtheme1)
									+ "&jobBeginTime=" + jobBeginTime1
									+ "&jobEndTime=" + jobEndTime1 )
					.setAlertRequest(request);

		} else {
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro"))
					.setAlertRequest(request);
		}
		return forward;
	}

	public boolean getDletedFile(String[] str_array, String str) {
		boolean talg = false;
		if (null != str_array) {
			for (String s : str_array) {
				if (s.equals(str)) {
					talg = true;
				}
			}
		}
		return talg;
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
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OAJobSearchForm jobForm = (OAJobSearchForm) form;
		// 获取查查询参数
		String createPerson = request.getParameter("createPerson");
		String createPersonName = request.getParameter("createPersonName");
		String jobtheme = request.getParameter("jobtheme");
		String jobBeginTime = request.getParameter("jobBeginTime");
		String jobEndTime = request.getParameter("jobEndTime");
		String auditStatus=request.getParameter("auditStatus");
		if ("GET".equals(request.getMethod())) {
			createPersonName = createPersonName == null ? "" : GlobalsTool.toChinseChar(createPersonName);
			jobtheme = jobtheme == null ? "" : GlobalsTool.toChinseChar(jobtheme);
		}
		request.setAttribute("createPerson", createPerson);
		request.setAttribute("createPersonName", createPersonName);
		request.setAttribute("jobtheme", jobtheme);
		request.setAttribute("jobBeginTime", jobBeginTime);
		request.setAttribute("jobEndTime", jobEndTime);
		request.setAttribute("auditStatus",auditStatus);

		LoginBean loginBean = getLoginBean(request);// 获取登录用户
		String winCurIndex=request.getParameter("winCurIndex");
		request.setAttribute("winCurIndex", winCurIndex);
		if (jobForm != null) {
			// 执行查询
			Result rs = mgt.query2(loginBean.getId(), createPersonName, jobtheme,
					jobBeginTime, jobEndTime,auditStatus,jobForm.getPageNo(),jobForm.getPageSize());
			List<OAJobBean> listOajob = (List<OAJobBean>) rs.retVal;
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 查询成功
				Result rss = null;
				EmployeeBean employeeBean = null;
				// 获取协助单创建人
				for (int i = 0; i < listOajob.size(); i++) {
					rss = mgt.detail(listOajob.get(i).getCreatePerson(),
							EmployeeBean.class);
					employeeBean = (EmployeeBean) rss.retVal;
					if (employeeBean != null) {
						listOajob.get(i).setCreatePerson(
								employeeBean.getEmpFullName());
					}

				}
				request.setAttribute("pageBar", this.pageBar(rs, request));
				request.setAttribute("result",rs.getRetVal());
			} else {
				// 查询失败
				EchoMessage.error()
						.add(getMessage(request, "common.msg.error"))
						.setRequest(request);
				return getForward(request, mapping, "queryjob");
			}
		}
		return getForward(request, mapping, "jobindex");
	}
	/**
     * 拼邮件内容
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward sendEmailPre(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws
            Exception {    	
    	String keyId = request.getParameter("keyId");
    	request.setAttribute("keyId", keyId);
    	request.setAttribute("tableName", "OAJobodd");
        String html_str = "";
        if (keyId != null && keyId.length() != 0) {
			// 执行加载明细
			Result rs = mgt.detail(keyId, OAJobBean.class);
			OAJobBean oajobBean = (OAJobBean) rs.retVal;
		
			// 加载所有参与者的回复
			Object RestoreList = mgt.getRestore(keyId).getRetVal();
			List<OAjobRestoreBean> restoreList = new ArrayList<OAjobRestoreBean>();
			if (RestoreList != null) {
				restoreList = (ArrayList<OAjobRestoreBean>) RestoreList;
				for (int i = 0; i < restoreList.size(); i++) {
					restoreList.get(i)
							.setParticipantPerson(OnlineUserInfo.getUser(restoreList.get(i).getParticipantPerson()).getName());
				}
			}

			// 加载所有审核者的审核说明
			Object AuditingList = mgt.getAuditing(keyId).getRetVal();
			List<OAJobAuditingBean> auditingList = new ArrayList<OAJobAuditingBean>();
			if (AuditingList != null) {
				auditingList = (ArrayList<OAJobAuditingBean>) AuditingList;
				for (int i = 0; i < auditingList.size(); i++) {
					auditingList.get(i).setAssessor(OnlineUserInfo.getUser( auditingList.get(i).getAssessor()).getName());
				}
			}
			String participantNames = this.getParticipantNames(oajobBean);
			String assessorNames = this.getassessorNames(oajobBean);
			String IntterfixServerName = mgt
					.getIntterfixServerNameById(oajobBean.getIntterfixServer()).retVal
					.toString();
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            	html_str += "<STYLE type=text/css>";
                html_str += "BODY {FONT-SIZE: 12px; MARGIN: 0px; SCROLLBAR-SHADOW-COLOR: #999999; SCROLLBAR-ARROW-COLOR: #555555; SCROLLBAR-DARKSHADOW-COLOR: #ffffff; SCROLLBAR-BASE-COLOR: #e0e0e0; HEIGHT: 100%; BACKGROUND-COLOR: #ffffff}\n";
                html_str +=
                        "A:link {COLOR: #000000; TEXT-DECORATION: none}\n";
                html_str +=
                        "A:visited {COLOR: #000000; TEXT-DECORATION: none}\n";
                html_str +=
                        "A:hover {COLOR: #0060ff; TEXT-DECORATION: none}\n";
                html_str +=
                        "A:active {COLOR: #0060ff; TEXT-DECORATION: none}\n";
                html_str += "IMG {BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px}\n";
                html_str += "LI {LIST-STYLE-TYPE: none}\n";
                html_str += ".scroll_function_small {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 3px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; OVERFLOW: auto; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid; HEIGHT: 80%}\n";
                html_str += ".scroll_function_small_1 {BORDER-RIGHT: #e0e0e 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; OVERFLOW: auto; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid; HEIGHT: 80%}\n";
                html_str += ".scroll_function_small_2 {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
                html_str += ".scroll_function_small_3 {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
                html_str += ".scroll_function_small_repotlist {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 885px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
                html_str +=
                        ".listRange_list_function {BORDER-RIGHT: #d2d2d2 1px solid}\n";
                html_str += ".listRange_list_function TBODY TD {BORDER-TOP-WIDTH: 0px; PADDING-RIGHT: 1px; PADDING-LEFT: 5px; FONT-SIZE: 12px; BORDER-LEFT: #d2d2d2 1px solid; BORDER-BOTTOM: #d2d2d2 1px solid; WHITE-SPACE: nowrap; HEIGHT: 22px}\n";
                html_str += ".listRange_list_function THEAD TR {}\n";
                html_str += ".listRange_list_function THEAD TD {PADDING-RIGHT: 5px; PADDING-LEFT: 5px; FONT-SIZE: 12px; FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#cccccc,endColorStr=#ffffff); BORDER-LEFT: #d2d2d2 1px solid; COLOR: #42789c; PADDING-TOP: 4px; BORDER-BOTTOM: #d2d2d2 1px solid; WHITE-SPACE: nowrap; HEIGHT: 22px; TEXT-ALIGN: center}\n";
                html_str += ".listRange_list_function INPUT {BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; TEXT-ALIGN: left; BORDER-RIGHT-WIDTH: 0px}\n";
                html_str += ".listRange_list_function SELECT {BORDER-TOP-WIDTH: 0px; PADDING-RIGHT: 0px; PADDING-LEFT: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; PADDING-BOTTOM: 0px; WIDTH: 100%; PADDING-TOP: 0px; TEXT-ALIGN: left; BORDER-RIGHT-WIDTH: 0px}\n";
                html_str += ".listRange_list_statistic {BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid; BACKGROUND-COLOR: #fefef4}\n";
                html_str += ".listRange_list_statistic TD {PADDING-RIGHT: 5px; PADDING-LEFT: 5px; FONT-WEIGHT: bold; BORDER-LEFT: #d2d2d2 1px solid; PADDING-TOP: 4px; HEIGHT: 22px}\n";
                html_str += ".HeadingTitle {font-size:14px;font-weight:bold;MARGIN-TOP: 5px; PADDING-LEFT: 10px; FLOAT: left; WIDTH: 150px; PADDING-TOP: 8px; BORDER-BOTTOM: #81b2e3 1px solid; HEIGHT: 17px; TEXT-ALIGN: left}\n";

                html_str += ".scroll_function_big {FLOAT: left; MARGIN-BOTTOM: 3px; WIDTH: 900px; HEIGHT: 600px;}\n";
                html_str += ".listRange_1 {BORDER-RIGHT: #e0e0e0 1px solid;PADDING-RIGHT: 0px;BORDER-TOP: #e0e0e0 1px solid;MARGIN-TOP: 5px; PADDING-LEFT: 0px;FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#E6F4FD,endColorStr=#ffffff);FLOAT: left;PADDING-BOTTOM: 2px;MARGIN-LEFT: 3px;BORDER-LEFT: #e0e0e0 1px solid;WIDTH: 886px;PADDING-TOP: 2px;BORDER-BOTTOM: #e0e0e0 1px solid;HEIGHT: auto; TEXT-ALIGN: left}\n";
                html_str += ".listRange_1 LI {FLOAT: left; WIDTH: 215px}\n";
                html_str += ".listRange_1 LI2 {FLOAT: left; WIDTH: 415px}\n";
                html_str += ".listRange_1 BUTTON {VERTICAL-ALIGN: top}\n";
                html_str += ".listRange_1_button LI {MARGIN-TOP: -5px; FLOAT: left; MARGIN-BOTTOM: 5px; VERTICAL-ALIGN: top; WIDTH: 827px; TEXT-ALIGN: right}\n";
                html_str += ".listRange_1 LI SPAN {MARGIN-TOP: 5px; FLOAT: left; WIDTH: 85px; TEXT-ALIGN: right}\n";
                html_str += ".listRange_1 LI DIV {MARGIN-TOP: 6px; FLOAT: left; MARGIN-LEFT: 3px; WIDTH: auto}\n";
                html_str += ".listRange_1 INPUT {FLOAT: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"left\":\"\"); ; MARGIN-LEFT: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"3px\":\"\"); ; WIDTH: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"13px\":\"100px\"); ; BORDER: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"\":\"1px solid #42789C\"); TEXT-ALIGN: left}\n";
                html_str +=
                        ".listRange_1 LI SELECT {WIDTH: 100px; TEXT-ALIGN: left}\n";
                html_str += "\n";
                html_str += "</STYLE>\n";
                html_str += "<DIV class=HeadingTitle>" +this.getMessage(request, "oa.job.jobDetail")+ "</DIV>\n";
                html_str += "<DIV class=scroll_function_big>\n";
                html_str += "<UL class=listRange_1>\n";
                
                html_str += "<LI><SPAN>" +this.getMessage(request, "oa.subjects")+"：</SPAN>"+oajobBean.getJobtheme()+"</LI>";
                html_str += "<LI><SPAN>" +this.getMessage(request, "customTable.lb.tableType")+"：</SPAN>"+oajobBean.getJobType()+"</LI>";
                html_str += "<LI><SPAN>" +this.getMessage(request, "oa.jobodd.participant")+"：</SPAN>"+participantNames+"</LI>";
                html_str += "<LI><SPAN>" +this.getMessage(request, "oa.mydata.creatTime")+"：</SPAN>"+oajobBean.getCreateTime()+"</LI>";
                html_str += "<LI><SPAN>" +this.getMessage(request, "scope.lb.tsscopeValue")+"：</SPAN>"+oajobBean.getJobBeginTime()+"</LI>";
                html_str += "<LI><SPAN>" +this.getMessage(request, "oa.job.endtime")+"：</SPAN>"+oajobBean.getJobEndTime()+"</LI>";
                html_str += "<LI><SPAN>" +this.getMessage(request, "check.lb.user")+"：</SPAN>"+assessorNames+"</LI>";
                html_str += "<LI><SPAN>" +this.getMessage(request, "oa.jobodd.InterfixUnit")+"：</SPAN>"+IntterfixServerName+"</LI>";
                html_str += "<LI><SPAN>" +this.getMessage(request, "oa.job.ElaborateOn")+"：</SPAN>"+oajobBean.getElaborateOn()+"</LI>";
                html_str += "</UL>\n";
                html_str += "<UL class=listRange_1>\n";
                for(int i=0;i<restoreList.size();i++){
                	OAjobRestoreBean bean=restoreList.get(i);
                	html_str+="<LI2>"+this.getMessage(request, "oa.bbs.peopleofrevertto")+"："+bean.getParticipantPerson()+"&nbsp;&nbsp;&nbsp;&nbsp;"+
                			this.getMessage(request, "oa.bbs.reverttoas")+"："+bean.getRestoreTime();
                	html_str += bean.getParticipantRestore()+"</LI>";
                }
                
                html_str += "</UL>\n";
                html_str += "<UL class=listRange_1>\n";
                for(int i=0;i<auditingList.size();i++){
                	OAJobAuditingBean bean=auditingList.get(i);
                	html_str += "<LI2>" +this.getMessage(request, "check.lb.user")+"："+bean.getAssessor()+"&nbsp;&nbsp;&nbsp;&nbsp;"+
                				this.getMessage(request, "oa.job.approval.time")+"："+bean.getAuditingTime()+"&nbsp;&nbsp;&nbsp;&nbsp;"+
                				this.getMessage(request, "check.lb.status")+"："+
                				(bean.getState().equals("pass")?this.getMessage(request, "check.lb.pass"):this.getMessage(request, "check.lb.notPass"));
                	html_str += bean.getAuditing() +"</LI>";
                }
                html_str += "</UL>\n";
            	html_str+="</tbody></table>";
               
                //加载成功
            } else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
            	 //记录不存在错误
                EchoMessage.error().add(getMessage(
                        request, "common.error.nodata")).setRequest(request);
            } else {
            	 //加载失败
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                        setRequest(request);
            }
        }
        request.setAttribute("html_str", html_str);
        String userId = this.getLoginBean(request).getId();
        Result rs=new EMailMgt().selectAccountByUser(userId);
        MailinfoSettingBean obj =null;
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list=(ArrayList)rs.retVal;
			for(int i=0;i<list.size();i++){
				MailinfoSettingBean a=(MailinfoSettingBean)list.get(i);
				if(a.getDefaultUser().equals("1")){
					obj=a;
					break;
				}
			}
			request.setAttribute("setting", obj);
		}
        
        return getForward(request, mapping, "sendemail");
    }

	/**
	 * 详细信息
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
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String innoback=this.getParameter("noback", request);
		// 获取查查询参数
		String createPerson = request.getParameter("createPerson");
		String createPersonName = request.getParameter("createPersonName");
		String jobtheme = request.getParameter("jobtheme");
		String jobBeginTime = request.getParameter("jobBeginTime");
		String jobEndTime = request.getParameter("jobEndTime");
		if ("GET".equals(request.getMethod())) {
			createPersonName = createPersonName == null ? "" : GlobalsTool
					.toChinseChar(createPersonName);
			jobtheme = jobtheme == null ? "" : GlobalsTool
					.toChinseChar(jobtheme);
		}
		request.setAttribute("createPerson", createPerson);
		request.setAttribute("createPersonName", createPersonName);
		request.setAttribute("jobtheme", jobtheme);
		request.setAttribute("jobBeginTime", jobBeginTime);
		request.setAttribute("jobEndTime", jobEndTime);

		// 获得当前登录用户
		LoginBean loginBean = this.getLoginBean(request);
		String nstr = request.getParameter("keyId");
		String winCurIndex=request.getParameter("winCurIndex");
		request.setAttribute("winCurIndex", winCurIndex);
		String userType = request.getParameter("userType");// 'assessor'
															// 'participant'

		if (nstr != null && nstr.length() != 0) {
			// 执行加载明细
			Result rs = mgt.detail(nstr, OAJobBean.class);
			if(rs.getRetVal()==null){
				EchoMessage.error().add(getMessage(request, "oaJob.not.find"))
		 		   .setNoBackButton()
		 		   .setAlertRequest(request);
		    	return getForward(request, mapping, "message");
			}
			OAJobBean oajobBean = (OAJobBean) rs.retVal;
			
			request.setAttribute("backtype", innoback);
			request.setAttribute("isSaveReading", oajobBean.getIsSaveReading());
			request.setAttribute("userType", userType);
			request.setAttribute("currentUser", loginBean.getId());
			String url = request.getRequestURI() ;
			String favoriteURL = java.net.URLEncoder.encode("/OAJob.do?operation=5&keyId="+nstr+"&winCurIndex="+winCurIndex);
			request.setAttribute("favoriteURL", favoriteURL) ;
			

			// 判断最终的审核是否已审核
			if (oajobBean.getState()!=null && "finish".equals(oajobBean.getState())){
				request.setAttribute("state", "true");
			} else {
				// 判断当前登录者是不是审核人
				String[] auditingNames = oajobBean.getAssessor().split(";");
				for (int i = 0; i < auditingNames.length; i++) {
					if (loginBean.getId().equals(auditingNames[i])) {
						// 是审核人,可以审核
						request.setAttribute("isAuditing", "true");
						break;
					}
				}
				request.setAttribute("state", "flase");
				// 判断是否已审核通过
				Result rsAuditing = mgt.isAuditing(nstr, loginBean.getId());
				OAJobAuditingBean auditingBean = (OAJobAuditingBean) rsAuditing.retVal;
				if (auditingBean.getState() != null) {
					// 已审核通过
					request.setAttribute("rsAuditing", "true");
					request.setAttribute("resultAuditing", rsAuditing.retVal);
				} else {
					// 未审核或审核未通过
					request.setAttribute("rsAuditing", "false");
				}
			}
			
			//判断当前登录者是不是参与者
			String[] restoreNames = oajobBean.getParticipant().split(";");
			for (int i = 0; i < restoreNames.length; i++) {
				if (loginBean.getId().equals(restoreNames[i])) {
					// 是参与者,可以回复
					request.setAttribute("isRestore", "true");
					break;
				}
			}
			
			// 获取所有的参与者的名字
			String participantNames = this.getParticipantNames(oajobBean);
			request.setAttribute("participantNames", participantNames);
			// 获取所有审核者
			String assessorNames = this.getassessorNames(oajobBean);
			request.setAttribute("assessorNames", assessorNames);
			// 获取关联单位
			String IntterfixServerName = mgt
					.getIntterfixServerNameById(oajobBean.getIntterfixServer()).retVal
					.toString();
			request.setAttribute("IntterfixServerName", IntterfixServerName);

			// 加载所有参与者的回复
			Object RestoreList = mgt.getRestore(nstr).getRetVal();
			List<OAjobRestoreBean> restoreList = new ArrayList<OAjobRestoreBean>();
			if (RestoreList != null) {
				restoreList = (ArrayList<OAjobRestoreBean>) RestoreList;
				for (int i = 0; i < restoreList.size(); i++) {
					// 获取参与者的ID
					String ids = restoreList.get(i).getParticipantPerson();
					// ids = ids.substring(0, ids.indexOf(";"));
					// 通过ID查找客户
					Result r = mgt.detail(ids, EmployeeBean.class);
					EmployeeBean eb = (EmployeeBean) r.retVal;
					// 用名字去替换ID
					if(eb!=null){
						restoreList.get(i).setParticipantPerson(eb.getEmpFullName());
					}
				}
			}

			// 加载所有审核者的审核说明
			Object AuditingList = mgt.getAuditing(nstr).getRetVal();
			List<OAJobAuditingBean> auditingList = new ArrayList<OAJobAuditingBean>();
			if (AuditingList != null) {
				auditingList = (ArrayList<OAJobAuditingBean>) AuditingList;

				for (int i = 0; i < auditingList.size(); i++) {
					// 获取参与者的ID
					String ids = auditingList.get(i).getAssessor();
					// ids = ids.substring(0, ids.indexOf(";"));
					// 通过ID查找客户
					Result r = mgt.detail(ids, EmployeeBean.class);
					EmployeeBean eb = (EmployeeBean) r.retVal;
					// 用名字去替换ID
					auditingList.get(i).setAssessor(eb.getEmpFullName());
				}
			}
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 加载成功
				request.setAttribute("result", rs.retVal);
				// 参与者的回复
				request.setAttribute("RestoreList", restoreList);
				// 审核者的审核说明
				request.setAttribute("AuditingList", auditingList);
			} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
				// 记录不存在错误
				EchoMessage.error().add(
						getMessage(request, "common.error.nodata")).setRequest(
						request);
				return getForward(request, mapping, "to_detailjob");
			} else {
				// 加载失败
				EchoMessage.error()
						.add(getMessage(request, "common.msg.error"))
						.setRequest(request);
				return getForward(request, mapping, "to_detailjob");
			}
		}
		return getForward(request, mapping, "to_detailjob");
	}

	/**
	 * 普通用户删除(OAJobodd)
	 * 
	 * @param mapping
	 *            ActionMapping
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
		String userId = this.getLoginBean(request).getId();
		// 获得页面参数-->需要删除的ID
		String jobId[] = request.getParameterValues("keyId");
		String winCurIndex=GlobalsTool.getStringDate(request
				.getAttribute("winCurIndex1"));
		request.setAttribute("winCurIndex", winCurIndex);
		Result rs_jobdele = null;
		// 根据公文发送ID删除公文发送表(OADocumentSend)
		for (int i = 0; i < jobId.length; i++) {
			OAJobBean oajob = (OAJobBean)mgt.getJob(jobId[i]).getRetVal();
			if(!"finish".equals(oajob.getState())){
				rs_jobdele = mgt.delete(jobId[i]);
			}
			if("1".equals(userId) && "finish".equals(oajob.getState())){
				rs_jobdele = mgt.delete(jobId[i]);
			}
			
		}
		if (rs_jobdele.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 删除成功
			request.setAttribute("result", rs_jobdele.retVal);
			
			//删除所有通知
			String ids= "";
			for(String delId : jobId){
				ids += delId + ",";
			}
			amgt.deleteByRelationId(ids, "");	
			
			// 删除后刷新
			EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/OAJob.do?winCurIndex=" + winCurIndex)
				.setRequest(request);
			forward = getForward(request, mapping, "message");
		} else {
			// 删除失败
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
					.setRequest(request);
			forward = getForward(request, mapping, "message");
		}
		return forward;
	}

	/**
	 * 获取所有参与者姓名
	 * 
	 * @param jobBean
	 *            协助单对象
	 * @return 所有参与者
	 */
	protected String getParticipantNames(OAJobBean jobBean) {
		Result rss = null;
		EmployeeBean userBean = null;
		String[] participants = jobBean.getParticipant().split(";");
		String participantNames = "";
		for (int i = 0; i < participants.length; i++) {
			rss = mgt.detail(participants[i], EmployeeBean.class);
			userBean = (EmployeeBean) rss.retVal;
			if(userBean!=null){
				participantNames += userBean.getEmpFullName() + ";";
			}
		}
		return participantNames;
	}

	/**
	 * 获取所有审核者
	 * 
	 * @param jobBean
	 *            协助单对象
	 * @return 所有审核者
	 */
	protected String getassessorNames(OAJobBean jobBean) {
		Result rss = null;
		EmployeeBean userBean = null;
		String[] assessors = jobBean.getAssessor().split(";");
		String assessorNames = "";
		for (int i = 0; i < assessors.length; i++) {
			rss = mgt.detail(assessors[i], EmployeeBean.class);
			userBean = (EmployeeBean) rss.retVal;
			if(userBean!=null){
				assessorNames += userBean.getEmpFullName() + ";";
			}
		}
		return assessorNames;
	}

	/**
	 * 上传文件
	 * 
	 * @param files
	 * @param path
	 * @return
	 */
	public String uploadFile(Hashtable<String, FormFile> files, String path) {
		String filesName = "";// 上传文件的名字，多个文件中间用，分开
		if (files != null && files.size() > 0) {
			Iterator<String> iterFile = files.keySet().iterator();
			File filePath = new File(path);
			if (!filePath.exists()) {
				filePath.mkdir();
			}
			while (iterFile.hasNext()) {
				String key = iterFile.next();
				FormFile fileItem = files.get(key);
				if (fileItem != null && fileItem.getFileName() != null
						&& fileItem.getFileName().length() > 0) {
					// 提取扩展名
					int start = fileItem.getFileName().lastIndexOf("\\");
					String fileName = fileItem.getFileName().substring(
							start + 1);
					int substr_start = fileName.lastIndexOf(".");
					String extendName = fileName.substring(substr_start);
					// 随机给文件取一个名字
					int randomNumber = (int) (new Random().nextDouble() * 1000);
					String outFileName = new java.util.Date().getTime() + ""
							+ randomNumber + "" + extendName;
					filesName += outFileName + ":" + fileName + "|";
					try {
						OutputStream os = new FileOutputStream(new File(path,
								outFileName));
						InputStream is = fileItem.getInputStream();
						byte[] buffer = new byte[1024 * 2];
						int length = 0;
						while ((length = is.read(buffer)) > 0) {
							os.write(buffer, 0, length);
						}
						os.close();
						is.close();
					} catch (Exception e) {
						BaseEnv.log.error("OAWorkPlanAction add File UpLoad"
								+ e);
					}
				}
			}
		}
		return filesName;
	}

	protected ActionForward Adduditing(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("调用审核的方法");
		OAJobAuditingForm auditingform = (OAJobAuditingForm) form;
		OAJobAuditingBean auditing = new OAJobAuditingBean();
		read(auditingform, auditing); // 将Form数据读入到Bean中
		String id = IDGenerater.getId(); // 自动生成一个ID
		auditing.setId(id);
		auditing.setAssessor(this.getLoginBean(request).getId());
		String auditingTime = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		auditing.setAuditingTime(auditingTime);
		Result rs = mgt.addjobAuditing(auditing);
		if (rs.getRetCode() == 1) {
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/OAJob.do?winCurIndex="
							+ getParameter("winCurIndex", request))
					.setAlertRequest(request);
		}
		return getForward(request, mapping, "addjob");
	}

	/**
	 * 审核
	 * 
	 * @param files
	 * @param path
	 * @return
	 */
	protected ActionForward auditing(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 获取查询参数
		String createPerson = request.getParameter("createPerson");
		String createPersonName = request.getParameter("createPersonName");
		String jobtheme = request.getParameter("jobtheme");
		String jobBeginTime = request.getParameter("jobBeginTime");
		String jobEndTime = request.getParameter("jobEndTime");
		//附件
		String accessories = request.getParameter("attachFiles");
		accessories = accessories== null?"":accessories;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && accessories.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/plan/" + del);
				aFile.delete();
			}
		}	

		Result rs_jobadd = new Result();

		// 获取参数，决定是回复还是审核
		String editType = request.getParameter("editType"); // Auditing表示审核,Restore表示回复
		String oajoboddId =  request.getParameter("oajoboddId");//mj提出来
		// 加载协助单 mj提出来
		Result rs = mgt.detail(oajoboddId, OAJobBean.class);
		OAJobBean oajobBean = (OAJobBean) rs.retVal;
		//通知通告title提示信息
		String mes = "";
		if (!editType.equals("") && editType.equals("Auditing")) {
			OAJobAuditingBean auditing = new OAJobAuditingBean();
			auditing.setId(IDGenerater.getId());
			auditing.setAuditingTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			auditing.setAssessor(this.getLoginBean(request).getId());
			auditing.setOajoboddId(oajoboddId);
			auditing.setAuditing(request.getParameter("auditing"));
			auditing.setState(request.getParameter("state"));	
			auditing.setAttaches(accessories);
			
			rs_jobadd = mgt.addjobAuditing(auditing);//添加审核记录
		
			// 根据审核状态决定是否要修改审核状态
			if (!oajobBean.getState().equals("finish")) {
				String[] assessorNames = oajobBean.getAssessor().split(";");
				int countassessor = assessorNames.length;
				// 以下用于统计审核的通过数
				rs = mgt.getPassCount(auditing.getOajoboddId());
				int passCount=0;
				if(rs.getRetVal()!=null){
					passCount=Integer.parseInt(rs.getRetVal().toString());
				}
				rs=mgt.getAuditeCount(auditing.getOajoboddId());
				int auditeCount=0;
				if(rs.getRetVal()!=null){
					auditeCount=Integer.parseInt(rs.getRetVal().toString());
				}
				
				//如果审核人数和审核通过的数目相同，则状态为审核完毕。如果审核人数和审核记录的人数相等但和审核通过的数目不等，则状态为审核不通过。如果审核人数和审核记录人数不等，则状态为审核中
				String state="";
				if(countassessor==passCount){
					state = "finish";
					mes = "oa.job.approved";
				}else if(countassessor==auditeCount&&countassessor!=passCount){
					state = "notPass";
					mes = "oa.job.noApproved";
				}else if(countassessor!=auditeCount){
					state = "approved";
					mes = "oa.job.noApproved";
				}
				// 修改审核状态
				oajobBean.setState(state);
				oajobBean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				mgt.update(oajobBean);
				
			}
		} else {
			// 执行回复操作
			OAjobRestoreBean restore = new OAjobRestoreBean();
			restore.setId(IDGenerater.getId());
			restore.setRestoreTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			restore.setParticipantPerson(this.getLoginBean(request).getId());
			restore.setOajoboddId(oajoboddId);
			String participantRestore = request
					.getParameter("participantRestore");
			restore.setParticipantRestore(participantRestore);
			restore.setAttaches(accessories);
			rs_jobadd = mgt.addjobRestore(restore);
		
			oajobBean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			mgt.update(oajobBean);
			
			mes = "oa.job.reply";
		}
		//mj
		LoginBean loginBean = this.getLoginBean(request);// 获取登录用户
		// 给审核者发送通知提醒
		// 获取审核人ID
		String receive = oajobBean.getCreatePerson();
		String title = loginBean.getEmpFullName() + getMessage(request, mes)
				+ ":" + oajobBean.getJobtheme() + " "
				+ getMessage(request, "oa.job.sure");

		String type = "other";
		if( "oa.job.reply".equals(mes))
			type = "other";
		else if("oa.job.noApproved".equals(mes))
			type = "notApprove";
		else if("oa.job.approved".equals(mes))
			type = "approve";
		
		amgt.add(loginBean.getId(), 
				title, 
				"<a href=\"javascript:mdiwin('/OAJob.do?noback=true&operation=5&userType=assessor&keyId=" + oajoboddId + "','" + getMessage(request, "oa.job.jobDetail") + "')\">" + title + getMessage(request, "com.click.see") + "</a>", 
				receive, 
				oajoboddId, 
				type);
				
		// 删除此条通知消息

		amgt.deleteByRelationId(oajoboddId, loginBean.getId());	
		
		ActionForward forward = getForward(request, mapping, "alert");
		if (rs_jobadd.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.success().add(
					getMessage(request, "common.AuditingSuccess")).setBackUrl(
					"/OAJob.do?winCurIndex="
							+ getParameter("winCurIndex", request)
							+ "&createPerson=" + createPerson
							+ "&createPersonName=" + createPersonName
							+ "&jobtheme=" + jobtheme + "&jobBeginTime="
							+ jobBeginTime + "&jobEndTime=" + jobEndTime + "")
					.setAlertRequest(request);
		} else {
			// 添加失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture"))
					.setAlertRequest(request);
		}
		return forward;
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		
		int operation = getParameterInt("operation", req) ;
		if(operation == OperationConst.OP_CHECK){
			return null ;
		}
		return super.doAuth(req, mapping);
	}
	
	
}
