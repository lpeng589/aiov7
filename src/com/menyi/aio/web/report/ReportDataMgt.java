package com.menyi.aio.web.report;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.bsf.BSFManager;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.web.util.*;

public class ReportDataMgt extends DBManager {
	
	public Result deleteAdanceCond(final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "";
						try {
							sql = "delete from tblReportCond where id=?";
							PreparedStatement st = conn.prepareStatement(sql);			
							st.setString(1, id);
							st.execute();
						} catch (Exception ex) {
							BaseEnv.log.debug("ReportDataMgt.deleteAdanceCond sql ="+sql,ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	
	public Result defaultAdanceCond(final String id,final String reportNumber, final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "";
						try {
							sql = "update tblReportCond set defCond=0 where reportNumber=? and userId=?";
							PreparedStatement st = conn.prepareStatement(sql);			
							st.setString(1, reportNumber);
							st.setString(2, userId);
							st.execute();
							if(id != null && id.length() > 0){
								sql = "update tblReportCond set defCond=1 where reportNumber=? and userId=? and id=? ";
								st = conn.prepareStatement(sql);			
								st.setString(1, reportNumber);
								st.setString(2, userId);
								st.setString(3, id);
								st.execute();
							}
						} catch (Exception ex) {
							BaseEnv.log.debug("ReportDataMgt.deleteAdanceCond sql ="+sql,ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	public Result queryAdanceCond(final String reportNumber, final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "";
						try {
							sql = "select id,name,defCond from tblReportCond where reportNumber=? and userId=?";
							PreparedStatement st = conn.prepareStatement(sql);			
							st.setString(1, reportNumber);
							st.setString(2, userId);
							ResultSet rss = st.executeQuery();
							ArrayList list = new ArrayList();
							while (rss.next()) {
								list.add(new String[]{rss.getString(1),rss.getString(2),rss.getString(3)});
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							BaseEnv.log.debug("ReportDataMgt.queryAdvanceCond sql ="+sql,ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	public Result loadDefAddanceCond(final String reportNumber, final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql ="select field,value,hide_value from tblReportCondItem a join tblReportCond b on a.condId=b.id  where reportNumber=? and userId=? and b.defCond=1 order by a.id";
							PreparedStatement st = conn.prepareStatement(sql);
							st.setString(1, reportNumber);
							st.setString(2, userId);
							ResultSet rss = st.executeQuery();
							ArrayList list = new ArrayList();
							while (rss.next()) {
								list.add(new String[]{rss.getString(1),rss.getString(2),rss.getString(3)});
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.loadDefAddanceCond Error",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	public Result loadAddanceCond(final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select name from tblReportCond where id=?";
							PreparedStatement st = conn.prepareStatement(sql);
							st.setString(1, id);
							ResultSet rss = st.executeQuery();
							String name = "";
							if(rss.next()){
								name = rss.getString(1);
							}
							sql ="select groupName,field,value,hide_value from tblReportCondItem where condId=? order by id";
							st = conn.prepareStatement(sql);
							st.setString(1, id);
							rss = st.executeQuery();
							ArrayList list = new ArrayList();
							while (rss.next()) {
								list.add(new String[]{rss.getString(1),rss.getString(2),rss.getString(3),rss.getString(4)});
							}
							rs.setRetVal(new Object[]{name,list});
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.loadAddanceCond Error:",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	public Result saveAdvanceCond(final String reportNumber, final String id, final String userId, final String name,final ArrayList<String[]> list) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "";
						try {
							if(id != null && id.length() > 0){
								//先删除
								sql  = " delete from tblReportCond where id = ? ";
								PreparedStatement st = conn.prepareStatement(sql);
								st.setString(1, id);
								st.execute();
							}
							sql  = "insert into tblReportCond(reportNumber,name,userid) values(?,?,?) ";
							PreparedStatement st = conn.prepareStatement(sql);
							st.setString(1, reportNumber);
							st.setString(2, name);
							st.setString(3, userId);
							st.execute();
							sql  = " select @@identity ";
							st = conn.prepareStatement(sql);
							ResultSet rss = st.executeQuery();
							int newId = 0;
							if(rss.next()){
								newId = rss.getInt(1);
							}else{
								rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								return;
							}
							for(String[] ss:list){
								sql = "insert into tblReportCondItem(condId,groupName,field,value,hide_value) values(?,?,?,?,?) ";
								st =conn.prepareStatement(sql);
								st.setInt(1, newId);
								st.setString(2, ss[0]);
								st.setString(3, ss[1]);
								st.setString(4, ss[2]);
								st.setString(5, ss[3]);
								st.execute();
							}
							rs.retVal = newId;
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.saveAdvanceCond Error :"+sql,ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	
	public boolean testFilterSQL(final String tsql ,final String billId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql;
						try {
							Statement st = conn.createStatement();							
							sql = tsql;
							sql = sql.replaceAll("@ValueOfDB:id", "'"+billId+"'");
							ResultSet rss = st.executeQuery(sql);
							ArrayList formatList = new ArrayList();
							if (rss.next()) {
								rs.retVal = "true";
							}else{
								rs.retVal = "false";
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.testFilterSQL 打印样式过滤sql 错误",ex);
							
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		if(retCode == ErrorCanst.DEFAULT_SUCCESS && "false".equals(rs.retVal)){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 取单据打印的所有样式
	 * @param reportNumber
	 * @param locale
	 * @param userId
	 * @param deptId
	 * @return
	 */
	public Result getBillFormatList(final String tableName,final String moduleType, final String locale, final String userId, final String deptId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							String deptCode = deptId;
							String tempSQL = "";
							while (deptCode != null && deptCode.length() > 0) {
								tempSQL += " or ','+deptIds+',' like '%," + deptCode + ",%'";
								deptCode = deptCode.substring(0, deptCode.length() - 5);
							}
							String sql = "select FormatName, FormatFileName,a.id,a.reportNumber,filterSQL from tblReportsDet d join tblReports a on a.id=d.f_ref " + 
							"where languageType='" + locale + "' and a.reportType='BILL' and a.billTable='"+tableName+"' and (isnull(a.moduleType,'') = '"+(moduleType==null?"":moduleType)+"' or  isnull(a.moduleType,'') = '' )  "
									+ "and ('1'='" + userId + "' or userIds like '%" + userId + ",%' " + tempSQL + " or (len(isnull(userIds,''))=0 and len(isnull(deptIds,''))=0)) order by FormatName";
							ResultSet rss = st.executeQuery(sql);
							ArrayList formatList = new ArrayList();
							while (rss.next()) {
								formatList.add(new String[]{rss.getString(1),rss.getString(2),rss.getString(3),rss.getString(4),rss.getString(5)});
							}
							rs.setRetVal(formatList);
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getBillFormatList Error ",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	
	public Result getFormatList(final String reportNumber, final String locale, final String userId, final String deptId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							String deptCode = deptId;
							String tempSQL = "";
							while (deptCode != null && deptCode.length() > 0) {
								tempSQL += " or ','+deptIds+',' like '%," + deptCode + ",%'";
								deptCode = deptCode.substring(0, deptCode.length() - 5);
							}
							String sql = "select FormatName, FormatFileName,a.id,a.reportNumber,filterSQL from tblReportsDet d join tblReports a on a.id=d.f_ref  " + 
								"where languageType='" + locale + "' and FormatFileName like '" + reportNumber + "Format_%' "
									+ "and ('1'='" + userId + "' or userIds like '%" + userId + ",%' " + tempSQL + " or (len(isnull(userIds,''))=0 and len(isnull(deptIds,''))=0)) order by FormatName";
							ResultSet rss = st.executeQuery(sql);
							ArrayList formatList = new ArrayList();
							while (rss.next()) {
								formatList.add(new String[]{rss.getString(1),rss.getString(2),rss.getString(3),rss.getString(4),rss.getString(5)});
							}
							rs.setRetVal(formatList);
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getFormatList ",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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

	public Result getClassCode(final String tableName,final ArrayList<String> valueList){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select classCode,isCatalog  from " + tableName + " where 1=1 ";
							
							ArrayList<String> param = new ArrayList<String>();
							for(String s:valueList){
								String fs = "";
								String fieldName = s.substring(0,s.indexOf(":"));
								
								//检查fieldName是否是多语言字段
								boolean ismorel = false;
								DBFieldInfoBean dfb = GlobalsTool.getFieldBean(fieldName);
								if(dfb != null &&dfb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE){
									ismorel = true;
								}
								if(ismorel){
									String[] values = s.substring(s.indexOf(":")+1).split(",");
									for(String value:values){
										fs +=" or "+fieldName +" in (select id from tbllanguage where zh_CN like ? )";
										param.add(value);
									}
									if(fs.length() > 0){
										fs = fs.substring(3);
										sql += " and ("+fs+")";
									}
								}else{
									String[] values = s.substring(s.indexOf(":")+1).split(",");
									for(String value:values){
										fs +=" or "+fieldName +" like ?";
										param.add(value);
									}
									if(fs.length() > 0){
										fs = fs.substring(3);
										sql += " and ("+fs+")";
									}
								}
							}
							PreparedStatement st = conn.prepareStatement(sql);	
							BaseEnv.log.debug("ReportDataMgt.getClassCode 自动取条件classCode sql :"+sql);
							for(int i = 0;i<param.size();i++){
								st.setString(i+1, "%"+param.get(i)+"%");
								BaseEnv.log.debug("ReportDataMgt.getClassCode 自动取条件classCode 参数"+(i+1)+" :"+param.get(i));
							}							
							ResultSet rss = st.executeQuery();
							HashMap<String,String[]> sameMap = new HashMap<String,String[]>();
							while (rss.next()) {
								String cls = rss.getString(1);
								String isc = rss.getString(2);
								boolean found = false;
								for(String s:sameMap.keySet()){
									if(cls.startsWith(s)){
										found = true;
									}
								}
								if(!found){
									sameMap.put(cls,new String[]{cls,isc});
									//检查原来的是否有包含关系，如果有删除
									ArrayList<String> dels = new ArrayList<String>();
									for(String s:sameMap.keySet()){
										if(s.startsWith(cls) && !s.equals(cls)){
											dels.add(s);
										}
									}
									for(String d :dels){
										sameMap.remove(d);
									}
									
								}			
								BaseEnv.log.debug("ReportDataMgt.getClassCode 自动取条件classCode 结果 :"+cls);
							}							
							//如果有包含的子级，则去掉
							
							rs.retVal = sameMap.values();
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getClassCode error",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	
	private Result getClassCode(final String tableName,final String fieldName,final String isCatalog,final String value){
		final Result rs = new Result();
		if(value == null || value.length() ==0){
			rs.retVal = "1=1";
			return rs;
		}
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select classCode  from " + tableName + " where "+fieldName + " like ? and isCatalog = 0";
							PreparedStatement st = conn.prepareStatement(sql);	
							st.setString(1, "%"+value+"%");
							ResultSet rss = st.executeQuery();
							String cls = "";
							while (rss.next()) {
								cls +=",'"+rss.getString(1)+"'";
							}
							if(cls.length() > 0){
								cls = cls.substring(1);
								cls = tableName+".classCode in("+cls+")";
							}
							if("true".equals(isCatalog)){ //这是要包括目录的
								sql = "select classCode  from " + tableName + " where "+fieldName + " like ? and isCatalog = 1";
								st = conn.prepareStatement(sql);
								st.setString(1, "%"+value+"%");
								rss = st.executeQuery(); 
								while (rss.next()) {
									cls += " or "+ tableName+".classCode like '"+rss.getString(1)+"%'";
								}								
							}
							if(cls.startsWith(" or")){
								cls = cls.substring(3);
							}
							if(cls.equals("")){
								cls = " 1 != 1";
							}
							rs.retVal = cls;
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getClassCode error",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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

	public Result getReportData(final DefineReportBean reportBean, String sqlOld, final ArrayList<String> paramList, final String locale, final String reportType, final int showTotalStat,
			final String from, final int pageSize, final int pageNo, final int showTotalPage, final String statField, final int crossColNum, final String formType, final LoginBean loginBean) {
		if(sqlOld .length() ==0){
			//这种请况出现在报表，默认不显示，且未点查询时
			Result rs = new Result();
			rs.retVal=new ArrayList();
			return rs;
		}
		//处理toClassCode函数  and (tblStock.StockFullName =toClassCode(tblStock,StockFullName,true,@condition:StockFullName))
		Pattern pattern = Pattern.compile("getClassCode\\(([^,]*),([^,]*),([^,]*),\\'([^\\']*)\\'\\)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sqlOld);
		boolean hasTable=false;
		while (matcher.find()) {
			String all = matcher.group(0);
			String tableName = matcher.group(1);
			String fieldName = matcher.group(2);
			String isCatalog = matcher.group(3);
			String value = matcher.group(4);
			Result cls = getClassCode(tableName, fieldName, isCatalog, value);
			if(cls.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return cls;
			}
			String clstr = cls.retVal+"";
			
			clstr = "("+clstr+")";
			sqlOld = sqlOld.substring(0,sqlOld.indexOf(all))+clstr + sqlOld.substring(sqlOld.indexOf(all)+all.length());
		}
			
		final String sql =  sqlOld;
		BaseEnv.log.debug("ReportDataMgt.getReportData SQL="+sql);
		final Result rs = new Result();
		int retCode = DBUtil.execute(ReportData.TABLELIST.equals(reportType) || ReportData.BILL.equals(reportType)  ? "" : "reportDB", new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							long time = System.currentTimeMillis();
							ResultSet rss = null;
							int paramPos = 0;
							PreparedStatement st2 = null;
							if (reportType.equals("PROCLIST")) {
								st2 = conn.prepareCall(sql);
								st2.setInt(1, pageNo);
								st2.setInt(2, pageSize);
								st2.setString(3, locale);
								st2.setString(4, loginBean.getId());
								st2.setString(5, loginBean.getSunCmpClassCode());
								BaseEnv.log.debug("ReportDataMgt.getReportData 参数1:" + pageNo);
								BaseEnv.log.debug("ReportDataMgt.getReportData 参数2:" + pageSize);
								BaseEnv.log.debug("ReportDataMgt.getReportData 参数3:" + locale);
								BaseEnv.log.debug("ReportDataMgt.getReportData 参数4:" + loginBean.getId());
								BaseEnv.log.debug("ReportDataMgt.getReportData 参数5:" + loginBean.getSunCmpClassCode());
								paramPos = 5;
							} else
								st2 = conn.prepareStatement(sql);

							if (reportType.equals("BILL") && sql.indexOf("?") < 0) {
								rs.setRetCode(ErrorCanst.RET_REPORTSQL_BillREPORTNOTSETID);
								return;
							}
							for (int i = 1; i <= paramList.size(); i++) {
								st2.setString(paramPos + i, String.valueOf(paramList.get(i - 1)));
								BaseEnv.log.debug("ReportDataMgt.getReportData 参数" + (paramPos + i) + ":" + String.valueOf(paramList.get(i - 1)));
							}
							rss = st2.executeQuery();
							SQLWarning warn = st2.getWarnings();
							while (warn != null) {
								BaseEnv.log.debug("ReportDataMgt.getReportData warn " + warn.getMessage());
								warn = warn.getNextWarning();
							}

							ArrayList<HashMap<String, String>> rows = new ArrayList<HashMap<String, String>>();
							ArrayList<HashMap<String, String>> chartRow = new ArrayList<HashMap<String, String>>();
							KRLanguageQuery klQuery = new KRLanguageQuery();
							String zeroExport = GlobalsTool.getSysSetting("zeroExport");
							while (rss.next()) {
								HashMap<String, String> values = new HashMap<String, String>();
								HashMap<String, String> chartValues = new HashMap<String, String>();
								for (int i = 0; i < reportBean.getQueryFields().size(); i++) {
									ReportField field = (ReportField) reportBean.getQueryFields().get(i);
									String value = rss.getString(field.getAsFieldName());
									//检查本列是否是为零不显示列  导出时 系统配置是否启用导出时为0不显示
									if (field.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE && value != null && value.indexOf("@EVAL") != 0) {
										try {
											new BigDecimal(value.trim()).doubleValue();
										} catch (Exception e1) {
											BaseEnv.log.debug("ReportDataMgt.getReportData **************报表字段:" + field.getFieldName() + ";" + field.getAsFieldName() + "数据类型错误的指定为双精度型");
										}
									}
									if (((!"export".equals(formType) && field.getZeroDisplay() == 1) || ("export".equals(formType) && "true".equals(zeroExport)))
											&& field.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE && value != null && value.indexOf("@EVAL") != 0 && new BigDecimal(value.trim()).doubleValue() == 0) {
										value = "";
									}
									if (value != null) {
										if (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
											String key = value;
											if (value.indexOf("&nbsp;") >= 0) {
												key = value.replaceAll("&nbsp;", "");
											}
											klQuery.addLanguageId(key);
										} else if (field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
											if (field.getFieldName().indexOf(".") > 0) {
												DBFieldInfoBean f = GlobalsTool.getFieldBean(field.getFieldName().substring(0, field.getFieldName().indexOf(".")), field.getFieldName().substring(
														field.getFieldName().indexOf(".") + 1));
												if (f != null && f.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
													String key = value;
													if (value.indexOf("&nbsp;") >= 0) {
														key = value.replaceAll("&nbsp;", "");
													}
													klQuery.addLanguageId(key);
												}
											}
										} else if (!"print".equals(formType) && ((reportBean.isCross() && reportBean.getDisFields().indexOf(reportBean.getCrossField()) > -1) || crossColNum > 0)) {
											if (value.indexOf("&") >= 0) {
												value = value.replaceAll("&", "&amp;");
											}
											if (value.indexOf("\"") >= 0) {
												value = value.replaceAll("\"", "&quot;");
											}
											if (value.indexOf("<") >= 0) {
												value = value.replaceAll("<", "&lt;");
											}
											if (value.indexOf(">") >= 0) {
												value = value.replaceAll(">", "&gt;");
											}
											value = GlobalsTool.replaceSpecLitter(value);
										}
									}
									values.put(field.getAsFieldName(), value);
									if (value != null
											&& (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE || (field.getPopUpName() != null && field.getPopUpName().length() > 0 && field.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT))) {
										value = GlobalsTool.getEnumerationItemsDisplay(field.getPopUpName(), value, locale);
										if (value.length() == 0) {
											value = "";
										}
									}

									chartValues.put(field.getAsFieldName(), value);
								}
								rows.add(values);
								chartRow.add(chartValues);
							}
							boolean haveNext = false;
							if ("page".equals(formType) && pageSize != -1) {
								int currPageSize = pageSize;
								if ((reportBean.isCross() && reportBean.getDisFields().indexOf(reportBean.getCrossField()) > -1) || crossColNum > 0) {
									currPageSize = reportBean.getPageSizeE() - reportBean.getPageSizeS() + 1;
								}
//								存储过程报表中，如果最后一行有标识haveNext,则说明有下一页
								HashMap lastRow=rows.size()>1?rows.get(rows.size()-1):null;
								;
								if (showTotalPage == 0) {
									if ((rows.size() > currPageSize)||(lastRow!=null&&lastRow.containsValue("haveNext"))) {
										rs.setTotalPage(pageNo + 1);
									} else {
										rs.setTotalPage(pageNo);
									}
									rs.setRealTotal(0);
								}
								
								
								
								if ((rows.size() > currPageSize)|| (lastRow!=null&&lastRow.containsValue("haveNext"))) {
									haveNext = true;
									rows.remove(rows.size() - 1);
								}

							}

							BaseEnv.log.debug("通过SQL读原始数据：" + (System.currentTimeMillis() - time));
							time = System.currentTimeMillis();
							/*组织图形报表数据*/
							ArrayList<DefineChartBean> chartList = reportBean.getChartList();
							if (chartList != null && chartList.size() > 0 && chartRow.size() > 0) {
								ArrayList<String> fusionList = new ArrayList<String>();
								for (DefineChartBean chartBean : chartList) {
									ArrayList<String> xList = chartBean.getXAxis();
									ArrayList<String> yList = chartBean.getYAxis();
									HashMap chartMap = new HashMap();
									FusionBean fusionBean = new FusionBean();
									fusionBean.setType(chartBean.getType());
									fusionBean.setCaption(chartBean.getChartName());
									fusionBean.setXAxis(chartBean.getXAxisName());
									fusionBean.setYAxis(chartBean.getYAxisName());
									String[] categorys = null;
									if (xList.size() > 1 && yList.size() == 1) {
										for (String x : xList) {
											chartMap.put(x, new ArrayList<String>());
										}
									}
									if (yList.size() > 1 && xList.size() == 1) {
										for (String y : yList) {
											chartMap.put(y, new ArrayList<String>());
										}
									}
									categorys = new String[rows.size()];
									for (int i = 0; i < chartRow.size(); i++) {
										HashMap values = (HashMap) chartRow.get(i);
										if (xList.size() == 1 && yList.size() == 1) {
											if ("STATTABLE".equals(reportType)) {
												chartMap.put(values.get(statField) == null ? "" : values.get(statField), values.get(yList.get(0)));
											} else {
												chartMap.put(values.get(xList.get(0)) == null ? "" : values.get(xList.get(0)), values.get(yList.get(0)));
											}
										}
										/*X轴多列*/
										if (xList.size() > 1 && yList.size() == 1) {
											categorys[i] = (String) values.get(yList.get(0));
											for (String str : xList) {
												ArrayList<String> chart = (ArrayList<String>) chartMap.get(str);
												chart.add((String) values.get(str));
											}
										}
										/*Y轴多列*/
										if (yList.size() > 1 && xList.size() == 1) {
											categorys[i] = values.get(xList.get(0)) == null ? "" : (String) values.get(xList.get(0));
											for (String str : yList) {
												ArrayList<String> chart = (ArrayList<String>) chartMap.get(str);
												chart.add((String) values.get(str));
											}
										}
									}
									fusionBean.setValues(chartMap);
									fusionBean.setCategorys(categorys);
									fusionList.add(fusionBean.getCaption() + "@koron@" + fusionBean.getType() + "@koron@" + FusionCharts.getChart(fusionBean, reportBean.getQueryFields()));
								}
								reportBean.setFusionList(fusionList);
							}

							HashMap map = klQuery.query(conn);
							for (int i = 0; i < rows.size(); i++) {
								HashMap values = (HashMap) rows.get(i);
								for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
									ReportField field = (ReportField) reportBean.getQueryFields().get(j);
									String value = (String) values.get(field.getAsFieldName());

									if (value != null) {
										if (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
											String key = value;
											if (value.indexOf("&nbsp;") >= 0) {
												key = value.replaceAll("&nbsp;", "");
											}
											KRLanguage lan = (KRLanguage) map.get(key);
											if (lan != null) {
												value = value.replace(key, lan.get(locale));
											}
											values.put(field.getAsFieldName(), value);
										} else if (field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
											if (field.getFieldName().indexOf(".") > 0) {
												DBFieldInfoBean f = GlobalsTool.getFieldBean(field.getFieldName().substring(0, field.getFieldName().indexOf(".")), field.getFieldName().substring(
														field.getFieldName().indexOf(".") + 1));
												if (f != null && f.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
													String key = value;
													if (value.indexOf("&nbsp;") >= 0) {
														key = value.replaceAll("&nbsp;", "");
													}
													KRLanguage lan = (KRLanguage) map.get(key);
													if (lan != null) {
														value = value.replace(key, lan.get(locale));
													}
													values.put(field.getAsFieldName(), value);
													klQuery.addLanguageId(key);
												}
											}
										}
									}
								}
							}
							
                            //找最后一个重复显示的列
                            int lastHidCol=0;
                            for(int j=0;j<reportBean.getDisFields().size();j++){        		   
                        		   ReportField field = (ReportField) reportBean.getDisFields().get(j);
                        		   if(field.getRepeatNotShow()!=1){     
                        			   lastHidCol=j;
                        			   break;
                        		   }
                            }
                            HashMap lastValue=rows.size()>0?(HashMap)rows.get(0):null; //记录上一次重复的单
                            
                            for(int i=1;i<rows.size() && lastHidCol > 0;i++){
	                           	   HashMap values=(HashMap)rows.get(i);
	                           	   boolean isSame = true;
	                           	   for(int j=0;j<lastHidCol;j++){        		   
	                          		   ReportField field = (ReportField) reportBean.getDisFields().get(j);
	                           		   String value=(String)values.get(field.getAsFieldName());                           			   
                         			   
                     				   //和上一行比较到哪一列出现不一致情况
                     				   String preV = (String)lastValue.get(field.getAsFieldName());
                     				   if((preV !=null  && !preV.equals(value)) || (preV ==null && value !=null) ){
                     					   isSame = false;
                     				   }   
	                           	   }
	                           	   if(isSame){
	                           		   for(int j=0;j<lastHidCol;j++){        		   
		                          		   ReportField field = (ReportField) reportBean.getDisFields().get(j);
		                          		   values.put(field.getAsFieldName(), "");
	                           		   }
	                           	   }else{
	                           		   lastValue = values;
	                           	   }
                            }

							boolean isQueryTotal;
							if ("page".equals(formType)) {
								isQueryTotal = true;
							} else {//如果是打印和导出就要判断上述字段中是否用到@TotalDB:,如果没有用到就不用查询统计数据
								isQueryTotal = false;
								for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
									ReportField field = (ReportField) reportBean.getQueryFields().get(j);
									if (field.getFieldName().contains("@TotalDB:")) {
										isQueryTotal = true;
										break;
									}
								}
							}

							//                            查询统计数据
							HashMap sumMap = new HashMap();
							if (reportBean.isHaveStat() && showTotalStat == 1 && isQueryTotal && !ReportData.PROCLIST.equals(reportType)) {
								if ((haveNext && pageNo == 1) || pageNo > 1 || (reportBean.isCross() && crossColNum == 0)) {
									String sqlTemp = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")"));
									String sqlAll = "";
									for (int i = 0; i < reportBean.getQueryFields().size(); i++) {
										ReportField field = (ReportField) reportBean.getQueryFields().get(i);
										if ("1".equals(field.getIsStat()) && !field.getFieldName().contains("@EVAL")) {
											sqlAll += "sum(" + field.getAsFieldName() + ") as " + field.getAsFieldName() + ",";
										}
									}
									if (sqlAll.length() > 0) {
										sqlAll = sqlAll.substring(0, sqlAll.length() - 1);

										sqlAll = sqlAll + " from (" + sqlTemp + ") as a";
										if (reportBean.isCross() && reportBean.getDisFields().indexOf(reportBean.getCrossField()) > -1) {
											if (crossColNum == 0) {
												sqlAll = reportBean.getCrossField().getAsFieldName() + " as " + reportBean.getCrossField().getAsFieldName() + "," + sqlAll + " group by "
														+ reportBean.getCrossField().getAsFieldName();
											}
										}
										System.out.println("select " + sqlAll);

										PreparedStatement ps = conn.prepareStatement("select " + sqlAll);

										for (int i = 1; i <= paramList.size(); i++) {
											ps.setString(i, paramList.get(i - 1).toString());
										}
										ResultSet res = ps.executeQuery();

										if (reportBean.isCross() && reportBean.getDisFields().indexOf(reportBean.getCrossField()) > -1 && crossColNum == 0) {
											while (res.next()) {
												HashMap sumChildMap = new HashMap();
												String crosVal = res.getString(reportBean.getCrossField().getAsFieldName());
												if (crosVal == null)
													crosVal = "";
												if (reportBean.getCrossField().getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
													crosVal = crosVal == null ? "" : crosVal;
													if (map.get(crosVal) != null) {
														crosVal = ((KRLanguage) map.get(crosVal)).get(locale);
													}
												}
												if (crosVal.indexOf("&") >= 0) {
													crosVal = crosVal.replaceAll("&", "&amp;");
												}
												if (crosVal.indexOf("\"") >= 0) {
													crosVal = crosVal.replaceAll("\"", "&quot;");
												}
												if (crosVal.indexOf("<") >= 0) {
													crosVal = crosVal.replaceAll("<", "&lt;");
												}
												if (crosVal.indexOf(">") >= 0) {
													crosVal = crosVal.replaceAll(">", "&gt;");
												}
												sumMap.put(crosVal, sumChildMap);
												for (int i = 0; i < reportBean.getQueryFields().size(); i++) {
													ReportField field = (ReportField) reportBean.getQueryFields().get(i);
													if ("1".equals(field.getIsStat())) {
														String value = res.getString(field.getAsFieldName());
														sumChildMap.put(field.getAsFieldName(), value);
													}
												}

											}
										} else {
											if (res.next()) {
												for (int i = 0; i < reportBean.getQueryFields().size(); i++) {
													ReportField field = (ReportField) reportBean.getQueryFields().get(i);
													if ("1".equals(field.getIsStat()) && !field.getFieldName().contains("@EVAL")) {
														String value = res.getString(field.getAsFieldName());
														sumMap.put(field.getAsFieldName(), value);
													}
												}
											}
										}
									}
								} else {//如果没有下一页，只用统计本页的信息
									for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
										ReportField field = (ReportField) reportBean.getQueryFields().get(j);
										if ("1".equals(field.getIsStat())) {
											double stat = 0;
											for (int i = 0; i < rows.size(); i++) {
												HashMap values = (HashMap) rows.get(i);
												Object obj = values.get(field.getAsFieldName());
												if (obj == null || (obj != null && obj.toString().length() == 0)) {
													obj = "0";
												}
												stat += Double.valueOf(obj.toString());
											}
											sumMap.put(field.getAsFieldName(), stat);
										} else {
											sumMap.put(field.getAsFieldName(), "");
										}
									}
								}
							}

							//设置交叉报表数据                              
							if (!"print".equals(formType) && ((reportBean.isCross() && reportBean.getDisFields().indexOf(reportBean.getCrossField()) > -1) || crossColNum > 0)) {
								time = System.currentTimeMillis();
								ArrayList newRows = new ArrayList();
								if (crossColNum > 0) {
									HashMap crossMap = new HashMap();
									for (int i = 0; i < rows.size(); i++) {
										HashMap values = (HashMap) rows.get(i);
										String groupVal = "";
										for (int j = 0; j < reportBean.getDisFields().size(); j++) {
											ReportField field = (ReportField) reportBean.getDisFields().get(j);
											if (field.getCrossField() == 0) {//分组列
												if (values.get(field.getAsFieldName()) != null) {
													groupVal += values.get(field.getAsFieldName());
												}
												groupVal += ";";
											}
										}

										HashMap fieldMap;
										if (crossMap.get(groupVal) == null) {
											fieldMap = new HashMap();
											crossMap.put(groupVal, fieldMap);
											newRows.add(groupVal);
										} else {
											fieldMap = (HashMap) crossMap.get(groupVal);
										}

										for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
											ReportField field = (ReportField) reportBean.getQueryFields().get(j);
											Object ov = values.get(field.getAsFieldName());
											if (field.getCrossField() == 0) {//分组列
												fieldMap.put(field.getAsFieldName(), ov);
											} else if (field.getCrossField() == 1) {//交叉值
												Object ovLast = (fieldMap.get(field.getAsFieldName()) == null ? "" : fieldMap.get(field.getAsFieldName()));
												fieldMap.put(field.getAsFieldName(), ovLast.toString() + (ov == null ? "" : ov) + ";");
											} else if (field.getCrossField() == 3) {//统计列
												if (fieldMap.get(field.getAsFieldName()) == null) {
													fieldMap.put(field.getAsFieldName(), ov);
												} else {
													ov = (ov == null) || (ov == "") ? "0" : ov;
													String ovT = fieldMap.get(field.getAsFieldName()).toString();
													if (ovT.length() == 0)
														ovT = "0";
													BigDecimal val = new BigDecimal(ov.toString()).add(new BigDecimal(ovT));
													if (field.getZeroDisplay() == 1) {
														fieldMap.put(field.getAsFieldName(), val.doubleValue() == 0 ? "" : val.doubleValue());
													} else {
														fieldMap.put(field.getAsFieldName(), val.doubleValue());
													}
												}
											}
										}
									}

									rows.clear();
									for (int i = 0; i < newRows.size(); i++) {
										HashMap valMap = (HashMap) crossMap.get(newRows.get(i));
										rows.add(valMap);

										boolean flag = false;
										int rowNo = rows.size() - 1;
										for (int j = 0; j < reportBean.getFieldInfo().size(); j++) {
											ReportField field = (ReportField) reportBean.getFieldInfo().get(j);
											if (field.getCrossField() == 1) {
												valMap = (HashMap) rows.get(rowNo);
												int rowNoTemp = rowNo;
												Object value = valMap.get(field.getAsFieldName());
												String[] vals = value.toString().substring(0, value.toString().length() - 1).split(";");
												int count = 0;
												for (int k = 0; k < vals.length; k++) {
													count = count + 1;
													valMap.put(field.getAsFieldName() + count, vals[k]);
													if (count == crossColNum && k + 1 < vals.length) {
														if (flag) {
															rowNoTemp++;
															valMap = (HashMap) rows.get(rowNoTemp);
														} else {
															valMap = new HashMap();
															rows.add(valMap);
														}
														count = 0;
													}
												}
												flag = true;
											}
										}
									}

									//先确定第一个交叉列的所在位置
									int index1 = 0;
									int index2 = 0;
									int index3 = 0;
									ArrayList crossFields = new ArrayList();
									for (int i = 0; i < reportBean.getFieldInfo().size(); i++) {
										ReportField crossField = (ReportField) reportBean.getFieldInfo().get(i);
										if (crossField.getCrossField() == 1) {
											if (index1 == 0) {
												index1 = reportBean.getFieldInfo().indexOf(crossField);
												index2 = reportBean.getQueryFields().indexOf(crossField);
												index3 = reportBean.getDisFields().indexOf(crossField);
											}
											crossFields.add(crossField);
										}
									}

									for (int i = 1; i <= crossColNum; i++) {
										for (int j = 0; j < crossFields.size(); j++) {
											ReportField crossField = (ReportField) crossFields.get(j);
											if (crossField.getCrossField() == 1) {
												ReportField crossF = new ReportField();
												crossF.setAsFieldName(crossField.getAsFieldName() + i);
												crossF.setFieldName(crossField.getFieldName());
												crossF.setFieldType(crossField.getFieldType());
												crossF.setIsStat("0");
												crossF.setWidth(crossField.getWidth());
												crossF.setDisplayFlag(crossField.getDisplayFlag());
												crossF.setFixColName("0");
												crossF.setDisplay(crossField.getDisplay() + i);
												crossF.setGroupName(crossField.getGroupName());
												crossF.setCrossField((byte) 2);
												crossF.setInputType(crossField.getInputType());
												crossF.setZeroDisplay(crossField.getZeroDisplay());
												crossF.setFieldIdentity(crossField.getFieldIdentity());
												reportBean.getFieldInfo().add(index1, crossF);
												reportBean.getQueryFields().add(index2, crossF);
												if (reportBean.getDisFields().contains(crossField)) {
													reportBean.getDisFields().add(index3, crossF);
													index3++;
												}

												index1++;
												index2++;
											}
										}
									}

									for (int i = 0; i < crossFields.size(); i++) {
										ReportField crossField = (ReportField) crossFields.get(i);
										if (crossField.getCrossField() == 1) {
											reportBean.getFieldInfo().remove(crossField);
											reportBean.getQueryFields().remove(crossField);
											reportBean.getDisFields().remove(crossField);
										}
									}
								} else {
									HashMap crossMap = new HashMap();
									ArrayList crossVals = new ArrayList();
									ArrayList crossValFields = new ArrayList();
									for (int i = 0; i < rows.size(); i++) {
										HashMap values = (HashMap) rows.get(i);
										String groupVal = "";
										for (int j = 0; j < reportBean.getDisFields().size(); j++) {
											ReportField field = (ReportField) reportBean.getDisFields().get(j);
											if (field.getCrossField() == 0) {//分组列
												groupVal += values.get(field.getAsFieldName());
											}
										}
										HashMap fieldMap;
										if (crossMap.get(groupVal) != null) {
											fieldMap = (HashMap) crossMap.get(groupVal);
										} else {
											fieldMap = new HashMap();
											crossMap.put(groupVal, fieldMap);
											newRows.add(groupVal);
										}

										String crossVal = "";//得到本次查询的所有的仓库信息
										if (values.get(reportBean.getCrossField().getAsFieldName()) != null) {
											crossVal = values.get(reportBean.getCrossField().getAsFieldName()).toString();
											if (!crossVals.contains(crossVal)) {
												crossVals.add(crossVal);
											}
										}

										for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
											ReportField field = (ReportField) reportBean.getQueryFields().get(j);
											Object ov = values.get(field.getAsFieldName());
											if (field.getCrossField() == 0) {//分组列
												fieldMap.put(field.getAsFieldName(), ov);
											} else if (field.getCrossField() == 2) {//交叉字段
												if (!crossValFields.contains(field))
													crossValFields.add(field);
												fieldMap.put(crossVal + "_" + field.getAsFieldName(), ov);
											} else if (field.getCrossField() == 3) {//统计列
												if (fieldMap.get(field.getAsFieldName()) == null) {
													fieldMap.put(field.getAsFieldName(), ov);
												} else {
													ov = (ov == null) || (ov == "") ? "0" : ov;
													String ovT = fieldMap.get(field.getAsFieldName()).toString();
													if (ovT.length() == 0)
														ovT = "0";
													BigDecimal val = new BigDecimal(ov.toString()).add(new BigDecimal(ovT));
													if (field.getZeroDisplay() == 1) {
														fieldMap.put(field.getAsFieldName(), val.doubleValue() == 0 ? "" : val.doubleValue());
													} else {
														fieldMap.put(field.getAsFieldName(), val.doubleValue());
													}
												}
											}
										}

									}
									int index1 = reportBean.getFieldInfo().indexOf(reportBean.getCrossField());
									int index2 = reportBean.getQueryFields().indexOf(reportBean.getCrossField());
									int index3 = reportBean.getDisFields().indexOf(reportBean.getCrossField());
									if (crossVals.size() > 200) {
										rs.retCode = ErrorCanst.DEFAULT_FAILURE;
										rs.retVal = "列数不能超过200列，请缩小查询范围";
										return;
									}
									//限制交叉后最多200例
									for (int i = 0; i < crossVals.size() && i < 200; i++) {
										for (int j = 0; j < crossValFields.size(); j++) {
											ReportField field = (ReportField) crossValFields.get(j);
											if (field.getCrossField() == 2) {
												//将行的数据转换成列
												ReportField crossF = new ReportField();
												String colName = crossVals.get(i).toString();

												crossF.setAsFieldName(colName + "_" + field.getAsFieldName());
												crossF.setFieldName(field.getFieldName());
												crossF.setFieldType(field.getFieldType());
												crossF.setIsStat(field.getIsStat());
												crossF.setWidth(field.getWidth());
												crossF.setDisplayFlag(crossVals.get(i).toString());
												crossF.setFixColName("0");
												String value = crossVals.get(i).toString();

												if (reportBean.getCrossField().getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
													value = GlobalsTool.getEnumerationItemsDisplay(reportBean.getCrossField().getPopUpName(), crossVals.get(i).toString(), locale);
												}
												if (crossValFields.size() > 1) {
													crossF.setDisplay(field.getDisplay());
												} else {
													crossF.setDisplay(value);
												}
												crossF.setGroupName(value);
												crossF.setCrossField((byte) 2);
												crossF.setZeroDisplay(field.getZeroDisplay());
												crossF.setFieldIdentity(field.getFieldIdentity());
												reportBean.getFieldInfo().add(index1, crossF);
												reportBean.getQueryFields().add(index2, crossF);
												reportBean.getDisFields().add(index3, crossF);
												index1++;
												index2++;
												index3++;
											}
										}
									}

									//去掉交叉列，交叉值原本的字段
									for (int i = 0; i < crossValFields.size(); i++) {
										ReportField field = (ReportField) crossValFields.get(i);
										if (field.getCrossField() == 2) {
											reportBean.getFieldInfo().remove(field);
											reportBean.getQueryFields().remove(field);
											reportBean.getDisFields().remove(field);
										}
									}

									reportBean.getFieldInfo().remove(reportBean.getCrossField());
									reportBean.getQueryFields().remove(reportBean.getCrossField());
									reportBean.getDisFields().remove(reportBean.getCrossField());
									//整理数据
									rows.clear();

									for (int i = 0; i < newRows.size(); i++) {
										HashMap maps = (HashMap) crossMap.get(newRows.get(i));
										for (int j = 0; j < reportBean.getDisFields().size(); j++) {
											ReportField field = (ReportField) reportBean.getDisFields().get(j);
											Object value = maps.get(field.getAsFieldName());
											if (field.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE
													&& (value == null || (value != null && value.toString().length() > 0 && Double.parseDouble(value.toString()) == 0))) {
												if (field.getZeroDisplay() == 1) {
													maps.put(field.getAsFieldName(), "");
												} else {
													maps.put(field.getAsFieldName(), "0");
												}
											}
										}
										rows.add(maps);
									}
								}
								BaseEnv.log.debug("交叉报表整理数据：" + (System.currentTimeMillis() - time));

							}

							//判断哪个字段上用到了已查询数据，查找所有使用的已查询字段
							ArrayList valList = new ArrayList();
							for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
								ReportField field = (ReportField) reportBean.getQueryFields().get(j);
								if (field.getFieldName().contains("@ValueofDB:") || field.getFieldName().contains("@TotalDB:")) {
									valList.addAll(ConfigParse.parseSentenceGetParam(field.getFieldName(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(),
											new ArrayList()));
								}
							}

							BSFManager evalM = BaseEnv.evalManager;
							//所有页统计
							if (reportBean.isHaveStat() && showTotalStat == 1 && isQueryTotal) {
								HashMap statAll = new HashMap();
								statAll.put("rowAll", "rowAll");
								if (reportBean.isCross() && rows.size() > 0 && crossColNum == 0) {
									for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
										ReportField field = (ReportField) reportBean.getQueryFields().get(j);
										if (!field.getAsFieldName().equals("keyId") && !field.getAsFieldName().equals("classCode") && !field.getAsFieldName().equals("childCount")
												&& !field.getAsFieldName().equals("createBy") && !field.getAsFieldName().equals("deptCodeR")) {

											if ("1".equals(field.getIsStat()) && field.getCrossField() == 2) {
												String cross = field.getAsFieldName().substring(0, field.getAsFieldName().indexOf("_"));
												String fieldName = field.getAsFieldName().substring(field.getAsFieldName().indexOf("_") + 1);
												HashMap sumChildMap = (HashMap) sumMap.get(cross);
												Object obj = sumChildMap.get(fieldName);
												if (obj == null) {
													obj = "0";
												}
												double stat = Double.valueOf(obj.toString());
												statAll.put(field.getAsFieldName(), stat);
											} else if ("1".equals(field.getIsStat()) && field.getCrossField() == 3) {
												Iterator it = sumMap.keySet().iterator();
												double stat = 0;
												while (it.hasNext()) {
													Object cross = it.next();
													HashMap sumChildMap = (HashMap) sumMap.get(cross);
													Object obj = sumChildMap.get(field.getAsFieldName());
													if (obj == null) {
														obj = "0";
													}
													stat += Double.valueOf(obj.toString());
												}
												statAll.put(field.getAsFieldName(), stat);
											}
										} else {
											statAll.put(field.getAsFieldName(), "");
										}
									}
								} else {
									for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
										ReportField field = (ReportField) reportBean.getQueryFields().get(j);
										if (!field.getAsFieldName().equals("keyId") && !field.getAsFieldName().equals("classCode") && !field.getAsFieldName().equals("childCount")
												&& !field.getAsFieldName().equals("createBy") && !field.getAsFieldName().equals("deptCodeR")) {
											if ("1".equals(field.getIsStat())) {
												if (field.getFieldName().contains("@ValueofDB:")) {
													statAll.put(field.getAsFieldName(), field.getFieldName());
												} else {
													Object obj = sumMap.get(field.getAsFieldName());
													if (obj == null) {
														obj = "0";
													}
													double stat = Double.valueOf(obj.toString());
													statAll.put(field.getAsFieldName(), stat);
												}
											}
										} else {
											statAll.put(field.getAsFieldName(), "");
										}
									}
								}

								//处理带公式的统计字段，在后续解析中会用到统计字段，所以先解析统计字段
								for (int i = 0; i < valList.size(); i++) {
									for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
										ReportField field = (ReportField) reportBean.getQueryFields().get(j);
										if ("1".equals(field.getIsStat()) && field.getFieldName().contains("@ValueofDB:")) {
											Object fieldVal = statAll.get(field.getAsFieldName());
											Object obj = statAll.get(valList.get(i).toString().replace("@ValueofDB:", ""));
											String value = (obj == null || "".equals(obj)) ? "0" : obj.toString();
											value = fieldVal.toString().replaceAll(valList.get(i).toString(), value);
											statAll.put(field.getAsFieldName(), value);

										}
									}
								}
								for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
									ReportField field = (ReportField) reportBean.getQueryFields().get(j);
									if ("1".equals(field.getIsStat())) {
										String value = statAll.get(field.getAsFieldName()).toString();
										if (value.contains("@EVAL[")) {
											int indexs = value.indexOf("@EVAL[") + "@EVAL[".length();
											int indexe = value.indexOf("]", indexs);
											String exeCondition = value.substring(indexs, indexe);
											value = evalM.eval("javascript", "XX", 0, 0, exeCondition).toString();
											if ("NaN".equals(value)) {
												value = "";
											}
										}
										statAll.put(field.getAsFieldName(), "-0".equals(value) ? 0 : value);
									}
								}

								rows.add(statAll);
							}

							if (valList.size() > 0) {//将使用的字段替换其中的字段
								for (int i = 0; i < rows.size(); i++) {
									HashMap values = (HashMap) rows.get(i);
									for (int j = 0; j < valList.size(); j++) {
										for (int k = 0; k < reportBean.getQueryFields().size(); k++) {
											ReportField field = (ReportField) reportBean.getQueryFields().get(k);
											Object fieldVal = values.get(field.getAsFieldName());
											if (fieldVal != null && field.getFieldName().contains(valList.get(j).toString())) {
												if (valList.get(j).toString().indexOf("@ValueofDB:") >= 0) {
													Object obj = values.get(valList.get(j).toString().replace("@ValueofDB:", ""));
													String value = (obj == null || "".equals(obj)) ? "0" : obj.toString();
													values.put(field.getAsFieldName(), fieldVal.toString().replaceAll(valList.get(j).toString(), value));
												} else if (valList.get(j).toString().indexOf("@TotalDB:") >= 0) {
													HashMap lastMap = (HashMap) rows.get(rows.size() - 1);
													if (lastMap.get("rowAll") != null) {
														String value = lastMap.get(valList.get(j).toString().replace("@TotalDB:Total_", "")).toString();
														values.put(field.getAsFieldName(), fieldVal.toString().replaceAll(valList.get(j).toString(), value));
													}
												}
											}
										}
									}
								}
							}

							for (int i = 0; i < rows.size(); i++) {
								HashMap values = (HashMap) rows.get(i);
								for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
									ReportField field = (ReportField) reportBean.getQueryFields().get(j);
									String value = (values.get(field.getAsFieldName()) == null ? "" : values.get(field.getAsFieldName()).toString());
									if (value.contains("@EVAL[")) {
										int indexs = value.indexOf("@EVAL[") + "@EVAL[".length();
										int indexe = value.indexOf("]", indexs);
										String exeCondition = value.substring(indexs, indexe);
										value = evalM.eval("javascript", "XX", 0, 0, exeCondition).toString();
										if ("NaN".equals(value)) {
											value = "";
										}
									}

									if (field.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
										if (value.length() > 0) {
											value = GlobalsTool.formatNumberS(Double.valueOf(value), false, true, field.getFieldIdentity(), field.getFieldName());
										}
										values.put(field.getAsFieldName(), "-0".equals(value) ? 0 : value);
									} else if (field.getFieldType() == DBFieldInfoBean.FIELD_INT && field.getInputType() != DBFieldInfoBean.INPUT_ENUMERATE) {

										if (value.length() > 0 && field.getFieldName().indexOf(".") > 0) {
											String dt = field.getFieldName();
											String df = dt.substring(dt.indexOf(".") + 1);
											dt = dt.substring(0, dt.indexOf("."));

											value = GlobalsTool.formatNumber(Double.valueOf(value), false, true, true, dt, df, true);
										}
										values.put(field.getAsFieldName(), "-0".equals(value) ? 0 : value);

									}
								}
							}
							if (reportBean.isHaveStat() && showTotalStat == 1 && !"page".equals(formType) && rows.size() > 0 && isQueryTotal) {
								rows.remove(rows.size() - 1);
							}
							BaseEnv.log.debug("整理数据：" + (System.currentTimeMillis() - time));
							rs.setRetVal(rows);
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getReportData() Error:",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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

	public Result delPrintData(final String tableName, final String id, final String loginId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							ResultSet rst = st.executeQuery("select count(0) from " + tableName + " where id='" + id + "' and workFlowNodeName='print'");
							if (rst.next()) {
								if (rst.getInt(1) > 0) {
									ArrayList list = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
									for (int i = 0; i < list.size(); i++) {
										DBTableInfoBean cb = (DBTableInfoBean) list.get(i);
										st.addBatch("delete from " + cb.getTableName() + " where f_ref in (select id from " + tableName + " where createBy='" + loginId
												+ "' and workFlowNodeName='print')");
									}
									st.addBatch("delete from " + tableName + " where createBy='" + loginId + "' and workFlowNodeName='print'");
									st.executeBatch();
								}
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.delPrintData ",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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

	public Result getPrintData(final DefineReportBean reportBean, final String sql, final ArrayList paramList, final String locale, final String reportType, final int showTotalStat,
			final LoginBean loginBean) {
		int pageSize=0;
		int pageNo = 0;
		if(ReportData.PROCLIST.equals(reportType)){
			pageSize=900000; //如果是存储过程报表，pageSize放到最大，避免每个存储过程忘了写分页逻辑
			pageNo = 1;
		}
		
		Result rss = getReportData(reportBean, sql, paramList, locale, reportType, showTotalStat, "print", pageSize, pageNo, 0, "", 0, "print", loginBean);
		if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			ArrayList list = (ArrayList) rss.getRetVal();
			for (int i = 0; i < list.size(); i++) {
				HashMap values = (HashMap) list.get(i);
				for (int j = 0; j < reportBean.getQueryFields().size(); j++) {
					ReportField field = (ReportField) reportBean.getQueryFields().get(j);
					if (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE
							|| (field.getPopUpName() != null && field.getPopUpName().length() > 0 && field.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT)) {
						String value;
						if (values.get(field.getAsFieldName()) == null) {
							value = "";
						} else {
							value = GlobalsTool.getEnumerationItemsDisplay(field.getPopUpName(), values.get(field.getAsFieldName()).toString(), locale);
							if (value == null || value.length() == 0) {
								value = "";
							}
						}
						values.put(field.getAsFieldName(), value);
					} else if (field.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX) {
						String value;
						if (values.get(field.getAsFieldName()) == null) {
							value = "";
						} else {
							value = GlobalsTool.getCheckBoxDisplay(field.getPopUpName(), values.get(field.getAsFieldName()).toString(), locale);
							if (value == null || value.length() == 0) {
								value = "";
							}
						}
						values.put(field.getAsFieldName(), value);
					}
				}
			}
		}
		return rss;
	}

	/**
	 * @param parentCode
	 * @param tableBean
	 * @return
	 */
	public Result getParentName(final String parentCode, final DBTableInfoBean tableBean, final String local) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String parents = "";
							String tempCode = parentCode;
							while (tempCode.length() > 0) {
								parents += "'" + tempCode + "',";
								tempCode = tempCode.substring(0, tempCode.length() - 5);
							}

							List fields = tableBean.getFieldInfos();
							String markerField = "";
							boolean isInputLanguage = false;
							for (int i = 0; i < fields.size(); i++) {
								DBFieldInfoBean field = (DBFieldInfoBean) fields.get(i);
								if (field.getFieldSysType() != null && field.getFieldSysType().equals("RowMarker")) {
									markerField = field.getFieldName();
									if (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
										isInputLanguage = true;
									}
									break;
								}
							}
							if (markerField.length() == 0) {
								rs.setRetCode(ErrorCanst.RET_NOTSETROWMARKER);
								return;
							}
							ArrayList parentName = new ArrayList();
							if (parents.length() > 0) {
								parents = parents.substring(0, parents.length() - 1);
								String sql = "";
								if (isInputLanguage) {
									sql += "select b." + local + ",a.classCode from " + tableBean.getTableName() + " a ";
									sql += " left join tblLanguage b on a." + markerField + "=b.id ";
									sql += " where a.classCode in (" + parents + ") group by a.classCode,b." + local + " order by a.classCode";
								} else {
									sql += " select a." + markerField + ",a.classCode from " + tableBean.getTableName() + " a ";
									sql += " where a.classCode in (" + parents + ") group by a.classCode,a." + markerField + " order by a.classCode";
								}
								ResultSet rs = st.executeQuery(sql);
								while (rs.next()) {
									String[] val = new String[2];
									val[0] = rs.getString(1);
									val[1] = rs.getString(2);
									parentName.add(val);
								}
							}
							rs.setRetVal(parentName);

						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getParentName",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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

	public Result getMinClassLen(final String sql, final ArrayList paramList) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							PreparedStatement st = conn.prepareStatement(sql);
							for (int i = 1; i <= paramList.size(); i++) {
								st.setString(i, paramList.get(i - 1).toString());
							}

							BaseEnv.log.debug(sql);
							ResultSet rss = st.executeQuery();
							if (rss.next()) {
								rs.setRetVal(rss.getInt(1));
							} else {
								rs.setRetVal("0");
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getMainClassLen Error",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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

	public Result getReportByReportNumber(final String reportNumber) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblReports where reportNumber = ?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, reportNumber);
							ResultSet rss = ps.executeQuery();
							if (rss.next()) {
								ReportsBean reportBean = new ReportsBean();
								reportBean.setId(rss.getString("id"));
								reportBean.setSQLFileName(rss.getString("SQLFileName"));
								reportBean.setNewFlag(rss.getString("newFlag"));
								rs.setRetVal(reportBean);
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getReportByReportNumber Error:",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	 * @param disField
	 * @param con
	 * @return
	 */
	private Result getClassInfo(final ArrayList disField) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						HashMap map = new HashMap();
						String toClassTable = "";
						for (int k = 0; k < disField.size(); k++) {
							ReportField field = (ReportField) disField.get(k);
							String linkAdd = field.getLinkAdd() == null ? "" : field.getLinkAdd();
							if (linkAdd.startsWith("@ClassCode:")) {
								int index = linkAdd.indexOf("#");
								if (index > -1) {
									toClassTable = linkAdd.substring(index + 1);
									break;
								}
							}
						}
						if (toClassTable.length() > 0) {
							String sql = "select classCode,isCatalog from " + toClassTable;
							try {
								Statement st = connection.createStatement();
								ResultSet rss = st.executeQuery(sql);
								while (rss.next()) {
									map.put(rss.getString("classCode"), rss.getString("isCatalog"));
								}
								rss.close();
								rs.setRetVal(map);
							} catch (SQLException e) {
								BaseEnv.log.error("ReportDataMgt.getClassInfo Error:",e);
								rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								return;
							}
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result getDisplayData(final DefineReportBean reportBean, final ReportsBean reportSet, final String reportType, final String reportNumber, final String sql, final ArrayList paramList,
			final String locale, final ArrayList scopeRight, final String userId, final HashMap queryMaps, final ArrayList<ColConfigBean> configList, final String winCurIndex,
			final ArrayList conditions, final OAWorkFlowTemplate workFlow, final String scompayId, int pageSize, int pageNo, final String host, final String formType, final String statField,
			final LoginBean loginBean, final HttpServletRequest request) {
		ArrayList rowsT = null;

		final Result rs = getReportData(reportBean, sql, paramList, locale, reportSet.getReportType(), reportSet.getShowTotalStat(), reportType, pageSize, pageNo, reportSet.getShowTotalPage(),
				statField, reportSet.getCrossColNum(), formType, loginBean);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		} else {
			rowsT = (ArrayList) rs.getRetVal();
		}
		HashMap classMapT = null;
		Result rs1 = getClassInfo(reportBean.getDisFields());
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs1;
		} else {
			classMapT = (HashMap) rs1.getRetVal();
		}
		final ArrayList rows = rowsT;
		final HashMap classMap = classMapT;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							ArrayList disField = reportBean.getDisFields();
							HashMap map = new HashMap();
							DynDBManager db = new DynDBManager();
							ArrayList rowPage = new ArrayList();
							for (int i = 0; i < rows.size(); i++) {
								Object[] rowDiss = new Object[4];
								String rowDis = "";
								HashMap values = (HashMap) rows.get(i);
								//                                  zxy 在defineReportBean中已经跟据列配置过滤了一次，该过滤是加上了如goodsCode字段的，且goodsCode等不是可见字段在列配置中不可能存在，而造成标准不统一

								for (int j = 0; j < disField.size(); j++) {
									boolean isLast = false;
									ReportField field = (ReportField) disField.get(j);
									String fieldName = field.getFieldName();
									String fieldIdentity = field.getFieldIdentity();

									boolean flag = true;
									flag = DynDBManager.getViewRight(reportNumber, fieldName, scopeRight, fieldIdentity, loginBean);
									
									if("export".equals(formType) && field.getFieldType() == DBFieldInfoBean.FIELD_PIC){
										//导出时不至图片
										flag= false;
									}
									
									if (flag) {
										String value = values.get(field.getAsFieldName()) == null ? "" : values.get(field.getAsFieldName()).toString();
										if (field.getFieldType() == DBFieldInfoBean.FIELD_HTML || field.getFieldType() == DBFieldInfoBean.FIELD_TEXT)
											value = GlobalsTool.encodeHTMLFlash(value);
										if (field.getFieldType() == DBFieldInfoBean.FIELD_PIC && value.trim().length() > 0) {
											if(!value.toLowerCase().startsWith("http")){
												//value = java.net.URLEncoder.encode(value, "UTF-8");
												String tn =  field.getFieldName().substring(0, field.getFieldName().indexOf("."));
												//由于存储过程报表，表名不一定正确导到图片显示不出，这里判断如果表名不存在，一律认为是商品表
												if(GlobalsTool.getTableInfoBean(tn)==null){
													tn = "tblGoods";
												}
												value = "ReadFile.jpg?tempFile=false&type=PIC&YS=true&tableName=" + tn + "&fileName=" + value;
											}

										}
										if (value.trim().length() == 0)
											value = "";
										String reportGoodsProp = GlobalsTool.getSysSetting("ReportGoodsProp");
										if (reportGoodsProp.equals("true")) {
											if ("GoodsField".equals(field.getFieldSysType())) {
												if (!field.getAsFieldName().endsWith("NV")) {
													GoodsPropInfoBean gpBean = (GoodsPropInfoBean) BaseEnv.propIgnoreCaseMap.get(field.getAsFieldName().toLowerCase());
													if (gpBean != null && gpBean.getInputBill() == 2) {
														List items = gpBean.getEnumItem();
														for (int k = 0; k < items.size(); k++) {
															GoodsPropEnumItemBean item = (GoodsPropEnumItemBean) items.get(k);
															if (item.getEnumValue().equals(value)) {
																value = item.getDisplay().get(locale).toString();
															}
														}
													}
												}
											}
										}
										String linkAdd = field.getLinkAdd() == null ? "" : field.getLinkAdd();
										if (linkAdd.startsWith("@ClassCode:")) {
											if (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
												value = GlobalsTool.getEnumerationItemsDisplay(field.getPopUpName(), value, locale);
												if (value == null || value.length() == 0) {
													value = "";
												}
											}
											String elseParam = linkAdd.substring("@ClassCode:".length());
											Object parentCode = values.get(reportBean.getClassCode().getAsFieldName());
											if (linkAdd.indexOf("#") > -1) {
												linkAdd = linkAdd.substring(0, linkAdd.indexOf("#"));
												if (parentCode != null && parentCode.toString().length() > 0) {
													if (classMap.get(parentCode).equals("0")) {
														isLast = true;
													}
												}
											}
											if (!isLast) {
												linkAdd = "&parentCode=" + parentCode + elseParam;
											} else {
												linkAdd = "";
											}
										}
										if (linkAdd.equals("")) {
											if (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
												value = GlobalsTool.getEnumerationItemsDisplay(field.getPopUpName(), value, locale);
												if (value.length() == 0) {
													value = "";
												}
											} else if (field.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX) {
												String oldVal = value;
												value = GlobalsTool.getCheckBoxDisplay(field.getPopUpName(), value, locale);
												value = value.length() == 0 ? "" : value;
												if (value.length() == 0 && field.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
													value = oldVal;
												}
											}

											if (value.indexOf("\r\n") >= 0) {
												value = value.replace("\r\n", "\\r\\n");
											}
											
											rowDis += "'" + value + "',";
										} else {
											ArrayList sysParam = new ArrayList();
											ArrayList tabParam = new ArrayList();
											ArrayList sessParam = new ArrayList();
											ArrayList codeParam = new ArrayList();
											ArrayList queryParam = new ArrayList();
											HashMap tabMap = new HashMap();
											HashMap queryMap = new HashMap();
											
					                        //处理变量条件信息
											Pattern pattern = Pattern.compile("[&|\\?]([\\w]+)\\.\\[(\\w+)\\][\\s]*=[^&]*",Pattern.CASE_INSENSITIVE);
									    	Matcher matcher = pattern.matcher(linkAdd);	
									        while (matcher.find()){
									        	String all = matcher.group();
									        	String preFN =matcher.group(1);
												String showSet = matcher.group(2);
												//tableName,fieldName,reportView,billView,popSel,keyword,popupView
												String str = "";
						                        for (String[] shows : BaseEnv.reportShowSet) { 
													if (showSet.equals(shows[0]) && "1".equals(shows[2])) {															
														str +="&"+preFN+"."+shows[1]+"=@ValueofDB:"+shows[1];
													}
						                        }
						                        linkAdd = linkAdd.substring(0,linkAdd.indexOf(all))+str +linkAdd.substring(linkAdd.indexOf(all)+all.length());
									    	}
											
											ArrayList paramList = ConfigParse.parseSentenceGetParam(linkAdd, tabParam, sysParam, sessParam, codeParam, queryParam, null);
											for (int k = 0; k < paramList.size(); k++) {
												String param = paramList.get(k).toString();
						                        if (param.indexOf("@ValueofDB:") > -1) {
													param = param.substring("@ValueofDB:".length());
													String val = values.get(param) == null ? "" : values.get(param).toString().trim();
													if (!val.contains("&#39;") && !val.contains("&#34;")) {
														val = val.replaceAll("&", "@join:");
													}
													tabMap.put(param, val);
												}
											}
											HashMap sysMap = ConfigParse.getSystemParam(sysParam, scompayId);
											HashMap sessMap = ConfigParse.getSessParam(userId, sessParam);
											HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);

											linkAdd = ConfigParse.parseSentencePutParam(linkAdd, tabMap, sysMap, queryMaps, sessMap, codeMap, null, null, null);
											boolean defaultLink = false;
											if (linkAdd.contains("@defaultLink:")) {
												linkAdd = linkAdd.replace("@defaultLink:", "");
												defaultLink = true;
											}
											String temp = "";
											while (linkAdd.indexOf("=") >= 0 && !linkAdd.contains("javascript:")) {
												int start = linkAdd.indexOf("=") + 1;
												int end = linkAdd.indexOf("&", start);
												if (end < 0) {
													end = linkAdd.length();
												}
												String valueStr = linkAdd.substring(start, end);
												String encode = "";
												if (!valueStr.contains("&#39;") && !valueStr.contains("&#34;")) {
													encode = java.net.URLEncoder.encode(valueStr, "UTF-8");
												}
												if (end == linkAdd.length()) {
													temp = temp + linkAdd.substring(0, start) + encode;
													linkAdd = "";
												} else {
													temp = temp + linkAdd.substring(0, start) + encode + linkAdd.substring(end, end + 1);
													linkAdd = linkAdd.substring(end + 1);
												}
											}

											if (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
												value = GlobalsTool.getEnumerationItemsDisplay(field.getPopUpName(), value, locale);
												value = value.length() == 0 ? "" : value;
											} else if (field.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX) {
												value = GlobalsTool.getCheckBoxDisplay(field.getPopUpName(), value, locale);
												value = value.length() == 0 ? "" : value;
											}
											if (!linkAdd.contains("javascript:")) {
												linkAdd = temp;
												linkAdd = linkAdd.replaceAll("@join:", "&");
											}
											if (values.containsKey("rowOne") || values.containsKey("rowAll")) {
												rowDis += "'" + value + "',";
											} else {
												if (reportBean.getClassCode() != null) {
													if ("".equals(value)) {
														linkAdd = "";
														rowDis += "'',";
													} else {
														if (linkAdd.contains("javascript:")) {
															rowDis += "'" + value + "#;#" + linkAdd + "',";
														} else {
															//条件改为内存传递
//															for (int l = 0; l < conditions.size(); l++) {
//																String[] param = (String[]) conditions.get(l);
//																if (param[2] != null && param[2].length() > 0) {
//																	if (!linkAdd.contains("&" + param[1] + "="))
//																		linkAdd += "&" + param[1] + "=" + java.net.URLEncoder.encode(param[2], "UTF-8");
//																	if (!linkAdd.contains("&" +"hide_" + param[1] + "=") && request.getParameter("hide_" + param[1]) != null && !request.getParameter("hide_" + param[1]).equals("")) {
//																		linkAdd += "&hide_" + param[1] + "=" + java.net.URLEncoder.encode(request.getParameter("hide_" + param[1]), "UTF-8");
//																	}
//																	if (param[11] != null) {
//																		String[] items = param[11].split("\\|");
//																		for (String string : items) {
//																			String[] tmpValues = string.split(";");
//																			if (tmpValues.length > 2 && !linkAdd.contains("&"  + tmpValues[1] + "=")) {
//																				linkAdd += "&" + tmpValues[1] + "=" + tmpValues[2];
//																			}
//																		}
//																	}
//																}
//															}
															linkAdd += "&queryChannel=normal&LinkType=@URL:";
															rowDis += "'" + value + "#;#" + linkAdd + "',";
														}
													}
												} else {
													if ("".equals(value)) {
														linkAdd = "";
														rowDis += "'',";
													} else {
														if (linkAdd.contains("javascript:")) {
															rowDis += "'" + value + "#;#" + linkAdd + "',";
														} else {
															//条件改为内存传递
//															for (int l = 0; l < conditions.size(); l++) {
//																String[] param = (String[]) conditions.get(l);
//																if (param[2] != null && param[2].length() > 0) {
//																	if (!linkAdd.contains("&" + param[1] + "="))
//																		linkAdd += "&" + param[1] + "=" + java.net.URLEncoder.encode(param[2], "UTF-8");
//																	if (!linkAdd.contains("&" +"hide_" + param[1] + "=") && request.getParameter("hide_" + param[1]) != null && !request.getParameter("hide_" + param[1]).equals("")) {
//																		linkAdd += "&hide_" + param[1] + "=" + java.net.URLEncoder.encode(request.getParameter("hide_" + param[1]), "UTF-8");
//																	}
//																	if (param[11] != null) {
//																		String[] items = param[11].split("\\|");
//																		for (String string : items) {
//																			String[] tmpValues = string.split(";");
//																			if (tmpValues.length > 2 && !linkAdd.contains("&"  + tmpValues[1] + "=")) {
//																				if (!linkAdd.contains("&" + tmpValues[1] + "="))
//																					linkAdd += "&" + tmpValues[1] + "=" + tmpValues[2];
//																			}
//																		}
//																	}
//																}
//															}
															linkAdd = linkAdd + "&noback=true&queryChannel=normal&LinkType=@URL:";
															rowDis += "'" + value + "#;#/" + linkAdd + "',";
														}
													}
												}
											}
											if (defaultLink) {
												rowDiss[1] = "link(&apos;" + (linkAdd.replaceAll("&", "&amp;")) + "&apos;)";
											}
										}
									}
								}

								List sysSCeList = ((EnumerateBean) BaseEnv.enumerationMap.get("SpecialCharacter")).getEnumItem();
								if (sysSCeList != null) {
									for (Object obj : sysSCeList) {
										EnumerateItemBean item = (EnumerateItemBean) obj;
										String itemValue = item.getEnumValue();
										String itemName = item.getDisplay().get("zh_CN");
										if (rowDis.contains(itemValue)) {
											rowDis = rowDis.replaceAll(item.getEnumValue(), itemName);
										}
									}
								}
								if (rowDis.length() == 0) {
									rs.setRetCode(ErrorCanst.RET_LIST_NOCOLUMN);
									return;
								}
								rowDis = rowDis.substring(0, rowDis.length() - 1);
								if (values.containsKey("rowOne")) {
									rowDis = rowDis + ",'rowOne'";
								}
								if (values.containsKey("rowAll")) {
									rowDis = rowDis + ",'rowAll'";
								}
								if (reportType.equals("TABLELIST")) {
									TableListResult tlr = new TableListResult();
									tlr.setRowDis(rowDis);
									tlr.setKeyId(values.get("keyId") == null ? "1" : values.get("keyId").toString());
									tlr.setCreateBy(values.get("createBy") == null ? "" : values.get("createBy").toString());
									tlr.setDeptCodeR(values.get("deptCodeR") == null ? "" : values.get("deptCodeR").toString());
									tlr.setF_brother(values.get("f_brother") == null ? "" : values.get("f_brother").toString());
									if (values.get("classCode") != null) {
										tlr.setClassCode(values.get("classCode").toString());
										tlr.setChildCount(values.get("childCount").toString() == "" ? "0" : values.get("childCount").toString());
									} else {
										tlr.setClassCode("");
									}
									tlr.setWorkFlowNode(values.get("workFlowNode") + "");
									tlr.setWorkFlowNodeName(values.get("workFlowNodeName") + "");
									tlr.setCheckPersons(values.get("checkPersons") == null ? "" : values.get("checkPersons") + "");
									tlr.setLastNodeID(values.get("lastNodeID") + "");
									tlr.setEmployeeId(values.get("employeeId") + "");
									tlr.setDeptCodeRE(values.get("deptCodeRE") + "");

									rowPage.add(tlr);
								} else if (reportType.equals("scopeClassPop")) {
									Object[] cols = new Object[5];
									cols[0] = rowDis;
									if (values.get("classCode") != null) {
										cols[1] = values.get("classCode").toString();
									} else {
										cols[1] = "";
									}
									cols[2] = cols[1];
									cols[3] = values.get("childCount").toString() == "" ? "0" : values.get("childCount").toString();
									int number = 0;
									for (int n = 0; n < disField.size(); n++) {
										ReportField field = (ReportField) disField.get(n);
										String[] fieldName = field.getFieldName().split("\\.");
										DBFieldInfoBean dbField = GlobalsTool.getFieldBean(fieldName[0], fieldName[1]);
										if (dbField != null && "RowMarker".equals(dbField.getFieldSysType())) {
											number = n;
											break;
										}
									}
									Object obj = values.get(((ReportField) disField.get(number)).getAsFieldName());

									if (obj != null) {
										cols[4] = cols[1].toString() + ";" + obj.toString();
									} else {
										cols[4] = cols[1].toString() + ";" + cols[1];
									}

									rowPage.add(cols);
								} else if (reportType.equals("popData")) {
									String[] cols = new String[2];
									cols[0] = values.get("classCode").toString();

									int number = 0;
									for (int n = 0; n < disField.size(); n++) {
										ReportField field = (ReportField) disField.get(n);
										String[] fieldName = field.getFieldName().split("\\.");
										DBFieldInfoBean dbField = GlobalsTool.getFieldBean(fieldName[0], fieldName[1]);
										if (dbField != null && "RowMarker".equals(dbField.getFieldSysType())) {
											number = n;
											break;
										}
									}
									Object obj = values.get(((ReportField) disField.get(number)).getAsFieldName());

									if (obj != null) {
										cols[1] = obj.toString();
									} else {
										cols[1] = cols[0];
									}
									map.put(cols[0], cols[1]);
								} else {
									rowDiss[0] = rowDis;
									if (rowDiss[1] == null) {
										rowDiss[1] = "";
									}
									if (reportBean.getClassCode() != null) {
										if (values.get("isCatalog") != null) {
											if (values.get("classCode") != null) {
												rowDiss[2] = values.get("classCode").toString();
											} else {
												rowDiss[2] = "";
											}
											rowDiss[3] = values.get("isCatalog").toString() == "" ? "0" : values.get("isCatalog").toString();
										}
									}
									rowPage.add(rowDiss);
								}
							}

							if (reportType.equals("popData")) {
								rs.setRetVal(map);
							} else {
								if (rowPage.size() > 0 && reportSet.getShowTotalStat() == 1 && reportBean.isHaveStat() && !"export".equals(formType)) {
									if (reportType.equals("TABLELIST")) {
										TableListResult lastRow = (TableListResult) rowPage.get(rowPage.size() - 1);
										String[] cols = (lastRow.getRowDis().toString() + ",").split("',");
										String boldStr = "";
										for (int i = 0; i < cols.length; i++) {
											boldStr += "'" + cols[i].substring(1) + "',";
										}
										lastRow.setRowDis(boldStr.substring(0, boldStr.length() - 1));
										lastRow.setKeyId("");
									} else {
										Object[] lastRow = (Object[]) rowPage.get(rowPage.size() - 1);
										String[] cols = (lastRow[0].toString() + ",").split("',");
										String boldStr = "";
										for (int i = 0; i < cols.length; i++) {
											boldStr += "'" + cols[i].substring(1) + "',";
										}
										lastRow[0] = boldStr.substring(0, boldStr.length() - 1);
										lastRow[1] = "";
									}
								}
								rs.setRetVal(rowPage);
							}

						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.getDisplayData Error:",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		long last = System.currentTimeMillis();
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 执行临时表存储过程
	 * @param procName
	 * @param locale
	 * @param request
	 * @param procParams
	 * @param parentCode
	 * @param classRF
	 * @return
	 */
	public Result execTemplateProc(final String procName, final String locale, final HttpServletRequest request, final ArrayList<String> procParams, final String parentCode, final ReportField classRF) {
		final Result rs = new Result();
		final LoginBean loginBean = GlobalsTool.getLoginBean(request);
		int retCode = DBUtil.execute("reportDB", new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String placeHolder = "";
							for (String str : procParams) {
								placeHolder += ",?";
							}
							CallableStatement cs;
							cs = conn.prepareCall("{call " + procName + "(?,?,?" + placeHolder + ")}");
							BaseEnv.log.debug("{call " + procName + "(?,?,?" + placeHolder + ")}");
							cs.setString(1, locale);
							cs.setString(2, loginBean.getId());
							cs.setString(3, loginBean.getSunCmpClassCode());
							for (int i = 0; i < procParams.size(); i++) {
								cs.setString(i + 4, procParams.get(i));
								BaseEnv.log.debug("值" + (i + 4) + ":" + procParams.get(i));
							}
							cs.execute();
						} catch (SQLException e) {
							int code = e.getErrorCode();
							if (code == 8144 || code == 201) {
								try {
									CallableStatement cs = conn.prepareCall("{call " + procName + "(?,?)}");
									cs.setString(1, locale);
									cs.setString(2, loginBean.getId());
									cs.execute();
								} catch (Exception ex) {
									if (code == 8144) {
										rs.setRetVal(GlobalsTool.getMessage(request, "report.lb.procParamsTooLittle"));
										System.out.println(GlobalsTool.getMessage(request, "report.lb.procParamsTooLittle"));
										BaseEnv.log.error("ReportDataMgt.execTemplateProc Error:",ex);

									} else if (code == 201) {
										rs.setRetVal(GlobalsTool.getMessage(request, "report.lb.procParamsTooMany"));
										System.out.println(GlobalsTool.getMessage(request, "report.lb.procParamsTooMany"));
										BaseEnv.log.error("ReportDataMgt.execTemplateProc Error:",ex);
									}
								}
							} else {
								BaseEnv.log.error("ReportDataMgt.execTemplateProc Error:",e);
								rs.setRetVal(e.getMessage());
								rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								return;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ReportDataMgt.execTemplateProc Error:",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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

	public Result getPageSQL(final String sql, final int pageNo, final int pageSize, final ArrayList paramList, String orderByContent, String fieldAS, ArrayList queryFields, int showPageTotal,
			String orderAS, DefineReportBean defBean, HttpServletRequest request, int crossNum) {
		final Result rs = new Result();
		rs.pageNo = pageNo;
		rs.pageSize = pageSize;
		String keyId = sql.substring("select".length(), sql.indexOf(" ", "select".length() + 1)).trim();
		int fromS = 0;
		for (int i = 0; i < queryFields.size(); i++) {
			ReportField rf = (ReportField) queryFields.get(i);
			if (rf.getFieldName().contains(" from ")) {
				int indexS = sql.indexOf(rf.getFieldName());
				if (indexS > fromS) {
					fromS = indexS + rf.getFieldName().length();
				}
			}
		}

		/***********************************取交叉报表的页数*******************************************/
		long time = System.currentTimeMillis();
		int pageSizeS = 1;
		int pageSizeE = 1;
		String crossTotalSql = "";
		String groupCols = "';'";
		int crossTotalRow = 0;
		if (defBean.isCross() && (defBean.getDisFields().indexOf(defBean.getCrossField()) > -1 || crossNum > 0)) {
			for (int j = 0; j < defBean.getDisFields().size(); j++) {
				ReportField field = (ReportField) defBean.getDisFields().get(j);
				if (field.getCrossField() == 0 && !field.getFieldName().contains("select")) {
					if (field.getFieldType() == 0 || field.getFieldType() == 1) {
						groupCols += "+cast(" + field.getFieldName() + " as varchar(30))+';'";
					} else {
						groupCols += "+isnull(" + field.getFieldName() + ",'')+';'";
					}
				}
			}

			String fromAfter;
			if (sql.indexOf("order by", fromS) > 0) {
				fromAfter = sql.substring(sql.indexOf("from", fromS), sql.indexOf("order by", fromS));
			} else {
				fromAfter = sql.substring(sql.indexOf("from", fromS));
			}

			if (groupCols.length() > 0 && orderByContent.length() > 0) {
				groupCols = "cast(" + orderByContent.substring(9).replaceAll(",", "+") + " as varchar(8000))+" + groupCols;
			}
			if (crossNum > 0) {//布匹固定列数
				final String esql = "select " + (showPageTotal == 1 ? "" : "top " + (pageSize * pageNo)) + " count(0) " + fromAfter + " group by " + groupCols + " order by " + groupCols;
				pageSizeS = 0;
				pageSizeE = 0;
				AIODBManager mgt = new AIODBManager();
				Result rst = mgt.sqlList(esql, paramList);
				if (rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null) {
					List list = (List) rst.retVal;
					int rowNumS = 0;
					int rowNumE = 0;
					boolean sflag = true;
					boolean eflag = true;
					for (int i = 0; i < list.size(); i++) {
						Object obj = ((Object[]) list.get(i))[0];
						int count = Integer.parseInt(obj.toString());//每组数据所包含的数据个数
						int currRowNum = (count % crossNum > 0 ? count / crossNum + 1 : count / crossNum);//按10个一组，当前组可以拆分成几行数据

						if (sflag) {
							if ((rowNumS + currRowNum) > pageSize * (pageNo - 1)) {
								pageSizeS = pageSizeS + (pageSize * (pageNo - 1) - rowNumS) * currRowNum + 1;
								sflag = false;
							} else if ((rowNumS + currRowNum) < pageSize * (pageNo - 1)) {
								rowNumS = rowNumS + currRowNum;
								pageSizeS = pageSizeS + count;
							} else if ((rowNumS + currRowNum) == pageSize * (pageNo - 1)) {
								pageSizeS = pageSizeS + count + 1;
								sflag = false;
							}
						}
						if (eflag) {
							if ((rowNumE + currRowNum) > pageSize * pageNo) {
								pageSizeE = pageSizeE + (pageSize * pageNo - rowNumE) * currRowNum;
								break;
							} else if ((rowNumE + currRowNum) < pageSize * pageNo) {
								rowNumE = rowNumE + currRowNum;
								pageSizeE = pageSizeE + count;
							} else if ((rowNumE + currRowNum) == pageSize * pageNo) {
								pageSizeE = pageSizeE + count;
								break;
							}
						}
						if (showPageTotal == 1) {
							crossTotalRow += currRowNum;
						}
					}
				}
				request.setAttribute("CrossPageSize", pageSizeE - pageSizeS + 1);
			} else {//工贸不固定列数        	
				crossTotalSql = "select count(0) from (select count(0) as count,ROW_NUMBER() over(order by " + groupCols + ") as row_id " + fromAfter + " group by " + groupCols + ") as a";
				final String ssql = "select sum(count) from (select top " + (pageSize * (pageNo - 1)) + " count(0) as count  " + fromAfter + " group by " + groupCols + " order by " + groupCols
						+ ") as a";
				final String esql = "select sum(count) from (select top " + (pageSize * pageNo) + " count(0) as count  " + fromAfter + " group by " + groupCols + " order by " + groupCols + ") as a";
				int retCode = DBUtil.execute(new IfDB() {
					public int exec(Session session) {
						session.doWork(new Work() {
							public void execute(Connection connection) throws SQLException {
								Connection conn = connection;
								try {
									int scount = 0;
									int ecount = 0;

									PreparedStatement st = conn.prepareStatement(ssql);
									for (int i = 1; i <= paramList.size(); i++) {
										st.setString(i, paramList.get(i - 1).toString());
									}
									ResultSet rset = st.executeQuery();
									if (rset.next()) {
										scount = rset.getInt(1);
									}

									st = conn.prepareStatement(esql);
									for (int i = 1; i <= paramList.size(); i++) {
										st.setString(i, paramList.get(i - 1).toString());
									}
									rset = st.executeQuery();
									if (rset.next()) {
										ecount = rset.getInt(1);
									}
									int[] count = { scount + 1, ecount };
									rs.setRetVal(count);
								} catch (SQLException ex) {
									rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
									BaseEnv.log.error("Query data Error :" + ssql, ex);
									return;
								}
							}
						});
						return rs.getRetCode();
					}
				});
				rs.retCode = retCode;
				if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
					return rs;
				}

				int[] count = (int[]) rs.getRetVal();
				pageSizeS = count[0];
				pageSizeE = count[1];
			}
			BaseEnv.log.error("取交叉报表分页页数使用时间" + (System.currentTimeMillis() - time));
		}
		defBean.setPageSizeS(pageSizeS);
		defBean.setPageSizeE(pageSizeE);

		time = System.currentTimeMillis();
		if (showPageTotal == 1) {
			if (crossTotalRow > 0) {
				rs.realTotal = crossTotalRow;
			} else {
				final String countsql = crossTotalSql.length() > 0 ? crossTotalSql : "select count(0) from (" + sql + ") as a";
				int retCode = DBUtil.execute(new IfDB() {
					public int exec(Session session) {
						session.doWork(new Work() {
							public void execute(Connection connection) throws SQLException {
								Connection conn = connection;
								try {
									PreparedStatement st = conn.prepareStatement(countsql);
									for (int i = 1; i <= paramList.size(); i++) {
										st.setString(i, paramList.get(i - 1).toString());
									}
									ResultSet rset = st.executeQuery();
									if (rset.next()) {
										rs.realTotal = rset.getInt(1);
									}
								} catch (SQLException ex) {
									rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
									rs.retVal = ex.getMessage();
									BaseEnv.log.error("Query data Error :" + countsql, ex);
									return;
								}
							}
						});
						return rs.getRetCode();
					}
				});
				rs.retCode = retCode;
			}
			rs.totalPage = ((rs.realTotal + pageSize) - 1) / pageSize;
			if (rs.pageNo > rs.totalPage)
				rs.pageNo = rs.totalPage;
			if (rs.pageNo < 1)
				rs.pageNo = 1;
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				return rs;
			}
			BaseEnv.log.debug("取总页数时间" + (System.currentTimeMillis() - time));
		}

		String querysql = "";
		if (keyId.length() > 0) {
			keyId = "keyId,createBy,deptCodeR,employeeId,deptCodeRE,";
		}
		if (sql.indexOf("isCatalog as childCount") >= 0) {
			keyId += "classCode,childCount,";
		}
		if (pageSize == -1) {
			querysql = sql + " " + orderByContent;
		} else {
			if(sql.indexOf("from", fromS)== -1){
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal="未找到from，请注意大小写";
				return rs;
			}
			if (defBean.isCross() && defBean.getDisFields().indexOf(defBean.getCrossField()) > -1 || crossNum > 0) {
				if (orderAS != null && orderAS.length() > 0 && sql.contains("CRMClientInfoEmp")) {
					/*此处加distinct主要为了过滤CRM中重复问题*/
					querysql = "select distinct " + keyId + fieldAS + " from ( " + sql.substring(0, sql.indexOf("from", fromS)) + "  ,ROW_NUMBER() over(order by " + groupCols + ") as row_id  "
							+ sql.substring(sql.indexOf("from", fromS)) + " )  a " + " where row_id between " + pageSizeS + " and " + (pageSizeE + 1) + orderAS;
				} else {
					querysql = "select " + keyId + fieldAS + " from ( " + sql.substring(0, sql.indexOf("from", fromS)) + "  ,ROW_NUMBER() over(order by " + groupCols + ") as row_id  "
							+ sql.substring(sql.indexOf("from", fromS)) + " )  a " + " where row_id between " + pageSizeS + " and " + (pageSizeE + 1)+" order by row_id";
				}
			} else {
				if (orderAS != null && orderAS.length() > 0 && sql.contains("CRMClientInfoEmp")) {
					/*此处加distinct主要为了过滤CRM中重复问题*/
					querysql = "select distinct " + keyId + fieldAS + " from ( " + sql.substring(0, sql.indexOf("from", fromS)) + "  ,ROW_NUMBER() over(" + orderByContent + ") as row_id  "
							+ sql.substring(sql.indexOf("from", fromS)) + " )  a " + " where row_id between " + (pageSize * (pageNo - 1) + 1) + " and " + (pageSize * pageNo + 1) + orderAS;
				} else {
					querysql = "select " + keyId + fieldAS + " from ( " + sql.substring(0, sql.indexOf("from", fromS)) + "  ,ROW_NUMBER() over(" + orderByContent + ") as row_id  "
							+ sql.substring(sql.indexOf("from", fromS)) + " )  a " + " where row_id between " + (pageSize * (pageNo - 1) + 1) + " and " + (pageSize * pageNo + 1)+" order by row_id";
				}
			}
		}
		rs.setRetVal(querysql);

		return rs;
	}

	/**
	 * 根据表名获取列表查询审核状态的设置值
	 * @param tableName String 表名
	 */
	public Result getBillApproveStatus(final String tableName) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = " select AuditStatus from tblBillSet where NameOfTable=?";
							PreparedStatement psmt = connection.prepareStatement(sql);
							psmt.setString(1, tableName);
							ResultSet rst = psmt.executeQuery();
							if (rst.next()) {
								res.retVal = rst.getString("AuditStatus");
								res.retCode = ErrorCanst.DEFAULT_SUCCESS;
							} else {
								res.retCode = ErrorCanst.DEFAULT_FAILURE;
							}
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt getBillApproveStatus method:", ex);
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		return res;
	}

	/**
	 * 根据表名获取表所有的数据
	 * @param tableName
	 * @return
	 */
	public Result getTableData(final DBTableInfoBean tableInfo, final String fieldList, final String condition) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuilder sbData = new StringBuilder();
							String sql = "select * from " + tableInfo.getTableName() + " td where 1=1 ";
							if (condition != null && condition.length() > 0) {
								sql += condition;
							}
							if ("tblStockDet".equals(tableInfo.getTableName())) {
								sql += "and itemNO =(select max(itemNO) from tblstockDet where goodsCode = td.goodsCode and  stockCode = td.stockCode "
										+ " and batchno = td.batchno and yearNO = td.YearNO and color= td.color and seq = td.seq and Inch = td.Inch and  Hue = td.Hue and Availably=td.Availably and ProDate=td.ProDate)";
							}
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							while (rss.next()) {
								sbData.append("<row ");
								List<DBFieldInfoBean> list = tableInfo.getFieldInfos();
								for (DBFieldInfoBean dbField : list) {
									if (fieldList != null && fieldList.trim().length() > 0) {
										if (!fieldList.contains(dbField.getFieldName() + ",")) {
											continue;
										}
									}
									Object object = rss.getObject(dbField.getFieldName());
									if (object != null && object.toString() != null) {
										object = object.toString().replace("\"", "&quot;");
										object = object.toString().replace("<", "&lt;");
										object = object.toString().replace(">", "&gt;");
										object = object.toString().replace("&", "&amp;");
									}
									sbData.append(dbField.getFieldName().toUpperCase() + "=\"" + object + "\" ");
								}
								sbData.append(" />\n");
							}
							res.setRetVal(sbData);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt getTableData method:", ex);
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		return res;
	}

	/**
	 * 获取二维表字段名和显示名
	 * @param tableName
	 * @return
	 */
	public Result queryViewStockDistributing(final String sql) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							ArrayList<String[]> stockList = new ArrayList<String[]>();
							while (rss.next()) {
								String[] stock = new String[2];
								stock[0] = rss.getString(1);
								stock[1] = rss.getString(2);
								stockList.add(stock);
							}
							res.setRetVal(stockList);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt queryViewStockDistributing method:", ex);
						}
					}
				});
				return res.getRetCode();
			}
		});
		return res;
	}

	/**
	 * 根据门店ID查询门店信息
	 * @param shopId
	 * @return
	 */
	public Result queryReatailShopByID(final String shopId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select defaultStock,defaultCompany from tblShop where id=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, shopId);
							ResultSet rss = pss.executeQuery();
							String[] shop = new String[2];
							if (rss.next()) {
								shop[0] = rss.getString("defaultStock");
								shop[1] = rss.getString("defaultCompany");
							}
							result.setRetVal(shop);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt queryReatailShopByID method:", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	/**
	 * 根据门店ID和零售单ID查询该零售单是否已经上传
	 * @param shopId
	 * @return
	 */
	public Result queryExistReatailByID(final String reatailId, final String shopId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblSalesOutStock where reatailId=? and shopId=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, reatailId);
							pss.setString(2, shopId);
							ResultSet rss = pss.executeQuery();
							if (rss.next()) {
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							} else {
								result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							}
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt queryExistReatailByID method:", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	/**
	 * 更新会员卡积分
	 * @param shopId
	 * @return
	 */
	public Result updateCardConsumeIntegral(final String cardNo, final double monty) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select consumeIntegral from tblIntegralSetingdet where ?>startAmount and ?<=endAmount "
									+ "and f_ref=(select id from tblIntegralSeting where cardType=(select cardType from tblOpenCard where cardNo = ?))";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setDouble(1, monty);
							pss.setDouble(2, monty);
							pss.setString(3, cardNo);
							ResultSet rssQuery = pss.executeQuery();
							Double integral = 0.0;
							if (rssQuery.next()) {
								integral = rssQuery.getDouble("consumeIntegral");
							} else {
								sql = "select max(endAmount),max(consumeIntegral) from tblIntegralSetingdet where f_ref=(select id from tblIntegralSeting where cardType=(select cardType from tblOpenCard where cardNo = ?))";
								pss = conn.prepareStatement(sql);
								pss.setString(1, cardNo);
								rssQuery = pss.executeQuery();
								if (rssQuery.next()) {
									double maxAmount = rssQuery.getDouble(1);
									double maxIntegral = rssQuery.getDouble(2);
									if (monty > maxAmount) {
										integral = maxIntegral;
									}
								}
							}
							sql = "update tblOpenCard set ConsumeIntegral=ConsumeIntegral+? where CardNO=?";
							pss = conn.prepareStatement(sql);
							pss.setDouble(1, integral);
							pss.setString(2, cardNo);
							int n = pss.executeUpdate();
							if (n > 0) {
								result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt updateCardConsumeIntegral method:", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	/**
	 * 根据用户Id查询用户所在的门店
	 * @param shopId
	 * @return
	 */
	public Result queryShopIdByUserId(final String userId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,ShopName from tblShop where id in (select f_ref from tblShopDet where employeeId=?);";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, userId);
							ResultSet rss = pss.executeQuery();
							ArrayList<String[]> listShop = new ArrayList<String[]>();
							while (rss.next()) {
								String[] shop = new String[2];
								shop[0] = rss.getString("id");
								shop[1] = rss.getString("ShopName");
								listShop.add(shop);
							}
							result.setRetVal(listShop);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt queryShopIdByUserId method:", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	/**
	 * 查询老板报表数据
	 * @param shopId
	 * @return
	 */
	public Result queryBossReport(final String startDate, final String endDate, final String locale, final String userId, final String scompayId, final String deptName) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							CallableStatement cs = null;
							if (BaseEnv.version == 8 || BaseEnv.version == 12) {//工贸版增加部门查询条件
								cs = conn.prepareCall("{call proc_ReportBoss(?,?,?,?,?,?)}");
							} else {
								cs = conn.prepareCall("{call proc_ReportBoss(?,?,?,?,?)}");
							}
							cs.setString(1, locale);
							cs.setString(2, userId);
							cs.setString(3, scompayId);
							cs.setString(4, startDate);
							cs.setString(5, endDate);
							if (BaseEnv.version == 8 || BaseEnv.version == 12) {
								cs.setString(6, deptName);
							}
							cs.execute();
							String sql = "select * from ReportBossTemp";
							Statement state = conn.createStatement();
							ResultSet rss = state.executeQuery(sql);
							HashMap values = new HashMap();
							if (rss.next()) {
								values.put("BuyQty", GlobalsTool.round(rss.getDouble("BuyQty"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyQty")));
								values.put("BuyReturn", GlobalsTool.round(rss.getDouble("BuyReturn"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyReturn")));
								values.put("SalesQty", GlobalsTool.round(rss.getDouble("SalesQty"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesQty")));
								values.put("SalesReturn", GlobalsTool.round(rss.getDouble("SalesReturn"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesReturn")));
								values.put("BuyOrder", GlobalsTool.round(rss.getDouble("BuyOrder"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyOrder")));
								values.put("BuyIn", GlobalsTool.round(rss.getDouble("BuyIn"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyIn")));
								values.put("BuyNoIn", GlobalsTool.round(rss.getDouble("BuyNoIn"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyNoIn")));
								values.put("SalesOrder", GlobalsTool.round(rss.getDouble("SalesOrder"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesOrder")));
								values.put("SalesOut", GlobalsTool.round(rss.getDouble("SalesOut"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesOut")));
								values.put("SalesNoOut", GlobalsTool.round(rss.getDouble("SalesNoOut"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesNoOut")));
								values.put("LastQty", GlobalsTool.round(rss.getDouble("LastQty"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "LastQty")));
								values.put("LastAmount", GlobalsTool.round(rss.getDouble("LastAmount"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "LastAmount")));
								values.put("Receive", GlobalsTool.round(rss.getDouble("Receive"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "Receive")));
								values.put("Pay", GlobalsTool.round(rss.getDouble("Pay"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "Pay")));
								values.put("SalesInCome", GlobalsTool.round(rss.getDouble("SalesInCome"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesInCome")));
								values.put("OrtherInCome", GlobalsTool.round(rss.getDouble("OrtherInCome"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "OrtherInCome")));
								values.put("ComExpensed", GlobalsTool.round(rss.getDouble("ComExpensed"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "ComExpensed")));
								values.put("OrtherExpensed", GlobalsTool.round(rss.getDouble("OrtherExpensed"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "OrtherExpensed")));
								values.put("InComeExpensed", GlobalsTool.round(rss.getDouble("InComeExpensed"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "InComeExpensed")));
								values.put("BuyPieces", GlobalsTool.round(rss.getDouble("BuyPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyPieces")));
								values.put("BuyInAmount", GlobalsTool.round(rss.getDouble("BuyInAmount"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyInAmount")));
								values.put("BuyReturnPieces", GlobalsTool.round(rss.getDouble("BuyReturnPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyReturnPieces")));
								values.put("BuyRtnAmount", GlobalsTool.round(rss.getDouble("BuyRtnAmount"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyRtnAmount")));
								values.put("SalesPieces", GlobalsTool.round(rss.getDouble("SalesPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesPieces")));
								values.put("SalesAmount", GlobalsTool.round(rss.getDouble("SalesAmount"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesAmount")));
								values.put("SalesReturnPieces", GlobalsTool.round(rss.getDouble("SalesReturnPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesReturnPieces")));
								values.put("SalesReturnAmount", GlobalsTool.round(rss.getDouble("SalesReturnAmount"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesReturnAmount")));
								if (BaseEnv.version == 3) {
									values.put("SalesRetail", GlobalsTool.round(rss.getDouble("SalesRetail"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesRetail")));
									values.put("SalesRetailPieces", GlobalsTool.round(rss.getDouble("SalesRetailPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesRetailPieces")));
									values.put("SalesRetailAmount", GlobalsTool.round(rss.getDouble("SalesRetailAmount"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesRetailAmount")));
									values.put("SalesRetailGrossProfit", GlobalsTool.round(rss.getDouble("SalesRetailGrossProfit"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp",
											"SalesRetailGrossProfit")));
								}

								values.put("SalesGrossProfit", GlobalsTool.round(rss.getDouble("SalesGrossProfit"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesGrossProfit")));
								values.put("SalesRtnGrossProfit", GlobalsTool.round(rss.getDouble("SalesRtnGrossProfit"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesRtnGrossProfit")));
								values.put("BuyOrderPieces", GlobalsTool.round(rss.getDouble("BuyOrderPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyOrderPieces")));
								values.put("BuyInPieces", GlobalsTool.round(rss.getDouble("BuyInPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyInPieces")));
								values.put("BuyNoInPieces", GlobalsTool.round(rss.getDouble("BuyNoInPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "BuyNoInPieces")));
								values.put("SalesOrderPieces", GlobalsTool.round(rss.getDouble("SalesOrderPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesOrderPieces")));
								values.put("SalesOutPieces", GlobalsTool.round(rss.getDouble("SalesOutPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesOutPieces")));
								values.put("SalesNoOutPieces", GlobalsTool.round(rss.getDouble("SalesNoOutPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "SalesNoOutPieces")));
								values.put("LastPieces", GlobalsTool.round(rss.getDouble("LastPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "LastPieces")));
								values.put("NowQty", GlobalsTool.round(rss.getDouble("NowQty"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "NowQty")));
								values.put("NowPieces", GlobalsTool.round(rss.getDouble("NowPieces"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "NowPieces")));
								values.put("NowAmount", GlobalsTool.round(rss.getDouble("NowAmount"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "NowAmount")));
								values.put("PayEnd", GlobalsTool.round(rss.getDouble("PayEnd"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "PayEnd")));
								values.put("AccInFlow", GlobalsTool.round(rss.getDouble("AccInFlow"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "AccInFlow")));
								values.put("AccOutFlow", GlobalsTool.round(rss.getDouble("AccOutFlow"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "AccOutFlow")));
								values.put("AccNetIncome", GlobalsTool.round(rss.getDouble("AccNetIncome"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "AccNetIncome")));
								values.put("ReceiveEnd", GlobalsTool.round(rss.getDouble("ReceiveEnd"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "ReceiveEnd")));
								values.put("PayBegin", GlobalsTool.round(rss.getDouble("PayBegin"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "PayBegin")));
								values.put("ReceiveBegin", GlobalsTool.round(rss.getDouble("ReceiveBegin"), GlobalsTool.getDigitsOrSysSetting("ReportBossTemp", "ReceiveBegin")));
								values.put("otherInAccCode",java.net.URLEncoder.encode(rss.getString("otherInAccCode"), "UTF-8"));
								values.put("otherInhideAccCode",java.net.URLEncoder.encode(rss.getString("otherInhideAccCode"), "UTF-8"));
								values.put("otherInAccNumber",java.net.URLEncoder.encode(rss.getString("otherInAccNumber"), "UTF-8"));
								values.put("otherInAccName",java.net.URLEncoder.encode(rss.getString("otherInAccName"), "UTF-8"));
								values.put("feeAccCode",java.net.URLEncoder.encode(rss.getString("feeAccCode"), "UTF-8"));
								values.put("feehideAccCode",java.net.URLEncoder.encode(rss.getString("feehideAccCode"), "UTF-8"));
								values.put("feeAccNumber",java.net.URLEncoder.encode(rss.getString("feeAccNumber"), "UTF-8"));
								values.put("feeAccName",java.net.URLEncoder.encode(rss.getString("feeAccName"), "UTF-8"));
								values.put("otherFeeAccCode",java.net.URLEncoder.encode(rss.getString("otherFeeAccCode"), "UTF-8"));
								values.put("otherFeehideAccCode",java.net.URLEncoder.encode(rss.getString("otherFeehideAccCode"), "UTF-8"));
								values.put("otherFeeAccNumber",java.net.URLEncoder.encode(rss.getString("otherFeeAccNumber"), "UTF-8"));
								values.put("otherFeeAccName",java.net.URLEncoder.encode(rss.getString("otherFeeAccName"), "UTF-8"));
								values.put("deptName",java.net.URLEncoder.encode(deptName, "UTF-8"));
								
								result.setRetVal(values);
							}
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt queryBossReport method:", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	public String getClassCode(final String tableName, final String fieldName, final String values) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "";
							String value = values;
							if (value.startsWith("'")) {
								value = value.substring(1, value.length() - 1);
							}
							if (value.length() == 0) {
								return;
							}
							for (String val : value.split(",")) {
								if (val.length() > 0) {
									sql += " Select classCode from " + tableName + " where 1=1 and isCatalog=0 and (" + fieldName + " like '%" + val + "%') " + " union  Select g.classCode from "
											+ tableName + " g , ( Select classCode from " + tableName + " where isCatalog=1 and (" + fieldName + " like '%" + val
											+ "%')) c where g.classcode like c.classcode+'%' and g.isCatalog=0 union";
								}
							}
							if (sql.length() > 0) {
								sql = sql.substring(0, sql.length() - 5);
							}
							sql = "select classCode from (" + sql + ") cc group by classCode ";
							PreparedStatement state = conn.prepareStatement(sql);

							BaseEnv.log.debug("ReportDataMgt.getClassCode " + sql);
							ResultSet rss = state.executeQuery();
							String ret = "";
							while (rss.next()) {
								ret += "'" + rss.getString(1) + "',";
							}
							if (ret.length() > 0) {
								ret = ret.substring(0, ret.length() - 1);
							} else {
								ret = "''";
							}
							result.retVal = ret;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ReportDataMgt queryBossReport method:", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		String ret = (String) result.retVal;
		return ret == null || ret.length() == 0 ? "''" : ret;
	}

}
