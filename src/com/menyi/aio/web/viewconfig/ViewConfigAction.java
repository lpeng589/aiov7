package com.menyi.aio.web.viewconfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;

public class ViewConfigAction extends BaseAction {

	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req);
		if (loginBean == null) {
			BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
			return getForward(req, mapping, "indexPage");
		}
		if ("admin".equalsIgnoreCase(getLoginBean(req).getName())) {
			return null;
		}

		return null;
	}

	/**
	 * 所有界面设计的代码写在此类中，和UserFunctionAction分开放置，避免该类代码太多
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		String op = request.getParameter("op");
		ActionForward forward = null;
		if ("addCol".equals(op)) {
			forward = addCol(mapping, form, request, response);
		} else {
			forward = viewConfig(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 添加列
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addCol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		String tableName = request.getParameter("tableName");
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(BaseEnv.tableInfos, tableName);
		if ("mainTable".equals(type)) {
			//取主表可添加字段
			ArrayList<DBFieldInfoBean> allFields = getAllMainFieldList(tableInfo, request, tableName);
			//如果只是默认列配置，则只需显示所有隐藏可显示字段即可    		
			request.setAttribute("mainFieldInfos", allFields); //取主表各个字段
		}

		return getForward(request, mapping, "addCol");

	}

	/**
	 * 界面设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward viewConfig(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String tableName = request.getParameter("tableName");
		String moduleType = request.getParameter("moduleType");
		request.setAttribute("tableName", tableName);
		request.setAttribute("moduleType", moduleType);
		request.setAttribute("parentCode", request.getParameter("parentCode"));
		request.setAttribute("parentTableName", request.getParameter("parentTableName"));

		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(BaseEnv.tableInfos, tableName);
		ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);

		/**
		 * 1、取所有字段
		 * 1、取显示字段，不显示字段，可显示字段
		 */
		request.setAttribute("mainFieldInfos", getAllMainFieldList(tableInfo, request, tableName)); //取主表各个字段
		
		
		request.setAttribute("childTableList", getChildTableList(childTableList, request)); //取明细表列表

		return getForward(request, mapping, "viewConfig");

	}

	/**
	 * 取得明细表的列表，1、不同模块使用同一个表时要能过滤不同模块，2，对表进行排序
	 * @param childTableList
	 * @param request
	 * @return
	 */
	public ArrayList<DBTableInfoBean> getChildTableList(ArrayList<DBTableInfoBean> childTableList, HttpServletRequest request) {
		MOperation mop = GlobalsTool.getMOperationMap(request);
		//得到各个模块的字段信息，此功能是为了实现不同模块使用同一个表，和相同的define，可能字段名字会有不同
		HashMap<String, ArrayList<String>> moduleTable = (HashMap<String, ArrayList<String>>) BaseEnv.ModuleField.get(mop.getModuleUrl());
		if (moduleTable != null ) {
			ArrayList<DBTableInfoBean> newChildTable = cloneObject(childTableList);
			for (int i = 0; i < newChildTable.size(); i++) {
				DBTableInfoBean childTableInfo = (DBTableInfoBean) newChildTable.get(i);
				if (moduleTable.get(childTableInfo.getTableName()) != null) {
					for (DBFieldInfoBean dbField : childTableInfo.getFieldInfos()) {
						HashMap<String,ArrayList<String[]>> tableMap = ((HashMap<String, HashMap<String,ArrayList<String[]>>>)BaseEnv.ModuleField).get(mop.getModuleUrl()+"");
						if(tableMap != null && tableMap.get(childTableInfo.getTableName()) != null){
							for(String[] ss : tableMap.get(childTableInfo.getTableName())){
								if (ss[2] != null && !ss[2].equals("")) {
									KRLanguage language = new KRLanguage();
									language.putLanguage("zh_CN", ss[2]);
									dbField.setDisplay(language);
								}
							}
						}
					}
				}
			}
			Collections.sort(newChildTable, new SortDBTable());
			return newChildTable;
		} else {
			Collections.sort(childTableList, new SortDBTable());
			return childTableList;

		}
	}

	/**
	 * 取主表的所有字段包括弹出窗，不管列配置，如果有同一表不同模块时，这里要能过滤非本模板字段。
	 * @param tableInfo
	 * @param request
	 * @param tableName
	 * @return
	 */
	public ArrayList<DBFieldInfoBean> getAllMainFieldList(DBTableInfoBean tableInfo, HttpServletRequest request, String tableName) {
		ArrayList<DBFieldInfoBean> fieldList = new ArrayList<DBFieldInfoBean>();
		ArrayList<DBFieldInfoBean> dbList = (ArrayList<DBFieldInfoBean>) tableInfo.getFieldInfos();
 
		MOperation mop = GlobalsTool.getMOperationMap(request);
		//得到各个模块的字段信息，此功能是为了实现不同模块使用同一个表，和相同的define，可能字段名字会有不同
		HashMap<String, ArrayList<String>> moduleTable = (HashMap<String, ArrayList<String>>) BaseEnv.ModuleField.get(mop.getModuleUrl());

		if (moduleTable != null && moduleTable.get(tableName) != null) {
			ArrayList<String> moduleField = moduleTable.get(tableName);
			for (int i = 0; i < dbList.size(); i++) {
				DBFieldInfoBean dbField = dbList.get(i);
				if (moduleField.contains(dbField.getFieldName())) {
					fieldList.add(dbField);
				} else {
					DBFieldInfoBean field = cloneObject((DBFieldInfoBean) dbList.get(i));
					field.setInputType((byte) 3);
					fieldList.add(field);
				}
			}
		} else {
			fieldList = dbList;
		}
		//增加弹出窗字段
		for(DBFieldInfoBean field : fieldList){
			if(field.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE || field.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE){
				
			}
		}
		
		
		return fieldList;
	}

	/**
	 * 克隆一个DBFieldInfoBean对象
	 * @param fieldBean
	 * @return
	 */
	public DBFieldInfoBean cloneObject(DBFieldInfoBean fieldBean) {
		DBFieldInfoBean field = new DBFieldInfoBean();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(fieldBean);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			field = (DBFieldInfoBean) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}

	/**
	 * 克隆一个DBTableInfoBean对象
	 * 
	 * @param fieldBean
	 * @return
	 */
	public ArrayList<DBTableInfoBean> cloneObject(ArrayList<DBTableInfoBean> fieldList) {

		ArrayList<DBTableInfoBean> listField = new ArrayList<DBTableInfoBean>();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(fieldList);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			listField = (ArrayList<DBTableInfoBean>) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listField;
	}

	/**
	 * DBTableInfoBean 按tbleName排序
	 */
	public class SortDBTable implements Comparator {
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
}
