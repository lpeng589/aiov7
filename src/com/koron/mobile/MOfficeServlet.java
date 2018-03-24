/**
 * ������� �ֻ�ƽ̨
 */
package com.koron.mobile;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import sun.misc.BASE64Decoder;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.koron.crm.client.CRMClientMgt;
import com.koron.crm.setting.ClientSetingMgt;
import com.koron.mobile.bean.AddClientBean;
import com.koron.mobile.bean.Item;
import com.koron.mobile.bean.MobileAttrs;
import com.koron.mobile.bean.MobileCount;
import com.koron.mobile.bean.MobileCountList;
import com.koron.mobile.bean.MobileDetail;
import com.koron.mobile.bean.MobileEmail;
import com.koron.mobile.bean.MobileEmailDate;
import com.koron.mobile.bean.MobileEmailList;
import com.koron.mobile.bean.MobileFlowAction;
import com.koron.mobile.bean.MobileListData;
import com.koron.mobile.bean.MobileMessage;
import com.koron.mobile.bean.MobileMessageBean;
import com.koron.mobile.bean.MobileOnlineUser;
import com.koron.mobile.bean.MobileParam;
import com.koron.mobile.bean.MobileQuery;
import com.koron.mobile.bean.MobileResult;
import com.koron.mobile.bean.MobileWorkFlow;
import com.koron.mobile.bean.SaleFollowUp;
import com.koron.mobile.bean.SalesOutStock;
import com.koron.oa.OACalendar.OACalendaMgt;
import com.koron.oa.OACalendar.OACalendarAction;
import com.koron.oa.OACalendar.OACalendarBean;
import com.koron.oa.bbs.forum.OABBSForumMgt;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.MailinfoSettingBean;
import com.koron.oa.bean.OAAdviceBean;
import com.koron.oa.bean.OABBSForumBean;
import com.koron.oa.bean.OABBSReplayForumBean;
import com.koron.oa.bean.OABBSTopicBean;
import com.koron.oa.bean.OABBSUserBean;
import com.koron.oa.bean.OADayWorkPlanBean;
import com.koron.oa.bean.OAKnowledgeCenterFile;
import com.koron.oa.bean.OAMailInfoBean;
import com.koron.oa.bean.OATaskBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.discuss.DiscussAction;
import com.koron.oa.individual.workPlan.OADateWorkPlanMgt;
import com.koron.oa.message.MessageMgt;
import com.koron.oa.oaTask.OATaskAction;
import com.koron.oa.oaTask.OATaskMgt;
import com.koron.oa.publicMsg.knowledgeCenter.OAKnowCenterMgt;
import com.koron.oa.publicMsg.newadvice.OAAdviceMgt;
import com.koron.oa.toDo.OAToDoBean;
import com.koron.oa.toDo.ToDoAction;
import com.koron.oa.toDo.ToDoMgt;
import com.koron.oa.util.AttentionMgt;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.FieldScopeSetBean;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.RoleBean;
import com.menyi.aio.bean.RoleModuleBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.importData.BSFExec;
import com.menyi.aio.web.importData.ExcelFieldInfoBean;
import com.menyi.aio.web.login.LoginAction;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.menu.MenuManageAction;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.email.BackgroundThread;
import com.menyi.email.EMailMgt;
import com.menyi.email.util.AIOEMail;
import com.menyi.email.util.EMailMessage;
import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.ErrorMessage;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.ReportField;
import com.menyi.web.util.SystemState;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
/**
 * <p>Title:�ֻ������ ��������</p> 
 * <p>Description: ���ظ��ֻ��˵�����ȫ��������JSON��ʽ������</p>
 *
 * @Date:Feb 3, 2012
 * @Copyright: �����п���������޹�˾
 * @Author ǮС��
 */
