package com.menyi.aio.web.sysAcc;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.menyi.aio.bean.AccMainSettingBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 开账，结账的接口类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * h
 * <p>
 * Company: 周新宇
 * </p>
 * 
 * @author 张雪
 * @version 1.0
 */
public class SettleCostMgt extends DBManager {

	private Hashtable tableMap;
	private String locale;
	private String profitLoss;
	private String settle;
	public SettleCostMgt(Hashtable tableMap,String locale,String profitLoss,String settle){
		this.tableMap=tableMap;
		this.locale=locale;
		this.profitLoss=profitLoss;
		this.settle=settle;
	}
	/**
	 * 取会计科目的借贷方向
	 * 
	 * @param conn
	 *            Connection
	 * @param accCode
	 *            String
	 * @param sunCompany
	 *            String
	 * @return int
	 * @throws SQLException
	 */
	public int getJdFlag(Connection conn, String accCode, String sunCompany)
			throws SQLException {
		int jdFlag = 0;
		String sql = "select JdFlag from tblAccTypeInfo where scompanyid='"
				+ sunCompany + "' and accNumber='" + accCode + "'";
		Statement cs = conn.createStatement();
		ResultSet rs = cs.executeQuery(sql);
		if (rs.next()) {
			jdFlag = rs.getInt("JdFlag");
		}
		return jdFlag;
	}

	
	/**
	 * 插入凭证明细
	 * 
	 * @param st
	 *            Statement
	 * @param accYear
	 *            int
	 * @param accMonth
	 *            int
	 * @param subCode
	 *            String
	 * @param debit
	 *            double
	 * @param debitCurr
	 *            double
	 * @param credit
	 *            double
	 * @param creditCurr
	 *            double
	 * @param loginId
	 *            String
	 * @param mainId
	 *            String
	 * @param type
	 *            String
	 * @param remark
	 *            String
	 * @param sunCompany
	 *            String
	 * @throws Exception
	 */
	HashMap map = new HashMap();

	int count = 0;

	public void insertAccDetail(Statement st, int accYear, int accMonth,
			String subCode, double debit, double debitCurr, double credit,
			double creditCurr, String loginId, String mainId, String type,
			String remark, String sunCompany,String departmentCode) throws Exception {
		if(type.equals("adjustExchange")||type.equals("ProfitLossCarryForward")){
			remark=settle+"."+profitLoss;
		}else{
			if(tableMap.get(type)!=null){
				DBTableInfoBean bean=(DBTableInfoBean)tableMap.get(type);
				if(remark!=null&&remark.length()>0){
					remark=settle+"."+bean.getDisplay().get(locale)+"-"+remark;
				}else{
					remark=settle+"."+bean.getDisplay().get(locale);
				}
				
			}
		}

		int digits =Integer.parseInt(BaseEnv.systemSet.get("DigitsAmount").getSetting());

		debit = Math.round(debit * Math.pow(10, digits)) / Math.pow(10, digits);
		debitCurr = Math.round(debitCurr * Math.pow(10, digits))
				/ Math.pow(10, digits);
		credit = Math.round(credit * Math.pow(10, digits))
				/ Math.pow(10, digits);
		creditCurr = Math.round(creditCurr * Math.pow(10, digits))
				/ Math.pow(10, digits);
		String BillDate = accYear + "-";
		if (accMonth < 10)
			BillDate += "0";
		BillDate += accMonth + "-" + GlobalsTool.getMaxDay(accYear, accMonth);
		String longDate = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		String id = IDGenerater.getId();

		String sql = "insert into  tblAccDetail (id,RefBillType,AccCode,DebitAmount,DebitCurrencyAmount,LendAmount,LendCurrencyAmount,PeriodYear,PeriodMonth,"
				+ "AccDate,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,f_ref,RefBillID,RecordComment,SCompanyID,EmployeeID,DepartmentCode) "
				+ "values('"
				+ id
				+ "','"
				+ type
				+ "','"
				+ subCode
				+ "',"
				+ debit
				+ ","
				+ debitCurr
				+ ","
				+ credit
				+ ","
				+ creditCurr
				+ ","
				+ accYear
				+ ","
				+ accMonth
				+ ",'"
				+ BillDate
				+ "','"
				+ loginId
				+ "','"
				+ loginId
				+ "','"
				+ longDate
				+ "','"
				+ longDate
				+ "','0','"
				+ mainId
				+ "','settleAcc','"
				+ remark
				+ "','" + sunCompany + "','"+loginId+"','"+departmentCode+"')";
		st.execute(sql);
		sql = "delete from tblAccDetail where DebitAmount = 0 and LendAmount = 0";
		st.execute(sql);
	}

