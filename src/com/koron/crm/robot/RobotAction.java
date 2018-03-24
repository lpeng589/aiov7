package com.koron.crm.robot;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koron.oa.bean.Department;
import com.koron.oa.bean.Employee;
import com.koron.oa.util.EmployeeMgt;
import com.koron.robot.AlibabaSite;
import com.koron.robot.BaiduSite;
import com.koron.robot.Google2Site;
import com.koron.robot.GoogleSite;
import com.koron.robot.HuiCongSite;
import com.koron.robot.InfoItem;
import com.koron.robot.InformationFinder;
import com.koron.robot.Net114Site;
import com.koron.robot.SearchSite;
import com.koron.robot.SnapRobot;
import com.koron.robot.SogouSite;
import com.koron.robot.SosoSite;
import com.koron.robot.TaskItem;
import com.koron.robot.YahooSite;
import com.menyi.aio.web.usermanage.*;
import com.dbfactory.Result;
import com.menyi.web.util.*;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

import org.apache.struts.action.*;

import com.menyi.aio.web.role.RoleMgt;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import com.menyi.aio.web.usermanage.UserMgt;
import java.util.Date;

import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.enumeration.EnumerationMgt;
import java.util.Locale;
import com.menyi.aio.bean.ModuleBean;
import java.util.Map;
import com.dbfactory.Result;
import com.menyi.aio.bean.ModuleOperationBean;
import com.menyi.aio.bean.UserModuleBean;
import com.menyi.aio.bean.ScopeBean;
import java.util.HashMap;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.bean.DiscontrolBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;

import java.util.*;
import com.dbfactory.hibernate.DBUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Session;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.bean.*;

import java.sql.PreparedStatement;

