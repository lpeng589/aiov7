package com.menyi.aio.web.customize;

import com.menyi.web.util.MgtBaseAction;
import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForward;

import javax.activation.FileDataSource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.menyi.web.util.BusinessException;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.SqlConfig;
import com.menyi.web.util.SystemState;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.struts.action.ActionForm;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import com.menyi.web.util.BaseEnv;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Hashtable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.TableNameGenerate;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.web.util.IDGenerater;
import com.menyi.aio.web.importData.ImportForm;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.report.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.menyi.aio.bean.*;
import com.menyi.aio.web.usermanage.UserManageAction;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class CustomizeAction extends MgtBaseAction {

	CustomizeMgt mgt = new CustomizeMgt();
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}

	/**
	 * exe 控制器入口函数
	 *
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("popWinName", request.getParameter("popWinName")); //记录用于打开自己的弹出窗的名字，用于关闭自己
		request.setAttribute("JSESSIONID", request.getSession().getId());
		
		//跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) { 
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_COPY:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_COPY_PREPARE:
			forward = copyPrepare(mapping, form, request, response);
			break;

		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			forward = detail(mapping, form, request, response);
			break;
		default:
			if ("popupSet".equals(request.getParameter("type"))) {
				forward = popupSet(mapping, form, request, response);
			} else if ("popupQuery".equals(request.getParameter("type"))) {
				forward = popupQuery(mapping, form, request, response);
			} else if ("popupUpdate".equals(request.getParameter("type"))) {
				forward = popupUpdate(mapping, form, request, response);
			} else if ("popupQutoeShow".equals(request.getParameter("type"))) {
				forward = popupQutoeShow(mapping, form, request, response);
			} else if ("popupExport".equals(request.getParameter("type"))) {
				forward = popupExport(mapping, form, request, response);
			}else if ("popupImport".equals(request.getParameter("type"))) {
				forward = popupImport(mapping, form, request, response);
			}  else if ("addBrother".equals(request.getParameter("type"))) {
				forward = addBrother(mapping, form, request, response);
			}  else if ("defineOp".equals(request.getParameter("type"))) {
				forward = defineOp(mapping, form, request, response);
			}  else if ("defineUpdate".equals(request.getParameter("type"))) {
				forward = defineUpdate(mapping, form, request, response);
			}  else if ("defineQuery".equals(request.getParameter("type"))) {
				forward = defineQuery(mapping, form, request, response);
			}  else if ("defineFile".equals(request.getParameter("type"))) {
				forward = defineFile(mapping, form, request, response);
			}   else if ("newdefineFile".equals(request.getParameter("type"))) {
				forward = newDefineFile(mapping, form, request, response);
			}   else if ("getBillSQL".equals(request.getParameter("type"))) {
				forward = getBillSQL(mapping, form, request, response);
			}   else if ("getReportId".equals(request.getParameter("type"))) {
				forward = getReportId(mapping, form, request, response);
			}   else if ("getMenu".equals(request.getParameter("type"))) {
				forward = getMenu(mapping, form, request, response);
			}    else if ("export".equals(request.getParameter("type"))) {
				forward = export(mapping, form, request, response);
			}    else if ("importTable".equals(request.getParameter("type"))) {
				forward = importTable(mapping, form, request, response);
			}  else {
				forward = query(mapping, form, request, response);
			}
		}
		return forward;
	}

	/**
	 * 添加前的准备
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.removeAttribute("operation");
		DBTableInfoBean newInfo = new DBTableInfoBean();
		//从工作流创建时，会带有tableName表单名称
		String tableName = request.getParameter("tableName");
		String tableCHName = request.getParameter("tableCHName");
		tableCHName = GlobalsTool.toChinseChar(tableCHName);
		
		String tableType = request.getParameter("tableType");
		tableType = tableType == null ? "0" : tableType;
		request.setAttribute("tableType", tableType);
		newInfo.setTableType(Byte.parseByte(tableType));
		newInfo.setIsUsed(1);
		

		if (newInfo.getTableType() != 0) {
			newInfo.setPerantTableName(request.getParameter("perantTableName") + ";");
			newInfo.setDefRowCount(1);
		}
		
		if(tableCHName!= null && tableCHName.length() > 0){			
			newInfo.setDisplay(KRLanguageQuery.create(tableCHName, "", tableCHName));
			if(tableName != null && tableName.length() > 0){
				newInfo.setTableName(tableName);
			}else if (newInfo.getTableType() != 0) {
				//如果tableName没名字，且是明细表时
				tableName = request.getParameter("perantTableName") +"Det";
				//查明细个数
				int cnum = DDLOperation.getChildTables(newInfo.getPerantTableName(), BaseEnv.tableInfos).size();
				tableName +=(cnum+1)+"";
				
				newInfo.setTableName(tableName);
			}
		}
		

		newInfo.setFieldInfos(new ArrayList());
		DBFieldInfoBean oneF = new DBFieldInfoBean();
		oneF.setFieldName("");
		oneF.setFieldType(DBFieldInfoBean.FIELD_ANY);
		oneF.setWidth(200);
		newInfo.getFieldInfos().add(oneF);
		request.setAttribute("result", newInfo);

		if (false && request.getParameter("type") != null && request.getParameter("type").equals("simple")) {
			return getForward(request, mapping, "simpleTableMgt");
		} else {
			return getForward(request, mapping, "tableMgt");
		}
	}

	/**
	 * 添加
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	protected ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Result rs = new Result();
		String tableName = getParameter("tableName", request).trim();
		String OAWorkFlowId = getParameter("OAWorkFlowId", request);
		if (tableName == null || tableName.length() == 0) {
			tableName = TableNameGenerate.getTableName();
		}
		if(OAWorkFlowId != null){
			System.out.println(OAWorkFlowId);
			mgt.updateTemplateFile(OAWorkFlowId,tableName);
			
		}

		//取主表数据
		DBTableInfoBean tableInfo = new DBTableInfoBean();
		tableInfo.setTableName(tableName);
		tableInfo.setId(IDGenerater.getId());
		tableInfo.setUpdateAble(DBTableInfoBean.CAN_UPDATE);
		tableInfo.setUdType(DBTableInfoBean.USER_TYPE);

		tableInfo.setIsUsed(getParameterInt("isUsed", request));
		tableInfo.setFieldCalculate(getParameter("fieldCalculate", request));
		//明细表设置显示行数
		tableInfo.setDefRowCount(getParameterInt("defRowCount", request));
		tableInfo.setBrotherType(getParameterInt("brotherType", request));
		tableInfo.setCopyParent(getParameterInt("copyParent", request));
		tableInfo.setTWidth(getParameterInt("tWidth", request));
		tableInfo.setTHeight(getParameterInt("tHeight", request));
		tableInfo.setExtendButton(getParameter("extendButton", request));//扩展按钮
		tableInfo.setIsSunCmpShare(getParameterInt("isSunCmpShare", request));//      设置是否分支机构共享
		tableInfo.setIsBaseInfo(getParameterInt("isBaseInfo", request));//设置是否基础信息
		tableInfo.setNeedsCopy(getParameterInt("needsCopy", request));//设置是否需要复制
		tableInfo.setWakeUp(getParameterInt("wakeUp", request));//是否支持提醒 
		tableInfo.setHasNext(getParameterInt("hasNext", request));//是否支持上下篇
		tableInfo.setIsView(getParameterInt("isView", request));//是否是视图
		tableInfo.setIsLayout(getParameterInt("isLayout", request));
		tableInfo.setReAudit(getParameterInt("reAudit", request));
		//tableInfo.setLayoutHTML(request.getParameter("layoutHTML"));
		tableInfo.setTableSysType(request.getParameter("tableSysType"));//系统类型
		tableInfo.setRelationTable(request.getParameter("relationTable"));//视图对应的表
		tableInfo.setClassFlag((byte) getParameterInt("classFlag", request));//支持分类标志
		tableInfo.setClassCount(getParameterInt("classCount", request));//类别级数
		tableInfo.setApproveFlow(getParameter("approveFlow", request));//审核流程
		tableInfo.setDraftFlag((byte) getParameterInt("draftFlag", request));//能否存为草稿
		tableInfo.setTableType((byte) getParameterInt("tableType", request));

		tableInfo.setSysParameter(getParameter("sysParameter", request));
		tableInfo.setTriggerExpress(getParameterInt("triggerExpress", request));
		tableInfo.setPerantTableName(getParameter("perantTableName", request));

		tableInfo.setTableDesc(request.getParameter("tableDesc"));
		tableInfo.setMainModule(request.getParameter("MainModule"));
		String mainDisplay = getParameter("tableDisplay", request);

		tableInfo.setIsNull(getParameterInt("tableIsNull", request));
		tableInfo.setFieldInfos(new ArrayList());

		if (!tableName.startsWith("Flow2_")) {
			tableInfo.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
					org.apache.struts.Globals.LOCALE_KEY), mainDisplay));
		} else { //个性化表单设计表名多语言
			String tableCHName = request.getParameter("tableCHName");
			tableInfo.setDisplay(KRLanguageQuery.create(tableCHName, tableName, tableCHName));
		}
		tableInfo.setLanguageId(tableInfo.getDisplay().getId());
		tableInfo.setCreateBy(getLoginBean(request).getId());
		tableInfo.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		tableInfo.setLastUpdateBy(getLoginBean(request).getId());
		tableInfo.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));

		tableInfo.setFieldInfos(new ArrayList());

		if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE) {
			DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("f_ref");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo.setMaxLength(30);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NORMAL);
			fieldInfo.setListOrder((short) 0);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			tableInfo.getFieldInfos().add(fieldInfo);
		} else if (tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE) {
			DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("f_brother");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo.setMaxLength(30);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NORMAL);
			fieldInfo.setListOrder((short) 0);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			tableInfo.getFieldInfos().add(fieldInfo);
		} else {
			DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("classCode");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo.setMaxLength(500);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setListOrder((short) 0);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			tableInfo.getFieldInfos().add(fieldInfo);
			
			fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("isCatalog");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_INT);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setListOrder((short) 0);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDefaultValue("0");
			fieldInfo.setDisplay(KRLanguageQuery.create("类别", "Catalog", "类别"));
			tableInfo.getFieldInfos().add(fieldInfo);
		}




		//取字段数据
		String[] fieldDisplays = getParameters("fieldDisplay", request);
		String[] isNulls = getParameters("isNull", request);
		String[] maxLengths = getParameters("maxLength", request);
		String[] defaultValues = getParameters("defaultValue", request);
		String[] listOrders = getParameters("listOrder", request);
		String[] fieldTypes = getParameters("fieldType", request);
		String[] digitses = getParameters("digits", request);
		String[] inputTypes = getParameters("inputType", request);
		String[] moreInputs = getParameters("moreInput", request);
		String[] fieldNames = getParameters("fieldName", request);
		String[] widths = getParameters("width", request);
		String[] calculates = getParameters("calculate", request);
		String[] isUniques = getParameters("isUnique", request);
		String[] isStats = getParameters("isStat", request);
		String[] fieldSysType = getParameters("fieldSysType", request);
		String[] fieldIdentityStr = getParameters("fieldIdentityStr", request);
		String[] logicValidate = getParameters("logicValidate", request);
		String[] popupType = getParameters("popupType", request);
		String[] groupName = getParameters("groupName", request);
		String[] insertTable = getParameters("insertTable", request);
		String[] isCopy = getParameters("isCopy", request);
		String[] copyType = getParameters("copyType", request);
		String[] isLog = getParameters("isLog", request);
		String[] isMobile = getParameters("isMobile", request);

		String[] fieldId = getParameters("fieldId", request); //字段ID，工作流隐藏
		String[] languageIds = getParameters("languageId", request); //字段ID，工作流隐藏
		String[] inputTypeOlds = getParameters("inputTypeOld", request); //输入类型，工作流不隐藏		
		String[] refEnumerationNames = getParameters("refEnumerationName", request); //枚举，工作流不隐藏
		String[] inputValues = getParameters("inputValue", request); //录入，工作流不隐藏
		String[] groupLanguageIds = getParameters("groupLanguageId", request); //分组，工作流隐藏
		String[] isCopys = getParameters("isCopy", request); //是否单据复制时，字段是否复制，工作流隐藏
		String[] isReAudit = getParameters("isReAudit", request); //复核

		for (int i = 0; i < fieldNames.length; i++) {
			if ("detOrderNo".equals(fieldNames[i])) {
				//新创建的明细表不再增加detOrderNo字段，因复制有可能带有此字段，所以过滤掉
				continue;
			}
			if (fieldNames[i] != null && fieldNames[i].trim().length() > 0) {
				DBFieldInfoBean fieldInfo = new DBFieldInfoBean();

				fieldInfo.setId(IDGenerater.getId());
				fieldInfo.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
						org.apache.struts.Globals.LOCALE_KEY), fieldDisplays[i]));
				fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());

				if (groupName[i] != null && !groupName[i].equals("")) {
					fieldInfo.setGroupDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
							org.apache.struts.Globals.LOCALE_KEY), groupName[i]));
					if (fieldInfo.getGroupDisplay().map.size() == 0) {
						fieldInfo.setGroupDisplay(null);
					} else {
						fieldInfo.setGroupName(fieldInfo.getGroupDisplay().getId());
					}
				}

				fieldInfo.setTableBean(tableInfo);

				String fieldName = fieldNames[i];
				if (fieldName == null || fieldName.length() == 0) {
					fieldName = TableNameGenerate.getFieldName(i);
				}
				fieldInfo.setFieldName(fieldName);
				fieldInfo.setInputType((byte) paseInt(inputTypes[i]));
				fieldInfo.setInputTypeOld((byte) paseInt(inputTypeOlds[i]));
				if(fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_NO &&
						fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_HIDDEN&&
						fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_HIDDEN_INPUT&&
						fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_ONLY_READ){
					fieldInfo.setInputTypeOld(fieldInfo.getInputType());
				}

				fieldInfo.setFieldType((byte) paseInt(fieldTypes[i]));
				
				
				//-------------------------------------------

				fieldInfo.setIsNull((byte) paseInt(isNulls[i]));
				fieldInfo.setListOrder((short) paseInt(listOrders[i]));
				fieldInfo.setMaxLength(paseInt(maxLengths[i]));
				fieldInfo.setWidth(paseInt(widths[i]));

				fieldInfo.setCalculate(calculates[i]==null?null:calculates[i].replace("'", "\""));
				fieldInfo.setIsUnique((byte) paseInt(isUniques[i]));
				fieldInfo.setIsStat((byte) paseInt(isStats[i]));
				fieldInfo.setDefaultValue(defaultValues[i]);
				fieldInfo.setFieldSysType(fieldSysType[i]);
				fieldInfo.setFieldIdentityStr(fieldIdentityStr[i]);
				fieldInfo.setLogicValidate(logicValidate[i]);
				fieldInfo.setPopupType(popupType[i]);
				fieldInfo.setInsertTable(insertTable[i]);
				fieldInfo.setIsCopy((byte) paseInt(isCopys[i]));
				fieldInfo.setIsLog((byte) paseInt(isLog[i]));
				fieldInfo.setCopyType(copyType[i]);
				fieldInfo.setIsMobile((byte)paseInt(isMobile[i]));
				fieldInfo.setIsReAudit((byte) paseInt(isReAudit[i]));
				fieldInfo.setStatusId(paseInt(request.getParameter(fieldNames[i] + "_statusId")));

				if(fieldInfo.getFieldType()==1 && "Qty".equals(fieldInfo.getFieldIdentityStr())){
					fieldInfo.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsQty")));
				}else if(fieldInfo.getFieldType()==1 && ("priceIdentifier".equals(fieldInfo.getFieldIdentityStr()) || 
						"SPriceIdentifier".equals(fieldInfo.getFieldIdentityStr()) ||"Price".equals(fieldInfo.getFieldIdentityStr()) )){
					fieldInfo.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsPrice")));
				}else if(fieldInfo.getFieldType()==1 && ("Amount".equals(fieldInfo.getFieldIdentityStr()) || 
						"AmountIdentifier".equals(fieldInfo.getFieldIdentityStr()) ||"SAmountIdentifier".equals(fieldInfo.getFieldIdentityStr()) )){
					fieldInfo.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsAmount")));
				}else{
					fieldInfo.setDigits((byte) paseInt(digitses[i]));
				}
				fieldInfo.setInputValue("");
				fieldInfo.setRefEnumerationName("");
				if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_ENUMERATE)) {
					fieldInfo.setRefEnumerationName((refEnumerationNames[i].trim()));
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_DOWNLOAD_SELECT || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_DOWNLOAD_SELECT)) {
					fieldInfo.setInputValue((inputValues[i].trim()));
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE || fieldInfo.getInputType() == DBFieldInfoBean.INPUT_FUNCTION
						|| fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE || fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_FUNCTION) {
					String iv = inputValues[i].trim();
					if(iv.indexOf(":") > -1){
						iv = iv.substring(0,iv.indexOf(":"));
					}
					fieldInfo.setInputValue(iv);
					fieldInfo.setInputValue(iv);
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX)) {
					fieldInfo.setRefEnumerationName(refEnumerationNames[i].trim());
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_RADIO || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_RADIO)) {
					fieldInfo.setRefEnumerationName(refEnumerationNames[i].trim());
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_PYM || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_PYM)) {
					DBFieldInfoBean fieldPYM = DDLOperation.getFieldPYM(fieldInfo);
					tableInfo.getFieldInfos().add(fieldPYM);
				}

				//检查系统配置是否启用
				if (fieldInfo.getFieldSysType() != null && fieldInfo.getFieldSysType().length() > 0) {
					SystemSettingBean ssBean = BaseEnv.systemSet.get(fieldInfo.getFieldSysType());
					if (ssBean != null) {
						if (!Boolean.parseBoolean(ssBean.getSetting()) && fieldInfo.getInputType() != DBFieldInfoBean.INPUT_HIDDEN && fieldInfo.getInputType() != DBFieldInfoBean.INPUT_NO) {
							fieldInfo.setInputType((byte) 3);
						}
					}
				}

				tableInfo.getFieldInfos().add(fieldInfo);
			}
		}

		//当为主表时 添加建立人，建立时间，修改人，修改时间，分支机构
		if (tableInfo.getTableType() != DBTableInfoBean.CHILD_TABLE) {
			DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("createBy");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo.setMaxLength(30);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setIsNull(DBFieldInfoBean.NOT_NULL);
			fieldInfo.setListOrder((short) 100);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDisplay(KRLanguageQuery.create("建立人", "Creator", "建立人"));
			fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
			tableInfo.getFieldInfos().add(fieldInfo);

			fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("lastUpdateBy");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo.setMaxLength(30);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
			fieldInfo.setListOrder((short) 100);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDisplay(KRLanguageQuery.create("最后修改人", "LastUpdate", "最後修改人"));
			fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
			tableInfo.getFieldInfos().add(fieldInfo);

			fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("createTime");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_DATE_LONG);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
			fieldInfo.setMaxLength(19);
			fieldInfo.setListOrder((short) 100);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDisplay(KRLanguageQuery.create("建立时间", "CreateTime", "建立時間"));
			fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
			tableInfo.getFieldInfos().add(fieldInfo);

			fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("lastUpdateTime");
			fieldInfo.setMaxLength(19);
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_DATE_LONG);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
			fieldInfo.setListOrder((short) 100);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDisplay(KRLanguageQuery.create("修改时间", "UpdateTime", "修改時間"));
			fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
			tableInfo.getFieldInfos().add(fieldInfo);

			fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("statusId");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_INT);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
			fieldInfo.setDefaultValue("0");
			fieldInfo.setListOrder((short) 100);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDisplay(KRLanguageQuery.create("状态", "StatusId", "狀態"));
			fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
			tableInfo.getFieldInfos().add(fieldInfo);

			fieldInfo = new DBFieldInfoBean();
			//分支机构
			fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("SCompanyID");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo.setMaxLength(30);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
			fieldInfo.setListOrder((short) 100);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDisplay(KRLanguageQuery.create("分支机构", "SonCompanyID", "分支機搆"));
			fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
			tableInfo.getFieldInfos().add(fieldInfo);

			//过账时间
			fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("finishTime");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_DATE_LONG);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
			fieldInfo.setMaxLength(19);
			fieldInfo.setListOrder((short) 100);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDisplay(KRLanguageQuery.create("过账时间", "finish Time", "過帳時間"));
			fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
			tableInfo.getFieldInfos().add(fieldInfo);
			
			//打印次数
			fieldInfo = new DBFieldInfoBean();
			fieldInfo.setId(IDGenerater.getId());
			fieldInfo.setFieldName("printCount");
			fieldInfo.setFieldType(DBFieldInfoBean.FIELD_INT);
			fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
			fieldInfo.setDefaultValue("0");
			fieldInfo.setListOrder((short) 100);
			fieldInfo.setTableBean(tableInfo);
			fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
			fieldInfo.setDisplay(KRLanguageQuery.create("打印次数", "PrintCount", "打印次数"));
			fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
			tableInfo.getFieldInfos().add(fieldInfo);
			
			DBFieldInfoBean fieldInfo2 = new DBFieldInfoBean();
			fieldInfo2.setId(IDGenerater.getId());
			fieldInfo2.setFieldName("workFlowNode");
			fieldInfo2.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo2.setMaxLength(30);
			fieldInfo2.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo2.setListOrder((short) 0);
			fieldInfo2.setTableBean(tableInfo);
			fieldInfo2.setUdType(DBFieldInfoBean.USER_TYPE);
			tableInfo.getFieldInfos().add(fieldInfo2);

			DBFieldInfoBean fieldInfo3 = new DBFieldInfoBean();
			fieldInfo3.setId(IDGenerater.getId());
			fieldInfo3.setFieldName("workFlowNodeName");
			fieldInfo3.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo3.setMaxLength(30);
			fieldInfo3.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo3.setListOrder((short) 0);
			fieldInfo3.setTableBean(tableInfo);
			fieldInfo3.setUdType(DBFieldInfoBean.USER_TYPE);
			tableInfo.getFieldInfos().add(fieldInfo3);

			DBFieldInfoBean fieldInfo4 = new DBFieldInfoBean();
			fieldInfo4.setId(IDGenerater.getId());
			fieldInfo4.setFieldName("checkPersons");
			fieldInfo4.setFieldType(DBFieldInfoBean.FIELD_ANY);
			fieldInfo4.setMaxLength(8000);
			fieldInfo4.setInputType(DBFieldInfoBean.INPUT_NO);
			fieldInfo4.setListOrder((short) 0);
			fieldInfo4.setTableBean(tableInfo);
			fieldInfo4.setUdType(DBFieldInfoBean.USER_TYPE);
			tableInfo.getFieldInfos().add(fieldInfo4);
			
			
		}
		if(tableInfo.getTableName().contains("_")){
			EchoMessage.error().add("表名不能包含下划线").setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}

		Object o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		//获得data.txt路径
		String path = request.getSession().getServletContext().getRealPath("data.sql");
		DDLOperation ddlo = new DDLOperation();
		rs = ddlo.create((Hashtable) o, tableInfo, path);
		
		String oldtableName = getParameter("oldtableName", request);
		if(oldtableName!= null ){
			mgt.updateLayoutHTML(oldtableName,tableName);			
		}

		ActionForward forward = getForward(request, mapping, "alert");

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {//添加成功
			//更新内存
			//保存 PopDisSentence BaseEnv.popDisSentence.get(tableName)
			String PopDisSentence = request.getParameter("PopDisSentence");
			ddlo.savePopDisSentence(tableName, PopDisSentence, path);
			
			new InitMenData().initDBInformation(request.getSession().getServletContext(), tableInfo.getTableName());
			String type = request.getParameter("type");
			if (type != null && type.equals("simple")) {
				EchoMessage em = EchoMessage.error().add(GlobalsTool.getMessage(request, "mrp.help.saveSucess"));
				em.setPopWin(true);
				em.setAlertRequest(request);
			} else {
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl(
						request.getRequestURI()+"?operation=7&keyId=" + tableInfo.getTableName() + "&popWinName=" + request.getParameter("popWinName")).setAlertRequest(request);
				request.getSession().removeAttribute("BUTTON");
			}
			new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), tableInfo.getDisplay().get(
					this.getLocale(request).toString()), "tblDBTableInfo", "表结构管理", "");

		} else if (rs.retCode == ErrorCanst.RET_FIELD_EXIST_ERROR) {
			//列名重复
			EchoMessage.error().add("列名 " + rs.getRetVal() + " 重复").setAlertRequest(request);
		} else {
			//添加失败
			EchoMessage.error().add(rs.retCode, request).setAlertRequest(request);
		}
		return forward;
	}

	public static String getId(String id) {
		String result = "";
		if (id.length() > 27) {
			result = id.substring(0, 26) + "pym";
		} else {
			result = id + "pym";
		}

		return result;
	}

	private int paseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	/**
	 * 显示弹出窗
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward popupSet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String popupValue = request.getParameter("popupValue");
		String msg = "";
		if (popupValue == null || "".equals(popupValue)) {
			msg = "未设置弹出窗";
		}
		PopupSelectBean bean = BaseEnv.popupSelectMap.get(popupValue);
		if (bean == null) {
			msg = "弹出窗不存在";
		} else {
			request.setAttribute("popupDesc", bean.getDesc());
			request.setAttribute("popupName", bean.getName());
			request.setAttribute("popupPath", bean.getPath().substring(bean.getPath().lastIndexOf("..") + 3));
		}

		request.setAttribute("msg", msg);
		return getForward(request, mapping, "popupSet");
	}
	
	/**
	 * 查询弹出窗
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward popupQutoeShow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String popupValue = request.getParameter("popupValue");
		ArrayList list = new ArrayList();
		String locale = getLocale(request).toString();
		for (DBTableInfoBean tableBean : BaseEnv.tableInfos.values()) {
			for(DBFieldInfoBean fieldBean : tableBean.getFieldInfos())
			{
				if(popupValue.equals(fieldBean.getInputValue())){
				list.add(tableBean.getDisplay().get(locale)+"("+tableBean.getTableName()+")"+
						fieldBean.getDisplay().get(locale)+"("+fieldBean.getFieldName()+")");
				}
			}
		}
		
		String path = BaseEnv.REPORTPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		File file = new File(path);
		for(File f:file.listFiles()){
			if(f.getName().toLowerCase().endsWith(".xml")){
				FileInputStream fis = new FileInputStream(f);
				byte[] bs = new byte[fis.available()];
				fis.read(bs);
				fis.close();
				String s = new String(bs,"UTF-8");
				if(s.indexOf("popUpName=\""+popupValue+"\"")>-1){
					int pos = s.indexOf("reportName=\"")+"reportName=\"".length();
					if(pos>0){
						String reportName = s.substring(pos,s.indexOf("\"",pos));
						list.add(reportName+"("+file.getAbsolutePath()+")");
					}
				}
			}
		}
		path = BaseEnv.USERREPORTPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		file = new File(path);
		for(File f:file.listFiles()){
			if(f.getName().toLowerCase().endsWith(".xml")){
				FileInputStream fis = new FileInputStream(f);
				byte[] bs = new byte[fis.available()];
				fis.read(bs);
				fis.close();
				String s = new String(bs,"UTF-8");
				if(s.indexOf("popUpName=\""+popupValue+"\"")>-1){
					int pos = s.indexOf("reportName=\"")+"reportName=\"".length();
					if(pos>0){
						String reportName = s.substring(pos,s.indexOf("\"",pos));
						list.add(reportName+"("+file.getAbsolutePath()+")");
					}
				}
			}
		}
		request.setAttribute("result", list);
		return getForward(request, mapping, "popupQutoeShow");
	}

	/**
	 * 查询弹出窗
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward popupQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String keyword = request.getParameter("keyword");
		request.setAttribute("keyword", keyword);
		ArrayList list = new ArrayList();
		for (PopupSelectBean bean : BaseEnv.popupSelectMap.values()) {
			if (keyword != null && keyword.length() > 0) {
				if (bean.getName().toLowerCase().contains(keyword.toLowerCase()) || bean.getDesc().toLowerCase().contains(keyword.toLowerCase())) {
					list.add(bean);
				}
			} else {
				list.add(bean);
			}
		}
		request.setAttribute("result", list);
		request.setAttribute("from", request.getParameter("from"));
		return getForward(request, mapping, "popupQuery");
	}

	private boolean[] setCBVal(int len, String name, HttpServletRequest request) {
		boolean[] ret = new boolean[len];
		String[] val = request.getParameterValues(name);
		if (val != null) {
			for (String v : val) {
				int i = Integer.parseInt(v);
				ret[i] = true;
			}
		}
		return ret;
	}

	public static String encode(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");

		return str;
	}
	protected ActionForward popupExport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String popupName = request.getParameter("popupName");
		String popupWin = request.getParameter("popupWin");
		request.setAttribute("popupWin", popupWin);

		
		ArrayList fields = new ArrayList();
		PopupSelectBean bean = BaseEnv.popupSelectMap.get(popupName);
		String path = bean.getPath();
		DataInputStream dis = new DataInputStream(new FileInputStream(path));
		byte[] bs = new byte[dis.available()];
		dis.read(bs);
		dis.close();
		String str=new String(bs);
		
		Pattern pattern = Pattern.compile("[\\s]*<select[\\s]*name[\\s]*=[\\s]*\"" + bean.getName() + "\"[\\s\\S]*?</select>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			str = matcher.group();
		}
		
		String testxml = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n";
		testxml += "<popupSelect>";
		testxml += str;
		testxml += "\n</popupSelect>";
		
		request.setAttribute("msg", testxml);
		return getForward(request, mapping, "blank");
	}
	protected ActionForward popupImport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String popupName = request.getParameter("popupName");
		String popupWin = request.getParameter("popupWin");
		request.setAttribute("popupWin", popupWin);

		String xml = request.getParameter("xml");
		
		ArrayList fields = new ArrayList();
		PopupSelectBean bean = BaseEnv.popupSelectMap.get(popupName);
		String path = bean.getPath();
		
		request.setAttribute("msg", "OK");
		try {
			popupSave(popupName, bean, xml);
		} catch (Exception ex) {
			BaseEnv.log.error("CustomizeAction 修改弹出窗测试结果",ex);
			request.setAttribute("msg", ex.getMessage());
		}
		
		return getForward(request, mapping, "blank");
	}
	private void popupSave(String popupName,PopupSelectBean bean,  String xml) throws Exception{
		//检查文件路径是否只读
		File testFile = new File("testPopupSelect.xml");
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(testFile));
		dos.write(xml.getBytes("gb2312"));
		dos.close();
		
		HashMap tMap =  new HashMap();
		HashMap tPathMap =  new HashMap();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document document = dbf.newDocumentBuilder().parse(testFile);
			Node config = document.getDocumentElement();
			NodeList nodeList = config.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node tempNode = nodeList.item(i);
				if (tempNode.getNodeName().equalsIgnoreCase("select")) {
					PopupSelectBean.parse(tempNode, tMap,tPathMap, testFile.getAbsolutePath());
				}
			}
			//   BaseEnv.log.debug("-----------PopSelectConfig parseFile " + fileName + " Successful----------------");
		} catch (Exception ex) {
			BaseEnv.log.error("CustomizeAction 修改弹出窗测试结果",ex);
			throw new Exception("修改失败"+ex.getMessage());
		}
		if(tMap.size() ==0){
			throw new Exception("修改失败,解释不成功");
		}
		
		String path = BaseEnv.DEFINEPATH + "\\popupSelect\\" + popupName + ".xml";

		String errorMsg = null;
		boolean createXml = true;
		if (bean != null) {
			//这是修改弹出窗，如果在开发环境下,或文件本身就在自定义目录下。
			if (SystemState.instance.develope || bean.getPath().indexOf("user_config") > -1) {
				path = bean.getPath();
				createXml = false; //修改文件，
			} else {
				//客户环境下
				path = "../../user_config/popupSelect/" + popupName + ".xml";
			}
			File file = new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if (file.exists()) {
				if (!file.canWrite()) {
					throw new Exception("文件只读，不能修改"+file.getAbsolutePath());
				}
			}
		} else {
			if (!SystemState.instance.develope) {
				//客户环境下
				path = "../../user_config/popupSelect/" + popupName + ".xml";
			}
		}
		
		File file = new File(path);
		if(!file.exists()){
			file.getParentFile().mkdirs();
		}
		String fileStr = "";
		if (!createXml) {
			//修改
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			int count = dis.available();
			byte[] bs = new byte[count];
			dis.readFully(bs);
			dis.close();
			fileStr = new String(bs, "gb2312");
			xml = xml.substring(xml.indexOf("<select"),xml.indexOf("</select>")+"</select>".length());
			fileStr = fileStr.replaceAll("[\\s]*<select[\\s]*name[\\s]*=[\\s]*\"" + bean.getName() + "\"[\\s\\S]*?</select>", xml);
		} else {
			//新加
			fileStr += xml;
		}
		
		dos = new DataOutputStream(new FileOutputStream(file));
		dos.write(fileStr.getBytes("gb2312"));
		dos.close();
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document document = dbf.newDocumentBuilder().parse(file);
			Node config = document.getDocumentElement();
			NodeList nodeList = config.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node tempNode = nodeList.item(i);
				if (tempNode.getNodeName().equalsIgnoreCase("select")) {
					PopupSelectBean.parse(tempNode, BaseEnv.popupSelectMap,tPathMap, file.getAbsolutePath());
				}
			}
			//   BaseEnv.log.debug("-----------PopSelectConfig parseFile " + fileName + " Successful----------------");
		} catch (Exception ex) {
			BaseEnv.log.error("CustomizeAction 修改弹出窗结果",ex);
			throw new Exception("修改失败"+ex.getMessage());
		}
	}

	/**
	 * 修改弹出窗
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward popupUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String popupName = request.getParameter("popupName");
		String popupWin = request.getParameter("popupWin");
		request.setAttribute("popupWin", popupWin);
		String copy = request.getParameter("copy");
		request.setAttribute("copy", copy);
		
		ArrayList fields = new ArrayList();
		PopupSelectBean bean = BaseEnv.popupSelectMap.get(popupName);

		if ("true".equals(request.getParameter("exec"))) { //执行修改动作
			
			//读取客户端数据
			String xml = "\n\t<select name=\"" + request.getParameter("popupName") + "\" ";
			if ("checkBox".equals(request.getParameter("checkBox"))) {//多选类型
				xml += " type=\"checkBox\" ";
			}
			if ("defNoShow".equals(request.getParameter("showType"))) { //默认无条件不显示
				xml += " showType=\"defNoShow\" ";
			}
			String version = request.getParameter("version"); //版本
			if (version != null && version.length() > 0) {
				xml += " version=\"" + version + "\" ";
			}
			String desc = request.getParameter("desc"); //描述
			if (desc != null && desc.length() > 0) {
				xml += " desc=\"" + encode(desc) + "\" ";
			}
			String belongTableName = request.getParameter("belongTableName"); //所属表名
			if (belongTableName != null && belongTableName.trim().length() > 0) {
				xml += " belongTableName=\"" + encode(belongTableName) + "\" ";
			}
			xml += "> \n";
			if ("true".equals(request.getParameter("saveParentFlag"))) { //分级时可选择目录
				xml += "\t\t<saveParentFlag>true</saveParentFlag>\n";
			}
			String classCode = request.getParameter("classCode"); //分级字段
			if (classCode != null && classCode.trim().length() > 0) {
				String classSysType = request.getParameter("classSysType");
				if(classSysType != null && classSysType.length() > 0){
					xml += "\t\t<classCode classSysType=\""+classSysType+"\">" + encode(classCode.trim()) + "</classCode>\n";
				}else{
					xml += "\t\t<classCode>" + encode(classCode.trim()) + "</classCode>\n";
				}
			}
			String defParentCode = request.getParameter("defParentCode"); //默认父级
			if (defParentCode != null && defParentCode.trim().length() > 0) {
				xml += "\t\t<defParentCode>" + encode(defParentCode.trim()) + "</defParentCode>\n";
			}
			String orderBy = request.getParameter("orderBy"); //排序
			if (orderBy != null && orderBy.trim().length() > 0) {
				xml += "\t\t<orderBy>" + encode(orderBy.trim()) + "</orderBy>\n";
			}
			String topPopup = request.getParameter("topPopup"); //父级弹出窗
			if (topPopup != null && topPopup.trim().length() > 0) {
				xml += "\t\t<topPopup>" + encode(topPopup.trim()) + "</topPopup>\n";
			}
			String hasChild = request.getParameter("hasChild"); //子弹出窗
			if (hasChild != null && hasChild.trim().length() > 0) {
				xml += "\t\t<hasChild>" + encode(hasChild.trim()) + "</hasChild>\n";
			}
			String forwardModel = request.getParameter("forwardModel"); //快速添加
			if (forwardModel != null && forwardModel.trim().length() > 0) {
				xml += "\t\t<forwardModel>" + encode(forwardModel.trim()) + "</forwardModel>\n";
			}

			String defineSQL = request.getParameter("defineSQL"); //sql
			if (defineSQL != null && defineSQL.trim().length() > 0) {
				xml += "\t\t<defineSQL>\n" + encode(defineSQL.trim()) + "\n\t\t</defineSQL>\n";
			}
			String changeCond = request.getParameter("changeCond"); //sql
			if (changeCond != null && changeCond.trim().length() > 0) {
				xml += "\t\t<changeCond>" + encode(changeCond.trim()) + "</changeCond>\n";
			}
			String displayxml = "";
			String savexml = "";
			String fieldNames[] = request.getParameterValues("fieldName");
			String fTypes[] = request.getParameterValues("fType");
			String asNames[] = request.getParameterValues("asName");
			String sysTypes[] = request.getParameterValues("sysType");
			String parentNames[] = request.getParameterValues("parentName");
			boolean[] hiddens = setCBVal(fieldNames.length, "hiddfd", request);
			boolean[] compares = setCBVal(fieldNames.length, "compare", request);
			boolean[] relationKeys = setCBVal(fieldNames.length, "relationKey", request);
			String displays[] = request.getParameterValues("display");
			boolean[] parentDisplays = setCBVal(fieldNames.length, "parentDisplay", request);
			String widths[] = request.getParameterValues("width");
			String searchTypes[] = request.getParameterValues("searchType");
			String defaultValues[] = request.getParameterValues("defaultValue");
			boolean[] keySearchs = setCBVal(fieldNames.length, "keySearch", request);
			boolean[] hiddenInputs = setCBVal(fieldNames.length, "hiddenInput", request);
			String inputTypes[] = request.getParameterValues("inputType");
			String inputValues[] = request.getParameterValues("inputValue");
			String popSelects[] = request.getParameterValues("popSelect");

			for (int i = 0; i < fieldNames.length; i++) {
				if (fieldNames[i].trim().length() > 0) {
					String line = "\t\t\t<field";
					line += " name=\"" + encode(fieldNames[i]) + "\"";
					if (asNames[i] != null && asNames[i].trim().length() > 0) {
						line += " asName=\"" + encode(asNames[i]) + "\"";
					}
					if (sysTypes[i] != null && sysTypes[i].trim().length() > 0) {
						line += " sysType=\"" + sysTypes[i] + "\"";
					}
					if (parentNames[i] != null && parentNames[i].trim().length() > 0) {
						line += " parentName=\"" + encode(parentNames[i]) + "\"";
					}
					if (hiddens[i]) {
						line += " hidden=\"true\"";
					}
					if (fTypes[i].equals("1") && (!compares[i])) {
						line += " compare=\"false\"";
					}
					if (relationKeys[i]) {
						line += " relationKey=\"true\"";
					}
					if (displays[i] != null && displays[i].trim().length() > 0) {
						line += " display=\"" + encode(displays[i]) + "\"";
					}
					if (parentDisplays[i]) {
						line += " parentDisplay=\"true\"";
					} else if (fTypes[i].equals("0")) {
						line += " parentDisplay=\"false\"";
					}
					if (widths[i] != null && widths[i].trim().length() > 0) {
						line += " width=\"" + widths[i] + "\"";
					}
					if (searchTypes[i] != null && searchTypes[i].trim().length() > 0) {
						line += " searchType=\"" + searchTypes[i] + "\"";
					}
					if (defaultValues[i] != null && defaultValues[i].trim().length() > 0) {
						line += " defaultValue=\"" + encode(defaultValues[i]) + "\"";
					}
					if (keySearchs[i]) {
						line += " keySearch=\"true\"";
					}
					if (hiddenInputs[i]) {
						line += " hiddenInput=\"true\"";
					}
					if (inputTypes[i] != null && inputTypes[i].trim().length() > 0) {
						line += " inputType=\"" + inputTypes[i] + "\"";
					}
					if (inputValues[i] != null && inputValues[i].trim().length() > 0) {
						line += " inputValue=\"" + encode(inputValues[i]) + "\"";
					}
					if (i<popSelects.length && popSelects[i] != null && popSelects[i].trim().length() > 0) {
						line += " popSelect=\"" + encode(popSelects[i]) + "\"";
					}
					line += " />\n";
					if (fTypes[i].equals("0")) {
						displayxml += line;
					} else {
						savexml += line;
					}
				}
			}
			xml += "\t\t<displayFields>\n" + displayxml + "\t\t</displayFields>\n";
			xml += "\t\t<saveFields>\n" + savexml + "\t\t</saveFields>\n";
			xml += "\t</select>";

			
			String testxml = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n";
			testxml += "<popupSelect>";
			testxml += xml;
			testxml += "\n</popupSelect>";
			
			
			
			
			try {
				popupSave(popupName, bean, testxml);
			} catch (Exception ex) {
				BaseEnv.log.error("CustomizeAction 修改弹出窗测试结果",ex);
				EchoMessage.error().add(ex.getMessage()).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}

			EchoMessage.success().add("修改成功").setBackUrl(
					request.getRequestURI()+"?type=popupUpdate&popupName=" + popupName + "&popupWin=" + request.getParameter("popupWin") + "&popWinName=" + request.getParameter("popWinName"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}

		if (bean == null) {
			fields.add(new PopField());
		} else {
			request.setAttribute("bean", bean);
			String defineSQL = bean.getDefineSQL();
			//把所有非自定义语句的弹出窗全部转化成自定义的，简化处理，兼容老版
			if (defineSQL == null || defineSQL.length() == 0) {
				defineSQL = PopupSelectBean.tableTosql(bean.getTables(), bean.getRelation());
			} else {
				int fpos = PopupSelectBean.pos(defineSQL, "from");
				if (fpos == -1) {
				} else {
					defineSQL = defineSQL.substring(fpos);
				}
			}
			//把所有condition内容 转到defineSQL中，简化处理，兼容老版
			if (bean.getCondition() != null && bean.getCondition().trim().length() > 0) {
				int fpos = PopupSelectBean.pos(defineSQL, "where");
				if (fpos == -1) {
					if (defineSQL.contains("group by ")) {
						defineSQL += " having 1=1 and ";
					} else {
						defineSQL += " where 1=1  and ";
					}
					defineSQL += bean.getCondition();
				} else {
					defineSQL += " and " + bean.getCondition();
				}
			}
			request.setAttribute("defineSQL", defineSQL);

			fields.addAll(bean.getDisplayField3());
			fields.addAll(bean.getSaveFields3());
		}
		request.setAttribute("fields", fields);
		return getForward(request, mapping, "popupUpdate");
	}

	/**
	 * 修改前的准备
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward copyPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;

		String nstr = request.getParameter("keyId");
		
		if (nstr != null && nstr.length() != 0) {
			Result rs = mgt.getTableInfo(nstr);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS && ((List) rs.getRetVal()).size() == 0) {
				throw new BusinessException("表不存在");
			}
			List ls = (List) rs.getRetVal();
			DBTableInfoBean dib = (DBTableInfoBean) ls.get(0);
			KRLanguageQuery krQuery = new KRLanguageQuery();
			krQuery.addLanguageId(dib.getLanguageId());
			for (DBFieldInfoBean dfb : dib.getFieldInfos()) {
				krQuery.addLanguageId(dfb.getLanguageId());
				if (dfb.getGroupName() != null && dfb.getGroupName().length() > 0) {
					krQuery.addLanguageId(dfb.getGroupName());
				}
			}
			HashMap<String, KRLanguage> krLanguageMap = krQuery.query();
			dib.setDisplay(krLanguageMap.get(dib.getLanguageId()));
			for (DBFieldInfoBean dfb : dib.getFieldInfos()) {
				dfb.setDisplay(krLanguageMap.get(dfb.getLanguageId()));
				if (dfb.getGroupName() != null && dfb.getGroupName().length() > 0) {
					dfb.setGroupDisplay(krLanguageMap.get(dfb.getGroupName()));
				}
			}
			dib.setId("");
			String designId = request.getParameter("designId");
			if(designId != null){
				request.setAttribute("designId", designId);
			}
			request.setAttribute("result", dib);
			request.setAttribute("isCopy", "copy");
			request.setAttribute("tableType", dib.getTableType());

			if (false && request.getParameter("type") != null && request.getParameter("type").equals("simple")) {
				return getForward(request, mapping, "tableUpdateSimple");
			} else {
				forward = getForward(request, mapping, "tableMgt");
			}
		}
		request.setAttribute("operation", this.getOperation(request));
		return forward;
	}

	/**
	 * 修改前的准备
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;

		String nstr = request.getParameter("keyId");
		if (nstr != null && nstr.length() != 0) {
			Result rs = mgt.getTableInfo(nstr);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS && ((List) rs.getRetVal()).size() == 0) {
				throw new BusinessException("表不存在");
			}
			List ls = (List) rs.getRetVal();
			DBTableInfoBean dib = (DBTableInfoBean) ls.get(0);
			KRLanguageQuery krQuery = new KRLanguageQuery();
			krQuery.addLanguageId(dib.getLanguageId());
			for (DBFieldInfoBean dfb : dib.getFieldInfos()) {
				krQuery.addLanguageId(dfb.getLanguageId());
				if (dfb.getGroupName() != null && dfb.getGroupName().length() > 0) {
					krQuery.addLanguageId(dfb.getGroupName());
				}
			}
			HashMap<String, KRLanguage> krLanguageMap = krQuery.query();
			dib.setDisplay(krLanguageMap.get(dib.getLanguageId()));
			for (DBFieldInfoBean dfb : dib.getFieldInfos()) {
				dfb.setDisplay(krLanguageMap.get(dfb.getLanguageId()));
				if (dfb.getGroupName() != null && dfb.getGroupName().length() > 0) {
					dfb.setGroupDisplay(krLanguageMap.get(dfb.getGroupName()));
				}
				//对公式中的双引号进行单引号替换
				if(dfb.getCalculate() != null&& dfb.getCalculate().length() > 0){
					dfb.setCalculate(dfb.getCalculate().replace("\"", "'"));
				}
				if(dfb.getInputType()== DBFieldInfoBean.INPUT_MAIN_TABLE || dfb.getInputTypeOld()== DBFieldInfoBean.INPUT_MAIN_TABLE){
					PopupSelectBean pb = (PopupSelectBean)BaseEnv.popupSelectMap.get(dfb.getInputValue());
					String desc = pb==null?dfb.getInputValue():pb.getDesc();
					if(desc==null || desc.length() ==0) desc = dfb.getInputValue();
					dfb.setInputValue(dfb.getInputValue()+":"+desc);
				}
			}
			
			request.setAttribute("PopDisSentence", BaseEnv.popDisSentence.get(nstr));

			request.setAttribute("result", dib);
			request.setAttribute("tableType", dib.getTableType());

			rs = mgt.getChildInfo(nstr, request.getLocale().toString());
			request.setAttribute("childList", rs.getRetVal());
			rs = mgt.getBrotherInfo(nstr, request.getLocale().toString());
			request.setAttribute("brotherList", rs.getRetVal());

			//修改时，只看表名，以Flow_开头的表都是OA工作流表，都直接进入简单版修改界面
			//request.setAttribute("ftableType", request.getParameter("type"));
			if(!"FlowNwaichushenqingdan2".equals(dib.getTableName())&&!"FlowNqingjiadan2".equals(dib.getTableName())){
				request.setAttribute("ftableType", dib.getTableName().startsWith("Flow")?"simple":"");
			}			
			//if (request.getParameter("type") != null && request.getParameter("type").equals("simple")) {
			if (false && dib.getTableName().startsWith("Flow_")) {
				return getForward(request, mapping, "simpleTableMgt");
			} else {
				forward = getForward(request, mapping, "tableMgt");
			}
		}
		request.setAttribute("operation", this.getOperation(request));
		return forward;
	}

	/**
	 * 显示控制修改前的准备
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward tableViewPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		String nstr = request.getParameter("keyId");
		if (nstr != null && nstr.length() != 0) {
			Object o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			Hashtable tab = (Hashtable) o;
			DBTableInfoBean tib = DDLOperation.getTableInfo(tab, nstr);
			ArrayList fieldList = new ArrayList();
			for (int i = 0; i < tib.getFieldInfos().size(); i++) {
				DBFieldInfoBean field = (DBFieldInfoBean) tib.getFieldInfos().get(i);

				if (field.getInputType() == DBFieldInfoBean.INPUT_NORMAL || field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
					if ( //!field.getFieldName().equals("id") &&
					!field.getFieldName().equals("f_ref") && !field.getFieldName().equals("f_brother")) {
						Object fos[] = new Object[8];
						fos[0] = field.getTableBean().getId();
						fos[1] = field.getId();
						fos[2] = field.getTableBean().getTableName();
						fos[3] = field.getFieldName();
						fos[4] = field.getTableBean().getDisplay() == null ? field.getTableBean().getTableName() : field.getTableBean().getDisplay().get(getLocale(request).toString());
						if (fos[4] == null || fos[4].toString().length() == 0) {
							fos[4] = field.getTableBean().getTableName();
						}
						Object fd = field.getDisplay() == null ? field.getFieldName() : field.getDisplay().get(getLocale(request).toString());
						fos[5] = fd == null || fd.toString().trim().length() == 0 ? field.getFieldName() : fd;
						fos[6] = field.getFieldName(); //关联字段
						fos[7] = ""; //关联字段

						fieldList.add(fos);
					}
				} else if (field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
					if (field.getSelectBean() != null && DDLOperation.getTableInfo(tab, field.getSelectBean().getRelationTable()) != null) {
						List temp = DDLOperation.getTableInfo(tab, field.getSelectBean().getRelationTable()).getFieldInfos();
						for (int j = 0; j < temp.size(); j++) {
							DBFieldInfoBean tempField = (DBFieldInfoBean) temp.get(j);
							if ( //!tempField.getFieldName().equals("id") &&
							!tempField.getFieldName().equals("f_ref") && !tempField.getFieldName().equals("f_brother")) {
								Object fos[] = new Object[8];
								fos[0] = tempField.getTableBean().getId();
								fos[1] = tempField.getId();
								fos[2] = tempField.getTableBean().getTableName();
								fos[3] = tempField.getFieldName();
								fos[4] = tempField.getTableBean().getDisplay() == null ? "" : tempField.getTableBean().getDisplay().get(getLocale(request).toString());
								if (fos[4] == null || fos[4].toString().length() == 0) {
									fos[4] = tempField.getTableBean().getTableName();
								}
								Object fd = tempField.getDisplay() == null ? tempField.getFieldName() : tempField.getDisplay().get(getLocale(request).toString());
								/**
								 * 这里要判断InputType的主表录入是否变化
								 * 如果变化要删除tableView中的相关数据，否则会报错
								 */
								fos[5] = fd == null || fd.toString().trim().length() == 0 ? tempField.getFieldName() : fd;
								fos[6] = field.getFieldName(); //关联字段
								fd = field.getDisplay() == null ? field.getFieldName() : field.getDisplay().get(getLocale(request).toString());
								fos[7] = fd == null || fd.toString().trim().length() == 0 ? field.getFieldName() : fd;

								fieldList.add(fos);
							}
						}
					}
				}
			}
			o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_VIEW_INFO);
			Hashtable tv = (Hashtable) o;
			Object lv = tv.get(nstr);
			request.setAttribute("tableName", nstr);
			request.setAttribute("allFields", fieldList);

			if (lv != null) {
				request.setAttribute("tableView", lv);
			}
			forward = getForward(request, mapping, "tableView");
		}
		return forward;
	}

	/**
	 * 修改
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = null;
		//原数据
		String tableName = getParameter("tableName", request);

		Result rs = mgt.getTableInfo(tableName);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS && ((List) rs.getRetVal()).size() == 0) {
			throw new BusinessException("表不存在");
		}
		List ls = (List) rs.getRetVal();
		DBTableInfoBean oldInfo = (DBTableInfoBean) ls.get(0);
		KRLanguageQuery krQuery = new KRLanguageQuery();
		krQuery.addLanguageId(oldInfo.getLanguageId());
		for (DBFieldInfoBean dfb : oldInfo.getFieldInfos()) {
			krQuery.addLanguageId(dfb.getLanguageId());
			if (dfb.getGroupName() != null && dfb.getGroupName().length() > 0) {
				krQuery.addLanguageId(dfb.getGroupName());
			}
		}
		HashMap<String, KRLanguage> krLanguageMap = krQuery.query();
		oldInfo.setDisplay(krLanguageMap.get(oldInfo.getLanguageId()));
		for (DBFieldInfoBean dfb : oldInfo.getFieldInfos()) {
			dfb.setDisplay(krLanguageMap.get(dfb.getLanguageId()));
			if (dfb.getGroupName() != null && dfb.getGroupName().length() > 0) {
				dfb.setGroupDisplay(krLanguageMap.get(dfb.getGroupName()));
			}
		}

		//取主表数据
		DBTableInfoBean tableInfo = new DBTableInfoBean();

		//不可以修改的字段
		tableInfo.setUpdateAble(oldInfo.getUpdateAble());
		tableInfo.setUdType(oldInfo.getUdType());
		tableInfo.setCreateBy(oldInfo.getCreateBy());
		tableInfo.setCreateTime(oldInfo.getCreateTime());
		tableInfo.setLastUpdateBy(getLoginBean(request).getId());
		tableInfo.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));

		tableInfo.setId(getParameter("tableId", request));

		tableInfo.setIsUsed(getParameterInt("isUsed", request));
		tableInfo.setFieldCalculate(getParameter("fieldCalculate", request));
		tableInfo.setCopyParent(getParameterInt("copyParent", request));
		//明细表设置显示行数
		tableInfo.setDefRowCount(getParameterInt("defRowCount", request));
		tableInfo.setBrotherType(getParameterInt("brotherType", request));
		tableInfo.setTWidth(getParameterInt("tWidth", request));
		tableInfo.setTHeight(getParameterInt("tHeight", request));
		tableInfo.setExtendButton(getParameter("extendButton", request));//扩展按钮
		tableInfo.setIsSunCmpShare(getParameterInt("isSunCmpShare", request));//      设置是否分支机构共享
		tableInfo.setIsBaseInfo(getParameterInt("isBaseInfo", request));//设置是否基础信息
		tableInfo.setNeedsCopy(getParameterInt("needsCopy", request));//设置是否需要复制
		tableInfo.setWakeUp(getParameterInt("wakeUp", request));//是否支持提醒 
		tableInfo.setHasNext(getParameterInt("hasNext", request));//是否支持上下篇
		tableInfo.setIsView(getParameterInt("isView", request));//是否是视图
		tableInfo.setIsLayout(getParameterInt("isLayout", request));
		tableInfo.setReAudit(getParameterInt("reAudit", request));
		//tableInfo.setLayoutHTML(request.getParameter("layoutHTML"));
		tableInfo.setTableSysType(request.getParameter("tableSysType"));//系统类型
		tableInfo.setRelationTable(request.getParameter("relationTable"));//视图对应的表
		tableInfo.setClassFlag((byte) getParameterInt("classFlag", request));//支持分类标志
		tableInfo.setClassCount(getParameterInt("classCount", request));//类别级数
		tableInfo.setApproveFlow(getParameter("approveFlow", request));//审核流程
		tableInfo.setDraftFlag((byte) getParameterInt("draftFlag", request));//能否存为草稿
		tableInfo.setTableType((byte) getParameterInt("tableType", request));
		tableInfo.setTableName(tableName);
		tableInfo.setSysParameter(getParameter("sysParameter", request));
		tableInfo.setTriggerExpress(getParameterInt("triggerExpress", request));
		tableInfo.setPerantTableName(getParameter("perantTableName", request));
		if(tableInfo.getTableType() != 0 && !tableInfo.getPerantTableName().endsWith(";")){
			tableInfo.setPerantTableName(tableInfo.getPerantTableName() + ";");
		}

		tableInfo.setTableDesc(request.getParameter("tableDesc"));
		tableInfo.setMainModule(request.getParameter("MainModule"));
		String mainDisplay = getParameter("tableDisplay", request);
		tableInfo.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
				org.apache.struts.Globals.LOCALE_KEY), mainDisplay));
		tableInfo.getDisplay().setId(oldInfo.getLanguageId());
		tableInfo.setLanguageId(oldInfo.getLanguageId()); //不要更换多语言的代号
		tableInfo.setFieldInfos(new ArrayList());

		tableInfo.setIsNull(getParameterInt("tableIsNull", request));

		//取字段数据
		String[] fieldDisplays = getParameters("fieldDisplay", request); //多语言，工作流不隐藏
		String[] isNulls = getParameters("isNull", request); //是否为空，工作流不隐藏
		String[] maxLengths = getParameters("maxLength", request); //最大长度，工作流不隐藏
		String[] defaultValues = getParameters("defaultValue", request); //默认值，工作流不隐藏
		String[] listOrders = getParameters("listOrder", request); //排序，工作流不隐藏
		String[] fieldTypes = getParameters("fieldType", request); //字段类型，工作流不隐藏
		String[] fieldId = getParameters("fieldId", request); //字段ID，工作流隐藏
		String[] languageIds = getParameters("languageId", request); //字段ID，工作流隐藏
		String[] inputTypes = getParameters("inputType", request); //输入类型，工作流不隐藏
		String[] inputTypeOlds = getParameters("inputTypeOld", request); //输入类型，工作流不隐藏		
		String[] refEnumerationNames = getParameters("refEnumerationName", request); //枚举，工作流不隐藏
		String[] inputValues = getParameters("inputValue", request); //录入，工作流不隐藏
		String[] digitses = getParameters("digits", request); //小数位，工作流不隐藏
		String[] fieldNames = getParameters("fieldName", request); //字段名，工作流不隐藏
		String[] widths = getParameters("width", request); //输入宽度，工作流不隐藏
		String[] calculates = getParameters("calculate", request); //计算公式，工作流隐藏
		String[] isUniques = getParameters("isUnique", request); //是否唯一，工作流不隐藏
		String[] isStats = getParameters("isStat", request); //是否统计，工作流隐藏
		String[] fieldSysType = getParameters("fieldSysType", request); //系统类型，工作流隐藏
		String[] fieldIdentityStr = getParameters("fieldIdentityStr", request); //字段标识，工作流不隐藏
		String[] logicValidate = getParameters("logicValidate", request); //逻辑验证，工作流不隐藏
		String[] popupType = getParameters("popupType", request); //弹出框方式，工作流隐藏
		String[] groupName = getParameters("groupName", request); //分组，工作流隐藏
		String[] groupLanguageIds = getParameters("groupLanguageId", request); //分组，工作流隐藏
		String[] insertTable = getParameters("insertTable", request); //自动插入表，工作流隐藏
		String[] isCopys = getParameters("isCopy", request); //是否单据复制时，字段是否复制，工作流隐藏
		String[] copyType = getParameters("copyType", request); //复制上一行 标识
		String[] isReAudit = getParameters("isReAudit", request); //复核
		String[] isLog = getParameters("isLog", request); //记录日志
		String[] isMobiles = getParameters("isMobile", request); //是否单据复制时，字段是否复制，工作流隐藏
		forward = getForward(request, mapping, "alert");

		// 去除undefined数据，把undefined设为""值 多语言和字段名称过滤
		// fieldDisplays = GlobalsTool.justStr(fieldDisplays);
		isNulls = GlobalsTool.justStr(isNulls);
		maxLengths = GlobalsTool.justStr(maxLengths);
		defaultValues = GlobalsTool.justStr(defaultValues);
		listOrders = GlobalsTool.justStr(listOrders);
		fieldTypes = GlobalsTool.justStr(fieldTypes);
		fieldId = GlobalsTool.justStr(fieldId);
		inputTypes = GlobalsTool.justStr(inputTypes);
		refEnumerationNames = GlobalsTool.justStr(refEnumerationNames);
		inputValues = GlobalsTool.justStr(inputValues);
		digitses = GlobalsTool.justStr(digitses);
		// fieldNames = GlobalsTool.justStr(fieldNames);
		widths = GlobalsTool.justStr(widths);
		calculates = GlobalsTool.justStr(calculates);
		isUniques = GlobalsTool.justStr(isUniques);
		isStats = GlobalsTool.justStr(isStats);
		fieldSysType = GlobalsTool.justStr(fieldSysType);
		fieldIdentityStr = GlobalsTool.justStr(fieldIdentityStr);
		logicValidate = GlobalsTool.justStr(logicValidate);
		popupType = GlobalsTool.justStr(popupType);
		groupName = GlobalsTool.justStr(groupName);
		insertTable = GlobalsTool.justStr(insertTable);

		List<String> fieldNameTemp = new ArrayList<String>();
		for (int i = 0; i < fieldNames.length; i++) {
			if (fieldNames[i] != null && fieldNames[i].trim().length() > 0) {
				DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
				boolean isNew = false;
				if (fieldId[i] != null && fieldId[i].length() != 0) {
					fieldInfo.setId((fieldId[i]));
				} else {
					isNew = true;
					fieldInfo.setId(IDGenerater.getId());
				}
				fieldInfo.setTableBean(tableInfo);

				fieldInfo.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
						org.apache.struts.Globals.LOCALE_KEY), fieldDisplays[i]));
				fieldInfo.setLanguageId(fieldInfo.getDisplay().getId());
				if (!isNew && languageIds[i] != null && languageIds[i].length() > 0) {//老字段不能更新languageId
					fieldInfo.setLanguageId(languageIds[i]);
					fieldInfo.getDisplay().setId(languageIds[i]);
				}

				if (groupName[i] != null && !groupName[i].equals("")) {
					fieldInfo.setGroupDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
							org.apache.struts.Globals.LOCALE_KEY), groupName[i]));
					if (fieldInfo.getGroupDisplay().map.size() == 0) {
						fieldInfo.setGroupDisplay(null);
					} else {
						fieldInfo.setGroupName(fieldInfo.getGroupDisplay().getId());
						if (!isNew && groupLanguageIds[i] != null && groupLanguageIds[i].length() > 0) {
							fieldInfo.setGroupName(groupLanguageIds[i]);
							fieldInfo.getGroupDisplay().setId(groupLanguageIds[i]);
						}
					}
				}

				String fieldName = fieldNames[i];
				if (fieldName == null || fieldName.length() == 0) {
					fieldName = TableNameGenerate.getFieldName(i);
				}
				fieldInfo.setFieldName(fieldName);
				fieldInfo.setInputTypeOld((byte) paseInt(inputTypeOlds[i]));

				fieldInfo.setFieldType((byte) paseInt(fieldTypes[i]));
				fieldInfo.setDigits((byte) paseInt(digitses[i]));
				
				fieldInfo.setInputType((byte) paseInt(inputTypes[i]));
				if(fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_NO &&
						fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_HIDDEN&&
						fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_HIDDEN_INPUT&&
						fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_ONLY_READ){
					fieldInfo.setInputTypeOld(fieldInfo.getInputType());
				}
				//-------------------------------------------

				fieldInfo.setIsNull((byte) paseInt(isNulls[i]));
				fieldInfo.setListOrder((short) paseInt(listOrders[i]));
				fieldInfo.setMaxLength(paseInt(maxLengths[i]));
				fieldInfo.setWidth(paseInt(widths[i]));

				fieldInfo.setCalculate(calculates[i]==null?null:calculates[i].replace("'", "\""));
				fieldInfo.setIsUnique((byte) paseInt(isUniques[i]));
				fieldInfo.setIsStat((byte) paseInt(isStats[i]));
				fieldInfo.setDefaultValue(defaultValues[i]);
				fieldInfo.setFieldSysType(fieldSysType[i]);
				fieldInfo.setFieldIdentityStr(fieldIdentityStr[i]);
				fieldInfo.setLogicValidate(logicValidate[i]);
				fieldInfo.setPopupType(popupType[i]);
				fieldInfo.setInsertTable(insertTable[i]);
				fieldInfo.setIsCopy((byte) paseInt(isCopys[i]));
				fieldInfo.setIsMobile((byte) paseInt(isMobiles[i]));
				fieldInfo.setIsLog((byte) paseInt(isLog[i]));
				fieldInfo.setCopyType(copyType[i]);
				fieldInfo.setIsReAudit((byte) paseInt(isReAudit[i]));
				fieldInfo.setStatusId(paseInt(request.getParameter(fieldNames[i] + "_statusId")));

				fieldInfo.setInputValue("");
				fieldInfo.setRefEnumerationName("");
				if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_ENUMERATE)) {
					fieldInfo.setRefEnumerationName((refEnumerationNames[i].trim()));
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_DOWNLOAD_SELECT || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_DOWNLOAD_SELECT)) {
					fieldInfo.setInputValue((inputValues[i].trim()));
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE || fieldInfo.getInputType() == DBFieldInfoBean.INPUT_FUNCTION
						|| fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE || fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_FUNCTION) {
					String iv = inputValues[i].trim();
					if(iv.indexOf(":") > -1){
						iv = iv.substring(0,iv.indexOf(":"));
					}
					fieldInfo.setInputValue(iv);
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX)) {
					fieldInfo.setRefEnumerationName(refEnumerationNames[i].trim());
				} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_RADIO || (fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_RADIO)) {
					fieldInfo.setRefEnumerationName(refEnumerationNames[i].trim());
				}

				//检查系统配置是否启用
				if (fieldInfo.getFieldSysType() != null && fieldInfo.getFieldSysType().length() > 0) {
					SystemSettingBean ssBean = BaseEnv.systemSet.get(fieldInfo.getFieldSysType());
					if (ssBean != null) {
						if (!Boolean.parseBoolean(ssBean.getSetting()) && fieldInfo.getInputType() != DBFieldInfoBean.INPUT_HIDDEN && fieldInfo.getInputType() != DBFieldInfoBean.INPUT_NO) {
							fieldInfo.setInputType((byte) 3);
						}
					}
				}

				tableInfo.getFieldInfos().add(fieldInfo);
			}
		}
		


		if (tableInfo.getTableType() == DBTableInfoBean.MAIN_TABLE || tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE) {
			//寻找子表
			List childTableList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
			for (int i = 0; i < calculates.length; i++) {
				String cal = calculates[i];
				if (!cal.equals("")) {
					rs = this.checkMainTableCalculate(tableInfo, cal, childTableList, request);
					if (rs.getRetCode() == ErrorCanst.DEFAULT_FAILURE) {
						throw new BusinessException(this.getMessage(request, "tableinfo.update.calerror6", fieldNames[i]) + rs.getRetVal());
					}
				}
			}
		} else {
			List mainTables = new ArrayList();
			String[] parentNames = tableInfo.getPerantTableName().split(";");
			for (int i = 0; i < parentNames.length; i++) {
				DBTableInfoBean mainTableInfo = DDLOperation.getTableInfo(BaseEnv.tableInfos, parentNames[i]);
				mainTables.add(mainTableInfo);
			}
			for (int i = 0; i < calculates.length; i++) {
				String cal = calculates[i];
				if (!cal.equals("")) {
					rs = this.checkChildTableCalculate(tableInfo, cal, mainTables, request);
					if (rs.getRetCode() == ErrorCanst.DEFAULT_FAILURE) {
						throw new BusinessException(this.getMessage(request, "tableinfo.update.calerror6", fieldNames[i]) + rs.getRetVal());

					}
				}
			}
		}

		Hashtable tvs = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_VIEW_INFO);
		//获得data.txt路径
		String path = request.getSession().getServletContext().getRealPath("data.sql");

		DDLOperation ddlo = new DDLOperation();
		rs = ddlo.alter(oldInfo, tableInfo, tvs, path, this.getLocale(request).toString());

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//修改成功  
			String type = request.getParameter("type");
			if (type != null && type.equals("simple")) {
				EchoMessage.success().add(getMessage(request, "mrp.help.saveSucess")).setClose().setRefresh(true).setAlertRequest(request);
			} else {
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl(request.getRequestURI()+"?operation=7&keyId="+tableInfo.getTableName()+
						"&popWinName="+request.getParameter("popWinName")).setAlertRequest(request);
			}
			//保存 PopDisSentence BaseEnv.popDisSentence.get(tableName)
			String PopDisSentence = request.getParameter("PopDisSentence");
			ddlo.savePopDisSentence(tableName, PopDisSentence, path);
			
			//更新内存
			new InitMenData().initDBInformation(request.getSession().getServletContext(), tableInfo.getTableName());

			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), tableInfo.getDisplay().get(
					this.getLocale(request).toString()), "tblDBTableInfo", "表结构管理", "");
			if (rs.retVal != null) {
				ArrayList<String[]> logList = (ArrayList<String[]>) rs.retVal;
				for (String[] logrow : logList) {
					new DynDBManager()
							.addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), logrow[2], logrow[0], logrow[1], "");
				}
			}
		} else if (rs.retVal != null) {
			//修改失败
			EchoMessage.error().add(rs.retVal.toString()).setAlertRequest(request);
		} else {
			//修改失败
			EchoMessage.error().add(rs.retCode, request).setAlertRequest(request);
		}
		return forward;
	}

	/**
	 * 检查子表字段公式
	 * @param childTable
	 * @param cal
	 * @param mainTables
	 * @return
	 */
	private Result checkChildTableCalculate(DBTableInfoBean childTable, String cal, List mainTables, HttpServletRequest request) {
		Result rs = new Result();
		try {
			int index = cal.indexOf("{");
			while (cal.length() > 0 && index != -1) {
				int index1 = cal.indexOf("{");
				int index2 = cal.indexOf("}");
				String checkStr = cal.substring(index1 + 1, index2);
				BaseEnv.log.debug(checkStr);

				int mIndex = checkStr.indexOf("_");
				if (mIndex != -1) {
					String childTableName = checkStr.substring(0, mIndex);
					String fieldName = checkStr.substring(mIndex + 1);
					boolean childTableExist = false;
					if (!childTable.getTableName().equals(childTableName)) {
						DBTableInfoBean objTable = (DBTableInfoBean) BaseEnv.tableInfos.get(childTableName);
						DBTableInfoBean tableInfo = (DBTableInfoBean) mainTables.get(0);
						if (tableInfo.getTableName().equals(objTable.getPerantTableName().replaceAll(";", ""))) {
							childTable = objTable;
						}
					}
					if (childTable.getTableName().equals(childTableName)) {
						childTableExist = true;
						boolean childFieldExist = false;
						for (int i = 0; i < childTable.getFieldInfos().size(); i++) {
							DBFieldInfoBean fieldInfo = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
							if (fieldInfo.getFieldName().equals(fieldName)) {
								childFieldExist = true;
								break;
							}
						}
						if (!childFieldExist) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(this.getMessage(request, "tableinfo.update.calerror1", fieldName, childTableName));
							return rs;
						}
					}
					if (!childTableExist) {
						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						rs.setRetVal(checkStr + this.getMessage(request, "tableinfo.update.calerror5"));
						return rs;
					}
				} else {
					boolean mainFieldExist = false;
					for (int i = 0; i < mainTables.size(); i++) {
						DBTableInfoBean tableInfo = (DBTableInfoBean) mainTables.get(i);
						for (int j = 0; j < tableInfo.getFieldInfos().size(); j++) {
							DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(j);
							if (fieldInfo.getFieldName().equals(checkStr)) {
								mainFieldExist = true;
								break;
							}
						}
					}
					if (!mainFieldExist) {
						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						rs.setRetVal(this.getMessage(request, "tableinfo.update.calerror2", checkStr));
						return rs;
					}
				}
				cal = cal.substring(index2 + 1);
				index = cal.indexOf("{");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			rs.setRetVal(this.getMessage(request, "tableinfo.update.calerror4"));
		}
		return rs;
	}

	/**
	 * 检查主表字段公式
	 * @param mainTable
	 * @param cal
	 * @param childTables
	 * @return
	 */
	private Result checkMainTableCalculate(DBTableInfoBean mainTable, String cal, List childTables, HttpServletRequest request) {
		Result rs = new Result();
		try {
			int index = cal.indexOf("{");
			while (cal.length() > 0 && index != -1) {
				int index1 = cal.indexOf("{");
				int index2 = cal.indexOf("}");
				String checkStr = cal.substring(index1 + 1, index2);
				int mIndex = checkStr.indexOf("_");
				if (mIndex != -1) {//如果为子表字段
					String childTableName = checkStr.substring(0, mIndex);
					String fieldName = checkStr.substring(mIndex + 1);
					boolean childTableExist = false;
					for (int i = 0; i < childTables.size(); i++) {
						DBTableInfoBean childTable = (DBTableInfoBean) childTables.get(i);
						if (childTableName.equals(childTable.getTableName())) {
							childTableExist = true;
							boolean childFieldExist = false;
							for (int j = 0; j < childTable.getFieldInfos().size(); j++) {
								DBFieldInfoBean fieldInfo = (DBFieldInfoBean) childTable.getFieldInfos().get(j);
								if (fieldName.equals(fieldInfo.getFieldName())) {
									childFieldExist = true;
									break;
								}
							}
							if (!childFieldExist) {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								rs.setRetVal(this.getMessage(request, "tableinfo.update.calerror1", fieldName, childTableName));
								return rs;
							}
						}
					}
					if (!childTableExist) {
						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						rs.setRetVal(this.getMessage(request, "tableinfo.update.calerror3", childTableName));
						return rs;
					}
				} else {//如果为主表字段
					boolean mainFieldExist = false;
					for (int i = 0; i < mainTable.getFieldInfos().size(); i++) {
						DBFieldInfoBean fieldInfo = (DBFieldInfoBean) mainTable.getFieldInfos().get(i);
						if (fieldInfo.getFieldName().equals(checkStr)) {
							mainFieldExist = true;
							break;
						}
					}
					if (!mainFieldExist) {
						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						rs.setRetVal(this.getMessage(request, "tableinfo.update.calerror2", checkStr));
						return rs;
					}
				}
				cal = cal.substring(index2 + 1);
				index = cal.indexOf("{");
			}
		} catch (Exception e) {
			//  e.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			rs.setRetVal(this.getMessage(request, "tableinfo.update.calerror4"));
		}
		return rs;
	}

	/**
	 * 删除
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String search = request.getParameter("search") == null ? "" : request.getParameter("search");
		if (request.getParameter("LinkType") != null && request.getParameter("LinkType").equals("@URL:")) {
			search = new String(search.getBytes("iso-8859-1"), "UTF-8");
		}

		ActionForward forward = null;
		String nstr[] = request.getParameterValues("keyId");
		if (nstr != null && nstr.length != 0) {
			Object o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DDLOperation ddlo = new DDLOperation();

			Result rs = new Result();
			//获得data.txt路径
			String path = request.getSession().getServletContext().getRealPath("data.sql");
			ArrayList<DBTableInfoBean> delList = new ArrayList<DBTableInfoBean>();
			for (int i = 0; i < nstr.length; i++) {
				String tn = nstr[i];
				tn = new String(tn.getBytes("ISO8859-1"),"UTF-8");
				Hashtable tvs = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_VIEW_INFO);
				DBTableInfoBean tib = (DBTableInfoBean) ((Hashtable) o).get(tn);
				if(tib==null){
					continue;
				}
				delList.add(tib);
				String parentTableName = request.getParameter("parentTableName");
				if(tib.getTableType() == tib.BROTHER_TABLE && !tib.getPerantTableName().equals(parentTableName+";")){
					//对应多个主表
					rs = ddlo.deleteParentTable((Hashtable) o, parentTableName, tib, path);
				}else{
					rs = ddlo.drop((Hashtable) o, tn, tvs, path);
				}
				if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					delList.remove(tib);
				} else {

					//删除报表中相应的报表设置
					ReportSetMgt mgt = new ReportSetMgt();
					String locale = GlobalsTool.getLocale(request).toString();
					Result rs2 = mgt.getReportSetInfo(tn, locale);
					ReportsBean bean = (ReportsBean) rs2.getRetVal();
					if (bean != null) {
						String[] ids = new String[] { bean.getId() };
						mgt.delete(ids);
					}
				}
			}
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				//删除成功
				for (DBTableInfoBean tib : delList) {
					new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), tib.getDisplay().get(
							this.getLocale(request).toString()), "tblDBTableInfo", "表结构管理", "");
				}
				request.setAttribute("result", rs.retVal);
				//forward = query(mapping, form, request, response);
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/CustomQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex") + "&search=" + search)
						.setAlertRequest(request);
				if ("true".equals(request.getParameter("ajax"))) {
					request.setAttribute("msg", "OK:删除成功");
					return getForward(request, mapping, "blank");
				} else {
					return getForward(request, mapping, "alert");
				}
			} else {
				if ("true".equals(request.getParameter("ajax"))) {
					request.setAttribute("msg", "删除失败");
					return getForward(request, mapping, "blank");
				} else {
					//删除失败
					EchoMessage.error().add(rs.retCode, request).setBackUrl("/CustomQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex") + "&search=" + search).setRequest(request);
					forward = getForward(request, mapping, "message");
				}
			}
		}
		return forward;

	}

	/**
	 * 查询
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ArrayList list = new ArrayList();

		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String search = request.getParameter("search") == null ? "" : request.getParameter("search");
		if (request.getParameter("LinkType") != null && request.getParameter("LinkType").equals("@URL:")) {
			search = new String(search.getBytes("iso-8859-1"), "UTF-8");
		}
		String encode = java.net.URLEncoder.encode(search, "UTF-8");
		request.setAttribute("encode", encode);

		request.setAttribute("search", search);

		if (map != null) {
			Iterator it = map.values().iterator();
			while (it.hasNext()) {
				DBTableInfoBean tableInfo = (DBTableInfoBean) it.next();
				String usearch = search.toUpperCase();
				String utn = tableInfo.getTableName().toUpperCase();
				String display = tableInfo.getDisplay() == null ? "noDisplay" : tableInfo.getDisplay().get(getLocale(request).toString());
				display = display == null ? "noDisplay" : display.toUpperCase();
				if (("".equals(search) || utn.contains(usearch) || display.contains(search.toUpperCase()))) {
					Object[] os = new Object[] { tableInfo.getTableName(), tableInfo.getDisplay() == null ? "" : tableInfo.getDisplay().get(getLocale(request).toString()), tableInfo.getTableType(),
							tableInfo.getUdType(), tableInfo.getUpdateAble(), tableInfo.getPerantTableName(), "", tableInfo.getMainModule() };
					list.add(os);
				}
			}
			request.setAttribute("result", list);
		} else {
			//查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}

		return getForward(request, mapping, "tableList");
	}
	
	/**
	 * 明细
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward defineOp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Cookie[] coks = request.getCookies();
        for (int i = 0; i < coks.length; i++) {
            Cookie cok = coks[i];
            if (cok.getName().equals("JSESSIONID")) {
                request.setAttribute("JSESSIONID", cok.getValue());
                break;
            }
        }
		return getForward(request, mapping, "defineOp");
	}
	protected ActionForward defineUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String nstr = request.getParameter("id");
		DefineSQLBean sqlBean = (DefineSQLBean)BaseEnv.defineSqlMap.get(nstr);
		if("true".equals(request.getParameter("save"))){			
			String ret = request.getParameter("fileData");
			System.out.println(ret);
		}else{
			File file = new File(sqlBean.path);
			DataInputStream fis = new DataInputStream(new FileInputStream(file));
			int count = fis.available();
			byte[] bs = new byte[count];
			fis.readFully(bs);
			String str = new String(bs,"gb2312");
			str = str.replace("&", "&amp;");
			request.setAttribute("fileData", str);
		}
		
		return getForward(request, mapping, "defineUpdate");
	}
	
	protected ActionForward defineFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String nstr = request.getParameter("id");
		DefineSQLBean sqlBean = (DefineSQLBean)BaseEnv.defineSqlMap.get(nstr);
		String path = "";
		if(sqlBean != null){	
			path  = sqlBean.path;
			path = path.substring(path.lastIndexOf("..\\")+3);
		}
		request.setAttribute("msg", path);
		return getForward(request, mapping, "blank");
	}
	
	protected ActionForward newDefineFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String fileName = request.getParameter("fn");
		String cid= request.getParameter("cid");
		if (fileName == null || fileName.length() <= 0) {
			request.setAttribute("msg", "文件名不能为空");
			return getForward(request, mapping, "blank");
		}
		fileName = fileName.replace('\\', '/');
		String fn = fileName.substring(fileName.lastIndexOf("/")+1);
		File file = null;	
		if ("0".equals(SystemState.instance.dogId)) {
			//开发环境如果fileName带有路径
			if(fileName.indexOf("/") > 0){
				file = new File("../../"+fileName);
			}else{
				file = new File(BaseEnv.DEFINEPATH+"/define/"+fileName);
			}
		} else {
			//用户环境fileName带有路径
			if(fileName.indexOf("/") > 0){				
				file = new File("../../user_config/"+fn);
			}else{			
				file = new File("../../user_config/"+fileName   );
			}
		}
		if(file.exists()){
			//检查file文件是否可写
			request.setAttribute("msg", "文件已经存在，请改名");
			return getForward(request, mapping, "blank");
		}
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		

		try {
			OutputStream out = new FileOutputStream(file);
			String newdef = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\r\n<defineSqls>\r\n	<define name=\""+cid+"\">\r\n\r\n	</define>\r\n</defineSqls>";
			
			out.write(newdef.getBytes("GB2312"));
			out.close();
			
			BaseEnv.defineSqlFiles.add(file.getAbsolutePath());
			SqlConfig.parse(BaseEnv.defineSqlMap ,new HashMap(), file.getAbsolutePath());

			String msg = "添加自定义文件"+fileName;
			LoginBean user = this.getLoginBean(request);
			new DynDBManager().addLog(1, user.getId(), user.getName(), "00001",
					 msg, "", "自定义文件","");
			
			request.setAttribute("msg", "OK:"+file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("..\\..\\")+6));
			
		} catch (IOException ex) {
			request.setAttribute("msg", "文件已经存在，请改名");
			throw ex;
		}

		return getForward(request, mapping, "blank");
	}
	protected ActionForward getBillSQL(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tableName = request.getParameter("tableName");
		String reportType = request.getParameter("reportType");
		String locale= this.getLocale(request).toString();
		String userId= this.getLoginBean(request).getId();
		String path =request.getSession().getServletContext().getRealPath("Report.sql").toString(); //脚本备份路径
		Result rs = mgt.getBillSQL(tableName, locale, userId,reportType,path);
		HashMap msg = new HashMap();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			msg.put("CODE", "OK");
			msg.put("data", rs.retVal);
		}else{
			msg.put("CODE", "ERROR");
			msg.put("MSG", "执行错误"+rs.getRetVal());
		}
		request.setAttribute("msg", gson.toJson(msg));
		return getForward(request, mapping, "blank");
	}
	private void parseImportTableInfo(String fileName,HashMap<String,String> info) throws Exception{
		FileInputStream fis = new FileInputStream(fileName);
		ZipInputStream zipIn = new ZipInputStream(fis);
		FileOutputStream out=null;
		ZipEntry zipEntry = null; 
		try{
			String uniqueName = "";
	    	zipIn = new ZipInputStream(fis);
	        while ((zipEntry = zipIn.getNextEntry()) != null) {  
	            //如果是文件夹路径方式，本方法内暂时不提供操作  
	            if (!zipEntry.isDirectory()) {  
	                //如果是文件，则直接在对应路径下生成   
	                uniqueName = zipEntry.getName();  
	                String path = "";
	                String type = info.get(uniqueName);
	                
	                if(uniqueName.endsWith(".sql")){
	                	//执行脚本
	                	String sql ="";
	                	int b = 0;  
	                	byte[] bs=new byte[0];
	                	byte[] bt = new byte[1024];
	                    while ((b = zipIn.read(bt)) != -1){  
	                        byte temp[] = bs;
	                        bs = new byte[bs.length+b];
	                        System.arraycopy(temp, 0, bs, 0, temp.length);
	                        System.arraycopy(bt, 0, bs, temp.length, b);
	                    }  
	                    sql  = new String(bs,"GBK");
	                    BaseEnv.log.debug("导入模块sql="+sql);
	                	Result rs1 = CustomizeMgt.execTableInfo(sql);
	                	if(rs1.retCode !=ErrorCanst.DEFAULT_SUCCESS){
	                		throw new Exception("执行脚本错误："+rs1.getRetVal());
	                	}
	                	continue;
	                }else if(type==null){
	                	continue;
	                }else if(type.equals("define")){
	                	path = BaseEnv.FILESERVERPATH+"/../user_config/";
	                }else if(type.equals("popup")){
	                	path = BaseEnv.FILESERVERPATH+"/../user_config/popupSelect";
	                }else if(type.equals("report")){
	                	path = BaseEnv.FILESERVERPATH+"/../user_report_v7";
	                }else if(type.equals("reportFormat")){
	                	path = BaseEnv.FILESERVERPATH+"/userReport/";
	                }
	                if(path.length() > 0){
		                File file = new File(path,uniqueName);  
		                if(!file.getParentFile().exists()){
		                	file.getParentFile().mkdirs();
		                } 
		                out = new FileOutputStream(file);  
		                int b = 0;  
		                while ((b = zipIn.read()) != -1){  
		                    out.write(b);  
		                }  
		                out.close();  
	                }
	            }  
	        } 
		} catch(Exception ex){  
	    	throw new Exception(ex);
	    } 
        finally{  
            IOUtils.closeQuietly(zipIn);  
            IOUtils.closeQuietly(out);  
        }
	}
	private HashMap<String,String> getImportTableInfo(String fileName) throws Exception{
		if(!fileName.toLowerCase().endsWith(".zip")){
			throw new Exception("上传的文件不是zip格式");
		}
		HashMap<String,String> map = new HashMap<String,String>();
		FileInputStream fis = new FileInputStream("../temp/"+fileName);
		ZipInputStream zipIn = new ZipInputStream(fis);
		ZipEntry zipEntry = null; 
		String uniqueName = "";
		try{
	    	while ((zipEntry = zipIn.getNextEntry()) != null) {  
	            //如果是文件夹路径方式，本方法内暂时不提供操作  
	            if (!zipEntry.isDirectory()) {  
	                //如果是文件，则直接在对应路径下生成   
	                uniqueName = zipEntry.getName();  
	                String path = "";
	                if(uniqueName.equals("info.txt")){
	                	//执行脚本
	                	String info ="";
	                	int b = 0;  
	                	byte[] bs=new byte[0];
	                	byte[] bt = new byte[1024];
	                    while ((b = zipIn.read(bt)) != -1){  
	                        byte temp[] = bs;
	                        bs = new byte[bs.length+b];
	                        System.arraycopy(temp, 0, bs, 0, temp.length);
	                        System.arraycopy(bt, 0, bs, temp.length, b);
	                    }  
	                    info  = new String(bs,"GBK");
	                    String[] ss = info.split("\\n");
	                    for(String s:ss){
	                    	if(s.trim().length() > 0){
	                    		if(s.trim().indexOf("=")>-1){
	                    			String key = s.split("=")[0].trim();
	                    			String value=s.split("=")[1].trim();
	                    			if("tableName".equals(key)){
	                    				map.put(key, value);
	                    			}else{
	                    				map.put(value, key);
	                    			}
	                    		}
	                    	}
	                    }
	                	return map;
	                }
	            }  
	        }
		} catch(Exception ex){  
	    	throw new Exception(ex);
	    } 
	    finally{  
	        IOUtils.closeQuietly(zipIn);  
	    }
    	throw new Exception("上传的zip文件格式不正确未包含info.txt信息");
	}
	/**
	 * 导入表单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward importTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//导入模板名称
	    ImportForm importForm = (ImportForm) form;
	    String isConfirm = request.getParameter("isConfirm");
	    LoginBean login = (LoginBean) request.getSession().getAttribute("LoginBean");
	    
	    if("yes".equals(isConfirm)){
	        String zipFileName = request.getParameter("zipFileName");
	        ZipInputStream zipIn = null;  
	        try{  
	        	zipFileName = "../temp/"+zipFileName;
		    	HashMap<String,String> info = getImportTableInfo(zipFileName);
		    	
		    	parseImportTableInfo(zipFileName, info);
		    	
		    	new File(zipFileName).deleteOnExit();
		    	
	        } catch(Exception ex){  
	        	BaseEnv.log.error("ImportDataAction.importTableModule ",ex);
	        	throw new BusinessException("导入失败："+ex.getMessage(),"/vm/tableInfo/importTable.jsp?type=1");
	        }  
	          
	        EchoMessage.success().add("导入成功，请刷新内存后，给新模块授权，并重新登陆")
	                    			 .setClosePopWin("TableimportDiv").setNotAutoBack()
	                                 .setAlertRequest(request);
	        return getForward(request, mapping, "alert");
	    }else{
	        ZipInputStream zipIn = null;  
	        try{  
	        	//首次上传文件，先存入临时目录
		    	InputStream in = importForm.getFileName().getInputStream();
		    	String zipFileName = "../temp/"+importForm.getFileName().getFileName();
		    	FileOutputStream fos = new FileOutputStream(zipFileName);
	        	int b = 0;
		    	while((b=in.read())>-1){
		    		fos.write(b);
		    	}
		    	fos.close();
		    	HashMap<String,String> info = getImportTableInfo(zipFileName);
		    	String tableName = info.get("tableName");
		    	if(tableName==null||tableName.equals("")){
		    		throw new BusinessException("导入失败：文件包中未包含表名信息","/vm/tableInfo/importTable.jsp?type=1");
		    	}
		    	//检查表名是否存在
		    	if(BaseEnv.tableInfos.get(tableName)!=null){
		    		request.setAttribute("zipFileName", importForm.getFileName().getFileName());
		    		return getForward(request, mapping, "importTableConfirm");
		    	}
		    	parseImportTableInfo(zipFileName, info);
		    	
		    	new File(zipFileName).deleteOnExit();
		    	
	        } catch(Exception ex){  
	        	BaseEnv.log.error("ImportDataAction.importTableModule ",ex);
	        	throw new BusinessException("导入失败："+ex.getMessage(),"/vm/tableInfo/importTable.jsp?type=1");
	        }  
	          
	        EchoMessage.success().add("导入成功，请刷新内存后，给新模块授权，并重新登陆")
	                    			 .setClosePopWin("TableimportDiv").setNotAutoBack()
	                                 .setAlertRequest(request);
	        return getForward(request, mapping, "alert");
	    }
	}
	
	
	
	
	protected ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tableName = request.getParameter("tableName");
		LoginBean loginBean= this.getLoginBean(request);
		String mime = request.getSession().getServletContext().getMimeType("zip");
		String fileName = mgt.export(tableName, loginBean);
		
		String fn = fileName.indexOf("/")> -1?fileName.substring(fileName.lastIndexOf("/")+1):fileName;
		fn = fn.indexOf("\\")> -1?fn.substring(fn.lastIndexOf("\\")+1):fn;
    	if(mime == null || mime.length() == 0){
    		mime = "application/octet-stream; CHARSET=utf8";
    		response.setContentType(mime);
    		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
    	}else{
    		response.setContentType(mime);
    		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
    	}
    	
    	File existFile = new File(fileName);
    	if(!existFile.exists()){
    		BaseEnv.log.error("文件："+fileName+"-不存在");
    	}
        FileInputStream fis = new FileInputStream(fileName);
        response.setContentLength(fis.available());
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[20 * 1024];
        int i = 0;
        while ((i = fis.read(b)) > -1) {
        	out.write(b, 0, i);
        }
        fis.close();
		return null;
	}
	protected ActionForward getMenu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tableName = request.getParameter("tableName");
		String locale= this.getLocale(request).toString();
		String userId= this.getLoginBean(request).getId();
		String path =request.getSession().getServletContext().getRealPath("Report.sql").toString(); //脚本备份路径
		Result rs = mgt.getMenu(tableName, locale, userId,path);
		HashMap msg = new HashMap();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			msg.put("CODE", "OK");
			msg.put("id", rs.retVal);
		}else{
			msg.put("CODE", "ERROR");
			msg.put("MSG", "执行错误"+rs.getRetVal());
		}
		request.setAttribute("msg", gson.toJson(msg));
		return getForward(request, mapping, "blank");
	}
	protected ActionForward getReportId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tableName = request.getParameter("tableName");
		String reportType = request.getParameter("reportType");
		
		Result rs = mgt.getReportId(tableName, reportType);
		HashMap msg = new HashMap();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			if(((ArrayList<Object[]>)rs.retVal).size() >0){
				msg.put("CODE", "OK");
				msg.put("id", ((ArrayList<Object[]>)rs.retVal).get(0)[0]);
			}else{
				msg.put("CODE", "ERROR");
				msg.put("MSG", "请先进行单据打印数据源设计");
			}
		}else{
			msg.put("CODE", "ERROR");
			msg.put("MSG", "执行错误"+rs.getRetVal());
		}
		request.setAttribute("msg", gson.toJson(msg));
		return getForward(request, mapping, "blank");
	}
	
	protected ActionForward defineQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String keyword = request.getParameter("keyword");
		request.setAttribute("keyword", keyword);
		
		ArrayList oldList =  BaseEnv.defineSqlFiles;
		if(keyword != null && keyword.length() > 0){
			oldList = new ArrayList();
			HashMap map = BaseEnv.defineSqlMap;
			for(Object o :map.keySet()){
				if(o.toString().toLowerCase().indexOf(keyword.toLowerCase()) > -1){
					DefineSQLBean bean = (DefineSQLBean)map.get(o);
					oldList.add(bean.path);
				}
			}
		}
		
		ArrayList<String> list = new ArrayList<String>();
		for(Object o:oldList){
			String path = (String)o;
			if(path.indexOf("../") > -1){
				path  = path.substring(path.lastIndexOf("../")+3);
			}
			if(path.indexOf("..\\") > -1){
				path  = path.substring(path.lastIndexOf("..\\")+3);
			}
			
			if(path.startsWith("user_config\\")){
				//自定义文件夹下文件要去掉同名文件
				String fn = path.substring("user_config\\".length());
				for(int i=list.size()-1;i>=0;i--){
					if(list.get(i).endsWith(fn)){
						list.remove(i);
					}
				}
			}
			path = path.replace('/', '\\');
			list.add(path);
			
		}
		
		Cookie[] coks = request.getCookies();
        for (int i = 0; i < coks.length; i++) {
            Cookie cok = coks[i];
            if (cok.getName().equals("JSESSIONID")) {
                request.setAttribute("JSESSIONID", cok.getValue());
                break;
            }
        }
        request.setAttribute("defineName", request.getParameter("id"));
		request.setAttribute("from", request.getParameter("from"));
		request.setAttribute("files", list);
		return getForward(request, mapping, "defineQuery");
	}
	
	/**
	 * 明细
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward addBrother(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String nstr = request.getParameter("keyId");
		String tableName=request.getParameter("tableName");
		Result rs = mgt.setBrother(tableName, nstr);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", "OK:添加成功");
		}else{
			request.setAttribute("msg", "添加失败");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 明细
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward detail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String nstr = request.getParameter("keyId");
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		if (nstr != null && nstr.length() != 0) {
			//加载成功
			Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			request.setAttribute("result", DDLOperation.getTableInfo(map, nstr));
		}
		return getForward(request, mapping, "tableDetail");
	}

}