public class MOfficeServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static Gson gson ;
	private static MOfficeMgt mgt ;
	private static MessageMgt msg;
	EMailMgt emailmgt=new EMailMgt();
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
		mgt = new MOfficeMgt() ;
		msg = new MessageMgt();
	}
	
	/**
	 * �ֻ������нӿڵ���ڣ����ݲ�ͬ��op���ò�ͬ�Ĳ�������
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		debugRequest(request);
		String userId = request.getParameter("sid");
		String operation = request.getParameter("op");
		if (!"d".equals(operation) && checkMobileID(request, response) == false) {
			return;
		}
		
//		/*����û����������û��ڣ��Ѵ��û��߳�ȥ����¼Ȩ����֤����*/
//		OnlineUser online = OnlineUserInfo.getUser(userId) ;
//		if(!"auth".equals(operation) && online!=null){
//			if (online.isMobileOnline()){
//                MobileResult result = new MobileResult() ;
//                result.setCode(-1) ;
//                result.setDesc("��ʱ�������µ�¼") ;
//                writeJson(result,response) ;
//                return ;
//			}
//		}
		/*�ڴ˸����û���������Ϣ*/
		if (userId != null && userId.length() > 0) {
			OnlineUserInfo.refresh(userId, "mobile");
		}
		try {
			if (null == operation || "".equals(operation)) {	// ��������������������
				writeJson("OK", response);
			} else if ("d".equals(operation)) {
				download(request, response);
			} else if ("auth".equals(operation)) { // ��¼
				doAuth(request, response);
			} else if ("online".equals(operation)) { // ��ѯδ����֪ͨ����˽�������ʼ���
				online(request, response);
			} else if ("listTips".equals(operation)) { // ϵͳ��Ϣ
				listTips(request, response);
			} else if ("getInfo".equals(operation)) { // ����ϵͳ������ϸ��Ϣ
				getInfo(request, response);
			} else if ("listWorkflows".equals(operation)) {
				listWorkflows(request, response);
			} else if ("getWorkflow".equals(operation)) {
				getWorkflow(request, response);
			} else if ("approvalWorkflow".equals(operation)) {
				approvalWorkflow(request, response);
			} else if ("cancelWorkflow".equals(operation)) {
				cancelWorkflow(request, response);
			} else if ("listMyModules".equals(operation)) {
				listMyModules(request, response);
			} else if ("listBusinessModules".equals(operation)) {
				listBusinessModules(request, response);
			} else if ("doListview".equals(operation)) {
				doListview(request, response);
			} else if ("doQueryview".equals(operation)) {
				doQueryview(request, response);
			} else if ("doDetailsview".equals(operation)) {
				doDetailsview(request, response);
			} else if ("doQueryResultview".equals(operation)) {
				doListview(request, response);
			} else if ("doDetailsview".equals(operation)) {
				doDetailsview(request, response);
			} else if ("listAllUsers".equals(operation)) { // ��ȡȫ���û�
				listUsers(request, response);
			} else if ("listOnlineUsers".equals(operation)) {
				listOnlineUsers(request, response);
			} else if ("listMessageBox".equals(operation)) {
				listMessageBox(request, response);
			} else if ("sendMessage".equals(operation)) {
				sendMessage(request, response);
			} else if ("listMessages".equals(operation)) {
				listMessages(request, response);
			} else if ("listMail".equals(operation)) {
				listMail(request, response);
			} else if ("receiveMail".equals(operation)) {
				receiveMail(request, response);
			} else if ("getMail".equals(operation)) {
				getMail(request, response);
			} else if ("sendMail".equals(operation)) {
				sendMail(request, response);
			} else if ("listAddress".equals(operation)) {
				listAddress(request, response);
			} else if ("listMailBox".equals(operation)) {
				listMailBox(request, response);
			} else if ("getUserInfo".equals(operation)) {
				getUserInfo(request, response);
			} else if ("updateFlag".equals(operation)) {
				updateFlag(request, response);
			} else if ("doQueryKnowledge".equals(operation)) {
				doQueryKnowledge(request, response);
			} else if ("employeeGoal".equals(operation)) {
				employeeGoal(request, response);
			} else if ("syncInfo".equals(operation)) { // ͬ����Ʒ��Ϣ
				syncInfo(request, response);
			} else if ("saveValue".equals(operation)) { // ������۳��ⵥ
				saveValue(request, response);
			} else if ("getPrice".equals(operation)) {
				getPrice(request, response);
				// } else if ("getMsg".equals(operation)) {
				// getMsg(request, response);
				// } else if ("msgStatus".equals(operation)) {
				// msgStatus(request, response);
			} else if ("getMessage".equals(operation)) { // Adroid��ѭ����Ϣ
				getMessage(request, response);
			} else if ("selectClient".equals(operation)) { // ���۸���-ѡ��ͻ��������ƻ�-�����ͻ�
				selectClient(request, response);
			} else if ("addSaleBill".equals(operation)) { // ������۸���
				addSaleBill(request, response);
			} else if ("detail".equals(operation)) { // ֪ͨͨ�����ϸ��Ϣ
				showDetail(request, response);
			} else if ("forum".equals(operation)) { // ��ѯ���ӵ���ϸ��Ϣ
				showForum(request, response);
			} else if ("addForum".equals(operation)) { // ����
				addForum(request, response);
			} else if ("replayForum".equals(operation)) { // ����
				replayForum(request, response);
			} else if ("bbsType".equals(operation)) { // ��ʾ��̳���
				showBBSType(request, response);
			} else if ("bbsClass".equals(operation)) { // ��ʾ��̳����
				showBBSClass(request, response);
			} else if ("clientModule".equals(operation)) { // �ͻ�ģ��
				getModule(request, response);
			} else if ("clientDet".equals(operation)) { // �ͻ��б�
				getCRMClientDet(request, response);
			} else if ("ModuleClient".equals(operation)) { // ָ��ģ���µĿͻ�
				getCRMClientInModule(request, response);
			} else if ("clientInfo".equals(operation)) { // �ͻ�����
				getCRMClientInfo(request, response);
			} else if ("addClient".equals(operation)) { // ��ӿͻ�
				addClient(request, response);
			} else if ("showWorkPlanList".equals(operation)) { // �����ƻ�-�鿴�б�
				showWorkPlanList(request, response);
			} else if ("showWorkPlan".equals(operation)) { // �����ƻ�-�鿴����
				showWorkPlan(request, response);
			} else if ("selectKnowPerson".equals(operation)) { // �����ƻ�-֪����
				selectKnowPerson(request, response);
			} else if ("selectLinkPerson".equals(operation)) { // �����ƻ�-��ϵ��
				selectLinkPerson(request, response);
			} else if ("addUpdateWorkPlan".equals(operation)) { // �����ƻ�-���/�޸�
				addUpdateWorkPlan(request, response);
			} else if ("delWorkPlan".equals(operation)) { // �����ƻ�-ɾ��
				delWorkPlan(request, response);
			} else if ("summaryWorkPlan".equals(operation)) { // �����ƻ�-�ܽ�
				summaryWorkPlan(request, response);
			} else if ("commitWorkPlan".equals(operation)) { // �����ƻ�-����
				commitWorkPlan(request, response);
			} else if ("syncContact".equals(operation)) { // ͬ��ͨѶ¼
				syncContact(request, response);
			} else if ("androidVersion".equals(operation)) { // ��׿�汾
				getAndroidVersion(request, response);
			} else if ("selectGoods".equals(operation)) { // ѡ����Ʒ
				selectGoods(request, response);
			} else if ("selectStock".equals(operation)) { // ѡ��ֿ�
				selectStock(request, response);
			} else if ("storeDetail".equals(operation)) { // ���
				getStoreDetail(request, response);
			} else if ("clientTransfer".equals(operation)) { // �ͻ��ƽ�
				getCRMClientTransfer(request, response);
			} else if ("handleClientTransfer".equals(operation)) { // ����ͻ��ƽ�
				handleClientTransfer(request, response);
			} else if ("storeCheck".equals(operation)) { // �̵�
				storeCheck(request, response);
			} else if ("chatUser".equals(operation)) { // ��ȡȫ���û�
				listChatUser(request, response);
			} else if ("chatGroup".equals(operation)) { // ��ȡ����Ⱥ��
				listDeptAndGroup(request, response);
			} else if ("chatOnline".equals(operation)) { // ��ѯ�����û�
				listChatOnline(request, response);
			} else if ("chatHis".equals(operation)) { // �����ϵ��
				listChatCount(request, response);
			} else if ("receveChatMsg".equals(operation)) { // ������Ϣ
				receveChatMsg(request, response);
			} else if ("sendChatMsg".equals(operation)) { // ������Ϣ
				sendChatMsg(request, response);
			} else if ("listMyApp".equals(operation)) { // ��ȡ����ģ��
				listMyApp(request, response);
			} else if ("syncGoods".equals(operation)) { // ͬ����Ʒ��Ϣ
				syncSalesErpInfo(request, response);
			} else if ("syncSalesErp".equals(operation)) { // ͬ�����۹��������Ϣ
				syncSalesErpInfo(request, response);
			} else if ("SalesReturnStock".equals(operation)) { // �����˻�
				salesReturnStock(request, response);
			} else if ("SalesOrder".equals(operation)) { // ���۶���
				salesOrder(request, response);
			} else if ("SignIn".equals(operation)) { // �ƶ�ǩ��
				signIn(request, response);
			} else if ("listMyTodo".equals(operation)) { // ��ʾ����
				listMyTodo(request, response);
			} else if ("addUpdateMyTodo".equals(operation)) { // ���/�޸Ĵ���
				addUpdateMyTodo(request, response);
			} else if ("delMyTodo".equals(operation)) { // ɾ������
				delMyTodo(request, response);
			} else if ("listMySchedule".equals(operation)) { // ��ʾ�ճ�
				listMySchedule(request, response);
			} else if ("addUpdateMySchedule".equals(operation)) { // ���/�޸��ճ�
				addUpdateMySchedule(request, response);
			} else if ("delMySchedule".equals(operation)) { // ɾ���ճ�
				delMySchedule(request, response);
			} else if ("getMyItems".equals(operation)) { // ��ʾ������Ŀ
				getMyItems(request, response);
			} else if ("listMyItems".equals(operation)) { // ��ʾ��Ŀ�б�
				listMyItems(request, response);
			} else if ("listMyItemsLog".equals(operation)) { // ��ʾ��Ŀ��̬
				listMyItemsLog(request, response);
			} else if ("listMyItemsTask".equals(operation)) { // ��ʾ��Ŀ������
				listMyItemsTask(request, response);
			} else if ("addItemsLog".equals(operation)) { // �����Ŀ��̬
				addItemsLog(request, response);
			} else if ("addUpdateTask".equals(operation)) { // ���/�޸���Ŀ������
				addUpdateTask(request, response);
			} else if ("getMyTask".equals(operation)) { // ��ʾ��������
				getMyTask(request, response);
			} else if ("listMyTask".equals(operation)) { // ��ʾ�����б�
				listMyTask(request, response);
			} else if ("listMyTaskLog".equals(operation)) { // ��ʾ����̬
				listMyTaskLog(request, response);
			} else if ("listMyTaskSonTask".equals(operation)) { // ��ʾ������
				listMyTaskSonTask(request, response);
			} else if ("addTaskLog".equals(operation)) { // �������̬
				addTaskLog(request, response);
			} else if ("listWorkLog".equals(operation)) { // ��ʾ�ҵ���־
				listWorkLog(request, response);
			} else if ("getWorkLogDiscuss".equals(operation)) { // ��־����
				getWorkLogDiscuss(request, response);
			} else if ("addWorkLogDiscuss".equals(operation)) { // �����־����
				addWorkLogDiscuss(request, response);
			} else if ("addUpdateWorkLog".equals(operation)) { // ���/�޸���־
				addUpdateWorkLog(request, response);
			} else if ("getFollowIds".equals(operation)) { // ������� �Ŷ���־ʹ��
				getFollowIds(request, response);
			} else if ("warmToWriteLog".equals(operation)) { // ����������д��־
				warmToWriteLog(request, response);
			} else {
				MobileResult result = new MobileResult();
				result.setCode(-1);
				result.setDesc("δ֪�Ĳ���");
				writeJson(result, response);
			}
		} catch (Exception e) {
			BaseEnv.log.error("moffice--------", e);
		}
	}
	
	private void saveData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String tableName = request.getParameter("tableName");
		String values = request.getParameter("values");
		String parentCode = request.getParameter("parentCode");
		
		HashMap saveValues = gson.fromJson(values,HashMap.class);
		
		
		MobileResult result = new MobileResult();
		
		LoginBean loginBean = this.getLoginBean(request);
		Locale locale = getLocale(request);
		String addMessage = GlobalsTool.getMessage(locale, "common.lb.add");
		DBTableInfoBean tBean = BaseEnv.tableInfos.get(tableName);
		
		//����Ĭ��ֵ
		UserFunctionMgt userMgt = new UserFunctionMgt();
		DynDBManager mgt = new DynDBManager();
		try {							
			userMgt.setDefault(tBean, saveValues, loginBean.getId());
			//��ϸ������Ĭ��ֵ
			ArrayList<DBTableInfoBean> ct = DDLOperation.getChildTables(tBean.getTableName(), BaseEnv.tableInfos);
			for (int j = 0; ct != null && j < ct.size(); j++) {
				DBTableInfoBean cbean = ct.get(j);
				ArrayList clist = (ArrayList) saveValues.get("TABLENAME_" + cbean.getTableName());
				for (int k = 0; clist != null && k < clist.size(); k++) {
					HashMap cmap = (HashMap) clist.get(k);
					userMgt.setDefault(cbean, cmap, loginBean.getId());
				}
			}
		} catch (Exception e) {
			result.setCode(-1);
			result.setDesc("Ĭ��ֵ���ô���");
			writeJson(result, response);
			return;
		}		

		/*��֤�ǿ�-----���ﲻ����Ҫ����֤�Ĺ�������Ϊ��add�������и�ȫ��Ľ�����*/ 
		/********************************************
		 ִ����ؽӿ�  saveValues Ϊ�������ݵ�HashMap ����˲�������Ӧ�ӿ�������ݲ���
		 ********************************************/
		ClientModuleViewBean moduelViewbean = new ClientModuleViewBean(); //CRM�ͻ�ģ��
		saveValues.put("ModuleId", moduelViewbean.getModuleId());
		String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
		Hashtable props = (Hashtable)request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MOperation mop = GlobalsTool.getMOperationMap(request);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		Result rs;
		try {
			rs = userMgt.add(tBean, saveValues, loginBean, parentCode, BaseEnv.tableInfos, path, "", locale, addMessage, resources, props, mop, "10");
		} catch (Exception e) {
			result.setCode(-1);
			result.setDesc(GlobalsTool.getMessage(locale, "common.msg.error"));
			writeJson(result, response);
			return;			
		}
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			result.setCode(-1);
			result.setDesc(GlobalsTool.getMessage(locale, "common.msg.ok"));
			writeJson(result, response);
			return;
		} else {
			SaveErrorObj saveErrrorObj = mgt.saveError(rs, getLocale(request).toString(), "");
			result.setDesc(saveErrrorObj.getMsg());
			writeJson(result, response);			
		}
	}
	
	
	protected boolean checkMobileID(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("sid");
		String username = request.getParameter("username") ;
		String operation = request.getParameter("op") ;
		Result result = null;
		if ("auth".equals(operation)) {
			result = mgt.queryEmployeeByEmpName(username);			
		} else {
			result = mgt.queryEmployeeById(userId);
		}		
		if (result != null && result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			EmployeeBean employee = (EmployeeBean) result.retVal;
			if (employee.getCheckMobile() > 0) {
				String mobileId = request.getParameter("mobileID");
				if (mobileId != null && mobileId.length() > 0) {
					if ("".equals(employee.getMobileID())) {
						employee.setMobileID(mobileId);
						UserMgt umgt = new UserMgt();
						umgt.update(employee);
					}
					if (employee.getMobileID().indexOf(mobileId) != -1) {
						return true;
					} else {
						MobileResult rs = new MobileResult();
						rs.setCode(-401);
						rs.setDesc("���û��Ѿ��󶨱���ֻ�");
						writeJson(rs, response);
						return false;						
					}
				}
			} else {
				return true;
			}
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�����ڸ��û�");
			writeJson(rs, response);
			return false;
		}
		return true;
	}
	
	protected void debugRequest(HttpServletRequest request){
		if(BaseEnv.log.isDebugEnabled()){
			String uName = "";
			String userId = request.getParameter("sid");
			OnlineUser online = OnlineUserInfo.getUser(userId) ;
			if(online != null){
				uName = online.empNumber;
			}
			BaseEnv.log.debug("MOfficeServlet.service �����ַ��"+request.getRequestURI()+(request.getQueryString()==null?"":"?"+request.getQueryString()));
			String rd ="";
			for(Object key:request.getParameterMap().keySet()){
				Object value=request.getParameterMap().get(key);
				String values="";
				for(String v:(String[])value){
					values +=v+",";
				}
				rd +=key+"=["+values+"];";
			}
			
			BaseEnv.log.debug("MOfficeServlet.service ������:"+uName+";"+"�������ݣ�"+rd);
		}
	}
	
	protected boolean checkParameter(final String params,
			final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String notExistParam = "";
		String[] sP = params.replace(" ", "").split(",");
		for (int i = 0; i < sP.length; i++) {
			if (request.getParameter(sP[i]) == null) {
				notExistParam += sP[i] + ",";
			}
		}
		if (notExistParam.length() > 0) {
			notExistParam = notExistParam.substring(0, notExistParam
					.lastIndexOf(","));
		}

		if (notExistParam.length() > 0) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("ȱ�ٲ������²�����" + notExistParam);
			writeJson(rs, response);
			return false;
		}
		return true;
	}

	protected boolean checkParameter(final String params, final String name, HashMap map,
			final HttpServletResponse response) throws ServletException,
			IOException {
		String notExistParam = "";
		String[] sP = params.replace(" ", "").split(",");
		for (int i = 0; i < sP.length; i++) {
			if (map.get(sP[i]) == null) {
				notExistParam += sP[i] + ",";
			}
		}
		if (notExistParam.length() > 0) {
			notExistParam = notExistParam.substring(0,
					notExistParam.lastIndexOf(","));
		}

		if (notExistParam.length() > 0) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc(name + "ȱ�ٲ������²�����" + notExistParam);
			writeJson(rs, response);
			return false;
		}
		return true;
	}
	
	/**
	 * ��java����ת���ɵ�json����, ������ֻ���
	 * 
	 * @param obj
	 * @param response
	 * @throws IOException
	 */
	private void writeJson(Object obj, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(obj);
		out.println(json);
	    BaseEnv.log.debug("MOfficeServlet---------------response-------------------------------");
		BaseEnv.log.debug(json.substring(0, json.length() > 1000 ? 1000 : json.length()));
		out.flush();
		out.close();
	}

	private void writeJson(JsonObject json, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String str = json.toString();
		out.println(str);
	    BaseEnv.log.debug("MOfficeServlet---------------response-------------------------------");
	    BaseEnv.log.debug(str.substring(0, str.length() > 1000 ? 1000 : str.length()));
		out.flush();
		out.close();
	}

	private void writeJson(String str, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(str);
		BaseEnv.log.debug("MOfficeServlet---------------response-------------------------------");
		BaseEnv.log.debug(str.substring(0, str.length() > 1000 ? 1000 : str.length()));
		out.flush();
		out.close();
	}

	/**
	 * ��ȡ�Զ��������Ϣ
	 * @param request
	 * @param rs
	 * @return
	 */
	public String getDefineErrMsg(final HttpServletRequest request,
			final Result rs) {

		String msg = "";

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return msg;
		}
		
		// �Զ���sql��䶨�Ʒ��ؽ��
		
		if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			String[] str = (String[]) rs.getRetVal();
			msg = getDefSQLMsg(request, str[0]);
		} else if (rs.getRetCode() == ErrorCanst.RET_FIELD_VALIDATOR_ERROR) {
			// �ֶ�У�����
			msg = (String) rs.getRetVal();
		} else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
			// �Զ���������Ҫ�û�ȷ��
			ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
			msg = getDefSQLMsg(request, confirm.getMsgContent());
		} else if (rs.getRetCode() == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
			// ����¼����ǰ����
			msg = "����¼����ǰ����";
		}  else if (rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR) {
			// �洢���̷��ظ�������
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			String[] error = errorMessage.split(";");
			if (error.length == 1) {
				msg = getMessageResources2(request, "common.error.negative2",
						error[0]);
			} else {
				msg = getMessageResources3(request, "common.error.negative",
						error[0], error[1]);
			}
		} else if (rs.getRetCode() == ErrorCanst.RET_BILL_HASCERTIFICATE) {
			msg = getMessageResources2(request, "common.hasCertificate.error",
					rs.retVal.toString());
		} else if (rs.getRetCode() == ErrorCanst.WORK_FLOW_NO_NEXT_NODE) {
			msg = "�Ҳ�����һ����������˽�㣬������������Ƿ���ȷ��";
		} else if (rs.getRetCode() == ErrorCanst.BILL_ADD_WORK_FLOW_ERROR) {
			msg = getMessageResources(request, "com.add.workfow.error");
		} else if (rs.getRetCode() == 2601) {
			msg = getMessageResources(request, "common.error.laterExec");
		} else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SENTENCE_ERROR) {
			Object o = rs.getRetVal();
			if (o != null) {
				msg = o.toString();
			} else {
				ErrorMessage em = new ErrorMessage();
				msg = em.toString(rs.retCode, getLocale(request).toString());
			}
		} else {
			ErrorMessage em = new ErrorMessage();
			msg = em.toString(rs.retCode, getLocale(request).toString());
		}
		return msg;
	}

	/**
	 * ��ȡ��Դ�ļ�
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public String getMessageResources(HttpServletRequest request, String key) {
		String value = "";
		try{
			Object o = request.getSession().getServletContext().getAttribute("userResource") ;
			if (o instanceof MessageResources) {
				MessageResources resources = (MessageResources) o;
				value = resources.getMessage(this.getLocale(request), key);
			}
			if(null == value || "".equals(value)){
				o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				if (o instanceof MessageResources) {
					MessageResources resources = (MessageResources) o;
					value = resources.getMessage(this.getLocale(request), key);
				}
			}
		}catch(Exception e){}
		return value;
	}
	
	public String getMessageResources2(HttpServletRequest request, String key, String param1) {
		String value = "";
		try{
			Object o = request.getSession().getServletContext().getAttribute("userResource") ;
			if (o instanceof MessageResources) {
				MessageResources resources = (MessageResources) o;
				value = resources.getMessage(getLocale(request), key, param1);
			}
			if(null == value || "".equals(value)){
				o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				if (o instanceof MessageResources) {
					MessageResources resources = (MessageResources) o;
					value = resources.getMessage(this.getLocale(request), key);
				}
			}
		}catch(Exception e){}
		return value;
	}
	
	public String getMessageResources3(HttpServletRequest request, String key, String param1,
			String param2)
	{
		String value = "";
		try{
			Object o = request.getSession().getServletContext().getAttribute("userResource") ;
			if (o instanceof MessageResources) {
				MessageResources resources = (MessageResources) o;
				value = resources.getMessage(getLocale(request), key, param1, param2);
			}
			if(null == value || "".equals(value)){
				o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				if (o instanceof MessageResources) {
					MessageResources resources = (MessageResources) o;
					value = resources.getMessage(this.getLocale(request), key);
				}
			}
		}catch(Exception e){}
		return value;
	}
	
	public String getMessageResources4(HttpServletRequest request, String key, String param1,
			String param2, String param3)
	{
		String value = "";
		try{
			Object o = request.getSession().getServletContext().getAttribute("userResource") ;
			if (o instanceof MessageResources) {
				MessageResources resources = (MessageResources) o;
				value = resources.getMessage(getLocale(request), key, param1, param2, param3);
			}
			if(null == value || "".equals(value)){
				o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				if (o instanceof MessageResources) {
					MessageResources resources = (MessageResources) o;
					value = resources.getMessage(this.getLocale(request), key);
				}
			}
		}catch(Exception e){}
		return value;
	}

	
	private Locale getLocale(HttpServletRequest req) {
		Locale loc = null;
		Object obj = req.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (obj != null) {
			loc = (Locale) obj;
			return loc;
		}
		if (loc == null) {
			loc = req.getLocale();
		}
		if (loc == null) {
			loc = loc.getDefault();
		}
		return loc;
	}
	
	/**
	 * Adroid��ѭ����Ϣ
	 * @param request
	 * @param response
	 */
	public void getMessage(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid") ;
		Result result = mgt.getMessage(userId) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			response.setContentType("text/plain; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(result.retVal) ;
			out.flush();
			out.close();
		}
	}
	/**
	 * ֪ͨͨ�����ϸ��Ϣ
	 * @param request
	 * @param response
	 */
	public void showDetail(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String type = request.getParameter("type") ;
		String adviceId = request.getParameter("refid");
		String json = "";
		if("advice".equals(type)){//��ȡ֪ͨͨ������
			Result result = new OAAdviceMgt().loadAdvice(adviceId);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null){
				OAAdviceBean advice = (OAAdviceBean)result.retVal ;
				advice.setCreateBy(OnlineUserInfo.getUser(advice.getCreateBy()).getName());
				json = gson.toJson(advice);
			}
		}
		writeJson(json, response);
	}
	
	
	/**
	 * ��ʾ��̳����
	 * @param request
	 * @param response
	 */
	public void showBBSClass(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		List list = GlobalsTool.getEnumerationItems("OABBSList","zh_CN");
		writeJson(list, response) ;
	}
	
	
	/**
	 * ��ʾ��̳���
	 * @param request
	 * @param response
	 */
	public void showBBSType(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String userId = request.getParameter("sid");
		
		OABBSForumMgt forumMgt = new OABBSForumMgt();
		OABBSUserBean userBean = (OABBSUserBean)((ArrayList)forumMgt.loadBBSUser(userId).retVal).get(0);
		OnlineUser lineuser = OnlineUserInfo.getUser(userId);
		Result result = forumMgt.queryTopic(userBean.getId(),userId,lineuser.getDeptId()) ;
		ArrayList<String[]> topicList = new ArrayList<String[]>();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			topicList = (ArrayList<String[]>) result.retVal ;
		}
		writeJson(topicList, response) ;
	}
	
	
	
	/**
	 * ����
	 * @param request
	 * @param response
	 */
	public void replayForum(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String forumId = request.getParameter("refid");
		String topicId = request.getParameter("topicId");
		String userId  = request.getParameter("sid");
		String content = request.getParameter("content");
		
		OABBSForumMgt forumMgt = new OABBSForumMgt();
		OABBSReplayForumBean replayBean = new OABBSReplayForumBean() ;
		replayBean.setId(IDGenerater.getId()) ;
		replayBean.setContent(content) ;
		/*�����û�*/
		replayBean.setBbsUser((OABBSUserBean)((ArrayList)forumMgt.loadBBSUser(userId).retVal).get(0)) ;
		replayBean.setSendId(forumId) ;
		replayBean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		replayBean.setCreateBy(getLoginBean(request).getId()) ;
		String[] message = new String[2] ;
		message[0] = getMessageResources(request, "bbs.msg.newreplay") ;
		message[1] = getMessageResources(request, "bbs.msg.newreplay2") ;
		
		Result result2 = forumMgt.replayForum(replayBean, topicId, message,"") ;
		MobileResult result = new MobileResult() ;
		if(result2.retCode == ErrorCanst.DEFAULT_SUCCESS){
			result.setCode(1);
			result.setDesc("�����ɹ�");
		}else{
			result.setCode(0);
			result.setDesc("����ʧ��");
		}
		writeJson(result, response) ;
	}
	
	/**
	 * ����
	 * @param request
	 * @param response
	 */
	public void addForum(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String topicId = request.getParameter("topicId");
		String userId  = request.getParameter("sid");
		String content = request.getParameter("content");
		
		HashMap contentMap = gson.fromJson(content, HashMap.class);
		OABBSForumMgt forumMgt = new OABBSForumMgt();
		OABBSTopicBean topic = (OABBSTopicBean) forumMgt.loadTopic(topicId).retVal ;
		OABBSForumBean forumBean = new OABBSForumBean() ;
		forumBean.setId(IDGenerater.getId()) ;
		forumBean.setTopic(topic) ;
		forumBean.setForumType("topic") ;
		/*�����û�*/
		OABBSUserBean bbsUser = (OABBSUserBean)((ArrayList)forumMgt.loadBBSUser(userId).retVal).get(0) ;
		forumBean.setBbsUser(bbsUser) ;
		forumBean.setTopicContent(String.valueOf(contentMap.get("content")));
		ArrayList<String> imgArray = (ArrayList<String>) contentMap.get("photo") ;
		if(imgArray!=null && imgArray.size()>0){
			String path = request.getSession().getServletContext().getRealPath("upload") ;
			for(String strImage : imgArray){
				String strDay = String.valueOf(System.currentTimeMillis() + new Random().nextInt(100));
				BASE64Decoder decoder = new BASE64Decoder() ;
				try { 
					byte[] byteImage = decoder.decodeBuffer(strImage); 
					for(int i=0;i<byteImage.length;++i){
						if(byteImage[i]<0){
							byteImage[i]+=256;
						}
					}
					String imgFilePath = path+"/" + strDay +".jpg" ; 
					File imgFile = new File(imgFilePath) ;
					if(!imgFile.getParentFile().exists()){
						imgFile.getParentFile().mkdirs() ;
					}
					OutputStream out = new FileOutputStream(imgFilePath);
					out.write(byteImage); 
					out.flush(); 
					out.close(); 
					String imageName = strDay+".jpg" ;
					forumBean.setTopicContent(forumBean.getTopicContent()+"<br><img src='/upload/"+imageName+"'/>");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(String.valueOf(contentMap.get("address")).length()>0){
			forumBean.setTopicContent(forumBean.getTopicContent()+"<br/>����λ�ã�"+contentMap.get("address"));
		}
		forumBean.setTopicName(String.valueOf(contentMap.get("title")));
		forumBean.setCreateBy(getLoginBean(request).getId()) ;
		forumBean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		forumBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		Result result2 = forumMgt.addForum(forumBean) ;
		
		MobileResult result = new MobileResult() ;
		if(result2.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<String[]> list =(ArrayList<String[]>)new AttentionMgt().queryAttention(topicId,"OABBSTopic").getRetVal();			
			String refreshIds= "";
			for(int i = 0;i<list.size();i++){
				String content2="<a href=\"javascript:mdiwin('/OABBSForumAction.do?operation="+OperationConst.OP_SEND_PREPARE+"&forumId="+forumBean.getId()+"&topicId="+topicId+
							"','"+getMessageResources(request,"oa.bbs.innerBBS")+"')\">"+getMessageResources2(request,"oa.bbs.Attention",list.get(i)[2])+"</a>";
				NotifyFashion notifyFashion = new NotifyFashion(list.get(i)[0], getMessageResources(request,"oa.bbs.innerBBS"), content2, list.get(i)[0], 4, "OA", "attention");
				notifyFashion.sendAdvice(list.get(i)[0], getMessageResources(request,"oa.bbs.innerBBS"), content,list.get(i)[0], forumBean.getId(), "bbs");
				refreshIds += "'"+list.get(i)[1]+"',";
			}
			if(refreshIds.length() > 0){
				refreshIds = refreshIds.substring(0,refreshIds.length() -1);
				new AttentionMgt().refreshAttention(refreshIds);
			}
			result.setCode(1);
			result.setDesc(forumBean.getId());
		}else{
			result.setCode(0);
			result.setDesc("����ʧ��");
		}
		writeJson(result, response) ;
	}
	
	
	/**
	 * ��ѯ���ӵ���ϸ��Ϣ
	 * @param request
	 * @param response
	 */
	public void showForum(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String forumId = request.getParameter("refid");
		
		String json = "";
		OABBSForumMgt bbsMgt = new OABBSForumMgt();
		Result result = bbsMgt.loadForum(forumId) ;	
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			OABBSForumBean forumBean = (OABBSForumBean) result.retVal;
			String strHttp = "http://"+request.getHeader("host");
			forumBean.setTopicContent(forumBean.getTopicContent().replaceAll("src=\"/upload/", "src=\""+strHttp+"/upload/"));
			result = bbsMgt.queryReplayForum(forumId, 1000, 1,"","") ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				forumBean.setReplayList((List<OABBSReplayForumBean>)result.retVal);
			}
			json = gson.toJson(forumBean);
		}

		writeJson(json, response);
	}
	
	
	/**
	 * ������۸���
	 * @param request
	 * @param response
	 */
	public void addSaleBill(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// ���ﲻУ��Ȩ���������Զ���ִ��ʱ��У��
		
		String userId = request.getParameter("sid") ;
		String json = request.getParameter("json") ;
//		json = "{\"content\":\"1\",\"topic\":\"��\",\"addPlan\":true,\"location\":\"�㶫ʡ�������޺�������·68\",\"nextVisitTime\":\"2013-10-17\",\"locImage\":[],\"clientId\":\"2e51762e_1112101305004761700\"}";
		SaleFollowUp sales = gson.fromJson(json, SaleFollowUp.class) ;
		if (null == sales.getTopic() || "".equals(sales.getTopic())) {
			sales.setTopic(sales.getContent());
		}
		MobileResult result = new MobileResult() ;
		LoginBean login = getLoginBean(request);
		HashMap values = new HashMap() ;
		try{
			if(sales.getLocImage()!=null && sales.getLocImage().length>0){
				String imageName = "" ;
				for(String strImage : sales.getLocImage()){
					String strDay = String.valueOf(System.currentTimeMillis() + new Random().nextInt(100));
					BASE64Decoder decoder = new BASE64Decoder() ;
					try { 
						byte[] byteImage = decoder.decodeBuffer(strImage); 
						for(int i=0;i<byteImage.length;++i){
							if(byteImage[i]<0){
								byteImage[i]+=256;
							}
						}
						String imgFilePath = BaseEnv.FILESERVERPATH+"/temp/" + strDay +".jpg" ; 
//						String imgFilePath = BaseEnv.FILESERVERPATH+"/pic/CRMSaleFollowUp/" + strDay +".jpg" ; 
						File imgFile = new File(imgFilePath) ;
						if(!imgFile.getParentFile().exists()){
							imgFile.getParentFile().mkdirs() ;
						}
						imageName += strDay+".jpg;" ;
						OutputStream out = new FileOutputStream(imgFilePath);
						out.write(byteImage); 
						out.flush(); 
						out.close(); 
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				values.put("Attachment", imageName) ;
			}
			values.put("ClientId", sales.getClientId()) ;
			values.put("f_brother", sales.getClientId()) ;
			values.put("EmployeeID", login.getId()) ;
			values.put("DepartmentCode", login.getDepartCode()) ;
			values.put("Content",sales.getContent()) ;
			values.put("FollowPhase", "2") ;
			values.put("VisitMethod", "2") ;
			values.put("VisitTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)) ;
			values.put("location", sales.getLocation()) ;
			values.put("id", IDGenerater.getId()) ;
			values.put("NextVisitTime", sales.getNextVisitTime()) ;
			values.put("Topic", sales.getTopic()) ;
			values.put("GenWorkPlan", sales.isAddPlan() ? 1 : 2) ;
			
			Result addResult = execDefineAdd(request, values, "CRMSaleFollowUp", "");
			
			if (addResult.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				String msg = getDefineErrMsg(request, addResult);
				result.setCode(addResult.getRetCode()); 
				result.setDesc(msg) ;
			}else{
				result.setCode(0); 
				result.setDesc("��ӳɹ�") ;
			}
		}catch (Exception e) {
			e.printStackTrace() ;
			result.setCode(-1); 
			result.setDesc("���ʧ��") ;
		}
		writeJson(result, response) ;
	}
	
	
	/**
	 * ���۸��� ѡ��ͻ�
	 * @param request
	 * @param response	 
op=selectClient&keyword=XXXX&lastId=XXXX
	 */
	public void selectClient(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		// ���ﲻУ��Ȩ����ûȨ�������ݷ��ص�
		
		String userId = request.getParameter("sid");
		String keyword = request.getParameter("keyword");
		String lastId = request.getParameter("lastId");
		if (checkParameter("keyword,lastId",
				request, response) == false) {
			return;
		}
		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/CRMClientAction.do");

		Result result = mgt.queryClient(getLoginBean(request), keyword, mop, lastId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	
	
	/**
	 * ÿһ���Ӹ����û�����
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private void online(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid") ;
		String keycode = request.getParameter("keycode");
		/*�ڴ˸����û���������Ϣ*/
		if(userId!=null && userId.length()>0){
			String type = "mobile";
			OnlineUserInfo.refresh(userId,keycode == null ? type:"client");
			/*��ѯ˽����,ϵͳ��Ϣ��,�ʼ���,����������*/
			Result result = mgt.queryMessageByUserId(userId) ;
			if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
				MobileCount count = (MobileCount) result.retVal ;
				writeJson(count, response) ;
			}
		}
	}
	
	
	/**
	 * �ֻ��� Ȩ����֤ ��֤�ɹ���(˽����,ϵͳ��Ϣ��,�ʼ���,����������,�û�ID)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doAuth(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String username = request.getParameter("username") ;	/*���Զ˵ĵ�¼�û���*/
		String password = request.getParameter("password") ;	/*���Զ˵ĵ�¼����*/
		String mobile = request.getParameter("mobile") ;		/*�ֻ�����*/
		String userId = "" ;
		
        
		/*��֤�û����������Ƿ���ȷ�������Ǿ���MD5���ܵ�*/
		boolean auth = false ;
		if(username!=null && password!=null 
				&& username.length()>0 && password.length()>0){
			//if(keycode !=null && keycode.trim().length()>0){
			//	username = GlobalsTool.toChinseChar(username) ;
			//}
			Result result = mgt.queryEmployeeByEmpName(username.trim()) ;
			if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
				EmployeeBean employee = (EmployeeBean) result.retVal ;
				userId = employee.getId() ;
					String md5Pwd = employee.getPassword() ;
					//md5Pwd = StringUtils.lowerCase(md5Pwd);
					if(StringUtils.equalsIgnoreCase(password,md5Pwd)){
						auth = true ;
					}
			}
		}
		if(auth){
			OnlineUser lineUser =  OnlineUserInfo.getUser(userId);
			Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(userId) ;
			if(sessionSet == null){
//				sessionSet = new Hashtable();
		        sessionSet = new Hashtable();
		        sessionSet.put("SCompanyID", "1");
		        sessionSet.put("sunCmpClassCode", "00001");
		        sessionSet.put("DepartmentCode", lineUser.deptId) ;
		        sessionSet.put("UserId", userId);
		        sessionSet.put("UserName", lineUser.name);
		        sessionSet.put("Local", GlobalsTool.getLocale(request));
		        sessionSet.put("IsLastSCompany", true);		        
		        BaseEnv.sessionSet.put(userId, sessionSet);
			}
			String serverKey = IDGenerater.getId();
			JsonObject json = new JsonObject();
//			json.addProperty("code", 0);
			json.addProperty("serverKey", serverKey);
			json.addProperty("userid", userId);
			
			json.addProperty("dogid",SystemState.instance.dogId);//�������� ������������
			
			
			/*��¼iPhone*/
			String token = request.getParameter("dt") ;
			if(token!=null && token.length()>0){
				token = token.substring(1,token.length()-1) ;
				token = token.replaceAll(" ", "") ;
				//��ӵ����ط�����
				mgt.addIPhoneToken(userId, token, serverKey) ;
				//��ȡ�û�����
				Result result=mgt.getUserInfo(username);
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					MobileOnlineUser onlineUser =(MobileOnlineUser)result.getRetVal();
			   		json.addProperty("userinfo", gson.toJson(onlineUser));
				}
			}
			/*��¼Android*/
			String imei = request.getParameter("MobileId") ;
			if(StringUtils.isNotBlank(imei)){
				mgt.addIPhoneToken(userId, imei, serverKey) ;
			}

			// �����ڴ��и��û��ĵ�¼״̬����ʱͨѶ�ͻ�����Ҫ�õ�
			EmployeeItem loginItem = MSGConnectCenter.employeeMap.get(userId);
			if (loginItem != null
					&& loginItem.loginStatus == EmployeeItem.OFFLINE) {
				loginItem.loginType = EmployeeItem.MOBILE_LOGIN;
				loginItem.loginStatus = EmployeeItem.ONLINE;
				MSGConnectCenter.userStatus(loginItem.userId,
						loginItem.loginType, loginItem.loginStatus);
			}
			writeJson(json, response);
		}else{
			MobileResult result = new MobileResult() ;
			result.setCode(0) ;
			result.setDesc("�û������������") ;
			writeJson(result, response) ;
		}
		
	}

	
	
	
	/**
	 * �Ѽ��ܺ��ֽ�����ת����16�����ַ�
	 * @param buffer
	 * @return
	 */
	private static String toHex(byte[] buffer) {
		StringBuffer sb = new StringBuffer(buffer.length);
		String temp;
		for (int i = 0; i < buffer.length; i++) {
			temp = Integer.toHexString(0xFF & buffer[i]);
			if (temp.length() < 2) {
				sb.append("0");
			}
			sb.append(temp.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 * ��ѯϵͳ��Ϣ
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listTips(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid") ;
		String lastmid = request.getParameter("lastmid") ;
		String type = request.getParameter("type") ;
		String status = request.getParameter("status") ;				
//		type = "mail";
//		lastmid = "c60c0a1d_1305201805352817371";
		Result result = mgt.queryMyAdvice(userId,lastmid,type,status);
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			List<MobileMessage> list = (List<MobileMessage>)result.retVal ;
			for(MobileMessage advice : list){
				advice.setContent(replace(request,advice.getContent())) ;
			}
			writeJson(list,response);
		}else{
			MobileResult res = new MobileResult() ;
			res.setCode(0) ;
			res.setDesc("���ݼ���ʧ�ܣ�") ;
			writeJson(res, response) ;
		}
	}
	
	private String  replace(HttpServletRequest request,String str){
    	while(str.indexOf("RES<")>-1){
    		int pos = str.indexOf("RES<");
    		String temp = str.substring(pos+4,str.indexOf(">",pos));
    		temp = getMessageResources(request, temp);
    		str = str.substring(0,pos)+temp+str.substring(str.indexOf(">",pos)+1);
    	}
    	return str;
    }
	
	
	/**
	 * ����ϵͳ������ϸ��Ϣ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getInfo(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		if (checkParameter("id,type", request, response) == false) {
			return;
		}

		String id = request.getParameter("id");
		String type = request.getParameter("type");
		
		Result rs = mgt.queryGetInfo(id, type);
		
		if (ErrorCanst.DEFAULT_SUCCESS == rs.getRetCode() && rs.getRealTotal() > 0) {
			JsonArray jarr = (JsonArray) rs.getRetVal();
			JsonObject j= new JsonObject();
			j.addProperty("code", 0);
			j.add("data", (JsonObject)jarr.get(0));
			writeJson(j, response) ;
		} else {
			MobileResult res = new MobileResult() ;
			res.setCode(-1) ;
			res.setDesc("���ݼ���ʧ�ܣ�") ;
			writeJson(res, response) ;
		}
		
	}
	
	/**
	 * �����ҵ����й�����������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listWorkflows(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String userId = request.getParameter("sid") ;
		String lastwid = request.getParameter("lastwid") ;
		Result result = mgt.queryMyWorkFlow(userId,lastwid) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			writeJson((List<MobileWorkFlow>)result.retVal, response) ;
		}else{
			MobileResult result2 = new MobileResult() ;
			result2.setCode(-1) ;
			result2.setDesc("��������ʧ��") ;
			writeJson(result2, response) ;
		}
	}
	
	/**
	 * ��ȡ����������ϸ��Ϣ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getWorkflow(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String userId = request.getParameter("sid") ;
		String flowId = request.getParameter("wid") ;	/*����������Id*/
		
		LoginBean loginBean = getLoginBean(request);
		String strHttp = "http://"+request.getHeader("host");
		Hashtable tableMap = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		
		Result result = mgt.queryWorkFlowNodeInfo(flowId,userId,strHttp,loginBean,tableMap,props) ;
		if(result.retCode == ErrorCanst.RET_NO_RIGHT_ERROR){
			MobileWorkFlow mworkFlow = (MobileWorkFlow) result.getRetVal() ;
			mworkFlow.setId(flowId) ;
			writeJson(mworkFlow, response) ;
		}else if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/*��ѯ��һ���������*/
			MobileWorkFlow mworkFlow = (MobileWorkFlow) result.getRetVal() ;
			OAWorkFlowTemplate tableFlow = BaseEnv.workFlowInfo.get(mworkFlow.getId());
			mworkFlow.setId(flowId) ;
			OAMyWorkFlowMgt flowMgt = new OAMyWorkFlowMgt() ;
			HashMap map = flowMgt.getOAMyWorkFlowInfo(flowId,tableFlow.getTemplateFile());
			if("1".equals(tableFlow.getDesignType())){
				mworkFlow.setDetailURL(strHttp+"/OAWorkFlowAction.do?operation=5&tableName="+tableFlow.getTemplateFile()+"&keyId="+flowId+"&sid="+loginBean.getId());
			}
			String designId = map.get("applyType").toString();
			String nextNodes = map.get("nextNodeIds").toString();			
			String currNodeId = map.get("currentNode").toString();
			String department = map.get("departmentCode").toString();
			
			if(nextNodes.length()==0){//����û�û�а������Ҳ����ֱ��ת������Ҫ��������һ���ڵ�,�������ϸ���Ƿ��в�������
				Result rs = flowMgt.getNextNodeIdByVal(designId, currNodeId, flowId,true,loginBean);
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && !"".equals(rs.retVal)){
					nextNodes=rs.retVal.toString();
				}
				flowMgt.transactStart(currNodeId, flowId, loginBean,tableFlow.getTemplateFile());
			}
			
			if(nextNodes.length()==0){//����û�û�а������Ҳ����ֱ��ת������Ҫ��������һ���ڵ�,�������ϸ���Ƿ��в�������
				Result rs=flowMgt.getNextNodeIdByVal(designId, currNodeId, flowId,true,loginBean);
				if("".equals(rs.retVal)||rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
					MobileResult result2 = new MobileResult() ;
					result2.setCode(0) ;
					result2.setDesc("��������ʧ��") ;
					writeJson(result2, response) ;
				}else{
					nextNodes=rs.retVal.toString();
				}
				flowMgt.transactStart(currNodeId, flowId, loginBean,tableFlow.getTemplateFile());
			}
			
			String[] nextNodeIds=nextNodes.split(";");
			WorkFlowDesignBean bean=BaseEnv.workFlowDesignBeans.get(designId);

			request.setAttribute("department", department);
			FlowNodeBean currNB=bean.getFlowNodeMap().get(currNodeId);		 
			
			/*�¸��������*/
			List<MobileFlowAction> actionList = new ArrayList<MobileFlowAction>() ;
			//�����ڵ�������
			for(int i=0;i<nextNodeIds.length;i++){
				FlowNodeBean nodeBean=bean.getFlowNodeMap().get(nextNodeIds[i]);
				if (nodeBean == null)
					continue;
				MobileFlowAction action = new MobileFlowAction() ;
				action.setId(nodeBean.getKeyId()) ;
				action.setName(nodeBean.getDisplay()) ;
				action.setCancel(false) ;
				action.setIdeaRequired(nodeBean.getIdeaRequired()==true?1:0) ;
				if(nodeBean.getZAction().equals("CHECK")){
					ArrayList<String []> checkPersons=flowMgt.getNodeCheckPerson(nodeBean, department,flowId,null,tableFlow.getTemplateFile(),userId);
					if("true".equals(tableFlow.getAutoPass())&&currNodeId.equals("0")&&i==0){
						for(int j=0;j<checkPersons.size();j++){
							String[] cps=checkPersons.get(j);
							if(cps[0].equals(userId)){
								checkPersons.remove(j);
							}
						}
					}
					String strPersons =  "" ;
					if(checkPersons.size()>0){
						for(String[] person : checkPersons){
							strPersons += person[1] + "("+person[0]+")," ;
						}
					}
					if(strPersons.length()>1){
						strPersons = strPersons.substring(0, strPersons.length()-1) ;
					}
					if(strPersons.split(",").length>1){
						action.setFixedApp(false) ;
					}else{
						action.setFixedApp(true) ;
					}
					action.setTo(strPersons) ;
				}
				actionList.add(action) ;
			}		
			//�鿴��ǰ�ڵ�����л��˹��ܣ���ô��ѯ֮ǰ������˹��Ľڵ㼰��Щ�ڵ�������
			List<MobileFlowAction> cancelList = new ArrayList<MobileFlowAction>() ;
			if(currNB.isAllowBack()){ 
				mgt.getBackNodeBean(flowId, currNB, cancelList, bean) ;
			}
			mworkFlow.setActions(actionList) ;
			mworkFlow.setCancelactions(cancelList) ;
			writeJson(mworkFlow, response) ;
		}else{
			MobileResult result2 = new MobileResult() ;
			result2.setCode(0) ;
			result2.setDesc("��������ʧ��") ;
			writeJson(result2, response) ;
		}
	}
	
	/**
	 * ����������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void approvalWorkflow(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String userId = request.getParameter("sid") ;	
		String to = request.getParameter("to") ;			/*�û�ID����ʽΪ ���ž���(FFDF)��*/
		String keyId = request.getParameter("wid") ;		/*��������ID*/
		String nextStep  = request.getParameter("actid") ;	/*���ID*/
		String memo   = request.getParameter("memo") ;		/*�������*/
		String cancel = request.getParameter("cancel") ;
		
		if("1".equals(cancel)){
			nextStep = "back_"+nextStep ;
		}
		String[]checkPersons = to.split(",") ;
		String affix=request.getParameter("affix");
		String oaTimeLimit=request.getParameter("oaTimeLimit");
		String oaTimeLimitUnit=request.getParameter("oaTimeLimitUnit");
		String appendWake=request.getParameter("appendWake");
		
		MobileResult mobileCode = new MobileResult() ;
		if((checkPersons==null || checkPersons.length==0) && !"-1".equals(nextStep)){
			mobileCode.setCode(0) ;
			mobileCode.setDesc("����Ҫ��һ�������ˣ�") ;
			writeJson(mobileCode, response) ;
			return ;
		}
		Locale locale = GlobalsTool.getLocale(request) ;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		
		OAMyWorkFlowMgt flowMgt = new OAMyWorkFlowMgt() ;
		boolean isSucess = false ;
		try{
			Result result = flowMgt.getWorkFlowInfo(keyId) ;
			String[] flowInfo = (String[]) result.retVal ;
			result = flowMgt.deliverTo(keyId, nextStep, checkPersons,flowInfo[2],getLoginBean(request),flowInfo[1],
					locale,memo,affix,oaTimeLimit,oaTimeLimitUnit,appendWake,resources,"","","");
			if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
				result=flowMgt.updateFlowDepict(flowInfo[1], keyId,locale);
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					isSucess = true ;
				}
			}
		}catch (Exception e) {
			e.printStackTrace() ;
			BaseEnv.log.error("MOfficeServlet approvalWorkflow method", e) ;
		}
		if(isSucess){
			mobileCode.setCode(1) ;
			mobileCode.setDesc("�����ɹ�") ;
		}else{
			mobileCode.setCode(0) ;
			mobileCode.setDesc("����ʧ��") ;
		}
		writeJson(mobileCode, response) ;
	}
	
	/**
	 * �˻�,�������˻ع�������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void cancelWorkflow(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid") ;
		String flowId = request.getParameter("wid") ;	/*����������Id*/
		boolean isSucess = false;
		MobileResult mobileCode = new MobileResult() ;
		try {
			OAMyWorkFlowMgt wkfmgt= new OAMyWorkFlowMgt();
			MessageResources resources = null;
			Locale locale = GlobalsTool.getLocale(request) ;
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			HashMap<String,String> OAMyWorkFlow = (HashMap<String, String>)mgt.cancelWorkflow(flowId).getRetVal();
			Result rs = wkfmgt.cancel(OAMyWorkFlow.get("id"),OAMyWorkFlow.get("lastNodeId") , OAMyWorkFlow.get("currentNode"), getLoginBean(request), OAMyWorkFlow.get("designId"));
			if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
				rs= wkfmgt.updateFlowDepict(OAMyWorkFlow.get("designId"), OAMyWorkFlow.get("id"),locale);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					isSucess = true ;
				}
			}
		} catch (Exception e) {
			e.printStackTrace() ;
			BaseEnv.log.error("MOfficeServlet cancelWorkflow method", e) ;
		}
		if(isSucess){
			mobileCode.setCode(1) ;
			mobileCode.setDesc("���˳ɹ�") ;
		}else{
			mobileCode.setCode(0) ;
			mobileCode.setDesc("����ʧ��") ;
		}
		writeJson(mobileCode, response) ;
	}
	
	/**
	 * �����ҵĸ���Ӧ�ù����б�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyModules(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid") ;
		Result result = mgt.queryBusinessModule(userId, "personal") ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			writeJson((List<Properties>)result.retVal, response) ;
		}else{
			MobileResult result2 = new MobileResult() ;
			result2.setCode(0) ;
			result2.setDesc("��ʱ��û����ĸ���Ӧ��") ;
			writeJson(result2, response);
		}
	}
	
	/**
	 * �����ҵ���ҵ�����б�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listBusinessModules(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String userId = request.getParameter("sid") ;
		Result result = mgt.queryBusinessModule(userId, "business") ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			writeJson((List<Properties>)result.retVal, response) ;
		}else{
			MobileResult result2 = new MobileResult() ;
			result2.setCode(0) ;
			result2.setDesc("��ʱ��û�������ҵ����") ;
			writeJson(result2, response);
		}
	}
	
	/**
	 * �����ҵ���ҵ���� ��ͨ��ͼ:���ݸ�ʽ֧��(txt��html��png��jpg)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doDetailsview(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String reportNumber = request.getParameter("reportNumber") ;
		
		LoginBean loginBean = getLoginBean(request) ;
		try {
			request.setAttribute("exportType", "mobile") ;
			request.setAttribute("layoutType", "bill") ;			
			DefineReportBean defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(request).toString(),loginBean.getId());
			Result result = new ReportData().showData(null, request, null, reportNumber,defBean, "", 1, 10000, new ArrayList(),new ArrayList(),loginBean,null) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				writeJson((MobileDetail)result.retVal, response) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			MobileResult result2 = new MobileResult() ;
			result2.setCode(0) ;
			result2.setDesc("��������ʧ��") ;
			writeJson(result2, response) ;
		}
	}
	
	
	/**
	 * ���ر����ѯ�����ֶ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doQueryview(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String moduleId = request.getParameter("mid") ;
		try {
			Result result = mgt.queryReportByModule(moduleId) ;
			String reportNumber = "" ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				reportNumber = (String) result.retVal ;
			}
			if("".equals(reportNumber)){
				MobileResult result2 = new MobileResult() ;
				result2.setCode(0) ;
				result2.setDesc("��������ʧ��") ;
				writeJson(result2, response) ;
				return ;
			}
			LoginBean loginBean = getLoginBean(request) ;
			DefineReportBean reportBean = DefineReportBean.parse(reportNumber+"SQL.xml", GlobalsTool.getLocale(request).toString(),loginBean.getId()) ;
			List<ReportField> conFields = reportBean.getConFields() ;
			MobileQuery query = new MobileQuery() ;
			List<MobileParam> listCon = new ArrayList<MobileParam>() ;
			if(conFields!=null && conFields.size()>0){
				for(int i=0;i<conFields.size();i++){
					ReportField reportField = conFields.get(i) ;
					MobileParam param = new MobileParam() ;
					param.setId(reportField.getAsFieldName()) ;
					param.setName(reportField.getDisplay()) ;
					int fieldType = reportField.getFieldType() ;
					if (fieldType == DBFieldInfoBean.FIELD_DOUBLE 
							|| fieldType == DBFieldInfoBean.FIELD_INT) {
						param.setFormat("NUMBER");
					} else if (fieldType == DBFieldInfoBean.FIELD_DATE_LONG
							|| fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
						param.setFormat("DATE");
					} else{
						param.setFormat("TEXT");
					}
					if("1".equals(reportField.getIsNull())){
						param.setEnabledNull(true) ;
					}else{
						param.setEnabledNull(false) ;
					}
					listCon.add(param) ;
				}
			}
			query.setParameters(listCon) ;
			query.setTarget("doQueryResultview") ;
			query.setType(3) ;
			writeJson(query, response) ;
		} catch (Exception e) {
			e.printStackTrace();
			BaseEnv.log.error("MOfficeServlet", e) ;
			MobileResult result = new MobileResult() ;
			result.setCode(0) ;
			result.setDesc("��ѯ����") ;
		}
	}
	
	/**
	 * ���ظ���Ӧ�ú���ҵ���ܵĲ�ѯ���
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doListview(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String moduleId = request.getParameter("mid") ;
		Result result = mgt.queryReportByModule(moduleId) ;
		String reportNumber ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			reportNumber = (String) result.retVal ;
		}
		reportNumber = "mobileCRMClient" ;
		if("".equals(reportNumber)){
			MobileResult result2 = new MobileResult() ;
			result2.setCode(0) ;
			result2.setDesc("��������ʧ��") ;
			writeJson(result2, response) ;
			return ;
		}
		
		LoginBean loginBean = getLoginBean(request) ;
		try {
			request.setAttribute("exportType", "mobile") ;
			DefineReportBean defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(request).toString(),loginBean.getId());
			result = new ReportData().showData(null, request, null, reportNumber,defBean, "", 1, 10000, new ArrayList(),new ArrayList(),loginBean,null) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				writeJson((MobileListData)result.retVal, response) ;
			}
		} catch (Exception e) {
			e.printStackTrace();

			MobileResult result2 = new MobileResult() ;
			result2.setCode(0) ;
			result2.setDesc("��������ʧ��") ;
			writeJson(result2, response) ;
		}
	}
	
	/**
	 * ��ȡ�û�Ȩ��
	 * @param request
	 * @return
	 */
	private LoginBean getLoginBean(HttpServletRequest request){
		
		String userId = request.getParameter("sid") ;
		LoginBean loginBean = new LoginBean() ;
		EmployeeBean userBean = (EmployeeBean) new UserMgt().detail(userId).retVal ;
		if (null == userBean) {
			return null;
		}
        ArrayList roleModuleList = new ArrayList();
        //�����û�Ȩ��
        //������ֻ���ҽ�ɫȨ��
        //�������Ա�����ò��ɫ��ֱ��ӵ������Ȩ��
        ArrayList allScopeRight = new ArrayList(); //��¼Ӧ��������ģ��ķ�ΧȨ��
        HashMap roleModuleScopeMap = new HashMap();
        if(!"admin".equalsIgnoreCase(userBean.getSysName())){
            ArrayList roles = (ArrayList) new LoginAction().getRoles(userBean, "1");
        	//�û�����Ҳ��һ����ɫ
        	RoleBean selfRb = new RoleBean();
        	selfRb.setId(userBean.getId());
        	selfRb.setRoleName(userBean.getEmpFullName());
        	selfRb.setRoleDesc(userBean.getEmpFullName());
        	selfRb.setHiddenField(userBean.getHiddenField());                	
        	roles.add(selfRb);
        	
            for (Object o : roles) {
                RoleBean rb = (RoleBean) o;
                
                //��ѯ��ɫģ��Ȩ��
                Result result = new RoleMgt().queryRoleModuleByRoleid(rb.getId(),userBean.getId());
                if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                	//�ϲ�����ģ��Ȩ��
                	roleModuleList.addAll((List)result.getRetVal());
                }
                
                //��ѯ��ɫ��ΧȨ��
                result = new RoleMgt().queryRoleScopyByRoleid(rb.getId(),userBean.getId(),userBean.getEmpFullName(),userBean.getDepartmentCode());
                if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                	//�ϲ����з�ΧȨ��
                	HashMap hm = (HashMap)result.getRetVal();
                	for(Object hmo:hm.keySet()){
                		ArrayList list = (ArrayList)roleModuleScopeMap.get(hmo);
                		if(list==null){
                			roleModuleScopeMap.put(hmo, hm.get(hmo));
                		}else{
                			list.addAll((List)hm.get(hmo));
                		}
                	}
                
                }
            }
        
            //��ѯӦ��������ģ�鷶ΧȨ��------------------------------------
            Result result = new RoleMgt().queryAllModScope(userBean.getId(),userBean.getDepartmentCode()) ;
            if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
            	allScopeRight =(ArrayList)result.retVal ;
            }
        }
        loginBean.setId(userId) ;
        loginBean.setName(userBean.getSysName()) ;
        loginBean.setMobile(userBean.getMobile());
        loginBean.setDefSys(userBean.getDefSys());
        loginBean.setLoginTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
        loginBean.setEmpFullName(userBean.getEmpFullName());
        loginBean.setDepartCode(userBean.getDepartmentCode()) ;
        loginBean.setSunCmpClassCode("00001");
        loginBean.setSunCompany("1");
        loginBean.setAllScopeRight(allScopeRight);
        loginBean.setRoleModuleList(roleModuleList);
        loginBean.setRoleModuleScopeMap(roleModuleScopeMap);
        for(Object o:SystemState.instance.moduleList){
        	if(o.toString().equals("1") && userBean.getCanJxc()==1){
        		loginBean.setCanJxc(1);
        	}
        	if(o.toString().equals("2") && userBean.getCanOa()==1){
        		loginBean.setCanOa(1);
        	}
        	if(o.toString().equals("3") && userBean.getCanCrm()==1){
        		loginBean.setCanCrm(1);
        	}
        	if(o.toString().equals("4") && userBean.getCanHr()==1){
        		loginBean.setCanHr(1);
        	}
        	
        }
        if(SystemState.instance.funOrders && userBean.getCanOrders()==1){
    		loginBean.setCanOrders(1);
    	}
        
        //ְԱ����ID
        String groupIds = (String) new DynDBManager().getGroupIds(userBean.getId()).getRetVal() ;
        loginBean.setGroupId(groupIds) ;
        
