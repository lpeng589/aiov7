package com.koron.oa.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.wxqy.WeixinApiMgt;
import com.menyi.aio.web.wxqy.bean.BaseResultBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;

/**
 * <p>
 * Title:新建，修改表单单据
 * Description:
 * </p>
 * @Date:2013-04-19
 * @CopyRight:科荣软件
 * @Author:李文祥
 */
public class OAWorkFlowAction extends MgtBaseAction {
	OAWorkFlowMgt mgt=new OAWorkFlowMgt();
	OAMyWorkFlowMgt oamgt=new OAMyWorkFlowMgt();
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int operation = getOperation(request);
		String noback=request.getParameter("noback");//不能返回
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			forward = detail(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			String type=request.getParameter("type");
    		if(type!=null&&"hurryTrans".equals(type)){//催办
    			forward = hurryTrans(mapping, form, request, response);
    		}else if("getJson".equals(type)){ //获得html编辑器内容
				forward = getWorkFlowJson(mapping, form, request, response);
			}else{
				forward = updatePrepare(mapping, form, request, response);
			}
			break;
		}
		return forward;
	}
	
	
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
	 * 打开添加工作流表单页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping,ActionForm form,
             	HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		String tableName = getParameter("tableName", request);
		String type=request.getParameter("type");
		 
		String designId=request.getParameter("designId"); //工作流流程id
		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(tableName);
		if(template == null){
			EchoMessage.info().add(this.getMessage(request, "oa.fileflow.error2")).setClose().setRequest(request);
			return getForward(request, mapping, "commonMessage");
		}
		
		if(designId==null){
			designId = template.getId();
		}
		WorkFlowDesignBean design = BaseEnv.workFlowDesignBeans.get(designId);
		if((design==null || design.getFlowNodeMap().size()==0) && !"viewFlow".equals(type)){
			EchoMessage.info().add(this.getMessage(request, "oa.fileflow.error2")).setClose().setRequest(request);
			return getForward(request, mapping, "commonMessage");
		}
        request.setAttribute("designId", designId);
        String layOutHtml="";
        HashMap htmlMap = mgt.getNewTable(tableName,designId);
        if(htmlMap != null){
			layOutHtml = String.valueOf(htmlMap.get("layOutHtml"));
        }else{
        	System.out.println("获取表单内容失败");
        }
        
        WorkFlowDesignBean workFlowBean=BaseEnv.workFlowDesignBeans.get(designId);
        if(workFlowBean!=null){
	        HashMap<String,FlowNodeBean> flowNodeMap =workFlowBean.getFlowNodeMap();
	        FlowNodeBean flowNodeBean=flowNodeMap.get("0");
	        List<FieldBean> fields = flowNodeBean.getFields();
	        if(fields!=null && fields.size()>0){
		        List<String> delFields=new ArrayList<String>();
		        for(FieldBean field : fields){
		        	DBFieldInfoBean dbField = GlobalsTool.getFieldBean(tableName, field.getFieldName());
		        	if(dbField == null){
		        		delFields.add(field.getFieldName());
		        	}
		        }
		        if(delFields.size()>0){
			        for(String delId:delFields){
			        	fields.remove(delId);
			        }
		        }
		    }
	        request.setAttribute("workFlowFields", fields); //获取流程设计中的字段设置
        }
    	request.setAttribute("tableCHName", template.getTemplateName());
        request.setAttribute("addType", type); //判断是否预览表单还是添加工作流
        request.setAttribute("layOutHtml", layOutHtml);
        
        request.setAttribute("tableInfo", GlobalsTool.getTableInfoBean(tableName));
		return getForward(request, mapping, "add");
	}
	/**添加单据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward add(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String designId=request.getParameter("designId");
		WorkFlowDesignBean design = BaseEnv.workFlowDesignBeans.get(designId);
		if(design==null || design.getFlowNodeMap().size()==0){
			EchoMessage.info().add(this.getMessage(request, "oa.fileflow.error2")).setClose().setRequest(request);
			return getForward(request, mapping, "commonMessage");
		}
		String tableName = getParameter("tableName", request); // 得到表名
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
    	HashMap<String,String> tableMap=getTableMap(tableInfo,"add",request,response);
    	LoginBean loginBean=this.getLoginBean(request);
    	Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		String deliverTo=request.getParameter("deliverTo");
		String buttonVal=request.getParameter("button");
		String backUrl="";
    	if(tableMap!=null){
	    	Result rs=mgt.executeAddSQL(tableInfo,tableMap, tableName,loginBean,resources,this.getLocale(request),deliverTo);
	    	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    		if(!"deliverTo".equals(buttonVal)){ //暂存
	    			backUrl = "/OAWorkFlowAction.do?tableName="+tableName+"&keyId="+tableMap.get("id")+"&operation=7";
	    		}else{ //保存后跳转到转交界面
	    			request.setAttribute("directJump", true);
	           		request.setAttribute("from", "add");
	    			String retUrl="/OAWorkFlowAction.do?tableName="+tableName+"&keyId="+tableMap.get("id")+"&operation=5";
	    			request.setAttribute("retValUrl", retUrl);
	    			backUrl="/OAMyWorkFlow.do?keyId="+tableMap.get("id")+"&currNodeId=0&tableName="+tableName+"&designId="+designId+"&operation="+OperationConst.OP_AUDITING_PREPARE;       			
	    		}
	    		EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl(backUrl).setAlertRequest(request);       			
	    	}else if(rs.getRetCode() == ErrorCanst.WORK_FLOW_NO_NEXT_NODE){
            	EchoMessage.error().add("找不到下一个工作流审核结点，请检查结点或条件是否正确！").setAlertRequest(request);
	    	}else if(rs.getRetCode() == ErrorCanst.BILL_ADD_WORK_FLOW_ERROR){
            	EchoMessage.error().add(getMessage(request, "com.add.workfow.error")).setAlertRequest(request);
	    	}else{
				// 添加失败
				EchoMessage.error().add(
						getMessage(request, "common.msg.addFailture"))
						.setAlertRequest(request);
	    	}
    	}
    	if(WeixinApiMgt.checkWxqy(request)){
    		BaseResultBean baseResultBean=new BaseResultBean();
    		baseResultBean.addData("backurl", backUrl);
    		return WeixinApiMgt.wxqyResponse(request, mapping, baseResultBean);
    	}else {
    		return getForward(request, mapping, "message");
		}	
	}
	
	/**
	 * 获取页面表单内容数据,修改和添加都要调用此方法
	 * @param tableInfo
	 * @param operateType 操作类型， add为添加操作，update为修改操作
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,String> getTableMap(DBTableInfoBean tableInfo,String operateType ,HttpServletRequest request,
								HttpServletResponse response) throws Exception{
		HashMap<String,String> tableMap=new HashMap<String,String>();
		for(int i=0;i<tableInfo.getFieldInfos().size();i++){
			DBFieldInfoBean fieldBean=tableInfo.getFieldInfos().get(i);
			String fieldValue="";
			if("update".equals(operateType)){ //修改工作流时需要过滤一些字段
				if(fieldBean.getFieldName().startsWith("field_") || "id".equals(fieldBean.getFieldName())){
					if(fieldBean!=null){
						if(fieldBean.getDefaultValue()!=null && fieldBean.getDefaultValue().length()>0 
								&& ("checkbox".equals(fieldBean.getDefaultValue()) 
								|| "img".equals(fieldBean.getDefaultValue()) 
								|| "affix".equals(fieldBean.getDefaultValue()))){
							String chkList[] = request.getParameterValues(fieldBean.getFieldName());
							if(chkList!=null && chkList.length>0){
								for(String ch:chkList){
									fieldValue+=ch+";";
								}
							}
						}else{
							fieldValue= request.getParameter(fieldBean.getFieldName());
						}
						tableMap.put(fieldBean.getFieldName(), fieldValue);
					}
				}
			}else{
				
				if(fieldBean!=null && "BillNo".equals(fieldBean.getFieldIdentityStr())){
					
					
				}else{
					if(fieldBean!=null){
						if(fieldBean.getDefaultValue()!=null && fieldBean.getDefaultValue().length()>0 
								&& ("checkbox".equals(fieldBean.getDefaultValue()) 
								|| "img".equals(fieldBean.getDefaultValue()) 
								|| "affix".equals(fieldBean.getDefaultValue()))){
							String chkList[] = request.getParameterValues(fieldBean.getFieldName());
							if(chkList!=null && chkList.length>0){
								for(String ch:chkList){
									fieldValue+=ch+";";
								}
							}
						}else{
							fieldValue= request.getParameter(fieldBean.getFieldName());
						}
					}
					tableMap.put(fieldBean.getFieldName(), fieldValue);
				}
			}
			if( !"BillNo".equals(fieldBean.getFieldIdentityStr())){
				if("add".equals(operateType)){ //增加
					if("id".equals(fieldBean.getFieldName()) ){
						fieldValue=IDGenerater.getId();
					}else if("createBy".equals(fieldBean.getFieldName())){
						fieldValue=this.getLoginBean(request).getId();
					}else if("createTime".equals(fieldBean.getFieldName())){
						fieldValue=BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
					}else if("lastUpdateBy".equals(fieldBean.getFieldName())){
						fieldValue=this.getLoginBean(request).getId();
						tableMap.put(fieldBean.getFieldName(), fieldValue);
					}
					tableMap.put(fieldBean.getFieldName(), fieldValue);
				}else { //修改
					if("lastUpdateBy".equals(fieldBean.getFieldName())){
						fieldValue=this.getLoginBean(request).getId();
						tableMap.put(fieldBean.getFieldName(), fieldValue);
					}else if("lastUpdateTime".equals(fieldBean.getFieldName())){
						fieldValue=BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
						tableMap.put(fieldBean.getFieldName(), fieldValue);
					}
				}
			}
		}
		return tableMap;
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward update(ActionMapping mapping,ActionForm form,
					HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		String tableName = getParameter("tableName", request); // 得到表名
		String billId=request.getParameter("billid");
		String buttonVal=request.getParameter("button");
		String retUrl=request.getParameter("retValUrl");
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
    	
    	HashMap<String,String> tableMap=getTableMap(tableInfo,"update",request,response);
    	tableMap.put("id", billId);
    	LoginBean loginBean=this.getLoginBean(request);
    	Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		String deliverTo=request.getParameter("deliverTo");
    	if(tableMap!=null){
	    	Result rs=mgt.executeUpdateSQL(tableMap, tableName,loginBean,resources,this.getLocale(request),deliverTo);
	    	if(rs.retCode!=ErrorCanst.DEFAULT_FAILURE){
	    		String backUrl="";
	    		if(!"deliverTo".equals(buttonVal) ){ //暂存
	    			backUrl = "/OAWorkFlowAction.do?tableName="+tableName+"&keyId="+tableMap.get("id")+"&operation=7";
	    			if("addAffix".equals(buttonVal)){
	    				request.setAttribute("directJump", true);
	    				backUrl="/OAMyWorkFlow.do?keyId="+tableMap.get("id")+"&addAffix=true" ; 
	    			}
	    		}else{
	    			request.setAttribute("directJump", true);
	    			//如果用户确定转交了，则要返回详情
	    			retUrl = "/OAWorkFlowAction.do?tableName="+tableName+"&keyId="+tableMap.get("id")+"&operation=5";
            		request.setAttribute("retValUrl", retUrl);
            		backUrl="/OAMyWorkFlow.do?keyId="+tableMap.get("id")+"&tableName="+tableName+"&approveStatus=transing&operation="+OperationConst.OP_AUDITING_PREPARE ; 
	    		}
	    		EchoMessage.success().add(getMessage(request, "common.msg.saveSuccess")).setBackUrl(backUrl).setAlertRequest(request) ; 

	    	}else{
				// 添加失败
				EchoMessage.error().add(
						getMessage(request, "common.msg.updateErro"))
						.setAlertRequest(request);
	    	}
    	}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 我的工作流详细页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward detail(ActionMapping mapping,ActionForm form,
								HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		String keyId=request.getParameter("keyId"); //工作流记录
		String tableName=request.getParameter("tableName");// 得到表名
		HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId);
		if(flowMap==null){
			//你访问的数据不存在
			EchoMessage.error().add("你所访问的工作流数据不存在，可能已经删除！")
	 		   .setNotAutoBack().setAlertRequest(request);
	    	return getForward(request, mapping, "message");
		}
		String currNodeId = String.valueOf(flowMap.get("currentNode"));
		String designId = String.valueOf(flowMap.get("applyType"));
		String templateName = BaseEnv.workFlowInfo.get(designId).getTemplateName();
		String userId = getParameter("sid", request);
		if(userId==null){
			userId = getLoginBean(request).getId();
		}else{
			request.setAttribute("mobileDetail", "true");
		}
		
		String hiddenFields=";";
		String userLastNode = "" ;
		Result rst=oamgt.getUserLastNode(keyId,userId,tableName);
		if(rst.retCode==ErrorCanst.DEFAULT_SUCCESS&&rst.retVal!=null){
			userLastNode=rst.retVal.toString();
		}
		if(userLastNode==null || userLastNode.length()==0){
			userLastNode = currNodeId ;
		} 
	    WorkFlowDesignBean workFlowBean=BaseEnv.workFlowDesignBeans.get(designId);
        if(workFlowBean!=null){
	        HashMap<String,FlowNodeBean> flowNodeMap =workFlowBean.getFlowNodeMap();
	        FlowNodeBean flowNodeBean=flowNodeMap.get(userLastNode);
	        List<FieldBean> fields=flowNodeBean.getFields();
	        if(fields!=null && fields.size()>0){
		        for(FieldBean flowBean : fields){
		        	DBFieldInfoBean dbField = GlobalsTool.getFieldBean(tableName, flowBean.getFieldName());
		        	if(dbField!=null){
		        		if(flowBean.getInputType()==3){
		        			hiddenFields+=flowBean.getFieldName()+";";
		        		}
		        	}
		        }
	        }
		    request.setAttribute("hiddenFields", hiddenFields); //获取流程设计中的隐藏字段
        }
		
		String layOutHtml="";
		HashMap htmlMap = mgt.getNewTable(tableName,designId);
        if(htmlMap != null){
			layOutHtml = String.valueOf(htmlMap.get("layOutHtml"));
        }else{
        	System.out.println("获取表单内容失败");
        }
        
        OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(designId);
        if(template!=null && template.getLines()!=null){ //新版工作流有xml结点位置线条的取详情
        	Result rs1 = oamgt.flowDepict(designId, keyId, tableName);
        	request.setAttribute("flowNew", true);
        	if(rs1.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		request.setAttribute("delivers", rs1.getRetVal());
        	}
        }else{
	      	//拿当前单据的所有会签意见
	    	Result rs1=oamgt.getDeliverance(keyId,null,tableName);
	    	if(rs1.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    		request.setAttribute("delivers", rs1.getRetVal());
	    	}
	    	//拿当前单据的所有附件
	    	rs1=oamgt.getDeliverance(keyId,"affix",tableName);
	    	if(rs1.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    		request.setAttribute("affixs", rs1.getRetVal());
	    	}
        }
    	request.setAttribute("tableCHName", templateName);
    	request.setAttribute("keyId", keyId);
        request.setAttribute("currNodeId", currNodeId);
        request.setAttribute("designId", designId);
        request.setAttribute("layOutHtml", layOutHtml);
        request.setAttribute("tableInfo", BaseEnv.tableInfos.get(tableName));
		return mapping.findForward("detail");
	}
	
	
	/**
	 * 获取工作流的数据（采用json方法，避免数据编译问题）
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward getWorkFlowJson(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		String keyId=request.getParameter("keyId"); //工作流记录
		String tableName=request.getParameter("tableName");// 得到表名
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		Result rsWorkFlow=mgt.loadWorkFlow(keyId, tableInfo);
		LoginBean loginBean=this.getLoginBean(request);
		HashMap<String,String> map=null;
		if(rsWorkFlow.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			System.out.println("加载工作流信息失败");
		}else{
			 map=(HashMap<String, String>) rsWorkFlow.retVal;
			for(DBFieldInfoBean fieldBean:tableInfo.getFieldInfos()){
				if("employee".equals(fieldBean.getDefaultValue())){
					String empId="popedomUserIds_"+fieldBean.getFieldName();
					String empNames=mgt.showNames("employee",fieldBean.getFieldName(),map);
					if("null".equals(empNames)|| empNames==null){
						empNames="";
					}
					map.put(empId, empNames);
				}else if("dept".equals(fieldBean.getDefaultValue())){
					String deptId="popedomDeptIds_"+fieldBean.getFieldName();
					String deptNames=mgt.showNames("dept",fieldBean.getFieldName(),map);
					if("null".equals(deptNames)|| deptNames==null){
						deptNames="";
					}
					map.put(deptId, deptNames);
				}else if("client".equals(fieldBean.getDefaultValue())){
					String clientId="popedomClientId_"+fieldBean.getFieldName();
					String clientNames=mgt.showNames("client",fieldBean.getFieldName(),map);
					if("null".equals(clientNames)|| clientNames==null){
						clientNames="";
					}
					map.put(clientId, clientNames);
				}else if("popup".equals(fieldBean.getDefaultValue())){
					
					Result rs=mgt.getPop(map, fieldBean, loginBean);
					if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
						System.out.println("定制弹出框内容查询失败!");
					}
				}	
			}
		}
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
		request.setAttribute("msg", gson.toJson(map));
		return mapping.findForward("blank");
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updatePrepare(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		String keyId=request.getParameter("keyId"); //工作流记录
		String tableName=request.getParameter("tableName");// 得到表名
		
		LoginBean loginBean = getLoginBean(request);
	 	HashMap oaMap = oamgt.getOAMyWorkFlowInfo(keyId,tableName); 
		String currNodeId = String.valueOf(oaMap.get("currentNode"));
		String designId = String.valueOf(oaMap.get("applyType"));
		String strCheckPersons = String.valueOf(oaMap.get("checkPerson"));
		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(String.valueOf(oaMap.get("applyType")));
    	/*查看当前审核人里有没有委托给我的*/
    	if(strCheckPersons!=null){
        	HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(loginBean.getId(), request.getParameter("designId"));
        	for (String person : strCheckPersons.split(";")) {
				if (consignMap.get(person) != null) {
					strCheckPersons += loginBean.getId() + ";";
				}
			}
    	}
		/*判断执行此操作的人是否当前审批结点的审核人 主要为了两个人同时审批或者一个人同时在(详情界面和列表界面同时打开)*/
        if("-1".equals(currNodeId) || (strCheckPersons.length()>0 && !strCheckPersons.contains(";"+loginBean.getId()+";") 
        		&& !"0".equals(currNodeId))){
        	String detailUrl="/OAWorkFlowAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=5";
    		return new ActionForward(detailUrl);
        }
		String fromAdvice=request.getParameter("fromAdvice");
	    Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);

    	String nextNode="";
	    WorkFlowDesignBean workFlowBean=BaseEnv.workFlowDesignBeans.get(designId);
        if(workFlowBean!=null){
	        HashMap<String,FlowNodeBean> flowNodeMap =workFlowBean.getFlowNodeMap();
	        FlowNodeBean flowNodeBean=flowNodeMap.get(currNodeId);
	        List<FieldBean> fields = flowNodeBean.getFields();
	        if(fields!=null && fields.size()>0){
		        List<String> delFields=new ArrayList<String>();
		        for(FieldBean fileBean : fields){
		        	DBFieldInfoBean dbField = GlobalsTool.getFieldBean(tableName, fileBean.getFieldName());
		        	if(dbField == null){
		        		delFields.add(fileBean.getFieldName());
		        	}
		        }
		        if(delFields.size()>0){
			        for(String delId:delFields){
			        	fields.remove(delId);
			        }
		        }
	        }
		    request.setAttribute("workFlowFields", fields); //获取流程设计中的字段设置
        }
		
		String layOutHtml="";
		HashMap htmlMap = mgt.getNewTable(tableName,designId);
        if(htmlMap != null){
			layOutHtml = String.valueOf(htmlMap.get("layOutHtml"));
        }else{
        	System.out.println("获取表单内容失败");
        	
        }
        if(template!=null && template.getLines()!=null){
        	Result rs1 = oamgt.flowDepict(designId, keyId, tableName);
        	request.setAttribute("flowNew", true);
        	if(rs1.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		request.setAttribute("delivers", rs1.getRetVal());
        	}
        }else{
	     	//拿当前单据的所有会签意见
	    	Result rs1=oamgt.getDeliverance(keyId,null,tableName);
	    	if(rs1.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    		request.setAttribute("delivers", rs1.getRetVal());
	    	}
	    	//拿当前单据的所有附件
	    	rs1=oamgt.getDeliverance(keyId,"affix",tableName);
	    	if(rs1.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    		request.setAttribute("affixs", rs1.getRetVal());
	    	}
        }
     	request.setAttribute("tableCHName", template.getTemplateName());
        request.setAttribute("keyId", keyId);
    	request.setAttribute("fromAdvice", fromAdvice);
        request.setAttribute("nextNodeIds", nextNode);
        request.setAttribute("currNodeId", currNodeId);
        request.setAttribute("designId", designId);
        request.setAttribute("layOutHtml", layOutHtml);
        request.setAttribute("tableInfo", tableInfo);
		
		return mapping.findForward("update");
	}
	
	protected ActionForward doAuth(HttpServletRequest request, ActionMapping mapping) {
		return null ;
	}
}
