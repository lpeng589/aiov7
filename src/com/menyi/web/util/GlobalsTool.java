package com.menyi.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.struts.util.MessageResources;
import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.tools.ViewTool;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sun.misc.BASE64Encoder;

import com.dbfactory.Result;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.MyWorkFlow;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.GoodsAttributeBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.RoleBean;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.alert.AlertDataBean;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.billNumber.BillNo.BillNoUnit;
import com.menyi.aio.web.bol88.AIOBOL88Bean;
import com.menyi.aio.web.bol88.AIOBOL88Mgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.moduleFlow.ModuleFlowMgt;
import com.menyi.aio.web.report.TableListResult;
import com.menyi.aio.web.userFunction.DefineJS;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * <p>
 * Title: 全局工具类
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: 周新宇
 * </p>
 * 
 * @author 周新宇
 * @version 1.0
 */
public class GlobalsTool implements ViewTool {

	public static ServletContext application;
	private HttpServletRequest request;
	private Locale locale;
	private MessageResources resources;
	public static Timer timer = new Timer();
	private HashMap opMap = new HashMap();
	private UserMgt userMgt = new UserMgt();
	private PublicMgt pubMgt = new PublicMgt();

	/**
	 * @author 陶智申 @ 定时发送器的语言选择
	 * 
	 */
	public static String getMessage(Locale loc, String key) {
		String msg = "";
		Object obj = application.getAttribute("userResource");
		if (obj instanceof MessageResources) {
			MessageResources messRes = (MessageResources) obj;
			msg = messRes.getMessage(loc, key);
		}
		if (msg == null || msg == "") {
			obj = application.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			if (obj instanceof MessageResources) {
				MessageResources messRes = (MessageResources) obj;
				msg = messRes.getMessage(loc, key);
			}
		}
		return msg;
	}

