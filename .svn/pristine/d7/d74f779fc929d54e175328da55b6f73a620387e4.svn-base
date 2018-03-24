package com.menyi.aio.web.sysAcc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import java.io.File;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Hashtable;

import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.systemSafe.SystemSafeMgt;

import java.util.*;

import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.bean.*;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.goodsProp.GoodsPropMgt;
import com.menyi.aio.web.iniSet.IniAccMgt;
import com.menyi.aio.web.iniSet.IniAccSearchForm;
import com.menyi.aio.bean.GoodsPropInfoBean;

/**
 * <p>
 * Title: 单位管理控制类
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: 周新宇
 * </p>
 * 
 * @author 周新宇
 * @version 1.0
 */
public class SysAccAction extends MgtBaseAction {

	SysAccMgt mgt = new SysAccMgt();

	String sunCompany = null;

	DynDBManager dbmgt = new DynDBManager();

	public static boolean sureSubmit = false;

	public SysAccAction() {
	}

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
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String type2 = request.getParameter("type2") == null ? "" : request.getParameter("type2");
		String exe = request.getParameter("exe");
		ActionForward forward = null;

		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		sunCompany = lg.getSunCmpClassCode();
		if (exe != null && exe.equals("exe")) {
			// 得到分支机构子级信息
			Hashtable<String, SystemSettingBean> systemSet = BaseEnv.systemSet;
			String OpenSunCompany = systemSet.get("sunCompany").getSetting();
			if (!Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString()) && OpenSunCompany.equals("true")) {
				forward = getForward(request, mapping, "message");
				EchoMessage.error().add(getMessage(request, "common.msg.notLastSunCompany")).setAlertRequest(request);
				return forward;
			}

