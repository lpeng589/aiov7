package com.koron.crm.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;
import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.crm.bean.BrotherFieldDisplayBean;
import com.koron.crm.bean.CRMLabelBean;
import com.koron.crm.bean.CRMShareClientBean;
import com.koron.crm.bean.ClientInfoRecordBean;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.koron.crm.brother.CRMBrotherAction;
import com.koron.crm.brother.CRMBrotherMgt;
import com.koron.crm.brotherSetting.CRMBrotherSettingMgt;
import com.koron.crm.salesFlow.CRMSalesFlowAction;
import com.koron.crm.salesFlow.CRMSalesFlowMgt;
import com.koron.crm.setting.ClientSetingMgt;
import com.koron.crm.setting.ClientSettingAction;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OATaskBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.discuss.DiscussAction;
import com.koron.oa.publicMsg.newsInfo.OANewsMgt;
import com.koron.oa.util.AttentionMgt;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.koron.oa.workflow.OAWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.FieldScopeSetBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.importData.ExportField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.AIOTelecomCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.ErrorMessage;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;

/**
 * 
 * <p>Title:客户列表</p> 
 * <p>Description: </p>
 *
 * @Date:Oct 23, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class CRMClientAction extends MgtBaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	CRMClientMgt mgt = new CRMClientMgt() ;
	ClientSetingMgt moduleMgt = new ClientSetingMgt() ;
	DynDBManager dbmgt = new DynDBManager();
	OANewsMgt omgt = new OANewsMgt();
	OAWorkFlowMgt workFlowMgt = new OAWorkFlowMgt();
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		String type = getParameter("type", request);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/CRMClientAction.do"));
		request.setAttribute("MOID",mop.moduleId);
		switch (operation) {
			case OperationConst.OP_ADD_PREPARE:
				if("enumer".equals(type)){
					forward = addEnumerPrepare(mapping, form, request, response);
				}else if("label".equals(type)){
					forward = addlabelPrepare(mapping, form, request, response);
				}else if("addContact".equals(type)){
					forward = addContactPrepare(mapping, form, request, response);
				}else{
					forward = addPrepare(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_ADD:
				if("enumer".equals(type)){
					forward = addEnumer(mapping, form, request, response);
				}else if("addLabel".equals(type)){
					forward = addLabel(mapping, form, request, response);
				}else if("addTransfer".equals(type)){
					forward = addTransfer(mapping, form, request, response);
				}else if("addContact".equals(type)){
					forward = addContact(mapping, form, request, response);
				}else if("addCommonInfo".equals(type)){
					forward = addCommonInfo(mapping, form, request, response);
				}else{
					forward = add(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_UPDATE_PREPARE:
				if("label".equals(type)){
					forward = updateLabelPrepare(mapping, form, request, response);
				}else if("updContact".equals(type)){
					forward = updContactPrepare(mapping, form, request, response);
				}else{
					forward = updatePrepare(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_UPDATE:
				if("reHistroy".equals(type)){
					forward = reHistroy(mapping, form, request, response);
				}else if("detailUpdate".equals(type)){
					forward = detailUpdate(mapping, form, request, response);
				}else if("labelBean".equals(type)){
					forward = updateLabelBean(mapping, form, request, response);
				}else if("labelUse".equals(type)){
					forward = labelUse(mapping, form, request, response);
				}else if("selLabel".equals(type)){
					forward = updateSelLabel(mapping, form, request, response);
				}else{
					forward = update(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_DETAIL:
				forward = detailNew(mapping, form, request, response);
				break;
			case OperationConst.OP_IMPORT_PREPARE:
				forward =importClient(mapping, form, request, response);
				break;
			case OperationConst.OP_IMPORT:
				forward =getModuleExcel(mapping,form,request,response);
				break;
			case OperationConst.OP_QUERY:
				if("contactVerify".equals(type)){
					forward = contactVerify(mapping, form, request, response);
				}else if("moduleTransfer".equals(type)){
					forward = moduleTransferPrepare(mapping, form, request, response);
				}else if("enumerVerify".equals(type)){
					forward = enumerVerify(mapping, form, request, response);
				}else if("history".equals(type)){
					forward = history(mapping, form, request, response);
				}else if("queryContactInfo".equals(type)){
					forward = queryContactInfo(mapping, form, request, response);
				}else if("label".equals(type)){
					forward = queryLabel(mapping, form, request, response);
				}else if("checkExistClients".equals(type)){
					forward = checkExistClients(mapping, form, request, response);
				}else if("showTransfer".equals(type)){
					forward = showTransferResult(mapping, form, request, response);
				}else if("popupSelectName".equals(type)){
					forward = checkPopupSelectName(mapping, form, request, response);
				}else if("transferRecord".equals(type)){
					forward = transferRecord(mapping, form, request, response);
				}else if("existClientNames".equals(type)){
					forward = checkExistClientNames(mapping, form, request, response);
				}else if("broFieldDisPlay".equals(type)){
					forward = existBroFieldDisPlay(mapping, form, request, response);
				}else if("phoneScreen".equals(type)){
					forward = phoneScreenQuery(mapping, form, request, response);
				}else if("clientLog".equals(type)){
					forward = clientLogQuery(mapping, form, request, response);
				}else{
					forward = query(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_DELETE:
				if("delLabel".equals(type)){
					forward = delLabel(mapping, form, request, response);
				}else if("affix".equals(type)){
					forward = delAffix(mapping, form, request, response);
				}else{
					forward = delete(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_POPUP_SELECT:
				if("handOver".equals(type)){
					forward = handOver(mapping, form, request, response);
				}else if("message".equals(type)){
					forward = message(mapping, form, request, response);
				}else if("selectStatus".equals(type)){
					forward = selectStatus(mapping, form, request, response);
				}else if("status".equals(type)){
					forward = updateStatus(mapping, form, request, response);
				}else if("sendPrepare".equals(type)){
					return sendPrepare(mapping, form, request, response);
				}else if("sendMessage".equals(type)){
					return sendMessage(mapping, form, request, response);
				}else if("stopClient".equals(type)){
					return stopClient(mapping, form, request, response);
				}else if("brotherTable".equals(type)){
					return brotherTable(mapping, form, request, response);
				}else if("brotherIframe".equals(type)){
					return brotherQuery(mapping, form, request, response);
				}else if("shareClient".equals(type)){
					return shareClient(mapping, form, request, response);
				}else if("shareValue".equals(type)){
					return shareValue(mapping, form, request, response);
				}else if("moduleTransfer".equals(type)){
					return moduleTransfer(mapping, form, request, response);
				}else if("setDefModule".equals(type)){
					return setDefModule(mapping, form, request, response);
				}else if("getWorkFlowInfo".equals(type)){
					forward = getWorkFlowInfo(mapping, form, request, response);
				}else if("deliverGoods".equals(type)){
					return deliverGoods(mapping, form, request, response);
				}else if("releaseInfo".equals(type)){
					return releaseInfo(mapping, form, request, response);
				}else if("extendButton".equals(type)){
					return extendButton(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_UPLOAD:	
				forward = affixUpload(mapping, form, request, response);
				break;
			case OperationConst.Op_AUDITING:
	            forward = cancelFlow(mapping, form, request, response);
	        	break;
			default:
				forward = query(mapping, form, request, response);
				break;
		}
		return forward;
	}

	/**
	 * 释放数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward releaseInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] keyIds = getParameters("keyId", request) ;		 /*客户Id*/
		String keyIdStr = "";
		for(String str : keyIds){
			keyIdStr += "'"+str+"',";
		}
		if(keyIdStr.endsWith(",")){
			keyIdStr = keyIdStr.substring(0,keyIdStr.length()-1);
		}
		String sql = "UPDATE CRMCLientInfo SET telecomRecordId='' WHERE workFlowNode <> '-1' and id in ("+keyIdStr+")";
		Result rs = mgt.operationSql(sql,new ArrayList());
		return query(mapping, form, request, response);
	}
	/**
	 * 拓展按钮
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward extendButton(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String defineName=request.getParameter("defineName");
		String[] keyIds = getParameters("keyId", request) ;		 /*客户Id*/
		Result rs=dbmgt.defineDelSql(defineName, keyIds, this.getLoginBean(request).getId(),null,this.getLocale(request),"");
		if( rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
			if(rs.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
            	// 自定义配置需要用户确认
            	ConfirmBean confirm=(ConfirmBean)rs.getRetVal();
            	String content=dbmgt.getDefSQLMsg(getLocale(request).toString(),confirm.getMsgContent());
            	String jsConfirmYes="";
            	String jsConfirmNo="";
            	
            	jsConfirmYes="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
            				+":"+confirm.getYesDefine()+";';this.parent.extendSubmit('"+defineName+"','',true);";
            	if(confirm.getNoDefine().length()>0){
            		jsConfirmNo="this.parent.document.form.defineInfo.value +='"+confirm.getFormerDefine()
            				   +":"+confirm.getNoDefine()+";';this.parent.extendSubmit('"+defineName+"','',true);";
            	}
                
            	EchoMessage.confirm().add(content).setJsConfirmYes(jsConfirmYes)
            						 .setJsConfirmNo(jsConfirmNo).setAlertRequest(request);
            	
            }else{
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
	        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/CRMClientAction.do").setAlertRequest(request);
				}
			}
			return getForward(request, mapping, "message");
		}else{
			return query(mapping, form, request, response);
		}
	}

	/**
	 * 获取信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward getWorkFlowInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String AuditingCount = GlobalsTool.getSysSetting("AuditingCount");//获取系统配置数量
		LoginBean loginBean = getLoginBean(request);
		ArrayList param = new ArrayList();
		
		String sql = "SELECT id FROM CRMClientInfo WHERE telecomRecordId = ? and workFlowNode <> '-1'";
		param.add(loginBean.getId());
		Result rs = mgt.publicSqlQuery(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			int count = Integer.parseInt(AuditingCount) - list.size();
			sql ="UPDATE CRMClientInfo SET telecomRecordId=? WHERE id in (SELECT TOP "+count+" id FROM CRMClientInfo WHERE isNull(telecomRecordId,'')='' and charIndex(';"+loginBean.getId()+";',checkPersons)>0 ORDER BY createTime)";
			rs = mgt.operationSql(sql, param);
		}
		return query(mapping, form, request, response);
	}

	/**
	 * 撤回按钮
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward cancelFlow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		OAMyWorkFlowMgt oamgt=new OAMyWorkFlowMgt();
    	Result rs=null;
    	String nextStep=request.getParameter("nextStep");
    	String id=request.getParameter("keyId");//在后面会作为审核流ID使用
    	String keyId=id;//单据本身ID
    	
		String designId=request.getParameter("designId");
		
		String currNode = "";
		String lastNodeId = "";
		String tableName=request.getParameter("tableName");
		if(tableName!=null&&tableName.length()>0){
			HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId, tableName) ;
			id = String.valueOf(flowMap.get("id"));//审核流ID
			currNode = String.valueOf(flowMap.get("currentNode"));
			lastNodeId = String.valueOf(flowMap.get("lastNodeId"));
			if(designId==null || designId.length()==0){
				designId = (String) flowMap.get("applyType") ;
			}
		}
		
		//判断已经审核完成，或者传递过来的当前节点不等于实际的当前节点，不允许执行此操作
		String workFlowNodeName="";
		String workFlowNode="";
		String tempTableName = new DynDBManager().getInsertTableName(tableName);//处理CRM多模板表名
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
        
		rs = new OAMyWorkFlowMgt().cancel(id, lastNodeId, currNode, this.getLoginBean(request), designId);
		String msg=this.getMessage(request, "oa.msg.cancelSucc");
		rs=oamgt.updateFlowDepict(designId, id,this.getLocale(request));
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(msg).setBackUrl("/CRMClientAction.do?operation=5&type=detailNew&keyId="+keyId).setAlertRequest(request);
			return getForward(request, mapping, "alert") ;
		}else{
			return query(mapping, form, request, response);
		}
	}

	private ActionForward delAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String clientId = getParameter("clientId",request);//客户ID
		String fileName = getParameter("fileName",request);//删除的文件名
		String tempFile = getParameter("tempFile",request);//是否临时文件,true:是,false:否
		
		if (fileName != null && fileName.length() != 0) {
			String sql = "UPDATE CRMClientInfo SET  attachment = replace(attachment,'"+fileName+";','') where id = '"+clientId+"'";
			ArrayList param = new ArrayList();
			Result rs = mgt.operationSql(sql, param);
			
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//删除正式的
				FileHandler.delete("CRMClientInfo",FileHandler.TYPE_AFFIX, fileName);
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
			
		}
	          
		return getForward(request, mapping, "blank");
	}
	/**
	 * 只取得客户名字
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
//	private void getClientName(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String clientId = getParameter("clientId",request);//客户ID
//		ArrayList param = new ArrayList();
//		if (clientId != null && clientId.length() != 0) {
//			String sql="select ClientName from CRMClientInfo where id=?";
//			param.add(clientId);
//			AIODBManager manager=new AIODBManager();
//			Result rs = manager.sqlList(sql, param);
//			if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
//				List<Object[]> list=(List<Object[]>)rs.getRetVal();
//				if(list.size()>0){
//					String clientname=(String) list.get(0)[0];
//					response.getWriter().print(clientname);
//				}
//			}
//		}
//	}

	/**
	 * 客户最新动态
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward clientLogQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String clientId = getParameter("clientId",request);
		/*客户最新动态*/
        Result result = mgt.queryClientLog(clientId,0) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("logList", result.retVal) ;
		}
		return getForward(request, mapping, "clientLog");
	}

	/**
	 * 快速添加日程、任务分派、联系记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addCommonInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tableName = getParameter("tableName",request);//表名
		LoginBean loginBean = getLoginBean(request);
		
		HashMap<String,String> conMap = new HashMap<String, String>();
		String id = IDGenerater.getId();
		conMap.put("clientId", getParameter("clientId",request));//客户ID
		conMap.put("content", getParameter("content",request));//内容
		conMap.put("startTime", getParameter("startTime",request));//日程开始时间
		conMap.put("time", getParameter("time",request));//日志时间
		conMap.put("finishTime", getParameter("finishTime",request));//任务完成时间
		conMap.put("taskUserId", getParameter("taskUserId",request));//任务执行人ID
		
		Result rs = mgt.addCommonInfo(tableName,id, conMap, loginBean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String msgInfo = id+"&&"+conMap.get("content")+"&&";
			if("OACalendar".equals(tableName)){
				if(conMap.get("startTime")!=null){
					msgInfo += conMap.get("startTime").substring(5);
				}
			}else if("OATask".equals(tableName)){
				if(conMap.get("finishTime")!=null){
					msgInfo += conMap.get("finishTime").substring(5);
				}
				
				if(!loginBean.getId().equals(conMap.get("taskUserId"))){
					msgInfo = "";
					
					//向负责人通知消息
					String title = loginBean.getEmpFullName()+"指派了任务【"+conMap.get("content")+"】给你";
					String url = "/OATaskAction.do?operation=5&taskId="+id;
					String content = "<a href=\"javascript:mdiwin('" + url + "','"+conMap.get("content")+"')\">"+title+"</a>";//内容
					new AdviceMgt().add(loginBean.getId(), title, content, conMap.get("taskUserId"), id, "OATask");
				}
				
			}else if("OAWorkLog".equals(tableName)){
				if(conMap.get("time")!=null){
					msgInfo += conMap.get("time").substring(5);
				}
				
			}else{
        		//插入客户历史记录
        		new CRMClientMgt().insertCRMCLientInfoLog(conMap.get("clientId"), tableName, "添加了联系记录", loginBean.getId());
				msgInfo += GlobalsTool.getHongVal("sys_date").substring(5);
			}
			request.setAttribute("msg",msgInfo);
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	private ActionForward updContactPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String contactId = getParameter("contactId",request);//联系人ID
		String isDetail = getParameter("isDetail",request);//是否详情进入
		LoginBean loginBean = getLoginBean(request);
		Result rs = mgt.queryClientIdByContactId(contactId);
		ArrayList list = (ArrayList)rs.retVal;
		String moduleId = "";
		String viewId = "";
		String clientId = "";
		if(list!=null && list.size()>0){
			clientId = String.valueOf(GlobalsTool.get(list.get(0),0));
		}
		
		String[] moduleArrInfo = this.getModuleInfo(moduleId, viewId, clientId, loginBean);
		String moduleCheckInfo = this.checkModuleInfo(moduleArrInfo);//检测是否存在模板、视图、客户、没有返回错误
		if("".equals(moduleCheckInfo)){
			moduleId = moduleArrInfo[0];
			viewId = moduleArrInfo[1];
		}else{
			EchoMessage.error().add(moduleCheckInfo).setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		
		
		Result results = moduleMgt.detailCrmModule(moduleId) ;
		ClientModuleBean module = (ClientModuleBean) results.retVal;
		results = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleView = (ClientModuleViewBean) results.retVal;
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		
		
		String tableName = module.getTableInfo().split(":")[0];//主表
		String contactTableName = module.getTableInfo().split(":")[1];//明细表
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		
		
		//获取隐藏字段信息
		List<FieldScopeSetBean> hideFieldScopeList = (List<FieldScopeSetBean>)mgt.findFieidScopes(loginBean.getDepartCode(), loginBean.getId(), "hidden",viewId).retVal;
		
		//获取过滤隐藏字段信息
		Result result = mgt.queryHideFields(contactTableName, false, moduleView, hideFieldScopeList,null,null);
		LinkedHashMap<String,List<String>> contactMap = (LinkedHashMap<String,List<String>>)result.retVal;	
		
		
		
		result = mgt.queryContactById(contactId);
		
		
		request.setAttribute("clientId",clientId);
		request.setAttribute("loginBean",loginBean);
		request.setAttribute("moduleId", moduleId);
		request.setAttribute("viewId", viewId);
		request.setAttribute("contactMap", contactMap);
		request.setAttribute("contactTableName",contactTableName);
		request.setAttribute("contactValueMap",result.retVal);
		request.setAttribute("contactId",contactId);
		request.setAttribute("isDetail",isDetail);
		request.setAttribute("tableName", tableName);
		return getForward(request, mapping, "updateContact");
	}

	/**
	 * 来电弹屏查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward phoneScreenQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
			String CallNumber = getParameter("CallNumber",request);
			
			//String sql ="SELECT top 1 CRMClientInfoDet.UserName,CRMClientInfoDet.Mobile,CRMClientInfoDet.ClientEmail,CRMClientInfo.ClientName,CRMClientInfo.Address,CRMClientInfo.id from CRMClientInfoDet LEFT JOIN CRMClientInfo ON CRMClientInfoDet.f_ref=CRMClientInfo.id WHERE Mobile like '"+CallNumber+"%' or Telephone like '"+CallNumber+"%'";
			String sql ="SELECT top 1 CRMClientInfoDet.UserName,CRMClientInfoDet.Gender,CRMClientInfoDet.Role,CRMClientInfo.ClientName,CRMClientInfo.District,CRMClientInfo.moduleId,CRMClientInfo.id from CRMClientInfoDet LEFT JOIN CRMClientInfo ON CRMClientInfoDet.f_ref=CRMClientInfo.id WHERE Mobile like '"+CallNumber+"%' or Telephone like '"+CallNumber+"%'";
			Result rs = mgt.publicSqlQuery(sql,new ArrayList());
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> list =(ArrayList)rs.retVal;
				if(list!=null && list.size()>0){
					Object obj = list.get(0);
					String userName = GlobalsTool.get(obj,0)+"";
					String gender = GlobalsTool.get(obj,1)+"";
					String role = GlobalsTool.get(obj,2)+"";
					String clientName = GlobalsTool.get(obj,3)+"";
					String distinct = GlobalsTool.get(obj,4)+"";
					String moduleId = GlobalsTool.get(obj,5)+"";
					String clientId = GlobalsTool.get(obj,6)+"";
					
					Result rest = moduleMgt.detailCrmModule(moduleId) ;
					ClientModuleBean moduleBean = (ClientModuleBean) rest.retVal;
					String tableName = moduleBean.getTableInfo().split(":")[0];
					String childName = moduleBean.getTableInfo().split(":")[1];
					String local = getLocale(request).toString();
					
					
					
					if(gender!=null && !"".equals(gender)){
						DBFieldInfoBean bean = GlobalsTool.getFieldBean(childName, "Gender");
						gender =GlobalsTool.getEnumerationItemsDisplay(bean.getRefEnumerationName(), gender,local);
					}
					
					if(role!=null && !"".equals(role)){
						DBFieldInfoBean bean = GlobalsTool.getFieldBean(childName, "Role");
						role =GlobalsTool.getEnumerationItemsDisplay(bean.getRefEnumerationName(), gender,local);
					}
					
					
					if(distinct!=null && !"".equals(distinct)){
						HashMap<String,String> deptMap = (HashMap<String, String>)request.getSession().getServletContext().getAttribute("deptMap");
						HashMap<String,String> districtMap = (HashMap<String,String>)request.getSession().getServletContext().getAttribute("districtMap");
						distinct = districtMap.get(distinct);
					}
					
					String msg = userName+":"+gender+":"+role+":"+clientName+":"+distinct+":"+clientId;
					
//					String msg = GlobalsTool.get(obj,0)+":"+GlobalsTool.get(obj,1)+":"+GlobalsTool.get(obj,2)+":"+GlobalsTool.get(obj,3)
//					+":"+GlobalsTool.get(obj,4)+":"+GlobalsTool.get(obj,5);
					request.setAttribute("msg",msg);
				}else{
					request.setAttribute("msg","empty");
				}
				
			}else{
				request.setAttribute("msg","error");
			}
			return getForward(request, mapping, "blank");
	}

	private ActionForward existBroFieldDisPlay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String tableName = getParameter("tableName",request);
		Result result = new CRMBrotherSettingMgt().loadBrotherFieldDisplayBean(tableName);
		BrotherFieldDisplayBean fieldDisplayBean = (BrotherFieldDisplayBean)result.retVal;
		if(fieldDisplayBean==null || fieldDisplayBean.getListFields()==null || "".equals(fieldDisplayBean.getListFields())){
			request.setAttribute("msg","error");//不存在
		}else{
			request.setAttribute("msg","success");//有字段
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 新建联系人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addContact(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String contactId = getParameter("contactId",request);//联系人ID
		String clientId = getParameter("clientId",request);//客户ID
		String contactTableName = getParameter("contactTableName",request);//联系人表
		String moduleId = getParameter("moduleId",request);//模板ID
		String viewId = getParameter("viewId",request);//视图ID
		
		
		if(viewId == null || "".equals(viewId)){
			viewId ="1_1";
		}
		
		Result result = moduleMgt.detailCrmModule(moduleId);
		ClientModuleBean moduleBean = (ClientModuleBean) result.retVal;
		
		result = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleViewBean = (ClientModuleViewBean) result.retVal;
		
		
		String tableName = moduleBean.getTableInfo().split(":")[0];//主表结构名称
		//获取联系人信息
		HashMap values = new HashMap() ;
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(contactTableName) ;
		readChildTable(values, tableInfo, request, "00001");
		ArrayList<HashMap<String,String>> valueList = (ArrayList<HashMap<String,String>>)values.get("TABLENAME_"+contactTableName);
		
		
		
		//处理CRM ERP同步问题
		LoginBean loginBean = getLoginBean(request);
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;
		String sunClassCode = loginBean.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		result = dbmgt.detail(tableName, map, clientId, sunClassCode,props,loginBean.getId(),true,null);
		
		HashMap clientValMap = (HashMap)result.retVal;
		ArrayList<HashMap<String,String>> contactList = (ArrayList<HashMap<String,String>>)clientValMap.get("TABLENAME_"+contactTableName);//存放原有的联系人LIST
		ArrayList<HashMap<String,String>> newContactList = new ArrayList<HashMap<String,String>>();//用来存放新的联系人LIST
		
		if(contactList!=null && contactList.size()>0){
			for(HashMap<String,String> detMap: contactList){
				//发现相同ID的过滤
				if(!detMap.get("id").equals(String.valueOf(valueList.get(0).get("id")))){
					newContactList.add(detMap);
				}
			}
			newContactList.addAll(valueList);
			clientValMap.put("TABLENAME_"+contactTableName, newContactList);
		}else{
			clientValMap.put("TABLENAME_"+contactTableName, valueList);
		}
		
		result = mgt.addContact(clientId, moduleViewBean.getPageFields(), valueList,contactId,moduleBean,clientValMap);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			String msg = getMessage(request, "common.msg.addSuccess");
			if(contactId!=null && !"".equals(contactId)){
				 msg = getMessage(request, "common.msg.updateSuccess");
			}
			String isConsole = getParameter("isConsole",request);//true表示从客户控制台进入,跳转联系人详情页面
			if(isConsole!=null && "true".equals(isConsole)){
				String url ="/CRMClientAction.do?operation=5&isContactEnter=true&keyId="+clientId;
				EchoMessage.success().add(msg).setBackUrl(url).setAlertRequest(request);
			}else{
				request.setAttribute("crmFreash", "true");
				EchoMessage.success().add(msg).setAlertRequest(request);
			}
		}else if(result.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
        	// 自定义配置需要用户确认
        	ConfirmBean confirm=(ConfirmBean)result.getRetVal();
        	String content=dbmgt.getDefSQLMsg(getLocale(request).toString(),confirm.getMsgContent());
        	String jsConfirmYes="";
        	String jsConfirmNo="";
        	String saveAdd = request.getParameter("button");
            if (saveAdd != null && saveAdd.equals("saveAdd")) { //依然延续交互弹出框之前的保存方式
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

        }else {
        	SaveErrorObj saveErrrorObj = dbmgt.saveError(result, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
			//添加失败
        }
		return getForward(request, mapping, "alert");
	}

	/**
	 * 添加联系人前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addContactPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String clientId = getParameter("clientId",request);//客户id
		String moduleId = getParameter("moduleId",request);//模板ID
		String viewId = getParameter("viewId",request);//视图
		
		
		//没有模板或视图直接用默认模板的
		if(moduleId == null || "".equals(moduleId)){
			moduleId = "1";//模板ID
		}
		
		if(viewId == null || "".equals(viewId)){
			viewId = "1_1";//视图ID
		}
		
		
		LoginBean loginBean = getLoginBean(request);
		Result results = moduleMgt.detailCrmModule(moduleId) ;
		ClientModuleBean module = (ClientModuleBean) results.retVal;
		results = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleView = (ClientModuleViewBean) results.retVal;
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		
		
		String tableName = module.getTableInfo().split(":")[0];//主表
		String contactTableName = module.getTableInfo().split(":")[1];//明细表
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		
		//获取隐藏字段信息
		List<FieldScopeSetBean> hideFieldScopeList = (List<FieldScopeSetBean>)mgt.findFieidScopes(loginBean.getDepartCode(), loginBean.getId(), "hidden",viewId).retVal;
		
		//获取过滤隐藏字段信息
		Result result = mgt.queryHideFields(contactTableName, false, moduleView, hideFieldScopeList,null,null);
		LinkedHashMap<String,List<String>> contactMap = (LinkedHashMap<String,List<String>>)result.retVal;	
		
		
		request.setAttribute("clientId",clientId);
		request.setAttribute("loginBean",loginBean);
		request.setAttribute("moduleId", moduleId);
		request.setAttribute("viewId", viewId);
		request.setAttribute("contactMap", contactMap);
		request.setAttribute("contactTableName",contactTableName);
		request.setAttribute("emailAddress",getParameter("emailAddress", request));//从邮件进入默认显示出来
		request.setAttribute("isConsole",getParameter("isConsole", request));//从邮件进入默认显示出来
		return getForward(request, mapping, "addContact");
	}

	/**
	 * 共享客户
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward shareValue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String clientIds = getParameter("clientIds", request);//客户IDS
		String isShareClient = getParameter("isShareClient", request);//是否提醒共享人
		String isSingle = getParameter("isSingle", request);//判断单个还是批量,true表示单个
		String moduleId = getParameter("moduleId", request);
		String viewId = getParameter("viewId", request);
		LoginBean login = getLoginBean(request) ;
		HashMap<String,String> clientMap = new HashMap<String, String>();
		List<CRMShareClientBean> shareBeans = new ArrayList<CRMShareClientBean>();
		Result rs = new Result();
		List<Object> allEmployList = new ArrayList<Object>();
		List<Object> saveEmployList = new ArrayList<Object>();
		List<Object> delEmployList = new ArrayList<Object>();
		
		//选择的所有用户ID
		String allDeptIds = getParameter("allDeptIds", request);
		String allUserIds = getParameter("allUserIds", request);
		String allTitleIds = getParameter("allTitleIds", request);
		if(!"".equals(allDeptIds) || !"".equals(allUserIds) || !"".equals(allTitleIds)){
			rs = mgt.findEmpoyeeByAll(allUserIds, allDeptIds,"",allTitleIds);
			allEmployList = (List<Object>)rs.retVal;
		}
		
		
		//发送消息,需要新增的部门,用户,分组IDS
			String addDeptIds = getParameter("addDeptIds", request);
			String addUserIds = getParameter("addUserIds", request);
			String addTitleIds = getParameter("addTitleIds", request);
		if("1".equals(isShareClient)){
			if(!"".equals(addDeptIds) || !"".equals(addUserIds) || !"".equals(addTitleIds)){
				rs = mgt.findEmpoyeeByAll(addUserIds, addDeptIds,"",addTitleIds);
				saveEmployList = (List<Object>)rs.retVal;
			}
		}
		
		//需要删除的部门,用户,分组IDS
		String delDeptIds = getParameter("delDeptIds", request);
		String delUserIds = getParameter("delUserIds", request);
		String delTitleIds = getParameter("delTitleIds", request);
		if(!"".equals(delDeptIds) || !"".equals(delUserIds) || !"".equals(delTitleIds)){
			rs = mgt.findEmpoyeeByAll(delUserIds, delDeptIds,"",delTitleIds);
			delEmployList = (List<Object>)rs.retVal;
		}
		
		String[] allIds = new String[3];//存放用户、部门、职位所有值,用于共享表没有本客户信息就插入不更新
		allIds[0]=allUserIds;
		allIds[1]=allDeptIds;
		allIds[2]=allTitleIds;
		
		HashMap<String, CRMShareClientBean> shareBeanMap = new HashMap<String, CRMShareClientBean>();
		if(clientIds !=null && !"".equals(clientIds)){
			String ids = "";
			for(String str : clientIds.split(",")){
				ids += "'"+str+"',";
			}
			if(ids.endsWith(",")){
				ids = ids.substring(0,ids.length()-1);
			}
			
			
			if("1".equals(isShareClient)){
				//发送信息获取客户名称的MAP
				rs = mgt.queryClientNameByIds(ids);
				clientMap = (HashMap<String,String>)rs.retVal;
			}
			
			
			rs = mgt.findSharerByClientId(ids);
			shareBeans = (List<CRMShareClientBean>)rs.retVal;//根据ids获取共享bean
			if(shareBeans!=null && shareBeans.size()>0){
				if(shareBeans.size()==1){
					//单个共享直接替换
					shareBeans.get(0).setPopedomUserIds(allUserIds);
					shareBeans.get(0).setPopedomDeptIds(allDeptIds);
					shareBeans.get(0).setPopedomTitleIds(allTitleIds);
					shareBeanMap.put(shareBeans.get(0).getClientId(), shareBeans.get(0));
				}else{
					//多个共享累加选项
					for(CRMShareClientBean bean : shareBeans){
						bean.setPopedomUserIds(this.getIndexIds(bean.getPopedomUserIds(), allUserIds));
						bean.setPopedomDeptIds(this.getIndexIds(bean.getPopedomDeptIds(), allDeptIds));
						bean.setPopedomTitleIds(this.getIndexIds(bean.getPopedomTitleIds(), allTitleIds));
						shareBeanMap.put(bean.getClientId(), bean);
					}
				}
			}
		}
		Result result = mgt.shareClientEmp(clientIds,allEmployList,saveEmployList,delEmployList,moduleId,viewId,clientMap,login,isShareClient,isSingle,shareBeanMap,allIds) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List<String[]> listAdvice = (List<String[]>) result.getRetVal();
			for (String[] val : listAdvice) {
				if (val.length == 2) {
					new AdviceMgt().deleteByRelationId(val[0], val[1]);
				} else {
					new AdviceMgt().add(val[0], val[1], val[2], val[3], val[4], val[5]);
				}
			}
			request.setAttribute("msg", "ok");
		}else{
			request.setAttribute("msg", "no");
		}
		return getForward(request, mapping, "blank") ;
	}
	
	/**
	 * 共享客户之前
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward shareClient(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String clientIds = getParameter("clientIds", request);
		String isSingle = "false";
		if(clientIds !=null && !"".equals(clientIds) && clientIds.split(",").length<2){
			if(clientIds.endsWith(",")){
				clientIds = clientIds.substring(0,clientIds.length()-1);
			}
			Result rs=mgt.findSharerByClientId("'"+clientIds+"'");
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && ((List<CRMShareClientBean>)rs.retVal).size()>0){
				CRMShareClientBean shareClientBean = ((List<CRMShareClientBean>)rs.retVal).get(0);
				/*获取授权对象（用户、部门、组）*/
				List<EmployeeBean> targetUsers =omgt.getEmployee(shareClientBean.getPopedomUserIds());
				List<Object> targetDept = omgt.getDepartment(shareClientBean.getPopedomDeptIds());
				//List<Object> listEmpGroup = omgt.getEmpGroup(shareClientBean.getPopedomEmpGroupIds());
				request.setAttribute("shareClientBean", shareClientBean);
				request.setAttribute("targetUsers", targetUsers);
				request.setAttribute("targetDept", targetDept);
				//request.setAttribute("targetEmpGroup", listEmpGroup);
				request.setAttribute("isSingle", "true");
				isSingle = "true";
			}
		}
		request.setAttribute("clientIds", clientIds);
		request.setAttribute("isSingle", isSingle);
		return getForward(request, mapping, "shareClient") ;
	}
	
	/**
	 * 兄弟表数据查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward brotherQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String tableName = getParameter("tableName", request) ;
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName) ;
		String locale = getLocale(request).toString() ;
		LoginBean loginBean = getLoginBean(request);
		String brotherId = getParameter("f_brother", request);
		if("CRMClientInfoLog".equals(tableName)){
			/*客户最新动态*/
	        Result result = mgt.queryClientLog(brotherId,0) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("logList", result.retVal) ;
			}
			return getForward(request, mapping, "clientLog");
		}
		
		request.setAttribute("popup", "crmPopup") ;
		
		//获取权限路径
		String mopUrl = "/CRMBrotherAction.do?tableName="+tableName;
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get(mopUrl);
		
		if("CRMMailInfoView".equals(tableName)){
			//处理邮件
			ArrayList<Object> mailList = new ArrayList<Object>();//存放筛选后的邮件数据	
			Result rs = mgt.findContactByClientId(brotherId, "ClientEmail");
			List<Object> rsList = (List<Object>)rs.retVal;
			
			String contactMails = "";//客户联系人邮箱
			String selectEMails = "";//记录所有的邮件
			if(rsList != null && rsList.size()>0){
				for(Object obj : rsList){
					String userName = GlobalsTool.get(obj,0).toString();
					if(userName!=null && !"".equals(userName)){
						contactMails += userName +",";
					}
				}
				rs = mgt.queryMailInfoByMails(contactMails);
				mailList = (ArrayList<Object>)rs.retVal;
				
				//封装筛选数据每行邮箱Map<id,mails>
				HashMap<String,String> rowMailMap = new LinkedHashMap<String, String>();
				
				for(Object obj : mailList){
					String mailStr = "";
					
					//toUserId==null表示是发送的
					if(GlobalsTool.get(obj,8) == null ){
						//封装mailTo与mailCC邮件
						String mailTo = GlobalsTool.get(obj,1)+"" ;
						String mailCC = GlobalsTool.get(obj,3)+"";
						if(mailTo!=null){
							for(String str : mailTo.split(",")){
								mailStr +=GlobalsTool.getMailAddress(str)+",";
							}
						}
						if(mailCC!=null){
							for(String str : mailCC.split(",")){
								mailStr +=GlobalsTool.getMailAddress(str)+",";
							}
						}
					}else{
						//fromUserId==null表示是接收的
						String mailFrom = GlobalsTool.get(obj,2).toString();
						mailStr +=GlobalsTool.getMailAddress(mailFrom)+",";
					}
					rowMailMap.put(GlobalsTool.get(obj,0).toString(),mailStr);
					selectEMails +=mailStr;
				}
				
				
				//封装查找联系人数据库条件
				String emailCondition = "";
				if(!"".equals(selectEMails)){
					for(String mail:selectEMails.split(",")){
						if(emailCondition.indexOf(mail)==-1){
							emailCondition += "'"+mail+"',";
						}
					}
					if(emailCondition.endsWith(",")){
						emailCondition = emailCondition.substring(0,emailCondition.length()-1);
					}
				}

				rs = mgt.queryContactByEmail(emailCondition);
				HashMap<String,String> filterMailMap = new LinkedHashMap<String, String>(); 
				filterMailMap = (HashMap<String,String>)rs.retVal;
				
				
				//替换rowMailMap 的邮箱地址
				HashMap<String,String> showMailMap = new LinkedHashMap<String, String>();
				Set Keys = rowMailMap.keySet();
				if(Keys != null) {
					Iterator iterator = Keys.iterator();
					while(iterator.hasNext()){
						Object key = iterator.next();
						String value = rowMailMap.get(key);
						String mailDisplay = "";//存放邮件联系人名称
						if(value!=null){
							for(String str: value.split(",")){
								if(filterMailMap.get(str)!=null){
									mailDisplay += filterMailMap.get(str)+",";
								}
							}
							if(mailDisplay.endsWith(",")){
								mailDisplay = mailDisplay.substring(0,mailDisplay.length()-1);
							}
							showMailMap.put(key.toString(), mailDisplay);
						}
					}
				}
				request.setAttribute("showMailMap",showMailMap);
			}
			request.setAttribute("contactMails",contactMails);
			request.setAttribute("mailList",mailList);
		}else if(mop == null ){
			DefineReportBean defBean = DefineReportBean.parse(tableName + "SQL.xml", GlobalsTool.getLocale(request).toString(),getLoginBean(request).getId());
			Result rs = new ReportData().showData(null,request, new ArrayList(),tableName,defBean,"",1, 1000,null,null,getLoginBean(request),null);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList rows = (ArrayList)rs.retVal ;
				ArrayList newRows = new ArrayList() ;
				for (int i = 0; i < rows.size(); i++) {
					Object[] row = (Object[]) rows.get(i);
					if(row[0].toString().contains("rowAll")) continue;
					/*去掉头尾''*/
					String content = row[0].toString().substring(1,row[0].toString().length()) ;
					content = content.substring(0,content.length()-1) ;
					/*按,分开*/
					String[] rowData = content.split("','");
					int maxIndex = 7;//默认数组长度为7
					if((rowData.length+1)>=7){
						maxIndex = rowData.length+1;
					}
					String[] rowNew = new String[maxIndex] ;
					for (int j = 0; j < rowData.length; j++) {
						rowNew[j] = rowData[j].split(";/")[0];
					}
					rowNew[maxIndex-1] = String.valueOf(row[1]);//存放单据id,用于销售跟单点击详情
					newRows.add(rowNew) ;
				}
				request.setAttribute("result", newRows) ;
			}
			ArrayList cols = (ArrayList) request.getAttribute("cols") ;
		}else{
			Result result = new CRMBrotherSettingMgt().loadBrotherFieldDisplayBean(tableName);
			BrotherFieldDisplayBean fieldDisplayBean = (BrotherFieldDisplayBean)result.retVal;
			
			StringBuilder condition = new StringBuilder();//条件语句
			//获取明细表表名
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
			
			//查看单据权限
			if(!"1".equals(loginBean.getId())){
				String scopeDeptCode = "";//除了客户资料保存部门权限字符串
				/*查看某字段值单据*/
				String fieldValueSQL = "" ;
				
				condition.append(" and (").append(tableName).append(".createBy = '").append(loginBean.getId()).append("' or ").append(tableName).append(".employeeId  ='").append(loginBean.getId()).append("' ");
				
				
				if(mop!=null){
					ArrayList scopeRight = this.getAllMopByType(mop, loginBean, MOperation.M_QUERY);
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
									//把部门所有存放起来
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
						
						//处理除了客户资料,其他表单的部门权限
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
			condition.append(" and ClientId='").append(brotherId).append("' ");
			
			Result rest = new CRMBrotherMgt().query(tableName,"",fieldDisplayBean,condition.toString(),null, 1, 1000,null,"all",loginBean.getId());
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				List brotherList = (List) rest.retVal ;
				if(brotherList!=null && brotherList.size()>0){
					List<String> relateClientSqlList = new CRMBrotherAction().getRelateClientSqlList(fieldDisplayBean, tableName);
					Result relateRs = new CRMBrotherMgt().relateClientAllInfoQuery(relateClientSqlList, brotherList);
					request.setAttribute("relateClientMap",relateRs.retVal);  
				}
				
				//显示字段，取前五个字段
				String[] displayFields = new String[5];
				String[] listFields = fieldDisplayBean.getListFields().split(",");
				
				int i=0;
				for(String str : fieldDisplayBean.getListFields().split(",")){
					if("ClientId".equals(str)){
						continue;
					}
					displayFields[i] = str;
					i++;
					if(displayFields[4]!=null ){
						break;
					}
					
				}
				
				request.setAttribute("displayFields", displayFields);
				request.setAttribute("brotherList", brotherList);
				request.setAttribute("tableName",tableName);
				request.setAttribute("fieldDisplayBean",fieldDisplayBean);
			}
			
		}
		
		request.setAttribute("f_brother", getParameter("f_brother", request)) ;
		request.setAttribute("tableInfo", tableInfo);
		request.setAttribute("tableDisplay", tableInfo.getDisplay().get(locale)) ;
		request.setAttribute("loginBean",getLoginBean(request));
		return getForward(request, mapping, "brotherIframe") ;
	}
	
	/**
	 * 弹出兄弟表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward brotherTable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		//得到用户有没有操作邻居表设置的权限
//        MOperation mopNeighbour = (MOperation) this.getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblNeighbourMain");
//        if(mopNeighbour!=null){
//        	request.setAttribute("updatestate", mopNeighbour.update);
//        }
        
        String viewId = getParameter("viewId", request);
//        Result result = moduleMgt.loadModuleView(viewId);
//        //Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
//        //ArrayList childTabList = DDLOperation.getBrotherTables("CRMClientInfo", map);
//        /*查询用户是否设置邻居了*/
//        //Result result = new TabMgt().selectBrotherTable(map,"CRMClientInfo",getLoginBean(request).getId()) ;
//        ArrayList childTabList = new ArrayList();
//        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
//        LoginBean loginBean=this.getLoginBean(request);
//        
//        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
//        	ClientModuleViewBean moduleView = (ClientModuleViewBean) result.retVal ;
//        	  Result rsBroTab = new TabMgt().selectBroTabByViewId(map,"CRMClientInfo",loginBean.getId(),viewId) ;
//	   	        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){ 
//	   	          	ArrayList<DBTableInfoBean> listBrother = (ArrayList<DBTableInfoBean>) rsBroTab.retVal ;
//	   	        	if(listBrother.size()>0){ //判断用户是否给视图设置了兄弟表排序
//	   	        		childTabList = listBrother ;
//	   	        	}else{
//	   	        		String[] childArray = moduleView.getBrotherTables().split(",");
//	   	        		for(String child : childArray){
//	   	        			DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(child) ;
//	   	        			if(tableInfo!=null){
//	   	        				childTabList.add(tableInfo);
//	   	        			}
//	   	        		}
//	   	        	}
//	   	        }
//        	
//        }
//        ArrayList moduleList = BaseEnv.allModule;
//        for(int i=0;i<childTabList.size();i++){
//        	//判断模块是否启用
//        	DBTableInfoBean tableIfno = (DBTableInfoBean) childTabList.get(i) ;
//			Result result2 = new Result() ;
//			GlobalsTool.moduleIsUsed(moduleList, tableIfno.getTableName(),result2) ;
//			MOperation mop = (MOperation) getLoginBean(request).getOperationMap()
//						.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName="+tableIfno.getTableName()) ;
//			if(result2.retCode != ErrorCanst.DATA_ALREADY_USED || mop==null){
//				childTabList.remove(i) ;
//				i-- ;
//			}
//        }
       
        
        Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		ArrayList<DBTableInfoBean> brotherTableList = new DDLOperation().getBrotherTables("CRMClientInfo", allTables);//获取兄弟表信息		
		
		ArrayList list = new ArrayList();
		Result rs = moduleMgt.brotherSortQueryByViewId(viewId);
		list = (ArrayList)rs.retVal;
		String userBrotherInfo = "";//已排序的邻居表信息
		if(list!=null && list.size()>0){
			userBrotherInfo = GlobalsTool.get(list.get(0),1).toString();
		}
		brotherTableList = new ClientSettingAction().checkModuleUse(brotherTableList, request);//过滤不启用模块
		
		//存放在map中
		HashMap<String,DBTableInfoBean> moduleMap = new HashMap<String, DBTableInfoBean>();
		for(DBTableInfoBean tableBean : brotherTableList){
			moduleMap.put(tableBean.getTableName(), tableBean);
		}
		
		request.setAttribute("userBrotherInfo", userBrotherInfo);
		request.setAttribute("moduleMap", moduleMap);
        request.setAttribute("viewId", viewId);
        request.setAttribute("keyId", getParameter("keyId", request)) ;
        request.setAttribute("loginBean", getLoginBean(request)) ;
		return getForward(request, mapping, "brotherTable");
	}
	
	/**
	 * 客户导入
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward importClient(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		LoginBean login = getLoginBean(request) ;
    	//Result rs=moduleMgt.queryMyModules(login);
		String moduleId = request.getParameter("moduleId");
		Result rs = moduleMgt.detailCrmModule(moduleId) ;
		ClientModuleBean moduleBean = (ClientModuleBean) rs.retVal;
		rs=moduleMgt.queryAllModuleViews(login,moduleBean.getId());
    	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("viewList", rs.retVal);
			request.setAttribute("moduleBean", moduleBean);
			request.setAttribute("viewId", request.getParameter("viewId"));
		}
		return mapping.findForward("importClient");
	}
	/**
	 * 启用或停用客户
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward stopClient(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = getParameters("keyId", request) ;  	/*客户Id*/
		String status  = getParameter("clientStatus", request) ;	/*状态*/
		String ispublic = getParameter("public", request) ;		/*公共池客户*/
		String openValue = getMessage(request, "OpenValue") ;    /*启用*/
		String stopValue = getMessage(request, "StopValue") ;   /*停用*/
		String client = getMessage(request, "call.lb.client") ;  /*客户*/  
		
		Result result = mgt.updateStatus(keyIds, status,getLoginBean(request),openValue,stopValue,client) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(request, "oa.bbs.operationOk"))
				.setBackUrl("/CRMClientAction.do?public="+ispublic).setAlertRequest(request);
		}else if(result.retCode == ErrorCanst.OVER_WILL_CLIENT_QTY){
			EchoMessage.success().add(getMessage(request, "CRMClientInfo.add.ExistError7"))
				.setBackUrl("/CRMClientAction.do").setNotAutoBack().setAlertRequest(request);
		}else{
			EchoMessage.success().add(getMessage(request, "common.alert.updateFailure"))
				.setBackUrl("/CRMClientAction.do?public="+ispublic).setAlertRequest(request);
		}
		return mapping.findForward("message");
	}
	
	/**
	 * 发送短信
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward sendMessage(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = getParameters("keyId", request) ;
		String mobile = getParameter("newCreateBy", request) ; /*手机号码*/ 
		String sendMessage = getParameter("handContent", request) ; /*短信内容*/
		String smsType = getParameter("handSmsType", request) ; /*短信内容*/
		int smsTypei = 0;
		try{
			smsTypei =Integer.parseInt(smsType);
		}catch(Exception e){}
		//String msgType  = getParameter("msgType", request) ;
		
		String sendContent = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+getMessage(request, "timeNote.lb.note")+" :"+sendMessage ;
		
		String[] mobiles = mobile.split(",") ;
		if (BaseEnv.telecomCenter == null){
			EchoMessage.info().add(getMessage(request, "sms.note.stop")).setAlertRequest(request);
		} else{
			BaseEnv.telecomCenter.send(sendMessage,mobiles,getLoginBean(request).getId());			
			for(String clientId : keyIds){
				mgt.insertCRMCLientInfoLog(clientId, "sms", sendContent, getLoginBean(request).getId()) ;
			}
			if("detail".equals(getParameter("strType", request))){
				EchoMessage.success().add(getMessage(request, "common.note.sendsuccess")).setBackUrl("/CrmTabAction.do?operation=5&keyId="+keyIds[0]).setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(request, "common.note.sendsuccess")).setBackUrl("/CRMClientAction.do").setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 发送短信之前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward sendPrepare(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyId =getParameter("keyId", request); /*客户Id*/
		String msgType = getParameter("msgType", request) ; /*消息类型 sms:短信,email:邮件*/
		Result result=null;
		if(!msgType.contains("detail")){ //列表页面发送信息
			result = mgt.selectClientDetById(keyId.split(","),msgType) ;
			request.setAttribute("clientId", keyId) ;
			request.setAttribute("msgType", msgType) ;
		}else{ //详情页面发送信息
			result=mgt.detailClientDetById(keyId);
			request.setAttribute("sendType", "detailPage");
		}
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("clientDetList", result.retVal) ;
			request.setAttribute("smsSign", AIOTelecomCenter.smsSign);
			request.setAttribute("smsSignLen", AIOTelecomCenter.smsSign==null?0:(AIOTelecomCenter.smsSign.length()+2));
		}
		return mapping.findForward("sendPrepare");	
	}
	
	/**
	 * 根据模板选择生命周期
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward selectStatus(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String moduleId = getParameter("moduleId", request);
		Result rs = moduleMgt.detailCrmModule(moduleId);
		 if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			 ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
			 DBFieldInfoBean fieldInfo = GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], "Status");
			 request.setAttribute("enumerationName", fieldInfo.getRefEnumerationName());
			 return mapping.findForward("selectStatus");
		 }else{
			 EchoMessage.error().add("查询生命周期报错").setNotAutoBack().setAlertRequest(request);
			 return getForward(request, mapping, "message");
		 }
	}
	
	/**
	 * 客户资料 生命周期转换
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateStatus(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = getParameters("keyId", request) ;  	/*客户Id*/
		String status  = getParameter("strStatus", request) ;	/*状态*/
		String ispublic = getParameter("public", request) ;		/*是否是公共池客户*/
		String change = getMessage(request, "CRMClientInfo.add.change") ;	/*转为*/
		String moduleId = getParameter("ModuleId", request); 
		String viewId = getParameter("viewId", request); 
		LoginBean login = getLoginBean(request) ;
		
		
		String ip = GlobalsTool.getIpAddr(request);
		
		
		
		
		String tableName = "";//表名
		Result rs = moduleMgt.detailCrmModule(moduleId) ;
		ClientModuleBean moduleBean = (ClientModuleBean) rs.retVal;
		tableName = moduleBean.getTableInfo().split(":")[0];


		//获取详情参数
		MOperation mop = (MOperation) login.getOperationMap().get("/CRMClientAction.do") ;
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = login.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(tableName, "Status");
		String enumName = fieldInfoBean.getRefEnumerationName();//枚举名称
		List<ClientInfoRecordBean> recordList = new ArrayList<ClientInfoRecordBean>();//存放ClientInfoRecordBean 集合
		String nowVal = GlobalsTool.getEnumerationItemsDisplay(enumName,status,getLocale(request).toString());//现在数据
		//批量处理历史记录
		for(String clientId : keyIds){
			rs = dbmgt.detail(moduleBean.getTableInfo().split(":")[0], map, clientId, sunClassCode,props,login.getId(),true,null);//获得详情
			HashMap histroylMap = (HashMap)rs.retVal;
			String ChangeInfo = "生命来源：";//修改记录
			String reVal = "空";
			if(histroylMap.get("Status") != null && !"".equals(histroylMap.get("Status").toString())){
				reVal = GlobalsTool.getEnumerationItemsDisplay(enumName,histroylMap.get("Status").toString(),getLocale(request).toString());//原数据
			}
			ChangeInfo +=reVal +" 改为 " + nowVal;
			//处理修改信息
			ClientInfoRecordBean clientRecordBean = new ClientInfoRecordBean();
			clientRecordBean.setId(IDGenerater.getId());
			clientRecordBean.setClientId(clientId);
			clientRecordBean.setUpdateInfo(ChangeInfo);
			clientRecordBean.setMapInfo(gson.toJson(histroylMap));
			clientRecordBean.setTableInfo(moduleBean.getTableInfo());
			clientRecordBean.setModuleId(moduleBean.getId());
			clientRecordBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			clientRecordBean.setIpAddress(ip);
			clientRecordBean.setEmployeeId(login.getId());
			clientRecordBean.setViewId(viewId);
			recordList.add(clientRecordBean);
		}
		
		Result result = mgt.updateClientStatus(keyIds, status,ispublic,login.getDepartCode(),login,change,recordList) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/*客户移交成功*/
			EchoMessage.success().add(getMessage(request, "oa.bbs.operationOk")).setBackUrl(
					"/CRMClientAction.do?public="+ispublic+"&ModuleId="+moduleId+"&viewId="+viewId).setAlertRequest(request);
		}else{
			if(result.retCode == ErrorCanst.BACK_PUBLIC_CLIENT_TIME){
				EchoMessage.info().add("\""+result.getRetVal()+"\""+getMessage(request, "CRMClientInfo.add.ExistError9")).setBackUrl(
						"/CRMClientAction.do?public="+ispublic).setNotAutoBack().setAlertRequest(request);
			}else if(result.retCode == ErrorCanst.COMPLETE_CLIENT_NO_CHANGE){
				EchoMessage.info().add(getMessage(request, "CRMClientInfo.add.ExistError10")).setBackUrl(
				"/CRMClientAction.do?public="+ispublic).setNotAutoBack().setAlertRequest(request);
			}else if(result.retCode == ErrorCanst.OVER_WILL_CLIENT_QTY){
				EchoMessage.info().add(getMessage(request, "CRMClientInfo.add.ExistError7")).setBackUrl(
							"/CRMClientAction.do?public="+ispublic).setNotAutoBack().setAlertRequest(request);
			}else{
				/*客户移交失败*/
				EchoMessage.error().add(getMessage(request, "common.alert.updateFailure")).setBackUrl(
					"/CRMClientAction.do?public="+ispublic).setAlertRequest(request);
			}
		}
		return mapping.findForward("message");
	}
	
	/**
	 * 客户资料 短信，邮件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward message(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return null ;
	}
	
	/**
	 * 客户资料转交
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward handOver(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String   userId = getParameter("newCreateBy", request) ; /*现跟单人*/
		String  content = getParameter("handContent", request) ;	 /*移交内容*/
		String[] keyIds = getParameters("keyId", request) ;		 /*客户Id*/
		String moduleId = getParameter("ModuleId", request);
		String viewId = getParameter("viewId", request);
		String isDetail = getParameter("isDetail", request);//true是否客户详情页面进入
		String handOver = getMessage(request, "CRMClientInfo.add.remove") ;  /*移交给*/
		
		LoginBean loginBean = getLoginBean(request);
		ArrayList<String[]> filterClientIdsList = mgt.filterClientIdsByEmployeeId(loginBean,keyIds);//过滤我创建的客户IDS
		
		if(filterClientIdsList==null || filterClientIdsList.size()==0){
			/*客户移交成功*/
			EchoMessage.success().add(getMessage(request, "common.msg.clientRemoveSucceed")).setBackUrl(
					"/CRMClientAction.do?firstEnter=false&ModuleId="+moduleId+"&viewId="+viewId).setAlertRequest(request);
		}
		
		HashMap values = new HashMap();	
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		String sqlName = "CRMClientTransfer_AutoReceive";		
		Result result = mgt.clientHandOver(filterClientIdsList, userId, getLoginBean(request),handOver,content,moduleId,allTables,resources, getLocale(request),sqlName) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List<String[]> listAdvice = (List<String[]>) result.getRetVal();
			for (String[] val : listAdvice) {
				if (val.length == 2) {
					new AdviceMgt().deleteByRelationId(val[0], val[1]);
				} else {
					new AdviceMgt().add(val[0], val[1], val[2], val[3], val[4], val[5]);
				}
			}
			if(isDetail!=null && "true".equals(isDetail)){
				request.setAttribute("noback", true);
			}
			/*客户移交成功*/
			EchoMessage.success().add(getMessage(request, "common.msg.clientRemoveSucceed")).setBackUrl(
					"/CRMClientAction.do?firstEnter=false&ModuleId="+moduleId+"&viewId="+viewId).setAlertRequest(request);
		}else{
			/*客户移交失败*/
			if(result.retCode == ErrorCanst.NO_ADD_AREA_CLIENT){
				String[] rst = (String[]) result.getRetVal() ;
				EchoMessage.info().add(rst[0]+getMessage(request, "common.msg.NoRightAdd")+"\""+rst[1]+"\""+getMessage(request, "crm.client.error.client")).setBackUrl(
					"/CRMClientAction.do").setNotAutoBack().setAlertRequest(request);
			}else if(result.retCode == ErrorCanst.OVER_WILL_CLIENT_QTY){
				EchoMessage.info().add(getMessage(request, "common.msg.intentClienAmountGobeyond")+"\""+result.getRetVal()+"\""+getMessage(request, "common.msg.intentClienSetting")).setBackUrl(
					"/CRMClientAction.do").setNotAutoBack().setAlertRequest(request);
			}else{
				EchoMessage.info().add(getMessage(request, "crm.customer.remove.failture")).setBackUrl(
					"/CRMClientAction.do").setAlertRequest(request);
			}
		}
		return mapping.findForward("message");
	}
	
	/**
	 * 删除客户资料
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = getParameters("keyId", request) ;/*客户Id*/
		String moduleId = getParameter("ModuleId",request);
		
		String viewId = getParameter("viewId",request);
		
		ClientModuleBean moduleBean = (ClientModuleBean)moduleMgt.detailCrmModule(moduleId).retVal;
		
		//是否启用了审核流
		boolean flowFlag = false;
		if(moduleBean!=null){
			String tableName = moduleBean.getTableInfo().split(":")[0];
			if(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1){
				flowFlag = true;
			}
		}
		
		
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		LoginBean lg=getLoginBean(request);
		//得到要删除单据的信息
		ArrayList manyValues=new ArrayList();
		for(int i=0;i<keyIds.length;i++){
			Result rso=dbmgt.detail("CRMClientInfo", BaseEnv.tableInfos, keyIds[i], lg.getSunCmpClassCode(), new Hashtable(), getLoginBean(request).getId(), false, "");
			if(rso.retCode==ErrorCanst.DEFAULT_SUCCESS&&rso.retVal!=null){
				manyValues.add(rso.getRetVal());
			}
		}
		
		
		
		
		Result result = mgt.delete(keyIds, allTables, getLoginBean(request).getId(), resources, getLocale(request),flowFlag) ;
		//记录删除日志信息
    	for(int i=0;i<manyValues.size();i++){
    		HashMap valuesOld=(HashMap)manyValues.get(i);
    		int operation=2;  
        	String billTypeName=getModuleNameByLinkAddr(request, mapping);
        	Hashtable map = (Hashtable) request.getSession().getServletContext().
            getAttribute(BaseEnv.TABLE_INFO);
        	DBTableInfoBean tableInfo = (DBTableInfoBean) map.get("CRMClientInfo");
    		dbmgt.addLog(operation, valuesOld, null, tableInfo, this.getLocale(request).toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"",billTypeName);
    	}
    	
		if(result.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			if(result.retCode==ErrorCanst.RET_DEFINE_SQL_ERROR){
				String[] msgCode = (String[])result.getRetVal() ;
				EchoMessage.error().add(getMessage(request, msgCode[0]))
									.setBackUrl("/CRMClientAction.do?operation=4&type=main&ModuleId="+moduleId+"&viewId="+viewId)
                    					.setRequest(request) ;
			}else{
                EchoMessage.error().add(getMessage(request, "common.msg.clientDataDeleteFailure"))
                    			.setBackUrl("/CRMClientAction.do?operation=4&type=main")
                    			.setRequest(request);
			}   
			return getForward(request, mapping, "message") ;
		}
		return query(mapping, form, request, response) ;
	}
	
	/**
	 * 查询客户资料
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		String keyWord = getParameter("keyword", request) ;
		if("@URL:".equals(request.getParameter("LinkType"))){
			try {
				keyWord = new String(keyWord.getBytes("ISO8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		String keyOption = getParameter("keyOption", request) ;
		String createTimeS= getParameter("createTimeS", request) ;
		String createTimeE= getParameter("createTimeE", request) ;
		String moduleId = getParameter("ModuleId", request) ;
		String viewId = getParameter("viewId", request) ;
		String multiple = getParameter("multiple", request) ; //导航单选、多选标识
		String isPublic  = getParameter("public", request) ;//是否只显示公共池客户
		String isFirstSearch  = getParameter("isFirstSearch", request) ;//用于标识是否搜索.false标识有搜索条件
		String sortInfo  = getParameter("sortInfo", request) ;//排序信息 排序标识,排序名
		String firstEnter  = getParameter("firstEnter", request) ;//是否第一次进入false表示否
		HashMap<String,String> conMap = (HashMap) request.getAttribute("conMap") ;
		if(conMap == null){
			conMap = new HashMap<String, String>() ;
		}
		conMap.put("keyword", keyWord) ;
		conMap.put("keyOption", keyOption) ;
		conMap.put("createTimeS", createTimeS);
		conMap.put("createTimeE", createTimeE);
		conMap.put("public", isPublic) ;
		conMap.put("multiple", multiple) ;
		conMap.put("isFirstSearch", isFirstSearch) ;
		conMap.put("sortInfo", sortInfo) ;
		
		/*查询条件 收缩 还是展开*/
		String expandStatus = getParameter("expandStatus", request);
		if(StringUtils.isBlank(expandStatus)){
			expandStatus = "close";
		}
		conMap.put("expandStatus", expandStatus);
		LoginBean login = getLoginBean(request) ;
		
		//根据当前用户获取有权限的当前模板
		Result result = moduleMgt.getFilterModules(login) ;
		request.setAttribute("filterModuleList",result.retVal) ;
		
		//第一次进入取默认模板
		/*
		if(moduleId==null || "".equals(moduleId)){
			moduleId = login.getModuleId();
			if(moduleId==null){
				moduleId = "";
			}
		}
		
		
		
		//检测是否有当前模板,没有设置为空
		result = moduleMgt.detailCrmModule(moduleId);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ClientModuleBean bean = (ClientModuleBean)result.retVal;
			if(bean==null){
				moduleId = "";
			}else{
				if(filterModuleList==null || filterModuleList.size()==0){
					moduleId = "";
				}else{
					String tempModuleId = "";
					for(Object obj :filterModuleList){
						if(bean.getId().equals(String.valueOf(GlobalsTool.get(obj,0))) && !"0".equals(String.valueOf(GlobalsTool.get(obj,4)))){
							tempModuleId = String.valueOf(GlobalsTool.get(obj,0));
							break;
						}
					}
					moduleId = tempModuleId;
				}
			}
		}else{
			moduleId = "";
		}
		
		*/
		
		//第一次进入取默认模板
		if(moduleId==null ){
			moduleId = "";
		}
		
		//检测是否有当前模板,没有设置为空
		result = moduleMgt.detailCrmModule(moduleId);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ClientModuleBean bean = (ClientModuleBean)result.retVal;
			if(bean==null){
				moduleId = "";
			}else{
				moduleId = bean.getId();
			}
		}else{
			moduleId = "";
		}
		
		
		//获取模板ID、视图ID信息
		String[] moduleArrInfo = this.getModuleInfo(moduleId, viewId, null, login);
		String moduleCheckInfo = this.checkModuleInfo(moduleArrInfo);//检测是否存在模板、视图、客户、没有返回错误
		if("".equals(moduleCheckInfo)){
			moduleId = moduleArrInfo[0];
			viewId = moduleArrInfo[1];
		}else{
			EchoMessage.error().add(moduleCheckInfo).setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		conMap.put("moduleId", moduleId);
		conMap.put("viewId", viewId);
		
		//存放视图list
		result = moduleMgt.queryModuleViewsByModuleId(login,moduleId);
		List<ClientModuleViewBean> moduleViewList = (List<ClientModuleViewBean>)result.retVal;
		request.setAttribute("moduleViewList", moduleViewList) ;
		
		
		ClientModuleBean moduleBean = (ClientModuleBean)moduleMgt.detailCrmModule(moduleId).retVal;
		String tableName = moduleBean.getTableInfo().split(":")[0];//主表名
		String contactTableName = moduleBean.getTableInfo().split(":")[1];//联系人表名
		
		request.setAttribute("moduleBean", moduleBean) ;
		request.setAttribute("clientTableName", tableName);
		request.setAttribute("contactTableName", contactTableName);
		
		result = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleViewBean = (ClientModuleViewBean)result.retVal;
		request.setAttribute("moduleViewBean", moduleViewBean);
		
		
		String userId = getLoginBean(request).getId() ;
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName=CRMClientInfo");
		request.setAttribute("MOID", mop.moduleId);
		StringBuilder condition = new StringBuilder();
		
		//单据是否启用了审核流
		boolean flowOpen = false;
		String flowCondition = "";//审核条件
		if(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1){
			flowOpen = true;
			//用于查单单据权限,审核条件workFlow=-1 或=0
			//flowCondition = " and (CRMClientInfo.workFlowNode='-1' or CRMClientInfo.workFlowNode='0')";
			//flowCondition = " and (CRMClientInfo.workFlowNode!='0')";
			flowCondition = " and ( 1=1 )";
		}
		
		ArrayList scopeRight = this.getAllMopByType(mop, login, MOperation.M_QUERY);
		String allDeptScopeStr = checkExistAllDeptScope(scopeRight);
		/*设置范围权限*/
		if(!"1".equals(userId) && !"public".equals(isPublic) && !"ALL".equals(allDeptScopeStr)){
			/*查看某字段值单据*/
			String fieldValueSQL = "" ;
			
			condition.append(" and ");
			
			String strUserIds = "'"+userId+"'," ;
			String strDeptIds = "" ;
			if(mop!=null){				
				if (scopeRight != null && scopeRight.size()>0) {
					for (Object o : scopeRight) {
						
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strUserIds += "'"+strId+"'," ;
							}
						}else if(lsb!=null && "5".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";") ){
								if(strDeptIds.indexOf(strId+"%'") ==-1){
									strDeptIds += "or departmentCode like '" +strId + "%' ";
								}
							}
						}
					}
					
				}
			}
			strUserIds = strUserIds.substring(0,strUserIds.length()  -1);
			
			condition.append(" (CRMClientInfo.id in (select f_ref from CRMClientInfoEmp where EmployeeId in ("+strUserIds+") "+strDeptIds+" )  )");
		}
		
		condition.append(" and moduleId='").append(moduleId).append("' ") ;
		
		if(createTimeS!=null&&createTimeS.length()>0){
			condition.append(" and SUBSTRING(createTime,1,10)>='").append(createTimeS).append("' ") ;
		}
		if(createTimeE!=null&&createTimeE.length()>0){
			condition.append(" and SUBSTRING(createTime,1,10)<='").append(createTimeE).append("' ") ;
		}
		
		//判断是否公共池客户
		if("public".equals(isPublic)){
			condition.append(" and status = '1' ") ;
		}else{
			condition.append(" and status != '1' ") ;
		}
		
		//电信录单员权限(客户)
		MOperation recordMOP = (MOperation) login.getOperationMap().get("/TelecomRecordAction.do");
		if(!"1".equals(login.getId()) && recordMOP!=null && recordMOP.query()){
			condition.append(" and telecomRecordId = '").append(login.getId()).append("' ") ;
		}
		
		//后付费管理，只查看电信录单员审核情况(客户)
		String isPostpay = getParameter("isPostpay",request);
		if(isPostpay!=null && "true".equals(isPostpay)){
			condition.append(" and fkfs='1' and WorkFlowStatus = 'finish'") ;
			conMap.put("isPostpay",isPostpay);
		}
		
		/*获取客户的导入，导出，打印的权限 */
		HashMap<String,Boolean> scopeMap = mgt.getCRmScope(login, viewId);
		request.setAttribute("scopeMap", scopeMap);
		
		
		
		String keySql = "";
		if(StringUtils.isNotBlank(keyWord)  &&  "all".equals(keyOption)){
			//keySql = " join CRMClientInfoIndex with(index(IX_CRMClientInfoIndex_Content)) on CRMClientInfo.id=CRMClientInfoIndex.index_id and module_Id='"+moduleId+"' and status_id "+("public".equals(isPublic)?"='1'":"!='1' where index_content like '%"+keyWord+"%'");
		}
		String keyIds = "";
		//if(indexFile.exists()) {
		//	keyIds = queryIndex(keyWord, moduleBean);
		//}else{
		//	createIndex(moduleBean);
		//}
		
		//获取查询条件
		if(moduleViewBean.getSearchFields()!=null && !"".equals(moduleViewBean.getSearchFields())){
			for(String field : moduleViewBean.getSearchFields().split(",")){
				DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(tableName, field);
				
				if(fieldBean.getInputType()==1){
					request.setAttribute(field+"_mul", request.getParameter(field+"_mul"));//是否多选标志
					String values = getParameter(fieldBean.getFieldName(), request) ;//获取值
					if(StringUtils.isNotBlank(values)){
						String conValue = "" ;
						if("LastContractTime".equals(fieldBean.getFieldName())){
							//最近联系天数
							Calendar calendar = Calendar.getInstance() ;
							calendar.add(Calendar.DAY_OF_YEAR, -Integer.parseInt(values.replace("more", "").replace(",", ""))+1) ;
							condition.append(" and ").append(fieldBean.getFieldName()).append(" < ").append(" '"+BaseDateFormat.format(calendar.getTime(), BaseDateFormat.yyyyMMdd)+"'");
						}else{
							condition.append(" and ").append(fieldBean.getFieldName()).append(" in(") ;
							for(String value : values.split(",")){
								conValue += "'"+value + "'," ;
							}
							if(conValue.endsWith(",")){
								conValue = conValue.substring(0,conValue.length()-1) ;
							}
							condition.append(conValue).append(") ") ;
						}
						conMap.put(fieldBean.getFieldName(), values) ;
					}else{
						/*默认不启用停用客户*/
						if("statusId".equals(fieldBean.getFieldName()) && values==null){
							condition.append(" and statusId=0 ");
							conMap.put("statusId", "0,");
						}
					}
				}else{
					//处理时间类型
					String timeStart = request.getParameter(field+"Start");
					String timeEnd = request.getParameter(field+"End");
					
					
					if(timeStart != null && !"".equals(timeStart)){
						condition.append(" and CRMClientInfo.").append(field).append(" >= '").append(timeStart).append("'");
						conMap.put(field+"Start", timeStart);
					}
					if(timeEnd != null && !"".equals(timeEnd)){
						condition.append(" and CRMClientInfo.").append(field).append(" <= '").append(timeEnd +" 23:59:999").append("'");
						request.setAttribute(field+"End",timeEnd);
						conMap.put(field+"End", timeEnd);
					}
				}
				
			}
		}
		
		//获取模糊查询字段
		String keyFields = moduleViewBean.getKeyFields();
		LinkedHashMap keyMaps = new LinkedHashMap();
		if(keyFields !=null && !"".equals(keyFields)){
			for(String s : moduleViewBean.getKeyFields().split(",")){
				DBFieldInfoBean keyField = GlobalsTool.getField(s,moduleBean.getTableInfo().split(":")[0],moduleBean.getTableInfo().split(":")[1]);
				if(keyField !=null){
					keyMaps.put(s,keyField.getDisplay().get(this.getLocale(request).toString()));
				}
			}
		}
		request.setAttribute("keyMaps", keyMaps);
		

		//分页信息
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	//pageSize = Integer.parseInt(moduleBean.getDefPageSize());
        	//若moduleBean没有默认页面就给30
        	pageSize = 30;
        }
        if(StringUtils.isNotBlank(keyWord) && pageNo<=1){
        	pageNo = 1;
        }
        
        //获取列表字段
		String fields = moduleViewBean.getListFields() ;
		if(StringUtils.isNotBlank(moduleViewBean.getDetailFields())){
			for(String str : moduleViewBean.getDetailFields().split(",")){
				if(str.startsWith("contact") || fields.contains(str+",")) continue ;
				fields += str +"," ;
			}
		}
		if(fields.indexOf("LastContractTime,")==-1){
			fields += "LastContractTime," ;
		}
		
		/*
		//客户标签查找,暂时不启用
		HashMap labelMap = new HashMap();
		String labelQueryIds = getParameter("labelQueryIds", request);
		Result rest = mgt.findCRMLabels(login.getId(),moduleBean.getId());
		List<CRMLabelBean> labelList = (List<CRMLabelBean>)rest.retVal;
		if(labelList!=null && labelList.size() !=0){
			for(CRMLabelBean bean : labelList){
				labelMap.put(bean.getId(), bean);
			}
			request.setAttribute("labelMap", labelMap);
			request.setAttribute("labelList", labelList);
		}else{
			labelQueryIds = "";
		}
		
		if(labelQueryIds !=null && !"".equals(labelQueryIds)){
			String newLabelIds = "";
			for(String str : labelQueryIds.split(",")){
				if(labelMap.get(str)!=null){
					newLabelIds += str +",";
				}
			}
			if(!"".equals(newLabelIds)){
				condition.append("and labelId is not null and labelId !='' and ");
				String labelIds = "";
				for(String labelId : newLabelIds.split(",")){
					labelIds += " ','+labelId like '%," +labelId +",%' or";
				}
				if(labelIds.endsWith("or")){
					labelIds = labelIds.substring(0,labelIds.length()-3);
				}
				condition.append(labelIds);
				request.setAttribute("labelQueryIds",newLabelIds);
			}
		}
		
		*/
		
		//获取首页查询条件信息
		String searchName = getParameter("searchName", request) ;
		String trade = getParameter("trade", request) ;//行业
		String district = getParameter("district", request) ;//地区
		String employee = getParameter("employee", request) ;//人员
		String departMent = getParameter("departMent", request) ;//部门
		
		if(trade != null && !"".equals(trade) && !",".equals(trade)) {
			HashMap tradeMap = (HashMap)request.getSession().getServletContext().getAttribute("workProfessionMap");
			LinkedHashMap showTradeMap = new LinkedHashMap();
			String str ="";
			for(int i=0;i<trade.split(",").length;i++){
				if(!"".equals(trade.split(",")[i])){
					showTradeMap.put(trade.split(",")[i].replaceAll("'", ""), tradeMap.get(trade.split(",")[i].replaceAll("'", "")));
					str += "'" + trade.split(",")[i] +"',";
				}
			}
			if(str.endsWith(",")){
				str = str.substring(0,str.length()-1);
			}
			condition.append(" and Trade in (").append(str).append(")");
			request.setAttribute("showTradeMap", showTradeMap);
			request.setAttribute("trade", trade);
		}
		
		if(district != null && !"".equals(district) && !",".equals(district)){
			HashMap districtMap = (HashMap)request.getSession().getServletContext().getAttribute("districtMap");
			LinkedHashMap showDistrictMap = new LinkedHashMap();
			String distName = "";
			condition.append(" and (");
			for(int i=0;i<district.split(",").length;i++){
				if(!"".equals(district.split(",")[i])){
					
					condition.append(" District like '").append(district.split(",")[i]).append("%' or"); 
					
					distName = (String)districtMap.get(district.split(",")[i]);
					if(district.split(",")[i].length() == 15){
						distName = distName.substring(distName.indexOf("-")+1, distName.length());
					}else if(district.split(",")[i].length() == 20){
						distName = distName.substring(distName.indexOf("-")+1, distName.length());
						distName = distName.substring(distName.indexOf("-")+1, distName.length());
					}
					showDistrictMap.put(district.split(",")[i],distName);
				}
			}
			
			if(condition.toString().endsWith(" or")){
				condition.delete(condition.length()-3,condition.length()) ;
			}
			condition.append(" )");
			request.setAttribute("showDistrictMap", showDistrictMap);
			request.setAttribute("district", district);
			
		}
		if(employee != null && !"".equals(employee) && !",".equals(employee)){
			String employeeIds = "";
			for(String employeeId : employee.split(",")){
				if(!"".equals(employeeId)){
					employeeIds += "'" + employeeId + "',";
				}
			}
			if(employeeIds.endsWith(",")){
				employeeIds = employeeIds.substring(0,employeeIds.length()-1);
			}
			if(fields.indexOf("ywy")>0){
				condition.append(" and CRMClientInfo.ywy in (").append(employeeIds).append(")");
			}else{
				condition.append(" and CRMClientInfo.createBy in (").append(employeeIds).append(")");
			}
			request.setAttribute("employee", employee);
			
			
		}
		if(departMent != null && !"".equals(departMent) && !",".equals(departMent)){
			String deptIds = "";
			for(String deptId : departMent.split(",")){
				if(!"".equals(deptId)){
					deptIds += "'" + deptId + "',";
				}
			}
			if(deptIds.endsWith(",")){
				deptIds = deptIds.substring(0,deptIds.length()-1);
			}
			if(fields.indexOf("ywy")>0){
				condition.append(" and CRMClientInfo.fgs in (").append(deptIds).append(")");
			}else{
				condition.append(" and CRMClientInfo.createBy in (select id from tblEmployee where departmentCode in (").append(deptIds).append("))");
			}
			request.setAttribute("departMent",departMent);
		}
		
		//审核状态
		String approveStatus = getParameter("approveStatus",request);
		if(flowOpen&&!"true".equals(isPostpay) &&(approveStatus== null ||"".equals(approveStatus)))approveStatus="all";
		if(approveStatus != null && !"".equals(approveStatus) && !"all".equals(approveStatus)){
			if("0".equals(approveStatus)){//当前用户的待审核记录
				condition.append(" and CRMClientInfo.checkPersons like '%;"+login.getId()+";%' and CRMClientInfo.workFlowNode <> '-1'");
				if(sortInfo == null || "".equals(sortInfo)){
					sortInfo="ASC,(select lastUpdateTime from OAMyWorkFlow where keyId=CRMClientInfo.id)";
				}
			}else if("approveing".equals(approveStatus)){//审核中记录
				condition.append(" and CRMClientInfo.workFlowNode not in ('0','-1') and CRMClientInfo.checkPersons not like '%;"+login.getId()+";%'");
			}else if("back".equals(approveStatus)){
				//退回(审核失败)
				condition.append(" and CRMClientInfo.workFlowNode <> '-1' and WorkFlowStatus ='back' ");
			}else{
				condition.append(" and CRMClientInfo.workFlowNode ='").append(approveStatus).append("' ");
			}
			conMap.put("approveStatus",approveStatus) ;
		}
		
		//电信录单审核查看
		String telecomRecordVal = getParameter("telecomRecordVal",request);
		if(telecomRecordVal != null && !"".equals(telecomRecordVal) && !"all".equals(telecomRecordVal)){
			if("empty".equals(telecomRecordVal)){
				condition.append(" and isNull(CRMClientInfo.telecomRecordId,'') = ''");
			}else if("approveing".equals(telecomRecordVal)){
				condition.append(" and CRMClientInfo.workFlowNode <> '-1' and isNull(CRMClientInfo.telecomRecordId,'') <> ''");
			}else{
				condition.append(" and CRMClientInfo.workFlowNode = '-1' and isNull(CRMClientInfo.telecomRecordId,'') <> ''");
			}
			conMap.put("telecomRecordVal",telecomRecordVal) ;
		}
		
		//发货状态(富胜通)
		String deliverStatus = getParameter("deliverStatus",request);
		if(deliverStatus != null && !"".equals(deliverStatus) && !"all".equals(deliverStatus)){
			condition.append(" and CRMClientInfo.fhzt ='").append(deliverStatus).append("' ");
			conMap.put("deliverStatus",deliverStatus) ;
		}
		
		/*查询我的关注客户*/
		String attention = getParameter("myAttention", request);
		if("ok".equals(attention)){
			condition = new StringBuilder();
			condition.append(" and id in (select oTopicId from tblAttention where type='CRMClientInfo' and empId='").append(login.getId()).append("')").append(" and moduleId='").append(moduleId).append("'") ;
			request.setAttribute("attention", "ok");
		}
		long start1 = System.currentTimeMillis();
		
		result = mgt.queryClient(condition.toString(),fields, keyIds, pageSize, pageNo, keyWord,
				moduleViewBean.getKeyFields(),keyOption,sortInfo,keySql) ;
		long start2 = System.currentTimeMillis();
		BaseEnv.log.debug("query client cast time:"+(start2-start1));
		request.setAttribute("searchName", searchName ==null ? "" : searchName);
		
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			this.getWorkFlowInfo(tableName, request);
			List clientList = (List) result.retVal ;
			request.setAttribute("clientList", clientList) ;
			request.setAttribute("pageBar",pageBar2(result, request)) ;
			//查询联系人
			long contactTime = System.currentTimeMillis() ;
			if(clientList!=null && clientList.size()>0){
				request.setAttribute("contactMap", mgt.queryContacts(clientList, moduleViewBean.getDetailFields()).retVal) ;
				request.setAttribute("attentMap", new AttentionMgt().attentionMap(login.getId(), clientList, "CRMClientInfo").retVal) ;
			}
			BaseEnv.log.debug("contactMap cast time : " + (System.currentTimeMillis() - contactTime));
		}
		
		//获取所有职员信息,用于textBox控件
		ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
		request.setAttribute("textBoxValues",gson.toJson(list));
		
		request.setAttribute("conMap", conMap) ;
		BaseEnv.log.debug("all cast time : " + (System.currentTimeMillis() - start));
		request.setAttribute("updateFlag", request.getParameter("updateFlag"));
		request.setAttribute("updateKeyId", request.getParameter("updateKeyId"));
		
		
		//后付费管理，跳转
		if(isPostpay!=null && "true".equals(isPostpay)){
			return getForward(request, mapping, "postpayIndex") ;
		}else{
			return getForward(request, mapping, "index") ;
		}
	}
	
	/**
	 * 根据索引查询
	 * @param keyWord
	 * @return
	 */
	public String queryIndex(String keyWord,ClientModuleBean moduleBean,ClientModuleViewBean moduelViewBean){
		if(keyWord==null || keyWord.trim().length()==0) return "";
		long start = System.currentTimeMillis();
		StringBuilder keyIds = new StringBuilder();
		try {
			IndexSearcher search = new IndexSearcher(FSDirectory.open(new File("../index/"+moduleBean.getId())));
			search.setSimilarity(new IKSimilarity());
			StringBuilder queryFields = new StringBuilder();
			if(moduelViewBean.getKeyFields()!=null){
				String[] keyFields = moduelViewBean.getKeyFields().split(",");
				for(String key : keyFields){
					if(key.startsWith("contact")){
						queryFields.append(key.substring(7, key.length())+":'"+keyWord+"' || ");
					}else{
						queryFields.append(key+":'"+keyWord+"' || ");
					}
				}
			}
			if(queryFields.toString().endsWith("|| ")){
				queryFields.delete(queryFields.length()-3, queryFields.length());
			}
			//queryFields id='1231' && (title:'文档标题'|| content:'内容') – author='helloworld' 
			// =表示精确匹配 && 表示多个条件并且关系 ||表示多个条件或者关系 -表示非的关系
			Query query = IKQueryParser.parse(queryFields.toString());
			TopDocs docs = search.search(query, search.maxDoc());
			BaseEnv.log.debug("query index length:"+docs.scoreDocs.length);
			for (int i = 0; i < docs.scoreDocs.length; i++) {
				int docId = docs.scoreDocs[i].doc;
				Document doc = search.doc(docId);
				keyIds.append("'").append(doc.getField("id").stringValue()).append("',");
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(keyIds.toString().endsWith(",")){
			keyIds = keyIds.deleteCharAt(keyIds.length()-1);
		}
		long end = System.currentTimeMillis();
		BaseEnv.log.debug("query client index cast time:"+(end-start));
		return keyIds.toString();
	}
	
	/**
	 * 根据相应的模板创建相应的索引
	 * @param module
	 */
	public void createIndex(ClientModuleBean module){
		
		File indexFile = new File("../index/"+module.getId());
		if(!indexFile.exists()) {
			indexFile.mkdir();
		}
		try {
			Directory directory = FSDirectory.open(indexFile);
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_36,analyzer);
			
			LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
			mergePolicy.setMergeFactor(50);
			mergePolicy.setMaxMergeDocs(5000);
			mergePolicy.setUseCompoundFile(true);
			
			indexConfig.setMergePolicy(mergePolicy);
			indexConfig.setOpenMode(OpenMode.CREATE);
			
			IndexWriter indexWriter = new IndexWriter(directory,indexConfig);
			mgt.queryModuleData(module,indexWriter);
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据相应的模板创建相应的索引
	 * @param module
	 */
	public void updateIndex(ClientModuleBean module,HashMap values){
		File indexFile = new File("../index/"+module.getId());
		if(!indexFile.exists()) {
			indexFile.mkdir();
		}
		try {
			Directory directory = FSDirectory.open(indexFile);
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_36,analyzer);
			
			LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
			mergePolicy.setMergeFactor(50);
			mergePolicy.setMaxMergeDocs(5000);
			mergePolicy.setUseCompoundFile(true);
			
			indexConfig.setMergePolicy(mergePolicy);
			indexConfig.setOpenMode(OpenMode.CREATE);
			
			IndexWriter indexWriter = new IndexWriter(directory,indexConfig);
			//先删除 后插入
			indexWriter.deleteDocuments(new Term("id",String.valueOf(values.get("id"))));
			Document doc = new Document();
			///此处添加相应字段的索引
			
			
			indexWriter.addDocument(doc);
			indexWriter.close();
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据相应的模板删除相应的索引
	 * @param module
	 */
	public void deleteIndex(ClientModuleBean module,String keyId){
		File indexFile = new File("../index/"+module.getId());
		if(!indexFile.exists()) {
			indexFile.mkdir();
		}
		try {
			Directory directory = FSDirectory.open(indexFile);
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_36,analyzer);
			
			LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
			mergePolicy.setMergeFactor(50);
			mergePolicy.setMaxMergeDocs(5000);
			mergePolicy.setUseCompoundFile(true);
			
			indexConfig.setMergePolicy(mergePolicy);
			indexConfig.setOpenMode(OpenMode.CREATE);
			
			IndexWriter indexWriter = new IndexWriter(directory,indexConfig);
			
			indexWriter.deleteDocuments(new Term("id",keyId));
			indexWriter.close();
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加前的准备
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//获得CRMClientInfo与CRMClientInfoDet表结构信息
		LoginBean loginBean = this.getLoginBean(request);
		String moduleId = request.getParameter("moduleId");//模板ID
		String viewId = request.getParameter("viewId");//模板ID
		String nowHead = request.getParameter("nowHead");//选择那个头部
		String email = request.getParameter("email");//email
		moduleId = moduleId ==null ? "1" : moduleId;
		Result results = moduleMgt.detailCrmModule(moduleId) ;
		ClientModuleBean module = (ClientModuleBean) results.retVal;
		viewId = viewId ==null ? "1_1" : viewId;
		results = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleView = (ClientModuleViewBean) results.retVal;
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		
		
		String tableName = module.getTableInfo().split(":")[0];//主表
		String contactTableName = module.getTableInfo().split(":")[1];//明细表
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		
		//审核流处理字段
		HashMap<String,String> workFlowFieldMap = this.getWorkFlowFieldsInfo(tableName, null,loginBean);
		
		//获取隐藏字段信息
		List<FieldScopeSetBean> hideFieldScopeList = (List<FieldScopeSetBean>)mgt.findFieidScopes(loginBean.getDepartCode(), loginBean.getId(), "hidden",viewId).retVal;
		
		//获取过滤隐藏字段信息
		Result result = mgt.queryHideFields(tableName, true, moduleView, hideFieldScopeList,workFlowFieldMap,contactTableName);
		LinkedHashMap<String,List<String>> mainMap = (LinkedHashMap<String,List<String>>)result.retVal;	
		
		result = mgt.queryHideFields(contactTableName, false, moduleView, hideFieldScopeList,workFlowFieldMap,contactTableName);
		LinkedHashMap<String,List<String>> contactMap = (LinkedHashMap<String,List<String>>)result.retVal;	
		
		//时候显示附件
		boolean showAttachment = this.showAttachment(hideFieldScopeList, moduleView);
		
		
		//审核流信息
		this.getWorkFlowInfo(tableName, request);
		
		this.getOnlyReadFields(loginBean, viewId, request,null,null);
		//request.setAttribute("ClientNo", BillNoManager.find("CRMClientInfo_ClientNo",loginBean));
		request.setAttribute("clientMap", mainMap);
		request.setAttribute("moduleId", moduleId);
		request.setAttribute("contactMap", contactMap);
		request.setAttribute("nowHead", nowHead);
		request.setAttribute("email", email);
		request.setAttribute("tableName",tableName);
		request.setAttribute("contactTableName",contactTableName);
		request.setAttribute("showAttachment",showAttachment);
		request.setAttribute("moduleViewBean",moduleView);
		request.setAttribute("loginBean",loginBean);		
		return getForward(request, mapping, "addPrepare");
	}
	
	/**
	 * 添加客户资料与联系人信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//处理客户信息
		String tableName = request.getParameter("tableName");//获得客户表
		String contactTableName = request.getParameter("contactTableName");//联系人表
		String[] uploadaffix = request.getParameterValues("uploadaffix");//上传的附件名称
		String isAttachment = request.getParameter("isAttachment");
		String moduleId = request.getParameter("ModuleId");//模板ID
		String designId = request.getParameter("designId");//审核流流程ID
		String approveBefore = getParameter("approveBefore",request);//用于添加页面点击转交标志,true表示是
		
		Result results = moduleMgt.detailCrmModule(moduleId) ;
		ClientModuleBean moduleBean = (ClientModuleBean) results.retVal;
		String deliverTo = request.getParameter("deliverTo") == null? "" : request.getParameter("deliverTo");//获得客户表
		LoginBean loginBean = this.getLoginBean(request);
		HashMap values = new HashMap() ;
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[0]) ;
		String mainUniFields = this.getUniqueFIelds(tableInfo);//获取主表唯一值字段
		
		readMainTable(values, tableInfo, request) ;
		if(isAttachment != null && "true".equals(isAttachment)){
			if(uploadaffix!=null && uploadaffix.length != 0){
				String uploadVals = "";
				for(int j=0;j<uploadaffix.length;j++){
					uploadVals += uploadaffix[j] + ";";
				}
				values.put("Attachment", uploadVals.substring(0, uploadVals.length()-1));
			}
		}
		
		
		String childUniFields = "";//获取从表唯一值字段
		//处理明细问题
		Hashtable alltables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(moduleBean.getTableInfo().split(":")[0], alltables);
		for(DBTableInfoBean tableBean : childTableList){
			BaseEnv.log.debug(tableBean.getTableName());
			if(!"CRMClientInfoEmp".equals(tableBean.getTableName())){
				childUniFields = this.getUniqueFIelds(tableBean);
				readChildTable(values, tableBean, request, "00001");
			}
		}
		//tableInfo = GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[1]) ;
		//String childUniFields = this.getUniqueFIelds(tableInfo);//获取从表唯一值字段
		//readChildTable(values, tableInfo, request, "00001");
		
		/*保存在内存中所有表结构的信息*/
        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		values.put("createBy", loginBean.getId());
		values.put("owner", loginBean.getId());
		values.put("departmentCode", loginBean.getDepartCode());
		values.put("lastUpdateBy", loginBean.getId()); 
		values.put("createTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		values.put("SCompanyID", "00001");
		values.put("noRelationDay", ((DBFieldInfoBean)(GlobalsTool.getField("noRelationDay",moduleBean.getTableInfo().split(":")[0],moduleBean.getTableInfo().split(":")[1]))).getDefaultValue());
		values.put("TransferDate", ((DBFieldInfoBean)(GlobalsTool.getField("TransferDate",moduleBean.getTableInfo().split(":")[0],moduleBean.getTableInfo().split(":")[1]))).getDefaultValue());
		values.put("Transferer", ((DBFieldInfoBean)(GlobalsTool.getField("Transferer",moduleBean.getTableInfo().split(":")[0],moduleBean.getTableInfo().split(":")[1]))).getDefaultValue());
		
		//审核
		if(designId !=null && !"".equals(designId)){
			values.put("workFlowNode","0");
			values.put("workFlowNodeName","notApprove");
			values.put("checkPersons",";"+loginBean.getId()+";");
		}else{
			values.put("workFlowNode","-1");
			values.put("workFlowNodeName","finish");
		}
		
		DBFieldInfoBean noRelationDay = GlobalsTool.getField("noRelationDay",moduleBean.getTableInfo().split(":")[0],moduleBean.getTableInfo().split(":")[1]);
		values.put("finishTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		
		
//		values.put("crmTableName", request.getParameter("mainTableName") == null ? "CRMClientInfo" : request.getParameter("mainTableName"));
		//values.put("crmTableName", arg1)
		  //获取路径
        String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
        //要执行的define的信息
        String defineInfo=request.getParameter("defineInfo");
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;
		String locale = GlobalsTool.getLocale(request).toString();
		//Result results = moduleMgt.detailCrmModule(moduleId) ;
		//ClientModuleBean module = (ClientModuleBean) results.retVal;
//		values.put("mainTableName", "true");
//		List<HashMap> childList = (List<HashMap>)values.get("TABLENAME_CRMClientInfoDet");
//		for(HashMap map : childList){
//			map.put("childTableName", "true");
//		}
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(moduleBean.getTableInfo().split(":")[0]) ;
		if(workFlow!=null){
			WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(workFlow.getId());
			if(designBean==null){
				workFlow = null;
			}
		}
		
		//检查字段是否启用了关键字设置
		String keyWordInfo= this.checkModuleKeyWork(moduleBean, values,null);
		if(!"".equals(keyWordInfo)){
			 EchoMessage.error().add(keyWordInfo).setAlertRequest(request);
			 return getForward(request, mapping, "alert");
		}
		
		//检查字段是否有存在重复值
		String uniqueInfo= this.checkUniqueFields(mainUniFields, childUniFields, moduleBean.getTableInfo(), values, locale.toString(),"add",moduleBean);
		if(!"".equals(uniqueInfo)){
			 EchoMessage.error().add(uniqueInfo).setAlertRequest(request);
			 return getForward(request, mapping, "alert");
		}
		
		Result rs = dbmgt.add(moduleBean.getTableInfo().split(":")[0], alltables, values, loginBean.getId(), path, defineInfo, resources, this.getLocale(request), "", loginBean, workFlow, deliverTo, null);
		

		/*添加系统日志*/
    	int operation=0;
    	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		String billTypeName=getModuleNameByLinkAddr(request, mapping);
    		dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), loginBean.getId(), loginBean.getEmpFullName(), loginBean.getSunCmpClassCode(),"",billTypeName);
    	}
		
		//Result rs = mgt.addClientInfo(alltables,values,loginBean,resources,getLocale(request));
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if(uploadaffix!=null && uploadaffix.length>0){
				for(int j=0;j<uploadaffix.length;j++){
					FileHandler.copy("CRMClientInfo", FileHandler.TYPE_AFFIX, uploadaffix[j], uploadaffix[j]);
					FileHandler.deleteTemp(uploadaffix[j]);
				}
			}
			
			//点击转交还是添加按钮
			if(approveBefore!=null && "true".equals(approveBefore)){
				//转交按钮跳转到index.jsp 的dealCheck(keyId)  处理
				request.setAttribute("noAlert", "true");
				request.setAttribute("checkFlag", "true");
				request.setAttribute("checkBackUrl", values.get("id"));
				request.setAttribute("dealAsyn", "true");
			}else{
				request.setAttribute("crmFreash", "true");
			}
			
			
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/CRMClientAction.do?operation=6&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&moduleId=" + moduleId).setAlertRequest(request);
		}else {
       	 	//添加失败
        	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
        }
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 修改客户资料与联系人信息准备前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LoginBean loginBean = (LoginBean)request.getSession().getAttribute("LoginBean");
		String moduleId = request.getParameter("moduleId");
		String viewId = request.getParameter("viewId");
		
		//获取模板ID、视图ID信息
		String[] moduleArrInfo = this.getModuleInfo(moduleId, viewId, null, loginBean);
		String moduleCheckInfo = this.checkModuleInfo(moduleArrInfo);//检测是否存在模板、视图、客户、没有返回错误
		if("".equals(moduleCheckInfo)){
			moduleId = moduleArrInfo[0];
			viewId = moduleArrInfo[1];
		}else{
			EchoMessage.error().add(moduleCheckInfo).setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		
		Result rs = moduleMgt.detailCrmModule(moduleId);
		ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
		rs = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleViewBean = (ClientModuleViewBean) rs.retVal;
        Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
       
        String keyId = request.getParameter("keyId");
        ActionForward forward = null;
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = loginBean.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;
		long long1 = System.currentTimeMillis();
		rs = dbmgt.detail(moduleBean.getTableInfo().split(":")[0], map, keyId, sunClassCode,props,loginBean.getId(),true,null);
		HashMap sortMap = this.getClientDetSort((HashMap)rs.retVal,moduleBean.getTableInfo());
		
		
		long long2 = System.currentTimeMillis();
		BaseEnv.log.debug("加载详情加载详情加载详情加载详情 time:"+(long2-long1));
		HashMap rsMap = (HashMap)rs.retVal;
		List affixList = new ArrayList();
		String attachment = (String)rsMap.get("Attachment");
		if(attachment!=null && !"".equals(attachment)){
			
			for(int i=0;i<((String)rsMap.get("Attachment")).split(";").length;i++){
				affixList.add(((String)rsMap.get("Attachment")).split(";")[i]);
			}
		}
		
		String tableName = moduleBean.getTableInfo().split(":")[0];//主表
		String contactTableName = moduleBean.getTableInfo().split(":")[1];//联系人表
		
		//审核流处理字段
		HashMap<String,String> workFlowFieldMap = this.getWorkFlowFieldsInfo(tableName, keyId,loginBean);
		
		//获取隐藏字段信息
		List<FieldScopeSetBean> hideFieldScopeList = (List<FieldScopeSetBean>)mgt.findFieidScopes(loginBean.getDepartCode(), loginBean.getId(), "hidden",viewId).retVal;
		
		//获取过滤隐藏字段信息
		Result result = mgt.queryHideFields(tableName, true, moduleViewBean,hideFieldScopeList,workFlowFieldMap,contactTableName);
		LinkedHashMap<String,List<String>> mainMap = (LinkedHashMap<String,List<String>>)result.retVal;	
		
		result = mgt.queryHideFields(contactTableName, false, moduleViewBean, hideFieldScopeList,workFlowFieldMap,contactTableName);
		LinkedHashMap<String,List<String>> contactMap = (LinkedHashMap<String,List<String>>)result.retVal;	
		
//		String isConDel = "false";//true表示有隐藏或字段字段,用于修改页面控制联系人是否能删除
//		Result results = moduleMgt.detailCrmModule(moduleId) ;
//		ClientModuleBean module = (ClientModuleBean) results.retVal;
//		String[] arrStr = this.getScreenFields(loginBean, moduleViewBean.getPageFields(),isConDel,viewId);//筛选隐藏的字段
//		LinkedHashMap clientMap = getValueMap(allTables, loginBean, module.getTableInfo().split(":")[0], true, module, true, arrStr[0], request,arrStr[1],viewId);
//		LinkedHashMap contactMap = getValueMap(allTables, loginBean, module.getTableInfo().split(":")[1], false, module, false, arrStr[0], request,arrStr[1],viewId);
		
		
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		HashMap<String,String> defineDisMap = this.getDefineDisplay(tableInfoBean, rsMap, loginBean);//获取自定义弹出框展示名称
		
		boolean showAttachment = this.showAttachment(hideFieldScopeList, moduleViewBean);
		
		//审核流信息
		this.getWorkFlowInfo(tableName, request);
		
		//获取只读信息
		this.getOnlyReadFields(loginBean, viewId, request,workFlowFieldMap,contactTableName);
		
		//判断是否有撤回权限
		checkCancelFlow(tableName, rsMap, loginBean, request);
		
		request.setAttribute("showAttachment",showAttachment);
		request.setAttribute("defineDisMap", defineDisMap);
		request.setAttribute("AFFIX", affixList);
		request.setAttribute("tableName", tableName);
		request.setAttribute("contactTableName", contactTableName);
		request.setAttribute("result", this.getClientDetSort(rsMap, moduleBean.getTableInfo()));
		request.setAttribute("clientMap", mainMap);
		request.setAttribute("contactMap", contactMap);
		request.setAttribute("keyId", keyId);
		//request.setAttribute("moduleId", moduleId);
		request.setAttribute("viewId", viewId);
		request.setAttribute("nowHead", request.getParameter("nowHead"));
		request.setAttribute("linkMan", request.getParameter("linkMan"));
		request.setAttribute("addContact", request.getParameter("addContact"));
		request.setAttribute("contactTableName", contactTableName);
		request.setAttribute("workFlowNotNullFields", workFlowFieldMap.get("notNull"));
		request.setAttribute("moduleViewBean",moduleViewBean);
		request.setAttribute("loginBean",loginBean);
		
		long longEend = System.currentTimeMillis();
		BaseEnv.log.debug("修改之前总共时间："+(longEend-long1));
		return getForward(request, mapping, "updatePrepare");
	}
	
	/**
	 * 修改客户资料与联系人信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		long long1 = System.currentTimeMillis();
		//处理客户信息
		String[] uploadaffix = request.getParameterValues("uploadaffix");//上传的附件名称
		String[] uploadDelAffix = request.getParameterValues("uploadDelAffix");//需要删除的附件
		String isAttachment = request.getParameter("isAttachment");
		String clientId = request.getParameter("clientId");
		String tableName = request.getParameter("tableName");
		String moduleId = request.getParameter("ModuleId");
		String viewId = request.getParameter("viewId");
		String contactTableName = request.getParameter("contactTableName");//联系人表
		String approveBefore = getParameter("approveBefore",request);//用于添加页面点击转交标志,true表示是
		LoginBean loginBean = this.getLoginBean(request);
		HashMap values = new HashMap() ;
		Result rs = moduleMgt.detailCrmModule(moduleId);
		ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
		rs = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleViewBean = (ClientModuleViewBean)rs.retVal;
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[0]) ;
		readMainTable(values, tableInfo, request);
		
		
		//附件信息
		String uploadVals = "";
		if(uploadaffix!=null && uploadaffix.length != 0){
			for(int j=0;j<uploadaffix.length;j++){
				uploadVals += uploadaffix[j] + ";";
			}
			values.put("Attachment", uploadVals.substring(0, uploadVals.length()-1));
		}else{
			values.put("Attachment", uploadVals);
		}
		
		String mainUniFields = this.getUniqueFIelds(tableInfo);//获取主表唯一值字段
		String childUniFields="";
		//处理明细问题
		Hashtable alltables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(moduleBean.getTableInfo().split(":")[0], alltables);
		for(DBTableInfoBean tableBean : childTableList){
			BaseEnv.log.debug(tableBean.getTableName());
			if(!"CRMClientInfoEmp".equals(tableBean.getTableName())){
				childUniFields = this.getUniqueFIelds(tableBean);
				readChildTable(values, tableBean, request, "00001");
			}
		}
		
		/*保存在内存中所有表结构的信息*/
        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		values.put("id", clientId);
		values.put("createBy", request.getParameter("createBy") != null ? request.getParameter("createBy") : "");
		values.put("lastUpdateBy", loginBean.getId());
		values.put("createTime", request.getParameter("createTime") != null ? request.getParameter("createTime") : "");
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		values.put("workFlowNode", request.getParameter("workFlowNode") != null ? request.getParameter("workFlowNode") : "");
		values.put("workFlowNodeName", request.getParameter("workFlowNodeName") != null ? request.getParameter("workFlowNodeName") : "");
		values.put("SCompanyID", request.getParameter("SCompanyID") != null ? request.getParameter("SCompanyID") : "");
		values.put("noRelationDay", request.getParameter("noRelationDay") != null ? request.getParameter("noRelationDay") : "");
		values.put("TransferDate", request.getParameter("TransferDate") != null ? request.getParameter("TransferDate") : "");
		values.put("Transferer", request.getParameter("Transferer") != null ? request.getParameter("Transferer") : "");
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;
		String locale = GlobalsTool.getLocale(request).toString();
		
		
		//检查字段是否启用了关键字设置
		String keyWordInfo= this.checkModuleKeyWork(moduleBean, values,clientId);
		if(!"".equals(keyWordInfo)){
			 EchoMessage.error().add(keyWordInfo).setAlertRequest(request);
			 return getForward(request, mapping, "alert");
		}
		
		
		//检查字段是否有存在重复值
		String uniqueInfo= this.checkUniqueFields(mainUniFields, childUniFields, moduleBean.getTableInfo(), values, locale.toString(),"update",moduleBean);
		if(!"".equals(uniqueInfo)){
			 EchoMessage.error().add(uniqueInfo).setAlertRequest(request);
			 return getForward(request, mapping, "alert");
			
		}
		
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = loginBean.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		rs = dbmgt.detail(moduleBean.getTableInfo().split(":")[0], map, clientId, sunClassCode,props,loginBean.getId(),true,null);
		
		HashMap histroylMap = (HashMap)rs.retVal;
		String clientChangeInfo = "";
		String detChangeInfo = "";
		String fieldValue = "";//现在的值
		String refieldValue = "";//历史记录的值
		int temp = 0;
		//处理主表信息
		for(String fieldName : moduleViewBean.getPageFields().split(",")){
			if(fieldName.indexOf("contact") == -1){
				fieldValue = values.get(fieldName) + "";
				refieldValue = histroylMap.get(fieldName) +"";
				fieldValue = fieldValue.trim();
				refieldValue = refieldValue.trim();
				if("".equals(refieldValue) || "null".equals(refieldValue)){
					refieldValue = "空";
				}
				if("".equals(fieldValue) || "null".equals(fieldValue)){
					fieldValue = "空";
				}
				if(!refieldValue.trim().equals(fieldValue.trim())){
					if(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getInputType() == 1){
						if(!refieldValue.equals(fieldValue)){
							clientChangeInfo += GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getDisplay().get("zh_CN") +"：" + GlobalsTool.getEnumerationItemsDisplay(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getRefEnumerationName(),refieldValue,"zh_CN") + " 改为 " + GlobalsTool.getEnumerationItemsDisplay(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getRefEnumerationName(),fieldValue,"zh_CN") +"、";
						}
					}else if(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getInputType() == 5){
						String oldStr = "";
						String newStr = "";
						if(fieldValue !=null && !"空".equals(fieldValue)){
							for(String str : fieldValue.split(",")){
								newStr += GlobalsTool.getEnumerationItemsDisplay(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getRefEnumerationName(),str,"zh_CN")+",";
							}
						}
						if(refieldValue !=null && !"空".equals(refieldValue)){
							for(String str : refieldValue.split(",")){
								oldStr += GlobalsTool.getEnumerationItemsDisplay(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getRefEnumerationName(),str,"zh_CN")+",";
							}
						}
						oldStr = "".equals(oldStr) ? "空" : oldStr;
						newStr = "".equals(newStr) ? "空" : newStr;
						clientChangeInfo += GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getDisplay().get("zh_CN") +"："  + oldStr + " 改为 " + newStr + "、";
					}else if(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getInputType() == 14 ){
						String oldStr = "";
						String newStr = "";
						HashMap<String,String> deptMap = (HashMap<String, String>)request.getSession().getServletContext().getAttribute("deptMap");
						if(fieldValue !=null && !"空".equals(fieldValue)){
							for(String str : fieldValue.split(",")){
								newStr += deptMap.get(str)+",";
							}
						}
						if(refieldValue !=null && !"空".equals(refieldValue)){
							for(String str : refieldValue.split(",")){
								oldStr += deptMap.get(str)+",";
							}
						}
						oldStr = "".equals(oldStr) ? "空" : oldStr;
						newStr = "".equals(newStr) ? "空" : newStr;
						clientChangeInfo += GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getDisplay().get("zh_CN") +"："  + oldStr + " 改为 " + newStr + "、";
					}else if(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getInputType() == 15 ){
						String oldStr = "";
						String newStr = "";
						if(fieldValue !=null && !"".equals(fieldValue)){
							for(String str : fieldValue.split(",")){
								if(!"".equals(str)){
									newStr += OnlineUserInfo.getUser(str).getName()+",";
								}
							}
						}
						if(refieldValue !=null && !"空".equals(refieldValue)){
							for(String str : refieldValue.split(",")){
								if(!"".equals(str)){
									oldStr += OnlineUserInfo.getUser(str).getName()+",";
								}
							}
						}
						oldStr = "".equals(oldStr) ? "空" : oldStr;
						newStr = "".equals(newStr) ? "空" : newStr;
						clientChangeInfo += GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getDisplay().get("zh_CN") +"："  + oldStr + " 改为 " + newStr + "、";
					}else if("Trade".equals(fieldName)){
						HashMap tradeMap = (HashMap)request.getSession().getServletContext().getAttribute("workProfessionMap");
						refieldValue = tradeMap.get(histroylMap.get(fieldName)) == null ? "" : tradeMap.get(histroylMap.get(fieldName))+"";
						clientChangeInfo += GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getDisplay().get("zh_CN") +"：" + refieldValue + " 改为 " + tradeMap.get(values.get(fieldName)) + "、";
					}else if("District".equals(fieldName)){
						HashMap districtMap = (HashMap)request.getSession().getServletContext().getAttribute("districtMap");
						refieldValue = districtMap.get(histroylMap.get(fieldName))  == null ? "" : districtMap.get(histroylMap.get(fieldName))+"";
						clientChangeInfo += GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getDisplay().get("zh_CN") +"：" + refieldValue + " 改为 " + districtMap.get(values.get(fieldName)) + "、";
					}else{
						clientChangeInfo += GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName).getDisplay().get("zh_CN") +"："  + refieldValue + " 改为 " + values.get(fieldName) + "、";
					}
				}
			}
		}
		
		//处理从表
		List<HashMap> histroyList = (List<HashMap>)histroylMap.get("TABLENAME_"+moduleBean.getTableInfo().split(":")[1]);
		List<HashMap> valueList = (List<HashMap>)values.get("TABLENAME_"+moduleBean.getTableInfo().split(":")[1]);
		if(valueList != null && valueList.size()>0){
			if(histroyList != null && histroyList.size()>0){
				for(int i=0;i<valueList.size();i++){
					for(int j=0;j<histroyList.size();j++){
						if(valueList.get(i).get("id").equals(histroyList.get(j).get("id"))){
							for(String fieldName : moduleViewBean.getPageFields().split(",")){
								if(fieldName.indexOf("contact") != -1){
									fieldName = fieldName.replace("contact", "");
									fieldValue = valueList.get(i).get(fieldName) +"";
									refieldValue = histroyList.get(j).get(fieldName) + "";
									if("".equals(refieldValue) || "null".equals(refieldValue)){
										refieldValue = "空";
									}
									if("".equals(fieldValue) || "null".equals(fieldValue)){
										fieldValue = "空";
									}
									if(!refieldValue.trim().equals(fieldValue.trim())){
										if(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[1], fieldName).getInputType() == 1 || GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[1], fieldName).getInputType() == 5){
											refieldValue = GlobalsTool.getEnumerationItemsDisplay(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[1], fieldName).getRefEnumerationName(),refieldValue,"zh_CN");
											fieldValue = GlobalsTool.getEnumerationItemsDisplay(GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[1], fieldName).getRefEnumerationName(),fieldValue,"zh_CN");
											refieldValue = "".equals(refieldValue)? "空" :refieldValue;
											fieldValue = "".equals(fieldValue)? "空" :fieldValue;
										}
										clientChangeInfo += GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[1], fieldName).getDisplay().get("zh_CN")   + "：" + refieldValue + " 改为 " + fieldValue + "、";
									}
								}
								
							}
						}
					}
				}
			}
		}
		
		if(valueList != null && valueList.size()>0){
			for(int i=0;i<valueList.size();i++){
				if(histroyList != null && histroyList.size()>0){
					for(int j=0;j<histroyList.size();j++){
						if(valueList.get(i).get("id").equals(histroyList.get(j).get("id"))){
							temp = 1;
							break;
						}
					}
				}else{
					clientChangeInfo += "新增联系人：" + valueList.get(i).get("UserName") +"、";
				}
				if(temp == 0){
					clientChangeInfo += "新增联系人：" + valueList.get(i).get("UserName") +"、";
				}
				temp =0;
			}
		}

		if(histroyList != null && histroyList.size()>0){
			for(int i=0;i<histroyList.size();i++){
				if(valueList != null && valueList.size()>0){
					for(int j=0;j<valueList.size();j++){
						if(histroyList.get(i).get("id").equals(valueList.get(j).get("id"))){
							temp = 1;
							break;
						}
					}
				}else{
					clientChangeInfo += "删除联系人：" + histroyList.get(i).get("UserName") +"、";
					
				}
				if(temp == 0){
					clientChangeInfo += "删除联系人：" + histroyList.get(i).get("UserName") +"、";
				}
				temp =0;
			}
			
		}
		
		if("".equals(clientChangeInfo)){
			clientChangeInfo ="no";
		}
		String ip = GlobalsTool.getIpAddr(request);
		ClientInfoRecordBean clientRecordBean = new ClientInfoRecordBean();
		clientRecordBean.setId(IDGenerater.getId());
		clientRecordBean.setClientId(clientId);
		clientRecordBean.setUpdateInfo(clientChangeInfo);
		clientRecordBean.setMapInfo(gson.toJson(histroylMap));
		clientRecordBean.setTableInfo(moduleBean.getTableInfo());
		clientRecordBean.setModuleId(moduleBean.getId());
		clientRecordBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		clientRecordBean.setEmployeeId(loginBean.getId());
		clientRecordBean.setIpAddress(ip);
		clientRecordBean.setViewId(viewId);
		long long2 = System.currentTimeMillis();
		BaseEnv.log.debug("修改之前："+(long2-long1));
		
		
		
		
		
		rs = mgt.update(moduleBean, alltables, values, loginBean.getId(), "", resources, this.getLocale(request), "", null, loginBean, null, null, mop,clientRecordBean);
		long long3 = System.currentTimeMillis();
		BaseEnv.log.debug("修改花费时间："+(long3-long2));
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	int operation=1;
        	
        	String billTypeName=getModuleNameByLinkAddr(request, mapping);
        	dbmgt.addLog(operation, values, histroylMap, tableInfo, this.getLocale(request).toString(), loginBean.getId(), loginBean.getEmpFullName(), loginBean.getSunCmpClassCode(),"",billTypeName);

        	
        	
        	if(uploadaffix!=null && uploadaffix.length>0){
				for(int j=0;j<uploadaffix.length;j++){
					FileHandler.copy("CRMClientInfo", FileHandler.TYPE_AFFIX, uploadaffix[j], uploadaffix[j]);
					FileHandler.deleteTemp(uploadaffix[j]);
				}
			}
			/*
				if(uploadDelAffix!=null && uploadDelAffix.length>0){
					for(int i=0;i<uploadDelAffix.length;i++){
						FileHandler.delete(tableName, FileHandler.TYPE_AFFIX, uploadDelAffix[i]);
					}
				}
			*/
			//点击转交还是添加按钮
			if(approveBefore!=null && "true".equals(approveBefore)){
				//转交按钮跳转到index.jsp 的dealCheck(keyId)  处理
				request.setAttribute("noAlert", "true");
				request.setAttribute("checkFlag", "true");
				request.setAttribute("checkBackUrl", values.get("id"));
				request.setAttribute("dealAsyn", "true");
			}else{
				request.setAttribute("crmFreash", "true");
			}
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/CRMClientAction.do?operation=6&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&moduleId=" + moduleId).setAlertRequest(request);
		}else {
       	 	//添加失败
        	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
        }
		return getForward(request, mapping, "alert");
	}
	
	

	/**
	 * ajax异步检测联系人姓名、电话、邮件、手机
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private ActionForward contactVerify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*String colName = request.getParameter("colName");
		String colVal = URLDecoder.decode(request.getParameter("colVal"),"UTF-8");;
		Result rs = mgt.checkContactCol(colName, colVal);
		List list = (List)rs.retVal;
		if(list.size() == 0){
			request.setAttribute("msg", "no");
		}else{
			String msg = "";
			rs = mgt.findEmployee(((Object[])list.get(0))[1].toString());
			EmployeeBean employee = (EmployeeBean)rs.retVal;
			if("ClientName".equals(colName)){
				msg += employee.getEmpFullName() + "创建的" + GlobalsTool.getEnumerationItemsDisplay("ClientStatus", ((Object[])list.get(0))[2].toString(), getLocale(request).toString()) +"'" + colVal + "'已存在";
			}else{
				msg += employee.getEmpFullName() + "创建的联系人:" + ((Object[])list.get(0))[2].toString();
				if("Mobile".equals(colName)){
					msg +=",手机:" + colVal; 
				}else if("Telephone".equals(colName)){
					msg +=",电话:" + colVal; 
				}else{
					msg +=",邮件:" + colVal; 
				}
					msg +="已存在";
			}
			request.setAttribute("msg", msg);
		}
		*/
		return getForward(request, mapping, "blank");
	}
	