	/**
	 * 插入凭证主表数据
	 * 
	 * @param cs
	 *            Statement
	 * @param id
	 *            String
	 * @param loginId
	 *            String
	 * @param accPeriod
	 *            int
	 * @param accYear
	 *            int
	 * @param accMonth
	 *            int
	 * @param departCode
	 *            String
	 * @param type
	 *            String
	 * @throws Exception
	 */
	public void insertAccMain(int IsAuditing,Statement cs, String id, String loginId,
			int accPeriod, int accYear, int accMonth, String departCode,
			String type, String sunCompany) throws Exception {
		
		String longDate = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		String BillDate = accYear + "-";
		if (accPeriod < 10)
			BillDate += "0";
		BillDate += accPeriod + "-" + GlobalsTool.getMaxDay(accYear, accPeriod);

		List<KeyPair> list=GlobalsTool.getEnumerationItems("CredTypeID ","zh_CN");
		String CredTypeID = "记";
		String sql = "select isnull(max(OrderNo),0) maxOrderNo,isnull(max(CredNo),0) maxCredNo from tblAccMain where SCompanyID='"
				+ sunCompany + "' and CredYear="+accYear+" and Period="+accPeriod;
		ResultSet rss = cs.executeQuery(sql);
		int OrderNo = 1;
		int CredNo = 1;
		if (rss.next()) {
			OrderNo = OrderNo + rss.getInt(1);
			CredNo = CredNo + rss.getInt(2);
		}
		
		/* 标准财务和非标准财务处理过账和审核 */
		String workFlowNodeName = "notApprove";
		String workFlowNode = "0";
		String isAuditing = "start";
		String postingUser = "";
		//凭证不启用审核流时，产生的凭证直接过账
		if(IsAuditing!=1){
			workFlowNodeName = "finish";
			workFlowNode = "-1";
			isAuditing = "finish";
			postingUser = loginId;
		}
		sql = "insert into tblAccMain (id,CredNo,BillDate,EmployeeID,RefBillTyp"
				+ "e,RefBillNo,RefBillID,"
				+ "Period,CredYear,CredMonth,createBy,lastUpdateBy,createTime,lastUpdateTime,DepartmentCode,SCompanyID,CredTypeID,OrderNo,AutoBillMarker,workFlowNodeName,workFlowNode,checkPersons,isAuditing,postingUser,isReview) "
				+ " values ('" + id + "'," +CredNo+",'" + BillDate + "','" + loginId
				+ "','" + type + "','settleAcc','settleAcc'," + accPeriod + ","
				+ accYear + "," + accMonth + ",'" + loginId + "','" + loginId
				+ "','" + longDate + "','" + longDate + "','" + departCode
				+ "','" + sunCompany + "','" + CredTypeID + "','" + OrderNo
				+ "','1','"+workFlowNodeName+"','"+workFlowNode+"','','"+isAuditing+"','"+postingUser+"',1)";
		cs.execute(sql);
		sql = "delete from tblAccMain where id not in (select f_ref from tblAccDetail)";
		cs.execute(sql);
		}

