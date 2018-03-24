package com.menyi.aio.web.mobile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ViewContext;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.setting.ClientSetingMgt;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAMyWorkFlowDetBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowForm;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.koron.wechat.common.media.Media;
import com.koron.wechat.common.util.BaseResultBean;
import com.koron.wechat.common.util.WXSetting;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.RoleBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.favourstyle.MessageBean;
import com.menyi.aio.web.logManage.LogManageMgt;
import com.menyi.aio.web.login.LoginAction;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.TableListResult;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.aio.web.userFunction.DynAjaxSelect;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.aio.web.wxqy.WeixinApiMgt;
import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIOConnect;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.DESPlus;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.ErrorMessage;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.SystemState;
/**
 * 深圳市科荣软件股份有限公司自定义平台接口
 * 
 * @version 1.0<br/>
 * 创建历史：2016-03-23<br/>
 * 修改历史：<br/>
 * 
 * @author 周新宇<br/><br/><br/>
 * 自定义功能接口<br/>
 * 本接口所有方法返回json对象，建议采用ajax调用。<br/>
 * 本接口同样适用C/S软件或手机APP，或JAVA HttpConnect调用，但需注意调用内部接口前应省先调用登陆接口<br/>
 * 如果是C/S软件调用接口，应先调用登陆接口，并分析接口返回头文件中的Cookie取得标识JSESSIONID,并在调用其它接口前把JSESSIONID值通过Cookie或URL连同接口要求参数一起发送给服务器
 * <br/>
 * 本接口调用地址为http://你的IP或域名/AIOApi?op=方法 或http://你的IP或域名/MobileAjax?op=方法(老版本地址)<br/>
 * <br/>
 * 例:<br/>
 * jQuery.post("/MobileAjax?op=LOGIN",<br/>
 * &nbsp;&nbsp;      { name: loginName, password: password },<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;      function(data){<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;        if(data.code != "OK"){<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         	alert(data.msg);//失败原因<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         }else{<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         //成功<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         } <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;      },<br/>
 * &nbsp;&nbsp;      "json" <br/>
 *	);<br/>
 * 应用场景一（添加单据）：<br/><br/>
 * 1、调用addPrepare.初始化界面数据，对应aio点击添加按扭，弹出添加界面，接口会返回默认值等信息<br/>
 * 2、调用add,保存数据到数据表。对应aio点击保存按扭<br/>
 * 3、调用deliverToPrepare，取单据的审核流信息。对应aio弹出窗审核界面（
 * 接口返回下一审核节点，下一审核人信息。如果单据未审核审核流，本接口可以不执行，本接口取得信息后可以弹出界面让用户选择转交信息，也可以直接调用deliver接口）<br/>
 * 4、调用deliver，执行审核程序。对应aio审核界面的保存按扭。<br/><br/>
 * 应用场景二（修改单据）：<br/><br/>
 * 1、调用detail 取单据数据，对应aio点击修改，弹出修改界面，接口同时返回本单的权限信息，是否允许修改，审核，反审核，撤回，同时包括单据字段只读，隐藏等信息<br/>
 * 2、调用update,保存数据到数据表。对应aio点击保存按扭<br/>
 * 3、调用deliverToPrepare，取单据的审核流信息。对应aio弹出窗审核界面（接口返回下一审核节点，下一审核人信息）<br/>
 * 4、调用deliver，执行审核程序。对应aio审核界面的保存按扭。<br/><br/>
 * 应用场景三（审核单据）：<br/><br/>
 * 1、调用check 校验单据（如果是修改单据，可省略本接口，如果没有调用保存按扭，一定要先调本接品，校验单据内容是否正确）<br/>
 * 3、调用deliverToPrepare，取单据的审核流信息。对应aio弹出窗审核界面（接口返回下一审核节点，下一审核人信息）<br/>
 * 4、调用deliver，执行审核程序。对应aio审核界面的保存按扭。<br/><br/>
 * 应用场景四（查报表）：<br/><br/>
 * 1、调用report 取报表数据。<br/><br/>
 * 应用场景五（弹出窗）：<br/><br/>
 * 1、调用popupInfo 取弹出窗的显示列信息。<br/>
 * 2、调用popup 取弹出窗内容。<br/><br/>
 * 
 */