//        //�����ȡmenuΪ�գ�˵����δ���ɹ��˵����������������в˵�������ֱ��ȡ�˵�����
        String locale = GlobalsTool.getLocale(request).toString();

        ArrayList userMenu = null;
        HashMap userMenus = new HashMap(); //�������в˵�������oa,crm
        HashMap operationMap = new HashMap();
        HashMap operationMapKeyId = new HashMap();
        //�����ȡmenuΪ�գ�˵����δ���ɹ��˵����������������в˵�������ֱ��ȡ�˵�����
        if(!"admin".equalsIgnoreCase(loginBean.getName())&&loginBean.getMenus() == null){

            for (int defSys = 0; defSys <= 4; defSys++) {
                //���ɲ˵�
                if(!GlobalsTool.hasMainModule(defSys+"")){
                    continue;
                }
                if(defSys == 1 && loginBean.getCanJxc()!=1){
                	continue;
                }
                if(defSys == 2 && loginBean.getCanOa()!=1){
                	continue;
                }
                if(defSys == 3 && loginBean.getCanCrm()!=1){
                	continue;
                }
                if(defSys == 4 && loginBean.getCanHr()!=1){
                	continue;
                }

                userMenu = new ArrayList();
                for (int i = 0; i < loginBean.getRoleModuleList().size(); i++) {
                    RoleModuleBean rmBean = (RoleModuleBean) (loginBean.getRoleModuleList()).get(i);
                    Result rtemp = new Result();
                    //���� ModuleBeanId �Ӳ˵����ҵ������ModuleBean ����
                    //�ҵ��û��ĵ�ǰϵͳ��������ϵĬ��һ��

                    MenuManageAction.userMouleRec((ArrayList) BaseEnv.moduleMap.get(defSys +""), 
                    			  rmBean.getModuleOpBean().getModuleBean().getId(), rtemp);
                    if (rtemp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        //�ҵ���Ӧ�Ĳ˵�������
                        ModuleBean moduleBean = (ModuleBean)rtemp.getRetVal();
                        //ͨ��ModuleBean �������˵�����һ������ӵ��˵�����ȥ
                        ModuleBean lastBean = MenuManageAction.userMenuRec(moduleBean,userMenu);

                        //ȡ���������operationMap��,����ģ�鰴Ť��Ȩ
                        String url = moduleBean.getLinkAddress();
                        int lastIndex = 0 ;
                        if(url!=null && url.contains("UserFunctionQueryAction.do")){
                            if(url.indexOf("parentTableName=")!=-1){
                            	lastIndex = url.indexOf("&",url.indexOf("&tableName=")+11) ;
                            	if(lastIndex!=-1){
                            		url = url.substring(0,lastIndex) ;
                            	}
                            }else if(url.indexOf("&moduleType=")!=-1){
                            	lastIndex = url.indexOf("&", url.indexOf("&moduleType=")+12) ;
                            	if(lastIndex!=-1){
                            		url = url.substring(0,lastIndex) ;
                            	}
                            }else{
                            	if(url.contains("&")){
                            		url = url.substring(0,url.indexOf("&",url.indexOf("?tableName"))) ;
                            	}
                            }
                        }
                        MOperation mop = (MOperation) operationMap.get(url);
                        if (mop == null) {
                            mop = new MOperation();
                            mop.moduleUrl = url;
                            mop.moduleId = moduleBean.getId().trim();
                            operationMap.put(url, mop);
                            operationMapKeyId.put(mop.moduleId, mop); //���ڵ������ļ�Ȩ
                        }
                        mop.setOperation(rmBean.getModuleOpBean().getOperationID());
                        ArrayList roleModuleSm = (ArrayList)loginBean.getRoleModuleScopeMap().get(rmBean.getModuleOpBean().getModuleOpId()+"");
                        if(roleModuleSm != null){                        	
                        	mop.setScope(rmBean.getModuleOpBean().getOperationID(),roleModuleSm);
                        }
                    }
                }
                userMenus.put(defSys+"", userMenu);               
            }
            //���login�н�ɫ���ͽ�ɫȨ���ڴ�
            loginBean.setRoleModuleList(null);
            
            loginBean.setMenus(userMenus);
            loginBean.setOperationMap(operationMap);
            
            loginBean.setOperationMapKeyId(operationMapKeyId);
            
            //setBrotherTableRight(loginBean, operationMap,operationMapKeyId) ;
            //���ù�������Ȩ�ޣ������޸ĺ�ֻ�ڵ�һ�ε�½ʱ����һ�Σ���Ȩ�޷����仯���û������µ�½һ�� -- ������
            MenuManageAction.setWorkFlowRight(locale,loginBean,operationMap,operationMapKeyId);
            //���ù����ƻ� �� �¼��ƻ� ��Ȩ��
            MenuManageAction.setWorkPlanRight(loginBean, operationMap,operationMapKeyId) ;
            
        }else if("admin".equalsIgnoreCase(loginBean.getName())&&loginBean.getOperationMap() == null){
            //����ϵͳĬ�ϵ�����Ȩ��
            loginBean.setOperationMap(BaseEnv.adminOperationMap);
            loginBean.setOperationMapKeyId(BaseEnv.adminOoperationMapKeyId);
            
            //setBrotherTableRight(loginBean, BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId) ;
            //���ù�������Ȩ�ޣ������޸ĺ�ֻ�ڵ�һ�ε�½ʱ����һ�Σ���Ȩ�޷����仯���û������µ�½һ�� -- ������
            MenuManageAction.setWorkFlowRight(locale,loginBean,BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId);
            //���ù����ƻ� �� �¼��ƻ� ��Ȩ��
            MenuManageAction.setWorkPlanRight(loginBean, BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId) ;
            
       }
        return loginBean ;
	}
	

	/**
	 * �����ҵ�˽���б�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMessages (final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String  userId = request.getParameter("sid");
		String senderid = request.getParameter("from");
		String lastmid=request.getParameter("lastmid");
		senderid = getEmailOrUserId(senderid) ;
		OnlineUser sendUser = OnlineUserInfo.getUser(senderid) ;
		if(sendUser!=null){
			senderid = sendUser.getId() ;
		}
		MSGConnectCenter.receiveMsg(userId, senderid);
		Result result = mgt.listMessages(userId, senderid, lastmid);
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			writeJson((List<MobileMessageBean>) result.retVal, response);
		} else {
			MobileResult res = new MobileResult();
			res.setCode(0);
			res.setDesc("���ݼ���ʧ�ܣ�");
			writeJson(res, response);
		}
	}
	/**
	 * ����˽��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void sendMessage (final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String usetId = request.getParameter("sid");
		String to = request.getParameter("to");
		to = to.replace(" ", "");
		
		MobileResult res = new MobileResult() ;
		/*�ж��Ƿ���ڴ��û�*/
		if (to != null && to.contains(",")) {
			String[] receives = to.split(",");
			for (String sName : receives) {
				sName = getUserName(sName);
				OnlineUser toUser = OnlineUserInfo.getUserByEmpName(sName);
				if (toUser == null){
					res.setCode(0);
					res.setDesc("����ʧ��,������\""+sName+"\"��") ;
					writeJson(res, response) ;
				}
			}
		}
		String content = request.getParameter("content");
		MSGConnectCenter.sendMsg(usetId, to, content, "person");
