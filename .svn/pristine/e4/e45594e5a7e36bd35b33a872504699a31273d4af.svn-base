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
 * <p>Title:工作流转交</p> 
 * <p>Description: </p>
 *
 * @Date:Nov 5, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class OAMyWorkFlowAction extends MgtBaseAction {

	/**
	 * exe 控制器入口函数
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

		// 跟据不同操作类型分配给不同函数处理
		String noback=request.getParameter("noback");//不能返回
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		int operation = getOperation(request);
		/*是否添加body2head.js*/		
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
					//对于死结点，重置功能
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
	 * 查看某个工作流的明细记录，并通过flash显示
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
	 * 死结点，重置到开始结点
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
				EchoMessage.info().add("重置失败").setBackUrl(backUrl).
                	setAlertRequest(request);
			}else{
				String backUrl = "/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type="+type+"&&winCurIndex="+request.getParameter("winCurIndex") ;
				EchoMessage.success().add("重置成功").setBackUrl(backUrl).
                	setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 查看某个工作流的明细记录，并通过flash显示
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
	 * 删除，或者还原工作流(修改工作流的状态)
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
			//记录日志
			ArrayList<String[]> list = (ArrayList<String[]>)rs.retVal;
			for(String[] ss:list){
				if("-1".equals(statusId)){
					new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), ss[2]+";Id:"+ss[0]+";", ss[3], ss[1],"");
				}else{
					new DynDBManager().addLog(14, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), ss[2]+";Id:"+ss[0]+";", ss[3], ss[1],"还原");
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
				error = "-1".equals(statusId)?"删除失败":"还原失败";
			}
			EchoMessage.error().add(error).setBackUrl("/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type=all&&winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
		}

		return getForward(request, mapping, "message");
	}
	/**
	 * 删除工作流
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
		//从回收站中删除，还是回回收站列表.zxy
		String backUrl = "/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&type=delete&&winCurIndex="+request.getParameter("winCurIndex") ;
		//String backUrl = "/OAMyWorkFlow.do?operation="+OperationConst.OP_QUERY+"&approveStatus="+approveStatus+"&&winCurIndex="+request.getParameter("winCurIndex") ;
		if("transing".equals(approveStatus)){
			/*判断当前结点是否是开始结点*/
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
		
		
		
		/*单据编号处理 ，删除工作流时，如发现有单据编号，需要处理单据编号*/
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
			//记录日志
			ArrayList<String[]> list = (ArrayList<String[]>)rs.retVal;
			for(String[] ss:list){				
				new DynDBManager().addLog(14, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), ss[2], ss[3], ss[1],"销毁");
			}
			EchoMessage.success().add(this.getMessage(request, "oa.bbs.operationOk")).setBackUrl(backUrl).
			setAlertRequest(request);
		}else{
			String error  = rs.retVal+"";
			if(error.length() ==0){
				error = "销毁失败";
			}
			EchoMessage.error().add(error).setBackUrl(backUrl).
                    setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}

	/**
	 * 我的工作流首页
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
		//查询是否存在代理
		Result result = mgt.existConsign(getLoginBean(request).getId());
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)result.retVal).size()>0){
			request.setAttribute("existConsign", "true");
		}
		if("all".equals(type)||"delete".equals(type)){//工作流监控
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
		}else{//我的工作流	
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
			
			//所有我能查看的单据，我都有办理和查看详情的权限
			ArrayList<MyWorkFlow> list=(ArrayList<MyWorkFlow>)rs.getRetVal();
			for(int i=0;list!=null&&i<list.size();i++){
				MyWorkFlow myFlow=list.get(i);
				String moduleUrl="/UserFunctionQueryAction.do?tableName="+myFlow.getTableName();
				if(this.getLoginBean(request).getOperationMap().get(moduleUrl)==null){
					MOperation mop=new MOperation();
	        		mop.moduleUrl=moduleUrl;
	        		String moduleId=getModuleId(moduleUrl,BaseEnv.allModule);
	        		if(moduleId != null && moduleId.length() > 0){ //如果是有菜单的自定义单据，应取菜单的模块ID,否则取工作流ID
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
		if("".equals(workFlowForm.getFlowBelong()) || workFlowForm.getFlowBelong()==null){   //工作流监控页面默认显示全部
			workFlowForm.setFlowBelong("all");
		}
		request.setAttribute("workFlowForm", workFlowForm) ;
		return forwrd;
	}
	

	
	
	public ActionForward vildatePeriod(ActionMapping mapping,String tableName,String keyId,HttpServletRequest request) throws Exception{
		DBTableInfoBean tableInfo=(DBTableInfoBean)BaseEnv.tableInfos.get(tableName);
		String sysParamter = tableInfo.getSysParameter();//表信息系统参数
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
	 * 通过解析工作流对象确定转交界面显示数据
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
		String keyIdOne = keyIdAll.split(",")[0];//如果一次传入多张单据，以第一张单做基准来显示审核信息，在转交处理时要校验结点，审核人是否一致
		String tableName = request.getParameter("tableName");
		
		ArrayList<FlowNodeBean> nodeBeans=new ArrayList<FlowNodeBean>();
		String fromPage = getParameter("fromPage", request) ;
		LoginBean loginBean=this.getLoginBean(request);
		
		request.setAttribute("type", request.getParameter("type"));
		
		//检查是否是会计期间前的数据(检查所有的单据)
		if(tableName!=null&&!"".equals(tableName)){
			String kis[] = keyIdAll.split(",");
			for(String ki:kis){//一个个校验单据是否会计期前前数据
				ActionForward forward=this.vildatePeriod(mapping, tableName, ki, request);
				if(forward!=null){
					return forward;
				}
			}
		}
		//当单据转交时，判断单据是否已经审核完毕，如果审核完毕不允许执行转交操作,如果审核人没有当前审核人也不允许执行此操作
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
    	
    	/*查看当前审核人里有没有委托给我的*/
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
    	//本参数只有在消息连接中出现，单据中链接不传入此参数
    	String outNodeId = request.getParameter("currNodeId");
    	if(outNodeId != null && outNodeId.length() > 0 && !currentNode.equals(outNodeId)){
    		//开始结点转交后，对方打开消息未处理，然后制单人撤回开始结点，对方再继续处理消息时从外部传入的当前结点id和数据库记录的当前结点不一致，需报错
    		request.setAttribute("noback", true) ;
        	EchoMessage.error().add("此单当前结点发生变化，可能原因有撤回，中止，或其它人已审核此结点").setcomeAdd(1).setAlertRequest(request);
        	return getForward(request, mapping, "commonMessage");
    	} 
    	
    	//!"0".equals(currentNode) 这个条件不能被去掉。因为在开始结点单据权限和现有权限一至，即有此单修改权限的在开始结点可以转交单据
    	if(strCheckPersons.length()>0 && !strCheckPersons.contains(";"+loginBean.getId()+";")&& !"0".equals(currentNode)){
        	request.setAttribute("noback", true) ;
        	EchoMessage.error().add("您不是此单的审核人").setcomeAdd(1).setAlertRequest(request);
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
		for(String ki:kis){//一个个记录此用户开始办理此工作流的时间
			mgt.transactStart(currentNode, ki, this.getLoginBean(request),tableName);
		}

		String[] nextNodeIds=nextNodes.split(";");
		request.setAttribute("department", department);
		FlowNodeBean currNB=bean.getFlowNodeMap().get(currentNode);		 
		
		//解析节点的审核人
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
		//查看当前节点如果有回退功能，那么查询之前所有审核过的节点及这些节点的审核人
		if(currNB.isAllowBack()){ 
			mgt.getBackNodeBean(myflowId, currNB, nodeBeans, bean);
		}		
		
		//拿当前单据的所有会签意见
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
		//如果来自微信的请求，返回以下内容
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
	 * 处理用户转交数据
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward deliverTo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String keyIdAll = request.getParameter("keyId");				/*单据ID*/
		String nextStep = request.getParameter("nextStep");		/*下个审批结点*/
		String currNode = request.getParameter("currNode");		/*当前审批结点*/
		String designId = request.getParameter("designId");		/*流程ID*/
		String type = request.getParameter("type");				/*审批类型*/
		String tableName = request.getParameter("tableName") ;	/*当前表单名称*/
		String checkFlag = getParameter("checkFlag", request);
		
		String msg = this.getMessage(request,"oa.msg.deliverToSucc");
		request.setAttribute("isWorkFlowEnter", "true");	//用于处理EchoMessage.java,setRequest()方法中判断是从工作流进入,true表示是
		String checkBackUrl = "";							/*审批返回路径*/
		
		LoginBean loginBean = this.getLoginBean(request);
		
		
		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(designId);
        
        /*设置ERP单据的返回路径*/
		String approveStatus = "transing";
		if("true".equals(checkFlag)){
			checkBackUrl = "/UserFunctionAction.do?tableName="+getParameter("tableName", request)+"&keyId="+getParameter("keyId", request)+"&moduleType="+getParameter("moduleType", request)+"&f_brother="+getParameter("f_brother", request)+"&winCurIndex="+getParameter("winCurIndex", request)+"&pageNo="+getParameter("pageNo", request)+"&parentCode="+getParameter("parentCode", request)+"&parentTableName="+getParameter("parentTableName", request)+"&saveDraft="+getParameter("saveDraft", request);
			request.setAttribute("checkFlag",checkFlag);
			request.setAttribute("checkBackUrl",checkBackUrl);
		}
		int deliveSuccess=0;
		/*以下各种类型的审批操作 批量转交,撤回，转交，回退*/
		Result rs = new Result();
		if("cancel".equals(nextStep)){//撤回
			
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
						
			/**转交，回退*/
			String[]checkPersons = getParameters("checkPerson",request);	//选择的审批人
			if((checkPersons==null || checkPersons.length==0) && !"-1".equals(nextStep)){
				EchoMessage.info().add("审批人不能为空！").setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
			
			String mailAttaches = request.getParameter("attachFiles");		// 附件
			mailAttaches = mailAttaches== null?"":mailAttaches;
			//需删除的附件
			String delFiles = request.getParameter("delFiles");
			//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
			String[] dels = delFiles==null?new String[0]:delFiles.split(";");
			if(tableName!=null && tableName.length()>0){
				for(String del:dels){
					if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
						File aFile = new File(BaseEnv.FILESERVERPATH + "/affix/"+ tableName + "/" + del);
						aFile.delete(); 
					}
				}
			}
			String deliverance = getParameter("deliverance",request);		//审批意见
			deliverance = deliverance==null?"":deliverance ;
			String userIds = getParameter("popedomUserIds", request);		//分发提醒人
			String deptIds = getParameter("popedomDeptIds", request);		//分发提醒部门
			String oaTimeLimit = request.getParameter("oaTimeLimit");			//限时数字 如：10小时、10天 
			String oaTimeLimitUnit = request.getParameter("oaTimeLimitUnit");	//限时单位
			String appendWake = deliverance ;
			//转交，更新后台下个结点信息
			for(String keyId : keyIdAll.split(",")){
				if(keyId!= null && keyId.length() > 0){
					rs = mgt.deliverTo(keyId, nextStep, checkPersons,currNode,getLoginBean(request),designId,
							getLocale(request),deliverance,mailAttaches,oaTimeLimit,oaTimeLimitUnit,appendWake,
							getResources(request),userIds,deptIds,type) ;
					
					if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
						/*更新流程描述信息*/
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
				
				String crmCancel = getParameter("crmCancel",request);//true表示CRM撤回进入
				String noback = getParameter("noback", request);
				if("true".equals(noback)){
					echo.setClose().setAlertRequest(request);
				}else if(crmCancel !=null && "true".equals(crmCancel)){
					String isBrother = getParameter("isBrother",request);//true表示CRM邻居表进入
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
							//OA个性化工作流
							echo.setBackUrl("/OAWorkFlowAction.do?tableName="+tableName+"&keyId="+keyIdAll+"&operation=5").setAlertRequest(request);
						}else{
							//自定义工作流
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
			//有执行不成功，但又执行成功数大于0，说明，这是批量审核，有部分成功的
			String batmsg = "批量审核成功"+deliveSuccess+"条，部分单据"+ request.getSession().getAttribute("MESSAGE_CONTENT")+" 批量审核中止";
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
	 * 添加会签意见和附件
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
		// 附件
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
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
			//添加失败
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
