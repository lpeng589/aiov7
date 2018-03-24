package com.menyi.web.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.bsf.BSFException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.mobile.DetailBean;
import com.menyi.aio.web.mobile.MFlowNodeBean;
import com.menyi.aio.web.mobile.Message;
import com.menyi.aio.web.sysAcc.ReCalcucateMgt;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
/**
 * 深圳市科荣软件股份有限公司自定义平台接口
 * 
 * @version 1.0<br/>
 * 创建历史：2016-03-23<br/>
 * 修改历史：<br/>
 * 
 * @author 周新宇<br/><br/><br/>
 * 自定义功能接口<br/>
 * 本接口所有方法返回json对象，建议采用ajax调用。<br/>
 * 本接口同样适用C/S软件或手机APP，或JAVA HttpConnect调用，但需注意调用内部接口前应省先调用登陆接口<br/>
 * 如果是C/S软件调用接口，应先调用登陆接口，并分析接口返回头文件中的Cookie取得标识JSESSIONID,并在调用其它接口前把JSESSIONID值通过Cookie或URL连同接口要求参数一起发送给服务器
 * <br/>
 * 本接口调用地址为http://你的IP或域名/AIOApi?op=方法 或http://你的IP或域名/MobileAjax?op=方法(老版本地址)<br/>
 * <br/>
 * 例:<br/>
 * jQuery.post("/MobileAjax?op=LOGIN",<br/>
 * &nbsp;&nbsp;      { name: loginName, password: password },<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;      function(data){<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;        if(data.code != "OK"){<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         	alert(data.msg);//失败原因<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         }else{<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         //成功<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         } <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;      },<br/>
 * &nbsp;&nbsp;      "json" <br/>
 *	);<br/>
 * 应用场景一（添加单据）：<br/><br/>
 * 1、调用addPrepare.初始化界面数据，对应aio点击添加按扭，弹出添加界面，接口会返回默认值等信息<br/>
 * 2、调用add,保存数据到数据表。对应aio点击保存按扭<br/>
 * 3、调用deliverToPrepare，取单据的审核流信息。对应aio弹出窗审核界面（
 * 接口返回下一审核节点，下一审核人信息。如果单据未审核审核流，本接口可以不执行，本接口取得信息后可以弹出界面让用户选择转交信息，也可以直接调用deliver接口）<br/>
 * 4、调用deliver，执行审核程序。对应aio审核界面的保存按扭。<br/><br/>
 * 应用场景二（修改单据）：<br/><br/>
 * 1、调用detail 取单据数据，对应aio点击修改，弹出修改界面，接口同时返回本单的权限信息，是否允许修改，审核，反审核，撤回，同时包括单据字段只读，隐藏等信息<br/>
 * 2、调用update,保存数据到数据表。对应aio点击保存按扭<br/>
 * 3、调用deliverToPrepare，取单据的审核流信息。对应aio弹出窗审核界面（接口返回下一审核节点，下一审核人信息）<br/>
 * 4、调用deliver，执行审核程序。对应aio审核界面的保存按扭。<br/><br/>
 * 应用场景三（审核单据）：<br/><br/>
 * 1、调用check 校验单据（如果是修改单据，可省略本接口，如果没有调用保存按扭，一定要先调本接品，校验单据内容是否正确）<br/>
 * 3、调用deliverToPrepare，取单据的审核流信息。对应aio弹出窗审核界面（接口返回下一审核节点，下一审核人信息）<br/>
 * 4、调用deliver，执行审核程序。对应aio审核界面的保存按扭。<br/><br/>
 * 应用场景四（查报表）：<br/><br/>
 * 1、调用report 取报表数据。<br/><br/>
 * 应用场景五（弹出窗）：<br/><br/>
 * 1、调用popupInfo 取弹出窗的显示列信息。<br/>
 * 2、调用popup 取弹出窗内容。<br/><br/>
 * 
 */
