package com.menyi.aio.web.userFunction;

//������ɾ������IMPORT ������ -˭�ٸ�ɾ���´���,��С����
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.dbfactory.Result;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.MailinfoSettingBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.*;
import com.menyi.aio.web.colconfig.ColConfigMgt;
import com.menyi.aio.web.customize.ApplyGoodsBillSum;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.importData.ExportField;
import com.menyi.aio.web.importData.ImportDataMgt;
import com.menyi.aio.web.iniSet.IniGoodsMgt;
import com.menyi.aio.web.login.*;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.report.TableListResult;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.aio.web.sysAcc.ReCalcucateMgt;
import com.menyi.aio.web.tab.TabMgt;
import com.menyi.aio.web.tablemapped.TableMappedMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.*;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.hibernate.DBUtil;

import java.security.Key;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Session;

import java.sql.PreparedStatement;

import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;

import org.hibernate.collection.PersistentBag;
import org.hibernate.jdbc.Work;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.apache.bsf.BSFException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import com.koron.aioshop.bean.SpOrder;
import com.koron.aioshop.services.IAIOShopServices;
import com.menyi.aio.web.aioshop.AIOShopMgt;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.billNumber.BillNo.BillNoUnit;
import com.menyi.aio.web.importData.ImportState;
//������ɾ������IMPORT ������ -˭�ٸ�ɾ���´���,��С����
//ע�⣺�ڱ��ļ��мӰ�������仰���棬����һ�β������κ��޸�
import com.menyi.email.EMailMgt;
import com.menyi.email.util.EMailMessage;

/**
/**
 *
 * <p>Title: </p>
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
public class UserFunctionAction extends MgtBaseAction { 
    DynDBManager dbmgt = new DynDBManager();
    UserFunctionMgt userMgt = new UserFunctionMgt() ;
    PublicMgt pcmgt = new PublicMgt();
    UserMgt empMgt=new UserMgt();
    RoleMgt roleMgt=new RoleMgt();
    public static final int DRAFT = 1;
    public static final int TEXT = 2;
    int draftFlag;
    /**
     * exe ��������ں���
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * 
     * 
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
    	 //���ݲ�ͬ�������ͷ������ͬ��������sh
        int operation = getOperation(request);
        request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
        String noback=request.getParameter("noback");//���ܷ���
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		request.setAttribute("popWinName", request.getParameter("popWinName")); //��¼���ڴ��Լ��ĵ����������֣����ڹر��Լ�
		/*�������� ��Ӻ� �Զ�ˢ�µ�������*/
		request.getSession().removeAttribute("fresh");
		request.setAttribute("fresh", getParameter("fresh", request)) ;
		
        String tableName = request.getParameter("tableName");
        request.setAttribute("tableName", tableName);
        
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_UPDATE_ADD:
            forward = updateAdd(mapping, form, request, response);
            break;
        case OperationConst.OP_ADD_PREPARE:
            forward = addPrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_ADD:
            String type = request.getParameter("type");
            /*
            if (null != type) {
                forward = send_email(mapping, form, request, response);
            } else {
                draftFlag = TEXT;
                forward = add(mapping, form, request, response);
            }*/
        	 draftFlag = TEXT;
             forward = add(mapping, form, request, response);
            break;
		case OperationConst.Op_RETAUDITING :
			forward = retAuditing(mapping, form, request, response);
			break;
        case OperationConst.OP_DRAFT:
            draftFlag = DRAFT;
            forward = add(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE_PREPARE:
            type = request.getParameter("type");
            if (null != type && !"".equals(type) && "consign".equals(type)) {
                forward = goConsign(mapping, form, request, response);
            } else {
                request.setAttribute("update", "update");
                forward = updatePrepare(mapping, form, request, response,null);
            }
            break;
        case OperationConst.OP_UPDATE:
	        type = request.getParameter("type");
	    	if(type != null && "reAudit".equals(type)){
	    		forward = dealReAudit(mapping, form, request, response);
	    	}else{
	    		forward = update(mapping, form, request, response);
	    	}
	        break;
        case OperationConst.Op_AUDITING:
            forward = deliverToAll(mapping, form, request, response);
        	break;
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_QUERY:
        	String optionType=request.getParameter("optionType");
    		if(optionType!=null&&optionType.equals("hurryTrans")){//�߰�
    			forward = hurryTrans(mapping, form, request, response);
    		}else{   	
        		forward = query(mapping, form, request, response);
        	}
            break;
        case OperationConst.OP_DETAIL:
            type = request.getParameter("type");
            if (null != type && !"".equals(type)) {
                forward = detail_email(mapping, form, request, response);
            }else {
                request.setAttribute("detail", "detail");
                request.setAttribute("detailOk", "1");
                forward = updatePrepare(mapping, form, request, response,null);
            }
            break;
        case OperationConst.OP_COPY:
            request.setAttribute("copy", "copy");
            forward = updatePrepare(mapping, form, request, response,null);
            break;
        case OperationConst.OP_QUOTE:
            request.setAttribute("quote", "quote");
            forward = quotePrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_POPUP_SELECT:
        	type=request.getParameter("type");
        	if("addSequence".equals(type)){
        		forward = addSequence(mapping, form, request, response);
        	}else if("stockSequence".equals(type)){
        		forward = stockSequence(mapping, form, request, response) ;
        	}else{
        		forward = popupSelect(mapping, form, request, response);
        	}
            break;
        case OperationConst.OP_CHECK:
            forward = check(mapping, form, request, response);
            break;
        case OperationConst.OP_CHECK_PREPARE:
            forward = checkPrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_CHECK_LIST:
            forward = check_list(mapping, form, request, response);
            break;
        case OperationConst.OP_PRINT:
            forward = showPrint(mapping, form, request, response);
            break;
        case OperationConst.OP_EXTENDBUTTON_DEFINE:
        	forward = extendDefine(mapping, form, request, response);
        	break;
        case OperationConst.OP_BUTTON_AUDITING:
        	forward = buttonAuditing(mapping,form,request,response) ;
        	break ;
        case OperationConst.OP_BUTTON_REV_AUDITING:
        	forward = buttonReverse(mapping,form,request,response) ;
        	break ;       
        case OperationConst.OP_SELECT_PIC:
        	forward = selectPic(mapping,form,request,response) ;
        	break ; 
        default:
            forward = query(mapping, form, request, response);
        }
        return forward;
    }
    
    /**
     * �������߰�
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws BSFException 
     */
    protected ActionForward selectPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	String detail = request.getParameter("detail"); //��ѯ���黹������ͼ
    	if(detail == null || detail.length() ==0){
    		detail = "snt";
    	}
    	request.setAttribute("detail", detail);
    	
    	String picPath = request.getParameter("picPath"); //ͼƬ·�������û�о���Ĭ��·��
    	if(picPath == null || picPath.length() ==0){
    		picPath = GlobalsTool.getSysSetting("picPath").replaceAll("\\\\", "/");
    	}
    	request.setAttribute("rootPath", GlobalsTool.getSysSetting("picPath").replaceAll("\\\\", "/")); //��Ŀ¼
    	
    	if(picPath.endsWith("\\")|| picPath.endsWith("/")){
    		picPath = picPath.substring(0,picPath.length()  -1);
    	}
    	if(!picPath.equals(GlobalsTool.getSysSetting("picPath").replaceAll("\\\\", "/"))){
    		request.setAttribute("backPath", picPath.substring(0, picPath.lastIndexOf("/")));
    	}else{
    		request.setAttribute("backPath", "");
    	}
    	final String search = request.getParameter("search");
    	request.setAttribute("search", search);
    	final String searchChild = request.getParameter("searchChild");
    	request.setAttribute("searchChild",searchChild);
    	File path = new File(picPath);
    	FileFilter filter =new FileFilter() {
            public boolean accept(File file) {
            	String fn = file.getName().toLowerCase();
                if ( (file.isDirectory() || fn.endsWith(".jpg") || fn.endsWith(".jpeg") || fn.endsWith(".png") || fn.endsWith(".bmp") || fn.endsWith(".gif"))) {
                    return true;
                }
                return false;
            }
        };
        ArrayList<File> fileList = new ArrayList<File>();
        if(search != null && search.length() > 0 && "1".equals(searchChild)){
        	//������Ŀ¼
        	recSearchFile(path, filter, search, fileList);
        }else{
        	File[] fs = path.listFiles(filter);
        	if(fs != null){
    	    	for(File f:fs){
    	    		if(search==null || search.length()==0 || f.getName().indexOf(search)>0){
    	    			fileList.add(f);
    	    		}  
    	    	}
        	}
        }
    	request.setAttribute("picPath", picPath);
    	
    	String sortName = request.getParameter("sortName");
    	if(sortName == null || sortName.length() ==0){ //name date type
    		sortName="name";
    	}
    	request.setAttribute("sortName", sortName);
    	String asc = request.getParameter("asc"); 
    	if(asc == null || asc.length() ==0){
    		asc="asc";
    	}
    	request.setAttribute("asc", asc);
    	
    	final String fsortName = sortName;
    	final String fasc = asc;
    	Collections.sort(fileList, new Comparator(){
    	     public int compare(Object o1, Object o2)
    	    {
    	    	File f1= (File)o1;
    	    	File f2= (File)o2;
    	    	if((f1.isDirectory() && f2.isDirectory()) || (!f1.isDirectory() && !f2.isDirectory())){
    	    		if(fsortName.equals("name")){
    	    			return f1.getName().compareTo(f2.getName()) * (fasc.equals("asc")?1:-1);
    	    		}else if(fsortName.equals("date")){
    	    			return (f1.lastModified()>f2.lastModified()?1:-1 ) * (!fasc.equals("asc")?1:-1);
    	    		}else if(fsortName.equals("type") && f1.getName().lastIndexOf(".") > -1 && f2.getName().lastIndexOf(".") > -1){
    	    			return  f1.getName().substring(f1.getName().lastIndexOf(".")).compareTo(f2.getName().substring(f2.getName().lastIndexOf("."))) * (fasc.equals("asc")?1:-1);
    	    		}
    	    	}else{
    	    		return f1.isDirectory() ? -1: 1;
    	    	}
    	    	return 0;
    	    }
    	});
    	request.setAttribute("files", fileList);    	
		return getForward(request, mapping, "selectPic");
    }  
    private void recSearchFile(File path,FileFilter filter,String search,ArrayList<File> fileList){
    	File[] fs = path.listFiles(filter);
    	if(fs != null){
	    	for(File f:fs){
	    		if(f.isDirectory()){
	    			recSearchFile(f, filter, search, fileList);
	    		}else if(f.getName().indexOf(search)>0){
	    			fileList.add(f);
	    		}  
	    	}
    	}
    }
    
    /**
     * �������߰�
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws BSFException 
     */
    protected ActionForward hurryTrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String keyId=request.getParameter("keyId");
		String tableName=request.getParameter("tableName");
		OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
		HashMap map=mgt.getOAMyWorkFlowInfo(keyId,tableName);
		
		if(map!=null){
			String createBy=map.get("createBy").toString();
			String applyType=map.get("applyType").toString();
			String checkPerson=map.get("checkPerson").toString();
			String content=request.getParameter("content");
			String nextNodeIds=map.get("nextNodeIds").toString();
			String currNodeId=map.get("currentNode").toString();
			String oaTimeLimit=map.get("benchmarkTime").toString();
			String oaTimeLimitUnit=map.get("oaTimeLimitUnit").toString();
			
			String applyBy=OnlineUserInfo.getUser(createBy).getName();
			String wakeType=request.getParameter("wakeType")==null?"":request.getParameter("wakeType");
			mgt.hurryTrans(applyBy,applyType,checkPerson,keyId,nextNodeIds,currNodeId,oaTimeLimit,oaTimeLimitUnit,
					this.getLocale(request),"",content,wakeType,"hurryTrans","",this.getLoginBean(request),"",this.getResources(request));
		
    	}
		request.setAttribute("dealAsyn", "true");
		EchoMessage.success().add(getMessage(request, "oa.transaction.succeed")).setAlertRequest(request);
		return getForward(request, mapping, "alert");
    }
    
    /**
     * ����ת��
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws BSFException 
     */
    protected ActionForward deliverToAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
    	OAMyWorkFlowMgt oamgt=new OAMyWorkFlowMgt();
    	Result rs=null;
    	String nextStep=request.getParameter("nextStep");
    	String id=request.getParameter("keyId");//�ں������Ϊ�����IDʹ��
    	String pageType = request.getParameter("pageType");    	
    	String keyId=id;//���ݱ���ID
    	if("cancel".equals(nextStep)){
    		if("detail".equals(pageType)||"update".equals(pageType)){
    			keyId = request.getParameter("id");//��������������
        	}
    		String designId=request.getParameter("designId");
    		
    		String currNode = "";
    		String lastNodeId = "";
    		String lastCheckPerson = "";
    		String tableName=request.getParameter("tableName");
			if(tableName!=null&&tableName.length()>0){
				HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId, tableName) ;
				id = String.valueOf(flowMap.get("id"));//�����ID
				currNode = String.valueOf(flowMap.get("currentNode"));
				lastNodeId = String.valueOf(flowMap.get("lastNodeId"));
				lastCheckPerson = String.valueOf(flowMap.get("lastCheckPerson"));
				if(designId==null || designId.length()==0){
					designId = (String) flowMap.get("applyType") ;
				}
			}
			
			if(!lastCheckPerson.equals(getLoginBean(request).getId())){
				EchoMessage.error().add("��ǰ���ĳ����˲����㣬���ܽ��д˲�����").setAlertRequest(request);
	        	return getForward(request, mapping, "message");
			}
			//�ж��Ѿ������ɣ����ߴ��ݹ����ĵ�ǰ�ڵ㲻����ʵ�ʵĵ�ǰ�ڵ㣬������ִ�д˲���
			String workFlowNodeName="";
			String workFlowNode="";
			String tempTableName = new DynDBManager().getInsertTableName(tableName);//����CRM��ģ�����
	        Result rst=new AIODBManager().sqlList("select workFlowNodeName,workFlowNode from "+tempTableName+" where id='"+keyId+"'", new ArrayList());
	    	if(rst.retVal!=null&&((ArrayList)rst.retVal).size()>0){
	    		Object[] obj=((Object[])((ArrayList)rst.retVal).get(0));    		
	    		workFlowNodeName=obj[0]==null?"":obj[0].toString();
	    		workFlowNode=obj[1]==null?"":obj[1].toString();
	    	}
	    	
	        if(workFlowNodeName.equals("finish")||!workFlowNode.equals(currNode)){
	        	EchoMessage.error().add(this.getMessage(request, "common.msg.hasAudit")).setAlertRequest(request);
	        	return getForward(request, mapping, "message");
	        }
	        
			rs = oamgt.cancel(id, lastNodeId, currNode, this.getLoginBean(request), designId);
			String msg=this.getMessage(request, "oa.msg.cancelSucc");
			rs=oamgt.updateFlowDepict(designId, id,this.getLocale(request));
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				String crmCancel = getParameter("crmCancel",request);
				if(crmCancel !=null && "true".equals(crmCancel)){
					EchoMessage.success().add(msg).setBackUrl("/CRMClientAction.do?operation=5&type=detailNew&keyId="+keyId).setAlertRequest(request) ;
				
				}else if("detail".equals(pageType) || "update".equals(pageType)){//������������
					EchoMessage.success().add(msg).setBackUrl("/UserFunctionAction.do?tableName=" + tableName 
					+ "&keyId="+keyId+"&moduleType="+(request.getParameter("moduleType")==null?"":request.getParameter("moduleType"))+
					"&f_brother=" + (request.getParameter("f_brother")==null?"":request.getParameter("f_brother")) 
					+ "&operation="+OperationConst.OP_DETAIL+"&winCurIndex="+request.getParameter("winCurIndex")+
					"&parentCode=" + (request.getParameter("parentCode")==null?"":request.getParameter("parentCode")) + "&parentTableName="+
					(request.getParameter("parentTableName")==null?"":request.getParameter("parentTableName"))
					+ "&saveDraft=&checkTab=Y&popWinName="+request.getParameter("popWinName")).setAlertRequest(request) ;
	        	}else{
					EchoMessage.success().add(msg).setBackUrl("/UserFunctionQueryAction.do?tableName=" +tableName 
							+ "&operation=" + OperationConst.OP_QUERY + "&parentCode=" +(request.getParameter("parentCode")==null?"":request.getParameter("parentCode"))
							+ "&f_brother=" +(request.getParameter("f_brother")==null?"":request.getParameter("f_brother")) +"&parentTableName="+(request.getParameter("parentTableName")==null?"":request.getParameter("parentTableName")) 
							+ "&moduleType="+(request.getParameter("moduleType")==null?"":request.getParameter("moduleType")) +"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request) ;
				}
				return getForward(request, mapping, "alert") ;
			}
    	}else{    	
	    	/*String[] ids=getParameter("varKeyIds", request).split("\\|");
	    	rs = oamgt.deliverToAll(ids,this.getLoginBean(request),this.getLocale(request),this.getResources(request)) ;
			if(rs.getRetCode()==ErrorCanst.ER_NO_DATA){
				EchoMessage.info().add(this.getMessage(request, "oa.errormsg.noCheckPerson")).setBackUrl("/OAMyWorkFlow.do?approveStatus=transing&operation="+OperationConst.OP_QUERY).setAlertRequest(request);
				return getForward(request, mapping, "message");
			}else if (rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR) {
	       	 	//�洢���̷��ظ�������
				String errorMessage = rs.getRetVal().toString() ;
				errorMessage = GlobalsTool.revertTextCode2(errorMessage) ;
	        	String[] str= errorMessage.split(";");
	        	String msg=this.getMessage(request,"common.error.negative2",errorMessage);;
	        	if(str.length==1){
	                 msg = this.getMessage(request,"common.error.negative2",str[0]);
	        	}else{
	        		 msg = this.getMessage(request,"common.error.negative",str[0],str[1]);
	        	}
	        	EchoMessage.error().add(msg).setAlertRequest(request);
	        	return getForward(request, mapping, "message");
	        }else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SENTENCE_ERROR) {
                Object o = rs.getRetVal();
                if (o != null) {
                    EchoMessage.error().add(o.toString()).setAlertRequest(request);
                } else {
                    EchoMessage.error().add(rs.retCode, request).setAlertRequest(request);
                }
                return getForward(request, mapping, "message");
            }*/
    	}
		return query(mapping, form, request, response);
    }

    
    protected ActionForward opInIniAmt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
    	ActionForward forCur=null;
        Result rs=new Result();
		String tablename = request.getParameter("tableName");
		String parentCode=request.getParameter("parentCode");
		String f_brother=request.getParameter("f_brother");
		String parentTableName=request.getParameter("parentTableName");
		String onOff=request.getParameter("onOff");
		byte inputType=3;
		String error="property.stop.error";
		if (onOff.equals("on")) {
			inputType = 0;
			error = "sms.set.startStop";
			request.setAttribute("onOff", "off");
		} else {
			request.setAttribute("onOff", "on");
		}
		rs = userMgt.updateFactQty(tablename, inputType);
		if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			try {
				forCur=addPrepare(mapping, form, request, response);
			} catch (Exception e) {
				BaseEnv.log.error("UserFunctionAction.java opInIniAmt() method "+e) ;
			}
		}else{
	        EchoMessage.error().add(getMessage(request, error))
	                		   .setBackUrl("/UserFunctionAction.do?tableName=" + tablename 
	                				   		+ "&parentCode=" + parentCode + "&operation=" + OperationConst.OP_DETAIL 
	                				   		+ "&f_brother"+f_brother+"&parentTableName="+parentTableName
	                				   		+ "&winCurIndex="+request.getParameter("winCurIndex"))
	                		  .setAlertRequest(request);
	        return getForward(request, mapping, "message");
		}
		return forCur;
	}
    
    /**
     * goInput
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward goInput(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
            Exception {
        // request.setAttribute("detail", "detail");
        ActionForward forward = null;
        Object nodeType_obj = request.getParameter("nodeType");
        if (null != nodeType_obj) {
            String nodeType = nodeType_obj.toString();
            if (null != nodeType && nodeType.equals("check")) {
                request.setAttribute("isCheck", true);
            }
        }
        /*�����ֵܱ��ID*/
        String f_brother = this.getParameter("f_brother", request);
        f_brother = f_brother == null ? "" : f_brother;
        request.setAttribute("f_brother", f_brother);
        
        String tableName = getParameter("tableName", request);
        request.setAttribute("tableName", tableName);
        String parentTableName = getParameter("parentTableName", request);
        request.setAttribute("parentTableName", parentTableName);
        Hashtable allTables = (Hashtable) request.getSession()
        								 .getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        LoginBean lg = new LoginBean();
        lg = (LoginBean) request.getSession().getAttribute("LoginBean");
        DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
        Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        //�����˷�֧����ʱ�����������ĩ����֧�����������޸Ĳ����������
        String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
        boolean openSunCompany = Boolean.parseBoolean(openValue);
        boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
        boolean isSharedTable = tableInfo.getIsSunCmpShare() == 1 ? true : false;
        //����ְԱ����ű�ʱ������ĩ����֧��������
        boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo.
                getTableName()) ||
                              "tblDepartment".equalsIgnoreCase(tableInfo.
                getTableName())
                              ? true : false;
        boolean isDetail = true;  //���޸Ļ�������
        if (openSunCompany && !isLastSunCompany && !isSharedTable &&
            !isEmpOrDept && !isDetail) {
            EchoMessage.error().add(getMessage(request,
                                               "common.msg.notLastSunCompany")).
                    setRequest(request);
            return getForward(request, mapping, "message");
        }

        String nstr = request.getParameter("keyId");
        request.setAttribute("keyId", nstr);

        // BaseEnv.log.debug(workFlowNodeName);

        if (nstr != null && nstr.length() != 0) {
            String keyId = nstr;
            //ִ�м�����ϸ
            Hashtable map = (Hashtable) request.getSession().getServletContext()
            												.getAttribute(BaseEnv.TABLE_INFO);
            String sunClassCode = lg.getSunCmpClassCode();  //��LoginBean��ȡ����֧������classCode
            Hashtable props = (Hashtable) request.getSession().getServletContext().
            getAttribute(BaseEnv.PROP_INFO);
            MOperation mop = getMOperation(map, request, tableName);
            Result rs = dbmgt.detail(tableName, map, keyId, sunClassCode,props,lg.getId(),isLastSunCompany,"");

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            	HashMap hm = (HashMap) rs.retVal;
            	HashMap moreLan=(HashMap)hm.get("LANGUAGEQUERY");
            	if(!isDetail){
            	String sysParamter = tableInfo.getSysParameter();//����Ϣϵͳ����
            	Date time=null;
            	for(int i=0;i<tableInfo.getFieldInfos().size();i++){
            		DBFieldInfoBean bean=(DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
            		if(bean.getDefaultValue()!=null&&"accendnotstart".equals(bean.getFieldIdentityStr())){
            			String timeStr=hm.get(bean.getFieldName()).toString();
            			if(timeStr!=null){
            			time=BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
            			}
            		}
            	}
            	int currentMonth = 0;
            	int currentYear=0;
                if (null != time) {
                    currentMonth = time.getMonth() + 1;
                    currentYear=time.getYear()+1900;
                }
                int periodMonth = -1;
                int periodYear=-1;
                AccPeriodBean accBean=(AccPeriodBean)sessionSet.get("AccPeriod");
                if (accBean!=null) {
                    periodMonth = accBean.getAccMonth();
                    periodYear=accBean.getAccYear();

                }
                if ((currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) ||periodMonth < 0 ) && currentMonth != 0) {
                        if (null != sysParamter) {
                            if (sysParamter.equals("CurrentAccBefBill") &&(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth))) {
                                EchoMessage.error().add(getMessage(request,
                                        "com.cantupdatebefbill")).
                                        setAlertRequest(request);
                                return getForward(request, mapping, "message");
                            }

                            if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") &&
                            		(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) ||periodMonth < 0 )) {
                                EchoMessage.error().add(getMessage(request,
                                        "com.cantupdatebefbill")).
                                        setAlertRequest(request);
                                return getForward(request, mapping, "message");
                            }
                        }

                    }
            	}
            	//�����еĵ����������
            	for(int i=0;i<tableInfo.getFieldInfos().size();i++){
            		DBFieldInfoBean bean=(DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
            		if(bean.getInputType()==bean.INPUT_MAIN_TABLE){
            			if(bean.getSelectBean()!=null){
	            			for(int j=0;j<bean.getSelectBean().getViewFields().size();j++){
	            				PopField pop=(PopField)bean.getSelectBean().getViewFields().get(j);
	                        	DBFieldInfoBean bean2=GlobalsTool.getFieldBean(pop.getFieldName().substring(0,pop.getFieldName().indexOf(".")),pop.getFieldName().substring(pop.getFieldName().indexOf(".")+1));
	                        	if(bean2.getInputType()==bean2.INPUT_LANGUAGE||bean2.getInputTypeOld()==bean2.INPUT_LANGUAGE){
	                        		if(moreLan.get(hm.get(pop.getAsName()))!=null){
	                        			hm.put(pop.getAsName(), ((KRLanguage)moreLan.get(hm.get(pop.getAsName()))).get(GlobalsTool.getLocale(request).toString()));
	                        		}else{
	                        			hm.put(pop.getAsName(), hm.get(pop.getAsName()));
	                        		}
	                        	}
	            			}
            			}
            		}
            	}
            	
            	ArrayList rowsList = new ArrayList();
                ArrayList childTableList = DDLOperation.getChildTables(tableName, map);

                // �����������Զ�������
                String allTableName = tableName+ "," ;
                ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
                ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;//�����������
                Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;//�ӱ��������
                Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
        												request.getSession().getServletContext().getAttribute("userSettingColConfig") ;

                if(userColConfig!=null && userColConfig.size()>0){
                	configList = userColConfig.get(tableName+"bill") ;
                	if(configList!=null){
                		allConfigList.addAll(configList) ;
                	}
                }


                for(int i=0;i<childTableList.size();i++){
                	DBTableInfoBean childTableInfo=(DBTableInfoBean)childTableList.get(i);
                	//������ϸ���Զ����е���ʾ
                	if(allConfigList!=null && allConfigList.size()>0){
                		allTableName += childTableInfo.getTableName()+"," ;
                		ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName()+"bill") ;
                    	if(childConfigList!=null){
                    		allConfigList.addAll(childConfigList) ;
                    		childTableConfigList.put(childTableInfo.getTableName(), childConfigList) ;
                    	}
                	}
                }

                request.setAttribute("allConfigList", allConfigList) ;
                request.setAttribute("allTableName", allTableName) ;
                request.setAttribute("childTableConfigList", childTableConfigList) ;
                Collections.sort(childTableList, new SortDBTable()) ;
                request.setAttribute("childTableList", childTableList) ;
                HashMap tblChildMap=new HashMap();
                for(int i=0;i<childTableList.size();i++){
                	tblChildMap.put(((DBTableInfoBean)childTableList.get(i)).getTableName(),String.valueOf(i));
                	tblChildMap.put(String.valueOf(i),((DBTableInfoBean)childTableList.get(i)).getTableName());
                }
                request.setAttribute("tblChildMap", tblChildMap);
                // DBTableInfoBean tableinfo = DDLOperation.getTableInfo(map, tableName);
                //����û��Ѿ��������Զ��������ã����ȡ�û��Զ���������
                //����û��Ѿ��������Զ��������ã����ȡ�û��Զ���������
                ArrayList<DBFieldInfoBean> fieldList = new ArrayList<DBFieldInfoBean>() ;
                ArrayList<DBFieldInfoBean> dbList = new ArrayList<DBFieldInfoBean>();
                if(PersistentBag.class.getName().equals(tableInfo.getFieldInfos().getClass().getName())){
                	PersistentBag bag = (PersistentBag) tableInfo.getFieldInfos();
                	for (Object object : bag) {
       					dbList.add((DBFieldInfoBean) object);
       				}
                }else{
                	dbList = (ArrayList<DBFieldInfoBean>) tableInfo.getFieldInfos() ;
                }
                //�����������Զ�������
                if(configList!=null && configList.size()>0){
                		for(ColConfigBean configBean : configList){
                			for(DBFieldInfoBean fieldBean : dbList){
                				if(configBean.getColName().equals(fieldBean.getFieldName())){
                					fieldList.add(fieldBean) ;
                					break ;
                				}
                			}
                		}
                		for(DBFieldInfoBean fieldBean : dbList){
                			if(3==fieldBean.getInputType() || 100==fieldBean.getInputType()){
                				fieldList.add(fieldBean) ;
                			}else{
                				boolean isExist = false ;
                				for(ColConfigBean configBean : configList){
                					if(configBean.getColName().equals(fieldBean.getFieldName())){
                						isExist = true ;
                					}
                				}
                				if(!isExist){
        	        				DBFieldInfoBean field = DynDBManager.cloneObject(fieldBean) ;
        	        				field.setInputType((byte)3) ;
        	        				fieldList.add(field) ;
                				}
                			}
                		}
                }

                if(fieldList.size()==0){
                	fieldList = dbList ;
                }
                request.setAttribute("fieldInfos", DynDBManager.distinctList(fieldList)) ;
                if (childTableList != null) {
                	Collections.sort(childTableList, new SortDBTable()) ;
                    request.setAttribute("childTableList2", childTableList);
                }

                //��Ϊ��˻�ͨ����ͬ�ķ�ʽʵ�֣����������չ��ť�ȣ�����ֻ��ͨ����˵�״̬���ж�
                String flowStatus=hm.get("workFlowNodeName")==null?"":hm.get("workFlowNodeName").toString();

                request.setAttribute("flowStatus", flowStatus);
                for (int k = 0;childTableList != null && k < childTableList.size();k++) {
                    DBTableInfoBean childTable = (DBTableInfoBean)childTableList.get(k);
                    ArrayList cols = new ArrayList();
                    ArrayList rows = new ArrayList();
                    ArrayList truncIndexs=new ArrayList();
                    if(childTableConfigList!=null && childTableConfigList.size()>0){//�û��Զ���������
                    	DBTableInfoBean tableInfoBean = (DBTableInfoBean) childTableList.get(k) ;
                    	ArrayList<ColConfigBean> cofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
                    	for(int m=0;cofingList!=null && m<cofingList.size();m++){
                    		ColConfigBean configBean = cofingList.get(m) ;
                    		GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)GlobalsTool.getPropBean(configBean.getColName());
                    		boolean isExist = false ;
                    		for (int i = 0;childTable != null &&i < childTable.getFieldInfos().size(); i++) {
    	                        DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);

    	                        String[] ss = new String[5];
    	                        if ((fi.getInputType() ==DBFieldInfoBean.INPUT_MAIN_TABLE
    	                        		|| (fi.getInputType()==6 && fi.getInputTypeOld()==2)) && fi.getSelectBean()!=null&&(!(gpInfo!=null&&gpInfo.getIsSequence()==1))) {

    	                        		if(!fi.getSelectBean().getRelationKey().hidden
    	                        							&& configBean.getColName().equals(fi.getFieldName())){
    	                        			Object o = fi.getDisplay().get(getLocale(request).toString());
    	                        			ss = new String[5];
    	         	                        setFieldString(childTable, fi, ss, o);
    	         	                        cols.add(ss);
    	                        		}
    	                        		GoodsPropInfoBean tempProp=GlobalsTool.getPropBean(fi.getFieldName());
    	                        		if(!(tempProp!=null&&tempProp.getIsSequence()==1)){
    		                            for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
    		                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
    		                                if(configBean.getColName().equals(temps.getAsName()) ){
    		                                	DBFieldInfoBean f=GlobalsTool.getFieldBean(temps.getFieldName().substring(0,temps.getFieldName().indexOf(".")), temps.getFieldName().substring(temps.getFieldName().indexOf(".")+1));
	    		                                ss = new String[5];
	    		    		                    ss[2] = DBFieldInfoBean.getFieldTypeString(f);
	    		                                ss[3] = temps.getAsName();
	    		                                ss[4] = temps.getFieldName();
	    		                                cols.add(ss);
	    		                                isExist=true ;
	    		                                break ;
    		                                }
    		                            }
    	                        	}

    	                        }else if(fi.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE&&gpInfo!=null&&gpInfo.getIsSequence()==1
    	                        		&& configBean.getColName().equals(fi.getFieldName())){
    	                        	Object o = fi.getDisplay().get(getLocale(request).toString());
         	                        setFieldString(childTable, fi, ss, o);
    	                        	cols.add(ss);
    	                        	cols.add(ss);
 		                            truncIndexs.add(cols.size()-1);
    	                        }else{
    	                        	if(configBean.getColName().equals(fi.getFieldName())){
	    	                        	Object o = fi.getDisplay().get(getLocale(request).toString());
	    		                        setFieldString(childTable, fi, ss, o);
	    		                        cols.add(ss);

	    	                        	if(fi.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||fi.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE){
	        	                        	String[] tempstr=new String[5];
	        	                        	tempstr[0]=ss[0];
	        	                        	tempstr[1]=ss[1];
	        	                        	tempstr[2]="lan";
	        	                        	tempstr[3] = fi.getFieldName();
	        	                        	tempstr[4] = childTable.getTableName();
	        	                            cols.add(tempstr);
	    	                        	}
	    	                        	isExist=true ;
	    	                        	break ;
    	                        	}
    	                        }
    	                        if(isExist){
    	                        	break ;
    	                        }
                    	    }
                    	}
                    	//���������ֶ�ֵ
                    	for (int i = 0;childTable != null &&i < childTable.getFieldInfos().size(); i++) {
                    		DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
                    		GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)GlobalsTool.getPropBean(fi.getFieldName());
                    		if(fi.getInputType()==3){
                    			String[] ss = new String[5];
    	                        Object o = fi.getDisplay().get(getLocale(request).toString());
    	                        setFieldString(childTable, fi, ss, o);
    	                        cols.add(ss);
                    		}else if(fi.getInputType()==2 && "GoodsField".equals(fi.getFieldSysType()) 
                    									  && gpInfo!=null && gpInfo.getIsSequence()==1){
                    			boolean isExist = false;
								ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName());
								for (int m = 0; childCofingList != null && m < childCofingList.size(); m++) {
									ColConfigBean configBean = childCofingList.get(m);
									if ((fi.getFieldName().equals(configBean.getColName()))) {
										isExist = true;
										break;
									}
								}
								if (!isExist) {
									String[] ss = new String[5];
									Object o = fi.getDisplay().get(getLocale(request).toString());
									setFieldString(childTable, fi, ss, o);
									cols.add(ss);
								}
                    		}else if(fi.getInputType()==2 && fi.getSelectBean()!=null && fi.getInputValue().length()>0
                    							&& fi.getSelectBean().getViewFields().size()>0){
                    			if(fi.getSelectBean().getRelationKey().hidden){
	                    			String[] ss = new String[5];
	    	                        Object o = fi.getDisplay().get(getLocale(request).toString());
	    	                        setFieldString(childTable, fi, ss, o);
	    	                        cols.add(ss);
                    			}
                    			for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
	                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
	                                DBFieldInfoBean f=GlobalsTool.getFieldBean(temps.getFieldName().substring(0,temps.getFieldName().indexOf(".")), temps.getFieldName().substring(temps.getFieldName().indexOf(".")+1));
	                                boolean isExist = false ;
	                    			ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
	                    			for(int m=0;childCofingList!=null&&m<childCofingList.size();m++){
	                    				ColConfigBean configBean = childCofingList.get(m) ;
	                    				if(temps.getAsName().equals(configBean.getColName())){
	                    					isExist = true ;
	                    					break ;
	                    				}
	                    			}
	                    			if(!isExist){
		                                String[] ss = new String[5];
		    		                    ss[2] = DBFieldInfoBean.getFieldTypeString(f);
		                                ss[3] = temps.getAsName();
		                                ss[4] = temps.getFieldName();
		                                cols.add(ss);
	                    			}
                    			}
                    		}else if(fi.getInputType()!=100 && !fi.getFieldName().equals("id")
                    								&& !fi.getFieldName().equals("f_ref") && !fi.getFieldName().equals("f_brother")){
                    			boolean isExist = false ;
                    			ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
                    			for(int m=0;childCofingList!=null&&m<childCofingList.size();m++){
                    				ColConfigBean configBean = childCofingList.get(m) ;
                    				if(fi.getFieldName().equals(configBean.getColName())){
                    					isExist = true ;
                    					break ;
                    				}
                    			}
                    			if(!isExist && fi.getDisplay()!=null){
                    				String[] ss = new String[5];
        	                        Object o = fi.getDisplay().get(getLocale(request).toString());
        	                        setFieldString(childTable, fi, ss, o);
        	                        cols.add(ss);
                    			}
                    		}
                    	}
                    }else{
                    	for (int i = 0;childTable != null &&i < childTable.getFieldInfos().size(); i++) {
	                        DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);

	                        if (fi.getFieldName().equals("id") ||
	                            fi.getFieldName().equals("f_ref") ||
	                            fi.getInputType() == DBFieldInfoBean.INPUT_NO) {
	                            continue;
	                        }
	                        String[] ss = new String[5];
	                        Object o = fi.getDisplay().get(getLocale(request).toString());
	                        setFieldString(childTable, fi, ss, o);
	                        cols.add(ss);
	                        //�ҳ������ֶ�
	                        if (fi.getInputType() ==
	                            DBFieldInfoBean.INPUT_MAIN_TABLE) {
	                        	GoodsPropInfoBean gpInfo=GlobalsTool.getPropBean(fi.getFieldName());
	                        	if(fi.getSelectBean()!=null){
	                        		if(!(gpInfo!=null&&gpInfo.getIsSequence()==1)){
		                            for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
		                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
		                                //if(!"true".equals(temps.hiddenInput)){
			                                DBFieldInfoBean f=GlobalsTool.getFieldBean(temps.getFieldName().substring(0,temps.getFieldName().indexOf(".")), temps.getFieldName().substring(temps.getFieldName().indexOf(".")+1));
			                                ss = new String[5];
			    		                    ss[2] = DBFieldInfoBean.getFieldTypeString(f);
			                                ss[3] = temps.getAsName();
			                                ss[4] = temps.getFieldName();
			                                cols.add(ss);
		                                //}
		                             }
	                        		}
	                        	}
	                        }
	                        GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)GlobalsTool.getPropBean(fi.getFieldName());
	                        if(fi.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE&&gpInfo!=null&&gpInfo.getIsSequence()==1){
	                            cols.add(ss);
	                            truncIndexs.add(cols.size()-1);
	                        }
	                        if(fi.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||fi.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE){
	                        	String[] tempstr=new String[5];
	                        	tempstr[0]=ss[0];
	                        	tempstr[1]=ss[1];
	                        	tempstr[2]="lan";
	                        	tempstr[3] = fi.getFieldName();
	                        	tempstr[4] = childTable.getTableName();
	                            cols.add(tempstr);
	                        }

	                    }
                    }

                    if (childTable != null) {
                        List childList = (List) hm.get("TABLENAME_" +
                                childTable.getTableName());
                        for (int i = 0; i < childList.size(); i++) {
                            HashMap os = (HashMap) (childList).get(i);
                            String[] ss = new String[2];
                            ss[0] = "";
                            for (int j = 0; j < cols.size(); j++) {
                                Object sv = os.get(((String[]) cols.get(j))[3]);
                                DBFieldInfoBean bean;
                                if(((String[]) cols.get(j))[3].indexOf(".")<0){
                                	bean= GlobalsTool.getFieldBean(childTable.getTableName(),((String[]) cols.get(j))[3]);
                                }else{
                                	bean=GlobalsTool.getFieldBean(((String[]) cols.get(j))[4].substring(0,((String[]) cols.get(j))[4].indexOf(".")), ((String[]) cols.get(j))[4].substring(((String[]) cols.get(j))[4].indexOf(".")+1));
                                }
    	                        if(truncIndexs.contains(j)){
                                	if(sv!=null&&sv.toString().length()>0){
                                		String[]seqList=sv.toString().split("~");
                                		sv=seqList[seqList.length-1];
                                	}
                                }
                                if((bean.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||bean.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE)&&"lan".equals(((String[]) cols.get(j))[2])){
                                	if(moreLan.get(sv)!=null){
                                		sv=((KRLanguage)moreLan.get(sv)).toString();
                                	}
                                }else if(bean.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||bean.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE){
                                	if(moreLan.get(sv) !=null){
                                		sv=((KRLanguage)moreLan.get(sv)).get(this.getLocale(request).toString());
                                	}
                                }
                                if ("float".equals(((String[]) cols.get(j))[2])) {
                                    String df = ((String[]) cols.get(j))[3];
                                    String dt = null;
                                    if (df.indexOf(".") > 0) {
                                        dt = df.substring(0, df.indexOf("."));
                                        df = df.substring(df.indexOf(".") + 1);
                                    } else {
                                        dt = ((String[]) cols.get(j))[4];
                                    }
                                    if(isDetail){
                                    ss[0] = ss[0] + "'" +
                                          GlobalsTool.formatNumber(Double.
                                          parseDouble(sv == null ? "0" :
                                          sv.toString()), false, false,
                                          "true".equals(BaseEnv.systemSet.get(
                                          "intswitch").getSetting()), dt, df, false) +
                                          "',";
                                    }else{
                                    ss[0] = ss[0] + "'" +
                                            GlobalsTool.newFormatNumber(Double.
                                            parseDouble(sv == null ? "0" :
                                            sv.toString()), false, false,
                                            "true".equals(BaseEnv.systemSet.get(
                                            "intswitch").getSetting()), dt, df, false) +
                                            "',";
                                    }
                                } else {
                                    ss[0] = ss[0] + "'" + (sv == null ? "" : sv) +
                                            "',";
                                }
                            }
                            ss[0] = ss[0].substring(0, ss[0].length() - 1);
                            ss[1] = os.get("id").toString();
                            rows.add(ss);
                        }
                    }
                    rowsList.add(new Object[] {childTable.getTableName(), rows});
                }
                
                DBTableInfoBean mainTable = DDLOperation.getTableInfo(map,
                        tableName);

                //������չ��ť��Ϣ
                String butTag = parseExtendButton(request, mainTable, "update");
                if (butTag != null && butTag.length() > 0) {
                    request.setAttribute("extendButton", butTag);
                }

                BaseCustomFunction impl = (BaseCustomFunction) BaseEnv.
                                          functionInterface.
                                          get(tableName);
                if (impl != null) {
                    impl.onUpdatePrepare(mapping, form, request, response,
                                         getLoginBean(request), keyId,
                                         tableName, map,
                                         hm);
                }
                //��ѯ�Ƿ���ͼƬ�͸���
                ArrayList picList = new ArrayList();
                ArrayList affixList = new ArrayList();
                for (int i = 0; i < mainTable.getFieldInfos().size(); i++) {
                    DBFieldInfoBean fi = (DBFieldInfoBean) mainTable.
                                         getFieldInfos().get(i);
                    if (fi.getFieldType() == DBFieldInfoBean.FIELD_PIC) {
                        String pic = (String) ((HashMap) rs.retVal).get(fi.
                                getFieldName());
                        if (pic != null && pic.length() > 0) {
                            String pics[] = pic.split(";");
                            for (int j = 0; j < pics.length; j++) {
                                picList.add(pics[j]);
                            }
                        }
                    } else if (fi.getFieldType() == DBFieldInfoBean.FIELD_AFFIX) {
                        String pic = (String) ((HashMap) rs.retVal).get(fi.
                                getFieldName());
                        if (pic != null && pic.length() > 0) {
                            String pics[] = pic.split(";");
                            for (int j = 0; j < pics.length; j++) {
                                affixList.add(pics[j]);
                            }
                        }
                    }

                }

                HashMap values = (HashMap) rs.retVal;

                Object copy = request.getAttribute("copy");
                if (copy != null && copy.toString().equals("copy")) {
                	 //�ڸ���ʱ�������ݱ�ŵ�Ĭ��ֵ����@CODE:���ֶ�ֵ�ĳ���һ�����ظ��ĵ��ݱ��
                    for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
                        DBFieldInfoBean field = (DBFieldInfoBean) tableInfo.
                                                getFieldInfos().get(i);
                        if (field.getDefaultValue() != null &&
                            field.getDefaultValue().contains("@CODE:")) {
                            values.put(field.getFieldName(), field.getDefValue());
                        }
                    }
                }
                //ȡ�ò�ѯʱ�ķ�ΧȨ��
                //MOperation mop = getMOperation(map, request, tableName);

                request.setAttribute("MOID", mop.moduleId);

                request.setAttribute("PIC", picList);
                request.setAttribute("AFFIX", affixList);

                request.setAttribute("mainTable", mainTable);
                request.setAttribute("values", values);
                request.setAttribute("tableName",
                                     mainTable.getDisplay().get(getLocale(
                                             request).
                        toString()).toString());
                request.setAttribute("result", rowsList);
                request.setAttribute("tableName", tableName);
                request.setAttribute("parentCode",
                                     request.getParameter("parentCode") == null ?
                                     "" : request.getParameter("parentCode"));
                
                String moduleType = getParameter("moduleType", request) ;
                //��ѯ�˵����Ƿ�����ƴ�ӡ��ʽ������Ѿ��������ʾ��ӡ��ť
                ReportSetMgt mgt = new ReportSetMgt();
                Result rsPrint = mgt.getBillTable(tableName,moduleType);
                if (rsPrint.getRealTotal() > 0) {
                    List list = (List) rsPrint.getRetVal();
                    ReportsBean report = (ReportsBean) list.get(0);
                    Map det = report.getReportDetBean();
                    Collection con = det.values();
                    Iterator iter = con.iterator();
                    while (iter.hasNext()) {
                        ReportsDetBean detBean = (ReportsDetBean) iter.next();
                        if (detBean.getNewFlag().equals("OLD")) {
                            request.setAttribute("reportNumber",report.getReportNumber());
                            request.setAttribute("print", "true");
                            break;
                        }
                    }
                }
                //���سɹ�
            } else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
            	  //��¼�����ڴ���
                EchoMessage.error().add(getMessage(
                        request, "common.error.nodata")).setRequest(request);
                return getForward(request, mapping, "message");
            } else {
            	 //����ʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                        setRequest(request);
                return getForward(request, mapping, "message");
            }
        }

        return forward;
    }


    /**
     * ֻ�Ǽ򵥵���ȡ���룬�����κδ���
     * @param childTable
     * @param fi
     * @param ss
     * @param o
     */
	private void setFieldString(DBTableInfoBean childTable, DBFieldInfoBean fi, String[] ss, Object o) {
		ss[0] = o == null ? fi.getFieldName() : o.toString();
		ss[2] = DBFieldInfoBean.getFieldTypeString(fi);
		ss[3] = fi.getFieldName();
		ss[4] = childTable.getTableName();
	}

	
   
    /**
     * goConsign
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward goConsign(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        return getForward(request, mapping, "addCosign");
    }


    /**
     * �����ݷ��͵��ⲿ����
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward send_email(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
    	String tableName = getParameter("tableName", request);
        String parentTableName = getParameter("parentTableName", request);
        String kid = getParameter("kid", request);
        String parentCode = getParameter("parentCode", request);
        String userId = this.getLoginBean(request).getId();
        String from = request.getParameter("from");
        List list = (List)new EMailMgt().selectAccountByEmail(from).getRetVal() ;
        MailinfoSettingBean obj = null ;
        if(list.size()!=0){
        	obj = (MailinfoSettingBean) list.get(0) ;
        }
        String to = request.getParameter("to");
        String subject = request.getParameter("subject");
        String html = request.getParameter("Notepadcontent");
        String creatTime = BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss);
        Result rs = null;

        if (null != obj) {
            try {
                
				// ָ���ʺź�����
                EMailMessage smes = new EMailMessage();
                smes.addRecipient(EMailMessage.TO, to,"");
                smes.setSubject(subject);
                smes.setContent(true, html);                
                
                new EMailMgt().sendByThread(smes, obj, null, userId);              

                rs = new EMailMgt().addMail(from, to, subject,creatTime, html,
                        1, "",userId,obj.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
                EchoMessage.error().add(getMessage(
                        request, "common.msg.sendError"))
                        .setBackUrl("/UserFunctionAction.do?keyId=" + kid +"&operation="+OperationConst.OP_DETAIL+
                                    "&tableName=" + tableName + "&parentCode=" +
                                    parentCode + "&checkTab=Y"
                                    + "&parentTableName="+parentTableName+"&winCurIndex="+request.getParameter("winCurIndex")).
                        setAlertRequest(request);
                return getForward(request, mapping, "message");

            }
        } else {
            EchoMessage.info().add(getMessage(
                    request, "oa.mail.notCreateMailAccount"))
                    .setBackUrl("/UserFunctionAction.do?keyId=" + kid +
                            "&tableName=" + tableName + "&parentCode=" +parentCode 
                            + "&operation=" +OperationConst.OP_DETAIL +"&checkTab=Y"
                            + "&parentTableName="+parentTableName
                            +"&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
        }

        //String
        if (null != rs) {
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            	 //��ӳɹ�
                EchoMessage.success().add(getMessage(
                        request, "common.msg.sendSuccess"))
                        .setBackUrl("/UserFunctionAction.do?keyId=" + kid +
                                    "&tableName=" + tableName + "&parentCode=" 
                                    + parentCode + "&operation=" + OperationConst.OP_DETAIL
                                    + "&checkTab=Y&parentTableName="+parentTableName
                                    +"&winCurIndex="+request.getParameter("winCurIndex")).
                        setAlertRequest(request);
                return getForward(request, mapping, "message");
            } else {
            	 //���ʧ��
                EchoMessage.error().add(getMessage(
                        request, "common.msg.sendError"))
                        .setBackUrl("/UserFunctionAction.do?keyId=" + kid +
                                    "&tableName=" + tableName 
                                    + "&parentTableName="+parentTableName
                                    + "&checkTab=Y&parentCode=" + parentCode + "&operation=" +  OperationConst.OP_DETAIL
                                    +"&winCurIndex="+request.getParameter("winCurIndex")).
                        setAlertRequest(request);
                return getForward(request, mapping, "message");
            }

        } else {
            EchoMessage.error().add(getMessage(
                    request, "common.msg.sendError"))
                    .setBackUrl("/UserFunctionAction.do?keyId=" + kid +
                                "&tableName=" + tableName + "&parentTableName="+parentTableName
                                + "&checkTab=Y&parentCode=" + parentCode + "&operation=" + OperationConst.OP_DETAIL
                                +"&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");

        }
    }
    
    private boolean hasRight(String url,LoginBean lb){
    	boolean right= false;
    	if(url.startsWith("/UserFunctionQueryAction.do?tableName=")){
    		String rightUrl = url;
    		if(url.indexOf("&") > 0){
    			rightUrl = url.substring(0,url.indexOf("&"));
    		}
    		String moduleType="";
    		if(url.indexOf("moduleType=")>0){
    			if(url.indexOf("&",url.indexOf("moduleType=")) > 0){
    				moduleType = url.substring(url.indexOf("moduleType=")+"moduleType=".length(),url.indexOf("&",url.indexOf("moduleType=")));
        		}else{
        			moduleType = url.substring(url.indexOf("moduleType=")+"moduleType=".length());
        		}
    		}
    		rightUrl = moduleType.length() > 0 ?rightUrl +"&moduleType="+moduleType:rightUrl;
    		return (MOperation)lb.getOperationMap().get(rightUrl) != null;    		
    	}else if(url.startsWith("/UserFunctionAction.do?tableName=")){
    		String rightUrl = url;
    		if(url.indexOf("&") > 0){
    			rightUrl = url.substring(0,url.indexOf("&"));
    		}
    		String moduleType="";
    		if(url.indexOf("moduleType=")>0){
    			if(url.indexOf("&",url.indexOf("moduleType=")) > 0){
    				moduleType = url.substring(url.indexOf("moduleType=")+"moduleType=".length(),url.indexOf("&",url.indexOf("moduleType=")));
        		}else{
        			moduleType = url.substring(url.indexOf("moduleType=")+"moduleType=".length());
        		}
    		}
    		rightUrl = rightUrl.replaceFirst("UserFunctionAction", "UserFunctionQueryAction");
    		rightUrl = moduleType.length() > 0 ?rightUrl +"&moduleType="+moduleType:rightUrl;
    		return (MOperation)lb.getOperationMap().get(rightUrl) != null;    		
    	}else if(url.indexOf("detTable_list=") > 0 ){
    		String rightUrl = url;
    		String detTable_list="";
    		if(url.indexOf("detTable_list=")>0){
    			if(url.indexOf("&",url.indexOf("detTable_list=")) > 0){
    				detTable_list = url.substring(url.indexOf("detTable_list=")+"detTable_list=".length(),url.indexOf("&",url.indexOf("detTable_list=")));
        		}else{
        			detTable_list = url.substring(url.indexOf("detTable_list=")+"detTable_list=".length());
        		}
    		}
    		String moduleType="";
    		if(url.indexOf("moduleType=")>0){
    			if(url.indexOf("&",url.indexOf("moduleType=")) > 0){
    				moduleType = url.substring(url.indexOf("moduleType=")+"moduleType=".length(),url.indexOf("&",url.indexOf("moduleType=")));
        		}else{
        			moduleType = url.substring(url.indexOf("moduleType=")+"moduleType=".length());
        		}
    		}
    		if(detTable_list.length() > 0){
    			//�����ϸ����Ȩ�������ݱ��б�������
    			rightUrl = "/UserFunctionQueryAction.do?tableName="+detTable_list;
    			if(moduleType != null && moduleType.length() > 0 ){
    				rightUrl +="&moduleType="+moduleType;
    			}
    			return (MOperation)lb.getOperationMap().get(rightUrl) != null; 
    		} else {
    			return false;
    		}
    		
        }else if(url.startsWith("/ReportDataAction.do?reportNumber=")){
    		String rightUrl = url;
    		if(url.indexOf("&mainNumber=") > 0){
    			int pos = url.indexOf("&mainNumber=") + "&mainNumber=".length();
    			String mainNumber = url.substring(pos,url.indexOf("&",pos));
    			rightUrl = "/ReportDataAction.do?reportNumber="+mainNumber;
    		}else if(url.indexOf("&moduleType=") > 0){
    			if(url.indexOf("&",url.indexOf("&moduleType=")+"&moduleType=".length()) > 0){
    				rightUrl = url.substring(0,url.indexOf("&",url.indexOf("&moduleType=")+"&moduleType=".length()));
    			}
    		}else if(url.indexOf("&") > 0){
    			rightUrl = url.substring(0,url.indexOf("&"));
    		}
    		return lb.getOperationMap().get(rightUrl) != null;
    	}else if(url.startsWith("/VoucherManageAction.do")){
    		return lb.getOperationMap().get("/VoucherManageAction.do") != null;
    	} else{
    		//�����ǵ��ݺͱ���
    		return true;
    	}
    }
    
    /**
     * �����Ӧ��ģ���Ƿ�����
     * @param tableName
     * @return
     */
    private boolean isStartModule(String tableName){
    	for(ModuleBean mb: (ArrayList<ModuleBean>)BaseEnv.allModule){
    		if(recIsStartModule(tableName,mb)){
    			return true;
    		}
    	}
    	return false;
    }
    private boolean recIsStartModule(String tableName,ModuleBean mb){
    	if(mb.getLinkAddress() != null && (mb.getLinkAddress().equalsIgnoreCase("/UserFunctionQueryAction.do?tableName="+tableName)
    			|| mb.getLinkAddress().toLowerCase().startsWith(("/UserFunctionQueryAction.do?tableName="+tableName+"&moduleType=").toLowerCase()))){
    		return true;
    	}
    	if(mb.getChildList() != null && mb.getChildList().size() > 0){
    		for(ModuleBean mb2: (ArrayList<ModuleBean>)mb.getChildList()){
        		if( recIsStartModule(tableName,mb2)){
        			return true;
        		}
        	}
    	}
    	return false;
    }

    /**
     * �����б�������չ��ť
     * @param tableInfo DBTableInfoBean ��ṹ
     * @param operation String ��������
     * @return String ����һ��<button>��ť
     *
     * <button type="copy" operation="list">
     * <button type="quote" operation="add" value="tblBuyOrder:quote_buyOrder">
     * <button type="define" operation="list" value="activeX">
     */

    private String parseExtendButton(HttpServletRequest request,
                                     DBTableInfoBean tableInfo,
                                     String operation) throws Exception {
    	//����Ƿ��ǲݸ��б�
    	String draft=(operation.contains("draft")?"draft":"");
    	operation=(operation.contains("draft")?"list":operation);
    	if("copy".equals(request.getAttribute("copy"))||"quote".equals(request.getAttribute("quote"))||
    			"draft".equals(request.getAttribute("saveDraft")) || "saveDraft".equals(request.getAttribute("saveDraft"))){
    		operation = "add";
    	}
    	
    	if(operation.equals("add")|| operation.equals("update")){
	    	Result smsResult = userMgt.querySMSModel(tableInfo.getTableName(),operation) ;
	        if(smsResult!=null && smsResult.retVal!=null){
	        	String[] exeArray = (String[]) smsResult.retVal ;
	        	if("11".equals(exeArray[0]) || "33".equals(exeArray[0])){
	        		request.setAttribute("popedomUserIds", exeArray[2]) ;
	        		request.setAttribute("popedomDeptIds", exeArray[1]) ;
	        		request.setAttribute("otherEmail", exeArray[3]) ;
	        		request.setAttribute("otherSMS", exeArray[4]) ;
	        		request.setAttribute("existSMS", true) ;
	        	}
	        }
    	}
    	LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
    	
        StringBuffer butTag = new StringBuffer("");
        StringBuffer quoteBtn = new StringBuffer() ;
        StringBuffer pushBtn = new StringBuffer() ; //�Ƶ������а�Ť
        StringBuffer queryBtn = new StringBuffer() ; //��������а�Ť
        StringBuffer toolBtn = new StringBuffer() ; //���ߵ����а�Ť
        String extendButton = tableInfo.getExtendButton();
        LoginBean loginBean = this.getLoginBean(request);
        Object tableName=request.getAttribute("tableName");
        String parentTableName=this.getParameter("parentTableName",request);
        parentTableName=parentTableName==null?"":parentTableName;
        String moduleType = getParameter("moduleType", request) ;
        moduleType=moduleType==null?"":moduleType;
        
        MOperation mop = GlobalsTool.getMOperationMap(request) ;
        int n = 1 ;  //��¼�м�����չ���ð�ť
        if (extendButton != null) {
            while (extendButton.indexOf("<button") > -1) {
            	if(extendButton.indexOf(">")==-1){
            		BaseEnv.log.error("UserFunctionAction.parseExtendButton Error "+extendButton);
            	}
                String button = extendButton.substring(extendButton.indexOf(
                        "<button"), extendButton.indexOf(">"));
                
                extendButton = extendButton.substring(extendButton.indexOf(">") +
                        1);
                String tagOP = getAttribute("operation", button);
                String type = getAttribute("type", button);
                String right= getAttribute("right", button);
                String fieldSysType=getAttribute("fieldSysType", button);//�Զ�����չ��ť��ָ�����ֶε�ϵͳ���ã�����ĳϵͳ����ʱ���˰�ť����ʾ
                
                if(type.equals("custom")){
                	request.setAttribute("right", "query");
                }else if(type.equals("define")){
                	request.setAttribute("right", right);
                }
                
                String buttonName="";
                if(button.indexOf("name=")>0){
                	buttonName=button.substring(button.indexOf("name=\"")+"name=\"".length(),button.indexOf("\"",button.indexOf("name=\"")+"name=\"".length()));
                }
                //�����Ťȡ������Դ�ļ�����ֱ����ʾkey,��������֧��ֱ��д����
                String buttonNameDisplay = getMessage(request, buttonName);
                if(buttonNameDisplay == null){
                	buttonNameDisplay = buttonName;
                }
                if (operation.equals(tagOP)||(operation.equals("add") && (type.equals("quote") || (type.equals("tool") && !"list".equals(tagOP) )))
                		||(operation.equals("update") && (type.equals("tool")  && !"list".equals(tagOP)  )) ) {
					if (type.equals("StockDistributing")) {
						if (mop.add() || mop.update()) {
							MOperation m = (MOperation) loginBean
									.getOperationMap()
									.get("/ReportDataAction.do?reportNumber=ReportStockDistributing");
							if (m != null && m.query()) {
								if (buttonName == null|| buttonName.length() == 0){
									buttonName = "com.userFunction.StockDistributing";
								}
								String reportName = getAttribute("reportName",button);

								butTag.append("<a href=\"javascript:toStockDistributing('"+reportName+"')\">")
									  .append(buttonNameDisplay)
									  .append("</a>");
							}
						}
					}else if(type.equals("myButton")){//�Զ�����չ��ť type=��myButton�� oper=����Ҫ��Ȩ��ҳ�桱 jsFunction=��js������ name="name"��ť�� param="����" selectName="selectName" id="defineName"
						String oper = getAttribute("oper",button);//��Ҫ��Ȩ��ҳ��
						String jsFunction = getAttribute("jsFunction",button);//��Ҫ��Ȩ��ҳ��
						String param = getAttribute("param",button);//����
						String id = getAttribute("id",button);//defineName
						String selectName = getAttribute("selectName",button);//selectName
						param = (param==null||"".equals(param))?"":("'"+param+"'").replaceAll( ",","','");
						
						
					
						if(oper!=null && !oper.equals("")){
							MOperation m = (MOperation) loginBean
							.getOperationMap()
							.get("/"+oper);
							if(m==null){
								continue;//û��Ȩ��
							}
						}
						butTag.append("<a href=\"javascript:"+jsFunction+"("+param+")\">")
						      .append(buttonNameDisplay)
						      .append("</a>");
						if(id!=null && !"".equals(id)){
							butTag.append("<input type=\"hidden\" id=\""+jsFunction+"DefineName\" name=\""+jsFunction+"DefineName\" value=\""+id+"\">");
						}
						if(selectName!=null && !"".equals(selectName)){
							butTag.append("<input type=\"hidden\" id=\"mySelectName\" name=\"mySelectName\" value=\""+selectName+"\">");
						}
					}else if (type.equals("autoBalance")) {
						String balanceType = getAttribute("balanceType",button);
						if (mop.add()|| mop.update()) {
							if (buttonName == null || buttonName.length() == 0)
								buttonName = "com.auto.money";
								butTag.append("<a href=\"javascript:autoBalance('"+ balanceType + "')\">")
									  .append(getMessage(request, buttonName))
									  .append("</a>");
                        }
                    }else if (type.equals("copy")) {
						if (mop.add()) {
							if (buttonName == null || buttonName.length() == 0)
								buttonName = "common.lb.copy";

							butTag.append("<a href=\"javascript:copyOperation()\">")
								  .append(getMessage(request,buttonName))
								  .append("</a>");
                        }
                    } else if (type.equals("quote")) {                    	
                        if (mop.add()) {
                        	n++ ;
                        	if(buttonName==null||buttonName.length()==0)buttonName="common.lb.quote";
                        	
                        	String buttonDis = getMessage(request,buttonName);
                        	if(buttonDis == null){
                        		buttonDis = buttonName;
                        	}
                            String[] value = getAttribute("value", button).
                                             split(":");  //[0]Դ����[1]�������id
                            String mainParam = "";
                            if(value.length <2){
                            	continue;
                            }
                            PopupSelectBean popSelectBean = (PopupSelectBean)
                                    BaseEnv.popupSelectMap.get(value[1]);
                            if(popSelectBean==null){
                            	return "";
                            }
                            ArrayList params = popSelectBean.getTableParams();
                            for (int i = 0; i < params.size(); i++) {
                                mainParam += "+\"&" + params.get(i) +
                                        "=\"+document.getElementsByName(\"" +
                                        params.get(i) + "\")[0].value";
                            }
                            String sourceTableName = value[0];  //Դ����
                            if(!isStartModule(sourceTableName)){
                            	continue; //ģ��δ����
                            }
                            
                            quoteBtn.append("<script language=\"javascript\"> ");
                            quoteBtn.append(" function quoteSelect"+n+"(){ ");
                            quoteBtn.append(" window.save=true; ");
                            //quoteBtn.append("var str =\"\";") ;
                            quoteBtn.append(" var url=\"/UserFunctionAction.do?tableName=")
                                  .append(tableInfo.getTableName())
                                  .append("&parentTableName="+request.getAttribute("parentTableName") +"&selectName=")
                                  .append(value[1]).append("&operation=").append(OperationConst.OP_POPUP_SELECT)
                                  .append("&MOID=").append(request.getAttribute("MOID"))
                                  .append("&isQuote="+n+"&LinkType=@URL:&displayName="+GlobalsTool.encode(buttonDis))
                                  .append("&moduleType="+moduleType)
                                  .append("&f_brother="+request.getAttribute("f_brother"))
                                  .append("&MOOP=" + request.getAttribute("MOOP") + "&popupWin=Quotepopup\"")
                                  .append(mainParam + "; ");
                            //quoteBtn.append("if(navigator.userAgent.toLowerCase().indexOf(\"safari\") != - 1){");
                            quoteBtn.append("if(typeof(window.parent.$('#bottomFrame').attr('id'))!='undefined'){asyncbox = parent.asyncbox;}");
                            quoteBtn.append("asyncbox.open({id:'Quotepopup',title:'���ô���',url:url,width:800,height:470});") ;
                            //quoteBtn.append("}else{") ;
                            //quoteBtn.append("str  = window.showModalDialog(url,\"\",\"dialogWidth=730px;dialogHeight=450px\"); if(typeof(str)==\"undefined\")return;");
                            //quoteBtn.append("fs=str.split(\";\");   ");
                            //quoteBtn.append(" location.href=\"/UserFunctionAction.do?tableName=").
                            //        append(tableInfo.getTableName()).append("&parentTableName="+request.getAttribute("parentTableName")
                            //        		+"&sourceTableName=").append(sourceTableName).
                            //        append("&f_brother="+request.getAttribute("f_brother")).
                            //        append("&moduleType="+moduleType).
                            //        append("&winCurIndex="+request.getParameter("winCurIndex")).
                            //        append("&sourceId=\"+encodeURIComponent(str)+\"&operation=").
                            //        append(OperationConst.OP_QUOTE).append("\";");
                            //quoteBtn.append(" }");
                            quoteBtn.append(" }") ;
                            quoteBtn.append(" function exeQuotepopup"+n+"(strValue){") ;
                            quoteBtn.append(" location.href=\"/UserFunctionAction.do?tableName=").
                            		append(tableInfo.getTableName()).append("&parentTableName="+request.getAttribute("parentTableName")
                            					+"&sourceTableName=").append(sourceTableName).
                            		append("&f_brother="+request.getAttribute("f_brother")).
                            		append("&moduleType="+moduleType).
                            		append("&winCurIndex="+request.getParameter("winCurIndex")).
                            		append("&sourceId=\"+encodeURIComponent(strValue)+\"&operation=").
                            		append(OperationConst.OP_QUOTE).append("\";");
                            quoteBtn.append(" }") ;
                            quoteBtn.append(" </script> ");
                            quoteBtn.append(
                                    " <a href=\"javascript:quoteSelect"+n+"()\">").
                                    append(buttonDis).append("</a>");
                        }

                    } else if (type.equals("tool")) { //���� 
                    	n++ ;
                    	if(buttonName==null||buttonName.length()==0)buttonName="����";
                    	
                    	String buttonDis = getMessage(request,buttonName);
                    	if(buttonDis == null){
                    		buttonDis = buttonName;
                    	}
                    	String val = getAttribute("value", button);
                    	if(hasRight(val,lb)){
	                    	toolBtn.append(" <a href=\"javascript:billTool('"+val+"','"+buttonDis+"','"+getAttribute("width", button)+"','"+getAttribute("height", button)+"')\">").
	                                append(buttonDis).append("</a>");
                    	}

                    } else if (type.equals("query")) { //����
                    	n++ ;
                    	if(buttonName==null||buttonName.length()==0)buttonName="����";
                    	
                    	String buttonDis = getMessage(request,buttonName);
                    	if(buttonDis == null){
                    		buttonDis = buttonName;
                    	}
                    	String val = getAttribute("value", button);
                    	if(hasRight(val,lb)){
	                    	queryBtn.append(" <a title='"+buttonDis+"' href=\"javascript:billQuery('"+val+"','"+buttonDis+"','"+getAttribute("width", button)+"','"+getAttribute("height", button)+"')\">").
	                                append(buttonDis).append("</a>");
                    	}

                    } else if (type.equals("define")&&!draft.equals("draft")) {
                    	 boolean isUsed=("".equals(fieldSysType)?true:Boolean.parseBoolean(BaseEnv.systemSet.get(fieldSysType).getSetting()));
                    	 //��Ӧ��ϵͳ�������ã����ҷ�����Ӧ��Ȩ��
                    	 if(right != null && right.indexOf(":")>-1){
                    		 Object o = request.getSession().getAttribute("LoginBean");
                    			if (o != null) {
                    				mop = ( MOperation )((LoginBean) o).getOperationMap().get(right.substring(0,right.indexOf(":")));
                    			}else{
                    				mop = null;
                    			}
                    			right = right.substring(right.indexOf(":")+1);
                    	 }
                    	if(mop!=null && isUsed&&((right.equals("add")&&mop.add)||(right.equals("update")&&mop.update)||(right.equals("delete")&&mop.delete)||(right.equals("query")&&mop.query)||(right.equals("approve"))
                    			||(right.equals("print")&&mop.print)||(right.equals("sendEmain")&&mop.sendEmail))){
                            String name = getAttribute("value", button);
                            
                            DefineButtonBean bean= DefineButtonBean.parse(name);
                            if(bean!=null && bean.getType()!=null && bean.getType().equals("execDefine")){
                            	String value = bean.getContent();
                            	String check = bean.getIsCheck(); ;
                            	//���ȡ��ȡ��Դ�ļ�����ֱ��ȡkey,������֧��ֱ��ʹ������
                            	String dis = this.getMessage(request, bean.getButtonName());
                            	if(dis == null) dis = bean.getButtonName();
                            	butTag.append("<a href=\"javascript:$('#ButtonType').val('execDefine'); extendSubmit('"+bean.getDefineName()+"','"+dis+"',"+(!"no".equals(check)?true:false)+")\" >"+dis+"</a>");
                            }else{
                            	butTag.append("<a href=\"javascript:\">" + name + "</a>");
                            }
                        }
                    } else if (type.equals("defineNew")&&!draft.equals("draft")) {
	                   	 boolean isUsed=("".equals(fieldSysType)?true:Boolean.parseBoolean(BaseEnv.systemSet.get(fieldSysType).getSetting()));
	                   	 //��Ӧ��ϵͳ�������ã����ҷ�����Ӧ��Ȩ��
	                   	 if(right != null && right.indexOf(":")>-1){
	                   		 Object o = request.getSession().getAttribute("LoginBean");
	                   			if (o != null) {
	                   				mop = ( MOperation )((LoginBean) o).getOperationMap().get(right.substring(0,right.indexOf(":")));
	                   			}else{
	                   				mop = null;
	                   			}
	                   			right = right.substring(right.indexOf(":")+1);
	                   	 }
	                   	if(mop!=null && isUsed&&((right.equals("add")&&mop.add)||(right.equals("update")&&mop.update)||(right.equals("delete")&&mop.delete)||(right.equals("query")&&mop.query)||(right.equals("approve"))
                   			||(right.equals("print")&&mop.print)||(right.equals("sendEmain")&&mop.sendEmail))){
                           String name = getAttribute("name", button);
                           
                           	String value = getAttribute("value", button);
                           	String check = "" ;
                           	if(value.indexOf(":")>-1){
                           		check = value.split(":")[1]; 
                           		value = value.split(":")[0]; 
                           	}
                           	//���ȡ��ȡ��Դ�ļ�����ֱ��ȡkey,������֧��ֱ��ʹ������
                           	String dis = this.getMessage(request, name);
                           	if(dis == null) dis = name;
                           	butTag.append("<a href=\"javascript:$('#ButtonType').val('execDefine'); extendSubmit('"+value+"','"+dis+"',"+(!"no".equals(check)?true:false)+")\" >"+dis+"</a>");
                           
                       }
                   }else if (type.equals("popDefine")&&!draft.equals("draft")) {//�ȵ���һ����������ִ��define����
                    	if(right.equals("")) { //��ʱ������Ȩ�޹��ൽ�޸�
                    		right = "update";
                    	}
                    	if((right.equals("add")&&mop.add)||(right.equals("update")&&mop.update)||(right.equals("delete")&&mop.delete)||(right.equals("query")&&mop.query)||(right.equals("approve"))
                    			||(right.equals("print")&&mop.print)||(right.equals("sendEmain")&&mop.sendEmail)){
                    		String defineName = getAttribute("id", button) ;
                    		String selectName = getAttribute("selectName", button) ;
                    		String disName = getAttribute("name", button) ;
                    		butTag.append("<a href=\"javascript:extendSubmitPopDefine('"+selectName+"','"+defineName+"','"+disName+"');\">")
                    			  .append(disName)
                    			  .append("</a>") ;
                    	}
	                }else if(type.equals("SMS")){
                    	if(mop.sendEmail){
                    		String fieldName = getAttribute("fieldname", button) ;
                    		String smsType = getAttribute("smsType", button) ;
                    		butTag.append("<a href=\"javascript:sendBillMsg('"+smsType+"','"+fieldName+"');\">")
        			  		  	  .append(getMessage(request, "common.lb.sendMessage"))
        			  		  	  .append("</a>") ;
                    	}
                    }else if(type.equals("sendEmail")){
                    	if(mop.sendEmail){
                    		butTag.append("<a href=\"javascript:$('#ButtonType').val('sendEmail');extendSubmit('sendEmail','"+getMessage(request, "com.send.email")+"');\">")
              			  		  .append(getMessage(request, "com.send.email"))
              			  		  .append("</a>") ;
                    	}
                    }else if(type.equals("sendSMS")){
                    	if(mop.sendEmail){
                    		butTag.append("<a href=\"javascript:$('#ButtonType').val('sendSMS');extendSubmit('sendSMS','"+getMessage(request, "com.send.sms")+"');\">")
              			  		  .append(getMessage(request, "com.send.sms"))
              			          .append("</a>") ;
                    	}
                    }else if(type.equals("custom")){
                    	if(mop.query){
                        	butTag.append("<a href=\"javascript:$('#ButtonType').val('custom');extendSubmit('custom','"+getMessage(request, "ApplyGoodsBill.total.msg")+"')\" >"+this.getMessage(request, "ApplyGoodsBill.total.msg")+"</a>");
                    	}
                    }else if(type.equals("Costing")){
                    	 if (mop.query()) {
                         	if(buttonName==null||buttonName.length()==0)buttonName="common.lb.costing";
                             butTag.append("<a href=\"javascript:costing()\">")
                             	   .append(getMessage(request,buttonName)).append("</a>");
                         }
                    }else if(type.equals("export") && (mop.add()||"tblEmployee".equals(tableName))){
                    	//ԭ���趨�ı�����ɾ��Ȩ�޲ſ��Ե��룬��ְԱû��ɾ��Ȩ�ޣ�Ҳ��Ҫ����
                    	String importValue = getAttribute("value", button);
                		String importType = getAttribute("importType", button) ;
                		if(importValue!=""){
                			Result rss_export = new ImportDataMgt().getImportDataByTableName(importValue,"") ;
                			if(rss_export.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
                				importValue = ((ImportDataBean)rss_export.getRetVal()).getId() ;
                				if(!"true".equals(request.getAttribute("CannotOper"))){
                					butTag.append("<a href=\"javascript:importData('"+importValue+"','"+importType+"')\">") 
                			  		  .append(getMessage(request, "excel.lb.upload"))
                			  		  .append("</a>") ;
                				}
                			}
                		}	
                    }else if("handover".equals(type)){
                    	if(mop.add()){
                    		butTag.append("<a href=\"javascript:extendSubmit('handover','"+getMessage(request, "com.hand.over")+"');\">")
                    			  .append(getMessage(request, "com.hand.over"))
                    			  .append("</a>") ;
                    	}
                    }else if("href".equals(type)){
                    	if(mop.query){
                    		String url = getAttribute("burl", button).replaceAll("colon;", ":") ;
                    		String bName = getAttribute("bname", button) ;
                    		String target = getAttribute("target", button) ;
                    		butTag.append("<a href=\"javascript:goURL('"+url+"&src=menu','"+target+"','"+getMessage(request, bName)+"')\">")
                    			  .append(getMessage(request, bName)) 
                    			  .append("</a>") ;
                    	}
                    }else if("dataMove".equals(type)){
                    	if(mop.add){
                    		String defineName = getAttribute("id", button) ;
                    		String selectName = getAttribute("selectName", button) ;
                    		butTag.append("<input type=\"hidden\" id=\"dataDefineName\" name=\"dataDefineName\" value=\""+defineName+"\">")
                    			  .append("<input type=\"hidden\" id=\"selectName\" name=\"selectName\" value=\""+selectName+"\">")
                    			  .append("<a href=\"javascript:extendSubmit('dataMove','"+getMessage(request, "com.data.move")+"');\">")
                    			  .append(getMessage(request, "com.data.move"))
                    			  .append("</a>") ;
                    	}
                    }else if("folderRight".equals(type)){
                    	if(mop.add){
                    		butTag.append("<a href=\"javascript:extendSubmit('folderRight','"+getMessage(request, "oa.kenowledgeCenter.licensed")+"');\">")
                    			  .append(getMessage(request, "oa.kenowledgeCenter.licensed"))
                    			  .append("</a>") ;
                    	}
                    }else if("keyWord".equals(type)){
                    	if(mop.add){
                    		String fieldName = getAttribute("fieldName", button) ;
                    		String value 	 = getAttribute("fieldDis", button) ;
                    		String defineId  = getAttribute("value", button) ;
                        	butTag.append("<a href=\"javascript:$('#ButtonType').val('execDefine'); checkKeyword('"+fieldName+"','"+defineId+"');\">"+this.getMessage(request, value)+"</a>");
                    	}
                   }/*else if("Attention".equals(type)){
                    	if(mop.query){
                    		String  keyId = getParameter("keyId",request);
                    		if(new AttentionMgt().isAttention(loginBean.getId(),keyId,(String)tableName).retCode==ErrorCanst.DEFAULT_SUCCESS){
                    			butTag.append("<li><button type=\"button\" id=\"Mybutton\" onClick=\"attention('attention','"+(String)tableName+"','"+(loginBean.getId())+"','"+keyId+"')\" class=\"b2\">")
                    			      .append(getMessage(request,"crm.attention"))
                    			      .append("</button></li>");
                    		}else{
                    			butTag.append("<li><button type=\"button\" id=\"Mybutton\" onClick=\"attention('abolishAttention','"+((String)tableName)+"','"+loginBean.getId()+"','"+keyId+"')\" class=\"b2\">")
                    				  .append(getMessage(request, "oa.bbs.bt.abolishAttention") + getMessage(request,"crm.attention"))
                    				  .append("</button></li>");
                    		}
                    	}
                    }*/else if("midCalculate".equals(type) && !"detail".equals(request.getAttribute("detail"))
                    		&&!"copy".equals(request.getAttribute("copy"))&&!"quote".equals(request.getAttribute("quote"))&&!"quoteDraft".equals(request.getAttribute("quoteDraft"))){
                    	//��ʾ������չ��Ť
                    	if(mop.add){
                        	butTag.append("<a href=\"javascript:if(beforSubmit(document.form)) {form.button.value='midCalculate';window.save=true; document.form.submit();}\">"+this.getMessage(request, "common.lb.calculate")+"</a>");
                    	}
                   }
                }
            }
        }
        //������б�����û����Ӧ����ϸ����
        if("list".equals(operation)){
        	Result rs = new ReportSetMgt().queryDetTable(tableInfo.getTableName());
        	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS &&rs.retVal != null){
        		for(String[] ss: (ArrayList<String[]>)rs.retVal)
        		queryBtn.insert(0," <a href=\"javascript:billDetQuery('"+ss[0]+"','"+tableInfo.getTableName()+"','"+ss[1]+"')\" title='"+ss[1]+"'>"+ss[1]+"</a>");
        	}
        }
        
        //������޸Ľ��棬Ҫ��ʾ�Ƶ���Ť����ֻҪ�����б��ϵӳ���ֱ����ʾ�Ƶ���Ť
        if("update".equals(operation)){
        	Result rs = new TableMappedMgt().queryChildTableName(tableInfo.getTableName());
        	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
        		ArrayList<String> pushList = (ArrayList)rs.retVal;
        		for(String os : pushList){
        			DBTableInfoBean ctb = GlobalsTool.getTableInfoBean(os);
        			if(ctb!=null && ctb.getTableType()== DBTableInfoBean.MAIN_TABLE){
        				//���Ȩ�ޣ�����ֻҪ�����͵�ģ���Ƿ���ڼ���������
        				if(BaseEnv.adminOperationMap.get("/UserFunctionQueryAction.do?tableName="+ctb.getTableName()) != null){
		        			String td = ctb.getDisplay().get(GlobalsTool.getLocale(request).toString());
		        			pushBtn.append(" <a href=\"javascript:billPush('"+os+"','"+td+"')\">").
		                    	append(td).append("</a>");
        				}
        			}
        		}
        	}
        }
        
        /*���ð�ť��������һ��*/
        request.setAttribute("quoteBtn", quoteBtn.toString()) ;
        request.setAttribute("pushBtn", pushBtn.toString()) ;
        request.setAttribute("toolBtn", toolBtn.toString()) ;
        request.setAttribute("queryBtn", queryBtn.toString()) ;
        return butTag.toString();
    }

    /**
     * ��ȡ��չ��ť���Ե�ֵ
     * @param attr 
     * @param str
     * @return
     */
    private String getAttribute(String attr, String str) {
    	//zxy �޸�   =\"([/\\?&\\=\\,\\@\\.\\w\\:\\-\\u4e00-\\u9fa5]+)\"
		Pattern pattern = Pattern.compile(attr + "=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
    
    /**
     * ���� ��ע��ͼƬ
     * @return
     */
    private List<String> getRemarkImage(String remark){
    	Pattern pattern = Pattern.compile("src=\"/upload/[\\.\\w]+\"");
        Matcher matcher = pattern.matcher(remark);
        ArrayList<String> listImage = new ArrayList<String>() ;
        while (matcher.find()) {
        	String str = matcher.group(0);
        	if(str!=null && str.contains("src=\"/upload/")){
        		str = str.substring(13,str.length()-1) ;
        		listImage.add(str);
        	}
        }
        return listImage;
    }

    protected ActionForward showPrint(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws
            Exception {
        ReportDataMgt mgt = new ReportDataMgt();
        String reportNumber = this.getParameter("reportNumber", request);
        String moduleType = getParameter("moduleType", request) ;
        moduleType = moduleType==null?"":moduleType;
        String tableName=request.getParameter("tableName");
        request.setAttribute("tableName", tableName);
        request.setAttribute("moduleType", moduleType);
        
        String keyId = this.getParameter("keyId", request) ;
        String locale = GlobalsTool.getLocale(request).toString();
        Result rs=new ReportSetMgt().getReportSetInfo(reportNumber,locale);
        ReportsBean repBean=null;
        boolean isbillType = false;
        //�������Ǵ�ӡ���ݣ�Ҫ���������ſ��Դ�ӡ
        if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
        	 EchoMessage.error().add(getMessage(
                     request, "common.msg.sendError"))
                     .setBackUrl("/UserFunctionAction.do?tableName=" + request.getParameter("tableName")  + "&winCurIndex="+request.getParameter("winCurIndex")).
                     setAlertRequest(request);
             return getForward(request, mapping, "message");
        }else{
        	if(rs.realTotal>0){
        		repBean=(ReportsBean)rs.getRetVal();
        		if(repBean.getReportType().equals("BILL")){
        			isbillType =  true;
        			
        			String BillID=request.getParameter("BillID");
        			rs=dbmgt.queryWorkFlowNodeName(tableName, BillID);
        			if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
        				EchoMessage.error().add(getMessage(
        	                     request, "common.msg.sendError"))
        	                     .setBackUrl("/UserFunctionAction.do?tableName=" +tableName + "&winCurIndex="+request.getParameter("winCurIndex")).
        	                     setAlertRequest(request);
        	            return getForward(request, mapping, "message");
        			}else{
        				String workFlowStatus=rs.getRetVal().toString();
        				if(!workFlowStatus.equals("finish")&&repBean.getAuditPrint()==1){
        					EchoMessage.error().add(getMessage(
        		                     request, "common.msg.notAudit"))
        		                     .setBackUrl("/UserFunctionAction.do?tableName=" + request.getParameter("tableName")  + "&winCurIndex="+request.getParameter("winCurIndex")).
        		                     setAlertRequest(request);
        		             return getForward(request, mapping, "message");
        				}
        			}
        		}
        		request.setAttribute("reportId", repBean.getId()) ;
        	}
        }
        request.setAttribute("isbillType", isbillType); //�����Ƿ��ǵ��ݱ���
        
        Result rs2;
        if(isbillType){
        	rs2 = mgt.getBillFormatList(tableName,moduleType,locale,getLoginBean(request).getId(),getLoginBean(request).getDepartCode());
        }else{
        	rs2 = mgt.getFormatList(reportNumber,locale,getLoginBean(request).getId(),getLoginBean(request).getDepartCode());
        }
        if (rs2.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
            EchoMessage.error().add(rs2.getRetVal().toString()).
                    setRequest(request);
            return getForward(request, mapping, "message");
        }
        
        
        Cookie[] coks = request.getCookies();
        for (int i = 0; i < coks.length; i++) {
            Cookie cok = coks[i];
            if (cok.getName().equals("JSESSIONID")) {
                request.setAttribute("JSESSIONID", cok.getValue());
                break;
            }
        }
        String BillID = request.getParameter("BillID") == null ? "" :
                        request.getParameter("BillID");
        String parentTableName=request.getParameter("parentTableName");
        
        if(BillID.length()>0){
        	String billTable = "" ;
        	if(parentTableName!=null&&parentTableName.length()>0){
        		billTable = request.getParameter("tableName")+";"+parentTableName ;
        	}else{
        		billTable = request.getParameter("tableName");
        	}
        	if(moduleType!=null && moduleType.length()>0){
        		billTable = billTable+";;" + moduleType ;
        	}
        	request.setAttribute("BillTable", billTable) ;
            request.setAttribute("BillID", BillID);
        }
        //FormatName, FormatFileName,a.id,a.reportNumber,filterSQL
        ArrayList<String[]> list = (ArrayList<String[]>)rs2.getRetVal();
        ArrayList<String[]> dellist = new ArrayList<String[]>();
    	for(String[] ss:list){
    		if(ss[4] != null && ss[4].length() > 0){
    			if(!mgt.testFilterSQL(ss[4], BillID)){
    				dellist.add(ss);
    			}
    		}
    		if(parentTableName!=null&&parentTableName.length()>0){
    			ss[3] +=";"+parentTableName;
    		}
    	}
    	list.removeAll(dellist);	
        
        String printType = getParameter("printType", request) ;
        /*��ǩ��ӡ����֯SQL���*/
        if("labelPrint".equals(printType)){
        	StringBuffer SQLSave = new StringBuffer("");
        	DefineReportBean defBean = null;
    		try {
    			defBean = DefineReportBean.parse(reportNumber + "SQL.xml", this.getLocale(request).toString(),getLoginBean(request).getId());
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}
        	SQLSave.append("@SQL:").append(defBean.getSql());
        	SQLSave.append("@ParamList:").append(keyId);
        	SQLSave.append("@end:");
        	request.setAttribute("printType", printType) ;
        	request.setAttribute("SQLSave", SQLSave.toString()) ;
        }else{
        	request.setAttribute("SQLSave",request.getSession().getAttribute("SQLSave"));
        }
        request.setAttribute("NewFlag", repBean.getNewFlag()) ;
        request.setAttribute("SQLFileName", repBean.getSQLFileName()) ;
        
        request.setAttribute("printType", getParameter("printType", request));
        request.setAttribute("formatList", rs2.getRetVal());
        
        return mapping.findForward("printActiveX");
    }

    protected ActionForward quotePrepare(ActionMapping mapping,ActionForm form,
              HttpServletRequest request,HttpServletResponse response) throws  Exception {
    	//���ݶര����ģ�����ӵ�ַ���Ȩ��
        MOperation mop = GlobalsTool.getMOperationMap(request) ;
        ArrayList scopeRight=mop.getScope(MOperation.M_UPDATE);
        request.setAttribute("scopeRight", scopeRight);
        
    	String tableName = getParameter("tableName", request);
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
    	
    	String parentTableName=this.getParameter("parentTableName",request);
        parentTableName=parentTableName==null?"":parentTableName;
        request.setAttribute("parentTableName", parentTableName);
        //��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��
        if (tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
           request.setAttribute("mainTableName", parentTableName);
        } else {
           request.setAttribute("mainTableName", tableName);
        }
        
        
        
        HashMap<String,ArrayList<String[]>> moduleTable= (HashMap)BaseEnv.ModuleField.get(mop.getModuleUrl());
        if(moduleTable!=null){
        	request.setAttribute("moduleTable", moduleTable);
        	request.setAttribute("moduleUrl", mop.getModuleUrl()) ;
        }
        
        
        
        String sourceTableName = getParameter("sourceTableName", request);
        String sourceId = getParameter("sourceId", request);
        //���ø���
        String f_brother = getParameter("f_brother", request) ;
        f_brother = f_brother == null ? "" : f_brother;
        request.setAttribute("f_brother", f_brother) ;

        String tabList = this.getParameter("tabList", request);
        tabList = tabList == null ? "" : tabList;
        request.setAttribute("tabList", tabList);
        
        //��ȡ��ǰ�޸ĵ�ҳ
        int pageNo = getParameterInt("pageNo", request);
        if(pageNo==0){
        	pageNo = 1 ;
        }
        request.setAttribute("pageNo", pageNo) ;
        LoginBean loginBean=getLoginBean(request);
        request.setAttribute("sessionSet", BaseEnv.sessionSet.get(loginBean.getId()));
        
        
        ActionForward forward = null; 
        if (sourceId != null && sourceId.length() != 0) {
        	 String[] keyIds1 = ((sourceId.indexOf(";childKeyId;")> -1)?sourceId.substring(0,sourceId.indexOf(";childKeyId;")):sourceId).split("#;#");
             String keyId="";
             if(keyIds1.length>0){
             	keyId = keyIds1[0];
             }
        	//�����ϸ�ĵ����򣬲�û��ѡ����ϸ���������ϸ��ĵ�������ҷ������������ݡ�
            String button=((DBTableInfoBean)BaseEnv.tableInfos.get(tableName)).getExtendButton();
            int indexs=button.indexOf("value=\""+sourceTableName+":")+("value=\""+sourceTableName+":").length();
        	String pselectName=button.substring(indexs,button.indexOf("\"",indexs)) ;
        	String selectName=BaseEnv.popupSelectMap.get(pselectName)==null?null:((PopupSelectBean)BaseEnv.popupSelectMap.get(pselectName)).getHasChild();
        	ArrayList childIds=new ArrayList();
        	if(selectName!=null&&selectName.length()>0&&sourceId.indexOf("childKeyId")<0){
        		PopupSelectBean popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);

            	Result rspop=dbmgt.popSelect("",tableName, popSelectBean, BaseEnv.tableInfos, new ArrayList(),
            	                                    new HashMap(), scopeRight,
            	                                   0, 0, "", keyId,
            	                                   loginBean.getId(),loginBean.getSunCompany(),this.getLocale(request),"",request,request.getParameter("src"),PopupSelectBean.DEFAULTRULE,"");
            	
            	if(rspop.retCode==ErrorCanst.DEFAULT_SUCCESS&&rspop.retVal!=null&&((ArrayList)rspop.retVal).size()>0){
            		ArrayList rsList=(ArrayList)rspop.retVal;
            		for(int i=0;i<rsList.size();i++){
            			childIds.add(((Object[])rsList.get(i))[0]);
            		}
            	}
        	}else if(sourceId.indexOf("childKeyId")>0){
        		String []keyIds = sourceId.substring(sourceId.indexOf(";childKeyId;")+";childKeyId;".length()).split("#;#");
        		for(int i=0;i<keyIds.length;i++){
        			if(keyIds[i].length()>0){
        				childIds.add(keyIds[i]);
        			}
        		}
        	} 
           
            //ִ�м�����ϸ
            LoginBean lg = new LoginBean();
            lg = (LoginBean) request.getSession().getAttribute("LoginBean");
            String sunClassCode = lg.getSunCmpClassCode(); //��LoginBean��ȡ����֧������classCode
            Hashtable props = (Hashtable) request.getSession().getServletContext().
            getAttribute(BaseEnv.PROP_INFO);
            Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
            boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
            Result rs = dbmgt.detail(sourceTableName, map, keyId, sunClassCode,props,lg.getId(),isLastSunCompany,"");

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            	//�õ����ñ�Ķ�Ӧ�ֶ�(HashMap keyĿ����ֶ�,valueԴ���ֶ�)
                Result rs2 = dbmgt.getTableMapping(sourceTableName, tableName);
                if (rs2.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                    EchoMessage.error().add(getMessage(request,
                            "common.msg.error")).setRequest(request);
                    return getForward(request, mapping, "message");
                }
                HashMap fieldMap = (HashMap) rs2.getRetVal();
                HashMap values = (HashMap) rs.getRetVal();  //Դ������



                HashMap moreLan=(HashMap)values.get("LANGUAGEQUERY");
                HashMap targetValues = new HashMap();
                List fields = DDLOperation.getTableInfo(map, tableName).
                              getFieldInfos();
                //��values��Դ�����ݸ��Ƶ�Ŀ���
                for (int i = 0; i < fields.size(); i++) {
                    DBFieldInfoBean field = (DBFieldInfoBean) fields.get(i);
                    Object sourceField = fieldMap.get(field.getFieldName());
                    String sourceValue = "";
                    if (sourceField != null) {
                        Object obj = values.get(sourceField);
                        if (obj != null) {
                            sourceValue = values.get(sourceField).toString();
                        }
                        //�õ���������
                        PopupSelectBean popBean = field.getSelectBean();
                        if (popBean != null) {
                            Result rs3 = dbmgt.getRefViewValues(field, sourceValue,map, sunClassCode, targetValues,targetValues,lg.getId(),isLastSunCompany,null,mop.getScope(MOperation.M_UPDATE));
                            if (rs3.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
                                EchoMessage.error().add(getMessage(request,"common.msg.error")).setRequest(request);
                                return getForward(request, mapping, "message");
                            }
                            //������������ֶ��ж�����
                            DBFieldInfoBean bean;
                            for(int j=0;j<popBean.getViewFields().size();j++){
                            	PopField pop=(PopField)popBean.getViewFields().get(j);
                            	bean=GlobalsTool.getFieldBean(pop.getAsName().substring(0,pop.getAsName().indexOf(".")),pop.getAsName().substring(pop.getAsName().indexOf(".")+1));
                            	if(bean == null){
                            		bean=GlobalsTool.getFieldBean(pop.getFieldName().substring(0,pop.getFieldName().indexOf(".")),pop.getFieldName().substring(pop.getFieldName().indexOf(".")+1));
                            	}
                            	if(bean !=null && (bean.getInputType()==bean.INPUT_LANGUAGE||bean.getInputTypeOld()==bean.INPUT_LANGUAGE)){
                            		KRLanguage language = (KRLanguage)moreLan.get(targetValues.get(pop.getAsName())) ;
                            		targetValues.put(pop.getAsName(), language==null?"":language.get(this.getLocale(request).toString()));
                            	}
                            }
                        }
                    }
                    //����Ĭ��ֵ
                    if (sourceValue.equals("")) {
                    	if(field.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE){
                           if(field.getInputValue()!=null&&field.getInputValue().length()>0&&field.getDefaultValue()!=null&&field.getDefaultValue().length()>0){
                        	   dbmgt.getDefRefValue(field,targetValues,sessionSet,getLoginBean(request).getId(),BaseEnv.tableInfos,"");
                        	   continue;
                           }
                        }else{
                        	if(!(field.getFieldName().equals("TrackNo")&&field.getInputType()==100)){
                        		if(field.getFieldIdentityStr()!=null&&DBFieldInfoBean.FIELD_IDENTIFIER.equals(field.getFieldIdentityStr())){
                        			//���ݱ��ȡĬ��ֵ����
                        			String keyValue = "";
                        			if(field.getDefaultValue()!=null && field.getDefaultValue().length()>0){
                        				//����Ĭ��ֵ
                        				keyValue = field.getDefaultValue();
                        			}else{
                        				keyValue = tableName+"_"+field.getFieldName();
                        			}
                        			
                        			BillNo billNo = new BillNoManager().find(keyValue);		//����Map����ֵ
                        			if(billNo == null){
                        				sourceValue = "";
                        			}else{
                        				BillNoUnit unit = null;
                        				if(billNo.isFillBack()){
                        					unit = billNo.getInvers(new HashMap<String, String>(), lg);
                        				}else{
                        					unit = billNo.getNumber(new HashMap<String, String>(), lg);
                        				}
                        				if(unit != null){
                        					sourceValue = unit.getValStr();
                        				}
                        			}
                        		}else{
                        			sourceValue = field.getDefValue(lg.getId());
                        		}
                        	}
                    	}
                    }

                    if (field.getFieldType() == field.FIELD_DOUBLE) {
                        sourceValue = GlobalsTool.formatNumberS(Double.
                                parseDouble(sourceValue.equals("") ? "0" :
                                            sourceValue), false, false,"", tableName+"."+field.getFieldName());
                    }
                    //����ʱ��������id,����������������id��
                    if(!field.getFieldName().equals("id"))
                    	targetValues.put(field.getFieldName(), sourceValue);

                }

                
                //����û��Ѿ��������Զ��������ã����ȡ�û��Զ���������
                String allTableName = tableName+ "," ;
                ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
                ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;//�����������
                Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;//�ӱ��������
                Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
        												request.getSession().getServletContext().getAttribute("userSettingColConfig") ;

                if(userColConfig!=null && userColConfig.size()>0){
                	configList = userColConfig.get(tableName+"bill") ;
                	if(configList!=null){
                		allConfigList.addAll(configList) ;

                	}
                }


                ArrayList childTableList = DDLOperation.getChildTables(tableName, map);
                ArrayList<DBFieldInfoBean> dbList = new ArrayList<DBFieldInfoBean>();
                if(PersistentBag.class.getName().equals(tableInfo.getFieldInfos().getClass().getName())){
                	PersistentBag bag = (PersistentBag) tableInfo.getFieldInfos();
                	for (Object object : bag) {
       					dbList.add((DBFieldInfoBean) object);
       				}
                }else{
                	dbList = (ArrayList<DBFieldInfoBean>) tableInfo.getFieldInfos() ;
                }

                for(int i=0;i<childTableList.size();i++){
                	DBTableInfoBean childTableInfo=(DBTableInfoBean)childTableList.get(i);
                	//������ϸ���Զ����е���ʾ
                	if(allConfigList!=null && allConfigList.size()>0){
                		allTableName += childTableInfo.getTableName()+"," ;
                		ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName()+"bill") ;
                    	if(childConfigList!=null){
                    		allConfigList.addAll(childConfigList) ;
                    		childTableConfigList.put(childTableInfo.getTableName(), childConfigList) ;
                    	}
                	}
                }

                request.setAttribute("allTableName", allTableName) ;
                request.setAttribute("allConfigList", allConfigList) ;
                request.setAttribute("childTableConfigList", childTableConfigList) ;

                

                //Ѱ���ӱ�
                ArrayList rowsList = new ArrayList();
                ArrayList souChildTableList = DDLOperation.getChildTables(sourceTableName, map);;
                souChildTableList.add(BaseEnv.tableInfos.get(sourceTableName));
                for (int k = 0;childTableList != null && k < childTableList.size();k++) {
                    DBTableInfoBean childTable = (DBTableInfoBean)childTableList.get(k);
                    ArrayList cols = new ArrayList();
                    ArrayList rows = new ArrayList();
                    ArrayList truncIndexs=new ArrayList();
                    if(childTableConfigList!=null && childTableConfigList.size()>0){//�û��Զ���������
                    	DBTableInfoBean tableInfoBean = (DBTableInfoBean) childTableList.get(k) ;
                    	ArrayList<ColConfigBean> cofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
                    	for(int m=0;cofingList!=null&&m<cofingList.size();m++){
                    		ColConfigBean configBean = cofingList.get(m) ;
                    		GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)GlobalsTool.getPropBean(configBean.getColName());
                    		boolean isExist = false ;
                    		for (int i = 0;childTable != null &&i < childTable.getFieldInfos().size(); i++) {
    	                        DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
    	                        String[] ss = new String[6];
    	                        if ((fi.getInputType() ==DBFieldInfoBean.INPUT_MAIN_TABLE
    	                        		|| (fi.getInputType()==6 && fi.getInputTypeOld()==2)) && fi.getSelectBean()!=null&&(!(gpInfo!=null&&gpInfo.getIsSequence()==1))) {
    	                        		if(fi.getSelectBean().getRelationKey()!=null && !fi.getSelectBean().getRelationKey().hidden && configBean.getColName().equals(fi.getFieldName())){
    	                        			ss = new String[6];
    	         	                        Object o = fi.getDisplay().get(getLocale(request).toString());
    	         	                        setFieldString(childTable, fi, ss, o);
    	         	                        cols.add(ss);
    	                        		}
    	                        		GoodsPropInfoBean tempProp=GlobalsTool.getPropBean(fi.getFieldName());
    	                        		if(!(tempProp!=null&&tempProp.getIsSequence()==1)){
    		                            for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
    		                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
    		                                if( configBean.getColName().equals(temps.getAsName())){
    		                                	DBFieldInfoBean f=GlobalsTool.getFieldBean(temps.getAsName().substring(0,temps.getAsName().indexOf(".")), temps.getAsName().substring(temps.getAsName().indexOf(".")+1));
    		                                	
	    		                                ss = new String[6];
	    		    		                    ss[2] = DBFieldInfoBean.getFieldTypeString(f);
	    		                                ss[3] = temps.getAsName();
	    		                                ss[4] = temps.getFieldName();
	    		                                ss[5]=temps.getDisplay();
	    		                                cols.add(ss);
	    		                                isExist=true ;
	    		                                break ;
    		                                }
    		                            }
    	                        	}
	                        		if(fi.getSelectBean().getViewFields().size()==0){
    	                        		if(configBean.getColName().equals(fi.getFieldName())){
    	    	                        	Object o = fi.getDisplay().get(getLocale(request).toString());
    	    		                        setFieldString(childTable, fi, ss, o);
    	    		                        cols.add(ss);

    	    	                        	if(fi.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||fi.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE){
    	        	                        	String[] tempstr=new String[6];
    	        	                        	tempstr[0]=ss[0];
    	        	                        	tempstr[1]=ss[1];
    	        	                        	tempstr[2]="lan";
    	        	                        	tempstr[3] = fi.getFieldName();
    	        	                        	tempstr[4] = childTable.getTableName();
    	        	                            cols.add(tempstr);
    	    	                        	}
    	    	                        	isExist=true ;
    	    	                        	break ;
        	                        	}
    	                        	}
    	                        }else if(fi.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE&&gpInfo!=null&&gpInfo.getIsSequence()==1
    	                        		&& configBean.getColName().equals(fi.getFieldName())){
    	                        	Object o = fi.getDisplay().get(getLocale(request).toString());
         	                        setFieldString(childTable, fi, ss, o);
    	                        	cols.add(ss);
    	                        	cols.add(ss);
 		                            truncIndexs.add(cols.size()-1);
    	                        }else{
    	                        	if(configBean.getColName().equals(fi.getFieldName())){
	    	                        	Object o = fi.getDisplay().get(getLocale(request).toString());
	    		                        setFieldString(childTable, fi, ss, o);
	    		                        cols.add(ss);
	    	                        	if(fi.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||fi.getInputTypeOld() ==DBFieldInfoBean.INPUT_LANGUAGE){
	        	                        	String[] tempstr=new String[5];
	        	                        	tempstr[0]=ss[0];
	        	                        	tempstr[1]=ss[1];
	        	                        	tempstr[2]="lan";
	        	                        	tempstr[3] = fi.getFieldName();
	        	                        	tempstr[4] = childTable.getTableName();
	        	                            cols.add(tempstr);
	    	                        	}
	    	                        	isExist=true ;
	    	                        	break ;
    	                        	}
    	                        }
    	                        if(isExist){
    	                        	break ;
    	                        }
                    		}
                    	}
                    	//���������ֶ�ֵ
                    	for (int i = 0;childTable != null &&i < childTable.getFieldInfos().size(); i++) {
                    		DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
                    		GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)GlobalsTool.getPropBean(fi.getFieldName());
                    		if(fi.getInputType()==3){
                    			String[] ss = new String[5];
    	                        Object o = fi.getDisplay().get(getLocale(request).toString());
    	                        setFieldString(childTable, fi, ss, o);
    	                        cols.add(ss);
                    		}else if(fi.getInputType()==2 && "GoodsField".equals(fi.getFieldSysType()) 
                    									  && gpInfo!=null && gpInfo.getIsSequence()==1){
                    			boolean isExist = false;
								ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName());
								for (int m = 0; childCofingList != null && m < childCofingList.size(); m++) {
									ColConfigBean configBean = childCofingList.get(m);
									if ((fi.getFieldName().equals(configBean.getColName()))) {
										isExist = true;
										break;
									}
								}
								if (!isExist) {
									String[] ss = new String[5];
									Object o = fi.getDisplay().get(getLocale(request).toString());
									setFieldString(childTable, fi, ss, o);
									cols.add(ss);
								}
                    		}else if(fi.getInputType()==2
                    				&& fi.getSelectBean()!=null &&  fi.getInputValue().length()>0
                    				&& fi.getSelectBean().getViewFields().size()>0){
                    			if(fi.getSelectBean().getRelationKey().hidden){
	                    			String[] ss = new String[6];
	    	                        Object o = fi.getDisplay().get(getLocale(request).toString());
	    	                        setFieldString(childTable, fi, ss, o);
	    	                        cols.add(ss);
                    			}else{
                    				boolean isExist = false ;
	                    			ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
	                    			for(int m=0;childCofingList!=null&&m<childCofingList.size();m++){
	                    				ColConfigBean configBean = childCofingList.get(m) ;
	                    				if(fi.getFieldName().equals(configBean.getColName())){
	                    					isExist = true ;
	                    					break ;
	                    				}
	                    			}
	                    			if(!isExist){
	                    				String[] ss = new String[6];
		    	                        Object o = fi.getDisplay().get(getLocale(request).toString());
		    	                        setFieldString(childTable, fi, ss, o);
		    	                        cols.add(ss);
	                    			}
                    			}
                    			for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
	                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
	                                DBFieldInfoBean f=GlobalsTool.getFieldBean(temps.getAsName().substring(0,temps.getAsName().indexOf(".")), temps.getAsName().substring(temps.getAsName().indexOf(".")+1));
	                                
	                                boolean isExist = false ;
	                    			ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
	                    			for(int m=0;childCofingList!=null&&m<childCofingList.size();m++){
	                    				ColConfigBean configBean = childCofingList.get(m) ;
	                    				if(temps.getAsName().equals(configBean.getColName())){
	                    					isExist = true ;
	                    					break ;
	                    				}
	                    			}
	                    			if(!isExist){
	                    				String[] ss = new String[6];
	        		                    ss[2] = DBFieldInfoBean.getFieldTypeString(f);
		                                ss[3] = temps.getAsName();
		                                ss[4] = temps.getFieldName();
		                                ss[5]=temps.getDisplay();
		                                cols.add(ss);
	                    			}
                    			}
                    		}else if(fi.getInputType()!=100 && !fi.getFieldName().equals("id")
                    								&& !fi.getFieldName().equals("f_ref") && !fi.getFieldName().equals("f_brother")){
                    			boolean isExist = false ;
                    			ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
                    			for(int m=0;childCofingList!=null&&m<childCofingList.size();m++){
                    				ColConfigBean configBean = childCofingList.get(m) ;
                    				if(fi.getFieldName().equals(configBean.getColName())){
                    					isExist = true ;
                    					break ;
                    				}
                    			}
                    			if(!isExist){
                    				String[] ss = new String[5];
        	                        Object o = fi.getDisplay().get(getLocale(request).toString());
        	                        setFieldString(childTable, fi, ss, o);
        	                        cols.add(ss);
                    			}
                    		}
                    	}
                    }else{
	                    for (int i = 0;childTable != null && i < childTable.getFieldInfos().size(); i++) {
	                        DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);

	                        if (fi.getFieldName().equals("id") ||
	                            fi.getFieldName().equals("f_ref") ||
	                            fi.getInputType() == DBFieldInfoBean.INPUT_NO) {
	                            continue;
	                        }
	                        String[] ss = new String[5];
	                        Object o = null;
	                        if(fi.getDisplay()!=null){
	                        	fi.getDisplay().get(getLocale(request). toString());
	                        }
	                               
	                        setFieldString(childTable, fi, ss, o);
	                        cols.add(ss);
	                        //�ҳ������ֶ�
	                        GoodsPropInfoBean gpInfo=GlobalsTool.getPropBean(fi.getFieldName());
	                        if (fi.getInputTypeOld() ==
	                            DBFieldInfoBean.INPUT_MAIN_TABLE&&fi.getSelectBean()!=null) {
	                        	if(!(gpInfo!=null&&gpInfo.getIsSequence()==1)){
	                            for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
	                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
		                                String viewName=temps.getAsName();
		                                ss = new String[6];
		                                DBFieldInfoBean view =GlobalsTool.getFieldBean(viewName.substring(0,viewName.indexOf(".")), viewName.substring(viewName.indexOf(".")+1));
		                                
		    		                    ss[2] = DBFieldInfoBean.getFieldTypeString(view);
		                                ss[3] = temps.asName;
		                                ss[4] = temps.fieldName;
		                                ss[5]=temps.display;
		                                cols.add(ss);
	                               // }
	                            }
	                          }
	                        }
	                        if(fi.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE&&gpInfo!=null&&gpInfo.getIsSequence()==1){
	                            cols.add(ss);
	                            truncIndexs.add(cols.size()-1);
	                        }
	                    }
                    }

                    if (childTable != null) {
                        String childTableName = childTable.getTableName();
                        String souChildTableName = null;
                        HashMap childMap = null;
                        //�õ����ӱ����õ�Դ����Ӧ�ֶ���Ϣ
                        for (int l = 0;souChildTableList != null &&l < souChildTableList.size(); l++) {
                            souChildTableName = ((DBTableInfoBean)souChildTableList.get(l)).getTableName();
                            Result rs3 = dbmgt.getTableMapping(souChildTableName, childTableName);
                            if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                                EchoMessage.error().add(getMessage(request,
                                        "common.msg.error")).
                                        setRequest(request);
                                return getForward(request, mapping, "message");
                            }
                            if (rs3.getRealTotal() != 0) {
                                childMap = (HashMap) rs3.getRetVal();
                                break;
                            }
                        }
                        Object childListO = null;
                        if(sourceTableName.equals(souChildTableName)){//Դ����Ҳ���Զ�ӦĿ���
                        	List list=new ArrayList();
                        	list.add(values);
                        	childListO=list;
                        }else{
                        	childListO=values.get("TABLENAME_" +souChildTableName);
                        }
                        if (childListO != null) {
                        	HashMap paramMap=new HashMap();
                        	paramMap.putAll(values);

                            List childList = (List) childListO;
                            for (int i = 0; i < childList.size(); i++) {
                                HashMap os = (HashMap) (childList).get(i);
                                String id = os.get("id").toString();
                                boolean flag = false;
                                flag=(sourceTableName.equals(souChildTableName)?true:false);
                                //�ڶ�����ϸ�����һ����ϸ��û��ʱ�������ݶ���ʾ������ϸʱ����Ҫ�����ϸ��ʾ
                                if (k!=0||childIds.size()==0||(childIds.size()>0&&childIds.contains(id))) {
                                	flag=true;
                                }

                                if (flag) {
	                                if(childMap!=null){
	                                	KRLanguageQuery kr= new KRLanguageQuery();
	                                	for(int j=0;j<childTable.getFieldInfos().size();j++){
	                                		DBFieldInfoBean dbf = (DBFieldInfoBean)childTable.getFieldInfos().get(j);
	                                		String fieldName = dbf.getFieldName() ;
	                                		Object souO = childMap.get(fieldName);
	                                		paramMap.put(souChildTableName+"_"+fieldName, souO);
	                                		//����Ĵ���ԭ����Ϊ����ͬ�ֶ��Զ�ӳ�䣬����SourceID�������Զ�ӳ�䣬���Ի������ݿͻ�����
	                                		if(!fieldName.equalsIgnoreCase("SourceID")){
		                                		Object value = os.get(fieldName) ;
		                                		paramMap.put(childTableName+"_"+fieldName, value) ;
	                                		}
	                                	}
	
	
	                                	 //���ù�������Ӧ�ֶ�ֵ
	                                    for (int j = 0;j < childTable.getFieldInfos().size(); j++) {
	                                        DBFieldInfoBean dbf = (DBFieldInfoBean)childTable.getFieldInfos().get(j);
	                                        Object souO = childMap.get(dbf.getFieldName());
	                                        if (dbf.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE||
	                                        		dbf.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE) {
	                                            if (souO != null && os.get(souO) != null) {
	                                                Result rs3 = dbmgt.getRefViewValues(dbf,os.get(souO).toString(),map, sunClassCode, os,paramMap,lg.getId(),isLastSunCompany,kr,mop.getScope(MOperation.M_UPDATE));
	                                                if (rs3.getRetCode() !=ErrorCanst.DEFAULT_SUCCESS) {
	                                                	EchoMessage.error().add(getMessage(request,"common.msg.error")).setRequest(request);
	                                                    return getForward(request, mapping, "message");
	                                                }
	                                            }
	                                        }
	                                    }
	
	                                    Result rsn=dbmgt.getLanguageId(kr, moreLan);
	                                    if(rsn.retCode!=ErrorCanst.DEFAULT_SUCCESS){
	                                    	//����ʧ��
	                                        EchoMessage.error().add(getMessage(request, "common.msg.error")).
	                                                setRequest(request);
	                                        return getForward(request, mapping, "message");
	                                    }
	
	                                    String[] ss = new String[2];
	                                    ss[0] = "";
	                                    for (int j = 0; j < cols.size(); j++) {
	                                        String targetName = ((String[]) cols.
	                                                get(j))[3];
	                                        String type = ((String[]) cols.get(j))[
	                                                2];
	                                        Object souO = childMap.get(targetName);
	                                        if (targetName.indexOf('.') >= 0) {
	                                            souO = targetName;
	                                        }
	                                        DBFieldInfoBean bean;
	                                        if(((String[]) cols.get(j))[3].indexOf(".")<0){
	                                        	bean= GlobalsTool.getFieldBean(childTable.getTableName(),((String[]) cols.get(j))[3]);
	                                        }else{
	                                        	bean=GlobalsTool.getFieldBean(((String[]) cols.get(j))[4].substring(0,((String[]) cols.get(j))[4].indexOf(".")), ((String[]) cols.get(j))[4].substring(((String[]) cols.get(j))[4].indexOf(".")+1));
	                                        }
		                                    if(bean==null){
		                                        bean=GlobalsTool.getFieldBean(((String[]) cols.get(j))[5].substring(0,((String[]) cols.get(j))[5].indexOf(".")), ((String[]) cols.get(j))[5].substring(((String[]) cols.get(j))[5].indexOf(".")+1));
		                                     }
	                                        Object tempValue=os.get(souO);
	                                        //��ǰ�����е��ֶ���Ҫ��Ĭ��ֵ��������û��Ҫ
	                                        if(((String[]) cols.get(j))[3].indexOf(".")<0&&tempValue==null&&bean.getDefValue(this.getLoginBean(request).getId())!=null&&bean.getDefValue(this.getLoginBean(request).getId()).length()>0){
	                                        	tempValue=bean.getDefValue(this.getLoginBean(request).getId());
	                                        }
	                                        String value = tempValue==null?"":tempValue.toString();
	                                        String fieldName = ((String[]) cols.get(j))[3] ;
	                                        if(truncIndexs.contains(j)){
	                                        	if(value!=null&&value.toString().length()>0){
	                                        		fieldName = "Seq_hid" ;
	                                        		String[]seqList=value.toString().split("~");
	                                        		value=seqList[seqList.length-1];
	                                        	}
	                                        }
	                                        //�����ж϶�����
	                                        if(bean.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||bean.getInputTypeOld() ==DBFieldInfoBean.INPUT_LANGUAGE){
	                                        	if(moreLan.get(value) !=null){
	                                        		value=((KRLanguage)moreLan.get(value)).get(this.getLocale(request).toString());
	                                        	}
	                                        }
	                                        if (type.equals("float")) {
	                                            String df = ((String[]) cols.
	                                                    get(j))[3];
	                                            String dt = null;
	                                            if (df.indexOf(".") > 0) {
	                                                dt = df.substring(0,
	                                                        df.indexOf("."));
	                                                df = df.substring(df.indexOf(
	                                                        ".") + 1);
	                                            } else {
	                                                dt = ((String[]) cols.
	                                                        get(j))[4];
	                                            }
	                                            value = GlobalsTool.formatNumberS(Double.parseDouble(value.
	                                                    equals("") ? "0" : value), false, false,"",dt+"."+df);
	                                        }
	                                        ss[0] = ss[0] + "\""+fieldName+"@koron@" + (value == null ? "" : value.toString().replaceAll("\\\\", "\\\\\\\\")) + "\",";
	                                    }
	                                    ss[0] = ss[0].substring(0,
	                                            ss[0].length() - 1);
	                                    ss[1] = id;
	                                    rows.add(ss);
	                                }
                                }
                            }
                        }
                    }
                    rowsList.add(new Object[] {childTable.getTableName(), rows});
                }
                if (childTableList != null) {
                    
                	Collections.sort(childTableList, new SortDBTable()) ;
                	request.setAttribute("childTableList", childTableList) ;
                    HashMap tblChildMap=new HashMap();
                    for(int i=0;i<childTableList.size();i++){
                    	tblChildMap.put(((DBTableInfoBean)childTableList.get(i)).getTableName(),String.valueOf(i));
                    	tblChildMap.put(String.valueOf(i),((DBTableInfoBean)childTableList.get(i)).getTableName());
                    }
                    request.setAttribute("tblChildMap", tblChildMap);
                }
                DBTableInfoBean mainTable = DDLOperation.getTableInfo(map,tableName);

                DBTableInfoBean sourceTable = DDLOperation.getTableInfo(map,sourceTableName);
                BaseCustomFunction impl = (BaseCustomFunction) BaseEnv.functionInterface.get(sourceTableName);
                if (impl != null) {
                    impl.onUpdatePrepare(mapping, form, request, response,
                                         getLoginBean(request), keyId,
                                         sourceTableName, map,
                                         values);
                }
                
                String designId = getParameter("designId", request) ;
                OAWorkFlowTemplate tableFlow = BaseEnv.workFlowInfo.get(tableName) ;
                WorkFlowDesignBean designFlow = BaseEnv.workFlowDesignBeans.get(mop.getModuleId());
                if(designFlow==null && tableFlow!=null){
                	designFlow = BaseEnv.workFlowDesignBeans.get(tableFlow.getId()) ;
                }
                
                
                ReportSetMgt mgt = new ReportSetMgt();
                String moduleType = getParameter("moduleType", request) ;
                Result rsPrint = mgt.getBillTable(tableName,moduleType);
                if (rsPrint.getRealTotal() > 0) {
                    List list = (List) rsPrint.getRetVal();
                    ReportsBean report = (ReportsBean) list.get(0);
                    Map det = report.getReportDetBean();
                    Collection con = det.values();
                    Iterator iter = con.iterator();
                    while (iter.hasNext()) {
                        ReportsDetBean detBean = (ReportsDetBean) iter.next();
                        //newFlag=OLD��ʾ����ƴ�ӡ��ʽ
                        //����Ҫ�ж��Ƿ�������˺��ӡ.������������������ǲ��ܳ��ִ�ӡ��Ť��
                        if (detBean.getNewFlag().equals("OLD") 
                        		&& !(tableFlow!=null && tableFlow.getTemplateStatus()==1 && designFlow!=null && report.getAuditPrint()==1)) {
                            request.setAttribute("reportNumber", report.getReportNumber());
                            request.setAttribute("print", "true");
                            break;
                        }
                    }
                }
                
                if(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1){
                	request.setAttribute("OAWorkFlow", true);
                	request.setAttribute("currNodeId", 0);
                	designId = BaseEnv.workFlowInfo.get(tableName).getId();
                	request.setAttribute("designId", BaseEnv.workFlowInfo.get(tableName).getId());
                }
                
                
                String butTag = parseExtendButton(request, tableInfo, "add");
                if (butTag != null && butTag.length() > 0) {
                    request.setAttribute("extendButton", butTag);
                }
                OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(tableName);


                String defineInfo=request.getParameter("defineInfo");
                Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
            	MessageResources resources = null;
        		if (ob instanceof MessageResources) {
        		    resources = (MessageResources) ob;
        		}
        		
                Result addView = dbmgt.addView(tableName, BaseEnv.tableInfos, targetValues, defineInfo, resources, this.getLocale(request), lg);
                if(addView.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
                	SaveErrorObj saveErrrorObj = dbmgt.saveError(addView, getLocale(request).toString(), "");
                	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
        				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
        			} else {
        				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
        			}
                    return getForward(request, mapping, "message");
                }
                ArrayList<String> defineField = (ArrayList<String>)values.get("DEFINE_INPUTTYPE");
                
                ArrayList<DBFieldInfoBean> fieldList;
                HashMap childFieldMap = new HashMap();
        		try { 
        			fieldList = DynDBManager.getMainFieldList(tableName, tableInfo,defineField, configList, template, null, moduleTable, mop, "0", false, f_brother, lg, scopeRight);
        			if (childTableList != null) {
        				for(DBTableInfoBean dib :(ArrayList<DBTableInfoBean>)childTableList){
        					ArrayList<DBFieldInfoBean> dibFieldList = DynDBManager.getMainFieldList(tableName, dib,defineField, childTableConfigList.get(dib.getTableName()), template, null, moduleTable, mop, "0", false, f_brother, lg, scopeRight);
        					childFieldMap.put(dib.getTableName(), dibFieldList);
        				}
        			}
        		} catch (Exception e) {
        			EchoMessage.error().add(e.getMessage()).setRequest(request);
                    return getForward(request, mapping, "message");
        		}


        		request.setAttribute("childFieldMap", childFieldMap) ;

                request.setAttribute("fieldInfos", DynDBManager.distinctList(fieldList)) ;
                request.setAttribute("MOID", mop.moduleId);
                request.setAttribute("mainTable", mainTable);
                request.setAttribute("values", targetValues);
                request.setAttribute("result", rowsList);
                request.setAttribute("tableName", tableName);
                request.setAttribute("moduleName",getModuleNameByLinkAddr(request, mapping));
                request.setAttribute("fromFoward", "quote") ;
                forward = getForward(request, mapping, "functionUpdate");

                //���سɹ�
            } else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
            	 //��¼�����ڴ���
                EchoMessage.error().add(getMessage(
                        request, "common.error.nodata")).setRequest(request);
                return getForward(request, mapping, "message");
            } else {
            	 //����ʧ��
            	if(rs.retVal != null && !(rs.retVal instanceof HashMap)){
            		EchoMessage.error().add(rs.retVal.toString()).setClosePopWin(request.getParameter("popWinName")).
                    	setRequest(request);
            	}else{
            		EchoMessage.error().add(getMessage(request, "common.msg.error")).setClosePopWin(request.getParameter("popWinName")).
                        setRequest(request);
            	}
                return getForward(request, mapping, "message");
            }
        }

        return forward;
    }
    /**
     * ��֤����������
     * @param tableInfo
     * @param popError
     */
    private void validatePopSelect(DBTableInfoBean  tableInfo,List popError,HttpServletRequest request){
    	for(int i=0;i<tableInfo.getFieldInfos().size()-1;i++){
        	DBFieldInfoBean fieldInfo1=(DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
        	if(fieldInfo1.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE){
	        	for(int j=i+1;j<tableInfo.getFieldInfos().size();j++){
	        		DBFieldInfoBean fieldInfo2=(DBFieldInfoBean)tableInfo.getFieldInfos().get(j);
	        		if(fieldInfo2.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE){
	        			if(fieldInfo1.getSelectBean()!=null&&fieldInfo2.getSelectBean()!=null){
	        			ArrayList array1=fieldInfo1.getSelectBean().getViewFields();
	        			ArrayList array2=fieldInfo2.getSelectBean().getViewFields();
	        			for(int k=0;k<array1.size();k++){
	        				PopField popField1=(PopField)array1.get(k);
	        				for(int p=0;p<array2.size();p++){
	        					PopField popField2=(PopField)array2.get(p);
	        					if(popField1.getAsName().equals(popField2.getAsName())){
	        						popError.add(this.getMessage(request, "customTable.lb.tableName")+tableInfo.getTableName()+this.getMessage(request,"add.popSelect.error",fieldInfo1.getFieldName(),fieldInfo2.getFieldName(),popField1.getFieldName())+"\t");
	        					}
	        				}
	        			}
	        		  }
	        		}
	        	}
        	}
        }
    }
    /**
     * ���ݲ���������ӣ����ڼ��޸�
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward updateAdd(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String tableName=request.getParameter("tableName");
    	String fieldName=request.getParameter("fieldName");
    	String VAL = request.getParameter("VAL");
    	Result rs = userMgt.getDataId(tableName, fieldName, VAL);
    	
        String moduleType = getParameter("moduleType", request) ;
        String param = "/UserFunctionQueryAction.do?tableName="+tableName;
        if(moduleType !=null && moduleType.length() > 0){
        	param +="&moduleType="+moduleType;
        }
        MOperation mo = (MOperation) getLoginBean(request).getOperationMap().get(param);
        
    	if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)rs.retVal).size() > 0){
    		if (!mo.update()|| !mo.query()) {
    			ActionForward forward = getForward(request, mapping, "message");
                EchoMessage.error().add("�Բ�����û���޸�Ȩ��").setClosePopWin(request.getParameter("popWinName")).setRequest(request);
                return forward;
            }
    		String id = ((Object[])((ArrayList)rs.retVal).get(0))[0]+"";
    		return updatePrepare(mapping, form, request, response, id);
    	}else{
    		if (!mo.add()) {
    			ActionForward forward = getForward(request, mapping, "message");
                EchoMessage.error().add("�Բ�����û�����Ȩ��").setClosePopWin(request.getParameter("popWinName")).setRequest(request);
                return forward;
            }
    		return addPrepare(mapping, form, request, response);
    	}
    }
    /**
     * ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
    	//ͨ���ര�ڵõ����ӵ�ַ�õ�Ȩ��
    	String tableName = getParameter("tableName", request);
    	Hashtable allTables = (Hashtable) request.getSession().
        getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
        MOperation mop = GlobalsTool.getMOperationMap(request) ;
        if(mop == null){
        	//������ģ���޷���Ȩ��
        	request.setAttribute("noback", true) ;
            ActionForward forward = getForward(request, mapping, "message");
            EchoMessage.error().add(getMessage(request,"common.msg.RET_NO_RIGHT_ERROR")).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
            return forward;
        }
        
        ArrayList scopeRight = new ArrayList();
        scopeRight.addAll(mop.getScope(MOperation.M_ADD));
        scopeRight.addAll(this.getLoginBean(request).getAllScopeRight());
        
        String parentTableName=this.getParameter("parentTableName",request);
        parentTableName=parentTableName==null?"":parentTableName;
        request.setAttribute("parentTableName", parentTableName);
        //��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��
        if (tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
           request.setAttribute("mainTableName", parentTableName);
        } else {
           request.setAttribute("mainTableName", tableName);
        }
        //�õ�����ģ����ֶ���Ϣ���˹�����Ϊ��ʵ�ֲ�ͬģ��ʹ��ͬһ��������ͬ��define
        HashMap<String,ArrayList<String[]>> moduleTable = (HashMap<String,ArrayList<String[]>>)BaseEnv.ModuleField.get(mop.getModuleUrl());
        if(moduleTable!=null){ 
        	request.setAttribute("moduleTable", moduleTable);
        	request.setAttribute("moduleUrl", mop.getModuleUrl()) ;
        }
    	 //�����ֵܱ��ID
        String f_brother = this.getParameter("f_brother", request);
        f_brother = f_brother == null ? "" : f_brother;
        request.setAttribute("f_brother", f_brother);
        
        if(f_brother!=null && f_brother.length() >0){
        	Result rr = userMgt.brotherName(parentTableName, f_brother);
        	request.setAttribute("brotherRMName", rr.getRetVal());
        }

        
        String parentCode = getParameter("parentCode", request);
        parentCode = parentCode==null?"":parentCode ;
        
        if(parentCode.length() > 0 && tableInfo.getCopyParent()==1){ //����Ǵ��ڸ����������ֱ���൱�ڸ��Ƹ���
        	Result rs = userMgt.getDataId(tableName, "classCode", parentCode);
        	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)rs.retVal).size() > 0){
        		String pid = String.valueOf(((ArrayList<Object[]>)rs.retVal).get(0)[0]);
	        	request.setAttribute("copy", "copy");
	        	request.setAttribute("copyParent", "copyParent");
	            return updatePrepare(mapping, form, request, response,pid);
        	}
        }
        
        //�����ƻ� ��Ҫ�õ��Ĳ���
        request.setAttribute("planType",getParameter("planType", request)) ;
        request.setAttribute("backType",getParameter("backType", request)) ;
        request.setAttribute("strType",getParameter("strType", request)) ;
        request.setAttribute("strDate",getParameter("strDate", request)) ;
        request.setAttribute("userId",getParameter("userId", request)) ;
        
        LoginBean lg = new LoginBean();
        lg = (LoginBean) request.getSession().getAttribute("LoginBean");
        Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        
        request.setAttribute("sessionSet", sessionSet);
        
        Hashtable map = allTables;
        
        if(f_brother!=null && (BaseEnv.workFlowDesignBeans.get(request.getParameter("designId"))!=null
				||BaseEnv.workFlowDesignBeans.get(mop.getModuleId())!=null)){
        	String flowId = f_brother;
        	OAMyWorkFlowMgt oamgt=new OAMyWorkFlowMgt();
        	String checkPerson = (String) oamgt.getCurrCheckPerson(flowId).retVal ;
        	if(checkPerson!=null && !checkPerson.equals(";"+getLoginBean(request).getId()+";")){
        		String curIndex = request.getParameter("winCurIndex") ; 
        		EchoMessage.error().add(getMessage(request,"common.flow.not.update"))
        		 					.setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName+"&f_brother="+f_brother
                                      		   +"&parentTableName="+getParameter("parentTableName", request)+"&winCurIndex="+("".equals(curIndex)?"&LinkType=@URL:&noBack=true":curIndex))
        		 					.setClosePopWin(request.getParameter("popWinName")).setRequest(request);
        		 return getForward(request, mapping, "message");
        	}
		}
        
        //�����ǰ�ڼ䲻Ϊ�ڳ������ڳ������ӣ�ɾ�����޸İ�ť������ʾ
        if(!"-1".equals(((Hashtable)BaseEnv.sessionSet.get(lg.getId())).get("NowPeriod"))){
        	String moduleType=request.getParameter("moduleType");
        	if(tableName.equals("tblCompanybeginning")||tableName.equals("tblBeginStock")||(tableName.equals("tblFixedAssetAdd")&&"0".equals(moduleType))){
        		request.setAttribute("CannotOper", "true");
        	}
        }
        
        //�����˷�֧����ʱ�����������ĩ����֧������������Ӳ����������
        String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
        boolean openSunCompany = Boolean.parseBoolean(openValue);
        boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
        boolean isSharedTable = tableInfo.getIsSunCmpShare() == 1 ? true : false;
        //����ְԱ����ű�ʱ������ĩ����֧��������
        boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo.
                getTableName()) ||
                              "tblDepartment".equalsIgnoreCase(tableInfo.
                getTableName())
                              ? true : false;
        if (openSunCompany && !isLastSunCompany && !isSharedTable &&
            !isEmpOrDept) {
            EchoMessage.error().add(getMessage(request,
                                               "common.msg.notLastSunCompany")).
                    setRequest(request);
            return getForward(request, mapping, "message");
        }
        String sysParamter = tableInfo.getSysParameter();
        int periodMonth = -1;
        AccPeriodBean accBean=(AccPeriodBean)sessionSet.get("AccPeriod");
        if (accBean!=null) {
            periodMonth = accBean.getAccMonth();
        }

        if (periodMonth < 0) {
            if (null != sysParamter) {
                if (sysParamter.equals("UnUseBeforeStart") ||
                    sysParamter.equals
                    ("CurrentAccBefBillAndUnUseBeforeStart")) {
                    EchoMessage.error().add(getMessage(request,
                            "com.UnUseBeforeStart")+", <a href='/SysAccAction.do?type=beginAcc'>"+getMessage(request, "userfunction.msg.checkhereOpen")+"</a>").
                            setClosePopWin(request.getParameter("popWinName")).
                            setAlertRequest(request);
                    return getForward(request, mapping, "message");
                }
            }
        }
        SystemSettingBean inIniAmt=BaseEnv.systemSet.get("InIniAmount");
        if(inIniAmt!=null&&!inIniAmt.getSetting().equals("")){//���ϵͳ�������á��ڳ�¼������
        	  boolean inIniAmount=false;
              if(tableName.equalsIgnoreCase("tblSalesOutStock")||tableName.equalsIgnoreCase("tblOtherOut")){//���Ϊ���۳��ⵥ���������ⵥ
                 String iniAm=BaseEnv.systemSet.get("InIniAmount").getSetting();
                 if(Boolean.parseBoolean(iniAm)){
                   boolean isOn=false;
                   DBTableInfoBean temptable=DDLOperation.getTableInfo(map, tableName+"Det");
                   if(temptable!=null){
        			   for(int i=0;i<temptable.getFieldInfos().size();i++){
        				   DBFieldInfoBean tempFi=(DBFieldInfoBean)temptable.getFieldInfos().get(i);
        				   if("iniOut".equals(tempFi.getFieldIdentityStr())){
        					   if(tempFi.getInputType()==DBFieldInfoBean.INPUT_NORMAL){
        	                	   isOn=true;break;
        	                   }
        				   }
        			   }
        		   }

                   String onOff=request.getParameter("onOff");
                   if(isOn){//���ʵ��������ʾ
                	   request.setAttribute("inIniAmountName", this.getMessage(request, "bp.outstock.inIniAmountNO"));
                	   if(onOff!=null&&onOff.equals("onOff")){
                		   request.setAttribute("inIniAmountName", this.getMessage(request, "bp.outstock.inIniAmount"));
                		   //Result rs=pcmgt.updateFactQty(tableName, DBFieldInfoBean.INPUT_HIDDEN);
                		   DBTableInfoBean tempTable=DDLOperation.getTableInfo(map, tableName+"Det");
                		   if(tempTable!=null){
                			   for(int i=0;i<tempTable.getFieldInfos().size();i++){
                				   DBFieldInfoBean tempFi=(DBFieldInfoBean)tempTable.getFieldInfos().get(i);
                				   if("iniOut".equals(tempFi.getFieldIdentityStr())){
                				   tempFi.setInputType(DBFieldInfoBean.INPUT_HIDDEN);
                				   }
                			   }
                		   }
                	   }
                   }else{
                	   request.setAttribute("inIniAmountName", this.getMessage(request, "bp.outstock.inIniAmount"));
                	   if(onOff!=null&&onOff.equals("onOff")){
                		   request.setAttribute("inIniAmountName", this.getMessage(request, "bp.outstock.inIniAmountNO"));
                		   //Result rs=pcmgt.updateFactQty(tableName, DBFieldInfoBean.INPUT_NORMAL);
                		   DBTableInfoBean tempTable=DDLOperation.getTableInfo(map, tableName+"Det");
                		   if(tempTable!=null){
                			   for(int i=0;i<tempTable.getFieldInfos().size();i++){
                				   DBFieldInfoBean tempFi=(DBFieldInfoBean)tempTable.getFieldInfos().get(i);
                				   if("iniOut".equals(tempFi.getFieldIdentityStr())){
                				   tempFi.setInputType(DBFieldInfoBean.INPUT_NORMAL);
                				   }
                			   }
                		   }
                	   }
                   }
              	   inIniAmount=true;
                 }
              }
              request.setAttribute("inIniAmount",inIniAmount);
        }
        HashMap values=new HashMap();
        values.put("f_brother", f_brother) ;
        values.put("parentCode", parentCode);
        request.setAttribute("values",values);
        /*���Ӳ���Ĭ��ֵ*/
        HashMap paramValue = new HashMap() ;
        String urlValue="";
        
        //�������͸�Ĭ������
        List listField = tableInfo.getFieldInfos();
        for (int i = 0; i < listField.size(); i++) {
            DBFieldInfoBean field = (DBFieldInfoBean) listField.get(i);
            if(field.getDefaultValue()!=null&&!field.getDefaultValue().equals("")){
	            if (field.getFieldType() == 5) {
	                field.setDefaultValue(BaseDateFormat.format(new Date(),
	                        BaseDateFormat.yyyyMMdd));
	                if("BornDate".equals(field.getFieldName())){
	                	field.setDefaultValue("");//Bug #10752 ����Ĭ�ϵ�ǰ���ڣ����Ϊ��
	                }
	            }else if (field.getFieldType() == 6) {
	                field.setDefaultValue(BaseDateFormat.format(new Date(),
	                        BaseDateFormat.yyyyMMddHHmmss));
	            }
            }
            if("paramDefaultValue".equals(field.getFieldIdentityStr())){
            	 /*���ô�����Ĭ��ֵ*/
                String fieldValue = getParameter(field.getFieldName(), request) ;
            	paramValue.put(field.getFieldName(), fieldValue) ;
            }
            // ������������Ĭ��ֵ
            if(field.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE){
               if(field.getInputValue()!=null&&field.getInputValue().length()>0&&field.getDefaultValue()!=null&&field.getDefaultValue().length()>0){
            	   dbmgt.getDefRefValue(field,values,sessionSet,getLoginBean(request).getId(),allTables,"");
               }
            }else{
            	if(field.getDefaultValue()!=null&&field.getDefaultValue().contains("@Sess:")){
            		String name=field.getDefaultValue().substring(field.getDefaultValue().indexOf("@Sess:")+"@Sess:".length());
            		String value;
            		if(name.equals("parentCode")){
            			value = request.getParameter("parentCode");
            		}else{
            			value=sessionSet.get(name)==null?"":sessionSet.get(name).toString();
            		}
                	values.put(field.getFieldName(),value);
            	}
            }
            //ͬһ�ű�ͬ��ģ�飬�������ӵ�ַ�м����ֶ�������ʾ���ģ�飬������Щ�ֶ���ֵ�����ڲ�ѯ �ֶ���Ҫ�ۼ�������¼
           if(GlobalsTool.getUrlBillDef(request, field.getFieldName())!=null&&GlobalsTool.getUrlBillDef(request, field.getFieldName()).length()>0 ){
        	   if(field!=null && field.getFieldType()==DBFieldInfoBean.FIELD_INT){
        		   urlValue+=" and "+field.getFieldName()+"="+GlobalsTool.getUrlBillDef(request, field.getFieldName())+"";
        	   }else{
        		   urlValue+=" and "+field.getFieldName()+"='"+GlobalsTool.getUrlBillDef(request, field.getFieldName())+"'";
        	   }
           }
           if("moduleType".equals(field.getFieldName()) && request.getParameter(field.getFieldName()) != null 
        		   && request.getParameter(field.getFieldName()).length()>0 && !urlValue.contains("moduleType") ){
        	   if(field!=null && field.getFieldType()==DBFieldInfoBean.FIELD_INT){
        		   urlValue+=" and "+field.getFieldName()+"="+request.getParameter(field.getFieldName())+"";
        	   }else{
        		   urlValue+=" and "+field.getFieldName()+"='"+request.getParameter(field.getFieldName())+"'";
        	   }
           }
        }
        //������ϸ��Ĵ�sess��Ĭ��ֵ
        ArrayList childTableList = DDLOperation.getChildTables(tableName, map);
        for (int i = 0; i < childTableList.size(); i++) {
        	DBTableInfoBean db=(DBTableInfoBean)childTableList.get(i);
        	List childFields =db.getFieldInfos();
        	
        	for(int j=0;j<childFields.size();j++){
        		DBFieldInfoBean field = (DBFieldInfoBean) childFields.get(j);        		
        		if(field.getDefaultValue()!=null&&field.getDefaultValue().contains("@Sess:")){
        			dbmgt.getChildDefRefValue(field,values,sessionSet,getLoginBean(request).getId(),allTables,db.getTableName());
        		}
        	}
        }        
        
        /* ��ȡ���ӵ�ַ�������ӵ�ַ�д���(paramValue=true)ʱ������Ĳ���ֵ�����ӵ�values�� */
        String paramStr = request.getQueryString();
        String pStr = "paramValue=true";
        if(paramStr.indexOf(pStr) != -1){
        	//����
        	String paramVal = paramStr.substring(paramStr.indexOf(pStr)+pStr.length());
        	String[] paramSplit = paramVal.split("&");
        	for(String s : paramSplit){
        		if(s != null && !"".equals(s) && s.indexOf("=")!=-1&&s.split("=").length>1){
        			String val = URLDecoder.decode(s.split("=")[1],"UTF-8");
        			if(val != null){
        				val  = GlobalsTool.replaceSpecLitter(val);
        			}
        			values.put(s.split("=")[0], val);
        			//����ǹ�����ѡ��ҲҪ����ʾ�ֶε�ֵ���浽values��
        			DBFieldInfoBean field=GlobalsTool.getFieldBean(tableName, s.split("=")[0]);
        			if(field!=null&&(field.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE || (
        					field.getInputType()==DBFieldInfoBean.INPUT_ONLY_READ&&field.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE) )){
        				DynDBManager dyn=new DynDBManager();
        				dyn.getRefReturnValues(field, val, allTables,lg.getSunCmpClassCode(), values, values, lg.getId(), true, null, null);
        			}
        			
        		}
        	}
        }
        //���Ǹ��������paramValue,��Ϊ�ڱ�ṹ�޸�ʱ��֪��ʲôԭ��@paramValue ǰ���@para������룬�����޸�һ�κ�ֵ�ͱ��ˣ����Կ���һ��defaultValue������
        paramStr = request.getQueryString();
        pStr = "defaultValue=true";
        if(paramStr.indexOf(pStr) != -1){
        	//����
        	String paramVal = paramStr.substring(paramStr.indexOf(pStr)+pStr.length());
        	String[] paramSplit = paramVal.split("&");
        	for(String s : paramSplit){
        		if(s != null && !"".equals(s) && s.indexOf("=")!=-1&&s.split("=").length>1){
        			String val = URLDecoder.decode(s.split("=")[1],"UTF-8");
        			if(val != null){
        				val  = GlobalsTool.replaceSpecLitter(val);
        			}
        			values.put(s.split("=")[0], val);
        			//����ǹ�����ѡ��ҲҪ����ʾ�ֶε�ֵ���浽values��
        			DBFieldInfoBean field=GlobalsTool.getFieldBean(tableName, s.split("=")[0]);
        			if(field!=null&&(field.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE || (
        					field.getInputType()==DBFieldInfoBean.INPUT_ONLY_READ&&field.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE) )){
        				DynDBManager dyn=new DynDBManager();
        				dyn.getRefReturnValues(field, val, allTables,lg.getSunCmpClassCode(), values, values, lg.getId(), true, null, null);
        			}
        			
        		}
        	}
        }
        
        
        //��������й�����ѡ���ֶ��еĵ������Ƿ������ͬ�ķ��ص�displayField�ֶκ͹��������ֶ�
        List popError=new ArrayList();
        validatePopSelect(tableInfo,popError,request);
        //����ӱ��й�����ѡ���ֶ��еĵ������Ƿ������ͬ�ķ��ص�displayField�ֶκ͹��������ֶ�
        ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
        // �����������Զ�������
        ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;
        Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;
        Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
												request.getSession().getServletContext().getAttribute("userSettingColConfig") ;
        if(userColConfig!=null && userColConfig.size()>0){
        	configList = userColConfig.get(tableName+"bill") ;
        	if(configList!=null){
        		allConfigList.addAll(configList) ;
        	}
        }
        String allChildName = tableName+ "," ;//��������ӱ�ȫ���ö�������
        //������ϸ���Զ����е���ʾ
        for(int i=0;i<childTableList.size();i++){
        	DBTableInfoBean childTableInfo=(DBTableInfoBean)childTableList.get(i);
            validatePopSelect(childTableInfo,popError,request);
            allChildName+=childTableInfo.getTableName()+ "," ;
            if(userColConfig!=null && userColConfig.size()>0){
            	ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName()+"bill") ;
            	if(childConfigList!=null){
            		//�������ģ���ֶ����ã���ô�������ֶ��н���������Щ�ֶΣ�ҳ�潫����������
            		if(moduleTable!=null&&moduleTable.get(childTableInfo.getTableName())!=null){
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
            			allConfigList.addAll(cols) ;
	            		childTableConfigList.put(childTableInfo.getTableName(), cols) ;
            		}else{
	            		allConfigList.addAll(childConfigList) ;
	            		childTableConfigList.put(childTableInfo.getTableName(), childConfigList) ;
            		}
            	}
            }
        }
        if(popError.size()>0){
        	String msg="";
        	for(int i=0;i<popError.size();i++){
        		msg+=popError.get(i).toString();
        	}
            EchoMessage.error().add(msg).setBackUrl("/UserFunctionQueryAction.do?tableName=" +
                                tableName + "&parentCode=" + parentCode +"&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);

            return this.getForward(request,mapping,"alert");
        }

    	Collections.sort(childTableList, new SortDBTable()) ;
    	request.setAttribute("childTableList", childTableList) ;
        request.setAttribute("allConfigList", allConfigList) ;
        request.setAttribute("allTableName", allChildName) ;
        request.setAttribute("childTableConfigList", childTableConfigList) ;
        
        HashMap tblChildMap=new HashMap();
        for(int i=0;i<childTableList.size();i++){
        	tblChildMap.put(((DBTableInfoBean)childTableList.get(i)).getTableName(),String.valueOf(i));
        	tblChildMap.put(String.valueOf(i),((DBTableInfoBean)childTableList.get(i)).getTableName());
        }
        request.setAttribute("tblChildMap", tblChildMap);

        DBTableInfoBean tableinfo = DDLOperation.getTableInfo(map, tableName);

        
        //�����ǰģ������ƹ�����������ݹ������п�ʼ�ڵ���ֶε���������ʾ�ֶ�
        boolean isOAWorkFlow=false;
        String designId = getParameter("designId", request) ;
        OAWorkFlowTemplate tableFlow = BaseEnv.workFlowInfo.get(tableName) ;
        if(designId==null && tableFlow!=null){
        	designId = tableFlow.getId();
        }
        WorkFlowDesignBean designFlow = BaseEnv.workFlowDesignBeans.get(mop.getModuleId());
        if(designFlow==null && tableFlow!=null){
        	designFlow = BaseEnv.workFlowDesignBeans.get(tableFlow.getId()) ;
        }
        if(tableFlow != null && tableFlow.getTemplateStatus()==1&& designFlow!=null){
        	WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(mop.getModuleId());
        	if(designBean==null){
        		designBean=BaseEnv.workFlowDesignBeans.get(BaseEnv.workFlowInfo.get(tableName).getId());
        		request.setAttribute("workFlow", "ERP");
        		if(designId==null || designId.trim().length()==0){
        			designId = BaseEnv.workFlowInfo.get(tableName).getId() ;
        		}
        		request.setAttribute("designId", designId);
        	}else{
        		request.setAttribute("workFlow", "OA");
        		if(designId==null || designId.trim().length()==0){
        			designId = mop.moduleId ;
        		}
        		request.setAttribute("designId", designId);
        	}       	
        	isOAWorkFlow=true;
        	if(!"HRReview".equals(tableName)){
        		request.setAttribute("OAWorkFlow", "true");
        	}
        }
        //ִ���Զ���define�����ó�ʼֵ�������ֶε�
        String defineInfo=request.getParameter("defineInfo");
        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		
        Result addView = dbmgt.addView(tableName, allTables, values, defineInfo, resources, this.getLocale(request), lg);
        if(addView.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
        	SaveErrorObj saveErrrorObj = dbmgt.saveError(addView, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
            return getForward(request, mapping, "message");
        }
        ArrayList<String> defineField = (ArrayList<String>)values.get("DEFINE_INPUTTYPE");
        
        OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(tableName);
        
        
        
        //����Ȩ��,�����ã�������������ʾ��Щ�ֶ�
        ArrayList<DBFieldInfoBean> fieldList;
        
        HashMap childFieldMap = new HashMap();
		try {
			fieldList = DynDBManager.getMainFieldList(tableName, tableInfo,defineField, configList, template, null, moduleTable, mop, "0", false, f_brother, lg, scopeRight);
			if (childTableList != null) {
				for(DBTableInfoBean dib :(ArrayList<DBTableInfoBean>)childTableList){
					ArrayList<DBFieldInfoBean> dibFieldList = DynDBManager.getMainFieldList(tableName, dib,defineField, childTableConfigList.get(dib.getTableName()), template, null, moduleTable, mop, "0", false, f_brother, lg, scopeRight);
					childFieldMap.put(dib.getTableName(), dibFieldList);
				}
			}
		} catch (Exception e) {
			BaseEnv.log.error("UserFunctionAction.addPrepare Error:",e);
			EchoMessage.error().add(e.getMessage()).setRequest(request);
            return getForward(request, mapping, "message");
		}


		request.setAttribute("childFieldMap", childFieldMap) ;
        
        request.setAttribute("fieldInfos2", tableInfo.getFieldInfos()) ;
        request.setAttribute("fieldInfos", DynDBManager.distinctList(fieldList)) ;
        request.setAttribute("mainTable",tableinfo);
        if (childTableList != null) {
        	Collections.sort(childTableList, new SortDBTable()) ;
            request.setAttribute("childTableList2", childTableList);
        }

        String configType = getParameter("configType", request) ;
        if("colConfig".equals(configType)){
        	return getForward(request, mapping, "colConfig");
        }
        
        request.setAttribute("paramValues", paramValue) ;
        //����Ƿ�������ѯ�Ƿ����ֶ���Ҫ���Ƹ��������
        if(tableInfo.getClassFlag()==1&&parentCode.length()>0){
        	ArrayList copySupField=new ArrayList();
        	DBFieldInfoBean b;
        	for(int i=0;i<tableInfo.getFieldInfos().size();i++){
        		b=(DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
        		if(b.getFieldIdentityStr()!=null&&b.getFieldIdentityStr().equals("copySuper")){
        			copySupField.add(tableInfo.getFieldInfos().get(i));
        		}
        	}
        	if(copySupField.size()>0){
        		Result rs=dbmgt.getSuperValue(tableName, parentCode, copySupField);
        		if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
        			EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
        			return getForward(request, mapping, "message");
        		}else{
        			request.setAttribute("copySupValue", rs.getRetVal());
        		}
        		HashMap supValue=(HashMap)rs.getRetVal();
        		for(int i=0;i<copySupField.size();i++){
            		DBFieldInfoBean df=(DBFieldInfoBean)copySupField.get(i);
            		values.put(df.getFieldName(), supValue.get(df.getFieldName()));
            		if(df.getInputType()==2){
            			dbmgt.setPopDisplay(df, supValue.get(df.getFieldName()).toString(), values, lg.getSunCmpClassCode(), lg.getId(), allTables);
            		}
            	}
        	}
        }

        //�ж���Щ�ֶ���Ҫ�ۼ�������¼
        HashMap lastValues=new HashMap();
        for(int i=0;i<tableInfo.getFieldInfos().size();i++){
        	DBFieldInfoBean b=(DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
        	if(b.getFieldIdentityStr()!=null && b.getFieldIdentityStr().equals("lastValueAdd")){
        		Result rs=dbmgt.getLastValue(tableName, parentCode, b.getFieldName(),urlValue);
            	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
        			if(rs.getRetVal()!=null){
        				lastValues.put(b.getFieldName(),rs.getRetVal());
        			}
        		}
        	}
        }

        request.setAttribute("lastValues", lastValues);

        BaseCustomFunction impl = (BaseCustomFunction) BaseEnv.
                                  functionInterface.
                                  get(tableName);
        if (impl != null) {
            impl.onAddPrepare(mapping, form, request, response,
                              getLoginBean(request),
                              tableName, map);
        }
        
        ReportSetMgt mgt = new ReportSetMgt();
        String moduleType = getParameter("moduleType", request) ;
        Result rsPrint = mgt.getBillTable(tableName,moduleType);
        if (rsPrint.getRealTotal() > 0) {
            List list = (List) rsPrint.getRetVal();
            ReportsBean report = (ReportsBean) list.get(0);
            Map det = report.getReportDetBean();
            Collection con = det.values();
            Iterator iter = con.iterator();
            while (iter.hasNext()) {
                ReportsDetBean detBean = (ReportsDetBean) iter.next();
                //newFlag=OLD��ʾ����ƴ�ӡ��ʽ
                //����Ҫ�ж��Ƿ�������˺��ӡ.������������������ǲ��ܳ��ִ�ӡ��Ť��
                if (detBean.getNewFlag().equals("OLD") 
                		&& !(tableFlow!=null && tableFlow.getTemplateStatus()==1 && designFlow!=null && report.getAuditPrint()==1)) {
                    request.setAttribute("reportNumber", report.getReportNumber());
                    request.setAttribute("print", "true");
                    break;
                }
            }
        }
        
        request.setAttribute("MOID", mop.moduleId);
        //����ģ�����ȡ�ò�ѯʱ�ķ�ΧȨ��
        mop = (MOperation) (getLoginBean(request).getOperationMapKeyId().get(mop.moduleId));
        if(tableInfo.getClassFlag()==1){
	        //�õ�����������
        	moduleType = moduleType==null?"":moduleType ;
	        Result rs3 = new ReportDataMgt().getParentName(parentCode, tableInfo,getLocale(request).toString());
			if (rs3.retCode == ErrorCanst.RET_NOTSETROWMARKER) {
				request.setAttribute("parentName", "");
			} else {
				ArrayList parentName = (ArrayList) rs3.retVal;
				String parentUrl = "";
				if(parentName.size()==0){
					parentUrl +=  GlobalsTool.getMessage(request, "com.acc.ini.root");
				}else{
					parentUrl += "<a href=\"/UserFunctionQueryAction.do?tableName=" +tableName 
			  		  + "&moduleType="+moduleType+"&winCurIndex="+request.getParameter("winCurIndex")+"&operation=" +OperationConst.OP_QUERY
			  		  + "\">"  +GlobalsTool.getMessage(request, "com.acc.ini.root")
					  + "</a> >> ";
					for (int i = 0; i < parentName.size(); i++) {
						String[] nameClass = (String[]) parentName.get(i);
						
						parentUrl += "<a href=\"/UserFunctionQueryAction.do?tableName=" +tableName 
									  + "&moduleType="+moduleType
									  + "&winCurIndex="+request.getParameter("winCurIndex")
							  		  + "&operation=" +OperationConst.OP_QUERY
							  		  + "&parentCode=" + nameClass[1]
							  		  + "\">" + nameClass[0]
							  		  + "</a> >> ";
					}
				}
				System.out.println("parentUrl:"+parentUrl);
				request.setAttribute("parentName", parentUrl);
			}
        }
        request.setAttribute("scopeRight",scopeRight);
        request.setAttribute("tableName", tableName);
        request.setAttribute("parentCode", parentCode);

        String butTag = parseExtendButton(request, tableinfo, "add");
        if (butTag != null && butTag.length() > 0) {
            request.setAttribute("extendButton", butTag);
        }
        if (tableinfo.getClassFlag() != 0) {
            request.setAttribute("hasClass", true);
        }
        /*��ѯ�����һ������*/
        //��ӽ����ݲ�֧����һ����һ�� 
//        HashMap hasNextMap = (HashMap) request.getSession().getAttribute("hasNextMap");
//    	if (hasNextMap != null && tableInfo.getHasNext()==1) {
//    		TreeMap<Integer, String> nextMap = (TreeMap<Integer, String>) hasNextMap.get(tableName);
//    		if(nextMap!=null && !nextMap.isEmpty()){
//    			request.setAttribute("lastBill", nextMap.get(nextMap.firstKey())) ;
//    		}
//    	}
        

        String fromCrm = getParameter("fromCRM", request) ;
        /*CRM��ϯ̨ ����¿ͻ�*/
        if("service".equals(fromCrm)){
        	request.setAttribute("fromCRM", "service") ;
        }
//        if(isOAWorkFlow && designId!=null){
//        	request.setAttribute("moduleName",BaseEnv.workFlowInfo.get(designId).getTemplateName());
//        }else{
        	request.setAttribute("moduleName",getModuleNameByLinkAddr(request, mapping));
//        }
        
        return getForward(request, mapping, "functionAdd");
        
    }

    /**
     * ���
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward add(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

    	 //=======�κ��˲���ɾ�����δ��� ������=============
        /**
         * ����������в������м��ܹ��ļ�飬
         * ���ð�Ҳ������������������м�飬
         * ���������Զ�����ɱ�����ִ�У�
         * ���Լ��ı�����Ϊ�ٷ�֮һ
         * 
         */
    	if ((System.currentTimeMillis() - com.menyi.web.util.SystemState.uCheckTime ) > 1*24*60*60000) {
        	com.menyi.web.util.SystemState.uCheckTime = System.currentTimeMillis();
        	System.out.println("UserFunctionAction check Dog----------------------");
            if (com.menyi.web.util.SystemState.instance.encryptionType ==
                com.menyi.web.util.SystemState.ENCRYPTION_DOG1) {
            	 //ִ�м��
                //���ģ��
                for (Object o :
                     com.menyi.web.util.SystemState.instance.getModuleList()) {
                    int module = Integer.parseInt(o.toString());
                    if (1 == module) {
                    	 //��������
                        //DOG_CHECK_JXC_START
                        //DOG_CHECK_JXC_END
                    } else if (2 == module) {
                    	 //���OA
                        //DOG_CHECK_OA_START
                        //DOG_CHECK_OA_END
                    } else if (3 == module) {
                        //CRM
                        //DOG_CHECK_CRM_START
                        //DOG_CHECK_CRM_END
                    } else if (4 == module) {
                        //HR
                        //DOG_CHECK_HR_START
                        //DOG_CHECK_HR_END
                    }
                }
                //����û�
                //�ȸ����û���ȷ�������ĵ�
                //DOG_CHECK_USER_START

                //DOG_CHECK_USER_END
                //�������
                //DOG_CHECK_LANGUAGE_START

                //DOG_CHECK_LANGUAGE_END
                //����Զ��幦��

                //DOG_CHECK_FUNCTION_START

                //DOG_CHECK_FUNCTION_END

                //���ﻹ�������û������Զ��幦�����Ƿ񳬱�
                

                //����Զ���ģ���Ƿ񱻷Ƿ�׫�ģ�������ݿ���ģ�����Ƿ񳬹���׼,�����Ʋ��ñȽ�
                //DOG_CHECK_REL_FUNCTION_START

                //DOG_CHECK_REL_FUNCTION_END
            } else if (com.menyi.web.util.SystemState.instance.encryptionType ==
                       com.menyi.web.util.SystemState.ENCRYPTION_EVAL) {
            	 //�������
                //������ģ��ļ��
                //��ѯ�ɹ������ۣ�����ģ����ʱ��
            	int er = new InitMenData().checkEnv();
                if (2==er) {
                    SystemState.instance.dogState = SystemState.DOG_ERROR_ENV_BIS;
                    return getForward(request, mapping, "fatalAlert"); 
                }
            } else if(com.menyi.web.util.SystemState.instance.encryptionType ==
                com.menyi.web.util.SystemState.ENCRYPTION_SOFT){
            	File file = new File("aio.cert");
                if(!file.exists()){
                	com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
                }
            	//���ļ�
            	byte[] bs = null;
            	try {
    				FileInputStream fis  = new FileInputStream(file);
    				int len = fis.available();
    				bs = new byte[len];
    				fis.read(bs);
    			} catch (Exception e) {
    				//���ܹ�������ܴ���
    				com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
    			}
    			//�����ں͹�˾����
    			byte[] tempBs = new byte[bs.length - 128];
    			System.arraycopy(bs, 128, tempBs, 0, tempBs.length);
    			//����
    			try{
    				//����Կ
    			    FileInputStream fisKey=new FileInputStream("../../website/WEB-INF/private1024.key");  
    				ObjectInputStream oisKey =new ObjectInputStream(fisKey);  
    			    Key key=(Key)oisKey.readObject();  
    			    oisKey.close();  
    			    fisKey.close();  
    			    
    				Cipher cipher =Cipher.getInstance("RSA");  
    				  
    			    //��˽Կ����  
    			    cipher.init(Cipher.DECRYPT_MODE, key);  
    			    bs=cipher.doFinal(tempBs); 
    			    //�����к�
    			    tempBs = new byte[30];
    			    System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
    				SystemState.instance.dogId = new String(tempBs).trim();
    				//֤��汾��Ԥ��
    				//��������
    			    tempBs = new byte[32];
    			    System.arraycopy(bs, 32, tempBs, 0, tempBs.length);
    				SystemState.instance.pcNo = new String(tempBs).trim();
    		        //ȡ������
    		        Random rd = new Random();
    		        rd.setSeed(System.currentTimeMillis());
    		        int rdi = rd.nextInt(4000);        
    		        String s =CallSoftDll.get(rdi+"");        
    		        byte[] mb= new byte[16] ;
    		        mb =SecurityLock2.hexStringToBytes(s);    	
    		    	rdi +=5;
    		    	for(int i=0;i<mb.length ;i++){    		
    		    		mb[i] = (byte)(mb[i]-rdi);
    		    		if(i>8){
    		    			mb[i] =(byte)(mb[i] -2);
    		    		}
    		    	}    	  	
    		        //У�������
    		    	String realPcNo = SecurityLock2.bytesToHexString(mb);
    		        		        
    				if(SystemState.instance.pcNo== null || !SystemState.instance.pcNo.equals(realPcNo)){
    					//�����벻һ��
    					s =CallSoftDll.get("mac");
    					if(SystemState.instance.pcNo== null || !SystemState.instance.pcNo.equals(s)){				
    						com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
    					}
    					
    				}
    			} catch (Exception e) {
    				
    			}
            } else if(com.menyi.web.util.SystemState.instance.encryptionType ==
                com.menyi.web.util.SystemState.ENCRYPTION_DOG2){
            	int version  = SecurityLock.getVersion();	        	
	        	if (version == 1) {
	        		//�ڶ������ܹ�    	            	
	            	byte[] bs = null;	            	
	    				    			
	    			//����
	    			try{
	    				//����Կ
	    			    FileInputStream fisKey=new FileInputStream("../../website/WEB-INF/private1024.key");  
	    			    ObjectInputStream oisKey =new ObjectInputStream(fisKey);  
	    			    Key key=(Key)oisKey.readObject();  
	    			    oisKey.close();  
	    			    fisKey.close();  
	    			    
	    				Cipher cipher =Cipher.getInstance("RSA");  
	    				  
	    			    //��˽Կ����  
	    			    cipher.init(Cipher.DECRYPT_MODE, key);  
	    			    byte[] tempBs = SecurityLock.getData();	    			    
	    			    bs=cipher.doFinal(tempBs); 
	    			    //������
	    			    tempBs = new byte[30];
	    			    System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
	    				SystemState.instance.dogId = new String(tempBs).trim();
	    				//֤��汾��Ԥ��
	    				//��������
	    			    tempBs = new byte[32];
	    			    System.arraycopy(bs, 32, tempBs, 0, tempBs.length);
	    				SystemState.instance.pcNo = new String(tempBs).trim();
	    		        //У�������
	    		    	String[] realPcNo = SecurityLock.F_ReadKey(1);
	    		        		        
	    				if(SystemState.instance.pcNo== null || !SystemState.instance.pcNo.equalsIgnoreCase(realPcNo[0]+"-"+realPcNo[1]) ||
	    						SystemState.instance.dogId== null || !SystemState.instance.dogId.equalsIgnoreCase(SecurityLock.readKeyId()+"")){
	    					//���ܹ��Ż������벻һ��
	    					com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
	    				}
	    			} catch (Exception e) {
        				
        			}
	        	}
            }
        }

 
 


        //=======�κ��˲���ɾ�����ϴ��� ������=============

        Result rs = new Result();
        /*�������ڴ������б�ṹ����Ϣ*/
        Hashtable map = (Hashtable) request.getSession()
        								   .getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        
        LoginBean lg = getLoginBean(request) ;
        /*�����ֵܱ��f_brother*/
        String f_brother = this.getBrotherId(getParameter("f_brother", request), getParameter("hasFrame", request));
        f_brother = f_brother == null ? "" : f_brother;
        request.setAttribute("f_brother", f_brother);

        String fromPage = request.getParameter("fromPage");
        String sCompany = "";
        String tableName  = getParameter("tableName", request);
        /*�����classCode*/
        String parentCode = request.getParameter("parentCode");
        String moduleType = getParameter("moduleType", request) ;
        
        /*��ṹ��Ϣ*/
        DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
        
        HashMap values = new HashMap();
        /*ȡ��������*/
        readMainTable(values, tableInfo, request);
        
        String saveType = request.getParameter("button");

        /*fjj�滻���ݱ����������һ��*/
		if (("quoteDraft".equals(saveType) || "saveDraft".equals(saveType)) && values.get("id") != null && values.get("id").toString().length() > 0) {

		} else {
			//ԭ���ɵ��ݱ�Ŵ���
		}
        
        
        /*��ȡ�ӱ�������*/
        List childTableList = DDLOperation.getChildTables(tableName, map);
        if (childTableList != null && childTableList.size() > 0) {
            for (int i = 0; i < childTableList.size(); i++) {
                DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
                readChildTable(values, childTable, request, lg.getSunCmpClassCode());
            }
        }
        
        /*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
        MOperation mop = GlobalsTool.getMOperationMap(request);
        OAWorkFlowTemplate workFlow = null;
        String designId = getParameter("designId", request) ;
        if(mop!=null&&mop.oaWorkFlow){
        	BaseEnv.log.debug("��ӵ���desingId��Ϊ�գ�"+designId);
        	if(designId!=null && designId.length()>0){
        		workFlow = BaseEnv.workFlowInfo.get(designId) ;
        	}else{
        		workFlow = BaseEnv.workFlowInfo.get(mop.getModuleId()) ;
        	}
        }else{
        	workFlow=BaseEnv.workFlowInfo.get(tableName) ; 
        }
        BaseEnv.log.debug("��ӵ���desingId="+designId+(workFlow==null?"������workflowΪ��":"������designId="+workFlow.getId()));
        //System.out.println("*****workFlow********"+workFlow);
        /*��ʼ��һЩ�ֶεĻ�����Ϣ*/
        Result rs_initDBField = userMgt.initDBFieldInfo(tableInfo, lg, values, 
        												parentCode, workFlow) ;
        
        if(rs_initDBField.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL){
        	/*����¼����ǰ����*/
        	EchoMessage.error().add(getMessage(request,"com.currentaccbefbill"))
        					   .setAlertRequest(request);
        	return getForward(request, mapping, "alert");
        }else if(rs_initDBField.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC){
        	/*�������ڵ��ڼ��ڻ���ڼ��в�����*/
        	EchoMessage.error().add(rs_initDBField.retCode, request).setAlertRequest(request);
        	return getForward(request, mapping, "alert") ;
        }
        /*���淽ʽ*/
      
        if("saveDraft".equals(saveType)){
        	values.put("workFlowNodeName", "draft") ;
        }else if("printSave".equals(saveType)){
        	values.put("workFlowNodeName", "print") ;
        	request.setAttribute("saveType", saveType);
        }
        //��ȡ·��
        String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
        //Ҫִ�е�define����Ϣ
        String defineInfo=request.getParameter("defineInfo");
        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		String deliverTo=request.getParameter("deliverTo");
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		
		//zxy ��midCalculate д��value�У����� ��define����@valueOfDB:midCalculate=true���жϵ�ǰ����Ϊ�м�����
		boolean isMidCalculate = false;
		if("midCalculate".equals(saveType)){ 
			saveType="saveAdd";
			isMidCalculate = true;
			values.put("midCalculate", "true");
		}
				
		
		rs = dbmgt.add(tableInfo.getTableName(), map, values,lg.getId(), path,defineInfo,
				resources,this.getLocale(request),saveType,getLoginBean(request),workFlow,deliverTo,props);
		
		String kid = values.get("id")==null?"":values.get("id").toString();
        request.setAttribute("BillId", kid) ;
        ActionForward forward = getForward(request, mapping, "alert");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
        	
        	String[] returnValue = (String[]) rs.retVal ;
        	/*��ǰ����������*/
        	String locale = getLocale(request).toString() ;
        	String addMessage = this.getMessage(request, "common.lb.add") ;
        	if("saveDraft".equals(saveType)){
        		addMessage = getMessage(request, "common.quote.draft") ;
        	}
        	/*���ϵͳ��־*/
        	int operation=0;
        	if("saveDraft".equals(saveType))operation=5;
        	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		//����û��ȡ��ģ����
        		String billTypeName= getModuleNameByLinkAddr(request, mapping);
        		dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"",billTypeName);
        		       
        		//******�ж��Ƿ�������Ϣ���ֻ���*******//
        		SystemSettingBean sys = BaseEnv.systemSet.get("MobileApi");
        		if(sys != null){
	        		String mUrl= BaseEnv.systemSet.get("MobileApi").getSetting();
	        		if(!"".equals(mUrl)){
	        			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	        			Map m = new HashMap();
	        			m.put("tableName", tableInfo.getTableName());
	        			m.put("keyId", values.get("id"));
	        			m.put("type", "1");
	        			m.put("createtime", values.get("createTime"));
	        			mUrl += "?tableName="+tableInfo.getTableName();
	        			mUrl += "&keyId="+values.get("id");
	        			mUrl += "&type=1";
	        			//mUrl += "&createtime="+values.get("createTime");
	        			String r = com.koron.wechat.common.util.HttpRequest.post(mUrl, gson.toJson(m));
        		}
        		}
        		//************end**********//
        	}
        	//ͬ����֯�ܹ��� ��ʱͨѶ�ͻ���
        	if("tblDepartment".equals(tableName)){
        		 MSGConnectCenter.refreshDeptInfo(String.valueOf(values.get("classCode")));
        	}
        	
        	/*�����ǰ�������� ������չ��ť��������Ϣ����*/
        	if(!"saveDraft".equals(saveType) && !"printSave".equals(saveType)){
	        	String smsType = request.getParameter("smsType") ;
	        	if(smsType!=null){
	        		userMgt.sendWakeUpMessage(request, values, lg, smsType,tableInfo,locale) ;
	        	}else{
	        		Result smsResult = userMgt.querySMSModelInfo(tableName, "add") ;
	                if(smsResult!=null && smsResult.retVal!=null){
	                	String[] sms = (String[]) smsResult.retVal ;
	                	userMgt.autoSendMessage(values, lg, tableInfo, locale, sms,"add") ;
	                }
	        	}
	        	
	        	if(1 == tableInfo.getWakeUp()&&request.getParameterValues("alertType")!=null&&request.getParameterValues("alertType").length>0){
	        		userMgt.setAlert(request, kid, lg, tableInfo) ;
	        	}
        	}
        	 /*ɾ���ڴ�ͼƬ*/
            ArrayList listPic = (ArrayList) request.getSession().getAttribute("PIC_UPLOAD");
            ArrayList listAffix = (ArrayList) request.getSession().getAttribute( "AFFIX_UPLOAD");
            for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
                DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
                if (fi.getFieldType() == DBFieldInfoBean.FIELD_PIC){
                	userMgt.deleteTempImage(request.getParameterValues(fi.getFieldName()), listPic) ;
                }else if (fi.getFieldType() == DBFieldInfoBean.FIELD_AFFIX){
                	userMgt.deleteTempImage(request.getParameterValues(fi.getFieldName()),listAffix) ;
                }
            }
                
            
            
            for(DBFieldInfoBean dbField : tableInfo.getFieldInfos()){
            	if("RowMarker".equals(dbField.getFieldSysType())){
            		request.setAttribute("keySearch", getParameter(dbField.getFieldName(), request)) ;
            		break ;
            	}
            }
            //��ӳɹ�
            if (draftFlag != TEXT) {  //����Ǵ�Ϊ�ݸ�ʱ������־��Ϊ�ݸ�״̬
                userMgt.updateCheckInfo(tableName, returnValue[0],UserFunctionMgt.DRAFT);
                //����״̬�޸Ľű� 
                TestRW.saveToSql(path, UserFunctionMgt.strStatu);
            }
            /*��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��*/
        	String parentTableName=request.getParameter("parentTableName");
            parentTableName=parentTableName==null?"":parentTableName;
            
            String message = getMessage(request, "common.msg.addSuccess") ; 
            if(returnValue[1]!=null && returnValue.length>0){
            	if(rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
            		message = dbmgt.getDefSQLMsg(getLocale(request).toString(), returnValue[1]) ;
            	}else{
            		message += "<br>"+getMessage(request, "userfunction.msg.billNoExistNew")+":"+returnValue[1] ;
            	}
            }
            EchoMessage echo = null ;
            //������ӡ����
        	String savePrint = BaseEnv.systemSet.get("savePrint").getSetting() ;
         	if(("true".equals(savePrint)||"printSave".equals(saveType)) && mop!=null && mop.print()){
                Result print = new ReportSetMgt().getBillTable(tableName,moduleType);
                if(print.retCode==ErrorCanst.DEFAULT_SUCCESS){
                	if(print.getRealTotal()>0){
                		List list = (List) print.getRetVal();
                        ReportsBean report = (ReportsBean) list.get(0);
                        Map det = report.getReportDetBean();
                        Collection con = det.values();
                        Iterator iter = con.iterator();
                        while (iter.hasNext()) {
                            ReportsDetBean detBean = (ReportsDetBean) iter.next();
                            if (detBean.getNewFlag().equals("OLD")) {
                                request.setAttribute("print", "true"); 
                                break;
                            }
                        }
                    	request.setAttribute("BillRepNumber", report.getReportNumber()) ;
                	}
                	
                }
         	}
            if (saveType != null && saveType.equals("saveAdd") && isMidCalculate) {
            	//�м����㣬ֱ�ӷ��ص��޸Ľ���
            	//&moduleType=&f_brother=&operation=7&winCurIndex=4&pageNo=&parentCode=&parentTableName=&saveDraft=
            	echo = EchoMessage.success().add(this.getMessage(request, "com.auto.succeed"))
                        			 .setBackUrl("/UserFunctionAction.do?tableName=" +tableName +"&keyId="+values.get("id")
                        				+ "&operation=" + OperationConst.OP_UPDATE_PREPARE + "&parentCode=" +String.valueOf(values.get("classCode")) 
                        				+ "&f_brother=" + f_brother+"&parentTableName="+parentTableName
                        				+ "&moduleType="+moduleType+"&winCurIndex="+request.getParameter("winCurIndex")+
                        				"&fresh="+request.getParameter("fresh")+"&popWinName="+request.getParameter("popWinName"));
            	
            }else if (saveType != null && saveType.equals("saveAdd")) {
            	echo = EchoMessage.success().add(message).setBackUrl("/UserFunctionQueryAction.do?tableName=" +tableName 
   				+ "&operation=" + OperationConst.OP_ADD_PREPARE + "&parentCode=" + parentCode 
   				+ "&f_brother=" + f_brother+"&parentTableName="+parentTableName
   				+ "&moduleType="+moduleType+"&winCurIndex="+request.getParameter("winCurIndex")+
   				"&fresh="+request.getParameter("fresh")+"&popWinName="+request.getParameter("popWinName"));
            }else if(saveType !=null && "copyAdd".equals(saveType)){
            	echo = EchoMessage.success().add(message)
                        			 .setBackUrl("/UserFunctionQueryAction.do?tableName=" + tableName 
                        				+ "&operation=" +OperationConst.OP_COPY 
                        				+ "&keyId=" + kid + "&parentCode=" +parentCode + "&f_brother=" + f_brother
                        				+ "&parentTableName="+parentTableName
                        				+ "&moduleType="+moduleType+"&winCurIndex="+request.getParameter("winCurIndex")+
                        				"&fresh="+request.getParameter("fresh")+"&popWinName="+request.getParameter("popWinName"));
            }else {                
             	String planType = getParameter("planType", request) ;
        		echo = EchoMessage.success().add(message)
   			 			.setBackUrl("/UserFunctionQueryAction.do?tableName=" + tableName 
   			 					+ "&operation=" +OperationConst.OP_DETAIL +"&keyId="+kid
   			 					+ "&parentCode=" + String.valueOf(values.get("classCode")) + "&f_brother=" + f_brother 
   			 					+ "&checkTab=Y&parentTableName="+parentTableName
   			 					+ "&moduleType="+moduleType+"&winCurIndex="+request.getParameter("winCurIndex")+
   			 					"&fresh="+request.getParameter("fresh")+"&popWinName="+request.getParameter("popWinName"));
            }
            
            if(echo!=null){
            	if(returnValue[1]!=null && returnValue.length>0){
            		echo = echo.setNotAutoBack() ;
            	}
            	if("HRReview".equals(tableName)){
            		forward = getForward(request, mapping, "commonMessage");
            		echo.setRequest(request) ;
            	}else{
            		echo.setAlertRequest(request) ;
            	}
            }            
            
            /*ִ�����������*/
            if (!"printSave".equals(saveType)&& !isMidCalculate 
            					&& null != workFlow&&1==workFlow.getTemplateStatus()) {
            	request.setAttribute("isOAWorkFlow", true);
            	String  retUrl="/UserFunctionQueryAction.do?tableName=" + tableName+ "&operation=" + ("true".equals(deliverTo)?OperationConst.OP_DETAIL:OperationConst.OP_UPDATE_PREPARE)
              			+ "&parentCode=" + parentCode + "&f_brother=" + f_brother + "&keyId="+kid
              			+ "&moduleType="+moduleType+"&checkTab=Y&parentTableName="+parentTableName+ "&winCurIndex="+request.getParameter("winCurIndex");
            
	            if("true".equals(deliverTo)){
	            	request.setAttribute("directJump", true);
	           		request.setAttribute("retValUrl", retUrl);
	           		request.setAttribute("from", "add");
	           		EchoMessage.success().add(message)
							.setBackUrl("/OAMyWorkFlow.do?keyId="+values.get("id")+"&currNodeId=0&nextNodeIds="+("noNode".equals(returnValue[2])?"":returnValue[2])+"&department="+
							this.getLoginBean(request).getDepartCode()+"&tableName="+tableName+"&designId="+workFlow.getId()+"&operation="+OperationConst.OP_AUDITING_PREPARE).setAlertRequest(request) ;       			
	            }else{ //���������������δת������ݸ�
	            	EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/UserFunctionQueryAction.do?tableName=" + tableName 
   			 					+ "&operation=" +OperationConst.OP_DETAIL +"&keyId="+kid
   			 					+ "&parentCode=" + String.valueOf(values.get("classCode")) + "&f_brother=" + f_brother 
   			 					+ "&checkTab=Y&parentTableName="+parentTableName
   			 					+ "&moduleType="+moduleType+"&winCurIndex="+request.getParameter("winCurIndex")+
   			 					"&fresh="+request.getParameter("fresh")+"&popWinName="+request.getParameter("popWinName")).setAlertRequest(request);
	            }
            }
        } else {
        	String picPath = GlobalsTool.getSysSetting("picPath");
        	if(picPath==null || picPath.length()==0){
	        	 //ɾ����ʽ�ļ�
	        	for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
	                DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
	                if (fi.getFieldType() == DBFieldInfoBean.FIELD_PIC){
	                	String upload[] = request.getParameterValues(fi.getFieldName());
	                	for (int j = 0; upload != null && j< upload.length;j++) {
	                        FileHandler.delete(tableName, FileHandler.TYPE_PIC,upload[j]);
	                    }
	                }else if (fi.getFieldType() == DBFieldInfoBean.FIELD_AFFIX){
	                	String upload[] = request.getParameterValues(fi.getFieldName());
	                	for (int j = 0; upload != null && j< upload.length;j++) {
	                        FileHandler.delete(tableName, FileHandler.TYPE_AFFIX,upload[j]);
	                    }
	                }
	            }
        	}
        	
            
           if(rs.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
            	// �Զ���������Ҫ�û�ȷ��
            	ConfirmBean confirm=(ConfirmBean)rs.getRetVal();
            	String content=dbmgt.getDefSQLMsg(getLocale(request).toString(),confirm.getMsgContent());
            	String jsConfirmYes="";
            	String jsConfirmNo="";
            	String saveAdd = request.getParameter("button");
                if (saveAdd != null && saveAdd.equals("saveAdd")) { //��Ȼ��������������֮ǰ�ı��淽ʽ
                	jsConfirmYes="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                				+":"+confirm.getYesDefine()+";';this.parent.subAdd();";
                	if(confirm.getNoDefine().length()>0){
                		jsConfirmNo="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                				   +":"+confirm.getNoDefine()+";';this.parent.subAdd();";
                	}
                }else{
                	jsConfirmYes="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                			   + ":"+confirm.getYesDefine()+";'; if(this.parent.beforSubmit(this.parent.document.form)){ " 
                			   + "this.parent.document.form.operation.value="+OperationConst.OP_ADD+";this.parent.document.form.submit();}";
                	if(confirm.getNoDefine().length()>0){
                		jsConfirmNo="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                				  + ":"+confirm.getNoDefine()+";'; if(this.parent.beforSubmit(this.parent.document.form)){ " 
                				  + "this.parent.document.form.operation.value="+OperationConst.OP_ADD+";this.parent.document.form.submit();}";
                	}
                }
            	EchoMessage.confirm().add(content).setJsConfirmYes(jsConfirmYes)
            						 .setJsConfirmNo(jsConfirmNo).setAlertRequest(request);

            }else{
            	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), saveType);
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
				}
            }
        }
        return forward ;
    }

    


    /**
     * ȡ�ֶε���ʾ��
     * @param fieldName String tableName.fieldName
     * @return String
     */
    public static DBFieldInfoBean getField(Hashtable allTables,
                                           String fieldName) {
        if (fieldName == null || fieldName.trim().length() == 0) {
            return null;
        }
        String table = fieldName.substring(0, fieldName.indexOf("."));
        String field = fieldName.substring(fieldName.indexOf(".") + 1);

        DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
        if (tableInfo == null) {
            return null;
        }
        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
            DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.
                                        getFieldInfos().get(i);
            if (fieldInfo.getFieldName().equals(field)) {
                return fieldInfo;
            }
        }
        return null;
    }


    /**
     * �޸�ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward updatePrepare(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response,String inputkeyId) throws Exception {
    	
    	LoginBean lg = (LoginBean)request.getSession().getAttribute("LoginBean");
    	//���ݶര����ģ�����ӵ�ַ���Ȩ��
        MOperation mop = GlobalsTool.getMOperationMap(request) ;
        if(mop == null){
        	//������ģ���޷���Ȩ��
        	request.setAttribute("noback", true) ;
        	request.setAttribute("updateNoRight", "true");
            ActionForward forward = getForward(request, mapping, "message");
            EchoMessage.error().add(getMessage(request,"common.msg.RET_NO_RIGHT_ERROR")).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
            return forward;
        }
        boolean isDetail = request.getAttribute("detail") != null;  //���޸Ļ�������
               
        String tableName = getParameter("tableName", request);
        Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
        request.setAttribute("reAudit", tableInfo.getReAudit());
        String parentTableName = getParameter("parentTableName",request);
        parentTableName=parentTableName==null?"":parentTableName;
        request.setAttribute("parentTableName", parentTableName);
        boolean CannotOper=false;
        
        request.setAttribute("sessionSet", BaseEnv.sessionSet.get(lg.getId()));
        
        String nstr = inputkeyId; //���ⲿ����һ��Keyʱ������Parameter��ȥȡ
        if(nstr==null ||nstr.length() ==0){
        	nstr = getParameter("keyId", request) ;
        }
        
        
        if(isDetail && !"Y".equals(request.getParameter("checkTab"))){
        	request.setAttribute("checkTab", request.getParameter("checkTab"));
        	//��鵱ǰ���Ƿ����ֵܱ����
            String checkTab = this.getParameter("checkTab", request);
            if (checkTab == null) {
                ArrayList childTabList = new ArrayList();
                childTabList = DDLOperation.getBrotherTables(tableName,BaseEnv.tableInfos);
                if (childTabList.size() > 0) {  //�Ƿ��ж���TAB
                    Result rs = new Result();
                    //rs = new TabMgt().getMainTableFirstId(tableName);
                    //request.setAttribute("firstId",rs.getRetVal().toString());

                    request.setAttribute("keyId", nstr) ;
                    String tabIndex = getParameter("tabIndex", request)==null?"":getParameter("tabIndex", request) ;
                    request.setAttribute("tabIndex", tabIndex) ;
                    request.setAttribute("tableName", tableName);
                    String urlParam="";
                    Iterator it=request.getParameterMap().keySet().iterator() ;
                    String linkType=request.getParameter("LinkType");
                    
                    while(it.hasNext()){
                    	String fieldName=it.next().toString();
                    	if(!fieldName.equals("src")&&!fieldName.equals("tableName")&&!fieldName.equals("winCurIndex")&&!fieldName.equals("operation")){
                    		String value=request.getParameter(fieldName);
                    		if (linkType != null && (linkType.equals("@URL") || linkType.equals("@URL:")) && value != null) {
                    			value =new String(value.getBytes("iso-8859-1"), "UTF-8");
            				}
                    		value=java.net.URLEncoder.encode(value,"UTF-8"); 
                   
        
                    		urlParam+="&"+fieldName+"="+value;
                    	}
                    }
                    request.setAttribute("urlParam", urlParam);
                    ReportSetMgt setMgt = new ReportSetMgt();
            		ReportsBean reportSetBean = (ReportsBean) setMgt.getReportSetInfo(tableName, this.getLocale(request).toString()).getRetVal();
            		request.setAttribute("reportName", reportSetBean ==null?tableName: reportSetBean.getReportName());
            		request.setAttribute("src", request.getParameter("src"));
                    return getForward(request, mapping, "tabIndex");
                }

            }
        }        
        
        //�����ǰ�ڼ䲻Ϊ�ڳ������ڳ������ӣ�ɾ�����޸İ�ť������ʾ
        if(!"-1".equals(((Hashtable)BaseEnv.sessionSet.get(lg.getId())).get("NowPeriod"))){
        	String moduleType=request.getParameter("moduleType");
        	if(tableName.equals("tblCompanybeginning")||tableName.equals("tblBeginStock")||(tableName.equals("tblFixedAssetAdd")&&"0".equals(moduleType))){
        		request.setAttribute("CannotOper", "true");
        		CannotOper = true;
        	}
        }
        //��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��
        if (tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
            // ����Ĳ�ѯ��ΧȨ�޼�����ϸ��ķ�ΧȨ��
        	//MOperation mopDet = (MOperation)this.getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?parentTableName="+parentTableName+"&tableName="+tableName);
            //if(mopDet!=null){
            //   	mop.queryScope.addAll(mopDet.queryScope);
            //}
            request.setAttribute("mainTableName", parentTableName);
        } else {
            request.setAttribute("mainTableName", tableName);
        }
        HashMap<String,ArrayList<String[]>> moduleTable= (HashMap)BaseEnv.ModuleField.get(mop.getModuleUrl());
        if(moduleTable!=null){
        	request.setAttribute("moduleTable", moduleTable);
        	request.setAttribute("moduleUrl", mop.getModuleUrl()) ;
        }
        ActionForward forward = null;
        //��ȡ��ǰ�޸ĵ�ҳ
        int pageNo = getParameterInt("pageNo", request);
        if(pageNo==0){
        	pageNo = 1 ;
        }
        request.setAttribute("pageNo", pageNo) ;

        request.setAttribute("fromPage", request.getParameter("fromPage"));
        //�����ֵܱ��ID
        
        
        //�����ƻ� ��Ҫ�õ��Ĳ���
        request.setAttribute("planType",getParameter("planType", request)) ;
        request.setAttribute("strType",getParameter("strType", request)) ;
        request.setAttribute("strDate",getParameter("strDate", request)) ;
        request.setAttribute("userId",getParameter("userId", request)) ;
        

        Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        //�����˷�֧����ʱ�����������ĩ����֧�����������޸Ĳ����������
        String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
        boolean openSunCompany = Boolean.parseBoolean(openValue);
        boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
        boolean isSharedTable = tableInfo.getIsSunCmpShare() == 1 ? true : false;
        //����ְԱ����ű�ʱ������ĩ����֧��������
        boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo.getTableName()) 
        		|| "tblDepartment".equalsIgnoreCase(tableInfo.getTableName()) ? true : false;
        
        request.setAttribute("isDetail", isDetail);
        if (openSunCompany && !isLastSunCompany && !isSharedTable &&
            !isEmpOrDept && !isDetail) {
            EchoMessage.error().add(getMessage(request,"common.msg.notLastSunCompany")).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
            return getForward(request, mapping, "message");
        }


        //�����ѯ�Ĳ���id,��ת��keyId
        String keyName = request.getParameter("keyName");
        if(keyName != null && !keyName.equals("")){
        	Result rk = userMgt.getDataId(tableName,keyName,nstr);
        	if(rk.retCode== ErrorCanst.DEFAULT_SUCCESS && rk.retVal!= null){
        		nstr = (String)(((Object[])(((ArrayList)rk.retVal).get(0)))[0]);
        	}
        }
        //�����ѯ�Ĳ���keyId,����classCode,��ת��keyId
        String classCode = request.getParameter("classCode");
        if(classCode != null && !classCode.equals("")){
        	Result rk = userMgt.getDataId(tableName,"classCode",classCode);
        	if(rk.retCode== ErrorCanst.DEFAULT_SUCCESS && rk.retVal!= null && ((ArrayList)rk.retVal).size() >0 ){
        		nstr = (String)(((Object[])(((ArrayList)rk.retVal).get(0)))[0]);
        	}
        }
        request.setAttribute("keyId", nstr);
        OAMyWorkFlowMgt oamgt=new OAMyWorkFlowMgt();
        HashMap flowMap = oamgt.getOAMyWorkFlowInfo(nstr,tableName);
        //�޸�ʱӦ��ȡԭ������ �汾
        
        
        boolean isOAWorkFlow=false;
        Object copy = request.getAttribute("copy");
        if (nstr != null && nstr.length() != 0) {
            String keyId = nstr;
            if(copy!=null&&copy.toString().equals("copy")&&keyId.contains(";hasChild")){
            	EchoMessage.error().add(getMessage(request,
                "goods.parent.uncopy")).setClosePopWin(request.getParameter("popWinName")).
				setRequest(request);
				return getForward(request, mapping, "message");
            }
            //ִ�м�����ϸ
            Hashtable map = (Hashtable) request.getSession().getServletContext().
                            getAttribute(BaseEnv.TABLE_INFO);

            String sunClassCode = lg.getSunCmpClassCode();  //��LoginBean��ȡ����֧������classCode
            Hashtable props = (Hashtable) request.getSession().getServletContext().
            getAttribute(BaseEnv.PROP_INFO);
            
            String oldClassCode = "";
            Result rs = dbmgt.detail(tableName, map, keyId, sunClassCode,props,lg.getId(),isLastSunCompany,copy);

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {

            	HashMap hm = (HashMap) rs.retVal;
            	if(hm==null || hm.get("id")==null){
            		//δ�ҵ���¼
            		request.setAttribute("noback", true) ;
                    forward = getForward(request, mapping, "message");
                    EchoMessage.error().add(getMessage(request,"common.error.nodata")).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
                    return forward;
            	}
            	HashMap values = (HashMap) rs.retVal;
            	
            	String f_brother = this.getParameter("f_brother", request); //��ǰ�Ǵ��ⲿ����f_brother�����ڸ�Ϊֱ��ȡ���ݿ���
                f_brother =values.get("f_brother")==null?"":values.get("f_brother")+"";
                request.setAttribute("f_brother", f_brother);
            	
            	OAWorkFlowTemplate template = flowMap == null?BaseEnv.workFlowInfo.get(tableName): BaseEnv.workFlowInfo.get(flowMap.get("applyType"));
            	if(template!=null && template.getTemplateStatus()==1 && flowMap != null){
            		request.setAttribute("currentNode", flowMap.get("currentNode"));
            		request.setAttribute("currentNodeName", flowMap.get("currentNodeName"));
            	}else{
            		request.setAttribute("currentNode", "");
            	}
            	
            	if(template!=null && template.getTemplateStatus()==1 && 7==getParameterInt("operation", request)){
                	if(flowMap!=null && !"0".equals(String.valueOf(flowMap.get("currentNode")))){
        	        	String flowId = String.valueOf(flowMap.get("applyType"));
        	        	String checkPerson = String.valueOf(flowMap.get("checkPerson"));
        	        	/*�鿴��ǰ���������û��ί�и��ҵ�*/
        	        	if(checkPerson!=null){
        		        	HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(lg.getId(), flowId);
        		        	for (String person : checkPerson.split(";")) {
        						if (consignMap.get(person) != null) {
        							checkPerson += lg.getId() + ";";
        						}
        					}
        	        	}
        	        	if(checkPerson!=null && !checkPerson.contains(";"+getLoginBean(request).getId()+";")){
        	        		String curIndex = request.getParameter("winCurIndex") ; 
        	        		String detailUrl = "/UserFunctionAction.do?tableName="+tableName+"&operation=5&f_brother="+f_brother
        	        						 + "&parentTableName="+parentTableName
        	        						 + "&checkTab="+request.getParameter("checkTab")
        	        						 + "&winCurIndex="+("".equals(curIndex)?"&LinkType=@URL:&noBack=true":curIndex);
        	        		return new ActionForward(detailUrl);
        	        	}
                	}
                }
                boolean isPeriodBefor=false; //�Ƿ��ǻ���ڼ�ǰ������
                //����Ƿ��ǻ���ڼ�ǰ������                
                if(!"copy".equals((String)copy)){
	            	String sysParamter = tableInfo.getSysParameter();//����Ϣϵͳ����
	            	Date time=null;
	            	for(int i=0;i<tableInfo.getFieldInfos().size();i++){
	            		DBFieldInfoBean bean=(DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
	            		if(bean.getDefaultValue()!=null&&"accendnotstart".equals(bean.getFieldIdentityStr())){
	            			String timeStr=hm.get(bean.getFieldName()).toString();
	            			if(timeStr!=null){
	            				time=BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
	            			}
	            		}
	            	}
	            	int currentMonth = 0;
	            	int currentYear=0;
	                if (null != time) {
	                    currentMonth = time.getMonth() + 1;
	                    currentYear=time.getYear()+1900;
	                }
	                int periodMonth = -1;
	                int periodYear=-1;
	                AccPeriodBean accBean=(AccPeriodBean)sessionSet.get("AccPeriod");
	                if (accBean!=null) {
	                    periodMonth = accBean.getAccMonth();
	                    periodYear=accBean.getAccYear();
	
	                }
	                String workFlowNodeName = (String) hm.get("workFlowNodeName") ;
	                if ((currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) 
                					||periodMonth < 0 ) && currentMonth != 0 && !"draft".equals(workFlowNodeName)) {
                        if (null != sysParamter) {
                            if (sysParamter.equals("CurrentAccBefBill") &&(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth))) {
                            	isPeriodBefor = true ;
                            }

                            if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") &&
                            		(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) ||periodMonth < 0 )) {
                            	isPeriodBefor = true ;
                            }
                            if(isPeriodBefor && !isDetail){//����ڼ�ǰ���޸�״̬���򱨴�
                            	if("detail".equals(getParameter("fromPage", request))){
                            		String curIndex = request.getParameter("winCurIndex") ;
                            		EchoMessage.error().add(getMessage(request,"com.cantupdatebefbill"))
                            						.setBackUrl("/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyId+"&f_brother="+f_brother
                            								+ "&checkTab="+request.getParameter("checkTab")
                                      					   +"&operation=5&winCurIndex="+("".equals(curIndex)?"&LinkType=@URL:&noBack=true":curIndex))				
                            						.setClosePopWin(request.getParameter("popWinName")).setAlertRequest(request);
                            	}else{
                            		EchoMessage.error().add(getMessage(request,"com.cantupdatebefbill")).setClosePopWin(request.getParameter("popWinName")).setAlertRequest(request);
                            	}
                            	return getForward(request, mapping, "message");
                            }
                        }

                    }
            	}
            	
            	
                
                
                if(f_brother!=null && f_brother.length() >0){ //�ھӱ���ʾ������б�ʶ
                	Result rr = userMgt.brotherName(parentTableName, f_brother);
                	request.setAttribute("brotherRMName", rr.getRetVal());
                }
                
                
                //�������Щ��,���Ŵ����ĵ������޸�Ȩ��
        		String[] deptRight = dbmgt.getDeptRight(mop.getScope(MOperation.M_UPDATE), mop.getScope(MOperation.M_DELETE), this.getLoginBean(request).getAllScopeRight());
        		String updateOtherRight = deptRight[0];//�鿴���˵���Ȩ��
        		String updateDeptRight =  deptRight[1];//���Ź�ϽȨ��
        		// �������Щ��,���Ŵ����ĵ�����ɾ��Ȩ��
        		String delOtherRight =  deptRight[2];
        		String delDeptRight =  deptRight[3];   
            	String createBy = (String)values.get("createBy");
            	String employeeId=(String)values.get("EmployeeID");
            	//��������ȡdeparmentCode,���employeeId,�ٴ�createBy
            	String dept = (String)values.get("DepartmentCode");
            	if(dept==null && employeeId !=null) dept =OnlineUserInfo.getUser(employeeId)==null?null:OnlineUserInfo.getUser(employeeId).getDeptId();
            	if(dept==null) dept=OnlineUserInfo.getUser(createBy)==null?"":OnlineUserInfo.getUser(createBy).getDeptId();
            	
                boolean deleteRight = mop.delete() && !CannotOper && dbmgt.getDeleteRight(dept, delDeptRight, delOtherRight, 
                		lg.getId(), (String)values.get("createBy"), 
                		(String)values.get("EmployeeID"),(String)values.get("workFlowNode"), (String)values.get("workFlowNodeName"), template);
                request.setAttribute("deleteRight", deleteRight); 
                
                
                if(isDetail && values.get("createBy")!=null){
                	/* ��ѯ��ǰ���ݱ���ί�и��ҵĹ������� */
            		HashMap<String, String> consignMap = new HashMap<String, String>();
            		if (template != null) {
            			consignMap = DynDBManager.queryConsignation(lg.getId(), template.getId());
            		}            		
        			//������Ϣ����δ���������
        			if (tableInfo.getIsBaseInfo() == 1 && (template==null || template.getTemplateStatus()==0)) {   
        				employeeId = lg.getId();
        				dept = lg.getDepartCode();
        			}
            		
            		
                	boolean updateRight=mop.update() && !CannotOper && dbmgt.getUpdateRight(dept, updateDeptRight, updateOtherRight, lg.getId(), 
                			(String)values.get("createBy"), employeeId, (String)values.get("checkPersons"), consignMap, 
                			(String)values.get("workFlowNode"), (String)values.get("workFlowNodeName"), template);              	
	                
	                //����������жϴ��û��Ƿ����޸�Ȩ��
	                if(template!=null&&!template.getTemplateClass().equals("00002")&&template.getTemplateStatus()==1 && !"draft".equals(values.get("workFlowNodeName"))){
	                	
	                	if("-1".equals((String)values.get("workFlowNode"))){
	                		if(OnlineUserInfo.getUser(values.get("createBy").toString())!=null){
		                		boolean flag=dbmgt.isRetCheckPer(lg, template, OnlineUserInfo.getUser(values.get("createBy").toString()).getDeptId());
		                		if(!flag && values.get("DepartmentCode")!=null && !values.get("DepartmentCode").equals("")  ){
		                			flag=dbmgt.isRetCheckPer(lg, template, values.get("DepartmentCode")+"");
		                		}
		                		if(flag){//����з���ˣ���ʾ����˰�ť
		                			request.setAttribute("retCheckRight", flag);
		                		}
	                		}
	                	}else if(flowMap != null && "0".equals(String.valueOf(flowMap.get("currentNode")))){
	                		//��ʼ�������ͨȨ��һ��
	                		if(updateRight){ //���޸�Ȩ�޾������Ȩ��
	                			request.setAttribute("checkRight", true);
	                		}
	                	}else{
	                		String strCheckPersons = String.valueOf(values.get("checkPersons"));
	                		if(strCheckPersons!=null){
	    						// �߰�,ֻ�д����˿��Դ߰�,���������ǰ����˲��Լ�
	                			if(values.get("createBy").toString().equals(lg.getId()) && strCheckPersons.indexOf(";" + lg.getId() + ";") < 0){
	                				request.setAttribute("hurryTrans", true);
	                			}
		                		/*�鿴��ǰ���������û��ί�и��ҵ�*/
	                			String designId = flowMap !=null? String.valueOf(flowMap.get("applyType")) :"";
		                    	for(String person : strCheckPersons.split(";")){
		                			 if(consignMap.get(person)!=null){
		                				 strCheckPersons += lg.getId()+";";
		                			 }
		                		}
		                    	if(strCheckPersons.indexOf(";"+lg.getId()+";")>=0 
		                    			|| ("0".equals((String)values.get("workFlowNode")) && updateRight)){
		                			request.setAttribute("checkRight", true);
		                		}
		                    	if(flowMap != null && (request.getAttribute("checkRight") ==null || !(Boolean)request.getAttribute("checkRight"))){ //�б�������˰�Ť�Ͳ����ֳ��ذ�Ť�����鱣��һ��
			                    	//�ж��Ƿ���г��ذ�ť
			                    	String lastCheckPerson =  String.valueOf(flowMap.get("lastCheckPerson"));
			                    	String currentNode = String.valueOf(flowMap.get("currentNode"));
			                    	FlowNodeBean flowNode = BaseEnv.workFlowDesignBeans.get(designId).getFlowNodeMap().get(currentNode);
			                    	if(lg.getId().equals(lastCheckPerson) && flowNode!=null && flowNode.isAllowCancel()){
			                    		request.setAttribute("hasCancel", true);
			                    	}
		                    	}
		                    	
	                		}
	                	}
	                }
	                	
	                request.setAttribute("updateRight", updateRight); 
	                if(!isPeriodBefor && updateRight && (request.getAttribute("checkRight") ==null || (Boolean)request.getAttribute("checkRight")) && "true".equals(GlobalsTool.getSysSetting("defaultUpdate"))){ 
	                	isDetail = false;
	                	request.setAttribute("isDetail", isDetail);
	                	request.setAttribute("update", "update");
	                	request.setAttribute("detail", "");
	                    request.setAttribute("detailOk", "");	                    
	                }
	                
                }   
                
                //ȡ��ΧȨ�ޣ�Ҫ���������Ϊdetail����Ȩ��ʱ�Զ����޸�
                ArrayList scopeRight = new ArrayList();
                scopeRight.addAll(isDetail==true?mop.getScope(MOperation.M_QUERY):mop.getScope(MOperation.M_UPDATE));
                scopeRight.addAll(this.getLoginBean(request).getAllScopeRight());
            	
            	if("copy".equals((String)copy)){
            		oldClassCode = hm.get("classCode")+"";
            	}
            	
                /*�ݸ�*/
                String draft = (String)hm.get("workFlowNodeName") ;
                request.setAttribute("saveDraft", draft) ;
                
                
            	HashMap moreLan=(HashMap)hm.get("LANGUAGEQUERY");
            	
            	 //Ѱ���ӱ�
            	ArrayList rowsList = new ArrayList();
                ArrayList childTableList = DDLOperation.getChildTables(tableName, map);

                // �����������Զ�������
                String allTableName = tableName+ "," ;
                ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
                ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;//�����������
                Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;//�ӱ��������
                Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
        												request.getSession().getServletContext().getAttribute("userSettingColConfig") ;

                if(userColConfig!=null && userColConfig.size()>0){
                	configList = userColConfig.get(tableName+"bill") ;
                	if(configList!=null){
                		allConfigList.addAll(configList) ;
                	}
                }


                for(int i=0;i<childTableList.size();i++){
                	DBTableInfoBean childTableInfo=(DBTableInfoBean)childTableList.get(i);
                	allTableName += childTableInfo.getTableName()+"," ;
                	//������ϸ���Զ����е���ʾ
                	if(allConfigList!=null && allConfigList.size()>0){
                		ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName()+"bill") ;
                    	if(childConfigList!=null){
                    		//�������ģ���ֶ����ã���ô�������ֶ��н���������Щ�ֶΣ�ҳ�潫����������
                    		if(moduleTable!=null&&moduleTable.get(childTableInfo.getTableName())!=null){
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
                    			allConfigList.addAll(cols) ;
        	            		childTableConfigList.put(childTableInfo.getTableName(), cols) ;
                    		}else{
	                    		allConfigList.addAll(childConfigList) ;
	                    		childTableConfigList.put(childTableInfo.getTableName(), childConfigList) ;
                    		}
                    	}
                	}
                }
                
                
            	Collections.sort(childTableList, new SortDBTable()) ;
            	request.setAttribute("childTableList", childTableList) ;
                
                request.setAttribute("allConfigList", allConfigList) ;
                request.setAttribute("allTableName", allTableName) ;
                request.setAttribute("childTableConfigList", childTableConfigList) ;
                HashMap tblChildMap=new HashMap();
                for(int i=0;i<childTableList.size();i++){
                	tblChildMap.put(((DBTableInfoBean)childTableList.get(i)).getTableName(),String.valueOf(i));
                	tblChildMap.put(String.valueOf(i),((DBTableInfoBean)childTableList.get(i)).getTableName());
                }
                request.setAttribute("tblChildMap", tblChildMap);
                if(template!=null && template.getTemplateStatus()==1){
                	request.setAttribute("OAWorkFlow", true);
                }
                String userLastNode=""; //�û���ǰ���һ�������Ϣ
                //���ú͹����������Ϣ
                if( template!=null && template.getTemplateStatus()==1 && flowMap!=null){
                	String currNodeId = "";
                	String designId = "";
                	if(flowMap!=null){
                		currNodeId = String.valueOf(flowMap.get("currentNode"));
                		designId = String.valueOf(flowMap.get("applyType"));
                	}else{
                		currNodeId="-1";
                	}
                	
                	isOAWorkFlow=true;
                	WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(designId);
                	if(designBean == null){
                		EchoMessage.error().add("������"+designId+"������").setClosePopWin(request.getParameter("popWinName")).setRequest(request);
                        return getForward(request, mapping, "message");
                	}
                	
                	if(isDetail){
                		Result rst=oamgt.getUserLastNode(keyId,lg.getId(),tableName);
                		if(rst.retCode==ErrorCanst.DEFAULT_SUCCESS&&rst.retVal!=null){
                			userLastNode=rst.retVal.toString();
                		}
                		if(userLastNode==null || userLastNode.length()==0){
                			userLastNode = currNodeId ;
                		} 
                		if(designBean.getFlowNodeMap().get(userLastNode)==null){
                			userLastNode = currNodeId;
                		}
                	}else{
                		userLastNode=currNodeId;
                	}
                	
                	if((f_brother==null|| f_brother.length()==0) && !"HRReview".equals(tableName)){
                		request.setAttribute("OAWorkFlow", "true");
                	}
                	if(template.getTemplateClass().indexOf("00002")==0){
                     	request.setAttribute("workFlow", "OA");
                    }
                	
                	request.setAttribute("designId", designId);
                	request.setAttribute("currNodeId",currNodeId);
                	
                	if(designBean.getFlowNodeMap().get(userLastNode)!=null){
	                	String pageCurrNode=request.getParameter("currNodeId");//�ӽ����ϴ���ĵ�ǰ�ڵ�
	                	if(!isDetail&&pageCurrNode!=null&&pageCurrNode.length()>0&&!currNodeId.equals(pageCurrNode)){//��ʵ�ʽڵ�ͽ���ڵ㲻ͬʱת���������
	                		isDetail=true;
	                	}
	                	request.setAttribute("userLastNode", userLastNode);
	                	if(!isDetail && flowMap!=null && flowMap.get("oaTimeLimitUnit")!=null&&flowMap.get("oaTimeLimitUnit").toString().length()>0){
	                		request.setAttribute("oaTimeLimitUnit", flowMap.get("oaTimeLimitUnit"));
	                		request.setAttribute("oaTimeLimit",Float.parseFloat(flowMap.get("benchmarkTime").toString()));
	                	}
	                	
	                	if(!isDetail){
	                		oamgt.transactStart(currNodeId, keyId, this.getLoginBean(request),tableName);
	                	}
	                	//�õ�ǰ���ݵ����л�ǩ���
	                	if(template!=null && template.getLines()!=null){
	                		Result rs1 = oamgt.flowDepict(designId, keyId, tableName);
	                    	request.setAttribute("flowNew", true);
	                    	if(rs1.retCode == ErrorCanst.DEFAULT_SUCCESS){
	                    		request.setAttribute("delivers", rs1.getRetVal());
	                    	}
	                	}else{
		                	Result rs1=oamgt.getDeliverance(keyId,null,tableName);
		                	if(rs1.retCode==ErrorCanst.DEFAULT_SUCCESS){
		                		request.setAttribute("delivers", rs1.getRetVal());
		                	}
		                	//�õ�ǰ���ݵ����и���
		                	rs1=oamgt.getDeliverance(keyId,"affix",tableName);
		                	if(rs1.retCode==ErrorCanst.DEFAULT_SUCCESS){
		                		request.setAttribute("affixs", rs1.getRetVal());
		                	}
	                	}
                	}
                }
                String defineInfo=request.getParameter("defineInfo");
                Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
            	MessageResources resources = null;
        		if (ob instanceof MessageResources) {
        		    resources = (MessageResources) ob;
        		}
        		
                Result addView = dbmgt.updateView(tableName, allTables, values, defineInfo, resources, this.getLocale(request), lg);
                if(addView.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
                	SaveErrorObj saveErrrorObj = dbmgt.saveError(addView, getLocale(request).toString(), "");
                	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
        				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
        			} else {
        				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
        			}
                    return getForward(request, mapping, "message");
                }
                ArrayList<String> defineField = (ArrayList<String>)values.get("DEFINE_INPUTTYPE");
                
                //����Ȩ��,�����ã�������������ʾ��Щ�ֶ�
                ArrayList<DBFieldInfoBean> fieldList;
                HashMap childFieldMap = new HashMap();
        		try {
        			fieldList = DynDBManager.getMainFieldList(tableName, tableInfo,defineField, configList, template, flowMap, moduleTable, mop, userLastNode, isDetail, f_brother, lg, scopeRight);
        			if (childTableList != null) {
        				for(DBTableInfoBean dib :(ArrayList<DBTableInfoBean>)childTableList){
        					ArrayList<DBFieldInfoBean> dibFieldList = DynDBManager.getMainFieldList(tableName, dib,defineField, childTableConfigList.get(dib.getTableName()), template, flowMap, moduleTable, mop, userLastNode, isDetail, f_brother, lg, scopeRight);
        					childFieldMap.put(dib.getTableName(), dibFieldList);
        				}
        			}
        		} catch (Exception e) {
                	EchoMessage.error().add(e.getMessage()).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
                    return getForward(request, mapping, "message");
        		}   
        		request.setAttribute("childFieldMap", childFieldMap) ;
                request.setAttribute("fieldInfos", DynDBManager.distinctList(fieldList)) ;
                request.setAttribute("fieldInfos2", tableInfo.getFieldInfos()) ;
                if (childTableList != null) {
                	Collections.sort(childTableList, new SortDBTable()) ;
                    request.setAttribute("childTableList2", childTableList);
                }
                //�����еĵ����������
            	for(int i=0;i<fieldList.size();i++){
            		DBFieldInfoBean bean=(DBFieldInfoBean)fieldList.get(i);
            		if(bean.getInputType()==bean.INPUT_MAIN_TABLE||bean.getInputTypeOld()==bean.INPUT_MAIN_TABLE){
            			if(bean.getSelectBean()!=null){
	            			for(int j=0;j<bean.getSelectBean().getViewFields().size();j++){
	            				PopField pop=(PopField)bean.getSelectBean().getViewFields().get(j);
	                        	DBFieldInfoBean bean2=GlobalsTool.getFieldBean(pop.getAsName().substring(0,pop.getAsName().indexOf(".")),pop.getAsName().substring(pop.getAsName().indexOf(".")+1));
	                        	if(bean2==null){
	                        		bean2=GlobalsTool.getFieldBean(pop.getFieldName().substring(0,pop.getFieldName().indexOf(".")),pop.getFieldName().substring(pop.getFieldName().indexOf(".")+1));
	                        	}
	                        	if(bean2!=null &&( bean2.getInputType()==bean2.INPUT_LANGUAGE||bean2.getInputTypeOld()==bean2.INPUT_LANGUAGE)){
	                        		if(moreLan.get(hm.get(pop.getAsName()))!=null){
	                        			hm.put(pop.getAsName(), ((KRLanguage)moreLan.get(hm.get(pop.getAsName()))).get(GlobalsTool.getLocale(request).toString()));
	                        		}else{
	                        			hm.put(pop.getAsName(), hm.get(pop.getAsName()));
	                        		}
	                        	}
	            			}
            			}
            		}
            	}
                //��Ϊ��˻�ͨ����ͬ�ķ�ʽʵ�֣����������չ��ť�ȣ�����ֻ��ͨ����˵�״̬���ж�
                String flowStatus=hm.get("workFlowNodeName")==null?"":hm.get("workFlowNodeName").toString();

                request.setAttribute("flowStatus", flowStatus);
                for (int k = 0;childTableList != null && k < childTableList.size();k++) {
                    DBTableInfoBean childTable = (DBTableInfoBean)childTableList.get(k);
                    ArrayList<String[]> cols = new ArrayList<String[]>();
                    ArrayList rows = new ArrayList();
                    ArrayList truncIndexs=new ArrayList();
                    if(childTableConfigList!=null && childTableConfigList.size()>0){//�û��Զ���������
                    	DBTableInfoBean tableInfoBean = (DBTableInfoBean) childTableList.get(k) ;
                    	ArrayList<ColConfigBean> cofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
                    	for(int m=0;cofingList!=null && m<cofingList.size();m++){
                    		ColConfigBean configBean = cofingList.get(m) ;
                    		GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)GlobalsTool.getPropBean(configBean.getColName());
                    		boolean isExist = false ;
                    		for (int i = 0;childTable != null &&i < childTable.getFieldInfos().size(); i++) {
    	                        DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);

    	                        String[] ss = new String[6];
    	                        if ((fi.getInputType() ==DBFieldInfoBean.INPUT_MAIN_TABLE
    	                        		|| (fi.getInputType()==6 && fi.getInputTypeOld()==2)
    	                        		|| (fi.getInputType()==8 && fi.getInputTypeOld()==2)) && fi.getSelectBean()!=null&&
    	                        		(!(gpInfo!=null&&gpInfo.getIsSequence()==1))) {

    	                        		if(fi.getSelectBean().getRelationKey()!=null && !fi.getSelectBean().getRelationKey().hidden
    	                        							&& configBean.getColName().equals(fi.getFieldName())){
    	                        			ss = new String[6];
    	         	                        Object o = fi.getDisplay().get(getLocale(request).toString());
    	         	                        setFieldString(childTable, fi, ss, o);    	
    	         	                        
    	         	                       addColExist(cols,ss);
    	         	                       
    	                        		}
    	                        		GoodsPropInfoBean tempProp=GlobalsTool.getPropBean(fi.getFieldName());
    	                        		if(!(tempProp!=null&&tempProp.getIsSequence()==1)){
    		                            for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
    		                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
    		                                if(( configBean.getColName().equals(temps.getAsName()))){
    		                                	DBFieldInfoBean f=GlobalsTool.getFieldBean(temps.getAsName().substring(0,temps.getAsName().indexOf(".")), temps.getAsName().substring(temps.getAsName().indexOf(".")+1));
    		                                	String fieldName=temps.getAsName();
    		                                	
	    		                                ss = new String[6];
	    	    		                        ss[2] = DBFieldInfoBean.getFieldTypeString(f);
	    		                                ss[3] = temps.getAsName();
	    		                                ss[4] = fieldName;
	    		                                ss[5]=temps.display;
	    		                                addColExist(cols,ss); 
	    		                                isExist=true ;
	    		                                break ;
    		                                }
    		                            }
    	                        	}
	                        		if(fi.getSelectBean().getViewFields().size()==0){
    	                        		if(configBean.getColName().equals(fi.getFieldName())){
    	    	                        	Object o = fi.getDisplay().get(getLocale(request).toString());
    	    		                        setFieldString(childTable, fi, ss, o);
    	    		                        addColExist(cols,ss);

    	    	                        	if(fi.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||fi.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE){
    	        	                        	String[] tempstr=new String[6];
    	        	                        	tempstr[0]=ss[0];
    	        	                        	tempstr[1]=ss[1];
    	        	                        	tempstr[2]="lan";
    	        	                        	tempstr[3] = fi.getFieldName();
    	        	                        	tempstr[4] = childTable.getTableName();
    	        	                        	addColExist(cols,ss);
    	    	                        	}
    	    	                        	isExist=true ;
    	    	                        	break ;
        	                        	}
    	                        	}
    	                        }else if(fi.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE&&gpInfo!=null&&gpInfo.getIsSequence()==1
    	                        		&& configBean.getColName().equals(fi.getFieldName())){
    	                        	Object o = fi.getDisplay().get(getLocale(request).toString());
         	                        setFieldString(childTable, fi, ss, o); 
    	                        	cols.add(ss);
    	                        	cols.add(ss); //����Ҫ�ǲ�������������������ʱ�����к����鲻��ʾ
 		                            truncIndexs.add(cols.size()-1);
    	                        }else{
    	                        	if(configBean.getColName().equals(fi.getFieldName())){
	    	                        	Object o = fi.getDisplay().get(getLocale(request).toString());
	    		                        setFieldString(childTable, fi, ss, o);
	    		                        cols.add(ss);

	    	                        	if(fi.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||fi.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE){
	        	                        	String[] tempstr=new String[6];
	        	                        	tempstr[0]=ss[0];
	        	                        	tempstr[1]=ss[1];
	        	                        	tempstr[2]="lan";
	        	                        	tempstr[3] = fi.getFieldName();
	        	                        	tempstr[4] = childTable.getTableName();
	        	                            cols.add(tempstr);
	    	                        	}
	    	                        	isExist=true ;
	    	                        	break ;
    	                        	}
    	                        }
    	                        if(isExist){
    	                        	break ;
    	                        }
                    	    }
                    	}
                    	//���������ֶ�ֵ
                    	for (int i = 0;childTable != null &&i < childTable.getFieldInfos().size(); i++) {
                    		DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
                    		GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)GlobalsTool.getPropBean(fi.getFieldName());
                    		if(fi.getInputType()==3){
                    			String[] ss = new String[6];
    	                        Object o = fi.getDisplay()!=null?fi.getDisplay().get(getLocale(request).toString()):null;
    	                        setFieldString(childTable, fi, ss, o);
    	                        cols.add(ss);
                    		}else if(fi.getInputType()==2 && "GoodsField".equals(fi.getFieldSysType()) 
                    									  && gpInfo!=null && gpInfo.getIsSequence()==1){
                    			boolean isExist = false;
								ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName());
								for (int m = 0; childCofingList != null && m < childCofingList.size(); m++) {
									ColConfigBean configBean = childCofingList.get(m);
									if ((fi.getFieldName().equals(configBean.getColName()))) {
										isExist = true;
										break;
									}
								}
								if (!isExist) {
									String[] ss = new String[6];
									Object o = fi.getDisplay().get(getLocale(request).toString());
									setFieldString(childTable, fi, ss, o);
									cols.add(ss);
								}
                    		}else if(fi.getInputType()==2 && fi.getSelectBean()!=null && fi.getInputValue().length()>0
                    							&& fi.getSelectBean().getViewFields().size()>0){
                    			if(fi.getSelectBean().getRelationKey()==null){
                    				BaseEnv.log.error(fi.getTableBean().getTableName()+"."+fi.getFieldName()+"������û������relationKay");
                    			}
                    			if(fi.getSelectBean().getRelationKey().hidden){
	                    			String[] ss = new String[6];
	    	                        Object o = fi.getDisplay().get(getLocale(request).toString());
	    	                        setFieldString(childTable, fi, ss, o);
	    	                        cols.add(ss);
                    			}else{
                    				boolean isExist = false ;
	                    			ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
	                    			for(int m=0;childCofingList!=null&&m<childCofingList.size();m++){
	                    				ColConfigBean configBean = childCofingList.get(m) ;
	                    				if(fi.getFieldName().equals(configBean.getColName())){
	                    					isExist = true ;
	                    					break ;
	                    				}
	                    			}
	                    			if(!isExist){
	                    				String[] ss = new String[6];
		    	                        Object o = fi.getDisplay().get(getLocale(request).toString());
		    	                        setFieldString(childTable, fi, ss, o);
		    	                        cols.add(ss);
	                    			}
                    			}
                    			for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
	                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
	                                DBFieldInfoBean f=GlobalsTool.getFieldBean(temps.getAsName().substring(0,temps.getAsName().indexOf(".")), temps.getAsName().substring(temps.getAsName().indexOf(".")+1));
	                                
	                                boolean isExist = false ;
	                    			ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
	                    			for(int m=0;childCofingList!=null&&m<childCofingList.size();m++){
	                    				ColConfigBean configBean = childCofingList.get(m) ;
	                    				if(temps.getAsName().equals(configBean.getColName())){
	                    					isExist = true ;
	                    					break ;
	                    				}
	                    			}
	                    			if(!isExist){
	                    				String[] ss = new String[6];
	    		                        ss[2] = DBFieldInfoBean.getFieldTypeString(f);
		                                ss[3] = temps.getAsName();
		                                ss[4] = temps.getFieldName();
		                                ss[5]=temps.getDisplay();
		                                cols.add(ss);
	                    			}
                    			}
                    		}else if(fi.getInputType()!=100 && !fi.getFieldName().equals("id")
                    								&& !fi.getFieldName().equals("f_ref") && !fi.getFieldName().equals("f_brother")){
                    			boolean isExist = false ;
                    			ArrayList<ColConfigBean> childCofingList = childTableConfigList.get(tableInfoBean.getTableName()) ;
                    			for(int m=0;childCofingList!=null&&m<childCofingList.size();m++){
                    				ColConfigBean configBean = childCofingList.get(m) ;
                    				if(fi.getFieldName().equals(configBean.getColName())){
                    					isExist = true ;
                    					break ;
                    				}
                    			}
                    			if(!isExist && fi.getDisplay()!=null){
                    				String[] ss = new String[6];
        	                        Object o = fi.getDisplay().get(getLocale(request).toString());
        	                        setFieldString(childTable, fi, ss, o);
        	                        cols.add(ss);
                    			}
                    		}
                    	}
                    }else{
                    	for (int i = 0;childTable != null &&i < childTable.getFieldInfos().size(); i++) {
	                        DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);

	                        if (fi.getFieldName().equals("id") ||
	                            fi.getFieldName().equals("f_ref") ||
	                            fi.getInputType() == DBFieldInfoBean.INPUT_NO) {
	                            continue;
	                        }
	                        String[] ss = new String[6];
	                        Object o=null ;
	                        if(fi.getDisplay()!=null){
	                        	o=fi.getDisplay().get(getLocale(request).toString());
	                        }
	                        setFieldString(childTable, fi, ss, o);
	                        cols.add(ss);
	                        //�ҳ������ֶ�
	                        if (fi.getInputType() ==
	                            DBFieldInfoBean.INPUT_MAIN_TABLE || (fi.getInputType() ==
	    	                            DBFieldInfoBean.INPUT_ONLY_READ && fi.getInputTypeOld() ==
	    	                            DBFieldInfoBean.INPUT_MAIN_TABLE)) {
	                        	GoodsPropInfoBean gpInfo=GlobalsTool.getPropBean(fi.getFieldName());
	                        	if(fi.getSelectBean()!=null){
	                        		if(!(gpInfo!=null&&gpInfo.getIsSequence()==1)){
		                            for (int j = 0;j < fi.getSelectBean().getViewFields().size(); j++) {
		                                PopField temps = (PopField) fi.getSelectBean().getViewFields().get(j);
		                                //if(!"true".equals(temps.hiddenInput)){
			                                DBFieldInfoBean f=GlobalsTool.getFieldBean(temps.getAsName().substring(0,temps.getAsName().indexOf(".")), temps.getAsName().substring(temps.getAsName().indexOf(".")+1));
			                                String fieldName=temps.getAsName() ;
			                                
			                                ss = new String[6];
		    		                        ss[2] = DBFieldInfoBean.getFieldTypeString(f);
			                                ss[3] = temps.getAsName();
			                                ss[4] = fieldName;
			                                ss[5]=temps.display;
			                                cols.add(ss);
		                                //}
		                             }
	                        		}
	                        	}
	                        }
	                        GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)GlobalsTool.getPropBean(fi.getFieldName());
	                        if(fi.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE&&gpInfo!=null&&gpInfo.getIsSequence()==1){
	                            cols.add(ss);
	                            truncIndexs.add(cols.size()-1);
	                        }
	                        if(fi.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||fi.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE){
	                        	String[] tempstr=new String[6];
	                        	tempstr[0]=ss[0];
	                        	tempstr[1]=ss[1];
	                        	tempstr[2]="lan";
	                        	tempstr[3] = fi.getFieldName();
	                        	tempstr[4] = childTable.getTableName();
	                            cols.add(tempstr);
	                        }

	                    }
                    }
                    if (childTable != null) {
                        List childList = (List) hm.get("TABLENAME_" +
                                childTable.getTableName());
                        for (int i = 0; i < childList.size(); i++) {
                            HashMap os = (HashMap) (childList).get(i);
                            String[] ss = new String[2];
                            ss[0] = "";
                            for (int j = 0; j < cols.size(); j++) {
                                Object sv = os.get(((String[]) cols.get(j))[3]);
                                DBFieldInfoBean bean;
                                if(((String[]) cols.get(j))[3].indexOf(".")<0){
                                	bean= GlobalsTool.getFieldBean(childTable.getTableName(),((String[]) cols.get(j))[3]);
                                }else{
                                	bean=GlobalsTool.getFieldBean(((String[]) cols.get(j))[4].substring(0,((String[]) cols.get(j))[4].indexOf(".")), ((String[]) cols.get(j))[4].substring(((String[]) cols.get(j))[4].indexOf(".")+1));
                                }
                                
                                String fieldName = ((String[]) cols.get(j))[3] ;
    	                        if(truncIndexs.contains(j)){
                                	if(sv!=null&&sv.toString().length()>0){
                                		fieldName = "Seq_hid" ;
                                		String[]seqList=sv.toString().split("~");
                                		sv=seqList[seqList.length-1];
                                	}
                                }
                                if(bean != null && ( bean.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||bean.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE)&&"lan".equals(((String[]) cols.get(j))[2])){
                                	if(moreLan.get(sv)!=null){
                                		sv=((KRLanguage)moreLan.get(sv)).toString();
                                	}
                                }else if(bean != null && (bean.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||bean.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE)){
                                	if(moreLan.get(sv) !=null){
                                		sv=((KRLanguage)moreLan.get(sv)).get(this.getLocale(request).toString());
                                	}
                                }
                                if ("float".equals(((String[]) cols.get(j))[2])) {
                                    String df = ((String[]) cols.get(j))[3];
                                    String dt = null;
                                    if (df.indexOf(".") > 0) {
                                        dt = df.substring(0, df.indexOf("."));
                                        df = df.substring(df.indexOf(".") + 1);
                                    } else {
                                        dt = ((String[]) cols.get(j))[4];
                                    }
                                    sv = (sv==null || sv.toString().length()==0) ? "0" :  sv ;
                                    if(isDetail){
                                    ss[0] = ss[0] +"\""+fieldName+"@koron@" + GlobalsTool.formatNumberS(Double.parseDouble(sv.toString()), false, false, "",dt+"."+df) + "\",";
                                    }else{     
                                    ss[0] = ss[0] +"\""+fieldName+"@koron@" + GlobalsTool.formatNumberS(Double.parseDouble(sv.toString()), false, false,"",dt+"."+df) + "\",";
                                    }
                                } else {
                                    ss[0] = ss[0] +"\""+fieldName+"@koron@" + (sv == null ? "" : sv.toString().replaceAll("\\\\", "\\\\\\\\")) + "\",";
                                }
                            }
                            ss[0] = ss[0]+"\""+""+"detsId@koron@" + os.get("id") + "\"";
                            ss[1] = os.get("id").toString();
                            rows.add(ss);
                        }
                    }
                    rowsList.add(new Object[] {childTable.getTableName(), rows});
                }

                DBTableInfoBean mainTable = DDLOperation.getTableInfo(map,
                        tableName);
                //��ƥ���۳�����������⣬�Ƿ����á��ڳ�¼������
                SystemSettingBean inIniAmt=BaseEnv.systemSet.get("InIniAmount");
                if(inIniAmt!=null&&!inIniAmt.getSetting().equals("")){//���ϵͳ�������á��ڳ�¼������
                	  boolean inIniAmount=false;
                      if(tableName.equalsIgnoreCase("tblSalesOutStock")||tableName.equalsIgnoreCase("tblOtherOut")){//���Ϊ���۳��ⵥ���������ⵥ
                         String iniAm=BaseEnv.systemSet.get("InIniAmount").getSetting();
                         if(Boolean.parseBoolean(iniAm)){
                           boolean isOn=false;
                           DBTableInfoBean temptable=DDLOperation.getTableInfo(map, tableName+"Det");
                           if(temptable!=null){
                			   for(int i=0;i<temptable.getFieldInfos().size();i++){
                				   DBFieldInfoBean tempFi=(DBFieldInfoBean)temptable.getFieldInfos().get(i);
                				   if("iniOut".equals(tempFi.getFieldIdentityStr())){
                					   if(tempFi.getInputType()==DBFieldInfoBean.INPUT_NORMAL){
                	                	   isOn=true;break;
                	                   }
                				   }
                			   }
                		   }
                           String onOff=request.getParameter("onOff");
                           if(isOn){//���ʵ��������ʾ
                        	   request.setAttribute("inIniAmountName", this.getMessage(request, "bp.outstock.inIniAmountNO"));
                        	   if(onOff!=null&&onOff.equals("onOff")){
                        		   request.setAttribute("inIniAmountName", this.getMessage(request, "bp.outstock.inIniAmount"));
                        		   Result rs1 = userMgt.updateFactQty(tableName, DBFieldInfoBean.INPUT_HIDDEN);
                        		   DBTableInfoBean tempTable=DDLOperation.getTableInfo(map, tableName+"Det");
                        		   if(tempTable!=null){
                        			   for(int i=0;i<tempTable.getFieldInfos().size();i++){
                        				   DBFieldInfoBean tempFi=(DBFieldInfoBean)tempTable.getFieldInfos().get(i);
                        				   if("iniOut".equals(tempFi.getFieldIdentityStr())){
                        				   tempFi.setInputType(DBFieldInfoBean.INPUT_HIDDEN);
                        				   }
                        			   }
                        		   }
                        	   }
                           }else{
                        	   request.setAttribute("inIniAmountName", this.getMessage(request, "bp.outstock.inIniAmount"));
                        	   if(onOff!=null&&onOff.equals("onOff")){
                        		   request.setAttribute("inIniAmountName", this.getMessage(request, "bp.outstock.inIniAmountNO"));
                        		   Result rs1 = userMgt.updateFactQty(tableName, DBFieldInfoBean.INPUT_NORMAL);
                        		   DBTableInfoBean tempTable=DDLOperation.getTableInfo(map, tableName+"Det");
                        		   if(tempTable!=null){
                        			   for(int i=0;i<tempTable.getFieldInfos().size();i++){
                        				   DBFieldInfoBean tempFi=(DBFieldInfoBean)tempTable.getFieldInfos().get(i);
                        				   if("iniOut".equals(tempFi.getFieldIdentityStr())){
                        				   tempFi.setInputType(DBFieldInfoBean.INPUT_NORMAL);
                        				   }
                        			   }
                        		   }
                        	   }
                           }
                      	   inIniAmount=true;
                         }
                      }
                      request.setAttribute("inIniAmount",inIniAmount);
                }

                String butTag = parseExtendButton(request, mainTable, "update");
                if (butTag != null && butTag.length() > 0) {
                    request.setAttribute("extendButton", butTag);
                }

                BaseCustomFunction impl = (BaseCustomFunction) BaseEnv.
                                          functionInterface.
                                          get(tableName);
                if (impl != null) {
                    impl.onUpdatePrepare(mapping, form, request, response,
                                         getLoginBean(request), keyId,
                                         tableName, map,
                                         hm);
                }
                //��ѯ�Ƿ���ͼƬ�͸���   
                if (copy != null && copy.toString().equals("copy")) {
                	 //�ڸ���ʱ�������ݱ�ŵ�Ĭ��ֵ����@CODE:���ֶ�ֵ�ĳ���һ�����ظ��ĵ��ݱ��
                	String workFlowNode = (String) values.get("workFlowNode") ;
                	if(workFlowNode!=null && workFlowNode.length()>0){
                		values.put("workFlowNode", null) ;
                	}
                    for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
                        DBFieldInfoBean field = (DBFieldInfoBean) tableInfo.
                                                getFieldInfos().get(i);
                        if (field.getDefaultValue() != null &&
                            field.getDefaultValue().contains("@CODE:")) {//Ӧ����׷�ٵ��ţ����ݱ��                       	
                        	if(field.getInputType()!=100&&field.getInputType()!=3){
                        		values.put(field.getFieldName(), field.getDefValue());
                        	}else{
                        		values.put(field.getFieldName(), "");
                        	}
                        }else if(field.getIsCopy()==0){
                        	if(field.getDefaultValue()!=null&&!field.getDefaultValue().equals("")&&(field.getFieldType()==5||field.getFieldType()==6)){
                	            if (field.getFieldType() == 5) {
                	            	values.put(field.getFieldName(),BaseDateFormat.format(new Date(),
                	                        BaseDateFormat.yyyyMMdd));
                	                if("BornDate".equals(field.getFieldName())){
                	                	field.setDefaultValue("");//Bug #10752 ����Ĭ�ϵ�ǰ���ڣ����Ϊ��
                	                }
                	            }else if (field.getFieldType() == 6) {
                	            	values.put(field.getFieldName(), BaseDateFormat.format(new Date(),
                	                        BaseDateFormat.yyyyMMddHHmmss));
                	            }
                            }else{
                            	values.put(field.getFieldName(), field.getDefaultValue());
                            }
                        }
                        //fjj���ݱ������ ���Ʋ���
						if (copy != null && "copy".equals(copy.toString())) {
							if (field.getFieldIdentityStr() != null
									&& DBFieldInfoBean.FIELD_IDENTIFIER
											.equals(field.getFieldIdentityStr())) {
								String key = "";
								String defaultValue = field.getDefaultValue();
								if (defaultValue != null && !"".equals(defaultValue)) {
									// ����Ĭ��ֵ
									key = defaultValue;
								} else {
									key = tableInfo.getTableName()+"_"+ field.getFieldName();
								}
								BillNo billno = BillNoManager.find(key);
								if(billno != null){
									if (!billno.isFillBack()) {//�����ñ������ʱ����Ҫ���ɱ��
										String valStr = BillNoManager.find(key, hm,request.getSession().getAttribute("LoginBean"));
										values.put(field.getFieldName(), valStr);
									}else{
										BillNoUnit unit  = billno.getInvers(new HashMap<String, String>(), lg);
										values.put(field.getFieldName(), unit.getValStr());
									}
								}
							}
						}
                    }
                }

                for (DBFieldInfoBean fieldTemp : fieldList) {
					if(fieldTemp.getInputType() == 7){
						Object obj = values.get(fieldTemp.getFieldName()) ;
						if(obj!=null){
							String pym = CustomizePYM.getFirstLetter(obj.toString());
							values.put(fieldTemp.getFieldName()+"PYM",pym);
						}
					}
				}

                
                if (tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
                    request.setAttribute("mainTableName", parentTableName);
                } else {
                    request.setAttribute("mainTableName", tableName);
                }
                
                request.setAttribute("MOID", mop.moduleId);
                mop = (MOperation) (getLoginBean(request).getOperationMapKeyId().get(mop.moduleId));
                String parentCode=request.getParameter("parentCode");
                if(oldClassCode  != null && oldClassCode.length() > 0 && !oldClassCode.equals("null") && copy.equals("copy")){
                	//������classCode���
                	parentCode = oldClassCode.substring(0,oldClassCode.length() -5);
                }
                if(null!=parentCode && parentCode.length()>0 && tableInfo.getClassFlag()==1){
	                // �õ�����������
	                if(copy==null||(copy!=null&&!copy.equals("copy"))){
	                	parentCode=parentCode.substring(0,parentCode.length()-5);
	                }
	                Result rs3 = new ReportDataMgt().getParentName(parentCode, tableInfo,getLocale(request).toString());
	        		if (rs3.retCode == ErrorCanst.RET_NOTSETROWMARKER) {
	        			request.setAttribute("parentName", "");
	        		} else {
	        			ArrayList parentName = (ArrayList) rs3.retVal;
	        			String parentUrl = "";
	        			parentUrl += ""  +GlobalsTool.getMessage(request, "com.acc.ini.root") + " >> ";
	        			for (int i = 0; i < parentName.size(); i++) {
	        				String[] nameClass = (String[]) parentName.get(i);
	        				parentUrl += "" + nameClass[0]  + " >> ";
	        			}	
	        			
	        			request.setAttribute("parentName", parentUrl);
	        		}
                }
                      
                 
                request.setAttribute("scopeRight",scopeRight);

                request.setAttribute("mainTable", mainTable);
                request.setAttribute("values", values);
                request.setAttribute("tableName",
                                     mainTable.getDisplay().get(getLocale(
                                             request).
                        toString()).toString());
                request.setAttribute("result", rowsList);
                request.setAttribute("tableName", tableName);                
                request.setAttribute("parentCode",parentCode);
                /*�ж��Ƿ����������*/
//        		Result result = new EMailMgt().loadAlertByEamilId(keyId) ;
//        		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
//        			request.setAttribute("alert", result.retVal) ;
//        		}
                //��ѯ�˵����Ƿ�����ƴ�ӡ��ʽ������Ѿ��������ʾ��ӡ��ť
                int auditprint=1;//Ĭ��Ϊ������˺���ܴ�ӡ
                ReportSetMgt mgt = new ReportSetMgt();
                String moduleType = getParameter("moduleType", request) ;
                Result rsPrint = mgt.getBillTable(tableName,moduleType);
                if (rsPrint.getRealTotal() > 0) {
                    List list = (List) rsPrint.getRetVal();
                    ReportsBean report = (ReportsBean) list.get(0);
                    auditprint=report.getAuditPrint();
                    Map det = report.getReportDetBean();
                    Collection con = det.values();
                    Iterator iter = con.iterator();
                    while (iter.hasNext()) {
                        ReportsDetBean detBean = (ReportsDetBean) iter.next();
//                        if (detBean.getNewFlag().equals("OLD") 
//                        		&& !(tableFlow!=null && tableFlow.getTemplateStatus()==1 && designFlow!=null && report.getAuditPrint()==1)) {
                        if (detBean.getNewFlag().equals("OLD")) {
                            request.setAttribute("reportNumber",
                                                 report.getReportNumber());
                            request.setAttribute("print", "true");
                            break;
                        }
                    }
                }
                request.setAttribute("auditprint",auditprint);
//                if(isOAWorkFlow && template!=null){
//                	request.setAttribute("moduleName",template.getTemplateName());
//                }else{
                	request.setAttribute("moduleName",getModuleNameByLinkAddr(request, mapping));
//                }
            } else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
            	 //��¼�����ڴ���
                EchoMessage.error().add(getMessage(
                        request, "common.error.nodata")).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
                return getForward(request, mapping, "message");
            } else {
            	 //����ʧ��
            	if(rs.retVal != null && !(rs.retVal instanceof HashMap)){
            		EchoMessage.error().add(rs.retVal.toString()).setClosePopWin(request.getParameter("popWinName")).
                    	setRequest(request);
            	}else{
            		EchoMessage.error().add(getMessage(request, "common.msg.error")).setClosePopWin(request.getParameter("popWinName")).
                        setRequest(request);
            	}
                return getForward(request, mapping, "message");
            }
        }
        

        String fromCrm = getParameter("fromCRM", request) ;
        request.setAttribute("fromCRM", fromCrm) ;
        if(forward==null){        		
        	forward = getForward(request, mapping, "functionUpdate");
        }
        return forward; 
    }


    /**
     * �޸�
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward update(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws  Exception {

        Result rs = new Result();
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
       
       
        int pageNo = getParameterInt("pageNo", request);
        if(pageNo==0){
        	pageNo = 1 ;
        }
        //�����ֵܱ��ID
        String f_brother = this.getBrotherId(getParameter("f_brother", request), getParameter("hasFrame", request));
        f_brother = f_brother == null ? "" : f_brother;
        request.setAttribute("f_brother", f_brother);
        String fromPage = request.getParameter("fromPage");
        String moduleType = getParameter("moduleType", request) ;
        
        HashMap values = new HashMap();
        //ȡ��������
       
        
        String tableName = getParameter("tableName", request);       
        
        DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
        readMainTable(values, tableInfo, request);
        
        
        
        LoginBean lg = new LoginBean();
        lg = (LoginBean) request.getSession().getAttribute("LoginBean");
        
        OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
        String currNodeId= "";
        String strCheckPersons = "";
        String designId = "";
        if(workFlow!=null&&workFlow.getTemplateStatus()==1){
	        HashMap oaMap = pcmgt.getOAMyWorkFlowInfo(String.valueOf(values.get("id")),tableName); 
	    	if(oaMap!=null){
	    		designId = String.valueOf(oaMap.get("applyType"));
	    		strCheckPersons = String.valueOf(oaMap.get("checkPerson"));
	    		currNodeId = String.valueOf(oaMap.get("currentNode"));
	    		/*�鿴��ǰ���������û��ί�и��ҵ�*/
	        	HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(lg.getId(), designId);
	        	for (String person : strCheckPersons.split(";")) {
					if (consignMap.get(person) != null) {
						strCheckPersons += lg.getId() + ";";
					}
				}
	    	}
	        if("-1".equals(currNodeId) || (strCheckPersons.length()>0 && !strCheckPersons.contains(";"+lg.getId()+";"))
	        		&& !"0".equals(currNodeId)){
	        	EchoMessage.error().add(this.getMessage(request, "common.msg.hasAudit")).setAlertRequest(request);
	        	return getForward(request, mapping, "alert");
	        }
	        String outNodeId = request.getParameter("currNodeId");
	    	if(outNodeId != null && outNodeId.length() > 0 && !currNodeId.equals(outNodeId)){
	    		//��ʼ���ת���󣬶Է�����Ϣδ����Ȼ���Ƶ��˳��ؿ�ʼ��㣬�Է��ټ���������Ϣʱ���ⲿ����ĵ�ǰ���id�����ݿ��¼�ĵ�ǰ��㲻һ�£��豨��
	    		request.setAttribute("noback", true) ;
	        	EchoMessage.error().add("�˵���ǰ��㷢���仯������ԭ���г��أ���ֹ��������������˴˽��").setcomeAdd(1).setAlertRequest(request);
	        	return getForward(request, mapping, "alert");
	    	} 
        }
        String sysParamter = tableInfo.getSysParameter();
        
        //�����޸�s����Ϣ
        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
            DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
            if (fi.getFieldType() == DBFieldInfoBean.FIELD_PIC) {
                String[] uploadpic = null;
                String[] fieldValue = getParameters(fi.getFieldName(), request);
                
                if(values.get(fi.getFieldName())!=null && values.get(fi.getFieldName()).toString().length()>0){
                	uploadpic = values.get(fi.getFieldName()).toString().split(";");
                }
                if(uploadpic==null){
                	uploadpic = getParameters(fi.getFieldName(), request);
                }
                String pic = "";
                if((null==uploadpic || uploadpic.length==0) && (fieldValue==null || fieldValue.length==0)){
                	values.put(fi.getFieldName(), "") ;
                }
                for (int k = 0; uploadpic != null && k < uploadpic.length; k++) {
                	if(uploadpic[k].startsWith("http:")){
                		pic += uploadpic[k] + ";"; //������ͼƬ
                	}else{
	                	String tf = FileHandler.getPathTempRead(uploadpic[k]);
	                	if(new File(tf).exists()){
	                		String upp = uploadpic[k].replaceAll("_"+fi.getFieldName(), "");//ȥ��ͼƬ�ļ��е��ֶ���
		                	String fn = FileHandler.getRealFileName(tableName, FileHandler.TYPE_PIC,upp);  
		                    pic += fn + ";";
		                    //��������ʽĿ¼
		                    FileHandler.copy(tableName, FileHandler.TYPE_PIC,uploadpic[k],
		                    		fn);
	                	}else{
	                		pic += uploadpic[k] + ";";
	                	}
                	}
                }
                if (pic.endsWith(";")) {
                    pic = pic.substring(0, pic.length() - 1);
                    values.put(fi.getFieldName(), pic);
                }
            } else if (fi.getFieldType() == DBFieldInfoBean.FIELD_AFFIX) {
                String[] uploadaffix = null;
                String[] fieldValue = getParameters(fi.getFieldName(), request);
                
                if(values.get(fi.getFieldName())!=null && values.get(fi.getFieldName()).toString().length()>0){
                	uploadaffix = values.get(fi.getFieldName()).toString().split(";");
                }
                if(uploadaffix==null){
                	uploadaffix = getParameters(fi.getFieldName(), request);;
                }
                String affix = "";
                if((null == uploadaffix || uploadaffix.length==0) && (fieldValue==null || fieldValue.length==0)){
                	values.put(fi.getFieldName(), "") ;
                }
                for (int k = 0; uploadaffix != null && k < uploadaffix.length;
                             k++) {
                	String tf = FileHandler.getPathTempRead(uploadaffix[k]);
                	if(new File(tf).exists()){
                		String upp = uploadaffix[k].replaceAll("_"+fi.getFieldName(), "");//ȥ��ͼƬ�ļ��е��ֶ���
	                	String fn = FileHandler.getRealFileName(tableName, FileHandler.TYPE_AFFIX,upp);  
	                    affix += fn + ";";
	                    //��������ʽĿ¼
	                    FileHandler.copy(tableName, FileHandler.TYPE_AFFIX,uploadaffix[k],
	                    		fn);
                	}else{
                		affix += uploadaffix[k] + ";";
                	}
                }
                if (affix.endsWith(";")) {
                    affix = affix.substring(0, affix.length() - 1);
                    values.put(fi.getFieldName(), affix);
                }
            }

            

        }
        //�ж��Ƿ����÷�֧����
        String sunClassCode = lg.getSunCmpClassCode();//��LoginBean��ȡ����֧������classCode


        //Ѱ���ӱ�
        List childTableList = DDLOperation.getChildTables(tableName, map);
        if (childTableList != null && childTableList.size() > 0) {
            for (int i = 0; i < childTableList.size(); i++) {
                DBTableInfoBean childTable = (DBTableInfoBean) childTableList.
                                             get(i);
                readChildTable(values, childTable, request, sunClassCode);
            }
        }


        //Ҫִ�е�define����Ϣ
        String defineInfo=request.getParameter("defineInfo");
        Object obj = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (obj instanceof MessageResources) {
		    resources = (MessageResources) obj;
		}
		
		 /*���淽ʽ*/
        String saveType = request.getParameter("button");
        //zxy ��midCalculate д��value�У����� ��define����@valueOfDB:midCalculate=true���жϵ�ǰ����Ϊ�м�����
        boolean isMidCalculate=false;
		if("midCalculate".equals(saveType)){
			saveType="";
			isMidCalculate =  true;
			values.put("midCalculate", "true");
		}
        
        
        //�޸Ĵ�ӡ����
        int printCount = new GlobalMgt().getPrintCount(tableName, String.valueOf(values.get("id")));
        values.put("printCount", printCount);
        Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = GlobalsTool.getMOperationMap(request) ;
		Result rso=dbmgt.detail(tableName, BaseEnv.tableInfos, values.get("id").toString(), lg.getSunCmpClassCode(), props, getLoginBean(request).getId(), false,  "");
		
        rs = dbmgt.update(tableInfo.getTableName(), map, values, getLoginBean(request).getId(),
        		defineInfo,resources,this.getLocale(request),saveType,getLoginBean(request),workFlow,props);
      //ERPͬ���ͻ���Ϣ��CRM
        if(rs.getRetCode() >= 0 && "tblCompany".equals(tableName)){
        	synClientInfoToCrm(request);      	
        }
        ActionForward forward = getForward(request, mapping, "alert");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode==ErrorCanst.RET_DEFINE_SQL_ALERT) {
        	/*���ϵͳ��־*/
        	//�õ��޸�ǰ��¼
        	HashMap valuesOld=new HashMap();
        	if(rso.retCode==ErrorCanst.DEFAULT_SUCCESS&&rso.retVal!=null){
        		valuesOld=(HashMap)rso.retVal;
        	}
        	int operation=1;
        	if("saveDraft".equals(saveType))operation=6;
        	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		String billTypeName=getModuleNameByLinkAddr(request, mapping);
        		dbmgt.addLog(operation, values, valuesOld, tableInfo, this.getLocale(request).toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"",billTypeName);
        		
        		//******�ж��Ƿ�������Ϣ���ֻ���*******//
        		SystemSettingBean sys = BaseEnv.systemSet.get("MobileApi");
        		if(sys != null){
	        		String mUrl= BaseEnv.systemSet.get("MobileApi").getSetting();
	        		if(!"".equals(mUrl)){
	        			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	        			Map m = new HashMap();
	        			m.put("tableName", tableInfo.getTableName());
	        			m.put("keyId", values.get("id"));
	        			m.put("type", "4");
	        			mUrl += "?tableName="+tableInfo.getTableName();
	        			mUrl += "&keyId="+values.get("id");
	        			mUrl += "&type=4";
	        			String r = com.koron.wechat.common.util.HttpRequest.post(mUrl, gson.toJson(m));
	        		}
        		}
        		//************end**********//
        	}
        	

        	 //���³ɹ��󱣴�ű�
            String path = request.getSession().getServletContext().getRealPath(
                    "DoSysModule.sql");
            TestRW.saveToSql(path, DynDBManager.mainUpdate);
            ArrayList childsDel = DynDBManager.childsDel;
            for (int i = 0; i < childsDel.size(); i++) {
                TestRW.saveToSql(path, childsDel.get(i).toString());
            }
            DynDBManager.childsDel.clear();
            ArrayList childsIns = DynDBManager.childsIns;
            for (int i = 0; i < childsIns.size(); i++) {
                TestRW.saveToSql(path, childsIns.get(i).toString());
            }
            DynDBManager.childsIns.clear();
            


            //ɾ����ʽ�ļ�
            String[] uploadDelAffix = getParameters("uploadDelAffix", request);
            String[] uploadDelPic = getParameters("uploadDelPic", request);
            for (int i = 0; uploadDelAffix != null && i < uploadDelAffix.length;
                         i++) {
                //FileHandler.delete(tableName, FileHandler.TYPE_AFFIX, uploadDelAffix[i]);
            }
            for (int i = 0; uploadDelPic != null && i < uploadDelPic.length; i++) {
               // FileHandler.delete(tableName, FileHandler.TYPE_PIC, uploadDelPic[i]);
            }
            
       	 	/*ɾ���ڴ�ͼƬ*/
            ArrayList listPic = (ArrayList) request.getSession().getAttribute("PIC_UPLOAD");
            ArrayList listAffix = (ArrayList) request.getSession().getAttribute( "AFFIX_UPLOAD");
            for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
                DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
                if (fi.getFieldType() == DBFieldInfoBean.FIELD_PIC){
                	userMgt.deleteTempImage(request.getParameterValues(fi.getFieldName()), listPic) ;
                }else if (fi.getFieldType() == DBFieldInfoBean.FIELD_AFFIX){
                	userMgt.deleteTempImage(request.getParameterValues(fi.getFieldName()),listAffix) ;
                }
            }

            
            //ͬ����֯�ܹ��� ��ʱͨѶ�ͻ���
            String objId=(String)values.get("id");
        	if("tblEmployee".equals(tableName) ){
        		boolean flag=empMgt.getOpenFlagUserById(objId);
        		if(flag==true ){
        			MSGConnectCenter.refreshEmpInfo(objId);
        		}
        	}
        	if("tblDepartment".equals(tableName)){
        		MSGConnectCenter.updateDeptInfo(objId);
        	}
            if(!"saveDraft".equals(saveType)){
	            /*�����ǰ�������� ������չ��ť��������Ϣ����*/
	        	String smsType = request.getParameter("smsType") ;
	        	if(smsType!=null){
	        		userMgt.sendWakeUpMessage(request, values, lg, smsType,tableInfo,getLocale(request).toString()) ;
	        	}else{
	        		Result smsResult = userMgt.querySMSModelInfo(tableName, "update") ;
	                if(smsResult!=null && smsResult.retVal!=null){
	                	String[] sms = (String[]) smsResult.retVal ;
	                	userMgt.autoSendMessage(values, lg, tableInfo, getLocale(request).toString(), sms,"update") ;
	                }
	        	}
	        	
	        	if(1 == tableInfo.getWakeUp()&&request.getParameterValues("alertType")!=null&&request.getParameterValues("alertType").length>0){
	        		userMgt.setAlert(request, (String)values.get("id"), lg, tableInfo) ;
	        	}
            }
            //��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��
        	String parentTableName=request.getParameter("parentTableName");
            parentTableName=parentTableName==null?"":parentTableName;
            if(tableName.equals("tblStockAnalysisInfo")){
            	StockAnalysisInfoMgt mgt = new StockAnalysisInfoMgt();
        		mgt.serivce();
            }
            
            //�޸ĳɹ�
            String parentCode = request.getParameter("parentCode") == null ? "" :
                                request.getParameter("parentCode");
            String fromCRM = getParameter("fromCRM", request) ;
            String alertMessage = getMessage(request, "common.msg.updateSuccess") ;
            if(rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
            	alertMessage = dbmgt.getDefSQLMsg(getLocale(request).toString(), (String)rs.retVal) ;
            }
            if("service".equals(fromCRM) || "detail".equals(fromCRM)){
        		EchoMessage.success().add(alertMessage).setBackUrl("/UserFunctionAction.do?operation=7&tableName="+tableName+"&fromCRM="+fromCRM+"&keyId="
        										+String.valueOf(values.get("id")+"&designId="+getParameter("designId", request)))
        									.setAlertRequest(request); ;
        	}else{
        		//������ӡ����
            	String savePrint = BaseEnv.systemSet.get("savePrint").getSetting() ;            	
             	if("true".equals(savePrint) && "quoteDraft".equals(saveType) && mop!=null && mop.print()){
             		request.setAttribute("BillId", values.get("id")) ;
	                Result print = new ReportSetMgt().getBillTable(tableName,moduleType);
	                if(print.retCode==ErrorCanst.DEFAULT_SUCCESS){
	                	if(print.getRealTotal()>0){
	                		List list = (List) print.getRetVal();
	                        ReportsBean report = (ReportsBean) list.get(0);
	                        Map det = report.getReportDetBean();
	                        Collection con = det.values();
	                        Iterator iter = con.iterator();
	                        while (iter.hasNext()) {
	                            ReportsDetBean detBean = (ReportsDetBean) iter.next();
	                            if (detBean.getNewFlag().equals("OLD")) {
	                                request.setAttribute("print", "true");
	                                break;
	                            }
	                        }
	                    	request.setAttribute("BillRepNumber", report.getReportNumber()) ;
	                	}
	                	
	                }
             	}
             	String planType = getParameter("planType", request) ;
             	if(isMidCalculate){
             		//���㣬ֱ�ӷ����޸Ľ���
             		EchoMessage.success().add(this.getMessage(request, "com.auto.succeed"))
       			 .setBackUrl("/UserFunctionAction.do?tableName=" +tableName +"&keyId="+values.get("id")
       				+ "&operation=" + OperationConst.OP_UPDATE_PREPARE + "&parentCode=" +parentCode 
       				+ "&f_brother=" + f_brother+"&parentTableName="+parentTableName
       				+ "&moduleType="+moduleType+"&winCurIndex="+
       				request.getParameter("winCurIndex")+"&popWinName="+request.getParameter("popWinName")).setAlertRequest(request);
             	}else if("detail".equals(fromPage)){
             		String curIndex = request.getParameter("winCurIndex") ;
             		EchoMessage.success().add(alertMessage)
             			.setBackUrl("/UserFunctionAction.do?tableName="+tableName+"&keyId="+values.get("id")+"&f_brother="+f_brother
             					   +"&operation=5&parentCode="+values.get("classCode")+"&parentTableName="+parentTableName
             					   +"&moduleType="+moduleType+"&winCurIndex="+("".equals(curIndex)?"&LinkType="+getParameter("LinkType", request)+
             							   "&noBack="+getParameter("noback", request):curIndex)+"&popWinName="+request.getParameter("popWinName")) 
             			.setAlertRequest(request) ;
             	}else{
             		if("true".equals(String.valueOf(request.getAttribute("noback")))){
             			EchoMessage.success().add(alertMessage).setAlertpopWin(request.getParameter("popWinName")).setAlertRequest(request);
             		}else{
	             		EchoMessage.success().add(alertMessage)
	                        .setBackUrl("/UserFunctionQueryAction.do?tableName=" + tableName + "&operation=" + OperationConst.OP_DETAIL + "&keyId="+values.get("id") 
	                        		  + "&parentCode=" + values.get("classCode") +  "&f_brother=" +f_brother +"&draftQuery="+("saveDraft".equals(saveType)?"draft":"")
	                        		  + "&moduleType="+moduleType+"&checkTab=Y&parentTableName="+parentTableName+"&pageNo="+pageNo+
	                        		  "&winCurIndex="+request.getParameter("winCurIndex")+"&popWinName="+request.getParameter("popWinName")).
	                        setAlertRequest(request);
             		}
             	}
        	}
            if(!"saveDraft".equals(saveType) && workFlow!=null && 1==workFlow.getTemplateStatus()){
            	String deliverTo = request.getParameter("deliverTo");
		        /*ִ�������*/
            	String fromAdvice=request.getParameter("fromAdvice")==null?"":request.getParameter("fromAdvice");
            	String retUrl = "/UserFunctionQueryAction.do?tableName=" + tableName + "&operation=" + ("true".equals(deliverTo)?OperationConst.OP_DETAIL:OperationConst.OP_UPDATE_PREPARE) +"&keyId="+values.get("id") 
            			   + "&parentCode=" + parentCode +  "&f_brother=" +f_brother +"&draftQuery="+("saveDraft".equals(saveType)?"draft":"")  + "&approveStatus="+getParameter("approveStatus", request)
           			 	   + "&moduleType="+moduleType+"&checkTab=Y&parentTableName="+parentTableName+"&pageNo="+pageNo+"&winCurIndex="+request.getParameter("winCurIndex");
           			 		
            	if("true".equals(deliverTo)){
            		String createBy="";
            		if(values.get("createBy")!=null){
            			createBy=values.get("createBy").toString();
            		}else{
            			createBy=((Result)new OAMyWorkFlowMgt().getBillCreateBy(values.get("id").toString(), tableName)).retVal.toString();
            		}
	            				
            		request.setAttribute("directJump", true);
            		//����û�ȷ��ת���ˣ���Ҫ�����б�
            		request.setAttribute("retValUrl", retUrl);
            		EchoMessage.success().add(getMessage(request, "common.msg.saveSuccess"))
						.setBackUrl("/OAMyWorkFlow.do?tableName=" + tableName + "&keyId=" 
							+ values.get("id")+"&fromAdvice="+fromAdvice+"&currNodeId=" + currNodeId + "&approveStatus="+getParameter("approveStatus", request)
							+ "&nextNodeIds="+rs.retVal+"&department="+OnlineUserInfo.getUser(createBy).getDeptId()
							+ "&designId="+getParameter("designId", request)+"&fromPage=erp&operation="+OperationConst.OP_AUDITING_PREPARE).setAlertRequest(request) ; 
            	}else{
            		EchoMessage.success().add(getMessage(request, "common.msg.saveSuccess"))
									.setBackUrl(retUrl).setAlertRequest(request) ;
            	}
            }
        } else {        	
        	 //ɾ����ʽ�ļ�,����Ҫ��֤ԭ��ʽͼƬ��Ҫɾ��������ֻɾ����session�д��ڵ���ʽ�ļ�
        	String picPath = GlobalsTool.getSysSetting("picPath");
        	if(picPath==null || picPath.length()==0){
	        	ArrayList listPic = (ArrayList) request.getSession().getAttribute(
	            "PIC_UPLOAD");
	        	ArrayList listAffix = (ArrayList) request.getSession().getAttribute(
	            "AFFIX_UPLOAD");
	            for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
	                DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
	                if (fi.getFieldType() == DBFieldInfoBean.FIELD_PIC){
	                	String upload[] = request.getParameterValues(fi.getFieldName());
	                	for (int k = 0; upload != null && k< upload.length;k++) {
	                		for (int j = 0; listPic != null && j < listPic.size(); j++) {
	                            if (listPic.get(j).toString().equals(upload[k])) {
	                                FileHandler.delete(tableName, FileHandler.TYPE_PIC,
	                                                   upload[k]);
	                                break;
	                            }
	                        }
	                    }
	                }else if (fi.getFieldType() == DBFieldInfoBean.FIELD_AFFIX){
	                	String upload[] = request.getParameterValues(fi.getFieldName());
	                	for (int k = 0; upload != null && k< upload.length;k++) {
	                		for (int j = 0; listAffix != null && j < listAffix.size(); j++) {
	                            if (listAffix.get(j).toString().equals(upload[k])) {
	                                FileHandler.delete(tableName, FileHandler.TYPE_AFFIX,
	                                                   upload[k]);
	                                break;
	                            }
	                        }
	                    }
	                }
	            }            
        	}
            
           if(rs.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
            	//�Զ���������Ҫ�û�ȷ��
            	ConfirmBean confirm=(ConfirmBean)rs.getRetVal();
            	String content=dbmgt.getDefSQLMsg(getLocale(request).toString(),confirm.getMsgContent());
            	String jsConfirmYes="";
            	String jsConfirmNo="";
            	jsConfirmYes="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()+":"+confirm.getYesDefine()+";'; if(this.parent.beforSubmit(this.parent.document.form)) this.parent.document.form.submit();";
            	if(confirm.getNoDefine().length()>0){
            		jsConfirmNo="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()+":"+confirm.getNoDefine()+";'; if(this.parent.beforSubmit(this.parent.document.form)) this.parent.document.form.submit();";
            	}
            	EchoMessage.confirm().add(content).setJsConfirmYes(jsConfirmYes).setJsConfirmNo(jsConfirmNo).setAlertRequest(request);

            }else {
            	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), saveType);
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
				}
            }
        }
       
        
        return forward;
    }
    
    /**
     * erp�ͻ���Ϣ�޸�ͬ����crm
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    
    private String synClientInfoToCrm(HttpServletRequest request){
		
		 //��ȡtblcompany��CRMClientInfo ӳ���ϵ
		 Result trff = userMgt.transferFields();
		 System.out.println(trff.getRetVal().toString());
		 JSONObject jsonObj = new JSONObject("{"+trff.getRetVal().toString()+"}");
		 //����map
		 Map crmMap = new HashMap();
		 //�ӱ�map
		 Map crmDetMap = new HashMap();
	        Iterator iterator = jsonObj.keys();
			while(iterator.hasNext()){
		        String key = (String) iterator.next();
		        String value = jsonObj.getString(key);
		        if(key.indexOf("_") > 0){
		        	crmDetMap.put(key.substring(key.indexOf("_")+1), value.substring(value.indexOf("_")+1));
		        }else{
		        	crmMap.put(key, value);
		        }
			}
			Result rst = userMgt.updateTransferFields(crmMap,crmDetMap,request);
    	
    	return null;
    }
    
    

    /**
     * ɾ��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
        ActionForward forward = null;
        String tableName = getParameter("tableName", request);        
        String nstr[] = getParameter("varKeyIds", request).split("\\|") ;
        String pageType = request.getParameter("pageType");
        if("detail".equals(pageType) || "update".equals(pageType)){
        	nstr = getParameters("id", request);
        }
        
        String moduleType = getParameter("moduleType", request) ;
        OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName) ; 
        
        /**������������������ѯ��ǰ�Ƿ��Ѿ���ˣ�����Ѿ���˲���ɾ������**/
        if (null != nstr && nstr.length > 0&&workFlow!=null&&workFlow.getTemplateStatus()==1) {
        	OAMyWorkFlowMgt workFlowMgt=new OAMyWorkFlowMgt();
            for (String kid : nstr) {
            	Object[] workFlowNode =workFlowMgt.getTableWorkFlowName(tableName, kid);
                if (workFlowNode!=null && ( !"draft".equals(workFlowNode[1]) && !workFlowNode[0].equals("0"))){
                	String parentTableName = getParameter("parentTableName", request) ;
                    EchoMessage.error().add(getMessage(
                            request, "com.approvedNotDelete"))
                            .setBackUrl("/UserFunctionQueryAction.do?parentTableName="+parentTableName+"&tableName=" +
                                    tableName+"&moduleType="+moduleType+"&winCurIndex="+request.getParameter("winCurIndex")
                            ).setAlertRequest(request);
                    return getForward(request, mapping, "message");
                }
            }
        }
        //����Ƿ���id
        boolean hasId = false; 
        for (String kid : nstr) {
        	if(kid != null && kid.length() > 0){
        		hasId = true;
        	}
        }
        if(!hasId ){
        	EchoMessage.error().add("δ����ID").setAlertRequest(request);
            return getForward(request, mapping, "alert");
        }
        for (String kid : nstr) {
	        Result rsAcc=dbmgt.hasCreateAcc(tableName,kid);
	    	if(rsAcc.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    		if(rsAcc.retVal!=null){//����Ѿ�����ƾ֤�������������ʾ
	    			String[] str=(String[])rsAcc.retVal;
	    			EchoMessage.error().add(this.getMessage(request, "common.hasCreateAcc.Oper.error",str[0],str[1])).setAlertRequest(request);
	                return getForward(request, mapping, "alert");
	    		}
	    	}else{
	    		EchoMessage.error().add(rsAcc.retCode,request).setAlertRequest(request);
	            return getForward(request, mapping, "alert");
	    	}
        }
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(tableName);
        LoginBean lg = (LoginBean) request.getSession().getAttribute(
                "LoginBean");
        String sunClassCode = lg.getSunCmpClassCode(); //��LoginBean��ȡ����֧������classCode
        Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        //����Ѿ��½ᣬ������ɾ��
        HashMap values = new HashMap();
        readMainTable(values, tableInfo, request);
        String sysParamter = tableInfo.getSysParameter();
        Date time = null;
        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
            DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
            String o = fi.getDefaultValue();
            String type = fi.getFieldIdentityStr() ;
            if (null != type) {
                if (null != o && type.equals("accendnotstart")) {
                    String date = (String) dbmgt.getFieldValueById(nstr,
                            tableInfo.getTableName(), fi.getFieldName());
                    if (null != date) {
                    	try{
                    		time = BaseDateFormat.parse(date,
                                BaseDateFormat.yyyyMMdd);
                    	}catch(Exception e){
                    		EchoMessage.error().add("�ֶ�"+fi.getFieldName()+"����Ϊ����ǰ����������ʽ����ȷ"+date).setBackUrl(
           	                     "/UserFunctionQueryAction.do?tableName=" +
           	                     tableName+"&winCurIndex="+request.getParameter("winCurIndex")
           	             	).setAlertRequest(request);
                    		return getForward(request, mapping, "message");
                    		
                    	}
                    }
                }
            }
        }
        int currentMonth = 0;
        int currentYear=0;
        if (null != time) {
            currentMonth = time.getMonth() + 1;
            currentYear=time.getYear()+1900;
        }
        int periodMonth = -1;
        int periodYear=-1;
        GlobalMgt mgt = new GlobalMgt();
        AccPeriodBean accBean=(AccPeriodBean)sessionSet.get("AccPeriod");
        if (accBean!=null) {
            periodMonth = accBean.getAccMonth();
            periodYear=accBean.getAccYear();
        }
        if ((currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth)) && currentMonth != 0) {
            if (null != sysParamter) {
                if (sysParamter.equals("CurrentAccBefBill") && (currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth))) {
                    EchoMessage.error().add(getMessage(request,
                            "com.currentaccbefbill.delete")).setBackUrl(
                            		"/UserFunctionQueryAction.do?tableName=" +
           	                     tableName+"&winCurIndex="+request.getParameter("winCurIndex")).
                            setAlertRequest(request);
                    return getForward(request, mapping, "message");
                }

                if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") &&(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) )) {
                    EchoMessage.error().add(getMessage(request,"com.can.not.monthly")).setBackUrl(
                    		"/UserFunctionQueryAction.do?tableName=" +
   	                     tableName+"&winCurIndex="+request.getParameter("winCurIndex")).
                            setAlertRequest(request);
                    return getForward(request, mapping, "message");
                }
            }
        }

        /*query reation table*/
        Result  rs= dbmgt.getRelation(tableName,nstr,map,this.getLocale(request).toString());
        if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
        	if(rs.retCode==ErrorCanst.RET_EXISTSRELATION_ERROR){
	        	 EchoMessage.error().add(this.getMessage(request,"common.msg.RET_EXISTSRELATION_ERROR",rs.retVal.toString())).setBackUrl(
	                     "/UserFunctionQueryAction.do?tableName=" +
	                     tableName+"&winCurIndex="+request.getParameter("winCurIndex")
	             ).setAlertRequest(request);
        	}else{
        		EchoMessage.error().add(rs.retCode,request).setBackUrl(
	                     "/UserFunctionQueryAction.do?tableName=" +
	                     tableName+"&winCurIndex="+request.getParameter("winCurIndex")
	             ).setAlertRequest(request);
        	}
        	return getForward(request, mapping, "message");
        }

        //�����˷�֧����ʱ�����������ĩ����֧����������ɾ�������������
        String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
        boolean openSunCompany = Boolean.parseBoolean(openValue);
        boolean isLastSunCompany =Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
        boolean isSharedTable = tableInfo.getIsSunCmpShare() == 1 ? true : false;
        //����ְԱ����ű�ʱ������ĩ����֧��������
        boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo.
                getTableName()) ||
                              "tblDepartment".equalsIgnoreCase(tableInfo.
                getTableName())
                              ? true : false;
        if (openSunCompany && !isLastSunCompany && !isSharedTable &&
            !isEmpOrDept) {
            EchoMessage.error().add(getMessage(request,
                                               "common.msg.notLastSunCompany")).
                    setRequest(request);
            return getForward(request, mapping, "message");
        }

       
    	Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		String draftQuery = getParameter("draftQuery", request) ;
		
		String deptCode[]=empMgt.getDeptCodeById(nstr);
		
		Result result=empMgt.getOpenFlagUser(nstr);
		List objList=(List) result.retVal;
		String userIds[] =new String[objList.size()];
		for(int i=0;i<objList.size();i++){
			Object[]  obj=(Object[]) objList.get(i);
			userIds[i]=(String) obj[0];
		}
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		
		//�õ�Ҫɾ�����ݵ���Ϣ
		ArrayList manyValues=new ArrayList();
		for(int i=0;i<nstr.length;i++){
			Result rso=dbmgt.detail(tableName, BaseEnv.tableInfos, nstr[i], lg.getSunCmpClassCode(), props, getLoginBean(request).getId(), false,  "");
			if(rso.retCode==ErrorCanst.DEFAULT_SUCCESS&&rso.retVal!=null){
				manyValues.add(rso.getRetVal());
			}
		}
		

		
        rs = dbmgt.delete(tableName, map, nstr,
                                 getLoginBean(request).getId(),resources,this.getLocale(request),"draft".equals(draftQuery)?true:false);


        
    	
    	
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
    		//��¼ɾ����־��Ϣ
        	for(int i=0;i<manyValues.size();i++){
        		HashMap valuesOld=(HashMap)manyValues.get(i);
        		int operation=2;
            	if("draft".equals(valuesOld.get("workFlowNodeName")))operation=7;     
            	String billTypeName=getModuleNameByLinkAddr(request, mapping);
        		dbmgt.addLog(operation, valuesOld, null, tableInfo, this.getLocale(request).toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"",billTypeName);
        		
        		//******�ж��Ƿ�������Ϣ���ֻ���*******//
        		SystemSettingBean sys = BaseEnv.systemSet.get("MobileApi");
        		if(sys != null){
	        		String mUrl= BaseEnv.systemSet.get("MobileApi").getSetting();
	        		if(!"".equals(mUrl)){
	        			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	        			Map m = new HashMap();
	        			m.put("tableName", tableInfo.getTableName());
	        			m.put("keyId", valuesOld.get("id"));
	        			m.put("type", "2");        			
	        			mUrl += "?tableName="+tableInfo.getTableName();
	        			mUrl += "&keyId="+valuesOld.get("id");
	        			mUrl += "&type=2";
	        			String r = com.koron.wechat.common.util.HttpRequest.post(mUrl, gson.toJson(m));
	        		}	
        		}
        		//************end**********//
        	}
        	
        	Object []obj=(Object[])rs.getRetVal();
        	//ɾ��ͼƬ�͸���
            String[] files = (String[])obj[0];
            //ɾ��ͼƬ
            if (files[0] != null && files[0].length() > 0) {
                String[] fs = files[0].split(";");
                for (int i = 0; i < fs.length; i++) {
                    //FileHandler.delete(tableName, FileHandler.TYPE_PIC, fs[i]);
                }
            }
            //ɾ������
            if (files[1] != null && files[1].length() > 0) {
                String[] fs = files[1].split(";");
                for (int i = 0; i < fs.length; i++) {
                    //FileHandler.delete(tableName, FileHandler.TYPE_AFFIX, fs[i]);
                }
            }
            
        	//ͬ����֯�ܹ��� ��ʱͨѶ�ͻ���
        	if("tblEmployee".equals(tableName)) {
        		if(userIds.length>0){
        			MSGConnectCenter.deleteObj(userIds, "employee");
        		}
        	}
        	if("tblDepartment".equals(tableName)){
        		 MSGConnectCenter.deleteObj(deptCode,"dept");
        	}
        	
        	//����ǵ���Ҫ��������ɱ�
        	if(tableInfo.getSysParameter().equals("CurrentAccBefBillAndUnUseBeforeStart")&&BaseEnv.systemSet.get("SaveBillAutoRecalc")!=null&&BaseEnv.systemSet.get("SaveBillAutoRecalc").getSetting().equals("true")){
        		ReCalcucateMgt sysMgt=new ReCalcucateMgt();
        		sysMgt.reCalcucateData(lg.getSunCmpClassCode(),accBean.getAccYear(),  accBean.getAccMonth(), lg.getId(),"reCalcucate","","");
        	}
        	
        	//��������˹�������ɾ���ҵĹ������ĵ�����Ϣ
        	if(workFlow!=null&&workFlow.getTemplateStatus()==1){
	        	String keyIds = "" ;
	        	if(nstr!=null){
	        		for(String str : nstr){
	        			keyIds += "'"+str+"'," ;
	        		}
	        		if(keyIds.length()>0){
	        			keyIds = keyIds.substring(0,keyIds.length()-1) ;
	        		}
	        	}
	        	new OAMyWorkFlowMgt().deleteOAMyWorkFlow(keyIds) ;
        	}
        	 
            //����ɾ���ű�
            String path = request.getSession().getServletContext().getRealPath(
                    "DoSysModule.sql");
            ArrayList delArray = DynDBManager.delArray;
            for (int i = 0; i < delArray.size(); i++) {
                TestRW.saveToSql(path, delArray.get(i).toString());
            }
            DynDBManager.delArray.clear();
            /////////////////////////
            /*ɾ��ʱ �Զ�������Ϣ*/
            Result smsResult = userMgt.querySMSModelInfo(tableName, "delete") ;
            if(smsResult!=null && smsResult.retVal!=null){
            	String[] sms = (String[]) smsResult.retVal ;
            	userMgt.autoSendMessage(values, lg, tableInfo, getLocale(request).toString(), sms,"delete") ;
            }
            //ɾ���ɹ�
            if("detail".equals(pageType)||"update".equals(pageType)){
            	//���޸�������������ɾ��
            	EchoMessage.success().add("ɾ���ɹ�").setClosePopWinRefresh(request.getParameter("popWinName")).setAlertRequest(request);
            	forward = getForward(request, mapping, "message");
            }else{
	            request.setAttribute("result", rs.retVal);
	            forward = query(mapping, form, request, response);
            }
        } else {
        	String backUrl = "/UserFunctionQueryAction.do?tableName=" +tableName+
			 "&operation=" +OperationConst.OP_QUERY +"&parentCode=" +request.getParameter("parentCode") +
				 "&moduleType="+moduleType+"&f_brother=" +request.getParameter("f_brother") + "&checkTab=Y&parentTableName="
				+request.getParameter("parentTableName")+"&winCurIndex="+request.getParameter("winCurIndex")
				+"&draftQuery="+getParameter("draftQuery", request);
        	
        	if(rs.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
            	// �Զ���������Ҫ�û�ȷ��
            	ConfirmBean confirm=(ConfirmBean)rs.getRetVal();
            	String content=dbmgt.getDefSQLMsg(getLocale(request).toString(),confirm.getMsgContent());
            	String jsConfirmYes="";
            	String jsConfirmNo="";
            	String saveAdd = request.getParameter("button");
                if (saveAdd != null && saveAdd.equals("saveAdd")) { //��Ȼ��������������֮ǰ�ı��淽ʽ
                	jsConfirmYes="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                				+":"+confirm.getYesDefine()+";';this.parent.subAdd();";
                	if(confirm.getNoDefine().length()>0){
                		jsConfirmNo="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                				   +":"+confirm.getNoDefine()+";';this.parent.subAdd();";
                	}
                }else{
                	jsConfirmYes="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                			   + ":"+confirm.getYesDefine()+";'; if(this.parent.beforSubmit(this.parent.document.form)){ " 
                			   + "this.parent.document.form.operation.value="+OperationConst.OP_ADD+";this.parent.document.form.submit();}";
                	if(confirm.getNoDefine().length()>0){
                		jsConfirmNo="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                				  + ":"+confirm.getNoDefine()+";'; if(this.parent.beforSubmit(this.parent.document.form)){ " 
                				  + "this.parent.document.form.operation.value="+OperationConst.OP_ADD+";this.parent.document.form.submit();}";
                	}
                }
            	EchoMessage.confirm().add(content).setJsConfirmYes(jsConfirmYes)
            						 .setJsConfirmNo(jsConfirmNo).setAlertRequest(request);
            }else{
            	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl(backUrl).setAlertRequest(request);
				}
            }

            forward = getForward(request, mapping, "message");
        }
        return forward;

    }

    /**
     * ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward checkPrepare(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws
            Exception {

        String KeyId = getParameter("keyId", request);
        String tableName = getParameter("tableName", request);
        String remark = userMgt.getApproveRemark(tableName, KeyId,
                                               getLoginBean(request).getId());
        request.setAttribute("tableName", tableName);
        request.setAttribute("keyId", KeyId);
        request.setAttribute("remark", remark);
        return getForward(request, mapping, "check");

    }


    protected ActionForward check(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
            Exception {
        ActionForward forward = null;
        String tableName = getParameter("tableName", request);
        String keyId = request.getParameter("keyId");
        String remark = request.getParameter("remark");
        String checkflag = request.getParameter("checkflag");
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);

        Result rs = userMgt.writeCheckInfo(tableName, keyId,
                                         getLoginBean(request).getId(),
                                         remark, checkflag);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	 //д״̬�ɹ�
        	userMgt.updateCheckInfo(tableName, keyId, -1);
            //������������״̬ ��3�������ǲݸ壬��Ҫ�鿴���¼�������״̬
            request.setAttribute("result", rs.retVal);
            forward = query(mapping, form, request, response);
        } else {

            EchoMessage.error().add(rs.retCode, request).
                    setRequest(request);
            forward = getForward(request, mapping, "message");
        }
        return forward;

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
    public ActionForward query(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
            Exception {

    	//�����ֵܱ�
    	int operation = getOperation(request);
    	String f_brother = getParameter("f_brother", request) ;
    	request.setAttribute("f_brother", f_brother) ;
    	 /*�ݸ嵯������*/
        String draftQuery = getParameter("draftQuery", request) ;
        /*CRM�����ѯ*/
        String fromCrm = getParameter("fromCRM", request) ;
        request.setAttribute("fromCRM", fromCrm) ;
        String reportNumber = null;
        try {
            int pageSize = getParameterInt("pageSize", request);
            int pageNo = getParameterInt("pageNo", request);
            String noPageSize = (String) request.getAttribute("NoPageSize") ;
            if (pageNo == 0 || "OK".equals(noPageSize)) {
                pageNo = 1;
            }
            
            reportNumber = this.getParameter("tableName", request); //��ñ���
            Hashtable map = (Hashtable) request.getSession().getServletContext().
                            getAttribute(BaseEnv.TABLE_INFO);
            DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,reportNumber);
            
            if(tableInfo == null){
	            request.setAttribute("noback", true) ;
	            ActionForward forward = getForward(request, mapping, "message");
	            EchoMessage.error().add("��"+reportNumber+"������").setRequest(request);
	            return forward;
            }
            

            
            request.setAttribute("tableInfo", tableInfo);

            //����ѯ�ı�Ϊ�ֱ���ھӱ�ʱ�����ݸ������ȡȨ��
            String parentTableName=this.getParameter("parentTableName",request);
            parentTableName=parentTableName==null?"":parentTableName;

            String[] parentTable = null ;
            if(tableInfo.getPerantTableName()!=null&&!"".equals(tableInfo.getPerantTableName())){
            	parentTable = tableInfo.getPerantTableName().split(";") ;
            }
            //�������������1��ʱ��
            if(parentTableName!=null&&!"".equals(parentTableName)
            									&&parentTable.length>1){
            	//zxy �ӷ�ΧȨ��������ҵ�һ����ͬ�ı�������������������ʲô������������
//            	List list = getLoginBean(request).getRoles() ;
//            	for (String str : parentTable) {
//					for (Object o : list) {
//						RoleBean roleBean = (RoleBean) o ;
//						List roleList = (List) new RoleMgt().queryScope(roleBean.getId()).getRetVal() ;
//						for(Object role : roleList){
//							Object[] roleScope = (Object[]) role ;
//							if(roleScope[2].toString().contains(str)){
//								parentTableName = str ;
//							}
//						}
//					}
//				}
            }
            
            MOperation mop = GlobalsTool.getMOperationMap(request) ;
            //��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��
            if(mop == null){
            	//������ģ���޷���Ȩ��
            	request.setAttribute("noback", true) ;
                ActionForward forward = getForward(request, mapping, "message");
                EchoMessage.error().add(getMessage(request,"common.msg.RET_NO_RIGHT_ERROR")).setRequest(request);
                return forward;
            }
            request.setAttribute("parentTableName", parentTableName);
            if (tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
                HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
                String winCurIndex = request.getParameter("winCurIndex");
                /*�ֵܱ�����ʾ������f_brother*/
                if(!("/UserFunctionQueryAction.do?tableName="+reportNumber).equals(winIndexMap.get(winCurIndex))){
                	request.setAttribute("f_brother", "") ;
                }
            } 
            
            if(tableInfo.getClassFlag()==1 && !"Y".equals(request.getParameter("checkTab"))){
            	//�����Ƿ��������checkʱ����ת��Ŀ¼��ʾ����
            	String mainSrc = request.getRequestURI()+(request.getQueryString()==null?"?checkTab=Y":"?"+request.getQueryString()+"&checkTab=Y");
            	request.setAttribute("mainSrc", mainSrc);
            	request.setAttribute("MOID", mop.getModuleId());
            	request.setAttribute("editable", "true");
            	return getForward(request, mapping, "frameSet");
            }
            
            ArrayList scopeRight = new ArrayList();
            scopeRight.addAll(mop.getScope(MOperation.M_QUERY));     
        	//�������з���Ȩ��
        	scopeRight.addAll(mop.classScope) ;
          	ArrayList allScopeList = this.getLoginBean(request).getAllScopeRight();
        	if(allScopeList!=null){        		
        	   scopeRight.addAll(allScopeList) ;
    		}
            request.setAttribute("MOID", mop.moduleId);
            //����Ƿ������˵���ѯ
         /*   
            //�ھӱ��Ϊ���������ʾ
            if (operation == 0 && !"draftPop".equals(draftQuery)) {
            	//��鵱ǰ���Ƿ����ֵܱ����
                String checkTab = this.getParameter("checkTab", request);
                if (checkTab == null) {
                    ArrayList childTabList = new ArrayList();
                    childTabList = DDLOperation.getBrotherTables(reportNumber,map);
                    if (childTabList.size() > 0) {  //�Ƿ��ж���TAB
                        Result rs = new Result();
                        rs = new TabMgt().getMainTableFirstId(reportNumber);
                        request.setAttribute("firstId",
                                             rs.getRetVal().toString());

                        String tabIndex = getParameter("tabIndex", request)==null?"":getParameter("tabIndex", request) ;
                        request.setAttribute("tabIndex", tabIndex) ;
                        request.setAttribute("tableName", reportNumber);
                        String urlParam="";
                        Iterator it=request.getParameterMap().keySet().iterator() ;
                        String linkType=request.getParameter("LinkType");
                        
                        while(it.hasNext()){
                        	String fieldName=it.next().toString();
                        	if(!fieldName.equals("src")&&!fieldName.equals("tableName")&&!fieldName.equals("winCurIndex")&&!fieldName.equals("operation")){
                        		String value=request.getParameter(fieldName);
                        		if (linkType != null && (linkType.equals("@URL") || linkType.equals("@URL:")) && value != null) {
                        			value =new String(value.getBytes("iso-8859-1"), "UTF-8");
                				}
                        		value=java.net.URLEncoder.encode(value,"UTF-8"); 
                       
            
                        		urlParam+="&"+fieldName+"="+value;
                        	}
                        }
                        request.setAttribute("urlParam", urlParam);
                        ReportSetMgt setMgt = new ReportSetMgt();
                		ReportsBean reportSetBean = (ReportsBean) setMgt.getReportSetInfo(reportNumber, this.getLocale(request).toString()).getRetVal();
                		request.setAttribute("reportName", reportSetBean.getReportName());
                		request.setAttribute("src", request.getParameter("src"));
                        return getForward(request, mapping, "tabIndex");
                    }

                }
            }
            */
            LoginBean lg = getLoginBean(request);

            
            ReportData reportData = new ReportData();
           
            ArrayList scopeRightUpdate=mop.getScope(MOperation.M_UPDATE);
            ArrayList scopeRightDel=mop.getScope(MOperation.M_DELETE);
            DefineReportBean defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(request).toString(),getLoginBean(request).getId());
            Result rs = reportData.showData(mop,request, scopeRight,reportNumber,defBean,"",pageNo, pageSize,
            		scopeRightUpdate,scopeRightDel,lg,null);
            	
        	//�����ɹ�
            if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && request.getAttribute("ExportFileName") !=null && !request.getAttribute("ExportFileName").equals("")){
            	String fn =request.getAttribute("ExportFileName")+"";
            	String mime = request.getSession().getServletContext().getMimeType(fn.substring(fn.lastIndexOf(".")).toLowerCase());
	        	if(mime == null || mime.length() == 0){
	        		mime = "application/octet-stream; CHARSET=utf8";
	        		response.setContentType(mime);
	        		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
	        	}else{
	        		response.setContentType(mime);
	        		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
	        	}
	        	FileHandler.readFile("../../AIOBillExport/"+fn,response);
            	return null;
            }
            
            if(BaseEnv.workFlowInfo.get(reportNumber)!=null&&BaseEnv.workFlowInfo.get(reportNumber).getTemplateStatus()==1){
            	request.setAttribute("OAWorkFlow", true);
            }
            
            if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS && rs.retCode!=ErrorCanst.RETURN_COL_CONFIG_SETTING){
            	if(rs.retCode == ErrorCanst.EXCEL_REPORT_EXPORT){
            		String moduleType = getParameter("moduleType", request) ;
            		request.setAttribute("noback", false);
            		///&isTab=yes&
            		EchoMessage.success().add(rs.getRetVal().toString())
            			.setBackUrl("/UserFunctionQueryAction.do?tableName="+tableInfo.getTableName()
            					+"&parentTableName="+parentTableName+"&moduleType="+(moduleType==null?"":moduleType)
            					+"&checkTab=Y&f_brother="+request.getParameter("f_brother")+"&brotherEnter="+request.getParameter("brotherEnter")+"&winCurIndex="+request.getParameter("winCurIndex"))
            			.setNotAutoBack()
	                    .setAlertRequest(request);
            		return getForward(request, mapping, "message") ;
            	}else if(rs.retCode==ErrorCanst.RET_LIST_NOCOLUMN){
	            	EchoMessage.error().add(rs.getRetCode(),request).setRequest(request);
	            	return getForward(request, mapping, "message");
            	}else{
            		EchoMessage.error().add(rs.getRetVal().toString()).setRequest(request);
	            	return getForward(request, mapping, "message");
            	}
            }
            
            /**������ֵܱ�toDetail=goֱ�ӽ��뵽�������**/
            String goDetail = getParameter("toDetail", request) ;
            String toDetail = (String) request.getAttribute("toDetail") ;
            if("go".equals(goDetail) && tableInfo.getHasNext()==1
            					&& toDetail!=null && toDetail.length()>0){
            	return new ActionForward(toDetail) ;
            }
            
            if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && pageNo!=1){
            	ArrayList listStr = (ArrayList) rs.getRetVal() ;
            	if(listStr!=null){
            		boolean isExist = false ;
	            	if(listStr.size()==0){
	            		isExist = true ;
	            	}else if(listStr.size()==1){
	            		if(listStr.get(0) instanceof TableListResult){
	            			if(((TableListResult)listStr.get(0)).getRowDis().contains("rowAll")){
	            				isExist = true ;
	            			}
	            		}else if(((Object[])listStr.get(0))[0].toString().contains("rowAll")){
	            			isExist = true ;
	            		}
	            	}
	            	if(isExist){
	            		request.setAttribute("NoPageSize", "OK") ;
	                	return query(mapping, form, request, response) ;
	            	}
            	}
            }

            if(rs.retCode==ErrorCanst.RETURN_COL_CONFIG_SETTING){
            	request.setAttribute("tableName", reportNumber) ;
            	return getForward(request, mapping, "listConfig") ;
            }
            request.setAttribute("pageBar", pageBar(rs, request));

            //��ҳ�����õ��ݴ�ӡ��ť
            String moduleType = getParameter("moduleType", request) ;
            rs=new ReportSetMgt().getBillTable(reportNumber,moduleType);
            if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
            	EchoMessage.error().add(this.getMessage(request,
                "common.msg.REPORTSQL_NOTEXISTS")).
                setBackUrl("/ReportSetQueryAction.do?reportNumber=" +
                           reportNumber + "&type=table").
                setRequest(request);
            	return getForward(request, mapping, "message");
            }else{
            	if(rs.getRealTotal()>0){
            		List list = (List) rs.getRetVal();
                    ReportsBean report = (ReportsBean) list.get(0);

                    Map det = report.getReportDetBean();
                    Collection con = det.values();
                    Iterator iter = con.iterator();
                    while (iter.hasNext()) {
                        ReportsDetBean detBean = (ReportsDetBean) iter.next();
                        //NewFlag=OLD��ʾ����ƴ�ӡ��ʽ
                        //
                        if (detBean.getNewFlag().equals("OLD")) {
                            request.setAttribute("print", "true");
                            //��˺��ӡ
                            request.setAttribute("AuditPrint", report.getAuditPrint());
                            break;
                        }
                    }
                	request.setAttribute("BillRepNumber", report.getReportNumber()) ;
                	Result rs2 = new ReportDataMgt().getBillFormatList(reportNumber,moduleType,getLocale(request).toString(),getLoginBean(request).getId(),getLoginBean(request).getDepartCode());
                    request.setAttribute("formatList", rs2.getRetVal());
            	}
            }
            //��ѯ�Ƿ����б�Ĵ�ӡ
            rs=new ReportSetMgt().getReportSetInfo(reportNumber,getLocale(request).toString());
            if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
            	EchoMessage.error().add(this.getMessage(request,
                "common.msg.REPORTSQL_NOTEXISTS")).
                setBackUrl("/ReportSetQueryAction.do?reportNumber=" +
                           reportNumber + "&type=table").
                setRequest(request);
            	return getForward(request, mapping, "message");
            }else{
            	if(rs.getRealTotal()>0){
            		ReportsBean report = (ReportsBean) rs.getRetVal();
                    Map det = report.getReportDetBean();
                    Collection con = det.values();
                    Iterator iter = con.iterator();
                    while (iter.hasNext()) {
                        ReportsDetBean detBean = (ReportsDetBean) iter.next();
                        if (detBean.getNewFlag().equals("OLD")) {
                            request.setAttribute("printList", "true");
                            break;
                        }
                    }
            	}
            }
            
            //��ѯ�Ƿ��б�ǩ��ӡ
            rs=new ReportSetMgt().getReportSetInfo("PrintSeq"+reportNumber,getLocale(request).toString());
            if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal()!=null){
            	request.setAttribute("printLabel", "label") ;
            }
            Cookie[] coks = request.getCookies();
            for (int i = 0; i < coks.length; i++) {
                Cookie cok = coks[i];
                if (cok.getName().equals("JSESSIONID")) {
                    request.setAttribute("JSESSIONID", cok.getValue());
                    break;
                }
            }
            //������չ��ť��Ϣ
            String butTag = parseExtendButton(request, tableInfo, "list"+(draftQuery==null?"":draftQuery));
            if (butTag != null && butTag.length() > 0) {
                request.setAttribute("extendButton", butTag);
            }
            /*��ѯ�Ƿ�浼��ģ��*/
            // ���ܴ治���ڵ���ģ�壬��������
//            Result rs_import = new ImportDataMgt().selectExistImportDataByTable(tableInfo.getTableName());
//	        if(rs_import.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
	        	request.setAttribute("billExport", "OK") ;
//	        }
	        /*�ݸ�*/
	        if(tableInfo.getDraftFlag()==1){
	        	request.setAttribute("draftFlag", 1) ;
	        }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            EchoMessage.error().add(this.getMessage(request,
                    "common.msg.REPORTSQL_NOTEXISTS")).
                    setBackUrl("/ReportSetQueryAction.do?reportNumber=" +
                               reportNumber + "&type=table").
                    setRequest(request);
            return getForward(request, mapping, "message");

        }
        /*�鿴����������վ�Ƿ�����*/
        AIOShopBean shopBean = BaseEnv.AIO_SHOP ;
        String sysSetting = GlobalsTool.getSysSetting("aioshop") ;
        if("true".equals(sysSetting) && shopBean!=null 
        			&&shopBean.getStatusId()!=null&& shopBean.getStatusId()==0){
        	request.setAttribute("existShop", "exist") ;
        }
        request.setAttribute("moduleName",getModuleNameByLinkAddr(request, mapping));//ҳ�������ʾģ������
        if("draftPop".equals(draftQuery)){
        	return getForward(request, mapping, "draftList") ;
        }else{
        	return getForward(request, mapping, "functionList");
        }
        
    }

    private MOperation getMOperation(Hashtable allTables,
                                     HttpServletRequest request,
                                     String tableName) {
        DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,
                tableName);
        String tn = tableName;
        if (tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE) {
//        	����ѯ�ı�Ϊ�ֱ���ھӱ�ʱ�����ݸ������ȡȨ��
            String parentTableName=this.getParameter("parentTableName",request);
            parentTableName=parentTableName==null?"":parentTableName;
            tn = parentTableName;
        }
        MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().
                                       get(
                                               "/UserFunctionQueryAction.do?tableName=" +
                                               tn));
        return mop;
    }

    public static String getEnumerationItems(HttpServletRequest request,
                                             Hashtable enumMap,
                                             String enumeration,
                                             String value, String locale) {
        List list = new ArrayList();
        EnumerateBean beans[] = (EnumerateBean[]) enumMap.values().toArray(new
                EnumerateBean[0]);
        for (int i = 0; i < beans.length; i++) {
            if (beans[i].getEnumName().equals(enumeration)) {
                for (int j = 0; j < beans[i].getEnumItem().size(); j++) {
                    EnumerateItemBean eib = (EnumerateItemBean) beans[i].
                                            getEnumItem().get(j);
                    if (eib.getEnumValue().equals(value)) {
                        return ((KRLanguage) (eib.getDisplay())).get(locale);
                    }
                }

                break;
            }
        }
        return "";
    }

    public static DBFieldInfoBean getFieldInfo(Hashtable allTables,
                                               String tableName,
                                               String fieldName) {
        DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
        if (tableInfo != null) {
            for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
                DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
                if (fi.getFieldName().equals(fieldName)) {
                    return fi;
                }
            }
        }
        return null;
    }


    /**
     * ������Ĳ�ѯ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward popupSelect(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws
            Exception {
    	request.setAttribute("noOp", request.getParameter("noOp")); //���Կ��ƽ��� �ϲ���ʾȷ�����رյȲ�����Ť
    	String isRoot = request.getParameter("isRoot");
    	String iroot = request.getParameter("root");
    	if("isRoot".equals(isRoot) || "root".equals(iroot)){  
    		request.setAttribute("root", "root");
    	}
    	LoginBean loginBean=this.getLoginBean(request);
        String tableName = getParameter("tableName", request);
        String displayName =  getParameter("tableDisplay", request);
       
        String LinkType = getParameter("LinkType", request) ;
        if(displayName!=null && "@URL:".equals(LinkType)){
        	displayName = GlobalsTool.toChinseChar(displayName) ;
        }
        String popDataType=getParameter("popDataType",request)==null?"":getParameter("popDataType",request);
        request.setAttribute("popDataType", popDataType);
        String reportNumber = getParameter("reportNumber", request);
        request.setAttribute("reportNumber", reportNumber) ;
        String reportName = getParameter("reportName", request);
        request.setAttribute("reportName", reportName) ;
        
        String parentTableName = getParameter("parentTableName", request) ;
        request.setAttribute("parentTableName", parentTableName);
        String fieldName = getParameter("fieldName", request);
        if(fieldName == null){fieldName = "";}
        String selectName = getParameter("selectName", request);
        String propFieldName=getParameter("iniPropField",request);
        String keySearch=request.getParameter("keySearch")==null?"":request.getParameter("keySearch");
        if("@URL:".equals(LinkType)){
        	keySearch = GlobalsTool.toChinseChar(keySearch) ;
        }
        keySearch = GlobalsTool.encodeTextCode(keySearch) ;
        String popPopSelect = request.getParameter("popPop") ;//�Ƿ��ǵ������ڵĵ�������
        request.setAttribute("popPop", popPopSelect) ;
        
        //�����ֵܱ��ID
        String f_brother = this.getParameter("f_brother", request);
        f_brother = f_brother == null ? "" : f_brother;
        request.setAttribute("f_brother", f_brother);

        //�����з���ĵ�����
        String parentCode = getParameter("parentCode", request) == null ? "" :
                            getParameter("parentCode", request);
        if(parentCode.equals("-1")){
        	isRoot = "isRoot";
        	parentCode = "";
        	request.setAttribute("root", "root");
        }
        //һ���������õ�����ʾ����ϸ������
        String mainId = request.getParameter("mainId") == null ? "" :
                        request.getParameter("mainId");
        String mainPop = request.getParameter("mainPop") == null ? "" :
                         request.getParameter("mainPop");
        String isQuote = getParameter("isQuote", request) == null ? "":
        				getParameter("isQuote", request);
        request.setAttribute("isQuote", isQuote) ;
        request.setAttribute("mainId", mainId);
        request.setAttribute("mainPop", mainPop);
        String iniPropField=request.getParameter("iniPropField")==null?"":request.getParameter("iniPropField");//�ڳ����Ե�����ʹ�ã����������ֶ�����
        request.setAttribute("iniPropField",iniPropField);
        String popupType = request.getParameter("popupType") ;
        request.setAttribute("popupType", popupType) ;
//      �Ƿ��ǲ��ò㵯������
        request.setAttribute("popupWin", getParameter("popupWin", request)) ;
        //�ж�ģ������Ƿ���ڣ��粻��������ʾ����Դģ��
        String moduleId = getParameter("MOID", request);
        String moOperation = getParameter("MOOP", request);
        Cookie[] s= request.getCookies();
        
        if ((moduleId == null || moduleId.length() == 0 
        	   || moOperation == null || moOperation.length() == 0)  && tableName != null && !tableName.startsWith("Flow")) {
        	  //��ָ����Դģ��  ������ֻӦ�ڿ����׶γ���
            EchoMessage.error().add(getMessage(request,"scopeRight.msg.noModule")).setRequest(request);
            return getForward(request, mapping, "message");
        }
        
       
        int pageSize = getParameterInt("pageSize", request);
        int pageNo = getParameterInt("pageNo", request);
        String nextClass = request.getParameter("nextClass") ;
        if (pageNo == 0 || "next".equals(nextClass)) {
        	ArrayList<String> listCheck =  (ArrayList<String>) request.getSession().getAttribute("listCheck") ;
        	String backType = request.getParameter("backType") ;
        	if(parentCode==null || parentCode==""){
        		request.getSession().removeAttribute("listCheck") ;
        		request.getSession().removeAttribute("listValue") ;
        	}
        	HashMap advanceMap = (HashMap) request.getSession().getAttribute("advanceMap") ;
        	if(advanceMap!=null){
        		request.getSession().removeAttribute("advanceMap") ;
        	}
        	/*���õ���ʱ ���� ��ѯ����*/
        	if(isQuote!=null && isQuote.length()>0){
        		ArrayList conditionList = (ArrayList) request.getSession().getAttribute("conditionList") ;
        		if(conditionList!=null && conditionList.size()>0 && !"backMain".equals(backType)){
	        		request.getSession().removeAttribute("conditionList") ;
	        	}
	        	ArrayList paramList = (ArrayList)request.getSession().getAttribute("paramList") ;
	        	if(paramList!=null && paramList.size()>0 && !"backMain".equals(backType)){
	        		request.getSession().removeAttribute("paramList") ;
	        	}
        	}
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = GlobalsTool.getPageSize();
        }
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("MOID", moduleId);
        request.setAttribute("MOOP", moOperation);
        request.setAttribute("tableName", tableName);
        request.setAttribute("fieldName", fieldName);
        /*��¼��������Ҫ�����ӵ�ַ*/
        String strAction = "" ;
        if("GET".equals(request.getMethod()) || (mainId!=null && mainId.length()>0)){
        	String strQuery = request.getQueryString() ;
        	if(mainId!=null && mainId.length()>0){
        		strAction = "/UserFunctionAction.do?tableName="+tableName+"&displayName="+GlobalsTool.encode(displayName)
        				   +"&MOID="+moduleId+"&LinkType=@URL:&MOOP="+moOperation+"&"+strQuery;
        	}else{
        		strAction = "/UserFunctionAction.do?"+strQuery ;
        	}
        	strAction = strAction.replace("&selectType=config","") ;
        }else{
        	if("2".equals(isQuote) || "backMain".equals(getParameter("backType", request))){
        		strAction = "/UserFunctionAction.do?operation=22&tableName="+tableName+"&displayName="+GlobalsTool.encode(displayName)
				   +"&MOID="+moduleId+"&LinkType=@URL:&isQuote=2&MOOP="+moOperation+"&selectName="+selectName;
        	}else{
        		strAction = request.getParameter("strAction") ;
        	}
        }
        request.setAttribute("strAction", strAction) ;
        
        ArrayList param = new ArrayList();
        Hashtable allTables = (Hashtable) request.getSession().
                              getServletContext().
                              getAttribute(BaseEnv.TABLE_INFO);

        Hashtable enumeratemap = BaseEnv.enumerationMap;

        DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,
                tableName);
        PopupSelectBean popSelectBean = null;

        //���ҳ�洫����ǵ���ѡ������ƣ���ֱ�Ӵ���
        if (selectName != null && selectName.length() > 0) {
            popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
            if(popSelectBean == null){
           	 	//��ѯʧ��
            	BaseEnv.log.error("UserFunctionAction.popupSelect ������"+selectName+"�����ڻ����");
                EchoMessage.error().add("������"+selectName+"�����ڻ����").
                        setRequest(request);
                return getForward(request, mapping, "message");
            }
        } else {
        	 //���ҳ�洫������ֶ�����������ֶ������ҳ���Ӧ�ĵ���ѡ���
            DBFieldInfoBean fieldInfotemp = null;
            for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
                DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
                if (fieldInfo.getFieldName().equals(fieldName)) {
                    fieldInfotemp = fieldInfo;
                }
            }
            DBFieldInfoBean fieldInfo = fieldInfotemp;
            popSelectBean = fieldInfo.getSelectBean();
            if(popSelectBean == null){
           	 	//��ѯʧ��
            	BaseEnv.log.error("UserFunctionAction.popupSelect ������"+fieldInfo.getInputValue()+"�����ڻ����");
                EchoMessage.error().add("������"+fieldInfo.getInputValue()+"�����ڻ����").
                        setRequest(request);
                return getForward(request, mapping, "message");
            }
            selectName = popSelectBean.getName() ; 
            
            
        }
        

        
	
        request.setAttribute("selectName", selectName);
    	//����ģ�����ȡ�ò�ѯʱ�ķ�ΧȨ��
        ArrayList tabParam = popSelectBean.getTableParams();
        HashMap mainParam = new HashMap();
        String mainHidden = "";
        String mainField = "";
        String value = "";
        String linkValue = "";
        for (int i = 0; i < tabParam.size(); i++) {
            mainField = tabParam.get(i).toString();
            if (mainField.indexOf("@TABLENAME") >= 0) {
                mainField = tableName + "_" +
                            mainField.substring(mainField.indexOf("_") + 1);
            }
            
        	value = request.getParameter(mainField) == null ? "" :
                request.getParameter(mainField);
        	if (request.getParameter("url") != null &&
                    request.getParameter("url").equals("@URL:")) {
        		value = GlobalsTool.toChinseChar(value) ;
        		if(value != null){
        			value = value.replaceAll("@join:amp;", "&");
        		}
        	}

            value = GlobalsTool.encodeTextCode(value) ;
            mainParam.put(mainField, value);
            boolean flag=true;
            for(int j=0;j<popSelectBean.getDisplayField().size();j++){
            	String asName=((PopField)popSelectBean.getDisplayField().get(j)).getAsName().replace(".", "_");
            	if(asName.equals(mainField))flag=false;
            }
            if(flag){
            	mainHidden += "<input type=\"hidden\" name=\"" + mainField +
                    "\" value=\"" + value + "\">";
            }
            linkValue += "&" + mainField + "=" + value;
        }

        request.setAttribute("mainHiddenFields", mainHidden);
        
        
        //------------------------------zxy �൥����
        String detailPopup=null;//��ǰ�л���һ��������ʱ����¼detailPopup,
        String topPopup=null;//��ǰΪ��ϸ������ʱ����¼һ��������
        String topId = null; //һ�����������ص�����
        
        if(popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0){
        	//�ⵯ������һ��������
        	String toDetail = request.getParameter("toDetail");
        	if(!"true".equals(toDetail)){
        		//������ʾ��ϸ���򵯳�һ��������      
        		detailPopup =  popSelectBean.getName();
        		request.setAttribute("detailPopup",detailPopup);
        		popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
        	}else{
        		request.setAttribute("toDetail", toDetail);
        		topId = request.getParameter("topId");
        		request.setAttribute("topId", topId);
        		topPopup=popSelectBean.getTopPopup();
        		request.setAttribute("topPopup", topPopup);
        		
        		request.setAttribute("autoCommit", request.getParameter("autoCommit")); //�Ƿ��Զ��ύ������һ��������ֱ��ȷ������������ϸ
        	}
        }
        if(popSelectBean.getClassCode()!=null && popSelectBean.getClassCode().trim().length()>0  && !"Y".equals(request.getParameter("checkTab"))){
        	//�����Ƿ��������checkʱ����ת��Ŀ¼��ʾ����
        	String mainSrc = request.getRequestURI()+(request.getQueryString()==null?"?checkTab=Y":"?"+request.getQueryString()+"&checkTab=Y");
        	request.setAttribute("mainSrc", mainSrc);
        	request.setAttribute("tableName", popSelectBean.getClassCode().substring(0,popSelectBean.getClassCode().indexOf(".")));
        	request.setAttribute("SysType", popSelectBean.getClassSysType());
        	return getForward(request, mapping, "frameSet");
        }

        String sunCompanyID = getLoginBean(request).getSunCmpClassCode();
        

        String popType = getParameter("popType", request) ;
        request.setAttribute("popType", popType) ;
        String[] str = request.getParameterValues("varKeyId") ;
        if(str!=null && str.length>0 && "checkBox".equals(popSelectBean.getType()) && !"sqty".equals(popType)){
        	ArrayList<String> listCheck =  (ArrayList<String>) request.getSession().getAttribute("listCheck") ;
        	ArrayList<String> listValue =  (ArrayList<String>) request.getSession().getAttribute("listValue") ;
        	if(listCheck==null || listValue==null){
        		listCheck =  new ArrayList<String>() ;
        		listValue = new ArrayList<String>() ;
        	}
        	for(String ss : str){
        		ss = GlobalsTool.replaceSpecLitter(ss);
        		if(ss.length()>0 && !listCheck.contains(ss) && !ss.contains("@hasChild:")){
        			listCheck.add(ss) ;
        			if(ss.indexOf("@#")!=-1)
        				listValue.add(ss.substring(ss.indexOf("@#")+2,ss.length())) ;
        			else
        				listValue.add(ss);
        		}
        	}
        	request.getSession().setAttribute("listCheck", listCheck) ;
        	request.getSession().setAttribute("listValue", listValue) ;
        }
        //ColConfigMgt configMgt = new ColConfigMgt() ;
        //Result rs_col = configMgt.queryConfigExistByTableName(popSelectBean.getName(), "popup") ;
        Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
												   request.getSession().getServletContext().getAttribute("userSettingColConfig") ;
        ArrayList<ColConfigBean> configList = userColConfig.get(tableName+popSelectBean.getName()+"popup") ;

        ArrayList conditions = new ArrayList();
        boolean flagKey=false;;//�ڵ������������������ѯ
        
        //����ģ�����ȡ�ò�ѯʱ�ķ�ΧȨ��
        MOperation mop = (MOperation) (getLoginBean(request).
                                       getOperationMapKeyId().get(moduleId));        
        
        ArrayList scopeRight = new ArrayList();
        if(mop!=null){
        	//zxy ---��Ա��Ͻ��Χ�Ͳ��Ź�Ͻ��Χ������ѯĳ����ʱ�ܲ鿴ĳ�����ĵ��ҿ��ƣ��޸ĺ�ɾ�������⼸����ʱ��Ҳֻ��ѡ�񵽺��⼸����صĵ��������ݡ�
        	//��������ֻҪ�ǵ��������Բ�ѯȨ��Ϊ������ԭ���ķ�ʽ�������Ա��Ͻ��Χ��Ӧ�õ�����ģ�飬�Ͳ������ã������ʱ��������Ա��Ͻ��Χ��û�������ӵģ��������ʱû�з�ΧȨ�޲���
//        	if("dataMove".equals(popDataType)){
//        		 scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
//        	}else if ("update".equals(moOperation)) {
//	            scopeRight.addAll(mop.getScope(MOperation.M_UPDATE));
//	        } else if ("add".equals(moOperation)) {
//	        	scopeRight.addAll(mop.getScope(MOperation.M_ADD));
//	        } else {
	        	scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
	            if(tableInfo!=null&&tableInfo.getPerantTableName()!=null&&!"".equals(tableInfo.getPerantTableName())){
	    	        //������ֵܱ�,����Ĳ�ѯ��ΧȨ�޼�����ϸ��ķ�ΧȨ��
	    	        MOperation mopDet = (MOperation)this.getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?parentTableName="+parentTableName+"&tableName="+tableName);
	    	        if(mopDet!=null){
	    	        	scopeRight.addAll(mopDet.queryScope) ;
	    	        } 
	            }
//	        }   
        	//�������з���Ȩ��
        	scopeRight.addAll(mop.classScope) ;
        	//���빫��Ȩ��
    		ArrayList allScopeList = this.getLoginBean(request).getAllScopeRight();
    		scopeRight.addAll(allScopeList) ;
    	}else{
    		BaseEnv.log.info("������δ����mop;moduleId="+moduleId);
    	}


        if (request.getParameter("url") != null && request.getParameter("url").equals("@URL:")&&popSelectBean.isKeySearch()){
        	flagKey=true;
        }
        for (int i = 0; i < popSelectBean.getDisplayField().size(); i++) {
            PopField fv = (PopField) (popSelectBean.getDisplayField()).get(i);
            if(0==fv.getSearchType()){
            	continue ;
            }
            
            //--------------��ʾȨ���ж�
            boolean viewRight = true;
        	if(fv.asName != null && fv.asName.length() > 0){   
        		String sd = fv.asName;
        		sd = tableName ==null?sd.replace("@TABLENAME", ""):sd.replace("@TABLENAME", tableName) ;
        		viewRight = DynDBManager.getViewRight("", sd,scopeRight, "popselect",loginBean);
        	}else{
            	ArrayList<String[]> list = ConfigParse.getTableFieldByReportField(fv.fieldName) ;
				for (int j1 = 0; j1 < list.size(); j1++) {
					String[] obj = list.get(j1);
					viewRight = DynDBManager.getViewRight("",obj[0]+"."+obj[1],scopeRight, "popselect",loginBean);
					if (!viewRight) {
						break;
					}
				} 
        	}
            if (!viewRight) {
                continue;
            }
            
            //ArrayList<ColConfigBean> configList = userColConfig.get(tableName+"popup") ; ;
            if(configList!=null && configList.size()>0){ //��������
	            boolean isExist = false ;
	            for(int j=0;j<configList.size();j++){
	            	ColConfigBean configBean = configList.get(j) ;
	            	String popTable = fv.asName ;
        			popTable = popTable.replace("@TABLENAME", tableName) ;
        			
	            	if(fv.getAsName().equals(configBean.getColName())){
	            		isExist = true ;
	            	}
	            }
	            if(isExist){
	            	  //�ҳ���ѯ����
		            if (fv.getSearchType() == PopField.SEARCH_EQUAL) {
		            	  //0�����ԣ�1����.�ֶ�����2ֵ��3����(0������1���ڣ�2ö��)��4inputValue
		                String[] cs = new String[5];
		                DBFieldInfoBean popfieldInfo = DynDBManager.cloneObject(getField(allTables,fv.getAsName()));
		                if(popfieldInfo==null){
	                    	String asName=fv.getAsName();
	                    	if(tableName!=null
	                				&& tableName.length()>0 && !"null".equals(tableName)){
	                    		asName = asName.replace("@TABLENAME", tableName);
	                    	}
	                    	popfieldInfo= DynDBManager.cloneObject(getFieldInfo(allTables,popSelectBean.getTableName(asName), popSelectBean.getFieldName(asName)));
	                    }
		                KRLanguage krLan=null;
                        if(fv.getDisplay()!=null){
                        	DBFieldInfoBean bean;
                        	if(tableName!=null){
                        		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                        	}else{
                        		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                        	}
                        	if(bean!=null){
                        		krLan=bean.getDisplay();
                        	}
                        }
		                if (popfieldInfo == null) {
		                    continue;
		                } else {
		                	/*ģ�鵯��������������*/
		                    
		                    try {
		                    	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
		                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
		                    		cs[0] = fv.getDisplay();
		                    	}else{
		                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
		                    			krLan.get(getLocale(request).toString());
		                    	}
		                    } catch (Exception ex) {
		                        cs[0] = popfieldInfo.getFieldName();
		                    }
		                }
		                cs[1] = fv.getAsName();
		                String temp = getParameter(fv.getAsName(), request);
		                if (temp != null && temp.trim().length() > 0) {
		                    if (request.getParameter("url") != null &&
		                        request.getParameter("url").equals("@URL:")) {
		                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
		                    }
		                }
		                temp=PopGetDefaultValue(fv,temp,request);
		                temp = GlobalsTool.encodeTextCode(temp) ;
		                
		                if (temp != null && temp.trim().length() > 0) {
		                    param.add(new String[] {fv.getFieldName(), temp, "="});
		                    cs[2] = temp;
		                }
		                cs[3] = "0";
		                if(fv.getPopSelect()!=null && fv.getPopSelect().length()>0){
		                	cs[3] = "3";
		                	cs[4] = fv.getPopSelect() ;
		                }else if (popfieldInfo != null) {
		                    if (popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_LONG ||
		                        popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_SHORT) {
		                        cs[3] = "1";
		                    } else if (popfieldInfo.getInputTypeOld() ==
		                               DBFieldInfoBean.INPUT_ENUMERATE) {
		                        cs[3] = "2";
		                        cs[4] = popfieldInfo.getRefEnumerationName();
		                    }
		                }
		                conditions.add(cs);
		            }else if (fv.getSearchType() == PopField.SEARCH_MORE) {
		            	  //0�����ԣ�1����.�ֶ�����2ֵ��3����(0������1���ڣ�2ö�٣�3������4��ѡ��)��4inputValue
		                String[] cs = new String[5];
		                DBFieldInfoBean popfieldInfo = DynDBManager.cloneObject(getField(allTables,fv.getAsName()));
		                
		                KRLanguage krLan=null;
                        if(fv.getDisplay()!=null){
                        	DBFieldInfoBean bean;
                        	if(tableName!=null){
                        		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                        	}else{
                        		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                        	}
                        	if(bean!=null){
                        		krLan=bean.getDisplay();
                        	}
                        }
		                if (popfieldInfo == null) {
		                    continue;
		                } else {
		                	/*ģ�鵯��������������*/
		                    
		                    try {
		                    	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
		                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
		                    		cs[0] = fv.getDisplay();
		                    	}else{
		                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
		                    			krLan.get(getLocale(request).toString());
		                    	}
		                    } catch (Exception ex) {
		                        cs[0] = popfieldInfo.getFieldName();
		                    }
		                }
		                cs[1] = fv.getAsName();
		                String temp = getParameter(fv.getAsName(), request);
		                if (temp != null && temp.trim().length() > 0) {
		                    if (request.getParameter("url") != null &&
		                        request.getParameter("url").equals("@URL:")) {
		                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
		                    }
		                }
		                temp=PopGetDefaultValue(fv,temp,request);
		                temp = GlobalsTool.encodeTextCode(temp) ;
		                
		                if (temp != null && temp.trim().length() > 0) {		                    
		                    param.add(new String[] {fv.getFieldName(), temp, ">"});
		                    cs[2] = temp;
		                }
		                cs[3] = "0";
		                if(fv.inputType!=null&&fv.inputType.equals("checkBox") ){
		                	cs[3] = "4";
		                	cs[4] = fv.inputValue ;
		                }else if(fv.getPopSelect()!=null && fv.getPopSelect().length()>0){
		                	cs[3] = "3";
		                	cs[4] = fv.getPopSelect() ;
		                }else if (popfieldInfo != null) {
		                    if (popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_LONG ||
		                        popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_SHORT) {
		                        cs[3] = "1";
		                    } else if (popfieldInfo.getInputTypeOld() ==
		                               DBFieldInfoBean.INPUT_ENUMERATE) {
		                        cs[3] = "2";
		                        cs[4] = popfieldInfo.getRefEnumerationName();
		                    }
		                }
		                conditions.add(cs);
		            } else if (fv.getSearchType() == PopField.SEARCH_MATCH||fv.getSearchType()==PopField.SEARCH_MATCHL||fv.getSearchType()==PopField.SEARCH_MATCHR) {
		            	  //0�����ԣ�1����.�ֶ�����2ֵ
		                String[] cs = new String[5];
		                DBFieldInfoBean popfieldInfo = DynDBManager.cloneObject(getField(allTables,fv.getAsName()));
		                
		                KRLanguage krLan=null;
                        if(fv.getDisplay()!=null){
                        	DBFieldInfoBean bean;
                        	if(tableName!=null){
                        		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                        	}else{
                        		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                        	}
                        	if(bean!=null){
                        		krLan=bean.getDisplay();
                        	}
                        }
		                if (popfieldInfo == null) {
		                    continue;
		                } else {
		                	
		                    try {
		                    	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
		                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
		                    		cs[0] = fv.getDisplay();
		                    	}else{
		                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
		                    			krLan.get(getLocale(request).toString());
		                    	}
		                    } catch (Exception ex) {
		                        cs[0] = popfieldInfo.getFieldName();
		                    }
		                }

		                cs[1] = fv.getAsName();
		                String temp = getParameter(fv.getAsName(), request);
		                if (temp != null && temp.trim().length() > 0) {
		                    if (request.getParameter("url") != null &&
		                        request.getParameter("url").equals("@URL:")) {
		                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
		                    }
		                }
		                temp=PopGetDefaultValue(fv,temp,request);
		                temp = GlobalsTool.encodeTextCode(temp) ;
		                
		                if (temp != null && temp.trim().length() > 0) {
		                    if(fv.getSearchType()==PopField.SEARCH_MATCH){
		                    	param.add(new String[] {fv.getFieldName(),
		                                "%" + temp + "%", "like"});
		                    }else if(fv.getSearchType()==PopField.SEARCH_MATCHL){
		                    	param.add(new String[] {fv.getFieldName(),
		                                temp + "%", "like"});
		                    }else if(fv.getSearchType()==PopField.SEARCH_MATCHR){
		                    	param.add(new String[] {fv.getFieldName(),
		                                "%" + temp , "like"});
		                    }
		                    cs[2] = temp;
		                }
		                
		                cs[3] = "0";
		                if(fv.getPopSelect()!=null && fv.getPopSelect().length()>0){
		                	cs[3] = "3";
		                	cs[4] = fv.getPopSelect() ;
		                }else if (popfieldInfo != null) {
		                    if (popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_LONG ||
		                        popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_SHORT) {
		                        cs[3] = "1";
		                    } else if (popfieldInfo.getInputTypeOld() ==
		                               DBFieldInfoBean.INPUT_ENUMERATE) {
		                        cs[3] = "2";
		                        cs[4] = popfieldInfo.getRefEnumerationName();
		                    }
		                }
		                
		                conditions.add(cs);
		            } else if (fv.getSearchType() == PopField.SEARCH_SCOPE) {
		            	 //0�����ԣ�1����.�ֶ�����2ֵ
		                DBFieldInfoBean popfieldInfo = DynDBManager.cloneObject(getField(allTables,fv.getAsName()));

		                //-----��ʼ
		                String[] cs = new String[5];
		                KRLanguage krLan=null;
                        if(fv.getDisplay()!=null){
                        	DBFieldInfoBean bean;
                        	if(tableName!=null){
                        		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                        	}else{
                        		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                        	}
                        	if(bean!=null){
                        		krLan=bean.getDisplay();
                        	}
                        }
		                if (popfieldInfo == null) {
		                    continue;
		                } else {
		                	
		                    try {
		                    	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
		                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
		                    		cs[0] = fv.getDisplay();
		                    	}else{
		                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
		                    			krLan.get(getLocale(request).toString());
		                    	}
		                        cs[0] =cs[0]+ "&ge;";
		                    } catch (Exception ex) {
		                        cs[0] = popfieldInfo.getFieldName() + "&ge;";
		                    }
		                }

		                cs[1] = fv.getAsName() + "_S";
		                String temp = getParameter(fv.getAsName() + "_S", request);
		                if (temp != null && temp.trim().length() > 0) {
		                    if (request.getParameter("url") != null &&
		                        request.getParameter("url").equals("@URL:")) {
		                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
		                    }
		                }
		                temp=PopGetDefaultValue(fv,temp,request);
		                temp = GlobalsTool.encodeTextCode(temp) ;
		                
		                if (temp != null && temp.trim().length() > 0) {		                    
		                    param.add(new String[] {fv.getFieldName(),
		                              temp, ">="});
		                    cs[2] = temp;
		                }
		                cs[3] = "0";
		                if (popfieldInfo != null) {
		                    if (popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_LONG ||
		                        popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_SHORT) {
		                        cs[3] = "1";
		                    }
		                }
		                conditions.add(cs);
		                //-----����
		                cs = new String[5];
		                try {
		                	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
	                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
	                    		cs[0] = fv.getDisplay();
	                    	}else{
	                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
	                    			krLan.get(getLocale(request).toString());
	                    	}
		                    cs[0] = cs[0]+ "&le;";
		                } catch (Exception ex) {
		                    cs[0] = popfieldInfo.getFieldName() + "&le;";
		                }

		                cs[1] = fv.getAsName() + "_E";
		                temp = getParameter(fv.getAsName() + "_E", request);		                
		                if (temp != null && temp.trim().length() > 0) {
		                    if (request.getParameter("url") != null &&
		                        request.getParameter("url").equals("@URL:")) {
		                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
		                    }
		                }
		                temp=PopGetDefaultValue(fv,temp,request);
		                temp = GlobalsTool.encodeTextCode(temp) ;
		                if (temp != null && temp.trim().length() > 0) {
			                    param.add(new String[] {fv.getFieldName(),
			                              temp, "<="});
			                    cs[2] = temp;
		                }
		                cs[3] = "0";
		                if (popfieldInfo != null) {
		                    if (popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_LONG ||
		                        popfieldInfo.getFieldType() ==
		                        DBFieldInfoBean.FIELD_DATE_SHORT) {
		                        cs[3] = "1";
		                    }
		                }
		                conditions.add(cs);
		            }
	            }
            }else{ //��������
            	//�ҳ���ѯ����
	            if (fv.getSearchType() == PopField.SEARCH_EQUAL) {
	            	 //0�����ԣ�1����.�ֶ�����2ֵ��3����(0������1���ڣ�2ö�٣�3������4��ѡ��)��4inputValue
	                String[] cs = new String[5];

	                DBFieldInfoBean popfieldInfo = DynDBManager.cloneObject(getField(allTables,fv.getAsName()));
	                
	                
	                KRLanguage krLan=null;
                    if(fv.getDisplay()!=null){
                    	DBFieldInfoBean bean;
                    	if(tableName!=null){
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                    	}else{
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                    	}
                    	if(bean!=null){
                    		krLan=bean.getDisplay();
                    	}
                    }
	                
	                if (popfieldInfo == null) {
	                    continue;
	                } else {
	                	 
	                    try {
	                    	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
	                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
	                    		cs[0] = fv.getDisplay();
	                    	}else{
	                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
	                    			krLan.get(getLocale(request).toString());
	                    	}
	                    } catch (Exception ex) {
	                        cs[0] = popfieldInfo.getFieldName();
	                    }
	                }
	                cs[1] = fv.getAsName();
	                String temp = getParameter(fv.getAsName(), request);
	                if (temp != null && temp.trim().length() > 0) {
	                    if (request.getParameter("url") != null &&
	                        request.getParameter("url").equals("@URL:")) {
	                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
	                    }
	                }
	                temp=PopGetDefaultValue(fv,temp,request);
	                temp = GlobalsTool.encodeTextCode(temp) ;
	                
	                if (temp != null && temp.trim().length() > 0) {
		                    param.add(new String[] {fv.getFieldName(), temp, "="});
		                    cs[2] = temp;
	                }
	                cs[3] = "0";
	                if(fv.getPopSelect()!=null && fv.getPopSelect().length()>0){
	                	cs[3] = "3";
	                	cs[4] = fv.getPopSelect() ;
	                }else if (popfieldInfo != null) {
	                    if (popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_LONG ||
	                        popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_SHORT) {
	                        cs[3] = "1";
	                    } else if (popfieldInfo.getInputTypeOld() ==
	                               DBFieldInfoBean.INPUT_ENUMERATE) {
	                        cs[3] = "2";
	                        cs[4] = popfieldInfo.getRefEnumerationName();
	                    }
	                }
	                conditions.add(cs);
	            }else if (fv.getSearchType() == PopField.SEARCH_MORE ) {
	            	 //0�����ԣ�1����.�ֶ�����2ֵ��3����(0������1���ڣ�2ö�٣�3������4��ѡ��)��4inputValue
	                String[] cs = new String[5];

	                DBFieldInfoBean popfieldInfo = DynDBManager.cloneObject(getField(allTables,fv.getAsName()));
	                
	                
	                KRLanguage krLan=null;
                    if(fv.getDisplay()!=null){
                    	DBFieldInfoBean bean;
                    	if(tableName!=null){
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                    	}else{
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                    	}
                    	if(bean!=null){
                    		krLan=bean.getDisplay();
                    	}
                    }
	                
	                if (popfieldInfo == null) {
	                    continue;
	                } else {
	                	
	                    try {
	                    	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
	                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
	                    		cs[0] = fv.getDisplay();
	                    	}else{
	                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
	                    			krLan.get(getLocale(request).toString());
	                    	}
	                    } catch (Exception ex) {
	                        cs[0] = popfieldInfo.getFieldName();
	                    }
	                }
	                cs[1] = fv.getAsName();
	                String temp = getParameter(fv.getAsName(), request);
	                if (temp != null && temp.trim().length() > 0) {
	                    if (request.getParameter ("url") != null &&
	                        request.getParameter("url").equals("@URL:")) {
	                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
	                    }
	                }
	                temp=PopGetDefaultValue(fv,temp,request);
	                temp = GlobalsTool.encodeTextCode(temp) ;
	                
	                if (temp != null && temp.trim().length() > 0) {
		                    param.add(new String[] {fv.getFieldName(), temp, ">"});
		                    cs[2] = temp;
	                }
	                cs[3] = "0";
	                if(fv.getPopSelect()!=null && fv.getPopSelect().length()>0){
	                	cs[3] = "3";
	                	cs[4] = fv.getPopSelect() ;
	                }else if(fv.inputType!=null&&fv.inputType.equals("checkBox")){
	                	cs[3] = "4";
	                	cs[4] = fv.inputValue;
	                }else if (popfieldInfo != null) {
	                    if (popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_LONG ||
	                        popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_SHORT) {
	                        cs[3] = "1";
	                    } else if (popfieldInfo.getInputTypeOld() ==
	                               DBFieldInfoBean.INPUT_ENUMERATE) {
	                        cs[3] = "2";
	                        cs[4] = popfieldInfo.getRefEnumerationName();
	                    }
	                }
	                conditions.add(cs);
	            } else if (fv.getSearchType()==PopField.SEARCH_INPUT||fv.getSearchType() == PopField.SEARCH_MATCH||fv.getSearchType()==PopField.SEARCH_MATCHL||fv.getSearchType()==PopField.SEARCH_MATCHR) {
	            	 //0�����ԣ�1����.�ֶ�����2ֵ
	                String[] cs = new String[5];
	                DBFieldInfoBean popfieldInfo = DynDBManager.cloneObject(getField(allTables,fv.getAsName()));
	                
                    
	                KRLanguage krLan=null;
                    if(fv.getDisplay()!=null){
                    	DBFieldInfoBean bean;
                    	if(tableName!=null){
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                    	}else{
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                    	}
                    	if(bean!=null){
                    		krLan=bean.getDisplay();
                    	}
                    }
	                
	                if (popfieldInfo == null) {
	                    continue;
	                } else {
	                	
	                    try {
	                    	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
	                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
	                    		cs[0] = fv.getDisplay();
	                    	}else{
	                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
	                    			krLan.get(getLocale(request).toString());
	                    	}
	                    } catch (Exception ex) {
	                        cs[0] = popfieldInfo.getFieldName();
	                    }
	                }

	                cs[1] = fv.getAsName();
	                String temp = getParameter(fv.getAsName(), request);
	                if (temp != null && temp.trim().length() > 0) {
	                    if (request.getParameter("url") != null &&
	                        request.getParameter("url").equals("@URL:")) {
	                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
	                    }
	                }
	                temp=PopGetDefaultValue(fv,temp,request);
	                temp = GlobalsTool.encodeTextCode(temp) ;
	                
	                if (temp != null && temp.trim().length() > 0) {	   
	                    if(fv.getSearchType()==PopField.SEARCH_MATCH){
	                    	param.add(new String[] {fv.getFieldName(),
	                                "%" + temp + "%", "like"});
	                    }else if(fv.getSearchType()==PopField.SEARCH_MATCHL){
	                    	param.add(new String[] {fv.getFieldName(),
	                                temp + "%", "like"});
	                    }else if(fv.getSearchType()==PopField.SEARCH_MATCHR){
	                    	param.add(new String[] {fv.getFieldName(),
	                                "%" + temp , "like"});
	                    }else if(fv.getSearchType()==PopField.SEARCH_INPUT){
	                    	param.add(new String[] {fv.getFieldName(),
	                                temp , ""});
	                    }
	                    cs[2] = temp;
	                }
	                
	                cs[3] = "0";
	                if(fv.getPopSelect()!=null && fv.getPopSelect().length()>0){
	                	cs[3] = "3";
	                	cs[4] = fv.getPopSelect() ;
	                }else if (popfieldInfo != null) {
	                    if (popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_LONG ||
	                        popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_SHORT) {
	                        cs[3] = "1";
	                    } else if (popfieldInfo.getInputTypeOld() ==
	                               DBFieldInfoBean.INPUT_ENUMERATE) {
	                        cs[3] = "2";
	                        cs[4] = popfieldInfo.getRefEnumerationName();
	                    }
	                }
	                
	                conditions.add(cs);
	            } else if (fv.getSearchType() == PopField.SEARCH_SCOPE) {
	            	 //0�����ԣ�1����.�ֶ�����2ֵ
	                DBFieldInfoBean popfieldInfo = DynDBManager.cloneObject(getField(allTables,fv.getAsName()));

	                //-----��ʼ
	                String[] cs = new String[5];
	                
	                KRLanguage krLan=null;
                    if(fv.getDisplay()!=null){
                    	DBFieldInfoBean bean;
                    	if(tableName!=null){
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                    	}else{
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                    	}
                    	if(bean!=null){
                    		krLan=bean.getDisplay();
                    	}
                    }
	                if (popfieldInfo == null) {
	                    continue;
	                } else {
	                	
	                    try {
	                    	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
	                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
	                    		cs[0] = fv.getDisplay();
	                    	}else{
	                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
	                    			krLan.get(getLocale(request).toString());
	                    	}
	                        cs[0] =cs[0]+ "&ge;";
	                    } catch (Exception ex) {
	                        cs[0] = popfieldInfo.getFieldName() + "&ge;";
	                    }
	                }

	                cs[1] = fv.getAsName() + "_S";
	                String temp = getParameter(fv.getAsName() + "_S", request);
	                if (temp != null && temp.trim().length() > 0) {
	                    if (request.getParameter("url") != null &&
	                        request.getParameter("url").equals("@URL:")) {
	                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
	                    }
	                }
	                temp=PopGetDefaultValue(fv,temp,request);
	                temp = GlobalsTool.encodeTextCode(temp) ;
	                
	                if (temp != null && temp.trim().length() > 0) {
		                    param.add(new String[] {fv.getFieldName(),
		                              temp, ">="});
		                    cs[2] = temp;
	                }
	                cs[3] = "0";
	                if (popfieldInfo != null) {
	                    if (popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_LONG ||
	                        popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_SHORT) {
	                        cs[3] = "1";
	                    }
	                }
	                conditions.add(cs);
	                //-----����
	                cs = new String[5];
	                try {
	                	if(fv.getDisplay() != null && fv.getDisplay().length()>0 && fv.getDisplay().indexOf(".")==-1){
                    		//�������ֶ�display��Ϊ����û��.�����������ֶ���ʽ����ֱ����д��������
                    		cs[0] = fv.getDisplay();
                    	}else{
                    		cs[0] =krLan==null? popfieldInfo.getDisplay().get(getLocale(request).toString()).toString():
                    			krLan.get(getLocale(request).toString());
                    	}
	                    cs[0] = cs[0] + "&le;";
	                } catch (Exception ex) {
	                    cs[0] = popfieldInfo.getFieldName() + "&le;";
	                }

	                cs[1] = fv.getAsName() + "_E";
	                temp = getParameter(fv.getAsName() + "_E", request);
	                if (temp != null && temp.trim().length() > 0) {
	                    if (request.getParameter("url") != null &&
	                        request.getParameter("url").equals("@URL:")) {
	                        temp = new String(temp.getBytes("iso-8859-1"),"UTF-8");
	                    }
	                }
	                temp=PopGetDefaultValue(fv,temp,request);
	                temp = GlobalsTool.encodeTextCode(temp) ;
	                
	                if (temp != null && temp.trim().length() > 0) {
		                    param.add(new String[] {fv.getFieldName(),
		                              temp, "<="});
		                    cs[2] = temp;
	                }
	                cs[3] = "0";
	                if (popfieldInfo != null) {
	                    if (popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_LONG ||
	                        popfieldInfo.getFieldType() ==
	                        DBFieldInfoBean.FIELD_DATE_SHORT) {
	                        cs[3] = "1";
	                    }
	                }
	                conditions.add(cs);
	            }
            }
        }
	        
        request.setAttribute("keySearch",keySearch);
        
        /*��� ���õ���ʱ ��������ʱ ������ʧ����*/
        if((mainId==null || mainId.length()==0) && !"backMain".equals(getParameter("backType", request))){
        	request.getSession().setAttribute("paramList", param) ;
        	request.getSession().setAttribute("mainParam", mainParam) ;
        	request.getSession().setAttribute("conditionList", conditions) ;
        }
        
        if("backMain".equals(getParameter("backType", request))){
        	ArrayList paramList = (ArrayList) request.getSession().getAttribute("paramList") ;
        	ArrayList conditionList = (ArrayList) request.getSession().getAttribute("conditionList") ;
        	mainParam = (HashMap) request.getSession().getAttribute("mainParam") ;
        	param = paramList ;
        	conditions = conditionList ;
        }
        
        
        /*�Ƿ���������*/
        String selectType = getParameter("selectType", request) ;
        Result rs =null;
        if(!"isRoot".equals(isRoot) && !"config".equals(selectType) 
        			&& ((param!=null&&param.size()>0)||!"defNoShow".equals(popSelectBean.getShowType()))){
            if("next".equals(nextClass)){
            	param = new ArrayList() ;
            	keySearch = "" ;
            	for(int i=0;i<conditions.size();i++){
            		String[] cond = (String[]) conditions.get(i) ;
            		cond[2] = "" ;
            	}
            }
            while(true){
            	rs = dbmgt.popSelect(propFieldName,tableName, popSelectBean, allTables, param,
                                    mainParam, scopeRight,
                                    pageNo, pageSize, parentCode, mainId,
                                    getLoginBean(request).getId(), sunCompanyID,this.getLocale(request),keySearch,request,request.getParameter("src"),PopupSelectBean.DEFAULTRULE,topId);
            	String isCatalog="";
            	String classCode="";
            	if(request.getParameter("src")!=null&&request.getParameter("src").equals("menu")&& rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&popSelectBean.getClassCode()!=null&&popSelectBean.getClassCode().length()>0&&rs.getRealTotal()==1){
	            	Object[] os = (Object[]) ((List) rs.retVal).get(0);
	            	for(int j = 0; j < popSelectBean.getAllFields().size(); j++){
	                    PopField fv2 = (PopField) popSelectBean.getAllFields().get(j);
	                    if (fv2.getFieldName().equals(popSelectBean.getClassCode())) {
	                    	classCode = os[j].toString();
	                    	isCatalog =os[os.length-1].toString();
	                    }
	                }
            	}
            	
	            if(isCatalog.equals("1")){
            		parentCode=classCode;
            	}else{
            		break ;
            	}
	            if(keySearch!=null && keySearch.length()>0){
	            	break ;
	            }
            }
            
            if(rs.getRealTotal()==pageSize&&rs.getTotalPage()==0){//��ѯ������������=һҳ����������ô�ٶ�����һҳ
				rs.setTotalPage(pageNo+1);
			}else{
				rs.setTotalPage(pageNo);
			}
            rs.setRealTotal(0);//��������ʾ��ҳ��
        }else{
        	//������defNoShow ��û�в�������ʾ����
        	rs=new Result();
        	rs.setRetVal(new ArrayList());
        }

        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	//������defNoShow ��û�в�������ʾ����
        	 /**
             * ������
             * type  :
             * int   ������
             * float ������
             * date  ��������
             * String ������
             * nosort  �������ͣ�����ͼƬ����Ť����ѡ��������� ����������
             * other    �������ͣ���������
             * function addCols(name,width,type){
             */
        	ArrayList<PopField> oldCols = new ArrayList<PopField>() ;
        	ArrayList<PopField> newCols = new ArrayList<PopField>() ;
            ArrayList cols = new ArrayList();
            ArrayList SelCols=new ArrayList();
            if(configList!=null && configList.size()>0){//�û��������Զ�������
            	for(int j=0;j<configList.size();j++){
            		ColConfigBean configBean = configList.get(j) ;
            		for (int i = 0; i < popSelectBean.getDisplayField().size(); i++) {
            			PopField fv = (PopField) popSelectBean.getDisplayField().get(i);
            			
            			if(configBean.getColName().equals(fv.getAsName().replace('+', ' ')) ){
            				//�ҵ���Ӧ���ֶ�
            				String asName= fv.getAsName() ;  
            				
            				 if(asName.contains("tblCompany1")||asName.contains("tblCompany2") || asName.contains("tblCompany3")){
                             	String newTableName=asName.substring(0,asName.lastIndexOf("."));
                             	asName=asName.replace(newTableName, "tblCompany");
                             }
                            DBFieldInfoBean fi = DynDBManager.cloneObject(getFieldInfo(allTables,popSelectBean.getTableName(asName), popSelectBean.getFieldName(asName)));
                            
                            KRLanguage krLan=null;
                            if(fv.getDisplay()!=null){
                            	DBFieldInfoBean bean;
                            	if(tableName!=null){
                            		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                            	}else{
                            		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                            	}
                            	if(bean!=null){
                            		krLan=bean.getDisplay();
                            	}
                            }
                            
                            //--------------��ʾȨ���ж�
                            boolean viewRight = true;
                        	if(fv.asName != null && fv.asName.length() > 0){   
                        		String sd = fv.asName;
                        		sd = tableName ==null?sd.replace("@TABLENAME", ""):sd.replace("@TABLENAME", tableName) ;
                        		viewRight = DynDBManager.getViewRight("", sd,scopeRight, "popselect",loginBean);
                        	}else{
                            	ArrayList<String[]> list = ConfigParse.getTableFieldByReportField(fv.fieldName) ;
                				for (int j1 = 0; j1 < list.size(); j1++) {
                					String[] obj = list.get(j1);
                					viewRight = DynDBManager.getViewRight("",obj[0]+"."+obj[1],scopeRight, "popselect",loginBean);
                					if (!viewRight) {
                						break;
                					}
                				} 
                        	}
                            if (!viewRight) {
                                continue;
                            }


                            String[] ss = new String[4];
                            if(krLan==null && fi!=null)krLan=fi.getDisplay();
                            Object fd = krLan == null ? "" :krLan.get(getLocale(request).toString());
                            if(fv.display !=null && !fv.display.equals("") && fv.display.indexOf(".") == -1){
                            	ss[0] = fv.display;
                            }else{
                            	ss[0] = fd == null ? fi.getFieldName() : fd.toString();
                            }
//                            if(fv.display!=null){
//                            	ss[3] = fv.display.replace("@TABLENAME", tableName) ;
//                            }else{
//                            	ss[3] = fv.fieldName ;
//                            }
                            ss[3] = fv.asName;
                            String strValue = String.valueOf(GlobalsTool.getFieldWidth(tableName, ss[3])) ;
                            if(!"0".equals(strValue)){
                            	ss[1] = Integer.parseInt(strValue)+"";
                            }else{
                            	ss[1] = fv.width + "" ;
                            } 
	                        ss[2] = DBFieldInfoBean.getFieldTypeString(fi);
                            if (fi != null && fi.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
                                ss[2] = "other";
                            }else if (fi != null && fi.getFieldType() == DBFieldInfoBean.FIELD_PIC) {
                            	ss[2] = "img";
                            }

                            cols.add(ss);
                            newCols.add(fv);
                            if(popSelectBean.getType().equals("multiSeleToRow")&&fv.getMarker().equals("true")){
                            	SelCols.add(ss);
                            }
            			}
            		}
            	}
            }else{  //�û�û�н����Զ�������
            	for (int i = 0; i < popSelectBean.getDisplayField().size(); i++) {
                    PopField fv = (PopField) popSelectBean.getDisplayField().get(i);
                    
                    //�ҵ���Ӧ���ֶ�
                    String asName= fv.getAsName() ;
                    if(asName.contains("tblCompany1")||asName.contains("tblCompany2") || asName.contains("tblCompany3")){
                    	String newTableName=asName.substring(0,asName.lastIndexOf("."));
                    	asName=asName.replace(newTableName, "tblCompany");
                    }
                    DBFieldInfoBean fi= DynDBManager.cloneObject(getFieldInfo(allTables,popSelectBean.getTableName(asName), popSelectBean.getFieldName(asName)));
                    
                    KRLanguage krLan=null;
                    if(fv.getDisplay()!=null){
                    	DBFieldInfoBean bean;
                    	if(tableName!=null){                    		
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                    	}else{
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                    	}
                    	if(bean!=null){
                    		krLan=bean.getDisplay();
                    	}
                    }
                    
                   
                    //--------------��ʾȨ���ж�
                    boolean viewRight = true;
                	if(fv.asName != null && fv.asName.length() > 0){    
                		String sd = fv.asName;
                		if(tableName!=null)
                		sd = sd.replace("@TABLENAME", tableName) ;
                		viewRight = DynDBManager.getViewRight("", sd,scopeRight, "popselect",loginBean);
                	}else{
                    	ArrayList<String[]> list = ConfigParse.getTableFieldByReportField(fv.fieldName) ;
						for (int j = 0; j < list.size(); j++) {
							String[] obj = list.get(j);
							viewRight = DynDBManager.getViewRight("",obj[0]+"."+obj[1],scopeRight, "popselect",loginBean);
							if (!viewRight) {
								break;
							}
						} 
                	}
                    if (!viewRight) {
                        continue;
                    }

                    String[] ss = new String[4];
                    if(krLan==null && fi != null )krLan=fi.getDisplay();
                    Object fd = krLan == null ? "" :krLan.get(getLocale(request).toString());
                    if(fv.display !=null && !fv.display.equals("") && fv.display.indexOf(".") == -1){
                    	ss[0] = fv.display; //ֱ����ʾdisplay����
                    }else{
                    	ss[0] = fd == null ? fi.getFieldName() : fd.toString();
                    }
//                    if(fv.display!=null && tableName!=null){
//                    	ss[3] = fv.display.replace("@TABLENAME", tableName) ;
//                    }else{
//                    	ss[3] = fv.fieldName ;
//                    }
                    ss[3] = fv.asName;
                    String strValue = String.valueOf(GlobalsTool.getFieldWidth(tableName, fv.fieldName)) ;
                    if(!"0".equals(strValue)){
                    	ss[1] = Integer.parseInt(strValue)+ "";
                    }else{
                    	ss[1] = fv.width + "" ;
                    }
                    ss[2] = DBFieldInfoBean.getFieldTypeString(fi);
                    if (fi!=null && fi.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
                        ss[2] = "other";
                    }else if (fi!=null && fi.getFieldType() == DBFieldInfoBean.FIELD_PIC) {
                    	ss[2] = "img";
                    }

                    cols.add(ss);
                    newCols.add(fv) ;
                    if(popSelectBean.getType().equals("multiSeleToRow")&&fv.getMarker().equals("true")){
                    	SelCols.add(ss);
                    }
                }
            }

            for (int i = 0; i < popSelectBean.getDisplayField2().size(); i++) {
    			PopField fv = (PopField) popSelectBean.getDisplayField2().get(i);
    			oldCols.add(fv) ;
    		}
            
            
            request.setAttribute("popupName", popSelectBean.getName()) ;
            request.setAttribute("conditions", conditions);
            request.setAttribute("cols", cols);
            request.setAttribute("oldCols", oldCols) ;
            request.setAttribute("newCols", newCols) ; 
            request.setAttribute("SelCols", SelCols);
            
            /**
             * �����е����������ڻ���--zxy
             * 
             */
            String returnCols="";
            for (int j = 0; j < popSelectBean.getReturnFields().size(); j++) {
                PopField fv = (PopField) popSelectBean.getReturnFields(). get(j);
                if("list".equals(popupType)){
                	if(true==fv.parentDisplay){
                		for (int k = 0; k < popSelectBean.getAllFields().size(); k++) {
	                        PopField fv2 = (PopField) popSelectBean.getAllFields().get(k);
	                        if (fv.getFieldName().equals(fv2.getFieldName())) {
	                        	if(fv.type==1){
	                        		String fvn = fv.parentName;
	                        		if(fvn == null || fvn.length() == 0){
		                        		fvn = fv.fieldName;	                        		
		                        	}
		                        	returnCols += fvn + ";";
	                        	}else{
		                        	String fvn =  fv.asName;
		                        	returnCols += fvn + ";";
	                        	}
	                        	break ;
	                        }
                		}
                		break ;
                	}
                }else{
                    for (int k = 0; k < popSelectBean.getAllFields().size(); k++) {
                        PopField fv2 = (PopField) popSelectBean.getAllFields().get(k);
                        if (fv.getFieldName().equals(fv2.getFieldName())) {
                        	if(fv.type==1){
                        		String fvn = fv.parentName;
                        		if(fvn == null || fvn.length() == 0){
	                        		fvn = fv.fieldName;	                        		
	                        	}
	                        	returnCols += fvn + ";";
                        	}else{
	                        	String fvn =  fv.asName;
	                        	returnCols += fvn + ";";
                        	}
                        	break ;
                        }
                    }
                }
            }
            request.setAttribute("returnCols", returnCols);
            request.setAttribute("isMain", request.getParameter("isMain"));
            
            
            /*ת��������*/
            if("config".equals(selectType)){
            	return getForward(request, mapping, "selectConfig") ;
            }
            ArrayList rows = new ArrayList();
            ArrayList rowsMark=new ArrayList();
            for (int i = 0; i < ((List) rs.retVal).size(); i++) {
                Object[] os = (Object[]) ((List) rs.retVal).get(i);
                Object[] ss = new String[6];
                Object[] ssMark=new String[4];
                ssMark[0]="";
                ssMark[1]="";
                ss[0] = "";  //��������ʾ��ֵ
                ss[1] = ""; //�����践�ص�ֵ
                ss[5] = ""; //������Ƚϵ�ֵ

                if(os.length>1){
                	ss[4] = (String)os[0]+os[1] ;
                }else{
                	ss[4] = (String)os[0] ;
                }
                //���ҳ��践�ص�ֵ
                for (int j = 0; j < popSelectBean.getReturnFields().size(); j++) {
                    PopField fv = (PopField) popSelectBean.getReturnFields(). get(j);
                    if("list".equals(popupType)){
                    	if(true==fv.parentDisplay){
                    		for (int k = 0; k < popSelectBean.getAllFields().size(); k++) {
    	                        PopField fv2 = (PopField) popSelectBean.getAllFields().get(k);
    	                        if (fv.getFieldName().equals(fv2.getFieldName())) {
    	                        	ss[1] = os[k] + "#;#";
    	                        	if(fv.compare){
    	                        		ss[5] = os[k] + "#;#";
    	                        	}
    	                        	break ;
    	                        }
                    		}
                    		break ;
                    	}
                    }else{
	                    for (int k = 0; k < popSelectBean.getAllFields().size(); k++) {
	                        PopField fv2 = (PopField) popSelectBean.getAllFields().get(k);
	                        if (fv.getFieldName().equals(fv2.getFieldName())) {
	                            String osstr = os[k] + "";
	                            String asName=fv.asName;
                                DBFieldInfoBean tempfib = getFieldInfo(allTables,
	                                        popSelectBean.getTableName(asName),
	                                        popSelectBean.getFieldName(asName));
                                if(tempfib == null && fv.getType()==1 && fv.getParentName() != null && fv.getParentName().length() > 0 ){
                                	//���Ǹ��ϵĻ����ֶ�
                                	String parentName = fv.getParentName();
                                	if(parentName.startsWith("@TABLENAME")){
                                		parentName = tableName+parentName.substring(10);
                                	}
                                	tempfib = getFieldInfo(allTables,
	                                        popSelectBean.getTableName(parentName),
	                                        popSelectBean.getFieldName(parentName));
                                	asName= parentName;
                                }
	                            if (tempfib!=null&&(tempfib.getFieldType() ==
	                                DBFieldInfoBean.FIELD_DOUBLE)) {
	                                if (osstr == null || osstr.equals("null") ||
	                                    osstr.length() == 0) {
	                                    osstr = "0";
	                                }
	                                if(Double.parseDouble(osstr)==-99999999){
	                                	osstr="";
	                                }
	                                
	                                if(osstr.length()>0){
		                                DBTableInfoBean tempTableInfo=(DBTableInfoBean)allTables.get(popSelectBean.getTableName(asName)) ;
		                                boolean isMain=(tempTableInfo.getTableType()==0?true:false);
		                                osstr =  GlobalsTool.formatNumberS(new Double(
	                                            osstr), false, false,"",asName);
	                                }
	                                
	                            }
	                            if (tempfib!=null&&(tempfib.getFieldType() ==
		                                DBFieldInfoBean.FIELD_INT)) {
	                                if (osstr == null || osstr.equals("null") ||
	                                    osstr.length() == 0) {
	                                    osstr = "0";
	                                }
	                                if(Double.parseDouble(osstr)==-99999999){
	                                	osstr="";
	                                }	                                
	                            }
	                            ss[1] = ss[1] + osstr.trim() + "#;#";
	                            if(fv.compare){
	                            	ss[5] = ss[5] + osstr.trim() + "#;#";
	                        	}
	                            break;
	                        }
	                    }
	                }
                }
                ssMark[1]=ss[1];
                //�ҳ�����ʾ��ֵ
                if(configList!=null && configList.size()>0){
	                for(int m=0;m<configList.size();m++){
	                	ColConfigBean configBean = configList.get(m) ;
		                for (int j = 0; j < popSelectBean.getDisplayField().size(); j++) {
		                	PopField fv = (PopField) popSelectBean.getDisplayField().get(j);
		                	
		                	if(configBean.getColName().equals(fv.asName.replace('+', ' ')) ){
			                    for (int k = 0; k < popSelectBean.getAllFields().size(); k++) {
			                        PopField fv2 = (PopField) popSelectBean.getAllFields().get(k);
			                        if (fv.getFieldName().equals(fv2.getFieldName())) {
			                            String osstr = os[k] + "";
			                            
			                            //--------------��ʾȨ���ж�
			                            boolean viewRight = true;
			                        	if(fv.asName != null && fv.asName.length() > 0){    
			                        		String sd = fv.asName;
			                        		if(tableName!=null)
			                        		sd = sd.replace("@TABLENAME", tableName) ;
			                        		viewRight = DynDBManager.getViewRight("", sd,scopeRight, "popselect",loginBean);
			                        	}else{
			                            	ArrayList<String[]> list = ConfigParse.getTableFieldByReportField(fv.fieldName) ;
			        						for (int l = 0; l < list.size(); l++) {
			        							String[] obj = list.get(l);
			        							viewRight = DynDBManager.getViewRight("",obj[0]+"."+obj[1],scopeRight, "popselect",loginBean);
			        							if (!viewRight) {
			        								break;
			        							}
			        						} 
			                        	}
			                            if (!viewRight) {
			                                continue;
			                            }

			                            String asName=fv.asName;
			                            if(asName.contains("tblCompany1")||asName.contains("tblCompany2") || asName.contains("tblCompany3")){
		                                	String newTableName=asName.substring(0,asName.lastIndexOf("."));
		                                	asName=asName.replace(newTableName, "tblCompany");
			                            }
		                                DBFieldInfoBean tempfib = getFieldInfo(allTables,
			                                        popSelectBean.getTableName(asName),
			                                        popSelectBean.getFieldName(asName));
			                            if (tempfib != null &&  tempfib.getInputTypeOld() ==
			                                DBFieldInfoBean.INPUT_ENUMERATE) {
			                                osstr = getEnumerationItems(request,
			                                        enumeratemap,
			                                        tempfib.getRefEnumerationName(), osstr,
			                                        getLocale(request).toString());
										} else if (tempfib != null && tempfib.getFieldType() ==
			                                       DBFieldInfoBean.FIELD_DOUBLE) {
			                                if (osstr.equals("null") || osstr.equals("")) {
			                                    osstr = "0";
			                                }
			                                if(Double.parseDouble(osstr)==-99999999){
			                                	osstr="";
			                                }
			                                if(osstr.length()>0){
				                                DBTableInfoBean tempTableInfo=(DBTableInfoBean)allTables.get(popSelectBean.getTableName(asName)) ;
				                                boolean isMain=(tempTableInfo.getTableType()==0?true:false);
				                                osstr = GlobalsTool.formatNumberS(new Double(
			                                            osstr), false, true,"",asName);
			                                }
			                            }
			                            if (osstr != null && osstr.length() == 0) {
			                                osstr = "";
			                            }
			                            ss[0] = ss[0] + "'" + osstr + "',";
			                            if(fv.marker.equals("true")){
			                            	ssMark[0]=ssMark[0]+"'"+osstr+"',";
			                            }
			                            break;
			                        }
			                    }
			                    break ;
		                	}
		                }
	                }
                }else{
                	for (int j = 0; j < popSelectBean.getDisplayField().size(); j++) {
                        PopField fv = (PopField) popSelectBean.getDisplayField(). get(j);
                        for (int k = 0; k < popSelectBean.getAllFields().size(); k++) {
                            PopField fv2 = (PopField) popSelectBean.getAllFields().get(k);
                            if (fv.getFieldName().equals(fv2.getFieldName())) {
                                String osstr = os[k] + "";

                                //--------------��ʾȨ���ж�
	                            boolean viewRight = true;
	                        	if(fv.asName != null && fv.asName.length() > 0){    
	                        		String sd = fv.asName;
	                        		if(tableName!=null)
	                        		sd = sd.replace("@TABLENAME", tableName) ;
	                        		viewRight = DynDBManager.getViewRight("", sd,scopeRight, "popselect",loginBean);
	                        	}else{
	                            	ArrayList<String[]> list = ConfigParse.getTableFieldByReportField(fv.fieldName) ;
	        						for (int l = 0; l < list.size(); l++) {
	        							String[] obj = list.get(l);
	        							viewRight = DynDBManager.getViewRight("",obj[0]+"."+obj[1],scopeRight, "popselect",loginBean);
	        							if (!viewRight) {
	        								break;
	        							}
	        						} 
	                        	}
                                if (!viewRight) {
                                    continue;
                                }
                                String asName=fv.asName;
                                if(asName.contains("tblCompany1")||asName.contains("tblCompany2") || asName.contains("tblCompany3")){
                                	String newTableName=asName.substring(0,asName.lastIndexOf("."));
                                	asName=asName.replace(newTableName, "tblCompany");
                                }
                                DBFieldInfoBean tempfib = getFieldInfo(allTables,
	                                        popSelectBean.getTableName(asName),
	                                        popSelectBean.getFieldName(asName));
                                if (tempfib!=null&&tempfib.getInputTypeOld() ==
                                    DBFieldInfoBean.INPUT_ENUMERATE) { 
                                    osstr = getEnumerationItems(request,
                                            enumeratemap,
                                            tempfib.getRefEnumerationName(), osstr,
                                            getLocale(request).toString());
                                } else if (tempfib!=null&&tempfib.getFieldType() ==
                                           DBFieldInfoBean.FIELD_DOUBLE) {
                                    if (osstr.equals("null") || osstr.equals("")) {
                                        osstr = "0";
                                    }
                                    if(Double.parseDouble(osstr)==-99999999){
	                                	osstr="";
	                                }
	                                if(osstr.length()>0){                                    
	                                    DBTableInfoBean tempTableInfo=(DBTableInfoBean)allTables.get(popSelectBean.getTableName(asName)) ;
	                                    boolean isMain=true;
	                                    if(tempTableInfo!=null){
	                                       isMain=(tempTableInfo.getTableType()==0?true:false);
	                                    }
	                                    osstr = GlobalsTool.formatNumberS(new Double(
	                                            osstr), false, true,"",asName);
	                                }
                                }
                                if (osstr != null && osstr.length() == 0) {
                                    osstr = "";
                                }
                                ss[0] = ss[0] + "'" + osstr + "',";
                                if(fv.marker.equals("true")){
                                	ssMark[0]=ssMark[0]+"'"+osstr+"',";
                                }
                                break;
                            }
                        }
                	}
                }                
                               
                for (int j = 0; j < popSelectBean.getAllFields().size(); j++) {
                    PopField fv2 = (PopField) popSelectBean.getAllFields().get(
                            j);
                    if (fv2.getFieldName().equals(popSelectBean.getClassCode())) {
                        ss[2] = os[j].toString();
                        ss[3] =os[os.length-1].toString();
                        if (ss[3].equals("0")) {
                            ss[0] = ss[0] + "'" + this.getMessage(request, "common.lb.noChild") + ";',";
                        } else {                        	
                        	ss[0] = ss[0] + "'"+this.getMessage(request, "common.lb.viewNextClass")+";"+os[j]+"',";
                        }
                        break;
                    }
                }
                if(ss[0].toString().length()>0){
                	ss[0] = ss[0].toString().substring(0,
                        ss[0].toString().length() - 1);
                }

                rows.add(ss);
                if(popSelectBean.getType().equals("multiSeleToRow")&&ssMark[0].toString().length()>0){
                	ssMark[0]=ssMark[0].toString().substring(0,
                			ssMark[0].toString().length() - 1);
                	rowsMark.add(ssMark);
                }

            }

            if(displayName!=null && displayName.length()>0){
            	request.setAttribute("displayName", displayName) ;
            	request.setAttribute("tableDisplayName", this.getMessage(request, "tip.plese.select")+" "+displayName);
            }else if (fieldName != null && !fieldName.equals("")) {
                List das= tableInfo.getFieldInfos();
                for (Object object : das) {
					DBFieldInfoBean tempBean = (DBFieldInfoBean) object;
					if(fieldName.equals(tempBean.getFieldName())){
						String temp;
						if(tempBean.getDisplay()!=null&&tempBean.getDisplay().get(getLocale(request).toString())!=null){
							temp= tempBean.getDisplay().get(getLocale(request).toString()).toString();
						}else{
							temp=tempBean.getFieldName();
						}
						request.setAttribute("tableDisplayName", this.getMessage(request, "tip.plese.select")+temp);
					}
				}
            }else if(tableInfo!=null){
            	request.setAttribute("tableDisplayName", tableInfo.getDisplay().get(getLocale(request).toString()).toString());
            }else{
            	request.setAttribute("tableDisplayName","�ּ�Ŀ¼");
            }
            //�����ص�ת��һ�� ����������
            ArrayList hiddenList = popSelectBean.getHiddenField() ;
            ArrayList hiddenField = new ArrayList() ;
            if(hiddenList!=null && hiddenList.size()>0){
            	for(int m=0;m<hiddenList.size();m++){
            		PopField fv = (PopField) hiddenList.get(m) ;
            		//�ҵ���Ӧ���ֶ�
                    String asName= fv.getAsName() ;
                    if(asName.contains("tblCompany1")||asName.contains("tblCompany2") || asName.contains("tblCompany3")){
                    	String newTableName=asName.substring(0,asName.lastIndexOf("."));
                    	asName=asName.replace(newTableName, "tblCompany");
                    }
                    DBFieldInfoBean fi= DynDBManager.cloneObject(getFieldInfo(allTables,popSelectBean.getTableName(asName), popSelectBean.getFieldName(asName)));
                    
                    
                    if (fi == null) {
                        continue;
                    }                    
                    KRLanguage krLan=null;
                    if(fv.getDisplay()!=null){
                    	DBFieldInfoBean bean;
                    	if(tableName!=null){                    		
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay().replace("@TABLENAME", tableName)), popSelectBean.getFieldName(fv.getDisplay().replace("@TABLENAME", tableName)));
                    	}else{
                    		bean=this.getFieldInfo(allTables, popSelectBean.getTableName(fv.getDisplay()), popSelectBean.getFieldName(fv.getDisplay()));
                    	}
                    	if(bean!=null){
                    		krLan=bean.getDisplay();
                    	}
                    }
                   
        
                    String[] ss = new String[4];
                    if(krLan==null && fi != null)krLan=fi.getDisplay();
                    Object fd = krLan == null ? "" :krLan.get(getLocale(request).toString());
                    if(fv.display !=null && !fv.display.equals("") && fv.display.indexOf(".") == -1){
                    	ss[0] = fv.display;
                    }else{
                    	ss[0] = fd == null ? fi.getFieldName() : fd.toString();
                    }
//                    if(fv.display!=null && tableName!=null){
//                    	ss[3] = fv.display.replace("@TABLENAME", tableName) ;
//                    }else{
//                    	ss[3] = fv.fieldName ;
//                    }
                    ss[3] = fv.asName;
                    String strValue = String.valueOf(GlobalsTool.getFieldWidth(tableName, fv.fieldName)) ;
                    if(!"0".equals(strValue)){
                    	ss[1] = Integer.parseInt(strValue)+ "";
                    }else{
                    	ss[1] = fv.width + "" ;
                    }
                    hiddenField.add(ss) ;
            	}
            }
            String moreLanguage = GlobalsTool.getMessage(request, "com.listgrid.lock")+";"
								+ GlobalsTool.getMessage(request, "com.listgrid.cancel.lock")+";"
								+ GlobalsTool.getMessage(request, "com.listgrid.save.config")+";"
								+ GlobalsTool.getMessage(request, "com.listgrid.default.config")+";"
								+ GlobalsTool.getMessage(request, "com.listgrid.hidden.column")+";"
								+ GlobalsTool.getMessage(request, "com.listgrid.no.hidden")+";"
								+ GlobalsTool.getMessage(request, "com.listgrid.add.column")+";"
								+ GlobalsTool.getMessage(request, "common.lb.ok")+";"
								+ GlobalsTool.getMessage(request, "mrp.lb.cancel") ;
            PopupGrid popup = new PopupGrid(getLoginBean(request)) ;
            popup.moreLanguage = moreLanguage ;
            popup.setIsRoot(isRoot);
            popup.setNextClass(getMessage(request, "common.lb.showNext")) ;
            popup.setStrRadio(getMessage(request, "mrp.lb.chooseOne")) ;
            popup.setMainPop(mainPop) ;
            popup.setCols(cols) ;
            popup.setRows(rows) ;
            popup.topPopup = topPopup;
            popup.detailPopup = detailPopup; //��ϸ������
            popup.pageNo=pageNo;
            popup.pageSize=pageSize;
            popup.setHiddenField(hiddenField) ;
            popup.setSaveParentFlag(popSelectBean.isSaveParentFlag());
            popup.fieldName = fieldName;
            
            popup.configScope = loginBean.getId().equals("1");
            
            ArrayList<String> listValue =  (ArrayList<String>) request.getSession().getAttribute("listValue") ;
            popup.setListValue(listValue) ;
            popup.setSelectName(selectName);
            String rootDir = getMessage(request, "com.acc.ini.root");//��Ŀ¼
            String showNext = getMessage(request, "common.lb.viewNextClass");//��ʾ�¼�
            request.setAttribute("popupHTML", popup.toHTML(popSelectBean,rootDir,showNext) );
            request.setAttribute("result", rows);
            request.setAttribute("rowsMark", rowsMark);
            
            String keyId = (request.getParameter("keyId") == null ||
                            request.getParameter("keyId").length() == 0) ?
                           "noData" : request.getParameter("keyId");
            request.setAttribute("keyId", keyId);
            request.setAttribute("popBean", popSelectBean);
            String classCode = popSelectBean.getClassCode() ;
            if(classCode!=null && classCode.trim().length()>0){
	            DBTableInfoBean popupTableInfo = DDLOperation.getTableInfo(allTables,classCode.substring(0,classCode.indexOf("."))); ;
	            Result rs3 = new ReportDataMgt().getParentName(parentCode, popupTableInfo,getLocale(request).toString());
				if (rs3.retCode == ErrorCanst.RET_NOTSETROWMARKER) {
					request.setAttribute("parentName", "");
				} else {
					ArrayList parentName = (ArrayList) rs3.retVal;
					
					String parentUrl = "";
					if(isRoot!=null&&"isRoot".equals(isRoot)){
						
					}else if(parentName.equals("")){
						parentUrl +=  GlobalsTool.getMessage(request, "com.acc.ini.root");
					}else{
						if("root".equals(iroot)){
							parentUrl += "<a href=\"javascript:nextClass2('');\">" +GlobalsTool.getMessage(request, "com.acc.ini.root")
									  + "</a> >> ";
							for (int i = 0; i < parentName.size(); i++) {
								String[] nameClass = (String[]) parentName.get(i);
								if(nameClass[1].length()==(parentName.size())*5){
									parentUrl += nameClass[0] ;
								}else{
									parentUrl += "<a href=\"javascript:nextClass2('"+ nameClass[1] +"');\">" + nameClass[0]
									  		  + "</a> >> ";
								}
		
							}
						
						}else{

							parentUrl += "<a href=\"javascript:nextClass2('');\">" +GlobalsTool.getMessage(request, "com.acc.ini.root")
									  + "</a> >> ";
							for (int i = 0; i < parentName.size(); i++) {
								String[] nameClass = (String[]) parentName.get(i);
								if(nameClass[1].length()==(parentName.size())*5){
									parentUrl += nameClass[0] ;
								}else{
									parentUrl += "<a href=\"javascript:nextClass2('"+ nameClass[1] +"');\">" + nameClass[0]
									  		  + "</a> >> ";
								}
		
							}
						
						}
						
					}
					request.setAttribute("parentName", parentUrl);
				}
            }
            String keyIdType=request.getParameter("keyIdType")==null?"":request.getParameter("keyIdType");
            if(popSelectBean.getType().equals("multiSeleToRow")&&keyIdType.equals("saveType")){
            	String[] keyIds = getParameter("varKeyIds", request).split(";");
            	if(keyIds!=null){
            		//������ҳ����ѡ�������
        		ArrayList rowsSel=new ArrayList();
        		ArrayList markIndex=new ArrayList();
        		for(int i = 0; i < popSelectBean.getReturnFields().size(); i++){
        			PopField field = (PopField) popSelectBean.getReturnFields().get(
                        i);
        			if(field.marker.equals("true")){
        				markIndex.add(i);
        			}
        		}

        		for(int i=0;i<keyIds.length;i++){
        			Object []ss=new Object[2];
        			String []id=keyIds[i].split(";");
        			ss[0]="";
        			ss[1]=keyIds[i];
        			for(int j=0;j<markIndex.size();j++){
        				int index=Integer.parseInt(markIndex.get(j).toString());
        				ss[0]=ss[0]+"'"+(id[index].length()==0?"&nbsp;":id[index])+"',";
        			}
        			if(ss[0].toString().length()>0){
        				ss[0]=ss[0].toString().substring(0,ss[0].toString().length()-1);
        			}
        			rowsSel.add(ss);
        		}

        		request.setAttribute("rowsSel", rowsSel);
            	}

            }

            for (int i = 0; i < popSelectBean.getReturnFields().size(); i++) {
                PopField field = (PopField) popSelectBean.getReturnFields().get(i);
                if(field.defaultValue==null || field.defaultValue.length()==0){
	                if (field.getParentName() != null) {
	                    DBFieldInfoBean tempfib = getFieldInfo(allTables,
	                            popSelectBean.getTableName(field.getParentName()),
	                            popSelectBean.getFieldName(field.getParentName()));
	                    if (tempfib != null) {
	                        field.setDefaultValue(tempfib.getDefaultValue());
	                    }else {
	                        field.setDefaultValue("");
	                    }
	                } else {
	                    field.setDefaultValue("");
	                }
                }
            }

            if (popSelectBean.getClassCode() != null &&
                popSelectBean.getClassCode().length() > 0) {
                request.setAttribute("hasClass", true);
                request.setAttribute("parentCode", parentCode);
            }
            request.setAttribute("queryField", request.getParameter("queryField")==null?"":request.getParameter("queryField"));
            if(rs.totalPage>0){
            	request.setAttribute("pageBar", pageBar(rs, request,"popupSelect"));
            }           
            
        } else {
        	 //��ѯʧ��
            EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
            return getForward(request, mapping, "message");
        }
        
        //�ж�forwardModel�Ƿ������Ȩ��
        if(popSelectBean!=null && popSelectBean.getForwardModel()!=null && popSelectBean.getForwardModel().trim().length()>0){
        	String forwardModel = popSelectBean.getForwardModel() ;
        	//���⴦��������λ
        	if(forwardModel.contains("&ClientFlag")){
        		forwardModel = forwardModel.substring(0, forwardModel.indexOf("&ClientFlag")) ;
        	}else if(forwardModel.contains("&PropName")){
        		forwardModel = forwardModel.substring(0, forwardModel.indexOf("&PropName")) ;
        	}
        	ArrayList forwardscopeRight = new ArrayList();
        	MOperation modelMop = (MOperation) getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName="+forwardModel) ;
        	if(modelMop!=null && modelMop.add()){
        		request.setAttribute("modelAdd", true) ; //�����Ȩ��
        		
        		//���´��뿴�Ƿ������ͬ����Ȩ�� 
	        	forwardscopeRight.addAll(modelMop.queryScope) ;
	            //�������з���Ȩ��
		        forwardscopeRight.addAll(modelMop.classScope) ;
		        
	        	//���빫��Ȩ��
	    		ArrayList allScopeList = this.getLoginBean(request).getAllScopeRight();
	    		forwardscopeRight.addAll(allScopeList) ;
	    		if(forwardModel.contains("&ClientFlag")){
	        		forwardModel = forwardModel.substring(0, forwardModel.indexOf("&ClientFlag")) ;
	        	}
	    		String forwardTs = forwardModel;
	    		if(forwardTs.contains("&moduleType")){
	    			forwardTs = forwardTs.substring(0, forwardTs.indexOf("&moduleType")) ;
	        	}
	        	DBTableInfoBean forwardTable = GlobalsTool.getTableInfoBean(forwardTs);
	        	boolean cantAddLevel = false;        	
	        	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
	        		ArrayList rows = (ArrayList)rs.retVal;
		    		if (forwardTable.getClassFlag() == 1) {
		    			//�ҵ�classCode���к�
		    			int coluNo = 0;
		    			for(int j = 0;j<popSelectBean.getAllFields().size();j++){
		    				PopField pf1= popSelectBean.getAllFields().get(j);
		    				if(pf1.getFieldName().equals(popSelectBean.getClassCode())){
		    					coluNo = j;
		    				}		    				
		    			}
		    			if (forwardscopeRight != null) {
		    				for (Object o : forwardscopeRight) {
		    					LoginScopeBean lsb = (LoginScopeBean) o;
		    					if (lsb.getFlag().equals("0") && lsb.getIsAddLevel().equals("0") && lsb.getTableName().equals(forwardTable.getTableName())) {
		    						String[] claCodes = lsb.getScopeValue().split(";");
		    						if (claCodes.length > 0) {
		    							for (String ccode : claCodes) {
		    								if(rows.size() ==0){
		    									if(parentCode == null || parentCode.equals("")){
		    										//�з�ΧȨ�ޣ�������һ������û�У�Ҳû��parentCode,
		    										cantAddLevel = true;
		    										break;
		    									}else{
			    									if (ccode.startsWith(parentCode)) {
			    										cantAddLevel = true;
			    										break;
			    									}
		    									}
		    								}else{
			    								for (int i = 0; i < rows.size(); i++) {	    									
			    									if (ccode.startsWith((((Object[])rows.get(i))[coluNo]).toString())) {
			    										cantAddLevel = true;
			    										break;
			    									}
			    								}
		    								}
		    								if (cantAddLevel)
		    									break;
		    							}
		    						}
		    					}
		    					if (cantAddLevel)
		    						break;
		    				}
		    			}
		    		}
	        	}
	        	request.setAttribute("cantAddLevel", cantAddLevel);
        	}
        }
        
        
        if(isRoot!=null && "isRoot".equals(isRoot)){
        	String pop = (String) request.getAttribute("popupHTML");
        	pop.replaceAll(getMessage(request, "com.acc.ini.root"), GlobalsTool.getMessage(request, "com.acc.ini.root"));
        	pop.replaceAll(getMessage(request, "common.lb.viewNextClass"), GlobalsTool.getMessage(request, "common.lb.showNext"));
        	request.setAttribute("isRoot", "root");
        }
        /*������������ ��������*/
        if("sqty".equals(popType)){
        	String billDate = getParameter("BillDate", request) ;
        	String stockCode = getParameter("StockCode", request) ;
        	request.setAttribute("BillDate", billDate) ;
        	request.setAttribute("StockCode", stockCode) ;
        	/*����ѡ�е���������*/
        	String[] keyValues  = getParameters("cutKeyId", request) ;
        	String[] codeValues = getParameters("codeValue", request) ;
        	String[] cutQtys    = getParameters("cutQty", request) ;
        	if(keyValues!=null){
        		request.setAttribute("keyValues", Arrays.asList(keyValues)) ;
        		request.setAttribute("codeValues", codeValues) ;
        		request.setAttribute("cutQtys", cutQtys) ;
        	}
        	return getForward(request, mapping, "salesSelect");
        }else{
        	return getForward(request, mapping, "selectList");
        }
        
    }

    
    /**
	 * �ж����ؿ���ʾ�ֶ��Ƿ�����������
	 * 
	 * @param fieldNames
	 * @return
	 */
	public boolean existColConfig(String tableName, String fieldName,HttpServletRequest request) {
		boolean exist = false;
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request
		.getSession().getServletContext().getAttribute(
				"userSettingColConfig");
		if (userColConfig != null) {
			ArrayList<ColConfigBean> configList = userColConfig.get(tableName
					+ "bill");

			if (configList != null) {
				for (ColConfigBean bean : configList) {
					if (bean.getColName().equals(fieldName) 
							|| bean.getColName().equals("@TABLENAME."+fieldName)) {
						exist = true;
						break;
					}
				}
			}
		}
		return exist;
	}

    /**
     * ƴ��ϸ���ַ���
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward detail_email(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws
            Exception {

    	String fromJsp = request.getParameter("fromJsp");
    	if(fromJsp!=null&&"update".equals(fromJsp)){
    		request.setAttribute("fromJsp", fromJsp);
    	}
    	/*�Ƿ�Ӳݸ�ҳ�����ӹ�����*/
    	String saveDraft = getParameter("saveDraft", request) ;
    	request.setAttribute("saveDraft", saveDraft) ;
    	
    	String nstr = request.getParameter("keyId");
        request.setAttribute("kid", nstr);
        String html_str = "";
        if (nstr != null && nstr.length() != 0) {
            String keyId = nstr;
            //ִ�м�����ϸ
            String tableName = getParameter("tableName", request);
            request.setAttribute("tableName", tableName);
            String parentTableName = getParameter("parentTableName", request);
            request.setAttribute("parentTableName", parentTableName);
            request.setAttribute("parentCode",
                                 request.getParameter("parentCode") == null ?
                                 "" : request.getParameter("parentCode"));

            Hashtable map = (Hashtable) request.getSession().getServletContext().
                            getAttribute(BaseEnv.TABLE_INFO);
            //�ж��Ƿ����÷�֧����
            LoginBean lg = getLoginBean(request) ;
            String sunClassCode = lg.getSunCmpClassCode();
            Hashtable props = (Hashtable) request.getSession().getServletContext().
            getAttribute(BaseEnv.PROP_INFO);
            Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
            boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
            //ȡ�ò�ѯʱ�ķ�ΧȨ��
            MOperation mop = getMOperation(map, request, tableName);
            Result rs = dbmgt.detail(tableName, map, keyId, sunClassCode,props,lg.getId(),isLastSunCompany,"");
            HashMap values = (HashMap) rs.getRetVal();
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            	DBTableInfoBean mainTable = DDLOperation.getTableInfo(map,tableName);
            	html_str += "<STYLE type=text/css>";
                html_str += "BODY {FONT-SIZE: 12px; MARGIN: 0px; SCROLLBAR-SHADOW-COLOR: #999999; SCROLLBAR-ARROW-COLOR: #555555; SCROLLBAR-DARKSHADOW-COLOR: #ffffff; SCROLLBAR-BASE-COLOR: #e0e0e0; HEIGHT: 100%; BACKGROUND-COLOR: #ffffff}\n";
                html_str +=
                        "A:link {COLOR: #000000; TEXT-DECORATION: none}\n";
                html_str +=
                        "A:visited {COLOR: #000000; TEXT-DECORATION: none}\n";
                html_str +=
                        "A:hover {COLOR: #0060ff; TEXT-DECORATION: none}\n";
                html_str +=
                        "A:active {COLOR: #0060ff; TEXT-DECORATION: none}\n";
                html_str += "IMG {BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px}\n";
                html_str += "LI {LIST-STYLE-TYPE: none}\n";
                html_str += ".scroll_function_small {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 3px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; OVERFLOW: auto; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid; HEIGHT: 80%}\n";
                html_str += ".scroll_function_small_1 {BORDER-RIGHT: #e0e0e 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; OVERFLOW: auto; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid; HEIGHT: 80%}\n";
                html_str += ".scroll_function_small_2 {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
                html_str += ".scroll_function_small_3 {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
                html_str += ".scroll_function_small_repotlist {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 885px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
                html_str +=
                        ".listRange_list_function {BORDER-RIGHT: #d2d2d2 1px solid}\n";
                html_str += ".listRange_list_function TBODY TD {BORDER-TOP-WIDTH: 0px; PADDING-RIGHT: 1px; PADDING-LEFT: 5px; FONT-SIZE: 12px; BORDER-LEFT: #d2d2d2 1px solid; BORDER-BOTTOM: #d2d2d2 1px solid; WHITE-SPACE: nowrap; HEIGHT: 22px}\n";
                html_str += ".listRange_list_function THEAD TR {}\n";
                html_str += ".listRange_list_function THEAD TD {PADDING-RIGHT: 5px; PADDING-LEFT: 5px; FONT-SIZE: 12px; FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#cccccc,endColorStr=#ffffff); BORDER-LEFT: #d2d2d2 1px solid; COLOR: #42789c; PADDING-TOP: 4px; BORDER-BOTTOM: #d2d2d2 1px solid; WHITE-SPACE: nowrap; HEIGHT: 22px; TEXT-ALIGN: center}\n";
                html_str += ".listRange_list_function INPUT {BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; TEXT-ALIGN: left; BORDER-RIGHT-WIDTH: 0px}\n";
                html_str += ".listRange_list_function SELECT {BORDER-TOP-WIDTH: 0px; PADDING-RIGHT: 0px; PADDING-LEFT: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; PADDING-BOTTOM: 0px; WIDTH: 100%; PADDING-TOP: 0px; TEXT-ALIGN: left; BORDER-RIGHT-WIDTH: 0px}\n";
                html_str += ".listRange_list_statistic {BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid; BACKGROUND-COLOR: #fefef4}\n";
                html_str += ".listRange_list_statistic TD {PADDING-RIGHT: 5px; PADDING-LEFT: 5px; FONT-WEIGHT: bold; BORDER-LEFT: #d2d2d2 1px solid; PADDING-TOP: 4px; HEIGHT: 22px}\n";
                html_str += ".HeadingTitle {font-size:14px;font-weight:bold;MARGIN-TOP: 5px; PADDING-LEFT: 10px; FLOAT: left; WIDTH: 150px; PADDING-TOP: 8px; BORDER-BOTTOM: #81b2e3 1px solid; HEIGHT: 17px; TEXT-ALIGN: left}\n";

                html_str += ".scroll_function_big {FLOAT: left; MARGIN-BOTTOM: 3px; WIDTH: 100%; HEIGHT: 600px;}\n";
                html_str += ".listRange_1 {BORDER-RIGHT: #e0e0e0 1px solid;PADDING-RIGHT: 0px;BORDER-TOP: #e0e0e0 1px solid;MARGIN-TOP: 5px; PADDING-LEFT: 0px;FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#E6F4FD,endColorStr=#ffffff);FLOAT: left;PADDING-BOTTOM: 2px;MARGIN-LEFT: 3px;BORDER-LEFT: #e0e0e0 1px solid;WIDTH: 886px;PADDING-TOP: 2px;BORDER-BOTTOM: #e0e0e0 1px solid;HEIGHT: auto; TEXT-ALIGN: left}\n";
                html_str += ".listRange_1 LI {FLOAT: left; WIDTH: 215px}\n";
                html_str += ".listRange_1 BUTTON {VERTICAL-ALIGN: top}\n";
                html_str += ".listRange_1_button LI {MARGIN-TOP: -5px; FLOAT: left; MARGIN-BOTTOM: 5px; VERTICAL-ALIGN: top; WIDTH: 827px; TEXT-ALIGN: right}\n";
                html_str += ".listRange_1 LI SPAN {MARGIN-TOP: 5px; FLOAT: left; WIDTH: 85px; TEXT-ALIGN: right}\n";
                html_str += ".listRange_1 LI DIV {MARGIN-TOP: 6px; FLOAT: left; MARGIN-LEFT: 3px; WIDTH: auto}\n";
                html_str += ".listRange_1 INPUT {FLOAT: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"left\":\"\"); ; MARGIN-LEFT: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"3px\":\"\"); ; WIDTH: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"13px\":\"100px\"); ; BORDER: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"\":\"1px solid #42789C\"); TEXT-ALIGN: left}\n";
                html_str +=
                        ".listRange_1 LI SELECT {WIDTH: 100px; TEXT-ALIGN: left}\n";
                html_str += "\n";
                html_str += "</STYLE>\n";
                html_str += "<DIV class=HeadingTitle>" +
                        mainTable.getDisplay().get(getLocale(request).
                        toString()) + "</DIV>\n<p>&nbsp;</p>";
                html_str += "<DIV class=scroll_function_big>\n";
                html_str += "<UL class=listRange_1>\n";

                //leep load
                List<DBFieldInfoBean> mainFields = mainTable.getFieldInfos();
                for (DBFieldInfoBean df : mainFields) {
                    
                	if(!GlobalsTool.canImportField(df) || df.getInputType()==3){
                		continue ;
                	}
                	if(df.getInputType()==6 && !existColConfig(tableName, df.getFieldName(), request)){
                		continue ;
                	}
                    Object o = df.getDisplay() == null ? "" :df.getDisplay().get(getLocale(request).toString());
                    if(df.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE 
                    		|| (df.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && df.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE)){
                    	ArrayList popList = df.getSelectBean().getViewFields() ;
                    	for(int i=0;i<popList.size();i++ ){
                    		PopField field = (PopField) popList.get(i) ;
                    		DBFieldInfoBean db = null ;
                    		if(field.getFieldName().indexOf(".")!=-1){
                    			db = DDLOperation.getFieldInfo(map, field.getFieldName().substring(0,field.getFieldName().indexOf(".")),
                    								 field.getFieldName().substring(field.getFieldName().indexOf(".")+1)) ;
                    		}
                    		if(db!=null&&(db.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||db.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE) && values.get(field.getFieldName())!=null){
                				HashMap moreLan=(HashMap)values.get("LANGUAGEQUERY");
                				String strLanguage = ((KRLanguage)moreLan.get(values.get(field.getFieldName()))).get(getLocale(request).toString()) ;
                				html_str += "<LI><SPAN>" + (null == o ? "" :o.toString()) +"��</SPAN>" 
                						 + (strLanguage!=null?strLanguage: "&nbsp;")+ "</LI>" ;
                						
                			}else{
                				html_str += "<LI><SPAN>" + (null == o ? "" :o.toString()) +"��</SPAN>" 
                            			 + (values.get(field.getFieldName()) != null ?values.get(field.getFieldName()).toString(): "&nbsp;")+ "</LI>";
                			}
                    	}
                    }else if(df.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE){
                    	ArrayList listEnum = (ArrayList) GlobalsTool.getEnumerationItems(df.getRefEnumerationName(), getLocale(request).toString()) ;
                    	String strValue = String.valueOf(values.get(df.getFieldName())) ;
                    	for(int j=0;j<listEnum.size();j++){
                    		KeyPair key = (KeyPair) listEnum.get(j) ;
                    		if(strValue!=null && strValue.equals(key.getValue())){
                    			html_str += "<LI><SPAN>" + (null == o ? "" :o.toString()) +"��</SPAN>" 
                                + key.getName()+ "</LI>";
                    			break ;
                    		}
                    	}
                    }else{
                    	html_str += "<LI><SPAN>" + (null == o ? "" :o.toString()) +"��</SPAN>" 
                        		 + (values.get(df.getFieldName()) != null ? GlobalsTool.formatNumber(values.get(df.getFieldName()), false, false, 
                                		"true".equals(BaseEnv.systemSet.get("intswitch").getSetting()),
                                		tableName, df.getFieldName(), true): "&nbsp;")+ "</LI>";
                    }
                    
    
                }

                html_str += "</UL>\n";
            	 //Ѱ���ӱ�
                ArrayList rowsList = new ArrayList();
                ArrayList childTableList = DDLOperation.getChildTables(tableName, map);
                for (int k = 0;childTableList != null && k < childTableList.size(); k++) {
                    DBTableInfoBean childTable = (DBTableInfoBean)childTableList.get(k);
                    ArrayList cols = new ArrayList();
                    ArrayList rows = new ArrayList();
                    for (int i = 0;  childTable != null &&
                                 i < childTable.getFieldInfos().size(); i++) {
                        DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
                        if (!GlobalsTool.canImportField(fi) || fi.getInputType()==3) {
                            continue;
                        }
                        if(fi.getInputType()==6 && !existColConfig(tableName, fi.getFieldName(), request)){
                    		continue ;
                    	}
                        if(fi.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE 
                        			&& fi.getInputValue()!=null && fi.getInputValue().length()>0){
                        	if(!fi.getSelectBean().getRelationKey().hidden){
	                        	String[] ss = new String[5];
		                        Object o = fi.getDisplay().get(getLocale(request).toString());
		                        setFieldString(childTable, fi, ss, o);
		                        cols.add(ss);
                        	}
                        	ArrayList popList = fi.getSelectBean().getViewFields() ;
                        	for(int m=0;m<popList.size();m++ ){
                        		PopField field = (PopField) popList.get(m) ;
                        		if(!"true".equals(field.hiddenInput)){
	                        		DBFieldInfoBean dbField=GlobalsTool.getFieldBean(field.getAsName().substring(0,field.getAsName().indexOf(".")), 
	                        								field.getAsName().substring(field.getAsName().indexOf(".")+1));
	                        		String[] ss = new String[4];
	                        		if(field.getDisplay()!=null && field.getDisplay().length()>0){
	                        			if(field.getDisplay().contains("@TABLENAME")){
	                        				DBFieldInfoBean db = DDLOperation.getFieldInfo(map, childTable.getTableName(),field.getDisplay().substring(field.getDisplay().indexOf(".")+1)) ;
	                        				if(db!=null){
	                        					ss[0] = db.getDisplay().get(getLocale(request).toString()) ;
	                        				}
	                        			}else{
	                        				DBFieldInfoBean db = DDLOperation.getFieldInfo(map, field.getFieldName().substring(0,field.getFieldName().indexOf(".")),
      								 				 		field.getFieldName().substring(field.getFieldName().indexOf(".")+1)) ;
				               				if(db!=null){
				               					ss[0] = db.getDisplay().get(getLocale(request).toString()) ;
				               				}
	                        			}
	                        		}else{
	                        			Object obj = dbField.getDisplay() == null ? "" :dbField.getDisplay().get(getLocale(request).toString());
	                        			ss[0] = obj == null ? dbField.getFieldName() : obj.toString();
	                        		}
	    		                    ss[2] = DBFieldInfoBean.getFieldTypeString(dbField);
	                                 ss[3] = field.getFieldName();
	                                 cols.add(ss);
                        		}
                        	}
                        }else{
                        	 String[] ss = new String[4];
                             Object o = fi.getDisplay() == null ? "" :
                                        fi.getDisplay().get(getLocale(request).toString());
                             ss[0] = o == null ? fi.getFieldName() : o.toString();
 		                    ss[2] = DBFieldInfoBean.getFieldTypeString(fi);
                             ss[3] = fi.getFieldName();
                             cols.add(ss);
                        }
                       
                    }

                    HashMap hm = (HashMap) rs.retVal;
                    if (childTable != null) {
                        List childList = (List) hm.get("TABLENAME_" + childTable.getTableName());
                        HashMap moreLan=(HashMap)hm.get("LANGUAGEQUERY");
                        for (int i = 0; i < childList.size(); i++) {
                            HashMap os = (HashMap) (childList).get(i);
                            String[] ss = new String[2];
                            ss[0] = "";
                            for (int j = 0; j < cols.size(); j++) {
                            	String fieldName = ((String[]) cols.get(j))[3] ;
                            	if(os.get(fieldName)==null){
                            		ss[0] = ss[0] +"''," ;
                            	}else{
                            		DBFieldInfoBean dbField = null ;
                            		if(fieldName.contains(".")){
                            			dbField = DDLOperation.getFieldInfo(map, fieldName.substring(0,fieldName.indexOf(".")), fieldName.substring(fieldName.indexOf(".")+1)) ;
                            		}else{
                            			dbField = DDLOperation.getFieldInfo(map, childTable.getTableName(), fieldName) ;
                            		}
                            		
                            		if(dbField!=null && dbField.getInputType()==DBFieldInfoBean.INPUT_ENUMERATE){
                            			ss[0] = ss[0] +"'"+GlobalsTool.getEnumerationItemsDisplay(dbField.getRefEnumerationName(), (String)os.get(fieldName), getLocale(request).toString()) +"'," ;
                            		}else if(dbField!=null &&(dbField.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||dbField.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE)){
                            			ss[0] = ss[0] +"'"+((KRLanguage)moreLan.get(os.get(fieldName))).get(getLocale(request).toString())+"'," ;
                            		}else{
                            			ss[0] = ss[0] + "'" +  GlobalsTool.formatNumber(os.get(fieldName), false, false, 
                            					"true".equals(BaseEnv.systemSet.get("intswitch").getSetting()),tableName, fieldName, true) + "',";
                            		}
                            	}
                            }
                            ss[0] = ss[0].substring(0, ss[0].length() - 1);
                            ss[1] = os.get("id").toString();
                            rows.add(ss);
                        }
                    }
                    rowsList.add(new Object[] {childTable.getTableName(), rows});
                    if (childTableList != null) {
                    	Collections.sort(childTableList, new SortDBTable()) ;
                        request.setAttribute("childTableList", childTableList);
                    }
                    html_str += "<DIV class=scroll_function_small>\n";
                    html_str += "<TABLE class=listRange_list_function width=887 id=tblSort cellSpacing=0 cellPadding=0 border=0 name=\"table\">\n";
                    html_str += "<THEAD>\n";
                    html_str += "<tr>";

                    int length_cols = cols.size();
                    int length_rows = rows.size();
                    for (int i = 0; i < length_cols; i++) {

                        String[] col_str = (String[]) cols.get(i);
                        if (col_str[3] != "id") {
                            html_str += "<td>";
                            html_str += col_str[0];
                            html_str += "</td>";
                        }
                    }
                    html_str += "</tr>";
                    html_str += "</THEAD>";
                    html_str += "<TBODY>";

                    for (int i = 0; i < length_rows; i++) {
                        html_str += "<tr>";
                        String[] row_str = (String[]) rows.get(i);
                        String[] row_str2 = row_str[0].split(",");
                        int leng = row_str2.length;
                        for (int j = 0; j < leng; j++) {
                            html_str += "<td>";
                            html_str += (row_str2[j].substring(1,row_str2[j].length() - 1).equals("") ?
                                     	"&nbsp;" :row_str2[j].substring(1,row_str2[j].length() - 1));
                            html_str += "</td>";

                        }

                        html_str += "</tr>";

                    }
                    html_str += "<TBODY>";
                    html_str += "</table>";
                    html_str += "</DIV>";
                    html_str += "</DIV>";

                }

                request.setAttribute("mainTable", mainTable);
                request.setAttribute("values", rs.retVal);
                request.setAttribute("tableName",
                                     mainTable.getDisplay().get(getLocale(
                                             request).
                        toString()).toString());
                request.setAttribute("result", rowsList);
                request.setAttribute("tableName", tableName);
                //���سɹ�
            } else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
            	 //��¼�����ڴ���
                EchoMessage.error().add(getMessage(
                        request, "common.error.nodata")).setRequest(request);
            } else {
            	 //����ʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                        setRequest(request);
            }
        }
        request.setAttribute("html_str", html_str);
        String userId = this.getLoginBean(request).getId();
        Result rs=new EMailMgt().selectAccountByUser(userId);
        MailinfoSettingBean obj =null;
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list=(ArrayList)rs.retVal;
			for(int i=0;i<list.size();i++){
				MailinfoSettingBean a=(MailinfoSettingBean)list.get(i);
				if(a.getDefaultUser().equals("1")){
					obj=a;
					break;
				}
			}
			request.setAttribute("setting", obj);
		}
        
        return getForward(request, mapping, "sendemail");
    }

    protected ActionForward check_list(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws  Exception {

        String tableName = getParameter("tableName", request);
        String keyId = getParameter("keyId", request);
        String parentCode = request.getParameter("parentCode");
        request.setAttribute("parentCode", parentCode);
        if (parentCode == null) {
            parentCode = "";
        }
        int pageSize = getParameterInt("pageSize", request);
        int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = GlobalsTool.getPageSize();
        }

        Result rs = userMgt.getApproveRecdList(tableName, keyId);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            request.setAttribute("result", rs.retVal);
            request.setAttribute("tableName", tableName);
            request.setAttribute("keyId", keyId);
            request.setAttribute("parentCode", parentCode);
        } else {
            EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
            return getForward(request, mapping, "message");
        }
        return getForward(request, mapping, "checkList");
	}

	protected ActionForward extendDefine(ActionMapping mapping, ActionForm form,
			            HttpServletRequest request,
			            HttpServletResponse response) throws
			            Exception {
		//��ȡ��ǰ�޸ĵ�ҳ
        int pageNo = getParameterInt("pageNo", request);
        if(pageNo==0){
        	pageNo = 1 ;
        }
        request.setAttribute("pageNo", pageNo) ;
        
		String buttonType=getParameter("ButtonType", request);
		String buttonTypeName = getParameter("ButtonTypeName",request);
		if(buttonTypeName==null) buttonTypeName = "";
		String parentCode = getParameter("parentCode", request) ;
		String tableName = getParameter("tableName", request) ;
		String parentTableName = getParameter("parentTableName", request) ;
		String moduleType = getParameter("moduleType", request) ;
		moduleType = moduleType==null?"":moduleType ;
		
		request.setAttribute("tableName", tableName) ;
		
		LoginBean lg = getLoginBean(request) ;
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(BaseEnv.tableInfos, tableName);
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		MOperation mop = GlobalsTool.getMOperationMap(request);
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
		
		Result rs=null;
		
		/*
		 * �������޸ģ�keyIds��������Զ���ץȡ����Ϊ����ͳһ��ȡ��Ŀ�ģ�Ҫ������ȥ����ͬ�ĵ��ݺţ������в��ٵĵ����б�ͬʱ��ʾ����ϸ���е����ݣ�
		 * ����ͬһ���ݺŻ���ֶ�Σ��Ӷ�ʹ��չ��Ť�ظ�ִ��
		 **/
		String varkeyIds = getParameter("varKeyIds", request);
		String[] keyIds = null;
		if(varkeyIds != null && !"".equals(varkeyIds)){
			keyIds= varkeyIds.split("\\|") ;
			if(keyIds != null){
				ArrayList list=new ArrayList();
				for(String keyi:keyIds){
					if(!list.contains(keyi)){
						list.add(keyi);
					}
				}
				keyIds=new String[list.size()];
				for(int i=0;i<list.size();i++){
					keyIds[i]=list.get(i).toString();
				}
			}
		}
		
		//����չ��ť����ִ���Զ���define�ļ��е����
		if(buttonType.equals("execDefine")){			
			String defineName=getParameter("defineName",request);
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			if("StatCheckReady_Add".equals(defineName) || "NoStatCheckReady_Del".equals(defineName)||"CheckSeq".equals(defineName)||"DelCheckSeq".equals(defineName)){
				rs = dbmgt.checkReady(defineName, getParameter("varKeyIds", request), getLoginBean(request).getId(), resources, getLocale(request)) ;
			}else{
				rs = dbmgt.defineDelSql(defineName, keyIds, this.getLoginBean(request).getId(),resources,this.getLocale(request),request.getParameter("defineInfo"));
			}
			//ͬ����ʱͨѶ�Ĳ���
			//ͣ�ò���
			if("StopValue_BaseInfo_tblDepartment".equals(defineName)){
				String deptCode[]=empMgt.getDeptCodeById(keyIds);
				MSGConnectCenter.deleteObj(deptCode,"dept");
			}
			
			//���ò���
			if("OpenValue_BaseInfo_tblDepartment".equals(defineName)){
				MSGConnectCenter.addEnableDept(keyIds);
			}
			
			// ͣ��ְԱ
			if ("StopValue_BaseInfo_tblEmployee".equals(defineName)) {
				MSGConnectCenter.deleteObj(keyIds,"employee");
			}
			
			// ����ְԱ
			if ("OpenValue_BaseInfo_tblEmployee".equals(defineName)) {
				MSGConnectCenter.refreshEmpInfo(keyIds);
			}
			if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS&& rs.getRetCode()!=ErrorCanst.RET_DEFINE_SQL_ALERT){
				String backUrl = "/UserFunctionQueryAction.do?tableName="+tableName
				   +"&parentCode="+parentCode+"&parentTableName="+parentTableName
				      +"&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex");
				if(rs.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
	            	// �Զ���������Ҫ�û�ȷ��
	            	ConfirmBean confirm=(ConfirmBean)rs.getRetVal();
	            	String content=dbmgt.getDefSQLMsg(getLocale(request).toString(),confirm.getMsgContent());
	            	String jsConfirmYes="";
	            	String jsConfirmNo="";
	            	
                	jsConfirmYes="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                				+":"+confirm.getYesDefine()+";';this.parent.extendSubmit('"+defineName+"','"+buttonTypeName+"',true);";
                	if(confirm.getNoDefine().length()>0){
                		jsConfirmNo="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
                				   +":"+confirm.getNoDefine()+";';this.parent.extendSubmit('"+defineName+"','"+buttonTypeName+"',true);";
                	}
	                
	            	EchoMessage.confirm().add(content).setJsConfirmYes(jsConfirmYes)
	            						 .setJsConfirmNo(jsConfirmNo).setAlertRequest(request);
	            	
	            }else{
					SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
	            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
						EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
					} else {
						EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl(backUrl).setAlertRequest(request);
					}
				}
				return getForward(request, mapping, "message");
			}else{ 
				int operation=14;
	        	String locale = getLocale(request).toString() ;
	        	
	        	 for(int i=0;keyIds !=null && i<keyIds.length;i++){
	 	        	String keyId=keyIds[i];
	 	        	/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
	 		        OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName) ;
	 		        if(keyId.indexOf("hasChild")>0)keyId=keyId.substring(0,keyId.indexOf(";hasChild"));
	 				Result result = dbmgt.detail(tableName, map, keyId, lg.getSunCmpClassCode(),props,lg.getId(),isLastSunCompany,"");
	 				
	 				HashMap values = (HashMap) result.getRetVal() ;
	 				//����Ƿ����Ѿ��½���ڼ� 				
	 				String billTypeName2=getModuleNameByLinkAddr(request, mapping);
	 				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),buttonTypeName,billTypeName2);
	        	 }
	        	
	        	 
        		 String msg=this.getMessage(request, "oa.bbs.operationOk");
        		 if(rs.retVal != null){
        			 msg = rs.retVal+"";
        		 }
        		 String backUrl=null;
        		 if(rs.getRetCode()==ErrorCanst.RET_DEFINE_SQL_ALERT && rs.retVal instanceof Object[]){
        			 msg =((Object[])rs.retVal)[0]+"";
        			 backUrl =((Object[])rs.retVal)[1]+"";
        		 }
        		 if (backUrl!=null && backUrl.length() > 0) {
					EchoMessage.info().add(msg).setGoUrl(backUrl).setAlertRequest(request);
				}else{
					if(request.getParameter("id")!= null && request.getParameter("id").length() > 0){
		        		 //id��Ϊ��˵�����Ǵ������������	   
		        		 EchoMessage.success().add(msg)
		        		 .setBackUrl("/UserFunctionAction.do?tableName="+tableName+"&keyId="+request.getParameter("id")
								 +"&operation=5&parentCode="+parentCode+"&parentTableName="+parentTableName
								 	+"&moduleType="+moduleType+"&f_brother="+request.getParameter("f_brother")+
								 	"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex")+
								 	"&popWinName="+request.getParameter("popWinName"))
								 	.setAlertRequest(request);
		        	 }else{
						EchoMessage.success().add(msg)
									 .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
											 +"&parentCode="+parentCode+"&parentTableName="+parentTableName
											 	+"&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
									 .setAlertRequest(request);
					}
	        	 }
				return getForward(request, mapping, "message");
			}
		}else if("custom".equals(buttonType)){
			request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
			StringBuffer ids = new StringBuffer();
			for (String id : keyIds) {
				ids.append("'"+id+"',");
			}
			int pageSize = "".equals(request.getParameter("pageSizeBy")) || null == request.getParameter("pageSizeBy")?
					25:Integer.parseInt(request.getParameter("pageSizeBy"));
			int startNo = (pageNo-1)*pageSize + 1;
			int endNo = pageNo*pageSize;

			String idsStr = ids.toString().substring(0, ids.length()-1);
			Result result = new ApplyGoodsBillSum().searchByWorkFlowNode(idsStr);
			if(Integer.parseInt(result.getRetVal().toString()) != 0){
				EchoMessage.error().add(
						getMessage(request, "apply.goods.sum.err")).setBackUrl(
						"/UserFunctionQueryAction.do?tableName=tblApplyGoodsBill&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);

				return getForward(request, mapping, "message");
			}
			result =  new ApplyGoodsBillSum().searchApplyGoodsByids(idsStr,startNo,endNo);
			request.getSession().setAttribute("ids", idsStr);
			request.getSession().setAttribute("applys", (List<ApplyGoodsDecBean>)result.getRetVal());
			request.setAttribute("pageSumList", getSumPageList(result.getTotalPage(), pageSize));
			request.setAttribute("pageSizeBy", pageSize);
			request.setAttribute("pageNo", pageNo);

			if(result.getTotalPage() > pageSize){
				request.setAttribute("enable", "true");
			}

			return getForward(request, mapping, "applyGoodsSum");
		}else if("handover".equals(buttonType)){
			request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
			String wakeUp = getParameter("wakeUp", request) ;//���ѷ�ʽ
			String newCreateBy = getParameter("newCreateBy", request) ;//�ֹ�����
			ArrayList<DBTableInfoBean> brotherTable = DDLOperation.getBrotherTables(tableName, map);
			StringBuffer sb = new StringBuffer() ;
			for(String str : keyIds){
				sb.append("'").append(str).append("',") ;
			}
			String keyIdstr = sb.toString().substring(0, sb.toString().length()-1) ;
			TabMgt tabMgt = new TabMgt() ;
			/**��ѯ��Ҫ�ƽ��Ŀͻ��Ƿ������**/
			Result rss2 = tabMgt.selectCRMCustomerIsApproved(tableName, keyIdstr) ;
			if(rss2.retCode==ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.error().add(this.getMessage(request, "Auditing.Gross.error"))
				   					.setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
				   					.setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
			
			/**���¿ͻ�������**/
			Result rss = tabMgt.updateCRMNewCreateBy(tableName, brotherTable, keyIdstr, newCreateBy) ;
			if(rss.retCode!=ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.error().add(this.getMessage(request, "crm.customer.remove.failture"))
								   .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
								   .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
			
			/**��Ϣ����**/
			String message = getMessage(request, "crm.customer.remove.message", lg.getEmpFullName(), "") ;
			if(wakeUp!=null && wakeUp.length()>0){
				String[] arrWakeUp = wakeUp.split(",") ;
				for(String wakeup : arrWakeUp){
					Thread wakeupThread = new Thread(new NotifyFashion(lg.getId(), message, message,newCreateBy, Integer.parseInt(wakeup),"no","")) ;
					wakeupThread.start() ;//����һ��֪ͨ�̡߳�
				}
			}
		}else if("sendEmail".equals(buttonType)){
			List list=(ArrayList)new EMailMgt().selectAccountByUser(getLoginBean(request).getId()).getRetVal();
			
			MailinfoSettingBean obj =null ;
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					MailinfoSettingBean a=(MailinfoSettingBean)list.get(i);
					if(a.getDefaultUser().equals("1")){
						obj=a;
						break;
					}
				}
			}
			if(obj==null){
				EchoMessage.info().add(getMessage(request, "com.oa.notsetMail"))
   				  				  .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName+"&checkTab=Y"
   				  							+"&winCurIndex="+request.getParameter("winCurIndex"))
   				  				  .setNotAutoBack()
   				  				  .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
			StringBuffer sb = new StringBuffer() ;
			for(String str : keyIds){
				sb.append("'").append(str).append("',") ;
			}
			String keyId = sb.toString().substring(0, sb.toString().length()-1) ;
			Result result = new TabMgt().selectSMSorEmailById(tableName, keyId, "ClientEmail") ;
			String strEmail = "" ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<String> emails = (ArrayList<String>) result.getRetVal() ;
				for(String email : emails){
					if(email != null && !"".equals(email) && email.contains("@")){
						strEmail += email+"," ;
					}
				}
			}
		    request.setAttribute("defaultAccount", obj.getMailaddress());
		    request.setAttribute("emailUser", obj.getId()) ;
		    request.setAttribute("strEmail", strEmail) ;
		    return new ActionForward("/vm/classFunction/sendBill.jsp") ;
		}else if("sendSMS".equals(buttonType)){
			String sendMessage = getParameter("sendMessage", request) ;
			StringBuffer sb = new StringBuffer() ;
			for(String str : keyIds){
				sb.append("'").append(str).append("',") ;
			}
			String keyId = sb.toString().substring(0, sb.toString().length()-1) ;
			Result result = new TabMgt().selectSMSorEmailById(tableName, keyId, "Mobile") ;
			int sucessNumber = 0 ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<String> mobiles = (ArrayList<String>) result.getRetVal() ;
				if (BaseEnv.telecomCenter != null) {
					sucessNumber +=BaseEnv.telecomCenter.send(sendMessage,mobiles.toArray(new String[0]),getLoginBean(request).getId());				
					
				}else{
					EchoMessage.info().add(getMessage(request, "sms.not.start"))
					   				  .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName+"&checkTab=Y"
					   						  	 +"&winCurIndex="+request.getParameter("winCurIndex"))
					   				  .setNotAutoBack()
					   				  .setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
				EchoMessage.success().add(getMessage(request, "sms.send.number",String.valueOf(sucessNumber)))
								   .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName+"&checkTab=Y"
										   	  +"&winCurIndex="+request.getParameter("winCurIndex"))
								   .setNotAutoBack()
								   .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
		}else if("dataMove".equals(buttonType)){
			String defineName=getParameter("dataDefineName",request);
			String classCode = getParameter("classCode", request) ;
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			if("ROOT".equals(classCode)){
				classCode = "";
			}
			rs=dbmgt.defineDataMoveSql(defineName, keyIds, tableName,  classCode.replaceAll("#", ""), this.getLoginBean(request).getId(),resources,this.getLocale(request));
			if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
							   +"&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
				}
				return getForward(request, mapping, "message");
			}else{
				int operation=14;
	        	String locale = getLocale(request).toString() ;	        	
	        	for(int i=0;i<keyIds.length;i++){
	 	        	String keyId=keyIds[i];
	 	        	/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
	 		        OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName) ;
	 				Result result = dbmgt.detail(tableName, map, keyId, lg.getSunCmpClassCode(),props,lg.getId(),isLastSunCompany,"");
	 				HashMap values = (HashMap) result.getRetVal() ;
	 				//����Ƿ����Ѿ��½���ڼ� 				
	 				String billTypeName2=getModuleNameByLinkAddr(request, mapping);
	 				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),buttonTypeName,billTypeName2);
	        	}
				EchoMessage.success().add(this.getMessage(request, "com.data.move.success"))
									 .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
											 +"&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
									 .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
		}else if("billExport".equals(buttonType)){
			if(keyIds==null || keyIds.length==0){
				keyIds = getParameters("keyId", request) ;
			}
			boolean isUpdate = false;
			if(getParameters("id", request) != null){
				//�޸Ľ����������id
				isUpdate = true;
				keyIds = getParameters("id", request) ;
			}
	        
        	File file = new File("../../AIOBillExport") ;
			if(!file.exists()){
				file.mkdirs() ;
			}
			String tableDisplay=GlobalsTool.getTableInfoBean(request.getParameter("tableName")).getDisplay().get(getLocale(request).toString());
			String fileName = file.getCanonicalPath()+"\\"+tableDisplay+"_"+getLoginBean(request).getEmpFullName()+"_"+(BaseDateFormat.format(new Date(),
                    BaseDateFormat.yyyyMMdd).replaceAll("-", ""))+".xls" ;
			Result result=new Result();
			ArrayList<ExportField> exportList = new ArrayList<ExportField>();
			ArrayList<HashMap> exportValuesList = new ArrayList<HashMap>();
			
			//����������ݣ�������ݿ��������һ��������Ϊ��������
			if("true".equals(request.getParameter("example"))){
				keyIds = new String[]{"example"};
			}
			if("tblBOM".equals(tableName) && GlobalsTool.getVersion()==8){
				
			}else{
				for(int i=0;keyIds != null && i<keyIds.length;i++){
					String keyex =keyIds[i];
					if(keyex.indexOf(";hasChild;")>0){
						//bc308c64_1206041611063070096;hasChild;00003
						keyex = keyex.substring(0,keyex.indexOf(";hasChild;"));
					}
					request.setAttribute("detail", "detail");
	                request.setAttribute("detailOk", "1");
					this.updatePrepare(mapping, form, request, response, keyex);
					if("true".equals(request.getAttribute("updateNoRight"))){
						EchoMessage.error().add("û�иõ���Ȩ�ޣ����ܵ�������")
						 .setNotAutoBack().setAlertRequest(request);
						return getForward(request, mapping, "message");
					}
					HashMap exportValues = new HashMap();
					exportValuesList.add(exportValues);
					exportList = new ArrayList<ExportField>();
					ExportData.export(request,exportList,exportValues);		
					if(keyIds[i].indexOf(";hasChild;")>0){
						//bc308c64_1206041611063070096;hasChild;00003
						String pcs  = keyIds[i].substring(keyIds[i].indexOf(";hasChild;")+";hasChild;".length());
						if(pcs.length() > 0){
							Result pcsr = userMgt.getChildData(tableName, pcs);
							if(pcsr.retVal !=null){
								ArrayList<String> pcl = (ArrayList<String>)pcsr.retVal;
								for(String ckey:pcl){
									updatePrepare(mapping, form, request, response, ckey);
									exportValues = new HashMap();
									exportValuesList.add(exportValues);
									exportList = new ArrayList<ExportField>();
									ExportData.export(request,exportList,exportValues);	
								}
							}
						}
					}
				}
				
				FileOutputStream fos = new FileOutputStream(fileName);
				result=ExportData.billExport(fos,tableDisplay, exportList, exportValuesList,null);
				fos.close();
			}
			String backURL = "/UserFunctionQueryAction.do?tableName="+tableName+"&checkTab=Y"
				+"&parentTableName="+parentTableName+"&moduleType="+moduleType+"&winCurIndex="+request.getParameter("winCurIndex") ;
			String fromCrm = getParameter("fromCRM", request) ;
			if("clientList".equals(fromCrm)){
				backURL = "/ClientAction.do?operation=4&type=main&public="+getParameter("public", request)+"&tableName="+tableName ;
			}
			if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
				new DynDBManager().addLog(15, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "��������"+tableDisplay, tableName, tableDisplay,"");
				
				String mime = request.getSession().getServletContext().getMimeType(".xls");
				String fn =tableDisplay+"_"+getLoginBean(request).getEmpFullName()+"_"+(BaseDateFormat.format(new Date(),
                        BaseDateFormat.yyyyMMdd).replaceAll("-", ""))+".xls";
	        	if(mime == null || mime.length() == 0){
	        		mime = "application/octet-stream; CHARSET=utf8";
	        		response.setContentType(mime);
	        		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
	        	}else{
	        		response.setContentType(mime);
	        		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
	        	}
	        	FileHandler.readFile("../../AIOBillExport/"+fn,response);
	        	return null;
	        	
//	        	String fromPage = getParameter("fromPage", request) ;
//	        	if(isUpdate){
//	        		request.removeAttribute("print");
//	        		backURL = "/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyIds[0]+"&moduleType="+moduleType+
//	        			"&f_brother="+request.getParameter("f_brother")+"&operation=5&winCurIndex="+request.getParameter("winCurIndex")+
//	        			"&parentCode="+request.getParameter("parentCode")+
//	        			"&parentTableName="+parentTableName+"&saveDraft=&popWinName="+request.getParameter("popWinName") ;
//	        		EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
//		        			+GlobalsTool.encode(tableDisplay+"_"+getLoginBean(request).getEmpFullName()+"_"+(BaseDateFormat.format(new Date(),
//		                            BaseDateFormat.yyyyMMdd).replaceAll("-", ""))+".xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
//		        			getMessage(request, "com.export.success.over")+fileName).setGoUrl("/common/message.jsp")
//				   					 	.setBackUrl(backURL).setNotAutoBack()
//				   						.setAlertRequest(request);
//	        	}else{	        	
//	        		EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
//	        			+GlobalsTool.encode(tableDisplay+"_"+getLoginBean(request).getEmpFullName()+"_"+(BaseDateFormat.format(new Date(),
//	                            BaseDateFormat.yyyyMMdd).replaceAll("-", ""))+".xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
//	        			getMessage(request, "com.export.success.over")+fileName)
//			   					 	.setBackUrl(backURL).setNotAutoBack()
//			   						.setAlertRequest(request);
//	        	}
	        	/*String downUrl = "/ReadFile?tempFile=export&fileName="+GlobalsTool.encode(tableDisplay+"_"+getLoginBean(request).getEmpFullName()+".xls");
				request.setAttribute("msg", downUrl);*/
	        }else{
	        	EchoMessage.error().add(getMessage(request, "com.export.failure")).setBackUrl(backURL)
	        						 .setNotAutoBack().setAlertRequest(request);
	        }
	        return getForward(request, mapping, "message");
            
		}else if("taskAllot".equals(buttonType)){
			String defineName=getParameter("taskAllotDefineName",request);
			String classCode = getParameter("classCode", request) ;
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			rs=dbmgt.defineTaskAllotSql(defineName, keyIds, classCode, this.getLoginBean(request).getId(), resources, this.getLocale(request));
			if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
				if(rs.retCode==ErrorCanst.RET_DEFINE_SQL_ERROR){
					String[] msgCode = (String[])rs.getRetVal() ;
					EchoMessage.error().add(getMessage(request, msgCode[0]))
										.setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
	                    					+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
	                    					.setRequest(request) ;
				}else{
	                EchoMessage.error().add(getMessage(request, "crm.tast.error"))
	                    			.setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
	                    					+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
	                    			.setRequest(request);
				}   
				
				return getForward(request, mapping, "message");
			}else{
				EchoMessage.success().add(this.getMessage(request, "crm.tast.ok"))
									 .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
											 +"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
									 .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
			
		}else if("keyWord".equals(buttonType)){
			
			String defineName=getParameter("defineName",request);
			String keyword = getParameter("checkFieldName", request) ;
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			rs=dbmgt.checkKeyword(defineName, tableName,keyword, this.getLoginBean(request).getId(),resources,this.getLocale(request));
			if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
							   +"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
				}
				return getForward(request, mapping, "alert");
			}else{
				EchoMessage.success().add(this.getMessage(request, "com.data.move.success"))
									 .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
											 +"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
									 .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
		}else if("draftAudit".equals(buttonType)){
			/*�ݸ���������*/			
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
			
	        String detailId = "" ;
	        String mainTableName=tableName;
	        
	        for(int i=0;i<keyIds.length;i++){
	        	String keyId=keyIds[i];
	        	if(mainTableName.equals("tblView")){
	        		String[] billTypes=request.getParameter("billTypes").split("\\|");
	        		tableName=GlobalsTool.getEnumerationValue("BillType", this.getLocale(request).toString(), billTypes[i]);
	        		tableInfo=DDLOperation.getTableInfo(map, tableName);
	        	}
	        	/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
		        OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName) ;
				Result result = dbmgt.detail(tableName, map, keyId, lg.getSunCmpClassCode(),props,lg.getId(),isLastSunCompany,"");
				HashMap values = (HashMap) result.getRetVal() ;
				//����Ƿ����Ѿ��½���ڼ�
				if(values.get("BillDate")!=null){
					int periodYear=Integer.parseInt(values.get("BillDate").toString().substring(0,4));
					int period=Integer.parseInt(values.get("BillDate").toString().substring(5,7));
					
					Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(this.getLoginBean(request).getId());
					int curPeriodYear=Integer.parseInt(ht.get("NowYear").toString());
					int curPeriod=Integer.parseInt(ht.get("NowPeriod").toString());
					
					if(periodYear<curPeriodYear||(periodYear==curPeriodYear&&period<curPeriod)){
						EchoMessage.error().add(getMessage(request,"com.currentaccbefbill")).setRequest(request);
						return getForward(request, mapping, "message");
					} 
				}
				
				if(workFlow==null || workFlow.getTemplateStatus() == 0){
					values.put("workFlowNodeName", "finish") ;
					values.put("workFlowNode", "-1");
            		values.put("checkPersons", "") ;
            		values.put("finishTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				}else{
					values.put("workFlowNodeName", "notApprove") ;
					values.put("workFlowNode", "0") ;
	            	values.put("checkPersons", ";"+lg.getId()+";") ;
	            	values.put("finishTime","");
				}
				values.put("createTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				//Ҫִ�е�define����Ϣ
		        String defineInfo=request.getParameter("defineInfo");
				result = dbmgt.add(tableInfo.getTableName(), map, values,lg.getId(), 
						path,defineInfo,resources,this.getLocale(request),"quoteDraft",
						getLoginBean(request),workFlow,"false",props);
				
				detailId = values.get("id").toString();
				//if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				//	String[] str = (String[]) result.getRetVal();
				//	detailId = str[0] ;
				//	WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(workFlow.getid());
				//	if(workFlow.getIsStatart()==1 && designBean!=null){
				//		rs=new OAMyWorkFlowMgt().addOAMyWorkFlow(workFlow.getid(),tableName, values,this.getLoginBean(request),this.getLocale(request),
            	//			this.getResources(request),(workFlow.getclassCode().indexOf("00002")==0?true:false),"false");
				//	}
				//}
				if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
					/*���ϵͳ��־*/
		        	int operation=8;
		        	String locale = getLocale(request).toString() ;
		        	String billTypeName2=getModuleNameByLinkAddr(request, mapping);
		        	dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),buttonTypeName,billTypeName2);
		        	
				}else if(result.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
	            	// �Զ���������Ҫ�û�ȷ��
	            	ConfirmBean confirm=(ConfirmBean)result.getRetVal();
	            	String content=dbmgt.getDefSQLMsg(getLocale(request).toString(),confirm.getMsgContent());
	            	String jsConfirmYes="";
	            	String jsConfirmNo="";
	                jsConfirmYes="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
	                			   + ":"+confirm.getYesDefine()+";'; this.parent.draftAudit() ";
	                if(confirm.getNoDefine().length()>0){
	                		jsConfirmNo="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
	                				  + ":"+confirm.getNoDefine()+";'; this.parent.draftAudit() " ;
	                }
	            	EchoMessage.confirm().add(content).setJsConfirmYes(jsConfirmYes).setJsConfirmNo(jsConfirmNo).setAlertRequest(request);
	            	return getForward(request, mapping, "alert");
	            }else {
                	SaveErrorObj saveErrrorObj = dbmgt.saveError(result, getLocale(request).toString(), "");
                	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
    					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
    				} else {
    					EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
    				}                    
                    return getForward(request, mapping, "alert");
                }
			}
	        tableName=mainTableName;
			String from = getParameter("pageType", request) ;
			if("detail".equals(from)){
				EchoMessage.success().add(getMessage(request, "userfunction.draft.success"))
            		.setBackUrl("/UserFunctionAction.do?tableName="+tableName+"&keyId="+detailId+
            				"&f_brother="+(getParameter("f_brother", request)==null?"":getParameter("f_brother", request))
            				+"&moduleType="+(getParameter("moduleType", request)==null?"":getParameter("moduleType", request))
            				+"&parentCode="+(getParameter("parentCode", request)==null?"":getParameter("parentCode", request))
            				+"&parentTableName="+(getParameter("parentTableName", request)==null?"":getParameter("parentTableName", request))
            				+"&popWinName="+(getParameter("popWinName", request)==null?"":getParameter("popWinName", request))
                        	+"&operation=5&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			}else{
				if(mainTableName.equals("tblView")){
					EchoMessage.success().add(getMessage(request, "userfunction.msg.batchSuccess"))
                	.setBackUrl("/UserFunctionQueryAction.do?tableName=" +tableName+"&winCurIndex="
                		+request.getParameter("winCurIndex")).setAlertRequest(request);
				}else{
					EchoMessage.success().add(getMessage(request, "userfunction.msg.batchSuccess"))
                    	.setBackUrl("/UserFunctionQueryAction.do?tableName=" +tableName+"&winCurIndex="
                    		+request.getParameter("winCurIndex")).setAlertRequest(request);
				}
			}
			return getForward(request, mapping, "message") ;
		}else if("saveCheckBill".equals(buttonType)){
			/*����һ�ε��ݣ�Ŀ��ִ��defineУ�鵥���Ƿ���ȷ,���б���˵���ʱ�ȵ��˷���*/
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
			
	        String detailId = "" ;
	        String mainTableName=tableName;
	        
        	String keyId=keyIds[0];
        	
        	/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
        	
        	OAMyWorkFlowMgt oamgt=new OAMyWorkFlowMgt();
            HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId,tableName);
            //�޸�ʱӦ��ȡԭ������ �汾            
            OAWorkFlowTemplate workFlow = flowMap == null?BaseEnv.workFlowInfo.get(tableName): BaseEnv.workFlowInfo.get(flowMap.get("applyType"));
        	
			Result result = dbmgt.detail(tableName, map, keyId, lg.getSunCmpClassCode(),props,lg.getId(),isLastSunCompany,"");
			HashMap values = (HashMap) result.getRetVal() ;
			
			
			//����Ƿ����Ѿ��½���ڼ�
			if(!"CommonFunction".equals(tableInfo.getSysParameter()) && values.get("BillDate")!=null&&!values.get("BillDate").equals("")){
				int periodYear=Integer.parseInt(values.get("BillDate").toString().substring(0,4));
				int period=Integer.parseInt(values.get("BillDate").toString().substring(5,7));
				
				Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(this.getLoginBean(request).getId());
				int curPeriodYear=Integer.parseInt(ht.get("NowYear").toString());
				int curPeriod=Integer.parseInt(ht.get("NowPeriod").toString());
				
				if(periodYear<curPeriodYear||(periodYear==curPeriodYear&&period<curPeriod)){
					request.setAttribute("msg", getMessage(request,"com.currentaccbefbill"));
					return getForward(request, mapping, "blank");
				} 
			}
			//Ҫִ�е�define����Ϣ
	        String defineInfo=request.getParameter("defineInfo");
	        
			if("draft".equals(values.get("workFlowNodeName"))){ //ԭ���ǲݸ壬ִ�вݸ���ʹ���
				values.put("workFlowNodeName", "notApprove") ;
				values.put("workFlowNode", "0") ;
            	values.put("checkPersons", ";"+lg.getId()+";") ;
            	values.put("finishTime","");
            	
            	values.put("createTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				//Ҫִ�е�define����Ϣ
				result = dbmgt.add(tableInfo.getTableName(), map, values,lg.getId(), 
						path,defineInfo,resources,this.getLocale(request),"quoteDraft",
						getLoginBean(request),workFlow,"false",props);
				
				detailId = values.get("id").toString();
				if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
					int operation=8;
		        	String locale = getLocale(request).toString() ;
		        	String billTypeName2=getModuleNameByLinkAddr(request, mapping);
		        	dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),buttonTypeName,billTypeName2);
				}
			}else{ //�ǲݸ壬ִ���޸�
				if("-1".equals(values.get("workFlowNode"))){
	            	request.setAttribute("msg", "�����Ѿ���˽���");
					return getForward(request, mapping, "blank");
				}else if(workFlow==null || workFlow.getTemplateStatus() == 0){
					values.put("workFlowNodeName", "finish") ;
					values.put("workFlowNode", "-1");
	        		values.put("checkPersons", "") ;
	        		values.put("finishTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				}
				values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		        // ��ѯ��ǰ�������Ƿ��Ѿ�������������������Ѿ���ˣ�������������ݲ������޸�
				result = dbmgt.update(tableInfo.getTableName(), map, values,lg.getId(), 
						defineInfo,resources,this.getLocale(request),"",
						getLoginBean(request),workFlow,props);
				if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
					int operation=14;
					String locale = getLocale(request).toString() ;
//	        		dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),buttonTypeName);
				}
			}

			
			if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg", "OK");
				return getForward(request, mapping, "blank");
			}else if(result.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
            	// �Զ���������Ҫ�û�ȷ��
            	ConfirmBean confirm=(ConfirmBean)result.getRetVal();
            	String content=dbmgt.getDefSQLMsg(getLocale(request).toString(),confirm.getMsgContent());
                String msg="defineInfo:"+confirm.getFormerDefine()+":"+confirm.getYesDefine()+":"+confirm.getNoDefine()+":"+content;
                request.setAttribute("msg", msg);
				return getForward(request, mapping, "blank");
            }else {
            	SaveErrorObj saveErrrorObj = dbmgt.saveError(result, getLocale(request).toString(), "");
            	request.setAttribute("msg", saveErrrorObj.getMsg());
				return getForward(request, mapping, "blank");
            }	        
		}else if("popDefine".equals(buttonType)){
			String defineName=getParameter("defineName",request);
			String popReturnVal = getParameter("popReturnVal", request) ;
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			rs=dbmgt.popDefineSql(defineName, keyIds, tableName,  popReturnVal, this.getLoginBean(request).getId(),resources,this.getLocale(request));
			if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
							   +"&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
				}
				return getForward(request, mapping, "message");
			}else{
				int operation=14;
	        	String locale = getLocale(request).toString() ;	        	
	        	for(int i=0;i<keyIds.length;i++){
	 	        	String keyId=keyIds[i];
	 	        	/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
	 		        OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName) ;
	 				Result result = dbmgt.detail(tableName, map, keyId, lg.getSunCmpClassCode(),props,lg.getId(),isLastSunCompany,"");
	 				HashMap values = (HashMap) result.getRetVal() ;
	 				//����Ƿ����Ѿ��½���ڼ� 				
	 				String billTypeName2=getModuleNameByLinkAddr(request, mapping);
	 				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),buttonTypeName,billTypeName2);
	        	}
				EchoMessage.success().add(buttonTypeName+"ִ�гɹ�")
									 .setBackUrl("/UserFunctionQueryAction.do?tableName="+tableName
											 +"&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
									 .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		return query(mapping, form, request, response);
	}

	protected ActionForward buttonAuditing(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
            Exception {

		ActionForward forward = null;
        String tableName = getParameter("tableName", request);
        String []keyid = getParameter("varKeyIds", request).split(";") ;
        Hashtable allTables = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        String optionType = getParameter("optionType", request) ;
        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
        Result rs = dbmgt.defineAuditingOrReverseSql(tableName, allTables, keyid, getLoginBean(request).getId(), optionType,resources,this.getLocale(request)) ;
        if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
			SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}

		}else if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, (null!=optionType && optionType.equals("auditing"))?"common.AuditingSuccess":"oa.returnadudtiingSuccess"))
                    .setBackUrl("/UserFunctionQueryAction.do?tableName=" +
                                tableName+"&winCurIndex="+request.getParameter("winCurIndex")
                    ).setAlertRequest(request);
		}
        return getForward(request, mapping, "message");
	}

	protected ActionForward buttonReverse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
            Exception {

		ActionForward forward = null;
        String tableName = getParameter("tableName", request);
        String []keyId = getParameter("varKeyIds", request).split(";") ;
        Hashtable allTables = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        String optionType = getParameter("optionType", request) ;

        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
        Result rs = dbmgt.defineAuditingOrReverseSql(tableName, allTables, keyId, getLoginBean(request).getId(), optionType,resources,this.getLocale(request)) ;
        if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
			SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
		}else if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, (null!=optionType && optionType.equals("auditing"))?"common.AuditingSuccess":"oa.returnadudtiingSuccess"))
                    .setBackUrl("/UserFunctionQueryAction.do?tableName=" +
                                tableName+"&winCurIndex="+request.getParameter("winCurIndex")
                    ).setAlertRequest(request);
		}
        return getForward(request, mapping, "message");
	}
	/**
	 * �������кŵ�������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	  protected ActionForward addSequence(ActionMapping mapping, ActionForm form,
              HttpServletRequest request,
              HttpServletResponse response) throws Exception {
		  
		  String seq=request.getParameter("seq");
		  seq=seq==null?"":seq;
		  request.setAttribute("seq", seq);
		  String seqFname=request.getParameter("seqFname");
		  request.setAttribute("tableName",seqFname.split("_")[0]);
		  request.setAttribute("seqFname",seqFname.split("_")[1]);
		  request.setAttribute("goodsCode",request.getParameter("goodsCode"));
		  request.setAttribute("defSeqStr",request.getParameter("defSeqStr"));
		  request.setAttribute("lastSeq", request.getParameter("lastSeq"));
		  request.setAttribute("newSeq", request.getParameter("newSeq"));
		  //request.setAttribute("lastId", request.getParameter("lastId"));
		  return getForward(request, mapping, "addSequence");
	  }

	  /**
	   * ������кŵ�������
	   * @param mapping
	   * @param form
	   * @param request
	   * @param response
	   * @return
	   * @throws Exception
	   */
	  protected ActionForward stockSequence(ActionMapping mapping, ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		  
		  String seq = getParameter("seq", request);//�Ѿ����õ����к�
		  
		  
		  
		  seq=seq==null?"":seq;
		  String seqStr = getParameter("seqStr", request) ;
		  if(seqStr!=null && seqStr.length()>0){
			  seq = seqStr ;
		  }
		  request.setAttribute("seq", seq);
		  if(seq!=null&&seq.length()>0){
			  Result rs=new IniGoodsMgt().querySeqTime(seq.substring(0,seq.length()-1).replaceAll("~", ","));
			  if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				  request.setAttribute("seqTime", rs.retVal);
			  }
		  }
		  String seqFname=request.getParameter("seqFname");
		  request.setAttribute("tableName",seqFname.split("_")[0]);
		  request.setAttribute("goodsCode",request.getParameter("goodsCode"));
		  request.setAttribute("seqFname",seqFname.split("_")[1]);
		  request.setAttribute("defSeqStr",request.getParameter("defSeqStr"));
		  request.setAttribute("lastSeq", request.getParameter("lastSeq"));
		  request.setAttribute("newSeq", request.getParameter("newSeq"));
		  
		  String queryString = request.getQueryString() ;
		  request.setAttribute("queryString", queryString) ;
		  //String colName   = getParameter("colname", request) ;	 //���к�����
		  String goodsCode = getParameter("goodsCode", request) ;//��ƷID
		  String stockCode = getParameter("stockCode", request) ;//�ֿ�
		  String tableName = getParameter("childTableName", request) ; //��ǰ����
		  
		  Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
	      DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
	      
	      String propSQL = "" ;
	      if(tableInfo!=null){
	    	  for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
	              DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
	              if(fieldInfo.getFieldName().endsWith("Before")){
	            	  String goodProp = getParameter(fieldInfo.getFieldName(), request) ;
	            	  if(goodProp!=null && goodProp.trim().length()>0){
	            		  goodProp = GlobalsTool.toChinseChar(goodProp) ;
	            		  propSQL += " and "+fieldInfo.getFieldName().replace("Before", "")+"='"+goodProp+"'" ;
	            	  }
	              }else if (fieldInfo.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE 
	            		  && "GoodsField".equals(fieldInfo.getFieldSysType()) 
	            		  && GlobalsTool.getPropBean(fieldInfo.getFieldName()).getIsSequence()!=1) {
	            	  String goodProp = getParameter(fieldInfo.getFieldName(), request) ;	            	  
	            	  goodProp = goodProp==null?"":GlobalsTool.toChinseChar(goodProp) ;
	            	  propSQL += " and "+fieldInfo.getFieldName()+"='"+goodProp+"'" ;
	              }
	          }
	      }
	      if(propSQL != null && propSQL.indexOf("") > -1){
	    	  propSQL = propSQL.replaceAll("@join:amp;", "&"); //&ת��
	      }
	      //�����ֶ�
	      DBFieldInfoBean dbField = DDLOperation.getFieldInfo(allTables, tableName, "Seq") ;
	      String stockTable = "" ;
	      if(dbField!=null && dbField.getSelectBean()!=null){
	    	  stockTable = dbField.getSelectBean().getStockTable() ;
	      }
	      int pageNo = getParameterInt("pageNo", request) ;
	      int pageSize = getParameterInt("pageSize", request) ;
	      if(pageNo == 0) {
              pageNo = 1;
          }
	      if(pageSize == 0) {
	            pageSize = GlobalsTool.getPageSize();
	      }
	      Result rs_seq = new IniGoodsMgt().querySeqByGoodsCode(goodsCode, stockCode, propSQL,stockTable,pageNo,pageSize) ;
	      if(rs_seq.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
	    	  request.setAttribute("seqList", rs_seq.getRetVal()) ;
	    	  request.setAttribute("pageBar", pageBar(rs_seq, request)) ;
	      }
		  return getForward(request, mapping, "stockSequence") ;
	  }
	  
	  //���±�ʶ������ɾ��----������
	  //DOG_CHECK_HS_FUNCTION_START
	  //DOG_CHECK_HS_FUNCTION_END
	  private String PopGetDefaultValue(PopField mPopField,String str,HttpServletRequest req) throws Exception{
		  String temp = str;
		  PopField fv = mPopField;
		  HttpServletRequest request = req; 
		  
		  if ((temp == null|| (temp != null && temp.trim().length() == 0))&&(req.getParameter("src")!=null&&req.getParameter("src").equals("menu"))) {
			  	String dv=req.getParameter(fv.asName.replace(".", "_"));
			  	if(dv!=null){
			  		dv=java.net.URLDecoder.decode(dv,"UTF-8");
			  	}
		  		
			  	if(dv==null||(dv!=null&&dv.trim().length()==0)){
					dv= fv.getDefaultValue();
					if (dv != null && dv.length() > 0) {
						if (dv.indexOf("@MEM:") >= 0|| dv.indexOf("@Sess:") >= 0) {
							ArrayList sysParam = new ArrayList();
							ArrayList sessParam = new ArrayList();
							ArrayList codeParam = new ArrayList();
							ArrayList queryParam = new ArrayList();
	
							ConfigParse.parseSentenceGetParam(dv,new ArrayList(), sysParam,sessParam, codeParam, queryParam,null);
							LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
							HashMap sysMap = ConfigParse.getSystemParam(sysParam,loginBean.getSunCmpClassCode());
							HashMap sessMap = ConfigParse.getSessParam(loginBean.getId(), sessParam);
							Connection conn = null;
							HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);
							dv = ConfigParse.parseSentencePutParam(dv,new HashMap(), sysMap,new HashMap(), sessMap, codeMap,null,null,this.getLocale(req));
						}
					}
			  	}
				temp = dv.trim();
			}
      	return temp;
      }
	 

	  

	
	  /**
		 * ����˲���
		 * @param mapping  ActionMapping
		 * @param form ActionForm
		 * @param request HttpServletRequest
		 * @param response HttpServletResponse
		 * @return ActionForward
		 * @throws Exception
		 */
		protected ActionForward retAuditing(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			ActionForward forward = getForward(request, mapping, "message");
			String keyId=request.getParameter("keyId");
			String pageType = request.getParameter("pageType");
			if("detail".equals(pageType) || "update".equals(pageType)){
				keyId= request.getParameter("id");
			}
				
			String tableName=request.getParameter("tableName");
			DBTableInfoBean tableInfo=DDLOperation.getTableInfo(BaseEnv.tableInfos, tableName);
			
			String workFlowNodeName="";
			String tempTableName = new DynDBManager().getInsertTableName(tableName);//����CRM��ģ���������
	        Result rst=new AIODBManager().sqlList("select workFlowNodeName,checkPersons from "+tempTableName+" where id='"+keyId+"'", new ArrayList());
	    	if(rst.retVal!=null&&((ArrayList)rst.retVal).size()>0){
	    		Object[] obj=((Object[])((ArrayList)rst.retVal).get(0));    		
	    		workFlowNodeName=obj[0]==null?"":obj[0].toString();
	    	}
	    	String checkPersons = BaseEnv.workFlowInfo.get(tableName).getAllowVisitor();
	        if(workFlowNodeName.equals("notApprove") 
	        		|| !(checkPersons.contains(","+getLoginBean(request).getId()+",")
	        		|| checkPersons.contains(","+getLoginBean(request).getDepartCode()+","))){
	        	request.setAttribute("noback", true) ;
	        	EchoMessage.error().add(this.getMessage(request, "common.msg.hasAudit")).setcomeAdd(1).setAlertRequest(request);
	        	if("detail".equals(pageType) || "update".equals(pageType)){
	        		return getForward(request, mapping, "alert");
	        	}else{
	        		return getForward(request, mapping, "commonMessage");
	        	}
	        }
	        /*�жϵ�ǰ�����Ƿ��Ѿ�����ƾ֤*/
	        Result rsAcc=dbmgt.hasCreateAcc(tableName,keyId);
	    	if(rsAcc.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    		if(rsAcc.retVal!=null){//����Ѿ�����ƾ֤�������������ʾ
	    			String[] str=(String[])rsAcc.retVal;
	    			EchoMessage.error().add(this.getMessage(request, "common.hasCreateAcc.Oper.error",str[0],str[1])).setAlertRequest(request);
	                return getForward(request, mapping, "alert");
	    		}
	    	}else{
	    		EchoMessage.error().add(rsAcc.retCode,request).setAlertRequest(request);
	            return getForward(request, mapping, "alert");
	    	}
			/*********************************�õ���ǰҪ����˵��ݵ����ڣ��Ƿ����½�ǰ����******************************/
			String sysParamter = tableInfo.getSysParameter();//����Ϣϵͳ����
        	Date time=null;
        	String timeStr="";
        	for(int i=0;i<tableInfo.getFieldInfos().size();i++){
        		DBFieldInfoBean bean=(DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
        		if(bean.getDefaultValue()!=null&&"accendnotstart".equals(bean.getFieldIdentityStr())){
        			AIODBManager aioMgt=new AIODBManager();
        			Result rs=aioMgt.sqlList("select "+bean.getFieldName()+",workFlowNodeName from "+tempTableName+" where id='"+keyId+"'", new ArrayList());
        			if(((ArrayList)rs.retVal).size()>0){
        				timeStr=((Object[])((ArrayList)rs.retVal).get(0))[0].toString();
        				workFlowNodeName=((Object[])((ArrayList)rs.retVal).get(0))[1].toString();
        			}
        		}
        	}
        	if(timeStr.length()>0){
	        	time=BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
	        	int currentMonth = 0;
	        	int currentYear=0;
	            if (null != time) {
	                currentMonth = time.getMonth() + 1;
	                currentYear=time.getYear()+1900;
	            }
	            int periodMonth = -1;
	            int periodYear=-1;
	            AccPeriodBean accBean=(AccPeriodBean)BaseEnv.accPerios.get(this.getLoginBean(request).getSunCmpClassCode());
	            if (accBean!=null) {
	                periodMonth = accBean.getAccMonth();
	                periodYear=accBean.getAccYear();
	
	            }
	            if ((currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) 
	            					||periodMonth < 0 ) && currentMonth != 0 && !"draft".equals(workFlowNodeName)) {
	            	if (null != sysParamter) {
                    	boolean flag = false ;
                        if (sysParamter.equals("CurrentAccBefBill") &&(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth))) {
                            flag = true ;
                        }

                        if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") &&
                        		(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) ||periodMonth < 0 )) {
                           flag = true ;
                        }
                        if(flag){
                        	if("detail".equals(getParameter("fromPage", request))){
                        		String curIndex = request.getParameter("winCurIndex") ;
                        		EchoMessage.error().add(getMessage(request,"com.cantupdatebefbill"))
                        						.setBackUrl("/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyId+"&f_brother="+request.getParameter("f_brother") 
                                  					   +"&operation=5&winCurIndex="+("".equals(curIndex)?"&LinkType=@URL:&noBack=true":curIndex))				
                        						.setAlertRequest(request);
                        	}else{
                        		EchoMessage.error().add(getMessage(request,"com.cantRetAuditebefbill")).setAlertRequest(request);
                        	}
                        	return getForward(request, mapping, "message");
                        }
                        
                    }
	           }
	        }
			/*********************************�õ���ǰҪ����˵��ݵ����ڣ��Ƿ����½�ǰ����******************************/
		
			OAMyWorkFlowMgt oaMgt=new OAMyWorkFlowMgt();
			Hashtable table=((Hashtable)BaseEnv.sessionSet.get(this.getLoginBean(request).getId()));
         	table.put("BillOper", "returnAuditing");
			Result rs=oaMgt.execRetCheckDefine(tableName,keyId, this.getLoginBean(request), this.getResources(request), this.getLocale(request));
			
			/*�����classCode*/
			 String parentCode = request.getParameter("parentCode");
			 String moduleType = getParameter("moduleType", request) ;
			 parentCode = parentCode == null ? "" : parentCode;
			 moduleType = moduleType == null ? "" : moduleType;
			  
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				/*�����ֵܱ��f_brother*/
				String f_brother = this.getParameter("f_brother", request);
				f_brother = f_brother == null ? "" : f_brother;
				request.setAttribute("f_brother", f_brother);
				/*��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��*/
				String parentTableName=request.getParameter("parentTableName");
				parentTableName=parentTableName==null?"":parentTableName;
				
				String backUrl = "/UserFunctionQueryAction.do?tableName=" + tableName 
				+ "&operation=" +OperationConst.OP_QUERY 
					+ "&parentCode=" + parentCode + "&f_brother=" + f_brother 
					+ "&checkTab=Y&parentTableName="+parentTableName
					+ "&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex");
				String detailReCheck = request.getParameter("detailReCheck");
				String crmReCheck = request.getParameter("crmReCheck");//true��ʾCRM�ھӷ����
				if(crmReCheck!=null && "true".equals(crmReCheck)){
					String isCRMClient = request.getParameter("isCRMClient");//true��ʾCRM�ͻ��б����
					if(isCRMClient!=null && "true".equals(isCRMClient)){
						backUrl = "/CRMClientAction.do?operation=5&type=detailNew&keyId="+keyId;
					}else{
						backUrl = "/CRMBrotherAction.do?operation=5&tableName=" + tableName+"&keyId="+keyId;
					}
				}else if("detail".equals(pageType) || "update".equals(pageType)){//������������
					backUrl = "/UserFunctionAction.do?tableName=" + tableName 
					+ "&keyId="+keyId+"&moduleType="+moduleType+"&f_brother=" + f_brother 
					+ "&operation="+OperationConst.OP_DETAIL+"&winCurIndex="+request.getParameter("winCurIndex")+
					"&parentCode=" + parentCode + "&parentTableName="+parentTableName
					+ "&saveDraft=&checkTab=Y&popWinName="+request.getParameter("popWinName");
	        	} else if("true".equals(detailReCheck)){
					String saveDraft = request.getParameter("saveDraft");
					String pageNo = request.getParameter("pageNo");
					backUrl = "/UserFunctionQueryAction.do?tableName=" + tableName 
					+ "&operation=" +OperationConst.OP_UPDATE_PREPARE 
						+ "&parentCode=" + parentCode + "&f_brother=" + f_brother 
						+ "&parentTableName="+parentTableName
						+ "&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex") + "&saveDraft=" + saveDraft + "&pageNo=" +pageNo +"&keyId=" + keyId;
				}
				
				
				EchoMessage.success().add(getMessage(request, "define.button.reverse.success")).setBackUrl(backUrl).setAlertRequest(request);
			}else{
	        	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
	        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
				}
	        }
			return forward;
		}
		
	
	/**
	 * ����ھӱ����Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addNeighbour(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String userID=this.getLoginBean(request).getId();
		String[] keyids=request.getParameterValues("keyhaving");
		String tableName=request.getParameter("tableName");
		TabMgt tabmgt=new TabMgt();
		Integer[] orders=new Integer[keyids.length];
		for(int i=0;i<keyids.length;i++){
			String order=request.getParameter(keyids[i]+"orderby");
			if(order==null || "".equals(order)){
				order="0";
			}
			orders[i]=new Integer(Integer.parseInt(order));
		}
		Integer tabStatus = getParameterInt("tab_bottom", request);
		Result result=tabmgt.insertNeighbourDetail(userID,tableName,keyids, orders,tabStatus);
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			
		}
		return query(mapping, form, request, response);
	}
	/**
	* DBTableInfoBean ��tbleName����
	*/
	public class SortDBTable implements Comparator {  
		public int compare(Object o1, Object o2) { 
			DBTableInfoBean table1 = (DBTableInfoBean) o1;
			DBTableInfoBean table2 = (DBTableInfoBean) o2;
			
			if(table1==null || table2==null){
				return 0 ;
			}
			
			String tableName1 = table1.getTableName() ;
			String tableName2 = table2.getTableName() ;
			
			return tableName1.compareToIgnoreCase(tableName2) ;
		}
	}
	
	/**
	 * ��ӵ��ݵ�ʱ�����ҵĹ�������������
	 * @throws BSFException 
	 */
	public Result addOAMyWorkFlow(final String designId,final String tableName,final HashMap values,
			final LoginBean loginBean,final Locale locale,final MessageResources mr,
			final boolean isOAWorkFlow,final String saveType,final Connection conn) throws BSFException{
		Result rss = new OAMyWorkFlowMgt().addOAMyWorkFlow(designId,tableName, values,loginBean,locale,
				mr,conn);
		return rss ;
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
	private ActionForward dealReAudit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tableName = request.getParameter("tableName");
		String keyId = request.getParameter("keyId");
		String colName = request.getParameter("colName");
		String colValue = URLDecoder.decode(request.getParameter("colValue"),"UTF-8");;
		Hashtable alltables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		LoginBean loginBean = this.getLoginBean(request);
		String sql = "update " + tableName + " set " +colName + "='" + colValue + "' where id='" + keyId + "'"; 
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
		String locale = GlobalsTool.getLocale(request).toString();
		HashMap values = new HashMap();
		values.put("id", keyId);
		Result rs  = userMgt.updateReAudit(sql,tableName,alltables,values,keyId,loginBean.getId(),"",resources,this.getLocale(request));
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", "ok");
		}else {
			request.setAttribute("msg", rs.retVal);
		}
		return getForward(request, mapping, "blank");
	}
	

	
	/**
	 * ��ȡbrotherId,�ж��Ƿ�����frame,����CRM�����ֵܱ���ֱ�ӽ����
	 * @param f_brother
	 * @param hasFrame
	 * @return
	 */
	public String getBrotherId(String f_brother,String hasFrame){
		if(hasFrame ==null || "".equals(hasFrame)){
			f_brother = "";
		}
		return f_brother;
	}
	
	private void addColExist(ArrayList<String[]> cols,String[] ss){
		boolean fd = false;
         for(String[] os :cols){
         	if(os[3].equals(ss[3])){
         		fd = true;
         		break;
         	}
         }
         if(!fd){
         	cols.add(ss);
         }
	}
	
	public List getSumPageList(int pageSum,int pageSize){
		List pageSumList = new ArrayList();
		int sumPage = pageSum%pageSize == 0?pageSum/pageSize:pageSum/pageSize+1;
		for(int i = 1;i <= sumPage;i++){
			pageSumList.add(i);
		}
		
		return pageSumList;
	}
}
