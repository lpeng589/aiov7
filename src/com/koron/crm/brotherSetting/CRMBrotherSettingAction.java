package com.koron.crm.brotherSetting;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.bean.BrotherFieldDisplayBean;
import com.koron.crm.bean.BrotherFieldScopeBean;
import com.koron.crm.bean.BrotherPublicScopeBean;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.brother.CRMBrotherMgt;
import com.koron.crm.setting.ClientSetingMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ChineseSpelling;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>Title:兄弟表设置</p> 
 * <p>Description: </p>
 *
 * @Date:July 31, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class CRMBrotherSettingAction  extends BaseAction{
	CRMBrotherSettingMgt mgt = new CRMBrotherSettingMgt();
	CRMBrotherMgt brotherMgt = new CRMBrotherMgt();
	ClientSetingMgt clientSetingMgt = new ClientSetingMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		String type = getParameter("type", request);
		request.setAttribute("type", type);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			forward = addFieldScopePrepare(mapping, form, request, response);//显示字段设置添加前
			break;
		case OperationConst.OP_ADD:
			forward = addFieldScope(mapping, form, request, response);//字段只读，隐藏设置添加
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			if("publicScope".equals(type)){
				forward = updatePublicScopePrepare(mapping, form, request, response);//导入、导出更新前	
			}else if("maintain".equals(type)){
				forward = updateFieldMaintainPrepare(mapping, form, request, response);//字段维护更新前
			}else if("fieldScope".equals(type)){
				forward = updateFieldScopePrepare(mapping, form, request, response);//字段维护更新
			}else if("fieldMapping".equals(type)){
				forward = updateFieldMappingPrepare(mapping, form, request, response);//潜在客户字段映射
			}else{
				forward = updateFieldsDisplayPrepare(mapping, form, request, response);//显示字段设置更新前
			}
			break;
		case OperationConst.OP_UPDATE:
			if("publicScope".equals(type)){
				forward = updatePublicScope(mapping, form, request, response);//导入、导出更新
			}else if("maintain".equals(type)){
				forward = updateFieldMaintain(mapping, form, request, response);//字段维护更新
			}else if("fieldScope".equals(type)){
				forward = updateFieldScope(mapping, form, request, response);//字段维护更新
			}else if("fieldMapping".equals(type)){
				forward = updateFieldMapping(mapping, form, request, response);//潜在客户字段映射更新
			}else{
				forward = updateFieldsDisplay(mapping, form, request, response);//显示字段设置更新
			}
			break;
		case OperationConst.OP_DELETE:
			if("delField".equals(type)){
				forward = delField(mapping, form, request, response);	
			}else{
				forward = delFieldScope(mapping, form, request, response);//显示字段设置更新
			}
			break;
		
		
		case OperationConst.OP_QUERY:
			if("fieldScope".equals(type)){
				forward = fieldScopeQuery(mapping, form, request, response);//字段只读，隐藏列表
			}else{
				forward = query(mapping, form, request, response);
			}
			break;
		default:
			forward = query(mapping, form, request, response);
			break;
		}
		return forward;
	}
	
	/**
	 * 客户字段映射
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFieldMapping(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String moduleId=getParameter("moduleId", request);//模板ID
		String clientFieldsMapping=getParameter("clientFieldsMapping", request);//映射字段
		
    	Result rs = clientSetingMgt.detailCrmModule(moduleId);
    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
    		moduleBean.setClientFieldsMapping(clientFieldsMapping);
    		rs = clientSetingMgt.updateCrmModule(moduleBean);
    		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			return new ActionForward("/CRMBrotherSettingAction.do?type=fieldMapping&operation="+OperationConst.OP_UPDATE_PREPARE+"&moduleId="+moduleId,true);	
    		}else{
    			// 修改失败
    			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
    			return getForward(request, mapping, "message");
    		}
    	}else{
    		// 修改失败
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
			return getForward(request, mapping, "message");
    	}
	}

	/**
	 * 字段映射更新前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFieldMappingPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		LoginBean loginBean = getLoginBean(request);
		
		String moduleId=getParameter("moduleId", request);//模板ID
		
		Result rs = clientSetingMgt.getFilterModules(loginBean);//获取有权限的模板
		ArrayList<Object> moduleList = (ArrayList<Object>)rs.retVal;
		DBTableInfoBean potentialTableBean = GlobalsTool.getTableInfoBean("CRMPotentialClient");//潜在客户tableBean
		String clientFieldsMapping = "";//存放模板映射字段信息
		String tableName = "";
		String childTableName = "";
		
		if(moduleId!=null && !"".equals(moduleId)){
			rs = clientSetingMgt.detailCrmModule(moduleId);
			ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
			tableName = moduleBean.getTableInfo().split(":")[0];
			childTableName = moduleBean.getTableInfo().split(":")[1];
			clientFieldsMapping = moduleBean.getClientFieldsMapping();
		}else{
			//没有模板ID默认去第一个模板的映射信息
			if(moduleList!=null && moduleList.size()>0){
				String tableInfo = String.valueOf(GlobalsTool.get(moduleList.get(0),2));
				clientFieldsMapping = String.valueOf(GlobalsTool.get(moduleList.get(0),3));
				moduleId = String.valueOf(GlobalsTool.get(moduleList.get(0),0));
				if(tableInfo !=null && !"".equals(tableInfo)){
					tableName = tableInfo.split(":")[0];
					childTableName = tableInfo.split(":")[1];
				}
			}
		}

		DBTableInfoBean clientInfoTableBean = GlobalsTool.getTableInfoBean(tableName);
		DBTableInfoBean clientInfoDetTableBean = GlobalsTool.getTableInfoBean(childTableName);
		request.setAttribute("clientInfoTableBean",clientInfoTableBean);
		request.setAttribute("clientInfoDetTableBean",clientInfoDetTableBean);
		
		request.setAttribute("moduleList",moduleList);
		request.setAttribute("tableName",tableName);
		request.setAttribute("childTableName",childTableName);
		request.setAttribute("moduleId",moduleId);
		request.setAttribute("potentialTableBean",potentialTableBean);
		request.setAttribute("clientFieldsMapping",clientFieldsMapping);
		return getForward(request, mapping, "updateFieldMapping");
	}

	private ActionForward fieldScopeQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String tableName = getParameter("tableName",request);
		Result rs = mgt.fieldScopeQuery(tableName);
		
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//根据表名查找明细
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<BrotherFieldScopeBean> list = (ArrayList<BrotherFieldScopeBean>)rs.retVal;
			request.setAttribute("tableName",tableName);
			request.setAttribute("list",list);
			request.setAttribute("operationSuccess",getParameter("operationSuccess",request));
			if(childTableList!=null && childTableList.size()>0){
				request.setAttribute("childTableName",childTableList.get(0).getTableName());
			}
		}
		return getForward(request, mapping, "fieldScopeList");
	}


	/**
	 * 首页查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tableName = getParameter("tableName",request);
		request.setAttribute("tableName",tableName);
		return getForward(request, mapping, "index") ;
	}
	
	/**
	 * 字段维护更新前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFieldMaintainPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String tableName = getParameter("tableName",request);
		String childTableName ="";
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//根据表名查找明细
		
		if(childTableList!=null && childTableList.size()>0){
			childTableName = childTableList.get(0).getTableName();
			request.setAttribute("childTableName",childTableName);
			request.setAttribute("childFieldList",childTableList.get(0).getFieldInfos());
		}
		
		HashMap relateClientMap = new LinkedHashMap();
		for(DBFieldInfoBean bean : tableInfoBean.getFieldInfos()){
			if(bean.getInputType() == 20){
				Result rs = mgt.relateClientQuery(bean.getRefEnumerationName());
				ArrayList list = (ArrayList)rs.getRetVal();
				if(list !=null && list.size()>0){
					relateClientMap.put(bean.getFieldName(), list);
				}
			}
		}
		
		Result result = mgt.relateClientQuery("crmClient");
		
		Map map = BaseEnv.enumerationMap;
		EnumerateBean beans[] = (EnumerateBean[]) map.values().toArray(new EnumerateBean[0]);
		HashMap<String,String> enumerMap = new HashMap<String,String>();
		String enumerationId = "";
		for (int i = 0; i < beans.length; i++) {
			enumerMap.put(beans[i].getEnumName(), beans[i].getId());
		}
		request.setAttribute("enumerMap", enumerMap);
		request.setAttribute("relateClientOpetions",result.retVal);
		request.setAttribute("relateClientMap",relateClientMap);
		request.setAttribute("tableName",tableName);
		request.setAttribute("fieldList",tableInfoBean.getFieldInfos());
		request.setAttribute("operationSuccess",getParameter("operationSuccess",request));
		
		return getForward(request, mapping, "updateFieldMaintain");
	}

	/**
	 * 字段维护更新
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFieldMaintain(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String tableName = getParameter("tableName",request);//主表
		String childTableName = getParameter("childTableName",request);//明细表
		String updateInfo = getParameter("updateInfo",request);//获取更新信息
		String addMainFieldInfo = getParameter("addMainFieldInfo",request);//获取更新信息
		String addChildFieldInfo = getParameter("addChildFieldInfo",request);//获取更新信息
		ArrayList<String> updateSqlList = new ArrayList<String>();
		ArrayList<String> statusStopSqlList = new ArrayList<String>();//字段不启用，去掉记录显示字段
		String sql ="";//存放update语句
		
		//获取主表与明细表BEAN,用于新增字段插入
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean mainTableInfo = (DBTableInfoBean) allTables.get(tableName);
		DBTableInfoBean childTableInfo = new DBTableInfoBean();
		if(childTableName!=null && !"".equals(childTableName)){
			childTableInfo = (DBTableInfoBean) allTables.get(childTableName);
		}
		
		short mainCount = mainTableInfo.getFieldInfos().get(mainTableInfo.getFieldInfos().size()-1).getListOrder();//获得主表结构最后一个排序值用于新增字段的的排序值累加
		boolean reloadEnumer = false;//操作成功后是否重新加载枚举内存,默认false;
		
		Result rs = new Result();
		if(updateInfo!=null && !"".equals(updateInfo)){
			for(String updateStr : updateInfo.split(";")){
				String[] info = updateStr.split(":");
				if("language".equals(info[2])){
					sql = "UPDATE tblLanguage SET zh_CN='"+info[3]+"',zh_TW='"+info[3]+"' WHERE id=(SELECT languageId from tblDBFieldInfo WHERE tableId=(SELECT id FROM tblDBTableInfo WHERE tableName='"+info[0]+"') and fieldName='"+info[1]+"')";
				}else{
					if("statusId".equals(info[2]) && "0".equals(info[3])){
						String fieldName = info[1];
						String statusSql = "UPDATE CRMBrotherFieldDisplay SET keyFields = replace(keyFields,'"+fieldName+",',''),searchFields = replace(searchFields,'"+fieldName+",',''),listFields = replace(listFields,'"+fieldName+",',''),detailFields = replace(detailFields,'"+fieldName+",',''),pageFields = replace(pageFields,'"+fieldName+",',''),pageChildFields = replace(pageChildFields,'"+fieldName+",','') where id = '"+tableName+"'";
						statusStopSqlList.add(statusSql);
					}
					sql = "UPDATE tblDBFieldInfo SET "+info[2] +"='"+info[3]+"' WHERE tableId=(SELECT id FROM tblDBTableInfo WHERE tableName='"+info[0]+"') and fieldName='"+info[1]+"'";
				}
				updateSqlList.add(sql);
			}
		}
		
		//处理新增字段
		ArrayList<DBFieldInfoBean> saveBeanList = new ArrayList<DBFieldInfoBean>();
		ArrayList<String> languageList = new ArrayList<String>();
		String addMainPageFIelds = "";//字段启用,新增修改页面字段显示默认放出来(主表)
		String addChildPageFIelds = "";//字段启用,新增修改页面字段显示默认放出来（明细）
		if(addMainFieldInfo!=null && !"".equals(addMainFieldInfo)){
			for(String fieldInfo : addMainFieldInfo.split(";")){
				DBFieldInfoBean fieldBean = new DBFieldInfoBean();
				String[] field = fieldInfo.split("&&");
				String tableType = field[0];//main表示添加主表,child表示添加明细
				String fieldName = field[1];
				String inputType = field[2];
				String defaultValue = field[3];
				String width = field[4];
				String groupName = field[5];
				String statusId = field[6];
				String isNull = field[7];
				
				fieldBean.setGroupName(groupName);
				fieldBean.setStatusId(Integer.parseInt(statusId));
				fieldBean.setIsNull((byte)Integer.parseInt(isNull));
				fieldBean.setMaxLength(500);
				//处理名称
				//如果少于两个等于字母或汉字用全拼
				if(fieldName.length()<=2){
					fieldBean.setFieldName(ChineseSpelling.getSelling(fieldName));
				}else{
					fieldBean.setFieldName(CustomizePYM.getFirstLetter(fieldName));
				}

				//处理输入类型
				if("2".equals(inputType)){
					fieldBean.setInputType((byte)2);
					fieldBean.setFieldType((byte)2);
					//fieldBean.setInputValue(defineTableName[i]);
				}if("20".equals(inputType)){
					fieldBean.setInputType((byte)20);
					fieldBean.setFieldType((byte)2);
					fieldBean.setDefaultValue(defaultValue);
					fieldBean.setRefEnumerationName("crmClient");
					//fieldBean.setInputValue(defineTableName[i]);
				}else if(inputType.indexOf("fieldType") == -1){
					fieldBean.setInputType((byte)(Integer.parseInt(inputType)));
					fieldBean.setFieldType((byte)2);
				}else{
					fieldBean.setFieldType((byte)(Integer.parseInt(inputType.split(":")[1])));
					fieldBean.setInputType((byte)0);
				}
				
				
				//判断fieldName是否存在
				int colNum = mgt.checkCols(fieldBean.getFieldName(),tableName);//查看是否已经存在此字段
    			if(colNum>0){
    				colNum = colNum+1;
    				while(true){
    					int temp = mgt.checkCols(fieldBean.getFieldName()+String.valueOf(colNum),tableName);
    					if(temp==0){
    						//表示没有此字段
    						fieldBean.setFieldName(fieldBean.getFieldName()+String.valueOf(colNum));
    						break;
    					}else{
    						//有的话.默认后面递增	
    						colNum++;
    					}
    				}
    			}else{
    				int tempCount = 0;//临时变量,用于记录是否有中文不同英文字段相同个数
    				for(DBFieldInfoBean bean : saveBeanList){
    					if(bean.getFieldName().indexOf(fieldBean.getFieldName()) != -1){
    						tempCount ++;//存在+1
    					}
    				}
    				if(tempCount != 0){
    					fieldBean.setFieldName(fieldBean.getFieldName()+tempCount);
    				}
    			}
				
    			
    			String languageId = IDGenerater.getId();
				fieldBean.setId(IDGenerater.getId());
				
				fieldBean.setLanguageId(languageId);
				
				fieldBean.setLanguageId(languageId);
				fieldBean.setMaxLength(500);
				
				//存放本字段多语言
				languageList.add("insert into tblLanguage(id,zh_TW,zh_CN,en) values('"+languageId+"','"+fieldName+"','"+fieldName+"','"+fieldName+"')");
				
				
				
				mainCount++;
				fieldBean.setListOrder(mainCount);//序号
				//如果是枚举类型，给予默认选项值
				if("1".equals(inputType) || "5".equals(inputType)){
					fieldBean.setRefEnumerationName(this.getBeanEnumer(languageList,fieldBean.getFieldName()));
					reloadEnumer = true;//表示需要加载枚举
				}
				
				//处理启用字段
				if("1".equals(statusId)){
					if("main".equals(tableType)){
						fieldBean.setTableBean(mainTableInfo);//保存主表bean	
						addMainPageFIelds += fieldBean.getFieldName()+",";
						languageList.add("alter table "+tableName+" add "+fieldBean.getFieldName()+" varchar(1000)");//添加主表物理表字段
					}else{
						fieldBean.setTableBean(childTableInfo);//保存明细表bean
						addChildPageFIelds += fieldBean.getFieldName()+",";
						languageList.add("alter table "+childTableName+" add "+fieldBean.getFieldName()+" varchar(1000)");//添加明细物理表字段
					}
				}
				saveBeanList.add(fieldBean);
			}
			
		}
		
		//处理新增启用字段，默认在新增修改显示字段放出来(主表)
		if(!"".equals(addMainPageFIelds)){
			String addPageSql = "UPDATE CRMBrotherFieldDisplay SET pageFields= isnull(pageFields,'')+'"+addMainPageFIelds+"' WHERE id='"+tableName+"'";
			languageList.add(addPageSql);
		}
		
		//处理新增启用字段，默认在新增修改显示字段放出来(明细)
		if(!"".equals(addChildPageFIelds)){
			String addPageSql = "UPDATE CRMBrotherFieldDisplay SET pageChildFields= isnull(pageChildFields,'')+'"+addChildPageFIelds+"' WHERE id='"+tableName+"'";
			languageList.add(addPageSql);
		}
		
		rs = mgt.updateFieldMaintain(updateSqlList,saveBeanList,languageList,statusStopSqlList);
		
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//更新主表表结构
			new InitMenData().initDBInformation(request.getSession().getServletContext(), tableName);
			//更新明细表结构
			if(childTableName!=null && !"".equals(childTableName)){
				new InitMenData().initDBInformation(request.getSession().getServletContext(), childTableName);
			}
			
			if(reloadEnumer){
				//更新内存数据
				InitMenData imd = new InitMenData();
				//更新内存中枚举值
				imd.initEnumerationInformation();
			}
			
			return new ActionForward("/CRMBrotherSettingAction.do?operation=7&type=maintain&operationSuccess=true&tableName="+tableName,true);
		}else{
			//修改失败
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
			
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 显示字段设置更新前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFieldsDisplayPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String tableName = getParameter("tableName",request);//表名
		
		String childTableName="";
		ArrayList<DBFieldInfoBean> allFieldsBeanList = new ArrayList<DBFieldInfoBean>();//存放主表与明细表fieldBean
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//根据表名查找明细
		
		allFieldsBeanList.addAll(tableInfoBean.getFieldInfos());
		
		if(childTableList!=null && childTableList.size()>0){
			childTableName = childTableList.get(0).getTableName();
			request.setAttribute("childFieldList",childTableList.get(0).getFieldInfos());
			allFieldsBeanList.addAll(childTableList.get(0).getFieldInfos());
		}
		
		Result rs = mgt.loadBrotherFieldDisplayBean(tableName);
		BrotherFieldDisplayBean fieldDisplayBean = (BrotherFieldDisplayBean)rs.retVal;
		if(fieldDisplayBean == null){
			request.setAttribute("tableName",tableName);
			request.setAttribute("childTableName",childTableName);
			request.setAttribute("mainFieldList",tableInfoBean.getFieldInfos());
			request.setAttribute("fieldList",allFieldsBeanList);
			return getForward(request, mapping, "updateFieldDisplay") ;
		}
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("fieldDisplayBean",fieldDisplayBean);
			request.setAttribute("tableName",tableName);
			request.setAttribute("childTableName",childTableName);
			request.setAttribute("mainFieldList",tableInfoBean.getFieldInfos());
			request.setAttribute("fieldList",allFieldsBeanList);
			request.setAttribute("operationSuccess",getParameter("operationSuccess",request));
		}else{
			EchoMessage.error().add("显示设置错误").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "updateFieldDisplay") ;
	}
	
	/**
	 * 更新显示字段设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFieldsDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String tableName = getParameter("tableName",request);
		LoginBean loginBean = getLoginBean(request);
		boolean isAddBean = false;//判断是添加还是更新，默认false表示更新
		Result rs= mgt.loadBrotherFieldDisplayBean(tableName);
		BrotherFieldDisplayBean displayBean = (BrotherFieldDisplayBean)rs.retVal;
		if(displayBean == null){
			isAddBean = true;
			displayBean = new BrotherFieldDisplayBean();
		}
		CRMBrotherSettingForm settingForm = (CRMBrotherSettingForm)form;
		read(settingForm, displayBean);
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		displayBean.setLastUpdateBy(loginBean.getId());
		displayBean.setLastUpdateTime(nowTime);
		
		if(isAddBean){
			//新增bean
			displayBean.setId(tableName);
			displayBean.setCreateBy(loginBean.getId());
			displayBean.setCreateTime(nowTime);
			rs = mgt.addBrotherShowFieldsBean(displayBean);
		}else{
			//修改
			rs = mgt.updateBrotherShowFieldsBean(displayBean);
		}
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			//EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/CRMBrotherSettingAction.do?operation="+OperationConst.OP_UPDATE_PREPARE+"&tableName="+tableName).setAlertRequest(request);
			return new ActionForward("/CRMBrotherSettingAction.do?operation=7&type=fieldDisplay&operationSuccess=true&tableName="+tableName,true);	
		}else{
			//修改失败
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 字段只读，隐藏设置更新前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addFieldScopePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String tableName = getParameter("tableName",request);//表名
		
		ArrayList<DBFieldInfoBean> allFieldsBeanList = new ArrayList<DBFieldInfoBean>();//存放主表与明细表fieldBean
		String childTableName="";
		
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//根据表名查找明细
		
		allFieldsBeanList.addAll(tableInfoBean.getFieldInfos());
		
		if(childTableList!=null && childTableList.size()>0){
			childTableName = childTableList.get(0).getTableName();
			allFieldsBeanList.addAll(childTableList.get(0).getFieldInfos());
		}
		
		request.setAttribute("tableName",tableName);
		request.setAttribute("childTableName",childTableName);
		request.setAttribute("fieldList",allFieldsBeanList);
		return getForward(request, mapping, "addFieldScope");
	}
	
	/**
	 * 字段只读，隐藏设置更新
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addFieldScope(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		BrotherFieldScopeBean fieldScopeBean = new BrotherFieldScopeBean();
		CRMBrotherFieldScopeForm fieldScopeForm = (CRMBrotherFieldScopeForm)form;
		LoginBean loginBean = getLoginBean(request);
		read(fieldScopeForm, fieldScopeBean);
		String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		fieldScopeBean.setId(IDGenerater.getId());
		fieldScopeBean.setCreateBy(loginBean.getId());
		fieldScopeBean.setCreateTime(nowDate);
		fieldScopeBean.setLastUpdateBy(loginBean.getId());
		fieldScopeBean.setLastUpdateTime(nowDate);
		Result rs = mgt.addFieldScopeBean(fieldScopeBean);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			return new ActionForward("/CRMBrotherSettingAction.do?operation=4&type=fieldScope&operationSuccess=true&tableName="+fieldScopeForm.getTableName(),true);
			//添加成功
			//EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/CRMBrotherSettingAction.do?operation=4&type=fieldScope&tableName="+fieldScopeForm.getTableName()).setAlertRequest(request);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
        return getForward(request, mapping, "message");
        
	}
	
	/**
	 * 字段只读，隐藏设置更新
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFieldScopePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String tableName = getParameter("tableName",request);
		String keyId = getParameter("keyId",request);
		ArrayList<DBFieldInfoBean> allFieldsBeanList = new ArrayList<DBFieldInfoBean>();//存放主表与明细表fieldBean
		String childTableName="";
		
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean tableInfoBean = GlobalsTool.getTableInfoBean(tableName);
		ArrayList<DBTableInfoBean> childTableList = new DDLOperation().getChildTables(tableName, allTables);//根据表名查找明细
		
		allFieldsBeanList.addAll(tableInfoBean.getFieldInfos());
		
		if(childTableList!=null && childTableList.size()>0){
			childTableName = childTableList.get(0).getTableName();
			allFieldsBeanList.addAll(childTableList.get(0).getFieldInfos());
		}
		Result rs = mgt.loadFieldScopeBean(keyId);
		BrotherFieldScopeBean fieldScopeBean = (BrotherFieldScopeBean)rs.retVal;
		request.setAttribute("fieldScopeBean",fieldScopeBean);
		request.setAttribute("tableName",tableName);
		request.setAttribute("childTableName",childTableName);
		request.setAttribute("fieldList",allFieldsBeanList);
		request.setAttribute("operationSuccess",getParameter("operationSuccess",request));
        return getForward(request, mapping, "updateFieldScope");
        
	}
	
	/**
	 * 字段只读，隐藏设置更新
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateFieldScope(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String keyId = getParameter("keyId",request);
		LoginBean loginBean = getLoginBean(request);
		Result rs = mgt.loadFieldScopeBean(keyId);
		BrotherFieldScopeBean fieldScopeBean = (BrotherFieldScopeBean)rs.retVal;
		CRMBrotherFieldScopeForm fieldScopeForm = (CRMBrotherFieldScopeForm)form;
		read(fieldScopeForm, fieldScopeBean);
		fieldScopeBean.setLastUpdateBy(loginBean.getId());
		fieldScopeBean.setLastUpdateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		rs = mgt.updateFieldScopeBean(fieldScopeBean);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			return new ActionForward("/CRMBrotherSettingAction.do?operation=7&type=fieldScope&operationSuccess=true&tableName="+fieldScopeForm.getTableName()+"&keyId="+keyId,true);
		} else {
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
		}
        return getForward(request, mapping, "message");
        
	}

	/**
	 * 导入、导出更新前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePublicScopePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String tableName = getParameter("tableName",request);//表名
		String[] scopeNames = {"import","export","print"};//默认权限名称存入数组中
		HashMap<String,BrotherPublicScopeBean> scopeMap = new LinkedHashMap<String, BrotherPublicScopeBean>();//把所有权限Bean存放权限MAP中,key为权限名
		HashMap<String,String> groupNameMap = new LinkedHashMap<String, String>();//封装职员分组Map key= scopeName+groupId
		for(String scopeName : scopeNames){
			Result rs = mgt.loadPublicScopeBean(tableName, scopeName);
			ArrayList<BrotherPublicScopeBean> list = (ArrayList<BrotherPublicScopeBean>)rs.retVal;
			if(list!=null && list.size()>0){
				scopeMap.put(scopeName, list.get(0));
				String groupIds = list.get(0).getGroupIds();
				if(groupIds!=null && !"".equals(groupIds)){
					Result result = mgt.groupNameQueryByIds(groupIds);
					ArrayList<Object> groupList = (ArrayList<Object>)result.retVal;
					if(groupList!=null && groupList.size()>0){
						for(Object group : groupList){
							String key = scopeName + GlobalsTool.get(group,0);
							groupNameMap.put(key, GlobalsTool.get(group,1).toString());
						}
					}
				}
			}
		}

		//若为null值,表示权限表中没有此邻居表信息.因此需要新增相关记录,默认都为选项都为全部
		if(scopeMap.get("import")==null){
			Result rs = mgt.addPublicScope(tableName);
		}
		request.setAttribute("scopeMap",scopeMap);
		request.setAttribute("groupNameMap",groupNameMap);
		request.setAttribute("tableName",tableName);
		request.setAttribute("operationSuccess",getParameter("operationSuccess",request));
		return getForward(request, mapping, "updatePublicScope");
	}
	
	/**
	 * 导入、导出更新
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePublicScope(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LoginBean loginBean = getLoginBean(request);
		CRMBrotherPublicScopeForm publicScopeForm = (CRMBrotherPublicScopeForm)form;
		HashMap<String,String[]> scopeMap = new LinkedHashMap<String, String[]>();
		//String[] importArr = {publicScopeForm.getImportScopeFlag(),publicScopeForm.getImportDeptIds(),publicScopeForm.getImportUserIds(),publicScopeForm.getImportGroupIds()};
		String[] exportArr = {publicScopeForm.getExportScopeFlag(),publicScopeForm.getExportDeptIds(),publicScopeForm.getExportUserIds(),publicScopeForm.getExportGroupIds()};
		String[] printArr = {publicScopeForm.getPrintScopeFlag(),publicScopeForm.getPrintDeptIds(),publicScopeForm.getPrintUserIds(),publicScopeForm.getPrintGroupIds()};
		//scopeMap.put("import", importArr);
		scopeMap.put("export", exportArr);
		scopeMap.put("print", printArr);
		Result rs = mgt.updatePublicScope(scopeMap, publicScopeForm.getTableName(), loginBean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return new ActionForward("/CRMBrotherSettingAction.do?operation=7&type=publicScope&operationSuccess=true&tableName="+publicScopeForm.getTableName(),true);	
		}else{
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		
	}
	
	/**
	 * 删除字段权限
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delFieldScope(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String ids = getParameter("ids",request);//keyIds
		String[] keyIds = ids.split(",");
		Result rs = mgt.delFieldScopeBean(keyIds);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	public String getBeanEnumer(List<String> languageList,String fieldName){
		Date date = new Date();
		//添加多语言分组枚举
		String enumerLanId = IDGenerater.getId();
		String enumerId = IDGenerater.getId();
		String extentEnumName = "extent" + "_" + fieldName;
		languageList.add("insert into tblLanguage(id,zh_TW,zh_CN,en) values('"+enumerLanId+"','扩展选项','扩展选项','扩展选项')");
		languageList.add("insert into tblDBEnumeration values('"+enumerId+"','"+extentEnumName+"','1','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"','1','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"',NULL,NULL,NULL,'finish','-1','0','"+enumerLanId+"','0','')");
		
		//添加多语言枚举选项
		enumerLanId = IDGenerater.getId();
		languageList.add("insert into tblLanguage(id,zh_TW,zh_CN,en)values('"+enumerLanId+"','选项1','选项1','')");
		languageList.add("insert into tblDBEnumerationItem VALUES('"+IDGenerater.getId()+"','1','"+enumerId+"','','','','finish','-1','0','"+enumerLanId+"','','1')");
		enumerLanId = IDGenerater.getId();
		languageList.add("insert into tblLanguage(id,zh_TW,zh_CN,en)values('"+enumerLanId+"','选项2','选项2','')");
		languageList.add("insert into tblDBEnumerationItem VALUES('"+IDGenerater.getId()+"','2','"+enumerId+"','','','','finish','-1','0','"+enumerLanId+"','','1')");
		
		return extentEnumName;
	}
	
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}
	
	
	/**
	 * 删除新增字段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delField(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tableName = getParameter("tableName",request);
		String fieldName = getParameter("fieldName",request);
		ArrayList<String> sqlList = new ArrayList<String>();
		DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(tableName, fieldName);
		String enumId = "";//枚举ID
		if(fieldBean.getInputType() == 1){
			Map map = BaseEnv.enumerationMap;
			enumId = ((EnumerateBean)map.get(fieldBean.getRefEnumerationName())).getId();
		}
		
		sqlList.add("ALTER TABLE "+tableName+" DROP COLUMN " + fieldBean.getFieldName());//删除物理表字段
		sqlList.add("delete from tblDBFieldInfo where id='"+fieldBean.getId()+"'");//删除dbFieldInfo
		sqlList.add("delete from tbllanguage where id='"+fieldBean.getLanguageId()+"'");//删除dbFieldInfo多语言
		if(!"".equals(enumId) && fieldBean.getRefEnumerationName().startsWith("extent_")){ //只有extent_表头的枚举才是字段自动产生的，可以删除，否则有可能是公用枚举，删除可能会影响其它表
			//若是下拉框删除枚举值
			sqlList.add("delete from tblDBEnumeration where id='"+enumId+"'");
			sqlList.add("delete from tblDBEnumerationItem where id='"+enumId+"'");
		}
		
		
		//删除时去掉显示字段已选的选项
		String sql = "UPDATE CRMBrotherFieldDisplay SET keyFields = replace(keyFields,'"+fieldName+",',','),searchFields = replace(searchFields,'"+fieldName+",',','),listFields = replace(listFields,'"+fieldName+",',','),detailFields = replace(detailFields,'"+fieldName+",',','),pageFields = replace(pageFields,'"+fieldName+",',','),pageChildFields = replace(pageChildFields,'"+fieldName+",',',') where id = '"+tableName+"'";
		sqlList.add(sql);
		Result rs = mgt.delField(sqlList);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			new InitMenData().initDBInformation(request.getSession().getServletContext(),tableName);
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}
}