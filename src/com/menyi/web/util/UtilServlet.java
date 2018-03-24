package com.menyi.web.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import bsh.Interpreter;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.koron.aioshop.services.IAIOShopServices;
import com.koron.crm.service.ClientServiceMgt;
import com.koron.hr.review.HRReviewMgt;
import com.koron.oa.album.AlbumMgt;
import com.koron.oa.bean.AlbumBean;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.HTMLTemplate;
import com.koron.oa.bean.OAReadingRecord;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.PhotoInfoBean;
import com.koron.oa.message.MessageMgt;
import com.koron.oa.mydesktop.MyDesktopMgt;
import com.koron.oa.oaReadingRecord.OAReadingRecordMgt;
import com.koron.oa.publicMsg.advice.OAAdviceMgt;
import com.koron.oa.util.EmployeeMgt;
import com.koron.oa.workflow.OAWorkFlowTableMgt;
import com.koron.oa.workflow.template.OAWorkFlowTempMgt;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.bom.BomMgt;
import com.menyi.aio.web.call.CallMgt;
import com.menyi.aio.web.colconfig.ColConfigMgt;
import com.menyi.aio.web.coldisplay.ColDisplayMgt;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.goodsProp.GoodsPropMgt;
import com.menyi.aio.web.importData.ExportField;
import com.menyi.aio.web.importData.ImportThread;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.moduleFlow.ModuleFlowMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.tab.TabMgt;
import com.menyi.aio.web.tablemapped.TableMappedMgt;
import com.menyi.aio.web.upgrade.UpgradeMgt;
import com.menyi.aio.web.userFunction.DynAjaxSelect;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.aio.web.userFunction.UserFunctionAction;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserManageAction;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.email.EMailMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.msgcenter.server.MSGSession;
import com.menyi.msgcenter.server.MsgMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class UtilServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserMgt userMgt = new UserMgt();
	private static Object oLock = new Object();
	private GoodsPropMgt propMgt = new GoodsPropMgt();
	private OAReadingRecordMgt oaReadingRecordMgt = new OAReadingRecordMgt();
	private NotifyFashion notify = new NotifyFashion();
	private OAWorkFlowTableMgt workMgt=new OAWorkFlowTableMgt();
	private PublicMgt pubMgt = new PublicMgt();
	private static long curtime;
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String operation = req.getParameter("operation");
			
		if(BaseEnv.log.isDebugEnabled() && !operation.equals("msgData")){
			String uName = "";
			if(this.getLoginBean(req) != null){
				uName = this.getLoginBean(req).getEmpFullName();
			}
			BaseEnv.log.debug("UtilServlet.service 请求地址："+req.getRequestURI()+(req.getQueryString()==null?"":"?"+req.getQueryString()));
			String rd ="";
			for(Object key:req.getParameterMap().keySet()){
				Object value=req.getParameterMap().get(key);
				String values="";
				for(String v:(String[])value){
					values +=v+",";
				}
				rd +=key+"=["+values+"];";
			}
			
			BaseEnv.log.debug("UtilServlet.service 操作人:"+uName+";"+"请求数据："+rd);
		}
		
		
		// 因为此类将被频繁由ajax调用，故在此更新用户的心跳信息
		Object o = req.getSession().getAttribute("LoginBean");
		if (o != null) {
			LoginBean bean = (LoginBean) o;
			//String keycode = req.getParameter("keycode");
		    //String type = "mobile";
			OnlineUserInfo.refresh(bean.getId(),null);
		}
		
		

		if (operation.equals("currPeriod") || operation.equals("alertData")
				|| operation.equals("showEmp")) {
			if (getLoginBean(req) == null) {
				PrintWriter out = resp.getWriter();
				out.print("close");
				return;
			}
		}
		if (operation.equals("sunCompany")) {
			getSunCompany(req, resp);
		} else if (operation.equals("currencyRate")) {
			currencyRate(req, resp);
		} else if (operation.equals("Ajax")) {//弹出框自动回填，只取第一行
			dynSelect(req, resp);
		} else if (operation.equals("DropdownPopup")) {//下拉弹出框
			DropdownPopup(req, resp);
		} else if (operation.equals("PopupTitle")) {//取弹出框显示名
			PopupTitle(req, resp);
		}else if (operation.equals("DropdownChildData")) {//下拉子弹出窗
			DropdownChildData(req, resp);
		} else if (operation.equals("RecAutoSettlement")) {
			RecAutoSettlement(req, resp);
		} else if (operation.equals("ajaxPYM")) {
			dyGetPYM(req, resp);
		} else if (operation.equals("ajaxSpell")) {
			dyGetSpell(req, resp);
		} else if (operation.equals("msgData")) {
			msgData(req, resp);
		} else if (operation.equals("showEmp")) {
			showEmp(req, resp);
		} else if (operation.equals("currStyle")) {
			currStyle(req, resp);
		} else if (operation.equals("currLoc")) {
			currLoc(req, resp);
		} else if (operation.equals("validateField")) {
			validateField(req, resp);
		} else if (operation.equals("validateGoodsSeq")) {
			validateGoodsSeq(req, resp);
		} else if (operation.equals("isHasPopedom")) {
			isHasPopedom(req, resp);
		} else if (operation.equals("validateSeq")) {
			validateSeq(req, resp);
		} else if (operation.equals("getUserInfo")) {
			getUserInfo(req, resp);
		} else if (operation.equals("getUserLocStyle")) {
			getUserLocStyle(req, resp);
		} else if (operation.equals("comTel")) {// 来电：判断是否有该电话的公司信息
			isExistComByTel(req, resp);
		}else if (operation.equals("removeCheck")) {
			removeCheck(req, resp);
		} else if (operation.equals("promptSound")) {
			promptSound(req, resp);
		} else if (operation.equals("valpwd")) {
			validatePwd(req, resp);
		} else if (operation.equals("validateDetailSeq")) {
			validateDetailSeq(req, resp);
		} else if (operation.equals("getCurPeriod")) {
			getCurPeriod(req, resp);
		} else if (operation.equals("closeAccCourse")) {
			closeAccCourse(req, resp);
		} else if (operation.equals("hasVisitWorkPlan")) {
			hasVisitWorkPlan(req, resp);
		} else if (operation.equals("readErrorExcel")) {
			readErrorExcel(req, resp);
		} else if (operation.equals("importInfo")) {
			importInfo(req, resp);
		} else if (operation.equals("cancelImport")) {
			cancelImport(req, resp);
		} else if (operation.equals("getFieldInfo")) {
			getFieldInfo(req, resp);
		} else if (operation.equals("importTemplete")) {
			importTemplete(req, resp);
		} else if (operation.equals("addOAReadingInfo")) {
			addOAReadingInfo(req, resp);
		} else if (operation.equals("getDepartmentManagerByEmpId")) {
			getDepartmentManagerByEmpId(req, resp);
		} else if (operation.equals("calculate")) {
			getCalculateResult(req, resp);
		} else if ("sendBillMsg".equals(operation)) {
			sendBillMsg(req, resp);
		} else if ("existClient".equals(operation)) {
			existClient(req, resp);
		} else if ("displayGoal".equals(operation)) {
			displayGoal(req, resp);
		} else if ("displayYibiaopan".equals(operation)) {
			displayYibiaopan(req, resp);
		}  else if ("updateQA".equals(operation)) {
			updateQA(req, resp);
		} else if ("updateAlertStatus".equals(operation)) {
			updateAlertStatus(req, resp);
		} else if ("enumDis".equals(operation)) {
			enumDis(req, resp);
		} else if ("testHttp".equals(operation)) {
			testHppt(req, resp);
		} else if ("qtySplit".equals(operation)) {
			qtySplit(req, resp);
		} else if ("saveMrpQty".equals(operation)) {
			saveMrpQty(req, resp);
		} else if ("freshAIOShop".equals(operation)) {
			freshAIOShop(req, resp);
		} else if ("lockColumn".equals(operation)) {
			lockColumn(req, resp);
		} else if ("colconfig".equals(operation)) {
			colconfig(req, resp);
		} else if ("defaultConfig".equals(operation)) {
			defaultConfig(req, resp);
		} else if ("displayMS2line".equals(operation)) {
			displayMS2line(req, resp);
		} else if ("workFlowIsStand".equals(operation)) {
			workFlowIsStand(req, resp);
		} else if ("getFlowDesc".equals(operation)) {
			getFlowDesc(req, resp);
		} else if ("saveAsModule".equals(operation)) {
			saveAsModule(req, resp);
		} else if ("cancelAlert".equals(operation)) {
			cancelAlert(req, resp);
		} else if ("BillNotFinish".equals(operation)) {
			billNotFinish(req, resp);
		} else if ("setFormatScope".equals(operation)) {
			setFormatScope(req, resp);
		} else if ("setFormatStyle".equals(operation)) {
			setFormatStyle(req, resp);
		} else if ("uploadFile".equals(operation)) {
			uploadFile(req, resp);
		} else if ("changeViewMenu".equals(operation)) {
			changeViewMenu(req, resp);
		} else if ("queryAllName".equals(operation)) {
			queryAllName(req, resp);
		} else if ("queryHTMLModule".equals(operation)) {
			queryHTMLModule(req, resp);
		} else if ("queryHTMLModuleID".equals(operation)) {
			queryHTMLModuleID(req, resp);
		} else if ("fameTopWish".equals(operation)) {
			addfameWish(req, resp);
		} else if ("fameTopWishtype".equals(operation)) {
			queryfameWishType(req, resp);
		} else if ("createNewFile".equals(operation)) {
			createNewFile(req, resp);
		}  else if ("adviceData".equals(operation)) {
			adviceData(req, resp);
		} else if ("addAlert".equals(operation)) {
			addAlert(req, resp);
		} else if ("alertDetail".equals(operation)) {
			alertDetail(req, resp);
		} else if ("deleteAlert".equals(operation)) {
			deleteAlert(req, resp);
		} else if ("uploadPhos".equals(operation)) {
			downLoadFiles(req, resp);
		} else if ("validateBPSeq".equals(operation)){
			validateBPSeq(req, resp) ;
		} else if ("menu".equals(operation)){
			showMenu(req,resp) ;
		} else if("menu2".equals(operation)){
			showMenu2(req,resp) ;
		}  else if("screenWidth".equals(operation)){
			screenWidth(req,resp) ;
		} else if("exportFlow".equals(operation)){
			exportFlow(req,resp);
		}else if("downSelect".equals(operation)){
			//下拉选择
			downSelect(req,resp);
		}else if("viewMenu".equals(operation)){
			//默认菜单显示还是快捷显示
			viewMenu(req,resp);
		}else if("setDesk".equals(operation)){
			//设置桌面
			setDesk(req,resp);
		}else if("pushBill".equals(operation)){
			//推送单据
			pushBill(req,resp);
		}else if("mailSendHistory".equals(operation)){
			//发送邮件人的历史记录
			mailSendHistory(req,resp);
		}else if("postFormula".equals(operation))//反馈建议发贴
		{
			postFormula(req,resp);
		}else if("getBrotherRow".equals(operation)){
			//下拉选择
			getBrotherRow(req,resp);
		}else if ("startFlow".equals(operation)) { //自定义模块启用审核流
			startFlow(req, resp);
		}
	}
	/**
	 * 启用审核流
	 * @param req
	 * @param resp
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void startFlow(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		LoginBean lb = getLoginBean(req);
		if(lb == null){
			out.print("true");
		}else{
			String tableName = req.getParameter("tableName");
			String mailModule = req.getParameter("MainModule");
			Result rs;
			if(tableName ==null || tableName.length() == 0){
				String moduleId= req.getParameter("moduleId");
				rs = new PublicMgt().getModuleAddress(moduleId);
				if(rs.retVal == null){
					out.print("未找到对应模块");
					return;
				}
				String[] rets = (String[])rs.retVal;
				String linkAddress = String.valueOf(rets[0]);
				String classCode= rets[1];
				mailModule = rets[2];
				if(!linkAddress.startsWith("/UserFunctionQueryAction.do")){
					out.print("只有自定义单据才可启用审核流");
					return;
				}
				int pos = linkAddress.indexOf("tableName=")+"tableName=".length();
				
				if(linkAddress.indexOf("&",pos)>0){
					tableName = linkAddress.substring(pos,linkAddress.indexOf("&",pos));
				}else{
					tableName = linkAddress.substring(pos);
				}
			}
			DBTableInfoBean bean = BaseEnv.tableInfos.get(tableName);
			if(bean == null){
				out.print("表结构不存在");
				return;
			}
			rs = new PublicMgt().getFlowByTableName(tableName);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal !=null && rs.retVal.toString().length() > 0){
				out.print("ID="+rs.retVal);
				return;
			}
			
			String locale = "";
			Object lo = req.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
			if (lo == null) {					
				lo = req.getSession().getServletContext().getAttribute(
				"DefaultLocale");
			}
			locale = lo== null ? "" : lo.toString();
			String id = IDGenerater.getId();
			rs = new PublicMgt().startFlow(id, bean.getDisplay().get(locale), "3".equals(mailModule)?"00003":"00001", 
					tableName, id+".xml", tableName+"_Del_One", tableName+"_Add_One", lb.getId());			
			if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
				out.print("ID="+id);
			}else{
				out.print("执行失败");
			}
			
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 取邻居表行数
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void getBrotherRow(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		TabMgt bMgt=new TabMgt();
		String tableName=request.getParameter("tableName");
		
		DBTableInfoBean bean = GlobalsTool.getTableInfoBean(tableName);
		String sb = "";
		if(bean.getBrotherType() == 1){ //一对多邻居表，直接显示列表
			sb= "list";
		}else{
			String brotherId=request.getParameter("brotherId");
			Result rs=bMgt.getBrotherRow(tableName, brotherId);
			
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				ArrayList list=(ArrayList)rs.retVal;
				if(list.size() == 0){
					sb = "add";
				}else{
					sb = list.get(0)+"";
				}				
			}else{
				sb = "add";
			}
		}
		
        PrintWriter out = response.getWriter();
        out.print(sb) ;
	}
	
	
	
	/**
	 * 导出工作流表单
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void exportFlow(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		String keyId=request.getParameter("keyId");
		String view=request.getParameter("view");
		
		String tableName = request.getParameter("tableName");
		String designId = request.getParameter("designId");
		Result rs=null;
		String flowContent="";
		if(!"true".equals(view)){
			rs=workMgt.getNewTable(tableName,designId);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				List obj=(List)rs.retVal;
				if(obj.size()>0){
					Object[] objlist=(Object[]) obj.get(0);
					flowContent=objlist[2].toString();
				}
			}
		}else{
			rs=workMgt.getTableById(keyId);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				List obj=(List)rs.retVal;
				Object[] objList=(Object[]) obj.get(0);
				flowContent=(String) objList[0];
			}
		}
		
		//导出表单时，替换html文本中"field_"的内容为空，防止再次导入时字段报错
		flowContent=flowContent.replace("field_", "");
		
		
		Result rsTemplate=workMgt.getWorkTemplate(designId);
		String fileName="";
		if(rsTemplate.retCode==ErrorCanst.DEFAULT_SUCCESS){
			List obj=(List)rsTemplate.retVal;
			if(obj.size()>0){
				Object[] objlist=(Object[]) obj.get(0);
				fileName=objlist[1].toString();
			}
		}
		fileName=fileName+".html";
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
		response.getOutputStream().write(flowContent.getBytes());
	}
	
	/**
	 * 导航式的新版界面显示菜单
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void showMenu2(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		response.setContentType("text/plain; charset=UTF-8");
		String defSys = request.getParameter("defSys") ;
		
		String locale = GlobalsTool.getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
        loginBean.setDefSys(defSys);
        
        if(defSys!=null){
	        Hashtable sessionSet = BaseEnv.sessionSet;
	        Hashtable session=((Hashtable)sessionSet.get(loginBean.getId()));
	        session.put("sysType", defSys);
        }
        int pageNo = getParameterInt("pageNo", request);
        if(pageNo==0){
        	pageNo = 1 ;
        }
        StringBuilder sb = new StringBuilder();
		Result result = new ModuleFlowMgt().queryModuleMenu(defSys,locale, 9, pageNo);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null){
			sb.append("{\"total\":\""+result.totalPage+"\",\"menuList\":[");
			ArrayList objList = (ArrayList) result.retVal;
			for(int i=0;i<objList.size();i++){
				Object[] obj = (Object[]) objList.get(i);
				sb.append("{\"id\":\""+obj[0]+"\",\"name\":\""+obj[1]+"\",\"tab\":\""+obj[2]+"\"},");
			}
			if(sb.toString().endsWith(",")){
				sb.delete(sb.length()-1, sb.length());
			}
			sb.append("]}");
		}
        PrintWriter out = response.getWriter();
        out.print(sb.toString()) ;
        out.flush();
		out.close();
	}
	
	/**
	 * 默认菜单显示还是快捷显示
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void viewMenu(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		response.setContentType("text/plain; charset=UTF-8");
		String viewMenu = getParameter("viewType", request);
		LoginBean login = getLoginBean(request);
		EmployeeBean empBean = (EmployeeBean) userMgt.detail(login.getId()).retVal;
		if(empBean != null){
			empBean.setViewTopMenu(viewMenu);
			userMgt.update(empBean);
			
			login.setViewTopMenu(viewMenu);
			request.getSession().setAttribute("LoginBean", login);
		}
	
		PrintWriter out = response.getWriter();
	    out.flush();
		out.close();
	}
	
	/**
	 * 新版界面显示菜单
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void showMenu(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		
		String defSys = request.getParameter("defSys") ;
		
		LoginBean loginBean = getLoginBean(request);
        loginBean.setDefSys(defSys);
        
        if(defSys!=null){
	        Hashtable sessionSet = BaseEnv.sessionSet;
	        Hashtable session=((Hashtable)sessionSet.get(loginBean.getId()));
	        session.put("sysType", defSys);
        }
        
		String keyWord = request.getParameter("keyWord");
		keyWord = GlobalsTool.toChinseChar(keyWord) ;
		String locale = GlobalsTool.getLocale(request).toString() ;
		StringBuffer sb = new StringBuffer();
        //如果非admin用户
        ArrayList menu;
        sb.append("[");
        if(keyWord!=null && keyWord.trim().length()>0){
        	keyWord = keyWord.toLowerCase() ;
    		//关键字查询
        	for(int i=1;i<5;i++){
        		if(!"admin".equalsIgnoreCase(loginBean.getName())){
                    menu = loginBean.getMenus(String.valueOf(i));
                }else{
                    menu = (ArrayList) BaseEnv.moduleMap.get(String.valueOf(i));
                }
        		if (menu!=null && menu.size() > 0) {
                    for (int j = 0; j < menu.size(); j++) {
                        ModuleBean moduleBean = (ModuleBean) menu.get(j);
                        if(moduleBean.getIsHidden()!=1){
                        	rec2(sb, moduleBean, true, locale, keyWord) ;
                        }
                    }
                }
        	}
        	if(sb.toString().endsWith(",")){
        		sb.delete(sb.length()-1, sb.length()) ;
        	}
    	}else{
    		if(!"admin".equalsIgnoreCase(loginBean.getName())){
                menu = loginBean.getMenus(defSys);
            }else{
                menu = (ArrayList) BaseEnv.moduleMap.get(defSys);
            }
    		if (menu!=null && menu.size() > 0) {
                for (int i = 0; i < menu.size(); i++) {
                    ModuleBean moduleBean = (ModuleBean) menu.get(i);
                    if(moduleBean.getIsHidden()!=1){
                		sb.append("{strUrl:'',name:'"+moduleBean.getModelDisplay().get(locale)+"',isParent:true,nodes:[") ;
                    	rec(sb, moduleBean,true,defSys,locale);
                    	sb.delete(sb.length()-1, sb.length()) ;
                    	sb.append("]},");
                    }
                }
                sb.delete(sb.length()-1, sb.length()) ;
            }
    	}
        
		sb.append("]") ;
        PrintWriter out = response.getWriter();
        out.print(sb.toString()) ;
	}
	
	/**
	 * 循环遍历菜单
	 * @param sb
	 * @param moduleBean
	 */
	private void rec(StringBuffer sb, ModuleBean moduleBean,boolean root,String sysType,String locale) {
        for (int i = 0; moduleBean.getChildList() != null && i < moduleBean.getChildList().size(); i++) {
            ModuleBean childBean = (ModuleBean) moduleBean.getChildList().get(i);
            if(childBean.getIsHidden()!=1){
	            if (childBean.getChildList() != null && childBean.getChildList().size() > 0) {
	            	sb.append("{strUrl:'',name:'"+childBean.getModelDisplay().get(locale)+"',icon:'1'") ;
	            	if("0".equals(moduleBean.getMainModule())||sysType.equals(moduleBean.getMainModule())){
	            		sb.append(",isParent:true,nodes:[") ;
	            		rec(sb, childBean, false,sysType,locale);
	            		if(sb.toString().endsWith(",")){
	            			sb.delete(sb.length()-1, sb.length()) ;
	            		}
	            		sb.append("]") ;
	            	}
	                sb.append("},") ;
	            } else {
	            	if(childBean.getMainModule().equals("0")||childBean.getMainModule().equals(sysType)){
		                String url = childBean.getLinkAddress();
		                String display = childBean.getModelDisplay().get(locale) ;
		                if (display!=null && display.trim().length()>0) {
		                    if (url != null && url.length() > 0 && url.indexOf("src=menu") == -1) {
		                        if (url != null && url.indexOf("?") > 0) {
		                            url += "&src=menu";
		                        } else {
		                            url += "?src=menu";
		                        }
		                    }
		                    sb.append("{strUrl:'"+url+"',name:'"+display+"'},") ;
		                } 
	            	}
	            }
	        }
        } 
	}
	
	/**
	 * 循环遍历菜单
	 * @param sb
	 * @param moduleBean
	 */
	private void rec2(StringBuffer sb, ModuleBean moduleBean,boolean root,String locale,String keyWord) {
        for (int i = 0; moduleBean.getChildList() != null && i < moduleBean.getChildList().size(); i++) {
            ModuleBean childBean = (ModuleBean) moduleBean.getChildList().get(i);
            if(childBean.getIsHidden()!=1){
	            if (childBean.getChildList() != null && childBean.getChildList().size() > 0) {   	
	            	rec2(sb, childBean, false,locale,keyWord);
	            } else {
	            	String display = childBean.getModelDisplay().get(locale) ;
	            	String pingying = ChineseSpelling.getPYM(display) ;
	            	String pingyingw = ChineseSpelling.getSelling(display) ;
	            	if(display.contains(keyWord) || pingying.contains(keyWord) || pingyingw.contains(keyWord)){
		                String url = childBean.getLinkAddress();
		                if (url != null && url.length() > 0) {
		                    if (url.indexOf("src=menu") == -1) {
		                        if (url != null && url.indexOf("?") > 0) {
		                            url += "&src=menu";
		                        } else {
		                            url += "?src=menu";
		                        }
		                    }
		                } 
		                sb.append("{strUrl:'"+url+"',name:'"+display+"'},") ;
	            	}
	            }
	        }
        } 
	}
	
	/**
	 * 删除通知以及一些相关联的数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void deleteAlert(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		String relationId = request.getParameter("relationId");
		Result result = new EMailMgt().deleteAlerts(relationId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			new AdviceMgt().deleteByRelationId(relationId, "");
			 request.setAttribute("dealAsyn", "true");	
			 EchoMessage.success().add(getMessageResources(request,
			 "tel.msg.cancelSuccess"))
			 .setAlertRequest(request);
			 request.getRequestDispatcher("/common/alert.jsp").forward(request,
			 response) ;
		} else {
			// 取消失败
			 EchoMessage.error().add(getMessageResources(request,
			 "common.alert.unsuccess"))
			 .setAlertRequest(request);
			 request.getRequestDispatcher("/common/alert.jsp").forward(request,
			 response) ;
		}
	}

	/**
	 * wxq 添加或修改提醒
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void addAlert(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		LoginBean loginBean = getLoginBean(request);
		String relationId = request.getParameter("relationId");
		String alertDate = request.getParameter("alertDate"); /* 提醒日期 */
		String alertHour = request.getParameter("alertHour"); /* 提醒小时 */
		String alertMinute = request.getParameter("alertMinute"); /* 提醒分钟 */
		String alertContent = request.getParameter("alertContent"); /* 提醒内容 */
		String isLoop = request.getParameter("isLoop"); /* 循环提醒 */
		String loopType = request.getParameter("loopType"); /* 循环类型 */
		int loopTime = Integer.parseInt(request.getParameter("loopTime")); /* 循环次数 */
		String endDate = request.getParameter("endDate"); /* 结束日期 */
		String[] alertType = request.getParameterValues("alertType"); /* 提醒方式 */
		String popedomUserIds = request.getParameter("popedomUserIds"); /* 提醒用户ID */
		String popedomDeptIds = request.getParameter("popedomDeptIds"); /* 提醒部门ID */

		String falg = request.getParameter("falg");
		String alertUrl = "";
		String typestr = GlobalsTool.toChinseChar(request
				.getParameter("typestr")); /* 连接的显示文字 */
		String urls = GlobalsTool.toChinseChar(request.getParameter("urls")); /* 连接的路径 */
		if ("false".equals(falg)) {
			popedomUserIds = loginBean.getId() + ",";
		}
		String title = GlobalsTool.toChinseChar(request.getParameter("title")); /* 连接的标题 */
		if (alertContent != null) {
			alertContent = GlobalsTool.toChinseChar(alertContent);
			alertUrl = "<a href=\"javascript:mdiwin('" + urls + "','" + title
					+ "')\">" + typestr + alertContent + "</a>"; /* 得到总的连接路径 */
		} else {
			alertContent = title;
			alertUrl = "<a href=\"javascript:mdiwin('" + urls + "','" + title
					+ "')\">" + typestr + "</a>"; /* 得到总的连接路径 */
		}
		String strAlertType = "";
		for (String strType : alertType) {
			strAlertType += strType + ",";
		}
		if (strAlertType.length() == 0) {
			out.print("");
		}
		if (popedomDeptIds == null || popedomDeptIds == null) {
			popedomUserIds = loginBean.getId();
		}

		AlertBean alertBean = new AlertBean();
		alertBean.setId(IDGenerater.getId());
		alertBean.setAlertDate(alertDate);
		alertBean.setAlertHour(Integer.parseInt(alertHour));
		alertBean.setAlertMinute(Integer.parseInt(alertMinute));
		alertBean.setAlertContent(alertContent);
		alertBean.setIsLoop(isLoop);
		alertBean.setLoopType(loopType);
		alertBean.setLoopTime(loopTime);
		alertBean.setEndDate(endDate);
		alertBean.setAlertType(strAlertType);
		alertBean.setCreateBy(loginBean.getId());
		alertBean.setCreateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		alertBean.setRelationId(relationId);
		alertBean.setNextAlertTime(alertDate + " " + alertHour + ":"
				+ alertMinute + ":00");
		alertBean.setStatusId(0);
		alertBean.setPopedomUserIds(popedomUserIds);
		alertBean.setPopedomDeptIds(popedomDeptIds);
		alertBean.setAlertUrl(alertUrl);
		Result result = new EMailMgt().addAlert(alertBean);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			添加成功
			 request.setAttribute("dealAsyn", "true");
			 EchoMessage.success().add(getMessageResources(request,"mywork.lb.addwarm")).setAlertRequest(request);
			 request.getRequestDispatcher("/common/alert.jsp").forward(request,
			 response) ;
		} else {
			// 添加失败
			// EchoMessage.error().add(getMessageResources(request,
			// "common.msg.addFailture"))
			// .setAlertRequest(request);
			// request.getRequestDispatcher("/common/alert.jsp").forward(request,
			// response) ;
		}
	}

	/**
	 * wxq 显示提醒
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void alertDetail(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("utf-8");
		String relationId = request.getParameter("relationId");// 关联Id
		/* 判断是否存提醒设置 */
		Result result = new EMailMgt().loadAlertByEamilId(relationId);		
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AlertBean alertbean = (AlertBean)result.retVal;
			alertbean.setAlertContent(this.replaceBlank(alertbean.getAlertContent()));
			request.setAttribute("alert", result.retVal);
		}else{
			LoginBean loginBean = this.getLoginBean(request);
			request.setAttribute("userName", loginBean.getEmpFullName());
			request.setAttribute("userId", loginBean.getId());
			
		}
		String typestr = GlobalsTool.toChinseChar(request
				.getParameter("typestr"));
		System.out.println(GlobalsTool.encodeHTML(typestr));
		String urls = GlobalsTool.toChinseChar(request.getParameter("urls"));
		String title = GlobalsTool.toChinseChar(request.getParameter("title"));
		String falg = GlobalsTool.toChinseChar(request.getParameter("falg"));
		request.setAttribute("relationId", relationId);
		request.setAttribute("typestr",typestr);
		request.setAttribute("urls", urls);
		request.setAttribute("title", title);
		request.setAttribute("falg", falg);
		request.getRequestDispatcher("/vm/util/commonAlert.jsp").forward(
				request, response);
	}
	private String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;		
	}

	private void changeViewMenu(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LoginBean lb = (LoginBean) request.getSession().getAttribute(
				"LoginBean");
		UserMgt um = new UserMgt();
		String type = request.getParameter("type");
		String value = request.getParameter("value");
		um.changeViewMenu(lb.getId(), type, value);
		if (type.equals("viewTopMenu")) {
			lb.setViewTopMenu(value);
		} else {
			lb.setViewLeftMenu(value);
		}
		PrintWriter out = response.getWriter();
		out.print("");
	}

	private void billNotFinish(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String keyId = request.getParameter("keyId");
		String tableNames = new OAWorkFlowTempMgt().selectExistBillNotFinish2(
				keyId.split(","), GlobalsTool.getLocale(request).toString());
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(tableNames);
	}

	/**
	 * 设置打印样式的人员和部门范围权限
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void setFormatScope(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String reportId = request.getParameter("reportId");
		String scopeType = request.getParameter("scopeType");
		String userIds = request.getParameter("userIds");
		String deptIds = request.getParameter("deptIds");

		if ("deptIds".equals(scopeType)) {
			userIds = deptIds;
		}
		Result result = new ReportSetMgt().setFormatScope(scopeType, userIds,
				reportId);
		
		Result rl = new ReportSetMgt().getFormatById(reportId,GlobalsTool.getLocale(request).toString());
		if(rl.retVal != null && result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String[] rset = (String[])rl.retVal;
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),
				"设置样式范围:"+rset[0]+":"+rset[1], "tblReportsDet", "打印样式设计","");
		}
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.print("OK");
		} else {
			out.print("NO");
		}
	}
	
	
	/**
	 * 验证序列号是否存在，并返回商品名称和属性
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void validateBPSeq(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=utf-8");
		String seq = request.getParameter("Seq");
		String stockCode = request.getParameter("StockCode") ;
		String goodsCode = request.getParameter("GoodsCode") ;
		String popupBean = request.getParameter("popupBean");
		
		PrintWriter out = response.getWriter();
		Result result;
		
		PopupSelectBean selectBean = (PopupSelectBean)BaseEnv.popupSelectMap.get(popupBean);
		String sql = selectBean.getPopsql();
		
		
		
		if(seq==null || seq.length()==0){
			out.print("noSeq") ;
			return;
		}
		
		//先查询出对应seq 的仓库
		Result rs = propMgt.queryGoodsBySeq(stockCode, seq);
		if(rs.retVal==null){
			out.print("noSeq") ;
			return;
		}
		stockCode = ((String[])rs.retVal)[0];
		goodsCode = ((String[])rs.retVal)[2];		
		String StockFullName= ((String[])rs.retVal)[1];
		String GoodsNumber= ((String[])rs.retVal)[3];
		if("SelectQtyForReSales".equals(popupBean)){
			//预售单
			if(stockCode!=null && stockCode.trim().length()>0){
				sql = sql.replace("@ValueofDB:StockCode", stockCode) ;
				sql = sql.replace("@ValueofDB:@TABLENAME_StockCode", stockCode) ;
			}
			int rootCount=0;
			LoginBean loginBean = (LoginBean)request.getSession().getAttribute("LoginBean");
			while(sql.indexOf("@Sess:")>0 ){
            	rootCount++;
            	if(rootCount>200){
            		throw new RuntimeException("UtilServlet 处理@Sess时陷入死循环");
            	}
            	Pattern pattern = Pattern.compile("@Sess:([\\w]+)");
        		Matcher matcher = pattern.matcher(sql);
        		String sName = "";
        		if(matcher.find()){
        			sName =matcher.group(1);
        		}else{
        			throw new RuntimeException("导入解释@Sess失败UtilServlet");
        		}
        		
            	Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(loginBean.getId());
            	String sValue =sessionSet != null&&sessionSet.get(sName)!=null?sessionSet.get(sName).toString():"";
            	sql=sql.replaceFirst("@Sess:"+sName, sValue);
            }
			rootCount=0;
			sql = sql.replaceAll("@TABLENAME", "tblStockDet");
            while(sql.indexOf("@ValueofDB:")>0){
            	rootCount++;
            	if(rootCount>200){
            		throw new RuntimeException("UtilServlet 处理@ValueofDB时陷入死循环");
            	}
            	Pattern pattern = Pattern.compile("@ValueofDB:([\\w]+)");
        		Matcher matcher = pattern.matcher(sql);
        		String sName = "";
        		if(matcher.find()){
        			sName =matcher.group(1);
        		}else{
        			throw new RuntimeException("导入解释@ValueofDB失败UtilServlet");
        		}        		
        		//找到该值前换为1=1
        		sql=sql.replaceAll("(and|or|AND|OR)[\\s]*[\\w|\\.]+[\\s]*(=|!=|(like)|(LIKE)|>|>=|<|<=)[\\s]*[']{0,1}@ValueofDB:"+sName+"['\\s]{1}", " ");
        		sql=sql.replaceAll("[\\s]*[\\w|\\.]+[\\s]*(=|!=|(like)|(LIKE)|>|>=|<|<=)[\\s]*[']{0,1}@ValueofDB:"+sName+"['\\s]{1}[\\s]*(and|or|AND|OR)", " ");
        		sql=sql.replaceAll("[\\s]*[\\w|\\.]+[\\s]*(=|!=|(like)|(LIKE)|>|>=|<|<=)[\\s]*[']{0,1}@ValueofDB:"+sName+"['\\s]{1}[\\s]*", " 1=1 ");
            }
            sql +=" where tblStocks.seq = '"+seq+"' or tblGoodsOutStockLockTemp.Seq = '"+seq+"' ";
		}else{
			if(stockCode!=null && stockCode.trim().length()>0){
				sql = sql.replace("@ValueofDB:StockCode", stockCode) ;
				sql = sql.replace("@ValueofDB:@TABLENAME_StockCode", stockCode) ;
			}
			String condition = selectBean.getCondition() ;
			if(condition!=null && condition.trim().length()>0){
				sql += "where tblStocks.seq = '"+seq+"' " ;
			}
		}
		
		result = propMgt.queryGoodsBySeq(sql, selectBean, GlobalsTool.getLocale(request).toString(), seq) ;
		

		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal!=null) {
			String saveVal = ""; // 保存需显示的值
			String returnVal = ""; // 保存需返回的值
			Object[] os = (Object[]) result.retVal;
			// 先找出需返回的值
			for (int j = 0; j < selectBean.getReturnFields().size(); j++) {
				PopField fv = (PopField) selectBean.getReturnFields().get(j);
				for (int k = 0; k < selectBean.getAllFields().size(); k++) {
					PopField fv2 = (PopField) selectBean.getAllFields().get(k);
					if (fv.getFieldName().equals(fv2.getFieldName())) {
						String osstr = os[k] + "";
						DBFieldInfoBean tempfib = GlobalsTool.getFieldBean(selectBean.getTableName(fv.fieldName),selectBean.getFieldName(fv.fieldName));
						if (tempfib == null && fv.display != null&& fv.display.indexOf(".")> -1) {
							tempfib = GlobalsTool.getFieldBean(selectBean.getTableName(fv.display), selectBean.getFieldName(fv.display));
						}
						if (tempfib != null &&tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
							if (osstr == null || osstr.equals("null") || osstr.length() == 0) {
								osstr = "0";
							}
							DBTableInfoBean tempTableInfo = (DBTableInfoBean) allTables.get(selectBean.getTableName(fv.fieldName));
							if (tempTableInfo == null && fv.display != null) {
								tempTableInfo = (DBTableInfoBean) allTables.get(selectBean.getTableName(fv.display));
							}
							boolean isMain = (tempTableInfo.getTableType() == 0 ? true : false);
							osstr = GlobalsTool.newFormatNumber(new Double(osstr), false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()),
									selectBean.getTableName(fv.fieldName),selectBean.getFieldName(fv.fieldName),isMain);
						}
						returnVal = returnVal + osstr.trim() + ";";
						if (fv.compare && (fv.returnToIdentity==null||fv.returnToIdentity.length()==0)) {
							saveVal = saveVal + osstr.trim() + ";";
						}
						break;
					}
				}
			}
			out.print(saveVal + "@@" + returnVal+"@@"+stockCode+"@@"+goodsCode+"@@"+StockFullName+"@@"+goodsCode);
		}else{
			out.print("noSeq") ;
		}
	}

	
	/**
	 * 添加或者删除打印样式
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void setFormatStyle(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String reportId = request.getParameter("reportId");
		String styleName = request.getParameter("styleName");
		String reportNumber = request.getParameter("reportNumber");
		ReportSetMgt rsmgt = new ReportSetMgt();
		Result result = new Result();
		if (styleName != null && styleName.length() > 0) {
			styleName = GlobalsTool.toChinseChar(styleName);
			String formatFileName = reportNumber + "Format_"
					+ IDGenerater.getId() + ".xml";
			result = rsmgt.insertNowFormatName(reportNumber,
					formatFileName, styleName, GlobalsTool.getLocale(request).toString());
			Result rl = rsmgt.getReportSetInfo(reportNumber,GlobalsTool.getLocale(request).toString());
			if(rl.retVal != null){
				new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),
					((ReportsBean)rl.retVal).getReportName()+":"+styleName, "tblReportsDet", "打印样式设计","");
			}
		} else {
			Result rl = rsmgt.getFormatById(reportId,GlobalsTool.getLocale(request).toString());
			result = rsmgt.deleteFormatStyle(reportId);			
			if(rl.retVal != null && result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				String[] rset = (String[])rl.retVal;
				new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),
						rset[0]+":"+rset[1], "tblReportsDet", "打印样式设计","");
			}
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.print("OK");
		} else {
			out.print("NO");
		}
	}

	/**
	 * HTML文本编辑器
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws Exception
	 */
	private void uploadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		// 获取图片存放路径
		String savePath = request.getSession().getServletContext().getRealPath(
				"upload");

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		File uploadDir = new File(savePath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		// 最大文件大小
		// long maxSize = 1000000;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List items = new ArrayList();
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}
		Iterator itr = items.iterator();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			String fileName = item.getName();
			if (!item.isFormField()) {
				// 检查文件大小
				// if(item.getSize() > maxSize){
				// out.print(getError("上传文件大小超过限制。"));
				// return;
				// }
				// 检查扩展名
				String fileExt = fileName.substring(
						fileName.lastIndexOf(".") + 1).toLowerCase();
				if (!Arrays.<String> asList(extMap.get(dirName).split(","))
						.contains(fileExt)) {
					out.print(getError("对不起，你上传的文件不符要求，目前只支持"
							+ extMap.get(dirName) + "格式。"));
					return;
				}
				long time;
				synchronized (oLock) {
					time = System.currentTimeMillis();
					while (time <= curtime) {
						try {
							Thread.sleep(2);
						} catch (InterruptedException ex) {
						}
						time = System.currentTimeMillis();
					}
					curtime = time;
				}
				String newFileName = time + "." + fileExt;
				try {
					File uploadedFile = new File(savePath, newFileName);
					item.write(uploadedFile);
				} catch (Exception e) {
					out.print(getError("上传文件失败。"));
					return;
				}
				JsonObject obj = new JsonObject();
				obj.addProperty("error", 0);
				obj.addProperty("url", "/upload/" + newFileName);
				out.print(obj.toString());
			}
		}
	}

	private String getError(String message) {
		JsonObject obj = new JsonObject();
		obj.addProperty("error", 1);
		obj.addProperty("message", message);
		return obj.toString();
	}

	

	/**
	 * 设置字段的枚举显示
	 * 
	 * @param request
	 * @param response
	 */
	private void enumDis(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String tableName = request.getParameter("tableName");
		String fieldName = request.getParameter("fieldName");
		if (fieldName.indexOf(".") > 0) {
			tableName = fieldName.substring(0, fieldName.indexOf("."));
			fieldName = fieldName.substring(fieldName.indexOf(".") + 1);
		}
		String dis = "";
		
		DBFieldInfoBean field = GlobalsTool.getFieldBean(tableName, fieldName);
		EnumerateBean beans[] = (EnumerateBean[]) BaseEnv.enumerationMap.values().toArray(new EnumerateBean[0]);
		String locale = GlobalsTool.getLocale(request).toString();
		for (int i = 0; i < beans.length; i++) {
			if (beans[i].getEnumName().equals(field.getRefEnumerationName())) {
				for (int j = 0; j < beans[i].getEnumItem().size(); j++) {
					EnumerateItemBean eib = (EnumerateItemBean) beans[i]
							.getEnumItem().get(j);
					dis += "<input type=checkbox style='width:30px;' name=\""+fieldName+"\" value=\"" + eib.getEnumValue() + "\">"
					+ ((KRLanguage) (eib.getDisplay())).get(locale)
					+ "</input>";
				
				}
				break;
			}
		}
		System.out.println("dis值："+dis);
		out.print(dis);
	}



	private void closeAccCourse(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(BaseEnv.CLOSE_ACC);
	}

	private void getCurPeriod(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String sunCompany = request.getSession().getAttribute("SCompanyID")
				.toString();
		AccPeriodBean bean = BaseEnv.accPerios.get(sunCompany);
		PrintWriter out = response.getWriter();
		if (bean == null) {
			out.print("-1");
		} else {
			out.print(bean.getAccPeriod());
		}
	}

	private void validateDetailSeq(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String tableName = request.getParameter("tableName");
		String fieldName = request.getParameter("validateField");
		DBFieldInfoBean fieldInfo = GlobalsTool.getFieldBean(tableName,
				fieldName);
		PopupSelectBean selectBean = fieldInfo.getSelectBean();
		ArrayList tabParam = selectBean.getTableParams();
		HashMap mainParam = new HashMap();
		String mainField = "";
		String value = "";
		for (int i = 0; i < tabParam.size(); i++) {
			mainField = tabParam.get(i).toString();
			if (mainField.indexOf("@TABLENAME") >= 0) {
				mainField = tableName + "_"
						+ mainField.substring(mainField.indexOf("_") + 1);
			}
			value = request.getParameter(mainField) == null ? "" : request
					.getParameter(mainField);
			mainParam.put(mainField, value);
		}
		String sql = selectBean.getPopsql();
		int condWhere = 0;
		// 启用了分支机构时，且不是分支机构共享，加入分支机构限制（只能查当前登录分支机构及其下级的数据）
		//ArrayList tables = selectBean.getTables(); // 取popup的xml中<table>节点所有表名
		String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
		boolean openSunCompany = Boolean.parseBoolean(openValue);
		Hashtable allTables = (Hashtable) request.getSession()
				.getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunCompanyID = getLoginBean(request).getSunCmpClassCode();
		int onStart = 0;

		/*
		for (int i = 1; i < tables.size(); i++) { // 取popup的xml中<table>节点第2个及以后的表，在left
			// join中加入分支机构限制
			DBTableInfoBean selectTable = (DBTableInfoBean) allTables
					.get(tables.get(i).toString().trim());
			boolean isSharedTable = selectTable.getIsSunCmpShare() == 1 ? true
					: false;
			if (openSunCompany && !isSharedTable) {
				String sunCmpCondition = tables.get(i) + ".SCompanyID like '"
						+ sunCompanyID + "%' ";
				if (sql.indexOf(" on ", onStart) > -1) {
					sql = sql.substring(0, sql.indexOf(" on ", onStart))
							+ " on " + sunCmpCondition + " and "
							+ sql.substring(sql.indexOf(" on ", onStart) + 3);
				}
			}
			onStart = sql.indexOf(" on ", onStart) + 3;
		} 
		// 取popup的xml中<table>节点中的第1个表，将分支机构限制加入where子句中。
		DBTableInfoBean firstTable = (DBTableInfoBean) allTables.get(tables
				.get(0).toString().trim());
		boolean isSharedTable = firstTable.getIsSunCmpShare() == 1 ? true
				: false;
		if (openSunCompany && !isSharedTable) {
			String sunCmpCondition = tables.get(0) + ".SCompanyID like '"
					+ sunCompanyID + "%' ";
			condWhere = (sql + " where ").length();
			sql += " where " + sunCmpCondition;
		} */
		Hashtable enumeratemap = BaseEnv.enumerationMap;
		String condition = selectBean.getCondition();
		if (condition != null && condition.length() > 0) {
			if (condition.indexOf("@TABLENAME") >= 0) {
				condition = condition.replaceAll("@TABLENAME", tableName);
			}

			HashMap sysParamMap = null;
			HashMap sessParamMap = null;
			HashMap codeParamMap = null;
			try {
				sysParamMap = ConfigParse.getSystemParam(selectBean
						.getSysParams(), getLoginBean(request)
						.getSunCmpClassCode());
				sessParamMap = ConfigParse.getSessParam(getLoginBean(request)
						.getId(), selectBean.getSessParams());
				Connection conn = null;
				codeParamMap = ConfigParse.getCodeParam(selectBean
						.getCodeParams(), conn);
			} catch (Exception ex) {
				ex.printStackTrace();

			}
			HashMap queryParamMap = new HashMap();
			condition = ConfigParse.parseConditionSentencePutParam(condition,
					mainParam, sysParamMap, queryParamMap, sessParamMap,
					codeParamMap, null);
			condition = condition == null ? "" : condition.trim();
			if (condition.length() > 0) {
				if (condWhere > 0) {
					sql += "   and ";
				} else {
					sql += "  where ";
					condWhere = sql.length();
				}
				sql += condition;

			}
			sql = ConfigParse.parseConditionSentencePutParam(sql, mainParam,
					sysParamMap, queryParamMap, sessParamMap, codeParamMap,
					null);
		}

		String changeCond = selectBean.getChangeCond();
		if (changeCond != null && changeCond.length() > 0) {
			if (changeCond.indexOf("@TABLENAME") >= 0) {
				changeCond = changeCond.replaceAll("@TABLENAME", tableName);
			}

			HashMap sysParamMap = null;
			HashMap sessParamMap = null;
			HashMap codeParamMap = null;
			try {
				sysParamMap = ConfigParse.getSystemParam(selectBean
						.getSysParams(), getLoginBean(request)
						.getSunCmpClassCode());
				sessParamMap = ConfigParse.getSessParam(getLoginBean(request)
						.getId(), selectBean.getSessParams());
				Connection conn = null;
				codeParamMap = ConfigParse.getCodeParam(selectBean
						.getCodeParams(), conn);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			HashMap queryParamMap = new HashMap();
			changeCond = ConfigParse.parseConditionSentencePutParam(changeCond,
					mainParam, sysParamMap, queryParamMap, sessParamMap,
					codeParamMap, null);
			changeCond = changeCond == null ? "" : changeCond.trim();
			if (changeCond.length() > 0) {
				//if (condWhere > 0) {
					sql += "   and ";
				//} else {
				//	sql += "  where 1=1 and ";
				//	condWhere = sql.length();
				//}
				sql += changeCond;

			}
			sql = ConfigParse.parseConditionSentencePutParam(sql, mainParam,
					sysParamMap, queryParamMap, sessParamMap, codeParamMap,
					null);
		}
		
		BaseEnv.log.debug("UtilServlet.validateDetailSeq 序列号扫描sql=" + sql);

		Result rs = propMgt.queryGoodsBySeq(sql, selectBean, GlobalsTool.getLocale(
				request).toString());

		PrintWriter out = response.getWriter();
		
		String returnCols = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			String saveVal = ""; // 保存需显示的值
			String returnVal = ""; // 保存需返回的值
			if (((List) rs.retVal).size() > 1) {
				out.print("hasMore");
				return;
			}
			if (((List) rs.retVal).size() != 0) {
				Object[] os = (Object[]) ((List) rs.retVal).get(0);
				// 先找出需返回的值				
				for (int j = 0; j < selectBean.getReturnFields().size(); j++) {
					PopField fv = (PopField) selectBean.getReturnFields()
							.get(j);
					for (int k = 0; k < selectBean.getAllFields().size(); k++) {
						PopField fv2 = (PopField) selectBean.getAllFields()
								.get(k);
						if (fv.getFieldName().equals(fv2.getFieldName())) {
							
                        	if(fv.type==1){
                        		String fvn = fv.parentName;
                        		if(fvn == null || fvn.length() == 0){
	                        		fvn = fv.fieldName;	                        		
	                        	}
	                        	returnCols += fvn + ";";
                        	}else{
	                        	String fvn =  fv.asName;
	                        	if(fvn == null || fvn.length() == 0){
	                        		fvn = fv.fieldName;	                        		
	                        	}
	                        	returnCols += fvn + ";";
                        	}
							
							
							String osstr = os[k] + "";
							DBFieldInfoBean tempfib = GlobalsTool.getFieldBean(
									selectBean.getTableName(fv.fieldName),
									selectBean.getFieldName(fv.fieldName));
							if (tempfib == null && fv.display != null && fv.display.indexOf(".") >-1) {
								tempfib = GlobalsTool.getFieldBean(selectBean
										.getTableName(fv.display), selectBean
										.getFieldName(fv.display));
							}
							if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
								if (osstr == null || osstr.equals("null")
										|| osstr.length() == 0) {
									osstr = "0";
								}
								DBTableInfoBean tempTableInfo = (DBTableInfoBean) allTables
										.get(selectBean
												.getTableName(fv.fieldName));
								if (tempTableInfo == null && fv.display != null) {
									tempTableInfo = (DBTableInfoBean) allTables
											.get(selectBean
													.getTableName(fv.display));
								}
								boolean isMain = (tempTableInfo.getTableType() == 0 ? true
										: false);
								osstr = GlobalsTool.newFormatNumber(new Double(
										osstr), false, false, "true"
										.equals(BaseEnv.systemSet.get(
												"intswitch").getSetting()),
										selectBean.getTableName(fv.fieldName),
										selectBean.getFieldName(fv.fieldName),
										isMain);
							}
							returnVal = returnVal + osstr.trim() + "#;#";
							if (fv.compare) {
								saveVal = saveVal + osstr.trim() + "#;#";
							}
							break;
						}
					}
				}
			}
			if (((List) rs.retVal).size() != 0) {
				out.print(saveVal + "@@" + returnVal+"@@"+returnCols);
			}
		}
	}

	public static String getEnumerationItems(HttpServletRequest request,
			Hashtable enumMap, String enumeration, String value, String locale) {
		List list = new ArrayList();
		EnumerateBean beans[] = (EnumerateBean[]) enumMap.values().toArray(
				new EnumerateBean[0]);
		for (int i = 0; i < beans.length; i++) {
			if (beans[i].getEnumName().equals(enumeration)) {
				for (int j = 0; j < beans[i].getEnumItem().size(); j++) {
					EnumerateItemBean eib = (EnumerateItemBean) beans[i]
							.getEnumItem().get(j);
					if (eib.getEnumValue().equals(value)) {
						return ((KRLanguage) (eib.getDisplay())).get(locale);
					}
				}

				break;
			}
		}
		return "";
	}

	private void validatePwd(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String[] temp = (String[]) userMgt.getLoginPwd().getRetVal();
		boolean isInit = false;
		
		MessageDigest md;
		String md5Pwd="";
		try {
			md = MessageDigest.getInstance("MD5");
			md.update("admin".getBytes()) ;
			md5Pwd = toHex(md.digest()) ;
		} catch (NoSuchAlgorithmException e) {
			BaseEnv.log.error("UtilServlet.validatePwd Password MD5 Error :",e);
		}
		
		if (temp[1].equals(md5Pwd)) {
			isInit = true;
		}
		PrintWriter out = response.getWriter();

		out.print(isInit);
	}

	/**
	 * 判断该电话是否有公司信息
	 * 
	 * @param tel
	 * @return
	 * @throws IOException
	 */
	private void isExistComByTel(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String tel = request.getParameter("tel");// 获取来电/拨出电话号码
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute(
				"LoginBean");// 获取系登录bean
		String telPrefix = loginBean.getTelPrefix();// 获取电话前缀
		telPrefix = telPrefix == null ? "" : telPrefix;// 判断是否有电话前缀
		if (tel.indexOf(telPrefix) == 0) {// 如果某个电话的开头与前缀匹配，即拨出的电话带了前缀
			tel = tel.substring(telPrefix.length());// 去掉前缀
		}
		tel = tel.endsWith("#") ? tel.substring(0, tel.length() - 1) : tel;
		// tel= !telPrefix.equals("")?
		// tel.substring(telPrefix.length()-1):tel;//拨出号码有前缀，则去掉前缀
		if (tel.indexOf("0") == 0) {// (除去前缀后)以0开头，可能是手机加0，也可能是区位号的0,如"013588888888,01025476640,0752254565550"
			if (tel.substring(1).charAt(0) == '1') {// 去掉0之后的号码从1开始可能是异地手机或者北京的座机以010的区号开始的
				if (tel.substring(2).charAt(0) != '0') {// 去掉01后的号码不是从0开始的即为异地手机号码
					tel = tel.substring(1);
				}
			}
		} else {// 号不从0开的，即为本地座机或手机
			String telAreaCode = loginBean.getTelAreaCode();// 用户区位码
			if (tel.charAt(0) != '1') {// 号码的开头第一位不是1即为是本地座机，则加上本地区号
				if (tel.length() == 7 || tel.length() == 8) {// 本地号码是7为或8位的才加区号,否视为特殊号码,不加区号
					telAreaCode = telAreaCode == null ? "" : telAreaCode;
					tel = telAreaCode + tel;
				}
			}
		}
		Result rs = new CallMgt().queryComByTel(tel);
		if (rs.getRealTotal() > 0) {
			request.getSession().setAttribute("txtRemote", tel);
		}
		response.getWriter().print(rs.getRealTotal() > 0 ? true : false);// 如果要改条件可能会有问题，查询时只设置了total
	}



	public void getUserLocStyle(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userName = request.getParameter("name");
		userName = GlobalsTool.toChinseChar_GBK(userName);
		Result rs = GlobalMgt.getUserLocStyle(userName);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			if (rs.retVal != null) {
				String rstr = rs.retVal.toString();
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print(rstr);
			}
		}
	}

	public void getUserInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userName = request.getParameter("name");
		userName = GlobalsTool.toChinseChar(userName);
		Result rs = GlobalMgt.getUserInfo(userName);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			if (rs.retVal != null) {
				String rstr = rs.retVal.toString();
				String mac = rstr.substring(rstr.indexOf("<userStr1>")
						+ "<userStr1>".length(), rstr.indexOf("</userStr1>"));

				// 检查系统是否，启用mac地址过滤，如果启用，则替换mac地址
				String macfilter = (BaseEnv.systemSet.get("MACFilter")
						.getSetting());
				if ("true".equals(macfilter)) {
					// 查询mac地址表
					String ml = GlobalMgt.getMacList();
					if (ml != null && ml.length() > 0) {
						mac = ml;
						rstr = rstr.substring(0, rstr.indexOf("<userStr1>")
								+ "<userStr1>".length())
								+ mac
								+ rstr.substring(rstr.indexOf("</userStr1>"));
					}
				}

				if (mac.trim().length() > 0) {
					Random rd = new Random(System.currentTimeMillis());
					int rdmac = rd.nextInt(100000000);
					rstr += "<userStr2>" + rdmac + "</userStr2>";
					request.getSession().setAttribute("ClientMACKey",
							String.valueOf(rdmac));
					request.getSession().setAttribute("ClientMACValue", mac);
				}else{
					request.getSession().removeAttribute("ClientMACValue");
				}
				String uk = rstr.substring(rstr.indexOf("<userKeyId>")
						+ "<userKeyId>".length(), rstr.indexOf("</userKeyId>"));
				if (uk.trim().length() > 0) {
					// 需设置安全u遁检查
					Random rd = new Random(System.currentTimeMillis());
					int rdi = rd.nextInt(100000000);
					rstr += "<userKeyId2>" + rdi + "</userKeyId2>";
					request.getSession().setAttribute("ClientUSBKey", rdi + "");
				}else{
					request.getSession().removeAttribute("ClientUSBKey");
				}
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print(rstr);
			}
		}
	}

	public String getDefSQLMsg(HttpServletRequest request, String obj) {
		Object o = request.getSession().getServletContext().getAttribute(
				org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = (MessageResources) o;

		String msg = "";
		String[] os = (String[]) obj.split(",");
		if (os.length == 1) {
			msg = resources.getMessage(request.getLocale(), os[0]);
		} else if (os.length == 2) {
			msg = resources.getMessage(request.getLocale(), os[0], os[1]);
		} else if (os.length == 3) {
			msg = resources
					.getMessage(request.getLocale(), os[0], os[1], os[2]);
		} else if (os.length >= 4) {
			msg = resources.getMessage(request.getLocale(), os[0], os[1],
					os[2], os[3]);
		}
		return msg;
	}

	/**
	 * 验证序列号 1.输入的序列号不能相同 2.如果商品启用序列号并且连续，验证输入的序列号是否连续 3.如果入库，判断序列号是否已存在
	 * 4.如果出库，判断序列号是否不存在
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void validateSeq(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		Hashtable map = (Hashtable) request.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);
		String tableName = request.getParameter("tableName");
		String seqFName = request.getParameter("validateField");
		String seqStr = request.getParameter("seqStr");
		String goodsCode = request.getParameter("goodsCode");
		String defSeqStr = request.getParameter("defSeqStr");// 修改时的原始序列号字符串
		String oldSeq = request.getParameter("oldSeq");

		String[] oldList = oldSeq.split("~");
		DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(map, tableName,
				seqFName);
		String[] seqList = seqStr.split("~");// 待验证的序列号
		String res = "";

		Result rs1 = propMgt.queryGoodsSeqDigit(goodsCode);
		if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			int digit = Integer.parseInt(rs1.getRetVal().toString());
			if (digit > 0) {
				for (int i = 0; i < seqList.length; i++) {
					if (seqList[i].length() != digit) {
						res = this.getMessageResources(request,
								"seq.validate.error6_1")
								+ "\n\n"
								+ this.getMessageResources(request,
										"seq.validate.error9") + digit;
						break;
					}
				}
				if (res.length() > 0) {
					out.print(res);
					return;
				}
			}
		}

		String logic = fieldBean.getLogicValidate();
		if (!"".equals(logic) && logic.split(":")[1].equals("Instore")) {// 入库验证
			Result rs = propMgt.queryGoodsSeqSetN(goodsCode);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				String[] strs = (String[]) rs.getRetVal();
				int sIndex = Integer.parseInt(strs[1]);
				int eIndex = Integer.parseInt(strs[2]);
				if (strs[0].equals("0")) {// 商品序列号连续,验证序列号连续部分是否为数字
					for (int i = 0; i < seqList.length; i++) {
						String temp = seqList[i].substring(sIndex - 1, eIndex);
						try {
							Integer.parseInt(temp);
						} catch (Exception e) {
							out.print(this.getMessageResources(request,
									"seq.validate.error6_1")
									+ "\n\n"
									+ this.getMessageResources(request,
											"seq.validate.error3"));
							return;
						}
					}
				}
				if (defSeqStr != null && !"".equals(defSeqStr)) {// 修改存在原始值
					String[] iniSeqArr = defSeqStr.split("~");
					ArrayList newSeqArr = new ArrayList();
					for (int i = 0; i < seqList.length; i++) {
						boolean exist = false;
						for (int j = 0; j < iniSeqArr.length; j++) {
							if (iniSeqArr[j].equals(seqList[i])) {
								exist = true;
							}
						}
						if (!exist) {
							newSeqArr.add(seqList[i]);
						}
					}
					if (newSeqArr.size() > 0) {
						rs = propMgt.validateSeqIn(seqFName, newSeqArr);
						if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							ArrayList ret = (ArrayList) rs.getRetVal();
							if (ret.size() > 0) {
								res = this.getMessageResources(request,
										"seq.validate.error6_1")
										+ "\n\n"
										+ this.getMessageResources(request,
												"seq.validate.error6") + "\n";
								for (int i = 0; i < ret.size(); i++) {
									if (i == ret.size() - 1) {
										res += "[" + ret.get(i) + "]";
									} else {
										res += "[" + ret.get(i) + "]、";
									}
								}
								out.print(res);
								return;
							}
						}
					}
				} else {// 添加时或是修改没原始值
					// 首先查看序列号在库存中是否重复(如果是序列号盘点单，不需要查询库存)
					if("tblseqCheckDet".equals(tableName)){
						rs=new Result();
						rs.setRetVal(new ArrayList());
					}else{
						rs = propMgt.queryGoodsSeqInStockdet(seqFName, seqList);
					}
					if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						ArrayList alertList = (ArrayList) rs.getRetVal();// 出入库明细中存在的所有序列号
						ArrayList lastList = new ArrayList();
						for (int i = 0; i < alertList.size(); i++) {
							boolean exist = false;
							for (int j = 0; j < oldList.length; j++) {
								if (oldList[j].equals(alertList.get(i))) {
									exist = true;
								}
							}
							if (!exist) {
								lastList.add(alertList.get(i));
							}
						}
						if (lastList.size() > 0) {
							res = this.getMessageResources(request,
									"seq.validate.error6_1")
									+ "\n\n"
									+ this.getMessageResources(request,
											"seq.validate.error6") + "\n";
							for (int i = 0; i < lastList.size(); i++) {
								if (i == lastList.size() - 1) {
									res += "[" + lastList.get(i) + "]";
								} else {
									res += "[" + lastList.get(i) + "]、";
								}
							}
							out.print(res);
							return;
						}
					}
					// 验证序列号连续的商品的序列号是否连续
					if (strs[0].equals("0")) {
						for (int i = 0; i < seqList.length - 1; i++) {
							if (Integer.parseInt(seqList[i].substring(
									sIndex - 1, eIndex)) + 1 != Integer
									.parseInt(seqList[i + 1].substring(
											sIndex - 1, eIndex))) {
								out.print(this.getMessageResources(request,
										"seq.validate.error6_1")
										+ "\n\n"
										+ this.getMessageResources(request,
												"seq.validate.error8"));
								return;
							}
						}
						rs = propMgt.queryLastContiPartOfSeq(seqFName,
								goodsCode, sIndex, eIndex);
						if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							String lastContiSeq = rs.getRetVal().toString();
							if (!lastContiSeq.equals("")) {
								try {
									Integer.parseInt(lastContiSeq);
								} catch (Exception e) {
									out.print(this.getMessageResources(request,
											"seq.validate.error6_1")
											+ "\n\n"
											+ this.getMessageResources(request,
													"seq.validate.error11"));
									return;
								}
								if (Integer.parseInt(seqList[0].substring(
										sIndex - 1, eIndex)) != Integer
										.parseInt(lastContiSeq) + 1) {
									out.print(this.getMessageResources(request,
											"seq.validate.error6_1")
											+ "\n\n"
											+ this.getMessageResources(request,
													"seq.validate.error2")
											+ lastContiSeq);
									return;
								}
							}
						}
					}
				}
			}
		} else if (!"".equals(logic) && logic.split(":")[1].equals("Outstore")) {// 出库验证
			Result rs = propMgt.queryGoodsSeqInStockdetByGoodsCode(seqFName,
					goodsCode);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList existSeqList = (ArrayList) rs.getRetVal();
				ArrayList alertList = new ArrayList();
				for (int i = 0; i < seqList.length; i++) {
					if (!existSeqList.contains(seqList[i])) {
						alertList.add(seqList[i]);
					}
				}
				if (alertList.size() > 0) {
					res = this.getMessageResources(request,
							"seq.validate.error6_1")
							+ "\n\n"
							+ this.getMessageResources(request,
									"seq.validate.error7")
							+ "\n"
							+ this.getMessageResources(request,
									"seq.validate.error7_2") + "\n";
					for (int i = 0; i < alertList.size(); i++) {
						if (i == alertList.size() - 1) {
							res += "[" + alertList.get(i) + "]";
						} else {
							res += "[" + alertList.get(i) + "]、";
						}
					}
					out.print(res);
					return;
				}
			}
		}
	}

	/**
	 * 验证商品是否启用了序列号
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void validateGoodsSeq(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		Hashtable map = (Hashtable) request.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);
		HashMap values = new HashMap();
		DynDBManager dbmgt = new DynDBManager();
		String validateField = request.getParameter("validateField");
		DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(map,
				validateField.substring(0, validateField.indexOf("_")),
				validateField.substring(validateField.indexOf("_") + 1));
		String[] validates = fieldBean.getLogicValidate().split(":");
		values.put(validateField, request.getParameter(validateField));
		for (int i = 4; i < validates.length; i++) {
			values.put(validates[i], request.getParameter(validates[i]));
		}
		Object ob = request.getSession().getServletContext().getAttribute(
				org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		Result rs = dbmgt.defineSql(validates[3], values, this.getLoginBean(
				request).getId(), resources, GlobalsTool.getLocale(request));
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS
				&& rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			out.print("true");
		} else {
			out.print("false");
		}

	}

	/**
	 * 业务逻辑验证
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public void validateField(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String msgStr = "";
		Hashtable map = (Hashtable) request.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);
		HashMap values = new HashMap();
		DynDBManager dbmgt = new DynDBManager();
		String validateField = request.getParameter("validateField");
		DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(map,
				validateField.substring(0, validateField.indexOf("_")),
				validateField.substring(validateField.indexOf("_") + 1));
		String[] validates = fieldBean.getLogicValidate().split(":");
		values.put(validateField, request.getParameter(validateField));
		for (int i = 1; i < validates.length; i++) {
			values.put(validates[i], request.getParameter(validates[i]));
		}
		Object ob = request.getSession().getServletContext().getAttribute(
				org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		Result rs = dbmgt.defineSql(validates[0], values, this.getLoginBean(
				request).getId(), resources, GlobalsTool.getLocale(request));
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS
				&& rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			// 自定义sql语句定制返回结果
			String[] str = (String[]) rs.getRetVal();

			if (str != null) {
				msgStr = getDefSQLMsg(request, str[0]);
				out.print(msgStr);
			}
		}

	}

	/**
	 * 玫诒只械诔
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public void currStyle(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String msgStr = "";
		String userName = request.getParameter("name");
		userName = GlobalsTool.toChinseChar_GBK(userName);
		Result rs = GlobalMgt.getCurrStyle(userName);
		String style = rs.retVal == null ? "" : rs.retVal.toString();
		List list = GlobalsTool.getEnumerationItems("style", GlobalsTool.getLocale(
				request).toString());
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && style.length() > 0) {
			msgStr += "<select name=\"style\">";
			for (int i = 0; i < list.size(); i++) {
				KeyPair kp = (KeyPair) list.get(i);
				if (kp.getValue().equals(style)) {
					msgStr += "<option value=\"" + kp.getValue()
							+ "\" selected >" + kp.getName() + "</option>";
				} else {
					msgStr += "<option value=\"" + kp.getValue() + "\" >"
							+ kp.getName() + "</option>";
				}
			}
			msgStr += "</select>";
		}
		out.print(msgStr);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void currLoc(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String msgStr = "";
		String userName = request.getParameter("name");
		userName = GlobalsTool.toChinseChar_GBK(userName);
		Result rs = GlobalMgt.getCurrLoc(userName);
		String style = rs.retVal == null ? "" : rs.retVal.toString();
		Hashtable locTable = (Hashtable) request.getSession()
				.getServletContext().getAttribute("LocaleTable");

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && style.length() > 0) {
			msgStr += "<select name=\"loc\" onChange =\"changeLoc();\">";
			Iterator iter = locTable.keySet().iterator();
			while (iter.hasNext()) {
				String locName = (String) iter.next();
				Locale locale = (Locale) locTable.get(locName);
				if (locName.equals(style)) {
					request.getSession().setAttribute(
							org.apache.struts.Globals.LOCALE_KEY, locale);
					msgStr += "<option value=\"" + locName + "\" selected >"
							+ locale.getDisplayName(locale) + "</option>";
				} else {
					msgStr += "<option value=\"" + locName + "\" >"
							+ locale.getDisplayName(locale) + "</option>";
				}
			}
			msgStr += "</select>";
		}
		out.print(msgStr);
	}

	
	public String folderTree(ArrayList<String[]> list,String folderName,String classCode,LoginBean loginBean,String aTalkPic,ArrayList<OnlineUser> allUser){
		String folderTree="";
		/*if (loginBean.getDepartCode().startsWith(classCode)){
			folderTree="<li "+ aTalkPic+"><span class=\"org-s\"  onclick=\"openDialog('dept','"
							+ classCode
							+ "','"
							+ folderName
							+ "');\">" + folderName + "</span>" +
							" <img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+classCode+"','2');\" src=\"/style/images/body/aiochat.png \"/> "+		
							"<ul>";
			
		} else {*/
			folderTree="<li><span>" + folderName + "</span><ul>";
		//}
		for (OnlineUser onlineUser : allUser) {
			String phUrl = GlobalsTool.checkEmployeePhoto("48",onlineUser.getId());
			String imgPhoto = "<img src=\""+phUrl+"\" />";
			
			if (onlineUser.deptId.equals(classCode)) {
				String click = "onClick=\"openDialog('person','"
						+ onlineUser.getId() + "','"
						+ onlineUser.name + "')\"" ;
			
				String deptAtalk=" <img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+onlineUser.getId()+"','1');\" src=\"/style/images/body/aiochat.png \"/> ";
				String mobile = "<img class=\"kk-img kk-mobile\"  src=\"/style/images/mobile.gif\"/>";
				/*if (loginBean.getId().equals(onlineUser.getId())) {
					click = "";
					deptAtalk="";
				}*/
				if (!"mobile".equals(onlineUser.getType())) {
					mobile = "";
				}
				if (onlineUser.isOnline()) {
					folderTree+="<li "+aTalkPic+"><span class=\"i-name\" " + click
							+ ">"
							+ imgPhoto+"<i>"+onlineUser.name 
							+ "</i></span> "+deptAtalk+ mobile+
							" </li>";
				} else {
					folderTree+="<li "+aTalkPic+"><span class=\"i-name\" " + click 
							+ ">"
							+ imgPhoto+"<i>"+onlineUser.name + "</i></span> "+deptAtalk+
							"</li>";
				}
			}
		}
		
   		for(String[] group : list){
   			if(group[0].substring(0,group[0].length()-5).equals(classCode)){
   				folderTree+=this.folderTree(list,group[1], group[0],loginBean,aTalkPic,allUser);
   			}
   		}
   		folderTree+="</ul></li>";
       	return folderTree;
    }
	
	/**
	 * 查询在线人员
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public void showEmp(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html; charset=utf-8");
		String tabType = request.getParameter("tabType"); // tab类型
		String keyWord = request.getParameter("keyWord");
		if (keyWord != null) {
			keyWord = keyWord.toLowerCase();
			keyWord = GlobalsTool.toChinseChar(keyWord);
		}
		
		ArrayList[] userList = OnlineUserInfo.getOnlineOfflineUser("");
		ArrayList<OnlineUser> allUser = new ArrayList<OnlineUser>();
		allUser.addAll(userList[0]);
		allUser.addAll(userList[1]);
		request.setAttribute("totleNum", (userList[0].size() + userList[1].size() ));// 总人数量
		request.setAttribute("onLineNum", userList[0].size());// 在线人数量 mj在线人数
		LoginBean loginBean = getLoginBean(request);

		//Object o = request.getSession().getAttribute("LoginBean");
		
		String aTalkPic="onmouseout=\"hideATalk(this);\" onmouseover=\"showATalk(this);\"";
		
		/*for (int i = 0; i < userList[0].size(); i++) {
			OnlineUser online = (OnlineUser) userList[0].get(i);
			LoginBean bean = null;
			if (o != null) {
	            bean = (LoginBean) o;
	             if(bean.getName().equals(online.getSysName())){
	            	userList[0].remove(online);
	            }
			}
		}*/
		// 这里只查1、在线人员，2、所有部门，3、职员分组,4、搜索用户
		if (keyWord != null && keyWord.length() > 0) {
			PrintWriter out = response.getWriter();
			StringBuilder searchStr = new StringBuilder();
			for (OnlineUser user : allUser) {
				System.out.println(user.pingying);
				String phUrl = GlobalsTool.checkEmployeePhoto("48",user.getId());
				String imgPhoto = "<img src=\""+phUrl+"\" />";
				if (user.getName().contains(keyWord)
						|| user.pingying.contains(keyWord)) {
					String click = "onClick=\"openDialog('person','"
							+ user.getId() + "','" + user.getName() + "')\"";
					String mobile = "<img class=\"kk-img kk-mobile\" src=\"/style/images/mobile.gif\"/>";
					/*if (loginBean.getId().equals(user.getId())) {
						click = "";
					}*/
					if (!"mobile".equals(user.getType())) {
						mobile = "";
					}
					if (user.isOnline()) {
						searchStr.append("<div "+aTalkPic+"><span class=\"i-name\"  " + click
								+ ">" + imgPhoto+"<i>"+user.getName()
								+ mobile + "</i></span>" +
								"<img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+user.getId()+"','1');\" src=\"/style/images/body/aiochat.png \"/> "+
								"</div>");
					} else {
						searchStr.append("<div "+aTalkPic+"><span class=\"i-name\" " + click 
								+ ">" +imgPhoto+"<i>"+ user.getName()
								+ "</i></span> "+
								"<img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+user.getId()+"','1');\" src=\"/style/images/body/aiochat.png \"/> "+
								"</div>");
					}
				}
			}
			out.print(searchStr.toString());
			out.flush();
			return;
		} else if ("deptTab".equals(tabType)) {
			// 获取所有的部门信息
			PrintWriter out = response.getWriter();
			request.setAttribute("deparList", OnlineUserInfo.getAllDept());
			StringBuilder deptList = new StringBuilder();
			Result rs = new UserMgt().queryDept2();
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList<String[]> listDept = (ArrayList<String[]>) rs.retVal;
				for (String[] group : listDept) {
					if(group[0].length()==5){
						deptList.append(this.folderTree(listDept, group[1], group[0],loginBean,aTalkPic,allUser));
					}
				}
				
			}
			out.print(deptList.toString());
			out.flush();
			return;
		} else if ("groupTab".equals(tabType)) {
			PrintWriter out = response.getWriter();
			// 职员分组，需取职员分组信息
			Result rs = new MsgMgt().getMsgGroup(loginBean.getId());
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				StringBuilder groupList = new StringBuilder();
				HashMap<String, String> gmap = new HashMap<String, String>();
				for (Object[] os : (ArrayList<Object[]>) rs.retVal) {
					gmap.put((String) os[3], (String) os[2]);
				}
				for (String group : gmap.keySet()) {
					groupList
							.append("<li "+ aTalkPic+" id=\"userId\"><span   onclick=\"openDialog('group','"
									+ group
									+ "','"
									+ gmap.get(group)
									+ "')\">"
									+ gmap.get(group) + "</span>" +
									" <img style=\"cursor:pointer;vertical-align: middle;margin-left:5px;display: none;\" onClick=\"openATalk('"+loginBean.getId()+"','"+group+"','3');\" src=\"/style/images/body/aiochat.png \"/> "+
									"<ul>");
					for (Object[] os : (ArrayList<Object[]>) rs.retVal) {
						if (group.equals(os[3])) {
							OnlineUser onlineUser = OnlineUserInfo
									.getUser((String) os[0]);
							String phUrl = GlobalsTool.checkEmployeePhoto("48",onlineUser.getId());
							String imgPhoto = "<img src=\""+phUrl+"\" />";
							
							String click = "onClick=\"openDialog('person','"
									+ os[0] + "','" + onlineUser.name + "')\"";
							String groupAtalk=" <img style=\"cursor:pointer;vertical-align: middle;margin-left:5px;display: none;\" onClick=\"openATalk('"+loginBean.getId()+"','"+onlineUser.getId()+"','1');\" src=\"/style/images/body/aiochat.png \"/> ";
							/*if (loginBean.getId().equals(os[0])) {
								click = "";
								groupAtalk="";
							}*/
							if (onlineUser != null) {
								if (onlineUser.isOnline()) {
									groupList
											.append("<li "+aTalkPic+"><span class=\"i-name\" " + click
													+ ">"
													+ imgPhoto+"<i>"+onlineUser.name
													+ "</i></span> "+groupAtalk+
													"</li>");
								} else {
									groupList.append("<li "+aTalkPic+"><span class=\"i-name\" " + click
											+ ">"
											+ imgPhoto+"<i>"+onlineUser.name + "</i></span> "+groupAtalk+
											"</li>");
								}
							}
						}
					}
					groupList.append("</ul></li>");
				}
				/*MOperation mop = (MOperation) loginBean.getOperationMap().get(
						"/UserFunctionQueryAction.do?tableName=tblEmpGroup");
				if (mop != null && mop.query) {
					groupList
							.append("<li id=\"userId\"><span><a href='javascript:mdiwin(\"/UserFunctionQueryAction.do?tableName=tblEmpGroup\",\"职员分组\");'>创建职员分组</a></span></li>");
				}*/
				//添加自己所在的部门
				Result deptRs = new UserMgt().queryDept2();
				if (deptRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					ArrayList<String[]> listDept = (ArrayList<String[]>) deptRs.retVal;
					for (String[] group : listDept) {
						if(loginBean.getDepartCode().startsWith(group[0])){
							String deptTree="<li "+ aTalkPic+"><span class=\"org-s\"  onclick=\"openDialog('dept','"
							+ group[0]
							+ "','"
							+ group[1]
							+ "');\">" + group[1] + "</span>" +
							" <img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+group[1]+"','2');\" src=\"/style/images/body/aiochat.png \"/> "+		
							"<ul>";
							String deptTrees="";
							for (OnlineUser onlineUser : allUser) {
								String phUrl = GlobalsTool.checkEmployeePhoto("48",onlineUser.getId());
								String imgPhoto = "<img src=\""+phUrl+"\" />";						
								if (onlineUser.deptId.indexOf(group[0]) !=-1) {
									String click = "onClick=\"openDialog('person','"
											+ onlineUser.getId() + "','"
											+ onlineUser.name + "')\"" ;								
									String deptAtalk=" <img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+onlineUser.getId()+"','1');\" src=\"/style/images/body/aiochat.png \"/> ";																																		
									deptTrees +="<li "+aTalkPic+"><span class=\"i-name\" " + click 
											+ ">"
											+ imgPhoto+"<i>"+onlineUser.name + "</i></span> "+deptAtalk+
											"</li>";
									
								}
							}
							deptTrees += "</ul>";
							groupList.append(deptTree+deptTrees);
						}
					}
					
				}								
				
				out.print(groupList.toString());
				out.flush();
				return;
			}
		} else if ("onlineTab".equals(tabType)) {
			// 显示在线人员
			PrintWriter out = response.getWriter();
			StringBuilder onlineList = new StringBuilder();
			for (int i = 0; i < userList[0].size(); i++) {
				
				OnlineUser online = (OnlineUser) userList[0].get(i);
				String phUrl = GlobalsTool.checkEmployeePhoto("48",online.getId());
				String imgPhoto = "<img class=\"kk-img\" src=\""+phUrl+"\" />";
				
				String click = "onClick=\"openDialog('person','"
						+ online.getId() + "','" + online.name + "')\"";
				String mobile = "<img class=\"kk-img kk-mobile\" src=\"/style/images/mobile.gif\"/>";
				/*if (loginBean.getId().equals(online.getId())) {
					click = "";
				}*/
				if (!"mobile".equals(online.getType())) {
					mobile = "";
				}
				onlineList.append("<div "+aTalkPic+"><span  class=\"i-name\" " + click + " >"
						+imgPhoto+"<i>"+ online.name + "</i></span>"+ " <img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+online.getId()+"','1');\" src=\"/style/images/body/aiochat.png \"/> " 
						+ mobile + "</div>");
			}
			out.print(onlineList.toString());
			out.flush();
			return;
		} else if ("historyTab".equals(tabType)) {
			Result result = new MessageMgt().historyEmp(loginBean.getId());
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				PrintWriter out = response.getWriter();
				StringBuilder onlineList = new StringBuilder();
				List<String> listEmp = (List<String>) result.retVal;
				for (String userId : listEmp) {
					OnlineUser online = OnlineUserInfo.getUser(userId);
					String phUrl = GlobalsTool.checkEmployeePhoto("48",online.getId());
					String imgPhoto = "<img class=\"kk-img\" src=\""+phUrl+"\" />";
					
					if (online == null)
						continue;
					String click = "onClick=\"openDialog('person','"
							+ online.getId() + "','" + online.name + "')\"";
					String mobile = "<img class=\"kk-img kk-mobile\" src=\"/style/images/mobile.gif\"/>";
//					if (loginBean.getId().equals(online.getId())) {
//						click = "";
//					}
					if (!"mobile".equals(online.getType())) {
						mobile = "";
					}
					if (online.isOnline()) {
						onlineList.append("<div "+aTalkPic+" ><span class=\"i-name\" " + click
								+ ">" + imgPhoto+ "<i>"+online.name
								+ "</i></span>" +"<img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+online.getId()+"','1');\" src=\"/style/images/body/aiochat.png \"/>"
								+ mobile + "</div>");
					} else {
						onlineList.append("<div "+aTalkPic+" ><span class=\"i-name\" " + click
								+ ">" + imgPhoto+"<i>"+online.name
								+ "</i></span>" +"<img class=\"kk-img\" onClick=\"openATalk('"+loginBean.getId()+"','"+online.getId()+"','1');\" src=\"/style/images/body/aiochat.png \"/> "
								+ mobile + "</div>");
					}
				}
				out.print(onlineList.toString());
			}
			return;
		}
		
		request.setAttribute("onlineUsers", userList[0]);
		request.getRequestDispatcher("/vm/moduleFlow/empList.jsp").forward(
				request, response);
	}
	
	
	/**
	 * 返回，告警，消息，通知的数量
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public void msgData(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		if (this.getLoginBean(request) == null) {
			out.print("close");
			return;
		}
		// 预警
		// 如果不是默认分支机构管理员(id为1),则只能看到自己的预警信息
		LoginBean loginBean = getLoginBean(request);
		request.setAttribute("uid", loginBean.getId());
		int alertNum =  0 ;
		out.print(alertNum + "||");
		// 通知
		AdviceMgt adviceMgt = new AdviceMgt();
		Result rs = adviceMgt.getCurrLoginMsgCount(this.getLoginBean(request)
				.getId());
		int noteNum = rs.retVal == null ? 0 : rs.retVal == null ? 0
				: ((Long) (((ArrayList) rs.retVal).get(0))).intValue();
		out.print(noteNum + "||");

		// 消息
		MessageMgt msgMgt = new MessageMgt();
		MSGSession ms = (MSGSession)MSGConnectCenter.sessionPool.get(this.getLoginBean(request).getId());
		StringBuilder msgList=null;
		
		if(ms==null){ //如果当前用户客户端不在线，则在网页端显示未读消息，客户端在线的话，网页端不需要显示未读信息
			rs = msgMgt.getCurrLoginMsgCount(this.getLoginBean(request));
			msgList = (StringBuilder) rs.retVal;
		}
		int oamsgNum = 0;
		String msgListStr="";
		if (msgList != null && msgList.toString().length() > 0) {
			oamsgNum = msgList.toString().split("</a>").length;
			msgListStr=msgList.toString();
		}
		out.print(oamsgNum + "||" + msgListStr + "||");
		if (alertNum > 0 || noteNum > 0 || oamsgNum > 0) {
			if (loginBean.getPromptSound()) {
				String msgStr = "";
				msgStr += "<div><embed src=\"/js/msg.wav\" loop=\"0\" autostart=\"true\" hidden=\"true\"></embed></div>";
				out.print(msgStr);
			}
		}
		
	}

	/**
	 * 最新7条消息
	 * 
	 * @param request
	 *            ttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public void adviceData(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		Result result = new AdviceMgt().getAdvices(getLoginBean(request)
				.getId());
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& result.retVal != null) {
			Result rs = new AdviceMgt().getCurrLoginMsgCount(this.getLoginBean(
					request).getId());
			String adviceNum = rs.retVal == null ? "0" : (String
					.valueOf(((ArrayList) rs.retVal).get(0)));
			List<AdviceBean> adviceList = (List<AdviceBean>) result.retVal;
			for (int i = 0; i < adviceList.size(); i++) {
				AdviceBean bean = adviceList.get(i);
				bean.setTitle(replace(request, bean.getTitle()));
				bean.setContent(replace(request, bean.getContent()));
				if (bean.getStatus().equals("noRead")) {
//					if (bean.getContent().indexOf("mdiwin") > 0) {
//						bean.setContent(bean.getContent().substring(0,
//								bean.getContent().indexOf("mdiwin"))
//								+ "read('"
//								+ bean.getId()
//								+ "');"
//								+ bean.getContent().substring(
//										bean.getContent().indexOf("mdiwin")));
//					}
					if (bean.getContent().toLowerCase().indexOf("<a ") > -1) {
						bean.setContent(bean.getContent().substring(0,
								bean.getContent().toLowerCase().indexOf("<a ")+3)
								+ "onclick=\" read('"
								+ bean.getId()
								+ "'); \""
								+ bean.getContent().substring(
										bean.getContent().toLowerCase().indexOf("<a ")+3));
					}
				}
				// 返回未读的消息 用type来保存
				bean.setType(adviceNum);
			}
			writeJson(adviceList, response);
		}
	}

	private String replace(HttpServletRequest request, String str) {
		while (str.indexOf("RES<") > -1) {
			int pos = str.indexOf("RES<");
			String temp = str.substring(pos + 4, str.indexOf(">", pos));
			temp = this.getMessageResources(request, temp);
			str = str.substring(0, pos) + temp
					+ str.substring(str.indexOf(">", pos) + 1);
		}
		return str;
	}

	/**
	 * @param obj
	 * @param response
	 * @throws IOException
	 */
	private void writeJson(Object obj, HttpServletResponse response) {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = gson.toJson(obj);
		out.println(json);
		out.flush();
		out.close();
	}

	/**
	 * 玫诒只械诔
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	// public void alertData(HttpServletRequest request,
	// HttpServletResponse response) throws IOException {
	// response.setContentType("text/html; charset=utf-8");
	// PrintWriter out = response.getWriter();
	// if (this.getLoginBean(request) == null) {
	// out.print("close");
	// return;
	// }
	// // 如果不是默认分支机构管理员(id为1),则只能看到自己的预警信息
	// LoginBean loginBean = getLoginBean(request);
	// request.setAttribute("uid", loginBean.getId());
	// Object ob = request.getSession().getServletContext().getAttribute(
	// org.apache.struts.Globals.MESSAGES_KEY);
	// MessageResources resources = null;
	// if (ob instanceof MessageResources) {
	// resources = (MessageResources) ob;
	// }
	// String msgStr = "";
	// String alertPopup = BaseEnv.systemSet.get("alertPopup").getSetting();
	// ArrayList msg = AlertCenterMgt.getAlertByUser(loginBean.getId());
	// int alertNum = msg == null?0:msg.size();
	// out.print(alertNum + "@koron@");
	// if ("true".equals(alertPopup)&&msg!=null) {
	// for (int i = 0; i < msg.size() && i<5; i++) {
	// Object[] obj = (Object[]) msg.get(i);
	// if (obj[0].toString().length() > 0
	// && obj[0].toString().equals(
	// getLoginBean(request).getSunCmpClassCode())) {
	// obj[2] = ConfigParse.parseSentencePutParam(obj[2]
	// .toString(), null, null, null, null, null,null,
	// resources, this.getLocale(request));
	// msgStr += "<li style=\"list-style:none; text-indent:18px;
	// text-decoration: none; background:url(/"
	// + this.getLoginBean(request).getDefStyle()
	// + "/images/bottom/info_ico.gif) no-repeat left 4px\"
	// onmouseover=\"this.style.cursor = 'hand'\">"
	// + obj[2] + "</li>";
	// }
	// }
	// if (loginBean.getPromptSound() && msgStr.length() > 0) {
	// msgStr += "<embed src=\"/js/msg.wav\" loop=\"0\" autostart=\"true\"
	// hidden=\"true\"></embed>";
	// }
	//
	// if (msgStr.length() == 0) {
	// msgStr = this.getMessageResources(request, "message.msg.noMsg");
	// msgStr = "<li>" + msgStr + "</li>";
	// }
	// }
	// out.print(msgStr);
	// }
	/**
	 * 玫诒只械诔
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public void currencyRate(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String currencyId = request.getParameter("currency");
		GlobalMgt mgt = new GlobalMgt();

		out.print(mgt.getCurrencyRate(currencyId, this.getLoginBean(request)
				.getSunCmpClassCode()));
	}

	private LoginBean getLoginBean(HttpServletRequest request) {
		Object o = request.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}

	// 玫前陆没募时息
	// public void oaMsg(HttpServletRequest request, HttpServletResponse
	// response)
	// throws IOException {
	//
	// MessageMgt msgMgt = new MessageMgt();
	// response.setContentType("text/html; charset=utf-8");
	// PrintWriter out = response.getWriter();
	// if (this.getLoginBean(request) == null) {
	// out.print("close");
	// return;
	// }
	// Result rs = msgMgt.getCurrLoginMsg(this.getLoginBean(request).getId());
	// String oamsg = "";
	// oamsg = rs.getRealTotal() + "@koron@";
	// String sysMsg = BaseEnv.systemSet.get("sysMsg").getSetting();
	// if ("true".equals(sysMsg)) {
	// if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	// if (rs.getRealTotal() != 0) {
	// List list = (List) rs.getRetVal();
	// int num = list.size() > 5 ? 5 : list.size();
	// for (int i = 0; i < num; i++) {
	// Object[] obj = (Object[]) list.get(i);
	// String str = obj[1] == null ? "" : (String) obj[1];
	// while (str.indexOf("RES<") > -1) {
	// String temp = str.substring(
	// str.indexOf("RES<") + 4, str.indexOf(">"));
	// temp = getMessageResources(request, temp);
	// str = str.substring(0, str.indexOf("RES<")) + temp
	// + str.substring(str.indexOf(">") + 1);
	// }
	// if(str.contains("mdiwin(")){
	// oamsg += "<li style=\"list-style:none; text-indent:18px; text-decoration:
	// none; background:url(/"
	// + this.getLoginBean(request).getDefStyle()
	// + "/images/bottom/inMessage.gif) no-repeat left 4px\"
	// onmouseover=\"this.style.cursor = 'hand' \">"
	// + str
	// + "</li>" ;
	// }else{
	// oamsg += "<li style=\"list-style:none; text-indent:18px; text-decoration:
	// none; background:url(/"
	// + this.getLoginBean(request).getDefStyle()
	// + "/images/bottom/inMessage.gif) no-repeat left 4px\"
	// onmouseover=\"this.style.cursor = 'hand'\"
	// onclick=\"mdiwin('/MessageAction.do?operation="
	// + OperationConst.OP_DETAIL
	// + "&keyId="
	// + obj[0]
	// + "','"
	// + getMessageResources(request, "oa.mydesk.news")
	// + "')\">" + str + "</li>";
	// }
	// }
	// if (getLoginBean(request).getPromptSound()) {
	// oamsg += "<embed src=\"/js/msg.wav\" loop=\"0\" autostart=\"true\"
	// hidden=\"true\"></embed>";
	// }
	// } else {
	// oamsg += "<span style=\"float:left;\"><img src=\"/"
	// + this.getLoginBean(request).getDefStyle()
	// + "/images/bottom/Message.gif\"></span><li>"
	// + this.getMessageResources(request,
	// "message.msg.noMsg") + "</li>";
	// }
	// } else {
	// oamsg += "<span style=\"float:left;\"><img src=\"/"
	// + this.getLoginBean(request).getDefStyle()
	// + "/images/bottom/Message.gif\"></span><li>"
	// + this.getMessageResources(request, "common.msg.error")
	// + "</li>";
	// }
	// }
	// out.print(oamsg);
	// }

	// public void myAdvice(HttpServletRequest request,
	// HttpServletResponse response) throws IOException {
	//
	// AdviceMgt adviceMgt = new AdviceMgt();
	// response.setContentType("text/html; charset=utf-8");
	// PrintWriter out = response.getWriter();
	// if (this.getLoginBean(request) == null) {
	// out.print("close");
	// return;
	// }
	// Result rs = adviceMgt.getCurrLoginMsg(this.getLoginBean(request)
	// .getId());
	// String advice = "";
	// advice = rs.getRealTotal() + "@koron@";
	// String sysAdvice = BaseEnv.systemSet.get("sysAdvince").getSetting();
	// if ("true".equals(sysAdvice)) {
	// if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	// if (rs.getRealTotal() != 0) {
	// List list = (List) rs.getRetVal();
	// int num = list.size() > 5 ? 5 : list.size();
	// for (int i = 0; i < num; i++) {
	// Object[] obj = (Object[]) list.get(i);
	// String url = "";
	//						
	// String str = obj[1] == null ? "" : (String) obj[1];
	// while (str.indexOf("RES<") > -1) {
	// int pos = str.indexOf("RES<") ;
	// String temp = str.substring(pos+ 4, str.indexOf(">",pos));
	// temp = getMessageResources(request, temp);
	// str = str.substring(0, pos) + temp
	// + str.substring(str.indexOf(">",pos) + 1);
	// }
	// if (obj[3].toString().indexOf("mdiwin('") >= 0) {
	// url = obj[3].toString();
	// while (url.indexOf("RES<") > -1) {
	// int pos = url.indexOf("RES<");
	// String temp = url.substring(pos + 4
	// , url.indexOf(">",pos));
	// temp = getMessageResources(request, temp);
	// url = url.substring(0, pos) + temp
	// + url.substring(url.indexOf(">",pos) + 1);
	// }
	//							
	// String url2=url;
	// url = url.substring(url.indexOf("mdiwin("), url
	// .indexOf("mdiwin(")
	// + "mdiwin(".length())
	// + "decodeURIComponent("
	// + url.substring(url.indexOf("mdiwin(")
	// + "mdiwin(".length(), url
	// .indexOf("',") + 1)
	// + ")"
	// + url.substring(url.indexOf("',") + 1, url
	// .indexOf("')") + 1)
	// + ",'"
	// + obj[0]
	// + "','advice')";
	// //如果用户可以看到连接，那么设置用户可以看到连接页面的权限,仅限于OA的工作流。因为OA的工作流没有建模块，所以拿工作流模板的ID作为模块ID，而其他的模块无法确定模块ID
	// url2=url2.substring(url2.indexOf("mdiwin('")
	// + "mdiwin('".length(), url2.indexOf("',") );
	// String
	// moduleUrl=url2.indexOf("&")>=0?url2.substring(0,url2.indexOf("&")):url2;
	// moduleUrl=moduleUrl.replace("UserFunctionAction",
	// "UserFunctionQueryAction");
	// if(this.getLoginBean(request).getOperationMap().get(moduleUrl)==null&&url2.indexOf("designId")>=0){
	// MOperation mop=new MOperation();
	// mop.moduleUrl=moduleUrl;
	// mop.moduleId=url2.substring(url2.indexOf("designId=")+"designId=".length(),url2.indexOf("&",url2.indexOf("designId=")));
	// this.getLoginBean(request).getOperationMap().put(mop.moduleUrl, mop);
	// this.getLoginBean(request).getOperationMapKeyId().put(mop.moduleId, mop);
	// BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
	// BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
	// mop.update=true;
	// mop.colconfig=true;
	// mop.print=true;
	// mop.query=true;
	// mop.sendEmail=true;
	// mop.oaWorkFlow=true;
	// }
	// }else if(obj[3].toString().indexOf("EMailAction.do")>=0){//邮件的特殊处理
	// int index=obj[3].toString().indexOf("\"");
	// int index2=obj[3].toString().indexOf("\"",index+1);
	// url="windowOpen('"+obj[3].toString().substring(index+1,index2)+"','"
	// + obj[0]
	// + "','advice')";
	// }else {
	// url = "mdiwin('/AdviceAction.do?operation="
	// + OperationConst.OP_DETAIL
	// + "&keyId="
	// + obj[0]
	// + "','"
	// + getMessageResources(request,
	// "advice.lb.info") + "','"
	// + obj[0]
	// + "','advice')";
	// }
	//
	//						
	// advice += "<li style=\"list-style:none; text-indent:18px;
	// text-decoration: none; background:url(/"
	// + this.getLoginBean(request).getDefStyle()
	// + "/images/bottom/inMessage.gif) no-repeat left 4px\"
	// onmouseover=\"this.style.cursor = 'hand'\" onclick=\""
	// + url + "\">" + str + "</li>";
	// }
	// if (getLoginBean(request).getPromptSound()) {
	// advice += "<embed src=\"/js/msg.wav\" loop=\"0\" autostart=\"true\"
	// hidden=\"true\"></embed>";
	// }
	// } else {
	// advice += "<span style=\"float:left;\"><img src=\"/"
	// + this.getLoginBean(request).getDefStyle()
	// + "/images/bottom/Message.gif\"></span><li>"
	// + this.getMessageResources(request,
	// "message.msg.noMsg") + "</li>";
	// }
	// } else {
	// advice += "<span style=\"float:left;\"><img src=\"/"
	// + this.getLoginBean(request).getDefStyle()
	// + "/images/bottom/Message.gif\"></span><li>"
	// + this.getMessageResources(request, "common.msg.error")
	// + "</li>";
	// }
	// }
	// out.print(advice);
	// }

	public String getMessageResources(HttpServletRequest request, String key) {
		String value = "";
		Object o = request.getSession().getServletContext().getAttribute(
				"userResource");
		if (o instanceof MessageResources) {
			MessageResources resources = (MessageResources) o;
			value = resources.getMessage(GlobalsTool.getLocale(request), key);
		}
		if (value == null || value == "") {
			o = request.getSession().getServletContext().getAttribute(
					org.apache.struts.Globals.MESSAGES_KEY);
			if (o instanceof MessageResources) {
				MessageResources resources = (MessageResources) o;
				value = resources.getMessage(GlobalsTool.getLocale(request), key);
			}
		}
		return value;
	}

	// 玫前陆没姆支
	public void getSunCompany(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		boolean isOpenSunCompanys = Boolean.parseBoolean(BaseEnv.systemSet.get(
				"sunCompany").getSetting());

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String loginName = URLDecoder.decode(request.getParameter("name"),"UTF-8");
	//	String loginName = request.getParameter("name");
		//loginName = GlobalsTool.toChinseChar_GBK(loginName);
		String sunCompanys = "";
		// 陆为
		if (!"".equals(loginName) || loginName != null) {
			UserManageAction userMgt = new UserManageAction();
			Result rsEmployee = userMgt.getUser(loginName);
			List ls = (List) rsEmployee.retVal;
			Result rs = null;
			String employeeId = null;
			if (ls.size() > 0) {
				for (int i = 0; i < ls.size(); i++) {
					employeeId = ls.get(i).toString();
				}
			} else {
				// 没写没时示息
				String msg = "";
				// 取源募
				Object o = request.getSession().getServletContext()
						.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				if (o instanceof MessageResources) {
					MessageResources resources = (MessageResources) o;
					msg = resources.getMessage(request.getLocale(),
							"login.lb.noSuchUser");
				}
				sunCompanys = "";
				out.print(msg);
				return;
			}
			/**
			if (isOpenSunCompanys) { // 朔支
				if (!"".equals(employeeId) && employeeId != null) {
					rs = userMgt.getUserSunCompany(employeeId);
				} else {
					sunCompanys = "";
				}

				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List list = (List) rs.getRetVal();
					if (list.size() == 1) { // 没只1支时
						String[] strSunCompany = (String[]) list.get(0);
						sunCompanys = strSunCompany[1]
								+ "<input type=\"hidden\" name=\"sunCompany\" value=\""
								+ strSunCompany[0] + "\">";
					} else if (list.size() > 1) { // 只械没卸支时懦
						sunCompanys += "<select name=\"sunCompany\">";
						for (int i = 0; i < list.size(); i++) {
							String[] strSunCompany = (String[]) list.get(i);
							sunCompanys += "<option value=\""
									+ strSunCompany[0] + "\">"
									+ strSunCompany[1] + "</option>";
						}
					} else {
						sunCompanys = "";
					}

					sunCompanys += "</select>";
				} else {
					sunCompanys = "";
				}
			} else { // 没梅支为默戏支id为1
				sunCompanys = "<input type=\"hidden\" name=\"sunCompany\" value=\"1\">";
			}
			**/
			out.print(sunCompanys);
		}
	}

	/**<pre>
	 * 异步弹出窗查询
	 * 弹出窗有两个方式获得(1、弹出窗名(selectName) 2，表名(tableName)加字段名(fieldName)再从自定义表设置中获取相对应的弹出窗)
	 * 流程：
	 * 先调用DynAjaxSelect.getResultStr得到返回数据
	 * 如果返回的数据不是1行，则判断是否取得到tableName和fieldName对应的字段并且根据{@link DBFieldInfoBean#getInsertTable()}
	 * 执行插入操作，再重新取得数据
	 * 返回的数据格式为：弹出窗的返回字段的值，用 <b>分号</b> 分隔
	 * </pre>
	 * @param request
	 * @param response
	 * @throws IOException
	 * @see DynAjaxSelect#getResultStr
	 * 
	 */
	public void dynSelect(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");

		String result = "";
		try {
			result = DynAjaxSelect.getResultStr(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = GlobalsTool.getMOperationMap(request);
		
		// 判断如果此字段此次查询没有结果，并且指定了是自动插入到表的，那么执行下面的操作将数据插入到相关的表中
		String tableName = request.getParameter("tableName");
		String fieldName = request.getParameter("fieldName");
			
			DBTableInfoBean tableBean = DDLOperation.getTableInfo(BaseEnv.tableInfos, tableName);
			DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(BaseEnv.tableInfos, tableName, fieldName);
			if (result.indexOf("@condition:") == 0 && fieldBean != null && fieldBean.getInsertTable() != null && fieldBean.getInsertTable().length() > 0) {
				
				OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(fieldBean.getInsertTable());
				UserFunctionMgt userMgt = new UserFunctionMgt();
				DynDBManager dbmgt = new DynDBManager();
				HashMap values = new HashMap();
				Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				MessageResources resources = null;
				if (ob instanceof MessageResources) {
					resources = (MessageResources) ob;
				}
				String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
				DBTableInfoBean insertTable = DDLOperation.getTableInfo(BaseEnv.tableInfos, fieldBean.getInsertTable());
				Result rs_initDBField = userMgt.initDBFieldInfo(insertTable,this.getLoginBean(request), values, "", workFlow);

				// 拿页面的参数值
				PopupSelectBean pop = fieldBean.getSelectBean();
				for (int i = 0; i < pop.getViewFields().size(); i++) {
					PopField popField = (PopField) pop.getViewFields().get(i);
					String paramName = "";
					String tableField = tableField = popField.asName
							.substring(popField.asName.indexOf(".") + 1);
					if (tableBean.getTableType() != 0) {
						paramName = tableName + "_"
								+ GlobalsTool.getTableField(popField.asName);
					} else {
						paramName = GlobalsTool.getTableField(popField.asName);
					}

					if (request.getParameter(paramName) != null
							&& request.getParameter(paramName).length() > 0) {
						values.put(tableField, GlobalsTool.toChinseChar_GBK(request.getParameter(paramName)));
					} else {
						DBFieldInfoBean bean = DDLOperation.getFieldInfo(BaseEnv.tableInfos, insertTable.getTableName(),tableField);
						if (bean != null) {
							if (bean.getDefaultValue() != null) {
								values.put(tableField, bean.getDefValue());
							} else {
								values.put(tableField, "");
							}
						}
					}
				}
				// 设置默认值
				userMgt.setDefault(insertTable, values, this.getLoginBean(
						request).getId());
				rs_initDBField = dbmgt.add(insertTable.getTableName(),
						BaseEnv.tableInfos, values, this.getLoginBean(request)
								.getId(), path, "", resources, GlobalsTool
								.getLocale(request), "", this
								.getLoginBean(request), workFlow, "false",props);
				// 成功插入后再次从此表中查询相关数据
				if (rs_initDBField.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					try {
						result = DynAjaxSelect.getResultStr(request, response);
					} catch (Exception ex) {

					}
				}
			}
		PrintWriter out = response.getWriter();
		out.print(result);
	}
	/**
	 * 下拉弹出框使用，返回几条记录在 DynAjaxSelect中设置
	 * @param request
	 * @param response
	 * @throws IOException
	 * @see DynAjaxSelect#DropdownPopup(HttpServletRequest, HttpServletResponse)
	 */
	public void DropdownPopup(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String result = "";
		try {
			result = DynAjaxSelect.DropdownPopup(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(result);
	}
	
	/**
	 * 下拉明细弹出框使用，返回几条记录在 DynAjaxSelect中设置
	 * @param request
	 * @param response
	 * @throws IOException
	 * @see DynAjaxSelect#DropdownPopup(HttpServletRequest, HttpServletResponse)
	 */
	public void DropdownChildData(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String result = "";
		try {
			result = DynAjaxSelect.DropdownChildData(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(result);
	}
	
	public void PopupTitle(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String result = "";
		try {
			//表名
			String tableName = request.getParameter("tableName");
			//字段名
			String fieldName = request.getParameter("fieldName");
			//弹出窗名
			String selectName = request.getParameter("selectName");
			Hashtable<String,DBTableInfoBean> allTables = (Hashtable<String,DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			LoginBean o = (LoginBean)request.getSession().getAttribute("LoginBean");
			result = DynAjaxSelect.showView(allTables, selectName, tableName, fieldName,GlobalsTool.getLocale(request),o.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(result);
	}
	
	public void RecAutoSettlement(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");

		String result = "";
		try {
			result = DynAjaxSelect.RecAutoSettlement(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(result);
	}
	/**
	 * 异步获取中文拼音首字母
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public void dyGetPYM(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String chinese = request.getParameter("chinese");
		chinese = GlobalsTool.toChinseChar(chinese);
		chinese = chinese.replaceAll(" ", "");
		String pym = CustomizePYM.getFirstLetter(chinese);
		if (pym != null && pym.length() > 30) {
			pym = pym.substring(0, 30);
		}
		PrintWriter out = response.getWriter();

		out.print(pym);
	}
	public void dyGetSpell(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String chinese = request.getParameter("chinese");
		chinese = GlobalsTool.toChinseChar(chinese);
		chinese = chinese.replaceAll(" ", "");
		String pym = CustomizePYM.getFullSpell(chinese);
		if (pym != null && pym.length() > 30) {
			pym = pym.substring(0, 30);
		}
		PrintWriter out = response.getWriter();

		out.print(pym);
	}

	/**
	 * 是否有权限访问该模块
	 */
	public void isHasPopedom(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String url = request.getParameter("urlStr");
		if (url.indexOf("src=") != -1) {
			url = url.substring(0, url.indexOf("src=") - 1);
		}
		if ("/moduleFlow.do?operation=docFlow".equals(url)) {
			out.print("true");
		} else {
			LoginBean loginBean = getLoginBean(request);
			ModuleFlowMgt mgt = new ModuleFlowMgt();
			if (loginBean != null) {
				MOperation mo = (MOperation) loginBean.getOperationMap().get(
						url);
				if (mo != null && mo.query()) {
					out.print("true");
				} else {
					Result rs_flow = mgt.getMainModel(url);
					if (rs_flow.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						String mainModule = (String) rs_flow.getRetVal();
						out.print(mainModule);
					} else {
						out.print("5");
					}
				}
			}
		}
	}

	public void removeCheck(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String str = request.getParameter("strValue");
		str = new String(str.getBytes("ISO-8859-1"));
		ArrayList<String> listCheck = (ArrayList<String>) request.getSession()
				.getAttribute("listCheck");
		ArrayList<String> listValue = (ArrayList<String>) request.getSession()
				.getAttribute("listValue");
		if (listCheck != null) {
			listCheck.remove(str);
			listValue
					.remove(str.substring(str.indexOf("@#") + 2, str.length()));
		}
	}

	public void promptSound(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		if (loginBean.getPromptSound()) {
			loginBean.setPromptSound(false);
			out.print(false) ;
		} else {
			loginBean.setPromptSound(true);
			out.print(true) ;
		}
	}

	/**
	 * 查询当前用户是否有访问某个工作计划
	 */
	public void hasVisitWorkPlan(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		String userId = request.getParameter("userId");// 要访问人的Id
		String loginId = getLoginBean(request).getId();// 登录者Id
		if ("1".equals(loginId) || loginId.equals(userId)) {
			out.print("true");
			return;
		}
		EmployeeBean employee = (EmployeeBean) ((ArrayList) new UserMgt()
				.queryEmp(userId).getRetVal()).get(0);// 用户Id获取用户部门编号。

		MOperation mop = (MOperation) this.getLoginBean(request)
				.getOperationMap().get("/OAWorkPlanAction.do");
		ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
		if (scopeRight != null) {
			boolean hasDept = false;
			boolean hasEmp = false;
			for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if ("5".equals(lsb.getFlag())) {
					String[] scopes = lsb.getScopeValue().split(";");
					for (String strScope : scopes) {
						if (employee.getDepartmentCode().startsWith(strScope)) {
							hasDept = true;
							break;
						}
					}
				} else if ("1".equals(lsb.getFlag())) {
					if (lsb.getScopeValue().contains(userId)) {
						hasEmp = true;
						break;
					}
				}
			}
			if (hasDept || hasEmp) {
				out.print("true");
			} else {
				out.print("false");
			}
		} else {
			out.print("false");
		}

	}

	public void readErrorExcel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String fileName = req.getParameter("fileName");

		resp.setContentType("application/msexcel");
		resp.setHeader("Content-Disposition", "attachment; filename="
				+ URLEncoder.encode(fileName
						.substring(fileName.indexOf("/") + 1), "UTF-8"));

		FileInputStream fos = new FileInputStream(fileName);
		byte[] bs = new byte[fos.available()];
		fos.read(bs);
		fos.close();
		resp.getOutputStream().write(bs);
	}

	public void importInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String importName = req.getParameter("importName");

		ImportThread is = (ImportThread) req.getSession()
				.getAttribute(importName+"ImportThread");

		resp.setContentType("html/text");
		String text = "";
		if (is != null && is.isRuning == false){
			text = "finish";
		}else if (is != null) {
			text = is.importState.getTotal() + "|" + is.importState.getSuccess() + "|" + is.importState.getFail();
		} else {
			text = "nullObject";
		}
		resp.getWriter().write(text);
	}

	public void cancelImport(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String importName = req.getParameter("importName");

		ImportThread is = (ImportThread) req.getSession()
		.getAttribute(importName+"ImportThread");

		is.importState.setCancel(true);
		resp.getWriter().write("ok");
	}

	public void getFieldInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Object o = req.getSession(true).getAttribute(
				org.apache.struts.Globals.LOCALE_KEY);
		if (o == null) {
			o = req.getSession().getServletContext().getAttribute(
					"DefaultLocale");
		}
		String locale = o.toString();

		String tableName = req.getParameter("tableName");
		Hashtable map = (Hashtable) req.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean table = (DBTableInfoBean) map.get(tableName);

		StringBuilder sb = new StringBuilder();
		for (DBFieldInfoBean field : table.getFieldInfos()) {
			String display = field.getDisplay() != null ? field.getDisplay()
					.get(locale) : null;
			if (display == null || display.equals(""))
				display = field.getFieldName();
			sb.append("" + field.getFieldName() + "|" + display + ";");
		}

		resp.setContentType("text/html; charset=utf-8");
		resp.getWriter().write(new String(sb.toString()));
	}

	public void importTemplete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/msexcel");

		String tableName = req.getParameter("tableName");
		
		Object o = req.getSession(true).getAttribute(
				org.apache.struts.Globals.LOCALE_KEY);
		String locale = o == null ? "" : o.toString();
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName);
		String tableDisplay =  tableInfo.getDisplay().get(locale);		
		if (!"en".equals(locale)) {
			tableDisplay = URLEncoder.encode(tableDisplay, "UTF-8");
		}
		resp.setHeader("Content-Disposition", "attachment; filename="
				+ tableDisplay + ".xls");
		
		Result result=new Result();
		
		
		try{
            String[] keyIds = new String[]{"9a75c572_1209281435516920005"};
            
	        String accName = GlobalsTool.getSysSetting("DefBackPath");
        	File file = new File(accName+"\\AIOBillExport") ;
			if(!file.exists()){
				file.mkdirs() ;
			}
			ArrayList<ExportField> exportList = new ArrayList<ExportField>();
			ArrayList<HashMap> exportValuesList = new ArrayList<HashMap>();
			
			for(int i=0;keyIds != null && i<keyIds.length;i++){
				UserFunctionAction action = new UserFunctionAction();
				ActionMapping mapping = new ActionMapping(){
					public ActionForward findForward(String str){
						return new ActionForward();
					}
					
				};
				action.updatePrepare(mapping, new BaseSearchForm(), req, resp, keyIds[i]);
				HashMap exportValues = new HashMap();
				exportValuesList.add(exportValues);
				exportList = new ArrayList<ExportField>();
				ExportData.export(req,exportList,exportValues);		        
			}
			result=ExportData.billExport(resp.getOutputStream(),tableDisplay, exportList, exportValuesList,null);
			
		}catch(Exception e){
			throw new ServletException(e);
		}
		

	}
	  /**
	   * 克隆一个DBFieldInfoBean对象
	   * @param fieldBean
	   * @return
	   */
	public DBFieldInfoBean cloneObject(DBFieldInfoBean fieldBean) {
		DBFieldInfoBean field = new DBFieldInfoBean();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(fieldBean);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			field = (DBFieldInfoBean) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field ;
	}

	/**
	 * 为“通知通告”添加的收藏方法var
	 * str="/UtilServlet?operation=addOAReadingInfo&infoId=$!globals.get($arr_newInfo,0)&infoTable=OAAdviceInfo";
	 * 
	 * @param request
	 * @param response
	 */
	private void addOAReadingInfo(HttpServletRequest request,
			HttpServletResponse response) {

		String readingInfoId = request.getParameter("infoId");// 内容所属对象ID
		String readingInfoTable = request.getParameter("infoTable");// 内容所属对象表
		String userId = this.getLoginBean(request).getId(); // 当前阅读者
		String id = IDGenerater.getId(); // 自动生成一个ID
		String readTime = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss); // 阅读时间
		String ip = request.getRemoteAddr();
		String a = "";
		try {
			if ("127.0.0.1".equals(ip)) {// 如果是在本机，改成实际的IP
				a = Inet4Address.getLocalHost().toString();
				ip = a.substring(a.indexOf('/') + 1);
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		String visitMachine = "";
		/*
		 * try { InetAddress address=InetAddress.getLocalHost();
		 * ip=address.getHostAddress().toString();
		 * visitMachine=address.getHostName().toString(); } catch
		 * (UnknownHostException e1) { e1.printStackTrace(); }
		 */

		OAReadingRecord obj = new OAReadingRecord();
		obj.setId(id);
		obj.setIp(ip);
		obj.setReader(userId);
		obj.setReadingInfoId(readingInfoId);
		obj.setReadingInfoTable(readingInfoTable);
		obj.setReadingTime(readTime);
		obj.setVisitCount(1);
		obj.setVisitMachine(visitMachine);
		Result rs = oaReadingRecordMgt.addOAReadingRecord(obj);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 添加成功
			try {
				PrintWriter out = response.getWriter();
				out.print("1");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			// 添加失败
			try {
				PrintWriter out = response.getWriter();
				out.print("0");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据登录用户查找对应部门的部门主管
	 * 
	 * @param request
	 * @param response
	 */
	private void getDepartmentManagerByEmpId(HttpServletRequest request,
			HttpServletResponse response) {
		String userId = this.getLoginBean(request).getId(); // 当前用户
		Result rs = new EmployeeMgt().selectDepartmentManagerByUid(userId);
		if (rs.getRetVal() != null && !rs.getRetVal().equals("")) {
			// 存在
			try {
				PrintWriter out = response.getWriter();
				out.print("1");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			// 不存在
			try {
				PrintWriter out = response.getWriter();
				out.print("0");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 表达式求值
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getCalculateResult(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String value = request.getParameter("value");
		String fieldName = request.getParameter("fieldName");

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		Interpreter eval = new Interpreter();

		if (value != null && !value.trim().equals("")) {
			try {
				int digits = GlobalsTool.getDigitsOrSysSetting(fieldName
						.substring(0, fieldName.indexOf("_")), fieldName
						.substring(fieldName.indexOf("_") + 1, fieldName
								.length()));
				if (digits > 0) {
					double doubleValue = GlobalsTool.round(Double
							.parseDouble(eval.eval(value).toString()), digits);
					out.write(String.valueOf(doubleValue));
				} else {
					out.write(eval.eval(value).toString());
				}
				out.flush();
				out.close();
			} catch (Exception e) {
				out.write("error");
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * 根据关键查询是否客户
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void existClient(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String keyWord = request.getParameter("keyWord");/* 关键字 */
		if (keyWord != null) {
			keyWord = GlobalsTool.toChinseChar_GBK(keyWord);
		}
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		ClientServiceMgt serviceMgt = new ClientServiceMgt();
		Result result = serviceMgt.existClient(keyWord);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			String returnValue = (String) result.getRetVal();
			if ("noData".equals(returnValue)) {
				out.print("noData");
			} else if ("more".equals(returnValue)) {
				out.print("more");
			} else {
				out.print(returnValue);
			}
		}
		out.flush();
		out.close();
	}

	/**
	 * 显示目标柱状图
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void displayGoal(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String goalClass = request.getParameter("goalClass"); /* 目标类别 */
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		MyDesktopMgt mgt = new MyDesktopMgt();
		String goalType = BaseEnv.systemSet.get("CompanyGoal").getSetting();
		LoginBean login = getLoginBean(request);
		Calendar calendar = Calendar.getInstance();
		Result result = mgt.queryGoal(login.getDepartCode(), login.getId(),
				goalClass, String.valueOf(calendar.get(Calendar.YEAR)),
				goalType, GlobalsTool.getLocale(request).toString());
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.print(result.retVal);
		}
		out.flush();
		out.close();
	}

	/**
	 * 验证这个工作流设计是否已经完成
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void workFlowIsStand(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		OAWorkFlowTempMgt tempMgt = new OAWorkFlowTempMgt();
		String tempId = request.getParameter("tempId");
		boolean flag = tempMgt.isFinish(tempId);
		out.print(flag);
		out.flush();
		out.close();
	}

	/**
	 * 显示目标走势图
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void displayMS2line(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String goalClass = request.getParameter("goalClass"); /* 目标类别 */
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		MyDesktopMgt mgt = new MyDesktopMgt();
		String goalType = BaseEnv.systemSet.get("CompanyGoal").getSetting();
		LoginBean login = getLoginBean(request);
		Calendar calendar = Calendar.getInstance();
		Result result = mgt.queryGoalMS2Line(login.getDepartCode(), login
				.getId(), goalClass, String
				.valueOf(calendar.get(Calendar.YEAR)), goalType, GlobalsTool.getLocale(
				request).toString());
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.print(result.retVal);
		}
		out.flush();
		out.close();
	}

	/**
	 * 判断是否做了列配置，如果做了列配置查看是否显示
	 * 
	 * @return
	 */
	public boolean existInColconfig(String tableName, String goalType,
			Hashtable<String, ArrayList<ColConfigBean>> userColConfig) {
		if (userColConfig != null) {
			ArrayList<ColConfigBean> configList = userColConfig.get(tableName
					+ "bill");
			if (configList != null) {
				for (ColConfigBean config : configList) {
					if (goalType.equals(config.getColName())) {
						return true;
					}
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * 显示我的仪表盘
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void displayYibiaopan(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String goalClass = request.getParameter("goalClass"); /* 目标类别 */
		String displayType = request.getParameter("displayType"); /* 显示类型 */

		response.setContentType("text/html; charset=utf-8");
		LoginBean lg = getLoginBean(request);

		PrintWriter out = response.getWriter();
		MyDesktopMgt mgt = new MyDesktopMgt();
		String goalType = BaseEnv.systemSet.get("CompanyGoal").getSetting();

		String strCode = lg.getDepartCode();
		if ("tblEmployeeGoal".equals(goalClass)) {
			strCode = lg.getId();
		}
		Calendar calendar = Calendar.getInstance();
		/* 当月仪表盘的显示 */
		Result result = null;
		if ("varYear".equals(displayType)) {
			result = mgt.queryMyYearGoal(strCode, goalClass, calendar
					.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		} else if ("varSeason".equals(displayType)) {
			String month = "";
			if ("1,2,3,".contains(String
					.valueOf(calendar.get(Calendar.MONTH) + 1))) {// 第一季度
				month = "1,2,3";
			} else if ("4,5,6,".contains(String.valueOf(calendar
					.get(Calendar.MONTH) + 1))) {// 第二季度
				month = "4,5,6";
			} else if ("7,8,9,".contains(String.valueOf(calendar
					.get(Calendar.MONTH) + 1))) {// 第三季度
				month = "7,8,9";
			} else {// 第四季度
				month = "10,11,12";
			}
			result = mgt.queryMyMonthGoal(goalType, strCode, goalClass, month,
					calendar.get(Calendar.YEAR));
		} else {
			int month = calendar.get(Calendar.MONTH) + 1;
			String strMonth = String.valueOf(month);
			result = mgt.queryMyMonthGoal(goalType, strCode, goalClass,
					strMonth, calendar.get(Calendar.YEAR));
		}
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			int[] goals = (int[]) result.retVal;
			// Hashtable<String, ArrayList<ColConfigBean>> userColConfig =
			// (Hashtable<String, ArrayList<ColConfigBean>>) request
			// .getSession().getServletContext().getAttribute(
			// "userSettingColConfig");
			// String tableName = goalClass + "Det";
			// if ("varYear".equals(displayType)) {
			// tableName = goalClass;
			// }
			String[] colors = new String[] { "0x00BB11", "0xFFFF00",
					"0xFF0000", "0x00AA11", "0xFFAA00", "0xFF00AA" };

			String strValue = "0,";
			String strColor = "";
			ArrayList<Integer> list = new ArrayList<Integer>();

			// 重新排序
			list.add(goals[1]);
			for (int i = 2; i < goals.length; i++) {
				boolean found = false;
				for (int j = 0; j < list.size(); j++) {
					if (goals[i] < list.get(j)) {
						list.add(j, goals[i]);
						found = true;
						break;
					}
				}
				if (!found) {
					list.add(goals[i]);
				}
			}

			for (int i = 0; i < list.size(); i++) {
				strValue += list.get(i) + ",";
				strColor += colors[i] + ",";
			}
			strValue = strValue.substring(0, strValue.length() - 1);
			strColor = strColor.substring(0, strColor.length() - 1);
			out.print(strValue + ";" + strColor + ";" + goals[0]);
		} else {
			out.print("noExist");
		}
		out.flush();
		out.close();
	}

	/**
	 * 更新QA库检索次数
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void updateQA(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String keyId = request.getParameter("keyId");/* QAId */
		Result result = new ClientServiceMgt().updateQATimes(keyId);
	}

	/**
	 * 更改预警状态为已阅读
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void updateAlertStatus(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String alertId = request.getParameter("alertId");
		String type = request.getParameter("type");
		if (alertId == null || alertId.length() == 0)
			return;
		if ("advice".equals(type)) {
			new AdviceMgt().readOverById(alertId);
		}
	}



	/**
	 * 单据发送消息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void sendBillMsg(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String smsType = request.getParameter("smsType");
		String wakeUp = request.getParameter("wakeUp"); /* 提醒类型 */
		String billMsg = request.getParameter("billMsg"); /* 消息内容 */
		String strPopedomUserIds = request.getParameter("popedomUserIds"); /* 职员Id */
		String strPopedomDeptIds = request.getParameter("popedomDeptIds"); /* 部门Id */
		String strPopedomCompanyCodes = request
				.getParameter("popedomCompanyCodes"); /* 往来单位Id */
		String strPopedomCRMCompany = request.getParameter("popedomCRMCompany"); /* 客户Id */

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		if (wakeUp != null && billMsg != null && billMsg.length() > 0) {
			String popedomUserIds = ""; // 所有通知对象
			if (strPopedomDeptIds != null && !"".equals(strPopedomDeptIds)) {
				String[] deptIds = strPopedomDeptIds.split(",");
				OAAdviceMgt adviceMgt = new OAAdviceMgt();
				for (String departId : deptIds) {
					List<Employee> list_emp = adviceMgt
							.queryAllEmployeeByClassCode(departId);// 根据部门编号查找部门人员
					for (Employee emp : list_emp) {
						if (!popedomUserIds.contains(emp.getid())) {// 判断是否已经单独授权
							popedomUserIds += emp.getid() + ",";
						}
					}
				}
			}
			if (strPopedomUserIds == null) {
				strPopedomUserIds = "";
			}
			if (strPopedomCompanyCodes != null) {
				popedomUserIds += strPopedomCompanyCodes;
			}
			if (strPopedomCRMCompany != null) {
				popedomUserIds += strPopedomCRMCompany;
			}
			popedomUserIds += strPopedomUserIds;
			for (String wakeup : wakeUp.split(";")) {
				Thread wakeupThread = new Thread(new NotifyFashion(
						getLoginBean(request).getId(), billMsg, billMsg,
						popedomUserIds, Integer.parseInt(wakeup), "no", ""));
				wakeupThread.start();// 启动一个通知线程。
			}
		}
		out.print("sendSucess");
		out.flush();
		out.close();
	}

	/**
	 * 测试http是否可用
	 * 
	 * @param request
	 * @param response
	 */
	private void testHppt(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String httpURL = request.getParameter("httpURL");

		String aioUrl = httpURL + "/services/AIOShopServices";
		HttpURLConnection conn = (HttpURLConnection) new URL(aioUrl)
				.openConnection();
		int resCode = 0;
		try {
			resCode = conn.getResponseCode();
		} catch (Exception e) {
			resCode = -1;
		}
		out.print(resCode);
	}

	/**
	 * 数量明码出库时 裁剪
	 * 
	 * @param request
	 * @param response
	 */
	private void qtySplit(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String billDate = request.getParameter("BillDate");
		String stockCode = request.getParameter("StockCode");
		String seq = request.getParameter("Seq");
		String qty = request.getParameter("Qty");
		String qty2 = request.getParameter("Qty2");

		DynDBManager dbManager = new DynDBManager();
		String userId = getLoginBean(request).getId();
		Object ob = request.getSession().getServletContext().getAttribute(
				org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		Result result = dbManager.qtySplit(seq, billDate, qty, qty2, stockCode,
				userId, resources, GlobalsTool.getLocale(request));
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.print(result.retVal);
		} else {
			String[] msg = (String[]) result.retVal;
			String alertMsg = "failure";
			if (msg != null) {
				String strMsg = getMessageResources(request, msg[0]);
				if (strMsg != null && strMsg.trim().length() > 0) {
					alertMsg += "@" + strMsg;
				}
			}
			out.print(alertMsg);
		}
	}

	/**
	 * 保存打印的MRP运算的数量
	 * 
	 * @param request
	 * @param response
	 */
	private void saveMrpQty(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.getSession().setAttribute("mrpQty",
				request.getParameter("mrpQty"));
	}

	/**
	 * 刷新AIOSHOP是否存在新的咨询和评论，投诉
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void freshAIOShop(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		if (this.getLoginBean(request) == null) {
			out.print("close");
			return;
		}
		AIOShopBean shopBean = BaseEnv.AIO_SHOP;
		Service srvcModel = new ObjectServiceFactory()
				.create(IAIOShopServices.class);
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		String aioShopURL = shopBean.getLinkAddr()
				+ "/services/AIOShopServices";
		IAIOShopServices srvc = (IAIOShopServices) factory.create(srvcModel,
				aioShopURL);
		/* 查看是否有新的咨询和评论 */
		String aioshop = "";
		try {
			String returnValue = srvc.freshConsult();
			if ("news".equals(returnValue)) {
				String currentTime = Long.toString(Calendar.getInstance()
						.getTimeInMillis());
				LoginBean loginBean = getLoginBean(request);
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				md.update((loginBean.getShopName() + currentTime + loginBean
						.getShopPwd()).getBytes());
				String md5 = toHex(md.digest());
				aioshop += "<span style=\"float:left;\"><img src=\"/"
						+ this.getLoginBean(request).getDefStyle()
						+ "/images/bottom/Message.gif\"></span><li><a href=\"javascript:window.open('http://192.168.2.232/system/TelnetLogin?netTime="
						+ currentTime + "&login=" + md5
						+ "')\" >AIO-SHOP存在新的咨询和评论,请及时回复</a></li>";

			}
			returnValue = srvc.existNewMember();
			if ("exist".equals(returnValue)) {
				aioshop += "<span style=\"float:left;\"><img src=\"/"
						+ this.getLoginBean(request).getDefStyle()
						+ "/images/bottom/Message.gif\"></span><li>AIO-SHOP网站有新的会员注册，请及时查看</li>";
			}
			returnValue = srvc.existNewOrder();
			if ("exist".equals(returnValue)) {
				aioshop += "<span style=\"float:left;\"><img src=\"/"
						+ this.getLoginBean(request).getDefStyle()
						+ "/images/bottom/Message.gif\"></span><li>AIO-SHOP网站有新的订单，请及时查看</li>";
			}
		} catch (Exception e) {

		}
		if (!"".equals(aioshop)) {
			if (getLoginBean(request).getPromptSound()) {
				aioshop += "<embed src=\"/js/msg.wav\" loop=\"0\" autostart=\"true\" hidden=\"true\"></embed>";
			}
			out.print(aioshop);
		} else {
			out.print("no");
		}
	}

	/**
	 * 表现评估 工作流 流程走向
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getFlowDesc(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String designId = request.getParameter("designId");
		String billId = request.getParameter("billId");
		try {
			Object ob = request.getSession().getServletContext().getAttribute(
					org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}
			String flowDoc = new HRReviewMgt().getFlowDepict(designId, billId,
					GlobalsTool.getLocale(request), resources);
			out.print(flowDoc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	/**
	 * 锁定列
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void lockColumn(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String number = request.getParameter("number");
		String report = request.getParameter("report");
		ReportSetMgt reportMgt = new ReportSetMgt();
		reportMgt.lockReportColumn(number, report);
		out.flush();
		out.close();
	}


	/**
	 * 默认列配置
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void defaultConfig(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String tableName = request.getParameter("tableName");
		String tableDisplay = "";
		if(tableName != null){
			Hashtable allTables = (Hashtable) request.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
			if (tableInfo != null) {
				KRLanguage kr = tableInfo.getDisplay();
				if (kr != null) {
					tableDisplay = kr.get(GlobalsTool.getLocale(request).toString()).toString();
				}
			}
		}
		String userId = this.getLoginBean(request).getId();
		String reportNumber = request.getParameter("reportNumber");
		String reportName = request.getParameter("reportName");
		if(reportName != null && reportName.length() > 0 && !"undefined".equals(reportName) ){
			tableDisplay=new String(reportName.getBytes("ISO8859_1"),"UTF-8");
		}
		String colType = request.getParameter("colType");
		String allTableName = request.getParameter("allTableName");
		String popupName = request.getParameter("popupName");

		ColConfigMgt configMgt = new ColConfigMgt();
		ColDisplayMgt displayMgt = new ColDisplayMgt();
		if ("popup".equals(colType)) {// 弹出窗口
			// 删除 列配置
			configMgt.delByTableName(popupName, colType,"1");
			// 删除 列宽配置
			displayMgt.defaultColWidth(tableName, colType,"1");
			
			new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		"默认列配置:"+tableDisplay+"弹出窗:"+popupName, "tblColConfig", tableDisplay,"");
		} else if ("report".equals(colType)) {// 报表中心
			// 删除 列配置
			configMgt.delByTableName(reportNumber, "list",userId);
			// 删除 列宽配置
			displayMgt.defaultColWidth(reportNumber, "list",userId);
			new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		"默认列配置:"+tableDisplay+"报表", "tblColConfig", tableDisplay,"");
		} else if ("list".equals(colType)) {// 单据列表
			// 删除 列配置
			configMgt.delByTableName(tableName, colType,userId);
			// 删除 列宽配置
			displayMgt.defaultColWidth(tableName, colType,userId);
			new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		"默认列配置:"+tableDisplay+"数据表列表", "tblColConfig", tableDisplay,"");
		} else if ("bill".equals(colType)) {
			allTableName += tableName + ",";
			configMgt.delByTableNameAndColType(allTableName, colType);
			new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		"默认列配置:"+tableDisplay+"单据", "tblColConfig", tableDisplay,"");
		} else if ("colWidth".equals(colType)) {
			allTableName += tableName + ",";
			displayMgt.defaultColWidth2(allTableName, colType);
			new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		"默认列配置:"+tableDisplay+"单据列宽", "tblColConfig", tableDisplay,"");
		}
		// 更新 缓存
		InitMenData init = new InitMenData();
		init.initUserColConfig(request.getSession().getServletContext());
		PopSelectConfig.parseConfig();
		init.initUserColWidth(request.getSession().getServletContext());
		GenJS.clearJS();
		if (!"bill".equals(colType)) {
			/* 取消 锁定列 */
			ReportSetMgt reportMgt = new ReportSetMgt();
			reportMgt.lockReportColumn("0", reportNumber);
		}
		out.flush();
		out.close();
	}

	/**
	 * 报表，弹出窗口列配置
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void colconfig(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		String userId = getLoginBean(request).getId();
		String tableName = request.getParameter("tableName"); // 表名
		String allTableName = request.getParameter("allTableName");
		String reportNumber = request.getParameter("reportNumber");
		String colType = request.getParameter("colType"); // 类型
		String popupName = request.getParameter("popupName");
		String colNames = request.getParameter("colSelect"); // 列名
		
		String tableDisplay = "";
		if(tableName != null){
			Hashtable allTables = (Hashtable) request.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
			if (tableInfo != null) {
				KRLanguage kr = tableInfo.getDisplay();
				if (kr != null) {
					tableDisplay = kr.get(GlobalsTool.getLocale(request).toString()).toString();
				}
			}
		}
		String reportName = request.getParameter("reportName");
		if(reportName != null && reportName.length() > 0 && !"undefined".equals(reportName) ){
			tableDisplay=new String(reportName.getBytes("ISO8859_1"),"UTF-8");
		}
		String lockName = request.getParameter("lockName");

		ColConfigMgt configMgt = new ColConfigMgt();
		ColDisplayMgt displayMgt = new ColDisplayMgt();
		if ("bill".equals(colType)) {
			if (colNames != null) {
				String[] tables = allTableName.split(",");
				for (int i = 0; i < tables.length; i++) {
					colNames = colNames.replaceAll("a" + i + ":", tables[i]
							+ ":");
				}
				Result rs_del = configMgt.delByTableNameAndColType(
						allTableName, colType);
				if (rs_del.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					String[] colSelect = colNames.split(",");
					for (int i = 0; i < colSelect.length; i++) {
						String[] str = colSelect[i].split(":");
						configMgt.add(str[1],str[2],str[3], str[0], "", Integer
								.parseInt(str[4]), colType, i + 1, userId);
					}
					new InitMenData().initUserColConfig(request.getSession()
							.getServletContext());
				}
				new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
	            		"列配置:"+tableDisplay+"单据", "tblColConfig", tableDisplay,"");
			}
		} else if ("colWidth".equals(colType)) {
			/* 单据从表列宽设置 */
			if (colNames != null && colNames.length() > 0) {
				// 获取用户自定义列名信息
				Hashtable<String, ColDisplayBean> userSetWidth = (Hashtable<String, ColDisplayBean>) request
						.getSession().getServletContext().getAttribute(
								"userSettingWidth");
				String[] str = colNames.split(";");
				ColDisplayMgt disMgt = new ColDisplayMgt();
				if (str != null && str.length > 0) {
					for (String strName : str) {
						String[] fields = strName.split(":");
						ColDisplayBean display = new ColDisplayBean();
						display.setTableName(fields[0]);
						display.setColName(fields[1]);
						display.setColWidth(fields[2]);
						display.setColType("bill");
						Result rs = disMgt.addUserSetColWidth(display);
						if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							if (fields[1].contains("_")) {
								if (userSetWidth != null) {
									ColDisplayBean colBean = userSetWidth
											.get(fields[0] + fields[1]);
									if (colBean != null) {
										colBean.setColWidth(display
												.getColWidth());
									} else {
										userSetWidth.put(fields[0] + fields[1],
												display);
									}
								}
							} else {
								Object o = request.getSession()
										.getServletContext().getAttribute(
												BaseEnv.TABLE_INFO);
								Hashtable tab = (Hashtable) o;
								DBTableInfoBean tib = DDLOperation
										.getTableInfo(tab, fields[0]);
								if (tib != null) {
									ArrayList<DBFieldInfoBean> fieldInfo = (ArrayList<DBFieldInfoBean>) tib
											.getFieldInfos();
									for (int j = 0; j < fieldInfo.size(); j++) {
										DBFieldInfoBean field = fieldInfo
												.get(j);
										if (field.getFieldName().equals(
												fields[1])) {
											field.setWidth(Integer
													.parseInt(fields[2]));
										}
									}
								}
							}
						}
					}
				}
				new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
	            		"列配置:"+tableDisplay+"单据列宽", "tblColConfig", tableDisplay,"");
			}
		} else if ("list".equals(colType)) {
			/* 单据报表形式 */
			if (colNames != null) {
				if("1".equals(userId)){
					configMgt.updateListConfigAdmin(tableName, colNames,lockName);
					//更新列配置缓存
					new InitMenData().initUserColConfig(request.getSession()
							.getServletContext());
				}else{
					configMgt.updateListConfig(tableName, colNames, userId,lockName);
				}
				new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
	            		"列配置:"+tableDisplay+"数据表列表", "tblColConfig", tableDisplay,"");
			}
		} else if ("report".equals(colType)) {
			/* 报表中心 */
			if (colNames != null) {
				if("1".equals(userId)){
					configMgt.updateListConfigAdmin(reportNumber, colNames,lockName);
					//更新列配置缓存
					new InitMenData().initUserColConfig(request.getSession()
							.getServletContext());
				}else{
					configMgt.updateListConfig(reportNumber, colNames, userId,lockName);
				}
				new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
	            		"列配置:"+tableDisplay+"数据表列表", "tblColConfig", tableDisplay,"");
			}
		} else {
			/* 弹出窗口形式 */
			if (colNames != null) {
				Result rs_del = configMgt.delByTableName(popupName, colType,"1");
				// 获取用户自定义列宽
				Hashtable<String, ColDisplayBean> userSetWidth = (Hashtable<String, ColDisplayBean>) request
						.getSession().getServletContext().getAttribute(
								"userSettingWidth");
				if (rs_del.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					String[] colSelect = colNames.split("\\|");
					for (int i = 0; i < colSelect.length; i++) {
						String[] nameWidth = colSelect[i].split(":");
						if ("autoNum".equals(nameWidth[0])
								|| "nextClass".equals(nameWidth[0])) {
							continue;
						}
						configMgt.add(nameWidth[0],nameWidth[0],nameWidth[0], popupName, tableName, 0,
								colType, i + 1, userId);
						// 添加列宽配置
						ColDisplayBean display = new ColDisplayBean();
						display.setTableName(tableName);
						display.setColType(colType);
						display.setId(IDGenerater.getId());
						display.setColName(nameWidth[0]);
						display.setColWidth(nameWidth[1]);
						displayMgt.addUserSetColWidth(display);
						// 更新列宽缓存
						if (userSetWidth != null) {
							ColDisplayBean colBean = userSetWidth.get(tableName
									+ nameWidth[0]);
							if (colBean != null) {
								colBean.setColWidth(display.getColWidth());
							} else {
								userSetWidth.put(tableName + nameWidth[0],
										display);
							}
						}
					}
					new InitMenData().initUserColConfig(request.getSession()
							.getServletContext());
				}
				
				new DynDBManager().addLog(9, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
	            		"列配置:"+tableDisplay+"弹出窗:"+popupName, "tblColConfig", tableDisplay,"");
			}
			PopSelectConfig.parseConfig();
		}
		// 更新 缓存
		InitMenData init = new InitMenData();
		init.initUserColWidth(request.getSession().getServletContext());
		GenJS.clearJS();
		out.flush();
		out.close();
	}

	/*
	 * 把加密后字节数组转换成16进制字符
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
	 * 存为模板
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void saveAsModule(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String tableName = request.getParameter("tableName");
		String action = request.getParameter("action");
		String content = request.getParameter("content");
		if (content != null) {
			content = GlobalsTool.toChinseChar(content);
			content = content.replaceAll("\r\n", "");
		}
		UserFunctionMgt mgt = new UserFunctionMgt();
		Result rss = mgt.saveAsModule(tableName, action, content);
		if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.print("OK");
		} else {
			out.print("NO");
		}
		out.flush();
		out.close();
	}

	/**
	 * 取消提醒
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void cancelAlert(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String alertId = request.getParameter("alertId");
		Result result = new UserFunctionMgt().deleteAlert2(alertId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.print("OK");
		} else {
			out.print("NO");
		}
		out.flush();
		out.close();
	}

	public void queryAllName(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		/*response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		OnlineUser[] list = OnlineUserInfo.getAllUserList();
		String msgStr = "";
		int num = Integer.parseInt(BaseEnv.systemSet.get("isOperUser")
				.getSetting());
		if (num == 0) {
			msgStr += "<select name=\"chooses\" id=\"chooses\" onChange =\"choose(this.value);\">";
			msgStr += "<option value=\"" + "\" selected >" + "快速登录</option>";
			for (int i = 0; i < list.length; i++) {
				OnlineUser emp = list[i];
				msgStr += "<option value=\"" + emp.getSysName() + "\" >"
						+ emp.getName() + "</option>";
			}
			msgStr += "</select>";
		}
		out.print(msgStr);
		out.flush();
		out.close();*/
	}


	/**
	 * HTML模板查询
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void queryHTMLModule(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String msgStr = "";
		LoginBean login = getLoginBean(request);
		List<HTMLTemplate> list = pubMgt.queryHTMLTemplate(login.getId());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().length() < 10) {
				msgStr += "<option value=\"" + list.get(i).getId() + "\" >"
						+ list.get(i).getName() + "</option>";
			} else {
				String name = list.get(i).getName().substring(0, 10) + "...";
				msgStr += "<option value=\"" + list.get(i).getId() + "\" >"
						+ name + "</option>";
			}
		}
		out.print(msgStr);
		out.flush();
		out.close();
	}

	/**
	 * 通过ID查询HTML模板
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void queryHTMLModuleID(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String msgStr = "";
		String id = request.getParameter("id");
		List<HTMLTemplate> list = pubMgt.queryHTMLTemplateID(id);
		for (int i = 0; i < list.size(); i++) {
			msgStr += "<option value=\"" + list.get(i).getId() + "\" >"
					+ list.get(i).getContent() + "</option>";
		}
		out.print(msgStr);
		out.flush();
		out.close();
	}

	/**
	 * 添加祝贺
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void addfameWish(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String userId = request.getParameter("userId"); // 明细表的用户ID
		String typeId = request.getParameter("typeId"); // 类型
		String id = request.getParameter("id"); // 光荣明细代号
		String Employeeid = getLoginBean(request).getId(); // 当前登录用户

		Result result = new MyDesktopMgt().addfameTopWish(id, typeId, Employeeid, userId);
		String fstr = "";
		if (Integer.parseInt(typeId) == 3) {
			fstr = this.getMessageResources(request, "crm.deskTop.Defiant");
		} else if (Integer.parseInt(typeId) == 2) {
			fstr = this
					.getMessageResources(request, "crm.deskTop.Blood.Arouse");
		} else {
			fstr = this.getMessageResources(request, "crm.deskTop.Applause");
		}
		String FullName = getLoginBean(request).getEmpFullName();
		String title = this.getMessageResources(request, "crm.deskTop.fameTop")
				+ ":" + FullName
				+ this.getMessageResources(request, "fame.lb.foryou") + fstr;
		notify
				.sendAdvice(
						Employeeid,
						title,
						"<a href=\"javascript:mdiwin('/MyDesktopAction.do?operation=5&status=0','RES<crm.deskTop.fameD>')\">"
								+ title + "</a>", userId, "", "fame");

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.print("OK");

		} else {
			out.print("NO");
		}
		out.flush();
		out.close();
	}

	/**
	 * 查询给鼓励，鲜花，下挑战书的人数
	 * 
	 * @param request
	 * @param response
	 * @param response
	 * @throws IOException
	 */
	public void queryfameWishType(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String employeeID = request.getParameter("employeeID"); // 明细表的employeeID,得到祝贺的ID
		String msgstr = "";
		MyDesktopMgt mgt = new MyDesktopMgt() ;
		Result re = mgt.queryeval(employeeID);
		msgstr += re.retVal + ";";
		for (int i = 1; i < 4; i++) {
			Result result = mgt.queryApplause(i, employeeID);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				msgstr += result.retVal + ";";
			}
		}
		out.print(msgstr);
		out.flush();
		out.close();
	}

	/**
	 * 新建文件夹
	 * 
	 * @param request
	 * @param response
	 * @param response
	 * @throws IOException
	 */
	public void createNewFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String msgstr = "";
		String paths = request.getParameter("path");
		String path = GlobalsTool.toChinseChar_GBK(paths);
		String addFile = request.getParameter("addFile");
		String fileName = GlobalsTool.toChinseChar_GBK(addFile);
		File file = null;
		file = new File(path + "/" + fileName);
		if (null != path && !"".equals("path")) {
			if (!file.exists()) {
				file.mkdirs();
				msgstr = path;
				out.print(msgstr);
			} else {
				msgstr = "";
				out.print(msgstr);
			}
		}
		out.flush();
		out.close();
	}
	

	// 文件打包下载 mj
	public static HttpServletResponse downLoadFiles(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			String albumId = request.getParameter("albumId");
			// 查询该相册对应的 所有的 照片信息
			AlbumMgt aMgt = new AlbumMgt();
			Result result = aMgt.queryBegnameByAlbumId("desc" ,albumId,1,100);
			List<PhotoInfoBean> photos = (ArrayList<PhotoInfoBean>)result.getRetVal();
			List<File> files = new ArrayList<File>();
			String tempName = ((AlbumBean)aMgt.load(albumId).getRetVal()).getName();
			for (int i = 0; i < photos.size(); i++) {
				String beginName = photos.get(i).getBeginName();
				String path =  BaseEnv.FILESERVERPATH+"/album/img/"+beginName;
				File file = new File(path);
				files.add(file);
			}

			/**
			 * 创建一个临时压缩文件， 我们会把文件流全部注入到这个文件中 这里的文件你可以自定义是.rar还是.zip
			 */
			tempName = GlobalsTool.encode(tempName);
			File file = new File("c:/"+tempName+".rar");
			if (!file.exists()) {
				file.createNewFile();
			}
			response.reset();
			// response.getWriter()
			// 创建文件输出流
			FileOutputStream fous = new FileOutputStream(file);
			org.apache.tools.zip.ZipOutputStream zipOut = new org.apache.tools.zip.ZipOutputStream(
					fous);
			// ZipOutputStream zipOut = new ZipOutputStream(fous); 該java自帶的
			// 會有中文亂碼問題
			/**
			 * 这个方法接受的就是一个所要打包文件的集合， 还有一个ZipOutputStream
			 */
			// 設置編碼
			boolean isWindows = GlobalsTool.isWindowsOS(); // 判断是否是windows系统
			if (isWindows) {
				zipOut.setEncoding("GBK");// 设置为GBK后在windows下就不会乱码了，如果要放到Linux或者Unix下就不要设置了
			}

			GlobalsTool.zipFile(files, zipOut);
			zipOut.close();
			fous.close();
			return GlobalsTool.downloadZip(file, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 直到文件的打包已经成功了， 文件的打包过程被我封装在FileUtil.zipFile这个静态方法中，
		 * 稍后会呈现出来，接下来的就是往客户端写数据了
		 */
		// OutputStream out = response.getOutputStream();
		return response;
	}
	
	
	protected int getParameterInt(String param, HttpServletRequest req) {
		try {
			return Integer.parseInt(req.getParameter(param));
		} catch (NumberFormatException ex) {
			return 0;
		}
	}
	
	protected String getParameter(String param, HttpServletRequest req) {
		return req.getParameter(param);
	}
	
	/**
	 * 非正常登陆进入的.默认把屏幕宽度放入session
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void screenWidth(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String screenWidth=request.getParameter("screenWidth");//页面获取宽度
		//先拿session查看是否已经有宽度
		String width = (String)request.getSession().getAttribute("screenWidth");
		if(width==null || "".equals(width)){
			//若没有把页面获取的宽度放入session 
			request.getSession().setAttribute("screenWidth", screenWidth);
		}
	}
	public void downSelect(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html; charset=utf-8");
		String msgStr="";
		PrintWriter out = response.getWriter();
		String sql = request.getParameter("sql");
		String field = request.getParameter("field");
		if(sql==null || sql.length() ==0){
			
			//取得这个字段的sql语句
			DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(field);
			sql = fieldBean==null?"":fieldBean.getInputValue();
			if(sql==null || sql.length()==0){
				msgStr = "ERROR:"+field+" inputValue is Null !";
			}
		}
		if((sql==null || sql.length()==0) && msgStr.length()==0){
			msgStr = "ERROR: sql is Null !";
		}
		if(sql != null && sql.length() > 0){
			Pattern pattern = Pattern.compile("@ValueOfDBMust:([\\w]+)");
			Matcher matcher = pattern.matcher(sql);	
			
			while(matcher.find()){
				String temp = matcher.group(1);
				String value = request.getParameter(temp);
				value = value==null?"":new String(value.getBytes("ISO8859-1"),"UTF-8");
				sql = sql.replaceAll("@ValueOfDBMust:"+temp, "'"+value+"'");
			}
			pattern = Pattern.compile("@ValueOfDB:([\\w]+)");
			matcher = pattern.matcher(sql);	
			while(matcher.find()){
				String temp = matcher.group(1);
				String value = request.getParameter(temp);
				value = value==null?"":new String(value.getBytes("ISO8859-1"),"UTF-8");
				if(value.length()==0){
					//去掉sql=空条件。
					sql=sql.replaceAll("[\\w|.]+[\\s]*=[\\s]*@ValueOfDB:"+temp, "1=1");
				}else{
					sql=sql.replaceAll("@ValueOfDB:"+temp, "'"+value+"'");
				}
				

			}
			pattern = Pattern.compile("@ValueOfConst:([^\\s]+)");
			matcher = pattern.matcher(sql);	
			while(matcher.find()){
				String temp = matcher.group(1);
				sql=sql.replaceAll("@ValueOfConst:"+temp, "'"+temp+"'");
			}
			
			BaseEnv.log.debug("UtilServlet.downSelect field="+field+";sql="+sql);
			//执行数据库查询
			PublicMgt pmgt= new PublicMgt();
			msgStr = pmgt.downSelect(sql);
		}
		
		out.print(msgStr);
		out.flush();
		out.close();
	}

	/**
	 * 设置桌面
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void setDesk(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		response.setContentType("text/plain; charset=UTF-8");
		String url = getParameter("url", request);										//url连接地址
		String title = getParameter("title", request);									//标题
		String mainModule = getParameter("mainModule", request);						//所属模块类型（ERP,OA,CRM,HR）
		LoginBean login = getLoginBean(request);
		/* 处理URL中存在文字 */
		url = GlobalsTool.toChinseChar(url);
		title = GlobalsTool.toChinseChar(title);
		String desk = mainModule+","+title+","+url;
		PrintWriter out = response.getWriter();
		Result rs = userMgt.setDesk(login.getId(), desk);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//修改成功
			int count = Integer.parseInt(rs.retVal.toString());
			if(count == 1){
				out.print("OK");
				login.setDefDesk(desk);
				request.getSession().setAttribute("LoginBean", login);
			}else{
				out.print("ERROR");
			}
		}else{
			out.print("ERROR");
		}
	    out.flush();
		out.close();
	}
	/**
	 * 推送单据
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void pushBill(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String tableName = request.getParameter("tableName");
		String destTableName = request.getParameter("destTableName");
		
		if("true".equals(request.getParameter("getFirstChildTable"))){			
			out.print(new TableMappedMgt().queryChildByMostlyAndChild(tableName, destTableName)) ;	        
	        out.flush();
			out.close();
			return;
		}
		
		String keyId=request.getParameter("keyId");
		String sb="推单成功";
		
		String userId = request.getParameter("userId");
		LoginBean lb= GlobalsTool.getLoginBean(request);
		if(userId == null || userId.length() ==0){
			userId= lb.getId();
		}
		String varKeyIds = request.getParameter("varKeyIds");
		String detId = request.getParameter("detId"); //明细行ID
		boolean isContinue = "true".equals(request.getParameter("isContinue"))?true:false; //系统检查到单据以经有推送记录时，要提示，是否继续
		Result rs =new DynDBManager().billPush(tableName, destTableName, varKeyIds, userId,lb, detId,isContinue,request);
        
        if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
        	sb="ok:";
        	String id = rs.retVal==null?"":rs.retVal.toString();
        	
        	String url  = "/UserFunctionAction.do?tableName="+destTableName+"&keyId="+id+
        			"&moduleType=&f_brother=&operation=7&pageNo=&parentCode=&parentTableName=&saveDraft=draft";
        	//找出当前用户有没有目标模块的权限
        	MOperation m = (MOperation)getLoginBean(request).getOperationMap()
					.get("/UserFunctionQueryAction.do?tableName="+destTableName);
			if (m != null && m.add()) {
				sb +="update:";
			}else if (m != null && m.query()) {
				sb +="query:";
			}else{
				sb +="noright:";
			}
			DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(destTableName);
			sb += tableInfo.getTWidth()+":"+tableInfo.getTHeight()+":";        	
        	sb = sb+url;
        	String tableDis = GlobalsTool.getTableInfoBean(destTableName).getDisplay().get(GlobalsTool.getLocale(request).toString());
        	new AdviceMgt().add(lb.getId(), lb.getEmpFullName()+
        			"推送给您一张"+GlobalsTool.getTableInfoBean(destTableName).getDisplay().get(GlobalsTool.getLocale(request).toString())+"，请查收", 
        			"<a href=\"javascript:mdiwin('"+url+"','"+tableDis+"')\" >"+ lb.getEmpFullName()+"推送给您一张"+tableDis+"，请查收</a>", 
        			userId, id, "other");
        }else if(rs.retCode== ErrorCanst.USER_STOP){
        	sb="HASPUSH";
        }else{
        	if(rs.getRetCode() == ErrorCanst.BILL_ADD_WORK_FLOW_ERROR){
        		sb  =GlobalsTool.getMessage(request, "com.add.workfow.error");
            } else if(rs.getRetCode() == ErrorCanst.CURRENT_ACC_BEFORE_BILL){
        		sb  ="不能录入会计期间前数据或未开帐";
            }else if(rs.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC){
            	/*单据日期的期间在会计期间中不存在*/
            	sb  ="单据日期的期间在会计期间中不存在";
            }  else if(rs.retVal != null){
        		sb = rs.retVal+"";
        	}else{
        		sb="推单失败";
        	}
        }
        out.print(sb) ;
        
        out.flush();
		out.close();
	}
	
	/**
	 * 查询发送邮件人的历史记录
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	public void mailSendHistory(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		response.setCharacterEncoding("utf-8");
		//用户Id
		String loginId = request.getParameter("loginId");
		//email类型
		String emailType = request.getParameter("emailType");
		//关键字搜索
		String keyWord = request.getParameter("keyWord");
		String pageNo = request.getParameter("pageNo");
		keyWord = GlobalsTool.toChinseChar(keyWord);
		PrintWriter out = response.getWriter();
		Result rs = new EMailMgt().queryEmailSendHistory(loginId, emailType, keyWord, pageNo);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			Object[] obj = (Object[])rs.getRetVal();
			List list = (ArrayList)obj[1];
			String str = obj[0].toString()+";|";
			for(int i=0;i<list.size();i++){
				HashMap map = (HashMap)list.get(i);
				String receiveId = (String)map.get("receiveId");
				receiveId = receiveId.replaceAll("[\\s]*", "").trim();
				//receiveId = receiveId.replaceAll("\\(|\\)", "").trim();
				String display = (String)map.get("name");
				String emailStr  = (String)map.get("email");
				if(receiveId.indexOf("<") > -1 && receiveId.indexOf(">") == receiveId.length() - 1) {
					display = receiveId.substring(0, receiveId.indexOf("<")).trim();
					emailStr = receiveId.substring(receiveId.indexOf("<") + 1,receiveId.length() - 1).trim();
					
				}
				str += "{\"id\": \""+receiveId+"\",\"name\": \"" +display +"\",\"email\": \""+emailStr+"\"}";
				if(i<list.size()-1){
					str += ";";
				}
			}
			out.print(str);
		}
		out.flush();
		out.close();
	}
	/**
	 * 反馈建议发贴
	 * @param request
	 * @param response
	 */
	public void postFormula(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		GlobalsTool globals = new GlobalsTool();
		
		String html = "\r\n---------------------------------------------------\r\n";
		html += "联系方式："+encGbk(request.getParameter("tel"))+"   \r\n";
		html += "---------------------------------------------------\r\n加密方式：";
		if(globals.getSystemState().encryptionType == 0){  
			html += "免费版";
		}else if(globals.getSystemState().encryptionType == 3){
			html += "软加密 (序列号 "+globals.securityLockCode()+")";
		}else if(globals.getSystemState().encryptionType == 2){
			html += "加密狗 ("+getMessageResources(request,"upgrade.lb.id")+" "+globals.securityLockCode()+")";
		}else{
			html += "硬加密 ("+getMessageResources(request,"upgrade.lb.id")+" "+globals.securityLockCode()+")";
		}
		html += "\r\n";
		html += getMessageResources(request,"upgrade.lb.compName")+":";
		if("".equals(globals.getSystemState().companyName)){
			html += getMessageResources(request,"upgrade.msg.noReg");
		}else{ 
			html += globals.getCompanyName();
		}
		html += "\r\n";
		UpgradeMgt infoMgt = new UpgradeMgt();
        Result result = infoMgt.findLastInfo();
        UpdateInfo info = (UpdateInfo) result.getRetVal();
		html += getMessageResources(request,"com.product.name")+":";
		html += globals.getSysVersionName()+"-"+info.getVersionId()+"."+info.getOrderId()+" \r\n";
		html += getMessageResources(request,"upgrade.lb.regdate")+":";
		if("".equals(globals.getSystemState().registerDate)){  
			html += getMessageResources(request,"upgrade.msg.noReg");
		}else{
			html += globals.getSystemState().registerDate;
			if(!"".equals(globals.getSystemState().grantDate)){
				html += "(授权期至"+globals.getSystemState().grantDate+")";
			}
		}
		html += "\r\n";
		html += getMessageResources(request,"upgrade.lb.iModule")+":";;
		List enuList = globals.getEnumerationItems("MainModule","zh_cn");
		for (int i = 0; i < enuList.size(); i++) {
			KeyPair kp = (KeyPair) enuList.get(i);
			if(globals.hasMainModule(kp.getValue()) && !"0".equals(kp.getValue().toString()) && !"4".equals(kp.getValue().toString())){
				html += kp.getName();				
			}
		}
		if(globals.getSystemState().funUserDefine){
			html += getMessageResources(request,"upgrade.lb.userDefine")+"   ";
		}
		if(globals.getSystemState().funMoreCurrency){
			 html += getMessageResources(request,"upgrade.lb.moreCurrency")+"   ";
		}
		if(globals.getSystemState().funAttribute){
			html += "商品属性管理   ";
		}
		LoginBean lb= getLoginBean(request);
		html += "\r\n";
		html += "授权用户数量：";
	    html += "进销存财务("+globals.getSystemState().userNum+")   办公自动化("+globals.getSystemState().oaUserNum+")    客户关系管理("+globals.getSystemState().crmUserNum+") \r\n";
		html += "授权帐套数量："+globals.getSystemState().serverCount+" \r\n";
		html += "反馈用户："+lb.getEmpFullName() +"\r\n ";
		html += "部门："+lb.getDepartmentName() +"\r\n";
		html += "手机："+lb.getMobile()+"\r\n";
		html += "邮箱："+lb.getEmail()+"\r\n";
		html += "QQ："+lb.getQq()+"\r\n";
		html += "---------------------------------------------------\r\n";
		
		String str = "http://help.koronsoft.com/forum.php?mod=post&action=newthread&fid=37&topicsubmit=yes&infloat=yes&handlekey=fastnewpost&inajax=1";
		String data = "formhash=&subject="+encGbk(request.getParameter("subject"))+"&message="+encGbk(request.getParameter("postmessage"));
		data += html;
		try {
			URL u = new URL(str);
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream ous = conn.getOutputStream();
			ous.write(data.getBytes());
			ous.flush();
			ous.close();
			Reader r = new InputStreamReader(conn.getInputStream(), "gbk");
			java.io.BufferedReader br = new BufferedReader(r);
			while((str = br.readLine())!=null){
				System.out.println(str);
			}
			request.getRequestDispatcher("/feedback.jsp?responseCode="+conn.getResponseCode()).forward(request, response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	private String encGbk(String data)
	{
		try {
			return new String(data.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return data;
	}
}
