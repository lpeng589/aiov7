package com.menyi.aio.web.iniSet;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.*;
import com.menyi.aio.web.customize.*;
import com.menyi.aio.web.importData.*;
import com.menyi.aio.web.importData.ExcelFieldInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:财务期初Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-10-22 下午 16:10
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class IniAccAction extends MgtBaseAction{

	String simpleAccFlag = "";							//简单财务标志
	String openFCurrency = "";							//启用外币
	String openSunCompany = "";							//启用分支机构
	String sunClassCode = null;							//分支机构代码
	String currPeriod = "";								//会计期间
	String newDate = "";
	String fileName = "";
	
	IniAccMgt mgt = new IniAccMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/**
		 * 系统配置中的设置（控制期初的一些操作）
		 */
		Hashtable<String, SystemSettingBean> base = BaseEnv.systemSet;
		
		/* 启用简单财务标志 */
		SystemSettingBean bean = base.get("SimpleAccFlag");
		String value = bean.getSetting();
		if (value == null) {
			value = bean.getDefaultValue();
		}
		simpleAccFlag = value;
		request.setAttribute("SimpleAccFlag", value);
		
		/* 启用外币核算 */
		bean = base.get("currency");
		openFCurrency = bean.getSetting();
		request.setAttribute("OpenFCurrency", openFCurrency);
		
		/* 启用固定汇率 */
		bean = base.get("FixRate");
		request.setAttribute("FixRate", bean.getSetting());
		
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		/* 启用分支机构 */
		openSunCompany = base.get("sunCompany").getSetting();
		sunClassCode = lg.getSunCmpClassCode();
		
		/* 会计期间 */
		Hashtable sess = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		currPeriod = sess.get("NowPeriod").toString();
		
		/**
		 * 跟据不同操作类型分配给不同函数处理
		 */
		String opType = request.getParameter("opType");
		
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_UPDATE_PREPARE:
			if(opType != null && "importPre".equals(opType)){
				//导入期初前
				forward = importPre(mapping, form, request, response);
			}else if(opType != null && "downModule".equals(opType)){
				//下载模板
				forward = downModule(mapping, form, request, response);
			}else if(opType != null && "addIniItemPre".equals(opType)){
				//增加核算项前准备
				forward = addIniItemPre(mapping, form, request, response);
			}else if(opType != null && "updateIniItemPre".equals(opType)){
				//修改核算项前准备
				forward = updateIniItemPre(mapping, form, request, response);
			}else if(opType != null && "addIniItem".equals(opType)){
				//增加核算项
				forward = addIniItem(mapping, form, request, response);
			}else if(opType != null && "updateIniItem".equals(opType)){
				//修改核算项
				forward = updateIniItem(mapping, form, request, response);
			}else if(opType != null && "delIniItem".equals(opType)){
				//增加核算项
				forward = delIniItem(mapping, form, request, response);
			}else if(opType != null && "importHistory".equals(opType)){
				//导入历史
				forward = importHistory(mapping, form, request, response);
			}else if(opType != null && "delImport".equals(opType)){
				//导入历史删除
				forward = delImport(mapping, form, request, response);
			}else{
				//期初修改前
				forward = updateIniPre(mapping, form, request, response);
			}
			break;		
		case OperationConst.OP_UPDATE:
			if(opType != null && "importIniAcc".equals(opType)){
				//导入期初数据
				forward = importIniAcc(mapping, form, request, response);
			}else{
				//修改期初
				forward = updateIni(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUERY:
			//查询期初列表
			forward = queryIniList(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			//详情
			forward = detailIni(mapping, form, request, response);
			break;
		default:
			forward = queryIniList(mapping, form, request, response);
		}
		return forward;
	}
	
	
	/**
	 * 查询期初列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward queryIniList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		IniAccSearchForm searchForm = (IniAccSearchForm)form;
		
		if(searchForm != null){
			if(searchForm.getPageNo()==0){
				searchForm.setPageNo(1);
			}
		}
		
		//表数据
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		
		//部门核算
		String department = request.getParameter("department");
		department = department != null && OnlineUserInfo.getDept(department)!=null?OnlineUserInfo.getDept(department).departmentName:"";
		String deptName = department;
		if(department != null && !"".equals(department) && "".equals(deptName)){
			//当未找到部门名称时去查询数据库
			Result rs=mgt.getDeptName(department);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
				deptName=rs.retVal.toString();
			}
		}
		//保存查询的部门名称
		request.setAttribute("deptName", deptName);
		
		String accCode = request.getParameter("accCode") == null ? "" : request.getParameter("accCode");
		//查询方式（查询按钮，点击科目）
		String type = request.getParameter("type");
		
		/* 如果是点击头部的目录accCode必须减5位 */
		if (accCode.length() > 0 && type != null && ("update".equals(type) || "back".equals(type))) {
			accCode = accCode.substring(0, accCode.length() - 5);
		}
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/IniAccQueryAction.do");
		
		LoginBean lg = this.getLoginBean(request);
		
		//查询期初数据
		Result rs = mgt.queryIni(searchForm, accCode, sunClassCode, map, simpleAccFlag, type,
				department, this.getLocale(request).toString(), mop, this.getLoginBean(request));
		request.setAttribute("currPeriod", currPeriod);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			ArrayList rs_list = (ArrayList) rs.getRetVal();
			if (rs_list != null) {
				Object lastOne = "";
				if (rs_list.size() > 0) {
					lastOne = rs_list.get(rs_list.size() - 1);
				}
				//取最后一条数据
				request.setAttribute("parentJd", lastOne);
				if (rs_list.size() > 0) {
					rs_list.remove(rs_list.size() - 1);
				}

				//定义财务期初余额变量
				BigDecimal asset = new BigDecimal("0.00");
				BigDecimal liability = new BigDecimal("0.00");
				BigDecimal ownership = new BigDecimal("0.00");
				BigDecimal Cost = new BigDecimal("0.00");
				BigDecimal InCommon = new BigDecimal("0.00");

				//定义财务期初金额变量
				BigDecimal iniAsset = new BigDecimal("0.00");
				BigDecimal iniLiability = new BigDecimal("0.00");
				BigDecimal iniOwnership = new BigDecimal("0.00");
				BigDecimal iniCost = new BigDecimal("0.00");
				BigDecimal iniInCommon = new BigDecimal("0.00");

				//定义财务期初累计借变量
				BigDecimal debitAsset = new BigDecimal("0.00");
				BigDecimal debitLiability = new BigDecimal("0.00");
				BigDecimal debitOwnership = new BigDecimal("0.00");
				BigDecimal debitCost = new BigDecimal("0.00");
				BigDecimal debitInCommon = new BigDecimal("0.00");

				//定义财务期初累计贷变量
				BigDecimal creditAsset = new BigDecimal("0.00");
				BigDecimal creditLiability = new BigDecimal("0.00");
				BigDecimal creditOwnership = new BigDecimal("0.00");
				BigDecimal creditCost = new BigDecimal("0.00");
				BigDecimal creditInCommon = new BigDecimal("0.00");

				boolean first = false;
				for (int i = 0; i < rs_list.size(); i++) {
					Object[] obj = (Object[]) rs_list.get(i);
					if (obj[3].toString().equals(getMessage(request, "com.acc.asset"))) {
						asset = new BigDecimal(obj[7].toString().replace(",", ""));
						iniAsset = new BigDecimal(obj[4].toString().replace(",", ""));
						debitAsset = new BigDecimal(obj[5].toString().replace(",", ""));
						creditAsset = new BigDecimal(obj[6].toString().replace(",", ""));
						first = true;
					} else if (obj[3].toString().equals(getMessage(request, "com.acc.liability"))) {
						liability = new BigDecimal(obj[7].toString().replace(",", ""));
						iniLiability = new BigDecimal(obj[4].toString().replace(",", ""));
						debitLiability = new BigDecimal(obj[5].toString().replace(",", ""));
						creditLiability = new BigDecimal(obj[6].toString().replace(",", ""));
					} else if (obj[3].toString().equals(getMessage(request, "com.acc.ownership"))) {
						ownership = new BigDecimal(obj[7].toString().replace(",", ""));
						iniOwnership = new BigDecimal(obj[4].toString().replace(",", ""));
						debitOwnership = new BigDecimal(obj[5].toString().replace(",", ""));
						creditOwnership = new BigDecimal(obj[6].toString().replace(",", ""));
					} else if (obj[3].toString().equals(getMessage(request, "iniAcc.lb.Cost"))) {
						Cost = new BigDecimal(obj[7].toString().replace(",", ""));
						iniCost = new BigDecimal(obj[4].toString().replace(",", ""));
						debitCost = new BigDecimal(obj[5].toString().replace(",", ""));
						creditCost = new BigDecimal(obj[6].toString().replace(",", ""));
					} else if (obj[3].toString().equals(getMessage(request, "iniAcc.lb.profitloss"))) {
						InCommon = new BigDecimal(obj[7].toString().replace(",", ""));
						iniInCommon = new BigDecimal(obj[4].toString().replace(",", ""));
						debitInCommon = new BigDecimal(obj[5].toString().replace(",", ""));
						creditInCommon = new BigDecimal(obj[6].toString().replace(",", ""));
					}
				}

				String autoAdjust = request.getParameter("autoAdjust") == null ? "" : request.getParameter("autoAdjust");
				if ("true".equals(autoAdjust) && request.getAttribute("isBeginAcc") == null) {
					SysAccMgt sysAccMgt = new SysAccMgt();
					//只更新贷方余额，所有累计借-累计贷
					rs = sysAccMgt.autoAdjust(sunClassCode, iniAsset.add(iniCost).subtract(
							iniLiability.add(iniOwnership)).doubleValue(), creditAsset.add(creditCost)
							.subtract(debitLiability.add(debitOwnership)).doubleValue(), debitAsset.add(
							debitCost).add(debitLiability).add(debitOwnership).add(debitInCommon)
							.doubleValue()- creditAsset.add(creditCost).add(creditLiability).add(creditOwnership).add(
									creditInCommon).doubleValue(), asset.add(Cost).subtract(
							liability.add(ownership)).doubleValue(),department);
					if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						request.setAttribute("isBeginAcc", "true");
						new DynDBManager().addLog(14, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"自动调平财务期初;","tblAccBalance", "财务期初","调平期初");
						EchoMessage.success().add("自动调平成功！").setBackUrl("/IniAccQueryAction.do?department="+department).setAlertRequest(request);
						return getForward(request, mapping, "message");
					} else {
						EchoMessage.error().add(rs.retCode, request).setBackUrl(
								"/IniAccQueryAction.do?department="+department+"&winCurIndex="
										+ request.getParameter("winCurIndex")).setAlertRequest(request);
						return getForward(request, mapping, "message");
					}
				}
				request.setAttribute("ini", iniAsset.add(iniCost).doubleValue() == iniLiability.add(
						iniOwnership).doubleValue());

				//更新的期初试算平衡规则：（资产类借方累计发生额)+（成本率借方累计） +负债借方累计发生额+所有者权益借方累计发生额+损益类累借方发生额
				//        		                         =资产类贷方累计发生额+  成本类贷方累计+(负债贷方累计发生额)+  (所有者权益贷方累计发生额)+损益类贷方发生额

				request.setAttribute("debit", debitAsset.add(debitCost).add(debitLiability).add(
						debitOwnership).add(debitInCommon).doubleValue() == creditAsset.add(creditCost)
						.add(creditLiability).add(creditOwnership).add(creditInCommon).doubleValue());
				request.setAttribute("first", first);

				/* 设置当前位置 */
				DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,"tblAccTypeInfo");
				Result rs3 = new ReportDataMgt().getParentName(accCode, tableInfo,GlobalsTool.getLocale(request).toString());
				if (rs3.retCode == ErrorCanst.RET_NOTSETROWMARKER) {
					request.setAttribute("parentName", "");
				} else {
					ArrayList parentName = (ArrayList) rs3.retVal;
					String parentUrl = "";
					if(parentName.size()==0){
						parentUrl = getMessage(request, "com.acc.ini.root") ;
					}else{
						parentUrl += "<a style=\"color:blue;\" href=\"/IniAccQueryAction.do?type=back&accCode=" 
							+ "&winCurIndex=" + request.getParameter("winCurIndex") + "\">" +getMessage(request, "com.acc.ini.root")
							+ "</a> >> ";
						for (int i = 0; i < parentName.size(); i++) {
							String[] nameClass = (String[]) parentName.get(i);
							if(nameClass[1].length()==(parentName.size())*5){
								parentUrl += nameClass[0] ;
							}else{
								parentUrl += "<a style=\"color:blue;\" href=\"/IniAccQueryAction.do?type=back&accCode="+ nameClass[1]+"00001"
								+ "&operation=" + OperationConst.OP_QUERY + "&winCurIndex="
								+ request.getParameter("winCurIndex") + "\">" + nameClass[0] + "</a> >> ";
							}
							
						}
					}
					request.setAttribute("parentName", parentUrl);
				}

				request.setAttribute("accNumber", type==null?searchForm.getAccNumber():"");
				request.setAttribute("accName", searchForm.getAccName());				
				request.setAttribute("keyword", searchForm.getKeyword()) ;
				request.setAttribute("accCode", accCode);
				request.setAttribute("result", rs.getRetVal());
				request.setAttribute("pageBar", pageBar(rs, request));
			}else {
				//查询失败
				EchoMessage.error().add(rs.retCode, request).setRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		return getForward(request, mapping, "list");
	}
	
	/**
	 * 修改期初前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward importHistory(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String accNumber = request.getParameter("accNumber");			//科目accNumber
		LoginBean lg = this.getLoginBean(request);		
		/**
		 * 查询期初记录
		 */
		Result rs = mgt.getImportHistory(accNumber);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){			
			request.setAttribute("result", rs.retVal);
		}else {
			//加载失败
			EchoMessage.error().add(rs.getRetVal()==null?"加载失败":rs.getRetVal().toString()).setRequest(request);
		}
		return getForward(request, mapping, "importHistory");
	}
	
	/**
	 * 删除导入记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward delImport(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String accNumber = request.getParameter("accNumber");			//科目accNumber
		String impId = request.getParameter("impId");			//导入记录
		
		LoginBean lg = this.getLoginBean(request);		
		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		/**
		 * 查询期初记录
		 */
		Result rs = mgt.delImport(accNumber,impId,lg,this.getLocale(request),resources);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){			
			request.setAttribute("list", rs.retVal);
		}else {
			//加载失败
			EchoMessage.error().add(rs.getRetVal()==null?"加载失败":rs.getRetVal().toString()).setRequest(request);
		}
		return getForward(request, mapping, "importHistory");
	}
	
	/**
	 * 修改期初前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward updateIniPre(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String accCode = request.getParameter("accCode");				//科目classCode
		String accNumber = request.getParameter("accNumber");			//科目accNumber
		String pageSize = request.getParameter("pageSize");				
		String pageNo = request.getParameter("pageNo");		
		String conDepartmentCode = request.getParameter("conDepartmentCode");				
		String conEmployeeID = request.getParameter("conEmployeeID");	
		String conProjectCode = request.getParameter("conProjectCode");		
		String conStockCode = request.getParameter("conStockCode");		
		String conCompanyCode = request.getParameter("conCompanyCode");	
		String conCurrency = request.getParameter("conCurrency");	
		request.setAttribute("conCurrency", conCurrency);
		request.setAttribute("digits", GlobalsTool.getSysSetting("DigitsAmount"));
		
		
		
		String paramStr = "conDepartmentCode;conDepartmentCodeName;conEmployeeID;conEmployeeIDName;conProjectCode;conProjectCodeName;conStockCode;conStockCodeName;conCompanyCode;conCompanyCodeName";
				
		/* 读取页面明细的数据 */
		String[] str = paramStr.split(";");
		HashMap conMap = new HashMap();
		for(int i=0;i<str.length;i++){
			String s = str[i];
			String parameter = s;		
			conMap.put(s, request.getParameter(parameter));
		}	
		request.setAttribute("conMap", conMap);
		
		if(pageNo==null || pageNo.length()==0){
			pageNo="1";
		}
		if(pageSize==null || pageSize.length()==0){
			pageSize="100";
		}
		
		LoginBean lg = this.getLoginBean(request);
		
		/**
		 * 查询期初记录
		 */
		Result rs = mgt.getIniAccCode(accCode,  accNumber, this.getLocale(request).toString(), lg.getSunCmpClassCode(),
				pageSize,pageNo,conDepartmentCode,conEmployeeID,conProjectCode,conStockCode,conCompanyCode,conCurrency);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])rs.getRetVal();
			
			//期初数据
			HashMap map = (HashMap) obj[0];
			request.setAttribute("iniData", map);
			
			//期初明细
			request.setAttribute("iniList", obj[1]);
			
			//启用核算的项目
			request.setAttribute("isItem", obj[2]);
			//不启用核算的项目
			request.setAttribute("isNoItem", obj[3]);
			
			//是否启用核算项
			request.setAttribute("isCalculates", obj[5]);
			
			
			//外币
			request.setAttribute("currStr", obj[4]);
			
			request.setAttribute("currList", obj[6]);
			
			request.setAttribute("pageBar", pageBar(rs, request));
		}else {
			//加载失败
			EchoMessage.error().add(rs.getRetVal()==null?"加载失败":rs.getRetVal().toString()).setRequest(request);
		}
		return getForward(request, mapping, "dealIniAcc");
	}
	
	/**
	 * 修改期初前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward addIniItem(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String classCode = request.getParameter("accCode");				//classCode
		String accNumber = request.getParameter("accNumber");			//科目代码accNumber
		String accName = request.getParameter("accName");			//会计科目名称		
		if (!currPeriod.equals("-1")) {
			//已经开账不能修改期初信息
			EchoMessage.error().add(getMessage(request, "common.msg.RET_BEGINACC_END")).setBackUrl("/IniAccQueryAction.do").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
				
		/* 明细 */
		String paramStr = "BeginAmount;TotalDebit;TotalLend;TotalRemain;CompanyCode;DepartmentCode;EmployeeID;ProjectCode;StockCode;Remark";
		if("true".equals(openFCurrency)){
			paramStr += ";Currency;CurrencyRate;CurBeginAmount;CurTotalDebit;CurTotalLend;CurTotalRemain";
		}
		paramStr += ";Remark";
		
		/* 读取页面明细的数据 */
		String[] str = paramStr.split(";");
		HashMap map = new HashMap();
		for(int i=0;i<str.length;i++){
			String s = str[i];
			String parameter = s;		
			map.put(s, request.getParameter(parameter));
		}	
		
		
		map.put("AccCode", accNumber);
		
		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		if((Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString()) && "true".equals(openSunCompany)) || "false".equals(openSunCompany)) {
			//修改期初
			Result rs = mgt.addIniAccItem(map,accNumber,lg,this.getLocale(request),resources);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				new DynDBManager().addLog(0, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"修改科目期初:"+accNumber+"-"+accName,"tblAccBalance", "财务期初","");
				EchoMessage.success().add("执行成功").setClosePopWinRefresh(request.getParameter("popWinName")).setAlertRequest(request);
			}else if(rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR){
				//存储过程执行错误
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				String[] errorstr = errorMessage.split(";");
				String msg = "";
				if (errorstr.length == 1) {
					msg = this.getMessage(request, "common.error.negative2", errorstr[0]);
				} else {
					msg = this.getMessage(request, "common.error.negative", errorstr[0], errorstr[1]);
				}
				EchoMessage.error().add(msg).setAlertRequest(request);
			}else if(rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR) {
				EchoMessage.error().add(rs.retVal!=null?rs.retVal+",记录重复":getMessage(
			            request, "common.msg.updateErro")).setAlertRequest(request);
			}else {
				EchoMessage.error().add(rs.retVal!=null?rs.retVal+"":getMessage(
			            request, "common.msg.updateErro")).setAlertRequest(request);
			}
		} else {
			EchoMessage.error().add(getMessage(
					request, "common.msg.notLastSunCompany")).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	/**
	 * 修改期初前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward updateIniItem(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String classCode = request.getParameter("accCode");				//classCode
		String accNumber = request.getParameter("accNumber");			//科目代码accNumber
		String accName = request.getParameter("accName");			//会计科目名称		
		if (!currPeriod.equals("-1")) {
			//已经开账不能修改期初信息
			EchoMessage.error().add(getMessage(request, "common.msg.RET_BEGINACC_END")).setBackUrl("/IniAccQueryAction.do").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
				
		/* 明细 */
		String paramStr = "id;BeginAmount;TotalDebit;TotalLend;TotalRemain;CompanyCode;DepartmentCode;EmployeeID;ProjectCode;StockCode;Remark";
		if("true".equals(openFCurrency)){
			paramStr += ";Currency;CurrencyRate;CurBeginAmount;CurTotalDebit;CurTotalLend;CurTotalRemain";
		}
		paramStr += ";Remark;impId";
		
		/* 读取页面明细的数据 */
		String[] str = paramStr.split(";");
		HashMap map = new HashMap();
		for(int i=0;i<str.length;i++){
			String s = str[i];
			String parameter = s;		
			map.put(s, request.getParameter(parameter));
		}			
		map.put("AccCode", accNumber);
		
		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		if((Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString()) && "true".equals(openSunCompany)) || "false".equals(openSunCompany)) {
			//修改期初
			Result rs = mgt.updateIniAccItem(map,accNumber,lg,this.getLocale(request),resources);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"修改科目期初:"+accNumber+"-"+accName,"tblAccBalance", "财务期初","");
				EchoMessage.success().add("执行成功").setClosePopWinRefresh(request.getParameter("popWinName")).setAlertRequest(request);
			}else if(rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR){
				//存储过程执行错误
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				String[] errorstr = errorMessage.split(";");
				String msg = "";
				if (errorstr.length == 1) {
					msg = this.getMessage(request, "common.error.negative2", errorstr[0]);
				} else {
					msg = this.getMessage(request, "common.error.negative", errorstr[0], errorstr[1]);
				}
				EchoMessage.error().add(msg).setAlertRequest(request);
			}else if(rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR) {
				EchoMessage.error().add(rs.retVal!=null?rs.retVal+",记录重复":getMessage(
			            request, "common.msg.updateErro")).setAlertRequest(request);
			}else{
				EchoMessage.error().add(rs.retVal!=null?rs.retVal+"":getMessage(
			            request, "common.msg.updateErro")).setAlertRequest(request);
			}
		} else {
			EchoMessage.error().add(getMessage(
					request, "common.msg.notLastSunCompany")).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 删除期初前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward delIniItem(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String classCode = request.getParameter("accCode");				//classCode
		String accNumber = request.getParameter("accNumber");			//科目代码accNumber
		String accName = request.getParameter("oldaccName");			//会计科目名称		
		if (!currPeriod.equals("-1")) {
			//已经开账不能修改期初信息
			EchoMessage.error().add(getMessage(request, "common.msg.RET_BEGINACC_END")).setBackUrl("/IniAccQueryAction.do").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
				
		
		
		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		String[] list = request.getParameterValues("keyId");
		String backurl = "&pageSize=" + (request.getParameter("pageSize")==null?"":request.getParameter("pageSize")) + 
			"&pageNo=" + (request.getParameter("pageNo")==null?"":request.getParameter("pageNo")) + 
			"&conCurrency=" + (request.getParameter("conCurrency")==null?"":request.getParameter("conCurrency"));
		String paramStr = "conDepartmentCode;conDepartmentCodeName;conEmployeeID;conEmployeeIDName;conProjectCode;conProjectCodeName;conStockCode;conStockCodeName;conCompanyCode;conCompanyCodeName";
		/* 读取页面明细的数据 */
		String[] str = paramStr.split(";");
		for (int i = 0; i < str.length; i++) {
			String s = str[i];
			String parameter = s;
			backurl += "&" + s + "=" + (request.getParameter(parameter)==null?"":request.getParameter(parameter));
		}
		backurl = "/IniAccQueryAction.do?accCode=" + request.getParameter("accCode") + "&accNumber=" + request.getParameter("accNumber") + backurl + "&operation=7&popWinName="
				+ request.getParameter("popWinName");
		
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		if((Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString()) && "true".equals(openSunCompany)) || "false".equals(openSunCompany)) {
			//修改期初
			Result rs = mgt.delIniAccItem(list,accNumber,lg,this.getLocale(request),resources);
			
			
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				new DynDBManager().addLog(2, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"修改科目期初:"+accNumber+"-"+accName,"tblAccBalance", "财务期初","");
				EchoMessage.success().add("执行成功").setBackUrl(backurl).setAlertRequest(request);
			}else if(rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR){
				//存储过程执行错误
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				String[] errorstr = errorMessage.split(";");
				String msg = "";
				if (errorstr.length == 1) {
					msg = this.getMessage(request, "common.error.negative2", errorstr[0]);
				} else {
					msg = this.getMessage(request, "common.error.negative", errorstr[0], errorstr[1]);
				}
				EchoMessage.error().add(msg).setBackUrl(backurl).setAlertRequest(request);
			}else{
				EchoMessage.error().add(rs.retVal!=null?rs.retVal+"":getMessage(
			            request, "common.msg.updateErro")).setBackUrl(backurl).setAlertRequest(request);
			}
		} else {
			EchoMessage.error().add(getMessage(
					request, "common.msg.notLastSunCompany")).setBackUrl(backurl).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 修改期初前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward addIniItemPre(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String accCode = request.getParameter("accCode");				//科目classCode
		String accNumber = request.getParameter("accNumber");			//科目accNumber
		request.setAttribute("accCode", accCode);
		request.setAttribute("accNumber", accNumber);
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		LoginBean lg = this.getLoginBean(request);
		request.setAttribute("digits", GlobalsTool.getSysSetting("DigitsAmount"));
		
		/**
		 * 查询期初记录
		 */
		Result rs = mgt.getAccTypeCal(accNumber);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])rs.getRetVal();
						
			//启用核算的项目
			request.setAttribute("isItem", obj[0]);
			
			//是否启用核算项
			request.setAttribute("isCalculates", obj[2]);
			
			//外币
			request.setAttribute("currStr", obj[1]);
			
			request.setAttribute("currList", obj[3]);
			
			request.setAttribute("accName", obj[4]);
			
			HashMap accMap =  new HashMap();
			accMap.put("CurBeginAmount", 0);
			accMap.put("CurTotalDebit", 0);
			accMap.put("CurTotalLend", 0);
			accMap.put("CurTotalRemain", 0);
			
			accMap.put("BeginAmount", 0);
			accMap.put("TotalDebit", 0);
			accMap.put("TotalLend", 0);
			accMap.put("TotalRemain", 0);
			request.setAttribute("accMap", accMap);
		}else {
			//加载失败
			EchoMessage.error().add(rs.getRetVal().toString()).setRequest(request);
		}
		return getForward(request, mapping, "dealIniAccItem");
	}
	/**
	 * 修改期初前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward updateIniItemPre(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String accCode = request.getParameter("accCode");				//科目classCode
		String accNumber = request.getParameter("accNumber");			//科目accNumber
		request.setAttribute("accCode", accCode);
		request.setAttribute("accNumber", accNumber);
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		LoginBean lg = this.getLoginBean(request);
		request.setAttribute("digits", GlobalsTool.getSysSetting("DigitsAmount"));
		
		/**
		 * 查询期初记录
		 */
		Result rs = mgt.getAccTypeCal(accNumber);
		Result rs1 = mgt.getAccItem(request.getParameter("keyId"),this.getLocale(request).toString());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs1.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])rs.getRetVal();
						
			//启用核算的项目
			request.setAttribute("isItem", obj[0]);
			
			//是否启用核算项
			request.setAttribute("isCalculates", obj[2]);
			
			//外币
			request.setAttribute("currStr", obj[1]);
			
			request.setAttribute("currList", obj[3]);
			request.setAttribute("accName", obj[4]);
			request.setAttribute("accMap", rs1.retVal);
			
			
		}else {
			//加载失败
			EchoMessage.error().add(rs.getRetVal().toString()).setRequest(request);
		}
		return getForward(request, mapping, "dealIniAccItem");
	}
	
	
	/**
	 * 详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param reponse
	 * @return
	 */
	protected ActionForward detailIni(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse reponse){
		
		String accCode = request.getParameter("accCode");				//科目classCode
		String accNumber = request.getParameter("accNumber");			//科目accNumber
		String pageSize = request.getParameter("pageSize");				
		String pageNo = request.getParameter("pageNo");		
		String conDepartmentCode = request.getParameter("conDepartmentCode");				
		String conEmployeeID = request.getParameter("conEmployeeID");	
		String conProjectCode = request.getParameter("conProjectCode");		
		String conStockCode = request.getParameter("conStockCode");		
		String conCompanyCode = request.getParameter("conCompanyCode");	
		String conCurrency = request.getParameter("conCurrency");	
		
		String paramStr = "conDepartmentCode;conDepartmentCodeName;conEmployeeID;conEmployeeIDName;conProjectCode;conProjectCodeName;conStockCode;conStockCodeName;conCompanyCode;conCompanyCodeName";
		
		/* 读取页面明细的数据 */
		String[] str = paramStr.split(";");
		HashMap conMap = new HashMap();
		for(int i=0;i<str.length;i++){
			String s = str[i];
			String parameter = s;		
			conMap.put(s, request.getParameter(parameter));
		}	
		request.setAttribute("conMap", conMap);
		
		
		LoginBean lg = this.getLoginBean(request);
		
		/**
		 * 查询期初记录
		 */
		Result rs = mgt.getIniAccCode(accCode,  accNumber, this.getLocale(request).toString(), lg.getSunCmpClassCode(),
				pageSize,pageNo,conDepartmentCode,conEmployeeID,conProjectCode,conStockCode,conCompanyCode,conCurrency);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])rs.getRetVal();
			
			//期初数据
			HashMap map = (HashMap) obj[0];
			request.setAttribute("iniData", map);
			
			//期初明细
			request.setAttribute("iniList", obj[1]);
			
			//启用核算的项目
			request.setAttribute("isItem", obj[2]);
			//不启用核算的项目
			request.setAttribute("isNoItem", obj[3]);
			
			//是否启用核算项
			request.setAttribute("isCalculates", obj[5]);
			
			//外币
			request.setAttribute("currStr", obj[4]);
			
			request.setAttribute("currList", obj[6]);

			try {
				/* 获取本位币 */
				GlobalMgt gmgt = new GlobalMgt();
				Object[] objr = gmgt.getBaseCurrency();
				request.setAttribute("baseCur", objr[1]);
			} catch (Exception ex) {
				EchoMessage.error().add(ex.getMessage()).setRequest(request);
			}
			request.setAttribute("detail", "true");
		}else {
			//加载失败
			EchoMessage.error().add(rs.getRetVal().toString()).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
			return getForward(request, mapping, "alert");
		}
		return getForward(request, mapping, "dealIniAcc");
	}
	/**
	 * 修改期初
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updateIni(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String classCode = request.getParameter("accCode");				//classCode
		String accNumber = request.getParameter("accNumber");			//科目代码accNumber
		String accName = request.getParameter("oldaccName");			//会计科目名称
		String keyid = request.getParameter("id");						//id
		if (!currPeriod.equals("-1")) {
			//已经开账不能修改期初信息
			EchoMessage.error().add(getMessage(request, "common.msg.RET_BEGINACC_END")).setBackUrl("/IniAccQueryAction.do").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		
		boolean falg = false;
		String isItems = request.getParameter("isItems");
		if(isItems != null && "true".equals(isItems)){
			//当未启用任何核算项目时，金额取主表的金额
			falg = true;
		}
		
		/* 明细 */
		String paramStr = "BeginAmount;TotalDebit;TotalLend;TotalRemain;CompanyCode;DepartmentCode;EmployeeID;ProjectCode;StockCode;Remark";
		if("true".equals(openFCurrency)){
			paramStr += ";Currency;CurrencyRate;CurBeginAmount;CurTotalDebit;CurTotalLend;CurTotalRemain";
		}
		
		/* 读取页面明细的数据 */
		String[] str = paramStr.split(";");
		List list = new ArrayList();
		for(int i=0;i<str.length;i++){
			String s = str[i];
			String parameter = s;
			if(falg && !"Currency".equals(s) && !"CurrencyRate".equals(s)){
				parameter += "s";
			}
			String[] paramValues = request.getParameterValues(parameter);
			if(paramValues != null){
				for(int j=0;j<paramValues.length;j++){
					HashMap map = new HashMap();
					if(list.size()>j){
						map = (HashMap)list.get(j);
						map.put(s, paramValues[j]);
						list.set(j, map);
					}else{
						map.put(s, paramValues[j]);
						list.add(map);
					}
				}
			}
		}
		/**
		 * 删除空值的数据
		 */
		List newList = new ArrayList();
		for(int i=0;i<list.size();i++){
			HashMap map = (HashMap)list.get(i);
			if(!(isValue(map.get("BeginAmount")) && isValue(map.get("TotalDebit")) 
					&& isValue(map.get("TotalLend")) && isValue(map.get("TotalRemain")))){
				newList.add(map);
			}
		}	
		
		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		String backurl= "/IniAccQueryAction.do?operation=7&accCode="+classCode+"&accNumber="+accNumber+"&keyid="+keyid+"&popWinName="+request.getParameter("popWinName");
		if((Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString()) && "true".equals(openSunCompany)) || "false".equals(openSunCompany)) {
			//修改期初
			Result rs = mgt.updateIniAcc(newList,keyid,classCode,accNumber,sunClassCode,openFCurrency,lg,this.getLocale(request),resources,"update");
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"修改科目期初:"+accNumber+"-"+accName,"tblAccBalance", "财务期初","");
				EchoMessage.success().add(getMessage(
	                request, "common.msg.updateSuccess")).setBackUrl(backurl).setAlertRequest(request);
			}else if(rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR){
				//存储过程执行错误
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				String[] errorstr = errorMessage.split(";");
				String msg = "";
				if (errorstr.length == 1) {
					msg = this.getMessage(request, "common.error.negative2", errorstr[0]);
				} else {
					msg = this.getMessage(request, "common.error.negative", errorstr[0], errorstr[1]);
				}
				EchoMessage.error().add(msg).setBackUrl(backurl).setAlertRequest(request);
			}else  if(rs.getRetVal() != null && !rs.getRetVal().equals("")){
				String msg = new DynDBManager().getDefSQLMsg(getLocale(request).toString(), rs.getRetVal().toString());
				EchoMessage.error().add(rs.getRetVal()+"").setBackUrl(backurl).setAlertRequest(request);
			}else{
				EchoMessage.error().add(getMessage(
		            request, "common.msg.updateErro")).setBackUrl(backurl).setAlertRequest(request);
			}
		} else {
			EchoMessage.error().add(getMessage(
					request, "common.msg.notLastSunCompany")).setBackUrl(backurl).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	
	/**
	 * 判断值是否存在或者为零
	 * @param o
	 * @return
	 */
	public boolean isValue(Object o){
		boolean isYes = false;
		if(o == null || "".equals(o) || Float.valueOf(o.toString())==0){
			isYes = true;
		}
		return isYes;
	}
	
	
	/**
	 * 导入期初前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward importPre(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		return getForward(request, mapping, "importIniAcc");
	}
	
	
	/**
	 * 下载期初模板
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward downModule(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		File file = new File("../../AIOBillExport");
		if(!file.exists()){
			file.mkdirs() ;
		}
		String moduleName = "财务期初";
		
		String fileName = file.getAbsolutePath()+"\\"+moduleName+"_"+getLoginBean(request).getEmpFullName()+".xls";
		FileOutputStream fos = new FileOutputStream(fileName);
		ArrayList<ExportField> exportList = new ArrayList<ExportField>();								//保存的字段头部
		ArrayList<HashMap> exportValueList = new ArrayList<HashMap>();									//数据
		Object localeObj = request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (localeObj == null) {
			localeObj = request.getSession().getServletContext().getAttribute("DefaultLocale");
		}
		final String locale = localeObj == null ? "" : localeObj.toString();
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);	//查询所有表结构
		List<DBFieldInfoBean> fieldInfos = ((DBTableInfoBean) allTables.get("tblIniAccDet")).getFieldInfos();
		DBTableInfoBean mainTable = GlobalsTool.getTableInfoBean("tblIniAccDet");
		
		
		
		String accCode = request.getParameter("accCode");				//科目classCode
		String accNumber = request.getParameter("accNumber");			//科目accNumber
		request.setAttribute("accCode", accCode);
		request.setAttribute("accNumber", accNumber);
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		LoginBean lg = this.getLoginBean(request);
		
		String tn = "期初明细";
		ExportField ef = null;
		Result rs = mgt.getAccTypeCal(accNumber);
		boolean hasCurrency = false;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])rs.getRetVal();
			
			tn = tn+"("+obj[4]+")";
			
			List<String>  isItem = (List<String>)obj[0];			
			for(String item:isItem){
				String[] str = item.split(";");
				ef = new ExportField("main", "", str[1],0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+str[1], locale), tn,false);
				exportList.add(ef);
			}
			
			String currStr = (String)obj[1];
			String[] str = currStr.split(";");
			//有外币
			if("1".equals(str[0])){	
				hasCurrency = true;
				ef = new ExportField("main", "", "Currency",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.Currency", locale), tn,false);
				exportList.add(ef);
				
				ef = new ExportField("main", "", "CurrencyRate",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.CurrencyRate", locale), tn,false);
				exportList.add(ef);
				ef = new ExportField("main", "", "CurBeginAmount",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.CurBeginAmount", locale), tn,false);
				exportList.add(ef);
				ef = new ExportField("main", "", "CurTotalDebit",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.CurTotalDebit", locale), tn,false);
				exportList.add(ef);
				ef = new ExportField("main", "", "CurTotalLend",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.CurTotalLend", locale), tn,false);
				exportList.add(ef);
				ef = new ExportField("main", "", "CurTotalRemain",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.CurTotalRemain", locale), tn,false);
				exportList.add(ef);
			}else{
				ef = new ExportField("main", "", "BeginAmount",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.BeginAmount", locale), tn,false);
				exportList.add(ef);
				ef = new ExportField("main", "", "TotalDebit",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.TotalDebit", locale), tn,false);
				exportList.add(ef);
				ef = new ExportField("main", "", "TotalLend",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.TotalLend", locale), tn,false);
				exportList.add(ef);
				ef = new ExportField("main", "", "TotalRemain",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.TotalRemain", locale), tn,false);
				exportList.add(ef);
			}
			ef = new ExportField("main", "", "TotalRemain",0, GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet.Remark", locale), tn,false);
			exportList.add(ef);
		}else {
			EchoMessage.error().add(getMessage(request, "com.export.failure")).setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		
		

		/* 导出模板 */
		Result result = ExportData.billExport(fos, moduleName, exportList, exportValueList, moduleName);
		fos.close();
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			//导出盘点单模板
			String downUrl = "/ReadFile?tempFile=export&fileName="+GlobalsTool.encode(moduleName+"_"+getLoginBean(request).getEmpFullName()+".xls");
			request.setAttribute("msg", downUrl);
			return getForward(request, mapping, "blank");
		}else {
			EchoMessage.error().add(getMessage(request, "com.export.failure")).setNotAutoBack().setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 导入期初数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward importIniAcc(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Locale locale = this.getLocale(request);
		String newDates= request.getParameter("newDate");
		if(!"".equals(newDate) && newDate.equals(newDates)){
			return mapping.findForward("importIniAcc");
		}
		newDate = newDates;
		
		String urls = "/IniAccQueryAction.do?operation=2&opType=importIniAcc";
		
		Hashtable<String, DBTableInfoBean> allTables = (Hashtable<String, DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/IniAccQueryAction.do");
		//得到表结构
		DBTableInfoBean tableInfoBean = allTables.get("tblIniAccDet");
		String mainTableDisplay = tableInfoBean.getDisplay().get(locale.toString()); //记录主表的中文名字
		try{
			//导入模板名称
	        ImportForm importForm = (ImportForm) form;
	        JXLTOOL jxlTool = new JXLTOOL(importForm.getFileName());
	        Result exceldatars = jxlTool.getExcelData(false);
	        if (exceldatars.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
	        	//读取数据成功
	        	//检查模板中表名否正确		        
				for(int i=0;i<jxlTool.getTopTitle().size();i++){
					String[] ss = jxlTool.getTopTitle().get(i);
					boolean isSame = false;
					
					if(mainTableDisplay.equals(ss[0])){
						isSame = true;
					}
					if(!isSame){
						EchoMessage.error().add("请检查导入模板中表名\""+ss[0]+"\"是否正确").setAlertRequest(request);
						return mapping.findForward("alert");
					}
				}
				ArrayList<DBFieldInfoBean> fieldInfoList = new ArrayList<DBFieldInfoBean>();
				
				fieldInfoList.addAll(tableInfoBean.getFieldInfos());
				List<HashMap<String, ExcelFieldInfoBean>> list = (List<HashMap<String, ExcelFieldInfoBean>>) exceldatars.getRetVal();
				jxlTool.close();
				
				/**
				 * 导入处理
				 */
				int success = 0;							//成功条数
				Result rs = importDataDeal(request, tableInfoBean, list, jxlTool, mainTableDisplay, locale, mop, this.getLoginBean(request));
				success = Integer.parseInt(rs.getRetVal().toString());
				int error = list.size() - success;			//导入失败条数
				
				request.setAttribute("totalimport", list.size());
				request.setAttribute("successimport", success);
				request.setAttribute("errorimport", error);
				request.setAttribute("fileName", fileName);
				request.setAttribute("showRsDiv", "true");
				return mapping.findForward("importIniAcc");
	        } else {
				jxlTool.close();
				// 弹出错误信息,读取数据失败
				EchoMessage.error().add(GlobalsTool.getMessage(locale, "excel.lb.readerror")).setBackUrl(urls).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
		}catch (Exception e) {
			EchoMessage.error().add(GlobalsTool.getMessage(locale, "common.msg.error")).setBackUrl(urls).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
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
	 */
	public Result importDataDeal(HttpServletRequest request, DBTableInfoBean tableInfoBean,List<HashMap<String, ExcelFieldInfoBean>> list,JXLTOOL jxlTool, 
			String mainTableDisplay, Locale locale,MOperation mop,LoginBean lg){
		
		//记录导入失败的数据
		List<HashMap<String, ExcelFieldInfoBean>> errorList = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
		Result rs = new Result();
		
		Object ob = request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		ImportDataBean importBean = new ImportDataBean();
		importBean.setName(mainTableDisplay);
		importBean.setTargetTable("tblIniAccDet");
		
		/* 保存在内存中所有表结构的信息 */
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		
		String msgModuleNot = GlobalsTool.getMessage(locale, "importData.moduleNot");
		String msgDownload = GlobalsTool.getMessage(locale, "importData. downloadRightModule");
		/* 对数据进行处理 */
		int success = 0;
		for (HashMap<String, ExcelFieldInfoBean> excelMap : list) {
			List arrayList = new ArrayList();
			List<HashMap<String, ExcelFieldInfoBean>> group = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
			group.add(excelMap);
			HashMap values = new HashMap();
			rs = jxlTool.replaceData(importBean, excelMap, values, locale.toString(), allTables, 
					new ArrayList(), new HashMap<String, ExcelFieldInfoBean>(), msgModuleNot, msgDownload, resources, lg, "", props,
					mop, new HashMap(), 0, true);
			if (rs.retCode == ErrorCanst.EXCEL_READ_FIELDNOTEXIST) {
				groupErrorHandler(GlobalsTool.getMessage(locale,"import.not.exist.column"), group, errorList);
				break;
			}
			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				groupErrorHandler(rs.getRetVal().toString(), group, errorList);
				break;
			}
			Object accCode = values.get("AccCode");
			if(accCode == null || "".equals(accCode)){
				groupErrorHandler("会计科目不能为空或者填写不正确！", group, errorList);
				continue;
			}
			/**
			 * 删除空值的数据
			 */
			if(isValue(values.get("BeginAmount")) && isValue(values.get("TotalDebit")) 
					&& isValue(values.get("TotalLend")) && isValue(values.get("TotalRemain"))){
				groupErrorHandler("金额未输入！", group, errorList);
				continue;
			}
			arrayList.add(values);
			//修改期初
			rs = mgt.updateIniAcc(arrayList,"","","",sunClassCode,openFCurrency,lg,this.getLocale(request),resources,"import");
			
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				String msg = new DynDBManager().getDefSQLMsg(getLocale(request).toString(), rs.getRetVal().toString());
				groupErrorHandler(msg, group, errorList);
				continue;
			}
			new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"导入科目期初:"+values.get("AccCode"),"tblAccBalance", "财务期初","");
			success++;
		}
		
		rs.setRetVal(success);
		fileName = "../log/" + tableInfoBean.getTableName() + "_" + lg.getId() + ".xls";
		File file = new File(fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		String error = GlobalsTool.getMessage(locale, "excel.error.title");
		jxlTool.writeExcel(fileName, errorList, error);
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