			OnlineUserInfo.lockSystem();// 当做以下操作的时候锁定系统，不允许其他用户操作
			OnlineUserInfo.setLockOper("");
			// 跟据不同操作类型分配给不同函数处理
			if (type.equals("beginAcc")) {
				String beginAccType=request.getParameter("beginAccType");
				if("setAccPeriod".equals(beginAccType)){
					forward = setAccPeriod(mapping, form, request, response);
				}else{
					forward = beginAcc(mapping, form, request, response);
				}
			} else if (type.equals("reBeginAcc")) {
				forward = reBeginAcc(mapping, form, request, response);
			} else if("settleAcc".equals(type)){				
				if ("billSettleAcc".equals(type2)) {//业务月结	
					forward = billSettleAcc(mapping, form, request, response);
				} else if ("AccSettleAcc".equals(type2)) {//财务月结
					forward = AccSettleAcc(mapping, form, request, response);
				} else if (type2.equals("adjustExchange")) {//期末调汇
					forward = adjustExchange(mapping, form, request, response);
				}else if ("settleProfitLoss".equals(type2)) {//结转损益
					forward = settleProfitLoss(mapping, form, request, response);
				}
			}else if (type.equals("reSettleAcc")) {
				if (type2.equals("AccReSettleAcc")) {//财务反月结			
					forward = AccReSettleAcc(mapping, form, request, response);
				}else if (type2.equals("reSettleAcc")) {//业务反月结
					forward = reSettleAcc(mapping, form, request, response);
				}
			} else if (type.equals("yearSettleAcc")) {
				forward = yearSettleAcc(mapping, form, request, response);
			} else if (type.equals("reCalculate")) {
				forward = reCalculate(mapping, form, request, response);
			} else if (type.equals("delBill")) {
				forward = delBill(mapping, form, request, response);
			} else if ("bakAccount".equals(type)) {
				forward = bakAccount(mapping, form, request, response);
			} 
			if(!"billSettleAcc".equals(type2)||("billSettleAcc".equals(type2)&&!"Settleing".equals(request.getAttribute("SettleStatus")))){ 
				OnlineUserInfo.unlockSystem();// 当以上操作完成的时候解除锁定
			}

		} else {
			forward = prepare(mapping, form, request, response);
		}
		// 设置系统设置值当前会计期间
		mgt.setCurrPeriodSysParam(sunCompany, this.getLoginBean(request).getId());
		return forward;

	}

	/**
	 * 备份帐套
	 */
	protected ActionForward bakAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = getForward(request, mapping, "message");
		Result rb = new Result();
		Result rs = new SystemSafeMgt().querySafeValues();
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			rs.setRetVal("查询备份路径异常");
		}else{
			HashMap<String,String> bmap = (HashMap)rs.retVal;
			
			String backPath = bmap.get("backPath"); //数据备份保存路径
			if(backPath==null || backPath.length()  == 0){
				String userDir = System.getProperty("user.dir");
				String defDisk = userDir.substring(0,userDir.indexOf(":")+1);
				backPath = defDisk+"\\AioDefDbBakup";
			}			
			rs = mgt.backupDataBase(backPath);
		}
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.error().add(getMessage(request, "com.auto.error") + rs.retVal).setBackUrl("/SysAccAction.do?type=bakAccount&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(
					request);
			return forward;
		} else {
			new DynDBManager().addLog(14, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),
					 "备份帐套", "", "备份帐套","备份帐套");
			EchoMessage.success().add(getMessage(request, "com.bak.bakSucess") + rs.retVal).setBackUrl("/SysAccAction.do?type=bakAccount").setNotAutoBack().setRequest(request);
		}
		return forward;
	}

	protected ActionForward autoBeginAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		LoginBean login = this.getLoginBean(request);
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
		if (pageNo == 0)
			pageNo = 1;
		if (pageSize == 0)
			pageSize = GlobalsTool.getPageSize();
		IniAccSearchForm searchForm = new IniAccSearchForm();
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/IniAccQueryAction.do");
		//Result rs = iniMgt.query(login.getSunCmpClassCode(), "", "", "", "", "", map, "", this.getLocale(request).toString(), "", pageNo, pageSize);
		Result rs = new IniAccMgt().queryIni(searchForm, "", login.getSunCmpClassCode(), map, "", "", "", this.getLocale(request).toString(),mop,login);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			ArrayList rs_list = (ArrayList) rs.getRetVal();
			if (rs_list != null) {
				Object lastOne = "";
				if (rs_list.size() > 0) {
					lastOne = rs_list.get(rs_list.size() - 1);
				}
				request.setAttribute("parentJd", lastOne);
				if (rs_list.size() > 0) {
					rs_list.remove(rs_list.size() - 1);
				}

				// 定义财务期初余额变量
				BigDecimal asset = new BigDecimal("0.00");
				BigDecimal liability = new BigDecimal("0.00");
				BigDecimal ownership = new BigDecimal("0.00");
				BigDecimal Cost = new BigDecimal("0.00");
				BigDecimal InCommon = new BigDecimal("0.00");

				// 定义财务期初金额变量
				BigDecimal iniAsset = new BigDecimal("0.00");
				BigDecimal iniLiability = new BigDecimal("0.00");
				BigDecimal iniOwnership = new BigDecimal("0.00");
				BigDecimal iniCost = new BigDecimal("0.00");
				BigDecimal iniInCommon = new BigDecimal("0.00");

				// 定义财务期初累计借变量
				BigDecimal debitAsset = new BigDecimal("0.00");
				BigDecimal debitLiability = new BigDecimal("0.00");
				BigDecimal debitOwnership = new BigDecimal("0.00");
				BigDecimal debitCost = new BigDecimal("0.00");
				BigDecimal debitInCommon = new BigDecimal("0.00");

				// 定义财务期初累计贷变量
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

				SysAccMgt mgt = new SysAccMgt();
				// 只更新贷方余额，所有累计借-累计贷
				rs = mgt
						.autoAdjust(login.getSunCmpClassCode(), iniAsset.add(iniCost).subtract(iniLiability.add(iniOwnership)).doubleValue(), creditAsset.add(creditCost).subtract(
								debitLiability.add(debitOwnership)).doubleValue(), debitAsset.add(debitCost).add(debitLiability).add(debitOwnership).add(debitInCommon).doubleValue()
								- creditAsset.add(creditCost).add(creditLiability).add(creditOwnership).add(creditInCommon).doubleValue(), asset.add(Cost).subtract(liability.add(ownership))
								.doubleValue(), "");
				if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					EchoMessage.error().add(rs.retCode, request).setBackUrl("/IniAccQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex")).setAlertRequest(request);
					return getForward(request, mapping, "message");
				}

				// 更新的期初试算平衡规则：（资产类借方累计发生额)+（成本率借方累计）
				// +负债借方累计发生额+所有者权益借方累计发生额+损益类累借方发生额
				// =资产类贷方累计发生额+ 成本类贷方累计+(负债贷方累计发生额)+ (所有者权益贷方累计发生额)+损益类贷方发生额
			}
		} else {
			// 查询失败
			EchoMessage.error().add(rs.retCode, request).setRequest(request);
			return getForward(request, mapping, "message");
		}

		// 开账
		rs = mgt.beginAcc(login.getId(), sunCompany, map, "true");

		forward = getForward(request, mapping, "message");
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			rs = mgt.getCurrPeriod(sunCompany);// ////////
			request.getSession().setAttribute("NowPeriod", ((AccPeriodBean) rs.getRetVal()).getAccMonth());
			request.setAttribute("AUTO_JUMP", "true");
			// 开账成功
			EchoMessage.success().setNotAutoBack().add(getMessage(request, "common.msg.beginAccSuccess")).setBackUrl(
					"/SysAccAction.do?type=beginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
		}else if (rs.getRetCode() == ErrorCanst.RET_BEGINACC_NOBEGINPERIOD) {

			EchoMessage.error().add(this.getMessage(request, "sysacc.begin.nobeginperiod")).setBackUrl("/SysAccAction.do?type=beginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(
					request);
		} else if (rs.getRetCode() == ErrorCanst.RET_BEGINACC_END) {
			String fromPage = request.getParameter("fromPage");
			if (fromPage != null && fromPage.contains("guide")) {
				EchoMessage.error().add(this.getMessage(request, "common.msg.RET_BEGINACC_END")).setBackUrl("/vm/guide/" + fromPage).setAlertRequest(request);
			} else {
				EchoMessage.error().add(this.getMessage(request, "common.msg.RET_BEGINACC_END")).setAlertRequest(request);
			}
		} else {
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/SysAccAction.do?type=beginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
			}
		}
		return forward;
	}

	protected ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String text = "";

		if (type.equals("settleAcc") || type.equals("newSettleAcc")) {
			Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			Result rs = mgt.isAbleSettleAcc(map, this.getLocale(request).toString(), sunCompany);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList obj = (ArrayList) rs.getRetVal();
				if (obj.size() > 0) {
					request.setAttribute("result", obj);
					// 单据的凭证没有被创建的消息
					text = getMessage(request, "sysAcc.msg.accNotBuild") + ",";
				}
				if ("true".equals(request.getParameter("notAuditBill"))) {
					request.setAttribute("notAuditBill", request.getSession().getAttribute("notAuditBill"));
					request.getSession().removeAttribute("notAuditBill");
				}
				if ("true".equals(request.getParameter("draftBill"))) {
					request.setAttribute("draftBill", request.getSession().getAttribute("draftBill"));
					request.getSession().removeAttribute("draftBill");
				}
			} else if (rs.getRetCode() == ErrorCanst.RET_SETTLEACC_END) {// 已经月结				
				EchoMessage.error().add(getMessage(request, "common.msg.RET_SETTLEACC_END")).setBackUrl("/welcome.htm").setRequest(request);
				forward = getForward(request, mapping, "message");
				return forward;
			} else {// 月结失败				
				EchoMessage.error().add(getMessage(request, "common.msg.error")).setBackUrl("/welcome.htm").setRequest(request);
				forward = getForward(request, mapping, "message");
				return forward;
			}
		}

		if (type.equals("beginAcc")) {
			/* 开账 */ 
			//开账期间
			String date = request.getParameter("accPeriodDate");
			if(!"".equals(date) && null != date){
				request.setAttribute("currentlyPeriod",date);
			}else{
				Result result = mgt.getCurrentlyPeriod();
				request.setAttribute("currentlyPeriod", result.getRetVal());
			}
			text = getMessage(request, "common.msg.isBeginAcc");
			forward = mapping.findForward("openAcc");
		} else if (type.equals("reBeginAcc")) {
			//系统反开账
			text = getMessage(request, "common.msg.isReBeginAcc");
			forward = mapping.findForward("reOpenAcc");
		}  else if (type.equals("settleAcc")) {
			text = text + getMessage(request, "common.msg.isSettleAcc");
			Result rs = mgt.getSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				if(list.size() == 0){
					//系统未开账，不能月结
					EchoMessage.error().add("<a href='/SysAccAction.do?type=beginAcc&src=menu'>系统未开账，无法进行月结</a>").setNotAutoBack().setAlertRequest(request);
					forward = mapping.findForward("message");
					return forward;
				}
				request.setAttribute("from", list.get(0));
				request.setAttribute("to", list.get(1));
			}
			rs = mgt.getAccSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("Accfrom", list.get(0));
				request.setAttribute("Accto", list.get(1));
			}
			forward = mapping.findForward("settleAcc");
		}else if (type.equals("reSettleAcc")) {
			//反月结
			text = getMessage(request, "common.msg.isReSettleAcc");
			Result rs = mgt.getReSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("from", list.get(0));
				request.setAttribute("to", list.get(1));
				request.setAttribute("flag", "true");
			} else if (rs.getRetCode() == ErrorCanst.ER_NO_DATA) {
				request.setAttribute("flag", "false");
			}
			rs = mgt.getReSettlePeriodFromToA();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("fromA", list.get(0));
				request.setAttribute("toA", list.get(1));
				request.setAttribute("flagA", "true");
			} else if (rs.getRetCode() == ErrorCanst.ER_NO_DATA) {
				request.setAttribute("flagA", "false");
			}
			forward = mapping.findForward("reCloseAcc");
		} else if (type.equals("yearSettleAcc")) {
			text = getMessage(request, "common.msg.isYearSettleAcc");
			Result rs = mgt.existNewYearBill();
			request.setAttribute("existNewYearBill", rs.getRetVal());
			forward = mapping.findForward("yearSettleAcc");
		} else if (type.equals("adjustExchange")) {
			text = getMessage(request, "common.msg.isAdjustExchange");
			forward = mapping.findForward("adjustExchange");
		} else if (type.equals("reCalculate")) {
			AccPeriodBean bean = (AccPeriodBean) BaseEnv.accPerios.get(this.getLoginBean(request).getSunCmpClassCode());
			String GoodsCode=request.getParameter("GoodsCode");
			String isCatalog=request.getParameter("isCatalog");
			String GoodsFullName=request.getParameter("GoodsFullName");
			request.setAttribute("periodYear", bean.getAccYear());
			request.setAttribute("period", bean.getAccPeriod());
			request.setAttribute("GoodsCode", GoodsCode);
			request.setAttribute("isCatalog", isCatalog);
			if(GoodsFullName!=null&&GoodsFullName.length()>0){
				GoodsFullName = new String(GoodsFullName.getBytes("iso-8859-1"), "UTF-8");
			}
			request.setAttribute("GoodsFullName", GoodsFullName);
			
			text = getMessage(request, "common.msg.isReCalculate");
			forward = mapping.findForward("reCalculate");
		} else if (type.equals("exeStockTotal")) {
			forward = mapping.findForward("exeStockTotal");
		} else if (type.equals("delBill")) {
			text = getMessage(request, "Accounting.delBill.sureDel");
			forward = mapping.findForward("delBill");
		} else if ("bakAccount".equals(type)) {
			forward = mapping.findForward("bakAccount");
		} else if ("settleProfitLoss".equals(type)) {
			forward = mapping.findForward("profitLoss");
		}

		request.setAttribute("msgtext", text);
		request.setAttribute("type", type);

		return forward;
	}

	/**
	 * 删除指定期间以前的数据(包括当前期间)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward delBill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = getForward(request, mapping, "message");

		String period = request.getParameter("period");
		String periodYear = request.getParameter("periodYear");
		String SERVER=request.getParameter("SERVER");
		String UID=request.getParameter("UID");
		String PWD=request.getParameter("PWD");
		String DataBase=request.getParameter("DataBase");
		LoginBean lg=this.getLoginBean(request);
		Hashtable hashTable = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Result rs = mgt.delBill(period, periodYear, sunCompany, this.getLoginBean(request).getId(), hashTable, this.getLocale(request).toString(),SERVER,UID,PWD,DataBase);

		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, lg.getId(),lg.getEmpFullName(),lg.getSunCmpClassCode(), "删除单据", "删除单据", "删除单据","删除单据");
			EchoMessage.success().setNotAutoBack().add(this.getMessage(request, "Accounting.delBill.delSuccess")).setRequest(request);
			forward = getForward(request, mapping, "message");
		} else if (rs.getRetCode() == ErrorCanst.RET_NOTSETTLEACC_LAST) {
			EchoMessage.error().add(getMessage(request, "common.msg.RET_NOTSETTLEACC_LAST")).setBackUrl("/SysAccAction.do?type=delBill&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(
					request);
		} else {
			EchoMessage.error().add(rs.getRetCode(), request).setBackUrl("/SysAccAction.do?type=delBill&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		}
		return forward;
	}

	protected ActionForward yearSettleAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		LoginBean login = this.getLoginBean(request);
		String newAcc = request.getParameter("newAcc") == null ? "" : request.getParameter("newAcc");
		String accName = request.getRealPath("");

		Result rs = mgt.isAbleYearSettleAcc(sunCompany);
		if (rs.getRetCode() == ErrorCanst.RET_NOTSETTLEACC_LAST) {
			// 最后一个期间未月结
			EchoMessage.error().add("最后一个财务期间未做月结").setBackUrl("/SysAccAction.do?type=settleAcc&winCurIndex=" + request.getAttribute("winCurIndex"))
					.setRequest(request);
			forward = getForward(request, mapping, "message");
			return forward;
		}
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String delDraft = request.getParameter("delDraft") == null ? "" : request.getParameter("delDraft");
		rs = mgt.yearSettleAcc(newAcc, accName, login, sunCompany, map, this.getLocale(request).toString(), delDraft);

		forward = getForward(request, mapping, "message");
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			//年结存后,如果下一年没有当前期间，系统开账
			int[] result = (int[]) rs.getRetVal();
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, login.getId(),login.getEmpFullName(),login.getSunCmpClassCode(), "年结存："+result[0], "年结存", "年结存","年结存");
			if (newAcc.equals("true")) {
				SysAccMgt mgt = new SysAccMgt();
				if (rs.getRetVal() != null && result[1] == 0) {
					rs = mgt.beginAcc(login.getId(), sunCompany, map, "false");
				}
				if (request.getAttribute("LAST_SETTLEPERIOD") != null) {
					request.removeAttribute("LAST_SETTLEPERIOD");
				}
				if (request.getSession().getAttribute("LAST_SETTLEPERIOD") != null) {
					request.getSession().removeAttribute("LAST_SETTLEPERIOD");
				}
				if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					request.setAttribute("AUTO_JUMP", "true");
					// 年结存成功
					EchoMessage.success().setNotAutoBack().add(result[0] + getMessage(request, "common.msg.yearSettleAccSuccess")).setBackUrl(
							"/SysAccAction.do?type=yearSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
				} else if (rs.getRetCode() == ErrorCanst.RET_BEGINACC_NOBEGINPERIOD) {

					EchoMessage.error().add(this.getMessage(request, "sysacc.begin.nobeginperiod")).setBackUrl("/SysAccAction.do?type=beginAcc&winCurIndex=" + request.getAttribute("winCurIndex"))
							.setRequest(request);
				} else {
					SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
		        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
						EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
					} else {
						EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/SysAccAction.do?type=beginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
					}
				}
			} else {
				if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					// 年结存成功
					EchoMessage.success().setNotAutoBack().add(getMessage(request, "common.msg.yearSettleAccSuccess")).setBackUrl(
							"/SysAccAction.do?type=yearSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
				} else {
					// 年结存失败
					EchoMessage.error().add(rs.retCode, request).setBackUrl("/SysAccAction.do?type=yearSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
					forward = getForward(request, mapping, "message");
					return forward;
				}
			}
		} else {
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/SysAccAction.do?type=yearSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
			}
		}

		return forward;
	}

	protected ActionForward adjustExchange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		LoginBean login = this.getLoginBean(request);

		String adjustExchange = this.getMessage(request, "SysAcc.lb.adjustExchange");
		String settle = this.getMessage(request, "SysAcc.lb.settle");
		Result rs = mgt.adjustExchange(login.getId(), sunCompany, adjustExchange, settle);

		forward = getForward(request, mapping, "message");
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, login.getId(),login.getEmpFullName(),login.getSunCmpClassCode(), "期末调汇", "期末调汇", "月结","期末调汇");
			// 期末调汇成功
			EchoMessage.success().setNotAutoBack().add(getMessage(request, "common.msg.adjustExchangeSuccess")).setBackUrl(
					"/SysAccAction.do?type=adjustExchange&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
		} else {
			EchoMessage.error().add(rs.getRetCode(), request).setBackUrl("/SysAccAction.do?type=adjustExchange&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		}
		return forward;
	}

	/**
	 * 系统开账
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward beginAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = null;

		String beginType = getParameter("autoBegin", request);
		if ("true".equals(beginType)) {
			return autoBeginAcc(mapping, form, request, response);
		}
		LoginBean login = this.getLoginBean(request);
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Result rs = mgt.beginAcc(login.getId(), sunCompany, map, "true");
		
		forward = getForward(request, mapping, "message");
		String url = "/SysAccAction.do?type=beginAcc&winCurIndex=" + request.getAttribute("winCurIndex");
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, login.getId(), login.getEmpFullName(), login.getSunCmpClassCode(), "开账", "开账", "开账","开账");
			rs = mgt.getCurrPeriod(sunCompany);// ////////
			request.getSession().setAttribute("NowPeriod", ((AccPeriodBean) rs.getRetVal()).getAccMonth());
			request.setAttribute("AUTO_JUMP", "true");
			EchoMessage.success().setNotAutoBack().add(getMessage(request, "common.msg.beginAccSuccess")).setBackUrl(
					url).setAlertRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			// 自定义sql语句定制返回结果
			String[] str = (String[]) rs.getRetVal();

			if (str != null) {
				String msg = new DynDBManager().getDefSQLMsg(getLocale(request).toString(), str[0]);
				if (str[1].length() > 0) {
					EchoMessage.info().add(msg).setGoUrl(str[1]).setAlertRequest(request);
				} else {
					EchoMessage.error().add(msg).setAlertRequest(request);
				}
			} else {
				EchoMessage.error().add(rs.retCode, request).setAlertRequest(request);
			}
		} else if (rs.getRetCode() == ErrorCanst.RET_BEGINACC_NOBEGINPERIOD) {
			EchoMessage.error().add("<a href='/AccPeriodIntercalateAction.do'>" + this.getMessage(request, "sysacc.begin.nobeginperiod") + "</a>").setBackUrl(
					url).setRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_BEGINACC_END) {
			String fromPage = request.getParameter("fromPage");
			if (fromPage != null && fromPage.contains("guide")) {
				EchoMessage.error().add(this.getMessage(request, "common.msg.RET_BEGINACC_END")).setBackUrl("/vm/guide/" + fromPage).setAlertRequest(request);
			} else {
				EchoMessage.error().add(this.getMessage(request, "common.msg.RET_BEGINACC_END")).setAlertRequest(request);
			}
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.RET_ACCNOTEQUAL") + "&nbsp;<a href='/IniAccQueryAction.do?src=menu'>" + getMessage(request, "sysAcc.lb.checkhereauto") + "</a>")
					.setBackUrl(url).setRequest(request);
		}
		return forward;
	}

	protected ActionForward reBeginAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = null;
		String accName = request.getRealPath("");
		if (accName.length() > 0) {

		}
		LoginBean lg=getLoginBean(request);
		String delIni = request.getParameter("delIni") == null ? "" : request.getParameter("delIni");
		String delBase = request.getParameter("delBase") == null ? "" : request.getParameter("delBase");
		String delBill = request.getParameter("delBill") == null ? "" : request.getParameter("delBill");
		String delDraft = request.getParameter("delDraft") == null ? "" : request.getParameter("delDraft");
		Result rs = mgt.reBeginAcc(delIni, delBase, delBill, delDraft, sunCompany, getLoginBean(request).getId(), accName);

		forward = getForward(request, mapping, "message");
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, lg.getId(),lg.getEmpFullName(),lg.getSunCmpClassCode(), "反开账", "反开账", "反开账","反开账");
			request.getSession().setAttribute("NowPeriod", "-1");
			// 反开账成功
			request.setAttribute("AUTO_JUMP", "true");
			EchoMessage.success().setNotAutoBack().add(getMessage(request, "common.msg.reBeginAccSuccess")).setBackUrl(
					"/SysAccAction.do?type=reBeginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_ASSET_NODEPRECIATE) {
			EchoMessage.error().add(getMessage(request, "SysAcc.msg.existDepreciate")).setBackUrl(
					"/UserFunctionQueryAction.do?tableName=tblFixedAssetDepreciate&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.ER_NO_DATA) {
			// 反开账失败
			EchoMessage.error().add(getMessage(request, "common.msg.ER_NO_DATA")).setBackUrl("/SysAccAction.do?type=reBeginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_SETTLE_END) {
			// 系统有已经月结的会计期间，需要反月结
			EchoMessage.error().add(getMessage(request, "common.msg.RET_SETTLE_END")).setBackUrl("/SysAccAction.do?type=reBeginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(
					request);

		} else if (rs.getRetCode() == ErrorCanst.RET_NOTBEGINACC) {
			// 系统未开账
			EchoMessage.error().add(getMessage(request, "common.msg.RET_NOTBEGINACC")).setBackUrl("/SysAccAction.do?type=reBeginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(
					request);
		} else {
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg().replaceAll("\"", "")).setBackUrl("/SysAccAction.do?type=reBeginAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
			}			
		}

		return forward;

	}
	
	protected ActionForward AccReSettleAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = null;
		LoginBean login=getLoginBean(request);
		Hashtable hashTable = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Result rs = mgt.AccReSettleAcc(sunCompany, getLoginBean(request).getId(), hashTable);

		forward = getForward(request, mapping, "message");
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, login.getId(),login.getEmpFullName(),login.getSunCmpClassCode(), "财务反月结:"+rs.retVal, "财务反月结", "反月结","财务反月结");
			// 反结账成功
			request.setAttribute("AUTO_JUMP", "true");
			EchoMessage.success().setNotAutoBack().add(getMessage(request, "common.msg.reSettleAccSuccess")).setBackUrl(
					"/SysAccAction.do?type=reSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_ASSET_NODEPRECIATE) {
			EchoMessage.error().add(getMessage(request, "SysAcc.msg.existDepreciate")).setBackUrl(
					"/UserFunctionQueryAction.do?tableName=tblFixedAssetDepreciate&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_NOTBEGINACC) {
			// 系统未开账
			EchoMessage.error().add(getMessage(request, "common.msg.RET_NOTBEGINACC")).setBackUrl("/SysAccAction.do?type=reSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(
					request);
		} else if (rs.getRetCode() == ErrorCanst.RET_NOTSETTLEPERIOD) {
			// 没有已经月结的会计期间
			EchoMessage.error().add(getMessage(request, "common.msg.RET_NOTSETTLEPERIOD")).setBackUrl("/SysAccAction.do?type=reSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex"))
					.setRequest(request);
		} else {
			// 反结账失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setBackUrl("/SysAccAction.do?type=reSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		}

		return forward;

	}
	
	protected ActionForward reSettleAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = getForward(request, mapping, "message");
		Hashtable hashTable = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(getLoginBean(request).getId());
		
		AccPeriodBean accPeriod = (AccPeriodBean) hashSession.get("AccPeriod");
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriodAcc");
		LoginBean login=getLoginBean(request);
		
		if(accPeriod.getAccPeriod()==accPeriodBean.getAccPeriod() && accPeriod.getAccYear()==accPeriodBean.getAccYear()){
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add("请先进行当前期间的财务反月结").setBackUrl("/SysAccAction.do?type=reSettleAcc").setRequest(request);
			return forward;
		}
		
		Result rs = mgt.reSettleAcc(sunCompany, getLoginBean(request).getId(), hashTable);

		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, login.getId(),login.getEmpFullName(),login.getSunCmpClassCode(), "业务反月结:"+rs.retVal, "业务反月结", "反月结","业务反月结");
			// 反结账成功
			request.setAttribute("AUTO_JUMP", "true");
			EchoMessage.success().setNotAutoBack().add(getMessage(request, "common.msg.reSettleAccSuccess")).setBackUrl(
					"/SysAccAction.do?type=reSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_ASSET_NODEPRECIATE) {
			EchoMessage.error().add(getMessage(request, "SysAcc.msg.existDepreciate")).setBackUrl(
					"/UserFunctionQueryAction.do?tableName=tblFixedAssetDepreciate&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_NOTBEGINACC) {
			// 系统未开账
			EchoMessage.error().add(getMessage(request, "common.msg.RET_NOTBEGINACC")).setBackUrl("/SysAccAction.do?type=reSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(
					request);
		} else if (rs.getRetCode() == ErrorCanst.RET_NOTSETTLEPERIOD) {
			// 没有已经月结的会计期间
			EchoMessage.error().add(getMessage(request, "common.msg.RET_NOTSETTLEPERIOD")).setBackUrl("/SysAccAction.do?type=reSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex"))
					.setRequest(request);
		} else {
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/SysAccAction.do?type=reSettleAcc&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
			}	
			// 反结账失败
		}

		return forward;

	}

	// 重算成本
	protected ActionForward reCalculate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = null;
		try {

			Hashtable hashTable = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
			int periodYear = Integer.parseInt(request.getParameter("periodYear"));
			int period = Integer.parseInt(request.getParameter("period"));
			String GoodsCode=request.getParameter("GoodsCode");
			String isCatalog=request.getParameter("isCatalog");
			String GoodsFullName=request.getParameter("GoodsFullName");
			GoodsFullName = new String(GoodsFullName.getBytes("iso-8859-1"), "UTF-8");
 
			Result rs = (new ReCalcucateMgt()).reCalcucateData(sunCompany, periodYear, period,lg.getId(),"reCalcucate",GoodsCode,isCatalog);
			forward = getForward(request, mapping, "message");
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				DynDBManager dyn=new DynDBManager();
				dyn.addLog(14, lg.getId(),lg.getEmpFullName(),lg.getSunCmpClassCode(), "重算成本:"+periodYear+"."+period, "重算成本", "重算成本","重算成本");
				// 重算成本成功
				EchoMessage.success().add(getMessage(request, "common.msg.reCalculateSuccess")).setBackUrl("/SysAccAction.do?type=reCalculate&GoodsCode="+GoodsCode+"&isCatalog="+isCatalog+"&GoodsFullName="+GoodsFullName+"&winCurIndex=" + request.getAttribute("winCurIndex"))
						.setAlertRequest(request);
			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, getLocale(request).toString(), "");
				// 报错
				EchoMessage.error().add(saveErrrorObj.getMsg()).setBackUrl("/SysAccAction.do?type=reCalculate&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			BaseEnv.log.error("SysAccAction.reCalculate", e);
		}
		return forward;
	}

	/**
	 * 结转损益
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward settleProfitLoss(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* 用户登陆信息*/
		LoginBean loginBean = this.getLoginBean(request);

		/* 当前会计期间*/
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriodAcc");

		Hashtable hashTable = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String profitLoss = this.getMessage(request, "SysAcc.msg.settleProfitLoss");
		String settle = this.getMessage(request, "muduleFlow.lb.tocost");

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		String accPass=request.getParameter("accPass")==null?"":request.getParameter("accPass");

		/* 结转损益*/
		Result rs = mgt.profitLoss(loginBean, accPeriodBean, sunCompany, hashTable, this.getLocale(request), profitLoss, settle, "profitloss", resources,accPass);

		ActionForward forward = getForward(request, mapping, "message");
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, loginBean.getId(),loginBean.getEmpFullName(),loginBean.getSunCmpClassCode(), "结转损益:"+accPeriodBean.getAccYear()+"."+accPeriodBean.getAccMonth(), "结转损益", "月结","结转损益");
			// 结转损益成功
			EchoMessage.success().add("结转损益成功").setBackUrl("/SysAccAction.do?type=settleAcc").setAlertRequest(request);
		}else if (rs.getRetCode() == ErrorCanst.RET_HAS_AUDITING) {
			BaseEnv.CLOSE_ACC = "";
			request.setAttribute("AccHasNotAuditing", "true");
			request.setAttribute("accPeriodBean", accPeriodBean); 
			String text = getMessage(request, "common.msg.isSettleAcc");
			rs = mgt.getSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("from", list.get(0));
				request.setAttribute("to", list.get(1));
			}
			rs = mgt.getAccSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("Accfrom", list.get(0));
				request.setAttribute("Accto", list.get(1));
			}
			forward = mapping.findForward("settleAcc");//EchoMessage.error().add(rs.retVal.toString()).setBackUrl("/VoucherManageAction.do").setRequest(request);
		} else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			BaseEnv.CLOSE_ACC = "";
			// 自定义sql语句定制返回结果
			String[] str = (String[]) rs.getRetVal();
			if (str != null) {
				String msg = new DynDBManager().getDefSQLMsg(getLocale(request).toString(), str[0]);
				if (str[1].length() > 0) {
					EchoMessage.info().add(msg).setGoUrl(str[1]).setAlertRequest(request);
				} else {
					EchoMessage.error().add(msg).setAlertRequest(request);
				}
			} else {
				EchoMessage.error().add(rs.retCode, request).setAlertRequest(request);
			}
		} else {
			BaseEnv.CLOSE_ACC = "";
			List rows = new ArrayList();
			
			boolean isEq = false;
			HashMap map = null;
			try {
				map = (HashMap) rs.getRetVal();
				if (map == null)
					isEq = true;
			} catch (Exception e) {
				isEq = true;
			}
			if (!isEq) {
				Iterator it = map.keySet().iterator();
				while (it.hasNext()) {
					StockDetBean stockDet = (StockDetBean) map.get(it.next());
					Object[] strs = new Object[9];
					strs[0] = stockDet.getId();
					strs[1] = stockDet.getBillID();
					Object modelName = mgt.getModelName(stockDet.getBillType()).getRetVal();
					modelName = modelName == null ? "" : modelName;
					strs[2] = modelName;
					strs[3] = stockDet.getBillDate();
					strs[4] = mgt.getStockFullName(stockDet.getStockCode()).getRetVal().toString();
					strs[5] = mgt.getGoodsName(stockDet.getGoodsCode()).getRetVal().toString();
					Object price = stockDet.getLastPrice();
					price = price == null ? "0" : price;
					strs[6] = GlobalsTool.formatNumber(price, false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), null, null, true);
					Object totalQty = stockDet.getTotalQty();
					totalQty = totalQty == null ? "0" : totalQty;
					strs[7] = (GlobalsTool.formatNumber(totalQty, false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), null, null, true));
					Object totalAmt = stockDet.getTotalQty();
					totalAmt = totalAmt == null ? "0" : totalAmt;
					strs[8] = GlobalsTool.formatNumber(totalAmt, false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), null, null, true);
					rows.add(strs);
				}
				request.setAttribute("result", rows);
				return getForward(request, mapping, "profitLoss");
			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.error")).setBackUrl("/AccountManageAction.do").setRequest(
						request);
			}
		}
		return forward;
	}

	/**
	 * 财务月结
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward AccSettleAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = getForward(request, mapping, "message");
		/* 用户登陆信息*/
		LoginBean loginBean = this.getLoginBean(request);

		/* 当前会计期间*/
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		
		AccPeriodBean accPeriod = (AccPeriodBean) hashSession.get("AccPeriod");
		
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriodAcc");

		if(accPeriod.getAccPeriod()==accPeriodBean.getAccPeriod()){
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add("请先进行当前期间的业务月结").setBackUrl("/SysAccAction.do?type=settleAcc").setRequest(request);
			return forward;
		}

		Hashtable hashTable = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String profitLoss = this.getMessage(request, "SysAcc.msg.settleProfitLoss");
		String settle = this.getMessage(request, "SysAcc.lb.settle");

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		this.sureSubmit = request.getParameter("sureSubmit") == null ? false : true;

		/* 财务月结*/
		Result rs = mgt.AccSettleAcc(loginBean, accPeriodBean, sunCompany, hashTable, this.getLocale(request), profitLoss, settle, resources);
		

		String url = "/SysAccAction.do?type=settleAcc";
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, loginBean.getId(),loginBean.getEmpFullName(),loginBean.getSunCmpClassCode(), "财务月结:"+accPeriodBean.getAccYear()+"."+accPeriodBean.getAccMonth(), "财务月结", "月结","财务月结");
			this.sureSubmit = false;
			request.setAttribute("AUTO_JUMP", "true");
			BaseEnv.CLOSE_ACC = "";
			// 月结成功
			EchoMessage.success().setNotAutoBack().add(getMessage(request, "common.msg.settleAccSuccess")).setBackUrl(
					url).setAlertRequest(request);
		} else if (rs.retCode == ErrorCanst.RET_NOTPROFITLOSS_ERROR) {
			//没有进行结转损益操作
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add(errorMessage).setBackUrl(url).setRequest(request);
		} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
			BaseEnv.CLOSE_ACC = ""; 
			request.setAttribute("AccHasNotAuditing", "true");
			request.setAttribute("accPeriodBean", accPeriodBean); 
			String text = getMessage(request, "common.msg.isSettleAcc");
			rs = mgt.getSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("from", list.get(0));
				request.setAttribute("to", list.get(1));
			}
			rs = mgt.getAccSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("Accfrom", list.get(0));
				request.setAttribute("Accto", list.get(1));
			}
			forward = mapping.findForward("settleAcc");
		} 
		return forward;
	}
	
	/**
	 * 业务月结
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward billSettleAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = null;
		/* 用户登陆信息*/
		LoginBean loginBean = this.getLoginBean(request);

		/* 当前会计期间*/
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");

		Hashtable hashTable = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String profitLoss = this.getMessage(request, "SysAcc.msg.settleProfitLoss");
		String settle = this.getMessage(request, "SysAcc.lb.settle");

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		this.sureSubmit = request.getParameter("sureSubmit") == null ? false : true;
		String zeroPriceIn=request.getParameter("zeroPriceIn");
		/* 业务月结*/
		Result rs = mgt.billSettleAcc(loginBean, accPeriodBean, sunCompany, hashTable, this.getLocale(request), profitLoss, settle, resources,zeroPriceIn);
		forward = getForward(request, mapping, "message");

		String url = "/SysAccAction.do?type=settleAcc";
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			OnlineUserInfo.setLockOper("BillSettleAcc");
			request.setAttribute("SettleStatus", "Settleing");
			rs = mgt.getSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("from", list.get(0));
				request.setAttribute("to", list.get(1));
			}
			rs = mgt.getAccSettlePeriodFromTo();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs.getRetVal();
				request.setAttribute("Accfrom", list.get(0));
				request.setAttribute("Accto", list.get(1));
			}
			forward = mapping.findForward("settleAcc");
			
		}else if (rs.retCode == ErrorCanst.RET_INPRICE_IS_ZERO) {
			BaseEnv.CLOSE_ACC = "";
			request.setAttribute("returnBills", rs.retVal);
			Result rs2 = mgt.getSettlePeriodFromTo();
			if (rs2.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs2.getRetVal();
				request.setAttribute("from", list.get(0));
				request.setAttribute("to", list.get(1));
			}
			rs2 = mgt.getAccSettlePeriodFromTo();
			if (rs2.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (List) rs2.getRetVal();
				request.setAttribute("Accfrom", list.get(0));
				request.setAttribute("Accto", list.get(1));
			}
			return forward = mapping.findForward("settleAcc");
		}else if (rs.getRetCode() == ErrorCanst.RET_ASSET_NODEPRECIATE) {
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add(getMessage(request, "SysAcc.msg.existNoDepreciate")).setBackUrl(
					"/UserFunctionQueryAction.do?tableName=tblFixedAssetDepreciate&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
			return forward;
		} else if (rs.getRetCode() == ErrorCanst.RET_ACCMAIN_NOTAPPROVE) {
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add(getMessage(request, "common.msg.RET_ACCMAIN_NOTAPPROVE")).setBackUrl(url)
					.setRequest(request);
			return forward;
		} else if (rs.getRetCode() == ErrorCanst.RET_BILL_NOTCREATEACC) {
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add("本期内有如下单据未产生凭证，不允许月结  "+rs.retVal).setBackUrl(url)
					.setRequest(request);
			return forward;
		} else if (rs.getRetCode() == ErrorCanst.RET_BILL_NOTAPPROVE) {
			request.getSession().setAttribute("notAuditBill", rs.getRetVal());
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add(getMessage(request, "common.msg.RET_BILL_NOTAPPROVE")).setBackUrl(
					url+"&notAuditBill=true").setRequest(request);
			return forward;
		} else if (rs.getRetCode() == ErrorCanst.SETTLE_LASTQTYNEGATIVE_ERROR) {
			request.getSession().setAttribute("lastQtyNegGoods", rs.getRetVal());
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add(getMessage(request, "settleAcc.NegativeLastQty.error")).setBackUrl(url)
					.setRequest(request);
			return forward;
		} else if (rs.getRetCode() == ErrorCanst.RET_BILL_EXISTDRAFT) {
			request.getSession().setAttribute("draftBill", rs.getRetVal());
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add(getMessage(request, "common.msg.RET_BILL_EXISTDRAFT")).setBackUrl(
					url+"&draftBill=true").setRequest(request);
			return forward;
		} else if (rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR) {
			BaseEnv.CLOSE_ACC = "";
			if (rs.retVal != null && rs.retVal.toString().length() > 0) {
				EchoMessage.error().add(rs.retVal.toString()).setBackUrl(url).setRequest(request);
			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.RET_BILL_Negative")).setBackUrl(url)
						.setRequest(request);
			}
			return forward;
		} else if (rs.getRetCode() == ErrorCanst.RET_NOTSETTLE_LASTNOTYEAR) {
			BaseEnv.CLOSE_ACC = "";
			EchoMessage.error().add(getMessage(request, "common.msg.RET_NOTSETTLE_LASTNOTYEAR")).setBackUrl(url)
					.setRequest(request);
			return forward;
		} else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			BaseEnv.CLOSE_ACC = "";
			// 自定义sql语句定制返回结果
			String[] str = (String[]) rs.getRetVal();
			if (str != null) {
				String msg = new DynDBManager().getDefSQLMsg(getLocale(request).toString(), str[0]);
				if (str[1].length() > 0) {
					EchoMessage.info().add(msg).setGoUrl(str[1]).setAlertRequest(request);
				} else {
					EchoMessage.error().add(msg).setAlertRequest(request);
				}
			} else {
				EchoMessage.error().add(rs.retCode, request).setAlertRequest(request);
			}
			return forward;
		}  else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SENTENCE_ERROR) {
			BaseEnv.CLOSE_ACC = "";
			// 自定义的语法错误
			Object str=rs.getRetVal();
			if(str!=null){
				EchoMessage.error().add(str.toString()).setAlertRequest(request);
			}
			return forward;
		} else {
			BaseEnv.CLOSE_ACC = "";
			// 单据成本单价异常
			List rows = new ArrayList();
			
			boolean isEq = false;
			HashMap map = null;
			try {
				map = (HashMap) rs.getRetVal();
				if (map == null)
					isEq = true;
			} catch (Exception e) {
				isEq = true;
			}
			if (!isEq) {
				Iterator it = map.keySet().iterator();
				while (it.hasNext()) {
					StockDetBean stockDet = (StockDetBean) map.get(it.next());
					Object[] strs = new Object[9];
					strs[0] = stockDet.getId();
					strs[1] = stockDet.getBillID();
					Object modelName = mgt.getModelName(stockDet.getBillType()).getRetVal();
					modelName = modelName == null ? "" : modelName;
					strs[2] = modelName;
					strs[3] = stockDet.getBillDate();
					strs[4] = mgt.getStockFullName(stockDet.getStockCode()).getRetVal().toString();
					strs[5] = mgt.getGoodsName(stockDet.getGoodsCode()).getRetVal().toString();
					Object price = stockDet.getLastPrice();
					price = price == null ? "0" : price;
					strs[6] = GlobalsTool.formatNumber(price, false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), null, null, true);
					Object totalQty = stockDet.getTotalQty();
					totalQty = totalQty == null ? "0" : totalQty;
					strs[7] = (GlobalsTool.formatNumber(totalQty, false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), null, null, true));
					Object totalAmt = stockDet.getTotalQty();
					totalAmt = totalAmt == null ? "0" : totalAmt;
					strs[8] = GlobalsTool.formatNumber(totalAmt, false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), null, null, true);
					rows.add(strs);
				}
				request.setAttribute("result", rows);
				return getForward(request, mapping, "closeAcc");
			} else {
				EchoMessage.error().add(getMessage(request, "common.msg.error")).setBackUrl(url)
						.setRequest(request);
			}
		}
		return forward;
	}
	
	/**
	 * 设置开账期间
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward setAccPeriod(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		//开账日期
		LoginBean lg = this.getLoginBean(request);
		String accPeriodDate = request.getParameter("accPeriodDate");
		//设置开账期间
		Result result = mgt.accPeriodByDate(accPeriodDate, lg.getId(),lg.getSunCmpClassCode());
		
		request.setAttribute("dealAsyn", "true");
		request.setAttribute("noAlert", "true");
		request.setAttribute("checkFlag", "define");
		if(result.getRetCode() ==ErrorCanst.DEFAULT_SUCCESS){			
			DynDBManager dyn=new DynDBManager();
			dyn.addLog(14, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "开账时间："+accPeriodDate, "开账", "开账","修改开账时间");
			request.setAttribute("checkBackUrl", "parent.dealCheck('OK;"+getMessage(request, "accperiod.msg.addSuccess")+"')");
			EchoMessage.success().add(getMessage(request, "accperiod.msg.addSuccess")).setAlertRequest(request);
		}else if(result.getRetCode()==ErrorCanst.DATA_ALREADY_USED){
			request.setAttribute("checkBackUrl", "parent.dealCheck('ERROR;"+getMessage(request, "AccPeriod.set.msg")+"')");
			EchoMessage.error().add(getMessage(request, "AccPeriod.set.msg")).setAlertRequest(request);
		}else{
			request.setAttribute("checkBackUrl", "parent.dealCheck('ERROR;"+getMessage(request, "accperiod.msg.addFailture")+"')");
			EchoMessage.error().add(getMessage(request, "accperiod.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}

}
