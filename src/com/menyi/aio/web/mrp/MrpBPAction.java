 package com.menyi.aio.web.mrp;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.GoodsAttributeBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
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
public class MrpBPAction extends MgtBaseAction {

	private Result rs = null;// 查询结果
	private ActionForward forward = null;// 跳转
	private ArrayList<String[]> usedProp = null;// 存放启用的商品属性
	private boolean flag = false;// 低级商品选中开关
	private MrpBPMgt mgt = new MrpBPMgt();
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// method用于判断正在执行的MRP步骤
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		String method = request.getParameter("method");// 获得执行方法
		if (method == null) {// MRP信息查询
			method = "mrpSel";
		} 
		if ("mrpSel".equals(method)||method.length()==0 || "backtoMrpManager".equals(method)) {
			mrpSel(mapping, form, request, response);

		} else if ("orderSel".equals(method)) {// MRP来源订单选取
			orderSel(mapping, form, request, response);
		} else if ("bomSelPro".equals(method)) {// 展开物料
			//如果系统配置显示MRP运算，则先展开物料，否则直接进入运算
			//if("true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())){
				//bomSelPro(mapping, form, request, response);
			//}else{
				bomDemand2(mapping, form, request, response);
			//}
		} else if ("bomDemand".equals(method)) {// MRP物料运算
			//如果系统配置显示MRP运算，则先展开物料，否则直接进入运算
			//if("true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())){
				//bomDemand(mapping, form, request, response);
			//}else{
				bomDemand2(mapping, form, request, response);
			//}
		} else if ("doProduceOrder".equals(method)) {// 生产任务单列表

			doProduceOrder(mapping, form, request, response);

		} else if ("uniteProSameGood".equals(method)) {// 合并生产任务单相同的商品

			uniteProSameGood(mapping, form, request, response);

		}else if ("doOrder".equals(method)||"doQuoteBuy".equals(method)) {// MRP采购订单,MRP下达请购单

			doOrder(mapping, form, request, response);
			//下达生产任务单后删除session 中的值
			request.getSession().removeAttribute("hashChildRep");

		}else if ("uniteSameGood".equals(method)) {//合并相同的商品

			uniteSameGood(mapping, form, request, response);

		} else if ("delMrp".equals(method)) {// 根据id删除数据

			delMrp(mapping, form, request, response);

		} else if ("simulateOrder".equals(method)) {// 模拟定单

			simulateOrder(mapping, form, request, response);

		} else if ("mrpDetSel".equals(method)) {// 查询MRP从表信息

			mrpDetSel(mapping, form, request, response);

		} else if ("saveMrp".equals(method)) { // MRP运算之后保存MRP信息

			saveMrp(mapping, form, request, response);

		} else if ("selGoods".equals(method)) {// 查询商品

			selGoods(mapping, form, request, response);

		}else if ("saveBuyOrder".equals(method)) {// 下达采购定单

			saveBuyOrder(mapping, form, request, response);

		} else if ("saveQuoteBuy".equals(method)) {// 下达请购单

			saveQuoteBuy(mapping, form, request, response);

		}else if ("saveProduceOrder".equals(method)) {// 保存生产任务单
			if("true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())){
				saveProduceOrder(mapping, form, request, response);
			}else{
				saveProduceOrderGM(mapping, form, request, response);
			}
			//下达生产任务单后删除session 中的值
			request.getSession().removeAttribute("hashChildRep");
		} else if ("backtoMa".equals(method)) {// 从MRP运算返回物料分析

			backtoMa(mapping, form, request, response);
		} else if ("savePrintData".equals(method)) {// 保存打印数据
			if("true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())){
				savePrintData(mapping, form, request, response);
			}else{
				savePrintData2(mapping, form, request, response);
			}
		}
		MOperation mop =  (MOperation) getLoginBean(request).getOperationMap().get("/MrpBP.do");
		request.setAttribute("MOID", mop.moduleId);
		request.setAttribute("propList", BaseEnv.attList);
		request.setAttribute("locale", this.getLocale(request).toString());
		