	/**
	 * 修改科目余额（数据为本次发生额）
	 * 
	 * @param conn
	 *            Connection
	 * @param accNumber
	 *            String
	 * @param period
	 *            int
	 * @param periodYear
	 *            int
	 * @param periodMonth
	 *            int
	 * @param sumDebit
	 *            double 此科目的本次发生额
	 * @param sumCredit
	 *            double
	 * @param sumDebitCurr
	 *            double
	 * @param sumCreditCurr
	 *            double
	 * @param jdFlag
	 *            int
	 * @throws Exception
	 */
	public void updateAccBalanceCurr(Connection conn, CallableStatement cbs,
			Statement cs, String accNumber, int period, int periodYear,
			double sumDebit, double sumCredit, double sumDebitCurr,
			double sumCreditCurr, int jdFlag, String sunCompany,String departmentCode)
			throws Exception {
		int digits =Integer.parseInt(BaseEnv.systemSet.get("DigitsAmount").getSetting());
		if(BaseEnv.version!=11){//工贸版增加了部门的参数，由于此存储过程在做单中用到，所以脚本不能同步布匹版
			CallableStatement createPeriod = conn.prepareCall("{call proc_createAccBalPeriod(?,?,?,?,?,?,?)}");
			createPeriod.setInt(1, periodYear);
			createPeriod.setInt(2, period);
			createPeriod.setInt(3, period);
			createPeriod.setString(4, accNumber);
			createPeriod.setString(5, sunCompany);
			createPeriod.setString(6, departmentCode);
			createPeriod.setString(7, "");
			createPeriod.execute();
		}else{
			CallableStatement createPeriod = conn.prepareCall("{call proc_createAccBalPeriod(?,?,?,?,?)}");
			createPeriod.setInt(1, periodYear);
			createPeriod.setInt(2, period);
			createPeriod.setInt(3, period);
			createPeriod.setString(4, accNumber);
			createPeriod.setString(5, sunCompany);
			createPeriod.execute();
		}
		
		
		ResultSet rss;
		String sql = "";
		// 修改科目余额表中此损益类的科目余额
		if (jdFlag == 1) {
			sql = "update tblAccBalance set CurrYIniDebitSum=round(CurrYIniDebitSum+("
					+ sumDebitCurr
					+ "),"+digits+"), CurrYIniCreditSum=round(CurrYIniCreditSum+("
					+ sumCreditCurr
					+ "),"+digits+"), CurrYIniBala=round(CurrYIniBala+("
					+ sumDebitCurr
					+ ")-("
					+ sumCreditCurr
					+ "),"+digits+"),"
					+ " CurrYIniDebitSumBase=round(CurrYIniDebitSumBase+("
					+ sumDebit
					+ "),"+digits+"), CurrYIniCreditSumBase=round(CurrYIniCreditSumBase+("
					+ +sumCredit
					+ "),"+digits+") ,CurrYIniBalaBase=round(CurrYIniBalaBase+("
					+ sumDebit
					+ ")-("
					+ sumCredit
					+ "),"+digits+"), PeriodDebitSumBase=round(PeriodDebitSumBase+("
					+ sumDebit
					+ "),"+digits+"), PeriodCreditSumBase=round(PeriodCreditSumBase+("
					+ sumCredit
					+ "),"+digits+"), PeriodDCBalaBase=round((PeriodDebitSumBase+("
					+ sumDebit
					+ ")-(PeriodCreditSumBase+"
					+ sumCredit
					+ ")),"+digits+"), PeriodBalaBase=round(PeriodBalaBase+("
					+ sumDebit
					+ ")-("
					+ sumCredit
					+ "),"+digits+"),PeriodDebitSum=round(PeriodDebitSum+("
					+ sumDebitCurr
					+ "),"+digits+"), PeriodCreditSum=round(PeriodCreditSum+("
					+ sumCreditCurr
					+ "),"+digits+"), PeriodDCBala=round((PeriodDebitSum+("
					+ sumDebitCurr
					+ "))-(PeriodCreditSum+("
					+ sumCreditCurr
					+ ")),"+digits+"), PeriodBala=round(PeriodBala+("
					+ sumDebitCurr
					+ ")-("
					+ sumCreditCurr
					+ "),"+digits+")  where SubCode='"
					+ accNumber
					+ "' and Period="
					+ period
					+ " and Nyear="
					+ periodYear
					+ " and SCompanyID='" + sunCompany + "'";
		} else {
			sql = "update tblAccBalance set CurrYIniDebitSum=round(CurrYIniDebitSum+("
					+ sumDebitCurr
					+ "),"+digits+"), CurrYIniCreditSum=round(CurrYIniCreditSum+("
					+ sumCreditCurr
					+ "),"+digits+"), CurrYIniBala=round(CurrYIniBala+("
					+ sumCreditCurr
					+ ")-("
					+ sumDebitCurr
					+ "),"+digits+"),"
					+ " CurrYIniDebitSumBase=round(CurrYIniDebitSumBase+("
					+ sumDebit
					+ "),"+digits+"), CurrYIniCreditSumBase=round(CurrYIniCreditSumBase+("
					+ +sumCredit
					+ "),"+digits+") ,CurrYIniBalaBase=round(CurrYIniBalaBase+("
					+ sumCredit
					+ ")-("
					+ sumDebit
					+ "),"+digits+"),PeriodDebitSumBase=round(PeriodDebitSumBase+("
					+ sumDebit
					+ "),"+digits+"), PeriodCreditSumBase=round(PeriodCreditSumBase+("
					+ sumCredit
					+ "),"+digits+"), PeriodDCBalaBase=round((PeriodCreditSumBase+("
					+ sumCredit
					+ "))-(PeriodDebitSumBase+("
					+ sumDebit
					+ ")),"+digits+"), PeriodBalaBase=round(PeriodBalaBase+("
					+ sumCredit
					+ ")-("
					+ sumDebit
					+ "),"+digits+"),PeriodDebitSum=round(PeriodDebitSum+("
					+ sumDebitCurr
					+ "),"+digits+"), PeriodCreditSum=round(PeriodCreditSum+("
					+ sumCreditCurr
					+ "),"+digits+"), PeriodDCBala=round((PeriodCreditSum+("
					+ sumCreditCurr
					+ "))-(PeriodDebitSum+("
					+ sumDebitCurr
					+ ")),"+digits+"), PeriodBala=round(PeriodBala+("
					+ sumCreditCurr
					+ ")-("
					+ sumDebitCurr
					+ "),"+digits+")  where SubCode='"
					+ accNumber
					+ "' and Period="
					+ period
					+ " and Nyear="
					+ periodYear
					+ " and SCompanyID='" + sunCompany + "'";

		}
		cs.execute(sql);
		String classCode = "";
		// 存储过程参数1：修改的表名，参数2：分类的表(没有为null)，参数3：前面两个表关联的条件 (没有为null)，参数4：修改依据的条件
		// (没有为null)，参数5：要修改的字段，参数6：要修改的分类代码
		rss = cs
				.executeQuery("select classCode from tblAccTypeInfo where accNumber='"
						+ accNumber + "' and SCompanyID='" + sunCompany + "'");
		if (rss.next()) {
			classCode = rss.getString(1);
		}
	}
	public Result updateParentAccBalance(Connection conn,String accNumber, int period, int periodYear)
			throws Exception {
		CallableStatement createPeriod = conn.prepareCall("{call proc_updateParentBalance(?,?,?,?,?)}");
		createPeriod.setString(1, accNumber);
		createPeriod.setInt(2, periodYear);
		createPeriod.setInt(3, period);
		createPeriod.registerOutParameter(4,
				java.sql.Types.INTEGER);
		createPeriod.registerOutParameter(5,
				java.sql.Types.VARCHAR);
		createPeriod.execute();
		int retCode = createPeriod.getInt(4);
		String retVal = createPeriod.getString(5);
		Result rs =  new Result();
		rs.retCode = retCode;
		rs.retVal = retVal;
		return rs;
	}
	
