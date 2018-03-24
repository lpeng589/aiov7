package com.koron.hr.review;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.koron.oa.workflow.template.OAWorkFlowTempMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: 表现评估</p>
 *
 * @Date:Oct 10, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class HRReviewAction extends MgtBaseAction{

	HRReviewMgt mgt = new HRReviewMgt() ;
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
			case OperationConst.OP_AUDITING_PREPARE:
				forward = deliverToPrepare(mapping, form, request, response);
				break;
			case OperationConst.OP_QUERY:
				forward = query(mapping, form, request, response);
				break;
			default:
				forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		HRReviewSearchForm hrForm = (HRReviewSearchForm) form ;
		hrForm.setUserId(getLoginBean(request).getId()) ;
		Result result = mgt.queryReview(hrForm) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList values = (ArrayList) result.retVal ;
			OAWorkFlowTempMgt oaMgt=new OAWorkFlowTempMgt();
    		String designId = (String) oaMgt.queryFlowIdByAllowUser("HRReview", getLoginBean(request).getId()).retVal ;
			String billId = "" ;
			Calendar calendar = Calendar.getInstance() ;
    		for(int i=0;i<values.size();i++){
				Object[] obj = (Object[]) values.get(i) ;
				if(i==0){
					billId = obj[0].toString() ;
					String flowDoc = mgt.getFlowDepict(obj[7].toString(), billId, getLocale(request), this.getResources(request)) ;
					request.setAttribute("flowDoc", flowDoc) ;
				}
				/*查询下个转交结点*/
				if((obj[9]==null || obj[9].toString().length()==0) && obj[8]!=null && !"-1".equals(obj[8])){
					String nextNodeId = (String) mgt.getNextNodeId("HRReview", obj[7].toString(),obj[8].toString(),getLoginBean(request)).retVal ;
					obj[9] = nextNodeId ;
				}
			}
			request.setAttribute("reviewList", values) ;
			pageBar(result, request) ;
			
			result = mgt.getFlowTemplate(designId) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("addFlow", false) ;
			}else{
				request.setAttribute("addFlow", true) ;
			}
		}
		return getForward(request, mapping, "reviewList");
	}
	
	/**
	 * 转交之前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward deliverToPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {	
		
		String keyId = getParameter("keyId", request) ;
		String currNodeId = getParameter("currNodeId", request) ;
		String department = getParameter("department", request) ;
		String designId = getParameter("designId", request) ;
		String nextNodeIds = getParameter("nextNodeIds", request) ;
		
		HashMap values = new HashMap() ;
		Result result = new OAMyWorkFlowMgt().transactStart(currNodeId, keyId, getLoginBean(request),"HRReview") ;
		return new ActionForward("/OAMyWorkFlow.do?keyId="+keyId+"&currNodeId="+currNodeId+"&nextNodeIds=" + nextNodeIds +
				"&department="+department+"&designId="+designId+"&operation=82&noback=true&toPage=message") ;
	}
}