public class AIOApi1 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}

	private UserMgt userMgt = new UserMgt();

	private DynDBManager dbmgt = new DynDBManager();

	private UserFunctionMgt userFunMgt = new UserFunctionMgt();

	private OAMyWorkFlowMgt oamgt = new OAMyWorkFlowMgt();

	private PublicMgt pcmgt = new PublicMgt();
	
	

	
	
	@Override
	protected void service(final HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
		if (GlobalsTool.application == null) {
			GlobalsTool globals = new GlobalsTool();
			ViewContext vc = new ViewContext() {
				public Object getAttribute(String arg0) {
					return null;
				}
				public HttpServletRequest getRequest() {
					return req;
				}
				public HttpServletResponse getResponse() {
					return null;
				}

				public ServletContext getServletContext() {
					return req.getSession().getServletContext();
				}
				public Context getVelocityContext() {
					return null;
				}
				public VelocityEngine getVelocityEngine() {
					return null;
				}
			};
			globals.init(vc);
		}
		//记录本次请求的日志 getParameterMap() getQueryString() getRequestURI()
		// 当系统重启时
		if (SystemState.instance.dogState == SystemState.DOG_RESTART) {
			setMsg(req, rsp, setMsg("ERROR", "系统正在重启，请稍后重试"));
			return;
		}
		// 判断系统是否被锁住
		if (OnlineUserInfo.lockState()) {
			setMsg(req, rsp, setMsg("ERROR", "系统已被锁"));
			return;
		}
		if (SystemState.instance.lastErrorCode != SystemState.NORMAL || (SystemState.instance.dogState != SystemState.DOG_FORMAL && SystemState.instance.dogState != SystemState.DOG_EVALUATE)) {
			setMsg(req, rsp, setMsg("ERROR", "系统状态异常或非法授权"));
			return;
		}

		String charset = req.getHeader("Charset");
		if (charset != null) {
			req.setCharacterEncoding(charset);
		}
		debugRequest(req);
		
		/**
		 * 如果是加密数据过来，说明这个连接是从加密通道过来的。直接转加密方法处理
		 */
		String secData = req.getParameter("secData");
		if(secData != null && secData.length() > 0){
			handSecData(req, rsp);
			return;
		}
		
		
		String userId = req.getParameter("sid");
		String op = req.getParameter("op");
		if (op.equals("sendmsg")) {
			sendMsg(req);
			return;
		}
		
		BaseEnv.log.debug("AIOApi op="+op+"&sessionid="+req.getSession().getId());
		
		if (!"LOGIN".equals(op) && this.getLoginBean(req) == null) {
			setMsg(req, rsp, setMsg("NOLOGIN", "未登陆"));
			return;
		}
		
		if ("LOGIN".equals(op)) { //登录
			login(req, rsp);
		} else if ("deliverTo".equals(op)) { //审核
			deliverTo(req, rsp);
		} else if ("deliverToNext".equals(op)) { //审核到第一个结点
			deliverToNext(req, rsp);
		} else if ("getHome".equals(op)) { //取首页的链接
			getHome(req, rsp);
		}  else if ("getMenu".equals(op)) { //取当前用户菜单
			getMenu(req, rsp);
		} else if ("report".equals(op)) { //取首页的链接
			report(req, rsp);
		} else if ("sync".equals(op)) { //进行同步通讯录操作,返回操作结果页面
			sync(req, rsp);
		} else if ("execDefine".equals(op)) {
			execDefine(req, rsp);
		} else if ("getEnum".equals(op)) {
			getEnum(req, rsp);
		} else if ("add".equals(op)) {
			add(req, rsp);
		} else if ("addPrepare".equals(op)) {
			addPrepare(req, rsp);
		} else if ("detail".equals(op)) {
			detail(req, rsp);
		} else if("popup_m".equals(op)){
			//*****调用移动端弹出窗口******//
			popup_m(req,rsp);			
		} else if ("popup".equals(op)) {
			//*****调用弹出窗数据获取接口*****//			
			popup(req, rsp);
		} else if ("popupInfo".equals(op)) {
			popupInfo(req, rsp);
		} else if ("update".equals(op)) {
			update(req, rsp);
		} else if ("deliverToPrepare".equals(op)) {
			deliverToPrepare(req, rsp);
		} else if ("check".equals(op)) { //对单据先执行一个修改保存动作，目的执行define校验
			check(req, rsp);
		} else if ("hurryTrans".equals(op)) { //对单据先执行一个修改保存动作，目的执行define校验
			hurryTrans(req, rsp);
		} else if ("cancelTo".equals(op)) { //撤回
			cancelTo(req, rsp);
		} else if ("retCheck".equals(op)) { //反审核
			retCheck(req, rsp);
		}  else if ("delete".equals(op)) { //反审核
			delete(req, rsp);
		}  else if ("canAdd".equals(op)) { //是否有添加权限
			canAdd(req, rsp);
		}  else if ("myworkflow".equals(op)) { //查询我的工作流
			myworkflow(req, rsp);
		}  else if("workflowDet".equals(op)){
			myworkflowDet(req,rsp);
		}  else if("cardscan".equals(op)){
			cardscan(req,rsp);
		}  else if("getJsticket".equals(op)){
			getJsticket(req,rsp);
		} else if("getSessEmp".equals(op)) {
			getSessEmp(req, rsp);
		}
	}
	
	
	/**
	 * @throws IOException 
	 * 处理名片扫描请求
	 * @param req
	 * @param rsp
	 * @throws  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void cardscan(HttpServletRequest req, HttpServletResponse rsp) throws IOException{
		DiskFileItemFactory factory = new DiskFileItemFactory();   
        ServletFileUpload upload = new ServletFileUpload(factory);   
        upload.setHeaderEncoding("UTF-8");  
        List<FileItem> items = null;       
        try {
			items = upload.parseRequest(req);
		} catch (Exception e) {
			respMsg(req, rsp, gson.toJson(new Message("error", "表单信息解析失败")));
			return;
		}  
    	String uploadPath = BaseEnv.FILESERVERPATH+"/temp/";
    	String fileFullName =  new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.random()*10000;
    	//Map<String, Object> params = new HashMap<>();
    	Map params = new HashMap();
    	for(FileItem fileItem : items) {
	      if (fileItem.isFormField()) //微信上存的图片   
	        params.put(fileItem.getFieldName(), fileItem.getString("utf-8"));   
	      else//直接上存的图片
	       	params.put("file", fileItem);	
        }
    	// 判断是否上存成功
    	Boolean uploadFileSuccess= false;
    	if(params.get("wxstate") != null && params.get("serverId") != null) { // 去微信拉取图片然后保存
    		fileFullName += "_"+params.get("wxstate") + ".jpeg";
    		BaseResultBean getMediaRet = new Media(params.get("wxstate").toString()).get(params.get("serverId").toString(), uploadPath + fileFullName);
    		if(getMediaRet != null && getMediaRet.getErrcode().equals("0"))
    			uploadFileSuccess = true;
    		else 
    			BaseEnv.log.debug("向微信拉取名片扫面的图片失败:" + gson.toJson(getMediaRet)) ;
    	} 
    	else if(params.get("file") != null) {// 直接保存图片
    		FileItem fileItem = (FileItem)params.get("file");
    		fileFullName += fileItem.getName();
    		try {
    			fileItem.write(new File(uploadPath, fileFullName));
    			uploadFileSuccess = true;
    		}
    		catch(Exception e) {					
    			respMsg(req, rsp, gson.toJson(new Message("error", "图片读取失败")));
    			return;
    		}        	     		
    	}
    	if (!uploadFileSuccess) {
    		respMsg(req, rsp, gson.toJson(new Message("error", "提交的参数有误")));
			return;
    	}
    	
        rsp.setHeader("Content-type", "application/json;charset=UTF-8");
        rsp.setCharacterEncoding("UTF-8");
    	final String eid = getLoginBean(req).getId();
    	try{
    		//使用DES
    		final DESPlus des = new DESPlus(BaseEnv.bol88URL);
    		Map<String, String> data = new HashMap();
    		//data.put("dogid",SystemState.instance.dogId);
    		data.put("dogid","1111-1111-1111-1862");
	    	String str = des.encrypt(gson.toJson(data));
	    	// 查询bol88此客户是否有调用权限
	    	Message msg = AIOConnect.toURL(BaseEnv.bol88URL + "/cardscan?op=query", str);
	    	if("OK".equals(msg.getCode())){
	    		HashMap r = gson.fromJson(des.decrypt(msg.getMsg()), HashMap.class);
	    		if("success".equals(r.get("code"))){
	    			HashMap map = (HashMap)r.get("data");
	    			if((Double)map.get("BALANCE") <= 0.0){
	    				respMsg(req, rsp, gson.toJson(new Message("error", "已达图片识别上限")));
    					return;
	    			}
	    			//获取图片扫描api账户密码并调用api返回名片扫描结果
	    			Message _r = AIOConnect.cardScan((String)map.get("mykey"),(String)map.get("secret"),uploadPath+fileFullName);
	    			final Map card = new HashMap();
	    			if("OK".equals(_r.getCode())){
	    				String cardInf = _r.getMsg();
	        			Map cardMaps = gson.fromJson(cardInf, HashMap.class);    			
	        			//名片识别数据整理
	        			Map ms = (Map)cardMaps.get("message");        			
	        			if((Double)ms.get("status") == 0.0){		
	        				List<HashMap> cardInfDet = (List<HashMap>)cardMaps.get("cardsinfo");
	        				if(cardInfDet.size()>0){
	        					HashMap cardMap = cardInfDet.get(0);
	        					List<HashMap> dets = (List<HashMap>)cardMap.get("items");
	        					for(HashMap item : dets){
	        						card.put(item.get("desc"),item.get("content"));
	        					}
	        					card.put("id",IDGenerater.getId());
	        				} else{
	        					rsp.getWriter().print(new Gson().toJson(new Message("error","图片识别失败")));
	        					return;
	        				}
	        			} else{
	        				rsp.getWriter().print(new Gson().toJson(new Message("error","图片识别失败")));
	        				return;
	        			}
	    			} else{
	    				rsp.getWriter().print(new Gson().toJson(new Message("error",_r.getMsg())));
	    				return;
	    			}
	    			
	    			//创建线程回传扫描结果到bol88
	    			new Thread(new Runnable() {
	    				public void run() {
	    					Map map = new HashMap();
	    					map.put("dogid", SystemState.instance.dogId);
	    					map.put("ComContactor",card.get("姓名"));
	    					map.put("ComContactorMobile",card.get("手机"));
	    					map.put("ComName",card.get("公司"));
	    					map.put("ComTel", card.get("电话"));
	    					map.put("ComEmail", card.get("电子邮箱"));
	    					map.put("ComAddress",card.get("地址"));
	    					map.put("QQ", card.get("QQ"));
	    						    				
	    					try {
								Message msg = AIOConnect.toURL(BaseEnv.bol88URL+"/cardscan?op=add", des.encrypt(gson.toJson(map)));
							} catch (Exception e) {
								
							}
	    				}
	    			}).start();
	    			//end
	    			//保存名片信息
	    			DBUtil.execute(new IfDB() {
	    				public int exec(Session session) {
	    					session.doWork(new Work() {
	    						public void execute(Connection connection) throws SQLException {
	    							try {
	    								Connection conn = connection;
	    								
	    								
	    									String addSQL = " insert into  tblcardscan(id,createBy,createTime,ComContactor,ComContactorMobile,ComName,ComTel,ComEmail,ComAddress,QQ) values(?,?,?,?,?,?,?,?,?,?)";
	    									PreparedStatement pstmt2 = conn.prepareStatement(addSQL);
	    									pstmt2.setString(1, (String)card.get("id"));
	    									pstmt2.setString(2, eid);
	    									pstmt2.setString(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	    									pstmt2.setString(4, (String)card.get("姓名"));
	        								pstmt2.setString(5, (String)card.get("手机"));
	        								pstmt2.setString(6, (String)card.get("公司"));
	        								pstmt2.setString(7, (String)card.get("电话"));
	        								pstmt2.setString(8, (String)card.get("邮箱"));
	        								pstmt2.setString(9, (String)card.get("地址"));
	        								pstmt2.setString(10, (String)card.get("QQ"));
	        								int r = pstmt2.executeUpdate();
	    								
	    							} catch (Exception ex) {
	    								BaseEnv.log.error("名片信息更细失败：", ex);
	    								return ;
	    							}
	    						}
	    					});
	    					return 1;
	    				}
	    			});
	    			//end
	    			//返回结果到前端	    			
	    			Map d = new HashMap();
	    			d.put("id", card.get("id"));
	    			d.put("ComContactor", card.get("姓名"));
	    			d.put("ComContactorMobile", card.get("手机"));
	    			d.put("ComName", card.get("公司"));
	    			d.put("ComTel", card.get("电话"));
	    			d.put("ComEmail", card.get("电子邮箱"));
	    			d.put("ComAddress", card.get("地址"));
	    			d.put("QQ", card.get("QQ"));
	    			rsp.getWriter().print(new Gson().toJson(new Message("success","识别成功",d)));
	    			return;
	    			//end
	    		} else{
	    			rsp.getWriter().print(new Gson().toJson(new Message("error",String.valueOf(r.get("msg")))));
	    			return;
	    		}
	    	} else{    		
				rsp.getWriter().print(new Gson().toJson(new Message("error",msg.getMsg())));
				return;
	    	}  
    	}catch(Exception e){
    		rsp.getWriter().print(new Gson().toJson(new Message("error",e.getMessage())));
			return;
    	}
    	//end
	}
	
	/**
	 * 处理加密数接过来的数据
	 * @param secData
	 */
	private void handSecData(HttpServletRequest req, HttpServletResponse rsp){
		String password = GlobalsTool.getSysSetting("remotePassword");
		if(password ==null || "".equals(password)){
			BaseEnv.log.debug("远程调用，被禁止");
			Message msg = new Message("ERROR","对方系统禁止远程调用");
			setMsg(req, rsp, msg);
			return;
		}
		
		String secData = req.getParameter("secData");
		
		BaseEnv.log.debug("AIOApi接收加密信息:时间"+System.currentTimeMillis());
		String data = secData;
//		String data = AIOConnect.des.Decode(password+AIOConnect.strDefaultKey, secData);
//		if (BaseEnv.log.isDebugEnabled()) {
//			BaseEnv.log.debug("AIOApi接收加密信息解码：时间"+System.currentTimeMillis()+",内容："+data);
//		}
//		//data= data.trim();
//		data = ZipUtil.unzip(data);
//		if (BaseEnv.log.isDebugEnabled()) {
//			BaseEnv.log.debug("AIOApi接收加密信息解压缩：时间"+System.currentTimeMillis()+",内容:"+data);
//		}
		if(!data.startsWith("AB:")){
			BaseEnv.log.debug("加密数据非法，格式不正确，可能密码不一至");
			Message msg = new Message("ERROR","加密数据不能正确解码，可能密码不一至");
			setMsg(req, rsp, msg);
			return;
		}
		data = data.substring(3); 
		HashMap map = gson.fromJson(data, HashMap.class);
		
		Object time = map.get("time");
		if(time==null||time.equals("")){
			BaseEnv.log.debug("加密数据非法，不带时间撮信息");
			Message msg = new Message("ERROR","数据非法");
			setMsg(req, rsp, msg);
			return;
		}
		if(System.currentTimeMillis()-Long.parseLong(time+"") > 10000){
			BaseEnv.log.debug("加密数据非法，时间超过10秒，可能需要同步时钟"+System.currentTimeMillis());
			Message msg = new Message("ERROR","数据失效，如确定本数据是合法的，请同步您服务器时钟");
			setMsg(req, rsp, msg);
			return;
		}
		String type=map.get("op")+"";
		if("deliverToNext".equals(type)){
			Message msg = AIOConnect.deliverToNext(map.get("locale")+"", new LoginBean(map.get("userId")+"",""), map.get("keyId")+"", map.get("tableName")+"");
			setMsg(req, rsp, msg);
			return;
		}else if("execDefine".equals(type)){
			String defineName = map.get("defineName")+"";
			String buttonTypeName = map.get("buttonTypeName")+"";
			String[] keyIds = new String[]{ map.get("keyId")+""};
			String tableName = map.get("tableName")+"";
			String defineInfo = map.get("defineInfo")+"";
			String locale = map.get("locale")+"";
			LoginBean loginBean = new LoginBean(map.get("userId")+"","");
			Message msg = AIOConnect.execDefine(locale, loginBean, defineName, buttonTypeName, keyIds, tableName, defineInfo);
			if(msg.getCode().equals("OK")){
				String msgStr = gson.toJson(msg.getObj());
				respMsg(req, rsp, msgStr);
				return;
			}else{
				setMsg(req, rsp, msg);
				return;
			}
		}else if("add".equals(type)){
			String locale = map.get("locale")+"";
			LoginBean loginBean = new LoginBean(map.get("userId")+"","");

			String tableName = map.get("tableName")+"";
			/*父类的classCode*/
			String parentCode = map.get("parentCode")+"";
			//要执行的define的信息
			String defineInfo = map.get("defineInfo")+"";
			
			String saveType = map.get("saveType")+""; //保存类型 saveDraft 草稿
			
			String valuesStr = map.get("values")+"";
			
			String deliverTo = map.get("deliverTo")+"";
			Message msg = AIOConnect.add(locale, loginBean, tableName, parentCode, defineInfo, saveType, valuesStr, deliverTo);
			this.setMsg(req, rsp, msg);
			return ;
		}else if("update".equals(type)){
			String locale = map.get("locale")+"";
			LoginBean loginBean = new LoginBean(map.get("userId")+"","");

			String tableName = map.get("tableName")+"";
			/*父类的classCode*/
			String parentCode = map.get("parentCode")+"";
			//要执行的define的信息
			String defineInfo = map.get("defineInfo")+"";
			
			String saveType = map.get("saveType")+""; //保存类型 saveDraft 草稿
			
			String valuesStr = map.get("values")+"";
			
			String deliverTo = map.get("deliverTo")+"";
			
			Message msg = AIOConnect.update(locale, loginBean, tableName, saveType, defineInfo, valuesStr);
			this.setMsg(req, rsp, msg);
			return;
		}else{
			Message msg = new Message("ERROR","没有op操作类型信息"+type);
			this.setMsg(req, rsp, msg);
			return;
		}
		
	}
	
	
	
	
	/**
	 * 我的工作流--查询所有和当前登陆人相关的所有工作流，
	 * 
	 * @param approveStatus  transing:待审；consignation:委托我的，transing2：办理中；finish：已办结
	 * @param keyWord 关键字
	 * @param pageNo 页号
	 * <br/><br/>
	 * 返回值: {code:OK,obj:{totalPage:总页数,list：工作流列表[]}}
	 */
    public void myworkflow(HttpServletRequest request, HttpServletResponse rsp) {
    	String approveStatus = request.getParameter("approveStatus");
    	String keyWord = request.getParameter("keyWord");
    	String pageNo = request.getParameter("pageNo");
    	String tempClass = request.getParameter("tempClass");
    	String tempFile = request.getParameter("tempFile");
    	
    	if(pageNo == null || pageNo.length()==0){
    		pageNo = "1";
    	}
    	OAMyWorkFlowForm workFlowForm = new OAMyWorkFlowForm();
    	workFlowForm.setApproveStatus(approveStatus==null ||approveStatus.length()==0?"transing":approveStatus);
		workFlowForm.setKeyWord(keyWord);
		workFlowForm.setPageSize(10);
		workFlowForm.setPageNo(Integer.parseInt(pageNo));
		workFlowForm.setClassCode(tempClass);
		workFlowForm.setTemplateFile(tempFile);
		Result rs = oamgt.query(workFlowForm, getLoginBean(request).getId(),
				workFlowForm.getApproveStatus(), this.getLocale(request).toString(),
				"transing".equals(workFlowForm.getApproveStatus()) ? true : false);
		HashMap ret = new HashMap();
		ret.put("totalPage", rs.getTotalPage());
		ret.put("list", rs.retVal);
		Message msg = new Message("OK","查询成功", ret);
		setMsg(request, rsp, msg);
    }
    
    /**
	 * 工作流详情
	 * 返回值: {code:OK,obj:{totalPage:总页数,list：工作流列表[]}}
	 */
    public void myworkflowDet(HttpServletRequest req, HttpServletResponse rsp) {
    	String keyId = req.getParameter("keyId");
		String tableName = req.getParameter("tableName");
				
		Map ret = new HashMap();
		
		
		DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(tableName);
		
		LoginBean loginBean = this.getLoginBean(req);
	
		DetailBean bean = AIOConnect.detail(GlobalsTool.getLocale(req).toString(), loginBean,keyId, tableName);
		
		if (bean.getCode() == ErrorCanst.DEFAULT_SUCCESS) {
			List<OAMyWorkFlowDetBean> flows = bean.getFlowDepict();			
			
			//转化fieldList
			bean.setFieldList(toFieldList(bean.getFieldList(), GlobalsTool.getLocale(req).toString()));
			for(Object tn : bean.getChildShowField().keySet().toArray()){
				ArrayList list  = (ArrayList)bean.getChildShowField().get(tn);
				bean.getChildShowField().put(tn, toFieldList(list, GlobalsTool.getLocale(req).toString()));
			}
			
			/*
			OAMobileDetBean detBean = new OAMobileDetBean();
			detBean.setValues(bean.getValues());
			detBean.setChildTableList(bean.getChildTableList());
			detBean.setFlowDepict(bean.getFlowDepict());
			detBean.setFieldList(bean.getFieldList());
			detBean.setChildShowField(bean.getChildShowField());
			detBean.setCheckRight(bean.isCheckRight());
			detBean.setRetCheckRight(bean.isRetCheckRight());
			detBean.setHurryTrans(bean.isHurryTrans());
			detBean.setHasCancel(bean.isHasCancel());
			detBean.setModuleName(tableInfo.getDisplay().get(GlobalsTool.getLocale(req).toString()));
			*/	
				
			Message msg = new Message("OK","", bean);			
			setMsg(req, rsp, msg);			
			return;
		} else {
			Message msg = new Message("ERROR","审核流信息读取失败");
			setMsg(req, rsp, msg);
			return;		
		}
			    
    }
    
    /**
	 * 判断当前登陆人是否有添加某模块的权限
	 * 
	 * @param tableName  表名
	 * @param parentTableName 父表名
	 * @param moduleType 模块类型
	 * <br/><br/>
	 * 返回值: {code:OK,msg:'true:有添加权限；false：无添加权限'}
	 */
    public void canAdd(HttpServletRequest request, HttpServletResponse rsp) {
		String tableName=request.getParameter("tableName");
		String parentTableName=request.getParameter("parentTableName");
		String moduleType=request.getParameter("moduleType");
		LoginBean loginBean  = this.getLoginBean(request);
		MOperation mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
		
		Message msg = new Message("OK",mop.add()+"");
		setMsg(request, rsp, msg);
    }
	
    /**
	 * 工作流催办
	 * 
	 * @param tableName  表名
	 * @param keyId 单据ID
	 * <br/><br/>
	 * 返回值: {code:OK,msg:摧办成功}
	 */
    public void hurryTrans(HttpServletRequest request, HttpServletResponse rsp) {
    	String keyId=request.getParameter("keyId");
		String tableName=request.getParameter("tableName");
		String content=request.getParameter("content");
		String locale = getLocale(request).toString();
		LoginBean loginBean=getLoginBean(request);
		String wakeType=request.getParameter("wakeType");
		Message msg = AIOConnect.hurryTrans(locale, loginBean, keyId, tableName, content, wakeType);
		setMsg(request, rsp, msg);
    }
	
    /**
	 * 取自定义弹出窗数据，自定义弹出窗由AIO设计，接口调用数据与PC端数据一致
	 * 
	 * @param tableName  表名
	 * @param fieldName 字段名
	 * @param selectName 弹出窗名（可以不指定，如果在指定表名和字段名的情况下，会自动取对应表和字段的弹出窗，如果没有指定表和字段名可以直接指定本参数的弹出窗名）
	 * <br/><br/>
	 * 返回值: {code:OK,obj:{result:[弹出窗数据，具体字段名和数据由所配弹出窗决定]}}
	 */
    public void popup_m(HttpServletRequest req,HttpServletResponse rsp){
    	//表名
    			String tableName = req.getParameter("tableName");
    			//字段名
    			String fieldName = req.getParameter("fieldName");
    			//弹出窗名
    			String selectName = req.getParameter("selectName");

    			//当前页最后一行关键KeyField
    			String keyField = req.getParameter("keyF");
    			
    			//当前页最后一行关键KeyValue
    			String keyValue = req.getParameter("keyV");
    			
    			LoginBean loginBean = this.getLoginBean(req);

    			Map<String, Object> data = new HashMap<String, Object>();
    			//用来返回的数据
    			ArrayList<HashMap> rows = new ArrayList<HashMap>(); // 查结果集

    			Hashtable<String, DBTableInfoBean> allTables = BaseEnv.tableInfos;
    			Locale locale = GlobalsTool.getLocale(req);
    			//弹出窗对应的Bean
    			PopupSelectBean popSelectBean = null;
    			if (selectName != null && selectName.length() > 0) {
    				popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
    			} else {
    				// 如果页面传入的是字段名，则根据字段名查找出相应的弹出选择框
    				popSelectBean = DDLOperation.getFieldInfo(allTables, tableName, fieldName).getSelectBean();
    			}
    			if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
    				//这是一个有主弹出窗的弹窗
    				popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
    			}

    			//查询关键字
    			String keyword = req.getParameter("keyword");
    			keyword = keyword == null ? "" : keyword.trim();
    			if (req.getMethod().equals("GET"))
    				keyword = GlobalsTool.toChinseChar(keyword);
    			keyword = GlobalsTool.encodeTextCode(keyword);
    			if (keyword.endsWith(","))
    				keyword = keyword.substring(0, keyword.length() - 1);

    			boolean hasTopPopup = false;
    			if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
    				//这是一个有主弹出窗的弹窗
    				popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
    				hasTopPopup = true;
    			}

    			String sunCompanyID = loginBean.getSunCmpClassCode();

    			//数组顺序为 0 表名.字段名 1 值 2 对比(= like >= <=)
    			ArrayList<String[]> param = new ArrayList<String[]>();
    			//参加条件的字段
    			ArrayList<PopField> cfields = new ArrayList<PopField>();

    			//先处理弹出框包含关键字搜索的情况
    			if (popSelectBean.isKeySearch()) {
    				for (PopField popField : popSelectBean.getDisplayField2()) {
    					if (popField.keySearch)
    						cfields.add(popField);
    				}
    			} else {
    				for (PopField popField : popSelectBean.getDisplayField2()) {
    					if (popField.getSearchType() != 0 && popField.getSearchType() != PopField.SEARCH_SCOPE) {
    						//没有keyword的情况下，要模糊匹配所有条件字段，但是要除去数据型和范围型
    						DBFieldInfoBean dfb = GlobalsTool.getFieldBean(popField.getFieldName());
    						if (dfb == null || (dfb.getFieldType() != DBFieldInfoBean.FIELD_INT && dfb.getFieldType() != DBFieldInfoBean.FIELD_DOUBLE))
    							cfields.add(popField);
    					}
    				}
    			}

    			//如果没有keysearch则按displayField条件进行过滤
    			param = DynAjaxSelect.getCondition(keyword, allTables, cfields);
    			keyword = "";

    			ArrayList<String> tabParam = popSelectBean.getTableParams();
    			//获取产生弹出窗界面上的各个输入框的值
    			HashMap<String, String> mainParam = new HashMap<String, String>();
    			String mainField = "";
    			String value = "";
    			for (int i = 0; i < tabParam.size(); i++) {
    				mainField = tabParam.get(i).toString();
    				if (mainField.indexOf("@TABLENAME") >= 0) {
    					mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
    				}
    				value = req.getParameter(mainField) == null ? "" : req.getParameter(mainField);
    				mainParam.put(mainField, value);
    				try {
    					mainParam.put(mainField, java.net.URLDecoder.decode(value, "UTF-8"));
    				} catch (UnsupportedEncodingException e) {
    					e.printStackTrace();
    				}
    			}

    			// 根据模块代号取得查询时的范围权限

    			// 判断模块代号是否存在，如不存在则提示无来源模块
    			String moduleId = req.getParameter("MOID");
    			MOperation mop;
    			if (moduleId != null && moduleId.length() > 0) {
    				mop = (MOperation) (loginBean.getOperationMapKeyId().get(moduleId));
    			} else {
    				String parentTableName = null;
    				if(null != tableName && !tableName.equals("")){
    					DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
    					parentTableName = tableInfo.getPerantTableName();
    				}			
    				
    				if (parentTableName != null && parentTableName.indexOf(";") > 0) {
    					parentTableName = parentTableName.substring(0, parentTableName.indexOf(";"));
    				}
    				String moduleType = req.getParameter("moduleType");
    				mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
    			}
    			ArrayList<LoginScopeBean> scopeRight = new ArrayList<LoginScopeBean>();
    			if (mop != null) {
    				scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
    				DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
    				if (tableInfo != null && tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
    					// 如果是兄弟表,主表的查询范围权限加上明细表的范围权限
    					MOperation mopDet = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?parentTableName=" + "&tableName=" + tableName);
    					if (mopDet != null) {
    						scopeRight.addAll(mopDet.queryScope);
    					}
    				}
    			}
    			// 加入所有分类权限
    			scopeRight.addAll(mop.classScope);
    			// 加入公共权限
    			ArrayList<LoginScopeBean> allScopeList = loginBean.getAllScopeRight();
    			scopeRight.addAll(allScopeList);

    			DynDBManager dyn = new DynDBManager();
    			popSelectBean.setPopEnter(true);
    			int tmpPage = 1;
    			int pageSize = 9999;
    			if (req.getParameter("pageNo") != null) {
    				try {
    					tmpPage = Integer.parseInt(req.getParameter("pageNo"));
    				} catch (NumberFormatException e) {
    					e.printStackTrace();
    				}
    			}
    			if (req.getParameter("pageSize") != null) {
    				try {
    					pageSize = Integer.parseInt(req.getParameter("pageSize"));
    				} catch (NumberFormatException e) {
    					e.printStackTrace();
    				}
    			}
    			//查询数据库，并返回前9999条数据    			
    			/*
    			Map ret = dyn.popSelect_m(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, pageSize, "", "", loginBean.getId(), sunCompanyID, locale, keyword, req, "",
    					popSelectBean.isSaveParentFlag() ? 0 : PopupSelectBean.LEAFRULE, "");
    			*/
    			
    			Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, pageSize, "", "", loginBean.getId(), sunCompanyID, locale, keyword, req, "",
    					popSelectBean.isSaveParentFlag() ? 0 : PopupSelectBean.LEAFRULE, "");
				/*
				Result rs = null;
    			if("OK".equals(ret.get("code"))){
    				rs = (Result)ret.get("data");
    			} else{
    				Message msg = new Message("Error","-1");
        			setMsg(req, rsp, msg);
        			return;
    			}
    			*/
    			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
    				for (Object[] os : (List<Object[]>) rs.retVal) {
    					HashMap one = new HashMap();

    					for (int k = 0; k < os.length; k++) {
    						if(k>=popSelectBean.getAllFields().size()){
    							one.put("childCount", os[k]);
    							continue;
    						}
    						PopField fv = popSelectBean.getAllFields().get(k);
    						String osstr = os[k] + "";

    						String display = fv.fieldName;
    						DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
    						if (tempfib == null) {
    							display = fv.parentName;

    							if (display == null || display.length() == 0) {
    								display = fv.getDisplay();
    							}
    							if (display == null || display.length() == 0) {
    								BaseEnv.log.info("--------------弹出窗调试信息：弹出窗" + popSelectBean.getName() + "返回字段'" + fv.getFieldName() + "'在表构中不存，且未指定parentName或display");
    							}
    							if (tableName != null) {
    								display = display.replace("@TABLENAME", tableName);
    							}
    							tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
    						}
    						if (tempfib != null && tempfib.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
    							osstr = GlobalsTool.getEnumerationItemsDisplay(tempfib.getRefEnumerationName(), value, locale.toString());
    						} else if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
    							if (osstr.equals("null") || osstr.equals("")) {
    								osstr = "0";
    							}
    							osstr = GlobalsTool.newFormatNumber(new Double(osstr), false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), popSelectBean.getTableName(fv.fieldName),
    									popSelectBean.getFieldName(fv.fieldName), true);
    						}
    						osstr = GlobalsTool.revertTextCode2(osstr);

    						String fvn = "";
    						if (fv.type == 1) {
    							fvn = fv.parentName;
    							if (fvn == null || fvn.length() == 0) {
    								fvn = fv.fieldName;
    							}
    						} else {
    							fvn = fv.asName;
    							if (fvn == null || fvn.length() == 0) {
    								fvn = fv.fieldName;
    							}
    							//如果是个复合字段，则采用display
    							if (!fvn.matches("[\\w\\.]*")) {
    								fvn = fv.getDisplay();
    								if (fvn == null || fvn.length() == 0) {
    									Message msg = new Message("ERROR", "字段" + fv.getFieldName() + "是复合字段必须配置asName或display");
    									setMsg(req, rsp, msg);
    									return;
    								}
    							}
    						}
    						fvn = fvn.replace("@TABLENAME.", "").replace('.', '_');

    						one.put(fvn, osstr);
    					}
    					rows.add(one);
    				}
    			}
    			data.put("result", rows);

    			Message msg = new Message("OK","", data);
    			setMsg(req, rsp, msg);
    }

    /**
	 * 取自定义弹出窗数据，自定义弹出窗由AIO设计，接口调用数据与PC端数据一致
	 * 
	 * @param tableName  表名
	 * @param fieldName 字段名
	 * @param selectName 弹出窗名（可以不指定，如果在指定表名和字段名的情况下，会自动取对应表和字段的弹出窗，如果没有指定表和字段名可以直接指定本参数的弹出窗名）
	 * <br/><br/>
	 * 返回值: {code:OK,obj:{result:[弹出窗数据，具体字段名和数据由所配弹出窗决定]}}
	 */
	public void popup(HttpServletRequest req, HttpServletResponse rsp) {
		//表名
		String tableName = req.getParameter("tableName");
		//字段名
		String fieldName = req.getParameter("fieldName");
		//弹出窗名
		String selectName = req.getParameter("selectName");

		LoginBean loginBean = this.getLoginBean(req);

		Map<String, Object> data = new HashMap<String, Object>();
		//用来返回的数据
		ArrayList<HashMap> rows = new ArrayList<HashMap>(); // 查结果集

		Hashtable<String, DBTableInfoBean> allTables = BaseEnv.tableInfos;
		Locale locale = GlobalsTool.getLocale(req);
		//弹出窗对应的Bean
		PopupSelectBean popSelectBean = null;
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// 如果页面传入的是字段名，则根据字段名查找出相应的弹出选择框
			popSelectBean = DDLOperation.getFieldInfo(allTables, tableName, fieldName).getSelectBean();
		}
		if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
			//这是一个有主弹出窗的弹窗
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
		}

		//查询关键字
		String keyword = req.getParameter("keyword");
		keyword = keyword == null ? "" : keyword.trim();
		if (req.getMethod().equals("GET"))
			keyword = GlobalsTool.toChinseChar(keyword);
		keyword = GlobalsTool.encodeTextCode(keyword);
		if (keyword.endsWith(","))
			keyword = keyword.substring(0, keyword.length() - 1);

		boolean hasTopPopup = false;
		if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
			//这是一个有主弹出窗的弹窗
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
			hasTopPopup = true;
		}

		String sunCompanyID = loginBean.getSunCmpClassCode();

		//数组顺序为 0 表名.字段名 1 值 2 对比(= like >= <=)
		ArrayList<String[]> param = new ArrayList<String[]>();
		//参加条件的字段
		ArrayList<PopField> cfields = new ArrayList<PopField>();

		//先处理弹出框包含关键字搜索的情况
		if (popSelectBean.isKeySearch()) {
			for (PopField popField : popSelectBean.getDisplayField2()) {
				if (popField.keySearch)
					cfields.add(popField);
			}
		} else {
			for (PopField popField : popSelectBean.getDisplayField2()) {
				if (popField.getSearchType() != 0 && popField.getSearchType() != PopField.SEARCH_SCOPE) {
					//没有keyword的情况下，要模糊匹配所有条件字段，但是要除去数据型和范围型
					DBFieldInfoBean dfb = GlobalsTool.getFieldBean(popField.getFieldName());
					if (dfb == null || (dfb.getFieldType() != DBFieldInfoBean.FIELD_INT && dfb.getFieldType() != DBFieldInfoBean.FIELD_DOUBLE))
						cfields.add(popField);
				}
			}
		}

		//如果没有keysearch则按displayField条件进行过滤
		param = DynAjaxSelect.getCondition(keyword, allTables, cfields);
		keyword = "";

		ArrayList<String> tabParam = popSelectBean.getTableParams();
		//获取产生弹出窗界面上的各个输入框的值
		HashMap<String, String> mainParam = new HashMap<String, String>();
		String mainField = "";
		String value = "";
		for (int i = 0; i < tabParam.size(); i++) {
			mainField = tabParam.get(i).toString();
			if (mainField.indexOf("@TABLENAME") >= 0) {
				mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
			}
			value = req.getParameter(mainField) == null ? "" : req.getParameter(mainField);
			mainParam.put(mainField, value);
			try {
				mainParam.put(mainField, java.net.URLDecoder.decode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		// 根据模块代号取得查询时的范围权限

		// 判断模块代号是否存在，如不存在则提示无来源模块
		String moduleId = req.getParameter("MOID");
		MOperation mop;
		if (moduleId != null && moduleId.length() > 0) {
			mop = (MOperation) (loginBean.getOperationMapKeyId().get(moduleId));
		} else {
			String parentTableName = null;
			if(null != tableName && !tableName.equals("")){
				DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
				parentTableName = tableInfo.getPerantTableName();
			}			
			
			if (parentTableName != null && parentTableName.indexOf(";") > 0) {
				parentTableName = parentTableName.substring(0, parentTableName.indexOf(";"));
			}
			String moduleType = req.getParameter("moduleType");
			mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
		}
		ArrayList<LoginScopeBean> scopeRight = new ArrayList<LoginScopeBean>();
		if (mop != null) {
			scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
			if (tableInfo != null && tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
				// 如果是兄弟表,主表的查询范围权限加上明细表的范围权限
				MOperation mopDet = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?parentTableName=" + "&tableName=" + tableName);
				if (mopDet != null) {
					scopeRight.addAll(mopDet.queryScope);
				}
			}
		}
		// 加入所有分类权限
		scopeRight.addAll(mop.classScope);
		// 加入公共权限
		ArrayList<LoginScopeBean> allScopeList = loginBean.getAllScopeRight();
		scopeRight.addAll(allScopeList);

		DynDBManager dyn = new DynDBManager();
		popSelectBean.setPopEnter(true);
		int tmpPage = 1;
		int pageSize = 9999;
		if (req.getParameter("pageNo") != null) {
			try {
				tmpPage = Integer.parseInt(req.getParameter("pageNo"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (req.getParameter("pageSize") != null) {
			try {
				pageSize = Integer.parseInt(req.getParameter("pageSize"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		//查询数据库，并返回前9999条数据
		Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, pageSize, "", "", loginBean.getId(), sunCompanyID, locale, keyword, req, "",
				popSelectBean.isSaveParentFlag() ? 0 : PopupSelectBean.LEAFRULE, "");

		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			for (Object[] os : (List<Object[]>) rs.retVal) {
				HashMap one = new HashMap();

				for (int k = 0; k < os.length; k++) {
					if(k>=popSelectBean.getAllFields().size()){
						one.put("childCount", os[k]);
						continue;
					}
					PopField fv = popSelectBean.getAllFields().get(k);
					String osstr = os[k] + "";

					String display = fv.fieldName;
					DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					if (tempfib == null) {
						display = fv.parentName;

						if (display == null || display.length() == 0) {
							display = fv.getDisplay();
						}
						if (display == null || display.length() == 0) {
							BaseEnv.log.info("--------------弹出窗调试信息：弹出窗" + popSelectBean.getName() + "返回字段'" + fv.getFieldName() + "'在表构中不存，且未指定parentName或display");
						}
						if (tableName != null) {
							display = display.replace("@TABLENAME", tableName);
						}
						tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					}
					if (tempfib != null && tempfib.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
						osstr = GlobalsTool.getEnumerationItemsDisplay(tempfib.getRefEnumerationName(), value, locale.toString());
					} else if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						if (osstr.equals("null") || osstr.equals("")) {
							osstr = "0";
						}
						osstr = GlobalsTool.newFormatNumber(new Double(osstr), false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), popSelectBean.getTableName(fv.fieldName),
								popSelectBean.getFieldName(fv.fieldName), true);
					}
					osstr = GlobalsTool.revertTextCode2(osstr);

					String fvn = "";
					if (fv.type == 1) {
						fvn = fv.parentName;
						if (fvn == null || fvn.length() == 0) {
							fvn = fv.fieldName;
						}
					} else {
						fvn = fv.asName;
						if (fvn == null || fvn.length() == 0) {
							fvn = fv.fieldName;
						}
						//如果是个复合字段，则采用display
						if (!fvn.matches("[\\w\\.]*")) {
							fvn = fv.getDisplay();
							if (fvn == null || fvn.length() == 0) {
								Message msg = new Message("ERROR", "字段" + fv.getFieldName() + "是复合字段必须配置asName或display");
								setMsg(req, rsp, msg);
								return;
							}
						}
					}
					fvn = fvn.replace("@TABLENAME.", "").replace('.', '_');

					one.put(fvn, osstr);
				}
				rows.add(one);
			}
		}
		data.put("result", rows);

		Message msg = new Message("OK","", data);
		setMsg(req, rsp, msg);

	}

	/**
	 * 取自定义弹出窗显示列的字段信息，
	 * 
	 * @param tableName  表名
	 * @param fieldName 字段名
	 * @param selectName 弹出窗名（可以不指定，如果在指定表名和字段名的情况下，会自动取对应表和字段的弹出窗，如果没有指定表和字段名可以直接指定本参数的弹出窗名）
	 * <br/><br/>
	 * 返回值: {code:OK,obj:{showfields:显示字段名中文名,tabParam：表参数}}
	 */
	public void popupInfo(HttpServletRequest req, HttpServletResponse rsp) {
		//表名
		String tableName = req.getParameter("tableName");
		//字段名
		String fieldName = req.getParameter("fieldName");
		//弹出窗名
		String selectName = req.getParameter("selectName");

		LoginBean loginBean = this.getLoginBean(req);

		Map<String, Object> data = new HashMap<String, Object>();
		//用来返回的数据
		ArrayList<HashMap> rows = new ArrayList<HashMap>(); // 查结果集

		Hashtable<String, DBTableInfoBean> allTables = BaseEnv.tableInfos;
		Locale locale = GlobalsTool.getLocale(req);
		//弹出窗对应的Bean
		PopupSelectBean popSelectBean = null;
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// 如果页面传入的是字段名，则根据字段名查找出相应的弹出选择框
			popSelectBean = DDLOperation.getFieldInfo(allTables, tableName, fieldName).getSelectBean();
		}
		if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
			//这是一个有主弹出窗的弹窗
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
		}
		Map<String, String> map = new HashMap<String, String>();
		//以下为显示字段
		for (PopField field : popSelectBean.getDisplayField()) {
			//显示字段不包括图片
			if (13 == field.fieldType) {
				continue;
			}

			String display = field.fieldName;
			DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
			if (tempfib == null) {
				display = field.parentName;

				if (display == null || display.length() == 0) {
					display = field.getDisplay();
				}
				if (display == null || display.length() == 0) {
					BaseEnv.log.info("--------------弹出窗调试信息：弹出窗" + popSelectBean.getName() + "返回字段'" + field.getFieldName() + "'在表构中不存，且未指定parentName或display");
				}
				if (tableName != null) {
					display = display.replace("@TABLENAME", tableName);
				}
			}

			map.put(display.replace(".", "_"), field.getDisplayLocale(tableName, allTables, locale));
		}
		data.put("showfields", map);

		ArrayList<String> tabParam = popSelectBean.getTableParams();
		ArrayList<String> tabParamList = new ArrayList<String>();
		String mainField = "";
		String value = "";
		for (int i = 0; i < tabParam.size(); i++) {
			mainField = tabParam.get(i).toString();
			if (mainField.indexOf("@TABLENAME") >= 0) {
				mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
			}
			tabParamList.add(mainField);
		}
		data.put("tabParam", tabParamList);

		Message msg = new Message("OK","", data);
		setMsg(req, rsp, msg);

	}

	/**
	 * 修改自定义单据，
	 * 
	 * @param tableName  表名
	 * @param parentTableName 父表名
	 * @param fieldName 字段名
	 * @param saveType saveDraft:存草稿;空:正式单据
	 * @param defineInfo 如自定义中有弹出可选确认框，会自动带出一个字定义标识代码，按原文传入。默认为空
	 * @param values  存储所有单据字段及其值的HashMap<fieldName,value>
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void update(HttpServletRequest request, HttpServletResponse rsp) {
		String tableName = request.getParameter("tableName");
		String parentTableName = request.getParameter("parentTableName");
		
		String saveType = request.getParameter("saveType"); //保存类型 saveDraft 草稿
		String defineInfo = request.getParameter("defineInfo"); //记录可选defineInfo信息
		String valuesStr = request.getParameter("values");

		//设置兄弟表的ID
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		
		Message msg = AIOConnect.update(locale, loginBean, tableName, saveType, defineInfo, valuesStr);
		this.setMsg(request, rsp, msg);
		return;
	}

	/**
	 * 校验单据的会计期间
	 * @param tableName
	 * @param keyId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean vildatePeriod(String tableName, String keyId, HttpServletRequest request) throws Exception {
		DBTableInfoBean tableInfo = (DBTableInfoBean) BaseEnv.tableInfos.get(tableName);
		String sysParamter = tableInfo.getSysParameter();// 表信息系统参数
		Date time = null;
		String timeStr = "";
		String workFlowNodeName = "";
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean bean = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (bean.getDefaultValue() != null && "accendnotstart".equals(bean.getFieldIdentityStr())) {
				AIODBManager aioMgt = new AIODBManager();
				Result rs = aioMgt.sqlList("select " + bean.getFieldName() + ",workFlowNodeName from " + tableName + " where id='" + keyId + "'", new ArrayList());
				if (((ArrayList) rs.retVal).size() > 0) {
					timeStr = ((Object[]) ((ArrayList) rs.retVal).get(0))[0].toString();
					workFlowNodeName = ((Object[]) ((ArrayList) rs.retVal).get(0))[1].toString();
				}
			}
		}
		if (timeStr.length() > 0) {
			time = BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
			int currentMonth = 0;
			int currentYear = 0;
			if (null != time) {
				currentMonth = time.getMonth() + 1;
				currentYear = time.getYear() + 1900;
			}
			int periodMonth = -1;
			int periodYear = -1;
			AccPeriodBean accBean = (AccPeriodBean) BaseEnv.accPerios.get(this.getLoginBean(request).getSunCmpClassCode());
			if (accBean != null) {
				periodMonth = accBean.getAccMonth();
				periodYear = accBean.getAccYear();

			}
			if ((currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0) && currentMonth != 0 && !"draft".equals(workFlowNodeName)) {
				if (null != sysParamter) {
					boolean flag = false;
					if (sysParamter.equals("CurrentAccBefBill") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
						flag = true;
					}

					if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)) {
						flag = true;
					}
					if (flag) {
						return true;
					}

				}
			}
		}
		return false;
	}


	/**
	 * 取自定义单据详细内容，
	 * 
	 * @param tableName  表名
	 * @param keyId 单据ID
	 * <br/><br/>
	 * 返回值: {code:OK,obj:DetailBean对象，见DetailBean接口说明}
	 * @see DetailBean
	 */
	public void detail(HttpServletRequest req, HttpServletResponse rsp) {
		String keyId = req.getParameter("keyId");
		String tableName = req.getParameter("tableName");
		LoginBean loginBean = this.getLoginBean(req);
		
		String keyName = req.getParameter("keyName"); //查询详情不一定是用ID,如果用其它字段查询详情，这里指定字段名称
        if(keyName != null && !keyName.equals("")){
        	Result rk = userFunMgt.getDataId(tableName,keyName,keyId);
        	if(rk.retCode== ErrorCanst.DEFAULT_SUCCESS && rk.retVal!= null&&((ArrayList)rk.retVal).size()>0){
        		keyId = (String)(((Object[])(((ArrayList)rk.retVal).get(0)))[0]);
        	}else{
        		Message msg = new Message("ERROR", "数据不存在");
    			setMsg(req, rsp, msg);
    			return;
        	}
        }
		
		//*****加载列配置信息*****//
        ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
        // 加载主表中自定列配置
        ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;
        Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;
        Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
												req.getSession().getServletContext().getAttribute("userSettingColConfig") ;
        if(userColConfig!=null && userColConfig.size()>0){
        	configList = userColConfig.get(tableName+"bill") ;
        	if(configList!=null){
        		allConfigList.addAll(configList) ;
        	}
        }
        
        ArrayList childTableList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
		//加载明细中自定义列的显示
		for(int i=0;i<childTableList.size();i++){
			DBTableInfoBean childTableInfo=(DBTableInfoBean)childTableList.get(i);
		    if(userColConfig!=null && userColConfig.size()>0){
		    	ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName()+"bill") ;
		    	if(childConfigList!=null){
		    		//如果做了模块字段设置，那么列配置字段中将不包含这些字段，页面将不再做处理
		        	allConfigList.addAll(childConfigList) ;
		    	}
		    }
		}
        //********end********//
        
		DetailBean bean = AIOConnect.detail(GlobalsTool.getLocale(req).toString(), loginBean, keyId, tableName);
		if (bean.getCode() == ErrorCanst.DEFAULT_SUCCESS) {
			//针对客户资料关系表字段做特殊处理
			String serialStr = "";
			if("CRMClientInfo".equals(tableName)){
				ClientSetingMgt moduleMgt = new ClientSetingMgt() ;
				String moduleId = (String)bean.getValues().get("ModuleId");
				Result rs = moduleMgt.detailCrmModule(moduleId);
				ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
				serialStr = moduleBean.getTableInfo().split(":")[0].substring(tableName.length());
				//if(serialStr.length()>0){
					ArrayList<DBFieldInfoBean> mainFields =  bean.getFieldList();
					for(int i = 0 ;i<= mainFields.size()-1;i++){
						DBFieldInfoBean _i = mainFields.get(i);
						if(_i.getRefEnumerationName() == null){
							continue;
						}
						if(_i.getFieldName().startsWith("extend") || _i.getFieldName().startsWith("extent")){
							_i.setRefEnumerationName(_i.getFieldName());
						} else{
							_i.setRefEnumerationName(_i.getRefEnumerationName().replaceAll("[0-9]", ""));
						}						
						if(_i.getInputType() == 1 && !_i.getRefEnumerationName().contains(serialStr)){						
							_i.setRefEnumerationName(_i.getRefEnumerationName()+serialStr);
						}
						
					}					
				//}
			}
			
			//转化fieldList
			bean.setFieldList(toFieldList(bean.getFieldList(), GlobalsTool.getLocale(req).toString(),allConfigList,bean.getValues()));						
			
			for(Object tn : bean.getChildShowField().keySet().toArray()){				
				ArrayList list  = (ArrayList)bean.getChildShowField().get(tn);
				
				//针对客户资料关系表字段做特殊处理
				if("CRMClientInfo".equals(tableName)){
					ArrayList<DBFieldInfoBean> childFields = list;
					for(DBFieldInfoBean _subItem : childFields){
						if(_subItem.getRefEnumerationName() == null){
							continue;
						}
						if(_subItem.getFieldName().startsWith("extend") || _subItem.getFieldName().startsWith("extent")){
							_subItem.setRefEnumerationName(_subItem.getFieldName());
						} else{
							_subItem.setRefEnumerationName(_subItem.getRefEnumerationName().replaceAll("[0-9]", ""));
						}												
						
						if(_subItem.getInputType() == 1 && !_subItem.getRefEnumerationName().contains(serialStr)){
							_subItem.setRefEnumerationName(_subItem.getRefEnumerationName()+serialStr);
						}
					}
					bean.getChildShowField().put(tn, childFields);
				}
				
				//*******对特殊字段进行屏蔽处理********//
				ArrayList<FieldBean> childList = toFieldList(list, GlobalsTool.getLocale(req).toString(),allConfigList,bean.getValues());
				for(int i = childList.size()-1;i>=0;i--){
					FieldBean fBean = childList.get(i);
					if("id".equals(fBean.getFieldName()) || "createBy".equals(fBean.getFieldName()) || "createTime".equals(fBean.getFieldName()) || "lastUpdateBy".equals(fBean.getFieldName()) || "lastUpdateTime".equals(fBean.getFieldName()) || "f_brother".equals(fBean.getFieldName())){
						childList.remove(i);
						continue;
					}
				}
				//*************end**************//
				bean.getChildShowField().put(tn, childList);
			}						
			
			Message msg = new Message("OK","", bean); 
			setMsg(req, rsp, msg);
		} else {
			Message msg = new Message("ERROR", bean.getMsg());
			setMsg(req, rsp, msg);
		}
	}

	/**
	 * 表字段转换为接口字段类型
	 * @param fieldList
	 * @param locale
	 * @return
	 */
	private ArrayList<FieldBean> toFieldList(ArrayList<DBFieldInfoBean> fieldList, String locale,List allConfigList,HashMap fieldVal) {
		ArrayList<FieldBean> list = new ArrayList<FieldBean>();
		for (DBFieldInfoBean bean : fieldList) {
			FieldBean fb = new FieldBean();
			Field[] fs = fb.getClass().getDeclaredFields();
			//****过滤某些字段****//
			//#if("$row.inputType" != "100" && "$row.fieldType" != "16"   && $row.fieldName != "id" && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime" && "$row.fieldName"!="moduleType" && "$row.fieldName"!="f_brother")
			//if("100".equals(String.valueOf(bean.getInputType())) || "16".equals(String.valueOf(bean.getFieldType())) || "f_brother".equals(bean.getFieldName())){
			if("16".equals(String.valueOf(bean.getFieldType())) || "f_brother".equals(bean.getFieldName())){
				continue;
			}
			//*******end******//
			for (Field f : fs) {
				String name = f.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				Method getm;
				try {
					if (name.equals("TableName")) {
						getm = bean.getClass().getMethod("getTableBean");
						DBTableInfoBean value = (DBTableInfoBean) getm.invoke(bean);
						Method setm = fb.getClass().getMethod("set" + name, String.class);
						setm.invoke(fb, value.getTableName());
					} else {
						if(f.getName().equals("enumItem") || f.getName().equals("popupDis"))
							continue;
						getm = bean.getClass().getMethod("get" + name);
						Object value = getm.invoke(bean);
						if (name.equals("Display")) {
							String display = ((KRLanguage) value).get(locale);
							Method setm = fb.getClass().getMethod("set" + name, String.class);
							setm.invoke(fb, display);
						} else {
							Method setm = fb.getClass().getMethod("set" + name, f.getType());
							setm.invoke(fb, value);
						}
					}
				} catch (Exception e) {
					BaseEnv.log.error("MobileAjax.toFieldList ", e);
				}
			}
			//
			if(fb.getInputType()==1 || fb.getInputTypeOld()==1 || fb.getInputTypeOld()==10 || fb.getInputTypeOld()==5){//当是枚举或单选，复选时，
				fb.setEnumItem(GlobalsTool.getEnumerationItems(fb.getRefEnumerationName(), locale));
			}else if(fb.getInputType() == 15){
				//弹出窗
				fb.setDefaultValue(GlobalsTool.getEmpFullNameByUserId((String)fieldVal.get(bean.getFieldName())));
				/*
				ArrayList<FieldBean> pops = new ArrayList<FieldBean>();
				FieldBean f = new FieldBean();
				f.setInputType(bean.getInputType());
				f.setFieldName(bean.getFieldName());
				f.setDisplay(Globals.getEmpFullNameByUserId(""));
				pops.add(f);
				*/
			}else if(fb.getInputType()==2 || ( fb.getInputTypeOld()==2 && fb.getInputType()==8 )){
				//弹出窗
				ArrayList<FieldBean> popList = new ArrayList<FieldBean>();
				if(bean.getSelectBean() !=null && bean.getSelectBean().getRelationKey().hidden){
					FieldBean f = new FieldBean();
					f.setInputType((byte)3);
					f.setFieldName(bean.getSelectBean().getFieldName(bean.getSelectBean().getRelationKey().parentName));
					popList.add(f);
				}else if( "GoodsField".equals(bean.getFieldSysType()) && GlobalsTool.getPropBean(bean.getFieldName()).getIsSequence()==1){
					FieldBean f = new FieldBean();
					f.setInputType((byte)3);
					f.setFieldName(bean.getFieldName());
					popList.add(f);
					
					String dismh=bean.getFieldName()+"_mh";       
					f = new FieldBean();
					f.setInputType(bean.getInputType());
					f.setFieldName(dismh);
					f.setDisplay(bean.getDisplay().get(locale));
					popList.add(f);
				}else{
					FieldBean f = new FieldBean();
					f.setInputType(bean.getInputType());
					f.setFieldName(bean.getSelectBean().getFieldName(bean.getSelectBean().getRelationKey().parentName));
					f.setDisplay(bean.getDisplay().get(locale));
					popList.add(f);
				}


				if(!( "GoodsField".equals(bean.getFieldSysType()) && GlobalsTool.getPropBean(bean.getFieldName()).getIsSequence()==1)){
					for(PopField srow : bean.getSelectBean().getViewFields()){
						String tableField = "";
			 			if(srow.display!=null && !srow.display.equals("")){
							if(srow.display.indexOf("@TABLENAME")==0){
								int index=srow.display.indexOf(".")+1;
								tableField=fb.getTableName()+"."+srow.display.substring(index);
							}else{
								tableField=srow.display;
							}
			 			}else{
							tableField="";
			 			}
						
						String dis="";
						if(srow.display!=null && !srow.display.equals("") && srow.display.indexOf(".")==-1){
							dis = srow.display;
						}else {
							dis = getFieldDisplay(fb.getTableName(),bean.getSelectBean().getName(),tableField,locale);
							if(dis == "") 
								dis = getFieldDisplay(srow.fieldName,locale);
						}
						if(allConfigList.size()>0){
							if(GlobalsTool.colIsExistConfigList(fb.getTableName(),srow.asName,"bill")&&GlobalsTool.getFieldBean(srow.asName)!=null){
								FieldBean f = new FieldBean();
								f.setInputType(bean.getInputType());
								f.setFieldType(GlobalsTool.getFieldBean(srow.asName).getFieldType());
								f.setFieldName(getTableField(srow.asName));
								f.setDisplay(dis);
								popList.add(f);
							}
						}else{ //没有列配置时  
							if("true".equals(srow.hiddenInput)){
								continue;
								/*
								FieldBean f = new FieldBean();
								f.setInputType((byte)3);
								f.setFieldName(getTableField(srow.asName));
								popList.add(f);
								
								f = new FieldBean();
								f.setInputType((byte)3);
								f.setFieldName(getTableField(srow.asName));
								popList.add(f);
								*/
							}else{
								FieldBean f = new FieldBean();
								f.setInputType(bean.getInputType());
								if(GlobalsTool.getFieldBean(srow.asName) == null){
									if(GlobalsTool.getFieldBean(srow.fieldName) == null){
										continue;
									}
									f.setFieldType(GlobalsTool.getFieldBean(srow.fieldName).getFieldType());
								} else{
									f.setFieldType(GlobalsTool.getFieldBean(srow.asName).getFieldType());
								}
								
								f.setFieldName(getTableField(srow.asName));
								f.setDisplay(dis);
								popList.add(f);
							}
						}	//没有列配置结束时			
					}	
				}
				fb.setPopupDis(popList);
			}
			list.add(fb);
		}
		return list;
	}
	
	public static String getTableField(String fieldName) {
		return fieldName.substring(0, fieldName.indexOf(".")) + "." + fieldName.substring(fieldName.indexOf(".") + 1);
	}
	
	public String getFieldDisplay(String fieldName,String locale) {
		// 如果字段名中包含分号，代表是有多个表，则字段显示名返回空
		if (fieldName == null || fieldName.trim().length() == 0 || fieldName.indexOf(";") > 0) {
			return "";
		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		Hashtable allTables = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return table + " not Exist";
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(locale).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}
	
	public String getFieldDisplay(String tableNeme, String popupName, String fieldName,String locale) {
		// 如果字段名中包含分号，代表是有多个表，则字段显示名返回空
		if (fieldName == null || fieldName.trim().length() == 0 || fieldName.indexOf(";") > 0) {
			return "";
		}
		
		
//		Hashtable<String, KRLanguage> moduleLanguage = (Hashtable<String, KRLanguage>) application.getAttribute("moduleColLanguage");
//		String strType = "bill" ;
//		KRLanguage language = moduleLanguage.get(tableNeme + popupName + fieldName + strType);
//		if (language != null) {
//			return language.get(getLocale());
//		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		Hashtable allTables = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return table + " not Exist";
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(locale).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}
	
	private ArrayList<FieldBean> toFieldList(ArrayList<DBFieldInfoBean> fieldList, String locale) {
		ArrayList<FieldBean> list = new ArrayList<FieldBean>();
		for (DBFieldInfoBean bean : fieldList) {
			FieldBean fb = new FieldBean();
			Field[] fs = fb.getClass().getDeclaredFields();
			for (Field f : fs) {
				String name = f.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				Method getm;
				try {
					if (name.equals("TableName")) {
						getm = bean.getClass().getMethod("getTableBean");
						DBTableInfoBean value = (DBTableInfoBean) getm.invoke(bean);
						Method setm = fb.getClass().getMethod("set" + name, String.class);
						setm.invoke(fb, value.getTableName());
					} else {
						getm = bean.getClass().getMethod("get" + name);
						Object value = getm.invoke(bean);
						if (name.equals("Display")) {
							String display = ((KRLanguage) value).get(locale);
							Method setm = fb.getClass().getMethod("set" + name, String.class);
							setm.invoke(fb, display);
						} else {
							Method setm = fb.getClass().getMethod("set" + name, f.getType());
							setm.invoke(fb, value);
						}
					}
				} catch (Exception e) {
					BaseEnv.log.error("MobileAjax.toFieldList ", e);
				}
			}
			list.add(fb);
		}
		return list;
	}


	/**
	 * 自定义单据添加前准备数据接口，
	 * 
	 * @param tableName  表名
	 * @param parentTableName 父表名
	 * @param f_brother 兄弟表的父单据ID
	 * @param parentCode 父目录classCode
	 * @param moduleType 模块类型
	 * <br/><br/>
	 * 返回值: {code:OK,obj:DetailBean对象，见DetailBean接口说明}
	 * @see DetailBean
	 */
	public void addPrepare(HttpServletRequest request, HttpServletResponse rsp) {
		GlobalsTool globals = new GlobalsTool();
	
		DetailBean detailBean = new DetailBean();
		detailBean.setOpType("add");
		try {
			// 通过多窗口得到链接地址得到权限
			String tableName = request.getParameter("tableName");
			String parentTableName = request.getParameter("parentTableName");
			parentTableName = parentTableName == null ? "" : parentTableName;
			// 设置兄弟表的ID
			String f_brother = request.getParameter("f_brother");
			f_brother = f_brother == null ? "" : f_brother;
			String parentCode = request.getParameter("parentCode");
			parentCode = parentCode == null ? "" : parentCode;
			String moduleType = request.getParameter("moduleType");

			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
			MOperation mop = GlobalsTool.getMOperationMap(request);
			if (mop == null) {
				// 对整个模块无访问权限
				Message msg = new Message("ERROR", GlobalsTool.getMessage(request, "common.msg.RET_NO_RIGHT_ERROR"));
				setMsg(request, rsp, msg);
				return;
			}
			ArrayList scopeRight = new ArrayList();
			scopeRight.addAll(mop.getScope(MOperation.M_ADD));
			scopeRight.addAll(this.getLoginBean(request).getAllScopeRight());

			// 得到各个模块的字段信息，此功能是为了实现不同模块使用同一个表，和相同的define
			HashMap<String, ArrayList<String[]>> moduleTable = (HashMap<String, ArrayList<String[]>>) BaseEnv.ModuleField.get(mop.getModuleUrl());

			LoginBean lg = this.getLoginBean(request);
			Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());

			Hashtable map = allTables;

			// 启用了分支机构时，如果不是最末级分支机构，不能添加不共享的数据
			String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
			boolean openSunCompany = Boolean.parseBoolean(openValue);
			boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
			boolean isSharedTable = tableInfo.getIsSunCmpShare() == 1 ? true : false;
			// 当是职员表或部门表时，不作末级分支机构限制
			boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo.getTableName()) || "tblDepartment".equalsIgnoreCase(tableInfo.getTableName()) ? true : false;
			if (openSunCompany && !isLastSunCompany && !isSharedTable && !isEmpOrDept) {
				Message msg = new Message("ERROR", GlobalsTool.getMessage(request, "common.msg.notLastSunCompany"));
				setMsg(request, rsp, msg);
				return;
			}
			String sysParamter = tableInfo.getSysParameter();
			int periodMonth = -1;
			AccPeriodBean accBean = (AccPeriodBean) sessionSet.get("AccPeriod");
			if (accBean != null) {
				periodMonth = accBean.getAccMonth();
			}

			if (periodMonth < 0) {
				if (null != sysParamter) {
					if (sysParamter.equals("UnUseBeforeStart") || sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart")) {
						Message msg = new Message("ERROR", GlobalsTool.getMessage(request, "com.UnUseBeforeStart"));
						setMsg(request, rsp, msg);
						return;
					}
				}
			}

			HashMap values = new HashMap();
			values.put("f_brother", f_brother);
			String urlValue = "";
			//设置默认值
			List listField = tableInfo.getFieldInfos();
			for (int i = 0; i < listField.size(); i++) {
				DBFieldInfoBean field = (DBFieldInfoBean) listField.get(i);
				if (field.getDefaultValue() != null && !field.getDefaultValue().equals("")) {
					//日期类型给默认日期
					if (field.getFieldType() == 5) { 
						field.setDefaultValue(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
						if ("BornDate".equals(field.getFieldName())) {
							field.setDefaultValue("");// Bug #10752
														// 生日默认当前日期，请改为空
						}
					} else if (field.getFieldType() == 6) {
						field.setDefaultValue(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					}
				}
				if("BillNo".equalsIgnoreCase(field.getFieldIdentityStr())){
					field.setDefaultValue(globals.getBillNoCode(tableInfo.getTableName()+"_"+field.getFieldName(),request));
				}
				if ("paramDefaultValue".equals(field.getFieldIdentityStr())) {//字段是带参数默认值
					/* 设置带参数默认值 */
					String fieldValue = request.getParameter(field.getFieldName());
					values.put(field.getFieldName(), fieldValue);
				}
				// 给弹出框设置默认值
				if (field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE
						|| (field.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && field.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)) {
					if (field.getInputValue() != null && field.getInputValue().length() > 0 && field.getDefaultValue() != null && field.getDefaultValue().length() > 0) {
						dbmgt.getDefRefValue(field, values, sessionSet, getLoginBean(request).getId(), allTables, "");
					}
				} else {
					if (field.getDefaultValue() != null && field.getDefaultValue().contains("@Sess:")) {
						String name = field.getDefaultValue().substring(field.getDefaultValue().indexOf("@Sess:") + "@Sess:".length());
						String value;
						if (name.equals("parentCode")) {
							value = request.getParameter("parentCode");
						} else {
							value = sessionSet.get(name) == null ? "" : sessionSet.get(name).toString();
						}
						values.put(field.getFieldName(), value);
					}
				}
				// 同一张表不同的模块，会在连接地址中加上字段用来表示这个模块，保存这些字段与值，用于查询 字段需要累加上条记录
				if (GlobalsTool.getUrlBillDef(request, field.getFieldName()) != null && GlobalsTool.getUrlBillDef(request, field.getFieldName()).length() > 0) {
					if (field != null && field.getFieldType() == DBFieldInfoBean.FIELD_INT) {
						urlValue += " and " + field.getFieldName() + "=" + GlobalsTool.getUrlBillDef(request, field.getFieldName()) + "";
					} else {
						urlValue += " and " + field.getFieldName() + "='" + GlobalsTool.getUrlBillDef(request, field.getFieldName()) + "'";
					}
				}
				//如果有moduleType，则查询上累加值时要过滤moduleType
				if ("moduleType".equals(field.getFieldName()) && moduleType != null && moduleType.length() > 0
						&& !urlValue.contains("moduleType")) {
					if (field != null && field.getFieldType() == DBFieldInfoBean.FIELD_INT) {
						urlValue += " and moduleType=" + moduleType + "";
					} else {
						urlValue += " and moduleType='" + moduleType + "'";
					}
				}
			}
			// 设置明细表的带sess的默认值
			ArrayList childTableList = DDLOperation.getChildTables(tableName, map);
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean db = (DBTableInfoBean) childTableList.get(i);
				List childFields = db.getFieldInfos();

				for (int j = 0; j < childFields.size(); j++) {
					DBFieldInfoBean field = (DBFieldInfoBean) childFields.get(j);
					if (field.getDefaultValue() != null && field.getDefaultValue().contains("@Sess:")) {
						dbmgt.getChildDefRefValue(field, values, sessionSet, getLoginBean(request).getId(), allTables, db.getTableName());
					}
				}
			}

			/* 获取连接地址，当连接地址中存在(paramValue=true)时，后面的参数值都附加到values中 */
			String paramStr = request.getQueryString();
			String pStr = "paramValue=true";
			if (paramStr.indexOf(pStr) != -1) {
				// 存在
				String paramVal = paramStr.substring(paramStr.indexOf(pStr) + pStr.length());
				String[] paramSplit = paramVal.split("&");
				for (String s : paramSplit) {
					if (s != null && !"".equals(s) && s.indexOf("=") != -1 && s.split("=").length > 1) {
						String val = URLDecoder.decode(s.split("=")[1], "UTF-8");
						if (val != null) {
							val = GlobalsTool.replaceSpecLitter(val);
						}
						values.put(s.split("=")[0], val);
						// 如果是关联表选择也要将显示字段的值保存到values中
						DBFieldInfoBean field = GlobalsTool.getFieldBean(tableName, s.split("=")[0]);
						if (field != null && field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
							DynDBManager dyn = new DynDBManager();
							dyn.getRefReturnValues(field, val, allTables, lg.getSunCmpClassCode(), values, values, lg.getId(), true, null, null);
						}

					}
				}
			}
			// 这是复制上面的paramValue,因为在表结构修改时不知道什么原因，@paramValue
			// 前面的@para会变乱码，导致修改一次后值就变了，所以开了一种defaultValue代替他
			paramStr = request.getQueryString();
			pStr = "defaultValue=true";
			if (paramStr.indexOf(pStr) != -1) {
				// 存在
				String paramVal = paramStr.substring(paramStr.indexOf(pStr) + pStr.length());
				String[] paramSplit = paramVal.split("&");
				for (String s : paramSplit) {
					if (s != null && !"".equals(s) && s.indexOf("=") != -1 && s.split("=").length > 1) {
						String val = URLDecoder.decode(s.split("=")[1], "UTF-8");
						if (val != null) {
							val = GlobalsTool.replaceSpecLitter(val);
						}
						values.put(s.split("=")[0], val);
						// 如果是关联表选择也要将显示字段的值保存到values中
						DBFieldInfoBean field = GlobalsTool.getFieldBean(tableName, s.split("=")[0]);
						if (field != null && field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
							DynDBManager dyn = new DynDBManager();
							dyn.getRefReturnValues(field, val, allTables, lg.getSunCmpClassCode(), values, values, lg.getId(), true, null, null);
						}

					}
				}
			}
			detailBean.setValues(values);

			// 检查子表中关联表选择字段中的弹出框是否存在相同的返回的displayField字段和关联保存字段
			ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>();
			// 加载主表中自定列配置
			ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();
			Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String, ArrayList<ColConfigBean>>();
			Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
			if (userColConfig != null && userColConfig.size() > 0) {
				configList = userColConfig.get(tableName + "bill");
				if (configList != null) {
					allConfigList.addAll(configList);
				}
			}
			String allChildName = tableName + ",";// 把主表和子表全部用逗号连接
			// 加载明细中自定义列的显示
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTableInfo = (DBTableInfoBean) childTableList.get(i);
				allChildName += childTableInfo.getTableName() + ",";
				if (userColConfig != null && userColConfig.size() > 0) {
					ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName() + "bill");
					if (childConfigList != null) {
						// 如果做了模块字段设置，那么列配置字段中将不包含这些字段，页面将不再做处理
						if (moduleTable != null && moduleTable.get(childTableInfo.getTableName()) != null) {
							ArrayList<String[]> moduleFields=(ArrayList<String[]>)moduleTable.get(childTableInfo.getTableName());
                			ArrayList<ColConfigBean> cols=new ArrayList<ColConfigBean>();
                			for(ColConfigBean colBean :childConfigList){
                				boolean found = false;
                				for(String[] mf :moduleFields){
                					if(mf[0].equals(colBean.getColName())){
                						found=true;
                						break;
                					}
                				}
                				if(true){
                					cols.add(colBean);
                				}
                			}
							allConfigList.addAll(cols);
							childTableConfigList.put(childTableInfo.getTableName(), cols);
						} else {
							allConfigList.addAll(childConfigList);
							childTableConfigList.put(childTableInfo.getTableName(), childConfigList);
						}
					}
				}
			}

			Collections.sort(childTableList, new SortDBTable());
			ArrayList ctn = new ArrayList();
			for (DBTableInfoBean dtb : (ArrayList<DBTableInfoBean>) childTableList) {
				ctn.add(dtb.getTableName());
			}
			detailBean.setChildTableList(ctn);

			DBTableInfoBean tableinfo = DDLOperation.getTableInfo(map, tableName);

			OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(tableName);
			
			String defineInfo=request.getParameter("defineInfo");
	        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			
	        Result addView = dbmgt.addView(tableName, allTables, values, defineInfo, resources, this.getLocale(request), lg);
	        if(addView.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
	        	SaveErrorObj saveErrrorObj = dbmgt.saveError(addView, this.getLocale(request).toString(), "");
	        	Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				setMsg(request, rsp, msg);
				return;
	        }
	        ArrayList<String> defineField = (ArrayList<String>)values.get("DEFINE_INPUTTYPE");

			// 根据权限,列配置，工作流决定显示哪些字段
			ArrayList<DBFieldInfoBean> fieldList = DynDBManager
					.getMainFieldList(tableName, tableInfo,defineField, configList, template, null, moduleTable, mop, "0", false, f_brother, lg, scopeRight);

			Locale locale = this.getLocale(request);
			detailBean.setFieldList(toFieldList(DynDBManager.distinctList(fieldList), locale.toString()));

			HashMap childShowField = new HashMap();
			for (int i = 0; i < childTableList.size(); i++) { // 明细表的显示列
				DBTableInfoBean childTableInfo = (DBTableInfoBean) childTableList.get(i);
				childShowField.put(childTableInfo.getTableName(), toFieldList(DynDBManager.getMainFieldList(childTableInfo.getTableName(), childTableInfo, defineField,
						childTableConfigList.get(childTableInfo.getTableName()), template, null, moduleTable,
						 mop, "", false, f_brother, lg, scopeRight), locale.toString()));
			}
			detailBean.setChildShowField(childShowField);
			
			// 判断哪些字段需要累加上条记录
			HashMap lastValues = new HashMap();
			for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
				DBFieldInfoBean b = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
				if (b.getFieldIdentityStr() != null && b.getFieldIdentityStr().equals("lastValueAdd")) {
					Result rs = dbmgt.getLastValue(tableName, parentCode, b.getFieldName(), urlValue);
					if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						if (rs.getRetVal() != null) {
							values.put(b.getFieldName(), rs.getRetVal());
						}
					}
				}
			}
						
			Message msg = new Message("OK","", detailBean);
			setMsg(request, rsp, msg);
			return;
		}catch(Exception e){
			Message msg = new Message("ERROR", "执行错误："+e.getMessage());
			BaseEnv.log.error("MobileAjax.addPrepare Error:",e);
			setMsg(request, rsp, msg);
			return;
		}

	}
	
	/**
	 * DBTableInfoBean 按tbleName排序
	 */
	private  class SortDBTable implements Comparator {
		public int compare(Object o1, Object o2) {
			DBTableInfoBean table1 = (DBTableInfoBean) o1;
			DBTableInfoBean table2 = (DBTableInfoBean) o2;

			if (table1 == null || table2 == null) {
				return 0;
			}

			String tableName1 = table1.getTableName();
			String tableName2 = table2.getTableName();

			return tableName1.compareToIgnoreCase(tableName2);
		}
	}
	
	/**
	 * 自定义单据添加接口，
	 * 
	 * @param tableName  表名
	 * @param parentCode 父目录classCode
	 * @param saveType saveDraft:存草稿;空:正式单据
	 * @param defineInfo 如自定义中有弹出可选确认框，会自动带出一个字定义标识代码，按原文传入。默认为空
	 * @param values  存储所有单据字段及其值的HashMap<fieldName,value>
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void add(HttpServletRequest request, HttpServletResponse rsp) {
		
		/*设置兄弟表的f_brother*/
		String tableName = request.getParameter("tableName");
		/*父类的classCode*/
		String parentCode = request.getParameter("parentCode");
		//要执行的define的信息
		String defineInfo = request.getParameter("defineInfo");
		
		String saveType = request.getParameter("saveType"); //保存类型 saveDraft 草稿
		
		String valuesStr = request.getParameter("values");
		
		String deliverTo = request.getParameter("deliverTo");
		
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		
		Message msg = AIOConnect.add(locale, loginBean, tableName, parentCode, defineInfo, saveType, valuesStr, deliverTo);
		this.setMsg(request, rsp, msg);
		return ;
	}


	/**
	 * 取自定义枚举值，
	 * 
	 * @param enum  枚举标识名称
	 * <br/><br/>
	 * 返回值: {code:OK,obj:[枚举值]}
	 */
	public void getEnum(HttpServletRequest req, HttpServletResponse rsp) {
		String enumeration = req.getParameter("enum");
		List list = GlobalsTool.getEnumerationItems(enumeration, "zh_CN");
		Message msg = new Message("OK","", list);
		setMsg(req, rsp, msg);
	}

	/**
	 * 执行自定义Define代码，
	 * 
	 * @param tableName  表名
	 * @param defineName  自定义define的名字
	 * @param buttonTypeName define的中文名字
	 * @param keyId 单据ID，本参数会传入自定义参数表中，可以挤接ID字段，在Define中再折分
	 * @param defineInfo 如自定义中有弹出可选确认框，会自动带出一个字定义标识代码，按原文传入。默认为空
	 
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void execDefine(HttpServletRequest request, HttpServletResponse rsp) {
		String defineName = request.getParameter("defineName");
		String buttonTypeName = request.getParameter("buttonTypeName");
		String[] keyIds = request.getParameterValues("keyId");
		String tableName = request.getParameter("tableName");
		String defineInfo = request.getParameter("defineInfo");
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		Message msg = AIOConnect.execDefine(locale, loginBean, defineName, buttonTypeName, keyIds, tableName, defineInfo);
		if(msg.getCode().equals("OK")){
			String msgStr = gson.toJson(msg.getObj());
			respMsg(request, rsp, msgStr);
			return;
		}else{
			setMsg(request, rsp, msg);
			return;
		}
	}

	/**
	 * 取自定义报表，
	 * 
	 * @param reportNumber  报表名称
	 * @param pageNo  页号
	 * @param pageSize 每页行数
	 * @param parentTableName 父表名，数据表列表输传入的参数
	 * @param tableName 表名，数据表列表传入
	 * @param moduleType 模块名称，数据表列表传入
	 * <br/><br/>
	 * 返回值: {code:OK,obj:MReportBean对象，见MReportBean接口说明}
	 * @see MReportBean
	 */
	public void report(HttpServletRequest req, HttpServletResponse rsp) {
		String reportNumber = req.getParameter("reportNumber");
		String pageNoStr = req.getParameter("pageNo");
		String pageSizeStr = req.getParameter("pageSize");
		String parentTableName = req.getParameter("parentTableName"); //数据表列表输转入的参数
		String tableName = req.getParameter("tableName");
		String moduleType = req.getParameter("moduleType");

		int pageNo = 1;
		if (pageNoStr != null && pageNoStr.length() > 0) {
			pageNo = Integer.parseInt(pageNoStr);
		}
		int pageSize = 20;
		if (pageSizeStr != null && pageSizeStr.length() > 0) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		LoginBean lg = getLoginBean(req);

		boolean isTable = false;

		MOperation mop = null;
		if (tableName != null && tableName.length() > 0) {//当传入的是tableName时，表示这是一个数据表列表，否则是报表
			mop = GlobalsTool.getMOperationMap(req);
			reportNumber = tableName;
			isTable = true;
			MOperation murl = null;
			if(parentTableName != null && !"".equals(parentTableName)){
				murl = (MOperation) lg.getOperationMap().get("/UserFunctionQueryAction.do?parentTableName="+parentTableName+"&tableName="+tableName);
			}			
			else{
				murl = (MOperation) lg.getOperationMap().get("/UserFunctionQueryAction.do?tableName="+tableName);
			}
			//murl = (MOperation) lg.getOperationMap().get("/ReportDataAction.do?reportNumber=" + reportNumber);
			if(murl == null){
				mop = null;
			}
		} else {
			mop = (MOperation) lg.getOperationMap().get("/ReportDataAction.do?reportNumber=" + reportNumber);
		}

		//检查当前表名是否有主表存在,需要根据主表获取权限
		if (mop == null) {
			//对整个模块无访问权限
			setMsg(req, rsp, new Message("ERROR", "没有权限"));
			return;
		}
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
		//加入所有分类权限
		scopeRight.addAll(mop.classScope);
		ArrayList allScopeList = this.getLoginBean(req).getAllScopeRight();
		if (allScopeList != null) {
			scopeRight.addAll(allScopeList);
		}
		ArrayList scopeRightUpdate = mop.getScope(MOperation.M_UPDATE);
		ArrayList scopeRightDel = mop.getScope(MOperation.M_DELETE);

		DefineReportBean defBean;
		try {
			defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(req).toString(), lg.getId());
			ReportData reportData = new ReportData();
			req.setAttribute("mobileAjax", "true");//设置这是从手机ajax来的查询
			Result rs = reportData.showData(mop, req, scopeRight, reportNumber, defBean, "", pageNo, pageSize, scopeRightUpdate, scopeRightDel, lg, null);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				Message msg = new Message("ERROR", new ErrorMessage().toString(rs.retCode, GlobalsTool.getLocale(req).toString()));
				setMsg(req, rsp, msg);
				return;
			} else {
				MReportBean mrb = new MReportBean();
				mrb.setTotalPage(rs.getTotalPage());
				ArrayList<String[]> mcond = new ArrayList<String[]>();
				ArrayList<String[]> conditions = (ArrayList<String[]>) req.getAttribute("allConditions");
				for (String[] cond : conditions) {
					mcond.add(new String[] { cond[1], cond[2] });
				}
				mrb.setConditions(mcond);
				ArrayList<String[]> cols = (ArrayList<String[]>) req.getAttribute("cols");
				mrb.setCols(cols);
				ArrayList rows = new ArrayList();
				if (req.getAttribute("result") != null && isTable) {
					ArrayList<TableListResult> result = (ArrayList<TableListResult>) req.getAttribute("result");
					OAWorkFlowTemplate workFlowDesign = BaseEnv.workFlowInfo.get(tableName); //工作流信息
					for (TableListResult tl : result) {

						String workFlowNode = tl.getWorkFlowNode() == null ? "-1" : tl.getWorkFlowNode();
						HashMap row = new HashMap();
						String rowDis = tl.getRowDis();
						String[] rowDiss = rowDis.split("','");
						rowDiss[0]=rowDiss[0].substring(1);
						rowDiss[rowDiss.length -1] = rowDiss[rowDiss.length -1].substring(0,rowDiss[rowDiss.length -1].length()-1);
						for (int i = 0; i < cols.size(); i++) {
							row.put(cols.get(i)[4], rowDiss[i]);
						}
						row.put("f_brother", tl.getF_brother());
						row.put("classCode", tl.getClassCode());
						row.put("workFlowNode", workFlowNode);
						row.put("workFlowNodeName", tl.getWorkFlowNodeName());
						
						if (null != workFlowDesign && workFlowDesign.getTemplateStatus() == 1) {
							// 如果当前工作流已经有流程版本，需要根据单据id查询模板id
							String designId = workFlowDesign.getId();
							if (!workFlowDesign.getId().equals(workFlowDesign.getSameFlow())) {
								designId = new PublicMgt().getDesignIdByKeyId((String) tl.getKeyId(), tableName);
								if (designId == null || designId.length() == 0) {
									designId = workFlowDesign.getId();
								}
							}
							WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
							FlowNodeBean curNode = null;
							if (designBean != null) {
								HashMap<String, FlowNodeBean> nodeMap = designBean.getFlowNodeMap();
								curNode = nodeMap.get(workFlowNode);
							}
							if ("draft".equals(tl.getWorkFlowNodeName())) {
								row.put("workFlowStatus", "草稿");
							} else if (curNode != null) {
								row.put("workFlowStatus", curNode.getDisplay());
							}
						}
						row.put("keyId", tl.getKeyId());
						rows.add(row);
					}
				} else if (req.getAttribute("result") != null && !isTable) {
					ArrayList<Object[]> result = (ArrayList<Object[]>) req.getAttribute("result");
					for (Object[] tl : result) {
						String rowDis = tl[0] + "";
						
						String[] rowDiss = rowDis.split("','");
						rowDiss[0]=rowDiss[0].substring(1);
						rowDiss[rowDiss.length -1] = rowDiss[rowDiss.length -1].substring(0,rowDiss[rowDiss.length -1].length()-1);
						
						HashMap row = new HashMap();
						for (int i = 0; i < cols.size(); i++) {
							row.put(cols.get(i)[4], rowDiss[i]);
						}
						rows.add(row);
					}
				}
				mrb.setRows(rows);
				Message msg = new Message("OK","", mrb);
				setMsg(req, rsp, msg);
				return;
			}
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.report Error:", e);
			setMsg(req, rsp, new Message("ERROR", e.getMessage()));
			return;
		}
	}

	/**
	 * 取手机版首页的功能列表包括链接地址
	 * <br/><br/>
	 * 返回值: {code:OK,msg:功能列表}
	 */
	public void getHome(HttpServletRequest req, HttpServletResponse rsp) {
		LoginBean lb = this.getLoginBean(req);
		Message msg = new Message("OK","", req.getSession().getAttribute("MOBILEBODY"));
		setMsg(req, rsp, msg);
	}
	
	/**
	 * 取手机版当前用户菜单
	 * <br/><br/>
	 * 返回值: {code:OK,msg:功能列表}
	 */
	public void getMenu(HttpServletRequest req, HttpServletResponse rsp) {
		LoginBean lb = this.getLoginBean(req);
		ArrayList list = new ArrayList();
		for(MOperation mop :(Collection<MOperation>)( lb.getOperationMap().values())){
			System.out.println("================"+mop.moduleUrl);
			if(((Integer)1).equals(mop.isMobile)){
				HashMap map = new HashMap();
				map.put("icon", mop.icon);
				map.put("url", mop.moduleUrl);
				list.add(map);
			}
		}
		Message msg = new Message("OK","", list);
		setMsg(req, rsp, msg);
	}
	/**
	 * 审核前准备接口，返回审核结点，审核人等信息，
	 * 
	 * @param keyId 审核单据ID
	 * @param tableName 审核表名
	 * <br/><br/>
	 * 返回值: {code:OK,obj:{nextNodes：下一步结点列表，可能有多个；currNode：当前结点信息；designId：工作流模板ID；moduleName：模块名字；keyId：工作流对应单据ID；tableName表名；dispenseWake：允许分发信息}}
	 */
	public void deliverToPrepare(HttpServletRequest req, HttpServletResponse rsp) {
		String keyId = req.getParameter("keyId");
		String tableName = req.getParameter("tableName");
		LoginBean loginBean = this.getLoginBean(req);
		//本参数只有在消息连接中出现，单据中链接不传入此参数
		String outNodeId = req.getParameter("currNodeId");
		
		Message msg = AIOConnect.deliverToPrepare(getLocale(req).toString(), loginBean, keyId, tableName, outNodeId);
		setMsg(req, rsp, msg);
		return;
	}
	
	/**
	 * 审核到第一个节点，现在很多情况用户希望直接进入下一审核结点，
	 * 
	 * @param keyId 审核单据ID
	 * @param tableName 审核表名
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void deliverToNext(HttpServletRequest req, HttpServletResponse rsp) {
		String keyId = req.getParameter("keyId");
		String tableName = req.getParameter("tableName");
		LoginBean loginBean = this.getLoginBean(req);
		
		Message msg = AIOConnect.deliverToNext(getLocale(req).toString(), loginBean, keyId, tableName);
		setMsg(req, rsp, msg);
		return;
	}
	
	/**
	 * 删除自定义单据，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * <br/><br/>
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void delete(HttpServletRequest request, HttpServletResponse rsp) {

		ActionForward forward = null;
		String tableName = request.getParameter("tableName");
		String keyId = request.getParameter("keyId");
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		Message msg = AIOConnect.delete(locale, loginBean, tableName, keyId);
		setMsg(request, rsp, msg);
		return;
	}
	
	/**
	 * 反审核单据，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void retCheck(HttpServletRequest request, HttpServletResponse rsp) {

		String keyId = request.getParameter("keyId");
		String tableName = request.getParameter("tableName");
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		Message msg = AIOConnect.retCheck(locale, loginBean, keyId, tableName);
		this.setMsg(request, rsp, msg);
		return;
	}
	/**
	 * 撤回审核，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void cancelTo(HttpServletRequest req, HttpServletResponse rsp) {
		String keyId = req.getParameter("keyId"); /* 单据ID */
		String tableName = req.getParameter("tableName"); /* 当前表单名称 */
		LoginBean loginBean = this.getLoginBean(req);
		String locale = getLocale(req).toString();
		Message msg = AIOConnect.cancelTo(locale, loginBean, keyId, tableName);
		this.setMsg(req, rsp, msg);
		return;
	}
	
	/**
	 * 审核单据，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * @param nextStep 下个审批结点
	 * @param currNode 当前审批结点
	 * @param designId 工作流模板ID
	 * @param checkPerson 选择的审批人
	 * @param deliverance 审批意见
	 * @param popedomUserIds 分发提醒人
	 * @param popedomDeptIds 分发提醒部门
	 * @param oaTimeLimit 限时数字 如：10小时、10天
	 * @param oaTimeLimitUnit 限时单位
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void deliverTo(HttpServletRequest req, HttpServletResponse rsp) {
		try {
			String keyId = req.getParameter("keyId"); /*单据ID*/
			String nextStep = req.getParameter("nextStep"); /*下个审批结点*/
			String currNode = req.getParameter("currNode"); /*当前审批结点*/
			String designId = req.getParameter("designId"); /*流程ID*/
			String type = ""; 
			String tableName = req.getParameter("tableName"); /*当前表单名称*/

			String checkPerson = req.getParameter("checkPerson"); //选择的审批人
			String deliverance = req.getParameter("deliverance"); //审批意见
			deliverance = deliverance == null ? "" : deliverance;
			String userIds = req.getParameter("popedomUserIds"); //分发提醒人
			String deptIds = req.getParameter("popedomDeptIds"); //分发提醒部门
			String oaTimeLimit = req.getParameter("oaTimeLimit"); //限时数字 如：10小时、10天 
			String oaTimeLimitUnit = req.getParameter("oaTimeLimitUnit"); //限时单位


			LoginBean loginBean = this.getLoginBean(req);
			String locale = getLocale(req).toString();
			
			Message msg =AIOConnect.deliverTo(locale, loginBean, keyId, tableName, nextStep, currNode, designId, checkPerson, deliverance,
					userIds, deptIds, oaTimeLimit, oaTimeLimitUnit);
			
			this.setMsg(req, rsp, msg);
			return;
		} catch (Exception e) {
			this.setMsg(req, rsp, setMsg("ERROR", e.getMessage()));
			return;
		}
	}
	
	
	
	/**
	 * 审批前要执行一个校验动作（通过修改保存单据来实现此功能），确保系统的各种校验是否成功，如果在调审核接口前已经执行了修改动作，可以不执行这个方法，否则一定要执行确保数据正确性，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * @param defineInfo 如自定义中有弹出可选确认框，会自动带出一个字定义标识代码，按原文传入。默认为空
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public void check(HttpServletRequest req, HttpServletResponse rsp) {
		try {
			String keyId = req.getParameter("keyId"); /*单据ID*/
			String tableName = req.getParameter("tableName"); /*当前表单名称*/
			String defineInfo=req.getParameter("defineInfo");
			LoginBean loginBean = this.getLoginBean(req);
			String locale = getLocale(req).toString();
			
			Message msg =AIOConnect.check(locale, loginBean, keyId, tableName, defineInfo);
			this.setMsg(req, rsp,msg);
			return;
		} catch (Exception e) {
			this.setMsg(req, rsp, setMsg("ERROR", e.getMessage()));
			return;
		}
	}

	private static MessageResources getResources(HttpServletRequest req) {

		return (MessageResources) req.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	}

	
	/**
	 * 登录接口
	 * 
	 * @param name 登陆帐号
	 * @param password 密码（MD5加密）
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；ERROR：执行错误',msg：提示信息}
	 */
	public void login(HttpServletRequest req, HttpServletResponse rsp) {
		String userName = req.getParameter("name");
		String password = req.getParameter("password");
		Result result = userMgt.login(userName, password);
		Message msg = loginAuth(req, rsp, result, userName);
		if ( msg.getCode().equals("OK") && req.getSession().getAttribute("wxopenid") != null ) // 绑定微信openid
			userMgt.updateUserOpenid(userName, password, req.getSession().getAttribute("wxopenid").toString());
		setMsg(req, rsp, msg);
	} 

	/**
	 * 登录接口（本接口不对外开放）
	 * @param req
	 * @param rsp
	 * @param result
	 * @param userName
	 * @return
	 */

	public Message loginAuth(HttpServletRequest req, HttpServletResponse rsp, Result result, String userName) {
		//判断用户数是否超过限制
		if (SystemState.instance.userState != 0) {
			if (SystemState.instance.userState == SystemState.DOG_ERROR_USER) {
				return setMsg("ERROR", "ERP系统用户数超过最大限制(" + SystemState.instance.userNum + "), \\n请在电脑版用admin帐号登陆删除部分系统用户并重启系统");
			} else if (SystemState.instance.userState == SystemState.DOG_ERROR_USER_OA) {
				return setMsg("ERROR", "OA系统用户数超过最大限制(" + SystemState.instance.oaUserNum + "), \\n请在电脑版用admin帐号登陆删除部分系统用户并重启系统");
			} else if (SystemState.instance.userState == SystemState.DOG_ERROR_USER_CRM) {
				return setMsg("ERROR", "CRM系统用户数超过最大限制(" + SystemState.instance.crmUserNum + "), \\n请在电脑版用admin帐号登陆删除部分系统用户并重启系统");
			}
		}
		//密码验证成功
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List userList = (ArrayList) result.retVal;
			if (userList.size() > 0) {
				EmployeeBean userBean = (EmployeeBean) userList.get(0);

				//查找用户权限
				//这里先只查找角色权限
				//如果管理员，不用查角色，直接拥有所有权限
				/**
				 * 取权限
				 */
				ArrayList roleModuleList = new ArrayList();
				HashMap roleModuleScopeMap = new HashMap();
				ArrayList roles = new ArrayList();
				ArrayList allScopeRight = new ArrayList(); //记录应用于所有模块的范围权限
				String hiddenField = "";
				if (!"admin".equalsIgnoreCase(userBean.getSysName())) {
					roles = (ArrayList) LoginAction.getRoles(userBean, "1");
					//用户本身也是一个角色
					RoleBean selfRb = new RoleBean();
					selfRb.setId(userBean.getId());
					selfRb.setRoleName(userBean.getEmpFullName());
					selfRb.setRoleDesc(userBean.getEmpFullName());
					selfRb.setHiddenField(userBean.getHiddenField());
					roles.add(selfRb);
					RoleMgt roleMgt = new RoleMgt();

					for (Object o : roles) {
						RoleBean rb = (RoleBean) o;
						hiddenField += rb.getHiddenField() + ",";

						//查询角色模块权限
						result = roleMgt.queryRoleModuleByRoleid(rb.getId(), userBean.getId());
						if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
							new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.loginError"), req);
							return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.loginError"));
						} else {
							//合并所有模块权限
							roleModuleList.addAll((List) result.getRetVal());
						}

						//查询角色范围权限
						result = roleMgt.queryRoleScopyByRoleid(rb.getId(), userBean.getId(), userBean.getEmpFullName(), userBean.getDepartmentCode());
						if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
							new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.loginError"), req);
							return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.loginError"));
						} else {
							//合并所有范围权限
							HashMap hm = (HashMap) result.getRetVal();
							for (Object hmo : hm.keySet()) {
								ArrayList list = (ArrayList) roleModuleScopeMap.get(hmo);
								if (list == null) {
									roleModuleScopeMap.put(hmo, hm.get(hmo));
								} else {
									list.addAll((List) hm.get(hmo));
								}
							}
						}
					}

					//查询应用于所有模块范围权限------------------------------------
					result = roleMgt.queryAllModScope(userBean.getId(), userBean.getDepartmentCode());
					if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						allScopeRight = (ArrayList) result.retVal;
					}
				}

				LoginBean loginBean = OnlineUserInfo.getLoginBean(userBean.getId(), userBean.getSysName());
				loginBean.setHiddenField(hiddenField); //存入角色带入的隐藏信息。
				loginBean.setMobile(userBean.getMobile());
				loginBean.setDefSys(userBean.getDefSys());
				loginBean.setLoginTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				loginBean.setRoleModuleList(roleModuleList);
				loginBean.setRoleModuleScopeMap(roleModuleScopeMap);
				loginBean.setAllScopeRight(allScopeRight);
				loginBean.setEmpFullName(userBean.getEmpFullName());
				loginBean.setLbxMonitorCh(userBean.getLbxMonitorCh());//来电通道设定
				loginBean.setTelPrefix(userBean.getTelPrefix());//电话前缀
				loginBean.setTelAreaCode(userBean.getTelAreaCode());//用户区位
				loginBean.setTelpop(userBean.getTelpop());
				loginBean.setDepartCode(userBean.getDepartmentCode());
				loginBean.setShopName(userBean.getShopName());
				loginBean.setShopPwd(userBean.getShopPwd());
				loginBean.setDefaultPage(userBean.getDefaultPage());
				loginBean.setExtension(userBean.getExtension());
				loginBean.setViewLeftMenu(userBean.getViewLeftMenu());
				loginBean.setViewTopMenu(userBean.getViewTopMenu());
				loginBean.setJessionid(req.getSession().getId());
				loginBean.setDefStyle("1");
				loginBean.setModuleId(userBean.getModuleId());
				loginBean.setTitleId(userBean.getTitleID());
				loginBean.setDefDesk(userBean.getDefDesk());
				loginBean.setEmail(userBean.getEmail());
				loginBean.setQq(userBean.getQq());
				loginBean.setShowDesk(userBean.getShowDesk());//显示桌面
				loginBean.setFirstShow(userBean.getFirstShow());
				loginBean.setShowWebNote(userBean.getShowWebNote());
				for (Object o : SystemState.instance.moduleList) {
					if (o.toString().equals("1") && userBean.getCanJxc() == 1) {
						loginBean.setCanJxc(1);
					}
					if (o.toString().equals("2") && userBean.getCanOa() == 1) {
						loginBean.setCanOa(1);
					}
					if (o.toString().equals("3") && userBean.getCanCrm() == 1) {
						loginBean.setCanCrm(1);
					}
					if (o.toString().equals("4") && userBean.getCanHr() == 1) {
						loginBean.setCanHr(1);
					}
				}
				if (SystemState.instance.funOrders && userBean.getCanOrders() == 1) {
					loginBean.setCanOrders(1);
				}
				if (userBean.getPhoto() != null && userBean.getPhoto().contains(":")) {
					loginBean.setPhoto(userBean.getPhoto().split(":")[0]);
				} else {
					loginBean.setPhoto(userBean.getPhoto());
				}
				loginBean.setSunCompany("00001");

				//将用户bean放入session中
				req.getSession().removeAttribute("LoginBean");
				req.getSession().setAttribute("LoginBean", loginBean);
				//判断系统配置中是否隐藏邮件地址
				boolean HideEmail = (boolean) GlobalsTool.getHideEmail();
				req.getSession().setAttribute("HideEmail", HideEmail);
				//加入在线人员列表
				String userAgent = req.getHeader("user-agent");
				//来自微信企业号登陆
				WeixinApiMgt.getLoginBean(req, loginBean, userBean);
				OnlineUserInfo.wxqyUserLogin(loginBean, req.getSession().getId());
				//当前登录分支机构的会计期间
				String nowPeriod;
				int nowYear = -1;
				int nowMonth = -1;
				AccPeriodBean bean = null;
				Result rs = new SysAccMgt().getCurrPeriod(loginBean.getSunCmpClassCode());
				if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					nowPeriod = "-1";
				} else {
					bean = (AccPeriodBean) rs.getRetVal();
					nowPeriod = String.valueOf(bean.getAccPeriod());
					nowYear = bean.getAccYear();
					nowMonth = bean.getAccMonth();
				}

				AccPeriodBean AccBean = null;
				rs = new SysAccMgt().getCurrAccPeriod(loginBean.getSunCmpClassCode());
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					AccBean = (AccPeriodBean) rs.getRetVal();
				}

				int nowDay = Calendar.getInstance().get(Calendar.DATE);

				//登录成功时，将一些sess信息放到BaseEnv中
				if (BaseEnv.sessionSet == null) {
					BaseEnv.sessionSet = new Hashtable();
				}
				DynDBManager dbmgt = new DynDBManager();
				//职员分组ID
				String groupIds = (String) dbmgt.getGroupIds(userBean.getId()).getRetVal();
				loginBean.setGroupId(groupIds);
				boolean isLastSunCompany = dbmgt.getChildCount("tblSunCompanys.classCode", loginBean.getSunCmpClassCode()).getRetVal().toString().equals("0");
				Hashtable sessionSet = BaseEnv.sessionSet;
				Hashtable sess = new Hashtable();
				loginBean.setSessMap(sess);
				sess.put("SCompanyID", loginBean.getSunCmpClassCode());
				sess.put("IsLastSCompany", isLastSunCompany);
				sess.put("AccPeriod", bean);
				sess.put("AccPeriodAcc", AccBean);
				sess.put("NowPeriod", nowPeriod);
				sess.put("NowYear", nowYear);
				if (nowYear == -1) {//用于查询条件的默认值
					sess.put("NowPeriodQ", "");
					sess.put("NowYearQ", "");
				} else {
					sess.put("NowPeriodQ", nowPeriod);
					sess.put("NowYearQ", nowYear);
				}
				sess.put("NowMonth", nowMonth);
				sess.put("NowDay", nowDay);
				sess.put("DepartmentCode", userBean.getDepartmentCode() == null ? "" : userBean.getDepartmentCode());
				if (userBean.getDepartmentCode() != null && userBean.getDepartmentCode().length() > 0) {
					Result rss = dbmgt.getDepartMent(userBean.getDepartmentCode());
					if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS && rss.getRetVal() != null) {
						String[] depart = (String[]) rss.getRetVal();
						sess.put("DepartmentName", depart[0]);
						loginBean.setDepartmentName(depart[0]);
						loginBean.setDepartmentManager(depart[1]);
					}
				}
				sess.put("UserId", loginBean.getId());
				sess.put("UserName", loginBean.getEmpFullName());
				sess.put("Local", GlobalsTool.getLocale(req));
				sess.put("BillOper", "");
				sessionSet.put(loginBean.getId(), sess);
				rs = dbmgt.getAccPeriodOverYear();
				sessionSet.put("AccPeriodOverYear", rs.getRetVal());

				req.getSession().setAttribute("NowPeriod", nowPeriod);
				req.getSession().setAttribute("NowYear", nowYear);
				req.getSession().setAttribute("SCompanyID", loginBean.getSunCmpClassCode());

				//记录设置样式风格
				loginBean.setDefStyle("1");
				loginBean.setDefLoc("zh_CN");
				GlobalMgt glmgt = new GlobalMgt();
				LoginAction.parseDefValSet(loginBean.getId(), sess);

				//  更新内存中该用户的登录状态，即时通讯客户端需要用到
				EmployeeItem loginItem = MSGConnectCenter.employeeMap.get(loginBean.getId());
				if (loginItem != null && loginItem.loginStatus == EmployeeItem.OFFLINE) {
					loginItem.loginType = EmployeeItem.MOBILE_LOGIN;
					loginItem.loginStatus = EmployeeItem.ONLINE;
					MSGConnectCenter.userStatus(loginItem.userId, loginItem.loginType, loginItem.loginStatus);
				}
				req.getSession().setAttribute("DEVICE", "mobile");
				//记录登陆日志
				new LogManageMgt().addLog(loginBean.getId(), loginBean.getEmpFullName(), "LOGIN", null, "SUCCESS", "网页手机登陆", req);

				try {//读取用户自定义菜单项存入session 变量 MOBILEBODY
					ArrayList retObj = new ArrayList();
					File file = new File(req.getRealPath("mobile/define/config.ini"));
					if(file.exists()){ 
						java.io.BufferedReader br = new java.io.BufferedReader(new FileReader(file));
						String b = null;
						
						while ((b = br.readLine()) != null) {
							b = b.trim();
							if (!b.equals("") && !b.startsWith("#")) {
								String bs[] = b.split(",");
								if (bs.length >= 5) {//(add,update,query,print)
									MOperation mop = (MOperation) loginBean.getOperationMap().get(bs[3]);
									if (mop != null) {
										if (bs[4].equals("add")) {
											if (mop.add) {
												retObj.add(new String[] { bs[0], bs[1], bs[2] });
											}
										} else if (bs[4].equals("update")) {
											if (mop.update) {
												retObj.add(new String[] { bs[0], bs[1], bs[2] });
											}
										} else if (bs[4].equals("query")) {
											if (mop.query) {
												retObj.add(new String[] { bs[0], bs[1], bs[2] });
											}
										} else if (bs[4].equals("print")) {
											if (mop.query) {
												retObj.add(new String[] { bs[0], bs[1], bs[2] });
											}
										}
									}
								}
							}
						}
					}
					req.getSession().setAttribute("MOBILEBODY", retObj);
				} catch (Exception e1) {
					BaseEnv.log.error("MobileAjax.login =", e1);
				}

				return setMsg("OK", "登陆成功");
			} else {
				return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.namepassworderror"));
			}
		}
		//错误的用户名密码
		else if (result.retCode == ErrorCanst.RET_NAME_PSW_ERROR) {
			new LogManageMgt().addLog("", userName, "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.namepassworderror"), req);
			return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.namepassworderror"));
		}
		// 错误的用户名密码
		else if (result.retCode == ErrorCanst.TIMER_COMPARE_ERROR) {
			new LogManageMgt().addLog("", userName, "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.loginTimeError") + result.retVal, req);
			return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.loginTimeError") + result.retVal);
		}// 用户停用
		else if (result.retCode == ErrorCanst.USER_STOP) {
			new LogManageMgt().addLog("", userName, "LOGIN", null, "FAIL", "此用户帐号已停用", req);
			return setMsg("ERROR", "此用户帐号已停用");
		}
		//其他错误
		else {
			new LogManageMgt().addLog("", userName, "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.loginError"), req);
			return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.loginError"));
		}
	}

	/**
	 * 输出json结果
	 * @param req
	 * @param rsp
	 * @param code
	 * @param msg
	 */
	private Message setMsg(String code, String msg) {
		return new Message(code, msg);
	}

	/**
	 * 输出json结果
	 * @param req
	 * @param rsp
	 * @param code
	 * @param msg
	 */
	private void setMsg(HttpServletRequest req, HttpServletResponse rsp, Message msgObj) {
		try {
			String msgStr = gson.toJson(msgObj);
			respMsg(req, rsp, msgStr);
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.setMsg", e);
		}
	}

	/**
	 * 输出json结果
	 * @param req
	 * @param rsp
	 * @param code
	 * @param msg
	 */
	private void respMsg(HttpServletRequest req, HttpServletResponse rsp, String msgStr) {
		try {
			if (BaseEnv.log.isDebugEnabled()) { //打印调试信息
				String uName = "";
				if (this.getLoginBean(req) != null) {
					uName = this.getLoginBean(req).getEmpFullName();
				}
				BaseEnv.log.debug("MobileAjax 操作人:" + uName + ";" + "返回数据：" + (msgStr.length() > 500 ? msgStr.substring(0, 500) + "等等！" : msgStr));
			}
			rsp.getOutputStream().write(msgStr.getBytes("UTF-8"));
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.respMsg", e);
		}
	}

	/**
	 * 获取会话用户
	 * @param request
	 * @return
	 */
	private LoginBean getLoginBean(HttpServletRequest req) {
		Object o = req.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}

	/**
	 * 打印调试信息
	 * @param request
	 */
	private void debugRequest(HttpServletRequest req) {
		if (BaseEnv.log.isDebugEnabled()) {
			String uName = "";
			if (this.getLoginBean(req) != null) {
				uName = this.getLoginBean(req).getEmpFullName();
			}
			BaseEnv.log.debug("MobileAjax 请求地址：" + req.getRequestURI() + (req.getQueryString() == null ? "" : "?" + req.getQueryString()));
			String rd = "";
			for (Object key : req.getParameterMap().keySet()) {
				Object value = req.getParameterMap().get(key);
				String values = "";
				for (String v : (String[]) value) {
					values += v + ",";
				}
				rd += key + "=[" + values + "];";
			}

			BaseEnv.log.debug("MobileAjax 操作人:" + uName + ";" + "请求数据：" + rd);
		}		
	}

	/**
	 * 进行同步通讯录操作,返回操作结果页面（本接口不对外开放）
	 * @param request
	 * @param response
	 * @param isWXWork 是否是企业微信
	 **/
	public void sync(HttpServletRequest request, HttpServletResponse response) {
		String keyName = request.getParameter("KeyName");
		if (keyName == null || keyName.trim().equals(""))
			return ;
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			List<SyncDepartmentBean> departmentlist = getDepartment();
			List<SyncEmployeeBean> employeelist = getEmployee();
			HashMap<String,String> deptMap  = new HashMap<String,String>();
			List<SyncResultBean> departmentResultList = new ArrayList<SyncResultBean>();
			List<SyncResultBean> employeeResultList = new ArrayList<SyncResultBean>();
			departmentResultList = WXWorkSyncContact.syncDepartment(departmentlist, deptMap, keyName);
			employeeResultList =  WXWorkSyncContact.syncEmployee(employeelist,deptMap, keyName);	
			result.put("employee", employeeResultList);
			result.put("department", departmentResultList);
			result.put("result", "ok");
			
			int nullerror = 0;
			int otherError=0;
			for(SyncResultBean sb:employeeResultList){
				if(sb.getErrcode().equals("60113")){
					nullerror ++;
				}else{
					otherError ++;
				}
			}
			String resultMsg = "同步成功："+(nullerror > 0 ? "手机号为空"+nullerror+"个":"" )+(otherError > 0 ? "其它错误"+otherError+"个":"" );
			result.put("resultMsg", resultMsg);
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.sync Error",e);
			result.put("result", "error");
			result.put("resultMsg", e.getMessage());
		}
		try{
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", -10);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(new Gson().toJson(result));
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.sync Error",e);
		}
	}
	
    /**
     * 获取jsticket
     * @param request
     * @param response
     */
	public void getJsticket(HttpServletRequest request, HttpServletResponse response) {	
		String state = request.getParameter("wxstate");
		String url = request.getParameter("url");
		Message msg ;
		if(state == null || WXSetting.getInstance().getSettingBase(state) == null) {
			msg = new Message("ERROR", "找不到相关的微信配置信息");
		} else {
			msg = new Message("OK", "", WXSetting.getInstance().getJsapiSignature(state, url));
		}
		this.setMsg(request, response, msg);
	}	
	
	/**
	 * 获取登陆用户的职员信息
	 * @param request
	 * @param response
	 */
	public void getSessEmp(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		LoginBean bean = this.getLoginBean(request);
		data.put("id", bean.getId());
		data.put("name", bean.getName());
		data.put("departmentName", bean.getDepartmentName());
		data.put("titleId", bean.getTitleId());
		Message msg = new Message("OK", "", data);
		this.setMsg(request, response, msg);
	}	
	
	/**
	 * 获取所有部门
	 * @return
	 */
	private List<SyncDepartmentBean> getDepartment() {
		final String sql = "SELECT classcode,DeptFullName,id FROM tblDepartment where statusId=0 ORDER BY classcode ";
		final List<SyncDepartmentBean> rst = new ArrayList<SyncDepartmentBean>();
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn.prepareStatement(sql);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								SyncDepartmentBean bean = new SyncDepartmentBean();
								bean.setClasscode(rs.getString("classcode"));
								bean.setDeptFullName(rs.getString("DeptFullName"));
								bean.setId(rs.getString("id"));
								rst.add(bean);
							}

						} catch (Exception ex) {
							BaseEnv.log.error(sql, ex);
							return;
						}
					}
				});
				return 1;
			}
		});
		return rst;
	}

	/**
	 * 获取所有成员
	 * @return
	 */
	private List<SyncEmployeeBean> getEmployee() {  
		final String sql = "select a.id,a.departmentCode,a.email,a.mobile,a.sysName,a.titleId,a.EmpFullName,a.tel,b.DeptFullName from tblemployee a left join " +
				" tbldepartment b on a.DepartmentCode=b.classcode where a.statusId=0 and openflag=1 and len(isnull(a.mobile,''))>0 order by a.isPublic";
		final List<SyncEmployeeBean> rst = new ArrayList<SyncEmployeeBean>();
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn.prepareStatement(sql);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								SyncEmployeeBean bean = new SyncEmployeeBean();
								bean.setDeptFullName(rs.getString("DeptFullName"));
								bean.setId(rs.getString("id"));
								bean.setDepartmentCode(rs.getString("departmentCode"));
								bean.setEmail(rs.getString("email"));
								bean.setMobile(rs.getString("mobile"));
								bean.setSysName(rs.getString("sysName"));
								bean.setTitleId(rs.getString("titleId"));
								bean.setEmpFullName(rs.getString("EmpFullName"));
								bean.setTel(rs.getString("tel"));
								rst.add(bean);
							}

						} catch (Exception ex) {
							BaseEnv.log.error(sql, ex);
							return;
						}
					}
				});
				return 1;
			}
		});
		return rst;
	}

	private void sendMsg(HttpServletRequest request) {
		String key = "koronaio";
		String signPre = "";

		String userId = request.getParameter("userId");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String receiveIds = request.getParameter("receiveIds");
		String relationId = request.getParameter("relationId");
		String type = request.getParameter("type");
		String timeStamp = request.getParameter("timeStamp");
		signPre += "timeStamp" + timeStamp;
		signPre += key;
		String sign = request.getParameter("sign");
		if (sign.equals(MD5(signPre))) {
			new AdviceMgt().add(userId, title, content, receiveIds, relationId, type);
		}
	}

	private String MD5(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			byte[] b = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte c : b) {
				int val = ((int) c) & 0xff;
				if (val < 16)
					sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Locale getLocale(HttpServletRequest req) {
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
			loc = Locale.getDefault();
		}
		return loc;
	}
}
