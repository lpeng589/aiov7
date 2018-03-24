package com.menyi.aio.web.userFunction;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Alignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ViewContext;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.importData.ExportField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.tab.TabMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;

public class ExportData extends DBManager {

	public static void export(final HttpServletRequest request, ArrayList<ExportField> exportList, HashMap exportValues) {

		List<DBFieldInfoBean> fieldInfos = (List<DBFieldInfoBean>) request.getAttribute("fieldInfos");
		final DBTableInfoBean mainTable = (DBTableInfoBean) request.getAttribute("mainTable");
		String tableName = (String) request.getAttribute("tableName");
		ArrayList scopeRight = (ArrayList) request.getAttribute("scopeRight");

		HashMap values = (HashMap) request.getAttribute("values");
		ArrayList allConfigList = (ArrayList) request.getAttribute("allConfigList");
		ArrayList childTableList = (ArrayList) request.getAttribute("childTableList");
		Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = (Hashtable<String, ArrayList<ColConfigBean>>) request.getAttribute("childTableConfigList");
		HashMap<String, ArrayList<String>> moduleTable = (HashMap<String, ArrayList<String>>) request.getAttribute("moduleTable");
		String designId = (String) request.getAttribute("designId");
		String currNodeId = request.getAttribute("currNodeId") == null ? null : request.getAttribute("currNodeId").toString();
		ArrayList<Object[]> result = (ArrayList) request.getAttribute("result");
		ArrayList<GoodsPropInfoBean> PROP_INFOL = (ArrayList<GoodsPropInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFOL);

		LoginBean loginBean = (LoginBean) request.getSession(true).getAttribute("LoginBean");

		GlobalsTool globals = new GlobalsTool();
		ViewContext vc = new ViewContext() {
			public Object getAttribute(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			public HttpServletRequest getRequest() {
				// TODO Auto-generated method stub
				return request;
			}

			public HttpServletResponse getResponse() {
				// TODO Auto-generated method stub
				return null;
			}

			public ServletContext getServletContext() {
				// TODO Auto-generated method stub
				return request.getSession().getServletContext();
			}

			public Context getVelocityContext() {
				// TODO Auto-generated method stub
				return null;
			}

			public VelocityEngine getVelocityEngine() {
				// TODO Auto-generated method stub
				return null;
			}

		};
		globals.init(vc);

		Object localeObj = request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (localeObj == null) {
			localeObj = request.getSession().getServletContext().getAttribute("DefaultLocale");
		}
		final String locale = localeObj == null ? "" : localeObj.toString();

		ArrayList childExportList = new ArrayList();

		String parentName = "NOTPARENT";
		// 找父类 有classCode字段且有行标识字段，时可以查询父类字段
		boolean hasClassCode = mainTable.getClassFlag() == 1 ? true : false;

		if (hasClassCode) {
			String markerField = null;
			boolean isInputLanguage = false;
			for (DBFieldInfoBean fb : mainTable.getFieldInfos()) {
				if (fb.getFieldSysType() != null && fb.getFieldSysType().equals("RowMarker")) {
					markerField = fb.getFieldName();
					if (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
						isInputLanguage = true;
					}
				}
			}
			parentName = "";
			final String fMarkerField = markerField;
			final boolean fisInputLanguage = isInputLanguage;
			final String fId = (String) values.get("id");
			final Result parers = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							try {
								Statement st = conn.createStatement();

								String sql = "";
								if (fisInputLanguage) {
									sql += "select b." + locale + ",a.classCode from " + mainTable.getTableName() + " a ";
									sql += " left join tblLanguage b on a." + fMarkerField + "=b.id ";
									sql += " where len(isnull(a.classCode,''))>0 and a.classCode in (select substring(classCode,1,len(classCode)-5) from " + mainTable.getTableName() + " where id='"
											+ fId + "') group by a.classCode,b." + locale + " order by a.classCode";
								} else {
									sql += " select a." + fMarkerField + ",a.classCode from " + mainTable.getTableName() + " a ";
									sql += " where len(isnull(a.classCode,''))>0 and  a.classCode in (select substring(classCode,1,len(classCode)-5) from " + mainTable.getTableName() + " where id='"
											+ fId + "') group by a.classCode,a." + fMarkerField + " order by a.classCode";
								}
								ResultSet rs = st.executeQuery(sql);
								if (rs.next()) {
									String[] val = new String[2];
									val[0] = rs.getString(1);
									val[1] = rs.getString(2);
									parers.setRetVal(val);
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								parers.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								parers.setRetVal(null);
								return;
							}
						}
					});
					return parers.getRetCode();
				}
			});
			parers.setRetCode(retCode);

			if (parers.retVal != null) {
				parentName = ((String[]) parers.retVal)[0];
			}
		}

		if (parentName != null && !"NOTPARENT".equals(parentName)) {
			exportList.add(new ExportField("main", "", "parentName",DBFieldInfoBean.INPUT_NORMAL, GlobalsTool.getMessage(request, "importData.parent"), mainTable.getDisplay().get(locale), false));
			exportValues.put("parentName", parentName);
		}
		// fieldInfos 已经经过列配置处理
		for (DBFieldInfoBean row : fieldInfos) {
			if (row.getInputType() != 100 && row.getFieldType() != 3 && row.getFieldType() != 16 && row.getFieldType() != 13 && row.getFieldType() != 14 && !row.getFieldName().equals("id")
					&& !row.getFieldName().equals("f_brother") && !row.getFieldName().equals("createBy") && !row.getFieldName().equals("createTime") && !row.getFieldName().equals("lastUpdateBy")
					&& !row.getFieldName().equals("lastUpdateTime")) {
				// 0,8,7,9,
				if (row.getInputType() == 1) {

					ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale), false);
					exportList.add(ef);
					exportValues.put(ef.getFieldName(),
							globals.getEnumerationItemsDisplay(row.getRefEnumerationName(), values.get(row.getFieldName()) == null ? "" : values.get(row.getFieldName()).toString()));
				} else if (row.getInputType() == 16) {

					ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale), false);
					exportList.add(ef);
					exportValues.put(ef.getFieldName(), values.get(row.getFieldName()));
				} else if (row.getInputType() == 10) {
					List enumList = globals.getEnumerationItems(row.getRefEnumerationName());
					for (KeyPair erow : (ArrayList<KeyPair>) enumList) {
						if (erow.getValue().equals(values.get(row.getFieldName()))) {
							ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale),
									row.getFieldType() == DBFieldInfoBean.FIELD_INT || row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
							exportList.add(ef);
							exportValues.put(ef.getFieldName(), erow.getName());
							break;
						}
					}
				} else if (row.getInputType() == 4) {
					String lstr = values.get(row.getFieldName()) == null ? "" : values.get(row.getFieldName()).toString();
					if (lstr != null && lstr.length() > 0) {
						HashMap lm = (HashMap) values.get("LANGUAGEQUERY");
						lstr = ((KRLanguage) lm.get(lstr)).get(locale);
					}
					ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale), row.getFieldType() == DBFieldInfoBean.FIELD_INT
							|| row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
					exportList.add(ef);
					exportValues.put(ef.getFieldName(), lstr);
				} else if (row.getInputType() == 5) {
					String vs = "";
					List enumList = globals.getEnumerationItems(row.getRefEnumerationName());
					for (KeyPair erow : (ArrayList<KeyPair>) enumList) {
						for (Object fieldValue : globals.strSplit((String) values.get(row.getFieldName()), ",")) {
							if (erow.getValue().equals(fieldValue)) {
								vs = vs + erow.getName() + ",";
							}
						}
					}
					if (vs.length() > 0) {
						vs = vs.substring(0, vs.length() - 1);
					}
					ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale), row.getFieldType() == DBFieldInfoBean.FIELD_INT
							|| row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
					exportList.add(ef);
					exportValues.put(ef.getFieldName(), vs);
				} else if (row.getInputType() == 2) {
					if (row.getSelectBean() != null && row.getSelectBean().getRelationKey() != null && row.getSelectBean().getRelationKey().hidden) {

					} else if ("GoodsField".equals(row.getFieldSysType()) && globals.getPropBean(row.getFieldName()) != null && globals.getPropBean(row.getFieldName()).getIsSequence() == 1) {

						ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale),
								row.getFieldType() == DBFieldInfoBean.FIELD_INT || row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
						exportList.add(ef);
						exportValues.put(ef.getFieldName(), globals.getSeqDis(values.get(row.getFieldName())));
					} else {
						if (row.getFieldType() == 1) {
							ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale),
									row.getFieldType() == DBFieldInfoBean.FIELD_INT || row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
							exportList.add(ef);
							exportValues.put(ef.getFieldName(),
									globals.formatNumber(values.get(row.getFieldName()), false, false, globals.getSysIntswitch(), mainTable.getTableName(), row.getFieldName(), true));
						} else {
							ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale),
									row.getFieldType() == DBFieldInfoBean.FIELD_INT || row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
							exportList.add(ef);
							exportValues.put(ef.getFieldName(), values.get(row.getFieldName()));
						}
					}

					if (!("GoodsField".equals(row.getFieldSysType()) && globals.getPropBean(row.getFieldName()) != null && globals.getPropBean(row.getFieldName()).getIsSequence() == 1)) {
						for (PopField srow : (ArrayList<PopField>) row.getSelectBean().getViewFields()) {
							String colName = srow.getAsName();
							String tableField;
							if (srow.display != null && srow.display.length() > 0) {
								if (srow.display.indexOf("@TABLENAME") == 0) {
									int index = srow.display.indexOf(".") + 1;
									tableField = tableName + "." + srow.display.substring(index);
								} else {
									tableField = srow.display;
								}
							} else {
								tableField = "";
							}

							String dis;
							if (srow.display != null && srow.display.length() > 0 && srow.display.indexOf(".") == -1) {
								dis = srow.display;
							} else {
								dis = globals.getFieldDisplay(tableName, row.getSelectBean().getName(), tableField);
								if (dis == null || dis.length() == 0) {
									dis = globals.getFieldDisplay(srow.fieldName);
								}
							}
							if (allConfigList != null && allConfigList.size() > 0) {
								if (globals.colIsExistConfigList(mainTable.getTableName(), colName, "bill")) {
									if (srow.asName != null && globals.getFieldBean(srow.asName) != null && globals.getFieldBean(srow.asName).getFieldType() == 1) {
										ExportField ef = new ExportField("main", "", tableField + colName,
												srow.asName != null && globals.getFieldBean(srow.asName) != null?globals.getFieldBean(srow.asName).getInputType():DBFieldInfoBean.INPUT_NORMAL, 
														dis, mainTable.getDisplay().get(locale), true);
										exportList.add(ef);
										exportValues.put(ef.getFieldName(),
												globals.newFormatNumber(values.get(srow.asName), false, false, globals.getSysIntswitch(), mainTable.getTableName(), row.getFieldName(), true));
									} else {
										ExportField ef = new ExportField("main", "", tableField + colName,
												srow.asName != null && globals.getFieldBean(srow.asName) != null?globals.getFieldBean(srow.asName).getInputType():DBFieldInfoBean.INPUT_NORMAL, 
														 dis, mainTable.getDisplay().get(locale),
														 globals.getFieldBean(srow.asName) != null && globals.getFieldBean(srow.asName).getFieldType() == DBFieldInfoBean.FIELD_INT
														|| globals.getFieldBean(srow.asName).getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
										exportList.add(ef);
										exportValues.put(ef.getFieldName(), values.get(srow.getAsName()));
									}
									tableField = "";
								}
							} else {
								if (!srow.getHiddenInput().equals("true")) {
									if (srow.asName != null && globals.getFieldBean(srow.asName) != null && globals.getFieldBean(srow.asName).getFieldType() == 1) {
										ExportField ef = new ExportField("main", "", tableField + colName,
												srow.asName != null && globals.getFieldBean(srow.asName) != null?globals.getFieldBean(srow.asName).getInputType():DBFieldInfoBean.INPUT_NORMAL, 
														 dis, mainTable.getDisplay().get(locale), true);
										exportList.add(ef);
										exportValues.put(ef.getFieldName(),
												globals.newFormatNumber(values.get(srow.asName), false, false, globals.getSysIntswitch(), mainTable.getTableName(), row.getFieldName(), true));
									} else {
										ExportField ef = new ExportField(
												"main",
												"",
												tableField + colName,
												srow.asName != null && globals.getFieldBean(srow.asName) != null?globals.getFieldBean(srow.asName).getInputType():DBFieldInfoBean.INPUT_NORMAL, 
														
												dis,
												mainTable.getDisplay().get(locale),
												globals.getFieldBean(srow.asName) != null
														&& (globals.getFieldBean(srow.asName).getFieldType() == DBFieldInfoBean.FIELD_INT || 
														globals.getFieldBean(srow.asName).getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) ? true : false);
										exportList.add(ef);
										exportValues.put(ef.getFieldName(), values.get(srow.getAsName()));
									}

									tableField = "";
								}
							}

						}
					}
				} else if (row.getInputType() == 0 || row.getInputType() == 8 || row.getInputType() == 7 || row.getInputType() == 9) {
					// 0,8,7,9,
					if (row.getFieldType() == 1) {
						ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale), true);
						exportList.add(ef);
						exportValues.put(ef.getFieldName(),
								globals.formatNumber(values.get(row.getFieldName()), false, false, globals.getSysIntswitch(), mainTable.getTableName(), row.getFieldName(), true));
					} else {
						ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale),
								row.getFieldType() == DBFieldInfoBean.FIELD_INT || row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
						exportList.add(ef);
						exportValues.put(ef.getFieldName(), values.get(row.getFieldName()));
					}
				}
			}
		}

		for (DBFieldInfoBean row : fieldInfos) {
			if (globals.getScopeRight(mainTable.getTableName(), row.getFieldName(), scopeRight)) {
				// 文本
				if (row.getInputType() != 100 && row.getInputType() != 3 && row.getInputType() != 6 && row.getFieldType() == 3 && row.getWidth() == -1) {
					ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale), row.getFieldType() == DBFieldInfoBean.FIELD_INT
							|| row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
					exportList.add(ef);
					exportValues.put(ef.getFieldName(), values.get(row.getFieldName()));
				}
				// html
				if (row.getInputType() != 100 && row.getInputType() != 3 && row.getInputType() != 6 && row.getFieldType() == 16 && row.getWidth() == -1) {
					ExportField ef = new ExportField("main", "", row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), mainTable.getDisplay().get(locale), row.getFieldType() == DBFieldInfoBean.FIELD_INT
							|| row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
					exportList.add(ef);
					exportValues.put(ef.getFieldName(), values.get(row.getFieldName()));
				}
			}
		}

		// -------------------------------处理从表数据-------------------------
		HashMap childFieldMap = (HashMap) request.getAttribute("childFieldMap");
		for (DBTableInfoBean rowlist : (List<DBTableInfoBean>) childTableList) {

			if (globals.getSysSetting(rowlist.getTableSysType()).equals("true")) {
				String childTableDisplay = rowlist.getDisplay().get(locale);// 明细表的中文名
				// 当有列配置，或无列配置时
				for (DBFieldInfoBean row : (ArrayList<DBFieldInfoBean>) childFieldMap.get(rowlist.getTableName())) {
					if (row.getInputType() != 100 && row.getFieldType() != 16 && row.getFieldType() != 13 && row.getFieldType() != 14 && !row.getFieldName().equals("id")
							&& !row.getFieldName().equals("f_brother") && !row.getFieldName().equals("createBy") && !row.getFieldName().equals("createTime")
							&& !row.getFieldName().equals("lastUpdateBy") && !row.getFieldName().equals("lastUpdateTime")) {
						// 0,8,7,9,
						if (row.getInputType() == 2 ) {
							if (row.getSelectBean() != null && row.getSelectBean().getRelationKey() != null && row.getSelectBean().getRelationKey().hidden) {

							} else if ("GoodsField".equals(row.getFieldSysType()) && globals.getPropBean(row.getFieldName()) != null && globals.getPropBean(row.getFieldName()).getIsSequence() == 1) {
								ExportField ef = new ExportField("child", rowlist.getTableName(), row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), childTableDisplay,
										(row.getFieldType() == DBFieldInfoBean.FIELD_INT) || (row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) ? true : false);
								exportList.add(ef);
							} else {
								ExportField ef = new ExportField("child", rowlist.getTableName(), row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), childTableDisplay,
										(row.getFieldType() == DBFieldInfoBean.FIELD_INT) || (row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) ? true : false);
								exportList.add(ef);
							}

							if (row.getSelectBean() != null && !("GoodsField".equals(row.getFieldSysType()) && globals.getPropBean(row.getFieldName()) != null && globals.getPropBean(row.getFieldName()).getIsSequence() == 1)) {
								for (PopField srow : (ArrayList<PopField>) row.getSelectBean().getViewFields()) {
									String colName = srow.getAsName();
									

									String dis;
									if (srow.display != null && srow.display.length() > 0 && srow.display.indexOf(".") == -1) {
										dis = srow.display;
									} else {
										dis = globals.getFieldDisplay(rowlist.getTableName(), row.getSelectBean().getName(), srow.getAsName());
										if (dis == null || dis.length() == 0) {
											dis = globals.getFieldDisplay(srow.fieldName);
										}
									}
									if (allConfigList != null && allConfigList.size() > 0) {
										if (globals.colIsExistConfigList(rowlist.getTableName(), colName, "bill")) {
											ExportField ef = new ExportField(
													"child",
													rowlist.getTableName(),
													 colName,
													srow.asName != null && globals.getFieldBean(srow.asName) != null?globals.getFieldBean(srow.asName).getInputTypeOld():DBFieldInfoBean.INPUT_NORMAL, 
															
													dis,
													childTableDisplay,
													(srow.asName != null && globals.getFieldBean(srow.asName) != null && globals.getFieldBean(srow.asName).getFieldType() == DBFieldInfoBean.FIELD_INT)
															|| (srow.asName != null && globals.getFieldBean(srow.asName) != null && globals.getFieldBean(srow.asName).getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) ? true
															: false);
											exportList.add(ef);
										}
									} else {
										if (!srow.getHiddenInput().equals("true")) {
											ExportField ef = new ExportField(
													"child",
													rowlist.getTableName(),
													colName,
													srow.asName != null && globals.getFieldBean(srow.asName) != null?globals.getFieldBean(srow.asName).getInputTypeOld():DBFieldInfoBean.INPUT_NORMAL, 
															
													dis,
													childTableDisplay,
													(srow.asName != null && globals.getFieldBean(srow.asName) != null && globals.getFieldBean(srow.asName).getFieldType() == DBFieldInfoBean.FIELD_INT)
															|| (srow.asName != null && globals.getFieldBean(srow.asName) != null && globals.getFieldBean(srow.asName).getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) ? true
															: false);
											exportList.add(ef);
										}
									}

								}
							}
						} else if (row.getInputType() == 0 || row.getInputType() == 8 || row.getInputType() == 7 || 
								row.getInputType() == 9|| row.getInputType() == 5|| row.getInputType() == 1|| row.getInputType() == 16
								|| row.getInputType() == 10|| row.getInputType() == 4) {
							// 0,8,7,9,
							ExportField ef = new ExportField("child", rowlist.getTableName(), row.getFieldName(),row.getInputType(), row.getDisplay().get(locale), childTableDisplay,
									row.getFieldType() == DBFieldInfoBean.FIELD_INT || row.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE ? true : false);
							exportList.add(ef);
						}
					}
				}

				// --------------------------明细值开始
				ArrayList childValues = new ArrayList();
				exportValues.put("TABLENAME_" + rowlist.getTableName(), childValues);

				ArrayList<HashMap> cValues = (ArrayList<HashMap>) values.get("TABLENAME_" + rowlist.getTableName());
				HashMap lm = (HashMap) values.get("LANGUAGEQUERY");
				String tableNamec = rowlist.getTableName();
				for (HashMap rowlistResult : cValues) {
					childValues.add(setExportValue(globals,rowlist.getTableName(),exportList,rowlistResult,lm,locale));
				}
				// -----------明细值
			}
		}

		// billExport(null,null);

	}
	
	private static HashMap setExportValue(GlobalsTool globals,String tableName ,ArrayList<ExportField> exportList,HashMap valueMap,
			HashMap language,String locale){
		HashMap childValuesMap = new HashMap();
		for (ExportField ef : exportList) {
			if (ef.getTableName().equals(tableName)) {
				String fieldName = ef.getFieldName();
				Object o = valueMap.get(fieldName);
				String value = o==null?"": o.toString();
				if (ef.getInputType() == 1) {
					DBFieldInfoBean row = globals.getFieldBean(ef.getTableName(), ef.getFieldName());
					if(row!=null){
						value=globals.getEnumerationItemsDisplay(row.getRefEnumerationName(), value == null ? "" : value);
					}
				} else if (ef.getInputType() == 10) {
					DBFieldInfoBean row = globals.getFieldBean(ef.getTableName(), ef.getFieldName());
					if(row!=null){
						List enumList = globals.getEnumerationItems(row.getRefEnumerationName());
						for (KeyPair erow : (ArrayList<KeyPair>) enumList) {
							if (erow.getValue().equals(value)) {
								value =erow.getName();
								break;
							}
						}
					}
				} else if (ef.getInputType() == 4) {
					String lstr = value;
					if (lstr != null && lstr.length() > 0) {
						lstr = ((KRLanguage) language.get(lstr)).get(locale);
					}
					value= lstr;
				} else if (ef.getInputType() == 5) {
					String vs = "";
					DBFieldInfoBean row = globals.getFieldBean(ef.getTableName(), ef.getFieldName());
					if(row!=null){
						List enumList = globals.getEnumerationItems(row.getRefEnumerationName());
						for (KeyPair erow : (ArrayList<KeyPair>) enumList) {
							for (Object fieldValue : globals.strSplit(value, ",")) {
								if (erow.getValue().equals(fieldValue)) {
									vs = vs + erow.getName() + ",";
								}
							}
						}
						if (vs.length() > 0) {
							vs = vs.substring(0, vs.length() - 1);
						}
						value =vs;
					}
				}
				if(ef.isNumber()){
					if(value==null || value.equals("")) value="0";
					value = globals.formatNumber(value, false, false, globals.getSysIntswitch(), tableName, ef.getFieldName(),true);
				}
				childValuesMap.put(ef.getFieldName(), value);
			}
		}
		return childValuesMap;
	}

	/**
	 * 单据导出
	 * 
	 * @param tableName
	 * @param importDataBean
	 * @param allTables
	 * @param list
	 * @param locale
	 * @param fileName
	 * @return
	 */
	public static Result billExport(OutputStream outStream, String tableDisplay, ArrayList<ExportField> exportList, ArrayList<HashMap> exportValueList, String sheetName) {
		Result result = new Result();
		try {

			WritableCellFormat wf = new WritableCellFormat();
			wf.setBorder(Border.ALL, BorderLineStyle.THIN);
			wf.setAlignment(Alignment.LEFT);
			WritableWorkbook wbook = Workbook.createWorkbook(outStream);
			WritableSheet ws = null;
			if (sheetName != null && !"".equals(sheetName)) {
				ws = wbook.createSheet(sheetName, 0);
			} else {
				ws = wbook.createSheet(tableDisplay, 0);

			}

			WritableCellFormat wfnum = new WritableCellFormat();
			wfnum.setBorder(Border.ALL, BorderLineStyle.THIN);
			wfnum.setAlignment(Alignment.RIGHT);

			// 计算共多少张表
			boolean classFlags = false; // 是否支持分类

			TabMgt tabmgt = new TabMgt();

			ArrayList<String[]> tableList = new ArrayList<String[]>();
			// 根据主表和从表的方式，决定排列顺序
			// 写表头

			int curNum = 0;
			String tn = tableDisplay;
			for (ExportField ef : exportList) {
				if (ef.getTableDisplay().equals(tn)) {
					curNum++;
				} else {
					tableList.add(new String[] { tn, (curNum) + "" });
					tn = ef.getTableDisplay();
					curNum = 1;
				}
			}
			tableList.add(new String[] { tn, (curNum) + "" });

			int c = 0;
			for (String[] ss : tableList) {
				Label cell = new Label(c, 0, ss[0], wf);
				int len = Integer.parseInt(ss[1]) - 1;
				ws.addCell(cell);
				ws.mergeCells(c, 0, c + len, 0);
				c += len + 1;
			}

			for (int i = 0; i < exportList.size(); i++) {
				ExportField efield = exportList.get(i);
				Label cell = new Label(i, 1, efield.getDisplay(), wf);
				ws.addCell(cell);
			}

			int rowCount = 2;
			for (HashMap obj : exportValueList) {
				HashMap valuesMap = (HashMap) obj;
				int maxChildRow = 1;

				for (int i = 0; i < exportList.size(); i++) {
					ExportField efield = exportList.get(i);
					if (efield.getType().equals("main")) {

						String fieldName = efield.getFieldName();
						HashMap values = valuesMap;
						if (efield.isNumber()) {
							double strLabel = values.get(fieldName) == null || values.get(fieldName).equals("") ? 0 : Double.parseDouble(values.get(fieldName).toString());
							jxl.write.Number cell = new jxl.write.Number(i, rowCount, strLabel, wfnum);
							ws.addCell(cell);
						} else {
							String strLabel = values.get(fieldName) == null ? "" : values.get(fieldName).toString();
							strLabel = GlobalsTool.rereplaceSpecLitter(strLabel);
							Label cell = new Label(i, rowCount, strLabel, wf);
							ws.addCell(cell);
						}
					} else if (efield.getType().equals("child")) {
						ArrayList childList = (ArrayList) valuesMap.get("TABLENAME_" + efield.getTableName());
						if (childList.size() > maxChildRow) {
							maxChildRow = childList.size();
						}
						for (int j = 0; j < childList.size(); j++) {
							HashMap values = (HashMap) childList.get(j);

							String fieldName = efield.getFieldName();
							if (efield.isNumber() && values.get(fieldName) != null && !values.get(fieldName).equals("")) {
								double strLabel = values.get(fieldName) == null ? 0 : Double.parseDouble(values.get(fieldName).toString());
								jxl.write.Number cell = new jxl.write.Number(i, rowCount + j, strLabel, wfnum);
								ws.addCell(cell);
							} else {
								String strLabel = values.get(fieldName) == null ? "" : values.get(fieldName).toString();
								strLabel = GlobalsTool.rereplaceSpecLitter(strLabel);
								Label cell = new Label(i, rowCount + j, strLabel, wf);
								ws.addCell(cell);
							}
						}

					}
				}
				rowCount += maxChildRow;
			}

			wbook.write();
			wbook.close();

		} catch (Exception ex) {
			result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("UserFunctionAction billExport :", ex);
		}
		return result;
	}

	private static void recExport(String parentId, String parentName, HashMap valuesMap, ArrayList<ExportField> exportList, WritableCellFormat wf, WritableSheet ws, String locale, IntObj intObj,
			Hashtable map, LoginBean lg, String sunClassCode, Hashtable props, boolean isLastSunCompany, MOperation mop, int grade) throws Exception {
		ArrayList<HashMap> mlist = (ArrayList<HashMap>) valuesMap.get("TABLENAME_" + parentId + "tblBOMDet");

		for (HashMap mvalues : mlist) {
			// 这里先查半成品数据，为了，能取到半成品的版本号
			HashMap mvaluesMap = null;
			if (mvalues.get("BomId") != null && !mvalues.get("BomId").equals("")) {
				Result rs = new DynDBManager().detail("tblBOM", map, mvalues.get("BomId").toString(), sunClassCode, props, lg.getId(), isLastSunCompany,  "");
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null) {
					mvaluesMap = (HashMap) rs.retVal;
				}
			}
			for (int i = 0; i < exportList.size(); i++) {
				ExportField efield = exportList.get(i);
				if (efield.getType().equals("child")) {
					String fieldName = efield.getFieldName();
					String strLabel = mvalues.get(fieldName) == null ? "" : mvalues.get(fieldName).toString();
					if (fieldName.equals("BOMTYPE")) {
						strLabel = "物料";
					} else if (fieldName.equals("PARENTNAME")) {
						strLabel = parentName;
					} else if (fieldName.equals("VersionNO")) {
						if (mvalues.get("BomId") != null && !mvalues.get("BomId").equals("")) {
							strLabel = mvaluesMap.get("VersionNO").toString();
						} else {
							strLabel = "";
						}
					}
					DBFieldInfoBean fb = GlobalsTool.getFieldBean("tblBOMDet", fieldName);
					if (fb != null) {
						if (fb.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
							strLabel = GlobalsTool.getEnumerationItemsDisplay(fb.getRefEnumerationName(), strLabel, locale);
						} else if (fb.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
							if (strLabel != null && strLabel.length() > 0) {
								strLabel = GlobalsTool.formatNumber(Double.parseDouble(strLabel), false, false, GlobalsTool.getSysIntswitch(), "tblBOMDet", fb.getFieldName(), true);
							}
						}
					}

					Label cell = new Label(i, intObj.i, strLabel, wf);

					ws.addCell(cell);
				}
			}
			if (mvalues.get("BomId") != null && !mvalues.get("BomId").equals("")) {
				intObj.i = intObj.i + 1;
				if (mvaluesMap != null) {
					recExport("", getTite(mvalues, grade + 1), mvaluesMap, exportList, wf, ws, locale, intObj, map, lg, sunClassCode, props, isLastSunCompany, mop, grade + 1);
				}
			} else {
				intObj.i = intObj.i + 1;
			}

			// 半成品---------------------------------------------------
			ArrayList<HashMap> clist = (ArrayList<HashMap>) valuesMap.get("TABLENAME_" + mvalues.get("id") + "_tblBOMDetail");
			if (clist == null) {
				continue;
			}
			for (HashMap cvalues : clist) {
				// 这里先查半成品数据，为了，能取到半成品的版本号
				HashMap cvaluesMap = null;
				if (cvalues.get("BomId") != null && !cvalues.get("BomId").equals("")) {
					// Result rs = new DynDBManager().detail("tblBOM", map,
					// values.get("BomId").toString(),
					// sunClassCode,props,lg.getId(),isLastSunCompany,mop.getScope(MOperation.M_UPDATE),"");
					// if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS &&
					// rs.retVal!=null){
					// cvaluesMap= (HashMap) rs.retVal;
					// }
				}
				for (int i = 0; i < exportList.size(); i++) {
					ExportField efield = exportList.get(i);
					if (efield.getType().equals("child")) {

						String fieldName = efield.getFieldName();
						String strLabel = cvalues.get(fieldName) == null ? "" : cvalues.get(fieldName).toString();
						if (fieldName.equals("BOMTYPE")) {
							strLabel = "替换料";
						} else if (fieldName.equals("PARENTNAME")) {
							strLabel = getTite(mvalues, grade + 1);
						} else if (fieldName.equals("VersionNO")) {
							if (cvalues.get("BomId") != null && !cvalues.get("BomId").equals("")) {
								strLabel = cvaluesMap.get("VersionNO").toString();
							} else {
								strLabel = "";
							}
						}
						DBFieldInfoBean fb = GlobalsTool.getFieldBean("tblBOMDet", fieldName);
						if (fb != null) {
							if (fb.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
								strLabel = GlobalsTool.getEnumerationItemsDisplay(fb.getRefEnumerationName(), strLabel, locale);
							} else if (fb.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
								if (strLabel != null && strLabel.length() > 0) {
									strLabel = GlobalsTool.formatNumber(Double.parseDouble(strLabel), false, false, GlobalsTool.getSysIntswitch(), "tblBOMDet", fb.getFieldName(), true);
								}
							}
						}
						Label cell = new Label(i, intObj.i, strLabel, wf);
						ws.addCell(cell);
					}
				}
				if (cvalues.get("BomId") != null && !cvalues.get("BomId").equals("")) {
					intObj.i = intObj.i + 1;
					// if(cvaluesMap !=null){
					// recExport("",getTite(cvalues,grade+1),cvaluesMap,exportList,wf,ws,locale,intObj,map,lg,sunClassCode,props,isLastSunCompany,mop,grade+1);
					// }
				} else {
					intObj.i = intObj.i + 1;
				}
			}
		}
	}

	private static String getTite(HashMap value, int grade) {
		SystemSettingBean bean = BaseEnv.systemSet.get("bomset");
		if (grade == 0) {
			if ("GoodsNumber".equals(bean.getSetting())) {
				return (String) value.get("tblGoods.GoodsNumber");
			} else if ("GoodsFullName".equals(bean.getSetting())) {
				return (String) value.get("tblGoods.GoodsFullName");
			} else {
				return (String) value.get("tblGoods.GoodsSpec");
			}
		} else {
			if ("GoodsNumber".equals(bean.getSetting())) {
				return getBlank(grade) + (String) value.get("ViewBomGoods.GoodsNumber");
			} else if ("GoodsFullName".equals(bean.getSetting())) {
				return getBlank(grade) + (String) value.get("ViewBomGoods.GoodsFullName");
			} else {
				return getBlank(grade) + (String) value.get("ViewBomGoods.GoodsSpec");
			}
		}
	}

	private static String getBlank(int grade) {
		String str = "";
		for (int i = 0; i < grade * 5; i++) {
			str += " ";
		}
		return str;
	}

	private static boolean isNull(String str) {
		return str == null || str.length() == 0;
	}

	public class IntObj {
		public int i;
	}
}
