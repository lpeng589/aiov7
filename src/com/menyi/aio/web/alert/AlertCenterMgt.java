package com.menyi.aio.web.alert;

import java.util.*;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.web.util.*;
import com.dbfactory.hibernate.DBUtil;
import com.menyi.web.util.BaseEnv;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

import org.apache.bsf.BSFManager;
import org.apache.struts.chain.contexts.ServletActionContext;
import org.apache.tools.zip.ZipOutputStream;
import org.hibernate.Session;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import com.menyi.web.util.*;

import java.sql.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.*;
import com.menyi.aio.web.login.*;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.report.TableListResult;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.aio.web.userFunction.ReportData.CompressFilter;
import com.menyi.aio.bean.*;
import com.menyi.web.util.*;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 往来期初的接口类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: 张雪
 * </p>
 * 
 * @author 张雪
 * @version 1.0
 */
public class AlertCenterMgt extends AIODBManager {


	/**
	 * 查询当前预警设置的预警总数量进行提醒
	 * @servletContext  
	 * @return
	 */
	public Result queryAlertTotals(ServletContext servletContext) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							ResultSet rset = null;
							Statement st = null;

							/* 查询启用的预警设置 */
							StringBuffer sql = new StringBuffer("SELECT tblSysAlert.id,isnull(tblSysAlert.SqlDefineName,'') as SqlDefineName,l.zh_cn as AlertName,");
							sql.append("isnull(tblSysAlert.condition,'') AS condition,tblSysAlert.ActionTime,tblReports.sqlFileName,tblSysAlert.modelId,");
							sql.append(" isnull(tblSysAlert.alertType,'') as alertType,isnull(tblSysAlert.ActionFrequency,0) as ActionFrequency,tblLanguage.zh_cn as modelName,");
							sql.append(" tblSysAlert.nextAlertTime,isnull(tblReports.BillTable,'') as BillTable, ");
							sql.append(" isnull((select top 1 ReportNumber from tblReports as report where report.endClassNumber=tblReports.reportNumber),'') as mainNumber ");
							sql.append(" FROM tblSysAlert LEFT JOIN tblReports ON tblReports.reportNumber=tblSysAlert.modelId ");
							sql.append(" LEFT JOIN tblLanguage on tblLanguage.id=tblReports.reportName");
							sql.append(" LEFT JOIN tblLanguage l on l.id=tblSysAlert.AlertName");
							sql.append(" WHERE tblSysAlert.Status=0 and tblSysAlert.isHidden=0 and tblSysAlert.nextAlertTime<GetDate()");

							/* 查询授权的用户SQL */
							String sqlDet = "SELECT id,f_ref,AlertUser FROM tblSysAlertDet WHERE f_ref=?";
							PreparedStatement ps = conn.prepareStatement(sqlDet);