public class AIOConnect extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Gson gson;
	static { 
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}

	private static UserMgt userMgt = new UserMgt();

	private static DynDBManager dbmgt = new DynDBManager();

	private static UserFunctionMgt userFunMgt = new UserFunctionMgt();

	private static OAMyWorkFlowMgt oamgt = new OAMyWorkFlowMgt();

	private static PublicMgt pcmgt = new PublicMgt();
	
	public static String strDefaultKey = "laozhouzxy"; 
	public static DES des=new DES();

	
    /**
	 * 工作流催办
	 * 
	 * @param tableName  表名
	 * @param keyId 单据ID
	 * <br/><br/>
	 * 返回值: {code:OK,msg:摧办成功}
	 */
    public static Message hurryTrans(String locale,LoginBean loginBean,String keyId, String tableName,String content,String wakeType) {
		OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
		HashMap map=mgt.getOAMyWorkFlowInfo(keyId,tableName);
		
		if(map!=null){
			String createBy=map.get("createBy").toString();
			String applyType=map.get("applyType").toString();
			String checkPerson=map.get("checkPerson").toString();
			String nextNodeIds=map.get("nextNodeIds").toString();
			String currNodeId=map.get("currentNode").toString();
			String oaTimeLimit=map.get("benchmarkTime").toString();
			String oaTimeLimitUnit=map.get("oaTimeLimitUnit").toString();
			
			String applyBy=OnlineUserInfo.getUser(createBy).getName();
			wakeType=wakeType==null?"":wakeType;
			mgt.hurryTrans(applyBy,applyType,checkPerson,keyId,nextNodeIds,currNodeId,oaTimeLimit,oaTimeLimitUnit,
					new Locale(locale),"",content,wakeType,"hurryTrans","",loginBean,"",getResources());
		
    	}
		Message msg = new Message("OK", "摧办成功");
		return msg;
    }
	


	

	/**
	 * 修改自定义单据，
	 * 
	 * @param tableName  表名
	 * @param parentTableName 父表名
	 * @param fieldName 字段名
	 * @param saveType saveDraft:存草稿;空:正式单据
	 * @param defineInfo 如自定义中有弹出可选确认框，会自动带出一个字定义标识代码，按原文传入。默认为空
	 * @param values  存储所有单据字段及其值的HashMap<fieldName,value>
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message update(String locale,LoginBean loginBean,String tableName, String saveType,
			String defineInfo,String valuesJson) {
		
		HashMap<String, String> values = gson.fromJson(valuesJson, HashMap.class);

		Result rs = new Result();
		Hashtable map = BaseEnv.tableInfos;

		//设置兄弟表的ID
		String f_brother=values.get("f_brother");
		f_brother = f_brother == null ? "" : f_brother;
		String moduleType = values.get("moduleType");

		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
		LoginBean lg = loginBean;

		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
		String currNodeId = "";
		String strCheckPersons = "";
		String designId = "";
		if (workFlow != null && workFlow.getTemplateStatus() == 1) {
			HashMap oaMap = pcmgt.getOAMyWorkFlowInfo(String.valueOf(values.get("id")), tableName);
			if (oaMap != null) {
				designId = String.valueOf(oaMap.get("applyType"));
				strCheckPersons = String.valueOf(oaMap.get("checkPerson"));
				currNodeId = String.valueOf(oaMap.get("currentNode"));
				/*查看当前审核人里有没有委托给我的*/
				HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(lg.getId(), designId);
				for (String person : strCheckPersons.split(";")) {
					if (consignMap.get(person) != null) {
						strCheckPersons += lg.getId() + ";";
					}
				}
			}
			if ("-1".equals(currNodeId) || (strCheckPersons.length() > 0 && !strCheckPersons.contains(";" + lg.getId() + ";")) && !"0".equals(currNodeId)) {
				Message msg = new Message("OK", GlobalsTool.getMessage(locale, "common.msg.hasAudit"));
				return msg;
			}
			
		}
		String sysParamter = tableInfo.getSysParameter();

		//判断是否启用分支机构
		String sunClassCode = lg.getSunCmpClassCode();//从LoginBean中取出分支机构的classCode

		MessageResources resources = getResources();

		/*保存方式*/
		//zxy 将midCalculate 写入value中，方便 在define中以@valueOfDB:midCalculate=true来判断当前操作为中间运算
		boolean isMidCalculate = false;
		if ("midCalculate".equals(saveType)) {
			saveType = "";
			isMidCalculate = true;
			values.put("midCalculate", "true");
		}
		//修改打印次数
		int printCount = new GlobalMgt().getPrintCount(tableName, String.valueOf(values.get("id")));
		values.put("printCount", printCount + "");
		Hashtable props = (Hashtable) BaseEnv.servletContext.getAttribute(BaseEnv.PROP_INFO);
		Result rso = dbmgt.detail(tableName, BaseEnv.tableInfos, values.get("id").toString(), lg.getSunCmpClassCode(), props, lg.getId(), false,  "");

		if("zh_CN".equals(locale)){
			rs = dbmgt.update(tableInfo.getTableName(), map, values, lg.getId(), defineInfo, resources, new Locale("zh","CN"), saveType,lg, workFlow, props);
		} else{
			rs = dbmgt.update(tableInfo.getTableName(), map, values, lg.getId(), defineInfo, resources, new Locale(locale), saveType,lg, workFlow, props);
		}
		

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
			/*添加系统日志*/
			//得到修改前记录
			HashMap valuesOld = new HashMap();
			if (rso.retCode == ErrorCanst.DEFAULT_SUCCESS && rso.retVal != null) {
				valuesOld = (HashMap) rso.retVal;
			}
			int operation = 1;
			if ("saveDraft".equals(saveType))
				operation = 6;
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				//这里模块名没有根据模块去取，而是直接是表名
				String billTypeName = tableInfo.getDisplay().get(locale); //getModuleNameByLinkAddr(request, mapping);
				dbmgt.addLog(operation, values, valuesOld, tableInfo, locale, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
			}
			//更新成功后保存脚本
			String path = BaseEnv.servletContext.getRealPath("DoSysModule.sql");
			TestRW.saveToSql(path, DynDBManager.mainUpdate);
			ArrayList childsDel = DynDBManager.childsDel;
			for (int i = 0; i < childsDel.size(); i++) {
				TestRW.saveToSql(path, childsDel.get(i).toString());
			}
			DynDBManager.childsDel.clear();
			ArrayList childsIns = DynDBManager.childsIns;
			for (int i = 0; i < childsIns.size(); i++) {
				TestRW.saveToSql(path, childsIns.get(i).toString());
			}
			DynDBManager.childsIns.clear();

			//同步组织架构到 即时通讯客户端
			String objId = (String) values.get("id");
			if ("tblEmployee".equals(tableName)) {
				boolean flag = new UserMgt().getOpenFlagUserById(objId);
				if (flag == true) {
					MSGConnectCenter.refreshEmpInfo(objId);
				}
			}
			if ("tblDepartment".equals(tableName)) {
				MSGConnectCenter.updateDeptInfo(objId);
			}
			//检查当前表名是否有主表存在,需要根据主表获取权限
			if (tableName.equals("tblStockAnalysisInfo")) {
				StockAnalysisInfoMgt mgt = new StockAnalysisInfoMgt();
				mgt.serivce();
			}

			//修改成功
			String alertMessage = GlobalsTool.getMessage(locale, "common.msg.updateSuccess");
			if (rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
				alertMessage = dbmgt.getDefSQLMsg(locale, (String) rs.retVal);
			}
			Message msg = new Message("OK", alertMessage);
			return msg;

		} else {
			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				//自定义配置需要用户确认
				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;

			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, saveType);
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}
		}
	}

	/**
	 * 校验单据的会计期间
	 * @param tableName
	 * @param keyId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static boolean vildatePeriod(String tableName, String keyId,LoginBean loginBean) throws Exception {
		DBTableInfoBean tableInfo = (DBTableInfoBean) BaseEnv.tableInfos.get(tableName);
		String sysParamter = tableInfo.getSysParameter();// 表信息系统参数
		Date time = null;
		String timeStr = "";
		String workFlowNodeName = "";
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean bean = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (bean.getDefaultValue() != null && "accendnotstart".equals(bean.getFieldIdentityStr())) {
				AIODBManager aioMgt = new AIODBManager();
				Result rs = aioMgt.sqlList("select " + bean.getFieldName() + ",workFlowNodeName from " + tableName + " where id='" + keyId + "'", new ArrayList());
				if (((ArrayList) rs.retVal).size() > 0) {
					timeStr = ((Object[]) ((ArrayList) rs.retVal).get(0))[0].toString();
					workFlowNodeName = ((Object[]) ((ArrayList) rs.retVal).get(0))[1].toString();
				}
			}
		}
		if (timeStr.length() > 0) {
			time = BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
			int currentMonth = 0;
			int currentYear = 0;
			if (null != time) {
				currentMonth = time.getMonth() + 1;
				currentYear = time.getYear() + 1900;
			}
			int periodMonth = -1;
			int periodYear = -1;
			AccPeriodBean accBean = (AccPeriodBean) BaseEnv.accPerios.get(loginBean.getSunCmpClassCode());
			if (accBean != null) {
				periodMonth = accBean.getAccMonth();
				periodYear = accBean.getAccYear();

			}
			if ((currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0) && currentMonth != 0 && !"draft".equals(workFlowNodeName)) {
				if (null != sysParamter) {
					boolean flag = false;
					if (sysParamter.equals("CurrentAccBefBill") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
						flag = true;
					}

					if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)) {
						flag = true;
					}
					if (flag) {
						return true;
					}

				}
			}
		}
		return false;
	}
	
	/**
	 * DBTableInfoBean 按tbleName排序
	 */
	private static class SortDBTable implements Comparator {
		public int compare(Object o1, Object o2) {
			DBTableInfoBean table1 = (DBTableInfoBean) o1;
			DBTableInfoBean table2 = (DBTableInfoBean) o2;

			if (table1 == null || table2 == null) {
				return 0;
			}

			String tableName1 = table1.getTableName();
			String tableName2 = table2.getTableName();

			return tableName1.compareToIgnoreCase(tableName2);
		}
	}

	/**
	 * 取自定义单据详细内容(本接口不对外开放)，
	 * 
	 * <br/><br/>
	 * 返回值: DetailBean对象，见DetailBean接口说明
	 * @see DetailBean
	 */
	public static  DetailBean  detail(String locale, LoginBean loginBean,  String keyId, String tableName) {
		DetailBean detailBean = new DetailBean();

		DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(tableName);
		boolean isDetail = true;

		String parentTableName = tableInfo.getPerantTableName();
		if (parentTableName != null && parentTableName.indexOf(";") > 0) {
			parentTableName = parentTableName.substring(0, parentTableName.indexOf(";"));
		}
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) BaseEnv.servletContext.getAttribute("userSettingColConfig");


		final Result billDetail = dbmgt.detail(tableName, BaseEnv.tableInfos, keyId, loginBean.getSunCmpClassCode(), BaseEnv.propMap, loginBean.getId(), true,  "");
		if (billDetail.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
			detailBean.setMsg("查询详情报错" + billDetail.getRetVal());
			return detailBean;
		}
		HashMap values = (HashMap) billDetail.getRetVal();
		if(values==null || values.get("id")==null){
    		//未找到记录
			detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
			detailBean.setMsg("记录不存在");
			return detailBean;
    	}

		String moduleType = values.get("moduleType") == null ? "" : values.get("moduleType") + "";
		MOperation mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
		if (mop == null) { 
			//所有我能查看的单据，我都有办理和查看详情的权限
			mop=new MOperation();
            mop.update=false;
            mop.print=false;
            mop.query=true;
            mop.sendEmail=false;
            mop.oaWorkFlow=true;
			
			//detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
			//detailBean.setMsg("权限MOP为空，请检查是否分配模块权限");
			//return detailBean;
		}

		boolean CannotOper = false;
		//如果当前期间不为期初，则期初表的添加，删除，修改按钮不能显示
		if (!"-1".equals(((Hashtable) BaseEnv.sessionSet.get(loginBean.getId())).get("NowPeriod"))) {
			if (tableName.equals("tblCompanybeginning") || tableName.equals("tblBeginStock") || (tableName.equals("tblFixedAssetAdd") && "0".equals(moduleType))) {
				CannotOper = true;
			}
		}
		HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId, tableName);
		OAWorkFlowTemplate template = flowMap == null ? BaseEnv.workFlowInfo.get(tableName) : BaseEnv.workFlowInfo.get(flowMap.get("applyType"));

		//整理对哪些人,部门创建的单据有修改权限
		String[] deptRight = dbmgt.getDeptRight(mop.getScope(MOperation.M_UPDATE), mop.getScope(MOperation.M_DELETE), loginBean.getAllScopeRight());
		String updateOtherRight = deptRight[0];//查看他人单据权限
		String updateDeptRight = deptRight[1];//部门管辖权限
		// 整理对哪些人,部门创建的单据有删除权限
		String delOtherRight = deptRight[2];
		String delDeptRight = deptRight[3];
		String createBy = (String) values.get("createBy");
		String employeeId = (String) values.get("EmployeeID");
		//部门优先取deparmentCode,其次employeeId,再次createBy
		String dept = (String) values.get("DepartmentCode");
		//部门优先取deparmentCode,其次employeeId,再次createBy
		if (dept == null && employeeId != null)
			dept = OnlineUserInfo.getUser(employeeId) == null ? null : OnlineUserInfo.getUser(employeeId).getDeptId();
		if (dept == null)
			dept = OnlineUserInfo.getUser(createBy) == null ? "" : OnlineUserInfo.getUser(createBy).getDeptId();
		boolean deleteRight = mop.delete()
				&& !CannotOper
				&& dbmgt.getDeleteRight(dept, delDeptRight, delOtherRight, loginBean.getId(), (String) values.get("createBy"), (String) values.get("EmployeeID"), (String) values.get("workFlowNode"),
						(String) values.get("workFlowNodeName"), template);
		detailBean.setDeleteRight(deleteRight);

		if (values.get("createBy") != null) {
			/* 查询当前单据别人委托给我的工作流程 */
			HashMap<String, String> consignMap = new HashMap<String, String>();

			if (template != null) {
				consignMap = DynDBManager.queryConsignation(loginBean.getId(), template.getId());
			}

			//基础信息，且未启用审核流
			if (tableInfo.getIsBaseInfo() == 1 && (template == null || template.getTemplateStatus() == 0)) {
				employeeId = loginBean.getId();
				dept = loginBean.getDepartCode();
			}

			boolean isPeriodBefor = false; //是否是会计期间前的数据
			//检查是否是会计期间前的数据      
			String copy = "";
			if (!"copy".equals((String) copy)) {
				String sysParamter = tableInfo.getSysParameter();//表信息系统参数
				Date time = null;
				for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
					DBFieldInfoBean bean = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
					if (bean.getDefaultValue() != null && "accendnotstart".equals(bean.getFieldIdentityStr())) {
						String timeStr = values.get(bean.getFieldName()).toString();
						if (timeStr != null) {
							try {
								time = BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
							} catch (Exception e) {
								BaseEnv.log.error("MobileAjax.detail 转化日期格式错误", e);
							}
						}
					}
				}
				int currentMonth = 0;
				int currentYear = 0;
				if (null != time) {
					currentMonth = time.getMonth() + 1;
					currentYear = time.getYear() + 1900;
				}
				int periodMonth = -1;
				int periodYear = -1;
				AccPeriodBean accBean = (AccPeriodBean) sessionSet.get("AccPeriod");
				if (accBean != null) {
					periodMonth = accBean.getAccMonth();
					periodYear = accBean.getAccYear();

				}
				String workFlowNodeName = (String) values.get("workFlowNodeName");
				if ((currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0) && currentMonth != 0 && !"draft".equals(workFlowNodeName)) {
					if (null != sysParamter) {
						if (sysParamter.equals("CurrentAccBefBill") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
							isPeriodBefor = true;
						}

						if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)) {
							isPeriodBefor = true;
						}
					}

				}
			}

			boolean updateRight = mop.update()
					&& !CannotOper
					&& dbmgt.getUpdateRight(dept, updateDeptRight, updateOtherRight, loginBean.getId(), (String) values.get("createBy"), employeeId, (String) values.get("checkPersons"), consignMap,
							(String) values.get("workFlowNode"), (String) values.get("workFlowNodeName"), template);

			//根据审核流判断此用户是否有修改权限
			if (template != null && !template.getTemplateClass().equals("00002") && template.getTemplateStatus() == 1 && !"draft".equals(values.get("workFlowNodeName"))) {

				if ("-1".equals((String) values.get("workFlowNode"))) {
					if (OnlineUserInfo.getUser(values.get("createBy").toString()) != null) {
						boolean flag = dbmgt.isRetCheckPer(loginBean, template, OnlineUserInfo.getUser(values.get("createBy").toString()).getDeptId());
						if (!flag && values.get("DepartmentCode") != null && !values.get("DepartmentCode").equals("")) {
							flag = dbmgt.isRetCheckPer(loginBean, template, values.get("DepartmentCode") + "");
						}
						if (flag) {//如果有反审核，显示反审核按钮
							detailBean.setRetCheckRight(true);
						}
					}
				} else if (flowMap != null && "0".equals(String.valueOf(flowMap.get("currentNode")))) {
					//开始结点与普通权限一致
					if (updateRight) { //有修改权限就有审核权限
						detailBean.setCheckRight(true);
					}
				} else {
					String strCheckPersons = String.valueOf(values.get("checkPersons"));
					if (strCheckPersons != null) {
						// 催办,只有创建人可以催办,并且如果当前审核人不自己
						if (values.get("createBy").toString().equals(loginBean.getId()) && strCheckPersons.indexOf(";" + loginBean.getId() + ";") < 0) {
							detailBean.setHurryTrans(true);
						}
						/*查看当前审核人里有没有委托给我的*/
						String designId = flowMap != null ? String.valueOf(flowMap.get("applyType")) : "";
						for (String person : strCheckPersons.split(";")) {
							if (consignMap.get(person) != null) {
								strCheckPersons += loginBean.getId() + ";";
							}
						}
						if (strCheckPersons.indexOf(";" + loginBean.getId() + ";") >= 0 || ("0".equals((String) values.get("workFlowNode")) && updateRight)) {
							detailBean.setCheckRight(true);
						}
						if (flowMap != null && (!detailBean.isCheckRight())) { //列表是有审核按扭就不出现撤回按扭，详情保持一致
							//判断是否具有撤回按钮
							String lastCheckPerson = String.valueOf(flowMap.get("lastCheckPerson"));
							String currentNode = String.valueOf(flowMap.get("currentNode"));
							FlowNodeBean flowNode = BaseEnv.workFlowDesignBeans.get(designId).getFlowNodeMap().get(currentNode);
							if (loginBean.getId().equals(lastCheckPerson) && flowNode != null && flowNode.isAllowCancel()) {
								detailBean.setHasCancel(true);
							}
						}

					}
				}
			}
			detailBean.setUpdateRight(updateRight);
			//非会计期间前，有修改权限，
			if (!isPeriodBefor && updateRight) {
				isDetail = false;
				detailBean.setOpType("update");
			} else {
				isDetail = true;
				detailBean.setOpType("detail");
			}

		}

		//取范围权限，要放在这里，因为detail在有权限时自动变修改
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(isDetail == true ? mop.getScope(MOperation.M_QUERY) : mop.getScope(MOperation.M_UPDATE));
		scopeRight.addAll(loginBean.getAllScopeRight());

		detailBean.setValues(values); //设置返回值		

		ArrayList childTableList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);

		HashMap<String, ArrayList<String[]>> moduleTable = (HashMap) BaseEnv.ModuleField.get(mop.getModuleUrl()); // 同一表有多个模块，且配置的不同模块字段不一致的信息

		String allTableName = tableName + ",";

		ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>();
		ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();// 主表的列配置
		Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String, ArrayList<ColConfigBean>>();// 子表的列配置
		if (userColConfig != null && userColConfig.size() > 0) { // 加载主表的列配置
			configList = userColConfig.get(tableName + "bill");
			if (configList != null) {
				allConfigList.addAll(configList);
			}
		}
		for (int i = 0; i < childTableList.size(); i++) { // 加载明细表的列配置
			DBTableInfoBean childTableInfo = (DBTableInfoBean) childTableList.get(i);
			allTableName += childTableInfo.getTableName() + ",";
			// 加载明细中自定义列的显示
			if (allConfigList != null && allConfigList.size() > 0) {
				ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName() + "bill");
				if (childConfigList != null) {
					// 如果做了模块字段设置，那么列配置字段中将不包含这些字段，页面将不再做处理
					if (moduleTable != null && moduleTable.get(childTableInfo.getTableName()) != null) {
						ArrayList<String[]> moduleFields=(ArrayList<String[]>)moduleTable.get(childTableInfo.getTableName());
            			ArrayList<ColConfigBean> cols=new ArrayList<ColConfigBean>();
            			for(ColConfigBean colBean :childConfigList){
            				boolean found = false;
            				for(String[] mf :moduleFields){
            					if(mf[0].equals(colBean.getColName())){
            						found=true;
            						break;
            					}
            				}
            				if(true){
            					cols.add(colBean);
            				}
            			}
						allConfigList.addAll(cols);
						childTableConfigList.put(childTableInfo.getTableName(), cols);
					} else {
						allConfigList.addAll(childConfigList);
						childTableConfigList.put(childTableInfo.getTableName(), childConfigList);
					}
				}
			}
		}
		Collections.sort(childTableList, new SortDBTable());
		ArrayList ctn = new ArrayList();
		for (DBTableInfoBean dtb : (ArrayList<DBTableInfoBean>) childTableList) {
			Map kv = new HashMap();
			if(dtb.getIsUsed() == 1){
				kv.put("name",dtb.getTableName());
				kv.put("display",dtb.getDisplay().get(locale));
				ctn.add(kv);
			}
			
			/*
			if(dtb.getIsUsed()==1){
				HashMap map = new HashMap();
				map.put("tableName", dtb.getTableName());
				map.put("display", dtb.getDisplay().get(locale));
				ctn.add(map);
			}*/
		}
		detailBean.setChildTableList(ctn);

		String userLastNode = ""; // 用户当前最后一个结点信息

		// 设置和工作流相关信息
		if (template != null && template.getTemplateStatus() == 1 && flowMap != null) {
			String currNodeId = "";
			String designId = "";
			if (flowMap != null) {
				currNodeId = String.valueOf(flowMap.get("currentNode"));
				designId = String.valueOf(flowMap.get("applyType"));
			} else {
				currNodeId = "-1";
			}

			WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
			if (designBean == null) {
				detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
				detailBean.setMsg("工作流" + designId + "不存在");
				return detailBean;
			}

			if (isDetail) {
				Result rst = oamgt.getUserLastNode(keyId, loginBean.getId(), tableName);
				if (rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null) {
					userLastNode = rst.retVal.toString();
				}
				if (userLastNode == null || userLastNode.length() == 0) {
					userLastNode = currNodeId;
				}
				if (designBean.getFlowNodeMap().get(userLastNode) == null) {
					userLastNode = currNodeId;
				}
			} else {
				userLastNode = currNodeId;
			}

			Result rs1 = oamgt.flowDepict(designId, keyId, tableName);
			if (rs1.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				detailBean.setFlowDepict((ArrayList) rs1.getRetVal());
			}
		}

		String f_brother = values.get("f_brother") == null ? "" : values.get("f_brother") + "";

		// 根据权限,列配置，工作流决定显示哪些字段
		ArrayList<DBFieldInfoBean> fieldList;
		try {
			fieldList = DynDBManager.getMainFieldList(tableName, tableInfo,null, configList, template, flowMap, moduleTable, mop, userLastNode, isDetail, f_brother,
				loginBean, scopeRight);
		} catch (Exception e) {
			detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
			detailBean.setMsg(e.getMessage());
			return detailBean;
		}
		detailBean.setFieldList(DynDBManager.distinctList(fieldList));

		HashMap moreLan = (HashMap) values.get("LANGUAGEQUERY");
		for (int i = 0; i < fieldList.size(); i++) {
			DBFieldInfoBean bean = (DBFieldInfoBean) fieldList.get(i);
			if (bean.getInputType() == bean.INPUT_MAIN_TABLE) {
				if (bean.getSelectBean() != null) {
					for (int j = 0; j < bean.getSelectBean().getViewFields().size(); j++) {
						PopField pop = (PopField) bean.getSelectBean().getViewFields().get(j);
						DBFieldInfoBean bean2 = GlobalsTool.getFieldBean(pop.getFieldName().substring(0, pop.getFieldName().indexOf(".")), pop.getFieldName().substring(
								pop.getFieldName().indexOf(".") + 1));
						if (bean2 == null && pop.getDisplay() != null && pop.getDisplay().length() > 0) {
							bean2 = GlobalsTool.getFieldBean(pop.getDisplay().substring(0, pop.getDisplay().indexOf(".")), pop.getDisplay().substring(pop.getDisplay().indexOf(".") + 1));
						}
						// 当弹窗里面的表是视图时，查询详情时报错，参考后台UserFunctionAction#updatePrepare后注释该代码
						if (bean2 == null) {
//							detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
//							detailBean.setMsg("字段" + bean.getFieldName() + "弹出窗" + bean.getSelectBean().getName() + "字段" + pop.getFieldName() + "不存在");
//							return detailBean;
							continue;
						}
						if (bean2.getInputType() == bean2.INPUT_LANGUAGE || bean2.getInputTypeOld() == bean2.INPUT_LANGUAGE) {
							if (moreLan.get(values.get(pop.getAsName())) != null) {
								values.put(pop.getAsName(), ((KRLanguage) moreLan.get(values.get(pop.getAsName()))).get(locale));
							} else {
								values.put(pop.getAsName(), values.get(pop.getAsName()));
							}
						}
					}
				}
			}
		}

		
		
		HashMap childShowField = new HashMap();
		for (int i = 0; i < childTableList.size(); i++) { // 明细表的显示列
			DBTableInfoBean childTableInfo = (DBTableInfoBean) childTableList.get(i);
			
			try {
				childShowField.put(childTableInfo.getTableName(), DynDBManager.getMainFieldList(childTableInfo.getTableName(), childTableInfo,null, childTableConfigList.get(childTableInfo.getTableName()), template, flowMap, moduleTable,
						mop, userLastNode, isDetail, f_brother, loginBean, scopeRight));
			} catch (Exception e) {
				detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
				detailBean.setMsg(e.getMessage());
				return detailBean;
			}
		}
		detailBean.setChildShowField(childShowField);
		

		return detailBean;
	}

	





	/**
	 * 自定义单据添加接口，
	 * 
	 * @param tableName  表名
	 * @param parentCode 父目录classCode
	 * @param saveType saveDraft:存草稿;空:正式单据
	 * @param defineInfo 如自定义中有弹出可选确认框，会自动带出一个字定义标识代码，按原文传入。默认为空
	 * @param values  存储所有单据字段及其值的HashMap<fieldName,value>
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message add(String locale,LoginBean loginBean,String tableName,String parentCode,String defineInfo,String saveType,String valuesJson,String deliverTo) {
		
		
		// =======任何人不得删除本段代码 周新宇=============
		/**
		 * 在这里对所有操作进行加密狗的检查， 试用版也在这里对试用条件进行检查， 由于所有自定义表都由本方法执行， 所以检查的比例定为百分之一
		 * 
		 */
		if ((System.currentTimeMillis() - com.menyi.web.util.SystemState.uCheckTime) > 1 * 24 * 60 * 60000) {
			com.menyi.web.util.SystemState.uCheckTime = System.currentTimeMillis();
			System.out.println("UserFunctionAction check Dog----------------------");
			if (com.menyi.web.util.SystemState.instance.encryptionType == com.menyi.web.util.SystemState.ENCRYPTION_EVAL) {
				// 检查试用
				// 进销存模块的检查
				// 查询采购，销售，库存表的，最初时间
				int er = new InitMenData().checkEnv();
				if (2 == er) {
					SystemState.instance.dogState = SystemState.DOG_ERROR_ENV_BIS;
					Message msg = new Message("ERROR", "试用超量");
					return msg;
				}
			} else if (com.menyi.web.util.SystemState.instance.encryptionType == com.menyi.web.util.SystemState.ENCRYPTION_SOFT) {
				File file = new File("aio.cert");
				if (!file.exists()) {
					com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
				}
				// 读文件
				byte[] bs = null;
				try {
					FileInputStream fis = new FileInputStream(file);
					int len = fis.available();
					bs = new byte[len];
					fis.read(bs);
				} catch (Exception e) {
					// 加密狗或软加密错误
					com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
				}
				// 读日期和公司名称
				byte[] tempBs = new byte[bs.length - 128];
				System.arraycopy(bs, 128, tempBs, 0, tempBs.length);
				// 解码
				try {
					// 读密钥
					FileInputStream fisKey = new FileInputStream("../../website/WEB-INF/private1024.key");
					ObjectInputStream oisKey = new ObjectInputStream(fisKey);
					Key key = (Key) oisKey.readObject();
					oisKey.close();
					fisKey.close();

					Cipher cipher = Cipher.getInstance("RSA");

					// 用私钥解密
					cipher.init(Cipher.DECRYPT_MODE, key);
					bs = cipher.doFinal(tempBs);
					// 读序列号
					tempBs = new byte[30];
					System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
					SystemState.instance.dogId = new String(tempBs).trim();
					// 证书版本，预留
					// 读机器码
					tempBs = new byte[32];
					System.arraycopy(bs, 32, tempBs, 0, tempBs.length);
					SystemState.instance.pcNo = new String(tempBs).trim();
					// 取机器码
					Random rd = new Random();
					rd.setSeed(System.currentTimeMillis());
					int rdi = rd.nextInt(4000);
					String s = CallSoftDll.get(rdi + "");
					byte[] mb = new byte[16];
					mb = SecurityLock2.hexStringToBytes(s);
					rdi += 5;
					for (int i = 0; i < mb.length; i++) {
						mb[i] = (byte) (mb[i] - rdi);
						if (i > 8) {
							mb[i] = (byte) (mb[i] - 2);
						}
					}
					// 校验机器码
					String realPcNo = SecurityLock2.bytesToHexString(mb);

					if (SystemState.instance.pcNo == null || !SystemState.instance.pcNo.equals(realPcNo)) {
						// 机器码不一致
						s = CallSoftDll.get("mac");
						if (SystemState.instance.pcNo == null || !SystemState.instance.pcNo.equals(s)) {
							com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
						}

					}
				} catch (Exception e) {

				}
			} else if (com.menyi.web.util.SystemState.instance.encryptionType == com.menyi.web.util.SystemState.ENCRYPTION_DOG2) {
				int version = SecurityLock.getVersion();
				if (version == 1) {
					// 第二代加密狗
					byte[] bs = null;

					// 解码
					try {
						// 读密钥
						FileInputStream fisKey = new FileInputStream("../../website/WEB-INF/private1024.key");
						ObjectInputStream oisKey = new ObjectInputStream(fisKey);
						Key key = (Key) oisKey.readObject();
						oisKey.close();
						fisKey.close();

						Cipher cipher = Cipher.getInstance("RSA");

						// 用私钥解密
						cipher.init(Cipher.DECRYPT_MODE, key);
						byte[] tempBs = SecurityLock.getData();
						bs = cipher.doFinal(tempBs);
						//读狗号
						tempBs = new byte[30];
						System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
						SystemState.instance.dogId = new String(tempBs).trim();
						//证书版本，预留
						//读机器码
						tempBs = new byte[32];
						System.arraycopy(bs, 32, tempBs, 0, tempBs.length);
						SystemState.instance.pcNo = new String(tempBs).trim();
						//校验机器码
						String[] realPcNo = SecurityLock.F_ReadKey(1);

						if (SystemState.instance.pcNo == null || !SystemState.instance.pcNo.equalsIgnoreCase(realPcNo[0] + "-" + realPcNo[1]) || SystemState.instance.dogId == null
								|| !SystemState.instance.dogId.equalsIgnoreCase(SecurityLock.readKeyId() + "")) {
							//加密狗号或特殊码不一致
							com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
						}
					} catch (Exception e) {

					}
				}
			}
		}
		//=======任何人不得删除以上代码 周新宇=============

		Result rs = new Result();
		/*保存在内存中所有表结构的信息*/
		Hashtable map = BaseEnv.tableInfos;

		LoginBean lg = loginBean;
		
		/*设置兄弟表的f_brother*/
		
		HashMap values = gson.fromJson(valuesJson, HashMap.class);

		/*表结构信息*/
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);

		/*从内存中读取当前单据的工作流信息*/
		OAWorkFlowTemplate workFlow = null;

		workFlow = BaseEnv.workFlowInfo.get(tableName);
		//System.out.println("*****workFlow********"+workFlow);
		/*初始化一些字段的基本信息*/
		Result rs_initDBField = userFunMgt.initDBFieldInfo(tableInfo, lg, values, parentCode, workFlow);
		/* fjj不成功 删除单据编号*/
		
		if (rs_initDBField.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
			/*不能录入会计前数据*/
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.currentaccbefbill"));
			return msg;
		} else if (rs_initDBField.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC) {
			/*单据日期的期间在会计期间中不存在*/
			Message msg = new Message("ERROR", "单据日期的期间在会计期间中不存在");
			return msg;
		}else if (rs_initDBField.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			BaseEnv.log.error("AIOConnect.add initDBFieldInfo Error:"+rs_initDBField.retCode+":"+rs_initDBField.retVal);
			Message msg = new Message("ERROR", "初始化字段错误"+rs_initDBField.retVal);
			return msg;
		}

		//获取路径
		String path = BaseEnv.servletContext.getRealPath("DoSysModule.sql");

		MessageResources resources = getResources();
		
		Hashtable props = (Hashtable) BaseEnv.servletContext.getAttribute(BaseEnv.PROP_INFO);

		//zxy 将midCalculate 写入value中，方便 在define中以@valueOfDB:midCalculate=true来判断当前操作为中间运算
		boolean isMidCalculate = false;
		if ("midCalculate".equals(saveType)) {
			saveType = "saveAdd";
			isMidCalculate = true;
			values.put("midCalculate", "true");
		}

		if("zh_CN".equals(locale)){
			rs = dbmgt.add(tableInfo.getTableName(), map, values, lg.getId(), path, defineInfo, resources,new Locale("zh","CN"), saveType, lg, workFlow, deliverTo, props);
		} else{
			rs = dbmgt.add(tableInfo.getTableName(), map, values, lg.getId(), path, defineInfo, resources,new Locale(locale), saveType, lg, workFlow, deliverTo, props);
		}
		

		String kid = values.get("id") == null ? "" : values.get("id").toString();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {

			String billTypeName = tableInfo.getDisplay().get(locale);
			
			String[] returnValue = (String[]) rs.retVal;
			/*当前多语言种类*/
			String addMessage = GlobalsTool.getMessage(locale, "common.lb.add");
			/*添加系统日志*/
			int operation = 0;
			if ("saveDraft".equals(saveType))
				operation = 5;
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
			}

			String message = GlobalsTool.getMessage(locale, "common.msg.addSuccess");
			if (returnValue[1] != null && returnValue.length > 0) {
				if (rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
					message = dbmgt.getDefSQLMsg(locale, returnValue[1]);
				} else {
					message += "<br>" + GlobalsTool.getMessage(locale, "userfunction.msg.billNoExistNew") + ":" + returnValue[1];
				}
			}
			HashMap retMap = new HashMap();
			retMap.put("msg", message);
			retMap.put("keyId", kid);
			Message msg = new Message("OK",message, retMap);
			return msg;
		} else {
			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				// 自定义配置需要用户确认
				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;
			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, saveType);
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}
		}
	}



	/**
	 * 执行自定义Define代码，
	 * 
	 * @param tableName  表名
	 * @param defineName  自定义define的名字
	 * @param buttonTypeName define的中文名字
	 * @param keyId 单据ID，本参数会传入自定义参数表中，可以挤接ID字段，在Define中再折分
	 * @param defineInfo 如自定义中有弹出可选确认框，会自动带出一个字定义标识代码，按原文传入。默认为空
	 
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message  execDefine(String locale,LoginBean loginBean,String defineName, String buttonTypeName,String[] keyIds,String tableName,String defineInfo) {
		
		MessageResources resources = getResources();

		Result rs = dbmgt.defineDelSql(defineName, keyIds, loginBean.getId(), resources, new Locale(locale), defineInfo);

		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS&& rs.getRetCode() != ErrorCanst.RET_DEFINE_SQL_ALERT) {
			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				// 自定义配置需要用户确认
				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;
			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, "");
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}
		} else {
			int operation = 14;
			Hashtable map = BaseEnv.tableInfos;
			Hashtable props = (Hashtable) BaseEnv.servletContext.getAttribute(BaseEnv.PROP_INFO);
			Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
			boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
			DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(tableName);
			for (int i = 0; i < keyIds.length; i++) {
				String keyId = keyIds[i];
				/*从内存中读取当前单据的工作流信息*/
				OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
				if (keyId.indexOf("hasChild") > 0)
					keyId = keyId.substring(0, keyId.indexOf(";hasChild"));
				Result result = dbmgt.detail(tableName, map, keyId, loginBean.getSunCmpClassCode(), props, loginBean.getId(), isLastSunCompany, "");

				HashMap values = (HashMap) result.getRetVal();
				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), loginBean.getId(), loginBean.getEmpFullName(), loginBean.getSunCmpClassCode(), buttonTypeName, tableInfo.getDisplay().get(locale));
			}
			HashMap msg = new HashMap();
			msg.put("code", "OK");
			msg.put("msg", "执行成功");
			if(rs.retVal instanceof String[]){
				String[] ss = (String[])rs.retVal;
				if(ss.length > 1){
					msg.put("toUrl", ss[1]);
				}
				msg.put("msg", ss[0]);
			}
			return new Message("OK","",msg);
		}
	}


	
	/**
	 * 审核前准备接口，返回审核结点，审核人等信息，
	 * 
	 * @param keyId 审核单据ID
	 * @param tableName 审核表名
	 * <br/><br/>
	 * 返回值: {code:OK,obj:{nextNodes：下一步结点列表，可能有多个；currNode：当前结点信息；designId：工作流模板ID；moduleName：模块名字；keyId：工作流对应单据ID；tableName表名；dispenseWake：允许分发信息}}
	 */
	public static Message deliverToPrepare(String locale,LoginBean loginBean,String keyId,String tableName,String currNodeId) {
		
		ArrayList<FlowNodeBean> nodeBeans = new ArrayList<FlowNodeBean>();
		//检查是否是会计期间前的数据(检查所有的单据)
		if (tableName != null && !"".equals(tableName)) {
			boolean vpError;
			try {
				vpError = vildatePeriod(tableName, keyId, loginBean);
				if (vpError) {
					Message msg = new Message("ERROR", "会计期间前数据不可以审核");
					return msg;
				}
			} catch (Exception e) {
				BaseEnv.log.error("MobileAjax.deliverToPrepare Error:",e);
				Message msg = new Message("ERROR", "会计期间前数据报错"+e.getMessage());
				return msg;
			}
			
		}
		//当单据转交时，判断单据是否已经审核完毕，如果审核完毕不允许执行转交操作,如果审核人没有当前审核人也不允许执行此操作
		String designId = "";
		String department = "";
		String strCheckPersons = "";
		String currentNode = "";
		String myflowId = "";
		HashMap map = oamgt.getOAMyWorkFlowInfo(keyId, tableName);
		if (map != null) {
			designId = String.valueOf(map.get("applyType"));
			department = String.valueOf(map.get("departmentCode"));
			currentNode = String.valueOf(map.get("currentNode"));
			strCheckPersons = String.valueOf(map.get("checkPerson"));
			myflowId = String.valueOf(map.get("id"));
		}

		/*查看当前审核人里有没有委托给我的*/
		HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(loginBean.getId(), designId);
		for (String person : strCheckPersons.split(";")) {
			if (consignMap.get(person) != null) {
				strCheckPersons += loginBean.getId() + ";";
			}
		}
		if ("-1".equals(currentNode)) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.msg.hasAudit"));
			return msg;
		}
		//本参数只有在消息连接中出现，单据中链接不传入此参数
		String outNodeId = currNodeId;
		if (outNodeId != null && outNodeId.length() > 0 && !currentNode.equals(outNodeId)) {
			//开始结点转交后，对方打开消息未处理，然后制单人撤回开始结点，对方再继续处理消息时从外部传入的当前结点id和数据库记录的当前结点不一致，需报错
			Message msg = new Message("ERROR", "此单当前结点发生变化，可能原因有撤回，中止，或其它人已审核此结点");
			return msg;
		}

		//!"0".equals(currentNode) 这个条件不能被去掉。因为在开始结点单据权限和现有权限一至，即有此单修改权限的在开始结点可以转交单据
		if (strCheckPersons.length() > 0 && !strCheckPersons.contains(";" + loginBean.getId() + ";") && !"0".equals(currentNode)) {
			Message msg = new Message("ERROR", "您不是此单的审核人");
			return msg;
		}

		WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
		if (designBean == null) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "oa.fileflow.error2"));
			return msg;
		}
		String nextNodes = "";
		Result rs = oamgt.getNextNodeIdByVal(designId, currentNode, keyId, true, loginBean);
		if ("".equals(rs.retVal) || rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "oa.errormsg.noNode") + rs.retVal);
			return msg;
		} else {
			nextNodes = rs.retVal.toString();
		}

		WorkFlowDesignBean bean = BaseEnv.workFlowDesignBeans.get(designId);
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(designId);

		oamgt.transactStart(currentNode, keyId, loginBean, tableName);

		String[] nextNodeIds = nextNodes.split(";");
		FlowNodeBean currNB = bean.getFlowNodeMap().get(currentNode);

		//解析节点的审核人
		for (int i = 0; i < nextNodeIds.length; i++) {
			FlowNodeBean nodeBean = bean.getFlowNodeMap().get(nextNodeIds[i]);
			if (nodeBean == null) {
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "oa.errormsg.noCheckPerson"));
				return msg;
			}
			FlowNodeBean nodeBeanNew = new FlowNodeBean();
			nodeBeanNew.setKeyId(nodeBean.getKeyId());
			nodeBeanNew.setDisplay(nodeBean.getDisplay());
			nodeBeanNew.setApprovePeople(nodeBean.getApprovePeople());
			nodeBeanNew.setZAction(nodeBean.getZAction());
			nodeBeanNew.setForwardTime(nodeBean.isForwardTime());
			if (nodeBean.getZAction().equals("CHECK")) {
				try {
					ArrayList<String[]> checkPersons = oamgt.getNodeCheckPerson(nodeBean, department, keyId, null, tableName, loginBean.getId());
					if (checkPersons.size() > 0) {
						nodeBeanNew.setCheckPeople(checkPersons);
					} else {
						Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "oa.errormsg.noCheckPerson"));
						return msg;
					}
				} catch (Exception e) {
					Message msg = new Message("ERROR", e.getMessage());
					return msg;
				}
			}
			nodeBeans.add(nodeBeanNew);
		}
		//查看当前节点如果有回退功能，那么查询之前所有审核过的节点及这些节点的审核人
		if (currNB.isAllowBack()) {
			try {
				oamgt.getBackNodeBean(myflowId, currNB, nodeBeans, bean);
			} catch (Exception e) {
				BaseEnv.log.error("MobileAjax.deliverToPrepare error:",e);
				Message msg = new Message("ERROR", e.getMessage());
				return msg;
			}
		}

		if (tableName == null || tableName.length() == 0) {
			tableName = workFlow.getTemplateFile();
		}
		HashMap<String,Object> data = new HashMap();
		ArrayList<MFlowNodeBean> mnodeBeans = new ArrayList<MFlowNodeBean>();
		for(FlowNodeBean b:nodeBeans){
			MFlowNodeBean mb = new MFlowNodeBean();
			mb.setApprovePeople(b.getApprovePeople());
			mb.setCheckPeople(b.getCheckPeople());
			mb.setDisplay(b.getDisplay());
			mb.setForwardTime(b.isForwardTime());
			mb.setKeyId(b.getKeyId());
			mb.setUseAllApprove(b.isUseAllApprove());
			mb.setZAction(b.getZAction());
			mnodeBeans.add(mb);
		}
		
		MFlowNodeBean mcurrNB = new MFlowNodeBean();
		mcurrNB.setApprovePeople(currNB.getApprovePeople());
		mcurrNB.setCheckPeople(currNB.getCheckPeople());
		mcurrNB.setDisplay(currNB.getDisplay());
		mcurrNB.setForwardTime(currNB.isForwardTime());
		mcurrNB.setKeyId(currNB.getKeyId());
		mcurrNB.setUseAllApprove(currNB.isUseAllApprove());
		mcurrNB.setZAction(currNB.getZAction());
		
		data.put("nextNodes", mnodeBeans);
		data.put("currNode", mcurrNB);
		data.put("designId", designId);
		data.put("moduleName", BaseEnv.workFlowInfo.get(designId).getTemplateName());
		data.put("keyId", keyId);
		data.put("tableName", tableName);
		data.put("dispenseWake", workFlow.getDispenseWake()); // 分发，手机版暂时不做此功能
		Message msg = new Message("OK","", data);
		return msg;
	}
	

	
	/**
	 * 删除自定义单据，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * <br/><br/>
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message delete(String locale,LoginBean loginBean,String tableName, String keyId) {

		ActionForward forward = null;

		if( keyId ==null || keyId.length() == 0){
			Message msg = new Message("ERROR", "请选择要删除的数据");
			return msg;
		}
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);

		/** 如果启用了审核流，查询当前是否已经审核，如果已经审核不能删除！！* */
		if ( keyId !=null && keyId.length() > 0 && workFlow != null && workFlow.getTemplateStatus() == 1) {
			OAMyWorkFlowMgt workFlowMgt = new OAMyWorkFlowMgt();
			Object[] workFlowNode = workFlowMgt.getTableWorkFlowName(tableName, keyId);
			if (workFlowNode != null && (!"draft".equals(workFlowNode[1]) && !workFlowNode[0].equals("0"))) {
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.approvedNotDelete"));
				return msg;
			}
		}

		Result rsAcc = dbmgt.hasCreateAcc(tableName, keyId);
		if (rsAcc.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (rsAcc.retVal != null) {// 如果已经生成凭证，则给出错误提示
				String[] str = (String[]) rsAcc.retVal;
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.hasCreateAcc.Oper.error", str[0], str[1]));
				return msg;
			}
		} else {
			Message msg = new Message("ERROR", new ErrorMessage().toString(rsAcc.retCode, locale));
			return msg;
		}
		Hashtable map = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(tableName);
		LoginBean lg = loginBean;
		String sunClassCode = lg.getSunCmpClassCode(); // 从LoginBean中取出分支机构的classCode
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		// 如果已经月结，不允许删除
		HashMap values = new HashMap();
		String sysParamter = tableInfo.getSysParameter();
		Date time = null;
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			String o = fi.getDefaultValue();
			String type = fi.getFieldIdentityStr();
			if (null != type) {
				if (null != o && type.equals("accendnotstart")) {
					String date = (String) dbmgt.getFieldValueById(new String[]{keyId}, tableInfo.getTableName(), fi.getFieldName());
					if (null != date) {
						try {
							time = BaseDateFormat.parse(date, BaseDateFormat.yyyyMMdd);
						} catch (Exception e) {
							Message msg = new Message("ERROR", "字段" + fi.getFieldName() + "被设为开账前操作，但格式不正确" + date);
							return msg;
						}
					}
				}
			}
		}
		int currentMonth = 0;
		int currentYear = 0;
		if (null != time) {
			currentMonth = time.getMonth() + 1;
			currentYear = time.getYear() + 1900;
		}
		int periodMonth = -1;
		int periodYear = -1;
		GlobalMgt mgt = new GlobalMgt();
		AccPeriodBean accBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		if (accBean != null) {
			periodMonth = accBean.getAccMonth();
			periodYear = accBean.getAccYear();
		}
		if ((currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth)) && currentMonth != 0) {
			if (null != sysParamter) {
				if (sysParamter.equals("CurrentAccBefBill") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
					Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.currentaccbefbill.delete"));
					return msg;
				}

				if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
					Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.can.not.monthly"));
					return msg;
				}
			}
		}

		/* query reation table */
		Result rs = dbmgt.getRelation(tableName,new String[]{ keyId}, map, lg.toString());
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			if (rs.retCode == ErrorCanst.RET_EXISTSRELATION_ERROR) {
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.msg.RET_EXISTSRELATION_ERROR", rs.retVal.toString()));
				return msg;
			} else {
				Message msg = new Message("ERROR", new ErrorMessage().toString(rs.retCode, locale));
				return msg;
			}
		}

		// 启用了分支机构时，如果不是最末级分支机构，不能删除不共享的数据
		String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
		boolean openSunCompany = Boolean.parseBoolean(openValue);
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
		boolean isSharedTable = tableInfo.getIsSunCmpShare() == 1 ? true : false;
		// 当是职员表或部门表时，不作末级分支机构限制
		boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo.getTableName()) || "tblDepartment".equalsIgnoreCase(tableInfo.getTableName()) ? true : false;
		if (openSunCompany && !isLastSunCompany && !isSharedTable && !isEmpOrDept) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.msg.notLastSunCompany"));
			return msg;
		}

		MessageResources resources = getResources();

		String deptCode[] = userMgt.getDeptCodeById(new String[]{keyId});

		Result result = userMgt.getOpenFlagUser(new String[]{keyId});
		List objList = (List) result.retVal;
		String userIds[] = new String[objList.size()];
		for (int i = 0; i < objList.size(); i++) {
			Object[] obj = (Object[]) objList.get(i);
			userIds[i] = (String) obj[0];
		}
		Hashtable props = (Hashtable) BaseEnv.servletContext.getAttribute(BaseEnv.PROP_INFO);

		// 得到要删除单据的信息
		ArrayList manyValues = new ArrayList();
		Result rso = dbmgt.detail(tableName, BaseEnv.tableInfos, keyId, lg.getSunCmpClassCode(), props, lg.getId(), false, "");
		if (rso.retCode == ErrorCanst.DEFAULT_SUCCESS && rso.retVal != null) {
			manyValues.add(rso.getRetVal());
		}

		rs = dbmgt.delete(tableName, map, new String[]{keyId}, lg.getId(),  resources, new Locale(locale), "draft".equals("") ? true : false);

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 记录删除日志信息
			for (int i = 0; i < manyValues.size(); i++) {
				HashMap valuesOld = (HashMap) manyValues.get(i);
				int operation = 2;
				if ("draft".equals(valuesOld.get("workFlowNodeName")))
					operation = 7;
				String billTypeName = tableInfo.getDisplay().get(locale);
				dbmgt.addLog(operation, valuesOld, null, tableInfo, locale, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
			}
			// 同步组织架构到 即时通讯客户端
			if ("tblEmployee".equals(tableName)) {
				if (userIds.length > 0) {
					MSGConnectCenter.deleteObj(userIds, "employee");
				}
			}
			if ("tblDepartment".equals(tableName)) {
				MSGConnectCenter.deleteObj(deptCode, "dept");
			}

			// 如果是单据要调用重算成本
			if (tableInfo.getSysParameter().equals("CurrentAccBefBillAndUnUseBeforeStart") && BaseEnv.systemSet.get("SaveBillAutoRecalc") != null
					&& BaseEnv.systemSet.get("SaveBillAutoRecalc").getSetting().equals("true")) {
				ReCalcucateMgt sysMgt = new ReCalcucateMgt();
				sysMgt.reCalcucateData(lg.getSunCmpClassCode(), accBean.getAccYear(), accBean.getAccMonth(), lg.getId(), "reCalcucate", "", "");
			}

			// 如果启用了工作流，删除我的工作流的单据信息
			if (workFlow != null && workFlow.getTemplateStatus() == 1) {
				new OAMyWorkFlowMgt().deleteOAMyWorkFlow("'" + keyId + "'");
			}

			// 保存删除脚本
			String path = BaseEnv.servletContext.getRealPath("DoSysModule.sql");
			ArrayList delArray = DynDBManager.delArray;
			for (int i = 0; i < delArray.size(); i++) {
				TestRW.saveToSql(path, delArray.get(i).toString());
			}
			DynDBManager.delArray.clear();
			// ///////////////////////
			/* 删除时 自动发送消息 */
			Result smsResult = userFunMgt.querySMSModelInfo(tableName, "delete");
			if (smsResult != null && smsResult.retVal != null) {
				String[] sms = (String[]) smsResult.retVal;
				userFunMgt.autoSendMessage(values, lg, tableInfo, locale, sms, "delete");
			}
			// 删除成功
			Message msg = new Message("OK","删除成功");
			return msg;
			
		} else {
			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				//自定义配置需要用户确认
				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;

			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, "");
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}
		}

	}
	
	/**
	 * 反审核单据，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message retCheck(String locale,LoginBean loginBean,String keyId, String tableName) {

		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(BaseEnv.tableInfos, tableName);

		String workFlowNodeName = "";
		String tempTableName = new DynDBManager().getInsertTableName(tableName);// 处理CRM多模板表名问题
		Result rst = new AIODBManager().sqlList("select workFlowNodeName,checkPersons from " + tempTableName + " where id='" + keyId + "'", new ArrayList());
		if (rst.retVal != null && ((ArrayList) rst.retVal).size() > 0) {
			Object[] obj = ((Object[]) ((ArrayList) rst.retVal).get(0));
			workFlowNodeName = obj[0] == null ? "" : obj[0].toString();
		}
		String checkPersons = BaseEnv.workFlowInfo.get(tableName).getAllowVisitor();
		if (workFlowNodeName.equals("notApprove") || !(checkPersons.contains("," + loginBean.getId() + ",") || checkPersons.contains("," + loginBean.getDepartCode() + ","))) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.msg.hasAudit"));
			return msg;
		}
		/* 判断当前单据是否已经生成凭证 */
		Result rsAcc = dbmgt.hasCreateAcc(tableName, keyId);
		if (rsAcc.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (rsAcc.retVal != null) {// 如果已经生成凭证，则给出错误提示
				String[] str = (String[]) rsAcc.retVal;
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.hasCreateAcc.Oper.error", str[0], str[1]));
				return msg;
			}
		} else {
			Message msg = new Message("ERROR", new ErrorMessage().toString(rsAcc.retCode, locale));
			return msg;
		}
		/** *******************************得到当前要反审核单据的日期，是否是月结前单据***************************** */
		String sysParamter = tableInfo.getSysParameter();// 表信息系统参数
		Date time = null;
		String timeStr = "";
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean bean = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (bean.getDefaultValue() != null && "accendnotstart".equals(bean.getFieldIdentityStr())) {
				AIODBManager aioMgt = new AIODBManager();
				Result rs = aioMgt.sqlList("select " + bean.getFieldName() + ",workFlowNodeName from " + tempTableName + " where id='" + keyId + "'", new ArrayList());
				if (((ArrayList) rs.retVal).size() > 0) {
					timeStr = ((Object[]) ((ArrayList) rs.retVal).get(0))[0].toString();
					workFlowNodeName = ((Object[]) ((ArrayList) rs.retVal).get(0))[1].toString();
				}
			}
		}
		if (timeStr.length() > 0) {
			try {
				time = BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
			}catch (Exception e) {
				BaseEnv.log.error("");
			}
			int currentMonth = 0;
			int currentYear = 0;
			if (null != time) {
				currentMonth = time.getMonth() + 1;
				currentYear = time.getYear() + 1900;
			}
			int periodMonth = -1;
			int periodYear = -1;
			AccPeriodBean accBean = (AccPeriodBean) BaseEnv.accPerios.get(loginBean.getSunCmpClassCode());
			if (accBean != null) {
				periodMonth = accBean.getAccMonth();
				periodYear = accBean.getAccYear();

			}
			if ((currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0) && currentMonth != 0 && !"draft".equals(workFlowNodeName)) {
				if (null != sysParamter) {
					boolean flag = false;
					if (sysParamter.equals("CurrentAccBefBill") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
						flag = true;
					}

					if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)) {
						flag = true;
					}
					if (flag) {
						Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.cantupdatebefbill"));
						return msg;
					}

				}
			}
		}
		/** *******************************得到当前要反审核单据的日期，是否是月结前单据***************************** */

		OAMyWorkFlowMgt oaMgt = new OAMyWorkFlowMgt();
		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(loginBean.getId()));
		table.put("BillOper", "returnAuditing");
		Result rs = oaMgt.execRetCheckDefine(tableName, keyId, loginBean, getResources(), new Locale( locale));

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			Message msg = new Message("OK", GlobalsTool.getMessage(locale, "define.button.reverse.success"));
			return msg;
		} else {
			SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, "");
			Message msg = new Message("ERROR", saveErrrorObj.getMsg());
			return msg;
		}

	}
	/**
	 * 撤回审核，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message cancelTo(String locale,LoginBean loginBean,String keyId, String tableName) {
		Result rs;
		try {
			HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId, tableName);

			OAWorkFlowTemplate template = flowMap == null ? BaseEnv.workFlowInfo.get(tableName) : BaseEnv.workFlowInfo.get(flowMap.get("applyType"));

			// 判断是否具有撤回按钮
			String lastCheckPerson = String.valueOf(flowMap.get("lastCheckPerson"));
			String currentNode = String.valueOf(flowMap.get("currentNode"));
			FlowNodeBean flowNode = BaseEnv.workFlowDesignBeans.get(flowMap.get("applyType")).getFlowNodeMap().get(currentNode);
			if (loginBean.getId().equals(lastCheckPerson) && flowNode != null && flowNode.isAllowCancel()) {
				Result rsDel = oamgt.queryDeleteAdvice(keyId, currentNode);
				if (rsDel.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					String ids = (String) rsDel.getRetVal();
					new AdviceMgt().deleteById(ids);
				}
				String lastNodeId = flowMap.get("lastNodeId")+"";

				rs = oamgt.cancel(keyId, lastNodeId, currentNode, loginBean, flowMap.get("applyType") + "");
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){					
					return new Message("ERROR", "执行失败"+rs.retVal);
				}
				rs = oamgt.updateFlowDepict(flowMap.get("applyType") + "", keyId, new Locale(locale));
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){					
					return new Message("ERROR", "执行失败"+rs.retVal);
				}else{
					return new Message("OK", GlobalsTool.getMessage(locale, "oa.msg.cancelSucc"));
				}
			} else {
				return new Message("ERROR", "当前状态不允许撤回，可能已经审核！");
			}
		} catch (BSFException e) {
			BaseEnv.log.error("MobileAjax.cancelTo Error,", e);
			return new Message("ERROR", "执行错误！");
		}
	}
	
	public static Message cardScan(String key,String secret,String file){
		
		String ret = "";
		String typeId = "20";//名片扫描
		String url = BaseEnv.PicScanURL;
		String format = "json";
			
		//*******上传送图片*******//
		try{
			HttpClient client = new HttpClient(); // 1.创建httpclient对象
			PostMethod post = new PostMethod(url); // 2.通过url创建post方法
			post.setRequestHeader("Accept", "application/json");			
			FilePart fp = new FilePart("file", new File(file)); 
			 
			Part[] parts = {new StringPart("secret", secret),new StringPart("key", key),new StringPart("typeId", typeId),new StringPart("format", format),fp};  
			MultipartRequestEntity mre = new MultipartRequestEntity(parts, post.getParams());  
			post.setRequestEntity(mre);    
			post.getParams().setContentCharset("utf-8");
			client.executeMethod(post);
			ret = post.getResponseBodyAsString();
			return new Message("OK",ret);
		} catch(Exception e){
			BaseEnv.log.error("AIOConnect.cardScan 发送post请求异常：" , e);
			return new Message("ERROR","调用api失败");
		}
		//********end********//
	}
	
	public static Message toURL(String url,String data){
		if(!url.startsWith("http://")){
			url = "http://"+url;
		}
		
		long time = System.currentTimeMillis();		
//		String secData = des.Encode(password+strDefaultKey, "AB:"+json);
//		BaseEnv.log.debug("AIOConnect发送远程请求，url="+url+",time="+System.currentTimeMillis()+",data=："+json);
		PrintWriter out = null;
		DataInputStream in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性 
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
			// 发送请求参数
			out.print("data="+data);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new DataInputStream(conn.getInputStream());
			byte[] bs= new byte[0];
			byte[] b = new byte[1024];
			int count = 0;
			while ((count = in.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length+count];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, count);
			}
			result = new String(bs,"UTF-8");
			BaseEnv.log.debug("AIOConnect发送远程请求返回："+result);
			if(result==null || result.trim().length() ==0){
				Message msg = new Message("ERROR","服务端未返回任何数据");
				return msg;
			}
			//Message msg = gson.fromJson(result, Message.class);
			Message msg = new Message("OK",result);
			return msg;
		} catch (Exception e) {
			BaseEnv.log.error("AIOConnect发送 POST 请求出现异常！" , e);
			return new Message("ERROR",e.getMessage());
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				BaseEnv.log.error("AIOConnect发送 POST  关闭连接出现异常！" , ex);
			}
		}
	}
	
	public static Message toURL(String url,String password,HashMap map){
		if(!url.startsWith("http://")){
			url = "http://"+url;
		}
		if(!url.endsWith("AIOApi")){
			url = (url.endsWith("/")?url:url+"/")+"AIOApi";
		}
		long time = System.currentTimeMillis();
		map.put("time", time+""); 
		String json = gson.toJson(map);
		String secData="AB:"+json;
//		BaseEnv.log.debug("AIOConnect发送远程请求开始压缩，url="+url+",time="+System.currentTimeMillis());
//		json=ZipUtil.zip(json);
//		BaseEnv.log.debug("AIOConnect发送远程请求开始加密，url="+url+",time="+System.currentTimeMillis());
//		String secData = des.Encode(password+strDefaultKey, "AB:"+json);
//		BaseEnv.log.debug("AIOConnect发送远程请求，url="+url+",time="+System.currentTimeMillis()+",data=："+json);
		PrintWriter out = null;
		DataInputStream in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性 
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
			// 发送请求参数
			out.print("secData="+secData);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new DataInputStream(conn.getInputStream());
			byte[] bs= new byte[0];
			byte[] b = new byte[1024];
			int count = 0;
			while ((count = in.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length+count];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, count);
			}
			result = new String(bs,"UTF-8");
			BaseEnv.log.debug("AIOConnect发送远程请求返回："+result);
			if(result==null || result.trim().length() ==0){
				Message msg = new Message("ERROR","服务端未返回任何数据");
				return msg;
			}
			Message msg = gson.fromJson(result, Message.class);
			return msg;
		} catch (Exception e) {
			BaseEnv.log.error("AIOConnect发送 POST 请求出现异常！" , e);
			return new Message("ERROR",e.getMessage());
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				BaseEnv.log.error("AIOConnect发送 POST  关闭连接出现异常！" , ex);
			}
		}
	}
	
	public static Message update(final String url,String password,String userId,String tableName, String saveType,Connection conn,String sql) throws Exception{
		return _update(url,password,  userId, tableName, saveType, conn, sql);
	}
	public static Message update(final String url,String password, String userId,String tableName, String saveType,Connection conn,String sql,String sql1) throws Exception{
		return _update(url,password,  userId, tableName, saveType, conn, sql,sql1);
	}
	public static Message update(final String url,String password, String userId,String tableName, String saveType,Connection conn,String sql,String sql1,String sql2) throws Exception{
		return _update(url,password,  userId, tableName, saveType, conn, sql,sql1,sql2);
	}
	public static Message update(final String url,String password, String userId,String tableName, String saveType,Connection conn,String sql,String sql1,String sql2,String sql3) throws Exception{
		return _update(url,password,  userId, tableName, saveType, conn, sql,sql1,sql2,sql3);
	}
		
	
	/**
	 * 调用远程地址修改表单数据
	 * @param url
	 * @param locale
	 * @param userId
	 * @param tableName
	 * @param saveType
	 * @param defineInfo
	 * @param valuesJson
	 * @return
	 * @throws Exception
	 */
	private static Message _update(final String url,final String password, String userId,String tableName, String saveType,Connection conn,String ...sqls) throws Exception{
		HashMap map = new HashMap();
		map.put("locale", "zh_CN");
		map.put("userId", userId);
		map.put("tableName", tableName);
		map.put("defineInfo", "");
		map.put("saveType", saveType);
		map.put("deliverTo", "");
		map.put("op", "update");
		
		HashMap values = new HashMap();
		for(String sql:sqls){
			String cTable = "";
			if(sql.indexOf(":")>-1){
				//这是子表数据
				cTable = sql.substring(0,sql.indexOf(":"));
				sql = sql.substring(sql.indexOf(":")+1);
			}
			selectData(conn, sql, cTable, values);
		}
		String valuesJson = gson.toJson(values);
		map.put("values", valuesJson);
		Message msg = toURL(url,password, map); 
		if(!"OK".equals(msg.getCode())){
			throw new Exception("远程执行错误,"+msg.getMsg());
		}
		return msg;
	}
	
	private static void selectData(Connection conn,String sql,String cTable,HashMap values) throws Exception{
		//转化语句中的中文逗号
		sql = sql.replaceAll("，", ",");
		BaseEnv.log.debug("AIOConnect.selectData sql="+sql);
		Statement pstmt =  conn.createStatement();
		ResultSet rs =pstmt.executeQuery(sql);
		ArrayList<HashMap> list = new ArrayList();
		while (rs.next()) {
        	HashMap map=new HashMap();
        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
        		String obj=rs.getString(i);
        		if(obj==null){
        			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
        				map.put(rs.getMetaData().getColumnName(i), "0");
        			}else{
        				map.put(rs.getMetaData().getColumnName(i), "");
        			}
        		}else{
        			map.put(rs.getMetaData().getColumnName(i), obj);
        		}
        	}
        	list.add(map);
        }
		if(cTable==null||cTable.length()==0){
			//这是主表
			values.putAll(list.get(0));
		}else{
			//这是明细表
			values.put("TABLENAME_"+cTable, list);
		}
	}
	
	public static Message add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String sql) throws Exception{
		return _add(url,password, userId, tableName, parentCode, saveType, conn, sql);
	}
	public static Message add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String sql,String sql1) throws Exception{
		return _add(url,password, userId, tableName, parentCode, saveType, conn, sql,sql1);
	}
	public static Message add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String sql,String sql1,String sql2) throws Exception{
		return _add(url,password,  userId, tableName, parentCode, saveType, conn, sql,sql1,sql2);
	}
	public static Message add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String sql,String sql1,String sql2,String sql3) throws Exception{
		return _add(url,password,  userId, tableName, parentCode, saveType, conn, sql,sql1,sql2,sql3);
	}
		
	
	/**
	 * 调用远程地址插入表单数据
	 * @param url
	 * @param locale
	 * @param userId
	 * @param tableName
	 * @param parentCode
	 * @param defineInfo
	 * @param saveType
	 * @param valuesJson
	 * @return
	 * @throws Exception
	 */
	private static Message _add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String ...sqls) throws Exception{
		HashMap map = new HashMap();
		map.put("locale", "zh_CN");    
		map.put("userId", userId);
		map.put("tableName", tableName);
		map.put("parentCode", parentCode);
		map.put("defineInfo", "");
		map.put("saveType", saveType);
		
		HashMap values = new HashMap();
		for(String sql:sqls){
			String cTable = "";
			if(sql.indexOf(":")>-1){
				//这是子表数据
				cTable = sql.substring(0,sql.indexOf(":"));
				sql = sql.substring(sql.indexOf(":")+1);
			}
			selectData(conn, sql, cTable, values);
		}
		String valuesJson = gson.toJson(values);
		map.put("values", valuesJson);
		map.put("deliverTo", "");
		map.put("op", "add");
		Message msg = toURL(url,password, map); 
		if(!"OK".equals(msg.getCode())){
			throw new Exception("远程执行错误,"+msg.getMsg());
		}
		return msg;
	}
		
	/**
	 * 调用远程地址执行define动作
	 * @param url
	 * @param locale
	 * @param userId
	 * @param defineName
	 * @param buttonTypeName
	 * @param keyId
	 * @param tableName
	 * @param defineInfo
	 * @return
	 * @throws Exception
	 */
	public static Message  execDefine(final String url,String password, String userId,String defineName, String buttonTypeName,String keyId,String tableName) throws Exception{
		HashMap map = new HashMap();
		map.put("locale", "zh_CN");
		map.put("userId", userId);
		map.put("defineName", defineName);
		map.put("buttonTypeName", buttonTypeName);
		map.put("keyId", keyId);
		map.put("tableName", tableName);
		map.put("defineInfo", "");
		map.put("op", "execDefine");
		Message msg = toURL(url,password, map); 
		if(!"OK".equals(msg.getCode())){
			throw new Exception("远程执行错误,"+msg.getMsg());
		}
		return msg;
	}
		
	
	/**
	 * 直接执行审核到第下个节点的任务，并指定url去执行另一个服务器的审核
	 * 由于在define中调用会产生一个事物，而这里调用的方法复杂，不可能再控制在同一事物中，所以采用新线程的方式来执行，避免事物嵌套错误
	 * 因此define在调用这个方法时要注意执行时机，他是不受事物控制的，不要放在define的开始，应该放在最后。
	 * @param locale
	 * @param userId
	 * @param keyId
	 * @param tableName
	 * @return
	 */
	public static Message deliverToNext(final String url,String password, final String userId,final String keyId,final String tableName)  throws Exception{
		HashMap map = new HashMap();
		map.put("locale", "zh_CN");
		map.put("userId", userId);
		map.put("keyId", keyId);
		map.put("tableName", tableName);
		map.put("op", "deliverToNext");
		Message msg = toURL(url,password, map); 
		if(!"OK".equals(msg.getCode())){
			throw new Exception("远程执行错误,"+msg.getMsg());
		}
		return msg;
	}
	/**
	 * 本方法适用于在define中调用，执行审核到第一个节点的任务，现在很多自动生成的表往往希望直接进入第一个审核节点，
	 * 由于在define中调用会产生一个事物，而这里调用的方法复杂，不可能再控制在同一事物中，所以采用新线程的方式来执行，避免事物嵌套错误
	 * 因此define在调用这个方法时要注意执行时机，他是不受事物控制的，不要放在define的开始，应该放在最后。
	 * @param locale
	 * @param userId
	 * @param keyId
	 * @param tableName
	 * @return
	 */
	public static Message deliverToNext(final String userId,final String keyId,final String tableName) throws Exception{
		final LoginBean loginBean = new LoginBean(userId,"");
		final ArrayList<Message> list = new ArrayList();
		Thread thread = new Thread(){
			public void run(){
				Message msg = deliverToNext("zh_CN", loginBean, keyId, tableName);
				list.add(msg);
			}
		};
		thread.start();
		//等待线程执行结束。
		do{
			try{
			Thread.sleep(500);
			}catch(Exception e){}
		}while(thread.isAlive());
		
		if(!"OK".equals(list.get(0).getCode())){
			throw new Exception("远程执行错误"+list.get(0).getMsg());
		}
		return list.get(0);
	}
	
	
	/**
	 * 审核到第一个节点，现在很多自动生成的表往往希望直接进入第一个审核节点，
	 * 
	 * @param keyId 审核单据ID
	 * @param tableName 审核表名
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message deliverToNext(String locale,LoginBean loginBean,String keyId,String tableName) {
		Message msg = deliverToPrepare(locale, loginBean, keyId, tableName, "");
		if(!msg.getCode().equals("OK")){
			return msg;
		}
		HashMap<String,Object> map = (HashMap<String,Object>)msg.getObj();
		MFlowNodeBean mcurrNB = (MFlowNodeBean)map.get("currNode");
		ArrayList<MFlowNodeBean> mnodeBeans = (ArrayList<MFlowNodeBean>)map.get("nextNodes");
		String designId = map.get("designId")+"";
		String moduleName = map.get("moduleName")+"";
		MFlowNodeBean mnextNB = mnodeBeans.get(0);
		String checkPersion = "";
		for(String[] ss:mnextNB.getCheckPeople()){
			checkPersion +=";"+ss[0];
		}
		if(checkPersion.length() > 0)
			checkPersion = checkPersion.substring(1);
		msg = deliverTo(locale, loginBean, keyId, tableName, mnextNB.getKeyId(), mcurrNB.getKeyId(), designId, 
				checkPersion, "", "", "", "0", "0");
		return msg;
	}
	

	
	/**
	 * 审核单据，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * @param nextStep 下个审批结点
	 * @param currNode 当前审批结点
	 * @param designId 工作流模板ID
	 * @param checkPerson 选择的审批人
	 * @param deliverance 审批意见
	 * @param popedomUserIds 分发提醒人
	 * @param popedomDeptIds 分发提醒部门
	 * @param oaTimeLimit 限时数字 如：10小时、10天
	 * @param oaTimeLimitUnit 限时单位
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message deliverTo(String locale,LoginBean loginBean,String keyId,String tableName,String nextStep,String currNode,String designId,
			String checkPerson,String deliverance,String userIds,String deptIds,String oaTimeLimit,String oaTimeLimitUnit) {
		try {
			
			String type = ""; 

			String msg = GlobalsTool.getMessage(locale, "oa.msg.deliverToSucc");


			OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(designId);

			if (keyId == null && keyId.length() == 0) {
				return new Message("ERROR", "单据编号不能为空！");
			}

			String[] checkPersons = checkPerson.split(";");

			/*设置ERP单据的返回路径*/
			String approveStatus = "transing";

			/*以下各种类型的审批操作 批量转交,撤回，转交，回退*/
			Result rs = new Result();
			if (checkPerson == null || checkPerson.length() == 0 && !"-1".equals(nextStep)) {
				return new Message("ERROR", "审批人不能为空！");
			}
			/**转交，回退*/
			if ((checkPersons == null || checkPersons.length == 0) && !"-1".equals(nextStep)) {
				return new Message("ERROR", "审批人不能为空！");
			}
			String appendWake = deliverance;
			//转交，更新后台下个结点信息
			rs = oamgt.deliverTo(keyId, nextStep, checkPersons, currNode, loginBean, designId,new Locale(locale), deliverance, "", oaTimeLimit, oaTimeLimitUnit, appendWake,
					getResources(), userIds, deptIds, type);

			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				/*更新流程描述信息*/
				rs = oamgt.updateFlowDepict(designId, keyId, new Locale(locale));
			}
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if ("cancel".equals(nextStep)) {//撤回
					return new Message("OK", "撤回成功！");
				}else{
					return new Message("OK", "审批成功！");
				}
			} else {
				SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, locale, "");
				return new Message("ERROR", saveErrrorObj.getMsg());
			}
		} catch (Exception e) {
			return new Message("ERROR", e.getMessage());
		}
	}
	
	/**
	 * 审批前要执行一个校验动作（通过修改保存单据来实现此功能），确保系统的各种校验是否成功，如果在调审核接口前已经执行了修改动作，可以不执行这个方法，否则一定要执行确保数据正确性，
	 * 
	 * @param keyId 单据ID
	 * @param tableName 表名
	 * @param defineInfo 如自定义中有弹出可选确认框，会自动带出一个字定义标识代码，按原文传入。默认为空
	 * <br/><br/>
	 * 返回值: {code:'OK执行成功；CONFIRM弹出窗确认框；ERROR：执行错误',msg：提示信息}
	 */
	public static Message check(String locale,LoginBean loginBean,String keyId, String tableName,String defineInfo) {
		try {
			
			HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId, tableName);
			OAWorkFlowTemplate template = flowMap == null ? BaseEnv.workFlowInfo.get(tableName) : BaseEnv.workFlowInfo.get(flowMap.get("applyType"));
			
			
			final Result billDetail = dbmgt.detail(tableName, BaseEnv.tableInfos, keyId, loginBean.getSunCmpClassCode(),BaseEnv.propMap ,loginBean.getId(),true,"");
			HashMap values = (HashMap) billDetail.getRetVal() ;
			//执行一个update动作
			if(values.get("BillDate")!=null && !values.get("BillDate").equals("")){
				int periodYear=Integer.parseInt(values.get("BillDate").toString().substring(0,4));
				int period=Integer.parseInt(values.get("BillDate").toString().substring(5,7));
				
				Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
				int curPeriodYear=Integer.parseInt(ht.get("NowYear").toString());
				int curPeriod=Integer.parseInt(ht.get("NowPeriod").toString());
				
				if(periodYear<curPeriodYear||(periodYear==curPeriodYear&&period<curPeriod)){
					return new Message("ERROR", GlobalsTool.getMessage(locale,"com.currentaccbefbill"));
				} 
			}
			
			if(template==null || template.getTemplateStatus() == 0){
				values.put("workFlowNodeName", "finish") ;
				values.put("workFlowNode", "-1");
        		values.put("checkPersons", "") ;
        		values.put("finishTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			}else if("draft".equals(values.get("workFlowNodeName"))){
				values.put("workFlowNodeName", "notApprove") ;
				values.put("workFlowNode", "0") ;
            	values.put("checkPersons", ";"+loginBean.getId()+";") ;
            	values.put("finishTime","");
			}
			values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			
			//要执行的define的信息
	        
	        DBTableInfoBean tb = BaseEnv.tableInfos.get(tableName);
	        String parentTableName = tb==null?"":tb.getPerantTableName();
	        String moduleType = values.get("moduleType")==null?"":values.get("moduleType")+"";
	        MOperation mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
			Hashtable props = BaseEnv.propMap;
			Object obj = BaseEnv.servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (obj instanceof MessageResources) {
				resources = (MessageResources) obj;
			}
	        Result updateRs = dbmgt.update(tableName, BaseEnv.tableInfos, values,loginBean.getId(), 
					defineInfo,resources,new Locale(locale),"",loginBean,template,props);	        
	        
	        if (updateRs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				//自定义配置需要用户确认
				ConfirmBean confirm = (ConfirmBean) updateRs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;
			}else  if (updateRs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
				SaveErrorObj saveErrrorObj = dbmgt.saveError(updateRs, locale, "");
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}else{
				Message msg = new Message("OK", "可以修改");
				return msg;
			}
		} catch (Exception e) {
			return new Message("ERROR", e.getMessage());
		}
	}

	private static MessageResources getResources() {

		return (MessageResources) BaseEnv.servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	}


	/**
	 * 输出json结果
	 * @param req
	 * @param rsp
	 * @param code
	 * @param msg
	 */
	private Message setMsg(String code, String msg) {
		return new Message(code, msg);
	}

	private void sendMsg(HttpServletRequest request) {
		String key = "koronaio";
		String signPre = "";

		String userId = request.getParameter("userId");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String receiveIds = request.getParameter("receiveIds");
		String relationId = request.getParameter("relationId");
		String type = request.getParameter("type");
		String timeStamp = request.getParameter("timeStamp");
		signPre += "timeStamp" + timeStamp;
		signPre += key;
		String sign = request.getParameter("sign");
		if (sign.equals(MD5(signPre))) {
			new AdviceMgt().add(userId, title, content, receiveIds, relationId, type);
		}
	}

	private String MD5(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			byte[] b = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte c : b) {
				int val = ((int) c) & 0xff;
				if (val < 16)
					sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}


}
