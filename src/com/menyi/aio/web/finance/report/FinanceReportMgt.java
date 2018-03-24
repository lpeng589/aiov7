package com.menyi.aio.web.finance.report;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.write.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.*;

/**
 * ���񱨱� ���ݿ������
 * <p>Title:���в��񱨱����mgt</p> 
 * <p>Description: �ܷ����˱�����ϸ�����˱���</p>
 *
 * @Date:2013-05-21
 * @Copyright: �������
 * @Author fjj
 */
public class FinanceReportMgt extends AIODBManager {
	
	/**
	 * ��ѯ���б�������
	 * @return
	 */
	public Result queryCurrencyAll(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT id,CurrencyName,case IsBaseCurrency when 1 then 'true' when 2 then 'false' end as IsBaseCurrency,currencySign FROM tblCurrency order by IsBaseCurrency DESC");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							List<String[]> list = new ArrayList<String[]>();
							while(rs.next()){
								String[] str=new String[4];
								str[0] = rs.getString("id");
								str[1] = rs.getString("CurrencyName");
								str[2] = rs.getString("IsBaseCurrency");
								str[3] = rs.getString("currencySign");
								list.add(str);
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryCurrencyAll:",ex) ;
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
	 * ��ѯ�Ƿ�λ��
	 * @param id   �ұ�ID
	 * @return
	 */
	public Result queryIsBaseCurrency(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT IsBaseCurrency FROM tblCurrency where id='"+id+"'");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							String falg = "false";
							if(rs.next()){
								Integer isBaseCurrency = rs.getInt("IsBaseCurrency");
								if(isBaseCurrency==1){
									falg = "true";
								}
							}
							result.setRetVal(falg);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryIsBaseCurrency:",ex) ;
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
	 * ��ѯ��С�ڼ������ڼ�
	 * @return
	 */
	public Result queryPeriod(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT MIN(AccYear) AS minAccYear,MAX(AccYear) AS maxAccYear,MIN(AccPeriod) AS minAccPeriod,MAX(AccPeriod) AS maxAccPeriod");
							sql.append(",MIN(PeriodBegin) AS minPeriodBegin,MAX(PeriodEnd) AS maxPeriodEnd FROM tblAccPeriod tap");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							String[] str = new String[6];
							if(rs.next()){
								str[0] = String.valueOf(rs.getInt("minAccYear"));
								str[1] = String.valueOf(rs.getInt("maxAccYear"));
								str[2] = String.valueOf(rs.getInt("minAccPeriod"));
								str[3] = String.valueOf(rs.getInt("maxAccPeriod"));
								str[4] = rs.getString("minPeriodBegin");
								str[5] = rs.getString("maxPeriodEnd");
							}
							result.setRetVal(str);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryPeriod:",ex) ;
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
	 * �ܷ����˱����ѯ
	 * 1����ѯ��Χ�ڵ����п�Ŀ��
	 * 2����ѯ��Χ�ڵ����л���ڼ䡣
	 * 3�����ܷ����˱�tblAccBalance��ѯ�Ѿ�ͳ�Ƶ����� 
	 * 4����ѯδ����ƾ֤��ϸ���ݡ�����ƾ֤��ϸ��ȡ��Χ���������� ������������Ŀ������ڼ��꣬��,ƾ֤�֣���ң�ȡ���������ܽ������Сƾ֤�ţ����ƾ֤��
	 * 5���������չʾ
	 * @param periodYearStart ����ڼ俪ʼ��
	 * @param periodStart ��ʼ����ڼ�
	 * @param periodYearEnd ����ڼ������
	 * @param periodEnd ��������ڼ�
	 * @param levelStart ��ʼ�ȼ�
	 * @param levelEnd �����ȼ�
	 * @param acctypeCodeStart ��ʼ��Ŀ
	 * @param acctypeCodeEnd ������Ŀ
	 * @param currencyName ����
	 * @param takeBrowNo �޷������ʾ Ϊ�ղ���ʾ
	 * @param balanceAndTakeBrowNo �޷���������Ϊ�� Ϊ�ղ���ʾ
	 * @param binderNo ����δ����ƾ֤
	 * @param showIsItem ��ʾ�����Ŀ
	 * @param loginBean �û���½��Ϣ
	 * @param showBanAccTypeInfo ��ʾ���ÿ�Ŀ
	 * @param pageSize ÿҳ����������
	 * @param pageNo ��ǰ�ڼ�ҳ
	 * @return
	 */
	public Result queryAccBalanceData(final String periodYearStart,final String periodStart,
			final String periodYearEnd,final String periodEnd,final String levelStart,
			final String levelEnd,final String acctypeCodeStart,final String acctypeCodeEnd,
			final String currencyName,final String takeBrowNo,final String balanceAndTakeBrowNo,
			final String binderNo,final String showIsItem,final String showBanAccTypeInfo,
			final LoginBean loginBean,final Integer pageSize,final Integer pageNo,final MOperation mop){
		//�Աұ���д���
		String currencyNames = currencyName; 									//�ұ�('isBase'=��λ�ң�''=�ۺϱ�λ�ң�'all'=���бұ�'��ҵ�id'=���)
		Result rs = queryIsBase(currencyNames);
		currencyNames = rs.retVal.toString();
		//�������
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		final String currencyValue = currencyNames;
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	Statement st = conn.createStatement();
						ResultSet rset = null;
						
						/* ����ʼ�ڼ�ͽ����ڼ��ʽΪ��2013-08�� */
						String startTime = "";
						String endTime = "";
						if(Integer.valueOf(periodStart)<10){
							startTime = periodYearStart+"-0"+periodStart;
						}else{
							startTime = periodYearStart+"-"+periodStart;
						}
						if(Integer.valueOf(periodEnd)<10){
							endTime = periodYearEnd+"-0"+periodEnd;
						}else{
							endTime = periodYearEnd+"-"+periodEnd;
						}
						
						/**
                         * �ڼ��������
                         */
                        String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,ap.AccYear)+'-'+CONVERT(VARCHAR,ap.AccPeriod)+'-01',120),120)";
                        String periodCondition = " AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'";
                        
                        HashMap accMap = new HashMap();
                        String condition = scopeSql(mop, loginBean);				//��������
                        
						/**
						 * ��ѯ�������������Ļ�ƿ�Ŀ
						 */
						StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,l.zh_cn as AccFullName,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,");
						sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.jdFlag FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 ");
						if(acctypeCodeStart != null && !"".equals(acctypeCodeStart)){
							//��Ŀ��ʼ
							condition += " AND (tblAccTypeInfo.AccNumber>='"+acctypeCodeStart+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeStart+"')+'%')";
						}
						if(acctypeCodeEnd != null && !"".equals(acctypeCodeEnd)){
							//��Ŀ����
							condition += " AND (tblAccTypeInfo.AccNumber<='"+acctypeCodeEnd+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeEnd+"')+'%')";
						}
						if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
							//����ʾ���ÿ�Ŀ
							condition += " AND tblAccTypeInfo.statusId=0";
						}
						sql.append(condition);
						
						sql.append(" ORDER BY tblAccTypeInfo.AccNumber");
						System.out.println("��Ŀ��ѯSQL��"+sql.toString());
                        /* ִ�в�ѯ���ұ������������Ŀ�Ŀ */
                        rset=st.executeQuery(sql.toString());
                        List accTypeInfoList = new ArrayList();
                        while (rset.next()) {
                        	HashMap map=new HashMap();
                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                        		Object obj=rset.getObject(i);
                        		if(obj==null){
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                        				map.put(rset.getMetaData().getColumnName(i), 0);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), "");
                        			}
                        		}else{
                        			map.put(rset.getMetaData().getColumnName(i), obj);
                        		}
                        	}
                        	accMap.put(map.get("classCode")+"_isCalculate", map.get("isCalculate"));
                        	accMap.put(map.get("classCode")+"_jdFlag", map.get("jdFlag"));
                        	accMap.put(map.get("classCode")+"_AccNumber", map.get("AccNumber"));
                        	accTypeInfoList.add(map);
                        }
                        
                        /**
                         * ��ѯ���������Ļ���ڼ�
                         */
                        sql = new StringBuffer("SELECT ap.AccYear,ap.AccPeriod FROM tblAccPeriod ap WHERE 1=1 ");
                        sql.append(periodCondition);
						sql.append(" ORDER BY ap.AccYear,ap.AccPeriod");
						rset=st.executeQuery(sql.toString());
						List periodList = new ArrayList();
						while (rset.next()) {
							HashMap map=new HashMap();
                          	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                          		Object obj=rset.getObject(i);
                          		if(obj==null){
                          			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                          				map.put(rset.getMetaData().getColumnName(i), 0);
                          			}else{
                          				map.put(rset.getMetaData().getColumnName(i), "");
                          			}
                          		}else{
                          			map.put(rset.getMetaData().getColumnName(i), obj);
                          		}
                          	}
                          	periodList.add(map);
						}
                        
						/**
                         * ����δ����ƾ֤����ѯָ���ڼ��δ�������ݽ��
                         */
                        HashMap noBindMap = new HashMap();				//�����ڼ��е�δ���˵�����
                        HashMap noBindInitMap = new HashMap();			//���濪ʼ�ڼ�֮ǰδ���˵�����
						if(binderNo != null && !"".equals(binderNo)){
							String initSql = "";						//��ѯ�ڳ�δ��������sql							
							
							sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,tblAccMain.CredYear,tblAccMain.Period,SUM(DebitAmount) AS PeriodDebitSumBase,");
                            sql.append("SUM(LendAmount) AS PeriodCreditSumBase,SUM(DebitAmount)-SUM(LendAmount) AS PeriodDCBalaBase, ");
                            sql.append("SUM(DebitAmount) AS CurrYIniDebitSumBase,SUM(LendAmount) AS CurrYIniCreditSumBase,SUM(DebitAmount)-SUM(LendAmount) AS CurrYIniAmount ");
                            if(!"".equals(currencyValue)){
                            	sql.append(",SUM(DebitCurrencyAmount) AS PeriodDebitSum,SUM(LendCurrencyAmount) AS PeriodCreditSum ");
                            	sql.append(",SUM(DebitCurrencyAmount) AS CurrYIniDebitSum,SUM(LendCurrencyAmount) AS CurrYIniCreditSum,SUM(DebitCurrencyAmount)-SUM(LendCurrencyAmount) AS CurrYIni,");
                            	sql.append("SUM(DebitCurrencyAmount)-SUM(LendCurrencyAmount) AS PeriodDCBala ");
                            	if("all".equals(currencyValue)){
                            		sql.append(",isnull(tblAccDetail.Currency,'') as Currency ");
                            	}
                            }
                            
                            sql.append("FROM  tblAccDetail ");
                            sql.append("LEFT JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id LEFT JOIN tblAccTypeInfo tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode ");
                            sql.append("WHERE tblAccMain.workFlowNodeName!='finish' ");
                            //��ѡ����Ҳ���<�ۺϱ�λ��><���б���>ʱ���й���
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		sql.append(" AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+currencyName+"') ");
                            	}else{
                            		sql.append(" AND tblAccDetail.Currency='"+currencyName+"' ");
                            	}
                            }
                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
                            initSql = sql.toString().replaceAll("tblAccMain.CredYear,tblAccMain.Period,", "")+" AND "+str+"<'"+startTime+"' "+condition+" GROUP BY tblAccTypeInfo.classCode";
                            sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
                            sql.append(condition);
                            sql.append(" GROUP BY tblAccTypeInfo.classCode,tblAccMain.CredYear,tblAccMain.Period");
                            if("all".equals(currencyValue)){
                            	//���б���Ҫ����ҽ��з���
                            	sql.append(",tblAccDetail.Currency");
                            	initSql += ",tblAccDetail.Currency";
                            }
                            sql.append(" ORDER BY tblAccTypeInfo.classCode,tblAccMain.CredYear,tblAccMain.Period");
                            BaseEnv.log.error("δ����ƾ֤���ݽ�"+sql.toString());
                            rset = st.executeQuery(sql.toString());
                            while (rset.next()) {
                            	HashMap periodMap=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			periodMap.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					periodMap.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				periodMap.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	//��ѡ��ıұ��ǡ������бұ����ʽ
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
                            		if("".equals(periodMap.get("Currency"))){
                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					periodMap.put(s+"_", periodMap.get(s+"Amount"));
                            				}else{
                            					periodMap.put(s+"_", periodMap.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//�������
                            			for(String s : curs){
                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
                            			}
                            		}
                            	}
                            	noBindMap.put(periodMap.get("CredYear")+"_"+periodMap.get("Period")+"_"+periodMap.get("classCode"), periodMap);
                            }
                            
                            //��ѯ�ڳ�����ʱ���в�ѯ����ڼ�֮ǰδ���˵����ݽ���ͳ��
                            rset = st.executeQuery(initSql);
                            while (rset.next()) {
                            	HashMap periodMap=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			periodMap.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					periodMap.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				periodMap.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	//��ѡ��ıұ��ǡ������бұ����ʽ
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
                            		if("".equals(periodMap.get("Currency"))){
                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					periodMap.put(s+"_", periodMap.get(s+"Amount"));
                            				}else{
                            					periodMap.put(s+"_", periodMap.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//�������
                            			for(String s : curs){
                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
                            			}
                            		}
                            	}
                            	noBindInitMap.put(periodMap.get("classCode"), periodMap);
                            }
						}
                        
						/**
                         * ��ѯ��Ŀ�ĸ��ڼ�Ľ�tblAccBalance��
                         */                       											
						sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccBalance.Nyear,");
                        sql.append("tblAccBalance.Period,tblAccBalance.SubCode,ISNULL(tblAccTypeInfo.isCataLog,0) as isCataLog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,");
                        sql.append("(case tblAccTypeInfo.JdFlag when 2 then 0-tblAccBalance.PeriodIniBase else tblAccBalance.PeriodIniBase end) as PeriodIniBase,");
                        sql.append("(case tblAccTypeInfo.JdFlag when 2 then 0-tblAccBalance.PeriodIni else tblAccBalance.PeriodIni end) as PeriodIni,");
                        sql.append("tblAccBalance.PeriodDebitSumBase,tblAccBalance.PeriodDebitSum,tblAccBalance.PeriodCreditSumBase,tblAccBalance.PeriodCreditSum,");
                        sql.append("tblAccBalance.CurrYIniDebitSumBase,tblAccBalance.CurrYIniDebitSum,tblAccBalance.CurrYIniCreditSumBase,tblAccBalance.CurrYIniCreditSum,");
                        sql.append("tblAccBalance.PeriodBalaBase,tblAccBalance.PeriodBala,(tblAccBalance.CurrYIniDebitSumBase-tblAccBalance.CurrYIniCreditSumBase) as CurrYIniAmount,");
                        sql.append("(tblAccBalance.CurrYIniDebitSum-tblAccBalance.CurrYIniCreditSum) as CurrYIni,");
                        sql.append("(tblAccBalance.PeriodDebitSumBase-tblAccBalance.PeriodCreditSumBase) AS PeriodDCBalaBase,");
                        sql.append("(tblAccBalance.PeriodDebitSum-tblAccBalance.PeriodCreditSum) AS PeriodDCBala,tblAccBalance.CurType,");                        
                        sql.append("tblAccTypeInfo.jdFlag FROM tblAccBalance left join tblAccTypeInfo ON tblAccBalance.SubCode=tblAccTypeInfo.AccNumber");
                        sql.append(" WHERE tblAccBalance.Nyear!=-1 AND tblAccBalance.period!=-1 ");
                        if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                        	if("isBase".equals(currencyValue)){
                        		//��λ��
                        		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+currencyName+"') ");
                        	}else{
                        		//������ҽ��й���
                        		sql.append(" AND tblAccBalance.CurType='"+currencyName+"' ");
                        	}
                        }
                        sql.append(condition);
                        str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccBalance.Nyear)+'-'+CONVERT(VARCHAR,tblAccBalance.Period)+'-01',120),120)";
                        sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"' AND tblAccTypeInfo.isCatalog=0 and ISNULL(tblAccTypeInfo.isCalculateParent,0) != 1  AND (tblAccBalance.DepartmentCode is null or tblAccBalance.DepartmentCode='')");
                        sql.append(" ORDER BY tblAccTypeInfo.classCode,tblAccBalance.Nyear,tblAccBalance.Period");
                        System.out.println("��ѯ�ܷ����˸���ƿ�Ŀ�ĸ�����ڼ�Ľ�"+sql.toString());
                        rset=st.executeQuery(sql.toString());
                        HashMap accBalanceMap = new HashMap();
                        while (rset.next()) {
                        	HashMap map=new HashMap();
                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                        		Object obj=rset.getObject(i);
                        		if(obj==null){
                        			map.put(rset.getMetaData().getColumnName(i), "");
                        		}else{
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                    					String strvalue = String.valueOf(obj);
                    					if (strvalue.indexOf(".")>0){
                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                    					}
                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        					strvalue = "0";
                    					}
                    					if(Double.valueOf(strvalue)==0){
                    						strvalue = "";
                    					}
                        				map.put(rset.getMetaData().getColumnName(i), strvalue);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), obj);
                        			}
                        		}
                        	}
                        	
                        	//��ѡ��ıұ��ǡ������бұ����ʽ
                        	if("all".equals(currencyValue)){
                        		String[] curs = new String[]{"PeriodIni","PeriodDebitSum","PeriodCreditSum","CurrYIniDebitSum","CurrYIniCreditSum","PeriodBala","CurrYIni","PeriodDCBala"};
                        		if("".equals(map.get("CurType"))){
                        			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
                        			for(String s : curs){
                        				if("CurrYIni".equals(s)){
                        					map.put(s+"_", map.get(s+"Amount"));
                        				}else{
                        					map.put(s+"_", map.get(s+"Base"));
                        				}
                        			}
                        		}else{
                        			//�������
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("CurType"), map.get(s));
                        			}
                        		}
                        	}
                        	
            				//��ѯ�˿�Ŀ���ϼ�����ֵ���ϼ���
        					String classCode = map.get("classCode").toString();
        					for(int j=0;j<(classCode.length()-5)/5;j++){
        						//��ƿ�ĿclassCode����ѭ����ȡ�ϼ�
        						HashMap oldMap = (HashMap)accBalanceMap.get(map.get("Nyear")+"_"+map.get("Period")+"_"+classCode.substring(0,5+j*5));
    							String[] moneyStr = null;
    							if("all".equals(currencyValue)){
    								moneyStr = new String[]{"PeriodIniBase","PeriodIni_"+map.get("CurType"),"PeriodDebitSumBase","PeriodDebitSum_"+map.get("CurType"),"PeriodCreditSumBase","PeriodCreditSum_"+map.get("CurType"),"CurrYIniDebitSumBase","CurrYIniDebitSum_"+map.get("CurType")
    										,"CurrYIniCreditSumBase","CurrYIniCreditSum_"+map.get("CurType"),"PeriodBalaBase","PeriodBala_"+map.get("CurType"),"CurrYIniAmount","CurrYIni_"+map.get("CurType"),"PeriodDCBalaBase","PeriodDCBala_"+map.get("CurType")};
    							}else{
    								moneyStr = new String[]{"PeriodIniBase","PeriodIni","PeriodDebitSumBase","PeriodDebitSum","PeriodCreditSumBase","PeriodCreditSum","CurrYIniDebitSumBase","CurrYIniDebitSum"
    										,"CurrYIniCreditSumBase","CurrYIniCreditSum","PeriodBalaBase","PeriodBala","CurrYIniAmount","CurrYIni","PeriodDCBalaBase","PeriodDCBala"};
    							}
    							if(oldMap!=null && oldMap.size()>0){
    								//�������ϼ�ʱ��ȡ�ϼ���������ۼ�
    								String moneys = "0";
    								for(String s : moneyStr){
    									moneys = "0";
    									Object o = oldMap.get(s);
    									if(o!=null && !"".equals(o)){
    										moneys = o.toString();
    									}
    									if(map.get(s)!=null && !"".equals(map.get(s))){
    										moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
    									}
    									String totalAmount = moneys;
    									if(Double.parseDouble(moneys)==0){
    										totalAmount = "";
    									}
    									oldMap.put(s,totalAmount);
    								}
    							}else{
    								String accnumber = String.valueOf(map.get("SubCode"));
    								//����������һ��ʱ�������µ�Map��������
    								oldMap = new HashMap();
    								oldMap.put("Nyear", map.get("Nyear"));
    								oldMap.put("Period", map.get("Period"));
    								Object accNumber = accMap.get(classCode.substring(0,5+j*5)+"_AccNumber");
    								oldMap.put("SubCode", accNumber);
    								oldMap.put("classCode", classCode.substring(0,5+j*5));
    								for(String s : moneyStr){
    									String totalAmount = "0";
        								if(map.get(s) != null && !"".equals(map.get(s))){
        									totalAmount = map.get(s).toString();
        								}
        								if(Double.valueOf(totalAmount)==0){
        									totalAmount = "";
        								}
        								oldMap.put(s,totalAmount);
    								}
    							}
    							accBalanceMap.put(map.get("Nyear")+"_"+map.get("Period")+"_"+classCode.substring(0,5+j*5),oldMap);
        					}
        					accBalanceMap.put(map.get("Nyear")+"_"+map.get("Period")+"_"+map.get("classCode"), map);
                        }
                        
                        /**
						 * ��ѯƾ֤�ֺ����ֵ����Сֵ
						 */
						sql = new StringBuffer("SELECT accmain.CredYear,accmain.Period,accmain.CredTypeID,acctype.classCode,");
						sql.append("MIN(OrderNo) AS MinOrderNo,MAX(OrderNo) AS MaxOrderNo FROM tblAccDetail detail ");
						sql.append("LEFT JOIN tblAccTypeInfo acctype ON detail.AccCode=acctype.AccNumber JOIN tblAccMain accmain ON detail.f_ref=accmain.id WHERE 1=1");
						if(binderNo == null || "".equals(binderNo)){
							sql.append(" AND accmain.workFlowNodeName='finish'");
						}
						//��ѡ����Ҳ���<�ۺϱ�λ��><���б���>ʱ���й���
                        if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                        	if("isBase".equals(currencyValue)){
                        		//��λ��
                        		sql.append(" AND (detail.Currency is null or detail.Currency='' or detail.Currency='"+currencyName+"') ");
                        	}else{
                        		sql.append(" AND detail.Currency='"+currencyName+"' ");
                        	}
                        }
						sql.append(" AND CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,accmain.CredYear)+'-'+CONVERT(VARCHAR,accmain.Period)+'-01',120),120)>='"+startTime+"' ");
						sql.append(" AND CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,accmain.CredYear)+'-'+CONVERT(VARCHAR,accmain.Period)+'-01',120),120)<='"+endTime+"' ");
						sql.append(" GROUP BY accmain.CredYear,accmain.Period,accmain.CredTypeID,acctype.classCode");
						rset=st.executeQuery(sql.toString());
						List OrderNoList = new ArrayList();
						while (rset.next()) {
							HashMap map=new HashMap();
                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                        		Object obj=rset.getObject(i);
                        		if(obj==null){
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                        				map.put(rset.getMetaData().getColumnName(i), 0);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), "");
                        			}
                        		}else{
                        			map.put(rset.getMetaData().getColumnName(i), obj);
                        		}
                        	}
                        	OrderNoList.add(map);
						}
                        
						/* ����ѯ���ڼ��ڱ�tblAccBalance������ʱ��ʹ�ڼ���ǰ�ƣ���ѯ����ļ�¼ */
						HashMap periodMap = new HashMap();
						if(periodList != null && periodList.size()>0){
							periodMap = (HashMap)periodList.get(0);
						};
						sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccBalance.Nyear,");
						sql.append("tblAccBalance.Period,tblAccBalance.SubCode,ISNULL(tblAccTypeInfo.isCataLog,0) as isCataLog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,");
						sql.append("(case tblAccTypeInfo.JdFlag when 2 then 0-tblAccBalance.CurrYIniBalaBase else tblAccBalance.CurrYIniBalaBase end) as PeriodIniBase,");
						sql.append("(case tblAccTypeInfo.JdFlag when 2 then 0-tblAccBalance.CurrYIniBala else tblAccBalance.CurrYIniBala end) as PeriodIni,");
						sql.append("tblAccBalance.CurrYIniDebitSumBase,tblAccBalance.CurrYIniDebitSum,tblAccBalance.CurrYIniCreditSumBase,tblAccBalance.CurrYIniCreditSum,");
						sql.append("(tblAccBalance.CurrYIniDebitSumBase-tblAccBalance.CurrYIniCreditSumBase) as CurrYIniAmount,");
						sql.append("(tblAccBalance.CurrYIniDebitSum-tblAccBalance.CurrYIniCreditSum) as CurrYIni,tblAccBalance.CurType,");
						sql.append(" '' AS PeriodDCBalaBase,'' AS PeriodDCBala,tblAccTypeInfo.jdFlag FROM tblAccBalance left join tblAccTypeInfo ON tblAccBalance.SubCode=tblAccTypeInfo.AccNumber WHERE");
						sql.append(" SubCode not in (select SubCode from tblAccBalance where "+str+"='"+startTime+"' AND Nyear!=-1 AND Period!=-1) AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1 AND (tblAccBalance.DepartmentCode is null or tblAccBalance.DepartmentCode='')");
						if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                        	if("isBase".equals(currencyValue)){
                        		//��λ��
                        		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+currencyName+"') ");
                        	}else{
                        		sql.append(" AND tblAccBalance.CurType='"+currencyName+"' ");
                        	}
                        }
						sql.append(" and tblAccBalance.Nyear =(select top 1 a.Nyear from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
						sql.append(" and tblAccBalance.Period =(select top 1 a.Period from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
						sql.append(condition + " ORDER BY tblAccTypeInfo.classCode");
						System.out.println("��ѯ�ܷ�������ӽ������ݣ�"+sql.toString());
						rset=st.executeQuery(sql.toString());
						while (rset.next()) {
							HashMap map=new HashMap();
							for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
								Object obj=rset.getObject(i);
								if(obj==null){
									map.put(rset.getMetaData().getColumnName(i), "");
								}else{
									if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
										String strvalue = String.valueOf(obj);
										if (strvalue.indexOf(".")>0){
											strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
										}
										if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
											strvalue = "0";
										}
                    					if(Double.valueOf(strvalue)==0){
                    						strvalue = "";
                    					}
										if(!"PeriodDebitSumBase".equals(rset.getMetaData().getColumnName(i)) 
												|| !"PeriodCreditSumBase".equals(rset.getMetaData().getColumnName(i))){
											map.put(rset.getMetaData().getColumnName(i), strvalue);
										}
									}else{
										map.put(rset.getMetaData().getColumnName(i), obj);
									}
								}
							}
							
							//��ѡ��ıұ��ǡ������бұ����ʽ
                        	if("all".equals(currencyValue)){
                        		String[] curs = new String[]{"PeriodIni","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni","PeriodDCBala"};
                        		if("".equals(map.get("CurType"))){
                        			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
                        			for(String s : curs){
                        				if("CurrYIni".equals(s)){
                        					map.put(s+"_", map.get(s+"Amount"));
                        				}else{
                        					map.put(s+"_", map.get(s+"Base"));
                        				}
                        			}
                        		}else{
                        			//�������
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("CurType"), map.get(s));
                        			}
                        		}
                        	}
							
							map.put("Nyear" ,periodMap.get("AccYear"));
							map.put("Period" ,periodMap.get("AccPeriod"));
							//��ѯ�˿�Ŀ���ϼ�����ֵ���ϼ���
							String classCode = map.get("classCode").toString();
							for(int j=0;j<(classCode.length()-5)/5;j++){
								//��ƿ�ĿclassCode����ѭ����ȡ�ϼ�
								HashMap oldMap = (HashMap)accBalanceMap.get(map.get("Nyear")+"_"+map.get("Period")+"_"+classCode.substring(0,5+j*5));
								String[] moneyStr = null;
								if("all".equals(currencyValue)){
    								moneyStr = new String[]{"PeriodIniBase","PeriodIni_"+map.get("CurType"),"CurrYIniDebitSumBase","CurrYIniDebitSum_"+map.get("CurType"),"CurrYIniCreditSumBase","CurrYIniCreditSum_"+map.get("CurType"),"CurrYIniAmount","CurrYIni_"+map.get("CurType")};
    							}else{
    								moneyStr = new String[]{"PeriodIniBase","PeriodIni","CurrYIniDebitSumBase","CurrYIniDebitSum","CurrYIniCreditSumBase","CurrYIniCreditSum","CurrYIniAmount","CurrYIni"};
    							}
								if(oldMap!=null && oldMap.size()>0){
									//�������ϼ�ʱ��ȡ�ϼ���������ۼ�
									String moneys = "0";
									for(String s : moneyStr){
										moneys = "0";
										Object o = oldMap.get(s);
										if(o!=null && !"".equals(o)){
											moneys = o.toString();
										}
										if(map.get(s)!=null && !"".equals(map.get(s))){
											if(s.indexOf("PeriodIni") != -1){
												if(accMap.get(classCode.substring(0,5+j*5)+"_jdFlag")!=null){
													int oldjdFlag = Integer.parseInt(accMap.get(classCode.substring(0,5+j*5)+"_jdFlag").toString());
													moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();
												}
											}else{
												moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
											}
										}
										String totalAmount = moneys;
										if(Double.parseDouble(moneys)==0){
											totalAmount = "";
										}
										oldMap.put(s,totalAmount);
									}
								}else{
									//����������һ��ʱ�������µ�Map��������
									oldMap = new HashMap();
									oldMap.put("Nyear", map.get("Nyear"));
									oldMap.put("Period", map.get("Period"));
									Object accNumber = accMap.get(classCode.substring(0,5+j*5)+"_AccNumber");
    								oldMap.put("SubCode", map.get("SubCode"));
									oldMap.put("classCode", classCode.substring(0,5+j*5));
									for(String s : moneyStr){
										String totalAmount = "0";
										if(map.get(s) != null && !"".equals(map.get(s))){
        									if(s.indexOf("PeriodIni") != -1){
    											if(accMap.get(classCode.substring(0,j*5)+"_jdFlag")!=null){
    												int oldjdFlag = Integer.parseInt(accMap.get(classCode.substring(0,j*5)+"_jdFlag").toString());
    												if(oldjdFlag == Integer.parseInt(map.get("jdFlag").toString())){
    													//�跽
    													totalAmount = new BigDecimal(totalAmount).add(new BigDecimal(map.get(s).toString())).toString();
    												}else{
    													totalAmount = new BigDecimal(totalAmount).subtract(new BigDecimal(map.get(s).toString())).toString();
    												}
    											}
    										}else{
    											totalAmount = map.get(s).toString();
    										}
        								}
										if(Double.valueOf(totalAmount)==0){
											totalAmount = "";
										}
										oldMap.put(s,totalAmount);
									}
								}
									accBalanceMap.put(map.get("Nyear")+"_"+map.get("Period")+"_"+classCode.substring(0,5+j*5),oldMap);
							}
							accBalanceMap.put(map.get("Nyear")+"_"+map.get("Period")+"_"+map.get("classCode"), map);
						}
						
						
						/**
						 * �����������ݵ�object��
						 * accTypeInfoList ��ƿ�Ŀ�б�,accBalanceMap �ܷ�����HashMap��ʽ����,
						 * periodList ����ڼ��б�,OrderNoList ƾ֤�ֺ��б�,noBindMap �ڼ���δ���˵�����,noBindInitMap �ڼ�֮ǰδ��������
						 */
                        Object[] object = new Object[]{accTypeInfoList,accBalanceMap,periodList,OrderNoList,noBindMap,noBindInitMap};
						rst.setRetVal(object);
	                }
	            });
	            return rst.getRetCode();
	        }
	    });
        rst.setRetCode(retCode);
        
        /**
         * �����ݽ�����װ
         */
        Object[] object = (Object[])rst.getRetVal();
        /* �õ����� */
	    List accTypeInfoList = (ArrayList)object[0];
	    HashMap accBalanceMap = (HashMap)object[1];
	    List periodList = (ArrayList)object[2];
	    List orderNoList = (ArrayList)object[3];
	    HashMap noBindMap = (HashMap)object[4];
	    HashMap noBindInitMap = (HashMap)object[5];
	      
		/* ��ƾ֤�ֺŶη������Ӧ��HashMap�� */
		for(int i=0;i<orderNoList.size();i++){
			HashMap map = (HashMap)orderNoList.get(i);
			HashMap accType_Period = (HashMap)accBalanceMap.get(map.get("CredYear")+"_"+map.get("Period")+"_"+map.get("classCode"));
			String str = map.get("CredTypeID").toString()+map.get("MinOrderNo").toString();
			if(!map.get("MaxOrderNo").toString().equals(map.get("MinOrderNo").toString())){
				str += "~"+map.get("MaxOrderNo").toString();
			}
			if(accType_Period!=null && accType_Period.size()>0){
				Object orderno = accType_Period.get("credTypeidOrderNo");
				if(orderno!=null && !"".equals(orderno)){
					//����ƾ֤�ֺ�
					str = orderno+","+str;
				}
			}else{
				accType_Period = new HashMap();
  			accType_Period.put("Nyear", map.get("CredYear"));
  			accType_Period.put("Period", map.get("Period"));
			}
			accType_Period.put("credTypeidOrderNo", str);
			accBalanceMap.put(map.get("CredYear")+"_"+map.get("Period")+"_"+map.get("classCode"),accType_Period);
		}
		
		//���ڼ���δ���˵����ݷ���map��
		if(noBindMap != null && noBindMap.size()>0){
			Iterator nobindMap = noBindMap.entrySet().iterator();
        	//����map�õ����������
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//�õ��������ݵ�map
				if(bindMap != null && bindMap.size()>0){
					//����δ���˵�����
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					for(int j=1;j<=bindClassCode.length()/5;j++){
						HashMap oldMap = (HashMap)accBalanceMap.get(bindMap.get("CredYear")+"_"+bindMap.get("Period")+"_"+bindClassCode.substring(0,j*5));
						String[] str1 = new String[]{"PeriodDebitSumBase","PeriodCreditSumBase","PeriodDCBalaBase","CurrYIniDebitSumBase","CurrYIniCreditSumBase","CurrYIniAmount",
								"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
						if("all".equals(currencyValue)){
							String[] currS = new String[(currList.size()+1)*6];
							int counts = 0;
							for(int k = 6;k<str1.length;k++){
								for(int l=0;l<currList.size();l++){
									String[] currStr = (String[])currList.get(l);
									if("true".equals(currStr[2])){
										//��λ��
										currS[counts] = str1[k]+"_";
									}else{
										currS[counts] = str1[k]+"_"+currStr[0];
									}
									counts ++;
								}
							}
							int curCount = currList.size()*6;
							for(int k=0;k<str1.length-6;k++){
								currS[curCount+k] = str1[k];
							}
							str1 = currS;
						}
						if(oldMap == null){
							oldMap = new HashMap();
							oldMap.put("Nyear", bindMap.get("CredYear"));
							oldMap.put("Period", bindMap.get("Period"));
						}
						for(int k=0;k<str1.length;k++){
							String moneys1 = "0";
							//������Ͻ��
							Object o1 = null;
							if(oldMap!=null && oldMap.size()>0){
								o1 = oldMap.get(str1[k]);
							}
							if(o1!=null && !"".equals(o1)){
								moneys1 = o1.toString();
							}
							Object o3 = bindMap.get(str1[k]);
							if(o3!=null && !"".equals(o3)){
								moneys1 = new BigDecimal(moneys1).add(new BigDecimal(o3.toString())).toString();
							}
							oldMap.put(str1[k],moneys1);
						}
						accBalanceMap.put(bindMap.get("CredYear")+"_"+bindMap.get("Period")+"_"+bindClassCode.substring(0,j*5),oldMap);
					}
				}
        	}
		}
		
		//���ڼ�֮ǰδ���˵����ݷ���map��
		if(noBindInitMap != null && noBindInitMap.size()>0){
        	/**
        	 * ��ѯ�Ļ���ڼ�֮ǰ�д���δ���˵�����
        	 */
        	Iterator nobindMap = noBindInitMap.entrySet().iterator();
        	//����map�õ����������
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//�õ��������ݵ�map
				if(bindMap != null && bindMap.size()>0){
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					String[] str1 = new String[]{"PeriodIniBase","PeriodIni"};
					String[] str2 = new String[]{"PeriodDCBalaBase","PeriodDCBala"};
					
					//���бұ����ʽ
					if("all".equals(currencyValue)){
						String[] currS1 = new String[currList.size()+1];
						String[] currS2 = new String[currList.size()+1];
						int counts = 0;
						for(int k = 1;k<str1.length;k++){
							for(int l=0;l<currList.size();l++){
								String[] currStr = (String[])currList.get(l);
								if("true".equals(currStr[2])){
									//��λ��
									currS1[counts] = str1[k]+"_";
									currS2[counts] = str2[k]+"_";
								}else{
									currS1[counts] = str1[k]+"_"+currStr[0];
									currS2[counts] = str2[k]+"_"+currStr[0];
								}
								counts ++;
							}
						}
						int curCount = currList.size();
						currS1[curCount+1] = "PeriodIniBase";
						currS2[curCount+1] = "PeriodDCBalaBase";
						str1 = currS1;
						str2 = currS2;
					}
					
					for(int j=1;j<=bindClassCode.length()/5;j++){
						HashMap periodMap = new HashMap();
						if(periodList!=null && periodList.size()>0){
							periodMap = (HashMap)periodList.get(0);
						}
						HashMap oldMap = (HashMap)accBalanceMap.get(periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod")+"_"+bindClassCode.substring(0,j*5));
						if(oldMap == null){
							oldMap = new HashMap();
							oldMap.put("Nyear", periodMap.get("AccYear"));
							oldMap.put("Period", periodMap.get("AccPeriod"));
						}
						for(int k=0;k<str1.length;k++){
							String moneys1 = "0";
							Object o1 = null;
							if(oldMap!=null && oldMap.size()>0){
								o1 = oldMap.get(str1[k]);
							}
							if(o1!=null && !"".equals(o1)){
								moneys1 = o1.toString();
							}
							Object o3 = bindMap.get(str2[k]);
							if(o3!=null && !"".equals(o3)){
								moneys1 = new BigDecimal(moneys1).add(new BigDecimal(o3.toString())).toString();
							}
							oldMap.put(str1[k],moneys1);
						}
						accBalanceMap.put(periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod")+"_"+bindClassCode.substring(0,j*5),oldMap);
					}
				}
        	}
        }
		
		/**
		 * ��������
		 */
		List allDataList = new ArrayList();
		for(int i=0;i<accTypeInfoList.size();i++){
	      	//ÿһ����ƿ�Ŀ���б���
	      	HashMap accTypemap = (HashMap)accTypeInfoList.get(i);
	      	String classCode = accTypemap.get("classCode").toString();
	      	List list = new ArrayList();
	      	
	      	HashMap initMap = new HashMap();
	      	//�ڳ�
	      	for(int j=0;j<periodList.size();j++){
	      		
	      		//ѭ��Ҫ��ѯ�Ļ���ڼ�
	      		HashMap accPeriodmap = (HashMap)periodList.get(j);
	      		HashMap accType_Period = (HashMap)accBalanceMap.get(accPeriodmap.get("AccYear")+"_"+accPeriodmap.get("AccPeriod")+"_"+accTypemap.get("classCode"));
	      		if(accType_Period == null){
	      			accType_Period = new HashMap();
	      			accType_Period.put("Nyear", accPeriodmap.get("AccYear"));
	      			accType_Period.put("Period", accPeriodmap.get("AccPeriod"));
	      		}
	      		
	      		//���бұ����ʽ
	      		String[] str1 = null;
				if("all".equals(currencyValue)){
					str1 = new String[currList.size()+1];
					for(int l = 0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//��λ��
							str1[l] = "_";
						}else{
							str1[l] = "_"+currStr[0];
						}
					}
					str1[currList.size()] = "Base";
				}else{
					str1 = new String[]{"","Base"};
				}
	      		
	      		//��ȡ�ڳ�
				if(j == 0){
					//��һ����Ŀ�������
					for(String baseStr : str1){
						String initMoney = "";
						Object o = accType_Period.get("PeriodIni"+baseStr);
						if(o!=null && !"".equals(o)){
							//�ڳ����
							initMoney = String.valueOf(o);
						}
						if("Base".equals(baseStr)){
							if(initMoney != null && !"".equals(initMoney)){
								Double initMoneys = Double.parseDouble(initMoney);
								if(initMoneys>0){
									initMap.put("isIniflag","��");
								}else if(initMoneys<0){
									initMap.put("isIniflag","��");
								}else{
									initMap.put("isIniflag","ƽ");
								}
							}else{
								initMap.put("isIniflag","ƽ");
							}
						}
						initMap.put("PeriodIni"+baseStr, initMoney);
					}
				}
				
				/* �޷������ʾ */
				if(takeBrowNo!=null && !"".equals(takeBrowNo)){
					if(accType_Period!=null && accType_Period.size()>0){
						String debitsum = accType_Period.get("PeriodDebitSumBase")==null?"":accType_Period.get("PeriodDebitSumBase").toString();
						String creditsum = accType_Period.get("PeriodCreditSumBase")==null?"":accType_Period.get("PeriodCreditSumBase").toString();
						if("".equals(debitsum) && "".equals(creditsum)){
							continue;
						}
					}else{
						continue;
					}
				}
				/* ���Ϊ�����޷������ʾ */
				if(balanceAndTakeBrowNo!=null && !"".equals(balanceAndTakeBrowNo)){
					if(accType_Period!=null && accType_Period.size()>0){
						String debitsum = accType_Period.get("PeriodDebitSumBase")==null?"":accType_Period.get("PeriodDebitSumBase").toString();
						String creditsum = accType_Period.get("PeriodCreditSumBase")==null?"":accType_Period.get("PeriodCreditSumBase").toString();
						String dcBalasum = accType_Period.get("PeriodDCBalaBase")==null?"":accType_Period.get("PeriodDCBalaBase").toString();
						String iniBaseAmount = accTypemap.get("IniBaseAmount")==null?"":accTypemap.get("IniBaseAmount").toString();
						if("".equals(debitsum) && "".equals(creditsum) && "".equals(dcBalasum) && "".equals(iniBaseAmount)){
							continue;
						}
					}else{
						continue;
					}
				}
				
				for(String baseStr : str1){
		      		//��ѯ��һ�ڼ������
		      		Object obj = accPeriodmap.get("AccPeriod");
		      		String money = "0";
	      			Integer period = Integer.valueOf(obj.toString());
	      			HashMap oldType_Period = null;
	      			for(int k=period-1;k>0;k--){
	      				oldType_Period = (HashMap)accBalanceMap.get(accPeriodmap.get("AccYear")+"_"+String.valueOf(k)+"_"+accTypemap.get("classCode"));
	      				if(oldType_Period!=null){
	      					break;
	      				}
	      			}
	      			
	      			//������� = ��һ������+���ڽ��
	  				String[] strs = new String[]{"PeriodDCBala"+baseStr};
	  				for(String str : strs){
	  					Object o = accType_Period.get(str);
	  					money = "0";
	  					if(o!=null && !"".equals(o)){
	  						money = o.toString();
	  					}
	  					if(oldType_Period!=null){
	  						o = oldType_Period.get(str);
	  					}else{
	  						//�ڳ����
	  						o = initMap.get("PeriodIni"+baseStr);
	  					}
	  					if(o!=null && !"".equals(o)){
	  						money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
	  					}
	  					if("PeriodDCBalaBase".equals(str)){
	  						if(Double.valueOf(money)>0){
	  							accType_Period.put("PeriodDCBalaBaseisflag", "��");
	  						}else if(Double.valueOf(money)<0){
	  							accType_Period.put("PeriodDCBalaBaseisflag", "��");
	  						}else{
	  							accType_Period.put("PeriodDCBalaBaseisflag", "ƽ");
	  						}
	  					}
	  					accType_Period.put(str, money);
	  				}
	      			
	      			//���㱾���ۼ�����һ�ڵı����ۼ�+���ڵĽ������
	      			String periodmoney = "0";
		      		String debitsum = accType_Period.get("PeriodDebitSum"+baseStr)==null?"":accType_Period.get("PeriodDebitSum"+baseStr).toString();			//���ڽ跽���
					String creditsum = accType_Period.get("PeriodCreditSum"+baseStr)==null?"":accType_Period.get("PeriodCreditSum"+baseStr).toString();			//���ڴ������
					if(!("".equals(debitsum) && "".equals(creditsum))){
						if(debitsum != null && !"".equals(debitsum)){
							periodmoney = new BigDecimal(periodmoney).add(new BigDecimal(debitsum)).toString();
						}
		        		if(creditsum != null && !"".equals(creditsum)){
		        			periodmoney = new BigDecimal(periodmoney).subtract(new BigDecimal(creditsum)).toString();
						}
		      		}
					
					money = "0";
					strs = new String[]{"CurrYIniAmount","CurrYIni"+baseStr};
	  				for(String str : strs){
	  					Object o = periodmoney;
	  					if(o!=null && !"".equals(o)){
	  						money = o.toString();
	  					}
	  					if(oldType_Period!=null){
	  						o = oldType_Period.get("CurrYIni"+baseStr);
	  					}else{
	  						o = initMap.get("PeriodIni"+baseStr);
	  					}
	  					if(o!=null && !"".equals(o)){
	  						money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
	  					}
	  					if("CurrYIniAmount".equals(str)){
	  						if(Double.valueOf(money)>0){
	  							accType_Period.put("CurrYIniAmountisflag", "��");
	  						}else if(Double.valueOf(money)<0){
	  							accType_Period.put("CurrYIniAmountisflag", "��");
	  						}else{
	  							accType_Period.put("CurrYIniAmountisflag", "ƽ");
	  						}
	  					}
	  					accType_Period.put(str, money);
	  				}
	  				if(oldType_Period == null){
	  					oldType_Period = new HashMap();
	  				}
	  				
	  				//������Ľ�������
	  				strs = new String[]{"CurrYIniDebitSum"+baseStr,"CurrYIniCreditSum"+baseStr};
	  				String[] strs1 = new String[]{"PeriodDebitSum"+baseStr,"PeriodCreditSum"+baseStr};
	  				for(int s=0;s<strs.length;s++){
	  					money = "0";
	  					Object o = oldType_Period.get(strs[s]);
	  					if(o!=null && !"".equals(o)){
	  						money = o.toString();
	  					}
	  					o = accType_Period.get(strs1[s]);
	  					if(o!=null && !"".equals(o)){
	  						money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
	  					}
	  					accType_Period.put(strs[s], money);
	  				}
				}
  				list.add(accType_Period);
	      	}
	      	//�ڳ�ֵ����
	      	Iterator iterInit = initMap.entrySet().iterator();
	      	while(iterInit.hasNext()){
	      		Entry entry = (Entry)iterInit.next();
	      		if(!"isIniflag".equals(entry.getKey())){
	      			accTypemap.put(entry.getKey(), dealDataDouble(String.valueOf(entry.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
	      		}else{
	      			accTypemap.put(entry.getKey(),entry.getValue());
	      		}
	      	}
	      	
		  	/* ���ݴ��� */
		  	for(int j = 0;j<list.size();j++){
		  		HashMap valueMap = (HashMap)list.get(j);
		  		String strs[] = new String[]{"PeriodCreditSumBase","PeriodDebitSumBase","PeriodBalaBase","CurrYIniBalaBase",
		  				"PeriodDCBalaBase","CurrYIniDebitSumBase","CurrYIniCreditSumBase","PeriodIniBase","CurrYIniAmount",
		  				"PeriodBala","CurrYIni","PeriodDCBala","PeriodCreditSum","PeriodDebitSum","CurrYIniDebitSum",
		  				"CurrYIniCreditSum","CurrYIniBala","PeriodIni"};
		  		if("all".equals(currencyValue)){
					String[] currS = new String[(currList.size()+1)*9];
					int counts = 0;
					for(int k = 9;k<strs.length;k++){
						for(int l=0;l<currList.size();l++){
							String[] currStr = (String[])currList.get(l);
							if("true".equals(currStr[2])){
								//��λ��
								currS[counts] = strs[k]+"_";
							}else{
								currS[counts] = strs[k]+"_"+currStr[0];
							}
							counts ++;
						}
					}
					int curCount = currList.size()*9;
					for(int k=0;k<strs.length-9;k++){
						currS[curCount+k] = strs[k];
					}
					strs = currS;
				}
		  		
	    		for(int k = 0;k<strs.length;k++){
	    			if(valueMap.get(strs[k]) != null){
	    				String absValue = "";
	    				if(strs[k].indexOf("PeriodDCBala")!=-1 || strs[k].indexOf("CurrYIni")!=-1){
	    					absValue = "abs";
	    				}
	    				valueMap.put(strs[k],dealDataDouble(String.valueOf(valueMap.get(strs[k])), BaseEnv.systemSet.get("DigitsAmount").getSetting(), absValue));
	    			}else{
	    				valueMap.put(strs[k],"");
	    			}
	    		}
		  	}
		  	accTypemap.put("periodList", list);
	      	if(showIsItem == null || "".equals(showIsItem)){
				//����ʾ������Ŀ��ϸ
	      		String isCalculate = accTypemap.get("isCalculate").toString();
	      		if("1".equals(isCalculate)){
	      			continue;
	      		}
			}
	      	boolean falg = false;
	      	if(levelStart != null && !"".equals(levelStart)){
	      		//��Ŀ�ȼ���ʼ
	      		if(classCode.length()/5-1>=Integer.parseInt(levelStart)){
	      			falg = true;
	      		}else{
	      			falg = false;
	      		}
	      	}
	      	if(levelEnd != null && !"".equals(levelEnd)){
	      		//��Ŀ�ȼ�����
	      		if(classCode.length()/5-1<=Integer.parseInt(levelEnd)){
	      			falg = true;
	      		}else{
	      			falg = false;
	      		}
	      	}
	      	if((levelStart == null || "".equals(levelStart)) && (levelEnd != null || "".equals(levelEnd))){
	      		falg = true;
	      	}
	      	if(falg){
	      		allDataList.add(accTypemap);
	      	}
		}
		Object[] obj = new Object[]{allDataList,currencyValue};
		rst.setRetVal(obj);
        return rst;
	}

	/**
	 * ���ַ������͵Ľ���ת��ΪDouble����
	 * @param str
	 * @return
	 */
	public Double toDouble(String str){
		Double value = 0.0;
		if(str == null || "".equals(str)){
			return value;
		}else{
			return Double.valueOf(str);
		}
		
	}
	
	
	/**
	 * ��������Excel����
	 * @param outStream					//������
	 * @param tableDisplay				//execlѡ�������
	 * @param titleName					//ͷ���ı��⣨�磺�ܷ����ˣ���ϸ�����˵ȣ�
	 * @param strTitle					//�е�ͷ��
	 * @param dataList					//���������
	 * @return
	 */
	public Result exportBanlance(OutputStream outStream, String tableDisplay, String titleName,List strTitle, List dataList){
		Result result = new Result();
		try {
			WritableCellFormat wf = new WritableCellFormat();
			wf.setBorder(Border.ALL, BorderLineStyle.THIN);
			wf.setAlignment(Alignment.CENTRE);
			WritableWorkbook wbook = Workbook.createWorkbook(outStream);
			WritableSheet ws = wbook.createSheet(tableDisplay, 0);
			
			int c = 0;
			int r = 0;
			int maxCount = 0;
			//�еı�ͷ
			r++;
			for(int i=0;i<strTitle.size();i++){
				HashMap setMap = (HashMap)strTitle.get(i);
				
				//���¶�������ʾ
				Object o = setMap.get("nextLine");
				if(o != null && !"".equals(o)){
					r += Integer.parseInt(o.toString());
				}
				
				//�����ڶ�������ʾ
				o = setMap.get("nextX");
				if(o != null && !"".equals(o)){
					c = Integer.parseInt(o.toString());
				}
				boolean lastXFlag = false;
				int lastX = c;
				//���õ�Ԫ��������
				o = setMap.get("lastX");
				if(o != null && !"".equals(o)){
					lastX += Integer.parseInt(o.toString());
					lastXFlag = true;
				}
				int lastY = r;
				//���õ�Ԫ��������
				o = setMap.get("lastY");
				if(o != null && !"".equals(o)){
					lastY += Integer.parseInt(o.toString());
				}
				
				Label cell = new Label(c, r, String.valueOf(setMap.get("name")==null?"":setMap.get("name")), wf);
				ws.mergeCells(c, r, lastX, lastY);				//�ϲ���Ԫ������
				ws.addCell(cell);
				if(lastXFlag){
					c = lastX;
				}
				maxCount = c;
				c++;
			}
			
			//��ʾ����ı���
			Label cell = new Label(0, 0, tableDisplay+"   "+titleName, wf);
			ws.mergeCells(0, 0, maxCount, 0);
			ws.addCell(cell);
			
			//���ݵ�����
			r++;
			c = 0;
			for(int i=0;i<dataList.size();i++){
				List list = (ArrayList)dataList.get(i);
				for(int j =0;j<list.size();j++){
					Object o = list.get(j);
					String values = "";
					if(o != null){
						values = String.valueOf(o);
					}
					cell = new Label(c, r, values, wf);
					ws.addCell(cell);
					c++;
				}
				r++;
				c = 0;
			}
			
			wbook.write();
			wbook.close();
		} catch (Exception ex) {
			result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("FinanceReportMgt exportBanlance :", ex);
		}
		return result;
	}
	
	/**
	 * ��ѯ��ƿ�Ŀ
	 * @param showBanAccTypeInfo			���˽��õĻ�ƿ�Ŀ
	 * @param showIsItem					ֻ��ʾ��ϸ��Ŀ
	 * @param showItemDetail				��ʾ������Ŀ��ϸ
	 * @param gradeShow						�ּ���ʾ
	 * @param itemSort						��Ŀ���
	 * @param itemLevel						��Ŀ����
	 * @param itemCodeStart					��Ŀ���뿪ʼ
	 * @param itemCodeEnd					��Ŀ�������
	 * @param keyWord						�ؼ�������
	 * @return
	 */
	public Result queryAccTypeInfoAll(final String showBanAccTypeInfo,final String showIsItem,final String showItemDetail,
			final String gradeShow,final String itemSort,final String itemLevel,final String itemCodeStart,
			final String itemCodeEnd,final String keyWord,final MOperation mop,final LoginBean loginBean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							ResultSet rs = null;
							StringBuffer sql = new StringBuffer("select classCode from tblAccTypeInfo where statusId!=0");
							Statement state = conn.createStatement();
							rs = state.executeQuery(sql.toString());
							String conSql = "";
							while(rs.next()){
								String classCode = rs.getString("classCode");
								conSql = conSql+" classCode not like '"+classCode+"%' AND ";
							}
							
							boolean falg = false;
							boolean itemdetail = false;
							if(gradeShow!=null && !"".equals(gradeShow)){
								falg = true;
							}
							if(showIsItem!=null && !"".equals(showIsItem)){
								itemdetail = true;
							}
							sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,l.zh_CN as AccName,PyCode,tblAccTypeInfo.classCode,tblAccTypeInfo.isCatalog,");
							sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.classCode as asClassCode, ");
							sql.append("IsDept,IsPersonal,IsClient,IsProvider,IsStock,CASE WHEN (tblAccTypeInfo.IsDept=1 OR tblAccTypeInfo.IsPersonal=1 OR tblAccTypeInfo.IsClient=1 OR tblAccTypeInfo.IsStock=1 OR tblAccTypeInfo.IsProvider=1) THEN '1' ELSE '2' END AS isItem");
							String strsql = " FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccName WHERE 1=1 ";
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//���˽��õĻ�ƿ�Ŀ
								strsql += " AND statusId=0 ";
								if(conSql.length()>0){
									//���ڼ�¼
									strsql += " AND "+conSql+" 1=1 ";
								}
							}
							//����ʾ������Ŀ��ϸ
							
//							strsql += " AND (((DepartmentCode IS NULL OR DepartmentCode='') AND (ClientCode IS NULL OR ClientCode='')";
//							strsql += " AND (EmployeeID IS NULL OR EmployeeID='') AND (SuplierCode IS NULL OR SuplierCode='')";
//							strsql += " AND (StockCode IS NULL OR StockCode=''))";
							strsql += " and ((isnull(isCalculate,0) != 1)";
							
							String isItem = "";
							String isSql = "";
							String isCondition = "";
							if(showItemDetail != null && !"".equals(showItemDetail)){
								if((!"all".equals(itemSort))){
									//��������
									isCondition += " OR ("+itemSort+"!='' ";
									if("DepartmentCode".equals(itemSort)){
										isItem = "IsDept";
										isSql = " classCode,deptCode as AccNumber,DeptFullName as AccName  from tblDepartment) as dept on dept.classCode=groupids."+itemSort;
									}else if("EmployeeID".equals(itemSort)){
										isItem = "IsPersonal";
										isSql = " id as classCode,id,EmpNumber as AccNumber,EmpFullName as AccName from tblEmployee) as employee on employee.id=groupids."+itemSort;
									}else if("ClientCode".equals(itemSort)){
										isItem = "IsClient";
										isSql = " classCode,ComNumber as AccNumber,ComFullName as AccName FROM tblCompany) as company on company.classCode=groupids."+itemSort;
									}else if("SuplierCode".equals(itemSort)){
										isItem = "IsProvider";
										isSql = " classCode,ComNumber as AccNumber,ComFullName as AccName FROM tblCompany) as company on company.classCode=groupids."+itemSort;
									}else if("StockCode".equals(itemSort)){
										isItem = "IsStock";
										isSql = " classCode,StockNumber as AccNumber,StockFullName as AccName FROM tblStock) as stocks on stocks.classCode=groupids."+itemSort;
									}
									isCondition += ")";
								}else{
									isCondition += " OR departmentCode!='' OR clientCode!='' OR employeeId!='' OR supliercode!='' OR stockCode!='' ";
								}
							}
							sql.append(strsql);
							if(itemdetail && !falg){
								sql.append(isCondition);
							}
							String strsql1 = ")";
							/* ֻ��ʾ��ϸ��Ŀ */
							if(showIsItem != null && !"".equals(showIsItem)){
								//���˴��ں�����Ŀ�Ŀ
								strsql1 += " AND (isCatalog=0 or len(classCode)=5) AND ISNULL(isCalculateParent,0)!=1";
							}
							sql.append(strsql1);
							/* �ؼ������� */
							if(keyWord != null && !"".equals(keyWord)){
								sql.append(" AND (tblAccTypeInfo.AccNumber LIKE '%"+keyWord+"%' OR l.zh_CN LIKE '%"+keyWord+"%' OR PyCode LIKE '%"+keyWord+"%') ");
							}
							sql.append(scopeSql(mop, loginBean));
							sql.append(" ORDER BY tblAccTypeInfo.AccNumber,l.zh_CN");
							Statement st = conn.createStatement();
							rs = st.executeQuery(sql.toString());
							List<HashMap> list = new ArrayList<HashMap>();
							while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	
                            	if(falg){
    								//ֻ��ʾ��ϸ��Ŀ+������+�ּ���ʾ
                            		if((Integer.valueOf(map.get(isItem).toString())==1 && !itemdetail) || (itemdetail && (map.get("classCode").toString()).length()==5)){
                            			//������Ŀ
                            			String sqlshow = "select * from (SELECT "+itemSort+",1 as isCatalog,1 as isItem,'"+map.get("classCode")+"00000' as asClassCode "+strsql+isCondition+strsql1+" AND "+itemSort+" IS NOT NULL AND "+itemSort+"!='' ";
                            			if(!itemdetail){
                            				sqlshow += " AND tblAccTypeInfo.classCode like '"+map.get("classCode")+"_____' ";
                            			}else{
                            				sqlshow += " AND tblAccTypeInfo.classCode like '"+(map.get("classCode").toString()).substring(0,5)+"%' ";
                            			}
                            			sqlshow += "GROUP BY tblAccTypeInfo."+itemSort+") as groupids ";
                            			sqlshow += " left join (select "+isSql;
        								ResultSet resultSet = state.executeQuery(sqlshow);
        								while (resultSet.next()) {
        									HashMap Ismap=new HashMap();
        	                            	for(int i=1;i<=resultSet.getMetaData().getColumnCount();i++){
        	                            		Object obj=resultSet.getObject(i);
        	                            		if(obj==null){
        	                            			if(resultSet.getMetaData().getColumnType(i)==Types.NUMERIC||resultSet.getMetaData().getColumnType(i)==Types.INTEGER){
        	                            				Ismap.put(resultSet.getMetaData().getColumnName(i), 0);
        	                            			}else{
        	                            				Ismap.put(resultSet.getMetaData().getColumnName(i), "");
        	                            			}
        	                            		}else{
        	                            			Ismap.put(resultSet.getMetaData().getColumnName(i), obj);
        	                            		}
        	                            	}
        	                            	list.add(Ismap);
        								}
                            		}
    							}
                            	list.add(map);
                            }
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryAccTypeInfoAll:",ex) ;
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
	 * ��ϸ�����˱�������
	 * @param conMap   			��������
	 * @param classCode			classCode
	 * @param classType			���������
	 * @return
	 */
	public Result queryAccDetData(final HashMap<String,String> conMap,final String classCode){
		
		/* ���ֽ��д��� */
		String currencyNames = conMap.get("currencyName"); 									//�ұ�('isBase'=��λ�ң�''=�ۺϱ�λ�ң�'all'=���бұ����ʽ��'currency'=���бұ�'��ҵ�id'=���)
		Result rs = queryIsBase(currencyNames);
		currencyNames = rs.retVal.toString();
		
		//�������
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyNames;
		final String yearStart = conMap.get("periodYearStart");							//��ʼ�ڼ���
		final String yearEnd = conMap.get("periodYearEnd");								//�����ڼ���
		final String periodstart = conMap.get("periodStart");							//��ʼ�ڼ�
		final String periodend = conMap.get("periodEnd");								//�����ڼ�
		final String binderNo = conMap.get("binderNo");									
		final String dateType = conMap.get("dateType");
		final String dateStart = conMap.get("dateStart");
		final String dateEnd = conMap.get("dateEnd");
		final String takeBrowNo = conMap.get("takeBrowNo");
		final String showMessage = conMap.get("showMessage");
		final String balanceAndTakeBrowNo = conMap.get("balanceAndTakeBrowNo");
		final String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");				//��ʾ���ÿ�Ŀ
		final String showAccType = conMap.get("showAccType");
		final String orderby = conMap.get("orderby");
		final String momentType = conMap.get("momentType");
		final String currencyName = conMap.get("currencyName");
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
	    	public int exec(Session session) {
	            session.doWork(new Work() {
	                public void execute(Connection conn) throws SQLException {
	                	try {
	                    	Statement st;
							ResultSet rset = null;
							String periodYearStart = yearStart;
							String periodYearEnd= yearEnd;
							String periodStart = periodstart;
							String periodEnd = periodend;
							String conditions = "";
	                    	if(!"".equals(currencyValue) && !"all".equals(currencyValue) && !"currency".equals(currencyValue)){
	            				if("isBase".equals(currencyValue)){
	            					/* �Ǳ�λ�� */
	            					conditions += " AND (tblAccDetail.Currency='' OR tblAccDetail.Currency IS NULL OR tblAccDetail.Currency='"+currencyName+"') ";
	            				}else{
	            					conditions += " AND tblAccDetail.Currency='"+currencyName+"' ";
	            				}
	            			}
		                    String sim1 = "";
		                    String sim2 = "";
	                        /**
	                         * ��ѯ�ڳ�
	                         */
		                    String startDate = "";
		                    if(Integer.valueOf(periodStart)<10){
		                    	startDate = periodYearStart+"-0"+periodStart+"-01";
							}else{
								startDate = periodYearStart+"-"+periodStart+"-01";
							}
		                    //���ڳ�����������ָ������ǰ��ƾ֤��ϸ��Ľ�����
	                        StringBuffer sql = new StringBuffer("SELECT '"+startDate+"' AS periodBegin,");
	                        sql.append("0 AS sumDebitAmountBase,0 AS sumLendAmountBase,");
							sql.append("0 AS sumDebitAmount,0 AS sumLendAmount,");
							sql.append("isnull(SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount),0) AS sumBalanceAmountBase,");
							sql.append("isnull(SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount),0) AS sumBalanceAmount ");
							sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo");
							sql.append(" ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccTypeInfo.classCode like '"+classCode+"%'");
							
							//�鱾���ۼƽ��������ָ�����ڵ�������н��
							StringBuffer sqlsum = new StringBuffer("SELECT '"+startDate+"' AS periodBegin,");
							sqlsum.append("isnull(SUM(tblAccDetail.DebitAmount),0) AS sumDebitAmountBase,isnull(SUM(tblAccDetail.LendAmount),0) AS sumLendAmountBase,");
							sqlsum.append("isnull(SUM(tblAccDetail.DebitCurrencyAmount),0) AS sumDebitAmount,isnull(SUM(tblAccDetail.LendCurrencyAmount),0) AS sumLendAmount,");
							sqlsum.append("0 AS sumBalanceAmountBase,");
							sqlsum.append("0 AS sumBalanceAmount ");
							sqlsum.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo");
							sqlsum.append(" ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccTypeInfo.classCode like '"+classCode+"%'");
							
							//�ڳ����
							SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
							Calendar calendar = Calendar.getInstance();
							sim2 = simple.format(calendar.getTime());
							if("halfyear".equals(momentType)){
								calendar.set(calendar.MONTH, (calendar.get(calendar.MONTH))-5);
							}else if("threemonth".equals(momentType)){
								calendar.set(calendar.MONTH, (calendar.get(calendar.MONTH))-2);
							}else if("tomonth".equals(momentType)){
								calendar.set(calendar.MONTH, (calendar.get(calendar.MONTH))-1);
								calendar.set(calendar.DATE,  1 );
								SimpleDateFormat simple1 = new SimpleDateFormat("yyyy-MM");
								sim2 = simple1.format(calendar.getTime())+"-31";
							}else if("month".equals(momentType)){
								calendar.set(calendar.DATE, calendar.getActualMinimum(calendar.DAY_OF_MONTH));
							}else if("week".equals(momentType)){
								calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
								sim2 = simple.format(calendar.getTime().getTime() + (6 * 24 * 60 * 60 * 1000));
							}else if("threeday".equals(momentType)){
								calendar.set(calendar.DATE, (calendar.get(calendar.DATE))-2);
							}else if("day".equals(momentType)){
								calendar.set(calendar.DATE, (calendar.get(calendar.DATE))-0);
							}else if("yesterday".equals(momentType)){
								calendar.set(calendar.DATE, (calendar.get(calendar.DATE))-1);
								sim2 = simple.format(calendar.getTime());
							}
							
							sim1 = simple.format(calendar.getTime());
							if("year".equals(momentType)){
								sim1 = (calendar.get(calendar.YEAR))+"-01-01";
								sim2 = (calendar.get(calendar.YEAR))+"-12-31";
							}
							String sqlCondition = "";
							String startTime = "";
							String endTime = "";
							String periodSql = "";																//����ڼ��������
							String strPeriod = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,accPeriod.AccYear)+'-'+CONVERT(VARCHAR,accPeriod.AccPeriod)+'-01',120),120)";
							if(dateType!=null && "1".equals(dateType)){
								//ѡ�����ڼ�
								if(Integer.valueOf(periodStart)<10){
									startTime = periodYearStart+"-0"+periodStart;
								}else{
									startTime = periodYearStart+"-"+periodStart;
								}
								if(Integer.valueOf(periodEnd)<10){
									endTime = periodYearEnd+"-0"+periodEnd;
								}else{
									endTime = periodYearEnd+"-"+periodEnd;
								}
								sql.append(" AND (tblAccMain.CredYear<"+periodYearStart+" OR ( tblAccMain.CredYear="+periodYearStart+" AND tblAccMain.Period<"+periodStart+"))");
								sqlsum.append(" AND ( tblAccMain.CredYear="+periodYearStart+" AND tblAccMain.Period<"+periodStart+")");
								
								String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccPeriod.AccYear)+'-'+CONVERT(VARCHAR,tblAccPeriod.AccPeriod)+'-01',120),120)";
								sqlCondition += " AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'";
								periodSql += " AND "+strPeriod+">='"+startTime+"' AND "+strPeriod+"<='"+endTime+"'";
							}else if(dateType!=null && "2".equals(dateType)){
								//ѡ������
								sql.append(" AND tblAccDetail.AccDate<'"+dateStart+"'");
								sqlsum.append(" AND tblAccDetail.AccDate<'"+dateStart+"' AND tblAccMain.CredYear="+dateStart.substring(0,4)+" ");
								sqlCondition += " AND tblAccDetail.AccDate>='"+dateStart+"' AND tblAccDetail.AccDate<='"+dateEnd+"'";
								periodSql += " AND "+strPeriod+">='"+dateStart.substring(0,dateStart.lastIndexOf("-"))+"' AND "+strPeriod+"<='"+dateEnd.substring(0,dateEnd.lastIndexOf("-"))+"'";
							}else if(dateType!=null && "3".equals(dateType)){
								//ѡ��׶�
								sql.append(" AND tblAccDetail.AccDate<'"+sim1+"'");
								sqlsum.append(" AND tblAccDetail.AccDate<'"+sim1+"' AND tblAccMain.CredYear="+sim1.substring(0,4)+" ");
								sqlCondition += " AND tblAccDetail.AccDate>='"+sim1+"' AND tblAccDetail.AccDate<='"+sim2+"'";
								periodSql += " AND "+strPeriod+">='"+sim1.substring(0,sim1.lastIndexOf("-"))+"' AND "+strPeriod+"<='"+sim2.substring(0,sim2.lastIndexOf("-"))+"'";
							}
							
							if(binderNo == null || "".equals(binderNo)){
	            				//������δ����ƾ֤
								sql.append(" AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ");
								sqlsum.append(" AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ");
	            			}
							System.out.println("��ѯ�ڳ���"+sql);
							st = conn.createStatement();
	                        rset=st.executeQuery(sql.toString());
							HashMap initMap = new HashMap();
							if (rset.next()) {
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				initMap.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				initMap.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
	                    					String strvalue = String.valueOf(obj);
	                    					if (strvalue.indexOf(".")>0){
	                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
	                    					}
	                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
	                    						strvalue = "";
	                    					}
	                    					if("sumBalanceAmountBase".equals(rset.getMetaData().getColumnName(i))){
	                        					if(!"".equals(strvalue)){
	                        						if(Double.valueOf(strvalue)>0){
	                        							initMap.put("isIniflag", "��");
	                        						}else if(Double.valueOf(strvalue)<0){
	                        							initMap.put("isIniflag", "��");
	                        						}else{
	                        							initMap.put("isIniflag", "ƽ");
	                        						}
	                        					}else{
	                        						initMap.put("isIniflag", "ƽ");
	                        					}
	                        				}
	                    					initMap.put(rset.getMetaData().getColumnName(i), strvalue);
	                        			}else{
	                        				initMap.put(rset.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
							}
							//���ڱ����ۼƣ�ֻ���ۼƵ���Ľ�����ݣ��������ڳ������ۼƵ���������ݵĽ���ܶ���������Ҫ����
							System.out.println("��ѯ�����ۼƽ�"+sqlsum);
							st = conn.createStatement();
	                        rset=st.executeQuery(sqlsum.toString());
							if (rset.next()) {
								for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
									String mname=rset.getMetaData().getColumnName(i);
									if(mname.equals("sumDebitAmountBase")||mname.equals("sumLendAmountBase")||mname.equals("sumDebitAmount")||mname.equals("sumLendAmount")){
		                        		Object obj=rset.getObject(i);
		                        		if(obj==null){
		                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
		                        				initMap.put(rset.getMetaData().getColumnName(i), 0);
		                        			}else{
		                        				initMap.put(rset.getMetaData().getColumnName(i), "");
		                        			}
		                        		}else{
		                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
		                    					String strvalue = String.valueOf(obj);
		                    					if (strvalue.indexOf(".")>0){
		                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
		                    					}
		                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
		                    						strvalue = "";
		                    					}
		                    					
		                    					initMap.put(rset.getMetaData().getColumnName(i), strvalue);
		                        			}
		                        		}
		                        	}
								}
							}
							
							
							/* ��ѯ����ǰ���ڳ����  ���ϴ�ƾ֤��ϸ������������Ϊ�ڳ� */
							sql = new StringBuffer("SELECT '"+startDate+"' AS periodBegin,'0' AS sumDebitAmountBase,'0' AS sumLendAmountBase,");
							sql.append("(case acctype.JdFlag when 2 then 0-CurrYIniBala else CurrYIniBala end) AS sumBalanceAmount,");
							sql.append("(case acctype.JdFlag when 2 then 0-CurrYIniBalaBase else CurrYIniBalaBase end) AS sumBalanceAmountBase,");
							sql.append("acctype.JdFlag,isnull(tblAccBalance.CurType,'') as Currency ");
							sql.append("FROM tblAccBalance left join tblAccTypeInfo acctype ");
							sql.append("ON tblAccBalance.SubCode=acctype.AccNumber WHERE Nyear=-1 AND Period=-1");
							sql.append(" AND SubCode = (select AccNumber from tblAccTypeInfo where classCode='"+classCode+"')");
							sql.append("ORDER BY SubCode");
							System.out.println("�����ڳ�sql:"+sql.toString());
							rset = st.executeQuery(sql.toString());
							if (rset.next()) {
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				map.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
	                    					String strvalue = String.valueOf(obj);
	                    					if (strvalue.indexOf(".")>0){
	                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
	                    					}
	                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
	                    						strvalue = "";
	                    					}
	                    					map.put(rset.getMetaData().getColumnName(i), strvalue);
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
	                        	//��ѡ��ıұ��ǡ������бұ����ʽ
	                        	if("all".equals(currencyValue)){
	                        		String[] curs = new String[]{"sumBalanceAmount"};
	                        		if("".equals(map.get("Currency"))){
	                        			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
	                        			for(String s : curs){
	                        				map.put(s+"_", map.get(s+"Base"));
	                        			}
	                        		}else{
	                        			//�������
	                        			for(String s : curs){
	                        				map.put(s+"_"+map.get("Currency"), map.get(s));
	                        			}
	                        		}
	                        	}
	                        	/* �����ڳ����ݣ������� */
	                        	String[] strMoney = new String[]{"sumDebitAmountBase","sumLendAmountBase","sumBalanceAmountBase","sumBalanceAmount"};
	                        	if(initMap.size()>0){
	                        		Iterator iter = map.entrySet().iterator();
	                        		while(iter.hasNext()){
	                        			Entry entry = (Entry)iter.next();
	                        			String key = entry.getKey().toString();
	                        			if(key.indexOf("sumBalanceAmount") != -1){
	                        				String money = "0";
											Object o = initMap.get(key); 
											if(o != null && !"".equals(o)){
												money = o.toString();
											}
											o = map.get(key);
											if(o != null && !"".equals(o)){
												//�跽
												money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
											}
											if("sumBalanceAmountBase".equals(key)){
		                						if(Double.valueOf(money)>0){
		                							initMap.put("isIniflag", "��");
		                						}else if(Double.valueOf(money)<0){
		                							initMap.put("isIniflag", "��");
		                						}else{
		                							initMap.put("isIniflag", "ƽ");
		                						}
		                    				}
											initMap.put(key, money);
	                        			}
	                        		}
	                        	}
							}
							
							/**
							 * ��ѯ���ںϼ�
							 */
							sql = new StringBuffer("SELECT accPeriod.AccYear,accPeriod.AccPeriod,accPeriod.periodEnd,periods.* ");
							sql.append("FROM tblAccPeriod accPeriod left join (SELECT tblAccPeriod.AccYear AS accyear,tblAccPeriod.AccPeriod AS accperiod,");
							sql.append("SUM(tblAccDetail.DebitAmount) AS sumDebitAmountBase,SUM(tblAccDetail.LendAmount) AS sumLendAmountBase,");
							sql.append("SUM(tblAccDetail.DebitCurrencyAmount) AS sumDebitAmount,SUM(tblAccDetail.LendCurrencyAmount) AS sumLendAmount,");
							sql.append("SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount) AS sumBalanceAmountBase,");
							sql.append("SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount) AS sumBalanceAmount");
							if(!"".equals(currencyValue)){
                        		sql.append(",isnull(tblAccDetail.Currency,'') as Currency ");
                        	}
							sql.append(" FROM tblAccPeriod left join tblAccDetail on tblAccPeriod.AccYear=tblAccDetail.PeriodYear and ");
							sql.append("tblAccPeriod.AccPeriod=tblAccDetail.PeriodMonth JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo");
							sql.append(" ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccTypeInfo.classCode like '"+classCode+"%'");
							sql.append(sqlCondition);
							if(binderNo == null || "".equals(binderNo)){
	            				//������δ����ƾ֤
								sql.append(" AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ");
	            			}
							if(!"".equals(currencyValue) && !"all".equals(currencyValue) && !"currency".equals(currencyValue)){
	                        	if("isBase".equals(currencyValue)){
	                        		//��λ��
	                        		sql.append(" AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+currencyName+"') ");
	                        	}else{
	                        		sql.append(" AND tblAccDetail.Currency='"+currencyName+"' ");
	                        	}
	                        }
							/* ��ʾ���ÿ�Ŀ */
	                    	if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
	                    		sql.append(" AND tblAccTypeInfo.statusId=0 ");
	                    	}
							sql.append(" GROUP BY tblAccPeriod.AccYear,tblAccPeriod.AccPeriod");
							if(!"".equals(currencyValue)){
								sql.append(",tblAccDetail.Currency");
							}
							sql.append(") AS periods ON accPeriod.AccYear=periods.accyear ");
							sql.append("AND accPeriod.AccPeriod=periods.accperiod");
							sql.append(" WHERE 1=1 "+periodSql);
							if(takeBrowNo != null && !"".equals(takeBrowNo)){
								//�޷������ʾ
								sql.append(" AND sumDebitAmountBase IS NOT NULL AND sumLendAmountBase IS NOT NULL");
							}
							if(balanceAndTakeBrowNo != null && !"".equals(balanceAndTakeBrowNo)){
								//�޷���������Ϊ�㲻��ʾ
								sql.append(" AND sumDebitAmountBase IS NOT NULL AND sumDebitAmountBase IS NOT NULL");
							}
							
							sql.append(" ORDER BY accPeriod.AccYear,accPeriod.AccPeriod");
							System.out.println("���ںϼ�SQL:"+sql.toString());
							List periodList = new ArrayList();
							rset=st.executeQuery(sql.toString());
							while (rset.next()) {
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			map.put(rset.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
	                    					String strvalue = String.valueOf(obj);
	                    					if (strvalue.indexOf(".")>0){
	                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
	                    					}
	                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
	                    						strvalue = "";
	                    					}
	                        				map.put(rset.getMetaData().getColumnName(i), strvalue);
	                        				if("sumBalanceAmountBase".equals(rset.getMetaData().getColumnName(i))){
	                        					if(!"".equals(strvalue)){
	                        						if(Double.valueOf(strvalue)>0){
	                        							map.put("isflag", "��");
	                        						}else if(Double.valueOf(strvalue)<0){
	                        							map.put("isflag", "��");
	                        						}else{
	                        							map.put("isflag", "ƽ");
	                        						}
	                        					}
	                        				}
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
	                        	
	                        	//��ѡ��ıұ��ǡ������бұ����ʽ
	                        	if("all".equals(currencyValue)){
	                        		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount"};
	                        		if("".equals(map.get("Currency"))){
	                        			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
	                        			for(String s : curs){
	                        				map.put(s+"_", map.get(s+"Base"));
	                        			}
	                        		}else{
	                        			//�������
	                        			for(String s : curs){
	                        				map.put(s+"_"+map.get("Currency"), map.get(s));
	                        			}
	                        		}
	                        	}
	                        	for(int k=0;k<periodList.size();k++){
	                        		HashMap oldMap = (HashMap)periodList.get(k);
	                        		if(oldMap != null && oldMap.size()>0){
	                        			if(oldMap.get("AccYear").equals(map.get("AccYear")) && oldMap.get("AccPeriod").equals(map.get("AccPeriod"))){
	                        				Iterator iter = oldMap.entrySet().iterator();
	                        				while(iter.hasNext()){
	                        					Entry entry = (Entry)iter.next();
	                        					String key = String.valueOf(entry.getKey());
	                        					if("sumDebitAmountBase".equals(key) || "sumLendAmountBase".equals(key)
	                        							|| "sumBalanceAmountBase".equals(key) || "sumBalanceAmount".equals(key)){
	                        						String money = "0";
	                        						Object o = entry.getValue();
	                        						if(o != null && !"".equals(o)){
	                        							money = o.toString();
	                        						}
	                        						o = map.get(key);
	                        						if(o != null && !"".equals(o)){
	                        							money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
	                        						}
	                        						map.put(key ,money);
	                        					}else{
	                        						map.put(key, entry.getValue());
	                        					}
	                        				}
	                        				periodList.remove(k);
	                        			}
	                        		}
	                        	}
	                        	periodList.add(map);
							}
							
	                        /**
	                    	 * �����ڼ�������ڲ�ѯ�ڴ�֮�е�ƾ֤
	                    	 */
	                    	StringBuffer sqlStr = new StringBuffer("SELECT * FROM (SELECT tblAccMain.BillDate,tblAccMain.CredTypeId,(tblAccMain.CredTypeId+' - '+convert(varchar(50),tblAccMain.OrderNo)) AS CredTypeOrderNo ");
	                    	sqlStr.append(",tblAccDetail.RecordComment,tblAccMain.OrderNo,tblAccMain.id as accmainid,tblAccMain.CredYear,tblAccMain.Period,");
	                    	sqlStr.append("tblAccDetail.DebitAmount as sumDebitAmountBase,tblAccDetail.LendAmount as sumLendAmountBase,tblAccDetail.DebitCurrencyAmount as sumDebitAmount,tblAccDetail.LendCurrencyAmount as sumLendAmount, ");
	                    	sqlStr.append("(tblAccDetail.DebitCurrencyAmount-tblAccDetail.LendCurrencyAmount) AS sumBalanceAmount,isnull(tblAccDetail.Currency,'') as Currency,");
	                    	sqlStr.append("(tblAccDetail.DebitAmount-tblAccDetail.LendAmount) AS sumBalanceAmountBase,tblAccDetail.CurrencyRate, ");
	                    	sqlStr.append("CASE WHEN tblAccDetail.Currency='' THEN (SELECT CurrencyName FROM tblCurrency WHERE isBaseCurrency=1) ");
	                    	sqlStr.append("WHEN tblAccDetail.currency!='' THEN (SELECT CurrencyName FROM tblCurrency WHERE id=tblAccDetail.Currency) END AS currencyName, ");
	                    	sqlStr.append("tblAccDetail.f_ref,tblAccDetail.AccCode ");
	                    	/* ��ʾƾ֤ҵ����Ϣ */
	                    	if(showMessage!=null && !"".equals(showMessage)){
	                        	sqlStr.append(",(SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(SELECT tblDbTableInfo.languageId FROM tblDbTableInfo WHERE tblDbTableInfo.tableName=tblAccMain.RefBillType and tblAccMain.RefBillType!='tblAccMain')) as RefBillTypeName ");
	                        	sqlStr.append(",(SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(SELECT modelName FROM tblModules WHERE tblModules.classCode=(SELECT TOP 1 substring(classCode,1,5) FROM tblModules ");
	                        	sqlStr.append(" WHERE (tblModules.linkAddress='/UserFunctionQueryAction.do?tableName='+tblAccMain.RefBillType or tblModules.linkAddress='/UserFunctionQueryAction.do?tableName='+tblAccMain.RefBillType+'&moduleType=1')))) AS RefModuleName ");
	                    	}
	                    	/* ���������Է���Ŀ������ʾ��sql��丽�� */
	                    	if(showAccType==null || "".equals(showAccType)){
	                    		sqlStr.append(",(SELECT TOP 1 acccode FROM tblAccDetail accdetail WHERE accdetail.f_ref=tblAccDetail.f_ref AND accdetail.DebitAmount!=tblAccDetail.DebitAmount) as detailacccode ");
	                    		sqlStr.append(",(SELECT TOP 1 (SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(select AccName from tblAccTypeInfo where AccNumber=accdetail.AccCode)) as accname FROM tblAccDetail accdetail WHERE accdetail.f_ref=tblAccDetail.f_ref AND accdetail.DebitAmount!=tblAccDetail.DebitAmount) as accname ");
	                    	}
	                    	sqlStr.append("FROM tblAccMain LEFT JOIN tblAccDetail ON tblAccMain.id=tblAccDetail.f_ref ");
	                    	sqlStr.append(" LEFT JOIN tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccTypeInfo.classCode like '"+classCode+"%' ");
	                    	
	                    	if(binderNo == null || "".equals(binderNo)){
	            				//������δ����ƾ֤
	                    		sqlStr.append(" AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ");
	            			}
	                    	/* ��ʾ���ÿ�Ŀ */
	                    	if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
	                    		sqlStr.append(" AND tblAccTypeInfo.statusId=0 ");
	                    	}
	                    	
	            			/* �����ڲ�ѯ */
	                    	if(dateType!=null && "1".equals(dateType)){
	                    		sqlStr.append(" AND CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+ right('00' + convert(varchar(10),tblAccMain.Period),2)>='"+startTime+"' ");
	                    		sqlStr.append(" AND CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+ right('00' + convert(varchar(10),tblAccMain.Period),2)<='"+endTime+"' ");
	                    	}else if(dateType!=null && "2".equals(dateType)){
								sqlStr.append(" AND tblAccMain.BillDate>='"+dateStart+"' ");
								sqlStr.append(" AND tblAccMain.BillDate<='"+dateEnd+"' ");
	                    	}else if(dateType!=null && "3".equals(dateType)){
	                    		//�׶�
								if("year".equals(momentType)){
									sqlStr.append(" AND tblAccMain.CredYear="+calendar.get(calendar.YEAR));
								}else if("month".equals(momentType)){
									sqlStr.append(" AND tblAccMain.CredYear="+calendar.get(calendar.YEAR)+" AND tblAccMain.Period="+(calendar.get(calendar.MONTH)+1));
								}else if("halfyear".equals(momentType) || "threemonth".equals(momentType)){
									simple = new SimpleDateFormat("yyyy-MM");
									sqlStr.append(" AND tblAccMain.BillDate>='"+simple.format(calendar.getTime())+"-01' and tblAccMain.BillDate<='"+simple.format(Calendar.getInstance().getTime())+"-31'");
								}else if("tomonth".equals(momentType)){
									simple = new SimpleDateFormat("yyyy-MM");
									sqlStr.append(" AND tblAccMain.BillDate>='"+simple.format(calendar.getTime())+"-01' and tblAccMain.BillDate<='"+simple.format(calendar.getTime())+"-31'");
								}else if("week".equals(momentType) || "threeday".equals(momentType)
										|| "day".equals(momentType) || "yesterday".equals(momentType)){
									simple = new SimpleDateFormat("yyyy-MM-dd");
									if("yesterday".equals(momentType)){
										sqlStr.append( "AND tblAccMain.BillDate='"+simple.format(calendar.getTime())+"'");
									}else{
										sqlStr.append(" AND tblAccMain.BillDate>='"+simple.format(calendar.getTime())+"' and tblAccMain.BillDate<='"+simple.format(Calendar.getInstance().getTime())+"'");
									}
								}
	                    	}
	                    	
	                    	//��ҹ���
	                    	if(!"".equals(currencyValue) && !"all".equals(currencyValue) && !"currency".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		sql.append(" AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+currencyName+"') ");
                            	}else{
                            		sql.append(" AND tblAccDetail.Currency='"+currencyName+"' ");
                            	}
                            }
	                    	
							sqlStr.append(conditions + ") AS detail1 ");
							/* ���������Է���Ŀ������ʾ��sql��丽�� */
							if(showAccType!=null && !"".equals(showAccType)){
								sqlStr.append("JOIN (SELECT AccCode as detailacccode,(SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(select AccName from tblAccTypeInfo where AccNumber=accdetail.AccCode)) as accname,");
								sqlStr.append("f_ref,isnull(DebitAmount,0) as dAmount,isnull(LendAmount,0) as lAmount FROM tblAccDetail accdetail)");
								sqlStr.append(" AS detail2 ON detail2.f_ref=detail1.f_ref "); 
								sqlStr.append("WHERE detail2.detailacccode!=detail1.AccCode AND detail1.sumDebitAmountBase!=detail2.dAmount ");
							}
							if(orderby != null && !"".equals(orderby)){
								//�������õĽ�������
								sqlStr.append(" order by "+orderby);
								if(orderby.indexOf("CredTypeId")!=-1){
									sqlStr.append(","+orderby.replace("CredTypeId", "OrderNo"));
								}
							}else{
								//Ĭ�ϸ��ݵ���ʱ������
								sqlStr.append(" order by BillDate ASC");
							}
							System.out.println("��ѯ�ڼ��ƾ֤sql:"+sqlStr.toString());
							rset = st.executeQuery(sqlStr.toString());
							List listAcc = new ArrayList();
							String ids = "";
							while (rset.next()) {
	                        	HashMap accmap=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			accmap.put(rset.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC && !"CurrencyRate".equals(rset.getMetaData().getColumnName(i))){
	                    					String strvalue = String.valueOf(obj);
	                    					if (strvalue.indexOf(".")>0){
	                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
	                    					}
	                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
	                    						strvalue = "";
	                    					}
	                    					accmap.put(rset.getMetaData().getColumnName(i), strvalue);
	                        			}else{
	                        				accmap.put(rset.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
	                        	
	                        	//���бұ����ʽʱ
	                        	if("all".equals(currencyValue)){
	                        		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount"};
	                        		if("".equals(accmap.get("Currency"))){
	                        			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
	                        			for(String s : curs){
	                        				accmap.put(s+"_", accmap.get(s+"Base"));
	                        			}
	                        		}else{
	                        			//�������
	                        			for(String s : curs){
	                        				accmap.put(s+"_"+accmap.get("Currency"), accmap.get(s));
	                        			}
	                        		}
	                        	}
	                        	//��ͬʱ
	                        	if(ids.equals(accmap.get("accmainid").toString())){
	                        		//accmap.put("DebitAmount", accmap.get("dAmount"));
	                        		//accmap.put("LendAmount", accmap.get("lAmount"));
	                        	}
	                        	ids = accmap.get("accmainid").toString();
	                        	listAcc.add(accmap);
							}
							
							/**
							 * �������ݣ�initMap �ڳ�Map, periodList ���ڽ��List, listAcc �ڼ��е�ƾ֤���ݣ�
							 */
				            Object[] object = new Object[]{initMap, periodList, listAcc};
				            rst.setRetVal(object);
	            	} catch (Exception ex) {
						ex.printStackTrace();
						BaseEnv.log.error("FinanceReportMgt queryAccDetData:",ex) ;
						rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						return;
					}
	            }
	        });
	            return rst.getRetCode();
	        }
        });
        rst.setRetCode(retCode);
        
        //ȡ����
        Object[] object = (Object[])rst.getRetVal();
        HashMap initMap = (HashMap)object[0];
        List periodList = (ArrayList)object[1];
        List listAcc = (ArrayList)object[2];
        
        /**
         * ��������
         */
		List allDataList = new ArrayList();
		//String[] yearStr = new String[]{"sumDebitAmount","sumLendAmount","sumDebitCurrencyAmount","sumLendCurrencyAmount","sumBalanceAmount"};
		
		//��ÿһ�����ݽ��д���
		String[] strs = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount","sumDebitAmountBase","sumLendAmountBase","sumBalanceAmountBase"};
    	if("all".equals(currencyValue)){
    		String[] newstrs = new String[strs.length+currList.size()*3];
    		int count = 0;
    		for(int n=0;n<strs.length/2;n++){
    			for(int l = 0;l<currList.size();l++){
    				String[] currStr = (String[])currList.get(l);
    				if("true".equals(currStr[2])){
    					//��λ��
    					newstrs[count] = strs[n]+"_";
    				}else{
    					newstrs[count] = strs[n]+"_"+currStr[0];
    				}
    				count ++ ;
    			}
    			newstrs[count] = strs[n];
    			count ++;
    		}
    		for(int n=strs.length/2;n<strs.length;n++){
    			newstrs[count] = strs[n];
    			count ++;
    		}
    		strs = newstrs;
    	}
    	for(int j=0;j<periodList.size();j++){
    		//ѭ��Ҫ��ѯ�Ļ���ڼ�
    		HashMap accPeriodmap = (HashMap)periodList.get(j);
    		
    		/* ���汾�ڼ����е�ƾ֤��Ϣ */
    		List detailList = new ArrayList();
    		HashMap oldMap = null;
	        for(int k=0;k<listAcc.size();k++){
	        	//ѭ������ƾ֤��ϸ
	        	HashMap accmap = (HashMap)listAcc.get(k);
	        	if(accPeriodmap.get("AccYear").equals(accmap.get("CredYear")) && 
	        			accPeriodmap.get("AccPeriod").equals(accmap.get("Period"))){
	        		//������ڼ��꣬����ڼ���ڴ��ڼ�ʱ��֤����ƾ֤��ϸ�Ǳ��ڵ�ƾ֤��ϸ
	        		String money = "0";
	        		Object o = null;
	        		
	        		String[] str1 = null;
	        		//���бұ����ʽ
					if("all".equals(currencyValue)){
						str1 = new String[currList.size()+1];
						for(int l = 0;l<currList.size();l++){
							String[] currStr = (String[])currList.get(l);
							if("true".equals(currStr[2])){
								//��λ��
								str1[l] = "_";
							}else{
								str1[l] = "_"+currStr[0];
							}
						}
						str1[currList.size()] = "Base";
					}else{
						str1 = new String[]{"","Base"};
					}
	        		for(String s : str1){
	        			money = "0";
	        			if(oldMap != null && oldMap.size()>0){
		        			//��һ����ϸ�����
		        			o = oldMap.get("sumBalanceAmount"+s);
		        			if(o!=null && !"".equals(o)){
								money = o.toString();
							}
		        			//��������ϸ���
							o = accmap.get("sumBalanceAmount"+s);
							if(o!=null && !"".equals(o)){
								money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
							}
							//����һ����¼�Ľ��+������¼�Ľ�
							accmap.put("sumBalanceAmount"+s, money);
		        		}else{
		        			//��ϸ��¼��һ��ʱ�����=��һ�ڵı��ںϼƻ����ڳ����+��¼����
		        			HashMap preMap = null;
		        			if(allDataList.size()>0){
		        				preMap = (HashMap)allDataList.get(allDataList.size()-1);			//ȡ��һ�ڼ����������
							}
		        			if(preMap!=null && preMap.size()>0){
		        				//������һ�ڵļ�¼ʱ�����ȡ��һ�ڵĽ��
		        				o = preMap.get("sumBalanceAmount"+s);
		        			}else{
		        				//����������һ��ʱ�����ȡ�ڳ����
		        				o = initMap.get("sumBalanceAmount"+s);
		        			}
		        			if(o!=null && !"".equals(o)){
		        				money = o.toString();
		        			}
		        			//����¼�����
		        			o = accmap.get("sumBalanceAmount"+s);
		        			if(o!=null && !"".equals(o)){
		        				money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
		        			}
		        			accmap.put("sumBalanceAmount"+s, money);						//������
		        		}
	        		}
	        		
	        		//�Ѵ���������ݱ�����ArrayList��
	        		detailList.add(accmap);
	        		oldMap = accmap;
	        	}
	        }
	        
	        //ѭ������ı��ڼ䵥�����ݽ��д���
	        for(int s =0;s<detailList.size();s++){
	        	HashMap accMap = (HashMap)detailList.get(s);
	        	
	        	for(String str : strs){
	        		String absValue = "";
	        		
	        		if(str.indexOf("sumBalanceAmount")!=-1){
	        			absValue = "abs";
	        			//����
	        			if("sumBalanceAmountBase".equals(str)){
	        				if(accMap.get(str)!=null && !"".equals(accMap.get(str))){
	        					if(Double.valueOf(accMap.get(str).toString())>0){
	        						//�跽
	        						accMap.put("isflag", "��");
	        					}else if(Double.valueOf(accMap.get(str).toString())<0){
	        						//����
	        						accMap.put("isflag", "��");
	        					}else{
	        						accMap.put("isflag", "ƽ");
	        					}
	        				}
	        			}
	        		}
	        		accMap.put(str, dealDataDouble(String.valueOf(accMap.get(str)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), absValue));
	        	}
	        }
	        
	        //���汾�ڼ��е�ƾ֤����
	        accPeriodmap.put("detail", detailList);
	        
	        /* �����ںϼƺͱ����ۼ� */
			for(int k = 0;k<strs.length;k++){
				String moneyyear = "0";
				String moneyperiod = "0";
				//���ڽ��
				Object o = accPeriodmap.get(strs[k]);
				if(o != null && !"".equals(o)){
					moneyyear = o.toString();
					moneyperiod = o.toString();
				}
				Integer period = Integer.valueOf(accPeriodmap.get("AccPeriod").toString());
				oldMap = new HashMap();
				if(period>-1){
					//������ڴ���1 �����ںϼ�+�����ۼƣ�
					if(allDataList.size()>0){
						oldMap = (HashMap)allDataList.get(allDataList.size()-1);			//ȡ��һ�ڼ�ı����ۼ�
					}
				}
				
				/* �����ںϼƣ����ڽ��+��һ�ڱ����������ڳ��� */
				/* �������ۼƽ�� */
				if(oldMap.size()>0){
					/* �ж��ڼ��Ƿ�����һ�ڵģ��������һ�ڵľ���� */
					if(oldMap.get("AccYear").equals(accPeriodmap.get("AccYear"))){
						
						//��һ�ڵı����ۼ�
						o = oldMap.get("year_"+strs[k]);
						if(o != null && !"".equals(o)){
							moneyyear = new BigDecimal(moneyyear).add(new BigDecimal(o.toString())).toString();
						}
						//��һ�ڵı��ںϼ�
						if(strs[k].indexOf("sumBalanceAmount") !=-1){
							o = oldMap.get(strs[k]);
							if(o != null && !"".equals(o)){
								moneyperiod = new BigDecimal(moneyperiod).add(new BigDecimal(o.toString())).toString();
							}
						}
					}else{ //��������������ۼƼ����
						//��һ�ڵı����ۼ�
						o = oldMap.get("year_"+strs[k]);
						if(strs[k].indexOf("sumDebitAmount") !=-1 || strs[k].indexOf("sumLendAmount") !=-1){
							moneyyear = moneyyear;//�������
						}else{
							if(o != null && !"".equals(o)){
								moneyyear = new BigDecimal(moneyyear).add(new BigDecimal(o.toString())).toString();
							}
						}
						//��һ�ڵı��ںϼ�
						if(strs[k].indexOf("sumBalanceAmount") !=-1){
							o = oldMap.get(strs[k]);
							if(o != null && !"".equals(o)){
								moneyperiod = new BigDecimal(moneyperiod).add(new BigDecimal(o.toString())).toString();
							}
						}
					}
				}else{
					/* ��������һ��Ľ��ʱ��ȡ�ڳ���� */ 
					o = initMap.get(strs[k]);
					if(o != null && !"".equals(o)){
						moneyyear = new BigDecimal(moneyyear).add(new BigDecimal(o.toString())).toString();
						if(strs[k].indexOf("sumBalanceAmount") != -1){
							moneyperiod = new BigDecimal(moneyperiod).add(new BigDecimal(o.toString())).toString();
						}
					}
					
				}
				/* ���������� */
				if("sumBalanceAmountBase".equals(strs[k])){
					//���ڷ���
					if(Double.valueOf(moneyperiod)>0){
						//�跽
						accPeriodmap.put("isflag", "��");
					}else if(Double.valueOf(moneyperiod)<0){
						//����
						accPeriodmap.put("isflag", "��");
					}else{
						accPeriodmap.put("isflag", "ƽ");
					}
					
					//���귽��
					if(Double.valueOf(moneyyear)>0){
						//�跽
						accPeriodmap.put("isYearflag", "��");
					}else if(Double.valueOf(moneyyear)<0){
						//����
						accPeriodmap.put("isYearflag", "��");
					}else{
						accPeriodmap.put("isYearflag", "ƽ");
					}
				}
				accPeriodmap.put(strs[k], dealDataDouble(String.valueOf(moneyperiod), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
				accPeriodmap.put("year_"+strs[k], dealDataDouble(String.valueOf(moneyyear), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
			}
    		allDataList.add(accPeriodmap);
    	}
    	
    	/**
    	 * �ڳ�����
    	 */
    	for(String s : strs){
    		String absFlag = "";
    		if(s.indexOf("sumBalanceAmount")!=-1){
    			absFlag = "abs";
    		}
    		initMap.put(s, dealDataDouble(String.valueOf(initMap.get(s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), absFlag));
    	}
    	
    	/**
    	 * ����С������ƺ���������
    	 */
    	for(int i=0;i<allDataList.size();i++){
    		HashMap accPeriodMap = (HashMap)allDataList.get(i);
    		Iterator iter = accPeriodMap.entrySet().iterator();
    		while(iter.hasNext()){
    			Entry entry = (Entry)iter.next();
    			String key = entry.getKey().toString();
    			if(key.indexOf("sumBalanceAmount")!=-1 || key.indexOf("year_sumBalanceAmount")!=-1){
    				accPeriodMap.put(entry.getKey(), dealDataDouble(String.valueOf(accPeriodMap.get(entry.getKey())), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
    			}
    		}
    	}
    	object = new Object[]{initMap, allDataList, currencyValue, currList};
    	rst.setRetVal(object);
        return rst;
	}                    	

	/**
	 * ������ú�����Ŀ�Ŀʱ����ѯ�¼�����Ŀ�Ŀ
	 * @param classCode
	 * @param showItemDetail
	 * @param gradeShow
	 * @param itemSort
	 * @param itemLevel
	 * @param itemCodeStart
	 * @param itemCodeEnd
	 * @return
	 */
	public Result queryAccTypeIsItem(final String classCode,final String asClassCode,final String showBanAccTypeInfo,final String showItemDetail,final String gradeShow,final String itemSort,
			final String itemLevel,final String itemCodeStart,final String itemCodeEnd){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							StringBuffer sql = new StringBuffer("SELECT a.AccNumber,l.zh_CN as AccName,PyCode,a.classCode,a.isCatalog,ISNULL(a.isCalculateParent,0) as isCalculateParent,a.classCode as asClassCode");
							sql.append(" FROM tblAccTypeInfo a LEFT JOIN tblLanguage l ON l.id=a.AccName WHERE 1=1 ");
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//���˽��õĻ�ƿ�Ŀ
								sql.append(" AND statusId=0 ");
							}
							if(showItemDetail != null && !"".equals(showItemDetail)){
								if((!"all".equals(itemSort))){
									//��������
									sql.append(" AND "+itemSort+"!='' ");
									//��Ŀ����
									if(itemLevel!=null && !"".equals(itemLevel)){
										sql.append(" AND len("+itemSort+")/5="+itemLevel);
									}
									//��Ŀ���뿪ʼ
									if(itemCodeStart!=null && !"".equals(itemCodeStart)){
										sql.append(" AND "+itemSort+">='"+itemCodeStart+"'");
									}
									//��Ŀ�������
									if(itemCodeEnd!=null && !"".equals(itemCodeEnd)){
										sql.append(" AND "+itemSort+"<='"+itemCodeEnd+"'");
									}
								}else{
									sql.append(" AND (departmentCode!='' OR clientCode!='' OR employeeId!='' OR supliercode!='' OR stockCode!='') ");
								}
							}
							
							if(gradeShow!=null && !"".equals(gradeShow)){
								//�ּ���ʾ
								String classCodes = asClassCode.substring(0,asClassCode.length()-5);
								sql.append(" and "+itemSort+"='"+classCode+"' and a.classCode like '"+classCodes+"%'");
							}else{
								sql.append(" and a.classCode like '"+classCode+"%' ");
							}
							sql.append(" order by a.AccNumber,l.zh_CN");
							Statement st = conn.createStatement();
                            List list = new ArrayList();
                            ResultSet rs=st.executeQuery(sql.toString()); 
							while(rs.next()){
								HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryAccTypeIsItem:",ex) ;
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
	 * ��ѯ����ʽ��ϸ���б�����
	 * @parent keyWord      //�ؼ�������
	 * @return
	 */
	public Result queryAccDesign(final String keyWord){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT id,accName FROM tblAccDesign");
							if(keyWord!=null && !"".equals(keyWord)){
								sql.append(" WHERE accName like '%"+keyWord+"%'");
							}
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							List list = new ArrayList();
							while(rs.next()){
								String[] str = new String[2];
								str[0] = rs.getString("id");
								str[1] = rs.getString("accName");
								list.add(str);
							}
							result.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryAccDesign:",ex) ;
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
	 * ɾ��������Ƽ�¼
	 * @param id
	 * @return
	 */
	public Result delAccDesign(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("DELETE FROM tblAccDesign WHERE id=?");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, id);
							int count = ps.executeUpdate();
							result.setRetVal(count);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt delAccDesign:",ex) ;
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
	 * ����id��ѯ������Ƽ�¼
	 * @param id
	 * @return
	 */
	public Result queryDesignById(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT id,acctypecode,accName,columndata,currencydata,levelsetting,levelvalue FROM tblAccDesign WHERE id='"+id+"'");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							String[] str = new String[7];
							if(rs.next()){
								str[0] = rs.getString("id");
								str[1] = rs.getString("acctypecode");
								str[2] = rs.getString("accName");
								str[3] = rs.getString("columndata");
								str[4] = rs.getString("currencydata");
								str[5] = String.valueOf(rs.getInt("levelsetting"));
								str[6] = rs.getString("levelvalue");
							}
							result.setRetVal(str);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryDesignById:",ex) ;
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
	 * ���ݸ�����ƿ�Ŀ��ѯ���¼���ƿ�Ŀ
	 * @param acctype
	 * @param level
	 * @param itemSort
	 * @return
	 */
	public Result queryAccTypeChild(final String acctype,final String level,final String itemSort){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT a.AccNumber,l.zh_CN as AccName,a.jdFlag FROM tblAccTypeInfo a LEFT JOIN tblLanguage l ON l.id=a.AccName");
							sql.append(" WHERE a.statusId!='-1' AND len(a.classCode)>5 AND a.classCode LIKE (SELECT classCode FROM tblAccTypeInfo WHERE AccNumber='"+acctype+"')+'%'");
							if(level != null && !"".equals(level)){
								sql.append(" AND (a.isCatalog=0 OR a.classCode LIKE substring(a.classCode,0,10)+'%') AND len(a.classCode)/5-1<="+level+" AND isnull(a.isCalculateParent,0)!=1");
							}else{
								sql.append(" AND a.isCatalog=0 AND isnull(a.isCalculateParent,0)!=1");
							}
							if(itemSort != null && !"".equals(itemSort)){
								sql.append(" AND "+itemSort+" IS NOT NULL AND "+itemSort+"!=''");
							}else{
								sql.append(" AND isnull( a.isCalculate,2)!=1");
							}
							sql.append(" ORDER BY a.AccNumber");
							Statement st = conn.createStatement();
							BaseEnv.log.debug("FinanceReportMgt.queryAccTypeChild sql="+sql.toString());
							ResultSet rs = st.executeQuery(sql.toString());
							List list = new ArrayList();
							while(rs.next()){
								String[] str = new String[3];
								str[0] = rs.getString("AccNumber");
								str[1] = rs.getString("AccName");
								str[2] = String.valueOf(rs.getInt("jdFlag"));
								list.add(str);
							}
							result.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryAccTypeChild:",ex) ;
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
	 * ���ݻ�ƿ�ĿclassCode��ѯ��ƿ�Ŀ��Ϣ
	 * @param acctype
	 * @return
	 */
	public Result queryAccType(final String acctype){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select a.AccNumber,l.zh_CN as AccName,a.jdFlag FROM tblAccTypeInfo a LEFT JOIN tblLanguage l ON l.id=a.AccName");
							sql.append(" WHERE a.AccNumber='"+acctype+"'");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							String[] str = new String[3];
							if(rs.next()){
								str[0] = rs.getString("AccNumber");
								str[1] = rs.getString("AccName");
								str[2] = String.valueOf(rs.getInt("jdFlag"));
							}
							result.setRetVal(str);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryAccType:",ex) ;
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
	 * ���/�޸��������
	 * @param id
	 * @return
	 */
	public Result addOrUpdateDesign(final String id,final String code,final String name,
			final String columndata,final String currencydata,final String levelsetting, final String levelvalue){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("");
							String ids = id;
							if(id != null && !"".equals(id)){
								//���ڼ�¼ʱ��ִ���޸Ĳ���
								sql.append("UPDATE tblAccDesign SET acctypecode=?,accName=?,columndata=?,currencydata=?,levelsetting=?,levelvalue=? WHERE id=?");
							}else{
								//������¼
								sql.append("INSERT INTO tblAccDesign(acctypecode,accName,columndata,currencydata,levelsetting,levelvalue,id) VALUES(?,?,?,?,?,?,?)");
								ids = IDGenerater.getId();
							}
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, code);
							ps.setString(2, name);
							ps.setString(3, columndata);
							ps.setString(4, currencydata);
							ps.setString(5, levelsetting);
							ps.setString(6, levelvalue);
							ps.setString(7, ids);
							ps.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt addOrUpdateDesign:",ex) ;
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
	 * ����ʽ��ϸ�˲�ѯ�б�
	 * 1.�����û��Զ������ü�¼��ѯ�ڳ����
	 * 2.��ѯ���ںϼ� �����ۼ�
	 * 3.��ѯĳ�ڼ����ϸ����ƾ֤��¼
	 * 4.��������
	 * @param conMap       ��ѯ����
	 * @return
	 */
	public Result queryAllDetData(final HashMap<String, String> conMap,final String[] str){
		
		String accTypeValue = "";								//��Ʊ��
		String currencys = "";									//����
		String isbaseCurr = "";									//��¼��λ��id
		String currIsBase = "all";
		String isBaseCurrency = "";
		HashMap acctypeMap = new HashMap();
		
		/**
		 * �����û����õ���Ϣ
		 */
		if(str!=null && str.length>0){
			/* �û����õ���Ŀ */
			String designAccType = str[3];
			if(designAccType!=null && !"".equals(designAccType)){
				String[] value1 = designAccType.split("//");
				for(int i=0;i<value1.length;i++){
					String[] value2 = value1[i].split(";");
					accTypeValue += "'"+value2[1]+"'";
					if(i<value1.length-1){
						accTypeValue += ",";
					}
					acctypeMap.put(value2[1], value2);
				}
			}
			/* �û�ѡ������ */
			String currencyValues = str[4];
			if(currencyValues!=null && !"".equals(currencyValues)){
				String[] value1 = currencyValues.split(",");
				for(int i=0;i<value1.length;i++){
					Result res = queryIsBaseCurrency(value1[i]);
					isBaseCurrency = res.retVal.toString();
					if("true".equals(isBaseCurrency)){
						isbaseCurr = value1[i];
						currencys += "'',";
					}
					currencys += "'"+value1[i]+"'";
					if(i<value1.length-1){
						currencys += ",";
					}
				}
				if(value1.length==1 && "true".equals(isBaseCurrency)){
					//ֻ����һ�ֱұ����Ǳ�λ��
					currIsBase = "isbase";
				}else{
					currIsBase = "other";
				}
			}
		}
		
		/**
		 * ��ѯ���ݿ�����
		 */
		final String accTypeValues = accTypeValue;
		final String currency = currencys;
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement st = conn.createStatement();
						ResultSet rset = null;
						/* ��ƿ�Ŀƴ���ַ������磺'100101','100102','100103'�� */
						
						String binderNo = conMap.get("binderNo");												//����δ����ƾ֤
						String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");							//��ʾ���ÿ�Ŀ
						String itemSort = conMap.get("itemSort");												//������Ŀ
						String acctypeCodeStart = conMap.get("acctypeCodeStart");								//������Ŀ��ſ�ʼ
						String acctypeCodeEnd = conMap.get("acctypeCodeEnd");									//������Ŀ��Ž���
						
						//���ڼ�����ڼ�ת��Ϊָ����ʽ
						String startTime = "";
						String endTime = "";
						if(Integer.valueOf(conMap.get("periodStart"))<10){
							startTime = conMap.get("periodYearStart")+"-0"+conMap.get("periodStart");
						}else{
							startTime = conMap.get("periodYearStart")+"-"+conMap.get("periodStart");
						}
						if(Integer.valueOf(conMap.get("periodEnd"))<10){
							endTime = conMap.get("periodYearEnd")+"-0"+conMap.get("periodEnd");
						}else{
							endTime = conMap.get("periodYearEnd")+"-"+conMap.get("periodEnd");
						}
						
						//��ѯ�����Ľ���ڳ�
						String sql = "SELECT tblAccDetail.AccCode,SUM(tblAccDetail.DebitAmount) AS sumDebitAmount,SUM(tblAccDetail.LendAmount) AS sumLendAmount,";
						sql += "SUM(tblAccDetail.DebitCurrencyAmount) AS sumDebitCurrencyAmount,SUM(tblAccDetail.LendCurrencyAmount) AS sumLendCurrencyAmount,";
						sql += "(SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount)) AS sumBalanceAmount,";
						sql += "(SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount)) AS sumBalanceCurrencyAmount";
						if(!"".equals(currency)){
							sql += ",ISNULL(tblAccDetail.currency,'') AS Currency"; 
						}
						sql += " FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo";
						sql += " ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccDetail.AccCode IN ("+accTypeValues+")";
						sql += " AND tblAccMain.CredYear<="+conMap.get("periodYearStart")+" AND tblAccMain.Period<"+conMap.get("periodStart");
						if(!"".equals(currency)){
							sql += " AND tblAccDetail.currency IN ("+currency+")";
						}
						if(binderNo == null || "".equals(binderNo)){
            				//������δ����ƾ֤
							sql += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
            			}
						if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
							//����ʾ���ÿ�Ŀ
							sql += " AND tblAccTypeInfo.statusId=0";
						}
						sql += " GROUP BY tblAccDetail.AccCode ";
						if(!"".equals(currency)){
							sql += ",tblAccDetail.currency ";
						}
						sql += "ORDER BY tblAccDetail.AccCode";
						System.out.println("��ѯ�ڳ���"+sql);
						rset = st.executeQuery(sql);
						List initList = new ArrayList();
						while (rset.next()) {
							HashMap map=new HashMap();
                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                        		Object obj=rset.getObject(i);
                        		if(obj==null){
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                        				map.put(rset.getMetaData().getColumnName(i), 0);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), "");
                        			}
                        		}else{
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                    					String strvalue = String.valueOf(obj);
                    					if (strvalue.indexOf(".")>0){
                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                    					}
                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                    						strvalue = "";
                    					}
                    					map.put(rset.getMetaData().getColumnName(i), strvalue);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), obj);
                        			}
                        		}
                        	}
                        	initList.add(map);
						}
						
						/* ��ѯ����ǰ���ڳ���� */
						sql = "SELECT SubCode AS AccCode,CurrYIniDebitSumBase AS sumDebitAmount,CurrYIniCreditSumBase AS sumLendAmount,";
						sql += "CurrYIniDebitSum as sumDebitCurrencyAmount,CurrYIniCreditSum as sumLendCurrencyAmount,";
						sql += "(case tblAccTypeInfo.JdFlag when 2 then 0-CurrYIniBalaBase else CurrYIniBalaBase end) AS sumBalanceAmount,";
						sql += "(case tblAccTypeInfo.JdFlag when 2 then 0-CurrYIniBala else CurrYIniBala end) AS sumBalanceCurrencyAmount,";
						sql += "isnull(CurType,'') AS Currency FROM tblAccBalance ";
						sql += " left join tblAccTypeInfo on tblAccBalance.SubCode=tblAccTypeInfo.AccNumber ";
						sql += " WHERE Nyear=-1 AND Period=-1 AND SubCode IN ("+accTypeValues+") ORDER BY SubCode";
						System.out.println("�ڳ�sql:"+sql);
						rset = st.executeQuery(sql);
						while (rset.next()) {
							HashMap map=new HashMap();
                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                        		Object obj=rset.getObject(i);
                        		if(obj==null){
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                        				map.put(rset.getMetaData().getColumnName(i), 0);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), "");
                        			}
                        		}else{
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                    					String strvalue = String.valueOf(obj);
                    					if (strvalue.indexOf(".")>0){
                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                    					}
                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                    						strvalue = "";
                    					}
                    					map.put(rset.getMetaData().getColumnName(i), strvalue);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), obj);
                        			}
                        		}
                        	}
                        	/* �����ڳ����ݣ���ͬ��Ŀ�Ľ����� */
                        	String[] strMoney = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount","sumDebitCurrencyAmount","sumLendCurrencyAmount","sumBalanceCurrencyAmount"};
                        	for(int j=0;j<initList.size();j++){
    							HashMap initmap1 = (HashMap)initList.get(j);
    							if(initmap1.get("AccCode").equals(map.get("AccCode"))){
    								boolean isCur = false;
    								if(!"".equals(currency)){
    									//���������
    									if(initmap1.get("Currency").equals(map.get("Currency"))){
    										isCur = true;
    									}
    								}else{
    									isCur = true;
    								}
    								if(isCur){
    									//�ÿ�Ŀ���
    									for(String s : strMoney){
    										String money = "0";
    										Object o = initmap1.get(s); 
    										if(o != null && !"".equals(o)){
    											money = o.toString();
    										}
    										o = map.get(s);
    										if(o != null && !"".equals(o)){
    											money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
    										}
    										map.put(s, money);
    									}
    									initList.remove(j);
    								}
    							}
        					}
                        	initList.add(map);
						}
						
						/* ������Ŀ��ʼ */
						String conditions = "";
						if(acctypeCodeStart!=null && !"".equals(acctypeCodeStart)){
							if("ClientCode".equals(itemSort) || "SuplierCode".equals(itemSort)){
								//������λ���ͻ�,��Ӧ��
								conditions += " AND tblAccDetail.CompanyCode>=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+acctypeCodeStart+"' order by com.classCode)";
							}else if("StockCode".equals(itemSort)){
								//�ֿ⿪ʼ
								conditions += " AND tblAccDetail.StockCode>=(select top 1 stock.classCode from tblStock stock where stock.stockNumber='"+acctypeCodeStart+"' order by stock.classCode)";
							}else if("DepartmentCode".equals(itemSort)){
								//����
								conditions += " AND tblAccDetail.DepartmentCode>=(select top 1 dept.classCode from tblDepartment dept where dept.deptCode='"+acctypeCodeStart+"' order by dept.classCode)";
							}else if("EmployeeID".equals(itemSort)){
								//ְԱ
								conditions += " AND tblAccDetail.EmployeeID>=(select top 1 emp.id from tblEmployee emp where emp.EmpNumber='"+acctypeCodeStart+"' order by tblEmployee.id)";
							}else if("ProjectCode".equals(itemSort)){
								//��Ŀ
								conditions += " AND tblAccDetail.ProjectCode>=(select top 1 p.id from tblProject p where p.ProjectNo='"+acctypeCodeStart+"' order by tblProject.id)";
							}
						}
						
						/* ѡ�������Ŀ���� */
						if(acctypeCodeEnd!=null && !"".equals(acctypeCodeEnd)){
							if("ClientCode".equals(itemSort) || "SuplierCode".equals(itemSort)){
								conditions += " AND tblAccDetail.CompanyCode<=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+acctypeCodeEnd+"' order by com.classCode DESC)";
							}else if("StockCode".equals(itemSort)){
								//�ֿ⿪ʼ
								conditions += " AND tblAccDetail.StockCode<=(select top 1 stock.classCode from tblStock stock where stock.stockNumber='"+acctypeCodeEnd+"' order by stock.classCode DESC)";
							}else if("DepartmentCode".equals(itemSort)){
								//����
								conditions += " AND tblAccDetail.DepartmentCode<=(select top 1 dept.classCode from tblDepartment dept where dept.deptCode='"+acctypeCodeEnd+"' order by dept.classCode DESC)";
							}else if("EmployeeID".equals(itemSort)){
								//ְԱ
								conditions += " AND tblAccDetail.EmployeeID<=(select top 1 emp.id from tblEmployee emp where emp.EmpNumber='"+acctypeCodeEnd+"' order by tblEmployee.id DESC)";
							}else if("ProjectCode".equals(itemSort)){
								//��Ŀ
								conditions += " AND tblAccDetail.ProjectCode<=(select top 1 p.id from tblProject p where p.ProjectNo='"+acctypeCodeEnd+"' order by tblProject.id DESC)";
							}
						}
						
						//��ѯ���ںϼ� �����ۼ�
						sql = "SELECT tblAccDetail.AccCode,tblAccMain.CredYear,tblAccMain.Period,tblAccPeriod.periodEnd,SUM(tblAccDetail.DebitAmount) AS sumDebitAmount,SUM(tblAccDetail.LendAmount) AS sumLendAmount,";
						sql += "SUM(tblAccDetail.DebitCurrencyAmount) AS sumDebitCurrencyAmount,SUM(tblAccDetail.LendCurrencyAmount) AS sumLendCurrencyAmount,";
						sql += "(SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount)) AS sumBalanceAmount";
						if(!"".equals(currency)){
							sql += ",ISNULL(tblAccDetail.currency,'') AS Currency";
						}
						sql += " FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode";
						sql += " left join tblAccPeriod on tblAccPeriod.AccYear=tblAccMain.CredYear and tblAccPeriod.AccPeriod=tblaccmain.Period ";
						sql += " WHERE tblAccDetail.AccCode IN ("+accTypeValues+")";
                        String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
                        String periodCondition = " AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'";
                        if(!"".equals(currency)){
                        	periodCondition += " AND tblAccDetail.currency IN ("+currency+")";
                        }
						sql += periodCondition;
						if(binderNo == null || "".equals(binderNo)){
            				//������δ����ƾ֤
							sql += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
            			}
						if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
							//����ʾ���ÿ�Ŀ
							sql += " AND tblAccTypeInfo.statusId=0";
						}
						sql += conditions;
						sql += " GROUP BY tblAccDetail.AccCode ";
						if(!"".equals(currency)){
							sql += ",tblAccDetail.currency ";
						}
						sql += " ,tblAccMain.CredYear,tblAccMain.Period,tblAccPeriod.periodEnd ORDER BY tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccCode";
						System.out.println("��ѯ����/�����"+sql);
						rset = st.executeQuery(sql);
						List curPeriodList = new ArrayList();
						while (rset.next()) {
							HashMap map=new HashMap();
                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                        		Object obj=rset.getObject(i);
                        		if(obj==null){
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                        				map.put(rset.getMetaData().getColumnName(i), 0);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), "");
                        			}
                        		}else{
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                    					String strvalue = String.valueOf(obj);
                    					if (strvalue.indexOf(".")>0){
                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                    					}
                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                    						strvalue = "";
                    					}
                    					map.put(rset.getMetaData().getColumnName(i), strvalue);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), obj);
                        			}
                        		}
                        	}
                        	curPeriodList.add(map);
						}
						
						//��ѯ����������������ϸ������ƾ֤�����ݣ�
						sql = "SELECT tblAccMain.id,tblAccDetail.AccCode,tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccDate,";
						sql += "tblAccMain.CredTypeID+'-'+convert(varchar,tblAccMain.OrderNo) AS credTypeIdOrderNo,tblAccDetail.RecordComment,";
						sql += "(DebitAmount-LendAmount) AS sumAmount,";
						sql += "DebitAmount,LendAmount,DebitCurrencyAmount,LendCurrencyAmount,ISNULL(tblAccDetail.Currency,'') AS Currency from tblAccDetail LEFT JOIN ";
						sql += "tblAccMain ON tblAccDetail.f_ref=tblAccMain.id LEFT JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccDetail.AccCode IN ("+accTypeValues+")"+periodCondition;
						if(binderNo == null || "".equals(binderNo)){
            				//������δ����ƾ֤
							sql += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
            			}
						if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
							//����ʾ���ÿ�Ŀ
							sql += " AND tblAccTypeInfo.statusId=0";
						}
						sql += conditions;
						sql += " ORDER BY tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccDate,tblAccMain.CredTypeId,tblAccMain.orderNo,tblAccDetail.AccCode";
						System.out.println("��ѯ��ϸ��"+sql);
						rset = st.executeQuery(sql);
						List accMainList = new ArrayList();
						while (rset.next()) {
							HashMap map=new HashMap();
                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                        		Object obj=rset.getObject(i);
                        		if(obj==null){
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                        				map.put(rset.getMetaData().getColumnName(i), 0);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), "");
                        			}
                        		}else{
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                    					String strvalue = String.valueOf(obj);
                    					if (strvalue.indexOf(".")>0){
                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                    					}
                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                    						strvalue = "";
                    					}
                    					map.put(rset.getMetaData().getColumnName(i), strvalue);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), obj);
                        			}
                        		}
                        	}
                        	accMainList.add(map);
						}
						
						/**
                         * ��ѯ���������Ļ���ڼ�
                         */
                        sql = "SELECT ap.AccYear,ap.AccPeriod,ap.periodEnd FROM tblAccPeriod ap WHERE ";
                        str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,ap.AccYear)+'-'+CONVERT(VARCHAR,ap.AccPeriod)+'-01',120),120)";
                        periodCondition = str+">='"+startTime+"' AND "+str+"<='"+endTime+"'";
						sql += periodCondition+" ORDER BY ap.AccYear,ap.AccPeriod";
						System.out.println("����ڼ䣺"+sql);
						rset=st.executeQuery(sql.toString());
						List periodList = new ArrayList();
						while (rset.next()) {
							HashMap map=new HashMap();
                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                        		Object obj=rset.getObject(i);
                        		if(obj==null){
                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                        				map.put(rset.getMetaData().getColumnName(i), 0);
                        			}else{
                        				map.put(rset.getMetaData().getColumnName(i), "");
                        			}
                        		}else{
                        			map.put(rset.getMetaData().getColumnName(i), obj);
                        		}
                        	}
                        	periodList.add(map);
						}
						
						Object[] obj = new Object[]{initList,curPeriodList,accMainList,periodList,startTime};
						result.setRetVal(obj);
					}
				});
				return result.getRetCode();
			}
		});
		
		Object[] obj = (Object[])result.retVal;
		
		//��������
		List initList = (ArrayList)obj[0];						//�ڳ����
		List curPeriodList = (ArrayList)obj[1];					//���ںϼ� �����ۼ�
		List accMainList = (ArrayList)obj[2];					//ƾ֤��¼����
		List periodList = (ArrayList)obj[3];					//����ڼ�
		String startTime = String.valueOf(obj[4]);
		
		String yearStart = conMap.get("periodYearStart");
		String yearEnd = conMap.get("periodYearEnd");
		String periodStart = conMap.get("periodStart");
		
		/**
		 * �����ڳ��������
		 */
		String accCode = "";
		HashMap values = new HashMap();
		values.put("AccDate", startTime+"-01");
		for(int i=0;i<initList.size();i++){
			String setname = "initData";
			values.put(setname, dealData(values, initList, i, isbaseCurr, currencys, acctypeMap,setname));
		}
		
		/**
		 * ���ںϼ�/�����ۼ����ݴ���
		 */
		for(int i=0;i<periodList.size();i++){
			HashMap periodMap = (HashMap)periodList.get(i);
			String yearPeriod = periodMap.get("AccYear").toString();				//����ڼ���
			String period = periodMap.get("AccPeriod").toString();					//����ڼ�
			//ѭ�����ںϼ�����
			for(int j=0;j<curPeriodList.size();j++){
				HashMap curPeriodMap = (HashMap)curPeriodList.get(j);
				String curYear = curPeriodMap.get("CredYear").toString();
				String curPeriod = curPeriodMap.get("Period").toString();
				if(yearPeriod.equals(curYear) && period.equals(curPeriod)){
					//���ڴ˱��ںϼ�
					String setname = "period_"+curYear+"_"+curPeriod;
					HashMap map = dealData(values, curPeriodList, j, isbaseCurr, currencys, acctypeMap, setname);
					HashMap curMap = (HashMap)curPeriodList.get(j);
					Iterator iter = acctypeMap.entrySet().iterator();
					String curr = "";
					if(curMap.get("Currency")!=null){
						curr = curMap.get("Currency").toString();
					}
					
					//�����õĿ�Ŀû�н������ʱ
					while(iter.hasNext()){
						Entry entry = (Entry)iter.next();
						String[] acctypeStr = (String[])entry.getValue();
						String[] sumStr4 = new String[]{"sumDebitAmount","sumDebitCurrencyAmount"};
						String[] sumStr5 = new String[]{"sumLendAmount","sumLendCurrencyAmount"};
						if("1".equals(acctypeStr[0])){
							//�跽
							if(map.get("curr_"+curr+"_"+entry.getKey()+"_"+sumStr4[0]) == null ){
								map.put("curr_"+curr+"_"+entry.getKey()+"_"+sumStr4[0],"");
							}
							if(map.get("curr_"+curr+"_"+entry.getKey()+"_"+sumStr4[1]) == null ){
								map.put("curr_"+curr+"_"+entry.getKey()+"_"+sumStr4[1],"");
							}
						}else if("2".equals(acctypeStr[0])){
							//����
							if(map.get("curr_"+curr+"_"+entry.getKey()+"_"+sumStr5[0]) == null ){
								map.put("curr_"+curr+"_"+entry.getKey()+"_"+sumStr5[0],"");
							}
							if(map.get("curr_"+curr+"_"+entry.getKey()+"_"+sumStr5[1]) == null ){
								map.put("curr_"+curr+"_"+entry.getKey()+"_"+sumStr5[1],"");
							}
						}
					}
					map.put("periodEnd", curPeriodMap.get("periodEnd"));
					values.put(setname, map);
				}
			}
			//���������������+��һ���������ڳ���
			boolean flag = false;
			Integer startPeriod = Integer.valueOf(period);
			HashMap preInit = null;
			for(int k=Integer.valueOf(yearPeriod);k>=Integer.valueOf(yearStart);k--){
				for(int l=startPeriod-1;l>0;l--){
					preInit = (HashMap)values.get("period_"+k+"_"+l);
					if(preInit!=null){
						flag = true;
						break;
					}
				}
				if(flag){
					break;
				}
				startPeriod = 13;
			}
			Object object = null;
			String money = "0";
			String newMoney = "0";                                //���ڽ跽-���������
			//���ڷ��������
			HashMap map = (HashMap)values.get("period_"+yearPeriod+"_"+period);
			if(map != null){
				object = map.get("totalBalance");
				if(object != null && !"".equals(object)){
					money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
					newMoney = money;
				}
				if(preInit!=null){
					//������һ�ڵĽ��
					object = preInit.get("totalBalance");				//��һ�����
					if(object != null && !"".equals(object)){
						money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
					}
				}else{
					//��������һ�ڽ��ʱ�����ȡ�ڳ����
					HashMap initMap = (HashMap)values.get("initData");
					if(initMap!=null){
						object = initMap.get("totalBalance");				//�ڳ����
						if(object != null && !"".equals(object)){
							money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
						}
					}
				}
				if(Double.valueOf(money)>0){
					map.put("totalBalanceisflag", "��");
				}else if(Double.valueOf(money)<0){
					map.put("totalBalanceisflag", "��");
				}else{
					map.put("totalBalanceisflag", "ƽ");
				}
				map.put("totalBalance", money);
				values.put("period_"+yearPeriod+"_"+period, map);
			}
			
			//�����ۼ� ����һ�ڼ�ı����ۼ�+���ڵı��ںϼƣ�
			HashMap yearMap = null;
			if(Integer.valueOf(period)>1){
				String periods = String.valueOf(Integer.valueOf(period)-1);
				yearMap = (HashMap)values.get("CredYear_"+yearPeriod+"_"+periods);				//��һ�ڼ�ı����ۼ�����
			}
			if(yearMap!=null){
				//������һ�ڵı����ۼ�
				HashMap hashmap = new HashMap();
				Iterator iterator = yearMap.entrySet().iterator();
				//ѭ����һ�ڵı����ۼ�����
				while (iterator.hasNext()) {
					Entry entry = (Entry)iterator.next();
					String key = String.valueOf(entry.getKey());
					//����ǽ������ʱ�ۼ�
					if(key.indexOf("sumDebitAmount")>0 || key.indexOf("sumLendAmount")>0 || key.indexOf("sumDebitCurrencyAmount")>0 ||
							key.indexOf("sumLendCurrencyAmount")>0 || key.indexOf("sumBalanceAmount")>0 || "totalBalance".equals(key)){
						money = "0";
						//�����ۼƵĽ��
						Object o = entry.getValue();
						if(o!=null && !"".equals(o)){
							money = o.toString();
						}
						//���ڽ��
						if(map != null){
							o = map.get(key);
							if(o != null && !"".equals(o)){
								if("totalBalance".equals(key)){
									money = new BigDecimal(money).add(new BigDecimal(newMoney)).toString();
								}else{
									money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
								}
							}
						}
						hashmap.put(key,money);
					}else{
						hashmap.put(key,entry.getValue());
					}
				}
				values.put("CredYear_"+yearPeriod+"_"+period, hashmap);
			}else{
				//��������һ�ڼ�ı����ۼ�ʱ�������ۼ�=���ںϼ�+�ڳ����
				HashMap hashmap = new HashMap();
				HashMap valueMap = (HashMap)values.get("period_"+yearPeriod+"_"+period);
				//�ڳ����
				HashMap initMap = (HashMap)values.get("initData");
				if(initMap!=null){
					if(valueMap != null){
						Iterator iter = valueMap.entrySet().iterator();
						while(iter.hasNext()){
							Entry entry = (Entry)iter.next();
							String key = String.valueOf(entry.getKey());
							if(key.indexOf("sumDebitAmount")>0 || key.indexOf("sumLendAmount")>0 || key.indexOf("sumDebitCurrencyAmount")>0 ||
									key.indexOf("sumLendCurrencyAmount")>0 || key.indexOf("sumBalanceAmount")>0){
								money = String.valueOf(entry.getValue());
								if("".equals(money)){
									money = "0";
								}
								object = initMap.get(entry.getKey());
								if(object != null && !"".equals(object)){
									money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
								}
								hashmap.put(key, money);
							}else{
								hashmap.put(key, entry.getValue());
							}
						}
					}else{
						//���ڳ����������ۼ�
						Iterator iter = initMap.entrySet().iterator();
						while(iter.hasNext()){
							Entry entry = (Entry)iter.next();
							String key = String.valueOf(entry.getKey());
							if(key.indexOf("sumDebitAmount")>0 || key.indexOf("sumLendAmount")>0 || key.indexOf("sumDebitCurrencyAmount")>0 ||
									key.indexOf("sumLendCurrencyAmount")>0 || key.indexOf("sumBalanceAmount")>0 || "totalBalance".equals(key)){
								money = String.valueOf(entry.getValue());
								if("".equals(money)){
									money = "0";
								}
								hashmap.put(key, money);
							}else{
								hashmap.put(key, entry.getValue());
							}
						}
					}
				}else{
					hashmap = map;
				}
				values.put("CredYear_"+yearPeriod+"_"+period, hashmap);
			}
		}
		
		/**
		 * ƾ֤��ϸ
		 */
		String showOperationBranch = conMap.get("showOperationBranch");
		String[] str1= new String[]{"DebitAmount","DebitCurrencyAmount"};
		String[] str2= new String[]{"LendAmount","LendCurrencyAmount"};
		for(int i=0;i<periodList.size();i++){
			HashMap periodMap = (HashMap)periodList.get(i);
			List list = new ArrayList();
			String id = "";
			String code = "";
			for(int j=0;j<accMainList.size();j++){
				HashMap accmain = (HashMap)accMainList.get(j);
				Integer accyear = Integer.valueOf(periodMap.get("AccYear").toString());
				Integer credyear = Integer.valueOf(accmain.get("CredYear").toString());
				Integer accperiod = Integer.valueOf(periodMap.get("AccPeriod").toString());
				Integer period = Integer.valueOf(accmain.get("Period").toString());
				if(accyear.equals(credyear) && accperiod.equals(period)){
					/* �ж��ڼ�����ڼ���ͬʱ�����ݼӵ�list�� */
					if(accmain.get("Currency").equals(isbaseCurr)){
						//�Ǳ�λ��ʱ
						accmain.put("Currency", "");
					}
					String curr = accmain.get("Currency").toString();
					String[] accStrs = (String[])acctypeMap.get(accmain.get("AccCode"));
					String money1 = "0";
					String money2 = "0";
					String money = "0";
					String keys = "";
					for(int k=0;k<str1.length;k++){
						money1 = "0";
						money2 = "0";
						if(accmain.get(str1[k])!=null && !"".equals(accmain.get(str1[k]))){
							money1 = accmain.get(str1[k]).toString();
						}
						if(accmain.get(str2[k])!=null && !"".equals(accmain.get(str2[k]))){
							money2 = accmain.get(str2[k]).toString();
						}
						money = "0";
						keys = "";
						if("1".equals(accStrs[0])){
							//�û�ƿ�Ŀ�ǽ跽
							money = new BigDecimal(money1).subtract(new BigDecimal(money2)).toString();
							//keys = str1[k]+"s";
							keys = "curr_"+curr+"_"+accmain.get("AccCode")+"_"+str1[k];
						}else if("2".equals(accStrs[0])){
							//��ƿ�Ŀ�Ǵ���
							money = new BigDecimal(money2).subtract(new BigDecimal(money1)).toString();
							//keys = str2[k]+"s";
							keys = "curr_"+curr+"_"+accmain.get("AccCode")+"_"+str2[k];
						}
						accmain.put(keys,money);
					}
					//accmain.put("sumAmount", accmain.get("sumAmount").toString());
					//ҵ���¼������ʾ
					if(id.equals(accmain.get("id")) && 
							(showOperationBranch==null || "".equals(showOperationBranch))){
						HashMap oldMap = (HashMap)list.get(list.size()-1);
						HashMap hashmap = new HashMap();
						Iterator iterator = oldMap.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry entry = (Entry)iterator.next();
							String key = String.valueOf(entry.getKey());
							//����ǽ������ʱ�ۼ�
							if(key.indexOf("Amount")>0){
								String m = "0";
								Object o = entry.getValue();
								if(o!=null && !"".equals(o)){
									m = o.toString();
								}
								Object periodvalue = accmain.get(key);
								if(periodvalue!=null && !"".equals(periodvalue)){
									m = new BigDecimal(m).add(new BigDecimal(periodvalue.toString())).toString();
								}
								hashmap.put(key,m);
							}else{
								hashmap.put(key,entry.getValue());
							}
						}
						
						/* ѭ������ */
						iterator = accmain.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry entry = (Entry)iterator.next();
							String key = String.valueOf(entry.getKey());
							//����ǽ������ʱ�ۼ�
							if(key.indexOf("curr_"+curr)>=0){
								String m = "0";
								Object o = entry.getValue();
								if(o!=null && !"".equals(o)){
									m = o.toString();
								}
								hashmap.put(key,m);
							}
						}
						
						if(hashmap.get("sumAmount")!=null && !"".equals(hashmap.get("sumAmount"))){
							if(Double.valueOf(hashmap.get("sumAmount").toString())>0){
								hashmap.put("isflag","��");
							}else if(Double.valueOf(hashmap.get("sumAmount").toString())<0){
								hashmap.put("isflag","��");
							}else{
								hashmap.put("isflag","ƽ");
							}
							hashmap.put("sumAmount", Double.valueOf(hashmap.get("sumAmount").toString()));
						}
						//accmain = hashmap;
						list.remove(list.size()-1);
						list.add(hashmap);
						continue;
					}
					
					String oldmoney = accmain.get("sumAmount").toString();
					//ÿһ������ϸ��Ҫ������һ����¼�����
					if(list.size()>0){
						//���ڼ�¼ʱ��ȡ��һ������
						HashMap oldMap = (HashMap)list.get(list.size()-1);
						//��ȡ���
						Object oldSumAmount = oldMap.get("sumAmount");
						if(oldSumAmount!= null && !"".equals(oldSumAmount)){
							oldmoney = new BigDecimal(oldmoney).add(new BigDecimal(oldSumAmount.toString())).toString();
						}
						accmain.put("sumAmount", oldmoney);
					}else{
						//����ǵ�һ����¼ʱ�����=�ü�¼�����+��һ�ڵı����ۼƽ�
						HashMap preInit = null;
						boolean flag = false;
						Integer startPeriod = accperiod;
						for(int k=accyear;k>=Integer.valueOf(yearStart);k--){
							for(int l=startPeriod-1;l>0;l--){
								preInit = (HashMap)values.get("CredYear_"+k+"_"+l);
								if(preInit!=null){
									flag = true;
									break;
								}
							}
							if(flag){
								break;
							}
							startPeriod = 13;
						}
						if(preInit!=null){
							//���ڼ�¼
							Object oldSumAmount = preInit.get("totalBalance");
							if(oldSumAmount!= null && !"".equals(oldSumAmount)){
								oldmoney = new BigDecimal(oldmoney).add(new BigDecimal(oldSumAmount.toString())).toString();
							}
							accmain.put("sumAmount", oldmoney);
						}else{
							//��������һ�ڵı����ۼƽ��ʱ��ȡ�ڳ����
							HashMap initMap = (HashMap)values.get("initData");
							if(initMap!= null && initMap.size()>0){
								Object oldSumAmount = initMap.get("totalBalance");
								if(oldSumAmount!= null && !"".equals(oldSumAmount)){
									oldmoney = new BigDecimal(oldmoney).add(new BigDecimal(oldSumAmount.toString())).toString();
								}
								accmain.put("sumAmount", oldmoney);
							}
						}
					}
					if(Double.valueOf(accmain.get("sumAmount").toString())>0){
						accmain.put("isflag","��");
					}else if(Double.valueOf(accmain.get("sumAmount").toString())<0){
						accmain.put("isflag","��");
					}else{
						accmain.put("isflag","ƽ");
					}

					id = String.valueOf(accmain.get("id"));
					code = String.valueOf(accmain.get("AccCode"));
					list.add(accmain);
				}
			}
			values.put("detail_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"), list);
		}
		
		/* �ڳ����� */
		HashMap initMap = (HashMap)values.get("initData");
		if(initMap!= null && initMap.size()>0){
			String[] strs = new String[]{"sumDebitCurrencyAmount","sumLendCurrencyAmount","totalsumLendAmount","sumLendAmount","sumDebitAmount","totalsumDebitAmount","totalBalance"};
			Iterator iter = initMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry entry = (Entry)iter.next();
				String keyStr = String.valueOf(entry.getKey());
				for(String s : strs){
					if(keyStr.indexOf(s)>0 || "totalBalance".equals(keyStr)){
						//����
						if("totalBalance".equals(s)){
							initMap.put(keyStr,dealDataDouble(String.valueOf(entry.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
						}else{
							initMap.put(keyStr,dealDataDouble(String.valueOf(entry.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}
					}
				}
			}
		}
		/* ���ںϼơ������ۼ����ݹ��� */
		for(int i=0;i<periodList.size();i++){
			HashMap periodMap = (HashMap)periodList.get(i);
			//����ϼƽ���
			String[] strName = new String[]{"CredYear_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"),"period_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod")};
			for(int k=0;k<strName.length;k++){
				HashMap hashmap = (HashMap)values.get(strName[k]);
				if(hashmap!=null && hashmap.size()>0){
					String[] strs = new String[]{"sumDebitCurrencyAmount","sumLendCurrencyAmount","totalsumLendAmount","sumLendAmount","sumDebitAmount","totalsumDebitAmount","totalBalance"};
					Iterator iter = hashmap.entrySet().iterator();
					while (iter.hasNext()) {
						Entry entry = (Entry)iter.next();
						String keyStr = String.valueOf(entry.getKey());
						for(String s : strs){
							if(keyStr.indexOf(s)>0 || "totalBalance".equals(keyStr)){
								//����
								if("totalBalance".equals(s)){
									if(!"".equals(entry.getValue())){
										if(Double.valueOf(entry.getValue().toString())>0){
											hashmap.put("totalBalanceisflag","��");
										}else if(Double.valueOf(entry.getValue().toString())<0){
											hashmap.put("totalBalanceisflag","��");
										}else{
											hashmap.put("totalBalanceisflag","ƽ");
										}
									}
									hashmap.put(keyStr,dealDataDouble(String.valueOf(entry.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
								}else{
									hashmap.put(keyStr,dealDataDouble(String.valueOf(entry.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
								}
							}
						}
					}
				}
			}
		}
		
		/* ��ϸ��Ϣ���ݴ��� */
		for(int i=0;i<periodList.size();i++){
			HashMap periodMap = (HashMap)periodList.get(i);
			List list = (ArrayList)values.get("detail_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
			String[] strs = new String[]{"DebitAmount","DebitAmounts","LendAmount","LendAmounts","sumAmount","DebitCurrencyAmount",
					"DebitCurrencyAmounts","LendCurrencyAmount","LendCurrencyAmounts"};
			for(int j=0;j<list.size();j++){
				HashMap detailMap = (HashMap)list.get(j);
				for(String valueStr : strs){
					if(detailMap.get(valueStr)!=null){
						//����
						if("sumAmount".equals(valueStr)){
							detailMap.put(valueStr,dealDataDouble(String.valueOf(detailMap.get(valueStr)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
						}else{
							detailMap.put(valueStr,dealDataDouble(String.valueOf(detailMap.get(valueStr)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}
					}
				}
			}
		}
		values.put("periodList", periodList);			//�ڼ�List
		values.put("isbaseCurr", isbaseCurr);
		values.put("currIsBase", currIsBase);
		result.setRetVal(values);
		result.setRetCode(retCode);
		return result;
	}
	
	
	/**
	 * ��װ���ݣ��ڳ������ںϼƣ������ۼƣ�
	 * @param values
	 * @param dataList
	 * @param i
	 * @param isbaseCurr
	 * @param currencys
	 * @param acctypeMap
	 * @return
	 */
	public HashMap dealData(HashMap values,List dataList, int i,String isbaseCurr,String currencys,HashMap acctypeMap,String setname){
		HashMap map = (HashMap)dataList.get(i);
		//���н�����sumDebitAmount sumLendAmount sumDebitCurrencyAmount sumLendCurrencyAmount
		HashMap valueMap = (HashMap)values.get(setname);				//�ڳ�����
		if(valueMap==null){
			valueMap = new HashMap();
		}
		String[] sumStr1 = new String[]{"totalsumDebitAmount","totalsumLendAmount","totalBalance"};
		String[] sumStr2 = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount"};
		String[] sumStr3 = new String[]{"sumDebitAmount","sumLendAmount","sumDebitCurrencyAmount","sumLendCurrencyAmount"};
		
		//ͳ�ƽ���ϼƱ�λ�ҽ��
		Object o = null;
		String money = "0";
		for(int j=0;j<sumStr1.length;j++){
			money = "0";
			
			//��ȡ������valueMap�оɵ�����
			o = valueMap.get(sumStr1[j].toString());
			if(o != null && !"".equals(o)){
				money = o.toString();
			}
			
			//��ȡ��ǰ�Ľ��
			o = map.get(sumStr2[j]);
			if(o != null && !"".equals(o)){
				money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
			}
			if(j==2){
				//������д���
				if(Double.valueOf(money)>0){
					valueMap.put("totalBalanceisflag","��");
				}else if(Double.valueOf(money)<0){
					valueMap.put("totalBalanceisflag","��");
				}else{
					valueMap.put("totalBalanceisflag","ƽ");
				}
			}
			if(i==dataList.size()-1){
				money = dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), "");
			}
			valueMap.put(sumStr1[j],money);
		}
		String curr = "";
		if(map.get("Currency")!=null){
			//�������
			curr = map.get("Currency").toString();
		}
		String acctype = map.get("AccCode").toString();
		for(int j=0;j<sumStr3.length;j++){
			money = "0";
			if(curr.equals(isbaseCurr)){
				//��λ��ʱ
				curr = "";
			}
			if(!"".equals(currencys)){
				//ͳ�ƽ�����ݱ��ֱַ�Ľ�� ���ұ�Ϊ��ʱ����ͳ�ƣ�
				o = valueMap.get("curr_"+curr+"_"+sumStr3[j]);
				if(o != null && !"".equals(o)){
					money = o.toString();
				}
				o = map.get(sumStr3[j]);
				if(o != null && !"".equals(o)){
					money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
				}
				valueMap.put("curr_"+curr+"_"+sumStr3[j],money);
			}
		}
		
		String[] sumStr4 = new String[]{"sumDebitAmount","sumDebitCurrencyAmount"};
		String[] sumStr5 = new String[]{"sumLendAmount","sumLendCurrencyAmount"};
		String[] accStrs = (String[])acctypeMap.get(acctype);
		
		//���ݻ�ƿ�Ŀ��ͬͳ�Ʋ�ͬ���
		if("1".equals(accStrs[0])){
			//�û�ƿ�Ŀ�ǽ跽
			for(int j=0;j<sumStr4.length;j++){
				money="0";
				o = valueMap.get("curr_"+curr+"_"+acctype+"_"+sumStr4[j]);
				if(o != null && !"".equals(o)){
					money = o.toString();
				}
				o = map.get(sumStr4[j]);
				if(o != null && !"".equals(o)){
					money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
				}
				o = map.get(sumStr5[j]);
				if(o !=null && !"".equals(o)){
					money = new BigDecimal(money).subtract(new BigDecimal(o.toString())).toString();
				}
				valueMap.put("curr_"+curr+"_"+acctype+"_"+sumStr4[j],money);
			}
		}else if("2".equals(accStrs[0])){
			//��ƿ�Ŀ�Ǵ���
			for(int j=0;j<sumStr5.length;j++){
				money="0";
				o = valueMap.get("curr_"+curr+"_"+acctype+"_"+sumStr5[j]);
				if(o != null && !"".equals(o)){
					money = o.toString();
				}
				o = map.get(sumStr5[j]);
				if(o != null && !"".equals(o)){
					money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
				}
				o = map.get(sumStr4[j]);
				if(o !=null && !"".equals(o)){
					money = new BigDecimal(money).subtract(new BigDecimal(o.toString())).toString();
				}
				valueMap.put("curr_"+curr+"_"+acctype+"_"+sumStr5[j],money);
			}
		}
		return valueMap;
	}
	
	/**
	 * ��ѯ������Ŀ����
	 * @param conMap
	 * @return
	 */
	public Result quertItemDate(final HashMap<String,String> conMap){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							/* ֻ��ʾ��ϸ��Ŀ */
							String itemSort = conMap.get("itemSort");						//��Ŀ���
							String itemCodeStart = conMap.get("itemCodeStart");				//��Ŀ���뿪ʼ
							String itemCodeEnd = conMap.get("itemCodeEnd");					//��Ŀ�������		
							String keyWord = conMap.get("keyWord");							//�ؼ�������
							String accTypeNo = conMap.get("accTypeNo");						//��Ŀ�޷������ʾ
							String yearStart = conMap.get("periodYearStart");
							String yearEnd = conMap.get("periodYearEnd");
							String periodStart = conMap.get("periodStart");
							String periodEnd = conMap.get("periodEnd");
							String accCode = conMap.get("accCode");							//������ϸ�˲�ѯʱ����ָ����ƿ�Ŀ
							String binderNo = conMap.get("binderNo");						//����δ����ƾ֤
							boolean flag = false;
							if(keyWord!=null && !"".equals(keyWord)){
								flag = true;
							}
							String startTime = "";
							String endTime = "";
							if(Integer.valueOf(periodStart)<10){
								startTime = yearStart+"-0"+periodStart;
							}else{
								startTime = yearStart+"-"+periodStart;
							}
							if(Integer.valueOf(periodEnd)<10){
								endTime = yearEnd+"-0"+periodEnd;
							}else{
								endTime = yearEnd+"-"+periodEnd;
							}
							String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccDetail.PeriodYear)+'-'+CONVERT(VARCHAR,tblAccDetail.PeriodMonth)+'-01',120),120)";
							//String condition = " AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'";
							
							String condition = " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') AND "+str+"<='"+endTime+"'";
							boolean itemStartFlag = false;
							boolean itemEndFlag = false;
							if(itemCodeStart!=null && !"".equals(itemCodeStart)){
								itemStartFlag = true;
							}
							if(itemCodeEnd!=null && !"".equals(itemCodeEnd)){
								itemEndFlag = true;
							}
							
							/*
							boolean takeFlag = false;
							if(accTypeNo!=null && !"".equals(accTypeNo)){
								takeFlag = true;
							}*/
							
							/* ��ƿ�Ŀ���� */							
							String accCodeSql = "";
							if(accCode!=null && !"".equals(accCode)){
								accCodeSql = " AND tblAccDetail.AccCode='"+accCode+"'";
							}
							StringBuffer sql = new StringBuffer();
							if("DepartmentCode".equals(itemSort)){
								sql.append("SELECT tblDepartment.classCode,tblDepartment.deptCode as AccNumber,tblDepartment.DeptFullName as AccName from tblDepartment ");
								if("1".equals(accTypeNo)){
									sql.append(" JOIN tblAccDetail ON tblAccDetail.DepartmentCode=tblDepartment.classCode");
									sql.append(" JOIN tblAccMain on tblAccMain.id = tblAccDetail.f_ref");
									sql.append(" JOIN tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
									sql.append(" AND (tblAccTypeInfo.IsDept=1 OR tblAccTypeInfo.isCalculate=1)");
								}
								//sql.append(" LEFT JOIN tblAccTypeInfo ON tblAccTypeInfo.");
								//if(binderNo == null || "".equals(binderNo)){
									//sql.append(" LEFT JOIN tblAccMain ON tblAccMain.id=tblAccDetail.f_ref WHERE tblAccMain.workFlowNodeName='finish'");
								//}else{
									sql.append(" WHERE 1=1");
								//}
								if("1".equals(accTypeNo)){
									sql.append(condition);
									sql.append(accCodeSql);
								}
								if(itemStartFlag){
									sql.append(" AND tblDepartment.classCode>=(select top 1 dept.classCode from tblDepartment dept where dept.deptCode='"+itemCodeStart+"' order by dept.classCode)");
								}
								if(itemEndFlag){
									sql.append(" AND tblDepartment.classCode<=(select top 1 dept.classCode from tblDepartment dept where dept.deptCode='"+itemCodeEnd+"' order by dept.classCode DESC)");
								}
								if(flag){
									sql.append(" AND (tblDepartment.deptCode like '%"+keyWord+"%' OR tblDepartment.DeptFullName like '%"+keyWord+"%')");
								}
								sql.append(" GROUP BY tblDepartment.classCode,tblDepartment.deptCode,tblDepartment.DeptFullName");
							}else if("EmployeeID".equals(itemSort)){
								sql.append("SELECT tblEmployee.id as classCode,tblEmployee.EmpNumber as AccNumber,tblEmployee.EmpFullName as AccName from tblEmployee ");
								if("1".equals(accTypeNo)){
									sql.append(" JOIN tblAccDetail ON tblAccDetail.EmployeeID=tblEmployee.id");
									sql.append(" JOIN tblAccMain on tblAccMain.id = tblAccDetail.f_ref");
									sql.append(" JOIN tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
									sql.append(" AND (tblAccTypeInfo.IsPersonal=1 OR tblAccTypeInfo.isCalculate=1)");
								}
								//if(binderNo == null || "".equals(binderNo)){
									//sql.append(" LEFT JOIN tblAccMain ON tblAccMain.id=tblAccDetail.f_ref WHERE tblAccMain.workFlowNodeName='finish'");
								//}else{
									sql.append(" WHERE 1=1");
								//}
								if("1".equals(accTypeNo)){
									sql.append(condition);
									sql.append(accCodeSql);
								}
								if(itemStartFlag){
									sql.append(" AND tblEmployee.id>=(select top 1 emp.id from tblEmployee emp where emp.EmpNumber='"+itemCodeStart+"' order by tblEmployee.id)");
								}
								if(itemEndFlag){
									sql.append(" AND tblEmployee.id<=(select top 1 emp.id from tblEmployee emp where emp.EmpNumber='"+itemCodeEnd+"' order by tblEmployee.id DESC)");
								}
								if(flag){
									sql.append(" AND (tblDepartment.EmpNumber like '%"+keyWord+"%' OR tblDepartment.EmpFullName like '%"+keyWord+"%')");
								}
								sql.append(" GROUP BY tblEmployee.id,tblEmployee.EmpNumber,tblEmployee.EmpFullName");
							}else if("ClientCode".equals(itemSort)){
								sql.append("SELECT tblCompany.classCode,tblCompany.ComNumber as AccNumber,tblCompany.ComFullName as AccName FROM tblCompany");
								if("1".equals(accTypeNo)){
									sql.append(" JOIN tblAccDetail ON tblAccDetail.CompanyCode=tblCompany.classCode");
									sql.append(" JOIN tblAccMain on tblAccMain.id = tblAccDetail.f_ref");
									sql.append(" JOIN tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
									sql.append(" AND (tblAccTypeInfo.IsClient=1 OR tblAccTypeInfo.isCalculate=1)");
								}
								//if(binderNo == null || "".equals(binderNo)){
								//	sql.append(" LEFT JOIN tblAccMain ON tblAccMain.id=tblAccDetail.f_ref WHERE tblCompany.statusId!='-1' AND tblAccMain.workFlowNodeName='finish'");
								//}else{
									sql.append(" WHERE (tblCompany.ClientFlag=2 or tblCompany.ClientFlag=3) AND tblCompany.statusId!='-1'");
								//}
								if("1".equals(accTypeNo)){
									sql.append(condition);
									sql.append(accCodeSql);
								}
								if(itemStartFlag){
									sql.append(" AND tblCompany.classCode>=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+itemCodeStart+"' order by com.classCode)");
								}
								if(itemEndFlag){
									sql.append(" AND tblCompany.classCode<=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+itemCodeEnd+"' order by com.classCode DESC)");
								}
								if(flag){
									sql.append(" AND (tblCompany.ComNumber like '%"+keyWord+"%' OR tblCompany.ComFullName like '%"+keyWord+"%')");
								}
								sql.append(" GROUP BY tblCompany.classCode,tblCompany.ComNumber,tblCompany.ComFullName");
							}else if("SuplierCode".equals(itemSort)){
								sql.append("SELECT tblCompany.classCode,tblCompany.ComNumber as AccNumber,tblCompany.ComFullName as AccName FROM tblCompany ");
								if("1".equals(accTypeNo)){
									sql.append(" JOIN tblAccDetail ON tblAccDetail.CompanyCode=tblCompany.classCode");
									sql.append(" JOIN tblAccMain on tblAccMain.id = tblAccDetail.f_ref");
									sql.append(" JOIN tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
									sql.append(" AND (tblAccTypeInfo.IsProvider=1 OR tblAccTypeInfo.isCalculate=1)");																	
								}
								//if(binderNo == null || "".equals(binderNo)){
								//	sql.append(" LEFT JOIN tblAccMain ON tblAccMain.id=tblAccDetail.f_ref WHERE tblAccMain.workFlowNodeName='finish' AND (tblCompany.ClientFlag=1 or tblCompany.ClientFlag=3) and tblCompany.statusId!='-1'");
								//}else{
									sql.append(" WHERE (tblCompany.ClientFlag=1 or tblCompany.ClientFlag=3) and tblCompany.statusId!='-1'");
								//}
								if("1".equals(accTypeNo)){
									sql.append(condition);
									sql.append(accCodeSql);
								}
								if(itemStartFlag){
									sql.append(" AND tblCompany.classCode>=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+itemCodeStart+"' order by com.classCode)");
								}
								if(itemEndFlag){
									sql.append(" AND tblCompany.classCode<=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+itemCodeEnd+"' order by com.classCode DESC)");
								}
								if(flag){
									sql.append(" AND (tblCompany.ComNumber like '%"+keyWord+"%' OR tblCompany.ComFullName like '%"+keyWord+"%')");
								}
								sql.append(" GROUP BY tblCompany.classCode,tblCompany.ComNumber,tblCompany.ComFullName");
							}else if("StockCode".equals(itemSort)){
								sql.append("SELECT tblStock.classCode,tblStock.StockNumber as AccNumber,tblStock.StockFullName as AccName FROM tblStock ");
								if("1".equals(accTypeNo)){
									sql.append(" JOIN tblAccDetail ON tblAccDetail.StockCode=tblStock.classCode");
									sql.append(" JOIN tblAccMain on tblAccMain.id = tblAccDetail.f_ref");
									sql.append(" JOIN tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
									sql.append(" AND (tblAccTypeInfo.isStock=1 OR tblAccTypeInfo.isCalculate=1)");
								}
								//if(binderNo == null || "".equals(binderNo)){
								//	sql.append(" LEFT JOIN tblAccMain ON tblAccMain.id=tblAccDetail.f_ref WHERE tblAccMain.workFlowNodeName='finish' ");
								//}else{
									sql.append(" WHERE tblStock.statusid!='-1'");
								//}
								if("1".equals(accTypeNo)){
									sql.append(condition);
									sql.append(accCodeSql);
								}
								if(itemStartFlag){
									sql.append(" AND tblStock.classCode>=(select top 1 stock.classCode from tblStock stock where stock.stockNumber='"+itemCodeStart+"' order by stock.classCode)");
								}
								if(itemEndFlag){
									sql.append(" AND tblStock.classCode<=(select top 1 stock.classCode from tblStock stock where stock.stockNumber='"+itemCodeEnd+"' order by stock.classCode DESC)");
								}
								if(flag){
									sql.append(" AND (tblStock.StockNumber like '%"+keyWord+"%' OR tblStock.StockFullName like '%"+keyWord+"%')");
								}
								sql.append(" GROUP BY tblStock.classCode,tblStock.StockNumber,tblStock.StockFullName");
							}else if("ProjectCode".equals(itemSort)){
								sql.append("SELECT tblProject.id as classCode,tblProject.ProjectNo as AccNumber,tblLanguage.zh_cn as AccName from tblProject LEFT JOIN tblLanguage ON tblLanguage.id=tblProject.ProjectName");
								if("1".equals(accTypeNo)){
									sql.append(" JOIN tblAccDetail ON tblAccDetail.ProjectCode=tblProject.id");
									sql.append(" JOIN tblAccMain on tblAccMain.id = tblAccDetail.f_ref");
									sql.append(" JOIN tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
									sql.append(" AND (tblAccTypeInfo.IsProject=1 OR tblAccTypeInfo.isCalculate=1)");
								}
								//if(binderNo == null || "".equals(binderNo)){
									//sql.append(" LEFT JOIN tblAccMain ON tblAccMain.id=tblAccDetail.f_ref WHERE tblAccMain.workFlowNodeName='finish'");
								//}else{
									sql.append(" WHERE 1=1");
								//}
								if("1".equals(accTypeNo)){
									sql.append(condition);
									sql.append(accCodeSql);
								}
								if(itemStartFlag){
									sql.append(" AND tblProject.id>=(select top 1 p.id from tblProject p where p.ProjectNo='"+itemCodeStart+"' order by tblProject.id)");
								}
								if(itemEndFlag){
									sql.append(" AND tblProject.id<=(select top 1 p.id from tblProject p where p.ProjectNo='"+itemCodeEnd+"' order by tblProject.id DESC)");
								}
								if(flag){
									sql.append(" AND (tblProject.ProjectNo like '%"+keyWord+"%' OR tblLanguage.zh_cn like '%"+keyWord+"%')");
								}
								sql.append(" GROUP BY tblProject.id,tblProject.ProjectNo,tblLanguage.zh_cn");
							}
							System.out.println("������Ŀ�б�SQL��"+sql.toString());
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							List<HashMap> list = new ArrayList<HashMap>();
							while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
                            }
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt quertItemDate:",ex) ;
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
	 * ������Ŀ������������
	 * 1.��ѯ���������Ļ�ƿ�Ŀ
	 * 2.��ѯ��������
	 * 4.��ѯ���������Ļ���ڼ�
	 * 4.�������
	 * @param conMap
	 * @param classCode
	 * @return
	 */
	public Result queryAccCalculateDetail(final HashMap<String,String> conMap,final String classCode,
			final MOperation mop,final LoginBean loginBean){
		final Result result = new Result();
		
		//�Աұ���д���
		String currencyNames = conMap.get("currencyName");; 									//�ұ�('isBase'=��λ�ң�''=�ۺϱ�λ�ң�'all'=���бұ�'��ҵ�id'=���)
		Result rs = queryIsBase(currencyNames);
		currencyNames = rs.retVal.toString();
		
		//�������
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyNames;
		final String searchCurrency = conMap.get("currencyName");
		final String yearStart = conMap.get("periodYearStart");
		final String yearEnd = conMap.get("periodYearEnd");
		final String periodStart = conMap.get("periodStart");
		final String periodEnd = conMap.get("periodEnd");
		final String accCodeLevel = conMap.get("accCodeLevel");       				//��Ŀ����
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							String binderNo = conMap.get("binderNo");								//����δ����ƾ֤
							String accCodeStart = conMap.get("accCodeStart");						//��ƿ�Ŀ��ʼ
							String accCodeEnd = conMap.get("accCodeEnd");							//��ƿ�Ŀ����
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
							String itemSort = conMap.get("itemSort"); 								//��Ŀ���
							
							String startTime = "";
							String endTime = "";
							if(Integer.valueOf(periodStart)<10){
								startTime = yearStart+"-0"+periodStart;
							}else{
								startTime = yearStart+"-"+periodStart;
							}
							if(Integer.valueOf(periodEnd)<10){
								endTime = yearEnd+"-0"+periodEnd;
							}else{
								endTime = yearEnd+"-"+periodEnd;
							}
							/**
							 * ��ѯ����
							 */
							String condition = scopeSql(mop, loginBean);
							if(accCodeStart != null && !"".equals(accCodeStart)){
								//��Ŀ��ʼ
								condition += " AND (tblAccTypeInfo.AccNumber>='"+accCodeStart+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+accCodeStart+"')+'%')";
							}
							if(accCodeEnd != null && !"".equals(accCodeEnd)){
								//��Ŀ����
								condition += " AND (tblAccTypeInfo.AccNumber<='"+accCodeEnd+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+accCodeEnd+"')+'%')";
							}
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//����ʾ���ÿ�Ŀ
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
							String sql1 = "",sql2 = "";
							if(binderNo == null || "".equals(binderNo)){
	            				//������δ����ƾ֤
								sql1 += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
	            			}
							if("DepartmentCode".equals(itemSort)){
								//�������
								sql1 += " AND tblAccTypeInfo.DepartmentCode like '"+classCode+"%'";
								
								sql2 += " AND tblAccTypeInfo.DepartmentCode like '"+classCode+"%'";
							}else if("EmployeeID".equals(itemSort)){
								//���ְԱ
								sql1 += " AND tblAccTypeInfo.EmployeeID='"+classCode+"'";
								
								sql2 += " AND tblAccTypeInfo.EmployeeID='"+classCode+"'";
							}else if("StockCode".equals(itemSort)){
								//����ֿ�
								sql1 += " AND tblAccTypeInfo.StockCode like '"+classCode+"%'";
							
								sql2 += " AND tblAccTypeInfo.StockCode like '"+classCode+"%'";
							}else if("ClientCode".equals(itemSort)){
								//����ͻ�
								sql1 += " AND tblAccTypeInfo.ClientCode like '"+classCode+"%' AND (tblCompany.ClientFlag=2 or tblCompany.ClientFlag=3) AND tblCompany.statusId!='-1'";
								
								sql2 += " AND tblAccTypeInfo.ClientCode like '"+classCode+"%' ";
							}else if("SuplierCode".equals(itemSort)){
								//�����Ӧ��
								sql1 += " AND tblAccTypeInfo.SuplierCode like '"+classCode+"%' AND (tblCompany.ClientFlag=1 or tblCompany.ClientFlag=3) AND tblCompany.statusId!='-1'";
								
								sql2 += " AND tblAccTypeInfo.SuplierCode like '"+classCode+"%' ";
							}else if("ProjectCode".equals(itemSort)){
								//�����Ŀ
								sql1 += " AND tblAccTypeInfo.ProjectCode like '"+classCode+"%'";
								
								sql2 += " AND tblAccTypeInfo.ProjectCode like '"+classCode+"%'";
							}
                            
							if(!"".equals(currencyValue)){
								if("isBase".equals(currencyValue)){
									//��λ��
									sql1 += " AND (tblAccDetail.Currency='' OR tblAccDetail.Currency IS NULL OR tblAccDetail.Currency='"+searchCurrency+"') ";
								}else{
									//��������
									sql1 += " AND tblAccDetail.Currency='"+searchCurrency+"' ";
								}
								
							}
							HashMap accDateMap = new HashMap();
							/**
							 * ��ѯ���������Ļ�ƿ�Ŀ
							 */
							StringBuffer sql = new StringBuffer("SELECT acctype.classCode,acctype.AccNumber,ISNULL(acctype.isCalculate,'') AS isCalculate,l.zh_cn AS AccFullName FROM tblAccTypeInfo acctype join (SELECT tblAccTypeInfo.classCode");
							sql.append(" FROM tblAccDetail JOIN tblAccTypeInfo on tblAccDetail.AccCode=tblAccTypeInfo.AccNumber ");
							sql.append(" JOIN tblAccMain ON tblAccMain.id=tblAccDetail.f_ref LEFT JOIN tblCompany ON tblCompany.classCode=tblAccDetail.CompanyCode WHERE 1=1");
							sql.append(condition);
							sql.append(sql1);
							sql.append(" AND tblAccTypeInfo.isCalculate=1 GROUP BY tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode");
							sql.append(" ) AS a ON a.classCode like acctype.classCode+'%' JOIN tblLanguage l ON l.id=acctype.AccFullName GROUP BY acctype.AccNumber,acctype.classCode,l.zh_cn,acctype.isCalculate");
							sql.append(" ORDER BY acctype.classCode");
							System.out.println("��ƿ�ĿSQL��"+sql.toString());
							rset=st.executeQuery(sql.toString());
                            List accTypeInfoList = new ArrayList();
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rset.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	accTypeInfoList.add(map);
                            	accDateMap.put(map.get("classCode")+"_isCalculate", map.get("isCalculate"));
                            	accDateMap.put(map.get("classCode")+"_AccNumber", map.get("AccNumber"));
                            }
							
							/**
							 * ��ѯ�ڳ����
							 */							
                            sql = new StringBuffer("SELECT tblAccDetail.AccCode,tblAccTypeInfo.classCode,ISNULL(tblAccTypeInfo.isCalculate,'') AS isCalculate,");
							sql.append("SUM(tblAccDetail.DebitAmount) AS sumInitDebitAmount,SUM(tblAccDetail.DebitCurrencyAmount) AS sumInitDebitCurrencyAmount,");
							sql.append("SUM(tblAccDetail.LendAmount) AS sumInitLendAmount,SUM(tblAccDetail.LendCurrencyAmount) AS sumInitLendCurrencyAmount,");
							sql.append("(SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount)) AS sumBalanceAmount,");
							sql.append("(SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount)) AS sumBalanceCurrencyAmount ");
							sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo");
							sql.append(" ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode LEFT JOIN tblCompany ON tblCompany.classCode=tblAccDetail.CompanyCode WHERE ");
							sql.append(" tblAccMain.CredYear<="+yearStart+" AND tblAccMain.Period<"+periodStart);
							sql.append(condition);
							sql.append(sql1);
							sql.append(" GROUP BY tblAccDetail.AccCode,tblAccTypeInfo.classCode,tblAccTypeInfo.isCalculate ");
							//sql.append("ORDER BY tblAccDetail.AccCode");
							
                            //**��ѯ�ڳ������������ڳ���
                            //*****�����ڳ�*******//						
							StringBuffer iniSql = new StringBuffer("select tblAccTypeInfo.AccNumber as AccCode,tblAccTypeInfo.classCode,ISNULL(tblAccTypeInfo.isCalculate,'') AS isCalculate,");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 1 then tblAccBalance.CurrYIniBalaBase else 0 end) as sumInitDebitAmount, ");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 1 then tblAccBalance.CurrYIniBalaBase else 0 end) as sumInitDebitCurrencyAmount,");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 2 then tblAccBalance.CurrYIniBalaBase else 0 end) as sumInitLendAmount, ");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 2 then tblAccBalance.CurrYIniBalaBase else 0 end) as sumInitLendCurrencyAmount, ");
							iniSql.append("(case when tblAccTypeInfo.JdFlag = 1 then tblAccBalance.CurrYIniBalaBase else -tblAccBalance.CurrYIniBalaBase end) as sumBalanceAmount,");
							iniSql.append("(case when tblAccTypeInfo.JdFlag = 1 then tblAccBalance.CurrYIniBalaBase else -tblAccBalance.CurrYIniBalaBase end) as sumBalanceCurrencyAmount");
							iniSql.append(" from tblAccTypeInfo join tblAccBalance on tblAccTypeInfo.AccNumber=tblAccBalance.SubCode ");
							iniSql.append(" where 1 = 1 and tblAccTypeInfo.statusId=0 AND tblAccBalance.Nmonth =-1 and tblAccBalance.Nyear=-1 ");
							iniSql.append(condition);																																																																			
							iniSql.append(sql2);
							//*******end*******//                                                        
                           
							StringBuffer execSql = new StringBuffer("select AccCode, classCode,isCalculate,");
							execSql.append("sum(sumInitDebitAmount) as sumInitDebitAmount,sum(sumInitDebitCurrencyAmount) as sumInitDebitCurrencyAmount,");
							execSql.append("sum(sumInitLendAmount) as sumInitLendAmount,sum(sumInitLendCurrencyAmount) as sumInitLendCurrencyAmount,");
							execSql.append("sum(sumBalanceAmount) as sumBalanceAmount,sum(sumBalanceCurrencyAmount) as sumBalanceCurrencyAmount ");
							execSql.append(" from ("+iniSql+" union "+sql+")t group by AccCode,classCode,isCalculate order by AccCode");
                            
							System.out.println("��ѯ�ڳ���"+execSql);
							rset = st.executeQuery(execSql.toString());
							HashMap initMap = new HashMap();
							while (rset.next()) {
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				map.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					if("sumBalanceAmount".equals(rset.getMetaData().getColumnCount())){
                        						if(!"".equals(obj)){
                        							Double discrepancyAmount = Double.valueOf(obj.toString());
                        							if(discrepancyAmount>0){
                        								map.put("isInitflag", "��");
                        							}else if(discrepancyAmount<0){
                        								map.put("isInitflag", "��");
                        							}else{
                        								map.put("isInitflag", "ƽ");
                        							}
                        						}else{
                        							map.put("isInitflag", "ƽ");
                        						}
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                        					
                        					String classCode = map.get("classCode").toString();
                        					/* ���ϼ�������һЩ���� */
                        					if(classCode.length()>5){
                        						for(int j=0;j<(classCode.length()-5)/5;j++){
                        							HashMap oldMap = (HashMap)initMap.get(classCode.substring(0,5+j*5)+"_"+yearStart+"_"+periodStart);
                        							if(oldMap!=null && oldMap.size()>0){
                        								//������һ����Ŀ���ʱ
                        								Object oldColumn = oldMap.get(rset.getMetaData().getColumnName(i));
                        								String totalAmount = String.valueOf(toDouble(strvalue));
                        								if(oldColumn!=null && !"".equals(oldColumn)){
                        									totalAmount = new BigDecimal(totalAmount).add(new BigDecimal(toDouble(oldColumn.toString()))).toString();
                        								}
                        								if(Double.valueOf(totalAmount)==0){
                        									totalAmount = "";
                        								}
                        								oldMap.put(rset.getMetaData().getColumnName(i),totalAmount);
                        							}else{
                        								oldMap = new HashMap();
                        								Object code = accDateMap.get(classCode.substring(0,5+j*5)+"_AccNumber");
                        								oldMap.put("AccCode", code);
                        								oldMap.put("classCode", classCode.substring(0,5+j*5));
                        								oldMap.put(rset.getMetaData().getColumnName(i), toDouble(strvalue));
                        							}
                        							initMap.put(classCode.substring(0,5+j*5)+"_"+yearStart+"_"+periodStart, oldMap);
                        						}
                        					}
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
	                        		}
	                        	}
	                        	initMap.put(map.get("classCode")+"_"+yearStart+"_"+periodStart, map);
							}
							
							/**
							 * ���ڽ�������
							 */
                            sql = new StringBuffer("SELECT tblAccDetail.AccCode,tblAccTypeInfo.classCode,tblAccMain.CredYear,tblAccMain.Period,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,");
                            sql.append("SUM(tblAccDetail.DebitAmount) as sumDebitAmount,SUM(tblAccDetail.DebitCurrencyAmount) as sumDebitCurrencyAmount,");
                            sql.append("SUM(tblAccDetail.LendAmount) as sumLendAmount,SUM(tblAccDetail.LendCurrencyAmount) as sumLendCurrencyAmount,");
                            sql.append("SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount) AS sumDCBala,");
                            sql.append("SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount) AS sumDCBalaCurrency");
                            sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id ");
                            sql.append("LEFT JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode ");
                            sql.append(" LEFT JOIN tblCompany ON tblCompany.classCode=tblAccDetail.CompanyCode WHERE 1=1");
                            condition += " AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'";
                            sql.append(condition);
                            sql.append(sql1);
                            sql.append(" AND tblAccTypeInfo.isCalculate=1 GROUP BY tblAccDetail.AccCode,tblAccMain.CredYear,tblAccMain.Period,tblAccTypeInfo.classCode,tblAccTypeInfo.isCalculate ");
                            sql.append(" ORDER BY tblAccMain.CredYear,tblAccMain.period,tblAccDetail.AccCode");
                            System.out.println("���ڽ��sql:"+sql);
                            rset=st.executeQuery(sql.toString());
                            LinkedHashMap accBalanceMap = new LinkedHashMap();
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                        					String classCode = map.get("classCode").toString();
                        					/* ���ϼ�������һЩ���� */
                        					Integer Nyear = Integer.valueOf(map.get("CredYear").toString());
                        					Integer Period = Integer.valueOf(map.get("Period").toString());
                        					if(classCode.length()>5){
                        						for(int j=0;j<(classCode.length()-5)/5;j++){
                        							Object isCalculate = accDateMap.get(classCode.substring(0,5+j*5)+"_isCalculate");
                        							HashMap oldMap = (HashMap)accBalanceMap.get(classCode.substring(0,5+j*5)+"_"+map.get("CredYear")+"_"+map.get("Period"));
                        							if(oldMap!=null && oldMap.size()>0){
                        								//������һ����Ŀ���ʱ
                        								Object oldColumn = oldMap.get(rset.getMetaData().getColumnName(i));
                        								String totalAmount = String.valueOf(toDouble(strvalue));
                        								if(oldColumn!=null && !"".equals(oldColumn)){
                        									totalAmount = new BigDecimal(totalAmount).add(new BigDecimal(toDouble(oldColumn.toString()))).toString();
                        								}
                        								if(Double.valueOf(totalAmount)==0){
                        									totalAmount = "";
                        								}
                        								oldMap.put(rset.getMetaData().getColumnName(i),totalAmount);
                        							}else{
                        								oldMap = new HashMap();
                        								Object code = accDateMap.get(classCode.substring(0,5+j*5)+"_AccNumber");
                        								oldMap.put("AccCode", code);
                        								oldMap.put("CredYear", map.get("CredYear"));
                        								oldMap.put("Period", map.get("Period"));
                        								oldMap.put("classCode", classCode.substring(0,5+j*5));
                        								oldMap.put(rset.getMetaData().getColumnName(i), toDouble(strvalue));
                        							}
                        							accBalanceMap.put(classCode.substring(0,5+j*5)+"_"+map.get("CredYear")+"_"+map.get("Period"), oldMap);
                        						}
                        					}
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	accBalanceMap.put(map.get("classCode")+"_"+map.get("CredYear")+"_"+map.get("Period"), map);
                            }
                            
                            /**
                             * ��ѯ���������Ļ���ڼ�
                             */
                            sql = new StringBuffer("SELECT ap.AccYear,ap.AccPeriod FROM tblAccPeriod ap WHERE 1=1 ");
                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,ap.AccYear)+'-'+CONVERT(VARCHAR,ap.AccPeriod)+'-01',120),120)";
							sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
							sql.append(" ORDER BY ap.AccYear,ap.AccPeriod");
							rset=st.executeQuery(sql.toString());
							List periodList = new ArrayList();
							while (rset.next()) {
								HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rset.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	periodList.add(map);
							}
							
							//�������� accTypeInfoList ��ƿ�Ŀ��initMap �ڳ����,accBalanceMap ���ڽ������periodList ����ڼ�
							Object[] obj = new Object[]{accTypeInfoList,initMap,accBalanceMap,periodList};
							result.setRetVal(obj);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryAccCalculateDetail:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		
		/* ȡ���� */
		Object[] obj = (Object[])result.getRetVal();
		List accTypeInfoList = (ArrayList)obj[0];																//��ƿ�Ŀ
		HashMap initMap = (HashMap)obj[1];																		//�ڳ����
		LinkedHashMap accBalanceMap = (LinkedHashMap)obj[2];													//���ڽ��
		List periodList = (ArrayList)obj[3];																	//����ڼ�
		
		
		/**
		 * ��������
		 */
		HashMap valuesMap = new HashMap();																		//������������
		//ѭ����ƿ�Ŀ
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			
			String accClassCode = String.valueOf(map.get("classCode"));											//��ƿ�ĿclassCode
			//ѭ������ڼ�
			for(int j = 0;j<periodList.size();j++){
				HashMap periodMap = (HashMap)periodList.get(j);
				Integer credYear = Integer.valueOf(periodMap.get("AccYear").toString());						//����ڼ���
				Integer period = Integer.valueOf(periodMap.get("AccPeriod").toString());						//����ڼ�
				
				//�õ����ڼ�Ľ�������
				HashMap accMap = (HashMap)accBalanceMap.get(accClassCode+"_"+credYear+"_"+period);
				if(accMap == null ){
					//������ڼ䱾���ڱ��ڽ���¼ʱ�������µ�HashMap;
					accMap = new HashMap();
					accMap.put("CredYear", credYear);
					accMap.put("Period", period);
					accMap.put("classCode", accClassCode);
					accMap.put("AccCode", map.get("AccNumber"));
				}
				
				//�õ��ڳ����
				HashMap init = (HashMap)initMap.get(accClassCode+"_"+credYear+"_"+period);
				Integer yearStarts = Integer.valueOf(conMap.get("periodYearStart").toString());					//��ѯ���ڼ��ʼ����
				if(init != null){
					/* ����ѯ�Ļ���ڼ����ڳ�ʱ�����ڼ���ڳ������ڲ�ѯ���ڳ���� */
					accMap.put("initCurrencyAmount", init.get("sumBalanceCurrencyAmount"));						//�ڳ�ԭ�ҽ��
					accMap.put("initAmount", init.get("sumBalanceAmount"));										//�ڳ����
					accMap.put("sumInitDebitAmount", init.get("sumInitDebitAmount"));							//�ڳ��跽���
					accMap.put("sumInitLendAmount", init.get("sumInitLendAmount"));								//�ڳ��������
				}else{
					//�������ڳ����ʱ���ڳ����=��һ�ڵ���ĩ���
					HashMap preInit = null;																		//��һ�ڵĽ������
					Integer startPeriod = period;
					boolean flag = false;
					for(int k=credYear;k>=yearStarts;k--){
						for(int l=startPeriod-1;l>0;l--){
							preInit = (HashMap)valuesMap.get(accClassCode+"_"+k+"_"+l);
							if(preInit!=null){
								flag = true;
								break;
							}
						}
						if(flag){
							break;
						}
						startPeriod = 13;
					}
					if(preInit!=null){
						//������һ�ڵ�����
						accMap.put("initAmount", preInit.get("PeriodBalaBase"));
						accMap.put("initCurrencyAmount", preInit.get("PeriodBala"));
					}
				}
				
				//�õ���ĩ���ڳ�+�跽���-������
				String[] initAmountStr = new String[]{"initAmount","initCurrencyAmount"};
				String[] initAmountStrCurr = new String[]{"sumDCBala","sumDCBalaCurrency"};
				String[] periodStr = new String[]{"PeriodBalaBase","PeriodBala"};
				for(int k =0;k<initAmountStr.length;k++){
					Object initAmount = accMap.get(initAmountStr[k]);
					String periodEnds = "0";
					if(accMap.get(initAmountStrCurr[k]) != null && !"".equals(accMap.get(initAmountStrCurr[k]))){
						//�������
						periodEnds = accMap.get(initAmountStrCurr[k]).toString();
					}
					if(initAmount!=null && !"".equals(initAmount)){
						//�ڳ����+�������=��ĩ���
						periodEnds = new BigDecimal(initAmount.toString()).add(new BigDecimal(periodEnds)).toString();
					}
					accMap.put(periodStr[k], periodEnds);														//������ĩ���
				}
				
				//�õ��������������ڽ��+��һ�ڱ����
				String debit = "0";
				String lend = "0";
				String debitCurrency = "0";
				String lendCurrency = "0";
				if(accMap.get("sumDebitAmount") != null && !"".equals(accMap.get("sumDebitAmount"))){
					debit = accMap.get("sumDebitAmount").toString();
				}
				if(accMap.get("sumLendAmount") != null && !"".equals(accMap.get("sumLendAmount"))){
					lend = accMap.get("sumLendAmount").toString();
				}
				if(accMap.get("sumDebitCurrencyAmount") != null && !"".equals(accMap.get("sumDebitCurrencyAmount"))){
					debitCurrency = accMap.get("sumDebitCurrencyAmount").toString();
				}
				if(accMap.get("sumLendCurrencyAmount") != null && !"".equals(accMap.get("sumLendCurrencyAmount"))){
					lendCurrency = accMap.get("sumLendCurrencyAmount").toString();
				}
				Integer startPeriod = period;
				if(startPeriod>1){
					HashMap oldMap = null;
					for(int k=startPeriod-1;k>0;k--){
						oldMap = (HashMap)valuesMap.get(accClassCode+"_"+credYear+"_"+k);				//��һ�ڼ�ı����ۼ�����
						if(oldMap!=null){
							break;
						}
					}
					if(oldMap!=null){
						Object yearDebit = oldMap.get("sumYearDebitAmount");
						if(yearDebit!=null && !"".equals(yearDebit)){
							debit = new BigDecimal(debit).add(new BigDecimal(yearDebit.toString())).toString();
						}
						Object yearLend = oldMap.get("sumYearLendAmount");
						if(yearLend!=null && !"".equals(yearLend)){
							lend = new BigDecimal(lend).add(new BigDecimal(yearLend.toString())).toString();
						}
						Object yearDebitCurrency = oldMap.get("sumYearDebitCurrencyAmount");
						if(yearDebitCurrency!=null && !"".equals(yearDebitCurrency)){
							debitCurrency = new BigDecimal(debitCurrency).add(new BigDecimal(yearDebitCurrency.toString())).toString();
						}
						Object yearLendCurrency = oldMap.get("sumYearLendCurrencyAmount");
						if(yearLendCurrency!=null && !"".equals(yearLendCurrency)){
							lendCurrency = new BigDecimal(lendCurrency).add(new BigDecimal(yearLendCurrency.toString())).toString();
						}
					}else{
						if(init != null){
							Object yearDebit = init.get("sumInitDebitAmount");
							if(yearDebit!=null && !"".equals(yearDebit)){
								debit = new BigDecimal(debit).add(new BigDecimal(yearDebit.toString())).toString();
							}
							Object yearLend = init.get("sumInitLendAmount");
							if(yearLend!=null && !"".equals(yearLend)){
								lend = new BigDecimal(lend).add(new BigDecimal(yearLend.toString())).toString();
							}
							Object yearDebitCurrency = init.get("sumInitDebitCurrencyAmount");
							if(yearDebitCurrency!=null && !"".equals(yearDebitCurrency)){
								debitCurrency = new BigDecimal(debitCurrency).add(new BigDecimal(yearDebitCurrency.toString())).toString();
							}
							Object yearLendCurrency = init.get("sumInitLendCurrencyAmount");
							if(yearLendCurrency!=null && !"".equals(yearLendCurrency)){
								lendCurrency = new BigDecimal(lendCurrency).add(new BigDecimal(yearLendCurrency.toString())).toString();
							}
						}
					}
				}
				accMap.put("sumYearDebitAmount", debit);																//����跽���
				accMap.put("sumYearLendAmount", lend);																	//����������
				accMap.put("sumYearDebitCurrencyAmount", debitCurrency);												//����ԭ�ҽ跽���
				accMap.put("sumYearLendCurrencyAmount", lendCurrency);													//����ԭ�Ҵ������
				
				/* ��ָ����ʽ�������������� */
				valuesMap.put(accClassCode+"_"+credYear+"_"+period, accMap);
			}
		}
		
		/* ����������ʾ�淶 */
		Iterator iter  = valuesMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			HashMap valueMap = (HashMap)entry.getValue();
			String[] strs = new String[]{"sumYearDebitAmount","sumYearLendAmount","PeriodBalaBase","sumDCBala","PeriodBala","sumDebitCurrencyAmount",
					"sumYearLendCurrencyAmount","sumDebitAmount","sumDCBalaCurrency","sumLendCurrencyAmount","sumYearDebitCurrencyAmount","sumLendAmount","initAmount","initCurrencyAmount"};
			if(valueMap != null && valueMap.size()>0){
				for(String str : strs){
					String absValue = "abs";
					if("sumDebitAmount".equals(str) || "sumLendAmount".equals(str) || "sumYearDebitAmount".equals(str) || "sumYearLendAmount".equals(str) || 
							"sumDebitCurrencyAmount".equals(str) || "sumLendCurrencyAmount".equals(str)){
						absValue = "";
					}
					//��ĩ����
					if("PeriodBalaBase".equals(str) || "initAmount".equals(str)){
						String init = "isflag";
						if("initAmount".equals(str)){
							init = "isInitflag";
						}
						String moneys = "0";
						Object o = valueMap.get(str);
						if(o != null && !"".equals(o)){
							moneys = new BigDecimal(moneys).add(new BigDecimal(o.toString())).toString();
							if(Double.valueOf(moneys)>0){
								valueMap.put(init, "��");
							}else if(Double.valueOf(moneys)<0){
								valueMap.put(init, "��");
							}else{
								valueMap.put(init, "ƽ");
							}
						}else{
							valueMap.put(init, "ƽ");
						}
					}
					valueMap.put(str,dealDataDouble(String.valueOf(valueMap.get(str)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), absValue));
				}
			}
		}
		
		List accList = new ArrayList();
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			//��Ŀ���ν��й���
			if(accCodeLevel != null && !"".equals(accCodeLevel)){
				//��Ŀ����
				String code = String.valueOf(map.get("classCode"));
				if(!(code.length()/5-1<=Integer.parseInt(accCodeLevel))){
					continue;
				}
			}
			//�Ƿ��Ǻ�����
			String isCalculate = map.get("isCalculate").toString();
			if(!"1".equals(isCalculate)){
				//�Ǻ����Ŀ
				map.put("period", periodList);
				accList.add(map);
			}
		}
		obj = new Object[]{accList,valuesMap,currencyValue};
		result.setRetVal(obj);
		return result;
	}
	
	/**
	 * ��ѯ������Ŀ��ϸ����������
	 * 1.��ѯ����ƿ�Ŀ���ڳ����
	 * 2.��ѯ���ںϼ�
	 * 3.��ѯ��ϸ��ƾ֤��¼
	 * 4.��ѯ�����������ڼ�
	 * 5.������ݣ����ݵ���ϣ�ͳ�Ʊ����ۼƣ�
	 * @param conMap
	 * @param classCode
	 * @return
	 */
	public Result queryAccCalculateDetDetail(final HashMap<String,String> conMap,final String classCode,
			final MOperation mop,final LoginBean loginBean){
		final Result result = new Result();
		
		//�Աұ���д���
		final String currencyName = conMap.get("currencyName"); 									//�ұ�
		String isBaseCurrency = "";																	//�����ıұ�(all ���бұ����ʽ�� "".equals(currencyName) �ۺϱ�λ�ң�false ��ң�true ��λ��)
		if(currencyName!=null && !"".equals(currencyName)){
			//�����ۺϱ�λ��
			if("all".equals(currencyName)){
				isBaseCurrency = "all";
			}else{
				Result rs = queryIsBaseCurrency(currencyName);
				isBaseCurrency = rs.retVal.toString();
			}
		}
		final String isCurrency = isBaseCurrency;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							String binderNo = conMap.get("binderNo");								//����δ����ƾ֤
							String accCodeLevel = conMap.get("accCodeLevel");       				//��Ŀ����
							String accCodeStart = conMap.get("accCodeStart");						//��ƿ�Ŀ��ʼ
							String accCodeEnd = conMap.get("accCodeEnd");							//��ƿ�Ŀ����
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
							String yearStart = conMap.get("periodYearStart");
							String yearEnd = conMap.get("periodYearEnd");
							String periodStart = conMap.get("periodStart");
							String periodEnd = conMap.get("periodEnd");
							String itemSort = conMap.get("itemSort"); 								//��Ŀ���
							String accCode = conMap.get("accCode");									//��ƿ�Ŀ
							String dateType = conMap.get("dateType");
							String dateStart = conMap.get("dateStart");								//���ڿ�ʼ
							String dateEnd = conMap.get("dateEnd");									//���ڽ���
							
							String startTime = "";
							String endTime = "";
							if(dateType != null && "1".equals(dateType)){
								//���ڼ���в�ѯ
								if(Integer.valueOf(periodStart)<10){
									startTime = yearStart+"-0"+periodStart;
								}else{
									startTime = yearStart+"-"+periodStart;
								}
								if(Integer.valueOf(periodEnd)<10){
									endTime = yearEnd+"-0"+periodEnd;
								}else{
									endTime = yearEnd+"-"+periodEnd;
								}
							}else if(dateType != null && "2".equals(dateType)){
								//��ʱ����в�ѯ
								startTime = dateStart.substring(0,dateStart.lastIndexOf("-"));
								endTime = dateEnd.substring(0,dateEnd.lastIndexOf("-"));
								yearStart = dateStart.substring(0,dateStart.indexOf("-"));
								periodStart = String.valueOf(Integer.parseInt(dateStart.substring(dateStart.indexOf("-")+1,dateStart.lastIndexOf("-"))));
							}
							
							/* �������� */
							String sql1 = scopeSql(mop, loginBean);
							if(binderNo == null || "".equals(binderNo)){
	            				//������δ����ƾ֤
								sql1 += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
	            			}
							if("DepartmentCode".equals(itemSort)){
								//�������
								sql1 += " AND tblAccTypeInfo.DepartmentCode like '"+classCode+"%'";
							}else if("EmployeeID".equals(itemSort)){
								//���ְԱ
								sql1 += " AND tblAccTypeInfo.EmployeeID like '"+classCode+"%'";
							}else if("StockCode".equals(itemSort)){
								//����ֿ�
								sql1 += " AND tblAccTypeInfo.StockCode like '"+classCode+"%'";
							}else if("ClientCode".equals(itemSort)){
								//����ͻ�
								sql1 += " AND tblAccTypeInfo.ClientCode like '"+classCode+"%' AND (tblCompany.ClientFlag=2 or tblCompany.ClientFlag=3) AND tblCompany.statusId!='-1'";
							}else if("SuplierCode".equals(itemSort)){
								//�����Ӧ��
								sql1 += " AND tblAccTypeInfo.SuplierCode like '"+classCode+"%' AND (tblCompany.ClientFlag=1 or tblCompany.ClientFlag=3) and tblCompany.statusId!='-1'";
							}else if("ProjectCode".equals(itemSort)){
								//�����Ŀ
								sql1 += " AND tblAccTypeInfo.ProjectCode like '"+classCode+"%'";
							}
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//����ʾ���ÿ�Ŀ
								sql1 += " AND tblAccTypeInfo.statusId=0";
							}
							String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
                            
							if("true".equals(isCurrency)){
								//��λ��
								sql1 += " AND (tblAccDetail.Currency='' OR tblAccDetail.Currency IS NULL OR tblAccDetail.Currency='"+currencyName+"') ";
							}else if("false".equals(isCurrency)){
								//��������
								sql1 += " AND tblAccDetail.Currency='"+currencyName+"' ";
							}
							
							
							/**
							 * ��ѯ����ƿ�Ŀ���ڳ����
							 */
							StringBuffer sql = new StringBuffer("SELECT "+yearStart+" as accYear,"+periodStart+" as accPeriod,");
							sql.append("abs(SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount)) AS sumBalanceCurrencyAmount,");
							sql.append("abs(SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount)) AS sumBalanceAmount,");
							sql.append("(SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount)) AS sumAmount,");
							sql.append("SUM(tblAccDetail.DebitAmount) AS sumDebit,");
							sql.append("SUM(tblAccDetail.LendAmount) AS sumLend,");
							sql.append("(SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount)) AS sumCurrencyAmount ");
							sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo");
							sql.append(" ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode LEFT JOIN tblCompany ON tblCompany.classCode=tblAccDetail.CompanyCode WHERE 1=1 ");
							if(dateType!=null && "1".equals(dateType)){
								sql.append(" AND tblAccMain.CredYear<="+yearStart+" AND tblAccMain.Period<"+periodStart);
							}else if(dateType!=null && "2".equals(dateType)){
								sql.append(" AND tblAccMain.BillDate<'"+dateStart+"'");
							}
							sql.append(sql1);
							sql.append(" AND tblAccTypeInfo.isCalculate=1 ");
							sql.append(" AND tblAccTypeInfo.classCode like (select a.classCode from tblAccTypeInfo a where a.AccNumber='"+accCode+"')+'%'");
							
							//*****�����ڳ�*******//
							StringBuffer iniSql = new StringBuffer("select "+yearStart+" as accYear,"+periodStart+" as accPeriod,0 as sumBalanceCurrencyAmount,0 as sumBalanceAmount,");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 1 then tblAccBalance.CurrYIniBalaBase else -tblAccBalance.CurrYIniBalaBase end) AS sumAmount, ");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 1 then tblAccBalance.CurrYIniBalaBase else 0 end)as sumDebit,");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 2 then tblAccBalance.CurrYIniBalaBase else 0 end)as sumLend,");
							iniSql.append(" 0 AS sumCurrencyAmount ");
							iniSql.append(" from tblAccTypeInfo join tblAccBalance on tblAccTypeInfo.AccNumber=tblAccBalance.SubCode ");
							iniSql.append(" where 1 = 1 and tblAccTypeInfo.statusId=0 AND tblAccBalance.Nmonth =-1 and tblAccBalance.Nyear=-1 ");
							iniSql.append(" and tblAccTypeInfo.classCode like (select a.classCode from tblAccTypeInfo a where a.AccNumber='"+accCode+"')+'%' ");																																																										
							//*******end*******//
												
							//*****����sql****//
							String execsql = "select accYear,accPeriod,sum(sumBalanceCurrencyAmount) as sumBalanceCurrencyAmount,sum(sumBalanceAmount) as sumBalanceAmount,sum(sumAmount) as sumAmount,sum(sumDebit) as sumDebit,sum(sumLend) as sumLend,sum(sumCurrencyAmount) as sumCurrencyAmount from("+sql+" union "+iniSql+")t group by accYear,accPeriod";
							//******end*****//
							
							System.out.println("��ѯ�ڳ���"+execsql);
							rset = st.executeQuery(execsql);
							HashMap initMap = new HashMap();
							if (rset.next()) {
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				initMap.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				initMap.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					if("sumAmount".equals(rset.getMetaData().getColumnName(i))){
                        						if(!"".equals(strvalue)){
                        							Double discrepancyAmount = Double.valueOf(strvalue);
                        							if(discrepancyAmount>0){
                        								initMap.put("isInitflag", "��");
                        							}else if(discrepancyAmount<0){
                        								initMap.put("isInitflag", "��");
                        							}else{
                        								initMap.put("isInitflag", "ƽ");
                        							}
                        						}else{
                        							initMap.put("isInitflag", "ƽ");
                        						}
                        					}
                        					initMap.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				initMap.put(rset.getMetaData().getColumnName(i), obj);
                            			}
	                        		}
	                        	}
							}
							
							/**
							 * ��ѯ���ڽ�����ϼ�
							 */
                            sql = new StringBuffer("SELECT tblAccMain.CredYear,tblAccMain.Period,");
                            if("all".equals(isCurrency)){
                            	sql.append("ISNULL(tblAccDetail.currency,'') AS Currency,");
                            }
                            sql.append("SUM(tblAccDetail.DebitAmount) as sumDebitAmount,SUM(tblAccDetail.DebitCurrencyAmount) as sumDebitCurrencyAmount,");
                            sql.append("SUM(tblAccDetail.LendAmount) as sumLendAmount,SUM(tblAccDetail.LendCurrencyAmount) as sumLendCurrencyAmount,");
                            sql.append("SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount) AS sumDCBala,");
                            sql.append("SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount) AS sumDCBalaCurrency ");
                            sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id ");
                            sql.append(" JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode ");
                            sql.append(" LEFT JOIN tblCompany ON tblCompany.classCode=tblAccDetail.CompanyCode ");
                            sql.append(" WHERE tblAccTypeInfo.isCalculate=1");
                            sql.append(" AND tblAccTypeInfo.classCode like (select a.classCode from tblAccTypeInfo a where a.AccNumber='"+accCode+"')+'%'");
                            sql.append(sql1);
                            if(dateType!=null && "1".equals(dateType)){
                            	sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
                        	}else if(dateType!=null && "2".equals(dateType)){
                        		sql.append(" AND tblAccMain.BillDate>='"+dateStart+"' ");
                        		sql.append(" AND tblAccMain.BillDate<='"+dateEnd+"' ");
                        	}
                            sql.append(" GROUP BY tblAccMain.CredYear,tblAccMain.Period ");
                            if("all".equals(isCurrency)){
                            	//���бұ����ʽ
                            	sql.append(",tblAccDetail.currency");
                            }
                            sql.append(" ORDER BY tblAccMain.CredYear,tblAccMain.period");
                            System.out.println("���ڽ��sql:"+sql);
                            HashMap curPeriodMap = new HashMap();
                            rset = st.executeQuery(sql.toString());
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            		/* ����ҽ��з���ʱ���跽�����ߴ������Ҫ���е��� */
                            		HashMap oldMap = (HashMap)curPeriodMap.get(map.get("CredYear")+"_"+map.get("Period"));
                            		if(oldMap != null && oldMap.size()>0){
                            			if("sumDebitAmount".equals(rset.getMetaData().getColumnName(i)) || "sumLendAmount".equals(rset.getMetaData().getColumnName(i)) || 
                            					"sumDCBala".equals(rset.getMetaData().getColumnName(i))){
                            				Object o = oldMap.get(rset.getMetaData().getColumnName(i));
                            				if(o != null && !"".equals(o)){
                            					String money = map.get(rset.getMetaData().getColumnName(i)).toString();
                            					if("".equals(money)){
                            						money = "0";
                            					}
                            					map.put(rset.getMetaData().getColumnName(i),new BigDecimal(money.toString()).add(new BigDecimal(o.toString())).toString());
                            				}
                            			}
                            		}
                            		if("all".equals(isCurrency) && ("sumDebitCurrencyAmount".equals(rset.getMetaData().getColumnName(i)) || "sumLendCurrencyAmount".equals(rset.getMetaData().getColumnName(i))
                            				|| "sumDCBalaCurrency".equals(rset.getMetaData().getColumnName(i)))){
                            			map.put("curr_"+map.get("Currency")+"_"+rset.getMetaData().getColumnName(i), map.get(rset.getMetaData().getColumnName(i)));
                            		}
                            	}
                            	curPeriodMap.put(map.get("CredYear")+"_"+map.get("Period"), map);
                            }
                            
                            /**
                             * ��ѯ��ʼ�ڼ�֮ǰ�ı����ۼ�
                             */
                            sql = new StringBuffer("SELECT SUM(tblAccDetail.DebitAmount) AS sumDebitAmount,SUM(tblAccDetail.LendAmount) AS sumLendAmount");
							sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo");
							sql.append(" ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode LEFT JOIN tblCompany ON tblCompany.classCode=tblAccDetail.CompanyCode WHERE 1=1 ");
							if(dateType!=null && "1".equals(dateType)){
								sql.append(" AND tblAccMain.CredYear="+yearStart+" AND tblAccMain.Period<"+periodStart);
							}else if(dateType!=null && "2".equals(dateType)){
								sql.append(" AND tblAccMain.CredYear="+dateStart.substring(0,dateStart.indexOf("-"))+" AND tblAccMain.BillDate<'"+dateStart+"'");
							}
							sql.append(sql1);
							sql.append(" AND tblAccTypeInfo.isCalculate=1 ");
							sql.append(" AND tblAccTypeInfo.classCode like (select a.classCode from tblAccTypeInfo a where a.AccNumber='"+accCode+"')+'%'");
							rset = st.executeQuery(sql.toString());
							HashMap yearPreMap = new HashMap();
							if (rset.next()) {
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				yearPreMap.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				yearPreMap.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					yearPreMap.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				yearPreMap.put(rset.getMetaData().getColumnName(i), obj);
                            			}
	                        		}
	                        	}
							}
                            
                            /**
                             * ��ѯƾ֤��ϸ����
                             */
    						sql = new StringBuffer("SELECT tblAccMain.id,tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccDate,");
    						sql.append("tblAccMain.CredTypeID+'-'+convert(varchar,tblAccMain.OrderNo) AS credTypeIdOrderNo,tblAccDetail.RecordComment,");
    						sql.append("(DebitAmount-LendAmount) AS sumAmount,(DebitCurrencyAmount-LendCurrencyAmount) AS sumCurrencyAmount,");
    						sql.append("DebitAmount,LendAmount,DebitCurrencyAmount,LendCurrencyAmount,ISNULL(tblAccDetail.Currency,'') AS Currency,CurrencyRate from tblAccDetail LEFT JOIN ");
    						sql.append("tblAccMain ON tblAccDetail.f_ref=tblAccMain.id LEFT JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode ");
    						sql.append("LEFT JOIN tblCompany ON tblCompany.classCode=tblAccDetail.CompanyCode ");
    						sql.append("WHERE tblAccTypeInfo.classCode like (select a.classCode from tblAccTypeInfo a where a.AccNumber='"+accCode+"')+'%'");
    						sql.append(sql1);
    						if(dateType!=null && "1".equals(dateType)){
    							sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
                        	}else if(dateType!=null && "2".equals(dateType)){
                        		sql.append(" AND tblAccMain.BillDate>='"+dateStart+"' ");
                        		sql.append(" AND tblAccMain.BillDate<='"+dateEnd+"' ");
                        	}
    						sql.append(" AND tblAccTypeInfo.isCalculate=1 ");
    						sql.append(" ORDER BY tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccDate,credTypeIdOrderNo");
    						System.out.println("��ѯ��ϸ��"+sql.toString());
    						rset = st.executeQuery(sql.toString());
    						List accMainList = new ArrayList();
    						while (rset.next()) {
    							HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rset.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	accMainList.add(map);
    						}
    						
    						/**
                             * ��ѯ���������Ļ���ڼ�
                             */
                            sql = new StringBuffer("SELECT ap.AccYear,ap.AccPeriod,ap.periodEnd FROM tblAccPeriod ap WHERE 1=1 ");
                            if(dateType!=null && "1".equals(dateType)){
    							str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,ap.AccYear)+'-'+CONVERT(VARCHAR,ap.AccPeriod)+'-01',120),120)";
    							sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
                        	}else if(dateType!=null && "2".equals(dateType)){
                        		sql.append(" AND SUBSTRING(periodBegin,1,7)>='"+dateStart.substring(0,dateStart.lastIndexOf("-"))+"' ");
                        		sql.append(" AND SUBSTRING(periodBegin,1,7)<='"+dateEnd.substring(0,dateStart.lastIndexOf("-"))+"' ");
                        	}
    						sql.append(" ORDER BY ap.AccYear,ap.AccPeriod");
    						System.out.println("����ڼ䣺"+sql);
    						rset=st.executeQuery(sql.toString());
    						List periodList = new ArrayList();
    						while (rset.next()) {
    							HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rset.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	periodList.add(map);
    						}
                            
    						/**
    						 * �������� ��initMap �ڳ����ͳ�ƣ�curPeriodMap ���ڼ�Ľ��ͳ�ư�����ҽ� accMainList ���ƾ֤���飬periodList �ڼ䣬yearPreMap ��ѯ֮ǰ�ı����
    						 */
                            Object[] obj = new Object[]{initMap, curPeriodMap, accMainList, periodList, yearPreMap};
                            result.setRetVal(obj);
                            
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryAccCalculateDetDetail:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		
		/* ȡ���� */
		Object[] obj = (Object[])result.retVal;
		HashMap initMap = (HashMap)obj[0];																		//�ڳ�����HashMap
		HashMap curPeriodMap = (HashMap)obj[1];																	//���ڽ��������
		List accMainList = (ArrayList)obj[2];																	//��ϸ������
		List periodList = (ArrayList)obj[3];																	//���еĲ�ѯ�ڼ�
		HashMap yearPreMap = (HashMap)obj[4];																	//����֮ǰ�Ľ��ϼ�
		
		/**
		 * �������
		 */
		HashMap values = new HashMap();
		
		//�����ڳ����
		if(initMap.get("isInitflag") == null){
			initMap.put("isInitflag","ƽ");
		}
		values.put("initMap", initMap);
		
		List list = new ArrayList();
		
		/**
		 * ѭ��Ҫ��ѯ�Ļ���ڼ�
		 */
		for(int i=0;i<periodList.size();i++){
			HashMap map = (HashMap)periodList.get(i);														//����ڼ�Map
			Integer accYear = Integer.valueOf(map.get("AccYear").toString());								//����ڼ���
			Integer accPeriod = Integer.valueOf(map.get("AccPeriod").toString());							//����ڼ�
			Integer yearStarts = Integer.valueOf(conMap.get("periodYearStart").toString());					//��ѯ�Ŀ�ʼ�ڼ�
			
			HashMap curMap = (HashMap)curPeriodMap.get(accYear+"_"+accPeriod);								//��ȡ���ڵĽ�����
			if(curMap == null){
				//����δ��������ʱ��
				curMap = new HashMap();
				curMap.put("CredYear", accYear);
				curMap.put("Period", accPeriod);
			}
			
			String moneysumDCBala = "0";
			String moneysumDebit = "0";
			String moneysumLend = "0";
			Object o = null;
			Object oYearDebit = null;
			Object oYearLend = null;
			Object upsumDCBala = null;
			//�õ����ںϼƵ����  (��һ�ڵ��������ڳ����+���ڵĽ�������)
			if(i == 0){
				//����һ������ڼ�ʱ�����ںϼƵ����=�ڳ����+���ڽ跽���-���ڴ������
				o = initMap.get("sumAmount");
				upsumDCBala = initMap.get("sumAmount");
				oYearDebit = yearPreMap.get("sumDebitAmount");
				oYearLend = yearPreMap.get("sumLendAmount");
			}else{
				//ȡ��һ�ڵ����+���ڵĽ�������
				HashMap lastcurMap = null;																		//��һ�ڵĽ������
				Integer startPeriod = Integer.valueOf(accPeriod);
				boolean flag = false;
				Integer beforeYear = accYear;
				for(int k=accYear;k>=yearStarts;k--){
					for(int l=startPeriod-1;l>0;l--){
						lastcurMap = (HashMap)values.get(k+"_"+l);												//��values��ȡ������
						if(lastcurMap!=null){
							beforeYear = k;
							flag = true;
							break;
						}
					}
					if(flag){
						break;
					}
					startPeriod = 13;
				}
				if(lastcurMap!=null){
					//������һ�ڵ�����
					upsumDCBala = lastcurMap.get("sumDCBala");
					o = lastcurMap.get("sumDCBala");												//��һ�ڵ���ĩ���
					if(beforeYear.equals(accYear)){
						oYearDebit = lastcurMap.get("yearsumDebitAmount");								//��һ�ڵı���跽
						oYearLend = lastcurMap.get("yearsumLendAmount");								//��һ�ڵı������
					}
				}
			}
			if(o != null && !"".equals(o)){
				moneysumDCBala = o.toString();
			}
			o = curMap.get("sumDCBala");			//���ڽ�������
			if(o != null && !"".equals(o)){
				moneysumDCBala = new BigDecimal(moneysumDCBala).add(new BigDecimal(o.toString())).toString();
			}
			//�������
			curMap.put("sumDCBala", moneysumDCBala);
			
			/**
			 * ���������������= ��һ�ڼ�ı�������+���ڽ������
			 */
			if(oYearDebit != null && !"".equals(oYearDebit)){
				moneysumDebit = oYearDebit.toString();
			}
			oYearDebit = curMap.get("sumDebitAmount");						//���ڽ跽���
			if(oYearDebit != null && !"".equals(oYearDebit)){
				moneysumDebit = new BigDecimal(moneysumDebit).add(new BigDecimal(oYearDebit.toString())).toString();
			}
			//����跽���
			curMap.put("yearsumDebitAmount", moneysumDebit);
			
			if(oYearLend != null && !"".equals(oYearLend)){
				moneysumLend = oYearLend.toString();
			}
			oYearLend = curMap.get("sumLendAmount");						//���ڴ������
			if(oYearLend != null && !"".equals(oYearLend)){
				moneysumLend = new BigDecimal(moneysumLend).add(new BigDecimal(oYearLend.toString())).toString();
			}
			//����������
			curMap.put("yearsumLendAmount", moneysumLend);
			curMap.put("yearsumDCBala", moneysumDCBala);					//�������
			
			
			/**
			 * ��ȡƾ֤��ϸ
			 */
			List accList = new ArrayList();
			for(int j = 0;j<accMainList.size();j++){
				HashMap accmain = (HashMap)accMainList.get(j);
				if(map.get("AccYear").equals(accmain.get("CredYear")) && map.get("AccPeriod").equals(accmain.get("Period"))){
					//���ڸû���ڼ��ƾ֤��ϸ
					String money = "0";
					Object object = new Object();
					if(accList.size()>0){
						//���ǵ�һ����ϸʱ �����=��һ�����+����ϸ�Ľ����
						HashMap accMap = (HashMap)accList.get(accList.size()-1);
						money = "0";
						object = accMap.get("sumAmount");													//��һ�������
						if(object!=null && !"".equals(object)){
							money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
						}
						object = accmain.get("sumAmount");													//���������
						if(object!=null && !"".equals(object)){
							money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
						}
						accmain.put("sumAmount", money);
					}else{
						//����ǵ�һ��ʱ����һ�ڼ�ı������+����ϸ�Ľ�����
						money = "0";
						if(upsumDCBala!=null && !"".equals(upsumDCBala)){
							money = new BigDecimal(money).add(new BigDecimal(upsumDCBala.toString())).toString();
						}
						object = accmain.get("sumAmount");
						if(object!=null && !"".equals(object)){
							money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
						}
						accmain.put("sumAmount", money);
					}
					accList.add(accmain);
				}
			}
			curMap.put("accMainList", accList);													//��ϸ
			values.put(accYear+"_"+accPeriod, curMap);
		}
		
		/**
		 * ����
		 */
		HashMap map = new HashMap();
		Iterator iterator = values.entrySet().iterator();
		while(iterator.hasNext()){
			Entry entry = (Entry)iterator.next();
			String key = String.valueOf(entry.getKey());
			HashMap hashMap = (HashMap)entry.getValue();
			String[] strs = null;
			if("initMap".equals(key)){
				//�ڳ�map����
				strs = new String[]{"sumAmount","sumCurrencyAmount","sumBalanceAmount","sumBalanceCurrencyAmount","sumAmount"};
				for(String s : strs){
					hashMap.put(s,dealDataDouble(hashMap.get(s)==null?"":String.valueOf(hashMap.get(s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
				}
			}else{
				//�������ݴ���
				strs = new String[]{"yearsumDebitAmount","yearsumLendAmount","yearsumDCBala","sumDebitAmount","sumLendAmount","sumDCBala","sumDCBalaCurrency"};
				for(String s : strs){
					if("yearsumDCBala".equals(s) || "sumDCBala".equals(s)){
						String isFlag = "";
						if("yearsumDCBala".equals(s)){
							isFlag = "year";
						}
						Object o = hashMap.get(s);
						String m = "0";
						if(o != null && !"".equals(o)){
							m = o.toString();
						}
						if(Double.parseDouble(m)>0){
							//�跽
							hashMap.put(isFlag+"isflag","��");
						}else if(Double.parseDouble(m)<0){
							//����
							hashMap.put(isFlag+"isflag","��");
						}else{
							hashMap.put(isFlag+"isflag","ƽ");	
						}
					}
					hashMap.put(s,dealDataDouble(hashMap.get(s)==null?"":String.valueOf(hashMap.get(s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
				}
				
				//������ϸ
				List accList = (ArrayList)hashMap.get("accMainList");
				strs = new String[]{"sumAmount","sumCurrencyAmount"};
				for(int k = 0;k<accList.size();k++){
					HashMap detail = (HashMap)accList.get(k);
					if(detail != null){
						for(String s : strs){
							if("sumAmount".equals(s)){
								Object o = detail.get(s);
								String m = "0";
								if(o != null && !"".equals(o)){
									m = o.toString();
								}
								if(Double.parseDouble(m)>0){
									//�跽
									detail.put("isflag","��");
								}else if(Double.parseDouble(m)<0){
									//����
									detail.put("isflag","��");
								}else{
									detail.put("isflag","ƽ");	
								}
							}
							detail.put(s,dealDataDouble(detail.get(s)==null?"":String.valueOf(detail.get(s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
						}
					}
				}
			}
		}
		
		Object[] o = new Object[]{periodList,values};
		result.setRetVal(o);
		return result;
	}
	
	/**
	 * ����������
	 * @param value  ֵ
	 * @param len	 ����С�����λ
	 * @param flag   abs ȡ����ֵ
	 * @return
	 */
	public String dealDataDouble(String value,String len,String flag){
		if(value == null || "".equals(value) || value.length()==0){
			return "";
		}
		Pattern pattern = Pattern.compile("^[-]{0,1}[0-9,\\.]*$");
        Matcher matcher = pattern.matcher(value);
        if(matcher.find()) {
        	String strvalue = value;
			String values = "000000000";
			//���ص����ж�������
			if("abs".equals(flag)){
				DecimalFormat df = new DecimalFormat("#.###########");
				strvalue = df.format(Math.abs(Double.parseDouble(strvalue)));
			}
			if (strvalue.indexOf(".")>0){
				if(strvalue.substring(strvalue.indexOf(".")+1).length()<Integer.valueOf(len)){
					strvalue = strvalue+values.substring(0,Integer.valueOf(len)-strvalue.substring(strvalue.indexOf(".")+1).length());
				}
				strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(len)+1);
			}else{
				strvalue = strvalue+"."+values.substring(0,Integer.valueOf(len));
			}
			if(Double.valueOf(strvalue)==0){
				strvalue = "";
			}
			return strvalue;
        }
		return "";
	}
	
	/**
	 * ��ѯ����ƽ�������
	 * @param conMap
	 * @return
	 */
	public Result accTrialBalance(final HashMap<String,String> conMap,final MOperation mop,final LoginBean loginBean){
		final Result result = new Result();
		
		//�Աұ���д���
		String currencyName = conMap.get("currencyName"); 									//�ұ�('isBase'=��λ�ң�''=�ۺϱ�λ�ң�'all'=���бұ�'��ҵ�id'=���)
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		//�������
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		final String accTypeNoItem = conMap.get("accTypeNoItem");									//��Ŀ�޷������ʾ
		final String showDetail = conMap.get("showDetail");											//ֻ��ʾ��ϸ
		final String levelStart = conMap.get("levelStart");											//��Ŀ����
		final String showItemDetail = conMap.get("showItemDetail");									//��ʾ������Ŀ��ϸ
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							
							String binderNo = conMap.get("binderNo");								//����δ����ƾ֤
							String periodYearStart = conMap.get("periodYearStart");					//����ڼ���
							String periodStart = conMap.get("periodStart");							//����ڼ�
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ

							String startTime = "";
							if(Integer.valueOf(periodStart)<10){
								startTime = periodYearStart+"-0"+periodStart;
							}else{
								startTime = periodYearStart+"-"+periodStart;
							}
							
							String condition = scopeSql(mop, loginBean);
							
							HashMap accMap = new HashMap();
							
							/**
							 * ��ѯ�������������Ļ�ƿ�Ŀ
							 */
							StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,l.zh_cn as AccFullName,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,");
							sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.jdFlag FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 ");
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//����ʾ���ÿ�Ŀ
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							sql.append(condition);
							sql.append(" ORDER BY tblAccTypeInfo.AccNumber");
							System.out.println("��Ŀ��ѯSQL��"+sql.toString());
                            /* ִ�в�ѯ���ұ������������Ŀ�Ŀ */
                            rset=st.executeQuery(sql.toString());
                            List accTypeInfoList = new ArrayList();
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rset.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	accMap.put(map.get("classCode")+"_isCalculate", map.get("isCalculate"));
                            	accMap.put(map.get("classCode")+"_jdFlag", map.get("jdFlag"));
                            	accMap.put(map.get("classCode")+"_AccNumber", map.get("AccNumber"));
                            	accTypeInfoList.add(map);
                            }
                            
                            /**
                             * ��ѡ�ˣ�����δ����ƾ֤������ѯָ���ڼ��δ�������ݽ��
                             */
                            String str = "";
                            HashMap noBindMap = new HashMap();				//�����ڼ��е�δ���˵�����
                            HashMap noBindInitMap = new HashMap();			//���濪ʼ�ڼ�֮ǰδ���˵�����
							if(binderNo != null && !"".equals(binderNo)){
								String initSql = "";						//��ѯ�ڳ�δ��������sql
	                            sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,SUM(DebitAmount) AS PeriodDebitSumBase,");
	                            sql.append("SUM(LendAmount) AS PeriodCreditSumBase,SUM(DebitAmount)-SUM(LendAmount) AS PeriodDCBalaBase ");
	                            if(!"".equals(currencyValue)){
	                            	sql.append(",SUM(DebitCurrencyAmount) AS PeriodDebitSum,SUM(LendCurrencyAmount) AS PeriodCreditSum,SUM(DebitCurrencyAmount)-SUM(LendCurrencyAmount) AS PeriodDCBala ");
	                            	if("all".equals(currencyValue)){
	                            		sql.append(",isnull(tblAccDetail.Currency,'') as Currency ");
	                            	}
	                            }
	                            sql.append("FROM  tblAccDetail ");
	                            sql.append("LEFT JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id LEFT JOIN tblAccTypeInfo tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode ");
	                            sql.append("WHERE tblAccMain.workFlowNodeName!='finish' ");
	                            //��ѡ����Ҳ���<�ۺϱ�λ��><���б���>ʱ���й���
	                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
	                            	if("isBase".equals(currencyValue)){
	                            		//��λ��
	                            		sql.append(" AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+searchCurrency+"') ");
	                            	}else{
	                            		sql.append(" AND tblAccDetail.Currency='"+searchCurrency+"' ");
	                            	}
	                            }
	                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
	                            initSql = sql.toString()+" AND "+str+"<'"+startTime+"' "+condition+" GROUP BY tblAccTypeInfo.classCode,tblAccTypeInfo.JdFlag";
	                            if("all".equals(currencyValue)){
	                            	initSql += ",tblAccDetail.Currency";
	                            }
	                            sql.append(" AND "+str+"='"+startTime+"'");
	                            sql.append(condition);
	                            sql.append(" GROUP BY tblAccTypeInfo.classCode");
	                            if("all".equals(currencyValue)){
	                            	sql.append(",tblAccDetail.Currency");
	                            }
	                            System.out.println("��ƿ�Ŀδ����sql��"+sql.toString());
	                            rset = st.executeQuery(sql.toString());
	                            while (rset.next()) {
	                            	HashMap periodMap=new HashMap();
	                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                            		Object obj=rset.getObject(i);
	                            		if(obj==null){
	                            			periodMap.put(rset.getMetaData().getColumnName(i), "");
	                            		}else{
	                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                            					String strvalue = String.valueOf(obj);
                            					if (strvalue.indexOf(".")>0){
                            						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                            					}
                            					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                            						strvalue = "";
                            					}
                            					periodMap.put(rset.getMetaData().getColumnName(i), strvalue);
	                            			}else{
	                            				periodMap.put(rset.getMetaData().getColumnName(i), obj);
	                            			}
	                            		}
	                            	}
	                            	//��ѡ��ıұ��ǡ������бұ����ʽ
	                            	if("all".equals(currencyValue)){
	                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala"};
	                            		if("".equals(periodMap.get("Currency"))){
	                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
	                            			for(String s : curs){
	                            				periodMap.put(s+"_", periodMap.get(s+"Base"));
	                            			}
	                            		}else{
	                            			//�������
	                            			for(String s : curs){
	                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
	                            			}
	                            		}
	                            	}
	                            	noBindMap.put(periodMap.get("classCode"), periodMap);
	                            }
	                            
	                            //��ѯ�ڳ�����ʱ���в�ѯ����ڼ�֮ǰδ���˵����ݽ���ͳ��
	                            initSql = initSql.replaceAll("SELECT ", "SELECT tblAccTypeInfo.JdFlag,");
	                            System.out.println("��ƿ�Ŀǰδ����"+initSql);
	                            rset = st.executeQuery(initSql);
	                            while (rset.next()) {
	                            	HashMap periodMap=new HashMap();
	                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                            		Object obj=rset.getObject(i);
	                            		if(obj==null){
	                            			periodMap.put(rset.getMetaData().getColumnName(i), "");
	                            		}else{
	                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                            					String strvalue = String.valueOf(obj);
                            					if (strvalue.indexOf(".")>0){
                            						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                            					}
                            					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                            						strvalue = "";
                            					}
                            					periodMap.put(rset.getMetaData().getColumnName(i), strvalue);
	                            			}else{
	                            				periodMap.put(rset.getMetaData().getColumnName(i), obj);
	                            			}
	                            		}
	                            	}
	                            	//��ѡ��ıұ��ǡ������бұ����ʽ
	                            	if("all".equals(currencyValue)){
	                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala"};
	                            		if("".equals(periodMap.get("Currency"))){
	                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
	                            			for(String s : curs){
	                            				periodMap.put(s+"_", periodMap.get(s+"Base"));
	                            			}
	                            		}else{
	                            			//�������
	                            			for(String s : curs){
	                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
	                            			}
	                            		}
	                            	}
	                            	noBindInitMap.put(periodMap.get("classCode"), periodMap);
	                            }
							}
							
                            /**
                             * ��ѯ����ƽ���Ľ�tblAccBalance��
                             */
                            sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccBalance.Nyear,");
                            sql.append("tblAccBalance.Period,tblAccBalance.SubCode,ISNULL(tblAccTypeInfo.isCataLog,0) as isCataLog,tblAccBalance.PeriodIniBase,");
                            sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,");
                            sql.append("tblAccBalance.PeriodIni,tblAccBalance.PeriodDebitSumBase,tblAccBalance.PeriodDebitSum,tblAccBalance.PeriodCreditSumBase,");
                            sql.append("tblAccBalance.PeriodCreditSum,tblAccBalance.PeriodBalaBase,tblAccBalance.PeriodBala,");
                            sql.append("(tblAccBalance.PeriodDebitSumBase-tblAccBalance.PeriodCreditSumBase) AS PeriodDCBalaBase,");
                            sql.append("(tblAccBalance.PeriodDebitSum-tblAccBalance.PeriodCreditSum) AS PeriodDCBala,");
                            sql.append("tblAccTypeInfo.jdFlag,isnull(tblAccBalance.CurType,'') as CurType FROM tblAccBalance left join ");
                            sql.append("tblAccTypeInfo ON tblAccBalance.SubCode=tblAccTypeInfo.AccNumber WHERE tblAccBalance.Nyear!=-1 AND tblAccBalance.period!=-1");
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
                            sql.append(condition);
                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccBalance.Nyear)+'-'+CONVERT(VARCHAR,tblAccBalance.Period)+'-01',120),120)";
                            sql.append(" AND "+str+"='"+startTime+"' AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1");
                            sql.append(" ORDER BY tblAccTypeInfo.classCode,tblAccBalance.Nyear,tblAccBalance.Period");
                            System.out.println("��ѯ����ƽ����"+sql.toString());
                            rset=st.executeQuery(sql.toString());
                            HashMap accBalanceMap = new HashMap();
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                            				map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	
                            	//��ѡ��ıұ��ǡ������бұ����ʽ
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodIni","PeriodDebitSum","PeriodCreditSum","PeriodBala","PeriodDCBala"};
                        			String base = "";
                        			if("".equals(map.get("CurType"))){
                        				base = "Base";
                        			}
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("CurType"), map.get(s+base));
                        			}
                            	}
                            	
                				//���ϼ���ֵ
            					String classCode = map.get("classCode").toString();
            					accBalanceMap.put(classCode,map);
            					for(int j=1;j<classCode.length()/5;j++){
            						HashMap oldMap = (HashMap)accBalanceMap.get(classCode.substring(0,j*5));
        							int count = 0;
        							String[] moneyStr = new String[]{"PeriodIniBase","PeriodIni","PeriodDebitSumBase","PeriodDebitSum","PeriodCreditSumBase","PeriodCreditSum","PeriodBalaBase","PeriodBala","PeriodDCBalaBase","PeriodDCBala"};
        							if("all".equals(currencyValue)){
        								moneyStr = new String[]{"PeriodIniBase","PeriodIni_"+map.get("CurType"),"PeriodDebitSumBase","PeriodDebitSum_"+map.get("CurType"),"PeriodCreditSumBase","PeriodCreditSum_"+map.get("CurType")
        										,"PeriodBalaBase","PeriodBala_"+map.get("CurType"),"PeriodDCBalaBase","PeriodDCBala_"+map.get("CurType")};
        							}
        							if(oldMap!=null && oldMap.size()>0){
        								for(String s : moneyStr){
        									String moneys = "0";
        									Object o = oldMap.get(s);
        									if(o!=null && !"".equals(o)){
        										moneys = o.toString();
        									}
        									if(map.get(s)!=null && !"".equals(map.get(s))){
        										moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
        									}
        									String totalAmount = moneys;
        									if(Double.parseDouble(moneys)==0){
        										totalAmount = "";
        									}
        									oldMap.put(s,totalAmount);
        								}
        								accBalanceMap.put(classCode.substring(0,j*5),oldMap);
        							}else{
        								//����������һ��ʱ�������µ�Map��������
        								oldMap = new HashMap();
        								oldMap.put("Nyear", map.get("Nyear"));
        								oldMap.put("Period", map.get("Period"));
        								Object accNumber = accMap.get(classCode.substring(0,j*5)+"_AccNumber");
        								oldMap.put("SubCode", accNumber);
        								oldMap.put("classCode", classCode.substring(0,j*5));
        								for(String s : moneyStr){
        									String totalAmount = "0";
	        								if(map.get(s) != null && !"".equals(map.get(s))){
	        									totalAmount = map.get(s).toString();
	        								}
	        								if(Double.valueOf(totalAmount)==0){
	        									totalAmount = "";
	        								}
	        								oldMap.put(s,totalAmount);
        								}
        								accBalanceMap.put(classCode.substring(0,j*5),oldMap);
        							}
            					}
                            }
                            
                            /* ����ѯ���ڼ��ڱ�tblAccBalance������ʱ��ʹ�ڼ���ǰ�ƣ���ѯ����ļ�¼ */
                            sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccBalance.Nyear,");
                            sql.append("tblAccBalance.Period,tblAccBalance.SubCode,ISNULL(tblAccTypeInfo.isCataLog,0) as isCataLog,");
                            sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,");
                            sql.append("tblAccBalance.CurrYIniBalaBase as PeriodIniBase,tblAccBalance.CurrYIniBala as PeriodIni,");
                            sql.append(" '' AS PeriodDCBalaBase,'' AS PeriodDCBala,tblAccTypeInfo.jdFlag,isnull(tblAccBalance.CurType,'') as CurType ");
                            sql.append(" FROM tblAccBalance left join tblAccTypeInfo ON tblAccBalance.SubCode=tblAccTypeInfo.AccNumber WHERE");
                            sql.append(" SubCode not in (select SubCode from tblAccBalance where "+str+"='"+startTime+"' AND Nyear!=-1 AND Period!=-1) AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1");
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
                            sql.append(" and tblAccBalance.Nyear =(select top 1 a.Nyear from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
							sql.append(" and tblAccBalance.Period =(select top 1 a.Period from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
                            sql.append(condition + " ORDER BY tblAccTypeInfo.classCode");
                            System.out.println("��ѯ����ƽ�����ӽ������ݣ�"+sql.toString());
                            rset=st.executeQuery(sql.toString());
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					if(!"PeriodDebitSumBase".equals(rset.getMetaData().getColumnName(i)) 
                        							|| !"PeriodCreditSumBase".equals(rset.getMetaData().getColumnName(i))){
                        						map.put(rset.getMetaData().getColumnName(i), strvalue);
                        					}
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	//��ѡ��ıұ��ǡ������бұ����ʽ
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodIni","PeriodDCBala"};
                            		String base = "";
                        			if("".equals(map.get("CurType"))){
                        				base = "Base";
                        			}
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("CurType"), map.get(s+base));
                        			}
                            	}
                				//���ϼ���ֵ
            					String classCode = map.get("classCode").toString();
            					for(int j=1;j<classCode.length()/5;j++){
            						HashMap oldMap = (HashMap)accBalanceMap.get(classCode.substring(0,j*5));
        							int count = 0;
        							String[] moneyStr = new String[]{"PeriodIniBase","PeriodIni"};
        							if("all".equals(currencyValue)){
        								moneyStr = new String[]{"PeriodIniBase","PeriodIni_"+map.get("CurType")};
        							}
        							if(oldMap!=null && oldMap.size()>0){
        								String moneys = "0";
        								for(String s : moneyStr){
        									moneys = "0";
        									Object o = oldMap.get(s);
        									if(o!=null && !"".equals(o)){
        										moneys = o.toString();
        									}
        									if(map.get(s)!=null && !"".equals(map.get(s))){
        										if(s.indexOf("PeriodIni") != -1){
        											if(accMap.get(classCode.substring(0,j*5)+"_jdFlag")!=null){
        												int oldjdFlag = Integer.parseInt(accMap.get(classCode.substring(0,j*5)+"_jdFlag").toString());
        												if(oldjdFlag == Integer.parseInt(map.get("jdFlag").toString())){
        													//�跽
        													moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();
        												}else{
        													moneys = new BigDecimal(moneys).subtract(new BigDecimal(map.get(s).toString())).toString();
        												}
        											}
        										}else{
        											moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
        										}
        									}
        									String totalAmount = moneys;
        									if(Double.parseDouble(moneys)==0){
        										totalAmount = "";
        									}
        									oldMap.put(s,totalAmount);
        								}
        								accBalanceMap.put(classCode.substring(0,j*5),oldMap);
        							}else{
        								//����������һ��ʱ�������µ�Map��������
        								oldMap = new HashMap();
        								oldMap.put("Nyear", map.get("Nyear"));
        								oldMap.put("Period", map.get("Period"));
        								Object accnumber = accMap.get(classCode.substring(0,j*5)+"_AccNumber");
        								oldMap.put("SubCode", accnumber);
        								oldMap.put("classCode", classCode.substring(0,j*5));
        								for(String s : moneyStr){
        									String totalAmount = "0";
	        								if(map.get(s) != null && !"".equals(map.get(s))){
	        									if(s.indexOf("PeriodIni") != -1){
        											if(accMap.get(classCode.substring(0,j*5)+"_jdFlag")!=null){
        												int oldjdFlag = Integer.parseInt(accMap.get(classCode.substring(0,j*5)+"_jdFlag").toString());
        												if(oldjdFlag == Integer.parseInt(map.get("jdFlag").toString())){
        													//�跽
        													totalAmount = new BigDecimal(totalAmount).add(new BigDecimal(map.get(s).toString())).toString();
        												}else{
        													totalAmount = new BigDecimal(totalAmount).subtract(new BigDecimal(map.get(s).toString())).toString();
        												}
        											}
        										}else{
        											totalAmount = map.get(s).toString();
        										}
	        								}
	        								if(Double.valueOf(totalAmount)==0){
	        									totalAmount = "";
	        								}
	        								oldMap.put(s,totalAmount);
        								}
        								accBalanceMap.put(classCode.substring(0,j*5),oldMap);
        							}
            					}
                            	accBalanceMap.put(map.get("classCode"), map);
                            }
							
                            //��������
                            Object obj = new Object[]{accTypeInfoList,accBalanceMap,noBindMap,noBindInitMap};
                            result.setRetVal(obj);
							
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt AccTrialBalance:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		/* ��ȡ���� */
		Object[] obj = (Object[])result.retVal;
		List accTypeInfoList = (ArrayList)obj[0];
		HashMap	accBalanceMap = (HashMap)obj[1];
		HashMap noBindMap = (HashMap)obj[2];
		HashMap noBindInitMap = (HashMap)obj[3];
		
		
		/**
		 * ��������
		 */
		
		//�����ڼ���δ��������
		if(noBindMap != null && noBindMap.size()>0){
			Iterator nobindMap = noBindMap.entrySet().iterator();
        	//����map�õ����������
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//�õ��������ݵ�map
				if(bindMap != null && bindMap.size()>0){
					//����δ���˵�����
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					for(int j=1;j<=bindClassCode.length()/5;j++){
						HashMap oldMap = (HashMap)accBalanceMap.get(bindClassCode.substring(0,j*5));
						String[] str1 = new String[]{"PeriodDebitSumBase","PeriodCreditSumBase","PeriodDCBalaBase","PeriodDebitSum","PeriodCreditSum","PeriodDCBala"};
						if("all".equals(currencyValue)){
							String[] currS = new String[(currList.size()+1)*3];
							int counts = 0;
							for(int k = 3;k<str1.length;k++){
								for(int l=0;l<currList.size();l++){
									String[] currStr = (String[])currList.get(l);
									if("true".equals(currStr[2])){
										//��λ��
										currS[counts] = str1[k]+"_";
									}else{
										currS[counts] = str1[k]+"_"+currStr[0];
									}
									counts ++;
								}
							}
							int curCount = currList.size()*3;
							for(int k=0;k<str1.length-3;k++){
								currS[curCount+k] = str1[k];
							}
							str1 = currS;
						}
						if(oldMap == null){
							oldMap = new HashMap();
							oldMap.put("classCode", bindClassCode.substring(0,j*5));
						}
						for(int k=0;k<str1.length;k++){
							String moneys1 = "0";
							//������Ͻ��
							Object o1 = null;
							if(oldMap!=null && oldMap.size()>0){
								o1 = oldMap.get(str1[k]);
							}
							if(o1!=null && !"".equals(o1)){
								moneys1 = o1.toString();
							}
							Object o3 = bindMap.get(str1[k]);
							if(o3!=null && !"".equals(o3)){
								moneys1 = new BigDecimal(moneys1).add(new BigDecimal(o3.toString())).toString();
							}
							oldMap.put(str1[k],moneys1);
						}
						accBalanceMap.put(bindClassCode.substring(0,j*5),oldMap);
					}
				}
        	}
		}
		
		//���ڼ�֮ǰδ���˵����ݷ���map��
		if(noBindInitMap != null && noBindInitMap.size()>0){
        	/**
        	 * ��ѯ�Ļ���ڼ�֮ǰ�д���δ���˵�����
        	 */
        	Iterator nobindMap = noBindInitMap.entrySet().iterator();
        	//����map�õ����������
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//�õ��������ݵ�map
				if(bindMap != null && bindMap.size()>0){
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					String[] str1 = new String[]{"PeriodIniBase","PeriodIni"};
					String[] str2 = new String[]{"PeriodDCBalaBase","PeriodDCBala"};
					
					//���бұ����ʽ
					if("all".equals(currencyValue)){
						String[] currS1 = new String[currList.size()+1];
						String[] currS2 = new String[currList.size()+1];
						int counts = 0;
						for(int k = 1;k<str1.length;k++){
							for(int l=0;l<currList.size();l++){
								String[] currStr = (String[])currList.get(l);
								if("true".equals(currStr[2])){
									//��λ��
									currS1[counts] = str1[k]+"_";
									currS2[counts] = str2[k]+"_";
								}else{
									currS1[counts] = str1[k]+"_"+currStr[0];
									currS2[counts] = str2[k]+"_"+currStr[0];
								}
								counts ++;
							}
						}
						int curCount = currList.size();
						currS1[curCount] = "PeriodIniBase";
						currS2[curCount] = "PeriodDCBalaBase";
						str1 = currS1;
						str2 = currS2;
					}
					for(int j=1;j<=bindClassCode.length()/5;j++){
						HashMap oldMap = (HashMap)accBalanceMap.get(bindClassCode.substring(0,j*5));
						if(oldMap == null){
							oldMap = new HashMap();
							oldMap.put("classCode", bindClassCode.substring(0,j*5));
						}
						for(int k=0;k<str1.length;j++){
							String moneys1 = "0";
							Object o1 = null;
							if(oldMap!=null && oldMap.size()>0){
								o1 = oldMap.get(str1[k]);
							}
							if(o1!=null && !"".equals(o1)){
								moneys1 = o1.toString();
							}
							Object o3 = bindMap.get(str2[k]);
							if(o3!=null && !"".equals(o3)){
								if(bindMap.get("JdFlag") == null){
									continue;
								}
								if(Integer.parseInt(bindMap.get("JdFlag").toString()) == 1){
									moneys1 = new BigDecimal(moneys1).add(new BigDecimal(o3.toString())).toString();
								}else if(Integer.parseInt(bindMap.get("JdFlag").toString()) == 2){
									moneys1 = new BigDecimal(moneys1).subtract(new BigDecimal(o3.toString())).toString();
								}
							}
							oldMap.put(str1[k],moneys1);
						}
						accBalanceMap.put(bindClassCode.substring(0,j*5),oldMap);
					}
				}
        	}
        }
		//��������
		int count = 0;
		List accList = new ArrayList();
		HashMap totalMap = new HashMap();
		for(int i=0;i<accTypeInfoList.size();i++){
			//ѭ����ƿ�Ŀ�����ڳ����ͱ��ڷ������ĩ������ݵĴ���
			HashMap accmap = (HashMap)accTypeInfoList.get(i);
			HashMap hashMap = (HashMap)accBalanceMap.get(accmap.get("classCode").toString());				//�õ�����ƿ�Ŀ�Ľ������
			if(hashMap != null && hashMap.size()>0){
				String[] str1 = null;
				if("all".equals(currencyValue)){
					str1 = new String[currList.size()+1];
					for(int l = 0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//��λ��
							str1[l] = "_";
						}else{
							str1[l] = "_"+currStr[0];
						}
					}
					str1[currList.size()] = "Base";
				}else{
					str1 = new String[]{"","Base"};
				}
				for(String s : str1){
					Object o = hashMap.get("PeriodIni"+s);													//�ڳ����
					if(o!=null && !"".equals(o)){
						//���ݻ���ڼ�ķ����������ڳ�����ڽ跽�����ڴ���
						if(Integer.parseInt(accmap.get("jdFlag").toString()) == 1){
							accmap.put("PeriodIniDebit"+s, dealDataDouble(String.valueOf(o), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}else if(Integer.parseInt(accmap.get("jdFlag").toString()) == 2){
							accmap.put("PeriodIniCredit"+s, dealDataDouble(String.valueOf(o), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
							o = (Object)(0-Double.parseDouble(o.toString()));
						}
					}
					//�����ڳ����
					accmap.put("PeriodIni"+s, o);
					
					//�����ڽ跽�����ڴ������
					String money = "0";
					String[] strs = new String[]{"PeriodDebitSum"+s,"PeriodCreditSum"+s};
					for(String str : strs){
						money = "0";
						o = hashMap.get(str);
						if(o != null && !"".equals(o)){
							money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
						}
						accmap.put(str, dealDataDouble(String.valueOf(money), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					}
					
					//��ĩ���ڳ����+���ڽ跽-���ڴ�����
					money = "0";
					String jdFlag = accmap.get("jdFlag").toString();
					
					o = accmap.get("PeriodIni"+s);
					if(o!=null && !"".equals(o)){
						money = o.toString();
					}
					for(String str : strs){
						o = accmap.get(str);
						if(o != null && !"".equals(o)){
							String newStr = "PeriodCreditSum"+s;
							if(newStr.equals(str)){
								money = new BigDecimal(money).subtract(new BigDecimal(o.toString())).toString();
							}else{
								money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
							}
						}
					}
					
					if(Integer.parseInt(accmap.get("jdFlag").toString()) == 1){
						accmap.put("PeriodDebitBala"+s, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));						
					}else if(Integer.parseInt(accmap.get("jdFlag").toString()) == 2){
						//money = String.valueOf((0-Double.parseDouble(money)));
						money = new BigDecimal(0).subtract(new BigDecimal(money)).toString();
						accmap.put("PeriodCreditBala"+s, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));						
					}
					accmap.put("PeriodBala"+s, money);						//��ĩ���Ľ��
				}
			}
			
			/* ͳ�ƺϼƽ�� */
			String[] strs = new String[]{"PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase",
					"PeriodIniDebit","PeriodIniCredit","PeriodDebitSum","PeriodCreditSum","PeriodDebitBala","PeriodCreditBala"};
			if("all".equals(currencyValue)){
				String[] currS = new String[(currList.size()+1)*6];
				int counts = 0;
				for(int k = 6;k<strs.length;k++){
					for(int l=0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//��λ��
							currS[counts] = strs[k]+"_";
						}else{
							currS[counts] = strs[k]+"_"+currStr[0];
						}
						counts ++;
					}
				}
				int curCount = currList.size()*6;
				for(int k=0;k<strs.length-6;k++){
					currS[curCount+k] = strs[k];
				}
				strs = currS;
			}
			if(Integer.parseInt(accmap.get("isCatalog").toString()) == 0 && Integer.parseInt(accmap.get("isCalculateParent").toString())!=1){
				for(String str : strs){
					String money = "0";
					Object o = totalMap.get(str);
					if(o!=null && !"".equals(o)){
						money = o.toString();
					}
					o = accmap.get(str);
					if(o!=null && !"".equals(o)){
						money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
					}
					totalMap.put(str, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
				}
			}
			
			//��Ŀ�ȼ�
			if(levelStart!=null && !"".equals(levelStart)){
				String classCodes = accmap.get("classCode").toString();
				if(classCodes.length()/5-1>Integer.parseInt(levelStart)){
					continue;
				}
			}
			
			if(showItemDetail == null || "".equals(showItemDetail)){
				//����ʾ������Ŀ��ϸ
				if(String.valueOf(accmap.get("isCalculate").toString()).equals("1")){
					continue;
				}
			}
			if(showDetail !=null && !"".equals(showDetail)){
				//ֻ��ʾ��ϸ��Ŀ
				if(Integer.parseInt(accmap.get("isCatalog").toString()) == 0 && Integer.parseInt(accmap.get("isCalculateParent").toString())!=1){
					String classCodes = accmap.get("classCode").toString();					
					if(accList!=null && accList.size()>0){
						for(int j = 0;j<accList.size();j++){
							HashMap newmap = (HashMap)accList.get(j);
							if(newmap.get("classCode").equals(classCodes.substring(0, classCodes.length()-5)) || newmap.get("classCode").equals(classCodes.substring(0, classCodes.length()-10))){
								//���ڸ���
								accList.remove(j);
							}
						}						
					}
					
				}
			}
			accList.add(accmap);
		}
		
		//��Ŀ�޷������ʾ
		List accLists = new ArrayList();
		for(int i=0;i<accList.size();i++){
			HashMap accMap = (HashMap)accList.get(i);
			Object sumAmount = accMap.get("PeriodIniBase");								//�ڳ����
			if(accTypeNoItem!=null && !"".equals(accTypeNoItem)){
				Object sumdebit = accMap.get("PeriodDebitSumBase");
				Object sumlend = accMap.get("PeriodCreditSumBase");
				if(!((sumAmount==null || "".equals(sumAmount) || "0E-8".equals(sumAmount) || Double.valueOf(sumAmount.toString())==0) && 
						(sumdebit==null || "".equals(sumdebit) || "0E-8".equals(sumdebit) || Double.valueOf(sumdebit.toString())==0) && 
						(sumlend==null || "".equals(sumlend) || "0E-8".equals(sumlend) || Double.valueOf(sumlend.toString())==0))){
					accLists.add(accMap);
				}
			}else{
				accLists = accList;
			}
		}
			
		/* ����ƽ�⴦�� */
		String money = "0";
		Object sumAmountEnd = totalMap.get("PeriodDebitBalaBase");
		if(sumAmountEnd!=null && !"".equals(sumAmountEnd)){
			money = new BigDecimal(money).add(new BigDecimal(sumAmountEnd.toString())).toString();
		}
		sumAmountEnd = totalMap.get("PeriodCreditBalaBase");
		if(sumAmountEnd!=null && !"".equals(sumAmountEnd)){
			money = new BigDecimal(money).subtract(new BigDecimal(sumAmountEnd.toString())).toString();
		}
		/**
		 * ����ƽ���ж�
		 */
		if(Double.valueOf(money)==0){
			//����ƽ��
			totalMap.put("flag", "true");
		}else{
			//���㲻ƽ��
			totalMap.put("flag", "false");
		}
		obj = new Object[]{accLists,totalMap,currencyValue};
		result.setRetVal(obj);
		
		return result;
	}
	
	
	/**
	 * ��Ŀ����
	 * 1.��ѯ�������������Ļ�ƿ�Ŀ
	 * 2.�ӿ�Ŀ�����ѯ�Ѿ����˵�����
	 * 3.��ѯδ����ƾ֤���ݽ��
	 * 4.��������
	 * @param conMap
	 * @return
	 */
	public Result accTypeInfoBalance(final HashMap<String,String> conMap,final LoginBean loginBean,final MOperation mop){
		final Result result = new Result();
		
		//�Աұ���д���
		String currencyName = conMap.get("currencyName"); 									//�ұ�('isBase'=��λ�ң�''=�ۺϱ�λ�ң�'all'=���бұ�'��ҵ�id'=���)
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		//�������
		rs = queryCurrencyAll();
		List<String[]> currList = (ArrayList<String[]>)rs.retVal;
		for(String[] ss:currList){
			if(ss[2].equals("true")){
				currList.remove(ss);
				break;
			}
		}
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		final String periodYearStart = conMap.get("periodYearStart");
		final String periodStart = conMap.get("periodStart");
		final String periodYearEnd = conMap.get("periodYearEnd");
		final String periodEnd = conMap.get("periodEnd");
		final String accTypeNoItem = conMap.get("accTypeNoItem");									//��Ŀ�޷������ʾ
		final String levelStart = conMap.get("levelStart");
		String showItemDetail = conMap.get("showItemDetail");
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							/* �������� */
							String binderNo = conMap.get("binderNo");
							String acctypeCodeStart = conMap.get("acctypeCodeStart");
							String acctypeCodeEnd = conMap.get("acctypeCodeEnd");
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");
							
							/* �ڼ䴦�� */
							String startTime = "";
							String endTime = "";
							if(Integer.valueOf(periodStart)<10){
								startTime = periodYearStart+"-0"+periodStart;
							}else{
								startTime = periodYearStart+"-"+periodStart;
							}
							if(Integer.valueOf(periodEnd)<10){
								endTime = periodYearEnd+"-0"+periodEnd;
							}else{
								endTime = periodYearEnd+"-"+periodEnd;
							}
							
							/* ��ǰ����ڼ� */
							Hashtable sessionSet = BaseEnv.sessionSet;
							Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
							AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
							
							HashMap accMap = new HashMap();
							
							/**
                             * �ڼ��������
                             */
                            String condition = scopeSql(mop, loginBean);;
                            
							/**
							 * ��ѯ�������������Ļ�ƿ�Ŀ
							 */
							StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,l.zh_cn as AccFullName,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,");
							sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.jdFlag FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 ");
							if(acctypeCodeStart != null && !"".equals(acctypeCodeStart)){
								//��Ŀ��ʼ
								condition += " AND (tblAccTypeInfo.AccNumber>='"+acctypeCodeStart+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeStart+"')+'%')";
							}
							if(acctypeCodeEnd != null && !"".equals(acctypeCodeEnd)){
								//��Ŀ����
								condition += " AND (tblAccTypeInfo.AccNumber<='"+acctypeCodeEnd+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeEnd+"')+'%')";
							}
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//����ʾ���ÿ�Ŀ
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							sql.append(condition);
							sql.append(" ORDER BY tblAccTypeInfo.AccNumber");
							BaseEnv.log.info("��Ŀ��ѯSQL��"+sql.toString());
                            /* ִ�в�ѯ���ұ������������Ŀ�Ŀ */
                            rset=st.executeQuery(sql.toString());
                            List accTypeInfoList = new ArrayList();
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rset.getMetaData().getColumnName(i), 0);
                            			}else{
                              				map.put(rset.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	accMap.put(map.get("classCode")+"_isCalculate", map.get("isCalculate"));
                            	accMap.put(map.get("classCode")+"_jdFlag", map.get("jdFlag"));
                            	accMap.put(map.get("classCode")+"_AccNumber", map.get("AccNumber"));
                            	// accMap.put("classCode",map.get("classCode"));
                            	accTypeInfoList.add(map);
                            }
                            
                            /**
                             * ��ѡ�ˣ�����δ����ƾ֤������ѯָ���ڼ��δ�������ݽ��
                             */
                            String str = "";
                            HashMap noBindMap = new HashMap();				//�����ڼ��е�δ���˵�����
                            HashMap noBindInitMap = new HashMap();			//���濪ʼ�ڼ�֮ǰδ���˵�����
							if(binderNo != null && !"".equals(binderNo)){
								String initSql = "";						//��ѯ�ڳ�δ��������sql
	                            sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,SUM(DebitAmount) AS PeriodDebitSumBase,");
	                            sql.append("SUM(LendAmount) AS PeriodCreditSumBase,SUM(DebitAmount)-SUM(LendAmount) AS PeriodDCBalaBase, ");
	                            sql.append("SUM(DebitAmount) AS CurrYIniDebitSumBase,SUM(LendAmount) AS CurrYIniCreditSumBase,SUM(DebitAmount)-SUM(LendAmount) AS CurrYIniAmount ");
	                            if(!"".equals(currencyValue)){
	                            	sql.append(",SUM(DebitCurrencyAmount) AS PeriodDebitSum,SUM(LendCurrencyAmount) AS PeriodCreditSum,SUM(DebitCurrencyAmount)-SUM(LendCurrencyAmount) AS PeriodDCBala ");
	                            	sql.append(",SUM(DebitCurrencyAmount) AS CurrYIniDebitSum,SUM(LendCurrencyAmount) AS CurrYIniCreditSum,SUM(DebitCurrencyAmount)-SUM(LendCurrencyAmount) AS CurrYIni ");
	                            	if("all".equals(currencyValue)){
	                            		sql.append(",isnull(tblAccDetail.Currency,'') as Currency ");
	                            	}
	                            }
	                            sql.append("FROM  tblAccDetail ");
	                            sql.append("LEFT JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id LEFT JOIN tblAccTypeInfo tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode ");
	                            sql.append("WHERE tblAccMain.workFlowNodeName!='finish' ");
	                            //��ѡ����Ҳ���<�ۺϱ�λ��><���б���>ʱ���й���
	                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
	                            	if("isBase".equals(currencyValue)){
	                            		//��λ��
	                            		sql.append(" AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+searchCurrency+"') ");
	                            	}else{
	                            		sql.append(" AND tblAccDetail.Currency='"+searchCurrency+"' ");
	                            	}
	                            }
	                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
	                            initSql = sql.toString()+" AND "+str+"<'"+startTime+"' "+condition+" GROUP BY tblAccTypeInfo.classCode,tblAccTypeInfo.JdFlag";
	                            sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
	                            sql.append(condition);
	                            sql.append(" GROUP BY tblAccTypeInfo.classCode");
	                            if("all".equals(currencyValue)){
	                            	//���б���Ҫ����ҽ��з���
	                            	sql.append(",tblAccDetail.Currency");
	                            	initSql += ",tblAccDetail.Currency";
	                            }
	                            rset = st.executeQuery(sql.toString());
	                            while (rset.next()) {
	                            	HashMap periodMap=new HashMap();
	                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                            		Object obj=rset.getObject(i);
	                            		if(obj==null){
	                            			periodMap.put(rset.getMetaData().getColumnName(i), "");
	                            		}else{
	                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                            					String strvalue = String.valueOf(obj);
                            					if (strvalue.indexOf(".")>0){
                            						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                            					}
                            					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                            						strvalue = "";
                            					}
                            					periodMap.put(rset.getMetaData().getColumnName(i), strvalue);
	                            			}else{
	                            				periodMap.put(rset.getMetaData().getColumnName(i), obj);
	                            			}
	                            		}
	                            	}
	                            	//��ѡ��ıұ��ǡ������бұ����ʽ
	                            	if("all".equals(currencyValue)){
	                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
	                            		if("".equals(periodMap.get("Currency"))){
	                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
	                            			for(String s : curs){
	                            				if("CurrYIni".equals(s)){
	                            					periodMap.put(s+"_", periodMap.get(s+"Amount"));
	                            				}else{
	                            					periodMap.put(s+"_", periodMap.get(s+"Base"));
	                            				}
	                            			}
	                            		}else{
	                            			//�������
	                            			for(String s : curs){
	                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
	                            			}
	                            		}
	                            	}
	                            	noBindMap.put(periodMap.get("classCode"), periodMap);
	                            }
	                            
	                            //��ѯ�ڳ�����ʱ���в�ѯ����ڼ�֮ǰδ���˵����ݽ���ͳ��
	                            initSql = initSql.replaceAll("SELECT ", "SELECT tblAccTypeInfo.JdFlag,");
	                            rset = st.executeQuery(initSql);
	                            while (rset.next()) {
	                            	HashMap periodMap=new HashMap();
	                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                            		Object obj=rset.getObject(i);
	                            		if(obj==null){
	                            			periodMap.put(rset.getMetaData().getColumnName(i), "");
	                            		}else{
	                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                            					String strvalue = String.valueOf(obj);
                            					if (strvalue.indexOf(".")>0){
                            						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                            					}
                            					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                            						strvalue = "";
                            					}
                            					periodMap.put(rset.getMetaData().getColumnName(i), strvalue);
	                            			}else{
	                            				periodMap.put(rset.getMetaData().getColumnName(i), obj);
	                            			}
	                            		}
	                            	}
	                            	//��ѡ��ıұ��ǡ������бұ����ʽ
	                            	if("all".equals(currencyValue)){
	                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
	                            		if("".equals(periodMap.get("Currency"))){
	                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
	                            			for(String s : curs){
	                            				if("CurrYIni".equals(s)){
	                            					periodMap.put(s+"_", periodMap.get(s+"Amount"));
	                            				}else{
	                            					periodMap.put(s+"_", periodMap.get(s+"Base"));
	                            				}
	                            			}
	                            		}else{
	                            			//�������
	                            			for(String s : curs){
	                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
	                            			}
	                            		}
	                            	}
	                            	noBindInitMap.put(periodMap.get("classCode"), periodMap);
	                            }
							}
                            
                            /**
                             * ��ѯ��Ŀ����Ľ�tblAccBalance��
                             */
                            sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccBalance.Nyear,");
                            sql.append("tblAccBalance.Period,tblAccBalance.SubCode,ISNULL(tblAccTypeInfo.isCataLog,0) as isCataLog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccBalance.PeriodIniBase,");
                            sql.append("tblAccBalance.PeriodIni,tblAccBalance.PeriodDebitSumBase,tblAccBalance.PeriodDebitSum,tblAccBalance.PeriodCreditSumBase,");
                            sql.append("tblAccBalance.PeriodCreditSum,tblAccBalance.CurrYIniDebitSumBase,tblAccBalance.CurrYIniDebitSum,tblAccBalance.CurrYIniCreditSumBase,");
                            sql.append("tblAccBalance.CurrYIniCreditSum,tblAccBalance.PeriodBalaBase,tblAccBalance.PeriodBala,");
                            sql.append("(tblAccBalance.CurrYIniDebitSumBase-tblAccBalance.CurrYIniCreditSumBase) as CurrYIniAmount,");
                            sql.append("(tblAccBalance.CurrYIniDebitSum-tblAccBalance.CurrYIniCreditSum) as CurrYIni,");
                            sql.append("(tblAccBalance.PeriodDebitSumBase-tblAccBalance.PeriodCreditSumBase) AS PeriodDCBalaBase,");
                            sql.append("(tblAccBalance.PeriodDebitSum-tblAccBalance.PeriodCreditSum) AS PeriodDCBala,");
                            sql.append("tblAccTypeInfo.jdFlag,isnull(tblAccBalance.CurType,'') as CurType ");
                            sql.append("FROM tblAccBalance left join tblAccTypeInfo ON tblAccBalance.SubCode=tblAccTypeInfo.AccNumber WHERE ");
                            sql.append(" tblAccBalance.Nyear!=-1 AND tblAccBalance.period!=-1");
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
                            sql.append(condition);
                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccBalance.Nyear)+'-'+CONVERT(VARCHAR,tblAccBalance.Period)+'-01',120),120)";
                            sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"' AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1 AND (tblAccBalance.DepartmentCode is null or tblAccBalance.DepartmentCode='')");
                            sql.append(" ORDER BY tblAccTypeInfo.classCode,tblAccBalance.Nyear,tblAccBalance.Period");
                            BaseEnv.log.info("��ѯ��Ŀ�����"+sql.toString());
                            rset=st.executeQuery(sql.toString());
                            HashMap accBalanceMap = new HashMap();
                            String accClassCode = "";
                            String classCode_Old = "";
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "0";
                        					}
                        					if(Double.valueOf(strvalue)==0){
                        						strvalue = "";
                        					}
                            				map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	
                            	//��ѡ��ıұ��ǡ������бұ����ʽ
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodIni","PeriodDebitSum","PeriodCreditSum","CurrYIniDebitSum","CurrYIniCreditSum","PeriodBala","CurrYIni","PeriodDCBala"};
                            		if("".equals(map.get("CurType"))){
                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					map.put(s+"_", map.get(s+"Amount"));
                            				}else{
                            					map.put(s+"_", map.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//�������
                            			for(String s : curs){
                            				map.put(s+"_"+map.get("CurType"), map.get(s));
                            			}
                            		}
                            	}
                            	
                            	HashMap oldAccMap = (HashMap)accBalanceMap.get(map.get("classCode"));
                            	HashMap tempMap=(HashMap)map.clone();
                            	if(oldAccMap != null){
                            		//�ڳ�ȡ��һ���ڼ��
                            		if(accClassCode.equals(map.get("classCode"))){
                            			map.put("PeriodIniBase",oldAccMap.get("PeriodIniBase"));
                            			map.put("PeriodIni",oldAccMap.get("PeriodIni"));
                            			map.put("PeriodIni_"+map.get("CurType"), oldAccMap.get("PeriodIni_"+map.get("CurType")));
                            		}
                            		
                            		
                            		//���ö���ڼ���в�ѯʱ�����ڽ����Ҫ�����ۼ�
                            		String[] strs = null;
                            		if("all".equals(currencyValue)){
                            			strs = new String[]{"PeriodDebitSumBase","PeriodDebitSum_"+map.get("CurType"),"PeriodCreditSumBase","PeriodCreditSum_"+map.get("CurType"),"PeriodDCBalaBase","PeriodDCBala_"+map.get("CurType"),"PeriodBalaBase","PeriodBala_"+map.get("CurType")};
                            		}else{
                            			strs = new String[]{"PeriodDebitSumBase","PeriodDebitSum","PeriodCreditSumBase","PeriodCreditSum","PeriodDCBalaBase","PeriodDCBala","PeriodBalaBase","PeriodBala"};
                            		}
                            		
                            		for(String s : strs){
                            			String moneys = "0";
                            			Object os = oldAccMap.get(s);
    									if(os!=null && !"".equals(os)){
    										moneys = os.toString();
    									}
    									if(map.get(s)!=null && !"".equals(map.get(s))){
    										moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
    									}
    									if(Double.parseDouble(moneys)==0){
    										moneys = "";
    									}
    									map.put(s, moneys);
                            		}
                            	}
                            	accClassCode = String.valueOf(map.get("classCode"));
                				//���ϼ���ֵ
            					String classCode = map.get("classCode").toString();
            					String jdFlag = map.get("jdFlag").toString();
            					accBalanceMap.put(classCode,map);
            					for(int j=1;j<classCode.length()/5;j++){
            						HashMap oldMap = (HashMap)accBalanceMap.get(classCode.substring(0,j*5));
        							String[] moneyStr = null;
        							if("all".equals(currencyValue)){
        								moneyStr = new String[]{"PeriodIniBase","PeriodIni_"+map.get("CurType"),"PeriodDebitSumBase","PeriodDebitSum_"+map.get("CurType"),"PeriodCreditSumBase","PeriodCreditSum_"+map.get("CurType"),"CurrYIniDebitSumBase","CurrYIniDebitSum_"+map.get("CurType")
        										,"CurrYIniCreditSumBase","CurrYIniCreditSum_"+map.get("CurType"),"PeriodBalaBase","PeriodBala_"+map.get("CurType"),"CurrYIniAmount","CurrYIni_"+map.get("CurType"),"PeriodDCBalaBase","PeriodDCBala_"+map.get("CurType")};
        							}else{
        								moneyStr = new String[]{"PeriodIniBase","PeriodIni","PeriodDebitSumBase","PeriodDebitSum","PeriodCreditSumBase","PeriodCreditSum","CurrYIniDebitSumBase","CurrYIniDebitSum"
        										,"CurrYIniCreditSumBase","CurrYIniCreditSum","PeriodBalaBase","PeriodBala","CurrYIniAmount","CurrYIni","PeriodDCBalaBase","PeriodDCBala"};
        							}
        							String topjdFlag = String.valueOf(accMap.get(classCode.substring(0,j*5)+"_jdFlag"));
        							if(oldMap!=null && oldMap.size()>0){
        								for(String s : moneyStr){
        									String moneys = "0";
        									Object o = oldMap.get(s);
        									if(o!=null && !"".equals(o)){
        										moneys = o.toString();
        									}
        									if((tempMap.get(s)!=null && !"".equals(tempMap.get(s))) || (s.contains("CurrYIniDebit")||s.contains("CurrYIniCredit"))){
        										if(s.indexOf("PeriodIni") != -1 && classCode.equals(classCode_Old)){
            										continue;
            									}
        										if(jdFlag.equals(topjdFlag)||s.contains("Debit")||s.contains("Credit")){
        											//��������ۼƽ������Ҫ��ȥ��ǰ��Ŀ��֮ǰ�ڼ�����ۼƽ�����.(��ǰ�ڼ�����ۼƽ���Ѿ�������֮ǰ�ڼ��)
        											if((s.contains("CurrYIniDebit")||s.contains("CurrYIniCredit"))&&oldAccMap!=null&&oldAccMap.get(s)!=null && !"".equals(oldAccMap.get(s))){
        												moneys = new BigDecimal(moneys).subtract(new BigDecimal(oldAccMap.get(s).toString())).toString();
        											}
        											
        											moneys = new BigDecimal(moneys).add(new BigDecimal(tempMap.get(s).toString().equals("")?"0":tempMap.get(s).toString())).toString();	
        										}else{
        											moneys = new BigDecimal(moneys).subtract(new BigDecimal(tempMap.get(s).toString())).toString();
        										}
        									}
        									String totalAmount = moneys;
        									if(Double.parseDouble(moneys)==0){
        										totalAmount = "";
        									}
        									oldMap.put(s,totalAmount);
        									if(classCode.substring(0,j*5).equals("0000600007") && s.equals("CurrYIniCreditSumBase")){
        										System.out.println(classCode+":"+totalAmount+":"+map.get("Period")+":"+s+":"+tempMap.get(s));
        									}
        								}
        								accBalanceMap.put(classCode.substring(0,j*5),oldMap);
        							}else{
        								//����������һ��ʱ�������µ�Map��������
        								oldMap = new HashMap();
        								oldMap.put("Nyear", map.get("Nyear"));
        								oldMap.put("Period", map.get("Period"));
        								Object accNumber = accMap.get(classCode.substring(0,j*5)+"_AccNumber");
        								oldMap.put("SubCode", accNumber);
        								oldMap.put("classCode", classCode.substring(0,j*5));
        								for(String s : moneyStr){
        									if(s.indexOf("PeriodIni") != -1 && classCode.equals(classCode_Old)){
        										continue;
        									}
        									String totalAmount = "0";
	        								if(map.get(s) != null && !"".equals(map.get(s))){
	        									if(jdFlag.equals(topjdFlag)||s.contains("Debit")||s.contains("Credit")){
	        										totalAmount = map.get(s).toString();
	        									}else{
	        										totalAmount = String.valueOf(0-Double.parseDouble(map.get(s).toString()));
	        									}
	        								}
	        								if(Double.valueOf(totalAmount)==0){
	        									totalAmount = "";
	        								}
	        								oldMap.put(s,totalAmount);
        								}
        								accBalanceMap.put(classCode.substring(0,j*5),oldMap);
        							}
            					}
            					classCode_Old = classCode;
                            }
                            
                            /* ����ѯ���ڼ��ڱ�tblAccBalance������ʱ��ʹ�ڼ���ǰ�ƣ���ѯ����ļ�¼ */
                            sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccBalance.Nyear,");
                            sql.append("tblAccBalance.Period,tblAccBalance.SubCode,ISNULL(tblAccTypeInfo.isCataLog,0) as isCataLog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,");
                            sql.append("tblAccBalance.CurrYIniBalaBase as PeriodIniBase,tblAccBalance.CurrYIniBala as PeriodIni,");
                            sql.append("tblAccBalance.CurrYIniDebitSumBase,tblAccBalance.CurrYIniDebitSum,tblAccBalance.CurrYIniCreditSumBase,tblAccBalance.CurrYIniCreditSum,");
                            sql.append("(tblAccBalance.CurrYIniDebitSumBase-tblAccBalance.CurrYIniCreditSumBase) as CurrYIniAmount,");
                            sql.append("(tblAccBalance.CurrYIniDebitSum-tblAccBalance.CurrYIniCreditSum) as CurrYIni,");
                            sql.append(" '' AS PeriodDCBalaBase,'' AS PeriodDCBala,tblAccTypeInfo.jdFlag,isnull(tblAccBalance.CurType,'') as CurType ");
                            sql.append(" FROM tblAccBalance left join tblAccTypeInfo ON tblAccBalance.SubCode=tblAccTypeInfo.AccNumber WHERE ");
                            sql.append(" SubCode not in (select SubCode from tblAccBalance where "+str+"='"+startTime+"' AND Nyear!=-1 AND Period!=-1) AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1 AND (tblAccBalance.DepartmentCode is null or tblAccBalance.DepartmentCode='')");
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
                            sql.append(" AND tblAccBalance.Nyear =(select top 1 a.Nyear from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
							sql.append(" AND tblAccBalance.Period =(select top 1 a.Period from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
                            sql.append(condition + " ORDER BY tblAccTypeInfo.classCode");
                            BaseEnv.log.info("��ѯ��Ŀ������ӽ������ݣ�"+sql.toString());
                            rset=st.executeQuery(sql.toString());
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "0";
                        					}
                        					if(Double.valueOf(strvalue)==0){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	
                            	//��ѡ��ıұ��ǡ������бұ����ʽ
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodIni","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni","PeriodDCBala"};
                            		if("".equals(map.get("CurType"))){
                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					map.put(s+"_", map.get(s+"Amount"));
                            				}else{
                            					map.put(s+"_", map.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//�������
                            			for(String s : curs){
                            				map.put(s+"_"+map.get("CurType"), map.get(s));
                            			}
                            		}
                            	}
                            	
                				//���ϼ���ֵ
            					String classCode = map.get("classCode").toString();
            					for(int j=1;j<classCode.length()/5;j++){
            						HashMap oldMap = (HashMap)accBalanceMap.get(classCode.substring(0,j*5));
        							int count = 0;
        							String[] moneyStr = null;
        							if("all".equals(currencyValue)){
        								moneyStr = new String[]{"PeriodIniBase","PeriodIni_"+map.get("CurType"),"CurrYIniDebitSumBase","CurrYIniDebitSum_"+map.get("CurType"),"CurrYIniCreditSumBase","CurrYIniCreditSum_"+map.get("CurType"),"CurrYIniAmount","CurrYIni_"+map.get("CurType")};
        							}else{
        								moneyStr = new String[]{"PeriodIniBase","PeriodIni","CurrYIniDebitSumBase","CurrYIniDebitSum","CurrYIniCreditSumBase","CurrYIniCreditSum","CurrYIniAmount","CurrYIni"};
        							}
        							if(oldMap!=null && oldMap.size()>0){
        								String moneys = "0";
        								for(String s : moneyStr){
        									moneys = "0";
        									Object o = oldMap.get(s);
        									if(o!=null && !"".equals(o)){
        										moneys = o.toString();
        									}
        									if(map.get(s)!=null && !"".equals(map.get(s))){
        										if(s.indexOf("PeriodIni") != -1){
        											if(accMap.get(classCode.substring(0,j*5)+"_jdFlag")!=null){
        												int oldjdFlag = Integer.parseInt(accMap.get(classCode.substring(0,j*5)+"_jdFlag").toString());
        												if(oldjdFlag == Integer.parseInt(map.get("jdFlag").toString())){
        													//�跽
        													moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();
        												}else{
        													moneys = new BigDecimal(moneys).subtract(new BigDecimal(map.get(s).toString())).toString();
        												}
        											}
        										}else{
        											moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
        										}
        									}
        									String totalAmount = moneys;
        									if(Double.parseDouble(moneys)==0){
        										totalAmount = "";
        									}
        									oldMap.put(s,totalAmount);
        								}
        								accBalanceMap.put(classCode.substring(0,j*5),oldMap);
        							}else{
        								//����������һ��ʱ�������µ�Map��������
        								oldMap = new HashMap();
        								oldMap.put("Nyear", map.get("Nyear"));
        								oldMap.put("Period", map.get("Period"));
        								Object accnumber = accMap.get(classCode.substring(0,j*5)+"_AccNumber");
        								oldMap.put("SubCode", accnumber);
        								oldMap.put("classCode", classCode.substring(0,j*5));
        								for(String s : moneyStr){
        									String totalAmount = "0";
	        								if(map.get(s) != null && !"".equals(map.get(s))){
	        									if(s.indexOf("PeriodIni") != -1){
        											if(accMap.get(classCode.substring(0,j*5)+"_jdFlag")!=null){
        												int oldjdFlag = Integer.parseInt(accMap.get(classCode.substring(0,j*5)+"_jdFlag").toString());
        												if(oldjdFlag == Integer.parseInt(map.get("jdFlag").toString())){
        													//�跽
        													totalAmount = new BigDecimal(totalAmount).add(new BigDecimal(map.get(s).toString())).toString();
        												}else{
        													totalAmount = new BigDecimal(totalAmount).subtract(new BigDecimal(map.get(s).toString())).toString();
        												}
        											}
        										}else{
        											totalAmount = map.get(s).toString();
        										}
	        								}
	        								if(Double.valueOf(totalAmount)==0){
	        									totalAmount = "";
	        								}
	        								oldMap.put(s,totalAmount);
        								}
        								accBalanceMap.put(classCode.substring(0,j*5),oldMap);
        							}
            					}
                            	accBalanceMap.put(map.get("classCode"), map);
                            }
                            
							/* �������� */
							//accTypeInfoList ��ƿ�Ŀ�б�,accBalanceMap �ѹ��˵�����map,noBindMap δ���˵�map,noBindInitMap ��ѯ�ڼ�֮ǰ��δ��������
							Object obj = new Object[]{accTypeInfoList,accBalanceMap,noBindMap,noBindInitMap};
                            result.setRetVal(obj);
							
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt AccTypeInfoBalance:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		
		/* ��ȡ���� */
		Object[] obj = (Object[])result.retVal;
		List accTypeInfoList = (ArrayList)obj[0];
		HashMap	accBalanceMap = (HashMap)obj[1];
		HashMap noBindMap = (HashMap)obj[2];
		HashMap noBindInitMap = (HashMap)obj[3];
		
		String accTypeNoOperation = conMap.get("accTypeNoOperation");					//����û��ҵ�����Ŀ�Ŀ���ڳ��������ۼƣ�
		String accTypeNoPeriod = conMap.get("accTypeNoPeriod");							//��������û�з�����Ŀ�Ŀ
		String accTypeNoYear = conMap.get("accTypeNoYear");								//��������û�з�����Ŀ�Ŀ
		String takeBrowNo = conMap.get("takeBrowNo");									//�����޷�����Ŀ�Ŀ
		
		/**
		 * ��������
		 */
		
		//�����ڼ���δ��������
		if(noBindMap != null && noBindMap.size()>0){
			Iterator nobindMap = noBindMap.entrySet().iterator();
        	//����map�õ����������
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//�õ��������ݵ�map
				if(bindMap != null && bindMap.size()>0){
					//����δ���˵�����
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					for(int j=1;j<=bindClassCode.length()/5;j++){
						HashMap oldMap = (HashMap)accBalanceMap.get(bindClassCode.substring(0,j*5));
						
						String[] str1 = new String[]{"PeriodDebitSumBase","PeriodCreditSumBase","PeriodDCBalaBase","CurrYIniDebitSumBase","CurrYIniCreditSumBase",
								"CurrYIniAmount","PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
						if("all".equals(currencyValue)){
							String[] currS = new String[(currList.size()+1)*6];
							int counts = 0;
							for(int k = 6;k<str1.length;k++){
								for(int l=0;l<currList.size();l++){
									String[] currStr = (String[])currList.get(l);
									if("true".equals(currStr[2])){
										//��λ��
										currS[counts] = str1[k]+"_";
									}else{
										currS[counts] = str1[k]+"_"+currStr[0];
									}
									counts ++;
								}
							}
							int curCount = currList.size()*6;
							for(int k=0;k<str1.length-6;k++){
								currS[curCount+k] = str1[k];
							}
							str1 = currS;
						}
						if(oldMap == null){
							oldMap = new HashMap();
							oldMap.put("classCode", bindClassCode.substring(0,j*5));
						}
						for(int k=0;k<str1.length;k++){
							String moneys1 = "0";
							//������Ͻ��
							Object o1 = null;
							if(oldMap!=null && oldMap.size()>0){
								o1 = oldMap.get(str1[k]);
							}
							if(o1!=null && !"".equals(o1)){
								moneys1 = o1.toString();
							}
							Object o3 = bindMap.get(str1[k]);
							if(o3!=null && !"".equals(o3)){
								moneys1 = new BigDecimal(moneys1).add(new BigDecimal(o3.toString())).toString();
							}
							oldMap.put(str1[k],moneys1);
						}
						accBalanceMap.put(bindClassCode.substring(0,j*5),oldMap);
					}
				}
        	}
		}
		
		//���ڼ�֮ǰδ���˵����ݷ���map��
		if(noBindInitMap != null && noBindInitMap.size()>0){
        	/**
        	 * ��ѯ�Ļ���ڼ�֮ǰ�д���δ���˵�����
        	 */
        	Iterator nobindMap = noBindInitMap.entrySet().iterator();
        	//����map�õ����������
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//�õ��������ݵ�map
				if(bindMap != null && bindMap.size()>0){
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					String[] str1 = new String[]{"PeriodIniBase","PeriodIni"};
					String[] str2 = new String[]{"PeriodDCBalaBase","PeriodDCBala"};
					
					//���бұ����ʽ
					if("all".equals(currencyValue)){
						String[] currS1 = new String[currList.size()+1];
						String[] currS2 = new String[currList.size()+1];
						int counts = 0;
						for(int k = 1;k<str1.length;k++){
							for(int l=0;l<currList.size();l++){
								String[] currStr = (String[])currList.get(l);
								if("true".equals(currStr[2])){
									//��λ��
									currS1[counts] = str1[k]+"_";
									currS2[counts] = str2[k]+"_";
								}else{
									currS1[counts] = str1[k]+"_"+currStr[0];
									currS2[counts] = str2[k]+"_"+currStr[0];
								}
								counts ++;
							}
						}
						int curCount = currList.size();
						currS1[curCount+1] = "PeriodIniBase";
						currS2[curCount+1] = "PeriodDCBalaBase";
						str1 = currS1;
						str2 = currS2;
					}
					for(int j=1;j<=bindClassCode.length()/5;j++){
						HashMap oldMap = (HashMap)accBalanceMap.get(bindClassCode.substring(0,j*5));
						if(oldMap == null){
							oldMap = new HashMap();
							oldMap.put("classCode", bindClassCode.substring(0,j*5));
						}
						for(int k=0;k<str1.length;k++){
							String moneys1 = "0";
							Object o1 = null;
							if(oldMap!=null && oldMap.size()>0){
								o1 = oldMap.get(str1[k]);
							}
							if(o1!=null && !"".equals(o1)){
								moneys1 = o1.toString();
							}
							Object o3 = bindMap.get(str2[k]);
							if(o3!=null && !"".equals(o3)){
								if(bindMap.get("JdFlag") == null){
									continue;
								}
								if(Integer.parseInt(bindMap.get("JdFlag").toString()) == 1){
									moneys1 = new BigDecimal(moneys1).add(new BigDecimal(o3.toString())).toString();
								}else if(Integer.parseInt(bindMap.get("JdFlag").toString()) == 2){
									moneys1 = new BigDecimal(moneys1).subtract(new BigDecimal(o3.toString())).toString();
								}
							}
							oldMap.put(str1[k],moneys1);
						}
						accBalanceMap.put(bindClassCode.substring(0,j*5),oldMap);
					}
				}
        	}
        }
		
		//ѭ������
		int count = 0;
		List accList = new ArrayList();
		HashMap totalMap = new HashMap();
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap accmap = (HashMap)accTypeInfoList.get(i);
			//��ȡ�ʼ����ڼ�Ľ������
			HashMap hashMap = (HashMap)accBalanceMap.get(accmap.get("classCode").toString());
			if(hashMap != null && hashMap.size()>0){
				
				String[] str1 = null;
				if("all".equals(currencyValue)){
					str1 = new String[currList.size()+1];
					for(int l = 0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//��λ��
							str1[l] = "_";
						}else{
							str1[l] = "_"+currStr[0];
						}
					}
					str1[currList.size()] = "Base";
				}else{
					str1 = new String[]{"","Base"};
				}
				
				for(String s : str1){
					Object o = hashMap.get("PeriodIni"+s);
					if(o!=null && !"".equals(o)){
						//�ڳ����
						if(Integer.parseInt(accmap.get("jdFlag").toString()) == 1){
							accmap.put("PeriodIniDebit"+s, dealDataDouble(String.valueOf(o), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}else if(Integer.parseInt(accmap.get("jdFlag").toString()) == 2){
							accmap.put("PeriodIniCredit"+s, dealDataDouble(String.valueOf(o), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
							o = (Object)(0-Double.parseDouble(o.toString()));
						}
					}
					accmap.put("PeriodIni"+s, o);
					accmap.put("CurrYIniDebitSum"+s, dealDataDouble(String.valueOf(hashMap.get("CurrYIniDebitSum"+s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));		//�����ۼƽ跽
					accmap.put("CurrYIniCreditSum"+s, dealDataDouble(String.valueOf(hashMap.get("CurrYIniCreditSum"+s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));		//�����ۼƴ���
					
					//���ڽ跽�����ڴ���
					String money = "0";
					String[] strs = new String[]{"PeriodDebitSum"+s,"PeriodCreditSum"+s};
					for(String str : strs){
						money = "0";
						o = accmap.get(str);														//ԭ�����ڽ跽���
						if(o != null && !"".equals(o)){
							money = o.toString();
						}
						o = hashMap.get(str);
						if(o != null && !"".equals(o)){
							money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
						}
						accmap.put(str, dealDataDouble(String.valueOf(money), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					}
					
					//��ĩ���ڳ����+���ڽ跽-���ڴ�����
					money = "0";
					o = accmap.get("PeriodIni"+s);
					if(o!=null && !"".equals(o)){
						money = o.toString();
					}
					for(String str : strs){
						o = accmap.get(str);
						if(o != null && !"".equals(o)){
							String newStr = "PeriodCreditSum"+s;
							if(newStr.equals(str)){
								money = new BigDecimal(money).subtract(new BigDecimal(o.toString())).toString();
							}else{
								money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
							}
						}
					}
					if(Integer.parseInt(accmap.get("jdFlag").toString()) == 1){
						accmap.put("PeriodDebitBala"+s, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					}else if(Integer.parseInt(accmap.get("jdFlag").toString()) == 2){
						money = new BigDecimal("0").subtract(new BigDecimal(money)).toString();
						accmap.put("PeriodCreditBala"+s, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					}
					accmap.put("PeriodBala"+s, money);
				}
			}
			
			/* ͳ�ƺϼƽ�� */
			String[] strs = new String[]{"PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase",
					"CurrYIniDebitSumBase","CurrYIniCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase",
					"PeriodIniDebit","PeriodIniCredit","PeriodDebitSum","PeriodCreditSum","CurrYIniDebitSum","CurrYIniCreditSum",
					"PeriodDebitBala","PeriodCreditBala"};
			if("all".equals(currencyValue)){
				String[] currS = new String[(currList.size()+1)*8];
				int counts = 0;
				for(int k = 8;k<strs.length;k++){
					for(int l=0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//��λ��
							currS[counts] = strs[k]+"_";
						}else{
							currS[counts] = strs[k]+"_"+currStr[0];
						}
						counts ++;
					}
				}
				int curCount = currList.size()*8;
				for(int k=0;k<strs.length-8;k++){
					currS[curCount+k] = strs[k];
				}
				strs = currS;
			}
			
			if(Integer.parseInt(accmap.get("isCatalog").toString()) == 0 && Integer.parseInt(accmap.get("isCalculateParent").toString())!=1){
				for(String str : strs){
					String money = "0";
					Object o = accmap.get(str);
					if(o!=null && !"".equals(o)){
						money = o.toString();
					}
					o = totalMap.get(str);
					if(o!=null && !"".equals(o)){
						money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
					}
					totalMap.put(str, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
				}
			}
			
			String flags = new String();
			/* ��ҵ�����Ĳ���ʾ�ÿ�Ŀ */
			for(int k = 0;k<strs.length;k++){
				Object o = accmap.get(strs[k]);
				if(o == null || "".equals(o)){
					//�����ڼ�¼
					flags += ",true,";
				}else{
					flags += ",false,";
				}
			}
			if(takeBrowNo == null || "".equals(takeBrowNo)){
				boolean flag = false;
				/*û��ҵ�����Ŀ�Ŀ���ڳ��������ۼƣ�*/
				if(accTypeNoOperation!= null && !"".equals(accTypeNoOperation)){
					Object o1 = accmap.get("PeriodIniBase");
					Object o2 = accmap.get("CurrYIniDebitSumBase");
					Object o3 = accmap.get("CurrYIniCreditSumBase");
					if("".equals(o1) && "".equals(o2) && "".equals(o3)){
						flag = true;
					}
				}
				/* �����޷������Ŀ */
				if(accTypeNoPeriod != null && !"".equals(accTypeNoPeriod)){
					Object o1 = accmap.get("PeriodDebitSumBase");
					Object o2 = accmap.get("PeriodCreditSumBase");
					if("".equals(o1) && "".equals(o2)){
						flag = true;
					}
				}
				/* �����޷������Ŀ */
				if(accTypeNoYear != null && !"".equals(accTypeNoYear)){
					Object o1 = accmap.get("CurrYIniDebitSumBase");
					Object o2 = accmap.get("CurrYIniCreditSumBase");
					if("".equals(o1) && "".equals(o2)){
						flag = true;
					}
				}
				if(flag){
					accList.add(accmap);
					continue;
				}
				if(flags.indexOf("false")==-1){
					continue;
				}
			}
			boolean falg = true;
			String classCode = accmap.get("classCode").toString();
			if(levelStart != null && !"".equals(levelStart)){
				//��Ŀ�ȼ���ʼ
				if(classCode.length()/5-1<=Integer.parseInt(levelStart)){
					falg = true;
				}else{
					falg = false;
				}
			}
			
			if(showItemDetail == null || "".equals(showItemDetail)){
				//����ʾ������Ŀ��ϸ
				if(accmap.get("isCalculate")!= null && "1".equals(accmap.get("isCalculate"))){
					falg = false;
				}
			}
			if(falg){
				accList.add(accmap);
			}
		}
		obj = new Object[]{accList,totalMap,currencyValue};
		result.setRetVal(obj);
		return result;
	}
	
	
	public static String decimalFormart(String value){
		return new DecimalFormat("#.#########").format(value);
	}
	
	/**
	 * ��Ŀ�ձ���
	 * 1.��ѯ��ƿ�Ŀ
	 * 2.��ѯ���ս�������
	 * 3.��ѯ���ս�������
	 * 4.��ѯ�跽����/��������
	 * 5.��������
	 * @param conMap
	 * @return
	 */
	protected Result accTypeInfoDay(final HashMap<String,String> conMap, final MOperation mop, final LoginBean loginBean){
		//�Աұ���д���
		String currencyName = conMap.get("currencyName"); 									//�ұ�('isBase'=��λ�ң�''=�ۺϱ�λ�ң�'all'=���бұ�'��ҵ�id'=���)
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		//�������
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		
		/**
		 * ��ѯ���ݿ�����
		 */
		final String acctypeCodeStart = conMap.get("acctypeCodeStart");
		final String acctypeCodeEnd = conMap.get("acctypeCodeEnd");
		final String showItemDetail = conMap.get("showItemDetail");
		final String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");
		final String levelStart = conMap.get("levelStart");
		final String levelEnd = conMap.get("levelEnd");
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							/* �������� */
							String dateStart = conMap.get("dateStart");
							String dateEnd = conMap.get("dateEnd");
							String binderNo = conMap.get("binderNo");
							
							HashMap accMap = new HashMap();										//������������ݣ�������������õ�
							String condition = scopeSql(mop, loginBean);						//����
							String currentCondition = "";
							/**
							 * ��ѯ��ƿ�Ŀ
							 */
							StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog, ");
							sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.JdFlag,l.zh_cn as AccFullName FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 ");
							if(acctypeCodeStart != null && !"".equals(acctypeCodeStart)){
								//��Ŀ��ʼ
								condition += " AND (tblAccTypeInfo.AccNumber>='"+acctypeCodeStart+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeStart+"')+'%')";
							}
							if(acctypeCodeEnd != null && !"".equals(acctypeCodeEnd)){
								//��Ŀ����
								condition += " AND (tblAccTypeInfo.AccNumber<='"+acctypeCodeEnd+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeEnd+"')+'%')";
							}
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//����ʾ���ÿ�Ŀ
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							currentCondition = condition;
							sql.append(condition);
							sql.append(" ORDER BY tblAccTypeInfo.classCode");
							System.out.println("��Ŀ��ѯSQL��"+sql.toString());
							rset = st.executeQuery(sql.toString());
							List accTypeInfoList = new ArrayList();
							while(rset.next()){
								HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	accMap.put(map.get("classCode")+"_jdflag", map.get("JdFlag"));
                            	accMap.put(map.get("classCode")+"_isCalculate", map.get("isCalculate"));
                            	accMap.put(map.get("classCode")+"_AccNumber", map.get("AccNumber"));
                            	accMap.put(map.get("classCode")+"_isCatalog", map.get("isCatalog"));
                            	accMap.put(map.get("classCode")+"_isCalculateParent", map.get("isCalculateParent"));
                            	accTypeInfoList.add(map);
							}
							
							if(binderNo == null || "".equals(binderNo)){
	            				//������δ����ƾ֤
								condition += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
	            			}
							if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		condition += " AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+searchCurrency+"') ";
                            	}else{
                            		//�������ʱ
                            		condition += " AND tblAccDetail.Currency='"+searchCurrency+"' ";
                            	}
                            }
							HashMap totalMap = new HashMap();
							/**
							 * ��ѯ���ս�������
							 */
							sql = new StringBuffer("SELECT tblAccDetail.AccCode,tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent");
							if("all".equals(currencyValue)){
								//���бұ����ʽʱ�Ա��ֽ��з���
								sql.append(",ISNULL(tblAccDetail.Currency,'') AS Currency");
							}
							sql.append(",SUM(tblAccDetail.DebitAmount-tblAccDetail.LendAmount) AS sumdisAmountBase,tblAccTypeInfo.JdFlag,");
							sql.append("SUM(tblAccDetail.DebitCurrencyAmount-tblAccDetail.LendCurrencyAmount) AS sumdisAmount");
							sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
							sql.append(" WHERE tblAccMain.BillDate<'"+dateStart+"'");
							sql.append(condition);
							sql.append(" GROUP BY tblAccDetail.AccCode,tblAccTypeInfo.classCode,tblAccTypeInfo.JdFlag,tblAccTypeInfo.isCalculate,tblAccTypeInfo.isCatalog,tblAccTypeInfo.isCalculateParent ");
							if("all".equals(currencyValue)){
								sql.append(",tblAccDetail.Currency ");
							}
							sql.append(" ORDER BY tblAccDetail.AccCode,tblAccTypeInfo.isCalculateParent");
							System.out.println("��ѯ���ս������"+sql.toString());
							rset = st.executeQuery(sql.toString());
							HashMap preMap = new HashMap();
							while (rset.next()) {
								HashMap map=new HashMap();
								for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
								
								String lineStr = "sumdisAmount;sumdisAmountBase;";
								//��ѡ��ıұ��ǡ������бұ����ʽ
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumdisAmount"};
                            		String base = "";
                            		if("".equals(map.get("Currency"))){
                            			//��λ��
                            			base = "Base";
                            		}
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("Currency"), map.get(s+base));
                        				lineStr += s+"_"+map.get("Currency")+";";
                        			}
                            	}
                            	/**
								 * �������ս��ĺϼ�
								 */
                            	int jdFlag = Integer.parseInt(map.get("JdFlag").toString());
                            	String[] strs = lineStr.split(";");
								for(String s : strs){
									String m = "0";
									Object object = totalMap.get("pre_"+s);
									if(object != null && !"".equals(object)){
										m = object.toString();
									}
									object = map.get(s);
									if(object != null && !"".equals(object)){
//										if(jdFlag == 2){
//											//����
//											m = new BigDecimal(m).subtract(new BigDecimal(object.toString())).toString();
//										}else{
											m = new BigDecimal(m).add(new BigDecimal(object.toString())).toString();
//										}
									}
									totalMap.put("pre_"+s, m.toString());
								}
								
            					String classCode = map.get("classCode").toString();
            					preMap.put(classCode,map);
            					
            					//���ϼ���ֵ
            					for(int j=1;j<classCode.length()/5;j++){
            						HashMap oldMap = (HashMap)preMap.get(classCode.substring(0,j*5));
        							int count = 0;
        							String[] moneyStr = null;
        							if("all".equals(currencyValue)){
        								moneyStr = new String[]{"sumdisAmountBase","sumdisAmount_"+map.get("Currency")};
        							}else{
        								moneyStr = new String[]{"sumdisAmountBase","sumdisAmount"};
        							}
        							if(oldMap!=null && oldMap.size()>0){
        								for(String s : moneyStr){
        									String moneys = "0";
        									Object o = oldMap.get(s);
        									if(o!=null && !"".equals(o)){
        										moneys = o.toString();
        									}
        									if(map.get(s)!=null && !"".equals(map.get(s))){
        										moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
        									}
        									String totalAmount = moneys;
        									if(Double.parseDouble(moneys)==0){
        										totalAmount = "";
        									}
        									oldMap.put(s,totalAmount);
        								}
        								preMap.put(classCode.substring(0,j*5),oldMap);
        							}else{
        								//����������һ��ʱ�������µ�Map��������
        								oldMap = new HashMap();
        								Object accNumber = accMap.get(classCode.substring(0,j*5)+"_AccNumber");
        								oldMap.put("AccCode", accNumber);
        								oldMap.put("isCalculate", accMap.get(classCode.substring(0,j*5)+"_isCalculate"));
        								oldMap.put("JdFlag", accMap.get(classCode.substring(0,j*5)+"_jdflag"));
        								oldMap.put("classCode", classCode.substring(0,j*5));
        								oldMap.put("isCatalog", accMap.get(classCode.substring(0,j*5)+"_isCatalog"));
        								oldMap.put("isCalculateParent", accMap.get(classCode.substring(0,j*5)+"_isCalculateParent"));
        								for(String s : moneyStr){
        									String totalAmount = "0";
	        								if(map.get(s) != null && !"".equals(map.get(s))){
	        									totalAmount = map.get(s).toString();
	        								}
	        								if(Double.valueOf(totalAmount)==0){
	        									totalAmount = "";
	        								}
	        								oldMap.put(s,totalAmount);
        								}
        								preMap.put(classCode.substring(0,j*5),oldMap);
        							}
            					}
							}
							
							/* ��ѯ����ǰ���ڳ���� */
							sql = new StringBuffer("SELECT SubCode as AccCode,tblAccTypeInfo.classCode,");
							sql.append("(case tblAccTypeInfo.JdFlag when 2 then 0-CurrYIniBalaBase else CurrYIniBalaBase end) AS sumdisAmountBase,");
							sql.append("(case tblAccTypeInfo.JdFlag when 2 then 0-CurrYIniBala else CurrYIniBala end) AS sumdisAmount,");
							sql.append("isnull(tblAccTypeInfo.isCalculate,'') AS isCalculate,tblAccTypeInfo.isCataLog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.JdFlag,");
							sql.append("isnull(tblAccBalance.CurType,'') as CurType FROM tblAccBalance left join tblAccTypeInfo on tblAccBalance.subCode=tblAccTypeInfo.AccNumber");
							sql.append(" WHERE Nyear=-1 AND Period=-1 AND tblAccTypeInfo.classCode not like '00004%' AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1");
							if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		//�������ʱ
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
							sql.append(currentCondition);
							sql.append(" ORDER BY SubCode");
							System.out.println("�ڳ�sql:"+sql.toString());
							rset = st.executeQuery(sql.toString());
							while(rset.next()) {
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				map.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
	                    					String strvalue = String.valueOf(obj);
	                    					if (strvalue.indexOf(".")>0){
	                    						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
	                    					}
	                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
	                    						strvalue = "";
	                    					}
	                    					map.put(rset.getMetaData().getColumnName(i), strvalue);
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
	                        	//��ѡ��ıұ��ǡ������бұ����ʽ
	                        	String lineStr = "sumdisAmount;sumdisAmountBase;";
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumdisAmount"};
                            		String base = "";
                            		if("".equals(map.get("CurType"))){
                            			//��λ��
                            			base = "Base";
                            		}
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("CurType"), map.get(s+base));
                        				lineStr += s+"_"+map.get("CurType")+";";
                        			}
                            	}
                            	
                            	/**
								 * ����ϼ�
								 */
                            	String[] strs = lineStr.split(";");
                            	int jdFlag = Integer.parseInt(map.get("JdFlag").toString());
                        		for(String s : strs){
                        			String m = "0";
                        			Object object = totalMap.get("pre_"+s);
                        			if(object != null && !"".equals(object)){
                        				m = object.toString();
                        			}
                        			object = map.get(s);
                        			if(object != null && !"".equals(object)){
//                        				if(jdFlag == 2){
//											//����
//											m = new BigDecimal(m).subtract(new BigDecimal(object.toString())).toString();
//										}else{
											m = new BigDecimal(m).add(new BigDecimal(object.toString())).toString();
//										}
                        			}
                        			String totalAmount = m;
									if(Double.parseDouble(m)==0){
										totalAmount = "";
									}
                        			totalMap.put("pre_"+s, totalAmount);
                        		}                            		
                            	
            					if(preMap != null && preMap.size()>0){
            						String[] moneyStr = new String[]{"sumdisAmountBase","sumdisAmount"};
            						if("all".equals(currencyValue)){
        								moneyStr = new String[]{"sumdisAmountBase","sumdisAmount_"+map.get("CurType")};
        							}
	                        		HashMap oldMap = (HashMap)preMap.get(map.get("classCode"));
	                        		if(oldMap != null){
	                        			//����ƾ֤�ڳ����ʱ
	                        			for(String s : moneyStr){
	                        				String money = "0";
	                        				Object o = oldMap.get(s);
	                        				if(o != null && !"".equals(o)){
	                        					money = o.toString();
	                        				}
	                        				o = map.get(s);
	                        				if(o != null && !"".equals(o)){
	                        					money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
	                        				}
	                        				oldMap.put(s, money);
	                        			}
	                        		}else{
	                        			oldMap = map;
	                        		}
	                        		preMap.put(map.get("classCode"), oldMap);
	                        	}else{
	                        		preMap.put(map.get("classCode"), map);
	                        	}
            					
            					String classCode = String.valueOf(map.get("classCode"));
            					//���ϼ���ֵ
            					for(int j=1;j<classCode.length()/5;j++){
            						HashMap oldMap = (HashMap)preMap.get(classCode.substring(0,j*5));
        							int count = 0;
        							String[] moneyStr = null;
        							if("all".equals(currencyValue)){
        								moneyStr = new String[]{"sumdisAmountBase","sumdisAmount_"+map.get("CurType")};
        							}else{
        								moneyStr = new String[]{"sumdisAmountBase","sumdisAmount"};
        							}
        							if(oldMap!=null && oldMap.size()>0){
        								for(String s : moneyStr){
        									String moneys = "0";
        									Object o = oldMap.get(s);
        									if(o!=null && !"".equals(o)){
        										moneys = o.toString();
        									}
        									if(map.get(s)!=null && !"".equals(map.get(s))){
        										moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
        									}
        									String totalAmount = moneys;
        									if(Double.parseDouble(moneys)==0){
        										totalAmount = "";
        									}
        									oldMap.put(s,totalAmount);
        								}
        								preMap.put(classCode.substring(0,j*5),oldMap);
        							}else{
        								//����������һ��ʱ�������µ�Map��������
        								oldMap = new HashMap();
        								Object accNumber = accMap.get(classCode.substring(0,j*5)+"_AccNumber");
        								oldMap.put("AccCode", accNumber);
        								oldMap.put("isCalculate", accMap.get(classCode.substring(0,j*5)+"_isCalculate"));
        								oldMap.put("JdFlag", accMap.get(classCode.substring(0,j*5)+"_jdflag"));
        								oldMap.put("isCatalog", accMap.get(classCode.substring(0,j*5)+"_isCatalog"));
        								oldMap.put("isCalculateParent", accMap.get(classCode.substring(0,j*5)+"_isCalculateParent"));
        								oldMap.put("classCode", classCode.substring(0,j*5));
        								for(String s : moneyStr){
        									String totalAmount = "0";
	        								if(map.get(s) != null && !"".equals(map.get(s))){
	        									totalAmount = map.get(s).toString();
	        								}
	        								if(Double.valueOf(totalAmount)==0){
	        									totalAmount = "";
	        								}
	        								oldMap.put(s,totalAmount);
        								}
        								preMap.put(classCode.substring(0,j*5),oldMap);
        							}
            					}
							}
							
							/**
							 * �������ڲ�ѯͳ��ƾ֤�Ľ�����
							 */
							sql = new StringBuffer("SELECT tblAccDetail.AccCode,tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,");
							if("all".equals(currencyValue)){
								//���бұ����ʽʱ�Ա��ֽ��з���
								sql.append("ISNULL(tblAccDetail.Currency,'') AS Currency,");
							}
							sql.append("SUM(tblAccDetail.DebitAmount) AS sumDebitAmountBase,SUM(tblAccDetail.LendAmount) AS sumLendAmountBase,tblAccTypeInfo.JdFlag,");
							sql.append("SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount) AS sumdisAmountBase,SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount) AS sumdisAmount,");
							sql.append("SUM(tblAccDetail.DebitCurrencyAmount) AS sumDebitAmount,SUM(tblAccDetail.LendCurrencyAmount) AS sumLendAmount");
							sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
							sql.append(" WHERE tblAccMain.BillDate>='"+dateStart+"' AND tblAccMain.BillDate<='"+dateEnd+"'");
							sql.append(condition);
							sql.append(" GROUP BY tblAccDetail.AccCode,tblAccTypeInfo.classCode,tblAccTypeInfo.JdFlag,tblAccTypeInfo.isCalculate,tblAccTypeInfo.isCatalog,tblAccTypeInfo.isCalculateParent ");
							if("all".equals(currencyValue)){
								sql.append(",tblAccDetail.Currency ");
							}
							sql.append(" ORDER BY tblAccDetail.AccCode");
							System.out.println("��ѯ���ս������"+sql.toString());
							rset = st.executeQuery(sql.toString());
							HashMap periodMap = new HashMap();
							while (rset.next()) {
								HashMap map=new HashMap();
								for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
								
								//��ѡ��ıұ��ǡ������бұ����ʽ
								String lineStr = "sumDebitAmountBase;sumLendAmountBase;sumdisAmountBase;sumDebitAmount;sumLendAmount;sumdisAmount;";
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumdisAmount"};
                            		String base = "";
                            		if("".equals(map.get("Currency"))){
                            			base = "Base";
                            		}
                            		for(String s : curs){
                        				map.put(s+"_"+map.get("Currency"), map.get(s+base));
                        				lineStr += s+"_"+map.get("Currency")+";";
                        			}
                            	}
            					
                            	/**
								 * ������ĺϼ�
								 */
                            	int jdFlag = Integer.parseInt(map.get("JdFlag").toString());
                            	String[] strs = lineStr.split(";");
                        		for(String s : strs){
                        			String m = "0";
                        			Object object = totalMap.get("period_"+s);
                        			if(object != null && !"".equals(object)){
                        				m = object.toString();
                        			}
                        			object = map.get(s);
                        			if(object != null && !"".equals(object)){
//                        				if(jdFlag == 2){
//											//����
//											m = new BigDecimal(m).subtract(new BigDecimal(object.toString())).toString();
//										}else{
											m = new BigDecimal(m).add(new BigDecimal(object.toString())).toString();
//										}
                        			}
                        			String totalAmount = m;
									if(Double.parseDouble(m)==0){
										totalAmount = "";
									}
                        			totalMap.put("period_"+s, totalAmount);
                        		}                            		
                            	
                        		//���ϼ���ֵ
            					String classCode = map.get("classCode").toString();
            					
            					for(int j=1;j<classCode.length()/5;j++){
            						HashMap oldMap = (HashMap)periodMap.get(classCode.substring(0,j*5));
        							int count = 0;
        							String[] moneyStr = new String[]{"sumDebitAmount","sumDebitAmountBase","sumLendAmount","sumLendAmountBase","sumdisAmount","sumdisAmountBase"};
        							if("all".equals(currencyValue)){
        								moneyStr = new String[]{"sumDebitAmountBase","sumDebitAmount_"+map.get("Currency"),"sumLendAmountBase","sumLendAmount_"+map.get("Currency"),"sumdisAmountBase","sumdisAmount_"+map.get("Currency")};
        							}
        							if(oldMap!=null && oldMap.size()>0){
        								for(String s : moneyStr){
        									String moneys = "0";
        									Object o = oldMap.get(s);
        									if(o!=null && !"".equals(o)){
        										moneys = o.toString();
        									}
        									if(map.get(s)!=null && !"".equals(map.get(s))){
        										moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
        									}
        									String totalAmount = moneys;
        									if(Double.parseDouble(moneys)==0){
        										totalAmount = "";
        									}
        									oldMap.put(s,totalAmount);
        								}
        								periodMap.put(classCode.substring(0,j*5),oldMap);
        							}else{
        								//����������һ��ʱ�������µ�Map��������
        								oldMap = new HashMap();
        								Object accNumber = accMap.get(classCode.substring(0,j*5)+"_AccNumber");
        								oldMap.put("AccCode", accNumber);
        								oldMap.put("classCode", classCode.substring(0,j*5));
        								oldMap.put("isCalculate", accMap.get(classCode.substring(0,j*5)+"_isCalculate"));
        								oldMap.put("isCatalog", accMap.get(classCode.substring(0,j*5)+"_isCatalog"));
        								oldMap.put("isCalculateParent", accMap.get(classCode.substring(0,j*5)+"_isCalculateParent"));
        								oldMap.put("JdFlag", accMap.get(classCode.substring(0,j*5)+"_jdflag"));
        								for(String s : moneyStr){
        									String totalAmount = "0";
	        								if(map.get(s) != null && !"".equals(map.get(s))){
	        									totalAmount = map.get(s).toString();
	        								}
	        								if(Double.valueOf(totalAmount)==0){
	        									totalAmount = "";
	        								}
	        								oldMap.put(s,totalAmount);
        								}
        								periodMap.put(classCode.substring(0,j*5),oldMap);
        							}
            					}
            					
            					if("all".equals(currencyValue)){
                            		/* ����ҽ��з���ʱ���跽�����ߴ������Ҫ���е��� */
                            		HashMap oldMap = (HashMap)periodMap.get(classCode);
                            		if(oldMap != null && oldMap.size()>0){
                            			String[] str = new String[]{"sumDebitAmountBase","sumLendAmountBase","sumdisAmountBase"};
                            			for(int j=0;j<str.length;j++){
                            				Object o = oldMap.get(str[j]);
                            				if(o != null && !"".equals(o)){
                            					String money = map.get(str[j]).toString();
                            					if("".equals(money)){
                            						money = "0";
                            					}
                            					map.put(str[j],new BigDecimal(money.toString()).add(new BigDecimal(o.toString())).toString());
                            				}
                            			}
                            		}
                        		}
            					periodMap.put(classCode,map);
							}
							/**
							 * ͳ�ƽ������
							 */
							String conditions = "FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode";
							conditions += " WHERE tblAccMain.BillDate>='"+dateStart+"' AND tblAccMain.BillDate<='"+dateEnd+"'"+condition;
							sql = new StringBuffer("SELECT 'debit' as groups,ISNULL(COUNT(tblAccDetail.DebitAmount),0) as count,tblAccTypeInfo.classCode,tblAccDetail.AccCode "+conditions+" AND tblAccDetail.DebitAmount!=0 GROUP BY tblAccDetail.AccCode,tblAccTypeInfo.classCode");
							sql.append(" UNION ALL ");
							sql.append("SELECT 'lend' as groups,ISNULL(COUNT(tblAccDetail.LendAmount),0) as count,tblAccTypeInfo.classCode,tblAccDetail.AccCode "+conditions+" AND tblAccDetail.LendAmount!=0 GROUP BY tblAccDetail.AccCode,tblAccTypeInfo.classCode");
							System.out.println("���������"+sql.toString());
							rset = st.executeQuery(sql.toString());
							HashMap countMap = new HashMap();
							while (rset.next()) {
								HashMap map=new HashMap();
								for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
								String countstr = "";
								if("debit".equals(map.get("groups"))){
									countstr = "debitCount";
								}else if("lend".equals(map.get("groups"))){
									countstr = "lendCount";
								}
								
								String classCode = map.get("classCode").toString();
								for(int j=0;j<=(classCode.length()-5)/5;j++){
									String oldclass = classCode.substring(0,5+j*5);
									HashMap oldMap = (HashMap)countMap.get(oldclass+"_"+map.get("groups"));
									Integer count = 0;
									if(oldMap != null && oldMap.size()>0){
										Object o = oldMap.get("count");
										if(o != null && !"".equals(o)){
											count = Integer.parseInt(o.toString());
										}
									}else{
										oldMap = new HashMap();
									}
									count = count+Integer.parseInt(map.get("count").toString());
									oldMap.put("count", count);
									countMap.put(oldclass+"_"+map.get("groups"), oldMap);
								}
								
								//ͳ�ƽ��������
								Object o = totalMap.get(countstr);
								Integer count = 0;
								if(o != null && !"".equals(o)){
									count = count + Integer.parseInt(o.toString());
								}
								count = count + Integer.parseInt(map.get("count").toString());
								totalMap.put(countstr, count);
								
								countMap.put(map.get("classCode")+"_"+map.get("groups"), map);
							}
							/**
							 * ��������
							 * accTypeInfoList ��ƿ�Ŀ
							 * periodMap       ���ս�������
							 * preMap          ���ս�������
							 * countMap        ���ս������
							 * accMap          ��Ŀ��������ݣ�accnumber,jdflag,isCalculate��
							 * totalMap        �ϼ�
							 */
							Object[] obj = new Object[]{accTypeInfoList, periodMap, preMap, countMap, accMap, totalMap};
							result.setRetVal(obj);
							
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt AccTypeInfoDay:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		
		/* ��ȡ���� */
		Object[] obj = (Object[])result.retVal;
		List accTypeInfoList = (ArrayList)obj[0];
		HashMap periodMaps = (HashMap)obj[1];
		HashMap preMaps = (HashMap)obj[2];
		HashMap countMap = (HashMap)obj[3];
		HashMap accMap = (HashMap)obj[4];
		HashMap totalMap = (HashMap)obj[5];											//�ϼ�
		
		//�õ����������+���ս�����
		Iterator iter = periodMaps.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			HashMap periodMap = (HashMap)entry.getValue();
			String isCatalog = String.valueOf(periodMap.get("isCatalog"));
			if(periodMap.get("JdFlag") == null){
				continue;
			}
			int jdFlag = Integer.parseInt(periodMap.get("JdFlag").toString());
			
			//�õ��������
			String[] strs = new String[]{"sumdisAmountBase","sumdisAmount"};
			
			//���бұ����ʽ
			if("all".equals(currencyValue)){
				strs = new String[currList.size()+1];
				for(int k=0;k<currList.size();k++){
					String[] currStr = (String[])currList.get(k);
					String curId = currStr[0];
					if("true".equals(currStr[2])){
						//��λ��
						curId = "";
					}
					strs[k] = "sumdisAmount_"+curId;
				}
				strs[currList.size()] = "sumdisAmountBase";
			}
			for(String s : strs){
				HashMap pMap = (HashMap)preMaps.get(entry.getKey());
				String money = "0";
				Object o = null;
				if(pMap != null && pMap.size()>0){
					o = pMap.get(s);
					if(o != null && !"".equals(o)){
						money = o.toString();
					}
				}
				o = periodMap.get(s);
				if(o != null && !"".equals(o)){
//					if(jdFlag == 2){
//						money = new BigDecimal(money).subtract(new BigDecimal(o.toString())).toString();
//					}else{
						money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
//					}
				}
				//�������
				periodMap.put("total"+s, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
			}
		}
		iter = preMaps.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			HashMap preMap = (HashMap)entry.getValue();
			HashMap pMap = (HashMap)periodMaps.get(entry.getKey());
			if(pMap==null){
				//�������ڱ��ս��ʱ��������Ϊ�������
				if(preMap.get("JdFlag") == null){
					continue;
				}
				int jdFlag = Integer.parseInt(preMap.get("JdFlag").toString());
				pMap = new HashMap();
				String isCatalog = String.valueOf(preMap.get("isCatalog"));
				pMap.put("AccCode",preMap.get("AccCode"));
				pMap.put("classCode",preMap.get("classCode"));
				pMap.put("JdFlag", jdFlag);
				pMap.put("isCalculateParent",preMap.get("isCalculateParent"));
				String[] strs = new String[]{"sumdisAmountBase","sumdisAmount"};
				//���бұ����ʽ
				if("all".equals(currencyValue)){
					strs = new String[currList.size()+1];
					for(int k=0;k<currList.size();k++){
						String[] currStr = (String[])currList.get(k);
						String curId = currStr[0];
						if("true".equals(currStr[2])){
							//��λ��
							curId = "";
						}
						strs[k] = "sumdisAmount_"+curId;
					}
					strs[currList.size()] = "sumdisAmountBase";
				}
				for(String s : strs){
					String money = "0";
					Object o = preMap.get(s);
					if(o != null && !"".equals(o)){
						money = o.toString();
					}
					pMap.put("total"+s, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					periodMaps.put(entry.getKey(), pMap);
				}
			}
		}
		
		String takeBrowNo = conMap.get("takeBrowNo");					//�޷������ʾ
		String balanceZero = conMap.get("balanceZero");					//���Ϊ�㲻��ʾ
		List accList = new ArrayList();
		
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			String classCode = (String)map.get("classCode");
			HashMap periodMap = (HashMap)periodMaps.get(classCode);
			HashMap preMap = (HashMap)preMaps.get(classCode);
			
			Integer isCatalog = Integer.parseInt(map.get("isCatalog").toString());
			
			if(takeBrowNo!=null && !"".equals(takeBrowNo)){
				//ѡ�����޷������ʾ
				boolean flag2 = false;
				if(periodMap != null){
					if((periodMap.get("sumDebitAmountBase")==null || "".equals(periodMap.get("sumDebitAmountBase")))
							&& (periodMap.get("sumLendAmountBase")==null || "".equals(periodMap.get("sumLendAmountBase")))
							&& (periodMap.get("totalsumdisAmountBase")==null || "".equals(periodMap.get("totalsumdisAmountBase")))){
						continue;
					}
				}else{
					continue;
				}
			}
			if(balanceZero != null && !"".equals(balanceZero)){
				//���Ϊ�㲻��ʾ
				if(periodMap != null && periodMap.size()>0){
					Object o1 = periodMap.get("totalsumdisAmountBase");
					if(o1 == null || "".equals(o1) || "0".equals(o1)){
						continue;
					}
				}else{
					continue;
				}
			}
			String classCodes = map.get("classCode").toString();
			String accNumber = map.get("AccNumber").toString();
			if(levelEnd != null && !"".equals(levelEnd)){
				//��Ŀ�ȼ�����
				if(classCodes.length()/5-1>Integer.parseInt(levelEnd)){
					continue;
				}
			}
			if(showItemDetail == null || "".equals(showItemDetail)){
				//����������Ŀ��ϸ
				if("1".equals(map.get("isCalculate").toString())){
					continue;
				}
			}
			accList.add(map);
		}
		
		//����С���㱣������������(�������)
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			String classCode = (String)map.get("classCode");
			HashMap periodMap = (HashMap)periodMaps.get(classCode);
			HashMap preMap = (HashMap)preMaps.get(classCode);
			
			/**
			 * �����������
			 */
			if(preMap != null && preMap.size()>0){
				String lineStr = "sumdisAmount;sumdisAmountBase;";
				if("all".equals(currencyValue)){
					for(int k=0;k<currList.size();k++){
						String[] currStr = (String[])currList.get(k);
						String curId = currStr[0];
						if("true".equals(currStr[2])){
							//��λ��
							curId = "";
						}
						lineStr += "sumdisAmount_"+curId+";";
					}
				}
				String[] fieldStr = lineStr.split(";");
				for(int k=0;k<fieldStr.length;k++){
					if(preMap.get(fieldStr[k])!=null && !"".equals(preMap.get(fieldStr[k]))){
						//������
						String money = String.valueOf(preMap.get(fieldStr[k]));
						preMap.put(fieldStr[k],dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					}
				}
			}
			
			/**
			 * ���������
			 */
			if(periodMap != null && periodMap.size()>0){
				String lineStr = "totalsumdisAmount;totalsumdisAmountBase;";
				if("all".equals(currencyValue)){
					for(int k=0;k<currList.size();k++){
						String[] currStr = (String[])currList.get(k);
						String curId = currStr[0];
						if("true".equals(currStr[2])){
							//��λ��
							curId = "";
						}
						lineStr += "totalsumdisAmount_"+curId+";";
					}
				}
				String[] fieldStr = lineStr.split(";");
				for(int k=0;k<fieldStr.length;k++){
					if(periodMap.get(fieldStr[k])!=null && !"".equals(periodMap.get(fieldStr[k]))){
						//������
						String money = String.valueOf(periodMap.get(fieldStr[k]));
						periodMap.put(fieldStr[k],dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						//����ϼ�
						String isCatalog = String.valueOf(periodMap.get("isCatalog"));
						Integer isCalculateParent = Integer.parseInt(periodMap.get("isCalculateParent").toString());
						if(isCatalog != null && "0".equals(isCatalog) && isCalculateParent!=1){
							String m = "0";
							Object o = totalMap.get("period_"+fieldStr[k]);
							if(o != null && !"".equals(o)){
								m = o.toString();
							}
							if(Integer.parseInt(periodMap.get("JdFlag").toString())==2){
								m = new BigDecimal(m).subtract(new BigDecimal(money)).toString();
							}else{
								m = new BigDecimal(m).add(new BigDecimal(money)).toString();
							}
							totalMap.put("period_"+fieldStr[k], dealDataDouble(m, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}
					}
				}
			}
		}
		
		obj = new Object[]{accList, periodMaps, preMaps, countMap, totalMap, currencyValue};
		result.setRetVal(obj);
		return result;
	}
	
	/**
	 * ƾ֤���ܱ�
	 * @param conMap
	 * @return
	 */
	protected Result accCertificateSum(final HashMap<String,String> conMap,final MOperation mop,final LoginBean loginBean){
		//������������
		String currencyName = conMap.get("currencyName");
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		final String showItemDetail = conMap.get("showItemDetail");
		final String takeBrowNo = conMap.get("takeBrowNo");
		final String levelStart = conMap.get("levelStart");						//��Ŀ����ʼ
		final String levelEnd = conMap.get("levelEnd");							//��Ŀ�������
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							String dateStart = conMap.get("dateStart");							//���ڿ�ʼ
							String dateEnd = conMap.get("dateEnd");								//���ڽ���
							String area = conMap.get("area");									//��Χ��0 ����ƾ֤��1 δ����ƾ֤��2 �ѹ���ƾ֤��
							String showAll = conMap.get("showAll");								//��������ƾ֤�ֺ�
							String credTypeStr = conMap.get("credTypeStr");
							
							HashMap accMap = new HashMap();
							
							String condition = scopeSql(mop, loginBean);
							
							/**
							 * ��ѯ���л�ƿ�Ŀ
							 */
							StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.JdFlag,l.zh_cn as AccFullName ");
							sql.append("FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 "+condition);
							sql.append(" ORDER BY tblAccTypeInfo.AccNumber");
							System.out.println("��Ŀ��ѯSQL��"+sql.toString());
							rset = st.executeQuery(sql.toString());
							List accTypeInfoList = new ArrayList();
							while(rset.next()){
								HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	accMap.put(map.get("classCode")+"_isCalculate", map.get("isCalculate"));
                            	accMap.put(map.get("classCode")+"_AccNumber", map.get("AccNumber"));
                            	accTypeInfoList.add(map);
							}
							
							HashMap totalMap = new HashMap();									//�ϼ�map
							/**
							 * ���ڽ�������
							 */
                            sql = new StringBuffer("SELECT tblAccDetail.AccCode,tblAccTypeInfo.classCode,ISNULL(tblAccTypeInfo.isCalculate,'') AS isCalculate,");
                            sql.append("SUM(tblAccDetail.DebitAmount) as sumDebitAmountBase,SUM(tblAccDetail.DebitCurrencyAmount) as sumDebitAmount,");
                            sql.append("SUM(tblAccDetail.LendAmount) as sumLendAmountBase,SUM(tblAccDetail.LendCurrencyAmount) as sumLendAmount,");
                            sql.append("SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount) AS sumBalanceAmountBase,");
                            sql.append("SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount) AS sumBalanceAmount");
                            if("all".equals(currencyValue)){
                            	sql.append(",isnull(tblAccDetail.Currency,'') as Currency");
                            }
                            sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id ");
                            sql.append("LEFT JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE 1=1 ");
                            
                            sql.append(condition);
                            //��������
                            if(dateStart!=null && !"".equals(dateStart)){
                            	sql.append(" AND tblAccMain.BillDate>='"+dateStart+"'");
                            }
                            if(dateEnd != null && !"".equals(dateEnd)){
                            	sql.append(" AND tblAccMain.BillDate<='"+dateEnd+"'");
                            }
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		sql.append(" AND (tblAccDetail.Currency='' OR tblAccDetail.Currency IS NULL OR tblAccDetail.Currency='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccDetail.Currency='"+searchCurrency+"'");
                            	}
                            }
                            if(area != null && "1".equals(area)){
                            	sql.append(" AND tblAccMain.workFlowNodeName!='finish'");
                            }
                            if(area != null && "2".equals(area)){
                            	sql.append(" AND tblAccMain.workFlowNodeName='finish'");
                            }
                            if(showAll==null || "".equals(showAll)){
                            	//δ��ѡ����ƾ֤
                            	if(credTypeStr!=null && !"".equals(credTypeStr)){
                            		String[] credStr = credTypeStr.split(";]");
                            		sql.append(" AND (");
                            		for(int i=0;i<credStr.length;i++){
                            			String[] values = credStr[i].split(";");
                            			for(int j=0;j<values.length;j++){
                            				if(j==0){
                            					sql.append(" (tblAccMain.CredTypeID='"+values[j]+"'");
                            				}
                            				if(j==1 && !"".equals(values[j])){
                            					sql.append(" AND tblAccMain.OrderNo>="+values[j]);
                            				}
                            				if(j==2 && !"".equals(values[j])){
                            					sql.append(" AND tblAccMain.OrderNo<="+values[j]);
                            				}
                            			}
                            			sql.append(" )");
                        				if(i!=credStr.length-1){
                        					sql.append(" OR ");
                        				}
                            		}
                            		sql.append(" )");
                            	}
                            }
                            sql.append(" GROUP BY tblAccDetail.AccCode,tblAccTypeInfo.classCode,tblAccTypeInfo.isCalculate ");
                            if("all".equals(currencyValue)){
                            	sql.append(",tblAccDetail.Currency");
                            }
                            sql.append(" ORDER BY tblAccDetail.AccCode");
                            System.out.println("ƾ֤���ܱ�sql:"+sql);
                            rset=st.executeQuery(sql.toString());
                            HashMap accMainMap = new HashMap();
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                        					
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	
                            	//��ѡ��ıұ��ǡ������бұ����ʽ
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount"};
                            		if("".equals(map.get("Currency"))){
                            			//��λ��ʱ����ҵĽ����ڵ�ǰ��λ�ҵĽ��
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					map.put(s+"_", map.get(s+"Amount"));
                            				}else{
                            					map.put(s+"_", map.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//�������
                            			for(String s : curs){
                            				map.put(s+"_"+map.get("Currency"), map.get(s));
                            			}
                            		}
                            	}
                            	
                        		/**
            					 * ���ϼ���ֵ
            					 */
                        		String classCode = map.get("classCode").toString();
            					for(int j=0;j<(classCode.length()-5)/5;j++){
            						//��ƿ�ĿclassCode����ѭ����ȡ�ϼ�
            						HashMap oldMap = (HashMap)accMainMap.get(classCode.substring(0,5+j*5));
        							String[] moneyStr = null;
        							if("all".equals(currencyValue)){
        								moneyStr = new String[]{"sumDebitAmountBase","sumDebitAmount_"+map.get("Currency"),"sumLendAmountBase","sumLendAmount_"+map.get("Currency"),"sumBalanceAmountBase","sumBalanceAmount_"+map.get("Currency")};
        							}else{
        								moneyStr = new String[]{"sumDebitAmountBase","sumDebitAmount","sumLendAmountBase","sumLendAmount","sumBalanceAmountBase","sumBalanceAmount"};
        							}
        							String moneys = "0";
    								for(String s : moneyStr){
    									moneys = "0";
    									if(oldMap == null){
    										oldMap = new HashMap();
    										Object accNumber = accMap.get(classCode.substring(0,5+j*5)+"_AccNumber");
            								oldMap.put("AccCode", accNumber);
            								oldMap.put("classCode", classCode.substring(0,5+j*5));
    									}
    									Object o = oldMap.get(s);
    									if(o!=null && !"".equals(o)){
    										moneys = o.toString();
    									}
    									if(map.get(s)!=null && !"".equals(map.get(s))){
    										moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();	
    									}
    									String totalAmount = moneys;
    									if(Double.parseDouble(moneys)==0){
    										totalAmount = "";
    									}
    									oldMap.put(s,totalAmount);
    									
    									//ͳ�ƺϼ�
    									if(j == 0){
    										moneys = "0";
    										o = totalMap.get(s);
    										if(o != null && !"".equals(o)){
    											moneys = o.toString();
    										}
    										if(map.get(s)!=null && !"".equals(map.get(s))){
    											moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s).toString())).toString();
    										}
    										totalMap.put(s, dealDataDouble(moneys, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
    									}
    								}
        							accMainMap.put(classCode.substring(0,5+j*5), oldMap);
                            	}
                            	accMainMap.put(map.get("classCode"), map);
                            }
                            
                            /**
                             * accTypeInfoList ��ƿ�Ŀ,accMainMap ƾ֤������,totalMap �ϼ�
                             */
                            Object[] obj = new Object[]{accTypeInfoList,accMainMap,totalMap};
                            result.setRetVal(obj);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt AccCertificateSum:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		
		/**
		 * ��������
		 */
		Object[] obj = (Object[])result.getRetVal();
		List accTypeInfoList = (ArrayList)obj[0];
		HashMap accMainMap = (HashMap)obj[1];
		HashMap totalMap = (HashMap)obj[2];
		
		
		List accList = new ArrayList();
		for(int i =0;i<accTypeInfoList.size();i++){
			HashMap acctypeMap = (HashMap)accTypeInfoList.get(i);
			String classCode = String.valueOf(acctypeMap.get("classCode"));						//classCode
			HashMap map = (HashMap)accMainMap.get(classCode);
			
			//�޷������ʾ
			if(takeBrowNo != null && !"".equals(takeBrowNo)){
				if(map == null || map.size()==0){
					continue;
				}
			}
			
			//��ʾ������Ŀ��ϸ
			if(showItemDetail == null || "".equals(showItemDetail)){
				if("1".equals(acctypeMap.get("isCalculate"))){
					continue;
				}
			}
			
			//��Ŀ�ȼ���ʼ
			String classCodes = acctypeMap.get("classCode").toString();
			int count = 1;
			if(classCode.length()>10){
				count = (classCodes.length()-5)/5 ;
			}
			//��Ŀ�ȼ�����
			if(levelStart!=null && !"".equals(levelStart) && levelEnd!=null && !"".equals(levelEnd)){
				if(!(Integer.parseInt(levelStart)<=count && count<=Integer.parseInt(levelEnd))){
					continue;
				}
			}

			if(map != null && map.size()>0){
				Iterator iter  = map.entrySet().iterator();
				while(iter.hasNext()){
					Entry entry = (Entry)iter.next();
					if("sumBalanceAmountBase".equals(entry.getKey())){
						Object o = entry.getValue();
						if(o != null && !"".equals(o)){
							Double moneys = Double.parseDouble(o.toString());
							if(moneys>0){
								acctypeMap.put("isflag","��");
							}else if(moneys<0){
								acctypeMap.put("isflag","��");
							}else{
								acctypeMap.put("isflag","ƽ");
							}
						}else{
							acctypeMap.put("isflag","ƽ");
						}
					}
					if(entry.getKey().toString().indexOf("sumBalanceAmount")!=-1){
						acctypeMap.put(entry.getKey(), dealDataDouble(entry.getValue().toString(), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
					}else{
						acctypeMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
			accList.add(acctypeMap);
		}
		
		//�ϼƽ����д���
		HashMap newTotalMap = new HashMap();
		if(totalMap != null && totalMap.size()>0){
			Iterator iter  = totalMap.entrySet().iterator();
			while(iter.hasNext()){
				Entry entry = (Entry)iter.next();
				if("sumBalanceAmountBase".equals(entry.getKey())){
					Object o = entry.getValue();
					if(o != null && !"".equals(o)){
						Double moneys = Double.parseDouble(o.toString());
						if(moneys>0){
							newTotalMap.put("isflag","��");
						}else if(moneys<0){
							newTotalMap.put("isflag","��");
						}else{
							newTotalMap.put("isflag","ƽ");
						}
					}else{
						newTotalMap.put("isflag","ƽ");
					}
				}
				if(entry.getKey().toString().indexOf("sumBalanceAmount")!=-1){
					newTotalMap.put(entry.getKey(), dealDataDouble(entry.getValue().toString(), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
				}else{
					newTotalMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		//��������
		obj = new Object[]{accList,newTotalMap,currencyValue};
		result.setRetVal(obj);
		return result;
	}
	
	
	/**
	 * ������Ŀ����
	 * 1.��ѯ���ڷ����Ľ�����
	 * 2.��ѯ��ƿ�Ŀ���ڳ���������
	 * 3.������ݣ����ݵ���ϣ�ͳ�Ʊ����ۼƽ������ĩ��
	 * @param conMap
	 * @return
	 */
	protected Result accCalculateBalance(final HashMap<String,String> conMap,final MOperation mop,final LoginBean loginBean){
		
		/* ��Ҵ��� */
		String currencyName = conMap.get("currencyName");							//�ұ�('isBase'=��λ�ң�''=�ۺϱ�λ�ң�'all'=���бұ�'��ҵ�id'=���)
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		//�������
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		
		final String takeBrowNo = conMap.get("takeBrowNo");							//���Ϊ�㲻��ʾ
		final String balanceAndTakeBrowNo = conMap.get("balanceAndTakeBrowNo");		//��ʾ������Ϊ��ļ�¼
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							/* �������� */
							String yearStart = conMap.get("periodYearStart");						//����ڼ俪ʼ��
							String periodStart = conMap.get("periodStart");							//����ڼ俪ʼ
							String yearEnd = conMap.get("periodYearEnd");							//����ڼ������
							String periodEnd = conMap.get("periodEnd");								//����ڼ����	
							String accCode = conMap.get("accCode");									//��ƿ�Ŀ
							String currencyName = conMap.get("currencyName");						//�ұ�
							String itemSort = conMap.get("itemSort");								//��Ŀ���
							String itemCodeStart = conMap.get("itemCodeStart");						//��Ŀ���뿪ʼ
							String itemCodeEnd = conMap.get("itemCodeEnd");							//��Ŀ�������
							String levelStart = conMap.get("levelStart");							//��Ŀ����ʼ
							String binderNo = conMap.get("binderNo");								//����δ����ƾ֤
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
							
							String startTime = "";
							String endTime = "";
							if(Integer.valueOf(periodStart)<10){
								startTime = yearStart+"-0"+periodStart;
							}else{
								startTime = yearStart+"-"+periodStart;
							}
							if(Integer.valueOf(periodEnd)<10){
								endTime = yearEnd+"-0"+periodEnd;
							}else{
								endTime = yearEnd+"-"+periodEnd;
							}
							
							/* �������� */
							String accStr = "";
							String condition = scopeSql(mop, loginBean);
							if(binderNo == null || "".equals(binderNo)){
	            				//������δ����ƾ֤
								condition += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
	            			}
							String itemType = "";					//����ֵ
							String isType = "";						//�������
							String acctypeItem = "";
							if("DepartmentCode".equals(itemSort)){
								//�������
								itemType = "tblAccDetail.DepartmentCode";
								isType = "tblAccTypeInfo.IsDept=1";
								acctypeItem = "tblAccTypeInfo.DepartmentCode";
							}else if("EmployeeID".equals(itemSort)){
								//���ְԱ
								itemType = "tblAccDetail.EmployeeID";
								isType = "tblAccTypeInfo.IsPersonal=1";
								acctypeItem = "tblAccTypeInfo.EmployeeID";
							}else if("StockCode".equals(itemSort)){
								//����ֿ�
								itemType = "tblAccDetail.StockCode";
								isType = "tblAccTypeInfo.isStock=1";
								acctypeItem = "tblAccTypeInfo.StockCode";
							}else if("ClientCode".equals(itemSort) || "SuplierCode".equals(itemSort)){
								//����ͻ����߹�Ӧ��
								itemType = "tblAccDetail.CompanyCode";
								if("ClientCode".equals(itemSort)){
									isType = "tblAccTypeInfo.IsClient=1";
									acctypeItem = "tblAccTypeInfo.ClientCode";
								}else if("SuplierCode".equals(itemSort)){
									isType = "tblAccTypeInfo.IsProvider=1";
									acctypeItem = "tblAccTypeInfo.SuplierCode";
								}
							}else if("ProjectCode".equals(itemSort)){
								//���ְԱ
								itemType = "tblAccDetail.ProjectCode";
								isType = "tblAccTypeInfo.IsProject=1";
								acctypeItem = "tblAccTypeInfo.ProjectCode";
							}
							accStr = accStr+" AND "+acctypeItem+" IS NOT NULL AND "+acctypeItem+" != ''";
							boolean itemStartFlag = false;
							boolean itemEndFlag = false;
							if(itemCodeStart != null && !"".equals(itemCodeStart)){
								itemStartFlag = true;
							}
							if(itemCodeEnd != null && !"".equals(itemCodeEnd)){
								itemEndFlag = true;
							}
							String consql = "";
							if("DepartmentCode".equals(itemSort)){
								if(itemStartFlag){
									consql += " AND "+itemType+">=(select top 1 dept.classCode from tblDepartment dept where dept.deptCode='"+itemCodeStart+"' order by dept.classCode)";
								}
								if(itemEndFlag){
									consql += " AND "+itemType+"<=(select top 1 dept.classCode from tblDepartment dept where dept.deptCode='"+itemCodeEnd+"' order by dept.classCode DESC)";
								}
							}else if("EmployeeID".equals(itemSort)){
								if(itemStartFlag){
									consql += " AND "+itemType+">=(select top 1 emp.id from tblEmployee emp where emp.EmpNumber='"+itemCodeStart+"' order by tblEmployee.id)";
								}
								if(itemEndFlag){
									consql += " AND "+itemType+"<=(select top 1 emp.id from tblEmployee emp where emp.EmpNumber='"+itemCodeEnd+"' order by tblEmployee.id DESC)";
								}
							}else if("ClientCode".equals(itemSort)){
								if(itemStartFlag){
									consql += " AND "+itemType+">=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+itemCodeStart+"' order by com.classCode)";
								}
								if(itemEndFlag){
									consql += " AND "+itemType+"<=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+itemCodeEnd+"' order by com.classCode DESC)";
								}
							}else if("SuplierCode".equals(itemSort)){
								if(itemStartFlag){
									consql += " AND "+itemType+">=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+itemCodeStart+"' order by com.classCode)";
								}
								if(itemEndFlag){
									consql += " AND "+itemType+"<=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+itemCodeEnd+"' order by com.classCode DESC)";
								}
							}else if("StockCode".equals(itemSort)){
								if(itemStartFlag){
									consql += " AND "+itemType+">=(select top 1 stock.classCode from tblStock stock where stock.stockNumber='"+itemCodeStart+"' order by stock.classCode)";
								}
								if(itemEndFlag){
									consql += " AND "+itemType+"<=(select top 1 stock.classCode from tblStock stock where stock.stockNumber='"+itemCodeEnd+"' order by stock.classCode DESC)";
								}
							}
							condition += consql;
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//����ʾ���ÿ�Ŀ
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
							if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//��λ��
                            		condition += " AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+searchCurrency+"') ";
                            	}else{
                            		//�������ʱ
                            		condition += " AND tblAccDetail.Currency='"+searchCurrency+"' ";
                            	}
                            }
							/**
							 * ��ѯ���ڽ�����ϼ�
							 */
                            StringBuffer sql = new StringBuffer("SELECT tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccCode,");
                            sql.append("SUM(tblAccDetail.DebitAmount) as sumDebitAmountBase,SUM(tblAccDetail.DebitCurrencyAmount) as sumDebitAmount,");
                            sql.append("SUM(tblAccDetail.LendAmount) as sumLendAmountBase,SUM(tblAccDetail.LendCurrencyAmount) as sumLendAmount,");
                            sql.append("SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount) AS sumdisAmountBase,");
                            sql.append("SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount) AS sumdisAmount");
                            if("all".equals(currencyValue)){
								//���бұ����ʽʱ�Ա��ֽ��з���
								sql.append(",ISNULL(tblAccDetail.Currency,'') AS Currency");
							}
                            sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id ");
                            sql.append(" JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode ");
                            sql.append(" WHERE tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+accCode+"')+'%' ");
                            sql.append(condition);
                            sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
                            sql.append(" GROUP BY tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccCode ");
                            if("all".equals(currencyValue)){
								sql.append(",tblAccDetail.Currency ");
							}
                            sql.append(" ORDER BY tblAccMain.CredYear,tblAccMain.period,tblAccDetail.AccCode");
                            System.out.println("���ڽ��sql:"+sql);
                            HashMap curPeriodMap = new HashMap();
                            rset = st.executeQuery(sql.toString());
                            while (rset.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
                            		}
                            	}
                            	
                            	//��ѡ��ıұ��ǡ������бұ����ʽ
								String lineStr = "sumDebitAmountBase;sumLendAmountBase;sumdisAmountBase;sumDebitAmount;sumLendAmount;sumdisAmount;";
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumdisAmount"};
                            		String base = "";
                            		if("".equals(map.get("Currency"))){
                            			base = "Base";
                            		}
                            		for(String s : curs){
                        				map.put(s+"_"+map.get("Currency"), map.get(s+base));
                        				lineStr += s+"_"+map.get("Currency")+";";
                        			}
                        		}
                            	
                            	/* ��������һ����¼ʱ������ */
                            	HashMap oldMap = (HashMap)curPeriodMap.get(map.get("AccCode")+"_"+map.get("CredYear")+"_"+map.get("Period"));
                            	if(oldMap != null && oldMap.size()>0){
                            		Iterator oldIter = oldMap.entrySet().iterator();
                            		while(oldIter.hasNext()){
                            			Entry oldEntry = (Entry)oldIter.next();
                            			String oldKey = String.valueOf(oldEntry.getKey());
                            			if(!"CredYear".equals(oldKey)&&!"Period".equals(oldKey)&&!"AccCode".equals(oldKey)&&!"Currency".equals(oldKey)){
                            				Object oldObject = oldEntry.getValue();
                            				if("sumDebitAmountBase".equals(oldKey) || "sumLendAmountBase".equals(oldKey) 
                            						|| "sumdisAmountBase".equals(oldKey)){
                            					String oldMoney = "0";
                            					if(oldObject != null && !"".equals(oldObject)){
                            						oldMoney = String.valueOf(oldObject);
                            					}
                            					Object newObject = map.get(oldKey);
                            					if(newObject != null && !"".equals(newObject)){
                            						oldMoney = new BigDecimal(oldMoney.toString()).add(new BigDecimal(newObject.toString())).toString();
                            					}
                            					oldObject = oldMoney;
                            				}
                            				map.put(oldKey,oldObject);
                            			}
                            		}
                            	}
                            	curPeriodMap.put(map.get("AccCode")+"_"+map.get("CredYear")+"_"+map.get("Period"), map);
                            }
							
                            /**
                             * ��ѯ��ƿ�Ŀ
                             */
                            sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber AS AccCode,tblLanguage.zh_cn as AccFullName");
                            sql.append(" FROM tblAccTypeInfo JOIN tblLanguage ON tblAccTypeInfo.AccFullName=tblLanguage.id ");
                            sql.append(" WHERE tblAccTypeInfo.classCode like (SELECT classCode FROM tblAccTypeInfo WHERE AccNumber='"+accCode+"')+'%'");
                            sql.append(" AND isnull(tblAccTypeInfo.isCalculate,'')=1 "+accStr);
                            
                            if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//����ʾ���ÿ�Ŀ
                            	sql.append(" AND tblAccTypeInfo.statusId=0 ");
							}
                            sql.append(scopeSql(mop, loginBean));
                            sql.append(" order by tblAccTypeInfo.AccNumber");
                            System.out.println("��ƿ�Ŀsql:"+sql.toString());
                            List accTypeInfoList = new ArrayList();
                            rset = st.executeQuery(sql.toString());
                            while(rset.next()){
                            	HashMap map = new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				map.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	accTypeInfoList.add(map);
                            }
                            
                            
                            /**
							 * ��ѯ����ƿ�Ŀ���ڳ����
							 */
							sql = new StringBuffer("SELECT tblAccDetail.AccCode,");
							if("all".equals(currencyValue)){
								//���бұ����ʽʱ�Ա��ֽ��з���
								sql.append("ISNULL(tblAccDetail.Currency,'') AS Currency,");
							}
							sql.append("SUM(tblAccDetail.DebitAmount) AS sumDebitAmountBase,SUM(tblAccDetail.LendAmount) AS sumLendAmountBase,");
							sql.append("SUM(tblAccDetail.DebitCurrencyAmount) AS sumDebitAmount,SUM(tblAccDetail.LendCurrencyAmount) AS sumLendAmount,");
							sql.append("SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount) AS sumdisAmountBase,");
							sql.append("SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount) AS sumdisAmount ");
							sql.append(" FROM tblAccDetail JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode");
							sql.append(" JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id ");
							sql.append(" WHERE tblAccMain.CredYear<="+yearStart+" AND tblAccMain.Period<"+periodStart);
							sql.append(condition);
							sql.append(" AND (isnull(tblAccTypeInfo.isCalculate,'')=1)");
							sql.append(" AND tblAccTypeInfo.classCode LIKE (SELECT classCode FROM tblAccTypeInfo WHERE AccNumber='"+accCode+"')+'%'");
							sql.append(" GROUP BY tblAccDetail.AccCode ");
                            if("all".equals(currencyValue)){
                            	//���бұ����ʽ
                            	sql.append(",tblAccDetail.currency");
                            }
                            sql.append(" ORDER BY tblAccDetail.AccCode");
							System.out.println("��ѯ�ڳ���"+sql);
							rset = st.executeQuery(sql.toString());
							HashMap initMap = new HashMap();
							while (rset.next()) {
								HashMap map = new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				map.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC){
                        					String strvalue = String.valueOf(obj);
                        					if (strvalue.indexOf(".")>0){
                        						strvalue = strvalue.substring(0,strvalue.indexOf(".")+Integer.valueOf(BaseEnv.systemSet.get("DigitsAmount").getSetting())+1);
                        					}
                        					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                        						strvalue = "";
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), obj);
                            			}
	                        		}
	                        	}
	                        	//��ѡ��ıұ��ǡ������бұ����ʽ
								String lineStr = "sumDebitAmountBase;sumLendAmountBase;sumdisAmountBase;sumDebitAmount;sumLendAmount;sumdisAmount;";
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumdisAmount"};
                            		String base = "";
                            		if("".equals(map.get("Currency"))){
                            			base = "Base";
                            		}
                            		for(String s : curs){
                        				map.put(s+"_"+map.get("Currency"), map.get(s+base));
                        				lineStr += s+"_"+map.get("Currency")+";";
                        			}
                            	}
                            	/* ��������һ����¼ʱ������ */
                            	HashMap oldMap = (HashMap)initMap.get("init_"+map.get("AccCode"));
                            	if(oldMap != null && oldMap.size()>0){
                            		Iterator oldIter = oldMap.entrySet().iterator();
                            		while(oldIter.hasNext()){
                            			Entry oldEntry = (Entry)oldIter.next();
                            			String oldKey = String.valueOf(oldEntry.getKey());
                            			if(!"AccCode".equals(oldKey)&&!"Currency".equals(oldKey)){
                            				Object oldObject = oldEntry.getValue();
                            				if("sumDebitAmountBase".equals(oldKey) || "sumLendAmountBase".equals(oldKey) 
                            						|| "sumdisAmountBase".equals(oldKey)){
                            					String oldMoney = "0";
                            					if(oldObject != null && !"".equals(oldObject)){
                            						oldMoney = String.valueOf(oldObject);
                            					}
                            					Object newObject = map.get(oldKey);
                            					if(newObject != null && !"".equals(newObject)){
                            						oldMoney = new BigDecimal(oldMoney.toString()).add(new BigDecimal(newObject.toString())).toString();
                            					}
                            					oldObject = oldMoney;
                            				}
                            				map.put(oldKey,oldObject);
                            			}
                            		}
                            	}
	                        	initMap.put("init_"+map.get("AccCode"), map);
							}
							
							
    						/**
                             * ��ѯ���������Ļ���ڼ�
                             */
                            sql = new StringBuffer("SELECT ap.AccYear,ap.AccPeriod,ap.periodEnd FROM tblAccPeriod ap WHERE ");
                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,ap.AccYear)+'-'+CONVERT(VARCHAR,ap.AccPeriod)+'-01',120),120)";
                            sql.append(str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
    						sql.append(" ORDER BY ap.AccYear,ap.AccPeriod");
    						System.out.println("����ڼ䣺"+sql);
    						rset=st.executeQuery(sql.toString());
    						List periodList = new ArrayList();
    						while (rset.next()) {
    							HashMap map=new HashMap();
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rset.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rset.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	periodList.add(map);
    						}
                            
							/* �������� */
							Object[] obj = new Object[]{curPeriodMap, accTypeInfoList, initMap, periodList};
                            result.setRetVal(obj);
							
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt AccCalculateBalance:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		
		/* ȡ���� */
		Object[] obj = (Object[])result.retVal;
		HashMap curPeriodMap = (HashMap)obj[0];
		List accTypeInfoList = (ArrayList)obj[1];
		HashMap initMap = (HashMap)obj[2];
		List periodList = (ArrayList)obj[3];
		
		/**
		 * ��������
		 */
		
		List accList = new ArrayList();
		String strs = "sumDebitAmountBase;sumDebitAmount;sumLendAmountBase;sumLendAmount;sumdisAmountBase;sumdisAmount;";
		if("all".equals(currencyValue)){
			String[] strSingle = strs.split(";");
			for(int k=0;k<currList.size();k++){
				String[] currStr = (String[])currList.get(k);
				String curId = currStr[0];
				if("true".equals(currStr[2])){
					//��λ��
					curId = "";
				}
				for(int j=0;j<strSingle.length;j++){
					strs += strSingle[j]+"_"+curId+";";
				}
			}
		}
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			HashMap accMap = new LinkedHashMap();
			//ѭ������ڼ�
			for(int j = 0;j<periodList.size();j++){
				HashMap periodMap = (HashMap)periodList.get(j);
				HashMap currMap = (HashMap)curPeriodMap.get(map.get("AccCode")+"_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
				currMap = currMap==null ? new HashMap() : currMap;
				String[] lineStr = strs.split(";");
				if(j == 0){
					//��ֵ���ڳ�
					HashMap init = (HashMap)initMap.get("init_"+map.get("AccCode"));
					if(init!=null && init.size()>0){
						for(int k = 0;k<lineStr.length;k++){
							currMap.put("init_"+lineStr[k], init.get(lineStr[k]));
						}
					}
				}else{
					//ȡ��һ�ڼ����ĩ�����Ϊ���ڵ��ڳ����
					HashMap preMap = (HashMap)periodList.get(j-1);
					HashMap currPreMap = (HashMap)accMap.get(map.get("AccCode")+"_"+preMap.get("AccYear")+"_"+preMap.get("AccPeriod"));
					if(currPreMap!= null && currPreMap.size()>0){
						for(int k = 0;k<lineStr.length;k++){
							currMap.put("init_"+lineStr[k], currPreMap.get("end_"+lineStr[k]));
						}
					}
				}
				/* �õ������ۼ� */
				for(int k = 0;k<lineStr.length;k++){
					/* ���ۼƣ��ڳ�����+�����ۼƣ� */
					String money = "0";
					Object o = currMap.get("init_"+lineStr[k]);
					if(o != null && !"".equals(o)){
						money = o.toString();
					}
					if(currMap != null && currMap.size()>0){
						//���ڱ��ڽ�����
						o = currMap.get(lineStr[k]);
						if(o != null && !"".equals(o)){
							money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
						}
					}
					currMap.put("year_"+lineStr[k], dealDataDouble(String.valueOf(money), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
				}
				
				/* �õ���ĩ��� ���ڳ����-���ڷ����*/
				for(int k = 0;k<lineStr.length;k++){
					String money = "0";
					Object o = currMap.get("init_"+lineStr[k]);
					if(o != null && !"".equals(o)){
						money = o.toString();
					}
					if(currMap != null && currMap.size()>0){
						//���ڱ��ڽ�����
						o = currMap.get(lineStr[k]);
						if(o != null && !"".equals(o)){
							money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
						}
					}
					currMap.put("end_"+lineStr[k], money);
				}
				currMap.put("AccYear",periodMap.get("AccYear"));
				currMap.put("AccPeriod",periodMap.get("AccPeriod"));
				currMap.put("AccCode", map.get("AccCode"));
				currMap.put("AccFullName", map.get("AccFullName"));
				
				//���Ϊ�㲻��ʾ
				if(takeBrowNo != null && !"".equals(takeBrowNo)){
					if(Double.valueOf(currMap.get("end_sumdisAmountBase").toString())==0){
						continue;
					}
				}
				//��ʾ������Ϊ��ļ�¼
				if(balanceAndTakeBrowNo == null || "".equals(balanceAndTakeBrowNo)){
					Object sumDebit = currMap.get("sumDebitAmountBase");
					Object sumLend = currMap.get("sumLendAmountBase");
					if(sumDebit == null && sumLend == null){
						continue;
					}
				}
				//******�ж���ĩ�����*****//
				if(!"".equals(currMap.get("end_sumDebitAmountBase")) &&  !"".equals(currMap.get("end_sumLendAmountBase"))){
					double endDebit = Double.parseDouble((String)currMap.get("end_sumDebitAmountBase"));
					double endLend = Double.parseDouble((String)currMap.get("end_sumLendAmountBase"));
					if(endDebit > endLend){
						currMap.put("end_sumDebitAmountBase",(endDebit-endLend));
						currMap.put("end_sumLendAmountBase",0);
					} else if(endDebit < endLend){
						currMap.put("end_sumDebitAmountBase",0);
						currMap.put("end_sumLendAmountBase",(endLend-endDebit));
					}
				}
				//*********end********//
				accMap.put(map.get("AccCode")+"_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"), currMap);
			}
			if(accMap.size()!=0){
				accList.add(accMap);
			}
		}
		
		
		/* �������� */
		for(int i=0;i<accList.size();i++){
			HashMap accMap = (LinkedHashMap)accList.get(i);
			if(accMap.size()==0){
				accList.remove(i);
			}
			Iterator iter = accMap.entrySet().iterator();
			while(iter.hasNext()){
				Entry entry = (Entry)iter.next();
				HashMap currMap = (HashMap)entry.getValue();
				Iterator iterNext = currMap.entrySet().iterator();
				while(iterNext.hasNext()){
					Entry entryNext = (Entry)iterNext.next();
					String key = String.valueOf(entryNext.getKey());
					if(key != null && key.indexOf("sum")!=-1){
						currMap.put(key, dealDataDouble(String.valueOf(entryNext.getValue()==null?"":entryNext.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					}
				}
				
			}
			
		}
		
//		for(int i=0;i<accList.size();i++){
//			HashMap accMap = (LinkedHashMap)accList.get(i);
//			if(accMap.size()==0){
//				accList.remove(i);
//			}
//			Iterator iter = accMap.entrySet().iterator();
//			while(iter.hasNext()){
//				Entry entry = (Entry)iter.next();
//				HashMap currMap = (HashMap)entry.getValue();
//				String money = "0";
//				Object o = null;
//				String[] sumStrs = new String[]{"init_sumDebitAmount;init_sumLendAmount;init_debit;init_lend","end_sumDebitAmount;end_sumLendAmount;end_debit;end_lend",
//						"init_sumDebitCurrencyAmount;init_sumLendCurrencyAmount;init_debitCurr;init_lendCurr","end_sumDebitCurrencyAmount;end_sumLendCurrencyAmount;end_debitCurr;end_lendCurr"};
//				for(String s : sumStrs){
//					money = "0";
//					String[] sumStr = s.split(";");
//					o = currMap.get(sumStr[0]);
//					if(o != null && !"".equals(o)){
//						money = o.toString();
//					}
//					o = currMap.get(sumStr[1]);
//					if(o != null && !"".equals(o)){
//						money = new BigDecimal(money).subtract(new BigDecimal(o.toString())).toString();
//					}
//					if(Double.valueOf(money)>0){
//						//�跽
//						currMap.put(sumStr[2], dealDataDouble(String.valueOf(money), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
//					}
//					if(Double.valueOf(money)<0){
//						//����
//						currMap.put(sumStr[3], dealDataDouble(String.valueOf(money), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
//					}
//				}
//			}
//		}
		obj = new Object[]{accList,currencyValue};
		result.setRetVal(obj);
		return result;
	}
	
	/**
	 * �����û���Ų�ѯ�����������û�id
	 * @param employeeStart				��ſ�ʼ
	 * @param employeeEnd				��Ž���
	 * @return
	 */
	public Result queryEmployeeAll(final Connection conn,final String employeeStart ,final String employeeEnd ){
		final Result result = new Result();
		try {
			StringBuffer sql = new StringBuffer("SELECT id FROM tblEmployee WHERE 1=1 ");
			if(employeeStart!=null && !"".equals(employeeStart)){
				sql.append(" AND EmpNumber>='"+employeeStart+"' ");
			}
			if(employeeEnd!=null && !"".equals(employeeEnd)){
				sql.append(" AND EmpNumber<='"+employeeEnd+"' ");
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			List<String> list = new ArrayList<String>();
			while(rs.next()){
				String str = rs.getString("id");
				list.add(str);
			}
			result.retVal = list;
		} catch (Exception ex) {
			ex.printStackTrace();
			BaseEnv.log.error("FinanceReportMgt queryEmployeeAll:",ex) ;
			result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return result;
	}
	
	
	/**
	 * ���ݱ�Ų�ѯ�Ƿ��Ǻ�����Ŀ������㲿�ţ�����ְԱ������ֿ⣬����ͻ������㹩Ӧ�̵ȣ�
	 * @param accCode
	 * @return
	 */
	public Result queryAccTypeInfoItem(final String accNumber){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT classCode,AccNumber,IsDept,IsPersonal,IsClient,IsProject,IsProvider,isStock FROM tblAccTypeInfo WHERE AccNumber='"+accNumber+"'");
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							HashMap map=new HashMap();
							if(rset.next()){
								for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				map.put(rset.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			map.put(rset.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
							}
							result.setRetVal(map);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryAccTypeInfoItem:",ex) ;
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
	 * ��ѯ�����ڼ�
	 * @return
	 */
	public Result getCurrentlyPeriod(){
		final Result result = new Result();
		DBUtil.execute(new IfDB(){
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						String sql = "select AccYear,AccPeriod from tblAccPeriod where IsBegin = 1";
						Statement state = con.createStatement();
						ResultSet rs = state.executeQuery(sql);
						Integer[] str = new Integer[2];
						if(rs.next()){
							str[0] = rs.getInt("AccYear");
							str[1] = rs.getInt("AccPeriod");
						}
						result.setRetVal(str);
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}
	
	/**
	 * ��ѯ�Ƿ��Ǳ�λ�ң�����Ƿ���'isBase',��Ȼ������ҵ�id��
	 * @param id
	 * @return
	 */
	public Result queryIsBase(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							if(id == null || "".equals(id)){
								//�ۺϱ�λ��
								result.setRetVal("");
							}else if("all".equals(id)){
								//���бұ����ʽ
								result.setRetVal("all");
							}else if("currency".equals(id)){
								//���бұ�
								result.setRetVal("currency");
							}else{
								StringBuffer sql = new StringBuffer("SELECT IsBaseCurrency FROM tblCurrency where id='"+id+"'");
								Statement st = conn.createStatement();
								ResultSet rs = st.executeQuery(sql.toString());
								String falg = id;
								if(rs.next()){
									Integer isBaseCurrency = rs.getInt("IsBaseCurrency");
									if(isBaseCurrency==1){
										falg = "isBase";
									}
								}
								result.setRetVal(falg);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryIsBase:",ex) ;
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
	 * ��ѯ���ֵ�����
	 * @return
	 */
	public Result queryCurrencyName(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT CurrencyName FROM tblCurrency WHERE id='"+id+"'");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							String name = "";
							if(rs.next()){
								name = rs.getString("CurrencyName");
							}
							result.retVal = name;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("FinanceReportMgt queryCurrencyName:",ex) ;
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
	 * ��ȡ��ǰ�û����õĻ�ƿ�Ŀ��ϽȨ��
	 * @param mop
	 * @param loginBean
	 */
	public String scopeSql(MOperation mop, LoginBean loginBean){
		
		/* Ȩ�޿��� */
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
		scopeRight.addAll(loginBean.getAllScopeRight());
		String scopeRightSql = DynDBManager.scopeRightHandler("tblAccTypeInfo", "TABLELIST", "", loginBean.getId(), scopeRight, "select * from tblAccTypeInfo where 1=1 ", "endClass","");
		scopeRightSql = scopeRightSql.substring(scopeRightSql.indexOf("where 1=1")+"where 1=1".length());
		return scopeRightSql;
	}
}
