package com.menyi.aio.web.upgrade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.menyi.aio.bean.RoleBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.menyi.aio.bean.RoleModuleBean;

import java.text.SimpleDateFormat;

import com.menyi.aio.bean.RoleScopeBean;
import com.menyi.aio.web.login.MOperation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.net.URLConnection;


import com.menyi.aio.web.upgrade.*;
import com.menyi.aio.web.userFunction.ReportData;

import java.util.List;

import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.UpdateInfo;

import java.util.Map;

import com.menyi.aio.web.usermanage.UserMgt;
import com.sun.org.apache.bcel.internal.generic.Type;

/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class UpgradeAction extends MgtBaseAction {
    public UpgradeAction() {
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
        int type = getParameterInt("type", request);
        ActionForward forward = null;
        if(type==5){
        	return softAbout(mapping,form,request,response) ;
        }else if(type==6){
        	return lanaguage(mapping,form,request,response) ;
        }
        switch (operation) {
        case OperationConst.OP_QUERY:
            forward = queryAccount(mapping, form, request, response);
            break;
        case OperationConst.OP_UPGRADE_PREPARE:
            forward = upgradePrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_UPGRADE:
            forward = upgrade(mapping, form, request, response);
            break;
        default:
            forward = upgradePrepare(mapping, form, request, response);
        }
        return forward;
    }
    
    

    @Override
	protected ActionForward doAuth(HttpServletRequest request, ActionMapping mapping) {
		// TODO Auto-generated method stub
    	int type = getParameterInt("type", request);
      	if(5!=type &&(SystemState.instance.dogState == SystemState.DOG_FORMAL ||
    		SystemState.instance.dogState == SystemState.DOG_EVALUATE )){
    		return super.doAuth(request, mapping);
    	}
    	return null;
		
	}

	/**
     * 添加前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward upgradePrepare(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws
        Exception {
        request.removeAttribute("operation");

        request.setAttribute("type", getParameter("type", request));
        String exig = request.getParameter("exig");
        if(!"true".equals(exig)) exig = "false";
        request.setAttribute("exig", exig);

        String step = getParameter("step", request);

        if (SystemState.instance.dogState == SystemState.DOG_EVALUATE) {
            request.setAttribute("code", "");
            return getForward(request, mapping, "upgradeNext");
        }
        request.setAttribute("code", SecurityLock.readKeyId());
        //设置系统的所有模块
        request.setAttribute("mainModule",getMainModuleItems(request));
        if ("one".equals(step)) {
            request.setAttribute("module", getParameter("module", request));
            request.setAttribute("userNum", getParameter("userNum", request));
            request.setAttribute("languageNum", getParameter("languageNum", request));
            request.setAttribute("functionNum", getParameter("functionNum", request));
            int codeLength = 1;
            if ("2".equals(getParameter("type", request))) {
                codeLength = SecurityLock.getKeyNum(getParameterInt("userNum", request));
            }
            request.setAttribute("codeNum", codeLength);
            return getForward(request, mapping, "upgradeNext");
        }
        return getForward(request, mapping, "upgrade");
    }
    
    protected ActionForward queryAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.removeAttribute("operation");
		
		UpgradeMgt infoMgt = new UpgradeMgt();
		Result rs= infoMgt.queryAccount(SystemState.instance.dogId);
		request.setAttribute("list", rs.retVal);
		return getForward(request, mapping, "queryAccount");
	}

    /**
     * 当枚举类型为MainModule时，由于必须受制于加密狗信息，所以从加密狗中读取
     * @param enumeration String
     * @return List
     */
    private List getMainModuleItems(HttpServletRequest request) {
        List list = new ArrayList();
        if(BaseEnv.enumerationMap == null)
            return list;
        EnumerateBean beans = (EnumerateBean)BaseEnv.enumerationMap.get("MainModule");
        for (int j = 0;beans !=null && j < beans.getEnumItem().size(); j++) {
            EnumerateItemBean eib = (EnumerateItemBean) beans.
                                    getEnumItem().get(j);
            KeyPair kp = new KeyPair();
            kp.setName(((KRLanguage) (eib.getDisplay())).get(getLocale(request).toString()));
            kp.setValue(eib.getEnumValue());
            list.add(kp);
        }
        return list;
    }


    /**
     * 添加前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward upgrade(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws
        Exception {

        ActionForward forward = getForward(request, mapping, "message");

        String[] aucodes = getParameters("aucode", request);
        int type = getParameterInt("type", request);

        //如果是紧急升级先验证管理员密码的正确性
        String exig = request.getParameter("exig");
        if ("true".equals(exig)) {
            UserMgt userMgt = new UserMgt();
            Result result = userMgt.loginNoMD5("admin", request.getParameter("password"));
            //登陆成功
            if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)result.getRetVal()).size()<1) {
                EchoMessage.error().add(getMessage(request, "upgrade.msg.passwordError")).setBackUrl("/UpgradeAction.do?type="+type+"&exig=true").
                    setRequest(request);
                return forward;
            } else if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                EchoMessage.error().add(getMessage(request, "login.msg.loginError")).setBackUrl("/UpgradeAction.do?type="+type+"&exig=true").
                    setRequest(request);
                return forward;
            }
        }

        int num = 0;
        boolean ret = false;

        //正式写加密狗
        if (type == 1) {
            num = getParameterInt("module", request);
            ret = SecurityLock.writeModule(num, aucodes[0], aucodes[1]);           
        } else if (type == 2) {
            num = getParameterInt("userNum", request);
            ret = SecurityLock.writeUserNum(num, aucodes);
        } else if (type == 3) {
            num = getParameterInt("languageNum", request);
            ret = SecurityLock.writeLanguageNum(num, aucodes);
        } else if (type == 4) {
            num = getParameterInt("functionNum", request);
            if(num == 51){
                ret = SecurityLock.writeUserDefine(aucodes[0], aucodes[1]);
            }else if(num == 52){
                ret = SecurityLock.writeFenZhi(aucodes[0], aucodes[1]);
            }else if(num == 53){
                ret = SecurityLock.writeMoreCurrency(aucodes[0], aucodes[1]);
            }else if(num == 54){
                ret = SecurityLock.writeProduct(aucodes[0], aucodes[1]);
            }
        }

        if (ret) {
        	EchoMessage.success().add(getMessage(request, "upgrade.msg.success")).setBackUrl("/forwardIndex.jsp").setRequest(request);
        	SystemState.instance.dogState = SystemState.DOG_RESTART;
        } else {
            if ("true".equals(exig)) {
                EchoMessage.error().add(getMessage(request, "upgrade.msg.fature")).setBackUrl("/UpgradeAction.do?type="+type+"&exig=true").
                    setRequest(request);
            }else{
            	EchoMessage.error().add(getMessage(request, "upgrade.msg.fature")).setBackUrl("/UpgradeAction.do?type="+type+"").
                setRequest(request);
            }
        }
        request.removeAttribute("operation");
        return forward;
    }




    protected ActionForward softAbout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

    	request.removeAttribute("operation");
        if(SystemState.instance.dogState == SystemState.DOG_EVALUATE){
            request.setAttribute("code", "");
        }else{
            request.setAttribute("code", SystemState.instance.dogId);
        }
        
        String session = SystemState.instance.dogId+":"+System.currentTimeMillis();
		byte[] b = ClientConnection.encryptMode(session.getBytes());
		
        session = ClientConnection.bytesToHexString(b).toUpperCase();
        request.setAttribute("validcode", session);
        
        
        UpgradeMgt infoMgt = new UpgradeMgt();
        Result result = infoMgt.findLastInfo();
        UpdateInfo info = (UpdateInfo) result.getRetVal();
		request.setAttribute("version_id",info.getVersionId() );
		request.setAttribute("order_id",info.getOrderId() );
		request.setAttribute("languageNum", SystemState.instance.languageNum );
		//获取AIOV7产品动态信息
		String msg  = new HttpTransfer().postHttp("http://www.bol88.com/msgsvr?op=upgradeMSG","");
		ProductInfoBean infos = new ProductInfoBean();
		if(msg != ""){
			Gson gson = new Gson();
			ProductInfoBean infos1 = gson.fromJson(msg,ProductInfoBean.class);
			ArrayList<String> list = new ArrayList<String>();
			String [] infoArr = infos1.getMsg();
			for (String str : infoArr) {
				list.add(str);
			}
			request.setAttribute("productInfos",list);
		}
		
		/*判断title*/
    	Result rs = new PublicMgt().getCompany();
    	ArrayList rsRs = (ArrayList)rs.retVal;
		if(rsRs !=null && rsRs.size() >0){
			request.setAttribute("companyName", GlobalsTool.get(rsRs.get(0),1).toString());
		}
		
		return getForward(request, mapping, "about");
	}
    
    
    protected ActionForward lanaguage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

    	request.removeAttribute("operation");
    	UpgradeMgt infoMgt = new UpgradeMgt();
    	
    	if("true".equals(request.getParameter("isSave"))){
	    	String def = request.getParameter("defaultLang");
	    	if(def !=null && def.length() >0){
	    		infoMgt.defaultLang(request.getParameter("defaultLang"));
	    	}
	    	String lang[] = request.getParameterValues("saveLang");
	    	if(lang==null){lang = new String[0];}
	    	infoMgt.saveLang(lang);
	    	
	    	EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/UpgradeAction.do?type=6").setRequest(request);
	    	return getForward(request,mapping,"alert");
    	}
    	
    	
    	Result rs = infoMgt.queryLanuage();
    	
    	HashMap map = new HashMap();
    	for(int i=0;rs.retVal != null && i<((ArrayList)rs.retVal).size();i++){
    		Object o = ((ArrayList)rs.retVal).get(i);
    		map.put(((Object[])o)[0], ((Object[])o)[0]);
    	}    	
    	request.setAttribute("userLocale",map);
    	
    	request.setAttribute("sysLocale",getSystemLang());
    	request.setAttribute("langNum", SystemState.instance.getLanguageNum());
    	
        
		return getForward(request, mapping, "language");
	}
    
    private List getSystemLang(){
    	ArrayList list = new ArrayList();
    	try{
    	BufferedReader br = new BufferedReader(new InputStreamReader(new
                FileInputStream("../../config/language.xml")));

        String str;
        while ((str = br.readLine()) != null) {
            if (str.indexOf("<language>") > -1 &&
                str.indexOf("</language>") > -1) {
                String name = str.substring(str.indexOf("<language>") +
                                            "<language>".length(),
                                            str.indexOf("</language>"));
                list.add(name);
            }
        }
    	}catch(Exception e){}
        return list;
    }


}
