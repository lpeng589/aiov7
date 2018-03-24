package com.menyi.aio.web.finance.voucher;

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
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.*;
import com.menyi.web.util.*;

/**
 * 凭证 数据库操作类
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2013-03-04
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class VoucherMgt extends AIODBManager {

	/**
	 * 查询现金流量主表项目
	 * @return
	 */
	public Result queryMItems(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select * from tblCashFlow where Number <=32 and isCatalog = 0");
							PreparedStatement st = conn.prepareStatement(sql.toString());
							
							ResultSet rs = st.executeQuery();
							List<String[]> list = new ArrayList<String[]>();
							while(rs.next()){
								String[] recordcomment=new String[2];
								recordcomment[0] = rs.getString("Number");
								recordcomment[1] = rs.getString("Name");
								list.add(recordcomment);
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryMItem:",ex) ;
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
	 * 查询现金流量附表项目
	 */
	public Result querySItems(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select * from tblCashFlow where Number >= 34 and isCatalog = 0");
							PreparedStatement st = conn.prepareStatement(sql.toString());
							
							ResultSet rs = st.executeQuery();
							List<String[]> list = new ArrayList<String[]>();
							while(rs.next()){
								String[] recordcomment=new String[2];
								recordcomment[0] = rs.getString("Number");
								recordcomment[1] = rs.getString("Name");
								list.add(recordcomment);
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryMItem:",ex) ;
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
	 * 查询摘要库
	 * @return
	 */
	public Result queryRecord(final String searchValue){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT ID,NAME,CREATETIME");
							sql.append(",CREATEBY FROM tblRecordComment");
							if(searchValue != null && !"".equals(searchValue)){
								sql.append(" WHERE NAME like ? ");
							}
							sql.append(" order by CREATETIME");
							PreparedStatement st = conn.prepareStatement(sql.toString());
							if(searchValue != null && !"".equals(searchValue)){
								st.setString(1, "%"+searchValue+"%");
							}
							ResultSet rs = st.executeQuery();
							List<String[]> list = new ArrayList<String[]>();
							while(rs.next()){
								String[] recordcomment=new String[2];
								recordcomment[0] = rs.getString("ID");
								recordcomment[1] = rs.getString("NAME");
								list.add(recordcomment);
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryRecord:",ex) ;
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
	 * 根据凭证字，会计期间查询当前最大的凭证号
	 * @param CredTypeID
	 * @param Period
	 * @return
	 */
	public Result queryMaxOrderNo(final String CredTypeID,final String billDate){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String[] date_sp = billDate.split("-");
							StringBuffer sql = new StringBuffer("select isnull(max(OrderNo),0)+1 as maxOrderNo from tblAccMain");
							sql.append(" where CredTypeID=? and CredYear=? and Period=? ");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, CredTypeID);
							ps.setInt(2, Integer.parseInt(date_sp[0]));
							ps.setInt(3, Integer.parseInt(date_sp[1]));
							ResultSet rs = ps.executeQuery();
							Integer str = 0;
							if(rs.next()){
								str = rs.getInt("maxOrderNo");
							}
							result.setRetVal(str);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryMaxOrderNo:",ex) ;
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
	 * 根据Id查询摘要
	 * @param id
	 * @return
	 */
	public Result queryRecordId(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT name");
							sql.append(" FROM tblRecordComment where id='"+id+"'");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							String name = "";
							if(rs.next()){
								name = rs.getString("name");
							}
							result.retVal = name;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryRecordId:",ex) ;
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
	 * 添加摘要库
	 * @return
	 */
	public Result addRecord(final String value, final String userId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("INSERT tblRecordComment(ID,NAME,CREATETIME");
							sql.append(",CREATEBY) VALUES(?,?,?,?)");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, IDGenerater.getId());
							ps.setString(2, GlobalsTool.toChinseChar(value));
							ps.setString(3, BaseDateFormat.format(new Date(),
									BaseDateFormat.yyyyMMddHHmmss));
							ps.setString(4, userId);
							result.retVal = ps.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt addRecord:",ex) ;
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
	 * 删除摘要
	 * @return
	 */
	public Result delRecord(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("DELETE tblRecordComment ");
							sql.append(" WHERE ID=?");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, id);
							result.retVal = ps.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt delRecord:",ex) ;
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
	 * 查询会计科目
	 * @param classCode
	 * @param keywordSearch
	 * @param accCodeSearch
	 * @param accNameSearch
	 * @param accFullNameSearch
	 * @param allScopeRightList   控制分类权限
	 * @return
	 */
	public Result queryAcc(final String classCode,final String keywordSearch,
			final String accCodeSearch,final String accNameSearch,final String accFullNameSearch,final ArrayList allScopeRightList){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sqlStr = "SELECT '%'+currencyName+'%' as currency FROM tblCurrency";
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sqlStr);
							String str = "";
							while(rs.next()){
								str += "'"+rs.getString("currency")+"';";
							}
							
							StringBuffer sql = new StringBuffer("select a.AccNumber,l.zh_CN as AccName,language.zh_CN as AccFullName,PyCode,a.classCode,");
							sql.append("(SELECT COUNT(0) FROM tblAccTypeInfo acc LEFT JOIN tblLanguage la ON la.id=acc.AccName WHERE (acc.statusId!='-1' and ");
							sql.append("(acc.ClientCode IS NULL or acc.ClientCode='') and(acc.DepartmentCode IS NULL or acc.DepartmentCode='')");
							sql.append(" and (acc.EmployeeID IS NULL or acc.EmployeeID='') and (acc.StockCode IS NULL or acc.StockCode='')");
							if(str.length()>0){
								String[] array = str.split(";");
								for(String s : array){
									if(s != ""){
										sql.append(" and la.zh_CN not like "+s+"");
									}
								}
							}
							sql.append(" and (acc.SuplierCode IS NULL or acc.SuplierCode='')) and acc.classCode like a.classCode+'_____') AS counts");
							sql.append(" from tblAccTypeInfo a LEFT JOIN tblLanguage l ON l.id=a.AccName ");
							sql.append("left JOIN tblLanguage language ON language.id=a.AccFullName where 1=1 and (a.statusId!='-1' and (a.ClientCode IS NULL or a.ClientCode='')");
							sql.append(" and (a.DepartmentCode IS NULL or a.DepartmentCode='') and (a.EmployeeID IS NULL or a.EmployeeID='') ");
							if(str.length()>0){
								String[] array = str.split(";");
								for(String s : array){
									if(s != ""){
										sql.append(" and l.zh_CN not like "+s+"");
									}
								}
							}
							sql.append(" and (a.StockCode IS NULL or a.StockCode='') and (a.SuplierCode IS NULL or a.SuplierCode='')) ");
							
							/* 会计科目控制分类权限控制*/
							String consql = "";
							if(allScopeRightList != null && allScopeRightList.size()>0){
								for(int i=0;i<allScopeRightList.size();i++){
									LoginScopeBean loginScope = (LoginScopeBean)allScopeRightList.get(i);
									if(loginScope!=null && "tblAccTypeInfo".equals(loginScope.getTableName())){
										//存在设置可以管理的科目
										String scopes = loginScope.getScopeValue();
										String[] value = scopes.split(";");
										for(String strvalue : value){
											if(!"".equals(strvalue)){
												consql += " classCode like '"+strvalue;
												if(classCode!=null && "".equals(classCode)){
													consql += "%";
												}
												consql += "' or ";
											}
										}
									}
								}
							}
							boolean flag = false;
//							if(classCode==null){
//								if(consql.length()>0){
//									flag = true;
//								}else{
//									sql.append(" and len(classCode)=5");
//								}
//							}else if(!"".equals(classCode)){
//								sql.append("and classCode like '"+classCode+"_____'");
//							}else{
//								flag = true;
//							}
							if(consql.length()>0){
								consql = " and ("+consql.substring(0,consql.length()-3)+")";
							}
							//if(flag){
								sql.append(consql);
							//}
							
							/* 关键字查询*/
							if(keywordSearch!=null && !"".equals(keywordSearch)){
								sql.append(" and (AccNumber like '%"+keywordSearch+"%' or l.zh_CN like '%"+keywordSearch+"%' or AccFullName like '%"+keywordSearch+"%')");
							}
							/* 会计科目*/
							if(accCodeSearch!=null && !"".equals(accCodeSearch)){
								sql.append(" and AccNumber='"+accCodeSearch+"'");
							}
							/* 科目名称*/
							if(accNameSearch!=null && !"".equals(accNameSearch)){
								sql.append(" and l.zh_CN like '%"+accNameSearch+"%'");
							}
							/* 科目全称*/
							if(accFullNameSearch!=null && !"".equals(accFullNameSearch)){
								sql.append(" and language.zh_CN like '%"+accFullNameSearch+"%'");
							}
							
							sql.append(" and statusId=0 order by a.classCode,l.zh_CN");
							System.out.println(sql.toString());
							rs = st.executeQuery(sql.toString());
							List list = new ArrayList();
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
							result.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryAcc:",ex) ;
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
	 * 查询会计科目的核算币种
	 * @return
	 */
	public Result queryCurrency(final String value,final String queryMode){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
//							StringBuffer sql = new StringBuffer("SELECT Acc.Currency as CurrencyID,ACC.IsForCur FROM tblAccTypeInfo ACC");
//							sql.append(" left join tblCurrency C on C.id=ACC.Currency ");
//							if(queryMode != null && "self".equals(queryMode)){
//								sql.append(" where ACC.classCode=(select classCode from tblAccTypeInfo where AccNumber='"+value+"')");
//							}else if(queryMode != null && "super".equals(queryMode)){
//								sql.append(" where ACC.classCode=(select CASE WHEN len(classCode)>5 THEN substring(classCode,1,len(classCode)-5)");
//								sql.append(" else classCode end from tblAccTypeInfo where AccNumber='"+value+"')");
//							}else{
//								sql.append(" where 1=2");
//							}
							StringBuffer sql = new StringBuffer("select classCode,ISNULL(isCalculate,2) as isCalculate from tblAccTypeInfo");
							sql.append(" where AccNumber='"+value+"'");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ResultSet rs = ps.executeQuery();
							String classCode = "";
							if(rs.next()){
								String isCalculate = rs.getString("isCalculate");
								int calculate = 2;
								if(!"".equals(isCalculate)){
									calculate = Integer.parseInt(isCalculate);
								}
								classCode = rs.getString("classCode");
								if(calculate == 1){
									//核算项
									classCode = classCode.substring(0,classCode.length()-5);
								}
							}
							sql = new StringBuffer("SELECT Acc.Currency as CurrencyID,ACC.IsForCur FROM tblAccTypeInfo ACC left join tblCurrency C on C.id=ACC.Currency");
							sql.append(" where ACC.classCode='"+classCode+"'");
							ps = conn.prepareStatement(sql.toString());
							rs = ps.executeQuery();
							String currencyId = null;
							Integer isForCur = null;
							if(rs.next()){
								currencyId = rs.getString("CurrencyID");
								isForCur = rs.getInt("IsForCur");
							}
							rs.close();
							ps.close();
							sql = new StringBuffer("");
							if(currencyId == null){
								//不存在记录 核算外币未启用
								result.retVal = "[]";
								return;
							}else if(isForCur==1 && "".equals(currencyId)){
								//启用外币核算 核算币种为空 查询所有的币种
								sql.append("SELECT ID,CURRENCYNAME FROM tblCurrency where IsBaseCurrency!=1 order by IsBaseCurrency");
							}else if(!"".equals(currencyId)){
								//核算币种不为空 根据currencyId查询数据
								sql.append("SELECT ID,CURRENCYNAME FROM tblCurrency where id in ('"+currencyId+"')");
							}
							//存在SQL语句
							if(sql.length()>0){
								ps = conn.prepareStatement(sql.toString());
								rs = ps.executeQuery();
								String strJson = "[";
								while(rs.next()){
									strJson += "{\"id\":\""+rs.getString("ID")+"\",";
									strJson += "\"value\":\""+rs.getString("CURRENCYNAME")+"\"},";
								}
								if(",".equals(strJson.substring(strJson.length()-1))){
									strJson = strJson.substring(0,strJson.length()-1);
								}
								strJson += "]";
								result.retVal = strJson;
							}else{
								result.retVal = "[]";
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryCurrency:",ex) ;
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
	 * 查询会计科目的核算种类
	 * @return
	 */
	public Result queryIsBreed(final String value,final String queryMode){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT ACC.IsDept,ACC.IsPersonal,ACC.IsCash,");
							sql.append("ACC.IsClient,ACC.IsProvider,ACC.IsProject,ACC.isStock,ACC.AccNumber FROM tblAccTypeInfo ACC");
							//不管queryMode如何，只要是科目是核算项目，则查上级
							sql.append(" where ACC.AccNumber in ( case (select isCalculate from tblAccTypeInfo where AccNumber='"+value+"') when 1 then '"+(value.length()>5? value.substring(0,value.length()-5):value)+"'  else '"+value+"' end  ) ");
							
							/*if(queryMode != null && "self".equals(queryMode)){
								sql.append(" WHERE ACC.classCode=(select classCode from tblAccTypeInfo where AccNumber='"+value+"')");
							}else if(queryMode != null && "super".equals(queryMode)){
								sql.append(" where ACC.classCode=(select CASE WHEN len(classCode)>5 THEN substring(classCode,1,len(classCode)-5)");
								sql.append(" else classCode end from tblAccTypeInfo where AccNumber='"+value+"')"); 
							}else{
								sql.append(" where 1=2");
							}*/
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ResultSet rs = ps.executeQuery();
							String strJson = "[";
							if(rs.next()){
								strJson += "{\"IsDept\":\""+rs.getInt("IsDept")+"\",";
								strJson += "\"IsPersonal\":\""+rs.getInt("IsPersonal")+"\",";
								strJson += "\"IsCash\":\""+rs.getInt("IsCash")+"\",";
								strJson += "\"IsClient\":\""+rs.getInt("IsClient")+"\",";
								strJson += "\"IsProvider\":\""+rs.getInt("IsProvider")+"\",";
								strJson += "\"IsProject\":\""+rs.getInt("IsProject")+"\",";
								String AccNumber = rs.getString("AccNumber");
								if(AccNumber.length()>=4){
									//String str = AccNumber.substring(0, 4);
									//if("1122".equals(str) || "1123".equals(str) 
									//		|| "2202".equals(str) || "2203".equals(str)){
										/* 需要输入往来单位*/
										//strJson += "\"IsCome\":\"1\",";
									//}else{
										strJson += "\"IsCome\":\"0\",";
									//}
								}else{
									strJson += "\"IsCome\":\"0\",";
								}
								strJson += "\"isStock\":\""+rs.getInt("isStock")+"\"}";
							}
							strJson += "]";
							result.retVal = strJson;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryCurrency:",ex) ;
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
	 * 查询币种查询汇率
	 * @return
	 */
	public Result queryExchange(final String value, final AccPeriodBean accPeriodBean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT isnull(RECORDEXCHANGE,1) as RECORDEXCHANGE FROM ");
							sql.append("tblSetExchange WHERE Currency=? AND Period=? AND PeriodYear=?");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, value);
							ps.setInt(2, accPeriodBean.getAccPeriod());
							ps.setInt(3, accPeriodBean.getAccYear());
							ResultSet rs = ps.executeQuery();
							Double str = null;
							if(rs.next()){
								str = rs.getDouble("RECORDEXCHANGE");
							}
							result.retVal = str;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryCurrency:",ex) ;
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
	 * 添加凭证
	 * @param tableName
	 * @param allTables
	 * @param values
	 * @param id
	 * @param userId
	 * @param defineInfo
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result addAccMain(final String tableName,final Hashtable allTables,
		final HashMap values,final String id,final LoginBean loginBean,final String defineInfo,
		final MessageResources resources,final Locale locale,final String saveType){
		final Result rs = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
			    	session.doWork(new Work() {
			        	public void execute(Connection conn) throws SQLException {
			        		try{
			        			Result result = new Result();
				        		//调用define文件来验证 添加前验证
			        			if(!"printSave".equals(saveType)){
			        				/* 打印不验证*/
			        				result = new DynDBManager().defineSql(conn, "add", false,tableName,(Hashtable)allTables, values,id, loginBean.getId(),defineInfo,resources,locale);
			        				if (result.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
						            	rs.setRetCode(result.getRetCode());
						            	rs.setRetVal(result.getRetVal());
						                BaseEnv.log.error("VoucherMgt Before add defineSql Error code = " 
						                		+result.getRetCode() + " Msg=" + result.getRetVal());
						                return;
						            }
			        			}
				            	//插入主表数据
				            	Result ires = new DynDBManager().execInert(conn, tableName, allTables, values, "", resources, locale);
				            	if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				                    BaseEnv.log.error("Insert Table " + tableName +
				                                      " Info: " +
				                                      ires.getRetVal());
				                    rs.setRetCode(ires.getRetCode());
				                    rs.setRetVal(ires.getRetVal());
				                }else{
				                	ArrayList childTableList = DDLOperation.getChildTables(tableName,allTables);
									for (int i = 0; i < childTableList.size(); i++) {
									     DBTableInfoBean childTb = (DBTableInfoBean)childTableList.get(i);
									    ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
										if (childList == null) {
										    continue;
										}
										StringBuffer procstr = new StringBuffer("{call proc_insertAccDetail(@from=?,@where=?,");
										procstr.append("@CompanyCode=?,@DepartmentCode=?,@ProjectCode=?,@EmployeeID=?,@StockCode=?,");
										procstr.append("@id=?,@AccCode=?,@RefBillID=?,@RefBillType=?,@DebitAmount=?,@LendAmount=?,");
										procstr.append("@Currency=?,@CurrencyRate=?,@LendCurrencyAmount=?,@DebitCurrencyAmount=?,");
										procstr.append("@PeriodYear=?,@PeriodMonth=?,@AccDate=?,@createBy=?,@createTime=?,");
										procstr.append("@lastUpdateBy=?,@lastUpdateTime=?,@SCompanyID=?,@RecordComment=?,");
										procstr.append("@RecordOrder=?,@mainId=?,@RowNum=?,@Refs=?,@MainItem=?,@SecItem=?,@retCode=?,@retValue=?)}");
										CallableStatement cs = conn.prepareCall(procstr.toString()) ;
										for (int j = 0; j < childList.size(); j++) {
										     HashMap childMap = (HashMap) childList.get(j);
										     if(childMap.get("AccCode") != null && !"".equals(childMap.get("AccCode"))){
											     childMap.put("id", IDGenerater.getId());
												 childMap.put("f_ref", id);
												 childMap.put("PeriodYear", values.get("CredYear"));
												 childMap.put("PeriodMonth", values.get("CredMonth"));
												 childMap.put("RefBillType", tableName);
												 //转换特殊字符
												 Result sp_values =  GlobalsTool.conversionSpecialCharacter(childTb.getFieldInfos(),childMap) ;
												 childMap = (HashMap) sp_values.getRetVal() ;
												 
												//执行从表插入
												cs.setString(1, null);
												cs.setString(2, null);
												cs.setString(3, String.valueOf(childMap.get("CompanyCode")));
												cs.setString(4, String.valueOf(childMap.get("DepartmentCode")));
												cs.setString(5, String.valueOf(childMap.get("ProjectCode")==null?"":childMap.get("ProjectCode")));
												cs.setString(6, String.valueOf(childMap.get("EmployeeID")));
												cs.setString(7, String.valueOf(childMap.get("StockCode")));
												cs.setString(8, String.valueOf(childMap.get("id")));
												cs.setString(9, String.valueOf(childMap.get("AccCode")));
												String Acccode = String.valueOf(childMap.get("AccCode"));
												Result rss = queryAccTypeInfo(Acccode, conn);
												Acccode = (String)rss.retVal;
												cs.setString(9, Acccode);
												cs.setString(10, String.valueOf(childMap.get("RefBillID")));
												cs.setString(11, String.valueOf(childMap.get("RefBillType")));
												cs.setString(12, String.valueOf(childMap.get("DebitAmount")));
												cs.setString(13, String.valueOf(childMap.get("LendAmount")));
												cs.setString(14, String.valueOf(childMap.get("Currency")==null?"":childMap.get("Currency")));
												cs.setString(15, String.valueOf(childMap.get("CurrencyRate")));
												cs.setString(16, String.valueOf(childMap.get("LendCurrencyAmount")));
												cs.setString(17, String.valueOf(childMap.get("DebitCurrencyAmount")));
												cs.setString(18, String.valueOf(childMap.get("PeriodYear")));
												cs.setString(19, String.valueOf(childMap.get("PeriodMonth")));
												cs.setString(20, String.valueOf(childMap.get("AccDate")));
												cs.setString(21, String.valueOf(values.get("createBy")));
												cs.setString(22, String.valueOf(values.get("createTime")));
												cs.setString(23, String.valueOf(values.get("lastUpdateBy")));
												cs.setString(24, String.valueOf(values.get("lastUpdateTime")));
												cs.setString(25, String.valueOf(childMap.get("SCompanyID")));
												cs.setString(26, String.valueOf(childMap.get("RecordComment")));
												cs.setString(27, String.valueOf(childMap.get("RecordOrder")));
												cs.setString(28, String.valueOf(childMap.get("f_ref")));
												
												//*****添加流量单字段****//
												cs.setString(29, String.valueOf(childMap.get("RowNum")));
												cs.setString(30, String.valueOf(childMap.get("Refs")));
												cs.setString(31, String.valueOf(childMap.get("MainItem")));
												cs.setString(32, String.valueOf(childMap.get("SecItem")));
												
												cs.registerOutParameter(33, Types.INTEGER);
												cs.registerOutParameter(34, Types.VARCHAR);
						                        cs.execute();
						                        rs.setRetCode(cs.getInt(33));
								                rs.setRetVal(cs.getString(34));
						                        if (rs.getRetCode() !=
							                        ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {                    	
							                        BaseEnv.log.debug("VoucherMgt.addAccMain Insert Table " + childTb.getTableName() + " Error Info: " + cs.getString(34));
							                        rs.setRetCode(cs.getInt(33));
							                        rs.setRetVal(cs.getString(34));
							                        return;
							                    }
										     }
										}
									}
									if(!"printSave".equals(saveType)){
										result = new DynDBManager().defineSql(conn, "add", true, tableName,(Hashtable) allTables, values,id, loginBean.getId(),defineInfo,resources,locale);
							            if (result.getRetCode() <
							                ErrorCanst.DEFAULT_SUCCESS) {
							                rs.setRetCode(result.getRetCode());
							                rs.setRetVal(result.getRetVal());
							                BaseEnv.log.error(
							                        "VoucherMgt After addAccMain defineSql Error code = " +
							                        result.getRetCode() + " Msg=" +
							                        result.getRetVal());
							                return;
							            }
									}
									/*判断是否启用标准财务和是否不启用审核自动审核*/
									//String standards = new GlobalsTool().getSysSetting("standardAcc");
									//if("false".equals(standards)){
										//不启用标准财务
										String sql = "select IsAuditing from tblAccMainSetting";
										Statement st = conn.createStatement();
										ResultSet rset = st.executeQuery(sql);
										int isAuditing = 0;
										if(rset.next()){
											isAuditing = rset.getInt("IsAuditing");
										}
										/* 不启用审核*/
										if(isAuditing==0){
											//过账
											dealbind(conn, locale, loginBean, values, resources);
										}
									//}
				                }
			        		}catch (Exception ex) {
					                rs.setRetCode(ErrorCanst.
					                              DEFAULT_FAILURE);
					                BaseEnv.log.error("VoucherMgt addAccMain data Error :", ex);
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
	 * 添加和修改处理过账
	 * @param conn
	 * @param locale
	 * @param loginBean
	 * @param values
	 * @param resources
	 * @return
	 */
	public Result dealbind(Connection conn, Locale locale,LoginBean loginBean,HashMap values,MessageResources resources){
		Result result = new Result();
		try {
			String dateTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
			/* 处理信息*/
			String isAuStr = GlobalsTool.getMessage(locale,"oa.job.approved")+" "+loginBean.getEmpFullName()+" "+dateTime+"<br />";
			String ispostStr = GlobalsTool.getMessage(locale,"muduleFlow.lb.voucher")+" "+loginBean.getEmpFullName()+" "+dateTime+"<br />";
			Statement st = conn.createStatement();
			ResultSet rset = null;
			boolean flagid = false;
			//处理凭证时进行验证
			String sql = "select tblAccMain.id,";
			sql += "acctype.cashClass,acctype.bankClass,acctype.equivalentClass from tblAccMain join ";
			sql += "tblAccDetail on tblAccMain.id=tblAccDetail.f_ref JOIN tblAccTypeInfo acctype ";
			sql += "ON acctype.AccNumber=tblAccDetail.AccCode where tblAccMain.id='"+values.get("id")+"'";
			rset = st.executeQuery(sql);
			if(rset.next()){
				Integer cashClass = rset.getInt("cashClass");   				//现金科目类
				Integer bankClass = rset.getInt("bankClass");   				//银行科目类
				Integer equivalentClass = rset.getInt("equivalentClass");   	//现金等价物类
				//复核
				if(cashClass == 1 || bankClass == 1 || equivalentClass == 1){
					//科目启用现金科目类,银行科目类,现金等价物类之一可以复核和反复核
					flagid = true;
				}
			}
			String checkStr = "";
			if(flagid){
				checkStr = GlobalsTool.getMessage(locale,"ReportBillView.button.Check")+" "+loginBean.getEmpFullName()+" "+dateTime+"<br />";
			}
			/* 修改为已审核等*/
			sql = "update tblAccMain set workflowNodeName='finish',isAuditing='finish',workFlowNode='-1',";
			sql += "approveRemark=isnull(approveRemark,'')+'"+isAuStr+ispostStr+checkStr+"'";
			if(flagid){
				sql += ",isReview=2,ReviewUser='"+loginBean.getId()+"'";
			}
			sql += ",approver='"+loginBean.getId()+"',postingUser='"+loginBean.getId()+"' where id='"+values.get("id")+"'";
			st.executeUpdate(sql);
			DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("tblAccMain_Add_One");
			
			//添加数据到map中为
			HashMap<String,String> hashmap = new HashMap<String,String>();
			hashmap.put("tblAccMain_BillDate", String.valueOf(values.get("BillDate")));
			hashmap.put("tblAccMain_id", String.valueOf(values.get("id")));
			hashmap.put("tblAccMain_createBy", String.valueOf(values.get("createBy")));
			hashmap.put("tblAccMain_createTime", String.valueOf(values.get("createTime")));
			hashmap.put("tblAccMain_lastUpdateBy", String.valueOf(values.get("lastUpdateBy")));
			hashmap.put("tblAccMain_lastUpdateTime", String.valueOf(values.get("lastUpdateTime")));
			hashmap.put("tblAccMain_SCompanyID", String.valueOf(values.get("SCompanyID")));
			result = defineSqlBean.execute(conn, hashmap, loginBean.getId(),resources,locale,"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 修改凭证
	 * @param tableName
	 * @param allTables
	 * @param values
	 * @param id
	 * @param userId
	 * @param defineInfo
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result updateAccMain(final String tableName,final Hashtable allTables,
		final HashMap values,final LoginBean loginBean,final String defineInfo,
		final MessageResources resources,final Locale locale){
		final Result rs = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
			    	session.doWork(new Work() {
			        	public void execute(Connection conn) throws SQLException {
			        		try{
			        			Result result = new Result();
			        			int isAuditing = 1;
			        			/*判断是否启用标准财务和是否不启用审核自动审核*/
								String sql = "select IsAuditing from tblAccMainSetting";
								Statement st = conn.createStatement();
								ResultSet rset = st.executeQuery(sql);
								if(rset.next()){
									isAuditing = rset.getInt("IsAuditing");
								}
								/* 不启用审核*/
								if(isAuditing==0){
									/* 先反审核*/
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("tblAccMain_Del_One");
									//添加数据到map中为
									sql = "SELECT BillDate,id,createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,isnull(workFlowNodeName,'') as workFlowNodeName from tblAccMain where workFlowNodeName='finish' and id='"+values.get("id")+"'";
									rset = st.executeQuery(sql);
									if(rset.next()){
										/* 已过账的凭证 */
										if("finish".equals(rset.getString("workFlowNodeName"))){
											HashMap<String,String> hashmap = new HashMap<String,String>();
											hashmap.put("tblAccMain_BillDate", rset.getString("BillDate"));
											hashmap.put("tblAccMain_id", rset.getString("id"));
											hashmap.put("tblAccMain_createBy", rset.getString("createBy"));
											hashmap.put("tblAccMain_createTime", rset.getString("createTime"));
											hashmap.put("tblAccMain_lastUpdateBy", rset.getString("lastUpdateBy"));
											hashmap.put("tblAccMain_lastUpdateTime", rset.getString("lastUpdateTime"));
											hashmap.put("tblAccMain_SCompanyID", rset.getString("SCompanyID"));
											result = defineSqlBean.execute(conn, hashmap, loginBean.getId(),resources,locale,"");
					                        if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
					                        	rs.setRetCode(result.getRetCode());
								                rs.setRetVal(result.getRetVal());
					                        	return ;
					                        }
										}
									}
								}else{
									/* 调用define文件来验证 修改前验证*/ 
				        			result = new DynDBManager().defineSql(conn, "update", false,tableName,(Hashtable)allTables, values,values.get("id").toString(), loginBean.getId(),defineInfo,resources,locale);
				        			if (result.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
							        	rs.setRetCode(result.getRetCode());
							        	rs.setRetVal(result.getRetVal());
							        	BaseEnv.log.error("VoucherMgt Before update defineSql Error code = " 
							                		+result.getRetCode() + " Msg=" + result.getRetVal());
							                return;
							        }
								}
								final Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(loginBean.getId()) ;
				            	//修改主表数据
				            	Result ires = new DynDBManager().execUpdate(conn, tableName, allTables,values,sessionSet);
				            	if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				                    BaseEnv.log.debug("Update Table " + tableName +
				                                      " Info: " +
				                                      ires.getRetVal());
				                    rs.setRetCode(ires.getRetCode());
				                    rs.setRetVal(ires.getRetVal());
				                }else{
				                	ArrayList childTableList = DDLOperation.getChildTables(tableName,allTables);
									for (int i = 0; i < childTableList.size(); i++) {
									     DBTableInfoBean childTb = (DBTableInfoBean)childTableList.get(i);
									    ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
										if (childList == null) {
										    continue;
										}
										
										String insertTableName = childTb.getTableName();
										//删除多语言
										KRLanguageQuery.delete(conn,childTb,values.get("id").toString(),"f_ref");
			                            //先删除从表
			                            ires = new DynDBManager().execDelete(conn, insertTableName,values.get("id").toString(),childList);
			                            if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			                                BaseEnv.log.debug("Delete child Table " + insertTableName + " Info: " + ires.getRetVal());
			                                rs.setRetCode(ires.getRetCode());
			                                rs.setRetVal(ires.getRetVal());
			                                return;
			                            }
			                            
										StringBuffer procstr = new StringBuffer("{call proc_insertAccDetail(@from=?,@where=?,");
										procstr.append("@CompanyCode=?,@DepartmentCode=?,@ProjectCode=?,@EmployeeID=?,@StockCode=?,");
										procstr.append("@id=?,@AccCode=?,@RefBillID=?,@RefBillType=?,@DebitAmount=?,@LendAmount=?,");
										procstr.append("@Currency=?,@CurrencyRate=?,@LendCurrencyAmount=?,@DebitCurrencyAmount=?,");
										procstr.append("@PeriodYear=?,@PeriodMonth=?,@AccDate=?,@createBy=?,@createTime=?,");
										procstr.append("@lastUpdateBy=?,@lastUpdateTime=?,@SCompanyID=?,@RecordComment=?,");
										procstr.append("@RecordOrder=?,@mainId=?,@RowNum=?,@Refs=?,@MainItem=?,@SecItem=?,@retCode=?,@retValue=?)}");
										CallableStatement cs = conn.prepareCall(procstr.toString()) ;
										for (int j = 0; j < childList.size(); j++) {
										     HashMap childMap = (HashMap) childList.get(j);
										     if(childMap.get("AccCode") != null && !"".equals(childMap.get("AccCode"))){
											 	childMap.put("id", IDGenerater.getId());
												childMap.put("f_ref", values.get("id").toString());
												childMap.put("PeriodYear", values.get("CredYear"));
												childMap.put("PeriodMonth", values.get("CredMonth"));
												if(values.get("RefBillType") != null){
													childMap.put("RefBillType", values.get("RefBillType"));
												} else{
													childMap.put("RefBillType", tableName);
												}
																									
												//转换特殊字符
												Result sp_values =  GlobalsTool.conversionSpecialCharacter(childTb.getFieldInfos(),childMap) ;
												childMap = (HashMap) sp_values.getRetVal() ;
												
												//执行从表插入
												cs.setString(1, null);
												cs.setString(2, null);
												cs.setString(3, String.valueOf(childMap.get("CompanyCode")));
												cs.setString(4, String.valueOf(childMap.get("DepartmentCode")));
												cs.setString(5, String.valueOf(childMap.get("ProjectCode")==null?"":childMap.get("ProjectCode")));
												cs.setString(6, String.valueOf(childMap.get("EmployeeID")));
												cs.setString(7, String.valueOf(childMap.get("StockCode")));
												cs.setString(8, String.valueOf(childMap.get("id")));
												String Acccode=String.valueOf(childMap.get("AccCode"));
												Result rss = queryAccTypeInfo(Acccode, conn);
												Acccode = (String)rss.retVal;
												cs.setString(9, Acccode);
												cs.setString(10, String.valueOf(childMap.get("RefBillID")));
												cs.setString(11, String.valueOf(childMap.get("RefBillType")));
												cs.setString(12, String.valueOf(childMap.get("DebitAmount")));
												cs.setString(13, String.valueOf(childMap.get("LendAmount")));
												cs.setString(14, String.valueOf(childMap.get("Currency")==null?"":childMap.get("Currency")));
												cs.setString(15, String.valueOf(childMap.get("CurrencyRate")));
												cs.setString(16, String.valueOf(childMap.get("LendCurrencyAmount")));
												cs.setString(17, String.valueOf(childMap.get("DebitCurrencyAmount")));
												cs.setString(18, String.valueOf(childMap.get("PeriodYear")));
												cs.setString(19, String.valueOf(childMap.get("PeriodMonth")));
												cs.setString(20, String.valueOf(childMap.get("AccDate")));
												cs.setString(21, String.valueOf(values.get("createBy")));
												cs.setString(22, String.valueOf(values.get("createTime")));
												cs.setString(23, String.valueOf(values.get("lastUpdateBy")));
												cs.setString(24, String.valueOf(values.get("lastUpdateTime")));
												cs.setString(25, String.valueOf(childMap.get("SCompanyID")));
												cs.setString(26, String.valueOf(childMap.get("RecordComment")));
												cs.setString(27, String.valueOf(childMap.get("RecordOrder")));
												cs.setString(28, String.valueOf(childMap.get("f_ref")));
												
												//*****添加流量单字段****//
												cs.setString(29, String.valueOf(childMap.get("RowNum")));
												cs.setString(30, String.valueOf(childMap.get("Refs")));
												cs.setString(31, String.valueOf(childMap.get("MainItem")));
												cs.setString(32, String.valueOf(childMap.get("SecItem")));
												
												cs.registerOutParameter(33, Types.INTEGER);
												cs.registerOutParameter(34, Types.VARCHAR);
						                        cs.execute();
						                        
						                        //取到返回的数据
						                        rs.setRetCode(cs.getInt(33));
								                rs.setRetVal(cs.getString(34));
						                        if (rs.getRetCode() !=
							                        ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {
							                        BaseEnv.log.debug("Update Table " + childTb.getTableName() + " Info: " + cs.getString(34));
							                        rs.setRetCode(cs.getInt(33));
							                        rs.setRetVal(cs.getString(34));
							                        return;
							                    }
										     }
										}
									}
									
									/* 执行define文件验证 修改完后*/
									result = new DynDBManager().defineSql(conn, "update", true, tableName,(Hashtable) allTables, values,values.get("id").toString(), loginBean.getId(),defineInfo,resources,locale);
						            if (result.getRetCode() <
						                ErrorCanst.DEFAULT_SUCCESS) {
						                rs.setRetCode(result.getRetCode());
						                rs.setRetVal(result.getRetVal());
						                BaseEnv.log.error(
						                        "VoucherMgt After updateAccMain defineSql Error code = " +
						                        result.getRetCode() + " Msg=" +
						                        result.getRetVal());
						                return;
						            }
						            /* 不启用审核*/
									if(isAuditing==0){
										//过账
										dealbind(conn, locale, loginBean, values, resources);
									}
									
									/* 修改凭证号，使凭证号和原来保持一致 */
									//zxy 修改时要可以调整凭证号,
									//int orderNo = Integer.parseInt(values.get("oldOrderNo").toString());
									int orderNo = Integer.parseInt(values.get("OrderNo").toString());
									String oldcredTypeId = values.get("oldCredTypeID").toString();
									int credYear = Integer.parseInt(values.get("oldCredYear").toString());
									int period = Integer.parseInt(values.get("oldPeriod").toString());
									String credTypeId = values.get("CredTypeID").toString();
									String billDate = values.get("BillDate").toString();			//日期
									String[] billdates = billDate.split("-");
									
									/**
									 * 如果凭证字和会计期间年，会计期间相同时修改凭证号为添加时的
									 */
									System.out.println(oldcredTypeId+"==="+credTypeId);
									System.out.println(credYear+"==="+Integer.parseInt(billdates[0])+"====="+period+"==="+Integer.parseInt(billdates[1]));
									if(oldcredTypeId.equals(credTypeId) && credYear == Integer.parseInt(billdates[0]) 
											&& period == Integer.parseInt(billdates[1])){
										st = conn.createStatement();
										st.executeUpdate("update tblAccMain set orderNo="+orderNo+" where id='"+values.get("id")+"'");
									}
									
				                }
			        		}catch (Exception ex) {
					                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
					                BaseEnv.log.error("VoucherMgt updateAccMain data Error :", ex);
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
	 * 根据科目编号查询科目信息
	 * @param accCode
	 * @return
	 */
	public Result queryAccTypeInfo(final String accCode,final Connection conn){
		Result rs = new Result();
		String newaccCode = accCode;
		try {
			String sql = "select isnull(isCalculate,0) as isCalculate from tblAccTypeInfo where AccNumber=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, accCode);
			ResultSet reset = ps.executeQuery();
			if(reset.next()){
				int isCalculate = reset.getInt("isCalculate");
				if(isCalculate == 1){
					//核算类科目
					newaccCode = newaccCode.substring(0,newaccCode.length()-5);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.setRetVal(newaccCode);
		return rs;
	}
	
	/**
	 * 查询凭证列表
	 * @param searchForm
	 * @return
	 */
	public Result queryList(final VoucherSearchForm searchForm,final MOperation mop,final LoginBean loginBean){
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	Statement st;
                        ResultSet rs;
                        StringBuffer sql = new StringBuffer("select tblAccMain.id as id,isnull(tblAccMain.OrderNo,0) as OrderNo,");
                        sql.append("(case tblAccMain.CashFlag when '0' then '-' when '1' then '已指定' when '-1' then '未指定' end) as CashFlag,");
                        sql.append("row_number() over(order by tblAccMain.CredYear DESC,tblAccmain.Period DESC,tblAccMain.OrderNo DESC,d.detOrderNo) as row_id,");
                        sql.append("tblAccMain.BillDate as BillDate,tblAccMain.CredTypeID as CredTypeID,tblAccMain.CredTypeID+'_'+cast(tblAccMain.OrderNo as varchar(30)) as CredTypeIDs,tblAccMain.AcceNum as AcceNum,tblAccMain.RefBillType as RefBillType,");
                		sql.append("tblAccMain.CredNo as CredNo,tblAccMain.Period as Period,tblAccMain.EmployeeID as EmployeeID,tblAccMain.AcceptNumber as AcceptNumber,tblAccMain.AcceptMode as AcceptMode,");
                		sql.append("tblAccMain.isAuditing as isAuditing,tblAccMain.isReview as isReview,tblAccMain.RefBillID as RefBillID,tblAccMain.RefBillNo as RefBillNo,");
                		sql.append("tblAccMain.statusId as statusId,tblAccMain.CredYear as CredYear,tblAccMain.CredMonth as CredMonth,tblAccMain.workflowNodeName as workflowNodeName,");
                		sql.append("d.RefBillType as accDetailRefBillType,d.CompanyCode as CompanyCode,d.DepartmentCode as DepartmentCode,tblAccMain.workFlowNode as workFlowNode,");
                		sql.append("case tblAccMain.workFlowNodeName when 'finish' then '已过账' when 'notApprove' then '未过账' else '' end as workFlowNodeNames,");
                		sql.append("case tblAccMain.isReview when '1' then '未复核' when '2' then '已复核' end as isReviews,");
                		sql.append("case tblAccMain.isAuditing when 'finish' then '已审核' when 'noPass' then '审核不通过' when 'start' then '未审核' else '' end as isAuditings,");
                		sql.append("d.ProjectCode as ProjectCode,d.AccCode as AccCode,d.DebitAmount as DebitAmount,d.RecordComment as RecordComment,d.LendAmount as LendAmount,");
                		sql.append("d.DebitCurrencyAmount as DebitCurrencyAmount,d.LendCurrencyAmount as LendCurrencyAmount,d.Currency as Currency,c.Currencyname as Currencyname,");
                		sql.append("d.CurrencyRate as CurrencyRate,d.RecordOrder as RecordOrder,d.PeriodYear as PeriodYear,d.PeriodMonth as PeriodMonth,d.statusId as ");
                		sql.append("accDetailStatusId,d.AccDate as AccDate,d.SCompanyID as SCompanyID,d.EmployeeID as detailEmployeeID,d.StockCode as StockCode,l.zh_CN as AccName,");
                		sql.append("acctype.AccNumber as AccNumber,acctype.AccFullName as AccFullName,(select sum(DebitCurrencyAmount) from tblaccdetail where f_ref=tblAccMain.id) as CurNeedPayAmt, ");
                		sql.append("(SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(SELECT tblDbTableInfo.languageId FROM tblDbTableInfo WHERE tblDbTableInfo.tableName=tblAccMain.RefBillType)) as RefBillTypeName");
                		sql.append(",tblAccMain.createBy as createBy");
                		sql.append(" from tblAccMain tblAccMain join tblAccDetail d on d.f_ref =tblAccMain.id ");
                		sql.append("join tblAccTypeInfo acctype on acctype.AccNumber=d.AccCode ");
                		sql.append("left join tblLanguage l on l.id=acctype.AccFullName left join tblCurrency c ");
                		sql.append(" on c.id=d.Currency where isNull(tblAccMain.workFlowNodeName,'')!='draft' ");
                		sql.append(" and isNull(tblAccMain.workFlowNodeName,'')!='print' and isNull(tblAccMain.isAuditing,'')!='print'");
                		
                		String startTime = searchForm.getSearchStartTime();
                		String endTime = searchForm.getSearchEndTime();
                		String searchCredType = searchForm.getSearchCredType();
                		String searchOrderNoStart = searchForm.getSearchOrderNoStart();
                		String searchOrderNoEnd = searchForm.getSearchOrderNoEnd();
                		String searchCredYearStart = searchForm.getSearchCredYearStart();						//会计期间开始年
                		String searchCredMonthStart = searchForm.getSearchCredMonthStart();						//会计期间开始月
                		String searchCredYearEnd = searchForm.getSearchCredYearEnd();
                		String searchCredMonthEnd = searchForm.getSearchCredMonthEnd();
                		String searchAccCodeStart = searchForm.getSearchAccCodeStart();
                		String searchAccCodeEnd = searchForm.getSearchAccCodeEnd();
                		
                		String searchMoneyStart = searchForm.getSearchMoneyStart();
                		String searchMoneyEnd = searchForm.getSearchMoneyEnd();
                		
                		String searchRecordComment = searchForm.getSearchRecordComment();
                		String searchWName = searchForm.getSearchwName();
                		String searchReview = searchForm.getSearchIsReview();
                		String searchAuditing= searchForm.getSearchIsAuditing();
                		String searchRefBillNo = searchForm.getSearchRefBillNo();
                		String depts = searchForm.getSearchDept();
                		String deptName = searchForm.getSearchDeptName();
                		String employees = searchForm.getSearchEmployee();
                		String employeesName = searchForm.getSearchEmployeeName();
                		String searchClient = searchForm.getSearchClient();
                		String searchClientName = searchForm.getSearchClientName();
                		String searchRefBillType = searchForm.getSearchRefBillType();			//单据类型
                		String createByName=searchForm.getTblEmployee_EmpFullName();
                		String createBy=searchForm.getCreateBy();
                		
                		String searchAccType = searchForm.getSearchAccType();
                		
                		//查询条件
                		
                		/*凭证类型筛选*/
                		if(searchAccType != null && "ref".equals(searchAccType)){
                			sql.append(" and tblAccMain.RefBillNo != '' ");
                		}
                		
                		if(searchAccType != null && "manual".equals(searchAccType)){
                			sql.append(" and (tblAccMain.RefBillNo = '' or tblAccMain.RefBillNo is null) ");
                		}
                		
                		/* 单据时间检索*/
                		if(startTime != null && !"".equals(startTime)){
                			sql.append(" and tblAccMain.BillDate>='"+startTime+"'");
            		    }
                		if(endTime != null && !"".equals(endTime)){
                			sql.append(" and tblAccMain.BillDate<='"+endTime+"'");
                		}
                		
                		/* 凭证字*/
                		if(searchCredType != null && !"".equals(searchCredType)){ 
                			sql.append(" and tblAccMain.CredTypeID='"+searchCredType+"'");
                		}
                		/* 单据编号 */
                		if(searchRefBillNo != null && !"".equals(searchRefBillNo)){
                			sql.append(" and tblAccMain.RefBillNo like '%"+searchRefBillNo.trim()+"%'");
                		}
                		
                		/* 凭证号*/
                		if(searchOrderNoStart != null && !"".equals(searchOrderNoStart)){
                			sql.append(" and tblAccMain.OrderNo>="+searchOrderNoStart+"");
                		}
                		if(searchOrderNoEnd != null && !"".equals(searchOrderNoEnd)){
                			sql.append(" and tblAccMain.OrderNo<="+searchOrderNoEnd+"");
                		}
                		
                		/* 会计期间*/
                		if(searchCredYearStart != null && !"".equals(searchCredYearStart)){
                			sql.append(" and tblAccMain.credYear>="+searchCredYearStart+"");
                		}
                		if(searchCredYearEnd != null && !"".equals(searchCredYearEnd)){
                			sql.append(" and tblAccMain.credYear<="+searchCredYearEnd+"");
                		}
                		if(searchCredMonthStart != null && !"".equals(searchCredMonthStart)){
                			sql.append(" and tblAccMain.period>="+searchCredMonthStart+"");
                		}
                		if(searchCredMonthEnd != null && !"".equals(searchCredMonthEnd)){
                			sql.append(" and tblAccMain.period<="+searchCredMonthEnd+"");
                		}
                		
                		/**
                		 * 当输入的科目代码开始和结束相同时，特殊处理
                		 */
                		if(searchAccCodeStart != null && !"".equals(searchAccCodeStart) && searchAccCodeEnd != null && !"".equals(searchAccCodeEnd) && searchAccCodeStart.equals(searchAccCodeEnd)){
                			sql.append(" and acctype.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+searchAccCodeStart+"')+'%'");
                		}else{
                			/* 科目代码*/
                			if(searchAccCodeStart != null && !"".equals(searchAccCodeStart)){
                				sql.append(" and acctype.classCode>=(select classCode from tblAccTypeInfo where AccNumber='"+searchAccCodeStart+"')");
                			}
                			if(searchAccCodeEnd != null && !"".equals(searchAccCodeEnd)){
                				sql.append(" and acctype.classCode<=(select classCode from tblAccTypeInfo where AccNumber='"+searchAccCodeEnd+"')");
                			} 
                		}
                		
                		/* 摘要*/
                		if(searchRecordComment != null && !"".equals(searchRecordComment)){
                			sql.append(" and d.RecordComment like '%"+searchRecordComment+"%'");
                		}
                		
                		/* 过账状态*/
                		if(searchWName !=null && !"".equals(searchWName)){
                			sql.append(" and tblAccMain.workFlowNodeName='"+searchWName+"'");
                		}
                		/* 复核状态*/
                		if(searchReview != null && !"".equals(searchReview)){
                			sql.append(" and tblAccMain.isReview="+searchReview+"");
                		}
                		/* 审核状态*/
                		if(searchAuditing != null && !"".equals(searchAuditing)){
                			sql.append(" and tblAccMain.isAuditing='"+searchAuditing+"'");
                		}
                		
                		/* 部门 */
                		if(depts != null && !"".equals(depts)){
                			sql.append(" and tblAccMain.departmentCode='"+depts+"'");
                		}
                		if(deptName != null && !"".equals(deptName)){
                			sql.append(" and (SELECT DeptFullName FROM tblDepartment WHERE tblDepartment.classCode=tblAccMain.departmentCode) like '%"+deptName+"%'");
                		}
                		/* 经手人 */
                		if(employees != null && !"".equals(employees)){
                			sql.append(" and tblAccMain.employeeId='"+employees+"'");
                		}
                		if(employeesName != null && !"".equals(employeesName)){
                			sql.append(" and (SELECT EmpFullName FROM tblEmployee WHERE tblEmployee.id=tblAccMain.employeeId) like '%"+employeesName+"%'");
                		}
                		/* 创建人 */
                		if(createBy != null && !"".equals(createBy)){
                			sql.append(" and tblAccMain.createBy='"+createBy+"'");
                		}
                		if(createByName != null && !"".equals(createByName)){
                			sql.append(" and (SELECT EmpFullName FROM tblEmployee WHERE tblEmployee.id=tblAccMain.createBy) like '%"+createByName+"%'");
                		}
                		/* 往来单位 */
                		if(searchClient != null && !"".equals(searchClient)){
                			sql.append(" and d.CompanyCode='"+searchClient+"'");
                		}
                		if(searchClientName != null && !"".equals(searchClientName)){
                			sql.append(" and (SELECT ComFullName from tblCompany WHERE tblCompany.classCode=d.CompanyCode) like '%"+searchClientName+"%'");
                		}
                		
                		/* 单据类型 */
                		if(searchRefBillType != null && !"".equals(searchRefBillType)){
                			sql.append(" and ((SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(SELECT tblDbTableInfo.languageId FROM tblDbTableInfo WHERE tblDbTableInfo.tableName=tblAccMain.RefBillType)) like '%"+searchRefBillType+"%' or tblAccMain.RefBillType like '%"+searchRefBillType+"%')");
                		}
                		
                		/* 金额 */
                		if(searchMoneyStart != null && !"".equals(searchMoneyStart)){
                			sql.append(" and (d.DebitAmount+d.LendAmount >="+searchMoneyStart+") ");
                		}
                		if(searchMoneyEnd != null && !"".equals(searchMoneyEnd)){
                			sql.append(" and (d.DebitAmount+d.LendAmount <="+searchMoneyEnd+") ");
                		}
                		
                		/*设置范围权限*/
        				ArrayList scopeRight = new ArrayList();
        				scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
        				scopeRight.addAll(loginBean.getAllScopeRight());
                		sql = new StringBuffer(DynDBManager.scopeRightHandler("tblAccMain", "TABLELIST", "", loginBean.getId(), scopeRight, sql.toString(), null,""));
            			
                		String sqlStr = "";
                        int total=0;
                        int pageNo2 = searchForm.getPageNo();
                        int pageSize = searchForm.getPageSize();
                        try {  
                        	st = conn.createStatement();
	                        if(pageSize>0){//查询总行数
	                        	sqlStr="select count(0) from ("+sql.toString()+") as a";
	                            rs=st.executeQuery(sqlStr);                            	
	                            if(rs.next()){
	                            	int totalSize=rs.getInt(1);
	                            	total = totalSize;
	                              	int totalPage = totalSize%pageSize>0?totalSize/pageSize+1:totalSize/pageSize;
	                              	if(pageNo2>totalPage){
	                              		pageNo2 = totalPage;
	                              	}
	                              	rst.setTotalPage(totalPage);
	                            }
	                            rs.close();
	                            sqlStr="select *,ROW_NUMBER() OVER( order by a.row_id asc) as counts from ("+sql+") as a";
	                            sqlStr+=" where row_id between "+(pageSize*(pageNo2-1)+1)+" and "+pageSize*pageNo2;    
	                            sqlStr += " ORDER BY CredYear desc,period desc,orderNo desc";
	                        }else if(pageSize==-1111){//这是导出时取所有id用
	                        	sqlStr = "select tblAccMain.id as id "+sql.substring(sql.indexOf(" from tblAccMain"));
	                        }else{
	                            sqlStr=sql.toString();
	                        }
	                        
                            if (pageNo2 < 1){
                            	pageNo2 = 1;
                            }
                            List list = new ArrayList();
                            BaseEnv.log.debug("VoucherMgt.queryList 查询凭证sql = "+sqlStr);
                            rs=st.executeQuery(sqlStr);    
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
                            sqlStr = sqlStr.replace("+", "@AddSign:");
                            Object[] object = new Object[]{list,sqlStr};
                            rst.setRetVal(object);
                            rst.setPageNo(pageNo2);
                            rst.setPageSize(pageSize);
                            rst.setRealTotal(total);
                        } catch (Exception ex) {
                        	BaseEnv.log.error("VoucherMgt queryList:",ex) ;
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
	
	/**
	 * 删除凭证
	 * @param keyId
	 * @return
	 */
	public Result deleteData(final String[] keyIds,final String tableName,final Hashtable allTables,
			final HashMap values,final String userId,final String defineInfo,
			final MessageResources resources,final Locale locale){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement() ;
							ResultSet rset = null;
							Integer isAuditing = 1;
							/*判断是否启用标准财务和是否不启用审核自动审核*/
							String sqls = "select IsAuditing from tblAccMainSetting";
							rset = state.executeQuery(sqls);
							if(rset.next()){
								isAuditing = rset.getInt("IsAuditing");
							}
							
							String selectsql = "select Attachment,BillDate,id,createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,isnull(workFlowNodeName,'') as workFlowNodeName,isnull(AutoBillMarker,0) as AutoBillMarker,RefBillType,RefBillID from tblAccMain where id=?";
							PreparedStatement ps = conn.prepareStatement(selectsql);
							String[] files = new String[keyIds.length];
							int i =0;
							Result result = new Result();
							for(String str : keyIds){
								/* 查询是否存在附件*/
					            ps.setString(1, str);
					            rset = ps.executeQuery();
								//删除前进行define文件验证
								values.put("id", str);
								if (rset.next()) {
									if(isAuditing == 1 || rset.getInt("AutoBillMarker")==1){
										result = new DynDBManager().defineSql(conn, "delete", false, tableName,(Hashtable) allTables, values,str, userId,defineInfo,resources,locale);
							            if (result.getRetCode() <
							                ErrorCanst.DEFAULT_SUCCESS) {
							                rs.setRetCode(result.getRetCode());
							                rs.setRetVal(result.getRetVal());
							                BaseEnv.log.error("VoucherMgt before deleteData Error code = " + result.getRetCode() + " Msg=" + result.getRetVal());
							                return;
							            }
									}
									
					            	files[i] = rset.getString("Attachment");
					            	/* 不启用审核*/
									if(isAuditing==0 && "finish".equals(rset.getString("workFlowNodeName"))){
										/* 反过账*/
										DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("tblAccMain_Del_One");
										HashMap<String,String> hashmap = new HashMap<String,String>();
										hashmap.put("tblAccMain_BillDate", rset.getString("BillDate"));
										hashmap.put("tblAccMain_id", rset.getString("id"));
										hashmap.put("tblAccMain_createBy", rset.getString("createBy"));
										hashmap.put("tblAccMain_createTime", rset.getString("createTime"));
										hashmap.put("tblAccMain_lastUpdateBy", rset.getString("lastUpdateBy"));
										hashmap.put("tblAccMain_lastUpdateTime", rset.getString("lastUpdateTime"));
										hashmap.put("tblAccMain_SCompanyID", rset.getString("SCompanyID"));
										result = defineSqlBean.execute(conn, hashmap, userId,resources,locale,"");
				                        if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				                        	rs.setRetCode(result.getRetCode());
							                rs.setRetVal(result.getRetVal());
				                        	return ;
				                        }
									}
									String RefBillType = rset.getString("RefBillType");
									String RefBillID = rset.getString("RefBillID");
									DBTableInfoBean dfb = GlobalsTool.getTableInfoBean(RefBillType);
									boolean isExistCerti = false;
									if(dfb!=null){
										for (DBFieldInfoBean dfbno : dfb.getFieldInfos()) {
											if ("CertificateNo".equalsIgnoreCase(dfbno.getFieldName())) {
												isExistCerti = true;
											}
										}
									}
									if(isExistCerti){
										String sql = "update "+RefBillType+" set CertificateNo=''  where CertificateNo = '"+str+"'";
							            state.addBatch(sql) ;
									}
			                    }
								
					            /* 删除*/
					            String sql = "delete tblAccDetail where f_ref in (select id from tblAccMain where id='"+str+"')";
					            state.addBatch(sql) ;
					            sql = "delete tblAccMain where id='"+str+"'";
					            state.addBatch(sql) ;
					            i++;
					            result = new DynDBManager().defineSql(conn, "delete", true, tableName,allTables, values,str, userId,defineInfo,resources,locale);
					            if (result.getRetCode() <
					                ErrorCanst.DEFAULT_SUCCESS) {
					                rs.setRetCode(result.getRetCode());
					                rs.setRetVal(result.getRetVal());
					                BaseEnv.log.error("VoucherMgt after deleteData Error code = " + result.getRetCode() + " Msg=" +
					                        result.getRetVal());
					                return;
					            } 
					            
							}
							state.executeBatch() ;
							rs.setRetVal(files);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt deleteData:",ex) ;
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
	 * 凭证设置修改
	 * @param bean
	 * @return
	 */
	public Result updateSetting(AccMainSettingBean bean){
		return updateBean(bean);
	}
	
	/**
	 * 查询所有凭证模板
	 * @return
	 */
	public Result queryModuleList(final String searchValue){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT id,ModuleName FROM tblAccMainTemplete ");
							
							//当搜索条件为null时不添加查询条件
							if(searchValue != null && !"".equals(searchValue)){
								sql.append(" where ModuleName like '%"+ searchValue +"%'");
							}
							sql.append(" order by createTime desc");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ResultSet rs = ps.executeQuery();
							List<String[]> list = new ArrayList<String[]>();
							while(rs.next()){
								String[] str = new String[2];
								str[0] = rs.getString("id");
								str[1] = rs.getString("ModuleName");
								list.add(str);
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryModuleList:",ex) ;
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
	 * 添加凭证模板
	 * @param tableName
	 * @param allTables
	 * @param values
	 * @param id
	 * @param userId
	 * @param defineInfo
	 * @param resources
	 * @param locale
	 * @param saveType
	 * @return
	 */
	public Result addAccMainModule(final String tableName,final Hashtable allTables,
		final HashMap values,final String id,final String userId,final String defineInfo,
		final MessageResources resources,final Locale locale,final String saveType){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
		    	session.doWork(new Work() {
		        	public void execute(Connection conn) throws SQLException {
		        		try{
			            	//添加主表数据
			            	Result ires = new DynDBManager().execInert(conn, tableName, allTables, values, "", resources, locale);
			            	if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			                    BaseEnv.log.debug("Insert Table " + tableName +
			                                      " Info: " +
			                                      ires.getRetVal());
			                    rs.setRetCode(ires.getRetCode());
			                    rs.setRetVal(ires.getRetVal());
			                }else{
			                	ArrayList childTableList = DDLOperation.getChildTables(tableName,allTables);
								for (int i = 0; i < childTableList.size(); i++) {
								     DBTableInfoBean childTb = (DBTableInfoBean)childTableList.get(i);
								    ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
									if (childList == null) {
									    continue;
									}
									for (int j = 0; j < childList.size(); j++) {
					                    HashMap childMap = (HashMap) childList.get(j);
					                    childMap.put("id", IDGenerater.getId());
					                    childMap.put("f_ref", id);
					                    //添加从表插入
					                    ires = new DynDBManager().execInert(conn, childTb.getTableName(),allTables, childMap,"",resources,locale);
					                    
					                    //插入失败
					                    if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS 
					                    		&& ires.getRetCode() != 2601) {                    	
					                        BaseEnv.log.debug("Insert Table " + childTb.getTableName() + " Info: " + ires.getRetVal());
					                        rs.setRetCode(ires.getRetCode());
					                        rs.setRetVal(ires.getRetVal());
					                        return;
					                    }
					                }
								}
			                }
		        		}catch (Exception ex) {
				                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				                BaseEnv.log.error("VoucherMgt addAccMainModule data Error :", ex);
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
	 * 删除凭证模板
	 * @param keyId
	 * @return
	 */
	public Result deleteModule(final String[] keyIds){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement() ;
							String selectsql = "select Attachment from tblAccMainTemplete where id=?";
							PreparedStatement ps = conn.prepareStatement(selectsql);
							String[] files = new String[keyIds.length];
							int count = 0;
							for(String keyId : keyIds){
					            //查询是否存在附件
					            ps.setString(1, keyId);
					            ResultSet rset = ps.executeQuery();
					            if (rset.next()) {
					            	files[count] = rset.getString("Attachment");
			                    }
					            rset.close();
					            String sql = "delete tblAccDetailTemplete where f_ref in (select id from tblAccMainTemplete where id='"+keyId+"')";
					            state.addBatch(sql) ;
					            sql = "delete tblAccMainTemplete where id='"+keyId+"'";
					            state.addBatch(sql) ;
					            count ++;
							}
							state.executeBatch() ;
							rs.setRetVal(files);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt deleteModule:",ex) ;
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
	 * 审核(审核通过/审核不通过/反审核)
	 * @param keyIds				凭证ID	
	 * @param remark				批注
	 * @param workflowNodeName  	审核状态
	 * @param approver				审核人
	 * @return
	 */
	public Result approveUpdate(final String[] keyIds,final String remark,
			final String isAuditing,final String approver,final String dealType,
			final Locale locale, final LoginBean loginBean){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String str = "";
							for(int i=0;i<keyIds.length;i++){
								str += "'"+keyIds[i]+"'";
								if(i<keyIds.length-1){
									str += ",";
								}
							}
							//处理凭证时进行验证
							String sql = "select isNull(workflowNodeName,'') as workflowNodeName,isNull(isAuditing,'') as isAuditing,CredTypeID,OrderNo,CredYear,CredMonth,Period from tblAccMain where id in ("+str+")";
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql);
							int i=0;
							while(rset.next()){
								String Auditing = rset.getString("isAuditing");
								//验证凭证是否是当前会计期间之前
								Boolean flag = isAccPeriod(loginBean,rset.getInt("CredYear"),rset.getInt("CredMonth"),rset.getInt("Period"));
								if(flag){
									rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"在当前财务期间之前，不允许此操作");
									rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
									return;
								}
								if(Auditing!= null && !"".equals(Auditing)){
									if("noPass".equals(dealType)){
										//审核不通过
										if("noPass".equals(Auditing)){
											//已经是审核不通过
											//rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
											//		GlobalsTool.getMessage(locale,"erp.voucher.nopass.error"));
											//rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											//return;
										}else if("finish".equals(Auditing)){
											//审核通过
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
													GlobalsTool.getMessage(locale,"erp.voucher.yespass.error"));
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}else if("yesPass".equals(dealType)){
										//审核通过
										if("finish".equals(Auditing)){
//											//已审核
//											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
//													GlobalsTool.getMessage(locale,"erp.voucher.yespass.error"));
//											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
//											return;
											continue;
										}
									}else if("reversePass".equals(dealType)){
										//反审核
										if("noPass".equals(Auditing)){
											//审核不通过
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
													GlobalsTool.getMessage(locale,"erp.voucher.nopass.error"));
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}else if("start".equals(Auditing)){
											//未审核
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
													GlobalsTool.getMessage(locale,"erp.voucher.nostatus.error"));
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}
								}
								i++;
							}
							if(i==0){
								rs.setRetVal("所选凭证都已审核，无需再审核！");
								rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
								return;
							}
							sql = "update tblAccMain set isAuditing=?,approveRemark=isnull(approveRemark,'')+?,approver=? where id in ("+str+")";
							if("yesPass".equals(dealType)){
								sql=sql+" and isNull(isAuditing,'')!='finish' and isNull(workflowNodeName,'')!='finish'";
							}
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, isAuditing);
							ps.setString(2, remark);
							ps.setString(3, approver);
							ps.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt approveUpdate:",ex) ;
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
	 * 审核(审核通过/审核不通过/反审核)
	 * @param keyIds				凭证ID	
	 * @param remark				批注
	 * @param workflowNodeName  	审核状态
	 * @param approver				审核人
	 * @return
	 */
	public Result batchAudite(final Integer voucherarea,final String vouchertime,final LoginBean loginBean,
			final MessageResources resources,final Locale locale,final String voucherPeriodYear,final String voucherPeriod,final AccPeriodBean accPeriodBean,final String remark){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update tblAccMain set isAuditing='finish',approveRemark=isnull(approveRemark,'')+'"+remark+"',approver='"+loginBean.getId()
								+"' where (CredYear>"+accPeriodBean.getAccYear()+" or (CredYear="+accPeriodBean.getAccYear()+" and Period>="+accPeriodBean.getAccPeriod()+")) "
								+" and isNull(isAuditing,'')!='finish' and isNull(workflowNodeName,'')!='finish'";
							if(voucherarea==2){
								sql+=" and BillDate<='"+vouchertime+"'";
							}else if(voucherarea==3){
								sql+=" and CredYear="+voucherPeriodYear+" and Period="+voucherPeriod;
							}
							 
							Statement st = conn.createStatement();
							st.execute(sql);							
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt approveUpdate:",ex) ;
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
	 * 过账、反过账
	 * @param keyIds				凭证ID
	 * @param remark				批注
	 * @param workFlowNodeName     	是否过账
	 * @param postingUser   		过账人
	 * @param settingBean   		凭证设置
	 * @return
	 */
	public Result binderUpdate(final String[] keyIds,final String remark,
			final String workFlowNodeName,final String postingUser,final String dealType,
			final AccMainSettingBean settingBean,final LoginBean loginBean,
			final MessageResources resources,final Locale locale){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String str = "";
							for(int i=0;i<keyIds.length;i++){
								str += "'"+keyIds[i]+"'";
								if(i<keyIds.length-1){
									str += ",";
								}
							}
							//过账和反过账时进行验证（不符合条件时不过账和反过账）
							StringBuffer sql = new StringBuffer(" SELECT tblAccMain.workflowNodeName,isnull(tblAccMain.workFlowNode,'0') as workFlowNode,");
							sql.append("tblAccMain.CredTypeID,tblAccMain.CredNo,tblAccMain.OrderNo,tblAccMain.isAuditing,");
							sql.append("tblAccMain.BillDate,tblAccMain.id,tblAccMain.createBy,tblAccMain.createTime,");
							sql.append("tblAccMain.lastUpdateBy,tblAccMain.lastUpdateTime,tblAccMain.SCompanyID,");
							sql.append("acctype.cashClass,acctype.bankClass,acctype.equivalentClass,");
							sql.append("tblAccMain.CredYear,tblAccMain.CredMonth,tblAccMain.Period,");
							sql.append("tblAccMain.isReview from tblAccMain JOIN tblAccDetail tad ");
							sql.append("ON tad.f_ref = tblAccMain.id JOIN tblAccTypeInfo acctype ");
							sql.append("ON acctype.AccNumber=tad.AccCode where tblAccMain.id in ("+str+")");
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							HashMap<String, HashMap> map = new HashMap<String, HashMap>();
							while(rset.next()){
								String wFlowName = rset.getString("workFlowNodeName");		//是否过账
								String workFlowNode = rset.getString("workFlowNode");	
								//验证凭证是否是当前财务期间之前
								Boolean flag = isAccPeriod(loginBean,rset.getInt("CredYear"),rset.getInt("CredMonth"),rset.getInt("Period"));
								if(flag){
									rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"在当前财务期间之前，不允许此操作");
									rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
									return;
								}
								if("binder".equals(dealType)){
									/* 过账*/
									//启用了财务审核和选中启用过账时必须审核
									if(settingBean.getIsAuditing()==1 && settingBean.getIsAccountAuditing() == 1){
										//凭证过账前必须审核
										String auditing = rset.getString("isAuditing");
										if(!"finish".equals(auditing)){
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
													GlobalsTool.getMessage(locale, "erp.voucher.nostatus.error"));
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}
									if(settingBean.getIsCheck() == 1){
										//凭证过账前必须出纳复核
										Integer cashClass = rset.getInt("cashClass");   				//现金科目类
										Integer bankClass = rset.getInt("bankClass");   				//银行科目类
										Integer equivalentClass = rset.getInt("equivalentClass");   	//现金等价物类
										Integer isReview = rset.getInt("isReview");						//是否复核（1未复核）
										if(isReview == 1){
											if(cashClass == 1 || bankClass == 1 || equivalentClass == 1){
												//存在现金银行类科目并且未复核
												rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"未复核，不允许此操作");
												rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
												return;
											}
										}
									}
									if("finish".equals(wFlowName) || "-1".equals(workFlowNode)){
//										//已过账无法重复过账
//										rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
//												GlobalsTool.getMessage(locale,"erp.voucher.binder.error"));
//										rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
//										return;
										continue;
									}
								}else if("reverseBinder".equals(dealType)){
									/* 反过账*/
									if(!"finish".equals(wFlowName) && "0".equals(workFlowNode)){
										//未过账无法反过账
										rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"未过账，不允许此操作");
										rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
										return;
									}
								}
								
								//添加数据到map中为
								HashMap<String,String> hashmap = new HashMap<String,String>();
								hashmap.put("tblAccMain_BillDate", rset.getString("BillDate"));
								hashmap.put("tblAccMain_id", rset.getString("id"));
								hashmap.put("tblAccMain_createBy", rset.getString("createBy"));
								hashmap.put("tblAccMain_createTime", rset.getString("createTime"));
								hashmap.put("tblAccMain_lastUpdateBy", rset.getString("lastUpdateBy"));
								hashmap.put("tblAccMain_lastUpdateTime", rset.getString("lastUpdateTime"));
								hashmap.put("tblAccMain_SCompanyID", rset.getString("SCompanyID"));
								hashmap.put("tblAccMain_SCompanyID", rset.getString("SCompanyID"));
								map.put(rset.getString("id"), hashmap);
							}
							if(map.size()==0){
								rs.setRetVal("所选凭证都已过账，无需再过账！");
								rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
								return;
							}
							sql = new StringBuffer("update tblAccMain set workFlowNodeName=?,approveRemark=isnull(approveRemark,'')+?");
							
							if("binder".equals(dealType)){
								//过账
								sql.append(",workFlowNode='-1'");
							}else{
								sql.append(",workFlowNode='0'");
							}
							
							sql.append(",postingUser=? where id in ("+str+") ");
							if("binder".equals(dealType)){
								sql.append(" and isNull(workflowNodeName,'')!='finish'");
							}
							
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, workFlowNodeName);
							ps.setString(2, remark);
							ps.setString(3, postingUser);
							ps.executeUpdate();
							
							/* 处理完调用defineAcc.xml文件进行过账后续处理*/
							if(map.size()>0){
								Iterator iter= map.entrySet().iterator();
								while(iter.hasNext()){
									String defineName = "";
									if("binder".equals(dealType)){
										defineName = "tblAccMain_Add_One";
									}else if("reverseBinder".equals(dealType)){
										defineName = "tblAccMain_Del_One";
									}
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
									if (defineSqlBean == null) {
			                            BaseEnv.log.error("Define Sql Not Exist :Name = "+defineName);
			                            rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
			                            return  ;
			                        }
			                        
									//取hashMap中的数据
									Entry entry= (Entry)iter.next();
			                        Result ret = defineSqlBean.execute(conn, (HashMap)entry.getValue(), loginBean.getId(),resources,locale,"");									
									
									if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			                        	rs.retCode=ret.getRetCode();
			                        	rs.retVal=ret.getRetVal();
			                        	return ;
			                        }
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt binderUpdate:",ex) ;
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
	 * 根据过账向导进行过账
	 * @param keyIds
	 * @param remark
	 * @param workFlowNodeName
	 * @param postingUser
	 * @param dealType
	 * @param settingBean
	 * @param userId
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result batchBinder(final Integer iscredNo,final Integer postingerror,
			final Integer voucherarea,final String vouchertime,
			final AccMainSettingBean settingBean,final LoginBean loginBean,
			final MessageResources resources,final Locale locale,final String credTypeid,final String voucherPeriodYear,final String voucherPeriod){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							int total = 0;
							
							StringBuffer sql = new StringBuffer(" SELECT DISTINCT isnull(tblAccMain.workflowNodeName,'') as workflowNodeName,");
							sql.append("tblAccMain.CredTypeID,tblAccMain.CredNo,tblAccMain.OrderNo,isnull(tblAccMain.isAuditing,'') as isAuditing,");
							sql.append("tblAccMain.BillDate,tblAccMain.id,tblAccMain.createBy,tblAccMain.createTime,");
							sql.append("tblAccMain.CredYear,tblAccMain.CredMonth,tblAccMain.Period,");
							sql.append("tblAccMain.lastUpdateBy,tblAccMain.lastUpdateTime,tblAccMain.SCompanyID,");
							sql.append("acctype.cashClass,acctype.bankClass,acctype.equivalentClass,tblAccMain.isReview from tblAccMain JOIN tblAccDetail tad ");
							sql.append("ON tad.f_ref = tblAccMain.id JOIN tblAccTypeInfo acctype ");
							sql.append("ON acctype.AccNumber=tad.AccCode where tblAccMain.workflowNodeName!='finish' ");
							
							StringBuffer countSql = new StringBuffer();
							//指定日期之前过滤
							if(voucherarea == 2){
								countSql.append("and tblAccMain.BillDate<='"+vouchertime+"'");
							}
							//指定期间过滤
							if(voucherarea == 3){
								countSql.append("and tblAccMain.CredYear="+voucherPeriodYear+" and tblAccMain.Period="+voucherPeriod);
							}
							//已启用审核并且选择了凭证过账前必须审核
							if(settingBean.getIsAccountAuditing()==1){
								countSql.append(" and tblAccMain.isAuditing='finish'");
							}else{
								countSql.append(" and tblAccMain.isAuditing!='noPass'");
							}
							
							countSql.append(" and tblAccMain.CredTypeID='"+credTypeid+"' ");
							//查询总数
							Statement st = conn.createStatement();
							ResultSet rset=st.executeQuery("SELECT count(0) as count from tblAccMain where tblAccMain.workFlowNodeName!='finish' and tblAccMain.workFlowNode!='-1' "+countSql.toString());                            	
                            if(rset.next()){
                            	total=rset.getInt(1);
                            }
                            rset.close();
                            
							sql.append(countSql+" ORDER BY tblAccMain.CredTypeID,tblAccMain.OrderNo,tblAccMain.createtime");
							rset = st.executeQuery(sql.toString());
							String accMainId = "";
							Integer crednumber = 0;
							int successnum = 0;
							int errorsum = 0;
							while(rset.next()){
								String id = rset.getString("id");			//凭证ID
								Integer OrderNo = rset.getInt("OrderNo");		//凭证号
								
								//验证凭证是否是当前财务期间之前
								Boolean flag = isAccPeriod(loginBean,rset.getInt("CredYear"),rset.getInt("CredMonth"),rset.getInt("Period"));
								if(flag){
									continue;
								}
								
								if(iscredNo == 1 && !"".equals(accMainId) && !id.equals(accMainId) 
										&& crednumber != 0 && OrderNo!=crednumber+1){
									//凭证号不连续时停止过账
									rs.setRetVal(rset.getString("CredTypeID")+"-"+OrderNo+"不连续，不允许此操作");
									rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
									break ;
								}
								
								if(settingBean.getIsCheck() == 1){
									//凭证过账前必须出纳复核
									Integer cashClass = rset.getInt("cashClass");   				//现金科目类
									Integer bankClass = rset.getInt("bankClass");   				//银行科目类
									Integer equivalentClass = rset.getInt("equivalentClass");   	//现金等价物类
									Integer isReview = rset.getInt("isReview");						//是否复核
									if(isReview == 1){
										if(cashClass == 1 || bankClass == 1 || equivalentClass == 1){
											//科目启用现金科目类,银行科目类,现金等价物类之一可以复核和反复核 并且未复核
											if(postingerror == 1){
												//发生错误停止过账
												rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"未复核，不允许此操作");
												rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
												return;
											}else{
												errorsum++;
												continue;
											}
										}
									}
								}
								
								//是否审核
								String auditing = rset.getString("isAuditing");
								String remarkStr = "";
								//过账
								if(!id.equals(accMainId)){
									sql = new StringBuffer("update tblAccMain set workFlowNodeName=?,workFlowNode='-1',approveRemark=isnull(approveRemark,'')+?");
									//if(!"finish".equals(workflowNodeName)){
									//	sql.append(",workflowNodeName='finish',approver='"+loginBean.getId()+"'");
									//	remarkStr = GlobalsTool.getMessage(locale,"oa.job.approved")+" "+loginBean.getEmpFullName()+" "+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"<br />";
									//}
									sql.append(",postingUser=? where id=?");
									PreparedStatement ps = conn.prepareStatement(sql.toString());
									ps.setString(1, "finish");
									ps.setString(2, remarkStr+GlobalsTool.getMessage(locale,"muduleFlow.lb.voucher")+" "+loginBean.getEmpFullName()+" "+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"<br />");
									ps.setString(3, loginBean.getId());
									ps.setString(4, id);
									ps.executeUpdate();
									
									/* 处理完调用defineAcc.xml文件进行过账后续处理*/
									String defineName = "tblAccMain_Add_One";
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
									if (defineSqlBean == null) {
			                            BaseEnv.log.error("Define Sql Not Exist :Name = "+defineName);
			                            rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
			                            return  ;
			                        }
									
									//设置define中需要的参数
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("tblAccMain_BillDate", rset.getString("BillDate"));
									map.put("tblAccMain_id", id);
									map.put("tblAccMain_createBy", rset.getString("createBy"));
									map.put("tblAccMain_createTime", rset.getString("createTime"));
									map.put("tblAccMain_lastUpdateBy", rset.getString("lastUpdateBy"));
									map.put("tblAccMain_lastUpdateTime", rset.getString("lastUpdateTime"));
									map.put("tblAccMain_SCompanyID", rset.getString("SCompanyID"));
									
			                        Result ret = defineSqlBean.execute(conn, map, loginBean.getId(),resources,locale,"");
			                        if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			                        	if(postingerror == 1){
				                        	rs.retCode=ret.getRetCode();
				                        	rs.retVal=ret.getRetVal();
				                        	return ;
			                        	}else{
			                        		errorsum++;
			                        		continue ;
			                        	}
			                        }
			                        successnum++;
			                        new DynDBManager().addLog(14, loginBean.getId(), loginBean.getEmpFullName(), loginBean.getSunCmpClassCode(),"凭证处理:"+"["+rset.getString("CredTypeID")+"-"+rset.getString("CredNo")+"]"+"过账成功;","tblAccMain", "凭证管理","凭证处理");
								}
		                        accMainId = id;
								crednumber = OrderNo;
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        	rs.setRetVal("待过账凭证"+total+"张，正确过账"+successnum+"张，发生错误"+errorsum+"张");
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt batchBinder:",ex) ;
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
	 * 复核/反复核
	 * @param keyIds
	 * @param remark
	 * @param isReview
	 * @param reviewUser
	 * @param dealType
	 * @param settingBean
	 * @param userId
	 * @return
	 */
	public Result checkUpdate(final String[] keyIds,final String remark,
			final Integer isReview,final String reviewUser,final String dealType,
			final AccMainSettingBean settingBean,final LoginBean loginBean,final String acceptMode,
			final String acceptNumber,final String isList){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String str = "";
							for(int i=0;i<keyIds.length;i++){
								str += "'"+keyIds[i]+"'";
								if(i<keyIds.length-1){
									str += ",";
								}
							}
							String ids = "";
							//处理凭证时进行验证
							String sql = "select tblAccMain.id,tblAccMain.CredTypeID,tblAccMain.OrderNo,isnull(tblAccMain.workFlowNode,'0') as workFlowNode,isnull(tblAccMain.isReview,1) as isReview,tblAccMain.CredYear,tblAccMain.CredMonth,tblAccMain.Period,";
							sql += "acctype.cashClass,acctype.bankClass,acctype.equivalentClass,isnull(tblAccMain.workFlowNodeName,'') as workFlowNodeName from tblAccMain join ";
							sql += "tblAccDetail on tblAccMain.id=tblAccDetail.f_ref JOIN tblAccTypeInfo acctype ";
							sql += "ON acctype.AccNumber=tblAccDetail.AccCode where tblAccMain.id in ("+str+")";
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql);
							while(rset.next()){
								Integer cashClass = rset.getInt("cashClass");   				//现金科目类
								Integer bankClass = rset.getInt("bankClass");   				//银行科目类
								Integer equivalentClass = rset.getInt("equivalentClass");   	//现金等价物类
								//验证凭证是否是当前财务期间之前
								Boolean flag = isAccPeriod(loginBean,rset.getInt("CredYear"),rset.getInt("CredMonth"),rset.getInt("Period"));
								if(flag){
									rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"在当前财务期间之前，不允许此操作");
									rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
									return;
								}
								
								//复核或反复核
								if(cashClass == 1 || bankClass == 1 || equivalentClass == 1){
									Integer isReview = rset.getInt("isReview");
									String workFlowNodeName = rset.getString("workFlowNodeName");		//过账标识
									String workFlowNode = rset.getString("workFlowNode");
									//复核
									if("check".equals(dealType)){
										//已复核无法在复核
										if(isReview == 2){
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"已复核，不允许此操作");
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}else if("reverseCheck".equals(dealType)){
										//反复核
										if(isReview == 1){
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"未复核，不允许此操作");
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}
									//已过账无法复核
									if("finish".equals(workFlowNodeName) || "-1".equals(workFlowNode)){
										rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"已过账，不允许此操作");
										rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
										return;
									}
									//科目启用现金科目类,银行科目类,现金等价物类之一可以复核和反复核
									ids += "'"+rset.getString("id")+"',";
								}
							}
							if(ids.length()==0){
								rs.setRetVal("凭证无需复核或反复核！");
								rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
								return;
							}else{
								if(",".equals(ids.substring(ids.length()-1))){
									ids = ids.substring(0, ids.length()-1);
								}
							}
							sql = "update tblAccMain set isReview=?,approveRemark=isnull(approveRemark,'')+?,ReviewUser=? ";
							
							/* 复核如果修改了结算号和结算方式进行修改*/
							if(isList!= null && !"".equals(isList) && "check".equals(dealType)){
								sql += ",AcceptMode='"+acceptMode+"',AcceptNumber='"+acceptNumber+"' ";
							}
							
							sql += "where id in ("+ids+")";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setInt(1, isReview);
							ps.setString(2, remark);
							ps.setString(3, reviewUser);
							ps.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt checkUpdate:",ex) ;
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
	 * 查询做单人
	 * @param keyIds
	 * @return
	 */
	public Result queryCreatePerson(final String keyIds[]){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String str = "";
							for(int i=0;i<keyIds.length;i++){
								str += "'"+keyIds[i]+"'";
								if(i<keyIds.length-1){
									str += ",";
								}
							}
							String selectsql = "select id,CredTypeID,OrderNo,createBy from tblAccMain where id in ("+str+")";
							PreparedStatement ps = conn.prepareStatement(selectsql);
							ResultSet result = ps.executeQuery();
							List list = new ArrayList();
							while(result.next()){
								String[] rsStr = new String[4];
								rsStr[0] = result.getString("id");
								rsStr[1] = result.getString("CredTypeID");
								rsStr[2] = String.valueOf(result.getInt("OrderNo"));
								rsStr[3] = result.getString("createBy");
								list.add(rsStr);
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryCreatePerson:",ex) ;
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
	 * 查询凭证设置
	 * @return
	 */
	public Result queryVoucherSetting(){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Result rse = queryVoucherSetting(conn);
						rs.setRetCode(rse.getRetCode());
						rs.setRetVal(rse.getRetVal());
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 凭证设置
	 * @param conn
	 * @return
	 */
	public Result queryVoucherSetting(Connection conn){
		final Result rs = new Result();
		try {
			String selectsql = "select * from tblAccMainSetting";
			PreparedStatement ps = conn.prepareStatement(selectsql);
			ResultSet result = ps.executeQuery();
			AccMainSettingBean bean = null;
			if(result.next()){
				bean = new AccMainSettingBean();
				bean.setId(result.getString("id"));
				bean.setIsAccountAuditing(result.getInt("IsAccountAuditing"));
				bean.setIsAuditing(result.getInt("IsAuditing"));
				bean.setIsCheck(result.getInt("IsCheck"));
				bean.setIsCash(result.getInt("IsCash"));
				bean.setAuditingPersont(result.getString("auditingPersont"));
				bean.setReverseAuditing(result.getString("reverseAuditing"));
				bean.setBinderPersont(result.getString("binderPersont"));
				bean.setReverseBinder(result.getString("reverseBinder"));
				bean.setCheckPersont(result.getString("checkPersont"));
				bean.setCashPersont(result.getString("cashPersont"));
			}
			rs.setRetVal(bean);
		} catch (Exception ex) {
			ex.printStackTrace();
			BaseEnv.log.error("VoucherMgt queryVoucherSetting:",ex) ;
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rs;
		}
		return rs;
	}
	
	public Result  saveToAccTemp(final String name,final String keyId){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String id = IDGenerater.getId();
							StringBuffer selectsql = new StringBuffer("INSERT INTO [tblAccMainTemplete]([id],[SignerID],[CredNo],[BillDate],[RefBillType],[ModuleName],[AcceNum],[CredYear],[CredMonth],[AuditorID],[BinderID],[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[Period],[RefBillNo],[RefBillID],[EmployeeID],[DepartmentCode],[OrderNo],[CredTypeID],[Remark],[Attachment],[SCompanyID],[classCode],[RowON],[AutoBillMarker],[workFlowNodeName],[workFlowNode],[printCount],[checkPersons],[finishTime],[CheckPersont],[AcceptMode],[AcceptNumber]) ");
							selectsql.append("select ?,[SignerID],[CredNo],[BillDate],'',?,[AcceNum],[CredYear],[CredMonth],[AuditorID],[BinderID],[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[Period],[RefBillNo],'',[EmployeeID],[DepartmentCode],[OrderNo],[CredTypeID],[Remark],[Attachment],[SCompanyID],[classCode],[RowON],[AutoBillMarker],[workFlowNodeName],[workFlowNode],[printCount],[checkPersons],[finishTime],[CheckPersont],[AcceptMode],[AcceptNumber] ");
							selectsql.append(" from tblAccMain where id=?");
							PreparedStatement ps = conn.prepareStatement(selectsql.toString());
							ps.setString(1, id);
							ps.setString(2, name);
							ps.setString(3, keyId);
							ps.execute();
							
							selectsql = new StringBuffer("INSERT INTO [tblAccDetailTemplete]([id],[RefBillType],[CompanyCode],[DepartmentCode],[ProjectCode],[AccCode],[DebitAmount],[LendAmount],[DebitCurrencyAmount],[LendCurrencyAmount],[CurrencyRate],[RecordOrder],[PeriodYear],[PeriodMonth],[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[f_ref],[RecordComment],[RefBillID],[AccDate],[SCompanyID],[Currency],[classCode],[RowON],[workFlowNodeName],[workFlowNode],[EmployeeID],[StockCode])");
							selectsql.append("select subString(cast(newid() as varchar(36)),1,30),'',[CompanyCode],[DepartmentCode],[ProjectCode],[AccCode],[DebitAmount],[LendAmount],[DebitCurrencyAmount],[LendCurrencyAmount],[CurrencyRate],[RecordOrder],[PeriodYear],[PeriodMonth],[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],?,[RecordComment],'',[AccDate],[SCompanyID],[Currency],[classCode],[RowON],[workFlowNodeName],[workFlowNode],[EmployeeID],[StockCode] ");
							selectsql.append(" from tblAccDetail where f_ref=?  order by detOrderNo");
							ps = conn.prepareStatement(selectsql.toString());
							ps.setString(1, id);
							ps.setString(2, keyId);
							ps.execute();
							
							
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryStopCredNoList:",ex) ;
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
	 * 查询凭证号断号情况
	 * @param CredTypeID
	 * @param accPeriodBean
	 * @return
	 */
	public Result queryStopCredNoList(final String CredTypeID,final AccPeriodBean accPeriodBean,final String types){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							int count = 0;
							int maxOrderNo = 0;
							String sql = "SELECT COUNT(0) as count,isnull(max(OrderNo),0) as maxOrderNo FROM tblAccMain where CredTypeID='"+CredTypeID+"' and CredYear="+accPeriodBean.getAccYear()+" and Period="+accPeriodBean.getAccPeriod();
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql);
							if(rset.next()){
								count = rset.getInt("count");
								maxOrderNo = rset.getInt("maxOrderNo");
							}
							if(types!=null && !"".equals(types) && "queryCredNo".equals(types)){
								//if(maxOrderNo>count){
									count = maxOrderNo;
								//}
							}
							
							Map<Integer,Integer> map = new HashMap<Integer, Integer>();
							for(int i=1;i<=count;i++){
								map.put(i, i);
							}
							
							StringBuffer selectsql = new StringBuffer("select CredTypeID,CredYear,CredMonth,Period,isnull(OrderNo,0) as OrderNo from tblAccMain");
							selectsql.append(" where CredTypeID=? and CredYear=? and Period=? order by OrderNo");
							//and (workflowNodeName='finish' or isPosting=2)
							PreparedStatement ps = conn.prepareStatement(selectsql.toString());
							ps.setString(1, CredTypeID);
							ps.setInt(2, accPeriodBean.getAccYear());
							ps.setInt(3, accPeriodBean.getAccPeriod());
							rset = ps.executeQuery();
							while(rset.next()){
								int OrderNo = rset.getInt("OrderNo");
								map.remove(OrderNo);
							}
							rs.setRetVal(map);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryStopCredNoList:",ex) ;
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
	 * 整理凭证
	 * @param CredTypeID
	 * @param periodYear
	 * @param period
	 * @return
	 */
	public Result resetOrderNo(final String CredTypeID,final Integer periodYear,final Integer period){
		
		String credNoStr = "";
		/* 取空缺的凭证号*/
		AccPeriodBean accPeriodBean = new AccPeriodBean();
		accPeriodBean.setAccYear(periodYear);
		accPeriodBean.setAccPeriod(period);
		Result result = queryStopCredNoList(CredTypeID, accPeriodBean,"");
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			map = (HashMap<Integer, Integer>)result.retVal;
		}
		final Map<Integer,Integer> maps = map;									//未存在的凭证号
		
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							//查询是否启用审核流
							Result rsu = queryVoucherSetting(conn);
							AccMainSettingBean settingBean = new AccMainSettingBean();
							if(rsu.retCode != ErrorCanst.DEFAULT_SUCCESS){
								return;
							}
							settingBean = (AccMainSettingBean)rsu.retVal;
							
							Map<Integer,Integer> intMap = maps;
							String sql = "select id,isnull(OrderNo,0) as OrderNo from tblAccMain where CredTypeID='"+CredTypeID+"'";
							sql += " and CredYear="+periodYear+" and Period="+period;
							if(settingBean.getIsAuditing()==1){
								//启用审核流
								sql += " and workflowNodeName!='finish'";
								sql += " and isAuditing!='finish' order by OrderNo";
							}
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql);
							StringBuffer updatesql = new StringBuffer("update tblAccMain set OrderNo=? where id=?");
							PreparedStatement ps = conn.prepareStatement(updatesql.toString());
							String ids = "";
							while (rset.next()) {
								Integer OrderNo = rset.getInt("OrderNo");
								ids += rset.getString("id")+";";
								if(OrderNo!=0){
									intMap.put(OrderNo, OrderNo);
								}
							}
							int temp = 0;
							if(intMap.size()==0){
								return;
							}
							Iterator iter= intMap.entrySet().iterator();
							Integer[] cred = new Integer[intMap.size()];
							while(iter.hasNext()){
								Entry entry = (Entry)iter.next();
								cred[temp] = Integer.valueOf(entry.getKey().toString());
								temp++;
							}
							temp = 0;
							for(int i=0;i<cred.length;i++){
								for(int j=i;j<cred.length;j++){
									if(Integer.valueOf(cred[i])>Integer.valueOf(cred[j])){
										temp = Integer.valueOf(cred[i]);
										cred[i] = cred[j];
										cred[j] = temp;
									}
								}
							}
							for(int i=0;i<ids.split(";").length;i++){
								ps.setInt(1, Integer.valueOf(cred[i]));
								ps.setString(2, ids.split(";")[i]);
								ps.addBatch();
							}
							ps.executeBatch();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt resetOrderNo:",ex) ;
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
	 * 根据Id查询凭证模板
	 * @param id
	 * @return
	 */
	public Result queryAccMainTempleteId(final String id){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String selectsql = "select id,CredTypeID,isnull(OrderNo,0) as OrderNo from tblAccMainTemplete where id='"+id+"'";
							PreparedStatement ps = conn.prepareStatement(selectsql);
							ResultSet result = ps.executeQuery();
							List list = new ArrayList();
							while(result.next()){
								String[] rsStr = new String[4];
								rsStr[0] = result.getString("id");
								rsStr[1] = result.getString("CredTypeID");
								rsStr[2] = String.valueOf(result.getInt("OrderNo"));
								list.add(rsStr);
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryAccMainTempleteId:",ex) ;
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
	 * 判断凭证是否在会计期间前
	 * @param loginBean
	 * @param year
	 * @param month
	 * @param period
	 * @return
	 */
	public Boolean isAccPeriod(LoginBean loginBean, Integer year, Integer month, Integer period){
		//会计期间
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable)sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean)hashSession.get("AccPeriodAcc");
		boolean flag = false;
		if(year < accPeriodBean.getAccYear()){
			flag = true;
		}else if(year==accPeriodBean.getAccYear()){
			if(period < accPeriodBean.getAccPeriod()){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 查询未审核和未过账的凭证
	 * @return
	 */
	public Result queryNoBind(final AccPeriodBean accPeriod){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String selectsql = "select count(0) as count from tblAccMain where workFlowNodeName!='finish'";
							PreparedStatement ps = conn.prepareStatement(selectsql);
							ResultSet result = ps.executeQuery();
							Integer count = 0;
							if(result.next()){
								count = result.getInt("count");
							}
							rs.setRealTotal(count);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryVoucherSetting:",ex) ;
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
							BaseEnv.log.error("VoucherMgt queryPeriod:",ex) ;
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
	 * 查询最大期间
	 * @return
	 */
	public Result queryMaxPeriod(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT top 1 AccYear,AccPeriod,PeriodEnd FROM tblAccPeriod ORDER BY AccYear DESC,AccPeriod DESC");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							String[] str = new String[3];
							if(rs.next()){
								str[0] = String.valueOf(rs.getInt("AccYear"));
								str[1] = String.valueOf(rs.getInt("AccPeriod"));
								str[2] = rs.getString("PeriodEnd");
							}
							result.setRetVal(str);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryMaxPeriod:",ex) ;
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
	 * 查询当前财务期间
	 * @return
	 */
	public Result queryAccPeriod(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("SELECT AccYear,AccPeriod FROM tblAccPeriod where AccStatusId=1");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							String[] str = new String[3];
							if(rs.next()){
								str[0] = String.valueOf(rs.getInt("AccYear"));
								str[1] = String.valueOf(rs.getInt("AccPeriod"));
							}
							result.setRetVal(str);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryMaxPeriod:",ex) ;
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
	 * 根据id查询凭证数据
	 * @return
	 */
	public Result queryAccMain(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select id,CredTypeID,OrderNo from tblAccMain where id='"+id+"'");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							HashMap map=new HashMap();
							if(rs.next()) {
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
                            }
							result.retVal = map;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("VoucherMgt queryAccMain:",ex) ;
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
	
}
