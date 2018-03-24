package com.menyi.aio.web.customize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.ApplyGoodsDecBean;
import com.menyi.aio.bean.BuyOrderBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class ApplyGoodsBillAction extends MgtBaseAction {
	final ApplyGoodsBillSum sum = new ApplyGoodsBillSum();

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int opration = 0 == getOperation(request)?4:getOperation(request);
		
		ActionForward forward = null;
		switch (opration) {
		case OperationConst.OP_ADD:
			forward = addBuyOrder(mapping, form, request, response);
			break;
		default:
			break;
		}
		return forward;
	}
	
	private ActionForward addBuyOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ActionForward forward = null;
		String ids = request.getSession().getAttribute("ids").toString();
		
		
		List<ApplyGoodsDecBean> applys = (List<ApplyGoodsDecBean>) 
				request.getSession().getAttribute("applys");
			
		List isPriceGoodsNo = new ArrayList();
		List isBillDateGoodsNo = new ArrayList();
		for (ApplyGoodsDecBean bean : applys) {
			if(bean.getOOSNumber() != 0){
				if(bean.getCompanyCondition() == 1){
					isBillDateGoodsNo.add(bean);
				}else if(bean.getCompanyCondition() == -1){
					isPriceGoodsNo.add(bean);
				}
			}
		}
			
		Map<String, BuyOrderBean> companyByDate = sum.getCompanyCodeByDate(isBillDateGoodsNo);
		Map<String, BuyOrderBean> companyByPrice = sum.getCompanyCodeByPrice(isPriceGoodsNo);
		
		Map<String,List> buyOrders = new HashMap<String, List>();
		
		//将相同往来的商品集合在一起
		for (String key : companyByDate.keySet()) {
			List values = new ArrayList();
			boolean isAdd = false;
			for (String object : companyByDate.keySet()) {
				if(companyByDate.get(key).getCompanyCode().equals(companyByDate.get(object).getCompanyCode())){
					values.add(companyByDate.get(object));
					companyByDate.remove(object);
					isAdd = true;
				}
			}
			if(!isAdd){
				values.add(companyByDate.get(key));
			}
			buyOrders.put(companyByDate.get(key).getCompanyCode().toString(), values);
		}	
			
		for (String key : companyByPrice.keySet()) {
			List values = new ArrayList();
			boolean isAdd = false;
			for (String object : companyByPrice.keySet()) {
				if(companyByPrice.get(key).getCompanyCode().equals(companyByPrice.get(object).getCompanyCode())){
					values.add(companyByPrice.get(object));
					//companyByPrice.remove(object);
					isAdd = true;
				}
			}
			if(!isAdd){
				values.add(companyByPrice.get(key));
			}
			for (String object : buyOrders.keySet()) {
				if(object.equals(companyByPrice.get(key).getCompanyCode())){
					values.addAll(buyOrders.get(object));
					break;
				}
			}
			buyOrders.put(companyByPrice.get(key).getCompanyCode().toString(), values);
		}	
		if(buyOrders.size()==0){
			EchoMessage.error().add(
					getMessage(request, "ApplyGoodsBill.msg.notSureCom")).setBackUrl(
					"/UserFunctionQueryAction.do?tableName=tblApplyGoodsBill").setAlertRequest(request);

			return getForward(request, mapping, "message");
		}
		Result result = sum.updateApplyGoodsBillState(ids);
		if(0 < Integer.parseInt(result.getRetVal().toString())){
			String id = this.getLoginBean(request).getId();
		
			result = sum.addBuyOrder(buyOrders, id,this.getLoginBean(request).getSunCmpClassCode());
			if (ErrorCanst.DEFAULT_SUCCESS == result.getRetCode()) {
				
				EchoMessage.success().add(
						getMessage(request, "common.msg.SumSuccess")).setBackUrl(
						"/UserFunctionQueryAction.do?tableName=tblApplyGoodsBill").setAlertRequest(request);

				return getForward(request, mapping, "message");
			}
			EchoMessage.error().add(getMessage(request, "common.msg.SumFailture"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}	
		
		return forward;
	}

}
