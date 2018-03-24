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
 * ƾ֤ ���ݿ������
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2013-03-04
 * @Copyright: �������
 * @Author fjj
 */
public class VoucherMgt extends AIODBManager {

	/**
	 * ��ѯ�ֽ�����������Ŀ
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
	 * ��ѯ�ֽ�����������Ŀ
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
	 * ��ѯժҪ��
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
	 * ����ƾ֤�֣�����ڼ��ѯ��ǰ����ƾ֤��
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
	 * ����Id��ѯժҪ
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
	 * ���ժҪ��
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
	 * ɾ��ժҪ
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
	 * ��ѯ��ƿ�Ŀ
	 * @param classCode
	 * @param keywordSearch
	 * @param accCodeSearch
	 * @param accNameSearch
	 * @param accFullNameSearch
	 * @param allScopeRightList   ���Ʒ���Ȩ��
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
							
							/* ��ƿ�Ŀ���Ʒ���Ȩ�޿���*/
							String consql = "";
							if(allScopeRightList != null && allScopeRightList.size()>0){
								for(int i=0;i<allScopeRightList.size();i++){
									LoginScopeBean loginScope = (LoginScopeBean)allScopeRightList.get(i);
									if(loginScope!=null && "tblAccTypeInfo".equals(loginScope.getTableName())){
										//�������ÿ��Թ���Ŀ�Ŀ
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
							
							/* �ؼ��ֲ�ѯ*/
							if(keywordSearch!=null && !"".equals(keywordSearch)){
								sql.append(" and (AccNumber like '%"+keywordSearch+"%' or l.zh_CN like '%"+keywordSearch+"%' or AccFullName like '%"+keywordSearch+"%')");
							}
							/* ��ƿ�Ŀ*/
							if(accCodeSearch!=null && !"".equals(accCodeSearch)){
								sql.append(" and AccNumber='"+accCodeSearch+"'");
							}
							/* ��Ŀ����*/
							if(accNameSearch!=null && !"".equals(accNameSearch)){
								sql.append(" and l.zh_CN like '%"+accNameSearch+"%'");
							}
							/* ��Ŀȫ��*/
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
	 * ��ѯ��ƿ�Ŀ�ĺ������
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
									//������
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
								//�����ڼ�¼ �������δ����
								result.retVal = "[]";
								return;
							}else if(isForCur==1 && "".equals(currencyId)){
								//������Һ��� �������Ϊ�� ��ѯ���еı���
								sql.append("SELECT ID,CURRENCYNAME FROM tblCurrency where IsBaseCurrency!=1 order by IsBaseCurrency");
							}else if(!"".equals(currencyId)){
								//������ֲ�Ϊ�� ����currencyId��ѯ����
								sql.append("SELECT ID,CURRENCYNAME FROM tblCurrency where id in ('"+currencyId+"')");
							}
							//����SQL���
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
	 * ��ѯ��ƿ�Ŀ�ĺ�������
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
							//����queryMode��Σ�ֻҪ�ǿ�Ŀ�Ǻ�����Ŀ������ϼ�
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
										/* ��Ҫ����������λ*/
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
	 * ��ѯ���ֲ�ѯ����
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
	 * ���ƾ֤
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
				        		//����define�ļ�����֤ ���ǰ��֤
			        			if(!"printSave".equals(saveType)){
			        				/* ��ӡ����֤*/
			        				result = new DynDBManager().defineSql(conn, "add", false,tableName,(Hashtable)allTables, values,id, loginBean.getId(),defineInfo,resources,locale);
			        				if (result.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
						            	rs.setRetCode(result.getRetCode());
						            	rs.setRetVal(result.getRetVal());
						                BaseEnv.log.error("VoucherMgt Before add defineSql Error code = " 
						                		+result.getRetCode() + " Msg=" + result.getRetVal());
						                return;
						            }
			        			}
				            	//������������
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
												 //ת�������ַ�
												 Result sp_values =  GlobalsTool.conversionSpecialCharacter(childTb.getFieldInfos(),childMap) ;
												 childMap = (HashMap) sp_values.getRetVal() ;
												 
												//ִ�дӱ����
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
												
												//*****����������ֶ�****//
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
									/*�ж��Ƿ����ñ�׼������Ƿ���������Զ����*/
									//String standards = new GlobalsTool().getSysSetting("standardAcc");
									//if("false".equals(standards)){
										//�����ñ�׼����
										String sql = "select IsAuditing from tblAccMainSetting";
										Statement st = conn.createStatement();
										ResultSet rset = st.executeQuery(sql);
										int isAuditing = 0;
										if(rset.next()){
											isAuditing = rset.getInt("IsAuditing");
										}
										/* ���������*/
										if(isAuditing==0){
											//����
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
	 * ��Ӻ��޸Ĵ������
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
			/* ������Ϣ*/
			String isAuStr = GlobalsTool.getMessage(locale,"oa.job.approved")+" "+loginBean.getEmpFullName()+" "+dateTime+"<br />";
			String ispostStr = GlobalsTool.getMessage(locale,"muduleFlow.lb.voucher")+" "+loginBean.getEmpFullName()+" "+dateTime+"<br />";
			Statement st = conn.createStatement();
			ResultSet rset = null;
			boolean flagid = false;
			//����ƾ֤ʱ������֤
			String sql = "select tblAccMain.id,";
			sql += "acctype.cashClass,acctype.bankClass,acctype.equivalentClass from tblAccMain join ";
			sql += "tblAccDetail on tblAccMain.id=tblAccDetail.f_ref JOIN tblAccTypeInfo acctype ";
			sql += "ON acctype.AccNumber=tblAccDetail.AccCode where tblAccMain.id='"+values.get("id")+"'";
			rset = st.executeQuery(sql);
			if(rset.next()){
				Integer cashClass = rset.getInt("cashClass");   				//�ֽ��Ŀ��
				Integer bankClass = rset.getInt("bankClass");   				//���п�Ŀ��
				Integer equivalentClass = rset.getInt("equivalentClass");   	//�ֽ�ȼ�����
				//����
				if(cashClass == 1 || bankClass == 1 || equivalentClass == 1){
					//��Ŀ�����ֽ��Ŀ��,���п�Ŀ��,�ֽ�ȼ�����֮һ���Ը��˺ͷ�����
					flagid = true;
				}
			}
			String checkStr = "";
			if(flagid){
				checkStr = GlobalsTool.getMessage(locale,"ReportBillView.button.Check")+" "+loginBean.getEmpFullName()+" "+dateTime+"<br />";
			}
			/* �޸�Ϊ����˵�*/
			sql = "update tblAccMain set workflowNodeName='finish',isAuditing='finish',workFlowNode='-1',";
			sql += "approveRemark=isnull(approveRemark,'')+'"+isAuStr+ispostStr+checkStr+"'";
			if(flagid){
				sql += ",isReview=2,ReviewUser='"+loginBean.getId()+"'";
			}
			sql += ",approver='"+loginBean.getId()+"',postingUser='"+loginBean.getId()+"' where id='"+values.get("id")+"'";
			st.executeUpdate(sql);
			DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("tblAccMain_Add_One");
			
			//������ݵ�map��Ϊ
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
	 * �޸�ƾ֤
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
			        			/*�ж��Ƿ����ñ�׼������Ƿ���������Զ����*/
								String sql = "select IsAuditing from tblAccMainSetting";
								Statement st = conn.createStatement();
								ResultSet rset = st.executeQuery(sql);
								if(rset.next()){
									isAuditing = rset.getInt("IsAuditing");
								}
								/* ���������*/
								if(isAuditing==0){
									/* �ȷ����*/
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("tblAccMain_Del_One");
									//������ݵ�map��Ϊ
									sql = "SELECT BillDate,id,createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,isnull(workFlowNodeName,'') as workFlowNodeName from tblAccMain where workFlowNodeName='finish' and id='"+values.get("id")+"'";
									rset = st.executeQuery(sql);
									if(rset.next()){
										/* �ѹ��˵�ƾ֤ */
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
									/* ����define�ļ�����֤ �޸�ǰ��֤*/ 
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
				            	//�޸���������
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
										//ɾ��������
										KRLanguageQuery.delete(conn,childTb,values.get("id").toString(),"f_ref");
			                            //��ɾ���ӱ�
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
																									
												//ת�������ַ�
												Result sp_values =  GlobalsTool.conversionSpecialCharacter(childTb.getFieldInfos(),childMap) ;
												childMap = (HashMap) sp_values.getRetVal() ;
												
												//ִ�дӱ����
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
												
												//*****����������ֶ�****//
												cs.setString(29, String.valueOf(childMap.get("RowNum")));
												cs.setString(30, String.valueOf(childMap.get("Refs")));
												cs.setString(31, String.valueOf(childMap.get("MainItem")));
												cs.setString(32, String.valueOf(childMap.get("SecItem")));
												
												cs.registerOutParameter(33, Types.INTEGER);
												cs.registerOutParameter(34, Types.VARCHAR);
						                        cs.execute();
						                        
						                        //ȡ�����ص�����
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
									
									/* ִ��define�ļ���֤ �޸����*/
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
						            /* ���������*/
									if(isAuditing==0){
										//����
										dealbind(conn, locale, loginBean, values, resources);
									}
									
									/* �޸�ƾ֤�ţ�ʹƾ֤�ź�ԭ������һ�� */
									//zxy �޸�ʱҪ���Ե���ƾ֤��,
									//int orderNo = Integer.parseInt(values.get("oldOrderNo").toString());
									int orderNo = Integer.parseInt(values.get("OrderNo").toString());
									String oldcredTypeId = values.get("oldCredTypeID").toString();
									int credYear = Integer.parseInt(values.get("oldCredYear").toString());
									int period = Integer.parseInt(values.get("oldPeriod").toString());
									String credTypeId = values.get("CredTypeID").toString();
									String billDate = values.get("BillDate").toString();			//����
									String[] billdates = billDate.split("-");
									
									/**
									 * ���ƾ֤�ֺͻ���ڼ��꣬����ڼ���ͬʱ�޸�ƾ֤��Ϊ���ʱ��
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
	 * ���ݿ�Ŀ��Ų�ѯ��Ŀ��Ϣ
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
					//�������Ŀ
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
	 * ��ѯƾ֤�б�
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
                        sql.append("(case tblAccMain.CashFlag when '0' then '-' when '1' then '��ָ��' when '-1' then 'δָ��' end) as CashFlag,");
                        sql.append("row_number() over(order by tblAccMain.CredYear DESC,tblAccmain.Period DESC,tblAccMain.OrderNo DESC,d.detOrderNo) as row_id,");
                        sql.append("tblAccMain.BillDate as BillDate,tblAccMain.CredTypeID as CredTypeID,tblAccMain.CredTypeID+'_'+cast(tblAccMain.OrderNo as varchar(30)) as CredTypeIDs,tblAccMain.AcceNum as AcceNum,tblAccMain.RefBillType as RefBillType,");
                		sql.append("tblAccMain.CredNo as CredNo,tblAccMain.Period as Period,tblAccMain.EmployeeID as EmployeeID,tblAccMain.AcceptNumber as AcceptNumber,tblAccMain.AcceptMode as AcceptMode,");
                		sql.append("tblAccMain.isAuditing as isAuditing,tblAccMain.isReview as isReview,tblAccMain.RefBillID as RefBillID,tblAccMain.RefBillNo as RefBillNo,");
                		sql.append("tblAccMain.statusId as statusId,tblAccMain.CredYear as CredYear,tblAccMain.CredMonth as CredMonth,tblAccMain.workflowNodeName as workflowNodeName,");
                		sql.append("d.RefBillType as accDetailRefBillType,d.CompanyCode as CompanyCode,d.DepartmentCode as DepartmentCode,tblAccMain.workFlowNode as workFlowNode,");
                		sql.append("case tblAccMain.workFlowNodeName when 'finish' then '�ѹ���' when 'notApprove' then 'δ����' else '' end as workFlowNodeNames,");
                		sql.append("case tblAccMain.isReview when '1' then 'δ����' when '2' then '�Ѹ���' end as isReviews,");
                		sql.append("case tblAccMain.isAuditing when 'finish' then '�����' when 'noPass' then '��˲�ͨ��' when 'start' then 'δ���' else '' end as isAuditings,");
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
                		String searchCredYearStart = searchForm.getSearchCredYearStart();						//����ڼ俪ʼ��
                		String searchCredMonthStart = searchForm.getSearchCredMonthStart();						//����ڼ俪ʼ��
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
                		String searchRefBillType = searchForm.getSearchRefBillType();			//��������
                		String createByName=searchForm.getTblEmployee_EmpFullName();
                		String createBy=searchForm.getCreateBy();
                		
                		String searchAccType = searchForm.getSearchAccType();
                		
                		//��ѯ����
                		
                		/*ƾ֤����ɸѡ*/
                		if(searchAccType != null && "ref".equals(searchAccType)){
                			sql.append(" and tblAccMain.RefBillNo != '' ");
                		}
                		
                		if(searchAccType != null && "manual".equals(searchAccType)){
                			sql.append(" and (tblAccMain.RefBillNo = '' or tblAccMain.RefBillNo is null) ");
                		}
                		
                		/* ����ʱ�����*/
                		if(startTime != null && !"".equals(startTime)){
                			sql.append(" and tblAccMain.BillDate>='"+startTime+"'");
            		    }
                		if(endTime != null && !"".equals(endTime)){
                			sql.append(" and tblAccMain.BillDate<='"+endTime+"'");
                		}
                		
                		/* ƾ֤��*/
                		if(searchCredType != null && !"".equals(searchCredType)){ 
                			sql.append(" and tblAccMain.CredTypeID='"+searchCredType+"'");
                		}
                		/* ���ݱ�� */
                		if(searchRefBillNo != null && !"".equals(searchRefBillNo)){
                			sql.append(" and tblAccMain.RefBillNo like '%"+searchRefBillNo.trim()+"%'");
                		}
                		
                		/* ƾ֤��*/
                		if(searchOrderNoStart != null && !"".equals(searchOrderNoStart)){
                			sql.append(" and tblAccMain.OrderNo>="+searchOrderNoStart+"");
                		}
                		if(searchOrderNoEnd != null && !"".equals(searchOrderNoEnd)){
                			sql.append(" and tblAccMain.OrderNo<="+searchOrderNoEnd+"");
                		}
                		
                		/* ����ڼ�*/
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
                		 * ������Ŀ�Ŀ���뿪ʼ�ͽ�����ͬʱ�����⴦��
                		 */
                		if(searchAccCodeStart != null && !"".equals(searchAccCodeStart) && searchAccCodeEnd != null && !"".equals(searchAccCodeEnd) && searchAccCodeStart.equals(searchAccCodeEnd)){
                			sql.append(" and acctype.classCode like (select classCode from tblAccTypeInfo where AccNumber='"+searchAccCodeStart+"')+'%'");
                		}else{
                			/* ��Ŀ����*/
                			if(searchAccCodeStart != null && !"".equals(searchAccCodeStart)){
                				sql.append(" and acctype.classCode>=(select classCode from tblAccTypeInfo where AccNumber='"+searchAccCodeStart+"')");
                			}
                			if(searchAccCodeEnd != null && !"".equals(searchAccCodeEnd)){
                				sql.append(" and acctype.classCode<=(select classCode from tblAccTypeInfo where AccNumber='"+searchAccCodeEnd+"')");
                			} 
                		}
                		
                		/* ժҪ*/
                		if(searchRecordComment != null && !"".equals(searchRecordComment)){
                			sql.append(" and d.RecordComment like '%"+searchRecordComment+"%'");
                		}
                		
                		/* ����״̬*/
                		if(searchWName !=null && !"".equals(searchWName)){
                			sql.append(" and tblAccMain.workFlowNodeName='"+searchWName+"'");
                		}
                		/* ����״̬*/
                		if(searchReview != null && !"".equals(searchReview)){
                			sql.append(" and tblAccMain.isReview="+searchReview+"");
                		}
                		/* ���״̬*/
                		if(searchAuditing != null && !"".equals(searchAuditing)){
                			sql.append(" and tblAccMain.isAuditing='"+searchAuditing+"'");
                		}
                		
                		/* ���� */
                		if(depts != null && !"".equals(depts)){
                			sql.append(" and tblAccMain.departmentCode='"+depts+"'");
                		}
                		if(deptName != null && !"".equals(deptName)){
                			sql.append(" and (SELECT DeptFullName FROM tblDepartment WHERE tblDepartment.classCode=tblAccMain.departmentCode) like '%"+deptName+"%'");
                		}
                		/* ������ */
                		if(employees != null && !"".equals(employees)){
                			sql.append(" and tblAccMain.employeeId='"+employees+"'");
                		}
                		if(employeesName != null && !"".equals(employeesName)){
                			sql.append(" and (SELECT EmpFullName FROM tblEmployee WHERE tblEmployee.id=tblAccMain.employeeId) like '%"+employeesName+"%'");
                		}
                		/* ������ */
                		if(createBy != null && !"".equals(createBy)){
                			sql.append(" and tblAccMain.createBy='"+createBy+"'");
                		}
                		if(createByName != null && !"".equals(createByName)){
                			sql.append(" and (SELECT EmpFullName FROM tblEmployee WHERE tblEmployee.id=tblAccMain.createBy) like '%"+createByName+"%'");
                		}
                		/* ������λ */
                		if(searchClient != null && !"".equals(searchClient)){
                			sql.append(" and d.CompanyCode='"+searchClient+"'");
                		}
                		if(searchClientName != null && !"".equals(searchClientName)){
                			sql.append(" and (SELECT ComFullName from tblCompany WHERE tblCompany.classCode=d.CompanyCode) like '%"+searchClientName+"%'");
                		}
                		
                		/* �������� */
                		if(searchRefBillType != null && !"".equals(searchRefBillType)){
                			sql.append(" and ((SELECT zh_CN FROM tblLanguage tl WHERE tl.id=(SELECT tblDbTableInfo.languageId FROM tblDbTableInfo WHERE tblDbTableInfo.tableName=tblAccMain.RefBillType)) like '%"+searchRefBillType+"%' or tblAccMain.RefBillType like '%"+searchRefBillType+"%')");
                		}
                		
                		/* ��� */
                		if(searchMoneyStart != null && !"".equals(searchMoneyStart)){
                			sql.append(" and (d.DebitAmount+d.LendAmount >="+searchMoneyStart+") ");
                		}
                		if(searchMoneyEnd != null && !"".equals(searchMoneyEnd)){
                			sql.append(" and (d.DebitAmount+d.LendAmount <="+searchMoneyEnd+") ");
                		}
                		
                		/*���÷�ΧȨ��*/
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
	                        if(pageSize>0){//��ѯ������
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
	                        }else if(pageSize==-1111){//���ǵ���ʱȡ����id��
	                        	sqlStr = "select tblAccMain.id as id "+sql.substring(sql.indexOf(" from tblAccMain"));
	                        }else{
	                            sqlStr=sql.toString();
	                        }
	                        
                            if (pageNo2 < 1){
                            	pageNo2 = 1;
                            }
                            List list = new ArrayList();
                            BaseEnv.log.debug("VoucherMgt.queryList ��ѯƾ֤sql = "+sqlStr);
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
	 * ɾ��ƾ֤
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
							/*�ж��Ƿ����ñ�׼������Ƿ���������Զ����*/
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
								/* ��ѯ�Ƿ���ڸ���*/
					            ps.setString(1, str);
					            rset = ps.executeQuery();
								//ɾ��ǰ����define�ļ���֤
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
					            	/* ���������*/
									if(isAuditing==0 && "finish".equals(rset.getString("workFlowNodeName"))){
										/* ������*/
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
								
					            /* ɾ��*/
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
	 * ƾ֤�����޸�
	 * @param bean
	 * @return
	 */
	public Result updateSetting(AccMainSettingBean bean){
		return updateBean(bean);
	}
	
	/**
	 * ��ѯ����ƾ֤ģ��
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
							
							//����������Ϊnullʱ����Ӳ�ѯ����
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
	 * ���ƾ֤ģ��
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
			            	//�����������
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
					                    //��Ӵӱ����
					                    ires = new DynDBManager().execInert(conn, childTb.getTableName(),allTables, childMap,"",resources,locale);
					                    
					                    //����ʧ��
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
	 * ɾ��ƾ֤ģ��
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
					            //��ѯ�Ƿ���ڸ���
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
	 * ���(���ͨ��/��˲�ͨ��/�����)
	 * @param keyIds				ƾ֤ID	
	 * @param remark				��ע
	 * @param workflowNodeName  	���״̬
	 * @param approver				�����
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
							//����ƾ֤ʱ������֤
							String sql = "select isNull(workflowNodeName,'') as workflowNodeName,isNull(isAuditing,'') as isAuditing,CredTypeID,OrderNo,CredYear,CredMonth,Period from tblAccMain where id in ("+str+")";
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql);
							int i=0;
							while(rset.next()){
								String Auditing = rset.getString("isAuditing");
								//��֤ƾ֤�Ƿ��ǵ�ǰ����ڼ�֮ǰ
								Boolean flag = isAccPeriod(loginBean,rset.getInt("CredYear"),rset.getInt("CredMonth"),rset.getInt("Period"));
								if(flag){
									rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"�ڵ�ǰ�����ڼ�֮ǰ��������˲���");
									rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
									return;
								}
								if(Auditing!= null && !"".equals(Auditing)){
									if("noPass".equals(dealType)){
										//��˲�ͨ��
										if("noPass".equals(Auditing)){
											//�Ѿ�����˲�ͨ��
											//rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
											//		GlobalsTool.getMessage(locale,"erp.voucher.nopass.error"));
											//rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											//return;
										}else if("finish".equals(Auditing)){
											//���ͨ��
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
													GlobalsTool.getMessage(locale,"erp.voucher.yespass.error"));
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}else if("yesPass".equals(dealType)){
										//���ͨ��
										if("finish".equals(Auditing)){
//											//�����
//											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
//													GlobalsTool.getMessage(locale,"erp.voucher.yespass.error"));
//											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
//											return;
											continue;
										}
									}else if("reversePass".equals(dealType)){
										//�����
										if("noPass".equals(Auditing)){
											//��˲�ͨ��
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
													GlobalsTool.getMessage(locale,"erp.voucher.nopass.error"));
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}else if("start".equals(Auditing)){
											//δ���
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
								rs.setRetVal("��ѡƾ֤������ˣ���������ˣ�");
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
	 * ���(���ͨ��/��˲�ͨ��/�����)
	 * @param keyIds				ƾ֤ID	
	 * @param remark				��ע
	 * @param workflowNodeName  	���״̬
	 * @param approver				�����
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
	 * ���ˡ�������
	 * @param keyIds				ƾ֤ID
	 * @param remark				��ע
	 * @param workFlowNodeName     	�Ƿ����
	 * @param postingUser   		������
	 * @param settingBean   		ƾ֤����
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
							//���˺ͷ�����ʱ������֤������������ʱ�����˺ͷ����ˣ�
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
								String wFlowName = rset.getString("workFlowNodeName");		//�Ƿ����
								String workFlowNode = rset.getString("workFlowNode");	
								//��֤ƾ֤�Ƿ��ǵ�ǰ�����ڼ�֮ǰ
								Boolean flag = isAccPeriod(loginBean,rset.getInt("CredYear"),rset.getInt("CredMonth"),rset.getInt("Period"));
								if(flag){
									rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"�ڵ�ǰ�����ڼ�֮ǰ��������˲���");
									rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
									return;
								}
								if("binder".equals(dealType)){
									/* ����*/
									//�����˲�����˺�ѡ�����ù���ʱ�������
									if(settingBean.getIsAuditing()==1 && settingBean.getIsAccountAuditing() == 1){
										//ƾ֤����ǰ�������
										String auditing = rset.getString("isAuditing");
										if(!"finish".equals(auditing)){
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
													GlobalsTool.getMessage(locale, "erp.voucher.nostatus.error"));
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}
									if(settingBean.getIsCheck() == 1){
										//ƾ֤����ǰ������ɸ���
										Integer cashClass = rset.getInt("cashClass");   				//�ֽ��Ŀ��
										Integer bankClass = rset.getInt("bankClass");   				//���п�Ŀ��
										Integer equivalentClass = rset.getInt("equivalentClass");   	//�ֽ�ȼ�����
										Integer isReview = rset.getInt("isReview");						//�Ƿ񸴺ˣ�1δ���ˣ�
										if(isReview == 1){
											if(cashClass == 1 || bankClass == 1 || equivalentClass == 1){
												//�����ֽ��������Ŀ����δ����
												rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"δ���ˣ�������˲���");
												rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
												return;
											}
										}
									}
									if("finish".equals(wFlowName) || "-1".equals(workFlowNode)){
//										//�ѹ����޷��ظ�����
//										rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+
//												GlobalsTool.getMessage(locale,"erp.voucher.binder.error"));
//										rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
//										return;
										continue;
									}
								}else if("reverseBinder".equals(dealType)){
									/* ������*/
									if(!"finish".equals(wFlowName) && "0".equals(workFlowNode)){
										//δ�����޷�������
										rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"δ���ˣ�������˲���");
										rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
										return;
									}
								}
								
								//������ݵ�map��Ϊ
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
								rs.setRetVal("��ѡƾ֤���ѹ��ˣ������ٹ��ˣ�");
								rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
								return;
							}
							sql = new StringBuffer("update tblAccMain set workFlowNodeName=?,approveRemark=isnull(approveRemark,'')+?");
							
							if("binder".equals(dealType)){
								//����
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
							
							/* ���������defineAcc.xml�ļ����й��˺�������*/
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
			                        
									//ȡhashMap�е�����
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
	 * ���ݹ����򵼽��й���
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
							//ָ������֮ǰ����
							if(voucherarea == 2){
								countSql.append("and tblAccMain.BillDate<='"+vouchertime+"'");
							}
							//ָ���ڼ����
							if(voucherarea == 3){
								countSql.append("and tblAccMain.CredYear="+voucherPeriodYear+" and tblAccMain.Period="+voucherPeriod);
							}
							//��������˲���ѡ����ƾ֤����ǰ�������
							if(settingBean.getIsAccountAuditing()==1){
								countSql.append(" and tblAccMain.isAuditing='finish'");
							}else{
								countSql.append(" and tblAccMain.isAuditing!='noPass'");
							}
							
							countSql.append(" and tblAccMain.CredTypeID='"+credTypeid+"' ");
							//��ѯ����
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
								String id = rset.getString("id");			//ƾ֤ID
								Integer OrderNo = rset.getInt("OrderNo");		//ƾ֤��
								
								//��֤ƾ֤�Ƿ��ǵ�ǰ�����ڼ�֮ǰ
								Boolean flag = isAccPeriod(loginBean,rset.getInt("CredYear"),rset.getInt("CredMonth"),rset.getInt("Period"));
								if(flag){
									continue;
								}
								
								if(iscredNo == 1 && !"".equals(accMainId) && !id.equals(accMainId) 
										&& crednumber != 0 && OrderNo!=crednumber+1){
									//ƾ֤�Ų�����ʱֹͣ����
									rs.setRetVal(rset.getString("CredTypeID")+"-"+OrderNo+"��������������˲���");
									rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
									break ;
								}
								
								if(settingBean.getIsCheck() == 1){
									//ƾ֤����ǰ������ɸ���
									Integer cashClass = rset.getInt("cashClass");   				//�ֽ��Ŀ��
									Integer bankClass = rset.getInt("bankClass");   				//���п�Ŀ��
									Integer equivalentClass = rset.getInt("equivalentClass");   	//�ֽ�ȼ�����
									Integer isReview = rset.getInt("isReview");						//�Ƿ񸴺�
									if(isReview == 1){
										if(cashClass == 1 || bankClass == 1 || equivalentClass == 1){
											//��Ŀ�����ֽ��Ŀ��,���п�Ŀ��,�ֽ�ȼ�����֮һ���Ը��˺ͷ����� ����δ����
											if(postingerror == 1){
												//��������ֹͣ����
												rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"δ���ˣ�������˲���");
												rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
												return;
											}else{
												errorsum++;
												continue;
											}
										}
									}
								}
								
								//�Ƿ����
								String auditing = rset.getString("isAuditing");
								String remarkStr = "";
								//����
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
									
									/* ���������defineAcc.xml�ļ����й��˺�������*/
									String defineName = "tblAccMain_Add_One";
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
									if (defineSqlBean == null) {
			                            BaseEnv.log.error("Define Sql Not Exist :Name = "+defineName);
			                            rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
			                            return  ;
			                        }
									
									//����define����Ҫ�Ĳ���
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
			                        new DynDBManager().addLog(14, loginBean.getId(), loginBean.getEmpFullName(), loginBean.getSunCmpClassCode(),"ƾ֤����:"+"["+rset.getString("CredTypeID")+"-"+rset.getString("CredNo")+"]"+"���˳ɹ�;","tblAccMain", "ƾ֤����","ƾ֤����");
								}
		                        accMainId = id;
								crednumber = OrderNo;
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        	rs.setRetVal("������ƾ֤"+total+"�ţ���ȷ����"+successnum+"�ţ���������"+errorsum+"��");
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
	 * ����/������
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
							//����ƾ֤ʱ������֤
							String sql = "select tblAccMain.id,tblAccMain.CredTypeID,tblAccMain.OrderNo,isnull(tblAccMain.workFlowNode,'0') as workFlowNode,isnull(tblAccMain.isReview,1) as isReview,tblAccMain.CredYear,tblAccMain.CredMonth,tblAccMain.Period,";
							sql += "acctype.cashClass,acctype.bankClass,acctype.equivalentClass,isnull(tblAccMain.workFlowNodeName,'') as workFlowNodeName from tblAccMain join ";
							sql += "tblAccDetail on tblAccMain.id=tblAccDetail.f_ref JOIN tblAccTypeInfo acctype ";
							sql += "ON acctype.AccNumber=tblAccDetail.AccCode where tblAccMain.id in ("+str+")";
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql);
							while(rset.next()){
								Integer cashClass = rset.getInt("cashClass");   				//�ֽ��Ŀ��
								Integer bankClass = rset.getInt("bankClass");   				//���п�Ŀ��
								Integer equivalentClass = rset.getInt("equivalentClass");   	//�ֽ�ȼ�����
								//��֤ƾ֤�Ƿ��ǵ�ǰ�����ڼ�֮ǰ
								Boolean flag = isAccPeriod(loginBean,rset.getInt("CredYear"),rset.getInt("CredMonth"),rset.getInt("Period"));
								if(flag){
									rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"�ڵ�ǰ�����ڼ�֮ǰ��������˲���");
									rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
									return;
								}
								
