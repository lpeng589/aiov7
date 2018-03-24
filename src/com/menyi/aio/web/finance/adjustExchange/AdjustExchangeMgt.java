package com.menyi.aio.web.finance.adjustExchange;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.*;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.sysAcc.SettleCostMgt;
import com.menyi.web.util.*;

/**
 * �ڳ����� ���ݿ������
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2013-03-28
 * @Copyright: �������
 * @Author fjj
 */
public class AdjustExchangeMgt extends AIODBManager {
	
	/**
	 * ��ѯָ������ڼ������еĻ���
	 * @return
	 */
	public Result queryExchange(final AccPeriodBean accPeriodBean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							StringBuffer sql = new StringBuffer(" SELECT id,currencySign,currencyName,recordExchange");
							sql.append(",adjustExchange,periodYear,period FROM tblSetExchange");
							sql.append(" WHERE PeriodYear="+accPeriodBean.getAccYear()+" AND Period="+accPeriodBean.getAccPeriod());
							sql.append(" order by CREATETIME");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							List<String[]> list = new ArrayList<String[]>();
							while(rs.next()){
								String[] exchange=new String[7];
								exchange[0] = rs.getString("id");
								exchange[1] = rs.getString("currencySign");
								exchange[2] = rs.getString("currencyName");
								exchange[3] = rs.getString("recordExchange");
								exchange[4] = rs.getString("adjustExchange");
								exchange[5] = rs.getString("periodYear");
								exchange[6] = rs.getString("period");
								list.add(exchange);
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("AdjustExchangeMgt queryExchange:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * ��ĩ����
	 * @param keyids
	 * @param adjustexchange
	 * @param accCode
	 * @param accAssort
	 * @param accTime
	 * @param credTypeID
	 * @param recordCommon
	 * @return
	 */
	public Result adjustExchange(final String[] keyIds,final String[] currentexchange,final String[] adjustexchange,
			final String accCode,final String accTime,
			final String credTypeID,final String recordCommon,final AccPeriodBean accPeriodBean,
			final LoginBean loginBean, final String profitLoss, final String settle,final Locale locale,final MessageResources resources){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement ps = conn.prepareStatement("select COUNT(0) from tblAccTypeInfo where isAdjustExchange=1");
							ResultSet rs = ps.executeQuery();
							if (rs.next()) {
								if(rs.getInt(1) ==0){
									BaseEnv.log.error("û���κ�����Ϊ��δ����Ŀ�Ŀ");
									result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
									result.setRetVal("û���κ�����Ϊ��δ����Ŀ�Ŀ");
									return  ;
								}
	                        }
							
							
							String sunCompany = loginBean.getSunCmpClassCode();
							SettleCostMgt costMgt = new SettleCostMgt(null,null,profitLoss,settle);
							/* ��ѯ��Ҫ�����Ļ���*/
							String exchangeIds = "";
							HashMap currentMap = new HashMap<String, String>();					//��ǰ����
							HashMap adjustMap = new HashMap<String, String>();					//��������
							HashMap currencyIdsMap = new HashMap<String, String>();				//�ұ�id
							HashMap currencyNameMap = new HashMap<String, String>();			//�ұ�����
							HashMap currencySignMap = new HashMap<String, String>();			//�ұ����
							StringBuffer sql = new StringBuffer("SELECT id,CurrencyName,isnull(adjustExchange,0) as adjustExchange,isnull(recordExchange,0) as recordExchange,PeriodYear,Period,isnull(currency,'') as currency");
							sql.append(",currencyName,currencySign");
							sql.append(" FROM tblSetExchange WHERE id=? order by CREATETIME");
							ps = conn.prepareStatement(sql.toString());
							for(int i=0;i<keyIds.length;i++){
								ps.setString(1, keyIds[i]);
								rs = ps.executeQuery();
								if(rs.next()){
									//Double adjustExchange = Double.valueOf(rs.getString("adjustExchange"));
									Double adjustexchanges = Double.valueOf(adjustexchange[i]);
									Double currentexchanges = Double.valueOf(currentexchange[i]);
									currencyNameMap.put(rs.getString("id"), rs.getString("currencyName"));
									currencySignMap.put(rs.getString("id"), rs.getString("currencySign"));
									currencyIdsMap.put(rs.getString("id"), rs.getString("currency"));
									/* ��������*/
									if(adjustexchanges-currentexchanges!=0){
										/* ����ids*/
										exchangeIds = exchangeIds + "'"+rs.getString("id")+"',";
										/* �޸ĵĵ�ǰ����*/
										currentMap.put(rs.getString("id"), currentexchanges);
										/* �޸ĵĵ�������*/
										adjustMap.put(rs.getString("id"), adjustexchanges);
										
									}
								}
								rs.close();
							}
							ps.close();
							
							//���������ĩ����ƾ֤����ɾ��������ĩ����ƾ֤����������
							sql = new StringBuffer("select id,isnull(workFlowNodeName,'') as workFlowNodeName,BillDate,createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID from tblAccMain where CredYear=? and CredMonth=? and Period=? and RefBillType='adjustExchange'");
							ps = conn.prepareStatement(sql.toString());
							ps.setInt(1, accPeriodBean.getAccYear());
							ps.setInt(2, accPeriodBean.getAccMonth());
							ps.setInt(3, accPeriodBean.getAccPeriod());
							rs = ps.executeQuery();
							List<String[]> delList = new ArrayList<String[]>();
							while (rs.next()) {
								String[] str = new String[8];
								str[0] = rs.getString("id");
								str[1] = rs.getString("workFlowNodeName");
								str[2] = rs.getString("BillDate");
								str[3] = rs.getString("createBy");
								str[4] = rs.getString("createTime");
								str[5] = rs.getString("lastUpdateBy");
								str[6] = rs.getString("lastUpdateTime");
								str[7] = rs.getString("SCompanyID");
								delList.add(str);
							}
							/* ɾ��*/
							String deletesql = "";
							for (int i = 0; i < delList.size(); i++) {
								String[] strs = delList.get(i);
								if("finish".equals(strs[1])){
									/* ɾ��֮ǰ������ѹ������ȷ�����*/
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("tblAccMain_Del_One");
									if (defineSqlBean == null) {
			                            BaseEnv.log.error("Define Sql Not Exist :Name = tblAccMain_Del_One");
			                            result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
			                            return  ;
			                        }
									HashMap<String,String> hashmap = new HashMap<String,String>();
									hashmap.put("tblAccMain_BillDate", strs[2]);
									hashmap.put("tblAccMain_id", strs[0]);
									hashmap.put("tblAccMain_createBy", strs[3]);
									hashmap.put("tblAccMain_createTime", strs[4]);
									hashmap.put("tblAccMain_lastUpdateBy", strs[5]);
									hashmap.put("tblAccMain_lastUpdateTime", strs[6]);
									hashmap.put("tblAccMain_SCompanyID", strs[7]);
			                        //ȡhashMap�е�����
			                        Result ret = defineSqlBean.execute(conn, hashmap, loginBean.getId(),resources,locale,"");
			                        if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			                        	result.retCode=ret.getRetCode();
			                        	result.retVal=ret.getRetVal();
			                        	return ;
			                        }
								}
								deletesql = "delete from tblAccDetail where f_ref ='" + strs[0] + "'";
								Statement cs = conn.createStatement();
								cs.execute(deletesql);
								deletesql = "delete from tblAccMain where id='" + strs[0] + "'";
								cs.execute(deletesql);
								deletesql = "delete from tblExchangeHistory  where periodYear="+accPeriodBean.getAccYear()+" and periodMonth="+accPeriodBean.getAccPeriod();
								cs.execute(deletesql);
								
							}
							
							if(false){
								return;
							}
							
							/* ���ڵ����Ļ���*/
							if(exchangeIds.length()>0){
								exchangeIds = exchangeIds.substring(0,exchangeIds.length()-1);
							}else{
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								result.setRetVal("����û�б仯�������");
								return  ;
							}
							
							String mainId = IDGenerater.getId(); // ƾ֤�����ID
							boolean mainFlag = false;
							String longDate = BaseDateFormat.format(new Date(),
									BaseDateFormat.yyyyMMddHHmmss);
							double debits = 0; // ���н跽������
							double credits = 0; // ���д���������
							
							int digits =Integer.parseInt(BaseEnv.systemSet.get("DigitsAmount").getSetting());
							
							/**   ***************************** ��Ŀ�д�����ĩ���� *******************************  */
							/* ���ݻ���ڼ�����²�ѯƾ֤�д���Ҫ�����ƾ֤*/
							sql = new StringBuffer("select c.Currency,SubCode,a.PeriodBala,a.PeriodBalaBase,c.JdFlag,d.id exchangeid "
									+ "from tblAccBalance a "
									+ "join (select AccNumber from tblAccTypeInfo where tblAccTypeInfo.isAdjustExchange=1 and IsForCur=1 ) b on a.SubCode like b.AccNumber+'%' and ISNULL(CurType,'') <> '' "
									+ "join tblAccTypeInfo c on a.SubCode=c.AccNumber "
									+ "join tblSetExchange d on c.Currency=d.Currency where a.PeriodBalaBase >0 and a.Nyear=? and a.Period=? and  d.id in ("+exchangeIds+") ");
							ps = conn.prepareStatement(sql.toString());
							ps.setInt(1, accPeriodBean.getAccYear());
							ps.setInt(2, accPeriodBean.getAccPeriod());
							rs = ps.executeQuery();
							while(rs.next()){
								

								String accCodes = rs.getString("SubCode");  //��Ŀ���
								Double PeriodBala = rs.getDouble("PeriodBala"); //��ҽ��
								Double PeriodBalaBase = rs.getDouble("PeriodBalaBase"); //���ҽ��
								String exchangeid = rs.getString("exchangeid");//����Id
								String currencys = rs.getString("Currency");//�ұ�
								Integer jdFlag = rs.getInt("JdFlag"); //�������
								
								System.out.println(accCodes+":"+PeriodBala);
								
								Double adjustExchange = Double.parseDouble(String.valueOf(adjustMap.get(exchangeid)));
								
								Double dcmoney =  adjustExchange * PeriodBala -  PeriodBalaBase;			//��۽��
								
								BigDecimal bd = new BigDecimal(dcmoney);
								dcmoney = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								
								double debit = 0;
								double credit = 0;
								if(jdFlag==1){//�跽
									debit = dcmoney;
								}else{//����
									credit = dcmoney;
								}
								debits = new BigDecimal(debits).add(
										new BigDecimal(debit)).doubleValue();
								credits = new BigDecimal(credits).add(
										new BigDecimal(credit)).doubleValue();

								// ����ƾ֤��ϸ
								if (!(debit == 0 && credit == 0)) {
									/* ����ƾ֤��¼*/
									insertAccDetail(conn, "adjustExchange", accCodes, debit, 
											0, credit, 0, accPeriodBean, accTime, loginBean, mainId, profitLoss,currencys,adjustExchange);
									/* ���������ʷ������*/
									ExchangeHistoryBean bean = new ExchangeHistoryBean();
									bean.setAdjustmentRate(adjustExchange);
									
									bean.setAmount(PeriodBalaBase);
									bean.setRate((Double)currentMap.get(exchangeid));
									bean.setAttachItem(accCodes);
									bean.setCreateBy(loginBean.getId());
									bean.setCreateTime(longDate);
									bean.setDate(accTime);
									bean.setId(IDGenerater.getId());
									bean.setPeriodMonth(accPeriodBean.getAccMonth());
									bean.setPeriodYear(accPeriodBean.getAccYear());
									bean.setPeriod(accPeriodBean.getAccPeriod());
									bean.setAdjustmentRate(adjustExchange);
									bean.setJdFlag(jdFlag);
									bean.setCurrency(currencys);
									bean.setFCAmount(adjustExchange * PeriodBala);
									bean.setFCMarg(dcmoney);
									addExchangeHistory(bean,conn);
									mainFlag = true;
								}
							}
							
							/* ��������*/
							if(mainFlag){
								/* �õ�ƾ֤���ֵ*/
								sql = new StringBuffer("select isnull(max(OrderNo),0)+1 as maxOrderNo from tblAccMain where CredTypeID='"+credTypeID+"' " +
										"and CredMonth="+accPeriodBean.getAccMonth()+" and CredYear="+accPeriodBean.getAccYear()+"  and Period="+accPeriodBean.getAccPeriod());
								ps = conn.prepareStatement(sql.toString());
								ResultSet rss = ps.executeQuery();
								int OrderNo = 1;
								if (rss.next()) {
									OrderNo = rss.getInt("maxOrderNo");
								}
								rss.close();
								ps.close();
								/* ��������*/
								sql = new StringBuffer("insert into tblAccMain (id,BillDate,EmployeeID,RefBillType,");
								sql.append("RefBillNo,RefBillID,Period,CredYear,CredMonth,createBy,lastUpdateBy,");
								sql.append("createTime,lastUpdateTime,DepartmentCode,SCompanyID,CredTypeID,");
								sql.append("AutoBillMarker,workFlowNodeName,workFlowNode,checkPersons,OrderNo,isAuditing) values ");
								sql.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
								ps = conn.prepareStatement(sql.toString());
								ps.setString(1, mainId);
								ps.setString(2, accTime);
								ps.setString(3, loginBean.getId());
								ps.setString(4, "adjustExchange");
								ps.setString(5, "adjustExchange");
								ps.setString(6, "adjustExchange");
								ps.setInt(7, accPeriodBean.getAccPeriod());
								ps.setInt(8, accPeriodBean.getAccYear());
								ps.setInt(9, accPeriodBean.getAccMonth());
								ps.setString(10, loginBean.getId());
								ps.setString(11, loginBean.getId());
								ps.setString(12, longDate);
								ps.setString(13, longDate);
								ps.setString(14, loginBean.getDepartCode());
								ps.setString(15, loginBean.getSunCmpClassCode());
								ps.setString(16, credTypeID);
								ps.setString(17, "1");
								ps.setString(18, "notApprove");
								ps.setString(19, "0");
								ps.setString(20, "");
								ps.setInt(21, OrderNo);
								ps.setString(22, "start");
								ps.executeUpdate();
								
								/* ����һ��ƾ֤��ϸ��*/
								if (debits - credits > 0) {
									insertAccDetail(conn, "adjustExchange", accCode, 0, 0,
											new BigDecimal(debits).subtract(new BigDecimal(credits)).doubleValue(),
											0, accPeriodBean, accTime, loginBean, mainId, recordCommon,"",1);
									//���󣨲�����ƥ�䣩
//									//�޸Ļ������Ŀ�Ŀ���
//									costMgt.updateAccBalanceCurr(conn, cbs, cs,accCode, accPeriodBean.getAccPeriod(), accPeriodBean.getAccYear(), 0,
//											new BigDecimal(debits).subtract(new BigDecimal(credits)).doubleValue(), 0, 0, 2,sunCompany, loginBean.getDepartCode());
								} else {
									insertAccDetail(conn, "adjustExchange", accCode, new BigDecimal(credits).subtract(new BigDecimal(debits)).doubleValue(), 0,
											0,0, accPeriodBean, accTime, loginBean, mainId,recordCommon,"",1);
									//���󣨲�����ƥ�䣩
									//�޸Ļ������Ŀ�Ŀ���
//									costMgt.updateAccBalanceCurr(conn, cbs, cs,accCode, accPeriodBean.getAccPeriod(), accPeriodBean.getAccYear(),
//											new BigDecimal(credits).subtract(new BigDecimal(debits)).doubleValue(), 0, 0, 0, 2,sunCompany, loginBean.getDepartCode());
								}
								HashMap values = new HashMap();
								values.put("id", mainId);
								values.put("BillDate", accTime);
								values.put("createBy", loginBean.getId());
								values.put("createTime", longDate);
								values.put("lastUpdateBy", loginBean.getId());
								values.put("lastUpdateTime", longDate);
								values.put("SCompanyID", loginBean.getSunCmpClassCode());
								VoucherMgt voucherMgt = new VoucherMgt();
								/* ƾ֤���� */
								sql = new StringBuffer("select IsAuditing from tblAccMainSetting");
								Statement cs = conn.createStatement();
								ResultSet rset = cs.executeQuery(sql.toString());
								int isAuditing = 0;
								if(rset.next()){
									isAuditing = rset.getInt("IsAuditing");
								}
								/* ���������*/
								if(isAuditing==0){
									//����
									voucherMgt.dealbind(conn, locale, loginBean, values, resources);
								}
							}


							
							
							/* ��ѯ��һ������ڼ�ͻ���ڼ���*/
							sql = new StringBuffer("select top 1 AccPeriod,AccMonth,AccYear from tblAccPeriod where SCompanyID='"+sunCompany+"'");
							sql.append("and ((accYear="+accPeriodBean.getAccYear()+" and accPeriod>"+accPeriodBean.getAccPeriod()+")");
							sql.append(" or accYear>"+ accPeriodBean.getAccYear()+ ") order by accYear,accPeriod");
							Statement cs = conn.createStatement();
							rs = cs.executeQuery(sql.toString());
							AccPeriodBean accPeriodBean = new AccPeriodBean();
							accPeriodBean.setAccMonth(0);
							accPeriodBean.setAccPeriod(0);
							accPeriodBean.setAccYear(0);
							if (rs.next()) {
								accPeriodBean.setAccPeriod(rs.getInt(1));
								accPeriodBean.setAccMonth(rs.getInt(2));
								accPeriodBean.setAccYear(rs.getInt(3));
							}
							/* �޸Ļ���*/
							String exchangesql = "update tblSetExchange set AdjustExchange=? where id=?";
							PreparedStatement pss = conn.prepareStatement(exchangesql);
							for(int i=0;i<keyIds.length;i++){
								Double adjustValue = Double.valueOf(adjustexchange[i]);
								Double curValue = Double.valueOf(currentexchange[i]);
								
								pss.setDouble(1, adjustValue);
								pss.setString(2, keyIds[i]);
								pss.addBatch();
								
								/* ��ѯ�Ƿ������һ������ڼ�Ļ���*/
								sql = new StringBuffer("select id from tblSetExchange where currency='"+currencyIdsMap.get(keyIds[i])+"'");
								sql.append(" and period="+accPeriodBean.getAccPeriod()+" and periodYear="+accPeriodBean.getAccYear());
								rs = cs.executeQuery(sql.toString());
								String currencyid = "";
								while(rs.next()){
									currencyid = rs.getString("id");
									System.out.println("-----------"+keyIds[i]+":"+currencyid);
								}
								
								/* ������һ���ڼ�Ļ����޸ģ����������*/
								if(!"".equals(currencyid)){
									//���ڼ�¼
									sql = new StringBuffer("update tblSetExchange set recordExchange="+adjustValue+",AdjustExchange=0 where id='"+currencyid+"'");
								}else{
									sql = new StringBuffer("insert into tblSetExchange(id,CurrencyName,CurrencySign,Period,RecordExchange,AdjustExchange,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,sCompanyID,Currency,PeriodYear)");
									sql.append(" values('"+IDGenerater.getId()+"','"+currencyNameMap.get(keyIds[i])+"','"+currencySignMap.get(keyIds[i])+"',"+accPeriodBean.getAccPeriod()+","+adjustValue+",");
									sql.append("0,'1','1','"+longDate+"','"+longDate+"',0,'"+loginBean.getSunCmpClassCode()+"','"+currencyIdsMap.get(keyIds[i])+"',"+accPeriodBean.getAccYear()+")");
								}
								BaseEnv.log.debug("update SetExchange="+sql.toString());
								cs.executeUpdate(sql.toString());
							}
							pss.executeBatch();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("AdjustExchangeMgt adjustExchange:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * ����ƾ֤��ϸ
	 * @param conn
	 * @param refBillType
	 * @param accCode
	 * @param debits
	 * @param debitsCurrency
	 * @param credits
	 * @param creditsCurrency
	 * @param accPeriodBean
	 * @param accTime
	 * @param loginBean
	 * @param mainId
	 * @throws Exception
	 */
	public void insertAccDetail(Connection conn,String refBillType,String accCode,
			double debits,double debitsCurrency,double credits,double creditsCurrency,
			AccPeriodBean accPeriodBean,String accTime,LoginBean loginBean,String mainId,
			String RecordComment,String currency,double currencyRate) throws Exception{
		String addsql = "insert into  tblAccDetail (id,RefBillType,AccCode,DebitAmount,DebitCurrencyAmount,LendAmount,LendCurrencyAmount,PeriodYear,PeriodMonth,"
			+ "AccDate,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,f_ref,RefBillID,RecordComment,SCompanyID,currency,currencyRate) "
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String longDate = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		int digits =Integer.parseInt(BaseEnv.systemSet.get("DigitsAmount").getSetting());

		debits = Math.round(debits * Math.pow(10, digits)) / Math.pow(10, digits);
		debitsCurrency = Math.round(debitsCurrency * Math.pow(10, digits))
				/ Math.pow(10, digits);
		credits = Math.round(credits * Math.pow(10, digits))
				/ Math.pow(10, digits);
		creditsCurrency = Math.round(creditsCurrency * Math.pow(10, digits))
				/ Math.pow(10, digits);
		PreparedStatement ps = conn.prepareStatement(addsql);
		ps.setString(1, IDGenerater.getId());
		ps.setString(2, refBillType);
		ps.setString(3, accCode);
		ps.setDouble(4, debits);
		ps.setDouble(5, debitsCurrency);
		ps.setDouble(6, credits);
		ps.setDouble(7, creditsCurrency);
		ps.setInt(8, accPeriodBean.getAccYear());
		ps.setInt(9, accPeriodBean.getAccMonth());
		ps.setString(10, accTime);
		ps.setString(11, loginBean.getId());
		ps.setString(12, loginBean.getId());
		ps.setString(13, longDate);
		ps.setString(14, longDate);
		ps.setInt(15, 0);
		ps.setString(16, mainId);
		ps.setString(17, refBillType);
		ps.setString(18, RecordComment);
		ps.setString(19, loginBean.getSunCmpClassCode());
		ps.setString(20, currency);
		ps.setDouble(21, currencyRate);
		ps.executeUpdate();
	}
	
	/**
	 * ��ӵ�����ʷ��¼
	 * @param bean
	 */
	public void addExchangeHistory(ExchangeHistoryBean bean,Connection conn) throws Exception {
		String sql = "insert into tblExchangeHistory(id,date,periodYear,periodMonth,attachItem,amount,rate,adjustmentRate,"
				+ "createBy,createTime,jdFlag,period,currency,FCAmount,FCMarg)";
		sql += " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, bean.getId());
		ps.setString(2, bean.getDate());
		ps.setInt(3, bean.getPeriodYear());
		ps.setInt(4, bean.getPeriodMonth());
		ps.setString(5, bean.getAttachItem());
		ps.setDouble(6, bean.getAmount());
		ps.setDouble(7, bean.getRate());
		ps.setDouble(8, bean.getAdjustmentRate());
		ps.setString(9, bean.getCreateBy());
		ps.setString(10, bean.getCreateTime());
		ps.setInt(11, bean.getJdFlag());
		ps.setInt(12, bean.getPeriod());
		ps.setString(13,bean.getCurrency());
		ps.setDouble(14,bean.getFCAmount());
		ps.setDouble(15,bean.getFCMarg());		
		ps.executeUpdate();
	}
	
}
