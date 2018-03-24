package com.menyi.aio.web.login;

import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;

import com.menyi.aio.web.usermanage.UserMgt;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.RoleBean;
import java.util.Date;
import java.util.HashMap;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.logManage.LogManageMgt;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.QRCode;
import com.menyi.web.util.SecurityLock2;

import antlr.collections.*;
import javax.servlet.*;

import com.menyi.web.util.SystemState;
import com.dbfactory.hibernate.DBUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Session;
import com.menyi.web.util.BaseEnv;
import java.sql.PreparedStatement;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;

import sun.misc.BASE64Decoder;

import com.menyi.aio.web.usermanage.UserManageAction;
import java.sql.Statement;
import java.util.Hashtable;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import java.util.Calendar;

import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;

import java.util.Locale;
import com.menyi.web.util.GlobalsTool;
import com.menyi.aio.web.upgrade.RegisterMgt;
import com.menyi.aio.web.wxqy.WeixinApiMgt;
import com.menyi.web.util.ServerConnection;
import com.menyi.web.util.SecurityLock;
import com.menyi.web.util.UsbKey;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 *
 * <p>Title:登陆 </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class LoginAction extends BaseAction {
    UserMgt userMgt = new UserMgt();
    RoleMgt roleMgt = new RoleMgt();
    public LoginAction() {

    }

    /**
     * 登陆不需进行权限判断
     * @param req HttpServletRequest
     * @param mapping ActionMapping
     * @return ActionForward
     */
    protected ActionForward doAuth(HttpServletRequest req,
                                   ActionMapping mapping) {
        return null;
    }


    /**
     * exe 控制器入口函数
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {


    	
        //跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_LOGIN:
        	
        	String OpType=request.getParameter("type");
        	if("client".equals(OpType)){
        		String OpValue=request.getParameter("op");
        		if("login".equals(OpValue)){	// 验证用户名和密码
        			forward=checkUser(mapping,form,request,response);
        		} else if ("check_update".equals(OpValue)) { //检查AIO版本是否有更新
        			forward=checkVersion(mapping,form,request,response);
        		} else if ("adjust_time".equals(OpValue)) { // 客户端校时
        			forward=adjustTime(mapping,form,request,response);
        		} else if ("get_session".equals(OpValue)) { // 获取最新会话
        			forward=getSessionId(mapping,form,request,response);
        		}
        	}else{
        		forward = login(mapping, form, request, response);
        	}
            break;
        case OperationConst.OP_LOGOUT:
            forward = logout(mapping, form, request, response);
            break;
        case OperationConst.OP_QUOTE:
            forward = quote(mapping, form, request, response);
            break;
        default: //默认返回首页
            BaseEnv.log.debug("LoginAction.exec  ---------- default");
            forward = getForward(request, mapping, "indexPage");
        }
        return forward;
    }

    private ActionForward quote(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {    	
    	String logoPath = request.getSession().getServletContext().getRealPath("/style/v7/user") ;
    	File logoFile = new File(logoPath);
    	String msg ="companyLogo;logo1;logo2;logo3;title;link";
    	if(logoFile.isDirectory()){
    		File[] listFile = logoFile.listFiles() ;
    		for(File file :listFile){
    			if(file.getName().contains("loginLogo_1")){
    				msg = msg.replace("logo1", file.getName());    				 				
    			}else if(file.getName().contains("loginLogo_2")){
    				msg = msg.replace("logo2", file.getName());
    			}else if(file.getName().contains("loginLogo_3")){
    				msg = msg.replace("logo3", file.getName());
    			}else if(file.getName().contains("company")){
    				msg = msg.replace("companyLogo", file.getName());
    			}
    		}
    	} 
    	/*判断title和link*/
    	Result rs = new PublicMgt().getCompany();
    	ArrayList rsRs = (ArrayList)rs.retVal;
		if(rsRs !=null && rsRs.size() >0){
			String title = "".equals(GlobalsTool.get(rsRs.get(0),1))?"title":GlobalsTool.get(rsRs.get(0),1).toString();
			String link = "".equals(GlobalsTool.get(rsRs.get(0),0))?"link":GlobalsTool.get(rsRs.get(0),0).toString();
			msg = msg.replace("title", title);
			msg = msg.replace("link", link);
		}
		
    	request.setAttribute("msg", msg);
		return getForward(request, mapping, "blank");
	}

	/**
     * 登陆系统
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward login(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
        Exception {

        ActionForward forward;
        LoginForm loginForm = (LoginForm) form;
    	//判断用户数是否超过限制
    	if(SystemState.instance.userState != 0 && !"admin".equals(loginForm.getName())){
    		if(SystemState.instance.userState == SystemState.DOG_ERROR_USER){
    			EchoMessage.error().add("ERP系统用户数超过最大限制("+SystemState.instance.userNum+"), \\n请用admin帐号登陆删除部分系统用户并重启系统")
					.setNoBackButton() .setAlertRequest(request);
    			return getForward(request, mapping, "alert"); 			
    		}else if(SystemState.instance.userState == SystemState.DOG_ERROR_USER_OA){
    			EchoMessage.error().add("OA系统用户数超过最大限制("+SystemState.instance.oaUserNum+"), \\n请用admin帐号登陆删除部分系统用户并重启系统")
					.setNoBackButton() .setAlertRequest(request);
    			return getForward(request, mapping, "alert");        			
    		}else if(SystemState.instance.userState == SystemState.DOG_ERROR_USER_CRM){
    			EchoMessage.error().add("CRM系统用户数超过最大限制("+SystemState.instance.crmUserNum+"), \\n请用admin帐号登陆删除部分系统用户并重启系统")
					.setNoBackButton() .setAlertRequest(request);
    			return getForward(request, mapping, "alert");
    		}else if(SystemState.instance.userState == SystemState.DOG_ERROR_USER_HR){
    			
    		}else if(SystemState.instance.userState == SystemState.DOG_ERROR_USER_ORDERS){
    			
    		}
    	}
        
        /*天华*/
    	request.setAttribute("newBody", getParameter("newBody", request));
        
        Result result = null;
        String adviceId = getParameter("id",request);
        String adviceType = getParameter("adviceType", request);

        if (adviceType != null){//客户端 通知消息等连接过来的 mj
        	String userId = getParameter("userId", request);
        	String userName = getParameter("userName", request);
        	if (StringUtils.isNotBlank(userId)){
            	result = userMgt.loginById(userId);
        	} else {
            	if (StringUtils.isNotBlank(userName)){
            		userName = GlobalsTool.toChinseChar_GBK(userName);
            	}
            	result = userMgt.loginByName(userName);
        	}
        	String pwd = getParameter("pwd", request);
        	EmployeeBean beanReq  = (EmployeeBean)(((List)result.retVal).get(0));
            String sessionId = getParameter("JSESSIONID", request);
        	LoginBean beanCookie = (LoginBean)request.getSession().getAttribute("LoginBean");
            if (StringUtils.isNotBlank(sessionId) && beanReq != null && beanCookie != null && beanReq.getId().equals(beanCookie.getId())) {	// 带会话ID，并且当前页面登录的用户与客户端链接请求的一样
    	        if (adviceType != null && !"login".equals(adviceType) && OnlineUserInfo.checkUser(request)){ //客户端查看通知消息
    	        	if(adviceId != null &&adviceId.length() > 0){
    	        		new AdviceMgt().readOverById(adviceId);
    	        	}
    	            return new ActionForward("/MenuQueryAction.do?sysType=2&system=new&id="+adviceId+"&adviceType="+adviceType);
    	        } 
    	        if (OnlineUserInfo.checkUser(request)) { // 客户端点击首页链接
    	    		request.setAttribute("system", "new");
    	        	return getForward(request, mapping, "loginSucc");
    	        }
            } 
			loginForm.setName(beanReq.getSysName());
			loginForm.setPassword(beanReq.getPassword());
			loginForm.setSunCompany("1");
			loginForm.setLoc("zh_CN") ;
			loginForm.setStyle("style1") ;
			if(!pwd.equals(StringUtils.lowerCase(beanReq.getPassword()))){
				result.setRetCode(ErrorCanst.RET_NAME_PSW_ERROR) ;
			}
        }else {
        	String userName = loginForm.getName();
        	if("true".equals(getParameter("KK", request))){
        		userName = GlobalsTool.toChinseChar(userName);
        	}
        	result = userMgt.login(userName, loginForm.getPassword());
        	if ("true".equals(getParameter("KK", request)) && result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                List userList = (ArrayList) result.retVal;
                if (userList.size() > 0) {
                    EmployeeBean userBean = (EmployeeBean) userList.get(0);
                    if("1".equals(userBean.getIsPublic())){
                    	
		                 forward = getForward(request, mapping, "alert");
		                    //登陆失败提示信息
		                 EchoMessage.error().add("您的身份不允许登陆KK").setBackUrl("/login.jsp").
		                        setAlertRequest(request);
		                 return forward;
                    }
                }
        	}
        }
        
    	//密码验证成功
        if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            List userList = (ArrayList) result.retVal;
            if (userList.size() > 0) {
                EmployeeBean userBean = (EmployeeBean) userList.get(0);
                if (adviceType == null)	// KK链进来的，不用再验证了
                {
	                //IP网段检查
	                if((userBean.getIpstart()!=null&&userBean.getIpstart().length()>0) || (userBean.getIpend()!=null&&userBean.getIpend().length()>0)){
	                	String ipstart = userBean.getIpstart();
	                	String ipend = userBean.getIpend();
	                	String curIP = request.getRemoteAddr();
	                	
	                	if(ipstart==null||ipstart.length()==0){
	                		ipstart =userBean.getIpend().substring(0,userBean.getIpend().lastIndexOf("."))+".0";
	                	}
	                	if(ipend==null||ipend.length()==0){
	                		ipend =(userBean.getIpstart().substring(0,userBean.getIpstart().lastIndexOf("."))+".255");
	                	}
	                	String[] ipstarts = ipstart.split("\\.");
	                	String[] ipends = ipend.split("\\.");
	                	String[] curips = curIP.split("\\.");
	                	
	                	for(int i=0;i<curips.length;i++){
	                		int pi = Integer.parseInt(curips[i]);
	                		int ps = Integer.parseInt(ipstarts[i]);
	                		int pe = Integer.parseInt(ipends[i]);
	                		if(pi<ps || pi > pe){
	                			//IP段检查失败

	                			String terminal = null;
	            				if (null != getParameter("KK", request)) { // KK登录
	            					terminal = "KK";
	            				}
	                			
	                			new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", terminal, "FAIL", getMessage(request,
	                            	"common.msg.ipinvalid"), request);
	                			forward = getForward(request, mapping, "alert");
	                            EchoMessage.error().add(getMessage(request,
	                                "common.msg.ipinvalid")).setBackUrl("login.jsp").
	                                setAlertRequest(request);                            
	                            return forward;
	                		}
	                	}
	                	
	                }
	                //MACAddress检查
	                //系统的mac检查
	                String mac = (String)request.getSession().getAttribute("ClientMACValue");
	                boolean sysmac = "true".equals(BaseEnv.systemSet.get("MACFilter").getSetting());
	                if(sysmac){                	
	                	if(mac== null || mac.length()==0){
	                		sysmac = false;
	                	}
	                }
	                	
	                if((userBean.getMACAddress()!=null&&userBean.getMACAddress().length()>0) || sysmac){
	                	String macAddress=request.getParameter("strM1");//mac
	                	String macMD5 = request.getParameter("strM2") ; //md5 前台传过来MACAddress+随机数进行MD5加密后的密文
	                	String random = (String) request.getSession().getAttribute("ClientMACKey") ;//获取保存在session中随机数
	
	                	//对保存在数据库中的MACAddress+随机数 进行MD5加密
	                	MessageDigest md = MessageDigest.getInstance("MD5");
	                    md.update((macAddress+random).getBytes());
	                    String md5 = toHex(md.digest()) ;//把加密后字节数组转换成16进制字符
	
	                	if(null == macAddress || "".equals(macAddress) || mac.indexOf(macAddress) == -1 || macMD5==null || !macMD5.equals(md5)){

                			String terminal = null;
            				if (null != getParameter("KK", request)) { // KK登录
            					terminal = "KK";
            				}
	                		new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", terminal, "FAIL", getMessage(request,
	                    		"common.msg.onlyCompUsed"), request);
	                		forward = getForward(request, mapping, "alert");
	                        EchoMessage.error().add(getMessage(request,
	                            "common.msg.onlyCompUsed")).setBackUrl("login.jsp").
	                            setAlertRequest(request);
	                        return forward;
	                	}
	                }
	
	                //安且U遁检查
	                if(userBean.getUserKeyId()!=null && userBean.getUserKeyId().length()>0){
	                    String clientKey = request.getParameter("clientKey");
	                    String sClientKey = (String)request.getSession().getAttribute("ClientUSBKey");
	                    String ukId = request.getParameter("ukId");
	                    if(ukId == null || !userBean.getUserKeyId().equalsIgnoreCase(request.getParameter("ukId")) || clientKey==null || sClientKey==null || !UsbKey.compare(clientKey,UsbKey.StrEnc(sClientKey,UsbKey.keyId))){

                			String terminal = null;
            				if (null != getParameter("KK", request)) { // KK登录
            					terminal = "KK";
            				}
	                    	new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", terminal, "FAIL", getMessage(request,
	                			"login.msg.ukAuthError"), request);
	                    	forward = getForward(request, mapping, "alert");
	                        //安全U遁检查您不是合法用户
	                        EchoMessage.error().add(getMessage(request,
	                            "login.msg.ukAuthError")).setBackUrl("login.jsp").
	                            setAlertRequest(request);
	                        return forward;
	
	                    }
	                }
	            }
                
				if (null != getParameter("KK", request)) { // KK登录
					String port = GlobalsTool.getSysSetting("msgPort");
					String opResult = "SUCCESS";
					String reson = "";
					String returnStr = "op=login&code=0&port=" + port
							+ "&user_id=" + userBean.getId()
							+ "&user_name=" + userBean.getEmpFullName();
					if (null == MSGConnectCenter.instance) {
						returnStr += "&err=消息中心端口被占用，请联系管理员配置别的端口";
						opResult = "FAIL";
						reson = "消息中心端口被占用";
					} else {
						returnStr += "&err=";
					}
					request.setAttribute("msg", returnStr);

	                new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", "KK", opResult, reson, request);
					return getForward(request, mapping, "blank");
				}
                
               
                String curSys = userBean.getDefSys();
                boolean sysOk = false;
                String defSys = null;

                if (curSys != null && !curSys.equals("0")) {
                    List mainModulelist = getEnumerationItems("MainModule", request);
                    for (Object okp : mainModulelist) {
                        KeyPair kp = (KeyPair) okp;
                        if (kp.getValue().equals(curSys)) {
                            sysOk = true;
                        }
                        if (defSys == null && !kp.getValue().equals("0")) {
                            defSys = kp.getValue();
                        }
                    }
                } else {
                    List mainModulelist = getEnumerationItems("MainModule", request);
                    for (Object okp : mainModulelist) {
                        KeyPair kp = (KeyPair) okp;
                        if (!kp.getValue().equals("0")) {
                            defSys = kp.getValue();
                            break;
                        }
                    }
                }
                if (!sysOk) {
                    curSys = defSys;
                    userBean.setDefSys(curSys);
                }

                String sunCompanyId = loginForm.getSunCompany();
                //查找用户权限
                //这里先只查找角色权限
                //如果管理员，不用查角色，直接拥有所有权限
                /**
                 * 取权限
                 */
                ArrayList roleModuleList = new ArrayList();
                HashMap roleModuleScopeMap = new HashMap();
                ArrayList roles= new ArrayList();
                ArrayList allScopeRight = new ArrayList(); //记录应用于所有模块的范围权限
                String hiddenField = "";
                if(!"admin".equalsIgnoreCase(userBean.getSysName())){
                	roles = (ArrayList) getRoles(userBean, sunCompanyId);
                	//用户本身也是一个角色
                	RoleBean selfRb = new RoleBean();
                	selfRb.setId(userBean.getId());
                	selfRb.setRoleName(userBean.getEmpFullName());
                	selfRb.setRoleDesc(userBean.getEmpFullName());
                	selfRb.setHiddenField(userBean.getHiddenField());                	
                	roles.add(selfRb);
                	
                    for (Object o : roles) {
                        RoleBean rb = (RoleBean) o;                        
                        hiddenField +=rb.getHiddenField()+",";
                        
                        //查询角色模块权限
                        result = roleMgt.queryRoleModuleByRoleid(rb.getId(),userBean.getId());
                        if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        	new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", null, "FAIL", getMessage(request,
                				"login.msg.loginError"), request);
                            forward = getForward(request, mapping, "alert");
                            //登陆失败提示信息
                            EchoMessage.error().add(getMessage(request,"login.msg.loginError")).setBackUrl(
                                    "login.jsp").setAlertRequest(request);
                            return forward;
                        } else {
                        	//合并所有模块权限
                        	roleModuleList.addAll((List)result.getRetVal());
                        }
                        
                        //查询角色范围权限
                        result = roleMgt.queryRoleScopyByRoleid(rb.getId(),userBean.getId(),userBean.getEmpFullName(),userBean.getDepartmentCode());
                        if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        	new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", null, "FAIL", getMessage(request,
                				"login.msg.loginError"), request);
                            forward = getForward(request, mapping, "alert");
                            //登陆失败提示信息
                            EchoMessage.error().add(getMessage(request, "login.msg.loginError")).setBackUrl(
                                    "login.jsp").setAlertRequest(request);
                            return forward;
                        } else {
                        	//合并所有范围权限
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
                }
                //查询应用于所有模块范围权限------------------------------------
                result = roleMgt.queryAllModScope(userBean.getId(),userBean.getDepartmentCode()) ;
                if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
                	allScopeRight =(ArrayList)result.retVal ;
                }

                LoginBean loginBean = OnlineUserInfo.getLoginBean(userBean.getId(),userBean.getSysName());
                //loginBean.setHiddenField(userBean.getHiddenField()); //将用户自身的隐藏信息存入
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
                loginBean.setDepartCode(userBean.getDepartmentCode()) ;
                loginBean.setShopName(userBean.getShopName()) ;
                loginBean.setShopPwd(userBean.getShopPwd()) ;
                loginBean.setDefaultPage(userBean.getDefaultPage()) ;
                loginBean.setExtension(userBean.getExtension()) ;
                loginBean.setViewLeftMenu(userBean.getViewLeftMenu()) ;
                loginBean.setViewTopMenu(userBean.getViewTopMenu()) ;
                loginBean.setJessionid(request.getSession().getId()) ;
                loginBean.setDefStyle(loginForm.getStyle()) ;
                loginBean.setModuleId(userBean.getModuleId());
                loginBean.setTitleId(userBean.getTitleID());
                loginBean.setDefDesk(userBean.getDefDesk());
                loginBean.setEmail(userBean.getEmail());
                loginBean.setQq(userBean.getQq());
                loginBean.setShowDesk(userBean.getShowDesk());//显示桌面
                loginBean.setFirstShow(userBean.getFirstShow());
                loginBean.setShowWebNote(userBean.getShowWebNote());
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
                if(userBean.getPhoto()!=null && userBean.getPhoto().contains(":")){
                	loginBean.setPhoto(userBean.getPhoto().split(":")[0]) ;
                }else{
                	loginBean.setPhoto(userBean.getPhoto());
                }
                String sunCompany = loginForm.getSunCompany(); //获得分支机构ID
                if (!"".equals(sunCompany) && sunCompany != null) { //如果启用分支机构,
                    loginBean.setSunCompany(sunCompany); //将分支机构ID设置到loginBean
                    UserManageAction userMga = new UserManageAction();
                    Result rs = userMga.getsunCmpsClassCode(sunCompany);

                    List ls = (List) rs.getRetVal();
                    String sunCmpClassCode = null;
                    for (int i = 0; i < ls.size(); i++) {
                        sunCmpClassCode = ls.get(i).toString();
                    }
                    if (!"".equals(sunCmpClassCode) && sunCmpClassCode != null) {
                        loginBean.setSunCmpClassCode(sunCmpClassCode);
                    }
                }else{
                	loginBean.setSunCompany(null);
                	loginBean.setSunCmpClassCode(null);
                }
                request.setAttribute("system", getParameter("system", request)) ;
                //将用户bean放入session中
                request.getSession().removeAttribute("LoginBean");
                request.getSession().setAttribute("LoginBean", loginBean);
                //判断系统配置中是否隐藏邮件地址
        		boolean HideEmail =(boolean)GlobalsTool.getHideEmail();
        		request.getSession().setAttribute("HideEmail", HideEmail);
                //加入在线人员列表
        		String userAgent =request.getHeader("user-agent");
        		//判断是否来自微信企业号登陆
            	if(request.getParameter("wxqycheck")!=null){
            		WeixinApiMgt.getLoginBean(request,loginBean,userBean);
            		OnlineUserInfo.wxqyUserLogin(loginBean, request.getSession().getId());
            	}else{
            		OnlineUserInfo.userLogin(loginBean,request.getSession().getId());
            	}
            	//浏览器屏幕宽度
                String screenWidth = getParameter("screenWidth", request);
                request.getSession().setAttribute("screenWidth", screenWidth);
                //当前登录分支机构的会计期间
                String nowPeriod;
                int nowYear = -1;
                int nowMonth = -1;
                AccPeriodBean bean=null;
                Result rs=new SysAccMgt().getCurrPeriod(loginBean.getSunCmpClassCode());
                if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
                	nowPeriod="-1";
                }else{
                	 bean=(AccPeriodBean)rs.getRetVal();
                	 nowPeriod=String.valueOf(bean.getAccPeriod());
                	 nowYear=bean.getAccYear();
                	 nowMonth=bean.getAccMonth();
                }
                
                AccPeriodBean AccBean=null;
                rs=new SysAccMgt().getCurrAccPeriod(loginBean.getSunCmpClassCode());
                if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
                	AccBean=(AccPeriodBean)rs.getRetVal();
                }

                int nowDay = Calendar.getInstance().get(Calendar.DATE);
                
                //登录成功时，将一些sess信息放到BaseEnv中
                if (BaseEnv.sessionSet == null) {
                    BaseEnv.sessionSet = new Hashtable();
                }
                DynDBManager dbmgt = new DynDBManager();
                //职员分组ID
                String groupIds = (String) dbmgt.getGroupIds(userBean.getId()).getRetVal() ;
                loginBean.setGroupId(groupIds) ;
                boolean isLastSunCompany = dbmgt.getChildCount(
                        "tblSunCompanys.classCode",loginBean.getSunCmpClassCode()).getRetVal().toString().equals("0");
                Hashtable sessionSet = BaseEnv.sessionSet;
                Hashtable sess = new Hashtable();
                loginBean.setSessMap(sess); 
                sess.put("SCompanyID", loginBean.getSunCmpClassCode());
                sess.put("IsLastSCompany",isLastSunCompany);
                sess.put("AccPeriod",bean );
                sess.put("AccPeriodAcc",AccBean);
                sess.put("NowPeriod", nowPeriod);
                sess.put("NowYear", nowYear);
                if(nowYear==-1){//用于查询条件的默认值
                	sess.put("NowPeriodQ", "");
                    sess.put("NowYearQ", "");
                }else{
                	//用于查询的默认会计期间应该是上一个期间，
                	if(nowPeriod.equals("1")){
                		sess.put("NowPeriodQ", "12");
	                    sess.put("NowYearQ", nowYear-1);
                	}else{
	                	sess.put("NowPeriodQ", (Integer.parseInt(nowPeriod)-1)+"");
	                    sess.put("NowYearQ", nowYear);
                	}
                }
                sess.put("NowMonth", nowMonth);
                sess.put("NowDay", nowDay);
                sess.put("DepartmentCode", userBean.getDepartmentCode()==null?"":userBean.getDepartmentCode()) ;
                if(userBean.getDepartmentCode()!=null&&userBean.getDepartmentCode().length()>0){
                	Result rss=dbmgt.getDepartMent(userBean.getDepartmentCode());
                	if(rss.retCode==ErrorCanst.DEFAULT_SUCCESS&&rss.getRetVal()!=null){
                		String[] depart = (String[]) rss.getRetVal() ;
                		sess.put("DepartmentName",depart[0]);
                		loginBean.setDepartmentName(depart[0]) ;
                		loginBean.setDepartmentManager(depart[1]) ;
                	}
                }
                sess.put("UserId", loginBean.getId());
                sess.put("UserName", loginBean.getEmpFullName());
                sess.put("Local", GlobalsTool.getLocale(request));
                sess.put("BillOper", "");
                sessionSet.put(loginBean.getId(), sess);
                rs=dbmgt.getAccPeriodOverYear();
                sessionSet.put("AccPeriodOverYear", rs.getRetVal());
                
				request.getSession().setAttribute("NowPeriod", nowPeriod);
				request.getSession().setAttribute("NowYear", nowYear);
                request.getSession().setAttribute("SCompanyID",  loginBean.getSunCmpClassCode());

                //记录设置样式风格
                String style = loginForm.getStyle() ;
                loginBean.setDefStyle(style);
                String loc = loginForm.getLoc();
                loginBean.setDefLoc(loc) ;
                GlobalMgt glmgt = new GlobalMgt();
                glmgt.updateStyle(style,loginBean.getId());
                glmgt.updateLoc(loc, loginBean.getId()) ;
                this.parseDefValSet(loginBean.getId(), sess);
                
                //  更新内存中该用户的登录状态，即时通讯客户端需要用到
                EmployeeItem loginItem =MSGConnectCenter.employeeMap.get(loginBean.getId());
                if(loginItem!=null && loginItem.loginStatus==EmployeeItem.OFFLINE){
	                loginItem.loginType=EmployeeItem.BS_LOGIN;
	                loginItem.loginStatus=EmployeeItem.ONLINE;
	            	MSGConnectCenter.userStatus(loginItem.userId, loginItem.loginType, loginItem.loginStatus);
                }
                String rm = "";
                if (adviceType != null && "login".equals(adviceType)){ //客户端查看通知消息
                	rm = "KK链接首页登陆";
                }else if (adviceType != null && "advice".equals(adviceType)){ //客户端查看通知消息
                	rm = "KK链接消息登陆";
                }
                //记录登陆日志
                new LogManageMgt().addLog(loginBean.getId(), loginBean.getEmpFullName(), "LOGIN", null, "SUCCESS", rm , request);
                if (adviceType != null && !"login".equals(adviceType)){ //客户端查看通知消息
                		return new ActionForward("/MenuQueryAction.do?sysType="+loginBean.getDefSys()+"&system=new&id="+adviceId+"&adviceType="+adviceType);
                } else { //客户端跳转主页面,默认进入新页面
                	if(adviceType != null){ //判断是否是客户端进入主页面
                		request.setAttribute("system", "new");
                	}
                	forward = getForward(request, mapping, "loginSucc");
                }
            } else {

                forward = getForward(request, mapping, "alert");
                EchoMessage.error().add(getMessage(request,"login.msg.namepassworderror"))
                				   .setFirstFocus("password")
                				   .setAlertRequest(request);
            }
            
            
            
            
        }
        //错误的用户名密码
        else if (result.retCode == ErrorCanst.RET_NAME_PSW_ERROR) {
        	new LogManageMgt().addLog("", loginForm.getName(), "LOGIN", null, "FAIL", getMessage(request,
            	"login.msg.namepassworderror"), request);
            forward = getForward(request, mapping, "alert");
            //登陆失败提示信息
            EchoMessage.error().add(getMessage(request,
                                               "login.msg.namepassworderror")).setBackUrl("/login.jsp").setFirstFocus("password").setFunction("parent.parent.document.LoginForm.password.value=''").
                setAlertRequest(request);
        }
        // 错误的用户名密码
        else if (result.retCode == ErrorCanst.TIMER_COMPARE_ERROR) {
        	new LogManageMgt().addLog("", loginForm.getName(), "LOGIN", null, "FAIL", getMessage(request,
            	"login.msg.loginTimeError")+result.retVal, request);
        	
            forward = getForward(request, mapping, "alert");
            //登陆失败提示信息
            EchoMessage.error().add(getMessage(request,
                                               "login.msg.loginTimeError")+result.retVal).setBackUrl("/login.jsp").
                setAlertRequest(request);
        }// 用户停用
        else if (result.retCode == ErrorCanst.USER_STOP) {
        	new LogManageMgt().addLog("", loginForm.getName(), "LOGIN", null, "FAIL", "此用户帐号已停用", request);        	
            forward = getForward(request, mapping, "alert");
            //登陆失败提示信息
            EchoMessage.error().add("此用户帐号已停用").setBackUrl("/login.jsp").
                setAlertRequest(request);
        }
        //其他错误
        else {
        	new LogManageMgt().addLog("", loginForm.getName(), "LOGIN", null, "FAIL", getMessage(request,
            	"login.msg.loginError"), request);
            //返回到首页
            forward = getForward(request, mapping, "alert");
            //登陆失败提示信息
            EchoMessage.error().add(getMessage(request,
                                               "login.msg.loginError")).setBackUrl("/login.jsp").
                setAlertRequest(request);
        }
        return forward;
    }

    public static void parseDefValSet(String userId,Hashtable sess){
    	String sql=BaseEnv.systemSet.get("defaultValSet").getSetting();
    	AIODBManager aiomgt=new AIODBManager();
    	Result rs=aiomgt.sqlListMap(sql.replaceAll("@Sess:UserId", userId), new ArrayList(), 0, 0);
    	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    	ArrayList list=(ArrayList)rs.retVal;
	    	if(list.size()>0){
	    		HashMap map=(HashMap)list.get(0);
	    		sess.putAll(map);
	    	}
    	}
    }
    /**
     * 检查AIO客户端版本
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward checkVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String txtUrl="../../website/common/KK_version.txt";
    	FileReader read=new FileReader(txtUrl);
    	BufferedReader b=new BufferedReader(read);
    	String line;
    	String version="";
    	while((line=b.readLine())!=null){
    		version+=line;
    	}
    	String returnStr="op=check_update&version="+version;//返回的字符串
		request.setAttribute("msg", returnStr) ;
    	return getForward(request, mapping,"blank") ;
    }

    /**
     * 客户端校时
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward adjustTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

		String sClientTime = request.getParameter("time");
		Date dtClientTime = BaseDateFormat.parse(sClientTime, BaseDateFormat.yyyyMMddHHmmss);
    	Date dtServer = new Date();
    	dtServer.getTime();
    	long nTimeSpan = dtServer.getTime() - dtClientTime.getTime();
    	String returnStr="op=adjust_time&time_span="+nTimeSpan;
		request.setAttribute("msg", returnStr) ;
    	return getForward(request, mapping,"blank") ;
    }
    
    /**
     * 获取最新的会话ID
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward getSessionId(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

		String sUserId = request.getParameter("user_id");
		OnlineUser user = OnlineUserInfo.getUser(sUserId);
		String sSessionId = "";
		if (user != null) {
			sSessionId = user.session;
		}
		String userAgent = request.getParameter("user-agent");
    	String returnStr="op=get_session&session="+sSessionId;
		request.setAttribute("msg", returnStr) ;
    	return getForward(request, mapping,"blank") ;
    }

    /**
     * 客户端检查登录
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward checkUser(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
        Exception {
    	String username = request.getParameter("user") ;	/*客户端的登录用户名*/
		String password = request.getParameter("psw") ;		/*客户端的登录密码*/
		username = GlobalsTool.toChinseChar_2312(username) ;
		/*验证用户名和密码是否正确，密码是经过MD5加密的*/
		String userChName="";
		String userId = "" ;
 		int code=0;
		Result result = userMgt.queryEmployeeByEmpName(username.trim()) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			EmployeeBean employee = (EmployeeBean) result.retVal ;
			userId = employee.getId() ;
			userChName=employee.getEmpFullName();
			try {
				if(StringUtils.equalsIgnoreCase(password,employee.getPassword().trim())){
					code=0;
				}else{
					code=-2;
				}
//				MessageDigest md = MessageDigest.getInstance("MD5");
//				md.update(employee.getPassword().trim().getBytes()) ;
//				String md5Pwd = toHex(md.digest()) ;
//				if(StringUtils.equalsIgnoreCase(password,md5Pwd)){
//					code=0;
//				}else{
//					code=-2;
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			code=-1;
		}
		
		String returnStr="";//返回的字符串
		if(code==0){ //登录成功
			String server= "192.168.2.106";
			String port = GlobalsTool.getSysSetting("msgPort");
			
			returnStr="op=login&code=0&server="+server+"&port="+port+"&session="+userId+"&user_id="+userId+"&user_name="+userChName;
		}else{ 
			if(code==-1) //用户名不存在
				returnStr="op=login&code="+code+"&msg=用户名不存在";
			else   //密码错误
				returnStr="op=login&code="+code+"&msg=密码错误";
		}
		
		request.setAttribute("msg", returnStr) ;
    	return getForward(request, mapping,"blank") ;
    }
    /**
     * 根据登录的用户和所选分支机构查出相应权限
     * @param userBean EmployeeBean
     * @param sunCompanyId String
     * @return List
     */
    public static List getRoles(EmployeeBean userBean, String sunCompanyId) {
        List list = null;
        RoleMgt roleMgt = new RoleMgt();
        //如果是超级管理员，直接查出超级管理员角色，否则根据分支机构查角色
        if ("1".equals(userBean.getId())) {
            Result result = roleMgt.queryRoleById("1");
            list = (List) result.getRetVal();
        } else {
            UserManageAction userMgt = new UserManageAction();
            Result result = userMgt.getUserSunCompanyByUserId(userBean.getId(),sunCompanyId);
            if(result.retVal!=null && ((List)result.retVal).size()>0){
            	List<String[]> userSunCompanyList = (List<String[]>)result.retVal;
            	String roleIds = userSunCompanyList.get(0)[2];
            	list = (List) roleMgt.queryRolesByRoleIds(roleIds).getRetVal();
            }
        }
        return list;
    }

