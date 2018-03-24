package com.menyi.aio.web.moduleCol;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.ModulePopupDisplay;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:������Ҫ����ģ�鵯�������������õ���ɾ��Ĳ���</p> 
 * <p>Description: </p>
 *
 * @Date:Aug 16, 2010
 * @Copyright: �������
 * @Author ��СǮ
 */
public class ModuleColAction extends MgtBaseAction{
	private ModuleColMgt moduleMgt = new ModuleColMgt() ;
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//���ݲ�ͬ�������ͷ������ͬ��������
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        	case OperationConst.OP_ADD_PREPARE:
        		forward = addPrepare(mapping,form,request,response) ;
        		break ;
	        case OperationConst.OP_ADD:
	            forward = add(mapping, form, request, response);
	            break;
	        case OperationConst.OP_UPDATE_PREPARE:
        		forward = updatePrepare(mapping,form,request,response) ;
        		break ;
	        case OperationConst.OP_UPDATE:
	            forward = update(mapping, form, request, response);
	            break;
	        case OperationConst.OP_DELETE:
	        	forward = delete(mapping,form,request,response) ;
	        	break ;
	        default:
	            forward = query(mapping, form, request, response);
        }
        return forward;
	}

	/**
	 * ���ģ�鵯�������ֶ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward add(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId", request) ; /*ģ��ID*/
		String moduleName = getParameter("moduleName", request) ; /*ģ����*/
		String moduleDisplay = getParameter("moduleDisplay", request) ; /*ģ������*/
		String[] fieldName = getParameters("fieldName", request) ; /*���������ֶ���*/
		String[] fieldLanague = getParameters("fieldLanague", request) ;/*���������������ú��ֵ*/
		
		Result result = moduleMgt.queryIsExistModulePopupById(moduleId)  ;
		if(result.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(getMessage(request, "moduleCol.error.popupExisted"))
				.setBackUrl("/ModuleColAction.do?winCurIndex="+getParameter("winCurIndex", request))
				.setAlertRequest(request) ;
			return getForward(request, mapping, "alert") ;
		}
		result = moduleMgt.add(moduleId, moduleName,moduleDisplay, fieldName, fieldLanague) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			/*�������õĵ��������������ص��ڴ���*/
			Hashtable<String, KRLanguage> moduleLanguage = (Hashtable<String, KRLanguage>) request.getSession()
														.getServletContext().getAttribute("moduleColLanguage");
			if(moduleLanguage==null){
				moduleLanguage = new Hashtable<String, KRLanguage>() ;
				request.getSession().getServletContext().setAttribute("moduleColLanguage", moduleLanguage) ;
			}
			for(int i=0;i<fieldName.length;i++){
				KRLanguage display = KRLanguageQuery.create(GlobalsTool.getLocaleDisplay(fieldLanague[i], "zh_CN"),
    							GlobalsTool.getLocaleDisplay(fieldLanague[i], "en"),GlobalsTool.getLocaleDisplay(fieldLanague[i], "zh_TW"));
				String[] fields = fieldName[i].split("-") ;
				moduleLanguage.put(fields[0]+fields[3]+fields[2]+fields[4], display) ;
			}
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess"))
							 .setBackUrl("/ModuleColAction.do?winCurIndex="+getParameter("winCurIndex", request))
							 .setAlertRequest(request) ;
		}else{
			EchoMessage.success().add(getMessage(request, "common.msg.addFailture"))
			 				.setBackUrl("/ModuleColAction.do?winCurIndex="+getParameter("winCurIndex", request))
			 				.setAlertRequest(request) ;
		}
		return getForward(request, mapping, "alert") ;
	}
	
	/**
	 * ���֮ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addPrepare(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		return getForward(request, mapping, "moduleColAdd") ;
	}
	
	/**
	 * ��ѯ�����������������õ�����ģ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward query(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		String moduleName = getParameter("moduleName", request) ;
		moduleName = moduleName==null?"":moduleName ;
		request.setAttribute("moduleName", moduleName) ;
		Result result = moduleMgt.queryAllModuleCol(moduleName) ;
		request.setAttribute("moduleList", result.getRetVal()) ;
		return getForward(request, mapping, "moduleColList") ;
	}
	
	/**
	 * ɾ��ģ�鵯�������ֶ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delete(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		String[] moduleIds = getParameters("keyId", request) ;
		
		/*����ڴ�����ǰ���õ�ֵ*/
		Hashtable<String, KRLanguage> moduleLanguage = (Hashtable<String, KRLanguage>) request.getSession()
													.getServletContext().getAttribute("moduleColLanguage");
		for(String moduleId : moduleIds){
			Result result = moduleMgt.queryModulePopField(moduleId) ;
			if(result.retCode==ErrorCanst.DEFAULT_SUCCESS && moduleLanguage!=null){
				ArrayList<ModulePopupDisplay> popupList = (ArrayList<ModulePopupDisplay>) result.getRetVal() ;
				for(ModulePopupDisplay popup : popupList){
					moduleLanguage.remove(popup.getTableName()+popup.getPopupName()+popup.getPopupFieldName()+popup.getFieldType()) ;
				}
			}
		}
		
		StringBuffer moduleId = new StringBuffer() ;
		for(String str : moduleIds){
			moduleId.append("'").append(str).append("',") ;
		}
		String keyIds = moduleId.toString().substring(0, moduleId.toString().length()-1) ;
		Result result = moduleMgt.delModulePopupField(keyIds) ;
		if(result.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(request, "common.msg.delError"))
							.setBackUrl("/ModuleColAction.do?winCurIndex="+getParameter("winCurIndex", request))
							.setAlertRequest(request) ;
			return getForward(request, mapping, "message") ;
		}
		return query(mapping, form, request, response) ;
	}
	
	/**
	 * �޸�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward update(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId", request) ;/*ģ��ID*/
		String moduleName = getParameter("moduleName", request) ; /*ģ����*/
		String moduleDisplay = getParameter("moduleDisplay", request) ; /*ģ����ʾ��*/
		String[] fieldName = getParameters("fieldName", request) ; 
		String[] fieldLanague = getParameters("fieldLanague", request) ;
		
		/*����ڴ�����ǰ���õ�ֵ*/
		Hashtable<String, KRLanguage> moduleLanguage = (Hashtable<String, KRLanguage>) request.getSession()
													.getServletContext().getAttribute("moduleColLanguage");
		Result result = moduleMgt.queryModulePopField(moduleId) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS && moduleLanguage!=null){
			ArrayList<ModulePopupDisplay> popupList = (ArrayList<ModulePopupDisplay>) result.getRetVal() ;
			for(ModulePopupDisplay popup : popupList){
				moduleLanguage.remove(popup.getTableName()+popup.getPopupName()+popup.getPopupFieldName()+popup.getFieldType()) ;
			}
		}
		result = moduleMgt.update(moduleId, moduleName,moduleDisplay, fieldName, fieldLanague) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			/*�������õĵ��������������ص��ڴ���*/
			if(moduleLanguage==null){
				moduleLanguage = new Hashtable<String, KRLanguage>() ;
				request.getSession().getServletContext().setAttribute("moduleColLanguage", moduleLanguage) ;
			}
			for(int i=0;i<fieldName.length;i++){
				KRLanguage display = KRLanguageQuery.create(GlobalsTool.getLocaleDisplay(fieldLanague[i], "zh_CN"),
    							GlobalsTool.getLocaleDisplay(fieldLanague[i], "en"),GlobalsTool.getLocaleDisplay(fieldLanague[i], "zh_TW"));
				String[] fields = fieldName[i].split("-") ;
				moduleLanguage.put(fields[0]+fields[3]+fields[2]+fields[4], display) ;
			}
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
							 .setBackUrl("/ModuleColAction.do?winCurIndex="+getParameter("winCurIndex", request))
							 .setAlertRequest(request) ;
		}else{
			EchoMessage.success().add(getMessage(request, "common.msg.updateFailture"))
			 				.setBackUrl("/ModuleColAction.do?winCurIndex="+getParameter("winCurIndex", request))
			 				.setAlertRequest(request) ;
		}
		return getForward(request, mapping, "alert") ;
	}
	
	/**
	 * �޸�֮ǰ 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updatePrepare(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId", request) ;/*ģ��Id*/
		request.setAttribute("winCurIndex", getParameter("winCurIndex", request)) ;
		
		Result result = moduleMgt.queryModulePopField(moduleId) ;
		request.setAttribute("popupList", result.getRetVal()) ;
		
		return getForward(request, mapping, "moduleColUpdate") ;
	}
}
