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
 * <p>Title:ҵ���½�</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: �������
 * @Author ��ѩ
 */
public class ReCalcucateThreadPoolForV7 extends ReCalcucateThreadPool{
	public ReCalcucateThreadPoolForV7(ArrayList hashList,ArrayList seqhashList,int PeriodYear,int Period) {
		super(hashList,seqhashList,PeriodYear,Period);
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
							BigDecimal inAmt=new BigDecimal("0");
							BigDecimal inQty=new BigDecimal("0");
							BigDecimal outAmt=new BigDecimal("0");
							BigDecimal outQty=new BigDecimal("0");
							BigDecimal curPrice=new BigDecimal("0");
							BigDecimal reBuyPrice=new BigDecimal("0");
							//�õ����ڵĽ�����������
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

							//��ó(�ɹ���ⵥ,�ɹ�������,δ���õ��ݵ������ˡ�������,���װ��,������ⵥ,ͬ�۵�����),tblBuyInStock1�ǹ��۹�˾����
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
							//ȡ�����õ��ݵĲɹ��˻��ͻ�������Ϊ���ǵĳ���汾�����ԭ��һ��
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
							
							//�ҳ����ڼ�����ͬ�۵����������hash
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
			
										//���µ��ݲ��������㣬���ϵ�����ͬ�۵�������ͬ��������ɱ�����,ͬ�۵�����ⵥ�۲���.�ɹ��˻������������ⵥ���õ��ݵģ�statusId=1���ɱ�����
										
										/**************���˽�浥�۸��µ���Ӧ���ݵĵ�����***************/
										//���˽�浥�۸��µ����ⵥ���ⵥ����
										String sql="update tblStockDet set outstorePrice=round("+curPrice.doubleValue()+","+DigitsPrice+"),outstoreAmount=round(outstoreQty*"+
												curPrice.doubleValue()+","+DigitsAmount+") where goodPropHash="+goodPropHash+" and periodYear="+PeriodYear+" and period="+Period+
												" and outstoreQty!=0";
										Statement st=conn.createStatement();
										st.execute(sql);
										
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
													+"instorePrice=b.outstorePrice "
													+ "from tblStockDet a join tblStockDet b on a.billId=b.billId and a.sourceId=b.sourceId and b.outstoreQty !=0 "
													+ "where a.goodPropHash  ="+hs[0]+"  and a.periodYear="+PeriodYear+" and a.period="+Period+
													" and a.instoreQty!=0 and a.billType in ('tblAllot') and"
													+ " a.billId ='"+hs[1]+"'  "
													;
											st=conn.createStatement();
											st.execute(sql);
										}
										
										//�������ñ��ڳ��ⵥ�Ļ������ɱ�
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
