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
public class ReCalcucateThreadPoolForV7 extends ReCalcucateThreadPool{
	public ReCalcucateThreadPoolForV7(ArrayList hashList,ArrayList seqhashList,int PeriodYear,int Period) {
		super(hashList,seqhashList,PeriodYear,Period);
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
							BigDecimal inAmt=new BigDecimal("0");
							BigDecimal inQty=new BigDecimal("0");
							BigDecimal outAmt=new BigDecimal("0");
							BigDecimal outQty=new BigDecimal("0");
							BigDecimal curPrice=new BigDecimal("0");
							BigDecimal reBuyPrice=new BigDecimal("0");
							//得到上期的结存数，结存金额
							String sql="select isnull(sum(instoreAmount)-sum(outstoreAmount),0),isnull(sum(instoreQty)-sum(outstoreQty),0) from tblStockDet "
									+ " where goodPropHash=? and (periodYear<? or (periodYear=? and period<?))";										
							PreparedStatement st=conn.prepareStatement(sql);
							st.setString(1, goodPropHash);
							st.setInt(2, PeriodYear);
							st.setInt(3, PeriodYear);
							st.setInt(4, Period);
							
							ResultSet rst=st.executeQuery();
							if(rst.next()){
								lastAmt=rst.getBigDecimal(1);
								lastQty=rst.getBigDecimal(2);
							}

							//工贸(采购入库单,采购换货单,未引用单据的销售退、换货单,组拆装单,其他入库单,同价调拨单),tblBuyInStock1是郭雄公司开发
							sql="select isnull(sum(instoreAmount),0),isnull(sum(instoreQty),0) from tblStockDet "
									+ " where goodPropHash=? and periodYear=? and period=? and instoreQty!=0 and "
									+ " (billType in ('tblBuyInStock','tblBuyInStock1','tblBuyReplace','tblOtherIn','tblGoodsAssemblySplit','tblAllot') or "
									+ " (billType in ('tblSalesReturnStock','tblSalesReplace') ))";										
							st=conn.prepareStatement(sql);
							st.setString(1, goodPropHash);
							st.setInt(2, PeriodYear);
							st.setInt(3, Period);
							rst=st.executeQuery();
							if(rst.next()){
								inAmt=rst.getBigDecimal(1);
								inQty=rst.getBigDecimal(2);
							}
							//取得引用单据的采购退货和换货，因为他们的出库存本必须和原单一致
//							sql="select isnull(sum(OutstoreAmount),0),isnull(sum(OutstoreQty),0) from tblStockDet "
//									+ " where goodPropHash=? and periodYear=? and period=?" +
//							" and OutstoreQty!=0 and ( (billType in ('tblBuyOutStock','tblBuyReplace') and statusId=0))";										
//							st=conn.prepareStatement(sql);
//							st.setString(1, goodPropHash);
//							st.setInt(2, PeriodYear);
//							st.setInt(3, Period);
//							rst=st.executeQuery();
//							if(rst.next()){
//								outAmt=rst.getBigDecimal(1);
//								outQty=rst.getBigDecimal(2);
//							}
						
							if(lastQty.add(inQty).subtract(outQty).doubleValue()!=0){
								curPrice=lastAmt.add(inAmt).subtract(outAmt).divide(lastQty.add(inQty).subtract(outQty),18, RoundingMode.HALF_UP);
							}
							rs.retVal = new BigDecimal[]{lastAmt,lastQty,inAmt,inQty,curPrice};
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
									+ "		and billType in ('tblAllot')"
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
							
							String sql="select id from tblStockDet where OutstoreQty > 0 and goodPropHash=? and PeriodYear=? and period=? ";
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
					final BigDecimal inAmt=bds[2];
					final BigDecimal inQty=bds[3];
					final BigDecimal curPrice=bds[4];	
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
										String sql="update tblStockDet set outstorePrice=round("+curPrice.doubleValue()+","+DigitsPrice+"),outstoreAmount=round(outstoreQty*"+
												curPrice.doubleValue()+","+DigitsAmount+") where goodPropHash="+goodPropHash+" and periodYear="+PeriodYear+" and period="+Period+
												" and outstoreQty!=0";
										Statement st=conn.createStatement();
										st.execute(sql);
										
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
													+"instorePrice=b.outstorePrice "
													+ "from tblStockDet a join tblStockDet b on a.billId=b.billId and a.sourceId=b.sourceId and b.outstoreQty !=0 "
													+ "where a.goodPropHash  ="+hs[0]+"  and a.periodYear="+PeriodYear+" and a.period="+Period+
													" and a.instoreQty!=0 and a.billType in ('tblAllot') and"
													+ " a.billId ='"+hs[1]+"'  "
													;
											st=conn.createStatement();
											st.execute(sql);
										}
										
										//更新引用本期出库单的换货单成本
//										sql="update tblStockDet set statusId=1,instorePrice=round("+curPrice.doubleValue()+","+DigitsPrice+"),instoreAmount=round(instoreQty*"+
//										curPrice.doubleValue()+","+DigitsAmount+") where goodPropHash="+goodPropHash+" and periodYear="+PeriodYear+" and period="+Period+
//												" and instoreQty!=0 and billType in ('tblSalesReturnStock','tblSalesReplace') and statusId=0";
//										st=conn.createStatement();
//										st.execute(sql);
										
										for(int i=0;i<all.size();i++){
											cbs = conn.prepareCall("{call proc_rewriteBill(?)}");
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
