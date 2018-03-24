package com.menyi.aio.web.iniSet;

import java.util.*;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.GoodsBean;
import com.menyi.aio.bean.GoodsPropEnumItemBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.StockBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.dbfactory.hibernate.DBUtil;
import com.menyi.web.util.BaseEnv;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import org.hibernate.Session;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import java.sql.ResultSet;
import org.hibernate.jdbc.Work;
import com.menyi.aio.dyndb.DDLOperation;
import javax.servlet.http.HttpServletRequest;
import java.sql.CallableStatement;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.web.util.GlobalsTool;
import com.menyi.aio.dyndb.*;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.goodsProp.GoodsPropMgt;
import com.menyi.aio.web.report.*;
import java.sql.Types;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 往来期初的接口类
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
public class IniGoodsMgt extends AIODBManager {

	/**
	 * 修改往来单位期初值
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result updateMultiData(final String sunCompany,
			final List existPropNames, final List propValues,
			final String[] keyId, final String[] goodsCode,
			final String[] stockCode, final String[] IniTwoQty,
			final String[] iniQty, final String[] iniPrice,
			final String[] iniAmount, final boolean negativeStock,
			final String createBy,final ArrayList notUsedPropNames,final String[]StockLocation,
			final String seqPropfName,final String[]secUnit,final String[] conversion,final String[]secUnitQty,
			final String[]secUnitPrice,final String locale,final String[]provider,final List nvValues,final List existNVNames) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							boolean []isUpdateLast=new boolean[goodsCode.length];
							Result rs1=null;
							for(int i=0;i<goodsCode.length;i++){
								 rs1=delStockDet(goodsCode[i],stockCode[i],existPropNames,conn);
								if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
									rs.setRetCode(rs1.getRetCode());
									rs.setRetVal(rs1.getRetVal());
									return ;
								}
								isUpdateLast[i]=(Boolean)rs1.getRetVal();
								rs1=delIniStockDet(goodsCode[i],stockCode[i],conn);
								if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
									rs.setRetCode(rs1.getRetCode());
									rs.setRetVal(rs1.getRetVal());
									return ;
								}
							}
							for(int i=0;i<iniQty.length;i++){
								String[] row = new String[existPropNames.size()];
								String[] nvRow=new String[existNVNames.size()];
								for(int j=0;j<existNVNames.size();j++){
									nvRow[j]=((String[])nvValues.get(j))[i];
								}
								
								for (int j = 0; j < existPropNames.size(); j++) {
									if (propValues.get(j) != null) {
										row[j] = ((String[]) propValues.get(j))[i];
									} else {
										row[j] = null;
									}
								}
								String iniTwoQty = null;
								if (IniTwoQty != null && IniTwoQty.length > 0) {
									iniTwoQty = IniTwoQty[i];
								}
								String stockLoc=null;
								if(StockLocation!=null){
									stockLoc=StockLocation[i];
								}else{
									stockLoc="";
								}
								String iniSecUnit=null;
								if(secUnit!=null&&secUnit.length>0){
									iniSecUnit=secUnit[i];
								}
								String iniConv=null;
								if(conversion!=null&&conversion.length>0){
									iniConv=conversion[i];
								}
								String iniSecUnitQty=null;
								if(secUnitQty!=null&&secUnitQty.length>0){
									iniSecUnitQty=secUnitQty[i];
								}
								String iniSecUnitPrice=null;
								if(secUnitPrice!=null&&secUnitPrice.length>0){
									iniSecUnitPrice=secUnitPrice[i];
								}
								String iniProvider="";
								if(provider!=null&&provider.length>0){
									iniProvider=provider[i];
								}
								String GoodsCostMethod=BaseEnv.systemSet.get("GoodsCostMethod").getSetting();
								rs1=addIniStockDet(existPropNames,row,iniTwoQty,sunCompany,goodsCode[i],stockCode[i],"",Double.valueOf(iniQty[i]),Double.valueOf(iniPrice[i]),Double.valueOf(iniAmount[i]),createBy,stockLoc,conn,notUsedPropNames,GoodsCostMethod,iniSecUnit,iniConv,iniSecUnitQty,iniSecUnitPrice,iniProvider,existNVNames,nvRow,locale);
						        if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
						        	rs.setRetCode(rs1.getRetCode());
						        	rs.setRetVal(rs1.getRetVal());
						        	return ;
						        }
						        String [] last=new String[]{"1",iniTwoQty,iniQty[i],iniPrice[i],iniAmount[i]};
						        rs1=updateEditOne(existPropNames,row,iniTwoQty,sunCompany,goodsCode[i],stockCode[i],"",Double.valueOf(iniQty[i]),Double.valueOf(iniPrice[i]),Double.valueOf(iniAmount[i]),createBy,stockLoc,conn,notUsedPropNames,seqPropfName,isUpdateLast[i],GoodsCostMethod,iniProvider,last,locale);
						        if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
						        	rs.setRetCode(rs1.getRetCode());
						        	rs.setRetVal(rs1.getRetVal());
						        	return ;
						        }
								
								rs1=upStockAndOther(existPropNames,conn,goodsCode[i],stockCode[i],sunCompany,negativeStock);
								if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
									rs.setRetCode(rs1.getRetCode());
						        	rs.setRetVal(rs1.getRetVal());
							        return ;
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
		rs.setRetVal(System.currentTimeMillis());
		System.out.print("返回数据方法2："
				+ (System.currentTimeMillis() - Long.parseLong(rs.getRetVal()
						.toString())));
		return rs;
	}

//	public Result updateEditOne(List existPropNames, String[] propValues,
//			String iniTwoQty, String sunCompany, String keyId,
//			String goodsCode, String stockCode, float iniQtyS, float iniPriceS,
//			float iniAmountS, boolean negativeStock, String createBy,
//			Connection conn,List notUsedPropNames) {
//		Result rs = new Result();
//		try {
//			Statement cs = conn.createStatement();
//			ResultSet rss = null;
//			boolean isUpdateLast = false;
//			String queryList = "";
//			for (int i = 0; i < existPropNames.size(); i++) {
//				String[] prop = (String[]) existPropNames.get(i);
//				if (prop[2].equals("1")) {
//					if (propValues[i] != null && propValues[i].length() > 0) {
//						queryList += " and " + prop[0] + "='" + propValues[i]
//								+ "'";
//					} else {
//						queryList += " and " + prop[0] + "=''";
//					}
//				}
//			}
//			// 查询以后期间是否有此商品的数据，如果有更新以后的结存数，如果没有，直接执行操作
//			String sql = "select count(*) from tblStockDet where GoodsCode='"
//					+ goodsCode + "' and StockCode='" + stockCode
//					+ "' and id<>'" + keyId + "' " + queryList;
//			rss = cs.executeQuery(sql);
//			if (rss.next() && rss.getInt(1) > 0) {
//				isUpdateLast = true;
//			}
//			if (isUpdateLast) {
//				CallableStatement cbsDelStockDet = conn
//						.prepareCall("{call proc_delStockDet(?,?,?,?,?)}");
//				cbsDelStockDet.registerOutParameter(4, java.sql.Types.INTEGER);
//				cbsDelStockDet.registerOutParameter(5, java.sql.Types.VARCHAR);
//				cbsDelStockDet.setString(1, keyId);
//				cbsDelStockDet.setString(2, "tblStockDet");
//				cbsDelStockDet.setString(3, "1");
//				cbsDelStockDet.execute();
//			} else {
//				cs.execute("delete from tblStockDet where id='" + keyId + "'");
//			}
//
//			String newSql = "insert into tblStockDet(id,period,periodYear,periodMonth,BillDate,GoodsCode,IniQty,IniPrice,IniAmount,InstoreQty,"
//					+ "InstorePrice,InstoreAmount,createBy,lastUpdateBy,createTime,lastUpdateTime,"
//					+ "statusId,unit,ItemNo,BillType,StockCode,BillID,SCompanyID,TotalQty,TotalAmt,LastPrice,InstoreTwoQty,IniTwoQty,LastTwoQty";
//
//			for (int i = 0; i < existPropNames.size(); i++) {
//				newSql += "," + ((String[]) existPropNames.get(i))[0];
//			}
//			for(int k=0;k<notUsedPropNames.size();k++){
//				newSql += "," +notUsedPropNames.get(k);
//			}
//
//			newSql += ") values ";
//			String id = IDGenerater.getId();
//			String createTime = BaseDateFormat.format(new Date(),
//					BaseDateFormat.yyyyMMddHHmmss);
//			String valueSql = "('" + id + "',-1,-1,-1,'','" + goodsCode
//					+ "',0,0,0," + iniQtyS + "," + iniPriceS + "," + iniAmountS
//					+ ",'" + createBy + "','" + createBy + "','" + createTime
//					+ "','" + createTime + "',0,'',1,'tblStockDet','"
//					+ stockCode + "','" + id + "','" + sunCompany + "',"
//					+ iniQtyS + "," + iniAmountS + "," + iniPriceS;
//			// 如果没有双单位，则双单位数量为1
//			if (iniTwoQty == null) {
//				valueSql += ",1,0,1";
//			} else {
//				valueSql += "," + iniTwoQty + ",0," + iniTwoQty;
//			}
//
//			for (int k = 0; k < propValues.length; k++) {
//				if (propValues[k] != null) {
//					valueSql += ",'" + propValues[k].trim() + "'";
//				} else {
//					valueSql += ",''";
//				}
//			}
//			for(int k=0;k<notUsedPropNames.size();k++){
//				valueSql += ",''";
//			}
//			valueSql += ")";
//			String execSql = newSql + valueSql;
//			cs.execute(execSql);
//
//			if (isUpdateLast) {
//				String declareList_att = "";
//				String giveList_att = "";
//				String queryList_att = "";
//				String propName = "";
//				String condiNotCal_prop = "";
//				String notIsCalculate = "";
//				for (int i = 0; i < existPropNames.size(); i++) {
//					String[] prop = (String[]) existPropNames.get(i);
//					propName = prop[0];
//					declareList_att += " declare @" + propName + " varchar(50)";
//					giveList_att += ",@" + propName + "=" + propName;
//					queryList_att += " and " + propName + "=@" + propName;
//					if (prop[2].equals("1")) {
//						condiNotCal_prop += " and " + propName + "=@"
//								+ propName;
//					} else {
//						notIsCalculate += "," + propName;
//					}
//				}
//
//				if (giveList_att.length() > 0) {
//					giveList_att = giveList_att.substring(1);
//				}
//				if (notIsCalculate.length() > 0) {
//					notIsCalculate = notIsCalculate.substring(1);
//				}
//				CallableStatement cbsUpdateLastPrice = conn
//						.prepareCall("{call proc_updateLastPrice(?,?,?,?,?,?,?)}");
//				// 更新出入库明细最后结存数
//				cbsUpdateLastPrice.setString(1, id);
//				cbsUpdateLastPrice.setInt(2, 1);
//				cbsUpdateLastPrice.setString(3, declareList_att);
//				cbsUpdateLastPrice.setString(4, giveList_att);
//				cbsUpdateLastPrice.setString(5, queryList_att);
//				cbsUpdateLastPrice.setString(6, condiNotCal_prop);
//				cbsUpdateLastPrice.setString(7, notIsCalculate);
//				cbsUpdateLastPrice.execute();
//			}
//
//			CallableStatement cbsUpdateStocks = conn
//					.prepareCall("{call proc_updateStocks(?,?)}");
//			// 更新分仓库存表数据(其中更新了父类数据)
//			cbsUpdateStocks.setString(1, id);
//			cbsUpdateStocks.setString(2, "add");
//			cbsUpdateStocks.execute();
//			
//			//更新仓库总表
//			CallableStatement cbsUpdateStockTotal = conn.prepareCall("{call proc_updateStockTotal(?,?)}");
//			cbsUpdateStockTotal.setString(1, goodsCode);
//			cbsUpdateStockTotal.setString(2, sunCompany);
//			cbsUpdateStockTotal.execute();
//			
//			CallableStatement cbs = conn
//					.prepareCall("{call proc_updateSuper(?,?,?,?,?,?,?,?)}");
//			cbs.registerOutParameter(7, java.sql.Types.INTEGER);
//			cbs.registerOutParameter(8, java.sql.Types.VARCHAR);
//			String query = "";
//			String field = "";
//			// 查询修改的商品的会计科目
//			String accCode = "";
//			sql = "select AccCode from tblGoods where classCode='" + goodsCode
//					+ "'";
//			rss = cs.executeQuery(sql);
//			if (rss.next()) {
//				accCode = rss.getString(1);
//			}
//			// 修改商品对应科目的科目余额，科目表
//			sql = "select sum(IniAmount) from tblstocks where period='-1' and len(GoodsCode)=5 "
//					+ "and (select AccCode from tblGoods where classCode=tblStocks.GoodsCode)='"
//					+ accCode + "' and SCompanyID='" + sunCompany + "'";
//			String iniAmount = "0";
//			rss = cs.executeQuery(sql);
//			if (rss.next()) {
//				iniAmount = rss.getString(1);
//			}
//
//			StringBuffer upAccBal = new StringBuffer(""); // 科目余额表SQL
//
//			upAccBal.append(" update tblAccBalance set ");
//			upAccBal.append(" CurrYIniBase=").append(iniAmount).append(
//					",CurrYIniDebitSumBase=0");
//			upAccBal.append(",CurrYIniCreditSumBase=0").append(
//					",CurrYIniBalaBase=").append(iniAmount);
//			upAccBal.append(" where SubCode='" + accCode + "' and Period=-1");
//			upAccBal.append(" and SCompanyID='" + sunCompany + "'");
//			cs.execute(upAccBal.toString());
//
//			rss = cs
//					.executeQuery("select classCode from tblAccTypeInfo where AccNumber='"
//							+ accCode + "' and SCompanyID='" + sunCompany + "'");
//			String accClass = "";
//			if (rss.next()) {
//				accClass = rss.getString(1);
//				// 修改科目余额表中父类
//				cbs.setString(1, "tblAccBalance");
//				cbs.setString(2, "tblAccTypeInfo");
//				cbs
//						.setString(
//								3,
//								"tblAccBalance.subCode=tblAccTypeInfo.accNumber and tblAccTypeInfo.SCompanyID='"
//										+ sunCompany
//										+ "' and tblAccBalance.SCompanyID='"
//										+ sunCompany + "'");
//
//				query = "period=-1 ";
//
//				cbs.setString(4, query);
//				cbs
//						.setString(
//								5,
//								"currYIniBase=sum(currYIniBase)@SPFieldLink:currYIniDebitSumBase=sum(currYIniDebitSumBase)@SPFieldLink:CurrYIniCreditSumBase=sum(CurrYIniCreditSumBase)");
//				cbs.setString(6, accClass);
//				cbs.execute();
//			}
//			if (sunCompany.length() > 5) {
//				// 更新父类分支结构会计科目余额
//				GlobalMgt mgt = new GlobalMgt();
//				mgt.updateAccParentSum(conn, accClass, sunCompany, -1, -1);
//			}
//
//			// 修改商品期初完毕，如果不允许负库存过账，执行负库存验证
//			if (!negativeStock) {
//				Result res = negativeStockProc(conn, sunCompany);
//				if (res.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR) {
//					rs.setRetCode(res.getRetCode());
//					rs.setRetVal(res.getRetVal());
//				}
//			}
//
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//
//		}
//		return rs;
//	}
	/**
	 * 删除出入库明细数据
	 */
	private Result delStockDet(String goodsCode,String stockCode,List existPropNames,Connection con){
		Result rs=new Result();
		try {
			String sql="select id from tblStockDet where GoodsCode=? and stockCode=? and period=-1";
			PreparedStatement cs = con.prepareStatement(sql);
			cs.setString(1,goodsCode);
			cs.setString(2, stockCode);
			ResultSet rss = null;
			rss = cs.executeQuery();
			ArrayList ids = new ArrayList();
			while (rss.next()) {
				ids.add(rss.getString(1));
			}
			boolean isUpdateLast = false;
			if(ids.size()>0){
				
				// 查询以后期间是否有此商品的数据，如果有更新以后的结存数，如果没有，直接执行操作
			    sql = "select count(*) from tblStockDet where GoodsCode=? and StockCode=? and period>-1";
			    cs=con.prepareStatement(sql);
			    cs.setString(1, goodsCode);
			    cs.setString(2, stockCode);
				rss = cs.executeQuery();
				if (rss.next() && rss.getInt(1) > 0) {
					isUpdateLast = true;
				}
				if (isUpdateLast) {
					CallableStatement cbsDelStockDet = con
					.prepareCall("{call proc_delStockDet(?,?,?,?,?)}");
			cbsDelStockDet.registerOutParameter(4,
					java.sql.Types.INTEGER);
			cbsDelStockDet.registerOutParameter(5,
					java.sql.Types.VARCHAR);

					for (int i = 0; i < ids.size(); i++) {
						cbsDelStockDet.setString(1, ids.get(i)
								.toString());
						cbsDelStockDet.setString(2, "tblStockDet");
						cbsDelStockDet.setString(3, "1");
						cbsDelStockDet.execute();
					}
				}else{
					sql="delete from tblStockDet where GoodsCode=? and stockCode=? and period=-1";
					cs=con.prepareStatement(sql);
					cs.setString(1, goodsCode);
					cs.setString(2, stockCode);
					cs.executeUpdate();
					sql="update tblStocks set IniTwoQty=0,qty=0,iniprice=0,iniAmount=0,LastTwoQty=0,lastqty=0,lastprice=0,lastAmount=0 where  GoodsCode=? and stockCode=?";
					cs=con.prepareStatement(sql);
					cs.setString(1, goodsCode);
					cs.setString(2, stockCode);
					cs.executeUpdate();
				}
				if (existPropNames.size() > 0) {// 删除分仓库存多余数据
					Statement stmt=con.createStatement();
					List valueList = new ArrayList();
					String otherSql = "select id";
					for (int p = 0; p < existPropNames.size(); p++) {
						otherSql += ","
								+ ((String[]) existPropNames.get(p))[0];
					}
					otherSql += " from tblStocks where goodsCode='"
							+ goodsCode + "' and stockCode='"
							+ stockCode + "' and period=-1";
					ResultSet others = stmt.executeQuery(otherSql);
					while (others.next()) {
						String[] values = new String[existPropNames
								.size() + 1];
						for (int k = 0; k < existPropNames.size(); k++) {
							values[k] = others
									.getString(((String[]) existPropNames
											.get(k))[0]);
						}
						values[values.length - 1] = others
								.getString("id");
						valueList.add(values);
					}
					others.close();
					for (int q = 0; q < valueList.size(); q++) {
						String queryStockdet = "select count(*) from tblStockdet where ";
						String[] values = (String[]) valueList
								.get(q);
						for (int o = 0; o < existPropNames.size(); o++) {
							queryStockdet += ((String[]) existPropNames
									.get(o))[0]
									+ "='" + values[o] + "' and ";
						}
						queryStockdet += "stockCode='" + stockCode
								+ "' and goodsCode='" + goodsCode
								+ "' and period=-1";
						others = stmt.executeQuery(queryStockdet);
						int num = 0;
						if (others.next()) {
							num = others.getInt(1);
						}
						if (num == 0) {
							String detId = values[values.length - 1];
							String delStocksSql = "delete from tblStocks where id='"
									+ detId + "'";
							stmt.executeUpdate(delStocksSql);
						}
					}
				}
				
			}
			rs.setRetVal(isUpdateLast);
		} catch (SQLException e) {
			e.printStackTrace();
			rs.setRetVal(ErrorCanst.EXECUTE_DB_ERROR);
		}
		return rs;
	}
	/**
	 * 删除库存明细临时表(tblIniStockDet)数据
	 * @param goodsCode
	 * @param stockCode
	 * @param conn
	 * @return
	 */
	private Result delIniStockDet(String goodsCode,String stockCode,Connection conn){
		Result rs=new Result();
		try {
			Statement cs = conn.createStatement();
			String delStocksSql="delete from tblIniStockDet where GoodsCode='"+ goodsCode+ "' and stockCode='"+ stockCode + "' and period=-1";
			cs.executeUpdate(delStocksSql);
		} catch (SQLException e) {
			e.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return rs;
	}
	/**
	 * 向库存明细临时表新增数据
	 * @param existPropNames
	 * @param propValues
	 * @param iniTwoQty
	 * @param sunCompany
	 * @param goodsCode
	 * @param stockCode
	 * @param iniQtyS
	 * @param iniPriceS
	 * @param iniAmountS
	 * @param createBy
	 * @param stockLocation
	 * @param conn
	 * @param notUsedPropNames
	 * @return
	 */
    private Result addIniStockDet(List existPropNames, String[] propValues,
			String iniTwoQty, String sunCompany,
			String goodsCode, String stockCode,String inputType, double iniQtyS, double iniPriceS,
			double iniAmountS,String createBy,String stockLocation,
			Connection conn,List notUsedPropNames,String GoodsCostMethod,String iniSecUnit,String iniConv,
			String iniSecUnitQty,String iniSecUnitPrice,String iniCompanyCode,
			List existNVNames,String[]nvRow,String locale){
    	Result rs=new Result();
    	try {
			Statement cs = conn.createStatement();
			//如果存在名称&值属性
			if(existNVNames.size()!=0){
				String goodsId="";
				String goodsFullName="";
				String tSql="select Id,goodsFullName from tblGoods where classCode='"+goodsCode+"'";
				ResultSet rss=cs.executeQuery(tSql);
				if(rss.next()){
					goodsId=rss.getString("Id");
					goodsFullName=rss.getString("goodsFullName");
				}
				for(int i=0;i<existPropNames.size();i++){
					GoodsPropInfoBean gpInfo= GlobalsTool.getPropBean(((String[])existPropNames.get(i))[0].toString());
					String display="";
					boolean isExist=false;
					for(int j=0;j<existNVNames.size();j++){
						if(existNVNames.get(j).equals(((String[])existPropNames.get(i))[0].toString()+"NV")){
							display=nvRow[j];
							isExist=true;
							break;
						}
					}
					if(isExist&&!propValues[i].equals("")){
						if("".equals(display)){//如果没录入属性值显示名
							tSql="select "+locale+" from tblLanguage where id=(select languageid from tblDBFieldInfo where fieldName='"+(((String[])existPropNames.get(i))[0].toString()+"NV")+"' and tableid=(select id from tblDBTableInfo where tableName='tblStockDet'))";
							rss=cs.executeQuery(tSql);
							if(rss.next()){
								rs.setRetCode(ErrorCanst.GOODS_INI_PROPDISPLAY);
								rs.setRetVal(rss.getString(1));
								return rs;
							}
						}
						
						tSql="select b.PropItemID from tblGoodsOfProp a left join tblGoodsOfPropDet b on a.id=b.f_ref left join tblLanguage c on c.id=b.languageId "+  
                        "where a.goodsCode='"+goodsCode+"' and a.PropID='"+gpInfo.getId()+"' and c.zh_CN='"+display+"'";
						rss=cs.executeQuery(tSql);
						if(rss.next()){
							if(!propValues[i].equals(rss.getString(1))){
								rs.setRetCode(ErrorCanst.GOODS_INI_MULTI_VAL);
								String[] str=new String[3];
								str[0]=goodsFullName;
								str[1]=gpInfo.getDisplay().get(locale);
								str[2]=display;
								rs.setRetVal(str);
								return rs;
							}
						}
					
						tSql="select count(*) from tblGoodsOfProp a left join tblGoodsOfPropDet b on a.id=b.f_ref where a.goodsCode='"+goodsCode+"' and a.PropID='"+gpInfo.getId()+"' and b.PropItemID='"+propValues[i]+"'";//如果商品、属性、属性值不存在
						rss=cs.executeQuery(tSql);
						if(rss.next()){
							if(Integer.parseInt(rss.getString(1))==0){
								String createTime = BaseDateFormat.format(new Date(),
										BaseDateFormat.yyyyMMddHHmmss);
								String lgId=IDGenerater.getId();
								tSql="insert into tblLanguage(id,zh_CN,zh_TW,zh_HK,en) values('"+lgId+"','"+display+"','"+display+"','"+display+"','"+display+"')";
								cs.executeUpdate(tSql);
								String goodsPropId="";
								tSql="select id from tblGoodsOfProp where Goodscode='"+goodsCode+"'";
								rss=cs.executeQuery(tSql);
								if(rss.next()){
									goodsPropId=rss.getString("id");
								}else{
								goodsPropId=IDGenerater.getId();
								tSql="insert into tblGoodsOfProp(id,GoodsCode,createBy,lastUpdateBy,createTime,lastUpdateTime,SCompanyID,f_ref,PropID) values('"+goodsPropId+"','"+goodsCode+"','"+createBy+"','"+createBy+"','"+createTime+"','"+createTime+"','"+sunCompany+"','"+goodsId+"','"+gpInfo.getId()+"')";
								cs.executeUpdate(tSql);
								}
								tSql="insert into tblGoodsOfPropdet(id,f_ref,PropItemID,languageId,SCompanyID,PropItemName) values('"+IDGenerater.getId()+"','"+goodsPropId+"','"+propValues[i]+"','"+lgId+"','"+sunCompany+"','"+display+"')";
								cs.executeUpdate(tSql);
//								tSql="insert into tblGoodsPropEnumItem(id,SCompanyID,enumValue,propId,isUsed,languageId) values('"+IDGenerater.getId()+"','"+sunCompany+"','"+propValues[i]+"','"+gpInfo.getId()+"','1','"+lgId+"')";
//								cs.executeUpdate(tSql);
							}else{
								tSql="select c."+locale+" from tblGoodsOfProp a left join tblGoodsOfPropDet b on a.id=b.f_ref left join tblLanguage c on b.languageId=c.id"+
                                     " where a.goodsCode='"+goodsCode+"' and a.PropID='"+gpInfo.getId()+"' and b.PropItemID='"+propValues[i]+"'";
								rss=cs.executeQuery(tSql);
								if(rss.next()){
									if(!display.equals(rss.getString(1))){
										rs.setRetCode(ErrorCanst.GOODS_INI_MULTI);
										String[] str=new String[3];
										str[0]=goodsFullName;
										str[1]=gpInfo.getDisplay().get(locale);
										str[2]=propValues[i];
										rs.setRetVal(str);
										return rs;
									}
								}
						
							}
						}

					}
				}
			}
//			考虑到序列号的使用
		   String iniStocksSql = "insert into tblIniStockDet(id,period,periodYear,periodMonth,BillDate,GoodsCode,IniQty,IniPrice,IniAmount,InstoreQty,"
				+ "InstorePrice,InstoreAmount,createBy,lastUpdateBy,createTime,lastUpdateTime,"
				+ "statusId,unit,ItemNo,BillType,StockCode,BillID,SCompanyID,TotalQty,LastPrice,TotalAmt,InstoreTwoQty,IniTwoQty,LastTwoQty,StockLocation,BillNo,CompanyCode";
		    for (int i = 0; i < existPropNames.size(); i++) {
				iniStocksSql += ","
						+ ((String[]) existPropNames.get(i))[0];
			}
		    for(int i=0;i<existNVNames.size();i++){
		    	iniStocksSql += ","
					+ existNVNames.get(i);
		    }
			for(int j=0;j<notUsedPropNames.size();j++){
				GoodsPropInfoBean bean=(GoodsPropInfoBean)notUsedPropNames.get(j);
				iniStocksSql += ","+bean.getPropName();
			}
			if(BaseEnv.systemSet.get("AssUnit").getSetting().equals("true")){//启用辅助单位
				iniStocksSql+=",SecUnit,ConversionRate,SecUnitQty,SecUnitPrice";
			}
			iniStocksSql += ") values ";
			//如果是个别指定法，并且双单位数量大于1 ，则将多条拆分成一条
			float twoQty=1;
			if(GoodsCostMethod.equals("3")&&iniTwoQty!=null&&Float.parseFloat(iniTwoQty)>1&&!inputType.equals("Total")){
				twoQty=Float.parseFloat(iniTwoQty);
				iniQtyS=GlobalsTool.round(iniQtyS/twoQty,GlobalsTool.getDigitsOrSysSetting("tblStockDet", "InstoreQty"));
				iniAmountS=GlobalsTool.round(iniQtyS*iniPriceS,GlobalsTool.getDigitsOrSysSetting("tblStockDet", "InstoreAmount"));
				iniTwoQty="1";				
			}
			for(int i=0;i<twoQty;i++){
				String id = IDGenerater.getId();
				String createTime = BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss);
				String valueSql = "('" + id + "',-1,-1,-1,'','" + goodsCode
						+ "',0,0,0," + iniQtyS + "," + iniPriceS + "," + iniAmountS
						+ ",'" + createBy + "','" + createBy + "','" + createTime
						+ "','" + createTime + "',0,'',1,'tblStockDet','"
						+ stockCode + "','" + id + "','" + sunCompany + "',"
						+ iniQtyS + "," +iniPriceS+ "," + iniAmountS;
				// 如果没有双单位，则双单位数量为1
				if (iniTwoQty == null) {
					valueSql += ",1,0,1";
				} else {
					valueSql += "," + iniTwoQty + ",0," + iniTwoQty;
				}
				valueSql+=",'"+stockLocation+"','-1','"+iniCompanyCode+"'";
				for (int k = 0; k < propValues.length; k++) {
					if (propValues[k] != null) {
						valueSql += ",'" + propValues[k].trim() + "'";
					} else {
						valueSql += ",''";
					}
				}
				for(int k=0;k<nvRow.length;k++){
					valueSql+=",'"+nvRow[k]+"'";
				}
				for(int k=0;k<notUsedPropNames.size();k++){
					GoodsPropInfoBean bean=(GoodsPropInfoBean)notUsedPropNames.get(k);
					if(GoodsCostMethod.equals("3")&&bean.getSepAppoint()==1&&!inputType.equals("Total")){
						valueSql+=",'"+id+"'";
					}else{
						valueSql += ",''";
					}
				}
				if(BaseEnv.systemSet.get("AssUnit").getSetting().equals("true")){//启用辅助单位
					valueSql+=",'"+(iniSecUnit=iniSecUnit==null?"":iniSecUnit)+"',"+(iniConv=iniConv==null?"":iniConv)+","+(iniSecUnitQty=iniSecUnitQty==null?"":iniSecUnitQty)+","+(iniSecUnitPrice=iniSecUnitPrice==null?"":iniSecUnitPrice);
				}
				valueSql += ")";
				String execSql = iniStocksSql + valueSql;
				cs.execute(execSql);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
		}
    	return rs;
    }
    /**
     * 修改库存明细
     * @param existPropNames
     * @param propValues
     * @param iniTwoQty
     * @param sunCompany
     * @param goodsCode
     * @param stockCode
     * @param iniQtyS
     * @param iniPriceS
     * @param iniAmountS
     * @param createBy
     * @param stockLocation
     * @param conn
     * @param notUsedPropNames
     * @param seqPropfName
     * @param isUpdateLast
     * @return
     */
    public Result updateEditOne(List existPropNames, String[] propValues,
			String iniTwoQty, String sunCompany,
			String goodsCode, String stockCode,String inputType, double iniQtyS, double iniPriceS,
			double iniAmountS,String createBy,String stockLocation,
			Connection conn,List notUsedPropNames,String seqPropfName,boolean isUpdateLast,String GoodsCostMethod,String iniCompanyCode,String []last,String local) {
		Result rs = new Result();
		try {
			Statement cs = conn.createStatement();
			String createTime = BaseDateFormat.format(
					new Date(), BaseDateFormat.yyyyMMddHHmmss);
			String declareList_att = "";
			String giveList_att = "";
			String queryList_att = "";
			String propName = "";
			String condiNotCal_prop = "";
			String notIsCalculate = "";
			String seqAppoint="";//个别在指定属性字段
			for (int i = 0; i < existPropNames.size(); i++) {
				String[] prop = (String[]) existPropNames
						.get(i);
				propName = prop[0];
				declareList_att += " declare @" + propName
						+ " varchar(50)";
				giveList_att += ",@" + propName + "="
						+ propName;
				queryList_att += " and " + propName + "=@"
						+ propName;
				if (prop[2].equals("1")) {
					condiNotCal_prop += " and " + propName
							+ "=@" + propName;
				} else {
					notIsCalculate += "," + propName;
				}
			}

			if (giveList_att.length() > 0) {
				giveList_att = giveList_att.substring(1);
			}
			if (notIsCalculate.length() > 0) {
				notIsCalculate = notIsCalculate.substring(1);
			}

			String newSql = "insert into tblStockDet(period,periodYear,periodMonth,BillDate,GoodsCode,IniQty,IniPrice,IniAmount,InstoreQty,"
					+ "InstorePrice,InstoreAmount,createBy,lastUpdateBy,createTime,lastUpdateTime,"
					+ "statusId,unit,ItemNo,BillType,StockCode,SCompanyID,TotalQty,LastPrice,TotalAmt,InstoreTwoQty,IniTwoQty,LastTwoQty,StockLocation,BillNo,CompanyCode";
            int index_seq=-1;//记录序列号属性字段下标
            for(int j=0;j<notUsedPropNames.size();j++){
            	GoodsPropInfoBean bean=(GoodsPropInfoBean)notUsedPropNames.get(j);
            	if(bean.getSepAppoint()!=1){
            		newSql += ","+bean.getPropName();
            	}
			}
			for (int i = 0; i < existPropNames.size(); i++) {
				newSql += ","
						+ ((String[]) existPropNames.get(i))[0];
				if(seqPropfName.equals(((String[]) existPropNames.get(i))[0])){
					index_seq=i;
				}
			}
			for(int j=0;j<notUsedPropNames.size();j++){
            	GoodsPropInfoBean bean=(GoodsPropInfoBean)notUsedPropNames.get(j);
            	if(bean.getSepAppoint()==1){
            		newSql += ","+bean.getPropName();
            		seqAppoint=bean.getPropName();
            	}
			}
			newSql += ",id,BillID) values ";
			String seqStr="";
			String propValSql="";
			for (int k = 0; k < propValues.length; k++) {
				if (propValues[k] != null) {
					propValSql += ",'" + propValues[k].trim() + "'";
					if(index_seq==k){
						seqStr=propValues[k].trim();
					}
				} else {
					propValSql += ",''";
				}
			}
			//如果是个别指定法，并且双单位数量大于1 ，则将多条拆分成一条
			float twoQty=1;
			if(GoodsCostMethod.equals("3")&&iniTwoQty!=null&&Double.parseDouble(iniTwoQty)>1&&!inputType.equals("Total")){
				twoQty=Float.parseFloat(iniTwoQty);
				iniQtyS=GlobalsTool.round(iniQtyS/twoQty,GlobalsTool.getDigitsOrSysSetting("tblStockDet", "InstoreQty"));
				iniAmountS=GlobalsTool.round(iniQtyS*iniPriceS,GlobalsTool.getDigitsOrSysSetting("tblStockDet", "InstoreAmount"));
				iniTwoQty="1";
				last[1]=iniTwoQty;
				last[2]=String.valueOf(iniQtyS);
				last[3]=String.valueOf(iniPriceS);
				last[4]=String.valueOf(iniAmountS);
			}
			for(int i=0;i<twoQty;i++){
				double iniNewQty=iniQtyS;
				double iniNewAmount=iniAmountS;
				if(index_seq!=-1&&iniNewQty>0&&seqStr.length()>0)
				{
					iniNewQty=1;
					iniNewAmount=iniPriceS;
					last[2]=String.valueOf(iniNewQty);
					last[3]=String.valueOf(iniPriceS);
					last[4]=String.valueOf(iniNewAmount);
				}
				String valueSql = "(-1,-1,-1,'','"
						+ goodsCode + "',0,0,0," + iniNewQty
						+ "," + iniPriceS + ","
						+ iniNewAmount + ",'" + createBy
						+ "','" + createBy + "','" + createTime
						+ "','" + createTime
						+ "',0,'',"+last[0]+",'tblStockDet','"
						+ stockCode + "','"
						+ sunCompany + "'," + last[2] + ","
						+ last[3] + "," + last[4];
				
//				 如果没有双单位，则双单位数量为1
				if (iniTwoQty == null) {
					valueSql += ",1,0,1";
				} else {
					valueSql += "," + iniTwoQty + ",0," + last[1];
				}
				valueSql+=",'"+stockLocation+"','-1','"+iniCompanyCode+"'";
				for(int k=0;k<notUsedPropNames.size();k++){
					GoodsPropInfoBean bean=(GoodsPropInfoBean)notUsedPropNames.get(k);
					if(bean.getSepAppoint()!=1){
						valueSql += ",''";
					}
				}
				valueSql+=propValSql;
				
				String execSql = newSql + valueSql;
				
				if(!"".equals(seqStr)){
					String []insStr=execSql.split(seqStr);
					String[] arr=seqStr.split("~");
					for(int p=0;p<arr.length;p++){
						 String lastSql=insStr[0]+arr[p]+insStr[1];
						  String id = IDGenerater.getId();
			    		   if(GoodsCostMethod.equals("3")){
			    			   lastSql+=",'"+id+"'";
			    		   }				    		   
			    		   lastSql+=",'"+id+"','"+id+"')";
			    		   cs.execute(lastSql);
			    		   rs= upStocksAndLastPrice(existPropNames,conn,id,isUpdateLast,sunCompany,local);
			    			  if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
			    				  return rs;
			    				  
			    			  }   
					}
					/*
					String []insStr=execSql.split(seqStr.replaceAll("[)]", "[)]"));
				       String[] arr=seqStr.split(";");
				       for(int p=0;p<arr.length;p++){
				    	   String []tempArr=arr[p].split(":[)]");
				    	   if(tempArr[1].equals("1")){
				    		   String lastSql=insStr[0]+tempArr[2]+insStr[1];
				    		   String id = IDGenerater.getId();
				    		   if(GoodsCostMethod.equals("3")){
				    			   lastSql+=",'"+id+"'";
				    		   }				    		   
				    		   lastSql+=",'"+id+"','"+id+"')";
				    		   cs.execute(lastSql);
				    		   rs= upStocksAndLastPrice(existPropNames,conn,id,isUpdateLast,sunCompany);
				    			  if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
				    				  return rs;
				    				  
				    			  }   
				    	   }else{
				    		   String [] area=arr[p].split(":[)]")[2].split("~");
				    		   String startNum=area[2];
				    		   int start_n=Integer.parseInt(startNum);
				    		   if(startNum.length()>String.valueOf(start_n).length()){
								  String zeroStr=startNum.substring(0,startNum.length()-String.valueOf(start_n).length());
				    		      for(int j=0;j<Integer.parseInt(area[3]);j++){
				    		    	 int temp=start_n+j;
						    		String last=zeroStr+temp;
						    		int disc=last.length()-startNum.length();
						    		last=last.substring(disc);
						    		last=area[0]+last+area[1];
				    			   String lastSql=insStr[0]+last+insStr[1];
					    		   String id = IDGenerater.getId();
					    		   if(GoodsCostMethod.equals("3")){
					    			   lastSql+=",'"+id+"'";
					    		   }
					    		   lastSql+=",'"+id+"','"+id+"')";
					    		   cs.execute(lastSql);
					    		   rs= upStocksAndLastPrice(existPropNames,conn,id,isUpdateLast,sunCompany);
				    			  if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
				    				  return rs;
				    			  }
				    		   }
				    		  }else{
				    			  for (int j = 0; j < Integer.parseInt(area[3]); j++) {
										String last=area[0] + (start_n + j)+ area[1];
										 String lastSql=insStr[0]+last+insStr[1];
							    		   String id = IDGenerater.getId();
							    		   if(GoodsCostMethod.equals("3")){
							    			   lastSql+=",'"+id+"'";
							    		   }
							    		   lastSql+=",'"+id+"','"+id+"')";
							    		   cs.execute(lastSql);
							    		   rs= upStocksAndLastPrice(existPropNames,conn,id,isUpdateLast,sunCompany);
						    			  if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
						    				  return rs;
						    			  }
									}
				    		  }
				    		   
				    	   }
				       }
				       */
				}else{
					String id = IDGenerater.getId();
					if(GoodsCostMethod.equals("3")){
						if(!inputType.equals("Total")){
						     execSql+=",'"+id+"'";
						}else{
				    	     execSql+=",''";
						}
		    		}
					execSql += ",'"+id+"','"+id+"')";
					cs.execute(execSql);
					if(GoodsCostMethod.equals("3")&&!seqAppoint.equals("")){
						existPropNames.add(new String[]{seqAppoint,"","1"});
					}
					rs= upStocksAndLastPrice(existPropNames,conn,id,isUpdateLast,sunCompany,local);
					if(GoodsCostMethod.equals("3")&&!seqAppoint.equals("")){
					    existPropNames.remove(existPropNames.size()-1);
					}
	    			if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
	    				return rs;
	    			}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return rs;
    }
    /**
     * 修改最后结存和分仓库存
     * @param existPropNames
     * @param conn
     * @param id
     * @param isUpdateLast
     * @return
     */
    private Result upStocksAndLastPrice(List existPropNames,Connection conn,String id,boolean isUpdateLast,String sunCompany,String local){
    	Result rs=new Result();
    	String declareList_att = "";
		String giveList_att = "";
		String queryList_att = "";
		String propName = "";
		String condiNotCal_prop = "";
		String notIsCalculate = "";
		for (int i = 0; i < existPropNames.size(); i++) {
			String[] prop = (String[]) existPropNames
					.get(i);
			propName = prop[0];
			declareList_att += " declare @" + propName
					+ " varchar(50)";
			giveList_att += ",@" + propName + "="
					+ propName;
			queryList_att += " and " + propName + "=@"
					+ propName;
			if (prop[2].equals("1")) {
				condiNotCal_prop += " and " + propName
						+ "=@" + propName;
			} else {
				notIsCalculate += "," + propName;
			}
		}

		if (giveList_att.length() > 0) {
			giveList_att = giveList_att.substring(1);
		}
		if (notIsCalculate.length() > 0) {
			notIsCalculate = notIsCalculate.substring(1);
		}

		try {
			CallableStatement cbsUpdateLastPrice = conn
					.prepareCall("{call proc_updateLastPrice(?,?,?,?,?,?,?)}");
			CallableStatement cbsUpdateStocks = conn
			.prepareCall("{call proc_updateStocks(?,?)}");
			//if(isUpdateLast){
//			 更新出入库明细最后结存数
			cbsUpdateLastPrice.setString(1, id);
			cbsUpdateLastPrice.setInt(2, 1);
			cbsUpdateLastPrice.setString(3,
					declareList_att);
			cbsUpdateLastPrice.setString(4,
					giveList_att);
			cbsUpdateLastPrice.setString(5,
					queryList_att);
			cbsUpdateLastPrice.setString(6,
					condiNotCal_prop);
			cbsUpdateLastPrice.setString(7,
					notIsCalculate);
			cbsUpdateLastPrice.execute();
			
			//}
//			 更新分仓库存表数据(其中更新了父类数据)
			cbsUpdateStocks.setString(1, id);
			cbsUpdateStocks.setString(2, "add");
			cbsUpdateStocks.execute();
			CallableStatement cs = conn
			.prepareCall("{call proc_negativeUpdateInStock(?,?,?,?,?)}");
			//验证负库存过账
			cs.setString(1, local);
		    cs.setString(2,"");
			cs.setString(3, sunCompany);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR, 50);
			cs.execute();
			rs.setRetCode(cs.getInt(4));
			rs.setRetVal(cs.getString(5));
		} catch (SQLException e) {
			e.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
    	return rs;
    }
    /**
     * 修改会计科目余额、父类
     * @param existPropNames
     * @param conn
     * @param goodsCode
     * @param stockCode
     * @param sunCompany
     * @param negativeStock
     * @return
     */
    private Result upStockAndOther(List existPropNames,Connection conn,String goodsCode,String stockCode,String sunCompany,boolean negativeStock){
    	Result rs=new Result();
    	Statement cs;
		try {
			cs = conn.createStatement();
		
    	String sql="";
		
		CallableStatement cbs = conn
				.prepareCall("{call proc_updateSuper(?,?,?,?,?,?,?,?)}");
		cbs.registerOutParameter(7, java.sql.Types.INTEGER);
		cbs.registerOutParameter(8, java.sql.Types.VARCHAR);
		String query = "";
		String field = "";

		// 查询修改的商品的会计科目
		String accCode = "";
		sql = "select AccCode from tblGoods where classCode='"
				+ goodsCode + "'";
		ResultSet rss = cs.executeQuery(sql);
		if (rss.next()) {
			accCode = rss.getString(1);
		}
		// 修改商品对应科目的科目余额，科目表
		sql = "select isnull(sum(InstoreAmount),0) from tblstockDet where period='-1' "
				+ "and (select AccCode from tblGoods where classCode=tblstockDet.GoodsCode and isCatalog=0)='"
				+ accCode
				+ "' and SCompanyID='"
				+ sunCompany + "'";
		String iniAmount = "0";
		rss = cs.executeQuery(sql);
		if (rss.next()) {
			iniAmount =GlobalsTool.formatNumber(rss.getObject(1),false,true,
                    "true".equals(BaseEnv.
                            systemSet.get("intswitch").
                            getSetting()),"tblAccBalance","CurrYIniBase",true);
			iniAmount=iniAmount.replaceAll(",","");
		}

		StringBuffer upAccBal = new StringBuffer(""); // 科目余额表SQL

		upAccBal.append(" update tblAccBalance set ");
		upAccBal.append(" CurrYIniBase=").append(iniAmount)
				.append(",CurrYIniDebitSumBase=0");
		upAccBal.append(",CurrYIniCreditSumBase=0").append(
				",CurrYIniBalaBase=").append(iniAmount);
		upAccBal.append(" where SubCode='" + accCode
				+ "' and Period=-1");
		if (sunCompany != null && sunCompany.length() > 0) {
			upAccBal.append(" and SCompanyID='"
					+ sunCompany + "'");
		}

		cs.execute(upAccBal.toString());

		rss = cs
				.executeQuery("select classCode from tblAccTypeInfo where AccNumber='"
						+ accCode
						+ "' and SCompanyID='"
						+ sunCompany + "'");
		String accClass = "";
		if (rss.next()) {
			accClass = rss.getString(1);
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

			query = "period=-1 ";

			cbs.setString(4, query);
			cbs
					.setString(
							5,
							"currYIniBase=sum(currYIniBase)@SPFieldLink:currYIniDebitSumBase=sum(currYIniDebitSumBase)@SPFieldLink:CurrYIniCreditSumBase=sum(CurrYIniCreditSumBase)");
			cbs.setString(6, accClass);
			cbs.execute();
		}

		if (sunCompany.length() > 5) {
			// 更新父类分支结构的商品金额，会计科目余额
			GlobalMgt mgt = new GlobalMgt();
			mgt.updateAccParentSum(conn, accClass,
					sunCompany, -1, -1);
		}

		rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);

//		 //修改商品期初完毕，如果不允许负库存过账，执行负库存验证
//		if (!negativeStock) {
//			Result res = negativeStockProc(conn, sunCompany);
//			if (res.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR) {
//				rs.setRetCode(res.getRetCode());
//				rs.setRetVal(res.getRetVal());
//			}
//		}
		} catch (SQLException e) {
			e.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
    	return rs;
    }
    public String[] getEqLast(String[] propStrArr,String[] iniTwoQty,String[] iniQty,String[] iniPrice,String[] iniAmount,String GoodsCostMethod,int index,String inputType){
    	String[] res=new String[5];
    	res[0]="1";//itemo
    	if(iniTwoQty!=null){
    	res[1]=iniTwoQty[index];//lastTwoQty
    	}else{
    		res[1]=null;
    	}
    	res[2]=iniQty[index];//lastQty
    	res[3]=iniPrice[index];//lastPrice
    	res[4]=iniAmount[index];//lastAmount
    	if(inputType.equals("Total")){
    		double totalQty=Double.parseDouble(iniQty[index]);
    		double totalAmt=Double.parseDouble(iniAmount[index]);
    		double totalTwoQty=0;
    		if(iniTwoQty!=null){
    		totalTwoQty=Double.parseDouble(iniTwoQty[index]);
    		}
    		double curItemNo=1;
    		for(int i=0;i<index;i++){
    			if(propStrArr[i].equals(propStrArr[index])){//如果属性相同
    				totalQty+=Double.parseDouble(iniQty[i]);
    				totalAmt+=Double.parseDouble(iniAmount[i]);
    				if(iniTwoQty!=null){
    				totalTwoQty+=Double.parseDouble(iniTwoQty[i]);
    				}
    				curItemNo+=1;
    			}
    		}
    		res[2]=String.valueOf(totalQty);
    		res[4]=String.valueOf(totalAmt);
    		if(totalQty==0){
    			res[3]="0";
    	    }else{
    		    res[3]=String.valueOf(new BigDecimal(totalAmt).divide(new BigDecimal(totalQty), GlobalsTool.getDigitsOrSysSetting("tblStockDet", "lastPrice"),RoundingMode.HALF_UP).doubleValue());
    	    }
    		res[0]=String.valueOf(curItemNo);
    		if(iniTwoQty!=null){
    		res[1]=String.valueOf(totalTwoQty);
    		}
    	}
    	return res;
    }
	/**
	 * 修改往来单位期初值
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result 1.先调用存储过程proc_delStockDet删除出入库明细已经存在的此商品的数据
	 *         2.将期初数据保存到出入库明细(tblStockDet)期间为-1 保存到入库数量，入库单据，入库金额
	 *         3.调用存储过程proc_updateLastPrice更新最后结存数及此商品以后的结存数
	 *         4.调用存储过程proc_updateStocks更新分仓库存数据及以后期间的分仓库存数据（同时更新父类数据）
	 *         5.调用存储过程proc_updateStockTotal 更新仓库总表数据（同时更新父类数据及父类分支机构数据）
	 *         6.更新科目余额库存商品或原材料科目余额期初数 7.更新科目余额父类数据 8.验证是否允许负库存过账，如果不允许则保持
	 */
	public Result update(final String sunCompany,final String[] StockLocation, final String goodsCode,
			final String stockCode,final String inputType, final String[] iniQty,
			final String[] iniPrice, final String[] iniAmount,
			final String[] iniTwoQty, final boolean negativeStock,
			final List existPropNames, final List propValues,
			final String createBy,final List notUsedPropNames,final String seqPropfName,
			final String[] secUnit,final String[] conversion,final String[] secUnitQty,final String[]secUnitPrice,final String[]hidProvider,
			final List existNVNames,final List nvValues,final String locale,final String []propStrArr) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Result rs1=delStockDet(goodsCode,stockCode,existPropNames,conn);
							if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
								return ;
							}
							
							boolean isUpdateLast=(Boolean)rs1.getRetVal();
							rs1=delIniStockDet(goodsCode,stockCode,conn);
							if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
								rs.setRetCode(rs1.getRetCode());
								rs.setRetVal(rs1.getRetVal());
								return ;
							}
							String GoodsCostMethod=BaseEnv.systemSet.get("GoodsCostMethod").getSetting();
							int flagCount=0;
							for(int i=0;i<iniQty.length;i++){
								String[] row = new String[existPropNames.size()];
								for (int j = 0; j < existPropNames.size(); j++) {
									if (propValues.get(j) != null) {
										row[j] = ((String[]) propValues.get(j))[i];
									} else {
										row[j] = null;
									}
								}
								String[] nvRow=new String[existNVNames.size()];
								for(int j=0;j<existNVNames.size();j++){
									nvRow[j]=((String[])nvValues.get(j))[i];
								}
								String IniTwoQty = null;
								if (iniTwoQty != null && iniTwoQty.length > 0) {
									IniTwoQty = iniTwoQty[i];
								}
								String iniSecUnit=null;
								if(secUnit!=null&&secUnit.length>0){
									iniSecUnit=secUnit[i];
								}
								String iniConv=null;
								if(conversion!=null&&conversion.length>0){
									iniConv=conversion[i];
								}
								String iniSecUnitQty=null;
								if(secUnitQty!=null&&secUnitQty.length>0){
									iniSecUnitQty=secUnitQty[i];
								}
								String iniSecUnitPrice=null;
								if(secUnitPrice!=null&&secUnitPrice.length>0){
									iniSecUnitPrice=secUnitPrice[i];
								}
								String iniCompanyCode="";
								if(hidProvider!=null&&hidProvider.length>0){
									iniCompanyCode=hidProvider[i];
								}
								if(allEmptyPropVal(existPropNames, row, existNVNames, nvRow)&&allEmptyWippOffProp(IniTwoQty,Float.valueOf(iniQty[i]==""?"0.0":iniQty[i]),Float.valueOf(iniPrice[i]),Float.valueOf(iniAmount[i]),StockLocation==null?"":StockLocation[i],iniSecUnit,iniConv,iniSecUnitQty,iniSecUnitPrice)){
									if(i!=0)continue;
								}else{
									if(!(existPropNames.size()>0)){
										flagCount++;
										if(flagCount>1){
											rs.setRetCode(ErrorCanst.MULTI_VALUE_ERROR);
											return ;
										}
									}
								}
								rs1=addIniStockDet(existPropNames,row,IniTwoQty,sunCompany,goodsCode,stockCode,inputType,Double.valueOf(iniQty[i]==""?"0.0":iniQty[i]),Double.valueOf(iniPrice[i]),Double.valueOf(iniAmount[i]),createBy,StockLocation==null?"":StockLocation[i],conn,notUsedPropNames,GoodsCostMethod,iniSecUnit,iniConv,iniSecUnitQty,iniSecUnitPrice,iniCompanyCode,existNVNames,nvRow,locale);
						        if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
						        	rs.setRetCode(rs1.getRetCode());
						        	rs.setRetVal(rs1.getRetVal());
						        	return ;
						        }
						        String [] last=getEqLast(propStrArr, iniTwoQty, iniQty, iniPrice, iniAmount, GoodsCostMethod, i, inputType);
						        rs1=updateEditOne(existPropNames,row,IniTwoQty,sunCompany,goodsCode,stockCode,inputType,Double.valueOf(iniQty[i]==""?"0.0":iniQty[i]),Double.valueOf(iniPrice[i]),Double.valueOf(iniAmount[i]),createBy,StockLocation==null?"":StockLocation[i],conn,notUsedPropNames,seqPropfName,isUpdateLast,GoodsCostMethod,iniCompanyCode,last,locale);
						        if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
						        	rs.setRetCode(rs1.getRetCode());
						        	rs.setRetVal(rs1.getRetVal());
						        	return ;
						         }
							
								rs1=upStockAndOther(existPropNames,conn,goodsCode,stockCode,sunCompany,negativeStock);
								if(rs1.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
									 rs.setRetCode(rs1.getRetCode());
									 rs.setRetVal(rs1.getRetVal());
							         return ;
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
	 * 检查所有属性值是否为空
	 * @param existPropNames
	 * @param propValues
	 * @param existNVNames
	 * @param nvRow
	 * @return
	 */
	private boolean allEmptyPropVal(List existPropNames,String[]propValues,List existNVNames,String[] nvRow){
		boolean all_emptyPropVal=true;
    	if(existPropNames.size()>0){
    		for(int i=0;i<existPropNames.size();i++){
    			if(propValues[i]!=null&&!propValues[i].equals("")){
    				all_emptyPropVal=false;
    			}
    		}
    		if(!all_emptyPropVal){
    			return false;
    		}
    		if(existNVNames.size()>0){
               for(int i=0;i<existNVNames.size();i++){
            	   if(!nvRow[i].equals("")){
            		   all_emptyPropVal=false;
            	   }
               }
               if(!all_emptyPropVal){
       			return false;
       		 }
    		}
    	}
    	return true;
	}
	/**
	 * 检查除属性外其他项的值
	 * @param iniTwoQty
	 * @param iniQty
	 * @param iniPrice
	 * @param iniAmount
	 * @param StockLocation
	 * @param iniSecUnit
	 * @param iniConv
	 * @param iniSecUnitQty
	 * @param iniSecUnitPrice
	 * @return
	 */
    private boolean allEmptyWippOffProp(String iniTwoQty,Float iniQty,Float iniPrice,Float iniAmount,String StockLocation,String iniSecUnit,String iniConv,String iniSecUnitQty,String iniSecUnitPrice){
    	boolean all_empty=true;
        if(iniTwoQty!=null&&!iniTwoQty.equals("")&&Float.parseFloat(iniTwoQty)!=0.0){
        	return false;
        }
        if(iniQty!=0.0||iniPrice!=0.0||iniAmount!=0.0||!StockLocation.replaceAll(" ", "").equals("")){
        	return false;
        }
        if(iniSecUnit!=null&&!iniSecUnit.replaceAll(" ", "").equals("")){
        	return false;
        }
        if(iniSecUnitPrice!=null&&!iniSecUnitPrice.replaceAll(" ", "").equals("")&&Float.parseFloat(iniSecUnitPrice)!=0.0){
        	return false;
        }
        if(iniSecUnitQty!=null&&!iniSecUnitQty.replaceAll(" ", "").equals("")&&Float.parseFloat(iniSecUnitQty)!=0.0){
        	return false;
        }
    	return all_empty;
    }
	// 执行负库存验证
//	public Result negativeStockProc(Connection conn, String sunCompany)
//			throws SQLException {
//		Result rs = new Result();
//
//		CallableStatement cs = conn
//				.prepareCall("{call proc_negativeUpdateInStock(?,?,?)}");
//		cs.setString(1, sunCompany);
//		cs.registerOutParameter(2, Types.INTEGER);
//		cs.registerOutParameter(3, Types.VARCHAR, 50);
//		cs.execute();
//
//		rs.setRetCode(cs.getInt(2));
//		rs.setRetVal(cs.getString(3));
//
//		return rs;
//	}

	/**
	 * 查询单位期初记录
	 * 
	 * @param name
	 *            String
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return Result
	 */
	public Result query(String sunCompany, String goodsCode, String type,
			Hashtable map, String goodsNo, String goodsName, String stock,
			String stockState, String dimQuery, final int pageNo,
			final int pageSize,final boolean existsTowUnit) {
		
		StringBuffer hql = new StringBuffer("");
		hql.append("  select a.classCode ac,isnull(c.StockCode,b.classCode) as cc,a.GoodsNumber,a.GoodsFullName,b.stockfullname,");
		hql.append(" (select isnull(sum(InstoreQty),0) from tblStockDet e,tblGoods f where e.goodsCode=f.classCode and e.GoodsCode like a.classCode+'%' and f.isCatalog=0 and  e.stockcode=isnull(c.StockCode,b.classCode) and e.period=-1) as qty,");
		hql.append(" (select isnull(sum(InstoreAmount),0) from tblStockDet e,tblGoods f where e.goodsCode=f.classCode and e.GoodsCode like a.classCode+'%' and f.isCatalog=0 and  e.stockcode=isnull(c.StockCode,b.classCode) and e.period=-1)  as IniAmount");
		hql.append("  ,a.isCatalog childCount,a.GoodsSpec,d.UnitName,ROW_NUMBER() over( order by  a.classCode ) as row_id  ");
		if(existsTowUnit){
			hql.append(",(select isnull(sum(InstoreTwoQty),0) from tblStockDet e,tblGoods f where e.goodsCode=f.classCode and e.GoodsCode like a.classCode+'%' and f.isCatalog=0 and  e.stockcode=isnull(c.StockCode,b.classCode) and e.period=-1) as InstoreTwoQty ");
		}
		hql.append(" from tblGoods a left join tblStock b on 1=1 left join tblStockDet c on a.classCode=c.GoodsCode and b.classCode=c.StockCode and c.Period=-1 left join tblUnit d on d.id=a.BaseUnit  ");
        hql.append(" where 1=1 ");
		if (sunCompany != null && sunCompany.length() > 0) {// 如果启用了分支机构，加上分支机构的条件
			hql.append(" and a.SCompanyID like '" + sunCompany + "%'");
		}
		
		if (type == null || type.equals("query") || "back".equals(type) || "next".equals(type) ||type.equals("update")||type.equals("edit")) {
			if (goodsNo != null && goodsNo.length() > 0) {
				hql.append(" and a.goodsNumber like '%" + goodsNo + "%' ");
			}
			if (goodsName != null && goodsName.length() > 0) {
				hql.append(" and a.goodsFullName like '%" + goodsName + "%'");
			}
			
			if (stock != null && stock.length() > 0) {
				if("default".equals(stock)){
					hql.append(" and a.stockcode=b.classcode") ;
				}else if("all".equals(stock)){
					
				}else{
					hql.append(" and  b.classcode = '"+ stock + "'");
				}
				
			}
			if (stockState != null && stockState.length() > 0) {
				hql.append(" and c.StoreStateID=" + stockState);
			}

			String dimQuerySql = "";
			
			if (dimQuery != null && dimQuery.length() > 0) {				
				// 遍历商品表中的所有字段查找与模糊字段相匹配的信息
				DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,
						"tblGoods");
				List list = tableInfo.getFieldInfos();
				for (int i = 0; i < list.size(); i++) {
					DBFieldInfoBean field = (DBFieldInfoBean) list.get(i);
					dimQuerySql += "or a." + field.getFieldName() + " like '%"
							+ dimQuery + "%' ";
				}
			}
			if (dimQuerySql.length() > 0) {
				hql.append(dimQuerySql.replaceFirst("or", " and (") + ")");
			}
		}
		
		System.out.println("type:"+type+"stock:"+stock);
		if (goodsCode.length() == 0 ||"update".equals(type)||"edit".equals(type)) {
			
			if(!"default".equals(stock)){				
				ReportDataMgt reportMgt = new ReportDataMgt();
				String hqlMin = "select min(len(a.classCode)) "
						+ hql.toString().substring(
								hql.toString().lastIndexOf("from"));
				Result rs2 = reportMgt.getMinClassLen(hqlMin, new ArrayList());
				if (rs2.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {					
					return rs2;
				} else {					
					hql.append(" and len(a.classCode)=" + rs2.getRetVal());
				}				
			}else{				
				hql.append(" and a.isCatalog!=1 ");
			}
		} else {
			
			hql.append(" and a.classCode like '" + goodsCode + "_____'");
		}
		hql.append("  and (select count(*) from tblStock  where  classCode like b.classCode+'%')=1 ");
		hql.append(" group by a.classcode,a.isCatalog ,isnull(c.StockCode,b.classCode),b.StockFullName,a.GoodsNumber,a.GoodsFullName,b.stockfullname,a.GoodsSpec,d.UnitName ");
		
		//进行分页sql处理
		String pageSql=hql.toString();	
		final String sql ="select * from ("+pageSql+") tableTemp where row_id  between "+((pageNo-1)*pageSize+1)+" and "+(pageNo*pageSize)+"";		
		final String count="select count(*) from ("+pageSql+") tableTemp ";		
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							
							Statement cs = conn.createStatement();
							ResultSet rss =cs.executeQuery(count);							
							System.out.println(count);
							//设置分页信息
							if(rss.next()){								
								rs.setRealTotal(rss.getInt(1));								
							}
							rs.setPageSize(pageSize);
							int pageNos = pageNo > 0 ? pageNo : 1;
							
							double totalPage = (double) rs.getRealTotal()
							/ (double) rs.getPageSize();
							
							rs.setPageNo(pageNos);
							rs.setTotalPage((int) Math.ceil(totalPage));
							
							
							//查询数据
							rss = cs.executeQuery(sql);
							
							List values = new ArrayList();
							String[] fields = new String[3];
							fields[0] = "Qty";
							fields[1] = "IniAmount";
							while (rss.next()) {
								int count=10;
								if(existsTowUnit){
									count=11;
								}
								Object[] value = new Object[count];
								value[0] = rss.getString(1);
								value[1] = rss.getString(2);
								value[2] = rss.getString(3);
								value[3] = rss.getString(4);
								value[4] = rss.getString(5);
								for (int i = 5; i <= 6; i++) {
									value[i] = rss.getObject(i + 1);
									if (value[i] == null) {
										value[i] = "0";
									}
									value[i] = GlobalsTool.formatNumber(
											value[i], false, true, "true"
													.equals(BaseEnv.systemSet
															.get("intswitch")
															.getSetting()),
											"tblStocks", fields[i - 5], true);

								}
								value[7] = rss.getInt(8);
								value[8] = rss.getString("GoodsSpec");
								value[9] = rss.getString(10);
								if(existsTowUnit){
									value[10] = rss.getObject("InstoreTwoQty");
									if (value[10] == null) {
										value[10] = "0";
									}
									value[10] = GlobalsTool.formatNumber(
											value[10], false, true, "true"
													.equals(BaseEnv.systemSet
															.get("intswitch")
															.getSetting()),
											"tblStocks", "InstoreTwoQty", true);
								}
								values.add(value);
							}
							rs.setRetVal(values);

						} catch (SQLException ex) {
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
	public Result getProvidors(String[] keyIds) {
		StringBuffer hql = new StringBuffer("");
		hql.append(" select isnull(b.companyCode,''),isnull((select ComFullName from tblCompany e where e.classCode=b.companyCode),'') as comFullName");
		hql.append(" from tblGoods a,tblIniStockdet b,tblStock c  ");
		hql.append("  where a.classCode=b.GoodsCode and b.StockCode=c.classCode ");
		hql.append(" and (select count(*) from tblIniStockdet where GoodsCode like b.GoodsCode+'_%')=0 and period=-1  and (");
		for (int i = 0; i < keyIds.length; i++) {
			if (i != keyIds.length - 1) {
				hql.append("(b.GoodsCode='" + keyIds[i].split("@")[0]
						+ "' and b.StockCode='" + keyIds[i].split("@")[1]
						+ "') or ");
			} else {
				hql.append("(b.GoodsCode='" + keyIds[i].split("@")[0]
						+ "' and b.StockCode='" + keyIds[i].split("@")[1]
						+ "') ");
			}
		}
		hql.deleteCharAt(hql.length() - 1);
		hql.append(") ");
		hql.append(" order by a.goodsNumber ");
		final String sql = hql.toString();
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							ResultSet rss = cs.executeQuery(sql);
							List values = new ArrayList();
							while (rss.next()) {
								Object[] value = new Object[2];
								value[0] = rss.getString(1);
								value[1] = rss.getString(2);
								values.add(value);
							}
							rs.setRetVal(values);
						} catch (SQLException ex) {
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
	 * 查询单位期初记录
	 * @param name String
	 * @param pageNo int
	 * @param pageSize int
	 * @return Result
	 */
	public Result getEditList(String[] keyIds, final List existPropNames, final boolean existsTwoUnit) {
		StringBuffer hql = new StringBuffer("");
		hql.append(" select b.id ,a.classCode,a.goodsNumber,a.goodsFullName,c.StockFullName,InstoreQty,InstorePrice,InstoreAmount,c.classCode,a.GoodsSpec,b.StockLocation ");
		
		for (int i = 0; i < existPropNames.size(); i++) {
			hql.append(",b." + ((String[]) existPropNames.get(i))[0]);
		}
		if (existsTwoUnit) {
			hql.append(",b.InstoreTwoQty");
		}
		if (BaseEnv.systemSet.get("AssUnit").getSetting().equals("true")) {// 启用辅助单位
			hql.append(",b.SecUnit,isnull((select UnitName from tblUnit where id=b.secUnit),'') UnitName,b.ConversionRate,b.SecUnitQty,b.SecUnitPrice");
		}
		hql.append(" from tblGoods a,tblIniStockdet b,tblStock c  ");
		hql.append("  where a.classCode=b.GoodsCode and b.StockCode=c.classCode ");
		hql.append(" and a.isCatalog=0 and period=-1  and (");
		
		for (int i = 0; i < keyIds.length; i++) {
			if (i != keyIds.length - 1) {
				hql.append("(b.GoodsCode='" 
						+ keyIds[i].split("@")[0] + "' and b.StockCode='"
						+ keyIds[i].split("@")[1] + "') or ");
			} else {
				hql.append("(b.GoodsCode='" 
						+ keyIds[i].split("@")[0] + "' and b.StockCode='"
						+ keyIds[i].split("@")[1] + "') ");
			}
		}
		hql.deleteCharAt(hql.length() - 1);
		hql.append(") ");
		hql.append(" order by a.goodsNumber ");
		final String sql = hql.toString();
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							ResultSet rss = cs.executeQuery(sql);
							List values = new ArrayList();
							int count = 11;
							if (existPropNames.size() > 0) {
								count = count + existPropNames.size();
							}
							if (existsTwoUnit) {
								count = count + 1;
							}
							if (BaseEnv.systemSet.get("AssUnit").getSetting().equals("true")) {// 启用辅助单位
								count = count + 5;
							}
							while (rss.next()) {
								Object[] value = new Object[count];
								value[0] = rss.getString(1);
								value[1] = rss.getString(2);
								value[2] = rss.getString(3);
								value[3] = rss.getString(4);
								value[4] = rss.getString(5);
								for (int i = 5; i <= 7; i++) {
									value[i] = rss.getObject(i + 1);
									if (value[i] == null) {
										value[i] = "0";
									}
									value[i] = GlobalsTool.formatNumber(value[i], false, false, "true"
											.equals(BaseEnv.systemSet.get("intswitch").getSetting()), null,null, true);
								}
								value[8] = rss.getString(9);
								value[9] = rss.getString("GoodsSpec");
								value[10] = rss.getString("StockLocation");
								if (existPropNames.size() > 0) {
									for (int i = 0; i < existPropNames.size(); i++) {
										Object ob = (Object) rss.getString(((String[]) existPropNames.get(i))[0]);
										ob = ob == null ? "" : ob;
										value[10 + (i + 1)] = ob.toString();
									}
								}
								if (existsTwoUnit
										&& !BaseEnv.systemSet.get("AssUnit").getSetting().equals("true")) {
									value[count - 1] = rss.getDouble("InstoreTwoQty");

								} else if (existsTwoUnit
										&& BaseEnv.systemSet.get("AssUnit").getSetting().equals("true")) {
									value[count - 6] = rss.getDouble("InstoreTwoQty");
								}
								if (BaseEnv.systemSet.get("AssUnit").getSetting().equals("true")) {
									value[count - 5] = rss.getString("SecUnit");
									value[count - 4] = rss.getString("UnitName");
									value[count - 3] = GlobalsTool.formatNumber(Double.parseDouble(rss
											.getString("ConversionRate") == null ? "0" : rss.getString(
											"ConversionRate").toString()), false, false, "true"
											.equals(BaseEnv.systemSet.get("intswitch").getSetting()),
											"tblIniStockDet", "ConversionRate", false);
									value[count - 2] = GlobalsTool.formatNumber(Double.parseDouble(rss
											.getString("SecUnitQty") == null ? "0" : rss.getString(
											"SecUnitQty").toString()), false, false, "true"
											.equals(BaseEnv.systemSet.get("intswitch").getSetting()),
											"tblIniStockDet", "SecUnitQty", false);
									value[count - 1] = GlobalsTool.formatNumber(Double.parseDouble(rss
											.getString("SecUnitPrice") == null ? "0" : rss.getString(
											"SecUnitPrice").toString()), false, false, "true"
											.equals(BaseEnv.systemSet.get("intswitch").getSetting()),
											"tblIniStockDet", "SecUnitPrice", false);
								}
								values.add(value);
							}
							if (values.size() == 0) {
								rs.setRetCode(ErrorCanst.ER_NO_DATA);
								return;
							}
							rs.setRetVal(values);

						} catch (SQLException ex) {
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

	public Result detail(String goodsCode, String stockCode,final String assUnit) {
		// 设置SQL得到商品基本信息及和库存明细中对应的期初值信息
		StringBuffer sqltemp = new StringBuffer(
				"select bean2.id,bean1.goodsNumber,bean1.goodsFullName ,bean2.stockCode,bean3.stockFullName");
		sqltemp
				.append(",bean2.Qty,bean2.IniPrice,bean2.IniAmount,bean1.GoodsSpec,bean2.StockLocation,isnull((select unitname from tblunit where id=bean1.baseunit),'') UnitName");

		sqltemp.append(" from tblGoods bean1,tblStocks bean2 ,tblStock bean3 ");
		sqltemp
				.append(" where bean2.GoodsCode='"
						+ goodsCode
						+ "' and bean2.StockCode='"
						+ stockCode
						+ "' and  bean1.classCode=bean2.GoodsCode  and bean2.StockCode=bean3.classCode ");
		final String sql = sqltemp.toString();
	
	
		final Result rs = new Result();
		final String[] fields = new String[3];
		fields[0] = "Qty";
		fields[1] = "IniPrice";
		fields[2] = "IniAmount";

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							ResultSet rss = cs.executeQuery(sql);

							if (rss.next()) {
								Object[] values = new Object[12];
								for (int i = 0; i < 5; i++) {
									values[i] = rss.getString(i + 1);
								}
								for (int i = 5; i <= 7; i++) {
									values[i] = rss.getObject(i + 1);
									if (values[i] == null) {
										values[i] = 0;
									}
									values[i] = GlobalsTool.formatNumber(
											values[i], false, false, "true"
													.equals(BaseEnv.systemSet
															.get("intswitch")
															.getSetting()),
											"tblStocks", fields[i - 5], true);

								}
								values[9] = rss.getString("GoodsSpec");
								values[10] = rss.getString("StockLocation");
								if(assUnit.equals("true")){//启用辅助单位
									values[11]=rss.getString("UnitName");
								}
								rs.setRetVal(values);
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								rs.setRetCode(ErrorCanst.ER_NO_DATA);
							}

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
	 * 查询分仓库存表所有字段
	 * 
	 * @return Result
	 */
	public ArrayList queryStocksFieldsName(final String tableName,final Hashtable hashTable)
    {
       ArrayList list=new ArrayList();
       DBTableInfoBean tableInfo =(DBTableInfoBean) hashTable.get(tableName);
       List fields=tableInfo.getFieldInfos();
       for(int i=0;i<fields.size();i++){
    	   for(int j=i;j<fields.size()-1;j++){
    		  DBFieldInfoBean df1=(DBFieldInfoBean) fields.get(j);
    		  DBFieldInfoBean df2=(DBFieldInfoBean) fields.get(j+1);
    		  if(df1.getListOrder()>df2.getListOrder()){
    			  DBFieldInfoBean temp=df1;
    			  fields.remove(j);
    			  fields.add(j,df2);
    			  fields.remove(j+1);
    			  fields.add(j+1,temp);
    		  }
    	   }
       }
       for(int k=0;k<fields.size();k++){
    	   DBFieldInfoBean df=(DBFieldInfoBean) fields.get(k);
    	   if(df.getInputType()!=DBFieldInfoBean.INPUT_HIDDEN){
    		   list.add(df.getFieldName());
    	   }
       }
       return list;
    }
	public List queryStocksFieldsNameBySys(final String tableName,final Hashtable hashTable)
    {
	   List list=new ArrayList();
       DBTableInfoBean tableInfo =(DBTableInfoBean) hashTable.get(tableName);
       List fields=tableInfo.getFieldInfos();
       for(int i=0;i<fields.size();i++){
    	   for(int j=i;j<fields.size()-1;j++){
    		  DBFieldInfoBean df1=(DBFieldInfoBean) fields.get(j);
    		  DBFieldInfoBean df2=(DBFieldInfoBean) fields.get(j+1);
    		  if(df1.getListOrder()>df2.getListOrder()){
    			  DBFieldInfoBean temp=df1;
    			  fields.remove(j);
    			  fields.add(j,df2);
    			  fields.remove(j+1);
    			  fields.add(j+1,temp);
    		  }
    	   }
       }
       for(int k=0;k<fields.size();k++){
    	   DBFieldInfoBean df=(DBFieldInfoBean) fields.get(k);
    		   list.add(df.getFieldName());
       }
       return list;
    }
	/**
	 * 查询表中未启用的属性名称
	 * @param tableName
	 * @param hashTable
	 * @return
	 */
	public ArrayList queryNotUsedPropNameInTable(final String tableName,final Hashtable hashTable)
    {
	   GoodsPropMgt propMgt=new GoodsPropMgt();
       ArrayList list=new ArrayList();
       DBTableInfoBean tableInfo =(DBTableInfoBean) hashTable.get(tableName);
       List fields=tableInfo.getFieldInfos();
       for(int k=0;k<fields.size();k++){
    	   DBFieldInfoBean df=(DBFieldInfoBean) fields.get(k);
    	   if(df.getInputType()==DBFieldInfoBean.INPUT_HIDDEN){
    		   list.add(df.getFieldName());
    	   }
       }
       ArrayList notUsedPropNames=new ArrayList();
       for(int i=0;i<list.size();i++){
    	  Result rss= propMgt.queryPropName(list.get(i).toString());
     	  if(rss.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&rss.getRetVal()!=null&&((ArrayList)rss.getRetVal()).size()>0)
     	  {
     		 notUsedPropNames.add((GoodsPropInfoBean)((ArrayList)rss.getRetVal()).get(0));
     	  } 
       }
       return notUsedPropNames;
    }

	/**
	 * 获得分仓库存表的属性数据
	 * 
	 * @param existPropNames
	 *            List
	 * @return Result
	 */
	public Result queryStocksPropValues(final List existPropNames) {
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							String sql = "select id,";
							for (int i = 0; i < existPropNames.size(); i++) {
								if (i == existPropNames.size() - 1) {
									sql += ((String[]) existPropNames.get(i))[0];
								} else {
									sql += ((String[]) existPropNames.get(i))[0]
											+ ",";
								}
							}
							sql += " from tblStocks";
							ResultSet rss = cs.executeQuery(sql);
							Hashtable htValues = new Hashtable();
							List values = new ArrayList();
							while (rss.next()) {
								String[] propValues = new String[existPropNames
										.size()];
								for (int j = 0; j < existPropNames.size(); j++) {
									propValues[j] = rss
											.getString(
													((String[]) existPropNames
															.get(j))[0]).trim();
								}
								values.add(propValues);
								htValues.put(rss.getString("id"), propValues);

							}
							rs.setRetVal(htValues);

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

	public Result queryGoodsPropNames(final String goodsCode) {
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							String sql = "select  distinct(propId) from tblGoodsOfProp where GoodsCode='"
									+ goodsCode + "'";
							ResultSet rss = cs.executeQuery(sql);
							List<String> propIds = new ArrayList<String>();
							while (rss.next()) {
								propIds.add(rss.getString("propId"));
							}
							rs.setRetVal(propIds);
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

	public Result queryGoodsPropValues(final String goodsCode, final String stockCode, final List propNames,
			final boolean existsTwoUnit, final List existNVPropDis, final boolean goodsUseSeq,
			final int costMethod) {
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							boolean total = false;
							Statement cs = conn.createStatement();
							String sql = "select  ";
							for (int i = 0; i < propNames.size(); i++) {
								String propName = ((String[]) propNames.get(i))[0];
								sql += propName + ",";
								for (int j = 0; j < existNVPropDis.size(); j++) {
									String[] nvs = (String[]) existNVPropDis.get(j);
									if (nvs[0].equals(propName)) {
										sql += nvs[1] + ",";
										break;
									}
								}
							}
							if (goodsUseSeq) {
								sql += "CompanyCode,isNull((select ComFullName from tblcompany where classCode=CompanyCode),'') provider,";
							}
							String slSys = GlobalsTool.getSysSetting("StockLocation");
							if (slSys.equals("true")) {
								sql += "StockLocation,";
							}
							if (existsTwoUnit) {
								if (costMethod == 3) {
									sql += "seq,";
								}
								sql += "InstoreTwoQty,InstoreQty,InstorePrice,InstoreAmount,SecUnit,isnull((select unitname from tblunit where id=SecUnit),'') UnitName,ConversionRate,SecUnitQty,SecUnitPrice from tblIniStockDet where goodsCode='"
										+ goodsCode + "' and stockCode='" + stockCode + "' and period=-1 ";
							} else {
								sql += "InstoreQty,InstorePrice,InstoreAmount,SecUnit,isnull((select unitname from tblunit where id=SecUnit),'') UnitName,ConversionRate,SecUnitQty,SecUnitPrice from tblIniStockDet where goodsCode='"
										+ goodsCode + "' and stockCode='" + stockCode + "' and period=-1 ";
							}
							ResultSet rss = cs.executeQuery(sql);
							List propValues = new ArrayList();
							while (rss.next()) {
								List row = new ArrayList();

								for (int i = 0; i < propNames.size(); i++) {
									String propName = ((String[]) propNames.get(i))[0];
									List values = new ArrayList();
									Object value = rss.getObject(propName);
									value = value == null ? "" : value;
									values.add(value.toString());
									values.add(value.toString());
									row.add(values);
									for (int j = 0; j < existNVPropDis.size(); j++) {
										String[] nvs = (String[]) existNVPropDis.get(j);
										if (nvs[0].equals(propName)) {
											List tempVal = new ArrayList();
											Object val = rss.getObject(nvs[1]);
											val = val == null ? "" : val;
											tempVal.add(val);
											tempVal.add(val);
											row.add(tempVal);
											break;
										}
									}
								}
								if (goodsUseSeq) {
									row.add(rss.getString("CompanyCode"));
									row.add(rss.getString("provider"));
								}
								if (slSys.equals("true")) {
									row.add(rss.getString("StockLocation"));
								}
								if (existsTwoUnit) {
									row.add(Double.parseDouble(rss.getString("InstoreTwoQty")));
									if (costMethod == 3) {
										if (rss.getString("seq") == null 
													|| (rss.getString("seq") != null && rss.getString("seq").length() == 0)) {
											total = true;
										}
									}

								}
								row.add(GlobalsTool.formatNumber(Double.parseDouble(rss
										.getString("InstoreQty") == null ? "0" : rss.getString("InstoreQty")
										.toString()), false, false, "true".equals(BaseEnv.systemSet.get(
										"intswitch").getSetting()), "tblIniStockDet", "InstoreQty", false));
								row.add(GlobalsTool.formatNumber(Double.parseDouble(rss
										.getString("InstorePrice") == null ? "0" : rss.getString(
										"InstorePrice").toString()), false, false, "true"
										.equals(BaseEnv.systemSet.get("intswitch").getSetting()),
										"tblIniStockDet", "InstorePrice", false));
								row.add(GlobalsTool.formatNumber(Double.parseDouble(rss
										.getString("InstoreAmount") == null ? "0" : rss.getString(
										"InstoreAmount").toString()), false, false, "true"
										.equals(BaseEnv.systemSet.get("intswitch").getSetting()),
										"tblIniStockDet", "InstoreAmount", false));
								// row.add(Double.parseDouble(rss
								// .getString("InstoreQty")));
								// row.add(Double.parseDouble(rss
								// .getString("InstorePrice")));
								// row.add(Double.parseDouble(rss
								// .getString("InstoreAmount")));
								if (BaseEnv.systemSet.get("AssUnit").getSetting().equals("true")) {// 启用辅助单位
									row.add(rss.getString("SecUnit"));
									row.add(rss.getString("UnitName"));
									// row.add(rss.getString("ConversionRate"));
									// row.add(rss.getString("SecUnitQty"));
									// row.add(rss.getString("SecUnitPrice"));
									row.add(GlobalsTool.formatNumber(Double.parseDouble(
											rss.getString("ConversionRate") == null ? "0" : rss.getString(
											"ConversionRate").toString()), false, false, "true"
											.equals(BaseEnv.systemSet.get("intswitch").getSetting()),
											"tblIniStockDet", "ConversionRate", false));
									row.add(GlobalsTool.formatNumber(Double.parseDouble(
											rss.getString("SecUnitQty") == null ? "0" : rss.getString(
											"SecUnitQty").toString()), false, false, "true"
											.equals(BaseEnv.systemSet.get("intswitch").getSetting()),
											"tblIniStockDet", "SecUnitQty", false));
									row.add(GlobalsTool.formatNumber(Double.parseDouble(
											rss.getString("SecUnitPrice") == null ? "0" : rss.getString(
											"SecUnitPrice").toString()), false, false, "true"
											.equals(BaseEnv.systemSet.get("intswitch").getSetting()),
											"tblIniStockDet", "SecUnitPrice", false));
								}
								propValues.add(row);
							}
							Object[] o = new Object[2];
							o[0] = propValues;
							o[1] = total;
							rs.setRetVal(o);
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

	public Result checkEditList(final String[] ids, final String createBy,
			final String scompanyId) {
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql = "insert into tblIniStockDet(id,period,periodYear,periodMonth,BillDate,GoodsCode,IniQty,IniPrice,IniAmount,InstoreQty,"
								+ "InstorePrice,InstoreAmount,createBy,lastUpdateBy,createTime,lastUpdateTime,"
								+ "statusId,unit,ItemNo,BillType,StockCode,BillID,SCompanyID,InstoreTwoQty,IniTwoQty,BillNo) values(";
						Statement stmt = conn.createStatement();
						try {

							for (int i = 0; i < ids.length; i++) {
								Result rs2 = queryGoodsNext(connection, ids[i]
										.split("@")[0]);
								if (rs2.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
									if (Integer.parseInt(rs2.getRetVal()
											.toString()) == 0) {
										Result rs3 = queryInIniStockdet(
												connection,
												ids[i].split("@")[0], ids[i]
														.split("@")[1]);
										if (rs3.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
											if (Integer.parseInt(rs3
													.getRetVal().toString()) == 0) {
												String createTime = BaseDateFormat
														.format(
																new Date(),
																BaseDateFormat.yyyyMMddHHmmss);
												String id = IDGenerater.getId();
												String strSql = sql;
												strSql += "'"
														+ id
														+ "',-1,-1,-1,'','"
														+ ids[i].split("@")[0]
														+ "',0,0,0,0,0,0,'"
														+ createBy
														+ "','"
														+ createBy
														+ "','"
														+ createTime
														+ "','"
														+ createTime
														+ "',0,'',1,'tblStockDet','"
														+ ids[i].split("@")[1]
														+ "','" + id + "','"
														+ scompanyId + "',1,0,'-1')";
												stmt.executeUpdate(strSql);
											}
										} else {
											rs.setRetCode(rs3.getRetCode());
											return;
										}
									} else {
										rs
												.setRetCode(ErrorCanst.DATA_ALREADY_USED);
										return;
									}
								} else {
									rs.setRetCode(rs2.getRetCode());
									return;
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
	 * 查看分仓库存是否存在子商品
	 * 
	 * @param goodsCode
	 * @return
	 */

	public Result queryGoodsNext(Connection conn, String goodsCode) {
		Result rs = new Result();
		try {
			Statement cs = conn.createStatement();
			String sql = "select count(*) from tblstocks where period=-1 and goodscode  like '"
					+ goodsCode + "_%'";
			ResultSet rss = cs.executeQuery(sql);
			if (rss.next()) {
				rs.setRetVal(rss.getObject(1));
			}

			rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
		} catch (SQLException ex) {
			ex.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return rs;
	}

	/**
	 * 查询库存明细是否存在该商品、仓库的记录
	 * 
	 * @param conn
	 * @param goodsCode
	 * @param stockCode
	 * @return
	 */
	public Result queryInIniStockdet(Connection conn, String goodsCode,
			String stockCode) {
		final Result rs = new Result();
		try {
			Statement cs = conn.createStatement();
			String sql = "select count(*) from tblInistockdet where period=-1 and goodscode='"
					+ goodsCode + "' and stockCode='" + stockCode + "'";
			ResultSet rss = cs.executeQuery(sql);
			if (rss.next()) {
				rs.setRetVal(rss.getObject(1));
			}

			rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
		} catch (SQLException ex) {
			ex.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);

		}
		return rs;

	}
	/**
	 * 查询商品是否启用序列号
	 * @param goodsCode
	 * @return
	 */
		public Result queryGoodsSet(final String goodsCode) {
			final Result rs = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection)
								throws SQLException {
							Connection conn = connection;
							try {
								boolean flag=false;
								Statement cs = conn.createStatement();
								String sql = "select seqIsUsed as isUsed from tblGoods where classCode='"+goodsCode+"'";
								ResultSet rss = cs.executeQuery(sql);
								if (rss.next()) {
									String isUsed=rss.getString("isUsed");
									if(isUsed.equals("0")){
									   flag=true;
									}
								}
					            rs.setRetVal(flag);
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
		 * 查询所有仓库
		 * @param goodsCode
		 * @return
		 */
		public Result queryAllStocks() {
			final Result rs = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection)
								throws SQLException {
							Connection conn = connection;
							try {
								String sql = "select classCode,StockFullName from tblStock where isCatalog!=1" ;
								Statement st = conn.createStatement() ;
								ResultSet rss = st.executeQuery(sql) ;
								ArrayList<StockBean> stockList = new ArrayList<StockBean>() ;
								while(rss.next()){
									StockBean stock = new StockBean() ;
									stock.setStockCode(rss.getString("classCode")) ;
									stock.setStockFullName(rss.getString("StockFullName")) ;
									stockList.add(stock) ;
								}
					            rs.setRetVal(stockList);
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
	 * 查询商品的存库序列号
	 * @param goodsCode
	 * @return
	 */
	public Result querySeqByGoodsCode(String goodsCode, String stockCode, 
			String propSQL,String stockTable,int pageNo,int pageSize) {
		String sql = "select Seq,inTime as inTime from ViewtblStocksSeq where lastQty>0 and  classCode=? and stockCode=? and Seq not in (select Seq from tblSeqAuditing) " + propSQL+" order by inTime,createTime,ItemOrder,Seq";
		if(stockTable!=null && stockTable.trim().length()>0){
			sql = "select Seq,'' as inTime from "+stockTable+" where lastQty>0 and  classCode=? and stockCode=? and Seq not in (select Seq from tblSeqAuditing) " + propSQL+" order by Seq";
		}
		BaseEnv.log.debug("IniGoodsMgt.querySeqByGoodsCode sql="+sql);
		BaseEnv.log.debug("IniGoodsMgt.querySeqByGoodsCode 参数 goodsCode="+goodsCode);
		BaseEnv.log.debug("IniGoodsMgt.querySeqByGoodsCode 参数 stockCode="+stockCode);
		List param = new ArrayList() ;
		param.add(goodsCode) ;
		param.add(stockCode) ;
		return sqlList(sql, param, pageSize, pageNo,true) ;
	}
	/**
	 * 查询序列号对应的时间
	 * @param seqs abc,def
	 * @return
	 */
	public Result querySeqTime(final String seqs) {
		 final Result rst = new Result();
	        int retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                    	String sql ="";
	                        try {
	                            Connection conn = connection;
	                            sql="select seq,case when BillDate='-1' then '期初' else BillDate end from tblStockDet a  WHERE a.Seq in ('"+(seqs.replaceAll(",", "','"))+"') and a.id=(select top 1 id from tblStockDet where Seq=a.Seq and InstoreQty!=0 order by BillDate,createTime,ItemOrder)";
	                           
	                            Statement st=conn.createStatement();
	                            ResultSet rs=st.executeQuery(sql);
	                            HashMap map=new HashMap();
	                            while(rs.next()){
	                            	map.put(rs.getString(1), rs.getString(2));
	                            }
	                            String times="";
	                            String[] seqArray=seqs.split(",");
	                            for(int i=0;i<seqArray.length;i++){
	                            	times+=(map.get(seqArray[i])==null?"&nbsp;":map.get(seqArray[i])) +",";
	                            }
	                    		rst.setRetVal(times);
	                        } catch (Exception ex) {
	                        	BaseEnv.log.debug(sql);
	                            ex.printStackTrace();
	                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                            return;
	                        }
	                    }
	                });
	                return rst.getRetCode();
	            }
	        });
	        rst.setRetCode(retCode);
		return rst;
	}
}
