package com.koron.crm.salesFlow;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.setting.ClientSettingAction;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>Title:CRM兄弟表</p> 
 * <p>Description: </p>
 *
 * @Date:2013.8.27
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */

public class CRMSalesFlowAction extends BaseAction{
	private CRMSalesFlowMgt mgt = new CRMSalesFlowMgt();
	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		String type = getParameter("type", request);
		request.setAttribute("type", type);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);	
		}
		return forward;
	}

	
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String flowInfo = getParameter("flowInfo",request);
		String tableName = getParameter("tableName",request);
		Result rs = mgt.addFlow(flowInfo, tableName);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return new ActionForward("/CRMSalesFlowAction.do?tableName="+tableName,true);
		}else{
			return null;
		}
		
	}


	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String tableName = getParameter("tableName",request);
		Result rs = mgt.flowQueryByTableName(tableName);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String tempTableName = tableName;//查询兄弟表的表名,若是客户其他模板进入要截取字符
			if(tempTableName.indexOf("CRMClientInfo") >-1){
				tempTableName = "CRMClientInfo";
			}
			ArrayList<Object> rsList = (ArrayList<Object>)rs.retVal;
			LoginBean loginBean = getLoginBean(request);
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			ArrayList<DBTableInfoBean> brotherTableList = new DDLOperation().getBrotherTables(tempTableName, allTables);//获取兄弟表信息

			if(tableName!=null && tableName.indexOf("CRMClientInfo")>-1){
				this.setErpTableBean(brotherTableList);
			}
			
			
			String existTableNames = "";//存放已存在的邻居表
			String noSelectTables = "";//存放可选的邻居表
			
			//获取所有选中的邻居表信息
			for(Object obj : rsList){
				existTableNames += GlobalsTool.get(obj,2);
			}
			
			//过滤邻居表获取可选的邻居表
			for(DBTableInfoBean tableBean : brotherTableList){
				if(existTableNames.indexOf(tableBean.getTableName()) == -1){
					noSelectTables +=tableBean.getTableName()+",";
				}
			}
			
			brotherTableList = new ClientSettingAction().checkModuleUse(brotherTableList, request);//过滤未启用的邻居表
			HashMap<String,DBTableInfoBean> moduleMap = new HashMap<String, DBTableInfoBean>();//存放已启用的模板map
			for(DBTableInfoBean tableBean : brotherTableList){
				moduleMap.put(tableBean.getTableName(), tableBean);
			}
			
			
			request.setAttribute("flowList",rsList);
			request.setAttribute("tableName",tableName);
			request.setAttribute("noSelectTables",noSelectTables);
			request.setAttribute("moduleMap",moduleMap);
		}
		
		return getForward(request, mapping, "salesFlowIndex");
	}


	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 附加ERP单据
	 * @param brotherTableList
	 */
	public void setErpTableBean(ArrayList<DBTableInfoBean> brotherTableList){
		String erpTableName = "tblSalesOrder,tblSalesOutStock,tblSalesReturnStock,tblSalseQuote,tblSalesStandardInvoice";
		for(String str : erpTableName.split(",")){
			DBTableInfoBean tableBean = GlobalsTool.getTableInfoBean(str);
			if(tableBean!=null){
				brotherTableList.add(tableBean);
			}
		}
	}
}
