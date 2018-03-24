package com.menyi.aio.web.finance.voucher;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.*;

import org.apache.batik.dom.util.HashTable;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.customize.*;
import com.menyi.aio.web.importData.ExportField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:凭证Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-03-13 上午 11:30
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class VoucherAction extends MgtBaseAction {

	VoucherMgt mgt = new VoucherMgt();

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
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;

		String optype = request.getParameter("optype");

		// 问题：所有汉字改为多语言
		/*
		 * 注意：
		 * 表tblAccMain字段isAuditing代表是否审核（start未审核、finish已审核、noPass审核不通过）,workFlowNodeName代表是否过账（notApprove未过账、finish已过账）
		 */
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			// 增加前
			forward = addVoucherPre(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			if (optype != null && "addModule".equals(optype)) {
				// 增加凭证模板
				forward = addModule(mapping, form, request, response);
			} else {
				// 增加凭证或者增加时打印凭证
				forward = addVoucher(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			// 修改凭证前
			forward = updateVoucherPre(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			// 修改凭证
			forward = updateVoucher(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			if (optype != null && "exportVoucher".equals(optype)) {
				// 导出列表
				forward = exportVoucher(mapping, form, request, response);
			} else if (optype != null && "voucherModule".equals(optype)) {
				// 查询凭证模板
				forward = voucherModuleList(mapping, form, request, response);
			} else {
				// 查询列表
				forward = queryVoucher(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DELETE:
			if (optype != null && "deleteModule".equals(optype)) {
				// 删除凭证模板
				forward = deleteModule(mapping, form, request, response);
			} else {
				// 删除凭证
				forward = deleteVoucher(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DETAIL:
			// 凭证详情
			forward = detailVoucher(mapping, form, request, response);
			break;
		default:

			if (optype != null && !"".equals(optype)) {
				if (optype.equals("queryRecordComment")) {
					// 查询摘要库
					forward = queryRecordComment(mapping, form, request, response);
				} else if(optype.equals("queryDescription")){
					//录入摘要时进行查询匹配数据下拉显示在文本框下面的div中
					forward = queryDescription(mapping, form, request, response);
				} else if (optype.equals("addRecordComment")) {
					// 添加摘要
					forward = addRecordComment(mapping, form, request, response);
				} else if (optype.equals("delRecordComment")) {
					// 删除摘要
					forward = delRecordComment(mapping, form, request, response);
				} else if (optype.equals("queryAccCode")) {
					// 查询会计科目(弹出框)
					forward = queryAccCode(mapping, form, request, response);
				} else if (optype.equals("queryAccTemp")) {
					// 查询会计科目外币
					forward = queryAccTemp(mapping, form, request, response);
				} else if (optype.equals("querySetExchange")) {
					// 根据币种查询汇率
					forward = querySetExchange(mapping, form, request, response);
				} else if (optype.equals("queryComputeItem")) {
					// 根据会计科目查询核算项
					forward = queryComputeItem(mapping, form, request, response);
				} else if (optype.equals("approveRemarkPre")) {
					// 审核不通过时输入批注前
					forward = addRemarkPre(mapping, form, request, response);
				} else if (optype.equals("dealapprov")) {
					// 审核通过、审核不通过、反审核
					forward = dealapprov(mapping, form, request, response);
				} else if (optype.equals("dealbinder")) {
					// 过账，反过账
					forward = dealBinder(mapping, form, request, response);
				} else if (optype.equals("binderGuidePre")) {
					// 过账向导前
					forward = bindGuidePre(mapping, form, request, response);
				} else if (optype.equals("batchbinder")) {
					// 根据过账向导进行过账
					forward = batchbinder(mapping, form, request, response);
				} else if (optype.equals("AuditeGuidePre")) {
					// 审核向导前
					forward = AuditeGuidePre(mapping, form, request, response);
				} else if (optype.equals("batchAudite")) {
					// 根据审核向导进行审核
					forward = batchAudite(mapping, form, request, response);
				} else if (optype.equals("dealcheck")) {
					// 复核，反复核
					forward = dealcheck(mapping, form, request, response);
				} else if (optype.equals("stopCredNoList")) {
					// 断号查询
					forward = stopCredNoList(mapping, form, request, response);
				} else if (optype.equals("resetCredNoPre")) {
					// 凭证整理前
					forward = resetCredNoPre(mapping, form, request, response);
				} else if (optype.equals("resetCredNo")) {
					// 凭证整理
					forward = resetCredNo(mapping, form, request, response);
				}else if (optype.equals("saveToAccTemp")) {
					// 另存模板
					forward = saveToAccTemp(mapping, form, request, response);
				}
			} else {
				// 查询列表
				forward = queryVoucher(mapping, form, request, response);
			}
		}
		return forward;
	}

	/**
	 * 凭证录入前
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addVoucherPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		/* 查询启用的会计期间 */
		LoginBean loginBean = this.getLoginBean(request);
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		request.setAttribute("isEspecial", request.getParameter("isEspecial"));
		
		//*****根据当前会计期间设置凭证日期*****//
		if(accPeriodBean != null){
			Calendar cal = Calendar.getInstance();
	        //设置年份
	        cal.set(Calendar.YEAR,accPeriodBean.getAccYear());
	        //设置月份
	        cal.set(Calendar.MONTH, accPeriodBean.getAccMonth()-1);
	        //获取某月最大天数
	        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        //设置日历中月份的最大天数
	        cal.set(Calendar.DAY_OF_MONTH, lastDay);
	        //格式化日期
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String accDate = sdf.format(cal.getTime());	
	        request.setAttribute("accDate", accDate);
		}
		
		//***********end**********//
		
		/* 查询最小和最大期间用于在页面上验证期间 */
		Result rs = mgt.queryMaxPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("periodStr", rs.retVal);
		}
		
		/* 查询现金流量科目主项目 和 副项目  */
		Result mItems = mgt.queryMItems();
		if(mItems.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("mItems", new Gson().toJson(mItems.retVal));
		} else{
			request.setAttribute("mItems", "[]");
		}
		
		Result sItems = mgt.querySItems();
		if(sItems.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("sItems", new Gson().toJson(sItems.retVal));
		} else{
			request.setAttribute("sItems", "[]");
		}
	
			
		rs = mgt.queryAccPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("accPeriod", rs.retVal);
		}
		/* 凭证模板 */
		String moduleId = request.getParameter("moduleId");
		if (moduleId != null && !"".equals(moduleId)) {
			/* 存在模板 */
			rs = mgt.queryAccMainTempleteId(moduleId);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (ArrayList) rs.retVal;
				if (list == null || list.size() == 0) {
					request.setAttribute("moduleId", "");
					return getForward(request, mapping, "addVoucher");
				}
			}
			boolean isLastSunCompany = Boolean.parseBoolean(hashSession.get("IsLastSCompany").toString());

			Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			String sunClassCode = loginBean.getSunCmpClassCode(); // 从LoginBean中取出分支机构的classCode

			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);

			/* 用户权限 */
			MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/VoucherManageAction.do"));

			/* 查询详情 */
			rs = new DynDBManager().detail("tblAccMainTemplete", map, moduleId, sunClassCode, props, loginBean.getId(), isLastSunCompany,  "");
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				/* 查询成功 */
				request.setAttribute("values", rs.retVal);
			}
		} else {
			request.setAttribute("startTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
		}

		return getForward(request, mapping, "addVoucher");
	}

	/**
	 * 查询摘要库列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryRecordComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		/* 查询摘要库信息 */
		String searchValue = request.getParameter("searchValue");

		try {
			/* 处理URL中存在文字 */
			searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/* 根据条件查询数据 */
		Result result = mgt.queryRecord(searchValue);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("RecordList", result.retVal);
		}
		request.setAttribute("searchValue", searchValue);
		return getForward(request, mapping, "queryRecord");
	}

	/**
	 * 根据输入的摘要查询数据库中匹配的数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryDescription(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String description = request.getParameter("description");
		try {
			description = java.net.URLDecoder.decode(description, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Result result = mgt.queryRecord(description);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<String[]> list = (ArrayList<String[]>)result.retVal;
			String value = "";
			for(String[] str : list){
				value += str[0]+","+str[1]+";|";
			}
			request.setAttribute("msg", value);
		}
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * 添加摘要
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addRecordComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String value = request.getParameter("value");
		LoginBean lg = this.getLoginBean(request);
		// 添加
		Result result = mgt.addRecord(value, lg.getId());
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			new DynDBManager().addLog(0, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"添加凭证摘要:"+GlobalsTool.toChinseChar(value)+";","tblRecordComment", "凭证管理","");
			request.setAttribute("msg", "OK");
		} else {
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 删除摘要
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delRecordComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoginBean lg = getLoginBean(request);
		String id = request.getParameter("id");
		// 删除
		Result result = mgt.queryRecordId(id);
		String name = String.valueOf(result.getRetVal());
		
		result = mgt.delRecord(id);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			new DynDBManager().addLog(2, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"删除凭证摘要:"+name+";","tblRecordComment", "凭证管理","");
			request.setAttribute("msg", "OK");
		} else {
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 查询会计科目
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryAccCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String classCode = request.getParameter("classCode"); // 科目classCode
		String keywordSearch = request.getParameter("keywordSearch"); // 关键字
		String accCodeSearch = request.getParameter("accCodeSearch"); // 科目号
		String accNameSearch = request.getParameter("accNameSearch"); // 科目名称
		String accFullNameSearch = request.getParameter("accFullNameSearch"); // 科目全称
		String selectType = request.getParameter("selectType"); // 搜索类型（chooseChild只可以选择最下级或者，all全部）
		String isParent = request.getParameter("isParent");

		ArrayList allScopeRightList = this.getLoginBean(request).getAllScopeRight();

		Result rs = mgt.queryAcc(classCode, keywordSearch, accCodeSearch, accNameSearch, accFullNameSearch, allScopeRightList);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 查询成功
			request.setAttribute("accMainList", rs.retVal);
			ArrayList list = (ArrayList) rs.retVal;
			String folderTree = "[";
			for (int i = 0; i < list.size(); i++) {
				HashMap map = (HashMap) list.get(i);
				String classCodes = String.valueOf(map.get("classCode"));
				if (folderTree.indexOf("id:\"" + classCodes.substring(0, classCodes.length() - 5) + "\"") == -1) {
					folderTree += this.folderTree(list, String.valueOf(map.get("AccNumber")), String.valueOf(map.get("AccFullName")), classCodes, Integer
							.valueOf(map.get("counts").toString()), selectType);
				}
			}
			folderTree = folderTree.replaceAll("\\}\\{", "},{");
			folderTree += "]";
			request.setAttribute("folderTree", folderTree);
			request.setAttribute("selectType", selectType);
		}
		String strValue = request.getParameter("strValue");
		try {
			/* 处理URL中存在文字 */
			if (strValue != null) {
				strValue = java.net.URLDecoder.decode(strValue, "UTF-8");
			}
			request.setAttribute("strValue", strValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setAttribute("isParent", isParent);
		request.setAttribute("classCode", classCode);
		request.setAttribute("keywordSearch", keywordSearch);
		request.setAttribute("accCodeSearch", accCodeSearch);
		request.setAttribute("accNameSearch", accNameSearch);
		request.setAttribute("accFullNameSearch", accFullNameSearch);
		return getForward(request, mapping, "accMainList");
	}

	private String folderTree(ArrayList list, String accNumber, String folderName, String classCode, Integer isCatalog, String selectType) {
		String folderTree = "";
		folderName = folderName.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		if (isCatalog != null && isCatalog > 0) {
			// 存在下级
			folderTree += "{ id:\"" + classCode + "\",tname:\"" + accNumber + ";" + folderName + ";|" + "\",name:\"" + accNumber + " - " + folderName
					+ "\",nocheck:true,nodes:[";
			for (int i = 0; i < list.size(); i++) {
				HashMap map = (HashMap) list.get(i);
				String classCodes = String.valueOf(map.get("classCode"));
				if (classCodes.substring(0, classCodes.length() - 5).equals(classCode)) {
					folderTree += this.folderTree(list, String.valueOf(map.get("AccNumber")), String.valueOf(map.get("AccFullName")), classCodes, Integer
							.valueOf(map.get("counts").toString()), selectType);
				}
			}
			folderTree += "]}";
		} else {
			folderTree += "{ id:\"" + classCode + "\",tname:\"" + accNumber + ";" + folderName + ";|" + "\",name:\"" + accNumber + " - " + folderName
					+ "\",nodes: []";
			if ("all".equals(selectType)) {
				folderTree += ",nocheck:true";
			}
			folderTree += "}";
		}
		return folderTree;
	}

	/**
	 * 根据科目代码查询该科目外币
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryAccTemp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		// 会计科目代码
		String accCode = request.getParameter("accCode");
		String queryMode = request.getParameter("queryMode");
		String json = "{";

		/* 根据科目代码查询币种 */
		Result result = mgt.queryCurrency(accCode, queryMode);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			json += "\"currency\":" + result.retVal;
		}

		json += "}";
		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 根据科目代码查询该科目下核算项
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryComputeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		// 会计科目代码
		String accCode = request.getParameter("accCode");
		String queryMode = request.getParameter("queryMode");
		String json = "{";

		/* 根据会计科目查询核算项 */

		Result result = mgt.queryIsBreed(accCode, queryMode);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			json += "\"isbreed\":" + result.retVal;
		}
		json += "}";

		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 根据外币id查询汇率
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward querySetExchange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoginBean loginBean = this.getLoginBean(request);
//		// 会计期间
//		Hashtable sessionSet = BaseEnv.sessionSet;
//		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
//		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		
		String exchange = request.getParameter("exchange");
		String newYearPeriod = request.getParameter("newYearPeriod");
		String newPeriod = request.getParameter("newPeriod");
		if(newYearPeriod == null || "".equals(newYearPeriod)){
			newYearPeriod = "0";
		}
		if(newPeriod == null || "".equals(newPeriod)){
			newPeriod = "0";
		}
		AccPeriodBean accPeriodBean = new AccPeriodBean();
		accPeriodBean.setAccYear(Integer.parseInt(newYearPeriod));
		accPeriodBean.setAccPeriod(Integer.parseInt(newPeriod));
		Result result = mgt.queryExchange(exchange, accPeriodBean);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", result.retVal == null ? "" : result.retVal);
		} else {
			request.setAttribute("msg", "");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 添加凭证
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoginBean loginBean = this.getLoginBean(request);

		/* 附件 */
		String attachment = request.getParameter("attachFiles");

		String parentCode = request.getParameter("parentCode");
		String moduleType = getParameter("moduleType", request);

		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		/* 表结构信息 */
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, "tblAccMain");

		HashMap values = new HashMap();

		/* 取主表数据 */
		readMainTable(values, tableInfo, request);
		LoginBean lg = getLoginBean(request);

		/* 获取子表中数据 */
		List childTableList = DDLOperation.getChildTables("tblAccMain", map);
		if (childTableList != null && childTableList.size() > 0) {
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
				readChildTable(values, childTable, request, lg.getSunCmpClassCode());
			}
		}

		/* 保存方式 */
		String saveType = request.getParameter("button");
		if ("printSave".equals(saveType)) {
			values.put("isAuditing", "print");
			request.setAttribute("saveType", saveType);
		} else {
			values.put("isAuditing", "start");
		}

		values.put("RefBillType", "tblAccMain");
		values.put("createBy", loginBean.getId());
		values.put("lastUpdateBy", loginBean.getId());
		values.put("createTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("workFlowNode", "0");
		values.put("SCompanyID", "00001");
		values.put("Attachment", attachment);
		values.put("workFlowNodeName", "notApprove");
		values.put("AutoBillMarker", 0);

		// 设置主表的ID
		String id = (String) values.get("id");
		if (id == null || id.trim().length() == 0) {
			id = IDGenerater.getId();
			values.put("id", id);
		}

		// 会计期间
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");

		Result result = new Result();

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		/* 删除del的文件 */
		String delFiles = request.getParameter("delFiles");
		if (delFiles != null && !"".equals(delFiles)) {
			String[] delFile = delFiles.split(";");
			for (int i = 0; delFile != null && i < delFile.length; i++) {
				FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, delFile[i]);
			}
		}

		/* 保存主表和子表的数据 */
		result = mgt.addAccMain("tblAccMain", map, values, id, lg, "", resources, this.getLocale(request), saveType);
		ActionForward forward = getForward(request, mapping, "alert");
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS || result.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
			if ("printSave".equals(saveType)) {
				request.setAttribute("BillId", values.get("id"));
				Result print = new ReportSetMgt().getBillTable("tblAccMain", moduleType);
				if (print.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					if (print.getRealTotal() > 0) {
						List list = (List) print.getRetVal();
						ReportsBean report = (ReportsBean) list.get(0);
						Map det = report.getReportDetBean();
						Collection con = det.values();
						Iterator iter = con.iterator();
						while (iter.hasNext()) {
							ReportsDetBean detBean = (ReportsDetBean) iter.next();
							if (detBean.getNewFlag().equals("OLD")) {
								request.setAttribute("print", "true");
								break;
							}
						}
						request.setAttribute("BillRepNumber", report.getReportNumber());
					}
				}
			}
			String message = getMessage(request, "common.msg.addSuccess");
			EchoMessage echo = null;
			if ("printSave".equals(saveType)) {
				echo = EchoMessage.success().add(message).setBackUrl(
						"/UserFunctionQueryAction.do?tableName=tblAccMain" + "&operation=" + OperationConst.OP_QUERY + "&parentCode=" + parentCode
								+ "&parentTableName=&f_brother=&checkTab=Y" + "&moduleType=" + moduleType + "&winCurIndex="
								+ request.getParameter("winCurIndex")+"&popWinName="+request.getParameter("popWinName"));
			} else {
				result = mgt.queryAccMain(values.get("id").toString());
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					/*添加系统日志*/
					HashMap tblAccMainMap = (HashMap)result.getRetVal();
					new DynDBManager().addLog(0, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"添加凭证:"+tblAccMainMap.get("CredTypeID")+"-"+tblAccMainMap.get("OrderNo"),"tblAccMain", "凭证管理","");
				}
				/* 保存成功 */
				String isEspecial = request.getParameter("isEspecial");
				String optype = request.getParameter("optype");
				if(optype!=null && "saveOrAdd".equals(optype)){
					echo = EchoMessage.success().add(message).setBackUrl("/VoucherManage.do?operation=6&popWinName="+request.getParameter("popWinName"));
				}else{
					echo = EchoMessage.success().add(message).setBackUrl("/VoucherManageAction.do?operation=5&tableName=tblAccMain&id="+id+"&number=&popWinName="+request.getParameter("popWinName"));
				}
			}
			if (echo != null) {
				echo.setAlertRequest(request);
			}
		} else {
			/* 保存失败 */
			// 删除正式文件
			if (attachment != null && !"".equals(attachment)) {
				String[] uploadaffix = attachment.split(";");
				for (int i = 0; uploadaffix != null && i < uploadaffix.length; i++) {
					FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, uploadaffix[i]);
				}
			}
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(result, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
		}
		return forward;
	}

	/**
	 * 修改凭证前
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateVoucherPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		/* 凭证id */
		String keyId = request.getParameter("id");
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");

		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());

		/* 查询最小和最大期间用于在页面上验证期间 */
		Result rs = mgt.queryPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("periodStr", rs.retVal);
		}
		
		rs = mgt.queryAccPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("accPeriod", rs.retVal);
		}
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = lg.getSunCmpClassCode(); // 从LoginBean中取出分支机构的classCode

		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);

		/* 用户权限 */
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/VoucherManageAction.do"));

		/* 加载数据 */
		rs = new DynDBManager().detail("tblAccMain", map, keyId, sunClassCode, props, lg.getId(), isLastSunCompany,
				"");

		boolean refFlag = true;
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 加载成功
			request.setAttribute("values", rs.retVal);
			HashMap hm = (HashMap) rs.retVal;
			if (hm == null || hm.get("id") == null) {
				// 未找到记录
				EchoMessage.error().add(getMessage(request, "oaJob.not.find")).setRequest(request);
				return getForward(request, mapping, "message");
			}
			//*****判断是否关联单据生成凭证
			String RefBillType = (String)hm.get("RefBillType");
			String RefBillNo = (String)hm.get("RefBillNo");
			if(RefBillType != null && !"tblAccMain".equals(RefBillType) && RefBillNo != null && !"".equals(RefBillNo)){
				refFlag = false;
			}
			String number = request.getParameter("number");
			int num = 0;
			if (number != null && !"".equals(number)) {
				num = Integer.valueOf(number);
			}
			String isEspecial = request.getParameter("isEspecial");
			if (!"1".equals(isEspecial)) {
				/* 取到保存到内存中的列表 */
				HashTable voucherListHash = (HashTable) request.getSession().getAttribute("voucherList");
				List list = (ArrayList) voucherListHash.get(lg.getId());
				if (list != null && list.size() > 0) {
					/* 第一条 */
					HashMap hashmap = (HashMap) list.get(0);
					request.setAttribute("firstId", hashmap.get("id"));
					request.setAttribute("firstNumber", hashmap.get("counts"));
					/* 上一条 */
					int tempnum = num;
					if (tempnum > 1) {
						tempnum = tempnum - 1;
					} else {
						tempnum = 0;
					}
					for (int i = tempnum; i > 0; i--) {
						hashmap = (HashMap) list.get(i);
						if (!keyId.equals(hashmap.get("id"))) {
							break;
						}
					}
					request.setAttribute("preId", hashmap.get("id"));
					request.setAttribute("preNumber", hashmap.get("counts"));
					/* 下一条 */
					tempnum = num;
					if (tempnum == list.size()) {
						tempnum--;
					}

					for (int i = tempnum; i < list.size(); i++) {
						hashmap = (HashMap) list.get(i);
						if (!keyId.equals(hashmap.get("id"))) {
							break;
						}
					}
					request.setAttribute("nextId", hashmap.get("id"));
					request.setAttribute("nextNumber", hashmap.get("counts"));
					/* 最后一条 */
					hashmap = (HashMap) list.get(list.size() - 1);
					request.setAttribute("endId", hashmap.get("id"));
					request.setAttribute("endNumber", hashmap.get("counts"));
				}
			}

		} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// 记录不存在错误
			EchoMessage.error().add(getMessage(request, "common.error.nodata")).setRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// 加载失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("isEspecial", request.getParameter("isEspecial"));
		
		/* 查询现金流量科目主项目 和 副项目  */
		Result mItems = mgt.queryMItems();
		if(mItems.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("mItems", new Gson().toJson(mItems.retVal));
		}
		
		Result sItems = mgt.querySItems();
		if(sItems.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("sItems", new Gson().toJson(sItems.retVal));
		}
		
		// 凭证设置
		rs = mgt.queryVoucherSetting();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) rs.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		request.setAttribute("NoButton", request.getParameter("NoButton"));
		if(!refFlag){
			return getForward(request, mapping, "updateVoucherRef");
		} else{
			return getForward(request, mapping, "updateVoucher");
		}
	}

	/**
	 * 查询凭证列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public ActionForward queryVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		VoucherSearchForm searchForm = (VoucherSearchForm) form;
		
		String opUrl = request.getParameter("opUrl");
		if(opUrl != null && "url".equals(opUrl)){
			//如果是自定义报表进入时，对数据进行处理
			searchForm.setCreateBy(request.getParameter("createBy"));
			searchForm.setTblEmployee_EmpFullName(request.getParameter("tblEmployee_EmpFullName"));
			searchForm.setSearchCredYearStart(request.getParameter("searchCredYearStart"));
			searchForm.setSearchCredMonthStart(request.getParameter("searchCredMonthStart"));
			searchForm.setSearchCredYearEnd(request.getParameter("searchCredYearEnd"));
			searchForm.setSearchCredMonthEnd(request.getParameter("searchCredMonthEnd"));
			String searchRecordComment=request.getParameter("searchRecordComment")==null?"":request.getParameter("searchRecordComment");
			searchForm.setSearchRecordComment(new String(searchRecordComment.getBytes("iso-8859-1"), "UTF-8"));
			searchForm.setSearchStartTime(request.getParameter("searchStartTime"));
			searchForm.setSearchEndTime(request.getParameter("searchEndTime"));
			searchForm.setSearchAccCodeStart(request.getParameter("searchAccCodeStart"));
			searchForm.setSearchAccCodeEnd(request.getParameter("searchAccCodeEnd"));
			
			searchForm.setSearchMoneyEnd(request.getParameter("setSearchMoneyEnd"));
			searchForm.setSearchMoneyEnd(request.getParameter("setSearchMoneyEnd"));
			
			searchForm.setSearchwName(request.getParameter("searchwName"));
			
			//***添加凭证类型筛选条件
			searchForm.setSearchAccType(request.getParameter("searchAccType"));
			
			
			String searchDeptName = request.getParameter("searchDeptName");
			if(searchDeptName != null && !"".equals(searchDeptName)){
				searchForm.setSearchDeptName(GlobalsTool.toChinseChar(searchDeptName));
			}
			String searchEmployeeName = request.getParameter("searchEmployeeName");
			if(searchEmployeeName != null && !"".equals(searchEmployeeName)){
				searchForm.setSearchEmployeeName(GlobalsTool.toChinseChar(searchEmployeeName));
			}
			String searchClientName= request.getParameter("searchClientName");
			if(searchClientName != null && !"".equals(searchClientName)){
				searchForm.setSearchClientName(GlobalsTool.toChinseChar(searchClientName));
			}
		}
		
		LoginBean bean = this.getLoginBean(request);
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/VoucherManageAction.do");
		/* 查询列表 */
		Result result = mgt.queryList(searchForm, mop, bean);

		// 查询成功
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {

			/* 把列表存储到session中 */
			HashTable voucherListHash = (HashTable) request.getSession().getAttribute("voucherList");
			if (voucherListHash == null) {
				voucherListHash = new HashTable();
			}
			Object[] object = (Object[]) result.retVal;
			if (object != null && object.length > 0) {
				request.setAttribute("AccMainList", object[0]);
				String saveSql = object[1]+"";
				saveSql = saveSql.replaceAll("%", "@CentSign:");
				while (saveSql.indexOf("+") > -1) {
					String t = saveSql.substring(0, saveSql.indexOf("+"));
					saveSql = t + "@AddSign:" + saveSql.substring(saveSql.indexOf("+") + 1);
				}
				saveSql =GlobalsTool.replaceSpecLitter("@SQL:"+saveSql+"@ParamList:@end:");
				request.setAttribute("saveSql", saveSql);
				voucherListHash.put(bean.getId(), object[0]);
				result.setRetVal(object[0]);
				request.setAttribute("pageBar", this.pageBar(result, request));
			}
			request.getSession().setAttribute("voucherList", voucherListHash);
		}

		// 凭证设置
		result = mgt.queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(bean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriodBean", accPeriodBean);
		
		request.setAttribute("VoucherSearchForm", searchForm);
		
		
		Result rs2 = new ReportDataMgt().getBillFormatList("tblAccMain","",getLocale(request).toString(),getLoginBean(request).getId(),getLoginBean(request).getDepartCode());
        request.setAttribute("formatList", rs2.getRetVal());
        Cookie[] coks = request.getCookies();
        for (int i = 0; i < coks.length; i++) {
            Cookie cok = coks[i];
            if (cok.getName().equals("JSESSIONID")) {
                request.setAttribute("JSESSIONID", cok.getValue());
                break;
            }
        }
		
		return getForward(request, mapping, "voucherList");
	}

	/**
	 * 删除
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 选中要删除的项ID */
		String[] keyIds = request.getParameterValues("keyId");

		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

		/* 用户登陆信息 */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");

		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		List logList = new ArrayList();
		for(String s : keyIds){
			Result result = mgt.queryAccMain(s);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				HashMap tblAccMainMap = (HashMap)result.getRetVal();
				logList.add(tblAccMainMap);
			}
		}
		
		/* 删除 */
		Result rs = mgt.deleteData(keyIds, "tblAccMain", map, new HashMap(), lg.getId(), "", resources, this.getLocale(request));

		ActionForward forward = null;

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			/* 删除成功 */

			String[] files = (String[]) rs.getRetVal();
			// 删除附件
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, files[i]);
				}
			}
			/*添加系统日志*/
			for(int i =0;i<logList.size();i++){
				HashMap logMap = (HashMap)logList.get(i);
				new DynDBManager().addLog(2, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"删除凭证:"+logMap.get("CredTypeID")+"-"+logMap.get("OrderNo")+";","tblAccMain", "凭证管理","");
			}
			forward = queryVoucher(mapping, form, request, response);
		} else {
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}			
			forward = getForward(request, mapping, "message");
		}
		return forward;
	}

	/**
	 * 数据导出
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String[] keyIds = getParameters("keyId", request);
		String accName = GlobalsTool.getSysSetting("DefBackPath");
		File file = new File("..\\..\\AIOBillExport");
		if (!file.exists()) {
			file.mkdirs();
		}
		String tableDisplay = GlobalsTool.getTableInfoBean("tblAccMain").getDisplay().get(getLocale(request).toString());
		String fileName = file.getCanonicalPath() + "\\" + tableDisplay + ".xls";
		Result result = new Result();
		ArrayList<ExportField> exportList = new ArrayList<ExportField>();
		ArrayList<HashMap> exportValuesList = new ArrayList<HashMap>();
		LoginBean lg = getLoginBean(request);
		String sunClassCode = lg.getSunCmpClassCode();  //从LoginBean中取出分支机构的classCode
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
        boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
        MOperation mop = GlobalsTool.getMOperationMap(request) ;
        //执行加载明细
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        DBTableInfoBean dbTable = GlobalsTool.getTableInfoBean("tblAccMain");
        ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables("tblAccMain", map);
        request.setAttribute("mainTable",dbTable);
        request.setAttribute("fieldInfos", dbTable.getFieldInfos()) ;
        request.setAttribute("childTableList", childTableList) ;
        ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
        ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();
        Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;//子表的列配置
        Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)request.getSession().getServletContext().getAttribute("userSettingColConfig") ;
        request.setAttribute("configList", configList);
        request.setAttribute("allConfigList",allConfigList);
        request.setAttribute("childTableConfigList",childTableConfigList);
        request.setAttribute("tableName", "tblAccMain");
        
        if(keyIds == null || keyIds.length ==0){
        	LoginBean bean = this.getLoginBean(request);
    		mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/VoucherManageAction.do");
    		/* 查询列表 */
    		VoucherSearchForm searchForm = (VoucherSearchForm)form;
    		int pageSize = searchForm.getPageSize();
    		searchForm.setPageSize(-1111);
    		Result idresult = mgt.queryList(searchForm, mop, bean);
    		searchForm.setPageSize(pageSize);
    		if(idresult.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			ArrayList<HashMap> list= (ArrayList<HashMap>)((Object[])idresult.retVal)[0];
    			keyIds = new String[list.size()];
    			for(int i = 0;i<list.size();i++){
    				keyIds[i] = list.get(i).get("id")+"";
    			}
    		}
        }
        

		for (int z = 0; keyIds != null && z < keyIds.length; z++) {
			String keyex = keyIds[z];
			request.setAttribute("detail", "detail");
			request.setAttribute("detailOk", "1");
			Result rs = new DynDBManager().detail("tblAccMain", map, keyex, sunClassCode,props,lg.getId(),isLastSunCompany,"");
			HashMap hm = (HashMap) rs.retVal;
			request.setAttribute("values", hm);
			HashMap moreLan=(HashMap)hm.get("LANGUAGEQUERY");
			HashMap childFieldMap = new HashMap();
    		try {
    			if (childTableList != null) {
    				for(DBTableInfoBean dib :(ArrayList<DBTableInfoBean>)childTableList){
    					ArrayList<DBFieldInfoBean> dibFieldList = DynDBManager.getMainFieldList("tblAccMain", dib,null, childTableConfigList.get(dib.getTableName()), null, null, null, mop, "-1", true, "", lg, null);
    					childFieldMap.put(dib.getTableName(), dibFieldList);
    				}
    			}
    		} catch (Exception e) {
            	EchoMessage.error().add(e.getMessage()).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
                return getForward(request, mapping, "message");
    		}   
			
			request.setAttribute("childFieldMap", childFieldMap) ;
			HashMap exportValues = new HashMap();
			exportValuesList.add(exportValues);
			exportList = new ArrayList<ExportField>();
			ExportData.export(request, exportList, exportValues);
		}

		FileOutputStream fos = new FileOutputStream(fileName);
		result = ExportData.billExport(fos, tableDisplay, exportList, exportValuesList, null);
		fos.close();
		/* 导出成功 */
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String backURL = "/VoucherManageAction.do?operation=4";
			EchoMessage.success().add(
					getMessage(request, "com.export.success") + "<a href='/ReadFile?tempFile=export&fileName=" + GlobalsTool.encode(tableDisplay + ".xls")
							+ "'>" + getMessage(request, "com.export.success.download") + "</a>" + getMessage(request, "com.export.success.over") + fileName)
					.setBackUrl(backURL).setNotAutoBack().setAlertRequest(request);
		} else {
			EchoMessage.error().add(getMessage(request, "com.export.failure")).setNotAutoBack().setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}

	/**
	 * 凭证详情
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward detailVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 凭证id */
		String keyId = request.getParameter("id");
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");

		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());

		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = lg.getSunCmpClassCode(); // 从LoginBean中取出分支机构的classCode

		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);

		/* 用户权限 */
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/VoucherManageAction.do"));

		/* 加载数据 */
		Result rs = new DynDBManager().detail("tblAccMain", map, keyId, sunClassCode, props, lg.getId(), isLastSunCompany, 
				"");		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 加载成功
			request.setAttribute("values", rs.retVal);			
			HashMap hm = (HashMap) rs.retVal;
			if (hm == null || hm.get("id") == null) {
				// 未找到记录
				EchoMessage.error().add(getMessage(request, "oaJob.not.find")).setRequest(request);
				return getForward(request, mapping, "message");
			}					
			
			String number = request.getParameter("number");
			int num = 0;
			if (number != null && !"".equals(number)) {
				num = Integer.valueOf(number);
			}
			/* 取到保存到内存中的列表 */
			HashTable voucherListHash = (HashTable) request.getSession().getAttribute("voucherList");
			if (voucherListHash != null && voucherListHash.size() > 0) {
				List list = (ArrayList) voucherListHash.get(lg.getId());
				if (list != null && list.size() > 0) {
					/* 第一条 */
					HashMap hashmap = (HashMap) list.get(0);
					request.setAttribute("firstId", hashmap.get("id"));
					request.setAttribute("firstNumber", hashmap.get("counts"));
					/* 上一条 */
					int tempnum = num;
					if (tempnum > 1) {
						tempnum = tempnum - 1;
					} else {
						tempnum = 0;
					}
					for (int i = tempnum; i > 0; i--) {
						hashmap = (HashMap) list.get(i);
						if (!keyId.equals(hashmap.get("id"))) {
							break;
						}
					}
					request.setAttribute("preId", hashmap.get("id"));
					request.setAttribute("preNumber", hashmap.get("counts"));
					/* 下一条 */
					tempnum = num;
					if (tempnum == list.size()) {
						tempnum--;
					}

					for (int i = tempnum; i < list.size(); i++) {
						hashmap = (HashMap) list.get(i);
						if (!keyId.equals(hashmap.get("id"))) {
							break;
						}
					}
					request.setAttribute("nextId", hashmap.get("id"));
					request.setAttribute("nextNumber", hashmap.get("counts"));
					/* 最后一条 */
					hashmap = (HashMap) list.get(list.size() - 1);
					request.setAttribute("endId", hashmap.get("id"));
					request.setAttribute("endNumber", hashmap.get("counts"));
					request.setAttribute("number", number);
				}
			}
		} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// 记录不存在错误
			EchoMessage.error().add(getMessage(request, "common.error.nodata")).setRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// 加载失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("isEspecial", request.getParameter("isEspecial"));
		// 凭证设置
		rs = mgt.queryVoucherSetting();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) rs.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		/* 会计期间 */
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		request.setAttribute("NoButton", request.getParameter("NoButton"));
				
		return getForward(request, mapping, "detailVoucher");
		
	}

	/**
	 * 修改凭证
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginBean loginBean = this.getLoginBean(request);
		
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		

		/* 验证是否可以修改（会计期间前的数据不能修改） */
		Integer credYear = Integer.valueOf(request.getParameter("CredYear"));
		Integer credMonth = Integer.valueOf(request.getParameter("CredMonth"));

		Integer periodMonth = -1;
		Integer periodYear = -1;
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		if (accPeriodBean != null) {
			periodMonth = accPeriodBean.getAccMonth();
			periodYear = accPeriodBean.getAccYear();
		}
		if ((credYear < periodYear || (credYear == periodYear && credMonth < periodMonth) || periodMonth < 0) && credMonth != 0) {
			EchoMessage.error().add(getMessage(request, "com.cantupdatebefbill")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}

		/* 附件 */
		String attachment = request.getParameter("attachFiles");

		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		/* 表结构信息 */
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, "tblAccMain");

		HashMap values = new HashMap();

		/* 取主表数据 */
		readMainTable(values, tableInfo, request);
		LoginBean lg = getLoginBean(request);

		/* 获取子表中数据 */
		List childTableList = DDLOperation.getChildTables("tblAccMain", map);
		if (childTableList != null && childTableList.size() > 0) {
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
				readChildTable(values, childTable, request, lg.getSunCmpClassCode());
			}
		}
		values.put("isAuditing", "start");
		values.put("lastUpdateBy", loginBean.getId());
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("Attachment", attachment);
		//values.put("OrderNo", 0);
		values.put("AutoBillMarker", 0);
		
		values.put("oldOrderNo", request.getParameter("oldOrderNo"));							//旧的凭证号
		values.put("oldCredTypeID", request.getParameter("oldCredTypeID"));						//旧的凭证字
		values.put("oldCredYear", request.getParameter("CredYear"));							//旧的会计期间年
		values.put("oldPeriod", request.getParameter("Period"));								//旧的会计期间

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		/* 删除del的文件 */
		String delFiles = request.getParameter("delFiles");
		if (delFiles != null && !"".equals(delFiles)) {
			String[] delFile = delFiles.split(";");
			for (int i = 0; delFile != null && i < delFile.length; i++) {
				FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, delFile[i]);
			}
		}

		/* 修改主表和子表的数据 */
		Result result = mgt.updateAccMain("tblAccMain", map, values, loginBean, "", resources, this.getLocale(request));
		ActionForward forward = getForward(request, mapping, "alert");
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS || result.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
			/* 修改成功 */
			result = mgt.queryAccMain(values.get("id").toString());
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				/*添加系统日志*/
				HashMap tblAccMainMap = (HashMap)result.getRetVal();
				new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"凭证修改前:"+tblAccMainMap.get("CredTypeID")+"-"+tblAccMainMap.get("OrderNo")+";修改后:"+tblAccMainMap.get("CredTypeID")+"-"+tblAccMainMap.get("OrderNo")+";","tblAccMain", "凭证管理","");
			}
			String message = getMessage(request, "common.msg.updateSuccess");
			EchoMessage echo = EchoMessage.success().add(message).setBackUrl("/VoucherManageAction.do?operation=5&tableName=tblAccMain&id="+values.get("id")+"&number=&popWinName="+request.getParameter("popWinName"));
			echo.setAlertRequest(request);
		} else {
			/* 修改失败 */
			// 删除正式文件
			if (attachment != null && !"".equals(attachment)) {
				String[] uploadaffix = attachment.split(";");
				for (int i = 0; uploadaffix != null && i < uploadaffix.length; i++) {
					FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, uploadaffix[i]);
				}
			}
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(result, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}	
			
		}
		return forward;
	}

	/**
	 * 查询凭证模板
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward voucherModuleList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String searchValue = request.getParameter("searchValue");
		/* 查询凭证模板 */
		Result result = mgt.queryModuleList(searchValue);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 查询成功
			request.setAttribute("moduleList", result.retVal);
		}
		request.setAttribute("searchValue", searchValue);
		return getForward(request, mapping, "moduleList");
	}

	/**
	 * 保存模板
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward addModule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginBean loginBean = this.getLoginBean(request);

		/* 附件 */
		String attachment = request.getParameter("Attachment");

		/* 保存在内存中所有表结构的信息 */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		/* 表结构信息 */
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, "tblAccMainTemplete");

		HashMap values = new HashMap();

		/* 取主表数据 */
		readMainTable(values, tableInfo, request);
		LoginBean lg = getLoginBean(request);

		/* 获取子表中数据 */
		List childTableList = DDLOperation.getChildTables("tblAccMainTemplete", map);
		if (childTableList != null && childTableList.size() > 0) {
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
				readChildTables(values, childTable, request, lg.getSunCmpClassCode());
			}
		}
		values.put("isAuditing", "start");
		values.put("RefBillType", "tblAccMainTemplete");
		values.put("createBy", loginBean.getId());
		values.put("DepartmentCode", loginBean.getDepartCode());
		values.put("lastUpdateBy", loginBean.getId());
		values.put("createTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("workFlowNode", "0");
		values.put("SCompanyID", "00001");
		values.put("AutoBillMarker", 0);

		// 设置主表的ID
		String id = (String) values.get("id");
		if (id == null || id.trim().length() == 0) {
			id = IDGenerater.getId();
			values.put("id", id);
		}

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		/* 删除del的文件 */
		String delFiles = request.getParameter("delFiles");
		if (delFiles != null && !"".equals(delFiles)) {
			String[] delFile = attachment.split(";");
			for (int i = 0; delFile != null && i < delFile.length; i++) {
				FileHandler.delete("tblAccMainTemplete", FileHandler.TYPE_AFFIX, delFile[i]);
			}
		}

		/* 保存凭证模板主表和子表的数据 */
		Result result = mgt.addAccMainModule("tblAccMainTemplete", map, values, id, loginBean.getId(), "", resources, this.getLocale(request), "");

		// 添加成功
		request.setAttribute("noback", true);
		request.setAttribute("loadFresh", "true");
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("parent.asyRefure()").setAlertRequest(request);
		} else {
			// 添加凭证模块失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}

	/**
	 * 删除凭证模板
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteModule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 删除模板的Id数组 */
		String[] keyIds = request.getParameterValues("keyId");

		/* 执行删除操作 */
		Result result = mgt.deleteModule(keyIds);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 删除成功
			request.setAttribute("msg", "OK");

			String[] files = (String[]) result.getRetVal();
			// 删除附件
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					FileHandler.delete("tblAccMainTemplete", FileHandler.TYPE_AFFIX, files[i]);
				}
			}
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 审核不通过时输入批注前
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward addRemarkPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 凭证Id */
		String voucherId = request.getParameter("voucherId");
		request.setAttribute("voucherId", voucherId);
		request.setAttribute("isList", request.getParameter("isList"));
		return getForward(request, mapping, "approveRemark");
	}

	/**
	 * 审核处理（审核通过、审核不通过、反审核）
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward dealapprov(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 处理类型（noPass审核不通过，yesPass审核通过，reversePass反审核） */
		String dealType = request.getParameter("dealType");

		LoginBean loginBean = this.getLoginBean(request);
		/* 凭证Id数组 */
		String[] keyIds = request.getParameterValues("keyId");

		/* 批注 */
		String remark = request.getParameter("remark");

		String isList = request.getParameter("isList");

		String isAuditing = ""; // 凭证状态
		String approver = ""; // 审核人
		remark = remark == null ? "" : remark;
		if (dealType != null && "noPass".equals(dealType)) {
			remark += " " + getMessage(request, "oa.job.noApproved") + " ";
			isAuditing = "noPass";
		} else if (dealType != null && "yesPass".equals(dealType)) {
			remark = getMessage(request, "oa.job.approved") + " ";
			isAuditing = "finish";
			approver = loginBean.getId();
		} else if (dealType != null && "reversePass".equals(dealType)) {
			remark = getMessage(request, "common.turnAuditing") + " ";
			isAuditing = "noPass";
			approver = "";
		}
		EchoMessage echo = null;
		/* 批注+审核人+时间 */

		remark += loginBean.getEmpFullName() + " " + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "<br />";
		if (dealType != null && keyIds.length > 0) {
			Result rs = mgt.approveUpdate(keyIds, remark, isAuditing, approver, dealType, getLocale(request), loginBean);
			String message = "";
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				/* 成功 */
				message = "com.auto.succeed";
				// 发生通知消息
				rs = mgt.queryCreatePerson(keyIds);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List list = (ArrayList) rs.retVal;
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							String[] str = (String[]) list.get(i);
							String title = getMessage(request, "erp.voucher.my") + "[" + str[1] + "-" + str[2] + "]";
							if (dealType != null && "noPass".equals(dealType)) {
								title += getMessage(request, "erp.voucher.nopass");
							} else if (dealType != null && "yesPass".equals(dealType)) {
								title += getMessage(request, "erp.voucher.yesPass");
							} else if (dealType != null && "reversePass".equals(dealType)) {
								title += getMessage(request, "erp.voucher.reversePass");
							}
							String favoriteURL = "/VoucherManageAction.do?operation=5&tableName=tblAccMain&id=" + str[0] + "&isEspecial=1";
							String content = "<a href=javascript:mdiwin('" + favoriteURL + "','凭证管理')>" + title
									+ "</a>";
							new Thread(new NotifyFashion(loginBean.getId(), title, content, str[3], 4, "yes", str[0])).start();
							new DynDBManager().addLog(14, loginBean.getId(), loginBean.getName(), loginBean.getSunCmpClassCode(),"凭证处理:["+str[1]+"-"+str[2]+"]"+remark+";","tblAccMain", "凭证管理","凭证处理");
						}
					}
				}
				if (isList != null && !"".equals(isList)) {
					// 返回修改页面
					if (dealType != null && "noPass".equals(dealType)) {
						// 审核不通过
						request.setAttribute("loadFresh", "true");
						echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl("parent.reloads()");
					} else {
						String url = "/VoucherManageAction.do?";
						if ("update".equals(isList)) {
							url += "operation=7";
						} else {
							url += "operation=5";
						}
						url += "&id=" + keyIds[0];
						url +="&isEspecial="+request.getParameter("isEspecial")+"&NoButton="+request.getParameter("NoButton");
						url +="&popWinName="+request.getParameter("popWinName");
						echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl(url);
					}
				} else {
					// 凭证列表
					echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl("/VoucherManageAction.do?operation=4");
				}
			} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
				// 发生验证错误
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				echo = EchoMessage.error().add(errorMessage);
			} else {
				SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
	        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
	        		echo = EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl());
				} else {
					echo = EchoMessage.error().add(saveErrrorObj.getMsg());
				}	
				
			}
		}
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * 过账、反过账
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward dealBinder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 处理类型（binder过账，reversebinder反过账） */
		String dealType = request.getParameter("dealType");

		LoginBean loginBean = this.getLoginBean(request);
		/* 凭证Id数组 */
		String[] keyIds = request.getParameterValues("keyId");

		String isList = request.getParameter("isList");

		String remark = ""; // 批注
		String workFlowNodeName = "notApprove"; // 是否过账（notApprove未过账，finish已过账）
		String postingUser = ""; // 过账人

		if (dealType != null && "binder".equals(dealType)) {
			remark = getMessage(request, "muduleFlow.lb.voucher") + " ";
			workFlowNodeName = "finish";
			postingUser = loginBean.getId();
		} else if (dealType != null && "reverseBinder".equals(dealType)) {
			remark = getMessage(request, "erp.voucher.reverseBinder") + " ";
			workFlowNodeName = "notApprove";
			postingUser = "";
		}
		String logMsg = remark;
		/* 批注+操作人+时间 */
		remark += loginBean.getEmpFullName() + " " + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "<br />";
		String message = "";
		String url = "";
		EchoMessage echo = null;
		if (dealType != null && keyIds.length > 0) {

			// 凭证设置
			AccMainSettingBean settingBean = null;
			Result result = mgt.queryVoucherSetting();
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				settingBean = (AccMainSettingBean) result.retVal;
			}

			MessageResources resources = null;
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}

			// 过账或者反过账
			Result rs = mgt.binderUpdate(keyIds, remark, workFlowNodeName, postingUser, dealType, settingBean, loginBean, resources, this.getLocale(request));
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				message = "com.auto.succeed";
				if (isList != null && !"".equals(isList)) {
					url = "/VoucherManageAction.do?";
					if ("update".equals(isList)) {
						url += "operation=7";
					} else {
						url += "operation=5";
					}
					url += "&id=" + keyIds[0];
				} else {
					// 凭证列表
					url = "/VoucherManageAction.do?operation=4";
				}
				rs = mgt.queryCreatePerson(keyIds);
				List list = (ArrayList) rs.retVal;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String[] str = (String[]) list.get(i);
						new DynDBManager().addLog(14, loginBean.getId(), loginBean.getName(), loginBean.getSunCmpClassCode(),"凭证处理:"+"["+str[1]+"-"+str[2]+"]"+logMsg+"成功;","tblAccMain", "凭证管理","凭证处理");
					}
				}
				url +="&isEspecial="+request.getParameter("isEspecial")+"&NoButton="+request.getParameter("NoButton");
				url +="&popWinName="+request.getParameter("popWinName");
				echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl(url);
			} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
				// 验证错误处理信息
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				echo = EchoMessage.error().add(errorMessage);
			} else {
				SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
	        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
	        		echo = EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl());
				} else {
					echo = EchoMessage.error().add(saveErrrorObj.getMsg());
				}	
			}
		}
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * 过账向导前
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward bindGuidePre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(this.getLoginBean(request).getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriodBean", accPeriodBean);
		return getForward(request, mapping, "binderGuide");
	}
	
	/**
	 * 审核向导前
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward AuditeGuidePre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(this.getLoginBean(request).getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriodBean", accPeriodBean);
		return getForward(request, mapping, "AuditeGuide");
	}

	/**
	 * 根据过账向导进行过账
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward batchbinder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 凭证号不连续(停止过账1，继续过账2)
		String iscredNo = request.getParameter("iscredNo");

		// 过账发生错误(停止过账1，继续过账2)
		String postingerror = request.getParameter("postingerror");

		// 凭证范围(全部1，指定范围之前2，vouchertime时间，指定期间3)
		String voucherarea = request.getParameter("voucherarea");
		String vouchertime = request.getParameter("vouchertime");
		String voucherPeriodYear = request.getParameter("voucherPeriodYear");
		String voucherPeriod = request.getParameter("voucherPeriod");

		// 凭证字
		String credTypeid = request.getParameter("credTypeid");

		// 用户登陆信息
		LoginBean loginBean = this.getLoginBean(request);
		// 凭证设置
		AccMainSettingBean settingBean = null;
		Result result = mgt.queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			settingBean = (AccMainSettingBean) result.retVal;
		}

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		// 向导过账
		Result rs = mgt.batchBinder(Integer.valueOf(iscredNo), Integer.valueOf(postingerror), Integer.valueOf(voucherarea), vouchertime, settingBean,
				loginBean, resources, this.getLocale(request), credTypeid,voucherPeriodYear,voucherPeriod);

		EchoMessage echo = null;
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			// 过账成功
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			echo = EchoMessage.success().add(errorMessage).setBackUrl("parent.reloads()");
		} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
			// 验证错误处理信息
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			echo = EchoMessage.error().add(errorMessage);
		} else {
			echo = EchoMessage.error().add(rs.retCode, request);
		}
		request.setAttribute("loadFresh", "true");
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 根据审核向导进行审核
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward batchAudite(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 凭证范围(全部1，指定范围之前2，vouchertime时间，指定期间3)
		String voucherarea = request.getParameter("voucherarea");
		String vouchertime = request.getParameter("vouchertime");
		String voucherPeriodYear = request.getParameter("voucherPeriodYear");
		String voucherPeriod = request.getParameter("voucherPeriod");

		// 用户登陆信息
		LoginBean loginBean = this.getLoginBean(request);
		

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(this.getLoginBean(request).getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriodBean", accPeriodBean);
		
		// 向导过账
		String remark=loginBean.getEmpFullName() + " " + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "<br />";
		Result rs = mgt.batchAudite(Integer.valueOf(voucherarea),vouchertime,loginBean, resources, this.getLocale(request),voucherPeriodYear,voucherPeriod,accPeriodBean,remark);

		EchoMessage echo = null;
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			// 审核成功
			echo = EchoMessage.success().add("审核成功").setBackUrl("parent.reloads()");
		} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
			// 验证错误处理信息
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			echo = EchoMessage.error().add(errorMessage);
		} else {
			echo = EchoMessage.error().add(rs.retCode, request);
		}
		request.setAttribute("loadFresh", "true");
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * 复核/反复核
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward dealcheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 处理类型（复核，反复核） */
		String dealType = request.getParameter("dealType");

		LoginBean loginBean = this.getLoginBean(request);
		/* 凭证Id数组 */
		String[] keyIds = request.getParameterValues("keyId");

		/* 结算号和结算方式 */
		String acceptMode = request.getParameter("AcceptMode");
		String acceptNumber = request.getParameter("AcceptNumber");

		String isList = request.getParameter("isList");

		String remark = ""; // 批注
		Integer isReview = 1; // 是否复核（1未复核，2已复核）
		String reviewUser = ""; // 复核人

		if (dealType != null && "check".equals(dealType)) {
			remark = getMessage(request, "ReportBillView.button.Check") + " ";
			isReview = 2;
			reviewUser = loginBean.getId();
		} else if (dealType != null && "reverseCheck".equals(dealType)) {
			remark = getMessage(request, "erp.voucher.reverseCheck") + " ";
			isReview = 1;
			reviewUser = "";
		}
		/* 批注+操作人+时间 */
		remark += loginBean.getEmpFullName() + " " + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "<br />";
		String message = "";
		String url = "";
		EchoMessage echo = null;
		if (dealType != null && keyIds.length > 0) {

			// 凭证设置
			AccMainSettingBean settingBean = null;
			Result result = mgt.queryVoucherSetting();
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				settingBean = (AccMainSettingBean) result.retVal;
			}

			// 复核或者反复核
			Result rs = mgt.checkUpdate(keyIds, remark, isReview, reviewUser, dealType, settingBean, loginBean, acceptMode, acceptNumber, isList);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				message = "com.auto.succeed";
				if (isList != null && !"".equals(isList)) {
					url = "/VoucherManageAction.do?";
					if ("update".equals(isList)) {
						url += "operation=7";
					} else {
						url += "operation=5";
					}
					url += "&id=" + keyIds[0];
				} else {
					// 凭证列表
					url = "/VoucherManageAction.do?operation=4";
				}
				echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl(url);
			} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
				// 验证错误处理信息
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				echo = EchoMessage.error().add(errorMessage);
			} else {
				echo = EchoMessage.error().add(rs.retCode, request);
			}
		}
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * 断号查询列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward stopCredNoList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 凭证字 */
		String CredTypeID = request.getParameter("CredTypeID");

		if (CredTypeID != null && !"".equals(CredTypeID)) {
			String periodyear = request.getParameter("periodyear");
			String period = request.getParameter("period");
			AccPeriodBean accPeriodBean = new AccPeriodBean();
			accPeriodBean.setAccYear(Integer.valueOf(periodyear));
			accPeriodBean.setAccPeriod(Integer.valueOf(period));
			/* 查询断号 */
			Result rs = mgt.queryStopCredNoList(CredTypeID, accPeriodBean, "queryCredNo");
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				map = (HashMap<Integer, Integer>) rs.retVal;
				Iterator iter = map.entrySet().iterator();
				List list = new ArrayList<String[]>();
				while (iter.hasNext()) {
					String[] str = new String[4];
					Entry entry = (Entry) iter.next();
					str[0] = CredTypeID;
					str[1] = String.valueOf(accPeriodBean.getAccYear());
					str[2] = String.valueOf(accPeriodBean.getAccPeriod());
					str[3] = String.valueOf(entry.getKey().toString());
					list.add(str);
				}
				request.setAttribute("accmainList", list);
			}
			request.setAttribute("periodyear", periodyear);
			request.setAttribute("period", period);
			request.setAttribute("CredTypeID", CredTypeID);
		}
		return getForward(request, mapping, "stopCredNoList");
	}

	/**
	 * 另存模板
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveToAccTemp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 查询启用的会计期间 */
		LoginBean loginBean = this.getLoginBean(request);
		Hashtable sessionSet = BaseEnv.sessionSet;
		String name=request.getParameter("name");
		String keyId = request.getParameter("keyId");
		Result rs = mgt.saveToAccTemp(name, keyId);
		String msg = "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			msg = "另存模板成功";
		}else{
			msg = "另存模板失败";
		}
		request.setAttribute("msg", msg);

		return getForward(request, mapping, "blank");
	}
	/**
	 * 凭证整理前
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward resetCredNoPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 查询启用的会计期间 */
		LoginBean loginBean = this.getLoginBean(request);
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);

		return getForward(request, mapping, "resetCredNo");
	}

	/**
	 * 凭证整理
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward resetCredNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 凭证字
		String CredTypeID = request.getParameter("credTypeid");
		try {
			/* 处理URL中存在文字 */
			CredTypeID = java.net.URLDecoder.decode(CredTypeID, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 期间年
		String periodYear = request.getParameter("periodYear");
		// 期间
		String period = request.getParameter("period");

		LoginBean lg = getLoginBean(request);
		// 凭证整理
		Result rs = mgt.resetOrderNo(CredTypeID, Integer.parseInt(periodYear), Integer.parseInt(period));
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			new DynDBManager().addLog(14, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"凭证整理:"+CredTypeID+"-"+periodYear+"-"+period+";","tblAccMain", "凭证整理","凭证整理");
			EchoMessage.success().add(getMessage(request, "erp.voucher.credNo.success")).setBackUrl(
					"/VoucherManage.do?optype=resetCredNoPre&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setBackUrl(
					"/VoucherManage.do?optype=resetCredNoPre&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		}
		return getForward(request, mapping, "message");
	}

	/**
	 * 读取子表的数据
	 * 
	 * @param values
	 * @param tableInfo
	 * @param req
	 * @param sunCompany
	 */
	public void readChildTables(HashMap values, DBTableInfoBean tableInfo, HttpServletRequest req, String sunCompany) {
		ArrayList childList = new ArrayList();
		values.put("TABLENAME_" + tableInfo.getTableName(), childList);
		List list = tableInfo.getFieldInfos();
		// 找出记录条数

		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);
			if (fieldInfo.getDefaultValue() != null && fieldInfo.getDefaultValue().trim().length() > 0) {
				// 有默认值的不参与计算
				continue;
			} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX) {
				continue;
			} else if ((fieldInfo.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE || fieldInfo.getFieldType() == DBFieldInfoBean.FIELD_INT)
					&& (fieldInfo.getFieldSysType() == null || (fieldInfo.getFieldSysType() != null && !fieldInfo.getFieldSysType().equals("RowMarker")))) {
				continue;
			}
			String[] str = req.getParameterValues("tblAccDetail_" + fieldInfo.getFieldName());
			int c = 0;
			for (int j = 0; str != null && j < str.length; j++) {
				if (str[j] != null && str[j].trim().length() > 0 && !"0".equals(str[j]) && !str[j].trim().equals("-100000")) {
					c = j + 1;
				}
			}
			if (c > count)
				count = c;
		}

		for (int i = 0; i < count; i++) {
			childList.add(new HashMap());
		}

		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);
			String[] str;
			if (fieldInfo.getFieldName().equals("SCompanyID")) {
				str = new String[count];
				for (int j = 0; j < count; j++) {
					str[j] = sunCompany;
				}
			} else {
				str = req.getParameterValues("tblAccDetail_" + fieldInfo.getFieldName());
			}
			if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX
					|| (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX)) {
				for (int j = 0; j < count; j++) {
					String[] checkValue = req.getParameterValues("tblAccDetail_" + fieldInfo.getFieldName() + (j + 1));
					String check = "";
					if (checkValue != null) {
						for (String strs : checkValue) {
							check += strs + ",";
						}
					}
					((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), check);
				}
			} else {
				for (int j = 0; j < count; j++) {
					if (str != null && str[j] != null) {
						if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
							// 多语言
							((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), KRLanguageQuery.create((Hashtable) req.getSession().getServletContext()
									.getAttribute("LocaleTable"), (Locale) req.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY), str[j]));
						} else {
							if (str[j].length() == 0 && (fieldInfo.getFieldType() == 1 || fieldInfo.getFieldType() == 0)) {
								if (fieldInfo.getDefaultValue() != null && fieldInfo.getDefaultValue().length() > 0) {
									((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), fieldInfo.getDefaultValue());
								} else {
									((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), 0);
								}
							} else {
								((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), str[j]);
							}
						}
					} else {
						if (fieldInfo.getFieldType() == 1 || fieldInfo.getFieldType() == 0) {
							if (fieldInfo.getDefaultValue() != null && fieldInfo.getDefaultValue().length() > 0) {
								((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), fieldInfo.getDefaultValue());
							} else {
								((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), 0);
							}
						}
					}
				}
			}
		}
	}
}
