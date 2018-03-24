package com.koron.crm.brother;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import sun.security.action.GetLongAction;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.crm.bean.BrotherFieldDisplayBean;
import com.koron.crm.bean.BrotherFieldScopeBean;
import com.koron.crm.bean.CRMBrotherCommentBean;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.koron.crm.brotherSetting.CRMBrotherSettingMgt;
import com.koron.crm.client.CRMClientAction;
import com.koron.crm.client.CRMClientMgt;
import com.koron.crm.salesFlow.CRMSalesFlowMgt;
import com.koron.crm.setting.ClientSetingMgt;
import com.koron.crm.setting.ClientSettingAction;
import com.koron.oa.OACalendar.OACalendarBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.MailinfoSettingBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.bean.ApplyGoodsDecBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.FieldScopeSetBean;
import com.menyi.aio.bean.ImportDataBean;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.alert.AlertCenterMgt;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.ApplyGoodsBillSum;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.importData.ExportField;
import com.menyi.aio.web.importData.ImportDataMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.tab.TabMgt;
import com.menyi.aio.web.tablemapped.TableMappedMgt;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.email.EMailMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.DefineButtonBean;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.ErrorMessage;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.ReportField;
import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;
/**
 * 
 * <p>Title:CRM�ֵܱ�</p> 
 * <p>Description: </p>
 *
 * @Date:Jun 17, 2013
 * @Copyright: �����п���������޹�˾
 * @Author ��ܿ�
 */