		return forward;
	}
	
	/**
	 * 查询商品
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void selGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int pageSize=request.getParameter("pageSize")==null?GlobalsTool.getPageSize():Integer.parseInt(request.getParameter("pageSize"));
		int pageNo=request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo"));
		
		String selGoodsNumber = request.getParameter("selGoodsNumber")==null?"":request.getParameter("selGoodsNumber");
		String selGoodsFullName = request.getParameter("selGoodsFullName")==null?"":request.getParameter("selGoodsFullName");
		String selGoodsSpec = request.getParameter("selGoodsSpec")==null?"":request.getParameter("selGoodsSpec");
		MOperation  mop = (MOperation)this.getLoginBean(request).getOperationMap().get("/MrpBP.do");
		ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
		Result rst = getGoods(this.getLoginBean(request).getId(),scopeRight,selGoodsNumber,selGoodsFullName,selGoodsSpec,pageSize,pageNo);
		request.setAttribute("list", rst.retVal);
		request.setAttribute("pageBar", this.pageBar(rst, request));
		request.setAttribute("selGoodsNumber", selGoodsNumber);
		request.setAttribute("selGoodsFullName", selGoodsFullName);
		request.setAttribute("selGoodsSpec", selGoodsSpec);
		forward = mapping.findForward("chooseGoods");
	}

	/**
	 * 返回物料分析
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void backtoMa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		rs.retVal = request.getSession().getAttribute("bomList");
		forward = mapping.findForward("matereilAnalyse");//

		// -------------------------------------------可以写成一个方法
		Object[] fieldZhs = getBomAndBomDetFields(null, null, 1);
		int[] fieldValIndex = new int[fieldZhs.length];
		int len = ((String[]) ((ArrayList<String[]>) rs.retVal).get(0)).length;
		for (int i = 0; i < fieldValIndex.length; i++) {
			fieldValIndex[i] = len - fieldValIndex.length + i;
		}
		request.setAttribute("props", fieldZhs);// 返回属性表头
		request.setAttribute("index", fieldValIndex);// 返回属性值下标
		// -------------------------------------------
	}

	// 将1转化为0001.00(4位不足补0,小数不足补0)
	public String getDoubleStr(String val, int intNum, int digits) {

		NumberFormat nf = NumberFormat.getInstance();// 用于保留numeric的小数位数
		nf.setMinimumIntegerDigits(val.length() < intNum ? 4 : val.length());// 设置整数数最大位数
		nf.setMinimumFractionDigits(digits);// 设置小数最大位数
		val = nf.format(new Double(val)).replaceAll(",", "");
		return val;
	}

	// 获得模拟id
	public String getNextMnId(String lastId) {

		final String mnStr = "MN";
		final String nowDateStr = new GlobalsTool().getDate().replaceAll("-",
				"").substring(2);
		String mnId = null;
		boolean lastMnIdOk = false;
		String dateStr = null;
		if (lastId == null) {
			String sql = "select top 1 relationId from tblproductmrp order by createDate desc";
			rs = new MrpBPMgt().querySql(sql);

			if (((List) rs.getRetVal()).size() > 0) {
				String[] record = (String[]) ((List) rs.getRetVal()).get(0);
				if (record.length > 0) {
					lastId = record[0];
					dateStr = "1";
				}
			}
		}
		if (dateStr != null) {
			dateStr = lastId.substring(2, 8);
		}

		if (nowDateStr.equals(dateStr)) {
			String num = lastId.substring(8);
			if (num.matches("[0-9]*")) {
				lastMnIdOk = true;
				num = Integer.parseInt(num) + 1 + "";
				num = getDoubleStr(num, 4, 0);
				mnId = mnStr + nowDateStr + num;
			}
		}
		if (!lastMnIdOk) {
			mnId = mnStr + nowDateStr + "0001";
		}
		return mnId;
	}

	/**
	 * 模拟订单
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void simulateOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int pageSize=request.getParameter("pageSize")==null?GlobalsTool.getPageSize():Integer.parseInt(request.getParameter("pageSize"));
		int pageNo=request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo"));
		MOperation  mop = (MOperation)this.getLoginBean(request).getOperationMap().get("/MrpBP.do");
		ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
		rs = getGoods(this.getLoginBean(request).getId(),scopeRight,"","","",pageSize,pageNo);
		String lastMnId = request.getParameter("mnId");
		lastMnId = lastMnId.equals("") ? null : lastMnId;
		request.setAttribute("mnId", getNextMnId(lastMnId));// 模拟id
		request.setAttribute("MOID", mop.moduleId);
		forward = mapping.findForward("simulateOrder");
	}

	/**
	 * 保存生产任务订单
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void saveProduceOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//数据收集
		forward = getForward(request, mapping, "alert");
		String billNo = request.getParameter("BillNo");
		String billDate = request.getParameter("BillDate");

		String[] chooses=request.getParameterValues("choose");
		String[] goodsCodes = request.getParameterValues("goodsCode");
		String[] MaterielAttribute = request.getParameterValues("MaterielAttribute");
		String[] goodsNumbers = request.getParameterValues("goodsNumber");
		String[] startDate= request.getParameterValues("startDate");
		String[] submitDate= request.getParameterValues("submitDate");
		String[] qtys = request.getParameterValues("qty");
		String[] trackNos=request.getParameterValues("trackNo");
		String[] bomdetIds=request.getParameterValues("bomdetId");
		String[] isProduces=request.getParameterValues("isProduce");
		String[] mainDetIds=request.getParameterValues("mainDetId");
		String[] isPlaces=request.getParameterValues("isPlace");
		String ProductingQty="0";
		boolean isMA=Boolean.parseBoolean(BaseEnv.systemSet.get("MaterielAttribute").getSetting());
		String mrpFrom=request.getParameter("mrpFrom");
		
		
		/***************************准备要插入到生产任务单的数据**************************/
		Hashtable map = (Hashtable) request.getSession().getServletContext().
        getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,
                 "tblProduce");
		UserFunctionMgt usermgt=new UserFunctionMgt();
		boolean flag=usermgt.isPeriodPre(billDate, tableInfo, this.getLoginBean(request));
		if(flag){
			EchoMessage em = EchoMessage.error().add(getMessage(request,"com.currentaccbefbill"));
			em.setPopWin(true);
			em.setAlertRequest(request);
			return;
		}
	
		/***********查询审核流信息************************/
		String tableName = "tblProduce";
		LoginBean lg;
		lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		String workFlowNode = "";
		String workFlowNodeName = "";
		String checkPersons="";
		OAWorkFlowTemplate workFlow=BaseEnv.workFlowInfo.get("tblProduce");
		try {
			if (workFlow.getTemplateStatus() == 1) {
				workFlowNodeName = "notApprove";
				workFlowNode = "0";
				checkPersons=";"+lg.getId()+";";
			}else{
				workFlowNodeName = "finish" ;
				workFlowNode = "-1";
				checkPersons=new DynDBManager().getRetCheckPer(BaseEnv.workFlowInfo.get(workFlow.getId() ),lg.getDepartCode());
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//将多个相同的成品或者半成品整合成一个
		ArrayList billMap=new ArrayList();
		String trackNoC="";
		for(int i=0;i<chooses.length;i++){
			String qty=qtys[i];	
			if(chooses[i].equals("false")||isProduces[i].equals("false"))continue;
			
			trackNoC="".equals(trackNos[i].toString())?trackNoC:trackNos[i].toString();
			
			HashMap detMap=new HashMap();
			String keyId="goodsCode:"+goodsCodes[i];
			detMap.put("goodsCode", goodsCodes[i]);
			detMap.put("MaterielAttribute", MaterielAttribute[i]);
			
			
			ArrayList list=this.getPropInfo(bomdetIds[i], (goodsNumbers[i].indexOf("~")>=0?false:true),Boolean.parseBoolean(isPlaces[i]));
			if(list.size()>0){
				HashMap propMap=(HashMap)list.get(0);
				ArrayList attList=BaseEnv.attList;
				for(int j=0;j<attList.size();j++){
					GoodsAttributeBean propBean=(GoodsAttributeBean)attList.get(j);
					if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						keyId+=propBean.getPropName()+":"+propMap.get(propBean.getPropName());	
						detMap.put(propBean.getPropName(), propMap.get(propBean.getPropName()));
					}
				}		
			}
			
			boolean exis=false;
			for(int j=0;j<billMap.size();j++){
				HashMap goodsMap=(HashMap)billMap.get(j);
				if(goodsMap.get(keyId)!=null){
					detMap.put(keyId, keyId);
					String tqty=goodsMap.get("qty").toString();
					detMap.put("qty", String.valueOf(Double.parseDouble(qty)+Double.parseDouble(tqty)));
					detMap.put("bomdetId", bomdetIds[i]);
					detMap.put("isFirst", goodsNumbers[i].indexOf("~")>=0?false:true);
					String trackNo=goodsMap.get("trackNo").toString();
					if(trackNos[i]!=null&&trackNo.indexOf(trackNos[i])<0){
						trackNo=trackNo+","+trackNos[i];
					}
					detMap.put("trackNo",trackNo);
					billMap.remove(j);
					exis=true;
				}
			}
			
			if(!exis){
				detMap.put(keyId, keyId);
				detMap.put("qty", qty);
				detMap.put("bomdetId",bomdetIds[i]);
				detMap.put("isFirst",goodsNumbers[i].indexOf("~")>=0?false:true);
				detMap.put("trackNo",trackNoC);
				detMap.put("startDate", startDate[i]);
				detMap.put("submitDate", submitDate[i]);
			}
			billMap.add(detMap);
		}
		//查找商品表的兄弟表映射到生产任务单兄弟表的关系（2012.8.11东莞新达代理商需求）
		Result relationMap=mgt.queryGoodsReProduce();
		 
		MrpBPMgt mrpmgt=new MrpBPMgt();
		boolean isFirst=true;
		/**************************************需要生产的任务单主表信息保存在billMap中，循环插入**************************************************/
		for(int i=0;i<billMap.size();i++){
			HashMap detMap=(HashMap)billMap.get(i);
			HashMap values=new HashMap();
			String goodsCode=detMap.get("goodsCode").toString();
			String matt=detMap.get("MaterielAttribute").toString();
			String trackNo=detMap.get("trackNo").toString();
			String mainId=IDGenerater.getId();
			String bomdetId =detMap.get("bomdetId").toString();
			String qty = detMap.get("qty").toString();
			if(Boolean.parseBoolean(detMap.get("isFirst").toString())){
				ProductingQty=qty;
			}
			ArrayList goodsInfos = getGoodsSon(bomdetId,Boolean.parseBoolean(detMap.get("isFirst").toString()),qty);
			
			double amts=0;
			ArrayList childList = new ArrayList();
         	values.put("TABLENAME_tblProduceDet", childList);
			for(int j=0;j<goodsInfos.size();j++){
				//确定用户是否在页面选择了当前物料的替换料,并查询所选择的替换料的所有信息
				HashMap goodsInfo=(HashMap)goodsInfos.get(j);
				
				String bomDetId=goodsInfo.get("bomDetId").toString();
				HashMap idqtyMap=new HashMap();
				String cond="";
				for(int k=0;k<chooses.length;k++){
					if(chooses[k].equals("false"))continue;
					if(mainDetIds[k].equals(bomDetId)){
						idqtyMap.put(bomdetIds[k],qtys[k]);
						cond+="'"+bomdetIds[k]+"',";
					}
				}
				ArrayList replaceList=new ArrayList();
				if(cond.length()>0&&!cond.equals("'"+bomDetId+"',")){//假如勾选了除主物料之外的其他替换料,那么查询这些替换料的详细信息
					replaceList=this.getRePlaceInfo(cond.substring(0,cond.length()-1));
					//假如这些ID中包含了主物料，那么将主物料也加载到列表中
					if(cond.contains("'"+bomDetId+"',")){
						goodsInfo.put("isPlace", "true");
						replaceList.add(goodsInfo);
					}					
				}else{//如果没有替换料或者替换料是它本身，那么直接使用之前查询出的物料
					replaceList.add(goodsInfo);
				}
				
				for(int l=0;l<replaceList.size();l++){
					goodsInfo=(HashMap)replaceList.get(l);
					HashMap childValue=new HashMap();
	         		childList.add(childValue);
	         		BigDecimal needQty=null;
	         		if(goodsInfo.get("isPlace").equals("true")){//假如是替换料，数量就是界面用户输入的值
	         			needQty=new BigDecimal(idqtyMap.get(goodsInfo.get("bomDetId")).toString());
	         		}else{//通过物料的用料比例得到需求量
	         			needQty=new BigDecimal(qty.replaceAll(",", "")).multiply(new BigDecimal(goodsInfo.get("qty").toString()));
	         		}
					BigDecimal amt=needQty.multiply(new BigDecimal(goodsInfo.get("price").toString()));
					amts+=amt.doubleValue();
	         	
	         		String sql2="select  BaseUnit from tblGoods where classCode='"+goodsInfo.get("goodsCode")+"'";
	    			Result rs2=new MrpBPMgt().querySql(sql2);
	    			String unit=rs2.getRetVal()==null?"":((String[])((ArrayList)rs2.getRetVal()).get(0))[0];
	    			unit=unit==null?"":unit;
	    			
	         		childValue.put("id", IDGenerater.getId());
	         		childValue.put("f_ref", mainId);
	         		childValue.put("GoodsCode", goodsInfo.get("goodsCode"));
	         		childValue.put("Qty",GlobalsTool.round(needQty.doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
	         		childValue.put("TaxPrice", goodsInfo.get("price"));
	         		childValue.put("TaxAmount",GlobalsTool.round(amt.doubleValue(), GlobalsTool.getDigitsOrSysSetting("tblProduceDet", "TaxAmount")));
	         		childValue.put("Price", goodsInfo.get("price"));
	         		childValue.put("Amount", GlobalsTool.round(amt.doubleValue(), GlobalsTool.getDigitsOrSysSetting("tblProduceDet", "Amount")));
	         		childValue.put("UnitQty",GlobalsTool.round(needQty.doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
	         		childValue.put("NotInQty",GlobalsTool.round(needQty.doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
	         		childValue.put("Unit", unit);
	         		childValue.put("ConversionRate", "1");
	         		childValue.put("UnitPruQty",GlobalsTool.round(Double.parseDouble(goodsInfo.get("qty").toString()),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
	         		childValue.put("SCompanyID", lg.getSunCmpClassCode());
	         		childValue.put("MaterielType", goodsInfo.get("MaterielType"));
	         		
	         		ArrayList list=BaseEnv.attList;
	         		for(int k=0;k<list.size();k++){
	        			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(k);
	        			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
	         				childValue.put(propBean.getPropName(),goodsInfo.get(propBean.getPropName()));
	        			}
	         		}
				}
			}
			 
			String sql2="select  BaseUnit from tblGoods where classCode='"+goodsCode+"'";
			Result rs2=new MrpBPMgt().querySql(sql2);
			String unit=rs2.getRetVal()==null?"":((String[])((ArrayList)rs2.getRetVal()).get(0))[0];
			unit=unit==null?"":unit;
			String OCompanyCode="";
			String OBillNo="";
			String ORemark="";
			String OAttachment="";
			String OPicture="";
			String ORefBillNo="";
			String tn="";
			String detId="";
			if("2".equals(mrpFrom)){
				tn="tblSalesOrder";
				sql2="select distinct a.CompanyCode,a.id,a.Remark,a.Attachment,a.Picture,'',b.id as detId from tblSalesOrder a,tblSalesOrderDet b where a.id=b.f_ref and b.TrackNo in ('"+(trackNo.replaceAll(",", "','") )+"')";
			}else if("1".equals(mrpFrom)){
				sql2="select distinct a.CompanyCode,a.id,a.Remark,'','',a.BillNo,b.id as detId from tblPlan a,tblPlanDet b where a.id=b.f_ref and b.TrackNo in ('"+(trackNo.replaceAll(",", "','") )+"')";
				tn="tblPlan";
			}
			if(tn.length()>0){
				rs2=new MrpBPMgt().querySql(sql2);
				ArrayList orderList=(ArrayList)rs2.getRetVal();
			
				if(orderList.size()>0){//如果通过合并商品，往来单位等会有多条；只有上述信息唯一时，将这些值设置到任务单中
					String[] orders=(String[])orderList.get(0);
					OCompanyCode=orders[0];
					ORefBillNo=orders[5];
					if(orderList.size()==1){
						OBillNo=orders[1];
						ORemark=orders[2];
						OAttachment=orders[3];
						if(OAttachment!=null&&OAttachment.length()!=0){
							String[] Attachments=OAttachment.split(";");
							for(int k=0;k<Attachments.length;k++){
								FileHandler.copy("tblProduce", FileHandler.TYPE_AFFIX, BaseEnv.FILESERVERPATH+"/affix/"+tn+"/"+Attachments[0], Attachments[0]);
							}
						}
						OPicture=orders[4];
						detId=orders[6];
						if(OPicture!=null&&OPicture.length()!=0){
							String[] OPictures=OPicture.split(";");
							for(int k=0;k<OPictures.length;k++){
								FileHandler.copy("tblProduce", FileHandler.TYPE_PIC, BaseEnv.FILESERVERPATH+"/pic/"+tn+"/"+OPictures[0], OPictures[0]);
							}
						}
					}
				}
			}
			values.put("id", mainId);
			values.put("RefBillNo", ORefBillNo);
			values.put("Attachment", OAttachment);
			values.put("Remark", ORemark);
			values.put("CompanyCode", OCompanyCode);
			if("2".equals(mrpFrom)){
				values.put("SalesOrderID", OBillNo);
			}else if("1".equals(mrpFrom)){
				values.put("PlanID", detId);
			}
			values.put("Picture", OPicture);
			values.put("StartDate", detMap.get("startDate"));
			values.put("FinishDate", detMap.get("submitDate"));
			values.put("BillDate", billDate);
			values.put("DepartmentCode", lg.getDepartCode());
			values.put("EmployeeID", lg.getId());
			values.put("workFlowNode", workFlowNode);
			values.put("workFlowNodeName", workFlowNodeName);
			values.put("GoodsCode", goodsCode);
			values.put("BillAmt", GlobalsTool.round(amts, GlobalsTool.getDigitsOrSysSetting("tblProduce", "BillAmt")));
			values.put("Qty",GlobalsTool.round(Double.parseDouble(qty),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
			values.put("createBy", lg.getId());
			values.put("lastUpdateBy",lg.getId());
			values.put("createTime",BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			values.put("lastUpdateTime",BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			values.put("statusId", "0");
			values.put("SCompanyID", lg.getSunCmpClassCode());
			values.put("Unit", unit);
			values.put("TrackNo", trackNo); 
			values.put("billFrom", 2);
			values.put("checkPersons", checkPersons);
			values.put("Period",billDate.substring(billDate.indexOf("-")+1,billDate.lastIndexOf("-")));
    		values.put("PeriodYear",billDate.substring(0,billDate.indexOf("-")));
    		values.put("PeriodMonth",billDate.substring(billDate.indexOf("-")+1,billDate.lastIndexOf("-")));
    		if(isMA&&"WW".equals(matt)){
    			values.put("EntrustPro", "Outside");
    		}else{
    			values.put("EntrustPro", "Inside");
    		}
    		
			ArrayList list=BaseEnv.attList;
			for(int j=0;j<list.size();j++){
				GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(j);
				if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						values.put(propBean.getPropName(), detMap.get(propBean.getPropName()));
				}
			}
			
			rs=this.insertTable(values, tableName, request);
         	if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
         		//自定义sql语句定制返回结果
         		SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
				}
         		return;
         	}else{//插入主表数据成功后，根据商品表兄弟表与任务单兄弟表的映射，将商品表信息插入到任务单表中
         		if(relationMap.retCode==ErrorCanst.DEFAULT_SUCCESS&&relationMap.retVal!=null){
         			mgt.execGoodsReProduce((HashMap)relationMap.retVal, goodsCode, mainId);
         		}
         	}
         	isFirst=false;
		}
		
		try {
			/**********************更新MRP列表中的已下任务单状态********************/
			String tracks="";
			for(int i=0;i<trackNos.length;i++){
				if(trackNos[i].length()>0&&!tracks.contains("'"+trackNos[i].replaceAll(",", "','")+"',")){
					tracks+="'"+trackNos[i].replaceAll(",", "','")+"',";
				}
			}
			if(tracks.length()>0){
				tracks=tracks.substring(0,tracks.length()-1);
				String sql="update tblProductMRP set hasProduce=1 ,StoringQty=(case when MRPFrom=2 then (select buyIngNum from tblSalesOrderQuantum where orderDetId=tblProductMRP.RelationID and salesOrderId=tblProductMRP.orderTrackNo and goodsCode=tblProductMRP.goodsCode and  bomdetId=tblProductMRP.bomdetId) else (select buyIngNum from tblSalesOrderQuantum where salesOrderId=tblProductMRP.orderTrackNo "+
				"and goodsCode=tblProductMRP.goodsCode and  bomdetId=tblProductMRP.bomdetId) end),"+
				"ProductingQty=(case when MRPFrom=2 then (select proingNum from tblSalesOrderQuantum where orderDetId=tblProductMRP.RelationID and salesOrderId=tblProductMRP.orderTrackNo and goodsCode=tblProductMRP.goodsCode and  bomdetId=tblProductMRP.bomdetId)"+
				" else (select proingNum from tblSalesOrderQuantum where salesOrderId=tblProductMRP.orderTrackNo and goodsCode=tblProductMRP.goodsCode and  bomdetId=tblProductMRP.bomdetId) end) where orderTrackNo in ("+tracks+")";
				mrpmgt.execSql(sql);
			}
			if("true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())){
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do?method=bomDemand&opeType=BackMrp&ProductMRPIds="+request.getParameter("ProductMRPIds")+"&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do?method=bomDemand&opeType=BackMrp&mrpFrom="+mrpFrom+"&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(rs.getRetCode(),request).setAlertRequest(request);
		}
	}
	/**
	 * 保存生产任务订单
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void saveProduceOrderGM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//数据收集
		forward = getForward(request, mapping, "alert");
		String billNo = request.getParameter("BillNo");
		String billDate = request.getParameter("BillDate");

		String[] chooses=request.getParameterValues("choose");
		String[] goodsCodes = request.getParameterValues("goodsCode");
		String[] goodPropHashs = request.getParameterValues("goodPropHash");
		String[] MaterielAttribute = request.getParameterValues("MaterielAttribute");
		String[] goodsNumbers = request.getParameterValues("goodsNumber");
		String[] startDate= request.getParameterValues("startDate");
		String[] submitDate= request.getParameterValues("submitDate");
		String[] qtys = request.getParameterValues("qty");
		String[] trackNos=request.getParameterValues("trackNo");
		String[] bomdetIds=request.getParameterValues("bomdetId");
		String[] isProduces=request.getParameterValues("isProduce");
		String[] isPlaces=request.getParameterValues("isPlace");
		String ProductingQty="0";
		boolean isMA=Boolean.parseBoolean(BaseEnv.systemSet.get("MaterielAttribute").getSetting());
		
		
		/***************************准备要插入到生产任务单的数据**************************/
		Hashtable map = (Hashtable) request.getSession().getServletContext().
        getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,
                 "tblProduce");
		UserFunctionMgt usermgt=new UserFunctionMgt();
		boolean flag=usermgt.isPeriodPre(billDate, tableInfo, this.getLoginBean(request));
		if(flag){
			EchoMessage em = EchoMessage.error().add(getMessage(request,"com.currentaccbefbill"));
			em.setPopWin(true);
			em.setAlertRequest(request);
			return;
		}
	
		/***********查询审核流信息************************/
		String tableName = "tblProduce";
		LoginBean lg;
		lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		String workFlowNode = "";
		String workFlowNodeName = "";
		String checkPersons="";
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get("tblProduce");
		try {
			if (workFlow.getTemplateStatus() == 1) {
				workFlowNodeName = "notApprove";
				workFlowNode = "0";
				checkPersons=";"+lg.getId()+";";
			}else{
				workFlowNodeName = "finish" ;
				workFlowNode = "-1";
				checkPersons=new DynDBManager().getRetCheckPer(BaseEnv.workFlowInfo.get(workFlow.getId() ),lg.getDepartCode());
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/*********************************************将多个相同的成品或者半成品整合成一个*************************************/
		ArrayList billMap=new ArrayList();
		String trackNoC="";
		for(int i=0;i<chooses.length;i++){
			String qty=qtys[i];	
			if(chooses[i].equals("false")||isProduces[i].equals("false"))continue;
			
			trackNoC="".equals(trackNos[i].toString())?trackNoC:trackNos[i].toString();
			
			HashMap detMap=new HashMap();
			String keyId="goodsCode:"+goodsCodes[i];
			detMap.put("goodsCode", goodsCodes[i]);
			detMap.put("goodPropHash", goodPropHashs[i]);
			detMap.put("MaterielAttribute", MaterielAttribute[i]);
			
			
			ArrayList list=this.getPropInfo(bomdetIds[i], (goodsNumbers[i].indexOf("~")>=0?false:true),Boolean.parseBoolean(isPlaces[i]));
			if(list.size()>0){
				HashMap propMap=(HashMap)list.get(0);
				ArrayList attList=BaseEnv.attList;
				for(int j=0;j<attList.size();j++){
					GoodsAttributeBean propBean=(GoodsAttributeBean)attList.get(j);
					if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						keyId+=propBean.getPropName()+":"+propMap.get(propBean.getPropName());	
						detMap.put(propBean.getPropName(), propMap.get(propBean.getPropName()));
					}
				}		
			}
			
			boolean exis=false;
			for(int j=0;j<billMap.size();j++){
				HashMap goodsMap=(HashMap)billMap.get(j);
				if(goodsMap.get(keyId)!=null){
					detMap.put(keyId, keyId);
					String tqty=goodsMap.get("qty").toString();
					detMap.put("qty", String.valueOf(Double.parseDouble(qty)+Double.parseDouble(tqty)));
					detMap.put("bomdetId", bomdetIds[i]);
					detMap.put("isFirst", goodsNumbers[i].indexOf("~")>=0?false:true);
					String trackNo=goodsMap.get("trackNo").toString();
					if(trackNos[i]!=null&&trackNo.indexOf(trackNos[i])<0){
						trackNo=trackNo+","+trackNos[i];
					}
					detMap.put("trackNo",trackNo);
					billMap.remove(j);
					exis=true;
				}
			}
			
			if(!exis){
				detMap.put(keyId, keyId);
				detMap.put("qty", qty);
				detMap.put("bomdetId",bomdetIds[i]);
				detMap.put("isFirst",goodsNumbers[i].indexOf("~")>=0?false:true);
				detMap.put("trackNo",trackNoC);
				detMap.put("startDate", startDate[i]);
				detMap.put("submitDate", submitDate[i]);
			}
			billMap.add(detMap);
		}
		/***********************************************以上代码：将多个相同的成品或者半成品整合成一个*************************************/
		//查找商品表的兄弟表映射到生产任务单兄弟表的关系（2012.8.11东莞新达代理商需求）
		Result relationMap=mgt.queryGoodsReProduce();
		HashMap hashChildRep=(HashMap)request.getSession().getAttribute("hashChildRep"); 
		MrpBPMgt mrpmgt=new MrpBPMgt();
		boolean isFirst=true;
		/**************************************需要生产的任务单主表信息保存在billMap中，循环插入**************************************************/
		for(int i=0;i<billMap.size();i++){
			HashMap detMap=(HashMap)billMap.get(i);
			HashMap values=new HashMap();
			String goodsCode=detMap.get("goodsCode").toString();
			String goodPropHash=detMap.get("goodPropHash").toString();
			String matt=detMap.get("MaterielAttribute").toString();
			String trackNo=detMap.get("trackNo").toString();
			String mainId=IDGenerater.getId();
			String bomdetId =detMap.get("bomdetId").toString();
			String qty = detMap.get("qty").toString();
			if(Boolean.parseBoolean(detMap.get("isFirst").toString())){
				ProductingQty=qty;
			}
			ArrayList goodsInfos = getGoodsSon(bomdetId,Boolean.parseBoolean(detMap.get("isFirst").toString()),qty);
			
			double amts=0;
			HashMap replaceMap=hashChildRep.get(goodPropHash)==null?null:(HashMap)hashChildRep.get(goodPropHash);
			ArrayList mateHashList=new ArrayList();//保存hash值的列表
			ArrayList childList = new ArrayList();
         	values.put("TABLENAME_tblProduceDet", childList);
			for(int j=0;j<goodsInfos.size();j++){
				//确定用户是否在页面选择了当前物料的替换料,并查询所选择的替换料的所有信息
				HashMap goodsInfo=(HashMap)goodsInfos.get(j);
				String bomDetId=goodsInfo.get("bomDetId").toString();
				ArrayList materList=new ArrayList();
				
				if("exist".equals(goodsInfo.get("replace"))){
					ArrayList replaceList=(ArrayList)replaceMap.get(bomDetId);
					BaseEnv.log.debug("生产任务单中物料："+bomDetId);
					for(int k=0;k<replaceList.size();k++){						
						HashMap repMap=(HashMap)replaceList.get(k);
						BaseEnv.log.debug(repMap.get("goodsCode")+"  "+repMap.get("count"));
						String mateHash=repMap.get("goodPropHash").toString();
						if(!mateHashList.contains(mateHash)){
							repMap.put("qty", 0);
							repMap.put("needQty", repMap.get("count"));
							materList.add(repMap);
							mateHashList.add(mateHash);
						}
					}
				}else{
					String mateHash=goodsInfo.get("goodPropHash").toString();
					if(!mateHashList.contains(mateHash)){
						materList.add(goodsInfo);
						mateHashList.add(mateHash);
					}
				}
				
				for(int l=0;l<materList.size();l++){
					goodsInfo=(HashMap)materList.get(l);
					HashMap childValue=new HashMap();
	         		childList.add(childValue);
	         		BigDecimal needQty=new BigDecimal(goodsInfo.get("needQty").toString());
					BigDecimal amt=needQty.multiply(new BigDecimal(goodsInfo.get("price").toString()));
					amts+=amt.doubleValue();
	         	
	         		String sql2="select  BaseUnit from tblGoods where classCode='"+goodsInfo.get("goodsCode")+"'";
	    			Result rs2=new MrpBPMgt().querySql(sql2);
	    			String unit=rs2.getRetVal()==null?"":((String[])((ArrayList)rs2.getRetVal()).get(0))[0];
	    			unit=unit==null?"":unit;
	    			
	         		childValue.put("id", IDGenerater.getId());
	         		childValue.put("f_ref", mainId);
	         		childValue.put("GoodsCode", goodsInfo.get("goodsCode"));
	         		childValue.put("Qty",GlobalsTool.round(needQty.doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
	         		childValue.put("TaxPrice", goodsInfo.get("price"));
	         		childValue.put("TaxAmount",GlobalsTool.round(amt.doubleValue(), GlobalsTool.getDigitsOrSysSetting("tblProduceDet", "TaxAmount")));
	         		childValue.put("Price", goodsInfo.get("price"));
	         		childValue.put("Amount", GlobalsTool.round(amt.doubleValue(), GlobalsTool.getDigitsOrSysSetting("tblProduceDet", "Amount")));
	         		childValue.put("UnitQty",GlobalsTool.round(needQty.doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
	         		childValue.put("NotInQty",GlobalsTool.round(needQty.doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
	         		childValue.put("Unit", unit);
	         		childValue.put("ConversionRate", "1");
	         		childValue.put("UnitPruQty",GlobalsTool.round(Double.parseDouble(goodsInfo.get("qty").toString()),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
	         		childValue.put("SCompanyID", lg.getSunCmpClassCode());
	         		childValue.put("MaterielType", goodsInfo.get("MaterielType"));
	         		
	         		ArrayList list=BaseEnv.attList;
	         		for(int k=0;k<list.size();k++){
	        			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(k);
	        			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
	         				childValue.put(propBean.getPropName(),goodsInfo.get(propBean.getPropName()));
	        			}
	         		}
				}
			}
			 
			String sql2="select  BaseUnit from tblGoods where classCode='"+goodsCode+"'";
			Result rs2=new MrpBPMgt().querySql(sql2);
			String unit=rs2.getRetVal()==null?"":((String[])((ArrayList)rs2.getRetVal()).get(0))[0];
			unit=unit==null?"":unit;
			String OCompanyCode="";
			String OBillNo="";
			String ORemark="";
			String OAttachment="";
			String OPicture="";
			String ORefBillNo="";
			String tn="";
			String detId="";
			String mrpFrom="";
			
			sql2="select distinct a.CompanyCode,a.id,a.Remark,a.Attachment,a.Picture,'','' as detId,'2' as mrpFrom from tblSalesOrder a,tblSalesOrderDet b where a.id=b.f_ref and b.TrackNo in ('"+(trackNo.replaceAll(",", "','") )+"')";
			sql2+=" union all ";
			sql2+="select distinct a.CompanyCode,'' as id,a.Remark,'','',a.BillNo,b.id as detId,'1' as mrpFrom from tblPlan a,tblPlanDet b where a.id=b.f_ref and b.TrackNo in ('"+(trackNo.replaceAll(",", "','") )+"')";

		
			if(sql2.length()>0){
				rs2=new MrpBPMgt().querySql(sql2);
				ArrayList orderList=(ArrayList)rs2.getRetVal();
			
				for(int j=0;j<orderList.size();j++){//如果通过合并商品，往来单位等会有多条；只有上述信息唯一时，将这些值设置到任务单中
					String[] orders=(String[])orderList.get(j);
					OCompanyCode=orders[0];
					mrpFrom=orders[7];
					ORefBillNo=orders[5];
					
					if("2".equals(mrpFrom)){
						OBillNo=orders[1];
					}
					ORemark=orders[2];
					OAttachment=orders[3];
					if(OAttachment!=null&&OAttachment.length()!=0){
						String[] Attachments=OAttachment.split(";");
						for(int k=0;k<Attachments.length;k++){
							FileHandler.copy("tblProduce", FileHandler.TYPE_AFFIX, BaseEnv.FILESERVERPATH+"/affix/"+tn+"/"+Attachments[0], Attachments[0]);
						}
					}
					OPicture=orders[4];
					if("1".equals(mrpFrom)){
						detId=orders[6];
					}
					if(OPicture!=null&&OPicture.length()!=0){
						String[] OPictures=OPicture.split(";");
						for(int k=0;k<OPictures.length;k++){
							FileHandler.copy("tblProduce", FileHandler.TYPE_PIC, BaseEnv.FILESERVERPATH+"/pic/"+tn+"/"+OPictures[0], OPictures[0]);
						}
					}
					
				}
			}
			values.put("id", mainId);
			values.put("RefBillNo", ORefBillNo);
			values.put("Attachment", OAttachment);
			values.put("Remark", ORemark);
			values.put("CompanyCode", OCompanyCode);
			values.put("SalesOrderID", OBillNo);
			values.put("PlanID", detId);
			values.put("Picture", OPicture);
			values.put("StartDate", detMap.get("startDate"));
			values.put("FinishDate", detMap.get("submitDate"));
			values.put("BillDate", billDate);
			values.put("DepartmentCode", lg.getDepartCode());
			values.put("EmployeeID", lg.getId());
			values.put("workFlowNode", workFlowNode);
			values.put("workFlowNodeName", workFlowNodeName);
			values.put("GoodsCode", goodsCode);
			values.put("BillAmt", GlobalsTool.round(amts, GlobalsTool.getDigitsOrSysSetting("tblProduce", "BillAmt")));
			values.put("Qty",GlobalsTool.round(Double.parseDouble(qty),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
			values.put("createBy", lg.getId());
			values.put("lastUpdateBy",lg.getId());
			values.put("createTime",BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			values.put("lastUpdateTime",BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			values.put("statusId", "0");
			values.put("SCompanyID", lg.getSunCmpClassCode());
			values.put("Unit", unit);
			values.put("TrackNo", ""); 
			values.put("billFrom", 2);
			values.put("checkPersons", checkPersons);
			values.put("Period",billDate.substring(billDate.indexOf("-")+1,billDate.lastIndexOf("-")));
    		values.put("PeriodYear",billDate.substring(0,billDate.indexOf("-")));
    		values.put("PeriodMonth",billDate.substring(billDate.indexOf("-")+1,billDate.lastIndexOf("-")));
    		if(isMA&&"WW".equals(matt)){
    			values.put("EntrustPro", "Outside");
    		}else{
    			values.put("EntrustPro", "Inside");
    		}
    		
			ArrayList list=BaseEnv.attList;
			for(int j=0;j<list.size();j++){
				GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(j);
				if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						values.put(propBean.getPropName(), detMap.get(propBean.getPropName()));
				}
			}
			
			rs=this.insertTable(values, tableName, request);
         	if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
         		//自定义sql语句定制返回结果
         		SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
					EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
				} else {
					EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
				}
         		return;
         	}else{//插入主表数据成功后，根据商品表兄弟表与任务单兄弟表的映射，将商品表信息插入到任务单表中
         		if(relationMap.retCode==ErrorCanst.DEFAULT_SUCCESS&&relationMap.retVal!=null){
         			mgt.execGoodsReProduce((HashMap)relationMap.retVal, goodsCode, mainId);
         		}
         	}
         	isFirst=false;
		}
		
		try {
			/**********************更新MRP列表中的已下任务单状态********************/
			String tracks="";
			for(int i=0;i<trackNos.length;i++){
				if(chooses[i].equals("false")||isProduces[i].equals("false"))continue;
				String[] trackArray=trackNos[i].split(",");
				for(int k=0;k<trackArray.length;k++){
					if(trackArray[k].length()>0&&!tracks.contains("'"+trackArray[k].replaceAll(",", "','")+"',")){
						tracks+="'"+trackArray[k].replaceAll(",", "','")+"',";
					}
				}
				
			}
			if(tracks.length()>0){
				tracks=tracks.substring(0,tracks.length()-1);
				String sql="update tblProductMRP set hasProduce=1 where orderTrackNo in ("+tracks+")";
				mrpmgt.execSql(sql);
			}
			
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do?method=bomDemand&opeType=BackMrp&mrpFrom=&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(rs.getRetCode(),request).setAlertRequest(request);
		}
	}
	private void saveQuoteBuy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 数据收集
		forward = getForward(request, mapping, "alert");
		String billNo = request.getParameter("BillNo");
		String billDate = request.getParameter("BillDate");
		String mrpFrom=request.getParameter("mrpFrom");
		
		String[] chooses=request.getParameterValues("choose");
		String[] goodsCodes = request.getParameterValues("goodsCode");;
		String[] qtys = request.getParameterValues("qty");
		String[] trackNos=request.getParameterValues("trackNo");
		String[] buyPersons=request.getParameterValues("buyPerson");
		String[] submitDate=request.getParameterValues("submitDate");
		
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,"tblBuyApplication");
		UserFunctionMgt usermgt=new UserFunctionMgt();
		boolean flag=usermgt.isPeriodPre(billDate, tableInfo, this.getLoginBean(request));
		if(flag || billDate==null || billDate.length()==0){
			EchoMessage em = EchoMessage.error().add(getMessage(request,(billDate==null || billDate.length()==0)?"mrp.orderDateNull":"com.currentaccbefbill"));
			em.setPopWin(true);
			em.setAlertRequest(request);
			return;
		}

		
		//统计物料明细的条数，如果超过500条，则每500条明细商品创建一个申请单
		int goodsCount=0;
		for(int i=0;i<chooses.length;i++){
			if(chooses[i].equals("false"))continue;
			goodsCount++;			
		}
		/**********************查询采购申请单是否启用审核流***********************************/
		OAWorkFlowTemplate tbleWokeFlow = null;
		String tableName = "tblBuyApplication";
		LoginBean lg;
		lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		String createby = lg.getId();
		String sunClassCode = lg.getSunCmpClassCode(); // 从LoginBean中取出分支机构的classCode
		String workFlowNode = "";
		String workFlowNodeName = "";
		String checkPersons="";
		OAWorkFlowTemplate workFlow=BaseEnv.workFlowInfo.get("tblBuyApplication");
		try {
			if (workFlow.getTemplateStatus() == 1&&goodsCount<=500) {
				workFlowNodeName = "notApprove";
				workFlowNode = "0";
				checkPersons=";"+lg.getId()+";";
			}else{
				workFlowNodeName = "finish" ;
				workFlowNode = "-1";
				checkPersons=new DynDBManager().getRetCheckPer(BaseEnv.workFlowInfo.get(workFlow.getId() ),lg.getDepartCode());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/**********************以上代码：查询采购申请单是否启用审核流****************************/
		String tracks="";
		String trackNo="";
		for(int i=0;i<trackNos.length;i++){
			if(chooses[i].equals("false"))continue;
			
			String [] trackNoArray=trackNos[i].split(",");
			for(int k=0;k<trackNoArray.length;k++){
				if(trackNoArray[k].length()>0&&!tracks.contains("'"+trackNoArray[k].replaceAll(",", "','")+"',")){
					tracks+="'"+trackNoArray[k].replaceAll(",", "','")+"',";
				}
			}
			if(trackNos[i]!=null&&trackNos[i].length()>0&&trackNo.indexOf(trackNos[i]+",")<0){
				trackNo=trackNo+trackNos[i]+",";
			}
		}
		
		//新增采购申请单主表信息
		MrpBPMgt mrpmgt=new MrpBPMgt();
		Result rs=new Result();
		HashMap values=null;
		int goodsCount2=0;
		for(int i=0;i<chooses.length;i++){
			if(chooses[i].equals("false"))continue;
			
			if(goodsCount2%500==0){//如果是500的倍数，则新增一个申请单
				 values=new HashMap();
				 String id=IDGenerater.getId();
				 values.put("id", id);
				 if(goodsCount2>0){
				 }
				 values.put("BillNo",billNo);
				 values.put("BillDate", billDate);
				 if (trackNo.indexOf(",")>0)
				 {
					 trackNo = trackNo.substring(0,trackNo.indexOf(",")-1);
				 }
				 values.put("TrackNo",trackNo.length()>1? trackNo.substring(0,trackNo.length()-1):"");
				 values.put("DepartmentCode", lg.getDepartCode());
				 values.put("createBy", createby);
				 values.put("workFlowNode", workFlowNode);
				 values.put("workFlowNodeName", workFlowNodeName);
				 values.put("CurrencyRate", "1");
				 values.put("EmployeeID", createby);
				 values.put("SCompanyID", lg.getSunCmpClassCode());
				 values.put("billFrom", 2);
				 values.put("checkPersons", checkPersons);
				 values.put("Period",billDate.substring(billDate.indexOf("-")+1,billDate.lastIndexOf("-")));
				 values.put("PeriodYear",billDate.substring(0,billDate.indexOf("-")));
				 values.put("PeriodMonth",billDate.substring(billDate.indexOf("-")+1,billDate.lastIndexOf("-")));
					
				 ArrayList childList = new ArrayList();
				 values.put("TABLENAME_tblBuyApplicationDet", childList);
			}
			
         	HashMap childValue=new HashMap();
         	((ArrayList)values.get("TABLENAME_tblBuyApplicationDet")).add(childValue);
         	String sql2="select  BaseUnit,CONVERT(varchar(100),Dateadd(d,-1*(case when isnull(BuyPeriod,1)=0 then 1 else isnull(BuyPeriod,1) end)+1,'"+submitDate[i]+"'),23) as orderDate from tblGoods where classCode='"+goodsCodes[i]+"'";
    		Result rs2=new MrpBPMgt().querySql(sql2);
    		String unit=rs2.getRetVal()==null?"":((String[])((ArrayList)rs2.getRetVal()).get(0))[0];
    		String orderDate=rs2.getRetVal()==null?"":((String[])((ArrayList)rs2.getRetVal()).get(0))[1];
    		unit=unit==null?"":unit;
    			
         	childValue.put("id", IDGenerater.getId());
         	childValue.put("f_ref", values.get("id").toString());
         	childValue.put("GoodsCode",goodsCodes[i]);
         	childValue.put("Qty",GlobalsTool.round(Double.parseDouble(qtys[i]),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));         		
         	childValue.put("UnitQty",GlobalsTool.round(Double.parseDouble(qtys[i]),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
         	childValue.put("NotInQty",GlobalsTool.round(Double.parseDouble(qtys[i]),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
         	childValue.put("Unit", unit);
         	childValue.put("ConversionRate", "1");
         	childValue.put("SCompanyID", lg.getSunCmpClassCode());
         	childValue.put("EmployeeID", buyPersons[i]);
         	childValue.put("OrderDate", orderDate);
         	childValue.put("ArriveDate", submitDate[i]);
         	
         	ArrayList attList=BaseEnv.attList;
			for(int j=0;j<attList.size();j++){
				GoodsAttributeBean propBean=(GoodsAttributeBean)attList.get(j);
				if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
					childValue.put(propBean.getPropName(),request.getParameterValues(propBean.getPropName())[i]);
					if(propBean.getPropName().equals("color")){
						childValue.put("colorName",request.getParameterValues("colorName")[i]);
					}
				}
			}
         	
			if((goodsCount2+1)%500==0||(goodsCount2+1)==goodsCount){
				rs=this.insertTable(values, tableName, request);
			}
				
			goodsCount2++;	
		}
		
     	if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
     		EchoMessage.error().add(rs.getRetCode(),request).setAlertRequest(request);
     	}else{
     		/**********************更新MRP列表中的已下申请单状态********************/
			if(tracks.length()>0){
				tracks=tracks.substring(0,tracks.length()-1);
				String sql="update tblProductMRP set hasApp=1 where orderTrackNo in ("+tracks+")";
				mrpmgt.execSql(sql);
			}
     	}
     	// 插入审核流信息,如果启用审核流，自动插入此记录
     	if(!workFlowNodeName.equals("finish")){
     		String deliverTo=request.getParameter("deliverTo");
         	try{
	         	//如果用户点击了转交按钮，弹出转交界面   
         		String nextId="";
         		if(rs.retVal!=null){
         			String []str=(String[])rs.retVal;
         			if(str.length>=3){
         				nextId=str[2];
         			}
         		}
	         	if("true".equals(deliverTo)){
	         		String retUrl="/MrpBP.do?method=bomDemand&opeType=BackMrp&ProductMRPIds="+request.getParameter("ProductMRPIds");
	        		request.setAttribute("directJump", true);
	        		request.setAttribute("retValUrl", retUrl);
	        		request.setAttribute("from", "add");
	        		String message = getMessage(request, "common.msg.addSuccess") ; 
	        		EchoMessage.success().add(message)
								.setBackUrl("/OAMyWorkFlow.do?keyId="+values.get("id")+"&currNodeId=0&nextNodeIds="+(nextId.equals("noNode")?"":nextId)+"&department="+
										this.getLoginBean(request).getDepartCode()+"&designId="+workFlow.getId()+"&operation="+OperationConst.OP_AUDITING_PREPARE).setAlertRequest(request) ;
	        		return;
	         	}
         	}catch(Exception ex){
         		ex.printStackTrace();
         	}
     	}
     	
		if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
		}else{		
			if("true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())){
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do?method=bomDemand&opeType=BackMrp&ProductMRPIds="+request.getParameter("ProductMRPIds")+"&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do?method=bomDemand&opeType=BackMrp&mrpFrom="+mrpFrom+"&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			}
		}
	}

	private void saveBuyOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws  ParseException {
		// 数据收集
		forward = getForward(request, mapping, "alert");
		String billNo = request.getParameter("BillNo");
		String billDate = request.getParameter("BillDate");
		String mrpFrom=request.getParameter("mrpFrom");
		
		String[] chooses=request.getParameterValues("choose");
		String[] goodsCodes = request.getParameterValues("goodsCode");
		String[] goodsNumbers = request.getParameterValues("goodsNumber");
		String[] companyCodes = request.getParameterValues("companyCode");
		String[] qtys = request.getParameterValues("qty");
		String[] prices = request.getParameterValues("price");
		String[] stockCodes=request.getParameterValues("stockCode");
		String[] trackNos=request.getParameterValues("trackNo");
		String[] bomdetIds=request.getParameterValues("bomdetId");
		String[] isPlaces=request.getParameterValues("isPlace");
		String[] submitDate=request.getParameterValues("submitDate");
		
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
	
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,"tblBuyOrder");
		UserFunctionMgt usermgt=new UserFunctionMgt();
		boolean flag=usermgt.isPeriodPre(billDate, tableInfo, this.getLoginBean(request));
		if(flag || billDate==null || billDate.length()==0){
			EchoMessage em = EchoMessage.error().add(getMessage(request,(billDate==null || billDate.length()==0)?"mrp.orderDateNull":"com.currentaccbefbill"));
			em.setPopWin(true);
			em.setAlertRequest(request);
			return;
		}
		

		String qty = null;
		
		/**********************查询采购订单是否启用审核流***********************************/
		String tableName = "tblBuyOrder";
		LoginBean lg;
		lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		String createby = lg.getId();
		String sunClassCode = lg.getSunCmpClassCode(); // 从LoginBean中取出分支机构的classCode
		String workFlowNode = "";
		String workFlowNodeName = "";
		String checkPersons="";
		OAWorkFlowTemplate workFlow=BaseEnv.workFlowInfo.get("tblBuyOrder");
		try {
			if (workFlow.getTemplateStatus() == 1) {
				workFlowNodeName = "notApprove";
				workFlowNode = "0";
				checkPersons=";"+createby+";";
			}else{
				workFlowNodeName = "finish" ;
				workFlowNode = "-1";
				checkPersons=new DynDBManager().getRetCheckPer(BaseEnv.workFlowInfo.get(workFlow.getId() ),lg.getDepartCode());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/*******将所有选中的商品分组，分供应商保存，以便后面插入采购订单，id记录器,记录一个商品不同供应商的采购订单id**********/
		Map<String, String> idMap = new HashMap<String, String>();
		Map<String, BigDecimal> idAmtMap = new HashMap<String, BigDecimal>();
		Map<String, ArrayList>  billMap= new HashMap<String, ArrayList>();
		Map<String, StringBuffer>  idTrackNo= new HashMap<String, StringBuffer>();
		
		for(int i=0;i<chooses.length;i++){
			if(chooses[i].equals("false"))continue;
			String mainId="";
			String curCompany=companyCodes[i];//当前物料供应商
			String stockCode=stockCodes[i];//当前仓库
			ArrayList billDets;
			StringBuffer trackNo=new StringBuffer("");
			if(idMap.containsKey(curCompany+";"+stockCode)){
			   mainId=idMap.get(curCompany+";"+stockCode);
			   billDets=(ArrayList)billMap.get(mainId);
			   trackNo=idTrackNo.get(mainId) ;
			}else{
				mainId=IDGenerater.getId();
				idMap.put(curCompany+";"+stockCode, mainId);//新建一个采购订单的id				
				idAmtMap.put(mainId, new BigDecimal(0));
				billDets=new ArrayList();
				billMap.put(mainId, billDets);
				idTrackNo.put(mainId, trackNo);
			}

			if(trackNos[i]!=null&&trackNos[i].length()>0&&trackNo.indexOf(trackNos[i]+",")<0){
				trackNo.append(trackNos[i]+",");
			}
			
			qty = qtys[i];
			BigDecimal bd=mgt.getGoodsPrice(curCompany,goodsCodes[i]);
			prices[i]=bd.doubleValue()+"";
			BigDecimal amt=new BigDecimal(qty.replaceAll(",", "")).multiply(new BigDecimal(prices[i].replaceAll(",", "")));
			amt=new BigDecimal(GlobalsTool.round(amt.doubleValue(), GlobalsTool.getDigitsOrSysSetting("tblBuyOrderDet", "TaxAmount")));
			idAmtMap.put(mainId, idAmtMap.get(mainId).add(amt));
			
			//通过BOM明细ID拿到属性值
			HashMap detMap=new HashMap();
			detMap.put("goodsCode", goodsCodes[i]);
			
			String keyId="goodCode:"+goodsCodes[i];
			ArrayList list=this.getPropInfo(bomdetIds[i], (goodsNumbers[i].indexOf("~")>=0?false:true),Boolean.parseBoolean(isPlaces[i]));
			if(list.size()>0){
				HashMap propMap=(HashMap)list.get(0);
				ArrayList attList=BaseEnv.attList;
				for(int j=0;j<attList.size();j++){
					GoodsAttributeBean propBean=(GoodsAttributeBean)attList.get(j);
					if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						keyId+=propBean.getPropName()+":"+propMap.get(propBean.getPropName());	
						detMap.put(propBean.getPropName(), propMap.get(propBean.getPropName()));
						if(BaseEnv.version==3&&propBean.getPropName().equals("color")){
							detMap.put("colorName", propMap.get("colorName"));
						}
					}
				}		
			}
			
			boolean exis=false;
			for(int j=0;j<billDets.size();j++){
				HashMap goodsMap=(HashMap)billDets.get(j);
				if(goodsMap.get(keyId)!=null){
					detMap.put(keyId, keyId);
					detMap.put("qty",((BigDecimal)goodsMap.get("qty")).add(new BigDecimal(qty.replaceAll(",", ""))));
					detMap.put("price",goodsMap.get("price"));
					detMap.put("amt",((BigDecimal)goodsMap.get("amt")).add(amt));
					billDets.remove(j);
					exis=true;
				}
			}
			
			if(!exis){
				detMap.put(keyId, keyId);
				detMap.put("qty",new BigDecimal(qty.replaceAll(",", "")));
				detMap.put("price",prices[i].replaceAll(",", ""));
				detMap.put("stockCode", stockCodes[i].split(";").length>0?stockCodes[i].split(";")[0]:"");
				detMap.put("amt",amt);
				detMap.put("submitDate", submitDate[i]);
			}
			
			billDets.add(detMap);
		}		 
		//新增采购订单主表信息
		Iterator it=idMap.keySet().iterator();
		MrpBPMgt mrpmgt=new MrpBPMgt();
		Result rs=new Result();
		boolean isFirst=true;
		while(it.hasNext()){
			String curCompanyStock=it.next().toString();		
			String companyCode=curCompanyStock.split(";")[0];
			String stockCode=curCompanyStock.split(";").length==2?curCompanyStock.split(";")[1]:"";
			String id=idMap.get(curCompanyStock);
			String trackNo=idTrackNo.get(id).toString();
			trackNo=trackNo.length()>0?trackNo.substring(0,trackNo.length()-1):"";
			BigDecimal amt=idAmtMap.get(id);
			HashMap values=new HashMap();
			values.put("id", id);
			values.put("BillDate", billDate);
			values.put("TrackNo","true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())?trackNo:"");
			values.put("CompanyCode", companyCode);
			values.put("StockCode", stockCode);
			values.put("DepartmentCode", lg.getDepartCode());
			values.put("createBy", createby);
			values.put("workFlowNode", workFlowNode);
			values.put("workFlowNodeName", workFlowNodeName);
			values.put("CurrencyRate", "1");
			values.put("EmployeeID", createby);
			values.put("SCompanyID", lg.getSunCmpClassCode());
			values.put("TaxAmount",GlobalsTool.round(amt.doubleValue(), GlobalsTool.getDigitsOrSysSetting("tblBuyOrder", "TaxAmount")));
			values.put("billFrom", 2);
			values.put("checkPersons", checkPersons);
			values.put("InvoiceType", 1);
			values.put("Period",billDate.substring(billDate.indexOf("-")+1,billDate.lastIndexOf("-")));
    		values.put("PeriodYear",billDate.substring(0,billDate.indexOf("-")));
    		values.put("PeriodMonth",billDate.substring(billDate.indexOf("-")+1,billDate.lastIndexOf("-")));
			
			ArrayList childList = new ArrayList();
         	values.put("TABLENAME_tblBuyOrderDet", childList);
         	//新增明细表信息 
         	ArrayList billDets=billMap.get(id);
         	for(int i=0;i<billDets.size();i++){
         		HashMap detMap=(HashMap)billDets.get(i);
         		HashMap childValue=new HashMap();
         		childList.add(childValue);
         		String sql2="select  BaseUnit from tblGoods where classCode='"+detMap.get("goodsCode").toString()+"'";
    			Result rs2=new MrpBPMgt().querySql(sql2);
    			String unit=rs2.getRetVal()==null?"":((String[])((ArrayList)rs2.getRetVal()).get(0))[0];
    			String price=detMap.get("price") .toString().replaceAll(",", "");
    			double amttax=((BigDecimal)detMap.get("amt")).doubleValue();
    			unit=unit==null?"":unit;
    			
         		childValue.put("id", IDGenerater.getId());
         		childValue.put("f_ref", id);
         		childValue.put("GoodsCode",detMap.get("goodsCode"));
         		childValue.put("ArriveDate",detMap.get("submitDate")==null||"".equals(detMap.get("submitDate"))||BaseDateFormat.parse(billDate,BaseDateFormat.yyyyMMdd).after(BaseDateFormat.parse(detMap.get("submitDate").toString(),BaseDateFormat.yyyyMMdd))?billDate:detMap.get("submitDate"));
         		childValue.put("Qty", GlobalsTool.round(((BigDecimal)detMap.get("qty")).doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
         		childValue.put("TaxPrice", price);
         		childValue.put("TaxAmount",GlobalsTool.round(amttax, GlobalsTool.getDigitsOrSysSetting("tblBuyOrderDet", "TaxAmount")));
         		childValue.put("Price", price);
         		childValue.put("Amount", GlobalsTool.round(amttax, GlobalsTool.getDigitsOrSysSetting("tblBuyOrderDet", "Amount")));
         		childValue.put("UnitQty",GlobalsTool.round(((BigDecimal)detMap.get("qty")).doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
         		childValue.put("NotInQty",GlobalsTool.round(((BigDecimal)detMap.get("qty")).doubleValue(),GlobalsTool.getDigitsOrSysSetting("tblStockDet", "TotalQty")));
         		childValue.put("Unit", unit);
         		childValue.put("ConversionRate", "1");
         		childValue.put("SCompanyID", lg.getSunCmpClassCode());
         		childValue.put("StockCode",detMap.get("stockCode")==null?"":detMap.get("stockCode").toString());
         		String presentSampleType="";
         		if(Double.parseDouble(price)==0){
         			presentSampleType="1";
         		}
         		childValue.put("PresentSampleType", presentSampleType);
         		
         		ArrayList attList=BaseEnv.attList;
				for(int j=0;j<attList.size();j++){
					GoodsAttributeBean propBean=(GoodsAttributeBean)attList.get(j);
					if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						childValue.put(propBean.getPropName(), detMap.get(propBean.getPropName()));
					}
         		}
				//采购订单的颜色名称要填充到表中
				if(BaseEnv.version==3){//布匹版
					childValue.put("colorName", detMap.get("colorName"));
				}
         	}
         	rs=this.insertTable(values, tableName, request);
         	if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
         		break;
         	}
         	
         	isFirst=false;
		}		
		
		if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			if(rs.retCode==ErrorCanst.WORK_FLOW_NO_NEXT_NODE){
				EchoMessage.error().add(this.getMessage(request, "com.update.workflow.error")).setAlertRequest(request);
			}else{
				EchoMessage.error().add(rs.getRetCode(),request).setAlertRequest(request);
			}
		}else{
			/**********************更新MRP列表中的已下订单状态********************/
			String tracks="";
			for(int i=0;i<trackNos.length;i++){
				if(chooses[i].equals("false"))continue;
				String [] trackNoArray=trackNos[i].split(",");
				for(int k=0;k<trackNoArray.length;k++){
					if(trackNoArray[k].length()>0&&!tracks.contains("'"+trackNoArray[k].replaceAll(",", "','")+"',")){
						tracks+="'"+trackNoArray[k].replaceAll(",", "','")+"',";
					}
				}
			}
			if(tracks.length()>0){
				tracks=tracks.substring(0,tracks.length()-1);
				String sql="update tblProductMRP set hasOrder=1 where orderTrackNo in ("+tracks+")";
				mrpmgt.execSql(sql);
			}
			if("true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())){
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do?method=bomDemand&opeType=BackMrp&ProductMRPIds="+request.getParameter("ProductMRPIds")+"&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do?method=bomDemand&opeType=BackMrp&mrpFrom="+mrpFrom+"&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			}
		}
	}
	
	private Result insertTable(HashMap values,String tableName,HttpServletRequest request){
		DBTableInfoBean tableBean=(DBTableInfoBean)BaseEnv.tableInfos.get(tableName);
		UserFunctionMgt userMgt = new UserFunctionMgt();
		DynDBManager dbmgt = new DynDBManager();
		Object ob = request.getSession().getServletContext()
				.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName) ; 
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = GlobalsTool.getMOperationMap(request);
		
		//设置默认值
		Result rs=new Result();
		try {
			userMgt.setDefault(tableBean, values, this.getLoginBean(request).getId());
			rs= dbmgt.add(tableBean.getTableName(),BaseEnv.tableInfos, values, this.getLoginBean(request).getId(), 
					path, "", resources, this.getLocale(request), "",getLoginBean(request),workFlow,"",props);
			
		} catch (UnsupportedEncodingException e) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;
	}
	/**
	 * 判断当前商品及其子物料是否选中
	 * 
	 * @param cbchecked
	 *            复选框是否被选中字符串，选中为"true"否则为"false"
	 * @param orderId
	 *            关联单据ID，为空或不为空
	 * @return 如果cbchecked为false并且orderId不为空时才返回false
	 */
	public boolean IsChecked(String cbchecked, String orderId) {

		if ("true".equals(cbchecked)) {// 如果选中商品flag开关打开
			flag = true;
		} else if (!orderId.trim().equals("")) {// 如果商品没选中,并且不是子物料将flag开关关闭
			flag = false;
		}
		return flag;
	}
	/**
	 * 合并采购物料页面相同的商品
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void uniteSameGood(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] cb = request.getParameterValues("choose");
		String[] goodsNumbers=request.getParameterValues("goodsNumber");
		String[] goodsCodes = request.getParameterValues("goodsCode"); 
		String[] needProducts = request.getParameterValues("qty"); 
		String[] prices = request.getParameterValues("price"); 
		String[] trackNos= request.getParameterValues("trackNo"); 
		String[] bomdetIds= request.getParameterValues("bomdetId"); 
		String[] companyCodes=request.getParameterValues("companyCode");
		String[] companyNames=request.getParameterValues("companyName");
		String[] buyPersons=request.getParameterValues("buyPerson");
		String[] buyPersonNames=request.getParameterValues("buyPersonName");
		String[] stockCodes=request.getParameterValues("stockCode");
		String[] stockNames=request.getParameterValues("stockName");
		String[] stores=request.getParameterValues("store");		
		String[] mainPaths=request.getParameterValues("mainPath");
		String[] isPlaces=request.getParameterValues("isPlace");
		String[] existsPlaces=request.getParameterValues("existsPlace");
		String[] paths=request.getParameterValues("path");
		String[] submitDate=request.getParameterValues("submitDate");
		
		String submitType=request.getParameter("submitType");
		request.setAttribute("method", submitType);

		//查询所有商品的属性，以便统计是否是相同的商品
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		for(int i=0;cb!=null&&i<cb.length;i++){
			HashMap map=new HashMap();		
			map.put("trackNo", trackNos[i]);
			map.put("goodsCode", goodsCodes[i]); 
			map.put("price", prices[i]); 
			map.put("qty", needProducts[i]); 
			map.put("bomdetId", bomdetIds[i]);
			map.put("stockCode", stockCodes[i]);
			map.put("stockName", stockNames[i]);
			map.put("store", stores[i]);
			map.put("submitDate", submitDate[i]);
			if(submitType.equals("doOrder")){
				map.put("companyCode", companyCodes[i]);
				map.put("companyName", companyNames[i]);
			}else{
				map.put("buyPerson", buyPersons[i]);
				map.put("buyPersonName", buyPersonNames[i]);
			}
			map.put("goodsNumber", goodsNumbers[i]);
			map.put("mainPath", mainPaths[i]);
			map.put("isPlace", isPlaces[i]);
			map.put("existsPlace", Boolean.parseBoolean(existsPlaces[i]));
			map.put("path", paths[i]);
			//查询此商品属性
			this.setPropDisToMap(bomdetIds[i], goodsNumbers[i].indexOf("~")<0?true:false,map,isPlaces[i]);
			list.add(map);
		}
		
		//统计所有相同的商品
		ArrayList attList=BaseEnv.attList;
		HashMap goodsPropMap=new HashMap();
		ArrayList orderList=new ArrayList();
		for(int i=0;i<list.size();i++){
			HashMap map=list.get(i);
			String key="companyCode:"+map.get("companyCode")+"goods:"+map.get("goodsCode")+map.get("mainPath");
			for(int j=0;j<attList.size();j++){
				GoodsAttributeBean propBean=(GoodsAttributeBean)attList.get(j);
				if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
					key+=propBean.getPropName()+":"+map.get(propBean.getPropName()) ;
				}
			}
			
			if(goodsPropMap.get(key)==null||"true".equals(map.get("isPlace").toString())||"true".equals(map.get("existsPlace").toString())){
				goodsPropMap.put(key, map);				
				orderList.add(key);
			}else{
				HashMap oldMap=(HashMap)goodsPropMap.get(key);
				String newTrackNo=map.get("trackNo").toString();
				String oldTrackNo=oldMap.get("trackNo").toString();
				if(newTrackNo.length()>0&&oldTrackNo.indexOf(newTrackNo)<0){
					oldMap.put("trackNo", oldTrackNo+","+newTrackNo);
				}
				
				String newStockName=map.get("stockName").toString();
				String oldStockName=oldMap.get("stockName").toString();
				if(newStockName.length()>0&&oldStockName.indexOf(newStockName)<0){
					oldMap.put("stockCode", oldMap.get("stockCode").toString()+","+map.get("stockCode").toString());
					oldMap.put("stockName", oldStockName+","+newStockName);
				}
				
				String newStore=map.get("store").toString();
				String oldStore=oldMap.get("store").toString();
				if(Double.parseDouble(newStore)>0){
					oldMap.put("store", Double.parseDouble(newStore)+Double.parseDouble(oldStore));
				}
				
				String newQty=map.get("qty").toString();
				String oldQty=oldMap.get("qty").toString();
				oldMap.put("qty", new BigDecimal(newQty).add(new BigDecimal(oldQty)).doubleValue());
			}
		}
		
		//查询这些商品的最低采购量
		ArrayList<Map> newList=new ArrayList<Map>();//将整理后的商品存放到新的列表中
		for(int i=0;i<orderList.size();i++){
			String key=orderList.get(i).toString();
			HashMap map=(HashMap)goodsPropMap.get(key);
			map.put("leastQty", 0);
			
			Result leastRs = mgt.queryGoodLeastQty(map.get("goodsCode").toString());
			String[] values=(String[])leastRs.getRetVal();//查询这个商品的最低采购量与默认供应商
			
			//分析最低采购量
			values[0] = values[0]==null||values[0].equals("0E-8")?"0.0":values[0];
			if(Double.parseDouble(values[0])>0){
				int temp = 0;
				double  needed=Double.parseDouble(map.get("qty").toString());
				double leastQty=Double.parseDouble(values[0]);
				String value = String.valueOf(needed/leastQty);
				temp = Integer.parseInt(value.substring(0, value.lastIndexOf(".")));
				map.put("leastQty",leastQty);
				map.put("qty", (needed%leastQty==0.0?needed:(temp+1)*leastQty));
			}
			
			map.put("goodsFullName", values[4]);
			map.put("goodsSpec", values[5]);			
			newList.add(map);
		}
		boolean isOpenWork=false;
		String tableName="";
		String ProductMRPIds=request.getParameter("ProductMRPIds");
		request.setAttribute("ProductMRPIds", ProductMRPIds);
		String billNo="";
		LoginBean lg=this.getLoginBean(request);
		if(submitType.equals("doOrder") ){
			tableName="tblBuyOrder";
		}else{
			tableName="tblBuyApplication";
		}
		request.getSession().setAttribute("mrpList", newList);
		request.getSession().setAttribute("mrpFromPrint", request.getParameter("mrpFrom"));
		request.setAttribute("list", newList);
		request.setAttribute("mrpFrom", request.getParameter("mrpFrom"));
		isOpenWork=(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1);
		request.setAttribute("isOpenWork", isOpenWork);
		forward = mapping.findForward("doOrder");
	}
	
	/**
	 * 下定单
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void doOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String showMrpProc=BaseEnv.systemSet.get("showMrpProc").getSetting();
		
		if("true".equals(showMrpProc)){
			this.saveMrp(mapping, form, request, response);
		}else{
			this.saveMrpResult(mapping, form, request, response);
		}
		
		String[] cb = request.getParameterValues("choose");// 复选框选中表示要下定单的行
		String[] goodsNumbers=request.getParameterValues("goodsNumber");
		String[] goodsCodes = request.getParameterValues("goodsCode");// 所选择的需要下订单商品
		String[] needProducts = request.getParameterValues("needProduct");//净需求量
		String[] prices = request.getParameterValues("price");// 成本价
		String[] trackNos= request.getParameterValues("trackNo");//追踪单号
		String[] bomdetIds= request.getParameterValues("bomdetId");//追踪单号
		String[] store=request.getParameterValues("store");//实际库存
		String[] stockCodes=request.getParameterValues("stockCode");
		String[] stockNames=request.getParameterValues("stockName");
		request.setAttribute("mrpFrom", request.getParameter("mrpFrom"));
		String zeroApply=request.getParameter("zeroApply");
		String method = request.getParameter("method");	
		String[] isRePlaces=request.getParameterValues("isPlaceh");//这个变量只有后台使用
		String[] isRePlacess=request.getParameterValues("isPlace");//替换料表中的数据
		String[] mainPaths=request.getParameterValues("mainPath");
		String[] paths=request.getParameterValues("path");
		String[] submitDate=request.getParameterValues("submitDate");//到货日期
		boolean isMA=Boolean.parseBoolean(BaseEnv.systemSet.get("MaterielAttribute").getSetting());

		ArrayList<Map> list = new ArrayList<Map>();// 存放被选中下定单的mrp运算记录

		String trackNo="";
		String mainPath="";
		for (int i = 0; i < cb.length; i++) {
			if(trackNos[i].length()>0)trackNo=trackNos[i];
			
			if("".equals(goodsCodes[i]))break;
			
			Result leastRs = mgt.queryGoodLeastQty(goodsCodes[i]);//0最小包装量,7最少订单
			String[] values=(String[])leastRs.getRetVal();//查询这个商品的最低采购量与默认供应商
			
			String MaterielAttribute=values[10].toString();
			//假如当前物料是替换料，其主料净需求量>0，可以加载到下达采购订单的界面
			boolean moreZero=false;
			if("true".equals(showMrpProc)){
				if(isRePlaces[i].equals("true")&&mainPaths[i].equals(mainPath)){
					moreZero=true;
				}
			}
			if(needProducts[i]=="")needProducts[i]="0";
			//当下采购订单时，如果净需求量<0不显示，但如果是替换料，其主物料净需求量>0就需要下采购订单
			if((isMA&&!"WG".equals(MaterielAttribute))||cb[i].equals("false")||(!moreZero&&Float.parseFloat(needProducts[i])<=0&&method.equals("doOrder"))
					||(Float.parseFloat(needProducts[i])<=0&&method.equals("doQuoteBuy")&&zeroApply==null))continue;
			HashMap map=new HashMap();	
			
			map.put("trackNo", trackNo);
			map.put("goodsCode", goodsCodes[i]); 
			map.put("submitDate", submitDate[i]);
			map.put("price","true".equals(showMrpProc)?prices[i]:0); 
			map.put("qty", needProducts[i]); 
			map.put("leastQty",0);
			map.put("store",store[i]);
			map.put("isPlace","true".equals(showMrpProc)?"".equals(isRePlaces[i])?"false":isRePlaces[i]:false); //是否是替换料
			map.put("mainPath","true".equals(showMrpProc)?mainPaths[i]:"");//如果是替换料，mainPath是其主物料的替换料
			map.put("path", paths[i]);//物料的名称路径
			//检查之后是否有物料是当前物料的替换料
			boolean existsPlace=false;
			for(int j=i;"true".equals(showMrpProc)&&j<cb.length;j++){
				if(mainPaths[j].length()>0&&mainPaths[j].equals(paths[i])&&mainPaths[i].length()==0){
					existsPlace=true;
					mainPath=paths[i];
					break;
				}
			}
			map.put("existsPlace", existsPlace);
			
			
			//分析最低采购量
			values[0] = values[0]==null||values[0].equals("0E-8")?"0.0":values[0];
			if(Double.parseDouble(values[0])>0||Double.parseDouble(values[7])>0){
				int temp = 0;
				double needed=Double.parseDouble(needProducts[i]);
				if(needed<Double.parseDouble(values[7]))needed=Double.parseDouble(values[7]);
				double leastQty=Double.parseDouble(values[0]);
				if(leastQty>0){
					String value = String.valueOf(needed/leastQty);
					temp = Integer.parseInt(value.substring(0, value.lastIndexOf(".")));
					map.put("leastQty",GlobalsTool.formatNumber(leastQty, false, false, true, "tblGoods", "leastQty", true));
					map.put("qty", (needed%leastQty==0.0?needed:(temp+1)*leastQty));
				}else{
					map.put("leastQty",0);
					map.put("qty", needed);
				}
			}
			if("false".equals(showMrpProc)||"".equals(stockCodes[i])){
				map.put("stockCode", values[8]);
				map.put("stockName", values[9]);
			}else{
				map.put("stockCode", stockCodes[i]);
				map.put("stockName", stockNames[i]);
			}
			map.put("companyCode", values[1]);
			map.put("companyName", values[2]);
			map.put("goodsFullName", values[4]);
			map.put("goodsNumber", values[3]);
			map.put("goodsSpec", values[5]);
			map.put("unitName", values[6]);
			map.put("bomdetId", bomdetIds[i]);
			
			//查询此商品属性
			this.setPropDisToMap(bomdetIds[i], goodsNumbers[i].indexOf("~")<0?true:false,map,"true".equals(showMrpProc)?isRePlacess[i]:"");
			list.add(map);
		}	
		
		
		DBFieldInfoBean field=null;
		String tableName="";
		String billNo="";
		boolean isOpenWork=false;
		LoginBean lg=this.getLoginBean(request);
		if(method.equals("doOrder") ){
			tableName="tblBuyOrder";
		}else{
			tableName="tblBuyApplication";
		}
		request.setAttribute("BillNo", billNo);
		request.setAttribute("list", list);
		request.getSession().setAttribute("mrpList", list);
		request.getSession().setAttribute("mrpFromPrint", request.getParameter("mrpFrom"));
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/MrpBP.do"));
		request.setAttribute("MOID", mop.moduleId);
		request.setAttribute("method", method);
		forward = mapping.findForward("doOrder");
		isOpenWork=(BaseEnv.workFlowInfo.get(tableName)!=null&&BaseEnv.workFlowInfo.get(tableName).getTemplateStatus()==1);
		request.setAttribute("isOpenWork", isOpenWork);

	}
	/**
	 * 合并生产任务单相同的商品
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void uniteProSameGood(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] goodsNumbers = request.getParameterValues("goodsNumber");// 所选择的需要下订单商品
		String[] goodsFullNames = request.getParameterValues("goodsFullName");// 所选择的需要下订单商品
		String[] goodsCodes = request.getParameterValues("goodsCode");// 所选择的需要下订单商品
		String[] MaterielAttribute=request.getParameterValues("MaterielAttribute");// 物料属性
		String[] needProducts = request.getParameterValues("qty");//净需求量
		String[] prices = request.getParameterValues("price");// 成本价
		String[] trackNos= request.getParameterValues("trackNo");//追踪单号
		String[] bomdetIds=request.getParameterValues("bomdetId"); //当前明细的物料明细ID
		String[] stores=request.getParameterValues("store");//实际库存
		String[] stockCodes=request.getParameterValues("stockCode");
		String[] stockNames=request.getParameterValues("stockName");
		//替换料信息
		String[] isReplace=request.getParameterValues("isPlace");
		String[] isReplaces=request.getParameterValues("isPlaceh");
		String[] mainPaths=request.getParameterValues("mainPath");
		String[] existsPlaces=request.getParameterValues("existsPlace");
		String[] paths=request.getParameterValues("path");
		String[] isProduces=request.getParameterValues("isProduce");
		String[] startDate=request.getParameterValues("startDate");
		String[] submitDate=request.getParameterValues("submitDate");
		
		String temp;
		//先查询此商品属性，在判断是否是相同的商品
		ArrayList list=new ArrayList();
		for(int i=0;i<goodsNumbers.length;i++){
			HashMap map=new HashMap();
			map.put("goodsCode", goodsCodes[i]);
			map.put("goodsNumber", goodsNumbers[i]);
			map.put("goodsFullName", goodsFullNames[i]);
			map.put("MaterielAttribute", MaterielAttribute[i]);
			map.put("needProduct", needProducts[i]);
			map.put("price", prices[i]);
			map.put("trackNo", trackNos[i]);
			map.put("isProduce", isProduces[i]);	
			map.put("bomdetId", bomdetIds[i]);	
			map.put("store", stores[i]);
			map.put("stockCode", stockCodes[i]);
			map.put("stockName", stockNames[i]);
			map.put("bomdetId", bomdetIds[i]);
			map.put("startDate", startDate[i]);
			map.put("submitDate", submitDate[i]);
			map.put("isPlace", "true".equals(isReplace[i])?true:false);
			map.put("isPlaceh", "true".equals(isReplaces[i])?true:false);
			map.put("mainPath", mainPaths[i]);
			map.put("existsPlace","true".equals(existsPlaces[i])?true:false);
			map.put("path", paths[i]);
			this.setPropDisToMap(bomdetIds[i], goodsNumbers[i].indexOf("~")<0?true:false,map,isReplace[i]);
			list.add(map);	
		}
		
		ArrayList propList=BaseEnv.attList;
		HashMap goodsMap=new HashMap();
		ArrayList orderNo=new ArrayList();
		for(int i=0;i<list.size();i++){
			HashMap map=(HashMap)list.get(i);
			String key="goodsCode:"+goodsCodes[i];
			
			if(Boolean.parseBoolean(map.get("existsPlace").toString())||Boolean.parseBoolean(map.get("isPlace").toString())){
				key+=map.get("bomdetId");
				key+=map.get("existsPlace");
			}else{
				for(int j=0;j<propList.size();j++){
					GoodsAttributeBean propBean=(GoodsAttributeBean)propList.get(j);
					if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						key+=propBean.getPropName()+":"+map.get(propBean.getPropName());
					}
				}
			}
			
			if(goodsMap.get(key)!=null){
				HashMap oldMap=(HashMap)goodsMap.get(key);
				oldMap.put("needProduct",new BigDecimal(oldMap.get("needProduct").toString()).add(new BigDecimal(map.get("needProduct").toString())).doubleValue());
				String newTrackNo=map.get("trackNo").toString();
				String oldTrackNo=oldMap.get("trackNo").toString();
				if(newTrackNo.length()>0&&oldTrackNo.indexOf(newTrackNo)<0){
					oldMap.put("trackNo", oldTrackNo+","+newTrackNo);
				}	
				String newStockName=map.get("stockName").toString();
				String oldStockName=oldMap.get("stockName").toString();
				if(newStockName.length()>0&&oldStockName.indexOf(newStockName)<0){
					oldMap.put("stockCode", oldMap.get("stockCode").toString()+","+map.get("stockCode").toString());
					oldMap.put("stockName", oldStockName+","+newStockName);
				}
			}else{
				goodsMap.put(key, map);
				orderNo.add(key);
			}
		}
		ArrayList newList=new ArrayList();
		for(int i=0;i<orderNo.size();i++){
			newList.add(goodsMap.get(orderNo.get(i)));
		}
		String ProductMRPIds=request.getParameter("ProductMRPIds");
		request.setAttribute("ProductMRPIds", ProductMRPIds);
		request.setAttribute("mrpFrom", request.getParameter("mrpFrom"));
		request.getSession().setAttribute("mrpList", newList);
		request.getSession().setAttribute("mrpFromPrint", request.getParameter("mrpFrom"));
		request.setAttribute("result", newList);// 保存被选中要下定单的记录
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap()
				.get("/MrpBP.do"));
		request.setAttribute("MOID", mop.moduleId);
		forward = mapping.findForward("doProduceOrder");
	}
	
	/**
	 * 下生产任务单的商品列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void doProduceOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String showMrpProc=BaseEnv.systemSet.get("showMrpProc").getSetting();
		
		if("true".equals(showMrpProc)){
			this.saveMrp(mapping, form, request, response);
		}else{
			this.saveMrpResult(mapping, form, request, response);
		}
		
		String[] cbs = request.getParameterValues("choose");// 复选框选中表示要下定单的行
		String[] goodPropHashs = request.getParameterValues("goodPropHash");//成品的hash值
		String[] goodsNumbers = request.getParameterValues("goodsNumber");// 所选择的需要下订单商品
		String[] goodsFullNames = request.getParameterValues("goodsFullName");// 所选择的需要下订单商品
		String[] goodsCodes = request.getParameterValues("goodsCode");// 所选择的需要下订单商品
		String[] needProducts = request.getParameterValues("needProduct");//净需求量
		String[] prices = request.getParameterValues("price");// 成本价
		String[] trackNos= request.getParameterValues("trackNo");//追踪单号
		String[] bomdetIds=request.getParameterValues("bomdetId"); //当前明细的物料明细ID
		String[] stores=request.getParameterValues("store");//实际库存
		String[] stockCodes=request.getParameterValues("stockCode");
		String[] stockNames=request.getParameterValues("stockName");
		String[] MaterielAttribute=request.getParameterValues("MaterielAttribute");
		String[] isRePlaces=request.getParameterValues("isPlaceh");//这个变量只有后台使用
		String[] isRePlacess=request.getParameterValues("isPlace");//存在于替换料表
		String[] mainPaths=request.getParameterValues("mainPath");
		String[] paths=request.getParameterValues("path");
		String[] counts=request.getParameterValues("qty");
		String[] startDate=request.getParameterValues("startDate");//开工日期
		String[] submitDate=request.getParameterValues("submitDate");//完工日期
		String[] BOMIds=request.getParameterValues("BOMId");//对应的BOM主表ID
		boolean isMA=Boolean.parseBoolean(BaseEnv.systemSet.get("MaterielAttribute").getSetting());
		
		String temp;
		ArrayList list=new ArrayList();
		String trackNo="";
		String needProduct="0";
		double totalProd=0d;
		int existsQtySize=-1;
		String lastMainPath="";
		
		for(int i=0;i<cbs.length;i++){			
			//检查当前物料是否是替换料，或者存在替换料
			boolean existsPlace=false;
			for(int j=i;"true".equals(showMrpProc)&&j<cbs.length;j++){
				if(mainPaths[j].length()>0&&mainPaths[j].equals(paths[i])&&mainPaths[i].length()==0){
					existsPlace=true;
					break;
				}
			}
			boolean moreZero=false;
			if("true".equals(showMrpProc)){
				if(existsPlace||"true".equals(isRePlaces[i])){//假如是替换料或者是存在替换料，查询此物料的上级物料生产需求量是否大于0
					for(int j=i-1;j>=0;j--){
						if(goodsNumbers[j].lastIndexOf("~")<goodsNumbers[i].lastIndexOf("~")){
							if(Double.parseDouble(needProducts[j])>0){
								moreZero=true;
							}
							break;
						}
					}
				}
			}
			
			if(trackNos[i].length()>0)trackNo=trackNos[i];
			if(needProducts[i]=="")needProducts[i]="0";
			//当物料不是采购件，自制件并且不是替换料或者净需求量为0并且也不是替换料情况下跳到下次循环
			if((isMA&&"WG".equals(MaterielAttribute[i])&&!moreZero)||cbs[i].equals("false")||(Double.parseDouble(needProducts[i])==0&&!moreZero))continue; 
			
			if((("true".equals(showMrpProc)&&(i+1<cbs.length&&goodsNumbers[i+1].lastIndexOf("~")>goodsNumbers[i].lastIndexOf("~"))||moreZero))||("false".equals(showMrpProc)&&BOMIds[i].length()>0)){
				HashMap map=new HashMap();
				list.add(map);
				Result leastRs = mgt.queryGoodLeastQty(goodsCodes[i]);
				String[] values=(String[])leastRs.getRetVal();
				map.put("goodsCode", goodsCodes[i]);
				map.put("goodPropHash", goodPropHashs[i]);
				map.put("goodsNumber",goodsNumbers[i].substring(0,goodsNumbers[i].lastIndexOf("~")+1)+values[3]);//从页面读取物料级别
				map.put("goodsFullName", values[4]);
				map.put("MaterielAttribute", values[10]);
				map.put("startDate", startDate[i]);
				map.put("submitDate", submitDate[i]);
				if("true".equals(showMrpProc)&&(existsPlace||mainPaths[i].length()>0)){
					double need=Double.parseDouble(counts[i])*Double.parseDouble(needProduct);
					double prod=0;
					
					if(!mainPaths[i].equals(lastMainPath)){
						totalProd=0;
						existsQtySize=list.size()-1;
					}
					Double d=mgt.getQuantum(trackNo,goodsCodes[i],bomdetIds[i]);
					if(d>0)existsQtySize=list.size()-1;
					if(totalProd>=need){
						prod=0;
					}else if(d<(need-totalProd)){
						prod=d;
					}else{
						prod=need-totalProd;
					}
					
					totalProd+=prod;
					map.put("needProduct",prod);
					//假如是最后一条替换料或者是最后一条记录，并且物料数量仍然不足，则将不足的物料补到最后一个有库存的物料中
					if(((i+1==cbs.length||(i+1<cbs.length&&!mainPaths[i].equals(mainPaths[i+1])))&&totalProd<need)&&existsQtySize!=-1){
						HashMap hisMap=(HashMap)list.get(existsQtySize);
						hisMap.put("needProduct",Double.parseDouble(hisMap.get("needProduct").toString())+(need-totalProd));
					}
					
					lastMainPath=mainPaths[i];					
				}else{
					needProduct=needProducts[i];
					map.put("needProduct", needProducts[i]);
				}
				map.put("price","true".equals(showMrpProc)?prices[i]:0);				
				map.put("trackNo", trackNos[i]);
				map.put("bomdetId", bomdetIds[i]);
				map.put("store",stores[i]);
				map.put("stockCode","true".equals(showMrpProc)?stockCodes[i]:"");
				map.put("stockName","true".equals(showMrpProc)?stockNames[i]:"");
				map.put("existsPlace", existsPlace);
				map.put("isPlaceh","true".equals(showMrpProc)?"".equals(isRePlaces[i])?"false":isRePlaces[i]:false); //是否是替换料
				map.put("isPlace", "true".equals(showMrpProc)?"".equals(isRePlacess[i])?"false":isRePlacess[i]:false); //是否是替换料
				map.put("mainPath","true".equals(showMrpProc)?mainPaths[i]:"");//如果是替换料，mainPath是其主物料的替换料
				map.put("path", paths[i]);//物料的名称路径
				String mainDetId="";
				if("true".equals(showMrpProc)&&isRePlaces[i].equals("true")){
					for(int j=0;j<cbs.length;j++){
						if(paths[j].equals(mainPaths[i])){
							mainDetId=bomdetIds[j];
							break;
						}
					}
				}
				map.put("mainDetId", mainDetId);//保存主替换料的明细表ID
				//判断当前商品是否是成品，或者半成品
				if(("true".equals(showMrpProc)&&((i+1<cbs.length&&goodsNumbers[i+1].lastIndexOf("~")>goodsNumbers[i].lastIndexOf("~"))))||("false".equals(showMrpProc)&&BOMIds[i].length()>0)){
					map.put("isProduce", "true");
				}else{
					map.put("isProduce", "false");
				}
				
				//查询此商品属性
				this.setPropDisToMap(bomdetIds[i], goodsNumbers[i].indexOf("~")<0?true:false,map,"true".equals(showMrpProc)?isRePlacess[i]:"");
				
			}
		}
		request.setAttribute("mrpFrom", request.getParameter("mrpFrom"));
		request.getSession().setAttribute("mrpList", list);
		request.getSession().setAttribute("mrpFromPrint", request.getParameter("mrpFrom"));
		request.setAttribute("result", list);// 保存被选中要下定单的记录
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap()
				.get("/MrpBP.do"));
		request.setAttribute("MOID", mop.moduleId);
		forward = mapping.findForward("doProduceOrder");
	}
	/**
	 * MRP信息查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void mrpSel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int pageSize=request.getParameter("pageSize")==null?GlobalsTool.getPageSize():Integer.parseInt(request.getParameter("pageSize"));
		int pageNo=request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo"));
		
		MOperation  mop = (MOperation)this.getLoginBean(request).
        getOperationMap().get("/MrpBP.do");
		String mrpFrom = request.getParameter("mrpFrom")==null?"":request.getParameter("mrpFrom");
		String billNo = request.getParameter("billNo")==null?"":request.getParameter("billNo");
		String goods = request.getParameter("goods")==null?"":request.getParameter("goods");
		String employee = request.getParameter("employee")==null?"":request.getParameter("employee");
		String department = request.getParameter("department")==null?"":request.getParameter("department");
		String[] conditions = new String[]{mrpFrom,billNo,goods,employee,department};
		rs = getProductMRP(this.getLoginBean(request).getId(),mop,conditions,pageSize,pageNo);// 获得MRP主表信息		
		request.setAttribute("list", rs.retVal);
		ArrayList scopeRightUpdate=mop.getScope(MOperation.M_UPDATE);
		String updateOtherRight="";
		String updateDeptRight="";
		for(int i=0;i<scopeRightUpdate.size();i++){
			LoginScopeBean lsb = (LoginScopeBean) scopeRightUpdate.get(i);
			if("1".equals(lsb.getFlag())){
				updateOtherRight+=lsb.getScopeValue();
			}
			if("5".equals(lsb.getFlag())){
				updateDeptRight+=lsb.getScopeValue();
			}
		}
		request.setAttribute("loginId", this.getLoginBean(request).getId());
		request.setAttribute("pageBar", this.pageBar(rs, request));// 返回分页信息
		request.setAttribute("mrpFrom", mrpFrom);
		request.setAttribute("billNo", billNo);
		request.setAttribute("goods", goods);
		request.setAttribute("employee", employee);
		request.setAttribute("department", department);
		forward = mapping.findForward("mrpManager");// 设置跳转
	}

	/**
	 * MRPDet查询（从表查询）
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	private void mrpDetSel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = request.getParameter("id").trim();// 得到mrp主表的主键

		// 设置查询字符串
		String sql = "select p.mrpFrom,pd.f_ref,pd.materialID,pd.ProductQty,pd.StoreQty,pd.SafeStoreQty";
		sql += ",pd.StoringQty,pd.ProductingQty,pd.UsedQty,pd.NeededQty,pd.createDate";
		sql += ",pd.EmployeeId,pd.DepartmentCode,pd.SCompanyID,pd.goodsNumber";
		sql += " from tblProductMRPDet pd,tblproductMRP p where pd.f_ref='"
				+ id + "'";
		sql += " and pd.f_ref=p.id ";
		rs = new MrpBPMgt().querySql(sql); // 查询
		request.setAttribute("list", rs.retVal);
		forward = mapping.findForward("mrpDetSel");// 设置跳转
	}

	/**
	 * 当在物料界面点击保存，或者下达采购订单，或者下达生产任务单时都会执行此方法
	 * 保存时会将所占用的采购订单，生产任务单标识追踪单号，并增加tblStocks中所占用的库存已分配量
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void saveMrp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		MrpBPMgt mrpMgt = new MrpBPMgt();
		String[] counts = request.getParameterValues("count");// 毛需求
		String[] trackNos = request.getParameterValues("trackNo");
		String[] goodsCodes = request.getParameterValues("goodsCode");
		String[] goodsNumbers = request.getParameterValues("goodsNumber");
		String[] goodsFullNames = request.getParameterValues("goodsFullName");
		String[] storeings = request.getParameterValues("storeingHid");// 在途量
		String[] productings = request.getParameterValues("producting");// 在产量
		String[] useds = request.getParameterValues("usedHid");// 库存已分配量
		String[] cbse = request.getParameterValues("choose");
		String[] bomdetIds=request.getParameterValues("bomdetId");
		String[] rePlaces=request.getParameterValues("isPlace");
		String[] stockCode=request.getParameterValues("stockCode");
		String[] keyIds=request.getParameterValues("keyId");
		String[] bomNos=request.getParameterValues("bomNo");
		String[] isRePlaces=request.getParameterValues("isPlaceh");//包括替换料和被替换料
		String[] isRePlaceSs=request.getParameterValues("isPlace");//只有替换料
		String[] storeingSelfs=request.getParameterValues("storeingSelf");//在途量_本单
		String[] productingSelfs=request.getParameterValues("productingSelf");//在产量_本单
		String[] usedSelfs=request.getParameterValues("usedSelf");//库存已分配量_本单
		String[] orderSelfs=request.getParameterValues("orderSelf");//订单已分配量_本单
		String[] cbs=request.getParameterValues("cb");//复选框
		
		
		String mrpFrom=request.getParameter("mrpFrom");		
		String billType="tblSalesOrder";
		if(mrpFrom==null){
			mrpFrom =request.getSession().getAttribute("mrpFrom").toString() ;//取出订单来源
		}
		if(mrpFrom.equals("1")){
			billType="tblPlan";
		}else if(mrpFrom.equals("0")){
			billType="tblBom";
		}
		
		/******找出所有选中的销售订单的商品和追踪单号，以便在订单分配量表中插入数据**********/
		ArrayList trackNoGood=new ArrayList();
		for(int i=0;i<cbse.length;i++){
			if(cbse[i].equals("false"))continue;
			if(goodsNumbers[i].indexOf("~")<0){
				String propVal=""+goodsCodes[i]+"GoodsCode";
				trackNoGood.add(new String[]{trackNos[i],goodsCodes[i],bomNos[i],counts[i],propVal});
			}
		}
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		mrpMgt.insertOrderQuantum(trackNoGood, lg.getId(),billType);
		
		/****根据在途量数量更新采购订单的追踪单号,先得到本次对应物料总共占用了多少在途量，更新对应物料在途量的追踪单号***/
		HashMap<String,String[]> goodMap = new HashMap<String, String[]>();
		ArrayList goods = new ArrayList();
		String trackNo="";
		for (int i = 0; i < goodsCodes.length; i++) {
			//在途量大于0的情况才需要更新采购
			if(trackNos[i].length()>0)trackNo=trackNos[i];
			if(Double.parseDouble(storeings[i])>0){
				String[] s = null;
				if(goodMap.get(goodsCodes[i])!=null){
					s = goodMap.get(goodsCodes[i]);
					double addUp = Double.parseDouble(s[1])+Double.parseDouble(storeings[i]);
					s[1] = String.valueOf(addUp);
				}else{
					s=new String[2];
					s[0] = trackNo;
					s[1] = storeings[i];
					goodMap.put(goodsCodes[i], s);
					goods.add(goodsCodes[i]);
				}
			}
		}		
		mrpMgt.updateBuyOrderTrackNos(goods, goodMap);
		
		/************************更新分仓库存表中本次运算商品所占用的库存已分配量*************************/
		String orderDetId="";
		ArrayList quanList=new ArrayList();
		for (int i = 0; i < cbse.length; i++) {
			if(cbse[i].equals("false"))continue; 
			//增加分仓库存库存已分配量			
			if(Double.parseDouble(useds[i])>0){
				if(trackNos[i].length()>0)trackNo=trackNos[i];
				mrpMgt.updateStockQuantum(Double.parseDouble(useds[i]),bomdetIds[i],goodsNumbers[i].indexOf("~")<0?true:false,stockCode[i],trackNo,rePlaces[i]);
			}else if("true".equals(isRePlaces[i])&&(Double.parseDouble(usedSelfs[i])>0||Double.parseDouble(orderSelfs[i])>0)){//是替换料，存在本单数量，但没有被勾选，在分仓库存要减去相应的数量
				boolean isselect=false;
				for(int j=1;j<cbs.length;j++){//jsp界面的checkBox只有被选中的，值才传递到后台，cbs里面保存的值是其行数
					if((i+1)==Integer.parseInt(cbs[j])){
						isselect=true;
						break;
					}
				}
				if(!isselect){//没有被勾选
					
					String goodPropHash=mrpMgt.getGoodsHash(false, bomdetIds[i], "true".equals(isRePlaceSs[i])?true:false);
					quanList.addAll(mrpMgt.updateSignStock(goodPropHash,Double.parseDouble(usedSelfs[i]),Double.parseDouble(orderSelfs[i])));
				}
			}
		}	
		//替换料的分仓库存表减去库存已分配量，订单已分配量
		if(quanList.size()>0){
			String []quans=new String[quanList.size()];
			for(int i=0;i<quanList.size();i++){
				quans[i]=quanList.get(i).toString();
			}
			mrpMgt.execBath(quans);
		}
		/*************************根据本单所占用的库存已分配量，在途量，在产量更新订单分配量表**********************/
		ArrayList trackNosList = new ArrayList();
		HashMap<String,ArrayList> orderMap = new HashMap<String, ArrayList>();
		String oper="";
		for (int i = 0; i < cbse.length; i++) {
			if(trackNos[i].length()>0)trackNo=trackNos[i];
			if(cbse[i].equals("false"))continue;
			if(!trackNo.equals("")){
				if(keyIds[i]!=null&&keyIds[i].length()>0){orderDetId=keyIds[i];}
				ArrayList bomDetList=null;
				if(orderMap.get(trackNo)!=null){
					bomDetList=orderMap.get(trackNo);
				}else{
					bomDetList=new ArrayList();
					orderMap.put(trackNo, bomDetList);
					trackNosList.add(trackNo);
				}
				
				HashMap<String,String> detMap = new HashMap<String, String>();//放入数据				
				
				detMap.put("TrackNo", trackNo.trim());
				detMap.put("GoodsCode", goodsCodes[i].trim());
				detMap.put("stockQuantum", useds[i].trim());
				detMap.put("buyingNum", storeings[i].trim());
				detMap.put("proingNum", productings[i].trim());
				detMap.put("levelCount",(goodsNumbers[i].lastIndexOf("~")+1)+"");
				detMap.put("orderDetId", orderDetId);
				detMap.put("bomdetId", bomdetIds[i]);
				detMap.put("oper", "add");
				//是替换料，存在本单数量，但没有被勾选，调用修改订单已分配量表存储过程应用delete
				if("true".equals(isRePlaces[i])&&(Double.parseDouble(usedSelfs[i])>0||Double.parseDouble(orderSelfs[i])>0
						||Double.parseDouble(storeingSelfs[i])>0||Double.parseDouble(productingSelfs[i])>0)){
					boolean isselect=false;
					for(int j=1;j<cbs.length;j++){//jsp界面的checkBox只有被选中的，值才传递到后台，cbs里面保存的值是其行数
						if((i+1)==Integer.parseInt(cbs[j])){
							isselect=true;
							break;
						}
					}
					if(!isselect){//没有被勾选
						detMap.put("stockQuantum", usedSelfs[i].trim());
						detMap.put("buyingNum", storeingSelfs[i].trim());
						detMap.put("proingNum", productingSelfs[i].trim());
						detMap.put("oper", "delete");
					}
				}
				
				bomDetList.add(detMap);
			}
		}		
		mrpMgt.updateOrderQuantum(trackNosList, lg.getId(), orderMap);
		
		/**********************保存本次MRP预算记录至tblProductMRP*******************************/	
		//删除之前的MRP运算的明细表
		if(trackNos.length>0){
			String orders="";
			for(int i=0;i<trackNos.length;i++){	
				orders+="'"+trackNos[i]+"',";
			}
			orders=orders.substring(0,orders.length()-1);
			new MrpBPMgt().execSql("delete from tblProductMrpDet where orderTrackNo in ("+orders+")");
		}
		
		String[] stores = request.getParameterValues("store");// 实际库存
		String[] storeSelves = request.getParameterValues("storeingSelf");//本单在途量
		String[] productSelves = request.getParameterValues("productingSelf");//本单在产量
		String[] usedSelves = request.getParameterValues("usedSelf");//本单库存已分配量
		String[] orderSelves = request.getParameterValues("orderSelf");//本单订单已分配量
		String[] neededs = request.getParameterValues("needed");// 净需求
		String[] stockFullNames=request.getParameterValues("stockName");//仓库名称
		
		String count = null; 
		String store = null; 
		String used = null; 
		String storeSelf = null;
		String productSelf = null;
		String usedSelf = null;
		String orderSelf = null;
		String needed = null;// 取出一个净需求
		String orderId = null;// 记录订单关联ID
		String goodsId = null;// 记录商品/物料ID
		String employeeId = null;// 经手人
		String MaterialGradeChar;// 物料等级字符
		StringBuffer sql = null;// 记录数据库的语句
		ArrayList<String> sqls = new ArrayList<String>();// 记录所有要执行的sql语句
		String id = null;// 用于记录编号，数据库中的主键(mrp主表)
		String detId = null;// 用于记录编号，数据库中的主键(mrp从表)
		String createDate = null;// 用于记录记录日期
		String ProductMRPIds="";//多个订单MRPID

		ArrayList salesOrders=new ArrayList();
		ArrayList attList=BaseEnv.attList;
		String trackNoA="";
		for(int i = 0; i < cbse.length ; i++) { 
			if(cbse[i].equals("false"))continue;
			
			count =  GlobalsTool.formatNumberS(Double.parseDouble(counts[i].replaceAll(",", "")), false, false, "Qty", "");// 净需求可能有','，如1,000,000
			store =  GlobalsTool.formatNumberS(Double.parseDouble(stores[i].replaceAll(",", "")), false, false, "Qty", "");
			used =  GlobalsTool.formatNumberS(Double.parseDouble(useds[i].replaceAll(",", "")), false, false, "Qty", "");
			storeSelf =  GlobalsTool.formatNumberS(Double.parseDouble(storeSelves[i].replaceAll(",", "")), false, false, "Qty", "");
			productSelf =  GlobalsTool.formatNumberS(Double.parseDouble(productSelves[i].replaceAll(",", "")), false, false, "Qty", "");
			usedSelf =  GlobalsTool.formatNumberS(Double.parseDouble(usedSelves[i].replaceAll(",", "")), false, false, "Qty", "");
			orderSelf =  GlobalsTool.formatNumberS(Double.parseDouble(orderSelves[i].replaceAll(",", "")), false, false, "Qty", "");
			needed =(neededs[i].replaceAll(",", "")==""?"0":GlobalsTool.formatNumberS(Double.parseDouble(neededs[i].replaceAll(",", "")), false, false, "Qty", ""));
			sql = new StringBuffer();
			

			if (goodsNumbers[i].indexOf("~")<0){//如果是成品，将数据记录到MRP主表
				trackNoA=trackNos[i];
				createDate=BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
				String periodYear=createDate.substring(0,createDate.indexOf("-")) ;
				String periodMonth=createDate.substring(createDate.indexOf("-")+1,createDate.lastIndexOf("-")) ;
				String period=periodMonth;
				id ="";
				//查询此追踪单号是否已经存在如果已经存在，修改而不是插入
				Result rs=new AIODBManager().sqlList("select id from tblProductMRP where orderTrackNo='"+trackNos[i]+"'", new ArrayList());
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					if(rs.retVal!=null&&((ArrayList)rs.retVal).size()>0){
						id=((Object[])((ArrayList)rs.retVal).get(0))[0].toString();
					}	
				}
				if(id.length()==0){
					id = IDGenerater.getId();
					sql.append("insert into tblProductMRP(id,mrpFrom,relationNo");
					sql.append(",goodsName,ProductQty,CreateDate,StoreQty,SafeStoreQty");
					sql.append(",StoringQty,ProductingQty,UsedQty,StoringQtySelf,ProductingSelf,UsedQtySelf,OrderQtySelf,orderTrackNo,createBy"+
							",employeeId,stockCode,stockCodeN,departmentCode,period,periodMonth,periodYear,RelationID,bomdetId,SCompanyID,bomNo"+
							",goodsNumber,goodsCode,hasProduce,hasOrder,hasApp,workFlowNodeName");
					
					sql.append(") values('" + id + "','" + mrpFrom + "','" + trackNos[i]
							+ "','" + goodsFullNames[i]);
					sql.append("','" + count + "','" + createDate);
					sql.append("','" + store + "','0','" + GlobalsTool.formatNumberS(Double.parseDouble(storeings[i]), false, false, "Qty", "") + "','" 
							+GlobalsTool.formatNumberS(Double.parseDouble( productings[i]), false, false, "Qty", "") + "','" + used + "','"+ storeSelf+ "','" + productSelf+ "','" + usedSelf+ "','" + orderSelf+ "','" + trackNos[i] + "','"+this.getLoginBean(request).getId()+"','"
							+ this.getLoginBean(request).getEmpFullName() + "','"+stockCode[i]+"','"+stockFullNames[i]+"','"
							+this.getLoginBean(request).getDepartCode()+"',"+period+","+periodMonth+","+periodYear+",'"+keyIds[i]+"','"
							+bomdetIds[i]+"','"+lg.getSunCmpClassCode()+"','"+bomNos[i]+"','"+goodsNumbers[i]+"','"+goodsCodes[i]+"',0,0,0,'finish'");
	
					sql.append(")");
				}else{
					sql.append("update tblProductMRP set bomNo='"+bomNos[i]+"',StoreQty="+store+",SafeStoreQty=0");
					sql.append(",UsedQty="+used+",StoringQtySelf="+storeSelf+",ProductingSelf="+productSelf+",UsedQtySelf="+usedSelf+",OrderQtySelf="+orderSelf+",StoringQty=(case when MRPFrom=2 then (select buyIngNum from tblSalesOrderQuantum where orderDetId=tblProductMRP.RelationID and salesOrderId=tblProductMRP.orderTrackNo and goodsCode=tblProductMRP.goodsCode and  bomdetId=tblProductMRP.bomdetId) else (select buyIngNum from tblSalesOrderQuantum where salesOrderId=tblProductMRP.orderTrackNo "+
				"and goodsCode=tblProductMRP.goodsCode and  bomdetId=tblProductMRP.bomdetId) end),"+
				"ProductingQty=(case when MRPFrom=2 then (select proingNum from tblSalesOrderQuantum where orderDetId=tblProductMRP.RelationID and salesOrderId=tblProductMRP.orderTrackNo and goodsCode=tblProductMRP.goodsCode and  bomdetId=tblProductMRP.bomdetId)"+
				" else (select proingNum from tblSalesOrderQuantum where salesOrderId=tblProductMRP.orderTrackNo and goodsCode=tblProductMRP.goodsCode and  bomdetId=tblProductMRP.bomdetId) end) where id='"+id+"'");
				}		
				ProductMRPIds=ProductMRPIds+id+",";				
			} else {// 如果是子级物料信息，则插入mrp从表
				detId = IDGenerater.getId();
				sql.append("insert into tblProductMrpDet(id,f_ref,goodsNumber,MaterialID,ProductQty,StoreQty");
				sql.append(" ,SafeStoreQty,StoringQty,ProductingQty");
				sql.append(" ,UsedQty,StoringQtySelf,ProductingSelf,UsedQtySelf,OrderQtySelf,orderTrackNo,NeededQty,createDate,"+
						"MaterialGradeChar,stockCode,stockCodeN,salesOrderDetId,bomdetId,SCompanyID");

				sql.append(") values('" + detId + "','" + id + "','"+goodsNumbers[i]+"','" + goodsFullNames[i]
						+ "','" +  GlobalsTool.formatNumberS(Double.parseDouble(count), false, false, "Qty", "") + "','" + GlobalsTool.formatNumberS(Double.parseDouble(store), false, false, "Qty", "") + "'");
				sql.append(" ,'0','" + GlobalsTool.formatNumberS(Double.parseDouble(storeings[i]), false, false, "Qty", "") + "','" + GlobalsTool.formatNumberS(Double.parseDouble(productings[i]), false, false, "Qty", "")  + "'");
				sql.append(" ,'" + GlobalsTool.formatNumberS(Double.parseDouble(used), false, false, "Qty", "") + "','"+ GlobalsTool.formatNumberS(Double.parseDouble(storeSelf), false, false, "Qty", "")+ "','" + GlobalsTool.formatNumberS(Double.parseDouble(productSelf), false, false, "Qty", "")+ "','" + GlobalsTool.formatNumberS(Double.parseDouble(usedSelf), false, false, "Qty", "")+ "','" + GlobalsTool.formatNumberS(Double.parseDouble(orderSelf), false, false, "Qty", "")
						+ "','" +trackNoA + "','" + GlobalsTool.formatNumberS(Double.parseDouble(needed), false, false, "Qty", "") + "','"
								+ createDate + "','','"+stockCode[i]+"','"+stockFullNames[i]+"','"+keyIds[i]+"','"+bomdetIds[i]+"','"+lg.getSunCmpClassCode()+"'");				
	
				sql.append(")");
			}

			sqls.add(sql.toString());// 将一条插入语句放入sqls中(批处理要执行的sql语句集)

		}
		request.setAttribute("ProductMRPIds", ProductMRPIds.substring(0,ProductMRPIds.length()-1));
		String[] sqlArr = new String[sqls.size()];// 记录sqls中的string语句
		for (int i = 0; i < sqls.size(); i++) {// 循环取出sqls中的sql字符串放入sqlArr中
			sqlArr[i] = (String) sqls.get(i);
		}
		//修改MRP运算主表或者插入主表及明细表
		new MrpBPMgt().execBath(sqlArr);// 执行批处理
		
		 
		forward = getForward(request, mapping, "alert");
		EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do").setAlertRequest(request) ;
	}
	
	/**
	 * 当在物料界面点击保存，或者下达采购订单，或者下达生产任务单时都会执行此方法
	 * 此方法用于判断当前追踪单号是否已经下采购订单、申请单、生产任务单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void saveMrpResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		MrpBPMgt mrpMgt = new MrpBPMgt();
		String [] trackNos=request.getSession().getAttribute("MRP_trackNos").toString().split(",");
		String [] counts=request.getSession().getAttribute("MRP_counts").toString().split(",");
		String [] chooses=request.getSession().getAttribute("MRP_chooses").toString().split(",");
		String [] bomNos=request.getSession().getAttribute("MRP_bomNos").toString().split(",");
		String [] mrpFroms=request.getSession().getAttribute("MRP_mrpFroms").toString().split(",");
		
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		
		/**********************保存本次MRP预算记录至tblProductMRP*******************************/	
		
		String count = null; 
		String orderId = null;// 记录订单关联ID
		String goodsId = null;// 记录商品/物料ID
		String employeeId = null;// 经手人
		String MaterialGradeChar;// 物料等级字符
		StringBuffer sql = null;// 记录数据库的语句
		ArrayList<String> sqls = new ArrayList<String>();// 记录所有要执行的sql语句
		String id = null;// 用于记录编号，数据库中的主键(mrp主表)
		String detId = null;// 用于记录编号，数据库中的主键(mrp从表)
		String createDate = null;// 用于记录记录日期
		String ProductMRPIds="";//多个订单MRPID

		ArrayList salesOrders=new ArrayList();
		ArrayList attList=BaseEnv.attList;
		String trackNoA="";
		
		createDate=BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		String periodYear=createDate.substring(0,createDate.indexOf("-")) ;
		String periodMonth=createDate.substring(createDate.indexOf("-")+1,createDate.lastIndexOf("-")) ;
		String period=periodMonth;
		for(int i = 0; i < trackNos.length ; i++) { 
			if(!chooses[i].equals("true"))continue;
			
			trackNoA=trackNos[i];
			count =(counts[i].replaceAll(",", "")==""?"0":GlobalsTool.formatNumberS(Double.parseDouble(counts[i].replaceAll(",", "")), false, false, "Qty", ""));
			sql = new StringBuffer();			
			id ="";
			//查询此追踪单号是否已经存在如果已经存在，修改而不是插入
			Result rs=new AIODBManager().sqlList("select id from tblProductMRP where orderTrackNo='"+trackNos[i]+"'", new ArrayList());
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				if(rs.retVal!=null&&((ArrayList)rs.retVal).size()>0){
					id=((Object[])((ArrayList)rs.retVal).get(0))[0].toString();
				}	
			}
			if(id.length()==0){
				id = IDGenerater.getId();
				sql.append("insert into tblProductMRP(id,mrpFrom,relationNo,bomdetId");
				sql.append(",goodsName,ProductQty,CreateDate,orderTrackNo,createBy"+
							",employeeId,stockCode,stockCodeN,departmentCode,period,periodMonth,periodYear,RelationID,SCompanyID,bomNo"+
							",goodsNumber,goodsCode,hasProduce,hasOrder,hasApp,workFlowNodeName");
					
				sql.append(") select '" + id + "','" + mrpFroms[i] + "','" + trackNos[i]+ "',c.id,b.GoodsFullName");
				sql.append(",'" + count + "','" + createDate+"','" + trackNos[i] + "','"+this.getLoginBean(request).getId()+"','"
							+ this.getLoginBean(request).getEmpFullName() + "','','','"
							+this.getLoginBean(request).getDepartCode()+"',"+period+","+periodMonth+","+periodYear+",'','"+lg.getSunCmpClassCode()+"','"+bomNos[i]+"',b.GoodsNumber,a.goodsCode,0,0,0,'finish'");
				if("2".equals(mrpFroms[i])){
					sql.append(" from tblSalesOrderDet a,tblGoods b,tblBOM c where a.goodsCode=b.classCode and a.trackNo='"+ trackNos[i]+"' and c.BillNo='"+bomNos[i]+"' ");
				}else{
					sql.append(" from tblPlanDet a,tblGoods b,tblBOM c where a.goodsCode=b.classCode and a.trackNo='"+ trackNos[i]+"' and c.BillNo='"+bomNos[i]+"' ");
				}
			}
			
			ProductMRPIds=ProductMRPIds+id+",";				

			sqls.add(sql.toString());// 将一条插入语句放入sqls中(批处理要执行的sql语句集)

		}
		request.setAttribute("ProductMRPIds", ProductMRPIds.substring(0,ProductMRPIds.length()-1));
		String[] sqlArr = new String[sqls.size()];// 记录sqls中的string语句
		for (int i = 0; i < sqls.size(); i++) {// 循环取出sqls中的sql字符串放入sqlArr中
			sqlArr[i] = (String) sqls.get(i);
		}
		//修改MRP运算主表或者插入主表
		new MrpBPMgt().execBath(sqlArr);// 执行批处理
		
		 
		forward = getForward(request, mapping, "alert");
		EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/MrpBP.do").setAlertRequest(request) ;
	}
	
	/**
	 * 订单查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void orderSel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MOperation  mop = (MOperation)this.getLoginBean(request).
        getOperationMap().get("/MrpBP.do");
		String mrpFrom = request.getParameter("mrpFrom");// 得到要查询的订单来源
		String selCondition = request.getParameter("selCondition");
		String goodsNumber = request.getParameter("goodsNumber");//成品编号
		String goodsFullName = request.getParameter("goodsFullName");// 成品名称
		String goodsSpec = request.getParameter("goodsSpec");// 成品规格
		String billNo=request.getParameter("billNo");

		String where = " where 1=1 ";// 组成的查询条件字符串
		String date1 = request.getParameter("sDate");// 条件日期1
		String date2 = request.getParameter("eDate");
		String operStatus=request.getParameter("operStatus");
		
		request.setAttribute("goodsNumber", goodsNumber) ;
		request.setAttribute("goodsFullName", goodsFullName) ;
		request.setAttribute("goodsSpec", goodsSpec) ;
		request.setAttribute("sDate", date1) ;
		request.setAttribute("eDate", date2) ;
		request.setAttribute("billNo", billNo);
		request.setAttribute("operStatus", operStatus);
		if(goodsNumber!=null && goodsNumber.trim().length()>0){
			where += " and (c.goodsNumber like '%" + goodsNumber.trim() + "%')";
		}
		if(goodsFullName!=null && goodsFullName.trim().length()>0){
			where += " and (c.goodsFullname like '%" + goodsFullName.trim() + "%')";
		}
		if(goodsSpec!=null && goodsSpec.trim().length()>0){
			where += " and (c.goodsSpec like '%" + goodsSpec.trim() + "%')";
		}
		
		if(billNo!=null&&billNo.length()>0){
			where+=" and b.trackNo like '%"+billNo.trim()+"%'";
		}
		date1 = date1==null?"":date1.trim();
		date2 = date2==null?"":date2.trim();
		String colDate = "a.sendDate";
		if("1".equals(mrpFrom)){
			colDate = "a.startDate";
		}
		if (!"".equals(date1) && "".equals(date2)) {// 前一日期存在，后一日期不存在，则查询大于等于前一日期的记录
			where += " and "+colDate+" >='" + date1 + "'";
		} else if ("".equals(date1) && !"".equals(date2)) {// 仅后一日期存在，则查询小于该日期的记录
			where += " and "+colDate+" <='" + date2 + "'";
		} else if (!"".equals(date1) && !"".equals(date2)) {// 当两个日期都存在时，查询日期之间的记录
			where += " and "+colDate+" between '" + date1 + "' and '" + date2
					+ "'";
		} else {// 如果两个日期都不存在，商品也不存在
			where += "";
		}
		if (mrpFrom == null)// 如果来源为null则设为默认查询销售订单1
			mrpFrom = "2";
		if("true".equals(BaseEnv.systemSet.get("showMrpProc").getSetting())){
			rs = getOrders(mrpFrom, where,mop,operStatus);// 获取定单数据
		}else{
			rs =mgt.getOperBil(where,operStatus);// 获取运算单据数据
		}
		
		request.setAttribute("bomNoMap", mgt.getAllBom());
		request.setAttribute("list", rs.retVal);
		request.setAttribute("mrpFrom", mrpFrom);
		request.setAttribute("selCondition", selCondition);// 设置选中的条件
		request.setAttribute("pageBar", this.pageBar(rs, request));// 返回分页信息
		forward = mapping.findForward("chooseOrder");// 设定跳转
	}

	/**
	 * 物料详细查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	private void bomSelPro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] trackNos = request.getParameterValues("trackNo");
		String[] counts = request.getParameterValues("count");
		String[] chooses=request.getParameterValues("choose");
		String[] bomNos=request.getParameterValues("bomNo");
		String mrpFrom=request.getParameter("mrpFrom");
		request.setAttribute("mrpFrom", mrpFrom);

		ArrayList list=new ArrayList();
		for (int i = 0; i < trackNos.length; i++) { 
			if(chooses[i].equals("false")||trackNos[i].length()==0)continue;
			Result rs=this.getOrdersBom(trackNos[i],mrpFrom,bomNos[i]);
			ArrayList temp=(ArrayList)rs.retVal;
			boolean flag=true;
			HashMap map=null;
			
			if(temp.size()==0){
				flag=false;
			}else{
				map=(HashMap)temp.get(0);
				if(map.get("bomdetId")==null||(map.get("bomdetId")!=null&&map.get("bomdetId").toString().length()==0)){
					flag=false;
				}
			}
			
			if(!flag){
				String errorMessage="";
				if(map!=null){
					errorMessage = GlobalsTool.getMessage(request,
					"mrp.error.bomSelPro")
					 +map.get("billNo")+";"+GlobalsTool.getMessage(request,"iniGoods.lb.goodsName")
					 +":"+map.get("goodsFullName");
				}else{
					errorMessage = GlobalsTool.getMessage(request,"common.msg.RET_ID_NO_VALUE_ERROR");
				}
				EchoMessage.error().add(errorMessage).setBackUrl("/MrpBP.do?method=orderSel&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
				forward = getForward(request, mapping, "message");
				return;
			}
				
			map.put("count", counts[i]);
			map.put("amount", Double.parseDouble(map.get("price").toString())*Double.parseDouble(counts[i]));
		    list.add(map);
			
			rs = this.getSelProSon(map.get("bomdetId").toString(),map.get("trackNo").toString());
			if(rs.getRetCode()==ErrorCanst.MRP_ERROR_CODE){
				EchoMessage.error().add(getMessage(request, "mrp.msg.orderSelError"))
                        .setBackUrl("/MrpBP.do?method=orderSel").setAlertRequest(request);
				forward = getForward(request, mapping, "message");
				return ;
			}
			list.addAll((ArrayList)rs.retVal);
		}	
		
		request.setAttribute("list", list);
		forward = mapping.findForward("matereilAnalyse");
	}

	/**
	 * 获得子级物料信息
	 * 
	 * @param id
	 * @param cols
	 * @return
	 */
	private String[] getGoodsBomDet(String id, String cols) {
		String[] result = null;// 要返回的结果

		// 查询
		StringBuffer sql = new StringBuffer();
		sql.append("select bd.id,g.goodsnumber,g.goodsFullName,bd.goodscode,bd.factqty as qty,bd.price");
		sql.append(cols);
		sql.append(" from tblbomdet bd,tblgoods g where bd.id='" + id + "'");
		sql.append(" and g.classcode=bd.goodscode");
		sql.append(" union select b.id,g.goodsnumber,g.goodsFullName,b.goodscode,b.qty,b.price");
		sql.append(cols);
		sql.append(" from tblbom b,tblgoods g where b.id='" + id + "'");
		sql.append(" and g.classcode=b.goodscode");
		result = (String[]) ((ArrayList) new MrpBPMgt().querySql(sql.toString()).retVal)
				.get(0);
		return result;
	}
	/**
	 * 获得半成品或者成品子级物料信息
	 * 
	 * @param id
	 * @param cols
	 * @return
	 */
	private ArrayList getGoodsSon(String bomDetId,boolean isFirst,String qty) {
		ArrayList result = null;// 要返回的结果
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				propStr+=",bd."+propBean.getPropName();				 
				bomCond+=" and a."+propBean.getPropName()+"=b."+propBean.getPropName();
			}
		}		
		 
		if(BaseEnv.version==3){
			propStr+=",bd.colorName";
		}
		StringBuffer sql = new StringBuffer();		
		if(isFirst){//如果是第一级商品，那么传递的ID是主表的ID，否则就是明细表的ID
			sql.append("select 'false' as isPlace,bd.id as bomDetId,bd.goodsCode,bd.factQty as qty,bd.factQty*"+qty+" as needQty,bd.price,bd.MaterielType"+
					",(case when (select COUNT(0) from tblBOMDetail where f_ref=bd.id)>0 then 'exist' else '' end) as replace "+
					",cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+isnull(color,'')+'color')) as bigint) as goodPropHash "+propStr+
					" from tblbomdet bd,tblgoods g where g.classcode=bd.goodscode and f_ref='"+bomDetId+"' order by bd.detOrderNo");	
		}else{//成品或者半成品就通过明细查询这个商品的物料清单
			sql.append("select 'false' as isPlace,bd.id as bomDetId,bd.goodsCode,bd.factQty as qty,bd.factQty*"+qty+" as needQty,bd.price,bd.MaterielType"+
					",(case when (select COUNT(0) from tblBOMDetail where f_ref=bd.id)>0 then 'exist' else '' end) as replace "+
					",cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+isnull(color,'')+'color')) as bigint) as goodPropHash "+propStr+
					" from tblbomdet bd,tblgoods g where g.classcode=bd.goodscode and f_ref=(select  top 1  a.id from tblBom a,tblBomDet b where  b.id='"+bomDetId+"' and  a.goodsCode=b.goodsCode "+
					bomCond+" and a.workFlowNodeName='finish') order by bd.detOrderNo");			
		}
		result =(ArrayList) new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0).retVal;
		return result;
	}
	
	/**
	 * 查询替换料表中相关id的详细信息
	 * 
	 * @param id
	 * @param cols
	 * @return
	 */
	private ArrayList getRePlaceInfo(String ids) {
		ArrayList result = null;// 要返回的结果
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				propStr+=",bd."+propBean.getPropName();				 
				bomCond+=" and a."+propBean.getPropName()+"=b."+propBean.getPropName();
			}
		}		
		 
		if(BaseEnv.version==3){
			propStr+=",bd.colorName";
		}

		StringBuffer sql=new StringBuffer("");
		sql.append("select 'true' as isPlace,bd.id as bomDetId,bd.goodsCode,bd.qty,bd.price,bd.MaterielType "+propStr+
					" from tblbomdetail bd,tblgoods g where g.classcode=bd.goodscode and bd.id in ("+ids+")");			

		BaseEnv.log.debug(sql.toString());
		result =(ArrayList) new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0).retVal;
		return result;
	}
	
	private ArrayList getPropInfo(String bomDetId,boolean isFirst,boolean isPlace) {
		ArrayList result = new ArrayList();// 要返回的结果
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				propStr+=",a."+propBean.getPropName();	
			}
		}	
		String colorStr="";
		if(BaseEnv.version==3){
			colorStr=",colorName";
		}
		if(propStr.length()>0){
			propStr=propStr.substring(1,propStr.length());
			StringBuffer sql = new StringBuffer();		
			if(isFirst){ 
				sql.append("select "+propStr+colorStr+" from tblbom a where a.id='"+bomDetId+"'");	
			}else{ 
				if(isPlace){
					sql.append("select "+propStr+colorStr+" from tblbomdetail a where a.id='"+bomDetId+"'");
				}else{
					sql.append("select "+propStr+colorStr+" from tblbomdet a where a.id='"+bomDetId+"'");
				}
			}
			result =(ArrayList) new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0).retVal;
		}
		return result;
	}
	
	private void setPropDisToMap(String bomDetId,boolean isFirst,HashMap map,String isPlace) {
		ArrayList result = new ArrayList();// 要返回的结果
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")
						||propBean.getPropName().equals("Design")||propBean.getPropName().equals("color")){
					propStr+=",a."+propBean.getPropName()+" as "+propBean.getPropName()+"Val";
					if(BaseEnv.version==8){//工贸版
						propStr+=",((select propItemName from tblGoodsPropItem where propItemID=a."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"')) as "+propBean.getPropName();
					}else{//布匹版
						if(propBean.getPropName().equals("color")&&BaseEnv.systemSet.get("Usepubliccolor").getSetting().equals("false")){//如果颜色值设置不是公共颜色
							propStr+=",(select propItemName from tblGoodsOfProp ,tblGoodsOfPropDet  where  tblGoodsOfProp.id=tblGoodsOfPropDet.f_ref and tblGoodsOfProp.goodsCode=a.goodsCode and tblGoodsOfPropDet.propItemID=a."+propBean.getPropName()+" and tblGoodsOfPropDet.PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}else{
							propStr+=",(select propItemName from tblGoodsOfPropDet where propItemID=a."+propBean.getPropName()+" and PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}
					}
				}else{
					propStr+=",a."+propBean.getPropName();
				}
			}
		}	
		
		StringBuffer sql = new StringBuffer();		
		if(isFirst){ 
			sql.append("select b.goodsSpec,c.unitName"+propStr+" from tblBom a left join tblGoods b on a.goodsCode=b.classCode left join tblUnit c on b.BaseUnit=c.id  where a.id='"+bomDetId+"'");	
		}else{ 
			if("true".equals(isPlace)){
				sql.append("select b.goodsSpec,c.unitName"+propStr+" from tblBomDetail a left join tblGoods b on a.goodsCode=b.classCode left join tblUnit c on b.BaseUnit=c.id where a.id='"+bomDetId+"'");
			}else{
				sql.append("select b.goodsSpec,c.unitName"+propStr+" from tblBomDet a left join tblGoods b on a.goodsCode=b.classCode left join tblUnit c on b.BaseUnit=c.id where a.id='"+bomDetId+"'");
			}
		}

		result =(ArrayList) new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0).retVal;

		
		if(result.size()>0){
			HashMap propMap=(HashMap)result.get(0);
			for(int i=0;i<list.size();i++){
				GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
				if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
					if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")
							||propBean.getPropName().equals("Design")||propBean.getPropName().equals("color")){
						map.put(propBean.getPropName()+"Val", propMap.get(propBean.getPropName()+"Val"));
					}
					map.put(propBean.getPropName(), propMap.get(propBean.getPropName()));
				}
			}
			map.put("goodsSpec", propMap.get("goodsSpec"));
			map.put("unitName", propMap.get("unitName"));
		}
		
		return;
	}

	/**
	 * 根据tblbom表的id查询id，商品
	 * 
	 * @param id
	 *            tblbom中的id
	 * @param fields启用的商品属性字段
	 * @return
	 */
	private String[] getGoodsBom(String id, String cols) {

		String[] result = null;// 要返回的结果
		cols=cols.replaceAll("''", "'");

		// 查询
		StringBuffer sql = new StringBuffer();
		sql
				.append("select b.id,g.goodsnumber,g.goodsFullName,b.goodscode,b.qty,b.price,g.GoodsSpec");
		sql.append(cols);
		sql.append(" from tblbom b,tblgoods g where b.id='" + id + "'");
		sql.append(" and g.classcode=b.goodscode");
		result = (String[]) ((ArrayList) new MrpBPMgt().querySql(sql.toString()).retVal)
				.get(0);
		return result;
	}

	/**
	 * 根据商品classcode获得其数量和单价
	 * 
	 * @param goodscode
	 *            商品代码
	 * @param fields
	 *            商品启用的属性字段
	 * @param fieldVals
	 *            商品启用属性字段对应的值
	 * @return 返回数量和单价的集合
	 * @throws Exception
	 */

	private String[] getGoodsBom(String goodscode, String[] fields,
			String[] fieldVals) throws Exception {
		String[] fields2 = getFields("tblbom", 0);// +++++++++++++++++++++++++++++++++++

		goodscode = goodscode.trim();
		StringBuffer sql = new StringBuffer();// 记录要执行的sql语句
		StringBuffer where = new StringBuffer();// 记录商品属性条件，意思是以商品属性的值来查询存在的记录
		for (int i = 0; i < fields2.length; i++) {// 根据启用的属性来设置条件
			if (fieldVals[i] == null) {
				where.append(" and b." + fields[i] + " =''");// 其中的b指tblbom的别名，与下面select语句对应
			} else {
				where.append(" and b." + fields[i] + "='" + fieldVals[i] + "'");
			}
		}
		sql.append(" select top 1 b.qty,b.price,b.id from tblbom b");
		sql.append(" where b.goodscode='" + goodscode + "'");
		sql.append(where+" and b.workFlowNodeName='finish' order by VersionNO desc");
		return (String[]) ((ArrayList) new MrpBPMgt().querySql(sql.toString()).retVal)
				.get(0);// 查询并返回
	}

	/**
	 * 展开物料查询子级
	 * @param bomId
	 * @param trackNo
	 * @return
	 */
	private Result getSelProSon(String bomId,String trackNo) {
		String propStr="";
		String propStrs="";
		String propCond="";
		String propCond2="";
		for(int i=0;i<BaseEnv.attList.size();i++){
			GoodsAttributeBean propBean=BaseEnv.attList.get(i) ;
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){
				propStr+=",bd."+propBean.getPropName();
				
				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("Design")
						||propBean.getPropName().equals("color")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")){
					if(BaseEnv.version==8){//工贸版
						propStrs+=",(select propItemName from tblGoodsPropItem where propItemID=bd."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"') as "+propBean.getPropName();
					}else{//布匹版
						if(propBean.getPropName().equals("color")&&BaseEnv.systemSet.get("Usepubliccolor").getSetting().equals("false")){//如果颜色值设置不是公共颜色
							propStrs+=",(select propItemName from tblGoodsOfProp a,tblGoodsOfPropDet b where  a.id=b.f_ref and a.goodsCode=bd.goodsCode and b.propItemID=bd."+propBean.getPropName()+" and b.PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}else{
							propStrs+=",(select propItemName from tblGoodsOfPropDet where propItemID=bd."+propBean.getPropName()+" and PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}
					}
				}else{
					propStrs+=",bd."+propBean.getPropName();
				}
				propCond+=" and c."+propBean.getPropName()+"=b."+propBean.getPropName();
				propCond2+=" and d."+propBean.getPropName()+"=b."+propBean.getPropName();
			}
		}
		
		StringBuffer sql = null;// 要执行的sql语句

		sql = new StringBuffer(// 执行子查询，～符号用以显示子级的深度///////////
				"declare @e varchar(8000) declare @prop varchar(500) ");
		sql.append(" if exists(select * from sysobjects where name='bomtmp') drop table bomtmp ");
		sql.append(" set @prop='" + propStr + "'");
		sql.append(" set @e='declare @l int declare @ch varchar(50)");
		sql.append(" set @ch=''~'' set @l=0 ");
		sql.append(" select @ch as ch,''false'' isPlace,'''' as mainPath,bd.id as bomdetId,bd.goodsCode as goodsCode,b.goodscode as pgd,g.goodsnumber as goodsnumber");
		sql.append(" ,g.goodsFullname as gname,g.GoodsSpec as goodsSpec,bd.factqty as qty,bd.price as price");
		sql.append(" ,@l as l ,bd.goodscode as path,bd.MaterielAttribute,f.unitName");
		sql.append(",0 as changeCount,bd.BOMId");
		sql.append("'+@prop+' into bomtmp ");
		sql.append(" from tblbom b left join tblbomdet bd on b.id=bd.f_ref left join tblgoods g on bd.goodscode=g.classcode left join  tblUnit f on g.BaseUnit=f.id");
		sql.append(" where b.id =''" + bomId + "''");
		sql.append(" alter table bomtmp alter column path varchar(100) alter table bomtmp alter column mainPath varchar(100) ");
		
		sql.append(" update bomtmp set ch=ch");
		sql.append(" while @@rowcount>0 begin set @l=@l+1 set @ch=@ch+''~''");
		sql.append(" insert into bomtmp select @ch,''false'' isPlace,'''' as mainPath,bd.id as bomdetId,bd.goodsCode,b.goodscode,g.goodsnumber");
		sql.append(" ,g.goodsfullname,g.GoodsSpec,bd.factqty as qty,bd.price,@l,c.path+bd.goodscode,bd.MaterielAttribute,f.unitName");
		sql.append(",0 as changeCount,bd.BOMId");
		sql.append("'+@prop+'");
		sql.append(" from tblbom b left join tblbomdet bd on b.id=bd.f_ref left join bomtmp c on 1=1 left join tblgoods g on 1=1 left join tblUnit f on g.BaseUnit=f.id ");
		sql.append("where b.workFlowNodeName=''finish'' and b.id=c.BOMId and c.goodsCode=b.goodscode "+propCond+" and bd.goodscode=g.classcode  and c.l=@l-1");
			
		sql.append(" end ' exec(@e)");
		Result result = new MrpBPMgt().execSql(sql.toString());
		if(result.getRetCode()==ErrorCanst.MRP_ERROR_CODE){
			return result;
		}
		
		sql = new StringBuffer(
				"select ch+goodsnumber as goodsNumber,path,isPlace,mainPath,gname as goodsFullName,goodsSpec,qty,price,qty*price as amount,bomdetId,goodsCode,MaterielAttribute,unitName,changeCount "+ propStrs);

		sql.append(" from bomtmp as bd order by path,mainPath,isPlace");
		rs =new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0) ;
		new MrpBPMgt().execSql(" drop table bomtmp");
		return rs;

	}
	/**
	 * 查询当前成品的所有物料，属性及其数量
	 * @param bomId
	 * @param trackNo
	 * @return
	 */
	private Result getSonMap(String bomId,String trackNo,String startDate) {		
		StringBuffer sql = null;// 要执行的sql语句

		sql = new StringBuffer(// 执行子查询，～符号用以显示子级的深度///////////
				"declare @e varchar(8000) declare @prop varchar(500) ");
		sql.append(" if exists(select * from sysobjects where name='bomtmp') begin delete bomtmp ; end ");
		sql.append(" set @e='declare @l int declare @ch varchar(50)");
		sql.append(" set @ch=''~'' set @l=0 ");
		
		//插入一级物料
		sql.append(" insert into bomtmp ");
		sql.append(" select @ch as ch,@l as l,bd.id as bomdetId,bd.goodsCode as goodsCode,bd.factqty as unitNum,bd.factqty as qty,bd.price as price,bd.MaterielType as MaterielType");
		sql.append(" ,(case when len(bd.BomId)>0 and (select COUNT(0) from tblBOMDet where tblBOMDet.f_ref=bd.BomId)=0 then '''' else bd.BomId end) as BOMId,''"+trackNo+"'' as mainPath,''"+trackNo+"''+(case len(cast(ROW_NUMBER() over(order by bd.detOrderNo) as varchar(10))) when 1 then ''000'' when 2 then ''00'' when 3 then ''0'' else '''' end)+cast(ROW_NUMBER() over(order by bd.detOrderNo) as varchar(10)) as path");
		sql.append(",CONVERT(varchar(100),Dateadd(d,-1*isnull((case when g.ProducePeriod=0 then 1 else g.ProducePeriod end),1),''"+startDate+"''),23) as startDate,CONVERT(varchar(100),Dateadd(d,-1,''"+startDate+"''),23) as submitDate");
		sql.append(",bd.color,''"+trackNo+"'' as trackNo");
		sql.append(",(case when (select COUNT(0) from tblBOMDetail WHERE f_ref=bd.id)>0 then ''exist'' else '''' end) as replace");
		sql.append(" from tblbom b left join tblbomdet bd on b.id=bd.f_ref left join tblGoods g on bd.goodsCode=g.classCode where b.id =''" + bomId + "'''");		
		sql.append(" exec(@e)");

		BaseEnv.log.debug("展示所有级别物料语句："+sql.toString());
		Result result = new MrpBPMgt().execSql(sql.toString());
		if(result.getRetCode()==ErrorCanst.MRP_ERROR_CODE){
			return result;
		}
		
		rs =mgt.getSonMap();
		return rs;

	}
	
