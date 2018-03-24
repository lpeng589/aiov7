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
 * 	<p>������Ҫ���ڲ�ѯ������ȱʡ����</p>
 * 
 *  @Copyright: �������
 *	
 *  @Date:2009-9-18
 *
 *	@Author ��СǮ
 */
public class LanguageSettingAction extends MgtBaseAction{

	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//���ݲ�ͬ�������ͷ������ͬ��������
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
     * ��������ǰ��׼��
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
     * ����ȱʡ����
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
    	
    	//�õ������õ�ȱʡ����
    	String defLoc = request.getParameter("locale");
    	
    	//ʹ������ȡ�����ļ�
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("../../config/language.xml")));

		String str = null;
		StringBuilder sb = new StringBuilder();
		while ((str = br.readLine()) != null) {
			//��ȡ����ȱʡ����
			if (str.indexOf("<defLanguage>") > -1 && str.indexOf("</defLanguage>") > -1) 
			{
				String name = str.substring(str.indexOf("<defLanguage>")
						+ "<defLanguage>".length(), str.indexOf("</defLanguage>"));
				//��ȱʡ�����滻�µ�ȱʡ����
				str = str.replace(name, defLoc);
				sb.append(str).append("\n");
			} 
			else 
			{
				sb.append(str).append("\n");
			}
		}
		//���¸��������ļ�
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("../../config/language.xml")));
		bw.write(sb.toString());
		br.close();
		bw.close();
		//�����ڴ�ȱʡ��������
		InitMenData init = new InitMenData();
		init.initLanguage(request.getSession().getServletContext());
		EchoMessage.success().add(
				getMessage(request, "common.msg.updateSuccess")).setBackUrl(
				"/LanguageSetting.do").setAlertRequest(request);
		return getForward(request, mapping, "message");
    }
    
    /**
     * ��ѯϵͳ֧�����ԣ��û�ӵ�����ԣ�ȱʡ����
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
    	
    	Hashtable lanTable = new Hashtable(); //ϵͳ֧�ֵ�����
    	Hashtable usedTable = new Hashtable() ; //�û�ӵ�е�����
    	
    	//��ȡ�����ļ�
    	BufferedReader br = new BufferedReader(new InputStreamReader(
    										new FileInputStream("../../config/language.xml")));
    	String str = null ;
    	while((str=br.readLine())!=null){
    		//ϵͳ֧������
    		if (str.indexOf("<language>") > -1 && str.indexOf("</language>") > -1) 
    		{
    			String name = str.substring(str.indexOf("<language>") + "<language>".length(), 
    																			str.indexOf("</language>"));
                Locale loc = getLocale(name);
    			lanTable.put(name, loc.getDisplayName()) ;
    		}
    		//�û�ӵ������
    		else if(str.indexOf("<usedLanguage>") > -1 && str.indexOf("</usedLanguage>") > -1)
    		{
    			String name = str.substring(str.indexOf("<usedLanguage>") + "<usedLanguage>".length(),
    																			str.indexOf("</usedLanguage>"));
                Locale loc = getLocale(name);
                usedTable.put(name, loc.getDisplayName()) ;
    		}
    		//ȱʡ����
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
     * ��ȡ��������
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
