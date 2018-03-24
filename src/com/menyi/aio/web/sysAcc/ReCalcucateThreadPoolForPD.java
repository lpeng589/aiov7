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
 * <p>Title:ҵ���½�</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: �������
 * @Author ������
 */ 
public class ReCalcucateThreadPoolForPD extends ReCalcucateThreadPool{
	private boolean isBegin = false;
	public ReCalcucateThreadPoolForPD(ArrayList hashList,ArrayList seqhashList,int PeriodYear,int Period) {
		super(hashList,seqhashList,PeriodYear,Period); 
		
		//�鵱ǰ�ڵ��ǲ��ǿ����ڼ�
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
							//�ҳ����ڼ�����ͬ�۵����������hash
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
	 * �Ѳ�ѯ��run�а��뿪�����Ҵ˷�����query��ͷ����������ġ�������������ĵȴ���
	 * @param goodPropHash
	 */
	public Result queryLastAmt(final String goodPropHash){
		final Result rs = new Result();
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							/********************************************************�����hashֵ�ĳ���ɱ�************************************************************/								
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
							//�õ����ڵĽ�����������
							int prePeriodYear = PeriodYear;
							int prePeriod = Period-1;
							if(prePeriod<1){
								prePeriod=12;
								prePeriodYear = prePeriodYear-1;
							}
							if(isBegin){//�����ڼ䣬���ڳɱ�ȡ�ڳ��ɱ�
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
							}else{//�ǿ����ڼ䣬���ڳɱ�ȡ���ڽ��ɱ�
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
							
							//��ó(�ɹ���ⵥ,�ɹ�������,δ���õ��ݵ������ˡ�������,���װ��,������ⵥ,ͬ�۵�����,�ɿⵥ,���ϵ�,���ϵ�,���ϵ�,Ƿ�ϻ��ص�)
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
							//�ع�����ɿⵥ�������ع�������ܿ��ܳ������ⶼ��ͬһ����Ʒ�����Ҫȥ��ͬһ��Ʒ�ĳ��⣬ֻ����ӹ���
							//���嵽�������Ǽ�ȥͬһ��Ʒ�ĳ������������
							//��浥�۵ļ��㹫ʽΪ���ڳ��ɱ�+���ɱ�-�ع����ϳɱ���/(�ڳ�����+�������-�ع���������)
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
							//�ҳ����ڼ�����ͬ�۵����������hash
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
	 * �Ѳ�ѯ��run�а��뿪�����Ҵ˷�����query��ͷ����������ġ�������������ĵȴ���
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
		while((goodPropHash1 = getHash()) !=  null){ //δȡ��hashʱ���д��߳�			
			final String goodPropHash = goodPropHash1;
			for(int i=0;i<500;i++){ //ͬһ��hash�ܿ���ִ�д��󣬱��������ȣ���������Ҫ�ظ�ִ�ж��ֱ���ɹ�����Ҳ�����м��������Ϊ�˱�����ѭ�������ִ��400��
				try{				
					//BaseEnv.log.debug(" hashֵ��"+goodPropHash+"��ʼ����");
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
			
										//���µ��ݲ��������㣬���ϵ�����ͬ�۵�������ͬ��������ɱ�����,ͬ�۵�����ⵥ�۲���.�ɹ��˻������������ⵥ���õ��ݵģ�statusId=1���ɱ�����
										
										/**************���˽�浥�۸��µ���Ӧ���ݵĵ�����***************/
										//���˽�浥�۸��µ����ⵥ���ⵥ����
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
										

										
										//�������ñ��ڳ��ⵥ�Ļ������ɱ�
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
			  
										//�ҳ�������Ҫ����Ľɿⵥ(�Ա���ƷΪ����ԭ�ϵĵ���)���������ƷΪ����ԭ�ϵ��ݵ�����Ʒ�ɱ��������ø���ƷΪ���㡣
										sql="select a.PDWorkOrderID from tblStockDet c join PDBuckleMaterial a on c.billID=a.id  where BillType='PDBuckleMaterial' and outstoreqty>0   and  "
												+ "c.periodYear="+PeriodYear+" and c.period="+Period+" and c.goodPropHash="+goodPropHash + 
												" group by PDWorkOrderID ";
										//Ѷ�밴���¹���ƽ���ɱ�
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
											//���������Ʒ���˹����Ʒѣ��ӹ���,�������ԵĴ��ڣ����ܽɿⵥ��Ӧ���Hash
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
											
											//Ŀ�굥�ݵĳ�����ϳɱ�
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
											//����Ŀ�굥�����ɱ����͵���
											sql = "update PDInvertoryPay set CostAmount=round("+costPrice+"*qty,"+DigitsPrice+"),CostPrice="+costPrice+" where periodYear="+PeriodYear+" and period="+Period+" and WorkOrderID= '"+PDWorkOrderID+"'" ;
											st=conn.createStatement();
											st.addBatch(sql);
											st.executeBatch();
											//�������
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
												//������ۼ����һ�нɿⵥ��
												sql = "update PDInvertoryPay set CostAmount=CostAmount + "+levAmount.doubleValue()+" where id ="
														+ "(select max(id) from PDInvertoryPay where  periodYear="+PeriodYear+" and period="+Period+" and WorkOrderID= '"+PDWorkOrderID+"' )" ;
												st.addBatch(sql);
											}
											
											//���³������ϸ
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
												if(!ingoodPropHash.equals(goodPropHash)){ //������Ʒ�ͳ���ԭ�ϲ�һ��ʱ��������������������ع�����
													//���½ɿⵥԭ�Ϻ�Ҫ����ɿⵥ�ĳ�Ʒ���������ع����������Ʒ����ϸ��Ʒһ�µģ�Ϊ������ѭ������������������
													putHash(ingoodPropHash);
												}else{
													//�ع��������󲿷���ʽ�� A+B =A,��A��Ʒ�����ӵ�ԭ�Ϻ���Ȼ������A�ɱ�������ԭ�Ϻͳ�Ʒ����ͬһ��Ʒ��һ������ɸ���Ʒȫ��ƽ�������ɱ������ɱ���һ��
													
												}
											}
											
										}
										
										/**************���½�浥�ۼ������Ա�ɾ�����ݵ�ʱ���õ�׼ȷ����***************/
										//�޸ĳ������ϸ��totalAmt�ֶΣ�����totalQty=0ʱ����֤totalAmtΪ0����˿ɻ���޸�ͬ�۵������ĳ���������Է��� �޸�ͬ�۵����������ɱ�����֮��
										CallableStatement cbs = conn.prepareCall("{call proc_MonthLastPrice(?,?,?,?)}");
										cbs.setBigDecimal(1, new BigDecimal(goodPropHash));
										cbs.setInt(2,Integer.parseInt(DigitsPrice));
										cbs.setInt(3,PeriodYear);
										cbs.setInt(4,Period);
										cbs.execute();
									
										//���±���ͬ�۵����������ɱ���ͬ�۵��������ϵ�,���ϵ�,���ϵ�,���쵥,Ƿ�ϻ��ص���
										for(String[] hs :allotHash ){
											//�����޸���ͬ�۵����������ɱ��������Ʒ���ֵ����ɱ������仯�������¼��㣬�����ǵ�ͬһ�ڵ��п��ܵ���ȥ�ֵ��������Σ���ֹ��ѭ��
											//Ҫ���һ����������ͬһhash�����������ظ����㲻�ܳ���8�Σ������󣬾Ͳ��ټ���
											if(getAllotHashCount(goodPropHash,hs[0])<8){
												putAllotHash(goodPropHash,hs[0]); //���¼������hash��ƽ���ɱ�
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
										
										/**************���½�浥�ۼ������Ա�ɾ�����ݵ�ʱ���õ�׼ȷ����***************/
										//���浥�۵���Ҫ�ֲֿ�����
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
					
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){ //ִ�гɹ������ݻ�
						BaseEnv.log.debug(" hashֵ��"+goodPropHash+"����ʱ��"+(System.currentTimeMillis()-timeDet));
						break;
					}else{
						BaseEnv.log.error(" hashֵ��"+goodPropHash+"������� �ظ�ִ�е�"+i+"�Σ�����ʱ��:"+(System.currentTimeMillis()-timeDet));
					}					
				} catch (Exception ex) {
					BaseEnv.log.debug("RecalcucateThread.run Error:",ex);		
					BaseEnv.log.error(" hashֵ��"+goodPropHash+"������� �ظ�ִ�е�"+i+"��");
				}				
			}
			try { //ÿ�δ�����ɺ���Ϣ2���룬���������߳��л����ȡ��Դ
				Thread.sleep(2);
			} catch (InterruptedException e) {
			}
		}
	}
}
