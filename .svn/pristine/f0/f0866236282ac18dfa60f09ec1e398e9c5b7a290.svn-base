package com.menyi.aio.web.tablemapped;

import com.menyi.web.util.*;

import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import com.dbfactory.*;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KRLanguage;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Hashtable;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;

import java.util.*;
import com.menyi.aio.bean.*;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.*;
import com.menyi.aio.web.usermanage.*;

/**
 * Description: <br/>Copyright (C), 2008-2012, Justin.T.Wang <br/>This Program
 * is protected by copyright laws. <br/>Program Name: <br/>Date:
 * 
 * @autor Justin.T.Wang yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class TableMappedAction extends MgtBaseAction {

	private TableMappedMgt tableMapMgt = new TableMappedMgt();

	private UserMgt userMgt = new UserMgt();

	public TableMappedAction() {
	}

	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TableMappedSearchForm searchForm = (TableMappedSearchForm) form;
		if (searchForm != null) {
			// 执行查询

			Result rs = tableMapMgt.query(searchForm.getName(),searchForm.getTargetName(), searchForm
					.getPageNo(), searchForm.getPageSize());

			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 查询成功
				request.setAttribute("result", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
			} else {
				// 查询失败
				EchoMessage.error()
						.add(getMessage(request, "common.msg.error"))
						.setRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		return getForward(request, mapping, "tableMappedList");
	}

	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HashMap param = new HashMap();
		Hashtable allTables = (Hashtable) request.getSession()
				.getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String localeStr = this.getLocale(request).toString();
		List<String[]> tableList = new ArrayList<String[]>();
		Map<String, List<String[]>> fieldMap = new HashMap<String, List<String[]>>();
		for (Object obj : allTables.keySet()) {
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,
					obj.toString());
			if (tableInfo != null) {
				KRLanguage tableDisplayMap = tableInfo.getDisplay();
				if (tableDisplayMap != null) {
					String tableDisplay = (String) tableDisplayMap
							.get(localeStr);
					String[] table = { tableInfo.getTableName(), tableDisplay,
							tableInfo.getId() };
					tableList.add(table);
					List<DBFieldInfoBean> fieldList = tableInfo.getFieldInfos();
					List<String[]> fields = new ArrayList<String[]>();
					for (DBFieldInfoBean field : fieldList) {
						KRLanguage displayMap = field.getDisplay();
						String fieldDisplayBean = null;
						if (displayMap != null) {
							fieldDisplayBean = displayMap.get(localeStr);
						}
						String fieldDisplay = field.getFieldName();
						if (fieldDisplayBean != null
								&& fieldDisplayBean != null) {
							fieldDisplay = fieldDisplayBean;
						}
						String[] ff = { field.getFieldName(), fieldDisplay,
								field.getId() };
						fields.add(ff);
					}
					fieldMap.put(tableInfo.getTableName(), fields);
				}
			}
		}
		// 根据表显名排序
		List<String[]> sortList = SortUtil.getSortList(tableList);
		request.setAttribute("tableList", sortList);
		request.setAttribute("fieldMap", fieldMap);
		return getForward(request, mapping, "tableMappedAdd");
	}

	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Result rs = new Result();
		LoginBean login = (LoginBean) request.getSession().getAttribute(
				"LoginBean");
		String[] fields = request.getParameterValues("fieldMapping");
		String mostlyTable = request.getParameter("mostlytable");
		String childTable = request.getParameter("childtable");
		String sysTime = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		rs.retCode = ErrorCanst.DEFAULT_FAILURE;
		ActionForward forward = getForward(request, mapping, "alert");
		// 保存生成的脚本
		List<TableMappedBean> list = new ArrayList<TableMappedBean>();
		for (String str : fields) {
			if (str != null && !str.trim().equals("")) {
				TableMappedBean bean = new TableMappedBean(IDGenerater.getId(),
						mostlyTable, str.split("#")[0], childTable, str
								.split("#")[1], login.getId(), login.getId(),
						sysTime, sysTime, login.getSunCmpClassCode());
				rs = tableMapMgt.add(bean);
				if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					break;
				}
				// 填充生成的映射脚本
				list.add(bean);
			}
		}

		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			forward = getForward(request, mapping, "message");
			return forward;

		}
		// 保存生成的脚本
		String path = request.getSession().getServletContext().getRealPath(
				"Mapped.sql");
		List<String> sqls = ConvertToSql.getMappedSql(list);
		for (int i = 0; i < sqls.size(); i++) {
			TestRW.saveToSql(path, sqls.get(i));
		}
		new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
				mostlyTable+"-"+childTable, "tblTableMapped", "字段映射关系","");
		// ////////////////////////
		EchoMessage.success().add(getMessage(request, "common.msg.addSuccess"))
				.setBackUrl(
						"/tableMappedQueryAction.do?winCurIndex="
								+ request.getParameter("winCurIndex"))
				.setAlertRequest(request);

		return forward;
	}

	protected ActionForward updatePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String tableMap = request.getParameter("keyId");
		String mostly = "";
		String child = "";
		Result rs = new Result();
		ActionForward forward = getForward(request, mapping, "alert");
		rs.retCode = ErrorCanst.DEFAULT_FAILURE;
		List<TableMappedBean> tableMapList = new ArrayList<TableMappedBean>();
		if (tableMap != null && !tableMap.trim().equals("")) {
			mostly = tableMap.split("@")[0];
			child = tableMap.split("@")[1];
			rs = tableMapMgt.queryByMostlyAndChild(mostly, child);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				EchoMessage.error()
						.add(getMessage(request, "common.msg.error"))
						.setRequest(request);
				forward = getForward(request, mapping, "message");
				return forward;
			}
			tableMapList = (ArrayList<TableMappedBean>) rs.retVal;
		}

		HashMap param = new HashMap();
		Hashtable allTables = (Hashtable) request.getSession()
				.getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		ArrayList list = new ArrayList();

		String localeStr = this.getLocale(request).toString();
		List<String[]> tableList = new ArrayList<String[]>();
		Map<String, List<String[]>> fieldMap = new HashMap<String, List<String[]>>();
		for (Object obj : allTables.keySet()) {
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,
					obj.toString());
			KRLanguage tableDisplayMap = tableInfo.getDisplay();
			String tableDisplay = "";
			if (tableDisplayMap != null) {
				tableDisplay = (String) tableDisplayMap.get(localeStr);
			}

			String[] table = { tableInfo.getTableName(), tableDisplay,
					tableInfo.getId() };
			tableList.add(table);
			List<DBFieldInfoBean> fieldList = tableInfo.getFieldInfos();
			List<String[]> fields = new ArrayList<String[]>();
			for (DBFieldInfoBean field : fieldList) {

				KRLanguage displayMap = field.getDisplay();
				String fieldDisplayBean = null;
				if (displayMap != null) {
					fieldDisplayBean = displayMap.get(localeStr);
				}

				String fieldDisplay = field.getFieldName();
				if (fieldDisplayBean != null && fieldDisplayBean != null) {
					fieldDisplay = fieldDisplayBean;
				}
				String[] ff = { field.getFieldName(), fieldDisplay,
						field.getId() };
				fields.add(ff);
			}
			fieldMap.put(tableInfo.getTableName(), fields);
		}
		request.setAttribute("mostly", mostly);
		request.setAttribute("child", child);
		// 根据表显名排序
		List<String[]> sortList = SortUtil.getSortList(tableList);
		request.setAttribute("tableList", sortList);
		request.setAttribute("fieldMap", fieldMap);
		request.setAttribute("tableMapList", tableMapList);
		forward = getForward(request, mapping, "tableMappedUpdate");
		return forward;
	}

	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String str = request.getParameter("oldMostlyChild");
		String mostly = request.getParameter("mostlytable");
		String child = request.getParameter("childtable");
		String[] fieldMaps = request.getParameterValues("fieldMapping");
		List<TableMappedBean> oldBeans = new ArrayList<TableMappedBean>();
		Result rs = new Result();
		LoginBean login = (LoginBean) request.getSession().getAttribute(
				"LoginBean");
		String sysTime = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		rs.retCode = ErrorCanst.DEFAULT_FAILURE;
		String oldMostly = "";
		String oldChild = "";
		// 保存修改数据
		List<String> list = new ArrayList<String>();

		if (str != null && !str.trim().equals("")) {
			oldMostly = str.split("@")[0];
			oldChild = str.split("@")[1];
		}
		rs = tableMapMgt.queryByMostlyAndChild(oldMostly, oldChild);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			oldBeans = (ArrayList<TableMappedBean>) rs.retVal;
		}
		for (TableMappedBean bean : oldBeans) {
			rs = tableMapMgt.delete(bean);
			// 删除旧的数据

			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				break;
			}
		}
		// 保存删除旧数据的脚本
		list.add("--" + getMessage(request, "tablemapped.lb.changetable") + ":"
				+ oldMostly + ""
				+ getMessage(request, "tablemapped.lb.withaimtable") + ":"
				+ oldChild + ""
				+ getMessage(request, "tablemapped.lb.beetweenwith") + "\n");
		list.add("delete from tblTableMapped where mostlyTable='" + oldMostly
				+ "' and childTable='" + oldChild + "'");
		for (String feilds : fieldMaps) {
			if (feilds != null && feilds.split("#").length > 1) {
				String mostlyField = feilds.split("#")[0];
				String childField = feilds.split("#")[1];
				if (mostlyField != null && !mostlyField.trim().equals("")
						&& childField != null && !childField.trim().equals("")) {
					TableMappedBean bean = new TableMappedBean(IDGenerater
							.getId(), mostly, mostlyField, child, childField,
							login.getId(), login.getId(), sysTime, sysTime,
							login.getSunCmpClassCode());
					rs = tableMapMgt.add(bean);
					// 插入新的数据
					list.add(ConvertToSql.getSqlByTblTableMapped(bean));
					if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
						break;
					}
				}
			}
		}
		ActionForward forward = getForward(request, mapping, "alert");
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 修改成功后保存生成的脚本
			String path = request.getSession().getServletContext().getRealPath(
					"Mapped.sql");
			for (int i = 0; i < list.size(); i++) {
				TestRW.saveToSql(path, list.get(i));
			}
			// 修改成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl(
							"/tableMappedQueryAction.do?winCurIndex="
									+ request.getParameter("winCurIndex"))
					.setAlertRequest(request);
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
					mostly+"-"+child, "tblTableMapped", "字段映射关系","");
		} else {
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateFailture"))
					.setAlertRequest(request);
		}
		return forward;
	}

	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 存放删除脚本
		List<String> list = new ArrayList<String>();
		list.add("--"+getMessage(request, "tablemapped.lb.deletetablebetween")+"\n");
		String[] tableMapping = request.getParameterValues("keyId");
		Result rs = new Result();
		rs.retCode = ErrorCanst.DEFAULT_FAILURE;
		if (tableMapping != null && tableMapping.length != 0) {
			for (String str : tableMapping) {
				String mostlyTable = str.split("@")[0];
				String childTable = str.split("@")[1];
				rs = tableMapMgt.queryByMostlyAndChild(mostlyTable, childTable);
				List<TableMappedBean> tableMaps = new ArrayList<TableMappedBean>();
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					tableMaps = (ArrayList<TableMappedBean>) rs.retVal;
					for (TableMappedBean tableMap : tableMaps) {
						rs = tableMapMgt.delete(tableMap);
						if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
							break;
						}

					}
				}
				new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
						mostlyTable+"-"+childTable, "tblTableMapped", "字段映射关系","");
				// 保存删除脚本
				list
						.add("delete from tblTableMapped where mostlyTable='"
								+ mostlyTable + "' and childTable='"
								+ childTable + "'");
			}
		}
		ActionForward forward = getForward(request, mapping, "alert");
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 删除成功后保存脚本
			String path = request.getSession().getServletContext().getRealPath(
					"Mapped.sql");
			for (int i = 0; i < list.size(); i++) {
				TestRW.saveToSql(path, list.get(i));
			}
			// 删除成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.delSuccess")).setBackUrl(
					"/tableMappedQueryAction.do?winCurIndex="
							+ request.getParameter("winCurIndex"))
					.setAlertRequest(request);
			
		} else {
			// 删除失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.delFailture"))
					.setAlertRequest(request);
		}
		return forward;
	}
}
