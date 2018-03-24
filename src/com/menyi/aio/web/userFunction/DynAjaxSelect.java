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
 * Description: �첽��ѯ������
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: �������
 * </p>
 * 
 * @author ����
 * @version 1.0
 */
public class DynAjaxSelect extends DynDBManager {
	/**
	 * ������������ʾ�ļ�¼��
	 */
	private static final int DROPDOWNCOUNT = 100;
	
	private DynAjaxSelect() {
	}

	/**
	 * <pre>
	 * ��ȡ�����Ķ������ݣ�ֻ����ǰ100����¼
	 * �����ַ�����ÿ������¼�� <b>|</b> �ֿ�����¼��ÿ���ֶ�ֵ�� <b>��</b>�ֿ�
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
			result = result + os[1].toString() + "|"; // �����
		}
		return result;
	}
	/**
	 * <pre>
	 * ��ȡ�����Ķ������ݣ�ֻ����ǰ{@value #DROPDOWNCOUNT}����¼
	 * �����ַ���ΪJSON�� һάΪ��¼��2άΪ [��ʾ���ݣ���������],�������ݼ�{@link #popupSelect(HttpServletRequest, HttpServletResponse)}
	 * </pre>	 
	 * @param request
	 * @param response
	 * @return
	 * @see #popupSelect(HttpServletRequest, HttpServletResponse) 
	 */
	public static String DropdownPopup(HttpServletRequest request, HttpServletResponse response){
		ArrayList<String[]> list = popupSelect(request, response,GlobalsTool.getLoginBean(request).getId());
		if(list.size() > DROPDOWNCOUNT)//���������Ҫ��ʾ�ļ�¼������ֻȡ���ݵ�ǰ����
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
	 * ��ȡ�ӵ����Ķ������ݣ�
	 * �����ַ���ΪJSON�� һάΪ��¼��2άΪ [��ʾ���ݣ���������],�������ݼ�{@link #popupSelect(HttpServletRequest, HttpServletResponse)}
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
	 * ��ȡ�����ĵ������ݣ�������Ӧrequest�Ĳ��� <b>selectValue</b>
	 * �����ѯ�󷵻ز���һ�������򷵻��ַ���  "@condition:"+[����]
	 * ���ֻ����һ�������򷵻���ֵ���÷ֺŷַָ�
	 * </pre>
	 * @param request
	 * @param response
	 * @return
	 * @see #popupSelect(HttpServletRequest, HttpServletResponse) 
	 * @see UtilServlet#dynSelect(HttpServletRequest, HttpServletResponse) ��̬���������ݲ�ѯ
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
				result = os[1]; // �����
		} else {
			result = "@condition:" + selectValue; // ����ѯ����
		}
		return result;
	}
	/**
	 * �ɵ������������+�ֶΣ���Ҫ��ʾ���ֶ���ʾ��\�����ֶ�\�����ֶ�\���Ի����� ����JSON���� 
	 * @param allTables �Զ����
	 * @param selectName ������������ΪNULL�򡰡�ʱ����tableName,fieldNameȡ�õ�����
	 * @param tableName ����
	 * @param fieldName �ֶ��� ���������ֶ��������Զ����п���ȡ�õ�����
	 * @param local ����
	 * @param userId �û���ʶ
	 * @return
	 */
	public static String showView(Hashtable<String,DBTableInfoBean> allTables,String selectName,String tableName,String fieldName,Locale locale,String userId)
	{
		Map<String,ArrayList<Map<String,String>>> data = new HashMap<String, ArrayList<Map<String,String>>>();
		//��������Ӧ��Bean
		PopupSelectBean popSelectBean = null;
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// ���ҳ�洫������ֶ�����������ֶ������ҳ���Ӧ�ĵ���ѡ���
			popSelectBean = DDLOperation.getFieldInfo(allTables,tableName,fieldName).getSelectBean();
		}
		if(popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0){
			//����һ�������������ĵ���
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
		}
		ArrayList<Map<String,String>> fields = new ArrayList<Map<String,String>>();
		//����Ϊ��ʾ�ֶ�
		for(PopField field:popSelectBean.getDisplayField())
		{
			//��ʾ�ֶβ�����ͼƬ
			if(13 ==field.fieldType){
				continue;
			}
			Map<String,String> map = new HashMap<String, String>();
			map.put("displayname", field.getDisplayLocale(tableName, allTables, locale));
			map.put("width", String.valueOf(field.getWidth()));
			fields.add(map);
		}
		data.put("showfields", fields);
		//����Ϊ�����ֶ�
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
		//================================����Ϊ�����ֶ�
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
	 * ��ȡ��������Ӧ���ݵ�����,ֻ����ǰ100��
	 * ��������������ʽ���(1����������(selectName) 2������(tableName)���ֶ���(fieldName)�ٴ��Զ���������л�ȡ���Ӧ�ĵ�����)
	 * �ؼ��ֲ�ѯ�ֳ����֣�
	 * 	<li>����������keySearch��ʱ�򣬰���keySearch�������ֶν�������</li>
	 *  <li>��������û��keySearch��ʱ�򣬰��е������ֶν�������</li>
	 * ���йؼ�������������ֵ������Զ��ŷָ�����ôȥ�����š�
	 * userIdΪnullʱ��ʾ��ʹ���û��Զ�������
	 * �û��Զ�������ʹ���� {@link UserStyleServlet#getFavourStyle},����ֵΪsearch_favour
	 * 
	 * ����ֵΪ {@link ArrayList},arraylist����Ԫ��Ϊ����,����[0]Ϊ��ʾ�ֶ�ֵ(�õ��������������ö��ŷָ�)��[1]Ϊ����ֵ(�÷ֺŷָ�)
	 * </pre>
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param userId �û���ID��null��ʱ��Ϊ��ʹ���û�����
	 * @return ArrayList 
	 * @see UserStyleServlet#getFavourStyle(String, String, String)
	 * @see #getCondition(String, Hashtable, ArrayList)
	 * @see DynDBManager#popSelect(String, String, PopupSelectBean, Hashtable, ArrayList, HashMap, ArrayList, int, int, String, String, String, String, Locale,
	 *      String, HttpServletRequest, String)
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<String[]> popupSelect(HttpServletRequest request, HttpServletResponse response,String userId){
		//�������ص�����
		ArrayList<String[]> rows = new ArrayList<String[]>(); // ������
		//����
		String tableName = request.getParameter("tableName");
		//�ֶ���
		String fieldName = request.getParameter("fieldName");
		//��������
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

		// �����з���ĵ�����
		String parentCode = request.getParameter("parentCode") == null ? "" : request.getParameter("parentCode");
		// һ���������õ�����ʾ����ϸ������
		String mainId = request.getParameter("mainId") == null ? "" : request.getParameter("mainId");

		// �ж�ģ������Ƿ���ڣ��粻��������ʾ����Դģ��
		String moduleId = request.getParameter("MOID");
		String moOperation = request.getParameter("MOOP");
	
		Hashtable<String,DBTableInfoBean> allTables = (Hashtable<String,DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

		Hashtable<?,EnumerateBean> enumeratemap = BaseEnv.enumerationMap;

		//��������Ӧ��Bean
		PopupSelectBean popSelectBean = null;
		// ���ҳ�洫����ǵ���ѡ������ƣ���ֱ�Ӵ���
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// ���ҳ�洫������ֶ�����������ֶ������ҳ���Ӧ�ĵ���ѡ���
			popSelectBean = DDLOperation.getFieldInfo(allTables,tableName,fieldName).getSelectBean();
		}
		boolean hasTopPopup  = false;
		if(popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0){
			//����һ�������������ĵ���
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
			hasTopPopup = true;
		}

		// ȷ��selectField(����б�������)������
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
		
		//����˳��Ϊ 0 ����.�ֶ��� 1 ֵ 2 �Ա�(= like >= <=)
		ArrayList<String[]> param = new ArrayList<String[]>();
		//�μ��������ֶ�
		ArrayList<PopField> fields = new ArrayList<PopField>(); 
		
		//�ȴ�����������ؼ������������
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
					//û��keyword������£�Ҫģ��ƥ�����������ֶΣ�����Ҫ��ȥ�����ͺͷ�Χ��
					DBFieldInfoBean dfb = GlobalsTool.getFieldBean(popField.getFieldName()) ;
					if(dfb==null || (dfb.getFieldType()!=DBFieldInfoBean.FIELD_INT && dfb.getFieldType()!=DBFieldInfoBean.FIELD_DOUBLE))
						fields.add(popField);
				}
			}
		}
		//���´���������ݹ��˵��ֶ�,���ݸ��Ի����ã�������ƴ����
		if(userId!=null)
		{	
			Map<String, String> m2 = new HashMap<String, String>();
			if(request.getParameter("search_favour") != null && request.getParameter("search_favour").length() > 0 ){
				m2.put("search_favour", request.getParameter("search_favour"));
			}else{
				m2 = UserStyleServlet.getFavourStyle(userId, popSelectBean.getName().trim(), "search_favour");
			}
			if(m2.get("search_favour")!=null)//����û��������ã�����������ɸѡ����
			{
				Map<String, String> m = new Gson().fromJson(m2.get("search_favour"), m2.getClass());
				if(!m.get("searchfield").equals(""))//���ɸѡ���ֶ�
				{
					PopField tmpPopField = null;
					for (PopField popField : fields) {
						if(popField.getFieldName().equals(m.get("searchfield")))//����ҵ��ֶ�
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
				if(m.get("searchtype").equals("checked"))//���ѡ���˵���
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
		//���û��keysearch��displayField�������й���
		param = getCondition(temp, allTables, fields);
		if(userId!=null)
			temp = "";
		ArrayList<String> tabParam = popSelectBean.getTableParams();
		//��ȡ���������������ϵĸ���������ֵ
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

		// ����ģ�����ȡ�ò�ѯʱ�ķ�ΧȨ��
		MOperation mop = (MOperation) (login.getOperationMapKeyId().get(moduleId));
		ArrayList<LoginScopeBean> scopeRight = new ArrayList<LoginScopeBean>();
		if (mop != null) {
			scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,
	                tableName);
			if (tableInfo != null && tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
				// ������ֵܱ�,����Ĳ�ѯ��ΧȨ�޼�����ϸ��ķ�ΧȨ��
				MOperation mopDet = (MOperation) login.getOperationMap()
						.get("/UserFunctionQueryAction.do?parentTableName=" + "&tableName=" + tableName);
				if (mopDet != null) {
					scopeRight.addAll(mopDet.queryScope);
				}
			} 
		}
		// �������з���Ȩ��
		scopeRight.addAll(mop.classScope);
		// ���빫��Ȩ��
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
		//��ѯ���ݿ⣬������ǰ100������
		Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, 100, parentCode, mainId, login.getId(),
				sunCompanyID, GlobalsTool.getLocale(request), temp, request, "",popSelectBean.isSaveParentFlag()?0:PopupSelectBean.LEAFRULE,"");

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//�洢�����ֶ���getAllFields�е�λ�ã������ѻ�õ����ݰ��������ɻ��������
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
			//�洢��ʾ�ֶ���getAllFields�е�λ��,�����ѻ�õ����ݰ�����������ʾ������
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
				ss[0] = ""; // ��������ʾ��ֵ
				ss[1] = ""; // �����践�ص�ֵ
				// ���ҳ��践�ص�ֵ
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
                			BaseEnv.log.info("--------------������������Ϣ��������"+popSelectBean.getName()+"�����ֶ�'"+fv.getFieldName()+"'�ڱ��в��棬��δָ��parentName��display");
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
				// �ҳ�����ʾ��ֵ
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
		
		//����
		String tableName = request.getParameter("tableName");
		//�ֶ���
		String fieldName = request.getParameter("fieldName");
		//��������
		String selectName = request.getParameter("selectName");
		
		String selectField = request.getParameter("selectField");

		tableName = tableName == null ? "" : tableName.trim();
		fieldName = fieldName == null ? "" : fieldName.trim();
		selectField = selectField == null ? "" : selectField.trim();

		String[] str = selectField.split("_");
		if (str.length > 1) {
			selectField = str[str.length-2] + "." + str[str.length-1];
		}

		

		// �����з���ĵ�����
		String parentCode = request.getParameter("parentCode") == null ? "" : request.getParameter("parentCode");
		// һ���������õ�����ʾ����ϸ������
		String mainId = request.getParameter("mainId") == null ? "" : request.getParameter("mainId");

		// �ж�ģ������Ƿ���ڣ��粻��������ʾ����Դģ��
		String moduleId = request.getParameter("MOID");
		String moOperation = request.getParameter("MOOP");
	
		Hashtable<String,DBTableInfoBean> allTables = (Hashtable<String,DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

		Hashtable<?,EnumerateBean> enumeratemap = BaseEnv.enumerationMap;

		//��������Ӧ��Bean
		PopupSelectBean popSelectBean = null;
		// ���ҳ�洫����ǵ���ѡ������ƣ���ֱ�Ӵ���
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// ���ҳ�洫������ֶ�����������ֶ������ҳ���Ӧ�ĵ���ѡ���
			popSelectBean = DDLOperation.getFieldInfo(allTables,tableName,fieldName).getSelectBean();
		}
		

		// ȷ��selectField(����б�������)������
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
		
		//����˳��Ϊ 0 ����.�ֶ��� 1 ֵ 2 �Ա�(= like >= <=)
		ArrayList<String[]> param = new ArrayList<String[]>();
		//�μ��������ֶ�
		ArrayList<PopField> fields = new ArrayList<PopField>(); 
		
		//�ȴ�����������ؼ������������
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
					//û��keyword������£�Ҫģ��ƥ�����������ֶΣ�����Ҫ��ȥ�����ͺͷ�Χ��
					DBFieldInfoBean dfb = GlobalsTool.getFieldBean(popField.getFieldName()) ;
					if(dfb==null || (dfb.getFieldType()!=DBFieldInfoBean.FIELD_INT && dfb.getFieldType()!=DBFieldInfoBean.FIELD_DOUBLE))
						fields.add(popField);
				}
			}
		}
		
		//���û��keysearch��displayField�������й���
		param = new ArrayList();
		
		ArrayList<String> tabParam = popSelectBean.getTableParams();
		//��ȡ���������������ϵĸ���������ֵ
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

		// ����ģ�����ȡ�ò�ѯʱ�ķ�ΧȨ��
		MOperation mop = (MOperation) (login.getOperationMapKeyId().get(moduleId));
		ArrayList<LoginScopeBean> scopeRight = new ArrayList<LoginScopeBean>();
		if (mop != null) {
			scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables,
	                tableName);
			if (tableInfo != null && tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
				// ������ֵܱ�,����Ĳ�ѯ��ΧȨ�޼�����ϸ��ķ�ΧȨ��
				MOperation mopDet = (MOperation) login.getOperationMap()
						.get("/UserFunctionQueryAction.do?parentTableName=" + "&tableName=" + tableName);
				if (mopDet != null) {
					scopeRight.addAll(mopDet.queryScope);
				}
			} 
		}
		// �������з���Ȩ��
		scopeRight.addAll(mop.classScope);
		// ���빫��Ȩ��
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
		//��ѯ���ݿ⣬������ǰ100������
		Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, 10000, parentCode, mainId, login.getId(),
				sunCompanyID, GlobalsTool.getLocale(request), "", request, "",popSelectBean.isSaveParentFlag()?0:PopupSelectBean.LEAFRULE,topId);
		String retStr = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//�洢�����ֶ���getAllFields�е�λ�ã������ѻ�õ����ݰ��������ɻ��������
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
			//�������ص�����
			String rowStr = "";
			
			for (Object[] os : (List<Object[]>)rs.retVal) {
				String[] ss = new String[2];
				ss[0] = ""; // ��������ʾ��ֵ
				ss[1] = ""; // �����践�ص�ֵ
				// ���ҳ��践�ص�ֵ
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
	 * ���ݹؼ��ֺ������е��ֶ�����������������
	 * </pre>
	 * @param temp �ؼ���
	 * @param allTables ���б�ļ���
	 * @param fvs �����ֶμ���
	 * @param param Ҫ���صĲ���
	 */
	public static ArrayList<String[]> getCondition(String temp,Hashtable<String, DBTableInfoBean> allTables, ArrayList<PopField> fvs) {
		ArrayList<String[]> param = new ArrayList<String[]>();
		for(PopField fv:fvs){
			String tmpKey = temp;
			String joinCondition = "or";
			int checkDefault = 0;
			// �ҳ���ѯ����
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
					// 0�����ԣ�1����.�ֶ�����2ֵ
					DBFieldInfoBean popfieldInfo = DDLOperation.getField(allTables, fv.getFieldName());
					if (popfieldInfo == null) {
						continue;
					}
					param.add(new String[] { fv.getFieldName(), tmpKey, ">=", joinCondition });
					// -----����
					param.add(new String[] { fv.getFieldName(), tmpKey, "<=", joinCondition });
				}
				if(checkDefault<2)
				{
					joinCondition = "and";
					//tmpKey = fv.getDefaultValue(); //zxy ����ѡ��Ĳ���ȡĬ��ֵ�����Ǹ����������ģ���ֻ�ǵ�������û������ʱ��Ĭ��ֵ
					tmpKey = "";
					checkDefault ++;
				}
			} while (checkDefault<2 && tmpKey!=null && !tmpKey.equals(""));
		}
		ArrayList<String[]> param2 = new ArrayList<String[]>();//��������and�ķ���ǰ�棬or�ķ��ں���
		for (String[] strings : param) {
			if(strings[3].equals("and"))
				param2.add(0,strings);
			else
				param2.add(strings);
		}
		return param2;
	}

	/**
	 * ��ȡö���Ͷ�Ӧ���ַ���ֵ
	 * @param enumMap ����ö�������ڵ�ö�˾ټ���
	 * @param enumeration ö����
	 * @param value ö�ٵ�ԭʼֵ
	 * @param locale ����
	 * @return ��ȡĳ��ö������Ӧ��ԭʼֵ����ʾ�ַ���
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
