package com.koron.crm.clientLinkman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.client.CRMClientAction;
import com.koron.crm.client.CRMClientMgt;
import com.koron.crm.setting.ClientSetingMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class ClientLinkManAction extends MgtBaseAction {

	ClientLinkmanMgt mgt = new ClientLinkmanMgt();
	ClientSetingMgt moduleMgt = new ClientSetingMgt() ;
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:// 删除
			forward = delete(mapping, form, request,response);
			break;
		default:
			forward = query(mapping, form, request, response);
			break;
		}
		return forward;
	}
	
	/**
	 * 发送短信
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward sendSMS(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String[] keyIds = getParameters("keyId", request) ; /*客户ID*/
		String sendMessage = getParameter("sendMessage", request) ;/*发送内容*/
		StringBuffer sb = new StringBuffer() ;
		for(String str : keyIds){
			sb.append("'").append(str).append("',") ;
		}
		String keyId = sb.toString().substring(0, sb.toString().length()-1) ;
		Result result = mgt.selectSMSorEmailById(keyId, "Mobile") ;
		int sucessNumber = 0 ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<String> mobiles = (ArrayList<String>) result.getRetVal() ;
			if (BaseEnv.telecomCenter != null) {
				BaseEnv.telecomCenter.send(sendMessage,mobiles.toArray(new String[0]),getLoginBean(request).getId());				
			}else{
				EchoMessage.info().add(getMessage(request, "sms.not.start"))
				   				  .setBackUrl("/ClientLinkmanAction.do?type=main")
				   				  .setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
			EchoMessage.success().add(getMessage(request, "common.note.sendsuccess"))
							   .setBackUrl("/ClientLinkmanAction.do?type=main")
							   .setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return mapping.findForward("ClientMain");	
	}
	
	/**
	 * 删除客户联系人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String[] keyIds = request.getParameterValues("keyId");	/*联系人Id*/
		Result result = mgt.deleteClientLinkMan(keyIds);
		if(result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
                    		   .setBackUrl("/ClientLinkmanAction.do?type=main&operation=4")
                    		   .setNotAutoBack().setAlertRequest(request);
			 return getForward(request, mapping, "message");
		}
		return queryList(mapping, form, request, response) ;
	}

	/**
	 * 客户联系人查询 左边查百家姓 右边客户联系人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String type = request.getParameter("type");
		/*
		if ("left".equals(type)) {
			return queryLeft(mapping, form, request, response);
		}else if("main".equals(type)){
			return queryList(mapping, form, request, response);
		}else if("sendSMS".equals(type)){
			return sendSMS(mapping, form, request,response) ;
		}
		return mapping.findForward("LinkmanIndex");
		*/
		if("sendSMS".equals(type)){
			return sendSMS(mapping, form, request,response) ;
		}else{
			return queryList(mapping, form, request, response);
		}
	}

	/**
	 * 查询客户联系人列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		/*
		String firstName = getParameter("firstName", request);		/*姓名 首字
	    if("GET".equals(request.getMethod()) && firstName!=null){
	   	 	firstName = GlobalsTool.toChinseChar(firstName) ;
	    }
	    */
	    String ClientName = getParameter("ClientName", request) ; 	/*客户名称*/
	    String telephone  = getParameter("telephone", request) ;	/*联系人电话*/
	    String mobile     = getParameter("mobile", request) ;		/*手机*/	 
	    String userName   = getParameter("userName", request) ;		/*联系人姓名*/
	    LoginBean login = getLoginBean(request);
	    
	    /*
	    if(userName != null && !"".equals(userName)){
	    	firstName = null;
	    }
	    request.setAttribute("firstName", firstName);
	     */
	    request.setAttribute("ClientName", ClientName);
	    request.setAttribute("telephone", telephone);
	    request.setAttribute("userName", userName);
	    request.setAttribute("mobile", mobile);
	    
	    
	    
	    //根据loginBean查找出查看模板的权限
	    String moduleIds="";//存放有权限的模板ids
	    Result rs = moduleMgt.queryModuleGroupBy(login);
	    
		HashMap<String,Integer> moduleCountMap= (HashMap<String,Integer>)rs.retVal;
		Set keys = moduleCountMap.keySet();
		if(keys != null) {
		Iterator iterator = keys.iterator();
			while(iterator.hasNext()) {
				Object key = iterator.next();
				int value = moduleCountMap.get(key);
				if(value >0){
					moduleIds +="'"+key.toString()+"',";
				}
			}
		}
	    if(moduleIds.endsWith(",")){
	    	moduleIds = moduleIds.substring(0,moduleIds.length()-1);
	    }
	    
	    
	    String clientSql = "SELECT id FROM CRMClientInfo WHERE 1=1 and status != '1' ";
	    
	    MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName=CRMClientInfo");
		StringBuilder condition = new StringBuilder();
		String userId = login.getId();
		/*设置范围权限*/
		if(!"1".equals(userId)){
			/*查看某字段值单据*/
			String fieldValueSQL = "" ;
			condition.append(" and (CRMClientInfo.createBy ='").append(userId).append("' or CRMClientInfo.id in(select f_ref from CRMClientInfoEmp " +
					"where employeeId ='").append(userId).append("' /*or (len(isnull(departmentCode,''))>0 and '").append(login.getDepartCode()).append("' like departmentCode+'%')*/) ") ;
			if(mop!=null){
				ArrayList scopeRight = new CRMClientAction().getAllMopByType(mop, login, MOperation.M_QUERY);	
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
							condition.append(" or CRMClientInfo.createBy in (").append(strUserIds).append(")");
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							//condition.append("or CRMClientInfo.createBy in  (select id from tblEmployee where ") ;
							for(String strId : lsb.getScopeValue().split(";")){
								//strDeptIds += "'"+strId+"'," ;
								condition.append(" or departmentCode like '").append(strId).append("%' ") ;
							}
						}
						
						/*
						if(lsb!=null && "6".equals(lsb.getFlag()) && "CRMClientInfo".equals(lsb.getTableName())){
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
						*/
					}
					
				}
			}
			condition.append(")") ;
			if(fieldValueSQL.length()>0){
				condition.append(" and (").append(fieldValueSQL).append(")") ;
			}
			condition.append(" and moduleId in (").append(moduleIds).append(")") ;
		}
		
	    
		clientSql = clientSql+condition.toString();
	    
	    
	    int pageSize = getParameterInt("pageSize", request);
        int pageNo = getParameterInt("pageNo", request);
        String noPageSize = (String) request.getAttribute("NoPageSize") ;
        if (pageNo == 0 || "OK".equals(noPageSize)) {
            pageNo = 1;
        }
        if (pageSize == 0) {
            pageSize = GlobalsTool.getPageSize();
        }
	    Result result = mgt.queryLinkMan(ClientName,telephone,userName,mobile,pageSize,pageNo,getLoginBean(request).getId(),clientSql);
	    if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
	    	request.setAttribute("pageBar", pageBar(result, request)) ;
	    	request.setAttribute("MOID", mop.getModuleId()) ;
	    	request.setAttribute("list", result.getRetVal());
	    }
		return mapping.findForward("LinkmanMain");
	}
	
	/**
	 * 百家姓
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryLeft(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String strTidy = request.getParameter("tidy") ; /*整理百家姓*/
		if("tidy".equals(strTidy)){
			mgt.queryFamilyName();
		}
		Result result = mgt.queryBJX();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("valueMap", result.retVal);
		}
		return mapping.findForward("LinkmanLeft");	
	}
	
}
