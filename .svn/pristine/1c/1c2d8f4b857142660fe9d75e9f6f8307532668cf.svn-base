package com.menyi.aio.web.sysAcc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletContext;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.AccBalanceBean;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.*;

/**
 * 
 * <p>Title:业务月结</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: 科荣软件
 * @Author 张雪
 */
public class BillSettleAccThread extends Thread {
	
	private UserFunctionMgt mgt = new UserFunctionMgt() ;
	private LoginBean loginBean;
	private int next_Period;
	private int next_Year;
	private int next_Month;
	private int accYear;
	private int accMonth;
	private int accPeriod;
	private String sunCompany;
	private String type;
	
    public BillSettleAccThread(LoginBean loginBean,int next_Period,int next_Year,int next_Month,int accYear,int accMonth,int accPeriod,String sunCompany,String type){
        this.loginBean = loginBean;
        this.next_Period=next_Period;
        this.next_Year=next_Year;
        this.next_Month=next_Month;
        this.accYear=accYear;
        this.accMonth=accMonth;
        this.accPeriod=accPeriod;
        this.sunCompany=sunCompany;
        this.type=type;
        this.setDaemon(true);
    }
    

	
	public void run() {
			try {
				ReCalcucateMgt rec=new ReCalcucateMgt();
				/* 重算成本*/
				final Result rs = rec.reCalcucateData(sunCompany,accYear,accPeriod,loginBean.getId(),type,"","");
				
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					BaseEnv.CLOSE_ACC = "CLOSE_FAILURE:"+rs.retVal;
					OnlineUserInfo.unlockSystem();
					OnlineUserInfo.setLockOper("");
					return;
				}
				
				/* 结转成本-生成凭证*/
				int retCode = DBUtil.execute(new IfDB() {
					public int exec(final Session session) {
						session.doWork(new Work() {
							public void execute(Connection connection)
									throws SQLException {
									Connection conn = connection;
									try{
										BaseEnv.CLOSE_ACC = "settleCreateAcc";
										long time=System.currentTimeMillis();
										/** *************************define验证(生成月结凭证模板)**************************************************** */
										DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("Settle_CreateAcc");
										if (defineSqlBean != null) {
											HashMap map = new HashMap();
				
											Result rs3 = defineSqlBean.execute(conn, map,
													loginBean.getId(), null, null, "");
											if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
												BaseEnv.log.debug(rs3.retVal);
												rs.setRetVal(rs3.retVal);
												rs.setRetCode(rs3.retCode);
												return;
											}
										}

										BaseEnv.log.debug("生成月结凭证模板时间："+ (System.currentTimeMillis() - time));
									}catch (Exception ex) {
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
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					BaseEnv.CLOSE_ACC = "CLOSE_FAILURE:"+rs.retVal;
					OnlineUserInfo.unlockSystem();
					OnlineUserInfo.setLockOper("");
					return;
				}
				retCode = DBUtil.execute(new IfDB() {
					public int exec(final Session session) {
						session.doWork(new Work() {
							public void execute(Connection connection)
									throws SQLException {
								Connection conn = connection;
								try {
									SysAccMgt accMgt = new SysAccMgt();
									Statement cs = conn.createStatement();
									String longDate = BaseDateFormat.format(new Date(),
											BaseDateFormat.yyyyMMddHHmmss);

									int nextPeriod = next_Period;
									int nextYear = next_Year;
									int nextMonth = next_Month;

									int[] cur = accMgt.getNextPriod(accYear, accMonth,
											accPeriod, loginBean.getId(), longDate, sunCompany,
											next_Period, next_Year, next_Month,
											session, connection, true);
									nextYear = cur[0];
									nextMonth = cur[1];
									nextPeriod = cur[2];

									ArrayList copyPeriod;
									if (nextYear > accYear) {
										SysAccMgt mgt = new SysAccMgt();
										Result rsCopy = mgt.getCopyPeriod(session,
												nextYear);
										if (rsCopy.retCode != ErrorCanst.DEFAULT_SUCCESS) {
											rs.retCode = rsCopy.retCode;
											return;
										} else {
											copyPeriod = (ArrayList) rsCopy.getRetVal();
										}
									} else {
										copyPeriod = new ArrayList();
										Object[] obj = new Object[3];
										obj[0] = nextYear;
										obj[1] = nextMonth;
										obj[2] = nextPeriod;
										copyPeriod.add(obj);
									}
									rs.setRetVal(copyPeriod);
									
									long time = System.currentTimeMillis();
									/** ****************************固定资产自动产生折旧记录************************************/							
									String sql="insert into tblFixedAssetDepreciate (id,billNo,workFlowNode,workFlowNodeName,AssetsAddId,useMonth,originalWorth,totalDeprecia,ReWorthRate,ReWorth,netWorth,currDeprecAmt"+
									",Period,PeriodYear,PeriodMonth,lastUpdateBy,lastUpdateTime,createBy,createTime,SCompanyID,statusId,deprecAccCode,DepartmentCode,DepreciateAcc)"+
										" select substring(replace(newid(),'-',''),1,28),'AD"+accYear+accPeriod+"'+ cast((ROW_NUMBER() over(order by GoodsNumber)) as varchar(20)),'-1','finish',tblFixedAssetAdd.id,planUseMonth,originalWorth,totalDeprecia,planReWorthRate,planReWorth,netWorth,monthDeprecAmt,"+
										"'"+nextPeriod+"','"+nextYear+"','"+nextMonth+"','"+loginBean.getId()+"','"+longDate+"','"+loginBean.getId()+"','"+longDate+"','"+sunCompany+"',0,deprecAccCode,DepartmentCode,DepreciateAcc from tblFixedAssetAdd join tblFixedAssetType on tblFixedAssetAdd.AssetsType=tblFixedAssetType.id where tblFixedAssetAdd.statusId=0 and netWorth>0  and tblFixedAssetAdd.workFlowNodeName='finish' and referDeprecia like '%0%'  and referMonth<planUseMonth and ((Period<="+accPeriod+" and PeriodYear="+accYear+") or PeriodYear<"+accYear+")";
									BaseEnv.log.debug(" 产生固定资产折旧记录 "+sql);
									cs.execute(sql);
									
									/** ***********************************各模块结转处理********************************************* */
									BaseEnv.CLOSE_ACC = "settleModulesBegin";
									
									cs = conn.createStatement();
									ResultSet rss = null;
									if (nextPeriod != 0) {
										/** ****************************科目余额表的结转 */
										// 得到所有科目余额本期信息，生成下一个期间的期初值
										ArrayList newAccBal = accMgt.getAccBalance(cs,
												accPeriod, accYear, sunCompany);
			
										sql = "select subCode+isnull(DepartmentCode,'') as subCode from tblAccBalance where Period="
												+ nextPeriod + " and Nyear=" + nextYear	+ " and SCompanyID='" + sunCompany + "'";
										cs = conn.createStatement();
										rss = cs.executeQuery(sql);
										ArrayList<String> subCodes = new ArrayList<String>();
										while (rss.next()) {
											subCodes.add(rss.getString("subCode").trim());
										}
										cs.close();
										
			
										sql = "insert into tblAccBalance (id,SubCode,CurType,createBy,lastUpdateBy,createTime,lastUpdateTime,"
											+ "statusId,CurrYIni,CurrYIniDebitSum,CurrYIniCreditSum,CurrYIniBala,PeriodIni,PeriodDebitSum,PeriodCreditSum,"
											+ "PeriodDCBala,PeriodBala,CurrYIniBase,CurrYIniDebitSumBase,CurrYIniCreditSumBase,CurrYIniBalaBase,PeriodIniBase,"
											+ "PeriodDebitSumBase,PeriodCreditSumBase,PeriodDCBalaBase,PeriodBalaBase,Period,Nyear,Nmonth,SCompanyID,DepartmentCode)";
										sql += " values(?,?,?,?,?,?,?,?,?,?,?,?,?,0,0,0,?,?,?,?,?,?,0,0,0,?,?,?,?,?,?)";
										PreparedStatement ps = conn.prepareStatement(sql);
										for (int i = 0; i < newAccBal.size(); i++) {
											AccBalanceBean balBean = (AccBalanceBean) newAccBal.get(i);
											boolean existsNowPeriod = false;
											if (subCodes.contains(balBean.getSubCode().trim()+balBean.getDepartmentCode().trim())) {
												existsNowPeriod = true;
											}
											if (!existsNowPeriod) { // 判断本期此科目信息不存在才新增本期期初信息
												ps.setString(1, IDGenerater.getId());
												ps.setString(2, balBean.getSubCode());
												ps.setString(3, balBean.getCurType());
												ps.setString(4, loginBean.getId());
												ps.setString(5, loginBean.getId());
												ps.setString(6, longDate);
												ps.setString(7, longDate);
												ps.setInt(8, balBean.getStatusId());
												ps.setDouble(9, Double.valueOf(balBean.getCurrYIni()==null?"0":balBean.getCurrYIni()));
												ps.setDouble(10, Double.valueOf(balBean.getCurrYIniDebitSum()==null?"0":balBean.getCurrYIniDebitSum()));
												ps.setDouble(11, Double.valueOf(balBean.getCurrYIniCreditSum()==null?"0":balBean.getCurrYIniCreditSum()));
												ps.setDouble(12, Double.valueOf(balBean.getCurrYIniBala()==null?"0":balBean.getCurrYIniBala()));
												ps.setDouble(13, Double.valueOf(balBean.getPeriodBala()==null?"0":balBean.getPeriodBala()));
												ps.setDouble(14, Double.valueOf(balBean.getPeriodBala()==null?"0":balBean.getPeriodBala()));
												ps.setDouble(15, Double.valueOf(balBean.getCurrYIniBase()==null?"0":balBean.getCurrYIniBase()));
												ps.setDouble(16, Double.valueOf(balBean.getCurrYIniDebitSumBase()==null?"0":balBean.getCurrYIniDebitSumBase()));
												ps.setDouble(17, Double.valueOf(balBean.getCurrYIniCreditSumBase()==null?"0":balBean.getCurrYIniCreditSumBase()));
												ps.setDouble(18, Double.valueOf(balBean.getCurrYIniBalaBase()==null?"0":balBean.getCurrYIniBalaBase()));
												ps.setDouble(19, Double.valueOf(balBean.getPeriodBalaBase()==null?"0":balBean.getPeriodBalaBase()));
												ps.setDouble(20, Double.valueOf(balBean.getPeriodBalaBase()==null?"0":balBean.getPeriodBalaBase()));
												ps.setInt(21, nextPeriod);
												ps.setInt(22, nextYear);
												ps.setInt(23, nextMonth);
												ps.setString(24, sunCompany);
												ps.setString(25, balBean.getDepartmentCode());
												ps.addBatch();
											}
										}
										ps.executeBatch();
									}
									
									
									//更新之后期间的所有信息
									CallableStatement cbsAfter = conn.prepareCall("{call proc_updateAfterBalance(?,?,?)}");
									cbsAfter.setInt(1, accYear);
									cbsAfter.setInt(2, accPeriod);
									cbsAfter.setString(3, sunCompany);
									cbsAfter.executeUpdate();
									cbsAfter.close();
																
									
									// 将各币种当前期间的调整汇率设置为下个期间的记账汇率（检查是否已经存在）
									Hashtable<String, SystemSettingBean> systemSet = BaseEnv.systemSet;
									String openCurrency = systemSet.get("currency").getSetting();
									if (openCurrency.equals("true")) {
										sql = "select Currency,CurrencyName,CurrencySign,AdjustExchange from  tblSetExchange a "
												+ " where period=" + accPeriod + " and PeriodYear=" + accYear + " and a.SCompanyID='"
												+ sunCompany + "' and (select count(*) from tblSetExchange b where b.Currency=a.Currency and b.SCompanyID='"
												+ sunCompany + "' and period=" + nextPeriod	+ " and periodYear=" + nextYear + ")=0 ";
										cs = conn.createStatement();
										rss = cs.executeQuery(sql);
										ArrayList list = new ArrayList();
										while (rss.next()) {
											Object[] obj = new Object[4];
											obj[0] = rss.getString(1);
											obj[1] = rss.getString(2);
											obj[2] = rss.getString(3);
											obj[3] = rss.getDouble(4);
											list.add(obj);
										}
										
										sql = "insert into tblSetExchange (id,CurrencyName,CurrencySign,Period,periodYear,"
											+ "RecordExchange,AdjustExchange,createBy,lastUpdateBy,createTime,"
											+ "lastUpdateTime,sCompanyID,Currency) values(?,?,?,?,?,?,0,?,?,?,?,?,?)";
										PreparedStatement ps = conn.prepareStatement(sql);
										for (int i = 0; i < list.size(); i++) {
											Object[] obj = (Object[]) list.get(i);
											ps.setString(1, IDGenerater.getId());
											ps.setString(2, String.valueOf(obj[1]));
											ps.setString(3, String.valueOf(obj[2]));
											ps.setInt(4, nextPeriod);
											ps.setInt(5, nextYear);
											ps.setString(6, String.valueOf(obj[3]));
											ps.setString(7, loginBean.getId());
											ps.setString(8, loginBean.getId());
											ps.setString(9, longDate);
											ps.setString(10, longDate);
											ps.setString(11, sunCompany);
											ps.setString(12, String.valueOf(obj[0]));
											ps.addBatch();
										}
										ps.executeBatch();
									}
									BaseEnv.log.debug("结转时间：" + (System.currentTimeMillis() - time));
									
									/** ****************************************修改会计期间信息********************************************** */
									BaseEnv.CLOSE_ACC = "updatePriodBegin";
									sql = "update tblAccPeriod set statusId=2 where AccPeriod="
											+ accPeriod	+ " and accYear=" + accYear	+ " and SCompanyID='" + sunCompany + "'";
									cs = conn.createStatement();
									cs.executeUpdate(sql);
									if (copyPeriod.size() > 0) {
										Object[] obj = (Object[]) copyPeriod.get(0);
										nextYear = Integer.parseInt(obj[0].toString());
										nextPeriod = Integer.parseInt(obj[2].toString());
									}
									sql = "update tblAccPeriod set statusId=1,lastUpdateTime='"+new BaseDateFormat().format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+"' where AccPeriod="+
											+ nextPeriod
											+ " and AccYear="
											+ nextYear
											+ " and SCompanyID='" + sunCompany + "'";
									cs = conn.createStatement();
									cs.executeUpdate(sql);
									sql="";
									BaseEnv.CLOSE_ACC = "CLOSE_SUCCESS";
								} catch (Exception ex) {
									ex.printStackTrace();
									BaseEnv.CLOSE_ACC = "CLOSE_FAILURE:"+ex;
									rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
									rs.retVal = ex.getMessage();
									return;
								}
							}

						});
						return rs.getRetCode();
					}
				});
			} catch (Exception ex1) {				
				BaseEnv.log.error("BillSettleAccThread error:", ex1) ;
				BaseEnv.CLOSE_ACC = "CLOSE_FAILURE:"+ex1;
			}
			OnlineUserInfo.unlockSystem();
			OnlineUserInfo.setLockOper("");
	}
}