//		Result result = mgt.sendMessage(usetId,to,content,"person");
//		if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			res.setCode(1);
			res.setDesc("���ͳɹ���") ;
//		}else{
//			res.setCode(0);
//			res.setDesc("����ʧ�ܣ�") ;
//		}
		writeJson(res, response) ;
	}
	
	/**
	 * �ҵ�˽��Ŀ¼�б�
	 * typeֵ 0 Ϊ ����  1Ϊ����  2ΪȺ��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMessageBox (final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid");
		String lastId = request.getParameter("lastId") ;
		Result result = mgt.listMessageBox(userId,lastId);
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			writeJson((List<MobileCountList>)result.retVal, response);
		}else{
			MobileResult res = new MobileResult() ;
			res.setCode(0) ;
			res.setDesc("���ݼ���ʧ�ܣ�") ;
			writeJson(res, response) ;
		}
	}
	/**
	 * ��ѯ��������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listOnlineUsers(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		//String userId = request.getParameter("sid");
  	   	List<MobileOnlineUser> userList = new ArrayList<MobileOnlineUser>();
  	   	for(OnlineUser user:OnlineUserInfo.getAllUserList()){
 			MobileOnlineUser onlineUser = new MobileOnlineUser();
 			onlineUser.setUid(user.getId());
 			onlineUser.setName(user.getName());
 			onlineUser.setTitle(user.getDepartmentName());
 			onlineUser.setSign("");
 			onlineUser.setIcon("") ;
 			//onlineUser.setKeyword(ChineseSpelling.getSelling(onlineUser.getName())) ;
  	   		if (user.isOnline()){
  	   			userList.add(onlineUser);
  	   		}
  	   	}
		writeJson(userList,response);
	}

	/**
	 * �����û�������Ϣ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getUserInfo(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userid = request.getParameter("userid");
		Result result=mgt.getUserInfo(userid);
		if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			MobileOnlineUser onlineUser =(MobileOnlineUser)result.getRetVal();
	   		writeJson(onlineUser, response);
		}else{
			MobileResult res = new MobileResult() ;
			res.setCode(0) ;
			res.setDesc("���ݼ���ʧ�ܣ�") ;
			writeJson(res, response) ;
		}
	}
	/**
	 * ��ѯ���������������û�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listUsers(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {		
		Result result=mgt.listUsers();
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			writeJson((List<MobileOnlineUser>)result.retVal, response);
		}else{
			MobileResult res = new MobileResult();
			res.setDesc("��ѯʧ�ܣ�");
			writeJson(res, response);
		}
	}
	

	/**
	 * �о�ָ������ָ���ļ��е������ʼ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMail(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {	
		String userId = request.getParameter("sid") ;
		String bid = request.getParameter("bid") ;
		String fid = request.getParameter("fid") ;
		String lastmid=request.getParameter("lastmid");
		Result result=mgt.querylistMail(userId, bid, fid, lastmid);
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			writeJson((List<MobileEmail>)result.getRetVal(), response);
		}else{
			MobileResult res = new MobileResult() ;
			res.setCode(0) ;
			res.setDesc("���ݼ���ʧ�ܣ�") ;
			writeJson(res, response) ;
		}
	}
	
	/**
	 * ���������ʼ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void receiveMail(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		//String userId = request.getParameter("sid") ;
		String mid = request.getParameter("bid") ;
		Result rs = emailmgt.loadAccount(mid);
		MailinfoSettingBean setting = (MailinfoSettingBean) rs.getRetVal();	
		try {
			String userId = setting.getCreateby() ;
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
				Result result= emailmgt.handNewMail1000(uid, sm, mid, setting.getRetentDay());
				
				if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
					BaseEnv.log.info("EMailAction.run receiveMail handNewMail Error "+result.retCode);
				}
				request.setAttribute("msg", sm.newMsgList.size()); //�����ʼ����ش��ʼ�����
			}
			new BackgroundThread(sm,userId,mid);
		}catch(Exception ex){
			MobileResult res = new MobileResult() ;
			res.setCode(0) ;
			res.setDesc("����ʧ�ܣ�") ;
			writeJson(res, response) ;
		}

	}
	
	/**
	 *�����ʼ���ϸ����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getMail(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid");
		String mid = request.getParameter("mid");
//		mid = "1cf6f438_1308131537473130006";
		
		String strHttp = "http://"+request.getHeader("host");
		Result result=mgt.getMail(userId, mid,strHttp);
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			new EMailMgt().notifyMobile(userId);
			writeJson((MobileEmailDate)result.retVal, response);
		}else{
			MobileResult res = new MobileResult();
			res.setCode(-1) ;
			res.setDesc("���ݲ����ڣ������ѱ�ɾ����");
			writeJson(res, response);
		}
	}
	
	/**
	 * ���ط���״̬
	 * @param request
	 * @param response
	 */
	private void sendMail(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid");	//�û�id
		String content = request.getParameter("content");	//�ʼ�����
		String subject = request.getParameter("subject");		//�ʼ�����
		String to = request.getParameter("to");				//���͵�.............
		String from=request.getParameter("from");
		OAMailInfoBean info = new OAMailInfoBean();
		try{
			content = content.replaceAll("\r", "<br/>").replaceAll("\n", "<br/>") ;
			OnlineUser user = OnlineUserInfo.getUser(userId) ;
			String[] userIds = to.split(",") ;
			String toUser = "" ;
			for(String sid : userIds){
				String toName = getUserName(sid) ;
				toUser += toName+";" ;
			}
			String id = IDGenerater.getId();
			info.setId(id);
			info.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			info.setEmailType((from.indexOf("@")!=-1) ? 1 : 0);
			info.setFromUserId(userId); //�����˴���
			info.setGroupId("3"); //������
			info.setMailSize(content == null ? 0 : content.length());
			info.setMailFrom(user.getName());
			// ������
			info.setMailTo(toUser) ;
			info.setMailContent(content);
			info.setMailTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			info.setMailTitle(subject);
			info.setState(1); // ����ʱ���������ʼ�
			// mailInfo.setToUserId(userId); //���͸����ѵ��ʼ�
			info.setUserId(userId);
			MailinfoSettingBean setting = null ;
			if(from.indexOf("@")!=-1){
				Result rs = emailmgt.selectAccountByEmail(from) ;
				//��ѯ�ʺ���Ϣ
				if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
					MobileResult res = new MobileResult();
					res.setCode(-1) ;
					writeJson(res, response);
					res.setDesc("�����ⲿ�ʼ�����ʧ��") ;
					return;
				}
				setting = (MailinfoSettingBean)((List)rs.getRetVal()).get(0) ;
				setting.setAccount(setting.getId()) ;
				info.setAccount(setting.getId()) ;
			}else{
				info.setAccount("");
			}
			//�����ⲿ�ʼ�
			if (from.indexOf("@")!=-1 && to.indexOf("@")!=-1) {
				EMailMessage smes = new EMailMessage();
				for(String strId : userIds){
					smes.addRecipient(EMailMessage.TO, getEmailOrUserId(strId), getUserName(strId))  ;
				}
				String attachPath = from;//���������Ϊ·��
				smes.setContent(true,content);
				smes.setSubject(subject);
				smes.setDate(new Date()) ;
				AIOEMail sm = new AIOEMail();
				boolean smtpauth = setting.getSmtpAuth() == 1 ? true : false;
				// ָ��Ҫʹ�õ��ʼ�������
				sm.setAccount(setting.getMailaddress(),
							setting.getPop3server(), setting.getPop3username(),
							setting.getPop3userpassword(), smtpauth, setting
									.getSmtpserver(), setting.getSmtpusername(),
							setting.getSmtpuserpassword(), setting.getPop3Port(),
							setting.getSmtpPort(), setting.getDisplayName(),
							attachPath,setting.getCreateby(),
							setting.getPopssl()==1?true:false,setting.getSmtpssl()==1?true:false,setting.getAutoAssign()==1?true:false);
						// ���÷����ʼ��ӿ�
				try {
					sm.send(smes);
				} catch (Exception e) {
					e.printStackTrace();
					MobileResult result = new MobileResult() ;
					result.setCode(-1) ;
					result.setDesc("�����ⲿ�ʼ�����") ;
					return;
				}			
				//***************�ʼ��� ��Ϊ�첽����*********************					
				//emailmgt.sendByThread(smes, setting.getId(), mailInfo.getId(), userId); 	
			}else{
				// ָ���ʺź�����
				OAMailInfoBean mailInfo = new OAMailInfoBean();
				mailInfo.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				mailInfo.setEmailType((from.indexOf("@")!=-1) ? 1 : 0);
				mailInfo.setFromUserId(userId); //�����˴���
				mailInfo.setGroupId("1"); // �ռ���
				mailInfo.setMailSize(content == null ? 0 : content.length());
				mailInfo.setMailFrom(user.getName());
				mailInfo.setMailTo(toUser) ;
				mailInfo.setMailContent(content);
				mailInfo.setMailTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				mailInfo.setMailTitle(subject);
				mailInfo.setState(0); // ����ʱ���������ʼ�
				int count = 0;
				for(String strId : userIds){
					String toStr = getEmailOrUserId(strId) ;
					OnlineUser lineUser = OnlineUserInfo.getUserBySysName(toStr) ;
					if (lineUser == null){
						lineUser = OnlineUserInfo.getUserByEmpName(toStr) ;
						if(lineUser==null){
							continue ;
						}
					}
					String mailId = IDGenerater.getId();
					mailInfo.setId(mailId);
					mailInfo.setMailUID(mailInfo.getId());
					mailInfo.setAccount("") ;
					mailInfo.setToUserId(lineUser.getId()) ;
					mailInfo.setUserId(lineUser.getId());
					emailmgt.addMail(mailInfo);
					/*���֪ͨ��Ϣ����*/
					new AdviceMgt().add(
							userId, 
							mailInfo.getMailTitle(), 
							"<a href=\"javascript:mdiwin('/EMailAction.do?operation=5&keyId="+userId+"" +"&groupId="+mailInfo.getGroupId()+"&emailType=&noback=true','"+"')\" >"+"��"+mailInfo.getMailTitle()+"</a>", 
							strId, 
							mailId, 
							"email");
					count++;
				}
				if (count == 0) {
					MobileResult res = new MobileResult();
					res.setCode(-1) ;
					res.setDesc("�ռ����д�");
					writeJson(res, response);
					return;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			MobileResult res = new MobileResult();
			res.setCode(-1) ;
			res.setDesc("����ʧ�ܣ�");
			writeJson(res, response);
			return;
		}
		MobileResult res = new MobileResult();
		res.setCode(1) ;
		res.setDesc("���ͳɹ���");
		writeJson(res, response);
		

		// ���浽���ݿ���ȥ
		emailmgt.addMail(info);	
	}
	
	/**
     * //to:"��СǮ"<7237978a_0912251612007550187>, ����7237978a_0912251612007550187
	 * //to:"������"<liaolinlin@cmbc.com.cn>, ����liaolinlin@cmbc.com.cn
     * @param str
     * @return
     */
    private String getEmailOrUserId(String str) {
        Pattern pattern = Pattern.compile("<([/\\?&\\=\\@\\.\\w\\:\\-\\_]+)>");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return str;
    }
    
    /**
     * //to:"��СǮ"<7237978a_0912251612007550187>, ���� ��СǮ
	 * //to:"������"<liaolinlin@cmbc.com.cn>, ���� ������
     * @param str
     * @return
     */
    private String getUserName(String str) {
    	if(str!=null && str.contains("@")){
    		return str.replace("\"", "") ;
    	}
    	Pattern pattern = Pattern.compile("\"([/\\?&\\=\\@\\.\\w\\:\\_\\-\\u4e00-\\u9fa5]+)\"");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        if(!matcher.find() && str.indexOf("<")!=-1){
        	return str.substring(0, str.indexOf("<")) ;
        }
        return str;
    }

	/**
	 * ������ϵ���������
	 * @param request
	 * @param response
	 */
	public void listAddress(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid") ;
		String keyWord =request.getParameter("skey");
		String intranet =request.getParameter("intranet");
		if(keyWord!=null && keyWord.length()>0){
			keyWord = keyWord.trim() ;
			keyWord = GlobalsTool.toChinseChar_GBK(keyWord);
		}
		if("1".equals(intranet)){
			Result result=mgt.listAddress();
			if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
				writeJson((List<MobileEmail>)result.retVal, response);
			}else{
				MobileResult res = new MobileResult();
				res.setDesc("��ѯʧ�ܣ�");
				writeJson(res, response);
			}
		}else{
			Result result = mgt.listAddress2(keyWord,userId) ;
			if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
				writeJson((List<MobileEmail>)result.retVal, response);
			}else{
				MobileResult res = new MobileResult();
				res.setDesc("��ѯʧ�ܣ�");
				writeJson(res, response);
			}
		}
	}
	
	/**
	 * ���������б���ļ���
	 * @param request
	 * @param response
	 */
	public void listMailBox(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sid") ;
		//Result result = emailmgt.selectAccountByUser(userId);
		Result result=mgt.listMailBox(userId);
		if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			writeJson((List<MobileEmailList>)result.getRetVal(), response);
		}else{
			MobileResult res = new MobileResult() ;
			res.setDesc("����ʧ��error��") ;
			writeJson(res, response) ;
		}	
	}
	
	
	/**
	 * ����״̬
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateFlag(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String type= request.getParameter("type");
		String refid = request.getParameter("refid");
		String flag = request.getParameter("flag");
		String tipId = request.getParameter("tipsid") ;

		AdviceMgt amgt = new AdviceMgt();
		boolean rs = false;
		
		if ( type.contains("mail") && null != refid && refid.length() > 0) {
			rs = amgt.deleteByRelationId(refid, "");
			rs &= mgt.updateMail(refid,flag).getRetCode() == ErrorCanst.DEFAULT_SUCCESS;
		} else if (null != tipId && tipId.length() > 0) {
			if ("2".equals(flag)) {
				rs = amgt.deleteById(tipId);
			} else {
				rs = amgt.readOverById(tipId);
			}
		}

		MobileResult res = new MobileResult() ;
		if(rs){
			res.setCode(1);
			res.setDesc("���ݸ��³ɹ�");
		}else{
			res.setCode(0) ;
			res.setDesc("���ݸ���ʧ�ܣ�") ;
		}
		writeJson(res,response);
	}
	
	
	/**
	 * ��Ʒ֪ʶ��ѯ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doQueryKnowledge(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String sid=request.getParameter("sid");
		String keyWord = request.getParameter("keyWord") ;
		String keyId = request.getParameter("id") ;
		String strHttp = "http://"+request.getHeader("host");
		if(keyId!=null && keyId.length()>0){
			/*��ʾ��������*/
			Result result = new OAKnowCenterMgt().getFile(keyId) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				OAKnowledgeCenterFile file = (OAKnowledgeCenterFile) result.retVal ;
				List<MobileAttrs> fileList = new ArrayList<MobileAttrs>() ;
				MobileAttrs attrs = new MobileAttrs() ;
				attrs.setKey("��Ʒ����|txt") ;
				attrs.setValue(file.getFileTitle()) ;
				fileList.add(attrs) ;
				attrs = new MobileAttrs() ;
				attrs.setKey("��Ʒ����|html") ;
				attrs.setValue(file.getDescription().replaceAll("src=\"/upload/", "src=\""+strHttp+"/upload/")) ;
				fileList.add(attrs) ;
				MobileDetail detail = new MobileDetail() ;
				detail.setData(fileList) ;
				detail.setType(2) ;
				writeJson(detail, response) ;
			}
		}else if(keyWord==null || keyWord.length()==0){
			MobileQuery query = new MobileQuery() ;
			List<MobileParam> listCon = new ArrayList<MobileParam>() ;
			MobileParam param = new MobileParam() ;
			param.setFormat("TEXT") ;
			param.setId("keyWord") ;
			param.setName("�ؼ���") ;
			param.setEnabledNull(false) ;
			listCon.add(param) ;
			query.setParameters(listCon) ;
			query.setTarget("doQueryKnowledge") ;
			query.setType(3) ;
			writeJson(query, response) ;
		}else if(keyWord!=null){
			OnlineUser user = OnlineUserInfo.getUser(sid) ;
			Result result = new OAKnowCenterMgt().query(null, sid, user.getDeptId(), "", keyWord, 1, 10000) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				List<OAKnowledgeCenterFile> fileList = (List<OAKnowledgeCenterFile>) result.retVal ;
				MobileListData listData = new MobileListData() ;
				if(fileList!=null && fileList.size()>0){
					listData.setLayout(4) ;
					listData.setType(1) ;
					listData.setParameterKey("id") ;
					listData.setParameterKey2("reportNumber") ;
					listData.setValuename1("title") ;
					//��ʾ����
					List<Properties> datas = new ArrayList<Properties>() ;
					for(OAKnowledgeCenterFile file : fileList){
						Properties fileName = new Properties() ;
						fileName.setProperty("id", file.getid()) ;
						fileName.setProperty("title", file.getFileTitle()) ;
						fileName.setProperty("target", "doQueryKnowledge") ;
						fileName.setProperty("reportNumber", "OAKnowledgeCenterFile") ;
						datas.add(fileName) ;
					}
					listData.setData(datas) ;
				}
				writeJson(listData, response) ;
			}
		}
	}
	
	/**
	 * ������������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void employeeGoal(final HttpServletRequest request, final HttpServletResponse response)
		throws ServletException, IOException {
		
		String sid=request.getParameter("sid");
		String keyId = request.getParameter("id") ;
		String strHttp = "http://"+request.getHeader("host");
		
		Result result = mgt.queryCompanyGoal() ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
			List<Long[]> goalList = (List<Long[]>) result.retVal ;
			for(Long[] goal : goalList){
				defaultcategorydataset.addValue(goal[1], "���", goal[0]+"��") ;
				defaultcategorydataset.addValue(goal[2], "Ŀ��", goal[0]+"��") ;
				defaultcategorydataset.addValue(goal[3], "����Ŀ��", goal[0]+"��") ;
				defaultcategorydataset.addValue(goal[4], "���Ŀ��", goal[0]+"��") ;
			}
			
			String path = request.getSession().getServletContext().getRealPath("upload") ;
			boolean chart = createLineChart(path, defaultcategorydataset) ;
			if(chart){
				List<MobileAttrs> fileList = new ArrayList<MobileAttrs>() ;
				MobileAttrs attrs = new MobileAttrs() ;
				attrs.setKey("ͼƬ|jpg") ;
				attrs.setValue(GlobalsTool.imageBase64(path+"/LineChart.jpg")) ;
				fileList.add(attrs) ;
				MobileDetail detail = new MobileDetail() ;
				detail.setData(fileList) ;
				detail.setType(2) ;
				writeJson(detail, response) ;
			}
		}
	}
	
	/**
	 * ����ͳ��ͼƬ
	 * @return
	 */
	public boolean createLineChart(String path,DefaultCategoryDataset categorydata) {
		try {
			//ƽ��
			JFreeChart imgChart = ChartFactory.createLineChart("", "", "",categorydata, PlotOrientation.VERTICAL, true, true, false);
			imgChart.setBackgroundPaint(Color.white);
			imgChart.setBorderVisible(true);// �߿�ɼ�
			imgChart.getLegend().setItemFont(new Font("����", Font.PLAIN, 12));
			CategoryPlot categoryplot = (CategoryPlot) imgChart.getPlot();
			categoryplot.setBackgroundPaint(Color.lightGray);
			categoryplot.setRangeGridlinePaint(Color.white);
			
			// Y��
			NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
			numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			// �Ƿ���ʾ���
			numberaxis.setAutoRangeIncludesZero(false);
			numberaxis.setAutoTickUnitSelection(true);
			//numberaxis.setTickUnit(new NumberTickUnit(10d)) ;
			// ���Y�������������
			numberaxis.setLabelFont(new Font("sans-serif", Font.PLAIN, 14));
			
			// x��
			CategoryAxis domainAxis = (CategoryAxis) categoryplot.getDomainAxis();
			// ���x����������������
			domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));
			// ���x�������������
			domainAxis.setLabelFont(new Font("����", Font.PLAIN, 14));
			// ������ʾX��̶�
			domainAxis.setTickMarksVisible(true);
			//����X��45��
			domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
			
			LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();// ���ݵ�
			// series �㣨�����ݵ㣩�ɼ�
			lineandshaperenderer.setBaseShapesVisible(true);
			// ��ʾ���ݵ������
			lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator()); 
			// ��ʾ����ͼ���ϵ�����
			lineandshaperenderer.setBaseItemLabelsVisible(true);
			FileOutputStream fos = new  FileOutputStream(path+"//LineChart.jpg");
			ChartUtilities.writeChartAsJPEG(fos,1, imgChart, 500,300,null);
			fos.close();
			return true ;
		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
	}
	
	/**
	 * ͬ����Ϣ 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private void syncInfo(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String tableName = request.getParameter("tableName") ;
		String fieldName = request.getParameter("fieldName");//GoodsFullName,GoodsSpec
		
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		//Result result = mgt.loadTableInfo(tableName) ;
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,tableName);
		if(tableInfo != null){
			StringBuilder json = new StringBuilder().append("{tableInfo:[") ;
			json.append("{fieldName:'GoodsNumber',fieldDisplay:'��Ʒ���'},") ;
			json.append("{fieldName:'GoodsFullName',fieldDisplay:'��Ʒ����'},") ;
			json.append("{fieldName:'GoodsSpec',fieldDisplay:'��Ʒ���'},") ;
			json.append("{fieldName:'BarCode',fieldDisplay:'������'},") ;
			json.append("{fieldName:'BaseUnit',fieldDisplay:'��λ'},") ;
			json.append("{fieldName:'Price',fieldDisplay:'�۸�'}") ;
			json.append("]") ;
			Result result = mgt.getTableData(tableInfo) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null){
				json.append(",tableData:["+result.retVal+"]") ;
			}
			json.append("}") ;
			out.print(json.toString()) ;
		}
	}
	
	/**
	 * ���浥�ݣ���������������������ύ�����к������ύ�� 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveValue(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		MobileResult result = new MobileResult() ;
		try{
			
			String userId = request.getParameter("sid") ;
			String json =  request.getParameter("json") ;
//			ServletInputStream in = request.getInputStream();
//			DataInputStream dis = new DataInputStream(in);
//			byte[] bt = new byte[1024];
//			byte[] btAll = new byte[0];
//			int count = 0;
//			while ((count = dis.read(bt)) > 0) {
//				byte[] temp = btAll;
//				btAll = new byte[temp.length + count];
//				System.arraycopy(temp, 0, btAll, 0, temp.length);
//				System.arraycopy(bt, 0, btAll, temp.length, count);
//			}
//			String json = new String(btAll);
//			json = URLDecoder.decode(json);
//			json = "{\"StockCode\":\"00001\",\"SaveType\":\"BarCode\",\"CompanyCode\":\"00001\",\"item\":[{\"Qty\":\"3\",\"GoodsCode\":\"00052\",\"Amount\":\"294\",\"Price\":\"98\"},{\"Qty\":\"1\",\"GoodsCode\":\"00060\",\"Amount\":\"5\",\"Price\":\"5\"},{\"Qty\":\"1\",\"GoodsCode\":\"00063\",\"Amount\":\"55\",\"Price\":\"55\"},{\"Qty\":\"1\",\"GoodsCode\":\"00064\",\"Amount\":\"22\",\"Price\":\"22\"}]}";
			SalesOutStock sales = gson.fromJson(json, SalesOutStock.class) ;
			if (!StringUtils.isNotBlank(sales.getCompanyCode()) || !StringUtils.isNotBlank(sales.getStockCode())) {
				HashMap<String, String> empMap = new UserMgt().getEmployee2(userId);
				if(empMap == null || !StringUtils.isNotBlank(empMap.get("CompanyCode")) || !StringUtils.isNotBlank(empMap.get("StockCode"))){
					result.setCode(-1); 
					result.setDesc("������ҳ�˵�ְԱ������������Ĭ�ϲֿ��������λ") ;
					writeJson(result, response) ;
					return ;
				}
				sales.setCompanyCode(empMap.get("CompanyCode"));
				sales.setStockCode(empMap.get("StockCode"));				
			}
			sales.setEmployeeID(userId);
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			LoginBean login = getLoginBean(request);
			HashMap values =  convertSalesOutStockData(allTables, login, sales) ;

			String saveDraft = "";
    		if ("true".equals(GlobalsTool.getSysSetting("MobileBillToDraf"))) {	// �Ƿ��ݸ�
    			saveDraft = "saveDraft";
    		}

			Result rs = execDefineAdd(request, values, "tblSalesOutStock", saveDraft);

			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				String msg = getDefineErrMsg(request, rs);
				result.setCode(rs.getRetCode());
				result.setDesc(msg);
			} else {
				Result stockRs = mgt.queryStock(sales.getStockCode());
				String stock = (String)(((List<Object[]>)stockRs.getRetVal()).get(0))[0];
				Result comRs = mgt.queryCompany(sales.getCompanyCode());
				String comName = (String)(((List<Object[]>)comRs.getRetVal()).get(0))[0];
				JsonObject j = new JsonObject();
				j.addProperty("code", 0);
				j.addProperty("company", GlobalsTool.getCompanyName("zh_CN"));
				j.addProperty("emp", OnlineUserInfo.getUser(userId).name);
				j.addProperty("stock", stock);
				j.addProperty("comName", comName);
				j.addProperty("billNo", values.get("billNo")+"");
				writeJson(j, response);
				return;
			}
		}catch (Exception e) {
			e.printStackTrace() ;
			result.setCode(-1); 
			result.setDesc("���ʧ��") ;
		}
		writeJson(result, response) ;
	}

	protected String getDefSQLMsg(HttpServletRequest request, String o) {
		String msg = "";
		String[] os = (String[]) o.split(",");
		if (os.length == 1) {
			String key = os[0].replace("@RepComma:", ",");
			msg = getMessageResources(request, key);
			if (msg == null || msg.length() == 0) {
				msg = key;
			}
		} else if (os.length == 2) {
			msg = getMessageResources2(request, os[0].replace("@RepComma:", ","), os[1]
					.replace("@RepComma:", ","));
		} else if (os.length == 3) {
			msg = getMessageResources3(request, os[0].replace("@RepComma:", ","), os[1]
					.replace("@RepComma:", ","), os[2].replace("@RepComma:",
					","));
		} else if (os.length >= 4) {
			msg = getMessageResources4(request, os[0].replace("@RepComma:", ","), os[1]
					.replace("@RepComma:", ","), os[2].replace("@RepComma:",
					","), os[3].replace("@RepComma:", ","));
		}
		return msg;
	}
	
//	
//	/**
//	 * ��ȡ�û���Ϣ
//	 * 
//	 * @param userId
//	 * @return
//	 */
//	public LoginBean queryLoginBeanByUserId(String userId,Locale local) {
//		Result result = new UserMgt().detail(userId);
//		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			EmployeeBean bean = (EmployeeBean) result.getRetVal();
//			LoginBean login = new LoginBean(bean.getId(), bean.getSysName());
//			login.setDepartCode(bean.getDepartmentCode());
//			login.setSunCmpClassCode(bean.getSCompanyID());
//			login.setEmpFullName(bean.getEmpFullName());
//			login.setDefStyle(bean.getDefSys());
//			
//			Hashtable sessionSet = BaseEnv.sessionSet;
//			if(sessionSet.get(login.getId())==null){
//		        Hashtable sess = new Hashtable();
//		        sess.put("SCompanyID", login.getSunCmpClassCode());
//		        //��ǰ��¼��֧�����Ļ���ڼ�
//				String nowPeriod;
//				int nowYear = -1;
//				int nowMonth = -1;
//				AccPeriodBean accBean = null;
//				Result rs = new SysAccMgt().getCurrPeriod(login
//						.getSunCmpClassCode());
//				if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//					accBean = (AccPeriodBean) rs.getRetVal();
//					nowPeriod = "-1";
//				} else {
//					accBean = (AccPeriodBean) rs.getRetVal();
//					nowPeriod = String.valueOf(accBean.getAccPeriod());
//					nowYear = accBean.getAccYear();
//					nowMonth = accBean.getAccMonth();
//				}
//				int nowDay = Calendar.getInstance().get(Calendar.DATE);
//                
//		        sess.put("AccPeriod",accBean);
//		        sess.put("NowPeriod", nowPeriod);
//		        sess.put("NowYear", nowYear);
//		        sess.put("NowMonth", nowMonth);
//		        sess.put("NowDay", nowDay);
//		        sess.put("UserId", login.getId());
//		        sess.put("UserName", login.getEmpFullName());
//		        sess.put("Local", local);
//		        sess.put("BillOper", "");
//		        sessionSet.put(login.getId(), sess);
//			}
//			return login;
//		}
//		return null;
//	}
	
	/**
	 * ת�������۳����Ӧ������
	 * @param listBean
	 * @return
	 */
	private HashMap convertSalesOutStockData(Hashtable allTables,LoginBean login,SalesOutStock sales){
		HashMap values = new HashMap() ;
		DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(allTables, "tblSalesOutStock", "BillNo") ;
		values.put("BillNo", fieldBean==null?"":fieldBean.getDefValue()) ;
		values.put("BillDate", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)) ;
		values.put("CompanyCode", sales.getCompanyCode()) ;	/*�ŵ�Ĭ��������λ*/
		values.put("EmployeeID", sales.getEmployeeID()) ;
		OnlineUser user = OnlineUserInfo.getUser(sales.getEmployeeID()) ;
		values.put("DepartmentCode", user.getDeptId()) ;
		values.put("StockCode", sales.getStockCode()) ;		/*�ŵ�Ĭ�ϲֿ�*/
		values.put("InVoiceType", "1") ;
		values.put("billType", "sales") ;
		values.put("SCompanyID", login.getSunCmpClassCode()) ;
		ArrayList childList = new ArrayList();
		values.put("TABLENAME_tblSalesOutStockDet", childList);
		double allAmount = 0 ;
		for(Item item : sales.getItem()){
			HashMap hm = new HashMap() ;
			hm.put("GoodsCode", item.getGoodsCode()) ;
			hm.put("Qty", item.getQty()) ;
			hm.put("Price", item.getPrice()) ;	
			
//			double disAmount = GlobalsTool.round(Double.parseDouble(item.getAmount()), GlobalsTool.getDigitsOrSysSetting("tblSalesOutStockDet","DisAmount")) ;
//			double amount = GlobalsTool.round(Double.parseDouble(item.getAmount()), GlobalsTool.getDigitsOrSysSetting("tblSalesOutStockDet","Amount")) ;
			hm.put("Amount", item.getAmount()) ;
			hm.put("Tax", 0) ;
			hm.put("TaxPrice", item.getPrice()) ;
			hm.put("TaxAmount", item.getAmount()) ;
			hm.put("Discount", 100) ;
			hm.put("DisPrice", item.getPrice()) ;
			hm.put("DisAmount", 0) ;
			hm.put("DisBackAmt", item.getAmount()) ;
			if("Seq".equals(sales.getSaveType())){
				hm.put("Seq", item.getSeqBarCode()+"~") ;
				hm.put("BatchNo", "") ;
				hm.put("Inch", "") ;
				hm.put("color", "") ;
				hm.put("yearNO", "") ;
				hm.put("Hue", "") ;
				hm.put("Availably", "") ;
				hm.put("ProDate", "") ;
			}
			allAmount += Double.parseDouble(item.getAmount()) ;
			hm.put("StockCode", sales.getStockCode()) ;	// ��ϸĬ�ϲֿ�(ŷ����) PWY
			childList.add(hm) ;
		}
		double totalTaxAmount = GlobalsTool.round(allAmount, GlobalsTool.getDigitsOrSysSetting("tblSalesOutStock","TotalTaxAmount")) ;
		values.put("TotalTaxAmount", totalTaxAmount) ;
		//values.put("AlrAccAmt", allAmount) ;
		values.put("AccAmt", allAmount) ;
		values.put("FactIncome", allAmount) ;
		values.put("DiscountAmount", 0) ;		
		return values ;
	}
	
	/**
	 * ���кŷ��ص��� 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getPrice(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		String seq = request.getParameter("inputValue") ;
		
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		Result result = mgt.getPrice(seq) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null){
			HashMap<String, String> values = (HashMap<String, String>) result.retVal ;
			StringBuilder json = new StringBuilder() ;
			json.append("{goodsCode:'"+values.get("GoodsCode")+"',price:"+values.get("Price")) ;
			String attribute = "" ;
			String batchNo = values.get("BatchNo") ;
			if(batchNo!=null && batchNo.trim().length()>0){
				attribute += "{attDisplay:'����',attValue:'"+batchNo+"'}," ;
			}
			String inch = values.get("Inch") ;
			if(inch!=null && inch.trim().length()>0){
				attribute += "{attDisplay:'�ߴ�',attValue:'"+inch+"'}," ;
			}
			String yearNo = values.get("yearNo") ;
			if(yearNo!=null && yearNo.trim().length()>0){
				attribute += "{attDisplay:'���',attValue:'"+yearNo+"'}," ;
			}
			String hue = values.get("Hue") ;
			if(hue!=null && hue.trim().length()>0){
				attribute += "{attDisplay:'��ɫ',attValue:'"+hue+"'}," ;
			}
			String proDate = values.get("ProDate") ;
			if(proDate!=null && proDate.trim().length()>0){
				attribute += "{attDisplay:'��������',attValue:'"+proDate+"'}," ;
			}
			String availably = values.get("Availably") ;
			if(availably!=null && availably.trim().length()>0){
				attribute += "{attDisplay:'��������',attValue:'"+availably+"'}," ;
			}
			if(attribute.length()>0){
				attribute = attribute.substring(0, attribute.length()-1) ;
				json.append(",attribute:["+attribute+"]}") ;
			}else{
				json.append("}") ;
			}
			out.print(json.toString()) ;
		}else{
			MobileResult mresult = new MobileResult() ;
			mresult.setCode(-1) ;
			mresult.setDesc("�����ڴ����к�") ;
			writeJson(mresult, response) ;
		}
	}
//	
//	/**
//	 * mj ��Ѷ�ͻ���������ύ��Ϣ �����ն�����ٷ�������ȡ��Ϣ�������ն���ʾ��Ϣ
//	 * @param request
//	 * @param response
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	public void getMsg(final HttpServletRequest request, final HttpServletResponse response)
//			{
//		try{
//			String UUID = request.getParameter("UUID");//ͨ��Ψһʶ����
//			String sendId = request.getParameter("sendname") ;//�����ʻ�
//			String msgId = request.getParameter("msgid");//��Ѷ��Ϣid
//			sendId = URLDecoder.decode(sendId,"utf-8");
//			sendId = sendId.split("@")[0];
//			//String msgid = request.getParameter("msgid");//��Ѷ��Ϣid
//			String receiveId = request.getParameter("receive");//�����˻�
//			receiveId = URLDecoder.decode(receiveId,"utf-8");
//			receiveId = receiveId.split("@")[0];
//			Result resultSend = new UserMgt().queryEmployeeBySysName(sendId);
//			EmployeeBean beanSend = new EmployeeBean();
//			if (resultSend.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//				ArrayList<EmployeeBean> empList = (ArrayList<EmployeeBean>)resultSend.getRetVal();
//				if(empList.size() > 0){
//					beanSend = empList.get(0);
//				}
//			}
//			
//			Result resultRece = new UserMgt().queryEmployeeBySysName(receiveId);
//			EmployeeBean beanRece = new EmployeeBean();
//			if (resultRece.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//				ArrayList<EmployeeBean> empList = (ArrayList<EmployeeBean>)resultRece.getRetVal();
//				if(empList.size() > 0){
//					beanRece = empList.get(0);
//				}
//			}
//			String content = request.getParameter("content");//����
//			//LoginBean login = queryLoginBeanByUserId(receiveId,getLocale(request));
//	    	//String receiveName = login.getEmpFullName();//�����˻���������
//	    	//String sendName = queryLoginBeanByUserId(sendId,getLocale(request)).getEmpFullName();
//			String receiveName = beanRece.getEmpFullName();
//			String sendName = beanSend.getEmpFullName();
//			BaseEnv.log.debug("����Ϊ��"+content);
//			if(content!=null){
//	    		content = URLDecoder.decode(content,"utf-8");
//	    		//��� ��������������� �˿��滻 ���顣
//	    		String reg = "<i@[\\d]+\\.gif>";
//	    		Pattern pattern = Pattern.compile(reg);  
//	    		Matcher matcher = pattern.matcher(content);  
//	    		while (matcher.find()) {
//	    			String str = matcher.group(0);
//	    			BaseEnv.log.debug(str);
//	    			if(str!=null && str.contains("<i@")){
//	    				int e = str.lastIndexOf(".gif");
//	            		String strNum = str.substring(3,e) ;
//	            		BaseEnv.log.debug(strNum);
//	            		int imgNum = Integer.parseInt(strNum);
//	            		BaseEnv.log.debug(imgNum);
//	            		String replaceStr = "<img style=\"cursor: pointer;\" onclick=\"zoomImage(this)\" border=\"0\" alt=\"\" src=\"/js/plugins/emoticons/images/"+imgNum+".gif\" />";
//	            		content = content.replace(str, replaceStr);
//	            	}
//	    		}
//	    		//content = GlobalsTool.toChinseChar(content);
//	    	}
//			
//	    	LoginBean loginBean = null;
//	    	Object o = request.getSession().getAttribute("LoginBean");
//			if (o != null) {
//				loginBean =  (LoginBean) o;
//			}
//	    	Result result = new Result() ;
//	    	MessageBean bean = new MessageBean() ;
//	        bean.setId(msgId) ;
//	        bean.setSend(beanSend.getId()) ;
//	        bean.setSendName(sendName) ;
//	        bean.setReceive(beanRece.getId()) ;
//	        bean.setReceiveName(receiveName) ;
//	        bean.setContent(content) ;
//	        bean.setOperType("person") ;
//	        bean.setCreateBy(loginBean == null ? beanSend.getId() : loginBean.getId()) ;
//	        bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
//	        bean.setStatus("read") ;
//	        bean.setRelationId(beanRece.getId()) ;
//	        bean.setAffix(null) ;
//	        String reId=IDGenerater.getId();
//	    	result = msg.sendMsg(reId,bean) ;
//	    	MobileResult results = new MobileResult() ;
//	        if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
//	        	results.setCode(1) ;
//	        	BaseEnv.log.debug("�ͻ���������ύ��Ϣ�ɹ���");
//	        	results.setDesc("�ͻ���������ύ��Ϣ�ɹ���") ;
//	        }else{
//	        	results.setCode(0) ;
//	        	BaseEnv.log.debug("�ͻ���������ύ��Ϣʧ�ܣ�");
//	        	results.setDesc("�ͻ���������ύ��Ϣʧ�ܣ�") ;
//	        }
//	        
//	        writeJson(result, response) ;
//		}catch (Exception e) {
//			BaseEnv.log.debug("�ύ��Ϣ������ʧ��");
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * ��Ϣ���� mj
//	 * @param request
//	 * @param response
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	public void msgStatus(final HttpServletRequest request,
//			final HttpServletResponse response) {
//		try{
//			String UUID = request.getParameter("UUID");//ͨ��Ψһʶ����	
//			//String keycode = request.getParameter("keycode");//���ܴ�	���ܴ��Զ���(Ĭ��Ϊ gzRN53VWRF9BYUXo)
//			Result rs = msg.updateMsgByApp(UUID);
//			BaseEnv.log.debug("");
//			MobileResult results = new MobileResult();
//			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//				results.setCode(1);
//				results.setDesc("������Ϣ�ɹ���");
//				BaseEnv.log.debug("������Ϣ�ɹ�");
//				
//
//	    		// ֪ͨAIO�ͻ��˲�������ʾ��Щ��Ϣ	PWY
//	    		List msgList = (List) rs.getRetVal() ;
//	    		if (msgList != null) {
//	    			for(int i=0;i<msgList.size();i++){
//	    				MessageBean message = (MessageBean) msgList.get(i) ;
//	    				MSGConnectCenter.CancelMsg(CancelMsgReq.TYPE_CHAT, getLoginBean(request).getId(), message.getId());
//	    			}
//	    		}
//			} else {
//				results.setCode(0);
//				results.setDesc("������Ϣʧ��!");
//				BaseEnv.log.debug("������Ϣʧ��");
//			}
//			writeJson(results, response) ;
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
   
    
	/**
	 * ��ȡģ����Ϣ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getModule(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// ���ﲻУ��Ȩ������Ȩ�������ݷ���
		
		String userId = request.getParameter("sid");
		LoginBean login = getLoginBean(request);

		JsonArray jarrModule = new JsonArray();		
		ClientSetingMgt moduleMgt = new ClientSetingMgt() ;
		Result result = moduleMgt.queryMyModules(login) ;
		ArrayList<String[]> moduleList = (ArrayList<String[]>)result.retVal;
		
		//����Ȩ�޻�ȡmoduleId ��viewId
		List<ClientModuleViewBean> moduleViewList = new ArrayList<ClientModuleViewBean>();//�����ͼlist
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && moduleList !=null && moduleList.size()>0){
			for(String[] module : moduleList){

				result = moduleMgt.queryModuleViewsByModuleId(login, module[1]);//����moduleId��Ȩ�޻�ȡ��ͼlist
				moduleViewList = (List<ClientModuleViewBean>)result.retVal;
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && moduleViewList!=null && moduleViewList.size()>0){
					JsonArray jarrView = new JsonArray();
					for (ClientModuleViewBean view : moduleViewList) {
						JsonObject jsonView = new JsonObject();
						jsonView.addProperty("id", view.getId());
						jsonView.addProperty("viewName", view.getViewName());
						jarrView.add(jsonView);
					}
					
					JsonObject jsonModule = new JsonObject();
					jsonModule.addProperty("id", (String)module[1]);
					jsonModule.addProperty("moduleName", (String)module[0]);
					jsonModule.add("viewList", jarrView);
					
					jarrModule.add(jsonModule);
				}
			}			
		}
		
		if (jarrModule.size() == 0) {
			MobileResult rt = new MobileResult();
			rt.setCode(-1);
			rt.setDesc("û���κ�ģ��鿴Ȩ��");
			writeJson(rt, response);
		} else {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("moduleList", jarrModule);
			
			writeJson(json, response);
		}	
		
		
	}
	/**
	 * �ͻ��б�
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
����op=clientDet&sid=XXXX&lastUpdateTime=2013-05-28 09:00:00
Ӧ��
{
telList:
	[
		{id:'634bf574_1209111449203232401',userName:'����',mobile:'13714037528',telephone:'075527299442',clientId:'00492571_1003101027230953278',clientName:'�����п�Ψ�ѿƼ���չ���޹�˾',lastUpdateTime:'2012-09-11 14:49:20',lastContractTime:'2012-07-19 10:46:52'}
		{id:'634bf574_1209111449203232401',userName:'����',mobile:'13714037528',telephone:'075527299442',clientId:'00492571_1003101027230953278',clientName:'�����п�Ψ�ѿƼ���չ���޹�˾',lastUpdateTime:'2012-09-11 14:49:20',lastContractTime:'2012-07-19 10:46:52'}
	]
}
	 */
	public void getCRMClientDet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/CRMClientAction.do");
		if (mop == null) {
			MobileResult rt = new MobileResult();
			rt.setCode(-1);
			rt.setDesc("�޲鿴�ͻ��б�Ȩ��");
			writeJson(rt, response);
			return;
		}
		
		if (checkParameter("lastUpdateTime", request, response) == false) {
			return;
		}
		
		String userId = request.getParameter("sid");
		String lastUpdateTime = request.getParameter("lastUpdateTime");
