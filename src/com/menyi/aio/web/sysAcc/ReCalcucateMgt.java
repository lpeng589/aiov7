package com.menyi.aio.web.sysAcc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.AccBalanceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.StockDetBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.goodsProp.GoodsPropMgt;
import com.menyi.aio.web.iniSet.IniGoodsMgt;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: ���ˣ����˵Ľӿ���
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005  
 * </p>
 * h
 * <p>
 * Company: ������
 * </p>
 * 
 * @author ��ѩ
 * @version 1.0
 */
public class ReCalcucateMgt extends AIODBManager {

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
						+ "isNull(sum(isNull(PeriodCreditSum,0)),0) PeriodCreditSum from tblAccTypeInfo a,tblAccBalance b where b.subCode=a.accNumber and b.period="
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
							+ rss.getString(10) + " where period=" + period
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
	
	

	// ���·ֲֿ���Ϳ���ܱ�(����ɱ�֮��)
	/**
	 * @param conn
	 * @param sunCompany
	 * @param existStocksProp
	 * @param periodYear
	 * @param period
	 * @param loginId
	 * @param flag
	 * @throws Exception
	 */
	public void updateStocksAndStockTotal(Connection conn, String sunCompany,
			int periodYear, int period, String loginId, String flag, int trade,
			ArrayList detList) throws Exception {	
		Statement cs = conn.createStatement();
		long time = System.currentTimeMillis();
		// ���·ֲֿ��
		// �޸ĵ��ۣ����
		String sql = "";
		
		String goodPropHashs="";
		for (int i = 0; i < detList.size(); i++) {
			StockDetBean bean = (StockDetBean) detList.get(i);
			if (!goodPropHashs.equals(bean.getGoodHashNoSeq())) {
				goodPropHashs+="'"+bean.getGoodHashNoSeq()+"',";
			}
		}
		if(goodPropHashs.length()>0){
			goodPropHashs=goodPropHashs.substring(0,goodPropHashs.length()-1);
			sql = "update tblStocks set lastAmount=isnull((select sum(a.InstoreAmount)-sum(a.OutstoreAmount) from tblStockDet a where"
				+ " a.goodPropHashNoSeq=tblStocks.goodPropHash),lastAmount) "
				+ " where goodPropHash in ("+goodPropHashs+") and SCompanyID='"+ sunCompany + "' ";
			cs.execute(sql);
		}
		
		sql = "update tblStocks set lastPrice= case lastQty when 0 then 0 else dbo.getDigits('tblStocks','lastPrice',LastAmount/lastQty) end";
		cs.execute(sql);

		BaseEnv.log.debug("���·ֲֿ������浥��ʱ�䣺"
				+ (System.currentTimeMillis() - time));		
	}

	// �ж���Ʒ�Ƿ���ĩ����Ʒ
	public boolean isLastLevelGoods(Connection conn, String goodsCode)
			throws Exception {
		String sql = "select isCatalog from tblGoods where classCode = '"
				+ goodsCode + "'";
		ResultSet rs = conn.createStatement().executeQuery(sql);
		if (rs.next() && rs.getInt(1) == 1) {
			return false;
		}
		return true;
	}

