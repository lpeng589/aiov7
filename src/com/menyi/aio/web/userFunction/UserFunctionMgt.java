package com.menyi.aio.web.userFunction;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.aioshop.bean.SpOrder;
import com.koron.aioshop.bean.SpOrderdet;
import com.koron.aioshop.bean.SpUser;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.SpGoods;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.PublicMgt;

/**
 * 
 * <p>
 * Title:此类主要负责自定类的业务逻辑操作
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2010-7-5
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class UserFunctionMgt extends AIODBManager {

	DynDBManager dbmgt = new DynDBManager();
	PublicMgt publicMgt = new PublicMgt();
	public static final int CAN_CHECK = 1;
	public static final int CAN_NOT_CHECK = 0;
	public static final int NO_CHECK = 0; // 正文未审核
	public static final int CHECKING = 1; // 审核中
	public static final int CHECKED = 2; // 已审核
	public static final int DRAFT = 3; // 草稿
	// 新增模块成功后，存放状态更改的脚本
	public static String strStatu = "";

	public Result getDataId(String tableName, String fieldName, String keyId) {
		String sql = " select id from  " + tableName + " where " + fieldName
				+ "=?";
		ArrayList param = new ArrayList();
		param.add(keyId);
		Result rs = this.sqlList(sql, param);
		return rs;
	}

	/**
	 * 在插入数据之前的一些基本信息的初始化
	 * 
	 * @param tableInfo
	 *            表结构信息
	 * @param lg
	 *            登录信息
	 * @param values
	 *            要保存的数据
	 * @param parentCode
	 *            父类classCode
	 * @param workFlow
	 *            表工作流信息
	 * @param uploadpic
	 *            上传的图片
	 * @param uploadaffix
	 *            上传的附件
	 * @return
	 */
	public Result initDBFieldInfo(DBTableInfoBean tableInfo, LoginBean lg,
			HashMap values, String parentCode, OAWorkFlowTemplate workFlow) {

		Result rs = new Result();
		/* 从LoginBean中取出分支机构的classCode */
		String sunClassCode = lg.getSunCmpClassCode();
		/* 取出分支机构是否启用 */
		boolean openSunCompany = Boolean.parseBoolean(BaseEnv.systemSet.get(
				"sunCompany").getSetting());
		/* 当是职员表或部门表时，从页面取分支机构值，而不取当前登陆的分支机构值 */
		boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo
				.getTableName())
				|| "tblDepartment".equalsIgnoreCase(tableInfo.getTableName()) ? true
				: false;

		Date time = null;
		String tableName = tableInfo.getTableName();
		PublicMgt pcmgt = new PublicMgt();

		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {

			DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos()
					.get(i);
			/* 如果启用分支机构,并且不是职员表或部门表时 */
			if ((openSunCompany && !isEmpOrDept) || !openSunCompany) {
				/* 将classCode存入到表的SCompanyI中 */
				if (fi.getFieldName().equals("SCompanyID")) {
					values.put("SCompanyID", sunClassCode);
				}
			}

			// 加入创建人信息
			if (fi.getFieldName().equals("createBy")
					&& (values.get("createBy") == null || values
							.get("createBy").equals(""))) {
				values.put("createBy", lg.getId());
			} else if (fi.getFieldName().equals("createTime")
					&& (values.get("createTime") == null || values.get(
							"createTime").equals(""))) {
				values.put("createTime", BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss));
			} else if (fi.getFieldName().equals("workFlowNodeName")) {
				if (workFlow != null && workFlow.getTemplateStatus() == 1) {
					values.put("workFlowNodeName", "notApprove");
					values.put("workFlowNode", "0");
					values.put("checkPersons", ";" + lg.getId() + ";");
					values.put("finishTime", "");
				} else {
					values.put("workFlowNodeName", "finish");
					values.put("workFlowNode", "-1");
					values.put("checkPersons", "");
					values.put("finishTime", BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss));
				}
				/*
				 * 会计凭证中workFlowNodeName字段初始化特殊处理(审核流已屏蔽故workFlow==null,
				 * 如果是凭证就强制给它写成初始化数据)
				 */
				if ("tblAccMain".equals(tableName)) {
					values.put("workFlowNodeName", "notApprove");
					values.put("workFlowNode", "0");
					values.put("isAuditing", "start");
				}
			} else if (fi.getFieldName().equals("lastUpdateBy")) {
				values.put("lastUpdateBy", lg.getId());
			} else if (fi.getFieldName().equals("lastUpdateTime")
					&& (values.get("lastUpdateTime") == null || values.get(
							"lastUpdateTime").equals(""))) {
				values.put("lastUpdateTime", BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss));
			} else if (fi.getFieldName().equals("classCode")) {
				if (tableInfo.getClassFlag() != 0) {
					String strClassCode = pcmgt.getLevelCode(tableName,
							parentCode);
					values.put("classCode", strClassCode);
				}
			} else if (fi.getFieldType() == DBFieldInfoBean.FIELD_PIC) {
				String pic = "";
				String[] picArray = null;
				if (values.get(fi.getFieldName()) != null
						&& values.get(fi.getFieldName()).toString().length() > 0) {
					picArray = values.get(fi.getFieldName()).toString()
							.split(";");
				}
				// if(picArray==null){
				// picArray =request==null?null:
				// request.getParameterValues(fi.getFieldName()) ;
				// }
				for (int k = 0; picArray != null && k < picArray.length; k++) {
					if (picArray[k].startsWith("http:")
							|| picArray[k].startsWith("PICPATH:")) {
						// 外链接图片
						pic += picArray[k] + ";";
					} else {
						// 检查文件是否存在
						String upp = picArray[k].replaceAll(
								"_" + fi.getFieldName(), "");// 去掉图片文件中的字段名
						String fn = FileHandler.getRealFileName(tableName,
								FileHandler.TYPE_PIC, upp);
						pic += fn + ";";
						// 拷贝到正式目录
						FileHandler.copy(tableName, FileHandler.TYPE_PIC,
								picArray[k], fn);
					}
				}
				if (pic.endsWith(";")) {
					pic = pic.substring(0, pic.length() - 1);
					values.put(fi.getFieldName(), pic);
				}
			} else if (fi.getFieldType() == DBFieldInfoBean.FIELD_AFFIX) {
				String affix = "";
				String[] affixArray = null;
				if (values.get(fi.getFieldName()) != null
						&& values.get(fi.getFieldName()).toString().length() > 0) {
					affixArray = values.get(fi.getFieldName()).toString()
							.split(";");
				}
				// if(affixArray==null){
				// affixArray = request==null?null:
				// request.getParameterValues(fi.getFieldName()) ;
				// }
				for (int k = 0; affixArray != null && k < affixArray.length; k++) {
					String upp = affixArray[k].replaceAll(
							"_" + fi.getFieldName(), "");// 去掉图片文件中的字段名
					String fn = FileHandler.getRealFileName(tableName,
							FileHandler.TYPE_AFFIX, upp);
					affix += fn + ";";
					// 拷贝到正式目录
					FileHandler.copy(tableName, FileHandler.TYPE_AFFIX,
							affixArray[k], fn);
				}
				if (affix.endsWith(";")) {
					affix = affix.substring(0, affix.length() - 1);
					values.put(fi.getFieldName(), affix);
				}
			} else if ((fi.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE || fi
					.getFieldType() == DBFieldInfoBean.FIELD_INT)
					&& values.get(fi.getFieldName()) == null) {
				values.put(fi.getFieldName(), fi.getDefaultValue());
			}

			String defaultValue = fi.getDefaultValue();
			String type = fi.getFieldIdentityStr();
			if (null != type) {
				if (null != defaultValue && type.equals("accendnotstart")) {
					Object oo = values.get(fi.getFieldName());
					if (null != oo) {
						try {
							time = BaseDateFormat.parse(oo.toString(),
									BaseDateFormat.yyyyMMdd);
						} catch (Exception e) {
							BaseEnv.log
									.error("UserFunctionDao.java initDBFieldInfo() method "
											+ e);
						}
					}
				}
			}
		}
		/* 会计期间月，年 */
		int currentMonth = 0;
		int currentYear = 0;
		if (null != time) {
			currentMonth = time.getMonth() + 1;
			currentYear = time.getYear() + 1900;
		}
		int periodMonth = -1;
		int periodYear = -1;

		String sysParamter = tableInfo.getSysParameter();
		AccPeriodBean accBean = BaseEnv.accPerios.get(lg.getSunCmpClassCode());

		if (accBean != null) {
			periodMonth = accBean.getAccMonth(); // 会计期间月
			periodYear = accBean.getAccYear(); // 会计期间年
		}

		/* 验证录入的数据是否是当前会计的数据 */
		if ((currentYear < periodYear
				|| (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)
				&& currentMonth != 0) {
			if (null != sysParamter) {
				if (sysParamter.equals("CurrentAccBefBill")
						&& (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
					rs.setRetCode(ErrorCanst.CURRENT_ACC_BEFORE_BILL);
					return rs;
				}

				if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart")
						&& (currentYear < periodYear
								|| (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)) {
					rs.setRetCode(ErrorCanst.CURRENT_ACC_BEFORE_BILL);
					return rs;
				}
			}

		}

		// 假如表结构中系统参数是“开账前及会计期间前均不能录入”,并且期间是不输入的，那么根据日期查询期间，并设置到values中
		DBFieldInfoBean periodField = DDLOperation.getFieldInfo(
				BaseEnv.tableInfos, tableName, "Period");
		if ("CurrentAccBefBillAndUnUseBeforeStart".equals(sysParamter)
				&& currentYear > 0 && currentMonth > 0 && periodField != null
				&& periodField.getInputType() == DBFieldInfoBean.INPUT_NO) {
			// 检查单据日期的期间在会计期间中是否存在
			if (null != time
					&& (currentYear > periodYear || (currentYear == periodYear && currentMonth > periodMonth))) {
				int billYear = time.getYear() + 1900;
				int billMonth = time.getMonth() + 1;
				rs = dbmgt.periodIsExist(billYear, billMonth);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					if (Integer.parseInt(rs.getRetVal().toString()) == 0) {
						rs = dbmgt.addNextPriodArr(dbmgt.getNewAccPeriodBean(
								billYear, billMonth, lg.getId(),
								lg.getSunCmpClassCode(), periodYear,
								periodMonth));
						if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
							// 添加失败
							rs.setRetCode(ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC);
							return rs;
						}
					}
				} else {
					// 添加失败
					rs.setRetCode(ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC);
					return rs;
				}
			}
			values.put("Period", currentMonth);
			values.put("PeriodYear", currentYear);
			values.put("PeriodMonth", currentMonth);

			// String
			// sql="select AccYear,AccMonth,AccPeriod from tblAccPeriod where AccMonth ="+currentMonth+" and AccYear="+currentYear
			// +" and SCompanyID='"+sunClassCode+"'";
			// Result rsperiod=new AIODBManager().sqlListMap(sql, null, 0, 0);
			// if(rsperiod!=null&&((ArrayList)rsperiod.retVal).size()>0){
			// HashMap periodMap=(HashMap)((ArrayList)rsperiod.retVal).get(0);
			// values.put("Period", periodMap.get("AccPeriod").toString());
			// values.put("PeriodYear", periodMap.get("AccYear").toString());
			// values.put("PeriodMonth", periodMap.get("AccMonth").toString());
			// }
		}

		rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
		rs.setRetVal(values);
		return rs;
	}

	public boolean isPeriodPre(String dateStr, DBTableInfoBean tableInfo,
			LoginBean lg) {
		try {
			Date date = BaseDateFormat.parse(dateStr.toString(),
					BaseDateFormat.yyyyMMdd);
			/* 会计期间月，年 */
			int currentMonth = 0;
			int currentYear = 0;
			if (null != date) {
				currentMonth = date.getMonth() + 1;
				currentYear = date.getYear() + 1900;
			}
			int periodMonth = -1;
			int periodYear = -1;

			String sysParamter = tableInfo.getSysParameter();
			AccPeriodBean accBean = BaseEnv.accPerios.get(lg
					.getSunCmpClassCode());

			if (accBean != null) {
				periodMonth = accBean.getAccMonth(); // 会计期间月
				periodYear = accBean.getAccYear(); // 会计期间年
			}

			/* 验证录入的数据是否是当前会计的数据 */
			if ((currentYear < periodYear
					|| (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)
					&& currentMonth != 0) {
				if (null != sysParamter) {
					if (sysParamter.equals("CurrentAccBefBill")
							&& (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
						return true;
					}

					if (sysParamter
							.equals("CurrentAccBefBillAndUnUseBeforeStart")
							&& (currentYear < periodYear
									|| (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)) {
						return true;
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
		return false;
	}

	/**
	 * 删除内存中的图片
	 * 
	 * @return
	 */
	public void deleteTempImage(String[] upload, ArrayList listtemp) {
		for (int i = 0; upload != null && i < upload.length; i++) {
			String delF = upload[i];
			if (delF.indexOf(":") > -1) {
				delF = delF.substring(0, delF.indexOf(":"));
			}
			FileHandler.deleteTemp(delF);
			for (int j = 0; listtemp != null && j < listtemp.size(); j++) {
				if (listtemp.get(j).toString().equals(delF)) {
					listtemp.remove(j);
					break;
				}
			}
		}
	}

	/**
	 * 如果设置提醒扩展按钮，发送消息 手动发送消息
	 * 
	 * @return
	 */
	public Result sendWakeUpMessage(HttpServletRequest request, HashMap values,
			LoginBean lg, String smsType, DBTableInfoBean tableInfo,
			String locale) {
		Result rs = new Result();
		String wakeUps = request.getParameter("wakeUpMode"); // 提醒方式
		String content = request.getParameter("wakeUpContent"); // 提醒内容
		String popedomDeptId = request.getParameter("popedomDeptIds"); // 通知部门
		String popedomUserId = request.getParameter("popedomUserIds"); // 通知个人
		String popedomCRMCompany = request.getParameter("popedomCRMCompany");// CRM客户
		String popedomCompanyCode = request.getParameter("popedomCompanyCodes");// 往来单位
		String otherEmail = request.getParameter("otherEmail");
		String otherSMS = request.getParameter("otherSMS");
		String parentTableName = "";
		if (tableInfo.getPerantTableName() != null
				&& tableInfo.getPerantTableName().contains(";")) {
			parentTableName = tableInfo.getPerantTableName().split(";")[0];
		}
		content = content.replace("[用户]", lg.getEmpFullName());
		content = replaceFieldNameByValue(tableInfo, values, content, locale);
		String linkContent = "<a href=\"javascript:mdiwin('/UserFunctionAction.do?parentTableName="
				+ parentTableName
				+ "&tableName="
				+ tableInfo.getTableName()
				+ "&src=menu&keyId="
				+ values.get("id")
				+ "&f_brother="
				+ values.get("f_brother")
				+ "&operation=5&noback=true','"
				+ tableInfo.getDisplay().get(locale)
				+ "');\">"
				+ content
				+ "</a>";
		if (wakeUps != null && content != null && content.length() > 0) {
			String popedomUserIds = ""; // 所有通知对象
			if (popedomDeptId != null && !"".equals(popedomDeptId)) {
				String[] deptIds = popedomDeptId.split(",");
				for (String departId : deptIds) {
					List<EmployeeBean> list_emp = publicMgt
							.queryAllEmployeeByClassCode(departId);// 根据部门编号查找部门人员
					for (EmployeeBean emp : list_emp) {
						if (!popedomUserId.contains(emp.getId())) {// 判断是否已经单独授权
							popedomUserIds += emp.getId() + ",";
						}
					}
				}
			}
			if (popedomCompanyCode != null) {
				popedomUserIds += popedomCompanyCode;
			}
			if (popedomCRMCompany != null) {
				popedomUserIds += popedomCRMCompany;
			}
			if (popedomUserId == null) {
				popedomUserId = "";
			}
			popedomUserIds += popedomUserId;
			String[] strWakeUp = wakeUps.split(";");
			for (String wakeup : strWakeUp) {
				Thread wakeupThread = new Thread(new NotifyFashion(lg.getId(),
						content, linkContent, popedomUserIds,
						Integer.parseInt(wakeup), "no", "", otherEmail,
						otherSMS, "other"));
				wakeupThread.start();// 启动一个通知线程。
			}
		}
		return rs;
	}

	/**
	 * 如果设置提醒扩展按钮，发送消息 自动发送消息
	 * 
	 * @return
	 */
	public Result autoSendMessage(HashMap values, LoginBean lg,
			DBTableInfoBean tableInfo, String locale, String[] smsModel,
			String action) {
		Result rs = new Result();
		String wakeUps = smsModel[0]; // 提醒方式
		String content = smsModel[5]; // 提醒内容
		String popedomDeptId = smsModel[3]; // 通知部门
		String popedomUserId = smsModel[4]; // 通知个人
		String otherEmail = smsModel[1];
		String otherSMS = smsModel[2];

		content = content.replace("[用户]", lg.getEmpFullName());
		content = replaceFieldNameByValue(tableInfo, values, content, locale);
		String linkContent = "";
		if ("delete".equals(action)) {
			linkContent = content;
		} else {
			linkContent = "<a href=\"javascript:mdiwin('/UserFunctionAction.do?tableName="
					+ tableInfo.getTableName()
					+ "&src=menu&keyId="
					+ values.get("id")
					+ "&f_brother="
					+ values.get("f_brother")
					+ "&operation=5&noback=true','"
					+ tableInfo.getDisplay().get(locale)
					+ "');\">"
					+ content
					+ "</a>";
		}
		if (wakeUps != null && content != null && content.length() > 0) {
			String popedomUserIds = ""; // 所有通知对象
			if (popedomDeptId != null && !"".equals(popedomDeptId)) {
				String[] deptIds = popedomDeptId.split(",");
				for (String departId : deptIds) {
					List<EmployeeBean> list_emp = publicMgt
							.queryAllEmployeeByClassCode(departId);// 根据部门编号查找部门人员
					for (EmployeeBean emp : list_emp) {
						if (!popedomUserId.contains(emp.getId())) {// 判断是否已经单独授权
							popedomUserIds += emp.getId() + ",";
						}
					}
				}
			}
			if (popedomUserId == null) {
				popedomUserId = "";
			}
			popedomUserIds += popedomUserId;
			String[] strWakeUp = wakeUps.split(",");
			for (String wakeup : strWakeUp) {
				Thread wakeupThread = new Thread(new NotifyFashion(lg.getId(),
						content, linkContent, popedomUserIds,
						Integer.parseInt(wakeup), "no", "", otherEmail,
						otherSMS, "other"));
				wakeupThread.start();// 启动一个通知线程。
			}
		}
		return rs;
	}

	/**
	 * 查询单据是否存在消息通知模板
	 * 
	 * @return
	 */
	public Result querySMSModel(final String tableName, final String action) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,ExecuteStyle,OtherEmail,OtherSMS from tblSMSModel "
									+ "where statusId=0 and billTableName=? and ExecuteAction=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, tableName);
							pss.setString(2, action);
							ResultSet rss = pss.executeQuery();
							if (rss.next()) {
								String[] returnValue = new String[5];
								String style = rss.getString("ExecuteStyle");
								String keyId = rss.getString("id");
								String otherEmail = rss.getString("OtherEmail");
								String otherSMS = rss.getString("OtherSMS");
								sql = "select departmentCode from tblSMSUser where f_ref=? and len(userID)=0";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								rss = pss.executeQuery();
								String deptIds = "";
								while (rss.next()) {
									deptIds += rss.getString("departmentCode")
											+ ",";
								}
								sql = "select userID from tblSMSUser where f_ref=?";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								rss = pss.executeQuery();
								String userIds = "";
								while (rss.next()) {
									userIds += rss.getString("userID") + ",";
								}
								returnValue[0] = style;
								returnValue[1] = deptIds;
								returnValue[2] = userIds;
								returnValue[3] = otherEmail;
								returnValue[4] = otherSMS;
								rs.setRetVal(returnValue);
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
	 * 查询单据是否存在消息通知模板
	 * 
	 * @return
	 */
	public Result saveAsModule(final String tableName, final String action,
			final String content) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id from tblSMSModel "
									+ "where statusId=0 and billTableName=? and ExecuteAction=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, tableName);
							pss.setString(2, action);
							ResultSet rss = pss.executeQuery();
							if (rss.next()) {
								String keyId = rss.getString("id");
								sql = "insert into tblSMSContent(id,f_ref,SMSContent) values(?,?,?)";
								pss = conn.prepareStatement(sql);
								pss.setString(1, IDGenerater.getId());
								pss.setString(2, keyId);
								pss.setString(3, content);
								pss.executeUpdate();
							}
						} catch (Exception ex) {
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
	 * 查询单据通知模板
	 * 
	 * @return
	 */
	public Result querySMSModelInfo(final String tableName, final String action) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,NotifyStyle,OtherEmail,OtherSMS from tblSMSModel "
									+ " where statusId=0 and billTableName=? and ExecuteAction=? "
									+ " and ExecuteStyle in ('22','33')";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, tableName);
							pss.setString(2, action);
							ResultSet rss = pss.executeQuery();
							String[] sms = new String[6];
							String keyId = "";
							if (rss.next()) {
								keyId = rss.getString("id");
								sms[0] = rss.getString("NotifyStyle");
								sms[1] = rss.getString("OtherEmail");
								sms[2] = rss.getString("OtherSMS");
							}
							if (keyId != null && keyId.length() > 0) {
								sql = "select departmentCode from tblSMSUser where f_ref=? and len(userID)=0";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								rss = pss.executeQuery();
								String deptIds = "";
								while (rss.next()) {
									deptIds += rss.getString("departmentCode")
											+ ",";
								}
								sms[3] = deptIds;
								sql = "select userID from tblSMSUser where f_ref=?";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								rss = pss.executeQuery();
								String userIds = "";
								while (rss.next()) {
									userIds += rss.getString("userID") + ",";
								}
								sms[4] = userIds;
								sql = "select smsContent from tblSMSContent where f_ref=? ";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								rss = pss.executeQuery();
								if (rss.next()) {
									sms[5] = rss.getString("smsContent");
								}
								rs.setRetVal(sms);
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
	 * 添加 提醒
	 * 
	 * @param request
	 * @param keyId
	 * @param lg
	 * @param tableInfo
	 * @return
	 */
	public Result setAlert(HttpServletRequest request, String keyId,
			LoginBean lg, DBTableInfoBean tableInfo) {

		String alertDate = request.getParameter("alertDate"); /* 提醒日期 */
		String alertHour = request.getParameter("alertHour"); /* 提醒小时 */
		String alertMinute = request.getParameter("alertMinute"); /* 提醒分钟 */
		String alertContent = request.getParameter("alertContent"); /* 提醒内容 */
		String isLoop = request.getParameter("isLoop"); /* 循环提醒 */
		String loopType = request.getParameter("loopType"); /* 循环类型 */
		int loopTime = Integer.parseInt(request.getParameter("loopTime")); /* 循环次数 */
		String endDate = request.getParameter("endDate"); /* 结束日期 */
		String[] alertType = request.getParameterValues("alertType"); /* 提醒方式 */
		String popedomUserIds = request.getParameter("popedomUserIds"); /* 提醒用户ID */
		String popedomDeptIds = request.getParameter("popedomDeptIds"); /* 提醒部门ID */

		String strAlertType = "";
		for (String strType : alertType) {
			strAlertType += strType + ",";
		}
		if (strAlertType.length() == 0) {
			return new Result();
		}
		if (popedomDeptIds == null || popedomDeptIds == null) {
			popedomUserIds = lg.getId();
		}

		AlertBean alertBean = new AlertBean();
		alertBean.setId(IDGenerater.getId());
		alertBean.setAlertDate(alertDate);
		alertBean.setAlertHour(Integer.parseInt(alertHour));
		alertBean.setAlertMinute(Integer.parseInt(alertMinute));
		alertBean.setAlertContent(alertContent);
		alertBean.setIsLoop(isLoop);
		alertBean.setLoopType(loopType);
		alertBean.setLoopTime(loopTime);
		alertBean.setEndDate(endDate);
		alertBean.setAlertType(strAlertType);
		alertBean.setCreateBy(lg.getId());
		alertBean.setCreateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		alertBean.setRelationId(keyId);
		alertBean.setRelationTable(tableInfo.getTableName());
		alertBean.setNextAlertTime(alertDate + " " + alertHour + ":"
				+ alertMinute + ":00");
		alertBean.setStatusId(0);
		alertBean.setPopedomUserIds(popedomUserIds);
		alertBean.setPopedomDeptIds(popedomDeptIds);
		return addBean(alertBean);
	}

	/**
	 * 查询提醒设置
	 */

	public Result queryAlert() {
		String hql = "from AlertBean bean where current_date()>bean.nextAlertTime"
				+ "  and bean.statusId=0";
		return list(hql, new ArrayList());

	}

	/**
	 * 更新提醒设置
	 * 
	 * @param bean
	 * @return
	 */
	public Result updateAlert(AlertBean bean) {
		return updateBean(bean);
	}

	/**
	 * 删除 提醒设置
	 * 
	 * @param alertId
	 * @return
	 */
	public Result deleteAlert(String alertId) {
		return deleteBean("relationId", AlertBean.class, alertId);
	}

	/**
	 * 删除提醒设置
	 * 
	 * @param bean
	 * @return
	 */
	public Result deleteAlert2(String alertId) {
		return deleteBean(alertId, AlertBean.class, "id");
	}

	/**
	 * 加载提醒设置
	 * 
	 * @param alertId
	 * @return
	 */
	public Result loadAlert(String alertId) {
		ArrayList<String> list = new ArrayList<String>();
		String hql = "from AlertBean bean where bean.relationId=?";
		list.add(alertId);
		return list(hql, list);
	}

	public void setDefault(DBTableInfoBean tableInfo, HashMap values,
			String loginId) throws UnsupportedEncodingException {
		for (DBFieldInfoBean field : tableInfo.getFieldInfos()) {
			if (field.getInputType() == DBFieldInfoBean.INPUT_CUT_LINE) {
				continue;
			}
			if (values.get(field.getFieldName()) == null
					&& field.getDefaultValue() != null
					&& field.getDefaultValue().length() > 0) {

				String defPopValue = "";
				String defPopDis = "";
				if (field.getDefaultValue().indexOf(";") > 0) {
					defPopValue = field.getDefaultValue().substring(0,
							field.getDefaultValue().indexOf(";"));
					defPopDis = field.getDefaultValue().substring(
							field.getDefaultValue().indexOf(";") + 1);
				} else {
					defPopValue = field.getDefaultValue();
				}
				if (defPopValue.indexOf("@Sess:") >= 0) {
					Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet
							.get(loginId);
					String name = defPopValue.substring(defPopValue
							.indexOf("@Sess:") + "@Sess:".length());
					values.put(field.getFieldName(),
							(String) sessionSet.get(name));
				} else {
					values.put(field.getFieldName(), defPopValue);
				}
				if (field.getFieldType() == 5) {
					values.put(field.getFieldName(), BaseDateFormat.format(
							new Date(), BaseDateFormat.yyyyMMdd));
				} else if (field.getFieldType() == 6) {
					values.put(field.getFieldName(), BaseDateFormat.format(
							new Date(), BaseDateFormat.yyyyMMddHHmmss));
				}

			}
			if (field.getFieldName().endsWith("PYM")) {
				String chinese = (String) values.get(field.getFieldName()
						.substring(0, field.getFieldName().length() - 3));
				if (chinese != null && chinese.trim().length() > 0) {
					String pym = CustomizePYM.getFirstLetter(chinese);
					if (pym != null && pym.length() > 30) {
						pym = pym.substring(0, 30);
					}
					values.put(field.getFieldName(), pym);
				}
			}
			// 非不输入字段的字符串类型，要给值“”
			if (values.get(field.getFieldName()) == null
					&& field.getInputType() != DBFieldInfoBean.INPUT_NO
					&& (field.getFieldType() != DBFieldInfoBean.FIELD_INT && field
							.getFieldType() != DBFieldInfoBean.FIELD_DOUBLE)) {
				values.put(field.getFieldName(), "");
			}
			if (values.get(field.getFieldName()) == null
					&& field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE
					&& (field.getFieldType() == DBFieldInfoBean.FIELD_INT || field
							.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE)) {
				values.put(field.getFieldName(), "-100000");
			}

		}
	}

	/**
	 * 单据推算，导入会调用此方法
	 * 
	 * @param tableInfo
	 * @param values
	 * @param lg
	 * @param parentCode
	 * @param map
	 * @param workFlow
	 * @param path
	 * @param defineInfo
	 * @param locale
	 * @param addMessage
	 * @param resources
	 * @param props
	 * @param mop
	 * @param saveType
	 * @return
	 * @throws Exception
	 */
	public Result add(DBTableInfoBean tableInfo, HashMap values, LoginBean lg,
			String parentCode, Hashtable map, String path, String defineInfo,
			Locale locale, String addMessage, MessageResources resources,
			Hashtable props, MOperation mop, String saveType) throws Exception {

		Result result = new Result();
		String tableName = tableInfo.getTableName();

		/* 从内存中读取当前单据的工作流信息 */
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);

		/* 初始化一些字段的基本信息 */
		Result rs_initDBField = initDBFieldInfo(tableInfo, lg, values,
				parentCode, workFlow);
		if (rs_initDBField.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
			/* 不能录入会计前数据 */
			result.setRetCode(rs_initDBField.retCode);
			return result;
		} else if (rs_initDBField.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC) {
			/* 单据日期的期间在会计期间中不存在 */
			result.setRetCode(rs_initDBField.retCode);
			return result;
		}
		int opertion = 10;
		if ("saveDraft".equals(saveType)) {
			values.put("workFlowNodeName", "draft");
			opertion = 13;
		}
		Result rs_add = dbmgt.add(tableInfo.getTableName(), map, values,
				lg.getId(), path, defineInfo, resources, locale, saveType, lg,
				workFlow, "false", props);

		if (rs_add.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			/* 添加系统日志 导入 */
			if ("tblAccMain".equals(tableInfo.getTableName())) {
				// 导入凭证特殊处理
				dbmgt.addLog(
						opertion,
						lg.getId(),
						lg.getEmpFullName(),
						lg.getSunCmpClassCode(),
						"凭证：" + values.get("CredTypeID") + "-"
								+ values.get("OrderNo") + ";", "tblAccMain",
						"凭证管理", "");
			} else {
				dbmgt.addLog(opertion, values, null, tableInfo,
						locale.toString(), lg.getId(), lg.getEmpFullName(),
						lg.getSunCmpClassCode(), "", "");
			}

		} else {
			return rs_add;
		}
		return result;
	}

	public Result detailkeyId(final String tableName, final String keyId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						String sql = "";
						try {
							Statement st = conn.createStatement();
							String querysql = "select id from  " + tableName
									+ " where f_brother = '" + keyId + "'";

							ResultSet rss = st.executeQuery(querysql);

							ArrayList<String> tableIds = new ArrayList<String>();
							while (rss.next()) {
								String id = rss.getString("id");
								tableIds.add(id);
							}
							rs.setRetVal(tableIds);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log
									.debug("UserFunctionMgt selectGoodNumber Error sql :"
											+ sql);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result brotherName(final String parentTableName, final String keyId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						String sql = "";
						try {
							DBFieldInfoBean rowMark = null;
							DBTableInfoBean bean = BaseEnv.tableInfos
									.get(parentTableName);
							for (DBFieldInfoBean dfb : bean.getFieldInfos()) {
								if ("RowMarker".equals(dfb.getFieldSysType())) {
									rowMark = dfb;
								}
							}
							if (rowMark == null) {
								rs.retVal = "";
								return;
							}
							Statement st = conn.createStatement();
							String querysql = "select "
									+ rowMark.getFieldName() + " from  "
									+ parentTableName + " where id = '" + keyId
									+ "'";

							ResultSet rss = st.executeQuery(querysql);

							if (rss.next()) {
								String name = rss.getString(1);
								rs.setRetVal(name);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log
									.debug("UserFunctionMgt selectGoodNumber Error sql :"
											+ sql);
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
	 * 判断弹出窗口字段是否存在别名
	 * 
	 * @param popup
	 * @return
	 */
	public String getPopupAsName(PopupSelectBean popup, String fieldName) {
		if (popup == null)
			return null;
		int length = popup.getDisplayField().size();
		for (int i = 0; i < length; i++) {
			PopField field = (PopField) popup.getDisplayField().get(i);
			if (field.getFieldName().equals(fieldName)
					&& field.getAsName() != null
					&& field.getAsName().length() > 0) {
				return field.getAsName();
			}
		}
		return null;
	}

	/**
	 * 替换【】中字段值
	 * 
	 * @param tableInfo
	 * @param values
	 * @param content
	 * @return
	 */
	public String replaceFieldNameByValue(DBTableInfoBean tableInfo,
			HashMap values, String content, String locale) {
		if (content == null || content.length() == 0)
			return "";
		List<String> listField = getFieldName(content);
		for (String fieldName : listField) {
			String strField = fieldName.substring(1, fieldName.length())
					.substring(0, fieldName.length() - 2);
			for (DBFieldInfoBean dbField : tableInfo.getFieldInfos()) {
				if (dbField.getDisplay() != null
						&& strField.equals(dbField.getDisplay().get(locale))) {
					String strValue = "";
					if (dbField.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
						String varFieldName = "";
						for (int j = 0; j < dbField.getSelectBean()
								.getViewFields().size(); j++) {
							PopField posf = ((PopField) dbField.getSelectBean()
									.getViewFields().get(j));
							if (posf.getDisplay() != null
									&& posf.parentDisplay == true) {
								varFieldName = posf.getAsName().replace(".",
										"_");
								break;
							}
						}
						strValue = (String) values.get(dbField.getFieldName());
					} else if (dbField.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
						strValue = GlobalsTool.getEnumerationItemsDisplay(
								dbField.getRefEnumerationName(),
								(String) values.get(dbField.getFieldName()),
								locale);
					} else {
						strValue = (String) values.get(dbField.getFieldName());
					}
					content = content.replace(fieldName, strValue);
					break;
				}
			}
		}
		return content;
	}

	/**
	 * 获取属性的值
	 * 
	 * @param attr
	 * @param str
	 * @return
	 */
	private List<String> getFieldName(String str) {
		List<String> listAtt = new ArrayList<String>();
		Pattern pattern = Pattern
				.compile("(\\[[\\.\\w\\:\\-\\u4e00-\\u9fa5]+\\])");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			listAtt.add(matcher.group(1));
		}
		return listAtt;
	}

	/**
	 * 批量执行SQL语句
	 * 
	 * @param sql
	 *            ArrayList 所有要执行的SQL语句
	 * @return Result
	 */
	public Result batchExecSql(final ArrayList sqls) {
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) {
						String sql = "";
						try {
							Statement st = connection.createStatement();
							for (int i = 0; i < sqls.size(); i++) {
								sql = sqls.get(i).toString();
								st.addBatch(sqls.get(i).toString());
							}
							st.executeBatch();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.debug("Error sql :" + sql);
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
	 * 根据classCode查询商品
	 * 
	 * @param classCode
	 * @return
	 */
	public Result queryGoodsByClassCode(String classCode) {
		String hql = "from SpGoods bean where " + classCode
				+ " order by classCode";
		return list(hql, new ArrayList());
	}

	/**
	 * 根据商品Id查询商品
	 * 
	 * @param goodsId
	 * @return
	 */
	public Result queryGoodsByGoodsId(String goodsId) {
		String hql = "from SpGoods bean where bean.id in (" + goodsId
				+ ") order by classCode";
		return list(hql, new ArrayList());
	}

	/**
	 * 更新商品上下架状态
	 * 
	 * @param classCode
	 * @return
	 */
	public Result updateGoodShelf(SpGoods goodBean) {
		return updateBean(goodBean);
	}

	/**
	 * 更新商品上下架状态
	 * 
	 * @param classCode
	 * @return
	 */
	public Result loadGoods(String goodId) {
		return loadBean(goodId, SpGoods.class);
	}

	/**
	 * 更新商品上下架状态
	 * 
	 * @param sqls
	 * @return
	 */
	public Result updateShelfType(final String classCode, final String type) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						String sql = "";
						try {
							Statement st = conn.createStatement();
							if ("classCode".equals(type)) {
								sql = "update tblGoods set shelfType='down' where classCode like '"
										+ classCode + "%'";
							} else {
								sql = "update tblGoods set shelfType='down' where id = '"
										+ classCode + "'";
							}
							st.executeUpdate(sql);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log
									.debug("UserFunctionMgt updateShelfType Error sql :"
											+ sql);
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
	 * 查询商品目录
	 * 
	 * @param sqls
	 * @return
	 */
	public Result makeImageDirs() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						String sql = "";
						try {
							Statement st = conn.createStatement();
							sql = "select GoodsNumber,ClassCode from tblGoods where isCatalog=1 order by classCode";
							ResultSet rss = st.executeQuery(sql);
							HashMap<String, String> imageMap = new HashMap<String, String>();
							while (rss.next()) {
								String classCode = rss.getString("ClassCode");
								String goodsNumber = rss
										.getString("GoodsNumber");
								imageMap.put(classCode, goodsNumber);
								String filePath = "";
								while (classCode.length() > 0) {
									filePath = "\\" + imageMap.get(classCode)
											+ filePath;
									classCode = classCode.substring(0,
											classCode.length() - 5);
								}
								File imageFile = new File(
										BaseEnv.FILESERVERPATH
												+ "\\ShopImage\\" + filePath);
								if (!imageFile.exists()) {
									imageFile.mkdirs();
								}
							}
							rs.setRetVal(imageMap);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log
									.debug("UserFunctionMgt selectGoodNumber Error sql :"
											+ sql);
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
	 * 查询商品图片目录
	 * 
	 * @param sqls
	 * @return
	 */
	public Result queryGoodsImageDir(final String classCode) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						String sql = "";
						try {
							Statement st = conn.createStatement();
							String parents = "";
							String tempCode = classCode;
							while (tempCode.length() > 0) {
								if (!classCode.equals(tempCode)) {
									parents += "'" + tempCode + "',";
								}
								tempCode = tempCode.substring(0,
										tempCode.length() - 5);
							}
							String imagePath = "";
							if (parents.length() > 0) {
								parents = parents.substring(0,
										parents.length() - 1);
								sql = "select goodsNumber from tblGoods where classCode in ("
										+ parents + ") order by classCode";
								ResultSet rss = st.executeQuery(sql);
								while (rss.next()) {
									imagePath = imagePath + "/"
											+ rss.getString("goodsNumber");
								}
							}
							rs.setRetVal(imagePath);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log
									.debug("UserFunctionMgt queryGoodsImageDir Error sql :"
											+ sql);
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
	 * 把电子商务订单转换成销售订单对应的数据
	 * 
	 * @param listBean
	 * @return
	 */
	public static HashMap convertSalesOutStockData(SpOrder order,
			Hashtable allTables, LoginBean login) {
		HashMap values = new HashMap();
		values.put("id", order.getId());
		values.put("BillNo", order.getOrderNo());
		values.put("BillDate",
				BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
		values.put("CreateBy", login.getId());
		values.put("EmployeeID", login.getId());
		values.put("createTime", order.getOrderTime());
		values.put("CompanyCode", "00001000890000100001");
		values.put("DepartmentCode", login.getDepartCode());
		values.put("TotalTaxAmount", order.getOrderTotal());
		values.put("SendDate",
				BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
		values.put("SendGoods",
				BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
		values.put("orderType", "internet");
		values.put("Tel", order.getTelephone());
		values.put("InvoiceType", order.getInvoiceType());
		String remark = "收货地址：" + order.getReceiveAddress() + " "
				+ order.getReceiveName() + "&#13;&#10";
		if (order.getInvoiceTitle() == null
				|| order.getInvoiceTitle().length() > 0) {
			remark += "发票抬头：" + order.getInvoiceTitle() + " 发票内容："
					+ order.getInvoiceContent();
		}
		values.put("Remark", remark);
		List<SpOrderdet> detList = order.getDetList();
		ArrayList childList = new ArrayList();
		values.put("TABLENAME_tblSalesOrderDet", childList);
		for (SpOrderdet orderDet : detList) {
			HashMap hm = new HashMap();
			hm.put("GoodsCode", orderDet.getGoodCode().getClassCode());
			hm.put("Qty", orderDet.getQty());
			hm.put("Price", orderDet.getPrice());
			hm.put("Amount", orderDet.getAmount());
			childList.add(hm);
		}
		return values;
	}

	/**
	 * 会员资料转换CRM客户资料
	 * 
	 * @param listBean
	 * @return
	 */
	public static HashMap convertCRMClientInfoData(SpUser user,
			Hashtable allTables, LoginBean login) {
		HashMap values = new HashMap();
		values.put("id", user.getId());
		values.put("ClientNo",
				GlobalsTool.getFieldBean("CRMClientInfo", "ClientNo")
						.getDefValue());
		values.put("ClientName", user.getLoginName());
		values.put("Email", user.getEmail());
		values.put("Phone", user.getMobile());
		values.put("createTime", user.getCreateTime());
		return values;
	}

	/**
	 * 查询最后一条单据
	 * 
	 * @param kid
	 * @return
	 */
	public Result queryLastBill(final String tableName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select top 1 id from " + tableName
									+ "  order by createTime desc";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							if (rss.next()) {
								rst.setRetVal(rss.getString("id"));
							} else {
								rst.setRetCode(ErrorCanst.RET_ID_NO_VALUE_ERROR);
							}
						} catch (Exception ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error(
									"UserFunctionMgt queryLastBill mehtod", ex);
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
	 * 过滤html标记
	 * 
	 * @param content
	 * @return
	 */
	public static String replaceHTML(String content) {
		if (content == null || content.trim().length() == 0) {
			return "";
		}
		content = content.replaceAll("\n", "").replaceAll("\t", "");
		content = content.replaceAll("&lt;(?!img|embed).*?&gt;", "")
				.replaceAll("&nbsp;", " ");
		return content;
	}

	/**
	 * 查询某表的下级所有id值
	 * 
	 * @param tableName
	 * @param parentKeyId
	 * @return
	 */
	public Result getChildData(final String tableName, final String parentKeyId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {

							PreparedStatement pstmt = null;
							pstmt = conn
									.prepareStatement("select id from "
											+ tableName
											+ " where classcode like ?+'%' and classcode <> ? order by classcode");
							pstmt.setString(1, parentKeyId);
							pstmt.setString(2, parentKeyId);
							ResultSet rs = pstmt.executeQuery();
							ArrayList list = new ArrayList();
							while (rs.next()) {
								list.add(rs.getString(1));
							}
							rst.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
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
	 * 更新销售出库或其他出库中的Fact_Qty%字段
	 * 
	 * @param tableName
	 * @return
	 */
	public Result updateFactQty(final String tableName, final int inputType) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "update tblDBFieldInfo set inputType="
									+ inputType
									+ " where tableId=(select id from tblDBTableInfo where tableName='"
									+ tableName
									+ "Det') and fieldIdentityStr='iniOut'";
							cs.execute(sql);
							DDLOperation.updateRefreshTime("tableInfo", conn);// 更新内存
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}

	public Result getApproveRecdList(String tableName, String keyId) {
		String strSQL = "select userId ,ApproveLevel, case when checkflag = 1 then '已审核' else '未审核' end as checkflag,remark "
				+ " from tblApproveRecord  where tableName='"
				+ tableName
				+ "' and r_ref ='" + keyId + "'";
		Result rs = execSQL(strSQL);
		return rs;
	}

	/**
	 * 获取审核意见
	 * 
	 * @param tableName
	 *            String
	 * @param KeyId
	 *            String
	 * @param userId
	 *            String
	 * @return String
	 */

	public String getApproveRemark(String tableName, String KeyId, String userId) {

		String strSQL = "select remark  from tblApproveRecord where tableName = '"
				+ tableName
				+ "'"
				+ " and r_ref = '"
				+ KeyId
				+ "' and userId = '" + userId + "'";
		Result rs = execSQL(strSQL);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			return "";
		}
		if (((List) rs.getRetVal()).size() > 0) {
			Object[] obj = (Object[]) ((List) rs.getRetVal()).get(0);
			if (obj[1] == null)
				return "";
			else
				return obj[1].toString();
		}
		return "";
	}

	/*
	 * 功能：更新业务对象的审核情况 参数：tableName 表名 参数：keyId 业务的单据编号
	 */
	public Result updateCheckInfo(String tableName, String keyId, int draft) {
		Result rs = new Result();
		int lngCheckStatus;
		if (draft == DRAFT) {
			lngCheckStatus = DRAFT;
		} else {
			lngCheckStatus = getCheckStatus(tableName, keyId);
		}
		String strSQL = "update " + tableName + " set statusId = "
				+ lngCheckStatus + " where id  = '" + keyId + "'";
		execUpdateSQL(strSQL, rs);
		return rs;
	}

	/*
	 * 功能：获取审核记录对某项业务的核审情况 参数：tableName 表名 参数：keyId 业务的单据编号
	 */
	public int getCheckStatus(String tableName, String KeyId) {
		Result rs = new Result();
		int checkflag = 0;
		int levelCount = 0;
		int oldLevel = 283;
		String strSQL = "select approveLevel,checkflag from tblApproveRecord where tableName ='"
				+ tableName + "' and r_ref = '" + KeyId + "'";
		rs = execSQL(strSQL);
		if (((List) rs.getRetVal()).size() > 0) {
			for (int i = 0; i < ((List) rs.getRetVal()).size(); i++) {
				int lngflag = 0;
				int lngLevel = 0;
				Object[] obj = (Object[]) ((List) rs.getRetVal()).get(i);
				lngLevel = (Integer) obj[1];
				lngflag = (Integer) obj[2];
				if (oldLevel != lngLevel) {
					oldLevel = lngLevel;
					levelCount += 1;
				}
				if (lngflag != 0) {
					checkflag += 1;
				}
			}
			if (checkflag == 0) {
				return NO_CHECK;// 没审核
			} else {
				if (checkflag < levelCount) {
					return CHECKING;// 审核中
				} else {
					return CHECKED;// 已审核
				}
			}
		}
		return NO_CHECK;
	}

	/*
	 * 功能：在审核记录表写入用户的对某项业务的审核动作 参数：tableName 表名 参数：keyId 业务的单据编号 参数：userId 用户编号
	 */
	public Result writeCheckInfo(String tableName, String keyId, String userId,
			String remark, String checkflag) {
		Result rs = new Result();
		String strSQL;
		strSQL = "update tblApproveRecord set checkflag = " + checkflag + ","
				+ "remark = '" + remark + "'" + " where tableName ='"
				+ tableName + "' and r_ref ='" + keyId + "'"
				+ "  and userId ='" + userId + "'";
		execUpdateSQL(strSQL, rs);
		return rs;
	}

	/*
	 * 功能 ：查询共用过程 参数：strSQL 查询语句
	 */
	private Result execSQL(final String strSQL) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							ResultSet rset = cs.executeQuery(strSQL);
							int j = rset.getMetaData().getColumnCount();
							while (rset.next()) {
								Object[] os = new Object[j + 1];
								for (int i = 1; i <= j; i++) {
									os[i] = rset.getObject(i);
								}
								((ArrayList) rs.getRetVal()).add(os);
							}
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log
									.error("Query data Error :" + strSQL, ex);
							return;
						}
					}
				});
				return 0;
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/*
	 * 功能：数据更新共用过程 参数: strSQL 更新的sql
	 */
	private int execUpdateSQL(final String strSQL, final Result rs) {
		int intPageCount;
		final String sql;
		rs.setRetVal(new ArrayList());
		sql = strSQL;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							cs.executeUpdate(strSQL);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return 0;
			}
		});
		return 0;

	}

	/**
	 * 用于处理复核字段
	 * 
	 * @param keyIds
	 * @return
	 */
	public Result updateReAudit(final String sql, final String tableName,
			final Hashtable allTables, final HashMap values, final String id,
			final String userId, final String defineInfo,
			final MessageResources resources, final Locale locale) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement();
							state.executeUpdate(sql);
							Result defineRs = null;
							// 执行插入前自定义语句
							defineRs = new DynDBManager().defineSql(conn,
									"add", false, tableName,
									(Hashtable) allTables, values, id, userId,
									defineInfo, resources, locale);
							if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
								rst.setRetCode(defineRs.getRetCode());
								rst.setRetVal(defineRs.getRetVal());
								BaseEnv.log
										.error("DynDBManager Before add defineSql Error code = "
												+ defineRs.getRetCode()
												+ " Msg="
												+ defineRs.getRetVal());
							} else {
								rst.setRetVal("ok");
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							}
						} catch (Exception ex) {
							BaseEnv.log.error(
									"CRMClientMgt updateClientStatus mehtod:",
									ex);
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 获取tblcompany与CRMClientInfo 映射关系
	 * 
	 * @param conn
	 * @return
	 */
	public Result transferFields() {
		String transferFields = null;
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select transferFields from CRMClientModule where id = '0'";
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql);
							while (rs.next()) {
								String transferFields = rs.getString("transferFields");
								rst.setRetVal(transferFields);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							return;
						}
					}
				});

				return 0;
			}
		});
		return rst;
	}
	
	
	public Result updateTransferFields(final Map map,final Map detMap,final HttpServletRequest request) {
		String transferFields = null;
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String id = request.getParameter("id");
							String value = null;
							//更新主表
							Statement st = conn.createStatement();
							Set keySet = map.keySet();
							for (Object obj : keySet) {	
								 value = request.getParameter(obj.toString());
								String sql = "update CRMClientInfo set "+map.get(obj)+" = '"+value+"' where id = '"+id+"'";								
								st.addBatch(sql);			
							}	
							st.executeBatch();
							st.clearBatch();
							//更新联系人表
							Set keySetDet = detMap.keySet();
							for (Object obj : keySetDet) {	
								 value = request.getParameter("tblCompanyEmployeeDet_"+obj.toString());
								String sqlDet = "update CRMClientInfoDet set "+detMap.get(obj)+" = '"+value+"' where f_ref = '"+id+"'";
								st.addBatch(sqlDet);
							}	
							st.executeBatch();
							st.clearBatch();
							rst.setRetCode(1);
							
						} catch (Exception ex) {
							ex.printStackTrace();
							return;
						}
					}
				});
				
				return 0;
			}
		});
		return rst;
	}


}
