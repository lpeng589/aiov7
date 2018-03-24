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
 * <p>Title:该类主要负责模块弹出窗口列名设置的添删查改操作</p> 
 * <p>Description: </p>
 *
 * @Date:Aug 16, 2010
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class ModuleColAction extends MgtBaseAction{
	private ModuleColMgt moduleMgt = new ModuleColMgt() ;
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//跟据不同操作类型分配给不同函数处理
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
	 * 添加模块弹出窗口字段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward add(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId", request) ; /*模块ID*/
		String moduleName = getParameter("moduleName", request) ; /*模块名*/
		String moduleDisplay = getParameter("moduleDisplay", request) ; /*模块名称*/
		String[] fieldName = getParameters("fieldName", request) ; /*弹出窗口字段名*/
		String[] fieldLanague = getParameters("fieldLanague", request) ;/*弹出窗口列名设置后的值*/
		
		Result result = moduleMgt.queryIsExistModulePopupById(moduleId)  ;
		if(result.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(getMessage(request, "moduleCol.error.popupExisted"))
				.setBackUrl("/ModuleColAction.do?winCurIndex="+getParameter("winCurIndex", request))
				.setAlertRequest(request) ;
			return getForward(request, mapping, "alert") ;
		}
		result = moduleMgt.add(moduleId, moduleName,moduleDisplay, fieldName, fieldLanague) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			/*把新设置的弹出窗口列名加载到内存中*/
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
	 * 添加之前
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
	 * 查询做弹出窗口列名设置的所有模块
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
	 * 删除模块弹出窗口字段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delete(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		String[] moduleIds = getParameters("keyId", request) ;
		
		/*清除内存中以前设置的值*/
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
	 * 修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward update(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId", request) ;/*模块ID*/
		String moduleName = getParameter("moduleName", request) ; /*模块名*/
		String moduleDisplay = getParameter("moduleDisplay", request) ; /*模块显示名*/
		String[] fieldName = getParameters("fieldName", request) ; 
		String[] fieldLanague = getParameters("fieldLanague", request) ;
		
		/*清除内存中以前设置的值*/
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
			/*把新设置的弹出窗口列名加载到内存中*/
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
	 * 修改之前 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updatePrepare(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId", request) ;/*模块Id*/
		request.setAttribute("winCurIndex", getParameter("winCurIndex", request)) ;
		
		Result result = moduleMgt.queryModulePopField(moduleId) ;
		request.setAttribute("popupList", result.getRetVal()) ;
		
		return getForward(request, mapping, "moduleColUpdate") ;
	}
}
