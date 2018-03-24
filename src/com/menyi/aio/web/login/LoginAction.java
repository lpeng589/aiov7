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
 * <p>Title:��½ </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class LoginAction extends BaseAction {
    UserMgt userMgt = new UserMgt();
    RoleMgt roleMgt = new RoleMgt();
    public LoginAction() {

    }

    /**
     * ��½�������Ȩ���ж�
     * @param req HttpServletRequest
     * @param mapping ActionMapping
     * @return ActionForward
     */
    protected ActionForward doAuth(HttpServletRequest req,
                                   ActionMapping mapping) {
        return null;
    }


    /**
     * exe ��������ں���
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


    	
        //���ݲ�ͬ�������ͷ������ͬ��������
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_LOGIN:
        	
        	String OpType=request.getParameter("type");
        	if("client".equals(OpType)){
        		String OpValue=request.getParameter("op");
        		if("login".equals(OpValue)){	// ��֤�û���������
        			forward=checkUser(mapping,form,request,response);
        		} else if ("check_update".equals(OpValue)) { //���AIO�汾�Ƿ��и���
        			forward=checkVersion(mapping,form,request,response);
        		} else if ("adjust_time".equals(OpValue)) { // �ͻ���Уʱ
        			forward=adjustTime(mapping,form,request,response);
        		} else if ("get_session".equals(OpValue)) { // ��ȡ���»Ự
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
        default: //Ĭ�Ϸ�����ҳ
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
    	/*�ж�title��link*/
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
     * ��½ϵͳ
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
    	//�ж��û����Ƿ񳬹�����
    	if(SystemState.instance.userState != 0 && !"admin".equals(loginForm.getName())){
    		if(SystemState.instance.userState == SystemState.DOG_ERROR_USER){
    			EchoMessage.error().add("ERPϵͳ�û��������������("+SystemState.instance.userNum+"), \\n����admin�ʺŵ�½ɾ������ϵͳ�û�������ϵͳ")
					.setNoBackButton() .setAlertRequest(request);
    			return getForward(request, mapping, "alert"); 			
    		}else if(SystemState.instance.userState == SystemState.DOG_ERROR_USER_OA){
    			EchoMessage.error().add("OAϵͳ�û��������������("+SystemState.instance.oaUserNum+"), \\n����admin�ʺŵ�½ɾ������ϵͳ�û�������ϵͳ")
					.setNoBackButton() .setAlertRequest(request);
    			return getForward(request, mapping, "alert");        			
    		}else if(SystemState.instance.userState == SystemState.DOG_ERROR_USER_CRM){
    			EchoMessage.error().add("CRMϵͳ�û��������������("+SystemState.instance.crmUserNum+"), \\n����admin�ʺŵ�½ɾ������ϵͳ�û�������ϵͳ")
					.setNoBackButton() .setAlertRequest(request);
    			return getForward(request, mapping, "alert");
    		}else if(SystemState.instance.userState == SystemState.DOG_ERROR_USER_HR){
    			
    		}else if(SystemState.instance.userState == SystemState.DOG_ERROR_USER_ORDERS){
    			
    		}
    	}
        
        /*�컪*/
    	request.setAttribute("newBody", getParameter("newBody", request));
        
        Result result = null;
        String adviceId = getParameter("id",request);
        String adviceType = getParameter("adviceType", request);

        if (adviceType != null){//�ͻ��� ֪ͨ��Ϣ�����ӹ����� mj
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
            if (StringUtils.isNotBlank(sessionId) && beanReq != null && beanCookie != null && beanReq.getId().equals(beanCookie.getId())) {	// ���ỰID�����ҵ�ǰҳ���¼���û���ͻ������������һ��
    	        if (adviceType != null && !"login".equals(adviceType) && OnlineUserInfo.checkUser(request)){ //�ͻ��˲鿴֪ͨ��Ϣ
    	        	if(adviceId != null &&adviceId.length() > 0){
    	        		new AdviceMgt().readOverById(adviceId);
    	        	}
    	            return new ActionForward("/MenuQueryAction.do?sysType=2&system=new&id="+adviceId+"&adviceType="+adviceType);
    	        } 
    	        if (OnlineUserInfo.checkUser(request)) { // �ͻ��˵����ҳ����
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
		                    //��½ʧ����ʾ��Ϣ
		                 EchoMessage.error().add("������ݲ������½KK").setBackUrl("/login.jsp").
		                        setAlertRequest(request);
		                 return forward;
                    }
                }
        	}
        }
        
    	//������֤�ɹ�
        if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            List userList = (ArrayList) result.retVal;
            if (userList.size() > 0) {
                EmployeeBean userBean = (EmployeeBean) userList.get(0);
                if (adviceType == null)	// KK�������ģ���������֤��
                {
	                //IP���μ��
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
	                			//IP�μ��ʧ��

	                			String terminal = null;
	            				if (null != getParameter("KK", request)) { // KK��¼
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
	                //MACAddress���
	                //ϵͳ��mac���
	                String mac = (String)request.getSession().getAttribute("ClientMACValue");
	                boolean sysmac = "true".equals(BaseEnv.systemSet.get("MACFilter").getSetting());
	                if(sysmac){                	
	                	if(mac== null || mac.length()==0){
	                		sysmac = false;
	                	}
	                }
	                	
	                if((userBean.getMACAddress()!=null&&userBean.getMACAddress().length()>0) || sysmac){
	                	String macAddress=request.getParameter("strM1");//mac
	                	String macMD5 = request.getParameter("strM2") ; //md5 ǰ̨������MACAddress+���������MD5���ܺ������
	                	String random = (String) request.getSession().getAttribute("ClientMACKey") ;//��ȡ������session�������
	
	                	//�Ա��������ݿ��е�MACAddress+����� ����MD5����
	                	MessageDigest md = MessageDigest.getInstance("MD5");
	                    md.update((macAddress+random).getBytes());
	                    String md5 = toHex(md.digest()) ;//�Ѽ��ܺ��ֽ�����ת����16�����ַ�
	
	                	if(null == macAddress || "".equals(macAddress) || mac.indexOf(macAddress) == -1 || macMD5==null || !macMD5.equals(md5)){

                			String terminal = null;
            				if (null != getParameter("KK", request)) { // KK��¼
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
	
	                //����U�ݼ��
	                if(userBean.getUserKeyId()!=null && userBean.getUserKeyId().length()>0){
	                    String clientKey = request.getParameter("clientKey");
	                    String sClientKey = (String)request.getSession().getAttribute("ClientUSBKey");
	                    String ukId = request.getParameter("ukId");
	                    if(ukId == null || !userBean.getUserKeyId().equalsIgnoreCase(request.getParameter("ukId")) || clientKey==null || sClientKey==null || !UsbKey.compare(clientKey,UsbKey.StrEnc(sClientKey,UsbKey.keyId))){

                			String terminal = null;
            				if (null != getParameter("KK", request)) { // KK��¼
            					terminal = "KK";
            				}
	                    	new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", terminal, "FAIL", getMessage(request,
	                			"login.msg.ukAuthError"), request);
	                    	forward = getForward(request, mapping, "alert");
	                        //��ȫU�ݼ�������ǺϷ��û�
	                        EchoMessage.error().add(getMessage(request,
	                            "login.msg.ukAuthError")).setBackUrl("login.jsp").
	                            setAlertRequest(request);
	                        return forward;
	
	                    }
	                }
	            }
                
				if (null != getParameter("KK", request)) { // KK��¼
					String port = GlobalsTool.getSysSetting("msgPort");
					String opResult = "SUCCESS";
					String reson = "";
					String returnStr = "op=login&code=0&port=" + port
							+ "&user_id=" + userBean.getId()
							+ "&user_name=" + userBean.getEmpFullName();
					if (null == MSGConnectCenter.instance) {
						returnStr += "&err=��Ϣ���Ķ˿ڱ�ռ�ã�����ϵ����Ա���ñ�Ķ˿�";
						opResult = "FAIL";
						reson = "��Ϣ���Ķ˿ڱ�ռ��";
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
                //�����û�Ȩ��
                //������ֻ���ҽ�ɫȨ��
                //�������Ա�����ò��ɫ��ֱ��ӵ������Ȩ��
                /**
                 * ȡȨ��
                 */
                ArrayList roleModuleList = new ArrayList();
                HashMap roleModuleScopeMap = new HashMap();
                ArrayList roles= new ArrayList();
                ArrayList allScopeRight = new ArrayList(); //��¼Ӧ��������ģ��ķ�ΧȨ��
                String hiddenField = "";
                if(!"admin".equalsIgnoreCase(userBean.getSysName())){
                	roles = (ArrayList) getRoles(userBean, sunCompanyId);
                	//�û�����Ҳ��һ����ɫ
                	RoleBean selfRb = new RoleBean();
                	selfRb.setId(userBean.getId());
                	selfRb.setRoleName(userBean.getEmpFullName());
                	selfRb.setRoleDesc(userBean.getEmpFullName());
                	selfRb.setHiddenField(userBean.getHiddenField());                	
                	roles.add(selfRb);
                	
                    for (Object o : roles) {
                        RoleBean rb = (RoleBean) o;                        
                        hiddenField +=rb.getHiddenField()+",";
                        
                        //��ѯ��ɫģ��Ȩ��
                        result = roleMgt.queryRoleModuleByRoleid(rb.getId(),userBean.getId());
                        if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        	new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", null, "FAIL", getMessage(request,
                				"login.msg.loginError"), request);
                            forward = getForward(request, mapping, "alert");
                            //��½ʧ����ʾ��Ϣ
                            EchoMessage.error().add(getMessage(request,"login.msg.loginError")).setBackUrl(
                                    "login.jsp").setAlertRequest(request);
                            return forward;
                        } else {
                        	//�ϲ�����ģ��Ȩ��
                        	roleModuleList.addAll((List)result.getRetVal());
                        }
                        
                        //��ѯ��ɫ��ΧȨ��
                        result = roleMgt.queryRoleScopyByRoleid(rb.getId(),userBean.getId(),userBean.getEmpFullName(),userBean.getDepartmentCode());
                        if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        	new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", null, "FAIL", getMessage(request,
                				"login.msg.loginError"), request);
                            forward = getForward(request, mapping, "alert");
                            //��½ʧ����ʾ��Ϣ
                            EchoMessage.error().add(getMessage(request, "login.msg.loginError")).setBackUrl(
                                    "login.jsp").setAlertRequest(request);
                            return forward;
                        } else {
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
                }
                //��ѯӦ��������ģ�鷶ΧȨ��------------------------------------
                result = roleMgt.queryAllModScope(userBean.getId(),userBean.getDepartmentCode()) ;
                if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
                	allScopeRight =(ArrayList)result.retVal ;
                }

                LoginBean loginBean = OnlineUserInfo.getLoginBean(userBean.getId(),userBean.getSysName());
                //loginBean.setHiddenField(userBean.getHiddenField()); //���û������������Ϣ����
                loginBean.setHiddenField(hiddenField); //�����ɫ�����������Ϣ��
                loginBean.setMobile(userBean.getMobile());
                loginBean.setDefSys(userBean.getDefSys());
                loginBean.setLoginTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                loginBean.setRoleModuleList(roleModuleList);
                loginBean.setRoleModuleScopeMap(roleModuleScopeMap);
                loginBean.setAllScopeRight(allScopeRight);
                loginBean.setEmpFullName(userBean.getEmpFullName());
                loginBean.setLbxMonitorCh(userBean.getLbxMonitorCh());//����ͨ���趨
                loginBean.setTelPrefix(userBean.getTelPrefix());//�绰ǰ׺
                loginBean.setTelAreaCode(userBean.getTelAreaCode());//�û���λ
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
                loginBean.setShowDesk(userBean.getShowDesk());//��ʾ����
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
                String sunCompany = loginForm.getSunCompany(); //��÷�֧����ID
                if (!"".equals(sunCompany) && sunCompany != null) { //������÷�֧����,
                    loginBean.setSunCompany(sunCompany); //����֧����ID���õ�loginBean
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
                //���û�bean����session��
                request.getSession().removeAttribute("LoginBean");
                request.getSession().setAttribute("LoginBean", loginBean);
                //�ж�ϵͳ�������Ƿ������ʼ���ַ
        		boolean HideEmail =(boolean)GlobalsTool.getHideEmail();
        		request.getSession().setAttribute("HideEmail", HideEmail);
                //����������Ա�б�
        		String userAgent =request.getHeader("user-agent");
        		//�ж��Ƿ�����΢����ҵ�ŵ�½
            	if(request.getParameter("wxqycheck")!=null){
            		WeixinApiMgt.getLoginBean(request,loginBean,userBean);
            		OnlineUserInfo.wxqyUserLogin(loginBean, request.getSession().getId());
            	}else{
            		OnlineUserInfo.userLogin(loginBean,request.getSession().getId());
            	}
            	//�������Ļ���
                String screenWidth = getParameter("screenWidth", request);
                request.getSession().setAttribute("screenWidth", screenWidth);
                //��ǰ��¼��֧�����Ļ���ڼ�
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
                
                //��¼�ɹ�ʱ����һЩsess��Ϣ�ŵ�BaseEnv��
                if (BaseEnv.sessionSet == null) {
                    BaseEnv.sessionSet = new Hashtable();
                }
                DynDBManager dbmgt = new DynDBManager();
                //ְԱ����ID
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
                if(nowYear==-1){//���ڲ�ѯ������Ĭ��ֵ
                	sess.put("NowPeriodQ", "");
                    sess.put("NowYearQ", "");
                }else{
                	//���ڲ�ѯ��Ĭ�ϻ���ڼ�Ӧ������һ���ڼ䣬
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

                //��¼������ʽ���
                String style = loginForm.getStyle() ;
                loginBean.setDefStyle(style);
                String loc = loginForm.getLoc();
                loginBean.setDefLoc(loc) ;
                GlobalMgt glmgt = new GlobalMgt();
                glmgt.updateStyle(style,loginBean.getId());
                glmgt.updateLoc(loc, loginBean.getId()) ;
                this.parseDefValSet(loginBean.getId(), sess);
                
                //  �����ڴ��и��û��ĵ�¼״̬����ʱͨѶ�ͻ�����Ҫ�õ�
                EmployeeItem loginItem =MSGConnectCenter.employeeMap.get(loginBean.getId());
                if(loginItem!=null && loginItem.loginStatus==EmployeeItem.OFFLINE){
	                loginItem.loginType=EmployeeItem.BS_LOGIN;
	                loginItem.loginStatus=EmployeeItem.ONLINE;
	            	MSGConnectCenter.userStatus(loginItem.userId, loginItem.loginType, loginItem.loginStatus);
                }
                String rm = "";
                if (adviceType != null && "login".equals(adviceType)){ //�ͻ��˲鿴֪ͨ��Ϣ
                	rm = "KK������ҳ��½";
                }else if (adviceType != null && "advice".equals(adviceType)){ //�ͻ��˲鿴֪ͨ��Ϣ
                	rm = "KK������Ϣ��½";
                }
                //��¼��½��־
                new LogManageMgt().addLog(loginBean.getId(), loginBean.getEmpFullName(), "LOGIN", null, "SUCCESS", rm , request);
                if (adviceType != null && !"login".equals(adviceType)){ //�ͻ��˲鿴֪ͨ��Ϣ
                		return new ActionForward("/MenuQueryAction.do?sysType="+loginBean.getDefSys()+"&system=new&id="+adviceId+"&adviceType="+adviceType);
                } else { //�ͻ�����ת��ҳ��,Ĭ�Ͻ�����ҳ��
                	if(adviceType != null){ //�ж��Ƿ��ǿͻ��˽�����ҳ��
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
        //������û�������
        else if (result.retCode == ErrorCanst.RET_NAME_PSW_ERROR) {
        	new LogManageMgt().addLog("", loginForm.getName(), "LOGIN", null, "FAIL", getMessage(request,
            	"login.msg.namepassworderror"), request);
            forward = getForward(request, mapping, "alert");
            //��½ʧ����ʾ��Ϣ
            EchoMessage.error().add(getMessage(request,
                                               "login.msg.namepassworderror")).setBackUrl("/login.jsp").setFirstFocus("password").setFunction("parent.parent.document.LoginForm.password.value=''").
                setAlertRequest(request);
        }
        // ������û�������
        else if (result.retCode == ErrorCanst.TIMER_COMPARE_ERROR) {
        	new LogManageMgt().addLog("", loginForm.getName(), "LOGIN", null, "FAIL", getMessage(request,
            	"login.msg.loginTimeError")+result.retVal, request);
        	
            forward = getForward(request, mapping, "alert");
            //��½ʧ����ʾ��Ϣ
            EchoMessage.error().add(getMessage(request,
                                               "login.msg.loginTimeError")+result.retVal).setBackUrl("/login.jsp").
                setAlertRequest(request);
        }// �û�ͣ��
        else if (result.retCode == ErrorCanst.USER_STOP) {
        	new LogManageMgt().addLog("", loginForm.getName(), "LOGIN", null, "FAIL", "���û��ʺ���ͣ��", request);        	
            forward = getForward(request, mapping, "alert");
            //��½ʧ����ʾ��Ϣ
            EchoMessage.error().add("���û��ʺ���ͣ��").setBackUrl("/login.jsp").
                setAlertRequest(request);
        }
        //��������
        else {
        	new LogManageMgt().addLog("", loginForm.getName(), "LOGIN", null, "FAIL", getMessage(request,
            	"login.msg.loginError"), request);
            //���ص���ҳ
            forward = getForward(request, mapping, "alert");
            //��½ʧ����ʾ��Ϣ
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
     * ���AIO�ͻ��˰汾
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
    	String returnStr="op=check_update&version="+version;//���ص��ַ���
		request.setAttribute("msg", returnStr) ;
    	return getForward(request, mapping,"blank") ;
    }

    /**
     * �ͻ���Уʱ
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
     * ��ȡ���µĻỰID
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
     * �ͻ��˼���¼
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
    	String username = request.getParameter("user") ;	/*�ͻ��˵ĵ�¼�û���*/
		String password = request.getParameter("psw") ;		/*�ͻ��˵ĵ�¼����*/
		username = GlobalsTool.toChinseChar_2312(username) ;
		/*��֤�û����������Ƿ���ȷ�������Ǿ���MD5���ܵ�*/
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
		
		String returnStr="";//���ص��ַ���
		if(code==0){ //��¼�ɹ�
			String server= "192.168.2.106";
			String port = GlobalsTool.getSysSetting("msgPort");
			
			returnStr="op=login&code=0&server="+server+"&port="+port+"&session="+userId+"&user_id="+userId+"&user_name="+userChName;
		}else{ 
			if(code==-1) //�û���������
				returnStr="op=login&code="+code+"&msg=�û���������";
			else   //�������
				returnStr="op=login&code="+code+"&msg=�������";
		}
		
		request.setAttribute("msg", returnStr) ;
    	return getForward(request, mapping,"blank") ;
    }
    /**
     * ���ݵ�¼���û�����ѡ��֧���������ӦȨ��
     * @param userBean EmployeeBean
     * @param sunCompanyId String
     * @return List
     */
    public static List getRoles(EmployeeBean userBean, String sunCompanyId) {
        List list = null;
        RoleMgt roleMgt = new RoleMgt();
        //����ǳ�������Ա��ֱ�Ӳ����������Ա��ɫ��������ݷ�֧�������ɫ
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

//    //ȡ��ĳ��֧������ĳ�û����������н�ɫid
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
//     * ȡ������tblUserRole���м�¼
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
     * �˳�ϵͳ
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
    	
        //����½�û���ŵ�������Ա��
        if (loginBean != null) {
            OnlineUserInfo.offlineUser(loginBean.getId());
            new LogManageMgt().addLog(loginBean.getId(), this.getLoginBean(request).getEmpFullName(), "LOGOUT", null, "SUCCESS", "", request);
        }
        System.out.println("LoginAction.logout() ---------- ");
        ActionForward forward = getForward(request, mapping, "indexPage");

        //�����ڴ��и��û��ĵ�¼״̬����¼��ʽ����ʱͨѶ�ͻ�����Ҫ�õ�
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
        //���ܹ����
        //һ���û����£�ÿ�ε�½����飬һ�����������飬������������10��֮1
        int cnum = 5999 * 2;
        //ȷ��ִ�б���
        cnum = (100 - cnum / 100 + 1) / 2;

        for (int i = 0; i < 100; i++) {
            int rand = (int) (Math.random() * 10000) % 50;

            if (rand < cnum) {
                //ִ�м��
                System.out.println("----check");
                System.out.println("" + cnum + ":" + rand);
            }

            System.out.println("" + cnum + ":" + rand);
        }
    }

    //DOG_CHECK_HS_FUNCTION_START

    //DOG_CHECK_HS_FUNCTION_END
}


