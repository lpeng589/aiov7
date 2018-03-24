package com.koron.oa.workflow.template;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.koron.oa.bean.ApproveBean;
import com.koron.oa.bean.ConditionBean;
import com.koron.oa.bean.ConditionsBean;
import com.koron.oa.bean.Department;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.publicMsg.advice.OAAdviceMgt;
import com.koron.oa.util.EmployeeMgt;
import com.koron.oa.workflow.ReadXML;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ChineseSpelling;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TestRW;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>Title:工作流模板设计和流程设计</p> 
 * <p>Description: </p>
 *
 * @Date:Oct 12, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class OAWorkFlowTempAction extends MgtBaseAction {

	OAWorkFlowTempMgt mgt = new OAWorkFlowTempMgt();

	PublicMgt flowUtil = new PublicMgt();

	EmployeeMgt empMgt = new EmployeeMgt();

	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-DD hh:mm:ss").create();

	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noback = request.getParameter("noback");//不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		/*是否添加body2head.js*/
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		request.setAttribute("moduleType", request.getParameter("moduleType"));

		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		// 增加前执行方法
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		// 增加方法
		case OperationConst.OP_ADD:
			String type = request.getParameter("type");
			if ("copy".equals(type)) {
				forward = copy(mapping, form, request, response);
			} else if ("addTable".equals(type)) {
				forward = addTable(mapping, form, request, response);
			} else {
				forward = add(mapping, form, request, response);
			}
			break;
		// 删除方法
		case OperationConst.OP_DELETE:
			forward = deleteWokFlow(mapping, form, request, response);
			break;
		// 修改前执行方法
		case OperationConst.OP_UPDATE_PREPARE:
			String isExistTable = request.getParameter("isExistTable");
			String isExistFile = request.getParameter("isExistFile");
			String updateNewPre = request.getParameter("type");
			if ("true".equals(isExistTable)) {
				forward = isExistTable(mapping, form, request, response);
			} else if ("true".equals(isExistFile)) {
				forward = isExistFile(mapping, form, request, response);
			} else if ("saveFlow".equals(updateNewPre)) {
				forward = saveFlow(mapping, form, request, response);
			} else {
				forward = updatePrepare(mapping, form, request, response);
			}
			break;
		// 修改方法
		case OperationConst.OP_DETAIL:
			request.setAttribute("operation", "detail");
			forward = updatePrepare(mapping, form, request, response);
			break;
		// 修改方法
		case OperationConst.OP_UPDATE:
			String updateType = request.getParameter("updateType");
			if ("updateSet".equals(updateType)) {
				//更新工作流设置
				forward = updateFlowSetting(mapping, form, request, response);
			} else {
				forward = update(mapping, form, request, response);
			}
			break;
		// 查询方法
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_EXPORT:
			forward = export(mapping, form, request, response);
			break;
		case OperationConst.OP_IMPORT:
			forward = importFlow(mapping, form, request, response);
			break;
		default:
			String queryType = getParameter("queryType", request);
			String opentype = getParameter("actionType", request);
			String comeType = this.getParameter("comeType", request);
			if ("version".equals(queryType)) {
				forward = queryVersion(mapping, form, request, response);
			} else if ("toTree".equals(opentype)) {
				forward = toTree(mapping, form, request, response);
			} else if ("erp".equals(comeType)) {
				forward = query(mapping, form, request, response);
			} else if ("design".equals(queryType)) {
				forward = designFlow(mapping, form, request, response);
			} else if ("flowfields".equals(queryType)) {
				forward = flowfields(mapping, form, request, response);
			} else if ("flowSet".equals(queryType)) {
				//工作流设置
				forward = queryFlowSetting(mapping, form, request, response);
			} else if ("uploadFile".equals(queryType)) {
				forward = uploadFlowFile(mapping, form, request, response);
			} else {
				forward = frame(mapping, form, request, response);
			}
		}
		return forward;
	}

	private ActionForward updateFlowSetting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String[] number = request.getParameterValues("number");//编号
		String[] types = request.getParameterValues("types");//类型	
		String[] classCode = request.getParameterValues("classCode");//classCode

		ArrayList<String> newValues = new ArrayList<String>();
		if (types != null && types.length > 0) {
			for (int i = 0; i < types.length; i++) {
				if (number[i] != null && !"".equals(number[i]) && types[i] != null && !"".equals(types[i])) {
					newValues.add(number[i] + ";" + types[i] + ";" + classCode[i]);
				}
			} 
			String moduleType = request.getParameter("moduleType");
			Result rs = mgt.updateFlowSetting(newValues, getLoginBean(request).getId());
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				EchoMessage.success().add("保存成功").setBackUrl("/OAWorkFlowTempAction.do?queryType=flowSet&moduleType="+moduleType).setAlertRequest(request);
			} else {
				EchoMessage.error().add("保存失败").setBackUrl("/OAWorkFlowTempAction.do?queryType=flowSet&moduleType="+moduleType).setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "message");
	}

	private ActionForward queryFlowSetting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String sqlList = " where 1 = 1 ";
		sqlList += " and F.isCatalog <> '1' and F.classCode like '00002%' ";
		Result rs = mgt.queryFlowSetting(sqlList);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("flowList", rs.retVal);
		}
		return getForward(request, mapping, "settingFlow");
	}

	/**
	 * 工作流程导入
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward uploadFlowFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		OAWorkFlowDesignForm myForm = (OAWorkFlowDesignForm) form;
		FormFile formFile = myForm.getFile();
		String ip = getParameter("ip", request); // ip地址
		String flowId = getParameter("flowId", request); // 流程ID
		String tableName = getParameter("tableName", request); // 流程表名

		try {
			File tempFile = new File(getUploadWokeFlowPath(formFile.getFileName()));
			FileOutputStream fos = new FileOutputStream(tempFile);
			fos.write(formFile.getFileData());
			fos.flush();
			fos.close();
			String fileText = new String(formFile.getFileData(), "utf-8");
			if (fileText != null && fileText.contains("<FlowJson>")) {
				request.setAttribute("node_json", ReadXML.parseJson(tempFile));
			} else {
				WorkFlowDesignBean designBean = ReadXML.parse(tempFile);
				if (designBean != null) {
					request.setAttribute("node_json", gson.toJson(designBean.getFlowNodeMap()));
				}
			}
			//tempFile.delete();
			OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(flowId);
			// 流程文件
			request.setAttribute("flowType", template.getDesignType());
			request.setAttribute("tableInfo", GlobalsTool.getTableInfoBean(tableName));
			request.setAttribute("ip", ip);
			request.setAttribute("tableName", tableName);
			request.setAttribute("flowDisplay", template.getTemplateName());
			request.setAttribute("newCreate", false);
			request.setAttribute("importFile", true);
			request.setAttribute("flowId", flowId);
			request.setAttribute("workFileName", tempFile.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getForward(request, mapping, "designFlow");
	}

	private String getUploadWokeFlowPath(String fileName) {
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		String dir = path += "wokeflow/upload/";
		File file = new File(dir);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
			}
		}
		path = dir + fileName;
		return path;
	}

	/**
	 * 加载左侧页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward toTree(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ERP的流程设计查询
		String mapParam = mapping.getParameter();
		if (mapParam.equals("/ERPWorkFlowTempSearchAction.do")) {
			return this.queryErp(mapping, form, request, response);
		}

		// OA的流程设计查询
		OAWorkFlowTempSearchForm searchForm = (OAWorkFlowTempSearchForm) form;
		String showType = this.getParameter("showType", request);
		if ((showType == null || "".equals(showType)) && mapParam.equals("/OAWorkFlowCreateAction.do")) {
			showType = "frame";
		}
		if (searchForm != null) {
			// 查询类型
			Result typers = mgt.queryType("all");
			if (typers.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList<String[]> list = (ArrayList<String[]>) typers.retVal;
				String folderTree = "";
				for (int i = 0; i < list.size(); i++) {
					String[] obj = (String[]) list.get(i);
					if (obj[1].length() == 5) {
						folderTree += this.folderTree(list, obj[2], obj[1], obj[3], obj[0]);
					}
				}
				request.setAttribute("result", folderTree);
			}
		}
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		return getForward(request, mapping, "totree");
	}

	/**
	 * 加载组所在的树
	 * @param list
	 * @param folderName
	 * @param classCode
	 * @param isCatalog
	 * @param folderId
	 * @return
	 */
	private String folderTree(ArrayList list, String folderName, String classCode, String isCatalog, String folderId) {
		String folderTree = "";
		if (isCatalog != null && isCatalog.equals("1")) {
			folderTree = "<li><span><a style=\"color:#000000\"  href=\"javascript:void(0);\" onclick=\"javascript:clearCommet();goAction('class','" + classCode + "," + folderName + "');\" >"
					+ folderName + "</a></span><ul>";

			for (int i = 0; i < list.size(); i++) {
				String[] obj = (String[]) list.get(i);
				if (obj[1].length() > classCode.length() && obj[1].indexOf(classCode) != -1) {
					folderTree += this.folderTree(list, obj[2], obj[1], obj[3], obj[0]);
				}
				/*if(obj[1].substring(0,obj[1].length()-5).equals(classCode+"0")){
				 folderTree+=this.folderTree(list,obj[2], obj[1],obj[3],obj[0]);
				 }*/
			}
			folderTree += "</ul></li>";

		} else {
			folderTree = "<li><span><a style=\"color:#000000\" href=\"javascript:void(0);\" onclick=\"javascript:clearCommet();goAction('class','" + classCode + "," + folderName + "');\" >"
					+ folderName + "</a></span></li>";
		}
		return folderTree;
	}

	/**
	 * 判断物理表结构是否存在
	 */
	protected ActionForward isExistTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tableName = request.getParameter("tableName");
		DBTableInfoBean tableBean = GlobalsTool.getTableInfoBean(tableName);
		String flag = "false";
		if (tableBean != null) {
			flag = "true";
		}
		request.setAttribute("msg", flag);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 判断流程设计文件是否存在
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward isExistFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String fileName = request.getParameter("fileName");
		WorkFlowDesignBean workflowFile = BaseEnv.workFlowDesignBeans.get(fileName);
		String flag = "false";
		if (workflowFile != null) {
			flag = "true";
		}
		request.setAttribute("msg", flag);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 加载框架
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward frame(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		return getForward(request, mapping, "toframe");
	}

	/**
	 * 流程设计
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward designFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String ip = getParameter("ip", request); //ip地址
		String keyId = getParameter("keyId", request); //流程ID
		String tableName = getParameter("tableName", request); //流程表名
		String newCreate = getParameter("newCreate", request); //是否新建

		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(keyId);
		//流程文件
		WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(keyId);
		if (designBean != null) {
			

			request.setAttribute("node_json", gson.toJson(designBean.getFlowNodeMap()));
			newCreate = "false";
		}

		request.setAttribute("flowType", template.getDesignType());
		request.setAttribute("tableInfo", GlobalsTool.getTableInfoBean(tableName));
		request.setAttribute("ip", ip);
		request.setAttribute("tableName", tableName);
		request.setAttribute("flowDisplay", BaseEnv.workFlowInfo.get(keyId).getTemplateName());
		request.setAttribute("newCreate", newCreate);
		request.setAttribute("flowId", keyId);
		request.setAttribute("workFileName", keyId + ".xml");
		return getForward(request, mapping, "designFlow");
	}

	/**
	 * 添加工作流表单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		OAWorkFlowDesignForm oaFlowForm = new OAWorkFlowDesignForm();

		String templateName = request.getParameter("templateFile");
		String fromTemplate = request.getParameter("fromTemplate");
		oaFlowForm.setTemplateName(templateName);
		String designType = request.getParameter("designType"); // 1,个性化设计,2:自定义设计， 新开发的工作流，只支持自定义设计，加个性化html，所以新添加的都为2.
		//designType = "2";
		String tempClass = request.getParameter("tempClass");
		String Id = IDGenerater.getId();
		oaFlowForm.setId(Id); 
		String flowStartName = "Flow_";
		if ("2".equals(designType)) { //自定义表单名称开头
			flowStartName = "FlowN";
		}
		templateName = templateName.trim().replace(" ", "").replace(".", ""); //将流程中的空格和.替换成下划线
		String templateFile = "";
		int templateType = 1;
		if(fromTemplate != null && fromTemplate != ""){
			templateFile = ChineseSpelling.getSelling(fromTemplate);
			templateType = 2;
		}else {
			templateFile = flowStartName + ChineseSpelling.getSelling(templateName);	
			//判断新创建的工作流英文名称是否已存在
			String num = mgt.isExistWorkFlow(templateFile);
			if (Integer.parseInt(num) > 0) { //如果已经存在相同的表机构，则更改表结构名称
				int temp = Integer.parseInt(num) + 1;
				templateFile += temp;
			}
		}

		
		
		
		if (tempClass.indexOf("00002") == -1) {
			designType = "2";
		}
		
		
		
		oaFlowForm.setDesignType(designType);
		oaFlowForm.setTemplateFile(templateFile);
		oaFlowForm.setTemplateStatus(1);
		oaFlowForm.setTemplateClass(tempClass);
		oaFlowForm.setStatusId("0");
		oaFlowForm.setTemplateType(templateType);
		oaFlowForm.setNextWake(new String[] { "4" });
		oaFlowForm.setOverTimeWake(new String[] { "4" });
		oaFlowForm.setStopStartWake(new String[] { "4" });
		oaFlowForm.setCreateTime(BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss));
		String allowVisitor = "";
		Result rs = mgt.add(Id, oaFlowForm, allowVisitor, "");
		String msg = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			msg = oaFlowForm.getId();
		} else {
			msg = "failure";
		}
		request.setAttribute("msg", msg);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 工作流 模板导入
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward importFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		OAWorkFlowDesignForm myForm = (OAWorkFlowDesignForm) form;
		try {
			// String savePath = BaseEnv.FILESERVERPATH + "/workflowtemplete/"
			// +myForm.getFileName();
			String savePath = "D:\\AIOBillExport\\flow.zip";
			File file = new File(savePath);
			// file.getParentFile().mkdirs();
			// FileOutputStream fos = new FileOutputStream(savePath);
			// fos.write(myForm.getFile().getFileData());
			// fos.close();
			ZipFile zipFile = new ZipFile(file);
			Enumeration enumZip = zipFile.getEntries();
			String flowSQL = "";
			String tableSQL = "";
			while (enumZip.hasMoreElements()) {
				ZipEntry zip = (ZipEntry) enumZip.nextElement();
				if (!zip.isDirectory()) {
					FileOutputStream fosFlow = null;
					if (!"flow.sql".equals(zip.getName()) && !"table.sql".equals(zip.getName())) {
						String path = BaseEnv.FILESERVERPATH + "/wokeflow/" + zip.getName();
						fosFlow = new FileOutputStream(new File(path));
					}
					InputStream input = zipFile.getInputStream(zip);
					BufferedReader br = new BufferedReader(new InputStreamReader(input));
					String content = "";
					while ((content = br.readLine()) != null) {
						if ("flow.sql".equals(zip.getName())) {
							flowSQL += content;
						} else if ("table.sql".equals(zip.getName())) {
							tableSQL += content + " \n";
						} else {
							fosFlow.write(content.getBytes());
						}
					}
					input.close();
					if (!"flow.sql".equals(zip.getName()) && !"table.sql".equals(zip.getName())) {
						fosFlow.close();
					}
				}
			}
			mgt.executeSQL(tableSQL);
			mgt.executeSQL(flowSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		EchoMessage.success().add("导入成功").setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * 工作流 模板导出
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String keyId = getParameter("keyId", request);

		String strSql = mgt.getWorkFlowDesingById(keyId);
		String[] strArray = strSql.split("@koron@");
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, strArray[1]);
		/* 压缩Excel文件 */
		String accName = GlobalsTool.getSysSetting("DefBackPath");
		File file = new File(accName + "\\AIOBillExport");
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileName = file.getAbsolutePath() + "\\flow.zip";
		/* 如果存在，先删除压缩文件 */
		File zipFile = new File(fileName);
		if (zipFile.exists()) {
			zipFile.delete();
		}
		String filePath = file.getAbsolutePath();
		FileOutputStream fileOutputStream = new FileOutputStream(filePath + "//flow.zip");
		ZipOutputStream out = new ZipOutputStream(fileOutputStream);
		/* 导出SQL语句 */
		if (tableInfo != null) {
			// 设置为用户表，系统表不可能在这定义
			tableInfo.setUdType(DBTableInfoBean.USER_TYPE);
			final Result rs = new Result();
			String ddl = " create table " + tableInfo.getTableName() + " (id varchar(30) not null ";

			// 若为主表，整理数据,防止部分人设置错误。
			if (tableInfo.getTableType() == DBTableInfoBean.MAIN_TABLE) {
				tableInfo.setPerantTableName(null);
			}

			ArrayList defConstraints = new ArrayList();
			for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
				DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
				String fieldName = fieldInfo.getFieldName();

				// 取字段类型和，默认值
				String fieldType = DDLOperation.getTypeStr(fieldInfo);
				String fieldefault = null;
				try {
					fieldefault = DDLOperation.getDefaultStr(fieldInfo);
				} catch (Exception ex) {
					rs.setRetCode(ErrorCanst.RET_DEFAULT_TYPE_ERROR);
				}

				String fieldNull = fieldInfo.getIsNull() == DBFieldInfoBean.IS_NULL ? "null" : "not null";
				if (fieldName.equals("detOrderNo")) {
					ddl += "," + fieldName + " " + fieldType + " NOT NULL IDENTITY (1, 1) ";
				} else {
					ddl += "," + fieldName + " " + fieldType + " " + fieldNull;
				}
				if (fieldefault != null && fieldefault.trim().length() > 0) {
					defConstraints.add("ALTER TABLE " + tableInfo.getTableName() + " ADD  CONSTRAINT DF_" + tableInfo.getTableName() + "_" + fieldName + " default(" + fieldefault + ") FOR "
							+ fieldName);
				}
			}
			ddl += ", primary key (id))";

			String contranst = "";
			// 对于子表，必须建立外键关系。
			if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE) {
				String parentTableName = tableInfo.getPerantTableName();
				// 生成约束名称
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
				String[] parentTableNames = parentTableName.split(";");
				if (parentTableNames.length == 1) {
					for (int i = 0; i < parentTableNames.length; i++) {
						String fk = "FK_f_ref_id" + sdf.format(new Date());
						contranst = "alter table " + tableInfo.getTableName() + " add constraint " + fk + " foreign key (f_ref) references " + parentTableNames[i]
								+ " ON DELETE CASCADE ON UPDATE CASCADE ";
					}
				}
			} else if (tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE) {
				String parentTableName = tableInfo.getPerantTableName();
				// 生成约束名称
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
				String[] parentTableNames = parentTableName.split(";");
				if (parentTableNames.length == 1) {
					for (int i = 0; i < parentTableNames.length; i++) {
						String fk = "FK_f_ref_id" + sdf.format(new Date());
						contranst = "alter table " + tableInfo.getTableName() + " add constraint " + fk + " foreign key (f_brother) references " + parentTableNames[i]
								+ " ON DELETE CASCADE ON UPDATE CASCADE ";
					}
				}
			}

			// 保存sql文本
			String sql = "if exists(select * from sysobjects where name='" + tableInfo.getTableName() + "')\nbegin\n"
					+ "delete from tblLanguage where id in (select f.languageId from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '" + tableInfo.getTableName() + "') \n"
					+ "delete from tblLanguage where id in (select f.groupName from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '" + tableInfo.getTableName() + "') \n"
					+ "delete from tblLanguage where id in (select t.languageId from tblDBTableInfo t where t.tableName = '" + tableInfo.getTableName() + "') \n"
					+ "exec proc_deleteExistsTable @tableName='" + tableInfo.getTableName() + "'\ndrop table " + tableInfo.getTableName() + "\nend\n";
			String lastSql = sql + ddl + "\n" + contranst + "\n";
			for (int j = 0; j < defConstraints.size(); j++) {
				lastSql += defConstraints.get(j) + "\n";
			}
			String path = request.getSession().getServletContext().getRealPath("table.sql");
			TestRW.saveToSql(path, lastSql);
			ConvertToSql.saveSqlByTableInfo(tableInfo, path);
			ConvertToSql.saveSqlByTableDisplay(tableInfo, path);
			ConvertToSql.saveSqlByDBField(tableInfo, path);
			ConvertToSql.saveSqlByFieldDisplay(tableInfo, path);
			compressFile(new File(path), out);
			new File(path).delete();
		}
		/* 模板SQL */
		String flowSql = request.getSession().getServletContext().getRealPath("flow.sql");
		TestRW.saveToSql(flowSql, strArray[0]);
		compressFile(new File(flowSql), out);
		new File(flowSql).delete();
		/* 模板文件 */
		String flowFile = mgt.getWokeFlowPath(strArray[2]);
		compressFile(new File(flowFile), out);

		out.close();
		fileOutputStream.close();
		EchoMessage.success().add(
				GlobalsTool.getMessage(request, "excel.export.success") + "," + "<a href='/ReadFile?tempFile=export&fileName=" + GlobalsTool.encode("flow.zip") + "'>"
						+ GlobalsTool.getMessage(request, "com.export.success.download") + "</a>" + GlobalsTool.getMessage(request, "com.export.success.over") + fileName).setNotAutoBack()
				.setAlertRequest(request);

		return getForward(request, mapping, "alert");
	}

	/** 压缩一个文件 */
	private void compressFile(File file, ZipOutputStream out) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
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

	/**
	 * 添加前的准备
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.removeAttribute("operation");

		String selectClass = request.getParameter("selectClass");
		if (!"".equals(selectClass) && selectClass != null) {
			selectClass = GlobalsTool.toChinseChar(selectClass);
			if ("1".equals(selectClass)) {
				selectClass = "00002";
			}
		}
		Result typersByCata = mgt.queryType("ByCatalog");
		Result fromMapRst = mgt.queryFromTemp();
		if (typersByCata.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("typeListbycata", typersByCata.getRetVal());
		request.setAttribute("selectClass", selectClass);	
		request.setAttribute("fromTempMap", fromMapRst.getRetVal());
		return getForward(request, mapping, "addTable");
	}

	/**
	 * 修改
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward copy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String keyId = getParameter("keyId", request);
		String newKeyId = IDGenerater.getId();
		String winCurIndex = getParameter("winCurIndex", request);
		//Result result = new Result();
		Result result = mgt.copyFlowTemplate(keyId, newKeyId + ".xml", getLoginBean(request).getId());
		String msg;
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//如果是个性化工作流生成的表单，则需要向OAWorkFlowTableVersion中插入一条数据
//			String backUrl = "";
//			String moduleType = request.getParameter("moduleType");
//			if ("erp".equals(getParameter("flowType", request))) {
//				backUrl = "/ERPWorkFlowTempAction.do?operation=7&keyId=" + newKeyId + "&winCurIndex=" + winCurIndex;
//			} else {
//				backUrl = "/OAWorkFlowTempAction.do?operation=7&winCurIndex=" + winCurIndex + "&keyId=" + newKeyId+"&moduleType="+moduleType;
//			}
//			EchoMessage.success().add(getMessage(request, "com.add.new.flow")).setBackUrl(backUrl).setAlertRequest(request);
			msg = newKeyId;
		}else{
			msg = "ERROR:执行错误"+result.retVal;
		}
		request.setAttribute("msg", msg);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 修改
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String type = this.getParameter("selectType", request);
		String val = this.getParameter("val", request);
		
		String fromPage = this.getParameter("fromPage", request);
		request.setAttribute("fromPage", fromPage); 

		ActionForward forward = getForward(request, mapping, "alert");
		OAWorkFlowDesignForm myForm = (OAWorkFlowDesignForm) form;
		String moduleType= request.getParameter("moduleType");
		myForm.setTemplateName(myForm.getTemplateName());
		String userId = this.getLoginBean(request).getId();
		myForm.setUpdateBy(userId);
		if (myForm.getTemplateStatus() == 0) {
			String tableName = mgt.selectExistBill(new String[] { myForm.getId() }, this.getLocale(request).toString(), "noFinish");
			if (tableName.length() > 0) {
				
				EchoMessage.info().add(getMessage(request, "com.not.approved.flow", tableName)).setBackUrl("/OAWorkFlowTempAction.do?operation=7&keyId="+myForm.getId()+"&winCurIndex=&val=1&selectType=" + type + "&val="+val+"&moduleType="+request.getParameter("moduleType")+"&fromPage="+fromPage)
						.setAlertRequest(request);
				return forward;
			}
		}

		// 执行添加操作
		String popedomUserId = getParameter("popedomUserIds", request); // 获得通知对象
		String popedomDeptId = getParameter("popedomDeptIds", request); // 获得能知部门
		String empGroupId = getParameter("EmpGroupId", request); // 获得通知 职员分组
		String empTitleIds = getParameter("empTitleIds", request);
		String[] wakeType = getParameters("wakeUpMode", request);

		// 提醒方式
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}
		popedomDeptId = "," + popedomDeptId;
		popedomUserId = "," + popedomUserId;
		empGroupId = "," + empGroupId;
		String allowVisitor = popedomDeptId + "|" + popedomUserId + "|" + empGroupId + "|," + empTitleIds;

		if("0".equals(request.getParameter("userType"))){//这是目标用户为所有
			allowVisitor = "";
		}
		if (request.getParameter("autoPass") == null) {
			myForm.setAutoPass("false");
		}

		
		Result rs = mgt.update(myForm, allowVisitor, wakeupType);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			/* 从新加载工作流模板到内存中 */
			new InitMenData().initWorkFlowInfo(request.getSession().getServletContext());
			//修改成功
			String mapParam = mapping.getParameter();
			if (mapParam.equals("/ERPWorkFlowTempSearchAction.do?comeType=erp")) {
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl("/ERPWorkFlowTempSearchAction.do?comeType=erp&winCurIndex=" + request.getParameter("winCurIndex")).setAlertRequest(request);
			} else {
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/OAWorkFlowTempAction.do?operation=7&keyId="+myForm.getId()+"&winCurIndex=&val=1&selectType=" + type + "&val="+val+"&moduleType="+request.getParameter("moduleType")+"&fromPage="+fromPage).setAlertRequest(
						request);
			}
		} else {
			//修改失败
			EchoMessage.error().add(getMessage(request, "common.msg.updateFailture")).setAlertRequest(request);
		}
		return forward;
	}

	/**
	 * 添加流程成功，跳转到编辑页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updateNewPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String keyId = request.getParameter("keyId");
		Result rs = mgt.loadNew(keyId);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS || ((List) rs.getRetVal()).size() < 1) {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		} else {

			Object[] objList = ((Object[]) ((List) rs.getRetVal()).get(0));

			String tableName = objList[4].toString();
			Object o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

			//判断表结构是否存在
			DBTableInfoBean tblInfo = DDLOperation.getTableInfo((Hashtable) o, tableName);
			String flag = "";
			if (tblInfo == null) {
				flag = "false";
			} else {
				flag = "true";
			}
			String host = request.getHeader("Host");
			if (host.indexOf(":") > 0) {
				host = host.replace(':', '|');
			} else {
				host = host + "|80";
			}

			String targetUserStr = ",1,";

			// 设置工作流控件需要的主机地址（URL）
			List<Employee> targetUsers = new ArrayList<Employee>();
			Employee employee = (Employee) empMgt.queryEmployeeByUserId("1").retVal;
			if (null != employee) {
				targetUsers.add(employee);
			}
			request.setAttribute("targetUsers", targetUsers);
			request.setAttribute("host", host);
			request.setAttribute("flag", flag);
			request.setAttribute("targetUserStr", targetUserStr);
			request.setAttribute("type", objList[2].toString());
			request.setAttribute("result", objList);
			return getForward(request, mapping, "update");
		}
	}

	/**
	 * 修改前的准备
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Object operation = request.getAttribute("operation");
		request.removeAttribute("operation");
		String selectType = this.getParameter("selectType", request);
		String val = this.getParameter("val", request);
		request.setAttribute("selectType", selectType);
		request.setAttribute("val", GlobalsTool.toChinseChar(val));
		
		String fromPage = this.getParameter("fromPage", request);
		request.setAttribute("fromPage", fromPage); 

		// 打开添加工作流界面
		String keyId = request.getParameter("keyId");
		Result rs = mgt.load(keyId);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS || ((List) rs.getRetVal()).size() < 1) {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		Object[] workObject = (Object[]) ((List) rs.getRetVal()).get(0);

		if (workObject[17] != null) {// 如果有设置工作流的标题模板
			request.setAttribute("titleTempDisplay", workObject[17].toString());
		}
		if (workObject[40] != null) {
			DBFieldInfoBean dbField = GlobalsTool.getFieldBean((String) workObject[4], workObject[40].toString());
			if (dbField != null) {
				request.setAttribute("fileContentDisplay", dbField.getDisplay().get(getLocale(request).toString()));
			}
		}
		request.setAttribute("result", workObject);
		request.setAttribute("type", workObject[2]);

		// 设置工作流控件需要的工作流文件名称
		request.setAttribute("workFileName", workObject[5]);
		String host = request.getHeader("Host");
		if (host.indexOf(":") > 0) {
			host = host.replace(':', '|');
		} else {
			host = host + "|80";
		}

		// 设置工作流控件需要的主机地址（URL）
		request.setAttribute("host", host);
		String allowVisitor = (String) workObject[6] + ","; // 避免最后位为空
		String allows[] = allowVisitor.split("\\|");

		OAAdviceMgt adviceMgt = new OAAdviceMgt();
		EmployeeMgt empMgt = new EmployeeMgt();
		List<Department> monDeptDis = new ArrayList<Department>();
		List<Employee> monUserDis = new ArrayList<Employee>();
		request.setAttribute("monDeptDis", monDeptDis);
		request.setAttribute("monUserDis", monUserDis);

		Result typersByCata = mgt.queryType("ByCatalog");
		if (typersByCata.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("typeListbycata", typersByCata.getRetVal());

		if (workObject[15] != null && !"".equals(workObject[15])) {
			String[] deptIds = workObject[15].toString().split(",");
			for (String classCode : deptIds) {
				if (classCode != null && !classCode.equals("")) {
					Department dept = adviceMgt.getDepartmentByClassCode(classCode);
					if (null != dept) {
						monDeptDis.add(dept);
					}
				}
			}
		}

		if (workObject[16] != null && !"".equals(workObject[16])) {
			String[] userIds = workObject[16].toString().split(",");
			for (String emp : userIds) {
				if (emp != null && !emp.equals("")) {
					Employee employee = (Employee) empMgt.queryEmployeeByUserId(emp).retVal;
					if (null != employee) {
						monUserDis.add(employee);
					}
				}
			}
		}

		// 查询目标部门
		List<Department> targetDept = new ArrayList<Department>();
		if (allows[0] != null && !"".equals(allows[0])) {
			String[] popedomDeptIds = allows[0].split(",");
			for (String classCode : popedomDeptIds) {
				if (classCode != null && !classCode.equals("")) {
					Department dept = adviceMgt.getDepartmentByClassCode(classCode);
					if (null != dept) {
						targetDept.add(dept);
					}
				}
			}
		}
		List<Employee> targetUsers = new ArrayList<Employee>();
		if (allows.length > 1 && allows[1] != null && !"".equals(allows[1])) {
			String[] accepterId = allows[1].split(",");
			for (String targetUser : accepterId) { // 根据用户ID循环查询用户信息
				if (targetUser != null && !targetUser.equals("")) {
					Employee employee = (Employee) empMgt.queryEmployeeByUserId(targetUser).retVal;
					if (null != employee) {
						targetUsers.add(employee);
					}
				}
			}
		}
		// 查找职员分组
		// 根据ID获得通知信息
		List<String[]> listEmpGroup = new ArrayList<String[]>();
		if (allows.length > 1 && allows[2] != null && !"".equals(allows[2])) {
			String[] popedomEmpGroupIds = allows[2].split(",");
			for (String empGroup : popedomEmpGroupIds) {
				if (empGroup != null && !empGroup.equals("")) {
					Result r = empMgt.selectEmpGroupById(empGroup);
					if (r.getRetVal() != null) {
						listEmpGroup.add((String[]) r.getRetVal());
					}
				}
			}
		}
		request.setAttribute("empTitleIds", allows.length > 3 ? allows[3] : "");

		// 指定人员提醒范围
		List<Department> setWakeDept = new ArrayList<Department>();
		if (workObject[22] != null && !"".equals(workObject[22])) {
			String[] popedomDeptIds = workObject[22].toString().split(",");
			for (String classCode : popedomDeptIds) {
				if (classCode != null && !classCode.equals("")) {
					Department dept = adviceMgt.getDepartmentByClassCode(classCode);
					if (null != dept) {
						setWakeDept.add(dept);
					}
				}
			}
		}
		List<Employee> setWakePer = new ArrayList<Employee>();
		if (workObject[23] != null && !"".equals(workObject[23])) {
			String[] accepterId = workObject[23].toString().split(",");
			for (String targetUser : accepterId) { // 根据用户ID循环查询用户信息
				if (targetUser != null && !targetUser.equals("")) {
					Employee employee = (Employee) empMgt.queryEmployeeByUserId(targetUser).retVal;
					if (null != employee) {
						setWakePer.add(employee);
					}
				}
			}
		}
		// 查找职员分组
		// 根据ID获得通知信息
		List<String[]> setWakeGroup = new ArrayList<String[]>();
		if (workObject[24] != null && !"".equals(workObject[24])) {
			String[] popedomEmpGroupIds = workObject[24].toString().split(",");
			for (String empGroup : popedomEmpGroupIds) {
				if (empGroup != null && !empGroup.equals("")) {
					Result r = empMgt.selectEmpGroupById(empGroup);
					if (r.getRetVal() != null) {
						setWakeGroup.add((String[]) r.getRetVal());
					}
				}
			}
		}

		// 结束时指定人员提醒范围
		List<Department> stopSetWakeDept = new ArrayList<Department>();
		if (workObject[28] != null && !"".equals(workObject[28])) {
			String[] popedomDeptIds = workObject[28].toString().split(",");
			for (String classCode : popedomDeptIds) {
				if (classCode != null && !classCode.equals("")) {
					Department dept = adviceMgt.getDepartmentByClassCode(classCode);
					if (null != dept) {
						stopSetWakeDept.add(dept);
					}
				}
			}
		}
		List<Employee> stopSetWakePer = new ArrayList<Employee>();
		if (workObject[29] != null && !"".equals(workObject[29])) {
			String[] accepterId = workObject[29].toString().split(",");
			for (String targetUser : accepterId) { // 根据用户ID循环查询用户信息
				if (targetUser != null && !targetUser.equals("")) {
					Employee employee = (Employee) empMgt.queryEmployeeByUserId(targetUser).retVal;
					if (null != employee) {
						stopSetWakePer.add(employee);
					}
				}
			}
		}
		// 查找职员分组
		// 根据ID获得通知信息
		List<String[]> stopSetWakeGroup = new ArrayList<String[]>();
		if (workObject[30] != null && !"".equals(workObject[30])) {
			String[] popedomEmpGroupIds = workObject[30].toString().split(",");
			for (String empGroup : popedomEmpGroupIds) {
				if (empGroup != null && !empGroup.equals("")) {
					Result r = empMgt.selectEmpGroupById(empGroup);
					if (r.getRetVal() != null) {
						stopSetWakeGroup.add((String[]) r.getRetVal());
					}
				}
			}
		}

		String tableName = BaseEnv.workFlowInfo.get(keyId).getTemplateFile();
		Object o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

		//判断表结构是否存在
		DBTableInfoBean tblInfo = DDLOperation.getTableInfo((Hashtable) o, tableName);
		String flag = "";
		if (tblInfo == null) {
			flag = "false";
		} else {
			flag = "true";
		}
		request.setAttribute("flag", flag);
		request.setAttribute("setWakeDept", setWakeDept);
		request.setAttribute("setWakePer", setWakePer);
		request.setAttribute("setWakeGroup", setWakeGroup);
		request.setAttribute("stopSetWakeDept", stopSetWakeDept);
		request.setAttribute("stopSetWakePer", stopSetWakePer);
		request.setAttribute("stopSetWakeGroup", stopSetWakeGroup);
		if (allows.length > 2) {
			request.setAttribute("targetUserStr", allows[1]);
			request.setAttribute("targetDeptStr", allows[0]);
			request.setAttribute("targetEmpGroupStr", allows[2]);
		}

		request.setAttribute("targetUsers", targetUsers);
		request.setAttribute("targetDept", targetDept);
		request.setAttribute("targetEmpGroup", listEmpGroup);
		if (workObject[46] != null && workObject[46].equals("1")) {
			request.getSession().setAttribute("newCreate", "false");
		} else {
			request.getSession().setAttribute("newCreate", "true");
		}
		request.setAttribute("tableName", BaseEnv.workFlowInfo.get(keyId).getTemplateFile());
		if (operation != null && operation.equals("detail")) {
			request.setAttribute("ip", host.replace("|", ":"));
			request.setAttribute("fileName", BaseEnv.workFlowInfo.get(keyId).getWorkFlowFile());
			return getForward(request, mapping, "detail");
		} else {
			return getForward(request, mapping, "update");
		}
	}

	/**
	 * 添加
	 * 
	 * @param mapping  ActionMapping
	 * @param form  ActionForm
	 * @param request HttpServletRequest
	 * @param response  HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = getForward(request, mapping, "alert");
		OAWorkFlowDesignForm myForm = (OAWorkFlowDesignForm) form;
		myForm.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"), (Locale) request.getSession().getAttribute(
				org.apache.struts.Globals.LOCALE_KEY), myForm.getTemplateName()));
		myForm.setTemplateName(myForm.getDisplay().getId());

		//创建时间 
		String createTime = BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss);
		myForm.setCreateTime(createTime);

		// 提醒方式
		String[] wakeType = getParameters("wakeUpMode", request);

		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}
		// 整理目标用户
		String popedomUserId = request.getParameter("popedomUserIds"); // 获得通知对象
		String popedomDeptId = request.getParameter("popedomDeptIds"); // 获得能知部门
		String empGroupId = request.getParameter("EmpGroupId"); // 获得通知 职员分组

		popedomDeptId = "," + popedomDeptId;
		popedomUserId = "," + popedomUserId;
		empGroupId = "," + empGroupId;
		String allowVisitor = popedomDeptId + "|" + popedomUserId + "|" + empGroupId;
		String id = IDGenerater.getId();
		myForm.setTemplateName(request.getParameter("flowName"));
		myForm.setDesignType(request.getParameter("designType"));
		Result rs = mgt.add(id, myForm, allowVisitor, wakeupType);

		if (request.getParameterValues("alertType") != null && request.getParameterValues("alertType").length > 0) {
			UserFunctionMgt userMgt = new UserFunctionMgt();
			userMgt.setAlert(request, id, this.getLoginBean(request), DDLOperation.getTableInfo(BaseEnv.tableInfos, "OAWorkFlowTemplate"));
		}
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 从新加载工作流模板到内存中 
			new InitMenData().initWorkFlowInfo(request.getSession().getServletContext());
			// 添加成功
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/OAWorkFlowTempAction.do?operation=7&keyId=" + id + "&winCurIndex=" + request.getParameter("winCurIndex")+"&moduleType="+request.getParameter("moduleType")).setAlertRequest(request);
		} else {
			// 添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return forward;

	}

	/**
	 * 查询版本
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String tableName = getParameter("tableName", request);
		String host = request.getHeader("Host");
		String versionType = request.getParameter("versionType");
		String designId = request.getParameter("designId");
		if (host.indexOf(":") > 0) {
			host = host.replace(':', '|');
		} else {
			host = host + "|80";
		}

		Result result = null;
		if ("flow".equals(versionType)) {
			result = mgt.queryFlowVersion(tableName);
		} else {
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
			if (tableInfo != null) {
				result = mgt.queryTableVersion(tableInfo.getId(), designId);
			}
		}
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("versions", result.retVal);
		}
		request.setAttribute("versionType", versionType);
		request.setAttribute("host", host);
		request.setAttribute("tableName", tableName);
		return getForward(request, mapping, "version");
	}

	/**
	 * queryWokFlow
	 * 
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws UnsupportedEncodingException 
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		String type = this.getParameter("selectType", request);
		request.setAttribute("type", type);

		String val = request.getParameter("val");
		if (!"".equals(val) && val != null) {
			val = GlobalsTool.toChinseChar(val);
		}
		request.setAttribute("val", val);

		// 设置启用，停用操作
		try {
			String enable = getParameter("sendOpKeyId", request);
			String noEnable = getParameter("sendEnKeyId", request);
			String[] userEnable = null;
			String[] userNoEnable = null;
			if (enable != null && enable != "") {
				enable = enable.substring(0, enable.length() - 1);
				// 启用版块
				userEnable = enable.split(",");
				int flag = 0;
				for (int i = 0; i < userEnable.length; i++) {
					flag = mgt.isAbleUsed(userEnable[0]);
				}
				if (flag == 1) {
					if ("erp".equals(this.getParameter("comeType", request))) {
						EchoMessage.error().add(getMessage(request, "workFlow.msg.cannotUseFlow")).setBackUrl("/ERPWorkFlowTempSearchAction.do?comeType=erp").setRequest(request);
						return getForward(request, mapping, "message");
					}
				} else if (flag == 2) {
					if ("erp".equals(this.getParameter("comeType", request))) {
						EchoMessage.error().add("流程未设置反审核人").setBackUrl("/ERPWorkFlowTempSearchAction.do?comeType=erp").setRequest(request);
						return getForward(request, mapping, "message");
					}
				}
				mgt.changeState(userEnable, 1);
			}
			if (noEnable != null && noEnable != "") {
				noEnable = noEnable.substring(0, noEnable.length() - 1);
				// 不启用版块
				userNoEnable = noEnable.split(",");
				String tableNames = mgt.selectExistBill(userNoEnable, this.getLocale(request).toString(), "noFinish");
				if (tableNames.length() > 0) {
					if ("erp".equals(this.getParameter("comeType", request))) {
						EchoMessage.info().add(getMessage(request, "com.not.approved.flow", tableNames)).setBackUrl("/ERPWorkFlowTempSearchAction.do?comeType=erp").setRequest(request);
					} else {
						EchoMessage.info().add(getMessage(request, "com.not.approved.flow", tableNames)).setBackUrl("/OAWorkFlowTempQueryAction.do?operation=4&selectType=" + type + "&val=" + val+"&moduleType="+request.getParameter("moduleType"))
								.setRequest(request);
					}
					return getForward(request, mapping, "message");
				}
				mgt.changeState(userNoEnable, 0);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// ERP的流程设计查询
		String mapParam = mapping.getParameter();
		if (mapParam.equals("/ERPWorkFlowTempSearchAction.do")) {
			return this.queryErp(mapping, form, request, response);
		}

		// OA的流程设计查询

		OAWorkFlowTempSearchForm searchForm = (OAWorkFlowTempSearchForm) form;

		if ("status".equals(type) && !val.equals(searchForm.getTemplateStatus())) { //按状态查询
			searchForm.setTemplateStatus(Integer.parseInt(val));
		}
		if ("type".equals(type) && !val.equals(searchForm.getTemplateType())) { //按类型查询
			searchForm.setTemplateType(Integer.parseInt(val));
		}
		if ("class".equals(type) && !val.equals(searchForm.getTemplateClass())) { // 按类别查询
			String[] selectClass = val.split(",");
			String theClass = selectClass[1];
			searchForm.setSelectClass(theClass);
			searchForm.setTemplateClass(selectClass[0]);
		}
		if ("keyword".equals(type) && !val.equals(searchForm.getTableName())) { //关键字查询
			searchForm.setTemplateName(val);
			request.setAttribute("keyVal", val);
		}

		String showType = this.getParameter("showType", request);
		if ((showType == null || "".equals(showType)) && mapParam.equals("/OAMyWorkFlow.do")) {
			showType = "frame";
		}

		if (searchForm != null) {
			Result rs;
			// 执行查询
			if ("frame".equals(showType)) {
				rs = flowUtil.queryClass(getLoginBean(request)); 
			} else {
				rs = mgt.query(getLocale(request).toString(), searchForm.getTemplateName(), searchForm.getTemplateType(), searchForm.getTemplateClass(), searchForm.getTemplateStatus(), searchForm
						.getPageNo(), searchForm.getPageSize());
				request.setAttribute("fromCount", (searchForm.getPageNo() - 1) * searchForm.getPageSize());
			}

			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 查询成功

				request.setAttribute("sForm", searchForm);
				//request.getSession().setAttribute("allForm", searchForm);
				request.setAttribute("result", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));

			} else {
				// 查询失败
				EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		String host = request.getHeader("Host");
		if (host.indexOf(":") > 0) {
			host = host.replace(':', '|');
		} else {
			host = host + "|80";
		}
		searchForm = new OAWorkFlowTempSearchForm();
		request.getSession().setAttribute("OAWorkFlowTempSearchForm", null);
		// 设置工作流控件需要的主机地址（URL）
		request.setAttribute("host", host);
		if ("true".equals(getParameter("isFlowTh", request))) {
			request.setAttribute("flowAdd", "true");
			return getForward(request, mapping, "workflowAdd");
		}
		if ("frame".equals(showType)) {
			return getForward(request, mapping, "frame");
		} else {
			return getForward(request, mapping, "list");
		}
	}

	/**
	 * 保存流程节点数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward saveFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String flowId = getParameter("flowId", request);
		String json = getParameter("json", request);
		String flowxml = getParameter("flowxml", request);

		OAWorkFlowTemplate workflow = BaseEnv.workFlowInfo.get(flowId);
		ArrayList<FlowNodeBean> nodeList = gson.fromJson(json, new TypeToken<ArrayList<FlowNodeBean>>() {
		}.getType());
		HashMap<String, FlowNodeBean> flowMap = new HashMap<String, FlowNodeBean>();
		HashMap<String, String> groupMap = mgt.selectEmpGroupMap();

		Properties codePro = new Properties();
		String code = "ok";
		/* 验证流程设计是否正确 */
		begin: for (int i = 0; i < nodeList.size(); i++) {
			FlowNodeBean flowNode = nodeList.get(i);
			codePro.setProperty("nodeId", flowNode.getKeyId());
			/*任何节点的步骤都不能为空*/
			if (!"0".equals(flowNode.getKeyId()) && !"-1".equals(flowNode.getKeyId()) && (flowNode.getDisplay() == null || flowNode.getDisplay().length() == 0)) {
				code = "节点名称不能为空！";
				break begin;
			}

			/*除了选择节点和结束节点，其它节点必须设置下一节点*/
			if (!"CHOICE".equals(flowNode.getZAction()) && !"STOP".equals(flowNode.getZAction())
					&& (flowNode.getTo() == null || flowNode.getTo().length() == 0 || !existFlowNode(nodeList, flowNode.getTo()))) {
				code = "【" + flowNode.getDisplay() + "】流程节点没设置下一节点";
				break begin;
			}
			flowNode.setId(IDGenerater.getId());
			flowNode.setFlowId(flowId);
			/*条件节点 验证判断*/
			if ("CHOICE".equals(flowNode.getZAction())) {
				if (flowNode.getConditionList() != null && flowNode.getConditionList().size() < 2) {
					code = "条件节点的分支条件必须有两个或两个以上";
					break begin;
				}
				for (ConditionsBean condition : flowNode.getConditionList()) {
					if (condition.getDisplay() == null || condition.getDisplay().length() == 0) {
						codePro.setProperty("to", condition.getTo());
						code = "节点名称不能为空！";
						break begin;
					}
					condition.setId(IDGenerater.getId());
					condition.setFlowNode3(flowNode);
					flowNode.getConditionSet().add(condition);
					if (condition.getConditions().size() == 0) {
						codePro.setProperty("to", condition.getTo());
						code = "分支节点【" + condition.getDisplay() + "】的条件不能为空！";
						break begin;
					}
					for (ConditionBean cond : condition.getConditions()) {
						cond.setId(IDGenerater.getId());
						cond.setConditionBean(condition);
					}
				}
			} else if (!"0".equals(flowNode.getKeyId()) && !"-1".equals(flowNode.getKeyId())) {
				//除开始结束流程，条件节点，其它流程必须设置审核人
				if (0 == flowNode.getAutoSelectPeople() && (flowNode.getApprovers() == null || flowNode.getApprovers().size() == 0)) {
					code = "【" + flowNode.getDisplay() + "】流程节点没设置审核人";
					break begin;
				}
			}

			for (ApproveBean approver : flowNode.getApprovers()) {
				String returnCode = existApprover(flowNode, approver, groupMap, "approver");
				if (!"ok".equals(returnCode)) {
					code = returnCode;
					break begin;
				}
				approver.setId(IDGenerater.getId());
				approver.setFlowNode2(flowNode);
				flowNode.getApproveSet().add(approver);
			}
			for (FieldBean fieldBean : flowNode.getFields()) {
				fieldBean.setId(IDGenerater.getId());
				fieldBean.setFlowNode(flowNode);

				//如果流程中的字段在表结构中不存在，则删除
				DBFieldInfoBean dbField = GlobalsTool.getFieldBean(workflow.getTemplateFile(), fieldBean.getFieldName());;
				if (dbField == null && fieldBean.getFieldName().contains("_")) {
					dbField = GlobalsTool.getFieldBean2(fieldBean.getFieldName());
				}
				if (dbField != null) {
					flowNode.getFieldSet().add(fieldBean);
				}
			}
			for (String strField : flowNode.getHiddenFields()) {
				FieldBean field = new FieldBean();
				field.setId(IDGenerater.getId());
				field.setFieldName(strField);
				field.setFieldType("public");
				field.setFlowNode(flowNode);
				//如果流程中的字段在表结构中不存在，则删除
				DBFieldInfoBean dbField = null;
				if (strField.contains("_")) {
					dbField = GlobalsTool.getFieldBean2(strField);
				} else {
					dbField = GlobalsTool.getFieldBean(workflow.getTemplateFile(), strField);
				}
				if (dbField != null) {
					flowNode.getFieldSet().add(field);
				}
			}
			for (ApproveBean notePeople : flowNode.getNotePeople()) {
				String returnCode = existApprover(flowNode, notePeople, groupMap, "note");
				if (!"ok".equals(returnCode)) {
					code = returnCode;
					break begin;
				}
				notePeople.setId(IDGenerater.getId());
				notePeople.setFlowNode2(flowNode);
				notePeople.setCheckType("note");
				flowNode.getApproveSet().add(notePeople);
			}
			if(flowNode.getFilterSet()==5 || flowNode.getFilterSet()==6 ){
				if(flowNode.getFilterSetSQL()==null || flowNode.getFilterSetSQL().trim().length()==0){
					code = "【" + flowNode.getDisplay() + "】流程节点没填写过滤规则SQL";
					break begin;
				}
			}
			if(flowNode.getAutoSelectPeople()==3 ){
				if(flowNode.getAutoSelectPeopleSQL()==null || flowNode.getAutoSelectPeopleSQL().trim().length()==0){
					code = "【" + flowNode.getDisplay() + "】流程节点没填写自定义选人SQL";
					break begin;
				}
			}
			
			flowMap.put(flowNode.getKeyId(), flowNode);
		}
		if ("ok".equals(code) && (flowMap.get("0") == null || flowMap.get("-1") == null)) {
			code = "必须包含开始节点和结束节点！";
		}
		for (FlowNodeBean tfb: flowMap.values()) {
			//检查choice结点中是否有空条件
			if("CHOICE".equals(tfb.getZAction())){
				ArrayList dels = new ArrayList();
				for(ConditionsBean condition :tfb.getConditionSet()){
					if(flowMap.get(condition.getTo())==null){
						dels.add(condition);
					}
				}
				tfb.getConditionSet().removeAll(dels);
			}
		}

		if ("ok".equals(code)) {
			Result result = mgt.addFlowData(flowMap, flowxml, flowId);
			if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				code =result.retVal==null||result.retVal.equals("")? "修改失败":""+result.retVal;
			}
		}
		codePro.setProperty("code", code);
		request.setAttribute("msg", gson.toJson(codePro));
		return getForward(request, mapping, "blank");
	}

	/**
	 * 判断某节点是否存在
	 * @return
	 */
	public boolean existFlowNode(ArrayList<FlowNodeBean> nodeList, String nodeId) {
		for (FlowNodeBean nodeBean : nodeList) {
			if (nodeBean.getKeyId().equals(nodeId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断设置的审核人，部门，职位，岗位 是否存在
	 * @param approver
	 * @return
	 */
	public String existApprover(FlowNodeBean flowNode, ApproveBean approver, HashMap<String, String> groupMap, String strType) {
		String code = "ok";
		if ("employee".equals(approver.getType())) {
			OnlineUser onlineuser = OnlineUserInfo.getUser(approver.getUser());
			if (onlineuser == null) {
				code = flowNode.getDisplay() + "-流程节点设置的" + ("note".equals(strType) ? "提醒人" : "审核人") + "【" + approver.getUserName() + "】不存在！";
			}
		} else if ("dept".equals(approver.getType())) {
			OnlineUser onlineuser = OnlineUserInfo.existDept(approver.getUser());
			if (onlineuser == null) {
				code = flowNode.getDisplay() + "-流程节点设置的" + ("note".equals(strType) ? "提醒部门" : "审核部门") + "【" + approver.getUserName() + "】不存在！";
			}
		} else if ("duty".equals(approver.getType())) {
			boolean exist = existEnumeItems("duty", approver.getUser());
			if (!exist) {
				code = flowNode.getDisplay() + "-流程节点设置的" + ("note".equals(strType) ? "提醒职位" : "审核职位") + "【" + approver.getUserName() + "】不存在！";
			}
		} else if ("post".equals(approver.getType())) {
			boolean exist = existEnumeItems("responsibility", approver.getUser());
			if (!exist) {
				code = flowNode.getDisplay() + "-流程节点设置的" + ("note".equals(strType) ? "提醒岗位" : "审核岗位") + "【" + approver.getUserName() + "】不存在！";
			}
		} else if ("group".equals(approver.getType())) {
			String exist = groupMap.get(approver.getUser());
			if (exist == null) {
				code = flowNode.getDisplay() + "-流程节点设置的" + ("note".equals(strType) ? "提醒分组" : "审核分组") + "【" + approver.getUserName() + "】不存在！";
			}
		}
		return code;
	}

	/**
	 * 
	 * 判断某个枚举值是否存在
	 * @param enumeration String
	 * @return List
	 */
	public boolean existEnumeItems(String enumName, String enumValue) {

		boolean exist = false;
		EnumerateBean enumBean = (EnumerateBean) BaseEnv.enumerationMap.get(enumName);
		for (int i = 0; i < enumBean.getEnumItem().size(); i++) {
			EnumerateItemBean item = (EnumerateItemBean) enumBean.getEnumItem().get(i);
			if (item != null && enumValue.equals(item.getEnumValue())) {
				exist = true;
			}
		}
		return exist;
	}

	/**
	 * 条件设置-查询流程字段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward flowfields(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String flowId = getParameter("flowId", request);
		String tableName = getParameter("tableName", request);
		String keyword = getParameter("keyword", request);

		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName);
		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(flowId);
		ArrayList<String[]> fieldList = new ArrayList<String[]>();
		for (DBFieldInfoBean field : tableInfo.getFieldInfos()) {
			if (GlobalsTool.canDisField(field)) {
				String display = field.getDisplay().get("zh_CN");
				if (template != null && "1".equals(template.getDesignType())) {
					display = field.getLanguageId();
				}
				if (keyword != null && !display.contains(keyword)) {
					continue;
				}
				String[] arrField = { field.getFieldName(), display, String.valueOf(field.getInputType()), field.getRefEnumerationName() };
				fieldList.add(arrField);
				if ("employee".equals(field.getDefaultValue()) || "EmployeeID".equalsIgnoreCase(field.getFieldName())) {
					String[] arrField1 = { field.getFieldName() + "_duty", display + "-职位", String.valueOf(field.getInputType()), "duty" };
					String[] arrField2 = { field.getFieldName() + "_post", display + "-岗位", String.valueOf(field.getInputType()), "post" };
					String[] arrField3 = { field.getFieldName() + "_dept", display + "-部门", String.valueOf(field.getInputType()), "dept" };
					fieldList.add(arrField1);
					fieldList.add(arrField2);
					fieldList.add(arrField3);
				}
			}
		}
		request.setAttribute("keyword", keyword);
		request.setAttribute("fieldList", fieldList);
		request.setAttribute("tableName", tableName);
		request.setAttribute("flowId", flowId);
		return getForward(request, mapping, "flowfields");
	}

	private ActionForward queryErp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		OAWorkFlowTempSearchForm searchForm = (OAWorkFlowTempSearchForm) form;

		Result rs = mgt.queryErp(getLocale(request).toString(), searchForm.getTemplateName(), searchForm.getTemplateStatus(), searchForm.getPageNo(), searchForm.getPageSize(), searchForm
				.getTableName());
		request.setAttribute("fromCount", (searchForm.getPageNo() - 1) * searchForm.getPageSize());
		String host = request.getHeader("Host");
		if (host.indexOf(":") < 0) {
			host = host + ":80";
		}

		// 设置工作流控件需要的主机地址（URL）
		request.setAttribute("host", host);

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 查询成功
			request.setAttribute("result", rs.retVal);
			request.setAttribute("pageBar", pageBar(rs, request));
		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "list");
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private ActionForward deleteWokFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		String type = this.getParameter("selectType", request);

		request.setAttribute("type", type);
		String val = request.getParameter("val");
		if (!"".equals(val) && val != null)
			val = GlobalsTool.toChinseChar(val);
		request.setAttribute("val", val);

		String[] keyIds = request.getParameter("keyId").split(",");

		String tableNames = mgt.selectExistBill(keyIds, this.getLocale(request).toString(), "all");
		if (tableNames.length() > 0) {
			EchoMessage.info().add(getMessage(request, "com.exist.workflow.bill", tableNames)).setBackUrl("/OAWorkFlowTempQueryAction.do?operation=4&selectType=" + type + "&val=" + val+"&moduleType="+request.getParameter("moduleType")).setRequest(
					request);
			return getForward(request, mapping, "message");
		}

		Result result = mgt.deleteWorkFlow(keyIds);
		if (result.retCode == ErrorCanst.DATA_ALREADY_USED) {
			// 删除失败\

			EchoMessage.error().add(getMessage(request, "web.oa.workFlow.msg.modelIsnull")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		return query(mapping, form, request, response);
	}

}