								//���˻򷴸���
								if(cashClass == 1 || bankClass == 1 || equivalentClass == 1){
									Integer isReview = rset.getInt("isReview");
									String workFlowNodeName = rset.getString("workFlowNodeName");		//���˱�ʶ
									String workFlowNode = rset.getString("workFlowNode");
									//����
									if("check".equals(dealType)){
										//�Ѹ����޷��ڸ���
										if(isReview == 2){
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"�Ѹ��ˣ�������˲���");
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}else if("reverseCheck".equals(dealType)){
										//������
										if(isReview == 1){
											rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"δ���ˣ�������˲���");
											rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
											return;
										}
									}
									//�ѹ����޷�����
									if("finish".equals(workFlowNodeName) || "-1".equals(workFlowNode)){
										rs.setRetVal(rset.getString("CredTypeID")+"-"+rset.getInt("OrderNo")+"�ѹ��ˣ�������˲���");
										rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
										return;
									}
									//��Ŀ�����ֽ��Ŀ��,���п�Ŀ��,�ֽ�ȼ�����֮һ���Ը��˺ͷ�����
									ids += "'"+rset.getString("id")+"',";
								}
							}
							if(ids.length()==0){
								rs.setRetVal("ƾ֤���踴�˻򷴸��ˣ�");
								rs.setRetCode(ErrorCanst.RET_HAS_AUDITING);
								return;
							}else{
								if(",".equals(ids.substring(ids.length()-1))){
									ids = ids.substring(0, ids.length()-1);
								}
							}
							sql = "update tblAccMain set isReview=?,approveRemark=isnull(approveRemark,'')+?,ReviewUser=? ";
							
							/* ��������޸��˽���źͽ��㷽ʽ�����޸�*/
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
	 * ��ѯ������
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
	 * ��ѯƾ֤����
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
	 * ƾ֤����
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
	 * ��ѯƾ֤�ŶϺ����
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
	 * ����ƾ֤
	 * @param CredTypeID
	 * @param periodYear
	 * @param period
	 * @return
	 */
	public Result resetOrderNo(final String CredTypeID,final Integer periodYear,final Integer period){
		
		String credNoStr = "";
		/* ȡ��ȱ��ƾ֤��*/
		AccPeriodBean accPeriodBean = new AccPeriodBean();
		accPeriodBean.setAccYear(periodYear);
		accPeriodBean.setAccPeriod(period);
		Result result = queryStopCredNoList(CredTypeID, accPeriodBean,"");
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			map = (HashMap<Integer, Integer>)result.retVal;
		}
		final Map<Integer,Integer> maps = map;									//δ���ڵ�ƾ֤��
		
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							//��ѯ�Ƿ����������
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
								//���������
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
	 * ����Id��ѯƾ֤ģ��
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
	 * �ж�ƾ֤�Ƿ��ڻ���ڼ�ǰ
	 * @param loginBean
	 * @param year
	 * @param month
	 * @param period
	 * @return
	 */
	public Boolean isAccPeriod(LoginBean loginBean, Integer year, Integer month, Integer period){
		//����ڼ�
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
	 * ��ѯδ��˺�δ���˵�ƾ֤
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
	 * ��ѯ����ڼ�
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
	 * ��ѯ��ǰ�����ڼ�
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
	 * ����id��ѯƾ֤����
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