	public static boolean existsBrother(String tableName) {
		ArrayList list = DDLOperation.getBrotherTables(tableName, BaseEnv.tableInfos);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static int subtracter(int a, int b) {
		return a - b;
	}

	// 取集合中的集合
	public static List getList(Object obj) {
		return (List) obj;
	}

	// 判断两个字符串是否相等
	public static boolean isEqualObject(Object obj1, Object obj2) {
		if (obj1.equals(obj2)) {
			return true;
		}
		return false;
	}

	// 判断是否是奇数，如果是奇数返回true
	public static boolean isOddNumber(String num) {
		int n = Integer.parseInt(num);
		if (n % 2 == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static String add(String num1, String num2) {
		if (num1 == null || num1.length() == 0 || num2 == null || num2.length() == 0) {
			return "0";
		}
		String formatNumber;
		try {
			/* 取数1 和 数2 小数点最大位数 */
			int decimal = 0;
			if (num1.indexOf(".") != -1) {
				decimal = num1.substring(num1.indexOf(".") + 1, num1.length()).length();
			}
			if (num2.indexOf(".") != -1) {
				int decimal2 = num2.substring(num2.indexOf(".") + 1, num2.length()).length();
				if (decimal2 > decimal) {
					decimal = decimal2;
				}
			}

			java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(decimal);
			formatNumber = nf.format(new BigDecimal(num1).add(new BigDecimal(num2)).doubleValue());

		} catch (Exception ex) {
			ex.printStackTrace();
			formatNumber = "0";
		}
		return formatNumber;
	}

	public static boolean isOddNumber(int num) {
		if (num % 2 == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static String getMessage(Locale loc, String key, String param1) {
		String msg = "";
		Object obj = application.getAttribute("userResource");
		if (obj instanceof MessageResources) {
			MessageResources messRes = (MessageResources) obj;
			msg = messRes.getMessage(loc, key);
		}
		if (msg == null || msg == "") {
			obj = application.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			if (obj instanceof MessageResources) {
				MessageResources messRes = (MessageResources) obj;
				msg = messRes.getMessage(loc, key, param1);
			}
		}
		return msg;
	}

	public static int getVersion() {
		return BaseEnv.version;
	}

	public static String getMessage(Locale loc, String key, String param1, String param2) {
		String msg = "";
		Object obj = application.getAttribute("userResource");
		if (obj instanceof MessageResources) {
			MessageResources messRes = (MessageResources) obj;
			msg = messRes.getMessage(loc, key);
		}
		if (msg == null || msg == "") {
			obj = application.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			if (obj instanceof MessageResources) {
				MessageResources messRes = (MessageResources) obj;
				msg = messRes.getMessage(loc, key, param1, param2);
			}
		}
		return msg;
	}

	public static String getMessage(Locale loc, String key, String param1, String param2, String param3) {
		String msg = "";
		Object obj = application.getAttribute("userResource");
		if (obj instanceof MessageResources) {
			MessageResources messRes = (MessageResources) obj;
			msg = messRes.getMessage(loc, key);
		}
		if (msg == null || msg == "") {
			obj = application.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			if (obj instanceof MessageResources) {
				MessageResources messRes = (MessageResources) obj;
				msg = messRes.getMessage(loc, key, param1, param2, param3);
			}
		}
		return msg;
	}

	/**
	 * @author 陶智申 @ 定时发送器的语言选择
	 * @language:只給兩種語言：中文和英文
	 * 
	 */

	public static String getMessage(String language, String key) {
		String msg = "";
		Locale loc = null;
		loc = new Locale(language);
		msg = getMessage(loc, key);
		return msg;
	}

	public static String getMessage(String language, String key, String param1) {
		String msg = "";
		Locale loc = null;
		loc = new Locale(language);
		msg = getMessage(loc, key, param1);
		return msg;
	}

	public static String getMessage(String language, String key, String param1, String param2) {
		String msg = "";
		Locale loc = null;
		loc = new Locale(language);
		msg = getMessage(loc, key, param1, param2);
		return msg;
	}
	public static String getMessage(String language, String key, String param1, String param2, String param3) {
		String msg = "";
		Locale loc = null;
		loc = new Locale(language);
		msg = getMessage(loc, key, param1, param2,param3);
		return msg;
	}

	/**
	 * init
	 * 
	 * @param object
	 *            Object
	 * @todo Implement this org.apache.velocity.tools.view.tools.ViewTool method
	 */
	public void init(Object object) {
		if (!(object instanceof ViewContext)) {
			throw new IllegalArgumentException("Tool can only be initialized with a ViewContext");
		}

		ViewContext context = (ViewContext) object;
		this.request = context.getRequest();
		this.application = context.getServletContext();
		
		Object o = application.getAttribute("res.DynamicResource");
		if (o instanceof MessageResources) {
			resources = (MessageResources) o;
		}
		// 已做修改
		locale = (Locale) request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (locale == null) {
			locale = request.getLocale();
		}

	}

	public static boolean hasInit() {
		return application != null;
	}

	public String getSeqDis(String seqStr) {
		if (seqStr.trim().length() == 0) {
			return "";
		}
		String[] seqList = seqStr.split("~");
		return seqList[seqList.length - 1];
	}

	public String changeDefaultValue() {
		HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
		String retVal = "";
		String winCurIndex = request.getParameter("winCurIndex");
		if (winIndexMap != null && winCurIndex != null && winCurIndex.length() > 0) {
			if (winIndexMap.get(winCurIndex) != null) {
				String url = (String) winIndexMap.get(winCurIndex);
				if (url != null) {
					// 找到类似明细表的字段
					String[] ss = url.split("&");
					for (String s : ss) {
						if (s.indexOf("DV") > 0 && s.indexOf("_") > 0 && s.indexOf("=") > 0) {
							// changeDV(griddata,name,defaultValue)
							retVal += "changeDetailValue('" + s.substring(0, s.indexOf("=") - 2) + "','" + s.substring(s.indexOf("=") + 1) + "');\r\n";
						}
					}
				}
			}
		}
		if (retVal.length() > 0) {
			retVal = "function changeDetailV(){\r\n" + retVal + "\r\n}";
		}
		return retVal;
	}
	
	public String changeDefineInputType(String tableName,ArrayList<String> defineField){
		String js="";
		ArrayList childTableList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
		for (int i = 0; i < childTableList.size(); i++) {
			String cjs="";
			DBTableInfoBean childTab = (DBTableInfoBean) childTableList.get(i);
			String tabData = childTab.getTableName() + "Data";
			boolean hasField = false;
			cjs += "for(var i=0;i<" + tabData + ".cols.length;i++){\n";
			if(defineField != null && defineField.size() > 0){
				for(DBFieldInfoBean dfb : childTab.getFieldInfos()){
					boolean found = false;
					for(String s:defineField){
						String fn = s.substring(0,s.indexOf("=")).trim();
						String tp = s.substring(s.indexOf("=")+1).trim();
						if(fn.equalsIgnoreCase(childTab.getTableName() + "_" +dfb.getFieldName())||
								fn.equalsIgnoreCase(childTab.getTableName() + "." +dfb.getFieldName())){
							String cfn = childTab.getTableName() + "_" +dfb.getFieldName();
							if(tp.equalsIgnoreCase("ReadOnly")){
								hasField = true;
								cjs += "   if(" + tabData + ".cols[i].name==\"" + cfn + "\"&&" + tabData + ".cols[i].inputType!=-2){\n   ";
								cjs += tabData + ".cols[i].inputType=" + 8 + ";\n";// 。
								cjs += tabData + ".cols[i].inputTypeOld=" + dfb.getInputType() + ";\n";// 。
								cjs += "   }\n";
							}else if(tp.equalsIgnoreCase("Hidden")){
								hasField = true;
								cjs += "   if(" + tabData + ".cols[i].name==\"" + cfn + "\"&&" + tabData + ".cols[i].inputType!=-2){\n   ";
								cjs += tabData + ".cols[i].inputType=-2;\n";
								cjs += tabData + ".cols[i].width=0;\n";
								cjs += "   }\n";
							}else if(tp.equalsIgnoreCase("notNull")||tp.equalsIgnoreCase("noNull")){
								hasField = true;
								cjs += "   if(" + tabData + ".cols[i].name==\"" + cfn + "\"&&" + tabData + ".cols[i].inputType!=-2){\n   ";
								cjs += tabData + ".cols[i].nullable=false;\n";// 。
								cjs += "updateValidateItem('"+cfn+"',false);\n";// 。
								cjs += "   }\n";
							}
							break;
						}
					}
				}
			}
			cjs += "}";
			if (!hasField) {
				cjs = "";
			}
			js +=cjs;
		}
		return js;
	}

	public String changeDetInputType(String tableName, String nodeId, String mopId) {
		String js = "";
		try {
			if (BaseEnv.workFlowDesignBeans.get(mopId) != null) {
				WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(mopId);
				List<FieldBean> listField = designBean.getFlowNodeMap().get(nodeId).getFields();
				ArrayList childTableList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
				if (listField == null)
					return "";
				boolean hasField = false;
				for (int i = 0; i < childTableList.size(); i++) {
					DBTableInfoBean childTab = (DBTableInfoBean) childTableList.get(i);
					String tabData = childTab.getTableName() + "Data";
					js += "for(var i=0;i<" + tabData + ".cols.length;i++){\n";
					for (FieldBean flowField : listField) {
						String fieldName = flowField.getFieldName();
						if (fieldName.indexOf(childTab.getTableName() + "_") >= 0) {
							DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(BaseEnv.tableInfos, childTab.getTableName(), fieldName.substring(fieldName.indexOf("_") + 1));
							if (fieldBean == null)
								continue;
							if ((fieldBean.getInputType() == fieldBean.INPUT_MAIN_TABLE || (fieldBean.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldBean.getInputTypeOld() == fieldBean.INPUT_MAIN_TABLE))
									&& fieldBean.getSelectBean() != null) {
								ArrayList viewList = fieldBean.getSelectBean().getViewFields();
								if (viewList != null && viewList.size() == 0) {
									js += this.chDetInContent(tabData, fieldName, flowField, fieldBean);
								} else {
									for (int j = 0; j < viewList.size(); j++) {
										PopField popField = (PopField) viewList.get(j);
										if (!popField.hiddenInput.equals("true") || (popField.hiddenInput.equals("true") && existColConfig(childTab.getTableName(), popField.getAsName()))) {
											js += this.chDetInContent(tabData, childTab.getTableName() + "_" + GlobalsTool.getTableField(popField.asName), flowField, fieldBean);
										}
									}
								}
								if (flowField.isNotNull())//弹出窗必填
								{
									js += "updateValidateItem('"+fieldName+"',false);\n";// 。
								}
							} else if (fieldBean.getFieldName().equals("Seq")) {
								js += this.chDetInContent(tabData, childTab.getTableName() + "_Seq_hid", flowField, fieldBean);
							} else {
								js += this.chDetInContent(tabData, fieldName, flowField, fieldBean);
							}
							hasField = true;
						}
					}
					js += "}";
					if (!hasField) {
						js = "";
					}
				}
			}
		} catch (Exception e) {
			js = "";
		}
		return js;
	}

	private String chDetInContent(String tabData, String fieldName, FieldBean bean, DBFieldInfoBean fieldBean) {
		String js = "";
		js += "   if(" + tabData + ".cols[i].name==\"" + fieldName + "\"&&" + tabData + ".cols[i].inputType!=-2){\n   ";
		if (bean.getInputType() == DBFieldInfoBean.INPUT_HIDDEN) {
			js += tabData + ".cols[i].inputType=-2;\n";
			js += tabData + ".cols[i].width=0;\n";
		} else if (bean.getInputType() == 8)//
		{
			js += tabData + ".cols[i].inputType=" + 8 + ";\n";// 。
			js += tabData + ".cols[i].inputTypeOld=" + fieldBean.getInputTypeOld() + ";\n";// 。
		} else if (bean.isNotNull())//
		{
			js += tabData + ".cols[i].nullable=false;\n";// 。
			js += "updateValidateItem('"+fieldName+"',false);\n";// 。
		}
		js += "   }\n";
		return js;
	}

	public String getUrlBillDef(String fieldName) {
		String value = "";
		HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
		String winCurIndex = request.getParameter("winCurIndex");
		if (winIndexMap != null && winCurIndex != null && winCurIndex.length() > 0) {
			if (winIndexMap.get(winCurIndex) != null) {
				String url = (String) winIndexMap.get(winCurIndex);

				if (url != null && (url.indexOf("?" + fieldName + "=") >= 0 || url.indexOf("&" + fieldName + "=") >= 0)) {
					int valStart = url.indexOf(fieldName + "=") + fieldName.length() + 1;
					int valEnd = url.indexOf("&", valStart);

					if (valEnd > 0) {
						value = url.substring(valStart, valEnd);
					} else {
						value = url.substring(valStart);
					}
				}
			}
		}
		String forward = request.getParameter("forward");
		if ("add".equals(forward)) {
			String url = request.getQueryString();
			if (url != null && url.indexOf(fieldName + "=") >= 0) {
				int valStart = url.indexOf(fieldName + "=") + fieldName.length() + 1;
				int valEnd = url.indexOf("&", valStart);
				if (valEnd > 0) {
					value = url.substring(valStart, valEnd);
				} else {
					value = url.substring(valStart);
				}
			}
		}
		if (value == null)
			value = "";
		return value;
	}

	public static String getUrlBillDef(HttpServletRequest request, String fieldName) {
		String value = null;
		HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
		String winCurIndex = request.getParameter("winCurIndex");
		if (winIndexMap != null && winCurIndex != null && winCurIndex.length() > 0) {
			if (winIndexMap.get(winCurIndex) != null) {
				String url = (String) winIndexMap.get(winCurIndex);

				if (url != null && url.indexOf(fieldName + "=") >= 0) {
					int valStart = url.indexOf(fieldName + "=") + fieldName.length() + 1;
					int valEnd = url.indexOf("&", valStart);

					if (valEnd > 0) {
						value = url.substring(valStart, valEnd);
					} else {
						value = url.substring(valStart);
					}
				}
			}
		}
		// zxy注释，这里不能再取参数值，因为如果是linkType=@URL: 会进行转码，这里重新取值覆盖前面的转码结果
		// else{
		// value = request.getParameter(fieldName) ;
		// }
		return value;
	}

	/**
	 * 获取单据编号
	 * 
	 * @param key
	 * @return
	 */
	public String getBillNoCode(String key) {
		String billStr = "";
		BillNo billNo = new BillNoManager().find(key); // 加载Map并赋值
		if (billNo == null) {
			billStr = "";
		} else {
			Object o = request.getSession().getAttribute("LoginBean");
			BillNoUnit unit = null;
			if (billNo.isFillBack()) {
				unit = billNo.getInvers(new HashMap<String, String>(), o);
			} else {
				unit = billNo.getNumber(new HashMap<String, String>(), o);
			}
			if (unit != null) {
				billStr = unit.getValStr();
			}
		}
		return billStr;
	}

	/**
	 * 获取单据编号
	 * 
	 * @param key
	 * @return
	 */
	public String getBillNoCode(String key,HttpServletRequest  req) {
		String billStr = "";
		BillNo billNo = new BillNoManager().find(key); // 加载Map并赋值
		if (billNo == null) {
			billStr = "";
		} else {
			Object o = req.getSession().getAttribute("LoginBean");
			BillNoUnit unit = null;
			if (billNo.isFillBack()) {
				unit = billNo.getInvers(new HashMap<String, String>(), o);
			} else {
				unit = billNo.getNumber(new HashMap<String, String>(), o);
			}
			if (unit != null) {
				billStr = unit.getValStr();
			}
		}
		return billStr;
	}
	
	public String getModuleName() {
		String moduleName = "";
		HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
		String winCurIndex = request.getParameter("winCurIndex");
		String url = (String) winIndexMap.get(winCurIndex);
		LoginBean o = (LoginBean) request.getSession().getAttribute("LoginBean");

		ArrayList menu = (ArrayList) BaseEnv.moduleMap.get(o.getDefSys());
		for (int i = 0; i < menu.size(); i++) {
			ModuleBean mod = (ModuleBean) menu.get(i);
			if (mod.getLinkAddress() != null && mod.getLinkAddress().equals(url)) {
				if (mod.getModelDisplay() != null) {
					moduleName = mod.getModelDisplay().get(this.getLocale());
				}
				return moduleName;
			}
		}

		return moduleName;
	}

	/**
	 * 判断某个模块是否启用
	 * 
	 * @param tableName
	 * @return
	 */
	public static void moduleIsUsed(ArrayList moduleList, String tableName, Result result2) {
		for (int i = 0; i < moduleList.size(); i++) {
			ModuleBean mod = (ModuleBean) moduleList.get(i);
			if (mod.getChildList() != null && mod.getChildList().size() > 0) {
				moduleIsUsed(mod.getChildList(), tableName, result2);
			} else {
				if (mod.getIsUsed() == 1) {
					result2.setRetCode(ErrorCanst.DATA_ALREADY_USED);
					return;
				}
			}
		}
	}

	public static boolean seqStartInTable(List fields) {
		boolean isExist = false;
		if (fields != null) {
			for (int i = 0; i < fields.size(); i++) {
				DBFieldInfoBean fieldInfo = (DBFieldInfoBean) fields.get(i);
				GoodsPropInfoBean propInfo = getPropBean(fieldInfo.getFieldName());
				if (propInfo != null) {
					if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE && fieldInfo.getFieldSysType().equals("GoodsField") && propInfo.getIsUsed() == 1 && propInfo.getIsSequence() == 1) {
						isExist = true;
						break;
					}
				}
			}
		}
		return isExist;
	}

	/**
	 * 根据访问路径获取模块名称
	 * 
	 * @return
	 */
	public static void getModuleNameN(ArrayList menu, String url, Result rs, String locale, int flag) {
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS || menu == null) {
			return;
		}
		for (int i = 0; i < menu.size(); i++) {
			ModuleBean mod = (ModuleBean) menu.get(i);
			if ((mod.getLinkAddress() != null && !mod.getLinkAddress().equals("")) && "0".equals(mod.getIsCatalog())) {
				if (flag == 1) {
					if (url.equals(mod.getLinkAddress())) {
						String moduleName = "";
						if (mod.getModelDisplay() != null) {
							moduleName = mod.getModelDisplay().get(locale);
						}
						rs.setRetVal(moduleName);
						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						return;
					}
				} else if (flag == 2) {
					if (url.startsWith(mod.getLinkAddress())) {
						String moduleName = "";
						if (mod.getModelDisplay() != null) {
							moduleName = mod.getModelDisplay().get(locale);
						}
						rs.setRetVal(moduleName);
						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						return;
					}
				} else if (flag == 3) {
					if (mod.getLinkAddress().endsWith(url)) {
						String moduleName = "";
						if (mod.getModelDisplay() != null) {
							moduleName = mod.getModelDisplay().get(locale);
						}
						rs.setRetVal(moduleName);
						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						return;
					}
				}
			} else {
				getModuleNameN(mod.getChildList(), url, rs, locale, flag);
			}
		}
	}

	public String getModuleUrlByWinCurIndex(String winCurIndex) {
		String moduleURL = "";
		if (winCurIndex != null && !winCurIndex.equals("-1")) {
			HashMap winIndex = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
			if (winIndex != null) {
				moduleURL = (String) winIndex.get(winCurIndex);
			}
		}
		return moduleURL;
	}

	public static String encode(String value) {
		value = value == null ? "" : value;
		try {
			value = java.net.URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String decode(String value) {
		value = value == null ? "" : value;
		try {
			value = java.net.URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String getSysVersionName() {
		return BaseEnv.sysVersionName;
	}

	public static String getCompanyName() {
		return getCompanyName(null);
	}

	public static String getCompanyName(String language) {
		/* 判断title和link */
		if (language == null || language.length() == 0) {
			language = "zh_CN";
		}
		String str = "";
		Result rs = new PublicMgt().getCompany();
		ArrayList rsRs = (ArrayList) rs.retVal;
		if (rsRs != null && rsRs.size() > 0) {
			str = get(rsRs.get(0), 1).toString();
		}
		if ("".equals(str)) {
			String company = SystemState.instance.companyName;
			if (company == null || company.length() == 0) {
				company = BaseEnv.sysVersionName;
			}
			str = company;
		}
		if (SystemState.instance.isFree) {
			str = str + "【基础版】";
		} else if (SystemState.instance.dogState == SystemState.DOG_ERROR_FORMAL_REG) {
			str += "-" + getMessage(language, "upgrade.msg.noReg");
		}
		return str;
	}

	public String getCompanyLogo() {
		String existLogo = "";
		String logoPath = request.getSession().getServletContext().getRealPath("logo");
		File logoFile = new File(logoPath);
		if (logoFile.isDirectory()) {
			File[] listFile = logoFile.listFiles();
			for (File file : listFile) {
				if (file.getName().contains("loginLogo")) {
					existLogo = file.getName();
				}
			}
		}
		return existLogo;
	}

	/**
	 * 获取语言的string
	 * 
	 * @return String
	 */
	public String getLocale() {
		Object o = request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (o == null) {
			System.out.println("-------from servlet ");
			o = request.getSession().getServletContext().getAttribute("DefaultLocale");
		}
		return o == null ? "" : o.toString();
	}

	/**
	 * eg:zh_CN:中国;zh_TW:中国;en:China
	 * 
	 * @return 如果local为zh_CN，得到字符串中国
	 */
	public String getLocaleDisplay(String display) {
		String str = "";
		if (display != null) {
			String locale = getLocale();
			str = getLocaleDisplay(display, locale);
		}
		return str;
	}

	/**
	 * eg:zh_CN:中国;zh_TW:中国;en:China
	 * 
	 * @return 如果local为zh_CN，得到字符串中国
	 */
	public static String getLocaleDisplay(String display, String locale) {
		String str = "";
		if (display != null) {
			int index = display.indexOf(locale + ":");
			if (index == -1)
				return "";
			int end = display.indexOf(";", index);
			str = display.substring(index + locale.length() + 1, end);
		}
		return str;
	}

	/**
	 * 根据STR获取语言
	 * 
	 * @param ls
	 *            String
	 * @return Locale
	 */
	public Locale getLocale(String ls) {
		Locale locale = Locale.getDefault();
		if (ls != null && ls.length() != 0) {
			String[] params = ls.split("_");
			if (params.length == 3) {
				locale = new Locale(params[0], params[1], params[2]);
			} else if (params.length == 2) {
				locale = new Locale(params[0], params[1]);
			} else {
				locale = new Locale(params[0]);
			}
		}
		return locale;
	}

	/**
	 * 获取操作类型
	 * 
	 * @param key
	 *            String
	 * @return int
	 */
	public int getOP(String key) {
		Object o = opMap.get(key);
		if (o == null) {
			try {
				Field f = OperationConst.class.getDeclaredField(key);
				int i = f.getInt(new OperationConst() {
				});
				o = new Integer(i);
				opMap.put(key, new Integer(i));
			} catch (Exception ex) {
				return -1000;
			}
		}
		return ((Integer) o).intValue();
	}

	/**
	 * 取得字符串的真实长度 英文为一个字符,中文为两个字符
	 * 
	 * @param str
	 * @return
	 */
	public static int getStringLength(String str) {
		int len = 0;
		if (str.length() <= 0) {
			len = 0;
		} else {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) < 256) {
					len = len + 1;
				} else {
					len = len + 2;
				}
			}
		}
		return len;
	}

	/**
	 * 按字节来截取字符
	 * 
	 * @param str
	 * @param toCount
	 * @return
	 */
	public static String subStringByByte(String str, int toCount) {
		int reInt = 0;
		String reStr = "";
		if (str == null)
			return "";
		char[] tempChar = str.toCharArray();
		for (int i = 0; (i < tempChar.length && toCount > reInt); i++) {
			String s1 = String.valueOf(tempChar[i]);
			byte[] b = s1.getBytes();
			reInt += b.length;
			reStr += tempChar[i];
		}
		return reStr;
	}

	/**
	 * 从数组中按下标取数据
	 * 
	 * @param o
	 *            Object
	 * @param i
	 *            int
	 * @return Object
	 */
	public Object getFromArray(Object o, int i) {
		if (o instanceof Object[]) {
			Object[] obj = (Object[]) o;
			if (i >= obj.length) {
				return "";
			} else {
				return ((Object[]) o)[i];
			}
		}
		return "";
	}

	/**
	 * 取当前日期,格式yyyyMMdd
	 * 
	 * @return String
	 */
	public String getDate() {
		return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
	}

	/**
	 * 取传递时间月份的最后一天,格式yyyyMMdd
	 * 
	 * @return String
	 */
	public String getMonthLastDay(String syear, String smonth) {
		int year = Integer.parseInt(syear);
		int month = Integer.parseInt(smonth);
		String newDate = year + "-" + (month < 10 ? "0" : "") + month + "-";
		newDate = newDate + (month == 4 || month == 6 || month == 9 || month == 11 ? 30 : (month == 2 ? (year % 4 == 0 ? 29 : 28) : 31));
		return newDate;
	}

	/**
	 * 取第二天日期,格式yyyyMMdd
	 * 
	 * @return String
	 */
	public String getTomorrowDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
	}

	/**
	 * 取昨天日期,格式yyyyMMdd
	 * 
	 * @return String
	 */
	public static String getYesteDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
	}

	public String compareStrDate(String str1, String str2) {
		if (str1.compareToIgnoreCase(str2) > 0) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 取动态值对数据
	 * 
	 * @param key
	 *            String
	 * @return List
	 */
	public List getDS(String key) {
		return getDS(key, 1);
	}

	/**
	 * 取动态值对的数据
	 * 
	 * @param key
	 *            String 键值
	 * @param start
	 *            int 值对的开始数
	 * @return List
	 */
	public List getDS(String key, int start) {
		List list = new ArrayList();
		for (int i = start; true; i++) {
			String name = resources.getMessage(locale, key + ".name." + i);
			String value = resources.getMessage(locale, key + ".value." + i);
			if (name == null || name.length() == 0) {
				break;
			}
			KeyPair kp = new KeyPair();
			kp.setName(name);
			kp.setValue(value);
			list.add(kp);
		}
		return list;
	}

	public static List getDS(String key, String locale, MessageResources resources) {
		return getDS(key, 1, locale, resources);
	}

	/**
	 * 取动态值对的数据
	 * 
	 * @param key
	 *            String 键值
	 * @param start
	 *            int 值对的开始数
	 * @return List
	 */
	public static List getDS(String key, int start, String locale, MessageResources resources) {
		List list = new ArrayList();
		for (int i = start; true; i++) {
			String name = resources.getMessage(locale, key + ".name." + i);
			String value = resources.getMessage(locale, key + ".value." + i);
			if (name == null || name.length() == 0) {
				break;
			}
			KeyPair kp = new KeyPair();
			kp.setName(name);
			kp.setValue(value);
			list.add(kp);
		}
		return list;
	}

	public List getDSExcelFieldType(String key) {
		List list = new ArrayList();
		for (int i = 1; i <= 4; i++) {
			String name = resources.getMessage(locale, key + ".name." + i);
			String value = resources.getMessage(locale, key + ".value." + i);
			if (name == null || name.length() == 0) {
				break;
			}
			KeyPair kp = new KeyPair();
			kp.setName(name);
			kp.setValue(value);
			list.add(kp);
		}
		String name = resources.getMessage(locale, key + ".name.7");
		String value = resources.getMessage(locale, key + ".value.7");
		if (name != null && name.length() != 0) {
			KeyPair kp = new KeyPair();
			kp.setName(name);
			kp.setValue(value);
			list.add(kp);
		}
		return list;
	}

	public static LoginBean getLoginBean(HttpServletRequest req) {
		Object o = req.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}

	public LoginBean getLoginBean() {
		Object o = request.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}

	/**
	 * <pre>
	 * 获取系统 {@link Locale}
	 * 先从Session中取，如果取不到则调用 {@link HttpServletRequest#getLocale()}取{@linkpla.getTableName()Locale},如果再取不到则取系统的Locale
	 * </pre>
	 * 
	 * @param req
	 *            http请求
	 * @return 系统的Locale
	 */
	public static Locale getLocale(HttpServletRequest req) {
		Locale loc = null;
		Object obj = req.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (obj != null) {
			loc = (Locale) obj;
			return loc;
		}
		if (loc == null) {
			loc = req.getLocale();
		}
		if (loc == null) {
			loc = Locale.getDefault();
		}
		return loc;
	}

	/**
	 * 根据动态资源文件的值取得对应的名称
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            String
	 * @return String
	 */
	public String getDSByValue(String key, String value) {
		List dsList = getDS(key);
		if (dsList.size() > 0) {
			for (int i = 0; i < dsList.size(); i++) {
				KeyPair kp = (KeyPair) dsList.get(i);
				if (value.equals(kp.getValue())) {
					return kp.getName();
				}
			}
		}
		return "";
	}

	/**
	 * 从字符串过滤掉HTML标签 如果在页面显示文本数据（比如说公告栏的内容），而且该段数据
	 * 不是放在页面的textarea中显示时，必须使用该方法过滤掉html和js标签
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String encodeHTML(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll(String.valueOf((char) 32), "&nbsp;");
		str = str.replaceAll(String.valueOf((char) 9), "&nbsp;");
		str = str.replaceAll(String.valueOf((char) 34), "&quot;");
		str = str.replaceAll(String.valueOf((char) 39), "&#39;");
		str = str.replaceAll(String.valueOf((char) 13), "");
		str = str.replaceAll(String.valueOf((char) 10), "<BR/> ");
		str = str.replaceAll(String.valueOf(new char[] { (char) 10, (char) 10 }), "&lt;/P&gt;&lt;P&gt; ");
		str = revertTextCode(str);
		return str;
	}

	/**
	 * 用于在flash页面显示数据
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeHTMLFlash(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		str = str.replaceAll("<br />", " ");
		str = str.replaceAll("<p>", " ");
		str = str.replaceAll("</p>", " ");
		str = str.replaceAll("&nbsp;", " ");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&apos;");
		str = revertTextCode(str);
		return str;
	}

	public static String encodeHTML2(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("\\\\r\\\\n", "");
		str = str.replaceAll("\\r", "");
		str = str.replaceAll("\\n", "");
		str = revertTextCode(str);
		return str;
	}

	public static String encodeHTML3(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("\\\\r\\\\n", "");
		str = str.replaceAll("\\r", "");
		str = str.replaceAll("\\n", "");
		return str;
	}

	public static String encodeHTML4(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\\\\r\\\\n", "<br>");
		return str;
	}

	/**
	 * 将指定字符串按指定的长度截断以后，过滤掉HTML标签
	 * 
	 * @param str
	 *            String
	 * @param length
	 *            int
	 * @return String
	 */
	public static String encodeHTML(String str, int length) {
		str = subTitle(str, length);
		return encodeHTML(str);
	}

	/**
	 * 从字符串过滤掉HTML标签 显示成一行
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String encodeHTMLLine(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll(String.valueOf((char) 32), "&nbsp;");
		str = str.replaceAll(String.valueOf((char) 9), "&nbsp;");
		str = str.replaceAll(String.valueOf((char) 34), "&quot;");
		str = str.replaceAll(String.valueOf((char) 39), "&#39;");
		str = str.replaceAll(String.valueOf((char) 13) + String.valueOf((char) 10), "");
		str = str.replaceAll(String.valueOf(new char[] { (char) 10, (char) 10 }), "</P><P> ");
		return str;
	}

	/**
	 * 转换特殊字符
	 * 
	 * @param strValue
	 * @return
	 */
	public static String encodeTextCode(String strValue) {
		// if(strValue==null || strValue.length()==0){
		// return "" ;
		// }
		// strValue = strValue.replaceAll("'", "&#39;") ;
		// strValue = strValue.replaceAll("\"", "&#34;") ;
		return strValue;
	}

	/**
	 * 转换特殊字符
	 * 
	 * @param strValue
	 * @return
	 */
	public static String encodeTextCode2(String strValue) {
		// if(strValue==null || strValue.length()==0){
		// return "" ;
		// }
		// strValue = strValue.replaceAll("&#39;", "@39@") ;
		// strValue = strValue.replaceAll("&#34;", "@34@") ;
		return strValue;
	}

	/**
	 * 还原特殊字符
	 * 
	 * @param strValue
	 * @return
	 */
	public static String revertTextCode(String strValue) {
		// if(strValue==null || strValue.length()==0){
		// return "" ;
		// }
		// strValue = strValue.replaceAll("&#39;","&apos;") ;
		// strValue = strValue.replaceAll("&#34;","&quot;") ;
		return strValue;
	}

	/**
	 * 还原特殊字符
	 * 
	 * @param strValue
	 * @return
	 */
	public static String revertTextCode2(String strValue) {
		// if(strValue==null || strValue.length()==0){
		// return "" ;
		// }
		// strValue = strValue.replaceAll("&#39;","'") ;
		// strValue = strValue.replaceAll("&#34;","\"") ;
		return strValue;
	}

	/**
	 * 还原特殊字符
	 * 
	 * @param strValue
	 * @return
	 */
	public static String revertTextCode3(String strValue) {
		if (strValue == null || strValue.length() == 0) {
			return "";
		}
		strValue = strValue.replaceAll("&apos;", "&#39;");
		strValue = strValue.replaceAll("&quot;", "&#34;");
		return strValue;
	}

	/**
	 * 还原特殊字符
	 * 
	 * @param strValue
	 * @return
	 */
	public static String revertTextCode4(String strValue) {
		if (strValue == null || strValue.length() == 0) {
			return "";
		}
		strValue = strValue.replaceAll("&apos;", "'");
		strValue = strValue.replaceAll("&quot;", "\"");
		return strValue;
	}

	/**
	 * 从字符串过滤掉HTML标签 显示成一行 指定字符串按指定的长度截断以后，过滤掉HTML标签
	 * 
	 * @param str
	 *            String
	 * @param length
	 *            int
	 * @return String
	 */
	public static String encodeHTMLLine(String str, int length) {
		str = subTitle(str, length);
		return encodeHTMLLine(str);
	}

	/**
	 * 将字符串截取一定的长度,截断后加上...做为结尾
	 * 
	 * @param str
	 *            String
	 * @param length
	 *            int 截断长度，双字节字符长度为2
	 * @return String
	 */
	public static String subTitle(String str, int length) {
		if (str == null || str.length() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i = 0, count = 0;
		for (; i < str.length(); i++) {
			String a = String.valueOf(str.charAt(i));
			if (!isEnStr(a)) {
				count += 2;
			} else {
				count++;
			}
			sb.append(a);
			if (count >= length) {
				sb.delete(sb.length() - 3, sb.length());
				sb.append("...");
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串截取一定的长度
	 * 
	 * @param str
	 *            String
	 * @param length
	 *            int
	 * @return String
	 */
	public static String substring(String str, int length) {
		if (str == null || str.trim().length() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		int i = 0, count = 0;
		for (; i < str.length(); i++) {
			String a = String.valueOf(str.charAt(i));
			if (!isEnStr(a)) {
				count += 2;
			} else {
				count++;
			}
			sb.append(a);
			if (count >= length) {
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param str
	 *            字符串
	 * @param odlStr
	 *            替换前的字符
	 * @param odlStr
	 *            替换后的字符
	 * @return
	 */
	public static String replaceString(String str, String odlStr, String newStr) {
		if (str == null || odlStr.length() == 0) {
			return str;
		}
		newStr = newStr.replaceAll("\\$", "\\\\\\$");
		return str.replaceAll(odlStr, newStr);
	}

	public static String existComType(String str) {
		if (str.contains("!=")) {
			return "!=";
		} else if (str.contains("<>")) {
			return "!=";
		} else if (str.contains("<")) {
			return "<";
		} else if (str.contains("<=")) {
			return "<=";
		} else if (str.contains(">")) {
			return ">";
		} else if (str.contains(">=")) {
			return ">=";
		} else if (str.contains("not like")) {
			return "not like";
		} else if (str.contains("like")) {
			return "like";
		} else {
			return "=";
		}
	}

	/**
	 * 截取某个字符前的数据
	 * 
	 * @param str
	 *            String
	 * @param length
	 *            int
	 * @return String
	 */
	public static String substring(String str, String flag) {
		if (str == null || str.length() == 0)
			return "";
		if (flag == null || flag.length() == 0)
			return "";
		int index = str.indexOf(flag);
		return str.substring(0, index);
	}

	/**
	 * 获取当前session中的同步令牌值
	 * 
	 * @return String
	 */
	public String getToken() {
		Object obj = request.getSession(true).getAttribute("org.apache.struts.action.TOKEN");
		String s = obj != null ? obj.toString() : "";
		return s;

	}

	/**
	 * 获取语言切换代码
	 * 
	 * @return String
	 */
	public String getLocaleParam() {
		String str = "";
		Hashtable ht = (Hashtable) application.getAttribute("LocaleTable");
		if (ht == null || ht.size() == 0) {
			return "";
		}

		String queryStr = request.getQueryString();

		String oldURL = request.getServletPath();
		if (queryStr != null && queryStr.length() != 0) {
			oldURL += "?" + queryStr;
		}

		Enumeration em = ht.keys();
		Locale locale;
		while (em.hasMoreElements()) {
			String name = em.nextElement().toString();
			String cname = getLocale();

			if (cname == null || cname.length() == 0) {
				cname = application.getAttribute("DefaultLocale").toString();
			}

			if (!name.equalsIgnoreCase(cname)) {
				locale = (Locale) ht.get(name);
				str += "<a href=\"/LocaleServlet?LOCALE=" + name + "&oldURL=" + oldURL + "\">" + locale.getDisplayName(locale) + "</a>";
			}
		}

		return str;
	}

	/**
	 * 判断是否都是单字节字符
	 * 
	 * @param s
	 *            String
	 * @return boolean
	 */
	public static boolean isEnStr(String s) {
		if (s == null || s.length() == 0) {
			return true;
		}
		return !s.matches(".*[^\\x00-\\xff].*");
	}

	/**
	 * 获得字符串的真实长度，一个双字节字符长度计为2
	 * 
	 * @param s
	 *            String
	 * @return int
	 */
	public static int strLength(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		return s.replaceAll("[^\\x00-\\xff]", "11").length();
	}

	/**
	 * 判断s1是否在s2中,s2以“，”分
	 * 
	 * @param s1
	 *            String
	 * @param s2
	 *            String
	 * @return boolean
	 */
	public boolean isIn(String s1, String s2) {
		String[] ss = s2.split(",");
		for (int i = 0; i < ss.length; i++) {
			if (s1.equals(ss[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 如果S1中包含S2返回true
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public boolean isExist(String s1, String s2) {
		boolean isExist = false;
		if (s1 != null && s2 != null) {
			if (s1.contains(s2)) {
				isExist = true;
			}
		}
		return isExist;
	}

	/**
	 * 判断个性界面是否存在某个字段
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public int indexOf(String layout, String fieldName) {
		int number = -1;
		if (layout != null && fieldName != null) {
			number = layout.indexOf("\"" + fieldName + "\"");
		}
		return number;
	}

	/**
	 * 以什么结束的字符串
	 * 
	 * @return
	 */
	public boolean endWidthStr(String str1, String end) {
		boolean isExist = false;
		if (str1 != null && end != null) {
			if (str1.endsWith(end)) {
				isExist = true;
			}
		}
		return isExist;
	}

	public String getModelNameByLinkAddress(String linkAddress) {

		ModuleFlowMgt mgt = new ModuleFlowMgt();
		String locale = getLocale();
		Result rs = mgt.getModelName(linkAddress, locale);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String str = (String) rs.getRetVal();
			return str;
		} else {
			return "";
		}
	}

	public static String getModelNameByLinkAdd(String linkAddress) {
		ModuleFlowMgt mgt = new ModuleFlowMgt();
		String locale = "zh_CN";
		Result rs = mgt.getModelName(linkAddress, locale);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String str = (String) rs.getRetVal();
			return str;
		} else {
			return "";
		}

	}

	/**
	 * 处理SQL语句中like子句内的字符串，以'\'为转义 例 like '%sqllike%' ，则处理sqllike，将sqllike
	 * 内的'%','_','\'转为'\%','\_','\\'。
	 * 
	 * @param sqllike
	 *            String 待处理字符串
	 * @return r String 返回结果
	 */
	public static String SqlLike(String sqllike) {
		String r = "";
		// 保证sqllike是不为空的字符串
		sqllike = "" + sqllike;
		// 去两边空格
		sqllike = sqllike.trim();
		// 将\ 替换为 \\
		sqllike = sqllike.replaceAll("[\\\\]", "\\\\\\\\");
		// 将% 替换为 \%
		sqllike = sqllike.replaceAll("[%]", "\\\\%");
		// 将_ 替换为 \_
		sqllike = sqllike.replaceAll("[_]", "\\\\_");
		r = sqllike;
		return r;
	}

	public LoginBean getLB() {
		Object o = request.getSession(true).getAttribute("LoginBean");
		if (o == null || !(o instanceof LoginBean)) {
			return null;
		} else {
			return (LoginBean) o;
		}

	}

	public static Object get(Object o, int i) {
		if (o instanceof Object[]) {
			Object[] obj = (Object[]) o;
			if (i >= obj.length) {
				return "";
			} else {
				if (((Object[]) o)[i] == null) {
					return "";
				} else {
					return ((Object[]) o)[i];
				}
			}
		}
		return "";
	}

	public Object getList(Object o, int i) {
		if (o instanceof List) {
			List obj = (List) o;
			if (i >= obj.size()) {
				return "";
			} else {
				return ((List) o).get(i);
			}
		}
		return "";
	}

	public static String getStyle() {
		return "nomarl";
	}

	public static String getDisplay(Map map) {
		if (map == null) {
			return "";
		}
		String str = "";
		if (map.size() > 1) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry en = (Map.Entry) it.next();
				str += en.getKey() + ":" + en.getValue() + ";";
			}
		} else if (map.size() == 1) {
			Iterator it = map.entrySet().iterator();
			if (it.hasNext()) {
				Map.Entry en = (Map.Entry) it.next();
				str += en.getValue();
			}
		}
		return str;
	}

	public String getStylePath() {
		HttpSession sess = request.getSession();
		LoginBean bean = (LoginBean) sess.getAttribute("LoginBean");
		if (bean == null || bean.getDefStyle() == null || bean.getDefStyle().trim().length() == 0 || bean.getDefStyle().trim().equals("0")) {
			if (BaseEnv.DefaultStyle != null && BaseEnv.DefaultStyle.length() > 0) {
				return BaseEnv.DefaultStyle;
			} else {
				return "style1";
			}

		} else {
			return "style1";
		}

	}

	public List getEnumeration() {
		List list = new ArrayList();
		Map map = BaseEnv.enumerationMap;
		EnumerateBean beans[] = (EnumerateBean[]) map.values().toArray(new EnumerateBean[0]);
		for (int i = 0; i < beans.length; i++) {
			KeyPair kp = new KeyPair();
			kp.setName(((KRLanguage) (beans[i].getDisplay())).get(getLocale()));
			kp.setValue(beans[i].getEnumName().toString());
			list.add(kp);
		}
		return list;

	}

	/**
	 * 当枚举类型为MainModule时，由于必须受制于加密狗信息，所以从加密狗中读取
	 * 
	 * @param enumeration
	 *            String
	 * @return List
	 */
	public List<KeyPair> getEnumerationItems(String enumeration) {
		List list = new ArrayList();
		Map map = BaseEnv.enumerationMap;
		EnumerateBean beans[] = (EnumerateBean[]) map.values().toArray(new EnumerateBean[0]);
		for (int i = 0; i < beans.length; i++) {
			if (beans[i].getEnumName().equals(enumeration)) {
				for (int j = 0; j < beans[i].getEnumItem().size(); j++) {
					EnumerateItemBean eib = (EnumerateItemBean) beans[i].getEnumItem().get(j);
					if ("MainModule".equals(enumeration)) {
						// 为模块枚举时从加密狗读取
						if (!eib.getEnumValue().equals("0") && !hasMainModule(eib.getEnumValue())) {
							continue;
						}
					}
					if ("AllSunCompany".equals(enumeration)) {
						LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
						String sunClassCode = lg.getSunCmpClassCode();
						// 为分支机构枚举时
						if (eib.getEnumValue().length() < sunClassCode.length()
								|| (eib.getEnumValue().length() >= sunClassCode.length() && !eib.getEnumValue().substring(0, sunClassCode.length()).equals(sunClassCode))) {
							continue;
						}
					}
					// 特殊处理有关隐藏往来单位等核算枚举
					if ("BuyType2".equals(enumeration) && eib.getEnumValue().equals("tblCompany")) {
						// 采购类报表统计类型，要控制是否隐藏供应商和往来单位
						if (("," + this.getLB().getHiddenField()).contains("," + 4) || ("," + this.getLB().getHiddenField()).contains("," + 5)) {
							continue;
						}
					} else if (("SalesDateType".equals(enumeration) || "DataType2".equals(enumeration) || "SaleAchievementType".equals(enumeration) || "SaleType2".equals(enumeration))
							&& eib.getEnumValue().indexOf("Company") > -1) {
						if (("," + this.getLB().getHiddenField()).contains("," + 3) || ("," + this.getLB().getHiddenField()).contains("," + 5)) {
							continue;
						}
					}

					KeyPair kp = new KeyPair();
					KRLanguage krl = ((KRLanguage) (eib.getDisplay()));
					String krld = "";
					if (krl == null) {
						krld = eib.getEnumValue();
					} else {
						krld = krl.get(getLocale());
					}
					kp.setName(krld);
					kp.setValue(eib.getEnumValue());
					list.add(kp);
				}
				break;
			}
		}
		return list;
	}

	/**
	 * 当枚举类型为MainModule时，由于必须受制于加密狗信息，所以从加密狗中读取
	 * 
	 * @param enumeration
	 *            String
	 * @return List
	 */
	public static List getEnumerationItems(String enumeration, String locale) {
		List list = new ArrayList();
		Map map = BaseEnv.enumerationMap;
		EnumerateBean beans[] = (EnumerateBean[]) map.values().toArray(new EnumerateBean[0]);
		for (int i = 0; i < beans.length; i++) {
			if (beans[i].getEnumName().equals(enumeration)) {
				for (int j = 0; j < beans[i].getEnumItem().size(); j++) {
					EnumerateItemBean eib = (EnumerateItemBean) beans[i].getEnumItem().get(j);
					if ("MainModule".equals(enumeration)) {
						// 为模块枚举时从加密狗读取
						if (!eib.getEnumValue().equals("0") && !hasMainModule(eib.getEnumValue())) {
							continue;
						}
					}
					KeyPair kp = new KeyPair();
					if (eib.getDisplay() == null) {
						kp.setName(eib.getEnumValue());
					} else {
						kp.setName(eib.getDisplay().get(locale));
					}
					kp.setValue(eib.getEnumValue());
					list.add(kp);

				}

				break;
			}
		}
		return list;
	}

	/**
	 * 当枚举类型为MainModule时，由于必须受制于加密狗信息，所以从加密狗中读取
	 * 
	 * @param enumeration
	 *            String
	 * @return List
	 */
	public static String getEnumerationValue(String enumeration, String locale, String display) {

		Map map = BaseEnv.enumerationMap;
		EnumerateBean beans[] = (EnumerateBean[]) map.values().toArray(new EnumerateBean[0]);
		for (int i = 0; i < beans.length; i++) {
			if (beans[i].getEnumName().equals(enumeration)) {
				for (int j = 0; j < beans[i].getEnumItem().size(); j++) {
					EnumerateItemBean eib = (EnumerateItemBean) beans[i].getEnumItem().get(j);
					if ("MainModule".equals(enumeration)) {
						// 为模块枚举时从加密狗读取
						if (!eib.getEnumValue().equals("0") && !hasMainModule(eib.getEnumValue())) {
							continue;
						}
					}
					if (((KRLanguage) (eib.getDisplay())).get(locale).equals(display)) {
						return eib.getEnumValue();
					}
				}

				break;
			}
		}
		return "";
	}

	/**
	 * 根据表名，字段名查找枚举值
	 * 
	 * @param enumeration
	 *            String
	 * @return List
	 */
	public static List getEnumerationItems(String tableName, String fieldName, String locale) {
		List list = new ArrayList();
		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
		DBFieldInfoBean dbField = DDLOperation.getFieldInfo(allTables, tableName, fieldName);
		if (dbField != null) {
			list = getEnumerationItems(dbField.getRefEnumerationName(), locale);
		}
		return list;
	}

	/**
	 * 根据表名，字段名查找枚举值 ,按值来排序
	 * 
	 * @param enumeration
	 *            String
	 * @return List
	 */
	public List getOrderEnumerationItems(String enumName, String locale) {
		List enumList = getEnumerationItems(enumName, locale);
		Collections.sort(enumList, new OrderEnumeration());
		return enumList;
	}

	class OrderEnumeration implements Comparator {
		public int compare(Object o1, Object o2) {
			KeyPair key1 = (KeyPair) o1;
			KeyPair key2 = (KeyPair) o2;
			Integer value1 = Integer.parseInt(key1.getValue());
			Integer value2 = Integer.parseInt(key2.getValue());
			if (value1 > value2) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public String getEnumerationItemsDisplay(String enumeration, String value) {
		List list = getEnumerationItems(enumeration);
		for (int i = 0; i < list.size(); i++) {
			KeyPair kp = (KeyPair) list.get(i);
			if (kp.getValue().equals(value)) {
				return kp.getName();
			}
		}
		return "";
	}

	public static String getEnumerationItemsDisplay(String enumeration, String value, String locale) {
		List list = GlobalsTool.getEnumerationItems(enumeration, locale);
		for (int i = 0; i < list.size(); i++) {
			KeyPair kp = (KeyPair) list.get(i);
			if (kp.getValue().equals(value)) {
				return kp.getName();
			}
		}
		return "";
	}

	public static String getCheckBoxDisplay(String enumeration, String value, String locale) {
		List list = GlobalsTool.getEnumerationItems(enumeration, locale);
		String[] values = value.split(",");
		String disPlay = "";
		for (int i = 0; i < list.size(); i++) {
			KeyPair kp = (KeyPair) list.get(i);
			for (int j = 0; j < values.length; j++) {
				if (kp.getValue().equals(values[j])) {
					disPlay = disPlay + kp.getName() + ",";
					break;
				}
			}
		}
		if (disPlay.length() > 0) {
			disPlay = disPlay.substring(0, disPlay.length() - 1);
		}

		return disPlay;
	}

	public static String getEnumerationItemsDisplay(List enumeration, String value) {

		for (int i = 0; i < enumeration.size(); i++) {
			KeyPair kp = (KeyPair) enumeration.get(i);
			if (kp.getValue().equals(value)) {
				return kp.getName();
			}
		}
		return "";
	}

	public static boolean getSysIntswitch() {
		return Boolean.parseBoolean(getSysSetting("intswitch"));
	}

	public static int getDigits() {
		return Integer.parseInt(getSysSetting("DigitsAmount"));// zxy
																// 从digits修改为DigitsAmount，原来的配置是隐藏的，存在不准确性，这里改成金额小数位数
	}

	public static int getDetDigits() {
		return Integer.parseInt(getSysSetting("DetDigits"));
	}

	public static boolean getHideEmail() {
		return Boolean.parseBoolean(getSysSetting("HideEmail"));
	}

	// 取系统参数--分页大小
	public static int getPageSize() {
		return Integer.parseInt(getSysSetting("PageSize"));
	}

	public static String formatNumber(Object num, boolean isCurrency, boolean isGroup, boolean intSwitch, String tableName, String fieldName, boolean isMain) {
		java.text.NumberFormat nf = null;
		if (isCurrency) {
			nf = java.text.NumberFormat.getCurrencyInstance();
		} else {
			nf = java.text.NumberFormat.getNumberInstance();
		}
		nf.setGroupingUsed(isGroup);

		// --表字段的小数位数----
		int dts = getFieldDigits(tableName, fieldName);
		if (intSwitch) {
			if (dts <= 0) {
				nf.setMaximumFractionDigits(isMain ? Integer.parseInt(getSysSetting("digits")) : Integer.parseInt(getSysSetting("DetDigits")));
			} else {
				nf.setMaximumFractionDigits(dts);
			}
			nf.setMinimumFractionDigits(0);
		} else {
			if (dts <= 0) {
				nf.setMaximumFractionDigits(isMain ? Integer.parseInt(getSysSetting("digits")) : Integer.parseInt(getSysSetting("DetDigits")));
				nf.setMinimumFractionDigits(isMain ? Integer.parseInt(getSysSetting("digits")) : Integer.parseInt(getSysSetting("DetDigits")));
			} else {
				nf.setMaximumFractionDigits(dts);
				nf.setMinimumFractionDigits(dts);
			}
		}
		try {
			return nf.format(num);
		} catch (Exception e) {
			return (String) num;
		}
	}

	public static String formatNumberS(Object num, boolean isCurrency, boolean isGroup, String fieldIdentity, String fieldName) {

		NumberFormat nf = null;
		if (isCurrency) {
			nf = NumberFormat.getCurrencyInstance();
		} else {
			nf = NumberFormat.getNumberInstance();
		}
		nf.setGroupingUsed(isGroup);
		int dts = Integer.parseInt(getSysSetting("digits"));

		// 如果没有指定字段标识，分解字段名称，设置字段标识=表结构的标识，如果表结构没有字段标识,并且小数位数大于0，设置其小数
		DBFieldInfoBean fieldBean = null;
		if ("".equals(fieldIdentity) && fieldName.indexOf(".") > 0) {
			String df = fieldName.substring(fieldName.indexOf(".") + 1);
			String dt = fieldName.substring(0, fieldName.indexOf("."));
			fieldBean = DDLOperation.getFieldInfo(BaseEnv.tableInfos, dt, df);
			if (fieldBean != null) {
				if (fieldBean.getFieldIdentityStr() != null && fieldBean.getFieldIdentityStr().length() > 0) {
					fieldIdentity = fieldBean.getFieldIdentityStr();
				} else if (fieldBean.getDigits() > 0) {
					dts = fieldBean.getDigits();
				}
			}
		}

		// 数量类型小数位数
		if ("Qty".equals(fieldIdentity)) {
			dts = Integer.parseInt(getSysSetting("DigitsQty"));
		} else if ("SPriceIdentifier".equals(fieldIdentity) || "priceIdentifier".equals(fieldIdentity) || "Price".equals(fieldIdentity)) {
			// 单价类型小数位数
			dts = Integer.parseInt(getSysSetting("DigitsPrice"));
		} else if ("SAmountIdentifier".equals(fieldIdentity) || "AmountIdentifier".equals(fieldIdentity) || "Amount".equals(fieldIdentity)) {
			// 金额类型小数位数
			dts = Integer.parseInt(getSysSetting("DigitsAmount"));
		} else if (fieldBean != null && fieldBean.getDigits() != 0) {
			// 不是上述字段标示且本身有小数位位，使用本身小数位数
			dts = fieldBean.getDigits();
		}

		boolean intSwitch = "true".equals(getSysSetting("intswitch"));
		nf.setMaximumFractionDigits(dts);
		if (intSwitch) {
			nf.setMinimumFractionDigits(0);
		} else {
			nf.setMinimumFractionDigits(dts);
		}

		try {
			return nf.format(num);
		} catch (Exception e) {
			return (String) num;
		}
	}

	public static String newFormatNumber(Object num, boolean isCurrency, boolean isGroup, boolean intSwitch, String tableName, String fieldName, boolean isMain) {
		if (num == null) {
			return "0";
		}
		java.text.NumberFormat nf = null;
		if (isCurrency) {
			nf = java.text.NumberFormat.getCurrencyInstance();
		} else {
			nf = java.text.NumberFormat.getNumberInstance();
		}
		nf.setGroupingUsed(isGroup);

		// --表字段的小数位数----
		int dts = getFieldDigits(tableName, fieldName);
		if (intSwitch) {
			if (dts <= 0) {
				nf.setMaximumFractionDigits(8);
			} else {
				nf.setMaximumFractionDigits(8);
			}
			nf.setMinimumFractionDigits(0);
		} else {
			if (dts <= 0) {
				nf.setMaximumFractionDigits(8);
				nf.setMinimumFractionDigits(8);
			} else {
				nf.setMaximumFractionDigits(8);
				nf.setMinimumFractionDigits(dts);
			}
		}
		String result = "";
		try {
			result = nf.format(num);
		} catch (Exception e) {
			result = (String) num;
		}
		if (result.indexOf(".") > 0) {
			while (true) {
				if ((result.length() - 1) == (result.lastIndexOf("0"))) {
					result = result.substring(0, result.length() - 1);
				} else {
					if (result.indexOf(".") == result.length() - 1) {
						result = result.substring(0, result.length() - 1);
					}
					break;
				}
			}
		}
		return result;
	}

	public List getTableName() {
		List list = new ArrayList();
		Map map = (Map) application.getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean beans[] = (DBTableInfoBean[]) map.values().toArray(new DBTableInfoBean[0]);
		for (int i = 0; i < beans.length; i++) {
			KeyPair kp = new KeyPair();
			KRLanguage hm = beans[i].getDisplay();
			if (hm == null) {
				BaseEnv.log.debug("Table Display is null" + beans[i].getTableName());
				kp.setName(beans[i].getTableName());
			} else {
				String td = beans[i].getDisplay().get(getLocale());
				if (td == null) {
					BaseEnv.log.debug("Table Display get " + getLocale() + " is null" + beans[i].getTableName());
					kp.setName(beans[i].getTableName());
				} else {
					kp.setName(td);
				}
			}
			kp.setValue(beans[i].getTableName().toString());
			list.add(kp);
		}
		return list;
	}

	public String[] split(String str, String split) {
		if (str == null || str.length() == 0)
			return new String[0];
		if ("shu".equals(split)) {
			return str.split("#\\|#");
		} else if ("feng".equals(split)) {
			return str.split("#;#");
		} else {
			return str.split(split);
		}
	}

	/**
	 * 自定义JS字段运算
	 * 
	 * @param map
	 *            Hashtable
	 * @param tableName
	 *            String
	 * @return List exeUserDefine
	 */
	public List splitString(String fieldCalculate) {
		// 这些都是参数固定的
		String param1 = ""; // 自定义参数1 listeud[2]
		String param2 = ""; // 自定义参数2 listeud[3]
		String param3 = ""; // 自定义参数3 listeud[4]
		String param4 = ""; // 自定义参数4 listeud[5]
		String strType = ""; // 自定义类型 listeud[0]
		String strid = ""; // 自定义ID listeud[1]

		List listeud = new ArrayList(); // 返回List结果集
		if (fieldCalculate == null || fieldCalculate.length() < 1) {
			return listeud;
		}
		String[] type = null; // 临时字段数组，从中取出所有的分字段
		String Temp = "\""; // 转义字段
		if ((fieldCalculate.indexOf("<") == 0) || fieldCalculate.lastIndexOf(">") == fieldCalculate.length()) {
			if (fieldCalculate.indexOf("<myd") >= 0) {
				type = fieldCalculate.split(" ");
				if (type[1].indexOf("type=\"") >= 0) {
					strType = type[1].substring(type[1].indexOf(Temp) + 1, type[1].lastIndexOf(Temp));
				}
				if (type[2].indexOf("id=\"") >= 0) {
					strid = type[2].substring(type[2].indexOf(Temp) + 1, type[2].lastIndexOf(Temp));
				}
				for (int i = 0; i < type.length; i++) {
					if (type[i].indexOf("param1=\"") >= 0) {
						param1 = type[i].substring(type[i].indexOf(Temp) + 1, type[i].lastIndexOf(Temp));
					}
					if (type[i].indexOf("param2=\"") >= 0) {
						param2 = type[i].substring(type[i].indexOf(Temp) + 1, type[i].lastIndexOf(Temp));
					}
					if (type[i].indexOf("param3=\"") >= 0) {
						param3 = type[i].substring(type[i].indexOf(Temp) + 1, type[i].lastIndexOf(Temp));
					}
					if (type[i].indexOf("param4=\"") >= 0) {
						param4 = type[i].substring(type[i].indexOf(Temp) + 1, type[i].lastIndexOf(Temp));
					}
				}
				listeud.add(strType);
				listeud.add(strid);
				listeud.add(param1);
				listeud.add(param2);
				listeud.add(param3);
				listeud.add(param4);
			}
		}
		return listeud;
	}

	/**
	 * 判断字段是否存在
	 * 
	 * @param ctb
	 * @param mtb
	 * @param fieldName
	 * @return
	 */
	private static boolean isExistField(DBTableInfoBean ctb, DBTableInfoBean mtb, HashMap tblList, String fieldName) {
		if (fieldName.indexOf("_") > -1) {
			if (ctb != null) { // ctb不为空，说明是从表的公式，从表公式只可能是从表表名，或主表表名
				// 明细字段
				fieldName = fieldName.substring(fieldName.indexOf("_") + 1);
				for (DBFieldInfoBean dfb : ctb.getFieldInfos()) {
					if (dfb.getFieldName().equals(fieldName) && dfb.getInputType() != DBFieldInfoBean.INPUT_NO) {
						return true;
					}
				}
			} else {
				// 主表公式，主表公式可能引用从表公式，从表要加序号
				String tableName = fieldName.substring(fieldName.indexOf("_") - 1, fieldName.indexOf("_"));
				tableName = (String) tblList.get(tableName);
				if (tableName == null) {
					BaseEnv.log.error("公式解释错误。表不存在" + fieldName);
					return false;
				}
				ctb = BaseEnv.tableInfos.get(tableName);
				if (ctb == null) {
					BaseEnv.log.error("公式解释错误。表不存在" + tableName);
				} else {
					fieldName = fieldName.substring(fieldName.indexOf("_") + 1);
					for (DBFieldInfoBean dfb : ctb.getFieldInfos()) {
						if (dfb.getFieldName().equals(fieldName) && dfb.getInputType() != DBFieldInfoBean.INPUT_NO) {
							return true;
						}
					}
				}
			}
		} else {
			for (DBFieldInfoBean dfb : mtb.getFieldInfos()) {
				if (dfb.getFieldName().equals(fieldName) && dfb.getInputType() != DBFieldInfoBean.INPUT_NO) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 替换公共公式
	 * 
	 * @param childTableName
	 * @param str
	 * @return
	 */
	public static String replacePubCaculate(String childTableName, String mainTableName, HashMap tblList, String str) {
		// 查询计算公式中是否有公用公式的变量。
		DBTableInfoBean ctb = childTableName == null ? null : BaseEnv.tableInfos.get(childTableName);
		DBTableInfoBean mtb = BaseEnv.tableInfos.get(mainTableName);

		Pattern pattern = Pattern.compile("@ValueOfDB:([^;]*)[;]{0,1}");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String pubname = matcher.group(1);
			String allStr = matcher.group();
			String pubVal = (String) BaseEnv.pupCalculate.get(pubname);
			if (pubVal == null) {
				throw new RuntimeException("公式解释错误，找不到公共公式:" + childTableName + "-----------" + str);
			}
			// 对pubVal中公式的字段进行校验，不存在的公式去掉
			pubVal = pubVal.replaceAll("\\}\\{", "};{"); // 防止公式中有错误写法

			String[] cstr = pubVal.split(";");
			String retStr = "";
			for (int i = 0; i < cstr.length; i++) {
				boolean right = true;
				Pattern p = Pattern.compile("\\{([^\\{\\}]*)\\}");
				Matcher m = p.matcher(cstr[i]);
				while (m.find()) {
					String tname = m.group(1);
					if (!isExistField(ctb, mtb, tblList, tname)) {
						right = false;
					}
				}
				if (right) {
					retStr += cstr[i] + ";";
				}
			}
			str = str.replaceAll(allStr, retStr);
		}

		return str;
	}

	/**
	 * 子表计算公式的解释。
	 * 
	 * @param childtableName
	 *            子表的表名
	 * @param tblList
	 * @param str
	 * @param mainTableName
	 * @return
	 */
	public String parseChildCaculate(String childTableName, HashMap tblList, String str, String mainTableName) {
		if (str == null) {
			return "";
		}
		// 替换公式中公共公式
		str = replacePubCaculate(childTableName, mainTableName, tblList, str);
		// 替换公式中所有表名

		str = str.replaceAll("@TABLENAME", childTableName);

		ArrayList childTableList = DDLOperation.getChildTables(mainTableName, BaseEnv.tableInfos);
		for (int i = 0; i < childTableList.size(); i++) {
			str = str.replaceAll("@CHILDTABLENAME" + i, (String) tblList.get("" + i));
		}

		str = str.replaceAll("\\}\\{", "};{");
		// 分解多语句
		String[] cstr = str.split(";");
		String retStr = "";
		for (int i = 0; i < cstr.length; i++) {
			boolean isRight = true; // 本段公式正确，如字段不存在，则要去掉该段公式
			if (cstr[i] == null || cstr[i].length() == 0) {
				continue;
			}
			// 判断公式中如果有系统参数，那么在这里直接用系统参数的值替换
			if (cstr[i].contains("@MEM:")) {
				// 解析语句得到有哪些系统参数
				ArrayList sysParams = new ArrayList();
				ConfigParse.parseSentenceGetParam(cstr[i], new ArrayList(), sysParams, new ArrayList(), new ArrayList(), new ArrayList(), null);
				for (int j = 0; j < sysParams.size(); j++) {
					cstr[i] = cstr[i].replaceAll("@MEM:" + sysParams.get(j), BaseEnv.systemSet.get(sysParams.get(j)).getSetting());
				}
			}

			// 1、找赋值字段
			String setField = "";
			Pattern pattern = Pattern.compile("\\{([^\\{\\}]*)\\}[\\s]*[=@]{1}([^;=@]+)");
			Matcher matcher = pattern.matcher(cstr[i]);
			if (!matcher.find()) {
				throw new RuntimeException("公式解释错误，找不到需赋值字段:" + cstr[i] + "-----------" + str);
			}
			setField = matcher.group(1);

			// 2、赋值字段先用r(0,fieldName).value替换
			String childField = setField;
			boolean isChild = false;

			// 赋值字段包含@交下面while处理
			if (childField.indexOf("_") > 0) {
				isChild = true;
				String tableName = childField.substring(0, childField.indexOf("_"));
				String fieldName = childField.substring(childField.indexOf("_") + 1);
				int index = Integer.parseInt(tblList.get(tableName).toString());
				childField = "r(" + index + ",'" + fieldName + "')";

				String temp = "r(" + index + ",\"" + fieldName + "\")";

				int sfPos = cstr[i].lastIndexOf("=");
				if(sfPos==-1)
					sfPos = cstr[i].lastIndexOf("@");
				String sfTemp = cstr[i].substring(0, sfPos);
				sfPos = sfTemp.lastIndexOf("{" + setField + "}");

				cstr[i] = cstr[i].substring(0, sfPos) + temp + "[p].value" + cstr[i].substring(sfPos + ("{" + setField + "}").length());
			} else {
				cstr[i] = cstr[i].substring(0, cstr[i].lastIndexOf("{" + setField + "}")) + "n('" + setField + "')[0].value"
						+ cstr[i].substring(cstr[i].lastIndexOf("{" + setField + "}") + ("{" + setField + "}").length());
			}

			boolean flagat = cstr[i].contains("@");
			// 解释语句

			while (cstr[i].indexOf("{") > -1) {
				String temp = cstr[i].substring(cstr[i].indexOf("{") + 1, cstr[i].indexOf("}"));
				if (cstr[i].indexOf("sum{") > -1) {
					int pos = cstr[i].indexOf("sum{") + 4;
					String right = cstr[i].substring(pos, cstr[i].indexOf("}", pos));
					cstr[i] = cstr[i].substring(0, pos - 4) + "sm('" + right + "')" + cstr[i].substring(cstr[i].indexOf("}", cstr[i].indexOf("sum{")) + 1);
				} else if (temp.indexOf("_") > 0) { // 如果是从表字段
					String tableName = temp.substring(0, temp.indexOf("_"));
					String fieldName = temp.substring(temp.indexOf("_") + 1);
					int index = Integer.parseInt(tblList.get(tableName).toString());

					DBFieldInfoBean dbBean = this.getFieldBean(tableName, fieldName);
					if (dbBean == null) {
						// 说明字段不存在，要去掉本段公式
						isRight = false;
						break;
					}
					// 如果字段类弄是数字，则用子函rn(转化
					if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT || dbBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						temp = "rn(" + index + ",\"" + fieldName + "\",p)";
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf("{")) + temp + cstr[i].substring(cstr[i].indexOf("}") + 1);
					} else {
						temp = "r(" + index + ",\"" + fieldName + "\")";
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf("{")) + temp + "[p].value" + cstr[i].substring(cstr[i].indexOf("}") + 1);
					}
				} else { // 如果是主表字段

					DBFieldInfoBean dbBean = this.getFieldBean(mainTableName, temp);
					if (dbBean == null) {
						// 说明字段不存在，要去掉本段公式
						isRight = false;
						break;
					}
					if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT || dbBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf("{")) + "nn('" + temp + "',0)" + cstr[i].substring(cstr[i].indexOf("}") + 1);
					} else {
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf("{")) + "n('" + temp + "')[0].value" + cstr[i].substring(cstr[i].indexOf("}") + 1);
					}
				}
			}

			if (!isRight) {
				cstr[i] = ""; // 公式不正确，如字段不存在
			} else if (!flagat) {
				// 取左边语句如果等于非数字，则制空。
				DBFieldInfoBean dbBean;
				if (setField.indexOf("_") > 0) {
					String tableName = setField.substring(0, setField.indexOf("_"));
					String fieldName = setField.substring(setField.indexOf("_") + 1);
					dbBean = this.getFieldBean(tableName, fieldName);
				} else {
					dbBean = this.getFieldBean(mainTableName, setField);
				}
				if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT || dbBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
					// 计算tempField的小数点位数
					int digits = 0;
					if (setField.indexOf("_") < 0) {
						digits = getDigitsOrSysSetting(mainTableName, setField);
					} else {
						digits = getDigitsOrSysSetting(setField.substring(0, setField.indexOf("_")), setField.substring(setField.indexOf("_") + 1));
					}
					if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT) {
						digits = 0;
					}
					cstr[i] = cstr[i].substring(0, cstr[i].lastIndexOf("=") + 1) + "f(" + cstr[i].substring(cstr[i].lastIndexOf("=") + 1) + "," + digits + ");";
				} else {
					cstr[i] += ";";
				}

			} else {
				pattern = Pattern.compile("([r|n]{1}\\([^\\(\\)]*\\)[^\\.]*)[^@\\(\\)=]*@fireEvent\\(['\"]{1}on([^'\"]*)['\"]{1}\\)");
				matcher = pattern.matcher(cstr[i]);
				if (matcher.find()) {
					// 取字段
					String fd = matcher.group(1);
					String ev = matcher.group(2);
					String all = matcher.group(0);

					if (cstr[i].indexOf("if") > -1) {
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf(all)) + "{ " + "if(document.createEvent){ var evt = document.createEvent('HTMLEvents');	evt.initEvent('" + ev.toLowerCase()
								+ "',true,false);" + fd + ".dispatchEvent(evt);\n" + "}else{ " + fd + ".fireEvent('on" + ev + "');  }     } " + cstr[i].substring(cstr[i].indexOf(all) + all.length());
					} else {
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf(all)) + " " + "if(document.createEvent){ var evt = document.createEvent('HTMLEvents');	evt.initEvent('" + ev.toLowerCase()
								+ "',true,false);" + fd + ".dispatchEvent(evt);\n" + "}else{ " + fd + ".fireEvent('on" + ev + "');  }     " + cstr[i].substring(cstr[i].indexOf(all) + all.length());
					}

				}
			}
			retStr += cstr[i] + "\n";

		}
		return retStr;
	}

	/**
	 * 取表字段的小数位数
	 * 
	 * @param table
	 *            String
	 * @param field
	 *            String
	 * @return int
	 */
	public static int getFieldDigits(String table, String field) {
		if (table == null || table.trim().length() == 0 || field == null || field.trim().length() == 0) {
			return 0;
		}

		Hashtable allTables = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return 0;
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				return fieldInfo.getDigits();
			}
		}
		return 0;
	}

	/**
	 * 取字段小数位，如没设置或为0，则取系统参数所设
	 * 
	 * @param tableName
	 *            String
	 * @param fieldName
	 *            String
	 * @return int
	 */
	public static int getDigitsOrSysSetting(String table, String field) {
		if (table == null || table.trim().length() == 0) {
			return 0;
		}

		Hashtable allTables = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return 0;
		}

		int digits = 0;
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				digits = fieldInfo.getDigits();				
			}
		}

		if (digits == 0) { // 字段没设置小数位，或没有这个字段

			if (tableInfo.getTableType() == 0) { // 主表

				digits = Integer.valueOf(getSysSetting("digits").toString());
			} else if (tableInfo.getTableType() == 1 || tableInfo.getTableType() == 2) { // 从表?

				digits = Integer.valueOf(getSysSetting("DetDigits").toString());
			}
		}
		
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {				
				if("Amount".equals(fieldInfo.getFieldIdentityStr())||"AmountIdentifier".equals(fieldInfo.getFieldIdentityStr())||"SAmountIdentifier".equals(fieldInfo.getFieldIdentityStr())){
					digits = Integer.valueOf(getSysSetting("DigitsAmount").toString());
				}
				else if("Price".equals(fieldInfo.getFieldIdentityStr())||"priceIdentifier".equals(fieldInfo.getFieldIdentityStr())||"SPriceIdentifier".equals(fieldInfo.getFieldIdentityStr())){
					digits = Integer.valueOf(getSysSetting("DigitsPrice").toString());
				}
				else if("Qty".equals(fieldInfo.getFieldIdentityStr())){
					digits = Integer.valueOf(getSysSetting("DigitsQty").toString());
				}
			}
		}

		return digits;
	}

