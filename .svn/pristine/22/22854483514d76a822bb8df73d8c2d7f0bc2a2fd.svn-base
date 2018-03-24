package com.koron.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.web.tab.TabMgt;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 *  该类主要用于把往来单位中数据全部转移到CRM目标客户中
 *  
 *  @Copyright: 科荣软件
 *	
 *  @Date:2009-9-26
 *
 *	@Author 文小钱
 */
public class TransferCRMAction extends BaseAction {

	TabMgt tabMgt = new TabMgt();
	public TransferCRMAction() {

	}

	public ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		int operation = getOperation(request);
		ActionForward forward = null;
		
		switch (operation) {
		case OperationConst.OP_UPDATE_PREPARE:
			forward = transferPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = transfer(mapping, form, request, response);
			break;
		}
		return forward;
	}

	/**
	 * 转移前准备，调转转移界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward transferPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return getForward(request, mapping, "transfer");
	}
	
	/**
	 * 把往来单位中数据全部转移到CRM目标客户中
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward transfer(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Result rs = tabMgt.transferCRM() ;
		ActionForward forward = null ;
		if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
	                request, "common.msg.transferSuccess")).
	                	setBackUrl("/TransferCRMAction.do?operation="+OperationConst.OP_UPDATE_PREPARE)
	                	.setAlertRequest(request);
			forward = getForward(request, mapping, "message") ;
		}else{
			EchoMessage.error().add(rs.getRetVal().toString())
						.setBackUrl("TransferCRMAction.do?operation="+OperationConst.OP_UPDATE_PREPARE)
						.setRequest(request);
            forward = getForward(request, mapping, "message");
		}
		return forward ;
	}

	
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		// TODO Auto-generated method stub
		return null;
	}
}
