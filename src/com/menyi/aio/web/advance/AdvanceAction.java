package com.menyi.aio.web.advance;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.aio.bean.AIOShopBean;
import com.menyi.aio.bean.NoteSetBean;
import com.menyi.aio.web.aioshop.AIOShopMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.sms.setting.SMSsetMgt;
import com.menyi.web.util.AIOTelecomCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GenJS;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.SystemState;
/**
 * 
 * <p>
 * Title:高级功能Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-08-13 上午 10:55
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class AdvanceAction extends MgtBaseAction {

	AdvanceMgt mgt = new AdvanceMgt();

	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String optype = request.getParameter("optype");

		if (optype != null && "clue".equals(optype)) {
			//提示页面
			forward = clue(mapping, form, request, response);
		} else if (optype != null && "refresh".equals(optype)) {
			//刷新内存
			forward = refresh(mapping, form, request, response);
		} else if (optype != null && "restart".equals(optype)) {
			//重启系统
			forward = restart(mapping, form, request, response);
		} else if (optype != null && "index".equals(optype)) {
			//首页
			forward = index(mapping, form, request, response);
		} else {
			forward = clue(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 刷新内存
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward restart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

//		String retUrl = "";
//		//读文件set SERVICE_NAME=AIOServer
//		DataInputStream dis =  new DataInputStream(new FileInputStream("service.bat"));
//		byte[] bs = new byte[dis.available()];
//		dis.read(bs);
//		String fs  = new String(bs);
//		int pos = fs.indexOf("set SERVICE_NAME=")+"set SERVICE_NAME=".length();
//		String sererName = fs.substring(pos,fs.indexOf("\n",pos));
//		BaseEnv.log.debug("SERVICE_NAME="+sererName);
//		
//		try {      
//			Process pro = Runtime.getRuntime().exec("cmd /c net restart "+sererName);      
//			BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));     
//			String msg = null;      
//			while ((msg = br.readLine()) != null) {      
//				BaseEnv.log.debug("AdvanceAction restart="+msg);
//			}     
//			BaseEnv.log.debug("AdvanceAction restart ------开始");
//		} catch (IOException exception) {    
//			BaseEnv.log.error("AdvanceAction restart Error :",exception);
//		} 
		SystemState.instance.dogState = SystemState.DOG_RESTART; //设置重启标志         
		EchoMessage.success().add("正在重启").setBackUrl("/AdvanceAction.do?optype=index").setAlertRequest(request);
		return getForward(request, mapping, "message");
	}
	
	public static void refreshMem(HttpServletRequest request){
		InitMenData initMenData = new InitMenData();
		ServletContext servletContext = request.getSession().getServletContext();

		Result rs = initMenData.initSystemSettingInformation();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新系统配置initSystemSettingInformation 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新系统配置initSystemSettingInformation 成功-----------");
        }
        

        //加载所有的枚举值
        rs = initMenData.initEnumerationInformation();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新枚举initEnumerationInformation 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新枚举initEnumerationInformation 成功-----------");
        }
                
        rs = initMenData.initModuleList();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新模块initModuleList 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新模块initModuleList 成功-----------");
        }
        
        //加载所有用户模块自定义列名设置信息
        rs = initMenData.initModuleColLanuage(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新自定义列名initModuleColLanuage 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新自定义列名initModuleColLanuage 成功-----------");
        }

        
        //加载所有用户自定义列宽设置信息
        rs = initMenData.initUserColWidth(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新自定义列宽initUserColWidth 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新自定义列宽initUserColWidth 成功-----------");
        }
        
        //加载所有用户自定义列名设置信息
        rs = initMenData.initUserLanuageInfo(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新自定义列名initUserLanuageInfo 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新自定义列名initUserLanuageInfo 成功-----------");
        }
        
        //加载所有的表信息
        rs = initMenData.initDBInformation(servletContext,null);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新表结构initDBInformation 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新表结构initDBInformation 成功-----------");
        }
        //加载所有用户自定义列配置设置信息
        rs = initMenData.initUserColConfig(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新列配置initUserColConfig 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新列配置initUserColConfig 成功-----------");
        }
                
        //加载模块对应字段信息（主要用于多个模块使用同一张表，并且使用同一个define）
        rs = initMenData.initModuleField(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新模块字段initModuleField 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新模块字段initModuleField 成功-----------");
        }
        //加载弹出框显示所需语句
        rs = initMenData.initPopDisSen(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新弹出框显示语句initPopDisSen 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新弹出框显示语句initPopDisSen 成功-----------");
        }
        
       
        
        //加载所有商品属性
        rs = initMenData.initPropInformation(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新商品属性initPropInformation 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新商品属性initPropInformation 成功-----------");
        }
        
        //      加载所有商品属性
        rs = initMenData.initAttInformation(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新商品属性initAttInformation 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新商品属性initAttInformation 成功-----------");
        }
        		       
        
        //读配置文件
        initMenData.initConfigFile();
        BaseEnv.log.debug("--------AdvanceAction.restart 刷新配置文件initConfigFile 成功-----------");

        
        /**加载所有已启用的工作流信息**/
        rs = initMenData.initWorkFlowInfo(servletContext) ;
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新已启用工作流initWorkFlowInfo 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新已启用工作流initWorkFlowInfo 成功-----------");
        }      
        
        
        
        //初始化报表显示配置
        rs = initMenData.initReportShowSet();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新报表显示配置initReportShowSet 失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新报表显示配置initReportShowSet 成功-----------");
        }
        
        //初始化KK缓存
        if (!MSGConnectCenter.init()) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart 刷新KK缓存   失败-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart 刷新KK缓存  成功-----------");
        }
        
        rs = initMenData.initFastQuery();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error("--------AdvanceAction. initFastQuery Failture-----------");
        }
        
        GenJS.clearJS();
	}

	/**
	 * 重启
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		refreshMem(request);        
		EchoMessage.success().add("内存更新完成").setBackUrl("/AdvanceAction.do?optype=index").setAlertRequest(request);
		return getForward(request, mapping, "message");
	}

	/**
	 * 提示
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward clue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getForward(request, mapping, "clue");
		
	}

	/**
	 * 首页
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward index(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 查询所有高级功能下的模块 */
		Result result = mgt.queryModule();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//查询成功
			request.setAttribute("moduleList", result.retVal);
		}
		return getForward(request, mapping, "index");
	}
}