/**
 * Description: <br/>Copyright (C), 2008-2012, Justin.T.Wang <br/>This Program
 * is protected by copyright laws. <br/>Program Name: <br/>Date:
 *
 * @autor Justin.T.Wang yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class RobotAction extends MgtBaseAction {

	private UserMgt userMgt = new UserMgt();

	private RoleMgt roleMgt = new RoleMgt();
    
	private EmployeeMgt empMgt=new EmployeeMgt();
	private static final int OPERATION_QUERY = 4;

	
	
	public static final int ISSTOP = 0x04;// 结束状态

	public static final int FORCESTOP = 0x01;// 强制结束指令
	
	
	public RobotAction() {

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


		// 这个模块只要有查询权限就能做本模块的所有操作，所以不区分operation操作
		String op = request.getParameter("op");
		ActionForward forward = null;
		if("newTask".equals(op)){
			forward = newTask(mapping, form, request, response);
		}else if("stopTask".equals(op)){
			forward = stopTask(mapping, form, request, response);
		}else if("detail".equals(op)){
			forward = detail(mapping, form, request, response);
		}else if("delTask".equals(op)){
			forward = delete(mapping, form, request, response);
		}else if("status".equals(op)){
			forward = status(mapping, form, request, response);
		}else{
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	protected ActionForward status(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String retStr = "";		
		for(TaskItem item : SnapRobot.taskMap.values()){
			//if(item.getStatus() == 0){
				retStr += item.getCode()+":"+item.getSearchCount()+":"+item.getImportCount()+":"+item.getStatus()+"|";
			//}
		}
		request.setAttribute("msg", retStr);
		return getForward(request, mapping, "blank");
	}
	
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String pName = request.getParameter("pName");
		pName = new String(pName.getBytes("iso8859-1"),"utf-8");
				
		SnapRobot.taskMap.remove(pName);
		
		return query(mapping, form, request, response);
	}
	
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String pName = request.getParameter("pName");
		pName = new String(pName.getBytes("iso8859-1"),"utf-8");
				
		request.setAttribute("list", SnapRobot.taskMap.get(pName).getInfoItems());
		return getForward(request, mapping, "detail");
	}
	
	protected ActionForward stopTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String pName = request.getParameter("pName");
		pName = new String(pName.getBytes("iso8859-1"),"utf-8");
		int t = SnapRobot.taskMap.get(pName).getStatus();
		t = t | FORCESTOP;
		SnapRobot.taskMap.get(pName).setStatus(new Integer(t));
		
		return query(mapping, form, request, response);
	}

	
	protected ActionForward newTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		
		final String[] engine = request.getParameterValues("engine");
		final String keywords = request.getParameter("keywords");
		
		if(keywords == null || keywords.trim().length() == 0 || engine == null || engine.length ==0){
			return query(mapping,form,request,response);
		}
		final String follower = request.getParameter("follower");
		final String followerDept = request.getParameter("followerDept");
		final String followerName = request.getParameter("followerName");
		
		int ps = 20;
		try{
			ps = Integer.parseInt(request.getParameter("spages"));
			
		}catch(Exception e){}
		
		final int pages = ps;
		
		final String pName = System.currentTimeMillis()+"";

		TaskItem task = new TaskItem();	
		SnapRobot.taskMap.put(pName, task);
		task.setCode(pName);
		task.setKeywords(keywords);
		task.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(new Date()));
		
		if(follower != null && follower.length() > 0){
			task.setFollower(follower);
			task.setFollowerDept(followerDept);
			task.setFollowerName(followerName);
		}
		new Thread(new Runnable() {
			public void run() {
				TaskItem task = SnapRobot.taskMap.get(pName);
				String[] keys = keywords.split(",|;");
				if(keys.length == 0){
					return;
				}
				for(String key:keys){
					for (String string : engine) {
						SearchSite ss = getSite(string);
						
						for (int i = 0; i <pages ; i++) {
							if ((task.getStatus() & FORCESTOP) == FORCESTOP)// 如果被设置成停止则返回
							{
								Integer tmpI = task.getStatus();
								task.setStatus(new Integer(tmpI
										.intValue()
										| ISSTOP));
								return;
							}
							String content = ss.getContent(key, i);
							if(content == null || content.length() ==0){
								continue;
							}
							ArrayList<String[]> al = ss.getLink(content);
							if(al == null || al.size() ==0){
								continue;
							}
							InformationFinder fi = ss.getFinder();
							for (String[] string2 : al) {
								//System.out.println(string2[1]+":"+string2[0]);
								try {
									//string2[0] = "http://cache.baidu.com/c?m=9f65cb4a8c8507ed4fece763105392230e54f730678783492ac3933fc239045c1131a5e87c7c0d07d4c77b6c02a54f57fdf04074340822b098c98a49c9fecf68798730447b0bf03005a368b8bd4632c050872bedb868e0&p=8e49c64ad1934eaf12b4c22246&user=baidu&fm=sc&query=%B9%DC%BC%D2%C6%C5&qid=a4a555f60caa16ad&p1=55";
									InfoItem item=fi.findInforItem(new URL(string2[0]),
											string2[1]);
									if(item.isValid()&& item.getName() != null && !item.getName().equals("")){
										if(task.getInfoItems().size()>200){
											task.getInfoItems().add(0,item);
											task.getInfoItems().remove(task.getInfoItems().size()-1);
										}else{
											task.getInfoItems().add(0,item);
										}
										task.addSearch();
										Result rs=new RobotMgt().addClientInfo(key,item.getName(), (item.getFax()), item.getSite(), 
												(item.getMobile()), (item.getEmail()), (item.getPhone()), task.getFollowerDept(), task.getFollower(),task.getFollowerDept());
										if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
											task.addImport();
										}
									}
								} catch (MalformedURLException e) {
									e.printStackTrace();
								}
							}							
						}
					}
				}
				Integer tmpI = task.getStatus();
		
				task.setStatus(new Integer(tmpI.intValue()
						| ISSTOP));
		
			}
		}).start();
		
		
		EchoMessage.success().add(
				this.getMessage(request, "common.msg.addSuccess")).setBackUrl(
				"/RobotAction.do").setAlertRequest(request);

		return getForward(request, mapping, "alert");
		
	}
	
	private SearchSite getSite(String name) {
		if (name.equals("baidu"))
			return new BaiduSite();
		else if (name.equals("google"))
			return new Google2Site();
		else if (name.equals("yahoo"))
			return new YahooSite();
		else if (name.equals("sogou"))
			return new SogouSite();
		else if (name.equals("soso"))
			return new SosoSite();
		else if (name.equals("alibaba"))
			return new AlibabaSite();
		else if (name.equals("huicong"))
			return new HuiCongSite();
		else if (name.equals("net114"))
			return new Net114Site();
		else
			return new BaiduSite();
		
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
		request.setAttribute("item", SnapRobot.taskMap);
		
		return getForward(request, mapping, "list");
	}


}