	/**
	 * ��鲢������ǰ��֧�����ĸ����֧������ǰ����ڼ�Ŀ�Ŀ�������
	 * 
	 * @param conn
	 *            Connection
	 * @param sunCompany
	 *            String ��ǰ��֧����
	 * @param period
	 *            int
	 * @param periodYear
	 *            int
	 * @param periodMonth
	 *            int
	 */
	public void createParentSumCurrPeriodAcc(Connection conn,
			String sunCompany, int period, int periodYear, int periodMonth,
			int lastPeriod, int lastYear, String loginId) throws SQLException,
			Exception {
		Statement st = conn.createStatement();
		ResultSet rs;
		String sql;
		String longTime = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		while (sunCompany.length() > 5) {
			sunCompany = sunCompany.substring(0, sunCompany.length() - 5);
			sql = "select * from tblAccBalance where period=" + period
					+ " and Nyear=" + periodYear + " and SCompanyID='"
					+ sunCompany + "'";
			rs = st.executeQuery(sql);
			if (!rs.next()) {
				ArrayList newAccBal = this.getAccBalance(st, lastPeriod,
						lastYear, sunCompany);

				for (int i = 0; i < newAccBal.size(); i++) {
					AccBalanceBean balBean = (AccBalanceBean) newAccBal.get(i);

					sql = "insert into tblAccBalance (id,SubCode,CurType,createBy,lastUpdateBy,createTime,lastUpdateTime,"
							+ "statusId,CurrYIni,CurrYIniDebitSum,CurrYIniCreditSum,CurrYIniBala,PeriodIni,PeriodDebitSum,PeriodCreditSum,"
							+ "PeriodDCBala,PeriodBala,CurrYIniBase,CurrYIniDebitSumBase,CurrYIniCreditSumBase,CurrYIniBalaBase,PeriodIniBase,"
							+ "PeriodDebitSumBase,PeriodCreditSumBase,PeriodDCBalaBase,PeriodBalaBase,Period,Nyear,Nmonth,SCompanyID) values ('"
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
							+ balBean.getCurrYIniCreditSum()
							+ ","
							+ balBean.getCurrYIniBala()
							+ ","
							+ balBean.getCurrYIniBala()
							+ ",0,0,0,"
							+ balBean.getCurrYIniBala()
							+ ","
							+ balBean.getCurrYIniBase()
							+ ","
							+ balBean.getCurrYIniDebitSumBase()
							+ ","
							+ balBean.getCurrYIniCreditSumBase()
							+ ","
							+ balBean.getCurrYIniBalaBase()
							+ ","
							+ balBean.getCurrYIniBalaBase()
							+ ",0,0,0,"
							+ balBean.getCurrYIniBalaBase()
							+ ","
							+ period
							+ ","
							+ periodYear
							+ ","
							+ periodMonth
							+ ",'"
							+ sunCompany + "')";
					st.execute(sql);
				}
			}
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

			newBean.setCurrYIni(rss.getString("CurrYIni"));
			newBean.setCurrYIniDebitSum(rss.getString("CurrYIniDebitSum"));
			newBean.setCurrYIniCreditSum(rss.getString("CurrYIniCreditSum"));
			newBean.setCurrYIniBala(rss.getString("CurrYIniBala"));

			newBean.setCurrYIniBase(rss.getString("CurrYIniBase"));
			newBean.setCurrYIniDebitSumBase(rss
					.getString("CurrYIniDebitSumBase"));
			newBean.setCurrYIniCreditSumBase(rss
					.getString("CurrYIniCreditSumBase"));
			newBean.setCurrYIniBalaBase(rss.getString("CurrYIniBalaBase"));

			newBean.setCurType(rss.getString("CurType"));
			newBean.setSubCode(rss.getString("SubCode"));

			newBean.setPeriodIni(rss.getString("PeriodIni"));
			newBean.setPeriodDebitSum(rss.getString("PeriodDebitSum"));
			newBean.setPeriodCreditSum(rss.getString("PeriodCreditSum"));
			newBean.setPeriodDCBala(rss.getString("PeriodDCBala"));
			newBean.setPeriodBala(rss.getString("PeriodBala"));

			newBean.setPeriodIniBase(rss.getString("PeriodIniBase"));
			newBean.setPeriodDebitSumBase(rss.getString("PeriodDebitSumBase"));
			newBean
					.setPeriodCreditSumBase(rss
							.getString("PeriodCreditSumBase"));
			newBean.setPeriodDCBalaBase(rss.getString("PeriodDCBalaBase"));
			newBean.setPeriodBalaBase(rss.getString("PeriodBalaBase"));

			newBean.setStatusId(rss.getInt("StatusId"));
			newAccBal.add(newBean);
		}

		return newAccBal;
	}