public class CRMBrotherAction extends MgtBaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	CRMBrotherMgt mgt = new CRMBrotherMgt();
	DynDBManager dbmgt = new DynDBManager();
	CRMClientMgt clientMgt = new CRMClientMgt();
	CRMBrotherSettingMgt brotherSettingMgt = new CRMBrotherSettingMgt();
	CRMSalesFlowMgt salesFlowMgt = new CRMSalesFlowMgt();
	ClientSetingMgt clientSetingMgt = new ClientSetingMgt() ;
	CRMClientAction clientAction = new CRMClientAction() ;
	String isConsole = "";//true��ʾ�ӹ���̨����
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// ���ܷ���
		String type = getParameter("type", request);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		
		String noApprove = getParameter("noApprove",request);//true ��ʾ�����Ҳ�����ְ�ť
		if (noApprove != null && "true".equals(noApprove)) {
			request.setAttribute("noApprove", true);
		} else {
			request.setAttribute("noApprove", false);
		}
		
		//true��ʾ�ӹ���̨����
		isConsole = getParameter("isConsole", request);
		if (isConsole != null && "true".equals(isConsole)) {
			request.setAttribute("isConsole", "true");
		} else {
			request.setAttribute("isConsole", "");
		}
		
		switch (operation) {
			//�첽��ȡ���
			case OperationConst.OP_QUOTE:
				getToltal(mapping, form, request, response);
				break;
			case OperationConst.OP_ADD_PREPARE:
				forward = addParpareNew(mapping, form, request, response);	
				break;
			case OperationConst.OP_ADD:
				if("comment".equals(type)){
					forward = addComment(mapping, form, request, response);	
				}else if("clientTransfer".equals(type)){
					forward = addClientTransfer(mapping, form, request, response);	
				}else{
					forward = add(mapping, form, request, response);	
				}
				break;
			case OperationConst.OP_UPDATE_PREPARE:
				forward = updateParpare(mapping, form, request, response);	
				break;
			case OperationConst.OP_UPDATE:
				forward = update(mapping, form, request, response);	
				break;
			case OperationConst.OP_DETAIL:
				forward = detail(mapping, form, request, response);	
				break;
			case OperationConst.OP_DELETE:
				if("delPic".equals(type)){
					forward = ajaxDelPic(mapping, form, request, response);	
				}else{
					forward = delete(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_QUERY:
				if("comment".equals(type)){
					forward = commentQuery(mapping, form, request, response);
				}else if("goodsTree".equals(type)){
					forward = goodsTreeQuery(mapping, form, request, response);	
				}else if("goodsQuery".equals(type)){
					forward = goodsQuery(mapping, form, request, response);	
				}else if("ajaxBrotherList".equals(type)){
					forward = brotehrListQuery(mapping, form, request, response);//����ҳ�棬�첽�����ھӱ��б���
				}else if("releateEnumer".equals(type)){
					forward = releateEnumer(mapping, form, request, response);//�첽��ȡ�����ͻ���������Ϣ
				}else if("contactInfo".equals(type)){
					forward = contactInfoQuery(mapping, form, request, response);//�첽��ȡ�����ͻ���������Ϣ
				}else if("PotentialAllocatee".equals(type)){
					forward = PotentialAllocatee(mapping, form, request, response);//��������
				}else if("PotentialCallback".equals(type)){
					forward = PotentialCallback(mapping, form, request, response);//��������
				}else{
					forward = queryNew(mapping, form, request, response);	
				}
				break;
			case OperationConst.OP_EXPORT:
				forward = exportBill(mapping, form, request, response);	
				break;
			case OperationConst.OP_POPUP_SELECT:	
				forward = getDatas(mapping, form, request, response);//��ȡ��Ϣ	
				break;
			case OperationConst.OP_EXTENDBUTTON_DEFINE:
	        	forward = extendDefine(mapping, form, request, response);
	        	break;
			default:
				forward = queryNew(mapping, form, request, response);
				break;	
		}
		return forward;
	}

	
	/**
	 * ��ȡ��Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward getDatas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String RecordBillCount = GlobalsTool.getSysSetting("RecordBillCount");//��ȡϵͳ��������
		LoginBean loginBean = getLoginBean(request);
		String getDataType = getParameter("getDataType",request);
		/*
		String sql = "SELECT * from CRMPotentialClient WHERE createBy =?";
		ArrayList param = new ArrayList();
		param.add(loginBean.getId());
		Result rs = mgt.publicSqlQuery(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			if(list==null || list.size()<=50){
				int topCount = 100;
				if(list!=null){
					topCount = 100 - list.size();
				}
				sql = "UPDATE CRMPotentialClient SET createBy = ? WHERE id in (SELECT Top "+topCount+" id FROM CRMPotentialClient WHERE ( isNull(createBy,'') ='' or createBy='1' ) and zlfl=? ORDER BY createTime )";
				param.add(getDataType);
				rs = mgt.operationSql(sql, param);
			}
		}*/
		String sql = "UPDATE CRMPotentialClient SET createBy = ?,DefineStatus='1' WHERE id in (SELECT Top "+RecordBillCount+" id FROM CRMPotentialClient WHERE ( isNull(createBy,'') ='' or createBy='1' ) and zlfl=? ORDER BY createTime )";
		ArrayList param = new ArrayList();
		param.add(loginBean.getId());
		param.add(getDataType);
		Result rs = mgt.operationSql(sql, param);
		return queryNew(mapping, form, request, response);
	}


	private ActionForward addClientTransfer(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId", request);//ģ��ID
		String[] keyIds = getParameters("keyId", request) ;
		String operationFlag = getParameter("operationFlag",request);//�ж�����ҳ��������ҳ�� 
		
		Result rest = clientSetingMgt.detailCrmModule(moduleId);
		String tableName = "";
		String workFlowNode="-1";
		String workFlowNodeName="finish";
		if(rest.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ClientModuleBean bean = (ClientModuleBean)rest.retVal;
			if(bean!=null){
				tableName = bean.getTableInfo().split(":")[0];
			}
		}
		
		LoginBean loginBean = getLoginBean(request);
		Result rs = clientSetingMgt.detailCrmModule(moduleId);
		ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
		String clientTableName = moduleBean.getTableInfo().split(":")[0];
		String clientChildTableName = moduleBean.getTableInfo().split(":")[1];
		
		//��ȡ����Ĳ���
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = loginBean.getSunCmpClassCode();  //��LoginBean��ȡ����֧������classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMBrotherAction.do?tableName=CRMPotentialClient") ;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		
		String clientFieldsMapping = moduleBean.getClientFieldsMapping();//��Ӧģ�����õ�ӳ���ֶ� Ǳ�ڿͻ��ֶΣ��ͻ������ֶ�
		//����Ĭ���ֶ�
		String mainFields ="id,createBy,lastUpdateBy,createTime,lastUpdateTime,SCompanyID,LastContractTime,ClientNo,moduleId,workFlowNode,workFlowNodeName,";//�����ֶ�
		String childFields = "id,SCompanyID,f_ref,";//��ϸ�ֶ�
		
		
		//��ż��Ψһֵ���ֶ�
		String mainUniFields = ""; //�����������Ψһֵ�ֶ�
		String childUniFields = "";//��Ŵӱ�����Ψһֵ�ֶ�
		
		//����ӳ����ֶ�
		for(String str : clientFieldsMapping.split(",")){
			if(str.indexOf("Contact_") == -1){
				mainFields +=str.split(":")[1]+",";
				DBFieldInfoBean bean = GlobalsTool.getFieldBean(clientTableName, str.split(":")[1]);
				if(bean!=null && bean.getIsUnique()==1){
					mainUniFields +=bean.getFieldName()+",";
				}
			}else{
				str = str.replaceAll("Contact_","");
				childFields +=str.split(":")[1]+",";
				DBFieldInfoBean bean = GlobalsTool.getFieldBean(clientChildTableName, str.split(":")[1]);
				if(bean!=null && bean.getIsUnique()==1){
					childUniFields +=bean.getFieldName()+",";
				}
			}
		}
		
		if(mainFields.endsWith(",")){
			mainFields = mainFields.substring(0,mainFields.length()-1);
		}
		
		if(childFields.endsWith(",")){
			childFields = childFields.substring(0,childFields.length()-1);
		}
		
		
		//���������ӱ�ֵ����
		List<HashMap<String,String>> mainValList = new ArrayList<HashMap<String,String>>();//��������ֶε�ֵ
		
		HashMap<String,HashMap<String,String>> childValMap = new LinkedHashMap<String, HashMap<String,String>>();//�����ϵ�˱��ֶε�ֵ
		//List<HashMap<String,String>> childValList = new ArrayList<HashMap<String,String>>();//�����ϵ�˱��ֶε�ֵ
		OAWorkFlowTemplate workFlow=BaseEnv.workFlowInfo.get(tableName);
		for(String keyId : keyIds){
			if(keyId!=null && !"".equals(keyId)){
				rs = dbmgt.detail("CRMPotentialClient", map, keyId, sunClassCode,props,loginBean.getId(),true,null);
				HashMap rsMap = (HashMap)rs.retVal;//��ȡCRM�ͻ�����ֵ
				if(rsMap !=null){
					
					
					//����ӳ���ֶΣ��鿴�ͻ��������õ�Ψһֵ�Ƿ��ظ�
					HashMap checkUniqueMap = new HashMap();//��ż���ֵmap
					HashMap chilTempdMap = new HashMap();//�����ϵ��map
					
					ArrayList<HashMap> childList = new ArrayList<HashMap>();//���checkUniqueMap������ϵ���ֶ�
					
					for(String str : clientFieldsMapping.split(",")){
						if(str.indexOf("Contact_") == -1){
							String val = String.valueOf(rsMap.get(str.split(":")[0]));
							checkUniqueMap.put(str.split(":")[1], val);
						}else{
							str = str.replaceAll("Contact_","");
							String val = String.valueOf(rsMap.get(str.split(":")[0]));
							chilTempdMap.put(str.split(":")[1], val);
						}
						
					}
					
					childList.add(chilTempdMap);
					checkUniqueMap.put("TABLENAME_"+moduleBean.getTableInfo().split(":")[1],childList);
					
					//����ֶ��Ƿ��д����ظ�ֵ
					String uniqueInfo= new CRMClientAction().checkUniqueFields(mainUniFields, childUniFields, moduleBean.getTableInfo(), checkUniqueMap, this.getLocale(request).toString(),"add",moduleBean);
					
					//���ظ���Ϣ���ر���
					if(!"".equals(uniqueInfo)){
						if(operationFlag!=null && "detail".equals(operationFlag)){
							request.setAttribute("msg",uniqueInfo);
							return getForward(request, mapping, "blank");
						}else{
							EchoMessage.error().add(uniqueInfo).setBackUrl("/CRMBrotherAction.do?tableName=CRMPotentialClient").setAlertRequest(request);
							return getForward(request, mapping, "alert");
						}
						
					}
					
					
					//����������Ϣ
					HashMap<String,String> clientMap = new HashMap<String, String>();
					String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
					clientMap.put("id", IDGenerater.getId());
					clientMap.put("createBy", loginBean.getId());
					clientMap.put("lastUpdateBy", loginBean.getId());
					clientMap.put("createTime",nowDate);
					clientMap.put("lastUpdateTime",nowDate);
					clientMap.put("statusId", "0");
					clientMap.put("SCompanyID", "00001");
					clientMap.put("LastContractTime",nowDate);
					clientMap.put("moduleId",moduleId);
					
					if(workFlow!=null&&workFlow.getTemplateStatus()==1){
						workFlowNode="0";
						workFlowNodeName="notApprove";
					}
					
					clientMap.put("workFlowNode",workFlowNode);
					clientMap.put("workFlowNodeName",workFlowNodeName);
					
					
					//����ӱ���Ϣ
					HashMap<String,String> childMap = new HashMap<String, String>();
					childMap.put("id", IDGenerater.getId());
					childMap.put("SCompanyID","00001");
					childMap.put("f_ref",keyId);
					
					for(String str : clientFieldsMapping.split(",")){
						if(str.indexOf("Contact_") == -1){
							String value = rsMap.get(str.split(":")[0]) == null ? "" : rsMap.get(str.split(":")[0]).toString();
							clientMap.put(str.split(":")[1],value);//��ӳ���ֶε�ֵ����map��
						}else{
							str = str.replaceAll("Contact_","");
							String value = rsMap.get(str.split(":")[0]) == null ? "" : rsMap.get(str.split(":")[0]).toString();
							childMap.put(str.split(":")[1],value);
						}
					}
					mainValList.add(clientMap);
					if(childMap.get("UserName")!=null && !"".equals(childMap.get("UserName"))){
						childValMap.put(keyId, childMap);
					}
				}
			}
		}
		
		
		
		Result result = mgt.addClientTransfer(mainFields, childFields, mainValList,childValMap, loginBean,resources,this.getLocale(request),tableName,request,workFlow);
		
		if(operationFlag!=null && "detail".equals(operationFlag)){
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
			return getForward(request, mapping, "blank");
		}else{
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				//����ǿͻ�����̨���룬��ת������ҳ��
				String backUrl = "/CRMBrotherAction.do?tableName=CRMPotentialClient";
				EchoMessage.success().add("ת�Ƴɹ�").setBackUrl(backUrl).setAlertRequest(request);
			}else {
	        	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
				}
				//���ʧ��
			}
			return getForward(request, mapping, "alert");
			
		}
		
		
		
	}


	/**
	 * AJAX���ҿͻ���ϵ����Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward contactInfoQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String contactId = getParameter("contactId",request);
		Result rs = new CRMClientMgt().detailClientDetById(contactId);
		ArrayList<Object> rsList = (ArrayList<Object>)rs.retVal;
		if(rsList!=null && rsList.size()>0){
			String userName = GlobalsTool.get(rsList.get(0),0).toString();
			String mobile = GlobalsTool.get(rsList.get(0),1).toString();
			String email = GlobalsTool.get(rsList.get(0),2).toString();
			String mainUser = GlobalsTool.get(rsList.get(0),3).toString(); 
			String msg = userName+","+mobile+","+email+","+mainUser;
			request.setAttribute("msg",msg);
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}
	/**
	 * ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward PotentialAllocatee(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String EmployeeID=request.getParameter("EmployeeID");
		String[] keyIds =request.getParameterValues("keyId");
		Result rs=mgt.PotentialAllocatee(keyIds, EmployeeID, this.getLoginBean(request).getId());
		String tableName=request.getParameter("tableName");
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add("����ɹ�").setBackUrl("/CRMBrotherAction.do?tableName="+tableName).setRequest(request) ;
		}else{
			EchoMessage.error().add("����ʧ��").setBackUrl("/CRMBrotherAction.do?tableName="+tableName).setRequest(request) ;
		}
		return getForward(request, mapping, "message") ;
	}
	/**
	 * ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward PotentialCallback(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String[] keyIds =request.getParameterValues("keyId");
		Result rs=mgt.PotentialCallback(keyIds,this.getLoginBean(request).getId());
		String tableName=request.getParameter("tableName");
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add("���ճɹ�").setBackUrl("/CRMBrotherAction.do?tableName="+tableName).setRequest(request) ;
		}else{
			EchoMessage.error().add("����ʧ��").setBackUrl("/CRMBrotherAction.do?tableName="+tableName).setRequest(request) ;
		}
		return getForward(request, mapping, "message") ;
	}



	/**
	 * ��ȡ�����ͻ�������optionѡ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward releateEnumer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String clientId = getParameter("clientId",request);//�ͻ�id
		String tableName = getParameter("tableName",request);//����
		String fieldNames = getParameter("fieldNames",request);//�ֶ���s
		
		
		HashMap<String,String> selectMap = new HashMap<String, String>();
		
		for(String fieldName : fieldNames.split(",")){
			DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(tableName, fieldName);
			Result rs = mgt.relateClientSqlQuery(fieldInfoBean.getRefEnumerationName(), fieldInfoBean.getDefaultValue());//����ö��������ֵ��ȡsql���
			List<Object> list = (List<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				//��װoptionѡ��
				String selectInfo ="<option>��ѡ��</option>";	
				String sql = GlobalsTool.get(list.get(0),0).toString();
				rs = mgt.relateInfoQueryByClientId(clientId, sql);
				List<Object> optionlist = (List<Object>)rs.retVal;
				for(Object obj : optionlist){
					selectInfo +="<option value='"+GlobalsTool.get(obj,0)+"'>"+GlobalsTool.get(obj,1)+"</option>";
				}
				selectMap.put(fieldName,selectInfo);	
			}
		}
		request.setAttribute("msg",gson.toJson(selectMap));
		return getForward(request, mapping, "blank");
	}

	private ActionForward goodsQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String classCode = getParameter("classCode",request);
		String keyWord = getParameter("keyWord",request);
		String selectOption = getParameter("selectOption",request);
		
		//��ҳ��Ϣ
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
    		pageSize = 15;
        }
		Result rs = mgt.goodsQueryByClassCode(classCode,pageSize,pageNo,keyWord,selectOption);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("goodsList",rs.retVal);
			request.setAttribute("pageBar",pageBarForCRM(rs, request));
			request.setAttribute("classCode",classCode);
		}else{
		}
		return getForward(request, mapping, "showGoods") ;
	}

	/**
	 * ��Ʒ����ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward goodsTreeQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ArrayList<HashMap<String,String>> goodsList = new ArrayList<HashMap<String,String>>();
		
		//��Ÿ�Ŀ¼,Ĭ��ID��0
		HashMap<String,String> rootMap = new LinkedHashMap<String, String>();
		rootMap.put("id", "0");
		rootMap.put("name","��Ŀ¼");
		rootMap.put("open","true");
		goodsList.add(rootMap);
		
		Result rs = mgt.goodsTreeQuery();
		List<Object> rsList = (List<Object>)rs.retVal;
		for(Object obj : rsList){
			HashMap<String,String> map = new LinkedHashMap<String, String>();
			String classCode = GlobalsTool.get(obj,0).toString();//classCode
			String classCodeLen = GlobalsTool.get(obj,1).toString();//classCode����
			String goodsName = GlobalsTool.get(obj,2).toString();//����
			
			map.put("id", classCode);
			map.put("name",goodsName);
			if("5".equals(classCodeLen)){
				//����Ϊ5��ʾ��һ��Ŀ¼����idֱ��Ϊ0
				map.put("pId","0");
			}else{
				//��IDĬ��ȡ����classCode����-5
				String pId = classCode.substring(0,classCode.length()-5);
				map.put("pId",pId);
			}
			goodsList.add(map);
		}
		
		request.setAttribute("useGoodsCode",getParameter("useGoodsCode",request));
		request.setAttribute("goodsList", gson.toJson(goodsList));
		return getForward(request, mapping, "goodsSelect") ;
	}


	private ActionForward queryNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		HashMap<String,String> conMap = new HashMap<String, String>();//�����������
		StringBuilder condition = new StringBuilder();//�������
		LoginBean loginBean = getLoginBean(request);
		String tableName = getParameter("tableName",request);//�������
		String childTableName="";//��ϸ�����
		
		Result rs = brotherSettingMgt.loadBrotherFieldDisplayBean(tableName);
		BrotherFieldDisplayBean fieldDisplayBean = (BrotherFieldDisplayBean)rs.retVal;
		
		if(fieldDisplayBean == null){
			request.setAttribute("tableName",tableName);
			request.setAttribute("noDisplayBean","true");//��ʾû����ʾ��Ϣ
			return getForward(request, mapping, "index");
		}
		
		//��ȡ��ϸ�����
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//���ݱ���������ϸ
		if(childTableList!=null && childTableList.size()>0){
			childTableName = childTableList.get(0).getTableName();
		}
		
		//��ҳ��Ϣ
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
    		pageSize = 30;
        }
        
        /*��ѯ���� ���� ����չ��*/
		String expandStatus = getParameter("expandStatus", request);
		if(StringUtils.isBlank(expandStatus)){
			expandStatus = "close";
		}
		conMap.put("expandStatus", expandStatus);
		
		//��ȡ�鿴����Ȩ������
		String scope=this.getBrotherScope(tableName, loginBean, request);
		//��������״̬
		String allocateStatus=request.getParameter("AllocateStatus")==null?"noAllocatee":request.getParameter("AllocateStatus");
		if("CRMPotentialClient".equals(tableName)){
			if(scope!=null&&scope.length()>0){
				scope=scope.substring(scope.indexOf("and")+3)+" or ";
				scope=" and ("+scope+" EmployeeID='"+loginBean.getId()+"')";
			}
		}
		//��ȡ�鿴����Ȩ������
		condition.append(scope);
        
		if(fieldDisplayBean.getSearchFields()!=null && !"".equals(fieldDisplayBean.getSearchFields())){
			for(String searchField : fieldDisplayBean.getSearchFields().split(",")){
				if("".equals(searchField)){
					continue;
				}
				DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(tableName, searchField);
				if(fieldInfoBean.getInputType() == 1){
					//ö�ٲ�ѯ
					String[] searchFieldVal = request.getParameterValues(searchField);//
					String searchField_mul = getParameter(searchField+"_mul",request);//�Ƿ��ѡ,true��ʾ��ѡ
					String enumStr = "";//ƴ�����ݿ��ѯ���
					String fieldValues = "";//��¼����ҳ��ѡ�е�ѡ��
					
					//����
					if(searchFieldVal!=null && searchFieldVal.length>0){
						HashMap<String,String> map = new HashMap<String, String>();
						for(String str :searchFieldVal){
							if("all".equals(str)){
								break;
							}
							enumStr += "'"+str+"',";
							fieldValues += str+",";
						}
						if(enumStr.endsWith(",")){
							enumStr = enumStr.substring(0,enumStr.length()-1);
						}
						if(enumStr.length()>0){
							condition.append(" and ").append(tableName).append(".").append(searchField).append(" in (").append(enumStr).append(") ");
							conMap.put(searchField+"_mul", searchField_mul);
							conMap.put(searchField, fieldValues);
						}
					}
				}else{
					//����ʱ���ֶ���Ϣ
					String startTime = getParameter(searchField+"Start",request);
					String endTime = getParameter(searchField+"End",request);
					String selFieldName = tableName +"."+searchField;
					if(startTime != null && !"".equals(startTime)){
						condition.append(" and ").append(selFieldName).append(" >= '").append(startTime).append("'");
						conMap.put(searchField+"Start",startTime);
					}
					
					if(endTime != null && !"".equals(endTime)){
						condition.append(" and ").append(selFieldName).append(" <= '").append(endTime +" 23:59:999").append("'");
						conMap.put(searchField+"End",endTime);
					}
				}
				
			}
		}

		String keyOption = getParameter("keyOption",request);//ģ����ѯѡ��
		String keyword = getParameter("keyword",request);//�ؼ���ֵ
		
		if(keyword!=null && !"".equals(keyword)){
			
			//ģ����ѯѡ��ȫ����
			if("all".equals(keyOption)){
				String str = "";//��¼�����Ϣ
				for(String fieldName : fieldDisplayBean.getKeyFields().split(",")){
					if("".equals(fieldName)){
						continue;
					}
					if("ClientId".equals(fieldName)){
						str += "CRMClientInfo.ClientName like '%"+keyword+"%' or ";
					}else{
						String tempTableName = tableName;
						if(fieldName.indexOf("child") > -1){
							tempTableName = childTableName;
						}
						str += tempTableName+"."+fieldName+" like '%"+keyword+"%' or ";
					}	
				}
				if(str.endsWith("or ")){
					str = str.substring(0,str.length()-3);
				}
				condition.append(" and (").append(str).append(") "); 
			}else{
				if("ClientId".equals(keyOption)){
					condition.append(" and CRMClientInfo.ClientName");//�ͻ�����
				}else{
					String tempTableName = tableName;
					if(keyOption.indexOf("child") > -1){
						tempTableName = childTableName;
					}
					condition.append(" and ").append(tempTableName).append(".").append(keyOption);
				}
				if("@URL:".equals(request.getParameter("url"))){
					try {
						keyword=new String(keyword.getBytes("iso-8859-1"),"utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				condition.append(" like '%").append(keyword).append("%' ");
			}
			conMap.put("keyOption",keyOption);
			conMap.put("keyword",keyword);
		}
        
		//��Ա��ѯ
		String employeeId = getParameter("employeeId",request);
		if(employeeId!=null && !"".equals(employeeId)){
			String ids = "";
			for(String str : employeeId.split(",")){
				ids += "'"+str+"',";
			}
			if(ids.endsWith(",")){
				ids = ids.substring(0,ids.length()-1);
			}
			if(!"".equals(ids)){
				condition.append(" and ").append(tableName);
				//����������Ա
				if("CRMPotentialClient".equals(tableName)){
					condition.append(".createBy in (");
				}else{
					condition.append(".employeeId in (");
					
				}
				condition.append(ids).append(") ");
				conMap.put("employeeId", employeeId);
			}
		}
		
		String sortInfo  = getParameter("sortInfo", request) ;//������Ϣ �����ʶ,������
		conMap.put("sortInfo",sortInfo);
		
		
		
		String clientKeyId=getParameter("clientKeyId",request);//�ͻ�ID
		if(clientKeyId!=null && !"".equals(clientKeyId)){
			condition.append(" and CRMClientInfo.id = '").append(clientKeyId).append("' ");	
			conMap.put("clientKeyId",clientKeyId);
		}
		//��������״̬
        Result result = mgt.query(tableName,childTableName,fieldDisplayBean,condition.toString(),sortInfo, pageNo, pageSize,null,allocateStatus,loginBean.getId());
        
        
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	
			List brotherList = (List) result.retVal ;
			
			if(brotherList!=null && brotherList.size()>0){
				//��������ͻ�������
				List<String> relateClientSqlList = this.getRelateClientSqlList(fieldDisplayBean, tableName);
				Result relateRs = mgt.relateClientAllInfoQuery(relateClientSqlList, brotherList);
				request.setAttribute("relateClientMap",relateRs.retVal);  
			}
			
			
			this.getWorkFlowInfo(tableName, request);//��ȡ�������Ϣ
			
			request.setAttribute("brotherList", brotherList);
			request.setAttribute("AllocateStatus", allocateStatus);
			request.setAttribute("pageBar",pageBar2(result, request)) ;
			request.setAttribute("loginBean",loginBean);
			request.setAttribute("tableName",tableName);
			request.setAttribute("childTableName",childTableName);
			request.setAttribute("conMap",conMap);
			request.setAttribute("fieldDisplayBean",fieldDisplayBean);
		}
        
      //by wyy ��ȡ����flash����
        if("CRMSalesChance".equals(tableName)){  
        	String param = request.getParameter("param");
        	String Flag = request.getParameter("FLAG");
        	Result flrs = mgt.queryNumTol(condition.toString());
        	HttpSession session = request.getSession();
        	session.setAttribute("condition", condition);
        	request.setAttribute("sginFlag", "true");
        	ArrayList arrRs = (ArrayList)flrs.retVal;
        	
        	//�������
        	if(arrRs.size()>0 && arrRs!=null){
        		String[] data = this.fillRsData(arrRs,request);
        		request.setAttribute("dataRight", data[0]);
        		request.setAttribute("dataLeft", data[1]);        		        		
        	} 
        	if("FLAG".equals(Flag)){
        		Result res = mgt.queryDetail(param,tableName,childTableName,fieldDisplayBean,condition.toString(),sortInfo, pageNo, pageSize);       		
        		 if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){       	        	
        				List DetailData = (List) res.retVal ;
        				request.setAttribute("DetailData", DetailData);
        				request.setAttribute("sgin", "true");
        			}        		       		
        	}
        }          
        
        //���ݵ�¼�˻�ȡ�Ƿ��е��뵼������Ȩ��
        String publicScopeStr = brotherSettingMgt.getPublicScope(loginBean, tableName);
        request.setAttribute("publicScopeStr",publicScopeStr);
        
        //��������ģ��ת�ƿͻ�ѡ��ģ��
        if("CRMPotentialClient".equals(tableName)){
        	Result rest = clientSetingMgt.getFilterModules(loginBean);//��ȡ��Ȩ�޵�ģ��
    		ArrayList<Object> moduleList = (ArrayList<Object>)rest.retVal;
    		request.setAttribute("moduleList",moduleList);
        }
        
        //����:�д��ֶξͲ�ѯ�Ƿ��л�ȡ��ϢȨ��
        request.setAttribute("hasGetDatasScope",mgt.checkGetDatasScope(loginBean));
        //������չ��ť��Ϣ
        String butTag;
		try {
			butTag = parseExtendButton(request, tableInfoBean, "list","/CRMBrotherAction.do?tableName=CRMPotentialClient");
			if (butTag != null && butTag.length() > 0) {
	            request.setAttribute("extendButton", butTag);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
            	
        return getForward(request, mapping, "index") ;
	
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
                                     String operation,String linkAdd) throws Exception {

    	//����Ƿ��ǲݸ��б�
    	String draft=(operation.contains("draft")?"draft":"");
    	operation=(operation.contains("draft")?"list":operation);
    	if("copy".equals(request.getAttribute("copy"))||"quote".equals(request.getAttribute("quote"))||
    			"draft".equals(request.getAttribute("saveDraft")) || "saveDraft".equals(request.getAttribute("saveDraft"))){
    		operation = "add";
    	}
    	
    	LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
    	
        StringBuffer butTag = new StringBuffer("");
        StringBuffer quoteBtn = new StringBuffer() ;
        StringBuffer pushBtn = new StringBuffer() ; //�Ƶ������а�Ť
        StringBuffer queryBtn = new StringBuffer() ; //��������а�Ť
        StringBuffer toolBtn = new StringBuffer() ; //���ߵ����а�Ť
        String extendButton = tableInfo.getExtendButton();
        String parentTableName=this.getParameter("parentTableName",request);
        parentTableName=parentTableName==null?"":parentTableName;
        String moduleType = getParameter("moduleType", request) ;
        moduleType=moduleType==null?"":moduleType;
        
        MOperation mop =(MOperation) getLoginBean(request).getOperationMap().get(linkAdd) ;
        int n = 1 ;  //��¼�м�����չ���ð�ť
        if (extendButton != null) {
            while (extendButton.indexOf("<button") > -1) {
                String button = extendButton.substring(extendButton.indexOf(
                        "<button"), extendButton.indexOf(">"));
                extendButton = extendButton.substring(extendButton.indexOf(">") +
                        1);
                String tagOP = getAttribute("operation", button);
                String type = getAttribute("type", button);
                String right= getAttribute("right", button);
                String fieldSysType=getAttribute("fieldSysType", button);//�Զ�����չ��ť��ָ�����ֶε�ϵͳ���ã�����ĳϵͳ����ʱ���˰�ť����ʾ
                
               if(type.equals("define")){
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
                if (operation.equals(tagOP)) {
					if (type.equals("define")&&!draft.equals("draft")) {
                    	 boolean isUsed=("".equals(fieldSysType)?true:Boolean.parseBoolean(BaseEnv.systemSet.get(fieldSysType).getSetting()));
                    	 //��Ӧ��ϵͳ�������ã����ҷ�����Ӧ��Ȩ��
                    	if(isUsed&&((right.equals("add")&&mop.add)||(right.equals("update")&&mop.update)||(right.equals("delete")&&mop.delete)||(right.equals("query")&&mop.query)||(right.equals("approve"))
                    			||(right.equals("print")&&mop.print)||(right.equals("sendEmain")&&mop.sendEmail))){
                            String name = getAttribute("value", button);
                            String check = getAttribute("isCheck", button) ;
                            DefineButtonBean bean= DefineButtonBean.parse(name);
                            if(bean!=null && bean.getType()!=null && bean.getType().equals("execDefine")){
                            	String value = bean.getContent();
                            	//���ȡ��ȡ��Դ�ļ�����ֱ��ȡkey,������֧��ֱ��ʹ������
                            	String dis = this.getMessage(request, bean.getButtonName());
                            	if(dis == null) dis = bean.getButtonName();
                            	butTag.append("<div class=\"btn btn-small\" id=\""+bean.getDefineName()+"\" onclick=\"extendSubmit('"+bean.getDefineName()+"','"+dis+"',"+(!"no".equals(check)?true:false)+")\">"+dis+"</div>");
                            }
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
    protected ActionForward extendDefine(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response){
		//��ȡ��ǰ�޸ĵ�ҳ
        int pageNo = getParameterInt("pageNo", request);
        if(pageNo==0){
        	pageNo = 1 ;
        }
        request.setAttribute("pageNo", pageNo) ;
        
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
		String[] keyIds =request.getParameterValues("keyId");
		
		//����չ��ť����ִ���Զ���define�ļ��е����
					
			String defineName=getParameter("defineName",request);
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			
			rs = dbmgt.defineDelSql(defineName, keyIds, this.getLoginBean(request).getId(),resources,this.getLocale(request),request.getParameter("defineInfo"));
		
			if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
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
						EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/CRMBrotherAction.do?tableName="+tableName
								   +"&parentCode="+parentCode+"&parentTableName="+parentTableName
								   	  +"&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
					}
				}
				return getForward(request, mapping, "message");
			}else{
				int operation=14;
	        	String locale = getLocale(request).toString() ;
	        	
	        	 for(int i=0;keyIds!=null&&i<keyIds.length;i++){
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
	        	
				EchoMessage.success().add(this.getMessage(request, "oa.bbs.operationOk"))
									 .setBackUrl("/CRMBrotherAction.do?tableName="+tableName
											 +"&parentCode="+parentCode+"&parentTableName="+parentTableName
											 	+"&moduleType="+moduleType+"&checkTab=Y&winCurIndex="+request.getParameter("winCurIndex"))
									 .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
    }
	/**
	 * �������
	 * @param obj
	 * @param request
	 * @return
	 */
	protected String[] fillRsData(ArrayList obj,HttpServletRequest request){
		List list = getEnumerationItems("SalesChancePhase", request);
		String[] data = new String[2];
		String[] colorArr ={"677a89","d9ae2e","80a800","9e5f2a","0e6964","9a4c4c","5c3b66","8d5489","8BBA00","588526","D64646","FF8E46"};
		String dataleft="<chart isSliced='1' showNames='1' showValues='0' baseFontSize='13' hoverCapBgColor='8BBA00'>";
		String dataright="<chart isSliced='1' showNames='1' showValues='0' baseFontSize='13' hoverCapBgColor='8BBA00'>";
		int cor=0;				
		for (int i = 0; i < obj.size(); i++) {
			if(cor>=colorArr.length){
				cor=0;
			}
			Object data1 = ((Object[])(obj.get(i)))[0];
			Object data2 = ((Object[])(obj.get(i)))[1];
			Object data3 = ((Object[])(obj.get(i)))[2];
			Object data4 = ((Object[])(obj.get(i)))[3];
			Object data5 = ((Object[])(obj.get(i)))[4];
			double numper = GlobalsTool.round(100*Float.parseFloat(data4.toString()),2);
			double tolper = GlobalsTool.round(100*Float.parseFloat(data5.toString()),2);
			for (int j = 0; j < list.size(); j++) {
				Object line = (Object)list.get(j);
				KeyPair keypair = (KeyPair)line;
				if(data3 !=null){
					if(keypair.getValue().equals(data3.toString())){
						dataleft +="<set name='"+keypair.getName()+","+data1+"'  value='"+data1+"' color='"+colorArr[cor]+"' hoverText='"+keypair.getName()+","+numper+"%'  alpha='85'/>";
						dataright +="<set name='"+keypair.getName()+","+GlobalsTool.round(Float.parseFloat(data2.toString()),2)+"' value='"+GlobalsTool.round(Float.parseFloat(data2.toString()),2)+"' color='"+colorArr[cor]+"' hoverText='"+keypair.getName()+","+tolper+"%' alpha='85' />";
						cor++;
					}
				}
				
			}						
		}
		dataleft +="</chart>";
		dataright +="</chart>";
		data[0]=dataleft;
		data[1]=dataright;
		return data;
	}
	/**
	 * �첽��ȡ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getToltal(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String beginTime = getParameter("beginTime", request);
    	String endTime = getParameter("endTime", request);
    	String changeType = getParameter("changeType", request);
    	String flagValue = getParameter("flagValue", request);      	
    	HttpSession session = request.getSession();
    	Object condition = session.getAttribute("condition");
   		Result totl = mgt.queryToltal(condition.toString(),beginTime,endTime,changeType,flagValue); 
   		ArrayList dat = (ArrayList)totl.retVal;
   		Object da = ((Object[])dat.get(0))[0];
   		double total = GlobalsTool.round(Float.parseFloat(da.toString()),GlobalsTool.getDetDigits());
   		String totData = String.valueOf(total);
   		response.setContentType("text/html;charset=utf-8");  
		PrintWriter out = response.getWriter();
		out.write(totData);
		out.flush();
		out.close();
	}

	/**
	 * �б��ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		HashMap<String,String> conMap = new HashMap<String, String>();//�����������
		StringBuilder condition = new StringBuilder();//�������
		LoginBean loginBean = getLoginBean(request);
		String tableName = getParameter("tableName",request);
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	if(pageSize ==0){
        		pageSize = 30;
        	}
        }
        
        /*��ѯ���� ���� ����չ��*/
		String expandStatus = getParameter("expandStatus", request);
		if(StringUtils.isBlank(expandStatus)){
			expandStatus = "close";
		}
		conMap.put("expandStatus", expandStatus);
		
        //�鿴����Ȩ��
        if(!"1".equals(loginBean.getId())){
        	String scopeDeptCode = "";//���˿ͻ����ϱ��沿��Ȩ���ַ���
			/*�鿴ĳ�ֶ�ֵ����*/
			String fieldValueSQL = "" ;
			
			condition.append(" and (").append(tableName).append(".employeeId  ='").append(loginBean.getId()).append("' ");
			
			//��ȡȨ��·��
			String mopUrl = "/CRMBrotherAction.do?tableName="+tableName;
			MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get(mopUrl);
			if(mop!=null){
				ArrayList scopeRight = new CRMClientAction().getAllMopByType(mop, loginBean, MOperation.M_QUERY);
				if (scopeRight != null && scopeRight.size()>0) {
					for (Object o : scopeRight) {
						String strUserIds = "" ;
						String strDeptIds = "" ;
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strUserIds += "'"+strId+"'," ;
							}
							strUserIds = strUserIds.substring(0, strUserIds.length()-1) ;
							condition.append(" or ").append(tableName).append(".employeeId in (").append(strUserIds).append(")");
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								//�Ѳ������д������
								scopeDeptCode += "classcode like '" + strId + "%' or ";
							}
						}
						
						if(lsb!=null && "6".equals(lsb.getFlag()) && tableName.equals(lsb.getTableName())){
							if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
								if(lsb.getScopeValue().contains(";")){
									String[] scopes = lsb.getScopeValue().split(";") ;
									String scopeSQL = "(" ;
									for(String str : scopes){
										scopeSQL += "'" + str + "'," ; 
									}
									scopeSQL = scopeSQL.substring(0, scopeSQL.length()-1) ;
									scopeSQL += ")" ;
									fieldValueSQL = lsb.getTableName() + "." + lsb.getFieldName() + " in " +scopeSQL ;
								}
							}
						}
					}
					
					//������˿ͻ�����,�������Ĳ���Ȩ��
					if(scopeDeptCode.endsWith("or ")){
						scopeDeptCode = scopeDeptCode.substring(0,scopeDeptCode.length()-3);
					}
					if(!"".equals(scopeDeptCode)){
						condition.append(" or ((select DepartmentCode from tblEmployee where tblEmployee.id=").append(tableName)
						.append(".employeeId) in (select classcode from tblDepartment where ").append(scopeDeptCode).append(" ))");
					}

					
				}
			}
			condition.append(")") ;
			if(fieldValueSQL.length()>0){
				condition.append(" and (").append(fieldValueSQL).append(")") ;
			}
		}
        
        String nextVisitTimeStart = getParameter("nextVisitTimeStart",request);//�´θ���ʱ�俪ʼ
        String nextVisitTimeEnd = getParameter("nextVisitTimeEnd",request);//�´θ���ʱ�����
        String visitTimeStart = getParameter("visitTimeStart",request);//����ʱ�俪ʼ
        String visitTimeEnd = getParameter("visitTimeEnd",request);//����ʱ�����
        
        if(nextVisitTimeStart != null && !"".equals(nextVisitTimeStart)){
			condition.append(" and CRMSaleFollowUp.NextVisitTime >= '").append(nextVisitTimeStart).append("'");
			conMap.put("nextVisitTimeStart",nextVisitTimeStart);
		}
        
		if(nextVisitTimeEnd != null && !"".equals(nextVisitTimeEnd)){
			condition.append(" and CRMSaleFollowUp.NextVisitTime <= '").append(nextVisitTimeEnd +" 23:59:999").append("'");
			conMap.put("nextVisitTimeEnd",nextVisitTimeEnd);
		}
		
		if(visitTimeStart != null && !"".equals(visitTimeStart)){
			condition.append(" and CRMSaleFollowUp.visitTime >= '").append(visitTimeStart).append("'");
			conMap.put("visitTimeStart",visitTimeStart);
		}
        
		if(visitTimeEnd != null && !"".equals(visitTimeEnd)){
			condition.append(" and CRMSaleFollowUp.visitTime <= '").append(visitTimeEnd +" 23:59:999").append("'");
			conMap.put("visitTimeEnd",visitTimeEnd);
		}
        

		String keyOption = getParameter("keyOption",request);//ģ����ѯѡ��
		String keyword = getParameter("keyword",request);//�ؼ���ֵ
		if(keyword!=null && !"".equals(keyword)){
			if("clientName".equals(keyOption)){
				condition.append(" and CRMClientInfo.");
			}else{
				condition.append(" and CRMSaleFollowUp.");
			}
			condition.append(keyOption).append(" like '%").append(keyword).append("%' ");
			conMap.put("keyOption",keyOption);
			conMap.put("keyword",keyword);
		}
        
		//ö�ٲ�ѯ
		String[] FollowPhase = request.getParameterValues("FollowPhase");//�����׶�
		String[] VisitMethod = request.getParameterValues("VisitMethod");//������ʽ
		String FollowPhase_mul = getParameter("FollowPhase_mul",request);//�����׶��Ƿ��ѡ,true��ʾ��ѡ
		String VisitMethod_mul = getParameter("VisitMethod_mul",request);//������ʽ�Ƿ��ѡ,true��ʾ��ѡ
		String enumStr = "";//ƴ�����ݿ��ѯ���
		String fieldValues = "";//��¼����ҳ��ѡ�е�ѡ��
		
		//��������׶�
		if(FollowPhase!=null && FollowPhase.length>0){
			HashMap<String,String> map = new HashMap<String, String>();
			for(String str :FollowPhase){
				if("all".equals(str)){
					break;
				}
				enumStr += "'"+str+"',";
				fieldValues += str+",";
			}
			if(enumStr.endsWith(",")){
				enumStr = enumStr.substring(0,enumStr.length()-1);
			}
			if(enumStr.length()>0){
				condition.append(" and CRMSaleFollowUp.FollowPhase in (").append(enumStr).append(") ");
				conMap.put("FollowPhase_mul", FollowPhase_mul);
				conMap.put("FollowPhase", fieldValues);
			}
		}
		
		//���������ʽ
		if(VisitMethod!=null && VisitMethod.length>0){
			enumStr = "";
			fieldValues = "";
			HashMap<String,String> map = new HashMap<String, String>();
			for(String str :VisitMethod){
				if("all".equals(str)){
					break;
				}
				enumStr += "'"+str+"',";
				fieldValues += str+",";
			}
			if(enumStr.endsWith(",")){
				enumStr = enumStr.substring(0,enumStr.length()-1);
			}
			if(enumStr.length()>0){
				condition.append(" and CRMSaleFollowUp.VisitMethod in (").append(enumStr).append(") ");
				conMap.put("VisitMethod_mul", VisitMethod_mul);
				conMap.put("VisitMethod", fieldValues);
			}
		}
		
		//��Ա��ѯ
		String employeeId = getParameter("employeeId",request);
		if(employeeId!=null && !"".equals(employeeId)){
			String ids = "";
			for(String str : employeeId.split(",")){
				ids += "'"+str+"',";
			}
			if(ids.endsWith(",")){
				ids = ids.substring(0,ids.length()-1);
			}
			if(!"".equals(ids)){
				condition.append(" and CRMSaleFollowUp.employeeId in (").append(ids).append(") ");
				conMap.put("employeeId", employeeId);
			}
		}
		
		String sortInfo  = getParameter("sortInfo", request) ;//������Ϣ �����ʶ,������
		conMap.put("sortInfo",sortInfo);
       // Result result = mgt.query(condition.toString(),sortInfo, pageNo, pageSize);
        
        
        //�г̲�ѯ
        Calendar date = Calendar.getInstance();
        date.setTime(new java.util.Date());
        date.add(Calendar.DATE,+2);
        String endTime = BaseDateFormat.format(date.getTime(),BaseDateFormat.yyyyMMdd);
        Result rs = mgt.querySchedule(loginBean,BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMdd),endTime);
        
        String scheduleScope = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMdd)+",";
        
        date.add(Calendar.DATE,-1);
        scheduleScope += BaseDateFormat.format(date.getTime(),BaseDateFormat.yyyyMMdd) +","+endTime;
        
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	request.setAttribute("scheduleMap", rs.retVal) ;
        	request.setAttribute("scheduleScope",scheduleScope) ;
        	
        }
        
//        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
//			List brotherList = (List) result.retVal ;
//			request.setAttribute("brotherList", brotherList) ;
//			request.setAttribute("pageBar",pageBar2(result, request)) ;
//			request.setAttribute("loginBean",loginBean);
//			request.setAttribute("tableName",tableName);
//			request.setAttribute("conMap",conMap);
//		}
        return getForward(request, mapping, "index") ;
	}
	
	
	/**
	 * ���ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addParpare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tableName = getParameter("tableName",request);
		String brotherId = getParameter("brotherId",request);//�ֵܱ�Id,��ʾ�ͻ��б��ֵܱ�չʾ�����ӽ���
		if(brotherId!=null && !"".equals(brotherId)){
			String clientName = clientMgt.findClientNameById(brotherId);
			request.setAttribute("clientName",clientName);
			request.setAttribute("brotherId",brotherId);
		}
		
		
		request.setAttribute("tableName",tableName);
		request.setAttribute("loginBean",this.getLoginBean(request));
		return getForward(request, mapping, "add");
	}
	
	/**
	 * ���ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addParpareNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tableName = getParameter("tableName",request);
		String brotherId = getParameter("brotherId",request);//�ֵܱ�Id,��ʾ�ͻ��б��ֵܱ�չʾ�����ӽ���
		String keyInfo = getParameter("keyInfo",request);//�������ҳ�������,�����������Ϣ �ֶ���:ID
		LoginBean loginBean = getLoginBean(request);
		
		
		if(keyInfo !=null && !"".equals(keyInfo)){
			String keyName = keyInfo.split(":")[0];//�ֶ���
			String keyVal = keyInfo.split(":")[1];//�ֶ�ֵ
			HashMap<String,List> selectMap = new LinkedHashMap<String, List>();
			if("ClientId".equals(keyName)){
				//��ȡ�����ͻ�ö����Ϣ
				Result rs = brotherSettingMgt.loadBrotherFieldDisplayBean(tableName);
				BrotherFieldDisplayBean fieldDisplayBean= (BrotherFieldDisplayBean)rs.retVal;
				selectMap = this.getRelateClientEnum(fieldDisplayBean, tableName,keyVal);
				//��ȡ�ͻ�����
				String clientName = clientMgt.findClientNameById(keyVal);
				request.setAttribute("clientName",clientName);
				request.setAttribute("clientId",keyVal);
			}else if(keyName.indexOf("CRMPotentialClient")>-1){
				String sql = "SELECT id,ClientName FROM CRMPotentialClient WHERE id= '"+keyVal+"'";
				Result rs = clientMgt.publicSqlQuery(sql, new ArrayList());
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList rsList = (ArrayList)rs.retVal;
					if(rsList!=null && rsList.size()>0){
						request.setAttribute("clientName",GlobalsTool.get(rsList.get(0),1));
						request.setAttribute("clientId",GlobalsTool.get(rsList.get(0),0));
					}
				}
			}else{
				String tempTableName = "CRMSalesChance";//Ĭ�����ۻ���
				if("SaleContractId".equals(keyName)){
					tempTableName = "CRMSaleContract";
				}
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT ").append(tempTableName).append(".id,Topic,").append(tempTableName).append(".ClientId,").append("CRMClientInfo.clientName FROM ");
				sql.append(tempTableName).append(" left join CRMClientInfo on CRMClientInfo.id = ").append(tempTableName).append(".ClientId WHERE ").append(tempTableName);
				sql.append(".id = ?");
				Result rs = mgt.brotherRelateInfoQuery( sql.toString(),keyVal);
				List<Object> rsList = (List<Object>)rs.retVal;
				if(rsList !=null && rsList.size()>0){
					request.setAttribute("clientName",GlobalsTool.get(rsList.get(0),3));
					request.setAttribute("clientId",GlobalsTool.get(rsList.get(0),2));
					selectMap.put(keyName, rsList);
				}
			}
			request.setAttribute("selectMap", selectMap);
		}
		
		
		if(brotherId!=null && !"".equals(brotherId)){
			String clientName = clientMgt.findClientNameById(brotherId);
			request.setAttribute("clientName",clientName);
			request.setAttribute("clientId",brotherId);
		}
		
		DBFieldInfoBean bean = GlobalsTool.getFieldBean(tableName,"billNo");
		
		String childTableName = "";//��ϸ��
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//���ݱ���������ϸ
		
		if(childTableList!=null && childTableList.size()>0){
			childTableName = childTableList.get(0).getTableName();
			request.setAttribute("childTableName",childTableName);
		}
		
		//����������ֶ�
		HashMap<String,String> workFlowFieldMap = clientAction.getWorkFlowFieldsInfo(tableName, null,loginBean);
		
		
		Result rs = brotherSettingMgt.loadBrotherFieldDisplayBean(tableName);
		BrotherFieldDisplayBean fieldDisplayBean= (BrotherFieldDisplayBean)rs.retVal;
		rs = mgt.findFieldScopeByType(loginBean, "hidden");//���������ֶε�list
		List<BrotherFieldScopeBean> hideFieldScopeList = (List<BrotherFieldScopeBean>)rs.retVal;

		rs = mgt.queryHideFields(tableName, true, fieldDisplayBean, hideFieldScopeList,workFlowFieldMap);//������������ֶ�MAP
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			this.getOnlyReadFields(loginBean,request,workFlowFieldMap,tableName);//��ȡֻ����Ϣ
			this.getWorkFlowInfo(tableName, request);//��ȡ�������Ϣ
			LinkedHashMap<String,List<String>> mainMap = (LinkedHashMap<String,List<String>>)rs.retVal;	
			if(!"".equals(childTableName)){
				rs = mgt.queryHideFields(childTableName, false, fieldDisplayBean, hideFieldScopeList,workFlowFieldMap);//������������ֶ�MAP
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					//��ѯ����
				}
				LinkedHashMap<String,List<String>> childMap = (LinkedHashMap<String,List<String>>)rs.retVal;
				request.setAttribute("childMap",childMap);
			}
			
			
			String id = IDGenerater.getId();
			
			
			//���۸����ڵ�һ����������calendarBtn�ֶΡ����������ճ�
			this.setCalendarBtn(tableName, mainMap);
			
			String newId = IDGenerater.getId();
			request.setAttribute("newId",newId);
			
			request.setAttribute("keyId",id);
			request.setAttribute("tableName",tableName);
			request.setAttribute("mainMap",mainMap);
			request.setAttribute("loginBean",this.getLoginBean(request));
			request.setAttribute("tableInfoBean",tableInfoBean);
			request.setAttribute("fieldDisplayBean",fieldDisplayBean);
			
			return getForward(request, mapping, "add");
		}else{
			return getForward(request, mapping, "message");
		}
	}
	
	/**
	 * ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tableName = getParameter("tableName", request);
		String childTableName = getParameter("childTableName", request);
		String keyId = getParameter("keyId", request);
		String designId = getParameter("designId",request);//�����templateId,��ֵ��ʾ�����������
		String approveBefore = getParameter("approveBefore",request);//��������ھӱ����ҳ����ת����־,true��ʾ��
		LoginBean loginBean = getLoginBean(request);
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		 //Ҫִ�е�define����Ϣ
        String defineInfo=request.getParameter("defineInfo");
		HashMap values = new HashMap() ;
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName) ;
		readMainTable(values, tableInfo, request);

		if(childTableName!=null && !"".equals(childTableName)){
			DBTableInfoBean childTableInfo = GlobalsTool.getTableInfoBean(childTableName) ;
			readChildTable(values, childTableInfo, request, "00001");
		}
		
		
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		
		if(keyId==null || "".equals(keyId)){
			keyId = IDGenerater.getId();
		}
		
		values.put("id",keyId);
		values.put("finishTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateBy",loginBean.getId());
		values.put("createBy",loginBean.getId());
		values.put("SCompanyID","00001");
		values.put("createTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		
		if(designId !=null && !"".equals(designId)){
			values.put("workFlowNode","0");
			values.put("workFlowNodeName","notApprove");
			values.put("checkPersons",";"+loginBean.getId()+";");
		}else{
			values.put("workFlowNode","-1");
			values.put("workFlowNodeName","finish");
		}
		
		//values.put("FollowNo",BillNoManager.find("CRMSaleFollowUp_FollowNo",loginBean));
		//values.put("AlertTime","0");
		//values.put("statusId","0");
		//values.put("SalesChanceId","");
//		if(values.get("DepartmentCode") == null){
//			values.put("DepartmentCode", loginBean.getDepartCode());  
//		}
		
		
		String[] uploadBtn = request.getParameterValues("uploadBtn");//�ϴ�������ͼƬ��Ϣ;
		String uploadStrs = "";//����ϴ���Ϣ
		if(uploadBtn!=null && uploadBtn.length>0){
			uploadStrs = this.uploadStr(uploadBtn);
			values.put("Attachment",uploadStrs);
		}
		
 		Result rs = mgt.add(tableName,childTableName,values, loginBean, resources, getLocale(request),allTables,defineInfo,designId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
			if(uploadStrs!=null && !"".equals(uploadStrs)){
				int handleType = FileHandler.TYPE_AFFIX;//����
				DBFieldInfoBean bean = GlobalsTool.getFieldBean(tableName,"Attachment");
				if(bean.getFieldType() == 13){
					handleType = FileHandler.TYPE_PIC;//�ϴ�ͼƬ	
				}
				
				for(String str :uploadStrs.split(";")){
					FileHandler.copy(tableName, handleType, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			
			//���ת��������Ӱ�ť
			if(approveBefore!=null && "true".equals(approveBefore)){
				//ת����ť��ת��index.jsp ��dealCheck(keyId)  ����
				request.setAttribute("noAlert", "true");
				request.setAttribute("checkFlag", "true");
				request.setAttribute("checkBackUrl", keyId);
			}
			
			
			if(isConsole!=null && "true".equals(isConsole)){
				//����ǿͻ�����̨���룬��ת������ҳ��
				String backUrl = "/CRMBrotherAction.do?tableName="+tableName+"&operation=5&isConsole=true&keyId="+keyId;
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl(backUrl).setAlertRequest(request);
			}else{
				request.setAttribute("dealAsyn", "true");
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
			}
			
		}else {
        	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
       	 	//���ʧ��
        }
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * �޸�ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateParpare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		
		String keyId = getParameter("keyId",request);
		String tableName = getParameter("tableName",request);
		LoginBean loginBean = getLoginBean(request);
		String childTableName = "";//��ϸ��
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//���ݱ���������ϸ
		
		if(childTableList!=null && childTableList.size()>0){
			childTableName = childTableList.get(0).getTableName();
			request.setAttribute("childTableName",childTableName);
		}
		
		Result rs = brotherSettingMgt.loadBrotherFieldDisplayBean(tableName);
		BrotherFieldDisplayBean fieldDisplayBean= (BrotherFieldDisplayBean)rs.retVal;
		rs = mgt.findFieldScopeByType(loginBean, "hidden");//���������ֶε�list
		List<BrotherFieldScopeBean> hideFieldScopeList = (List<BrotherFieldScopeBean>)rs.retVal;

		//����������ֶ�
		HashMap<String,String> workFlowFieldMap = clientAction.getWorkFlowFieldsInfo(tableName, keyId,loginBean);
		
		
		rs = mgt.queryHideFields(tableName, true, fieldDisplayBean, hideFieldScopeList,workFlowFieldMap);//������������ֶ�MAP
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			LinkedHashMap<String,List<String>> mainMap = (LinkedHashMap<String,List<String>>)rs.retVal;	
			if(!"".equals(childTableName)){
				rs = mgt.queryHideFields(childTableName, false, fieldDisplayBean, hideFieldScopeList,workFlowFieldMap);//������������ֶ�MAP
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					//��ѯ����
				}
				LinkedHashMap<String,List<String>> childMap = (LinkedHashMap<String,List<String>>)rs.retVal;	
				request.setAttribute("childMap",childMap);
			}
			Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			String sunClassCode = loginBean.getSunCmpClassCode();  //��LoginBean��ȡ����֧������classCode
			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
			MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMBrotherAction.do?tableName="+tableName) ;
			rs= dbmgt.detail(tableName, map, keyId, sunClassCode,props,loginBean.getId(),true,null);
			HashMap rsMap = (HashMap)rs.retVal;
			
			
			//����������۸���,���ͻ�����Ϊ�գ�����clientId�Ƿ���Ǳ�ڿͻ��С���
			this.getPotentialClientName(rsMap);
			
			if(!"CRMPotentialClient".equals(tableName)){
				//��ȡ�����ͻ�ö����Ϣ
				HashMap<String,List> selectMap = this.getRelateClientEnum(fieldDisplayBean, tableName, rsMap.get("ClientId").toString());
				request.setAttribute("selectMap",selectMap);
			}
			this.getWorkFlowInfo(tableName, request);//��ȡ�������Ϣ
			this.setCalendarBtn(tableName, mainMap);//���۸����ڵ�һ����������calendarBtn�ֶΡ����������ճ�
			
			
			//��ȡֻ����Ϣ
			this.getOnlyReadFields(loginBean,request,workFlowFieldMap,tableName);
			
			OACalendarBean calendarBean = mgt.getCalendarByRelationId(keyId);
			request.setAttribute("calendarBean", calendarBean);
			request.setAttribute("result", rsMap);
			request.setAttribute("tableName",tableName);
			request.setAttribute("mainMap",mainMap);
			request.setAttribute("loginBean",this.getLoginBean(request));
			request.setAttribute("fieldDisplayBean",fieldDisplayBean);
			request.setAttribute("keyInfo",getParameter("keyInfo",request));//��ֵ��ʾ������ҳ�����޸Ľ��롣���Կͻ����Ʋ��ܸ�
			request.setAttribute("workFlowNotNullFields", workFlowFieldMap.get("notNull"));
			return getForward(request, mapping, "update");
		}else{
			return getForward(request, mapping, "message");
		}
	}
	
	/**
	 * �޸�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tableName = getParameter("tableName", request);
		String childTableName = getParameter("childTableName", request);
		String keyId = getParameter("keyId",request);
		LoginBean loginBean = getLoginBean(request);
		String approveBefore = getParameter("approveBefore",request);//�������ҳ����ת����־,true��ʾ��
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        String defineInfo=request.getParameter("defineInfo");//Ҫִ�е�define����Ϣ
		HashMap values = new HashMap() ;
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName) ;
		readMainTable(values, tableInfo, request);
		
		if(childTableName!=null && !"".equals(childTableName)){
			DBTableInfoBean childTableInfo = GlobalsTool.getTableInfoBean(childTableName) ;
			readChildTable(values, childTableInfo, request, "00001");
		}
		
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		
		values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateBy",loginBean.getId());
		
		String[] uploadBtn = request.getParameterValues("uploadBtn");//�ϴ���ͼƬ
		String[] uploadDelPic = request.getParameterValues("uploadDelPic");//ɾ���ĵ�ͼƬ
		String uploadStrs = "";//����ϴ���Ϣ
		if(uploadBtn!=null && uploadBtn.length>0){
			uploadStrs = this.uploadStr(uploadBtn);
			values.put("Attachment",uploadStrs);
		}

		Result rs = mgt.update(keyId,tableName,childTableName, values, loginBean, resources, this.getLocale(request),allTables,defineInfo);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if(uploadStrs!=null && !"".equals(uploadStrs)){
				int handleType = FileHandler.TYPE_AFFIX;//����
				DBFieldInfoBean bean = GlobalsTool.getFieldBean(tableName,"Attachment");
				if(bean.getFieldType() == 13){
					handleType = FileHandler.TYPE_PIC;//�ϴ�ͼƬ	
				}
				
				for(String str :uploadStrs.split(";")){
					FileHandler.copy(tableName, handleType, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			
//			if(uploadDelPic!=null && uploadDelPic.length>0){
//				for(String delPic : uploadDelPic){
//					FileHandler.delete(tableName, FileHandler.TYPE_PIC, delPic);
//				}
//			}
			
			//���ת��������Ӱ�ť
			if(approveBefore!=null && "true".equals(approveBefore)){
				//ת����ť��ת��index.jsp ��dealCheck(keyId)  ����
				request.setAttribute("noAlert", "true");
				request.setAttribute("checkFlag", "true");
				request.setAttribute("checkBackUrl", values.get("id"));
				request.setAttribute("dealAsyn", "true");
			}else if(isConsole!=null && "true".equals(isConsole)){
				//����ǿͻ�����̨���룬��ת������ҳ��
				String backUrl = "/CRMBrotherAction.do?tableName="+tableName+"&operation=5&isConsole=true&keyId="+keyId;
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl(backUrl).setAlertRequest(request);
			}else{
				request.setAttribute("dealAsyn", "true");
			}			
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		}else {
        	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
       	 	//���ʧ��
        }
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyId = getParameter("keyId",request);
		String tableName = getParameter("tableName",request);
		LoginBean loginBean = getLoginBean(request);
		
		String childTableName = "";//��ϸ��
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//���ݱ���������ϸ
		ArrayList<DBTableInfoBean> brotherTableList = new DDLOperation().getBrotherTables(tableName, allTables);//��ȡ�ֵܱ���Ϣ
		
		if(childTableList!=null && childTableList.size()>0){
			childTableName = childTableList.get(0).getTableName();
			request.setAttribute("childTableName",childTableName);
		}
		
		Result rs = brotherSettingMgt.loadBrotherFieldDisplayBean(tableName);
		BrotherFieldDisplayBean fieldDisplayBean= (BrotherFieldDisplayBean)rs.retVal;
		rs = mgt.findFieldScopeByType(loginBean, "hidden");//���������ֶε�list
		List<BrotherFieldScopeBean> hideFieldScopeList = (List<BrotherFieldScopeBean>)rs.retVal;

		//����������ֶ�
		HashMap<String,String> workFlowFieldMap = clientAction.getWorkFlowFieldsInfo(tableName, keyId,loginBean);
		
		rs = mgt.queryHideFields(tableName, true, fieldDisplayBean, hideFieldScopeList,workFlowFieldMap);//������������ֶ�MAP
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			LinkedHashMap<String,List<String>> mainMap = (LinkedHashMap<String,List<String>>)rs.retVal;	
			if(!"".equals(childTableName)){
				if(fieldDisplayBean.getPageChildFields() != null && !"".equals(fieldDisplayBean.getPageChildFields())){
					rs = mgt.queryHideFields(childTableName, false, fieldDisplayBean, hideFieldScopeList,workFlowFieldMap);//������������ֶ�MAP
					if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
						//��ѯ����
					}
					LinkedHashMap<String,List<String>> childMap = (LinkedHashMap<String,List<String>>)rs.retVal;	
					request.setAttribute("childMap",childMap);
				}
			}
			String sunClassCode = loginBean.getSunCmpClassCode();  //��LoginBean��ȡ����֧������classCode
			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
			MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMBrotherAction.do?tableName="+tableName) ;
			
			//�������ϯ̨���롣û��Ȩ��Ĭ��new MOperation()
			if(mop==null && "true".equals(isConsole)){
				mop = new MOperation();
			}
			
			rs= dbmgt.detail(tableName, allTables, keyId, sunClassCode,props,loginBean.getId(),true,null);
			HashMap rsMap = (HashMap)rs.retVal;
			
			//����������۸���,���ͻ�����Ϊ�գ�����clientId�Ƿ���Ǳ�ڿͻ��С���
			this.getPotentialClientName(rsMap);
			
			
			if(!"CRMPotentialClient".equals(tableName)){
				//��ȡ�����ͻ�ö����Ϣ
				HashMap<String,List> selectMap = this.getRelateClientEnum(fieldDisplayBean, tableName, rsMap.get("ClientId").toString());
				request.setAttribute("selectMap",selectMap);
			}
			
			
			//���̹���
			Result result = salesFlowMgt.flowQueryByTableName(tableName);
			ArrayList<Object> flowList = (ArrayList<Object>)result.retVal;
			if(flowList!=null && flowList.size()>0){
				request.setAttribute("flowList",flowList);
			}
			
			//����ģ�����
			brotherTableList = new ClientSettingAction().checkModuleUse(brotherTableList, request);//����δ���õ��ھӱ�
			HashMap<String,DBTableInfoBean> moduleMap = new HashMap<String, DBTableInfoBean>();//��������õ�ģ��map
			for(DBTableInfoBean tableBean : brotherTableList){
				moduleMap.put(tableBean.getTableName(), tableBean);
			}
			
			this.getWorkFlowInfo(tableName, request);//��ȡ�������Ϣ
			
			//����˰�ť
			OAWorkFlowTemplate workFlow=BaseEnv.workFlowInfo.get(tableName);
			if(workFlow!=null&&workFlow.getTemplateClass().equals("00001")&&workFlow.getTemplateStatus()==1 && !rsMap.get("workFlowNodeName").toString().equals("draft")){
				if(OnlineUserInfo.getUser(rsMap.get("createBy").toString())!=null){
					boolean flag=dbmgt.isRetCheckPer(loginBean, workFlow, OnlineUserInfo.getUser(rsMap.get("createBy").toString()).getDeptId());
					if(flag){//����з���ˣ���ʾ����˰�ť
						request.setAttribute("retCheckRight", flag);
					}
				}
			}
			
			//�ж��Ƿ��г���Ȩ��
			FlowNodeBean curNode = null;
			if(workFlow!=null&&workFlow.getTemplateStatus()==1){
				WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(BaseEnv.workFlowInfo.get(tableName).getId());
				if (designBean != null) {
					HashMap<String, FlowNodeBean> nodeMap = designBean.getFlowNodeMap();
					curNode = nodeMap.get(rsMap.get("workFlowNode"));
					Result cancelRs = mgt.checkCancelFlow(tableName, rsMap.get("id").toString(), loginBean);
					ArrayList<Object> list = (ArrayList<Object>)cancelRs.retVal;
					Object obj = new Object();
					if(list!=null && list.size()>0){
						obj = GlobalsTool.get(list.get(0),1);
					}
					if(curNode!=null && curNode.isAllowCancel() && obj!=null){
						String lastNodeId = GlobalsTool.get(list.get(0),1)+""; 
						request.setAttribute("lastNodeId", lastNodeId);
						request.setAttribute("allowCancel", "true");
					}
				}
	        }
			
			//��������ģ��ת�ƿͻ�ѡ��ģ��
	        if("CRMPotentialClient".equals(tableName)){
	        	Result rest = clientSetingMgt.getFilterModules(loginBean);//��ȡ��Ȩ�޵�ģ��
	    		ArrayList<Object> moduleList = (ArrayList<Object>)rest.retVal;
	    		request.setAttribute("moduleList",moduleList);
	        }
			
			
	        this.setCalendarBtn(tableName, mainMap);//���۸����ڵ�һ����������calendarBtn�ֶΡ����������ճ�
	        
	        //����ID��ȡ�Ƿ��й����ճ̣�������ϵ��¼
	        OACalendarBean calendarBean = mgt.getCalendarByRelationId(keyId);
			request.setAttribute("calendarBean", calendarBean);
			
			
			request.setAttribute("result", rsMap);
			request.setAttribute("tableName",tableName);
			request.setAttribute("mainMap",mainMap);
			request.setAttribute("loginBean",this.getLoginBean(request));
			request.setAttribute("fieldDisplayBean",fieldDisplayBean);
			request.setAttribute("brotherTableList",brotherTableList);
			
			request.setAttribute("moduleMap",moduleMap);
			request.setAttribute("alertEnter",getParameter("alertEnter",request));
			return getForward(request, mapping, "detail");
		}else{
			return getForward(request, mapping, "message");
		}
		
		/*
		
		//��ѯ������ظ�
		Result rssult = mgt.queryCommentById(keyId);//��������
		if(rssult.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return getForward(request, mapping, "message");
		}
		
		ArrayList<Object> commentList = (ArrayList<Object>)rssult.getRetVal();
		if(commentList!=null && commentList.size()>0){
			String commentIds = "";
			
			for(Object obj : commentList){
				commentIds += "'"+GlobalsTool.get(obj,0) +"',";
			}
			if(commentIds.endsWith(",")){
				commentIds = commentIds.substring(0,commentIds.length()-1);
			}
			
			rssult = mgt.queryReply(commentIds);//���һظ�
			if(rssult.retCode == ErrorCanst.DEFAULT_SUCCESS){
				HashMap<String,List<CRMBrotherCommentBean>> replyMap = (HashMap<String,List<CRMBrotherCommentBean>>)rssult.retVal;
				request.setAttribute("commentList",commentList);
				request.setAttribute("replyMap",replyMap);
			}else{
				return getForward(request, mapping, "message");					
			}
		}
		request.setAttribute("tableName",tableName);
		request.setAttribute("f_ref",keyId);
		request.setAttribute("result", rs.retVal);
		request.setAttribute("tableName",tableName);
		request.setAttribute("isMessageEnter",getParameter("isMessageEnter",request));//true ��ʾ��֪ͨ��Ϣ���ӽ��롣
		return getForward(request, mapping, "detail");
		*/
	}

	/**
	 * ɾ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = getParameters("keyId", request) ;
		String tableName = getParameter("tableName",request);
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		LoginBean loginBean = getLoginBean(request);
		Result result = mgt.delete(keyIds,tableName,allTables, getLoginBean(request).getId(), resources, getLocale(request),loginBean);
		if(result.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			if(result.retCode==ErrorCanst.RET_DEFINE_SQL_ERROR){
				String[] msgCode = (String[])result.getRetVal() ;
				EchoMessage.error().add(getMessage(request, msgCode[0]))
									.setBackUrl("/CRMBrotherAction.do?tableName="+tableName)
                    					.setRequest(request) ;
			}else{
                EchoMessage.error().add(getMessage(request, "common.msg.clientDataDeleteFailure"))
                    			.setBackUrl("/CRMBrotherAction.do?tableName="+tableName)
                    			.setRequest(request);
			}   
			return getForward(request, mapping, "message") ;
		}
		return queryNew(mapping, form, request, response);
	}
	
	/**
	 * ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward exportBill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tableName = getParameter("tableName",request); //����
		String isTemplate = getParameter("isTemplate",request); //�Ƿ�ģ��
		File file = new File("../../AIOBillExport");
		if(!file.exists()){
			file.mkdirs() ;
		}
		
		String tableDisplay=GlobalsTool.getTableInfoBean(tableName).getDisplay().get(getLocale(request).toString());
		String fileName = file.getAbsolutePath()+"\\"+tableDisplay+".xls" ;
		FileOutputStream fos = new FileOutputStream(fileName);
		ArrayList<ExportField> exportList = new ArrayList<ExportField>();
		ArrayList<HashMap> exportValueList = new ArrayList<HashMap>();
		Object localeObj = request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (localeObj == null) {
			localeObj = request.getSession().getServletContext().getAttribute("DefaultLocale");
		}
		final String locale = localeObj == null ? "" : localeObj.toString();
		
		LoginBean loginBean = this.getLoginBean(request);
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = loginBean.getSunCmpClassCode();  //��LoginBean��ȡ����֧������classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;
		
		DBTableInfoBean tableBean = GlobalsTool.getTableInfoBean(tableName);
		BrotherFieldDisplayBean displayBean = (BrotherFieldDisplayBean)brotherSettingMgt.loadBrotherFieldDisplayBean(tableName).retVal;
		String[] pageFieldsArr = displayBean.getListFields().split(",");
		
		for(String fieldName :pageFieldsArr){
			if(fieldName != null){
				DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(tableName, fieldName);
				if(fieldBean.getFieldType()!=13 || fieldBean.getFieldType()!=14){
					exportList.add(new ExportField("main", fieldBean.getTableBean().getTableName(),fieldBean.getFieldName(),fieldBean.getInputType(), fieldBean.getDisplay().get(locale),tableBean.getDisplay().get(locale),false));
				}
			}
			
		}
		
		if(!"true".equals(isTemplate)){
			Result rs =new Result();
			String[] keyIds = getParameters("keyId", request) ;/*�ͻ�Id*/
			for(String keyId : keyIds){
				rs = dbmgt.detail(tableName, map, keyId, sunClassCode,props,loginBean.getId(),true,null);
				HashMap rsMap = (HashMap)rs.retVal;//��ȡֵ	
				this.setExportValue(rsMap, tableName, displayBean, exportValueList, locale,request,loginBean);
			}
		}
		
		

		Result result = ExportData.billExport(fos, tableBean.getDisplay().get(locale), exportList, exportValueList,tableBean.getDisplay().get(locale));
		fos.close();
		
		/* �����ɹ� */
		if("true".equals(isTemplate)){
			//ģ��
			if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg","/ReadFile?tempFile=export&fileName="+GlobalsTool.encode(tableDisplay+".xls"));
			}else{
				request.setAttribute("msg","error");
			}
			return getForward(request, mapping, "blank");
			
		}else{
			if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
				String backURL = "/CRMBrotherAction.do?tableName="+tableName;
				EchoMessage.success().add(
						getMessage(request, "com.export.success") + "<a href='/ReadFile?tempFile=export&fileName=" + GlobalsTool.encode(tableDisplay + ".xls")
						+ "'>" + getMessage(request, "com.export.success.download") + "</a>" + getMessage(request, "com.export.success.over") + fileName)
						.setBackUrl(backURL).setNotAutoBack().setAlertRequest(request);
			}else{
				EchoMessage.error().add(getMessage(request, "com.export.failure"))
				.setNotAutoBack().setAlertRequest(request);
			}
			return getForward(request, mapping, "message");
		}
	}
	
	/**
	 * ���۲���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward commentQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String keyId = getParameter("keyId", request);//����Id
		String tableName = getParameter("tableName", request);//����
		String employeeId = getParameter("employeeId", request);//������
		Result rs = mgt.queryCommentById(keyId);//��������
		
		
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			//��ѯʧ��
			EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		
		ArrayList<Object> commentList = (ArrayList<Object>)rs.getRetVal();
		if(commentList!=null && commentList.size()>0){
			String commentIds = "";
			
			for(Object obj : commentList){
				commentIds += "'"+GlobalsTool.get(obj,0) +"',";
			}
			if(commentIds.endsWith(",")){
				commentIds = commentIds.substring(0,commentIds.length()-1);
			}
			
			rs = mgt.queryReply(commentIds);//���һظ�
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				HashMap<String,List<CRMBrotherCommentBean>> replyMap = (HashMap<String,List<CRMBrotherCommentBean>>)rs.retVal;
				request.setAttribute("commentList",commentList);
				request.setAttribute("replyMap",replyMap);
			}else{
				//��ѯʧ��
				EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
				return getForward(request, mapping, "alert");					
			}
		}
		request.setAttribute("tableName",tableName);
		request.setAttribute("f_ref",keyId);
		request.setAttribute("employeeId",employeeId);
		return getForward(request, mapping, "comment") ;
	}
	
	/**
	 * �������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addComment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String contents = getParameter("contents",request);//����
		String f_ref = getParameter("f_ref",request);//���,����ID
		String tableName = getParameter("tableName",request);//����
		String commentId = getParameter("commentId",request);//����ID
		String commentBy = getParameter("commentBy",request);//�ظ���
		String billCreateBy = getParameter("billCreateBy",request);//���ݴ�����
		
		String clientName = "";
		try {
			clientName = URLDecoder.decode(getParameter("clientName",request),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		LoginBean loginBean = getLoginBean(request);
		
		CRMBrotherCommentBean commentBean = new CRMBrotherCommentBean();
		commentBean.setId(IDGenerater.getId());
		commentBean.setContents(contents);
		commentBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		commentBean.setEmployeeId(loginBean.getId());
		commentBean.setF_ref(f_ref);
		
		String str = "����";
		//true��ʾ�ظ�
		if(commentId!=null && !"".equals(commentId)){
			commentBean.setCommentId(commentId);
			commentBean.setCommentBy(commentBy);
			str ="�ظ�";
		}
		
		//���
		Result rs = mgt.addComment(commentBean,billCreateBy,tableName,loginBean,clientName);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){			

			String billId =commentBean.getF_ref();//����Id; 
			String employeeId = commentBean.getEmployeeId();
			
			String commentStr = "����";
			if(commentBean.getCommentId() !=null && !"".equals(commentBean.getCommentId())){
				commentStr = "�ظ�";
			}
			
			String title = GlobalsTool.getEmpFullNameByUserId(employeeId) + commentStr +"��������۸���,��鿴";//����
			String url ="/CRMBrotherAction.do?tableName="+tableName+"&isMessageEnter=true&operation=5&keyId="+billId;
			String message = "<a href=javascript:mdiwin(''" + url + "'',''"+clientName+"'')>"	+title;
			message+="</a>";//����
			
			
			String commentSuccess = "false";//�ж��Ƿ��в�������
			String id = IDGenerater.getId();
			if(!billCreateBy.equals(loginBean.getId())){
				//��������֪ͨ��Ϣ
				new AdviceMgt().add(employeeId, title, message, billCreateBy, billId, "");
				commentSuccess = "true";
			}
			
			//����ظ�֪ͨ��Ϣ
			if(commentBean.getCommentId() !=null && !"".equals(commentBean.getCommentId()) && !billCreateBy.equals(commentBean.getCommentBy()) && !commentBean.getEmployeeId().equals(commentBean.getCommentBy())){
				String replyTitle = GlobalsTool.getEmpFullNameByUserId(employeeId) +"�ظ����������";
				new AdviceMgt().add(employeeId, replyTitle, message, commentBean.getCommentBy(), billId, "");
			}
			
			String commentDiv = "";//ƴ������Div
			
			//��ȡͷ��·��
			String photo = loginBean.getPhoto();
			String pic = ""; 
			if(photo!=null && !"".equals(photo)){
				pic = "/ReadFile.jpg?fileName="+photo.split(";")[0]+"&realName="+photo.split(";")[0]+"&tempFile=false&type=PIC&tableName=tblEmployee";
			}else{
				//�յĻ�,ȡĬ��ͷ��
				pic = "/style/images/bbs/us_photos.gif";
			}
			
			if(commentId!=null && !"".equals(commentId)){
				//ƴ�ӻظ�DIV
				commentDiv="<div class='tk_in s_tk'><div class='in_img'><a class='in_img_a'><img src='"+pic+"' /></a></div><div class='in_wd'><div class='in_wd_t'>";
				if(commentBean.getEmployeeId().equals(commentBy)){
					commentDiv +="<a class='a_name'>"+GlobalsTool.getEmpFullNameByUserId(commentBean.getEmployeeId())+"</a><em>�ظ�</em>";
				}else{
					commentDiv +="<a class='a_name'>"+GlobalsTool.getEmpFullNameByUserId(commentBean.getEmployeeId())+"</a><em>�ظ�</em><a class='a_name'>"+GlobalsTool.getEmpFullNameByUserId(commentBy)+"</a>";
				}
				commentDiv +="<em>:</em><div class=''txt_d'>"+contents+"</div>"
				+"</div><div class='in_wd_b'><em class='em_time'>"+commentBean.getCreateTime()+"</em><a href='#' class='a_reply' onclick=\"addReply('"+commentBean.getCommentId()+"','"+commentBean.getCommentBy()+"',this,'ss')\">�ظ�</a></div></div></div>";
			}else{
				//ƴ������DIV
				commentDiv = "<div class='tk_iwp'><div class='tk_in'><div class='in_img'><a class='in_img_a'><img src='"+pic+"'/></a></div><div class='in_wd'><div class='in_wd_t'><a class='a_name'></a>"+GlobalsTool.getEmpFullNameByUserId(commentBean.getEmployeeId())+"<em>:</em>";
				commentDiv +="<div class='txt_d'>"+contents+"</div></div><div class='in_wd_b'><em class='em_time'>"+commentBean.getCreateTime()+"</em><a href='#' class='a_reply' onclick=\"addReply('"+commentBean.getId()+"','"+commentBean.getEmployeeId()+"',this,'ss')\">�ظ�</a></div></div></div></div>";
			}
			request.setAttribute("msg", commentDiv);
		}else{
			request.setAttribute("msg", "error");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * �첽ɾ��ͼƬ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward ajaxDelPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = null;
        String fileName = request.getParameter("fileName");
        String tempFile = request.getParameter("tempFile"); //�Ƿ�����ʱ�ļ�
        String tableName = request.getParameter("tableName"); //ɾ����ʽ�ļ�ʱҪ�ṩ����
        String type = request.getParameter("type"); //ɾ����ʽ�ļ�ʱҪ�ṩ����
        fileName = GlobalsTool.toChinseChar(fileName);
        
        if (fileName != null && fileName.length() != 0) {
            boolean rs = false;
            if("true".equals(tempFile)){
                //ɾ����ʱ�ļ�
                rs = FileHandler.deleteTemp(fileName);
            }else{
                rs = true;
                //���ﲻ����ʵɾ����ʽ�ļ����������ύʱ�ж��ļ���ɾ��������ʽɾ��
                //���������ʵɾ���ˣ����û�δ�ύ����£��������ݲ�һ��
//                rs = FileHandler.delete(tableName,
//                                        "PIC".equals(type) ? FileHandler.TYPE_PIC :
//                                        FileHandler.TYPE_AFFIX, fileName);
            }
            if (rs) {
                //ɾ���ɹ�
                //��ʱ�ļ�Ҫɾ���ڴ�����
                if("true".equals(tempFile)){
                    ArrayList list;
                    if ("PIC".equals(type)) {
                        Object o = request.getSession().getAttribute(
                                "PIC_UPLOAD");
                        if (o == null) {
                            list = new ArrayList();
                            request.getSession().setAttribute("PIC_UPLOAD",
                                    list);
                        } else {
                            list = (ArrayList) o;
                        }
                    } else {
                        Object o = request.getSession().getAttribute(
                                "AFFIX_UPLOAD");
                        if (o == null) {
                            list = new ArrayList();
                            request.getSession().setAttribute("AFFIX_UPLOAD",
                                    list);
                        } else {
                            list = (ArrayList) o;
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if(fileName.equals(list.get(i).toString())){
                            list.remove(i);
                            break;
                        }
                    }
                }
                request.setAttribute("msg","ok");	
//                request.setAttribute("fileName",fileName);
//                return getForward(request, mapping, "deleteSuccess");

            } else {
            	request.setAttribute("msg","no");	
                //ɾ��ʧ��
//                EchoMessage.error().add(getMessage(request,
//                        "common.msg.delError")).
//                        setRequest(request);
//                forward = getForward(request, mapping, "message");
            }
            return getForward(request, mapping, "blank");
        }
        return forward;
	}
	
	/**
	 * ��ȡ�ϴ�ͼƬ���ַ��� 
	 * @param picbutton
	 * @return
	 */
	public String uploadStr(String[] picbutton){
		String allPic ="";
		if(picbutton!=null && picbutton.length>0){
			for(String pic:picbutton){
				allPic += pic +";";
			}
			if(allPic.endsWith(";")){
				allPic = allPic.substring(0,allPic.length()-1);
			}
		}
		return allPic;
	}
	
	/**
	 * �첽�����ھӱ�list
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward brotehrListQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		StringBuilder condition = new StringBuilder();//�������
		LoginBean loginBean = getLoginBean(request);
		String tableName = getParameter("tableName",request);//�������
		String keyInfo=getParameter("keyInfo",request);//
		String childTableName="";//��ϸ�����
	
		String fieldList = "";//����ֶ��б��ֶ���Ϣ
		this.getWorkFlowInfo(tableName, request);//��ȡ�������Ϣ
		
		//��ҳ��Ϣ
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
		if (pageNo == 0 ) {
			pageNo = 1;
		}
		if (pageSize == 0) {
			pageSize = 5;
		}
		
		//��·��
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/CRMBrotherAction.do?tableName="+tableName);
		MOperation erpMop = (MOperation) this.getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName="+tableName);
		if(mop == null ){
			//û����·��ֱ�ӽ���ɴ���ʽ
			DefineReportBean reportBean = DefineReportBean.parse(tableName+"SQL.xml", getLocale(request).toString(),getLoginBean(request).getId());//���ݱ����õ�����������Ϣ

			
			
			//��ȡ����sql�����Ϣ
			HashMap map = new HashMap();
			map.put("reportNumber",tableName);
			map.put("sqlFileName",tableName+"SQL.xml");
			request.getSession().getServletContext().setAttribute("parameterMap",new HashMap());
			Result rs = new AlertCenterMgt().queryReportSql(request.getSession().getServletContext(), map);
			HashMap<String,String> rsMap = (HashMap<String,String>)rs.retVal;
			String tempSql = rsMap.get("pageSql");//��Ž������sql���
			
			if(reportBean !=null){
				String orderSql = "";//��������
				String sql = "";
				List<ReportField> fieldlist = reportBean.getDisFields();
				for(ReportField field : fieldlist){
					if("".equals(orderSql)){
						//���ݵ�һ���ֶ���˳��
						orderSql = tableName+"."+field.getAsFieldName() + " desc";
					}
				}
				
				//��ȡ�����ֶ�
				for(Object obj : reportBean.getConFields()){
					ReportField reportField = (ReportField)obj;
					if(reportField.getOrderByFlag() !=null && !"".equals(reportField.getOrderByFlag())){
						orderSql = tableName+"."+reportField.getAsFieldName();
						if("��".equals(reportField.getOrderByFlag())){
							orderSql+=" DESC";
						}else{
							orderSql+=" ASC";
						}
						break;
					}
				}
				
				sql ="SELECT row_number() over(order by "+orderSql+") row_id," + tempSql.substring(6,tempSql.indexOf("where 1=1")) + " where 1=1 ";
				if(erpMop==null){
					sql+=" and f_brother='"+keyInfo.split(":")[1]+"'";
				}else{
					sql+=" and "+tableName+".CompanyCode = (select classCode from tblCompany where id='"+keyInfo.split(":")[1]+"')";
				}
				
				Result result = mgt.reportSqlQuery(sql, pageNo, pageSize);
				request.setAttribute("brotherList", result.retVal);
				request.setAttribute("pageBar",pageBarForCRM(result, request));
				request.setAttribute("fieldlist",fieldlist);
			}
			request.setAttribute("tableName",tableName);
			request.setAttribute("childTableName",childTableName);
			request.setAttribute("oldPath","true");
			
		}else{
			Result rs = brotherSettingMgt.loadBrotherFieldDisplayBean(tableName);
			BrotherFieldDisplayBean fieldDisplayBean = (BrotherFieldDisplayBean)rs.retVal;
			//��ȡ��ϸ�����
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
			ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//���ݱ���������ϸ
			if(childTableList!=null && childTableList.size()>0){
				childTableName = childTableList.get(0).getTableName();
			}
			
			//��ȡ�鿴����Ȩ������
			condition.append(this.getBrotherScope(tableName, loginBean, request));
			
			//�����Ϣ,�ж��ǿͻ�����ͬ�����ۻ��ᡢǱ�ڿͻ�
			if(keyInfo!=null){
				condition.append(" and ").append(keyInfo.split(":")[0]).append("='").append(keyInfo.split(":")[1]).append("' ");
			}
			
			Result result = mgt.query(tableName,childTableName,fieldDisplayBean,condition.toString(),null, pageNo, pageSize,keyInfo,"all",loginBean.getId());
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				List brotherList = (List) result.retVal ;
				if(brotherList!=null && brotherList.size()>0){
					List<String> relateClientSqlList = this.getRelateClientSqlList(fieldDisplayBean, tableName);
					Result relateRs = mgt.relateClientAllInfoQuery(relateClientSqlList, brotherList);
					request.setAttribute("relateClientMap",relateRs.retVal);  
				}
				request.setAttribute("brotherList", brotherList);
				request.setAttribute("pageBar",pageBarForCRM(result, request));
				request.setAttribute("tableName",tableName);
				request.setAttribute("childTableName",childTableName);
				request.setAttribute("fieldDisplayBean",fieldDisplayBean);
			}
		}
		request.setAttribute("loginBean",getLoginBean(request));
		return getForward(request, mapping, "brotherList");
	}
	
	/**
	 * ��ù����ͻ���������Ϣ
	 * @param fieldDisplayBean �ֶ���ʾbean
	 * @param tableName ����
	 * @param clientId �ͻ�ID
	 * @return
	 */
	public HashMap<String,List> getRelateClientEnum(BrotherFieldDisplayBean fieldDisplayBean,String tableName,String clientId){
		HashMap<String,List> selectMap = new LinkedHashMap<String, List>();	
		for(String str:fieldDisplayBean.getPageFields().split(",")){
			DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(tableName, str);
			if(fieldInfoBean !=null && fieldInfoBean.getInputType() == 20){
				Result result = mgt.relateClientSqlQuery(fieldInfoBean.getRefEnumerationName(), fieldInfoBean.getDefaultValue());//����ö��������ֵ��ȡsql���
				List<Object> list = (List<Object>)result.retVal;
				if(list!=null && list.size()>0){
					String sql = GlobalsTool.get(list.get(0),0).toString();
					result = mgt.relateInfoQueryByClientId(clientId, sql);
					List<Object> optionList = (List<Object>)result.retVal;
					if(optionList!=null && optionList.size()>0){
						selectMap.put(str,optionList);	
					}
				}
			}
		}
		return selectMap;
	}
	
	/**
	 * ��ȡ�����ͻ����������е�sql���
	 * @param fieldDisplayBean
	 * @param tableName
	 * @return
	 */
	public List<String> getRelateClientSqlList(BrotherFieldDisplayBean fieldDisplayBean,String tableName){
		List<String> relateClientSqlList = new ArrayList<String>();
		//��ȡ�����ͻ�������sql
		for(String fieldName : fieldDisplayBean.getListFields().split(",")){
			if(fieldName!=null && !"".equals(fieldName)){
				DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(tableName, fieldName);
				if(fieldInfoBean.getInputType() == 20){
					Result rst = mgt.relateClientSqlQuery(fieldInfoBean.getRefEnumerationName(), fieldInfoBean.getDefaultValue());//����ö��������ֵ��ȡsql���
					List<Object> list = (List<Object>)rst.retVal;
					if(list!=null && list.size()>0){
						String sql = GlobalsTool.get(list.get(0),0).toString();
						relateClientSqlList.add(sql);
					}
				}
			}
		}
		return relateClientSqlList;
	}
	
	/**
	 * ��дȨ�޷���
	 */
	 protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		String type = req.getParameter("type");
		String isConsole = getParameter("isConsole", req);//��ʾ�ͷ�����̨���룬Ȩ�޲��ܿ���
		//��Ʒ��������ҪȨ��
		if("goodsTree".equals(type) || "goodsQuery".equals(type) || "ajaxBrotherList".equals(type) || "true".equals(isConsole)){
			return null;
		}
		return super.doAuth(req, mapping);
	}
	 
	
	
	/**
	 * ��ȡ�������Ϣ
	 * @param tableName
	 * @param request
	 */
	public void getWorkFlowInfo(String tableName,HttpServletRequest request){
		//��ȡ�������Ϣ
		if(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1){
        	request.setAttribute("OAWorkFlow", true);
        	request.setAttribute("currNodeId", 0);
         	request.setAttribute("designId", BaseEnv.workFlowInfo.get(tableName).getId());
        }
	}
	
	public String getBrotherScope(String tableName,LoginBean loginBean,HttpServletRequest request){
		StringBuilder condition = new StringBuilder();//�������
		
		//�����Ƿ������������
		boolean flowOpen = false;
		String flowCondition = "";//�������
		if(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1){
			flowOpen = true;
			//���ڲ鵥����Ȩ��,�������workFlow=-1 ��=0
			//flowCondition = " and ("+tableName +".workFlowNode='-1' or "+tableName+".workFlowNode='0')";
			//flowCondition = " and ("+tableName+".workFlowNode!='0')";
			flowCondition = " and ( 1=1 )";
		}
		
		//��ȡȨ��·��
		String mopUrl = "/CRMBrotherAction.do?tableName="+tableName;
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get(mopUrl);
		ArrayList scopeRight = new CRMClientAction().getAllMopByType(mop, loginBean, MOperation.M_QUERY);
		String allDeptScopeStr = new CRMClientAction().checkExistAllDeptScope(scopeRight);
		//�鿴����Ȩ��
        if(!"1".equals(loginBean.getId()) && !"ALL".equals(allDeptScopeStr)){
        	String scopeDeptCode = "";//���˿ͻ����ϱ��沿��Ȩ���ַ���
			/*�鿴ĳ�ֶ�ֵ����*/
			String fieldValueSQL = "" ;
			
			condition.append(" and ");
			if(flowOpen){
				condition.append(" ( ");
			}
			
			condition.append("  (").append(tableName).append(".createBy = '").append(loginBean.getId()).append("'");
			
			if(!"CRMPotentialClient".equals(tableName)){
				condition.append(" or ").append(tableName).append(".employeeId  ='").append(loginBean.getId()).append("' ");
				
			}
			
			
			if(mop!=null){
				
				if (scopeRight != null && scopeRight.size()>0) {
					for (Object o : scopeRight) {
						String strUserIds = "" ;
						String strDeptIds = "" ;
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strUserIds += "'"+strId+"'," ;
							}
							if(!"".equals(strUserIds)){
								strUserIds = strUserIds.substring(0, strUserIds.length()-1) ;
								condition.append(" or ");
								
								//���������������
								if(flowOpen){
									condition.append(" (");
								}
								
								condition.append(tableName);
								
								if("CRMPotentialClient".equals(tableName)){
									condition.append(".createBy ");
								}else{
									condition.append(".employeeId ");
								}
								
								condition.append(" in (").append(strUserIds).append(")");
								
								//���������������
								if(flowOpen){
									condition.append(flowCondition).append(" ) ");
								}
							}
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								//�Ѳ������д������
								scopeDeptCode += "classcode like '" + strId + "%' or ";
							}
						}
						
						if(lsb!=null && "6".equals(lsb.getFlag()) && tableName.equals(lsb.getTableName())){
							if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
								if(lsb.getScopeValue().contains(";")){
									String[] scopes = lsb.getScopeValue().split(";") ;
									String scopeSQL = "(" ;
									for(String str : scopes){
										scopeSQL += "'" + str + "'," ; 
									}
									scopeSQL = scopeSQL.substring(0, scopeSQL.length()-1) ;
									scopeSQL += ")" ;
									fieldValueSQL = lsb.getTableName() + "." + lsb.getFieldName() + " in " +scopeSQL ;
								}
							}
						}
					}
					
					//������˿ͻ�����,�������Ĳ���Ȩ��
					if(!"".equals(scopeDeptCode)){
						if(scopeDeptCode.endsWith("or ")){
							scopeDeptCode = scopeDeptCode.substring(0,scopeDeptCode.length()-3);
						}
						
						if(!"".equals(scopeDeptCode)){
							condition.append(" or ");
						}
						
						if(flowOpen){
							condition.append("(");
						}
						
						if(!"".equals(scopeDeptCode)){
							condition.append(" ((select DepartmentCode from tblEmployee where tblEmployee.id=").append(tableName);
							//07-23����Ҳ�������ߣ�����Ҫ�����ô��������ò���Ȩ��
//							if("CRMPotentialClient".equals(tableName)){
//								condition.append(".createBy ");
//							}else{
								condition.append(".employeeId ");
//							}
							condition.append(") in (select classcode from tblDepartment where ").append(scopeDeptCode).append(" )) ");
						}
						
						//���������������
						if(flowOpen){
							condition.append(flowCondition).append(" )");
						}
					}
				}
			}
			condition.append(") ") ;
			
			//���������������,��������Լ�
			if(flowOpen){
				condition.append(" or ").append(tableName).append(".checkPersons like '%;").append(loginBean.getId()).append(";%' )");
			}
			
			if(fieldValueSQL.length()>0){
				condition.append(" and (").append(fieldValueSQL).append(")") ;
			}
			
			//��ȡ�鿴�����޸�Ȩ�޵�����ְԱIDS
			new CRMClientAction().getAllowUpdateUserIds(mop, request);
		}
        return condition.toString();
	}
	
	/**
	 * ������۸�����ȡǱ�ڿͻ��ͻ�����
	 * @param rsMap
	 */
	public void getPotentialClientName(HashMap rsMap){
		String clientName = String.valueOf(rsMap.get("CRMClientInfo.ClientName"));
		if(clientName==null || "".equals(clientName) || "null".equals(clientName)){
			String sql = "SELECT id,ClientName FROM CRMPotentialClient WHERE id= '"+rsMap.get("ClientId")+"'";
			Result rs = clientMgt.publicSqlQuery(sql, new ArrayList());
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList rsList = (ArrayList)rs.retVal;
				if(rsList!=null && rsList.size()>0){
					rsMap.put("CRMClientInfo.ClientName", GlobalsTool.get(rsList.get(0),1));
				}
			}
		}
	}
	
	public void setCalendarBtn(String tableName,LinkedHashMap<String,List<String>> mainMap){
		//���۸����ڵ�һ����������calendarBtn�ֶΡ����������ճ�
		if("CRMSaleFollowUp".equals(tableName)){
			Set keys = mainMap.keySet( );
			if(keys != null) {
				Iterator iterator = keys.iterator( );
				while(iterator.hasNext()) {
					Object key = iterator.next();
					Object value = mainMap.get(key);
					mainMap.get(key).add("calendarBtn");
					break;
				}
			}
		}
		
	}
	
	/**
	 * ��װ��������
	 * @param rsMap
	 * @param moduleBean
	 * @param Allcols
	 * @param moduleFieldBeans
	 * @param exportValueList
	 * @param locale
	 * @param request
	 */
	private void setExportValue(HashMap rsMap,String tableName,BrotherFieldDisplayBean displayBean,ArrayList<HashMap> exportValueList,String locale,HttpServletRequest request,LoginBean loginBean){
		//rest = dbmgt.detail(moduleBean.getTableInfo().split(":")[0], map, keyIds[i], sunClassCode,props,loginBean.getId(),true,mop.getScope(MOperation.M_UPDATE),null);
		//HashMap rsMap = (HashMap)rest.retVal;//��ȡֵ
		List childInfoList = (ArrayList)rsMap.get("TABLENAME_"+tableName);//�õ��ӱ��List
		HashMap valMap = new HashMap();//���һ���ͻ���¼
		List childlist = new ArrayList();//��Ŵӱ�����
		HashMap childMap = new HashMap();	
		HashMap<String,String> deptMap = (HashMap<String, String>)request.getSession().getServletContext().getAttribute("deptMap");
		HashMap<String,String> defineDisMap = new CRMClientAction().getDefineDisplay(GlobalsTool.getTableInfoBean(tableName), rsMap, loginBean);//��ȡ�Զ��嵯����չʾ����
		for(String fieldName : displayBean.getPageFields().split(",")){
			String fieldVal = "";
			DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(tableName, fieldName);
			if(fieldBean !=null){
				if(rsMap.get(fieldName) !=null && !"".equals(rsMap.get(fieldName).toString())){
					if(fieldBean.getInputType() == 1){
						fieldVal = GlobalsTool.getEnumerationItemsDisplay(fieldBean.getRefEnumerationName(), rsMap.get(fieldName).toString(),locale);
					}else if(fieldBean.getInputType() == 14){
						fieldVal = deptMap.get(rsMap.get(fieldName));
					}else if(fieldBean.getInputType() == 15){
						fieldVal = GlobalsTool.getEmpFullNameByUserId(String.valueOf(rsMap.get(fieldName)));
					}else if(fieldBean.getInputType() == 2){
						fieldVal = defineDisMap.get("popup_"+fieldName); 
					}else{
						fieldVal = String.valueOf(rsMap.get(fieldName));
					}
				}
				valMap.put(fieldName,fieldVal);
			}
		}
		
		/*
		if(clientInfoList!=null && clientInfoList.size()>0){
			HashMap clientMap = new HashMap();
			for(int k=0;k<clientInfoList.size();k++){
				clientMap = (HashMap)clientInfoList.get(k);
				//���������ö������
				for(String col : Allcols){
					if(col !=null && !"".equals(col)){
						if(col.indexOf("contact")>-1){
							col = col.replace("contact", "");
							DBFieldInfoBean fieldBean = GlobalsTool.getField(col, moduleBean.getTableInfo().split(":")[0],moduleBean.getTableInfo().split(":")[1]);
							if(fieldBean !=null && fieldBean.getInputType()==1){
								if(clientMap.get(col) !=null && !"".equals(clientMap.get(col).toString())){
									String enumVal = GlobalsTool.getEnumerationItemsDisplay(fieldBean.getRefEnumerationName(), clientMap.get(col).toString(),getLocale(request).toString());
									if(enumVal!=null && !"".equals(enumVal)){
										clientMap.put(col, enumVal);
									}
								}
							}
						}
					}
				}
				childlist.add(clientMap);
			}
		}
		*/
		valMap.put("TABLENAME_"+tableName, childlist);
		exportValueList.add(valMap);
	}
	
	/**
	 * ��ȡֻ���ֶ�
	 * @param loginBean
	 * @param viewId
	 * @param request
	 */
	public void getOnlyReadFields(LoginBean loginBean,HttpServletRequest request,HashMap<String,String> workFlowFieldMap,String contactTableName){
		Result rs = mgt.findFieldScopeByType(loginBean, "read");//����ֻ���ֶε�list
		List<BrotherFieldScopeBean> readFieldScopeList = (List<BrotherFieldScopeBean>)rs.retVal;
		
		String readMainFieldsStr = ",";
		String readchildFieldsStr = ",";
		if(readFieldScopeList!=null && readFieldScopeList.size()>0){
			for(BrotherFieldScopeBean bean : readFieldScopeList){
				String readFieldsName = bean.getFieldsName();
				for(String str : readFieldsName.split(",")){
					if(str.startsWith("child")){
						str = str.substring(5);
						readchildFieldsStr +=str+",";
					}else{
						readMainFieldsStr +=str+",";
					}
				}
			}
		}
		
		if(workFlowFieldMap!=null){
			String workFlowReadFields = workFlowFieldMap.get("read");
			if(workFlowReadFields!=null && !"".equals(workFlowReadFields)){
				for(String str : workFlowReadFields.split(",")){
					/*if(str.startsWith(contactTableName)){
						str = str.replace(contactTableName,"");
						readchildFieldsStr +=str+",";
					}else{
						readMainFieldsStr +=str+",";
					}
					*/
					readMainFieldsStr +=str+",";
				}
			}
		}
	
		request.setAttribute("readMainFieldsStr",readMainFieldsStr);
		request.setAttribute("readchildFieldsStr",readchildFieldsStr);
	}
}