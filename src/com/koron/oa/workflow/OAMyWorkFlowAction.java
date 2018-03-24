package com.koron.oa.workflow;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dbfactory.Result;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.MyWorkFlow;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.wxqy.WeixinApiMgt;
import com.menyi.aio.web.wxqy.bean.BaseResultBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:������ת��</p> 
 * <p>Description: </p>
 *
 * @Date:Nov 5, 2013
 * @Copyright: �����п���������޹�˾
 * @Author ǮС��
 */
public class OAMyWorkFlowAction extends MgtBaseAction {

	/**
	 * exe ��������ں���
	 * 
	 * @param mapping ActionMapping
	 * @param form  ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ���ݲ�ͬ�������ͷ������ͬ��������
		String noback=request.getParameter("noback");//���ܷ���
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		int operation = getOperation(request);
		/*�Ƿ����body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		ActionForward forward = null;
		switch (operation) {
			case OperationConst.OP_QUERY:
				forward = query(mapping, form, request, response);
				break;
			case OperationConst.OP_AUDITING_PREPARE:
				forward = deliverToPrepare(mapping, form, request, response);
				break;
			case OperationConst.Op_AUDITING:
				forward = deliverTo(mapping, form, request, response);
				break;
			case OperationConst.OP_DETAIL:
				forward = flowDepict(mapping, form, request, response); 
				break;
			case OperationConst.OP_UPDATE:
				forward = update(mapping, form, request, response); 
				break;
			case OperationConst.OP_DELETE:
				if("-1".equals(request.getParameter("statusId")) || "0".equals(request.getParameter("statusId"))){
					forward = update(mapping, form, request, response); 
				}else if("reset".equals(getParameter("reset", request))){
					//��������㣬���ù���
					forward = reset(mapping, form, request, response);
				}else{
					forward = delete(mapping, form, request, response); 
				}
				break;
			case OperationConst.OP_ADD:
				forward = addAffix(mapping, form, request, response) ;
				break ;
			case OperationConst.OP_ADD_PREPARE:
				forward = addPrepareAffix(mapping, form, request, response) ;
				break ;
			default:
				forward = query(mapping, form, request, response);
				break ;
		}
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		return forward;
	}
	
	/**
	 * �鿴ĳ������������ϸ��¼����ͨ��flash��ʾ
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward index(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Result result = mgt.existConsign(getLoginBean(request).getId());
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)result.retVal).size()>0){
			request.setAttribute("existConsign", "true");
		}
		return getForward(request, mapping, "index");
	}
	
	/**
	 * ����㣬���õ���ʼ���
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward reset(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String type=request.getParameter("type")==null?"":request.getParameter("type");
		request.setAttribute("type", type);
		
		String[] keyIds = getParameters("keyId", request);
		if(keyIds.length>0){
			Result result = mgt.resetBegin(keyIds,getLoginBean(request));
			if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
				String backUrl = "/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type="+type+"&&winCurIndex="+request.getParameter("winCurIndex") ;
				EchoMessage.info().add("����ʧ��").setBackUrl(backUrl).
                	setAlertRequest(request);
			}else{
				String backUrl = "/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type="+type+"&&winCurIndex="+request.getParameter("winCurIndex") ;
				EchoMessage.success().add("���óɹ�").setBackUrl(backUrl).
                	setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * �鿴ĳ������������ϸ��¼����ͨ��flash��ʾ
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward flowDepict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String keyId=request.getParameter("keyId");
		String applyType=request.getParameter("applyType");		
		String tableName=request.getParameter("tableName");
		
		HashMap flowMap = mgt.getOAMyWorkFlowInfo(keyId, tableName) ;
		if(flowMap!=null && flowMap.get("applyType")!=null){
			applyType = (String) flowMap.get("applyType") ;
		}
		if(tableName==null||"".equals(tableName)){
			tableName=BaseEnv.workFlowInfo.get(applyType).getTemplateFile();
		}
		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(applyType);
		if(template==null){
			template = BaseEnv.workFlowInfo.get(tableName);
		}
		if(template.getLines()!=null && template.getLines().length()>0){
			request.setAttribute("flowNew", true);
		}
		if(flowMap!=null){
			if(template.getLines()!=null && template.getLines().length()>0){
				Result rs1 = mgt.flowDepict(applyType, keyId, tableName);
	        	if(rs1.retCode == ErrorCanst.DEFAULT_SUCCESS){
	        		request.setAttribute("delivers", rs1.getRetVal());
	        	}
	        	rs1 = mgt.flowDepict2(applyType, keyId, this.getResources(request), this.getLocale(request), tableName, String.valueOf(flowMap.get("createBy")));
	        	if(rs1.retCode==ErrorCanst.DEFAULT_SUCCESS){
			        request.setAttribute("flowDepict", rs1.getRetVal());
				}
			}else{
				Result rs =mgt.flowDepict(applyType,keyId,this.getResources(request),this.getLocale(request),tableName, String.valueOf(flowMap.get("createBy"))) ;
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			        request.setAttribute("flowDepict", rs.getRetVal());
				}
			}
		}
		String host = request.getHeader("Host");
        if (host.indexOf(":")< 0) {
        	host = host + ":80";
        } 
        request.setAttribute("ip", host);
        request.setAttribute("designId", applyType);
        request.setAttribute("fileName", template.getWorkFlowFile()==null?applyType:template.getWorkFlowFile());
        request.setAttribute("tableName",tableName);
		return getForward(request, mapping, "workFlowView");
	}
	/**
	 * ɾ�������߻�ԭ������(�޸Ĺ�������״̬)
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] keyId=request.getParameterValues("keyId");
		String statusId=request.getParameter("statusId");
		OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
		Result rs =mgt.updateStatus(statusId,keyId) ;
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			//��¼��־
			ArrayList<String[]> list = (ArrayList<String[]>)rs.retVal;
			for(String[] ss:list){
				if("-1".equals(statusId)){
					new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), ss[2]+";Id:"+ss[0]+";", ss[3], ss[1],"");
				}else{
					new DynDBManager().addLog(14, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), ss[2]+";Id:"+ss[0]+";", ss[3], ss[1],"��ԭ");
				}
			}
			
			if(statusId.equals("-1")){
				String ids = "";
				for (String id : keyId) {
					ids += id + ",";
				}
				new AdviceMgt().deleteByRelationId(ids, "");
				EchoMessage.success().add(this.getMessage(request, "common.msg.delSuccess")).setBackUrl("/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type=all&&winCurIndex="+request.getParameter("winCurIndex")).
				setAlertRequest(request);
			}else{
				EchoMessage.success().add(this.getMessage(request, "oa.bbs.operationOk")).setBackUrl("/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type=delete&&winCurIndex="+request.getParameter("winCurIndex")).
				setAlertRequest(request);
			}
		}else{
			String error = rs.retVal+"";
			if(error.length() ==0){
				error = "-1".equals(statusId)?"ɾ��ʧ��":"��ԭʧ��";
			}
			EchoMessage.error().add(error).setBackUrl("/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type=all&&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
		}

		return getForward(request, mapping, "message");
	}
	/**
	 * ɾ��������
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] keyId = request.getParameterValues("keyId");
		String approveStatus = getParameter("approveStatus", request) ;
		OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
		//�ӻ���վ��ɾ�������ǻػ���վ�б�.zxy
		String backUrl = "/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type=delete&&winCurIndex="+request.getParameter("winCurIndex") ;
		//String backUrl = "/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&approveStatus="+approveStatus+"&&winCurIndex="+request.getParameter("winCurIndex") ;
		if("transing".equals(approveStatus)){
			/*�жϵ�ǰ����Ƿ��ǿ�ʼ���*/
			backUrl = "/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&approveStatus=transing&&winCurIndex="+request.getParameter("winCurIndex") ;
			Result result = mgt.startFlow(keyId,getLoginBean(request).getId()) ;
			if(result.retCode == ErrorCanst.DEFAULT_FAILURE){
				EchoMessage.error().add(getMessage(request, "com.delete.faulure.approve"))
					.setBackUrl(backUrl).setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		List<String> nameList=new ArrayList<String>();
		Result rsTab = mgt.queryTableNameById(keyId) ;
		if(rsTab.retCode==ErrorCanst.DEFAULT_SUCCESS){
			nameList=(List) rsTab.retVal;
		}
		
		
		
		/*���ݱ�Ŵ��� ��ɾ��������ʱ���緢���е��ݱ�ţ���Ҫ�����ݱ��*/
		for(int i=0;i<nameList.size();i++){
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(nameList.get(i));
			for(DBFieldInfoBean fieldBean:tableInfo.getFieldInfos()){
				if("BillNo".equals(fieldBean.getFieldIdentityStr())){
					String key=tableInfo.getTableName()+"_"+fieldBean.getFieldName();
					BillNo billno = BillNoManager.find(key);
					if(billno != null && billno.isFillBack()==true){
						Result rsBill=mgt.queryBillVal(keyId[i], tableInfo.getTableName(), fieldBean.getFieldName());
						if(rsBill.retCode==ErrorCanst.DEFAULT_SUCCESS){
							String val=rsBill.retVal.toString();
							BillNo.billNoRemove(key,val);
						}
					}
				}
			}
		}
		
		Result rs  =mgt.delete(keyId) ;
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			//��¼��־
			ArrayList<String[]> list = (ArrayList<String[]>)rs.retVal;
			for(String[] ss:list){				
				new DynDBManager().addLog(14, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), ss[2], ss[3], ss[1],"����");
			}
			EchoMessage.success().add(this.getMessage(request, "oa.bbs.operationOk")).setBackUrl(backUrl).
			setAlertRequest(request);
		}else{
			String error  = rs.retVal+"";
			if(error.length() ==0){
				error = "����ʧ��";
			}
			EchoMessage.error().add(error).setBackUrl(backUrl).
                    setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}

	/**
	 * �ҵĹ�������ҳ
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
		String type=request.getParameter("type")==null?"":request.getParameter("type");
		request.setAttribute("type", type);
		Result rs;
		ActionForward forwrd;
		OAMyWorkFlowForm workFlowForm = (OAMyWorkFlowForm) form ;
		if("menu".equals(getParameter("src", request))){
			workFlowForm = new OAMyWorkFlowForm();
		}
		//��ѯ�Ƿ���ڴ���
		Result result = mgt.existConsign(getLoginBean(request).getId());
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)result.retVal).size()>0){
			request.setAttribute("existConsign", "true");
		}
		if("all".equals(type)||"delete".equals(type)){//���������
			rs=mgt.queryAll(workFlowForm,getLoginBean(request),type,this.getLocale(request).toString()) ;
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<MyWorkFlow> flowList=(ArrayList<MyWorkFlow>)rs.retVal;
				ArrayList tableNames=new ArrayList();
				for(int i=0;i<flowList.size();i++){
					MyWorkFlow flow=(MyWorkFlow)flowList.get(i);
					if(flow.getTableName()!=null&&!tableNames.contains(flow.getTableName())){
						tableNames.add(flow.getTableName());
					}
				}
				if(tableNames.size()>0){
					ReportSetMgt reMgt=new ReportSetMgt();
					Result rs2=reMgt.getFormatNameByBillTable(tableNames, this.getLocale(request).toString());
					if(rs2.retCode==ErrorCanst.DEFAULT_SUCCESS){
						Cookie[] coks = request.getCookies();
				        for (int i = 0; i < coks.length; i++) {
				            Cookie cok = coks[i];
				            if (cok.getName().equals("JSESSIONID")) {
				                request.setAttribute("JSESSIONID", cok.getValue());
				                break;
				            }
				        }
						request.setAttribute("styleFiles", rs2.retVal);
					}
				}
			}
			forwrd=getForward(request, mapping, "queryList");
		}else{//�ҵĹ�����	
			String approveStatus = request.getParameter("approveStatus");
			if(approveStatus==null || approveStatus.length()==0){
				approveStatus = "transing";
			}
			rs=mgt.query(workFlowForm,getLoginBean(request).getId(),approveStatus,this.getLocale(request).toString(),"transing".equals(approveStatus)?true:false) ;
			if("true".equals(getParameter("isFlowTh", request))){
				if("true".equals(getParameter("isnum", request))){
					request.setAttribute("msg",rs.realTotal);
					forwrd=getForward(request, mapping, "blank");
				}else{
					forwrd=getForward(request, mapping, "workflowTH");
				}			
			}else{
				forwrd=getForward(request, mapping, "list");
			}			
			request.setAttribute("approveStatus", approveStatus);
		}
		
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			
			//�������ܲ鿴�ĵ��ݣ��Ҷ��а���Ͳ鿴�����Ȩ��
			ArrayList<MyWorkFlow> list=(ArrayList<MyWorkFlow>)rs.getRetVal();
			for(int i=0;list!=null&&i<list.size();i++){
				MyWorkFlow myFlow=list.get(i);
				String moduleUrl="/UserFunctionQueryAction.do?tableName="+myFlow.getTableName();
				if(this.getLoginBean(request).getOperationMap().get(moduleUrl)==null){
					MOperation mop=new MOperation();
	        		mop.moduleUrl=moduleUrl;
	        		String moduleId=getModuleId(moduleUrl,BaseEnv.allModule);
	        		if(moduleId != null && moduleId.length() > 0){ //������в˵����Զ��嵥�ݣ�Ӧȡ�˵���ģ��ID,����ȡ������ID
	        			mop.moduleId = moduleId; 
	        		}else{
	        			mop.moduleId=myFlow.getApplyType();
	        		}
	        		this.getLoginBean(request).getOperationMap().put(mop.moduleUrl, mop);
	        		this.getLoginBean(request).getOperationMapKeyId().put(mop.moduleId, mop);
	                BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
	                BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
	                mop.update=true;
	                mop.print=true;
	                mop.query=true;
	                mop.sendEmail=true;
	                mop.oaWorkFlow=true;
				}
			}
			
			
			
			String host = request.getHeader("Host");
	        if (host.indexOf(":")< 0) {
	        	host = host + ":80";
	        } 
	        
	        request.setAttribute("ip", host);
			request.setAttribute("list",rs.getRetVal()) ;
			request.setAttribute("pageBar", this.pageBar2(rs, request));
		}
		if("".equals(workFlowForm.getFlowBelong()) || workFlowForm.getFlowBelong()==null){   //���������ҳ��Ĭ����ʾȫ��
			workFlowForm.setFlowBelong("all");
		}
		request.setAttribute("workFlowForm", workFlowForm) ;
		return forwrd;
	}
	

	
	
	public ActionForward vildatePeriod(ActionMapping mapping,String tableName,String keyId,HttpServletRequest request) throws Exception{
		DBTableInfoBean tableInfo=(DBTableInfoBean)BaseEnv.tableInfos.get(tableName);
		String sysParamter = tableInfo.getSysParameter();//����Ϣϵͳ����
    	Date time=null;
    	String timeStr="";
    	String workFlowNodeName="";
    	for(int i=0;i<tableInfo.getFieldInfos().size();i++){
    		DBFieldInfoBean bean=(DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
    		if(bean.getDefaultValue()!=null&&"accendnotstart".equals(bean.getFieldIdentityStr())){
    			AIODBManager aioMgt=new AIODBManager();
    			Result rs=aioMgt.sqlList("select "+bean.getFieldName()+",workFlowNodeName from "+tableName+" where id='"+keyId+"'", new ArrayList());
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
                    						.setClose().setAlertRequest(request);
                    	}else{
                    		EchoMessage.error().add(getMessage(request,"com.cantAuditebefbill")).setClose().setAlertRequest(request);
                    	}
                    	return getForward(request, mapping, "commonMessage");
                    }
                    
                }
           }
        }
    	return null;
	}
	
	/**
	 * ͨ����������������ȷ��ת��������ʾ����
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward deliverToPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String keyIdAll=request.getParameter("keyId");
		String keyIdOne = keyIdAll.split(",")[0];//���һ�δ�����ŵ��ݣ��Ե�һ�ŵ�����׼����ʾ�����Ϣ����ת������ʱҪУ���㣬������Ƿ�һ��
		String tableName = request.getParameter("tableName");
		
		ArrayList<FlowNodeBean> nodeBeans=new ArrayList<FlowNodeBean>();
		String fromPage = getParameter("fromPage", request) ;
		LoginBean loginBean=this.getLoginBean(request);
		
		request.setAttribute("type", request.getParameter("type"));
		
		//����Ƿ��ǻ���ڼ�ǰ������(������еĵ���)
		if(tableName!=null&&!"".equals(tableName)){
			String kis[] = keyIdAll.split(",");
			for(String ki:kis){//һ����У�鵥���Ƿ�����ǰǰ����
				ActionForward forward=this.vildatePeriod(mapping, tableName, ki, request);
				if(forward!=null){
					return forward;
				}
			}
		}
		//������ת��ʱ���жϵ����Ƿ��Ѿ������ϣ���������ϲ�����ִ��ת������,��������û�е�ǰ�����Ҳ������ִ�д˲���
    	String designId="";
		String department="";
		String strCheckPersons="";
		String currentNode = "";
		String myflowId = ""; 
		HashMap map=mgt.getOAMyWorkFlowInfo(keyIdOne,tableName);
		if(map!=null){
			designId = String.valueOf(map.get("applyType"));
			department = String.valueOf(map.get("departmentCode"));
			currentNode = String.valueOf(map.get("currentNode"));
			strCheckPersons = String.valueOf(map.get("checkPerson"));
			myflowId = String.valueOf(map.get("id"));
		}
    	
    	/*�鿴��ǰ���������û��ί�и��ҵ�*/
    	HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(loginBean.getId(), designId);
    	for(String person : strCheckPersons.split(";")){
			 if(consignMap.get(person)!=null){
				 strCheckPersons += loginBean.getId()+";";
			 }
		}
    	if("-1".equals(currentNode)){
			request.setAttribute("noback", true) ;
			EchoMessage.error().add(this.getMessage(request, "common.msg.hasAudit")).setcomeAdd(1).setAlertRequest(request);
			return getForward(request, mapping, "commonMessage");
		}
    	//������ֻ������Ϣ�����г��֣����������Ӳ�����˲���
    	String outNodeId = request.getParameter("currNodeId");
    	if(outNodeId != null && outNodeId.length() > 0 && !currentNode.equals(outNodeId)){
    		//��ʼ���ת���󣬶Է�����Ϣδ����Ȼ���Ƶ��˳��ؿ�ʼ��㣬�Է��ټ���������Ϣʱ���ⲿ����ĵ�ǰ���id�����ݿ��¼�ĵ�ǰ��㲻һ�£��豨��
    		request.setAttribute("noback", true) ;
        	EchoMessage.error().add("�˵���ǰ��㷢���仯������ԭ���г��أ���ֹ��������������˴˽��").setcomeAdd(1).setAlertRequest(request);
        	return getForward(request, mapping, "commonMessage");
    	} 
    	
    	//!"0".equals(currentNode) ����������ܱ�ȥ������Ϊ�ڿ�ʼ��㵥��Ȩ�޺�����Ȩ��һ�������д˵��޸�Ȩ�޵��ڿ�ʼ������ת������
    	if(strCheckPersons.length()>0 && !strCheckPersons.contains(";"+loginBean.getId()+";")&& !"0".equals(currentNode)){
        	request.setAttribute("noback", true) ;
        	EchoMessage.error().add("�����Ǵ˵��������").setcomeAdd(1).setAlertRequest(request);
        	return getForward(request, mapping, "commonMessage");
        }
    	
		
		WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(designId);
		if(designBean==null){
			EchoMessage.info().add(this.getMessage(request, "oa.fileflow.error2")).setNoBackButton().setClose().setRequest(request);
			return getForward(request, mapping, "commonMessage");
		}
		String nextNodes="";
		Result rs=mgt.getNextNodeIdByVal(designId, currentNode, keyIdOne,true,loginBean);
		if("".equals(rs.retVal)||rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.info().add(this.getMessage(request, "oa.errormsg.noNode")+rs.retVal)
				.setClose().setBackUrl("/OAMyWorkFlow.do?approveStatus=transing&operation="+OperationConst.OP_QUERY).setClose().setAlertRequest(request);
			return getForward(request, mapping, "commonMessage");
		}else{
			nextNodes=rs.retVal.toString();
		}
		
		WorkFlowDesignBean bean=BaseEnv.workFlowDesignBeans.get(designId);
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(designId);
		
		String kis[] = keyIdAll.split(",");
		for(String ki:kis){//һ������¼���û���ʼ����˹�������ʱ��
			mgt.transactStart(currentNode, ki, this.getLoginBean(request),tableName);
		}

		String[] nextNodeIds=nextNodes.split(";");
		request.setAttribute("department", department);
		FlowNodeBean currNB=bean.getFlowNodeMap().get(currentNode);		 
		
		//�����ڵ�������
		for(int i=0;i<nextNodeIds.length;i++){
			FlowNodeBean nodeBean=bean.getFlowNodeMap().get(nextNodeIds[i]);
			if(nodeBean==null){
				if("erp".equals(fromPage)){
					EchoMessage.info().add(this.getMessage(request, "oa.errormsg.noCheckPerson"))
						.setClose().setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}else{
					EchoMessage.info().add(this.getMessage(request, "oa.errormsg.noNode"))
						.setClose().setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}
			FlowNodeBean nodeBeanNew=new FlowNodeBean();
			nodeBeanNew.setKeyId(nodeBean.getKeyId());
			nodeBeanNew.setDisplay(nodeBean.getDisplay());
			nodeBeanNew.setApprovePeople(nodeBean.getApprovePeople());
			nodeBeanNew.setZAction(nodeBean.getZAction());
			nodeBeanNew.setForwardTime(nodeBean.isForwardTime());
			if(nodeBean.getZAction().equals("CHECK")){
				try{
					ArrayList<String []> checkPersons=mgt.getNodeCheckPerson(nodeBean, department,keyIdOne,null,tableName,loginBean.getId());
					if(checkPersons.size()>0){
						nodeBeanNew.setCheckPeople(checkPersons);
					}else{
						EchoMessage.info().add(this.getMessage(request, "oa.errormsg.noCheckPerson"))
							.setClose().setAlertRequest(request);
						return getForward(request, mapping, "alert");
					} 
				}catch(Exception e){
					EchoMessage.info().add(e.getMessage())
					.setClose().setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}
			nodeBeans.add(nodeBeanNew);
		}		
		//�鿴��ǰ�ڵ�����л��˹��ܣ���ô��ѯ֮ǰ������˹��Ľڵ㼰��Щ�ڵ�������
		if(currNB.isAllowBack()){ 
			mgt.getBackNodeBean(myflowId, currNB, nodeBeans, bean);
		}		
		
		//�õ�ǰ���ݵ����л�ǩ���
    	Result rs1=mgt.getDeliverance(keyIdOne,null,tableName);
    	if(rs1.retCode==ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("delivers", rs1.getRetVal());
    	}
    	if(tableName==null || tableName.length()==0){
    		tableName = workFlow.getTemplateFile() ;
    	}
    	
    	
		request.setAttribute("nextNBs", nodeBeans) ;
		request.setAttribute("currNB", currNB);
		request.setAttribute("ideaRequired", currNB.getIdeaRequired()) ;
		request.setAttribute("fromPage", fromPage);
		request.setAttribute("designId", designId);
		request.setAttribute("moduleName", BaseEnv.workFlowInfo.get(designId).getTemplateName());
		request.setAttribute("keyId", request.getParameter("keyId"));
		request.setAttribute("tableName", tableName) ;
		request.setAttribute("toPage", getParameter("toPage", request)) ;
		request.setAttribute("checkFlag", getParameter("checkFlag", request)) ;
		request.setAttribute("moduleType", getParameter("moduleType", request)) ;
		request.setAttribute("f_brother", getParameter("f_brother", request)) ;
		request.setAttribute("pageNo", getParameter("pageNo", request)) ;
		request.setAttribute("parentCode", getParameter("parentCode", request)) ;
		request.setAttribute("parentTableName", getParameter("parentTableName", request)) ;
		request.setAttribute("saveDraft", getParameter("saveDraft", request)) ;
		request.setAttribute("dispenseWake", workFlow.getDispenseWake());
		//�������΢�ŵ����󣬷�����������
		if(WeixinApiMgt.checkWxqy(request)){
			BaseResultBean baseResultBean = new BaseResultBean();
			Map<String, Object> data =new HashMap<String, Object>();
			data.put("nextNBs", nodeBeans);
			data.put("currNB", currNB);
			data.put("ideaRequired", currNB.getIdeaRequired());
			data.put("fromPage", fromPage);
			data.put("designId", designId);
			data.put("moduleName", BaseEnv.workFlowInfo.get(designId).getTemplateName());
			data.put("keyId", request.getParameter("keyId"));
			data.put("tableName", tableName) ;
			data.put("toPage", getParameter("toPage", request)) ;
			data.put("checkFlag", getParameter("checkFlag", request)) ;
			data.put("moduleType", getParameter("moduleType", request)) ;
			data.put("f_brother", getParameter("f_brother", request)) ;
			data.put("pageNo", getParameter("pageNo", request)) ;
			data.put("parentCode", getParameter("parentCode", request)) ;
			data.put("parentTableName", getParameter("parentTableName", request)) ;
			data.put("saveDraft", getParameter("saveDraft", request)) ;
			data.put("dispenseWake", workFlow.getDispenseWake());
			baseResultBean.SetData(data);
			return WeixinApiMgt.wxqyResponse(request, mapping,baseResultBean);
		}else{
			return getForward(request, mapping, "deliverTo");
		}
	}
	
	/**
	 * �����û�ת������
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward deliverTo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String keyIdAll = request.getParameter("keyId");				/*����ID*/
		String nextStep = request.getParameter("nextStep");		/*�¸��������*/
		String currNode = request.getParameter("currNode");		/*��ǰ�������*/
		String designId = request.getParameter("designId");		/*����ID*/
		String type = request.getParameter("type");				/*��������*/
		String tableName = request.getParameter("tableName") ;	/*��ǰ������*/
		String checkFlag = getParameter("checkFlag", request);
		
		String msg = this.getMessage(request,"oa.msg.deliverToSucc");
		request.setAttribute("isWorkFlowEnter", "true");	//���ڴ���EchoMessage.java,setRequest()�������ж��Ǵӹ���������,true��ʾ��
		String checkBackUrl = "";							/*��������·��*/
		
		LoginBean loginBean = this.getLoginBean(request);
		
		
		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(designId);
        
        /*����ERP���ݵķ���·��*/
		String approveStatus = "transing";
		if("true".equals(checkFlag)){
			checkBackUrl = "/UserFunctionAction.do?tableName="+getParameter("tableName", request)+"&keyId="+getParameter("keyId", request)+"&moduleType="+getParameter("moduleType", request)+"&f_brother="+getParameter("f_brother", request)+"&winCurIndex="+getParameter("winCurIndex", request)+"&pageNo="+getParameter("pageNo", request)+"&parentCode="+getParameter("parentCode", request)+"&parentTableName="+getParameter("parentTableName", request)+"&saveDraft="+getParameter("saveDraft", request);
			request.setAttribute("checkFlag",checkFlag);
			request.setAttribute("checkBackUrl",checkBackUrl);
		}
		int deliveSuccess=0;
		/*���¸������͵��������� ����ת��,���أ�ת��������*/
		Result rs = new Result();
		if("cancel".equals(nextStep)){//����
			
			Result rsDel=mgt.queryDeleteAdvice(keyIdAll, currNode);
			if(rsDel.retCode==ErrorCanst.DEFAULT_SUCCESS){
				String ids = (String) rsDel.getRetVal();
				new AdviceMgt().deleteById(ids);
			}
			String lastNodeId=request.getParameter("lastNodeId");			
			rs = mgt.cancel(keyIdAll, lastNodeId, currNode, this.getLoginBean(request), designId);
			msg=this.getMessage(request, "oa.msg.cancelSucc");
			rs=mgt.updateFlowDepict(designId, keyIdAll,this.getLocale(request));
			approveStatus = "transing2";
		}else{			
						
			/**ת��������*/
			String[]checkPersons = getParameters("checkPerson",request);	//ѡ���������
			if((checkPersons==null || checkPersons.length==0) && !"-1".equals(nextStep)){
				EchoMessage.info().add("�����˲���Ϊ�գ�").setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
			
			String mailAttaches = request.getParameter("attachFiles");		// ����
			mailAttaches = mailAttaches== null?"":mailAttaches;
			//��ɾ���ĸ���
			String delFiles = request.getParameter("delFiles");
			//��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ�� 
			String[] dels = delFiles==null?new String[0]:delFiles.split(";");
			if(tableName!=null && tableName.length()>0){
				for(String del:dels){
					if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
						File aFile = new File(BaseEnv.FILESERVERPATH + "/affix/"+ tableName + "/" + del);
						aFile.delete(); 
					}
				}
			}
			String deliverance = getParameter("deliverance",request);		//�������
			deliverance = deliverance==null?"":deliverance ;
			String userIds = getParameter("popedomUserIds", request);		//�ַ�������
			String deptIds = getParameter("popedomDeptIds", request);		//�ַ����Ѳ���
			String oaTimeLimit = request.getParameter("oaTimeLimit");			//��ʱ���� �磺10Сʱ��10�� 
			String oaTimeLimitUnit = request.getParameter("oaTimeLimitUnit");	//��ʱ��λ
			String appendWake = deliverance ;
			//ת�������º�̨�¸������Ϣ
			for(String keyId : keyIdAll.split(",")){
				if(keyId!= null && keyId.length() > 0){
					rs = mgt.deliverTo(keyId, nextStep, checkPersons,currNode,getLoginBean(request),designId,
							getLocale(request),deliverance,mailAttaches,oaTimeLimit,oaTimeLimitUnit,appendWake,
							getResources(request),userIds,deptIds,type) ;
					
					if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
						/*��������������Ϣ*/
						rs=mgt.updateFlowDepict(designId, keyId,this.getLocale(request));
						deliveSuccess++;
					}else{
						break;
					}
				}
			}
		}
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			String fromAdvice=request.getParameter("fromAdvice")==null?"":request.getParameter("fromAdvice");
			EchoMessage echo = null;
			if(fromAdvice.equals("true")){
				echo=EchoMessage.success().add(msg);
				if(!("deliverToAll").equals(type)&&!("cancel").equals(nextStep)){
					echo.setPopWin(true);
				}
				echo.setBackUrl("/OAMyWorkFlow.do").setAlertRequest(request) ;
			}else{
				echo=EchoMessage.success().add(msg);
				if(!("cancel").equals(nextStep)){
					echo.setPopWin(true);
				}
				
				String crmCancel = getParameter("crmCancel",request);//true��ʾCRM���ؽ���
				String noback = getParameter("noback", request);
				if("true".equals(noback)){
					echo.setClose().setAlertRequest(request);
				}else if(crmCancel !=null && "true".equals(crmCancel)){
					String isBrother = getParameter("isBrother",request);//true��ʾCRM�ھӱ����
					if(isBrother !=null && "true".equals(isBrother)){
						echo.setBackUrl("/CRMBrotherAction.do?&tableName="+tableName+"&operation=5&keyId="+keyIdAll).setAlertRequest(request) ;
					}else{
						String moduleId = getParameter("moduleId",request);
						String viewId = getParameter("viewId",request);
						echo.setBackUrl("/CRMClientAction.do?&operation=5&moduleId="+moduleId+"&viewId="+viewId+"&keyId="+keyIdAll).setAlertRequest(request) ;	
					}
				}else{
					if("cancel".equals(nextStep)){
						echo.setBackUrl("/OAMyWorkFlow.do?approveStatus="+approveStatus+"&operation="+OperationConst.OP_QUERY).setAlertRequest(request) ;
					}else{
						if("1".equals(template.getDesignType())){
							//OA���Ի�������
							echo.setBackUrl("/OAWorkFlowAction.do?tableName="+tableName+"&keyId="+keyIdAll+"&operation=5").setAlertRequest(request);
						}else{
							//�Զ��幤����
							echo.setBackUrl("/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyIdAll+"&operation=5").setAlertRequest(request);
						}
					}
				}
			}
		}else{	
        	SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
		}
		
		if(rs.retCode !=ErrorCanst.DEFAULT_SUCCESS && deliveSuccess > 0){
			//��ִ�в��ɹ�������ִ�гɹ�������0��˵��������������ˣ��в��ֳɹ���
			String batmsg = "������˳ɹ�"+deliveSuccess+"�������ֵ���"+ request.getSession().getAttribute("MESSAGE_CONTENT")+" ���������ֹ";
			EchoMessage echo=EchoMessage.success().add(batmsg).setNotAutoBack();
			if(!("cancel").equals(nextStep)){
				echo.setPopWin(true);
			}
			echo.setAlertRequest(request);
		}
		
		request.setAttribute("isWorkFlowEnter", "true");
		return getForward(request, mapping, "alert") ;
	}
	
	/**
	 * ��ӻ�ǩ����͸���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String keyId = getParameter("keyId", request) ;
		String tableName = getParameter("tableName", request) ;
		String deliverance=request.getParameter("deliverance");
		// ����
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;

		//��ɾ���ĸ���
		String delFiles = request.getParameter("delFiles");
		//��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ�� 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
			if(tableName!=null && tableName.length()>0){
			for(String del:dels){
				if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
					File aFile = new File(BaseEnv.FILESERVERPATH + "/affix/"+ tableName + "/" + del);
					aFile.delete(); 
				}
			}
		}
		Result result = mgt.addAffix(keyId, getLoginBean(request).getSunCmpClassCode(), 
				 						getLoginBean(request), mailAttaches,deliverance,tableName) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
	        request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
		}else{
			//���ʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert") ;
	}
	
	protected ActionForward addPrepareAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String keyId = getParameter("keyId", request) ;
		String tableName = getParameter("tableName", request) ;
		
		request.setAttribute("keyId", keyId) ;
		request.setAttribute("tableName", tableName) ;
		return getForward(request, mapping, "addAffix") ;
	}
	
	
	protected ActionForward doAuth(HttpServletRequest request, ActionMapping mapping) {
		int operation = getOperation(request);
		if(operation == OperationConst.OP_AUDITING_PREPARE 
				|| operation == OperationConst.Op_AUDITING
				|| operation == OperationConst.OP_DETAIL
				|| operation == OperationConst.OP_ADD
				|| operation == OperationConst.OP_ADD_PREPARE){
			return null ;
		}
		return super.doAuth(request, mapping);
	}
	
	
}
