package com.menyi.aio.web.finance.adjustExchange;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.*;
/**
 * 
 * <p>
 * Title:期末調匯Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-03-28 上午 11:30
 * @Copyright: 科榮軟件
 * @Author fjj
 * @preserve all
 */
public class AdjustExchangeAction extends MgtBaseAction {
	
	AdjustExchangeMgt mgt = new AdjustExchangeMgt();
	
	/**
	 * exe 控制器入口函數
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
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 根據不同操作類型分配給不同函數處理
		int operation = getOperation(request);
		ActionForward forward = null;
		
		String opType = request.getParameter("optype");
		
		switch (operation) {
		case OperationConst.OP_QUERY:
			if(opType != null && "queryExchange".equals(opType)){
				//查詢所有當前的外幣匯率
				forward = queryExchange(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			//期末调汇
			forward = adjustExchange(mapping, form, request, response);
			break;
		default:
			
		}
		return forward;
	}
	
	
	/**
	 * 查詢當前所有的匯率
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward queryExchange(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		
		/* 会计期间*/
		LoginBean loginBean = this.getLoginBean(request);
    	Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable)sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean)hashSession.get("AccPeriodAcc");
		
		/* 查詢本会计期间的所有的匯率*/
		Result result = mgt.queryExchange(accPeriodBean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			request.setAttribute("exchangeList", result.retVal);
		}
		return getForward(request, mapping, "dealexchange");
	}
	
	/**
	 * 期末调汇(修改货币调整汇率和生成转账凭证与记录调汇历史表)
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward adjustExchange(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* 汇率Id*/
		String[] keyIds = request.getParameterValues("keyId");
		/* 当前汇率*/
		String[] currentexchange = request.getParameterValues("currentexchange");
		/* 调整汇率*/
		String[] adjustexchange = request.getParameterValues("adjustexchange");
		/* 会计科目*/
		String accCode = request.getParameter("accCode");
		/* 生成凭证分类（1汇兑收益 2汇兑损失 3汇兑损益）*/
//		String[] accAssort = request.getParameterValues("accAssort");
		/* 凭证日期*/
		String accTime = request.getParameter("accTime");
		/* 凭证字*/
		String credTypeID = request.getParameter("credTypeID");
		/* 摘要*/
		String recordCommon = request.getParameter("recordCommon");
		
		/* 会计期间*/
		LoginBean loginBean = this.getLoginBean(request);
    	Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable)sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean)hashSession.get("AccPeriodAcc");
		
		String profitLoss = this.getMessage(request,"sysAcc.lb.adjustExchange");
		String settle = this.getMessage(request, "SysAcc.lb.settle");
		
		MessageResources resources = null;
        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
		    resources = (MessageResources) ob;
		}
		
		/* 期末调汇*/
		Result result = mgt.adjustExchange(keyIds, currentexchange, adjustexchange, accCode, 
				accTime, credTypeID, recordCommon, accPeriodBean, loginBean, profitLoss, settle, this.getLocale(request), resources);
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, loginBean.getId(),loginBean.getEmpFullName(),loginBean.getSunCmpClassCode(), "期末调汇:"+accPeriodBean.getAccYear()+"."+accPeriodBean.getAccMonth(), "期末调汇", "月结","期末调汇");
			// 期末调汇成功
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.adjustExchangeSuccess")).setAlertRequest(request);
			/**EchoMessage.success().add(
					getMessage(request, "common.msg.adjustExchangeSuccess"))
					.setBackUrl("/AdjustExchangeAction.do?operation=4&optype=queryExchange")
					.setAlertRequest(request);*/
		} else {
			EchoMessage.success().add("期末调汇失败");
			/**EchoMessage.error().add(result.getRetCode(), request).setBackUrl(
					"/AdjustExchangeAction.do?operation=4&optype=queryExchange").setRequest(
					request);*/
		}
		return getForward(request, mapping, "alert");
	}
}