//	 获得子物料
	private Result getSon(String bomId,String trackNo,String startDate) {
		String propStr="";
		String propStrs="";
		String propCond="";
		String propCond2="";
		String propSens="";
		for(int i=0;i<BaseEnv.attList.size();i++){
			GoodsAttributeBean propBean=BaseEnv.attList.get(i) ;
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){
				propStr+=",bd."+propBean.getPropName();
				
				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("Design")
						||propBean.getPropName().equals("color")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")){
					if(BaseEnv.version==8){//工贸版
						propStrs+=",(select propItemName from tblGoodsPropItem where propItemID=bd."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"') as "+propBean.getPropName();
					}else{//布匹版
						if(propBean.getPropName().equals("color")&&BaseEnv.systemSet.get("Usepubliccolor").getSetting().equals("false")){//如果颜色值设置不是公共颜色
							propStrs+=",(select propItemName from tblGoodsOfProp a,tblGoodsOfPropDet b where  a.id=b.f_ref and a.goodsCode=bd.goodsCode and b.propItemID=bd."+propBean.getPropName()+" and b.PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}else{
							propStrs+=",(select propItemName from tblGoodsOfPropDet where propItemID=bd."+propBean.getPropName()+" and PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}
					}
				}else{
					propStrs+=",bd."+propBean.getPropName();
				}
				propCond+=" and c."+propBean.getPropName()+"=b."+propBean.getPropName();
				propCond2+=" and d."+propBean.getPropName()+"=b."+propBean.getPropName();
			}
		}
		
		StringBuffer sql = null;// 要执行的sql语句
		
		sql = new StringBuffer(// 执行子查询，～符号用以显示子级的深度///////////
				"declare @e varchar(8000) declare @prop varchar(500) ");
		sql.append(" if exists(select * from sysobjects where name='bomtmp') drop table bomtmp ");		
		sql.append(" set @prop='" + propStr + "'");		
		sql.append(" set @e='declare @l int declare @ch varchar(50)");
		sql.append(" declare @rowCount int declare @bomdetId varchar(32)");
		sql.append(" set @ch=''~'' set @l=0 ");
		sql.append(" select @ch as ch,''false'' isPlace,'''' as mainPath,bd.id as bomdetId,bd.goodsCode as goodsCode,b.goodscode as pgd,g.goodsnumber as goodsnumber");
		sql.append(" ,g.goodsFullname as gname,g.GoodsSpec as goodsSpec,bd.factqty as qty,bd.price as price");
		sql.append(" ,@l as l ,(case len(cast(ROW_NUMBER() over(order by bd.detOrderNo) as varchar(10))) when 1 then ''000'' when 2 then ''00'' when 3 then ''0'' else '''' end)+cast(ROW_NUMBER() over(order by bd.detOrderNo) as varchar(10)) as path,bd.MaterielAttribute,f.unitName");
		sql.append(",(select count(0) from tblBomDetail where f_ref=bd.id) as changeCount,bd.BOMId,''false'' as cb");
		sql.append(",CONVERT(varchar(100),Dateadd(d,-1*isnull((case when g.ProducePeriod=0 then 1 else g.ProducePeriod end),1),''"+startDate+"''),23) as startDate,CONVERT(varchar(100),Dateadd(d,-1,''"+startDate+"''),23) as submitDate");
		sql.append("'+@prop+' into bomtmp ");
		sql.append(" from tblbom b left join tblbomdet bd on b.id=bd.f_ref left join tblGoods g on bd.goodscode=g.classcode left join  tblUnit f on g.BaseUnit=f.id");
		sql.append(" where b.id =''" + bomId + "''");
		sql.append(" set @rowCount=@@rowcount ");
		//由于后面这些字段的长度将增大
		sql.append(" alter table bomtmp alter column path varchar(200) ");
		sql.append(" alter table bomtmp alter column mainPath varchar(200)");		
		//增加替换料 物料本身		
		sql.append("insert into bomtmp select @ch as ch,''true'' isPlace,a.path as mainPath,a.bomdetId,a.goodsCode as goodsCode,a.pgd,a.goodsnumber ,a.gname,a.goodsSpec,a.qty,a.price");
		sql.append(",@l as l ,a.path+''001'' as path,a.MaterielAttribute,a.unitName,0,a.BOMId,''true'' as cb,a.startDate,a.submitDate'+(replace(@prop,'bd.','a.'))+' from bomtmp a where changeCount>0");
		
		//物料替换料
		sql.append("declare cur_replace cursor local for select bomdetId from bomtmp where l=@l and changeCount>0 ");
		sql.append("	open cur_replace ");
		sql.append("	fetch next from cur_replace into @bomdetId ");
		sql.append("	while(@@FETCH_STATUS=0) ");
		sql.append("		begin   ");
		sql.append(" insert into bomtmp select @ch as ch,''true'' isPlace,c.path as mainPath,bd.id as bomdetId,bd.goodsCode as goodsCode,c.pgd as pgd,g.goodsnumber+'' 替'' as goodsnumber");
		sql.append(" ,g.goodsFullname as gname,g.GoodsSpec as goodsSpec,bd.qty as qty,bd.price as price");
		sql.append(" ,@l as l,c.path+(case len(cast(1+ROW_NUMBER() over(order by bd.detOrderNo) AS varchar(10))) when 1 then ''00'' when 2 then ''0'' else '''' end)+cast(1+ROW_NUMBER() over(order by bd.detOrderNo) as varchar(10)) as path,bd.MaterielAttribute,f.unitName,0,bd.BOMId,''false'' as cb");
		sql.append(",CONVERT(varchar(100),Dateadd(d,-1*isnull((case when g.ProducePeriod=0 then 1 else g.ProducePeriod end),1),''"+startDate+"''),23) as startDate,CONVERT(varchar(100),Dateadd(d,-1,''"+startDate+"''),23) as submitDate");
		sql.append("'+@prop+'");
		sql.append(" from tblbomdetail bd left join bomtmp c on bd.f_ref=c.bomdetId left join tblgoods g on bd.GoodsCode=g.classCode left join tblUnit f on g.BaseUnit=f.id where c.bomdetId=@bomdetId and c.l=@l and c.changeCount>0 ");
		sql.append("fetch next from cur_replace into @bomdetId end close cur_replace deallocate cur_replace");
		
		
		sql.append(" update bomtmp set ch=ch");
		sql.append(" while @rowCount>0 begin set @l=@l+1 set @ch=@ch+''~''");
		sql.append(" insert into bomtmp select @ch,''false'','''' as mainPath,bd.id as bomdetId,bd.goodsCode,b.goodscode,g.goodsnumber");
		sql.append(" ,g.goodsfullname,g.GoodsSpec,bd.factqty as qty,bd.price,@l,c.path+(case len(cast(ROW_NUMBER() over(order by bd.detOrderNo) AS varchar(10))) when 1 then ''00'' when 2 then ''0'' else '''' end)+cast(ROW_NUMBER() over(order by bd.detOrderNo) as varchar(10)) as path,bd.MaterielAttribute,f.unitName");
		sql.append(",(select count(0) from tblBomDetail where f_ref=bd.id) as changeCount,bd.BOMId,''false'' as cb");
		sql.append(",CONVERT(varchar(100),Dateadd(d,-1*isnull((case when g.ProducePeriod=0 then 1 else g.ProducePeriod end),1),c.startDate),23) as startDate,CONVERT(varchar(100),Dateadd(d,-1,c.startDate),23) as submitDate");
		sql.append("'+@prop+'");
		sql.append(" from tblbom b left join tblbomdet bd on b.id=bd.f_ref left join bomtmp c on 1=1 left join tblgoods g on 1=1 left join tblUnit f on g.BaseUnit=f.id ");
		sql.append("where b.workFlowNodeName=''finish'' and b.id=c.BOMId and c.goodsCode=b.goodscode "+propCond+" and bd.goodscode=g.classcode  and c.l=@l-1 and c.changeCount=0");
		
		sql.append(" set @rowCount=@@rowcount ");
		//将替换料数量大于0的物料设置成不是替换料
		sql.append(" update bomtmp set isPlace=''false'' where changeCount>0 and bomtmp.l=@l");
		
		//查询第一级商品中是否有子级，如果有同时增加子级的替换料 物料本身
		 		
		sql.append(" insert into bomtmp select @ch,''true'' isPlace,bd.path as mainPath,bd.bomdetId,bd.goodsCode,bd.pgd,g.goodsnumber ,g.goodsfullname,g.GoodsSpec, ");
		sql.append(" bd.qty,bd.price,@l,bd.path+''001'',bd.MaterielAttribute,f.unitName,0,bd.BOMId,''true'' as cb");
		sql.append(",bd.startDate,bd.submitDate");
		sql.append("'+@prop+'");
		sql.append(" from  bomtmp bd left join tblgoods g on bd.goodsCode=g.classCode left join tblUnit f on g.BaseUnit=f.id where bd.l=@l and bd.changeCount>0");
				
		//增加替换料
