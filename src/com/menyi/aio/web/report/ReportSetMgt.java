package com.menyi.aio.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.bean.ReportsDetBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConnectionEnv;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GenJS;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.ReportField;

/**
 * <p>Title: </p>
 *
 * <p>Description: 报表设置的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0 
 */
public class ReportSetMgt extends DBManager {

	//存放新增报表bean的id
	public static String reportId = "";

	//存放更新报表脚本
	public static String upReportId = "";

	//存放新增报表名称多语言id
	public static String reportName = "";

	//存放更新报表名称多语言id
	public static String upReportName = "";

	/**
	 * 填加一条报表设置记录
	 * @param id long
	 * @param name String
	 * @return Result
	 */
	public Result add(ReportSetForm form, HttpServletRequest request, String createBy, String locale) {

		//设置多语言
		final KRLanguage krLan = KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
				org.apache.struts.Globals.LOCALE_KEY), form.getReportName());
		ReportsBean bean = new ReportsBean();
		String mainID = IDGenerater.getId();
		bean.setId(mainID);
		bean.setReportNumber(form.getReportNumber());
		bean.setReportName(krLan.getId());
		bean.setReportType(form.getReportType());
		bean.setBillTable(form.getBillTable());
		bean.setProcName(form.getProcName());
		bean.setSQLFileName(form.getReportNumber() + "SQL.xml");
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		bean.setCreateBy(createBy);
		bean.setLastUpdateBy(createBy);
		bean.setStatusId(new Integer(0));
		bean.setPageSize(form.getPageSize());
		bean.setShowTotalPage(form.getShowTotalPage());
		bean.setShowTotalStat(form.getShowTotalStat());
		bean.setAuditPrint(form.getAuditPrint());
		bean.setDefaultNoshow(form.getDefaultNoshow());
		bean.setColTitleSort(form.getColTitleSort());
		bean.setFixListTitle(form.getFixListTitle());
		bean.setFixNumberCol(form.getFixNumberCol());
		bean.setEndClassNumber(form.getEndClassNumber());
		bean.setListType(form.getListType());
		bean.setNewFlag("NEW");
		bean.setShowCondition(form.getShowCondition());
		bean.setCrossColNum(form.getCrossColNum());
		bean.setModuleType(form.getModuleType());
		bean.setShowHead(form.getShowHead());
		bean.setShowRowNumber(form.getShowRowNumber());
		bean.setParentLinkReport(form.getParentLinkReport());
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable session = ((Hashtable) sessionSet.get(createBy));
		//bean.setMainModule(Integer.parseInt((String) session.get("sysType")));
		bean.setShowDet(1);
		bean.setExtendsBut(form.getExtendsBut());
		Map<String, ReportsDetBean> reportDetBean = new HashMap<String, ReportsDetBean>();
		for (int i = 0; form.getFormatFileName() != null && i < form.getFormatFileName().length; i++) {
			ReportsDetBean detBean = new ReportsDetBean();
			detBean.setId(IDGenerater.getId());
			detBean.setFormatFileName(form.getReportNumber() + "Format_" + detBean.getId() + ".xml");
			detBean.setFormatName(form.getFormatFileName()[i]);
			detBean.setNewFlag("NEW");
			detBean.setLanguageType(locale);
			detBean.setReferBean(bean);
			reportDetBean.put(IDGenerater.getId(), detBean);
		}
		bean.setReportDetBean(reportDetBean);
		reportId = bean.getId();
		reportName = bean.getReportName();
		//执行插入多语言操作
		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							KRLanguageQuery.saveToDB(krLan.map, krLan.getId(), conn);
							rs_lan.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs_lan.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		//执行数据库插入操作
		Result rs_add = new Result();
		if (rs_lan.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			rs_add = addBean(bean);
		}
		return rs_add;
	}

	/**
	 * 修改一条单位记录
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result update(ReportSetForm form, HttpServletRequest request, String updateBy) {
		//存放保存某报表对应的报表详细里的所有数据

		//先查出相应的报表记录
		Result rs = loadBean(form.getId(), ReportsBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			final ReportsBean bean = (ReportsBean) rs.retVal;
			bean.setReportName(form.getReportName());
			bean.setReportType(form.getReportType());
			bean.setBillTable(form.getBillTable());
			bean.setProcName(form.getProcName());
			bean.setLastUpdateBy(updateBy);
			bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			bean.setStatusId(Integer.valueOf(form.getStatusId()));
			bean.setPageSize(form.getPageSize());
			bean.setShowTotalPage(form.getShowTotalPage());
			bean.setShowTotalStat(form.getShowTotalStat());
			bean.setAuditPrint(form.getAuditPrint());
			bean.setDefaultNoshow(form.getDefaultNoshow());
			bean.setColTitleSort(form.getColTitleSort());
			bean.setFixListTitle(form.getFixListTitle());
			bean.setFixNumberCol(form.getFixNumberCol());
			bean.setEndClassNumber(form.getEndClassNumber());
			bean.setListType(form.getListType());
			bean.setShowCondition(form.getShowCondition());
			bean.setCrossColNum(form.getCrossColNum());
			bean.setModuleType(form.getModuleType());
			bean.setShowHead(form.getShowHead());
			bean.setShowRowNumber(form.getShowRowNumber());
			bean.setParentLinkReport(form.getParentLinkReport());
			bean.setShowDet(1);
			bean.setExtendsBut(form.getExtendsBut());
			Map<String, ReportsDetBean> reportDetBeanO = bean.getReportDetBean();//原明细表中数据
			Collection<ReportsDetBean> values = reportDetBeanO.values();
			Iterator it = values.iterator();
			//封装所有的ReportsDetBean数据
			Map<String, ReportsDetBean> reportNewDetBean = new HashMap<String, ReportsDetBean>();
			while (it.hasNext()) {
				ReportsDetBean item = (ReportsDetBean) it.next();
				//将原中ReportsDetBean数据保存起来
				reportNewDetBean.put(item.getId(), item);
				String fileO = item.getFormatName();
				boolean flag = true;
				for (int i = 0; form.getFormatFileName() != null && i < form.getFormatFileName().length; i++) {
					String file = form.getFormatFileName()[i];
					if (file.equals(fileO)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					this.deleteBean(item.getId(), item.getClass(), "id");
				}
			}

			it = values.iterator();
			Map<String, ReportsDetBean> reportDetBean = new HashMap<String, ReportsDetBean>();

			for (int i = 0; form.getFormatFileName() != null && i < form.getFormatFileName().length; i++) {
				boolean flag = true;
				while (it.hasNext()) {
					ReportsDetBean item = (ReportsDetBean) it.next();
					if (item.getFormatName().equals(form.getFormatFileName()[i])) {
						flag = false;
						break;
					}
				}
				if (flag) {
					ReportsDetBean detBean = new ReportsDetBean();
					detBean.setId(IDGenerater.getId());
					detBean.setFormatFileName(bean.getReportNumber() + "Format_" + detBean.getId() + ".xml");
					detBean.setFormatName(form.getFormatFileName()[i]);
					detBean.setNewFlag("NEW");
					detBean.setReferBean(bean);
					reportDetBean.put(IDGenerater.getId(), detBean);
					//封装新的ReportsDetBean数据
					reportNewDetBean.put(IDGenerater.getId(), detBean);
				}
			}

			bean.setReportDetBean(reportDetBean);

			//设置多语言
			final KRLanguage krLan = KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
					org.apache.struts.Globals.LOCALE_KEY), form.getReportName());
			bean.setReportName(krLan.getId());
			//执行插入多语言操作
			final Result rs_lan = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							try {
								String sql = "delete from tblLanguage where id = ?";
								PreparedStatement ps = conn.prepareStatement(sql);
								ps.setString(1, bean.getReportName());
								ps.executeUpdate();
								KRLanguageQuery.saveToDB(krLan.map, krLan.getId(), conn);
								rs_lan.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} catch (Exception ex) {
								ex.printStackTrace();
								rs_lan.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								return;
							}
						}
					});
					return rs_lan.getRetCode();
				}
			});
			rs_lan.setRetCode(retCode);
			//修改记录
			rs = updateBean(bean);
			//获得更新报表的脚本
			bean.setReportDetBean(reportNewDetBean);
			upReportId = bean.getId();
			upReportName = bean.getReportName();
		}
		return rs;
	}

	/**
	 * 修改一条单位记录
	 * @param id long
	 * @param name String
	 * @return Result
	 */
	public Result copyStyle(String keyId, String FormatFileName, String styleName, String updateBy) {
		//先查出相应的报表记录
		Result rs = loadBean(keyId, ReportsBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			ReportsBean bean = (ReportsBean) rs.retVal;
			String id = IDGenerater.getId();
			String fileName = bean.getReportNumber() + "Format_" + id + ".xml";

			//复制文件
			File file = new File(BaseEnv.FILESERVERPATH + "/report/" + FormatFileName);
			File fileNew = new File(BaseEnv.FILESERVERPATH + "/report/" + fileName);

			try {
				OutputStream out = new FileOutputStream(fileNew);
				FileInputStream in = new FileInputStream(file);
				byte[] bs = new byte[1024];
				int count = 0;
				while ((count = in.read(bs)) > -1) {
					out.write(bs, 0, count);
				}

				out.close();
				in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				return rs;
			}

			Map<String, ReportsDetBean> reportDetBeanO = bean.getReportDetBean();//原明细表中数据

			ReportsDetBean detBean = new ReportsDetBean();
			detBean.setId(id);
			detBean.setFormatFileName(fileName);
			detBean.setFormatName(styleName);
			detBean.setNewFlag("OLD");
			detBean.setReferBean(bean);
			reportDetBeanO.put(IDGenerater.getId(), detBean);

			bean.setReportDetBean(reportDetBeanO);
			//修改记录
			Result rs2 = updateBean(bean);
			rs.setRetCode(rs2.retCode);
			//获得更新报表的脚本
			upReportId = bean.getId();

		}
		return rs;
	}

	/**
	 * 删除多条单位记录
	 * @param ids long[]
	 * @return Result
	 */
	public Result delete(String[] ids) {
		return deleteBean(ids, ReportsBean.class, "id");
	}

	/**
	 * 查询单位记录
	 * @param name String
	 * @param pageNo int
	 * @param pageSize int
	 * @return Result
	 */
	public Result query(final String name, String reportNumber, String statusId, String reportType, String reportModule, int pageNo, int pageSize, final String locale, final String defSys) {

		//创建参数
		List param = new ArrayList();
		String hql = "select bean from ReportsBean as bean";
		String query = "";
		//如标题不为空，则做标题模糊查询
		final Result rs_id = new Result();
		if (name != null && name.length() != 0) {
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							try {
								ArrayList<String> listId = new ArrayList<String>();
								String sql = "select reportName from tblReports a left join tblLanguage b on a.ReportName = b.id where zh_CN like ?";
								PreparedStatement ps = conn.prepareStatement(sql);
								ps.setString(1, "%" + name + "%");
								ResultSet rs = ps.executeQuery();
								while (rs.next()) {
									listId.add(rs.getString("reportName"));
								}
								rs_id.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								rs_id.setRetVal(listId);
							} catch (Exception ex) {
								ex.printStackTrace();
								rs_id.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								rs_id.setRetVal(ex.getMessage());
								return;
							}
						}
					});
					return rs_id.getRetCode();
				}
			});
			ArrayList<String> listId = new ArrayList<String>();
			if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
				listId = (ArrayList<String>) rs_id.getRetVal();
			}
			query = " where (";
			if (listId.size() > 0) {
				for (String str : listId) {
					query += " bean.ReportName = ? or ";
					param.add(str);
				}
			}
			query += " bean.ReportNumber like '%'||?||'%' ) ";
			param.add(name.trim());
		}

		if (reportType != null && reportType.length() != 0) {
			if (query.indexOf("where") < 0) {
				query = query + " where ";
			} else {
				query = query + " and ";
			}
			query += " bean.ReportType='" + reportType + "'";
		}

		if (reportModule != null && reportModule.length() != 0) {
			if (query.indexOf("where") < 0) {
				query = query + " where ";
			} else {
				query = query + " and ";
			}
			query += " bean.mainModule=" + reportModule + " ";
		}

		if (reportNumber != null && reportNumber.length() != 0) {
			if (query.indexOf("where") < 0) {
				query = query + " where ";
			} else {
				query = query + " and ";
			}
			query += " bean.ReportNumber='" + reportNumber + "'";
		}
		if (statusId != null && statusId.length() != 0) {
			if (query.indexOf("where") >= 0) {
				query += " and bean.statusId=?";
			} else {
				query += " where bean.statusId=?";
			}
			param.add(new Integer(statusId));
		} else {
			if (query.indexOf("where") >= 0) {
				query += " and bean.statusId=?";
			} else {
				query += " where bean.statusId=?";
			}
			param.add(new Integer(0));
		}
		hql += query;
		//调用list返回结果
		final Result rs_resport = list(hql, param, pageNo, pageSize, true);
		if (rs_resport.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							try {
								KRLanguageQuery krQuery = new KRLanguageQuery();
								ArrayList<ReportsBean> reportList = (ArrayList<ReportsBean>) rs_resport.getRetVal();
								for (int i = 0; i < reportList.size(); i++) {
									ReportsBean bean = reportList.get(i);
									krQuery.addLanguageId(bean.getReportName());
								}
								HashMap map = krQuery.query(conn);
								for (int i = 0; i < reportList.size(); i++) {
									ReportsBean bean = reportList.get(i);
									KRLanguage lan = (KRLanguage) map.get(bean.getReportName());
									if (lan != null) {
										bean.setReportName(lan.get(locale));
									}
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								rs_resport.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								rs_resport.setRetVal(ex.getMessage());
								return;
							}
						}
					});
					return rs_resport.getRetCode();
				}
			});
		}
		return rs_resport;
	}

	/**
	 * 查一条单位的详细信息
	 * 
	 * @param notepadId
	 *            long 代号
	 * @return Result 返回结果
	 */
	public Result detail(String id) {
		Result rs = loadBean(id, ReportsBean.class);
		final Result rs_detail = new Result();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			final ReportsBean bean = (ReportsBean) rs.getRetVal();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							try {
								String sql = "select 'en:'+isnull(en,'')+';zh_CN:'+isnull(zh_CN,'')+';zh_TW:'+isnull(zh_TW,'')+';' from tblLanguage where id  = ?";
								PreparedStatement ps = conn.prepareStatement(sql);
								ps.setString(1, bean.getReportName());
								ResultSet res = ps.executeQuery();
								if (res.next()) {
									bean.setReportName(res.getString(1));
								}
								rs_detail.setRetVal(bean);
							} catch (Exception ex) {
								ex.printStackTrace();
								rs_detail.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								rs_detail.setRetVal(ex.getMessage());
								return;
							}
						}
					});
					return rs_detail.getRetCode();
				}
			});
			rs_detail.setRetCode(retCode);
		}
		return rs_detail;
	}

	/**
	 * 得到单据报表设置数据信息
	 * 
	 * @return Result
	 */
	public Result getBillTable(String billTable, String moduleType) {

		List param = new ArrayList();
		String hql = "select bean from ReportsBean as bean where bean.ReportType=? and bean.BillTable=? ";
		param.add("BILL");
		param.add(billTable);
		if (moduleType != null && moduleType.trim().length() > 0) {
			hql += " and (bean.moduleType=? or isnull(bean.moduleType,'')='')";
			param.add(moduleType);
		}
		return this.list(hql, param);
	}
	
	/**
	 * 查询打印样式
	 * @param reportId
	 * @return
	 */
	public Result queryFormatStyle(String tableName,String moduleType, String style,final String locale) {
		String hql  = " select bean from  ReportsBean bean where bean.ReportType='BILL' and bean.BillTable='"+tableName+"' and (isnull(bean.moduleType,'') = '"+moduleType+"' or isnull(bean.moduleType,'') = '')";
		final Result rs = this.list(hql, new ArrayList());
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							KRLanguageQuery krQuery = new KRLanguageQuery();
							for(ReportsBean bean:(ArrayList<ReportsBean>)rs.retVal){							
								krQuery.addLanguageId(bean.getReportName());							
							}
							HashMap map = krQuery.query(conn);
							for(ReportsBean bean:(ArrayList<ReportsBean>)rs.retVal){							
								KRLanguage lan = (KRLanguage) map.get(bean.getReportName());
								if (lan != null) {
									bean.setReportName(lan.get(locale));
								}						
							}							
						} catch (Exception ex) {
							BaseEnv.log.error("ReportSetMgt.queryFormatStyle Error:",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		
		return rs;
	}

	/**
	 * 查询打印样式
	 * @param reportId
	 * @return
	 */
	public Result queryFormatStyle(String reportId, String style,final String locale) {
		String hql  = " select bean from  ReportsBean bean where bean.id='"+reportId+"'";
		final Result rs = this.list(hql, new ArrayList());
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							KRLanguageQuery krQuery = new KRLanguageQuery();
							for(ReportsBean bean:(ArrayList<ReportsBean>)rs.retVal){							
								krQuery.addLanguageId(bean.getReportName());							
							}
							HashMap map = krQuery.query(conn);
							for(ReportsBean bean:(ArrayList<ReportsBean>)rs.retVal){							
								KRLanguage lan = (KRLanguage) map.get(bean.getReportName());
								if (lan != null) {
									bean.setReportName(lan.get(locale));
								}						
							}							
						} catch (Exception ex) {
							BaseEnv.log.error("ReportSetMgt.queryFormatStyle Error:",ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		
		return rs;
	}

	/**
	 * 得到报表设置数据信息
	 * @return Result
	 */
	public Result getReportSetInfo(String reportNumber) {
		List param = new ArrayList();
		String hql = "select bean from ReportsBean as bean where bean.ReportNumber=?";
		param.add(reportNumber);
		Result rs = this.list(hql, param);
		return rs;
	}

	/**
	 * 得到报表设置数据信息
	 * @return Result
	 */
	public Result getReportSetInfo(final String reportNumber, final String locale) {
		List param = new ArrayList();
		String hql = "select bean from ReportsBean as bean where bean.ReportNumber=?";
		param.add(reportNumber);
		Result rs = this.list(hql, param);
		final List list = (List) rs.getRetVal();
		final Result rs_info = new Result();
		if (list.size() > 0) {
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							try {
								ReportsBean bean = (ReportsBean) list.get(0);
								KRLanguageQuery krQuery = new KRLanguageQuery();
								krQuery.addLanguageId(bean.getReportName());
								HashMap map = krQuery.query(conn);
								KRLanguage lan = (KRLanguage) map.get(bean.getReportName());
								if (lan != null) {
									bean.setReportName(lan.get(locale));
								}
								String sql = " select count(0) from tblReports where endClassNumber=? ";
								PreparedStatement st = conn.prepareStatement(sql);
								st.setString(1, reportNumber);
								ResultSet rs = st.executeQuery();
								if (rs.next()) {
									int count = rs.getInt(1);
									if (count > 0) {
										bean.setEndClassNumber(true);
									}
								}
								//查询报表是否是未级报表
								rs_info.setRetVal(bean);
							} catch (Exception ex) {
								BaseEnv.log.error("ReportSetMgt.getReportSetInfo Error:",ex);
								rs_info.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								rs_info.setRetVal(ex.getMessage());
								return;
							}
						}
					});
					return rs_info.getRetCode();
				}
			});
			rs_info.setRetCode(retCode);
		} else {
			rs_info.setRetVal(null);
		}
		rs_info.setRealTotal(rs.getRealTotal());
		return rs_info;
	}
	
	/**
	 * 得到报表设置数据信息
	 * @return Result
	 */
	public Result getReportSetInfoUp(String reportNumber, final String locale,final String id) {
		List param = new ArrayList();
		String hql = "select bean from ReportsBean as bean where bean.ReportNumber=?";
		param.add(reportNumber);
		if(id != null && id.length() >0){
			hql +=" and bean.id <> ?";
			param.add(id);
		}
		Result rs = this.list(hql, param);
		final List list = (List) rs.getRetVal();
		final Result rs_info = new Result();
		if (list.size() > 0) {
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							try {
								ReportsBean bean = (ReportsBean) list.get(0);
								KRLanguageQuery krQuery = new KRLanguageQuery();
								krQuery.addLanguageId(bean.getReportName());
								HashMap map = krQuery.query(conn);
								KRLanguage lan = (KRLanguage) map.get(bean.getReportName());
								if (lan != null) {
									bean.setReportName(lan.get(locale));
								}
								rs_info.setRetVal(bean);
							} catch (Exception ex) {
								ex.printStackTrace();
								rs_info.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								rs_info.setRetVal(ex.getMessage());
								return;
							}
						}
					});
					return rs_info.getRetCode();
				}
			});
			rs_info.setRetCode(retCode);
		} else {
			rs_info.setRetVal(null);
		}
		return rs_info;
	}

	/**
	 * 验证是否存在相同的单据主表 前提是选择了单据报表
	 * @param operation
	 * @param fileName
	 * @return
	 */
	public Result isExistBillMain(final String billTable, final String reportType,final String moduleType, final String id) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							if(billTable != null && billTable.length() > 0 && reportType.equals("BILL")){
								Statement st = conn.createStatement();
								String sql = "select count(0) as count from tblReports where reportType='" + reportType + "' and billTable='" + billTable + "' and isNull(moduleType,'') ='"+moduleType+"'";
								if (id != null && id.length() > 0) {
									sql += " and id <> '" + id + "'";
								}
								ResultSet rs = st.executeQuery(sql);
								String flag = "false";
								if (rs.next()) {
									int count = rs.getInt("count");
									if (count > 0) {
										flag = "true";
									}
								}
								result.setRetVal(flag);
							}else {
								result.setRetVal("false");
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
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

	public Result updateNewFlag(String operation, String fileName) {
		final Result rs = new Result();
		final String operationf = operation;
		final String fileNamef = fileName;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String sql = "";
							if (operationf.equals("saveSQLFile")) {
								sql = "update tblReports set newFlag='OLD' where SQLFileName='" + fileNamef + "'";
							} else if (operationf.equals("saveFormatFile")) {
								sql = "update tblReportsDet set newFlag='OLD' where FormatFileName='" + fileNamef + "'";
							}
							st.execute(sql);
						} catch (Exception ex) {
							ex.printStackTrace();
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
	
	
	public Result getReportNameByFile(String operation, String fileName) {
		final Result rs = new Result();
		final String operationf = operation;
		final String fileNamef = fileName;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String sql = "";
							if (operationf.equals("saveSQLFile")) {
								sql = "select zh_CN from  tblReports a join tblLanguage b on a.ReportName=b.id  where SQLFileName='" + fileNamef + "'";
							} else if (operationf.equals("saveFormatFile")) {
								sql = "select b.zh_CN+':样式'+FormatName from  tblReports a join tblLanguage b on a.ReportName=b.id join tblReportsDet c on a.id=c.f_ref and c.languageType='zh_CN' where FormatFileName='" + fileNamef + "'";
							}
							ResultSet rset =st.executeQuery(sql);
							if(rset.next()){
								rs.retVal = rset.getString(1);
							}
						} catch (Exception ex) {
							BaseEnv.log.debug("ReportSetMgt.getReportNameByFile Error:",ex);
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

	public Result getShowSet(final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String sql = "select tableName,fieldName,reportView,billView,popSel,keyword,popupView from tblShowSet where tableName='"+tableName+"'  order by id";
							ResultSet set = st.executeQuery(sql);
							ArrayList list = new ArrayList();
							rs.retVal = list;
							while (set.next()) {
								list.add(new String[] { set.getString(1), set.getString(2), set.getString(3),set.getString(4), set.getString(5), set.getString(6), set.getString(7) });
							}
						} catch (Exception ex) {
							ex.printStackTrace();
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
	
	public Result saveFilterSQL(final String fsql,final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							
							String sql = "update tblreportsdet set filterSQL=? where id=?";
							PreparedStatement pstmt =conn.prepareStatement(sql);
							pstmt.setString(1, fsql);
							pstmt.setString(2, id);
							pstmt.execute();
						} catch (Exception ex) {
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

	public Result updateShowSet(final String tableName,final ArrayList<HashMap> fList) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "delete from tblShowSet where tableName='"+tableName+"'";
							PreparedStatement st = conn.prepareStatement(sql);
							st.executeUpdate();
							sql = "insert into tblShowSet(tableName,fieldName,reportView,billView,popSel,keyword,popupView) values(?,?,?,?,?,?,?)";
							st = conn.prepareStatement(sql);
							for (HashMap map : fList) {	
								String str = (String)map.get("fieldName");
								String fieldName = str.substring(str.lastIndexOf("_") + 1);
								st.setString(1, tableName);
								st.setString(2, fieldName);
								st.setString(3, "1".equals(map.get("reportView"))?"1":"0");
								st.setString(4, "1".equals(map.get("billView"))?"1":"0");
								st.setString(5, "1".equals(map.get("popSel"))?"1":"0");
								st.setString(6, "1".equals(map.get("keyword"))?"1":"0");
								st.setString(7, "1".equals(map.get("popupView"))?"1":"0");
								st.execute();
							}
							sql ="delete from tblColConfig";
							st = conn.prepareStatement(sql);
							st.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
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
		if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//更新内存
			final Result res = new Result();
	        final ArrayList list =new ArrayList();
	        retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                        Connection conn = connection;
	                        //查主表
	                        String querysql = " select tableName,fieldName,reportView,billView,popSel,keyword,popupView from tblShowSet  order by tableName,id";
	                        try {
	                            PreparedStatement cs = conn.prepareStatement(
	                                    querysql);
	                            //  BaseEnv.log.debug(querysql);
	                            ResultSet rset = cs.executeQuery();
	                            while (rset.next()) {                                
	                                list.add(new String[] {rset.getString(1), rset.getString(2), rset.getString(3), rset.getString(4), rset.getString(5), rset.getString(6), rset.getString(7)});
	                            }
	                        } catch (Exception ex) {
	                            res.setRetCode(ErrorCanst.
	                                           DEFAULT_FAILURE);
	                            BaseEnv.log.error(
	                                    "Query data Error InitMenDate.initReportShowSet :" +
	                                    querysql, ex);
	                            return;
	                        }
	                    }
	                });
	                return res.getRetCode();
	            }
	        });
	        res.setRetCode(retCode);
	        if(retCode== ErrorCanst.DEFAULT_SUCCESS){
	        	BaseEnv.reportShowSet = list;
	        	GenJS.clearJS();
	        }
		}
		return rs;
	}

	/**
	 * 设置打印样式人员和部门范围权限
	 * @param operation
	 * @param fileName
	 * @return
	 */
	public Result setFormatScope(final String type, final String userIds, final String keyId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							String sql = "update tblReportsDet set " + type + "='" + userIds + "' where id='" + keyId + "'";
							st.executeUpdate(sql);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	 * 删除打印样式
	 * @param operation
	 * @param fileName
	 * @return
	 */
	public Result deleteFormatStyle(final String keyId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							String sql = "delete from tblReportsDet where id='" + keyId + "'";
							st.executeUpdate(sql);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	 * 跟据样式设计取报表的名字和样式的名字，用于计录日志
	 * @param keyId
	 * @return
	 */
	public Result getFormatById(final String keyId,final String locale) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							String sql = "select "+locale+",a.FormatName from tblReportsDet a join tblReports b on a.f_ref=b.id join tblLanguage c on b.ReportName=c.id where a.id='" + keyId + "'";
							ResultSet rset = st.executeQuery(sql);
							if(rset.next()){
								rs.retVal = new String[]{rset.getString(1),rset.getString(2)};
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	 * 根据样式文件名取报表信息，用于记录日志
	 * @param fileNamef
	 * @param locale
	 * @return
	 */	
	public Result getFormatByIdFile(final String fileNamef,final String locale) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							String sql = "select "+locale+",a.FormatName from tblReportsDet a join tblReports b on a.f_ref=b.id join tblLanguage c on b.ReportName=c.id where a.FormatFileName='" + fileNamef + "'";
							ResultSet rset = st.executeQuery(sql);
							if(rset.next()){
								rs.retVal = new String[]{rset.getString(1),rset.getString(2)};
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
	 * 锁定报表几列
	 * @param operation
	 * @param fileName
	 * @return
	 */
	public Result lockReportColumn(final String number, final String report) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update tblReports set fixNumberCol=" + number + " where ReportNumber=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, report);
							pss.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							BaseEnv.log.error("ReportSetMgt lockReportColumn error", ex);
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
	 * 查询tblReports表数据用以生成脚本
	 * @param id
	 * @param fieldnames
	 * @return
	 */
	public Result queryReportsNewData(final String id, final List fieldnames, final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String sql = "select ";
							for (int i = 0; i < fieldnames.size(); i++) {
								if (i == fieldnames.size() - 1) {
									sql += fieldnames.get(i) + " from " + tableName + " where id='" + id + "'";
								} else {
									sql += fieldnames.get(i) + ",";
								}
							}
							HashMap values = new HashMap();
							ResultSet rss = st.executeQuery(sql);
							while (rss.next()) {

								for (int i = 0; i < fieldnames.size(); i++) {
									Object value = rss.getObject(fieldnames.get(i).toString());
									value = value == null ? "" : value;
									if (fieldnames.get(i).toString().equalsIgnoreCase("newFlag")) {
										values.put(fieldnames.get(i).toString(), "OLD");
									} else {
										values.put(fieldnames.get(i).toString(), value);
									}
								}

							}
							rs.setRetVal(values);
						} catch (Exception ex) {
							ex.printStackTrace();
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
	 * 查询报表名称多语言
	 * @param languageId
	 * @return
	 */
	public Result queryReportNameLanguage(final String languageId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							KRLanguage lan = null;
							Statement st = conn.createStatement();
							String sql = "select * from tblLanguage where id='" + languageId + "'";
							ResultSet rss = st.executeQuery(sql);
							if (rss.next()) {
								lan = new KRLanguage();
								Enumeration en = BaseEnv.localeTable.keys();
								lan.setId(rss.getString("id"));
								while (en.hasMoreElements()) {
									String key = en.nextElement().toString();
									String value = rss.getString(key);
									if (value != null) {
										lan.putLanguage(key, value);
									}
								}
							}
							rs.setRetVal(lan);
						} catch (Exception ex) {
							ex.printStackTrace();
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
	 * 查询tblReportsDet表数据用以生成脚本
	 * @param id
	 * @param fieldnames
	 * @return
	 */
	public Result queryReportsDetNewData(final String id, final List fieldnames, final String tableName, final String fkName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String sql = "select ";
							for (int i = 0; i < fieldnames.size(); i++) {
								if (i == fieldnames.size() - 1) {
									sql += fieldnames.get(i) + " from " + tableName + " where " + fkName + "='" + id + "'";
								} else {
									sql += fieldnames.get(i) + ",";
								}
							}
							List list = new ArrayList();
							ResultSet rss = st.executeQuery(sql);
							while (rss.next()) {
								List<Object> values = new ArrayList<Object>();
								for (int i = 0; i < fieldnames.size(); i++) {
									Object value = rss.getObject(fieldnames.get(i).toString());
									value = value == null ? "" : value;
									values.add(value);
								}
								list.add(values);
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
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

	public Result getNowFormatName(final String reportNumber, final String styleName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select * from tblReportsDet where f_ref = (select id from tblReports where reportNumber=?) and formatName=? ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, reportNumber);
							ps.setString(2, styleName);
							ResultSet rss = ps.executeQuery();
							if (rss.next()) {
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
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
	 * 新增样式
	 * @param reportNumber
	 * @param styleFormatName
	 * @param styleName
	 * @return
	 */
	public Result insertNowFormatName(final String reportNumber, final String styleFormatName, final String styleName, final String languageType) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select id from tblReports where reportNumber=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, reportNumber);
							ResultSet rss = ps.executeQuery();
							String reportId = "";
							if (rss.next()) {
								reportId = rss.getString("id");
								sql = "insert into tblReportsDet(id,f_ref,formatFileName,newFlag,formatName,languageType) values(?,?,?,?,?,?)";
								ps = conn.prepareStatement(sql);
								ps.setString(1, IDGenerater.getId());
								ps.setString(2, reportId);
								ps.setString(3, styleFormatName);
								ps.setString(4, "NEW");
								ps.setString(5, styleName);
								ps.setString(6, languageType);
								ps.executeUpdate();
							}

						} catch (Exception ex) {
							ex.printStackTrace();
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
	 * 删除样式
	 * @param reportNumber
	 * @param styleName
	 * @return
	 */
	public Result deleteFormatName(final String reportNumber, final String styleName, final String language) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "delete from tblReportsDet where f_ref = (select id from tblReports where reportNumber=?) and formatName=? and languageType=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, reportNumber);
							ps.setString(2, styleName);
							ps.setString(3, language);
							int n = ps.executeUpdate();
							if (n > 0) {
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
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
	 * 根据reportNumber查询样式文件名
	 * @param reportNumber
	 * @return
	 */
	public Result getFormatNameByReportNumber(final String reportNumber, final String type) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select top 1 formatFileName from tblReportsDet where f_ref= " + "(select id from tblReports where reportNumber=?) and languageType=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, reportNumber);
							ps.setString(2, type);
							ResultSet rss = ps.executeQuery();
							if (rss.next()) {
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								rs.setRetVal(rss.getString("formatFileName"));
							} else {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
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
	 * 根据reportNumber查询样式文件名
	 * @param reportNumber
	 * @return
	 */
	public Result getFormatNameByBillTable(ArrayList tableNameList, final String locale) {
		String tableNames = "";
		for (int i = 0; i < tableNameList.size(); i++) {
			tableNames += "'" + tableNameList.get(i) + "',";
		}
		final String tableName = tableNames.substring(0, tableNames.length() - 1);
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select top 1 a.BillTable,b.formatFileName from tblReports a,tblReportsDet b where a.BillTable in (" + tableName + ") and a.id=b.f_ref "
									+ " and b.languageType='" + locale + "'";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							HashMap map = new HashMap();
							while (rss.next()) {
								map.put(rss.getString(1), rss.getString(2));
							}
							rs.setRetVal(map);
						} catch (Exception ex) {
							ex.printStackTrace();
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
	 * 查询部门全称
	 * @param reportNumber
	 * @return
	 */
	public Result queryDeptMapName() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select classCode,deptFullName from tblDepartment ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							HashMap<String, String> deptMap = new HashMap<String, String>();
							while (rss.next()) {
								deptMap.put(rss.getString("classCode"), rss.getString("deptFullName"));
							}
							rs.setRetVal(deptMap);
						} catch (Exception ex) {
							ex.printStackTrace();
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

	public Result queryDetTable(final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select reportNumber,zh_CN from tblReports a join tblLanguage b on a.ReportName=b.id " +
									"where reportType != 'BILL' and   billTable=? ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, tableName);
							ResultSet rss = ps.executeQuery();
							ArrayList list = new ArrayList();
							while (rss.next()) {
								list.add(new String[]{rss.getString(1) , rss.getString(2)});
								
							}
							rs.retVal = list;
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
							BaseEnv.log.error("ReportSetMgt.queryDetTable ", ex);
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

}
