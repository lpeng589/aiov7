package com.menyi.aio.web.userFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 * 	<p>该类主要用于查询和设置缺省语言</p>
 * 
 *  @Copyright: 科荣软件
 *	
 *  @Date:2009-9-18
 *
 *	@Author 文小钱
 */
public class LanguageSettingAction extends MgtBaseAction{

	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        ActionForward forward = null ;
        
        switch(operation){
        	case OperationConst.OP_LANGUAGE_SETTING_PREPARE:
        		forward = languageSettingPrepare(mapping, form, request, response);
        		break ;
        	case OperationConst.OP_LANGUAGE_SETTING:
        		forward = languageSetting(mapping, form, request, response);
        		break ;
        	default:
        		forward = languageSettingQuery(mapping, form, request, response);
            
        }
		return forward;
	}
	
	/**
     * 语言设置前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward languageSettingPrepare(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
    	return null ;
    }
    
    /**
     * 设置缺省语言
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward languageSetting(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
    	
    	//得到新设置的缺省语言
    	String defLoc = request.getParameter("locale");
    	
    	//使用流读取配置文件
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("../../config/language.xml")));

		String str = null;
		StringBuilder sb = new StringBuilder();
		while ((str = br.readLine()) != null) {
			//读取含有缺省的行
			if (str.indexOf("<defLanguage>") > -1 && str.indexOf("</defLanguage>") > -1) 
			{
				String name = str.substring(str.indexOf("<defLanguage>")
						+ "<defLanguage>".length(), str.indexOf("</defLanguage>"));
				//将缺省语言替换新的缺省语言
				str = str.replace(name, defLoc);
				sb.append(str).append("\n");
			} 
			else 
			{
				sb.append(str).append("\n");
			}
		}
		//重新覆盖配置文件
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("../../config/language.xml")));
		bw.write(sb.toString());
		br.close();
		bw.close();
		//更新内存缺省语言设置
		InitMenData init = new InitMenData();
		init.initLanguage(request.getSession().getServletContext());
		EchoMessage.success().add(
				getMessage(request, "common.msg.updateSuccess")).setBackUrl(
				"/LanguageSetting.do").setAlertRequest(request);
		return getForward(request, mapping, "message");
    }
    
    /**
     * 查询系统支持语言，用户拥有语言，缺省语言
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward languageSettingQuery(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
    	
    	Hashtable lanTable = new Hashtable(); //系统支持的语言
    	Hashtable usedTable = new Hashtable() ; //用户拥有的语言
    	
    	//读取配置文件
    	BufferedReader br = new BufferedReader(new InputStreamReader(
    										new FileInputStream("../../config/language.xml")));
    	String str = null ;
    	while((str=br.readLine())!=null){
    		//系统支持语言
    		if (str.indexOf("<language>") > -1 && str.indexOf("</language>") > -1) 
    		{
    			String name = str.substring(str.indexOf("<language>") + "<language>".length(), 
    																			str.indexOf("</language>"));
                Locale loc = getLocale(name);
    			lanTable.put(name, loc.getDisplayName()) ;
    		}
    		//用户拥有语言
    		else if(str.indexOf("<usedLanguage>") > -1 && str.indexOf("</usedLanguage>") > -1)
    		{
    			String name = str.substring(str.indexOf("<usedLanguage>") + "<usedLanguage>".length(),
    																			str.indexOf("</usedLanguage>"));
                Locale loc = getLocale(name);
                usedTable.put(name, loc.getDisplayName()) ;
    		}
    		//缺省语言
    		else if(str.indexOf("<defLanguage>") > -1 && str.indexOf("</defLanguage>") > -1)
    		{
    			String name = str.substring(str.indexOf("<defLanguage>") + "<defLanguage>".length(),
    																			str.indexOf("</defLanguage>"));
                Locale loc = getLocale(name);
                request.setAttribute("defLanguage", loc.getDisplayName()) ;
    		}
    	}
    	br.close() ;
    	request.setAttribute("language", lanTable) ;
    	request.setAttribute("usedLanguage", usedTable) ;
    	
    	return  getForward(request, mapping, "languageList");
    }

    /**
     * 获取本地语言
     * @param ls
     * @return
     */
    private Locale getLocale(String ls) {
        Locale locale = Locale.getDefault();
        if (ls != null && ls.length() != 0) 
        {
            String[] params = ls.split("_");
            if (params.length == 3) {
                locale = new Locale(params[0], params[1], params[2]);
            } else if (params.length == 2) {
                locale = new Locale(params[0], params[1]);
            } else {
                locale = new Locale(params[0]);
            }
        }
        return locale;
    }
	
}
