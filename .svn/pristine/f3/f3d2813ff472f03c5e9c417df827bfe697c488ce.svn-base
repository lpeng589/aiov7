package com.menyi.web.util;


import java.util.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.dyndb.DDLOperation;

/**
 *
 * <p>Title: 管理类基本action</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 * @preserve all
 */
public abstract class MgtBaseAction extends BaseAction {
 
    /**
     * 这里可进行权限判断
     * @param req HttpServletRequest
     * @param mapping ActionMapping
     * @return ActionForward
     */
    protected ActionForward doAuth(HttpServletRequest req,ActionMapping mapping) {
    	 
        if((req.getRequestURI().endsWith("/UpgradeAction.do")&&"true".equals(req.getParameter("exig")))){
            return null;
        }
        if(req.getRequestURI().contains("/importDataAction.do")){ //导入
        	return null ;
        }
        if((req.getRequestURI()+"?"+req.getQueryString()).contains("/ReportDataAction.do?operation=advance")){ //条件高级设置
        	return null ;
        }
        if(req.getRequestURI().contains("/advanceImportDataAction.do")){ //导入
        	return null ;
        }
        if((req.getRequestURI()+"?"+req.getQueryString()).contains("/OAWorkFlowTempAction.do?operation=1&type=fileupload")){
        	return null ;
        }
        if((req.getRequestURI()+"?"+req.getQueryString()).startsWith("/ReportDataAction.do?operation=bossReport")){
        	return null ;
        }
        if(req.getRequestURI().contains("/DirectorySettingAlbumQueryAction.do") || req.getRequestURI().contains("/DirectorySettingNetDiskQueryAction.do")){//硬盘和相册的目录设置
            return null;
        }
        if(req.getRequestURI().contains("/ERPWorkFlowTempSearchAction.do")){ //审核流程设置
        	return null;
        }
        if (mapping.getParameter() == null) {
            //如果mapping 没有配parameter 则提示非法,本提示只在开发阶段可能出现
            BaseEnv.log.error(" ------The action in strutsconfig.xml not set parameter-----");
            ActionForward forward = getForward(req, mapping, "message");
            EchoMessage.error().add(getMessage(req, "MgtbaseAction.lb.ActionNoSet")).
                setRequest(req);
            return forward;
        }

        LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
        	String url = req.getRequestURI()+(req.getQueryString()==null?"":"?"+req.getQueryString());
        	HashMap map = new HashMap();
        	for(Object key:req.getParameterMap().keySet()){
				Object value=req.getParameterMap().get(key);
				if(url.indexOf("%"+key+"=")==-1&&url.indexOf("?"+key+"=")==-1){
					map.put(key, value);
				}
			}
        	map.putAll(req.getParameterMap());
        	req.getSession().setAttribute("RElOGINDATA", map);
        	req.getSession().setAttribute("RElOGINURL", url);
        	ActionForward forward = getForward(req, mapping, "message");
            EchoMessage.error().add("请重新登陆").reLogin().
                setRequest(req);
            return forward;
        }
        if(req.getRequestURI().contains("/EnumerationAction.do")||req.getRequestURI().contains("/EnumerationQueryAction.do")){ //枚举的由于可以在crm或其它地方修改，所以只有有登陆就不再鉴权
        	return null;
        }
        //进行唯一用户验证，如果有生复登陆的，则后进入用户踢出前进入用户(手机端不踢人)
        if (!"mobile".equals(req.getSession().getAttribute("DEVICE")) && !OnlineUserInfo.checkUser(req) ) {
            //需踢出
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }
                
        if (loginBean.getOperationMap()==null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean.getOperationMap() is null");
            return getForward(req, mapping, "indexPage");
        }

        int operation = getOperation(req);

        if (operation == OperationConst.OP_POPUP_SELECT || operation==OperationConst.OP_DOWNLOAD || operation==OperationConst.OP_SELECT_PIC ) {
            //弹出框不做模块权限的判断
            return null;
        }

        
        //系统管理员不做任何权限判断
        if ("admin".equalsIgnoreCase(getLoginBean(req).getName())) {
        	if(SystemState.instance.accountType == SystemState.ACCOUNT_QUERY){
        		//查询帐套Admin也不允许做单.
	        	switch (operation) {
	            case OperationConst.OP_ADD_PREPARE:
	            case OperationConst.OP_QUOTE:
	            case OperationConst.OP_COPY:
	            case OperationConst.OP_COPY_PREPARE:
	            case OperationConst.OP_SCOPE_RIGHT_ADD_PREPARE:
	            case OperationConst.OP_SEND:
	            case OperationConst.OP_SEND_PREPARE:
	            case OperationConst.OP_REVERT_PREPARE:
	            case OperationConst.OP_REVERT:
	            case OperationConst.OP_SET_ALERT:
	            case OperationConst.OP_CANCEL_ALERT:
	            case OperationConst.OP_ADD:
	            case OperationConst.OP_DRAFT:	                
	                	ActionForward forward = getForward(req, mapping, "alert");
	                    EchoMessage.error().add(getMessage(req,
	                                                       "common.msg.RET_ACCOUNT_QUERY_ERROR")).
	                        setAlertRequest(req);
	                    return forward;
	        	}
        	}
            return null;
        }
        