//	用于获得模板EXCEL 与导出数据
	private ActionForward getModuleExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String isTemplet = request.getParameter("isTemplet");
		String accName = GlobalsTool.getSysSetting("DefBackPath");
		File file = new File("../../AIOBillExport");
		if(!file.exists()){
			file.mkdirs() ;
		}
		String moduleId = request.getParameter("ModuleId");
		String viewId = request.getParameter("viewId");
		Result rest = moduleMgt.detailCrmModule(moduleId);
		ClientModuleBean moduleBean = (ClientModuleBean)rest.retVal;
		rest = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleViewBean = (ClientModuleViewBean) rest.retVal;
		
		String tableDisplay=GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[0]).getDisplay().get(getLocale(request).toString());
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
		String sunClassCode = loginBean.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;
		List<DBFieldInfoBean> clientInfos = ((DBTableInfoBean) allTables.get(moduleBean.getTableInfo().split(":")[0])).getFieldInfos();
		List<DBFieldInfoBean> contactInfos = ((DBTableInfoBean) allTables.get(moduleBean.getTableInfo().split(":")[1])).getFieldInfos();
		HashMap<String, DBFieldInfoBean> moduleFieldBeans = new HashMap<String, DBFieldInfoBean>();
		String pageFields= moduleViewBean.getPageFields();
		pageFields += "createBy";
		String[] Allcols = pageFields.replaceAll("Attachment,", "").split(",");//获得模板的所有字段名
		
		for(int i=0;i<Allcols.length;i++){
			if(Allcols[i] !=null){
				for(int j=0;j<clientInfos.size();j++){
					if(clientInfos.get(j).getFieldName().equals(Allcols[i])){
						exportList.add(new ExportField("main", clientInfos.get(j).getTableBean().getTableName(), clientInfos.get(j).getFieldName(),clientInfos.get(j).getInputType(), clientInfos.get(j).getDisplay().get(locale), "客户资料",false));
						moduleFieldBeans.put(Allcols[i], clientInfos.get(j));
					}	
				}
			}
		}
		for(int i=0;i<Allcols.length;i++){
			if(Allcols[i] !=null){
				if(Allcols[i].indexOf("contact")>-1){
					for(int j=0;j<contactInfos.size();j++){
						if(contactInfos.get(j).getFieldName().equals(Allcols[i].replace("contact", ""))){
							exportList.add(new ExportField("child", contactInfos.get(j).getTableBean().getTableName(), contactInfos.get(j).getFieldName(), contactInfos.get(j).getInputType(), contactInfos.get(j).getDisplay().get(locale), "客户联系人明细",false));
						}	
					}
				}
			}
		}
		
		if(isTemplet ==null && !"true".equals("isTemplet")){
			
			String exportAll = getParameter("exportAll",request);
			if(exportAll !=null && "true".equals(exportAll)){
				//导出所有
				Result rs = mgt.queryClientIdsByModule(moduleBean.getId());
				List<Object> rsList = (List<Object>)rs.retVal;
				if(rsList!=null && rsList.size()>0){
					for(Object obj : rsList){
						String keyId = GlobalsTool.get(obj,0).toString();
						rest = dbmgt.detail(moduleBean.getTableInfo().split(":")[0], map, keyId, sunClassCode,props,loginBean.getId(),true,null);
						HashMap rsMap = (HashMap)rest.retVal;//获取值	
						this.setExportValue(rsMap, moduleBean, Allcols, moduleFieldBeans, exportValueList, locale, request,loginBean);
					}
				}
			}else{
				String[] keyIds = getParameters("keyId", request) ;/*客户Id*/
				for(String keyId : keyIds){
					rest = dbmgt.detail(moduleBean.getTableInfo().split(":")[0], map, keyId, sunClassCode,props,loginBean.getId(),true,null);
					HashMap rsMap = (HashMap)rest.retVal;//获取值	
					this.setExportValue(rsMap, moduleBean, Allcols, moduleFieldBeans, exportValueList, locale, request,loginBean);
				}
			}
			//rest = dbmgt.detail(moduleBean.getTableInfo().split(":")[0], map, keyIds[i], sunClassCode,props,loginBean.getId(),true,mop.getScope(MOperation.M_UPDATE),null);
			//HashMap rsMap = (HashMap)rest.retVal;//获取值
			
			
			
			
		}

		Result result = ExportData.billExport(fos, "客户资料", exportList, exportValueList,moduleViewBean.getViewName());
		fos.close();
		if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			if(isTemplet ==null && !"true".equals("isTemplet")){
				EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
						+GlobalsTool.encode(tableDisplay+".xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
						getMessage(request, "com.export.success.over")+fileName)
						.setBackUrl("/CRMClientAction.do?operation=4&firstEnter=false&ModuleId="+moduleId+"&viewId="+viewId).setNotAutoBack()
						.setAlertRequest(request);
				return getForward(request, mapping, "message");
			}else{
				String downUrl = "/ReadFile?tempFile=export&fileName="+GlobalsTool.encode(tableDisplay+".xls");
				request.setAttribute("msg", downUrl);
				return getForward(request, mapping, "blank");
			}
	    	
        }else{
        	EchoMessage.error().add(getMessage(request, "com.export.failure"))
        						 .setNotAutoBack().setAlertRequest(request);
        }
        return getForward(request, mapping, "importClient");
	}
	
	
	/**
	 * 重写权限方法
	 */
	 protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		String parameter = mapping.getParameter();
		String type = req.getParameter("type");
		String tableName = getParameter("tableName",req);//针对邮件记录，不限制权限
		String isConsole = getParameter("isConsole", req);//表示客服控制台进入，权限不受控制
		//
		int operation = getOperation(req);
		if("/CrmTabAction.do".equals(parameter) || operation == 54){
			return null ;
		}else if("delLabel".equals(type) || "selLabel".equals(type) || "true".equals(isConsole) || "clientLog".equals(type)){
			return null ;
		}else if("brotherIframe".equals(type) || "CRMMailInfoView".equals(tableName)){
			//邮件记录不控制权限
			return null ;
		}else if("checkExistClients".equals(type)|| "addCommonInfo".equals(type)){
			LoginBean loginBean = getLoginBean(req) ;
			//进行唯一用户验证，如果有生复登陆的，则后进入用户踢出前进入用户
			if (loginBean == null || !OnlineUserInfo.checkUser(req)) {
				req.setAttribute("msg", "dealLine"); 
				return getForward(req, mapping, "blank");
			}
		}
		return super.doAuth(req, mapping);
	}
	
	 /**
     * 取字段的显示名
     * @param fieldName String tableName.fieldName
     * @return String
     */
    public static String getFieldDisplay(Hashtable allTables, String fieldName,String locale) {
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
                try {
                    return fieldInfo.getDisplay().get(locale).toString();
                } catch (Exception ex) {
                    return fieldInfo.getFieldName();
                }
            }
        }
        return field;
    }
    
    /*
    public String[] getScreenFields(LoginBean loginBean,String fields,String isConDel,String viewId){
    	
    	Result rs = mgt.findFieidScopes(loginBean.getDepartCode(), loginBean.getId(), "hidden",viewId);
    	List<FieldScopeSetBean> list = (List<FieldScopeSetBean>)rs.retVal;
    	String hiddenStr = "";
    	String colFields = "";
    	String[] str = new String[2];
    	for(int i=0;i<list.size();i++){
    		hiddenStr += list.get(i).getFieldsName();
    	}
    	
    	for(int i=0;i<fields.split(",").length;i++){
    		if(hiddenStr.indexOf(fields.split(",")[i]) == -1){
    			colFields += fields.split(",")[i] + ","; 
    		}else{
    			if(fields.split(",")[i].indexOf("contact") > -1){
    				if("false".equals(isConDel)){
    					isConDel = "true";
    				}
    			}
    		}
    	}
    	
    	str[0] = colFields;
    	str[1] = isConDel;
    	return str;
    }*/
    
    /**
	 * 模板移交前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward moduleTransferPrepare(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		LoginBean login = getLoginBean(request) ;
		String moduleId = request.getParameter("moduleId") ;
		String isSetDefModule = request.getParameter("isSetDefModule") ;
		Result result = moduleMgt.queryMyModules(login) ;
		
		//存放模板对应的视图数量
		Result rs = moduleMgt.queryModuleGroupBy(login);
		HashMap<String,Integer> moduleCountMap= (HashMap<String,Integer>)rs.retVal;
		
		request.setAttribute("moduleCountMap", moduleCountMap);
		request.setAttribute("moduleList", result.retVal) ;
		request.setAttribute("moduleId", moduleId) ;
		request.setAttribute("isSetDefModule", isSetDefModule) ;
		request.setAttribute("defModuleId", login.getModuleId()) ;
		return getForward(request, mapping, "moduleTransfer") ;
	}
	
	/**
	 * 模板转移
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward moduleTransfer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] keyIds = getParameters("keyId", request) ;/*客户Id*/
		String moduleId = getParameter("ModuleId",request);
		String viewId = getParameter("viewId",request);
		String moduleTransferId = getParameter("moduleTransferId", request);
		Result result = mgt.moduleTransfer(keyIds, moduleTransferId);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/*客户移交成功*/
			EchoMessage.success().add(getMessage(request, "oa.bbs.operationOk")).setBackUrl(
					"/CRMClientAction.do?ModuleId="+moduleId+"&viewId="+viewId).setAlertRequest(request);
		}else{
				/*客户移交失败*/
				EchoMessage.error().add(getMessage(request, "common.alert.updateFailure")).setBackUrl(
					"/CRMClientAction.do").setAlertRequest(request);
		}
		return mapping.findForward("message");
	}
	
	/**
	 * 设置默认模板
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setDefModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginBean login = getLoginBean(request);
		String defModuleId = getParameter("defModuleId", request);
		Result result = mgt.moduleTransfer(defModuleId, login.getId());
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			login.setModuleId(defModuleId);
			request.getSession().setAttribute("LoginBean", login);
			/*成功*/
			EchoMessage.success().add(getMessage(request, "oa.bbs.operationOk")).setBackUrl(
					"/CRMClientAction.do").setAlertRequest(request);
		}else{
				/*失败*/
				EchoMessage.error().add(getMessage(request, "common.alert.updateFailure")).setBackUrl(
					"/CRMClientAction.do").setAlertRequest(request);
		}
		return mapping.findForward("message");
	}
	
	/**
	 * 添加选项数据前的准备
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward addEnumerPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String enumerName = request.getParameter("enumerName");//获得客户表
		String selName = request.getParameter("selName");//select 名称
		Result results = mgt.findEnumeration(enumerName);
		List<Object> list = (List<Object>)results.retVal;
		String enumerId = ((Object[])list.get(0))[0].toString();
		String enumerCN = ((Object[])list.get(0))[1].toString();
		String enumerVal = "";
		Date date = new Date();
		enumerVal = date.getMinutes() + "" + date.getSeconds();
		while(true){
			list = (List)mgt.findEnumerItem(selName, enumerVal).retVal;
			if(list==null || list.size() == 0){
				break;
			}
			enumerVal = date.getHours() + "" + date.getMinutes();
		}
		
		request.setAttribute("enumerId", enumerId) ;
		request.setAttribute("enumerCN", enumerCN) ;
		request.setAttribute("enumerVal", enumerVal) ;
		request.setAttribute("languageLocal", this.getLocale(request).getCountry()) ;
		return getForward(request, mapping, "addEnumerPrepare");
	}
	
	/**
	 * 添加选项数据
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward addEnumer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String languageVal = request.getParameter("languageVal");//
		String enumerId = request.getParameter("enumerId");//获得客户表
		String enumerVal = request.getParameter("enumerVal");//获得客户表
		String sort = request.getParameter("sort");//获得客户表
		String languageId = IDGenerater.getId();
		String languageSql = "";
		languageSql ="insert into tblLanguage(id,zh_TW,zh_CN,en)values('"+ languageId +"','" + languageVal + "','" + languageVal +"','" + languageVal + "')";
		String enumerSql = "";
		enumerSql = "insert into tblDBEnumerationItem VALUES('" + IDGenerater.getId() + "','" + enumerVal + "','" + enumerId + "','NULL','NULL','NULL','NULL','NULL','0','" + languageId + "','NULL','" + sort + "')";
		Result rs = mgt.addEnumer(languageSql, enumerSql);
		//更新内存数据
        InitMenData imd = new InitMenData();
        //更新内存中枚举值
        imd.initEnumerationInformation();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg","success");
		}else {
       	 //添加失败
			request.setAttribute("msg","error");
        }
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * ajax异步检测联系人姓名、电话、邮件、手机
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private ActionForward enumerVerify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String enumerId = request.getParameter("enumerId");
		String enumerVal = request.getParameter("enumerVal");
		Result rs = mgt.findEnumer(enumerId, enumerVal);
		List list = (List)rs.retVal;
		if(list.size() == 0){
			request.setAttribute("msg", "no");
		}else{
			request.setAttribute("msg", "yes");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 查看历史记录
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward history(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String clientId = request.getParameter("clientId");
		Result rs = mgt.findHistory(clientId);
		List<ClientInfoRecordBean> list = (List<ClientInfoRecordBean>)rs.retVal;
		LoginBean loginBean = this.getLoginBean(request);
		request.setAttribute("list", list);
		request.setAttribute("loginBean", loginBean);
		return getForward(request, mapping, "history");
	}
	
	/**
	 * 还原客户
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward reHistroy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String recordId = request.getParameter("recordId");
		Result rs = mgt.findClientRecord(recordId);
		ClientInfoRecordBean bean = (ClientInfoRecordBean) rs.retVal;
		HashMap map = gson.fromJson(bean.getMapInfo(), HashMap.class);
		Hashtable alltables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;
		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
		   resources = (MessageResources) ob;
		}
		Set keys = map.keySet( );
		if(keys != null) {
			Iterator iterator = keys.iterator();
			while(iterator.hasNext( )) {
				Object key = iterator.next( );
				Object value = map.get(key);
				BaseEnv.log.debug(key);
				if(GlobalsTool.getFieldBean(bean.getTableInfo().split(":")[0], (String)key)!=null && ((String)key).indexOf("TABLENAME")== -1 && !((String)key).equals("LANGUAGEQUERY") && GlobalsTool.getFieldBean(bean.getTableInfo().split(":")[0], (String)key).getFieldType() == 0){
					double d = (Double)value;
					map.put((String)key, (int)d);
				}
				
			}
		}
		List<HashMap> childList = (List<HashMap>)map.get("TABLENAME_"+bean.getTableInfo().split(":")[1]);
		List replaceList = new ArrayList();
		
		for(int i=0;i<childList.size();i++){
			HashMap tempMap = childList.get(i);
			keys = tempMap.keySet();
			if(keys != null) {
				Iterator iterator = keys.iterator( );
				while(iterator.hasNext( )) {
					Object key = iterator.next( );
					Object value = tempMap.get(key);
					if(GlobalsTool.getFieldBean(bean.getTableInfo().split(":")[1], (String)key).getFieldType() == 0){
						if(value !=null && !"".equals(value)){
							double d = (Double)value;
							tempMap.put((String)key, (int)d);
						}
						
					}
				}
			}
			replaceList.add(tempMap);
		}
		map.put("TABLENAME_"+bean.getTableInfo().split(":")[1], replaceList);
		
		ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[0]) ;
		
		String mainUniFields = this.getUniqueFIelds(tableInfo);//获取主表唯一值字段
		String childUniFields="";
		//处理明细问题
		ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(moduleBean.getTableInfo().split(":")[0], alltables);
		for(DBTableInfoBean tableBean : childTableList){
			if(!"CRMClientInfoEmp".equals(tableBean.getTableName())){
				childUniFields = this.getUniqueFIelds(tableBean);
			}
		}
		String locale = GlobalsTool.getLocale(request).toString();
		//检查字段是否有存在重复值
		String uniqueInfo= this.checkUniqueFields(mainUniFields, childUniFields, moduleBean.getTableInfo(), map, locale.toString(),"update",moduleBean);
		if(!"".equals(uniqueInfo)){
			 EchoMessage.error().add(uniqueInfo).setAlertRequest(request);
			 return getForward(request, mapping, "alert");
			
		}
		
		rs = dbmgt.update(bean.getTableInfo().split(":")[0], alltables, map, loginBean.getId(), "", resources, this.getLocale(request), "", loginBean, null, null);
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("dealAsyn", "true");
			request.setAttribute("noAlert", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("").setAlertRequest(request);
		}else {
       	 	//添加失败
        	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
        }
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 上传附件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward affixUpload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		String affix = URLDecoder.decode(request.getParameter("affix"),"UTF-8");
		String oldAffix = URLDecoder.decode(request.getParameter("oldAffix"),"UTF-8");
		String updAffixStr ="";
		String clientId = request.getParameter("clientId");
		String[] uploadaffix = affix.split(";");
		if(uploadaffix!=null && uploadaffix.length>0){
			for(int j=0;j<uploadaffix.length;j++){
				FileHandler.copy("CRMClientInfo", FileHandler.TYPE_AFFIX, uploadaffix[j], uploadaffix[j]);
				FileHandler.deleteTemp(uploadaffix[j]);
			}
		}
		if(oldAffix !=null || !"".equals(oldAffix)){
			updAffixStr = oldAffix+affix;
		}else{
			updAffixStr = affix;
		}
		mgt.updateAffix(clientId,updAffixStr,false);
		*/
		
		
		String clientId = getParameter("clientId",request);//任务id
		String uploadStrs = getParameter("uploadStrs",request);//附件信息
		
		if(uploadStrs!=null && !"".equals(uploadStrs)){
			for(String str :uploadStrs.split(";")){
				FileHandler.copy("CRMClientInfo", FileHandler.TYPE_AFFIX, str, str);
				FileHandler.deleteTemp(str);
			}
		}
		
		String sql = "UPDATE CRMClientInfo set attachment = isNull(attachment,'')+'"+uploadStrs+"' where id=?";
		ArrayList param = new ArrayList();
		param.add(clientId);
		
		Result rs = mgt.operationSql(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 详情更新某字段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward detailUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String clientId = getParameter("clientId",request);//客户ID
		String fieldName = getParameter("fieldName",request);//字段名称
		String fieldValue =getParameter("fieldValue",request);//字段值
		//String id = request.getParameter("id");
		//String isContact = request.getParameter("isContact");
		String moduleId = request.getParameter("moduleId");
		String viewId = request.getParameter("viewId");
		String ip = GlobalsTool.getIpAddr(request);
		
//		处理历史记录
		Result rs = moduleMgt.detailCrmModule(moduleId) ;
		ClientModuleBean moduleBean = (ClientModuleBean) rs.retVal;
		String tableName = moduleBean.getTableInfo().split(":")[0];
		
		if("ClientName".equals(fieldName)){
			HashMap map = new HashMap();
			map.put(fieldName, fieldValue);
			//检查字段是否启用了关键字设置
			String keyWordInfo= this.checkModuleKeyWork(moduleBean, map,clientId);
			if(!"".equals(keyWordInfo)){
				request.setAttribute("msg",keyWordInfo);
				return getForward(request, mapping, "blank");
			}
		}
		
		
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = loginBean.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		rs = dbmgt.detail(tableName, map, clientId, sunClassCode,props,loginBean.getId(),true,null);//获得详情
		HashMap histroylMap = (HashMap)rs.retVal;
		String ChangeInfo = "";//修改记录
		//处理修改信息
		String refieldValue = histroylMap.get(fieldName) == null ? "" : histroylMap.get(fieldName) + "";
		
//		if("true".equals(isContact)){
//			tableName = moduleBean.getTableInfo().split(":")[1];
//		}
		if(!refieldValue.trim().equals(fieldValue.trim())){
			if(GlobalsTool.getFieldBean(tableName, fieldName).getInputType() == 1 || GlobalsTool.getFieldBean(tableName, fieldName).getInputType() == 5){
					ChangeInfo += GlobalsTool.getFieldBean(tableName, fieldName).getDisplay().get("zh_CN") +"'" + GlobalsTool.getEnumerationItemsDisplay(GlobalsTool.getFieldBean(tableName, fieldName).getRefEnumerationName(),refieldValue,"zh_CN") + "'改为'" + GlobalsTool.getEnumerationItemsDisplay(GlobalsTool.getFieldBean(tableName, fieldName).getRefEnumerationName(),fieldValue,"zh_CN") + "' ";
			}else if("Trade".equals(fieldName)){
				HashMap tradeMap = (HashMap)request.getSession().getServletContext().getAttribute("workProfessionMap");
				refieldValue = tradeMap.get(histroylMap.get(fieldName)) == null ? " " : "'" + tradeMap.get(histroylMap.get(fieldName)) + "'";
				ChangeInfo += GlobalsTool.getFieldBean(tableName, fieldName).getDisplay().get("zh_CN") + refieldValue + "改为'" + tradeMap.get(fieldValue) + "' ";
			}else if("District".equals(fieldName)){
				HashMap districtMap = (HashMap)request.getSession().getServletContext().getAttribute("districtMap");
				refieldValue = districtMap.get(histroylMap.get(fieldName)) == null ? " " : "'" + districtMap.get(histroylMap.get(fieldName)) + "'";
				ChangeInfo += GlobalsTool.getFieldBean(tableName, fieldName).getDisplay().get("zh_CN")  +"'" + refieldValue + "改为'" + districtMap.get(fieldValue) + "' ";
			}else{
				ChangeInfo += GlobalsTool.getFieldBean(tableName, fieldName).getDisplay().get("zh_CN") +"'"  + refieldValue + "'改为'" + fieldValue + "' ";
			}
		}
		ClientInfoRecordBean clientRecordBean = new ClientInfoRecordBean();
		clientRecordBean.setId(IDGenerater.getId());
		clientRecordBean.setClientId(clientId);
		clientRecordBean.setUpdateInfo(ChangeInfo);
		clientRecordBean.setMapInfo(gson.toJson(histroylMap));
		clientRecordBean.setTableInfo(moduleBean.getTableInfo());
		clientRecordBean.setModuleId(moduleBean.getId());
		clientRecordBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		clientRecordBean.setIpAddress(ip);
		clientRecordBean.setEmployeeId(loginBean.getId());
		clientRecordBean.setViewId(viewId);
		
		histroylMap.put(fieldName, fieldValue);
		if("ClientName".equals(fieldName)){
			rs = mgt.checkContactCol(fieldName, fieldValue,clientId);
			List list = (List)rs.retVal;
			if(list.size() == 0){
				mgt.updateField(clientId, fieldName, fieldValue, false,clientRecordBean,loginBean,moduleBean,histroylMap);
				request.setAttribute("msg", "success");
			}else{
				request.setAttribute("msg", "error");
			}
		}else{
			mgt.updateField(clientId, fieldName, fieldValue, false,clientRecordBean,loginBean,moduleBean,histroylMap);
			request.setAttribute("msg", "success");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 发送邮件前，查看是否有联系人邮箱地址
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward queryContactInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String clientId = request.getParameter("clientId");
		String fieldName = request.getParameter("fieldName");
		Result rs = mgt.findContactByClientId(clientId,fieldName);
		List list = (List)rs.retVal;
		if(list ==null || list.size() ==0){
			request.setAttribute("msg", "no");
		}else{
			request.setAttribute("msg", "yes");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 添加标签
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward addLabel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String labelName = request.getParameter("labelName");
		String labelColor = request.getParameter("labelColor");
		String labelId = request.getParameter("labelId");
		String moduleId = request.getParameter("moduleId");
		LoginBean login = getLoginBean(request);
		CRMLabelBean labelBean = new CRMLabelBean();
		labelBean.setId(labelId);
		labelBean.setLabelName(labelName);
		labelBean.setLabelColor(labelColor);
		labelBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		labelBean.setEmployeeId(login.getId());
		labelBean.setModuleId(moduleId);
		Result rs = mgt.addCRMLabel(labelBean);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
	        request.setAttribute("dealAsyn", "true");
	        request.setAttribute("noAlert", "true");
	        request.setAttribute("labelId",labelBean.getId());
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
        return getForward(request, mapping, "alert");
	}
	
	/**
	 * 删除标签
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward delLabel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String labelId = request.getParameter("labelId");
		String clientId = request.getParameter("clientId");
		String moduleId = request.getParameter("moduleId");
		String isDelBean = request.getParameter("isDelBean");
		Result rs = mgt.delLabel(labelId,isDelBean,clientId);
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg","yes");
			if(isDelBean !=null && "false".equals(isDelBean)){
				return getForward(request, mapping, "blank");
			}else{
				return new ActionForward("/CRMClientAction.do?operation=4&type=label&moduleId="+moduleId,true);
			}
		} else {
			request.setAttribute("msg","no");
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
					.setAlertRequest(request);
		}
		if(isDelBean !=null && "false".equals(isDelBean)){
			return getForward(request, mapping, "blank");
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 更新标签
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward updateSelLabel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String clientId = request.getParameter("clientId");
		String labelId = request.getParameter("labelId");
		Result rs = mgt.updateLabel(clientId, labelId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", "yes");
		}else{
			request.setAttribute("msg", "no");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 查看CRM标签
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward queryLabel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String moduleId = request.getParameter("moduleId");
		Result result=(new ClientSetingMgt()).detailCrmModule(moduleId);
		ClientModuleBean moduleBean=(ClientModuleBean) result.retVal;
		LoginBean loginBean = getLoginBean(request);
		Result rs = mgt.findCRMLabels(loginBean.getId(),moduleBean.getId());
		List<CRMLabelBean> list = (List<CRMLabelBean>)rs.retVal;
		request.setAttribute("labelList", list);
		request.setAttribute("loginBean", loginBean);
		request.setAttribute("moduleBean", moduleBean);
		
		return getForward(request, mapping, "label");
	}
	
	/**
	 * 标签更新前
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward updateLabelPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String labelId = request.getParameter("labelId");
		String moduleId = request.getParameter("moduleId");
		Result rs = mgt.findLabel(labelId);
		CRMLabelBean labelBean = (CRMLabelBean)rs.retVal;
		request.setAttribute("labelBean", labelBean);
		request.setAttribute("moduleId", moduleId);
		return getForward(request, mapping, "updateLabelPrepare");
	}
	
	/**
	 * 更新标签Bean
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward updateLabelBean(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginBean login = getLoginBean(request);
		String labelId = request.getParameter("labelId");
		String labelName = request.getParameter("labelName");
		String labelColor = request.getParameter("labelColor");
		String createTime = request.getParameter("createTime");
		String moduleId = request.getParameter("moduleId");
		CRMLabelBean labelBean = new CRMLabelBean();
		labelBean.setId(labelId);
		labelBean.setLabelName(labelName);
		labelBean.setLabelColor(labelColor);
		labelBean.setCreateTime(createTime);
		labelBean.setEmployeeId(login.getId());
		labelBean.setModuleId(moduleId);
		Result rs = mgt.updateLabel(labelBean);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
	        request.setAttribute("dealAsyn", "true");
	        request.setAttribute("noAlert", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.updateFailture")).setAlertRequest(request);
		}
        return getForward(request, mapping, "alert");
	}
	
	/**
	 * 新增标签前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward addlabelPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String moduleId = getParameter("moduleId", request);
		String labelId = IDGenerater.getId();
		request.setAttribute("labelId", labelId);
		request.setAttribute("moduleId", moduleId);
        return getForward(request, mapping, "addlabelPrepare");
	}
	
	/**
	 * 更新标签是否启用
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward labelUse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String moduleId = request.getParameter("moduleId");
		String useFlag = request.getParameter("useFlag");
		Result rs = mgt.updateLabelUse(moduleId, useFlag);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", "yes");
		}else{
			request.setAttribute("msg", "no");
		}
		return getForward(request, mapping, "blank");
	}
	
	private HashMap getClientDetSort(HashMap map,String tableInfo){
		List sortList = new ArrayList();
		List<HashMap> list = (ArrayList<HashMap>)map.get("TABLENAME_"+tableInfo.split(":")[1]);
		if(list!=null && list.size() !=0){
			for(int i=0;i<list.size();i++){
				if("1".equals(list.get(i).get("mainUser"))){
					sortList.add(list.get(i));
					list.remove(list.get(i));
				}
			}
			sortList.addAll(list);
			map.put("TABLENAME_"+tableInfo.split(":")[1], sortList);
		}
		return map;
	}
	
	/**
	 * 批量共享,去除相同的
	 * @param oldIds
	 * @param newIds
	 * @return
	 */
	public String getIndexIds(String oldIds,String newIds){
		String ids = oldIds==null ? "":oldIds;
		if(newIds!=null && !"".equals(newIds)){
			for(String str : newIds.split(",")){
				if(oldIds.indexOf(str) == -1){
					ids += str +",";
				}
			}
		}
		return ids;
	}
	
	/**
	 * 获取自定义弹出框展示名称
	 * @param tableInfo 
	 * @param values 字段存放的值
	 * @param loginBean 
	 * @return
	 */
	public HashMap<String,String> getDefineDisplay(DBTableInfoBean tableInfo,HashMap rsMap ,LoginBean loginBean){
		HashMap<String,String> defineDisMap = new HashMap<String, String>();
		for(DBFieldInfoBean bean : tableInfo.getFieldInfos()){
			if(bean.getInputType() == 2){
				HashMap<String,String> map = new HashMap<String, String>();//key存放fieldName,value:对应的值
				if(rsMap.get(bean.getFieldName()) != null){
					map.put(bean.getFieldName(),rsMap.get(bean.getFieldName()).toString());
					Result rest = workFlowMgt.getPop(map, bean, loginBean);//获取对应的展示名称,存放到上面定义的map中,key为"popup_"+bean.getFieldName()
					if(map.get("popup_"+bean.getFieldName()) != null){
						//处理展示名称,放到defineDisMap中展示到页面
						String display = map.get("popup_"+bean.getFieldName());
						String names = "";
						for(String str : display.split(";")){
							if(str !=null && !"".equals(str)){
								if(str.indexOf("%koron%") >-1){
									names += str.substring(str.indexOf("%koron%")+7)+",";
								}else{
									names += str +",";
								}
							}
						}
						if(names.endsWith(",")){
							names = names.substring(0,names.length()-1);
						}
						defineDisMap.put("popup_"+bean.getFieldName(), names);
					}
				}
			}
		}
		return defineDisMap;
	}
	
	/**
	 * 根据弹出框名称检查是否有此弹出框
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward checkPopupSelectName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		String fieldName = getParameter("fieldName",request);//弹出框名称
		String selectName = getParameter("selectName",request);//弹出框名称
		String moduleId = getParameter("ModuleId",request);//模板id
		Result rs = moduleMgt.detailCrmModule(moduleId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ClientModuleBean bean = (ClientModuleBean)rs.retVal;
			DBFieldInfoBean fieldBean = GlobalsTool.getField(fieldName, bean.getTableInfo().split(":")[0],  bean.getTableInfo().split(":")[1]);
			if(fieldBean !=null ){
				PopupSelectBean selectBean = fieldBean.getSelectBean();
				if(selectBean == null){
					request.setAttribute("msg", "no");
				}else{
					request.setAttribute("msg", "yes");
				}
			}
		}else{
			request.setAttribute("msg", "module");
		}*/
		String selectNames = getParameter("selectName",request);//弹出框名称
		String noExistPop = "false";//不存在弹出框，默认false;
		String noExistSelectName = "";
		if(selectNames!=null && !"".equals(selectNames)){
			for(String selectName : selectNames.split(",")){
				PopupSelectBean selectBean = (PopupSelectBean)BaseEnv.popupSelectMap.get(selectName);
				if(selectBean == null){
					noExistPop = "true";
					noExistSelectName = selectName;
					break;
				}
			}
		}
		if("false".equals(noExistPop)){
			request.setAttribute("msg","false");
		}else{
			request.setAttribute("msg",noExistSelectName);//有不存在的弹出框
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 查找存在的Company客户
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward checkExistClients(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String clientIds = getParameter("clientIds", request);//客户ID
		String transferType = getParameter("transferType", request);//转换标识 erpToCrm or crmToErp
		
		Result rs  = new Result();
		String ids = "";
		for(String id : clientIds.split(",")){
			ids += "'"+id+"',";
		}
		if(ids.endsWith(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		
		if("crmToErp".equals(transferType)){
			rs = mgt.findCompanys(ids);
		}else{
			rs = mgt.findClients(ids);
		}
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List<Object> list = (List<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				String keyIds = "";
				for(Object obj : list){
					keyIds += GlobalsTool.get(obj,0)+",";
				}
				if(keyIds.endsWith(",")){
					keyIds = keyIds.substring(0,keyIds.length()-1);
				}
				request.setAttribute("msg", keyIds);
			}else{
				request.setAttribute("msg", "");
			}
		}else{
			request.setAttribute("msg", "no");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * ERPCRM互转客户查看是否有相同名称
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward checkExistClientNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String clientIds = getParameter("clientIds", request);//客户ID
		String transferType = getParameter("transferType", request);//转换标识 erpToCrm or crmToErp
		String clientNames = "";
		if(!"".equals(clientIds)){
			Result rs  = new Result();
			rs = mgt.findExistClientName(clientIds, transferType);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				List<Object> list = (List<Object>)rs.retVal;
				if(list!=null && list.size()>0){
					for(Object obj : list){
						clientNames += "'"+GlobalsTool.get(obj,0)+"',";
					}
					if(clientNames.endsWith(",")){
						clientNames = clientNames.substring(0,clientNames.length()-1);
					}
				}
				request.setAttribute("msg", clientNames);
			}else{
				request.setAttribute("msg", "error");
			}
		}else{
			request.setAttribute("msg", clientNames);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * CRM客户、ERP客户转换
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addTransfer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String moduleId = getParameter("ModuleId", request);//模板ID
		String viewId = getParameter("viewId", request);//模板ID
		String transferType = getParameter("transferType", request);//转换类型,crmToErp or erpToCrm
		String clientIds = getParameter("clientIds", request);//客户ID
		String existIds = getParameter("existIds", request);//转换存在的客户ID
		String isDetail = getParameter("isDetail", request);//true表示从详情进入，操作成功后异步返回
		String parentClasscode = getParameter("parentClasscode", request);//判断是否有分类classCode。
		if(parentClasscode == null){
			parentClasscode ="";
		}
		
		
		
		LoginBean loginBean = getLoginBean(request);
		Result rs = moduleMgt.detailCrmModule(moduleId);
		ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = loginBean.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do") ;

		
		
		
		//是否启用了审核流
		boolean flowFlag = false;
		if(moduleBean!=null){
			String tableName = moduleBean.getTableInfo().split(":")[0];
			if(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1){
				flowFlag = true;
			}
		}
		
		//启用审核流查看是否有未审核完毕的客户转换
		if(flowFlag && "crmToErp".equals(transferType)){
			String ids = "";
			for(String clientId : clientIds.split(",")){
				ids = "'"+clientId+"',";
			}
			
			if(ids.endsWith(",")){
				ids = ids.substring(0,ids.length()-1);
			}
			String sql = "SELECT id from CRMClientInfo where workFlowNode <> '-1' and id in ("+ids+")";
			Result result = mgt.publicSqlQuery(sql, new ArrayList());
			ArrayList list = (ArrayList)result.retVal;
			if(list!=null && list.size()>0){
				//报错直接关闭弹出框
				EchoMessage.error().add("未审核完毕的客户不可转为ERP客户!").setBackUrl("/CRMClientAction.do?operation=4&ModuleId="+moduleId+"&viewId="+viewId).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
		}
		
		String mainFields ="id,createBy,lastUpdateBy,createTime,lastUpdateTime,SCompanyID,statusId,";
		String childFields = "id,SCompanyID,f_ref,";//公用的字段
		HashMap<String,String> publicMainMap = new HashMap<String, String>();//存放主表公用的值
		publicMainMap.put("createBy", loginBean.getId());
		publicMainMap.put("lastUpdateBy", loginBean.getId());
		publicMainMap.put("createTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		publicMainMap.put("lastUpdateTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		publicMainMap.put("statusId", "0");
		publicMainMap.put("SCompanyID", "00001");
		
		String tableName = "tblCompany";//detail方法拿的表名
		String childTableName = "tblCompanyEmployeeDet";//detail方法map中从表的表名 TABLENAME_childTableName
		if("crmToErp".equals(transferType)){
			tableName = moduleBean.getTableInfo().split(":")[0];
			childTableName = moduleBean.getTableInfo().split(":")[1];
			mainFields += "ComNumber,ClientFlag,workFlowNodeName,workFlowNode,CustomerFlag,isCatalog,moduleType,ComFullNamePYM,classCode,";//公用的字段
			publicMainMap.put("ClientFlag", "2");
			publicMainMap.put("workFlowNodeName","finish");
			publicMainMap.put("workFlowNode", "-1");
			publicMainMap.put("CustomerFlag", "3");
			publicMainMap.put("isCatalog", "0");
			publicMainMap.put("moduleType", "2");
			
			//CRM转ERP还需要插入contactCode
			childFields +="contactCode,";
		}else{
			//erpToCrm
			mainFields += "ClientNo,LastContractTime,ModuleId,departmentCode,";
			publicMainMap.put("LastContractTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			publicMainMap.put("ModuleId",moduleId);
			publicMainMap.put("departmentCode",loginBean.getDepartCode());
		}
		
		
		//处理映射的字段
		for(String str : moduleBean.getTransferFields().split(",")){
			
			int arrIndex = 0;//CRM转ERP,取ERP字段名称,下标为0
			if("erpToCrm".equals(transferType)){
				arrIndex = 1;//ERP转CRM,取CRM字段名称,下标为1
			}
			if(str.indexOf("Contact_") == -1){
				mainFields +=str.split(":")[arrIndex]+",";
			}else{
				str = str.replaceAll("Contact_","");
				childFields +=str.split(":")[arrIndex]+",";
			}
		}
		if(mainFields.endsWith(",")){
			mainFields = mainFields.substring(0,mainFields.length()-1);
		}
		
		//主要处理CRM转ERP,若没有映射性别字段加上此字段
		if("crmToErp".equals(transferType) && (","+childFields).indexOf(",Gender,")==-1){
			childFields +="Gender,";
		}
		
		if(childFields.endsWith(",")){
			childFields = childFields.substring(0,childFields.length()-1);
		}
		
		List<HashMap<String,String>> mainValList = new ArrayList<HashMap<String,String>>();//存放主表字段的值
		
		HashMap<String, List<HashMap<String,String>>> childMap = new HashMap<String, List<HashMap<String,String>>>();
		
		//默认CRM客户转ERP
		int putFieldIndex = 0;//取字段的下标,用于map的put
		int getValueIndex = 1;//取字段的下标,用于map的get
		
		if("erpToCrm".equals(transferType)){
			//ERP客户转CRM
			putFieldIndex = 1;
			getValueIndex = 0;
		}
		//封装插入的字段与存放值MAP
		Result rest = new Result();
		if(clientIds!=null && !"".equals(clientIds)){
			for(String keyId : clientIds.split(",")){
				if(keyId !=null && !"".equals(keyId)){
					rs = dbmgt.detail(tableName, map, keyId, sunClassCode,props,loginBean.getId(),true,null);
					HashMap rsMap = (HashMap)rs.retVal;//获取CRM客户详情值
					if(rsMap !=null){
						HashMap<String,String> clientMap = new HashMap<String, String>();
						clientMap.putAll(publicMainMap);//拷贝主表默认MAP的值
						clientMap.put("id", keyId);//put一个id,tblCompany.id==CRMClientInfo.id
						if("crmToErp".equals(transferType)){
							clientMap.put("ComFullNamePYM",CustomizePYM.getFirstLetter(rsMap.get("ClientName").toString()));//拼音码
							clientMap.put("ComNumber",(String)rsMap.get("ClientNo"));
						}else{
							clientMap.put("ClientNo",(String)rsMap.get("ComNumber"));
						}
						for(String str : moduleBean.getTransferFields().split(",")){
							if(str.indexOf("Contact_") == -1){
								String value = rsMap.get(str.split(":")[getValueIndex]) == null ? "" : rsMap.get(str.split(":")[getValueIndex]).toString();
								clientMap.put(str.split(":")[putFieldIndex],value);//把映射字段的值放入map中
							}
						}
						mainValList.add(clientMap);
						
						//从表操作
						List<HashMap> childList = (ArrayList<HashMap>)rsMap.get("TABLENAME_"+childTableName);
						if(childList!=null && childList.size()>0){
							List<HashMap<String,String>> childValList = new ArrayList<HashMap<String,String>>();//存放联系人字段的值
							for(HashMap childmap : childList){
								HashMap<String,String> tempChildMap = new HashMap<String, String>();
								tempChildMap.put("id", IDGenerater.getId());//加一个id字段,自动生成
								tempChildMap.put("f_ref", keyId);//外键字段,对应clientId
								tempChildMap.put("SCompanyID", "00001");
								if("crmToErp".equals(transferType)){
									tempChildMap.put("contactCode", (String)rsMap.get("ClientNo"));
								}
								for(String str : moduleBean.getTransferFields().split(",")){
									if(str.indexOf("Contact_") > -1){
										str = str.replaceAll("Contact_","");
										String value = childmap.get(str.split(":")[getValueIndex]) == null ? "" : childmap.get(str.split(":")[getValueIndex]).toString();
										tempChildMap.put(str.split(":")[putFieldIndex],value);
									}
								}
								childValList.add(tempChildMap);
							}
							childMap.put(keyId, childValList);
						}
					}
				}
			}
			
			String ipAddress = GlobalsTool.getIpAddr(request);
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			
			rest = mgt.addTransfer(mainFields, childFields, mainValList, childMap,transferType,loginBean,ipAddress,allTables,resources,this.getLocale(request),parentClasscode);
		}
		
		
		//报错直接关闭弹出框
		if (rest.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("noback",true);
		}
		
		
		//详情页面进入，异步返回
		if(isDetail!=null && "true".equals(isDetail)){
			if (rest.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
			return getForward(request, mapping, "blank");
		}
		if (rest.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//return new ActionForward("/CRMClientAction.do?operation=4&type=showTransfer&moduleId="+moduleId+"&viewId="+viewId+"&clientIds="+clientIds+"&existIds="+existIds,true);
			return this.showTransferResult(mapping, form, request, response);
		}else {
       	 	//添加失败
        	SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
        }
		
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 展示客户转换结果
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward showTransferResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String clientIds = getParameter("clientIds", request);//转换的客户ID
		String existIds = getParameter("existIds", request);//转换存在的客户ID
		String moduleId = getParameter("ModuleId", request);//模板ID
		String viewId = getParameter("viewId", request);//视图ID
		int successCount = 0;//成功转入个数
		int existCount = 0;//已存在转入个数
		Result rs = new Result();
		if(clientIds!=null && !"".equals(clientIds)){
			successCount = clientIds.split(",").length;
		}
		if(existIds!=null && !"".equals(existIds)){
			existCount = existIds.split(",").length;
			
			String keyIds = "";
			for(String str : existIds.split(",")){
				keyIds +="'"+str+"',";
			}
			if(keyIds.endsWith(",")){
				keyIds = keyIds.substring(0,keyIds.length()-1);
			}
			rs = mgt.findCompanys(keyIds);//已转入的客户信息
			request.setAttribute("existList",rs.retVal);
		}
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String backUrl = "/CRMClientAction.do?firstEnter=false&ModuleId="+moduleId+"&viewId="+viewId;
			request.setAttribute("successCount",successCount);
			request.setAttribute("existCount",existCount);
			request.setAttribute("backUrl",backUrl);
			return getForward(request, mapping, "showTransfer");
		}else{
			return getForward(request, mapping, "alert");
		}
	}
	
	/**
	 * 客户转换记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward transferRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoginBean loginBean = this.getLoginBean(request);
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
		int transferType = getParameterInt("transferType", request);//转换类型,1表示CRM转ERP，2表示ERP转CRM 
		String clientName = getParameter("clientName",request);//客户名称
		String startTime = getParameter("startTime",request);//查询开始时间
		String endTime = getParameter("endTime",request);//查询结束时间
		String userGroupIds = getParameter("userGroupIds",request);//操作人IDS 
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	if(pageSize ==0){
        		pageSize = 15;
        	}
        }
        
        HashMap<String,String> valueMap = new HashMap<String, String>();
        valueMap.put("transferType",transferType+"");
        valueMap.put("clientName", clientName);
        valueMap.put("startTime", startTime);
        valueMap.put("endTime", endTime);
        valueMap.put("userGroupIds", userGroupIds);
        
		Result rs = mgt.findTransferRecord(loginBean,pageSize,pageNo,valueMap);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("recordList", rs.retVal);
			request.setAttribute("pageBar",pageBar(rs, request)) ;
			request.setAttribute("valueMap",valueMap) ;
			return getForward(request, mapping, "transferRecord");
		}else{
			EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
	}

	
	/**
	 * 客户详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detailNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String clientId=request.getParameter("keyId");
		String moduleId=request.getParameter("moduleId");
		String viewId=request.getParameter("viewId");
		LoginBean loginBean = (LoginBean)request.getSession().getAttribute("LoginBean");
		
		/*
		//判断是否已经加入收藏
		Result isexit = new AttentionMgt().isAttention(loginBean.getId(), clientId, "CRMClientInfo");		
		if(isexit.retCode == ErrorCanst.MULTI_VALUE_ERROR){
			request.setAttribute("attention", "OK");
		}
		*/
		
		//获取模板ID、视图ID信息
		String[] moduleArrInfo = this.getModuleInfo(moduleId, viewId, clientId, loginBean);
		String moduleCheckInfo = this.checkModuleInfo(moduleArrInfo);//检测是否存在模板、视图、客户、没有返回错误
		if("".equals(moduleCheckInfo)){
			moduleId = moduleArrInfo[0];
			viewId = moduleArrInfo[1];
		}else{
			EchoMessage.error().add(moduleCheckInfo).setAlertRequest(request);
			return getForward(request, mapping, "message");
			
		}
		
		//获取模板、视图信息
		Result result=(new ClientSetingMgt()).detailCrmModule(moduleId);
		ClientModuleBean cBean=(ClientModuleBean) result.retVal;
		request.setAttribute("moduleBean", cBean);
		result = moduleMgt.loadModuleView(viewId);
		ClientModuleViewBean moduleViewBean = (ClientModuleViewBean)result.retVal;
		request.setAttribute("moduleViewBean",moduleViewBean);
		
		String tableName = cBean.getTableInfo().split(":")[0];
		String contactTableName = cBean.getTableInfo().split(":")[1];
		
		//审核流处理字段
		HashMap<String,String> workFlowFieldMap = this.getWorkFlowFieldsInfo(tableName, clientId,loginBean);
		
		//获取隐藏字段信息
		List<FieldScopeSetBean> hideFieldScopeList = (List<FieldScopeSetBean>)mgt.findFieidScopes(loginBean.getDepartCode(), loginBean.getId(), "hidden",viewId).retVal;
		
		//获取过滤隐藏字段信息
		result = mgt.queryHideFields(tableName, true, moduleViewBean, hideFieldScopeList,workFlowFieldMap,contactTableName);
		LinkedHashMap<String,List<String>> mainMap = (LinkedHashMap<String,List<String>>)result.retVal;	
		
		result = mgt.queryHideFields(contactTableName, false, moduleViewBean, hideFieldScopeList,workFlowFieldMap,contactTableName);
		LinkedHashMap<String,List<String>> contactMap = (LinkedHashMap<String,List<String>>)result.retVal;	
		
		/*获取客户及联系人的信息*/
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		MOperation mop1 = GlobalsTool.getMOperationMap(request) ;
		
		String isConsole = getParameter("isConsole",request);//如果是坐席台进入。没有权限默认new MOperation()
		if(mop1==null && "true".equals(isConsole)){
			mop1 = new MOperation();
		}
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(loginBean.getId()) ;
        boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString()); 
        String sunClassCode = loginBean.getSunCmpClassCode();  
        Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
        
        
    	String isHistroy = request.getParameter("isHistroy");//true表示从详情--历史记录--查看进入
		Result rs = new Result();
		if(isHistroy !=null && "true".equals(isHistroy)){
			String recordId = request.getParameter("recordId");
			rs = mgt.findClientRecord(recordId);
			ClientInfoRecordBean bean = (ClientInfoRecordBean) rs.retVal;
			HashMap recordMap = gson.fromJson(bean.getMapInfo(), HashMap.class);
			rs.setRetVal(recordMap);
			clientId = bean.getClientId();
		}else{
			rs = dbmgt.detail(cBean.getTableInfo().split(":")[0], allTables, clientId, sunClassCode,props,loginBean.getId(),true,null);
		}
		HashMap rsMap = (HashMap)rs.retVal;
        
        
       // Result rs = dbmgt.detail(cBean.getTableInfo().split(":")[0], allTables, clientId, sunClassCode,props,loginBean.getId(),isLastSunCompany,mop1.getScope(MOperation.M_UPDATE),null);
       // HashMap rsMap = (HashMap)rs.retVal;
        //获取邻居表信息
		ArrayList<DBTableInfoBean> brotherTableList = new DDLOperation().getBrotherTables("CRMClientInfo", allTables);//获取兄弟表信息
		new CRMSalesFlowAction().setErpTableBean(brotherTableList);//附加ERP单据信息
		
		Result rst = new CRMSalesFlowMgt().flowQueryByTableName(tableName);
		ArrayList<Object> flowList = (ArrayList<Object>)rst.retVal;
		if(flowList!=null && flowList.size()>0){
			request.setAttribute("flowList",flowList);
		}
		
		brotherTableList = new ClientSettingAction().checkModuleUse(brotherTableList, request);//过滤未启用的邻居表
		HashMap<String,DBTableInfoBean> moduleMap = new HashMap<String, DBTableInfoBean>();//存放已启用的模板map
		for(DBTableInfoBean tableBean : brotherTableList){
			moduleMap.put(tableBean.getTableName(), tableBean);
		}
		
		
		/*获取客户的打印的权限 */
		HashMap<String,Boolean> scopeMap = mgt.getCRmScope(loginBean,viewId);
		request.setAttribute("scopeMap",scopeMap);

		//反审核按钮
		OAWorkFlowTemplate workFlow=BaseEnv.workFlowInfo.get(tableName);
		if(workFlow!=null&&workFlow.getTemplateClass().equals("00003")&&workFlow.getTemplateStatus()==1 && rsMap.get("workFlowNodeName")!=null && !rsMap.get("workFlowNodeName").toString().equals("draft")){
			if(OnlineUserInfo.getUser(rsMap.get("createBy").toString())!=null){
				boolean flag=dbmgt.isRetCheckPer(loginBean, workFlow, OnlineUserInfo.getUser(rsMap.get("createBy").toString()).getDeptId());
				if(flag){//如果有反审核，显示反审核按钮
					request.setAttribute("retCheckRight", flag);
				}
			}
		}
		
		
		this.getWorkFlowInfo(tableName, request);
		
		
		//判断是否有撤回权限
		checkCancelFlow(tableName, rsMap, loginBean, request);
		
		/*
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/CRMClientAction.do");
		//获取查看单据修改权限的所有职员ID
		this.getAllowUpdateUserIds(mop, request);
		*/
		
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		HashMap<String,String> defineDisMap = this.getDefineDisplay(tableInfoBean, rsMap, loginBean);//获取自定义弹出框展示名称
		
		//获取只读信息
		this.getOnlyReadFields(loginBean, viewId, request,workFlowFieldMap,contactTableName);
		
		Result commonInfoMap = mgt.queryCommonInfoByClientId(clientId, loginBean);//获取详情页面，常用信息(联系记录、日程、任务分派)
		request.setAttribute("commonInfoMap", commonInfoMap.retVal);
		
		String erpCompanyCode = mgt.isExistERPClient(clientId);//判断是否存在ERP客户
		request.setAttribute("erpCompanyCode",erpCompanyCode);
		
		request.setAttribute("tableName", tableName);
		request.setAttribute("contactTableName", contactTableName);
        request.setAttribute("moduleId", moduleId);
        request.setAttribute("viewId", viewId);
        request.setAttribute("mainMap", mainMap);
        request.setAttribute("contactMap", contactMap);
        request.setAttribute("brotherTableList", brotherTableList);
        request.setAttribute("result", rsMap);
        request.setAttribute("loginBean", loginBean);
        request.setAttribute("moduleMap", moduleMap); 
        request.setAttribute("defineDisMap", defineDisMap); 
        request.setAttribute("clientTransferEnter",getParameter("clientTransferEnter",request));//表示线索转移进入
        String isContactEnter = getParameter("isContactEnter", request);//true表示查看联系人详情
        if(isContactEnter!=null && "true".equals(isContactEnter)){
        	 request.setAttribute("isConsole", "true"); 
        	return getForward(request, mapping, "contactDetail");
        }else{
        	return getForward(request, mapping, "detailNew");
        }
	}
	
	/**
	 * 封装导出数据
	 * @param rsMap
	 * @param moduleBean
	 * @param Allcols
	 * @param moduleFieldBeans
	 * @param exportValueList
	 * @param locale
	 * @param request
	 */
	private void setExportValue(HashMap rsMap,ClientModuleBean moduleBean,String[] Allcols,HashMap<String, DBFieldInfoBean> moduleFieldBeans,ArrayList<HashMap> exportValueList,String locale,HttpServletRequest request,LoginBean loginBean){
		//rest = dbmgt.detail(moduleBean.getTableInfo().split(":")[0], map, keyIds[i], sunClassCode,props,loginBean.getId(),true,mop.getScope(MOperation.M_UPDATE),null);
		//HashMap rsMap = (HashMap)rest.retVal;//获取值
		List clientInfoList = (ArrayList)rsMap.get("TABLENAME_"+moduleBean.getTableInfo().split(":")[1]);//得到从表的List
		HashMap valMap = new HashMap();//存放一条客户记录
		List childlist = new ArrayList();//存放从表数据
		HashMap childMap = new HashMap();	
		HashMap tradeMap = (HashMap)request.getSession().getServletContext().getAttribute("workProfessionMap");	
		HashMap districtMap = (HashMap)request.getSession().getServletContext().getAttribute("districtMap");
		HashMap<String,String> defineDisMap = new CRMClientAction().getDefineDisplay(GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[0]), rsMap, loginBean);//获取自定义弹出框展示名称
		for(int j=0;j<Allcols.length;j++){
			if(Allcols[j].indexOf("contact") == -1){
				String fieldVal = "";
				DBFieldInfoBean fieldBean = moduleFieldBeans.get(Allcols[j]);
				if(fieldBean !=null && rsMap.get(Allcols[j]) !=null && !"".equals(rsMap.get(Allcols[j]).toString())){
					if(fieldBean.getInputType() == 1 || fieldBean.getInputType() == 5 || fieldBean.getInputType() == 10){
							fieldVal = GlobalsTool.getEnumerationItemsDisplay(fieldBean.getRefEnumerationName(), rsMap.get(Allcols[j]).toString(),locale);
					}else if(fieldBean.getInputType() == 2){
						fieldVal = defineDisMap.get("popup_"+fieldBean.getFieldName()); 
					}else{
						if("Trade".equals(fieldBean.getFieldName())) {
							fieldVal = (String)tradeMap.get((String)(rsMap.get(Allcols[j])));
						}else if("District".equals(fieldBean.getFieldName())){
							fieldVal = (String)districtMap.get((String)(rsMap.get(Allcols[j])));
						}else if("createBy".equals(fieldBean.getFieldName())){
							fieldVal = rsMap.get("createByName")+"";
						}else{
							fieldVal = rsMap.get(Allcols[j])+"";
						}
					}
					valMap.put(Allcols[j], fieldVal);
				}
			}
		}
		if(clientInfoList!=null && clientInfoList.size()>0){
			HashMap clientMap = new HashMap();
			for(int k=0;k<clientInfoList.size();k++){
				clientMap = (HashMap)clientInfoList.get(k);
				//处理子类的枚举类型
				for(String col : Allcols){
					if(col !=null && !"".equals(col)){
						if(col.indexOf("contact")>-1){
							col = col.replace("contact", "");
							DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[1], col);
							if(fieldBean !=null && clientMap.get(col) !=null && !"".equals(clientMap.get(col).toString())){
								String fieldVal = "";
								if(fieldBean.getInputType() == 1 || fieldBean.getInputType() == 5 || fieldBean.getInputType() == 10){
										fieldVal = GlobalsTool.getEnumerationItemsDisplay(fieldBean.getRefEnumerationName(), clientMap.get(col).toString(),locale);
								}else if(fieldBean.getInputType() == 2){
									fieldVal = defineDisMap.get("popup_"+fieldBean.getFieldName()); 
								}else{
									fieldVal = clientMap.get(col)+"";
								}
								clientMap.put(col, fieldVal);
							}
						}
					}
				}
				childlist.add(clientMap);
			}
		}
		valMap.put("TABLENAME_"+moduleBean.getTableInfo().split(":")[1], childlist);
		exportValueList.add(valMap);
	}
	
	/**
	 * 是否显示附件
	 * @param hideFieldScopeList
	 * @param viewBean
	 * @return
	 */
	public boolean showAttachment(List<FieldScopeSetBean> hideFieldScopeList,ClientModuleViewBean viewBean){
		boolean showAttachment = true;//默认显示
		
		//不包含在显示字段中，不显示
		if(viewBean.getPageFields().indexOf("Attachment")==-1){
			showAttachment = false;
		}
		
		//在隐藏字段中不显示
		if(showAttachment && hideFieldScopeList!=null && hideFieldScopeList.size()>0){
			for(FieldScopeSetBean bean : hideFieldScopeList){
				if(bean.getFieldsName()!=null&&bean.getFieldsName().indexOf("Attachment")>-1){
					showAttachment = false;
				}
			}
		}
		return showAttachment;
	}
	
	/**
	 * 获取审核流信息
	 * @param tableName
	 * @param request
	 */
	public void getWorkFlowInfo(String tableName,HttpServletRequest request){
		//获取审核流信息
		if(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1){
        	request.setAttribute("OAWorkFlow", true);
        	request.setAttribute("currNodeId", 0);
         	request.setAttribute("designId", BaseEnv.workFlowInfo.get(tableName).getId());
        }
	}
	
	/**
	 * 检查唯一性字段信息
	 * @param mainUniFields 主表设置了唯一性的字段
	 * @param childUniFields 从表设置了唯一性的字段
 	 * @param tableInfo 主表：从表信息
	 * @param values 
	 * @param locale
	 * @param operationFlag add添加操作,update是更新操作
	 * @return
	 */
	public String checkUniqueFields(String mainUniFields,String childUniFields,String tableInfo,HashMap values,String locale,String operationFlag,ClientModuleBean moduleBean){
		String tableName = tableInfo.split(":")[0];//主表名
		String childTableName = tableInfo.split(":")[1];//从表名
		String errorInfo = "";//记录唯一性字段:值字符串
		Result rs = new Result();
		String tempVal="";//临时值
		String sql = "";// sql语句
		String returnStr ="";//返回值
		
		//根据主表设置的唯一字段获取相关值
		for(String fieldName : mainUniFields.split(",")){
			if(fieldName!=null && !"".equals(fieldName)){
				tempVal = values.get(fieldName)+"";
				if(!"".equals(tempVal)){
					errorInfo +=fieldName+":"+tempVal+";";
				}
			}
		}
		
		//查询数据库是否有重复值
		if(!"".equals(errorInfo)){
			for(String str : errorInfo.split(";")){
				sql = "SELECT CRMClientInfo.id,ClientName,createBy,isNull("+str.split(":")[0]+",''),CRMClientModule.moduleName,ClientName,CRMClientInfo.createTime from CRMClientInfo left join CRMClientModule on CRMClientInfo.moduleId=CRMClientModule.id WHERE "+str.split(":")[0]+"='"+str.split(":")[1]+"' and moduleId='"+moduleBean.getId()+"'";
				if("update".equals(operationFlag)){
					sql +=" and CRMClientInfo.id != '"+values.get("id").toString()+"'";
				}
				rs = mgt.publicSqlQuery(sql,new ArrayList());
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					return "执行错误"+rs.retVal;
				}
				ArrayList list = (ArrayList)rs.retVal;
				
				if(list!=null && list.size()>0){
					String empFullName = String.valueOf(GlobalsTool.get(list.get(0),2));
					String clientName = String.valueOf(GlobalsTool.get(list.get(0),1));
					String disValues = String.valueOf(GlobalsTool.get(list.get(0),3));
					String moduleName = String.valueOf(GlobalsTool.get(list.get(0),4));
					String createTime = String.valueOf(GlobalsTool.get(list.get(0),6));
					DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(tableName,str.split(":")[0]);
					if(fieldInfoBean.getInputType()==1 || fieldInfoBean.getInputType()==5 || fieldInfoBean.getInputType()==10){
						disValues = GlobalsTool.getEnumerationItemsDisplay(fieldInfoBean.getRefEnumerationName(), disValues, locale);
					}
					//returnStr = fieldInfoBean.getDisplay().get(locale)+"录入的'"+disValues+"'已存在,请修改后提交。(重复数据来自:"+GlobalsTool.getEmpFullNameByUserId(empFullName)+"创建的客户【"+clientName+"】)";
					returnStr = "您录入的"+fieldInfoBean.getDisplay().get(locale)+"'"+disValues+"'与模板【"+moduleName+"】中"+GlobalsTool.getDeptByUserId(empFullName)+"的"+GlobalsTool.getEmpFullNameByUserId(empFullName)+"在"+createTime+"创建的【"+clientName+"】重复";
					break;
				}
			}
		}
		
		errorInfo="";
		if(childUniFields!=null && !"".equals(childUniFields)){
			//处理从表
			ArrayList<HashMap<String,String>> childList = (ArrayList<HashMap<String,String>>)values.get("TABLENAME_"+childTableName);
			if(childList!=null && childList.size()>0){
				for(String fieldName : childUniFields.split(",")){
					if(fieldName!=null && !"".equals(fieldName)){
						tempVal = "";//获取从表字段值
						for(HashMap map : childList){
							if(map.get(fieldName)!=null&&!"".equals(map.get(fieldName))){
								tempVal +="'"+map.get(fieldName)+"',";
							}
						}
						if(tempVal.endsWith(",")){
							tempVal = tempVal.substring(0,tempVal.length()-1);
						}
					}
					if(!"".equals(tempVal)){
						errorInfo +=fieldName+":"+tempVal+";";
					}
				}
				
				if(!"".equals(errorInfo)){
					for(String str : errorInfo.split(";")){
						sql = "SELECT CRMClientInfo.ClientName,CRMClientInfo.createBy,CRMClientInfoDet.UserName,isNull(CRMClientInfoDet."+str.split(":")[0]+",''),CRMClientModule.moduleName,ClientName,CRMClientInfo.createTime from CRMClientInfoDet left join CRMClientInfo on CRMClientInfoDet.f_ref=CRMClientInfo.id left join CRMClientModule on CRMClientInfo.moduleId=CRMClientModule.id  WHERE "+str.split(":")[0]+" in ("+str.split(":")[1]+")";
						if("update".equals(operationFlag)){
							sql +=" and f_ref != '"+values.get("id").toString()+"'";
						}
						rs = mgt.publicSqlQuery(sql,new ArrayList());
						if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
							return "执行错误"+rs.retVal;
						}
						ArrayList list = (ArrayList)rs.retVal;
						if(list!=null && list.size()>0){
							String clientName = String.valueOf(GlobalsTool.get(list.get(0),0));	
							String empFullName = String.valueOf(GlobalsTool.get(list.get(0),1));
							String userName = String.valueOf(GlobalsTool.get(list.get(0),2));	
							String disValues = String.valueOf(GlobalsTool.get(list.get(0),3));
							String moduleName = String.valueOf(GlobalsTool.get(list.get(0),4));
							String createTime = String.valueOf(GlobalsTool.get(list.get(0),6));
							DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(childTableName,str.split(":")[0]);
							if(fieldInfoBean.getInputType()==1 || fieldInfoBean.getInputType()==5 || fieldInfoBean.getInputType()==10){
								disValues = GlobalsTool.getEnumerationItemsDisplay(fieldInfoBean.getRefEnumerationName(), disValues, locale);
							}
							returnStr = "您录入的"+fieldInfoBean.getDisplay().get(locale)+"'"+disValues+"'与模板【"+moduleName+"】中"+GlobalsTool.getDeptByUserId(empFullName)+"的"+GlobalsTool.getEmpFullNameByUserId(empFullName)+"在"+createTime+"创建的【"+clientName+"】联系人重复";
							//returnStr = fieldInfoBean.getDisplay().get(locale)+":'"+disValues+"'已存在,请修改后提交。(重复数据来自:"+GlobalsTool.getEmpFullNameByUserId(empFullName)+"创建的客户【"+clientName+"】联系人:"+userName+")";
							break;
						}
					}
				}
			}
		}
		
		return returnStr;
	}
	
	/**
	 * 发货
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward deliverGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String clientId = getParameter("clientId",request);//客户ID
		String mobileCode = getParameter("mobileCode",request);//手机串码
		String deliverRemark = getParameter("deliverRemark",request);//投递备注
		
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		
		Result rs = mgt.deliverGoods(clientId, mobileCode, deliverRemark, getLoginBean(request), resources, getLocale(request));
		
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * 获取唯一值的字段
	 * @param tableInfo
	 * @return
	 */
	public String getUniqueFIelds(DBTableInfoBean tableInfo){
		String uniFields = "";
		for(DBFieldInfoBean bean : tableInfo.getFieldInfos()){
			if(bean.getIsUnique() == 1){
				uniFields +=bean.getFieldName()+",";
			}
		}
		return uniFields;
	}
	
	/**
	 * 获取查看单据修改权限的所有职员IDS
	 * @param mop
	 * @param request
	 */
	public void getAllowUpdateUserIds(MOperation mop,HttpServletRequest request){
		ArrayList updateScope = this.getAllMopByType(mop, getLoginBean(request), MOperation.M_UPDATE);
		if (updateScope != null && updateScope.size()>0) {
			String allowUpdUserIds = ",";//记录允许更新的userIds
			String updScopeDeptIds = "" ;
			for (Object o : updateScope) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if(lsb!=null && "1".equals(lsb.getFlag())){
					for(String strId : lsb.getScopeValue().split(";")){
						allowUpdUserIds += strId+"," ;
					}
				}
				
				if(lsb!=null && "5".equals(lsb.getFlag())){
					for(String strId : lsb.getScopeValue().split(";")){
						updScopeDeptIds += "departmentCode like '" +strId + "%' or ";
					}
				}
			}
			
			if(!"".equals(updScopeDeptIds)){
				if(updScopeDeptIds.endsWith("or ")){
					updScopeDeptIds = updScopeDeptIds.substring(0,updScopeDeptIds.length()-3);
				}	
				String sql = "SELECT id FROM tblEmployee WHERE "+updScopeDeptIds;
				Result rest = mgt.publicSqlQuery(sql, new ArrayList());
				ArrayList<Object> list = (ArrayList<Object>)rest.retVal;
				if(list!=null && list.size()>0){
					for(Object obj : list){
						allowUpdUserIds += GlobalsTool.get(obj,0)+",";
					}
				}
			}
			request.setAttribute("allowUpdUserIds", allowUpdUserIds);
		}
		
	}
	
	/**
	 * 获取只读字段
	 * @param loginBean
	 * @param viewId
	 * @param request
	 */
	public void getOnlyReadFields(LoginBean loginBean,String viewId,HttpServletRequest request,HashMap<String,String> workFlowFieldMap,String contactTableName){
		
		List<FieldScopeSetBean> readFieldScopeList = (List<FieldScopeSetBean>)mgt.findFieidScopes(loginBean.getDepartCode(), loginBean.getId(), "read",viewId).retVal;
		String readMainFieldsStr = ",";
		String readchildFieldsStr = ",";
		if(readFieldScopeList!=null && readFieldScopeList.size()>0){
			for(FieldScopeSetBean bean : readFieldScopeList){
				String readFieldsName = bean.getFieldsName();
				for(String str : readFieldsName.split(",")){
					if(str.startsWith("contact")){
						str = str.substring(7);
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
					if(str.startsWith(contactTableName)){
						str = str.replace(contactTableName,"");
						readchildFieldsStr +=str+",";
					}else{
						readMainFieldsStr +=str+",";
					}
				}
			}
		}
		
	
		request.setAttribute("readMainFieldsStr",readMainFieldsStr);
		request.setAttribute("readchildFieldsStr",readchildFieldsStr);
	}
	
	/**
	 * 根据mopType获取当前用户的所有权限
	 * @param mop
	 * @param loginBean
	 * @param mopType  权限类型(MOperation.M_QUERY:查询)
	 * @return
	 */
	public ArrayList getAllMopByType(MOperation mop,LoginBean loginBean,int mopType){
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(mop.getScope(mopType));
		scopeRight.addAll(mop.classScope) ;
		ArrayList allScopeList = loginBean.getAllScopeRight();
    	if(allScopeList!=null){        		
    	   scopeRight.addAll(allScopeList) ;
		}
		return scopeRight;
	}
	
	/**
	 * 过滤获取模板信息
	 * @param moduleId
	 * @param viewId
	 * @param clientId
	 * @param loginBean
	 * @return
	 */
	public String[] getModuleInfo(String moduleId,String viewId,String clientId,LoginBean loginBean){
		String[] str = new String[3];
		String moduleIdStr = "";//存放模板ID
		String viewIdStr = "";//存放视图ID
		String clientInfo ="";
		Result rs = new Result();
		
		if(clientId!=null && !"".equals(clientId)){
			rs = mgt.findClientById(clientId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
				if(list!=null && list.size()>0){
					moduleIdStr = String.valueOf(GlobalsTool.get(list.get(0),0));
					viewIdStr = this.getViewIdByModuleId(moduleIdStr, loginBean);
				}
			}else{
				clientInfo ="客户已被删除,信息不存在"; 
			}
		}else{
			if(moduleId==null || "".equals(moduleId)){
				rs = moduleMgt.queryModuleGroupBy(loginBean);
				HashMap<String,Integer> moduleCountMap= (HashMap<String,Integer>)rs.retVal;
				Set keys = moduleCountMap.keySet();
				if(keys != null) {
					Iterator iterator = keys.iterator();
					while(iterator.hasNext()) {
						Object key = iterator.next();
						int viewCount = moduleCountMap.get(key);
						if(viewCount>0){
							moduleIdStr = String.valueOf(key);
							break;
						}
					}
				}
				viewIdStr = this.getViewIdByModuleId(moduleIdStr, loginBean);
			}else{
				moduleIdStr = moduleId;
				if(viewId==null || "".equals(viewId)){
					viewIdStr = this.getViewIdByModuleId(moduleIdStr, loginBean);
				}else{
					viewIdStr = viewId;
				}
			}
		}
		
		str[0]=moduleIdStr;
		str[1]=viewIdStr;
		str[2]=clientInfo;
		return str;
	}
	
	/**
	 * 查找视图信息
	 * @param moduleId
	 * @param loginBean
	 * @return
	 */
	public String getViewIdByModuleId(String moduleId,LoginBean loginBean){
		String viewId="";
		Result rs = moduleMgt.queryModuleViewsByModuleId(loginBean,moduleId);//根据moduleId查看视图权限
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<ClientModuleViewBean> viewList = (ArrayList<ClientModuleViewBean>)rs.retVal;
			if(viewList!=null && viewList.size()>0){
				viewId = viewList.get(0).getId();
			}
		}
		return viewId;
	}
	
	/**
	 * 判断模板数组信息是否有数据
	 * @param moduleInfo
	 * @return
	 */
	public String checkModuleInfo(String[] moduleInfo){
		String str ="";
		if("".equals(moduleInfo[0])){
			str = "没有查看模板权限";
		}else if("".equals(moduleInfo[1])){
			str = "没有查看视图权限";
		}else if(!"".equals(moduleInfo[2])){
			str = moduleInfo[2];
		}
		
		return str;
	}
	
	/**
	 * 检测关键字
	 * @param moduleBean
	 * @param values
	 * @param clientId
	 * @return
	 */
	public String checkModuleKeyWork(ClientModuleBean moduleBean,HashMap values,String clientId){
		String retVal="";
		if("1".equals(moduleBean.getKeyWordStatus())){
			ArrayList param = new ArrayList();
			String clientName = String.valueOf(values.get("ClientName"));
			String sql = "SELECT clientName,Keywords,createBy,CRMClientModule.moduleName,CRMClientInfo.createTime FROM CRMClientInfo LEFT JOIN CRMClientModule on CRMClientInfo.moduleId = CRMClientModule.id WHERE '"+clientName+"' like '%'+Keywords+'%' and moduleId = ? and isNull(Keywords,'') !=''";
			param.add(moduleBean.getId());
			if(clientId!=null && !"".equals(clientId)){
				sql += " and id <> ?";
				param.add(clientId);
			}
			Result rs = mgt.publicSqlQuery(sql, param);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
				if(list!=null && list.size()>0){
					retVal ="在【"+GlobalsTool.get(list.get(0),3)+"】模板下"+GlobalsTool.getDeptByUserId(String.valueOf(GlobalsTool.get(list.get(0),2)))+"的"+GlobalsTool.getEmpFullNameByUserId(String.valueOf(GlobalsTool.get(list.get(0),2)))+"在"+GlobalsTool.get(list.get(0),4)+"创建的客户【"+GlobalsTool.get(list.get(0),0)+"】设置了关键字:"+GlobalsTool.get(list.get(0),1)+",客户名称不得包含此内容，请修改";
				}
			}
		}
		return retVal;
	}
	
	/**
	 * 检查是否有管辖全公司权限
	 * @param scopeRight
	 * @return
	 */
	public String checkExistAllDeptScope(ArrayList scopeRight){
		String retStr = "";
		if (scopeRight != null && scopeRight.size()>0) {
			for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if(lsb!=null && "5".equals(lsb.getFlag())){
					for(String strId : lsb.getScopeValue().split(";")){
						if("ALL".equals(strId)){
							retStr = "ALL";
							break;
						}
					}
					if("ALL".equals(retStr)){
						break;
					}
				}
			}
		}
		return retStr;
	}
	
	/**
	 * 获取审核流当前节点设置字段信息
	 * @param tableName
	 * @param keyId
	 * @return
	 */
	public HashMap<String,String> getWorkFlowFieldsInfo(String tableName,String keyId,LoginBean loginbean){
		HashMap<String,String> map = new HashMap<String, String>();
		OAMyWorkFlowMgt oamgt=new OAMyWorkFlowMgt();
		OAWorkFlowTemplate workFlow=BaseEnv.workFlowInfo.get(tableName);
		String workFlowReadFields="";//只读字段
		String workFlowHideFields="";//隐藏字段
		String workFlowNotNullFields="";//必填字段
		String currentNode = "";//当前节点
		Boolean isGetFields = false;//是否获取字段
		if(workFlow!=null&&workFlow.getTemplateStatus()==1){
			if(keyId==null|| "".equals(keyId)){
				//新增时，还没有转下去
				currentNode = "0";
				isGetFields = true;
			}else{
				//修改操作
//				if(flowMap!=null && !"0".equals(String.valueOf(flowMap.get("currentNode")))){
//					isGetFields = true;
//				}
				String userLastNode = "";
				Result rst=oamgt.getUserLastNode(keyId,loginbean.getId(),tableName);
        		if(rst.retCode==ErrorCanst.DEFAULT_SUCCESS&&rst.retVal!=null){
        			userLastNode=rst.retVal.toString();
        		}
        		
        		if(userLastNode!=null && !"".equals(userLastNode)){
        			currentNode = userLastNode;
        		}else{
        			HashMap flowMap = oamgt.getOAMyWorkFlowInfo(String.valueOf(keyId),tableName);
    				currentNode = String.valueOf(flowMap.get("currentNode"));
        		} 
        		
        		isGetFields = true;
			}
			
			if(true){
				WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(workFlow.getId());
				if(designBean.getFlowNodeMap().get(currentNode)!=null){
					List<FieldBean> flowList = designBean.getFlowNodeMap().get(currentNode).getFields();
					for(FieldBean bean : flowList){
						if(bean.isNotNull()){
							workFlowNotNullFields += bean.getFieldName()+",";//非空
						}else if(bean.getInputType() == 8){
							workFlowReadFields += bean.getFieldName()+",";//只读
						}else if(bean.getInputType() == 3){
							workFlowHideFields += bean.getFieldName()+",";//隐藏
						}
					}
				}
			}
			
		}
		map.put("read",workFlowReadFields);
		map.put("hide",workFlowHideFields);
		map.put("notNull",workFlowNotNullFields);
		return map;
	}
	
	/**
	 * 判断是否有撤回权限
	 * @param tableName 表名
	 * @param rsMap valueMap
	 * @param loginBean
	 * @param request
	 */
	public void checkCancelFlow(String tableName,HashMap rsMap,LoginBean loginBean,HttpServletRequest request){
		//判断是否有撤回权限
		if(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1){
			String designId = BaseEnv.workFlowInfo.get(tableName).getId();
			WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
			
			HashMap flowMap = new OAMyWorkFlowMgt().getOAMyWorkFlowInfo(String.valueOf(rsMap.get("id")),tableName);
			 
			if (designBean != null) {
							
				//判断是否具有撤回按钮
            	String lastCheckPerson = String.valueOf(flowMap.get("lastCheckPerson"));
            	String currentNode = String.valueOf(flowMap.get("currentNode"));
            	FlowNodeBean flowNode = BaseEnv.workFlowDesignBeans.get(designId).getFlowNodeMap().get(currentNode);
            	if(loginBean.getId().equals(lastCheckPerson) && flowNode!=null && flowNode.isAllowCancel()){
            		request.setAttribute("allowCancel", true);
            	}
				
			}
        }
		
	}
}
