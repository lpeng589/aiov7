package com.menyi.aio.web.sysAcc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

/**
 * 
 * <p>Title:业务月结</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: 科荣软件
 * @Author 周新宇
 */ 
public class ReCalcucateThreadPoolForPD extends ReCalcucateThreadPool{
	private boolean isBegin = false;
	public ReCalcucateThreadPoolForPD(ArrayList hashList,ArrayList seqhashList,int PeriodYear,int Period) {
		super(hashList,seqhashList,PeriodYear,Period); 
		
		//查当前期单是不是开帐期间
		Result rs = queryBeginPeriod(PeriodYear,Period);
		if(rs.retVal != null && rs.retVal.equals("1")){
			isBegin = true;
		}
	}
	public Result queryBeginPeriod(final int PeriodYear,final int Period){
		final Result rs = new Result();
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//找出本期间所有同价调拨单的入库hash
							String sql = " select IsBegin from tblAccPeriod where AccYear=?  and AccPeriod=?";
							PreparedStatement st=conn.prepareStatement(sql);
							st.setInt(1, PeriodYear);
							st.setInt(2, Period);
							ResultSet rst=st.executeQuery();
							ArrayList hashList = new ArrayList();
							if(rst.next()){
								rs.retVal = rst.getString(1);
							}
						} catch (Exception ex) {
							BaseEnv.log.debug("RecalcucateThread.queryBeginPeriod Error:",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});	
		rs.retCode = retCode;
		return rs;
	}
	
	
	/**
	 * 把查询从run中剥离开来，且此方法以query开头，是无事物的。避免表锁带来的等待，
	 * @param goodPropHash
	 */
	public Result queryLastAmt(final String goodPropHash){
		final Result rs = new Result();
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							/********************************************************计算此hash值的出库成本************************************************************/								
							BigDecimal lastAmt=new BigDecimal("0");
							BigDecimal lastQty=new BigDecimal("0");
							BigDecimal lastPrice=new BigDecimal("0");
							BigDecimal lastPriceMaterial=new BigDecimal("0");
							BigDecimal lastPriceLabor=new BigDecimal("0");
							BigDecimal lastPriceProdCost=new BigDecimal("0");
							BigDecimal lastPriceOutFee=new BigDecimal("0");
							
							BigDecimal inAmt=new BigDecimal("0");
							BigDecimal inMaterialAmt=new BigDecimal("0");
							BigDecimal inLaborAmt=new BigDecimal("0");
							BigDecimal inProdCostAmt=new BigDecimal("0");
							BigDecimal inOutFeeAmt=new BigDecimal("0");
							BigDecimal inQty=new BigDecimal("0");
							
							BigDecimal curPrice=new BigDecimal("0");
							BigDecimal curPriceMaterial=new BigDecimal("0");
							BigDecimal curPriceLabor=new BigDecimal("0");
							BigDecimal curPriceProdCost=new BigDecimal("0");
							BigDecimal curPriceOutFee=new BigDecimal("0");
							//得到上期的结存数，结存金额
							int prePeriodYear = PeriodYear;
							int prePeriod = Period-1;
							if(prePeriod<1){
								prePeriod=12;
								prePeriodYear = prePeriodYear-1;
							}
							if(isBegin){//开帐期间，上期成本取期初成本
								String sql="select TotalQty,InstorePrice,InstoreAmount,0,0,0,InstoreAmount from tblStockDet "
										+ " where goodPropHash=? and InstoreQty >0 and (periodYear=-1 and period=-1)";										
								PreparedStatement st=conn.prepareStatement(sql);
								st.setString(1, goodPropHash);
								
								ResultSet rst=st.executeQuery();
								if(rst.next()){
									lastAmt=rst.getBigDecimal("InstoreAmount");
									lastQty=rst.getBigDecimal("TotalQty");
									lastPrice=rst.getBigDecimal("InstorePrice");
									lastPriceMaterial=rst.getBigDecimal("InstorePrice");
								}
							}else{//非开帐期间，上期成本取下期结存成本
								String sql="select lastQty,lastPrice,lastPriceMaterial,lastPriceLabor,lastPriceProdCost,lastPriceOutFee,lastAmount from tblStocksPeriod "
										+ " where goodPropHash=? and (periodYear=? and period=?) ";										
								PreparedStatement st=conn.prepareStatement(sql);
								st.setString(1, goodPropHash);
								st.setInt(2, prePeriodYear);
								st.setInt(3, prePeriod);
								
								ResultSet rst=st.executeQuery();
								if(rst.next()){
									lastAmt=rst.getBigDecimal("lastAmount");
									lastQty=rst.getBigDecimal("lastQty");
									lastPrice=rst.getBigDecimal("lastPrice");
									lastPriceMaterial=rst.getBigDecimal("lastPriceMaterial");
									lastPriceLabor=rst.getBigDecimal("lastPriceLabor");
									lastPriceProdCost=rst.getBigDecimal("lastPriceProdCost");
									lastPriceOutFee=rst.getBigDecimal("lastPriceOutFee");
								}
							}
							
							//工贸(采购入库单,采购换货单,未引用单据的销售退、换货单,组拆装单,其他入库单,同价调拨单,缴库单,发料单,退料单,补料单,欠料还回单)
							String sql="select isnull(sum(instoreAmount),0),isnull(sum(instoreQty),0),"
									+ " isnull(sum(instoreQty*InstorePriceMaterial),0),"
									+ "isnull(sum(instoreQty*InstorePriceLabor),0),"
									+ "isnull(sum(instoreQty*InstorePriceProdCost),0),"
									+ "isnull(sum(instoreQty*InstorePriceOutFee),0) from tblStockDet "
									+ " where goodPropHash=? and periodYear=? and period=?  and instoreQty!=0 "
									+ " and  (billType in ('tblBuyInStock','tblBuyReplace','tblOtherIn','tblGoodsAssemblySplit',"
									+ "'tblAllot','PDInvertoryPay','PDOutMaterials','PDReturnItems','PDReFetchMaterial',"
									+ "'PDReturnOwe') or "
									+ " (billType in ('tblSalesReturnStock','tblSalesReplace') ))";										
							PreparedStatement st=conn.prepareStatement(sql);
							st.setString(1, goodPropHash);
							st.setInt(2, PeriodYear);
							st.setInt(3, Period);
							ResultSet rst=st.executeQuery();
							if(rst.next()){
								inAmt=rst.getBigDecimal(1);
								inQty=rst.getBigDecimal(2);
								inLaborAmt=rst.getBigDecimal(4);
								inProdCostAmt=rst.getBigDecimal(5);
								inOutFeeAmt=rst.getBigDecimal(6);
								inMaterialAmt=inAmt.subtract(inLaborAmt).subtract(inProdCostAmt).subtract(inOutFeeAmt);
							}
							//重工工令缴库单，由于重工工令单，很可能出库和入库都是同一个商品，因此要去掉同一商品的出库，只计算加工费
							//具体到计算上是减去同一商品的出库数与出库金额
							//结存单价的计算公式为（期初成本+入库成本-重工领料成本）/(期初数量+入库数量-重工领料数量)
							sql="select isnull(sum(outstoreAmount),0),isnull(sum(outstoreQty),0),"
									+ " isnull(sum(outstoreQty*outstorePriceMaterial),0),"
									+ "isnull(sum(outstoreQty*outstorePriceLabor),0),"
									+ "isnull(sum(outstoreQty*outstorePriceProdCost),0),"
									+ "isnull(sum(outstoreQty*outstorePriceOutFee),0) from tblStockDet "
									+ " where goodPropHash=? and tblStockDet.periodYear=? and tblStockDet.period=?  and tblStockDet.outstoreQty!=0 "
									+ " and billId in ( select billid from tblStockDet "
									+ " where goodPropHash=? and periodYear=? and period=?  and instoreQty!=0  and "
									+ " billType ='PDInvertoryPay'  ) ";										
							st=conn.prepareStatement(sql);
							st.setString(1, goodPropHash);
							st.setInt(2, PeriodYear);
							st.setInt(3, Period);
							st.setString(4, goodPropHash);
							st.setInt(5, PeriodYear);
							st.setInt(6, Period);
							rst=st.executeQuery();
							if(rst.next()){
								inAmt=inAmt.subtract(rst.getBigDecimal(1));
								inQty=inQty.subtract(rst.getBigDecimal(2));
								inMaterialAmt=inMaterialAmt.subtract(rst.getBigDecimal(3));
								inLaborAmt=inLaborAmt.subtract(rst.getBigDecimal(4));
								inProdCostAmt=inProdCostAmt.subtract(rst.getBigDecimal(5));
								inOutFeeAmt=inOutFeeAmt.subtract(rst.getBigDecimal(6));
							}
							
							if(lastQty.add(inQty).doubleValue()!=0){
								curPrice=lastAmt.add(inAmt).divide(lastQty.add(inQty),18, RoundingMode.HALF_UP);
								curPriceMaterial = lastQty.multiply(lastPriceMaterial).add(inMaterialAmt).divide(lastQty.add(inQty),18, RoundingMode.HALF_UP);
								curPriceLabor = lastQty.multiply(lastPriceLabor).add(inLaborAmt).divide(lastQty.add(inQty),18, RoundingMode.HALF_UP);
								curPriceProdCost = lastQty.multiply(lastPriceProdCost).add(inProdCostAmt).divide(lastQty.add(inQty),18, RoundingMode.HALF_UP);
								curPriceOutFee = lastQty.multiply(lastPriceOutFee).add(inOutFeeAmt).divide(lastQty.add(inQty),18, RoundingMode.HALF_UP);
							}
							
							
							
							
							rs.retVal = new BigDecimal[]{lastAmt,lastQty,lastPrice,lastPriceMaterial,lastPriceLabor,lastPriceProdCost,lastPriceOutFee,
									inAmt,inMaterialAmt,inLaborAmt,inProdCostAmt,inOutFeeAmt,inQty,curPrice,curPriceMaterial,curPriceLabor,curPriceProdCost,curPriceOutFee};
						} catch (Exception ex) {
							BaseEnv.log.debug("RecalcucateThread.queryLastAmt Error:",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});	
		rs.retCode = retCode;
		return rs;
	}
	
	public Result queryAllotInHash(final String goodPropHash){
		final Result rs = new Result();
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//找出本期间所有同价调拨单的入库hash
							String sql = " select a.goodPropHash,a.BillID from tblStockDet a join ( "
									+ "		select BillID,goodPropHash,goodPropHashNoStock from  tblStockDet where  "
									+ "		goodPropHash =? and periodYear=? and period=?  and outstoreQty!=0 "
									+ "		and billType in ('tblAllot','PDOutMaterials','PDReturnItems','PDReFetchMaterial','PDReturnOwe')"
									+ ")b on a.BillID=b.BillID and a.goodPropHashNoStock=b.goodPropHashNoStock where a.InstoreQty !=0";
							PreparedStatement st=conn.prepareStatement(sql);
							st.setString(1, goodPropHash);
							st.setInt(2, PeriodYear);
							st.setInt(3, Period);
							ResultSet rst=st.executeQuery();
							ArrayList hashList = new ArrayList();
							while(rst.next()){
								String[] ss = {rst.getString(1),rst.getString(2)};
								hashList.add(ss);
							}
							rs.retVal = hashList;
						} catch (Exception ex) {
							BaseEnv.log.debug("RecalcucateThread.queryLastAmt Error:",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});	
		rs.retCode = retCode;
		return rs;
	}
	
	/**
	 * 把查询从run中剥离开来，且此方法以query开头，是无事物的。避免表锁带来的等待，
	 * @param goodPropHash
	 */
	public Result queryRewriteId(final String goodPropHash){
		final Result rs = new Result();
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							String sql="select id from tblStockDet where OutstoreQty > 0 and goodPropHash=? and PeriodYear=? and period=?   ";
							PreparedStatement st=conn.prepareStatement(sql);
							st.setString(1, goodPropHash);
							st.setInt(2, PeriodYear);
							st.setInt(3, Period);
							
							ResultSet rst=st.executeQuery();
							ArrayList all=new ArrayList();
							while(rst.next()){
								all.add(rst.getString(1));
							}
							rs.retVal = all;
						} catch (Exception ex) {
							BaseEnv.log.debug("RecalcucateThread.queryRewriteId Error:",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});	
		rs.retCode = retCode;
		return rs;
	}
	
	public void run(){
		String goodPropHash1 = null;
		while((goodPropHash1 = getHash()) !=  null){ //未取得hash时，中此线程			
			final String goodPropHash = goodPropHash1;
			for(int i=0;i<500;i++){ //同一个hash很可能执行错误，比如死锁等，所以这里要重复执行多次直到成功，但也可能有极端情况，为了避免死循环，最多执行400次
				try{				
					//BaseEnv.log.debug(" hash值："+goodPropHash+"开始运算");
					long timeDet=System.currentTimeMillis();
					Result rs1= queryLastAmt(goodPropHash);
					Result rs2= queryRewriteId(goodPropHash);
					Result rs3= queryAllotInHash(goodPropHash);
					
					
					if(rs1.retCode != ErrorCanst.DEFAULT_SUCCESS || rs2.retCode != ErrorCanst.DEFAULT_SUCCESS || rs3.retCode != ErrorCanst.DEFAULT_SUCCESS){
						continue;
					}
					
					
					BigDecimal[] bds = (BigDecimal[])rs1.retVal;			
					final BigDecimal lastAmt=bds[0];
					final BigDecimal lastQty=bds[1];
					final BigDecimal lastPrice=bds[2];
					final BigDecimal lastPriceMaterial=bds[3];
					final BigDecimal lastPriceLabor=bds[4];
					final BigDecimal lastPriceProdCost=bds[5];
					final BigDecimal lastPriceOutFee=bds[6];
					
					final BigDecimal inAmt=bds[7];
					final BigDecimal inMaterialAmt=bds[8];
					final BigDecimal inLaborAmt=bds[9];
					final BigDecimal inProdCostAmt=bds[10];
					final BigDecimal inOutFeeAmt=bds[11];
					final BigDecimal inQty=bds[12];
					
					final BigDecimal curPrice=bds[13];
					final BigDecimal curPriceMaterial=bds[14];
					final BigDecimal curPriceLabor=bds[15];
					final BigDecimal curPriceProdCost=bds[16];
					final BigDecimal curPriceOutFee=bds[17];
					
					
					final ArrayList all = (ArrayList)rs2.retVal;
					final ArrayList<String[]> allotHash = (ArrayList)rs3.retVal;
					
					
					final Result rs = new Result();
					int retCode= DBUtil.execute(new IfDB() {
						public int exec(Session session) {
							session.doWork(new Work() {
								public void execute(Connection conn) throws SQLException {
									try {
										BigDecimal reBuyPrice=new BigDecimal("0");								
			
										//以下单据不参与重算，发料单等与同价调拨单类同，调动后成本不变,同价调拨入库单价不变.采购退货单，其他出库单引用单据的（statusId=1）成本不变
										
										/**************将此结存单价更新到相应单据的单价中***************/
										//将此结存单价更新到出库单出库单价中
										String sql="update tblStockDet set outstoreAmount=round(outstoreQty*"+curPrice.doubleValue()+","+DigitsAmount+"), "
												+"outstorePrice=round("+curPrice.doubleValue()+","+DigitsPrice+"), "
												+"outstorePriceMaterial=round("+curPriceMaterial.doubleValue()+","+DigitsPrice+"), "
												+"outstorePriceLabor=round("+curPriceLabor.doubleValue()+","+DigitsPrice+"), "
												+"outstorePriceProdCost=round("+curPriceProdCost.doubleValue()+","+DigitsPrice+"), "
												+"outstorePriceOutFee=round("+curPriceOutFee.doubleValue()+","+DigitsPrice+") "
												+ " where goodPropHash="+goodPropHash+" and periodYear="+PeriodYear+" and period="+Period+
												" and outstoreQty!=0 ";
										Statement st=conn.createStatement();
										st.execute(sql);
										

										
										//更新引用本期出库单的换货单成本
//										sql="update tblStockDet set statusId=1,instoreAmount=round(instoreQty*"+curPrice.doubleValue()+","+DigitsAmount+"), "
//												+"instorePrice=round("+curPrice.doubleValue()+","+DigitsPrice+"), "
//												+"instorePriceMaterial=round("+curPriceMaterial.doubleValue()+","+DigitsPrice+"), "
//												+"instorePriceLabor=round("+curPriceLabor.doubleValue()+","+DigitsPrice+"), "
//												+"instorePriceProdCost=round("+curPriceProdCost.doubleValue()+","+DigitsPrice+"), "
//												+"instorePriceOutFee=round("+curPriceOutFee.doubleValue()+","+DigitsPrice+") "
//												+ " where goodPropHash="+goodPropHash+" and periodYear="+PeriodYear+" and period="+Period+
//												" and instoreQty!=0 and billType in ('tblSalesReturnStock','tblSalesReplace') and statusId=0";
//										st=conn.createStatement();
//										st.execute(sql);
			  
										//找出所有需要计算的缴库单(以本商品为出库原料的单据)计算出本商品为出库原料单据的入库成品成本，并重置该商品为重算。
										sql="select a.PDWorkOrderID from tblStockDet c join PDBuckleMaterial a on c.billID=a.id  where BillType='PDBuckleMaterial' and outstoreqty>0   and  "
												+ "c.periodYear="+PeriodYear+" and c.period="+Period+" and c.goodPropHash="+goodPropHash + 
												" group by PDWorkOrderID ";
										//讯弘按当月工令平均成本
										Statement bst=conn.createStatement();
										ResultSet brst=bst.executeQuery(sql);
										while(brst.next()){
											String PDWorkOrderID = brst.getString(1);
											//System.out.println("billId="+billId);
											BigDecimal Amount = new BigDecimal("0");
											BigDecimal Material = new BigDecimal("0");
											BigDecimal Labor = new BigDecimal("0");
											BigDecimal ProdCost = new BigDecimal("0");
											BigDecimal OutFee = new BigDecimal("0");
											BigDecimal TotalInstoreQty = new BigDecimal("0");
											//查出本单商品，人工，制费，加工费,由于属性的存在，可能缴库单对应多个Hash
											sql = "select c.labor,c.prodCost,c.outFee,c.GoodsCode,a.goodPropHash,a.instoreQty  from PDInvertoryPay c "
													+ "join tblStockDet a on c.id=a.billID and a.instoreQty>0  where c.WorkOrderID='"+PDWorkOrderID+"' and "
													+ " c.periodYear="+PeriodYear+" and c.period="+Period+"" ;
											st=conn.createStatement();
											ResultSet rst=st.executeQuery(sql);
											String GoodsCode="";
											ArrayList<String> ingoodPropHashList=new ArrayList<String>();
											while(rst.next()){
												Amount = Amount.add(rst.getBigDecimal(1));
												Labor = Labor.add(rst.getBigDecimal(1));
												
												Amount = Amount.add(rst.getBigDecimal(2));
												ProdCost = ProdCost.add(rst.getBigDecimal(2));
												
												Amount = Amount.add(rst.getBigDecimal(3));
												OutFee = OutFee.add(rst.getBigDecimal(3));
												
												GoodsCode = rst.getString(4);
												if(!ingoodPropHashList.contains(rst.getString(5)) ){
													ingoodPropHashList.add(rst.getString(5));
												}
												TotalInstoreQty=TotalInstoreQty.add(rst.getBigDecimal(6));
											}
											
											//目标单据的出库材料成本
											sql = "select isnull(sum(OutstoreAmount),0),"
													+ "isnull(sum(isnull(OutstorePriceLabor*OutstoreQty,0)),0),isnull(sum(isnull(OutstorePriceProdCost*OutstoreQty,0)),0),"
													+ "isnull(sum(isnull(OutstorePriceOutFee*OutstoreQty,0)),0) "
													+ " from tblStockDet c join PDBuckleMaterial a on c.billID=a.id   where BillType='PDBuckleMaterial' and outstoreQty > 0  and "
													+ "c.periodYear="+PeriodYear+" and c.period="+Period+" and a.PDWorkOrderID='"+PDWorkOrderID+"'" ;
											rst=st.executeQuery(sql);
											
											while(rst.next()){
												Amount = Amount.add(rst.getBigDecimal(1));
												Labor = Labor.add(rst.getBigDecimal(2));
												ProdCost = ProdCost.add(rst.getBigDecimal(3));
												OutFee = OutFee.add(rst.getBigDecimal(4));
											}
											BigDecimal costPrice = Amount.divide(TotalInstoreQty,Integer.parseInt(DigitsPrice),Amount.ROUND_HALF_UP);
											//更新目标单据入库成本金额，和单价
											sql = "update PDInvertoryPay set CostAmount=round("+costPrice+"*qty,"+DigitsPrice+"),CostPrice="+costPrice+" where periodYear="+PeriodYear+" and period="+Period+" and WorkOrderID= '"+PDWorkOrderID+"'" ;
											st=conn.createStatement();
											st.addBatch(sql);
											st.executeBatch();
											//处理余额
											sql = " select sum(CostAmount) from PDInvertoryPay  where periodYear="+PeriodYear+" and period="+Period+" and WorkOrderID= '"+PDWorkOrderID+"'";
											st=conn.createStatement();
											rst=st.executeQuery(sql);
											BigDecimal levAmount = new BigDecimal(0);
											if(rst.next()){
												levAmount =rst.getBigDecimal(1);
											}
											levAmount = Amount.subtract(levAmount);
											
											st=conn.createStatement();
											if(levAmount.doubleValue() != 0){
												//有余额累加最后一行缴库单上
												sql = "update PDInvertoryPay set CostAmount=CostAmount + "+levAmount.doubleValue()+" where id ="
														+ "(select max(id) from PDInvertoryPay where  periodYear="+PeriodYear+" and period="+Period+" and WorkOrderID= '"+PDWorkOrderID+"' )" ;
												st.addBatch(sql);
											}
											
											//更新出入库明细
											sql = "update tblStockDet set InstoreAmount=a.CostAmount "+
													",InstorePrice="+costPrice+ 
													",InstorePriceMaterial=round("+Amount.subtract(Labor).subtract(ProdCost).subtract(OutFee).divide(TotalInstoreQty,10,Amount.ROUND_HALF_UP).doubleValue()+","+DigitsPrice+") "+
													",InstorePriceLabor=round("+Labor.divide(TotalInstoreQty,10,Amount.ROUND_HALF_UP).doubleValue()+","+DigitsPrice+") "+
													",InstorePriceProdCost=round("+ProdCost.divide(TotalInstoreQty,10,Amount.ROUND_HALF_UP).doubleValue()+","+DigitsPrice+") "+
													",InstorePriceOutFee=round("+OutFee.divide(TotalInstoreQty,10,Amount.ROUND_HALF_UP).doubleValue()+","+DigitsPrice+") "
													+ " from tblStockDet c join PDInvertoryPay a on c.billID=a.id  where BillType='PDInvertoryPay' and instoreqty>0   and  "
												    + "c.periodYear="+PeriodYear+" and c.period="+Period+" and WorkOrderID= '"+PDWorkOrderID+"' " ;
											st.addBatch(sql);
											st.executeBatch();
											
											
											
											for(String ingoodPropHash :ingoodPropHashList){
												if(!ingoodPropHash.equals(goodPropHash)){ //当入库成品和出库原料不一致时，是正常工令单，否则是重工工令
													//更新缴库单原料后，要重算缴库单的成品，但对于重工工令，主表商品和明细商品一致的，为避免死循环，不继续进行重算
													putHash(ingoodPropHash);
												}else{
													//重工工令单，这大部分形式是 A+B =A,即A商品返工加点原料后仍然生产出A成本，由于原料和成品都是同一商品，一定会造成该商品全月平均后出库成本和入库成本不一致
													
												}
											}
											
										}
										
										/**************更新结存单价及结存金额，以便删除单据的时候拿到准确数据***************/
										//修改出入库明细的totalAmt字段，并当totalQty=0时，保证totalAmt为0，因此可会会修改同价调拨单的出库金额，，所以放在 修改同价调拨单的入库成本代码之上
										CallableStatement cbs = conn.prepareCall("{call proc_MonthLastPrice(?,?,?,?)}");
										cbs.setBigDecimal(1, new BigDecimal(goodPropHash));
										cbs.setInt(2,Integer.parseInt(DigitsPrice));
										cbs.setInt(3,PeriodYear);
										cbs.setInt(4,Period);
										cbs.execute();
									
										//更新本期同价调拨单的入库成本（同价调拨，发料单,退料单,补料单,超领单,欠料还回单）
										for(String[] hs :allotHash ){
											//由于修改了同价调拨单的入库成本，则该商品入库仓的入库成本发生变化，需重新计算，但考虑到同一期单有可能调过去又调回来情形，防止死循环
											//要设计一计数据器，同一hash因调拨引起的重复计算不能超过8次，超过后，就不再计算
											if(getAllotHashCount(goodPropHash,hs[0])<8){
												putAllotHash(goodPropHash,hs[0]); //重新计算入库hash的平均成本
											}
											sql="update a set instoreAmount=b.outstoreAmount, "
													+"instorePrice=b.outstorePrice, "
													+"instorePriceMaterial=b.outstorePriceMaterial, "
													+"instorePriceLabor=b.outstorePriceLabor, "
													+"instorePriceProdCost=b.outstorePriceProdCost, "
													+"instorePriceOutFee=b.outstorePriceOutFee "
													+ "from tblStockDet a join tblStockDet b on a.billId=b.billId and a.sourceId=b.sourceId and b.outstoreQty !=0 "
													+ "where a.goodPropHash  ="+hs[0]+"  and a.periodYear="+PeriodYear+" and a.period="+Period+
													" and a.instoreQty!=0 and a.billType in ('tblAllot','PDOutMaterials','PDReturnItems','PDReFetchMaterial','PDReturnOwe') and"
													+ " a.billId ='"+hs[1]+"'  "
													;
											st=conn.createStatement();
											st.execute(sql);
										}
										
										/**************更新结存单价及结存金额，以便删除单据的时候拿到准确数据***************/
										//算结存单价单，要分仓库来算
										st=conn.createStatement();
										sql = " delete tblStocksPeriod  where periodYear="+PeriodYear+" and period="+Period+" and goodPropHash="+goodPropHash;
										st.addBatch(sql);
										sql = " INSERT INTO [tblStocksPeriod]([id],[GoodsCode],StockCode,Seq,[ProDate],[Availably],[BatchNo],[Inch],[Hue],[yearNO],"
												+ "[lastQty],[lastPrice],[lastPriceMaterial],[lastPriceLabor],[lastPriceProdCost],[lastPriceOutFee],[lastAmount],"
												+ "[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[SCompanyID],"
												+ "[iniQty],[iniPrice],[iniPriceMaterial],[iniPriceLabor],[iniPriceProdCost],[iniPriceOutFee],[iniAmount],"
												+ "[Period],[PeriodMonth],[PeriodYear],[goodPropHash])"
												+ "select substring(convert(varchar(40),newid()),1,30),[GoodsCode],StockCode,Seq,[ProDate],[Availably],[BatchNo],[Inch],[Hue],[yearNO],"
												+ lastQty.doubleValue()+"+isnull(sum(instoreQty-outstoreQty),0),"+curPrice.doubleValue()+","+curPriceMaterial.doubleValue()+","+curPriceLabor.doubleValue()+","+curPriceProdCost.doubleValue()+","+curPriceOutFee.doubleValue()+","+lastAmt.doubleValue()+"+sum(isnull(instoreAmount,0))-sum(isnull(outstoreAmount,0)),"
												+ "'1','1','"+BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+"','"+BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+"',0,'00001',"
												+ lastQty.doubleValue()+","+lastPrice.doubleValue()+","+lastPriceMaterial.doubleValue()+","+lastPriceLabor.doubleValue()+","+lastPriceProdCost.doubleValue()+","+lastPriceOutFee.doubleValue()+","+lastAmt.doubleValue()+","
												+ Period+ ","+Period+ ","+PeriodYear+ ","+goodPropHash
												+ " from tblStockDet where period="+Period+" and periodYear="+PeriodYear+" and goodPropHash="+goodPropHash
												+ " group by [GoodsCode],StockCode,Seq,[ProDate],[Availably],[BatchNo],[Inch],[Hue],[yearNO]";
										st.addBatch(sql);
										st.executeBatch();
										

										for(int i=0;i<all.size();i++){
											cbs = conn.prepareCall("{call proc_rewriteBillForPD(?)}");
											cbs.setString(1, all.get(i).toString());
											cbs.execute();
										}								
									} catch (Exception ex) {
										BaseEnv.log.error("RecalcucateThread.run Error:",ex);
										rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
										return;
									}
								}
							});
							return rs.getRetCode();
						}
					});	
					
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){ //执行成功跳出遁还
						BaseEnv.log.debug(" hash值："+goodPropHash+"运算时间"+(System.currentTimeMillis()-timeDet));
						break;
					}else{
						BaseEnv.log.error(" hash值："+goodPropHash+"运算错误 重复执行第"+i+"次，花费时间:"+(System.currentTimeMillis()-timeDet));
					}					
				} catch (Exception ex) {
					BaseEnv.log.debug("RecalcucateThread.run Error:",ex);		
					BaseEnv.log.error(" hash值："+goodPropHash+"运算错误 重复执行第"+i+"次");
				}				
			}
			try { //每次处理完成后，休息2毫秒，以让其它线程有机会获取资源
				Thread.sleep(2);
			} catch (InterruptedException e) {
			}
		}
	}
}
