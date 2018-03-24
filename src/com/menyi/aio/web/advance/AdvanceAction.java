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
 * Title:�߼�����Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-08-13 ���� 10:55
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
public class AdvanceAction extends MgtBaseAction {

	AdvanceMgt mgt = new AdvanceMgt();

	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
		String optype = request.getParameter("optype");

		if (optype != null && "clue".equals(optype)) {
			//��ʾҳ��
			forward = clue(mapping, form, request, response);
		} else if (optype != null && "refresh".equals(optype)) {
			//ˢ���ڴ�
			forward = refresh(mapping, form, request, response);
		} else if (optype != null && "restart".equals(optype)) {
			//����ϵͳ
			forward = restart(mapping, form, request, response);
		} else if (optype != null && "index".equals(optype)) {
			//��ҳ
			forward = index(mapping, form, request, response);
		} else {
			forward = clue(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * ˢ���ڴ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward restart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

//		String retUrl = "";
//		//���ļ�set SERVICE_NAME=AIOServer
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
//			BaseEnv.log.debug("AdvanceAction restart ------��ʼ");
//		} catch (IOException exception) {    
//			BaseEnv.log.error("AdvanceAction restart Error :",exception);
//		} 
		SystemState.instance.dogState = SystemState.DOG_RESTART; //����������־         
		EchoMessage.success().add("��������").setBackUrl("/AdvanceAction.do?optype=index").setAlertRequest(request);
		return getForward(request, mapping, "message");
	}
	
	public static void refreshMem(HttpServletRequest request){
		InitMenData initMenData = new InitMenData();
		ServletContext servletContext = request.getSession().getServletContext();

		Result rs = initMenData.initSystemSettingInformation();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ��ϵͳ����initSystemSettingInformation ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ��ϵͳ����initSystemSettingInformation �ɹ�-----------");
        }
        

        //�������е�ö��ֵ
        rs = initMenData.initEnumerationInformation();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ��ö��initEnumerationInformation ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ��ö��initEnumerationInformation �ɹ�-----------");
        }
                
        rs = initMenData.initModuleList();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ��ģ��initModuleList ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ��ģ��initModuleList �ɹ�-----------");
        }
        
        //���������û�ģ���Զ�������������Ϣ
        rs = initMenData.initModuleColLanuage(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ���Զ�������initModuleColLanuage ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ���Զ�������initModuleColLanuage �ɹ�-----------");
        }

        
        //���������û��Զ����п�������Ϣ
        rs = initMenData.initUserColWidth(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ���Զ����п�initUserColWidth ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ���Զ����п�initUserColWidth �ɹ�-----------");
        }
        
        //���������û��Զ�������������Ϣ
        rs = initMenData.initUserLanuageInfo(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ���Զ�������initUserLanuageInfo ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ���Զ�������initUserLanuageInfo �ɹ�-----------");
        }
        
        //�������еı���Ϣ
        rs = initMenData.initDBInformation(servletContext,null);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ�±�ṹinitDBInformation ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ�±�ṹinitDBInformation �ɹ�-----------");
        }
        //���������û��Զ���������������Ϣ
        rs = initMenData.initUserColConfig(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ��������initUserColConfig ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ��������initUserColConfig �ɹ�-----------");
        }
                
        //����ģ���Ӧ�ֶ���Ϣ����Ҫ���ڶ��ģ��ʹ��ͬһ�ű�����ʹ��ͬһ��define��
        rs = initMenData.initModuleField(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ��ģ���ֶ�initModuleField ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ��ģ���ֶ�initModuleField �ɹ�-----------");
        }
        //���ص�������ʾ�������
        rs = initMenData.initPopDisSen(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ�µ�������ʾ���initPopDisSen ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ�µ�������ʾ���initPopDisSen �ɹ�-----------");
        }
        
       
        
        //����������Ʒ����
        rs = initMenData.initPropInformation(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ����Ʒ����initPropInformation ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ����Ʒ����initPropInformation �ɹ�-----------");
        }
        
        //      ����������Ʒ����
        rs = initMenData.initAttInformation(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ����Ʒ����initAttInformation ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ����Ʒ����initAttInformation �ɹ�-----------");
        }
        		       
        
        //�������ļ�
        initMenData.initConfigFile();
        BaseEnv.log.debug("--------AdvanceAction.restart ˢ�������ļ�initConfigFile �ɹ�-----------");

        
        /**�������������õĹ�������Ϣ**/
        rs = initMenData.initWorkFlowInfo(servletContext) ;
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ�������ù�����initWorkFlowInfo ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ�������ù�����initWorkFlowInfo �ɹ�-----------");
        }      
        
        
        
        //��ʼ��������ʾ����
        rs = initMenData.initReportShowSet();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ�±�����ʾ����initReportShowSet ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ�±�����ʾ����initReportShowSet �ɹ�-----------");
        }
        
        //��ʼ��KK����
        if (!MSGConnectCenter.init()) {
        	BaseEnv.log.error(
                    "--------AdvanceAction.restart ˢ��KK����   ʧ��-----------" +
                    rs.retCode);
        }else{
        	BaseEnv.log.debug("--------AdvanceAction.restart ˢ��KK����  �ɹ�-----------");
        }
        
        rs = initMenData.initFastQuery();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error("--------AdvanceAction. initFastQuery Failture-----------");
        }
        
        GenJS.clearJS();
	}

	/**
	 * ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		refreshMem(request);        
		EchoMessage.success().add("�ڴ�������").setBackUrl("/AdvanceAction.do?optype=index").setAlertRequest(request);
		return getForward(request, mapping, "message");
	}

	/**
	 * ��ʾ
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
	 * ��ҳ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward index(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* ��ѯ���и߼������µ�ģ�� */
		Result result = mgt.queryModule();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//��ѯ�ɹ�
			request.setAttribute("moduleList", result.retVal);
		}
		return getForward(request, mapping, "index");
	}
}