//		物料替换料
		sql.append("declare cur_replace cursor local for select bomdetId from bomtmp where l=@l and changeCount>0 ");
		sql.append("	open cur_replace ");
		sql.append("	fetch next from cur_replace into @bomdetId ");
		sql.append("	while(@@FETCH_STATUS=0) ");
		sql.append("		begin   ");
		sql.append(" insert into bomtmp select @ch,''true'' isPlace,c.path as mainPath,bd.id as bomdetId,bd.goodsCode,c.pgd,g.goodsnumber+'' 替''");
		sql.append(" ,g.goodsfullname,g.GoodsSpec,bd.qty,bd.price,@l,c.path+(case len(cast(1+ROW_NUMBER() over(order by  bd.detOrderNo) AS varchar(10))) when 1 then ''00'' when 2 then ''0'' else '''' end)+cast(1+ROW_NUMBER() over(order by bd.detOrderNo) as varchar(10)),bd.MaterielAttribute,f.unitName,0,bd.BOMId,''false'' as cb");
		sql.append(",c.startDate,c.submitDate");
		sql.append("'+@prop+'");
		sql.append(" from tblbomdetail bd left join bomtmp c on bd.f_ref=c.bomdetId left join tblgoods g on bd.GoodsCode=g.classCode left join tblUnit f on g.BaseUnit=f.id where c.bomdetId=@bomdetId and c.l=@l and c.changeCount>0");
		sql.append("fetch next from cur_replace into @bomdetId end close cur_replace deallocate cur_replace");
		
		sql.append(" end ' exec(@e)");

		Result result = new MrpBPMgt().execSql(sql.toString());
		if(result.getRetCode()==ErrorCanst.MRP_ERROR_CODE){
			return result;
		}
	
		sql = new StringBuffer(
				"select ch+goodsnumber as goodsNumber,'"+trackNo+"'+path as path,isPlace,(case len(mainPath) when 0 then '' else '"+trackNo+"'+mainPath end) as mainPath,gname as goodsFullName,goodsSpec,qty,price,qty*price as amount,bomdetId,goodsCode,MaterielAttribute,unitName,changeCount,cb,startDate,submitDate "+ propStrs);

		sql.append(" from bomtmp as bd order by path");
		rs =new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0) ;
		new MrpBPMgt().execSql(" drop table bomtmp");
		return rs;

	}

	// 将查询结果加入list
	private void addList(List list, Result rs) {
		ArrayList al = (ArrayList) rs.retVal;// 取出结果集的集合。
		String[] row = null;// 用于记录一条集合中的结果

		for (int j = 0; j < al.size(); j++) {// 将记录加入list中
			row = (String[]) al.get(j);
			list.add(row);
		}
	}
	private void savePrintData2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		String[] goodsNumbers=request.getParameterValues("goodsNumberz");
		String[] goodsNames=request.getParameterValues("goodsFullName");
		String[] goodsSpecs=request.getParameterValues("goodsSpec");
		String[] unitName=request.getParameterValues("unitName");
		String[] materName=request.getParameterValues("materName");
		String stockName=request.getParameter("Stock");
		String[] unitNums=request.getParameterValues("unitNum");
		String[] BCounts=request.getParameterValues("BCount");
		String[] qtys=request.getParameterValues("qty");
		String[] counts=request.getParameterValues("count");	
		String[] stores=request.getParameterValues("store");
		String[] appings=request.getParameterValues("apping");	
		String[] storeings=request.getParameterValues("storeing");	
		String[] productings=request.getParameterValues("producting");	
		String[] neededs=request.getParameterValues("needed");	
		String[] batchNo=request.getParameterValues("BatchNo");
		String[] inch=request.getParameterValues("Inch");
		String[] Hue=request.getParameterValues("Hue");
		String[] yearNO=request.getParameterValues("yearNO");
		
		ArrayList attList=BaseEnv.attList;
		ArrayList list=new ArrayList();
		for (int i = 0; goodsNumbers!=null&&i < goodsNumbers.length; i++) { 
			 HashMap map=new HashMap();
			 map.put("goodsNumber", goodsNumbers[i]);
			 map.put("goodsFullName", goodsNames[i]);
			 map.put("goodsSpec", goodsSpecs[i]);
			 map.put("unitName", unitName[i]);
			 map.put("materName", materName[i]);
			 map.put("stockName", stockName);
			 map.put("unitNum", unitNums[i]);
			 map.put("BCount", BCounts[i]);
			 map.put("qty", qtys[i]);
			 map.put("count", counts[i]);
			 map.put("store", stores[i]);
			 map.put("apping", appings[i]);
			 map.put("storeing", storeings[i]);
			 map.put("producting", productings[i]);
			 map.put("needed", neededs[i]);
			 
			 if(batchNo!=null)map.put("BatchNo", batchNo[i]);
			 if(inch!=null) map.put("Inch", inch[i]);
			 if(Hue!=null)map.put("Hue", Hue[i]);
			 if(yearNO!=null)map.put("yearNO", yearNO[i]);
			 list.add(map);
		}	
		request.getSession().setAttribute("mrpList", list);
	}
	private void savePrintData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		String[] goodsNumbers=request.getParameterValues("goodsNumber");
		String[] goodsNames=request.getParameterValues("goodsFullName");
		String[] billNos=request.getParameterValues("trackNo");
		String[] goodsSpecs=request.getParameterValues("goodsSpec");
		String[] empFullNames=request.getParameterValues("empFullName");
		String[] prices=request.getParameterValues("price");
		String[] stockNames=request.getParameterValues("stockName");
		String[] qtys=request.getParameterValues("qty");
		String[] counts=request.getParameterValues("count");	
		String[] stores=request.getParameterValues("store");
		String[] storeings=request.getParameterValues("storeing");	
		String[] productings=request.getParameterValues("producting");
		String[] useds=request.getParameterValues("used");	
		String[] storeingSelfs=request.getParameterValues("storeingSelf");
		String[] productingSelfs=request.getParameterValues("productingSelf");	
		String[] usedSelfs=request.getParameterValues("usedSelf");	
		String[] orderSelfs=request.getParameterValues("orderSelf");	
		String[] neededs=request.getParameterValues("needed");	
		String[] totalPrices=request.getParameterValues("totalPrice");	
		String[] needQty=request.getParameterValues("");
		String[] batchNo=request.getParameterValues("BatchNo");
		String[] Hue=request.getParameterValues("Hue");
		String[] inch=request.getParameterValues("Inch");
		String[] yearNO=request.getParameterValues("yearNO");
		String[] unitName=request.getParameterValues("unitName");
		String[] materName=request.getParameterValues("materName");
		
		String mrpFrom=request.getParameter("mrpFrom");
		ArrayList attList=BaseEnv.attList;
		ArrayList list=new ArrayList();
		for (int i = 0; goodsNumbers!=null&&i < goodsNumbers.length; i++) { 
			 HashMap map=new HashMap();
			 map.put("goodsNumber", goodsNumbers[i]);
			 map.put("goodsFullName", goodsNames[i]);
			 map.put("billNo", billNos[i]);
			 map.put("goodsSpec", goodsSpecs[i]);
			 map.put("empFullName", empFullNames[i]);
			 map.put("price", prices[i]);
			 map.put("stockName", stockNames[i]);
			 map.put("qty", qtys[i]);
			 map.put("count", counts[i]);
			 map.put("store", stores[i]);
			 map.put("storeing", storeings[i]);
			 map.put("producting", productings[i]);
			 map.put("used", useds[i]);
			 map.put("storeingSelf", storeingSelfs[i]);
			 map.put("productingSelf", productingSelfs[i]);
			 map.put("usedSelf", usedSelfs[i]);
			 map.put("orderSelf", orderSelfs[i]);
			 map.put("needed", neededs[i]);
			 map.put("totalPrice", totalPrices[i]);
			 map.put("unitName", unitName[i]);
			 map.put("materName", materName[i]);
			 if(batchNo!=null)map.put("BatchNo", batchNo[i]);
			 if(Hue!=null)map.put("Hue", Hue[i]);
			 if(inch!=null) map.put("Inch", inch[i]);
			 if(yearNO!=null)map.put("yearNO", yearNO[i]);
			 list.add(map);
		}	
		request.getSession().setAttribute("mrpList", list);
		request.getSession().setAttribute("mrpFromPrint", mrpFrom);
	}

	// 物料需求计算（MRP运算）
	private void bomDemand(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		String[] trackNos=request.getParameterValues("trackNo");
		String[] chooses = request.getParameterValues("choose");
		String[] qtys = request.getParameterValues("count");
		String[] stockCodes=request.getParameterValues("stockCode");
		String[] stockNames=request.getParameterValues("stockName");		
		String[] needQty=request.getParameterValues("");
		String[] bomNos=request.getParameterValues("bomNo");
		String[] cbs=request.getParameterValues("cb");
		String mrpFrom=request.getParameter("mrpFrom");
		String operType=request.getParameter("opeType");
		if("BackMrp".equals(operType)){
			String ProductMRPs=request.getParameter("ProductMRPIds");
			Result rss=mgt.selectProductMRP(ProductMRPs);
			if(rss.retCode==ErrorCanst.DEFAULT_SUCCESS){
				ArrayList list=(ArrayList)rss.retVal;
			    trackNos=new String[list.size()];
			    chooses=new String[list.size()];
			    qtys=new String[list.size()];
			    bomNos=new String[list.size()];
			    for(int i=0;i<list.size();i++){
			       String []str=(String[])list.get(i);
			        trackNos[i]=str[0];
			        chooses[i]="true";        	
			        bomNos[i]=str[1];
			        mrpFrom=str[2];
			        qtys[i]=str[3];        	
			    }
			}
		}
		
		request.setAttribute("mrpFrom", mrpFrom);		
		request.setAttribute("operType", operType);
		
		String StockCode1=request.getParameter("StockCode1");
		String Stock=request.getParameter("Stock");
		
		ArrayList list=new ArrayList();
		for (int i = 0; i < chooses.length; i++) { 
			if(chooses[i].equals("false")||trackNos[i].length()==0)continue;
			Result rs=this.getOrdersBom(trackNos[i],mrpFrom,bomNos[i]);
			HashMap map=(HashMap)((ArrayList)rs.retVal).get(0);
			//当没有BOM清单时，给出提示
			if((map.get("bomdetId")==null||(map.get("bomdetId")!=null&&map.get("bomdetId").toString().length()==0))){
				String errorMessage = GlobalsTool.getMessage(request,
				"mrp.error.bomSelPro")
				 +map.get("trackNo")+";"+GlobalsTool.getMessage(request,"iniGoods.lb.goodsName")
				 +":"+map.get("goodsFullName");
				EchoMessage.error().add(errorMessage).setBackUrl("/MrpBP.do?method=orderSel&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
				forward = getForward(request, mapping, "message");
				return;
			}
			list.add(map);	
			map.put("count", qtys[i]);//毛需求量
			map.put("qty", 1);//单位数量
			
			//原材料的数量信息
			rs = getSon(map.get("bomdetId").toString(),map.get("trackNo").toString(),map.get("startDate").toString());
			if(rs.getRetCode()==ErrorCanst.MRP_ERROR_CODE){
				EchoMessage.error().add(getMessage(request, "mrp.msg.orderSelError"))
                        .setBackUrl("/MrpBP.do?method=orderSel").setAlertRequest(request);
				forward = getForward(request, mapping, "message");
				return ;
			}
			list.addAll((ArrayList)rs.retVal);
		}	
		//物料清单已经做了改动
		if(stockCodes!=null&&list.size()!=stockCodes.length){
			EchoMessage.error().add(getMessage(request, "MRP.BOMUpdate.error")).setBackUrl("/MrpBP.do?winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
			forward=getForward(request,mapping,"message");
			return ;
		}
		int count=0;
		String trackNo="";
		String orderDetId="";
		
		String BomId=",";
		String BomDetId=",";
		HashMap storingMaps=new HashMap();
		HashMap productingMaps=new HashMap();
		HashMap stockUsedMaps=new HashMap();
		int level=0;
		//得到第一级与其他级别的商品的id
		for(int i=0;i<list.size();i++){
			HashMap map=(HashMap)list.get(i);	
			String detId=map.get("bomdetId").toString();	
			level=map.get("goodsNumber").toString().indexOf("~");
			if(level==-1){
				if(!BomId.contains(",'"+detId+"',")){
					BomId=BomId+"'"+detId+"',";
				}
			}else{
				if(!BomDetId.contains(",'"+detId+"',")){
					BomDetId=BomDetId+"'"+detId+"',";
				}
			}
		}
		/************************************用一条语句得到所选择商品的所有在途量*********************/
		if(BomId.length()>1){		
			BomId=BomId.substring(1,BomId.length()-1);
			//在途量
			HashMap storingMap=getStoring("tblBOM",BomId);	
			storingMaps.putAll(storingMap);
			//在产量
			HashMap productingMap=getProducting("tblBOM",BomId);	
			productingMaps.putAll(productingMap);
			//库存已分配量
			HashMap stockUsedMap=mgt.getStockUsed("tblBOM",BomId);	
			stockUsedMaps.putAll(stockUsedMap);
		}
		if(BomDetId.length()>1){		
			BomDetId=BomDetId.substring(1,BomDetId.length()-1);
			//在途量
			HashMap storingMap=getStoring("tblBOMDet",BomDetId);	
			HashMap storingMapT=getStoring("tblBOMDetail",BomDetId);
			storingMaps.putAll(storingMap);			
			storingMaps.putAll(storingMapT);
			//在产量
			HashMap productingMap=getProducting("tblBOMDet",BomDetId);	
			HashMap productingMapT=getProducting("tblBOMDetail",BomDetId);	
			productingMaps.putAll(productingMap);
			productingMaps.putAll(productingMapT);
			//库存已分配量
			HashMap stockUsedMap=mgt.getStockUsed("tblBOMDet",BomDetId);	
			HashMap stockUsedMapT=mgt.getStockUsed("tblBOMDetail",BomDetId);
			stockUsedMaps.putAll(stockUsedMap);
			stockUsedMaps.putAll(stockUsedMapT);
		}
		/*********************************以上代码：用一条语句得到所选择商品的所有在途量*********************/
		for(int i=0;i<list.size();i++){
			HashMap map=(HashMap)list.get(i);			
			if(map.get("trackNo")!=null&&map.get("trackNo").toString().length()>0){
				trackNo=map.get("trackNo").toString();
				orderDetId=map.get("detId").toString();
				count=0;
			}
			count++;
			
			//得到所选择的仓库
			String stockCode="";
			if(stockCodes!=null&&stockCodes[i]!=null){
				stockCode=stockCodes[i];
				map.put("stockCode", stockCodes[i]);
				map.put("stockName", stockNames[i]);
			} 
			
			String[] stockStrs=stockCode.split(";");
			stockCode="";
			for(int k=0;k<stockStrs.length;k++){
				if(stockStrs[k].length()>0){
					stockCode+="'"+stockStrs[k]+"',";
				}
			}
			if(stockCode.length()>0)stockCode=stockCode.substring(0,stockCode.length()-1);
			
			String billType=map.get("goodsNumber").toString().indexOf("~")==-1?"tblBom":"tblBomDet";
			if("true".equals(map.get("isPlace"))&&!map.get("path").equals(map.get("mainPath")+"001")){
				billType="tblBomDetail";
			}
			String goodsCode=map.get("goodsCode").toString();
			String detId=map.get("bomdetId").toString();			
			
			HashMap stockStore=getStore(goodsCode,billType,detId,stockCode);
			map.put("stockStore", stockStore);
			String store =stockStore.get("total").toString();
			String storing =storingMaps.get(detId)!=null?storingMaps.get(detId).toString():"0";		
			String producting =productingMaps.get(detId)!=null?productingMaps.get(detId).toString():"0";	
			String stockReQuantum =stockUsedMaps.get(detId)!=null?stockUsedMaps.get(detId).toString():"0";	
			//判断如果库存已分配量>实际库存，要更新库存已分配量表中的库存已分配量，分仓库存表的库存已分配量
			if(Double.parseDouble(stockReQuantum)>Double.parseDouble(store)){
				mgt.upStockQuantum(Double.parseDouble(stockReQuantum)-Double.parseDouble(store), map.get("goodsNumber").toString().indexOf("~")<0?true:false, detId);
				stockReQuantum=store;
			}			
			if(billType.equals("tblBom")){//主表计算
				Double require = Double.parseDouble(map.get("count").toString())*(Double.parseDouble(map.get("qty").toString()));
				map.put("count", require);//毛需求
			}
			map.put("store",  GlobalsTool.formatNumberS(Double.parseDouble(store), false, false, "Qty", ""));// 实际库存
			map.put("storeing",  GlobalsTool.formatNumberS(Double.parseDouble(storing), false, false, "Qty", ""));// 在途量
			map.put("producting",  GlobalsTool.formatNumberS(Double.parseDouble(producting), false, false, "Qty", ""));// 在产量	
			map.put("stockReQuantum",GlobalsTool.formatNumberS(stockReQuantum, false, false, "Qty", ""));//库存已分配量
			//通过单据编号得到本张订单的在途量，在产量，库存已分配量，订单已分配量
			Object[] currQtys = mgt.getQuantumQtys(trackNo, map.get("goodsCode").toString(),mrpFrom,detId,orderDetId);
			map.put("buyIngNum", GlobalsTool.formatNumber(currQtys[0], false, false, true, "", "", true));//本单在途量
			map.put("proingNum", GlobalsTool.formatNumber(currQtys[1], false, false, true, "", "", true));//本单在产量
			map.put("stockQuantum", GlobalsTool.formatNumber(currQtys[2], false, false, true, "", "", true));//本单库存已分配量
			map.put("orderQuantum", GlobalsTool.formatNumber(currQtys[3], false, false, true, "", "", true));//本单订单已分配量
			
			//界面有传递值或者没有传值但存在本单在途量，本单在产量，库存已分配量，订单已分配量时也默认被勾选
			if(operType!=null&&operType.equals("oper")){// 如果是运算根据用户在界面的选择显示
				map.put("cb", "false");
				for(int j=1;j<cbs.length;j++){//jsp界面的checkBox只有被选中的，值才传递到后台，cbs里面保存的值是其行数
					if((i+1)==Integer.parseInt(cbs[j])){
						map.put("cb", "true");
						break;
					}
				}
			}else{
				if(Double.parseDouble(currQtys[0].toString())>0||Double.parseDouble(currQtys[1].toString())>0||Double.parseDouble(currQtys[2].toString())>0
						||Double.parseDouble(currQtys[3].toString())>0||"true".equals(map.get("isPlace"))){
					map.put("cb", "true");
				}
			}
		}
		
		request.setAttribute("list", list);
		String mrpSets="";
		
		String pageFrom=request.getParameter("pageFrom");
		if("demand".equals(pageFrom)){
			String []mrpSet=request.getParameterValues("mrpSet");		
			for(int i=0;mrpSet!=null&&i<mrpSet.length;i++){
				mrpSets+=mrpSet[i]+",";
			}
		}else{
			mrpSets+="cbStore,cbStoring,cbUsed,";
		}
		if(mrpSets.length()>0){
			request.setAttribute("mrpSets", mrpSets.substring(0,mrpSets.length()-1));
		}
		request.setAttribute("StockCode1", StockCode1);
		request.setAttribute("Stock", Stock);
		request.setAttribute("back", "/MrpBP.do?method=backtoMa");
		forward = mapping.findForward("matereilDemand");
	}

	/**
	 * BOM物料很多时，不能在界面运算净需求量，且不需要逐个展示订单的物料，通过此方法运算出净需求量且合并多个订单的相同物料
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void bomDemand2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		String[] trackNos = request.getParameterValues("trackNo");
		String[] counts = request.getParameterValues("count");
		String[] chooses=request.getParameterValues("choose");
		String[] bomNos=request.getParameterValues("bomNo");
		String[] mrpFroms=request.getParameterValues("mrpFrom");
		String opeType=request.getParameter("opeType");
		String StockCode1=request.getParameter("StockCode1")==null?"":request.getParameter("StockCode1");
		String Stock=request.getParameter("Stock")==null?"":request.getParameter("Stock");
				
		request.setAttribute("StockCode1", StockCode1);
		request.setAttribute("Stock", Stock);
		
		long time=System.currentTimeMillis();
		
		if("BackMrp".equals(opeType)){
			trackNos=request.getSession().getAttribute("MRP_trackNos").toString().split(",");
			counts=request.getSession().getAttribute("MRP_counts").toString().split(",");
			chooses=request.getSession().getAttribute("MRP_chooses").toString().split(",");
			bomNos=request.getSession().getAttribute("MRP_bomNos").toString().split(",");
			mrpFroms=request.getSession().getAttribute("MRP_mrpFroms").toString().split(",");
		}else{
			String MRP_trackNos="";
			String MRP_counts="";
			String MRP_chooses="";
			String MRP_bomNos="";
			String MRP_mrpFroms="";
			
			for(int i=0;i<bomNos.length;i++){
				MRP_trackNos+=trackNos[i]+",";
				MRP_counts+=counts[i]+",";
				MRP_chooses+=chooses[i]+",";
				MRP_bomNos+=bomNos[i]+",";
				MRP_mrpFroms+=mrpFroms[i]+",";
			}
			request.getSession().setAttribute("MRP_trackNos", MRP_trackNos.substring(0,MRP_trackNos.length()-1));
			request.getSession().setAttribute("MRP_counts", MRP_counts.substring(0,MRP_counts.length()-1));
			request.getSession().setAttribute("MRP_chooses", MRP_chooses.substring(0,MRP_chooses.length()-1));
			request.getSession().setAttribute("MRP_bomNos", MRP_bomNos.substring(0,MRP_bomNos.length()-1));
			request.getSession().setAttribute("MRP_mrpFroms", MRP_mrpFroms.substring(0,MRP_mrpFroms.length()-1));
		}
		
		//得到所有需要运算的销售订单和生产计划单
		
		ArrayList trackNoList=new ArrayList();
		ArrayList allBillNo=new ArrayList();
		for(int i=0;i<bomNos.length;i++){
			if(i<chooses.length&&"true".equals(chooses[i])){
				trackNoList.add(trackNos[i]);
				allBillNo.add(new String[]{trackNos[i],bomNos[i],counts[i]});
			}
		}
		/*
		Result allBillRs=mgt.getOperBil(" where 1=1","");
		ArrayList allBillList=(ArrayList)allBillRs.getRetVal();
		for(int i=0;i<allBillList.size();i++){
			HashMap map=(HashMap)allBillList.get(i);
			String temp=map.get("trackNo").toString();
			if(!trackNoList.contains(temp)){
				allBillNo.add(new String[]{map.get("trackNo").toString(),map.get("bomNo").toString(),map.get("produceQty").toString()});
			}
		}*/
		
		/********************************************查询所有单据的成品及其所有物料******************************************/
		
		HashMap bomNoCount=new HashMap();//相同BOM的投产数量先合计
		HashMap currBillCountMap = new HashMap();
		ArrayList choosTrackNoList=new ArrayList();//记录界面用户选择的单据追踪单号
		String currBillCount="";
		for(int i=0;i<allBillNo.size();i++){
			String [] billNo=(String[])allBillNo.get(i);
			choosTrackNoList.add(billNo[0]);
			if(currBillCountMap.get(billNo[1])==null){    //记录所选订单的bom相同订单合计
				currBillCountMap.put(billNo[1], counts[i]);
			}
			else{
				currBillCountMap.put(billNo[1],Double.parseDouble(currBillCountMap.get(billNo[1]).toString()) + Double.parseDouble(counts[i])); 
			}
			//currBillCount= counts[i];
			
			if(bomNoCount.get(billNo[1])==null){
				bomNoCount.put(billNo[1],billNo[2]);
			}else{
				double oldCount=Double.parseDouble(bomNoCount.get(billNo[1]).toString());
				bomNoCount.put(billNo[1], Double.parseDouble(billNo[2])+oldCount);
			}
		}
		
			
		Result rs=this.getBillBom(allBillNo);
		ArrayList productList=(ArrayList)rs.retVal;
		//按界面单据的顺序排列商品
		ArrayList produceListNew=new ArrayList();
		ArrayList boms=new ArrayList();
		for(int i=0;i<allBillNo.size();i++){
			String [] billNo=(String[])allBillNo.get(i);
			String bomNo=billNo[1];
			if(boms.contains(bomNo))continue;
			for(int k=0;k<productList.size();k++){
				HashMap map=(HashMap)productList.get(k);
				String bomNo2=map.get("bomNo").toString();
				if(bomNo.equals(bomNo2)){
					produceListNew.add(map);
					boms.add(bomNo);
					break;
				}
			}
		}
		
		
		ArrayList pathList=new ArrayList();//保存所有商品及物料的路径
		HashMap pathMap=new HashMap();//保存路径多对应的值
		for (int i = 0; i < productList.size(); i++) { 
			
			/*******************将当前记录与界面相同的BOM的追踪单号整合在一起***************/
			HashMap map=(HashMap)productList.get(i);			
			String bomNo=map.get("bomNo").toString();
			String trackNo="";
			
			trackNo= map.get("trackNo").toString() + ",";

			
		/*	
			for(int k=0;k<bomNos.length;k++){
				if(bomNos[k].length()>0&&bomNo.equals(bomNos[k])){
					trackNo+=trackNos[k]+",";
				} 
			}
			
		*/	
			if(trackNo.length()>0){
				trackNo=trackNo.substring(0,trackNo.length()-1);
				map.put("trackNo", trackNo);
			}else{
				trackNo=map.get("trackNo").toString();
			}
			/*******************以上代码：将当前记录与界面相同的BOM的追踪单号整合在一起***************/
			
			boolean flag=true;
			
			
			if(map.get("bomdetId")==null||(map.get("bomdetId")!=null&&map.get("bomdetId").toString().length()==0)){
				flag=false;
			}
			
			//如果所运算的BOM单不存在，则给出错误提示
			if(!flag&&"false".equals(BaseEnv.systemSet.get("MRPNoMaterOper"))){
				String errorMessage="";
				if(map!=null){
					errorMessage = GlobalsTool.getMessage(request,
					"mrp.error.bomSelPro")
					 +map.get("billNo")+";"+GlobalsTool.getMessage(request,"iniGoods.lb.goodsName")
					 +":"+map.get("goodsFullName");
				}else{
					errorMessage = GlobalsTool.getMessage(request,"common.msg.RET_ID_NO_VALUE_ERROR");
				}
				EchoMessage.error().add(errorMessage).setBackUrl("/MrpBP.do?method=orderSel&winCurIndex="+request.getParameter("winCurIndex")).setAlertRequest(request);
				forward = getForward(request, mapping, "message");
				return;
			}
			
			//将订单商品，需求量保存到list 中
 			//map.put("count",bomNoCount.get(bomNo)==null?map.get("qty"):bomNoCount.get(bomNo).toString());
			map.put("count",map.get("qty")); 
			map.put("ch", "");
			map.put("BOMId", map.get("bomdetId"));
			pathList.add(trackNo);
			pathMap.put(trackNo, map);
			
		    //得到所选择的BOM单所有物料，并保存到list中
			if(map.get("bomdetId")!=null&&map.get("bomdetId").toString().length()>0){
				rs = this.getSonMap(map.get("bomdetId").toString(),map.get("trackNo").toString(),map.get("startDate").toString());
				if(rs.getRetCode()==ErrorCanst.MRP_ERROR_CODE){
					EchoMessage.error().add(getMessage(request, "mrp.msg.orderSelError"))
	                        .setBackUrl("/MrpBP.do?method=orderSel").setAlertRequest(request);
					forward = getForward(request, mapping, "message");
					return ;
				}
				Object[] obj=(Object[])rs.getRetVal();
				pathList.addAll((ArrayList)obj[0]);
				pathMap.putAll((HashMap)obj[1]);
			}
		}	
		/********************************************以上代码：查询所有单据的成品及其所有物料******************************************/


		//得到所有商品的可用库存，可用在途量，可用在产量
		//在途量
		HashMap storingMaps=getAllStoring();
		HashMap storingMap=new HashMap();storingMap.putAll(storingMaps);
		//申请量
		HashMap appingMaps=getAllApping();
		HashMap appingMap=new HashMap();appingMap.putAll(appingMaps);
		//在产量
		HashMap productingMaps=getAllProducting();
		HashMap productingMap=new HashMap();productingMap.putAll(productingMaps);
		//可用库存
		HashMap useStockMaps=mgt.getUseStock(StockCode1);	
		HashMap useStockMap=new HashMap();useStockMap.putAll(useStockMaps);
		
			
		/*************************************根据可用库存，在途量逐个计算物料的净需求量***********************************/
		//保存父级hash值对应的所有子级替换料，存入session用于下生产任务单
		HashMap hashChildRep=new HashMap();
		
		for(int i=0;i<pathList.size();i++){
			String path=pathList.get(i).toString();
			Object obj=pathMap.get(path); 
			
			if(obj==null)continue;//如果存在替换料，则pathMap中部分值在运算过程中会被删除
			
			HashMap itemMap=(HashMap)obj;
			
			String replace=itemMap.get("replace")==null?"":itemMap.get("replace").toString();
			if(("is").equals(replace))continue;//如果是替换料不运算物料数量
			
			String goodPropHash=itemMap.get("goodPropHash").toString();
			//得到商品总库存及本单占用量
			Double storing=storingMaps.get(goodPropHash)==null?0:Double.parseDouble(storingMaps.get(goodPropHash).toString());
			Double apping=appingMaps.get(goodPropHash)==null?0:Double.parseDouble(appingMaps.get(goodPropHash).toString());
			Double useStock=useStockMaps.get(goodPropHash)==null?0:Double.parseDouble(useStockMaps.get(goodPropHash).toString());
			Double producting=productingMaps.get(goodPropHash)==null?0:Double.parseDouble(productingMaps.get(goodPropHash).toString());
			
			//计算物料的毛需求量及净需求量
			double count=0;
			double needQty=0;
			double needProduct=0;
			HashMap mainMap=null;
			if(itemMap.get("ch").toString().indexOf("~")==-1){
				count=Double.parseDouble(itemMap.get("count").toString());
			}else{
				mainMap=(HashMap)pathMap.get(itemMap.get("mainPath").toString());
				count=Double.parseDouble(mainMap.get("needQty").toString())*Double.parseDouble(itemMap.get("qty").toString());
			}
			itemMap.put("count",GlobalsTool.formatNumberS(count, false, false, "Qty", ""));
			
			
			if("exist".equals(replace)){
				String mainGoodPropHash=mainMap.get("goodPropHash").toString();
				ArrayList useReplaceList=null;
				if(hashChildRep.get(mainGoodPropHash)==null){
					HashMap mateMap=new HashMap();
					useReplaceList=new ArrayList();
					mateMap.put(itemMap.get("bomdetId").toString(),useReplaceList);
					hashChildRep.put(mainGoodPropHash, mateMap);
				}else{
					HashMap mateMap=(HashMap)hashChildRep.get(mainGoodPropHash);
					if(mateMap.get(itemMap.get("bomdetId").toString())==null){
						useReplaceList=new ArrayList();
						mateMap.put(itemMap.get("bomdetId").toString(),useReplaceList);
					}else{
						useReplaceList=(ArrayList)mateMap.get(itemMap.get("bomdetId").toString());
					}
				}
				
				this.replaceMaterOper(itemMap, pathMap, useStockMaps, storingMaps, appingMaps, productingMaps, count,useReplaceList);
			}else{
				needQty=count;
				//毛需求量-本单占用数量如果大于0，则扣减库存和在途量,并将扣减后的数量更新到Map中
				if(needQty>0&&useStock>0){
					if(needQty>=useStock){
						needQty=needQty-useStock;
						useStock=0d;
					}else{
						useStock=useStock-needQty;
						needQty=0d;
					}
				}
				if(needQty>0&&storing>0){
					if(needQty>=storing){
						needQty=needQty-storing;
						storing=0d;
					}else{
						storing=storing-needQty;
						needQty=0d;
					}
				}
				
				if(needQty>0&&apping>0){
					if(needQty>=apping){
						needQty=needQty-apping;
						apping=0d;
					}else{
						apping=apping-needQty;
						needQty=0d;
					}
				}
				//needQty用于计算排除生产外的数量，此数量用于计算物料的需求量。needProduct指当前物料的净需求量
				needProduct=needQty;
				if(needProduct>0&&producting>0){
					if(needProduct>=producting){
						needProduct=needProduct-producting;
						producting=0d;
					}else{
						producting=producting-needProduct;
						needProduct=0d;
					}
				}
				useStockMaps.put(goodPropHash, useStock);
				storingMaps.put(goodPropHash, storing);
				appingMaps.put(goodPropHash, apping);
				productingMaps.put(goodPropHash, producting);
				
				itemMap.put("needQty",GlobalsTool.formatNumberS(needQty, false, false, "Qty", ""));
				itemMap.put("needProduct",GlobalsTool.formatNumberS(needProduct, false, false, "Qty", ""));
			}
		}
		/*************************************以上代码：根据可用库存，在途量逐个计算物料的净需求量***********************************/
		/*Iterator it=hashChildRep.keySet().iterator();
		
		while(it.hasNext()){
			String hash=it.next().toString();
			HashMap mate=(HashMap)hashChildRep.get(hash);
			Iterator it2=mate.keySet().iterator();.
			System.out.println("hash值为："+hash);
			while(it2.hasNext()){
				String key2=it2.next().toString();
				ArrayList list=(ArrayList)mate.get(key2);
				System.out.println("的物料："+key2);
				for(int i=0;i<list.size();i++){
					HashMap map=(HashMap)list.get(i);
					System.out.println(map.get("goodsCode")+"   "+map.get("count"));
				}
			}
		}*/
		
		/*********************查询所选择的单据的各个级别的子级商品，且删除不在这些商品范围内的其他商品**************************************/
		
		ArrayList operGoodsHashs=new ArrayList();
		ArrayList pathTempList = new ArrayList();
		for(int i=0;i<choosTrackNoList.size();i++){
			String trackNo=choosTrackNoList.get(i).toString();
			
			for(int k=0;k<pathList.size();k++){
				String path=pathList.get(k).toString();
				
				if(path.indexOf(trackNo)>=0){//当前记录的链接地址是以前当前追踪单号开头的，则此商品是被选中单据的商品
					Object obj=pathMap.get(path);
					if(obj==null)continue;//如果存在替换料，则pathMap中部分值在运算过程中会被删除	
					if (!pathTempList.contains(path)){
						pathTempList.add(path);//保存选择的订单的路径
					}
					HashMap itemMap=(HashMap)pathMap.get(path);
					operGoodsHashs.add(itemMap.get("goodPropHash").toString());
				}
			}
		}
		for(int k=0;k<pathList.size();k++){
			String path=pathList.get(k).toString();
			Object obj=pathMap.get(path);
			
			if(obj==null)continue;//如果存在替换料，则pathMap中部分值在运算过程中会被删除			
			
			HashMap itemMap=(HashMap)pathMap.get(path);
			
		
			String goodPropHash=itemMap.get("goodPropHash").toString();
			if(!operGoodsHashs.contains(goodPropHash)){
				pathMap.remove(path);
			}
		}
		
		
		/*********************以上代码：查询所选择的单据的各个级别的子级商品，且删除不在这些商品范围内的其他商品*********************************/
		
		/*******************************************遍历所有数据，将相同的商品的数据进行合并*******************************************/
		HashMap hashMap=new HashMap();
		ArrayList orderList=new ArrayList();
		pathList = new ArrayList();
		pathList = pathTempList;
		String goodsCodes=",";
		for(int i=0;i<pathList.size();i++){
			String path=pathList.get(i).toString();
			Object obj=pathMap.get(path);
			
			if(obj==null)continue;//如果存在替换料，则pathMap中部分值在运算过程中会被删除			
			
			HashMap itemMap=(HashMap)pathMap.get(path);
			String goodPropHash=itemMap.get("goodPropHash").toString();
			
			if(hashMap.get(goodPropHash)==null){
				if(itemMap.get("ch").toString().indexOf("~")>=0){
					itemMap.put("BCount", GlobalsTool.formatNumberS(Double.parseDouble(currBillCount)*Double.parseDouble(itemMap.get("unitNum").toString()), false, false, "Qty", ""));
				}else{
				//	currBillCount = GlobalsTool.formatNumberS(Double.parseDouble(currBillCountMap.get(itemMap.get("bomNo")).toString()),false,false,"Qty","");
					currBillCount = GlobalsTool.formatNumberS(Double.parseDouble(itemMap.get("count").toString()),false,false,"Qty","");
					itemMap.put("BCount", GlobalsTool.formatNumberS(currBillCount, false, false, "Qty", ""));
				}
				itemMap.put("store", GlobalsTool.formatNumberS(useStockMap.get(goodPropHash), false, false, "Qty", ""));
				itemMap.put("storeing", GlobalsTool.formatNumberS(storingMap.get(goodPropHash), false, false, "Qty", ""));
				itemMap.put("apping", GlobalsTool.formatNumberS(appingMap.get(goodPropHash), false, false, "Qty", ""));
				itemMap.put("producting", GlobalsTool.formatNumberS(productingMap.get(goodPropHash), false, false, "Qty", ""));
				
				hashMap.put(goodPropHash, itemMap);
				orderList.add(goodPropHash);
				String goodsCode=itemMap.get("goodsCode").toString();
				if(goodsCodes.indexOf(","+goodsCode+",")<0){
					goodsCodes+=goodsCode+",";
				}
			}else{
				HashMap oldItemMap=(HashMap)hashMap.get(goodPropHash);
				oldItemMap.put("trackNo", oldItemMap.get("trackNo")+","+itemMap.get("trackNo"));
				oldItemMap.put("count",GlobalsTool.formatNumberS(Double.parseDouble(itemMap.get("count").toString())+Double.parseDouble(oldItemMap.get("count").toString()), false, false, "Qty", ""));
				oldItemMap.put("needProduct",GlobalsTool.formatNumberS(Double.parseDouble(itemMap.get("needProduct").toString())+Double.parseDouble(oldItemMap.get("needProduct").toString()), false, false, "Qty", ""));
			}
		} 
		HashMap goodsMap=new HashMap();
		if(goodsCodes.length()>1){
			Result rst=mgt.getGoodsInfo("'"+goodsCodes.substring(1,goodsCodes.length()-1).replaceAll(",", "','")+"'");
			goodsMap=(HashMap)rst.getRetVal();
		}
		/*******************************************以上代码：遍历所有数据，将相同的商品的数据进行合并*****************************************/
		//下生产任务单时如果物料有替换料，则从此hash中取值
		request.getSession().setAttribute("hashChildRep", hashChildRep);
		
		request.setAttribute("orderList", orderList);
		request.setAttribute("pathMaps",hashMap);
		request.setAttribute("goodsMap",goodsMap);
		
		BaseEnv.log.debug("后台运算时间："+(System.currentTimeMillis()-time));
		forward = mapping.findForward("matereilDemand2");
	}
	
	/**
	 * 如果MRP运算过程中物料存在替换料，则原物料和替换料共同运算，计算所需物料
	 * @param itemMap 当前原物料数据
	 * @param pathMap 所有物料数据的Map
	 * @param useStockMaps 所有物料的库存
	 * @param storingMaps 所有物料在途量
	 * @param appingMaps 所有物料采购申请
	 * @param productingMaps 所有物料在产量
	 * @param count 原物料毛需求量
	 */
	private void replaceMaterOper(HashMap itemMap,HashMap pathMap,HashMap useStockMaps,HashMap storingMaps,HashMap appingMaps,HashMap productingMaps,double count,ArrayList useReplaceList){
		String path=itemMap.get("path").toString();
		
		/*****************************查找当前物料的所有替换料****************************/
		ArrayList replaceList=new ArrayList();
		replaceList.add(itemMap);
		
		Iterator it=pathMap.keySet().iterator();
		while(it.hasNext()){
			HashMap tempMap=(HashMap)pathMap.get(it.next());
			
			if(path.equals(tempMap.get("mainPath"))&&"is".equals(tempMap.get("replace"))){
				replaceList.add(tempMap);
			}
		}
		
		/********************循环所有物料，确定展示物料和需要采购物料**************************/
		double needQty=count;
		double needProduct=0;
		double useStock=0;
		double storing=0;
		double apping=0;
		double producting=0;
		
		//记录每项物料的可用库存和所有物料的可用库存，如果某项可用库存足够，则其他物料需要从map中删除
		double oneQty=0;
		double totalQty=0;
		Double []oneQtys=new Double[replaceList.size()];
		double productings=0;
		
		HashMap oneMateMap=null;//某项库存足够的物料
		int lastSize=-1;//截止到某项为止累计物料已经足够
		
		for(int i=0;i<replaceList.size();i++){
			HashMap mateMap=(HashMap)replaceList.get(i);
			String goodPropHash=mateMap.get("goodPropHash").toString();
			
			useStock=useStockMaps.get(goodPropHash)==null?0:Double.parseDouble(useStockMaps.get(goodPropHash).toString());
			storing=storingMaps.get(goodPropHash)==null?0:Double.parseDouble(storingMaps.get(goodPropHash).toString());
			apping=appingMaps.get(goodPropHash)==null?0:Double.parseDouble(appingMaps.get(goodPropHash).toString());			
			producting=productingMaps.get(goodPropHash)==null?0:Double.parseDouble(productingMaps.get(goodPropHash).toString());
			
			oneQty=useStock+storing+apping+producting;
			oneQtys[i]=oneQty;
			productings=productings+producting;
			
			totalQty=totalQty+useStock+storing+apping+producting;
			if(oneQty>=count){
				oneMateMap=mateMap;
				break;
			}
			if(totalQty>=count&&lastSize==-1)lastSize=i;
		}
		
		//1、查询出某项物料的可用库存已经足够，则删除其他物料.2、所有物料的可用库存都不足，则采购
		if(oneMateMap!=null){
			for(int i=0;i<replaceList.size();i++){
				HashMap mateMap=(HashMap)replaceList.get(i);
				if(oneMateMap!=mateMap){
					pathMap.remove(mateMap.get("path").toString());
				}
			}
			this.updateStockMaps(count, useStockMaps, storingMaps, appingMaps, productingMaps, oneMateMap.get("goodPropHash").toString());
			oneMateMap.put("count", count);
			oneMateMap.put("needQty",0);
			oneMateMap.put("needProduct",0);
			HashMap tempMap=(HashMap)oneMateMap.clone();
			useReplaceList.add(tempMap);
		}else{
			//2、所有物料的可用库存累计都不足，则需采购主物料
			if(totalQty<count){
				if(totalQty==0){//如果没有一个物料有库存则只保留主物料
					for(int i=1;i<replaceList.size();i++){
						HashMap mateMap=(HashMap)replaceList.get(i);
						pathMap.remove(mateMap.get("path").toString());
					}
					itemMap.put("count", count);
					itemMap.put("needQty", count);
					itemMap.put("needProduct",count);
					HashMap tempMap=(HashMap)itemMap.clone();
					useReplaceList.add(tempMap);
				}else{//如果有物料则删除可用库存为0的物料,且采购第一个有库存的物料		
					boolean flag=false;
					for(int i=0;i<replaceList.size();i++){
						HashMap mateMap=(HashMap)replaceList.get(i);
						
						//可用库存为0的删除
						if(oneQtys[i]==0){
							pathMap.remove(mateMap.get("path").toString());
						}else{
							if(!false){
								mateMap.put("count", oneQtys[i]+(count-totalQty));
								mateMap.put("needQty",count-totalQty+productings);
								mateMap.put("needProduct",count-totalQty);
								flag=true;
							}else{
								mateMap.put("count", oneQtys[i]);
								mateMap.put("needQty",0);
								mateMap.put("needProduct",0);
							}
							HashMap tempMap=(HashMap)mateMap.clone();
							useReplaceList.add(tempMap);
							String goodPropHash=mateMap.get("goodPropHash").toString();
							useStockMaps.put(goodPropHash, 0);
							storingMaps.put(goodPropHash, 0);
							appingMaps.put(goodPropHash, 0);
							productingMaps.put(goodPropHash, 0);
						}
					}
				}
			}else{//3、所有物料的可用库存累计足够，则根据可用库存的降序排列物料，展示从开头到第一次累计足够的物料
				double tempTotal=0;
				for(int i=0;i<replaceList.size();i++){
					HashMap mateMap=(HashMap)replaceList.get(i);
					String goodPropHash=mateMap.get("goodPropHash").toString();
					
					//删除可用库存为0、lastSize 之后的物料
					if(oneQtys[i]==0||i>lastSize){
						pathMap.remove(mateMap.get("path").toString());;
					}else{
						if(i<lastSize){//处理最后一条记录之前的数据
							mateMap.put("count", oneQtys[i]);
							tempTotal=tempTotal+oneQtys[i];
							
							useStockMaps.put(goodPropHash, 0);
							storingMaps.put(goodPropHash, 0);
							appingMaps.put(goodPropHash, 0);
							productingMaps.put(goodPropHash, 0);
						}else{
							mateMap.put("count",count-tempTotal);
							this.updateStockMaps(count-tempTotal, useStockMaps, storingMaps, appingMaps, productingMaps, goodPropHash);
						}
						HashMap tempMap=(HashMap)mateMap.clone();
						useReplaceList.add(tempMap);
						mateMap.put("needQty",0);
						mateMap.put("needProduct",0);
					}
				}
				
			}
		}
	}
	
	/**
	 * 根据当前hash值及毛需求量修改内存中库存Map
	 * @param needQty
	 * @param useStockMaps
	 * @param storingMaps
	 * @param appingMaps
	 * @param productingMaps
	 * @param goodPropHash
	 */
	private void updateStockMaps(double needQty,HashMap useStockMaps,HashMap storingMaps,HashMap appingMaps,HashMap productingMaps,String goodPropHash){
		//得到商品总库存及本单占用量
		Double storing=storingMaps.get(goodPropHash)==null?0:Double.parseDouble(storingMaps.get(goodPropHash).toString());
		Double apping=appingMaps.get(goodPropHash)==null?0:Double.parseDouble(appingMaps.get(goodPropHash).toString());
		Double useStock=useStockMaps.get(goodPropHash)==null?0:Double.parseDouble(useStockMaps.get(goodPropHash).toString());
		Double producting=productingMaps.get(goodPropHash)==null?0:Double.parseDouble(productingMaps.get(goodPropHash).toString());
		//毛需求量-本单占用数量如果大于0，则扣减库存和在途量,并将扣减后的数量更新到Map中
		if(needQty>0&&useStock>0){
			if(needQty>=useStock){
				needQty=needQty-useStock;
				useStock=0d;
			}else{
				useStock=useStock-needQty;
				needQty=0d;
			}
		}
		if(needQty>0&&storing>0){
			if(needQty>=storing){
				needQty=needQty-storing;
				storing=0d;
			}else{
				storing=storing-needQty;
				needQty=0d;
			}
		}
		
		if(needQty>0&&apping>0){
			if(needQty>=apping){
				needQty=needQty-apping;
				apping=0d;
			}else{
				apping=apping-needQty;
				needQty=0d;
			}
		}
		//needQty用于计算排除生产外的数量，此数量用于计算物料的需求量。needProduct指当前物料的净需求量
		double needProduct=needQty;
		if(needProduct>0&&producting>0){
			if(needProduct>=producting){
				needProduct=needProduct-producting;
				producting=0d;
			}else{
				producting=producting-needProduct;
				needProduct=0d;
			}
		}
		useStockMaps.put(goodPropHash, useStock);
		storingMaps.put(goodPropHash, storing);
		appingMaps.put(goodPropHash, apping);
		productingMaps.put(goodPropHash, producting);
	}
	/**
	 * 删除时要关联删除订单已分配量，库存分布表，单据中的相关信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void delMrp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ids = request.getParameter("ids");//得到要删除的mrp主表id
		String[] trackNos=request.getParameter("trackNo").split(",");
		for(int i=0;i<trackNos.length;i++){
			mgt.updateStock(trackNos[i]);//修改分仓库存订单已分配量，库存已分配量
			mgt.updateBill(trackNos[i]);//将所有单据的这个追踪单号信息删除掉
			mgt.delQuantum(trackNos[i]);//删除订单已分配量表中的追踪单号信息
		}
		deleteProductMRP(ids);// 执行删除操作
		mrpSel(mapping, form, request, response);// 删除后重新查询
	}

	// 获得MRP记录
	private Result getProductMRP(String loginId,MOperation mop,String[] conditions,int pageSize,int pageNo) {
		int startNo = 1 + (pageNo - 1) * pageSize;
        int endNo = pageSize+ (pageNo - 1) * pageSize;
		ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
		Result rs2 = null;// 仅用于记录数据的总条数
		// sql用于分布查询mrp主表信息
		String sql = "select * from(select a.mrpFrom,a.relationNo,a.goodsName,a.ProductQty"+
			",a.StoreQty,a.SafeStoreQty,a.StoringQty,a.ProductingQty,a.UsedQty,a.CreateDate,a.EmployeeId,tblDepartment.deptFullName"+
			",a.SCompanyID,a.id,a.employeeId as emp,a.hasProduce,a.hasApp,a.hasOrder"+
			",row_number() over (order by a.CreateDate desc) as row,a.createBy,(select departmentCode from tblEmployee where id=a.createBy) as deptCode,"+
			"a.relationID,a.bomNo,a.goodsNumber,a.bomDetId"+
			" from tblProductMRP a left join tblbom b on a.bomDetId=b.id "+
			" left join tblDepartment on a.departmentCode=tblDepartment.classCode"+
			" where 1=1 " ;
		
		if(!"".equals(conditions[0])){
			sql += "and a.MRPFrom=" + conditions[0] + " ";
		}else{
			sql += "and a.MRPFrom=2";//默认销售订单
		}
		if(!"".equals(conditions[1])){
			sql += "and a.relationNo like '%" + conditions[1] + "%' ";
		}
		if(!"".equals(conditions[2])){
			sql += "and a.goodsName like '%" + conditions[2] + "%' ";
		}
		if(!"".equals(conditions[3])){
			sql += "and a.EmployeeId like '%" + conditions[3] + "%' ";
		}
		if(!"".equals(conditions[4])){
			sql += "and tblDepartment.deptFullName like '%" + conditions[4] + "%' ";
		}
		
		sql += "and (a.createBy in ('"+loginId+"') or '"+loginId+"'='1')) as list where row between " + startNo + " and "
				+ endNo;
		sql = DynDBManager.scopeRightHandler(null,null,null,loginId,scopeRight, sql, "","");
		rs = new AIODBManager().sqlListMap(sql, new ArrayList(), 0, 0);// 查询
		sql = "select count(tblProductMRP.id) from tblProductMRP  left join tblbom b on tblProductMRP.bomDetId=b.id left join tblgoods g on b.goodscode=g.classcode";
		sql += " left join tblEmployee e on tblProductMRP.employeeId=e.id left join tblDepartment on e.DepartmentCode=tblDepartment.classCode where 1=1 and (tblProductMRP.createBy in ('"+loginId+"') or '"+loginId+"'='1')";// 查询记录的总条数
		sql = DynDBManager.scopeRightHandler(null,null,null,loginId,scopeRight, sql, "","");

		rs2 = new MrpBPMgt().querySql(sql.toString());// 查询条数
		rs.setPageSize(pageSize);
		rs.setPageNo(pageNo);
        
		return rs;
	}

	// 获得商品ID和名称
	private Result getGoods(String loginId,ArrayList scopeRight,String selGoodsNumber,String selGoodsFullName,String selGoodsSpec,int pageSize,int pageNo) {
		String propStr="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")){
					propStr+=",((select propItemName from tblGoodsPropItem where propItemID=b."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"')) as "+propBean.getPropName();
				}else{
					propStr+=",b."+propBean.getPropName();
				}
			}
		}		
		StringBuffer sql = new StringBuffer();//分页查询

		sql.append("select b.id,b.classcode,tblGoods.goodsFullName,tblGoods.goodsnumber,tblGoods.goodsSpec,b.billNo");
		sql.append(propStr);
		sql.append(" ,row_number() over(order by b.id) as row_id from tblGoods inner join tblbom b");
		sql.append(" on tblGoods.classcode=b.goodscode ");
		sql.append(" where 1=1 and b.moduleType='B' and (select count(0) from tblBOMDet k where k.f_ref=b.id)>0 and b.workFlowNodeName='finish' and b.statusId=0");

		if (!selGoodsNumber.equals("")) {
			sql.append(" and goodsNumber like '%"+selGoodsNumber.trim()+"%' ");
		}
		if (!selGoodsFullName.equals("")) {
			sql.append(" and goodsFullName like '%"+selGoodsFullName.trim()+"%' ");
		}
		if (!selGoodsSpec.equals("")) {
			sql.append(" and goodsSpec like '%"+selGoodsSpec.trim()+"%' ");
		}
		DynDBManager dyn=new DynDBManager();
		String sql1=dyn.scopeRightHandler(null,null,null,loginId,scopeRight, sql.toString(),"endClass","");
		
		AIODBManager aioMgt=new AIODBManager();
		Result rs=aioMgt.sqlListMap(sql1, new ArrayList(), pageNo, pageSize);		
		
		return rs;
	}
	/**
	 * 
	 * @param tblName要查询是否启用属性的表名
	 * @return Map<表名,List>,List:记录启用属性的字段名,字段中文名,表名,中文表名,启用与否数字(不等于3表启用)
	 */
	// 查询某表中是否有属性字段，并且没有被隐藏（也就表示启用）
	public Map<String, List> getUsedGoodsProp(String tblName) {
		// 用于存放表名，［字段名］［多语言名-中文］
		Map<String, List> map = new HashMap<String, List>();// <表名，字段结果集［［字段］［多语言］。。。］>
		String language = "zh_CN";//getLocale(request).toString();
		StringBuffer sql = new StringBuffer("select tf.fieldname,tl."
				+ language + ",t.tablename,tf.inputtype");
		sql.append(" from tbldbtableinfo t,tbldbfieldinfo tf ,tblLanguage tl");
		sql.append(" where tablename= '" + tblName + "'");
		sql.append("  and t.id=tf.tableid and tf.languageId=tl.id");
		sql
				.append(" and tf.fieldname in(select propname from tblgoodspropinfo where isused='1')");
		sql.append(" and tf.inputtype<>'3' order by tf.fieldname");

		Result rs = new MrpBPMgt().querySql(sql.toString());// 查询

		map.put(tblName, (List) rs.retVal);// 放入map中
		return map;
	}

	/**
	 * 将所有String数组连接起来组成一个新数组，并去除数组中的重复字符串，
	 * 
	 * @param arr
	 * @return
	 */
	public Object[] clearOverhand(String[]... arr) {

		LinkedHashSet<String> hs = new LinkedHashSet<String>();
		Object[] obj = null;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				hs.add(arr[i][j]);
			}
		}
		obj = hs.toArray();
		return obj;
	}

	/**
	 * 获得要查询的表字段信息
	 * 
	 * @param fields
	 *            表字段，表中存在
	 * @param allFields
	 *            全表字段，表中可能不存在
	 * @param byName
	 *            给表的别名，可为null
	 * @param defaultStr
	 *            如果字段不存在时查询的默认字符
	 * @return 例： ",b.name as name,b.color as color,'' as yearNo"
	 */
	public String getColStr(String[] fields, Object[] cols, String byName,
			String defaultStr) {
		// 要做null或fields与allFields长度判断

		String col = null;
		StringBuffer rtlnVal = new StringBuffer();
		LinkedHashSet<String> fieldsSet = new LinkedHashSet<String>();
		for (int i = 0; i < fields.length; i++) {
			fieldsSet.add(fields[i]);
		}
		for (int i = 0; i < cols.length; i++) {
			col = String.valueOf(cols[i]);
			if (fieldsSet.contains(col))// 表中要查询的字段
				rtlnVal.append("," + byName + cols[i] + " as " + cols[i]);
			else
				rtlnVal.append(",'" + defaultStr + "' as " + cols[i]);
			
			if(col.equals("yearNO")||col.equals("Hue")){
				rtlnVal.append(",(select top 1 PropItemName from tblGoodsPropInfo an, "+
								" tblGoodsOfProp bn,tblGoodsOfPropdet cn where an.id=bn.PropID "+
								" and an.propName='"+col+"' and bn.id=cn.f_ref and cn.PropItemID="+byName+col+") as "+cols[i]+"Dis ");
			}
		}
		return rtlnVal.toString();
	}

	/**
	 * 获得表tblbom和tblbomdet共同的商品启用属性
	 * 
	 * @param index
	 *            0表示取得字段名(英文),1表示取得中文名(多语言)
	 * @return 返回object类型的对象数组，其中内容是字符串
	 */
	public Object[] getBomAndBomDetFields(String[] bomFields,
			String[] bomDetFields, int index) {
		if (bomFields == null) {
			bomFields = getFields("tblbom", index);// 用于jsp中的动态表头
		}
		if (bomDetFields == null) {
			bomDetFields = getFields("tblbomdet", index);// 用于jsp中的动态表头
		}
		LinkedHashSet<String> fieldSet = new LinkedHashSet<String>();
		for (int i = 0; i < bomFields.length; i++) {
			fieldSet.add(bomFields[i]);
		}
		for (int i = 0; i < bomDetFields.length; i++) {
			fieldSet.add(bomDetFields[i]);
		}
		Object[] fields = fieldSet.toArray();
		return fields;

	}

	/**
	 * 获得表中启用的商品属性数组
	 * 
	 * @param tblName
	 *            如果为null则
	 * @param index
	 * @return
	 */
	public String[] getFields(String tblName, int index) {

		String[] fields = null;
		if (!"".equals(tblName))
			// 每次重新查询
			usedProp = (ArrayList<String[]>) getUsedGoodsProp(tblName).get(
					tblName);
		// }

		if (usedProp != null)
			fields = getFields(usedProp, index);
		return fields;
	}

	/**
	 * 
	 * @param attList
	 * @param index
	 * @return
	 */
	// 获得启用的商品属性数组
	private String[] getFields(ArrayList attList, int index) {

		// if (attList == null)
		// attList = usedProp; // 如果没有设定启用属性集合，则用tblbomdet的属性
		String[] fields = new String[attList.size()]; // 记录要返回的字段名
		for (int i = 0; i < attList.size(); i++) {// 设置返回字段
			fields[i] = ((String[]) attList.get(i))[index];// list中每个对象是一个string数组
		}
		return fields;
	}

	// 获得定单记录
	private Result getOrders(String mrpFrom,String where,MOperation mop,String operStatus) {
		if(!mrpFrom.equals("1")&&!mrpFrom.equals("2")){
			rs.setRetVal(new ArrayList());
			return rs;
		}
		ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
		StringBuffer sql = null;// 要查询的sql语句
		String mainTable="";
		String detTable="";
		if("1".equals(mrpFrom)){
			detTable="tblPlanDet";
		}else{
			detTable="tblSalesOrderDet";
		}
		
		//得到已经启用的商品属性信息
		String propStr="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&!propBean.getPropName().equals("ProDate")&&!propBean.getPropName().equals("Availably")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, detTable, propBean.getPropName())!=null){//启用的商品属性
				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")
						||propBean.getPropName().equals("Design")||propBean.getPropName().equals("color")){
					if(BaseEnv.version==8){//工贸版
						propStr+=",isnull((select propItemName from tblGoodsPropItem where propItemID=@TABLENAME."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"'),'') as "+propBean.getPropName();
					}else{//布匹版
						if(propBean.getPropName().equals("color")&&BaseEnv.systemSet.get("Usepubliccolor").getSetting().equals("false")){//如果颜色值设置不是公共颜色
							propStr+=",(select propItemName from tblGoodsOfProp ,tblGoodsOfPropDet  where  tblGoodsOfProp.id=tblGoodsOfPropDet.f_ref and tblGoodsOfProp.goodsCode=@TABLENAME.goodsCode and tblGoodsOfPropDet.propItemID=@TABLENAME."+propBean.getPropName()+" and tblGoodsOfPropDet.PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}else{
							propStr+=",(select propItemName from tblGoodsOfPropDet where propItemID=@TABLENAME."+propBean.getPropName()+" and PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}
					}
				}else{
					propStr+=",@TABLENAME."+propBean.getPropName();
				}
			}
		}
		
		sql = new StringBuffer("select ");
		if("1".equals(mrpFrom)){//生产计划单
			sql.append(" b.trackNo,a.id as detId,b.goodsCode,c.goodsnumber,c.goodsFullName,c.GoodsSpec");
			sql.append(",b.NotProduceInQty as produceQty,a.finishDate as sendDate,d.EmpFullName,e.DeptFullName");
			sql.append(",row_number() over(order by a.finishDate,a.createTime desc) as row_id");
			sql.append(propStr.replaceAll("@TABLENAME", "b"));
			sql.append(" from tblPlan a");
			sql.append(" left join tblPlanDet b on a.id=b.f_ref");
			sql.append(" left join tblGoods c on b.goodsCode=c.classCode");
			sql.append(" left join tblEmployee d on a.EmployeeID=d.id");
			sql.append(" left join tblDepartment e on a.DepartmentCode=e.classcode");
		}else if("2".equals(mrpFrom)){//销售订单
			sql.append(" b.trackNo,a.id as detId,b.goodsCode,c.goodsnumber,c.goodsFullName,c.GoodsSpec");
			sql.append(",b.NotOutQty as produceQty,(case when len(b.sendDate)=0 then a.sendDate else b.sendDate end) as sendDate,d.EmpFullName,e.DeptFullName");
			sql.append(",row_number() over(order by case when len(b.sendDate)=0 then a.sendDate else b.sendDate end desc,a.createTime desc) as row_id");
			sql.append(propStr.replaceAll("@TABLENAME", "b"));
			sql.append(" from tblSalesOrder a"); 
			sql.append(" left join tblSalesOrderDet b on a.id=b.f_ref");
			sql.append(" left join tblGoods c on b.goodsCode=c.classCode");
			sql.append(" left join tblEmployee d on a.EmployeeID=d.id");
			sql.append(" left join tblDepartment e on a.DepartmentCode=e.classcode");
		}
		
		if("1".equals(mrpFrom)||"2".equals(mrpFrom)){
			sql.append(where);
			if(where.indexOf(" where ")==-1){
				sql.append(" where ");
			} 
			if("1".equals(mrpFrom)){//生产计划单
				sql.append(" and b.NotProduceInQty>0 ");
			}else{
				sql.append(" and b.NotOutQty>0 ");
			}

			sql.append(" and a.statusId=0 ");

	
			if("0".equals(operStatus)||operStatus==null){//未运算
				sql.append(" and b.trackNo not in (select salesOrderID from tblSalesOrderQuantum)");
			}else if("1".equals(operStatus)){//已运算
				sql.append(" and b.trackNo in (select salesOrderID from tblSalesOrderQuantum)");
			}
			
			sql.append(" and (a.workFlowNodeName='finish' or a.workFlowNodeName is null)") ;
			BaseEnv.log.debug("order query:"+sql.toString());
			rs = new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0);
		}
		return rs;
	}
	private Result getOrdersBom(String orderId,String mrpFrom,String bomNo) {
		StringBuffer sql = null;// 要查询的sql语句
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性

				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")
										||propBean.getPropName().equals("Design")||propBean.getPropName().equals("color")){
					if(BaseEnv.version==8){//工贸版
						propStr+=",((select propItemName from tblGoodsPropItem where propItemID=@TABLENAME."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"')) as "+propBean.getPropName();
					}else{//布匹版
						if(propBean.getPropName().equals("color")&&BaseEnv.systemSet.get("Usepubliccolor").getSetting().equals("false")){//如果颜色值设置不是公共颜色
							propStr+=",(select propItemName from tblGoodsOfProp ,tblGoodsOfPropDet  where  tblGoodsOfProp.id=tblGoodsOfPropDet.f_ref and tblGoodsOfProp.goodsCode=@TABLENAME.goodsCode and tblGoodsOfPropDet.propItemID=@TABLENAME."+propBean.getPropName()+" and tblGoodsOfPropDet.PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}else{
							propStr+=",(select propItemName from tblGoodsOfPropDet where propItemID=@TABLENAME."+propBean.getPropName()+" and PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}
					}
				}else{
					propStr+=",@TABLENAME."+propBean.getPropName();
				}
				bomCond+=" and f."+propBean.getPropName()+"=@TABLENAME."+propBean.getPropName();
			}
		}		
		
		sql = new StringBuffer();
		if("1".equals(mrpFrom)){
			sql.append("select top 1"); 
			sql.append(" b.trackNo,a.id as detId,c.goodsNumber,c.goodsFullName,c.goodsSpec,a.finishDate as sendDate,a.employeeId,d.empFullName,e.deptFullName,f.id as bomdetId,f.price,b.goodsCode,f.billNo as bomNo,c.MaterielAttribute,g.unitName");
			sql.append(",CONVERT(varchar(100),Dateadd(d,-1*isnull((case when c.ProducePeriod=0 then 1 else c.ProducePeriod end),1),a.finishTime),23) as startDate,CONVERT(varchar(100),Dateadd(d,-1,a.finishTime),23) as submitDate");
			sql.append(",cast(Hashbytes('md5',Lower(b.GoodsCode+'GoodsCode'+isnull(b.color,'')+'color')) as bigint) as goodPropHash");
			sql.append(propStr.replaceAll("@TABLENAME", "b"));
			sql.append(" from tblPlan a"); 
			sql.append(" left join tblPlanDet b on a.id=b.f_ref"); 
			sql.append(" left join tblGoods c on b.goodsCode=c.classCode");
			sql.append(" left join tblUnit g on c.BaseUnit=g.id");
			sql.append(" left join tblEmployee d on a.EmployeeID=d.id");
			sql.append(" left join tblDepartment e on a.DepartmentCode=e.classcode");
			sql.append(" left join tblBom f on  b.goodsCode=f.goodsCode");
			sql.append(" left join tblSalesOrderQuantum k on k.salesOrderId= b.trackNo and k.GoodsCode=b.GoodsCode and f.id=k.bomdetId");
			sql.append(" where f.billNo='"+bomNo+"' and b.trackNo='"+orderId+"'");
		}else if("2".equals(mrpFrom)){
			sql.append("select top 1"); 
			sql.append(" b.trackNo,a.id as detId,c.goodsNumber,c.goodsFullName,c.goodsSpec,a.sendDate,a.employeeId,d.empFullName,e.deptFullName,f.id as bomdetId,f.price,b.goodsCode,f.billNo as bomNo,c.MaterielAttribute,g.unitName");
			sql.append(",CONVERT(varchar(100),Dateadd(d,-1*isnull((case when c.ProducePeriod=0 then 1 else c.ProducePeriod end),1),(case when len(b.SendDate)=0 then a.BillDate else b.SendDate end)),23) as startDate,CONVERT(varchar(100),Dateadd(d,-1,(case when len(b.SendDate)=0 then a.BillDate else b.SendDate end)),23) as submitDate");
			sql.append(",cast(Hashbytes('md5',Lower(b.GoodsCode+'GoodsCode'+isnull(b.color,'')+'color')) as bigint) as goodPropHash");
			sql.append(propStr.replaceAll("@TABLENAME", "b"));
			sql.append(" from tblSalesOrder a"); 
			sql.append(" left join tblSalesOrderDet b on a.id=b.f_ref");
			sql.append(" left join tblGoods c on b.goodsCode=c.classCode");
			sql.append(" left join tblUnit g on c.BaseUnit=g.id");
			sql.append(" left join tblEmployee d on a.EmployeeID=d.id");
			sql.append(" left join tblDepartment e on a.DepartmentCode=e.classcode");
			sql.append(" left join tblBom f on  b.goodsCode=f.goodsCode");
			sql.append(" left join tblSalesOrderQuantum k on k.salesOrderId= b.trackNo and k.GoodsCode=b.GoodsCode and f.id=k.bomdetId");
			sql.append(" where (f.billNo='"+bomNo+"' or ''='"+bomNo+"') and b.trackNo='"+orderId+"'");
		}else if("0".equals(mrpFrom)){
			sql.append("select top 1"); 
			sql.append(" a.billNo as trackNo,a.id as detId,b.goodsNumber,b.goodsFullName,b.goodsSpec,'' as sendDate,'' as employeeId,'' as empFullName,'' as deptFullName,a.id as bomdetId,a.price,a.goodsCode,a.billNo as bomNo,b.MaterielAttribute,g.unitName,'' as startDate,'' as submitDate");
			sql.append(",cast(Hashbytes('md5',Lower(a.GoodsCode+'GoodsCode'+isnull(a.color,'')+'color')) as bigint) as goodPropHash");
			sql.append(propStr.replaceAll("@TABLENAME", "a"));
			sql.append(" from tblBom a"); 
			sql.append(" left join tblGoods b on a.goodsCode=b.classCode");
			sql.append(" left join tblUnit g on b.BaseUnit=g.id");
			sql.append(" left join tblSalesOrderQuantum k on k.salesOrderId= a.billNo and k.GoodsCode=a.GoodsCode and a.id=k.bomdetId");
			sql.append(" where a.billNo='"+orderId+"' and (a.workFlowNodeName='finish' or a.workFlowNodeName is null) order by VersionNO desc");
		}
		System.out.println("OrdersBom query:"+sql.toString());
		rs = new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0);
		return rs;
	}
	
	//获得订单及物料的信息
	private Result getBillBom(ArrayList allBillNo) {
		StringBuffer sql = null;// 要查询的sql语句
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		/*
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性

				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")
										||propBean.getPropName().equals("Design")||propBean.getPropName().equals("color")){
					propStr+=",((select propItemName from tblGoodsPropItem where propItemID=@TABLENAME."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"')) as "+propBean.getPropName();					
				}else{
					propStr+=",@TABLENAME."+propBean.getPropName();
				}
			}
		}*/		
		
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")
						||propBean.getPropName().equals("Design")||propBean.getPropName().equals("color")){
					if(propBean.getPropName().equals("color")&&BaseEnv.systemSet.get("Usepubliccolor").getSetting().equals("false")){//如果颜色值设置不是公共颜色
						propStr+=",(select propItemName from tblGoodsOfProp ,tblGoodsOfPropDet  where  tblGoodsOfProp.id=tblGoodsOfPropDet.f_ref and tblGoodsOfProp.goodsCode=@TABLENAME.goodsCode and tblGoodsOfPropDet.propItemID=@TABLENAME."+propBean.getPropName()+" and tblGoodsOfPropDet.PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
					}else{
						propStr+=",(select propItemName from tblGoodsOfPropDet where propItemID=@TABLENAME."+propBean.getPropName()+" and PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
					}
				
					}else{
						propStr+=",@TABLENAME."+propBean.getPropName();
					}
			}
		}	
		
		
		for(int i=0;i<allBillNo.size();i++){
			String []billNo=(String[])allBillNo.get(i);
			String trackNo=billNo[0];
			String bomNo=billNo[1];
			
			bomCond+="((f.BillNo='"+bomNo+"'or ''='"+bomNo+"') and b.trackNo='"+trackNo+"') or ";
		}
		
		if(bomCond.length()>0){
			bomCond=bomCond.substring(0,bomCond.length()-3);
		}
		
		sql = new StringBuffer();
		sql.append("select trackNo as trackNo,goodsNumber,goodsFullName,goodsSpec,min(sendDate) as sendDate,bomdetId,price,goodsCode,bomNo,MaterielAttribute,unitName,sum(qty) as qty,min(startDate) as startDate,min(submitDate) as submitDate,goodPropHash from (");
		/*
			sql.append("select "); 
			sql.append(" b.trackNo,a.id as detId,c.goodsNumber,c.goodsFullName,c.goodsSpec,a.finishDate as sendDate,a.employeeId,d.empFullName,e.deptFullName,f.id as bomdetId,f.price,b.goodsCode,isnull(f.billNo,b.trackNo) as bomNo,c.MaterielAttribute,g.unitName,b.NotProduceInQty as qty");
			sql.append(",CONVERT(varchar(100),Dateadd(d,-1*isnull((case when c.ProducePeriod=0 then 1 else c.ProducePeriod end),1),a.finishTime),23) as startDate,CONVERT(varchar(100),Dateadd(d,-1,a.finishTime),23) as submitDate");
			sql.append(",cast(Hashbytes('md5',Lower(b.GoodsCode+'GoodsCode'+isnull(b.BatchNo,'')+'BatchNo'+isnull(b.Hue,'')+'Hue'+isnull(b.Inch,'')+'Inch'+isnull(b.yearNO,'')+'yearNO')) as bigint) as goodPropHash");
			sql.append(propStr.replaceAll("@TABLENAME", "b"));
			sql.append(" from tblPlan a"); 
			sql.append(" left join tblPlanDet b on a.id=b.f_ref"); 
			sql.append(" left join tblGoods c on b.goodsCode=c.classCode");
			sql.append(" left join tblUnit g on c.BaseUnit=g.id");
			sql.append(" left join tblEmployee d on a.EmployeeID=d.id");
			sql.append(" left join tblDepartment e on a.DepartmentCode=e.classcode");
			sql.append(" left join tblBom f on  b.goodsCode=f.goodsCode and f.statusId=0");
			sql.append(" where "+bomCond);
			sql.append(" union all ");*/
			sql.append("select "); 
			sql.append(" b.trackNo,a.id as detId,c.goodsNumber,c.goodsFullName,c.goodsSpec,a.sendDate,a.employeeId,d.empFullName,e.deptFullName,f.id as bomdetId,f.price,b.goodsCode,isnull(f.billNo,b.trackNo) as bomNo,c.MaterielAttribute,g.unitName,b.NotOutQty as qty");
			sql.append(",CONVERT(varchar(100),Dateadd(d,-1*isnull((case when c.ProducePeriod=0 then 1 else c.ProducePeriod end),1),(case when len(b.SendDate)=0 then a.BillDate else b.SendDate end)),23) as startDate,CONVERT(varchar(100),Dateadd(d,-1,(case when len(b.SendDate)=0 then a.BillDate else b.SendDate end)),23) as submitDate");
			
			//sql.append(",cast(Hashbytes('md5',Lower(b.GoodsCode+'GoodsCode'+isnull(b.BatchNo,'')+'BatchNo'+isnull(b.Hue,'')+'Hue'+isnull(b.Inch,'')+'Inch'+isnull(b.yearNO,'')+'yearNO')) as bigint) as goodPropHash");
			//订单与库存属性匹配
			sql.append(",cast(Hashbytes('md5',Lower(b.GoodsCode+'GoodsCode'+isnull(b.color,'')+'color')) as bigint) as goodPropHash");
			
			sql.append(propStr.replaceAll("@TABLENAME", "b"));
			sql.append(" from tblSalesOrder a"); 
			sql.append(" left join tblSalesOrderDet b on a.id=b.f_ref");
			sql.append(" left join tblGoods c on b.goodsCode=c.classCode");
			sql.append(" left join tblUnit g on c.BaseUnit=g.id");
			sql.append(" left join tblEmployee d on a.EmployeeID=d.id");
			sql.append(" left join tblDepartment e on a.DepartmentCode=e.classcode");
			sql.append(" left join tblBom f on  b.goodsCode=f.goodsCode and f.statusId=0");
			sql.append(" where "+bomCond);
		sql.append(") as bill group by goodsNumber,goodsFullName,goodsSpec,bomdetId,price,goodsCode,bomNo,MaterielAttribute,unitName,goodPropHash,trackNo order by trackNo"); 
			
		BaseEnv.log.debug("OrdersBom query:"+sql.toString());
		rs = new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0);
		return rs;
	}

	// 根据id删除tblProductMRP表中的记录
	private boolean deleteProductMRP(String ids) {

		ids = "'" + ids.replaceAll(",", "','") + "'";// 把id转化为'id1','id2',...这种形式，
		StringBuffer sql = null;// 要执行的sql语句
		sql = new StringBuffer();
		sql.append("delete from tblProductMRPDet");// 删除mrp从表记录sql
		sql.append(" where f_ref in(" + ids + ")");
		new MrpBPMgt().execSql(sql.toString());// 执行删除
		sql = new StringBuffer("delete from tblProductMRP");// 删除mrp主表记录sql
		sql.append(" where id in(");
		sql.append(ids);
		sql.append(")");
		return Boolean.parseBoolean(new MrpBPMgt().execSql(sql.toString()).getRetVal().toString());// 删除mrp主表记录
	}

	// 获得商品所选择的各个仓库的实际库存
	public HashMap getStore(String goodsCode,String billType,String detId,String stockCodes) {
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				bomCond+=" and isnull(a."+propBean.getPropName()+",'')=isnull(b."+propBean.getPropName()+",'')";
			}
		}

		StringBuffer sql=new StringBuffer("");
		String goodsMethod=BaseEnv.systemSet.get("GoodsCostingMethod").getSetting() ;//MWAM 表示是移动加权 FIFO表示先进先出

		if(goodsMethod.equals("MWAM")||goodsMethod.equals("MONTH")){
			sql.append("select stockCode,isnull(sum(isnull(TotalQty,0)),0) from tblStocks a,"+billType+" b where b.id='"+detId+"' and a.goodsCode=b.goodsCode"+bomCond);
			if(stockCodes.length()>0){
				sql.append(" and a.stockCode in ("+stockCodes+") group by a.stockCode");
			}else{//如果没有选择仓库,要过滤掉不参与MRP运算的仓库
				sql.append(" and a.stockCode in (select classCode from tblStock where IsMrpOper=1) group by a.stockCode");
			}
		}else{
			sql.append("select stockCode,isnull(sum(isnull(totalQty,0)),0) from tblStockDet a,"+billType+" b where b.id='"+detId+"' and a.goodsCode=b.goodsCode and totalQty>0"+bomCond);
			if(stockCodes.length()>0){
				sql.append(" and a.stockCode in ("+stockCodes+") group by a.stockCode");
			}else{//如果没有选择仓库,要过滤掉不参与MRP运算的仓库
				sql.append(" and a.stockCode in (select classCode from tblStock where IsMrpOper=1) group by a.stockCode");
			}
		}
		HashMap map=new HashMap();
		AIODBManager aioMgt=new AIODBManager();
		Result rs=aioMgt.sqlList(sql.toString(), new ArrayList());
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
			List listStock=(ArrayList)rs.retVal;
			String total="0";
			for(int i=0;i<listStock.size();i++){
				Object[]obj=(Object[])listStock.get(i);
				if(obj[1]==null){
					map.put(obj[0], "0");
				}else{
					map.put(obj[0], GlobalsTool.newFormatNumber(obj[1], false, false, true, "", "", true));
				}				 
				
				total=GlobalsTool.newFormatNumber(Double.parseDouble(obj[1].toString())+Double.parseDouble(total), false, false, true, "", "", true);
			}

			map.put("total", total);
		}else{
			map.put("total", 0);
		}
		return map;
	}
	
	/**
	 * 获得商品的在途量(未关联销售订单)
	 * @param goodscode
	 * @param fields
	 * @param fieldVals
	 * @param stock
	 * @return
	 */
	public HashMap getStoring(String billType,String detIds) {
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&!propBean.getPropName().equals("ProDate")&&!propBean.getPropName().equals("Availably")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBuyOrderDet", propBean.getPropName())!=null){//启用的商品属性
				bomCond+=" and isnull(a."+propBean.getPropName()+",'')=isnull(c."+propBean.getPropName()+",'')";
			}
		}
		
		StringBuffer sql=new StringBuffer("");
		sql.append("select c.id as detId,sum(a.NotInQty) from tblBuyOrderDet a,tblBuyOrder b,"+billType+" c where c.id in ("+detIds+") and a.f_ref=b.id and a.goodsCode=c.goodsCode "+bomCond+" and a.NotInQty>0 ");
		sql.append("and ((select count(0) from tblSalesOrderQuantum where charIndex(salesOrderId,a.trackNo)>0)=0 or a.trackNo is null  or len(a.trackNo)=0) and b.workflowNodeName='finish'");

		//if(stockCodes.length()>0){
		//	sql.append(" and a.stockCode in ("+stockCodes+")");
		//}else{//如果没有选择仓库,要过滤掉不参与MRP运算的仓库
			sql.append(" and (a.stockCode in (select classCode from tblStock where IsMrpOper=1) or len(a.stockCode)=0 or a.stockCode is null) group by c.id");
		//}
		 
		try {
			HashMap storingMap=new HashMap();
			ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
			for(int i=0;li!=null&&i<li.size();i++){
				Object[] sf = ((Object[])li.get(i));
				String qty="0";
				if(sf!=null&&sf[1]!=null){
					qty=GlobalsTool.formatNumber(sf[1], false, false, true, "", "", true);
				}
				storingMap.put(sf[0], sf[1]);
			}
			return storingMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap();
	}
	/**
	 * 获得所有采购在途量(未关联销售订单)
	 * @param goodscode
	 * @param fields
	 * @param fieldVals
	 * @param stock
	 * @return
	 */
	public HashMap getAllStoring() {
		StringBuffer sql=new StringBuffer("");
		//sql.append("select cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+BatchNo+'BatchNo'+Hue+'Hue'+Inch+'Inch'+yearNO+'yearNO')) as bigint) as goodPropHash,sum(a.NotInQty) from tblBuyOrderDet a,tblBuyOrder b");
		
		sql.append("select cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+isnull(color,'')+'color')) as bigint) as goodPropHash,sum(a.NotInQty) from tblBuyOrderDet a,tblBuyOrder b");
		
		sql.append(" where a.f_ref=b.id and a.NotInQty>0 and b.workflowNodeName='finish'");
		sql.append(" and (a.stockCode in (select classCode from tblStock where IsMrpOper=1) or len(a.stockCode)=0 or a.stockCode is null)");
		sql.append(" group by a.GoodsCode,isnull(a.color,'')");
		 
		try {
			HashMap storingMap=new HashMap();
			ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
			for(int i=0;li!=null&&i<li.size();i++){
				Object[] sf = ((Object[])li.get(i));
				String qty="0";
				if(sf!=null&&sf[1]!=null){
					qty=GlobalsTool.formatNumber(sf[1], false, false, true, "", "", true);
				}
				storingMap.put(sf[0].toString(), sf[1]);
			}
			return storingMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap();
	}
	/**
	 * 获得所有采购在途量(未关联销售订单)
	 * @param goodscode
	 * @param fields
	 * @param fieldVals
	 * @param stock
	 * @return
	 */
	public HashMap getAllApping() {
		StringBuffer sql=new StringBuffer("");
		//sql.append("select cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+BatchNo+'BatchNo'+Hue+'Hue'+Inch+'Inch'+yearNO+'yearNO')) as bigint) as goodPropHash,sum(a.NoOrderQty) from tblBuyApplicationDet a,tblBuyApplication b");
		sql.append("select cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+isnull(color,'')+'color')) as bigint) as goodPropHash,sum(a.NoOrderQty) from tblBuyApplicationDet a,tblBuyApplication b");
		sql.append(" where a.f_ref=b.id and a.NoOrderQty>0 and b.workflowNodeName='finish'");
		sql.append(" group by a.GoodsCode,isnull(a.color,'')");
		 
		try {
			HashMap storingMap=new HashMap();
			ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
			for(int i=0;li!=null&&i<li.size();i++){
				Object[] sf = ((Object[])li.get(i));
				storingMap.put(sf[0].toString(), sf[1]);
			}
			return storingMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap();
	}
	/**
	 * 获得所有在产量(未关联销售订单)
	 * @param goodscode
	 * @param fields
	 * @param fieldVals
	 * @param stock
	 * @return
	 */
	public HashMap getAllProducting() {
		
		StringBuffer sql=new StringBuffer("");
		//sql.append("select cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+BatchNo+'BatchNo'+Hue+'Hue'+Inch+'Inch'+yearNO+'yearNO')) as bigint) as goodPropHash, sum(a.Qty)-sum(a.CheckQty) from tblProduce a");
		sql.append("select cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+isnull(color,'')+'color')) as bigint) as goodPropHash, sum(a.Qty)-sum(a.inQty) from tblProduceInform a");
		sql.append(" where ISNULL(a.GoodsCode,'')<>'' and a.workflowNodeName='finish'");
		sql.append(" group by a.GoodsCode,isnull(a.color,'')");

		Float f = 0f;// 记录查询出的库存
		try {
			HashMap productMap=new HashMap();
			ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
			for(int i=0;li!=null&&i<li.size();i++){
				Object[] sf = ((Object[])li.get(i));
				String qty="0";
				if(sf!=null&&sf[1]!=null){
					qty=GlobalsTool.formatNumber(sf[1], false, false, true, "", "", true);
				}
				productMap.put(sf[0].toString(), sf[1]);
			}
			return productMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap();
	}
	/**
	 * 获得商品的在产量(未关联销售订单)
	 * @param goodscode
	 * @param fields
	 * @param fieldVals
	 * @param stock
	 * @return
	 */
	public HashMap getProducting(String billType,String detIds) {
		//得到已经启用的商品属性信息
		String propStr="";
		String bomCond="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(!propBean.getPropName().equals("Availably")&&!propBean.getPropName().equals("ProDate")&&propBean.getIsUsed()==1&&
					!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblProduce", propBean.getPropName())!=null){//启用的商品属性
				bomCond+=" and isnull(a."+propBean.getPropName()+",'')=isnull(b."+propBean.getPropName()+",'')";
			}
		}
		
		StringBuffer sql=new StringBuffer("");
		String isUseMrp = (String)mgt.isUseProduce().getRetVal();
		sql.append("select b.id as detId, sum(a.Qty)-sum(a.inQty) from tblProduceInform a");
		sql.append(","+billType+" b where b.id in ("+detIds+") and a.goodsCode=b.goodsCode "+bomCond);
		sql.append(" and ((select count(0) from tblSalesOrderQuantum where charIndex(salesOrderId,a.trackNo)>0)=0 or a.trackNo is null  or len(a.trackNo)=0) and a.workflowNodeName='finish'");
		
		//if(BaseEnv.version==8){//工贸版
			//if(stockCodes.length()>0){
			//	sql.append(" and a.stockCode in ("+stockCodes+")");
			//}else{//如果没有选择仓库,要过滤掉不参与MRP运算的仓库
			//	sql.append(" and (a.stockCode in (select classCode from tblStock where IsMrpOper=1) or len(a.stockCode)=0 or a.stockCode is null) group by b.id");
			//}
		//}
		
		sql.append("  group by b.id");
		
		Float f = 0f;// 记录查询出的库存
		try {
			HashMap productMap=new HashMap();
			ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
			for(int i=0;li!=null&&i<li.size();i++){
				Object[] sf = ((Object[])li.get(i));
				String qty="0";
				if(sf!=null&&sf[1]!=null){
					qty=GlobalsTool.formatNumber(sf[1], false, false, true, "", "", true);
				}
				productMap.put(sf[0], sf[1]);
			}
			return productMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap();
	}

	// 获得生产任务单未出库数量（已分配）
	private Float getProduceNotOutQty(String id,String goodscode, String[] fields,
			String[] fieldVals) {
		goodscode = goodscode.replaceAll("~", "").trim();// 将商品/物料id中的～字符和两边空格去掉
		StringBuffer where = new StringBuffer();// 商品属性值条件
		StringBuffer sql = new StringBuffer();// 要执行的sql语句
		for (int i = 0; i < fields.length; i++) {// 根据启用的商品属性字段生成条件
			if (fieldVals[i] == null) {
				where.append(" and " + fields[i] + " is " + fieldVals[i]);// 如果值为null时
			} else {
				where.append(" and " + fields[i] + "='" + fieldVals[i] + "'");// 不为null时
			}
		}
		sql.append("select sum(notoutqty) from tblproducedet");
		sql.append(" where goodscode='" + goodscode + "' and f_ref<>'"+id+"'");
		sql.append(where);
		sql.append(" group by goodscode");

		Float f = 0f;
		try {// 如果查询结果不正常(如没有结果，或不能转化为数字)，f默认为0.0
			String sf = ((String[]) ((ArrayList) new MrpBPMgt().querySql(sql
					.toString()).retVal).get(0))[0];
			f = Float.parseFloat(sf);
		} catch (Exception e) {

		}
		return f;
	}
	/**
	 * 销售订单未出库20091119
	 * @param id
	 * @param goodscode
	 * @param fields
	 * @param fieldVals
	 * @return
	 */
	public Float gettblSalesOrderNotOutQty(String id,String goodscode, String[] fields,
			String[] fieldVals){
		Float relQty=0f;
		boolean refSalesOrder=false;
		String refBillNO="";
		Float planQty=0f;
		if(!"".equals(id)){
			Result rs=mgt.queryProductPalnSql(id);
			if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&rs.getRetVal()!=null){
				String[]res=(String[])rs.getRetVal();
				if(!res[2].equals("")&&goodscode.equals(res[0])){//如果生产计划单有关联销售订单
					refSalesOrder=true;
					refBillNO=res[2];
					planQty=Float.valueOf(res[1]);
					rs=mgt.queryRelSalesOrderQtySql(res[2], res[0]);
					relQty=Float.valueOf(rs.getRetVal().toString());
				}
			}
		}
		goodscode = goodscode.replaceAll("~", "").trim();// 将商品/物料id中的～字符和两边空格去掉
		StringBuffer where = new StringBuffer();// 商品属性值条件
		StringBuffer sql = new StringBuffer();// 要执行的sql语句
		for (int i = 0; i < fields.length; i++) {// 根据启用的商品属性字段生成条件
			if (fieldVals[i] == null) {
				where.append(" and " + fields[i] + " is " + fieldVals[i]);// 如果值为null时
			} else {
				where.append(" and " + fields[i] + "='" + fieldVals[i] + "'");// 不为null时
			}
		}
		sql.append("select sum(notoutqty) from tblSalesOrderDet");
		sql.append(" where goodscode='" + goodscode + "' and f_ref<>'"+id+"'");
		if(refSalesOrder){
			sql.append(" and f_ref<>(select id from tblSalesOrder where billno='"+refBillNO+"'");
			sql.append(")");//如果生产计划单关联销售订单，排除相关销售订单同商品数量
		}
		sql.append(where);
		sql.append(" group by goodscode");

		Float f = 0f;
		try {// 如果查询结果不正常(如没有结果，或不能转化为数字)，f默认为0.0
			String sf = ((String[]) ((ArrayList) new MrpBPMgt().querySql(sql
					.toString()).retVal).get(0))[0];
			f = Float.parseFloat(sf);
			
		} catch (Exception e) {

		}
		f=relQty+f-planQty;
		if(f<0){
			f=0f;
		}
		return f;
	}

	// 获得委托加工单未出库量
	private Float getEntrustProcessNotOutQty(String goodscode, String[] fields,
			String[] fieldVals,String stock) {
		goodscode = goodscode.replaceAll("~", "").trim();// 将商品/物料id中的～字符和两边空格去掉
		StringBuffer where = new StringBuffer();// 商品属性值条件
		StringBuffer sql = new StringBuffer();// 要执行的sql语句
		for (int i = 0; i < fields.length; i++) {// 根据启用的商品属性字段生成条件
			if (fieldVals[i] == null) {
				where.append(" and " + fields[i] + " is " + fieldVals[i]);// 如果值为null时
			} else {
				where.append(" and " + fields[i] + "='" + fieldVals[i] + "'");// 不为null时
			}
		}
		sql.append("select sum(notoutqty) from  tblEntrustProcessDet");
		sql.append(" where goodscode='" + goodscode + "'");
		sql.append(where);
		sql.append(" group by goodscode");

		Float f = 0f;
		try {// 如果查询结果不正常(如没有结果，或不能转化为数字)，f默认为0.0
			String sf = ((String[]) ((ArrayList) new MrpBPMgt().querySql(sql
					.toString()).retVal).get(0))[0];
			f = Float.parseFloat(sf);
		} catch (Exception e) {
			// ---------------------------------------------------------------
		}
		return f;
	}
	
	/**
	 * 获得有关表的未出库数量20091119
	 * @param tblName
	 * @param goodscode
	 * @param wok_flow_IsStart
	 * @return
	 */
	public float getNotOutQty(String tblName,String goodscode,boolean wok_flow_IsStart){
		
		float notOutQty = 0.0f;
		if(wok_flow_IsStart){//启用了审核流
			Result rs = this.mgt.getNotOutQty(tblName, goodscode);
			if(rs!=null&&rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){//查询成功
				notOutQty = Float.parseFloat(((List<String[]>)rs.getRetVal()).get(0)[0]);
			}
		}
		return notOutQty;
	}

	/**
	 * 有两个string类型数组，如果在arrFirst中存在，在arrSecond中不存在，则返回不存在的字符串以，分隔
	 * 
	 * @param arrFirst
	 * @param arrSecond
	 * @return 如{"a","b","c"}{"a"} 返回 ",b,c"
	 */
	private String getSecondArrNotExit(String[] arrFirst, String[] arrSecond) {

		String notExitStrs = "";// 要返回的字符串
		boolean isExit = false;
		for (String str1 : arrFirst) {
			isExit = false;
			for (String str2 : arrSecond) {
				if (str1.equalsIgnoreCase(str2)) {
					isExit = true;
				}
			}
			if (!isExit) {
				notExitStrs += "," + str1;
			}
		}
		if (notExitStrs.length() == 0) {
			notExitStrs = null;
		}
		return notExitStrs;
	}

}