//    public Result getSunCompanyRoles(String userSunCompanyId){
//        Result rs = new Result();
//        List roleList = new ArrayList();
//        RoleMgt roleMgt = new RoleMgt();
//        for(Object o : getRoleIds(userSunCompanyId)){
//            String roleid = (String)o;
//            List list = (List)roleMgt.queryRoleById(roleid).getRetVal();
//            roleList.add(list.get(0));
//        }
//        rs.setRetVal(roleList);
//        return rs;
//    }

//    //取到某分支机构下某用户所属的所有角色id
//    public List getRoleIds(String userSunCompanyId){
//        List list = new ArrayList();
//        for(Object o : (List)getUserRoles().getRetVal()){
//            String[] userRole = (String[])o;
//            if(userSunCompanyId.equals(userRole[1])){
//                list.add(userRole[2]);
//            }
//        }
//        return list;
//    }

//    /**
//     * 取得所有tblUserRole表中记录
//     * @return Result
//     */
//    public Result getUserRoles() {
//        final Result rs = new Result();
//        int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//                session.doWork(new Work() {
//                    public void execute(Connection connection) throws
//                            SQLException {
//                        Connection conn = connection;
//                        try {
//                            String str_sel = "select * from tblUserRole";
//                            Statement st = conn.createStatement();
//                            ResultSet rss = st.executeQuery(str_sel);
//                            List ls = new ArrayList();
//                            while (rss.next()) {
//                                String[] userRole = new String[3];
//                                userRole[0] = rss.getString(1);
//                                userRole[1] = rss.getString(2);
//                                userRole[2] = rss.getString(3);
//                                ls.add(userRole);
//                            }
//                            rs.retVal = ls;
//                        } catch (SQLException ex) {
//                            rs.setRetCode(ErrorCanst.
//                                          DEFAULT_FAILURE);
//                            BaseEnv.log.error("Query data Error :" +
//                                              "select * from tblUserRole", ex);
//                            return;
//                        }
//                    }
//                });
//                return rs.getRetCode();
//            }
//        });
//        return rs;
//    }

    /**
     * 退出系统
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward logout(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {
    	LoginBean loginBean = this.getLoginBean(request);
    	
        //将登陆用户存放到在线人员中
        if (loginBean != null) {
            OnlineUserInfo.offlineUser(loginBean.getId());
            new LogManageMgt().addLog(loginBean.getId(), this.getLoginBean(request).getEmpFullName(), "LOGOUT", null, "SUCCESS", "", request);
        }
        System.out.println("LoginAction.logout() ---------- ");
        ActionForward forward = getForward(request, mapping, "indexPage");

        //更新内存中该用户的登录状态、登录方式，即时通讯客户端需要用到
       if(this.getLoginBean(request)!=null){
	        EmployeeItem loginItem =MSGConnectCenter.employeeMap.get(this.getLoginBean(request).getId());
	        if(loginItem!=null && loginItem.loginType==EmployeeItem.BS_LOGIN && loginItem.loginStatus==EmployeeItem.ONLINE){
		        loginItem.loginType=0;
		        loginItem.loginStatus=EmployeeItem.OFFLINE;
		    	MSGConnectCenter.userStatus(loginItem.userId, loginItem.loginType, loginItem.loginStatus);
	        }
        }
        Enumeration enu = request.getSession().getAttributeNames();
        while (enu.hasMoreElements()) {
            Object obj = enu.nextElement();
            if (obj != null && !org.apache.struts.Globals.LOCALE_KEY.equals(obj)) {
                request.getSession().removeAttribute(obj.toString());
            }
        }
        if (request.getParameter("type") != null && request.getParameter("type").equals("closeWindow")) {
            request.setAttribute("type", "closeWindow");
        }else if("closeAjax".equals(request.getParameter("type"))){
        	return getForward(request,mapping,"blank");
        }
        return forward;
    }

    public static String toHex(byte[] buffer) {
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

    public String getValue(String xml, String name) {
        try {
            return xml.substring(xml.indexOf("<" + name + ">") +
                                 ("<" + name + ">").length(),
                                 xml.indexOf("</" + name + ">"));
        } catch (Exception ex) {
            return null;
        }
    }


    public static void main(String[] args) {
        //加密狗检查
        //一百用户以下，每次登陆都检查，一百以上随机检查，检查比例不少于10分之1
        int cnum = 5999 * 2;
        //确定执行比例
        cnum = (100 - cnum / 100 + 1) / 2;

        for (int i = 0; i < 100; i++) {
            int rand = (int) (Math.random() * 10000) % 50;

            if (rand < cnum) {
                //执行检查
                System.out.println("----check");
                System.out.println("" + cnum + ":" + rand);
            }

            System.out.println("" + cnum + ":" + rand);
        }
    }

    //DOG_CHECK_HS_FUNCTION_START

    //DOG_CHECK_HS_FUNCTION_END
}


