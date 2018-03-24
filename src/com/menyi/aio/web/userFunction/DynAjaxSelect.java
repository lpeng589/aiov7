package com.menyi.aio.web.userFunction;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.*;
import com.menyi.aio.web.favourstyle.UserStyleServlet;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.*;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 异步查询操作类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: 科荣软件
 * </p>
 * 
 * @author 刘勇
 * @version 1.0
 */
public class DynAjaxSelect extends DynDBManager {
	/**
	 * 下拉弹出框显示的记录数
	 */
	private static final int DROPDOWNCOUNT = 100;
	
	private DynAjaxSelect() {
	}

	/**
	 * <pre>
	 * 获取弹窗的多行数据，只返回前100条记录
	 * 返回字符串，每条件记录用 <b>|</b> 分开，记录里每个字段值用 <b>；</b>分开
	 * </pre>	 
	 * @param request
	 * @param response
	 * @return
	 * @see #popupSelect(HttpServletRequest, HttpServletResponse)
	 */
	public static String RecAutoSettlement(HttpServletRequest request, HttpServletResponse response){
		ArrayList<String[]> list = popupSelect(request, response,null);
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			Object[] os = list.get(i);
			result = result + os[1].toString() + "|"; // 给结果
		}
		return result;
	}
	/**
	 * <pre>
	 * 获取弹窗的多行数据，只返回前{@value #DROPDOWNCOUNT}条记录
	 * 返回字符串为JSON， 一维为记录，2维为 [显示内容，回填内容],两个内容见{@link #popupSelect(HttpServletRequest, HttpServletResponse)}
	 * </pre>	 
	 * @param request
	 * @param response
	 * @return
	 * @see #popupSelect(HttpServletRequest, HttpServletResponse) 
	 */
	public static String DropdownPopup(HttpServletRequest request, HttpServletResponse response){
		ArrayList<String[]> list = popupSelect(request, response,GlobalsTool.getLoginBean(request).getId());
		if(list.size() > DROPDOWNCOUNT)//如果超过需要显示的记录数，则只取数据的前几个
		{
			ArrayList<String[]> tmp = new ArrayList<String[]>();
			for (int i = 0; i < DROPDOWNCOUNT; i++) {
				tmp.add(list.get(i));
			}
			list = tmp;
		}
		Gson g = new Gson();
		return g.toJson(list);
	}
	/**
	 * <pre>
	 * 获取子弹窗的多行数据，
	 * 返回字符串为JSON， 一维为记录，2维为 [显示内容，回填内容],两个内容见{@link #popupSelect(HttpServletRequest, HttpServletResponse)}
	 * </pre>	 
	 * @param request
	 * @param response
	 * @return
	 * @see #popupSelect(HttpServletRequest, HttpServletResponse) 
	 */
	public static String DropdownChildData(HttpServletRequest request, HttpServletResponse response){
		String retStr = popupChildSelect(request, response,GlobalsTool.getLoginBean(request).getId());
		
		return retStr;
	}
	
	/**
	 * <pre>
	 * 获取弹窗的单行数据，条件对应request的参数 <b>selectValue</b>
	 * 如果查询后返回不是一行数据则返回字符串  "@condition:"+[条件]
	 * 如果只返回一行数据则返回数值，用分号分分隔
	 * </pre>
	 * @param request
	 * @param response
	 * @return
	 * @see #popupSelect(HttpServletRequest, HttpServletResponse) 
	 * @see UtilServlet#dynSelect(HttpServletRequest, HttpServletResponse) 动态弹出窗数据查询
	 */
	public static String getResultStr(HttpServletRequest request, HttpServletResponse response){
		String selectValue = request.getParameter("selectValue");
		selectValue = GlobalsTool.toChinseChar(selectValue);
		selectValue = GlobalsTool.encodeTextCode(selectValue);
		ArrayList<String[]> list = popupSelect(request, response,null);
		System.out.println(new Gson().toJson(list));
		String result = "";
		if (list.size() == 1) {
			String[] os = list.get(0);
				result = os[1]; // 给结果
		} else {
			result = "@condition:" + selectValue; // 给查询条件
		}
		return result;
	}
	/**
	 * 由弹出框名或表名+字段，把要显示的字段显示名\条件字段\回填字段\个性化设置 生成JSON数组 
	 * @param allTables 自定义表单
	 * @param selectName 弹出框名，当为NULL或“”时，则按tableName,fieldName取得弹出框
	 * @param tableName 表名
	 * @param fieldName 字段名 （表名加字段名，从自定义中可以取得弹出框）
	 * @param local 区域
	 * @param userId 用户标识
	 * @return
	 */
	public static String showView(Hashtable<String,DBTableInfoBean> allTables,String selectName,String tableName,String fieldName,Locale locale,String userId)
	{
		Map<String,ArrayList<Map<String,String>>> data = new HashMap<String, ArrayList<Map<String,String>>>();
		//弹出窗对应的Bean
		PopupSelectBean popSelectBean = null;
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// 如果页面传入的是字段名，则根据字段名查找出相应的弹出选择框
			popSelectBean = DDLOperation.getFieldInfo(allTables,tableName,fieldName).getSelectBean();
		}
		if(popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0){
			//这是一个有主弹出窗的弹窗
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
		}
		ArrayList<Map<String,String>> fields = new ArrayList<Map<String,String>>();
		//以下为显示字段
		for(PopField field:popSelectBean.getDisplayField())
		{
			//显示字段不包括图片
			if(13 ==field.fieldType){
				continue;
			}
			Map<String,String> map = new HashMap<String, String>();
			map.put("displayname", field.getDisplayLocale(tableName, allTables, locale));
			map.put("width", String.valueOf(field.getWidth()));
			fields.add(map);
		}
		data.put("showfields", fields);
		//以下为回填字段
		fields = new ArrayList<Map<String,String>>();
        for (int j = 0; j < popSelectBean.getReturnFields().size(); j++) {
            PopField fv = (PopField) popSelectBean.getReturnFields(). get(j);
                for (int k = 0; k < popSelectBean.getAllFields().size(); k++) {
                    PopField fv2 = (PopField) popSelectBean.getAllFields().get(k);
                    if (fv.getFieldName().equals(fv2.getFieldName())) {
                    	String fvn="";
                    	if(fv.type==1){
                    		fvn = fv.parentName;
                    		if(fvn == null || fvn.length() == 0){
                        		fvn = fv.fieldName;	                        		
                        	}                        	
                    	}else{
                        	fvn =  fv.asName;
                        	if(fvn == null || fvn.length() == 0){
                        		fvn = fv.fieldName;	                        		
                        	}
                    	}
                    	Map<String,String> map = new HashMap<String, String>();
            			map.put("fieldname", fvn);
            			map.put("compare", fv.compare?"true":"false");
            			fields.add(map);
                    	break ;
                    }
                }
        }
		data.put("returnfields", fields);
		//================================以下为条件字段
		ArrayList<Map<String,String>> cField = new ArrayList<Map<String,String>>();
		if (popSelectBean.isKeySearch()) {
			for (PopField field : popSelectBean.getDisplayField()) {
				if(field.keySearch)
				{
					Map<String,String> map = new HashMap<String, String>();
					map.put("fieldname", field.getFieldName());
					map.put("displayname", field.getDisplayLocale(tableName, allTables, locale));
					cField.add(map);
				}
			}
		} else {
			for (PopField field : popSelectBean.getDisplayField()) {
				if(field.getSearchType()!=PopField.SEARCH_NO && field.getSearchType()!=PopField.TYPE_DISPLAY)
				{
					Map<String,String> map = new HashMap<String, String>();
					map.put("fieldname", field.getFieldName());
					map.put("displayname", field.getDisplayLocale(tableName, allTables, locale));
					cField.add(map);
				}
			}
		}
		data.put("condition", cField);
		ArrayList<Map<String,String>> al = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String, String>();
		Map<String, String> m = UserStyleServlet.getFavourStyle(userId, popSelectBean.getName().trim(), "search_favour");
		if(m.get("search_favour")==null)
		{
			map.put("searchtype", "");
			map.put("searchfield", "");
		}else
		{
			map = new Gson().fromJson(m.get("search_favour"), map.getClass());
		}
		map.put("selectName", popSelectBean.getName().trim());
		al.add(map);
		data.put("favour", al);
		
		return new Gson().toJson(data);
	}

	/**
	 * <pre>
	 * 获取弹出窗对应数据的数组,只返回前100条
	 * 弹出窗有两个方式获得(1、弹出窗名(selectName) 2，表名(tableName)加字段名(fieldName)再从自定义表设置中获取相对应的弹出窗)
	 * 关键字查询分成两种：
	 * 	<li>当弹出窗有keySearch的时候，按有keySearch的所有字段进行搜索</li>
	 *  <li>当弹出窗没有keySearch的时候，按有的所有字段进行搜索</li>
	 * 进行关键字搜索搜索的值如果是以逗号分隔，那么去除逗号。
	 * userId为null时表示不使用用户自定义设置
	 * 用户自定义设置使用了 {@link UserStyleServlet#getFavourStyle},参数值为search_favour
	 * 
	 * 返回值为 {@link ArrayList},arraylist里面元素为数组,数组[0]为显示字段值(用单引号括起来，用逗号分隔)，[1]为回填值(用分号分隔)
	 * </pre>
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param userId 用户的ID，null的时候为不使用用户设置
	 * @return ArrayList 
	 * @see UserStyleServlet#getFavourStyle(String, String, String)
	 * @see #getCondition(String, Hashtable, ArrayList)
	 * @see DynDBManager#popSelect(String, String, PopupSelectBean, Hashtable, ArrayList, HashMap, ArrayList, int, int, String, String, String, String, Locale,
	 *      String, HttpServletRequest, String)
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<String[]> popupSelect(HttpServletRequest request, HttpServletResponse response,String userId){
		//用来返回的数据
		ArrayList<String[]> rows = new ArrayList<String[]>(); // 查结果集
		//表名
		String tableName = request.getParameter("tableName");
		//字段名
		String fieldName = request.getParameter("fieldName");
		//弹出窗名
		String selectName = request.getParameter("selectName");
		
		String selectField = request.getParameter("selectField");

		tableName = tableName == null ? "" : tableName.trim();
		fieldName = fieldName == null ? "" : fieldName.trim();
		selectField = selectField == null ? "" : selectField.trim();

		String[] str = selectField.split("_");
		if (str.length > 1) {
			selectField = str[str.length-2] + "." + str[str.length-1];
		}

		String temp = request.getParameter("selectValue").trim();
		if(request.getMethod().equals("GET"))
		temp = GlobalsTool.toChinseChar(temp);
		temp = GlobalsTool.encodeTextCode(temp);
		if(temp.endsWith(","))
			temp = temp.substring(0,temp.length()-1);

		// 用于有分类的弹出框
		String parentCode = request.getParameter("parentCode") == null ? "" : request.getParameter("parentCode");
		// 一般用于引用单据显示有明细表数据
		String mainId = request.getParameter("mainId") == null ? "" : request.getParameter("mainId");

		// 判断模块代号是否存在，如不存在则提示无来源模块
		String moduleId = request.getParameter("MOID");
		String moOperation = request.getParameter("MOOP");
	
		Hashtable<String,DBTableInfoBean> allTables = (Hashtable<String,DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

		Hashtable<?,EnumerateBean> enumeratemap = BaseEnv.enumerationMap;

		//弹出窗对应的Bean
		PopupSelectBean popSelectBean = null;
		// 如果页面传入的是弹出选择的名称，则直接处理
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// 如果页面传入的是字段名，则根据字段名查找出相应的弹出选择框
			popSelectBean = DDLOperation.getFieldInfo(allTables,tableName,fieldName).getSelectBean();
		}
		boolean hasTopPopup  = false;
		if(popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0){
			//这是一个有主弹出窗的弹窗
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
			hasTopPopup = true;
		}

		// 确定selectField(如果有别名命名)的名称
		for (PopField fv:popSelectBean.getDisplayField()) {
			if (fv.asName.equals(selectField)) {
				selectField = fv.fieldName;
				break;
			}
		}
		for (PopField fv: popSelectBean.getSaveFields()) {
			int pos = fv.parentName.indexOf(".");
			String name = "";
			if ( pos == -1) {
				name = fv.parentName;
			} else {
				name = tableName + fv.parentName.substring(pos);
			}
			if (name.equals(selectField)) {
				selectField = fv.fieldName;
			}
		}
		if (popSelectBean.getRelationKey() != null && selectField.equals(popSelectBean.getRelationKey().parentName)) {
			selectField = popSelectBean.getRelationKey().getAsName();
		}

		LoginBean login = (LoginBean) request.getSession().getAttribute("LoginBean");
		String sunCompanyID = login.getSunCmpClassCode();
		
		//数组顺序为 0 表名.字段名 1 值 2 对比(= like >= <=)
		ArrayList<String[]> param = new ArrayList<String[]>();
		//参加条件的字段
		ArrayList<PopField> fields = new ArrayList<PopField>(); 
		
		//先处理弹出框包含关键字搜索的情况
		if(popSelectBean.isKeySearch())
		{
			for (PopField popField : popSelectBean.getDisplayField2()) {
				if(popField.keySearch)
					fields.add(popField);
			}
		}else
		{
			for (PopField popField : popSelectBean.getDisplayField2()) {
				if(popField.getSearchType()!=0 && popField.getSearchType()!= PopField.SEARCH_SCOPE){
					//没有keyword的情况下，要模糊匹配所有条件字段，但是要除去数据型和范围型
					DBFieldInfoBean dfb = GlobalsTool.getFieldBean(popField.getFieldName()) ;
					if(dfb==null || (dfb.getFieldType()!=DBFieldInfoBean.FIELD_INT && dfb.getFieldType()!=DBFieldInfoBean.FIELD_DOUBLE))
						fields.add(popField);
				}
			}
		}
		//以下处理参与数据过滤的字段,根据个性化设置，把条件拼起来
		if(userId!=null)
		{	
			Map<String, String> m2 = new HashMap<String, String>();
			if(request.getParameter("search_favour") != null && request.getParameter("search_favour").length() > 0 ){
				m2.put("search_favour", request.getParameter("search_favour"));
			}else{
				m2 = UserStyleServlet.getFavourStyle(userId, popSelectBean.getName().trim(), "search_favour");
			}
			if(m2.get("search_favour")!=null)//如果用户作过设置，则以设置作筛选条件
			{
				Map<String, String> m = new Gson().fromJson(m2.get("search_favour"), m2.getClass());
				if(!m.get("searchfield").equals(""))//如果筛选了字段
				{
					PopField tmpPopField = null;
					for (PopField popField : fields) {
						if(popField.getFieldName().equals(m.get("searchfield")))//如果找到字段
						{
							tmpPopField = popField;
							break;
						}
					}
					if(tmpPopField!=null)
					{
						fields.clear();
						fields.add(tmpPopField);
					}
				}
				if(m.get("searchtype").equals("checked"))//如果选择了等于
				{
					ArrayList<PopField> fields2 = new ArrayList<PopField>();
					for (PopField popField : fields) {
						try {
							PopField p = popField.clone();
							p.setSearchType(PopField.SEARCH_EQUAL);
							fields2.add(p);
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					fields = fields2;
				}
			}
		}
		//如果没有keysearch则按displayField条件进行过滤
		param = getCondition(temp, allTables, fields);
		if(userId!=null)
			temp = "";
		ArrayList<String> tabParam = popSelectBean.getTableParams();
		//获取产生弹出窗界面上的各个输入框的值
		HashMap<String, String> mainParam = new HashMap<String, String>();
		String mainField = "";
		String value = "";
		for (int i = 0; i < tabParam.size(); i++) {
			mainField = tabParam.get(i).toString();
			if (mainField.indexOf("@TABLENAME") >= 0) {
				mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
			}
			value = request.getParameter(mainField) == null ? "" : request.getParameter(mainField);
			mainParam.put(mainField,value);
			try {
				mainParam.put(mainField, java.net.URLDecoder.decode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		// 根据模块代号取得查询时的范围权限
		MOperation mop = (MOperation) (login.getOperationMapKeyId().get(moduleId));
		ArrayList<LoginScopeBean> scopeRight = new ArrayList<LoginScopeBean>();
		if (mop != null) {
			scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,
	                tableName);
			if (tableInfo != null && tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
				// 如果是兄弟表,主表的查询范围权限加上明细表的范围权限
				MOperation mopDet = (MOperation) login.getOperationMap()
						.get("/UserFunctionQueryAction.do?parentTableName=" + "&tableName=" + tableName);
				if (mopDet != null) {
					scopeRight.addAll(mopDet.queryScope);
				}
			} 
		}
		// 加入所有分类权限
		scopeRight.addAll(mop.classScope);
		// 加入公共权限
		ArrayList<LoginScopeBean> allScopeList = login.getAllScopeRight();
		scopeRight.addAll(allScopeList);
		
		DynDBManager dyn = new DynDBManager();
		popSelectBean.setPopEnter(true);
		int tmpPage = 1;
		if(request.getParameter("pageNo")!=null)
		{
			try {
				tmpPage = Integer.parseInt(request.getParameter("pageNo"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		//查询数据库，并返回前100条数据
		Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, 100, parentCode, mainId, login.getId(),
				sunCompanyID, GlobalsTool.getLocale(request), temp, request, "",popSelectBean.isSaveParentFlag()?0:PopupSelectBean.LEAFRULE,"");

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//存储回填字段在getAllFields中的位置，用来把获得的数据按规则生成回填的数组
			ArrayList<Integer> retFieldPos = new ArrayList<Integer>();
			for (PopField fv: popSelectBean.getReturnFields()) {
				for(int i = 0 ;i < popSelectBean.getAllFields().size();i++)
				{
					PopField fv2 = popSelectBean.getAllFields().get(i);
					if(fv.getFieldName().equals(fv2.getFieldName()))
					{
						retFieldPos.add(i);
						break;
					}
				}
			}
			//存储显示字段在getAllFields中的位置,用来把获得的数据按规则生成显示的数组
			ArrayList<Integer> displayFieldPos = new ArrayList<Integer>();
			for (PopField fv: popSelectBean.getDisplayField()) {
				if(fv.fieldType == 13){
					continue;
				}
				for(int i = 0 ;i < popSelectBean.getAllFields().size();i++)
				{
					PopField fv2 = popSelectBean.getAllFields().get(i);
					if(fv.getFieldName().equals(fv2.getFieldName()))
					{
						displayFieldPos.add(i);
						break;
					}
				}
			}
			
			for (Object[] os : (List<Object[]>)rs.retVal) {
				String[] ss = new String[2];
				ss[0] = ""; // 保存需显示的值
				ss[1] = ""; // 保存需返回的值
				// 先找出需返回的值
				for(Integer k :retFieldPos){
					String osstr = os[k.intValue()] + "";
					PopField fv = popSelectBean.getAllFields().get(k.intValue());
					String display = fv.fieldName;
					DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					if (tempfib == null) {
						display = fv.parentName;
						
						if(display==null || display.length() ==0){
                    		display = fv.getDisplay();
                    	}
                    	if(display==null || display.length() ==0){
                			BaseEnv.log.info("--------------弹出窗调试信息：弹出窗"+popSelectBean.getName()+"返回字段'"+fv.getFieldName()+"'在表构中不存，且未指定parentName或display");
                		}
                    	if(tableName!=null){                                		
                    		display=display.replace("@TABLENAME", tableName);
                    	}
						
						
						tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					}
					if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						if (osstr == null || osstr.equals("null") || osstr.length() == 0) {
							osstr = "0";
						}
						osstr = GlobalsTool.formatNumberS(new Double(osstr), false, false, "", display);
					}
					osstr = GlobalsTool.revertTextCode2(osstr);
					ss[1] +=  (hasTopPopup?"TOPID:":"")+osstr.trim() + "#;#";
				}
				// 找出需显示的值
				for(Integer k :displayFieldPos){
					String osstr = os[k.intValue()] + "";
					PopField fv = popSelectBean.getAllFields().get(k.intValue());
					String display = fv.asName;
					DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					
					if (tempfib != null && tempfib.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
						osstr = getEnumerationItems(enumeratemap, tempfib.getRefEnumerationName(), osstr, GlobalsTool.getLocale(request).toString());
					} else if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						if (osstr.equals("null") || osstr.equals("")) {
							osstr = "0";
						}
						osstr = GlobalsTool.newFormatNumber(new Double(osstr), false, true,
								"true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), popSelectBean.getTableName(fv.fieldName),
								popSelectBean.getFieldName(fv.fieldName), true);
					}
					if (osstr != null && osstr.length() == 0) {
						osstr = "&nbsp;";
					}
					osstr = GlobalsTool.revertTextCode2(osstr);
					ss[0] +=  "'"+osstr.trim() + "',";
				}
				rows.add(ss);
			}
		}
		return rows;
	}
	
	private static String popupChildSelect(HttpServletRequest request, HttpServletResponse response,String userId){
		
		//表名
		String tableName = request.getParameter("tableName");
		//字段名
		String fieldName = request.getParameter("fieldName");
		//弹出窗名
		String selectName = request.getParameter("selectName");
		
		String selectField = request.getParameter("selectField");

		tableName = tableName == null ? "" : tableName.trim();
		fieldName = fieldName == null ? "" : fieldName.trim();
		selectField = selectField == null ? "" : selectField.trim();

		String[] str = selectField.split("_");
		if (str.length > 1) {
			selectField = str[str.length-2] + "." + str[str.length-1];
		}

		

		// 用于有分类的弹出框
		String parentCode = request.getParameter("parentCode") == null ? "" : request.getParameter("parentCode");
		// 一般用于引用单据显示有明细表数据
		String mainId = request.getParameter("mainId") == null ? "" : request.getParameter("mainId");

		// 判断模块代号是否存在，如不存在则提示无来源模块
		String moduleId = request.getParameter("MOID");
		String moOperation = request.getParameter("MOOP");
	
		Hashtable<String,DBTableInfoBean> allTables = (Hashtable<String,DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

		Hashtable<?,EnumerateBean> enumeratemap = BaseEnv.enumerationMap;

		//弹出窗对应的Bean
		PopupSelectBean popSelectBean = null;
		// 如果页面传入的是弹出选择的名称，则直接处理
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// 如果页面传入的是字段名，则根据字段名查找出相应的弹出选择框
			popSelectBean = DDLOperation.getFieldInfo(allTables,tableName,fieldName).getSelectBean();
		}
		

		// 确定selectField(如果有别名命名)的名称
		for (PopField fv:popSelectBean.getDisplayField()) {
			if (fv.asName.equals(selectField)) {
				selectField = fv.fieldName;
				break;
			}
		}
		for (PopField fv: popSelectBean.getSaveFields()) {
			int pos = fv.parentName.indexOf(".");
			String name = "";
			if ( pos == -1) {
				name = fv.parentName;
			} else {
				name = tableName + fv.parentName.substring(pos);
			}
			if (name.equals(selectField)) {
				selectField = fv.fieldName;
			}
		}
		if (popSelectBean.getRelationKey() != null && selectField.equals(popSelectBean.getRelationKey().parentName)) {
			selectField = popSelectBean.getRelationKey().getAsName();
		}

		LoginBean login = (LoginBean) request.getSession().getAttribute("LoginBean");
		String sunCompanyID = login.getSunCmpClassCode();
		
		//数组顺序为 0 表名.字段名 1 值 2 对比(= like >= <=)
		ArrayList<String[]> param = new ArrayList<String[]>();
		//参加条件的字段
		ArrayList<PopField> fields = new ArrayList<PopField>(); 
		
		//先处理弹出框包含关键字搜索的情况
		if(popSelectBean.isKeySearch())
		{
			for (PopField popField : popSelectBean.getDisplayField2()) {
				if(popField.keySearch)
					fields.add(popField);
			}
		}else
		{
			for (PopField popField : popSelectBean.getDisplayField2()) {
				if(popField.getSearchType()!=0 && popField.getSearchType()!= PopField.SEARCH_SCOPE){
					//没有keyword的情况下，要模糊匹配所有条件字段，但是要除去数据型和范围型
					DBFieldInfoBean dfb = GlobalsTool.getFieldBean(popField.getFieldName()) ;
					if(dfb==null || (dfb.getFieldType()!=DBFieldInfoBean.FIELD_INT && dfb.getFieldType()!=DBFieldInfoBean.FIELD_DOUBLE))
						fields.add(popField);
				}
			}
		}
		
		//如果没有keysearch则按displayField条件进行过滤
		param = new ArrayList();
		
		ArrayList<String> tabParam = popSelectBean.getTableParams();
		//获取产生弹出窗界面上的各个输入框的值
		HashMap<String, String> mainParam = new HashMap<String, String>();
		String mainField = "";
		String value = "";
		for (int i = 0; i < tabParam.size(); i++) {
			mainField = tabParam.get(i).toString();
			if (mainField.indexOf("@TABLENAME") >= 0) {
				mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
			}
			value = request.getParameter(mainField) == null ? "" : request.getParameter(mainField);
			mainParam.put(mainField,value);
			try {
				mainParam.put(mainField, java.net.URLDecoder.decode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		// 根据模块代号取得查询时的范围权限
		MOperation mop = (MOperation) (login.getOperationMapKeyId().get(moduleId));
		ArrayList<LoginScopeBean> scopeRight = new ArrayList<LoginScopeBean>();
		if (mop != null) {
			scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,
	                tableName);
			if (tableInfo != null && tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
				// 如果是兄弟表,主表的查询范围权限加上明细表的范围权限
				MOperation mopDet = (MOperation) login.getOperationMap()
						.get("/UserFunctionQueryAction.do?parentTableName=" + "&tableName=" + tableName);
				if (mopDet != null) {
					scopeRight.addAll(mopDet.queryScope);
				}
			} 
		}
		// 加入所有分类权限
		scopeRight.addAll(mop.classScope);
		// 加入公共权限
		ArrayList<LoginScopeBean> allScopeList = login.getAllScopeRight();
		scopeRight.addAll(allScopeList);
		
		DynDBManager dyn = new DynDBManager();
		popSelectBean.setPopEnter(true);
		int tmpPage = 1;
		if(request.getParameter("pageNo")!=null)
		{
			try {
				tmpPage = Integer.parseInt(request.getParameter("pageNo"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		String topId = request.getParameter("topId");
		if(topId != null && topId.startsWith("TOPID:")){
			topId = topId.substring(6);
			if(topId.indexOf("#;#") > 0){
				topId = topId.substring(0,topId.indexOf("#;#"));
			}
		}
		//查询数据库，并返回前100条数据
		Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, 10000, parentCode, mainId, login.getId(),
				sunCompanyID, GlobalsTool.getLocale(request), "", request, "",popSelectBean.isSaveParentFlag()?0:PopupSelectBean.LEAFRULE,topId);
		String retStr = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//存储回填字段在getAllFields中的位置，用来把获得的数据按规则生成回填的数组
			String returnCol = "";
	        for (int j = 0; j < popSelectBean.getReturnFields().size(); j++) {
	            PopField fv = (PopField) popSelectBean.getReturnFields(). get(j);
	                for (int k = 0; k < popSelectBean.getAllFields().size(); k++) {
	                    PopField fv2 = (PopField) popSelectBean.getAllFields().get(k);
	                    if (fv.getFieldName().equals(fv2.getFieldName())) {
	                    	String fvn="";
	                    	if(fv.type==1){
	                    		fvn = fv.parentName;
	                    		if(fvn == null || fvn.length() == 0){
	                        		fvn = fv.fieldName;	                        		
	                        	}                        	
	                    	}else{
	                        	fvn =  fv.asName;
	                        	if(fvn == null || fvn.length() == 0){
	                        		fvn = fv.fieldName;	                        		
	                        	}
	                    	}
	                    	returnCol +=fvn+";";
	                    	break ;
	                    }
	                }
	        }
			
			ArrayList<Integer> retFieldPos = new ArrayList<Integer>();
			for (PopField fv: popSelectBean.getReturnFields()) {
				for(int i = 0 ;i < popSelectBean.getAllFields().size();i++)
				{
					PopField fv2 = popSelectBean.getAllFields().get(i);
					if(fv.getFieldName().equals(fv2.getFieldName()))
					{
						retFieldPos.add(i);
						break;
					}
				}
			}
			//用来返回的数据
			String rowStr = "";
			
			for (Object[] os : (List<Object[]>)rs.retVal) {
				String[] ss = new String[2];
				ss[0] = ""; // 保存需显示的值
				ss[1] = ""; // 保存需返回的值
				// 先找出需返回的值
				for(Integer k :retFieldPos){
					String osstr = os[k.intValue()] + "";
					PopField fv = popSelectBean.getAllFields().get(k.intValue());
					String display = fv.fieldName;
					DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					if (tempfib == null && fv.display !=null && fv.display.indexOf(".")>-1) {
						display = fv.display;
						if (tableName != null) {
							display = display.replace("@TABLENAME", tableName);
						}
						tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					}
					if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						if (osstr == null || osstr.equals("null") || osstr.length() == 0) {
							osstr = "0";
						}
						osstr = GlobalsTool.formatNumberS(new Double(osstr), false, false, "", display);
					}
					osstr = GlobalsTool.revertTextCode2(osstr);
					osstr = GlobalsTool.rereplaceSpecLitter(osstr);
					ss[1] +=  osstr.trim() + "#;#";
				}
				rowStr +=ss[1]+"#|#";
			}
			
			retStr = returnCol+"::::"+rowStr;
		}
		return retStr;
	}
	/**<pre>
	 * 根据关键字和配置中的字段属性生成条件参数
	 * </pre>
	 * @param temp 关键字
	 * @param allTables 所有表的集合
	 * @param fvs 弹出字段集合
	 * @param param 要返回的参数
	 */
	public static ArrayList<String[]> getCondition(String temp,Hashtable<String, DBTableInfoBean> allTables, ArrayList<PopField> fvs) {
		ArrayList<String[]> param = new ArrayList<String[]>();
		for(PopField fv:fvs){
			String tmpKey = temp;
			String joinCondition = "or";
			int checkDefault = 0;
			// 找出查询条件
			do {
				if (fv.getSearchType() == PopField.SEARCH_EQUAL) {
					param.add(new String[] { fv.getFieldName(), tmpKey, "=", joinCondition });
				} else if (fv.getSearchType() == PopField.SEARCH_MATCH || fv.getSearchType() == PopField.SEARCH_MATCHL
						|| fv.getSearchType() == PopField.SEARCH_MATCHR) {
					if (fv.getSearchType() == PopField.SEARCH_MATCH) {
						param.add(new String[] { fv.getFieldName(), "%" + tmpKey + "%", "like", joinCondition });
					} else if (fv.getSearchType() == PopField.SEARCH_MATCHL) {
						param.add(new String[] { fv.getFieldName(), tmpKey + "%", "like", joinCondition });
					} else if (fv.getSearchType() == PopField.SEARCH_MATCHR) {
						param.add(new String[] { fv.getFieldName(), "%" + tmpKey, "like", joinCondition });
					}
				} else if (fv.getSearchType() == PopField.SEARCH_SCOPE) {
					// 0多语言；1表名.字段名；2值
					DBFieldInfoBean popfieldInfo = DDLOperation.getField(allTables, fv.getFieldName());
					if (popfieldInfo == null) {
						continue;
					}
					param.add(new String[] { fv.getFieldName(), tmpKey, ">=", joinCondition });
					// -----结束
					param.add(new String[] { fv.getFieldName(), tmpKey, "<=", joinCondition });
				}
				if(checkDefault<2)
				{
					joinCondition = "and";
					//tmpKey = fv.getDefaultValue(); //zxy 下拉选择的不用取默认值，这是根据条件来的，这只是弹出窗在没有条件时有默认值
					tmpKey = "";
					checkDefault ++;
				}
			} while (checkDefault<2 && tmpKey!=null && !tmpKey.equals(""));
		}
		ArrayList<String[]> param2 = new ArrayList<String[]>();//按条件把and的放在前面，or的放在后面
		for (String[] strings : param) {
			if(strings[3].equals("and"))
				param2.add(0,strings);
			else
				param2.add(strings);
		}
		return param2;
	}

	/**
	 * 获取枚举型对应的字符串值
	 * @param enumMap 包含枚举名在内的枚兴举集合
	 * @param enumeration 枚举名
	 * @param value 枚举的原始值
	 * @param locale 语言
	 * @return 获取某个枚举名对应的原始值的显示字符串
	 */
	private static String getEnumerationItems(Hashtable<?,EnumerateBean> enumMap, String enumeration, String value, String locale) {
		EnumerateBean beans[] =  enumMap.values().toArray(new EnumerateBean[0]);
		for (EnumerateBean eb : beans) {
			if (eb.getEnumName().equals(enumeration)) {
				for (int j = 0; j < eb.getEnumItem().size(); j++) {
					EnumerateItemBean eib = (EnumerateItemBean) eb.getEnumItem().get(j);
					if (eib.getEnumValue().equals(value)) {
						return ((KRLanguage) (eib.getDisplay())).get(locale);
					}
				}
				break;
			}
		}
		return "";
	}
}
