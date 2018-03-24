package com.menyi.aio.web.importData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.bean.ClientModuleBean;
import com.menyi.aio.bean.ImportDataBean;
import com.menyi.aio.web.customize.CustomizeMgt;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PublicMgt;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ����</p>
 *
 * @author ������
 * @version 1.0
 */
public class ImportDataAction extends MgtBaseAction {
    private ImportDataMgt importDataMgt = new ImportDataMgt();
    PublicMgt publicMgt = new PublicMgt() ;

    /**
     * Actionִ�к���
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.menyi.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        //���ݲ�ͬ�������ͷ������ͬ��������
        request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
        int operation = getOperation(request);
        ActionForward forward = null;
        
//        String opType = request.getParameter("opType");
//        if("start".equals(opType)){
//        	importDataMgt.updateFlag(false, request.getParameterValues("keyId"));
//        }else if("stop".equals(opType)){
//        	importDataMgt.updateFlag(true, request.getParameterValues("keyId"));
//        }
        
        switch (operation) {
        case OperationConst.OP_QUERY:
            forward = query(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_IMPORT_PREPARE:
            forward = importDataPrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_IMPORT:        	
        	
        		forward = importData(mapping, form, request, response);
            break;
        default:
            forward = query(mapping, form, request, response);
        }
        return forward;

    }

     /**
     * Excel���ݵ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward importDataPrepare(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws  Exception {
    	String NoBack = request.getParameter("NoBack");
    	request.setAttribute("fromPage", request.getParameter("fromPage"));
    	request.setAttribute("NoBack", NoBack);
    	request.setAttribute("moduleParam", request.getParameter("moduleParam"));
        String tableName = request.getParameter("tableName");

        Result rs  = importDataMgt.getImportDataByTableName(tableName, request.getParameter("moduleType"));
        request.setAttribute("importData", rs.retVal);
        request.setAttribute("importName", tableName);
        
        Object importThread = request.getSession().getAttribute(tableName+"ImportThread");
        if(importThread != null){
        	ImportThread it = (ImportThread)importThread;
        	if(it.isRuning){
        		request.setAttribute("mainTableDisplay", ((ImportDataBean)rs.retVal).getName());
        		
        		request.setAttribute("moduleType", it.moduleType);
        		return getForward(request, mapping, "importProcess");
        	}else{
                //ת�򵽽��ҳ
        		request.setAttribute("fromPage",request.getAttribute("fromPage"));
        		
        		request.getSession().removeAttribute(tableName+"ImportThread") ;
        		
                request.setAttribute("importName", tableName);
                request.setAttribute("moduleType", it.moduleType);
                request.setAttribute("totalimport", it.importState.getTotal());
                request.setAttribute("successimport", it.importState.getSuccess());
                request.setAttribute("errorimport", it.importState.getFail());
                request.setAttribute("fileName", it.fileName);
                request.setAttribute("NoBack", getParameter("NoBack", request)) ; 
                
                
                if(it.isCrmImport != null &&"true".equals(it.isCrmImport)){
                	//Result rs=moduleMgt.queryMyModules(login);
            		rs = publicMgt.detailCrmModule(it.moduelViewbean.getModuleId()) ;
            		ClientModuleBean moduleBean = (ClientModuleBean) rs.retVal;
            		Result rset=publicMgt.queryAllModuleViews(it.loginBean,moduleBean.getId());
                	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            			request.setAttribute("viewList", rset.retVal);
            			request.setAttribute("moduleBean", moduleBean);
            			request.setAttribute("viewId", request.getParameter("viewId"));
            			request.setAttribute("showRsDiv", "true");
            		}
        	    	return getForward(request, mapping, "importClient");
                }
                
                return getForward(request, mapping, "importComplete");
        	}
        }else{
        	String moduleId = getParameter("moduleId",request);//��ȡCRM�ͻ�ģ��ID
        	
        	//���moduleId !=null && !"".equals(moduleId) ˵����CRM�ͻ��б��롣���ؿͻ��б������
        	if(moduleId!=null && !"".equals(moduleId)){
        		return new ActionForward("/CRMClientAction.do?operation=91&moduleId="+moduleId,true);
        	}else{
        		//��ת��Ĭ�ϵĵ������
        		return getForward(request, mapping, "importUpdata");
        	}
        }        
    }
    


    /**
     * ����excel����
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward importData(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
        
        //����ģ������
        ImportForm importForm = (ImportForm) form;
        String importName = request.getParameter("importName");
        String moduleType = request.getParameter("moduleType");
        String moduleParam= request.getParameter("moduleParam");
        String isCrmImport= request.getParameter("isCrmImport");//true��ʾ���µ�CRMҳ�����
        String moduleId= request.getParameter("moduleId");//CRMģ��Id
        LoginBean login = (LoginBean) request.getSession().getAttribute("LoginBean");
        
        request.setAttribute("fromPage",request.getAttribute("fromPage"));
        
        
        //��ѯ������Ϣ
        /*��ǰ����������*/
    	Locale locale = getLocale(request);        		
		/**
         * session�н�����Ӧ������Ϣ
         */
    	Hashtable<String, DBTableInfoBean> allTables = (Hashtable<String, DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = allTables.get(importName);
		String mainTableDisplay = tableInfoBean.getDisplay().get(locale.toString()); //��¼�������������
		request.setAttribute("mainTableDisplay", mainTableDisplay);
		request.setAttribute("moduleType", moduleType);
		request.setAttribute("importName", importName);
		String NoBack = request.getParameter("NoBack");
    	request.setAttribute("fromPage", request.getParameter("fromPage"));
    	request.setAttribute("NoBack", NoBack);
    	
    	request.setAttribute("showRsDiv", "true");
    	
		ImportThread it = new ImportThread();		
		boolean isBom = false;
		if("tblBOM".equals(request.getParameter("importName"))&& GlobalsTool.getVersion()==8){
    		isBom = true;
    	}
		return it.initImport(isBom,mapping, importName, locale, request, moduleType, moduleParam, isCrmImport, moduleId, importForm, login);
    }
    
    
   
    
    /**
     * ��ѯ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
            Exception {
        ImportSearchForm searchForm = (ImportSearchForm) form;
        if (searchForm != null) {
            //ִ�в�ѯ
        	if("menu".equals(getParameter("src", request))){
        		request.removeAttribute("importSearchForm") ;
        		searchForm.setName("") ;
        	}
            Result rs = importDataMgt.queryImportData(searchForm.getName(),searchForm.getPageSize(),searchForm.getPageNo());
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            	Hashtable<String,DBTableInfoBean> allTables = (Hashtable<String,DBTableInfoBean>) request.getSession().
        		getServletContext().getAttribute(BaseEnv.TABLE_INFO);
            	ArrayList<ImportDataBean> list = (ArrayList<ImportDataBean>)rs.retVal;
            	for(ImportDataBean bean:list){
            		DBTableInfoBean tb = allTables.get(bean.getTargetTable());
            		if(tb !=null && tb.getTableType()!=DBTableInfoBean.MAIN_TABLE&&tb.getPerantTableName()!=null&&tb.getPerantTableName().length()>0){
            			bean.setParentTableName(tb.getPerantTableName().split(";")[0]);
            		}
            		
            	}
            	
            	
                request.setAttribute("result",rs.getRetVal());
                request.setAttribute("pageBar",this.pageBar(rs,request));
            } else {
                //��ѯʧ��
                throw new BusinessException("common.msg.error");
            }
        }

        return getForward(request, mapping, "importList");
    }
    

 
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
        String[] tableMapping = request.getParameterValues("keyId");
        Result rs = importDataMgt.deleteImportData(tableMapping);

        ActionForward forward = getForward(request, mapping, "message");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //ɾ���ɹ�
            EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
                    			 .setBackUrl("/advanceImportDataQueryAction.do" 
                    					   + "?winCurIndex=" +  request.getParameter("winCurIndex"))
                                 .setAlertRequest(request);
        } else {
            //ɾ��ʧ��
            throw new BusinessException("common.msg.updateFailture","/advanceImportDataQueryAction.do" + "?winCurIndex=" +
                               request.getParameter("winCurIndex"));
        }
        return forward;
    }

 

}
