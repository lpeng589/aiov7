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
 * 财务报表 数据库操作类
 * <p>Title:所有财务报表处理的mgt</p> 
 * <p>Description: 总分类账报表，明细分类账报表</p>
 *
 * @Date:2013-05-21
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class FinanceReportMgt extends AIODBManager {
	
	/**
	 * 查询所有币种类型
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
	 * 查询是否本位币
	 * @param id   币别ID
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
	 * 查询最小期间和最大期间
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
	 * 总分类账报表查询
	 * 1、查询范围内的所有科目。
	 * 2、查询范围内的所有会计期间。
	 * 3、从总分类账表tblAccBalance查询已经统计的数据 
	 * 4、查询未过账凭证明细数据——从凭证明细表取范围内所有数据 分组条件，科目，会计期间年，月,凭证字，外币，取数包括汇总借贷，最小凭证号，最大凭证号
	 * 5、组合数据展示
	 * @param periodYearStart 会计期间开始年
	 * @param periodStart 开始会计期间
	 * @param periodYearEnd 会计期间年结束
	 * @param periodEnd 结束会计期间
	 * @param levelStart 开始等级
	 * @param levelEnd 结束等级
	 * @param acctypeCodeStart 开始科目
	 * @param acctypeCodeEnd 结束科目
	 * @param currencyName 币种
	 * @param takeBrowNo 无发生额不显示 为空不显示
	 * @param balanceAndTakeBrowNo 无发生额并且余额为零 为空不显示
	 * @param binderNo 包括未过帐凭证
	 * @param showIsItem 显示核算科目
	 * @param loginBean 用户登陆信息
	 * @param showBanAccTypeInfo 显示禁用科目
	 * @param pageSize 每页多少条数据
	 * @param pageNo 当前第几页
	 * @return
	 */
	public Result queryAccBalanceData(final String periodYearStart,final String periodStart,
			final String periodYearEnd,final String periodEnd,final String levelStart,
			final String levelEnd,final String acctypeCodeStart,final String acctypeCodeEnd,
			final String currencyName,final String takeBrowNo,final String balanceAndTakeBrowNo,
			final String binderNo,final String showIsItem,final String showBanAccTypeInfo,
			final LoginBean loginBean,final Integer pageSize,final Integer pageNo,final MOperation mop){
		//对币别进行处理
		String currencyNames = currencyName; 									//币别('isBase'=本位币，''=综合本位币，'all'=所有币别，'外币的id'=外币)
		Result rs = queryIsBase(currencyNames);
		currencyNames = rs.retVal.toString();
		//所有外币
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
						
						/* 处理开始期间和结束期间格式为（2013-08） */
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
                         * 期间过滤条件
                         */
                        String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,ap.AccYear)+'-'+CONVERT(VARCHAR,ap.AccPeriod)+'-01',120),120)";
                        String periodCondition = " AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"'";
                        
                        HashMap accMap = new HashMap();
                        String condition = scopeSql(mop, loginBean);				//过滤数据
                        
						/**
						 * 查询所有满足条件的会计科目
						 */
						StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,l.zh_cn as AccFullName,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,");
						sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.jdFlag FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 ");
						if(acctypeCodeStart != null && !"".equals(acctypeCodeStart)){
							//科目开始
							condition += " AND (tblAccTypeInfo.AccNumber>='"+acctypeCodeStart+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeStart+"')+'%')";
						}
						if(acctypeCodeEnd != null && !"".equals(acctypeCodeEnd)){
							//科目结束
							condition += " AND (tblAccTypeInfo.AccNumber<='"+acctypeCodeEnd+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeEnd+"')+'%')";
						}
						if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
							//不显示禁用科目
							condition += " AND tblAccTypeInfo.statusId=0";
						}
						sql.append(condition);
						
						sql.append(" ORDER BY tblAccTypeInfo.AccNumber");
						System.out.println("科目查询SQL："+sql.toString());
                        /* 执行查询并且保存满足条件的科目 */
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
                         * 查询满足条件的会计期间
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
                         * 包括未过账凭证，查询指定期间的未过账数据金额
                         */
                        HashMap noBindMap = new HashMap();				//保存期间中的未过账的数据
                        HashMap noBindInitMap = new HashMap();			//保存开始期间之前未过账的数据
						if(binderNo != null && !"".equals(binderNo)){
							String initSql = "";						//查询期初未过账数据sql							
							
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
                            //当选择外币不是<综合本位币><所有币种>时进行过滤
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//本位币
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
                            	//所有币种要对外币进行分组
                            	sql.append(",tblAccDetail.Currency");
                            	initSql += ",tblAccDetail.Currency";
                            }
                            sql.append(" ORDER BY tblAccTypeInfo.classCode,tblAccMain.CredYear,tblAccMain.Period");
                            BaseEnv.log.error("未过账凭证数据金额："+sql.toString());
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
                            	//当选择的币别是——所有币别多栏式
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
                            		if("".equals(periodMap.get("Currency"))){
                            			//本位币时，外币的金额等于当前本位币的金额
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					periodMap.put(s+"_", periodMap.get(s+"Amount"));
                            				}else{
                            					periodMap.put(s+"_", periodMap.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//其它外币
                            			for(String s : curs){
                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
                            			}
                            		}
                            	}
                            	noBindMap.put(periodMap.get("CredYear")+"_"+periodMap.get("Period")+"_"+periodMap.get("classCode"), periodMap);
                            }
                            
                            //查询期初数据时进行查询会计期间之前未过账的数据进行统计
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
                            	//当选择的币别是——所有币别多栏式
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
                            		if("".equals(periodMap.get("Currency"))){
                            			//本位币时，外币的金额等于当前本位币的金额
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					periodMap.put(s+"_", periodMap.get(s+"Amount"));
                            				}else{
                            					periodMap.put(s+"_", periodMap.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//其它外币
                            			for(String s : curs){
                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
                            			}
                            		}
                            	}
                            	noBindInitMap.put(periodMap.get("classCode"), periodMap);
                            }
						}
                        
						/**
                         * 查询科目的各期间的金额（tblAccBalance）
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
                        		//本位币
                        		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+currencyName+"') ");
                        	}else{
                        		//其它外币进行过滤
                        		sql.append(" AND tblAccBalance.CurType='"+currencyName+"' ");
                        	}
                        }
                        sql.append(condition);
                        str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccBalance.Nyear)+'-'+CONVERT(VARCHAR,tblAccBalance.Period)+'-01',120),120)";
                        sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"' AND tblAccTypeInfo.isCatalog=0 and ISNULL(tblAccTypeInfo.isCalculateParent,0) != 1  AND (tblAccBalance.DepartmentCode is null or tblAccBalance.DepartmentCode='')");
                        sql.append(" ORDER BY tblAccTypeInfo.classCode,tblAccBalance.Nyear,tblAccBalance.Period");
                        System.out.println("查询总分类账各会计科目的各会计期间的金额："+sql.toString());
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
                        	
                        	//当选择的币别是——所有币别多栏式
                        	if("all".equals(currencyValue)){
                        		String[] curs = new String[]{"PeriodIni","PeriodDebitSum","PeriodCreditSum","CurrYIniDebitSum","CurrYIniCreditSum","PeriodBala","CurrYIni","PeriodDCBala"};
                        		if("".equals(map.get("CurType"))){
                        			//本位币时，外币的金额等于当前本位币的金额
                        			for(String s : curs){
                        				if("CurrYIni".equals(s)){
                        					map.put(s+"_", map.get(s+"Amount"));
                        				}else{
                        					map.put(s+"_", map.get(s+"Base"));
                        				}
                        			}
                        		}else{
                        			//其它外币
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("CurType"), map.get(s));
                        			}
                        		}
                        	}
                        	
            				//查询此科目的上级并附值到上级中
        					String classCode = map.get("classCode").toString();
        					for(int j=0;j<(classCode.length()-5)/5;j++){
        						//会计科目classCode进行循环获取上级
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
    								//当存在上级时，取上级金额数据累加
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
    								//当不存在上一级时，创建新的Map保存数据
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
						 * 查询凭证字号最大值，最小值
						 */
						sql = new StringBuffer("SELECT accmain.CredYear,accmain.Period,accmain.CredTypeID,acctype.classCode,");
						sql.append("MIN(OrderNo) AS MinOrderNo,MAX(OrderNo) AS MaxOrderNo FROM tblAccDetail detail ");
						sql.append("LEFT JOIN tblAccTypeInfo acctype ON detail.AccCode=acctype.AccNumber JOIN tblAccMain accmain ON detail.f_ref=accmain.id WHERE 1=1");
						if(binderNo == null || "".equals(binderNo)){
							sql.append(" AND accmain.workFlowNodeName='finish'");
						}
						//当选择外币不是<综合本位币><所有币种>时进行过滤
                        if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                        	if("isBase".equals(currencyValue)){
                        		//本位币
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
                        
						/* 当查询的期间在表tblAccBalance不存在时，使期间往前推，查询最近的记录 */
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
                        		//本位币
                        		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+currencyName+"') ");
                        	}else{
                        		sql.append(" AND tblAccBalance.CurType='"+currencyName+"' ");
                        	}
                        }
						sql.append(" and tblAccBalance.Nyear =(select top 1 a.Nyear from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
						sql.append(" and tblAccBalance.Period =(select top 1 a.Period from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
						sql.append(condition + " ORDER BY tblAccTypeInfo.classCode");
						System.out.println("查询总分类账最接近的数据："+sql.toString());
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
							
							//当选择的币别是——所有币别多栏式
                        	if("all".equals(currencyValue)){
                        		String[] curs = new String[]{"PeriodIni","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni","PeriodDCBala"};
                        		if("".equals(map.get("CurType"))){
                        			//本位币时，外币的金额等于当前本位币的金额
                        			for(String s : curs){
                        				if("CurrYIni".equals(s)){
                        					map.put(s+"_", map.get(s+"Amount"));
                        				}else{
                        					map.put(s+"_", map.get(s+"Base"));
                        				}
                        			}
                        		}else{
                        			//其它外币
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("CurType"), map.get(s));
                        			}
                        		}
                        	}
							
							map.put("Nyear" ,periodMap.get("AccYear"));
							map.put("Period" ,periodMap.get("AccPeriod"));
							//查询此科目的上级并附值到上级中
							String classCode = map.get("classCode").toString();
							for(int j=0;j<(classCode.length()-5)/5;j++){
								//会计科目classCode进行循环获取上级
								HashMap oldMap = (HashMap)accBalanceMap.get(map.get("Nyear")+"_"+map.get("Period")+"_"+classCode.substring(0,5+j*5));
								String[] moneyStr = null;
								if("all".equals(currencyValue)){
    								moneyStr = new String[]{"PeriodIniBase","PeriodIni_"+map.get("CurType"),"CurrYIniDebitSumBase","CurrYIniDebitSum_"+map.get("CurType"),"CurrYIniCreditSumBase","CurrYIniCreditSum_"+map.get("CurType"),"CurrYIniAmount","CurrYIni_"+map.get("CurType")};
    							}else{
    								moneyStr = new String[]{"PeriodIniBase","PeriodIni","CurrYIniDebitSumBase","CurrYIniDebitSum","CurrYIniCreditSumBase","CurrYIniCreditSum","CurrYIniAmount","CurrYIni"};
    							}
								if(oldMap!=null && oldMap.size()>0){
									//当存在上级时，取上级金额数据累加
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
									//当不存在上一级时，创建新的Map保存数据
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
    													//借方
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
						 * 保存所有数据到object中
						 * accTypeInfoList 会计科目列表,accBalanceMap 总分类账HashMap格式数据,
						 * periodList 会计期间列表,OrderNoList 凭证字号列表,noBindMap 期间中未过账的数据,noBindInitMap 期间之前未过账数据
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
         * 对数据进行组装
         */
        Object[] object = (Object[])rst.getRetVal();
        /* 得到数据 */
	    List accTypeInfoList = (ArrayList)object[0];
	    HashMap accBalanceMap = (HashMap)object[1];
	    List periodList = (ArrayList)object[2];
	    List orderNoList = (ArrayList)object[3];
	    HashMap noBindMap = (HashMap)object[4];
	    HashMap noBindInitMap = (HashMap)object[5];
	      
		/* 把凭证字号段放入相对应的HashMap中 */
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
					//存在凭证字号
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
		
		//把期间中未过账的数据放在map中
		if(noBindMap != null && noBindMap.size()>0){
			Iterator nobindMap = noBindMap.entrySet().iterator();
        	//遍历map得到具体的数据
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//得到保存数据的map
				if(bindMap != null && bindMap.size()>0){
					//存在未过账的数据
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
										//本位币
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
							//保存的老金额
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
		
		//把期间之前未过账的数据放入map中
		if(noBindInitMap != null && noBindInitMap.size()>0){
        	/**
        	 * 查询的会计期间之前中存在未过账的数据
        	 */
        	Iterator nobindMap = noBindInitMap.entrySet().iterator();
        	//遍历map得到具体的数据
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//得到保存数据的map
				if(bindMap != null && bindMap.size()>0){
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					String[] str1 = new String[]{"PeriodIniBase","PeriodIni"};
					String[] str2 = new String[]{"PeriodDCBalaBase","PeriodDCBala"};
					
					//所有币别多栏式
					if("all".equals(currencyValue)){
						String[] currS1 = new String[currList.size()+1];
						String[] currS2 = new String[currList.size()+1];
						int counts = 0;
						for(int k = 1;k<str1.length;k++){
							for(int l=0;l<currList.size();l++){
								String[] currStr = (String[])currList.get(l);
								if("true".equals(currStr[2])){
									//本位币
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
		 * 数据整理
		 */
		List allDataList = new ArrayList();
		for(int i=0;i<accTypeInfoList.size();i++){
	      	//每一个会计科目进行遍历
	      	HashMap accTypemap = (HashMap)accTypeInfoList.get(i);
	      	String classCode = accTypemap.get("classCode").toString();
	      	List list = new ArrayList();
	      	
	      	HashMap initMap = new HashMap();
	      	//期初
	      	for(int j=0;j<periodList.size();j++){
	      		
	      		//循环要查询的会计期间
	      		HashMap accPeriodmap = (HashMap)periodList.get(j);
	      		HashMap accType_Period = (HashMap)accBalanceMap.get(accPeriodmap.get("AccYear")+"_"+accPeriodmap.get("AccPeriod")+"_"+accTypemap.get("classCode"));
	      		if(accType_Period == null){
	      			accType_Period = new HashMap();
	      			accType_Period.put("Nyear", accPeriodmap.get("AccYear"));
	      			accType_Period.put("Period", accPeriodmap.get("AccPeriod"));
	      		}
	      		
	      		//所有币别多栏式
	      		String[] str1 = null;
				if("all".equals(currencyValue)){
					str1 = new String[currList.size()+1];
					for(int l = 0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//本位币
							str1[l] = "_";
						}else{
							str1[l] = "_"+currStr[0];
						}
					}
					str1[currList.size()] = "Base";
				}else{
					str1 = new String[]{"","Base"};
				}
	      		
	      		//获取期初
				if(j == 0){
					//第一个科目余额数据
					for(String baseStr : str1){
						String initMoney = "";
						Object o = accType_Period.get("PeriodIni"+baseStr);
						if(o!=null && !"".equals(o)){
							//期初金额
							initMoney = String.valueOf(o);
						}
						if("Base".equals(baseStr)){
							if(initMoney != null && !"".equals(initMoney)){
								Double initMoneys = Double.parseDouble(initMoney);
								if(initMoneys>0){
									initMap.put("isIniflag","借");
								}else if(initMoneys<0){
									initMap.put("isIniflag","贷");
								}else{
									initMap.put("isIniflag","平");
								}
							}else{
								initMap.put("isIniflag","平");
							}
						}
						initMap.put("PeriodIni"+baseStr, initMoney);
					}
				}
				
				/* 无发生额不显示 */
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
				/* 余额为零且无发生额不显示 */
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
		      		//查询上一期间的数据
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
	      			
	      			//本期余额 = 上一期余额额+本期金额
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
	  						//期初金额
	  						o = initMap.get("PeriodIni"+baseStr);
	  					}
	  					if(o!=null && !"".equals(o)){
	  						money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
	  					}
	  					if("PeriodDCBalaBase".equals(str)){
	  						if(Double.valueOf(money)>0){
	  							accType_Period.put("PeriodDCBalaBaseisflag", "借");
	  						}else if(Double.valueOf(money)<0){
	  							accType_Period.put("PeriodDCBalaBaseisflag", "贷");
	  						}else{
	  							accType_Period.put("PeriodDCBalaBaseisflag", "平");
	  						}
	  					}
	  					accType_Period.put(str, money);
	  				}
	      			
	      			//计算本年累计余额（上一期的本年累计+本期的借贷方差额）
	      			String periodmoney = "0";
		      		String debitsum = accType_Period.get("PeriodDebitSum"+baseStr)==null?"":accType_Period.get("PeriodDebitSum"+baseStr).toString();			//本期借方金额
					String creditsum = accType_Period.get("PeriodCreditSum"+baseStr)==null?"":accType_Period.get("PeriodCreditSum"+baseStr).toString();			//本期贷方金额
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
	  							accType_Period.put("CurrYIniAmountisflag", "借");
	  						}else if(Double.valueOf(money)<0){
	  							accType_Period.put("CurrYIniAmountisflag", "贷");
	  						}else{
	  							accType_Period.put("CurrYIniAmountisflag", "平");
	  						}
	  					}
	  					accType_Period.put(str, money);
	  				}
	  				if(oldType_Period == null){
	  					oldType_Period = new HashMap();
	  				}
	  				
	  				//处理本年的借贷方金额
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
	      	//期初值处理
	      	Iterator iterInit = initMap.entrySet().iterator();
	      	while(iterInit.hasNext()){
	      		Entry entry = (Entry)iterInit.next();
	      		if(!"isIniflag".equals(entry.getKey())){
	      			accTypemap.put(entry.getKey(), dealDataDouble(String.valueOf(entry.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
	      		}else{
	      			accTypemap.put(entry.getKey(),entry.getValue());
	      		}
	      	}
	      	
		  	/* 数据处理 */
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
								//本位币
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
				//不显示核算项目明细
	      		String isCalculate = accTypemap.get("isCalculate").toString();
	      		if("1".equals(isCalculate)){
	      			continue;
	      		}
			}
	      	boolean falg = false;
	      	if(levelStart != null && !"".equals(levelStart)){
	      		//科目等级开始
	      		if(classCode.length()/5-1>=Integer.parseInt(levelStart)){
	      			falg = true;
	      		}else{
	      			falg = false;
	      		}
	      	}
	      	if(levelEnd != null && !"".equals(levelEnd)){
	      		//科目等级结束
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
	 * 对字符串类型的进行转换为Double类型
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
	 * 公共导出Excel方法
	 * @param outStream					//输入流
	 * @param tableDisplay				//execl选项卡的名称
	 * @param titleName					//头部的标题（如：总分类账，明细分类账等）
	 * @param strTitle					//列的头部
	 * @param dataList					//保存的数据
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
			//列的表头
			r++;
			for(int i=0;i<strTitle.size();i++){
				HashMap setMap = (HashMap)strTitle.get(i);
				
				//向下多少行显示
				Object o = setMap.get("nextLine");
				if(o != null && !"".equals(o)){
					r += Integer.parseInt(o.toString());
				}
				
				//设置在多少列显示
				o = setMap.get("nextX");
				if(o != null && !"".equals(o)){
					c = Integer.parseInt(o.toString());
				}
				boolean lastXFlag = false;
				int lastX = c;
				//设置单元格跨多少列
				o = setMap.get("lastX");
				if(o != null && !"".equals(o)){
					lastX += Integer.parseInt(o.toString());
					lastXFlag = true;
				}
				int lastY = r;
				//设置单元格跨多少行
				o = setMap.get("lastY");
				if(o != null && !"".equals(o)){
					lastY += Integer.parseInt(o.toString());
				}
				
				Label cell = new Label(c, r, String.valueOf(setMap.get("name")==null?"":setMap.get("name")), wf);
				ws.mergeCells(c, r, lastX, lastY);				//合并单元格坐标
				ws.addCell(cell);
				if(lastXFlag){
					c = lastX;
				}
				maxCount = c;
				c++;
			}
			
			//显示报表的标题
			Label cell = new Label(0, 0, tableDisplay+"   "+titleName, wf);
			ws.mergeCells(0, 0, maxCount, 0);
			ws.addCell(cell);
			
			//数据的内容
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
	 * 查询会计科目
	 * @param showBanAccTypeInfo			过滤禁用的会计科目
	 * @param showIsItem					只显示明细科目
	 * @param showItemDetail				显示核算项目明细
	 * @param gradeShow						分级显示
	 * @param itemSort						项目类别
	 * @param itemLevel						项目级别
	 * @param itemCodeStart					项目代码开始
	 * @param itemCodeEnd					项目代码结束
	 * @param keyWord						关键字搜索
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
								//过滤禁用的会计科目
								strsql += " AND statusId=0 ";
								if(conSql.length()>0){
									//存在记录
									strsql += " AND "+conSql+" 1=1 ";
								}
							}
							//不显示核算项目明细
							
//							strsql += " AND (((DepartmentCode IS NULL OR DepartmentCode='') AND (ClientCode IS NULL OR ClientCode='')";
//							strsql += " AND (EmployeeID IS NULL OR EmployeeID='') AND (SuplierCode IS NULL OR SuplierCode='')";
//							strsql += " AND (StockCode IS NULL OR StockCode=''))";
							strsql += " and ((isnull(isCalculate,0) != 1)";
							
							String isItem = "";
							String isSql = "";
							String isCondition = "";
							if(showItemDetail != null && !"".equals(showItemDetail)){
								if((!"all".equals(itemSort))){
									//单核算项
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
							/* 只显示明细科目 */
							if(showIsItem != null && !"".equals(showIsItem)){
								//过滤存在核算项的科目
								strsql1 += " AND (isCatalog=0 or len(classCode)=5) AND ISNULL(isCalculateParent,0)!=1";
							}
							sql.append(strsql1);
							/* 关键字搜索 */
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
    								//只显示明细科目+单核算+分级显示
                            		if((Integer.valueOf(map.get(isItem).toString())==1 && !itemdetail) || (itemdetail && (map.get("classCode").toString()).length()==5)){
                            			//核算项目
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
	 * 明细分类账报表详情
	 * @param conMap   			搜索条件
	 * @param classCode			classCode
	 * @param classType			点击的类型
	 * @return
	 */
	public Result queryAccDetData(final HashMap<String,String> conMap,final String classCode){
		
		/* 币种进行处理 */
		String currencyNames = conMap.get("currencyName"); 									//币别('isBase'=本位币，''=综合本位币，'all'=所有币别多栏式，'currency'=所有币别，'外币的id'=外币)
		Result rs = queryIsBase(currencyNames);
		currencyNames = rs.retVal.toString();
		
		//所有外币
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyNames;
		final String yearStart = conMap.get("periodYearStart");							//开始期间年
		final String yearEnd = conMap.get("periodYearEnd");								//结束期间年
		final String periodstart = conMap.get("periodStart");							//开始期间
		final String periodend = conMap.get("periodEnd");								//结束期间
		final String binderNo = conMap.get("binderNo");									
		final String dateType = conMap.get("dateType");
		final String dateStart = conMap.get("dateStart");
		final String dateEnd = conMap.get("dateEnd");
		final String takeBrowNo = conMap.get("takeBrowNo");
		final String showMessage = conMap.get("showMessage");
		final String balanceAndTakeBrowNo = conMap.get("balanceAndTakeBrowNo");
		final String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");				//显示禁用科目
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
	            					/* 是本位币 */
	            					conditions += " AND (tblAccDetail.Currency='' OR tblAccDetail.Currency IS NULL OR tblAccDetail.Currency='"+currencyName+"') ";
	            				}else{
	            					conditions += " AND tblAccDetail.Currency='"+currencyName+"' ";
	            				}
	            			}
		                    String sim1 = "";
		                    String sim2 = "";
	                        /**
	                         * 查询期初
	                         */
		                    String startDate = "";
		                    if(Integer.valueOf(periodStart)<10){
		                    	startDate = periodYearStart+"-0"+periodStart+"-01";
							}else{
								startDate = periodYearStart+"-"+periodStart+"-01";
							}
		                    //查期初，汇总所有指定条件前的凭证明细表的借贷差额
	                        StringBuffer sql = new StringBuffer("SELECT '"+startDate+"' AS periodBegin,");
	                        sql.append("0 AS sumDebitAmountBase,0 AS sumLendAmountBase,");
							sql.append("0 AS sumDebitAmount,0 AS sumLendAmount,");
							sql.append("isnull(SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount),0) AS sumBalanceAmountBase,");
							sql.append("isnull(SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount),0) AS sumBalanceAmount ");
							sql.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo");
							sql.append(" ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccTypeInfo.classCode like '"+classCode+"%'");
							
							//查本年累计借贷，汇总指定日期当年的所有借贷
							StringBuffer sqlsum = new StringBuffer("SELECT '"+startDate+"' AS periodBegin,");
							sqlsum.append("isnull(SUM(tblAccDetail.DebitAmount),0) AS sumDebitAmountBase,isnull(SUM(tblAccDetail.LendAmount),0) AS sumLendAmountBase,");
							sqlsum.append("isnull(SUM(tblAccDetail.DebitCurrencyAmount),0) AS sumDebitAmount,isnull(SUM(tblAccDetail.LendCurrencyAmount),0) AS sumLendAmount,");
							sqlsum.append("0 AS sumBalanceAmountBase,");
							sqlsum.append("0 AS sumBalanceAmount ");
							sqlsum.append(" FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo");
							sqlsum.append(" ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccTypeInfo.classCode like '"+classCode+"%'");
							
							//期初金额
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
							String periodSql = "";																//会计期间过滤条件
							String strPeriod = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,accPeriod.AccYear)+'-'+CONVERT(VARCHAR,accPeriod.AccPeriod)+'-01',120),120)";
							if(dateType!=null && "1".equals(dateType)){
								//选择会计期间
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
								//选择日期
								sql.append(" AND tblAccDetail.AccDate<'"+dateStart+"'");
								sqlsum.append(" AND tblAccDetail.AccDate<'"+dateStart+"' AND tblAccMain.CredYear="+dateStart.substring(0,4)+" ");
								sqlCondition += " AND tblAccDetail.AccDate>='"+dateStart+"' AND tblAccDetail.AccDate<='"+dateEnd+"'";
								periodSql += " AND "+strPeriod+">='"+dateStart.substring(0,dateStart.lastIndexOf("-"))+"' AND "+strPeriod+"<='"+dateEnd.substring(0,dateEnd.lastIndexOf("-"))+"'";
							}else if(dateType!=null && "3".equals(dateType)){
								//选择阶段
								sql.append(" AND tblAccDetail.AccDate<'"+sim1+"'");
								sqlsum.append(" AND tblAccDetail.AccDate<'"+sim1+"' AND tblAccMain.CredYear="+sim1.substring(0,4)+" ");
								sqlCondition += " AND tblAccDetail.AccDate>='"+sim1+"' AND tblAccDetail.AccDate<='"+sim2+"'";
								periodSql += " AND "+strPeriod+">='"+sim1.substring(0,sim1.lastIndexOf("-"))+"' AND "+strPeriod+"<='"+sim2.substring(0,sim2.lastIndexOf("-"))+"'";
							}
							
							if(binderNo == null || "".equals(binderNo)){
	            				//不包括未过账凭证
								sql.append(" AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ");
								sqlsum.append(" AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ");
	            			}
							System.out.println("查询期初金额："+sql);
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
	                        							initMap.put("isIniflag", "借");
	                        						}else if(Double.valueOf(strvalue)<0){
	                        							initMap.put("isIniflag", "贷");
	                        						}else{
	                        							initMap.put("isIniflag", "平");
	                        						}
	                        					}else{
	                        						initMap.put("isIniflag", "平");
	                        					}
	                        				}
	                    					initMap.put(rset.getMetaData().getColumnName(i), strvalue);
	                        			}else{
	                        				initMap.put(rset.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
							}
							//由于本年累计，只是累计当年的借贷数据，以上算期初代码累计的是所有年份的借借总额所以这里要重算
							System.out.println("查询本年累计金额："+sqlsum);
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
							
							
							/* 查询开账前的期初金额  加上从凭证明细查出的余额数作为期初 */
							sql = new StringBuffer("SELECT '"+startDate+"' AS periodBegin,'0' AS sumDebitAmountBase,'0' AS sumLendAmountBase,");
							sql.append("(case acctype.JdFlag when 2 then 0-CurrYIniBala else CurrYIniBala end) AS sumBalanceAmount,");
							sql.append("(case acctype.JdFlag when 2 then 0-CurrYIniBalaBase else CurrYIniBalaBase end) AS sumBalanceAmountBase,");
							sql.append("acctype.JdFlag,isnull(tblAccBalance.CurType,'') as Currency ");
							sql.append("FROM tblAccBalance left join tblAccTypeInfo acctype ");
							sql.append("ON tblAccBalance.SubCode=acctype.AccNumber WHERE Nyear=-1 AND Period=-1");
							sql.append(" AND SubCode = (select AccNumber from tblAccTypeInfo where classCode='"+classCode+"')");
							sql.append("ORDER BY SubCode");
							System.out.println("开帐期初sql:"+sql.toString());
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
	                        	//当选择的币别是——所有币别多栏式
	                        	if("all".equals(currencyValue)){
	                        		String[] curs = new String[]{"sumBalanceAmount"};
	                        		if("".equals(map.get("Currency"))){
	                        			//本位币时，外币的金额等于当前本位币的金额
	                        			for(String s : curs){
	                        				map.put(s+"_", map.get(s+"Base"));
	                        			}
	                        		}else{
	                        			//其它外币
	                        			for(String s : curs){
	                        				map.put(s+"_"+map.get("Currency"), map.get(s));
	                        			}
	                        		}
	                        	}
	                        	/* 处理期初数据，金额叠加 */
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
												//借方
												money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
											}
											if("sumBalanceAmountBase".equals(key)){
		                						if(Double.valueOf(money)>0){
		                							initMap.put("isIniflag", "借");
		                						}else if(Double.valueOf(money)<0){
		                							initMap.put("isIniflag", "贷");
		                						}else{
		                							initMap.put("isIniflag", "平");
		                						}
		                    				}
											initMap.put(key, money);
	                        			}
	                        		}
	                        	}
							}
							
							/**
							 * 查询本期合计
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
	            				//不包括未过账凭证
								sql.append(" AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ");
	            			}
							if(!"".equals(currencyValue) && !"all".equals(currencyValue) && !"currency".equals(currencyValue)){
	                        	if("isBase".equals(currencyValue)){
	                        		//本位币
	                        		sql.append(" AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+currencyName+"') ");
	                        	}else{
	                        		sql.append(" AND tblAccDetail.Currency='"+currencyName+"' ");
	                        	}
	                        }
							/* 显示禁用科目 */
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
								//无发生额不显示
								sql.append(" AND sumDebitAmountBase IS NOT NULL AND sumLendAmountBase IS NOT NULL");
							}
							if(balanceAndTakeBrowNo != null && !"".equals(balanceAndTakeBrowNo)){
								//无发生额并且余额为零不显示
								sql.append(" AND sumDebitAmountBase IS NOT NULL AND sumDebitAmountBase IS NOT NULL");
							}
							
							sql.append(" ORDER BY accPeriod.AccYear,accPeriod.AccPeriod");
							System.out.println("本期合计SQL:"+sql.toString());
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
	                        							map.put("isflag", "借");
	                        						}else if(Double.valueOf(strvalue)<0){
	                        							map.put("isflag", "贷");
	                        						}else{
	                        							map.put("isflag", "平");
	                        						}
	                        					}
	                        				}
	                        			}else{
	                        				map.put(rset.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
	                        	
	                        	//当选择的币别是——所有币别多栏式
	                        	if("all".equals(currencyValue)){
	                        		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount"};
	                        		if("".equals(map.get("Currency"))){
	                        			//本位币时，外币的金额等于当前本位币的金额
	                        			for(String s : curs){
	                        				map.put(s+"_", map.get(s+"Base"));
	                        			}
	                        		}else{
	                        			//其它外币
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
	                    	 * 根据期间或者日期查询在此之中的凭证
	                    	 */
	                    	StringBuffer sqlStr = new StringBuffer("SELECT * FROM (SELECT tblAccMain.BillDate,tblAccMain.CredTypeId,(tblAccMain.CredTypeId+' - '+convert(varchar(50),tblAccMain.OrderNo)) AS CredTypeOrderNo ");
	                    	sqlStr.append(",tblAccDetail.RecordComment,tblAccMain.OrderNo,tblAccMain.id as accmainid,tblAccMain.CredYear,tblAccMain.Period,");
	                    	sqlStr.append("tblAccDetail.DebitAmount as sumDebitAmountBase,tblAccDetail.LendAmount as sumLendAmountBase,tblAccDetail.DebitCurrencyAmount as sumDebitAmount,tblAccDetail.LendCurrencyAmount as sumLendAmount, ");
	                    	sqlStr.append("(tblAccDetail.DebitCurrencyAmount-tblAccDetail.LendCurrencyAmount) AS sumBalanceAmount,isnull(tblAccDetail.Currency,'') as Currency,");
	                    	sqlStr.append("(tblAccDetail.DebitAmount-tblAccDetail.LendAmount) AS sumBalanceAmountBase,tblAccDetail.CurrencyRate, ");
	                    	sqlStr.append("CASE WHEN tblAccDetail.Currency='' THEN (SELECT CurrencyName FROM tblCurrency WHERE isBaseCurrency=1) ");
	                    	sqlStr.append("WHEN tblAccDetail.currency!='' THEN (SELECT CurrencyName FROM tblCurrency WHERE id=tblAccDetail.Currency) END AS currencyName, ");
	                    	sqlStr.append("tblAccDetail.f_ref,tblAccDetail.AccCode ");
	                    	/* 显示凭证业务信息 */
	                    	if(showMessage!=null && !"".equals(showMessage)){
	                        	sqlStr.append(",(SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(SELECT tblDbTableInfo.languageId FROM tblDbTableInfo WHERE tblDbTableInfo.tableName=tblAccMain.RefBillType and tblAccMain.RefBillType!='tblAccMain')) as RefBillTypeName ");
	                        	sqlStr.append(",(SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(SELECT modelName FROM tblModules WHERE tblModules.classCode=(SELECT TOP 1 substring(classCode,1,5) FROM tblModules ");
	                        	sqlStr.append(" WHERE (tblModules.linkAddress='/UserFunctionQueryAction.do?tableName='+tblAccMain.RefBillType or tblModules.linkAddress='/UserFunctionQueryAction.do?tableName='+tblAccMain.RefBillType+'&moduleType=1')))) AS RefModuleName ");
	                    	}
	                    	/* 条件：按对方科目多条显示，sql语句附加 */
	                    	if(showAccType==null || "".equals(showAccType)){
	                    		sqlStr.append(",(SELECT TOP 1 acccode FROM tblAccDetail accdetail WHERE accdetail.f_ref=tblAccDetail.f_ref AND accdetail.DebitAmount!=tblAccDetail.DebitAmount) as detailacccode ");
	                    		sqlStr.append(",(SELECT TOP 1 (SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(select AccName from tblAccTypeInfo where AccNumber=accdetail.AccCode)) as accname FROM tblAccDetail accdetail WHERE accdetail.f_ref=tblAccDetail.f_ref AND accdetail.DebitAmount!=tblAccDetail.DebitAmount) as accname ");
	                    	}
	                    	sqlStr.append("FROM tblAccMain LEFT JOIN tblAccDetail ON tblAccMain.id=tblAccDetail.f_ref ");
	                    	sqlStr.append(" LEFT JOIN tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccTypeInfo.classCode like '"+classCode+"%' ");
	                    	
	                    	if(binderNo == null || "".equals(binderNo)){
	            				//不包括未过账凭证
	                    		sqlStr.append(" AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ");
	            			}
	                    	/* 显示禁用科目 */
	                    	if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
	                    		sqlStr.append(" AND tblAccTypeInfo.statusId=0 ");
	                    	}
	                    	
	            			/* 按日期查询 */
	                    	if(dateType!=null && "1".equals(dateType)){
	                    		sqlStr.append(" AND CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+ right('00' + convert(varchar(10),tblAccMain.Period),2)>='"+startTime+"' ");
	                    		sqlStr.append(" AND CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+ right('00' + convert(varchar(10),tblAccMain.Period),2)<='"+endTime+"' ");
	                    	}else if(dateType!=null && "2".equals(dateType)){
								sqlStr.append(" AND tblAccMain.BillDate>='"+dateStart+"' ");
								sqlStr.append(" AND tblAccMain.BillDate<='"+dateEnd+"' ");
	                    	}else if(dateType!=null && "3".equals(dateType)){
	                    		//阶段
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
	                    	
	                    	//外币过滤
	                    	if(!"".equals(currencyValue) && !"all".equals(currencyValue) && !"currency".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//本位币
                            		sql.append(" AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+currencyName+"') ");
                            	}else{
                            		sql.append(" AND tblAccDetail.Currency='"+currencyName+"' ");
                            	}
                            }
	                    	
							sqlStr.append(conditions + ") AS detail1 ");
							/* 条件：按对方科目多条显示，sql语句附加 */
							if(showAccType!=null && !"".equals(showAccType)){
								sqlStr.append("JOIN (SELECT AccCode as detailacccode,(SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(select AccName from tblAccTypeInfo where AccNumber=accdetail.AccCode)) as accname,");
								sqlStr.append("f_ref,isnull(DebitAmount,0) as dAmount,isnull(LendAmount,0) as lAmount FROM tblAccDetail accdetail)");
								sqlStr.append(" AS detail2 ON detail2.f_ref=detail1.f_ref "); 
								sqlStr.append("WHERE detail2.detailacccode!=detail1.AccCode AND detail1.sumDebitAmountBase!=detail2.dAmount ");
							}
							if(orderby != null && !"".equals(orderby)){
								//根据设置的进行排序
								sqlStr.append(" order by "+orderby);
								if(orderby.indexOf("CredTypeId")!=-1){
									sqlStr.append(","+orderby.replace("CredTypeId", "OrderNo"));
								}
							}else{
								//默认根据单据时间排序
								sqlStr.append(" order by BillDate ASC");
							}
							System.out.println("查询期间的凭证sql:"+sqlStr.toString());
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
	                        	
	                        	//所有币别多栏式时
	                        	if("all".equals(currencyValue)){
	                        		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount"};
	                        		if("".equals(accmap.get("Currency"))){
	                        			//本位币时，外币的金额等于当前本位币的金额
	                        			for(String s : curs){
	                        				accmap.put(s+"_", accmap.get(s+"Base"));
	                        			}
	                        		}else{
	                        			//其它外币
	                        			for(String s : curs){
	                        				accmap.put(s+"_"+accmap.get("Currency"), accmap.get(s));
	                        			}
	                        		}
	                        	}
	                        	//相同时
	                        	if(ids.equals(accmap.get("accmainid").toString())){
	                        		//accmap.put("DebitAmount", accmap.get("dAmount"));
	                        		//accmap.put("LendAmount", accmap.get("lAmount"));
	                        	}
	                        	ids = accmap.get("accmainid").toString();
	                        	listAcc.add(accmap);
							}
							
							/**
							 * 保存数据（initMap 期初Map, periodList 本期金额List, listAcc 期间中的凭证数据）
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
        
        //取数据
        Object[] object = (Object[])rst.getRetVal();
        HashMap initMap = (HashMap)object[0];
        List periodList = (ArrayList)object[1];
        List listAcc = (ArrayList)object[2];
        
        /**
         * 数据整理
         */
		List allDataList = new ArrayList();
		//String[] yearStr = new String[]{"sumDebitAmount","sumLendAmount","sumDebitCurrencyAmount","sumLendCurrencyAmount","sumBalanceAmount"};
		
		//对每一个数据进行处理
		String[] strs = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount","sumDebitAmountBase","sumLendAmountBase","sumBalanceAmountBase"};
    	if("all".equals(currencyValue)){
    		String[] newstrs = new String[strs.length+currList.size()*3];
    		int count = 0;
    		for(int n=0;n<strs.length/2;n++){
    			for(int l = 0;l<currList.size();l++){
    				String[] currStr = (String[])currList.get(l);
    				if("true".equals(currStr[2])){
    					//本位币
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
    		//循环要查询的会计期间
    		HashMap accPeriodmap = (HashMap)periodList.get(j);
    		
    		/* 保存本期间所有的凭证信息 */
    		List detailList = new ArrayList();
    		HashMap oldMap = null;
	        for(int k=0;k<listAcc.size();k++){
	        	//循环所有凭证明细
	        	HashMap accmap = (HashMap)listAcc.get(k);
	        	if(accPeriodmap.get("AccYear").equals(accmap.get("CredYear")) && 
	        			accPeriodmap.get("AccPeriod").equals(accmap.get("Period"))){
	        		//当会计期间年，会计期间等于此期间时，证明该凭证明细是本期的凭证明细
	        		String money = "0";
	        		Object o = null;
	        		
	        		String[] str1 = null;
	        		//所有币别多栏式
					if("all".equals(currencyValue)){
						str1 = new String[currList.size()+1];
						for(int l = 0;l<currList.size();l++){
							String[] currStr = (String[])currList.get(l);
							if("true".equals(currStr[2])){
								//本位币
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
		        			//上一条明细的余额
		        			o = oldMap.get("sumBalanceAmount"+s);
		        			if(o!=null && !"".equals(o)){
								money = o.toString();
							}
		        			//此条的明细金额
							o = accmap.get("sumBalanceAmount"+s);
							if(o!=null && !"".equals(o)){
								money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
							}
							//余额（上一条记录的金额+本条记录的金额）
							accmap.put("sumBalanceAmount"+s, money);
		        		}else{
		        			//明细记录第一条时（余额=上一期的本期合计或者期初余额+记录的余额）
		        			HashMap preMap = null;
		        			if(allDataList.size()>0){
		        				preMap = (HashMap)allDataList.get(allDataList.size()-1);			//取上一期间的所有数据
							}
		        			if(preMap!=null && preMap.size()>0){
		        				//存在上一期的记录时，金额取上一期的金额
		        				o = preMap.get("sumBalanceAmount"+s);
		        			}else{
		        				//当不存在上一期时，金额取期初金额
		        				o = initMap.get("sumBalanceAmount"+s);
		        			}
		        			if(o!=null && !"".equals(o)){
		        				money = o.toString();
		        			}
		        			//本记录的余额
		        			o = accmap.get("sumBalanceAmount"+s);
		        			if(o!=null && !"".equals(o)){
		        				money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
		        			}
		        			accmap.put("sumBalanceAmount"+s, money);						//保存金额
		        		}
	        		}
	        		
	        		//把处理完的数据保存在ArrayList中
	        		detailList.add(accmap);
	        		oldMap = accmap;
	        	}
	        }
	        
	        //循环保存的本期间单条数据进行处理
	        for(int s =0;s<detailList.size();s++){
	        	HashMap accMap = (HashMap)detailList.get(s);
	        	
	        	for(String str : strs){
	        		String absValue = "";
	        		
	        		if(str.indexOf("sumBalanceAmount")!=-1){
	        			absValue = "abs";
	        			//方向
	        			if("sumBalanceAmountBase".equals(str)){
	        				if(accMap.get(str)!=null && !"".equals(accMap.get(str))){
	        					if(Double.valueOf(accMap.get(str).toString())>0){
	        						//借方
	        						accMap.put("isflag", "借");
	        					}else if(Double.valueOf(accMap.get(str).toString())<0){
	        						//贷方
	        						accMap.put("isflag", "贷");
	        					}else{
	        						accMap.put("isflag", "平");
	        					}
	        				}
	        			}
	        		}
	        		accMap.put(str, dealDataDouble(String.valueOf(accMap.get(str)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), absValue));
	        	}
	        }
	        
	        //保存本期间中的凭证数据
	        accPeriodmap.put("detail", detailList);
	        
	        /* 处理本期合计和本年累计 */
			for(int k = 0;k<strs.length;k++){
				String moneyyear = "0";
				String moneyperiod = "0";
				//本期金额
				Object o = accPeriodmap.get(strs[k]);
				if(o != null && !"".equals(o)){
					moneyyear = o.toString();
					moneyperiod = o.toString();
				}
				Integer period = Integer.valueOf(accPeriodmap.get("AccPeriod").toString());
				oldMap = new HashMap();
				if(period>-1){
					//如果本期大于1 （本期合计+本年累计）
					if(allDataList.size()>0){
						oldMap = (HashMap)allDataList.get(allDataList.size()-1);			//取上一期间的本年累计
					}
				}
				
				/* 处理本期合计（本期金额+上一期本期余额或者期初余额） */
				/* 处理本年累计金额 */
				if(oldMap.size()>0){
					/* 判断期间是否是上一期的，如果是上一期的就相加 */
					if(oldMap.get("AccYear").equals(accPeriodmap.get("AccYear"))){
						
						//上一期的本年累计
						o = oldMap.get("year_"+strs[k]);
						if(o != null && !"".equals(o)){
							moneyyear = new BigDecimal(moneyyear).add(new BigDecimal(o.toString())).toString();
						}
						//上一期的本期合计
						if(strs[k].indexOf("sumBalanceAmount") !=-1){
							o = oldMap.get(strs[k]);
							if(o != null && !"".equals(o)){
								moneyperiod = new BigDecimal(moneyperiod).add(new BigDecimal(o.toString())).toString();
							}
						}
					}else{ //跨年情况，本年累计加清空
						//上一期的本年累计
						o = oldMap.get("year_"+strs[k]);
						if(strs[k].indexOf("sumDebitAmount") !=-1 || strs[k].indexOf("sumLendAmount") !=-1){
							moneyyear = moneyyear;//跨年情况
						}else{
							if(o != null && !"".equals(o)){
								moneyyear = new BigDecimal(moneyyear).add(new BigDecimal(o.toString())).toString();
							}
						}
						//上一期的本期合计
						if(strs[k].indexOf("sumBalanceAmount") !=-1){
							o = oldMap.get(strs[k]);
							if(o != null && !"".equals(o)){
								moneyperiod = new BigDecimal(moneyperiod).add(new BigDecimal(o.toString())).toString();
							}
						}
					}
				}else{
					/* 不存在上一年的金额时，取期初金额 */ 
					o = initMap.get(strs[k]);
					if(o != null && !"".equals(o)){
						moneyyear = new BigDecimal(moneyyear).add(new BigDecimal(o.toString())).toString();
						if(strs[k].indexOf("sumBalanceAmount") != -1){
							moneyperiod = new BigDecimal(moneyperiod).add(new BigDecimal(o.toString())).toString();
						}
					}
					
				}
				/* 处理借贷方向 */
				if("sumBalanceAmountBase".equals(strs[k])){
					//本期方向
					if(Double.valueOf(moneyperiod)>0){
						//借方
						accPeriodmap.put("isflag", "借");
					}else if(Double.valueOf(moneyperiod)<0){
						//贷方
						accPeriodmap.put("isflag", "贷");
					}else{
						accPeriodmap.put("isflag", "平");
					}
					
					//本年方向
					if(Double.valueOf(moneyyear)>0){
						//借方
						accPeriodmap.put("isYearflag", "借");
					}else if(Double.valueOf(moneyyear)<0){
						//贷方
						accPeriodmap.put("isYearflag", "贷");
					}else{
						accPeriodmap.put("isYearflag", "平");
					}
				}
				accPeriodmap.put(strs[k], dealDataDouble(String.valueOf(moneyperiod), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
				accPeriodmap.put("year_"+strs[k], dealDataDouble(String.valueOf(moneyyear), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
			}
    		allDataList.add(accPeriodmap);
    	}
    	
    	/**
    	 * 期初金额处理
    	 */
    	for(String s : strs){
    		String absFlag = "";
    		if(s.indexOf("sumBalanceAmount")!=-1){
    			absFlag = "abs";
    		}
    		initMap.put(s, dealDataDouble(String.valueOf(initMap.get(s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), absFlag));
    	}
    	
    	/**
    	 * 数据小数点控制和正负控制
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
	 * 点击启用核算项的科目时，查询下级核算的科目
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
								//过滤禁用的会计科目
								sql.append(" AND statusId=0 ");
							}
							if(showItemDetail != null && !"".equals(showItemDetail)){
								if((!"all".equals(itemSort))){
									//单核算项
									sql.append(" AND "+itemSort+"!='' ");
									//项目级别
									if(itemLevel!=null && !"".equals(itemLevel)){
										sql.append(" AND len("+itemSort+")/5="+itemLevel);
									}
									//项目代码开始
									if(itemCodeStart!=null && !"".equals(itemCodeStart)){
										sql.append(" AND "+itemSort+">='"+itemCodeStart+"'");
									}
									//项目代码结束
									if(itemCodeEnd!=null && !"".equals(itemCodeEnd)){
										sql.append(" AND "+itemSort+"<='"+itemCodeEnd+"'");
									}
								}else{
									sql.append(" AND (departmentCode!='' OR clientCode!='' OR employeeId!='' OR supliercode!='' OR stockCode!='') ");
								}
							}
							
							if(gradeShow!=null && !"".equals(gradeShow)){
								//分级显示
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
	 * 查询多栏式明细账列表数据
	 * @parent keyWord      //关键字搜索
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
	 * 删除单个设计记录
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
	 * 根据id查询单条设计记录
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
	 * 根据父级会计科目查询最下级会计科目
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
	 * 根据会计科目classCode查询会计科目信息
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
	 * 添加/修改设计设置
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
								//存在记录时，执行修改操作
								sql.append("UPDATE tblAccDesign SET acctypecode=?,accName=?,columndata=?,currencydata=?,levelsetting=?,levelvalue=? WHERE id=?");
							}else{
								//新增记录
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
	 * 多栏式明细账查询列表
	 * 1.根据用户自定义设置记录查询期初金额
	 * 2.查询本期合计 本年累计
	 * 3.查询某期间的详细单条凭证记录
	 * 4.数据整理
	 * @param conMap       查询条件
	 * @return
	 */
	public Result queryAllDetData(final HashMap<String, String> conMap,final String[] str){
		
		String accTypeValue = "";								//会计编号
		String currencys = "";									//币种
		String isbaseCurr = "";									//记录本位币id
		String currIsBase = "all";
		String isBaseCurrency = "";
		HashMap acctypeMap = new HashMap();
		
		/**
		 * 保存用户设置的信息
		 */
		if(str!=null && str.length>0){
			/* 用户设置的栏目 */
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
			/* 用户选择的外币 */
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
					//只存在一种币别并且是本位币
					currIsBase = "isbase";
				}else{
					currIsBase = "other";
				}
			}
		}
		
		/**
		 * 查询数据库数据
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
						/* 会计科目拼成字符串（如：'100101','100102','100103'） */
						
						String binderNo = conMap.get("binderNo");												//包括未过账凭证
						String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");							//显示禁用科目
						String itemSort = conMap.get("itemSort");												//核算项目
						String acctypeCodeStart = conMap.get("acctypeCodeStart");								//核算项目编号开始
						String acctypeCodeEnd = conMap.get("acctypeCodeEnd");									//核算项目编号结束
						
						//把期间年和期间转换为指定格式
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
						
						//查询产生的金额期初
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
            				//不包括未过账凭证
							sql += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
            			}
						if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
							//不显示禁用科目
							sql += " AND tblAccTypeInfo.statusId=0";
						}
						sql += " GROUP BY tblAccDetail.AccCode ";
						if(!"".equals(currency)){
							sql += ",tblAccDetail.currency ";
						}
						sql += "ORDER BY tblAccDetail.AccCode";
						System.out.println("查询期初金额："+sql);
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
						
						/* 查询开账前的期初金额 */
						sql = "SELECT SubCode AS AccCode,CurrYIniDebitSumBase AS sumDebitAmount,CurrYIniCreditSumBase AS sumLendAmount,";
						sql += "CurrYIniDebitSum as sumDebitCurrencyAmount,CurrYIniCreditSum as sumLendCurrencyAmount,";
						sql += "(case tblAccTypeInfo.JdFlag when 2 then 0-CurrYIniBalaBase else CurrYIniBalaBase end) AS sumBalanceAmount,";
						sql += "(case tblAccTypeInfo.JdFlag when 2 then 0-CurrYIniBala else CurrYIniBala end) AS sumBalanceCurrencyAmount,";
						sql += "isnull(CurType,'') AS Currency FROM tblAccBalance ";
						sql += " left join tblAccTypeInfo on tblAccBalance.SubCode=tblAccTypeInfo.AccNumber ";
						sql += " WHERE Nyear=-1 AND Period=-1 AND SubCode IN ("+accTypeValues+") ORDER BY SubCode";
						System.out.println("期初sql:"+sql);
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
                        	/* 处理期初数据，相同科目的金额叠加 */
                        	String[] strMoney = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount","sumDebitCurrencyAmount","sumLendCurrencyAmount","sumBalanceCurrencyAmount"};
                        	for(int j=0;j<initList.size();j++){
    							HashMap initmap1 = (HashMap)initList.get(j);
    							if(initmap1.get("AccCode").equals(map.get("AccCode"))){
    								boolean isCur = false;
    								if(!"".equals(currency)){
    									//设置了外币
    									if(initmap1.get("Currency").equals(map.get("Currency"))){
    										isCur = true;
    									}
    								}else{
    									isCur = true;
    								}
    								if(isCur){
    									//该科目相等
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
						
						/* 核算项目开始 */
						String conditions = "";
						if(acctypeCodeStart!=null && !"".equals(acctypeCodeStart)){
							if("ClientCode".equals(itemSort) || "SuplierCode".equals(itemSort)){
								//往来单位，客户,供应商
								conditions += " AND tblAccDetail.CompanyCode>=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+acctypeCodeStart+"' order by com.classCode)";
							}else if("StockCode".equals(itemSort)){
								//仓库开始
								conditions += " AND tblAccDetail.StockCode>=(select top 1 stock.classCode from tblStock stock where stock.stockNumber='"+acctypeCodeStart+"' order by stock.classCode)";
							}else if("DepartmentCode".equals(itemSort)){
								//部门
								conditions += " AND tblAccDetail.DepartmentCode>=(select top 1 dept.classCode from tblDepartment dept where dept.deptCode='"+acctypeCodeStart+"' order by dept.classCode)";
							}else if("EmployeeID".equals(itemSort)){
								//职员
								conditions += " AND tblAccDetail.EmployeeID>=(select top 1 emp.id from tblEmployee emp where emp.EmpNumber='"+acctypeCodeStart+"' order by tblEmployee.id)";
							}else if("ProjectCode".equals(itemSort)){
								//项目
								conditions += " AND tblAccDetail.ProjectCode>=(select top 1 p.id from tblProject p where p.ProjectNo='"+acctypeCodeStart+"' order by tblProject.id)";
							}
						}
						
						/* 选择核算项目结束 */
						if(acctypeCodeEnd!=null && !"".equals(acctypeCodeEnd)){
							if("ClientCode".equals(itemSort) || "SuplierCode".equals(itemSort)){
								conditions += " AND tblAccDetail.CompanyCode<=(select top 1 com.classCode from tblCompany com where com.ComNumber='"+acctypeCodeEnd+"' order by com.classCode DESC)";
							}else if("StockCode".equals(itemSort)){
								//仓库开始
								conditions += " AND tblAccDetail.StockCode<=(select top 1 stock.classCode from tblStock stock where stock.stockNumber='"+acctypeCodeEnd+"' order by stock.classCode DESC)";
							}else if("DepartmentCode".equals(itemSort)){
								//部门
								conditions += " AND tblAccDetail.DepartmentCode<=(select top 1 dept.classCode from tblDepartment dept where dept.deptCode='"+acctypeCodeEnd+"' order by dept.classCode DESC)";
							}else if("EmployeeID".equals(itemSort)){
								//职员
								conditions += " AND tblAccDetail.EmployeeID<=(select top 1 emp.id from tblEmployee emp where emp.EmpNumber='"+acctypeCodeEnd+"' order by tblEmployee.id DESC)";
							}else if("ProjectCode".equals(itemSort)){
								//项目
								conditions += " AND tblAccDetail.ProjectCode<=(select top 1 p.id from tblProject p where p.ProjectNo='"+acctypeCodeEnd+"' order by tblProject.id DESC)";
							}
						}
						
						//查询本期合计 本年累计
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
            				//不包括未过账凭证
							sql += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
            			}
						if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
							//不显示禁用科目
							sql += " AND tblAccTypeInfo.statusId=0";
						}
						sql += conditions;
						sql += " GROUP BY tblAccDetail.AccCode ";
						if(!"".equals(currency)){
							sql += ",tblAccDetail.currency ";
						}
						sql += " ,tblAccMain.CredYear,tblAccMain.Period,tblAccPeriod.periodEnd ORDER BY tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccCode";
						System.out.println("查询本期/本年金额："+sql);
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
						
						//查询所有满足条件的明细（单张凭证的数据）
						sql = "SELECT tblAccMain.id,tblAccDetail.AccCode,tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccDate,";
						sql += "tblAccMain.CredTypeID+'-'+convert(varchar,tblAccMain.OrderNo) AS credTypeIdOrderNo,tblAccDetail.RecordComment,";
						sql += "(DebitAmount-LendAmount) AS sumAmount,";
						sql += "DebitAmount,LendAmount,DebitCurrencyAmount,LendCurrencyAmount,ISNULL(tblAccDetail.Currency,'') AS Currency from tblAccDetail LEFT JOIN ";
						sql += "tblAccMain ON tblAccDetail.f_ref=tblAccMain.id LEFT JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode WHERE tblAccDetail.AccCode IN ("+accTypeValues+")"+periodCondition;
						if(binderNo == null || "".equals(binderNo)){
            				//不包括未过账凭证
							sql += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
            			}
						if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
							//不显示禁用科目
							sql += " AND tblAccTypeInfo.statusId=0";
						}
						sql += conditions;
						sql += " ORDER BY tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccDate,tblAccMain.CredTypeId,tblAccMain.orderNo,tblAccDetail.AccCode";
						System.out.println("查询明细金额："+sql);
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
                         * 查询满足条件的会计期间
                         */
                        sql = "SELECT ap.AccYear,ap.AccPeriod,ap.periodEnd FROM tblAccPeriod ap WHERE ";
                        str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,ap.AccYear)+'-'+CONVERT(VARCHAR,ap.AccPeriod)+'-01',120),120)";
                        periodCondition = str+">='"+startTime+"' AND "+str+"<='"+endTime+"'";
						sql += periodCondition+" ORDER BY ap.AccYear,ap.AccPeriod";
						System.out.println("会计期间："+sql);
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
		
		//整理数据
		List initList = (ArrayList)obj[0];						//期初金额
		List curPeriodList = (ArrayList)obj[1];					//本期合计 本年累计
		List accMainList = (ArrayList)obj[2];					//凭证记录数据
		List periodList = (ArrayList)obj[3];					//会计期间
		String startTime = String.valueOf(obj[4]);
		
		String yearStart = conMap.get("periodYearStart");
		String yearEnd = conMap.get("periodYearEnd");
		String periodStart = conMap.get("periodStart");
		
		/**
		 * 处理期初余额数据
		 */
		String accCode = "";
		HashMap values = new HashMap();
		values.put("AccDate", startTime+"-01");
		for(int i=0;i<initList.size();i++){
			String setname = "initData";
			values.put(setname, dealData(values, initList, i, isbaseCurr, currencys, acctypeMap,setname));
		}
		
		/**
		 * 本期合计/本年累计数据处理
		 */
		for(int i=0;i<periodList.size();i++){
			HashMap periodMap = (HashMap)periodList.get(i);
			String yearPeriod = periodMap.get("AccYear").toString();				//会计期间年
			String period = periodMap.get("AccPeriod").toString();					//会计期间
			//循环本期合计数据
			for(int j=0;j<curPeriodList.size();j++){
				HashMap curPeriodMap = (HashMap)curPeriodList.get(j);
				String curYear = curPeriodMap.get("CredYear").toString();
				String curPeriod = curPeriodMap.get("Period").toString();
				if(yearPeriod.equals(curYear) && period.equals(curPeriod)){
					//存在此本期合计
					String setname = "period_"+curYear+"_"+curPeriod;
					HashMap map = dealData(values, curPeriodList, j, isbaseCurr, currencys, acctypeMap, setname);
					HashMap curMap = (HashMap)curPeriodList.get(j);
					Iterator iter = acctypeMap.entrySet().iterator();
					String curr = "";
					if(curMap.get("Currency")!=null){
						curr = curMap.get("Currency").toString();
					}
					
					//当设置的科目没有金额数据时
					while(iter.hasNext()){
						Entry entry = (Entry)iter.next();
						String[] acctypeStr = (String[])entry.getValue();
						String[] sumStr4 = new String[]{"sumDebitAmount","sumDebitCurrencyAmount"};
						String[] sumStr5 = new String[]{"sumLendAmount","sumLendCurrencyAmount"};
						if("1".equals(acctypeStr[0])){
							//借方
							if(map.get("curr_"+curr+"_"+entry.getKey()+"_"+sumStr4[0]) == null ){
								map.put("curr_"+curr+"_"+entry.getKey()+"_"+sumStr4[0],"");
							}
							if(map.get("curr_"+curr+"_"+entry.getKey()+"_"+sumStr4[1]) == null ){
								map.put("curr_"+curr+"_"+entry.getKey()+"_"+sumStr4[1],"");
							}
						}else if("2".equals(acctypeStr[0])){
							//贷方
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
			//本期余额处理（本期余额+上一期余额或者期初余额）
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
			String newMoney = "0";                                //本期借方-贷方的余额
			//本期发生的余额
			HashMap map = (HashMap)values.get("period_"+yearPeriod+"_"+period);
			if(map != null){
				object = map.get("totalBalance");
				if(object != null && !"".equals(object)){
					money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
					newMoney = money;
				}
				if(preInit!=null){
					//存在上一期的金额
					object = preInit.get("totalBalance");				//上一期余额
					if(object != null && !"".equals(object)){
						money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
					}
				}else{
					//不存在上一期金额时，金额取期初金额
					HashMap initMap = (HashMap)values.get("initData");
					if(initMap!=null){
						object = initMap.get("totalBalance");				//期初余额
						if(object != null && !"".equals(object)){
							money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
						}
					}
				}
				if(Double.valueOf(money)>0){
					map.put("totalBalanceisflag", "借");
				}else if(Double.valueOf(money)<0){
					map.put("totalBalanceisflag", "贷");
				}else{
					map.put("totalBalanceisflag", "平");
				}
				map.put("totalBalance", money);
				values.put("period_"+yearPeriod+"_"+period, map);
			}
			
			//本年累计 （上一期间的本年累计+本期的本期合计）
			HashMap yearMap = null;
			if(Integer.valueOf(period)>1){
				String periods = String.valueOf(Integer.valueOf(period)-1);
				yearMap = (HashMap)values.get("CredYear_"+yearPeriod+"_"+periods);				//上一期间的本年累计数据
			}
			if(yearMap!=null){
				//存在上一期的本年累计
				HashMap hashmap = new HashMap();
				Iterator iterator = yearMap.entrySet().iterator();
				//循环上一期的本年累计数据
				while (iterator.hasNext()) {
					Entry entry = (Entry)iterator.next();
					String key = String.valueOf(entry.getKey());
					//如果是金额类型时累加
					if(key.indexOf("sumDebitAmount")>0 || key.indexOf("sumLendAmount")>0 || key.indexOf("sumDebitCurrencyAmount")>0 ||
							key.indexOf("sumLendCurrencyAmount")>0 || key.indexOf("sumBalanceAmount")>0 || "totalBalance".equals(key)){
						money = "0";
						//本年累计的金额
						Object o = entry.getValue();
						if(o!=null && !"".equals(o)){
							money = o.toString();
						}
						//本期金额
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
				//不存在上一期间的本年累计时，本年累计=本期合计+期初金额
				HashMap hashmap = new HashMap();
				HashMap valueMap = (HashMap)values.get("period_"+yearPeriod+"_"+period);
				//期初金额
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
						//把期初金额附给本年累计
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
		 * 凭证明细
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
					/* 判断期间年和期间相同时，数据加到list中 */
					if(accmain.get("Currency").equals(isbaseCurr)){
						//是本位币时
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
							//该会计科目是借方
							money = new BigDecimal(money1).subtract(new BigDecimal(money2)).toString();
							//keys = str1[k]+"s";
							keys = "curr_"+curr+"_"+accmain.get("AccCode")+"_"+str1[k];
						}else if("2".equals(accStrs[0])){
							//会计科目是贷方
							money = new BigDecimal(money2).subtract(new BigDecimal(money1)).toString();
							//keys = str2[k]+"s";
							keys = "curr_"+curr+"_"+accmain.get("AccCode")+"_"+str2[k];
						}
						accmain.put(keys,money);
					}
					//accmain.put("sumAmount", accmain.get("sumAmount").toString());
					//业务记录分行显示
					if(id.equals(accmain.get("id")) && 
							(showOperationBranch==null || "".equals(showOperationBranch))){
						HashMap oldMap = (HashMap)list.get(list.size()-1);
						HashMap hashmap = new HashMap();
						Iterator iterator = oldMap.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry entry = (Entry)iterator.next();
							String key = String.valueOf(entry.getKey());
							//如果是金额类型时累加
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
						
						/* 循环数据 */
						iterator = accmain.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry entry = (Entry)iterator.next();
							String key = String.valueOf(entry.getKey());
							//如果是金额类型时累加
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
								hashmap.put("isflag","借");
							}else if(Double.valueOf(hashmap.get("sumAmount").toString())<0){
								hashmap.put("isflag","贷");
							}else{
								hashmap.put("isflag","平");
							}
							hashmap.put("sumAmount", Double.valueOf(hashmap.get("sumAmount").toString()));
						}
						//accmain = hashmap;
						list.remove(list.size()-1);
						list.add(hashmap);
						continue;
					}
					
					String oldmoney = accmain.get("sumAmount").toString();
					//每一条的明细余额都要加上上一条记录的余额
					if(list.size()>0){
						//存在记录时，取上一条数据
						HashMap oldMap = (HashMap)list.get(list.size()-1);
						//获取余额
						Object oldSumAmount = oldMap.get("sumAmount");
						if(oldSumAmount!= null && !"".equals(oldSumAmount)){
							oldmoney = new BigDecimal(oldmoney).add(new BigDecimal(oldSumAmount.toString())).toString();
						}
						accmain.put("sumAmount", oldmoney);
					}else{
						//如果是第一条记录时（余额=该记录的余额+上一期的本年累计金额）
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
							//存在记录
							Object oldSumAmount = preInit.get("totalBalance");
							if(oldSumAmount!= null && !"".equals(oldSumAmount)){
								oldmoney = new BigDecimal(oldmoney).add(new BigDecimal(oldSumAmount.toString())).toString();
							}
							accmain.put("sumAmount", oldmoney);
						}else{
							//不存在上一期的本年累计金额时，取期初金额
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
						accmain.put("isflag","借");
					}else if(Double.valueOf(accmain.get("sumAmount").toString())<0){
						accmain.put("isflag","贷");
					}else{
						accmain.put("isflag","平");
					}

					id = String.valueOf(accmain.get("id"));
					code = String.valueOf(accmain.get("AccCode"));
					list.add(accmain);
				}
			}
			values.put("detail_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"), list);
		}
		
		/* 期初金额处理 */
		HashMap initMap = (HashMap)values.get("initData");
		if(initMap!= null && initMap.size()>0){
			String[] strs = new String[]{"sumDebitCurrencyAmount","sumLendCurrencyAmount","totalsumLendAmount","sumLendAmount","sumDebitAmount","totalsumDebitAmount","totalBalance"};
			Iterator iter = initMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry entry = (Entry)iter.next();
				String keyStr = String.valueOf(entry.getKey());
				for(String s : strs){
					if(keyStr.indexOf(s)>0 || "totalBalance".equals(keyStr)){
						//存在
						if("totalBalance".equals(s)){
							initMap.put(keyStr,dealDataDouble(String.valueOf(entry.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
						}else{
							initMap.put(keyStr,dealDataDouble(String.valueOf(entry.getValue()), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}
					}
				}
			}
		}
		/* 本期合计、本年累计数据过滤 */
		for(int i=0;i<periodList.size();i++){
			HashMap periodMap = (HashMap)periodList.get(i);
			//本年合计金额处理
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
								//存在
								if("totalBalance".equals(s)){
									if(!"".equals(entry.getValue())){
										if(Double.valueOf(entry.getValue().toString())>0){
											hashmap.put("totalBalanceisflag","借");
										}else if(Double.valueOf(entry.getValue().toString())<0){
											hashmap.put("totalBalanceisflag","贷");
										}else{
											hashmap.put("totalBalanceisflag","平");
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
		
		/* 详细信息数据处理 */
		for(int i=0;i<periodList.size();i++){
			HashMap periodMap = (HashMap)periodList.get(i);
			List list = (ArrayList)values.get("detail_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
			String[] strs = new String[]{"DebitAmount","DebitAmounts","LendAmount","LendAmounts","sumAmount","DebitCurrencyAmount",
					"DebitCurrencyAmounts","LendCurrencyAmount","LendCurrencyAmounts"};
			for(int j=0;j<list.size();j++){
				HashMap detailMap = (HashMap)list.get(j);
				for(String valueStr : strs){
					if(detailMap.get(valueStr)!=null){
						//存在
						if("sumAmount".equals(valueStr)){
							detailMap.put(valueStr,dealDataDouble(String.valueOf(detailMap.get(valueStr)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
						}else{
							detailMap.put(valueStr,dealDataDouble(String.valueOf(detailMap.get(valueStr)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}
					}
				}
			}
		}
		values.put("periodList", periodList);			//期间List
		values.put("isbaseCurr", isbaseCurr);
		values.put("currIsBase", currIsBase);
		result.setRetVal(values);
		result.setRetCode(retCode);
		return result;
	}
	
	
	/**
	 * 封装数据（期初金额，本期合计，本年累计）
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
		//所有金额相加sumDebitAmount sumLendAmount sumDebitCurrencyAmount sumLendCurrencyAmount
		HashMap valueMap = (HashMap)values.get(setname);				//期初数据
		if(valueMap==null){
			valueMap = new HashMap();
		}
		String[] sumStr1 = new String[]{"totalsumDebitAmount","totalsumLendAmount","totalBalance"};
		String[] sumStr2 = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount"};
		String[] sumStr3 = new String[]{"sumDebitAmount","sumLendAmount","sumDebitCurrencyAmount","sumLendCurrencyAmount"};
		
		//统计借贷合计本位币金额
		Object o = null;
		String money = "0";
		for(int j=0;j<sumStr1.length;j++){
			money = "0";
			
			//获取保存在valueMap中旧的数据
			o = valueMap.get(sumStr1[j].toString());
			if(o != null && !"".equals(o)){
				money = o.toString();
			}
			
			//获取当前的金额
			o = map.get(sumStr2[j]);
			if(o != null && !"".equals(o)){
				money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
			}
			if(j==2){
				//方向进行处理
				if(Double.valueOf(money)>0){
					valueMap.put("totalBalanceisflag","借");
				}else if(Double.valueOf(money)<0){
					valueMap.put("totalBalanceisflag","贷");
				}else{
					valueMap.put("totalBalanceisflag","平");
				}
			}
			if(i==dataList.size()-1){
				money = dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), "");
			}
			valueMap.put(sumStr1[j],money);
		}
		String curr = "";
		if(map.get("Currency")!=null){
			//存在外币
			curr = map.get("Currency").toString();
		}
		String acctype = map.get("AccCode").toString();
		for(int j=0;j<sumStr3.length;j++){
			money = "0";
			if(curr.equals(isbaseCurr)){
				//本位币时
				curr = "";
			}
			if(!"".equals(currencys)){
				//统计借贷根据币种分别的金额 （币别不为空时进行统计）
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
		
		//根据会计科目不同统计不同金额
		if("1".equals(accStrs[0])){
			//该会计科目是借方
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
			//会计科目是贷方
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
	 * 查询核算项目数据
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
							/* 只显示明细科目 */
							String itemSort = conMap.get("itemSort");						//项目类别
							String itemCodeStart = conMap.get("itemCodeStart");				//项目代码开始
							String itemCodeEnd = conMap.get("itemCodeEnd");					//项目代码结束		
							String keyWord = conMap.get("keyWord");							//关键字搜索
							String accTypeNo = conMap.get("accTypeNo");						//项目无发生额不显示
							String yearStart = conMap.get("periodYearStart");
							String yearEnd = conMap.get("periodYearEnd");
							String periodStart = conMap.get("periodStart");
							String periodEnd = conMap.get("periodEnd");
							String accCode = conMap.get("accCode");							//核算明细账查询时必须指定会计科目
							String binderNo = conMap.get("binderNo");						//包括未过账凭证
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
							
							/* 会计科目存在 */							
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
							System.out.println("核算项目列表SQL："+sql.toString());
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
	 * 核算项目分类总账详情
	 * 1.查询满足条件的会计科目
	 * 2.查询借贷方金额
	 * 4.查询满足条件的会计期间
	 * 4.组合数据
	 * @param conMap
	 * @param classCode
	 * @return
	 */
	public Result queryAccCalculateDetail(final HashMap<String,String> conMap,final String classCode,
			final MOperation mop,final LoginBean loginBean){
		final Result result = new Result();
		
		//对币别进行处理
		String currencyNames = conMap.get("currencyName");; 									//币别('isBase'=本位币，''=综合本位币，'all'=所有币别，'外币的id'=外币)
		Result rs = queryIsBase(currencyNames);
		currencyNames = rs.retVal.toString();
		
		//所有外币
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyNames;
		final String searchCurrency = conMap.get("currencyName");
		final String yearStart = conMap.get("periodYearStart");
		final String yearEnd = conMap.get("periodYearEnd");
		final String periodStart = conMap.get("periodStart");
		final String periodEnd = conMap.get("periodEnd");
		final String accCodeLevel = conMap.get("accCodeLevel");       				//科目级次
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							String binderNo = conMap.get("binderNo");								//包括未过账凭证
							String accCodeStart = conMap.get("accCodeStart");						//会计科目开始
							String accCodeEnd = conMap.get("accCodeEnd");							//会计科目结束
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");			//显示禁用科目
							String itemSort = conMap.get("itemSort"); 								//项目类别
							
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
							 * 查询条件
							 */
							String condition = scopeSql(mop, loginBean);
							if(accCodeStart != null && !"".equals(accCodeStart)){
								//科目开始
								condition += " AND (tblAccTypeInfo.AccNumber>='"+accCodeStart+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+accCodeStart+"')+'%')";
							}
							if(accCodeEnd != null && !"".equals(accCodeEnd)){
								//科目结束
								condition += " AND (tblAccTypeInfo.AccNumber<='"+accCodeEnd+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+accCodeEnd+"')+'%')";
							}
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//不显示禁用科目
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
							String sql1 = "",sql2 = "";
							if(binderNo == null || "".equals(binderNo)){
	            				//不包括未过账凭证
								sql1 += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
	            			}
							if("DepartmentCode".equals(itemSort)){
								//点击部门
								sql1 += " AND tblAccTypeInfo.DepartmentCode like '"+classCode+"%'";
								
								sql2 += " AND tblAccTypeInfo.DepartmentCode like '"+classCode+"%'";
							}else if("EmployeeID".equals(itemSort)){
								//点击职员
								sql1 += " AND tblAccTypeInfo.EmployeeID='"+classCode+"'";
								
								sql2 += " AND tblAccTypeInfo.EmployeeID='"+classCode+"'";
							}else if("StockCode".equals(itemSort)){
								//点击仓库
								sql1 += " AND tblAccTypeInfo.StockCode like '"+classCode+"%'";
							
								sql2 += " AND tblAccTypeInfo.StockCode like '"+classCode+"%'";
							}else if("ClientCode".equals(itemSort)){
								//点击客户
								sql1 += " AND tblAccTypeInfo.ClientCode like '"+classCode+"%' AND (tblCompany.ClientFlag=2 or tblCompany.ClientFlag=3) AND tblCompany.statusId!='-1'";
								
								sql2 += " AND tblAccTypeInfo.ClientCode like '"+classCode+"%' ";
							}else if("SuplierCode".equals(itemSort)){
								//点击供应商
								sql1 += " AND tblAccTypeInfo.SuplierCode like '"+classCode+"%' AND (tblCompany.ClientFlag=1 or tblCompany.ClientFlag=3) AND tblCompany.statusId!='-1'";
								
								sql2 += " AND tblAccTypeInfo.SuplierCode like '"+classCode+"%' ";
							}else if("ProjectCode".equals(itemSort)){
								//点击项目
								sql1 += " AND tblAccTypeInfo.ProjectCode like '"+classCode+"%'";
								
								sql2 += " AND tblAccTypeInfo.ProjectCode like '"+classCode+"%'";
							}
                            
							if(!"".equals(currencyValue)){
								if("isBase".equals(currencyValue)){
									//本位币
									sql1 += " AND (tblAccDetail.Currency='' OR tblAccDetail.Currency IS NULL OR tblAccDetail.Currency='"+searchCurrency+"') ";
								}else{
									//其它币种
									sql1 += " AND tblAccDetail.Currency='"+searchCurrency+"' ";
								}
								
							}
							HashMap accDateMap = new HashMap();
							/**
							 * 查询满足条件的会计科目
							 */
							StringBuffer sql = new StringBuffer("SELECT acctype.classCode,acctype.AccNumber,ISNULL(acctype.isCalculate,'') AS isCalculate,l.zh_cn AS AccFullName FROM tblAccTypeInfo acctype join (SELECT tblAccTypeInfo.classCode");
							sql.append(" FROM tblAccDetail JOIN tblAccTypeInfo on tblAccDetail.AccCode=tblAccTypeInfo.AccNumber ");
							sql.append(" JOIN tblAccMain ON tblAccMain.id=tblAccDetail.f_ref LEFT JOIN tblCompany ON tblCompany.classCode=tblAccDetail.CompanyCode WHERE 1=1");
							sql.append(condition);
							sql.append(sql1);
							sql.append(" AND tblAccTypeInfo.isCalculate=1 GROUP BY tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode");
							sql.append(" ) AS a ON a.classCode like acctype.classCode+'%' JOIN tblLanguage l ON l.id=acctype.AccFullName GROUP BY acctype.AccNumber,acctype.classCode,l.zh_cn,acctype.isCalculate");
							sql.append(" ORDER BY acctype.classCode");
							System.out.println("会计科目SQL："+sql.toString());
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
							 * 查询期初金额
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
							
                            //**查询期初（包含开账期初）
                            //*****开账期初*******//						
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
                            
							System.out.println("查询期初金额："+execSql);
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
                        								map.put("isInitflag", "借");
                        							}else if(discrepancyAmount<0){
                        								map.put("isInitflag", "贷");
                        							}else{
                        								map.put("isInitflag", "平");
                        							}
                        						}else{
                        							map.put("isInitflag", "平");
                        						}
                        					}
                        					map.put(rset.getMetaData().getColumnName(i), strvalue);
                        					
                        					String classCode = map.get("classCode").toString();
                        					/* 给上级附金额等一些数据 */
                        					if(classCode.length()>5){
                        						for(int j=0;j<(classCode.length()-5)/5;j++){
                        							HashMap oldMap = (HashMap)initMap.get(classCode.substring(0,5+j*5)+"_"+yearStart+"_"+periodStart);
                        							if(oldMap!=null && oldMap.size()>0){
                        								//存在上一级科目金额时
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
							 * 本期借贷方金额
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
                            System.out.println("本期借贷sql:"+sql);
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
                        					/* 给上级附金额等一些数据 */
                        					Integer Nyear = Integer.valueOf(map.get("CredYear").toString());
                        					Integer Period = Integer.valueOf(map.get("Period").toString());
                        					if(classCode.length()>5){
                        						for(int j=0;j<(classCode.length()-5)/5;j++){
                        							Object isCalculate = accDateMap.get(classCode.substring(0,5+j*5)+"_isCalculate");
                        							HashMap oldMap = (HashMap)accBalanceMap.get(classCode.substring(0,5+j*5)+"_"+map.get("CredYear")+"_"+map.get("Period"));
                        							if(oldMap!=null && oldMap.size()>0){
                        								//存在上一级科目金额时
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
                             * 查询满足条件的会计期间
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
							
							//保存数据 accTypeInfoList 会计科目，initMap 期初余额,accBalanceMap 本期借贷方，periodList 会计期间
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
		
		/* 取数据 */
		Object[] obj = (Object[])result.getRetVal();
		List accTypeInfoList = (ArrayList)obj[0];																//会计科目
		HashMap initMap = (HashMap)obj[1];																		//期初金额
		LinkedHashMap accBalanceMap = (LinkedHashMap)obj[2];													//本期金额
		List periodList = (ArrayList)obj[3];																	//会计期间
		
		
		/**
		 * 数据整理
		 */
		HashMap valuesMap = new HashMap();																		//保存所有数据
		//循环会计科目
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			
			String accClassCode = String.valueOf(map.get("classCode"));											//会计科目classCode
			//循环会计期间
			for(int j = 0;j<periodList.size();j++){
				HashMap periodMap = (HashMap)periodList.get(j);
				Integer credYear = Integer.valueOf(periodMap.get("AccYear").toString());						//会计期间年
				Integer period = Integer.valueOf(periodMap.get("AccPeriod").toString());						//会计期间
				
				//得到本期间的借贷方金额
				HashMap accMap = (HashMap)accBalanceMap.get(accClassCode+"_"+credYear+"_"+period);
				if(accMap == null ){
					//如果本期间本存在本期金额记录时，创建新的HashMap;
					accMap = new HashMap();
					accMap.put("CredYear", credYear);
					accMap.put("Period", period);
					accMap.put("classCode", accClassCode);
					accMap.put("AccCode", map.get("AccNumber"));
				}
				
				//得到期初余额
				HashMap init = (HashMap)initMap.get(accClassCode+"_"+credYear+"_"+period);
				Integer yearStarts = Integer.valueOf(conMap.get("periodYearStart").toString());					//查询的期间最开始的年
				if(init != null){
					/* 当查询的会计期间有期初时，此期间的期初金额等于查询的期初金额 */
					accMap.put("initCurrencyAmount", init.get("sumBalanceCurrencyAmount"));						//期初原币金额
					accMap.put("initAmount", init.get("sumBalanceAmount"));										//期初余额
					accMap.put("sumInitDebitAmount", init.get("sumInitDebitAmount"));							//期初借方金额
					accMap.put("sumInitLendAmount", init.get("sumInitLendAmount"));								//期初贷方金额
				}else{
					//不存在期初金额时，期初金额=上一期的期末余额
					HashMap preInit = null;																		//上一期的金额数据
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
						//存在上一期的数据
						accMap.put("initAmount", preInit.get("PeriodBalaBase"));
						accMap.put("initCurrencyAmount", preInit.get("PeriodBala"));
					}
				}
				
				//得到期末余额（期初+借方金额-贷方金额）
				String[] initAmountStr = new String[]{"initAmount","initCurrencyAmount"};
				String[] initAmountStrCurr = new String[]{"sumDCBala","sumDCBalaCurrency"};
				String[] periodStr = new String[]{"PeriodBalaBase","PeriodBala"};
				for(int k =0;k<initAmountStr.length;k++){
					Object initAmount = accMap.get(initAmountStr[k]);
					String periodEnds = "0";
					if(accMap.get(initAmountStrCurr[k]) != null && !"".equals(accMap.get(initAmountStrCurr[k]))){
						//本期余额
						periodEnds = accMap.get(initAmountStrCurr[k]).toString();
					}
					if(initAmount!=null && !"".equals(initAmount)){
						//期初金额+本期余额=期末余额
						periodEnds = new BigDecimal(initAmount.toString()).add(new BigDecimal(periodEnds)).toString();
					}
					accMap.put(periodStr[k], periodEnds);														//本期期末余额
				}
				
				//得到本年借贷方金额（本期金额+上一期本年金额）
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
						oldMap = (HashMap)valuesMap.get(accClassCode+"_"+credYear+"_"+k);				//上一期间的本年累计数据
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
				accMap.put("sumYearDebitAmount", debit);																//本年借方金额
				accMap.put("sumYearLendAmount", lend);																	//本年贷方金额
				accMap.put("sumYearDebitCurrencyAmount", debitCurrency);												//本年原币借方金额
				accMap.put("sumYearLendCurrencyAmount", lendCurrency);													//本年原币贷方金额
				
				/* 以指定格式保存完整的数据 */
				valuesMap.put(accClassCode+"_"+credYear+"_"+period, accMap);
			}
		}
		
		/* 处理数据显示规范 */
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
					//期末余额方向
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
								valueMap.put(init, "借");
							}else if(Double.valueOf(moneys)<0){
								valueMap.put(init, "贷");
							}else{
								valueMap.put(init, "平");
							}
						}else{
							valueMap.put(init, "平");
						}
					}
					valueMap.put(str,dealDataDouble(String.valueOf(valueMap.get(str)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), absValue));
				}
			}
		}
		
		List accList = new ArrayList();
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			//科目级次进行过滤
			if(accCodeLevel != null && !"".equals(accCodeLevel)){
				//科目级次
				String code = String.valueOf(map.get("classCode"));
				if(!(code.length()/5-1<=Integer.parseInt(accCodeLevel))){
					continue;
				}
			}
			//是否是核算项
			String isCalculate = map.get("isCalculate").toString();
			if(!"1".equals(isCalculate)){
				//是核算科目
				map.put("period", periodList);
				accList.add(map);
			}
		}
		obj = new Object[]{accList,valuesMap,currencyValue};
		result.setRetVal(obj);
		return result;
	}
	
	/**
	 * 查询核算项目明细账详情数据
	 * 1.查询本会计科目的期初余额
	 * 2.查询本期合计
	 * 3.查询详细的凭证记录
	 * 4.查询满足条件的期间
	 * 5.组合数据（数据的组合，统计本年累计）
	 * @param conMap
	 * @param classCode
	 * @return
	 */
	public Result queryAccCalculateDetDetail(final HashMap<String,String> conMap,final String classCode,
			final MOperation mop,final LoginBean loginBean){
		final Result result = new Result();
		
		//对币别进行处理
		final String currencyName = conMap.get("currencyName"); 									//币别
		String isBaseCurrency = "";																	//处理后的币别(all 所有币别多栏式， "".equals(currencyName) 综合本位币，false 外币，true 本位币)
		if(currencyName!=null && !"".equals(currencyName)){
			//不是综合本位币
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
							
							String binderNo = conMap.get("binderNo");								//包括未过账凭证
							String accCodeLevel = conMap.get("accCodeLevel");       				//科目级次
							String accCodeStart = conMap.get("accCodeStart");						//会计科目开始
							String accCodeEnd = conMap.get("accCodeEnd");							//会计科目结束
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");			//显示禁用科目
							String yearStart = conMap.get("periodYearStart");
							String yearEnd = conMap.get("periodYearEnd");
							String periodStart = conMap.get("periodStart");
							String periodEnd = conMap.get("periodEnd");
							String itemSort = conMap.get("itemSort"); 								//项目类别
							String accCode = conMap.get("accCode");									//会计科目
							String dateType = conMap.get("dateType");
							String dateStart = conMap.get("dateStart");								//日期开始
							String dateEnd = conMap.get("dateEnd");									//日期结束
							
							String startTime = "";
							String endTime = "";
							if(dateType != null && "1".equals(dateType)){
								//按期间进行查询
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
								//按时间进行查询
								startTime = dateStart.substring(0,dateStart.lastIndexOf("-"));
								endTime = dateEnd.substring(0,dateEnd.lastIndexOf("-"));
								yearStart = dateStart.substring(0,dateStart.indexOf("-"));
								periodStart = String.valueOf(Integer.parseInt(dateStart.substring(dateStart.indexOf("-")+1,dateStart.lastIndexOf("-"))));
							}
							
							/* 过滤条件 */
							String sql1 = scopeSql(mop, loginBean);
							if(binderNo == null || "".equals(binderNo)){
	            				//不包括未过账凭证
								sql1 += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
	            			}
							if("DepartmentCode".equals(itemSort)){
								//点击部门
								sql1 += " AND tblAccTypeInfo.DepartmentCode like '"+classCode+"%'";
							}else if("EmployeeID".equals(itemSort)){
								//点击职员
								sql1 += " AND tblAccTypeInfo.EmployeeID like '"+classCode+"%'";
							}else if("StockCode".equals(itemSort)){
								//点击仓库
								sql1 += " AND tblAccTypeInfo.StockCode like '"+classCode+"%'";
							}else if("ClientCode".equals(itemSort)){
								//点击客户
								sql1 += " AND tblAccTypeInfo.ClientCode like '"+classCode+"%' AND (tblCompany.ClientFlag=2 or tblCompany.ClientFlag=3) AND tblCompany.statusId!='-1'";
							}else if("SuplierCode".equals(itemSort)){
								//点击供应商
								sql1 += " AND tblAccTypeInfo.SuplierCode like '"+classCode+"%' AND (tblCompany.ClientFlag=1 or tblCompany.ClientFlag=3) and tblCompany.statusId!='-1'";
							}else if("ProjectCode".equals(itemSort)){
								//点击项目
								sql1 += " AND tblAccTypeInfo.ProjectCode like '"+classCode+"%'";
							}
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//不显示禁用科目
								sql1 += " AND tblAccTypeInfo.statusId=0";
							}
							String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
                            
							if("true".equals(isCurrency)){
								//本位币
								sql1 += " AND (tblAccDetail.Currency='' OR tblAccDetail.Currency IS NULL OR tblAccDetail.Currency='"+currencyName+"') ";
							}else if("false".equals(isCurrency)){
								//其它币种
								sql1 += " AND tblAccDetail.Currency='"+currencyName+"' ";
							}
							
							
							/**
							 * 查询本会计科目的期初余额
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
							
							//*****开账期初*******//
							StringBuffer iniSql = new StringBuffer("select "+yearStart+" as accYear,"+periodStart+" as accPeriod,0 as sumBalanceCurrencyAmount,0 as sumBalanceAmount,");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 1 then tblAccBalance.CurrYIniBalaBase else -tblAccBalance.CurrYIniBalaBase end) AS sumAmount, ");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 1 then tblAccBalance.CurrYIniBalaBase else 0 end)as sumDebit,");
							iniSql.append(" (case when tblAccTypeInfo.JdFlag = 2 then tblAccBalance.CurrYIniBalaBase else 0 end)as sumLend,");
							iniSql.append(" 0 AS sumCurrencyAmount ");
							iniSql.append(" from tblAccTypeInfo join tblAccBalance on tblAccTypeInfo.AccNumber=tblAccBalance.SubCode ");
							iniSql.append(" where 1 = 1 and tblAccTypeInfo.statusId=0 AND tblAccBalance.Nmonth =-1 and tblAccBalance.Nyear=-1 ");
							iniSql.append(" and tblAccTypeInfo.classCode like (select a.classCode from tblAccTypeInfo a where a.AccNumber='"+accCode+"')+'%' ");																																																										
							//*******end*******//
												
							//*****链接sql****//
							String execsql = "select accYear,accPeriod,sum(sumBalanceCurrencyAmount) as sumBalanceCurrencyAmount,sum(sumBalanceAmount) as sumBalanceAmount,sum(sumAmount) as sumAmount,sum(sumDebit) as sumDebit,sum(sumLend) as sumLend,sum(sumCurrencyAmount) as sumCurrencyAmount from("+sql+" union "+iniSql+")t group by accYear,accPeriod";
							//******end*****//
							
							System.out.println("查询期初金额："+execsql);
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
                        								initMap.put("isInitflag", "借");
                        							}else if(discrepancyAmount<0){
                        								initMap.put("isInitflag", "贷");
                        							}else{
                        								initMap.put("isInitflag", "平");
                        							}
                        						}else{
                        							initMap.put("isInitflag", "平");
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
							 * 查询本期借贷方合计
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
                            	//所有币别多栏式
                            	sql.append(",tblAccDetail.currency");
                            }
                            sql.append(" ORDER BY tblAccMain.CredYear,tblAccMain.period");
                            System.out.println("本期借贷sql:"+sql);
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
                            		/* 对外币进行分组时，借方金额或者贷方金额要进行叠加 */
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
                             * 查询开始期间之前的本年累计
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
                             * 查询凭证明细数据
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
    						System.out.println("查询明细金额："+sql.toString());
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
                             * 查询满足条件的会计期间
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
    						System.out.println("会计期间："+sql);
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
    						 * 保存数据 （initMap 期初金额统计，curPeriodMap 各期间的金额统计包括外币金额， accMainList 会计凭证详情，periodList 期间，yearPreMap 查询之前的本年金额）
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
		
		/* 取数据 */
		Object[] obj = (Object[])result.retVal;
		HashMap initMap = (HashMap)obj[0];																		//期初数据HashMap
		HashMap curPeriodMap = (HashMap)obj[1];																	//本期借贷方数据
		List accMainList = (ArrayList)obj[2];																	//明细的数据
		List periodList = (ArrayList)obj[3];																	//所有的查询期间
		HashMap yearPreMap = (HashMap)obj[4];																	//本年之前的金额合计
		
		/**
		 * 组合数据
		 */
		HashMap values = new HashMap();
		
		//保存期初金额
		if(initMap.get("isInitflag") == null){
			initMap.put("isInitflag","平");
		}
		values.put("initMap", initMap);
		
		List list = new ArrayList();
		
		/**
		 * 循环要查询的会计期间
		 */
		for(int i=0;i<periodList.size();i++){
			HashMap map = (HashMap)periodList.get(i);														//会计期间Map
			Integer accYear = Integer.valueOf(map.get("AccYear").toString());								//会计期间年
			Integer accPeriod = Integer.valueOf(map.get("AccPeriod").toString());							//会计期间
			Integer yearStarts = Integer.valueOf(conMap.get("periodYearStart").toString());					//查询的开始期间
			
			HashMap curMap = (HashMap)curPeriodMap.get(accYear+"_"+accPeriod);								//获取本期的借贷金额
			if(curMap == null){
				//本期未存在数据时，
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
			//得到本期合计的余额  (上一期的余额或者期初余额+本期的借贷方差额)
			if(i == 0){
				//当第一个会计期间时，本期合计的余额=期初余额+本期借方金额-本期贷方金额
				o = initMap.get("sumAmount");
				upsumDCBala = initMap.get("sumAmount");
				oYearDebit = yearPreMap.get("sumDebitAmount");
				oYearLend = yearPreMap.get("sumLendAmount");
			}else{
				//取上一期的余额+本期的借贷方差额
				HashMap lastcurMap = null;																		//上一期的金额数据
				Integer startPeriod = Integer.valueOf(accPeriod);
				boolean flag = false;
				Integer beforeYear = accYear;
				for(int k=accYear;k>=yearStarts;k--){
					for(int l=startPeriod-1;l>0;l--){
						lastcurMap = (HashMap)values.get(k+"_"+l);												//从values中取出数据
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
					//存在上一期的数据
					upsumDCBala = lastcurMap.get("sumDCBala");
					o = lastcurMap.get("sumDCBala");												//上一期的期末余额
					if(beforeYear.equals(accYear)){
						oYearDebit = lastcurMap.get("yearsumDebitAmount");								//上一期的本年借方
						oYearLend = lastcurMap.get("yearsumLendAmount");								//上一期的本年贷方
					}
				}
			}
			if(o != null && !"".equals(o)){
				moneysumDCBala = o.toString();
			}
			o = curMap.get("sumDCBala");			//本期借贷方差额
			if(o != null && !"".equals(o)){
				moneysumDCBala = new BigDecimal(moneysumDCBala).add(new BigDecimal(o.toString())).toString();
			}
			//本期余额
			curMap.put("sumDCBala", moneysumDCBala);
			
			/**
			 * 处理本年金额（本年借贷方= 上一期间的本年借贷方+本期借贷方）
			 */
			if(oYearDebit != null && !"".equals(oYearDebit)){
				moneysumDebit = oYearDebit.toString();
			}
			oYearDebit = curMap.get("sumDebitAmount");						//本期借方金额
			if(oYearDebit != null && !"".equals(oYearDebit)){
				moneysumDebit = new BigDecimal(moneysumDebit).add(new BigDecimal(oYearDebit.toString())).toString();
			}
			//本年借方金额
			curMap.put("yearsumDebitAmount", moneysumDebit);
			
			if(oYearLend != null && !"".equals(oYearLend)){
				moneysumLend = oYearLend.toString();
			}
			oYearLend = curMap.get("sumLendAmount");						//本期贷方金额
			if(oYearLend != null && !"".equals(oYearLend)){
				moneysumLend = new BigDecimal(moneysumLend).add(new BigDecimal(oYearLend.toString())).toString();
			}
			//本年贷方金额
			curMap.put("yearsumLendAmount", moneysumLend);
			curMap.put("yearsumDCBala", moneysumDCBala);					//本年余额
			
			
			/**
			 * 获取凭证明细
			 */
			List accList = new ArrayList();
			for(int j = 0;j<accMainList.size();j++){
				HashMap accmain = (HashMap)accMainList.get(j);
				if(map.get("AccYear").equals(accmain.get("CredYear")) && map.get("AccPeriod").equals(accmain.get("Period"))){
					//属于该会计期间的凭证明细
					String money = "0";
					Object object = new Object();
					if(accList.size()>0){
						//不是第一个明细时 （余额=上一个余额+本明细的借贷差额）
						HashMap accMap = (HashMap)accList.get(accList.size()-1);
						money = "0";
						object = accMap.get("sumAmount");													//上一条的余额
						if(object!=null && !"".equals(object)){
							money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
						}
						object = accmain.get("sumAmount");													//本条的余额
						if(object!=null && !"".equals(object)){
							money = new BigDecimal(money).add(new BigDecimal(object.toString())).toString();
						}
						accmain.put("sumAmount", money);
					}else{
						//如果是第一个时，上一期间的本期余额+本明细的借贷差额
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
			curMap.put("accMainList", accList);													//明细
			values.put(accYear+"_"+accPeriod, curMap);
		}
		
		/**
		 * 金额处理
		 */
		HashMap map = new HashMap();
		Iterator iterator = values.entrySet().iterator();
		while(iterator.hasNext()){
			Entry entry = (Entry)iterator.next();
			String key = String.valueOf(entry.getKey());
			HashMap hashMap = (HashMap)entry.getValue();
			String[] strs = null;
			if("initMap".equals(key)){
				//期初map处理
				strs = new String[]{"sumAmount","sumCurrencyAmount","sumBalanceAmount","sumBalanceCurrencyAmount","sumAmount"};
				for(String s : strs){
					hashMap.put(s,dealDataDouble(hashMap.get(s)==null?"":String.valueOf(hashMap.get(s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
				}
			}else{
				//其它数据处理
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
							//借方
							hashMap.put(isFlag+"isflag","借");
						}else if(Double.parseDouble(m)<0){
							//贷方
							hashMap.put(isFlag+"isflag","贷");
						}else{
							hashMap.put(isFlag+"isflag","平");	
						}
					}
					hashMap.put(s,dealDataDouble(hashMap.get(s)==null?"":String.valueOf(hashMap.get(s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
				}
				
				//处理明细
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
									//借方
									detail.put("isflag","借");
								}else if(Double.parseDouble(m)<0){
									//贷方
									detail.put("isflag","贷");
								}else{
									detail.put("isflag","平");	
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
	 * 处理金额数据
	 * @param value  值
	 * @param len	 保留小数点后几位
	 * @param flag   abs 取绝对值
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
			//返回的所有都是正数
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
	 * 查询试算平衡表数据
	 * @param conMap
	 * @return
	 */
	public Result accTrialBalance(final HashMap<String,String> conMap,final MOperation mop,final LoginBean loginBean){
		final Result result = new Result();
		
		//对币别进行处理
		String currencyName = conMap.get("currencyName"); 									//币别('isBase'=本位币，''=综合本位币，'all'=所有币别，'外币的id'=外币)
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		//所有外币
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		final String accTypeNoItem = conMap.get("accTypeNoItem");									//科目无发生额不显示
		final String showDetail = conMap.get("showDetail");											//只显示明细
		final String levelStart = conMap.get("levelStart");											//科目级次
		final String showItemDetail = conMap.get("showItemDetail");									//显示核算项目明细
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							
							String binderNo = conMap.get("binderNo");								//包括未过账凭证
							String periodYearStart = conMap.get("periodYearStart");					//会计期间年
							String periodStart = conMap.get("periodStart");							//会计期间
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");			//显示禁用科目

							String startTime = "";
							if(Integer.valueOf(periodStart)<10){
								startTime = periodYearStart+"-0"+periodStart;
							}else{
								startTime = periodYearStart+"-"+periodStart;
							}
							
							String condition = scopeSql(mop, loginBean);
							
							HashMap accMap = new HashMap();
							
							/**
							 * 查询所有满足条件的会计科目
							 */
							StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,l.zh_cn as AccFullName,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,");
							sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.jdFlag FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 ");
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//不显示禁用科目
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							sql.append(condition);
							sql.append(" ORDER BY tblAccTypeInfo.AccNumber");
							System.out.println("科目查询SQL："+sql.toString());
                            /* 执行查询并且保存满足条件的科目 */
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
                             * 勾选了（包括未过账凭证），查询指定期间的未过账数据金额
                             */
                            String str = "";
                            HashMap noBindMap = new HashMap();				//保存期间中的未过账的数据
                            HashMap noBindInitMap = new HashMap();			//保存开始期间之前未过账的数据
							if(binderNo != null && !"".equals(binderNo)){
								String initSql = "";						//查询期初未过账数据sql
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
	                            //当选择外币不是<综合本位币><所有币种>时进行过滤
	                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
	                            	if("isBase".equals(currencyValue)){
	                            		//本位币
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
	                            System.out.println("会计科目未过账sql："+sql.toString());
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
	                            	//当选择的币别是——所有币别多栏式
	                            	if("all".equals(currencyValue)){
	                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala"};
	                            		if("".equals(periodMap.get("Currency"))){
	                            			//本位币时，外币的金额等于当前本位币的金额
	                            			for(String s : curs){
	                            				periodMap.put(s+"_", periodMap.get(s+"Base"));
	                            			}
	                            		}else{
	                            			//其它外币
	                            			for(String s : curs){
	                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
	                            			}
	                            		}
	                            	}
	                            	noBindMap.put(periodMap.get("classCode"), periodMap);
	                            }
	                            
	                            //查询期初数据时进行查询会计期间之前未过账的数据进行统计
	                            initSql = initSql.replaceAll("SELECT ", "SELECT tblAccTypeInfo.JdFlag,");
	                            System.out.println("会计科目前未过："+initSql);
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
	                            	//当选择的币别是——所有币别多栏式
	                            	if("all".equals(currencyValue)){
	                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala"};
	                            		if("".equals(periodMap.get("Currency"))){
	                            			//本位币时，外币的金额等于当前本位币的金额
	                            			for(String s : curs){
	                            				periodMap.put(s+"_", periodMap.get(s+"Base"));
	                            			}
	                            		}else{
	                            			//其它外币
	                            			for(String s : curs){
	                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
	                            			}
	                            		}
	                            	}
	                            	noBindInitMap.put(periodMap.get("classCode"), periodMap);
	                            }
							}
							
                            /**
                             * 查询试算平衡表的金额（tblAccBalance）
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
                            		//本位币
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
                            sql.append(condition);
                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccBalance.Nyear)+'-'+CONVERT(VARCHAR,tblAccBalance.Period)+'-01',120),120)";
                            sql.append(" AND "+str+"='"+startTime+"' AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1");
                            sql.append(" ORDER BY tblAccTypeInfo.classCode,tblAccBalance.Nyear,tblAccBalance.Period");
                            System.out.println("查询试算平衡表金额："+sql.toString());
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
                            	
                            	//当选择的币别是——所有币别多栏式
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
                            	
                				//给上级附值
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
        								//当不存在上一级时，创建新的Map保存数据
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
                            
                            /* 当查询的期间在表tblAccBalance不存在时，使期间往前推，查询最近的记录 */
                            sql = new StringBuffer("SELECT tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccBalance.Nyear,");
                            sql.append("tblAccBalance.Period,tblAccBalance.SubCode,ISNULL(tblAccTypeInfo.isCataLog,0) as isCataLog,");
                            sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,");
                            sql.append("tblAccBalance.CurrYIniBalaBase as PeriodIniBase,tblAccBalance.CurrYIniBala as PeriodIni,");
                            sql.append(" '' AS PeriodDCBalaBase,'' AS PeriodDCBala,tblAccTypeInfo.jdFlag,isnull(tblAccBalance.CurType,'') as CurType ");
                            sql.append(" FROM tblAccBalance left join tblAccTypeInfo ON tblAccBalance.SubCode=tblAccTypeInfo.AccNumber WHERE");
                            sql.append(" SubCode not in (select SubCode from tblAccBalance where "+str+"='"+startTime+"' AND Nyear!=-1 AND Period!=-1) AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1");
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//本位币
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
                            sql.append(" and tblAccBalance.Nyear =(select top 1 a.Nyear from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
							sql.append(" and tblAccBalance.Period =(select top 1 a.Period from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
                            sql.append(condition + " ORDER BY tblAccTypeInfo.classCode");
                            System.out.println("查询试算平衡表最接近的数据："+sql.toString());
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
                            	//当选择的币别是——所有币别多栏式
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
                				//给上级附值
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
        													//借方
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
        								//当不存在上一级时，创建新的Map保存数据
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
        													//借方
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
							
                            //保存数据
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
		/* 获取数据 */
		Object[] obj = (Object[])result.retVal;
		List accTypeInfoList = (ArrayList)obj[0];
		HashMap	accBalanceMap = (HashMap)obj[1];
		HashMap noBindMap = (HashMap)obj[2];
		HashMap noBindInitMap = (HashMap)obj[3];
		
		
		/**
		 * 数据整理
		 */
		
		//处理期间中未过账数据
		if(noBindMap != null && noBindMap.size()>0){
			Iterator nobindMap = noBindMap.entrySet().iterator();
        	//遍历map得到具体的数据
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//得到保存数据的map
				if(bindMap != null && bindMap.size()>0){
					//存在未过账的数据
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
										//本位币
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
							//保存的老金额
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
		
		//把期间之前未过账的数据放入map中
		if(noBindInitMap != null && noBindInitMap.size()>0){
        	/**
        	 * 查询的会计期间之前中存在未过账的数据
        	 */
        	Iterator nobindMap = noBindInitMap.entrySet().iterator();
        	//遍历map得到具体的数据
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//得到保存数据的map
				if(bindMap != null && bindMap.size()>0){
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					String[] str1 = new String[]{"PeriodIniBase","PeriodIni"};
					String[] str2 = new String[]{"PeriodDCBalaBase","PeriodDCBala"};
					
					//所有币别多栏式
					if("all".equals(currencyValue)){
						String[] currS1 = new String[currList.size()+1];
						String[] currS2 = new String[currList.size()+1];
						int counts = 0;
						for(int k = 1;k<str1.length;k++){
							for(int l=0;l<currList.size();l++){
								String[] currStr = (String[])currList.get(l);
								if("true".equals(currStr[2])){
									//本位币
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
		//处理数据
		int count = 0;
		List accList = new ArrayList();
		HashMap totalMap = new HashMap();
		for(int i=0;i<accTypeInfoList.size();i++){
			//循环会计科目进行期初余额和本期发生额，期末余额数据的处理
			HashMap accmap = (HashMap)accTypeInfoList.get(i);
			HashMap hashMap = (HashMap)accBalanceMap.get(accmap.get("classCode").toString());				//得到本会计科目的金额数据
			if(hashMap != null && hashMap.size()>0){
				String[] str1 = null;
				if("all".equals(currencyValue)){
					str1 = new String[currList.size()+1];
					for(int l = 0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//本位币
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
					Object o = hashMap.get("PeriodIni"+s);													//期初金额
					if(o!=null && !"".equals(o)){
						//根据会计期间的方向来处理期初金额在借方还是在贷方
						if(Integer.parseInt(accmap.get("jdFlag").toString()) == 1){
							accmap.put("PeriodIniDebit"+s, dealDataDouble(String.valueOf(o), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}else if(Integer.parseInt(accmap.get("jdFlag").toString()) == 2){
							accmap.put("PeriodIniCredit"+s, dealDataDouble(String.valueOf(o), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
							o = (Object)(0-Double.parseDouble(o.toString()));
						}
					}
					//保存期初金额
					accmap.put("PeriodIni"+s, o);
					
					//处理本期借方，本期贷方金额
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
					
					//期末金额（期初余额+本期借方-本期贷方）
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
					accmap.put("PeriodBala"+s, money);						//期末余额的金额
				}
			}
			
			/* 统计合计金额 */
			String[] strs = new String[]{"PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase",
					"PeriodIniDebit","PeriodIniCredit","PeriodDebitSum","PeriodCreditSum","PeriodDebitBala","PeriodCreditBala"};
			if("all".equals(currencyValue)){
				String[] currS = new String[(currList.size()+1)*6];
				int counts = 0;
				for(int k = 6;k<strs.length;k++){
					for(int l=0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//本位币
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
			
			//科目等级
			if(levelStart!=null && !"".equals(levelStart)){
				String classCodes = accmap.get("classCode").toString();
				if(classCodes.length()/5-1>Integer.parseInt(levelStart)){
					continue;
				}
			}
			
			if(showItemDetail == null || "".equals(showItemDetail)){
				//不显示核算项目明细
				if(String.valueOf(accmap.get("isCalculate").toString()).equals("1")){
					continue;
				}
			}
			if(showDetail !=null && !"".equals(showDetail)){
				//只显示明细科目
				if(Integer.parseInt(accmap.get("isCatalog").toString()) == 0 && Integer.parseInt(accmap.get("isCalculateParent").toString())!=1){
					String classCodes = accmap.get("classCode").toString();					
					if(accList!=null && accList.size()>0){
						for(int j = 0;j<accList.size();j++){
							HashMap newmap = (HashMap)accList.get(j);
							if(newmap.get("classCode").equals(classCodes.substring(0, classCodes.length()-5)) || newmap.get("classCode").equals(classCodes.substring(0, classCodes.length()-10))){
								//存在父级
								accList.remove(j);
							}
						}						
					}
					
				}
			}
			accList.add(accmap);
		}
		
		//科目无发生额不显示
		List accLists = new ArrayList();
		for(int i=0;i<accList.size();i++){
			HashMap accMap = (HashMap)accList.get(i);
			Object sumAmount = accMap.get("PeriodIniBase");								//期初余额
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
			
		/* 试算平衡处理 */
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
		 * 试算平衡判断
		 */
		if(Double.valueOf(money)==0){
			//试算平衡
			totalMap.put("flag", "true");
		}else{
			//试算不平衡
			totalMap.put("flag", "false");
		}
		obj = new Object[]{accLists,totalMap,currencyValue};
		result.setRetVal(obj);
		
		return result;
	}
	
	
	/**
	 * 科目余额报表
	 * 1.查询所有满足条件的会计科目
	 * 2.从科目余额表查询已经过账的数据
	 * 3.查询未过账凭证数据金额
	 * 4.数据整理
	 * @param conMap
	 * @return
	 */
	public Result accTypeInfoBalance(final HashMap<String,String> conMap,final LoginBean loginBean,final MOperation mop){
		final Result result = new Result();
		
		//对币别进行处理
		String currencyName = conMap.get("currencyName"); 									//币别('isBase'=本位币，''=综合本位币，'all'=所有币别，'外币的id'=外币)
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		//所有外币
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
		final String accTypeNoItem = conMap.get("accTypeNoItem");									//科目无发生额不显示
		final String levelStart = conMap.get("levelStart");
		String showItemDetail = conMap.get("showItemDetail");
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							/* 搜索条件 */
							String binderNo = conMap.get("binderNo");
							String acctypeCodeStart = conMap.get("acctypeCodeStart");
							String acctypeCodeEnd = conMap.get("acctypeCodeEnd");
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");
							
							/* 期间处理 */
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
							
							/* 当前会计期间 */
							Hashtable sessionSet = BaseEnv.sessionSet;
							Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
							AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
							
							HashMap accMap = new HashMap();
							
							/**
                             * 期间过滤条件
                             */
                            String condition = scopeSql(mop, loginBean);;
                            
							/**
							 * 查询所有满足条件的会计科目
							 */
							StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,l.zh_cn as AccFullName,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,");
							sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.jdFlag FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 ");
							if(acctypeCodeStart != null && !"".equals(acctypeCodeStart)){
								//科目开始
								condition += " AND (tblAccTypeInfo.AccNumber>='"+acctypeCodeStart+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeStart+"')+'%')";
							}
							if(acctypeCodeEnd != null && !"".equals(acctypeCodeEnd)){
								//科目结束
								condition += " AND (tblAccTypeInfo.AccNumber<='"+acctypeCodeEnd+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeEnd+"')+'%')";
							}
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//不显示禁用科目
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							sql.append(condition);
							sql.append(" ORDER BY tblAccTypeInfo.AccNumber");
							BaseEnv.log.info("科目查询SQL："+sql.toString());
                            /* 执行查询并且保存满足条件的科目 */
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
                             * 勾选了（包括未过账凭证），查询指定期间的未过账数据金额
                             */
                            String str = "";
                            HashMap noBindMap = new HashMap();				//保存期间中的未过账的数据
                            HashMap noBindInitMap = new HashMap();			//保存开始期间之前未过账的数据
							if(binderNo != null && !"".equals(binderNo)){
								String initSql = "";						//查询期初未过账数据sql
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
	                            //当选择外币不是<综合本位币><所有币种>时进行过滤
	                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
	                            	if("isBase".equals(currencyValue)){
	                            		//本位币
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
	                            	//所有币种要对外币进行分组
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
	                            	//当选择的币别是——所有币别多栏式
	                            	if("all".equals(currencyValue)){
	                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
	                            		if("".equals(periodMap.get("Currency"))){
	                            			//本位币时，外币的金额等于当前本位币的金额
	                            			for(String s : curs){
	                            				if("CurrYIni".equals(s)){
	                            					periodMap.put(s+"_", periodMap.get(s+"Amount"));
	                            				}else{
	                            					periodMap.put(s+"_", periodMap.get(s+"Base"));
	                            				}
	                            			}
	                            		}else{
	                            			//其它外币
	                            			for(String s : curs){
	                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
	                            			}
	                            		}
	                            	}
	                            	noBindMap.put(periodMap.get("classCode"), periodMap);
	                            }
	                            
	                            //查询期初数据时进行查询会计期间之前未过账的数据进行统计
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
	                            	//当选择的币别是——所有币别多栏式
	                            	if("all".equals(currencyValue)){
	                            		String[] curs = new String[]{"PeriodDebitSum","PeriodCreditSum","PeriodDCBala","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni"};
	                            		if("".equals(periodMap.get("Currency"))){
	                            			//本位币时，外币的金额等于当前本位币的金额
	                            			for(String s : curs){
	                            				if("CurrYIni".equals(s)){
	                            					periodMap.put(s+"_", periodMap.get(s+"Amount"));
	                            				}else{
	                            					periodMap.put(s+"_", periodMap.get(s+"Base"));
	                            				}
	                            			}
	                            		}else{
	                            			//其它外币
	                            			for(String s : curs){
	                            				periodMap.put(s+"_"+periodMap.get("Currency"), periodMap.get(s));
	                            			}
	                            		}
	                            	}
	                            	noBindInitMap.put(periodMap.get("classCode"), periodMap);
	                            }
							}
                            
                            /**
                             * 查询科目余额表的金额（tblAccBalance）
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
                            		//本位币
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
                            sql.append(condition);
                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccBalance.Nyear)+'-'+CONVERT(VARCHAR,tblAccBalance.Period)+'-01',120),120)";
                            sql.append(" AND "+str+">='"+startTime+"' AND "+str+"<='"+endTime+"' AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1 AND (tblAccBalance.DepartmentCode is null or tblAccBalance.DepartmentCode='')");
                            sql.append(" ORDER BY tblAccTypeInfo.classCode,tblAccBalance.Nyear,tblAccBalance.Period");
                            BaseEnv.log.info("查询科目余额表金额："+sql.toString());
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
                            	
                            	//当选择的币别是——所有币别多栏式
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodIni","PeriodDebitSum","PeriodCreditSum","CurrYIniDebitSum","CurrYIniCreditSum","PeriodBala","CurrYIni","PeriodDCBala"};
                            		if("".equals(map.get("CurType"))){
                            			//本位币时，外币的金额等于当前本位币的金额
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					map.put(s+"_", map.get(s+"Amount"));
                            				}else{
                            					map.put(s+"_", map.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//其它外币
                            			for(String s : curs){
                            				map.put(s+"_"+map.get("CurType"), map.get(s));
                            			}
                            		}
                            	}
                            	
                            	HashMap oldAccMap = (HashMap)accBalanceMap.get(map.get("classCode"));
                            	HashMap tempMap=(HashMap)map.clone();
                            	if(oldAccMap != null){
                            		//期初取第一个期间的
                            		if(accClassCode.equals(map.get("classCode"))){
                            			map.put("PeriodIniBase",oldAccMap.get("PeriodIniBase"));
                            			map.put("PeriodIni",oldAccMap.get("PeriodIni"));
                            			map.put("PeriodIni_"+map.get("CurType"), oldAccMap.get("PeriodIni_"+map.get("CurType")));
                            		}
                            		
                            		
                            		//当用多个期间进行查询时，本期借贷方要进行累加
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
                				//给上级附值
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
        											//如果是年累计借贷，则要减去当前科目的之前期间的年累计借贷金额.(当前期间的年累计借贷已经包含了之前期间的)
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
        								//当不存在上一级时，创建新的Map保存数据
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
                            
                            /* 当查询的期间在表tblAccBalance不存在时，使期间往前推，查询最近的记录 */
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
                            		//本位币
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
                            sql.append(" AND tblAccBalance.Nyear =(select top 1 a.Nyear from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
							sql.append(" AND tblAccBalance.Period =(select top 1 a.Period from tblAccBalance a where a.SubCode=tblAccBalance.SubCode and ((a.Nyear="+periodYearStart+" and a.Period<"+periodStart+") or (a.Nyear<"+periodYearStart+")) order by a.Nyear desc,a.Period desc)");
                            sql.append(condition + " ORDER BY tblAccTypeInfo.classCode");
                            BaseEnv.log.info("查询科目余额表最接近的数据："+sql.toString());
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
                            	
                            	//当选择的币别是——所有币别多栏式
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"PeriodIni","CurrYIniDebitSum","CurrYIniCreditSum","CurrYIni","PeriodDCBala"};
                            		if("".equals(map.get("CurType"))){
                            			//本位币时，外币的金额等于当前本位币的金额
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					map.put(s+"_", map.get(s+"Amount"));
                            				}else{
                            					map.put(s+"_", map.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//其它外币
                            			for(String s : curs){
                            				map.put(s+"_"+map.get("CurType"), map.get(s));
                            			}
                            		}
                            	}
                            	
                				//给上级附值
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
        													//借方
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
        								//当不存在上一级时，创建新的Map保存数据
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
        													//借方
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
                            
							/* 保存数据 */
							//accTypeInfoList 会计科目列表,accBalanceMap 已过账的数据map,noBindMap 未过账的map,noBindInitMap 查询期间之前的未过账数据
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
		
		/* 获取数据 */
		Object[] obj = (Object[])result.retVal;
		List accTypeInfoList = (ArrayList)obj[0];
		HashMap	accBalanceMap = (HashMap)obj[1];
		HashMap noBindMap = (HashMap)obj[2];
		HashMap noBindInitMap = (HashMap)obj[3];
		
		String accTypeNoOperation = conMap.get("accTypeNoOperation");					//包括没有业务发生的科目（期初，本年累计）
		String accTypeNoPeriod = conMap.get("accTypeNoPeriod");							//包括本期没有发生额的科目
		String accTypeNoYear = conMap.get("accTypeNoYear");								//包括本年没有发生额的科目
		String takeBrowNo = conMap.get("takeBrowNo");									//包括无发生额的科目
		
		/**
		 * 数据整理
		 */
		
		//处理期间中未过账数据
		if(noBindMap != null && noBindMap.size()>0){
			Iterator nobindMap = noBindMap.entrySet().iterator();
        	//遍历map得到具体的数据
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//得到保存数据的map
				if(bindMap != null && bindMap.size()>0){
					//存在未过账的数据
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
										//本位币
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
							//保存的老金额
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
		
		//把期间之前未过账的数据放入map中
		if(noBindInitMap != null && noBindInitMap.size()>0){
        	/**
        	 * 查询的会计期间之前中存在未过账的数据
        	 */
        	Iterator nobindMap = noBindInitMap.entrySet().iterator();
        	//遍历map得到具体的数据
        	while(nobindMap.hasNext()){
        		Entry entry = (Entry)nobindMap.next();
        		HashMap bindMap = (HashMap)entry.getValue();							//得到保存数据的map
				if(bindMap != null && bindMap.size()>0){
					String bindClassCode = String.valueOf(bindMap.get("classCode"));
					String[] str1 = new String[]{"PeriodIniBase","PeriodIni"};
					String[] str2 = new String[]{"PeriodDCBalaBase","PeriodDCBala"};
					
					//所有币别多栏式
					if("all".equals(currencyValue)){
						String[] currS1 = new String[currList.size()+1];
						String[] currS2 = new String[currList.size()+1];
						int counts = 0;
						for(int k = 1;k<str1.length;k++){
							for(int l=0;l<currList.size();l++){
								String[] currStr = (String[])currList.get(l);
								if("true".equals(currStr[2])){
									//本位币
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
		
		//循环数据
		int count = 0;
		List accList = new ArrayList();
		HashMap totalMap = new HashMap();
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap accmap = (HashMap)accTypeInfoList.get(i);
			//获取最开始会计期间的金额数据
			HashMap hashMap = (HashMap)accBalanceMap.get(accmap.get("classCode").toString());
			if(hashMap != null && hashMap.size()>0){
				
				String[] str1 = null;
				if("all".equals(currencyValue)){
					str1 = new String[currList.size()+1];
					for(int l = 0;l<currList.size();l++){
						String[] currStr = (String[])currList.get(l);
						if("true".equals(currStr[2])){
							//本位币
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
						//期初金额
						if(Integer.parseInt(accmap.get("jdFlag").toString()) == 1){
							accmap.put("PeriodIniDebit"+s, dealDataDouble(String.valueOf(o), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						}else if(Integer.parseInt(accmap.get("jdFlag").toString()) == 2){
							accmap.put("PeriodIniCredit"+s, dealDataDouble(String.valueOf(o), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
							o = (Object)(0-Double.parseDouble(o.toString()));
						}
					}
					accmap.put("PeriodIni"+s, o);
					accmap.put("CurrYIniDebitSum"+s, dealDataDouble(String.valueOf(hashMap.get("CurrYIniDebitSum"+s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));		//本年累计借方
					accmap.put("CurrYIniCreditSum"+s, dealDataDouble(String.valueOf(hashMap.get("CurrYIniCreditSum"+s)), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));		//本年累计贷方
					
					//本期借方，本期贷方
					String money = "0";
					String[] strs = new String[]{"PeriodDebitSum"+s,"PeriodCreditSum"+s};
					for(String str : strs){
						money = "0";
						o = accmap.get(str);														//原来本期借方金额
						if(o != null && !"".equals(o)){
							money = o.toString();
						}
						o = hashMap.get(str);
						if(o != null && !"".equals(o)){
							money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
						}
						accmap.put(str, dealDataDouble(String.valueOf(money), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					}
					
					//期末金额（期初余额+本期借方-本期贷方）
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
			
			/* 统计合计金额 */
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
							//本位币
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
			/* 无业务发生的不显示该科目 */
			for(int k = 0;k<strs.length;k++){
				Object o = accmap.get(strs[k]);
				if(o == null || "".equals(o)){
					//不存在记录
					flags += ",true,";
				}else{
					flags += ",false,";
				}
			}
			if(takeBrowNo == null || "".equals(takeBrowNo)){
				boolean flag = false;
				/*没有业务发生的科目（期初，本年累计）*/
				if(accTypeNoOperation!= null && !"".equals(accTypeNoOperation)){
					Object o1 = accmap.get("PeriodIniBase");
					Object o2 = accmap.get("CurrYIniDebitSumBase");
					Object o3 = accmap.get("CurrYIniCreditSumBase");
					if("".equals(o1) && "".equals(o2) && "".equals(o3)){
						flag = true;
					}
				}
				/* 本期无发生额科目 */
				if(accTypeNoPeriod != null && !"".equals(accTypeNoPeriod)){
					Object o1 = accmap.get("PeriodDebitSumBase");
					Object o2 = accmap.get("PeriodCreditSumBase");
					if("".equals(o1) && "".equals(o2)){
						flag = true;
					}
				}
				/* 本年无发生额科目 */
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
				//科目等级开始
				if(classCode.length()/5-1<=Integer.parseInt(levelStart)){
					falg = true;
				}else{
					falg = false;
				}
			}
			
			if(showItemDetail == null || "".equals(showItemDetail)){
				//不显示核算项目明细
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
	 * 科目日报表
	 * 1.查询会计科目
	 * 2.查询本日借贷方金额
	 * 3.查询上日借贷方金额
	 * 4.查询借方笔数/贷方笔数
	 * 5.整理数据
	 * @param conMap
	 * @return
	 */
	protected Result accTypeInfoDay(final HashMap<String,String> conMap, final MOperation mop, final LoginBean loginBean){
		//对币别进行处理
		String currencyName = conMap.get("currencyName"); 									//币别('isBase'=本位币，''=综合本位币，'all'=所有币别，'外币的id'=外币)
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		//所有外币
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		
		/**
		 * 查询数据库数据
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
							
							/* 搜索条件 */
							String dateStart = conMap.get("dateStart");
							String dateEnd = conMap.get("dateEnd");
							String binderNo = conMap.get("binderNo");
							
							HashMap accMap = new HashMap();										//保存特殊的数据，下面代码中有用到
							String condition = scopeSql(mop, loginBean);						//条件
							String currentCondition = "";
							/**
							 * 查询会计科目
							 */
							StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog, ");
							sql.append("ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.JdFlag,l.zh_cn as AccFullName FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 ");
							if(acctypeCodeStart != null && !"".equals(acctypeCodeStart)){
								//科目开始
								condition += " AND (tblAccTypeInfo.AccNumber>='"+acctypeCodeStart+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeStart+"')+'%')";
							}
							if(acctypeCodeEnd != null && !"".equals(acctypeCodeEnd)){
								//科目结束
								condition += " AND (tblAccTypeInfo.AccNumber<='"+acctypeCodeEnd+"' or tblAccTypeInfo.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+acctypeCodeEnd+"')+'%')";
							}
							if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//不显示禁用科目
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							currentCondition = condition;
							sql.append(condition);
							sql.append(" ORDER BY tblAccTypeInfo.classCode");
							System.out.println("科目查询SQL："+sql.toString());
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
	            				//不包括未过账凭证
								condition += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
	            			}
							if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//本位币
                            		condition += " AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+searchCurrency+"') ";
                            	}else{
                            		//其它外币时
                            		condition += " AND tblAccDetail.Currency='"+searchCurrency+"' ";
                            	}
                            }
							HashMap totalMap = new HashMap();
							/**
							 * 查询上日借贷方金额
							 */
							sql = new StringBuffer("SELECT tblAccDetail.AccCode,tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent");
							if("all".equals(currencyValue)){
								//所有币别多栏式时对币种进行分组
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
							System.out.println("查询上日借贷方金额："+sql.toString());
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
								//当选择的币别是——所有币别多栏式
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumdisAmount"};
                            		String base = "";
                            		if("".equals(map.get("Currency"))){
                            			//本位币
                            			base = "Base";
                            		}
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("Currency"), map.get(s+base));
                        				lineStr += s+"_"+map.get("Currency")+";";
                        			}
                            	}
                            	/**
								 * 整理上日金额的合计
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
//											//贷方
//											m = new BigDecimal(m).subtract(new BigDecimal(object.toString())).toString();
//										}else{
											m = new BigDecimal(m).add(new BigDecimal(object.toString())).toString();
//										}
									}
									totalMap.put("pre_"+s, m.toString());
								}
								
            					String classCode = map.get("classCode").toString();
            					preMap.put(classCode,map);
            					
            					//给上级附值
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
        								//当不存在上一级时，创建新的Map保存数据
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
							
							/* 查询开账前的期初金额 */
							sql = new StringBuffer("SELECT SubCode as AccCode,tblAccTypeInfo.classCode,");
							sql.append("(case tblAccTypeInfo.JdFlag when 2 then 0-CurrYIniBalaBase else CurrYIniBalaBase end) AS sumdisAmountBase,");
							sql.append("(case tblAccTypeInfo.JdFlag when 2 then 0-CurrYIniBala else CurrYIniBala end) AS sumdisAmount,");
							sql.append("isnull(tblAccTypeInfo.isCalculate,'') AS isCalculate,tblAccTypeInfo.isCataLog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.JdFlag,");
							sql.append("isnull(tblAccBalance.CurType,'') as CurType FROM tblAccBalance left join tblAccTypeInfo on tblAccBalance.subCode=tblAccTypeInfo.AccNumber");
							sql.append(" WHERE Nyear=-1 AND Period=-1 AND tblAccTypeInfo.classCode not like '00004%' AND tblAccTypeInfo.isCatalog=0 AND ISNULL(tblAccTypeInfo.isCalculateParent,0)!=1");
							if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//本位币
                            		sql.append(" AND (tblAccBalance.CurType is null or tblAccBalance.CurType='' or tblAccBalance.CurType='"+searchCurrency+"') ");
                            	}else{
                            		//其它外币时
                            		sql.append(" AND tblAccBalance.CurType='"+searchCurrency+"' ");
                            	}
                            }
							sql.append(currentCondition);
							sql.append(" ORDER BY SubCode");
							System.out.println("期初sql:"+sql.toString());
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
	                        	//当选择的币别是——所有币别多栏式
	                        	String lineStr = "sumdisAmount;sumdisAmountBase;";
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumdisAmount"};
                            		String base = "";
                            		if("".equals(map.get("CurType"))){
                            			//本位币
                            			base = "Base";
                            		}
                        			for(String s : curs){
                        				map.put(s+"_"+map.get("CurType"), map.get(s+base));
                        				lineStr += s+"_"+map.get("CurType")+";";
                        			}
                            	}
                            	
                            	/**
								 * 整理合计
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
//											//贷方
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
	                        			//存在凭证期初金额时
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
            					//给上级附值
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
        								//当不存在上一级时，创建新的Map保存数据
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
							 * 根据日期查询统计凭证的借贷金额
							 */
							sql = new StringBuffer("SELECT tblAccDetail.AccCode,tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,");
							if("all".equals(currencyValue)){
								//所有币别多栏式时对币种进行分组
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
							System.out.println("查询本日借贷方金额："+sql.toString());
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
								
								//当选择的币别是——所有币别多栏式
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
								 * 整理金额的合计
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
//											//贷方
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
                            	
                        		//给上级附值
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
        								//当不存在上一级时，创建新的Map保存数据
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
                            		/* 对外币进行分组时，借方金额或者贷方金额要进行叠加 */
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
							 * 统计借贷笔数
							 */
							String conditions = "FROM tblAccDetail JOIN tblAccMain ON tblAccDetail.f_ref=tblAccMain.id JOIN tblAccTypeInfo ON tblAccTypeInfo.AccNumber=tblAccDetail.AccCode";
							conditions += " WHERE tblAccMain.BillDate>='"+dateStart+"' AND tblAccMain.BillDate<='"+dateEnd+"'"+condition;
							sql = new StringBuffer("SELECT 'debit' as groups,ISNULL(COUNT(tblAccDetail.DebitAmount),0) as count,tblAccTypeInfo.classCode,tblAccDetail.AccCode "+conditions+" AND tblAccDetail.DebitAmount!=0 GROUP BY tblAccDetail.AccCode,tblAccTypeInfo.classCode");
							sql.append(" UNION ALL ");
							sql.append("SELECT 'lend' as groups,ISNULL(COUNT(tblAccDetail.LendAmount),0) as count,tblAccTypeInfo.classCode,tblAccDetail.AccCode "+conditions+" AND tblAccDetail.LendAmount!=0 GROUP BY tblAccDetail.AccCode,tblAccTypeInfo.classCode");
							System.out.println("借贷笔数："+sql.toString());
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
								
								//统计借贷笔数和
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
							 * 保存数据
							 * accTypeInfoList 会计科目
							 * periodMap       本日借贷方金额
							 * preMap          上日借贷方金额
							 * countMap        本日借贷笔数
							 * accMap          科目具体的数据（accnumber,jdflag,isCalculate）
							 * totalMap        合计
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
		
		/* 获取数据 */
		Object[] obj = (Object[])result.retVal;
		List accTypeInfoList = (ArrayList)obj[0];
		HashMap periodMaps = (HashMap)obj[1];
		HashMap preMaps = (HashMap)obj[2];
		HashMap countMap = (HashMap)obj[3];
		HashMap accMap = (HashMap)obj[4];
		HashMap totalMap = (HashMap)obj[5];											//合计
		
		//得到余额（上日余额+本日借贷方差）
		Iterator iter = periodMaps.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			HashMap periodMap = (HashMap)entry.getValue();
			String isCatalog = String.valueOf(periodMap.get("isCatalog"));
			if(periodMap.get("JdFlag") == null){
				continue;
			}
			int jdFlag = Integer.parseInt(periodMap.get("JdFlag").toString());
			
			//得到上日余额
			String[] strs = new String[]{"sumdisAmountBase","sumdisAmount"};
			
			//所有币别多栏式
			if("all".equals(currencyValue)){
				strs = new String[currList.size()+1];
				for(int k=0;k<currList.size();k++){
					String[] currStr = (String[])currList.get(k);
					String curId = currStr[0];
					if("true".equals(currStr[2])){
						//本位币
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
				//保存余额
				periodMap.put("total"+s, dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
			}
		}
		iter = preMaps.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			HashMap preMap = (HashMap)entry.getValue();
			HashMap pMap = (HashMap)periodMaps.get(entry.getKey());
			if(pMap==null){
				//当不存在本日金额时，上日余额当为本日余额
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
				//所有币别多栏式
				if("all".equals(currencyValue)){
					strs = new String[currList.size()+1];
					for(int k=0;k<currList.size();k++){
						String[] currStr = (String[])currList.get(k);
						String curId = currStr[0];
						if("true".equals(currStr[2])){
							//本位币
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
		
		String takeBrowNo = conMap.get("takeBrowNo");					//无发生额不显示
		String balanceZero = conMap.get("balanceZero");					//余额为零不显示
		List accList = new ArrayList();
		
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			String classCode = (String)map.get("classCode");
			HashMap periodMap = (HashMap)periodMaps.get(classCode);
			HashMap preMap = (HashMap)preMaps.get(classCode);
			
			Integer isCatalog = Integer.parseInt(map.get("isCatalog").toString());
			
			if(takeBrowNo!=null && !"".equals(takeBrowNo)){
				//选择了无发生额不显示
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
				//余额为零不显示
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
				//科目等级结束
				if(classCodes.length()/5-1>Integer.parseInt(levelEnd)){
					continue;
				}
			}
			if(showItemDetail == null || "".equals(showItemDetail)){
				//包括核算项目明细
				if("1".equals(map.get("isCalculate").toString())){
					continue;
				}
			}
			accList.add(map);
		}
		
		//数据小数点保留和正负处理(上日余额)
		for(int i=0;i<accTypeInfoList.size();i++){
			HashMap map = (HashMap)accTypeInfoList.get(i);
			String classCode = (String)map.get("classCode");
			HashMap periodMap = (HashMap)periodMaps.get(classCode);
			HashMap preMap = (HashMap)preMaps.get(classCode);
			
			/**
			 * 处理上日余额
			 */
			if(preMap != null && preMap.size()>0){
				String lineStr = "sumdisAmount;sumdisAmountBase;";
				if("all".equals(currencyValue)){
					for(int k=0;k<currList.size();k++){
						String[] currStr = (String[])currList.get(k);
						String curId = currStr[0];
						if("true".equals(currStr[2])){
							//本位币
							curId = "";
						}
						lineStr += "sumdisAmount_"+curId+";";
					}
				}
				String[] fieldStr = lineStr.split(";");
				for(int k=0;k<fieldStr.length;k++){
					if(preMap.get(fieldStr[k])!=null && !"".equals(preMap.get(fieldStr[k]))){
						//方向处理
						String money = String.valueOf(preMap.get(fieldStr[k]));
						preMap.put(fieldStr[k],dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
					}
				}
			}
			
			/**
			 * 处理本日余额
			 */
			if(periodMap != null && periodMap.size()>0){
				String lineStr = "totalsumdisAmount;totalsumdisAmountBase;";
				if("all".equals(currencyValue)){
					for(int k=0;k<currList.size();k++){
						String[] currStr = (String[])currList.get(k);
						String curId = currStr[0];
						if("true".equals(currStr[2])){
							//本位币
							curId = "";
						}
						lineStr += "totalsumdisAmount_"+curId+";";
					}
				}
				String[] fieldStr = lineStr.split(";");
				for(int k=0;k<fieldStr.length;k++){
					if(periodMap.get(fieldStr[k])!=null && !"".equals(periodMap.get(fieldStr[k]))){
						//方向处理
						String money = String.valueOf(periodMap.get(fieldStr[k]));
						periodMap.put(fieldStr[k],dealDataDouble(money, BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
						//计算合计
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
	 * 凭证汇总表
	 * @param conMap
	 * @return
	 */
	protected Result accCertificateSum(final HashMap<String,String> conMap,final MOperation mop,final LoginBean loginBean){
		//币种搜索处理
		String currencyName = conMap.get("currencyName");
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		final String showItemDetail = conMap.get("showItemDetail");
		final String takeBrowNo = conMap.get("takeBrowNo");
		final String levelStart = conMap.get("levelStart");						//科目级别开始
		final String levelEnd = conMap.get("levelEnd");							//科目级别结束
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							String dateStart = conMap.get("dateStart");							//日期开始
							String dateEnd = conMap.get("dateEnd");								//日期结束
							String area = conMap.get("area");									//范围（0 所有凭证，1 未过账凭证，2 已过账凭证）
							String showAll = conMap.get("showAll");								//包含所有凭证字号
							String credTypeStr = conMap.get("credTypeStr");
							
							HashMap accMap = new HashMap();
							
							String condition = scopeSql(mop, loginBean);
							
							/**
							 * 查询所有会计科目
							 */
							StringBuffer sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber,tblAccTypeInfo.classCode,isnull(tblAccTypeInfo.isCalculate,'') as isCalculate,tblAccTypeInfo.isCatalog,ISNULL(tblAccTypeInfo.isCalculateParent,0) as isCalculateParent,tblAccTypeInfo.JdFlag,l.zh_cn as AccFullName ");
							sql.append("FROM tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccFullName WHERE 1=1 "+condition);
							sql.append(" ORDER BY tblAccTypeInfo.AccNumber");
							System.out.println("科目查询SQL："+sql.toString());
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
							
							HashMap totalMap = new HashMap();									//合计map
							/**
							 * 本期借贷方金额
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
                            //搜索条件
                            if(dateStart!=null && !"".equals(dateStart)){
                            	sql.append(" AND tblAccMain.BillDate>='"+dateStart+"'");
                            }
                            if(dateEnd != null && !"".equals(dateEnd)){
                            	sql.append(" AND tblAccMain.BillDate<='"+dateEnd+"'");
                            }
                            if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//本位币
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
                            	//未勾选所有凭证
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
                            System.out.println("凭证汇总表sql:"+sql);
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
                            	
                            	//当选择的币别是——所有币别多栏式
                            	if("all".equals(currencyValue)){
                            		String[] curs = new String[]{"sumDebitAmount","sumLendAmount","sumBalanceAmount"};
                            		if("".equals(map.get("Currency"))){
                            			//本位币时，外币的金额等于当前本位币的金额
                            			for(String s : curs){
                            				if("CurrYIni".equals(s)){
                            					map.put(s+"_", map.get(s+"Amount"));
                            				}else{
                            					map.put(s+"_", map.get(s+"Base"));
                            				}
                            			}
                            		}else{
                            			//其它外币
                            			for(String s : curs){
                            				map.put(s+"_"+map.get("Currency"), map.get(s));
                            			}
                            		}
                            	}
                            	
                        		/**
            					 * 给上级附值
            					 */
                        		String classCode = map.get("classCode").toString();
            					for(int j=0;j<(classCode.length()-5)/5;j++){
            						//会计科目classCode进行循环获取上级
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
    									
    									//统计合计
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
                             * accTypeInfoList 会计科目,accMainMap 凭证的数据,totalMap 合计
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
		 * 数据整理
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
			
			//无发生额不显示
			if(takeBrowNo != null && !"".equals(takeBrowNo)){
				if(map == null || map.size()==0){
					continue;
				}
			}
			
			//显示核算项目明细
			if(showItemDetail == null || "".equals(showItemDetail)){
				if("1".equals(acctypeMap.get("isCalculate"))){
					continue;
				}
			}
			
			//科目等级开始
			String classCodes = acctypeMap.get("classCode").toString();
			int count = 1;
			if(classCode.length()>10){
				count = (classCodes.length()-5)/5 ;
			}
			//科目等级结束
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
								acctypeMap.put("isflag","借");
							}else if(moneys<0){
								acctypeMap.put("isflag","贷");
							}else{
								acctypeMap.put("isflag","平");
							}
						}else{
							acctypeMap.put("isflag","平");
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
		
		//合计金额进行处理
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
							newTotalMap.put("isflag","借");
						}else if(moneys<0){
							newTotalMap.put("isflag","贷");
						}else{
							newTotalMap.put("isflag","平");
						}
					}else{
						newTotalMap.put("isflag","平");
					}
				}
				if(entry.getKey().toString().indexOf("sumBalanceAmount")!=-1){
					newTotalMap.put(entry.getKey(), dealDataDouble(entry.getValue().toString(), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
				}else{
					newTotalMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		//保存数据
		obj = new Object[]{accList,newTotalMap,currencyValue};
		result.setRetVal(obj);
		return result;
	}
	
	
	/**
	 * 核算项目余额表
	 * 1.查询本期发生的借贷金额
	 * 2.查询会计科目的期初余额借贷金额
	 * 3.组合数据（数据的组合，统计本年累计借贷，期末余额）
	 * @param conMap
	 * @return
	 */
	protected Result accCalculateBalance(final HashMap<String,String> conMap,final MOperation mop,final LoginBean loginBean){
		
		/* 外币处理 */
		String currencyName = conMap.get("currencyName");							//币别('isBase'=本位币，''=综合本位币，'all'=所有币别，'外币的id'=外币)
		Result rs = queryIsBase(currencyName);
		currencyName = rs.retVal.toString();
		
		//所有外币
		rs = queryCurrencyAll();
		List currList = (ArrayList)rs.retVal;
		
		final String currencyValue = currencyName;
		final String searchCurrency = conMap.get("currencyName");
		
		final String takeBrowNo = conMap.get("takeBrowNo");							//余额为零不显示
		final String balanceAndTakeBrowNo = conMap.get("balanceAndTakeBrowNo");		//显示发生额为零的记录
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							Statement st = conn.createStatement();
							ResultSet rset = null;
							
							/* 搜索条件 */
							String yearStart = conMap.get("periodYearStart");						//会计期间开始年
							String periodStart = conMap.get("periodStart");							//会计期间开始
							String yearEnd = conMap.get("periodYearEnd");							//会计期间结束年
							String periodEnd = conMap.get("periodEnd");								//会计期间结束	
							String accCode = conMap.get("accCode");									//会计科目
							String currencyName = conMap.get("currencyName");						//币别
							String itemSort = conMap.get("itemSort");								//项目类别
							String itemCodeStart = conMap.get("itemCodeStart");						//项目代码开始
							String itemCodeEnd = conMap.get("itemCodeEnd");							//项目代码结束
							String levelStart = conMap.get("levelStart");							//科目级别开始
							String binderNo = conMap.get("binderNo");								//包括未过账凭证
							String showBanAccTypeInfo = conMap.get("showBanAccTypeInfo");			//显示禁用科目
							
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
							
							/* 过滤条件 */
							String accStr = "";
							String condition = scopeSql(mop, loginBean);
							if(binderNo == null || "".equals(binderNo)){
	            				//不包括未过账凭证
								condition += " AND (tblAccMain.workFlowNodeName='finish' OR tblAccMain.workFlowNode='-1') ";
	            			}
							String itemType = "";					//核算值
							String isType = "";						//核算类别
							String acctypeItem = "";
							if("DepartmentCode".equals(itemSort)){
								//点击部门
								itemType = "tblAccDetail.DepartmentCode";
								isType = "tblAccTypeInfo.IsDept=1";
								acctypeItem = "tblAccTypeInfo.DepartmentCode";
							}else if("EmployeeID".equals(itemSort)){
								//点击职员
								itemType = "tblAccDetail.EmployeeID";
								isType = "tblAccTypeInfo.IsPersonal=1";
								acctypeItem = "tblAccTypeInfo.EmployeeID";
							}else if("StockCode".equals(itemSort)){
								//点击仓库
								itemType = "tblAccDetail.StockCode";
								isType = "tblAccTypeInfo.isStock=1";
								acctypeItem = "tblAccTypeInfo.StockCode";
							}else if("ClientCode".equals(itemSort) || "SuplierCode".equals(itemSort)){
								//点击客户或者供应商
								itemType = "tblAccDetail.CompanyCode";
								if("ClientCode".equals(itemSort)){
									isType = "tblAccTypeInfo.IsClient=1";
									acctypeItem = "tblAccTypeInfo.ClientCode";
								}else if("SuplierCode".equals(itemSort)){
									isType = "tblAccTypeInfo.IsProvider=1";
									acctypeItem = "tblAccTypeInfo.SuplierCode";
								}
							}else if("ProjectCode".equals(itemSort)){
								//点击职员
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
								//不显示禁用科目
								condition += " AND tblAccTypeInfo.statusId=0";
							}
							String str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,tblAccMain.CredYear)+'-'+CONVERT(VARCHAR,tblAccMain.Period)+'-01',120),120)";
							if(!"".equals(currencyValue) && !"all".equals(currencyValue)){
                            	if("isBase".equals(currencyValue)){
                            		//本位币
                            		condition += " AND (tblAccDetail.Currency is null or tblAccDetail.Currency='' or tblAccDetail.Currency='"+searchCurrency+"') ";
                            	}else{
                            		//其它外币时
                            		condition += " AND tblAccDetail.Currency='"+searchCurrency+"' ";
                            	}
                            }
							/**
							 * 查询本期借贷方合计
							 */
                            StringBuffer sql = new StringBuffer("SELECT tblAccMain.CredYear,tblAccMain.Period,tblAccDetail.AccCode,");
                            sql.append("SUM(tblAccDetail.DebitAmount) as sumDebitAmountBase,SUM(tblAccDetail.DebitCurrencyAmount) as sumDebitAmount,");
                            sql.append("SUM(tblAccDetail.LendAmount) as sumLendAmountBase,SUM(tblAccDetail.LendCurrencyAmount) as sumLendAmount,");
                            sql.append("SUM(tblAccDetail.DebitAmount)-SUM(tblAccDetail.LendAmount) AS sumdisAmountBase,");
                            sql.append("SUM(tblAccDetail.DebitCurrencyAmount)-SUM(tblAccDetail.LendCurrencyAmount) AS sumdisAmount");
                            if("all".equals(currencyValue)){
								//所有币别多栏式时对币种进行分组
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
                            System.out.println("本期借贷sql:"+sql);
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
                            	
                            	//当选择的币别是——所有币别多栏式
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
                            	
                            	/* 当存在上一条记录时，金额处理 */
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
                             * 查询会计科目
                             */
                            sql = new StringBuffer("SELECT tblAccTypeInfo.AccNumber AS AccCode,tblLanguage.zh_cn as AccFullName");
                            sql.append(" FROM tblAccTypeInfo JOIN tblLanguage ON tblAccTypeInfo.AccFullName=tblLanguage.id ");
                            sql.append(" WHERE tblAccTypeInfo.classCode like (SELECT classCode FROM tblAccTypeInfo WHERE AccNumber='"+accCode+"')+'%'");
                            sql.append(" AND isnull(tblAccTypeInfo.isCalculate,'')=1 "+accStr);
                            
                            if(showBanAccTypeInfo == null || "".equals(showBanAccTypeInfo)){
								//不显示禁用科目
                            	sql.append(" AND tblAccTypeInfo.statusId=0 ");
							}
                            sql.append(scopeSql(mop, loginBean));
                            sql.append(" order by tblAccTypeInfo.AccNumber");
                            System.out.println("会计科目sql:"+sql.toString());
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
							 * 查询本会计科目的期初余额
							 */
							sql = new StringBuffer("SELECT tblAccDetail.AccCode,");
							if("all".equals(currencyValue)){
								//所有币别多栏式时对币种进行分组
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
                            	//所有币别多栏式
                            	sql.append(",tblAccDetail.currency");
                            }
                            sql.append(" ORDER BY tblAccDetail.AccCode");
							System.out.println("查询期初金额："+sql);
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
	                        	//当选择的币别是——所有币别多栏式
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
                            	/* 当存在上一条记录时，金额处理 */
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
                             * 查询满足条件的会计期间
                             */
                            sql = new StringBuffer("SELECT ap.AccYear,ap.AccPeriod,ap.periodEnd FROM tblAccPeriod ap WHERE ");
                            str = "CONVERT(varchar(7),CONVERT(datetime,CONVERT(VARCHAR,ap.AccYear)+'-'+CONVERT(VARCHAR,ap.AccPeriod)+'-01',120),120)";
                            sql.append(str+">='"+startTime+"' AND "+str+"<='"+endTime+"'");
    						sql.append(" ORDER BY ap.AccYear,ap.AccPeriod");
    						System.out.println("会计期间："+sql);
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
                            
							/* 保存数据 */
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
		
		/* 取数据 */
		Object[] obj = (Object[])result.retVal;
		HashMap curPeriodMap = (HashMap)obj[0];
		List accTypeInfoList = (ArrayList)obj[1];
		HashMap initMap = (HashMap)obj[2];
		List periodList = (ArrayList)obj[3];
		
		/**
		 * 数据整理
		 */
		
		List accList = new ArrayList();
		String strs = "sumDebitAmountBase;sumDebitAmount;sumLendAmountBase;sumLendAmount;sumdisAmountBase;sumdisAmount;";
		if("all".equals(currencyValue)){
			String[] strSingle = strs.split(";");
			for(int k=0;k<currList.size();k++){
				String[] currStr = (String[])currList.get(k);
				String curId = currStr[0];
				if("true".equals(currStr[2])){
					//本位币
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
			//循环会计期间
			for(int j = 0;j<periodList.size();j++){
				HashMap periodMap = (HashMap)periodList.get(j);
				HashMap currMap = (HashMap)curPeriodMap.get(map.get("AccCode")+"_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
				currMap = currMap==null ? new HashMap() : currMap;
				String[] lineStr = strs.split(";");
				if(j == 0){
					//附值给期初
					HashMap init = (HashMap)initMap.get("init_"+map.get("AccCode"));
					if(init!=null && init.size()>0){
						for(int k = 0;k<lineStr.length;k++){
							currMap.put("init_"+lineStr[k], init.get(lineStr[k]));
						}
					}
				}else{
					//取上一期间的期末余额做为本期的期初余额
					HashMap preMap = (HashMap)periodList.get(j-1);
					HashMap currPreMap = (HashMap)accMap.get(map.get("AccCode")+"_"+preMap.get("AccYear")+"_"+preMap.get("AccPeriod"));
					if(currPreMap!= null && currPreMap.size()>0){
						for(int k = 0;k<lineStr.length;k++){
							currMap.put("init_"+lineStr[k], currPreMap.get("end_"+lineStr[k]));
						}
					}
				}
				/* 得到本年累计 */
				for(int k = 0;k<lineStr.length;k++){
					/* 年累计（期初发生+本期累计） */
					String money = "0";
					Object o = currMap.get("init_"+lineStr[k]);
					if(o != null && !"".equals(o)){
						money = o.toString();
					}
					if(currMap != null && currMap.size()>0){
						//存在本期借贷金额
						o = currMap.get(lineStr[k]);
						if(o != null && !"".equals(o)){
							money = new BigDecimal(money).add(new BigDecimal(o.toString())).toString();
						}
					}
					currMap.put("year_"+lineStr[k], dealDataDouble(String.valueOf(money), BaseEnv.systemSet.get("DigitsAmount").getSetting(), ""));
				}
				
				/* 得到期末余额 （期初余额-本期发生额）*/
				for(int k = 0;k<lineStr.length;k++){
					String money = "0";
					Object o = currMap.get("init_"+lineStr[k]);
					if(o != null && !"".equals(o)){
						money = o.toString();
					}
					if(currMap != null && currMap.size()>0){
						//存在本期借贷金额
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
				
				//余额为零不显示
				if(takeBrowNo != null && !"".equals(takeBrowNo)){
					if(Double.valueOf(currMap.get("end_sumdisAmountBase").toString())==0){
						continue;
					}
				}
				//显示发生额为零的记录
				if(balanceAndTakeBrowNo == null || "".equals(balanceAndTakeBrowNo)){
					Object sumDebit = currMap.get("sumDebitAmountBase");
					Object sumLend = currMap.get("sumLendAmountBase");
					if(sumDebit == null && sumLend == null){
						continue;
					}
				}
				//******判断期末借贷方*****//
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
		
		
		/* 数据整理 */
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
//						//借方
//						currMap.put(sumStr[2], dealDataDouble(String.valueOf(money), BaseEnv.systemSet.get("DigitsAmount").getSetting(), "abs"));
//					}
//					if(Double.valueOf(money)<0){
//						//贷方
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
	 * 根据用户编号查询满足条件的用户id
	 * @param employeeStart				编号开始
	 * @param employeeEnd				编号结束
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
	 * 根据编号查询是否是核算项目（如核算部门，核算职员，核算仓库，核算客户，核算供应商等）
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
	 * 查询开账期间
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
	 * 查询是否是本位币（如果是返回'isBase',不然返回外币的id）
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
								//综合本位币
								result.setRetVal("");
							}else if("all".equals(id)){
								//所有币别多栏式
								result.setRetVal("all");
							}else if("currency".equals(id)){
								//所有币别
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
	 * 查询币种的名称
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
	 * 获取当前用户设置的会计科目管辖权限
	 * @param mop
	 * @param loginBean
	 */
	public String scopeSql(MOperation mop, LoginBean loginBean){
		
		/* 权限控制 */
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
		scopeRight.addAll(loginBean.getAllScopeRight());
		String scopeRightSql = DynDBManager.scopeRightHandler("tblAccTypeInfo", "TABLELIST", "", loginBean.getId(), scopeRight, "select * from tblAccTypeInfo where 1=1 ", "endClass","");
		scopeRightSql = scopeRightSql.substring(scopeRightSql.indexOf("where 1=1")+"where 1=1".length());
		return scopeRightSql;
	}
}
