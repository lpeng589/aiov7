package com.menyi.aio.web.sysAcc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.struts.util.MessageResources;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.AccBalanceBean;
import com.menyi.aio.bean.AccMainSettingBean;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.aio.web.goodsProp.GoodsPropMgt;
import com.menyi.aio.web.iniSet.IniGoodsMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.systemSafe.SystemSafeMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConnectionEnv;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.PublicMgt;

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
public class SysAccMgt extends DBManager {

	public void setCurrPeriodSysParam(String sunCompany, String userId) {
		Result rs = this.getCurrPeriod(sunCompany);
		String currPeriod;
		AccPeriodBean bean = null;
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			currPeriod = "-1";
		} else {
			bean = (AccPeriodBean) rs.getRetVal();
			currPeriod = String.valueOf(bean.getAccPeriod());
		}
		BaseEnv.accPerios.put(sunCompany, bean);
		
		Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(userId);
		ht.remove("AccPeriod");
		ht.remove("NowPeriod");
		ht.put("NowPeriod", currPeriod);
		ht.put("NowYear", bean.getAccYear());
		ht.put("NowMonth", bean.getAccMonth());
		ht.put("AccPeriod", bean);
		
	}

	public Result autoAdjust(final String sunCompany, final double iniMargin,
			final double debitMargin, final double creditMargin,
			final double balMargin,final String department) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						SysAccMgt mgt = new SysAccMgt();
						try {
							mgt.autoAdjust(sunCompany, iniMargin, debitMargin,
									creditMargin, balMargin, conn,department);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public void autoAdjust(final String sunCompany, final double iniMargin,
			final double debitMargin, final double creditMargin,
			final double balMargin, Connection conn,String department) throws Exception {
		CallableStatement cbs = conn
				.prepareCall("{call proc_updateSuper(?,?,?,?,?,?,?,?)}");
		try {
			Statement cs = conn.createStatement();

			StringBuffer accBal = new StringBuffer(""); // 修改科目余额表中资本公积

			accBal.append("update tblAccBalance set ");
			accBal.append(" CurrYIniBase=isnull(CurrYIniBase,0)+" + iniMargin
					+ ",CurrYIniCreditSumBase=isnull(CurrYIniCreditSumBase,0)+"+ creditMargin);
			accBal.append(",CurrYIniBalaBase=isnull(CurrYIniBase,0)+" + iniMargin+"+isnull(CurrYIniCreditSumBase,0)+"+ creditMargin+"-CurrYIniDebitSumBase");
			accBal.append(" where SubCode='4102' and period=-1 and SCompanyID='"+ sunCompany + "'");

			if("true".equals(BaseEnv.systemSet.get("openDeptAcc").getSetting())){
				accBal.append(" and DepartmentCode='"+department+"' ");
			}
			cs.execute(accBal.toString());
			ResultSet rss = cs.executeQuery("select classCode,JdFlag from tblAccTypeInfo where AccNumber='4102' and SCompanyID='"+ sunCompany + "'");
			String classCode = "";
			String JdFlag="";
			if (rss.next()) {
				classCode = rss.getString(1);
				JdFlag=rss.getString(2);
			}
							
				// 修改资本公积的父类
				cbs.registerOutParameter(7, java.sql.Types.INTEGER);
				cbs.registerOutParameter(8, java.sql.Types.VARCHAR);
	
				// 存储过程参数1：修改的表名，参数2：分类的表(没有为null)，参数3：前面两个表关联的条件
				// (没有为null)，参数4：修改依据的条件 (没有为null)，参数5：要修改的字段，参数6：要修改的分类代码
	
				// 修改科目余额表中父类
				cbs.setString(1, "tblAccBalance");
				cbs.setString(2, "tblAccTypeInfo");
				cbs.setString(
								3,
								"tblAccBalance.subCode=tblAccTypeInfo.accNumber and tblAccTypeInfo.SCompanyID='"
										+ sunCompany
										+ "' and tblAccBalance.SCompanyID='"
										+ sunCompany + "'");
				cbs.setString(4, "period=-1 ");
				cbs
						.setString(
								5,
								"currYIniBase=sum(currYIniBase)@SPFieldLink:currYIniDebitSumBase=sum(currYIniDebitSumBase)@SPFieldLink:CurrYIniCreditSumBase=sum(CurrYIniCreditSumBase)");
				cbs.setString(6, classCode);
				cbs.execute();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

	}

	/**
	 * 系统开账
	 * 
	 * @param loginId
	 *            String 用户ID
	 * @return Result
	 */
	public Result beginAcc(final String loginId, final String sunCompany,
			final Hashtable map, final String accIniEq) {
		// 得到会计期间验证是否已经开账
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							
							Statement cs = conn.createStatement();
							// 得到会计期间
							String periodQ = "select AccYear,AccMonth,AccPeriod from tblAccPeriod bean where bean.IsBegin=1 and SCompanyID='"+ sunCompany + "'";
							ResultSet rss = cs.executeQuery(periodQ);

							int nextPeriod = 0;
							int nextYear = 0;
							int nextMonth = 0;
							if (rss.next()) {
								nextYear = rss.getInt("AccYear");
								nextMonth = rss.getInt("AccMonth");
								nextPeriod = rss.getInt("AccPeriod");
							} else {
								rs.setRetCode(ErrorCanst.RET_BEGINACC_NOBEGINPERIOD);
								return;
							}
							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("OpenAcc");
							if (defineSqlBean != null) {
								HashMap map = new HashMap();
								Result rs3 = defineSqlBean.execute(conn, map,
										loginId, null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									rs.setRetVal(rs3.retVal);
									rs.setRetCode(rs3.retCode);
									return;
								}
							}
						
							String period = "update tblAccPeriod set statusId=1,AccStatusId=1 where IsBegin=1 and SCompanyID='"+ sunCompany + "'"; // 修改会计期间
							cs.execute(period);
							
							String sql = "";
							
							/** ***************************************判断是否启用外币，是否为固定汇率************************************ */
							Hashtable<String, SystemSettingBean> systemSet = BaseEnv.systemSet;
							String openCurrency = systemSet.get("currency")
									.getSetting();
							if (openCurrency.equals("true")) {
								String fixRate = systemSet.get("FixRate")
										.getSetting();
								if (fixRate.equals("true")) {
									sql = "select  RecordExchange from tblCurrency a left join tblSetExchange b on a.id=b.CurrencyID and period="
											+ nextPeriod
											+ " and b.periodYear="
											+ nextYear
											+ "  and  b.SCompanyID='"
											+ sunCompany
											+ "'  where IsBaseCurrency=2 ";
									rss = cs.executeQuery(sql);
									while (rss.next()) {
										double exchange = rss.getDouble(1);
										if (exchange == 0) {
											rs
													.setRetCode(ErrorCanst.RET_NOTREC_RECORDEXCHANGE);
											return; // 如果是固定汇率，所有的币种必须输入开账期间的记账汇率
										}
									}
								}
							}

							String longTime = BaseDateFormat.format(new Date(),
									BaseDateFormat.yyyyMMddHHmmss);

							/** ***************************************自动试算平衡********************************************* */
							/*
							 * 开账时 资本公积=资本公积+（资产+成本类+共同类-负债-所有者权益） 资本公积：4102
							 * 资产：10 负债：20 共同类：30 成本类：50 所有者权益：40
							 */
							String sqlEq = "select subCode,isnull(currYIniBase,0),isnull(currYIniDebitSumbase,0),isnull(currYIniCreditSumBase,0) "
									+ "from tblAccBalance where period=-1 and subCode in('10','60','20','40','50') and"
									+ " SCompanyID='" + sunCompany + "' and len(isnull(DepartmentCode,''))=0 ";
							rss = cs.executeQuery(sqlEq);
							BaseEnv.log.debug("试算平衡语句："+sqlEq);
							HashMap map = new HashMap();
							while (rss.next()) {
								map.put(rss.getString(1), new String[] {
										rss.getString(2), rss.getString(3),
										rss.getString(4) });
							}
							BigDecimal a = new BigDecimal(((String[]) map
									.get("10"))[0].toString())
									.add(new BigDecimal(((String[]) map
											.get("50"))[0].toString()));
							BigDecimal b = new BigDecimal(((String[]) map
									.get("20"))[0].toString())
									.add(new BigDecimal(((String[]) map
											.get("40"))[0].toString()));
							BigDecimal c = new BigDecimal(((String[]) map
									.get("10"))[1].toString()).add(
									new BigDecimal(
											((String[]) map.get("50"))[1]
													.toString())).add(
									new BigDecimal(
											((String[]) map.get("20"))[1]
													.toString())).add(
									new BigDecimal(
											((String[]) map.get("40"))[1]
													.toString())).add(
									new BigDecimal(
											((String[]) map.get("60"))[1]
													.toString()));

							BigDecimal d = new BigDecimal(((String[]) map
									.get("10"))[2].toString()).add(
									new BigDecimal(
											((String[]) map.get("50"))[2]
													.toString())).add(
									new BigDecimal(
											((String[]) map.get("20"))[2]
													.toString())).add(
									new BigDecimal(
											((String[]) map.get("40"))[2]
													.toString())).add(
									new BigDecimal(
											((String[]) map.get("60"))[2]
													.toString()));
							// 若为标准财务，用户录入必须平衡才能开账
							//System.out.println(((String[]) map.get("20"))[2].toString()+ "=======");
							if (accIniEq.equals("true")) {
								if (a.doubleValue() != b.doubleValue()|| c.doubleValue() != d.doubleValue()) {
									rs.setRetCode(ErrorCanst.RET_ACCNOTEQUAL);
									return;
								}
							}
							SysAccMgt accMgt = new SysAccMgt();

							/** *************************生成本期期初信息**************************************************** */
							long curTime = System.currentTimeMillis();
							// 得到所有科目余额期初信息
							ArrayList newAccBal = accMgt.getAccBalance(cs, -1,-1, sunCompany);

							sql = "select subCode from tblAccBalance where Period="
									+ nextPeriod
									+ " and Nyear="
									+ nextYear
									+ " and SCompanyID='" + sunCompany + "'";
							rss = cs.executeQuery(sql);
							List accBalanceSubCode = new ArrayList();
							while (rss.next()) {
								accBalanceSubCode.add(rss.getString("subCode"));
							}

							// 查出所有当前分支机构的会计科目和分类代码放入Hashtable中(accNumberAndClassCode)
							Hashtable<String, String> accNumberAndClassCode = new Hashtable<String, String>();
							rss = cs.executeQuery("select AccNumber,classCode from tblAccTypeInfo where SCompanyID='"+ sunCompany + "'");
							while (rss.next()) {
								accNumberAndClassCode.put(rss
										.getString("AccNumber"), rss
										.getString("classCode"));
							}
							
							for (int i = 0; i < newAccBal.size(); i++) {
								AccBalanceBean balBean = (AccBalanceBean) newAccBal.get(i);

								if (!accBalanceSubCode.contains(balBean.getSubCode())) { // 判断本期此科目信息不存在才新增本期期初信息
									if (balBean.getSubCode().indexOf("6") != 0) {
										sql = "insert into tblAccBalance (id,SubCode,CurType,createBy,lastUpdateBy,createTime,lastUpdateTime,"
												+ "statusId,CurrYIni,CurrYIniDebitSum,CurrYIniCreditSum,CurrYIniBala,PeriodIni,PeriodDebitSum,PeriodCreditSum,"
												+ "PeriodDCBala,PeriodBala,CurrYIniBase,CurrYIniDebitSumBase,CurrYIniCreditSumBase,CurrYIniBalaBase,PeriodIniBase,"
												+ "PeriodDebitSumBase,PeriodCreditSumBase,PeriodDCBalaBase,PeriodBalaBase,Period,Nyear,Nmonth,SCompanyID,DepartmentCode) values ('"
												+ IDGenerater.getId()
												+ "','"
												+ balBean.getSubCode()
												+ "','"
												+ balBean.getCurType()
												+ "','"
												+ loginId
												+ "','"
												+ loginId
												+ "','"
												+ longTime
												+ "','"
												+ longTime
												+ "',"
												+ balBean.getStatusId()
												+ ","
												+ balBean.getCurrYIni()
												+ ","
												+ balBean.getCurrYIniDebitSum()
												+ ","
												+ balBean
														.getCurrYIniCreditSum()
												+ ","
												+ balBean.getCurrYIniBala()
												+ ","
												+ balBean.getCurrYIniBala()
												+ ",0,0,0,"
												+ balBean.getCurrYIniBala()
												+ ","
												+ balBean.getCurrYIniBase()
												+ ","
												+ balBean
														.getCurrYIniDebitSumBase()
												+ ","
												+ balBean
														.getCurrYIniCreditSumBase()
												+ ","
												+ balBean.getCurrYIniBalaBase()
												+ ","
												+ balBean.getCurrYIniBalaBase()
												+ ",0,0,0,"
												+ balBean.getCurrYIniBalaBase()
												+ ","
												+ nextPeriod
												+ ","
												+ nextYear
												+ ","
												+ nextMonth
												+ ",'" + sunCompany + "','"+balBean.getDepartmentCode()+"')";
										cs.execute(sql);
									} else {
										sql = "insert into tblAccBalance (id,SubCode,CurType,createBy,lastUpdateBy,createTime,lastUpdateTime,"
												+ "statusId,CurrYIni,CurrYIniDebitSum,CurrYIniCreditSum,CurrYIniBala,PeriodIni,PeriodDebitSum,PeriodCreditSum,"
												+ "PeriodDCBala,PeriodBala,CurrYIniBase,CurrYIniDebitSumBase,CurrYIniCreditSumBase,CurrYIniBalaBase,PeriodIniBase,"
												+ "PeriodDebitSumBase,PeriodCreditSumBase,PeriodDCBalaBase,PeriodBalaBase,Period,Nyear,Nmonth,SCompanyID,DepartmentCode) values ('"
												+ IDGenerater.getId()
												+ "','"
												+ balBean.getSubCode()
												+ "','"
												+ balBean.getCurType()
												+ "','"
												+ loginId
												+ "','"
												+ loginId
												+ "','"
												+ longTime
												+ "','"
												+ longTime
												+ "',"
												+ balBean.getStatusId()
												+ ","
												+ balBean.getCurrYIni()
												+ ","
												+ balBean.getCurrYIniDebitSum()
												+ ","
												+ balBean
														.getCurrYIniCreditSum()
												+ ","
												+ balBean.getCurrYIniBala()
												+ ","
												+ balBean.getCurrYIniBala()
												+ ","
												+ balBean.getCurrYIniDebitSum()
												+ ","
												+ balBean
														.getCurrYIniCreditSum()
												+ ","
												+ balBean.getCurrYIniBala()
												+ ","
												+ balBean.getCurrYIniBala()
												+ ","
												+ balBean.getCurrYIniBase()
												+ ","
												+ balBean
														.getCurrYIniDebitSumBase()
												+ ","
												+ balBean
														.getCurrYIniCreditSumBase()
												+ ","
												+ balBean.getCurrYIniBalaBase()
												+ ","
												+ balBean.getCurrYIniBalaBase()
												+ ","
												+ balBean
														.getCurrYIniDebitSumBase()
												+ ","
												+ balBean
														.getCurrYIniCreditSumBase()
												+ ","
												+ balBean.getCurrYIniBalaBase()
												+ ","
												+ balBean.getCurrYIniBalaBase()
												+ ","
												+ nextPeriod
												+ ","
												+ nextYear
												+ ","
												+ nextMonth
												+ ",'" + sunCompany + "','"+balBean.getDepartmentCode()+"')";
										cs.execute(sql);
									}
								} 
							}
							/** ****************************期初固定资产自动产生折旧记录************************************/	
							String longDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
							sql="insert into tblFixedAssetDepreciate (id,billNo,workFlowNode,workFlowNodeName,AssetsAddId,useMonth,originalWorth,totalDeprecia,ReWorthRate,ReWorth,netWorth,currDeprecAmt"+
							",Period,PeriodYear,PeriodMonth,lastUpdateBy,lastUpdateTime,createBy,createTime,SCompanyID,statusId,deprecAccCode,DepartmentCode,DepreciateAcc)"+
								" select substring(replace(newid(),'-',''),1,28),'AD"+nextYear+nextPeriod+"'+ cast((ROW_NUMBER() over(order by GoodsNumber)) as varchar(20)),'-1','finish',tblFixedAssetAdd.id,planUseMonth,originalWorth,totalDeprecia,planReWorthRate,planReWorth,netWorth,monthDeprecAmt,"+
								"'"+nextPeriod+"','"+nextYear+"','"+nextMonth+"','"+loginId+"','"+longDate+"','"+loginId+"','"+longDate+"','"+sunCompany+"',0,deprecAccCode,DepartmentCode,DepreciateAcc from tblFixedAssetAdd join tblFixedAssetType on tblFixedAssetAdd.AssetsType=tblFixedAssetType.id  where tblFixedAssetAdd.statusId=0 and tblFixedAssetAdd.workFlowNodeName='finish' and netWorth>0 and referDeprecia like '%0%' and referMonth<planUseMonth and tblFixedAssetAdd.period=-1";
							BaseEnv.log.debug(" 产生固定资产折旧记录 "+sql);
							
							cs.execute(sql);
							//更新之后期间的所有信息
							CallableStatement cbsAfter = conn.prepareCall("{call proc_updateAllBalance(?)}");
							cbsAfter.setString(1, sunCompany);
							cbsAfter.execute();
							
							BaseEnv.log.debug("科目余额程序执行时长："
									+ ((System.currentTimeMillis() - curTime)));

						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	// 如果修改了往来期初或当前期间的往来结存，相应更新以后各会计期间的数据
	public void updateCompanyTotal(Statement cs, int currYear, int currPeriod,
			int nextYear, int nextPeriod, String sunCompany, String companyCode)
			throws SQLException {
		double[] beforeBeginAcc = new double[4];
		double[] firstPeriod = new double[4];
		String sql = "select ReceiveTotalRemain,PayTotalRemain,PreReceiveTotalRemain,PrePayTotalRemain from tblCompanyTotal where PeriodYear="
				+ currYear
				+ " and Period="
				+ currPeriod
				+ " and SCompanyID='"
				+ sunCompany + "' and CompanyCode='" + companyCode + "'";
		ResultSet rs = cs.executeQuery(sql);
		if (rs.next()) {
			beforeBeginAcc[0] = rs.getDouble("ReceiveTotalRemain");
			beforeBeginAcc[1] = rs.getDouble("PayTotalRemain");
			beforeBeginAcc[2] = rs.getDouble("PreReceiveTotalRemain");
			beforeBeginAcc[3] = rs.getDouble("PrePayTotalRemain");
		}

		sql = "select ReceiveBegin,PayBegin,PreReceiveBegin,PrePayBegin from tblCompanyTotal where PeriodYear="
				+ nextYear
				+ " and Period="
				+ nextPeriod
				+ " and SCompanyID='"
				+ sunCompany + "' and CompanyCode='" + companyCode + "'";
		rs = cs.executeQuery(sql);
		if (rs.next()) {
			firstPeriod[0] = rs.getDouble("ReceiveBegin");
			firstPeriod[1] = rs.getDouble("PayBegin");
			firstPeriod[2] = rs.getDouble("PreReceiveBegin");
			firstPeriod[3] = rs.getDouble("PrePayBegin");
		}

		double receiveMargin = new BigDecimal(beforeBeginAcc[0]).subtract(
				new BigDecimal(firstPeriod[0])).doubleValue();
		double payMargin = new BigDecimal(beforeBeginAcc[1]).subtract(
				new BigDecimal(firstPeriod[1])).doubleValue();
		double preReceiveMargin = new BigDecimal(beforeBeginAcc[2]).subtract(
				new BigDecimal(firstPeriod[2])).doubleValue();
		double prePayMargin = new BigDecimal(beforeBeginAcc[3]).subtract(
				new BigDecimal(firstPeriod[3])).doubleValue();
		if (receiveMargin != 0 || payMargin != 0 || preReceiveMargin != 0
				|| prePayMargin != 0) {// 如果修改过往来应收期初,更新以后各期间的往来应收数据
			sql = "update tblCompanyTotal set " + "ReceiveBegin=ReceiveBegin+"
					+ receiveMargin + ",ReceiveTotalRemain=ReceiveTotalRemain+"
					+ receiveMargin + ",PayBegin=PayBegin+" + payMargin
					+ ",PayTotalRemain=PayTotalRemain+" + payMargin
					+ ",PreReceiveBegin=PreReceiveBegin+" + preReceiveMargin
					+ ",PreReceiveTotalRemain=PreReceiveTotalRemain+"
					+ preReceiveMargin + ",PrePayBegin=PrePayBegin+"
					+ prePayMargin + ",PrePayTotalRemain=PrePayTotalRemain+"
					+ prePayMargin + " where (PeriodYear=" + nextYear
					+ " and Period>=" + nextPeriod + " or PeriodYear>"
					+ nextYear + ") and SCompanyID='" + sunCompany
					+ "' and CompanyCode='" + companyCode + "'";
			cs.execute(sql);
		}
	}
	

	public void updateParentAccBal(Connection conn, int period, int periodYear) {
		try {
			String sql = "select subcode from tblAccBalance where period="
					+ period + " and Nyear=" + periodYear + " order by subcode";
			Statement cs = conn.createStatement();
			ResultSet rss = cs.executeQuery(sql);
			List codes = new ArrayList();
			while (rss.next()) {
				codes.add(rss.getString(1));
			}
			for (int i = 0; i < codes.size(); i++) {
				sql = "select isNull(sum(isNull(CurrYIni,0)),0) CurrYIni,"
						+ "isNull(sum(isNull(CurrYiniDebitSum,0)),0) CurrYiniDebitSum,"
						+ "isNull(sum(isNull(CurrYIniCreditSum,0)),0) CurrYIniCreditSum,"
						+ "isNull(sum(isNull(CurrYiniDebitSumBase,0)),0) CurrYiniDebitSumBase,"
						+ "isNull(sum(isNull(CurrYIniCreditSumBase,0)),0) CurrYIniCreditSumBase,"
						+ "isNull(sum(isNull(PeriodDebitSumBase,0)),0) PeriodDebitSumBase,"
						+ "isNull(sum(isNull(PeriodCreditSumBase,0)),0) PeriodCreditSumBase,"
						+ "isNull(sum(isNull(PeriodIni,0)),0) PeriodIni,"
						+ "isNull(sum(isNull(PeriodDebitSum,0)),0) PeriodDebitSum,"
						+ "isNull(sum(isNull(PeriodCreditSum,0)),0) PeriodCreditSum," 
						+ "isNull(sum(isNull(CurrYIniBase,0)),0) CurrYIniBase," 
						+ "isNull(sum(isNull(PeriodIniBase,0)),0) PeriodIniBase from tblAccTypeInfo a,tblAccBalance b where b.subCode=a.accNumber and b.period="
						+ period
						+ " and b.Nyear="
						+ periodYear
						+ " and a.classCode like(select classCode from tblAccTypeInfo c where c.accNumber='"
						+ codes.get(i) + "')+'%' and a.isCatalog=0";
				rss = cs.executeQuery(sql);
				sql = "";
				if (rss.next()) {
					sql = "update tblAccBalance set CurrYIni="
							+ rss.getString(1) + ",CurrYiniDebitSum="
							+ rss.getString(2) + ",CurrYIniCreditSum="
							+ rss.getString(3) + ",CurrYiniDebitSumBase="
							+ rss.getString(4) + "," + "CurrYIniCreditSumBase="
							+ rss.getString(5) + ",PeriodDebitSumBase="
							+ rss.getString(6) + ",PeriodCreditSumBase="
							+ rss.getString(7) + ",PeriodIni="
							+ rss.getString(8) + ",PeriodDebitSum="
							+ rss.getString(9) + ",PeriodCreditSum="
							+ rss.getString(10) + ",CurrYIniBase="
							+ rss.getString(11)+ ",PeriodIni="
							+ rss.getString(12) + " where period=" + period
							+ " and Nyear=" + periodYear + " and subcode='"
							+ codes.get(i) + "'";
				}
				if (sql.length() > 0) {
					cs.execute(sql);
				}
			}

			sql = "update tblAccBalance set CurrYIni=isnull(CurrYIni,0),CurrYiniDebitSum=isnull(CurrYiniDebitSum,0),CurrYIniCreditSum=isnull(CurrYIniCreditSum,0),CurrYIniBase=isnull(CurrYIniBase,0),CurrYiniDebitSumBase=isnull(CurrYiniDebitSumBase,0),CurrYIniCreditSumBase=isnull(CurrYIniCreditSumBase,0)"
					+ ",PeriodIniBase=isnull(PeriodIniBase,0),PeriodDebitSumBase=isnull(PeriodDebitSumBase,0),PeriodCreditSumBase=isnull(PeriodCreditSumBase,0),PeriodIni=isnull(PeriodIni,0),PeriodDebitSum=isnull(PeriodDebitSum,0),PeriodCreditSum=isnull(PeriodCreditSum,0) where period="
					+ period + " and Nyear=" + periodYear;
			System.out.println(sql);
			cs.execute(sql);

			sql = "update tblAccBalance set CurrYIniBala=CurrYIni+CurrYiniDebitSum-CurrYiniCreditSum,"
					+ "CurrYIniBalaBase=CurrYIniBase+CurrYiniDebitSumBase-CurrYiniCreditSumBase,"
					+ "PeriodDCBala=PeriodDebitSum-PeriodCreditSum,"
					+ "PeriodBala=PeriodIni+PeriodDebitSum-PeriodCreditSum,"
					+ "PeriodDCBalaBase=PeriodDebitSumBase-PeriodCreditSumBase,"
					+ "PeriodBalaBase=PeriodIniBase+PeriodDebitSumBase-PeriodCreditSumBase"
					+ " where (select isCatalog from tblAccTypeInfo a where a.accNumber=tblAccBalance.subCode)=1 and period="
					+ period
					+ " and Nyear="
					+ periodYear
					+ " and (select jdFlag from tblAccTypeInfo a where a.accNumber=tblAccBalance.subCode)=1 ";
			System.out.println(sql);
			cs.execute(sql);
			sql = "update tblAccBalance set CurrYIniBala=CurrYIni+CurrYiniCreditSum-CurrYiniDebitSum,"
					+ "CurrYIniBalaBase=CurrYIniBase+CurrYiniCreditSumBase-CurrYiniDebitSumBase,"
					+ "PeriodDCBala=PeriodCreditSum-PeriodDebitSum,"
					+ "PeriodBala=PeriodIni+PeriodCreditSum-PeriodDebitSum,"
					+ "PeriodDCBalaBase=PeriodCreditSumBase-PeriodDebitSumBase,"
					+ "PeriodBalaBase=PeriodIniBase+PeriodCreditSumBase-PeriodDebitSumBase"
					+ " where (select isCatalog from tblAccTypeInfo a where a.accNumber=tblAccBalance.subCode)=1 and period="
					+ period
					+ " and Nyear="
					+ periodYear
					+ " and (select jdFlag from tblAccTypeInfo a where a.accNumber=tblAccBalance.subCode)=2";
			cs.execute(sql);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public ArrayList getAccBalance(Statement cs, int period, int periodYear,
			String sunCompany) throws Exception {

		String iniAccBals = "select * from tblAccBalance  bean where bean.Period="
				+ period
				+ " and Nyear="
				+ periodYear
				+ " and SCompanyID='"
				+ sunCompany + "'";
		ResultSet rss = cs.executeQuery(iniAccBals);

		ArrayList newAccBal = new ArrayList();

		while (rss.next()) {
			AccBalanceBean newBean = new AccBalanceBean();

			newBean.setCurrYIni(rss.getString("CurrYIni")==null?"0":rss.getString("CurrYIni"));
			newBean.setCurrYIniDebitSum(rss.getString("CurrYIniDebitSum")==null?"0":rss.getString("CurrYIniDebitSum"));
			newBean.setCurrYIniCreditSum(rss.getString("CurrYIniCreditSum")==null?"0":rss.getString("CurrYIniCreditSum"));
			newBean.setCurrYIniBala(rss.getString("CurrYIniBala")==null?"0":rss.getString("CurrYIniBala"));

			newBean.setCurrYIniBase(rss.getString("CurrYIniBase")==null?"0":rss.getString("CurrYIniBase"));
			newBean.setCurrYIniDebitSumBase(rss
					.getString("CurrYIniDebitSumBase"));
			newBean.setCurrYIniCreditSumBase(rss
					.getString("CurrYIniCreditSumBase"));
			newBean.setCurrYIniBalaBase(rss.getString("CurrYIniBalaBase"));

			newBean.setCurType(rss.getString("CurType")==null?"":rss.getString("CurType"));
			newBean.setSubCode(rss.getString("SubCode"));

			newBean.setPeriodIni(rss.getString("PeriodIni"));
			newBean.setPeriodDebitSum(rss.getString("PeriodDebitSum"));
			newBean.setPeriodCreditSum(rss.getString("PeriodCreditSum"));
			newBean.setPeriodDCBala(rss.getString("PeriodDCBala"));
			newBean.setPeriodBala(rss.getString("PeriodBala"));

			newBean.setPeriodIniBase(rss.getString("PeriodIniBase"));
			newBean.setPeriodDebitSumBase(rss.getString("PeriodDebitSumBase"));
			newBean.setPeriodCreditSumBase(rss.getString("PeriodCreditSumBase"));
			newBean.setPeriodDCBalaBase(rss.getString("PeriodDCBalaBase"));
			newBean.setPeriodBalaBase(rss.getString("PeriodBalaBase"));

			newBean.setStatusId(rss.getInt("StatusId"));
			String DepartmentCode=rss.getString("DepartmentCode");
			newBean.setDepartmentCode(DepartmentCode==null?"":DepartmentCode);
			
			newAccBal.add(newBean);
		}

		return newAccBal;
	}

	/**
	 * 系统反开账
	 * 
	 * @return Result
	 */
	public Result reBeginAcc(final String delIni, final String delBase,
			final String delBill,final String delDraft, final String sunCompany, final String userId,
			final String accName) {
		// 得到会计期间验证是否有已经月结的会计期间，如果已经月结必须进行反月结
		ArrayList paramLists = new ArrayList();
		String periods = "select bean from  AccPeriodBean bean where bean.statusId=2 and bean.SCompanyID=?";
		paramLists.add(sunCompany);
		Result rs2 = this.list(periods, paramLists);
		if (rs2.getRealTotal() > 0) {
			rs2.setRetCode(ErrorCanst.RET_SETTLE_END);
			return rs2;
		}

		periods = "select bean from  AccPeriodBean bean where bean.statusId=1 and bean.SCompanyID=?";
		rs2 = this.list(periods, paramLists);
		if (rs2.getRealTotal() == 0) {
			rs2.setRetCode(ErrorCanst.RET_NOTBEGINACC);
			return rs2;
		}

		
		
		if (accName.length() > 0) {
			//调用备份数据库存储过程
			Result rb = new Result();
			Result rs = new SystemSafeMgt().querySafeValues();
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				rb.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				rb.setRetVal("查询备份路径异常");
				return rb;			
			}
			HashMap<String,String> map = (HashMap)rs.retVal;
			
			String backPath = map.get("backPath"); //数据备份保存路径
			if(backPath==null || backPath.length()  == 0){
				String userDir = System.getProperty("user.dir");
				String defDisk = userDir.substring(0,userDir.indexOf(":")+1);
				backPath = defDisk+"\\AioDefDbBakup";
			}			
			rb = this.backupDataBase(backPath);
			if(rb.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return rb;
			}
		}
		AccPeriodBean periodBean=(AccPeriodBean)((List)rs2.getRetVal()).get(0);
		final int accPeriod=periodBean.getAccPeriod();
		final int accYear=periodBean.getAccYear();
		//查询当期期间是否存在已经计提的资产
		rs2=this.queryDepreAsset(periodBean.getAccPeriod(), periodBean.getAccYear(), periodBean.getAccMonth(), sunCompany);
		if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			rs2.setRetCode(ErrorCanst.RET_ASSET_NODEPRECIATE);
			return rs2;
		}
		
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							
							//删除固定资产上期自动产生的折旧记录	
							String sql="delete from tblFixedAssetDepreciate where Period="+ accPeriod+ " and PeriodYear="+ accYear+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);
							//删除凭证模板
							sql = "delete from tblAccMainTemplete";
							cs.execute(sql);
							//删除摘要
							sql = "delete from tblRecordComment";
							cs.execute(sql);
							//反开帐时一定会执行的Define,不管有没有勾选
							DefineSQLBean defineSqlBeanOpen = (DefineSQLBean) BaseEnv.defineSqlMap
									.get("DeOpenAcc");
							if (defineSqlBeanOpen != null) {
								HashMap map = new HashMap();
								Result rs3 = defineSqlBeanOpen.execute(conn,
										map, userId, null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									rs.setRetVal(rs3.retVal);
									rs.setRetCode(rs3.retCode);
									return;
								}
							}

							if (delBase.equals("true")) {
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap
										.get("DeOpenAccBase");
								if (defineSqlBean != null) {
									HashMap map = new HashMap();

									Result rs3 = defineSqlBean.execute(conn,
											map, userId, null, null, "");
									if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
										rs.setRetCode(rs3.retCode);
										return;
									}
								}
							}
							if (delBill.equals("true")||delBase.equals("true")) {
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap
										.get("DeOpenAccBill");
								if (defineSqlBean != null) {
									HashMap map = new HashMap();

									Result rs3 = defineSqlBean.execute(conn,
											map, userId, null, null, "");
									if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
										rs.setRetCode(rs3.retCode);
										return;
									}
								}
							}
							//清空内存的单据编号
							new BillNoManager().clear();

							// 判断用户是否选择了删除期初信息
							if (delIni.equals("true")||delBase.equals("true")) {
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap
										.get("DeOpenAccIni");
								if (defineSqlBean != null) {
									HashMap map = new HashMap();

									Result rs3 = defineSqlBean.execute(conn,
											map, userId, null, null, "");
									if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
										rs.setRetCode(rs3.retCode);
										return;
									}
								}
							}
							
							// 判断用户是否选择了删除草稿信息
							if (delDraft.equals("true")) {
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap
										.get("DeOpenAccDraft");
								if (defineSqlBean != null) {
									HashMap map = new HashMap();

									Result rs3 = defineSqlBean.execute(conn,
											map, userId, null, null, "");
									if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
										rs.setRetCode(rs3.retCode);
										return;
									}
								}
							}
							// 将会计期间的值设为开账前的状态
							sql = "update tblAccPeriod set statusId=0,AccStatusId=0 where SCompanyID='"
									+ sunCompany + "'";
							cs.execute(sql);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 判断是否能够年结存
	 * 
	 * @param map
	 *            Hashtable
	 * @param language
	 *            String
	 * @return Result
	 */
	public Result isAbleYearSettleAcc(final String sunCompany) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							//先删除开帐期间之前的期间数据
							
							Statement st = conn.createStatement();
							st.execute("delete from tblAccPeriod where accyear < (select accYear from tblAccPeriod where isbegin=1)");
							
							st = conn.createStatement();
							
							String sql = "select AccStatusId from tblAccPeriod where AccYear=(select min(AccYear) from tblAccPeriod) and SCompanyID='"
								+ sunCompany
								+ "'  order by AccYear desc,AccPeriod desc";

							ResultSet rss = st.executeQuery(sql);
							int statusId=0;
							if (rss.next()) {
								statusId = rss.getInt(1);
							}
							if (statusId != 2) {
								rs.setRetCode(ErrorCanst.RET_NOTSETTLEACC_LAST);
							}								
							
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 查询月结的会计期间年月及月结至哪个会计期间年月
	 * 
	 * @return
	 */
	public Result getAccSettlePeriodFromTo() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							ArrayList list = new ArrayList();
							PreparedStatement st = conn
									.prepareStatement("select AccYear,AccPeriod from tblaccperiod where AccStatusId=1");
							ResultSet rss = st.executeQuery();
							Object[] curPeriod = new Object[2];
							if (rss.next()) {
								curPeriod[0] = rss.getInt(1);
								curPeriod[1] = rss.getInt(2);
								list.add(curPeriod);
							}
							
							rss.close();
							st = conn
									.prepareStatement("select top 1 AccYear,AccPeriod from tblaccperiod where AccYear>? or (AccYear=? and AccPeriod>?)  order by AccYear,AccPeriod");
							st.setInt(1, Integer.parseInt(curPeriod[0]
									.toString()));
							st.setInt(2, Integer.parseInt(curPeriod[0]
									.toString()));
							st.setInt(3, Integer.parseInt(curPeriod[1]
									.toString()));
							rss = st.executeQuery();
							if (rss.next()) {
								Object[] nextPriod = new Object[2];
								nextPriod[0] = rss.getInt(1);
								nextPriod[1] = rss.getInt(2);
								list.add(nextPriod);
							} else {
								Object[] nextPriod = new Object[2];
								if (Integer.parseInt(curPeriod[1].toString()) != 12) {
									nextPriod[0] = curPeriod[0].toString();
									nextPriod[1] = String
											.valueOf(Integer
													.parseInt(curPeriod[1]
															.toString()) + 1);
								} else {
									nextPriod[0] = String
											.valueOf(Integer
													.parseInt(curPeriod[0]
															.toString()) + 1);
									nextPriod[1] = "1";
								}
								list.add(nextPriod);
							}
							rss.close();
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 查询月结的会计期间年月及月结至哪个会计期间年月
	 * 
	 * @return
	 */
	public Result getSettlePeriodFromTo() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							ArrayList list = new ArrayList();
							PreparedStatement st = conn.prepareStatement("select AccYear,AccPeriod from tblaccperiod where statusId=1");
							ResultSet rss = st.executeQuery();
							Object[] curPeriod = new Object[2];
							if (rss.next()) {
								curPeriod[0] = rss.getInt(1);
								curPeriod[1] = rss.getInt(2);
								list.add(curPeriod);
							}
							rss.close();
							if(list.size()>0){
								st = conn.prepareStatement("select top 1 AccYear,AccPeriod from tblaccperiod where AccYear>? or (AccYear=? and AccPeriod>?)  order by AccYear,AccPeriod");
								st.setInt(1, Integer.parseInt(curPeriod[0].toString()));
								st.setInt(2, Integer.parseInt(curPeriod[0].toString()));
								st.setInt(3, Integer.parseInt(curPeriod[1].toString()));
								rss = st.executeQuery();
								if (rss.next()) {
									Object[] nextPriod = new Object[2];
									nextPriod[0] = rss.getInt(1);
									nextPriod[1] = rss.getInt(2);
									list.add(nextPriod);
								} else {
									Object[] nextPriod = new Object[2];
									if (Integer.parseInt(curPeriod[1].toString()) != 12) {
										nextPriod[0] = curPeriod[0].toString();
										nextPriod[1] = String.valueOf(Integer.parseInt(curPeriod[1].toString()) + 1);
									} else {
										nextPriod[0] = String.valueOf(Integer.parseInt(curPeriod[0].toString()) + 1);
										nextPriod[1] = "1";
									}
									list.add(nextPriod);
								}
								rss.close();
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 查询月结的会计期间年月及反月结至哪个会计期间年月
	 * 
	 * @return
	 */
	public Result getReSettlePeriodFromTo() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							ArrayList list = new ArrayList();
							PreparedStatement st = conn
									.prepareStatement("select AccYear,AccPeriod from tblaccperiod where statusId=1 and isBegin!=1");
							ResultSet rss = st.executeQuery();
							Object[] curPeriod = new Object[2];
							if (rss.next()) {
								curPeriod[0] = rss.getInt(1);
								curPeriod[1] = rss.getInt(2);
								list.add(curPeriod);
							}
							if(list.size()==0){
								rs.setRetCode(ErrorCanst.ER_NO_DATA);
								return;
							}
							rss.close();
							st = conn
									.prepareStatement("select top 1 AccYear,AccPeriod from tblaccperiod where AccYear<? or (AccYear=? and AccPeriod<?) order by AccYear desc,AccPeriod desc");
							st.setInt(1, Integer.parseInt(curPeriod[0]
									.toString()));
							st.setInt(2, Integer.parseInt(curPeriod[0]
									.toString()));
							st.setInt(3, Integer.parseInt(curPeriod[1]
									.toString()));
							rss = st.executeQuery();
							if (rss.next()) {
								Object[] nextPriod = new Object[2];
								nextPriod[0] = rss.getInt(1);
								nextPriod[1] = rss.getInt(2);
								list.add(nextPriod);
							} else {
								Object[] nextPriod = new Object[2];
								nextPriod[0] = -1;
								nextPriod[1] = -1;
								list.add(nextPriod);
							}
							rss.close();
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 查询月结的会计期间年月及反月结至哪个会计期间年月
	 * 
	 * @return
	 */
	public Result getReSettlePeriodFromToA() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							ArrayList list = new ArrayList();
							PreparedStatement st = conn
									.prepareStatement("select AccYear,AccPeriod from tblaccperiod where AccStatusId=1 and isBegin!=1");
							ResultSet rss = st.executeQuery();
							Object[] curPeriod = new Object[2];
							if (rss.next()) {
								curPeriod[0] = rss.getInt(1);
								curPeriod[1] = rss.getInt(2);
								list.add(curPeriod);
							}
							if(list.size()==0){
								rs.setRetCode(ErrorCanst.ER_NO_DATA);
								return;
							}
							rss.close();
							st = conn
									.prepareStatement("select top 1 AccYear,AccPeriod from tblaccperiod where AccYear<? or (AccYear=? and AccPeriod<?) order by AccYear desc,AccPeriod desc");
							st.setInt(1, Integer.parseInt(curPeriod[0]
									.toString()));
							st.setInt(2, Integer.parseInt(curPeriod[0]
									.toString()));
							st.setInt(3, Integer.parseInt(curPeriod[1]
									.toString()));
							rss = st.executeQuery();
							if (rss.next()) {
								Object[] nextPriod = new Object[2];
								nextPriod[0] = rss.getInt(1);
								nextPriod[1] = rss.getInt(2);
								list.add(nextPriod);
							} else {
								Object[] nextPriod = new Object[2];
								nextPriod[0] = -1;
								nextPriod[1] = -1;
								list.add(nextPriod);
							}
							rss.close();
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 查询月结的会计期间年月及反月结至哪个会计期间年月
	 * 
	 * @return
	 */
	public Result getStockReSettlePeriodFromTo() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							ArrayList curPeriodList=new ArrayList();
							ArrayList lastPeriodList=new ArrayList();
							ArrayList list=new ArrayList();
							
							PreparedStatement st = conn
									.prepareStatement("select a.StockCode,b.StockFullName,a.AccYear,a.AccPeriod from tblAccPeriodStock a,tblStock b where a.StockCode=b.classCode and a.statusId=1");
							ResultSet rss = st.executeQuery();
							
							while (rss.next()) {
								Object[] curPeriod = new Object[6];
								curPeriod[0]=rss.getString(1);
								curPeriod[1]=rss.getString(2);        
								curPeriod[2]=rss.getInt(3);
								curPeriod[3]=rss.getInt(4);
								curPeriod[4]=-1;
								curPeriod[5]=-1;
								curPeriodList.add(curPeriod);
							}
							if(curPeriodList.size()==0){
								rs.setRetCode(ErrorCanst.ER_NO_DATA);
								return;
							}
							rss.close();
							st = conn.prepareStatement("select a.StockCode,b.StockFullName,a.AccYear,AccPeriod from tblAccPeriodStock a,tblStock b where a.StockCode=b.classCode and a.statusId=2 and a.id=(select top 1 id from tblAccPeriodStock b where b.StockCode=a.StockCode and periodBegin<(select periodBegin from tblAccPeriodStock c where c.StockCode=a.StockCode and c.statusId=1) order by periodBegin desc)");

							rss = st.executeQuery();
							while (rss.next()) {
								Object[] lastPriod = new Object[4];
								lastPriod[0] = rss.getString(1);
								lastPriod[1] = rss.getString(2);
								lastPriod[2] = rss.getInt(3);
								lastPriod[3] = rss.getInt(4);
								lastPeriodList.add(lastPriod);
							} 
							for(int i=0;i<curPeriodList.size();i++){
								Object[] obj=(Object[])curPeriodList.get(i);
								for(int j=0;j<lastPeriodList.size();j++){
									Object[] objN=(Object[])lastPeriodList.get(j);
									if(obj[0].equals(objN[0])){
										obj[4]=objN[2];
										obj[5]=objN[3];
										break;
									}
								}
								list.add(obj);
							}
							
							
							if(lastPeriodList.size()==0){
								Object[] lastPriod = new Object[4];
								lastPriod[0] = -1;
								lastPriod[1] = -1;
								lastPeriodList.add(lastPriod);
							}
							rss.close();
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 查询月结的会计期间年月及反月结至哪个会计期间年月
	 * 
	 * @return
	 */
	public Result getStockSettlePeriodFromTo() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							ArrayList curPeriodList=new ArrayList();
							ArrayList nextPeriodList=new ArrayList();
							ArrayList list=new ArrayList();
							
							PreparedStatement st = conn
									.prepareStatement("select a.StockCode,b.StockFullName,a.AccYear,a.AccPeriod from tblAccPeriodStock a,tblStock b where a.StockCode=b.classCode and a.statusId=1");
							ResultSet rss = st.executeQuery();
							
							while (rss.next()) {
								Object[] curPeriod = new Object[6];
								curPeriod[0]=rss.getString(1);
								curPeriod[1]=rss.getString(2);        
								curPeriod[2]=rss.getInt(3);
								curPeriod[3]=rss.getInt(4);
								curPeriod[4]=-1;
								curPeriod[5]=-1;
								curPeriodList.add(curPeriod);
							}
							if(curPeriodList.size()==0){
								rs.setRetCode(ErrorCanst.ER_NO_DATA);
								return;
							}
							rss.close();
							st = conn.prepareStatement("select a.StockCode,b.StockFullName,a.AccYear,AccPeriod from tblAccPeriodStock a,tblStock b where a.StockCode=b.classCode and a.id=(select top 1 id from tblAccPeriodStock b where b.StockCode=a.StockCode and periodBegin>(select periodBegin from tblAccPeriodStock c where c.StockCode=a.StockCode and c.statusId=1) order by periodBegin)");

							rss = st.executeQuery();
							while (rss.next()) {
								Object[] nextPriod = new Object[4];
								nextPriod[0] = rss.getString(1);
								nextPriod[1] = rss.getString(2);
								nextPriod[2] = rss.getInt(3);
								nextPriod[3] = rss.getInt(4);
								nextPeriodList.add(nextPriod);
							} 
							for(int i=0;i<curPeriodList.size();i++){
								Object[] obj=(Object[])curPeriodList.get(i);
								for(int j=0;j<nextPeriodList.size();j++){
									Object[] objN=(Object[])nextPeriodList.get(j);
									if(obj[0].equals(objN[0])){
										obj[4]=objN[2];
										obj[5]=objN[3];
										break;
									}
								}
								list.add(obj);
							}
							
							
							if(nextPeriodList.size()==0){
								Object[] nextPriod = new Object[4];
								nextPriod[0] = -1;
								nextPriod[1] = -1;
								nextPeriodList.add(nextPriod);
							}
							rss.close();
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	public Result getMaxPeriodOfCurrYear(final int periodYear) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							PreparedStatement st = conn
									.prepareStatement("select top 1 AccPeriod from tblaccperiod where AccYear=? order by AccPeriod desc");
							st.setInt(1, periodYear);
							ResultSet rss = st.executeQuery();
							rs.setRetVal(0);
							if (rss.next()) {
								rs.setRetVal(rss.getInt(1));
							}
							rss.close();
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 判断是否可月结
	 * 
	 * @param map
	 *            Hashtable
	 * @param language
	 *            String
	 * @return Result
	 */
	public Result isAbleSettleAcc(final Hashtable map, final String language,
			final String sunCompany) {
		final Result rs = this.getCurrPeriod(sunCompany);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}

		final AccPeriodBean periodBean = (AccPeriodBean) rs.getRetVal();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							ResultSet rss = null;
							// 得到所有的凭证表头信息保存在hashMap（单据类型，属于此单据类型单据ID列表）
							String sql = "select RefBillType,RefBillID from tblAccMain where CredYear="
									+ periodBean.getAccYear()
									+ " and period="
									+ periodBean.getAccPeriod();
							HashMap accMainMap = new HashMap();
							rss = cs.executeQuery(sql);
							while (rss.next()) {
								String billType = rss.getString(1);
								String billID = rss.getString(2);
								if (billID != null) {
									ArrayList IDS;
									Object obj = accMainMap.get(billType);
									if (obj == null) {
										IDS = new ArrayList();
										accMainMap.put(billType, IDS);
									} else {
										IDS = (ArrayList) obj;
									}
									IDS.add(billID);
								}
							}

							ArrayList validate = new ArrayList();// 保存未生成凭证的单据，

							// 验证单据是否都生成凭证
							// 采购发票(tblBuyInvoice)
							sql = " select a.id,a.BillNo,a.BillDate from tblBuyInvoices  a "
									+ " where  workFlowNodeName='finish' and a.SCompanyID='"
									+ sunCompany
									+ "' and PeriodYear="
									+ periodBean.getAccYear()
									+ " and Period="
									+ periodBean.getAccPeriod();
							DBTableInfoBean tableInfo = DDLOperation
									.getTableInfo(map, "tblBuyInvoices");
							if (tableInfo != null) {
								validate(tableInfo.getDisplay().get(language)
										.toString(), "tblBuyInvoices", cs, sql,
										accMainMap, validate);
							}
						
							
							// 费用单(tblExpensed)
							sql = " select a.id,a.BillNo,a.BillDate   from tblExpensed  a "
									+ " where workFlowNodeName='finish' and  a.SCompanyID='"
									+ sunCompany
									+ "' and PeriodYear="
									+ periodBean.getAccYear()
									+ " and Period="
									+ periodBean.getAccPeriod();
							tableInfo = DDLOperation.getTableInfo(map,
									"tblExpensed");
							validate(tableInfo.getDisplay().get(language)
									.toString(), "tblExpensed", cs, sql,
									accMainMap, validate);
							// 收入单(tblIncome)
							sql = " select a.id,a.BillNo,a.BillDate   from tblIncome  a "
									+ " where workFlowNodeName='finish' and   a.SCompanyID='"
									+ sunCompany
									+ "' and PeriodYear="
									+ periodBean.getAccYear()
									+ " and Period="
									+ periodBean.getAccPeriod();
							tableInfo = DDLOperation.getTableInfo(map,
									"tblIncome");
							validate(tableInfo.getDisplay().get(language)
									.toString(), "tblIncome", cs, sql,
									accMainMap, validate);

							rs.setRetVal(validate);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 得到未生成凭证的单据
	 * 
	 * @param disTabName
	 *            String 显示的表名
	 * @param tabName
	 *            String 表名
	 * @param cs
	 *            Statement
	 * @param sql
	 *            String
	 * @param accMainMap
	 *            HashMap 所有的凭证表头
	 * @param validate
	 *            ArrayList 未生成凭证的单据信息
	 * @return Result
	 */
	public Result validate(String disTabName, String tabName, Statement cs,
			String sql, HashMap accMainMap, ArrayList validate) {
		Result rs = new Result();
		try {
			ResultSet rss = cs.executeQuery(sql);
			String id;
			Object obj;
			obj = accMainMap.get(tabName);
			ArrayList IDS;
			while (rss.next()) {
				id = rss.getString(1);
				boolean flag = false;
				if (obj == null) {
					flag = true;
				} else if (obj != null) {
					IDS = (ArrayList) obj;
					if (!IDS.contains(id)) {
						flag = true;
					}
				}

				if (flag) {
					String[] bill = new String[4];
					bill[0] = id;
					bill[1] = rss.getString(2);
					bill[2] = disTabName;
					bill[3] = rss.getString(3);
					validate.add(bill);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			rs.setRetVal(ex.getMessage());
		}
		return rs;
	}

	/**
	 * 获得商品全名
	 * 
	 * @param goodsCode
	 *            String
	 * @return Result
	 */
	public Result getGoodsName(final String goodsCode) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement cs = conn.createStatement();
						String sql = "select GoodsFullName from tblGoods where classCode='"
								+ goodsCode + "'";
						ResultSet rss = cs.executeQuery(sql);
						try {
							if (rss.next()) {
								String goodsFullName = rss.getString(1);
								rs.setRetVal(goodsFullName);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 获得仓库全称
	 * 
	 * @param billType
	 *            String
	 * @return Result
	 */
	public Result getStockFullName(final String stockCode) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement cs = conn.createStatement();
						// 得到当前会计期间
						String sql = "select StockFullName from tblStock where classCode='"
								+ stockCode + "'";
						ResultSet rss = cs.executeQuery(sql);
						try {
							if (rss.next()) {
								String stockFullName = rss.getString(1);
								rs.setRetVal(stockFullName);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 获得单据类型
	 * 
	 * @param billType
	 *            String
	 * @return Result
	 */
	public Result getModelName(final String billType) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement cs = conn.createStatement();
						// 得到当前会计期间
						String sql = "select top 1 modelName from tblModules where tblName='"
								+ billType + "'";
						ResultSet rss = cs.executeQuery(sql);
						try {
							if (rss.next()) {
								String modelName = rss.getString(1);
								rs.setRetVal(modelName);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 查找库存明细表中字段是否为属性字段
	 * 
	 * @param conn
	 *            Connection
	 * @return List
	 */
	public boolean isExistOfPropName(Connection conn, String propName) {
		boolean isExist = false;
		List list = new ArrayList();
		try {
			Statement cs = conn.createStatement();
			String sql = "";
			sql = "select * from tblGoodsPropInfo where propName='" + propName
					+ "' and isUsed=1 and isCalculate=1";// 属性启用而且进行成本计算
			ResultSet rsss = cs.executeQuery(sql);
			if (rsss.next()) {
				isExist = true;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return isExist;
	}

	/**
	 * 获得库存明细表中的所属性字段名
	 * 
	 * @param conn
	 *            Connection
	 * @return List
	 */
	public Result queryAllPropName() {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement cs = conn.createStatement();
						String sql = "select count(*) from tblDBTableInfo where tableName='tblGoodsPropInfo'";
						ResultSet rss = cs.executeQuery(sql);
						int count = 0;
						if (rss.next()) {
							count = rss.getInt(1);
						}
						if (count > 0) {
							// 得到当前会计期间
							sql = "select fieldName from tblDBFieldInfo where tableId=(select id from tblDBTableInfo where tableName='tblStockDet')";
							rss = cs.executeQuery(sql);
							try {
								List list = new ArrayList();
								while (rss.next()) {
									list.add(rss.getString("fieldName"));
								}
								List existPropNames = new ArrayList();
								for (int i = 0; i < list.size(); i++) {
									if (isExistOfPropName(conn, list.get(i)
											.toString())) {
										existPropNames.add(list.get(i)); // 添加存在的属性字段名
									}
								}
								rs.setRetVal(existPropNames);

							} catch (Exception ex) {
								ex.printStackTrace();
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								return;
							}
						}

					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	
	/**
	 * 查询未审核的单据
	 * 
	 * @param conn
	 *            Connection
	 * @return List
	 */
	public Result queryNotAuditBill(final String scompanyID,final String locale) {
		final AccPeriodBean bean=BaseEnv.accPerios.get(scompanyID);
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement cs = conn.createStatement();
						try {
							//查询数据库中有哪些单据
							String sql = "select tableName,b."+locale
									+ " from tblDBTableInfo a,tblLanguage b,OAWorkFlowTemplate c where sysParameter='CurrentAccBefBillAndUnUseBeforeStart' and tableType=0 "
									+ " and isView=0 and (select count(0) from tblDBFieldInfo b where a.id=b.tableId and fieldName='billDate')>0 " +
									"and a.languageId=b.id and a.tableName=c.templateFile and templateStatus=1";
							ResultSet rss = cs.executeQuery(sql);
							ArrayList bills = new ArrayList();
							HashMap map=new HashMap();
							while (rss.next()) {
								bills.add(new String[]{rss.getString(1),rss.getString(2)});
								map.put(rss.getString(1),rss.getString(2));
							}	
							
							ArrayList billInfos = new ArrayList();
							//查询这些单据中本期有哪些单没有过账
							for (int i = 0; i < bills.size(); i++) {
								String []bill=(String[])bills.get(i);
								sql="select count(0) from "+bill[0]+" where workFlowNodeName='notApprove' and SUBSTRING(billDate,1,4)="+bean.getAccYear()+" and SUBSTRING(billDate,6,2)="+bean.getAccMonth();
								//BaseEnv.log.debug(sql);
								rss = cs.executeQuery(sql);
								double count=0;
								if(rss.next()){
									count=rss.getDouble(1);
								}
								if(count>0&&!billInfos.contains(map.get(bill[0]))){
									billInfos.add(map.get(bill[0]));
								}
							}
							
							rs.setRealTotal(billInfos.size());
							rs.setRetVal(billInfos);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 查询未产生凭证的单据
	 * 
	 * @param conn
	 *            Connection
	 * @return List
	 */
	public Result queryNotCreateAcc(final int accPeriod,final int accYear) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "{call queryNotCreateAcc(@period=?,@periodYear=?)}";
							PreparedStatement st= conn.prepareCall(sql);
							st.setInt(1, accPeriod);
							st.setInt(2, accYear);
							ResultSet rss = st.executeQuery();
							String billTypes="";
							while(rss.next()){
								billTypes+=rss.getString(1)+",";
							}
							if(billTypes.length()>0){
								billTypes=billTypes.substring(0,billTypes.length()-1);
								rs.setRealTotal(1);
							}
							rs.setRetVal(billTypes);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 查询库存中是否存在负数
	 * 
	 * @param conn
	 *            Connection
	 * @return List
	 */
	public Result queryNegative(final String scompanyID) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement cs = conn.createStatement();
						try {
							//查询数据库中有哪些单据
							String sql = "select count(0) from tblStocks where lastQty<0 and SCompanyID='"+scompanyID+"'";
							ResultSet rss = cs.executeQuery(sql);

							if (rss.next()&&rss.getInt(1)>0) {
								rs.setRetVal("Negative");
							}	
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 查询本期草稿单据
	 * 
	 * @param conn
	 *            Connection
	 * @return List
	 */
	public Result queryDraftBill(String scompanyID,final String locale) {
		final AccPeriodBean bean=BaseEnv.accPerios.get(scompanyID);
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement cs = conn.createStatement();
						try {
							// 查询数据库中有哪些单据
							String sql = "select tableName,b."+locale
									+ " from tblDBTableInfo a,tblLanguage b where sysParameter='CurrentAccBefBillAndUnUseBeforeStart' and tableType=0 and draftFlag=1"
									+ " and isView=0 and (select count(0) from tblDBFieldInfo b where a.id=b.tableId and fieldName='billDate')>0 " +
									"and a.languageId=b.id";
							ResultSet rss = cs.executeQuery(sql);
							ArrayList bills = new ArrayList();
							while (rss.next()) {
								bills.add(new String[]{rss.getString(1),rss.getString(2)});
							}

							ArrayList billInfos = new ArrayList();
							//查询这些单据中本期有哪些单没有过账
							for (int i = 0; i < bills.size(); i++) {
								sql = "select count(0) from "
										+ ((String[])bills.get(i))[0]
										+ " where workFlowNodeName ='draft' and subString(billDate,1,4)="
										+ bean.getAccYear() + " and subString(billDate,6,2)=" + bean.getAccMonth();

								rss = cs.executeQuery(sql);
								//BaseEnv.log.debug(sql);
								if (rss.next()&&rss.getInt(1)>0) {
									billInfos.add(bills.get(i));
								}
							}

							rs.setRealTotal(billInfos.size());
							rs.setRetVal(billInfos);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}


	private ArrayList<AccPeriodBean> getNextAccPeriodArr(int accYear,
			int accMonth, int accPeriod, String loginId, String createTime,
			String SCompanyID) {
		ArrayList<AccPeriodBean> list = new ArrayList<AccPeriodBean>();
		String[] daysInMonth = { "31", "30", "31", "30", "31", "30", "31",
				"31", "30", "31", "30", "31" };
		AccPeriodBean periodBean = new AccPeriodBean();
		periodBean.setId(IDGenerater.getId());
		periodBean.setAccYear(accYear);
		periodBean.setAccMonth(accMonth + 1);
		periodBean.setAccPeriod(accPeriod + 1);
		periodBean.setCreateBy(loginId);
		periodBean.setLastUpdateBy(loginId);
		periodBean.setCreateTime(createTime);
		periodBean.setLastUpdateTime(createTime);
		periodBean.setStatusId(1);
		periodBean.setIsBegin(2);
		periodBean.setSCompanyID(SCompanyID);
		String daysInMon = daysInMonth[accMonth];
		if (accMonth + 1 == 2) {
			daysInMon = String.valueOf(((accYear % 4 == 0)
					&& (accYear % 100 != 0) || (accYear % 400 == 0)) ? 29 : 28);
		}
		String periodBegin = "";
		String periodEnd = "";
		if (accMonth >= 9) {
			periodBegin = accYear + "-" + (accMonth + 1) + "-01";
			periodEnd = accYear + "-" + (accMonth + 1) + "-" + daysInMon;
		} else {
			periodBegin = accYear + "-0" + (accMonth + 1) + "-01";
			periodEnd = accYear + "-0" + (accMonth + 1) + "-" + daysInMon;
		}
		periodBean.setPeriodBegin(periodBegin);
		periodBean.setPeriodEnd(periodEnd);
		list.add(periodBean);
		return list;
	}

	/**
	 * 验证期间是否存在
	 * 
	 * @param accYear
	 * @param accPeriod
	 * @param connection
	 * @return
	 */
	public int periodIsExist(int accYear, int accPeriod, Connection connection) {
		Statement stmt;
		int res = -1;
		try {
			stmt = connection.createStatement();
			String sql = "select count(*) from tblAccperiod where AccYear="
					+ accYear + " and AccMonth=" + accPeriod
					+ " and AccPeriod=" + accPeriod;
			ResultSet rss = stmt.executeQuery(sql);
			if (rss.next()) {
				res = rss.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 得到下个会计期间年期间
	 * 
	 * @param nextYear
	 * @param loginId
	 * @param createTime
	 * @param SCompanyID
	 * @return
	 */
	private ArrayList<AccPeriodBean> getNextAccPeriodArr(int nextYear,
			String loginId, String createTime, String SCompanyID, boolean flag,
			Connection conn) {
		ArrayList<AccPeriodBean> list = new ArrayList<AccPeriodBean>();
		String[] daysInMonth = { "31", "30", "31", "30", "31", "30", "31",
				"31", "30", "31", "30", "31" };
		PublicMgt pubmgt = new PublicMgt();
		for (int i = 0; i < 12; i++) {
			if (flag) {
				int res = periodIsExist(nextYear, i + 1, conn);
				if (res > 0) {
					continue;
				}
			}
			AccPeriodBean periodBean = new AccPeriodBean();
			periodBean.setId(IDGenerater.getId());
			periodBean.setAccYear(nextYear);
			periodBean.setAccMonth(i + 1);
			periodBean.setAccPeriod(i + 1);
			periodBean.setCreateBy(loginId);
			periodBean.setLastUpdateBy(loginId);
			periodBean.setCreateTime(createTime);
			periodBean.setLastUpdateTime(createTime);
			if (i == 0) {
				periodBean.setStatusId(1);
			} else {
				periodBean.setStatusId(0);
			}
			periodBean.setIsBegin(2);
			periodBean.setSCompanyID(SCompanyID);
			String daysInMon = daysInMonth[i];
			if (i == 1) {
				daysInMon = String.valueOf(((nextYear % 4 == 0)
						&& (nextYear % 100 != 0) || (nextYear % 400 == 0)) ? 29
						: 28);
			}
			String periodBegin = "";
			String periodEnd = "";
			if (i >= 9) {
				periodBegin = nextYear + "-" + (i + 1) + "-01";
				periodEnd = nextYear + "-" + (i + 1) + "-" + daysInMon;
			} else {
				periodBegin = nextYear + "-0" + (i + 1) + "-01";
				periodEnd = nextYear + "-0" + (i + 1) + "-" + daysInMon;
			}
			periodBean.setPeriodBegin(periodBegin);
			periodBean.setPeriodEnd(periodEnd);
			list.add(periodBean);
		}
		return list;
	}

	private Result addNextPriodArr(ArrayList<AccPeriodBean> list,
			Session session) {
		Result rs = new Result();
		try {
			for (AccPeriodBean periodBean : list) {
				this.addBean(periodBean, session);
			}
		} catch (Exception e) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rs;
		}
		return rs;
	}

	// 如果月结到下一年，则查询出下一年那些期间没有复制
	public Result getCopyPeriod(Session sess, final int accYear) {
//		Result rs = this.list(
//				"select AccYear,AccMonth,AccPeriod from AccPeriodBean where AccYear="
//						+ accYear + " and AccOverYear=0 ", new ArrayList(),
//				sess);
		
		/**************
		 * 周新宇修改 2011-1-10
		 * 不明白这段代码是什么意思，但这段代码会引起12份月结后，下一期间自动变为第二年的2月份，很多客户年结后期间变为2月了
		 * 这里加了个order by 不知道会不会引起连锁问题，待评审。
		 *******************/
		Result rs = this.list(
				"select AccYear,AccMonth,AccPeriod from AccPeriodBean where AccYear="
						+ accYear + " and AccOverYear=0 order by AccYear,AccMonth", new ArrayList(),
				sess);

		return rs;
	}

	public int[] getNextPriod(int accYear, int accMonth, int accPeriod,
			String loginId, String longDate, String sunCompany,
			int next_Period, int next_Year, int next_Month, Session session,
			Connection connection, boolean canAdd) {
		int[] obj = new int[3];
		int nextPeriod = next_Period;
		int nextYear = next_Year;
		int nextMonth = next_Month;
		if (nextYear == 0) {// 月结自动复制上一年会计期间
			if (accMonth != 12) {
				if (canAdd) {
					ArrayList<AccPeriodBean> nextPeriodList = getNextAccPeriodArr(
							accYear, accMonth, accPeriod, loginId, longDate,
							sunCompany);
					Result rs1 = addNextPriodArr(nextPeriodList, session);
					if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						nextYear = accYear;
						nextMonth = accMonth + 1;
						nextPeriod = accPeriod + 1;
					}
				} else {
					nextYear = accYear;
					nextMonth = accMonth + 1;
					nextPeriod = accPeriod + 1;
				}
			} else {
				if (canAdd) {
					ArrayList<AccPeriodBean> nextPeriodList = getNextAccPeriodArr(
							accYear + 1, loginId, longDate, sunCompany, false,
							connection);
					Result rs1 = addNextPriodArr(nextPeriodList, session);
					if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						nextYear = accYear + 1;
						nextMonth = 1;
						nextPeriod = 1;
					}
				} else {
					nextYear = accYear + 1;
					nextMonth = 1;
					nextPeriod = 1;
				}
			}
		} else {
			if (accMonth == 12) {
				if (canAdd) {
					if (accYear + 1 != next_Year) {
						ArrayList<AccPeriodBean> nextPeriodList = getNextAccPeriodArr(
								accYear + 1, loginId, longDate, sunCompany,
								false, connection);
						Result rs1 = addNextPriodArr(nextPeriodList, session);
						if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							nextYear = accYear + 1;
							nextMonth = 1;
							nextPeriod = 1;
						}
					} else {
						ArrayList<AccPeriodBean> nextPeriodList = getNextAccPeriodArr(
								accYear + 1, loginId, longDate, sunCompany,
								true, connection);
						Result rs1 = addNextPriodArr(nextPeriodList, session);
						if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							nextYear = accYear + 1;
							nextMonth = 1;
							nextPeriod = 1;
						}
					}
				} else {
					nextYear = accYear + 1;
					nextMonth = 1;
					nextPeriod = 1;
				}
			}
		}
		obj[0] = nextYear;
		obj[1] = nextMonth;
		obj[2] = nextPeriod;
		return obj;
	}
	
	public int[] getStockNextPriod(int accYear, int accMonth, int accPeriod,
			String loginId, String longDate, String sunCompany,
			int next_Period, int next_Year, int next_Month, Session session,
			Connection connection, boolean canAdd,String stockCode) {
		int[] obj = new int[3];
		int nextPeriod = next_Period;
		int nextYear = next_Year;
		int nextMonth = next_Month;
		if (nextYear == 0) {// 月结自动复制上一年会计期间
			if (accMonth != 12) {
				if (canAdd) {
					ArrayList<AccPeriodBean> nextPeriodList = getNextAccPeriodArr(
							accYear, accMonth, accPeriod, loginId, longDate,
							sunCompany);
					Result rs1 = addNextPriodArr(nextPeriodList, session);
					if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						nextYear = accYear;
						nextMonth = accMonth + 1;
						nextPeriod = accPeriod + 1;
					}
				} else {
					nextYear = accYear;
					nextMonth = accMonth + 1;
					nextPeriod = accPeriod + 1;
				}
			} else {
				if (canAdd) {
					ArrayList<AccPeriodBean> nextPeriodList = getNextAccPeriodArr(
							accYear + 1, loginId, longDate, sunCompany, false,
							connection);
					Result rs1 = addNextPriodArr(nextPeriodList, session);
					if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						nextYear = accYear + 1;
						nextMonth = 1;
						nextPeriod = 1;
					}
				} else {
					nextYear = accYear + 1;
					nextMonth = 1;
					nextPeriod = 1;
				}
			}
		} else {
			if (accMonth == 12) {
				if (canAdd) {
					if (accYear + 1 != next_Year) {
						ArrayList<AccPeriodBean> nextPeriodList = getNextAccPeriodArr(
								accYear + 1, loginId, longDate, sunCompany,
								false, connection);
						Result rs1 = addNextPriodArr(nextPeriodList, session);
						if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							nextYear = accYear + 1;
							nextMonth = 1;
							nextPeriod = 1;
						}
					} else {
						ArrayList<AccPeriodBean> nextPeriodList = getNextAccPeriodArr(
								accYear + 1, loginId, longDate, sunCompany,
								true, connection);
						Result rs1 = addNextPriodArr(nextPeriodList, session);
						if (rs1.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							nextYear = accYear + 1;
							nextMonth = 1;
							nextPeriod = 1;
						}
					}
				} else {
					nextYear = accYear + 1;
					nextMonth = 1;
					nextPeriod = 1;
				}
			}
		}
		obj[0] = nextYear;
		obj[1] = nextMonth;
		obj[2] = nextPeriod;
		return obj;
	}

	public ArrayList getFirstDeptList(Connection conn) throws Exception{
		ArrayList list=new ArrayList();
		if("true".equals(BaseEnv.systemSet.get("openDeptAcc").getSetting())){
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery("select classCode from tblDepartment where LEN(classCode)=5");
			
			while(rs.next()){
				list.add(rs.getString(1));
			}
		}else{
			list.add("");
		}
		return list;
	}
	public Object[] getStocksProp(Hashtable map) {
		Result rss = null;
		IniGoodsMgt iniGoodsMgt = new IniGoodsMgt();
		GoodsPropMgt propMgt = new GoodsPropMgt();
		List existStocksProp = new ArrayList();
		List existStocksPropNames = new ArrayList();
		List fieldsName = iniGoodsMgt.queryStocksFieldsNameBySys("tblStocks",
				map);

		for (int i = 0; i < fieldsName.size(); i++) {
			rss = propMgt.queryPropName(fieldsName.get(i).toString());
			if (rss.getRetVal() != null
					&& ((ArrayList) rss.getRetVal()).size() > 0) {
				String name = fieldsName.get(i).toString();
				GoodsPropInfoBean bean = (GoodsPropInfoBean) ((ArrayList) rss
						.getRetVal()).get(0);

				String[] prop = new String[3];
				prop[0] = name;
				prop[1] = String.valueOf(bean.getIsUsed());
				prop[2] = String.valueOf(bean.getIsCalculate());
				existStocksProp.add(prop);
				existStocksPropNames.add(name);
			}
		}
		Object[] obj = new Object[2];
		obj[0] = existStocksProp;
		obj[1] = existStocksPropNames;
		return obj;
	}

	/**
	 * 查询本月是否存在没有进行计提折旧的资产
	 * @param Period
	 * @param PeriodYear
	 * @param PeriodMonth
	 * @param sunCompany
	 * @return
	 */
	public Result queryNoDepreAsset(final int Period,final int PeriodYear,final int PeriodMonth,final String sunCompany) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select count(0) from tblFixedAssetDepreciate where statusId=0 and Period="+Period+
										" and PeriodYear="+PeriodYear+" and PeriodMonth="+PeriodMonth+" and SCompanyID='"+ sunCompany + "' ";
							ResultSet rst=cs.executeQuery(sql);
							if (rst.next()) {
								int count=rst.getInt(1);
								
								if(count>0){
									rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 查询本月是否存在已经计提折旧的资产
	 * @param Period
	 * @param PeriodYear
	 * @param PeriodMonth
	 * @param sunCompany
	 * @return
	 */
	public Result queryDepreAsset(final int Period,final int PeriodYear,final int PeriodMonth,final String sunCompany) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select count(0) from tblFixedAssetDepreciate where statusId=1 and Period="+Period+
										" and PeriodYear="+PeriodYear+" and PeriodMonth="+PeriodMonth+" and SCompanyID='"+ sunCompany + "' ";
							ResultSet rst=cs.executeQuery(sql);
							if (rst.next()) {
								int count=rst.getInt(1);
								
								if(count>0){
									rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result getCurrPeriod(final String sunCompany) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select AccPeriod,AccMonth,AccYear from tblAccPeriod where statusId=1 and SCompanyID='"
									+ sunCompany + "' ";
							ResultSet rss = cs.executeQuery(sql);
							AccPeriodBean bean = new AccPeriodBean();
							bean.setAccMonth(-1);
							bean.setAccPeriod(-1);
							bean.setAccYear(-1);
							if (rss.next()) {
								bean.setAccPeriod(rss.getInt(1));
								bean.setAccMonth(rss.getInt(2));
								bean.setAccYear(rss.getInt(3));
							}
							rs.setRetVal(bean);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}
	public Result getCurrAccPeriod(final String sunCompany) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select AccPeriod,AccMonth,AccYear from tblAccPeriod where AccStatusId=1 and SCompanyID='"
									+ sunCompany + "' ";
							ResultSet rss = cs.executeQuery(sql);
							AccPeriodBean bean = new AccPeriodBean();
							bean.setAccMonth(-1);
							bean.setAccPeriod(-1);
							bean.setAccYear(-1);
							if (rss.next()) {
								bean.setAccPeriod(rss.getInt(1));
								bean.setAccMonth(rss.getInt(2));
								bean.setAccYear(rss.getInt(3));
							}
							rs.setRetVal(bean);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}
	public Result getLastYear(final String sunCompany) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select count(*) from tblAccPeriod where AccYear<(select AccYear from tblAccPeriod where statusId=1 and SCompanyID='"
									+ sunCompany
									+ "') and SCompanyID='"
									+ sunCompany + "' ";
							ResultSet rss = cs.executeQuery(sql);
							if (rss.next()) {
								if (rss.getInt(1) > 0) {
									rs
											.setRetCode(ErrorCanst.RET_NOTSETTLE_LASTNOTYEAR);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	public Result checkLastPeriod(final String sunCompany) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select count(*) from tblAccPeriod a,(select AccPeriod,AccMonth,AccYear from tblAccPeriod where statusId=1 and SCompanyID='"
									+ sunCompany
									+ "') b"
									+ " where a.AccYear=b.AccYear and a.AccPeriod>b.AccPeriod";
							ResultSet rss = cs.executeQuery(sql);
							if (rss.next()) {
								if (rss.getInt(1) == 0) {
									rs.setRetVal("true");
								} else {
									rs.setRetVal("false");
								}
							} else {
								rs.setRetVal("false");
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	public Result getNextPeriod(final String sunCompany, final int accYear,
			final int accPeriod) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select top 1 AccPeriod,AccMonth,AccYear from tblAccPeriod where SCompanyID='"
									+ sunCompany
									+ "' and ((accYear="
									+ accYear
									+ " and accPeriod>"
									+ accPeriod
									+ ") or accYear>"
									+ accYear
									+ ") order by accYear,accPeriod";
							ResultSet rss = cs.executeQuery(sql);
							AccPeriodBean bean = new AccPeriodBean();
							bean.setAccMonth(0);
							bean.setAccPeriod(0);
							bean.setAccYear(0);
							if (rss.next()) {
								bean.setAccPeriod(rss.getInt(1));
								bean.setAccMonth(rss.getInt(2));
								bean.setAccYear(rss.getInt(3));
							}
							rs.setRetVal(bean);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}
	
	public Result getNegativeLastQty(final String sunCompany, final int accYear,
			final int accPeriod) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//如果结存数量计算错误，先更新这些结存数量
							String CostingMethod=BaseEnv.systemSet.get("GoodsCostingMethod").getSetting();
							if(("MWAM".equals(CostingMethod)||"MONTH".equals(CostingMethod))){
								//这个存储过程会判断结存数量或结存金额不正确的所有hash,再从头至尾，去重新更新一遍单价和金额，导致已月结有出入库记录也跟着被刷一次，正确的变错误的。
								//可能这个存储过程根本是不必要的，暂时不再执行---zxy 2017.6.13
//								CallableStatement cst = conn.prepareCall("{call proc_transErrorData(?,?)}");
//								cst.setInt(1,accPeriod);
//								cst.setInt(2,accYear);
//								cst.execute();
							}
							Statement cs = conn.createStatement();
							String sql="select c.StockFullName,b.goodsNumber,b.goodsFullName,b.goodsSpec,a.Seq,a.BatchNo,a.Inch,Hue,yearNO,a.ProDate,a.Availably,a.totalQty from tblStockDet a  join tblGoods b on a.goodsCode=b.classCode join tblStock c on "+
							" a.StockCode=c.classCode "+
							"where a.id=(select top 1 id from tblStockDet k where k.goodPropHash=a.goodPropHash and ((k.Period<="+ accPeriod+ " and k.PeriodYear="+
							accYear+ ") or k.PeriodYear<" + accYear+") order by k.BillDate desc,k.CreateTime desc,k.ItemOrder desc) and a.TotalQty<0 ";
							BaseEnv.log.debug("检查结存数量<0的数 sql="+sql);
							ResultSet rss = cs.executeQuery(sql);
							ArrayList list=new ArrayList();
							while(rss.next()){
								HashMap map=new HashMap();
								map.put("StockFullName", rss.getString("StockFullName"));
								map.put("goodsNumber", rss.getString("goodsNumber"));
								map.put("goodsFullName", rss.getString("goodsFullName"));
								map.put("goodsSpec", rss.getString("goodsSpec"));
								map.put("Seq", rss.getString("Seq"));
								map.put("BatchNo", rss.getString("BatchNo"));
								map.put("Inch", rss.getString("Inch"));
								map.put("Hue", rss.getString("Hue"));
								map.put("yearNO", rss.getString("yearNO"));
								map.put("ProDate", rss.getString("ProDate"));
								map.put("Availably", rss.getString("Availably"));
								map.put("totalQty", rss.getString("totalQty"));
								list.add(map);
							}

							if(list.size()>0){//查询月结期间内有最后结存是负数的数据，给出错误提示
								rs.setRetVal(list);
								rs.setRetCode(ErrorCanst.SETTLE_LASTQTYNEGATIVE_ERROR);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 查询软件版本号，用以控制属性字段拼接
	 * 
	 * @return
	 */
	public Result queryVersionNum() {
		final Result rs = new Result();
		/**
		 * 查询软件版本号，用以控制属性字段拼接
		 */
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select isnull(ID,0) from tbltradeinfo";
						Statement cs = conn.createStatement();
						ResultSet rss = cs.executeQuery(sql);
						int id = 0;
						try {
							if (rss.next()) {
								id = rss.getInt(1);
							}
							rs.setRetVal(id);
						} catch (Exception ex) {
							BaseEnv.log
									.error(
											"SysAccMgt.reCalcucateData method:[select isnull(ID,0) from tbltradeinfo] error",
											ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	// 修改往来汇总表的当前期间年累计
	private void updateYCompanyTotal(Statement cs, int currYear,
			int currPeriod, String sunCompany) throws SQLException {
		// 得到开帐会计期间
		String sql = "select top 1 AccPeriod,AccYear from tblAccPeriod where IsBegin=1 and SCompanyID='"
				+ sunCompany + "'";
		ResultSet rss = cs.executeQuery(sql);
		int beginPeriod = 0;
		int beginYear = 0;
		if (rss.next()) {
			beginPeriod = rss.getInt("AccPeriod");
			beginYear = rss.getInt("AccYear");
		}

		double[] totalToCurrAcc = new double[4];
		sql = "select sum(ReceiveTotalDebit)-sum(ReceiveTotalLend) as totalReceive,sum(PayTotalDebit)-sum(PayTotalLend) as totalPay"
				+ ",sum(PreReceiveTotalDebit)-sum(PreReceiveTotalLend) as totalPreReceive,sum(PrePayTotalDebit)-sum(PrePayTotalLend) as totalPrePay"
				+ " from tblCompanyTotal where PeriodYear>="
				+ beginYear
				+ " and Period>="
				+ beginPeriod
				+ " and PeriodYear<="
				+ currYear
				+ " and Period<="
				+ currPeriod
				+ " and SCompanyID='"
				+ sunCompany + "'";
		rss = cs.executeQuery(sql);
		if (rss.next()) {
			totalToCurrAcc[0] = rss.getDouble("totalReceive");
			totalToCurrAcc[1] = rss.getDouble("totalPay");
			totalToCurrAcc[2] = rss.getDouble("totalPreReceive");
			totalToCurrAcc[3] = rss.getDouble("totalPrePay");
		}

		sql = "update tblCompanyTotal set "
				+ (totalToCurrAcc[0] >= 0 ? "YReceiveTotalDebit"
						: "YReceiveTotalLend")
				+ "="
				+ Math.abs(totalToCurrAcc[0])
				+ ","
				+ (totalToCurrAcc[1] >= 0 ? "YPayTotalDebit" : "YPayTotalLend")
				+ "="
				+ Math.abs(totalToCurrAcc[1])
				+ ","
				+ (totalToCurrAcc[2] >= 0 ? "YPreReceiveTotalDebit"
						: "YPreReceiveTotalLend")
				+ "="
				+ Math.abs(totalToCurrAcc[2])
				+ ","
				+ (totalToCurrAcc[3] >= 0 ? "YPrePayTotalDebit"
						: "YPrePayTotalLend") + "="
				+ Math.abs(totalToCurrAcc[3]) + " where PeriodYear=" + currYear
				+ " and Period=" + currPeriod + " and SCompanyID='"
				+ sunCompany + "'";
		cs.execute(sql);
	}

	/**
	 * 结转损益
	 * 
	 * @param conn
	 *            Connection
	 * @param accPeriod
	 *            int
	 * @param accMonth
	 *            int
	 * @param accYear
	 *            int
	 * @param loginId
	 *            String
	 * @param departCode
	 *            String
	 * @throws Exception
	 */
	private void settleProfitLoss(Connection conn, int accPeriod, int accMonth,
			int accYear, String loginId, String departCode, String sunCompany,String profitLossLocal,String settleLocal,String accPass)
			throws Exception {
		SettleCostMgt costMgt=new SettleCostMgt(null,null,profitLossLocal,settleLocal);
		String openDeptAcc=BaseEnv.systemSet.get("openDeptAcc")==null?"false":BaseEnv.systemSet.get("openDeptAcc").getSetting();
		boolean falg = false;
		Statement cs = conn.createStatement();
		ResultSet rs = null;
		String sql = "";
		AccMainSettingBean settingBean = null;
		Result result = new VoucherMgt().queryVoucherSetting(conn);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			settingBean = (AccMainSettingBean)result.retVal;
		}
		int IsAuditing=settingBean.getIsAuditing();
		if(IsAuditing!=1||"true".equals(accPass)){
			falg = true;
		}
		ArrayList deptList=new ArrayList();
		//查询科目余额表中的所有部门
		if("true".equals(openDeptAcc)){
			sql="select isnull(DepartmentCode,'') from tblAccBalance where len(isnull(DepartmentCode,''))>0 group by isnull(DepartmentCode,'')";
			rs=cs.executeQuery(sql);
			while(rs.next()){
				deptList.add(rs.getString(1));
			}
		}else{
			deptList.add("");
		}
		
		for(int k=0;k<deptList.size();k++){
			String deptCode=deptList.get(k).toString();
			//zxy修改结转损益从凭证明细取数
			sql = "select b.jdFlag jdFlag,a.AccCode SubCode,sum(DebitCurrencyAmount) PeriodDebitSum,sum(LendCurrencyAmount) PeriodCreditSum,0 PeriodBala,sum(DebitAmount) PeriodDebitSumBase,sum(LendAmount) PeriodCreditSumBase,0 PeriodBalaBase "
				+ " from tblAccDetail a,tblAccTypeInfo b,tblAccMain c where a.AccCode=b.AccNumber and a.f_ref=c.id and c.workflowNodeName='finish' and AccCode like '6_%' and PeriodMonth="
				+ accPeriod
				+ " and PeriodYear="
				+ accYear 
				+ " and b.SCompanyID='"
				+ sunCompany
				+ "' and b.isCatalog=0 and b.isCalculateParent=0 " 
				+ (deptCode ==null || deptCode.length() ==0 ?"": " and isnull(a.DepartmentCode,'')='"+deptCode+"' " )
				+ " group by b.jdFlag,a.AccCode order by AccCode";
			
			ResultSet rss = cs.executeQuery(sql);
			ArrayList profitLossAcc = new ArrayList();
	
			while (rss.next()) {
				AccBalanceBean accBean = new AccBalanceBean();
				accBean.setJdFlag(rss.getInt("jdFlag"));
				accBean.setSubCode(rss.getString("SubCode"));
				accBean.setPeriodDebitSum(rss.getString("PeriodDebitSum"));
				accBean.setPeriodCreditSum(rss.getString("PeriodCreditSum"));
				accBean.setPeriodBala(rss.getString("PeriodBala"));
				accBean.setPeriodDebitSumBase(rss.getString("PeriodDebitSumBase"));
				accBean.setPeriodCreditSumBase(rss.getString("PeriodCreditSumBase"));
				accBean.setPeriodBalaBase(rss.getString("PeriodBalaBase"));
				profitLossAcc.add(accBean);
			}
	
			String mainId = IDGenerater.getId();
			boolean mainFlag = false;
	
			String sumDebit = "0"; // 损益类所有借方发生额
			String sumDebitCurr = "0"; // 损益类所有外币借方发生额
			String sumCredit = "0"; // 损益类所有贷方发生额
			String sumCreditCurr = "0"; // 损益类所有外币贷方发生额
			
			for (int i = 0; i < profitLossAcc.size(); i++) {
				AccBalanceBean accBean = (AccBalanceBean) profitLossAcc.get(i);
				String debit = "0"; // 此科目借方发生额
				String debitCurr = "0"; // 此科目外币借方发生额
				String credit = "0"; // 此科目贷方发生额
				String creditCurr = "0"; // 此科目外币贷方发生额
				String bala = "0"; // 本币余额
				String balaCurr = "0";// 外币余额
	
				debit = accBean.getPeriodDebitSumBase()==null?"0":accBean.getPeriodDebitSumBase();
				debitCurr = accBean.getPeriodDebitSum()==null?"0":accBean.getPeriodDebitSum();
				credit =accBean.getPeriodCreditSumBase()==null?"0":accBean.getPeriodCreditSumBase();
				creditCurr =accBean.getPeriodCreditSum()==null?"0":accBean.getPeriodCreditSum();
				
				if (accBean.getJdFlag() == 1) {
					bala =String.valueOf(new BigDecimal(debit).subtract(new BigDecimal(credit))
							.doubleValue());
					balaCurr =String.valueOf(new BigDecimal(debitCurr).subtract(
							new BigDecimal(creditCurr)).doubleValue());
				} else if (accBean.getJdFlag() == 2) {
					bala =String.valueOf(new BigDecimal(credit).subtract(new BigDecimal(debit))
							.doubleValue());
					balaCurr =String.valueOf(new BigDecimal(creditCurr).subtract(
							new BigDecimal(debitCurr)).doubleValue());
				}
	
				if (accBean.getJdFlag() == 1) {
					sumDebit =String.valueOf(new BigDecimal(sumDebit).add(new BigDecimal(bala))
							.doubleValue());
					sumDebitCurr =String.valueOf(new BigDecimal(sumDebitCurr).add(
							new BigDecimal(balaCurr)).doubleValue());
				} else if (accBean.getJdFlag() == 2) {
					sumCredit =String.valueOf(new BigDecimal(sumCredit).add(new BigDecimal(bala))
							.doubleValue());
					sumCreditCurr =String.valueOf(new BigDecimal(sumCreditCurr).add(
							new BigDecimal(balaCurr)).doubleValue());
				}
				
	
				// 生成损益类科目，根据科目余额方向得到借贷方金额
				if (new BigDecimal(debit).subtract(new BigDecimal(credit)).doubleValue()!= 0 || new BigDecimal(debitCurr).subtract(new BigDecimal(debitCurr)).doubleValue() != 0) {
					mainFlag = true;
					if (accBean.getJdFlag() == 1) {
						if (Double.parseDouble(bala) > 0 || Double.parseDouble(balaCurr) > 0) {
							costMgt.insertAccDetail(cs, accYear, accMonth, accBean
									.getSubCode(), 0, 0, Double.parseDouble(bala),0, loginId,
									mainId, "ProfitLossCarryForward", "",
									sunCompany, deptCode.length()>0?deptCode:departCode);
							
						} else {// 如果小于0，取绝对值记到相反的方向
							costMgt.insertAccDetail(cs, accYear, accMonth, accBean
									.getSubCode(), Math.abs(Double.parseDouble(bala)), 0, 0, 0, loginId, mainId,
									"ProfitLossCarryForward", "", sunCompany, deptCode.length()>0?deptCode:departCode);							
						}
					} else {
						if (Double.parseDouble(bala) > 0 || Double.parseDouble(balaCurr) > 0) {
							costMgt.insertAccDetail(cs, accYear, accMonth, accBean
									.getSubCode(), Double.parseDouble(bala), 0, 0, 0, loginId,
									mainId, "ProfitLossCarryForward", "",
									sunCompany, deptCode.length()>0?deptCode:departCode);							
						} else {// 如果小于0，取绝对值记到相反的方向
							costMgt.insertAccDetail(cs, accYear, accMonth, accBean
									.getSubCode(), 0, 0, Math.abs(Double.parseDouble(bala)), 0, loginId, mainId,
									"ProfitLossCarryForward", "", sunCompany, deptCode.length()>0?deptCode:departCode);
							
						}
					}
					//这里不调用updateAccBalance 来更新科目余额
					//如果不存在当前部门的当前科目余额 ，则用下面的存储过程创建科目余额数据
					if(BaseEnv.version!=11){//工贸版增加了部门的参数，由于此存储过程在做单中用到，所以脚本不能同步布匹版
						CallableStatement createPeriod = conn.prepareCall("{call proc_createAccBalPeriod(?,?,?,?,?,?,?)}");
						createPeriod.setInt(1, accYear);
						createPeriod.setInt(2, accPeriod);
						createPeriod.setInt(3, accPeriod);
						createPeriod.setString(4, accBean.getSubCode());
						createPeriod.setString(5, sunCompany);
						createPeriod.setString(6, deptCode);
						createPeriod.setString(7, "");
						createPeriod.execute();
					}else{
						CallableStatement createPeriod = conn.prepareCall("{call proc_createAccBalPeriod(?,?,?,?,?)}");
						createPeriod.setInt(1, accYear);
						createPeriod.setInt(2, accPeriod);
						createPeriod.setInt(3, accPeriod);
						createPeriod.setString(4, accBean.getSubCode());
						createPeriod.setString(5, sunCompany);
						createPeriod.execute();
					}
					int digits =Integer.parseInt(BaseEnv.systemSet.get("DigitsAmount").getSetting());
					String preBala = "0";
					String preBalaBase = "0";
					if(accPeriod>1){
						String preSql  = " select CurrYIniDebitSum,CurrYIniDebitSumBase from tblAccBalance  where SubCode='"
						+ accBean.getSubCode()
						+ "' and Period="
						+ (accPeriod-1)
						+ " and Nyear="
						+ accYear
						+ " and SCompanyID='" + sunCompany + "' and isnull(DepartmentCode,'')='"+deptCode+"'";
						Statement st=conn.createStatement();
						ResultSet prerset = st.executeQuery(preSql);	
						if(prerset.next()){
							preBala = prerset.getString(1);
							preBalaBase = prerset.getString(2);
						}
					}
					sql ="update tblAccBalance set CurrYIniDebitSum=round(("
						+ preBala+"+"+balaCurr
						+ "),"+digits+"), CurrYIniCreditSum=round(("
						+ preBala+"+"+balaCurr
						+ "),"+digits+"), CurrYIniBala=0,"
						+ " CurrYIniDebitSumBase=round(("
						+ preBalaBase+"+"+bala
						+ "),"+digits+"), CurrYIniCreditSumBase=round(("
						+ preBalaBase+"+"+bala
						+ "),"+digits+") ,CurrYIniBalaBase=0, PeriodDebitSumBase=round(("
						+ bala
						+ "),"+digits+"), PeriodCreditSumBase=round(("
						+ bala
						+ "),"+digits+"), PeriodDCBalaBase=0, PeriodBalaBase=0,PeriodDebitSum=round(("
						+ balaCurr
						+ "),"+digits+"), PeriodCreditSum=round(("
						+ balaCurr
						+ "),"+digits+"), PeriodDCBala=0, PeriodBala=0  where SubCode='"
						+ accBean.getSubCode()
						+ "' and Period="
						+ accPeriod
						+ " and Nyear="
						+ accYear
						+ " and SCompanyID='" + sunCompany + "' and isnull(DepartmentCode,'')='"+deptCode+"'";
					
					Statement st=conn.createStatement();
					st.execute(sql);	
					
					//更新父级科目余额
					Result rsp = costMgt.updateParentAccBalance(conn, accBean.getSubCode(), accPeriod, accYear);
					if(rsp.retCode != ErrorCanst.DEFAULT_SUCCESS){
						new Exception(rsp.retVal+"");
					}
					
				}
			}
			if (mainFlag) {
				costMgt.insertAccMain(IsAuditing,cs, mainId, loginId, accPeriod, accYear,
						accMonth, deptCode.length()>0?deptCode:departCode, "ProfitLossCarryForward", sunCompany);
				if (new BigDecimal(sumCredit).subtract(new BigDecimal(sumDebit)).doubleValue() < 0) {
					sumDebit =String.valueOf(new BigDecimal(sumDebit).subtract(
							new BigDecimal(sumCredit)).doubleValue());
					sumCredit = "0";
				} else {
					sumCredit =String.valueOf(new BigDecimal(sumCredit).subtract(
							new BigDecimal(sumDebit)).doubleValue());
					sumDebit = "0";
				}
	
				if (new BigDecimal(sumCredit).subtract(new BigDecimal(sumDebit)).doubleValue()< 0) {
					sumDebitCurr =String.valueOf( new BigDecimal(sumDebitCurr).subtract(
							new BigDecimal(sumCreditCurr)).doubleValue());
					sumCreditCurr = "0";
				} else {
					sumCreditCurr =String.valueOf( new BigDecimal(sumCreditCurr).subtract(
							new BigDecimal(sumDebitCurr)).doubleValue());
					sumDebitCurr = "0";
				}
				if (new BigDecimal(sumDebit).subtract(new BigDecimal(sumCredit)).doubleValue()!= 0 ||new BigDecimal(sumDebitCurr).subtract(new BigDecimal(sumCreditCurr)).doubleValue() != 0) {
					costMgt.insertAccDetail(cs, accYear, accMonth, "4103", Double.parseDouble(sumDebit),0, Double.parseDouble(sumCredit), 0, loginId,
							mainId, "ProfitLossCarryForward", "", sunCompany, deptCode.length()>0?deptCode:departCode);
					if(falg){
					costMgt.updateAccBalance(conn,deptCode, "4103", accPeriod,
							accYear, Double.parseDouble(sumDebit), Double.parseDouble(sumCredit), Double.parseDouble(sumDebitCurr),
									Double.parseDouble(sumCreditCurr), 2, sunCompany);
					}
				}
				//不启用标准财务时修改信息
				if(falg){
					sql = "update tblAccMain set workFlowNodeName='finish',workFlowNode='-1',isAuditing='finish',postingUser='"+loginId+"' where id='"+mainId+"'";
					cs.executeUpdate(sql);
				}
			}
			
			//zxy修改-------------------
			
			/*
			
			// 本期所有损益类科目余额信息
			sql = "select b.jdFlag jdFlag,SubCode,PeriodDebitSum,PeriodCreditSum,isnull(PeriodBala,0) PeriodBala,PeriodDebitSumBase,PeriodCreditSumBase,isnull(PeriodBalaBase,0) PeriodBalaBase "
					+ " from tblAccBalance a,tblAccTypeInfo b where a.SubCode=b.AccNumber and SubCode like '6_%' and Period="
					+ accPeriod
					+ " and Nyear="
					+ accYear
					+ " and a.SCompanyID='"
					+ sunCompany
					+ "' and b.SCompanyID='"
					+ sunCompany
					+ "' and b.isCatalog=0 and b.isCalculateParent=0 and isnull(a.DepartmentCode,'')='"+deptCode+"' order by subCode";
	
			ResultSet rss = cs.executeQuery(sql);
			ArrayList profitLossAcc = new ArrayList();
	
			while (rss.next()) {
				AccBalanceBean accBean = new AccBalanceBean();
				accBean.setJdFlag(rss.getInt("jdFlag"));
				accBean.setSubCode(rss.getString("SubCode"));
				accBean.setPeriodDebitSum(rss.getString("PeriodDebitSum"));
				accBean.setPeriodCreditSum(rss.getString("PeriodCreditSum"));
				accBean.setPeriodBala(rss.getString("PeriodBala"));
				accBean.setPeriodDebitSumBase(rss.getString("PeriodDebitSumBase"));
				accBean.setPeriodCreditSumBase(rss.getString("PeriodCreditSumBase"));
				accBean.setPeriodBalaBase(rss.getString("PeriodBalaBase"));
				profitLossAcc.add(accBean);
			}
	
			String mainId = IDGenerater.getId();
			boolean mainFlag = false;
	
			String sumDebit = "0"; // 损益类所有借方发生额
			String sumDebitCurr = "0"; // 损益类所有外币借方发生额
			String sumCredit = "0"; // 损益类所有贷方发生额
			String sumCreditCurr = "0"; // 损益类所有外币贷方发生额
			
			for (int i = 0; i < profitLossAcc.size(); i++) {
				AccBalanceBean accBean = (AccBalanceBean) profitLossAcc.get(i);
				String debit = "0"; // 此科目借方发生额
				String debitCurr = "0"; // 此科目外币借方发生额
				String credit = "0"; // 此科目贷方发生额
				String creditCurr = "0"; // 此科目外币贷方发生额
				String bala = "0"; // 本币余额
				String balaCurr = "0";// 外币余额
	
				debit = accBean.getPeriodDebitSumBase()==null?"0":accBean.getPeriodDebitSumBase();
				debitCurr = accBean.getPeriodDebitSum()==null?"0":accBean.getPeriodDebitSum();
				credit =accBean.getPeriodCreditSumBase()==null?"0":accBean.getPeriodCreditSumBase();
				creditCurr =accBean.getPeriodCreditSum()==null?"0":accBean.getPeriodCreditSum();
				
				if (accBean.getJdFlag() == 1) {
					bala =String.valueOf(new BigDecimal(debit).subtract(new BigDecimal(credit))
							.doubleValue());
					balaCurr =String.valueOf(new BigDecimal(debitCurr).subtract(
							new BigDecimal(creditCurr)).doubleValue());
				} else if (accBean.getJdFlag() == 2) {
					bala =String.valueOf(new BigDecimal(credit).subtract(new BigDecimal(debit))
							.doubleValue());
					balaCurr =String.valueOf(new BigDecimal(creditCurr).subtract(
							new BigDecimal(debitCurr)).doubleValue());
				}
	
				if (accBean.getJdFlag() == 1) {
					sumDebit =String.valueOf(new BigDecimal(sumDebit).add(new BigDecimal(bala))
							.doubleValue());
					sumDebitCurr =String.valueOf(new BigDecimal(sumDebitCurr).add(
							new BigDecimal(balaCurr)).doubleValue());
				} else if (accBean.getJdFlag() == 2) {
					sumCredit =String.valueOf(new BigDecimal(sumCredit).add(new BigDecimal(bala))
							.doubleValue());
					sumCreditCurr =String.valueOf(new BigDecimal(sumCreditCurr).add(
							new BigDecimal(balaCurr)).doubleValue());
				}
				
	
				// 生成损益类科目，根据科目余额方向得到借贷方金额
				if (new BigDecimal(debit).subtract(new BigDecimal(credit)).doubleValue()!= 0 || new BigDecimal(debitCurr).subtract(new BigDecimal(debitCurr)).doubleValue() != 0) {
					mainFlag = true;
					if (accBean.getJdFlag() == 1) {
						if (Double.parseDouble(bala) > 0 || Double.parseDouble(balaCurr) > 0) {
							costMgt.insertAccDetail(cs, accYear, accMonth, accBean
									.getSubCode(), 0, 0, Double.parseDouble(bala),0, loginId,
									mainId, "ProfitLossCarryForward", "",
									sunCompany, deptCode.length()>0?deptCode:departCode);
							if(falg){
								costMgt.updateAccBalance(conn,deptCode, accBean.getSubCode(), accPeriod, accYear, 0, Double.parseDouble(bala), 0,
										Double.parseDouble(balaCurr), accBean.getJdFlag(), sunCompany);
							}
						} else {// 如果小于0，取绝对值记到相反的方向
							costMgt.insertAccDetail(cs, accYear, accMonth, accBean
									.getSubCode(), Math.abs(Double.parseDouble(bala)), 0, 0, 0, loginId, mainId,
									"ProfitLossCarryForward", "", sunCompany, deptCode.length()>0?deptCode:departCode);
							if(falg){
							costMgt.updateAccBalance(conn,deptCode, accBean.getSubCode(), accPeriod, accYear, Math
										.abs(Double.parseDouble(bala)), 0, Math.abs(Double.parseDouble(balaCurr)), 0, accBean
										.getJdFlag(), sunCompany);
							}
						}
					} else {
						if (Double.parseDouble(bala) > 0 || Double.parseDouble(balaCurr) > 0) {
							costMgt.insertAccDetail(cs, accYear, accMonth, accBean
									.getSubCode(), Double.parseDouble(bala), 0, 0, 0, loginId,
									mainId, "ProfitLossCarryForward", "",
									sunCompany, deptCode.length()>0?deptCode:departCode);
							if(falg){
							costMgt.updateAccBalance(conn,deptCode, accBean
									.getSubCode(), accPeriod, accYear, Double.parseDouble(bala), 0,
									Double.parseDouble(balaCurr), 0, accBean.getJdFlag(), sunCompany);
							}
						} else {// 如果小于0，取绝对值记到相反的方向
							costMgt.insertAccDetail(cs, accYear, accMonth, accBean
									.getSubCode(), 0, 0, Math.abs(Double.parseDouble(bala)), 0, loginId, mainId,
									"ProfitLossCarryForward", "", sunCompany, deptCode.length()>0?deptCode:departCode);
							if(falg){
							costMgt.updateAccBalance(conn,deptCode, accBean
									.getSubCode(), accPeriod, accYear, 0, Math
									.abs(Double.parseDouble(bala)), 0, Math.abs(Double.parseDouble(balaCurr)), accBean
									.getJdFlag(), sunCompany);
							}
						}
					}
					
					//更新父级科目余额
					Result rsp = costMgt.updateParentAccBalance(conn, accBean.getSubCode(), accPeriod, accYear);
					if(rsp.retCode != ErrorCanst.DEFAULT_SUCCESS){
						new Exception(rsp.retVal+"");
					}
					
				}
			}
			if (mainFlag) {
				costMgt.insertAccMain(IsAuditing,cs, mainId, loginId, accPeriod, accYear,
						accMonth, deptCode.length()>0?deptCode:departCode, "ProfitLossCarryForward", sunCompany);
				if (new BigDecimal(sumCredit).subtract(new BigDecimal(sumDebit)).doubleValue() < 0) {
					sumDebit =String.valueOf(new BigDecimal(sumDebit).subtract(
							new BigDecimal(sumCredit)).doubleValue());
					sumCredit = "0";
				} else {
					sumCredit =String.valueOf(new BigDecimal(sumCredit).subtract(
							new BigDecimal(sumDebit)).doubleValue());
					sumDebit = "0";
				}
	
				if (new BigDecimal(sumCredit).subtract(new BigDecimal(sumDebit)).doubleValue()< 0) {
					sumDebitCurr =String.valueOf( new BigDecimal(sumDebitCurr).subtract(
							new BigDecimal(sumCreditCurr)).doubleValue());
					sumCreditCurr = "0";
				} else {
					sumCreditCurr =String.valueOf( new BigDecimal(sumCreditCurr).subtract(
							new BigDecimal(sumDebitCurr)).doubleValue());
					sumDebitCurr = "0";
				}
				if (new BigDecimal(sumDebit).subtract(new BigDecimal(sumCredit)).doubleValue()!= 0 ||new BigDecimal(sumDebitCurr).subtract(new BigDecimal(sumCreditCurr)).doubleValue() != 0) {
					costMgt.insertAccDetail(cs, accYear, accMonth, "4103", Double.parseDouble(sumDebit),0, Double.parseDouble(sumCredit), 0, loginId,
							mainId, "ProfitLossCarryForward", "", sunCompany, deptCode.length()>0?deptCode:departCode);
					if(falg){
					costMgt.updateAccBalance(conn,deptCode, "4103", accPeriod,
							accYear, Double.parseDouble(sumDebit), Double.parseDouble(sumCredit), Double.parseDouble(sumDebitCurr),
									Double.parseDouble(sumCreditCurr), 2, sunCompany);
					}
				}
				//不启用标准财务时修改信息
				if(falg){
					sql = "update tblAccMain set workFlowNodeName='finish',workFlowNode='-1',isAuditing='finish',postingUser='"+loginId+"' where id='"+mainId+"'";
					cs.executeUpdate(sql);
				}
			}
			
			*/
		}

	}


	
	/**
	 * 月末调整汇率
	 * 
	 * @param conn
	 *            Connection
	 * 
	 */

	public Result adjustExchange(final String loginId, final String sunCompany,final String adjustExchange,final String settle) {
		final Result rs = new Result();
		// 得到登陆用户的信息
		Result rs2 = new UserMgt().getEmployee(loginId);
		EmployeeBean beanEmp = (EmployeeBean) rs2.getRetVal();
		if (rs2.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs2;
		}
		final String departCode = beanEmp.getDepartmentCode();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							String sql;
							SysAccMgt mgt = new SysAccMgt();
							SettleCostMgt costMgt=new SettleCostMgt(null,null,adjustExchange,settle);
							// 得到当前会计期间
							sql = "select  top 1 AccPeriod ,AccYear,AccMonth from tblAccPeriod where statusId=1 and SCompanyID='"
									+ sunCompany + "' order by AccMonth desc";
							ResultSet rss = cs.executeQuery(sql);
							int accPeriod = 0;
							int accYear = 0;
							int accMonth = 0;
							if (rss.next()) {
								accPeriod = rss.getInt(1);
								accYear = rss.getInt(2);
								accMonth = rss.getInt(3);
							}

							// 如果存在期末调汇凭证，则删除所有期末调汇凭证，重新生成
							sql = "select id from tblAccMain where CredYear="
									+ accYear + " and CredMonth=" + accMonth
									+ " and Period=" + accPeriod
									+ " and SCompanyID='" + sunCompany
									+ "' and RefBillType='adjustExchange'";
							rss = cs.executeQuery(sql);
							List delList = new ArrayList();
							while (rss.next()) {
								delList.add(rss.getString("id"));
								// rs.setRetCode(ErrorCanst.RET_EXISTS_ADJUSTEXCHANGE);
								// return;
							}
							for (int i = 0; i < delList.size(); i++) {
								sql = "delete from tblAccDetail where f_ref ='"
										+ delList.get(i) + "'";
								cs.execute(sql);
								sql = "delete from tblAccMain where id='"
										+ delList.get(i) + "'";
								cs.execute(sql);
							}

							Hashtable<String, SystemSettingBean> systemSet = BaseEnv.systemSet;
							String fixRate = systemSet.get("FixRate")
									.getSetting();
							if (fixRate.equals("true")) {
								sql = "select  AdjustExchange from tblCurrency a left join tblSetExchange b on a.id=b.CurrencyID and period="
										+ accPeriod
										+ " and periodYear="
										+ accYear
										+ "  where IsBaseCurrency=2 and  b.SCompanyID='"
										+ sunCompany + "'";
								rss = cs.executeQuery(sql);
								while (rss.next()) {
									double exchange = rss.getDouble(1);
									if (exchange == 0) {
										rs
												.setRetCode(ErrorCanst.RET_NOTREC_ADJUSTEXCHANGE);
										return; // 如果是固定汇率，所有的币种必须输入期末调整汇率
									}
								}
							}

							String mainId = IDGenerater.getId(); // 凭证主表的ID
							boolean mainFlag = false; // 判断是否有明细生成，如果没有明细不生成凭证主表数据
							double debits = 0; // 所有借方发生额
							double credits = 0; // 所有贷方发生额

							double recs = 0; // 应收总额
							double pays = 0; // 应付总额
							double preRecs = 0; // 预收总额
							double prePays = 0; // 预付总额

							/** *******************************************往来明细进行月末调汇****************************************************************** */
							// 1.此SQL语句用于的到当前会计期间往来单位中使用到的各种币种的调汇后的外币差额
							sql = "select companyCode,"
									+ "sum((se.AdjustExchange-se.RecordExchange)*isnull(FcRecTotalRemain,0)),"
									+ "sum((se.AdjustExchange-se.RecordExchange)*isnull(FcPayTotalRemain,0)),"
									+ "sum((se.AdjustExchange-se.RecordExchange)*isnull(FcPreRecTotalRemain,0)),"
									+ "sum((se.AdjustExchange-se.RecordExchange)*isnull(FcPrePayTotalRemain,0))"
									+ " from tblCompanyIni a ,tblSetExchange se where a.period<="
									+ accPeriod
									+ "  and a.periodYear<="
									+ accYear
									+ " and len(CurrencyTypeID)>0 "
									+ "and a.id=(select top 1(id) from tblCompanyIni b where b.period<="
									+ accPeriod
									+ " and b.periodYear<="
									+ accYear
									+ " and b.companyCode=a.CompanyCode and b.CurrencyTypeID=a.CurrencyTypeID and b.SCompanyID='"
									+ sunCompany
									+ "' order by b.ItemNo desc) "
									+ "and a.CurrencyTypeID=se.CurrencyID and se.period="
									+ accPeriod + " and se.periodYear="
									+ accYear + " and a.SCompanyID='"
									+ sunCompany + "' group by companyCode";

							sql = "select companyCode,"
									+ "sum(isnull(FcRecTotalRemain,0))*"
									+ "isnull((select se.AdjustExchange - se.RecordExchange from tblSetExchange se where se.SCompanyID='"
									+ sunCompany
									+ "' and se.CurrencyID=a.CurrencyTypeID and se.period="
									+ accPeriod
									+ " and se.periodYear="
									+ accYear
									+ "), 0),"
									+ "sum(isnull(FcPayTotalRemain,0))*"
									+ "isnull((select se.AdjustExchange - se.RecordExchange from tblSetExchange se where se.SCompanyID='"
									+ sunCompany
									+ "' and se.CurrencyID=a.CurrencyTypeID and se.period="
									+ accPeriod
									+ " and se.periodYear="
									+ accYear
									+ "), 0),"
									+ "sum(isnull(FcPreRecTotalRemain,0))*"
									+ "isnull((select se.AdjustExchange - se.RecordExchange from tblSetExchange se where se.SCompanyID='"
									+ sunCompany
									+ "' and se.CurrencyID=a.CurrencyTypeID and se.period="
									+ accPeriod
									+ " and se.periodYear="
									+ accYear
									+ "), 0),"
									+ "sum(isnull(FcPrePayTotalRemain,0))*"
									+ "isnull((select se.AdjustExchange - se.RecordExchange from tblSetExchange se where se.SCompanyID='"
									+ sunCompany
									+ "' and se.CurrencyID=a.CurrencyTypeID and se.period="
									+ accPeriod
									+ " and se.periodYear="
									+ accYear
									+ "), 0)"
									+ "from tblCompanyIni a where a.period<="
									+ accPeriod
									+ " and a.periodYear<="
									+ accYear
									+ " and a.SCompanyID='"
									+ sunCompany
									+ "' and len(CurrencyTypeID)>0 and lower(CurrencyTypeID)!='null' group by a.companyCode,a.currencyTypeId";

							ResultSet rs = cs.executeQuery(sql);

							ArrayList list = new ArrayList();
							while (rs.next()) {
								String[] obj = new String[5];
								obj[0] = rs.getString(1);
								double rec = rs.getDouble(2);
								double pay = rs.getDouble(3);
								double preRec = rs.getDouble(4);
								double prePay = rs.getDouble(5);

								recs = new BigDecimal(recs).add(
										new BigDecimal(rec)).doubleValue();
								pays = new BigDecimal(pays).add(
										new BigDecimal(pay)).doubleValue();
								preRecs = new BigDecimal(preRecs).add(
										new BigDecimal(preRec)).doubleValue();
								prePays = new BigDecimal(prePays).add(
										new BigDecimal(prePay)).doubleValue();

								obj[1] = String.valueOf(rec);
								obj[2] = String.valueOf(pay);
								obj[3] = String.valueOf(preRec);
								obj[4] = String.valueOf(prePay);
								list.add(obj);
							}

							// 2.修改往来明细中往来单位本期最后的余额加上外币的差额
							CallableStatement cbs = conn
									.prepareCall("{call proc_updateSuper(?,?,?,?,?,?,?,?)}");
							for (int i = 0; i < list.size(); i++) {
								String[] obj = (String[]) list.get(i);

								String itemNo = "0";
								sql = "select top 1 itemNo from tblCompanyIni where period<="
										+ accPeriod
										+ " and periodYear<="
										+ accYear
										+ "  and companyCode='"
										+ obj[0]
										+ "' and SCompanyID='"
										+ sunCompany + "' order by ItemNo desc";
								rs = cs.executeQuery(sql);
								if (rs.next()) {
									itemNo = rs.getString(1);
								}

								sql = "update tblCompanyIni set ReceiveTotalRemain=ReceiveTotalRemain+("
										+ obj[1]
										+ "), PayTotalRemain=PayTotalRemain+("
										+ obj[2]
										+ ")"
										+ ", PreReceiveTotalRemain=PreReceiveTotalRemain+("
										+ obj[3]
										+ ") ,"
										+ " PrePayTotalRemain=PrePayTotalRemain+("
										+ obj[4]
										+ ") where itemNo>="
										+ itemNo
										+ "  and companyCode='"
										+ obj[0]
										+ "' and SCompanyID='"
										+ sunCompany
										+ "'";
								cs.execute(sql);
								// 更新往来汇总表此往来单位当前会计期间的金额
								sql = "update tblCompanyTotal set ReceiveTotalDebit=ReceiveTotalDebit+"
										+ obj[1]
										+ ",ReceiveTotalRemain=ReceiveTotalRemain+"
										+ obj[1]
										+ " ,PayTotalLend=PayTotalLend+"
										+ obj[2]
										+ ", PayTotalRemain=PayTotalRemain+"
										+ obj[2]
										+ " ,PreReceiveTotalLend=PreReceiveTotalLend+"
										+ obj[3]
										+ ", PreReceiveTotalRemain=PreReceiveTotalRemain+"
										+ obj[3]
										+ ", PrePayTotalDebit=PrePayTotalDebit+"
										+ obj[4]
										+ ", PrePayTotalRemain=PrePayTotalRemain+"
										+ obj[4]
										+ " where companyCode='"
										+ obj[0]
										+ "' and period="
										+ accPeriod
										+ " and periodYear="
										+ accYear
										+ " and SCompanyID='"
										+ sunCompany
										+ "'";
								cs.execute(sql);

								cbs.registerOutParameter(7,
										java.sql.Types.INTEGER);
								cbs.registerOutParameter(8,
										java.sql.Types.VARCHAR);

								String field = "ReceiveBegin=sum(ReceiveBegin)@SPFieldLink:ReceiveTotalDebit=sum(ReceiveTotalDebit)@SPFieldLink:ReceiveTotalLend=sum(ReceiveTotalLend)"
										+ "@SPFieldLink:ReceiveTotalRemain=sum(ReceiveTotalRemain)@SPFieldLink:PayBegin=sum(PayBegin)@SPFieldLink:PayTotalDebit=sum(PayTotalDebit)@SPFieldLink:PayTotalLend=sum(PayTotalLend)"
										+ "@SPFieldLink:PayTotalRemain=sum(PayTotalRemain)@SPFieldLink:PreReceiveBegin=sum(PreReceiveBegin)"
										+ "@SPFieldLink:PreReceiveTotalDebit=sum(PreReceiveTotalDebit)@SPFieldLink:PreReceiveTotalLend=sum(PreReceiveTotalLend)"
										+ "@SPFieldLink:PreReceiveTotalRemain=sum(PreReceiveTotalRemain)@SPFieldLink:PrePayBegin=sum(PrePayBegin)"
										+ "@SPFieldLink:PrePayTotalDebit=sum(PrePayTotalDebit)@SPFieldLink:PrePayTotalLend=sum(PrePayTotalLend)"
										+ "@SPFieldLink:PrePayTotalRemain=sum(PrePayTotalRemain)@SPFieldLink:CurReceiveBegin=sum(CurReceiveBegin)"
										+ "@SPFieldLink:CurReceiveTotalDebit=sum(CurReceiveTotalDebit)@SPFieldLink:CurReceiveTotalLend=sum(CurReceiveTotalLend)"
										+ "@SPFieldLink:CurReceiveTotalRemain=sum(CurReceiveTotalRemain)@SPFieldLink:CurPayBegin=sum(CurPayBegin)@SPFieldLink:CurPayTotalDebit=sum(CurPayTotalDebit)@SPFieldLink:CurPayTotalLend=sum(CurPayTotalLend)"
										+ "@SPFieldLink:CurPayTotalRemain=sum(CurPayTotalRemain)@SPFieldLink:CurPreReceiveBegin=sum(CurPreReceiveBegin)"
										+ "@SPFieldLink:CurPreReceiveTotalDebit=sum(CurPreReceiveTotalDebit)@SPFieldLink:CurPreReceiveTotalLend=sum(CurPreReceiveTotalLend)"
										+ "@SPFieldLink:CurPreReceiveTotalRemain=sum(CurPreReceiveTotalRemain)@SPFieldLink:CurPrePayBegin=sum(CurPrePayBegin)"
										+ "@SPFieldLink:CurPrePayTotalDebit=sum(CurPrePayTotalDebit)@SPFieldLink:CurPrePayTotalLend=sum(CurPrePayTotalLend)"
										+ "@SPFieldLink:CurPrePayTotalRemain=sum(CurPrePayTotalRemain)";
								// 存储过程参数1：修改的表名，参数2：分类的表(没有为null)，参数3：前面两个表关联的条件
								// (没有为null)，参数4：修改依据的条件
								// (没有为null)，参数5：要修改的字段，参数6：要修改的分类代码

								// 修改往来单位汇总中父类
								cbs.setString(1, "tblCompanyTotal");
								cbs.setString(2, "tblCompany");
								cbs
										.setString(3,
												"tblCompanyTotal.CompanyCode=tblCompany.classCode");

								String query = "period=" + accPeriod
										+ " and periodYear=" + accYear
										+ " and tblCompanyTotal.SCompanyID='"
										+ sunCompany + "'";

								cbs.setString(4, query);
								cbs.setString(5, field);
								cbs.setString(6, obj[0]);
								cbs.execute();
							}
							/*
							 * 3.修改科目余额中相应的科目代码（应收1122
							 * ，应付2202，预收2203，预付1123），生成相应凭证
							 */
							double recsdebit = 0;
							double recsCre = 0;

							double paysdebit = 0;
							double payscre = 0;

							double preRecDebit = 0;
							double preRecCre = 0;

							double prePayDebit = 0;
							double prePayCre = 0;

							if (recs < 0) {
								recsCre = Math.abs(recs);
							} else {
								recsdebit = recs;
							}
							if (recs != 0) {
								debits = new BigDecimal(debits).add(
										new BigDecimal(recsdebit))
										.doubleValue();
								credits = new BigDecimal(credits).add(
										new BigDecimal(recsCre)).doubleValue();
								costMgt.updateAccBalanceCurr(conn, cbs, cs, "1122",
										accPeriod, accYear, recsdebit, recsCre,
										0, 0, 1, sunCompany, departCode);
								costMgt.insertAccDetail(cs, accYear, accMonth,
										"1122", recsdebit, 0, recsCre, 0,
										loginId, mainId, "adjustExchange", "",
										sunCompany, departCode);
								mainFlag = true;
							}
							if (pays < 0) {
								paysdebit = Math.abs(pays);
							} else {
								payscre = pays;
							}
							if (pays != 0) {
								debits = new BigDecimal(debits).add(
										new BigDecimal(paysdebit))
										.doubleValue();
								credits = new BigDecimal(credits).add(
										new BigDecimal(payscre)).doubleValue();
								costMgt.updateAccBalanceCurr(conn, cbs, cs, "2202",
										accPeriod, accYear, paysdebit, payscre,
										0, 0, 2, sunCompany, departCode);
								costMgt.insertAccDetail(cs, accYear, accMonth,
										"2202", paysdebit, 0, payscre, 0,
										loginId, mainId, "adjustExchange", "",
										sunCompany, departCode);
								mainFlag = true;
							}

							if (preRecs < 0) {
								preRecDebit = Math.abs(preRecs);
							} else {
								preRecCre = preRecs;
							}
							if (preRecs != 0) {
								debits = new BigDecimal(debits).add(
										new BigDecimal(preRecDebit))
										.doubleValue();
								credits = new BigDecimal(credits).add(
										new BigDecimal(preRecCre))
										.doubleValue();
								costMgt.updateAccBalanceCurr(conn, cbs, cs, "2203",
										accPeriod, accYear, preRecDebit,
										preRecCre, 0, 0, 2, sunCompany, departCode);
								costMgt.insertAccDetail(cs, accYear, accMonth,
										"2203", preRecDebit, 0, preRecCre, 0,
										loginId, mainId, "adjustExchange", "",
										sunCompany, departCode);
								mainFlag = true;
							}

							if (prePays < 0) {
								prePayCre = Math.abs(prePays);
							} else {
								prePayDebit = prePays;
							}
							if (prePays != 0) {
								debits = new BigDecimal(debits).add(
										new BigDecimal(prePayDebit))
										.doubleValue();
								credits = new BigDecimal(credits).add(
										new BigDecimal(prePayCre))
										.doubleValue();
								costMgt.updateAccBalanceCurr(conn, cbs, cs, "1123",
										accPeriod, accYear, prePayDebit,
										prePayCre, 0, 0, 1, sunCompany, departCode);
								costMgt.insertAccDetail(cs, accYear, accMonth,
										"1123", prePayDebit, 0, prePayCre, 0,
										loginId, mainId, "adjustExchange", "",
										sunCompany, departCode);
								mainFlag = true;
							}

							/** *******************************************科目余额表中其他需要月末调汇的科目****************************************************************** */
							list = new ArrayList();
							sql = "select subCode,(AdjustExchange-RecordExchange)*isnull(periodBala,0),b.jdFlag "
									+ " from tblAccBalance a,tblAccTypeInfo b,tblSetExchange c where accNumber=subCode "
									+ " and c.CurrencyID=a.CurType and a.period="
									+ accPeriod
									+ " and a.Nyear="
									+ accYear
									+ " and periodBala!=0"
									+ " and subCode not in('1122','2202','1123','2203') and a.SCompanyID='"
									+ sunCompany
									+ "' and b.SCompanyID='"
									+ sunCompany
									+ "' and c.SCompanyID='"
									+ sunCompany + "'";

							sql = "select subCode,"
									+ "(select c.AdjustExchange-c.RecordExchange from tblSetExchange c where c.CurrencyID=a.CurType"
									+ " and c.SCompanyID='"
									+ sunCompany
									+ "' and c.period="
									+ accPeriod
									+ " and c.periodYear="
									+ accYear
									+ ") *isnull(periodBala,0),"
									+ "(select jdFlag from tblAccTypeInfo b where b.AccNumber=a.subCode and b.SCompanyID='"
									+ sunCompany
									+ "')"
									+ "from tblAccBalance a where a.period="
									+ accPeriod
									+ " and a.Nyear="
									+ accYear
									+ " and a.periodBala!=0 and a.subCode not in('1122','2202','1123','2203') and a.SCompanyID='"
									+ sunCompany
									+ "' and a.CurType!='' and lower(a.CurType)!='null' and a.CurType is not null";

							rs = cs.executeQuery(sql);

							while (rs.next()) {
								String[] obj = new String[3];
								obj[0] = rs.getString(1);
								obj[1] = String.valueOf(rs.getDouble(2));
								obj[2] = String.valueOf(rs.getString(3));
								list.add(obj);
							}

							for (int i = 0; i < list.size(); i++) {
								String[] obj = (String[]) list.get(i);
								String accNumber = obj[0];
								double marg = Double.parseDouble(obj[1]);
								int jdFlag = Integer.parseInt(obj[2]);

								double debit = 0;
								double credit = 0;

								if (jdFlag == 1) {// 如果是借方科目,增加记借，减少记贷
									if (marg < 0) {
										credit = Math.abs(marg);
									} else {
										debit = marg;
									}
								} else {// 如果是贷方科目，增加记贷，减少记借
									if (marg < 0) {
										debit = Math.abs(marg);
									} else {
										credit = marg;
									}
								}

								debits = new BigDecimal(debits).add(
										new BigDecimal(debit)).doubleValue();
								credits = new BigDecimal(credits).add(
										new BigDecimal(credit)).doubleValue();

								// 修改科目余额
								costMgt.updateAccBalanceCurr(conn, cbs, cs,
										accNumber, accPeriod, accYear, debit,
										credit, 0, 0, jdFlag, sunCompany, departCode);
								// 生成凭证明细
								if (!(debit == 0 && credit == 0)) {
									costMgt.insertAccDetail(cs, accYear, accMonth,
											accNumber, debit, 0, credit, 0,
											loginId, mainId, "adjustExchange",
											"", sunCompany, departCode);
									mainFlag = true;
								}
							}
							AccMainSettingBean settingBean = null;
							Result result = new VoucherMgt().queryVoucherSetting(conn);
							if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
								settingBean = (AccMainSettingBean)result.retVal;
							}
							int IsAuditing=settingBean.getIsAuditing();
							 
							if (mainFlag) {
								// 插入凭证表头
								costMgt.insertAccMain(IsAuditing,cs, mainId, loginId,
										accPeriod, accYear, accMonth,
										departCode, "adjustExchange",
										sunCompany);
								// 插入汇兑损益的凭证明细
								if (debits - credits > 0) {
									costMgt.insertAccDetail(cs, accYear, accMonth,
											"660301", 0, 0, new BigDecimal(
													debits).subtract(
													new BigDecimal(credits))
													.doubleValue(), 0, loginId,
											mainId, "adjustExchange", "",
											sunCompany, departCode);
									// 修改汇兑损益的科目余额
									costMgt.updateAccBalanceCurr(conn, cbs, cs,
											"660301", accPeriod, accYear, 0,
											new BigDecimal(debits).subtract(
													new BigDecimal(credits))
													.doubleValue(), 0, 0, 2,
											sunCompany, departCode);

								} else {
									costMgt.insertAccDetail(cs, accYear, accMonth,
											"660301", new BigDecimal(credits)
													.subtract(
															new BigDecimal(
																	debits))
													.doubleValue(), 0, 0, 0,
											loginId, mainId, "adjustExchange",
											"", sunCompany, departCode);
									// 修改汇兑损益的科目余额
									costMgt.updateAccBalanceCurr(conn, cbs, cs,
											"660301", accPeriod, accYear,
											new BigDecimal(credits).subtract(
													new BigDecimal(debits))
													.doubleValue(), 0, 0, 0, 2,
											sunCompany,departCode);

								}

							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;

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

		// CallableStatement cbs = conn.prepareCall("{call
		// proc_updateSuper(?,?,?,?,?,?,?,?)}");
		cbs.registerOutParameter(7, java.sql.Types.INTEGER);
		cbs.registerOutParameter(8, java.sql.Types.VARCHAR);
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
		String classCode = "";
		// 存储过程参数1：修改的表名，参数2：分类的表(没有为null)，参数3：前面两个表关联的条件 (没有为null)，参数4：修改依据的条件
		// (没有为null)，参数5：要修改的字段，参数6：要修改的分类代码
		rss = cs
				.executeQuery("select classCode from tblAccTypeInfo where accNumber='"
						+ accNumber + "' and SCompanyID='" + sunCompany + "'");
		if (rss.next()) {
			classCode = rss.getString(1);
		}

		// 修改科目余额表中父类
		cbs.setString(1, "tblAccBalance");
		cbs.setString(2, "tblAccTypeInfo");
		cbs
				.setString(
						3,
						"tblAccBalance.subCode=tblAccTypeInfo.accNumber and tblAccTypeInfo.SCompanyID='"
								+ sunCompany
								+ "' and tblAccBalance.SCompanyID='"
								+ sunCompany + "'");
		cbs.setString(4, "period=" + period + " and Nyear=" + periodYear);
		cbs
				.setString(
						5,
						"PeriodDebitSumBase=sum(PeriodDebitSumBase)@SPFieldLink:PeriodCreditSumBase=sum(PeriodCreditSumBase)");
		cbs.setString(6, classCode);
		cbs.execute();

		// 更新父类分支机构的数据
		GlobalMgt globalMgt = new GlobalMgt();
		globalMgt.updateAccParentSum(conn, classCode, sunCompany, period,
				periodYear);
	}

	/**
	 * 系统反月结
	 * 
	 * @return Result
	 */
	public Result reSettleAcc(final String sunCompany, final String userId,
			final Hashtable map) {		
		Result rs2= this.getCurrPeriod(sunCompany);
		if (rs2.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs2;
		}
		AccPeriodBean periodBean = (AccPeriodBean) rs2.getRetVal();
		//查询当期期间是否存在已经计提的资产
		rs2=this.queryDepreAsset(periodBean.getAccPeriod(), periodBean.getAccYear(), periodBean.getAccMonth(), sunCompany);
		if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			rs2.setRetCode(ErrorCanst.RET_ASSET_NODEPRECIATE);
			return rs2;
		}
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();

							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap
									.get("DeCloseAcc");
							if (defineSqlBean != null) {
								HashMap map = new HashMap();

								Result rs3 = defineSqlBean.execute(conn, map,
										userId, null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									rs.setRetVal(rs3.retVal);
									rs.setRetCode(rs3.retCode);
									return;
								}
							}

							int accPeriod = 0;
							int accYear = 0;

							int perYear = 0;
							int perPeriod = 0;
							// 得到当前会计期间
							String sql = "select AccPeriod,AccYear,AccMonth from tblAccPeriod where statusId=1 ";
							ResultSet rss = cs.executeQuery(sql);
							if (rss.next()) {
								accPeriod = rss.getInt("AccPeriod");
								accYear = rss.getInt("AccYear");
							} else {
								rs.setRetCode(ErrorCanst.RET_NOTBEGINACC); // 未开账
								return;
							}

							//得到上个业务期间
							sql = "select top 1 AccYear,AccPeriod from tblAccPeriod where statusId=2 order by AccYear desc,AccPeriod desc";
							rss = cs.executeQuery(sql);
							if (rss.next()) {
								perYear = rss.getInt(1);
								perPeriod = rss.getInt(2);
								rs.retVal=perYear+"."+perPeriod;
							}else{
								rs.setRetCode(ErrorCanst.RET_NOTSETTLEPERIOD);//没有月结的业务期间
								return;
							}
							//删除固定资产上期自动产生的折旧记录	
							sql="delete from tblFixedAssetDepreciate where Period="+ accPeriod+ " and PeriodYear="+ accYear+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);

							// 删除凭证表头月结信息
							sql = "delete from tblAccMain where RefBillID='settleAcc' and RefBillType!='adjustExchange' and Period="
									+ perPeriod
									+ " and CredYear="
									+ perYear
									+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);
							// 删除凭证明细月结信息
							sql = "delete from tblAccDetail where RefBillID='settleAcc' and PeriodYear="
									+ perYear
									+ " and PeriodMonth in ("
									+ perPeriod
									+ ") and SCompanyID='"
									+ sunCompany + "'";
							cs.execute(sql);
							// 删除期未调汇月结信息
							sql = "delete from tblAccDetail where RefBillID='adjustExchange' and PeriodYear="
									+ perYear
									+ " and PeriodMonth in ("
									+ perPeriod
									+ ") and SCompanyID='"
									+ sunCompany + "'";
							cs.execute(sql);
							//更新之后期间的所有信息
							CallableStatement cbsAfter = conn.prepareCall("{call proc_updateAfterBalance(?,?,?)}");
							cbsAfter.setInt(1, perYear);
							cbsAfter.setInt(2, perPeriod);
							cbsAfter.setString(3, sunCompany);
							cbsAfter.execute();

							/** ****************************************修改会计期间信息********************************************** */
							sql = "update tblAccPeriod set statusId=0 where  AccPeriod="
									+ accPeriod
									+ " and AccYear="
									+ accYear
									+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);
							sql = "update tblAccPeriod set statusId=1 where  AccPeriod="
									+ perPeriod
									+ " and AccYear="
									+ perYear
									+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);

							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 系统反月结
	 * 
	 * @return Result
	 */
	public Result AccReSettleAcc(final String sunCompany, final String userId,final Hashtable map) {		
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();

							int accPeriod = 0;
							int accYear = 0;

							int perYear = 0;
							int perPeriod = 0;
							// 得到当前会计期间
							String sql = "select top 1 AccPeriod,AccYear,AccMonth from tblAccPeriod where AccStatusId=1 and SCompanyID='"
									+ sunCompany + "' order by AccMonth ";
							ResultSet rss = cs.executeQuery(sql);
							if (rss.next()) {
								accPeriod = rss.getInt("AccPeriod");
								accYear = rss.getInt("AccYear");
							} else {
								rs.setRetCode(ErrorCanst.RET_NOTBEGINACC); // 未开账
								return;
							}

							//得到上一个期间
							sql = "select top 1 AccYear,AccPeriod from tblAccPeriod where AccStatusId=2 and SCompanyID='"+ sunCompany+ "' order by AccYear desc,AccPeriod desc";
							rss = cs.executeQuery(sql);
							if (rss.next()) {
								perYear=rss.getInt(1);
								perPeriod = rss.getInt(2);
								rs.setRetVal(perYear+"."+perPeriod);
							} else {
								rs.setRetCode(ErrorCanst.RET_NOTSETTLEPERIOD); // 没有月结期间
								return;
							}
							
							/** ****************************************修改会计期间信息********************************************** */
							sql = "update tblAccPeriod set AccStatusId=0 where  AccPeriod="
									+ accPeriod
									+ " and AccYear="
									+ accYear
									+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);
							sql = "update tblAccPeriod set AccStatusId=1 where  AccPeriod="
									+ perPeriod
									+ " and AccYear="
									+ perYear
									+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);

							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public static Result backupDataBase(String path){
		final Result rs = new Result();
		try{
			Connection conn = ConnectionEnv.getConnection();
			// 备份旧帐套
			try{
				String date = BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss2);
				String backFileName = date + ".bak";
				CallableStatement cst = conn
						.prepareCall("{call proc_backupDataBase(?,?,?,?)}");					
				cst.setString(1, path);  
				cst.setString(2, backFileName);
				cst.registerOutParameter(3, Types.INTEGER);
				cst.registerOutParameter(4, Types.VARCHAR);
				cst.execute();
				int retCode =cst.getInt(3);   
				String retVal= cst.getString(4);   
				if(retCode != 0){
					rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
					rs.setRetVal(retVal);
				}
				
				//执行压缩
				String fn = retVal.substring(retVal.lastIndexOf("\\DB_")+4);
				String zipPath =path+(path.endsWith("\\")||path.endsWith("/")?"":"\\")+(fn.substring(0,fn.indexOf(".bak"))) + ".zip";
				FileOutputStream fileOutputStream = new FileOutputStream(zipPath);
				CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
				ZipOutputStream out = new ZipOutputStream(fileOutputStream);
				File comFile = new File(path+(path.endsWith("\\")||path.endsWith("/")?"":"\\")+"DB_"+fn);				
				compressFile(comFile, out);
				comFile.delete();
				comFile = new File(path+(path.endsWith("\\")||path.endsWith("/")?"":"\\")+"LOG_"+fn);				
				compressFile(comFile, out);
				comFile.delete();
				
				out.close();
				fileOutputStream.close();
				rs.setRetVal(zipPath);
			}catch (Exception ex) {
				BaseEnv.log.error("SysAccMgt.backupDataBase 数据备份失败",ex);
				rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				rs.setRetVal(ex.getMessage());
				if(ex.getMessage() != null && ex.getMessage().indexOf("xp_create_subdir()") > -1){
					rs.setRetVal("数据库服务器备份目录"+path+"不存在，请在系统安全中修改备份目录");
				}
			}	
			conn.close();
		}catch (Exception ex) {
			BaseEnv.log.error("SysAccMgt.backupDataBase 数据备份失败",ex);
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			rs.setRetVal(ex.getMessage());
		}		
		return rs;
	}
	
	/** 压缩一个文件 */
	private static void compressFile(File file, ZipOutputStream out) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			out.setEncoding("GBK");
			out.putNextEntry(new ZipEntry(file.getName()));
			int count;
			byte data[] = new byte[8192];
			while ((count = bis.read(data, 0, 8192)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * 系统年结存
	 * 
	 * @return Result
	 */

	public Result yearSettleAcc(final String newAcc, final String accName,
			final LoginBean loginBean, final String sunCompany,
			final Hashtable map, final String local,final String delDraft) {		
		if (accName.length() > 0) {			
			// 调用备份数据库存储过程
			BaseEnv.CLOSE_ACC = "backupDataBase";
			
			Result rb = new Result();
			Result rs = new SystemSafeMgt().querySafeValues();
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				rb.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				rb.setRetVal("查询备份路径异常");
				return rb;			
			}
			HashMap<String,String> bmap = (HashMap)rs.retVal;
			
			String backPath = bmap.get("backPath"); //数据备份保存路径
			if(backPath==null || backPath.length()  == 0){
				String userDir = System.getProperty("user.dir");
				String defDisk = userDir.substring(0,userDir.indexOf(":")+1);
				backPath = defDisk+"\\AioDefDbBakup";
			}			
			rb = this.backupDataBase(backPath);
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return rs;
			}			
		}
		final Result rs = new Result();

		
		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(loginBean.getId()));
		table.put("BillOper", "yearSettle");
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {

							String sql = "";
							Statement st = conn.createStatement();
							ResultSet rss = null;
							String timeStr = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
							int lastYear = 0;
							int lastMonth = 0;
							int lastPeriod = 0;

							// 查询出最小的会计期间年，年结后将此会计期间的年的期间数据都删除掉。
							sql = "select  top 1 AccYear,AccMonth,AccPeriod from tblAccPeriod where AccYear=(select min(AccYear) from tblAccPeriod) and statusId='2' and SCompanyID='"
									+ sunCompany
									+ "'  order by AccYear desc,AccPeriod desc";

							rss = st.executeQuery(sql);
							if (rss.next()) {
								lastYear = rss.getInt(1);
								lastMonth = rss.getInt(2);
								lastPeriod = rss.getInt(3);
							}
							
							if (newAcc.equals("true")) {
								// 判断用户是否已设置下一年会计期间
								sql = "select top 1 * from tblAccPeriod where AccYear="
										+ (lastYear + 1);
								ResultSet rsacc = st.executeQuery(sql);
								if (rsacc.next()) {
									sql = "delete from tblAccPeriod where AccYear="
											+ lastYear
											+ " and SCompanyID='"
											+ sunCompany + "'";
									st.execute(sql);
								} else {
									// 复制上一年的会计期间,将值设为初始状态
									sql = "update tblAccPeriod set AccYear=AccYear+1,statusId=0,AccStatusId=0,isBegin=2,periodBegin=replace(periodBegin,AccYear,AccYear+1), periodEnd=replace(periodEnd,AccYear,AccYear+1) where SCompanyID='"
											+ sunCompany + "'";
									st.execute(sql);
								}
								BaseEnv.CLOSE_ACC = "lastChangeIni";
								/***********************************更新业务单据数据********************************/
								SysAccMgt mgt=new SysAccMgt();
								mgt.yearSettleAccDelBill(conn, String.valueOf(lastPeriod), String.valueOf(lastYear), sunCompany);
								
								/*****************************判断用户是否选择了删除草稿信息*******************/
								if (delDraft.equals("true")) {
									DefineSQLBean defineSqlBeanDraft = (DefineSQLBean) BaseEnv.defineSqlMap
											.get("DeOpenAccDraft");
									if (defineSqlBeanDraft != null) {
										HashMap map = new HashMap();

										Result rs3 = defineSqlBeanDraft.execute(conn,
												map, loginBean.getId(), null, null, "");
										if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
											rs.setRetCode(rs3.retCode);
											return;
										}
									}
								}

								BaseEnv.CLOSE_ACC = "modulesChange";

								// 用户配置操作执行
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap
										.get("yearCloseAcc");
								if (defineSqlBean != null) {
									HashMap map = new HashMap();

									Result rs3 = defineSqlBean.execute(conn,
											map, loginBean.getId(), null, null,
											"");
									if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
										rs.setRetCode(rs3.retCode);
										return;
									}
								}
								
								sql = "update tblAccPeriod set isBegin=1 where AccPeriod=(select top 1 AccPeriod from tblAccPeriod where accYear="
										+ (lastYear + 1)
										+ " order by AccPeriod) and accYear="
										+ (lastYear + 1);
								st.execute(sql);
								
								int[] result=new int[2];
								result[0]=lastYear;
								
								sql="select count(AccPeriod) from tblAccPeriod where statusId=1 and accYear>"+lastYear;
								rsacc=st.executeQuery(sql);
								if(rsacc.next()){
									result[1]=rsacc.getInt(1);
								}
								rs.setRetVal(result);
							} 
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	// 得到当前会计期间及会计期间年和下一个会计期间及会计期间年
	public int[] getCurrAndNextPeriod(Statement cs, String sunCompany)
			throws Exception {
		int[] period = new int[4];
		// 得到当前会计期间
		String sql = "select top 1 AccPeriod,AccYear from tblAccPeriod where statusId=1 and SCompanyID='"
				+ sunCompany + "' order by AccMonth desc";
		ResultSet rss = cs.executeQuery(sql);
		int accPeriod = 0;
		int accYear = 0;
		if (rss.next()) {
			accPeriod = rss.getInt(1);
			accYear = rss.getInt(2);
		} else {// 如果没有开账期间
			sql = "select top 1 AccYear,AccPeriod from tblAccPeriod where IsBegin=1 and SCompanyID='"
					+ sunCompany + "' order by AccMonth desc";
			rss = cs.executeQuery(sql);
			if (rss.next()) {
				period[2] = rss.getInt(1);
				period[3] = rss.getInt(2);
			}
			period[0] = -1;
			period[1] = -1;
			return period;
		}
		// 得到下一个会计期间的信息
		int nextPeriod = accPeriod + 1;
		int nextYear = 0;
		sql = "select top 1 AccYear,AccPeriod from tblAccPeriod where AccPeriod="
				+ nextPeriod + " and AccYear=" + accYear + " order by AccMonth";
		rss = cs.executeQuery(sql);
		if (rss.next()) {
			nextYear = rss.getInt(1);
			nextPeriod = rss.getInt(2);
		} else {
			nextPeriod = 0;
		}
		period[0] = accYear;
		period[1] = accPeriod;
		period[2] = nextYear;
		period[3] = nextPeriod;

		return period;
	}

	public Result delBill(final String period, final String periodYear,
			final String SCompanyID, final String userId,
			final Hashtable tableInfos, final String local,final String SERVER,final String UID,final String PWD,final String DataBase) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();

							// 查询输入的期间是否已经月结
							String sql = "select statusId from tblAccPeriod where accYear="+ periodYear+ " and AccPeriod="+ period+ " and SCompanyID='" + SCompanyID + "'";
							ResultSet rss = st.executeQuery(sql);
							if (rss.next()) {
								int statusId = rss.getInt(1);
								if (statusId != 2) {
									rs.setRetCode(ErrorCanst.RET_NOTSETTLEACC_LAST);
									return;
								}
							} else {
								rs.setRetCode(ErrorCanst.RET_NOTSETTLEACC_LAST);
								return;
							}
							HashMap map = new HashMap();
							map.put("SERVER", SERVER);
							map.put("UID", SERVER);
							map.put("PWD", SERVER);
							map.put("DataBase", SERVER);
							map.put("period", period);
							map.put("periodYear", periodYear);
							map.put("SCompanyID", SCompanyID);
							// 执行结转数据前执行define CreateIniBefore
							DefineSQLBean DelBillBefore = (DefineSQLBean) BaseEnv.defineSqlMap.get("DelBillBefore");
							if (DelBillBefore != null) {
								Result rs3 = DelBillBefore.execute(conn, map,userId, null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									rs.setRetVal(rs3.retVal);
									rs.setRetCode(rs3.retCode);
									return;
								}
							}
							/***********************************更新期间数据************************************/
							//查询当前期间的下一个期间，并将其设置成开账期间，并将输入期间的月结标识修改成0
							sql = "select top 1 accYear,AccPeriod from tblAccPeriod where ((accYear="+ periodYear+ " and AccPeriod>"+ period+ ") or accYear>"+periodYear+") and SCompanyID='" + SCompanyID + "' order by accYear,AccPeriod";
							ResultSet rs=st.executeQuery(sql);
							int nextYear=0;
							int nextPeriod=0;
							
							if(rs.next()){
								nextYear=rs.getInt(1);
								nextPeriod=rs.getInt(2);
							}
							sql="update tblAccPeriod set isBegin=2 where SCompanyID='" + SCompanyID + "'";
							st.execute(sql);
							
							sql="update tblAccPeriod set isBegin=1 where SCompanyID='" + SCompanyID + "' and accYear="+nextYear+" and AccPeriod="+nextPeriod;
							st.execute(sql);
							
							sql="update tblAccPeriod set statusId=0 where ((accYear="+periodYear+" and AccPeriod<="+period+") or accYear<"+periodYear+") and SCompanyID='" + SCompanyID + "'";
							st.execute(sql);
							/***********************************更新业务单据数据************************************/
							SysAccMgt mgt=new SysAccMgt();
							mgt.yearSettleAccDelBill(conn, period, periodYear, SCompanyID);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 年结时删除单据的操作，适用于半年结
	 * @param conn
	 * @param period
	 * @param periodYear
	 * @param SCompanyID
	 * @throws Exception
	 */
    public void yearSettleAccDelBill(Connection conn,String period,String periodYear,String SCompanyID)throws Exception{
    	Statement st=conn.createStatement();
    	/***********************************库存数据处理************************************/
    	String sql="";
    	 
	    //将年前出入库明细中的销售出库单，换货单，退货单进行统计插入到年结前月度销售记录表中
	    sql="insert into tblMonthSaleNote(id,PeriodYear,Period,PeriodMonth,CompanyCode,StockCode,GoodsCode,DepartmentCode,EmployeeID,BatchNo,Inch,Hue,yearNO,ProDate,Availably,SalesAmount,"+
	    	"SalesQty,SalesProfit,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,SCompanyID)"+
	    	" select substring(replace(newid(),'-',''),1,30),PeriodYear,Period,PeriodMonth,CompanyCode,StockCode,GoodsCode,DepartmentCode,EmployeeID,BatchNo,Inch,Hue,yearNO,ProDate,Availably,"+
	    	"SUM(SalesAmount),SUM(SalesQty),SUM(SalesAmount)-SUM(OutstoreAmount)+SUM(InstoreAmount),'','',CONVERT(varchar(100), GETDATE(),20),CONVERT(varchar(100), GETDATE(),20),0,'00001' from tblStockDet where PeriodYear<="+periodYear+" and BillType in ('tblSalesOutStock','tblSalesReplace','tblSalesReturnStock') "+
	    	"group by PeriodYear,Period,PeriodMonth,CompanyCode,StockCode,GoodsCode,DepartmentCode,EmployeeID,BatchNo,Inch,Hue,yearNO,ProDate,Availably";
	    st.execute(sql);
	    //将年前出入库明细中的采购入库单，换货单，退货单进行统计插入到年结前月度销售记录表中
	    sql="insert into tblMonthSaleNote(id,PeriodYear,Period,PeriodMonth,CompanyCode,StockCode,GoodsCode,DepartmentCode,EmployeeID,BatchNo,Inch,Hue,yearNO,ProDate,Availably,BuyAmount,"+
	    	"BuyQty,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,SCompanyID)"+
	    	" select substring(replace(newid(),'-',''),1,30),PeriodYear,Period,PeriodMonth,CompanyCode,StockCode,GoodsCode,DepartmentCode,EmployeeID,BatchNo,Inch,Hue,yearNO,ProDate,Availably,"+
	    	"SUM(InstoreAmount-OutstoreAmount),SUM(InstoreQty-OutstoreQty),'','',CONVERT(varchar(100), GETDATE(),20),CONVERT(varchar(100), GETDATE(),20),0,'00001' from tblStockDet where PeriodYear<="+periodYear+" and BillType in ('tblBuyInStock','tblBuyOutStock','tblBuyReplace') "+
	    	"group by PeriodYear,Period,PeriodMonth,CompanyCode,StockCode,GoodsCode,DepartmentCode,EmployeeID,BatchNo,Inch,Hue,yearNO,ProDate,Availably";
	    st.execute(sql);
   
    	
		//先将期初期间改为0，并将期间前最后一个期间改为期初，删除除期初外期间前数据
		sql="update tblStockDet set Period=0,PeriodYear=0,PeriodMonth=0 where Period=-1 and PeriodYear=-1";
		st.execute(sql);
		
		String CostingMethod=BaseEnv.systemSet.get("GoodsCostingMethod").getSetting();
		//移动加权,全月一次平均将期间前最后一条记录设置成期初
		if("MWAM".equals(CostingMethod)||"MONTH".equals(CostingMethod)){
			//如果结存数量计算错误，先更新这些结存数量
			CallableStatement cst = conn.prepareCall("{call proc_transErrorData(?,?)}");
			cst.setInt(1, Integer.parseInt(period));
			cst.setInt(2, Integer.parseInt(periodYear));
			cst.execute();
	
			sql="update tblStockDet set Period=-1,PeriodYear=-1,PeriodMonth=-1,InstoreQty=TotalQty,InstorePrice=LastPrice,InstoreAmount=TotalAmt "+
				",OutstoreQty=0,OutstorePrice=0,OutstoreAmount=0 where id=(select top 1 id from tblStockDet a where a.goodPropHash=tblStockDet.goodPropHash and ((Period<="+ period+ " and PeriodYear="+
				periodYear+ ") or PeriodYear<" + periodYear+") order by BillDate desc,CreateTime desc,ItemOrder desc) and TotalQty!=0 ";
			st.execute(sql);
		}
		//先进先出商品商品将期间前记录设置成期初
		if("FIFO".equals(CostingMethod)){
			sql="update tblStockDet set Period=-1,PeriodYear=-1,PeriodMonth=-1,InstoreQty=TotalQty,InstorePrice=LastPrice,InstoreAmount=TotalAmt "+
			",OutstoreQty=0,OutstorePrice=0,OutstoreAmount=0 where ((Period<="+ period+ " and PeriodYear="+periodYear+ ") or PeriodYear<" + periodYear+") and TotalQty!=0 ";
			st.execute(sql);
		}
		sql="delete from tblStockDet where Period!=-1 and ((Period<="+ period+ " and PeriodYear="+ periodYear+ ") or PeriodYear<" + periodYear+")";
		st.execute(sql);
		//将期初数据插入到商品期初表中
		CallableStatement cst = conn.prepareCall("{call proc_GoodsIniBill(?)}");
		cst.setString(1, SCompanyID);
		cst.execute();
		
		/*******************************科目余额表数据处理**********************************/
		
		sql = "update tblAccBalance set Nyear=0,Nmonth=0,period=0 where Nyear=-1";
		st.execute(sql);
		sql = "update tblAccBalance set Nyear=-1 ,Nmonth=-1,period=-1,CurrYIni=PeriodBala,CurrYIniBala=PeriodBala,CurrYIniBase=PeriodBalaBase,CurrYIniBalaBase=PeriodBalaBase,"
				+ "CurrYIniDebitSum=0,CurrYIniCreditSum=0,PeriodIni=0,PeriodDebitSum=0,PeriodCreditSum=0,PeriodDCBala=0,PeriodBala=0,"
				+ "CurrYIniDebitSumBase=0,CurrYIniCreditSumBase=0,PeriodIniBase=0,PeriodDebitSumBase=0,PeriodCreditSumBase=0,PeriodDCBalaBase=0,PeriodBalaBase=0 "
				+ "where id=(select top 1 id from tblAccBalance a where tblAccBalance.subCode=a.subCode and isnull(tblAccBalance.DepartmentCode,'')=isnull(a.DepartmentCode,'') and ((Period<="+period+
				" and Nyear="+periodYear+") or Nyear<"+periodYear+") order by Nyear desc,Period desc)";
		st.execute(sql);
		
		sql="delete from tblAccBalance where Nyear!=-1 and ((period<="+ period+ " and Nyear="+ periodYear+ ") or Nyear<" + periodYear+")";
		st.execute(sql);
		
		//删除期初明细项目
		sql="delete from tblIniAccDet";
		st.execute(sql);
		//重新插入期初明细项目
		sql="INSERT INTO [tblIniAccDet]([id],[accCode],[workFlowNode],[workFlowNodeName],[Currency],[BeginAmount],[TotalDebit],[TotalLend]," +
				" [TotalRemain],[CurrencyRate],[CurBeginAmount],[CurTotalDebit],[CurTotalLend],[CurTotalRemain],[Remark],[SCompanyID],[detOrderNo]," +
				" [CompanyCode],[DepartmentCode],[EmployeeID],[ProjectCode],[StockCode],[createTime],[impId])" +
				"	select substring(replace(newid(),'-',''),1,30),a.SubCode,'-1','finish',a.CurType,a.CurrYIniBase,0,0,a.CurrYIniBase,a.curRate," +
				" a.CurrYIni,0,0,a.CurrYIni,'',a.SCompanyID,0,case when len(ISNULL(b.clientCode,'')) > 0 then b.ClientCode " +
				" when len(ISNULL(b.SuplierCode,'')) > 0 then b.SuplierCode else '' end,b.DepartmentCode,b.EmployeeID,b.ProjectCode,b.StockCode," +
				" CONVERT(varchar(19),getdate(),120),''  from  tblAccBalance  a join tblAccTypeInfo b on a.SubCode=b.AccNumber " +
				" where b.isCalculate=1 and a.Nyear=-1";
		st.execute(sql);
		
		//删除此期间及之前的凭证
		sql="delete from tblAccMain where (Period<="+period+" and CredYear="+periodYear+") or CredYear<"+periodYear;
		st.execute(sql);
		//删除此期间及之前的凭证明细
		sql="delete from tblAccDetail where (PeriodMonth<="+period+" and PeriodYear="+periodYear+") or PeriodYear<"+periodYear;
		st.execute(sql);
		
		/*************************************往来数据处理*********************************/			
		//先将期初期间改为0，并将期间前最后一条记录改为期初， 删除除期初外的往来明细表
		sql="update tblCompanyIni set Period=0,PeriodYear=0,PeriodMonth=0 where Period=-1 and PeriodYear=-1";
		st.execute(sql);
		sql="update tblCompanyIni set Period=-1,PeriodYear=-1,PeriodMonth=-1,BillDate='-1' "+
			",ReceiveTotalDebit=ReceiveTotalRemain,ReceiveTotalLend=0,PreReceiveTotalDebit=0,PreReceiveTotalLend=PreReceiveTotalRemain,PayTotalDebit=0,PayTotalLend=PayTotalRemain,PrePayTotalDebit=PrePayTotalRemain,PrePayTotalLend=0 "+
			",FcRecTotalDebit=FcRecTotalRemain,FcRecTotalCredit=0,FcPreRecTotalDebit=0,FcPreRecTotalCredit=FcPreRecTotalRemain,FcPayTotalDebit=0,FcPayTotalCredit=FcPayTotalRemain,FcPrePayTotalDebit=FcPrePayTotalRemain,FcPrePayTotalCredit=0 "+
			" where id=(select top 1 id from tblCompanyIni a where a.Currency=tblCompanyIni.Currency and a.CompanyCode=tblCompanyIni.CompanyCode"
			+" and ((Period<="+period+" and PeriodYear="+periodYear+") or PeriodYear<"+periodYear+") order by itemNo desc)";
		st.execute(sql);							
		sql="delete from tblCompanyIni where Period!=-1 and ((Period<="+period+" and PeriodYear="+periodYear+") or PeriodYear<"+periodYear+")";
		st.execute(sql);
		
		//根据往来明细更新往来汇总，先删除之前的往来汇总
		sql="delete from tblCompanyTotal";
		st.execute(sql);
		
		//根据往来明细插入往来汇总
		sql="insert into tblCompanyTotal (id,currency,CompanyCode,ReceiveBegin,PreReceiveBegin,PayBegin,PrePayBegin,ReceiveTotalDebit,ReceiveTotalLend,PreReceiveTotalDebit,PreReceiveTotalLend,PayTotalDebit,PayTotalLend,PrePayTotalDebit,PrePayTotalLend"+
			",ReceiveTotalRemain,PreReceiveTotalRemain,PayTotalRemain,PrePayTotalRemain"+
			",CurReceiveBegin,CurPreReceiveBegin,CurPayBegin,CurPrePayBegin,CurReceiveTotalDebit,CurReceiveTotalLend,CurPreReceiveTotalDebit,CurPreReceiveTotalLend,CurPayTotalDebit,CurPayTotalLend,CurPrePayTotalDebit,CurPrePayTotalLend,CurReceiveTotalRemain,CurPreReceiveTotalRemain,CurPayTotalRemain,CurPrePayTotalRemain,createTime,lastUpdateTime,SCompanyID) "+
			"select substring(replace(newid(),'-',''),1,30),Currency,a.CompanyCode,0,0,0,0,ReceiveTotalDebit,ReceiveTotalLend,PreReceiveTotalDebit,PreReceiveTotalLend,PayTotalDebit,PayTotalLend,PrePayTotalDebit,PrePayTotalLend,"+
			" ReceiveTotalDebit-ReceiveTotalLend,PreReceiveTotalLend-PreReceiveTotalDebit,PayTotalLend-PayTotalDebit,PrePayTotalDebit-PrePayTotalLend"+
			",0,0,0,0,FcRecTotalDebit,FcRecTotalCredit,FcPreRecTotalDebit,FcPreRecTotalCredit,FcPayTotalDebit,FcPayTotalCredit,FcPrePayTotalDebit,FcPrePayTotalCredit,FcRecTotalDebit-FcRecTotalCredit,FcPreRecTotalCredit-FcPreRecTotalDebit,FcPayTotalCredit-FcPayTotalDebit,FcPrePayTotalDebit-FcPrePayTotalCredit"+
			",'"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"','00001'"+
			" from (select a.classCode as CompanyCode,b.Currency,sum(isnull(b.ReceiveTotalDebit,0)) as ReceiveTotalDebit,sum(isnull(b.ReceiveTotalLend,0)) as ReceiveTotalLend,sum(isnull(b.PreReceiveTotalDebit,0)) as PreReceiveTotalDebit,sum(isnull(b.PreReceiveTotalLend,0)) as PreReceiveTotalLend,sum(isnull(b.PayTotalDebit,0)) as PayTotalDebit,sum(isnull(b.PayTotalLend,0)) as PayTotalLend,sum(isnull(b.PrePayTotalDebit,0)) as PrePayTotalDebit,sum(isnull(b.PrePayTotalLend,0)) as PrePayTotalLend"+
			",sum(isnull(FcRecTotalDebit,0)) as FcRecTotalDebit,sum(isnull(FcRecTotalCredit,0)) as FcRecTotalCredit,sum(isnull(FcPreRecTotalDebit,0)) as FcPreRecTotalDebit,sum(isnull(FcPreRecTotalCredit,0)) as FcPreRecTotalCredit,sum(isnull(FcPayTotalDebit,0)) as FcPayTotalDebit,sum(isnull(FcPayTotalCredit,0)) as FcPayTotalCredit,sum(isnull(FcPrePayTotalDebit,0)) as FcPrePayTotalDebit,sum(isnull(FcPrePayTotalCredit,0)) as FcPrePayTotalCredit from tblCompany a left join  tblCompanyIni b on a.classCode=b.CompanyCode where a.isCatalog=0 group by a.classCode,b.Currency) as a";
		System.out.println(sql);
		st.execute(sql);

		/**********************************固定资产数据处理********************************************/
		sql="update tblFixedAssetAdd set Period=-1,PeriodMonth=-1,PeriodYear=-1,moduleType=0 where  Period!=-1  and ((Period<="+period+" and PeriodYear="+periodYear+") or PeriodYear<"+periodYear+")";
		st.execute(sql);
		
		//删除期间之前的单据信息
		cst = conn.prepareCall("{call proc_delSettleBill(?,?,?)}");
		cst.setString(1, period);
		cst.setString(2, periodYear);
		cst.setString(3, SCompanyID);
		cst.execute();
    }
    
	public Result existNewYearBill() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String querysql = "select top 1 * from tblstockdet where periodYear=(select accYear from tblAccPeriod where statusId=1)";
						try {
							PreparedStatement cs = conn
									.prepareStatement(querysql);
							BaseEnv.log.debug(querysql);
							ResultSet rset = cs.executeQuery();
							if (rset.next()) {
								rs.setRetVal(true);
							} else {
								rs.setRetVal(false);
							}
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql,
									ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	
	/**
	 * 结转损益
	 * @return
	 */
	public Result profitLoss(final LoginBean loginBean, final AccPeriodBean accPeriodBean, 
			final String sunCompany, final Hashtable hashTable,final Locale locale,
			final String profitLossLocal, final String settleLocal,final String protype,final MessageResources resources,final String accPass){
		final Result rs = new Result();
		final int accPeriod = accPeriodBean.getAccPeriod();
		final int accYear = accPeriodBean.getAccYear();
		final int accMonth = accPeriodBean.getAccMonth();
		final String loginId = loginBean.getId();
		final String departCode = loginBean.getDepartCode();
		Result result = null;
		
		//查询是否未开账
		result = this.getCurrPeriod(sunCompany);
		if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return result;
		}
		AccPeriodBean periodBean = (AccPeriodBean) result.getRetVal();

		if (periodBean.getAccYear() == -1) {
			result.setRetCode(ErrorCanst.RET_NOTBEGINACC);
			return result;
		}
		result = this.queryNoAccMain(accPeriodBean,"profitLoss");
		if (result.realTotal>0) {
			result.setRetVal("当前财务期间中存在未过账凭证，请先进行过账，才可以结转损益");
			result.setRetCode(ErrorCanst.RET_HAS_AUDITING);
			return result;
		}
		//查询下一个期间数据
		result = getNextPeriod(sunCompany, accYear, accPeriod);
		if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return result;
		}
		
		/* 下一个会计期间的年月*/
		AccPeriodBean nextPeriodBean = (AccPeriodBean) result.getRetVal();
		final int next_Period = nextPeriodBean.getAccPeriod();
		final int next_Year = nextPeriodBean.getAccYear();
		final int next_Month = nextPeriodBean.getAccMonth();
		
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn)
							throws SQLException {
						try{
							Statement st = conn.createStatement();
							BaseEnv.log.debug("结转损益：开始删除本月已经结转的损益，待重新生成 开始"+BaseDateFormat.getNowTimeLong());
							/* 结转损益前先删除旧的结转损益凭证*/
							//查询已过账的凭证先进行反过账处理在删除
							String condition = "tblAccMain.CredYear="+accYear+" and tblAccMain.CredMonth="+accMonth+" and tblAccMain.Period="+accPeriod+" and RefBillNo='settleAcc' and RefBillType='ProfitLossCarryForward'";
							String selectSql = "select id,workFlowNodeName,BillDate,createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID from tblAccMain where workFlowNodeName='finish' and "+condition;
							ResultSet rset = st.executeQuery(selectSql);
							while(rset.next()){
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("tblAccMain_Del_One");
								if (defineSqlBean == null) {
		                            BaseEnv.log.error("Define Sql Not Exist :Name = tblAccMain_Del_One");
		                            rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
		                            return  ;
		                        }
								HashMap<String,String> hashmap = new HashMap<String,String>();
								hashmap.put("tblAccMain_BillDate", rset.getString("BillDate"));
								hashmap.put("tblAccMain_id", rset.getString("id"));
								hashmap.put("tblAccMain_createBy", rset.getString("createBy"));
								hashmap.put("tblAccMain_createTime", rset.getString("createTime"));
								hashmap.put("tblAccMain_lastUpdateBy", rset.getString("lastUpdateBy"));
								hashmap.put("tblAccMain_lastUpdateTime", rset.getString("lastUpdateTime"));
								hashmap.put("tblAccMain_SCompanyID", rset.getString("SCompanyID"));
		                        //取hashMap中的数据
		                        Result ret = defineSqlBean.execute(conn, hashmap, loginBean.getId(),resources,locale,"");
		                        if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
		                        	rs.retCode=ret.getRetCode();
		                        	rs.retVal=ret.getRetVal();
		                        	return ;
		                        }
							}
							
							//删除凭证
							selectSql = "delete tblAccDetail WHERE f_ref IN (SELECT id FROM tblAccMain WHERE "+condition+")";
							st.executeUpdate(selectSql);
							selectSql = "delete tblAccMain where "+condition;
							st.executeUpdate(selectSql);
							BaseEnv.log.debug("结转损益：开始删除本月已经结转的损益，待重新生成 完成"+BaseDateFormat.getNowTimeLong());
							
							long time = System.currentTimeMillis();
							/* 结转损益*/
							BaseEnv.CLOSE_ACC = "settleCostBegin";
							
							//结转损益前，先用凭证数据更新科目余额
							CallableStatement cbsAfter = conn.prepareCall("{call proc_updateAfterBalance(?,?,?)}");
							cbsAfter.setInt(1, accYear);
							cbsAfter.setInt(2, accPeriod);
							cbsAfter.setString(3, sunCompany);
							cbsAfter.execute();
							
							BaseEnv.log.debug("结转损益：用凭证数据更新科目余额 完成"+BaseDateFormat.getNowTimeLong());
							
							/** ***********************************结转损益********************************************* */
							if(protype!=null && "profitloss".equals(protype)){
								BaseEnv.CLOSE_ACC = "settleProfitLossBegin";
							}
							settleProfitLoss(conn, accPeriod, accMonth,accYear
									, loginId, departCode, sunCompany,profitLossLocal,settleLocal,accPass);
							
							BaseEnv.log.debug("结转损益时间："+ (System.currentTimeMillis() - time));
						} catch (Exception e) {
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 财务月结
	 * @param loginBean
	 * @param accPeriodBean
	 * @param sunCompany
	 * @param hashTable
	 * @param locale
	 * @param profitLossLocal
	 * @param settleLocal
	 * @return
	 */
	public Result AccSettleAcc(final LoginBean loginBean, final AccPeriodBean accPeriodBean, 
			final String sunCompany, final Hashtable hashTable,final Locale locale,
			final String profitLossLocal, final String settleLocal, final MessageResources resources){

		BaseEnv.CLOSE_ACC = "settleProfitLossValidate";
		Result rs2 = this.queryNoAccMain(accPeriodBean,"settle");
		if (rs2.realTotal>0) {
			rs2.setRetVal("当前财务期间中存在未过账凭证，请先进行过账，才可以进行财务月结");
			rs2.setRetCode(ErrorCanst.RET_HAS_AUDITING);
			return rs2;
		}
		/* 验证本期所有损益类是否都已经结转*/
		rs2 = this.queryVoucherProfitLoss(accPeriodBean,sunCompany);
		if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			rs2.setRetCode(ErrorCanst.RET_ASSET_NODEPRECIATE);
			return rs2;
		}else{
			Integer flag = Integer.valueOf(rs2.retVal.toString());
			if(flag == 1){
				//未结转损益凭证无法月结
				rs2.setRetVal("当前财务期间的损益类科目的本期余额不为0，请先进行结转损益，才可以进行财务月结");
				rs2.setRetCode(ErrorCanst.RET_NOTPROFITLOSS_ERROR);
				return rs2;
			}
		}
		
		final Result rs=new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							String sql = "select AccYear,AccPeriod from tblAccPeriod where AccStatusId=1 and SCompanyID='" + sunCompany + "'";	
							ResultSet rst=cs.executeQuery(sql);
							int AccYear=0;
							int AccPeriod=0;
							if(rst.next()){
								AccYear=rst.getInt(1);
								AccPeriod=rst.getInt(2);
							}
							int nextAccYear=0;
							int nextAccPeriod=0;
							if(AccPeriod==12){
								nextAccYear=AccYear+1;
								nextAccPeriod=1;
							}else{
								nextAccYear=AccYear;
								nextAccPeriod=AccPeriod+1;
							}
							
							/** ****************************************修改会计期间信息********************************************** */
							BaseEnv.CLOSE_ACC = "updatePriodBegin";
							sql = "update tblAccPeriod set AccStatusId=2 where AccPeriod="+ AccPeriod	+ " and accYear=" + AccYear	+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);

							sql = "update tblAccPeriod set AccStatusId=1,lastUpdateTime='"+new BaseDateFormat().format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+"' where AccPeriod="+
									+ nextAccPeriod+ " and AccYear="+ nextAccYear+ " and SCompanyID='" + sunCompany + "'";
							cs.execute(sql);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}

				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 业务月结 v7
	 * @param loginBean
	 * @param accPeriodBean
	 * @param sunCompany
	 * @param hashTable
	 * @param locale
	 * @param profitLossLocal
	 * @param settleLocal
	 * @return
	 */
	public Result billSettleAcc(final LoginBean loginBean, final AccPeriodBean accPeriodBean, 
			final String sunCompany, final Hashtable hashTable,final Locale locale,
			final String profitLossLocal, final String settleLocal, final MessageResources resources,String zeroPriceIn){
		
		final int accPeriod = accPeriodBean.getAccPeriod();
		final int accMonth = accPeriodBean.getAccMonth();
		final int accYear = accPeriodBean.getAccYear();
		Result rs2 = null;
		long time=System.currentTimeMillis();
		String CostingMethod=BaseEnv.systemSet.get("GoodsCostingMethod").getSetting();
		if("MONTH".equals(CostingMethod)){//全月一次允许先出后入，可能出现这种情况
			BaseEnv.log.debug("业务月结：1、查询月结的月份内的最后的结存数量是否有负数 代码开始执行");
			//查询月结的月份内的最后的结存数量是否有负数，如果有负数不允许月结
			rs2 = this.getNegativeLastQty(sunCompany, accYear, accPeriod);
			if (rs2.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				return rs2;
			}
			BaseEnv.log.debug("上述代码执行时间："+(System.currentTimeMillis()-time));
			time=System.currentTimeMillis();
		}
				
		if("true".equals(BaseEnv.systemSet.get("FixedAsset").getSetting())){
			BaseEnv.log.debug("业务月结：2、查询是否有未进行计提的资产 代码开始执行");
			//查询是否有未进行计提的资产
			rs2=this.queryNoDepreAsset(accPeriod, accYear, accMonth, sunCompany);
			if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
				rs2.setRetCode(ErrorCanst.RET_ASSET_NODEPRECIATE);
				return rs2;
			}
			BaseEnv.log.debug("上述代码执行时间："+(System.currentTimeMillis()-time));
			time=System.currentTimeMillis();
		}
		//查询未过账的草稿单据
		if(BaseEnv.systemSet.get("existDraftNotSettle").getSetting().equals("true")){
			BaseEnv.log.debug("业务月结：3、查询未过账的草稿单据 代码开始执行");
			BaseEnv.CLOSE_ACC="checkDraftBill";
			rs2=this.queryDraftBill(sunCompany, locale.toString());
			BaseEnv.log.debug("上述代码执行时间："+(System.currentTimeMillis()-time));
			if (rs2.realTotal>0) {
				rs2.setRetCode(ErrorCanst.RET_BILL_EXISTDRAFT);
				return rs2;
			}
			
			time=System.currentTimeMillis();
		}
		//查询未审核的单据
		if(BaseEnv.systemSet.get("existAuditNotSettle").getSetting().equals("true")){
			BaseEnv.log.debug("业务月结：4、查询未审核的单据 代码开始执行");
			BaseEnv.CLOSE_ACC="checkNotAuditBill";
			rs2=this.queryNotAuditBill(sunCompany,locale.toString()) ;
			BaseEnv.log.debug("上述代码执行时间："+(System.currentTimeMillis()-time));
			if (rs2.realTotal>0) {
				rs2.setRetCode(ErrorCanst.RET_BILL_NOTAPPROVE);
				return rs2;
			}
			
			time=System.currentTimeMillis();
		}
		
		//查询本期间未产生凭证的单据
		if(BaseEnv.systemSet.get("autoGenerateAcc").getSetting().equals("false")){
			BaseEnv.log.debug("业务月结：5、查询本期间未产生凭证的单据 代码开始执行");
			BaseEnv.CLOSE_ACC="checkNotAuditBill";
			rs2=this.queryNotCreateAcc(accPeriod,accYear) ;
			BaseEnv.log.debug("上述代码执行时间："+(System.currentTimeMillis()-time));
			if (rs2.realTotal>0) {
				rs2.setRetCode(ErrorCanst.RET_BILL_NOTCREATEACC);
				return rs2;
			}
			
			time=System.currentTimeMillis();
		}
		
		//查询下一个期间数据
		rs2 = this.getNextPeriod(sunCompany, accYear, accPeriod);
		if (rs2.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs2;
		}

		AccPeriodBean nextPeriodBean = (AccPeriodBean) rs2.getRetVal();
		final int next_Period = nextPeriodBean.getAccPeriod();
		final int next_Year = nextPeriodBean.getAccYear();
		final int next_Month = nextPeriodBean.getAccMonth();
		
		
		/* 设置并查询为0入库的其他入库单，退货单，换货单*/
		BaseEnv.log.debug("业务月结：6、计算并查询成本为0的销售退货、换货，其他入库的成本价 代码开始执行");
		ReCalcucateMgt rec=new ReCalcucateMgt();
		BaseEnv.CLOSE_ACC = "reCalBegin";		
		//如果界面提示有0单价入库可继续月结，则调用重算成本过程，不需要判断
		String type="true".equals(zeroPriceIn)?"reCalcucate":"";
		rs2=this.setQueryZeroPriceIn(accYear, accPeriod, type);
		BaseEnv.log.debug("上述代码执行时间：" + (System.currentTimeMillis() - time));
		if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			return rs2;
		}
		
		
		
		BillSettleAccThread accThread= new BillSettleAccThread( loginBean, next_Period,next_Year,next_Month,accYear,accMonth,accPeriod,sunCompany,type) ;
		accThread.start() ;
		
		return rs2;
	}
	/**
	 * 根据规则更新0成本入库的其他入库单，销售退货单，销售换货单的成本单价，并查询出更新后成本依然为0的商品
	 * @param sunCompany
	 * @param PeriodYear
	 * @param Period
	 * @param loginId
	 * @param type
	 * @param GoodsCode
	 * @param isCatalog
	 * @return
	 */
	public Result setQueryZeroPriceIn(final int PeriodYear,final int Period,final String type) {
		final Result rs = new Result();
		final String DigitsAmount=BaseEnv.systemSet.get("DigitsAmount").getSetting();
		final String DigitsPrice=BaseEnv.systemSet.get("DigitsPrice").getSetting();
		
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql="";
							Statement st=conn.createStatement();
							long time=System.currentTimeMillis();
							//如果是全月一次(其他算法的商品成本不由月结计算)，设置未引用单据单价为0的销售退货单，销售换货单，其他入库单的成本单价=商品的预设进价
							//********zxy屏蔽于2016年5月15日，本段代码不知基于什么考虑在其它入库和销售退货0单价时自动修改出入库明细的单价,而且没有开关设置，
							//带来成本突变之外，还引起存货和财务存货科目余额不一致。因为他只修改出入库明细记录，没有修改单据
							//**********************************
//							if("MONTH".equals(BaseEnv.systemSet.get("GoodsCostingMethod").getSetting())){
//								if("true".equals(BaseEnv.systemSet.get("OtherInGetLastCost").getSetting())){
//									sql="update tblStockDet set InstorePrice=PreBuyPrice,InstoreAmount=round(InstoreQty*PreBuyPrice,"+DigitsAmount+") from tblGoods where BillType='tblOtherIn' and period="+Period+" and periodYear="+PeriodYear+" and InstorePrice=0 and tblGoods.classCode=tblStockDet.GoodsCode and PreBuyPrice!=0";
//									st.execute(sql);
//								}
//								//计算没有引用单据的退货单的成本
//								sql="update tblStockDet set InstorePrice=PreBuyPrice,InstoreAmount=round(InstoreQty*PreBuyPrice,"+DigitsAmount+") from tblGoods where BillType='tblSalesReturnStock' and period="+Period+" and periodYear="+PeriodYear+" and InstorePrice=0 and (select isnull(SalesOutStockID,'') from tblSalesReturnStockDet where id=tblStockDet.SourceId)='' and tblGoods.classCode=tblStockDet.GoodsCode and PreBuyPrice!=0";
//								st.execute(sql);
//								sql="update tblStockDet set InstorePrice=PreBuyPrice,InstoreAmount=round(InstoreQty*PreBuyPrice,"+DigitsAmount+") from tblGoods where BillType='tblSalesReplace' and period="+Period+" and periodYear="+PeriodYear+" and InstorePrice=0 and isnull((select a.SalesOutStockID from tblSalesReplace a,tblSalesReplaceDet b where a.id=b.f_ref and b.id=tblStockDet.SourceID),'')='' and tblGoods.classCode=tblStockDet.GoodsCode and PreBuyPrice!=0";
//								st.execute(sql);
//								
//								//计算引用上个月销售单的退货单成本
//								sql="update tblStockDet set InstorePrice=b.CostPrice,InstoreAmount=round(InstoreQty*b.CostPrice,"+DigitsAmount+") from tblSalesOutStock a,tblSalesOutStockDet b,tblSalesReturnStockDet c where BillType='tblSalesReturnStock' and tblStockDet.period="+Period+" and tblStockDet.periodYear="+PeriodYear+" and tblStockDet.SourceId=c.id and a.id=c.SalesOutStockID and a.id=b.f_ref and c.GoodsCode=b.GoodsCode and c.BatchNo=b.BatchNo and c.Inch=b.Inch and c.Hue=b.Hue and c.yearNO=b.yearNO and c.ProDate=b.ProDate and c.Availably=b.Availably and (a.PeriodYear<"+PeriodYear+" or (a.PeriodYear="+PeriodYear+" and a.Period<"+Period+"))";
//								st.execute(sql);
//								sql="update tblStockDet set InstorePrice=b.CostPrice,InstoreAmount=round(InstoreQty*b.CostPrice,"+DigitsAmount+") from tblSalesOutStock a,tblSalesOutStockDet b,tblSalesReplace c,tblSalesReplaceDet d where BillType='tblSalesReplace' and tblStockDet.period="+Period+" and tblStockDet.periodYear="+PeriodYear+" and tblStockDet.SourceId=d.id and d.f_ref=c.id   and a.id=c.SalesOutStockID and a.id=b.f_ref and d.GoodsCode=b.GoodsCode and d.BatchNo=b.BatchNo and d.Inch=b.Inch and d.Hue=b.Hue and d.yearNO=b.yearNO and d.ProDate=b.ProDate and d.Availably=b.Availably and (a.PeriodYear<"+PeriodYear+" or (a.PeriodYear="+PeriodYear+" and a.Period<"+Period+"))";
//								st.execute(sql);
//								
//								//将退、换货单的状态都改为0
//								sql="update tblStockDet set statusId=0 where BillType in ('tblSalesReturnStock','tblSalesReplace') and period="+Period+" and periodYear="+PeriodYear;
//								st.execute(sql);
//								
//								//将已经计算了成本的换货单，退货单的出库明细的状态改为1
//								sql="update tblStockDet set statusId=1 where BillType='tblSalesReturnStock' and period="+Period+" and periodYear="+PeriodYear+" and ((select isnull(SalesOutStockID,'') from tblSalesReturnStockDet where id=tblStockDet.SourceID)='' or (select COUNT(0) from tblSalesOutStock where id=(select SalesOutStockID from tblSalesReturnStockDet where id=tblStockDet.SourceID) and (PeriodYear<"+PeriodYear+" or (PeriodYear="+PeriodYear+" and Period<"+Period+")))>0)";
//								st.execute(sql);
//								sql="update tblStockDet set statusId=1 where BillType='tblSalesReplace' and period="+Period+" and periodYear="+PeriodYear+" and ((select isnull(a.SalesOutStockID,'') from tblSalesReplace a,tblSalesReplaceDet b where a.id=b.f_ref and b.id=tblStockDet.SourceID)='' or (select COUNT(0) from tblSalesOutStock where id=(select a.SalesOutStockID from tblSalesReplace a,tblSalesReplaceDet b where a.id=b.f_ref and b.id=tblStockDet.SourceID) and (PeriodYear<"+PeriodYear+" or (PeriodYear="+PeriodYear+" and Period<"+Period+")))>0)";
//								st.execute(sql);
//							}
							if(!"reCalcucate".equals(type)){
								sql="select billType,BillId,BillDate,BillNo from tblStockDet where BillType='tblOtherIn' and period="+Period+" and periodYear="+PeriodYear+" and InstorePrice=0"
									+" union all "
									+" select billType,BillId,BillDate,BillNo from tblStockDet where BillType='tblSalesReturnStock' and period="+Period+" and periodYear="+PeriodYear+" and InstorePrice=0 and (select isnull(SalesOutStockID,'') from tblSalesReturnStockDet where id=tblStockDet.SourceId)=''"
									+" union all "
									+" select billType,BillId,BillDate,BillNo from tblStockDet where BillType='tblSalesReplace' and period="+Period+" and periodYear="+PeriodYear+" and InstorePrice=0 and (select isnull(a.SalesOutStockID,'') from tblSalesReplace a,tblSalesReplaceDet b where b.id=tblStockDet.SourceId and a.id=b.f_ref)=''";
								ArrayList list=new ArrayList();
								ResultSet rst=st.executeQuery(sql);
								while(rst.next()){
									String[] str=new String[4];
									str[0]=rst.getString(1);
									str[1]=rst.getString(2);
									str[2]=rst.getString(3);
									str[3]=rst.getString(4);
									list.add(str);
								}
								if(list.size()>0){
									rs.setRetCode(ErrorCanst.RET_INPRICE_IS_ZERO);
								}
								rs.setRetVal(list);
							}
							
						} catch (Exception ex) {								
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode); 
		return rs;
	}
	/**
	 * 根据当前会计期间查询凭证中是否存在损益类科目
	 * @param periodBean
	 * @return
	 */
	public Result queryVoucherProfitLoss(final AccPeriodBean periodBean,final String sunCompany){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String openDeptAcc=BaseEnv.systemSet.get("openDeptAcc")==null?"false":BaseEnv.systemSet.get("openDeptAcc").getSetting();
							ResultSet result = null;
							PreparedStatement ps = null;
							String deptCode = "";
							//查询科目余额表中的所有部门
							if("true".equals(openDeptAcc)){
								String sqlstr="select isnull(DepartmentCode,'') from tblAccBalance where len(isnull(DepartmentCode,''))>0 group by isnull(DepartmentCode,'')";
								ps = conn.prepareStatement(sqlstr);
								result = ps.executeQuery();
								while(result.next()){
									deptCode += "'"+result.getString(1)+"',";
								}
							}
							if(deptCode.length()>0){
								deptCode = deptCode.substring(0,deptCode.length()-1);
							}
							int flag = 0;
							String sql = "select COUNT(0) as sumBase from tblAccBalance a,tblAccTypeInfo b ";
							sql += "where a.SubCode=b.AccNumber and SubCode like '6_%' and Period=? and Nyear=? and a.SCompanyID=? ";
							sql += "and b.SCompanyID=? and b.isCatalog=0 and PeriodDebitSumBase!=PeriodCreditSumBase";
							if(!"".equals(deptCode)){
								sql += " and isnull(a.DepartmentCode,'') in ("+deptCode+")";
							}else{
								sql += " and isnull(a.DepartmentCode,'')='' ";
							}
							ps = conn.prepareStatement(sql);
							BaseEnv.log.debug("财务月结查询凭证中是否存在损益类科目sql="+sql);
							BaseEnv.log.debug("财务月结查询凭证中是否存在损益类科目参数1="+periodBean.getAccPeriod());
							BaseEnv.log.debug("财务月结查询凭证中是否存在损益类科目参数2="+periodBean.getAccYear());
							BaseEnv.log.debug("财务月结查询凭证中是否存在损益类科目参数3="+sunCompany);
							BaseEnv.log.debug("财务月结查询凭证中是否存在损益类科目参数4="+sunCompany);
							ps.setInt(1, periodBean.getAccPeriod());
							ps.setInt(2, periodBean.getAccYear());
							ps.setString(3, sunCompany);
							ps.setString(4, sunCompany);
							result = ps.executeQuery();
							int sumBase = 0;
							if(result.next()){
								sumBase = result.getInt("sumBase");
							}
							result.close();
							if(sumBase!=0){
									//存在金额未结转损益过
									flag = 1;
							}
							rs.setRetVal(flag);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("SysAccMgt queryVoucherProfitLoss:",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 查询未过账的凭证
	 * @return
	 */
	public Result queryNoAccMain(final AccPeriodBean accPeriod,final String type){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String selectsql = "select count(0) as count from tblAccMain where CredYear="+accPeriod.getAccYear()
							+" and CredMonth="+accPeriod.getAccMonth()+" and isAuditing!='print' and Period="+accPeriod.getAccPeriod()+" and workFlowNodeName!='finish' "+("settle".equals(type)?"":"and RefBillType!='ProfitLossCarryForward'");
							PreparedStatement ps = conn.prepareStatement(selectsql);
							BaseEnv.log.debug("结转损益-查询财务期间内是否存在未过账凭证："+selectsql);
							ResultSet result = ps.executeQuery();
							Integer count = 0;
							if(result.next()){
								count = result.getInt("count");
							}
							rs.setRealTotal(count);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("SysAccMgt queryNoBind:",ex) ;
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	
	/**
	 * 查询开账开始日期
	 * @return
	 */
	public Result getCurrentlyPeriod(){
		final Result result = new Result();
		final String sql = "select periodBegin from tblAccPeriod where IsBegin = 1";
		DBUtil.execute(new IfDB(){
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						Statement state = con.createStatement();
						ResultSet rs = state.executeQuery(sql);
						if(rs.next()){
							result.setRetVal(rs.getString("periodBegin"));
						}
					}
				});
				return 0;
			}
		});
		return result;
	}
	
	/**
	 * 设置开账期间
	 * @param date
	 * @param id
	 * @param ScompanyID
	 * @return
	 */
	public Result accPeriodByDate(final String date,final String id,final String ScompanyID){
		final Result result = new Result();		
		final String[] dates = date.split("-");
		
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) {
						try{
							Statement st=con.createStatement();
							//查询所设置的日期之前，期初之后是否存在库存明细，往来明细，凭证
							String sql="select count(0) from tblStockDet where billDate!='-1' and billDate<'"+date+"'";
							ResultSet rst=st.executeQuery(sql);
							int num=0;
							if(rst.next()){
								num=rst.getInt(1);
								if(num>0){
									result.setRetCode(ErrorCanst.DATA_ALREADY_USED);
									return;
								}
							}							
							sql="select count(0) from tblCompanyIni where billDate!='-1' and billDate<'"+date+"'";
							rst=st.executeQuery(sql);
							if(rst.next()){
								num=rst.getInt(1);
								if(num>0){
									result.setRetCode(ErrorCanst.DATA_ALREADY_USED);
									return;
								}
							}
							sql="select count(0) from tblAccMain a,tblAccDetail b where a.id=b.f_ref and a.billDate!='-1' and a.billDate<'"+date+"'";
							rst=st.executeQuery(sql);
							if(rst.next()){
								num=rst.getInt(1);
								if(num>0){
									result.setRetCode(ErrorCanst.DATA_ALREADY_USED);
									return;
								}
							}
							
							
						//将此月份前的会计期间都删除，年结时会拿最小的年份进行年结，应删除这些没用的数据
						sql = "delete from tblAccPeriod where AccYear<"+dates[0];
						PreparedStatement ps = con.prepareStatement(sql);
						ps.executeUpdate();
						
						//将原来的开账期间改为普通期间
						sql = "update tblAccPeriod set IsBegin = 2,statusId = 0 where IsBegin = 1";
						ps=con.prepareStatement(sql);
						ps.executeUpdate();
						
						//查询设置的期间是否已经存在
						sql = "select count(0) from tblAccPeriod where AccYear=? and AccMonth=?";
						ps=con.prepareStatement(sql);
						ps.setString(1, dates[0]);
						ps.setString(2, dates[1]);
						
						rst=ps.executeQuery();
						int count=0;
						if(rst.next()){
							count=rst.getInt(1);
						}
						
						//如果设置的期间存在就将此期间设置成开账期间，如果不存在就增加
						if(count>0){							
							sql = "update tblAccPeriod set IsBegin = 1 where AccYear = ? and AccMonth = ?";
							ps=con.prepareStatement(sql);
							ps.setString(1, dates[0]);
							ps.setString(2, dates[1]);
							ps.executeUpdate();
						}else{
							StringBuffer sqls = new StringBuffer();
							sqls.append("insert into tblAccPeriod(id,AccYear,AccMonth,AccPeriod,createBy,createTime,statusId,IsBegin,periodBegin,");
							sqls.append("periodEnd,printCount,lastUpdateBy,lastUpdateTime,SCompanyID) values (?,?,?,?,?,convert(varchar(19),getdate(),120),?,?,?,?,0,?,convert(varchar(19),getdate(),120),?)");
					
							int year = getIntByString(dates[0]);
							int month = getIntByString(dates[1]);
							int daysInMon = ((year%4==0)&&(year%100!=0)||(year%400==0))?29:28;
							String[] daysInMonth={"31",String.valueOf(daysInMon),"31","30","31","30","31","31","30","31","30","31"};
							
							ps = con.prepareStatement(sqls.toString());
							for (int i = 1; i <= 12; i++) {
								ps.setString(1, IDGenerater.getId());
								ps.setInt(2, year);
								ps.setInt(3, i);
								ps.setInt(4, i);
								ps.setString(5, id);
								if(i == month){
									ps.setInt(6, 0);
									ps.setInt(7, 1);
								}else{
									ps.setInt(6, 0);
									ps.setInt(7, 2);
								}
								String tempMonth = String.valueOf(i).length() == 1?"0"+String.valueOf(i):
									String.valueOf(i);
								String periodBegin = dates[0]+"-"+tempMonth+"-01";
								ps.setString(8, periodBegin);
								if(i == 2){
									String periodEnd = dates[0]+"-02-"+daysInMon;
									ps.setString(9, periodEnd);
								}else{
									String tempMonth1 = String.valueOf(i).length() == 1?"0"+String.valueOf(i):
										String.valueOf(i);
									String periodEnd = dates[0]+"-"+tempMonth1+"-"+daysInMonth[i-1];
									ps.setString(9, periodEnd);
								}
								ps.setString(10, id);
								ps.setString(11, ScompanyID);
								ps.executeUpdate();
							}
						}
						}catch(Exception ex){
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	public int getIntByString(String str){
		int temp = -1;
		try {
			temp = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return temp;
	}

	
}