	/**
	 * 根据部门，会计科目更新科目余额数据.启用部门核算时，一级部门都有自己的科目余额数据
	 * @param conn
	 * @param deptCode
	 * @param accNumber
	 * @param period
	 * @param periodYear
	 * @param sumDebit
	 * @param sumCredit
	 * @param sumDebitCurr
	 * @param sumCreditCurr
	 * @param jdFlag
	 * @param sunCompany
	 * @throws Exception
	 */
	public void updateAccBalance(Connection conn,String deptCode, String accNumber, int period, int periodYear,
			double sumDebit, double sumCredit, double sumDebitCurr,
			double sumCreditCurr, int jdFlag, String sunCompany)
			throws Exception {
		int digits =Integer.parseInt(BaseEnv.systemSet.get("DigitsAmount").getSetting());

		//如果不存在当前部门的当前科目余额 ，则用下面的存储过程创建科目余额数据
		if(BaseEnv.version!=11){//工贸版增加了部门的参数，由于此存储过程在做单中用到，所以脚本不能同步布匹版
			CallableStatement createPeriod = conn.prepareCall("{call proc_createAccBalPeriod(?,?,?,?,?,?,?)}");
			createPeriod.setInt(1, periodYear);
			createPeriod.setInt(2, period);
			createPeriod.setInt(3, period);
			createPeriod.setString(4, accNumber);
			createPeriod.setString(5, sunCompany);
			createPeriod.setString(6, deptCode);
			createPeriod.setString(7, "");
			createPeriod.execute();
		}else{
			CallableStatement createPeriod = conn.prepareCall("{call proc_createAccBalPeriod(?,?,?,?,?)}");
			createPeriod.setInt(1, periodYear);
			createPeriod.setInt(2, period);
			createPeriod.setInt(3, period);
			createPeriod.setString(4, accNumber);
			createPeriod.setString(5, sunCompany);
			createPeriod.execute();
		}
		//根据传递过来的值更新科目余额表的借，贷等字段
		String sql = "";		
		if (jdFlag == 1) {
			sql = "update tblAccBalance set CurrYIniDebitSum=round(CurrYIniDebitSum+("
					+ sumDebitCurr
					+ "),"+digits+"), CurrYIniCreditSum=round(CurrYIniCreditSum+("
					+ sumCreditCurr
					+ "),"+digits+"), CurrYIniBala=round(CurrYIniBala+("
					+ sumDebitCurr
					+ ")-("
					+ sumCreditCurr
					+ "),"+digits+"),"
					+ " CurrYIniDebitSumBase=round(CurrYIniDebitSumBase+("
					+ sumDebit
					+ "),"+digits+"), CurrYIniCreditSumBase=round(CurrYIniCreditSumBase+("
					+ +sumCredit
					+ "),"+digits+") ,CurrYIniBalaBase=round(CurrYIniBalaBase+("
					+ sumDebit
					+ ")-("
					+ sumCredit
					+ "),"+digits+"), PeriodDebitSumBase=round(PeriodDebitSumBase+("
					+ sumDebit
					+ "),"+digits+"), PeriodCreditSumBase=round(PeriodCreditSumBase+("
					+ sumCredit
					+ "),"+digits+"), PeriodDCBalaBase=round((PeriodDebitSumBase+("
					+ sumDebit
					+ ")-(PeriodCreditSumBase+"
					+ sumCredit
					+ ")),"+digits+"), PeriodBalaBase=round(PeriodBalaBase+("
					+ sumDebit
					+ ")-("
					+ sumCredit
					+ "),"+digits+"),PeriodDebitSum=round(PeriodDebitSum+("
					+ sumDebitCurr
					+ "),"+digits+"), PeriodCreditSum=round(PeriodCreditSum+("
					+ sumCreditCurr
					+ "),"+digits+"), PeriodDCBala=round((PeriodDebitSum+("
					+ sumDebitCurr
					+ "))-(PeriodCreditSum+("
					+ sumCreditCurr
					+ ")),"+digits+"), PeriodBala=round(PeriodBala+("
					+ sumDebitCurr
					+ ")-("
					+ sumCreditCurr
					+ "),"+digits+")  where SubCode='"
					+ accNumber
					+ "' and Period="
					+ period
					+ " and Nyear="
					+ periodYear
					+ " and SCompanyID='" + sunCompany + "' and isnull(DepartmentCode,'')='"+deptCode+"'";
		} else {
			sql = "update tblAccBalance set CurrYIniDebitSum=round(CurrYIniDebitSum+("
					+ sumDebitCurr
					+ "),"+digits+"), CurrYIniCreditSum=round(CurrYIniCreditSum+("
					+ sumCreditCurr
					+ "),"+digits+"), CurrYIniBala=round(CurrYIniBala+("
					+ sumCreditCurr
					+ ")-("
					+ sumDebitCurr
					+ "),"+digits+"),"
					+ " CurrYIniDebitSumBase=round(CurrYIniDebitSumBase+("
					+ sumDebit
					+ "),"+digits+"), CurrYIniCreditSumBase=round(CurrYIniCreditSumBase+("
					+ +sumCredit
					+ "),"+digits+") ,CurrYIniBalaBase=round(CurrYIniBalaBase+("
					+ sumCredit
					+ ")-("
					+ sumDebit
					+ "),"+digits+"),PeriodDebitSumBase=round(PeriodDebitSumBase+("
					+ sumDebit
					+ "),"+digits+"), PeriodCreditSumBase=round(PeriodCreditSumBase+("
					+ sumCredit
					+ "),"+digits+"), PeriodDCBalaBase=round((PeriodCreditSumBase+("
					+ sumCredit
					+ "))-(PeriodDebitSumBase+("
					+ sumDebit
					+ ")),"+digits+"), PeriodBalaBase=round(PeriodBalaBase+("
					+ sumCredit
					+ ")-("
					+ sumDebit
					+ "),"+digits+"),PeriodDebitSum=round(PeriodDebitSum+("
					+ sumDebitCurr
					+ "),"+digits+"), PeriodCreditSum=round(PeriodCreditSum+("
					+ sumCreditCurr
					+ "),"+digits+"), PeriodDCBala=round((PeriodCreditSum+("
					+ sumCreditCurr
					+ "))-(PeriodDebitSum+("
					+ sumDebitCurr
					+ ")),"+digits+"), PeriodBala=round(PeriodBala+("
					+ sumCreditCurr
					+ ")-("
					+ sumDebitCurr
					+ "),"+digits+")  where SubCode='"
					+ accNumber
					+ "' and Period="
					+ period
					+ " and Nyear="
					+ periodYear
					+ " and SCompanyID='" + sunCompany + "' and isnull(DepartmentCode,'')='"+deptCode+"'";

		}
		Statement st=conn.createStatement();
		st.execute(sql);		
	}