	/**
	 * �õ���ǰ��Ʒ�����
	 */
	private double[] getTotalAmt(Connection con, StockDetBean detCurr, String SCompanyID) {
		double[] str = new double[2];

		String sql = "select  top 1 isnull(TotalQty,0),isnull(TotalAmt,0) from tblStockDet where goodPropHash="+detCurr.getGoodHash()
				+ " and SCompanyID='"+ SCompanyID + "'";
		sql += "  and (billDate<'" + detCurr.getBillDate() + "' or (billDate='"
				+ detCurr.getBillDate() + "' and (createTime<'"
				+ detCurr.getCreateTime() + "' or (createTime='"
				+ detCurr.getCreateTime() + "' and ItemOrder<"
				+ detCurr.getItemNo()
				+ ")))) order by billdate desc,createtime desc,itemorder desc";

		try {
			Statement stmt = con.createStatement();
			ResultSet rss = stmt.executeQuery(sql);
			if (rss.next()) {
				str[0] = rss.getDouble(1);
				str[1] = rss.getDouble(2);
			}
			rss.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * ����ǳ��⣬���ò��������Ʒ�Ľ�浥�ۣ���ȡ���������ⵥ����Ʒ����
	 */
	private double getBackPrice(Connection con, StockDetBean detCurr,String SCompanyID) {
		double str = 0;
		try {
			Statement stmt=con.createStatement();
			String	sql = "select  top 1 InstorePrice from tblStockDet where goodPropHash="+detCurr.getGoodHash()+" and SCompanyID='"
						+ SCompanyID + "' and InstorePrice!=0 ";
				sql += "  and (billDate>'" + detCurr.getBillDate() + "' or (billDate='"
						+ detCurr.getBillDate() + "' and (createTime>'"
						+ detCurr.getCreateTime() + "' or (createTime='"
						+ detCurr.getCreateTime() + "' and ItemOrder>"
						+ detCurr.getItemNo()+ ")))) order by billdate ,createtime ,itemorder ";
			ResultSet rss = stmt.executeQuery(sql);
			if (rss.next()) {
				str = rss.getDouble(1);
			}
			rss.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return str;
	}

	private void updateStockDet(Connection conn, StockDetBean detCurr)
			throws SQLException {
		Statement cs = conn.createStatement();
		int inPriceDit=GlobalsTool.getDigitsOrSysSetting("tblStockDet","InstorePrice");
		int outPriceDit=GlobalsTool.getDigitsOrSysSetting("tblStockDet","OutstorePrice");
		int totalPriceDit=GlobalsTool.getDigitsOrSysSetting("tblStockDet","LastPrice");
		int inAmtDit=GlobalsTool.getDigitsOrSysSetting("tblStockDet","InstoreAmount");
		int outAmtDit=GlobalsTool.getDigitsOrSysSetting("tblStockDet","OutstoreAmount");
		int totalAmtDit=GlobalsTool.getDigitsOrSysSetting("tblStockDet","TotalAmt");
		StringBuffer sqlBuf = new StringBuffer();
		String method=BaseEnv.systemSet.get("GoodsCostingMethod").getSetting();
		sqlBuf.append("update tblStockDet set OutstorePrice=round(").append(
				detCurr.getOutstorePrice()).append(","+outPriceDit+"),OutstoreAmount=round(").append(
				detCurr.getOutstoreAmount()).append(","+outAmtDit+"),InstorePrice=round(").append(
				detCurr.getInstorePrice()).append(","+inPriceDit+"),InstoreAmount=round(").append(
				detCurr.getInstoreAmount()).append(","+inAmtDit+")");
		if(!method.equals("FIFO")){//�Ƚ��ȳ����ü�����
			sqlBuf.append(",TotalQty=").append(
				detCurr.getTotalQty()).append(",TotalAmt=round(").append(
				detCurr.getTotalAmt()).append(","+totalAmtDit+"),LastPrice=round(").append(
				detCurr.getLastPrice()).append(","+totalPriceDit+")");
		}
		sqlBuf.append(",statusid=0 where id='")
				.append(detCurr.getId()).append("'");
		cs.execute(sqlBuf.toString());
	}

	/**
	 * ���¿����ϸ
	 * 
	 * @param conn
	 * @param detCurr
	 * @param propConstr
	 * @param propStr
	 * @param propAllStr
	 * @param sunCompanyId
	 * @throws Exception
	 */
	public void updateStockDet(Connection conn, StockDetBean detCurr,String sunCompanyId) throws Exception {
		double[] last = this.getTotalAmt(conn, detCurr,sunCompanyId);
		String method=BaseEnv.systemSet.get("GoodsCostingMethod").getSetting();
		if(!method.equals("FIFO")){//�Ƚ��ȳ����ü�����
			if (detCurr.getInstoreQty() != 0 || detCurr.getInstoreAmount() != 0) {
				detCurr.setTotalQty(GlobalsTool.round(new BigDecimal(last[0])
						.add(new BigDecimal(detCurr.getInstoreQty()))
						.doubleValue(), 8));
				detCurr.setTotalAmt(GlobalsTool.round(new BigDecimal(last[1])
						.add(new BigDecimal(detCurr.getInstoreAmount()))
						.doubleValue(), GlobalsTool.getDigitsOrSysSetting(
						"tblStockDet", "TotalAmt")));
				if(last[0]==0){
					
					//����һ����¼�Ľ�浥��Ϊ0ʱ,��0�����⣬��浥�۵�����ⵥ��
					detCurr.setLastPrice(detCurr.getInstorePrice());
				}else if (detCurr.getTotalQty() != 0) {				
					detCurr.setLastPrice(new BigDecimal(detCurr.getTotalAmt())
							.divide(
									new BigDecimal(detCurr.getTotalQty()),
									GlobalsTool.getDigitsOrSysSetting(
											"tblStockDet", "LastPrice"),
									RoundingMode.HALF_UP).doubleValue());
				} else {
					detCurr.setLastPrice(0d);
				}
				
			} else {				
				if (detCurr.getTotalQty() != 0) {
					detCurr.setTotalAmt(GlobalsTool.round(new BigDecimal(last[1])
						.subtract(new BigDecimal(detCurr.getOutstoreAmount()))
						.doubleValue(), GlobalsTool.getDigitsOrSysSetting(
						"tblStockDet", "TotalAmt")));
					detCurr.setLastPrice(new BigDecimal(detCurr.getTotalAmt())
							.divide(
									new BigDecimal(detCurr.getTotalQty()),
									GlobalsTool.getDigitsOrSysSetting(
											"tblStockDet", "LastPrice"),
									RoundingMode.HALF_UP).doubleValue());
				} else {
					//zxy ��������Ϊ�㣬�������������ҲΪ0��������������Ľ����
					detCurr.setTotalAmt(0d);
					detCurr.setOutstoreAmount(last[1]);
					if(detCurr.getOutstoreQty()!=0){
						detCurr.setOutstorePrice(detCurr.getOutstoreAmount()/detCurr.getOutstoreQty());
					}
					detCurr.setLastPrice(0d);
				}
			}
		}
		updateStockDet(conn, detCurr);
	}


	// ������ص���
	public void rewriteBill(Connection conn, ArrayList detList, int trade,int periodYear,int period,boolean isReCal)
			throws SQLException {
		
		for (int i = 0; i < detList.size(); i++) {
			StockDetBean detCurr = (StockDetBean) detList.get(i);
			CallableStatement cstmt = conn.prepareCall("{call proc_rewriteBill(?)}");
			cstmt.setString(1, detCurr.getId());
			boolean em = cstmt.execute();
			SQLWarning warn = cstmt.getWarnings();
            while(warn != null){            	
            	if(warn.getMessage().indexOf("���α�") == -1)
            		System.out.println(warn.getMessage());
            	warn = warn.getNextWarning();
            }            
		}	
	}


	/**
	 * V7ȫ��һ��ƽ����
	 * @param versionNum
	 * @param sunCompany
	 * @param periodYear
	 * @param period
	 * @param loginId
	 * @return
	 */
	public Result reCalcucateData(final String sunCompany,final int PeriodYear,
			final int Period, final String loginId,final String type,final String GoodsCode,final String isCatalog) {		
		Long time = System.currentTimeMillis();
		final Result rs = new Result();
		if(!"MONTH".equals(BaseEnv.systemSet.get("GoodsCostingMethod").getSetting())){
			return rs;
		}
		/**
		 * ------------------ȫ��һ��ƽ����ɱ������ֲֿ⣬���Ҫ���ֲֿ⣬����ͬ�۵�����������»��γ���ѭ�������������ͬ�۵������Բ�����-----------------------
		 */
		final String DigitsAmount=BaseEnv.systemSet.get("DigitsAmount").getSetting();
		final String DigitsPrice=BaseEnv.systemSet.get("DigitsPrice").getSetting();
		
		BaseEnv.log.debug("����ɱ�ǰ��0��ִ��define����reCalcucateDataBefore ��ʼ");
		//����ɱ�define ���������������
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {							
							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("reCalcucateDataBefore");
							if (defineSqlBean != null) {
								HashMap map = new HashMap();
								map.put("periodYear", PeriodYear);
								map.put("period", Period);
	
								Result rs3 = defineSqlBean.execute(conn, map,loginId, null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs3, "zh_CN", "");
					            	BaseEnv.log.debug(saveErrrorObj.getMsg());
									rs.setRetVal(saveErrrorObj.getMsg());
									rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
									return;
								}
							}
						} catch (Exception ex) {								
							BaseEnv.log.error("ReCalcucateMgt.reCalcucateData Error",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		BaseEnv.log.debug("����ɱ�ǰ��ִ��define����reCalcucateDataBefore��ʱ�䣺" + (System.currentTimeMillis() - time));
		if(retCode != ErrorCanst.DEFAULT_SUCCESS){
			rs.retCode = retCode;
			return rs;
		}
		BaseEnv.log.debug("����ɱ���1����ѯ������Ҫ���������hashֵ ���뿪ʼִ��");
		//����Ƿ��нɿⵥ������а��������߳���ִ��
		String sql = "select id from sysobjects where id = object_id('PDInvertoryPay') and type = 'u'";
		Result ers = sqlList(sql, new ArrayList());
		ReCalcucateThreadPool reCalTPool = null;
		final boolean isPd = ers.retCode == ErrorCanst.DEFAULT_SUCCESS&&((ArrayList)ers.retVal).size()>0;
		time=System.currentTimeMillis();
		final ArrayList seqList = new ArrayList();
		retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st=conn.createStatement();
							String sql  = "";
							sql="select goodPropHash from tblStockDet where isnull(seq,'') = '' and period="+Period+" and periodYear="+PeriodYear+(GoodsCode.length()>0?(isCatalog.equals("1")?" and GoodsCode like '"+GoodsCode+"%'":" and GoodsCode='"+GoodsCode+"'"):"")+" group by goodPropHash";
							ArrayList list=new ArrayList();
							ResultSet rst=st.executeQuery(sql);
							while(rst.next()){
								list.add(rst.getString(1));
							}
							//�ֿ�ִ�У��ǹ���������ԭ���ϵ��ȼ���ɱ����ټ����Ʒ�������ɼ���������
							sql="select goodPropHash from tblStockDet where isnull(seq,'') <> '' and  period="+Period+" and periodYear="+PeriodYear+(GoodsCode.length()>0?(isCatalog.equals("1")?" and GoodsCode like '"+GoodsCode+"%'":" and GoodsCode='"+GoodsCode+"'"):"")+" group by goodPropHash";
							rst=st.executeQuery(sql);
							while(rst.next()){
								seqList.add(rst.getString(1));
							}
							rs.setRetVal(list);
						} catch (Exception ex) {								
							BaseEnv.log.error("ReCalcucateMgt.reCalcucateData Error",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		}); 
		if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		BaseEnv.log.debug("��������ִ��ʱ�䣺"+(System.currentTimeMillis()-time));
		time = System.currentTimeMillis();
		BaseEnv.log.debug("����ɱ���2�����³������ϸ�ɱ�������ݳɱ������ֶ� ���뿪ʼִ��");
		ArrayList list=(ArrayList)rs.getRetVal();
		final ArrayList markList=(ArrayList)list.clone();
		

		
		if(isPd){
			//�����ģ���ɾ��tblStocksPeriod��������һ������
			retCode= DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {
								final String DigitsPrice=BaseEnv.systemSet.get("DigitsPrice").getSetting();
								String sql  = "";
								sql="delete tblStocksPeriod  where periodYear="+PeriodYear+" and period="+Period+"";
								PreparedStatement st=conn.prepareStatement(sql);
								st.executeUpdate();
								//�鵱ǰ�ڼ��ǲ��ǿ����ڼ�
								sql = " select IsBegin from tblAccPeriod where AccYear=?  and AccPeriod=?";
								st=conn.prepareStatement(sql);
								st.setInt(1, PeriodYear);
								st.setInt(2, Period);
								ResultSet rst=st.executeQuery();
								ArrayList hashList = new ArrayList();
								boolean isBegin = false;
								if(rst.next()){
									if(rst.getInt(1)==1){
										isBegin = true;
									}
								}
								if(isBegin){
									//�����ڳ�����
									sql = " INSERT INTO [tblStocksPeriod]([id],[GoodsCode],StockCode,Seq,[ProDate],[Availably],[BatchNo],[Inch],[Hue],[yearNO],"
											+ "[lastQty],[lastPrice],[lastPriceMaterial],[lastPriceLabor],[lastPriceProdCost],[lastPriceOutFee],[lastAmount],"
											+ "[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[SCompanyID],"
											+ "[iniQty],[iniPrice],[iniPriceMaterial],[iniPriceLabor],[iniPriceProdCost],[iniPriceOutFee],[iniAmount],"
											+ "[Period],[PeriodMonth],[PeriodYear],[goodPropHash])"
											+ "select substring(convert(varchar(40),newid()),1,30),[GoodsCode],StockCode,Seq,[ProDate],[Availably],[BatchNo],[Inch],[Hue],[yearNO],"
											+ "isnull(sum(instoreQty),0),round(sum(instoreAmount)/sum(instoreQty),"+DigitsPrice+"),round(sum(instoreAmount)/sum(instoreQty),"+DigitsPrice+"),0,0,0,isnull(sum(instoreAmount),0),"
											+ "'1','1','"+BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+"','"+BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+"',0,'00001',"
											+ "0,0,0,0,0,0,0,"
											+ Period+ ","+Period+ ","+PeriodYear+ ",goodPropHash"
											+ " from tblStockDet where period=-1 and periodYear=-1 "
											+ " group by goodPropHash,[GoodsCode],StockCode,Seq,[ProDate],[Availably],[BatchNo],[Inch],[Hue],[yearNO]";
									st=conn.prepareStatement(sql);
									st.executeUpdate();
								}else{
									int prePeriodYear = PeriodYear;
									int prePeriod = Period-1;
									if(prePeriod<1){
										prePeriod=12;
										prePeriodYear = prePeriodYear-1;
									}
									sql = " INSERT INTO [tblStocksPeriod]([id],[GoodsCode],StockCode,Seq,[ProDate],[Availably],[BatchNo],[Inch],[Hue],[yearNO],"
											+ "[lastQty],[lastPrice],[lastPriceMaterial],[lastPriceLabor],[lastPriceProdCost],[lastPriceOutFee],[lastAmount],"
											+ "[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[SCompanyID],"
											+ "[iniQty],[iniPrice],[iniPriceMaterial],[iniPriceLabor],[iniPriceProdCost],[iniPriceOutFee],[iniAmount],"
											+ "[Period],[PeriodMonth],[PeriodYear],[goodPropHash])"
											+ "select substring(convert(varchar(40),newid()),1,30),[GoodsCode],StockCode,Seq,[ProDate],[Availably],[BatchNo],[Inch],[Hue],[yearNO],"
											+ "[lastQty],[lastPrice],[lastPriceMaterial],[lastPriceLabor],[lastPriceProdCost],[lastPriceOutFee],[lastAmount],"
											+ "[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[SCompanyID],"
											+ "[lastQty],[lastPrice],[lastPriceMaterial],[lastPriceLabor],[lastPriceProdCost],[lastPriceOutFee],[lastAmount],"
											+ Period+ ","+Period+ ","+PeriodYear+ ",[goodPropHash]"
											+ " from tblStocksPeriod where period="+prePeriod+" and periodYear= "+prePeriodYear +" and lastQty >0  " ;
									st=conn.prepareStatement(sql);
									st.executeUpdate();
								}
								rs.setRetVal("");
							} catch (Exception ex) {								
								BaseEnv.log.error("ReCalcucateMgt.reCalcucateData Error",ex);
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								return;
							}
						}
					});
					return rs.getRetCode();
				}
			}); 
			if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
				return rs;
			}
			sql = "select id from sysobjects where id = object_id('PDGoodsAdjust') and type = 'u'";
			Result hfrs = sqlList(sql, new ArrayList());
			//����е��͵�֤���ǹ���ϵͳ
			final boolean ishf = hfrs.retCode == ErrorCanst.DEFAULT_SUCCESS&&((ArrayList)hfrs.retVal).size()>0;
			if(ishf){//�����ĵ�����ɱ�
				reCalTPool = new ReCalcucateThreadPoolForHF(markList,seqList,PeriodYear,Period);
			}else{//��ͨ�����ɱ�
				reCalTPool = new ReCalcucateThreadPoolForPD(markList,seqList,PeriodYear,Period);
			}
		}else{
			reCalTPool = new ReCalcucateThreadPoolForV7(markList,seqList,PeriodYear,Period);
		}
		//���̴߳�������ɱ�.�ݶ���3���߳�
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		for(int k = 0;k<1;k++){
			Thread th = new Thread(reCalTPool);
			th.setName("ReCalcucateThread"+k);
			th.start();
			threadList.add(th);
		}
		do{ //���̼߳����ȴ�����ɱ����		
			try {
				Thread.sleep(5000); 
			} catch (Exception e) {	}
			//��� �Ƿ����߳��ڻ
			boolean isAlive = false;
			for(Thread th :threadList){
				if(th.isAlive()){
					isAlive = true;
					break;
				}
			}
			if(!isAlive){ //�����߳�ȫ����ֹ��������
				break;
			}			
			int rec= reCalTPool.hashSize();
			BaseEnv.log.debug("����ɱ����ȴ�����ɱ���ɣ���ʣ��hash================================"+rec+",seq:"+reCalTPool.seqhashList.size());
		}while(true);
		
		BaseEnv.log.debug("����ɱ������³������ϸ�ɱ�������ݳɱ������ֶΣ�����ִ��ʱ�䣺"+(System.currentTimeMillis()-time));
		time = System.currentTimeMillis();
		BaseEnv.log.debug("����ɱ���3�����·ֲֿ��ɱ� ���뿪ʼִ��");
		//���·ֲֿ��
		final ArrayList lists=new ArrayList();
		retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st=conn.createStatement();
							String sql="select goodPropHashNoSeq from tblStockDet where period="+Period+" and periodYear="+PeriodYear+(GoodsCode.length()>0?(isCatalog.equals("1")?" and GoodsCode like '"+GoodsCode+"%'":" and GoodsCode='"+GoodsCode+"'"):"")+" group by goodPropHashNoSeq";
							
							ResultSet rst=st.executeQuery(sql);
							while(rst.next()){
								lists.add(rst.getString(1));
							}
							rs.setRetVal(lists);
						} catch (Exception ex) {								
							BaseEnv.log.error("ReCalcucateMgt.reCalcucateData Error",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		}); 
		for(int i=0;i<lists.size();i++){
			final String goodPropHashNoSeq=lists.get(i).toString(); 
			retCode= DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {
								Statement st=conn.createStatement();
								String sql = "update tblStocks set lastAmount=isnull((select sum(a.InstoreAmount)-sum(a.OutstoreAmount) from tblStockDet a where"
									+ " a.goodPropHashNoSeq=tblStocks.goodPropHash),lastAmount) "
									+ " where goodPropHash="+goodPropHashNoSeq+" and SCompanyID='"+ sunCompany + "' ";
								st.execute(sql);
							
								sql = "update tblStocks set lastPrice= case lastQty when 0 then 0 else dbo.getDigits('tblStocks','lastPrice',LastAmount/lastQty) end"
									+ " where goodPropHash="+goodPropHashNoSeq+" and SCompanyID='"+ sunCompany + "' ";
								st.execute(sql);
								if(isPd){
									//����������棬��Ҫ�����˹����Ʒ�
//									sql = "update tblStocks set lastPriceMaterial= case a.lastQty when 0 then 0 else b.lastPriceMaterial  end,"
//											+ "lastPriceLabor= case a.lastQty when 0 then 0 else b.lastPriceLabor  end,"
//											+ "lastPriceProdCost= case a.lastQty when 0 then 0 else b.lastPriceProdCost  end,"
//											+ "lastPriceOutFee= case a.lastQty when 0 then 0 else b.lastPriceOutFee  end"
//											+ " from tblStocks a join tblStocksPeriod b on a.GoodsCode=b.GoodsCode and a.ProDate=b.ProDate and a.Availably=b.Availably and a.BatchNo=b.BatchNo and a.Inch=b.Inch and a.Hue=b.Hue and a.yearNO=b.yearNO "
//											+ " where a.goodPropHash="+goodPropHashNoSeq+" and a.SCompanyID='"+ sunCompany + "' ";
//									st.execute(sql);
								}
							} catch (Exception ex) {								
								BaseEnv.log.error("ReCalcucateMgt.reCalcucateData Error",ex);
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								return;
							}
						}
					});
					return rs.getRetCode();
				}
			}); 
		}
		BaseEnv.log.debug("��������ִ��ʱ�䣺"+(System.currentTimeMillis()-time));
		time = System.currentTimeMillis();
		BaseEnv.log.debug("����ɱ���4��ִ��define����reCalcucateData ��ʼ");
		//����ɱ�define ���������������
		retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {							
							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("reCalcucateData");
							if (defineSqlBean != null) {
								HashMap map = new HashMap();
								map.put("periodYear", PeriodYear);
								map.put("period", Period);
	
								Result rs3 = defineSqlBean.execute(conn, map,loginId, null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									BaseEnv.log.debug(rs3.retVal);
									rs.setRetVal(rs3.retVal);
									rs.setRetCode(rs3.retCode);
									return;
								}
							}
						} catch (Exception ex) {								
							BaseEnv.log.error("ReCalcucateMgt.reCalcucateData Error",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		BaseEnv.log.debug("����ɱ���ִ��define����reCalcucateData��ʱ�䣺" + (System.currentTimeMillis() - time));
		rs.setRetCode(retCode);
		return rs;
	}

	
}
	