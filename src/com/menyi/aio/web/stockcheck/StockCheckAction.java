package com.menyi.aio.web.stockcheck;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.ImportDataBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.importData.*;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:盘点Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-10-17 上午 10:30
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class StockCheckAction extends MgtBaseAction{
	DynDBManager dbmgt = new DynDBManager();
	StockCheckMgt mgt = new StockCheckMgt();
	
	String newDate = "";
	/**
	 * exe 控制器入口函数
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
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		
		DBFieldInfoBean nameField = null;
    	for(DBFieldInfoBean fb : BaseEnv.tableInfos.get("tblStock").getFieldInfos()){
    		if("RowMarker".equals(fb.getFieldSysType())){
    			nameField = fb;
    		}
    	}
    	String StockMarkerName = "StockFullName";
    	if(nameField != null){
    		StockMarkerName = nameField.getFieldName();
    	}
    	request.setAttribute("StockMarkerName", StockMarkerName);
		
		/* 不同操作 */
		String optype = request.getParameter("optype");
		
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			if(optype != null && "importPreCheckBill".equals(optype)){
				//导出模板和导入盘点单前
				forward = importPreCheckBill(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_ADD:
			if(optype != null && "exportTemplet".equals(optype)){
				//导出盘点单或者导出模板
				forward = exportTemplet(mapping, form, request, response);
			}else if(optype != null && "importCheckBill".equals(optype)){
				//导入盘点单
				forward = importCheckBill(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			if(optype != null && "checkDeal".equals(optype)){
				//盘点处理
				forward = checkDeal(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUERY:
			if(optype != null && "stockNext".equals(optype)){
				//查询页面的右边仓库数据
				forward = queryStockNext(mapping, form, request, response);
			}else if(optype != null && "stockPrepare".equals(optype)){
				//盘点准备
				forward = stockPrepare(mapping, form, request, response);
			}else if(optype != null && "stockCancelPrepare".equals(optype)){
				//取消盘点准备
				forward = stockCancelPrepare(mapping, form, request, response);
			}else if(optype != null && "stockCheckDealList".equals(optype)){
				//盘点处理列表
				forward = stockCheckDealList(mapping, form, request, response);
			}else if(optype != null && "delStockCheck".equals(optype)){
				//删除盘点单
				forward = delStockCheck(mapping, form, request, response);
			}else if(optype != null && "dealFormSubmit".equals(optype)){
				//处理调整到自定义列表的连接
				forward = dealFormSubmit(mapping, form, request, response);
			}else{
				//查询操作
				forward = queryStockCheck(mapping, form, request, response);
			}
			break;
		default:
			//查询列表
			forward = queryStockCheck(mapping, form, request, response);
		}
		return forward;
	}
	
	private String replace( String name){
		return  name.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		
	}
	/**
	 * 查询盘点数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryStockCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		DBFieldInfoBean nameField = null;
    	for(DBFieldInfoBean fb : BaseEnv.tableInfos.get("tblStock").getFieldInfos()){
    		if("RowMarker".equals(fb.getFieldSysType())){
    			nameField = fb;
    		}
    	}
    	String fieldName = "StockFullName";
    	if(nameField != null){
    		fieldName = nameField.getFieldName();
    	}
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/StockCheckAction.do");
		/**
		 * 查询数据
		 */
		String folderTree = "[";
		Result rs = mgt.queryAllStock(this.getLoginBean(request), mop);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			List stockList = (ArrayList)rs.retVal;
			int num =0;
			//对数据进行处理把数据组合成节点形式
			folderTree += "{ id:\"0\", pId:-1, name:\"仓库目录\"}";
			if(stockList.size()>0){
				folderTree += ",";
			}
			for(int i=0;i<stockList.size();i++){
				HashMap o = (HashMap)stockList.get(i);
				if(String.valueOf(o.get("classCode")).length() == 5){
					//父节点
					folderTree += "{ id:\""+o.get("classCode")+"\", pId:0, name:\""+replace(String.valueOf(o.get(fieldName)))+"\"}";
				}else{
					String classC = ((String)o.get("classCode")).substring(0, ((String)o.get("classCode")).length()-5);
					folderTree += "{ id:\""+o.get("classCode")+"\", pId:\""+classC+"\", name:\""+replace(String.valueOf(o.get(fieldName)))+"\"}";
				}
				if(num != stockList.size()-1){
					folderTree+=",";
				}
				num ++;
			}
		}
		folderTree += "]";
		request.setAttribute("stockData", folderTree);
		return getForward(request, mapping, "stockCheckList");
	}
	
	/**
	 * 查询页面的右边仓库数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward queryStockNext(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		String searchType = request.getParameter("searchType");							//搜索类型（点击仓库，关键字搜索）
		String searchValue = request.getParameter("searchValue");						//搜索值（仓库classCode,搜索字）
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/StockCheckAction.do");
		//查询数据
		Result rs = mgt.queryStock(searchType, searchValue, this.getLoginBean(request), mop);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			List stockList = (ArrayList)rs.retVal;
			String[] s = new String[]{"id","classCode","StockNumber","StockName","StockFullName","stockPreId","CheckDate","statusId","stockPreStatus","stockInputStatus","stockDealStatus"};
			StringBuffer str = new StringBuffer("[");
			for(int i = 0; i<stockList.size();i++){
				HashMap map = (HashMap)stockList.get(i);
				str.append("{");
				for(int j =0;j<s.length;j++){
					Object o = map.get(s[j])==null?"":map.get(s[j]);
					String so  = GlobalsTool.replaceSpecLitter(o.toString());
					str.append("\""+s[j]+"\": \""+so+"\"");
					if(j<s.length-1){
						str.append(",");
					}
				}
				str.append("}");
				if(i<stockList.size()-1){
					str.append(",");
				}
			}
			str.append("]");
			request.setAttribute("msg", str.toString());
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 盘点准备
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward stockPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		//仓库classCode
		String stockCode = request.getParameter("stockCode");
		LoginBean loginBean = this.getLoginBean(request);
		
		Result rs = mgt.stockPre(stockCode,loginBean.getId(),loginBean.getSunCmpClassCode());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
			request.setAttribute("msg", "OK;");
		}else{
			//存储过程错误
			String errorMessage = rs.getRetVal()==null?"":rs.getRetVal().toString();
			request.setAttribute("msg", "ERROR;"+errorMessage);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 取消盘点准备
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward stockCancelPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		String stockPreId = request.getParameter("stockPreId");				//盘点准备的ID
		String classCode = request.getParameter("classCode");				//仓库classCode
		Result rs = mgt.queryStockFullName(classCode);
		LoginBean lg = getLoginBean(request);
		String stockName = "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			stockName = String.valueOf(rs.getRetVal());
		}
		rs = mgt.stockCancelPre(stockPreId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			new DynDBManager().addLog(14, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "取消仓库盘点准备:"+stockName, "tblStockCheckPrepare", "盘点流程", "取消盘点准备");
			request.setAttribute("msg", "OK");
		}else{
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 盘点处理列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward stockCheckDealList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		//盘点准备单Id
		String stockPreId = request.getParameter("stockPreId");
		//仓库classCode
		String stockCode = request.getParameter("stockCode");
		//盘点日期
		String stockCheckDate= request.getParameter("stockCheckDate");

		String workFlowNodeName = request.getParameter("workFlowNodeName");					//''未过账，finish已过账，all全部
		
		//查询仓库的名称
		Result rs = mgt.queryStockFullName(stockCode);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("stockName", rs.retVal);
		}
		
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		int pageNos = 1;
		int pageSizes = 100;
		if(pageNo != null && !"".equals(pageNo)){
			pageNos = Integer.parseInt(pageNo);
		}
		if(pageSize != null && !"".equals(pageSizes)){
			pageSizes = Integer.parseInt(pageSize);
		}
		
		//查询盘点单的数据
		rs = mgt.stockCheckList(stockPreId ,stockCode, stockCheckDate, workFlowNodeName, pageNos, pageSizes);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List stockCheckList = (ArrayList)rs.retVal;
			request.setAttribute("pageBar", this.pageBar(rs, request));
			request.setAttribute("stockCheckList",stockCheckList);
		}
		request.setAttribute("stockPreId", stockPreId);
		request.setAttribute("stockCode", stockCode);
		request.setAttribute("stockCheckDate", stockCheckDate);
		request.setAttribute("workFlowNodeName", workFlowNodeName);
		return getForward(request, mapping, "stockCheckBillList");
	}
	
	/**
	 * 盘点处理
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward checkDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		String checkType = request.getParameter("checkType");				//盘点方式
		
		String stockCode = request.getParameter("stockCode");				//仓库
		String stockPreId = request.getParameter("stockPreId");				//准备表ID
		String stockCheckDate= request.getParameter("stockCheckDate");		//盘点日期
		
		LoginBean lg = this.getLoginBean(request);
		
		String SCompanyID = lg.getSunCmpClassCode();						//分支机构
		
		//盘点处理
		Result rs = mgt.checkDeal(this.getLocale(request).toString(),SCompanyID,lg.getId(),stockPreId,checkType);
		String url = "/StockCheckAction.do?operation=4&optype=stockCheckDealList&stockCode="+stockCode+"&stockPreId="+stockPreId+"&stockCheckDate="+stockCheckDate;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
			EchoMessage.success().add("盘点处理成功").setBackUrl(url).setAlertRequest(request);
		}else if(rs.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL){
			EchoMessage.error().add("不允许对已月结月份的单据做处理").setBackUrl(url).setAlertRequest(request);
		}else{
			String errorMessage = rs.getRetVal().toString();
			EchoMessage.error().add("盘点处理失败,"+errorMessage).setBackUrl(url).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 删除盘点单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delStockCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String id= request.getParameter("id");			//盘点单Id
		Result rs = mgt.delCheckBill(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", "OK");
		}else if(rs.retCode == ErrorCanst.RET_HAS_AUDITING){
			//无法删除
			request.setAttribute("msg", rs.getRetVal());
		}else{
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 查看按钮进入自定义页面时先跳转再进自定义列表界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward dealFormSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String paramStr = request.getQueryString();
        List list = new ArrayList();
        if(paramStr != null && !"".equals(paramStr)){
        	String[] paramSplit = paramStr.split("&");
        	for(String s : paramSplit){
        		if(s != null && !"".equals(s) && s.indexOf("=")!=-1){
        			HashMap map = new HashMap();
        			String val = URLDecoder.decode(s.split("=")[1],"UTF-8");
        			val  = val == null?"":GlobalsTool.replaceSpecLitter(val);
        			map.put(s.split("=")[0], val);
        			list.add(map);
        		}
        	}
        }
        request.setAttribute("list", list);
        return getForward(request, mapping, "dealForm");
	}
	
	/**
	 * 导入盘点单前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward importPreCheckBill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String stockCode = request.getParameter("stockCode");
		/**
		 * 如果仓库为空，则查询数据库得到仓库等数据
		 */
		Result rs = mgt.queryStockCheckPre(stockCode);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("list", rs.getRetVal());
		}
		request.setAttribute("stockCode", stockCode);
		return getForward(request, mapping, "importCheckBill");
	}
	
	
	/**
	 * 导出模板
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward exportTemplet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String stockPreId = request.getParameter("stockPreId");						//盘点准备表ID
		String stockCode = request.getParameter("stockCode");						//仓库编号
		String impmType = request.getParameter("impmoduleType");						//模板类型（1商品标准模板  2条形码模板  3序列号模板）
		
		String isModule = request.getParameter("isModule");							//是否是导出模板（true）,为空时导出盘点单数据
		
		File file = new File("../../AIOBillExport");
		if(!file.exists()){
			file.mkdirs() ;
		}
		//查询仓库的名称
		Result rs = mgt.queryStockFullName(stockCode);
		String stockName = "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			stockName = String.valueOf(rs.retVal);
			request.setAttribute("stockName", stockName);
		}
		String moduleName = "";
		if("1".equals(impmType)){
			moduleName = "商品";
		}else if("2".equals(impmType)){
			moduleName = "条形码";
		}else if("3".equals(impmType)){
			moduleName = "序列号";
		}
		String fileName = file.getAbsolutePath()+"\\"+stockName+"-"+moduleName+".xls" ;
		
        FileOutputStream fos = new FileOutputStream(fileName);
		ArrayList<ExportField> exportList = new ArrayList<ExportField>();								//保存的字段头部
		ArrayList<HashMap> exportValueList = new ArrayList<HashMap>();									//数据
		Object localeObj = request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (localeObj == null) {
			localeObj = request.getSession().getServletContext().getAttribute("DefaultLocale");
		}
		final String locale = localeObj == null ? "" : localeObj.toString();
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);	//查询所有表结构
		List<DBFieldInfoBean> fieldInfos = ((DBTableInfoBean) allTables.get("tblStockCheckDet")).getFieldInfos();
		DBTableInfoBean mainTable = GlobalsTool.getTableInfoBean("tblStockCheckDet");
		/**
		 * 对要导出的字段进行处理
		 */
		ExportField ef = null;
		if("1".equals(impmType) ){
				//商品
//			tableName,fieldName,reportView,billView,popSel,keyword,popupView
            for (String[] shows : BaseEnv.reportShowSet) { 
				if ("tblGoods".equals(shows[0]) && "1".equals(shows[6]) && "1".equals(shows[3])) {
					ef = new ExportField("main", "", shows[1],0, GlobalsTool.getFieldDisplay(allTables, "tblGoods."+shows[1], locale), mainTable.getDisplay().get(locale),
							false);
					exportList.add(ef);
				}
            }
		}else if("3".equals(impmType)){
			ef = new ExportField("main", "", "Seq",0, GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Seq", locale), mainTable.getDisplay().get(locale),false);
			exportList.add(ef);
		} else if("2".equals(impmType)){
			ef = new ExportField("main", "", "BarCode",0, GlobalsTool.getFieldDisplay(allTables, "tblGoods.BarCode", locale), mainTable.getDisplay().get(locale)+" (库存数仅做参考可以不填,盘点数可以不填默认为1)",false);
			exportList.add(ef);
		}
		//序列号盘点，有有序列号，没有其它任何信息
		if(!"3".equals(impmType)){
			for (DBFieldInfoBean row : fieldInfos) {
				if("GoodsField".equals(row.getFieldSysType()) && row.getInputType() != DBFieldInfoBean.INPUT_HIDDEN && !row.getFieldName().equals("Seq") ){
					//商品属性
					ef = new ExportField("main", "", row.getFieldName(),0, row.getDisplay().get(locale), mainTable.getDisplay().get(locale),
							row.getFieldType()==DBFieldInfoBean.FIELD_INT||row.getFieldType()==DBFieldInfoBean.FIELD_DOUBLE?true:false);
					exportList.add(ef);
				}
			}
			
			ef = new ExportField("main", "", "lastQty",0, GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.LastQty", locale.toString()), mainTable.getDisplay().get(locale),false);
			exportList.add(ef);
			ef = new ExportField("main", "", "Qty",0, GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Qty", locale.toString()), mainTable.getDisplay().get(locale),false);
			exportList.add(ef);
		}
		/**
		 * 查询数据
		 */
		rs = mgt.queryStocksCheckGoodsId(stockPreId, Integer.parseInt(impmType));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			exportValueList = (ArrayList)rs.retVal;	
		}
		
		Result result = ExportData.billExport(fos, "盘点单", exportList, exportValueList,stockName+"-"+moduleName);
		fos.close();
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			if(isModule != null && "true".equals(isModule)){
				//导出盘点单模板
				String downUrl = "/ReadFile?tempFile=export&fileName="+GlobalsTool.encode(stockName+"-"+moduleName+".xls");
				request.setAttribute("msg", downUrl);
				return getForward(request, mapping, "blank");
			}
		} else {
			request.setAttribute("msg", "导出失败"+result.getRetVal());
			return getForward(request, mapping, "blank");
		}
		request.setAttribute("msg", "导出失败"+result.getRetVal());
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * 导入盘点单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward importCheckBill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		Locale locale = this.getLocale(request);
		String importName = request.getParameter("importName");							//得到表名tblStockCheckDet
		String impmType = request.getParameter("impmoduleType");							//选择的模板
		String stockCodes = request.getParameter("stockCode");							//仓库
		String urls = "/StockCheckAction.do?operation=6&optype=importPreCheckBill&stockCode="+stockCodes;
		/**
		 * 则查询数据库得到仓库等数据
		 */
		
		
		Result rs = mgt.queryStockCheckPre(stockCodes);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("list", rs.getRetVal());
		}
		request.setAttribute("stockCode", stockCodes);
		String newDates= request.getParameter("newDate");
		if(!"".equals(newDate) && newDate.equals(newDates)){
			return mapping.findForward("importCheckBill");
		}
		newDate = newDates;
		Hashtable<String, DBTableInfoBean> allTables = (Hashtable<String, DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/StockCheckAction.do");
		//得到表结构
		DBTableInfoBean tableInfoBean = allTables.get(importName);
		String mainTableDisplay = tableInfoBean.getDisplay().get(locale.toString()); //记录主表的中文名字
		try{
			//导入模板名称
	        ImportForm importForm = (ImportForm) form;
	        JXLTOOL jxlTool = new JXLTOOL(importForm.getFileName());
	        Result exceldatars = jxlTool.getExcelData(true);
	        if (exceldatars.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
	        	//读取数据成功
	        	//检查模板中表名否正确		        
				
				ArrayList<DBFieldInfoBean> fieldInfoList = new ArrayList<DBFieldInfoBean>();
				
				fieldInfoList.addAll(tableInfoBean.getFieldInfos());
				List<HashMap<String, ExcelFieldInfoBean>> list = (List<HashMap<String, ExcelFieldInfoBean>>) exceldatars.getRetVal();
				jxlTool.close();
				
				//把主表数据和明细数据存储到values中
				HashMap values = new HashMap();
				/**
				 * 根据仓库查询仓库准备表数据
				 */
				
				rs = mgt.queryStockCheckName(stockCodes);
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.error().add("请检查导入模板仓库是否正确！").setBackUrl(urls).setAlertRequest(request);
					return mapping.findForward("alert");
				}else{
					HashMap stockMap = (HashMap)rs.getRetVal();
					if(stockMap == null || stockMap.size()==0){
						EchoMessage.error().add("请检查仓库是否是已准备状态！").setBackUrl(urls).setAlertRequest(request);
						return mapping.findForward("alert");
					}else{
						values = stockMap;
					}
				}
				
				/**
				 * 导入处理
				 */
				int success = 0;			//成功条数
				rs = importDataDeal(values,request, tableInfoBean, list, jxlTool, mainTableDisplay, locale, mop, this.getLoginBean(request),Integer.parseInt(impmType));
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					String[] sr  = (String[])rs.retVal;
					request.setAttribute("fileName", sr[3]);
					request.setAttribute("totalimport", sr[0]);
					request.setAttribute("successimport", sr[1]);
					request.setAttribute("errorimport", sr[2]);
					request.setAttribute("stockCode", stockCodes);
					return mapping.findForward("importResult");
				}else{
					if(rs.getRetVal() == null){
						EchoMessage.error().add(getMessage(request, "import.failure.number")).setAlertRequest(request);
					}else if(rs.getRetVal() instanceof String[]){
						String[] str = (String[]) rs.getRetVal();
						String msg = new DynDBManager().getDefSQLMsg(locale.toString(), str[0]);
						EchoMessage.error().add(msg).setBackUrl(urls).setAlertRequest(request);
					}else{
						EchoMessage.error().add(rs.getRetVal().toString()).setBackUrl(urls).setAlertRequest(request);
					}
					return mapping.findForward("alert");
				}
	        } else {
				jxlTool.close();
				// 弹出错误信息,读取数据失败
				EchoMessage.error().add(GlobalsTool.getMessage(locale, "excel.lb.readerror")).setBackUrl(urls).setAlertRequest(request);
				return mapping.findForward("alert");
			}
		}catch (Exception e) {
			EchoMessage.error().add(GlobalsTool.getMessage(locale, "common.msg.error")).setBackUrl(urls).setAlertRequest(request);
			return mapping.findForward("alert");
		}
	}
	

	
	private void rowErrorHandler(String msg, HashMap<String, ExcelFieldInfoBean> groupItem,List<HashMap<String, ExcelFieldInfoBean>> errorList) {
		ExcelFieldInfoBean eBean = new ExcelFieldInfoBean();
		eBean.setValue(msg);
		groupItem.put("Error", eBean);
		errorList.add(groupItem);
	}
	
	
	
	/**
	 * 数据处理（添加数据）
	 * @param values                保存的值
	 * @param request               request
	 * @param allTables             所有表数据
	 * @param list                  导入的数据
	 * @param jxlTool				excel总的数据
	 * @param mainTableDisplay		主表中文名称
	 * @param locale                多语言名称
	 * @param mop					权限
	 * @param lg					登陆信息
	 * @param impmType			模板类型
	 */
	public Result importDataDeal(HashMap values,HttpServletRequest request, DBTableInfoBean tableInfoBean,List<HashMap<String, ExcelFieldInfoBean>> list,JXLTOOL jxlTool, 
			String mainTableDisplay, Locale locale,MOperation mop,LoginBean lg,int impmType){
		
		//记录导入失败的数据
		List<HashMap<String, ExcelFieldInfoBean>> errorList = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
		Result rs = new Result();
		
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);	//查询所有表结构
		
		//会计期间
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		
		values.put("Period", accPeriodBean.getAccPeriod());
		values.put("PeriodMonth", accPeriodBean.getAccMonth());
		values.put("PeriodYear", accPeriodBean.getAccYear());
		
		
		List rowList = new ArrayList();						//保存的所有字段
		if(impmType==1){
            for (String[] shows : BaseEnv.reportShowSet) { 
				if ("tblGoods".equals(shows[0]) && "1".equals(shows[6]) && "1".equals(shows[3])) {
					rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblGoods."+shows[1], locale.toString())+";"+shows[1]);					
				}
            }
			for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
				if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
					rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet."+gpBean.getPropName(), locale.toString())+ ";"+gpBean.getPropName());
				}
			}
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.LastQty", locale.toString())+";LastQty");
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Qty", locale.toString())+";Qty");
		}else if(impmType == 3){
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Seq", locale.toString())+";Seq");
		} else if(impmType == 2){
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblGoods.BarCode", locale.toString())+";BarCode");
			
			for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
				if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
					rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet."+gpBean.getPropName(), locale.toString())+ ";"+gpBean.getPropName());
				}
			}
			
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Qty", locale.toString())+";Qty");
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.LastQty", locale.toString())+";LastQty");
		}
		
		
		Object ob = request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		ImportDataBean importBean = new ImportDataBean();
		importBean.setName(mainTableDisplay);
		
		ArrayList childList = new ArrayList();
		values.put("TABLENAME_tblStockCheckDet", childList);
		for (HashMap<String, ExcelFieldInfoBean> excelMap : list) {
			//设置明细行数据
			List<HashMap<String, ExcelFieldInfoBean>> group = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
			group.add(excelMap);
			HashMap fieldVal = new HashMap();
			for(int i=0;i<rowList.size();i++){
				String field = (String)rowList.get(i);
				String excelValue = excelMap.get(field.split(";")[0])==null?"":excelMap.get(field.split(";")[0]).getValue();
				fieldVal.put(field.split(";")[1], excelValue);
			}
			if(impmType == 3 && fieldVal.get("Seq") != null && !fieldVal.get("Seq").equals("")){
				//跟据序列号，从数据库中查询相关的商品，属性
				Result rr = mgt.checkSeq((String)fieldVal.get("Seq"), childList, (String)values.get("PrepareId"), (String)values.get("StockCode"));
				if(rr.retCode != ErrorCanst.DEFAULT_SUCCESS){
					rowErrorHandler((String)rr.getRetVal(),excelMap,errorList);
				}
			}else if(impmType == 2 ) {
				//验证商品是否存在				
				if(fieldVal.get("LastQty") == null || "".equals(fieldVal.get("LastQty")) ||
						!Pattern.compile("[0-9]*").matcher(fieldVal.get("LastQty").toString()).matches()){
					fieldVal.put("LastQty","0");
				}
				//当一行的标识不为空并且盘点数量不为空时，进行导入
				if(fieldVal.get("BarCode")!= null && !"".equals(fieldVal.get("BarCode"))){
					if(fieldVal.get("Qty") != null && !"".equals(fieldVal.get("Qty"))){
						//if(!Pattern.compile("[0-9]*").matcher(fieldVal.get("Qty").toString()).matches()){
						//	groupErrorHandler("盘点数量必须为正整数！", group, errorList);
						if(!Pattern.compile("^\\d+(\\.\\d+)?$").matcher(fieldVal.get("Qty").toString()).matches()){
							groupErrorHandler("盘点数量必须为数字！", group, errorList);
							continue;
						}
					}else{ //可以为空
						fieldVal.put("Qty","1");
					}
				}
				rs = mgt.exportValidate(fieldVal, impmType, (String)values.get("PrepareId"));
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){ 
						groupErrorHandler(""+rs.retVal, group, errorList);
						continue;
				}
				fieldVal.put("GoodsCode", rs.getRetVal());
				childList.add(fieldVal);
			}else{
				//验证商品是否存在
				
				if(fieldVal.get("LastQty") == null || "".equals(fieldVal.get("LastQty")) ||
						!Pattern.compile("[0-9]*").matcher(fieldVal.get("LastQty").toString()).matches()){
					fieldVal.put("LastQty","0");
				}
				//当一行的标识不为空并且盘点数量不为空时，进行导入
				if(fieldVal.get("Qty") != null && !"".equals(fieldVal.get("Qty"))){
					//if(!Pattern.compile("[0-9]*").matcher(fieldVal.get("Qty").toString()).matches()){
					if(!Pattern.compile("^\\d+(\\.\\d+)?$").matcher(fieldVal.get("Qty").toString()).matches()){
						groupErrorHandler("盘点数量必须为数字！", group, errorList);
						continue;
					}
				}else{ //可以为空
					groupErrorHandler("盘点数量不能为空！", group, errorList);
					continue;
				}
				
				rs = mgt.exportValidate(fieldVal, impmType, (String)values.get("PrepareId"));
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){ 
						groupErrorHandler(""+rs.retVal, group, errorList);
						continue;
				}
				fieldVal.put("GoodsCode", rs.getRetVal());
				childList.add(fieldVal);
			}
		}
		
		if(impmType == 3){
			//查各商品的盘点库存数
			mgt.inputLastQty(childList, (String)values.get("PrepareId"));
		}
		
		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		
		/* 添加盘点单数据 */
		Result result = mgt.addStockCheck(values, lg,(DBTableInfoBean)map.get("tblStockCheck"), locale, resources, map,request);
		rs.setRetCode(result.getRetCode());
		rs.setRetVal(result.getRetVal());
		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}else{
			String fileName = "";
			if(errorList.size() >0){
				fileName = "../log/stockCheck_" + lg.getId() + ".xls";
				File file = new File(fileName);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				String error = GlobalsTool.getMessage(locale, "excel.error.title");
				jxlTool.writeExcel(fileName, errorList, error);
			}
			rs.retVal = new String[]{""+list.size(),""+(list.size()-errorList.size()),""+errorList.size(),fileName};
		}
		
		return rs;
	}
	
	/**
	 * 失败
	 * @param importState
	 * @param errorCode
	 * @param errorValue
	 * @param group
	 * @param errorList
	 * @param allTables
	 * @param isAutoGoods
	 */
	private void groupErrorHandler(String errorValue, List<HashMap<String, ExcelFieldInfoBean>> group, List<HashMap<String, ExcelFieldInfoBean>> errorList) {
		errorList.addAll(group);
		if (group.get(0).get("Error") == null) {
			ExcelFieldInfoBean eBean = new ExcelFieldInfoBean();
			eBean.setValue(errorValue);
			group.get(0).put("Error", eBean);
		}
	}
}