							//查询主表信息
							st = conn.createStatement();
							rset = st.executeQuery(sql.toString());
							List list = new ArrayList();
							while (rset.next()) {
								//查询的数据保存到HashMap中
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("id", rset.getString("id"));
								map.put("sqlDefineName", rset.getString("SqlDefineName"));
								map.put("sqlFileName", rset.getString("sqlFileName"));
								map.put("AlertName", rset.getString("AlertName"));
								map.put("condition", rset.getString("condition"));
								map.put("reportNumber", rset.getString("modelId"));
								map.put("ActionTime", rset.getString("ActionTime"));
								map.put("alertType", rset.getString("alertType"));
								map.put("ActionFrequency", String.valueOf(rset.getInt("ActionFrequency")));
								map.put("modelName", rset.getString("modelName"));
								map.put("nextAlertTime", rset.getString("nextAlertTime"));
								map.put("BillTable", rset.getString("BillTable"));
								map.put("mainNumber", rset.getString("mainNumber"));

								/**
								 * 根据预警设置的id查询用户设置的提示用户
								 */
								ps.setString(1, map.get("id"));
								ResultSet rs = ps.executeQuery();
								String users = "";
								while (rs.next()) {
									String alertUser = rs.getString("AlertUser");
									users = users + alertUser + ",";
								}
								if (users.length() > 0) {
									users = users.substring(0, users.length() - 1);
								}
								map.put("users", users);
								list.add(map);
							}
							result.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("AlertCenterMgt queryAlertTotals:", ex);
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);

		Result rs = new Result();
		// 获取启用的预警设置

		List list = (ArrayList) result.retVal;
		/**
		 * 循环预警设置进行处理
		 */
		for (int y = 0; y < list.size(); y++) {
			HashMap<String, String> map = (HashMap) list.get(y);

			ServletContext sContext = servletContext;
			//清空servletContext的参数数据
			sContext.removeAttribute("parameterMap");

			/* 根据预警设置查询满足条件的预警总条数 */
			sContext.setAttribute("sysAlert", "true");

			/* 查询数据 */
			try {
				
				/**
				 * 获取解析后的sql语句和处理后的参数等一些数据
				 */
				String condition=map.get("condition");
				ReportSetMgt setMgt = new ReportSetMgt();
				ReportsBean reportSetBean = (ReportsBean) setMgt.getReportSetInfo(map.get("reportNumber").toString(), "zh_CN").getRetVal();
				Result rsData = this.showData(map.get("reportNumber").toString(),condition);
				if (rsData.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					int count=((ArrayList)rsData.retVal).size();
					count=count>0&&reportSetBean.getReportType().equals("PROCLIST")?count-1:count;
					map.put("count",String.valueOf(count));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			/**
			 * 发送邮件,短信,通知消息
			 */
			String alertName = map.get("AlertName");
			String[] arrayAlert = map.get("alertType").split(",");
			int count =0;
			if(map.get("count") !=null && !map.get("count").equals("null")){
				count = Integer.parseInt(map.get("count"));
			}
			
			String conditions = map.get("condition");
			String detTable = "";
			if(map.get("BillTable") != null && !"".equals(map.get("BillTable"))){
				detTable = "&detTable_list="+map.get("BillTable");
			}
			if(map.get("mainNumber") != null && !"".equals(map.get("mainNumber"))){
				detTable += "&mainNumber="+map.get("mainNumber");
			}
			
			if (count > 0) {
				for (String str : arrayAlert) {
					if (str != null && !"".equals(str)) {
						NotifyFashion notify = new NotifyFashion("1", alertName + count + "条", "<a href=\"javascript:mdiwin('/ReportDataAction.do?reportNumber=" + map.get("reportNumber") + "&"
								+ conditions + "&" + detTable +"&LinkType=@URL:','" + map.get("modelName") + "')\">" + alertName + count + "条</a>", map.get("users"), Integer
								.parseInt(str), "yes", map.get("id"));
						notify.start();
					}
				}
			}
			final HashMap oldmap = map;

			/**
			 *  修改下次提醒时间
			 */
			retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {
								PreparedStatement ps = null;
								
								String nextTime = String.valueOf(oldmap.get("nextAlertTime"));
								Date date1 = BaseDateFormat.parse(nextTime, BaseDateFormat.yyyyMMddHHmmss);
								Date date2 = new Date();
								int frequency = Integer.parseInt(oldmap.get("ActionFrequency").toString());
								long l = (date2.getTime()-date1.getTime())/(24*60*60*1000);
								if(l>1){
									//是大于1天的
									String sql = "update tblSysAlert set nextAlertTime = CONVERT(varchar, DATEADD(hour,-(24 / "+frequency+"),replace(nextAlertTime,CONVERT(varchar(10), nextAlertTime),CONVERT(varchar(10), getdate(), 120))), 120) where id='" + oldmap.get("id") + "'";
									if(date1.getHours()<date2.getHours()){
										sql = "update tblSysAlert set nextAlertTime = replace(nextAlertTime,CONVERT(varchar(10), nextAlertTime),CONVERT(varchar(10), getdate(), 120)) where id='" + oldmap.get("id") + "'";
									}
									ps = conn.prepareStatement(sql);
									ps.executeUpdate();
								}
								
								//执行sql修改相对应的数据
								String sql = "update tblSysAlert set nextAlertTime=substring(CONVERT(varchar, dateadd(hour," + (24 / frequency)
										+ ",nextAlertTime), 120),0,14)+':00:00' where id='" + oldmap.get("id") + "'";
								ps = conn.prepareStatement(sql);
								ps.executeUpdate();
							} catch (Exception ex) {
								ex.printStackTrace();
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								return;
							}
						}
					});
					return result.getRetCode();
				}
			});
		}
		return result;
	}
	/**
	 * 显示报表
	 * 
	 * @param request
	 * @param scopeRight
	 * @param reportNumber
	 *            　报表编号
	 * @param reportType
	 *            报表类型
	 * @param pageNo
	 *            　第几页
	 * @param pageSize
	 *            　每页记录数
	 * @param scopeRightUpdate
	 * @param scopeRightDel
	 * @param loginBean
	 *            　登录信息
	 * @return
	 * @throws Exception
	 */
	public Result showData(String reportNumber,String conditionStr) throws Exception {
		System.out.println("xxxxxxxxx "+conditionStr);
		Result rs = new Result();
		ReportDataMgt mgt = new ReportDataMgt();
		String locale ="zh_CN";
		String parentCode = "";
		ReportSetMgt setMgt = new ReportSetMgt();
		ReportsBean reportSetBean = (ReportsBean) setMgt.getReportSetInfo(reportNumber, locale).getRetVal();
		
		
		DefineReportBean defBean = DefineReportBean.parse(reportNumber + "SQL.xml","zh_CN","1");
		System.out.println(reportNumber + "SQL.xml   "+defBean);

		// 设置版本信息，客户端可以跟据不同的版本信息
		ArrayList condField = defBean.getConFields();
		ArrayList disField = defBean.getDisFields();

	
		ArrayList conditions = new ArrayList();
		ArrayList cols = new ArrayList();
		ArrayList rows = new ArrayList();

		ArrayList configField = new ArrayList();

		OAWorkFlowTemplate workFlowTemp = BaseEnv.workFlowInfo.get(reportNumber);
		ReportField reportField = null;
		String approveStatus = "all";
		ArrayList<String> procParams = new ArrayList<String>();
		String statType = "";
		String moduleNumber = "";


		defBean.setSql(defBean.getSql().replaceAll("@condition:classLen", String.valueOf((parentCode.length() + 5))));

		boolean hasConditionValue = false;//用于记录是否有查询条件，如果是从连接过来的带有查询条件的，必须直接查询数据


		final Hashtable<String, ArrayList<ColConfigBean>> userConfigBean = BaseEnv.colConfig;
		ArrayList<ColConfigBean> colList = userConfigBean.get(reportNumber + "list");
		HashMap queryMaps = new HashMap();
		ArrayList paramList = new ArrayList();
		int queryCount = 0;
		String temp = "";
		
		if (defBean.getClassCode() != null && reportSetBean.getReportType().equals("PROCLIST"))// 如果是存储过程报表，并且是分级报表，则在参数最前面加上Parentcode,在生成存储过程报表相应SQL语句的时候也加上相应参数
		{
			paramList.add(parentCode);
		}
		//如果未级报表，自动加个parentCode参数		
		if (reportSetBean.isEndClassNumber() && reportSetBean.getReportType().equals("PROCLIST"))// 如果是存储过程报表，并且是未级报表，则在参数最前面加上Parentcode,在生成存储过程报表相应SQL语句的时候也加上相应参数
		{
			paramList.add(parentCode);
		}
		LoginBean loginBean=OnlineUserInfo.getLoginBean("1", "admin");
		for (int i = 0; i < condField.size(); i++) {

			ReportField field = (ReportField) condField.get(i);

			String sql = defBean.getSql();


			String fieldName = field.getFieldName();
			String fieldIdentity = field.getFieldIdentity();
			boolean flag = true;
		
			if (!flag) {
				procParams.add("");
				//如果条件字段没权限显示，则包含这个字段的@condsent_内容全部删除
				while (sql.indexOf("@condsent_" + field.getAsFieldName()) > 0) {
					int indexs = sql.indexOf("@condsent_" + field.getAsFieldName() + ":[");
					int indexe = sql.indexOf("]", indexs);
					sql = sql.substring(0, indexs) + sql.substring(indexe + 1);
				}
				defBean.setSql(sql);
				continue;
			}

			if ("GoodsField".equals(field.getFieldSysType())) {
				// 如果属性字段未启用，并且字段系统类型为商品属性则界面不显示
				String propFieldName = field.getAsFieldName();
				if (propFieldName.toLowerCase().endsWith("nv")) {
					propFieldName = propFieldName.substring(0, propFieldName.length() - 2);
				}
				// 如果字段名字为颜色名称，则按颜色编号来处理属性
				if ("colorName".equalsIgnoreCase(propFieldName)) {
					propFieldName = "color";
				}
				GoodsPropInfoBean gpBean = (GoodsPropInfoBean) BaseEnv.propIgnoreCaseMap.get(propFieldName.toLowerCase());
				if (gpBean != null && gpBean.getIsUsed() == 2) {
					procParams.add("");
					continue;
				}
			}

			List sysTypeList = ((EnumerateBean) BaseEnv.enumerationMap.get("FieldSysType")).getEnumItem();
			if (sysTypeList != null) {
				boolean isContinue = false;
				for (Object obj : sysTypeList) {
					EnumerateItemBean item = (EnumerateItemBean) obj;
					boolean openSysType = BaseEnv.systemSet.get(item.getEnumValue()) == null ? true : Boolean.parseBoolean(BaseEnv.systemSet.get(item.getEnumValue()).getSetting());
					if (!openSysType && field.getFieldSysType().equals(item.getEnumValue())) {
						procParams.add("");
						isContinue = true;
						break;
					}
				}
				if (isContinue) {
					sql = sql.replaceAll("@conditionv2:" + field.getAsFieldName(), "''");
					defBean.setSql(sql);
					continue;
				}
			}

			byte fieldType = field.getFieldType();
			byte inputType = field.getInputType();

			/**
			 * 0 : 显示名Dispaly 1 : 字段名AsFieldName; 2 : 值 Value 3 : 类型 inputType 4
			 * : 弹出窗名，枚举名 5 ：系统类型getFieldSysType 6 : 弹出窗名getPopUpName。 复选框也指枚举名
			 * 7 ：是否必选 8 : 数据类型 9 : 默认值 10:classCode
			 * 11:弹出窗版本号大于1则把弹出框中所有displayfield属性为true的字段存储在cond
			 * [11]中，在客户端解析这个字段，生成文本框 12：存储子查询语句
			 * 13：存储弹出窗所属表名belongTableName
			 */
			String[] cond = new String[14];
			cond[0] = field.getDisplay();
			cond[12] = field.getSubSQL();

			cond[1] = field.getAsFieldName();

			String value = null;
			
			if(cond[1].equals(conditionStr.substring(0,conditionStr.indexOf("=")))) {
				value=conditionStr.substring(conditionStr.indexOf("=")+1);
			}
			value = GlobalsTool.encodeTextCode(value);

			 
			if (value != null) {//从条件过来的值
				hasConditionValue = true;
			}

			 
			
			String dv = field.getDefaultValue();
			//(value == null|| value.length() == 0)
			if (value == null && dv != null && dv.length() > 0) {

				if (dv.indexOf("@MEM:") >= 0 || dv.indexOf("@Sess:") >= 0) {
					ArrayList sysParam = new ArrayList();
					ArrayList sessParam = new ArrayList();
					ArrayList codeParam = new ArrayList();
					ArrayList queryParam = new ArrayList();
					ConfigParse.parseSentenceGetParam(dv, new ArrayList(), sysParam, sessParam, codeParam, queryParam, null);
					HashMap sysMap = ConfigParse.getSystemParam(sysParam, loginBean.getSunCmpClassCode());
					HashMap sessMap = ConfigParse.getSessParam(loginBean.getId(), sessParam);
					Connection conn = null;
					HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);
					dv = ConfigParse.parseSentencePutParam(dv, new HashMap(), sysMap, new HashMap(), sessMap, codeMap, null, null, null);
					BSFManager evalM = BaseEnv.evalManager;

					if (fieldType == DBFieldInfoBean.FIELD_INT) {
						if (dv.length() > 0 && dv.indexOf(".") > -1) {
							dv = dv.substring(0, dv.indexOf("."));
						}
					}
				}
				value = dv;
				if ("-1".equals(dv) && "AccYear".equals(field.getAsFieldName())) {
					value = "";
				}
			}
			cond[2] = value;
			
			if(cond[2] != null && cond[2].contains("@join:")){
				cond[2] = cond[2].replaceAll("@join:", "&");
			}
			
			if (cond[2] == null) {
				if (field.getInputType() == 5 || field.getPopUpName().equals("zero")) {
					cond[2] = "";
				
				} else {
					
					value = cond[2];
				}
			} 

			cond[2] = cond[2] == null ? "" : cond[2];
			procParams.add(cond[2]);


			cond[3] = "0";
			if (inputType == DBFieldInfoBean.INPUT_ENUMERATE) {
				cond[3] = "1";
			} else if (inputType == DBFieldInfoBean.INPUT_DOWNLOAD_SELECT) {
				cond[3] = "16";
			} else if (inputType == DBFieldInfoBean.INPUT_CHECKBOX) {
				cond[3] = "5";
				cond[6] = field.getPopUpName();
			} else if (fieldType == DBFieldInfoBean.FIELD_DATE_LONG || fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
				cond[3] = "2";
			} else if (inputType == DBFieldInfoBean.INPUT_HIDDEN) {
				cond[3] = "3";
			} else if (inputType == DBFieldInfoBean.INPUT_HIDDEN_INPUT) {
				cond[3] = "3";
			} else if (inputType == DBFieldInfoBean.INPUT_MAIN_TABLE) {
				cond[3] = "4";
				cond[6] = field.getPopUpName();
			} else if (inputType == DBFieldInfoBean.INPUT_NO) {
				cond[3] = "100";
				cond[2] = "";
			}

			cond[4] = field.getPopUpName();
			if (!cond[4].equals("")) {
				PopupSelectBean bean = (PopupSelectBean) BaseEnv.popupSelectMap.get(cond[4]);
				if (bean != null && bean.getReturnFields() != null) {
					cond[13] = bean.getBelongTableName();
					for (Object o : bean.getReturnFields()) {
						PopField tmp_f = (PopField) o;
						if (tmp_f.fieldName.toLowerCase().indexOf("iscatalog") != -1) {
							cond[10] = field.getCondition();
							//HashMap condRepMap = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);
							String tmpStr ="";
							
							if(cond[2]==null || cond[2].length() ==0){
								//如果对应值为空，则弹出窗显示也要为空
								tmpStr = "";
							}

							 
							if (cond[10].equals("")) {
								cond[10] = field.getFieldName().substring(0, field.getFieldName().indexOf('.')) + ".classCode";
							}
							break;
						}
					}
					 

					if (bean.getVersion() > 1) {// 版本号大于1则把弹出框中所有displayfield属性为true的字段存储在cond[11]中，在客户端解析这个字段，生成文本框

						// 存储格式为 字段名(多语言版本);父字段名|字段之间用｜分隔，每个字段内容用；分隔
						if (bean != null && bean.getDisplayField() != null && bean.getDisplayField().size() > 0) {
							StringBuffer sb = new StringBuffer();
							//HashMap condRepMap = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);

							for (Object o : bean.getDisplayField()) {
								PopField tmp_f = (PopField) o;
								if (!tmp_f.parentDisplay) {
									continue;
								}
								String display = tmp_f.getDisplay();
								if (display == null || display.trim().equals(""))
									display = tmp_f.getFieldName();
								if (display.indexOf('.') == -1) {
									sb.append(display + ";");
								} else {
									DBFieldInfoBean dbf = GlobalsTool.getFieldBean(display);
									if (dbf != null)
										display = dbf.getDisplay().get(loginBean.getDefLoc());
									else if (tmp_f.getFieldName().indexOf('.') > -1) { //用变量方式后，所有GoodsFullName的display都换成了@TableName.GoodsCode所以还是用原fieldName
										dbf = GlobalsTool.getFieldBean(tmp_f.getFieldName());
										if (dbf != null)
											display = dbf.getDisplay().get(loginBean.getDefLoc());
									}

									sb.append(display + ";");
								}

								String inputStr = tmp_f.getAsName();
								if (inputStr == null || inputStr.equals(""))
									inputStr = tmp_f.getFieldName();
								 
								String retValue ="";


								if(cond[2]==null || cond[2].length() ==0){
									//如果对应值为空，则弹出窗显示也要为空	
									retValue = "";
								}
								
								if (retValue == null)
									retValue = "";								
								
								

								sb.append(inputStr + ";" + retValue + "|");
							}
							if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '|')
								sb.deleteCharAt(sb.length() - 1);
							cond[11] = sb.toString();
							
							
						}
					}
				}
			}
			cond[5] = field.getFieldSysType();
			cond[7] = field.getIsNull();
			if (fieldType == DBFieldInfoBean.FIELD_ANY) {
				cond[8] = "any";
			} else if (fieldType == DBFieldInfoBean.FIELD_DOUBLE) {
				cond[8] = "double";
			} else if (fieldType == DBFieldInfoBean.FIELD_INT) {
				cond[8] = "int";
			} else if (fieldType == DBFieldInfoBean.FIELD_DATE_LONG || fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
				cond[8] = "date";
			} else if (fieldType == DBFieldInfoBean.FIELD_DATE_TIME) {
				cond[8] = "time";
			} else {
				cond[8] = "any";
			}
			cond[9] = field.getDefaultValue();
			conditions.add(cond);

			/* 含有@conditionFun：标识时， 不对值进行解析 */
			if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
				sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + cond[2] + "'");
			} else {
				PopupSelectBean popSelect = null;
				if (field.getInputType() == 2) {
					popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
				}

				// 如果这个条件的前后包含了@condsent_:[]并且用户没有输入条件值，那么把@condsent_:[...]去掉,如果用户输入了值，把@condsent_:[]去掉
				while (sql.indexOf("@condsent_" + field.getAsFieldName()) > 0) {
					int indexs = sql.indexOf("@condsent_" + field.getAsFieldName() + ":[");
					int indexe = sql.indexOf("]", indexs);
					if (value != null && value.trim().length() > 0) {
						sql = sql.substring(0, indexs) + sql.substring(indexs + ("@condsent_" + field.getAsFieldName() + ":[").length(), indexe) + sql.substring(indexe + 1);
					} else {
						sql = sql.substring(0, indexs) + sql.substring(indexe + 1);
					}
				}

				if (popSelect != null && popSelect.getVersion() > 1) {
					// String pat =
					// "(\\)| and | or )(.*?(like|=|\\<|\\>).*?\\@condition:"
					// + field.getAsFieldName() + ".*?)( and | or |\\()";
					// Pattern p = Pattern.compile(pat);
					// Matcher m = p.matcher(sql);
					sql = sql.replaceAll("@conditionv2:" + field.getAsFieldName(), "'" + cond[2] + "'");
					String pat = "@condition:" + field.getAsFieldName();
					int pos = 0;
					PopupSelectBean bean = (PopupSelectBean) BaseEnv.popupSelectMap.get(cond[4]);
					while (bean.getVersion() > 1 && (pos = sql.indexOf(pat, pos)) != -1) {
						int left = sql.lastIndexOf(")", pos) + 1;
						left = Math.max(left, sql.lastIndexOf(" and ", pos) + 5);
						left = Math.max(left, sql.lastIndexOf(" or ", pos) + 4);
						left = Math.max(left, sql.lastIndexOf("where ", pos) + 6);

						int right = pos + pat.length();

						int op = sql.lastIndexOf("=", pos);
						op = Math.max(op, sql.lastIndexOf(" like ", pos));
						op = Math.max(op, sql.lastIndexOf(" in ", pos));

						//HashMap condRepMap = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);

						String tmpStr =  "";
						String tmpField = sql.substring(left, op).trim();
						while (tmpField.startsWith("("))
							tmpField = tmpField.substring(1);
						String tmpValue = replaceClassCode(field, tmpStr, tmpField);

						if (tmpValue != null) {
							String tmpC = sql.substring(0, left);
							tmpC += " (" + tmpValue + ") ";
							pos = tmpC.length();
							tmpC += sql.substring(right);
							sql = tmpC;
						} else {
							pos = right;
						}

					}
				}
				if (popSelect != null && "checkBox".equals(popSelect.getType()) && value != null && value.contains(",") && sql.contains("@condition:" + field.getAsFieldName())) {
					 
				} else {
					sql = sql.replaceAll("@condition:" + field.getAsFieldName(), "'" + cond[2] + "'");
				}
			}
			for (int j = 0; j < defBean.getQueryFields().size(); j++) {
				ReportField rf = (ReportField) defBean.getQueryFields().get(j);
				if (rf.getFieldName().contains("@condition:" + field.getAsFieldName())) {
					String fieldStr = "";
					/* 含有@conditionFun：标识时， 不对值进行解析 */
					if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
						sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + cond[2] + "'");
					} else {
						PopupSelectBean popSelect = null;
						if (field.getInputType() == 2) {
							popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
						}
						if (popSelect != null && "checkBox".equals(popSelect.getType()) && cond[2] != null && cond[2].contains(",")
								&& rf.getFieldName().contains("@condition:" + field.getAsFieldName())) {
						} else {
							fieldStr = rf.getFieldName().replaceAll("@condition:" + field.getAsFieldName(), "'" + cond[2] + "'");
						}
					}
					rf.setFieldName(fieldStr);
				}
				if (rf.getLinkAdd() != null && rf.getLinkAdd().contains("@condition:" + field.getAsFieldName())) {
					String linkAdd = rf.getLinkAdd().replaceAll("@condition:" + field.getAsFieldName(), cond[2]);
					rf.setLinkAdd(linkAdd);
				}
				if (rf.getCondition() != null && rf.getCondition().contains("@condition:" + field.getAsFieldName())) {
					String condition = "";
					/* 含有@conditionFun：标识时， 不对值进行解析 */
					if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
						sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + cond[2] + "'");
					} else {
						PopupSelectBean popSelect = null;
						if (field.getInputType() == 2) {
							popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
						}
						if (popSelect != null && "checkBox".equals(popSelect.getType()) && cond[2] != null && cond[2].contains(",")
								&& rf.getCondition().contains("@condition:" + field.getAsFieldName())) {
						
						} else {
							condition = rf.getCondition().replaceAll("@condition:" + field.getAsFieldName(), "'" + cond[2] + "'");
						}
					}
					rf.setCondition(condition);
				}
			}
			defBean.setSql(sql);
		
		}
		String sql = defBean.getSql();
		for (int i = 0; i < condField.size(); i++) {
			ReportField field = (ReportField) condField.get(i);

			//在上面条件的处理中，所有值都已经存在这个map中，所以直接取值即可	
			String value = "";
			if(field.getAsFieldName().equals(conditionStr.substring(0,conditionStr.indexOf("=")))) {
				value=conditionStr.substring(conditionStr.indexOf("=")+1);
			}

			if (field.getAsFieldName().equals("workFlowNodeName") && ((value == null && "all".equals(field.getDefaultValue())) || "all".equals(value))) {
				value = "";
				field.setDefaultValue("");
			}

			int indexs = sql.indexOf(field.getConditionJoin() + " (" + field.getFieldName().replaceAll("\n", ""));
			int indexe = sql.indexOf(field.getCondition() + ")") + field.getCondition().length() + 1;

			// 假如此查询条件的语句不是 “字段名 like” 或者 “字段名 =” 的结构，而是作为子查询的条件，就做如下取值
			if (indexs < 0 || indexe < 0) {
				indexs = sql.indexOf(field.getConditionJoin() + " " + field.getCondition());
				indexe = sql.indexOf(field.getCondition()) + field.getCondition().length();
			}
			if (indexs < 0) {
				System.out.println("Error report field -------------" + field.getFieldName() + " 如果是复合字段，请最好加括号");
			}

			String t = "";
			if ((value != null && value.length() > 0) || (value != null && reportSetBean.getReportType().equals("PROCLIST"))) {
				if (field.getCondition().contains("like")) {
					value = value.replace("%", "!%");
					value = value.replace("[", "![");
					value = value.replace("]", "!]");
				}
				if (value.contains(",") && !",".equals(value)) {
					if (field.getCondition() != null && field.getCondition().contains("@ValueofFun:getClassCode")) {
						int p = sql.indexOf("@ValueofFun:getClassCode");
						if (p < 0)
							continue;
						String getCcFun = sql.substring(sql.indexOf("(", p) + 2, sql.indexOf(")", p));
						String[] ccfs = getCcFun.split(",");
						String condition2 = mgt.getClassCode(ccfs[0], ccfs[1], value);
						indexe = p + condition2.length() + sql.substring(sql.indexOf(")", p)).indexOf(")") + 2;
						sql = sql.substring(0, p) + condition2 + sql.substring(sql.indexOf(")", p) + 2);
						t = sql.substring(0, indexe);
						// System.out.println(rf.getCondition());
					} else {
						t = sql.substring(0, indexs);
						// 处理弹出窗返回带有iscatalog部分的SQL语句

						String tmpValue = null;
						 

						 

						if (reportSetBean.getReportType().equals("PROCLIST"))// 如果是存储过程报表，则把参数加入paramList里
						{
						 
								paramList.add(value);
						}
					}
				} else {
					// 处理弹出窗返回带有iscatalog部分的SQL语句
					String tmpValue = replaceClassCode(field, value);

					if ((sql.indexOf(field.getCondition())) != -1) {
						if (field.getCondition().indexOf(" or 1=1") == -1 && tmpValue != null)
							t += " " + field.getConditionJoin() + " (" + tmpValue + ") ";
						else
							t += " " + field.getConditionJoin() + " ( 1=1 ) ";
					}

					// ======================================处理弹出窗带有iscatalog字段

					// 周新宇，增加@ValueofFun:getClassCode函数的解释
					if (field.getCondition() != null && field.getCondition().contains("@ValueofFun:getClassCode")) {
						int p = sql.indexOf("@ValueofFun:getClassCode");
						if (p < 0)
							continue;
						String getCcFun = sql.substring(sql.indexOf("(", p) + 1, sql.indexOf(")", p));
						String[] ccfs = getCcFun.split(",");
						String condition = mgt.getClassCode(ccfs[0], ccfs[1], ccfs[2]);
						indexe = p + condition.length() + sql.substring(sql.indexOf(")", p)).indexOf(")") + 1;
						sql = sql.substring(0, p) + condition + sql.substring(sql.indexOf(")", p) + 1);
						t = sql.substring(0, indexe);
						// System.out.println(rf.getCondition());
					} else {
						if (reportSetBean.getReportType().equals("PROCLIST"))// 如果是存储过程报表，则把参数加入paramList里
						{
							 
								paramList.add(value);
						} else if (indexe != -1 && tmpValue == null) {
							t = sql.substring(0, indexe);
							paramList.add(value);
						} else if (indexs != -1) {
							t = sql.substring(0, indexs) + t;
						}
					}
					if (field.getInputType() != DBFieldInfoBean.INPUT_HIDDEN && field.getInputType() != DBFieldInfoBean.INPUT_HIDDEN_INPUT) {
						queryCount++;
					}
				}
				queryMaps.put(field.getAsFieldName(), value);
			} else {
				if (field.getDefaultValue() != null && field.getDefaultValue().length() > 0 && field.getInputType() != DBFieldInfoBean.INPUT_NO) {
					String se = sql.substring(indexs, indexe);
					if (field.getFieldType() != 0 && field.getFieldType() != 1) {
						se = se.replace("?", "'" + field.getDefaultValue() + "'");
					} else {
						se = se.replace("?", field.getDefaultValue());
					}
					t = sql.substring(0, indexs) + se;
				} else {
					if (indexs != -1)
						t = sql.substring(0, indexs);
				}
			}
			DBFieldInfoBean fieldBean = null;
			if (field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE && field.getFieldName().indexOf(".") > 0) {
				fieldBean = GlobalsTool.getFieldBean(field.getFieldName().substring(0, field.getFieldName().indexOf(".")), field.getFieldName().substring(field.getFieldName().indexOf(".") + 1));
			}

			if (value != null && value.length() > 0 && (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || (fieldBean != null && fieldBean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE))) {
				if (t.indexOf("where 1=1") > 0) {
					t = t.substring(0, t.indexOf("where 1=1")) + t.substring(t.indexOf("where 1=1")).replace(field.getFieldName(), "a" + i + "." + locale);
				} else {
					t = t.replace(field.getFieldName(), "a" + i + "." + locale);
				}

			}
			temp += t;
			if (value != null && value.length() > 0 && (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || (fieldBean != null && fieldBean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE))) {
				temp = temp.substring(0, temp.indexOf("where 1=1")) + " left join tblLanguage a" + i + " on " + field.getFieldName() + "=a" + i + ".id " + temp.substring(temp.indexOf("where 1=1"));
			}
			sql = sql.substring(indexe);
		}

		String sunCmp_sql = "";

		String tableName = reportNumber;
		Hashtable allTables =BaseEnv.tableInfos;
		String sunClassCode ="00001";
		if ("".equals(sunClassCode) || sunClassCode == null || !"TABLELIST".equals(reportSetBean.getReportType())) {
			sunCmp_sql = "";
		} else {
			
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
			boolean isSunCmpShare = false;
			if (tableInfo == null || tableInfo.getIsSunCmpShare() == 1) {
				isSunCmpShare = true;
			}
			if (!isSunCmpShare) {
				//				sunCmp_sql = " and " + tableName + ".SCompanyID like '" + sunClassCode + "%'  ";
				sunCmp_sql = "";
			} else {
				sunCmp_sql = "";
			}
		}
		sql = temp + sql;
		sql = sql.replace("'%')", "'%' escape '!')");

		ArrayList sysParam = new ArrayList();
		ArrayList tabParam = new ArrayList();
		ArrayList sessParam = new ArrayList();
		ArrayList codeParam = new ArrayList();
		ArrayList queryParam = new ArrayList();
		HashMap tabMap = new HashMap();

		ConfigParse.parseSentenceGetParam(sql, tabParam, sysParam, sessParam, codeParam, queryParam, null);

		HashMap sysMap = ConfigParse.getSystemParam(sysParam,sunClassCode);
		HashMap sessMap = ConfigParse.getSessParam("1", sessParam);
		Connection conn = null;
		HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);
		sql = ConfigParse.parseSentencePutParam(sql, null, sysMap, null, sessMap, codeMap, null, null, null);

		for (int i = 0; i < defBean.getQueryFields().size(); i++) {
			ReportField rf = (ReportField) defBean.getQueryFields().get(i);
			rf.setFieldName(ConfigParse.parseSentencePutParam(rf.getFieldName(), null, sysMap, null, sessMap, codeMap, null, null, null));
		}
		// 周新宇，增加@ValueofFun:getClassCode函数的解释
		while (sql.indexOf("@ValueofFun:getClassCode") > 0) {
			int p = sql.indexOf("@ValueofFun:getClassCode");
			if (p < 0)
				continue;
			String getCcFun = sql.substring(sql.indexOf("(", p) + 1, sql.indexOf(")", p));
			String[] ccfs = getCcFun.split(",");
			String condition = mgt.getClassCode(ccfs[0], ccfs[1], ccfs[2]);
			sql = sql.substring(0, p) + condition + sql.substring(sql.indexOf(")", p) + 1);
		}

		if ("TABLELIST".equals(reportSetBean.getReportType())) {
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
			if (tableInfo != null && tableInfo.getDraftFlag() == 1) {
					/* 默认不把打印查询出来 */
					if (sql.indexOf("where 1=1") > -1) {
						sql = sql.replace("where 1=1", "where 1=1 and isNull(" + tableName + ".workFlowNodeName,'')!='print'");
					}

			}

			DBTableInfoBean tableInfoBean = (DBTableInfoBean) allTables.get(reportNumber);
		}

		String src ="";
		int isUseFlow = 0;
		if (workFlowTemp != null) {
			isUseFlow = workFlowTemp.getTemplateStatus();
		}


		Result rs2 = new Result();

			rs2 = this.buildSQL(reportSetBean, defBean, "", sql, parentCode, paramList, procParams, hasConditionValue);

			if (rs2.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				return rs2;
			}
			HashMap sqls = (HashMap) rs2.getRetVal();
			sql = sqls.get("pageSql").toString();
			ReportDataMgt rdMgt=new ReportDataMgt();
			rs =rdMgt.getReportData(defBean, sql, paramList, locale, reportSetBean.getReportType(), 0, "", 0, 0, 0,
					"",0, "", OnlineUserInfo.getLoginBean("1", "admin"));
		return rs;
	}
	public Result buildSQL(ReportsBean reportSetBean, DefineReportBean defBean, String listType, String sql, String parentCode,
		ArrayList paramList, ArrayList<String> procParams, boolean hasConditionValue) {

		int slen = "select".length();
		ReportDataMgt mgt = new ReportDataMgt();
		String locale ="zh_CN";
		Result rs = new Result();
		String isAllListQuery = "YES";
		String selectType = "endClass";
		ReportField classRF = defBean.getClassCode();

		if (classRF != null) {
			String sqlT = sql;
			sql = sql.substring(0, sql.toLowerCase().indexOf("where 1=1") + "where 1=1".length());

			if ("YES".equals(isAllListQuery)) {
				String tableName = classRF.getFieldName().substring(0, classRF.getFieldName().indexOf("."));
				sql += " and " + tableName + ".isCatalog!=1";
				if (parentCode.length() > 0) {
					sql = sql + " and " + classRF.getFieldName() + " like '" + parentCode + "_____'";
				}
			} else {
				if (parentCode.length() > 0) {
					sql = sql + " and " + classRF.getFieldName() + " like '" + parentCode + "_____'";
				} else {
					if (!sqlT.contains((classRF.getFieldName() + " = ").toUpperCase())) {
						sql = sql + " and len(" + classRF.getFieldName() + ")=5";
					}
				}
			}

			sql = sql + sqlT.substring(sqlT.toLowerCase().indexOf("where 1=1") + "where 1=1".length());

		}

		HashMap result = new HashMap();
	
		if (!"popData".equals(listType)) {
			String orderByContent = "";
			if (sql.indexOf("order by") > -1) {
				orderByContent = sql.substring(sql.indexOf("order by"));
				sql = sql.substring(0, sql.indexOf("order by"));
			} else {
				orderByContent = "order by " + ((ReportField) defBean.getQueryFields().get(0)).getFieldName();
			}
			String fieldAS = "";// 所有需要显示的字段，用逗号隔开
			DBTableInfoBean tableInfo = (DBTableInfoBean) BaseEnv.tableInfos.get(reportSetBean.getReportNumber());
			String orderAS = "";// 排序用的字段

			for (int i = 0; i < defBean.getFieldInfo().size(); i++) {
				ReportField rep = (ReportField) defBean.getFieldInfo().get(i);
				if (rep.getDisplayFlag() != null && Integer.parseInt(rep.getDisplayFlag()) == 1) {
					fieldAS = fieldAS + rep.getAsFieldName() + ",";
					if ("降".equals(rep.getOrderByFlag())) {
						orderAS += rep.getAsFieldName() + " desc,";
					}
					if ("升".equals(rep.getOrderByFlag())) {
						orderAS += rep.getAsFieldName() + " asc,";
					}
				}
			}
			if (orderAS.length() > 0) {
				orderAS = " order by " + orderAS.substring(0, orderAS.length() - 1);
			}
			if (tableInfo == null || !tableInfo.getTableName().startsWith("CRM")) {
				orderAS = "";
			} else {
				orderAS += " ";
			}
			fieldAS = fieldAS.substring(0, fieldAS.length() - 1);

			if (reportSetBean.getReportType().equals("PROCLIST")) {
				StringBuffer sb = new StringBuffer();
				int size = procParams.size();
				while (size-- > 0) {
					sb.append(",@" + ((ReportField) defBean.getConFields().get(procParams.size() - size - 1)).getAsFieldName() + "=?");
				}
				sql = "{call " + reportSetBean.getProcName() + " (@pageNo=?,@pageSize=?,@locale=?,@userId=?,@sunCompany=?";
				//分级报表自动加分级classCode
				if (defBean.getClassCode() != null) {
					sql += ",@" + defBean.getClassCode().getAsFieldName() + "=?";
				}
				//未级报有自动加父级classCode
				if(reportSetBean.isEndClassNumber()){
					sql += ",@parentCode=?";
				}
				sql += sb.toString() + ")}";
				
				defBean.setPageSizeE(99999999);
				defBean.setPageSizeS(0);
			} 
			System.out.println("存储过程SQL:"+sql);
			result.put("dataSql", sql);
		} else {
			result.put("dataSql", sql);
			rs.setRetVal(sql);
		}
		result.put("pageSql", sql);

		rs.setRetVal(result);
		return rs;
	
	}
	/**
	 * 解析报表并获取
	 * 
	 * @return
	 * @throws Exception
	 */
	public Result queryReportSql(ServletContext sContext, HashMap map) throws Exception {
		Result rs = new Result();
		ReportData reportData = new ReportData();
		ReportDataMgt mgt = new ReportDataMgt();
		ReportSetMgt setMgt = new ReportSetMgt();
		HashMap parameterMap = (HashMap) sContext.getAttribute("parameterMap");
		String reportNumber = String.valueOf(map.get("reportNumber"));

		ReportsBean reportSetBean = (ReportsBean) setMgt.getReportSetInfo(reportNumber, "zh_cn").getRetVal();

		/* 获取报表信息 */
		DefineReportBean defBean = DefineReportBean.parseReport(String.valueOf(map.get("sqlFileName")));
		OAWorkFlowTemplate workFlowTemp = BaseEnv.workFlowInfo.get(reportNumber);
		ReportField reportField = null;
		String approveStatus = "notApprove";
		Result res_audit = mgt.getBillApproveStatus(reportNumber);
		if (res_audit.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			approveStatus = (String) res_audit.getRetVal();
		}

		ArrayList fieldList = defBean.getConFields();
		for (int k = 0; k < fieldList.size(); k++) {
			ReportField field = (ReportField) fieldList.get(k);
			String sql = defBean.getSql();
			Object o = parameterMap.get(field.getAsFieldName());
			String value = "";
			if (o == null) {
				value = "";
			} else {
				value = String.valueOf(o);
			}
			
			String dv = field.getDefaultValue();

			if ((value == null || (value != null && value.length() == 0)) && dv != null && dv.length() > 0) {

				if (dv.indexOf("@MEM:") >= 0 || dv.indexOf("@Sess:") >= 0) {
					sql = sql.replaceAll("@Sess:UserId", "1").replaceAll("@Sess:SCompanyID", "00001");
					ArrayList sysParam = new ArrayList();
					ArrayList sessParam = new ArrayList();
					ArrayList codeParam = new ArrayList();
					ArrayList queryParam = new ArrayList();
					ConfigParse.parseSentenceGetParam(dv, new ArrayList(), sysParam, sessParam, codeParam, queryParam, null);
					HashMap sysMap = ConfigParse.getSystemParam(sysParam, "00001");
					HashMap sessMap = ConfigParse.getSessParam("1", sessParam);
					Connection conn = null;
					HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);
					dv = ConfigParse.parseSentencePutParam(dv, new HashMap(), sysMap, new HashMap(), sessMap, codeMap, null, null, null);
					BSFManager evalM = BaseEnv.evalManager;

					if (field.getFieldType() == DBFieldInfoBean.FIELD_INT) {
						if (dv.length() > 0 && dv.indexOf(".") > -1) {
							dv = dv.substring(0, dv.indexOf("."));
						}
					}
				}
				value = dv;
				if ("-1".equals(dv) && "AccYear".equals(field.getAsFieldName())) {
					value = "";
				}
			}
			
			
			if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
				sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + value + "'");
			} else {
				PopupSelectBean popSelect = null;
				if (field.getInputType() == 2) {
					popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
				}

				// 如果这个条件的前后包含了@condsent_:[]并且用户没有输入条件值，那么把@condsent_:[...]去掉,如果用户输入了值，把@condsent_:[]去掉
				while (sql.indexOf("@condsent_" + field.getAsFieldName()) > 0) {
					int indexs = sql.indexOf("@condsent_" + field.getAsFieldName() + ":[");
					int indexe = sql.indexOf("]", indexs);
					if (value != null && value.trim().length() > 0) {
						sql = sql.substring(0, indexs) + sql.substring(indexs + ("@condsent_" + field.getAsFieldName() + ":[").length(), indexe)
								+ sql.substring(indexe + 1);
					} else {
						sql = sql.substring(0, indexs) + sql.substring(indexe + 1);
					}
				}
				
				if (popSelect != null && popSelect.getVersion() > 1) {
					sql = sql.replaceAll("@conditionv2:" + field.getAsFieldName(), "'" + value + "'");
				}
				
				if (popSelect != null && "checkBox".equals(popSelect.getType()) && value != null && value.contains(",")
						&& sql.contains("@condition:" + field.getAsFieldName())) {
					sql = reportData.parseMultiVal("@condition:" + field.getAsFieldName(), sql, null, value);
				} else {
					sql = sql.replaceAll("@condition:" + field.getAsFieldName(), "'" + value + "'");
				}
			}
			
			List sysTypeList = ((EnumerateBean) BaseEnv.enumerationMap.get("FieldSysType")).getEnumItem();
			if (sysTypeList != null) {
				boolean isContinue = false;
				for (Object obj : sysTypeList) {
					EnumerateItemBean item = (EnumerateItemBean) obj;
					boolean openSysType = BaseEnv.systemSet.get(item.getEnumValue()) == null ? true : Boolean.parseBoolean(BaseEnv.systemSet.get(
							item.getEnumValue()).getSetting());
					if (!openSysType && field.getFieldSysType().equals(item.getEnumValue())) {
						isContinue = true;
						break;
					}
				}
				if (isContinue) {
					sql = sql.replaceAll("@conditionv2:"+field.getAsFieldName(), "''");
					defBean.setSql(sql);
					continue;
				}
			}
			
			for (int j = 0; j < defBean.getQueryFields().size(); j++) {
				ReportField rf = (ReportField) defBean.getQueryFields().get(j);
				if (rf.getFieldName().contains("@condition:" + field.getAsFieldName())) {
					String fieldStr = "";
					/* 含有@conditionFun：标识时， 不对值进行解析 */
					if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
						sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + value + "'");
					} else {
						PopupSelectBean popSelect = null;
						if (field.getInputType() == 2) {
							popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
						}
						
						if (popSelect != null && popSelect.getVersion() > 1) {
							sql = sql.replaceAll("@conditionv2:" + field.getAsFieldName(), "'" + value + "'");
						}
						
						if (popSelect != null && "checkBox".equals(popSelect.getType()) && value != null && value.contains(",")
								&& rf.getFieldName().contains("@condition:" + field.getAsFieldName())) {
							fieldStr = reportData.parseMultiVal("@condition:" + field.getAsFieldName(), rf.getFieldName(), null, value);
						} else {
							fieldStr = rf.getFieldName().replaceAll("@condition:" + field.getAsFieldName(), "'" + value + "'");
						}
					}
					rf.setFieldName(fieldStr);
				}
				if (rf.getLinkAdd() != null && rf.getLinkAdd().contains("@condition:" + field.getAsFieldName())) {
					String linkAdd = rf.getLinkAdd().replaceAll("@condition:" + field.getAsFieldName(), value);
					rf.setLinkAdd(linkAdd);
				}
				if (rf.getCondition() != null && rf.getCondition().contains("@condition:" + field.getAsFieldName())) {
					String condition = "";
					/* 含有@conditionFun：标识时， 不对值进行解析 */
					if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
						sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + value + "'");
					} else {
						PopupSelectBean popSelect = null;
						if (field.getInputType() == 2) {
							popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
						}
						if (popSelect != null && "checkBox".equals(popSelect.getType()) && value != null && value.contains(",")
								&& rf.getCondition().contains("@condition:" + field.getAsFieldName())) {
							condition = reportData.parseMultiVal("@condition:" + field.getAsFieldName(), rf.getCondition(), null, value);
						} else {
							condition = rf.getCondition().replaceAll("@condition:" + field.getAsFieldName(), "'" + value + "'");
						}
					}
					rf.setCondition(condition);
				}
			}
			defBean.setSql(sql);
		}
		
		
		String sql = defBean.getSql();
		String sqlT = defBean.getSql();
		List paramList = new ArrayList(); // 保存的值参数
		if (defBean.getClassCode() != null && reportSetBean.getReportType().equals("PROCLIST"))// 如果是存储过程报表，并且是分级报表，则在参数最前面加上Parentcode,在生成存储过程报表相应SQL语句的时候也加上相应参数
		{
			paramList.add("");
		}
		//如果未级报表，自动加个parentCode参数		
		if (reportSetBean.isEndClassNumber() && reportSetBean.getReportType().equals("PROCLIST"))// 如果是存储过程报表，并且是未级报表，则在参数最前面加上Parentcode,在生成存储过程报表相应SQL语句的时候也加上相应参数
		{
			paramList.add("");
		}
		int queryCount = 0;
		HashMap queryMaps = new HashMap();
		String temp = "";
		sqlT = sqlT.substring(0, sqlT.indexOf("where 1=1") + "where 1=1".length()); // 查询where之前的字符串
		// 重新组装条件
		for (int k = 0; k < fieldList.size(); k++) {
			ReportField field = (ReportField) fieldList.get(k);
			int indexs = sql.indexOf(field.getConditionJoin() + " (" + field.getFieldName().replaceAll("\n", ""));
			int indexe = sql.indexOf(field.getCondition() + ")") + field.getCondition().length() + 1;

			// 假如此查询条件的语句不是 “字段名 like” 或者 “字段名 =” 的结构，而是作为子查询的条件，就做如下取值
			if (indexs < 0 || indexe < 0) {
				indexs = sql.indexOf(field.getConditionJoin() + " " + field.getCondition());
				indexe = sql.indexOf(field.getCondition()) + field.getCondition().length();
			}
			Object o = parameterMap.get(field.getAsFieldName());
			String value = "";
			if (o == null) {
				value = "";
			} else {
				value = String.valueOf(o);
			}
			String t = "";
			if ((value != null && String.valueOf(value).length() > 0) || (value != null && reportSetBean.getReportType().equals(ReportData.PROCLIST))) {
				if (field.getCondition().contains("like")) {
					value = value.replace("%", "!%");
					value = value.replace("[", "![");
					value = value.replace("]", "!]");
				}
				if (value.contains(",") && !",".equals(value)) {
					if (field.getCondition() != null && field.getCondition().contains("@ValueofFun:getClassCode")) {
						int p = sql.indexOf("@ValueofFun:getClassCode");
						if (p < 0)
							continue;
						String getCcFun = sql.substring(sql.indexOf("(", p) + 2, sql.indexOf(")", p));
						String[] ccfs = getCcFun.split(",");
						String condition = new ReportDataMgt().getClassCode(ccfs[0], ccfs[1], value);
						indexe = p + condition.length() + sql.substring(sql.indexOf(")", p)).indexOf(")") + 2;
						sql = sql.substring(0, p) + condition + sql.substring(sql.indexOf(")", p) + 2);
						t = sql.substring(0, indexe);
					} else {
						t = sql.substring(0, indexs);
						String tmpValue = null;
						if (parameterMap.get("hide_" + field.getAsFieldName()) != null) {
							tmpValue = replaceClassCode(field, String.valueOf(parameterMap.get("hide_" + field.getAsFieldName())));

							if ((sql.indexOf(field.getCondition())) != -1) {
								if (field.getCondition().indexOf(" or 1=1") == -1 && tmpValue != null)
									t += " " + field.getConditionJoin() + " (" + tmpValue + ") ";
								else
									t += " " + field.getConditionJoin() + " ( 1=1 ) ";
							}
						}

						// ======================================处理弹出窗带有iscatalog字段
						if (tmpValue == null && !reportSetBean.getReportType().equals(ReportData.PROCLIST))
							t += reportData.parseMultiVal("?", sql.substring(indexs, indexe), (ArrayList) paramList, value);

						if (reportSetBean.getReportType().equals(ReportData.PROCLIST)) {
							if (parameterMap.get("hide_" + field.getAsFieldName()) != null) {
								paramList.add(replaceClassCode(field.getPopUpName(), String.valueOf(parameterMap.get("hide_" + field.getAsFieldName()))));
							} else
								paramList.add(value);
						}
					}
				} else {
					// 处理弹出窗返回带有iscatalog部分的SQL语句
					String tmpValue = null;
					if (parameterMap.get("hide_" + field.getAsFieldName()) != null) {
						tmpValue = replaceClassCode(field, String.valueOf(parameterMap.get("hide_" + field.getAsFieldName())));
					}
					if ((sql.indexOf(field.getCondition())) != -1) {
						if (field.getCondition().indexOf(" or 1=1") == -1 && tmpValue != null) {
							t += " " + field.getConditionJoin() + " (" + tmpValue + ") ";
						} else {
							t += " " + field.getConditionJoin() + " ( 1=1 ) ";
						}
					}

					if (field.getCondition() != null && field.getCondition().contains("@ValueofFun:getClassCode")) {
						int p = sql.indexOf("@ValueofFun:getClassCode");
						if (p < 0)
							continue;
						String getCcFun = sql.substring(sql.indexOf("(", p) + 1, sql.indexOf(")", p));
						String[] ccfs = getCcFun.split(",");
						String condition = mgt.getClassCode(ccfs[0], ccfs[1], ccfs[2]);
						indexe = p + condition.length() + sql.substring(sql.indexOf(")", p)).indexOf(")") + 1;
						sql = sql.substring(0, p) + condition + sql.substring(sql.indexOf(")", p) + 1);
						t = sql.substring(0, indexe);
					} else {
						if (reportSetBean.getReportType().equals(ReportData.PROCLIST)) {
							if (parameterMap.get("hide_" + field.getAsFieldName()) != null) {
								paramList.add(replaceClassCode(field.getPopUpName(), String.valueOf(parameterMap.get("hide_" + field.getAsFieldName()))));
							} else
								paramList.add(value);
						} else if (indexe != -1 && tmpValue == null) {
							t = sql.substring(0, indexe);
							paramList.add(value);
						} else if (indexs != -1) {
							t = sql.substring(0, indexs) + t;
						}
					}
					if (field.getInputType() != DBFieldInfoBean.INPUT_HIDDEN && field.getInputType() != DBFieldInfoBean.INPUT_HIDDEN_INPUT) {
						queryCount++;
					}
				}
				queryMaps.put(field.getAsFieldName(), value);
			} else {
				if (field.getDefaultValue() != null && field.getDefaultValue().length() > 0 && field.getInputType() != DBFieldInfoBean.INPUT_NO) {
					String se = sql.substring(indexs, indexe);
					if (field.getFieldType() != 0 && field.getFieldType() != 1) {
						se = se.replace("?", "'" + field.getDefaultValue() + "'");
					} else {
						se = se.replace("?", field.getDefaultValue());
					}
					t = sql.substring(0, indexs) + se;
				} else {
					if (indexs != -1)
						t = sql.substring(0, indexs);
				}
			}
			DBFieldInfoBean fieldBean = null;
			if (field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE && field.getFieldName().indexOf(".") > 0) {
				fieldBean = GlobalsTool.getFieldBean(field.getFieldName().substring(0, field.getFieldName().indexOf(".")), field.getFieldName().substring(
						field.getFieldName().indexOf(".") + 1));
			}

			if (value != null
					&& value.length() > 0
					&& (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || (fieldBean != null && fieldBean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE))) {
				if (t.indexOf("where 1=1") > 0) {
					t = t.substring(0, t.indexOf("where 1=1")) + t.substring(t.indexOf("where 1=1")).replace(field.getFieldName(), "a" + k + "." + "zh_cn");
				} else {
					t = t.replace(field.getFieldName(), "a" + k + "." + "zh_cn");
				}

			}
			temp += t;
			if (value != null
					&& value.length() > 0
					&& (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || (fieldBean != null && fieldBean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE))) {
				temp = temp.substring(0, temp.indexOf("where 1=1")) + " left join tblLanguage a" + k + " on " + field.getFieldName() + "=a" + k + ".id "
						+ temp.substring(temp.indexOf("where 1=1"));
			}
			sql = sql.substring(indexe);
			
		}
		sql = temp + sql;
		sql = sql.replace("'%')", "'%' escape '!')");
		int count = sql.indexOf("1=1");
		if (count != -1) {
			count += 3;
			String sql_index = sql.substring(0, count);
			String sql_last = sql.substring(count);
			sql = sql_index + sql_last;
		}
		sql = sql.replaceAll("@Sess:UserId", "1").replaceAll("@Sess:SCompanyID", "00001");
		
		ArrayList sysParam = new ArrayList();
		ArrayList tabParam = new ArrayList();
		ArrayList sessParam = new ArrayList();
		ArrayList codeParam = new ArrayList();
		ArrayList queryParam = new ArrayList();

		ConfigParse.parseSentenceGetParam(sql, tabParam, sysParam, sessParam, codeParam, queryParam, null);

		HashMap sysMap = ConfigParse.getSystemParam(sysParam, null);
		HashMap sessMap = ConfigParse.getSessParam("1", sessParam);
		Connection conn = null;
		HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);
		sql = ConfigParse.parseSentencePutParam(sql, null, sysMap, null, sessMap, codeMap, null, null, null);
		if (ReportData.TEMPTABLE.equals(reportSetBean.getReportType())) {
			// 临时表处理（先执行存储过程，再执行查询数据）

		} else if (ReportData.TABLELIST.equals(reportSetBean.getReportType())) {
			// 数据表列表（直接查询数据）

		} else if (ReportData.PROCLIST.equals(reportSetBean.getReportType())) {
			// 存储过程报表（先执行存储过程直接查询数据）
			StringBuffer sb = new StringBuffer();
			int size = paramList.size();
			while (size-- > 0) {
				sb.append(",@" + ((ReportField) defBean.getConFields().get(paramList.size() - size - 1)).getAsFieldName() + "=?");
			}
			sql = "{call " + reportSetBean.getProcName() + " (@pageNo=?,@pageSize=?,@locale=?,@userId=?,@sunCompany=?";
			if (defBean.getClassCode() != null) {
				sql += ",@" + defBean.getClassCode().getAsFieldName() + "=?";
			}
			sql += sb.toString() + ")}";

			/*
			 * 执行存储过程
			 */
			final String sqls = sql;
			final List paramLists = paramList;
			final Result rss = rs;
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {
								CallableStatement st2 = conn.prepareCall(sqls);
								st2.setInt(1, 0);
								st2.setInt(2, 100);
								st2.setString(3, null);
								st2.setString(4, null);
								st2.setString(5, null);
								System.out.println("参数个数："+paramLists.size());
								for (int i = 1; i <= paramLists.size(); i++) {
									st2.setString(i + 5, paramLists.get(i - 1).toString());
								}
								st2.executeQuery();
								SQLWarning warn = st2.getWarnings();
								SQLWarning warns = new SQLWarning();
								while (warn != null) {
									warns = warn;
									warn = warn.getNextWarning();
								}
								String warnsql = warns.toString();
								warnsql = warnsql.replaceAll("java.sql.SQLWarning: ", "");
								warnsql = warnsql.substring(0, warnsql.indexOf("order by"));
								rss.setRetVal(warnsql);

							} catch (Exception ex) {
								ex.printStackTrace();
								return;
							}
						}
					});
					return rss.getRetCode();
				}
			});
			rs.setRetCode(retCode);

			// 返回要执行的sql查询行数
			sql = String.valueOf(rss.retVal);
			paramList = new ArrayList();
		} else if (ReportData.NORMAL.equals(reportSetBean.getReportType())) {
			// 普通报表（直接查询数据）
		}
		HashMap resultMap = new HashMap();
		if (sql.indexOf("order by") != -1) {
			sql = sql.substring(0, sql.indexOf("order by "));
		}
		sql = sql.replaceAll("noRight"," ");//把noRight过滤
		resultMap.put("pageSql", sql);
		resultMap.put("paramList", paramList);
		rs.setRetVal(resultMap);
		return rs;
	}

	/**
	 * 用来对有iscatalog字段弹出窗的条件进行替换
	 * 
	 * @param field
	 *            报表字段
	 * @param values
	 *            要进行替换的值
	 * @return 如果返回null表示未进替换，否则为替换后的字符串
	 */
	private String replaceClassCode(String popupName, String values) {
		int classCodePos = -1;
		PopupSelectBean bean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popupName);
		if (bean != null && bean.getReturnFields() != null) {
			for (int j = 0; j < bean.getReturnFields().size(); j++) {
				Object o = bean.getReturnFields().get(j);
				PopField tmp_f = (PopField) o;

				if (tmp_f.fieldName.toLowerCase().indexOf("classcode") != -1) {// 判断classcode的位置
					{
						classCodePos = j;
						break;
					}
				}
			}
			for (int j = 0; j < bean.getReturnFields().size(); j++) {
				Object o = bean.getReturnFields().get(j);
				PopField tmp_f = (PopField) o;
				if (tmp_f.fieldName.toLowerCase().indexOf("iscatalog") != -1) {
					if (classCodePos != -1)// 如果已经找到了classcode在弹出框中返回字段中的位置
					{
						StringBuffer tmpSql = new StringBuffer();// 用来生成用来替换用的SQL语句
						if (values == null || values.length() == 0)// 如果没有输入,则直接替换成一个真值的语句
						{
							tmpSql.append("");
						} else {
							String[] s = values.split("\\|");
							ArrayList<String> categoryValue = new ArrayList<String>();
							ArrayList<String> notCategoryValue = new ArrayList<String>();
							for (String string : s) {
								String[] itemValue = string.split(";");
								tmpSql.append(itemValue[classCodePos]).append(";").append(itemValue[j]).append("|");
							}
							if (tmpSql.length() > 1)
								tmpSql.deleteCharAt(tmpSql.length() - 1);
						}
						return tmpSql.toString();
					}
					break;
				}
			}
		}
		return null;
	}

	private String replaceClassCode(ReportField field, String values) {
		return replaceClassCode(field, values, field.getFieldName());
	}

	/**
	 * 用来对有iscatalog字段弹出窗的条件进行替换
	 * 
	 * @param field
	 *            报表字段
	 * @param values
	 *            要进行替换的值
	 * @return 如果返回null表示未进替换，否则为替换后的字符串
	 */
	private String replaceClassCode(ReportField field, String values, String fieldStr) {
		boolean willReplace = true;
		PopupSelectBean bean = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
		int classCodePos = -1;
		if (bean != null && bean.getReturnFields() != null) {
			for (int j = 0; j < bean.getReturnFields().size(); j++) {
				Object o = bean.getReturnFields().get(j);
				PopField tmp_f = (PopField) o;

				if (tmp_f.fieldName.toLowerCase().indexOf("classcode") != -1) {// 判断classcode的位置
					{
						classCodePos = j;
						break;
					}
				}
			}
			for (int j = 0; j < bean.getReturnFields().size(); j++) {
				Object o = bean.getReturnFields().get(j);
				PopField tmp_f = (PopField) o;
				if (tmp_f.fieldName.toLowerCase().indexOf("iscatalog") != -1) {
					if (classCodePos != -1)// 如果已经找到了classcode在弹出框中返回字段中的位置
					{
						willReplace = false;
						StringBuffer tmpSql = new StringBuffer();// 用来生成用来替换用的SQL语句
						if (values == null || values.length() == 0)// 如果没有输入,则直接替换成一个真值的语句
						{
							tmpSql.append(" 1=1 ");
						} else {
							String[] s = values.split("\\|");
							ArrayList<String> categoryValue = new ArrayList<String>();
							ArrayList<String> notCategoryValue = new ArrayList<String>();
							for (String string : s) {
								String[] itemValue = string.split(";");
								if (itemValue[j].trim().equals("1"))// 如果包含子节点
									categoryValue.add(itemValue[classCodePos]);
								else
									notCategoryValue.add(itemValue[classCodePos]);
							}
							for (String string : categoryValue) {
								tmpSql.append(fieldStr + "  like '" + string + "%' or ");// like前面有两个空格，不能删除,删了后后面的规则会把此句替换掉
							}
							if (notCategoryValue.size() > 0) {
								tmpSql.append(fieldStr + " in (");
								for (String string : notCategoryValue) {
									tmpSql.append("'" + string + "',");
								}
								tmpSql = tmpSql.deleteCharAt(tmpSql.length() - 1);
								tmpSql.append(")");
							} else {
								tmpSql.delete(tmpSql.length() - 4, tmpSql.length() - 1);
							}

						}
						return tmpSql.toString();
					}
					break;
				}
			}
		}
		return null;
	}

}