	/**
	 * 修改科目余额
	 * 
	 * @param conn
	 *            Connection
	 * @param accNumber
	 *            String
	 * @param period
	 *            int
	 * @param periodYear
	 *            int
	 * @param periodMonth
	 *            int
	 * @param sumDebit
	 *            double 此科目的累计发生额
	 * @param sumCredit
	 *            double
	 * @param sumDebitCurr
	 *            double
	 * @param sumCreditCurr
	 *            double
	 * @param jdFlag
	 *            int
	 * @throws Exception
	 */
	public void updateAccBalance(Connection conn, CallableStatement cbs,
			Statement cs, String accNumber, int period, int periodYear,
			double sumDebit, double sumCredit, double sumDebitCurr,
			double sumCreditCurr, int jdFlag, String sunCompany)
			throws Exception {
		ResultSet rss;
		String sql = "";
		// 修改科目余额表中此损益类的科目余额
		if (jdFlag == 1) {
			sql = "update tblAccBalance set CurrYIniDebitSum=CurrYIniDebitSum+("
					+ sumDebitCurr
					+ "), CurrYIniCreditSum=CurrYIniCreditSum+("
					+ sumCreditCurr
					+ "), CurrYIniBala=CurrYIniBala+("
					+ sumDebitCurr
					+ ")-("
					+ sumCreditCurr
					+ "),"
					+ " CurrYIniDebitSumBase=CurrYIniDebitSumBase+("
					+ sumDebit
					+ "), CurrYIniCreditSumBase=CurrYIniCreditSumBase+("
					+ +sumCredit
					+ ") ,CurrYIniBalaBase=CurrYIniBalaBase+("
					+ sumDebit
					+ ")-("
					+ sumCredit
					+ "),PeriodDebitSumBase="
					+ sumDebit
					+ ", PeriodCreditSumBase="
					+ sumCredit
					+ ", PeriodDCBalaBase="
					+ sumDebit
					+ "-"
					+ sumCredit
					+ ", PeriodBalaBase=PeriodIniBase+"
					+ sumDebit
					+ "-"
					+ sumCredit
					+ ",PeriodDebitSum="
					+ sumDebitCurr
					+ ", PeriodCreditSum="
					+ sumCreditCurr
					+ ", PeriodDCBala="
					+ sumDebitCurr
					+ "-"
					+ sumCreditCurr
					+ ", PeriodBala=PeriodIni+"
					+ sumDebitCurr
					+ "-"
					+ sumCreditCurr
					+ "  where SubCode='"
					+ accNumber
					+ "' and Period="
					+ period
					+ " and Nyear="
					+ periodYear
					+ " and SCompanyID='" + sunCompany + "'";
		} else {
			sql = "update tblAccBalance set CurrYIniDebitSum=CurrYIniDebitSum+("
					+ sumDebitCurr
					+ "), CurrYIniCreditSum=CurrYIniCreditSum+("
					+ sumCreditCurr
					+ "), CurrYIniBala=CurrYIniBala+("
					+ sumCreditCurr
					+ ")-("
					+ sumDebitCurr
					+ "),"
					+ " CurrYIniDebitSumBase=CurrYIniDebitSumBase+("
					+ sumDebit
					+ "), CurrYIniCreditSumBase=CurrYIniCreditSumBase+("
					+ +sumCredit
					+ ") ,CurrYIniBalaBase=CurrYIniBalaBase+("
					+ sumCredit
					+ ")-("
					+ sumDebit
					+ "), PeriodDebitSumBase="
					+ sumDebit
					+ ", PeriodCreditSumBase="
					+ sumCredit
					+ ", PeriodDCBalaBase="
					+ sumCredit
					+ "-"
					+ sumDebit
					+ ", PeriodBalaBase=PeriodIniBase+"
					+ sumCredit
					+ "-"
					+ sumDebit
					+ ",PeriodDebitSum="
					+ sumDebitCurr
					+ ", PeriodCreditSum="
					+ sumCreditCurr
					+ ", PeriodDCBala="
					+ sumCreditCurr
					+ "-"
					+ sumDebitCurr
					+ ", PeriodBala=PeriodIni+"
					+ sumCreditCurr
					+ "-"
					+ sumDebitCurr
					+ "  where SubCode='"
					+ accNumber
					+ "' and Period="
					+ period
					+ " and Nyear="
					+ periodYear
					+ " and SCompanyID='" + sunCompany + "'";

		}
		cs.execute(sql);
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public Hashtable getTableMap() {
		return tableMap;
	}
	public void setTableMap(Hashtable tableMap) {
		this.tableMap = tableMap;
	}

	
	
}
	