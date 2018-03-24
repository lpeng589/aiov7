 package com.menyi.aio.web.mrp;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.GoodsAttributeBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserSearchForm;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 * 物料运算
 * 	
 * @author LiuZhigao
 * 
 */
public class MrpAction extends MgtBaseAction {
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
	private MrpMgt mgt = new MrpMgt();
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = getForward(request, mapping, "alert");

		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		request.setAttribute("type", type);
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		
		if("showMrp".equals(type)){
			forward=showMrp(mapping, form, request, response);
		}else if("showBuyAppication".equals(type)){
			forward=showBuyAppication(mapping, form, request, response);
		}else if("toBuyApplication".equals(type)){
			forward=toBuyApplication(mapping, form, request, response);
		}else   if("computeMRP".equals(type)){
			forward=computeMRP(mapping, form, request, response);
		}else   if("computeMRPPR".equals(type)){
			forward=computeMRPPR(mapping, form, request, response);
		}else   if("deleteComputeMRPPR".equals(type)){
			forward=deleteComputeMRPPR(mapping, form, request, response);
		}else if("hasBuy".equals(type)){
			forward=hasBuy(mapping, form, request, response);
		}else if("selSalesOrder".equals(type)){
			forward=selSalesOrder(mapping, form, request, response);
		}else if("selGoods".equals(type)){
			forward=selGoods(mapping, form, request, response);
		}else if("selBom".equals(type)){
			forward=selBom(mapping, form, request, response);
		}else if("openBom".equals(type)){
			forward=openBom(mapping, form, request, response);
		}else if("saveBom".equals(type)){
			forward=saveBom(mapping, form, request, response);
		}else if("showBom".equals(type)){
			forward=showBom(mapping, form, request, response);
		}else if("showWorkOrder".equals(type)){
			forward=showWorkOrder(mapping, form, request, response);
		}else if("toWorkOrder".equals(type)){
			forward=toWorkOrder(mapping, form, request, response);	
//		}else if("toBuy".equals(type)){
//			forward=toBuy(mapping, form, request, response);
//		}else if("hasBuy".equals(type)){
//			forward=hasBuy(mapping, form, request, response);
		}else if("selSalesOrderForPD".equals(type)){
			forward=selSalesOrderForPD(mapping, form, request, response);	
		}else if("showSalesOrderLack".equals(type)){
			forward=showSalesOrderLack(mapping, form, request, response);	
		}else if("toProduceRequire".equals(type)){
			forward=toProduceRequire(mapping, form, request, response);	
		}else if("showMRPPR".equals(type)){
			forward=showMRPPR(mapping, form, request, response);	
		}else{
			forward=showMrp(mapping, form, request, response);
		}
		return forward;
	
	}
	
	protected ActionForward toProduceRequire(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String[] SourceIds= request.getParameterValues("SourceId");
		String[] lackQtys= request.getParameterValues("lackQty");
		
		ArrayList<HashMap> goodList = new ArrayList<HashMap>();
		if(SourceIds==null){
			EchoMessage.error().add("没有缺货，不需要下制造需求单或请购单").setBackUrl("/Mrp.do?type=showSalesOrderLack").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		for(int i=0;i<SourceIds.length;i++){
			String SourceId =SourceIds[i];
			String[] ss = SourceId.split(":");
			HashMap map = new HashMap();
			map.put("GoodsCode", ss[0]);
			map.put("attrType", ss[1]);
			map.put("BatchNo", ss[2]);
			map.put("yearNO", ss[3]);
			map.put("Hue", ss[4]);
			map.put("Inch", ss[5]);
			map.put("Qty", lackQtys[i]);
			goodList.add(map);
		}
		
		
		Result rs = mgt.toProduceRequire(goodList, loginBean, request);
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			HashMap map = (HashMap)rs.retVal;
			String BuykeyId=(String)map.get("BuykeyId");
			String ProdkeyId=(String)map.get("ProdkeyId");
			String msg = "执行成功，共生成";
			if(BuykeyId !=null && BuykeyId.length() > 0){
				msg +="一张请购单,";
			}
			if(ProdkeyId !=null && ProdkeyId.length() > 0){
				msg +="一张制造需求单,";
			}
			msg +="别忘了审核过帐!";
			EchoMessage.success().add(msg).setBackUrl("/Mrp.do?type=selSalesOrderForPD").setNotAutoBack().setAlertRequest(request);
		}else {
			EchoMessage.error().add(rs.retVal==null||rs.retVal.equals("")?"执行失败":rs.retVal+"").setBackUrl("/Mrp.do?type=selSalesOrderForPD").setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	/**
	 * 订单缺料选择订单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward selSalesOrderForPD(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		ArrayList<String[]> ComFList = new ArrayList<String[]>();
		for (String[] shows : BaseEnv.reportShowSet) { 
			if ("tblCompany_2".equals(shows[0]) && "1".equals(shows[2])) {
				DBFieldInfoBean fb =  GlobalsTool.getFieldBean("tblCompany." + shows[1]);	
		        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
		        String display = fb.getDisplay().get(this.getLocale(request).toString());
		        ComFList.add(new String[]{shows[1],display,fb.getWidth()+""});
			}
		}
		
		String BillDate1= request.getParameter("BillDate1");
		String BillDate2= request.getParameter("BillDate2");
		String BillNo= request.getParameter("BillNo");
		String CompanyCode= request.getParameter("CompanyCode");
		
		request.setAttribute("BillDate1", BillDate1);
		request.setAttribute("BillDate2", BillDate2);
		request.setAttribute("BillNo", BillNo);
		request.setAttribute("CompanyCode", CompanyCode);
		
		
		request.setAttribute("ComFList", ComFList);
		
		
		Result rs = mgt.selSalesOrderForPD(BillDate1, BillDate2, BillNo, CompanyCode, ComFList);
		
		
		request.setAttribute("salesList", rs.getRetVal());
		
		return getForward(request, mapping, "selSalesOrderForPD");

	}
	/**
	 * 显示订单的缺料明细
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showSalesOrderLack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] sids = request.getParameterValues("keyId"); 
		LoginBean loginBean = getLoginBean(request);
		ArrayList<String[]> GoodsFList = new ArrayList<String[]>();
		ArrayList<String[]> ComFList = new ArrayList<String[]>();
		for (String[] shows : BaseEnv.reportShowSet) { 
			if ("tblGoods".equals(shows[0]) && "1".equals(shows[2])) {
				DBFieldInfoBean fb =  GlobalsTool.getFieldBean("tblGoods." + shows[1]);	
		        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
		        String display = fb.getDisplay().get(this.getLocale(request).toString());
		        GoodsFList.add(new String[]{shows[1],display,fb.getWidth()+""});
			}
		}
		
		request.setAttribute("GoodsFList", GoodsFList);
		
		
		Result rs = mgt.showSalesOrderLack(sids,GoodsFList);
		
		request.setAttribute("GoodsCode", request.getParameter("GoodsCode"));
		
		request.setAttribute("salesList", rs.getRetVal());
		
		return getForward(request, mapping, "showSalesOrderLack");

	}
	
	/**
	 * 显示下达工令界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showWorkOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String mrpId = request.getParameter("mrpId");
		
		ArrayList GoodsFList = new ArrayList();
		for (String[] shows : BaseEnv.reportShowSet) { 
			if ("tblGoods".equals(shows[0]) && "1".equals(shows[2])) {
				DBFieldInfoBean fb =  GlobalsTool.getFieldBean("tblGoods." + shows[1]);	
		        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
		        String display = fb.getDisplay().get(this.getLocale(request).toString());
		        GoodsFList.add(new String[]{shows[1],display,fb.getWidth()+""});
			}
		}
		
		request.setAttribute("GoodsFList", GoodsFList);
		
		Result rs = mgt.showWorkOrder(mrpId);
		HashMap rsmap = new HashMap();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("workList", rs.retVal);
		}else{
			EchoMessage.error().add("执行错误"+rs.getRetVal()).setClose().setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		return getForward(request, mapping, "showWorkOrder");

	}
	
	/**
	 * 下达工令
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward toWorkOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String mrpId=request.getParameter("mrpId");
		String data = request.getParameter("data");
		ArrayList<HashMap> list =gson.fromJson(data, ArrayList.class);
		Result rs = mgt.toWorkOrder(mrpId, list,loginBean);
		
		HashMap rsmap = new HashMap();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			rsmap.put("code", "OK");	
			rsmap.put("keyId", rs.retVal);
		}else{
			rsmap.put("code", "ERROR");	
			rsmap.put("msg", rs.retVal);
		}
		String msg = gson.toJson(rsmap);
		request.setAttribute("msg", msg);	
		return getForward(request, mapping, "blank");

	}
	
	/**
	 * 检查是否已下请购
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward hasBuy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		Result rs = mgt.hasBuy();
		HashMap rsmap = new HashMap();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			rsmap.put("code", "OK");	
			rsmap.put("count", ((ArrayList<Object[]>)rs.retVal).get(0)[0]);
		}else{
			rsmap.put("code", "ERROR");	
			rsmap.put("msg", rs.retVal);
		}
		String msg = gson.toJson(rsmap);
		request.setAttribute("msg", msg);	
		return getForward(request, mapping, "blank");

	}
	
	protected ActionForward showBuyAppication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		request.setAttribute("from", request.getParameter("from"));

		ArrayList GoodsFList = new ArrayList();
		
		for (String[] shows : BaseEnv.reportShowSet) { 
			if ("tblGoods".equals(shows[0]) && "1".equals(shows[2])) {
				DBFieldInfoBean fb =  GlobalsTool.getFieldBean("tblGoods." + shows[1]);	
		        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
		        String display = fb.getDisplay().get(this.getLocale(request).toString());
		        GoodsFList.add(new String[]{shows[1],display,fb.getWidth()+""});
			}
		}
		
		request.setAttribute("GoodsFList", GoodsFList);
		
		String mrpId= request.getParameter("mrpId");
		request.setAttribute("mrpId", mrpId);
		
		Result rs = mgt.showBuyApplication(mrpId); 
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<HashMap> list = (ArrayList<HashMap>)rs.retVal;
			for(HashMap map :list){
				Double reTimes = Double.parseDouble(map.get("reTimes")+"");
				Double qty = Double.parseDouble(map.get("lackQty")+"");
				if(reTimes>1){ //补货陪量大于1时，要按补货陪量取整
					//为防止补货偣量出现小数，因些都放大8个0取整
					long breTimes = (long)(reTimes*1000000);
					long bqty = (long)(qty*1000000);
					bqty = ((bqty+breTimes-1)/breTimes) *breTimes;
					qty = bqty/1000000.0;
					
				}
				map.put("buyQty", qty);
			}
		}else{
			request.setAttribute("ErrorMsg", rs.getRetVal()==null||rs.getRetVal().equals("")?"执行错误":rs.retVal);
		}
		request.setAttribute("buyList", rs.getRetVal());
		
		return getForward(request, mapping, "showBuyApplication");

	}
	/**
	 * 新算法下请购 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward toBuyApplication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String data = request.getParameter("data");
		String mrpId = request.getParameter("mrpId");
		ArrayList<HashMap> goodsList =gson.fromJson(data, ArrayList.class);
		Result rs = mgt.toBuyApplication(mrpId,goodsList, this.getLoginBean(request), request);
		HashMap rsmap = new HashMap();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			rsmap.put("code", "OK");	
			//rsmap.put("keyId", ((HashMap)rs.retVal).get("keyId"));
		}else{
			rsmap.put("code", "ERROR");	
			rsmap.put("msg", rs.retVal);
		}
		String msg = gson.toJson(rsmap);
		request.setAttribute("msg", msg);	
		return getForward(request, mapping, "blank");

	}
	/**
	 * 新算法第二步：从规划单进行 运算 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward computeMRP(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String data = request.getParameter("data");
		ArrayList list =gson.fromJson(data, ArrayList.class);
		
		Result rs = mgt.computeMRP(list,this.getLoginBean(request));
		HashMap ret = new HashMap();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ret.put("code", "OK");
		}else{
			ret.put("code", "ERROR");
			ret.put("msg", rs.retVal==null || rs.retVal.equals("")?"运算失败":rs.retVal);
		}
		request.setAttribute("msg", gson.toJson(ret));
		return getForward(request, mapping, "blank");

	}
	/**
	 * 从制造需求单计算物料需求
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward computeMRPPR(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String data = request.getParameter("data");
		ArrayList list =gson.fromJson(data, ArrayList.class);
		
		Result rs = mgt.computeMRPPR(list,this.getLoginBean(request));
		HashMap ret = new HashMap();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ret.put("code", "OK");
		}else{
			ret.put("code", "ERROR");
			ret.put("msg", rs.retVal==null || rs.retVal.equals("")?"运算失败":rs.retVal);
		}
		request.setAttribute("msg", gson.toJson(ret));
		return getForward(request, mapping, "blank");

	}
	/**
	 * 从制造需求单计算物料需求
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward deleteComputeMRPPR(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String data = request.getParameter("data");
		ArrayList list =gson.fromJson(data, ArrayList.class);
		
		Result rs = mgt.deleteComputeMRPPR(list,this.getLoginBean(request));
		HashMap ret = new HashMap();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ret.put("code", "OK");
		}else{
			ret.put("code", "ERROR");
			ret.put("msg", rs.retVal==null || rs.retVal.equals("")?"删除运算失败":rs.retVal);
		}
		request.setAttribute("msg", gson.toJson(ret));
		return getForward(request, mapping, "blank");

	}
	
	/**
	 * 用制造需求单制造物料需求
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showMRPPR(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String BillNo=request.getParameter("BillNo");
		String GoodsNumber=request.getParameter("GoodsNumber");
		request.setAttribute("BillNo", BillNo);
		request.setAttribute("GoodsNumber", GoodsNumber);
		Result rs = mgt.showMRPPR(BillNo,GoodsNumber);
		request.setAttribute("goodList", rs.getRetVal());
		
		return getForward(request, mapping, "showMRPPR");

	}
	
	/**
	 * 新算法第一步：选择销售订单和制造需求单 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showMrp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		Result rs = mgt.showMrp();
		request.setAttribute("goodList", rs.getRetVal());
		
		return getForward(request, mapping, "showMRP");

	}
	
	/**
	 * 第一步：选择销售订单和制造需求单 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward selSalesOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String BillDate1=request.getParameter("BillDate1");
		String BillDate2=request.getParameter("BillDate2");
		String BillType=request.getParameter("BillType");
		String BillNo=request.getParameter("BillNo");
		String CompanyCode=request.getParameter("CompanyCode");
		String GoodsCode=request.getParameter("GoodsCode");
		String orderBy=request.getParameter("orderBy");
		
		request.setAttribute("BillDate1", BillDate1);
		request.setAttribute("BillDate2", BillDate2);
		request.setAttribute("BillType", BillType);
		request.setAttribute("BillNo", BillNo);
		request.setAttribute("CompanyCode", CompanyCode);
		request.setAttribute("GoodsCode", GoodsCode);
		request.setAttribute("orderBy", orderBy);
		
		ArrayList<String[]> GoodsFList = new ArrayList<String[]>();
		ArrayList<String[]> ComFList = new ArrayList<String[]>();
		for (String[] shows : BaseEnv.reportShowSet) { 
			if ("tblGoods".equals(shows[0]) && "1".equals(shows[2])) {
				DBFieldInfoBean fb =  GlobalsTool.getFieldBean("tblGoods." + shows[1]);	
		        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
		        String display = fb.getDisplay().get(this.getLocale(request).toString());
		        GoodsFList.add(new String[]{shows[1],display,fb.getWidth()+""});
			}else if ("tblCompany_2".equals(shows[0]) && "1".equals(shows[2])) { 
				DBFieldInfoBean fb =  GlobalsTool.getFieldBean("tblCompany." + shows[1]);	
		        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
		        String display = fb.getDisplay().get(this.getLocale(request).toString());
		        ComFList.add(new String[]{shows[1],display,fb.getWidth()+""});
			}
		}
		
		request.setAttribute("GoodsFList", GoodsFList);
		request.setAttribute("ComFList", ComFList);
		
		
		Result rs = mgt.selSalesOrder(BillDate1, BillDate2, BillType, BillNo, CompanyCode, GoodsCode, GoodsFList,ComFList,orderBy);
		
		request.setAttribute("saleOrders", request.getParameter("saleOrders"));
		request.setAttribute("GoodsCode", request.getParameter("GoodsCode"));
		
		request.setAttribute("salesList", rs.getRetVal());
		
		return getForward(request, mapping, "selSalesOrder");

	}
	/**
	 * 第二步：选择商品
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward selGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		
		ArrayList<String[]> GoodsFList = new ArrayList<String[]>();
		ArrayList<String[]> ComFList = new ArrayList<String[]>();
		for (String[] shows : BaseEnv.reportShowSet) { 
			if ("tblGoods".equals(shows[0]) && "1".equals(shows[2])) {
				DBFieldInfoBean fb =  GlobalsTool.getFieldBean("tblGoods." + shows[1]);	
		        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
		        String display = fb.getDisplay().get(this.getLocale(request).toString());
		        GoodsFList.add(new String[]{shows[1],display,fb.getWidth()+""});
			}else if ("tblCompany_2".equals(shows[0]) && "1".equals(shows[2])) { 
				DBFieldInfoBean fb =  GlobalsTool.getFieldBean("tblCompany." + shows[1]);	
		        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
		        String display = fb.getDisplay().get(this.getLocale(request).toString());
		        ComFList.add(new String[]{shows[1],display,fb.getWidth()+""});
			}
		}
		request.setAttribute("GoodsFList", GoodsFList);
		request.setAttribute("ComFList", ComFList);
		
		String keyIds[] = request.getParameterValues("keyId");
		HashMap<String,HashMap> goodsMap = new HashMap<String,HashMap>();//对商品进行分组
		for(String keyId:keyIds){
			String Source = request.getParameter("SourceId_"+keyId);
			String NeedQty = request.getParameter("NeedQty_"+keyId);
			String values[] = Source.split(":");
			String GoodsCode = values[0];
			String BillType = values[1];
			String BillId = values[2];
			String totalQty = values[3];
			String MRPQty = values[4];
			String BillNo = values[5];
			String BillDate = values[6];
			String CompanyCode = values[7];
			String Urgency = values[8];
			//String gstr = request.getParameter("Goods_"+keyId);
			String cstr = request.getParameter("Coms_"+keyId);
			HashMap goodsItem = goodsMap.get(GoodsCode);
			if(goodsItem==null){
				goodsItem = new HashMap();
				goodsMap.put(GoodsCode, goodsItem);
				goodsItem.put("GoodsCode",GoodsCode);
				goodsItem.put("NeedQty", 0d);
				goodsItem.put("BillList", new ArrayList());
				//goodsItem.put("goods", gstr);
			}
			goodsItem.put("NeedQty", (Double)goodsItem.get("NeedQty")+ Double.parseDouble(NeedQty));
			HashMap billMap = new HashMap();
			billMap.put("BillId", BillId);
			billMap.put("BillType", BillType);
			billMap.put("keyId", keyId);
			billMap.put("GoodsCode", GoodsCode);
			billMap.put("NeedQty", NeedQty);
			billMap.put("TotalQty", totalQty);
			billMap.put("MRPQty", MRPQty);
			billMap.put("BillNo", BillNo);
			billMap.put("BillDate", BillDate);
			billMap.put("CompanyCode", CompanyCode);
			billMap.put("Urgency", Urgency);
			if(cstr!= null && cstr.length() >0){
				String cstrs[]=cstr.split("#\\|#");
				for(String str : cstrs){
					billMap.put(str.split("#;#")[0], str.split("#;#")[1]);
				}
			}
			((ArrayList)(goodsItem.get("BillList"))).add(billMap);
		}
		

		Result rs = mgt.selGoods(goodsMap);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add("执行失败").setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		
		
		
		request.setAttribute("GoodsList", goodsMap.values());	
		return getForward(request, mapping, "selGoods");

	}
	/**
	 * 第三步：选择BOM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward selBom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		String BillDate1=request.getParameter("BillDate1");
		String BillDate2=request.getParameter("BillDate2");
		String BillType=request.getParameter("BillType");
		String BillNo=request.getParameter("BillNo");
		String ComFullNamen=request.getParameter("ComFullNamen");
		String EmpFullName=request.getParameter("EmpFullName");
		String DeptFullName=request.getParameter("DeptFullName");
		
		
		
		request.setAttribute("BillDate1", BillDate1);
		request.setAttribute("BillDate2", BillDate2);
		request.setAttribute("BillType", BillType);
		request.setAttribute("BillNo", BillNo);
		request.setAttribute("ComFullNamen", ComFullNamen);
		request.setAttribute("EmpFullName", EmpFullName);
		request.setAttribute("DeptFullName", DeptFullName);
		
		String saleOrders = request.getParameter("saleOrders");
		request.setAttribute("saleOrders", saleOrders);
		String GoodsCode = request.getParameter("GoodsCode");
		request.setAttribute("GoodsCode", GoodsCode);
		
		String mrpId= IDGenerater.getId();
		request.setAttribute("mrpId", mrpId);
		Result rs = mgt.selBom(GoodsCode,saleOrders,mrpId);
		
		if(rs.retVal instanceof Object[]){
			
			Object[] os = (Object[])rs.retVal;
			request.setAttribute("bomVersionList", os[2]);
			request.setAttribute("qty", os[3]);
			request.setAttribute("salesList", os[0]);
			request.setAttribute("goodsObj",  os[1]);
			return getForward(request, mapping, "selBom");
		}else{
			request.setAttribute("msg", rs.retVal+"");
			return selGoods(mapping, form, request, response);
		}

		

	}
	/**
	 * 第四步:ajax展开BOM树
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward openBom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String bomId = request.getParameter("bomId");
		String mrpId = request.getParameter("mrpId");
		String parentCode = request.getParameter("parentCode");
		Result rs  = mgt.openBom(bomId,mrpId,parentCode);
		HashMap rsmap = new HashMap();
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			rsmap.put("code", "ERROR");	
		}else{ 
			rsmap.put("code", "OK");
			rsmap.put("list", rs.retVal);
			rsmap.put("bomId", bomId);
		}
		String msg  = gson.toJson(rsmap);
		request.setAttribute("msg", msg);	
		return getForward(request, mapping, "blank");

	}
	/**
	 * 保存MRP运算结果
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward saveBom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String data = request.getParameter("data");
		HashMap<String, String> values = gson.fromJson(data, HashMap.class);
		HashMap rsmap = new HashMap();
		
		
		Result rs = mgt.saveBom(values,this.getLoginBean(request));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			rsmap.put("code", "OK");	
			rsmap.put("keyId", rs.retVal);
			rsmap.put("BuyAppId", values.get("BuyAppId"));
		}else{
			rsmap.put("code", "ERROR");	
			rsmap.put("msg", rs.retVal==null?"保存失败":rs.retVal);	
		}
		String msg = gson.toJson(rsmap);
		request.setAttribute("msg", msg);	
		return getForward(request, mapping, "blank");

	}
	/**
	 * 运算保存后，展示MRP运算结果，
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward showBom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		Result rs = mgt.showBom(request.getParameter("keyId"));
		Object[] os = (Object[])rs.retVal;
		request.setAttribute("bomList", os[2]);
		request.setAttribute("qty", os[3]);
		request.setAttribute("salesList", os[0]);
		request.setAttribute("goodsObj",((ArrayList)os[1]).size() >0? ((ArrayList)os[1]).get(0):"");
		return getForward(request, mapping, "showBom");

	}

//	/**
//	 * 下达请购单
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	protected ActionForward toBuy(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		LoginBean loginBean = getLoginBean(request);
//		Result rs = mgt.toBuy(request.getParameter("keyId"),request);
//		HashMap rsmap = new HashMap();
//		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
//			rsmap.put("code", "OK");	
//			rsmap.put("keyId", ((HashMap)rs.retVal).get("keyId"));
//		}else{
//			rsmap.put("code", "ERROR");	
//			rsmap.put("msg", rs.retVal);
//		}
//		String msg = gson.toJson(rsmap);
//		request.setAttribute("msg", msg);	
//		return getForward(request, mapping, "blank");
//
//	}
	protected ActionForward doAuth(HttpServletRequest req,ActionMapping mapping) {
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
        
//        MOperation mo = (MOperation) getLoginBean(req).getOperationMap().get(parameter);
//        if (mo == null) {
//            //对整个模块无访问权限
//        	req.setAttribute("noback", true) ;
//            ActionForward forward = getForward(req, mapping, "message");
//            EchoMessage.error().add(getMessage(req,"common.msg.RET_NO_RIGHT_ERROR")).setRequest(req);
//            return forward;
//        }
        
		return null;
	}
}