	/**
	 * 按给定的小数位四舍五入
	 * 
	 * @param num
	 *            double
	 * @param digits
	 *            int
	 * @return double
	 */
	public static double round(double num, int digits) {
		return Math.round(num * Math.pow(10, digits)) / Math.pow(10, digits);
	}

	// 将字符串转换成整形
	public static int parseInts(String str) {
		int int_param = 0;
		try {
			int_param = Integer.parseInt(str);
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
		return int_param;
	}

	public String manCalulateEvent(String str, HashMap tblList, String mainTableName) {
		Pattern pattern = Pattern.compile("\\{([\\w]*)\\}@fireEvent\\(['\"]{1}on([^'\"]*)['\"]{1}\\)");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			// 取字段
			String fd = matcher.group(1);
			String ev = matcher.group(2);
			String all = matcher.group(0);

			str = str.substring(0, str.indexOf(all)) + " $('input[name=" + fd + "]').trigger('" + ev + "')" + str.substring(str.indexOf(all) + all.length());

			// 处理取值字段
			while (str.indexOf("{") > -1) {
				String temp = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
				if (str.indexOf("sum{") > -1) {
					int pos = str.indexOf("sum{") + 4;
					String right = str.substring(pos, str.indexOf("}", pos));
					str = str.substring(0, pos - 4) + "sm('" + right + "')" + str.substring(str.indexOf("}", str.indexOf("sum{")) + 1);
				} else if (temp.indexOf("_") > 0) { // 如果是从表字段
					String tableName = temp.substring(0, temp.indexOf("_"));
					String fieldName = temp.substring(temp.indexOf("_") + 1);
					int index = Integer.parseInt(tblList.get(tableName).toString());

					DBFieldInfoBean dbBean = this.getFieldBean(tableName, fieldName);
					if (dbBean == null) {
						return "";
					}
					// 如果字段类弄是数字，则用子函rn(转化
					if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT || dbBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						temp = "rn(" + index + ",\"" + fieldName + "\",j)";
						str = str.substring(0, str.indexOf("{")) + temp + str.substring(str.indexOf("}") + 1);
					} else {
						temp = "r(" + index + ",\"" + fieldName + "\")";
						str = str.substring(0, str.indexOf("{")) + temp + "[j].value" + str.substring(str.indexOf("}") + 1);
					}
				} else { // 如果是主表字段

					DBFieldInfoBean dbBean = this.getFieldBean(mainTableName, temp);
					if (dbBean == null) {
						// 说明字段不存在，要去掉本段公式
						return "";
					}
					if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT || dbBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						str = str.substring(0, str.indexOf("{")) + "nn('" + temp + "',0)" + str.substring(str.indexOf("}") + 1);
					} else {
						str = str.substring(0, str.indexOf("{")) + "n('" + temp + "')[0].value" + str.substring(str.indexOf("}") + 1);
					}
				}
			}

		}
		return str;
	}

	public String parseMainCaculate(HashMap tblList, String str, String mainTableName) {
		if (str == null) {
			return "";
		}

		// 替换公式中公共公式
		str = replacePubCaculate(null, mainTableName, tblList, str);
		// 替换公式中所有表名
		ArrayList childTableList = DDLOperation.getChildTables(mainTableName, BaseEnv.tableInfos);
		for (int i = 0; i < childTableList.size(); i++) {
			str = str.replaceAll("@CHILDTABLENAME" + i, (String) tblList.get("" + i));
		}

		// 分解多语句
		str = str.replaceAll("\\}\\{", "};{");
		String[] cstr = str.split(";");
		String retStr = "";
		for (int i = 0; i < cstr.length; i++) {
			boolean isRight = true; // 本段公式正确，如字段不存在，则要去掉该段公式
			if (cstr[i] == null || cstr[i].length() == 0) {
				continue;
			}
			// 判断公式中如果有系统参数，那么在这里直接用系统参数的值替换
			if (cstr[i].contains("@MEM:")) {
				// 解析语句得到有哪些系统参数
				ArrayList sysParams = new ArrayList();
				ConfigParse.parseSentenceGetParam(cstr[i], new ArrayList(), sysParams, new ArrayList(), new ArrayList(), new ArrayList(), null);
				for (int j = 0; j < sysParams.size(); j++) {
					SystemSettingBean ssb = BaseEnv.systemSet.get(sysParams.get(j));
					if (ssb == null) {
						throw new RuntimeException("公式中引用系统参数" + sysParams.get(j) + "不存在。" + cstr[i]);
					}
					cstr[i] = cstr[i].replaceAll("@MEM:" + sysParams.get(j), ssb.getSetting());
				}
			}

			if (cstr[i].indexOf("@") > -1) {
				// 有函数要触发
				retStr += manCalulateEvent(cstr[i], tblList, mainTableName) + ";\n";

				continue;
			}

			// 解释语句
			// zxy
			// 当主表公式触发明细表公式时，要以明细表的字段多行循环处理，这里需找到需赋值的字段，而来的找法，如果有if公式，且公式字段为主表时只能触发一行公式

			// 1、找赋值字段
			String setField = "";
			Pattern pattern = Pattern.compile("\\{([^\\{\\}]*)\\}[\\s]*[=@]{1}([^;=@]+)");
			Matcher matcher = pattern.matcher(cstr[i]);
			if (!matcher.find()) {
				throw new RuntimeException("公式解释错误，找不到需赋值字段:" + cstr[i] + "-----------" + str);
			}
			setField = matcher.group(1);
			// 2、赋值字段先用r(0,fieldName).value替换
			String childField = setField;
			boolean isChild = false;
			String strTemp = "";
			if (childField.indexOf("_") > 0) {
				isChild = true;
				String tableName = childField.substring(0, childField.indexOf("_"));
				String fieldName = childField.substring(childField.indexOf("_") + 1);
				int index = Integer.parseInt(tblList.get(tableName).toString());
				childField = "r(" + index + ",'" + fieldName + "')";
				strTemp = "for(var j=0;j<" + childField + ".length;j++){\n";
				// strTemp += "if(" + childField + "[j].value != ''){\n";

				String temp = "r(" + index + ",\"" + fieldName + "\")";
				//
				int sfPos = cstr[i].lastIndexOf("=");
				String sfTemp = cstr[i].substring(0, sfPos);
				sfPos = sfTemp.lastIndexOf("{" + setField + "}");
				cstr[i] = cstr[i].substring(0, sfPos) + temp + "[j].value" + cstr[i].substring(sfPos + ("{" + setField + "}").length());
			} else {

				cstr[i] = cstr[i].substring(0, cstr[i].lastIndexOf("{" + setField + "}")) + "n('" + setField + "')[0].value"
						+ cstr[i].substring(cstr[i].lastIndexOf("{" + setField + "}") + ("{" + setField + "}").length());
			}
			// 处理取值字段
			while (cstr[i].indexOf("{") > -1) {
				String temp = cstr[i].substring(cstr[i].indexOf("{") + 1, cstr[i].indexOf("}"));
				if (cstr[i].indexOf("sum{") > -1) {
					int pos = cstr[i].indexOf("sum{") + 4;
					String right = cstr[i].substring(pos, cstr[i].indexOf("}", pos));
					cstr[i] = cstr[i].substring(0, pos - 4) + "sm('" + right + "')" + cstr[i].substring(cstr[i].indexOf("}", cstr[i].indexOf("sum{")) + 1);
				} else if (temp.indexOf("_") > 0) { // 如果是从表字段
					String tableName = temp.substring(0, temp.indexOf("_"));
					String fieldName = temp.substring(temp.indexOf("_") + 1);
					int index = Integer.parseInt(tblList.get(tableName).toString());

					DBFieldInfoBean dbBean = this.getFieldBean(tableName, fieldName);
					if (dbBean == null) {
						// 说明字段不存在，要去掉本段公式
						isRight = false;
						break;
					}
					// 如果字段类弄是数字，则用子函rn(转化
					if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT || dbBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						temp = "rn(" + index + ",\"" + fieldName + "\",j)";
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf("{")) + temp + cstr[i].substring(cstr[i].indexOf("}") + 1);
					} else {
						temp = "r(" + index + ",\"" + fieldName + "\")";
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf("{")) + temp + "[j].value" + cstr[i].substring(cstr[i].indexOf("}") + 1);
					}
				} else { // 如果是主表字段

					DBFieldInfoBean dbBean = this.getFieldBean(mainTableName, temp);
					if (dbBean == null) {
						// 说明字段不存在，要去掉本段公式
						isRight = false;
						break;
					}
					if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT || dbBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf("{")) + "nn('" + temp + "',0)" + cstr[i].substring(cstr[i].indexOf("}") + 1);
					} else {
						cstr[i] = cstr[i].substring(0, cstr[i].indexOf("{")) + "n('" + temp + "')[0].value" + cstr[i].substring(cstr[i].indexOf("}") + 1);
					}
				}
			}

			// 取左边语句如果等于非数字，则制空。

			DBFieldInfoBean dbBean;
			if (setField.indexOf("_") > 0) {
				String tableName = setField.substring(0, setField.indexOf("_"));
				String fieldName = setField.substring(setField.indexOf("_") + 1);
				dbBean = this.getFieldBean(tableName, fieldName);
			} else {
				dbBean = this.getFieldBean(mainTableName, setField);
			}
			if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT || dbBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
				// 计算tempField的小数点位数
				int digits = 0;
				if (setField.indexOf("_") < 0) {
					digits = getDigitsOrSysSetting(mainTableName, setField);
				} else {
					digits = getDigitsOrSysSetting(setField.substring(0, setField.indexOf("_")), setField.substring(setField.indexOf("_") + 1));
				}
				if (dbBean.getFieldType() == DBFieldInfoBean.FIELD_INT) {
					digits = 0;
				}
				cstr[i] = cstr[i].substring(0, cstr[i].lastIndexOf("=") + 1) + "f(" + cstr[i].substring(cstr[i].lastIndexOf("=") + 1) + "," + digits + ");";
			} else {
				cstr[i] += ";";
			}
			// 主表onchange事件改变子表值后，再次触发子表onchange事件，以触发合计改变
			if (isChild) {
				// v7开始这里不能搞级联触发，这和gm 有区别，所有公式要写完整，否则会双重触发公式
				// cstr[i] +=
				// " \n if(typeof("+childField+"[j].onchange)=='function'){"+childField
				// + "[j].onchange();}";

				strTemp = strTemp + cstr[i] + "\n}\n";
			} else {
				strTemp = strTemp + cstr[i] + "\n"; // 没有for循环只一个括号去掉if
			}
			if (!isRight) {
				cstr[i] = ""; // 公式不正确，如字段不存在
			} else {
				cstr[i] = strTemp;
			}

			retStr += cstr[i] + "\n";
		}
		// Infinity
		return retStr;
	}

	/**
	 * 将字符串截取一定的长度
	 * 
	 * @param str
	 *            String
	 * @param length
	 *            int 去除后N位的长度
	 * @return String
	 */
	public String classCodeSubstring(String str, int lnglen) {
		String returnstr = "";
		if (str == null) {
			return "";
		}
		if (str.length() <= lnglen) {
			return "";
		}
		returnstr = str.substring(0, str.length() - lnglen);
		return returnstr;
	}

	public static String getSysSetting(String str) {
		SystemSettingBean bean = BaseEnv.systemSet.get(str);
		if (bean != null) {
			return BaseEnv.systemSet.get(str).getSetting();
		} else {
			return "";
		}
	}

	public static String getBol88() {
		return BaseEnv.bol88URL;
	}

	/**
	 * 取字段的显示名
	 * 
	 * @param fieldName
	 *            String tableName.fieldName
	 * @return String
	 */
	public String getFieldDisplay(String tableNeme, String popupName, String fieldName, String fieldType, String moduleUrl) {
		// 如果字段名中包含分号，代表是有多个表，则字段显示名返回空
		if (fieldName == null || fieldName.trim().length() == 0 || fieldName.indexOf(";") > 0) {
			return "";
		}
		
		
		Hashtable<String, KRLanguage> moduleLanguage = (Hashtable<String, KRLanguage>) application.getAttribute("moduleColLanguage");
		String strType = fieldType == null ? "bill" : fieldType;
		KRLanguage language = moduleLanguage.get(tableNeme + popupName + fieldName + strType);
		if (language != null) {
			return language.get(getLocale());
		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return table + " not Exist";
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(getLocale()).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}

	public String getFieldDisplay(String tableNeme, String popupName, String fieldName) {
		return getFieldDisplay(tableNeme, popupName, fieldName, null, null);
	}

	public String getFieldDisplay(String tableName, String popupName, String fieldName, String moduleUrl) {
		return getFieldDisplay(tableName, popupName, fieldName, null, moduleUrl);
	}

	/**
	 * 模块字段列名设置的 字段列名的显示名
	 * 
	 * @param fieldName
	 *            String tableName.fieldName
	 * @return String
	 */
	public String getFieldDisplay(String fieldName) {
		// 如果字段名中包含分号，代表是有多个表，则字段显示名返回空
		if (fieldName == null || fieldName.trim().length() == 0 || fieldName.indexOf(";") > 0) {
			return "";
		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return table + " not Exist";
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(getLocale()).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}

	public String getFieldDisplay2(String fieldName, String moduleUrl) {
		// 如果字段名中包含分号，代表是有多个表，则字段显示名返回空
		if (fieldName == null || fieldName.trim().length() == 0 || fieldName.indexOf(";") > 0) {
			return "";
		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return table + " not Exist";
		}

		
		

		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(getLocale()).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}

	/**
	 * 获取报表字段
	 * 
	 * @param reportNumber
	 * @return
	 */
	public DefineReportBean getReportFieldDisplay(String reportNumber) {
		if (reportNumber == null || reportNumber.length() == 0)
			return null;
		try {
			DefineReportBean defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(request).toString(), "1");
			return defBean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取报表字段
	 * 
	 * @param reportNumber
	 * @return
	 */
	public List<PopField> getPopupFieldDisplay(String inputValue) {
		if (inputValue == null || inputValue.length() == 0)
			return null;
		try {
			PopupSelectBean popup = (PopupSelectBean) BaseEnv.popupSelectMap.get(inputValue);
			return popup.getDisplayField2();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取报表查询条件中弹出框字段
	 * 
	 * @param reportNumber
	 * @return
	 */
	public static PopupSelectBean getPopupBean(String inputValue) {
		if (inputValue == null || inputValue.length() == 0)
			return null;
		try {
			return (PopupSelectBean) BaseEnv.popupSelectMap.get(inputValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 包含主表和从表字段
	 */
	public String getAllFieldDisplay(String fieldName) {
		if (fieldName == null || fieldName.trim().length() == 0 || fieldName.indexOf(";") > 0) {
			return "";
		}

		String tableName = fieldName.substring(0, fieldName.indexOf("."));
		fieldName = fieldName.substring(fieldName.indexOf(".") + 1);

		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);

		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);

		if (tableInfo != null) {
			ArrayList list = new ArrayList();
			list.addAll(tableInfo.getFieldInfos());
			list.addAll(GlobalsTool.getChildTablesFields(tableName, allTables));
			if (tableInfo != null) {
				for (int i = 0; i < list.size(); i++) {
					DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);

					if (fieldName.equalsIgnoreCase(fieldInfo.getFieldName())) {
						fieldName = fieldInfo.getDisplay() == null ? fieldInfo.getFieldName() : fieldInfo.getDisplay().get(getLocale().toString());
						break;
					}
				}
			}
		}
		return fieldName;
	}

	/**
	 * 把带点的字段名转为带“_”
	 * 
	 * @param fieldName
	 *            String
	 * @return String
	 */
	public static String getTableField(String fieldName) {
		return fieldName.substring(0, fieldName.indexOf(".")) + "_" + fieldName.substring(fieldName.indexOf(".") + 1);
	}

	// 根据分号分隔的表名返回表的中文显示名
	public String getTableDisplay(String tableNames) {
		String[] allTableName = tableNames.split(";");
		String tableName = "";
		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
		for (int i = 0; i < allTableName.length; i++) {
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(allTableName[i]);
			if (tableInfo != null) {
				tableName += tableInfo.getDisplay().get(getLocale()).toString() + ";";
			}
		}
		if (tableName.length() == 0) {
			tableName = tableNames;
		}
		return tableName;
	}

	// 根据表名返回表的中文显示名
	public String getTableDisplayName(String tableName) {
		String str = "";
		if (tableName == null) {
			return "";
		}
		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		if (tableInfo != null) {
			KRLanguage kr = tableInfo.getDisplay();
			if (kr != null) {
				str = kr.get(getLocale()).toString();
			}
		}

		return str;
	}

	/**
	 * 通过菜单链接找到表结构
	 * 
	 * @param linkAddress
	 * @return
	 */
	public static DBTableInfoBean getTableNameByModule(String linkAddress) {
		int index = linkAddress.indexOf("tableName=");
		int index2 = linkAddress.indexOf("reportNumber=");
		index = index > index2 ? (index + 10) : (index2 + 13);
		int index3 = linkAddress.indexOf("&", index);
		if (!linkAddress.contains("&") || index3 == -1) {
			linkAddress = linkAddress.substring(index, linkAddress.length());
		} else {
			linkAddress = linkAddress.substring(index, index3);
		}
		String tableName = linkAddress;
		DBTableInfoBean tableInfo = getTableInfoBean(tableName);
		return tableInfo;
	}

	/**
	 * 返回表结构
	 * 
	 * @param tableName
	 * @return
	 */
	public static DBTableInfoBean getTableInfoBean(String tableName) {
		if (tableName == null)
			return null;
		Hashtable allTables = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		return tableInfo;
	}

	public static boolean hasMainModule(String id) {
		if ("0".equals(id)) {
			return true;
		}
		for (Object o : SystemState.instance.moduleList) {
			if (o.toString().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到加密狗内容（模块）
	 * 
	 * @return
	 */
	public static List<String> getMainModule() {
		List<String> list = new ArrayList<String>();
		for (Object o : SystemState.instance.moduleList) {
			list.add(o.toString());
		}
		return list;
	}

	public SystemState getSystemState() {
		return SystemState.instance;
	}

	// 取资源文件,在非action类中，给出request和资源文件key即可
	public static String getMessage(HttpServletRequest request, String key) {
		Object o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		String msg = "";
		if (o instanceof MessageResources) {
			MessageResources resources = (MessageResources) o;
			msg = resources.getMessage(getLocale(request), key);
		}
		return msg;
	}

	// 取资源文件,在非action类中，给出request和资源文件key,参数
	public static String getMessage(HttpServletRequest request, String key, String param1) {
		Object o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		String msg = "";
		if (o instanceof MessageResources) {
			MessageResources resources = (MessageResources) o;
			msg = resources.getMessage(getLocale(request), key, param1);
		}
		return msg;
	}

	// 取资源文件,在非action类中，给出request和资源文件key,参数
	public static String getMessage(HttpServletRequest request, String key, String param1, String param2) {
		Object o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		String msg = "";
		if (o instanceof MessageResources) {
			MessageResources resources = (MessageResources) o;
			msg = resources.getMessage(getLocale(request), key, param1, param2);
		}
		return msg;
	}

	public static String getMessage(HttpServletRequest request, String key, String param1, String param2, String param3)

	{
		Object o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		String msg = "";
		if (o instanceof MessageResources) {
			MessageResources resources = (MessageResources) o;
			msg = resources.getMessage(getLocale(request), key, param1, param2, param3);
		}
		return msg;
	}

	// 将java.util.Date的时间转换为java.sql.Date时间
	public static java.sql.Date toSqlDate(java.util.Date sqldate) {
		java.sql.Date newsqlDate = null;
		if (null != sqldate) {
			try {
				newsqlDate = new java.sql.Date(sqldate.getTime());
			} catch (Exception ex) {
				newsqlDate = new java.sql.Date(new java.util.Date().getTime());
			}
		} else {
			newsqlDate = new java.sql.Date(new java.util.Date().getTime());
		}
		return newsqlDate;
	}

	// 中文乱码处理
	public static String toChinseChar(String str) {
		String newStr = "";
		if (str == null || str.trim().length() == 0) {
			return "";
		}
		try {
			newStr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			return str;
		}
		return newStr;
	}

	// 中文乱码处理
	public static String toChinseChar_2312(String str) {
		String newStr = "";
		if (str == null || str.trim().length() == 0) {
			return "";
		}
		try {
			newStr = new String(str.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException ex) {
			return str;
		}
		return newStr;
	}

	// 中文乱码处理
	public static String toChinseChar_GBK(String str) {
		String newStr = "";
		try {
			newStr = new String(str.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException ex) {
		}
		return newStr;
	}

	public static String getSequence() {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			Random random = new Random();
			result.append(random.nextInt(9));

		}
		return result.toString();
	}

	public static DBFieldInfoBean getFieldBean(String tableName, String fieldName) {
		Hashtable map = BaseEnv.tableInfos;
		DBFieldInfoBean bean = DDLOperation.getFieldInfo(map, tableName, fieldName);
		return bean;
	}

	public static DBFieldInfoBean getFieldBean(String fieldName) {
		if (fieldName == null || fieldName.length() == 0 || fieldName.indexOf(".") == -1) {
			return null;
		}
		Hashtable map = BaseEnv.tableInfos;
		String tableName = fieldName.substring(0, fieldName.indexOf("."));
		String strField = fieldName.substring(fieldName.indexOf(".") + 1);
		DBFieldInfoBean bean = DDLOperation.getFieldInfo(map, tableName, strField);
		return bean;
	}

	public static DBFieldInfoBean getFieldBean2(String fieldName) {
		if (fieldName == null || fieldName.length() == 0 || fieldName.indexOf("_") == -1) {
			return null;
		}
		Hashtable map = BaseEnv.tableInfos;
		String tableName = fieldName.substring(0, fieldName.indexOf("_"));
		String strField = fieldName.substring(fieldName.indexOf("_") + 1);
		DBFieldInfoBean bean = DDLOperation.getFieldInfo(map, tableName, strField);
		return bean;
	}

	public String getFieldDisplay(String tableName, String fieldName) {
		Hashtable map = BaseEnv.tableInfos;
		DBFieldInfoBean bean = DDLOperation.getFieldInfo(map, tableName, fieldName);
		if (bean == null)
			return "";
		return bean.getDisplay().get(getLocale(request).toString());
	}

	public static GoodsPropInfoBean getPropBean(String propName) {
		HashMap map = BaseEnv.propIgnoreCaseMap;
		GoodsPropInfoBean bean = (GoodsPropInfoBean) map.get(propName.toLowerCase());
		return bean;
	}

	public static GoodsAttributeBean getAttBean(String propName) {
		ArrayList<GoodsAttributeBean> att = BaseEnv.attList;
		for (int i = 0; i < att.size(); i++) {
			if (propName.equalsIgnoreCase(att.get(i).getPropName())) {
				return att.get(i);
			}
		}
		return null;
	}

	public static ArrayList<GoodsAttributeBean> getAllAtt() {
		return BaseEnv.attList;
	}

	public MOperation getMOperation() {
		LoginBean lb = (LoginBean) request.getSession().getAttribute("LoginBean");
		String url = "";
		HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
		String winCurIndex = request.getParameter("winCurIndex");
		// 多窗口中每个窗口的首页保存在winIndexMap中
		if (winIndexMap != null && winCurIndex != null && !winCurIndex.equals("-1") && winIndexMap.get(winCurIndex) != null) {
			request.setAttribute("winCurIndex", winCurIndex);
			url = (String) winIndexMap.get(winCurIndex);
		} else {
			url = request.getSession().getAttribute(BaseEnv.CUR_INDEX_URL) + "";
		}

		MOperation mo = (MOperation) lb.getOperationMap().get(url);
		return mo;
	}

	// 在程序中通过多窗口得到权限
	public static MOperation getMOperation(HttpServletRequest request) {
		LoginBean lb = (LoginBean) request.getSession().getAttribute("LoginBean");
		String url = "";
		HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
		String winCurIndex = request.getParameter("winCurIndex");
		// 多窗口中每个窗口的首页保存在winIndexMap中
		if (winIndexMap != null && winCurIndex != null && !winCurIndex.equals("-1") && winIndexMap.get(winCurIndex) != null) {
			request.setAttribute("winCurIndex", winCurIndex);
			url = (String) winIndexMap.get(winCurIndex);
		} else {
			url = request.getSession().getAttribute(BaseEnv.CUR_INDEX_URL) + "";
		}

		MOperation mo = (MOperation) lb.getOperationMap().get(url);
		return mo;
	}

	/**
	 * 查看是否启用序列号
	 * 
	 * @return
	 */
	public static boolean isExistSeq() {
		boolean isExist = false;
		Hashtable propMap = BaseEnv.propMap;
		Iterator it = propMap.keySet().iterator();
		while (it.hasNext()) {
			GoodsPropInfoBean propInfo = (GoodsPropInfoBean) propMap.get(it.next());
			if (propInfo.getIsSequence() == 1 && propInfo.getIsUsed() == 1) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	/**
	 * 拆分序列号字符串
	 * 
	 * @param seqStr
	 * @return
	 */
	public static ArrayList getSeqList(String seqStr) {
		ArrayList list = new ArrayList();
		if (!"".equals(seqStr)) {
			String[] strs = seqStr.split(";");
			for (int i = 0; i < strs.length; i++) {
				String[] values = strs[i].split(":[)]");
				if (values[1].equals("1")) {
					list.add(values[2]);
				} else {
					String[] items = values[2].split("~");
					String startNum = items[2];
					int start_n = Integer.parseInt(startNum);
					if (startNum.length() > String.valueOf(start_n).length()) {
						String zeroStr = startNum.substring(0, startNum.length() - String.valueOf(start_n).length());
						for (int j = 0; j < Integer.parseInt(items[3]); j++) {
							int temp = start_n + j;
							String last = zeroStr + temp;
							int disc = last.length() - startNum.length();
							last = last.substring(disc);
							list.add(items[0] + last + items[1]);

						}
					} else {
						for (int j = 0; j < Integer.parseInt(items[3]); j++) {
							list.add(items[0] + (start_n + j) + items[1]);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 字符串拆分成数组
	 * 
	 * @param str
	 * @param flag
	 * @return
	 */
	public static Object[] strSplit(String str, String flag) {
		if (str == null || str.endsWith(flag) || str.length() == 0) { // zxy
																		// 很多情况下str最后一位是有,号的，但不知道什么时候有些数据最后没有,号了，不确定对程序有多大影响，故做此判断，如无,号结束，按另一规则处理
			List list = new ArrayList();
			while (str != null && str.length() > 0 && str.indexOf(flag) >= 0) {
				int i = str.indexOf(flag);
				String testStr = "";
				if (i != -1) {
					testStr = str.substring(0, i);
				}
				str = str.substring(i + 1);
				list.add(testStr);
			}
			return list.toArray();
		} else {
			List list = new ArrayList();
			for (String s : str.split(flag)) {
				list.add(s);
			}
			return list.toArray();
		}
	}

	public String[] strSplitByFlag(String str, String flag) {
		if (str != null && flag != null) {
			String[] strArray = str.split("\\" + flag);
			return strArray;
		}
		return null;
	}

	public int getArrayLength(String str, String flag) {
		if (str != null && str.length() > 0 && flag != null) {
			String[] strArray = str.split("\\" + flag);
			return strArray.length;
		}
		return 0;
	}

	/**
	 * 将字符形式数字转化为int 类型
	 * 
	 * @param intStr要转换的字符串
	 * @param defaultValue如果转化失败的默认值
	 * @return
	 */
	public static int formatInt(String intStr, int defaultValue) {

		try {
			defaultValue = Integer.parseInt(intStr);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public static List getAccountInfo() {
		ArrayList list = new ArrayList();

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document document = dbf.newDocumentBuilder().parse("../../config/AccountInfo.xml");
			Node config = document.getFirstChild();
			NodeList nodeList = config.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node tempNode = nodeList.item(i);
				if (tempNode.getNodeName().equalsIgnoreCase("account")) {
					Node attNode = tempNode.getAttributes().getNamedItem("status");
					if (attNode != null && "0".equals(attNode.getNodeValue())) {
						KeyPair kp = new KeyPair();
						attNode = tempNode.getAttributes().getNamedItem("accountName");
						kp.setName(attNode == null ? "null" : attNode.getNodeValue());
						attNode = tempNode.getAttributes().getNamedItem("httpPort");
						kp.setValue(attNode == null ? "null" : attNode.getNodeValue());
						list.add(kp);
					}
				}
			}
		} catch (Exception ex) {
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public boolean getScopeRight(String tableName, String fieldName, ArrayList scopeRight) {

		return getScopeRight(tableName, fieldName, scopeRight, request);

	}

	public static boolean getScopeRight(String tableName, String fieldName, ArrayList scopeRight, HttpServletRequest request) {
		boolean viewRight = true;

		/* 判断是否勾选了隐藏成本价、隐藏销售价等 begin--- */
		// 获取当前字段的字段标识
		DBFieldInfoBean fileBean = getFieldBean(tableName, fieldName);
		if (fileBean != null) {
			String fieldIdentity = fileBean.getFieldIdentityStr();
			if (!"".equals(fieldIdentity) && fieldIdentity != null) {
				// 获取登录用户的所有角色

				if (fieldIdentity.equals("priceIdentifier") || fieldIdentity.equals("AmountIdentifier")) { // 判断是否隐藏成本价
					fieldIdentity = "1";
				} else if (fieldIdentity.equals("SPriceIdentifier") || fieldIdentity.equals("SAmountIdentifier")) { // 判断是否隐藏销售价
					fieldIdentity = "2";
				} else if (fieldIdentity.equals("Customer")) { // 判断是否隐藏客户
					fieldIdentity = "3";
				} else if (fieldIdentity.equals("Supplier")) { // 判断是否隐藏供应商
					fieldIdentity = "4";
				} else if (fieldIdentity.equals("CompanyCodeIdentifier")) { // 判断是否隐藏往来单位
					fieldIdentity = "5";
				}

				if (("," + getLoginBean(request).getHiddenField()).contains("," + fieldIdentity))
					viewRight = false;
			}
		}
		/*--end--*/

		if (scopeRight != null) {
			for (Object sco : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) sco;
				if (lsb.getFlag().equals("4") && "1".equals(lsb.getScopeValue())) {
					if ((lsb.getTableName().trim().equalsIgnoreCase(tableName) && lsb.getFieldName().trim().equalsIgnoreCase(fieldName))) {
						viewRight = false;
						break;
					}
				}
			}
		}
		return viewRight;
	}

	/**
	 * 取明细表的只读范围权限
	 * 
	 * @param tableName
	 * @param fieldName
	 * @param scopeRight
	 * @return
	 */
	public ArrayList getScopeRightChildReadOnly(String tableName, ArrayList scopeRight) {
		ArrayList list = new ArrayList();
		if (scopeRight != null) {
			for (Object sco : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) sco;
				if (lsb.getFlag().equals("4") && "1".equals(lsb.getEscopeValue())) {
					DBTableInfoBean tb = this.getTableInfoBean(lsb.getTableName());
					if (tb != null && tb.getPerantTableName() != null && tb.getPerantTableName().indexOf(tableName) > -1) {
						// 主表的不处理
						list.add(new String[] { lsb.getTableName(), lsb.getFieldName() });
					}
				}
			}
		}
		return list;
	}

	/**
	 * 此方法只用于详情页面，那么就取当前登录用户的最后一个办理过的节点
	 * 
	 * @param designId
	 * @param fieldName
	 * @param tableName
	 * @param currNodeId
	 * @param flowId
	 * @param f_brother
	 * @return
	 */
	public boolean getFlowFieldRight(String designId, String fieldName, String tableName, String currNodeId, String flowId, String f_brother, String loginId, String mainTabName) {
		boolean viewRight = true;
		if (f_brother != null && f_brother.length() > 0)
			flowId = f_brother;
		WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
		if (designBean != null) {
			Result rcn = pubMgt.getCurrNodeId(flowId, mainTabName);
			currNodeId = rcn.retVal == null ? null : rcn.retVal.toString();
			Result rln = pubMgt.getUserLastNode(flowId, loginId, mainTabName);
			String lastNode = rln.retVal == null ? null : rln.retVal.toString();
			if (lastNode == null || lastNode.length() == 0) {
				lastNode = currNodeId;
			}
			if (lastNode == null) {
				return true;
			}
			if (designBean.getFlowNodeMap().get(lastNode) != null) {
				ArrayList hidFields = designBean.getFlowNodeMap().get(lastNode).getHiddenFields();
				DBFieldInfoBean field = getFieldBean(tableName, fieldName);
				FieldBean fb = null;
				fb = GlobalsTool.getFlowField(designBean.getFlowNodeMap().get(lastNode).getFields(), tableName + "_" + field.getFieldName());
				if (fb != null && fb.getInputType() == 3) {
					if (currNodeId.equals("-1") && hidFields != null) {
						for (int i = 0; i < hidFields.size(); i++) {
							if (hidFields.get(i).equals(tableName + "_" + field.getFieldName())) {
								return true;
							}
						}
						return false;
					} else {
						return false;
					}
				}
			}
		}
		return viewRight;
	}

	/**
	 * 根据字段名称获取流程结点设置字段(隐藏，只读，必填)
	 * 
	 * @param fields
	 * @param fieldName
	 * @return
	 */
	public static FieldBean getFlowField(List<FieldBean> fields, String fieldName) {
		if (fields == null || fields.size() == 0)
			return null;
		for (FieldBean field : fields) {
			if (field.getFieldName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	public static int getMaxDay(int year, int month) {
		int day = 31;
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			day = 30;
		} else if (month == 2) {
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
				day = 29;
			} else {
				day = 28;
			}
		}

		return day;
	}

	public static int getFieldWidth(String tableName, String colName) {
		Hashtable<String, ColDisplayBean> userSettingWidth = (Hashtable<String, ColDisplayBean>) application.getAttribute("userSettingWidth");
		if (userSettingWidth != null && userSettingWidth.size() > 0) {
			ColDisplayBean bean = userSettingWidth.get(tableName + colName);
			if (bean != null) {
				String width = bean.getColWidth();
				if (width.indexOf(".") > -1) {
					width = width.substring(0, width.indexOf("."));
				}
				return Integer.parseInt(width);
			}
		}
		return 0;
	}

	public static boolean colIsExistConfigList(String tableName, String fieldName, String colType) {
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) application.getAttribute("userSettingColConfig");
		if (userColConfig != null && userColConfig.size() > 0) {
			ArrayList<ColConfigBean> configList = userColConfig.get(tableName + colType);
			if (configList != null && configList.size() > 0) {
				for (ColConfigBean colBean : configList) {
					if (fieldName.equals(colBean.getColName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean hasUsedSeq(Object o) {
		boolean flag = false;
		if (o instanceof ArrayList) {
			for (int i = 0; i < ((ArrayList) o).size(); i++) {
				flag = exsistUsedSeq((DBTableInfoBean) ((ArrayList) o).get(i));
				if (flag) {
					break;
				}
			}
		} else if (o instanceof DBTableInfoBean) {
			flag = exsistUsedSeq((DBTableInfoBean) o);
		} else if (o instanceof String) {
			Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(o);
			if (tableInfo != null) {
				flag = exsistUsedSeq(tableInfo);
			}
		}
		return flag;
	}

	private static boolean exsistUsedSeq(DBTableInfoBean tableInfo) {
		boolean exist = false;
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE && "GoodsField".equals(fieldInfo.getFieldSysType())) {
				GoodsPropInfoBean propInfo = getPropBean(fieldInfo.getFieldName());
				if (propInfo != null && propInfo.getIsUsed() == 1 && propInfo.getIsSequence() == 1) {
					exist = true;
					break;
				}
			}
		}
		return exist;
	}

	public static String getSeqDis(Object seqStr) {
		String res = "";
		if (seqStr != null && !seqStr.equals("")) {
			String[] seqList = seqStr.toString().split("~");
			res = seqList[seqList.length - 1];
		}
		return res;
	}

	public static String getReportNameByReportNumber(String reportNumber, HttpServletRequest req) {
		DefineReportBean defBean = null;
		try {
			Object o = req.getSession().getAttribute("LoginBean");
			String userId = "1";
			if (o != null) {
				userId = ((LoginBean) o).getId();
			}
			defBean = DefineReportBean.parse(reportNumber + "SQL.xml", getLocale(req).toString(), userId);
			return defBean.getReportName();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 根据日期获取当天是星期几
	 */
	public String getWeekByDate(String strDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String strWeek = "";

		switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
		case 0:
			strWeek = getMessage(request, "oa.calendar.sunday");
			break;
		case 1:
			strWeek = getMessage(request, "oa.calendar.monday");
			break;
		case 2:
			strWeek = getMessage(request, "oa.calendar.tuesday");
			break;
		case 3:
			strWeek = getMessage(request, "oa.calendar.wednesday");
			break;
		case 4:
			strWeek = getMessage(request, "oa.calendar.thursday");
			break;
		case 5:
			strWeek = getMessage(request, "oa.calendar.friday");
			break;
		case 6:
			strWeek = getMessage(request, "oa.calendar.saturday");
			break;
		default:
			break;
		}
		return strWeek;
	}

	/**
	 * 2012-08-09 转换成 8月9号
	 */
	public String getDate(String strDate) {
		if (strDate == null || strDate.length() == 0) {
			return "";
		}
		strDate = strDate.substring(5, 10).replaceAll("-0", "-").replace("-", "月") + "号";
		return strDate;
	}

	// 字符串转数组
	public static String[] stringToArray(String str, String sign) {
		return str.split(sign);
	}

	// 字符串截取
	public static String interceptString(String str, int begin, int end) {
		return str.substring(begin, end);
	}

	public static int daystudyNum() {
		Random rd = new Random(System.currentTimeMillis());
		int rdNum = rd.nextInt(BaseEnv.dayStudyNum);
		if (rdNum == 0) {
			rdNum = 1;
		}
		return rdNum;
	}

	/**
	 * 获取弹出窗口必须输入的字段
	 * 
	 * @param tableName
	 *            表名
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public String getMainInputValue(String tableName, String fieldName) {
		String inputValue = "";
		DBFieldInfoBean fieldInfo = getFieldBean(tableName, fieldName);
		if (fieldInfo != null && fieldInfo.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE && fieldInfo.getInputValue() != null && fieldInfo.getInputValue().length() > 0
				&& fieldName.equals(fieldInfo.getFieldName())) {
			ArrayList<String> paramList = fieldInfo.getSelectBean() == null ? null : fieldInfo.getSelectBean().getTableParams();
			if (paramList != null) {
				for (String str : paramList) {
					DBFieldInfoBean paramBean = getFieldBean(tableName, str);
					if (paramBean != null && paramBean.getIsNull() == 1) {
						inputValue += paramBean.getIsNull() + paramBean.getDisplay().get(getLocale(request).toString()) + "@" + str + ":";
					}
				}
			}
		}
		return inputValue;
	}

	/**
	 * 查看当前详情页面是否有上下篇
	 * 
	 * @param keyId
	 * @return
	 */
	// public String hasNext(String tableName, String parentName, String keyId,
	// String f_brother,String moduleType) {
	//
	// HashMap hasNextMap = (HashMap) request.getSession().getAttribute(
	// "hasNextMap");
	// TreeMap<Integer, String> nextMap = null;
	// if (hasNextMap != null) {
	// nextMap = (TreeMap<Integer, String>) hasNextMap.get(tableName);
	// }
	// StringBuffer bf = new StringBuffer();
	// String winCurIndex = request.getParameter("winCurIndex");
	// if (nextMap != null && nextMap.size() > 0) {
	// Iterator<Integer> iter = nextMap.keySet().iterator();
	// Integer currNum = 0;
	// while (iter.hasNext()) {
	// Integer number = iter.next();
	// if (keyId.equals(nextMap.get(number))) {
	// currNum = number;
	// break;
	// }
	// }
	//
	// if (currNum == 1) {
	// if (nextMap.size() != 1) {
	// bf.append("<li><span class='hBtns' onclick=\"location.href='/UserFunctionAction.do?tableName="
	// + tableName
	// + "&keyId="
	// + nextMap.get(2)
	// + "&parentTableName="
	// + parentName
	// + "&moduleType="+moduleType+"&f_brother="
	// + f_brother
	// + "&operation=5&queryChannel=normal&winCurIndex="
	// + winCurIndex
	// + "'\">"
	// + getMessage(request, "oa.common.nextPiece")
	// + "</span></li>");
	// }
	// } else if (currNum == nextMap.size() || (keyId==null ||
	// keyId.length()==0)) {
	// bf.append("<li><span class='hBtns' onclick=\"location.href='/UserFunctionAction.do?tableName="
	// + tableName
	// + "&keyId="
	// + ((keyId==null || keyId.length()==0)?nextMap.get(1):nextMap.get(currNum
	// - 1))
	// + "&parentTableName="
	// + parentName
	// + "&moduleType="+moduleType+"&f_brother="
	// + f_brother
	// + "&operation=5&queryChannel=normal&winCurIndex="
	// + winCurIndex
	// + "'\">"
	// + getMessage(request, "oa.common.backPiece")
	// + "</span></li>");
	// } else {
	// bf.append("<li><span class='hBtns' onclick=\"location.href='/UserFunctionAction.do?tableName="
	// + tableName
	// + "&keyId="
	// + nextMap.get(currNum - 1)
	// + "&parentTableName="
	// + parentName
	// + "&moduleType="+moduleType+"&f_brother="
	// + f_brother
	// + "&operation=5&queryChannel=normal&winCurIndex="
	// + winCurIndex
	// + "'\">"
	// + getMessage(request, "oa.common.backPiece")
	// + "</span></li>");
	// bf.append("<li><span class='hBtns' onclick=\"location.href='/UserFunctionAction.do?tableName="
	// + tableName
	// + "&keyId="
	// + nextMap.get(currNum + 1)
	// + "&parentTableName="
	// + parentName
	// + "&moduleType="+moduleType+"&f_brother="
	// + f_brother
	// + "&operation=5&queryChannel=normal&winCurIndex="
	// + winCurIndex
	// + "'\">"
	// + getMessage(request, "oa.common.nextPiece")
	// + "</span></li>");
	// }
	// }
	// return bf.toString();
	// }

	/**
	 * 查看当前客户详情页面是否有上下篇
	 * 
	 * @param keyId
	 * @return
	 */
	// public String hasNext(String keyId) {
	//
	// HashMap hasNextMap = (HashMap) request.getSession().getAttribute(
	// "hasNextMap");
	// TreeMap<Integer, String> nextMap = null;
	// if (hasNextMap != null) {
	// nextMap = (TreeMap<Integer, String>) hasNextMap
	// .get("CRMClientInfo");
	// }
	// StringBuffer bf = new StringBuffer();
	// String winCurIndex = request.getParameter("winCurIndex");
	// if (nextMap != null && nextMap.size() > 0) {
	// Iterator<Integer> iter = nextMap.keySet().iterator();
	// Integer currNum = 0;
	// while (iter.hasNext()) {
	// Integer number = iter.next();
	// if (keyId.equals(nextMap.get(number))) {
	// currNum = number;
	// break;
	// }
	// }
	// if (currNum == 1) {
	// if (nextMap.size() != 1) {
	// bf.append("<button type='button' onclick=\"hasNext('" + nextMap.get(2)
	// + "');\">"
	// + getMessage(request, "oa.common.nextPiece")
	// + "</button>");
	// }
	// } else if (currNum == nextMap.size()) {
	// bf.append("<button type='button' onclick=\"hasNext('"
	// + nextMap.get(currNum - 1) + "');\">"
	// + getMessage(request, "oa.common.backPiece")
	// + "</button>");
	// } else {
	// bf.append("<button type='button' onclick=\"hasNext('"
	// + nextMap.get(currNum - 1) + "');\">"
	// + getMessage(request, "oa.common.backPiece")
	// + "</button>");
	// bf.append("<button type='button' onclick=\"hasNext('"
	// + nextMap.get(currNum + 1) + "');\">"
	// + getMessage(request, "oa.common.nextPiece")
	// + "</button>");
	// }
	// }
	// return bf.toString();
	// }

	public static boolean hasOrderApproved(String checkPerson, String userId, int orderIndex) {
		if (orderIndex < 0)
			return true;
		String[] arrCheck = checkPerson.split(",");
		int order = 0;
		for (String check : arrCheck) {
			if (check.length() > 0) {
				if (userId.equals(check) && order == orderIndex) {
					return true;
				}
				order++;
			}
		}
		return false;
	}

	public String getDepartByClassId(String id) {
		HashMap deptMap = userMgt.queryDeptByCode(id, "ID");
		if (deptMap != null && deptMap.get("DeptFullName") != null) {
			return String.valueOf(deptMap.get("DeptFullName"));
		}
		return "";
	}

	public String getDeptIdByCode(String code) {
		HashMap deptMap = userMgt.queryDeptByCode(code, "Code");
		if (deptMap != null && deptMap.get("DeptFullName") != null) {
			return String.valueOf(deptMap.get("id"));
		}
		return "";
	}

	public boolean fileExists(String partPath) {
		String path = request.getSession().getServletContext().getRealPath("vm/" + partPath);
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 转换枚举对象
	 * 
	 * @param enumeration
	 * @return
	 */
	public Map caseEnumeration(String enumeration) {
		Map map = new HashMap();
		List enuList = getEnumerationItems(enumeration);
		for (int i = 0; i < enuList.size(); i++) {
			KeyPair kp = (KeyPair) enuList.get(i);
			map.put(kp.getValue(), kp.getName());

		}
		return map;
	}

	/**
	 * 获取预警方式
	 * 
	 * @param preMode
	 * @param sign
	 */
	public String getPre(String preMode, String sign) {
		// 判断是否存在
		StringBuilder sb = new StringBuilder();
		if (null == preMode || "".equals(preMode.trim())) {
			return sb.append("无设置").toString();
		} else {
			Map map = caseEnumeration("AlertMode");
			String[] preModes = preMode.split(",");
			for (int i = 0; i < preModes.length; i++) {
				sb.append(map.get(preModes[i])).append(sign);
			}
			return sb.substring(0, sb.length() - 1);
		}
	}

	public boolean has(String preModes, String preMode) {
		boolean flag = false;
		if (null == preModes || "".equals(preMode.trim()))
			flag = false;
		else {
			String[] pres = preModes.split(",");
			int search = Arrays.binarySearch(pres, preMode);
			flag = search >= 0 ? true : false;
		}
		return flag;
	}

	public String getUser(ArrayList<AlertDataBean> list, String sign) {
		StringBuilder sb = new StringBuilder();
		if (list.size() == 0) {
			return sb.append(this.getMessage(request, "alert.set.noSet")).toString();
		} else {
			for (AlertDataBean adb : list) {
				sb.append(adb.getEmpFullName()).append(sign);
			}
			return sb.substring(0, sb.length() - 1);
		}
	}

	/**
	 * 根据用户Id获取用户名称（用户Id之间用逗号隔开）
	 * 
	 * @param userIds
	 * @return
	 */
	public ArrayList<OnlineUser> queryUserName(String userIds, String type) {
		ArrayList<OnlineUser> userList = new ArrayList<OnlineUser>();
		if (userIds != null && userIds.length() > 0) {
			String[] arrayUser = userIds.split(",");
			for (String userId : arrayUser) {
				OnlineUser user = null;
				if ("dept".equals(type)) {
					user = OnlineUserInfo.getDept(userId);
				} else {
					user = OnlineUserInfo.getUser(userId);
				}
				if (user != null) {
					userList.add(user);
				}
			}
		}
		return userList;
	}

	/**
	 * 判断是否在线
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isOnline(String userId) {
		OnlineUser user = OnlineUserInfo.getUser(userId);
		if (user == null)
			return false;

		return user.isOnline();
	}

	public boolean hasUser(String empid, ArrayList<AlertDataBean> beanList) {
		boolean flag = false;
		if (null == beanList || beanList.size() == 0) {
			flag = false;
		} else {
			for (AlertDataBean adb : beanList) {
				if (empid.equals(adb.getEmpid())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	public String getCataTableDisplay(String tableName) {

		ArrayList allTables = (ArrayList) request.getSession().getServletContext().getAttribute("scopeCata");
		for (int i = 0; i < allTables.size(); i++) {
			Object[] cataTab = (Object[]) allTables.get(i);
			if (cataTab[0].equals(tableName)) {
				tableName = ((KRLanguage) cataTab[3]).get(this.getLocale(request).toString());
			}
		}
		return tableName;
	}

	// 将对象转字符串
	public static String getStringDate(Object obj) {
		if (obj == null) {
			return "";
		} else {

			return obj.toString();
		}

	}

	public static ArrayList getChildTablesFields(final String tableName, final Map allTables) {
		if (tableName == null || tableName.length() == 0) {
			return null;
		}
		ArrayList list = new ArrayList();
		ArrayList fieldList = new ArrayList();
		// 找子表
		Iterator it = allTables.values().iterator();
		while (it.hasNext()) {
			DBTableInfoBean ti = (DBTableInfoBean) it.next();
			if (ti.getTableType() == DBTableInfoBean.CHILD_TABLE && ti.getPerantTableName() != null && ti.getPerantTableName().length() > 0) {
				String[] parentNames = ti.getPerantTableName().split(";");
				boolean isExist = false;
				for (int i = 0; i < parentNames.length; i++) {
					if (parentNames[i].equals(tableName)) {
						isExist = true;
					}
				}
				if (isExist) {
					list.add(ti);
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			DBTableInfoBean ti = (DBTableInfoBean) list.get(i);
			fieldList.addAll(ti.getFieldInfos());
		}

		return fieldList;
	}

	/**
	 * 获取主表和子表的字段
	 * 
	 * @return
	 */
	public List<DBFieldInfoBean> getAllFieldList(String tableName) {
		if (tableName == null || tableName.length() == 0) {
			return null;
		}
		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean mainTable = DDLOperation.getTableInfo(allTables, tableName);
		if (mainTable == null)
			return null;
		ArrayList<DBTableInfoBean> childTables = DDLOperation.getChildTables(tableName, allTables);
		List<DBFieldInfoBean> allList = new ArrayList<DBFieldInfoBean>();
		allList.addAll(mainTable.getFieldInfos());
		for (DBTableInfoBean table : childTables) {
			allList.addAll(table.getFieldInfos());
		}
		return allList;
	}

	/**
	 * 获取主表和子表的字段
	 * 
	 * @return
	 */
	public List<DBTableInfoBean> getChildTable(String tableName) {
		if (tableName == null || tableName.length() == 0) {
			return null;
		}
		Hashtable allTables = (Hashtable) application.getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean mainTable = DDLOperation.getTableInfo(allTables, tableName);
		if (mainTable == null)
			return null;
		ArrayList<DBTableInfoBean> childTables = DDLOperation.getChildTables(tableName, allTables);
		return childTables;
	}

	/**
	 * 数据导入时判断哪些字段可以导入
	 * 
	 * @param field
	 *            DBFieldInfoBean
	 * @return boolean
	 */
	public static boolean canImportField(DBFieldInfoBean field) {
		if (field.getInputType() == DBFieldInfoBean.INPUT_NO || field.getFieldType() == DBFieldInfoBean.FIELD_AFFIX || field.getDisplay() == null || field.getFieldName().equals("id")
				|| field.getFieldName().equals("classCode") || field.getFieldName().equals("workFlowNode") || field.getFieldName().equals("workFlowNodeName")
				|| field.getFieldName().equals("createBy") || field.getFieldName().equals("lastUpdateBy") || field.getFieldName().equals("createTime") || field.getFieldName().equals("lastUpdateTime")
				|| field.getFieldName().equals("statusId") || field.getFieldName().equals("SCompanyID") || field.getFieldName().endsWith("PYM")) {
			return false;
		}
		return true;
	}

	/**
	 * 流程设计-字段设置
	 * 
	 * @param field
	 *            DBFieldInfoBean
	 * @return boolean
	 */
	public static boolean canDisField2(DBFieldInfoBean field) {
		if (field.getInputType() == DBFieldInfoBean.INPUT_NO || field.getInputType() == DBFieldInfoBean.INPUT_HIDDEN 
				|| field.getInputType()==DBFieldInfoBean.INPUT_CUT_LINE ||  field.getFieldType() == DBFieldInfoBean.FIELD_AFFIX
				|| field.getFieldType() == DBFieldInfoBean.FIELD_PIC || field.getDisplay() == null || field.getFieldName().equals("id") || field.getFieldName().equals("classCode")
				|| field.getFieldName().equals("workFlowNode") || field.getFieldName().equals("workFlowNodeName") || field.getFieldName().equals("createBy")
				|| field.getFieldName().equals("lastUpdateBy") || field.getFieldName().equals("createTime") || field.getFieldName().equals("lastUpdateTime") || field.getFieldName().equals("statusId")
				|| field.getFieldName().equals("SCompanyID") || field.getFieldName().endsWith("PYM")) {
			return false;
		}
		return true;
	}

	/**
	 * 数据导入时判断哪些字段可以导入
	 * 
	 * @param field
	 *            DBFieldInfoBean
	 * @return boolean
	 */
	public static boolean canDisField(DBFieldInfoBean field) {
		if (field.getInputType() == DBFieldInfoBean.INPUT_NO || field.getDisplay() == null || field.getFieldName().equals("id") || field.getFieldName().equals("classCode")
				|| field.getFieldName().equals("workFlowNode") || field.getFieldName().equals("workFlowNodeName") || field.getFieldName().equals("createBy")
				|| field.getFieldName().equals("lastUpdateBy") || field.getFieldName().equals("createTime") || field.getFieldName().equals("lastUpdateTime") || field.getFieldName().equals("statusId")
				|| field.getFieldName().equals("SCompanyID") || field.getFieldName().endsWith("PYM")) {
			return false;
		}
		return true;
	}

	/**
	 * 客戶模版中的可选择字段
	 * 
	 * @param field
	 * @return
	 */
	public static boolean canSelectField(DBFieldInfoBean field, String mainTableName, String childTableName) {
		/* 过滤客户中的字段 */
		if (field.getTableBean().getTableName().equals(mainTableName)
				&& (field.getFieldName().equals("id") || field.getFieldName().equals("f_ref") || field.getFieldName().equals("f_brother") || field.getFieldName().equals("workFlowNode")
						|| field.getFieldName().equals("statusId") || field.getFieldName().equals("workFlowNodeName") || field.getInputType() == DBFieldInfoBean.INPUT_HIDDEN || field.getInputType() == DBFieldInfoBean.INPUT_NO)) {
			return false;
		}
		/* 过滤联系人中的字段 */
		if (field.getTableBean().getTableName().equals(childTableName)
				&& (field.getFieldName().equals("id") || field.getFieldName().equals("f_ref") || field.getFieldName().equals("workFlowNode") || field.getFieldName().equals("workFlowNodeName")
						|| field.getInputType() == DBFieldInfoBean.INPUT_HIDDEN || field.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT || field.getInputType() == DBFieldInfoBean.INPUT_NO)) {
			return false;
		}
		return true;
	}

	/**
	 * 取字段的显示名
	 * 
	 * @param fieldName
	 *            String tableName.fieldName
	 * @return String
	 */
	public static String getFieldDisplay(Hashtable allTables, String fieldName, String locale) {
		if (fieldName == null || fieldName.trim().length() == 0) {
			return null;
		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return null;
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(locale).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}

	/**
	 * 通过字段的显示名取字段信息
	 * 
	 * @param fieldName
	 *            String tableName.fieldName
	 * @return String
	 */
	public static DBFieldInfoBean getFieldByDisplay(String tableName, String fieldDisName, String locale) {
		if (fieldDisName == null || fieldDisName.trim().length() == 0) {
			return null;
		}

		DBTableInfoBean tableInfo = (DBTableInfoBean) BaseEnv.tableInfos.get(tableName);
		if (tableInfo == null) {
			return null;
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getDisplay() != null && fieldInfo.getDisplay().get(locale) != null) {
				String display = fieldInfo.getDisplay().get(locale).toString();
				if (fieldDisName.equals(display)) {
					return fieldInfo;
				}
			}
		}
		return null;
	}

	public static String checkAdv() {
		AIOBOL88Mgt mgt = new AIOBOL88Mgt();
		Result rs = mgt.queryBol88();
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			AIOBOL88Bean bean = (AIOBOL88Bean) rs.getRetVal();
			if (bean != null && bean.getFlag() == 0) {
				return bean.getUserName();
			} else if (bean != null && bean.getFlag() == 1) {
				return "";
			}
		}
		if (SystemState.instance.dogState == 0 || SystemState.instance.dogState == 4) {
			// 正版用户未开通，不显示广告
			return "";
		}
		return "Other";
	}

	public static String getEmpNameById(String id) {
		OnlineUser user = OnlineUserInfo.getUser(id);
		if (user != null) {
			return user.getName();
		}
		return "Other";
	}

	/**
	 * 把转义字符转\换成/
	 */
	public String addStringAfter(String str) {
		if (str == null || str.length() == 0)
			return "";
		if (str.contains("\\")) {
			str = str.replace("\\", "/");
		}
		return str;
	}

	public static String[] justStr(String[] str) {
		int len = str.length;
		for (int i = 0; i < len; i++) {
			if (str[i].equals("undefined")) {
				str[i] = "";
			}
		}
		return str;
	}

	/**
	 * 判断是包含特殊字符
	 * 
	 * @param strValue
	 * @return
	 */
	public static boolean existSpecialChar(String strValue) {
		if (strValue.contains("'"))
			return true;
		else if (strValue.contains("\""))
			return true;
		else if (strValue.contains("|"))
			return true;
		else if (strValue.contains(";"))
			return true;
		else if (strValue.contains("\\"))
			return true;
		else
			return false;
	}

	/**
	 * 对兄弟表根据表名按字符串排序
	 * 
	 * @param tableList
	 * @return
	 */
	public List<DBTableInfoBean> sorChildTableList(List<DBTableInfoBean> tableList) {
		if (tableList == null || tableList.size() == 0)
			return null;
		TreeMap<String, DBTableInfoBean> treeTable = new TreeMap<String, DBTableInfoBean>();
		for (DBTableInfoBean tableInfo : tableList) {
			treeTable.put(tableInfo.getTableName(), tableInfo);
		}
		List<DBTableInfoBean> listTable = new ArrayList<DBTableInfoBean>();
		for (String str : treeTable.keySet()) {
			listTable.add(treeTable.get(str));
		}
		return listTable;
	}

	/*
	 * 根据email地址查找通讯录中的联系人
	 */
	public String sel_UserByEmail(String email) {
		Result d = pubMgt.selUserNameByEmail(email);
		if (d.getRetVal() != null && !"".equals(d.getRetVal())) {
			return d.getRetVal().toString();
		}
		return "Other";
	}

	/**
	 * 截取小数点后几位前
	 * 
	 * @param str
	 * @param index
	 * @return
	 */
	public static Double subStrNum(String str, String index) {
		if (str != null && !"".equals(str)) {
			str = str.substring(0, str.indexOf(".") + 1 + Integer.parseInt(index));
		}
		return Double.valueOf(str);
	}

	/**
	 * 根据访问地址查看是否有权限
	 * 
	 * @param linkAddr
	 * @return
	 */
	public boolean hasQuery(String linkAddr) {

		if (linkAddr == null || linkAddr.trim().length() == 0)
			return false;
		if (linkAddr.indexOf("/") == -1 && linkAddr.indexOf("/UserFunctionQueryAction.do?tableName=") == -1) {
			linkAddr = "/UserFunctionQueryAction.do?tableName=" + linkAddr;
		}
		LoginBean lg = getLoginBean(request);
		if (lg == null)
			return false;
		MOperation mo = (MOperation) lg.getOperationMap().get(linkAddr);
		if (mo == null)
			return false;
		if (mo.add) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据访问地址查看是否有权限
	 * 
	 * @param linkAddr
	 * @return
	 */
	public boolean hasQuery(String linkAddr, String op) {

		if (linkAddr == null || linkAddr.trim().length() == 0)
			return false;
		if (linkAddr.indexOf("/") == -1 && linkAddr.indexOf("/UserFunctionQueryAction.do?tableName=") == -1) {
			linkAddr = "/UserFunctionQueryAction.do?tableName=" + linkAddr;
		}
		LoginBean lg = getLoginBean(request);
		if (lg == null)
			return false;
		MOperation mo = (MOperation) lg.getOperationMap().get(linkAddr);
		if (mo == null)
			return false;

		if ("add".equals(op) && mo.add) {
			return true;
		} else if ("update".equals(op) && mo.update) {
			return true;
		} else if ("query".equals(op) && mo.query) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 特殊字符的转换
	 * 
	 * @param fieldList
	 * @param values
	 * @return
	 */
	public static Result conversionSpecialCharacter(List fieldList, HashMap values) {
		Result rs = new Result();
		for (int i = 0; i < fieldList.size(); i++) {
			DBFieldInfoBean field = (DBFieldInfoBean) fieldList.get(i);
			Object o = values.get(field.getFieldName());
			if (field.getFieldName().equals("id") || field.getFieldName().equals("f_ref") || o == null || o.toString().trim().length() == 0) {
				continue;
			}
			if (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
				KRLanguage language = null;
				if (values.get(field.getFieldName()) instanceof KRLanguage) {
					language = (KRLanguage) values.get(field.getFieldName());
				} else {
					language = (KRLanguage) ((HashMap) values.get("LANGUAGEQUERY")).get(values.get(field.getFieldName()));
				}

				if (language != null) {
					language.putLanguage("zh_CN", GlobalsTool.encodeTextCode(language.get("zh_CN")));
					language.putLanguage("en", GlobalsTool.encodeTextCode(language.get("en")));
					language.putLanguage("zh_TW", GlobalsTool.encodeTextCode(language.get("zh_TW")));
				}
			} else if ((field.getFieldType() == DBFieldInfoBean.FIELD_ANY || field.getFieldType() == DBFieldInfoBean.FIELD_TEXT || field.getFieldType() == DBFieldInfoBean.FIELD_ONETEXT || field
					.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE_TEXT) && o != null) {
				String value = GlobalsTool.encodeTextCode(o.toString().trim());
				values.remove(field.getFieldName());
				values.put(field.getFieldName(), value);
			}
		}
		rs.setRetVal(values);
		return rs;
	}

	/**
	 * 判断当前登录者是否有权该问该文件工作流
	 * 
	 * @param visitor
	 * @return
	 */
	public boolean canVisit(String visitor) {
		if (visitor == null)
			return false;
		String[] visits = visitor.split("\\|");
		LoginBean lg = getLoginBean(request);

		if (visits[0] != null) {
			for (String str : visits[0].split(",")) {
				if (str.length() > 0 && lg.getDepartCode().contains(str)) {
					return true;
				}
			}
		}
		if (visits[1].contains(lg.getId() + ",")) {
			return true;
		}
		String groupId = lg.getGroupId();
		if (groupId != null) {
			String[] groupIds = groupId.split(",");
			for (String strId : groupIds) {
				if (strId.length() > 0 && visits[2].contains(strId + ",")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 根据某标签之间的值 <test1>testValue</test2> 返回testValue
	 * 
	 * @param varilable
	 * @return
	 */
	public static String getXMLValue(String varilable, String name) {

		Pattern pattern = Pattern.compile("<" + name + ">" + "([/\\\\?\\=\\.\\w\\:\\-\\u4e00-\\u9fa5]+)" + "</" + name + ">");
		Matcher matcher = pattern.matcher(varilable);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

	/**
	 * 查询导入模板是否有样板数据
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean existImportExample(String tableName) {
		String examplePath = BaseEnv.FILESERVERPATH + "/example/" + tableName + ".xls";
		File file = new File(examplePath);
		return file.exists();
	}

	/**
	 * 返回某个部门的人员
	 * 
	 * @param deptName
	 * @return
	 */
	public List getEmployeeByDeptId(String deptName) {
		ArrayList deptList = new ArrayList();
		OnlineUser[] userList = (OnlineUser[]) OnlineUserInfo.cloneMap().values().toArray(new OnlineUser[0]);
		for (OnlineUser user : userList) {
			if (deptName != null && deptName.equals(user.getDepartmentName())) {
				deptList.add(user);
			}
		}
		return deptList;
	}

	/**
	 * 跟据用户Id查询职员全部信息
	 * 
	 * @param deptName
	 * @return
	 */
	public OnlineUser getEmployeeById(String userId) {
		return OnlineUserInfo.getUser(userId);
	}

	/**
	 * 跟据用户Id得到用户名
	 * 
	 * @param deptName
	 * @return
	 */
	public static String getEmpFullNameByUserId(String userId) {
		String empFullName = "";
		OnlineUser onlineUser = (OnlineUser) OnlineUserInfo.cloneMap().get(userId);
		if (onlineUser != null && onlineUser.name != null) {
			empFullName = onlineUser.name;
		}
		return empFullName;
	}

	/**
	 * 跟据用户Id得到部门名
	 * 
	 * @param deptName
	 * @return
	 */
	public static String getDeptByUserId(String userId) {
		String deptName = "";
		OnlineUser onlineUser = (OnlineUser) OnlineUserInfo.cloneMap().get(userId);
		if (onlineUser != null && onlineUser.name != null) {
			deptName = onlineUser.departmentName;
		}
		return deptName;
	}

	/**
	 * 跟据用户Id得到部门code
	 * 
	 * @param userId
	 * @return
	 */
	public static String getDeptCodeByUserId(String userId) {
		String deptCode = "";
		OnlineUser onlineUser = (OnlineUser) OnlineUserInfo.cloneMap().get(userId);
		if (onlineUser != null && onlineUser.name != null) {
			deptCode = onlineUser.getDeptId();
		}
		return deptCode;
	}

	/**
	 * 跟据用户Id得到用户名
	 * 
	 * @param deptName
	 * @return
	 */
	public List<OnlineUser> listEmpNameByUserId(String userIds) {
		List<OnlineUser> listEmp = new ArrayList<OnlineUser>();
		String[] arrayUser = userIds.split(",");
		for (String userId : arrayUser) {
			OnlineUser onlineUser = (OnlineUser) OnlineUserInfo.cloneMap().get(userId);
			if (onlineUser != null && onlineUser.name != null) {
				listEmp.add(onlineUser);
			}
		}
		return listEmp;
	}

	/**
	 * 跟据用户Id得到用户名
	 * 
	 * @param deptName
	 * @return
	 */
	public List<OnlineUser> listDeptNameByDeptId(String deptIds) {
		List<OnlineUser> listDept = new ArrayList<OnlineUser>();
		String[] arrayDept = deptIds.split(",");
		for (String deptId : arrayDept) {
			OnlineUser onlineUser = (OnlineUser) OnlineUserInfo.getDept(deptId);
			if (onlineUser != null && onlineUser.departmentName != null) {
				listDept.add(onlineUser);
			}
		}
		return listDept;
	}

	/**
	 * 判断隐藏可显示字段是否在列配置中
	 * 
	 * @param fieldNames
	 * @return
	 */
	public boolean existColConfig(String tableName, String fieldName) {
		boolean exist = false;
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
		if (userColConfig != null) {
			ArrayList<ColConfigBean> configList = userColConfig.get(tableName + "bill");
			if (configList != null) {
				for (ColConfigBean bean : configList) {
					if (bean.getColName().equals(fieldName) || bean.getColName().equals("@TABLENAME." + fieldName)) {
						exist = true;
						break;
					}
				}
			}
		}
		return exist;
	}

	/**
	 * 判断隐藏单据 是否 做过列配置
	 * 
	 * @param fieldNames
	 * @return
	 */
	public boolean existConfig(String tableName) {
		boolean exist = false;
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
		if (userColConfig != null) {
			ArrayList<ColConfigBean> configList = userColConfig.get(tableName + "bill");
			if (configList != null) {
				exist = true;
			}
		}
		return exist;
	}

	/**
	 * 判断隐藏可显示字段是否在报表列配置中
	 * 
	 * @param fieldNames
	 * @return
	 */
	public boolean existListColConfig(String tableName, String fieldName) {
		boolean exist = false;
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
		if (userColConfig != null) {
			ArrayList<ColConfigBean> configList = userColConfig.get(tableName + "list");
			if (configList != null) {
				for (ColConfigBean bean : configList) {
					if (bean.getColName().equals(fieldName)) {
						exist = true;
						break;
					}
				}
			}
		}
		return exist;
	}

	/**
	 * 判断隐藏可显示字段是否在报表列配置中
	 * 
	 * @param fieldNames
	 * @return
	 */
	public static boolean existListColConfig(String tableName, String fieldName, HttpServletRequest request2) {
		boolean exist = false;
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request2.getSession().getServletContext().getAttribute("userSettingColConfig");
		if (userColConfig != null) {
			ArrayList<ColConfigBean> configList = userColConfig.get(tableName + "list");
			if (configList != null) {
				for (ColConfigBean bean : configList) {
					if (bean.getColName().equals(fieldName)) {
						exist = true;
						break;
					}
				}
			}
		}
		return exist;
	}

	/**
	 * 查询某表的列配置
	 * 
	 * @param fieldNames
	 * @return
	 */
	public ArrayList queryConfig(String tableName) {
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
		ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();
		if (userColConfig != null) {
			ArrayList<ColConfigBean> userConfigList = userColConfig.get(tableName + "bill");
			if (userConfigList != null) {
				configList = userConfigList;
			}
		}
		return configList;
	}

	/**
	 * 查询客户列表字符
	 * 
	 * @param reportNumber
	 * @return
	 */
	public static ArrayList queryReprotField(DefineReportBean defBean, String type, ArrayList<ColConfigBean> colList) {
		ArrayList<ReportField> fieldList = new ArrayList<ReportField>();
		try {
			if ("config".equals(type)) {
				ArrayList reportList = defBean.getDisFields();
				if (colList != null && colList.size() > 0) {
					/* 按照列配置的顺序 加进去 */
					for (ColConfigBean configBean : colList) {
						for (int i = 0; i < reportList.size(); i++) {
							ReportField reportField = (ReportField) reportList.get(i);
							if (configBean.getColName().equals(reportField.getAsFieldName())) {
								fieldList.add(reportField);
								break;
							}
						}
					}
				}
			} else {
				fieldList = defBean.getDisFields2();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldList;
	}

	/**
	 * 查询客户列表字符
	 * 
	 * @param reportNumber
	 * @return
	 */
	public ArrayList queryReprotField(String reportNumber, String type) {
		DefineReportBean defBean = null;
		ArrayList<ReportField> fieldList = new ArrayList<ReportField>();
		try {
			defBean = DefineReportBean.parse(reportNumber + "SQL.xml", getLocale(), "1");
			Hashtable<String, ArrayList<ColConfigBean>> userConfigBean = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
			ArrayList<ColConfigBean> colList = userConfigBean.get("CRMClientInfoListlist");
			fieldList = queryReprotField(defBean, type, colList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldList;
	}

	/**
	 * 查询客户列表字符
	 * 
	 * @param reportNumber
	 * @return
	 */
	public ArrayList queryReprotField(String reportNumber) {
		DefineReportBean defBean = null;
		ArrayList<ReportField> fieldList = new ArrayList<ReportField>();
		try {
			defBean = DefineReportBean.parse(reportNumber + "SQL.xml", getLocale(), "1");
			fieldList = defBean.getDisFields2();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldList;
	}

	/**
	 * 判断key是否在数组str中存在
	 * 
	 * @param fieldNames
	 * @return
	 */
	public static boolean isExistInArray(String[] str, String key) {
		boolean flag = false;
		for (String s : str) {
			if (key.equals(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 取两个时间的差天数， 如果lastDate为空，或格式不正确，取当前时间
	 * 
	 * @param lastDate
	 * @param preDate
	 * @return
	 */
	public static int getTimeDiff(String lastDate, String preDate) {
		try {
			long pre = System.currentTimeMillis();
			// 如果lastDate为空，或格式不正确，取当前时间
			try {
				pre = BaseDateFormat.parse(lastDate, BaseDateFormat.yyyyMMdd).getTime();
			} catch (Exception e) {
			}

			long space = pre - BaseDateFormat.parse(preDate, BaseDateFormat.yyyyMMdd).getTime();
			return (int) (space / (24 * 60 * 60000));
		} catch (Exception e) {
			return 0;
		}
	}

	// 取资源文件,在非action类中，给出request和资源文件key,参数
	public static String getLanguage(String locale) {
		Object o = application.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		String msg = "";
		if (o instanceof MessageResources) {
			MessageResources resources = (MessageResources) o;
			Locale loc;
			if (locale.indexOf("_") == -1) {
				loc = new Locale(locale);
			} else {
				String ls[] = locale.split("_");
				loc = new Locale(ls[0], ls[1]);
			}
			msg = resources.getMessage(loc, "common.lb.language");
		}
		return msg;
	}

	/**
	 * 查询论坛积分等级
	 * 
	 * @return
	 */
	public String getGrade(int scope, ArrayList<String[]> gradeList) {
		for (String[] arrayGrade : gradeList) {
			if (scope >= Integer.parseInt(arrayGrade[1]) && scope < Integer.parseInt(arrayGrade[2])) {
				return arrayGrade[0];
			}
		}
		return "";
	}

	/**
	 * 得到所有字母
	 * 
	 * @return
	 */
	public static String[] getAllLetter() {
		return "A;B;C;D;E;F;G;H;I;J;K;L;M;N;O;P;Q;R;S;T;U;V;W;X;Y;Z;".split(";");
	}

	public static String urlEncode(String str) {
		try {
			return java.net.URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到一种背景颜色
	 * 
	 * @param num
	 * @return
	 */
	public String getBackGround(int num) {
		String[] backGrounds = { "#6666FF", "#66CC33", "#FF9900", "#FF99CC", "#66FF99", "#CC6633", "#FF66FF", "#66FFFF", "#9966CC", "#CCFF99" };
		if (num >= backGrounds.length) {
			num = RandomUtils.nextInt(10);
		}
		return backGrounds[num];
	}

	/**
	 * 得到邮件发送人名
	 * 
	 * @param num
	 * @return
	 */
	public static String getMailName(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		int pos = str.indexOf("<");
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 得到邮件发送人名
	 * 
	 * @param num
	 * @return
	 */
	public static String getMailAddress(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		int pos = str.indexOf("<");
		if (pos == -1) {
			return str;
		}
		if (str.indexOf(">", pos) > 0) {
			return str.substring(pos + 1, str.indexOf(">", pos));
		} else {
			return str.substring(pos + 1);
		}
	}

	/**
	 * 跟据主表生成每个自定义文件的JS
	 * 
	 * @param fileName
	 * @param tableName
	 * @return
	 */
	public String js(String fileName, String tableName, MessageTool mt) {
		if (fileName.endsWith(".js")) {
			// 如果是js文件，直接返回，vjs才按下面的生成
			return GenJS.djs(fileName);// 免费每次要刷新缓存问题
		}
		// 取得文件路径,文件路径为website/js/gen/+fileName+ _zh.js
		String fn = fileName;
		fn = fn.startsWith("/") ? fn.substring(1) : fn;
		fn = fn.startsWith("js") ? fn.substring(3) : fn;
		String ret = "/js/gen/" + (fn) + tableName + "_" + this.getLocale() + ".js";

		// 开发版环境，不加密，不记录状态，每次刷新都重新生成js ,满足开发时改变js的要求
		if (!SystemState.instance.develope) {
			if (GenJS.getJS(ret) != null) {
				// 此文件已生成过，在下次重启前，不再生成
				return GenJS.djs(ret);
			}
		} else {
			// 开发环境，比较文件的时间，如果时间未变动则不做修改
			if (GenJS.getJS(ret) != null) {
				// 此文件已生成过，在下次重启前，不再生成
				long lm = (Long) GenJS.getJS(ret);
				if (lm == new File(request.getSession().getServletContext().getRealPath(fileName.startsWith("/") ? fileName : fileName.substring(1))).lastModified()) {
					return GenJS.djs(ret);
				}
			}
		}
		long curTime = System.currentTimeMillis();
		String savefileName = request.getSession().getServletContext().getRealPath(ret.startsWith("/") ? ret : ret.substring(1));
		try {
			String jsStr = "";
			HashMap map = new HashMap();
			map.put("globals", this);
			map.put("text", mt);
			Enumeration eum = request.getAttributeNames();
			while (eum.hasMoreElements()) {
				String str = eum.nextElement().toString();
				map.put(str, request.getAttribute(str));
			}
			map.put("request", request);
			jsStr = GenJS.js(request.getSession().getServletContext().getRealPath(""), fileName, map);
			// System.out.println("花时间1 "+ret +" :"+(System.currentTimeMillis()
			// - curTime));

			File file = new File(savefileName);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}

			// 非开发环境，需要对文件进行加密处理
			String regex = "[\\t]+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(jsStr);
			jsStr = m.replaceAll(" ");
			// 两个空格换一个
			regex = "[ ]{2,100}";
			p = Pattern.compile(regex);
			m = p.matcher(jsStr);
			jsStr = m.replaceAll(" ");

			while (jsStr.indexOf("/*") > -1) {
				int pos = jsStr.indexOf("/*");
				jsStr = jsStr.substring(0, pos) + jsStr.substring(jsStr.indexOf("*/", pos) + 2);
			}

			// 变http://
			regex = "://";
			p = Pattern.compile(regex);
			m = p.matcher(jsStr);
			jsStr = m.replaceAll(":/::/");
			// 去注释
			regex = "//[^\n]*";
			p = Pattern.compile(regex);
			m = p.matcher(jsStr);
			jsStr = m.replaceAll("");

			// 变http://
			regex = ":/::/";
			p = Pattern.compile(regex);
			m = p.matcher(jsStr);
			jsStr = m.replaceAll("://");

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(jsStr.getBytes("UTF-8"));
			fos.close();
			// 保存止文件已更新
			GenJS.putJS(ret, new File(request.getSession().getServletContext().getRealPath(fileName.startsWith("/") ? fileName : fileName.substring(1))).lastModified());
			BaseEnv.log.debug("GlobalsTool.js 生成js 时间" + (System.currentTimeMillis() - curTime) + ":" + ret);
		} catch (Exception e) {
			BaseEnv.log.error("GlobalsTool.js Error :", e);
		}
		return GenJS.djs(ret);
	}

	/**
	 * 查询某个阶段的第一天 如某周的第一天，月的第一天
	 * 
	 * @return
	 */
	public String getFirstDay(String date, String planType) {
		Calendar calendar = new GregorianCalendar();
		BaseDateFormat dateFormat = new BaseDateFormat();
		try {
			calendar.setTime(dateFormat.parse(date, BaseDateFormat.yyyyMMdd));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("week".equals(planType)) {
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		} else if ("month".equals(planType)) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		} else if ("year".equals(planType)) {
			calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(calendar.getTime());
	}

	/**
	 * 查询某个阶段的最后一天 如某周的最后一天，月最后一天
	 * 
	 * @return
	 */
	public String getLastDay(String date, String planType) {
		// 获取某个星期的最后一天.
		Calendar calendar = new GregorianCalendar();
		BaseDateFormat dateFormat = new BaseDateFormat();
		try {
			calendar.setTime(dateFormat.parse(date, BaseDateFormat.yyyyMMdd));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("week".equals(planType)) {
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
		} else if ("month".equals(planType)) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else if ("year".equals(planType)) {
			calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(calendar.getTime());
	}

	/**
	 * 3D 多列柱形图
	 * 
	 * @return
	 */
	public static String getMultiBarChart(ArrayList<String> categories, TreeMap<String, ArrayList<Double>> values, String locale) {
		StringBuilder strChart = new StringBuilder();
		strChart.append("<graph showValues='0' formatNumberScale='0' divLineDecimalPrecision='0'  hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0' baseFontSize='12' "
				+ "yAxisMaxValue='' numdivlines='4' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' "
				+ "AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' subcaption='' chartBottomMargin='0' chartLeftMargin='10' chartRightMargin='0' chartTopMargin='10'  >");

		strChart.append("<categories font='Arial' fontSize='13' fontColor='000000'>");
		for (String strName : categories) {
			strChart.append("<category name='" + strName + "' />");
		}
		strChart.append("</categories>");
		Iterator iterator = values.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next();
			String strName = getMessage(locale, strKey);
			if (strName == null || strName.length() == 0) {
				strName = strKey;
			}
			strChart.append("<dataset seriesname='" + strName + "' color='" + getColor(i++) + "'>");
			ArrayList<Double> arrStr = (ArrayList<Double>) values.get(strKey);
			for (double strValue : arrStr) {
				strChart.append("<set value='" + strValue + "'/>");
			}
			strChart.append("</dataset>");
		}
		strChart.append("</graph>");
		return strChart.toString();
	}

	/**
	 * 3D 多列柱形图
	 * 
	 * @return
	 */
	public static String getPie3DChart(TreeMap<String, String> values) {
		StringBuilder strChart = new StringBuilder();
		strChart.append("<graph baseFontsize='13' showNames='1'  decimalPrecision='0'  >");
		Iterator iterator = values.keySet().iterator();
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next();
			strChart.append("<set name='" + strKey + "' value='" + values.get(strKey) + "'/>");
		}
		strChart.append("</graph>");
		return strChart.toString();
	}

	/**
	 * 目标走势图
	 * 
	 * @return
	 */
	public static String getMultiMS2Line(ArrayList<String> categories, TreeMap<String, ArrayList<Double>> values, String locale) {
		StringBuilder strChart = new StringBuilder();
		String maxValue = "";
		String minValue = "";
		strChart.append("<graph  baseFontsize='13' formatNumberScale='0' subcaption='' " + "divlinecolor='F47E00' numdivlines='4' showAreaBorder='1' "
				+ "areaBorderColor='000000' numberPrefix='' showNames='1' numVDivLines='29' vDivLineAlpha='30' "
				+ "formatNumberScale='1' decimalPrecision='0' chartBottomMargin='0'  chartLeftMargin='10' chartTopMargin='10' chartRightMargin='0'>");
		strChart.append("<categories>");
		for (String strName : categories) {
			strChart.append("<category name='" + strName + "' />");
		}
		strChart.append("</categories>");
		Iterator iterator = values.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next();
			String strName = getMessage(locale, strKey);
			if (strName == null || strName.length() == 0) {
				strName = strKey;
			}
			String color = getColor(i++);
			strChart.append("<dataset seriesname='" + strName + "' color='" + color + "' showValues='0' areaAlpha='50' showAreaBorder='1'" + " areaBorderThickness='2' areaBorderColor='" + color
					+ "'>");
			ArrayList<Double> arrStr = (ArrayList<Double>) values.get(strKey);
			for (double strValue : arrStr) {
				strChart.append("<set value='" + strValue + "'/>");
			}
			strChart.append("</dataset>");
		}
		strChart.append("</graph>");
		return strChart.toString();
	}

	/**
	 * 颜色
	 * 
	 * @param number
	 * @return
	 */
	public static String getColor(int number) {
		String[] arrColor = { "B9DB7D", "3BB3C3", "9D080D", "A186BE", "008ED6", "B3AA00", "8BBA00", "588526", "8E468E", "D64646", "008E8E", "FF8E46" };
		if (number >= arrColor.length) {
			number = RandomUtils.nextInt(12);
		}
		return arrColor[number];
	}

	/**
	 * 获取用户对某一模块的操作权限
	 * 
	 * @return
	 */
	public MOperation getMOperation(String strUrl) {
		try {
			if (strUrl.contains("&moduleType=")) {
				int index = strUrl.indexOf("&", strUrl.indexOf("&moduleType=") + 12);
				if (index != -1) {
					strUrl = strUrl.substring(0, index);
				}
			}
			return (MOperation) getLoginBean().getOperationMap().get(strUrl);
		} catch (Exception e) {
			return (MOperation) getLoginBean().getOperationMap().get(strUrl);
		}
	}

	/**
	 * 获取用户对某一模块的操作权限
	 * 
	 * @return
	 */
	public static MOperation getMOperationMap(HttpServletRequest request) {

		String parentTableName = request.getParameter("parentTableName");
		String tableName = request.getParameter("tableName");
		String moduleType = request.getParameter("moduleType");
		return getMOperationMap(parentTableName, tableName, moduleType, getLoginBean(request));
	}

	public static MOperation getMOperationMap(String parentTableName, String tableName, String moduleType, LoginBean loginBean) {

		MOperation mop = new MOperation();
		if (tableName == null || tableName.length() == 0) {
			return mop;
		}
		if (tableName.startsWith("Flow")) {
			mop.add = true;
			mop.update = true;
			mop.query = true;
			mop.print = true;
			mop.oaWorkFlow = true;
			return mop;
		}
		String moduleURL = "";
		if (parentTableName != null && parentTableName.length() > 0 && !parentTableName.equalsIgnoreCase("NULL")) {
			moduleURL = "/UserFunctionQueryAction.do?tableName=" + parentTableName;
		} else {
			moduleURL = "/UserFunctionQueryAction.do?tableName=" + tableName;
		}
		if (moduleType != null && moduleType.length() > 0) {
			moduleURL += "&moduleType=" + moduleType;
			;
		}
		if (moduleURL != null && moduleURL.contains("/UserFunctionAction.do")) {
			moduleURL = moduleURL.replace("/UserFunctionAction.do", "/UserFunctionQueryAction.do");
		}
		mop = (MOperation) loginBean.getOperationMap().get(moduleURL);
		return mop;
	}

	/**
	 * 查询指定ip和访问终端是否是同一网段
	 * 
	 * @param ip
	 * @return
	 */
	public boolean isSameNet(String ip) {
		String rip = request.getRemoteAddr();
		if (request.getHeader("x-forwarded-for") != null) {
			rip = request.getHeader("x-forwarded-for");
		}
		// localhost
		if (rip.equals("0:0:0:0:0:0:0:1")) {
			return true;
		}
		if (rip.lastIndexOf(".") > 0 && ip.substring(0, ip.lastIndexOf(".")).equals(rip.substring(0, rip.lastIndexOf(".")))) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符是否是个数字
	 * 
	 * @param doubleValue
	 * @return
	 */
	public static boolean isDouble(String doubleValue) {
		if (doubleValue == null || doubleValue.length() == 0) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-]{0,1}[0-9,\\.]*$");
		Matcher matcher = pattern.matcher(doubleValue);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static boolean isShortDate(String date) {
		if (date == null || date.length() == 0) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\w]{4}-[\\w]{2}-[\\w]{2}$");
		Matcher matcher = pattern.matcher(date);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static boolean isLongDate(String date) {
		if (date == null || date.length() == 0) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\w]{4}-[\\w]{2}-[\\w]{2} [\\w]{2}:[\\w]{2}:[\\w]{2}$");
		Matcher matcher = pattern.matcher(date);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 把文件转化成Base64格式编码
	 * 
	 * @param imgFile
	 * @return
	 */
	public static String imageBase64(String imgFile) {
		File file = new File(imgFile);
		if (!file.exists()) {
			return "";
		}
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (data == null) {
			return "";
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

	/**
	 * 过滤内容中<a></a>
	 * 
	 * @param content
	 * @return
	 */
	public static String replaceA(String content) {
		if (content == null || content.trim().length() == 0) {
			return "";
		}
		Pattern pattern = Pattern.compile("<a[/\":()\\?@&,'\\u4e00-\\u9fa5.=\\s\\w]+>");
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			content = content.replace(matcher.group(), "");
		}
		content = content.replace("</a>", "");
		return content;
	}

	/**
	 * 替换特殊字符
	 * 
	 * @return
	 */
	public static String replaceSpecLitter(String str) {
		return str == null ? null : str.replaceAll("\"", "&quot;").replaceAll("'", "&apos;").replaceAll("\\\\", "&#92;");
	}

	/**
	 * 反替换特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String rereplaceSpecLitter(String str) {
		if (str == null)
			return str;
		str = str == null ? null : str.replaceAll("&quot;", "\"").replaceAll("&apos;", "'");
		while (str.indexOf("&#92;") > -1) {
			str = str.substring(0, str.indexOf("&#92;")) + "\\" + str.substring(str.indexOf("&#92;") + 5);
		}
		return str;
	}

	/**
	 * 反替换特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String rereplaceSpecLitter2(String str) {
		if (str == null)
			return str;
		str = str == null ? null : str.replaceAll("&quot;", "\\\\\"").replaceAll("&apos;", "\\\\'").replaceAll("\r?\n", "\\\\n");
		while (str.indexOf("&#92;") > -1) {
			str = str.substring(0, str.indexOf("&#92;")) + "\\\\" + str.substring(str.indexOf("&#92;") + 5);
		}
		return str;
	}

	/**
	 * 过滤html标记
	 * 
	 * @param content
	 * @return
	 */
	public static String replaceTextArea(String content) {
		if (content == null || content.trim().length() == 0) {
			return "";
		}
		content = content.replaceAll("<textarea.*?>", "").replaceAll("</textarea>", "");
		return content;
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
		content = content.replaceAll("\\\\r\\\\n", "").replaceAll("\\\\n", "").replaceAll("\\\\t", "").replaceAll("\\\\r", "");
		content = content.replaceAll("<.*?>", "").replaceAll("&nbsp;", " ");
		return content;
	}

	/**
	 * 过滤html标记(不过滤图片标签)
	 * 
	 * @param content
	 * @return
	 */
	public static String replaceHTML2(String content) {
		if (content == null || content.trim().length() == 0) {
			return "";
		}
		content = content.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "");
		content = content.replaceAll("<(?!img).*?>", "").replaceAll("&nbsp;", " ");
		return content;
	}

	/**
	 * 过滤html标记(图片转【图片】)
	 * 
	 * @param content
	 * @return
	 */
	public static String replaceHTML3(String content) {
		if (content == null || content.trim().length() == 0) {
			return "";
		}
		content = content.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "");
		content = content.replaceAll("<(?!img).*?>", "").replaceAll("&nbsp;", " ");
		content = content.replaceAll("<.*?>", "[图片]").replaceAll("&nbsp;", " ");
		return content;
	}

	public static String replaceHTMLMail(String content) {
		if (content == null || content.trim().length() == 0) {
			return "";
		}
		content = content.replaceAll("\\\\r\\\\n", "").replaceAll("\\r\\n", "").replaceAll("'|\\r|\\n", "").replaceAll("\\\\n", "").replaceAll("\\\\t", "").replaceAll("\\\\r", "");
		content = content.replaceAll("<(head|HEAD|style|STYLE|script|SCRIPT)[^>]*?>.*?</(head|HEAD|style|STYLE|script|SCRIPT)>", "");
		content = content.replaceAll("<.*?>", "").replaceAll("&nbsp;", " ");
		return content;
	}

	/**
	 * 少于多少天为新贴
	 * 
	 * @param beginTime
	 * @param day
	 * @return
	 */
	public boolean isNews(String beginTime, Integer day) {
		if (beginTime == null || beginTime.trim().length() == 0)
			return false;
		if (day == null || day == 0)
			return false;
		boolean isNew = false;
		try {
			Date time = BaseDateFormat.parse(beginTime, BaseDateFormat.yyyyMMddHHmmss);
			if (((new Date()).getTime() - time.getTime()) < (day * 24 * 60 * 60 * 1000)) {
				isNew = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNew;
	}

	/**
	 * 通过字段名查找客户相应的字段名
	 * 
	 * @param colName
	 * @return
	 */
	public String getColName(String colName) {
		DBFieldInfoBean fieldInfo = getFieldBean("CRMClientInfo", colName);
		if (fieldInfo == null) {
			fieldInfo = getFieldBean("CRMClientInfoDet", colName);
		}
		return fieldInfo.getDisplay().get(getLocale().toString());
	}

	/**
	 * 通过字段名查找客户相应的结构
	 * 
	 * @param colName
	 * @return
	 */
	public static DBFieldInfoBean getField(String colName) {
		if (colName.startsWith("contact")) {
			colName = colName = colName.substring(7, colName.length());
		}
		DBFieldInfoBean fieldInfo = getFieldBean("CRMClientInfo", colName);
		if (fieldInfo == null) {
			fieldInfo = getFieldBean("CRMClientInfoDet", colName);
		}
		return fieldInfo;
	}

	/**
	 * 通过字段名查找客户相应的结构
	 * 
	 * @param colName
	 * @return
	 */
	public static DBFieldInfoBean getDetField(String colName) {
		if (!colName.contains("contact"))
			return null;
		colName = colName.substring(7, colName.length());
		return getFieldBean("CRMClientInfoDet", colName);
	}

	/**
	 * 从请求的HEAD头获取浏览器是否IE8（FF CHROME）以上
	 * 
	 * @param header
	 * @return
	 */
	public boolean isLowerIE8() {
		String header = request.getHeader("User-Agent");
		Pattern p = Pattern.compile("MSIE ([0-9]+[\\.]*[0-9]*)");
		Matcher m = p.matcher(header);
		if (m.find()) {
			return Double.parseDouble(m.group(1)) < 8;
		}
		return false;
	}

	/**
	 * 通过字段名与表结构查找客户相应的字段名
	 * 
	 * @param colName
	 * @return
	 */
	public String getColName(String colName, String clientTableName, String contactTableName) {
		if (colName != null && !"".equals(colName)) {
			DBFieldInfoBean fieldInfo = getFieldBean(clientTableName, colName);
			if (fieldInfo == null) {
				fieldInfo = getFieldBean(contactTableName, colName);
			}
			return fieldInfo.getDisplay().get(getLocale().toString());
		} else {
			return "";
		}
	}

	/**
	 * 通过字段名与表明查找客户相应的结构
	 * 
	 * @param colName
	 * @return
	 */
	public static DBFieldInfoBean getField(String colName, String clientTableName, String contactTableName) {
		if (colName.startsWith("contact")) {
			colName = colName = colName.substring(7, colName.length());
		}
		DBFieldInfoBean fieldInfo = getFieldBean(clientTableName, colName);
		if (fieldInfo == null) {
			fieldInfo = getFieldBean(contactTableName, colName);
		}
		return fieldInfo;
	}

	/**
	 * 跟据部门ID查找部门名称
	 * 
	 * @param deptId
	 * @return
	 */
	public String getDepartMentById(String deptId) {
		String name = new PublicMgt().getDepartNameByCode(deptId);
		return name;
		/*
		 * String str =""; if(deptId != null && !"".equals(deptId)){ str =
		 * OnlineUserInfo.getDept(deptId).getDepartmentName(); } return str;
		 */
	}

	/**
	 * CRM模板INPUTTYPE显示
	 * 
	 * @param value
	 * @return
	 */

	public String getInputTypeForModule(String inputType, String fieldType) {
		String str = "";
		if ("5".equals(fieldType)) {
			str = "短日期";
		} else if ("6".equals(fieldType)) {
			str = "长日期";
		} else if ("18".equals(fieldType)) {
			str = "长字符";
		} else if ("3".equals(fieldType)) {
			str = "备注";
		} else if ("1".equals(inputType)) {
			str = "下拉列表";
		} else if ("5".equals(inputType)) {
			str = "复选框";
		} else if ("10".equals(inputType)) {
			str = "单选框";
		} else if ("14".equals(inputType)) {
			str = "部门弹出框";
		} else if ("15".equals(inputType)) {
			str = "职员弹出框";
		} else if ("2".equals(inputType)) {
			str = "自定义弹出框";
		} else if ("20".equals(inputType)) {
			str = "关联客户下拉框";
		} else {
			str = "输入";
		}
		return str;
	}

	/**
	 * 计算出一个字符串split后长度
	 * 
	 * @param str
	 * @param split
	 *            分割标点
	 * @return
	 */
	public int splitLength(String str, String split) {
		int i = 0;
		i = str.split(split).length;
		return i;
	}

	/**
	 * 表结构过滤CRM表不显示 条件： 1.客户资料表与明细表(表名包含:CRMClientInfo的)
	 * 2.CRM邻居表采用新版路径的/CRMBrotherAction.do?tableName=$tableName
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean filterTabel(String tableName) {
		String linkAddr = "/CRMBrotherAction.do?tableName=" + tableName;// 新版CRM邻居表访问地址
		if (tableName != null && (tableName.indexOf("CRMClientInfo") > -1 || this.hasQuery(linkAddr, "query"))) {
			return false;
		} else {
			return true;
		}
	}

	// 截取最后一个字节
	public String subEndwith(String str, int length) {
		str = str.substring(0, length - 1);
		return str;
	}

	// 获取IP地址
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	/**
	 * 把接受的全部文件打成压缩包 mj
	 * 
	 * @param List
	 *            <File>;
	 * @param org
	 *            .apache.tools.zip.ZipOutputStream
	 */
	public static void zipFile(List files, org.apache.tools.zip.ZipOutputStream outputStream) {
		int size = files.size();
		for (int i = 0; i < size; i++) {
			File file = (File) files.get(i);
			zipFile(file, outputStream);
		}
	}

	// mj
	public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
		try {
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				File f = new File(file.getPath());
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * 根据输入的文件与输出流对文件进行打包 mj
	 * 
	 * @param File
	 * @param org
	 *            .apache.tools.zip.ZipOutputStream
	 */
	public static void zipFile(File inputFile, org.apache.tools.zip.ZipOutputStream ouputStream) {
		try {
			if (inputFile.exists()) {
				/**
				 * 如果是目录的话这里是不采取操作的
				 */
				if (inputFile.isFile()) {
					FileInputStream IN = new FileInputStream(inputFile);
					BufferedInputStream bins = new BufferedInputStream(IN, 512);
					org.apache.tools.zip.ZipEntry entry = new org.apache.tools.zip.ZipEntry(inputFile.getName());// 不用自帶的解決中文亂碼

					ouputStream.putNextEntry(entry);
					// 向压缩文件中输出数据
					int nNumber;
					byte[] buffer = new byte[512];
					while ((nNumber = bins.read(buffer)) != -1) {
						ouputStream.write(buffer, 0, nNumber);
					}
					// 关闭创建的流对象
					bins.close();
					IN.close();
				} else {
					try {
						File[] files = inputFile.listFiles();
						for (int i = 0; i < files.length; i++) {
							zipFile(files[i], ouputStream);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * CRM环比同比增长率运算
	 * 
	 * @param prevVal
	 * @param nowVal
	 * @return
	 */
	public String getChangeRatio(String prevVal, String nowVal) {
		java.text.DecimalFormat mf = new java.text.DecimalFormat("0.0");
		prevVal = prevVal == null ? "0" : prevVal;
		nowVal = nowVal == null ? "0" : nowVal;
		double prev = Double.parseDouble(prevVal);
		double now = Double.parseDouble(nowVal);

		double change = 0;
		String str = "";
		if (prev == 0 && now == 0) {
			str = "-";
		} else if (prev == 0) {
			str = "+100.0%";
		} else if (now == 0) {
			str = "-100.0%";
		} else {
			change = ((now - prev) / prev) * 100;
			if (change > 0) {
				str += "+";
			}

			str += mf.format(Math.round(change)) + "%";
		}
		return str;
	}

	/**
	 * 获取数组下标值
	 * 
	 * @param str
	 * @param split
	 * @param index
	 * @return
	 */
	public String getSplitIndex(String str, String split, int index) {
		if (str == null || "".equals(str)) {
			return "";
		} else {
			return str.split(split)[index];
		}

	}

	/**
	 * 截取小数点后几位
	 * 
	 * @param val
	 *            数字
	 * @param len
	 *            小数点后几位
	 * @return
	 */
	public static String getSubStrDigit(String val, String len) {
		if (isDouble(val)) {
			String strvalue = val;
			String value = "000000000";
			if (strvalue.indexOf(".") > 0) {
				if (strvalue.substring(strvalue.indexOf(".") + 1).length() < Integer.valueOf(len)) {
					strvalue = strvalue + value.substring(0, Integer.valueOf(len) - strvalue.substring(strvalue.indexOf(".") + 1).length());
				}
				strvalue = strvalue.substring(0, strvalue.indexOf(".") + Integer.valueOf(len) + 1);
			}
			if (Double.valueOf(strvalue) == 0) {
				strvalue = "";
			}
			return strvalue;
		} else {
			return "";
		}
	}

	/**
	 * 获取百分比
	 * 
	 * @param rowTotal
	 * @param total
	 * @return
	 */
	public String getPercent(String rowTotal, String total) {
		java.text.DecimalFormat mf = new java.text.DecimalFormat("0.0");
		double row = Double.parseDouble(rowTotal);
		double all = Double.parseDouble(total);
		return mf.format((row / all) * 100) + "%";
	}

	/**
	 * 处理双精度小数位,一般截取2位小数,若sumFieldName=avg则截取1位小数
	 * 
	 * @param val
	 * @param sumFieldName
	 * @return
	 */
	public static String dealDoubleDigits(String val, String sumFieldName) {
		String formatStr = "0.00";
		double douVal = val == null || "".equals(val) ? 0 : Double.parseDouble(val);
		if ("0".equals(val) || "0.0".equals(val)) {
			formatStr = "0.00";
		}
		java.text.DecimalFormat mf = new java.text.DecimalFormat(formatStr);
		if (sumFieldName != null && !"".equals(sumFieldName)) {
			return mf.format(douVal);
		} else {
			return Math.round(douVal) + "";
		}

	}

	public static String getHongVal(String hongType) {
		String hongVal = "";
		GlobalsTool globalTool = new GlobalsTool();
		String curTime = BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss);
		if ("sys_date".equals(hongType)) {// 当前日期
			hongVal = curTime.substring(0, 10);
		} else if ("sys_time".equals(hongType)) {// 当前时间
			hongVal = curTime.substring(11, 19);
		} else if ("sys_datetime".equals(hongType)) {// 当前日期+时间，格式
			hongVal = curTime;
		} else if ("sys_empName".equals(hongType)) {// 当前用户名
			hongVal = globalTool.getLoginBean().getEmpFullName();
		} else if ("sys_empTitle".equals(hongType)) {// 当前用户职位
			hongVal = GlobalsTool.getEnumerationItemsDisplay("duty", globalTool.getLoginBean().getTitleId(), "zh_CN");
		} else if ("sys_dept".equals(hongType)) {// 当前部门名称
			hongVal = globalTool.getLoginBean().getDepartmentName();
		} else if ("sys_startdate".equals(hongType)) {// 流程开始日期

		} else if ("sys_starttime".equals(hongType)) {// 流程开始日期+时间

		}
		return hongVal;
	}

	/**
	 * 条形码ean13的校验码生成规则
	 * 
	 * @param code
	 * @return
	 */
	public static String ean13(String code) {

		int sumj = 0, sume = 0;
		int result = 0;
		for (int i = 0; i < code.length() - 1; i = i + 2) {
			sumj += code.charAt(i) - '0';
			sume += code.charAt(i + 1) - '0';
		}

		result = sumj + sume * 3;
		int c = result % 10;
		c = (10 - result % 10) % 10;
		// result = result %10;
		// if(result == 0)
		// result = 0;
		// else
		// result = 10 -result;
		return code + c;
	}

	public static DefineJS getDefineJS() {
		return DefineJS.instance;
	}

	/**
	 * 返回数组长度
	 * 
	 * @param str
	 * @return
	 */
	public int arrayLength(String[] str) {
		return str.length;
	}

	/**
	 * 通过字段名与表明查找客户相应的结构
	 * 
	 * @param colName
	 * @return
	 */
	public static DBFieldInfoBean getCRMBrotherField(String colName, String clientTableName, String contactTableName) {
		if (colName.startsWith("child")) {
			colName = colName = colName.substring(5, colName.length());
		}
		DBFieldInfoBean fieldInfo = getFieldBean(clientTableName, colName);
		if (fieldInfo == null) {
			fieldInfo = getFieldBean(contactTableName, colName);
		}
		return fieldInfo;
	}

	/**
	 * 获取审核流当前节点流程名称
	 * 
	 * @param designId
	 * @param currNodeId
	 * @return
	 */
	public String getCurrNodeName(String designId, String currNodeId) {
		MyWorkFlow workFlow = new MyWorkFlow();
		workFlow.setApplyType(designId);
		workFlow.setCurrentNode(currNodeId);
		if (BaseEnv.workFlowDesignBeans.get(workFlow.getApplyType()) != null) {
			FlowNodeBean flowNode = BaseEnv.workFlowDesignBeans.get(workFlow.getApplyType()).getFlowNodeMap().get(workFlow.getCurrentNode());
			if (flowNode != null && flowNode.getApprovers() != null) {
				return flowNode.getDisplay();
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * 根据职员ID获取头像信息
	 * 
	 * @param id
	 * @return
	 */
	public String getEmployeePhoto(String id) {
		OnlineUser user = OnlineUserInfo.getUser(id);
		if (user != null) {
			return user.getPhoto();
		}
		return "Other";
	}

	/**
	 * 根据index获取对应的周期中文
	 * 
	 * @param index
	 * @return
	 */
	public static String getWeekCN(int index) {
		String str = "";
		if (index == 1) {
			str = "周一";
		} else if (index == 2) {
			str = "周二";
		} else if (index == 3) {
			str = "周三";
		} else if (index == 4) {
			str = "周四";
		} else if (index == 5) {
			str = "周五";
		} else if (index == 6) {
			str = "周六";
		} else if (index == 7) {
			str = "周日";
		} else {
			str = "";
		}
		return str;
	}

	/**
	 * 判断取图片方式
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public static String checkEmployeePhoto(String type, String id) {
		OnlineUser user = OnlineUserInfo.getUser(id);
		if (user != null) {
			File flieName = new File(BaseEnv.FILESERVERPATH + "/pic/tblEmployee/" + id + "_140.jpg");
			if (!flieName.exists()) {
				if ("1".equals(user.getGender()) || user.getGender() == null) {
					return "/style/images/no_head.gif";
				} else {
					return "/style/images/no_head_gg.gif";
				}
			} else {
				if ("140".equals(type)) {
					return "/ReadFile.jpg?fileName=" + id + "_140.jpg&tempFile=false&type=PIC&tableName=tblEmployee";
				} else if ("48".equals(type)) {
					return "/ReadFile.jpg?fileName=" + id + "_48.jpg&tempFile=false&type=PIC&tableName=tblEmployee";
				}
			}
		}
		return "/style/images/no_head.gif";

	}

	/**
	 * 获取是否要隐藏的权限（隐藏客户，隐藏供应商，隐藏往来单位等）
	 * 
	 * @param reportName
	 *            表名
	 * @param reportfieldName
	 *            字段名
	 * @param fieldIdentity
	 *            隐藏类型
	 * @param url
	 *            对应的模块
	 * @return
	 */
	public boolean hideViewRight(String reportName, String reportfieldName, String fieldIdentity, String url) {
		/* 权限控制 */
		LoginBean loginBean = getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get(url);
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
		scopeRight.addAll(loginBean.getAllScopeRight());
		boolean falg = DynDBManager.getViewRight(reportName, reportfieldName, scopeRight, fieldIdentity, loginBean);
		return falg;
	}

	/**
	 * 获取加密狗代号
	 * 
	 * @return
	 */
	public String securityLockCode() {
		String code = "";
		if (SystemState.instance.dogState != SystemState.DOG_EVALUATE) {
			code = SystemState.instance.dogId;
		}
		return code;
	}

	public static String getWorkId(String userId) {
		return new PublicMgt().getWorkId(userId);
	}

	/**
	 * 获取是星期几
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(String dt) {
		if ("".equals(dt)) {
			return dt;
		}
		String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(BaseDateFormat.parse(dt, BaseDateFormat.yyyyMMdd));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	/**
	 * 获取指定时间的 下一天
	 * 
	 * @param strdt
	 * @return
	 */
	public static String getTomByDate(String strdt) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(BaseDateFormat.parse(strdt, BaseDateFormat.yyyyMMdd));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
	}

	/**
	 * 获取时间差距信息
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static HashMap<String, Object> getDifferTimeInfo(String beginTime, String endTime) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (!"".equals(beginTime) && !"".equals(endTime)) {
			Date begin = new Date();
			Date end = new Date();
			try {
				begin = BaseDateFormat.parse(beginTime, BaseDateFormat.yyyyMMddHHmmss);
				end = BaseDateFormat.parse(endTime, BaseDateFormat.yyyyMMddHHmmss);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			long between = begin.getTime() - end.getTime();
			long day = between / (24 * 60 * 60 * 1000);
			long hour = (between / (60 * 60 * 1000) - day * 24);
			long minute = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long second = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

			String timeStr = "" + day + "天" + hour + "小时" + minute + "分" + second + "秒";
			timeStr = timeStr.replaceAll("-", "");

			String isOver = "false";// 默认不超时
			if (between < 0) {
				isOver = "true";
			}
			map.put("isOver", isOver);// 是否超时
			map.put("day", day);// 相差的天数
			map.put("hour", hour);// 相差的小时
			map.put("timeStr", timeStr);// 封装相差多少x天x小时x分x秒
		}
		return map;
	}

	/**
	 * 跟据用户Id得到职称
	 * 
	 * @param TitleID
	 * @return
	 */
	public String getTitleIDByUserId(String userId) {
		String TitleID = "无";
		OnlineUser onlineUser = (OnlineUser) OnlineUserInfo.cloneMap().get(userId);
		if (onlineUser != null && onlineUser.TitleID != null) {
			TitleID = getEnumerationItemsDisplay("duty", onlineUser.TitleID);
		}
		return TitleID;
	}

	private DBFieldInfoBean getMainfb(ArrayList<DBFieldInfoBean> fieldInfos, String fieldName) {
		for (DBFieldInfoBean fb : fieldInfos) {
			if (fb.getFieldName().equals(fieldName)) {
				return fb;
			}
		}
		return null;
	}

	public String getlayoutHtml(String tableName, ArrayList<DBFieldInfoBean> fieldInfos, ArrayList<DBTableInfoBean> childTableList, String locale, String detail) {
		DBTableInfoBean bean = BaseEnv.tableInfos.get(tableName);
		String html = bean == null ? "" : bean.getLayoutHTML();
		// 如果html中包括#include(/vm/bom/bom.jsp)内容，先引用包含的内容
		if (html != null) {
			while (html.indexOf("#include(") > -1) {
				int pos = html.indexOf("#include(") + "#include(".length();
				int pos2 = html.indexOf(")", pos);
				if (pos2 < 0)
					break;
				String include = html.substring(pos, pos2);
				String str = application.getRealPath(include);
				File file = new File(str);
				if (!file.exists()) {
					html = html.substring(0, pos - "#include(".length()) + html.substring(pos2 + 1);
				} else {
					String repStr = "";
					DataInputStream dis = null;
					try {
						dis = new DataInputStream(new FileInputStream(file));
						byte[] b = new byte[0];
						byte[] bs = new byte[1024];
						int count = 0;
						while ((count = dis.read(bs)) > -1) {
							byte[] temp = new byte[b.length + count];
							System.arraycopy(b, 0, temp, 0, b.length);
							System.arraycopy(bs, 0, temp, b.length, count);
							b = temp;
						}
						repStr = new String(b, "UTF-8");

					} catch (Exception e) {
						BaseEnv.log.error("GlobalsTool.getLayoutHtml Error", e);
					} finally {
						try {
							dis.close();
						} catch (Exception e) {
						}
					}
					html = html.substring(0, pos - "#include(".length()) + repStr + html.substring(pos2 + 1);
				}
			}
		}
		String retHtml = html;
		ArrayList<String> hideField = new ArrayList<String>();// 记录需要隐藏的字段，如果有标识为forid的对象要一起隐藏，为了个性界面字段隐藏但字段名字不能隐藏的问题

		for (DBFieldInfoBean fb : fieldInfos) {
			if (!fb.getFieldName().equals("id") && !fb.getFieldName().equals("moduleType") && fb.getFieldType() != fb.FIELD_AFFIX && fb.getFieldType() != fb.FIELD_PIC) {
				if (fb.getInputType() == 8 && fb.getInputTypeOld() != 2) {
					if (fb.getInputTypeOld() == 1) { // 只读下拉
						String fieldName = fb.getFieldName();
						Pattern pattern = Pattern.compile("<select[^>]*fname=\"" + fieldName + "\"[\\s\\S]*?</select>", Pattern.CASE_INSENSITIVE);
						Matcher mat = pattern.matcher(html);
						boolean hasTable = false;
						if (mat.find()) {
							String all = mat.group();
							String style = "";
							if (all.indexOf("style=\"") > -1) {
								int pos = all.indexOf("style=\"");
								style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String classN = "";
							if (all.indexOf("class=\"") > -1) {
								int pos = all.indexOf("class=\"");
								classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String str = "";

							str = "<input id=\"" + fieldName + "Name\" name=\"" + fieldName
									+ "Name\" type=\"text\" onfocus=\"mainFocus(this);\"  readonly=\"readonly\" onKeyDown=\"if(event.keyCode==13) tabObj.next(this);\" />" + "<select id=\""
									+ fieldName + "\" name=\"" + fieldName + "\"  style=\"display:none;" + style + "\" " + classN + " selectRName=\"" + fieldName + "Name\" >"
									+ "							<option title=\"\" value=\"\" ></option>";
							for (KeyPair $erow : getEnumerationItems(fb.getRefEnumerationName())) {
								str += "<option title=\"" + $erow.getName() + "\" value=\"" + $erow.getValue() + "\" >" + $erow.getName() + "</option>";
							}
							str += "</select>";

							retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
						} else {
							retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
						}
					} else if (fb.getInputTypeOld() == 5) { // 只读复选
						String fieldName = fb.getFieldName();
						Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + fb.getFieldName() + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
						Matcher mat = pattern.matcher(html);
						boolean hasTable = false;
						if (mat.find()) {
							String all = mat.group();
							String style = "";
							if (all.indexOf("style=\"") > -1) {
								int pos = all.indexOf("style=\"");
								style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String classN = "";
							if (all.indexOf("class=\"") > -1) {
								int pos = all.indexOf("class=\"");
								classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String str = "";
							for (KeyPair $erow : getEnumerationItems(fb.getRefEnumerationName())) {
								str += "<input name=" + fieldName + " type=\"checkbox\" onKeyDown=\"if(event.keyCode==13)tabObj.next(this);\"  " + "								" + style + " " + classN
										+ " onclick=\"return false;\" id=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" value=\"" + $erow.getValue() + "\"/>" + "								<label for=\"cbox_"
										+ fieldName + "_" + $erow.getValue() + "\" class=\"cbox_w\" >" + $erow.getName() + "</label>";
							}
							retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
						} else {
							String str = "";
							for (KeyPair $erow : getEnumerationItems(fb.getRefEnumerationName())) {
								str += "<input name=" + fieldName + " type=\"checkbox\" onKeyDown=\"if(event.keyCode==13)tabObj.next(this);\"  "
										+ "								style=\"display:none\" onclick=\"return false;\" id=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" value=\"" + $erow.getValue() + "\"/>"
										+ "								<label for=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" >" + $erow.getName() + "</label>";
							}
							retHtml = str + retHtml;
						}
					} else {// 只读其它
						String fieldName = fb.getFieldName();
						Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + fb.getFieldName() + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
						Matcher mat = pattern.matcher(html);
						boolean hasTable = false;
						if (mat.find()) {
							String all = mat.group();
							String style = "";
							if (all.indexOf("style=\"") > -1) {
								int pos = all.indexOf("style=\"");
								style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String classN = "";
							if (all.indexOf("class=\"") > -1) {
								int pos = all.indexOf("class=\"");
								classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String str = "";
							str = "<input id=\"" + fieldName + "\" name=\"" + fieldName + "\" type=\"text\" onfocus=\"mainFocus(this);\" " + style + " " + classN
									+ " readonly=\"readonly\" onKeyDown=\"if(event.keyCode==13) event.keyCode=9\" >";
							retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
						} else {
							retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
						}
					}
				} else if (fb.getInputType() == 1) { // 下拉
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<select[^>]*fname=\"" + fieldName + "\"[\\s\\S]*?</select>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"");
							style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String str = "";

						str = "<select id=\"" + fieldName + "\" name=\"" + fieldName + "\" " + style + " " + classN + " onKeyDown=\"if(event.keyCode==13) tabObj.next(this);\"  >"
								+ "							<option title=\"\" value=\"\" ></option>";
						for (KeyPair $erow : getEnumerationItems(fb.getRefEnumerationName())) {
							str += "<option title=\"" + $erow.getName() + "\" value=\"" + $erow.getValue() + "\" >" + $erow.getName() + "</option>";
						}
						str += "</select>";

						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
					}
				} else if (fb.getInputType() == 16) { // 下拉动态
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<select[^>]*fname=\"" + fieldName + "\"[\\s\\S]*?</select>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"");
							style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String str = "";

						str = "<select id=\"" + fieldName + "\" name=\"" + fieldName + "\" " + style + " " + classN + " onKeyDown=\"if(event.keyCode==13) tabObj.next(this);\" >";

						str += "</select>";

						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
					}
				} else if (fb.getInputType() == 7) { // 拼音码
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + fb.getFieldName() + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"");
							style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String pymUrl = "/UtilServlet?operation=ajaxPYM&type=updatePYM";
						String str = "";

						str = "<input id=\"" + fieldName + "\" name=\"" + fieldName + "\" type=\"text\" " + style + " " + classN
								+ " onfocus=\"mainFocus(this);\" onKeyDown=\"if(event.keyCode==13)tabObj.next(this);\" onchange=\"dyGetPYM('" + pymUrl + "',this,'" + fieldName + "')\"  />";

						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
					}
				} else if (fb.getInputType() == 7) { // 多语言
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + fb.getFieldName() + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"");
							style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String popUrlValue = "mainMoreLanguage('" + fb.getStringLength() + "','" + fieldName + "')";
						String name = fieldName + "_lan";
						String str = "";

						str = "<input id=\"" + name + "\" name=\"" + name + "\" type=\"text\" " + style + " " + classN + " onfocus=\"mainFocus(this);\"   onchange=\"changeMoreLanguage('" + name
								+ "','" + fieldName + "')\"  onKeyDown=\"if(event.keyCode==13&&this.value.length>0) tabObj.next(this);\" popup=\"select\" ><!--  onDblClick=\"$popUrlValue\" -->"
								+ "				<!--  <b class=\"stBtn icon16\" onClick=\"" + popUrlValue + "\"></b>-->" + " <input id=\"" + fieldName + "\" name=\"" + fieldName + "\" type=\"hidden\" />";
						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
					}
				} else if (fb.getInputType() == 11) { // 功能方法
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + fb.getFieldName() + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"");
							style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}

						String str = "";

						str = "<input \"" + fieldName + "\" name=\"" + fieldName + "\" " + style + " " + classN + " type=\"text\" onfocus=\"mainFocus(this);\" onKeyDown='if(event.keyCode==13) "
								+ fb.getInputValue() + ";' value=\"\">";
						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
					}
				} else if (fb.getInputType() == 5) { // 复选
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + fb.getFieldName() + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"");
							style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String str = "";
						for (KeyPair $erow : getEnumerationItems(fb.getRefEnumerationName())) {
							str += "<input type=\"checkbox\" " + style + " " + classN + " id=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" name=\"" + fieldName + "\"  value=\""
									+ $erow.getValue() + "\"/>" + "<label for=\"cbox_" + fieldName + "_" + $erow.getValue() + "\"  "
									+ (classN.indexOf("cbox input_type") > -1 ? "class=\"cbox_w\"" : "") + " >" + $erow.getName() + "</label>";
						}
						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						String str = "";
						for (KeyPair $erow : getEnumerationItems(fb.getRefEnumerationName())) {
							str += "<input type=\"checkbox\" style=\"display:none\" id=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" name=\"" + fieldName + "\"  value=\"" + $erow.getValue()
									+ "\"/>" + "<label for=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" >" + $erow.getName() + "</label>";
						}
						retHtml = str + retHtml;
					}
				} else if (fb.getInputType() == 10) { // 单选
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + fb.getFieldName() + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"");
							style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String str = "";
						for (KeyPair $erow : getEnumerationItems(fb.getRefEnumerationName())) {
							str += "<input type=\"radio\" " + style + " " + classN + " id=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" name=\"" + fieldName + "\" value=\"" + $erow.getValue()
									+ "\" />" + "<label for=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" >" + $erow.getName() + "</label>";
						}
						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						String str = "";
						for (KeyPair $erow : getEnumerationItems(fb.getRefEnumerationName())) {
							str += "<input type=\"radio\" style=\"display:none\" id=\"cbox_" + fieldName + "_" + $erow.getValue() + "\" name=\"" + fieldName + "\" value=\"" + $erow.getValue()
									+ "\" />" + "";
						}
						retHtml = str + retHtml;
					}
				} else if (fb.getInputType() == 2 || fb.getInputTypeOld() == 2) {
					String fieldName = fb.getFieldName();
					if (fb.getSelectBean() == null) {
						throw new RuntimeException("字段" + fieldName + "弹出窗类型弹窗" + fb.getInputValue() + "不存在");

					}
					if (fb.getSelectBean().getRelationKey().hidden) {
						retHtml = "<input id=\"" + fb.getSelectBean().getFieldName(fb.getSelectBean().getRelationKey().parentName) + "\" name=\""
								+ fb.getSelectBean().getFieldName(fb.getSelectBean().getRelationKey().parentName) + "\" type=\"hidden\"/>" + retHtml;
					} else if ("GoodsField".equals(fb.getFieldSysType()) && getPropBean(fb.getFieldName()).getIsSequence() == 1) {
						String dismh = fieldName + "_mh";
						String mainInput = getMainInputValue(tableName, fieldName);
						String imgPopUrlValue = "";
						String dbPopUrlValue = "";
						if (fb.getLogicValidate() != null && fb.getLogicValidate().indexOf("@IOO:Outstore") != -1) {
							imgPopUrlValue = "if(popMainInput('" + mainInput + "'))openSelect('/UserFunctionAction.do?operation=" + getOP("OP_POPUP_SELECT") + "&type=stockSequence&seq=1',n('" + dismh
									+ "')[0],'" + dismh + "')";
							dbPopUrlValue = "if(popMainInput('" + mainInput + "'))openSelect('/UserFunctionAction.do?operation=" + getOP("OP_POPUP_SELECT") + "&type=addSequence&seq=1',n('" + dismh
									+ "')[0],'" + dismh + "')";
						} else {
							dbPopUrlValue = "if(popMainInput('" + mainInput + "'))openSelect('/UserFunctionAction.do?operation=" + getOP("OP_POPUP_SELECT") + "&type=addSequence&seq=1',n('" + dismh
									+ "')[0],'" + dismh + "')";
							imgPopUrlValue = dbPopUrlValue;
						}
						String str = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\"/>";

						Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + dismh + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
						Matcher mat = pattern.matcher(html);
						boolean hasTable = false;
						if (mat.find()) {
							String all = mat.group();
							String style = "";
							if (all.indexOf("style=\"") > -1) {
								int pos = all.indexOf("style=\"");
								style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String classN = "";
							if (all.indexOf("class=\"") > -1) {
								int pos = all.indexOf("class=\"");
								classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String readOnly = "";
							if (fb.getInputType() == 8) {
								readOnly = "readonly=\"readonly\" ";
							} else {
								readOnly = "popup=\"select\" onDblClick=\"" + dbPopUrlValue + "\"";
							}

							str += "<input id=\"" + dismh + "\" name=\"" + dismh + "\" type=\"text\" onfocus=\"mainFocus(this);\" " + readOnly + " " + style + " " + classN + "  />";
							if (fb.getInputType() != 8) {
								str += "<b class=\"stBtn icon16\" onClick=\"" + imgPopUrlValue + "\"></b>";
							}
							retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
						} else {
							str += "<input type=\"hidden\" id=\"" + dismh + "\" name=\"" + dismh + "\" />";
							retHtml = str + retHtml;
						}
					} else {
						String mainInput = getMainInputValue(tableName, fieldName);
						String mf = "";
						for (String mainField : fb.getSelectBean().getTableParams()) {
							mf += "+'&" + mainField + "='+nec('" + mainField + "')";
						}

						String popUrlValue = "if(popMainInput('" + mainInput + "'))openSelect('/UserFunctionAction.do?keyId=&tableName=" + tableName + "&fieldName=" + fieldName + "&operation="
								+ getOP("OP_POPUP_SELECT") + "'" + mf + ",this,'" + fieldName + "')";
						String popUrlValue2 = "if(popMainInput('" + mainInput + "'))mainSelect2('/UserFunctionAction.do?keyId=&tableName=" + tableName + "&fieldName=" + fieldName + "&operation="
								+ getOP("OP_POPUP_SELECT") + "'" + mf + ",'',this,'" + fieldName + "')";

						String str = "";
						String tfb = fb.getSelectBean().getFieldName(fb.getSelectBean().getRelationKey().parentName);
						Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + tfb + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
						Matcher mat = pattern.matcher(html);
						boolean hasTable = false;
						if (mat.find()) {
							String all = mat.group();
							String style = "";
							if (all.indexOf("style=\"") > -1) {
								int pos = all.indexOf("style=\"");
								style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							String classN = "";
							if (all.indexOf("class=\"") > -1) {
								int pos = all.indexOf("class=\"");
								classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
							}
							if (fb.getInputType() == fb.INPUT_HIDDEN || fb.getInputType() == fb.INPUT_NO) { // 如果是隐藏或不输入，要设置为不显示，因为如工作流中保密字段
								str = "<input type=\"hidden\" id=\"" + tfb + "\" name=\"" + tfb + "\" />";
								hideField.add(fieldName);
							} else {
								String readOnly = "";
								if (fb.getInputType() == 8) {
									readOnly = "readonly=\"readonly\" ";
								} else {
									readOnly = "onKeyDown=\"if(event.keyCode==13&&this.value.length>0)tabObj.next(this);\"  popup=\"select\" onKeyUp=\"" + popUrlValue2
											+ "\" onfocus=\"this.oldValue=this.value; mainFocus(this);\" onblur=\"cl(this);\" onDblClick=\"" + popUrlValue + "\"";
								}

								str += "<input id=\"" + tfb + "\" name=\"" + tfb + "\" " + readOnly + " " + style + " " + classN + "  type=\"text\" relationkey=\"true\" />";
								if (fb.getInputType() != 8) {
									str += "<b class=\"stBtn icon16\" onClick=\"" + popUrlValue + "\"></b>";
								}
							}
							retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
						} else {
							str += "<input type=\"hidden\" id=\"" + tfb + "\" name=\"" + tfb + "\" />";
							retHtml = str + retHtml;
						}
					}
					// 处理弹出窗其它字段

					if (!("GoodsField".equals(fb.getFieldSysType()) && getPropBean(fb.getFieldName()).getIsSequence() == 1)) {
						for (PopField srow : fb.getSelectBean().getViewFields()) {
							String colName = "";
							String tableField = "";
							if (srow.display != null && srow.display.length() > 0) {
								if (srow.display.indexOf("@TABLENAME") == 0) {
									int index = srow.display.indexOf(".") + 1;
									tableField = tableName + "." + srow.display.substring(index);
								} else {
									tableField = srow.display;
								}
								colName = srow.display;
							} else {
								tableField = "";
								colName = srow.fieldName;
							}
							// byte
							// viewFieldType=getFieldBean(srow.fieldName).getFieldType();

							String mf = "";
							String mfn = "";
							for (String mainField : fb.getSelectBean().getTableParams()) {
								mf += "+'&" + mainField + "='+nec('" + mainField + "')";
								mfn += "+'&" + mainField + "='+n('" + mainField + "')[0].value";
							}
							String mainInput = getMainInputValue(tableName, fieldName);
							String popUrlValue = "if(popMainInput('" + mainInput + "'))openSelect('/UserFunctionAction.do?keyId=&tableName=" + tableName + "&fieldName=" + fieldName + "&operation="
									+ getOP("OP_POPUP_SELECT") + "'" + mf + ",this,'" + fieldName + "')";
							String ajaxUrlValue = "'/UtilServlet?operation=Ajax&tableName=" + tableName + "&fieldName=" + fieldName + "'" + mfn;
							String openUrlValue = "'/UserFunctionAction.do?operation=" + getOP("OP_POPUP_SELECT") + "&tableName=" + tableName + "&fieldName=" + fieldName + "'" + mfn;
							String str = "";
							String asName = getTableField(srow.asName);

							Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + asName + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
							Matcher mat = pattern.matcher(html);
							boolean hasTable = false;
							if (mat.find()) {
								String all = mat.group();
								String style = "";
								if (all.indexOf("style=\"") > -1) {
									int pos = all.indexOf("style=\"");
									style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
								}
								String classN = "";
								if (all.indexOf("class=\"") > -1) {
									int pos = all.indexOf("class=\"");
									classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
								}
								if (fb.getInputType() == fb.INPUT_HIDDEN || fb.getInputType() == fb.INPUT_NO) { // 如果是隐藏或不输入，要设置为不显示，因为如工作流中保密字段
									str = "<input type=\"hidden\" id=\"" + asName + "\" name=\"" + asName + "\" />";
									hideField.add(fieldName);
								} else {
									String readOnly = "";
									if (fb.getInputType() == 8) {
										readOnly = "readonly=\"readonly\" onfocus=\"mainFocus(this);\" ";
									} else {
										readOnly = "onKeyUp=\"if(popMainInput('" + getMainInputValue(tableName, fieldName) + "'))mainSelect2(" + ajaxUrlValue + "," + openUrlValue + ",this,'"
												+ fieldName + "');\"  onblur=\"cl(this);\" onfocus=\"this.oldValue=this.value; mainFocus(this);\" popup=\"select\" onDblClick=\"" + popUrlValue + "\" ";
									}

									str += "<input id=\"" + asName + "\" name=\"" + asName + "\" type=\"text\"  " + style + " " + classN + " " + readOnly + "   />";
									if (fb.getInputType() != 8) {
										str += "<b class=\"stBtn icon16\" onClick=\"" + popUrlValue + "\"></b>";
									}
								}
								retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
							} else {
								str += "<input type=\"hidden\" id=\"" + asName + "\" name=\"" + asName + "\" />";
								retHtml = str + retHtml;
							}
						}
					}
				} else if (fb.getFieldType() == fb.FIELD_TEXT || fb.getFieldType() == fb.FIELD_HTML) { // 备注
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<textarea[^>]*fname=\"" + fieldName + "\"[\\s\\S]*?</textarea>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"") + 7;
							style = all.substring(pos, all.indexOf("\"", pos) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String str = "";
						if (fb.getInputType() == fb.INPUT_HIDDEN || fb.getInputType() == fb.INPUT_NO) { // 如果是隐藏或不输入，要设置为不显示，因为如工作流中保密字段
							str = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />";
						} else {
							str = "<textarea id=\"" + fieldName + "\" name=\"" + fieldName + "\" style=\"" + (fb.getFieldType() == fb.FIELD_HTML ? "display:none;" : "") + style + " " + classN
									+ "\" ></textarea>";
						}
						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
					}
				} else {
					String fieldName = fb.getFieldName();
					Pattern pattern = Pattern.compile("<input[^>]*fname=\"" + fb.getFieldName() + "\"[^>]*>", Pattern.CASE_INSENSITIVE);
					Matcher mat = pattern.matcher(html);
					boolean hasTable = false;
					if (mat.find()) {
						String all = mat.group();
						String style = "";
						if (all.indexOf("style=\"") > -1) {
							int pos = all.indexOf("style=\"");
							style = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String classN = "";
						if (all.indexOf("class=\"") > -1) {
							int pos = all.indexOf("class=\"");
							classN = all.substring(pos, all.indexOf("\"", pos + 7) + 1);
						}
						String str = "";
						if (fb.getInputType() == fb.INPUT_HIDDEN || fb.getInputType() == fb.INPUT_NO) { // 如果是隐藏或不输入，要设置为不显示，因为如工作流中保密字段
							str = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />";
							hideField.add(fieldName);
						} else if (fb.getFieldType() == fb.FIELD_DATE_TIME) {
							// 输入，时间
							str = "<input id=\""
									+ fieldName
									+ "\" name=\""
									+ fieldName
									+ "\" type=\"text\" "
									+ style
									+ " "
									+ classN
									+ " onfocus=\"mainFocus(this);\" onKeyDown=\"if(event.keyCode==13){ if(this.value.length>0){ tabObj.next(this); }else{  _SetTime(this);}}\" onClick=\"_SetTime(this);\" >";
						} else if (fb.getFieldType() == fb.FIELD_DATE_SHORT) {
							// 输入，短日期
							str = "<input id=\"" + fieldName + "\" name=\"" + fieldName + "\" type=\"text\"  " + style + " " + classN
									+ " onfocus=\"mainFocus(this);\" onKeyDown=\"if(event.keyCode==13){tabObj.next(this);}\" onClick=\"WdatePicker({lang:'" + getLocale() + "'});\">";
						} else if (fb.getFieldType() == fb.FIELD_DATE_LONG) {
							// 输入，长日期
							str = "<input id=\"" + fieldName + "\" name=\"" + fieldName + "\" type=\"text\"  " + style + " " + classN
									+ " onfocus=\"mainFocus(this);\" onKeyDown=\"if(event.keyCode==13){tabObj.next(this);}\" onClick=\"WdatePicker({lang:'" + getLocale()
									+ "',dateFmt:'yyyy-MM-dd HH:mm:ss'});\">";
						} else if (fb.getFieldType() == fb.FIELD_PASSWORD) {
							// 输入，密码
							str = "<input id=\"" + fieldName + "\" name=\"" + fieldName + "\" type=\"password\" " + style + "  onKeyDown=\"if(event.keyCode==13) tabObj.next(this);\">";
						} else if (fb.getFieldType() == fb.FIELD_DOUBLE) {
							// 输入，浮点
							str = "<input id=\"" + fieldName + "\" name=\"" + fieldName + "\" type=\"text\" " + style + " " + classN
									+ "  onfocus=\"mainFocus(this);\" onKeyDown=\"if(event.keyCode==13){tabObj.next(this);}\" >";
						} else {
							// 输入，其它
							str = "<input id=\"" + fieldName + "\" name=\"" + fieldName + "\" type=\"text\" " + style + " " + classN
									+ "  onfocus=\"mainFocus(this);\" onKeyDown=\"if(event.keyCode==13){tabObj.next(this);}\" >";
						}

						retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
					} else {
						// 如果是隐藏的下拉框
						pattern = Pattern.compile("<select[^>]*fname=\"" + fieldName + "\"[\\s\\S]*?</select>", Pattern.CASE_INSENSITIVE);
						mat = pattern.matcher(html);
						if (mat.find()) {
							String all = mat.group();
							String str = "";
							if (fb.getInputType() == fb.INPUT_HIDDEN || fb.getInputType() == fb.INPUT_NO) { // 如果是隐藏或不输入，要设置为不显示，因为如工作流中保密字段
								str = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />";
								hideField.add(fieldName);
							}
							retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
						} else {
							retHtml = "<input type=\"hidden\" id=\"" + fieldName + "\" name=\"" + fieldName + "\" />" + retHtml;
						}
					}

				}
			}
		}
		// 检查有没有明细图片和附件
		Pattern pattern = Pattern.compile("<textarea[^>]*fname=\"detailpic\"[\\s\\S]*?</textarea>", Pattern.CASE_INSENSITIVE);
		Matcher mat = pattern.matcher(html);
		boolean hasTable = false;
		if (mat.find()) {
			String all = mat.group();
			String style = "";
			if (all.indexOf("style=\"") > -1) {
				int pos = all.indexOf("style=\"") + 7;
				style = all.substring(pos, all.indexOf("\"", pos) + 1);
			}
			String str = "<div>";
			str += "<div class=\"showGridTags\">";
			for (DBTableInfoBean rowlist : childTableList) {
				if ("true".equals(GlobalsTool.getSysSetting(rowlist.getTableSysType())) && rowlist.getIsUsed() == 1 && rowlist.getIsView() == 0) {
					String gridtable = rowlist.getTableName() + "Table";
					str += "<span show=\"" + gridtable + "DIV\" id=\"" + gridtable + "DIVTitle\" name=\"detTitle\" class=\"tags\" onclick=\"changeTags(this);\">" + rowlist.getDisplay().get(locale)
							+ "</span>";
				}
			}

			for (DBFieldInfoBean fb : fieldInfos) {
				if (fb.getInputType() != 100 && fb.getInputType() != 3 && (fb.getFieldType() == 13 || fb.getFieldType() == 14) && fb.getInputType() != 6) {
					str += "<span show=\"" + fb.getFieldName() + "DIV\" class=\"tags\" name=\"detTitle\" onclick=\"changeTags(this);\">" + fb.getDisplay().get(locale)
							+ "<img src=\"/style/images/mail/attach.gif\" border=0 /> </span>";
				}
			}
			if (childTableList.size() > 0 && !"detail".equals(detail)) {
				str += "<div class=\"detbtBar\">\n";
				str += "    <li id='b_calc' class='f-icon16' onclick='cellCalc()' title='计算器'></li>\n";
				str += "    	<li id='b_dragcel' class='f-icon16' onclick='cellDrag()' title='拖选复制单元格'></li>\n";
				str += "    <li id='b_upline' class='f-icon16' onclick='celupline()'  title='上移'></li>\n";
				str += "    <li id='b_downline' class='f-icon16' onclick='celdownline()' title='下移'></li>\n";
				str += "    <li id='b_addline' class='f-icon16' onclick='celladdline()' title='插入一行'></li>\n";
				str += "    <li id='b_stickline' class='f-icon16' onclick='cellstickline()' title='复制整行'></li>\n";
				str += "    <li id='b_delline' class='f-icon16' onclick='celldelline()' title='删除本行'></li>\n";
				str += "    </div>";
				boolean hasGoodsbc = false;
				if ("1".equals(GlobalsTool.getSysSetting("scanType"))) {
					for (DBTableInfoBean rowlist : childTableList) {
						if ("true".equals(GlobalsTool.getSysSetting(rowlist.getTableSysType())) && rowlist.getIsUsed() == 1 && rowlist.getIsView() == 0
								&& !"tblGoodsUnit".equals(rowlist.getTableName())) {
							for (DBFieldInfoBean row : rowlist.getFieldInfos()) {
								if (!hasGoodsbc && "GoodsCode".equals(row.getFieldName()) && row.getInputType() != 3) {
									hasGoodsbc = true;
									str += "<div class='tags' style='margin-left:50px;padding:0px 10px 0px 10px'>";
									str += "	<label >条码扫描</label>";
									str += "		<input type=\"text\" id=\"scanBarcode\" name=\"scanBarcode\" onkeydown=\"if(event.keyCode==13){ scanBarCodeEv(this,'" + rowlist.getTableName()
											+ "',true);stopBubble(event);}\"/>";
									str += "	<label style=\"vertical-align: middle;\"><img src=\"/style/images/wh.png\" height=\"17\" style=\"margin-top:-2px\" onclick=\"scanBarCodeHelp('barcode')\"/></label>";
									str += "</div>";
								}
							}
						}
					}
				} else if ("2".equals(GlobalsTool.getSysSetting("scanType"))) {
					for (DBTableInfoBean rowlist : childTableList) {
						if ("true".equals(GlobalsTool.getSysSetting(rowlist.getTableSysType())) && rowlist.getIsUsed() == 1 && rowlist.getIsView() == 0
								&& !"tblGoodsUnit".equals(rowlist.getTableName())) {
							for (DBFieldInfoBean row : rowlist.getFieldInfos()) {
								if (!hasGoodsbc && "GoodsCode".equals(row.getFieldName()) && row.getInputType() != 3) {
									hasGoodsbc = true;
									str += "<div class='tags' style='margin-left:50px;padding:0px 10px 0px 10px'>";
									str += "	<label >条码扫描</label>";
									str += "		<input type=\"text\" id=\"scanBarcode\" name=\"scanBarcode\" onkeydown=\"if(event.keyCode==13){ scanBarCodeEv(this,'" + rowlist.getTableName()
											+ "',false);stopBubble(event);}\"/>";
									str += "	<label style=\"vertical-align: middle;\"><img src=\"/style/images/wh.png\" height=\"17\" style=\"margin-top:-2px\" onclick=\"scanBarCodeHelp('barcode')\"/></label>";
									str += "</div>";
								}
							}
						}
					}
				} else if ("3".equals(GlobalsTool.getSysSetting("scanType"))) {
					for (DBTableInfoBean rowlist : childTableList) {
						if ("true".equals(GlobalsTool.getSysSetting(rowlist.getTableSysType())) && rowlist.getIsUsed() == 1 && rowlist.getIsView() == 0
								&& !"tblGoodsUnit".equals(rowlist.getTableName())) {
							for (DBFieldInfoBean row : rowlist.getFieldInfos()) {
								if ("Seq".equals(row.getFieldName()) && row.getInputType() != 3 && "@IOO:Outstore".equals(row.getLogicValidate())) {
									str += "<div class='tags' style='margin-left:50px;padding:0px 10px 0px 10px'>";
									str += "	<label >序列号扫描</label>";
									str += "		<input type=\"text\" id=\"scanBarcode\" name=\"scanBarcode\" onkeydown=\"if(event.keyCode==13){ scanSeqEv(this,'" + rowlist.getTableName()
											+ "');stopBubble(event);}\"/>";
									str += "	<label style=\"vertical-align: middle;\"><img src=\"/style/images/wh.png\" height=\"17\" style=\"margin-top:-2px\" onclick=\"scanBarCodeHelp('seq')\"/></label>";
									str += "</div>";
								}
							}
						}
					}
				}
			}
			str += "</div>";

			str += "<div class=\"scroll_function_small_ud\" id=\"listRange_tableInfo\" " + style + " >";
			for (DBTableInfoBean rowlist : childTableList) {
				if ("true".equals(GlobalsTool.getSysSetting(rowlist.getTableSysType())) && rowlist.getIsUsed() == 1 && rowlist.getIsView() == 0) {
					String gridtable = rowlist.getTableName() + "Table";
					str += "<script language=\"javascript\">";
					str += "var tableId=\"" + gridtable + "\";";
					str += "</script>";
					str += "	<div  name=\"" + gridtable + "DIV\" id=\"" + gridtable + "DIV\"  ah=\"" + gridtable + "\"  > ";
					str += "		<table border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" class=\"listRange_list_function_b\" name=\"" + gridtable + "\" id=\"" + gridtable + "\">";
					str += "		</table>		";
					str += "	</div>";
				}
			}
			for (DBFieldInfoBean fb : fieldInfos) {
				if (fb.getInputType() != 100 && fb.getInputType() != 3 && fb.getFieldType() == 13 && fb.getInputType() != 6) {
					str += "<div  name=\"" + fb.getFieldName() + "DIV\" id=\"" + fb.getFieldName() + "DIV\" attType=\"PIC\" style=\"border:1px solid #dedede; overflow-y:auto\"> ";
					str += "<div class=\"listRange_1_photoAndDoc_1\">";
					if (!"detail".equals(detail)) {
						str += "<span class=\"btn btn-mini\" id=\"picbuttonthe222\" name=\"picbuttonthe222\"  onClick=\"upload('PIC','" + fb.getFieldName() + "');\">上传图片</span>";
						str += "<span class=\"btn btn-mini\" name=\"picbuttonthe333\" onClick=\"linkPIC('" + fb.getFieldName() + "');\">链接图片</span>	";
					}
					str += "<ul id=\"picuploadul_" + fb.getFieldName() + "\" >";

					str += "</ul>";
					str += "</div>";
					str += "</div>	";
				} else if (fb.getInputType() != 100 && fb.getInputType() != 3 && fb.getFieldType() == 14 && fb.getInputType() != 6) {
					str += "<div  name=\"" + fb.getFieldName() + "DIV\" id=\"" + fb.getFieldName() + "DIV\" attType=\"ATT\" style=\"border:1px solid #dedede; overflow-y:auto\"> ";
					str += "<div class=\"listRange_1_photoAndDoc_1\">";
					if (!"detail".equals(detail)) {
						str += "<span class=\"btn btn-mini\" id=\"affixbuttonthe222\" name=\"affixbuttonthe222\"  onClick=\"upload('AFFIX','" + fb.getFieldName() + "');\">上传附件</span>";
					}
					str += "<ul id=\"affixuploadul_" + fb.getFieldName() + "\" style=\"width:98%;\">";

					str += "</ul>";
					str += "</div>";
					str += "</div>	";
				}
			}
			str += "</div> <!-- 明细图片区 -->";
			str += "</div>";

			retHtml = retHtml.substring(0, retHtml.indexOf(all)) + str + retHtml.substring(retHtml.indexOf(all) + all.length());
		}
		retHtml = retHtml.replaceAll("aioscript", "script"); // 复原所有自定义脚本
		if (hideField.size() > 0) {
			// 有隐藏的字段
			String hideScript = " \n<script language='javascript'>   \n";
			for (String f : hideField) {
				hideScript += " $('[forid=" + f + "]').hide(); \n";
			}
			hideScript += "</script>\n";
			retHtml += hideScript;
		}
		return retHtml;
	}

	public Object getClassObj(String classStr) {
		Class cls;
		try {
			cls = Class.forName(classStr);
			return cls.newInstance();
		} catch (Exception e) {
			BaseEnv.log.error("GlobalsTool.getClassObj Error", e);
			return null;
		}
	}

	public String formatDate(long date) {
		return BaseDateFormat.format(new Date(date), BaseDateFormat.yyyyMMddHHmmss);
	}
	/**
	 * 标准界面可以执行一个独立的js，来完成一些特殊操作
	 * @param tableName
	 * @return
	 */
	public String getDefineJs(String tableName) {
		String rs="";
		String path = request.getSession().getServletContext().getRealPath("aaaa.sql");
		path = path.substring(0,path.length()-8);
		if(new File(path+"definejs/"+tableName+".js").exists()){
			return "<script type='text/javascript' src='/definejs/"+tableName+".js'></script>";
		}
		if(new File(path+"definejs/"+tableName+".vjs").exists()){
			return "<script type='text/javascript' src='/definejs/"+tableName+".vjs'></script>";
		}
		return rs;
	}


}