        //判断当前模块是否启用工作流
        String designId = req.getParameter("designId") ;
        if(designId!=null && designId.trim().length()>0 && (operation!=4 && operation!=0)){
	    	WorkFlowDesignBean flowDesign = BaseEnv.workFlowDesignBeans.get(designId) ;
	    	OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(designId);
	    	if(flowDesign!=null && workFlow!=null && workFlow.getTemplateStatus()==1){
	    		setWorkFlowScope(workFlow.getTemplateFile(), workFlow.getId(), req) ;
	    	}
        }
        
        
        
        //list界面中根据CUR_INDEX_URL 进行的权限判断，只是为了控制按扭显示。
        //在这里必须根据mapping和operation进行详细的权限控制
        String parameter = mapping.getParameter();
        String moduleType = getParameter("moduleType", req) ;
        if (parameter.indexOf("$") > 0) {
            int pos1 = parameter.indexOf("$");
            int pos2 = 0;
            if (parameter.indexOf("&", pos1) > 0) {
                pos2 = parameter.indexOf("&", pos1);
            }

            String beforeStr = parameter.substring(0, pos1);
            String midStr = "";
            String lastStr = "";
            if (pos2 == 0) {
                midStr = getParameter(parameter.substring(pos1 + 1), req);
            } else {
                midStr = getParameter(parameter.substring(pos1 + 1, pos2), req) ;
                lastStr = parameter.substring(pos2);
            }
            
            //这里如果是自己义表，则要判断表是否为邻居表，如果是邻居表则要用主表来控制权限
            if(beforeStr.startsWith("/UserFunctionQueryAction.do?tableName=")){
                String tableName = midStr;
                DBTableInfoBean tableInfoBean = DDLOperation.getTableInfo((Hashtable) req.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO),tableName);
                if(tableInfoBean != null && tableInfoBean.getTableType() == DBTableInfoBean.BROTHER_TABLE){
                	//当查询的表为字表或邻居表时，根据该主表获取权限
                    String parentTableName=req.getParameter("parentTableName");
                    parentTableName=parentTableName==null?"":parentTableName;
                    midStr = parentTableName;
                    parameter = "/UserFunctionQueryAction.do?tableName=" + parentTableName ;
                }else if(moduleType!=null && moduleType.length()>0){
                	parameter = beforeStr + midStr + "&moduleType=" + moduleType ;
                }else{
                	parameter = beforeStr+midStr+lastStr;
                }
            }else{
            	parameter = beforeStr+midStr+lastStr;
            }
        }
        if(parameter.startsWith("/ReportDataAction.do?reportNumber=")){
        	//如果是报表，则链接报表的权限由链接主报表的权限来控制        	
        	String mainNumber=req.getParameter("mainNumber");
        	if(mainNumber!=null&&mainNumber.length()>0){
        		parameter = "/ReportDataAction.do?reportNumber="+mainNumber;
        		if(moduleType!=null && moduleType.length()>0){
                	parameter = parameter + "&moduleType=" + moduleType ;
                }
        	}
        	
        }
        
        if(parameter.startsWith("/UserFunctionQueryAction.do?tableName=Flow")){ //这是个性化OA工作流，不做权限控制 
        	return null;
        }
        
        String detTable_list = req.getParameter("detTable_list");
        if(detTable_list != null && detTable_list.length() > 0){
        	//如果表细表报表，权限由数据表列表来控制
        	parameter = "/UserFunctionQueryAction.do?tableName="+detTable_list;
        	if(moduleType!=null && moduleType.length()>0){
            	parameter = parameter + "&moduleType=" + moduleType ;
            }
        }
        
        
        /* 如果是财务报表 */
        if(parameter.startsWith("/FinanceReportAction.do")){
        	String optype = req.getParameter("optype");
        	parameter = (optype!=null&&optype.length()>0)?"/FinanceReportAction.do?optype="+optype:parameter;
        }else if(parameter.startsWith("/UserFunctionQueryAction.do?tableName=tblBalance")){
        	String financeReportName = req.getParameter("financeReportName");
        	parameter = (financeReportName!=null&&financeReportName.length()>0)?"/FinanceReportAction.do?optype="+financeReportName:parameter;
        }else{
        	//非自定义模块也可通过增加moduleType来区分不同模块，如工作流设计在oa中有，在erp中也有一份，需不同的授权，通过增加moduleType可以分开权限
        	if(moduleType!=null && moduleType.length()>0 && !parameter.contains("moduleType=")){
        		if(parameter.indexOf("?")> 0){
        			parameter = parameter +"&moduleType=" + moduleType ;
        		}else{
        			parameter = parameter +"?moduleType=" + moduleType ;
        		}
            }
        }
        
        MOperation mo = (MOperation) getLoginBean(req).getOperationMap().get(parameter);
        if(mo==null && parameter.indexOf("CustomQueryAction.do")>0){
        	mo = (MOperation) getLoginBean(req).getOperationMap().get("/OAWorkFlowTempQueryAction.do");
        }
        if (mo == null) {
            //对整个模块无访问权限
        	req.setAttribute("noback", true) ;
            BaseEnv.log.error(" ------No right ---  illegal access Module -----"+parameter);
            ActionForward forward = getForward(req, mapping, "message");
            EchoMessage.error().add(getMessage(req,"common.msg.RET_NO_RIGHT_ERROR")).setRequest(req);
            return forward;
        }
        if (operation == OperationConst.OP_UPDATE_ADD){
        	return null;//这是添加修改功能，这里不做权限判断，方法内部去做
        }
        
        
        /*制单人是否在没有修改权限的情况下 可以修改草稿*/
        String draftUpdate = BaseEnv.systemSet.get("draftUpdate").getSetting() ;
        //如果未输入操作类型，默认为查询操作
        if (operation == 0)
            operation = OperationConst.OP_QUERY;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
        case OperationConst.OP_QUOTE:
        case OperationConst.OP_COPY:
        case OperationConst.OP_COPY_PREPARE:
        case OperationConst.OP_SCOPE_RIGHT_ADD_PREPARE:
        case OperationConst.OP_SEND:
        case OperationConst.OP_SEND_PREPARE:
        case OperationConst.OP_REVERT_PREPARE:
        case OperationConst.OP_REVERT:
        case OperationConst.OP_SET_ALERT:
        case OperationConst.OP_CANCEL_ALERT:
            //判断添加权限，返回message
            if (!mo.add()) {
                return getForward(false, req, mapping, operation);
            }
            if(SystemState.instance.accountType == SystemState.ACCOUNT_QUERY){
            	//查询帐套不允许做单.
            	ActionForward forward = getForward(req, mapping, "alert");
                EchoMessage.error().add(getMessage(req,
                                                   "common.msg.RET_ACCOUNT_QUERY_ERROR")).
                    setAlertRequest(req);
                return forward;
            }
            break;
        case OperationConst.OP_ADD:
        case OperationConst.OP_DRAFT:
            //判断添加权限，返回alert
            if (!mo.add()) {
                return getForward(true, req, mapping, operation);
            }
            if(SystemState.instance.accountType == SystemState.ACCOUNT_QUERY){
            	//查询帐套不允许做单.
            	ActionForward forward = getForward(req, mapping, "alert");
                EchoMessage.error().add(getMessage(req,
                                                   "common.msg.RET_ACCOUNT_QUERY_ERROR")).
                    setAlertRequest(req);
                return forward;
            }
            break;
        case OperationConst.OP_DELETE:
            //判断删除权限，返回message
            if (!mo.delete() && "false".equals(draftUpdate)) {
                return getForward(false, req, mapping, operation);
            }
            break;
        case OperationConst.OP_CHECK_PREPARE:
        case OperationConst.OP_CHECK_LIST:
        case OperationConst.OP_CHECK:
        case OperationConst.OP_UPDATE_PREPARE:
        case OperationConst.OP_TABLE_VIEW_PREPARE:
        case OperationConst.OP_MODULE_RIGHT_PREPARE:
        case OperationConst.OP_SCOPE_RIGHT_UPDATE_PREPARE:
        case OperationConst.OP_SCOPE_RIGHT_DELETE:
            //判断添加权限，返回alert
            if ((!mo.update() && "false".equals(draftUpdate)) && !"quoteDraft".equals(req.getParameter("saveDraft"))) {
                return getForward(false, req, mapping, operation);
            }
            break;
        case OperationConst.OP_UPDATE:
        ////mj	
        case OperationConst.OP_READ_OVER:
        case OperationConst.OP_IMPORT_PREPARE:
        case OperationConst.OP_IMPORT:
        case OperationConst.OP_URL_TO:
        case OperationConst.OP_TABLE_VIEW:
        case OperationConst.OP_MODULE_RIGHT:
        case OperationConst.OP_SCOPE_RIGHT_ADD:
        case OperationConst.OP_SCOPE_RIGHT_UPDATE:
        case OperationConst.OP_UPGRADE_PREPARE:
        case OperationConst.OP_UPGRADE:
            //判断添加权限，返回alert
            if (!mo.update()  && "false".equals(draftUpdate)) {
                return getForward(true, req, mapping, operation);
            }
            break;
        case OperationConst.OP_PRINT:
            //判断添加权限，返回alert
            if (!mo.print()) {
                return getForward(true, req, mapping, operation);
            }
            break;
        case OperationConst.OP_EXTENDBUTTON_DEFINE:
            //判断添加权限，返回alert
        	String right=req.getParameter("right")==null?"":req.getParameter("right");
            if (right.equals("add")&&!mo.add) {
                return getForward(false, req, mapping, operation);
            }else if (right.equals("update")&&!mo.update) {
                return getForward(false, req, mapping, operation);
            }else if (right.equals("delete")&&!mo.delete) {
                return getForward(false, req, mapping, operation);
            }else if (right.equals("query")&&!mo.query) {
                return getForward(false, req, mapping, operation);
            }else if (right.equals("print")&&!mo.print) {
                return getForward(false, req, mapping, operation);
            }else if (right.equals("sendEmain")&&!mo.sendEmail) {
                return getForward(false, req, mapping, operation);
            }
            break;

        case OperationConst.OP_SET:
        case OperationConst.OP_SET_PREPARE:
        case OperationConst.Op_AUDITING:
        case OperationConst.OP_AUDITING_PREPARE:
        case OperationConst.OP_QUERY:
        case OperationConst.OP_DETAIL:
        case OperationConst.OP_BACKUPDB:
        case OperationConst.OP_POPUP_SELECT:
        case OperationConst.OP_DELETE_PREPARE:
        case OperationConst.OP_SCOPE_RIGHT_QUERY:
        case OperationConst.OP_OA_VIEW_SINLE:
        case OperationConst.OP_OA_JSON:
        case OperationConst.OP_OA_GRID:
        case OperationConst.OP_OA_VIEW_GROUOP_QUERY:
        case OperationConst.OP_EXPORT:
        case OperationConst.Op_RETAUDITING:
        case OperationConst.OP_UPDATE_PWD:
        case OperationConst.OP_UPDATE_PWD_PREPARE:
            return null;
        default:
        	req.setAttribute("noback", true) ;
            //操作类型不正确，或未进行权限控制,只在开发阶段出现
            BaseEnv.log.error(" ------operation error or on right control -----");
            ActionForward forward = getForward(req, mapping, "message");
            EchoMessage.error().add(getMessage(req, "MgtbaseAction.lb.wrongType")).setRequest(req);
            return forward;
        }
        return null;
    }
    
    /**
     * 对工作流的单据进行授权
     */
    public void setWorkFlowScope(String tableName,String applyType,HttpServletRequest request){
    	String moduleUrl="/UserFunctionQueryAction.do?tableName="+tableName;
    	MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get(moduleUrl) ;
		if(mop==null || !mop.update){
			mop = new MOperation();
    		mop.moduleUrl = moduleUrl;
    		
    		String moduleId=getModuleId(moduleUrl,BaseEnv.allModule);
    		if(moduleId != null && moduleId.length() > 0){ //如果是有菜单的自定义单据，应取菜单的模块ID,否则取工作流ID
    			mop.moduleId = moduleId; 
    		}else{
    			mop.moduleId=applyType;
    		}
    		this.getLoginBean(request).getOperationMap().put(mop.moduleUrl, mop);
    		this.getLoginBean(request).getOperationMapKeyId().put(mop.moduleId, mop);
            //BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
            //BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
    		
            mop.add=true;
    		mop.update=true;
            mop.print=true;
            mop.query=true;
            mop.sendEmail=true;
            mop.oaWorkFlow=true;
		}
    }

    private ActionForward getForward(boolean alert, HttpServletRequest req, ActionMapping mapping, int operation) {
        BaseEnv.log.error(" ------No right ---  illegal operation " + operation + "  -----");
        if (alert) {
            ActionForward forward = getForward(req, mapping, "alert");
            EchoMessage.error().add(getMessage(req,
                                               "common.msg.RET_NO_RIGHT_ERROR")).
                setAlertRequest(req);
            return forward;
        } else {
            ActionForward forward = getForward(req, mapping, "message");
            EchoMessage.error().add(getMessage(req,
                                               "common.msg.RET_NO_RIGHT_ERROR")).
                setRequest(req);
            return forward;
        }
    }

}