//		lastUpdateTime = "";


		Result result = mgt.getCRMClientDet(getLoginBean(request), lastUpdateTime, mop);
		
		JsonObject json = new JsonObject();
		json.addProperty("code", 0);
		JsonArray jarr = new JsonArray();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			List clientList = (List) result.retVal;
			for (int i = 0; i < clientList.size(); i++) {
				JsonObject j = new JsonObject();
				Object[] obj = (Object[]) clientList.get(i);
				String sLastUpdateTime = (String)obj[6];
				if (null == sLastUpdateTime || "".equals(sLastUpdateTime) || "null".equals(sLastUpdateTime))
					sLastUpdateTime = "1970-01-01 00:00:00";
				String sLastContractTime = (String)obj[7];
				if (null == sLastContractTime || "".equals(sLastContractTime) || "null".equals(sLastContractTime))
					sLastContractTime = (String)obj[8];

				j.addProperty("id", (String)obj[0]);
				j.addProperty("userName", (String)obj[1]);
				j.addProperty("mobile", (String)obj[2]);
				j.addProperty("telephone", (String)obj[3]);
				j.addProperty("clientId", (String)obj[4]);
				j.addProperty("clientName", (String)obj[5]);
				j.addProperty("lastUpdateTime", sLastUpdateTime);
				j.addProperty("lastContractTime", sLastContractTime);
				j.addProperty("moduleId", (String)obj[9]);
				
				jarr.add(j);
			}
		}
		json.add("telList", jarr);
		writeJson(json, response);
	}
	
	/**
	 * ָ��ģ���µĿͻ��б�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * op=ModuleClient&moduleId=XXX&lastId=XXX
	 */
	public void getCRMClientInModule(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {		
		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/CRMClientAction.do");
		if (mop == null) {
			MobileResult rt = new MobileResult();
			rt.setCode(-1);
			rt.setDesc("�޲鿴�ͻ��б�Ȩ��");
			writeJson(rt, response);
			return;
		}

		if (checkParameter("sid,moduleId,lastId", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String moduleId = request.getParameter("moduleId");
		String lastId = request.getParameter("lastId");
		
//		// ��������
//		moduleId = "1";
//		lastId = "";
		
		Result result = mgt.getCRMClientInModule(getLoginBean(request), moduleId, mop, lastId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("listClient", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}

	/**
	 * �ͻ�����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 *
����op=clientInfo&sid=XXXX&clientId=XXXXX&moduleId=XXXX&viewId=XXXXX
Ӧ��
{"code":-1,"desc":"ģ�岻����"}
{
"code":0,
"info":
	[
		{"Key":"ҵ������","Val":"","Group":"������Ϣ"},
		{"Key":"�ͻ�����","Val":"","Group":"������Ϣ"},
		{"Key":"�����̶�","Val":"��","Group":"������Ϣ"},
		{"Key":"�ͻ���Դ","Val":"��������","Group":"������Ϣ"},
		{"Key":"��������","Val":"Ŀ��ͻ�","Group":"������Ϣ"},

		{"Key":"�ͻ����","Val":"�����ؼ���:���� ��Դ��ַ http://www.baidu.com/link?url=nmhTGJqjJ4zBBpC8yDF8xDh8vibi1UIgHioEbodO  ","Group":"������Ϣ"},
		{"Key":"�ͻ����","Val":"","Group":"������Ϣ"},
		{"Key":"����ʱ��","Val":"","Group":"������Ϣ"},
		{"Key":"���۽��~","Val":"","Group":"������Ϣ"},
		{"Key":"��ֵ˰��","Val":"","Group":"������Ϣ"},

		{"Key":"�ͻ���ַ","Val":"","Group":"��˾��Ϣ"},
		{"Key":"�ͻ�����","Val":"��������������Ƽ����޹�˾","Group":"��˾��Ϣ"},
		{"Key":"�ͻ����","Val":"C13011860938","Group":"��˾��Ϣ"},
		{"Key":"�ͻ�����","Val":"","Group":"��˾��Ϣ"},
		{"Key":"�ͻ��绰","Val":"","Group":"��˾��Ϣ"},
		{"Key":"�ͻ���ҵ","Val":"","Group":"��˾��Ϣ"},
		{"Key":"��˾��ַ","Val":"","Group":"��˾��Ϣ"}
	],

"contact":
	[
		[
			{"Key":"��ϵ��","Val":"aaaaaa"},
			{"Key":"�绰","Val":""},
			{"Key":"�ֻ�","Val":""},
			{"Key":"�ʼ���ַ","Val":""},
			{"Key":"QQ","Val":""},
			{"Key":"��ע","Val":""},
			{"Key":"��ɫ","Val":"�ܼ�"}
		],

		[
			{"Key":"��ϵ��","Val":"��ϵ��1"},
			{"Key":"�绰","Val":"07562183079"},
			{"Key":"�ֻ�","Val":""},
			{"Key":"�ʼ���ַ","Val":""},
			{"Key":"QQ","Val":""},
			{"Key":"��ע","Val":""},
			{"Key":"��ɫ","Val":""}
		]
	]
}
	 */
	public void getCRMClientInfo(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/CRMClientAction.do");
		if (mop == null) {
			MobileResult rt = new MobileResult();
			rt.setCode(-1);
			rt.setDesc("�޲鿴�ͻ��б�Ȩ��");
			writeJson(rt, response);
			return;
		}
		if (checkParameter("sid,clientId,moduleId,viewId", request, response) == false) {
			return;
		}
		
		String userId = request.getParameter("sid");
		String clientId = request.getParameter("clientId");
//		if (clientId == null)
//			clientId = "6ca3565e_1001092020240040062";
		String moduleId = request.getParameter("moduleId");
    	if (null != moduleId && !"".equals(moduleId) && !"null".equals(moduleId))
    		moduleId = null;
		String viewId = request.getParameter("viewId");  
    	if (null != viewId && !"".equals(viewId) && !"null".equals(viewId))
    		viewId = null;

		LoginBean lg = getLoginBean(request);

		MobileResult rt = new MobileResult() ;

		CRMClientMgt clientMgt = new CRMClientMgt() ;
		ClientSetingMgt moduleMgt = new ClientSetingMgt() ;
		//�ӹ����ƻ������ͻ������.û��ģ��ID����ͼID,����clientId����moduleId��ģ���µ���ͼ.��ûȨ�޷���
		if((moduleId==null || "".equals(moduleId)) && (viewId==null || "".equals(viewId))){
			Result rest = clientMgt.findClientById(clientId);//����clientId����moduleId
			if(rest.retCode == ErrorCanst.DEFAULT_SUCCESS){
				List<Object> list = (List<Object>)rest.retVal;
				//��û����Ϣ���ش���
				if(list ==null || list.size()==0){
					rt.setCode(-1); 
					rt.setDesc("ģ�岻����") ;
					writeJson(rt, response);
					return;
				}else{
					moduleId = ((Object[])list.get(0))[0].toString();
				}
				rest = moduleMgt.queryModuleViewsByModuleId(lg,moduleId);//����moduleId�鿴��ͼȨ��
				if(rest.retCode == ErrorCanst.DEFAULT_SUCCESS){
					List<ClientModuleViewBean> moduleViewList = (ArrayList<ClientModuleViewBean>)rest.retVal;
					if(moduleViewList == null || moduleViewList.size()==0){
						//û�з��ش��� 
						rt.setCode(-1); 
						rt.setDesc("û����ͼȨ��") ;
						writeJson(rt, response);
						return;
					}else{
						viewId = moduleViewList.get(0).getId();
					}
				}else{
					rt.setCode(-1); 
					rt.setDesc("��ѯ��ͼȨ�޴���") ;
					writeJson(rt, response);
					return;
				}
			}else{
				rt.setCode(-1); 
				rt.setDesc("���ʵ���Ϣ�����ڣ������ѱ�ɾ��") ;
				writeJson(rt, response);
				return;
			}
		}else if(moduleId!=null && moduleId.length() > 0 && (viewId ==null || "".equals(viewId))){
			Result rest = moduleMgt.queryModuleViewsByModuleId(lg,moduleId);//����moduleId�鿴��ͼȨ��
			if(rest.retCode == ErrorCanst.DEFAULT_SUCCESS){
				List<ClientModuleViewBean> moduleViewList = (ArrayList<ClientModuleViewBean>)rest.retVal;
				if(moduleViewList == null || moduleViewList.size()==0){
					//û�з��ش���
					rt.setCode(-1); 
					rt.setDesc("û����ͼȨ��") ;
					writeJson(rt, response);
					return;
				}else{
					viewId = moduleViewList.get(0).getId();
				}
			}else{
				rt.setCode(-1); 
				rt.setDesc("��ѯ��ͼȨ�޴���") ;
				writeJson(rt, response);
				return;
			}
		}

		Result result=(new ClientSetingMgt()).detailCrmModule(moduleId);
		ClientModuleBean cBean=(ClientModuleBean) result.retVal;
		result = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleViewBean = (ClientModuleViewBean)result.retVal;
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        String sunClassCode = lg.getSunCmpClassCode();  
        Hashtable props = (Hashtable) request.getSession().getServletContext().
        getAttribute(BaseEnv.PROP_INFO);
    	DynDBManager dbmgt = new DynDBManager();
        Result rs = dbmgt.detail(cBean.getTableInfo().split(":")[0], map, clientId, sunClassCode,props,userId,true,null);
        HashMap clientMap=(HashMap)rs.retVal;
        String clientName = cBean.getTableInfo().split(":")[0];
        String clientDetName = cBean.getTableInfo().split(":")[1];
		request.setAttribute("clientMap", clientMap);
		String pageFields = moduleViewBean.getPageFields();
		List<FieldScopeSetBean> hiddenList = (List<FieldScopeSetBean>)clientMgt.findFieidScopes(lg.getDepartCode(), lg.getId(), "hidden",viewId).retVal;
		String hiddenFields="";
		if(hiddenList !=null){
			for(int j=0;j<hiddenList.size();j++){
				String str=hiddenList.get(j).getFieldsName();
				if(!hiddenFields.contains(str+",")){
					hiddenFields += str ;
				}
			}
		}
		if("1".equals(lg.getId())){
			hiddenFields="";
		}

		JsonObject json = new JsonObject();
		json.addProperty("code", 0);
		JsonArray jarr = new JsonArray();
		String []sFields = pageFields.split(",");
		String []sHiddens = hiddenFields.split(",");
		List<String> sContacts = new ArrayList<String>();
		List<String> sFieldsInfo = new ArrayList<String>();
		for (String sField : sFields) {
			boolean bAdd = true;
			for (String sHidden : sHiddens) {
				if (sField.equals(sHidden)) {
					bAdd = false;
					break;
				}
			}
			
			if (bAdd && sField.startsWith("contact")) {	// ��ϵ��
				sContacts.add(sField);
			} else {
				sFieldsInfo.add(sField);
			}
		
		}
		result = mgt.getCRMClientFieldInfo(sFieldsInfo, clientName);
		HashMap<String, Object[]> mapSorted = new HashMap<String, Object[]>();
		List<String> listGroup = new ArrayList<String>();
		String sLastGroup = "";
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			List list = (List) result.retVal;
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				String sField = (String)obj[0];
				String sGroup = (String)obj[1];
				mapSorted.put(sField, obj);
				if (!sLastGroup.equals(sGroup)) {
					listGroup.add(sGroup);
					sLastGroup = sGroup;
				}
				
			}
		}
		for (String sGroup : listGroup){
			for (String sField : sFieldsInfo) {						
				Object[] obj = mapSorted.get(sField);
				if (null == obj || !sGroup.equals((String)obj[1]))
					continue;
				
				JsonObject j = new JsonObject();
				DBFieldInfoBean bean = GlobalsTool.getFieldBean(clientName, sField);
				j.addProperty("Key", bean.getDisplay().get(this.getLocale(request).toString()));
				String sVal = clientMap.get(sField) == null ? "null" : clientMap.get(sField).toString();
				if (GlobalsTool.getFieldBean(clientName, sField).getInputType() == 1) {
					sVal = GlobalsTool.getEnumerationItemsDisplay(bean.getRefEnumerationName(), sVal, this.getLocale(request).toString());
				}
				if ("Trade".equals(sField) && !"null".equals(sVal)) {
					HashMap<String, String> tradeMap = (HashMap)request.getSession().getServletContext().getAttribute("workProfessionMap");
					sVal = tradeMap.get(clientMap.get(sField).toString());
				} else if ("District".equals(sField) && !"null".equals(sVal)) {
					HashMap<String, String> districtMap = (HashMap)request.getSession().getServletContext().getAttribute("districtMap");
					sVal = districtMap.get(clientMap.get(sField).toString());
				}
				j.addProperty("Val", sVal);
				j.addProperty("Group", sGroup);
				
				jarr.add(j);
			}
		}
		
		json.add("info", jarr);
		
		jarr = new JsonArray();
		String tblName = "TABLENAME_" + clientDetName;
		List<HashMap> listContact = (List)clientMap.get(tblName);
		for (HashMap m : listContact) {
			JsonArray ja = new JsonArray();
			for (String sContact : sContacts) {
				sContact = sContact.replace("contact", "");
				JsonObject j = new JsonObject();
				DBFieldInfoBean bean = GlobalsTool.getFieldBean(clientDetName, sContact);
				j.addProperty("Key", bean.getDisplay().get(this.getLocale(request).toString()));
				String sVal = m.get(sContact) == null ? "null" : m.get(sContact).toString();
				if (GlobalsTool.getFieldBean(clientDetName, sContact).getInputType() == 1)
					sVal = GlobalsTool.getEnumerationItemsDisplay(bean.getRefEnumerationName(), sVal, this.getLocale(request).toString());
				j.addProperty("Val", sVal);
				ja.add(j);
			}
			jarr.add(ja);
		}
		json.add("contact", jarr);

		writeJson(json, response);
		
	}
	/**
	 * ���ӿͻ�
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addClient(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		MOperation mop = (MOperation)getLoginBean(request).getOperationMap().get("/CRMClientAction.do");
		if (mop == null) {
			MobileResult rt = new MobileResult() ;	
			rt.setCode(-1); 
			rt.setDesc("�޿ͻ��б�Ȩ��") ;
			writeJson(rt, response);
			return;
		}
		if (checkParameter("ModuleId", request, response) == false) {
			return;
		}
		String userId = request.getParameter("sid");
		String moduleId = request.getParameter("ModuleId");

		if (null == moduleId || "".equals(moduleId))
			moduleId = "1";
		String json = request.getParameter("json");
		AddClientBean bean = gson.fromJson(json, AddClientBean.class) ;
//		if (bean == null) {
//			bean = new AddClientBean();
//			bean.setClientName("ClientName");
//			bean.setClientRemark("ClientRemark");
//			bean.setUserName("UserName");
//			bean.setTelephone("Telephone");
//		}
		MobileResult rt = new MobileResult() ;
		rt.setCode(-1);
		rt.setDesc("���ʧ��");

		
		if (bean == null || userId == null) {
			writeJson(rt, response);
			return;
		}
		String Attachment = "" ;
		if(bean.getLocImage()!=null && bean.getLocImage().length>0){
			for(String strImage : bean.getLocImage()){
				String strDay = String.valueOf(System.currentTimeMillis() + new Random().nextInt(100));
				
				BASE64Decoder decoder = new BASE64Decoder() ;
				try { 
					byte[] byteImage = decoder.decodeBuffer(strImage); 
					for(int i=0;i<byteImage.length;++i){
						if(byteImage[i]<0){
							byteImage[i]+=256;
						}
					}
					String imgFilePath = BaseEnv.FILESERVERPATH+"/affix/CRMClientInfo/" + strDay +".jpg" ; 
					File imgFile = new File(imgFilePath) ;
					if(!imgFile.getParentFile().exists()){
						imgFile.getParentFile().mkdirs() ;
					}
					Attachment += strDay+".jpg;" ;
					OutputStream out = new FileOutputStream(imgFilePath);
					out.write(byteImage); 
					out.flush(); 
					out.close(); 
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		bean.setAttachment(Attachment);
				
		
		Result result = mgt.addClient(getLoginBean(request), moduleId, bean);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			rt.setCode(0);
			rt.setDesc("��ӳɹ�");
		}

		writeJson(rt, response);
//		StringBuilder url = new StringBuilder();
//		url.append("/CRMClientAction.do?");
//		url.append("userId=").append(userId);
//		url.append("&addFromMobile=true");
//		url.append("&ModuleId=").append(ModuleId);
//		url.append("&ClientName=").append(bean.getClientName());
//		url.append("&UserName=").append(bean.getUserName());
//		url.append("&Telephone=").append(bean.getTelephone());
//		url.append("&ClientRemark=").append(bean.getClientRemark());
//		url.append("&Attachment=").append(Attachment);
//		request.getRequestDispatcher(url.toString()).forward(request, response);
	}
	
	/**
	 * �鿴�����ƻ��б�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void showWorkPlanList(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		if (checkParameter("type,beginTime,endTime", request, response) == false) {
			return;
		}
		String userId = request.getParameter("sid");
		String type = request.getParameter("type");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		
//		// ��������
//		type = "month";
//		beginTime = "2013-07-10 00:00:00";
//		endTime = "2013-07-12 23:59:59";
		
		// �ȼ����Լ���
		Result result = mgt.queryMyWorkPlanList(userId, type, beginTime, endTime);
		JsonArray jarr = new JsonArray();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			jarr = (JsonArray)result.retVal;
		}
		
		// �ټ���֪���˵�
		result = mgt.queryKnowPersonWorkPlanList(userId, type, beginTime, endTime);		
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonArray jarr2 = (JsonArray)result.retVal;
			jarr.addAll(jarr2);
		}
		
		
		if (jarr.size() > 0) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", jarr);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}

	/**
	 * ��ѯ�����ƻ��б�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
����
op=showWorkPlan
&planId=XXXX
Ӧ��
{
    "code": 0,
    "data": 
        {
            "empId": "1",
            "empName": "admin",
            "planId": "bbbb975d_1306040922072180007",
            "planType": "week",
            "title": "2013���23�ܹ����ƻ�----------",
            "content": "<p>\r\n\t2013��� 23�ܹ����ƻ�2013���23�ܹ����ƻ�2013���23�ܹ����ƻ�2013���23�ܹ����ƻ�2013���23�ܹ����ƻ�\r\n</p>\r\n<p>\r\n\t&nbsp;\r\n</p>",
            "lastUpdateTime": "2013-06-05  10:07:08",
            "time": "10.0",
            "summary": "�ܼƻ��ܽ��ܼƻ��ܽ��ܼƻ��ܽ��ܼƻ��ܽ�",
            "summaryTime": "2013-06-05 10:04:19",
            "statusId": "1",
            "Ass": [
                {
                    "assName": "֪�� ��",
                    "id": "0ef3502a_1105071219215520280",
                    "name": "��Զ��"
                },
                {
                    "assName": "֪����",
                    "id": "15139c0f_1202011536531902553",
                    "name": "���ѿ�"
                },
                {
                    "assName": "��ϵ ��",
                    "id": "0ef3502a_1105091340520661128",
                    "name": "����/��ϵ��"
                },
                {
                    "assName": "�����ͻ�",
                    "id": "01ad971b_1001211114379572621",
                    "name": "���������������޹�˾"
                },
                {
                    "assName": "������ ��",
                    "id": "01ad971b_1001211119573612638",
                    "name": "���ݹܼ���Τ��ϲ"
                }
            ],
            "commit": [
                {
                    "id": "ba1dffd2_1306051006379530016",
                    "remarkType": "0",
                    "commitId": "",
                    "empFullName": "admin",
                    "createTime": "2013-06-05 10:06:37",
                    "content": "�ƻ�����"
                },
                {
                    "id": "ba1dffd2_1306051010068750024",
                    "remarkType": "0",
                    "commitId": "",
                    "empFullName": "admin",
                    "createTime": "2013-06-05 10:10:06",
                    "content": "�ƻ�����2"
                },
                {
                    "id": "ba1dffd2_1306051033135780034",
                    "remarkType": "0",
                    "commitId": "ba1dffd2_1306051010068750024",
                    "empFullName": "admin",
                    "createTime": "2013-06-05 10:33:13",
                    "content": "�ƻ����۵��� ��"
                },
                {
                    "id": "7297d246_1306051615262500002",
                    "remarkType": "0",
                    "commitId": "ba1dffd2_1306051010068750024",
                    "empFullName": "admin",
                    "createTime": "2013-06-05 16:15:26",
                    "content": "�ƻ����� �����ۼƻ����۵�����"
                },
                {
                    "id": "ba1dffd2_1306051011024530027",
                    "remarkType": "1",
                    "commitId": "",
                    "empFullName": "admin",
                    "createTime": "2013-06-05 10:11:02",
                    "content": "�ܽ�ĵ���"
                },
                {
                    "id": "ba1dffd2_1306051011435000030",
                    "remarkType": "1",
                    "commitId": "",
                    "empFullName": "admin",
                    "createTime": "2013-06-05 10:11:43",
                    "content": "�ܽ�ĵ���2"
                },
                {
                    "id": "ba1dffd2_1306051032078120033",
                    "remarkType": "1",
                    "commitId": "ba1dffd2_1306051011024530027",
                    "empFullName": "admin",
                    "createTime": "2013-06-05 10:32:07",
                    "content": "����"
                }
            ]
        }
}

	 */
	public void showWorkPlan(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		if (checkParameter("planId", request, response) == false) {
			return;
		}
		String planId = request.getParameter("planId");
//		
//		// ��������
//		planId = "3b7afcbf_1306061643356250004";
		
		JsonObject json = new JsonObject();
		Result result = mgt.queryWorkPlan(planId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
			json.addProperty("code", 0);			
			JsonArray jarr = (JsonArray)result.retVal;
			
			// ��ȡ֪���ˡ���ϵ�ˡ������ͻ�
			JsonObject json2 = (JsonObject)jarr.get(0);
			JsonArray jarr2 = new JsonArray();
			Result rt = mgt.queryWorkPlanAssItem(json2.get("planId").getAsString());
			if (rt.retCode == ErrorCanst.DEFAULT_SUCCESS && rt.retVal != null) {
				jarr2 = (JsonArray)rt.retVal;
			}
			json2.add("Ass", jarr2);
			
			// ��ȡ����
			jarr2 = new JsonArray();
			rt = mgt.queryWorkPlanCommit(json2.get("planId").getAsString());
			if (rt.retCode == ErrorCanst.DEFAULT_SUCCESS && rt.retVal != null) {
				jarr2 = (JsonArray)rt.retVal;
			}
			json2.add("commit", jarr2);
			
			json.add("data", (JsonArray)result.retVal);
			writeJson(json, response);
	
		}else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	
	/**
	 * �����ƻ�-֪����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
op=selectKnowPerson&keyword=XXXX&lastId=XXXX
	 */
	public void selectKnowPerson(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		if (checkParameter("keyword,lastId", request, response) == false) {
			return;
		}
		String keyword = request.getParameter("keyword");
		String lastId = request.getParameter("lastId");

		Result result = mgt.queryEmployee(keyword, lastId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}

	/**
	 * �����ƻ�-��ϵ��
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
op=selectLinkPerson&keyword=XXXX&lastId=XXXX
	 */
	public void selectLinkPerson(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		if (checkParameter("keyword,lastId", request, response) == false) {
			return;
		}
		String keyword = request.getParameter("keyword");
		String lastId = request.getParameter("lastId");
		Result result = mgt.queryLinkPerson(keyword, lastId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	
	/**
	 * ���/�޸Ĺ����ƻ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
����
op=addUpdateWorkPlan
&planId=XXXX
&planType=week
title=2013���23�ܹ����ƻ�----------
&content=<p>\r\n\t2013���23�ܹ����ƻ�2013���23�ܹ����ƻ�2013���23�ܹ����ƻ�2013���23�ܹ����ƻ�2013���23�ܹ����ƻ�\r\n</p>\r\n<p>\r\n\t&nbsp;\r\n</p>
&beginDate=2013-06-02 00:00:00
&endDate=2013-06-08 23:59:59
&Ass=[
	{"assName":"֪����","id":"0ef3502a_1105071219215520280","name":"��Զ��"},
	{"assName":"֪����","id":"15139c0f_1202011536531902553","name":"���ѿ�"},
	{"assName":"��ϵ��","id":"0ef3502a_1105091340520661128","name":"����/��ϵ��"},
	{"assName":"�����ͻ�","id":"01ad971b_1001211114379572621","name":"���������������޹�˾"},
	{"assName":"�����ͻ�","id":"01ad971b_1001211119573612638","name":"���ݹܼ���Τ��ϲ"}
]

	 */
	public void addUpdateWorkPlan(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		if (checkParameter("planId,planType,title,content,beginDate,endDate,Ass", request, response) == false) {
			return;
		}

		String planType = request.getParameter("planType");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String Ass = request.getParameter("Ass");
		String planId = request.getParameter("planId");
		
//		// ��������
//		planType = "week";
//		title = "2013���23�ܹ����ƻ�";
//		content = "2013���23�ܹ����ƻ�����";
//		beginDate = "2013-06-02 00:00:00";
//		endDate = "2013-06-08 23:59:59";
//		Ass = "[{\"assName\":\"֪����\",\"id\":\"0ef3502a_1105071219215520280\",\"name\":\"��Զ��\"},"
//				+ "{\"assName\":\"֪����\",\"id\":\"15139c0f_1202011536531902553\",\"name\":\"���ѿ�\"},"
//				+ "{\"assName\":\"��ϵ��\",\"id\":\"0ef3502a_1105091340520661128\",\"name\":\"����/��ϵ��\"},"
//				+ "{\"assName\":\"�����ͻ�\",\"id\":\"01ad971b_1001211114379572621\",\"name\":\"���������������޹�˾\"},"
//				+ "{\"assName\":\"�����ͻ�\",\"id\":\"01ad971b_1001211119573612638\",\"name\":\"���ݹܼ���Τ��ϲ\"}]";
		


		OADateWorkPlanMgt dmgt = new OADateWorkPlanMgt();

		OADayWorkPlanBean planBean=null;
		if (planId != null && planId.length() > 0) {
			Result result = dmgt.loadWorkPlan(planId);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				planBean =(OADayWorkPlanBean)result.retVal;
			}else{
				MobileResult rs = new MobileResult();
				rs.setCode(-1);
				rs.setDesc("���ҹ����ƻ�ʧ�ܣ������ѱ�ɾ��");
				writeJson(rs, response);
				return;
			}
		} else {
			planBean = new OADayWorkPlanBean();			
		}

		
		planBean.setCreateBy(this.getLoginBean(request).getId());
		planBean.setLastUpdateBy(this.getLoginBean(request).getId());
		planBean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		planBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		planBean.setEmployeeID(this.getLoginBean(request).getId());
		planBean.setDepartmentCode(this.getLoginBean(request).getDepartCode());
		
		planBean.setPlanType(planType);
		planBean.setTitle(title);
		planBean.setContent(content);
		planBean.setBeginDate(beginDate);
		planBean.setEndDate(endDate);
		

		String[] assoicates = null;
		ArrayList<HashMap> listAss = gson.fromJson(Ass, ArrayList.class);
		if (listAss != null) {
			assoicates = new String[listAss.size()];
		}
		for (int i = 0; listAss != null && i < listAss.size(); i++) {
			HashMap map = listAss.get(i);
			String assId = "2";
			String assName = (String)map.get("assName");
			if ("֪����".equals(assName)) {
				assId = "2";
			} else if ("��ϵ��".equals(assName)) {
				assId = "3";
			} else if ("�����ͻ�".equals(assName)) {
				assId = "5";
			}
			assoicates[i] = assId + ":" + (String)map.get("id") + ":" + (String)map.get("name") + ":1:" + assName;
		}
		
		// 2:07a6a75b_1010190907332452623:������:1:֪����
		
		String adt = OnlineUserInfo.getUser(planBean.getEmployeeID()).getName()
				+ "�Ĺ����ƻ�" + "[" + planBean.getTitle() + "]��Ҫ��";
		Result result = null;

		if (planId == null || planId.length() == 0) {
			planBean.setId(IDGenerater.getId());
			result = dmgt.addDayWorkPlan(planBean, assoicates, adt, getLoginBean(request));
		} else {
			result = dmgt.updateDayWorkPlan(planBean,assoicates,null,adt, getLoginBean(request));
		}

		if (result != null &&  result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AdviceMgt amgt = new AdviceMgt();
			List<String[]> listAdvice = (List<String[]>) result.getRetVal();
			for (String[] val : listAdvice) {
				if (val.length == 2) {
					amgt.deleteByRelationId(val[0], val[1]);
				} else {
					amgt.add(val[0], val[1], val[2], val[3], val[4], val[5]);
				}
			}
			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "�����ɹ�");
			json.addProperty("planId", planBean.getId());
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����ʧ��");
			writeJson(rs, response);
		}

	}
	
	/**
	 * ɾ�������ƻ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
����
op=delWorkPlan
&planId=XXXX
	 */
	public void delWorkPlan(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		String planId = request.getParameter("planId");
		
		if (checkParameter("planId", request, response) == false) {
			return;
		}
		OADateWorkPlanMgt dmgt = new OADateWorkPlanMgt();
		String[] ids = {planId};
		Result result = dmgt.delDayWorkPlan(ids);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			new AdviceMgt().deleteByRelationId(planId, "");			
			MobileResult rs = new MobileResult();
			rs.setCode(0);
			rs.setDesc("�����ɹ�");	
			writeJson(rs, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����ʧ��");	
			writeJson(rs, response);
		}
	}

	/**
	 * �ܽṤ���ƻ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
op=summaryWorkPlan
&planId=XXXX
&time=10.0
&summary=�ܼƻ��ܽ��ܼƻ��ܽ��ܼƻ��ܽ��ܼƻ��ܽ�
&statusId=1
	 */
	public void summaryWorkPlan(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		if (checkParameter("planId,time,summary,statusId", request, response) == false) {
			return;
		}

		String planId = request.getParameter("planId");
		String time = request.getParameter("time");
		String summary = request.getParameter("summary");
		String statusId = request.getParameter("statusId");

		// ��������
//		planId = "4c23836f_1306061025588590002";
//		time = "12";
//		summary = "�����ƻ��ܽṤ���ƻ��ܽṤ���ƻ��ܽ�";
//		statusId = "5";

		OADateWorkPlanMgt dmgt = new OADateWorkPlanMgt();
		Result result = dmgt.loadWorkPlan(planId);
		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("���ҹ����ƻ�ʧ�ܣ������ѱ�ɾ��");
			writeJson(rs, response);
			return;
		}
		OADayWorkPlanBean planBean =(OADayWorkPlanBean)result.retVal;

		planBean.setLastUpdateBy(this.getLoginBean(request).getId());
		planBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		planBean.setTime(time);
		planBean.setSummary(summary);
		planBean.setStatusId(Integer.parseInt(statusId));
		planBean.setSummaryTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));

		String adt = OnlineUserInfo.getUser(planBean.getEmployeeID()).getName()
				+ "�Ĺ����ƻ�" + "[" + planBean.getTitle() + "]��Ҫ��";
		
		String[] assoicates = null;
		Result rt = mgt.queryWorkPlanAssItem(planId);
		if (rt.retCode == ErrorCanst.DEFAULT_SUCCESS && rt.retVal != null) {
			JsonArray jarr = (JsonArray)rt.retVal;
			assoicates = new String[jarr.size()];
			for (int i = 0; i < jarr.size(); i++) {
				JsonObject json = (JsonObject)jarr.get(i);
				String assId = "2";
				String assName = json.get("assName").getAsString();
				if ("֪����".equals(assName)) {
					assId = "2";
				} else if ("��ϵ��".equals(assName)) {
					assId = "3";
				} else if ("�����ͻ�".equals(assName)) {
					assId = "5";
				}
				assoicates[i] = assId + ":" + json.get("id").getAsString() + ":" + json.get("name").getAsString() + ":1:" + assName;
			}
		}
		result = dmgt.updateDayWorkPlan(planBean,assoicates,new String[0],adt, getLoginBean(request));
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AdviceMgt amgt = new AdviceMgt();
			List<String[]> listAdvice = (List<String[]>) result.getRetVal();
			for (String[] val : listAdvice) {
				if (val.length == 2) {
					amgt.deleteByRelationId(val[0], val[1]);
				} else {
					amgt.add(val[0], val[1], val[2], val[3], val[4], val[5]);
				}
			}
			
			MobileResult rs = new MobileResult();
			rs.setCode(0);
			rs.setDesc("�����ɹ�");	
			writeJson(rs, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����ʧ��");	
			writeJson(rs, response);
		}
	}

	/**
	 * ���������ƻ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
op=commitWorkPlan
&planId=XXXX
&remarkType=0
&commitId=
&content=�ƻ�����
	 */
	public void commitWorkPlan(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		
		if (checkParameter("planId,remarkType,commitId,content", request, response) == false) {
			return;
		}
		OADateWorkPlanMgt dmgt = new OADateWorkPlanMgt();
		
		String userId = request.getParameter("sid");
		String planId = request.getParameter("planId");
		String remarkType = request.getParameter("remarkType");
		String commitId = request.getParameter("commitId");
		String content = request.getParameter("content");

		Result result = dmgt.loadWorkPlan(planId);
		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("���ҹ����ƻ�ʧ�ܣ������ѱ�ɾ��");
			writeJson(rs, response);
			return;
		}
		OADayWorkPlanBean planBean =(OADayWorkPlanBean)result.retVal;

		Result rss = dmgt.selComPerson(commitId);
		List<String> empIds = (List<String>) rss.getRetVal();
		result = dmgt.commit(planId, remarkType, content, userId,
				BaseDateFormat
						.format(new Date(), BaseDateFormat.yyyyMMddHHmmss),
				"���Ĺ����ƻ�[" + planBean.getTitle() + "]���µĵ���", planBean
						.getCreateBy(), OnlineUserInfo.getUser(userId).name,
				"", commitId, empIds, planBean.getTitle());
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			MobileResult rs = new MobileResult();
			rs.setCode(0);
			rs.setDesc("�����ɹ�");	
			writeJson(rs, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����ʧ��");	
			writeJson(rs, response);
		}
	}

	/**
	 * ͬ��ͨѶ¼
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void syncContact(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("sId");
		Result result = mgt.loadAddress(userId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	
	/**
	 * ��ȡ��ǰ����Android�汾��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getAndroidVersion(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		String txtUrl = "../../website/common/AndroidVersion.txt";
		FileReader read = new FileReader(txtUrl);
		BufferedReader b = new BufferedReader(read);
		String line;
		String version = "";
		while ((line = b.readLine()) != null) {
			version += line;
		}

		MobileResult rs = new MobileResult();
		rs.setCode(0);
		rs.setDesc(version);
		writeJson(rs, response);
	}

	/**
	 * ��ѯ��Ʒ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void selectGoods(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		if (checkParameter("goodsFullName,goodsNumber,barCode,lastId", request, response) == false) {
			return;
		}
		String goodsFullName = request.getParameter("goodsFullName");
		String goodsNumber = request.getParameter("goodsNumber");
		String barCode = request.getParameter("barCode");
		String lastId = request.getParameter("lastId");

		Result result = mgt.queryGoods(goodsFullName, goodsNumber, barCode, lastId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}

	/**
	 * ��ѯ�ֿ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void selectStock(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		if (checkParameter("keyword,lastId", request, response) == false) {
			return;
		}
		String keyword = request.getParameter("keyword");
		String lastId = request.getParameter("lastId");

		Result result = mgt.queryStock(keyword, lastId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	
	/**
	 * ��Ʒ���
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getStoreDetail(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/UserFunctionQueryAction.do?tableName=ViewStoreDetail");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲鿴���Ȩ��");
			writeJson(rs, response);
			return;
		}
		
		if (checkParameter("storeId, goodsId, lastId", request, response) == false) {
			return;
		}
		String storeId = request.getParameter("storeId");
		String goodsId = request.getParameter("goodsId");
		String lastId = request.getParameter("lastId");
		
		boolean bAllGoods = null != request.getParameter("all");

//		// ��������
//		storeId = "";
//		goodsId = "";
//		lastId = "";
				
		Result result = mgt.queryStoreDetail(storeId, goodsId, lastId, bAllGoods);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	
	/**
	 * �ͻ��ƽ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getCRMClientTransfer(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		
		if (checkParameter("sid, lastId", request, response) == false) {
			return;
		}
		String userId = request.getParameter("sid");
		String lastId = request.getParameter("lastId");
		
		Result result = mgt.queryCRMClientTransfer(userId, lastId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	
	/**
	 * ����ͻ��ƽ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void handleClientTransfer(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		
		if (checkParameter("sid,TransferId,handle", request, response) == false) {
			return;
		}
		String userId = request.getParameter("sid");
		String TransferId = request.getParameter("TransferId");
		String handle = request.getParameter("handle");
		
		// ��������
//		clientId = "c5e2edc5_1307051447437890006";
//		handle = "receive";
		
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		
		Locale locale = getLocale(request);
		
		HashMap values = new HashMap();
		values.put("id", TransferId);
		
		Result result;
		MobileResult rs = new MobileResult();
		if ("receive".equals(handle)) {
			result = mgt.execDefineSql("CRMClientTransfer_Receive", userId, values, resources, locale);
		} else if ("reject".equals(handle)) {
			result = mgt.execDefineSql("CRMClientTransfer_Reject", userId, values, resources, locale);
		} else {
			rs.setCode(-1);
			rs.setDesc("����handle��ֵ����");
			writeJson(rs, response);
			return;
		}

		if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			String msg = getDefineErrMsg(request, result);
			rs.setCode(result.getRetCode()); 
			rs.setDesc(msg) ;
		}else{
			rs.setCode(0); 
			rs.setDesc("ִ�гɹ�") ;
		}
		writeJson(rs, response);
	}

	/**
	 * �̵�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void storeCheck(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {		
		// ���ﲻУ��Ȩ���������Զ���ִ��ʱ��У��
		
		if (checkParameter("sid,list", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid") ;
		String storeId = request.getParameter("storeId") ;
		String sGoods = request.getParameter("list") ;

//		// ��������
//		storeId = "b149eff9_1112131724528777867";
//		sGoods = "[{'GoodsId':'15e6829a_1105311348296902281','Qty':'123'},{'GoodsId':'cf5f301a_1109261509526580615','Qty':'1234'}]";
		
		ArrayList<HashMap> listGoods = gson.fromJson(sGoods, ArrayList.class);
		if (checkParameter("GoodsId,Qty", "list", listGoods.get(0), response) == false) {
			return;
		}		
		
		MobileResult result = new MobileResult() ;
		try{
			LoginBean loginBean = getLoginBean(request);

			HashMap values = new HashMap();
			
			values.put("EmployeeID", userId);	// ������
			OnlineUser user = OnlineUserInfo.getUser(userId);
			values.put("DepartmentCode", user.getDeptId());	// ����
			Result stockRs = mgt.getClassCodeById(storeId, "tblStock");
			if (stockRs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				result.setCode(-1); 
				result.setDesc("���Ҳֿ�ʧ��") ;
				writeJson(result, response) ;
				return;
			}
			String StockCode = (String)(((List<Object[]>)stockRs.getRetVal()).get(0))[0];
			values.put("StockCode", StockCode);	// �ֿ�
			// ��������
			ArrayList childList = new ArrayList();
			values.put("TABLENAME_tblCheckReadyDet", childList);
		
			for(HashMap item : listGoods){
			// �ӱ�����
				Result goodsRs = mgt.loadGoods((String)item.get("GoodsId"), StockCode);
				if (goodsRs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
					result.setCode(-1); 
					result.setDesc("������Ʒʧ��") ;
					writeJson(result, response) ;
					return;
				}
				HashMap hm = (HashMap)(((List)goodsRs.getRetVal()).get(0));				
				hm.put("CQty", item.get("Qty"));
				childList.add(hm);
			}
			
			Result rs = execDefineAdd(request, values, "tblCheckReady", "");
			
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				String msg = getDefineErrMsg(request, rs);
				result.setCode(rs.getRetCode());
				result.setDesc(msg);
			} else {
				result.setCode(0);
				result.setDesc("��ӳɹ�");
			}
		}catch (Exception e) {
			e.printStackTrace() ;
			result.setCode(-1); 
			result.setDesc("���ʧ��") ;
		}
		writeJson(result, response) ;
	}
	
	/**
	 * ��ӵ���
	 * @param request
	 * @param values
	 * @param mop
	 * @return
	 * @throws Exception
	 */
	public Result execDefineAdd(final HttpServletRequest request, HashMap values, String tblName, String saveDraft) throws Exception {

		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,tblName);
		// ����Ĭ��ֵ
		for (DBFieldInfoBean field : tableInfo.getFieldInfos()) {
			if (values.get(field.getFieldName()) == null) {
				if (field.getFieldType() == 5) {
					field.setDefaultValue(BaseDateFormat.format(
							new Date(), BaseDateFormat.yyyyMMdd));
				} else if (field.getFieldType() == 6) {
					field.setDefaultValue(BaseDateFormat.format(
							new Date(), BaseDateFormat.yyyyMMddHHmmss));
				}
				values.put(field.getFieldName(), field.getDefValue());
			}
		}

		Locale locale = getLocale(request);
		String addMessage = this.getMessageResources(request, "common.lb.add");
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MessageResources resources = null;

		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		/* ������ӵ��ݽӿ� */

		LoginBean lb = getLoginBean(request);
		MOperation mop = (MOperation) lb.getOperationMap().get("/UserFunctionQueryAction.do?tableName=" + tblName);
		Result rs = new UserFunctionMgt().add(tableInfo, values, lb, "", allTables, "", "", locale, addMessage, resources, props,mop, saveDraft);
		return rs;
	}

	/**
	 * ��ѯָ��ʱ����޸Ĺ����û�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listChatUser(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		if (checkParameter("lastUpdateTime", request, response) == false) {
			return;
		}

		String lastUpdateTime = request.getParameter("lastUpdateTime");
		lastUpdateTime = "2000-01-01 00:00:00";//��ʱ�̶�ʱ�䣬�ֻ���δ�޸Ĺ���֮ǰ
		Result result = mgt.getAllUsersAfterTime(lastUpdateTime);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonArray jarr = (JsonArray) result.retVal;
			for (int i = 0; i < jarr.size(); i++) {
				JsonObject j = (JsonObject) jarr.get(i);
				if (j.get("icon").toString().length() > 2) {
					j.addProperty("icon", true);
				} else {
					j.addProperty("icon", false);
				}

				String duty=GlobalsTool.getEnumerationItemsDisplay("duty", j.get("title").toString().replaceAll("\"", ""),"zh_CN");
				j.addProperty("title", duty);
			}

			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", jarr);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
		return;
	}	

	/**
	 * ��ѯ���ź�Ⱥ��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listDeptAndGroup(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		if (checkParameter("sid", request, response) == false) {
			return;
		}
		String userId = request.getParameter("sid");
		Result result = mgt.listDeptAndGroup(userId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray) result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	
	/**
	 * ��ѯ������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listChatOnline(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		JsonObject json = new JsonObject();
		JsonArray jarr = new JsonArray();
		
  	   	Date curDate = new Date();
		for (OnlineUser user : OnlineUserInfo.getAllUserList()) {
			if (user.isOnline()) {	// ֻ�������ߵ�
				JsonObject j = new JsonObject();
				j.addProperty("id", user.getId());
				if ("mobile".equals(user.getType())) {
					j.addProperty("onlineType", EmployeeItem.MOBILE_LOGIN);
				} else if ("web".equals(user.getType())) {
					j.addProperty("onlineType", EmployeeItem.BS_LOGIN);
				} else if ("kk".equals(user.getType())) {
					j.addProperty("onlineType", EmployeeItem.CS_LOGIN);
				} else {
					continue;
				}
				j.addProperty("status", EmployeeItem.ONLINE);
				jarr.add(j);
			}
		}
		json.addProperty("code", 0);
		json.add("list", jarr);
		writeJson(json, response);
	}
	
	/**
	 * ��ѯδ����Ϣ����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listChatCount(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

//		BaseEnv.log.info("-------��ʼ");
		if (checkParameter("sid", request, response) == false) {
			return;
		}
		
		String userId = request.getParameter("sid");
		Result result = mgt.listChatNewly(userId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonArray jarr = (JsonArray) result.retVal;
			
			JsonArray jarrNew = new JsonArray();
			for (int i = 0; i < jarr.size(); i++) {
				JsonObject j = (JsonObject) jarr.get(i);
				JsonObject json = new JsonObject();
				boolean bNew = true;
				if (i > 0) {
					JsonObject jsonPre = (JsonObject) jarrNew.get(jarrNew.size() - 1);
					if (jsonPre.get("id").toString().equals(j.get("id").toString())) {
						json = jsonPre;
						bNew = false;
					}
				}
				json.addProperty("type", j.get("type").toString().replace("\"", ""));
				json.addProperty("id", j.get("id").toString().replace("\"", ""));
				json.addProperty("count", j.get("count").toString().replace("\"", ""));
				json.addProperty("name", j.get("relationName").toString().replace("\"", ""));
				String msg = j.get("sendName").toString().replace("\"", "") + ":" + GlobalsTool.replaceHTML3(j.get("content").toString().replace("\"", ""));
				msg = msg.length() > 50 ? msg.substring(0, 50) + "..." : msg;
				json.addProperty("msg", msg);
				json.addProperty("time", j.get("time").toString().replace("\"", ""));
				if (bNew) {
					jarrNew.add(json);
				}
			}
			
			JsonObject jsonReturn = new JsonObject();
			jsonReturn.addProperty("code", 0);
			jsonReturn.add("list", jarrNew);

//			BaseEnv.log.info("-------����");
			writeJson(jsonReturn, response);
		} else {
//			BaseEnv.log.info("-------����");
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}
	/**
	 * ��ѯ������Ϣ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void receveChatMsg(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		
		if (checkParameter("sid,relationId,lasstId", request, response) == false) {
			return;
		}	
		
		String  userId = request.getParameter("sid");
		String  relationId = request.getParameter("relationId");
//		String  type = request.getParameter("type");
		String  lastId = request.getParameter("lasstId");
		String  allNoRead = request.getParameter("allNoRead");
//		
//		type = "person";
//		relationId = "7237978a_0912251628128400201";
//		lastId = "5fcf5f77_1304161116323606486";

		Result result = MSGConnectCenter.receiveMsg(userId, relationId);	// ��δ�����Ѷ�
		int count = result.getRealTotal();
		if (null != allNoRead) {
			result = mgt.listChatMsg(userId, relationId, count);
		} else {
			result = mgt.listChatMsg(userId, relationId, lastId);
		}
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonArray jarr = (JsonArray) result.retVal;
			JsonArray jarrNew = new JsonArray();
			for (int i = jarr.size() - 1; i >= 0; i--) {
				JsonObject j = (JsonObject) jarr.get(i);
				j.addProperty("msg", GlobalsTool.replaceHTML3(j.get("msg").toString().replace("\"", "")));
				jarrNew.add(j);
			}
			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", jarrNew);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}		
	}

	/**
	 * ����˽��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void sendChatMsg (final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
		if (checkParameter("sid,relationId,type,msg", request, response) == false) {
			return;
		}		

		String  userId = request.getParameter("sid");
		String  relationId = request.getParameter("relationId");
		String  type = request.getParameter("type");
		String  content = request.getParameter("msg");
		
//		type = "person";
//		content = "test";
//		relationId = "3832bdf5_0912211631568490003";
		
		MSGConnectCenter.sendMsg(userId, relationId, content, type);

		MobileResult rs = new MobileResult();
		rs.setCode(0);
		rs.setDesc("���ͳɹ�");
		writeJson(rs, response);
	}

	/**
	 * ͬ�������������Ϣ 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private void syncSalesErpInfo(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		String lastId = request.getParameter("lastId");
		String size = request.getParameter("size");
		String lastUpdateTime = request.getParameter("lastUpdateTime");
		if (null == lastId || null == size) {
			lastId = "";
			size = "0";
		}
		int pageSize = Integer.parseInt(size);
		
		String type = request.getParameter("type");
				
//		// ��������
//		lastId = "cbe163e2_1112121432292910838";
//		pageSize = 10;
//		type = "company";
		
		Result result = new Result();
		if (null == type || "goods".equals(type)) {
			result = mgt.listGoods(lastId, pageSize, lastUpdateTime);
		} else if ("stock".equals(type)) {
			result = mgt.listStock(lastId, pageSize, lastUpdateTime);
		} else if ("company".equals(type)) {
			result = mgt.listCompany(lastId, pageSize, lastUpdateTime);
		} else {
			result.retCode = ErrorCanst.DEFAULT_FAILURE;
		}
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray) result.retVal);
			writeJson(json, response);
		} else {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("�޲�ѯ���");
			writeJson(rs, response);
		}
	}

	/**
	 * �����˻�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void salesReturnStock(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// ���ﲻУ��Ȩ���������Զ���ִ��ʱ��У��		

		if (checkParameter("sid,list", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid") ;
		String sGoods = request.getParameter("list") ;

//		// ��������
//		sGoods = "[{\"Qty\":\"1.0\",\"GoodsId\":\"15e6829a_1105311348296902281\",\"Price\":\"99.0\"},{\"Qty\":\"1.0\",\"GoodsId\":\"cf5f301a_1109261509526580615\",\"Price\":\"620.0\"},{\"Qty\":\"1.0\",\"GoodsId\":\"2e51762e_1112100941556200543\",\"Price\":\"500.0\"}]";
		
		

		MobileResult result = new MobileResult() ;
		String StockCode = request.getParameter("stockCode");
		String CompanyCode = request.getParameter("companyCode");
		StockCode = "00001";
		CompanyCode = "00001";
		
		if (!StringUtils.isNotBlank(StockCode) || !StringUtils.isNotBlank(CompanyCode)) {
			HashMap<String, String> empMap = new UserMgt().getEmployee2(userId);
			if(empMap == null || !StringUtils.isNotBlank(empMap.get("CompanyCode")) || !StringUtils.isNotBlank(empMap.get("StockCode"))){
				result.setCode(-1); 
				result.setDesc("������ҳ�˵�ְԱ������������Ĭ�ϲֿ��������λ") ;
				writeJson(result, response) ;
				return ;
			}
			CompanyCode = empMap.get("CompanyCode");
			StockCode = empMap.get("StockCode");
		}
		
		ArrayList<HashMap> listGoods = gson.fromJson(sGoods, ArrayList.class);
		if (checkParameter("GoodsId,Qty,Price", "list", listGoods.get(0), response) == false) {
			return;
		}		
		
		try{
			HashMap values = new HashMap();
			LoginBean loginBean = getLoginBean(request);
			values.put("CompanyCode", CompanyCode); // �ŵ�Ĭ��������λ
			values.put("StockCode", StockCode); // �ŵ�Ĭ�ϲֿ�
			values.put("EmployeeID", userId); // ������
			OnlineUser user = OnlineUserInfo.getUser(userId);
			values.put("DepartmentCode", user.getDeptId());	// ����
			values.put("SCompanyID", loginBean.getSunCmpClassCode()) ;
			
			values.put("InVoiceType", "1") ;
			values.put("billType", "sales") ;
			
			
			ArrayList childList = new ArrayList();
			values.put("TABLENAME_tblSalesReturnStockDet", childList);
			double allAmount = 0 ;
			for(HashMap item : listGoods){
			// �ӱ�����
				Result goodsRs = mgt.loadGoods((String)item.get("GoodsId"), StockCode);
				if (goodsRs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
					result.setCode(-1); 
					result.setDesc("������Ʒʧ��") ;
					writeJson(result, response) ;
					return;
				}
				HashMap hm = (HashMap)(((List)goodsRs.getRetVal()).get(0));	

				Double lfQty = Double.parseDouble(item.get("Qty").toString());
				Double lfPrice = Double.parseDouble(item.get("Price").toString());
				double lfAmount = lfPrice * lfQty;
				
				hm.put("Qty", lfQty) ;
				hm.put("Price", lfPrice) ;
				hm.put("Amount", lfAmount) ;
				hm.put("Tax", 0) ;
				hm.put("TaxPrice", lfPrice) ;
				hm.put("TaxAmount", lfAmount) ;
				hm.put("Discount", 1) ;
				hm.put("DisPrice", lfPrice) ;
				hm.put("DisAmount", 0) ;
				hm.put("DisBackAmt", lfAmount) ;
				hm.put("ApplyQty", 0) ;
				
				allAmount += lfAmount ;
				childList.add(hm) ;
			}
			double totalTaxAmount = GlobalsTool.round(allAmount, GlobalsTool.getDigitsOrSysSetting("tblSalesReturnStock","TotalTaxAmount")) ;
			values.put("TotalTaxAmount", totalTaxAmount) ;
			//values.put("AlrAccAmt", allAmount) ;
			values.put("AccAmt", allAmount) ;
			values.put("FactIncome", allAmount) ;
			values.put("DiscountAmount", 0) ;		

			String saveDraft = "";
    		if ("true".equals(GlobalsTool.getSysSetting("MobileBillToDraf"))) {	// �Ƿ��ݸ�
    			saveDraft = "saveDraft";
    		}
			Result rs = execDefineAdd(request, values, "tblSalesReturnStock", saveDraft);
						
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				String msg = getDefineErrMsg(request, rs);
				result.setCode(rs.getRetCode());
				result.setDesc(msg);
			} else {
				Result stockRs = mgt.queryStock(StockCode);
				String stock = (String)(((List<Object[]>)stockRs.getRetVal()).get(0))[0];
				Result comRs = mgt.queryCompany(CompanyCode);
				String comName = (String)(((List<Object[]>)comRs.getRetVal()).get(0))[0];
				JsonObject j = new JsonObject();
				j.addProperty("code", 0);
				j.addProperty("company", GlobalsTool.getCompanyName("zh_CN"));
				j.addProperty("emp", OnlineUserInfo.getUser(userId).name);
				j.addProperty("stock", stock);
				j.addProperty("comName", comName);
				j.addProperty("billNo", values.get("billNo")+"");
				writeJson(j, response);
				return;
			}
		}catch (Exception e) {
			e.printStackTrace() ;
			result.setCode(-1); 
			result.setDesc("���ʧ��") ;
		}
		writeJson(result, response) ;
	}

	/**
	 * ���۶���
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void salesOrder(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// ���ﲻУ��Ȩ���������Զ���ִ��ʱ��У��	

		if (checkParameter("sid,list,remark", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String sGoods = request.getParameter("list");
		String sRemark = request.getParameter("remark");

//		// ��������
//		sGoods = "[{\"Qty\":\"1.0\",\"GoodsId\":\"4244e6f6_1309081000573780611\",\"Price\":\"99.0\"},{\"Qty\":\"1.0\",\"GoodsId\":\"014eb90f_1309021034019022599\",\"Price\":\"620.0\"},{\"Qty\":\"1.0\",\"GoodsId\":\"014eb90f_1309021703569244316\",\"Price\":\"500.0\"}]";
//		sRemark = "����";
		

		MobileResult result = new MobileResult() ;

		String StockCode = request.getParameter("stockCode");
		String CompanyCode = request.getParameter("companyCode");
		
		if (!StringUtils.isNotBlank(StockCode) || !StringUtils.isNotBlank(CompanyCode)) {
			HashMap<String, String> empMap = new UserMgt().getEmployee2(userId);
			if(empMap == null || !StringUtils.isNotBlank(empMap.get("CompanyCode")) || !StringUtils.isNotBlank(empMap.get("StockCode"))){
				result.setCode(-1); 
				result.setDesc("������ҳ�˵�ְԱ������������Ĭ�ϲֿ��������λ") ;
				writeJson(result, response) ;
				return ;
			}
			CompanyCode = empMap.get("CompanyCode");
			StockCode = empMap.get("StockCode");
		}
		
		ArrayList<HashMap> listGoods = gson.fromJson(sGoods, ArrayList.class);
		if (checkParameter("GoodsId,Qty,Price", "list", listGoods.get(0), response) == false) {
			return;
		}		
		
		try{
			HashMap values = new HashMap();
			LoginBean loginBean = getLoginBean(request);
			values.put("CompanyCode", CompanyCode); // �ŵ�Ĭ��������λ
			values.put("StockCode", StockCode); // �ŵ�Ĭ�ϲֿ�
			values.put("EmployeeID", userId); // ������
			OnlineUser user = OnlineUserInfo.getUser(userId);
			values.put("DepartmentCode", user.getDeptId());	// ����
			values.put("SCompanyID", loginBean.getSunCmpClassCode()) ;
			
			values.put("InVoiceType", "1") ;
			values.put("billType", "sales") ;
			values.put("Remark", sRemark) ;
			
			
			ArrayList childList = new ArrayList();
			values.put("TABLENAME_tblSalesOrderDet", childList);
			double allAmount = 0 ;
			for(HashMap item : listGoods){
			// �ӱ�����
				Result goodsRs = mgt.loadGoods((String)item.get("GoodsId"), StockCode);
				if (goodsRs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
					result.setCode(-1); 
					result.setDesc("������Ʒʧ��") ;
					writeJson(result, response) ;
					return;
				}
				HashMap hm = (HashMap)(((List)goodsRs.getRetVal()).get(0));	

				Double lfQty = Double.parseDouble(item.get("Qty").toString());
				Double lfPrice = Double.parseDouble(item.get("Price").toString());
				double lfAmount = lfPrice * lfQty;
				
				hm.put("Qty", lfQty) ;
				hm.put("Price", lfPrice) ;
				hm.put("Amount", lfAmount) ;
				hm.put("Tax", 0) ;
				hm.put("TaxPrice", lfPrice) ;
				hm.put("TaxAmount", lfAmount) ;
				hm.put("Discount", 1) ;
				hm.put("DisPrice", lfPrice) ;
				hm.put("DisAmount", 0) ;
				hm.put("DisBackAmt", lfAmount) ;
				hm.put("ApplyQty", 0) ;
				
				allAmount += lfAmount ;
				childList.add(hm) ;
			}
			double totalTaxAmount = GlobalsTool.round(allAmount, GlobalsTool.getDigitsOrSysSetting("tblSalesOrder","TotalTaxAmount")) ;
			values.put("TotalTaxAmount", totalTaxAmount) ;
			//values.put("AlrAccAmt", allAmount) ;
			values.put("AccAmt", allAmount) ;
			values.put("FactIncome", allAmount) ;
			values.put("DiscountAmount", 0) ;		

			String saveDraft = "";
    		if ("true".equals(GlobalsTool.getSysSetting("MobileBillToDraf"))) {	// �Ƿ��ݸ�
    			saveDraft = "saveDraft";
    		}
			Result rs = execDefineAdd(request, values, "tblSalesOrder", saveDraft);
						
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				String msg = getDefineErrMsg(request, rs);
				result.setCode(rs.getRetCode());
				result.setDesc(msg);
			} else {
				Result stockRs = mgt.queryStock(StockCode);
				String stock = (String)(((List<Object[]>)stockRs.getRetVal()).get(0))[0];
				Result comRs = mgt.queryCompany(CompanyCode);
				String comName = (String)(((List<Object[]>)comRs.getRetVal()).get(0))[0];
				JsonObject j = new JsonObject();
				j.addProperty("code", 0);
				j.addProperty("company", GlobalsTool.getCompanyName("zh_CN"));
				j.addProperty("emp", OnlineUserInfo.getUser(userId).name);
				j.addProperty("stock", stock);
				j.addProperty("comName", comName);
				j.addProperty("billNo", values.get("billNo")+"");
				writeJson(j, response);
				return;
			}
		}catch (Exception e) {
			e.printStackTrace() ;
			result.setCode(-1); 
			result.setDesc("���ʧ��") ;
		}
		writeJson(result, response) ;
	}
	

	/**
	 * �ƶ�ǩ��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void signIn(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// ��������У��Ȩ��	

		if (checkParameter("sid,address,point,time", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String address = request.getParameter("address");
		String point = request.getParameter("point");
		String time = request.getParameter("time");

		Result rs = mgt.addSignIn(userId, address, point, time);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			JsonObject j = new JsonObject();
			j.addProperty("code", 0);
			j.addProperty("desc", "��ӳɹ�");
			j.addProperty("id", rs.getRetVal().toString());
			writeJson(j, response) ;
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("���ʧ��");
			writeJson(result, response) ;
		}
		
	}


	/**
	 * ��ʾ����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyTodo(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// ����û��У��Ȩ��	

		if (checkParameter("status,count", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String count = request.getParameter("count");
		String status = request.getParameter("status");
		
		int nCount = Integer.parseInt(count);

		Result rs = mgt.listMyTodo(userId, status, nCount);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {			
			JsonArray jarr = (JsonArray)rs.getRetVal();
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", jarr);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * �����޸Ĵ���
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addUpdateMyTodo(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// ����û��У��Ȩ��	

		if (checkParameter("id,title,time,status", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String time = request.getParameter("time");
		String status = request.getParameter("status");
		

		OAToDoBean bean = new OAToDoBean();
		if (!"".equals(id)) {
			bean = (OAToDoBean) new ToDoMgt().loadToDo(id).getRetVal();
		}
		bean.setCreateBy(userId);
		bean.setTitle(title);
		bean.setAlertTime(time);
		bean.setStatus(status);
		
		String err = "";
		
		if (null == bean.getId() || "".equals(bean.getId())) {
			id = IDGenerater.getId();
			bean.setId(id);
			err = ToDoAction.addTodo(bean);
		} else {
			err = ToDoAction.updateToDo(bean);
		}
		
		if ("".equals(err)) {			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "�����ɹ�");
			json.addProperty("id", id);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc(err);
			writeJson(result, response);
		}
	}


	/**
	 * ɾ������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void delMyTodo(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// ����û��У��Ȩ��	

		if (checkParameter("id", request, response) == false) {
			return;
		}

		String id = request.getParameter("id");
		
		if (new ToDoAction().delToDo(id)) {
			MobileResult result = new MobileResult();
			result.setCode(0);
			result.setDesc("�����ɹ�");
			writeJson(result, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("����ʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ�ճ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMySchedule(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// ����û��У��Ȩ��	

		if (checkParameter("beginTime,endTime", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		

		Result rs = mgt.listMySchedule(userId, beginTime, endTime);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {			
			JsonArray jarr = (JsonArray)rs.getRetVal();
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", jarr);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * �����޸��ճ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addUpdateMySchedule(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		if (checkParameter("id,title,beginTime,endTime,alertTime", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String alertTime = request.getParameter("alertTime");

		OACalendarBean bean = new OACalendarBean();
		if (!"".equals(id)) {
			bean = (OACalendarBean) new OACalendaMgt().loadCalendar(id).getRetVal();
		}
		bean.setUserId(userId);
		bean.setTitle(title);
		bean.setStratTime(beginTime);
		bean.setFinishTime(endTime);
		bean.setAlertTime(alertTime);
		
		String err = "";
		
		if ("".equals(bean.getId())) {
			id = IDGenerater.getId();
			bean.setId(id);
			err = OACalendarAction.addCalendar(bean);
		} else {
			err = OACalendarAction.updateCalendar(bean);
		}
		
		if ("".equals(err)) {			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "�����ɹ�");
			json.addProperty("id", id);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc(err);
			writeJson(result, response);
		}
	}


	/**
	 * ɾ���ճ�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void delMySchedule(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		if (checkParameter("id", request, response) == false) {
			return;
		}

		String id = request.getParameter("id");
				
		if (new OACalendarAction().delCalendar(id)) {			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "�����ɹ�");
			json.addProperty("id", id);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("����ʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ������Ŀ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getMyItems(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OAItemsAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����Ŀģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("itemId", request, response) == false) {
			return;
		}

		String itemId = request.getParameter("itemId");
		
//		// ��������
//		itemId = "69a1025b_1311121334033140002";

		Result rs = mgt.getMyItems(itemId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			JsonArray jarr = (JsonArray) rs.getRetVal();
			if (jarr.size() > 0) {
				JsonObject json = (JsonObject) jarr.get(0);
				json.addProperty("code", 0);
				writeJson(json, response);
				return;
			}
		}
		MobileResult result = new MobileResult();
		result.setCode(-1);
		result.setDesc("��ѯʧ��");
		writeJson(result, response);
		
	}

	/**
	 * ��ʾ�ҵ���Ŀ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyItems(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OAItemsAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����Ŀģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("type,status,count", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String type = request.getParameter("type");
		String status = request.getParameter("status");
		String count = request.getParameter("count");
		
//		// ��������
//		userId = "1";
//		type = "1";
//		status = "1";
//		count = "2";
		
		int nType = Integer.parseInt(type);
		int nCount = Integer.parseInt(count);

		Result rs = mgt.listMyItems(userId, nType, status, nCount);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)rs.getRetVal());
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ��Ŀ��̬
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyItemsLog(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OAItemsAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����Ŀģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("itemId", request, response) == false) {
			return;
		}

		String itemId = request.getParameter("itemId");

//		// ��������
//		itemId = "69a1025b_1311121334033140002";
		
		Result rs = mgt.listMyItemsLog(itemId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			JsonArray jarr = (JsonArray) rs.getRetVal();
			for (int i = 0; i < jarr.size(); i++) {
				JsonObject j = (JsonObject) jarr.get(i);
				j.addProperty("content", GlobalsTool.replaceHTML(j.get("content").toString().replace("\"", "")));
			}
			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", jarr);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ��Ŀ����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyItemsTask(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OAItemsAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����Ŀģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("itemId", request, response) == false) {
			return;
		}

		String itemId = request.getParameter("itemId");

//		// ��������
//		itemId = "69a1025b_1311121334033140002";
		
		Result rs = mgt.listMyItemsTask(itemId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)rs.getRetVal());
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * �����Ŀ��̬
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addItemsLog(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OAItemsAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����Ŀģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("itemId,content,createTime,commentId,replyId", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String itemId = request.getParameter("itemId");
		String content = request.getParameter("content");
		String createTime = request.getParameter("createTime");
		String commentId = request.getParameter("commentId");
		String replyId = request.getParameter("replyId");

//		// ��������
//		itemId = "69a1025b_1311121334033140002";
//		content = "��������";
//		commentId = "69a1025b_1311121335464870021";
//		replyId = "1";
		String id = IDGenerater.getId();
		if (DiscussAction.add(userId, id, createTime, "OAItemsLog", itemId, content, commentId, replyId, 0)) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "��ӳɹ�");
			json.addProperty("id", id);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("���ʧ��");
			writeJson(result, response);
		}
	}
	

	/**
	 * �����޸�����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addUpdateTask(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OAItemsAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("����Ŀģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("id,title,remark,executor,surveyor,status,schedule,beginTime,endTime,itemId,taskId", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String remark = request.getParameter("remark");
		String executor = request.getParameter("executor");
		String surveyor = request.getParameter("surveyor");
		String status = request.getParameter("status");
		String schedule = request.getParameter("schedule");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String itemId = request.getParameter("itemId");
		String taskId = request.getParameter("taskId");
		String emergencyLevel = request.getParameter("emergencyLevel");//�����̶�
		String taskType = request.getParameter("oaTaskType");//�������
		
		OATaskMgt tmgt = new OATaskMgt();
		
		OATaskBean bean = null;
		Result rs = null;
		
		if ("".equals(id)) {
			bean = new OATaskBean();
			bean.setId(IDGenerater.getId());
			bean.setCreateBy(userId);
			bean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		} else {
			bean = (OATaskBean)tmgt.loadTaskBean(id).retVal;
			if (null == bean) {
				MobileResult result = new MobileResult();
				result.setCode(-1);
				result.setDesc("��Ӧ�ļ�¼�����ڣ������ѱ�ɾ��");
				writeJson(result, response);				
			}
		}

		boolean flag = false;
		if ("".equals(id)) {
			id = IDGenerater.getId();
			flag = OATaskAction.addTask(userId, id, title, remark, executor, beginTime, endTime, surveyor, itemId, taskId, "0","","","","A","1");
		} else {
			flag = OATaskAction.updateTask(userId, id, title, remark, status, executor, beginTime, endTime, surveyor, itemId, schedule,bean.getClientId(),bean.getParticipant(),bean.getAffix(),"A","1");
		}
		
		if (flag) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "�����ɹ�");
			json.addProperty("id", id);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("����ʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ��������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getMyTask(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OATaskAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("������ģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("taskId", request, response) == false) {
			return;
		}

		String taskId = request.getParameter("taskId");
		
//		// ��������
//		itemId = "69a1025b_1311121334033140002";

		Result rs = mgt.getMyTask(taskId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			JsonArray jarr = (JsonArray) rs.getRetVal();
			if (jarr.size() > 0) {
				JsonObject json = (JsonObject) jarr.get(0);
				json.addProperty("code", 0);
				writeJson(json, response);
				return;
			}
		}
		MobileResult result = new MobileResult();
		result.setCode(-1);
		result.setDesc("��ѯʧ��");
		writeJson(result, response);
		
	}

	/**
	 * ��ʾ�ҵ������б�
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyTask(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OATaskAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("������ģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("type,status,count", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String type = request.getParameter("type");
		String status = request.getParameter("status");
		String count = request.getParameter("count");
		
//		// ��������
//		userId = "1";
//		type = "1";
//		status = "1";
//		count = "2";
		
		int nType = Integer.parseInt(type);
		int nCount = Integer.parseInt(count);

		Result rs = mgt.listMyTask(userId, nType, status, nCount);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)rs.getRetVal());
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ����̬
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyTaskLog(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OATaskAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("������ģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("taskId", request, response) == false) {
			return;
		}

		String taskId = request.getParameter("taskId");

//		// ��������
//		itemId = "69a1025b_1311121334033140002";
		
		Result rs = mgt.listMyTaskLog(taskId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			JsonArray jarr = (JsonArray) rs.getRetVal();
			for (int i = 0; i < jarr.size(); i++) {
				JsonObject j = (JsonObject) jarr.get(i);
				j.addProperty("content", GlobalsTool.replaceHTML(j.get("content").toString().replace("\"", "")));
			}
			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", jarr);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ������
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyTaskSonTask(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OATaskAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("������ģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("taskId", request, response) == false) {
			return;
		}

		String taskId = request.getParameter("taskId");

//		// ��������
//		itemId = "69a1025b_1311121334033140002";
		
		Result rs = mgt.listSonTask(taskId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {			
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray)rs.getRetVal());
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * �������̬
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addTaskLog(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OATaskAction.do");
		
		if (null == mop) {
			MobileResult rs = new MobileResult();
			rs.setCode(-1);
			rs.setDesc("������ģ��Ȩ��");
			writeJson(rs, response);
			return;
		}

		if (checkParameter("taskId,content,createTime,commentId,replyId", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String taskId = request.getParameter("taskId");
		String content = request.getParameter("content");
		String createTime = request.getParameter("createTime");
		String commentId = request.getParameter("commentId");
		String replyId = request.getParameter("replyId");

//		// ��������
//		itemId = "69a1025b_1311121334033140002";
//		content = "��������";
//		commentId = "69a1025b_1311121335464870021";
//		replyId = "1";
		
		String id = IDGenerater.getId();
		
		if (DiscussAction.add(userId, id, createTime, "OATaskLog", taskId, content, commentId, replyId, 0)) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "��ӳɹ�");
			json.addProperty("id", id);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("���ʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ�ҵ���־
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listWorkLog(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// �ݲ���Ȩ��

		if (checkParameter("creater,date", request, response) == false) {
			return;
		}

		String creator = request.getParameter("creater");
		String date = request.getParameter("date");
		

		Result rs = mgt.listMyLog(creator,date);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray) rs.getRetVal());
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * ��ʾ�ҵ���־����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getWorkLogDiscuss(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// �ݲ���Ȩ��
		if (checkParameter("logId", request, response) == false) {
			return;
		}

		String logId = request.getParameter("logId");

		Result rs = mgt.getWorkLogDiscuss(logId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.add("list", (JsonArray) rs.getRetVal());
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("��ѯʧ��");
			writeJson(result, response);
		}
	}

	/**
	 * �����־��̬
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addWorkLogDiscuss(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// �ݲ���Ȩ��
		if (checkParameter("logId,content,createTime,commentId,replyId", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String logId = request.getParameter("logId");
		String content = request.getParameter("content");
		String createTime = request.getParameter("createTime");
		String commentId = request.getParameter("commentId");
		String replyId = request.getParameter("replyId");

//		// ��������
//		itemId = "69a1025b_1311121334033140002";
//		content = "��������";
//		commentId = "69a1025b_1311121335464870021";
//		replyId = "1";
		
		String id = IDGenerater.getId();
		
		if (DiscussAction.add(userId, id, createTime, "OAWorkLogDiscuss", logId, content, commentId, replyId, 0)) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "��ӳɹ�");
			json.addProperty("id", id);
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("���ʧ��");
			writeJson(result, response);
		}
	}
	

	/**
	 * �����޸���־
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addUpdateWorkLog(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		// �ݲ���Ȩ��
		if (checkParameter("sid,workLogId,workLogType,workLogDate,workLogDetId,contentType,content", request, response) == false) {
			return;
		}

		String userId = request.getParameter("sid");
		String workLogId = request.getParameter("workLogId");
		String workLogType = request.getParameter("workLogType");
		String workLogDate = request.getParameter("workLogDate");
		String workLogDetId = request.getParameter("workLogDetId");
		String contentType = request.getParameter("contentType");
		String content = request.getParameter("content");
			
		Result rs=mgt.addUpdateLog(userId, workLogId, workLogType, workLogDate, workLogDetId, contentType, content);	
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 0);
			json.addProperty("desc", "�����ɹ�");
			json.addProperty("ids", rs.getRetVal().toString());
			writeJson(json, response);
		} else {
			MobileResult result = new MobileResult();
			result.setCode(-1);
			result.setDesc("����ʧ��");
			writeJson(result, response);
		}
	}
	
	private void download(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		String url = "";
		/* �ж��Ƿ��ǲݹ������ */
		String userAgent = request.getHeader("user-agent");
		if (userAgent != null
				&& (userAgent.toLowerCase().indexOf("ipad") != -1 || userAgent
						.toLowerCase().indexOf("iphone") != -1)) {
			url = "http://itunes.apple.com/cn/app/ke-rongm-office/id503063195?mt=8&ls=1";		
			
		} else {

			String strHttp = "http://" + request.getHeader("host");
			url = strHttp + "/common/mo.apk";
		}
		response.sendRedirect(url);
	}
	
	/**
	 * ��ѯ�ɼ�ģ��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listMyApp(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		
	}
	
	private void getFollowIds(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// �ݲ���Ȩ��
				if (checkParameter("userId", request, response) == false) {
					return;
				}

				String userId = request.getParameter("userId");

				Result rs = mgt.getFollowIds("'"+userId+"'", "");
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					JsonObject json = new JsonObject();
					json.addProperty("code", 0);
					json.add("list", (JsonArray) rs.getRetVal());
					writeJson(json, response);
				} else {
					MobileResult result = new MobileResult();
					result.setCode(-1);
					result.setDesc("��ѯʧ��");
					writeJson(result, response);
				}
	}
	private void warmToWriteLog(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// �ݲ���Ȩ��
				if (checkParameter("sid,beFollowId,teamLogDate,teamLogType", request, response) == false) {
					return;
				}

				String followId = request.getParameter("sid");
				String beFollowId = request.getParameter("beFollowId");
				String teamLogDate = request.getParameter("teamLogDate");
				String teamLogType = request.getParameter("teamLogType");
				
				Result rs = mgt.warmToWriteLog(followId, beFollowId, teamLogDate, teamLogType);
				MobileResult result = new MobileResult();
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					result.setCode(0);
					result.setDesc("���ѳɹ�");
					writeJson(result, response);
				} else {
					
					result.setCode(-1);
					result.setDesc("����ʧ��");
					writeJson(result, response);
				}
	}
}
