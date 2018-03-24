package com.menyi.aio.web.customize;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.UnitBean;

import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.hibernate.Session;

import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.jdbc.Work;

import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

import org.hibernate.Query;

import com.dbfactory.DBCanstant;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.report.ReportSetMgt;

import java.sql.Statement;

import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.web.util.BaseEnv;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomizeMgt extends AIODBManager {

	/** 压缩一个文件 */
	private static void compressFile(File file, ZipOutputStream out) {
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			out.setEncoding("GBK");
			out.putNextEntry(new ZipEntry(file.getName()));
			int count;
			byte data[] = new byte[8192];
			while ((count = bis.read(data, 0, 8192)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void compressFile(String str, String fileName,
			ZipOutputStream out) {
		try {
			out.setEncoding("GBK");
			out.putNextEntry(new ZipEntry(fileName));
			int count;
			byte data[] = new byte[8192];
			out.write(str.getBytes("GBK"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getTableData(String sql, String tableName) {
		ArrayList param = new ArrayList();
		Result rs = this.sqlListMap(sql, param);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null) {
			ArrayList<HashMap> mapList = (ArrayList<HashMap>) rs.retVal;
			if (mapList.size() == 0) {
				return "";
			}
			ArrayList fl = new ArrayList();
			String fields = "";
			for (Object o : mapList.get(0).keySet()) {
				fields += ",[" + o + "]";
				fl.add(o);
			}
			fields = fields.substring(1);
			String ret = "";
			for (HashMap map : mapList) {
				ret += "\r\nINSERT INTO [" + tableName + "](" + fields
						+ ") values(";
				String ofs = "";
				for (Object o : fl) {
					ofs += ",'" + (map.get(o) == null ? "" : map.get(o)) + "'";
				}
				ofs = ofs.substring(1);
				ret += ofs + ")";
			}
			return ret;
		}
		return "";
	}

	public String export(String tableName, LoginBean loginBean) {
		FileOutputStream fileOutputStream;
		try {
			String fileName = "../temp/" + tableName + "_"
					+ loginBean.getEmpFullName() + ".zip";
			/* 如果存在，先删除压缩文件 */
			File zipFile = new File(fileName);
			if (zipFile.exists()) {
				zipFile.delete();
			} else if (!zipFile.getParentFile().exists()) {
				zipFile.getParentFile().mkdirs();
			}
			fileOutputStream = new FileOutputStream(fileName);

			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			ZipOutputStream out = new ZipOutputStream(fileOutputStream);
			String tsql = "";
			tsql += "--"
					+ BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss) + ":新建表\n";
			ArrayList childTableList = DDLOperation.getChildTables(tableName,
					BaseEnv.tableInfos);
			for (DBTableInfoBean ctableInfo : (ArrayList<DBTableInfoBean>) childTableList) {
				tsql += "    IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].["
						+ ctableInfo.getTableName()
						+ "]') AND type in (N'U'))\n";
				tsql += "    begin\n";
				tsql += "    delete from tblLanguage where id in (select f.languageId from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '"
						+ ctableInfo.getTableName() + "') \n";
				tsql += "    delete from tblLanguage where id in (select f.groupName from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '"
						+ ctableInfo.getTableName() + "')\n";
				tsql += "    delete from tblLanguage where id in (select t.languageId from tblDBTableInfo t where t.tableName = '"
						+ ctableInfo.getTableName() + "') \n";
				tsql += "    exec proc_deleteExistsTable @tableName='"
						+ ctableInfo.getTableName() + "'\n";
				tsql += "    drop table " + ctableInfo.getTableName() + "\n";
				tsql += "    end\n";
			}
			tsql += "    IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].["
					+ tableName + "]') AND type in (N'U'))\n";
			tsql += "    begin\n";
			tsql += "    delete from tblLanguage where id in (select f.languageId from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '"
					+ tableName + "') \n";
			tsql += "    delete from tblLanguage where id in (select f.groupName from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '"
					+ tableName + "')\n";
			tsql += "    delete from tblLanguage where id in (select t.languageId from tblDBTableInfo t where t.tableName = '"
					+ tableName + "') \n";
			tsql += "    exec proc_deleteExistsTable @tableName='" + tableName
					+ "'\n";
			tsql += "    drop table " + tableName + "\n";
			tsql += "    end\n";

			tsql += getTableSql(tableName);
			DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(tableName);
			for (DBTableInfoBean ctableInfo : (ArrayList<DBTableInfoBean>) childTableList) {
				tsql += "\r\n" + getTableSql(ctableInfo.getTableName());
			}
			// 系统模块
			tsql += "\r\n --"
					+ BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss) + ":新建模块\n";
			tsql += "\r\n delete from tblLanguage where id in (select modelName from tblModules where tblName='"
					+ tableName + "')";
			tsql += "\r\n delete tblModelOperations where f_ref in (select id from tblModules where tblName='"
					+ tableName + "')";
			tsql += "\r\n delete tblModules where  tblName='" + tableName + "'";

			String sql = " select  classCode  from tblModules where tblName='"
					+ tableName + "' ";
			Result rs = sqlList(sql, new ArrayList());
			String pClass = "";
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null
					&& ((ArrayList) rs.retVal).size() > 0) {
				pClass = ((ArrayList<Object[]>) rs.retVal).get(0)[0] + "";
				if (pClass != null && pClass.length() > 0) {
					pClass = pClass.substring(0, pClass.length() - 5);
				}
			}

			tsql += "\r\n declare @newCode varchar(50),@retCode varchar(50),@parentCode varchar(50) set @parentCode ='"
					+ pClass + "' ";
			tsql += "\r\n exec proc_getNewClassCode 'tblModules',@parentCode,@retCode output,@newCode output ";
			String mStr = getTableData(
					"select 'replaceClass' [classCode],[id],[tblName],[modelName],[createBy],[statusId],[linkAddress],[IsUsed],[createTime],"
							+ "[lastUpdateBy],[lastUpdateTime],[OrderBy],[MainModule],[SCompanyID],[SystemParam],[RowON],[IsHidden],"
							+ "[workFlowNodeName],[workFlowNode],[printCount],[isCatalog],[checkPersons],[ICON],[finishTime],[CheckPersont],"
							+ "[isDisplay],[parentClass] from tblModules where tblName='"
							+ tableName + "'", "tblModules");
			mStr = mStr.replaceFirst("'replaceClass'", "@newCode");
			tsql += mStr;
			tsql += getTableData(
					"select id,zh_CN from tblLanguage where id in (select modelName from tblModules where tblName='"
							+ tableName + "')", "tblLanguage");
			tsql += getTableData(
					"select [f_ref],[OperationID],[SCompanyID],[classCode],[RowON],[workFlowNodeName],[workFlowNode],[moduleOpId] "
							+ "from tblModelOperations where  f_ref in (select id from tblModules where tblName='"
							+ tableName + "')", "tblModelOperations");
			tsql += "\r\n update tblModelOperations set moduleOpId=id where f_ref in (select id from tblModules where tblName='"
					+ tableName + "') ";
			// 报表
			tsql += "\r\n --"
					+ BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss) + ":新建报表\n";
			tsql += "\r\n delete tblReportsDet where f_ref in (select id from tblReports where reportNumber='"
					+ tableName + "')";
			tsql += "\r\n delete from tblLanguage where id=(select ReportName from tblReports where reportNumber='"
					+ tableName + "')";
			tsql += "\r\n delete from tblReports where  reportNumber='"
					+ tableName + "'";
			tsql += getTableData(
					"select id,classCode,ReportName,SQLFileName,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,ReportNumber,"
							+ "newFlag,ReportType,BillTable,ProcName,SCompanyID,RowON,workFlowNodeName,workFlowNode,pageSize,printCount,showTotalPage,"
							+ "showTotalStat,auditPrint,colTitleSort,fixListTitle,mainModule,checkPersons,defaultNoshow,fixNumberCol,endClassNumber,listType,"
							+ "showCondition,showDet,crossColNum,moduleType,showHead,showRowNumber,finishTime,CheckPersont,parentLinkReport,extendsBut "
							+ "from tblReports where reportNumber='"
							+ tableName + "'", "tblReports");

			tsql += getTableData(
					"select id,zh_CN from tblLanguage where id in (select ReportName from tblReports where reportNumber='"
							+ tableName + "')", "tblLanguage");
			tsql += getTableData(
					"select [id],[f_ref],[FormatFileName],[newFlag],[FormatName],[SCompanyID],[classCode],[RowON],[workFlowNodeName],[workFlowNode],[languageType],"
							+ "[userIds],[deptIds],[filterSQL] from tblReportsDet where f_ref in (select id from tblReports where reportNumber='"
							+ tableName + "')", "tblReportsDet");

			// 报表
			tsql += "\r\n --"
					+ BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss) + ":新建打印报表\n";
			tsql += "\r\n delete tblReportsDet where f_ref in (select id from tblReports where reportType='BILL' and billTable='"
					+ tableName + "')";
			tsql += "\r\n delete from tblLanguage where id in(select ReportName from tblReports where reportType='BILL' and  billTable='"
					+ tableName + "')";
			tsql += "\r\n delete from tblReports where reportType='BILL' and  billTable='"
					+ tableName + "'";
			tsql += getTableData(
					"select id,classCode,ReportName,SQLFileName,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,ReportNumber,"
							+ "newFlag,ReportType,BillTable,ProcName,SCompanyID,RowON,workFlowNodeName,workFlowNode,pageSize,printCount,showTotalPage,"
							+ "showTotalStat,auditPrint,colTitleSort,fixListTitle,mainModule,checkPersons,defaultNoshow,fixNumberCol,endClassNumber,listType,"
							+ "showCondition,showDet,crossColNum,moduleType,showHead,showRowNumber,finishTime,CheckPersont,parentLinkReport,extendsBut "
							+ "from tblReports where reportType='BILL' and billTable='"
							+ tableName + "'", "tblReports");

			tsql += getTableData(
					"select id,zh_CN from tblLanguage where id in (select ReportName from tblReports where  reportType='BILL' and  billTable='"
							+ tableName + "')", "tblLanguage");
			tsql += getTableData(
					"select [id],[f_ref],[FormatFileName],[newFlag],[FormatName],[SCompanyID],[classCode],[RowON],[workFlowNodeName],[workFlowNode],[languageType],"
							+ "[userIds],[deptIds],[filterSQL] from tblReportsDet where f_ref in (select id from tblReports where  reportType='BILL' and  billTable='"
							+ tableName + "')", "tblReportsDet");

			tsql += "\r\n --"
					+ BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss) + ":新建工作流\n";

			tsql += "\r\n delete from OAWorkFlowNodeApprover where flowNodeId in (select id from OAWorkFlowNode where  flowId in (select id from OAWorkFlowTemplate where templateFile='"
					+ tableName + "')) ";
			tsql += "\r\n delete from OAWorkFlowNodeConditionDet where conditionId in (select id from OAWorkFlowNodeCondition  where flowNodeId in (select id from OAWorkFlowNode where  flowId in (select id from OAWorkFlowTemplate where templateFile='"
					+ tableName + "'))) ";
			tsql += "\r\n delete from OAWorkFlowNodeCondition where flowNodeId in (select id from OAWorkFlowNode where  flowId in (select id from OAWorkFlowTemplate where templateFile='"
					+ tableName + "')) ";
			tsql += "\r\n delete from OAWorkFlowNodeField where flowNodeId in (select id from OAWorkFlowNode where  flowId in (select id from OAWorkFlowTemplate where templateFile='"
					+ tableName + "')) ";
			tsql += "\r\n delete from OAWorkFlowNode where  flowId in (select id from OAWorkFlowTemplate where templateFile='"
					+ tableName + "') ";
			tsql += "\r\n delete OAWorkFlowTemplate  where  templateFile='"
					+ tableName + "'";
			tsql += getTableData(
					"select [id],[templateName],[templateType],[templateClass],[templateFile],[workFlowFile],[allowVisitor],"
							+ "[templateStatus],[wakeUp],[finishTime],[flowOrder],[readLimit],[detail],[affix],[statusId],[fileFinish],"
							+ "[depMonitorScope],[perMonitorScope],[depMonitor],[perMonitor],[titleTemp],[nextWake],[startWake],[allWake],"
							+ "[setWake],[setWakeDept],[setWakePer],[setWakeGroup],[stopStartWake],[stopSAllWake],[stopSetWake],[stopSetWakeDept],"
							+ "[stopSetWakePer],[stopSetWakeGroup],[autoPass],[applyLookFieldDisplay],[usefulLifeS],[usefulLifeE],[allowAdd],"
							+ "[retCheckPerRule],[retdefineName],[defineName],[timeType],[timeVal],[fileContent],[version],[sameFlow],"
							+ "[createTime],[updateBy],[dispenseWake],[reviewWake],[designType],[lines],[defStatus],[overTimeWake],"
							+ "[parentTableName],[wakeLimitSQL] from OAWorkFlowTemplate where  templateFile='"
							+ tableName + "'", "OAWorkFlowTemplate");

			tsql += getTableData(
					"select [id],[keyId],[flowId],[zAction],[display],[to],[allowBack],[allowStop],[allowJump],[allowCancel],"
							+ "[useAllApprove],[ideaRequired],[forwardTime],[timeLimit],[noteTime],[noteRate],[noteTimeUnit],[noteRateUnit],"
							+ "[timeLimitUnit],[limitMinute],[rateMinute],[awokeMinute],[passExec],[backExec],[stopExec],[filterSet],"
							+ "[autoSelectPeople],[approvePeople],[fieldId],[approversId],[conditionsId],[standaloneNoteSet],[nextSMS],"
							+ "[nextMobile],[nextMail],[startSMS],[startMobile],[startMail],[allSMS],[allMobile],[allMail],[setSMS],[setMobile],"
							+ "[setMail],[filterSetSQL],[autoSelectPeopleSQL] from OAWorkFlowNode where flowId in (select id from OAWorkFlowTemplate where  templateFile='"
							+ tableName + "')", "OAWorkFlowNode");
			tsql += getTableData(
					"select [id],[flowNodeId],[userName],[user],[typeName],[type],[checkType] from OAWorkFlowNodeApprover  where flowNodeId in (select id from OAWorkFlowNode where  flowId in "
							+ "(select id from OAWorkFlowTemplate where  templateFile='"
							+ tableName + "')) ", "OAWorkFlowNodeApprover");
			tsql += getTableData(
					"select [id],[flowNodeId],[to],[display],[toCode] from OAWorkFlowNodeCondition  where flowNodeId in (select id from OAWorkFlowNode where  flowId in "
							+ "(select id from OAWorkFlowTemplate where  templateFile='"
							+ tableName + "')) ", "OAWorkFlowNodeCondition");
			tsql += getTableData(
					"select [id],[conditionId],[fieldName],[display],[andOrDisplay],[andOr],[valueDisplay],[value],[relationDisplay],[relation],[groupId],[groupType] from OAWorkFlowNodeConditionDet   "
							+ "where conditionId in (select id from OAWorkFlowNodeCondition  where flowNodeId in (select id from OAWorkFlowNode where  flowId in (select id from OAWorkFlowTemplate where templateStatus=1 and templateFile='"
							+ tableName + "')))  ",
					"OAWorkFlowNodeConditionDet");
			tsql += getTableData(
					"select [id],[flowNodeId],[fieldName],[inputType],[isNotNull],[fieldType] from OAWorkFlowNodeField  where flowNodeId in (select id from OAWorkFlowNode where  flowId in "
							+ "(select id from OAWorkFlowTemplate where  templateFile='"
							+ tableName + "')) ", "OAWorkFlowNodeField");
			compressFile(tsql, tableName + ".sql", out); // 压缩表结构文件,主表，明细表结构，单据编号数据，枚举，系统模块，报表，工作流

			String info = "tableName=" + tableName + "\r\n";

			// 取所在表自定义文件
			String fieldCalculate = tableInfo.getFieldCalculate();
			Pattern pattern = Pattern.compile("id=\"([^\"]*)\"",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(fieldCalculate);
			HashMap<String, String> dmap = new HashMap<String, String>();
			while (matcher.find()) {
				String t = matcher.group(1);
				DefineSQLBean sqlBean = (DefineSQLBean) BaseEnv.defineSqlMap
						.get(t);
				if (sqlBean != null && sqlBean.path.indexOf("user_config") > -1) {
					dmap.put(sqlBean.path, sqlBean.path);
				}
			}

			for (String f : dmap.keySet()) {
				File comFile = new File(f);
				compressFile(comFile, out);
				info += "define=" + comFile.getName() + "\r\n";
			}
			// 取弹出窗
			for (DBFieldInfoBean dfb : tableInfo.getFieldInfos()) {
				if (dfb.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE
						&& dfb.getInputValue() != null
						&& !dfb.getInputValue().equals("")) {
					PopupSelectBean bean = BaseEnv.popupSelectMap.get(dfb
							.getInputValue());
					if (bean != null
							&& bean.getPath().indexOf("user_config") > -1) {
						File comFile = new File(bean.getPath());
						compressFile(comFile, out);
						info += "popup=" + comFile.getName() + "\r\n";
					}
				}
			}
			childTableList = DDLOperation.getChildTables(tableName,
					BaseEnv.tableInfos);
			for (DBTableInfoBean ctableInfo : (ArrayList<DBTableInfoBean>) childTableList) {
				for (DBFieldInfoBean dfb : ctableInfo.getFieldInfos()) {
					if (dfb.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE
							&& dfb.getInputValue() != null
							&& !dfb.getInputValue().equals("")) {
						PopupSelectBean bean = BaseEnv.popupSelectMap.get(dfb
								.getInputValue());
						if (bean != null
								&& bean.getPath().indexOf("user_config") > -1) {
							File comFile = new File(bean.getPath());
							compressFile(comFile, out);
							info += "popup=" + comFile.getName() + "\r\n";
						}
					}
				}
			}
			// 取报表文件
			sql = "select SQLFileName from tblReports where ReportNumber='"
					+ tableName + "' or (reportType='BILL' and billTable='"
					+ tableName + "') ";
			rs = sqlList(sql, new ArrayList());
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null) {
				ArrayList<Object[]> al = ((ArrayList<Object[]>) rs.retVal);
				for (Object[] xml : al) {
					File f = new File(BaseEnv.FILESERVERPATH
							+ "/../user_report_v7/" + xml[0]);
					if (f.exists())
						compressFile(f, out);
					info += "report=" + f.getName() + "\r\n";
				}
			}
			sql = "select FormatFileName from tblReportsDet where f_ref in (select id  from tblReports where ReportNumber='"
					+ tableName
					+ "'  or (reportType='BILL' and billTable='"
					+ tableName + "') )";
			rs = sqlList(sql, new ArrayList());
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null) {
				ArrayList<Object[]> al = ((ArrayList<Object[]>) rs.retVal);
				ArrayList db = new ArrayList();
				for (Object[] xml : al) {
					if (db.contains(xml[0])) {
						continue;
					}
					db.add(xml[0]);
					File f = new File(BaseEnv.FILESERVERPATH + "/userReport/"
							+ xml[0]);
					if (f.exists()) {
						compressFile(f, out);
						info += "reportFormat=" + f.getName() + "\r\n";
					}
				}
			}
			compressFile(info, "info.txt", out);
			out.close();
			fileOutputStream.close();
			return fileName;
		} catch (Exception e) {
			BaseEnv.log.error("CustomizeMgt.export ", e);
		}
		return null;
	}

	public String getTableSql(String tableName) {
		try {
			StringBuffer ret = new StringBuffer();

			ret.append("create table " + tableName + " ( ");
			DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(tableName);

			for (DBFieldInfoBean dfb : tableInfo.getFieldInfos()) {
				if (dfb.getFieldName().equals("id")) {
					if (dfb.getFieldType() == DBFieldInfoBean.FIELD_INT) {
						ret.append("id bigint not null IDENTITY (1, 1), ");
					} else {
						ret.append("id varchar(30) not null, ");
					}
				} else if (dfb.getFieldName().equals("detOrderNo")) {
					ret.append("detOrderNo bigint not null IDENTITY (1, 1), ");
				} else {
					String fieldType = DDLOperation.getTypeStr(dfb);
					String fieldNull = dfb.getIsNull() == DBFieldInfoBean.IS_NULL ? "null"
							: "not null";
					ret.append(dfb.getFieldName() + " " + fieldType + " "
							+ fieldNull + ",");
				}
			}
			ret.setLength(ret.length() - 1); // 去掉最后的,号
			ret.append(")\n");

			if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE) {
				ret.append("CREATE CLUSTERED INDEX Inx_"
						+ tableInfo.getTableName() + "_f_ref ON "
						+ tableInfo.getTableName() + "(f_ref,id) \n");
			} else if (tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE) {
				ret.append("CREATE CLUSTERED INDEX Inx_"
						+ tableInfo.getTableName() + "_f_ref ON "
						+ tableInfo.getTableName() + "(f_brother,id) \n");
			}
			ret.append(" alter   table   " + tableInfo.getTableName()
					+ " add   constraint   PK_" + tableInfo.getTableName()
					+ "  primary   key   (id)\n");
			String contranst = "";
			if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE
					&& tableInfo.getPerantTableName() != null) {
				String parentTableName = tableInfo.getPerantTableName();
				// 生成约束名称
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
				String[] parentTableNames = parentTableName.split(";");
				if (parentTableNames.length == 1) {
					for (int i = 0; i < parentTableNames.length; i++) {
						String fk = "FK_f_ref_id" + sdf.format(new Date());
						ret.append("alter table " + tableInfo.getTableName()
								+ " add constraint " + fk
								+ " foreign key (f_ref) references "
								+ parentTableNames[i]
								+ " ON DELETE CASCADE ON UPDATE CASCADE \n");
					}
				}
			} else if (tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE
					&& tableInfo.getPerantTableName() != null) {
				String parentTableName = tableInfo.getPerantTableName();
				// 生成约束名称
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
				String[] parentTableNames = parentTableName.split(";");
				if (parentTableNames.length == 1) {
					for (int i = 0; i < parentTableNames.length; i++) {
						String fk = "FK_f_ref_id" + sdf.format(new Date());
						ret.append("alter table " + tableInfo.getTableName()
								+ " add constraint " + fk
								+ " foreign key (f_brother) references "
								+ parentTableNames[i]
								+ " ON DELETE CASCADE ON UPDATE CASCADE \n");
					}
				}
			}
			String fs = "";
			String vs = "";
			Field[] fd = tableInfo.getClass().getDeclaredFields();
			for (Field f : fd) {
				if (f.getAnnotations() != null && f.getAnnotations().length > 0) {
					if (f.getAnnotations()[0].toString().indexOf("OneToMany") > 0
							|| f.getAnnotations()[0].toString().indexOf(
									"Transient") > 0) {
						continue;
					}
				}
				if (!Modifier.isFinal(f.getModifiers())) {

					String mname = "get"
							+ f.getName().substring(0, 1).toUpperCase()
							+ f.getName().substring(1);
					Method method = tableInfo.getClass().getMethod(mname);
					if (mname != null) {
						fs += "," + f.getName();
						Object vo = method.invoke(tableInfo);
						String vv = vo == null ? null : vo.toString();
						vs += vv == null ? ",null" : ",'" + vv + "'";
					}
				}
			}
			fs = fs.substring(1);
			vs = vs.substring(1);
			ret.append("insert into tblDBTableInfo (" + fs + ")\n");
			ret.append("values(" + vs + ")\n");

			// insert into tblDBFieldInfo
			// (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode,printCount,logicValidate,languageId,popupType,checkPersons,groupName,insertTable,isCopy,copyType,isReAudit,statusId,isLog)
			// values('53584126_1403251435335460032','id','2','','0','1','0','30','','0','53584126_1403251435335460010','','','0','0','0','','','0','0','','','','0','','','','','','','0','','0','0','0')
			fd = DBFieldInfoBean.class.getDeclaredFields();
			for (DBFieldInfoBean fieldBean : tableInfo.getFieldInfos()) {
				fs = "";
				vs = "";
				for (Field f : fd) {
					if (f.getAnnotations() != null
							&& f.getAnnotations().length > 0) {
						if (f.getAnnotations()[0].toString().indexOf(
								"OneToMany") > 0
								|| f.getAnnotations()[0].toString().indexOf(
										"Transient") > 0) {
							continue;
						}
					}
					if (!Modifier.isFinal(f.getModifiers())) {

						String mname = "get"
								+ f.getName().substring(0, 1).toUpperCase()
								+ f.getName().substring(1);
						Method method = fieldBean.getClass().getMethod(mname);
						if (mname != null) {

							Object vo = method.invoke(fieldBean);
							if (vo instanceof DBTableInfoBean) {
								fs += ",tableId";
								vs += ",'" + ((DBTableInfoBean) vo).getId()
										+ "'";
							} else {
								fs += "," + f.getName();
								String vv = vo == null ? null : vo.toString();
								vs += vv == null ? ",null" : ",'" + vv + "'";
							}
						}
					}
				}
				fs = fs.substring(1);
				vs = vs.substring(1);
				ret.append("insert into tblDBFieldInfo (" + fs + ")\n");
				ret.append("values(" + vs + ")\n");
			}
			if (tableInfo.getDisplay() != null
					&& tableInfo.getDisplay().getId() != null
					&& tableInfo.getDisplay().getId().length() > 0) {
				ret.append(tableInfo.getDisplay().toAddSQL() + "\n");
			}
			for (DBFieldInfoBean fieldBean : tableInfo.getFieldInfos()) {
				if (fieldBean.getDisplay() != null
						&& fieldBean.getDisplay().getId() != null
						&& fieldBean.getDisplay().getId().length() > 0) {
					ret.append(fieldBean.getDisplay().toAddSQL() + "\n");
				}
			}
			for (DBFieldInfoBean fieldBean : tableInfo.getFieldInfos()) {
				if ("BillNo".equals(fieldBean.getFieldIdentityStr())) {
					ret.append("--"
							+ BaseDateFormat.format(new Date(),
									BaseDateFormat.yyyyMMddHHmmss) + "修改单据编号");
					ret.append("\r\nDELETE FROM tblBillNo WHERE [key]='"
							+ tableInfo.getTableName() + "_"
							+ fieldBean.getFieldName() + "'");
					String sql = " select [key],[billName],[pattern],[billNO],[start],[step],[isfillback],[reset],[laststamp],[explain],[isAddbeform],[statusId],[isDefaultLoginPerson] from tblBillNo where [key]='"
							+ tableInfo.getTableName()
							+ "_"
							+ fieldBean.getFieldName() + "' ";
					ret.append(getTableData(sql, "tblBillNo"));
				}
				// 枚举
				if (fieldBean.getInputTypeOld() == DBFieldInfoBean.INPUT_ENUMERATE
						&& fieldBean.getRefEnumerationName() != null
						&& !fieldBean.getRefEnumerationName().equals("")) {
					ret.append("\r\n--"
							+ BaseDateFormat.format(new Date(),
									BaseDateFormat.yyyyMMddHHmmss) + "修改枚举");
					ret.append("\r\ndelete from tblLanguage where id in (select languageId from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='"
							+ fieldBean.getRefEnumerationName() + "'))");
					ret.append("\r\ndelete from tblLanguage where id in (select languageId from tblDBEnumeration where enumName='"
							+ fieldBean.getRefEnumerationName() + "')");
					ret.append("\r\ndelete from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='"
							+ fieldBean.getRefEnumerationName() + "');");
					ret.append("\r\ndelete from tblDBEnumeration where enumName='"
							+ fieldBean.getRefEnumerationName() + "';");

					String sql = " select id,enumName,createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,classCode,RowON,workFlowNodeName,workFlowNode,printCount,languageId,mainModule,checkPersons "
							+ "from tblDBEnumeration where enumName='"
							+ fieldBean.getRefEnumerationName() + "' ";
					ret.append(getTableData(sql, "tblDBEnumeration"));

					sql = " select id,enumValue,enumId,SCompanyID,classCode,RowON,workFlowNodeName,workFlowNode,printCount,languageId,checkPersons,enumOrder from tblDBEnumerationItem where enumId in (select id from tblDBEnumeration where enumName='"
							+ fieldBean.getRefEnumerationName() + "') ";
					ret.append(getTableData(sql, "tblDBEnumerationItem"));

					sql = "select id,zh_CN from tblLanguage where id in (select languageId from tblDBEnumeration where enumName='"
							+ fieldBean.getRefEnumerationName()
							+ "') or id in (select languageId from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='"
							+ fieldBean.getRefEnumerationName() + "')) ";
					ret.append(getTableData(sql, "tblLanguage"));
				}

			}

			return ret.toString();
		} catch (Exception e) {
			return "脚本生成报错" + e;
		}
	}

	public static Result execTableInfo(final String sql) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List<String[]> list = new ArrayList<String[]>();
							PreparedStatement cs = conn.prepareStatement(sql);
							cs.executeUpdate();
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return 0;
			}
		});
		rs.retCode = retCode;
		return rs;
	}

	/***************************************************************************
	 * 赵健 根据表名查询该表是否允许共享
	 */
	public Result getTableInfo(String tableNam) {
		final String sql = " from DBTableInfoBean where tableName=?";
		ArrayList param = new ArrayList();
		param.add(tableNam);
		return list(sql, param);
	}

	public Result getChildInfo(final String tableName, final String locale) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List<String[]> list = new ArrayList<String[]>();

							String sql = " select a.id,tableName,"
									+ locale
									+ " from tblDBTableInfo a join tblLanguage b on a.languageId=b.id  where tableType=1 and perantTableName like ?";
							PreparedStatement cs = conn.prepareStatement(sql);
							cs.setString(1, "%" + tableName + ";%");
							ResultSet rss = cs.executeQuery();
							while (rss.next()) {
								list.add(new String[] { rss.getString(1),
										rss.getString(2), rss.getString(3) });
							}
							rs.setRetVal(list);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return 0;
			}
		});
		return rs;
	}

	public Result getReportId(String tableName, String reportType) {
		String sql = "";
		if ("BILL".equals(reportType)) {
			sql = " select id from tblReports where BillTable=? and ReportType='BILL' ";
		} else {
			sql = " select id from tblReports where reportNumber=? and ReportType='TABLELIST' ";
		}
		ArrayList param = new ArrayList();
		param.add(tableName);
		return sqlList(sql, param);
	}

	/**
	 * 取单据打印报表
	 * 
	 * @param tableName
	 * @param locale
	 * @return
	 */
	public Result getBillSQL(final String tableName, final String locale,
			final String userId, final String reportType, final String path) {
		final Result rs = new Result();
		final HashMap addMap = new HashMap();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List<String[]> list = new ArrayList<String[]>();
							String sql = "";
							if ("BILL".equals(reportType)) {
								sql = "select top 1 id,SQLFileName,ReportNumber,newFlag from tblReports where BillTable=? and ReportType='BILL' ";
							} else {
								sql = "select top 1 id,SQLFileName,ReportNumber,newFlag  from tblReports where reportNumber=? and ReportType='TABLELIST' ";
							}

							PreparedStatement cs = conn.prepareStatement(sql);
							cs.setString(1, tableName);
							ResultSet rss = cs.executeQuery();
							if (rss.next()) {
								rs.setRetVal(new String[] { rss.getString(1),
										rss.getString(2), rss.getString(3),
										rss.getString(4) });
							} else {
								String id = IDGenerater.getId();
								String languageId = IDGenerater.getId();
								String SQLFileName = "";
								String ReportNumber = "";
								String BillTable = "";
								if ("BILL".equals(reportType)) {
									SQLFileName = "Bill" + tableName
											+ "SQL.xml";
									ReportNumber = "Bill" + tableName;
									BillTable = tableName;
								} else {
									SQLFileName = tableName + "SQL.xml";
									ReportNumber = tableName;
									BillTable = "";
								}
								sql = " INSERT INTO [tblReports]([id],[classCode],[ReportName],[SQLFileName],[createBy],[lastUpdateBy],"
										+ "[createTime],[lastUpdateTime],[statusId],[ReportNumber],[newFlag],[ReportType],[BillTable],"
										+ "[ProcName],[SCompanyID],[RowON],[workFlowNodeName],[workFlowNode],[pageSize],[printCount],"
										+ "[showTotalPage],[showTotalStat],[auditPrint],[colTitleSort],[fixListTitle],[mainModule],"
										+ "[checkPersons],[defaultNoshow],[fixNumberCol],[listType],[showCondition],"
										+ "[showDet],[crossColNum],[showHead],[showRowNumber],[finishTime]) "
										+ "values(?,'',?,?,?,?,?,?,0,?,'NEW',?,?,'','00001','','finish','-1',"
										+ "0,0,1,1,0,1,1,1,'',0,0,1,0,1,0,1,1,'')";
								cs = conn.prepareStatement(sql);
								cs.setString(1, id);
								cs.setString(2, languageId);
								cs.setString(3, SQLFileName);
								cs.setString(4, userId);
								cs.setString(5, userId);
								cs.setString(6, BaseDateFormat.format(
										new Date(),
										BaseDateFormat.yyyyMMddHHmmss));
								cs.setString(7, BaseDateFormat.format(
										new Date(),
										BaseDateFormat.yyyyMMddHHmmss));
								cs.setString(8, ReportNumber);
								cs.setString(9, reportType);
								cs.setString(10, BillTable);
								cs.execute();
								sql = " INSERT INTO tblLanguage(id,zh_CN,zh_TW,zh_HK,en) "
										+ "select ?,zh_CN,zh_TW,zh_HK,en from tblLanguage where id=(select languageId from tblDBTableInfo where tableName=?)";
								cs = conn.prepareStatement(sql);
								cs.setString(1, languageId);
								cs.setString(2, tableName);
								int i = cs.executeUpdate();
								addMap.put("isAdd", id);
								rs.setRetVal(new String[] { id, SQLFileName,
										ReportNumber, "NEW" });
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("CustomizeMgt.getBillSQL", ex);
							return;
						}
					}
				});
				return 0;
			}
		});
		if (addMap.get("isAdd") != null && !addMap.get("isAdd").equals("")) {
			ConvertToSql.saveReports(path, addMap.get("isAdd") + "", 1);
		}
		return rs;
	}

	public Result getMenu(final String tableName, final String locale,
			final String userId, final String path) {
		final Result rs = new Result();
		final HashMap addMap = new HashMap();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List<String[]> list = new ArrayList<String[]>();
							String sql = "";
							sql = "select top 1 id from tblModules where linkAddress like ? or linkAddress like ? ";
							PreparedStatement cs = conn.prepareStatement(sql);
							cs.setString(1, "%tableName=" + tableName + "");
							cs.setString(2, "%tableName=" + tableName + "&%");
							ResultSet rss = cs.executeQuery();
							if (rss.next()) {
								rs.setRetVal(rss.getString(1));
							} else {
								String id = IDGenerater.getId();
								String languageId = IDGenerater.getId();
								sql = "INSERT INTO [tblModules]([id],[classCode],[tblName],[modelName],[createBy],[statusId],"
										+ "[linkAddress],[IsUsed],[createTime],[lastUpdateBy],[lastUpdateTime],[OrderBy],"
										+ "[MainModule],[SCompanyID],[SystemParam],[IsHidden],[workFlowNodeName],"
										+ "[workFlowNode],[printCount],[isCatalog],[checkPersons],[finishTime],"
										+ "[CheckPersont],[isDisplay]) values("
										+ "?,'',?,?,'1',0,?,1,?,'1',?,1,'1','00001','Normal','2','draft','0',0,0,'',?,'',0)";
								cs = conn.prepareStatement(sql);
								cs.setString(1, id);
								cs.setString(2, tableName);
								cs.setString(3, languageId);
								cs.setString(4,
										"/UserFunctionQueryAction.do?tableName="
												+ tableName);
								cs.setString(5, BaseDateFormat.format(
										new Date(),
										BaseDateFormat.yyyyMMddHHmmss));
								cs.setString(6, BaseDateFormat.format(
										new Date(),
										BaseDateFormat.yyyyMMddHHmmss));
								cs.setString(7, BaseDateFormat.format(
										new Date(),
										BaseDateFormat.yyyyMMddHHmmss));
								cs.execute();

								sql = "INSERT INTO [tblModelOperations]([f_ref],[OperationID],[SCompanyID],moduleOpId) values(?,?,?,0)";
								cs = conn.prepareStatement(sql);
								for (int i : new int[] { 1, 2, 3, 4, 6 }) {
									cs.setString(1, id);
									cs.setString(2, i + "");
									cs.setString(3, "00001");
									cs.addBatch();
								}
								cs.executeBatch();
								sql = " INSERT INTO tblLanguage(id,zh_CN,zh_TW,zh_HK,en) "
										+ "select ?,zh_CN,zh_TW,zh_HK,en from tblLanguage where id=(select languageId from tblDBTableInfo where tableName=?)";
								cs = conn.prepareStatement(sql);
								cs.setString(1, languageId);
								cs.setString(2, tableName);
								int i = cs.executeUpdate();
								addMap.put("isAdd", id);
								rs.setRetVal(id);
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("CustomizeMgt.getBillSQL", ex);
							return;
						}
					}
				});
				return 0;
			}
		});
		if (addMap.get("isAdd") != null && !addMap.get("isAdd").equals("")) {
			// ConvertToSql.saveReports(path,addMap.get("isAdd")+"",1);
		}
		return rs;
	}

	public Result getBrotherInfo(final String tableName, final String locale) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List<String[]> list = new ArrayList<String[]>();

							String sql = " select a.id,tableName,"
									+ locale
									+ " from tblDBTableInfo a join tblLanguage b on a.languageId=b.id  where tableType=2 and perantTableName like ?";
							PreparedStatement cs = conn.prepareStatement(sql);
							cs.setString(1, "%" + tableName + ";%");
							ResultSet rss = cs.executeQuery();
							while (rss.next()) {
								list.add(new String[] { rss.getString(1),
										rss.getString(2), rss.getString(3) });
							}
							rs.setRetVal(list);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return 0;
			}
		});
		return rs;
	}

	public Result setBrother(final String tableName, final String brother) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String[] bros = brother.split(";");
							for (String bro : bros) {
								if (bro.length() > 0) {
									String sql = " select perantTableName from tblDBTableInfo where tableName=?";
									PreparedStatement cs = conn
											.prepareStatement(sql);
									cs.setString(1, bro);
									ResultSet rss = cs.executeQuery();
									if (rss.next()) {
										String ptn = rss.getString(1);
										if (ptn.toLowerCase().startsWith(
												tableName.toLowerCase() + ";")
												|| ptn.toLowerCase()
														.indexOf(
																";"
																		+ tableName
																				.toLowerCase()
																		+ ";") > -1) {

										} else {
											ptn = ptn == null ? "" : ptn;
											ptn += tableName + ";";
											sql = "update tblDBTableInfo set perantTableName=?  where tableName=?";
											cs = conn.prepareStatement(sql);
											cs.setString(1, ptn);
											cs.setString(2, bro);
											cs.execute();

											BaseEnv.tableInfos.get(bro)
													.setPerantTableName(ptn);
										}
									}
								}
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return 0;
			}
		});
		return rs;
	}

	// 查询某表中的所有字段
	public Result queryFieldsByTableName(final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List<String> fields = new ArrayList<String>();
							Statement cs = conn.createStatement();
							String sql = "select NAME from syscolumns where id = object_id('"
									+ tableName + "')";
							ResultSet rss = cs.executeQuery(sql);
							while (rss.next()) {
								String fieldName = rss.getString("NAME");
								if (!fieldName.equalsIgnoreCase("detOrderNo")) {
									fields.add(fieldName);
								}
							}
							rs.setRetVal(fields);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return 0;
			}
		});
		return rs;
	}

	public Result queryFieldsValue(final String sql, final ArrayList fieldsName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List<Object> values = new ArrayList<Object>();
							Statement cs = conn.createStatement();
							ResultSet rss = cs.executeQuery(sql);
							while (rss.next()) {
								for (int i = 0; i < fieldsName.size(); i++) {
									Object value = rss.getObject(fieldsName
											.get(i).toString());
									value = value == null ? "" : value;
									values.add(value);
								}

							}
							rs.setRetVal(values);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return 0;
			}
		});
		return rs;
	}

	public static Result updateTemplateFile(final String oAWorkFlowId,
			final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {

							String sql = "update OAWorkFlowTemplate set templateFile = '"
									+ tableName
									+ "',templateType = 1 where id = '"
									+ oAWorkFlowId + "'";
							PreparedStatement cs = conn.prepareStatement(sql);
							cs.executeUpdate();
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);

						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return 0;
			}
		});
		rs.retCode = retCode;
		return rs;
	}

	/***
	 * 复制旧表表单样式
	 * 
	 * @param oldtableName
	 * @param tableName
	 */
	public static Result updateLayoutHTML(final String oldtableName,
			final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							
							DBTableInfoBean bean = BaseEnv.tableInfos.get(oldtableName);
							String html = bean == null ? "" : bean.getLayoutHTML();
							if(html != null){
								
								String sql = "update tbldbtableinfo set layoutHTML = '"
										+ html
										+ "' where tableName = '"
										+ tableName + "'";
								
								PreparedStatement cs = conn.prepareStatement(sql);
								
								int res = cs.executeUpdate();
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								System.out.println(res);
							}

						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return 0;
			}
		});
		rs.retCode = retCode;
		return rs;
	}

}
