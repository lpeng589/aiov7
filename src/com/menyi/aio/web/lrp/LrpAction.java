package com.menyi.aio.web.lrp;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.billNumber.BillNo.BillNoUnit;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.mrp.MrpMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;

public class LrpAction extends MgtBaseAction {
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
	private LrpMgt mgt = new LrpMgt();
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = getForward(request, mapping, "alert");
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		request.setAttribute("type", type);
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		
		if("showMrp".equals(type)){
			forward=showLrp(mapping, form, request, response);
		}else if("compute".equals(type)){
			forward=compute(mapping, form, request, response);
		}else if("buy".equals(type)){
			forward=showLrpBuy(mapping, form, request, response);
		}else if("prod".equals(type)){
			forward=showLrpProd(mapping, form, request, response);
		}else if("prodGoods".equals(type)){
			forward=showLrpProdGoods(mapping, form, request, response);
		}else if("toWorkOrder".equals(type)){
			forward=toWorkOrder(mapping, form, request, response);
		}else if("toMerge".equals(type)){
			forward=toMerge(mapping, form, request, response);
		}else if("toBuy".equals(type)){
			forward=toBuy(mapping, form, request, response);
		}else if("update".equals(type)){
			forward=update(mapping, form, request, response);
		}else{//默认进入lrp界面
			forward=showLrp(mapping, form, request, response);
		}
		return forward;
	}
	
	/**
	 * 新批次算法显示LRP界面 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String tableName = request.getParameter("tableName");
		String fieldName = request.getParameter("fieldName");
		String value = request.getParameter("value");
		String id = request.getParameter("id");
		Result rs = mgt.update(tableName, fieldName, value,id,loginBean.getId());
		HashMap ret = new HashMap();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ret.put("code", "OK");
		}else{
			ret.put("code", "ERROR");
			ret.put("msg", rs.retVal==null?"执行失败":rs.retVal);
		}
		request.setAttribute("msg", gson.toJson(ret));
		return getForward(request, mapping, "blank");

	}
	
	/**
	 * 新批次算法显示LRP界面 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward compute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		HashMap map  =gson.fromJson(request.getParameter("data"), HashMap.class);
		String computeNo = map.get("computeNo")+"";
		String isloss = map.get("isloss")+"";
		String isLowQty = map.get("isLowQty")+"";
		String isReplace = map.get("isReplace")+"";
		String billType = map.get("billType")+"";
		ArrayList list = (ArrayList)map.get("list");
		Result rs = mgt.compute(computeNo, isloss, isLowQty, isReplace, billType, loginBean.getId(), list);
		HashMap ret = new HashMap();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ret.put("code", "OK");
		}else{
			ret.put("code", "ERROR");
			ret.put("msg", rs.retVal==null?"执行失败":rs.retVal);
		}
		request.setAttribute("msg", gson.toJson(ret));
		return getForward(request, mapping, "blank");

	}
	/**
	 * 新批次算法显示LRP界面 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showLrpBuy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String computeId=request.getParameter("computeId");
		request.setAttribute("computeId", computeId);
		request.setAttribute("type", request.getParameter("type"));
		if(computeId==null || computeId.equals("")){
			//进入计算单号选择界面
			//进入计算单号选择界面
			return selCompute(mapping, form, request, response);
		}else{
			Result rs = mgt.queryBuy(computeId);
			ArrayList<HashMap> list = new ArrayList<HashMap>();
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				list = (ArrayList<HashMap>)rs.retVal;
				for(HashMap map :list){
					Set<Map.Entry> set =map.entrySet();
					for(Map.Entry o :set){
						if(o.getValue() instanceof BigDecimal){
							String v = formatNumberS(o.getValue());
							map.put(o.getKey(), v);
						}
					}
				}
			}
			request.setAttribute("list", rs.retVal);
			return getForward(request, mapping, "showLRPBuy");
		}

	}
	
	/**
	 * 新批次算法显示生产计划的物料
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showLrpProdGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String ProdPlanId=request.getParameter("ProdPlanId");
		request.setAttribute("type", request.getParameter("type"));
		ArrayList<HashMap> list = mgt.queryProdGoods(ProdPlanId);
		for(HashMap map :list){
			Set<Map.Entry> set =map.entrySet();
			for(Map.Entry o :set){
				if(o.getValue() instanceof BigDecimal){
					String v = formatNumberS(o.getValue());
					map.put(o.getKey(), v);
				}
			}
		}
		request.setAttribute("list", list);
		return getForward(request, mapping, "showLRPProdGoods");
		
	}
	/**
	 * 新批次算法显示LRP界面 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showLrpProd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String computeId=request.getParameter("computeId");
		request.setAttribute("type", request.getParameter("type"));
		if(computeId==null || computeId.equals("")){
			//进入计算单号选择界面
			return selCompute(mapping, form, request, response);
		}else{
			Result rs = mgt.queryProd(computeId);
			ArrayList<HashMap> list = new ArrayList<HashMap>();
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				list = (ArrayList<HashMap>)rs.retVal;
				for(HashMap map :list){
					Set<Map.Entry> set =map.entrySet();
					for(Map.Entry o :set){
						if(o.getValue() instanceof BigDecimal){
							String v = formatNumberS(o.getValue());
							map.put(o.getKey(), v);
						}
					}
				}
			}
			request.setAttribute("list", rs.retVal);
			return getForward(request, mapping, "showLRPProd");
		}
	}
	/**
	 * 计算选择界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward selCompute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String computeNo=request.getParameter("computeNo");
		String billType=request.getParameter("billType");
		String BillNo=request.getParameter("BillNo");
		String GoodsNumber=request.getParameter("GoodsNumber");
		String GoodsFullName=request.getParameter("GoodsFullName");
		if(startDate==null){
			Long time = new Date().getTime();
			time = time - 30*24*60*60000l;
			Date d = new Date(time);
			startDate = BaseDateFormat.format(d, BaseDateFormat.yyyyMMdd);
		}
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("computeNo", computeNo);
		request.setAttribute("billType", billType);
		request.setAttribute("BillNo", BillNo);
		request.setAttribute("GoodsNumber", GoodsNumber);
		request.setAttribute("GoodsFullName", GoodsFullName);

		Result rs = mgt.selCompute(startDate, endDate, computeNo, billType, BillNo, GoodsNumber, GoodsFullName);
		ArrayList<HashMap<String,Object>> list = (ArrayList<HashMap<String,Object>>)rs.retVal;
		for(int i=0;i<list.size();i++){
			if(i>0){
				if(list.get(i).get("id").equals(list.get(i-1).get("id"))){
					//id相同是同一个计算单号
					HashMap<String,Object> map =list.get(i);
					map.put("BillDate", "");
					map.put("BillNo", "");
					map.put("EmpFullName", "");
					map.put("BillType", "");
				}
			}
		}
		request.setAttribute("list", list);
		return getForward(request, mapping, "selCompute");
	}
	
	public static String formatNumberS(Object num) {

		NumberFormat nf = null;
		nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(true);
		int dts = Integer.parseInt(GlobalsTool.getSysSetting("DigitsQty"));

		nf.setMaximumFractionDigits(dts);
		nf.setMinimumFractionDigits(0);
		try {
			return nf.format(num);
		} catch (Exception e) {
			return (String) num;
		}
	}
	
	/**
	 * 新批次算法显示LRP界面 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showLrp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String key = "PDLRPCompute_BillNo";
		String bNo = "";
		BillNo billno = BillNoManager.find("PDLRPCompute_BillNo");
		if(billno != null){
			if (!billno.isFillBack()) {//不启用编号连续时，就要生成编号
				String valStr = BillNoManager.find(key, new HashMap(),loginBean);
				bNo = valStr;
			}else{
				BillNoUnit unit  = billno.getInvers(new HashMap<String, String>(), loginBean);
				bNo = unit.getValStr();
			}
		}
		request.setAttribute("billNo", bNo);
		return getForward(request, mapping, "showLRP");

	}
	
	protected ActionForward toWorkOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String data = request.getParameter("data");
		ArrayList<String> ids = gson.fromJson(data, ArrayList.class);
		Result rs = mgt.toWorkOrder(ids, loginBean);
		HashMap ret = new HashMap();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ret.put("code", "OK");
		}else{
			ret.put("code", "ERROR");
			ret.put("msg", rs.getRetVal());
		}
		request.setAttribute("msg", gson.toJson(ret));
		return getForward(request, mapping, "blank");

	}
	
	protected ActionForward toMerge(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String keyId = request.getParameter("keyId");
		Result rs = mgt.toMerge(keyId, loginBean);
		HashMap ret = new HashMap();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ret.put("code", "OK");
		}else{
			ret.put("code", "ERROR");
			ret.put("msg", rs.getRetVal());
		}
		request.setAttribute("msg", gson.toJson(ret));
		return getForward(request, mapping, "blank");

	}
	
	protected ActionForward toBuy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String computeId = request.getParameter("computeId");
		Result rs = mgt.toBuy(computeId, loginBean);
		HashMap ret = new HashMap();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ret.put("code", "OK");
		}else{
			ret.put("code", "ERROR");
			ret.put("msg", rs.getRetVal());
		}
		request.setAttribute("msg", gson.toJson(ret));
		return getForward(request, mapping, "blank");

	}

}
