package com.menyi.aio.web.userFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.bean.ReportsDetBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.importData.BSFExec;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.sysAcc.ReCalcucateMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.ErrorMessage;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.StockAnalysisInfoMgt;
import com.menyi.web.util.TestRW;

/**
 * 	<p>该类主要数据表列表，报表，弹出窗等的树目录显示</p>
 * 
 *  @Copyright: 科荣软件
 *	
 *  @Date:2009-9-18
 *
 *	@Author 文小钱
 */
public class TreeAction extends BaseAction{
	TreeMgt mgt = new TreeMgt();
	private static Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create(); 
	/**
	 * 不做权限控制
	 */
    protected ActionForward doAuth(HttpServletRequest req,ActionMapping mapping) {
    	return null;
    }
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//跟据不同操作类型分配给不同函数处理
        ActionForward forward = null ;
        String opType = request.getParameter("opType");
        if("getLastValue".equals(opType)){
        	forward = getLastValue(mapping, form, request, response);       
        }else if("add".equals(opType)){
        	forward = add(mapping, form, request, response);       
        }else if("delete".equals(opType)){
        	forward = delete(mapping, form, request, response);       
        }else if("update".equals(opType)){
        	forward = update(mapping, form, request, response);       
        }else if("convertFile".equals(opType)){
        	forward = convertFile(mapping, form, request, response);       
        }else{
        	forward = tree(mapping, form, request, response);            
        }
		return forward;
	}
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String tableName = request.getParameter("tableName");
		String classCodeField = request.getParameter("classCodeField");
		String msg = "";
		
		Result rs = mgt.delete(tableName, classCodeField);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			msg = "OK";
		}else if(rs.retVal != null){
			msg = rs.retVal.toString();
		}else{
			msg = "删除失败";
		}
		
    	request.setAttribute("msg", msg);
    	return getForward(request, mapping, "blank");
	}
	protected ActionForward update(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String tableName = request.getParameter("tableName");
		String parentCode = request.getParameter("parentCode");
		String numberField = request.getParameter("numberField");
		String numberFieldName = request.getParameter("numberFieldName");
		String nameField = request.getParameter("nameField");
		String nameFieldName = request.getParameter("nameFieldName");
		String classCodeField = request.getParameter("classCodeField");
		String msg = "";
		
		Result rs = mgt.update(tableName, classCodeField, nameField, numberField, nameFieldName, numberFieldName);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			msg = "OK";
		}else{
			msg = "修改失败";
		}
		
    	request.setAttribute("msg", msg);
    	return getForward(request, mapping, "blank");
	}
	protected ActionForward convertFile(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

		String tableName = request.getParameter("tableName");
		String parentCode = request.getParameter("parentCode");
		String keyId = request.getParameter("keyId");
		String moduleType = request.getParameter("moduleType");

        Result rs = new Result();
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        
        
        DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
        LoginBean lg = new LoginBean();
        lg = (LoginBean) request.getSession().getAttribute("LoginBean");
        
        OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
        

        Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = GlobalsTool.getMOperationMap(request) ;
		Result rso=new DynDBManager().detail(tableName, BaseEnv.tableInfos, keyId, lg.getSunCmpClassCode(), props, getLoginBean(request).getId(), false,  "");
		HashMap values = new HashMap();
		if(rso.retCode==ErrorCanst.DEFAULT_SUCCESS&&rso.retVal!=null){
    		values=(HashMap)rso.retVal;
    	}
        //加入修改s人信息
        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
            DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
            if (fi.getFieldName().equals("lastUpdateBy")) {
                values.put("lastUpdateBy", getLoginBean(request).getId());
            } else if (fi.getFieldName().equals("lastUpdateTime")) {
                values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
            }else if(fi.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE){
            	KRLanguage lgn =  (KRLanguage)((HashMap)values.get("LANGUAGEQUERY")).get(values.get(fi.getFieldName()));
            	values.put(fi.getFieldName(), lgn);
            }
        }
        values.put("isCatalog", "0"); //修改为子结点
        if(workFlow==null || workFlow.getTemplateStatus() == 0){
			values.put("workFlowNodeName", "finish") ;
			values.put("workFlowNode", "-1");
    		values.put("checkPersons", "") ;
    		values.put("finishTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		}else{
			values.put("workFlowNodeName", "notApprove") ;
			values.put("workFlowNode", "0") ;
        	values.put("checkPersons", ";"+lg.getId()+";") ;
        	values.put("finishTime","");
		}
        
        Object obj = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	MessageResources resources = null;
		if (obj instanceof MessageResources) {
		    resources = (MessageResources) obj;
		}
        rs = new DynDBManager().update(tableInfo.getTableName(), map, values, getLoginBean(request).getId(),
        		"",resources,this.getLocale(request),"",getLoginBean(request),workFlow,props);

        String msg = "";
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode==ErrorCanst.RET_DEFINE_SQL_ALERT) {
        	msg = "OK";
        	/*添加系统日志*/
        	int operation=1;
        	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		String billTypeName=getModuleNameByLinkAddr(request, mapping);
        		new DynDBManager().addLog(operation, values, values, tableInfo, this.getLocale(request).toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"",billTypeName);
        	}
        } else {
        	SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	msg = saveErrrorObj.getMsg();
        }
        request.setAttribute("msg", msg);
    	return getForward(request, mapping, "blank");
	}
	protected ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String tableName = request.getParameter("tableName");
		String parentCode = request.getParameter("parentCode");
		String numberField = request.getParameter("numberField");
		String numberFieldName = request.getParameter("numberFieldName");
		String nameField = request.getParameter("nameField");
		String nameFieldName = request.getParameter("nameFieldName");
		String moduleType = request.getParameter("moduleType");
		
		
		DBTableInfoBean bean = BaseEnv.tableInfos.get(tableName);
		LoginBean  loginBean = this.getLoginBean(request);
		HashMap saveValues = new HashMap();
		//设置默认值
		UserFunctionMgt userMgt = new UserFunctionMgt();
		DynDBManager mgt = new DynDBManager();
		Result rs = new Result();
		String msg = "";
		try {				
			userMgt.setDefault(bean, saveValues, loginBean.getId());
			
			/* 从内存中读取当前单据的工作流信息 */
			//目录不启用审核
			OAWorkFlowTemplate workFlow = null;//BaseEnv.workFlowInfo.get(tableName);
			DBFieldInfoBean nameF = GlobalsTool.getFieldBean(tableName, nameFieldName);
			if(nameF.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE){
				KRLanguage lan = KRLanguageQuery.create(nameField, "", "");
				saveValues.put(nameFieldName, lan);
			}else{
				saveValues.put(nameFieldName, nameField);
			}			
			if(numberFieldName != null && numberFieldName.length() > 0){
				saveValues.put(numberFieldName, numberField);
			}
			if(moduleType != null && moduleType.length() > 0){
				saveValues.put("moduleType", moduleType);
			}
			//处理拼音码
			for(DBFieldInfoBean fb : bean.getFieldInfos()){
	    		if(fb.getInputType() == DBFieldInfoBean.INPUT_PYM){
	    			String chinese = String.valueOf(saveValues.get(fb.getFieldName()));
	    			
	    			chinese = chinese.replaceAll(" ", "");
	    			String pym = CustomizePYM.getFirstLetter(chinese);
	    			if (pym != null && pym.length() > 30) {
	    				pym = pym.substring(0, 30);
	    			}
	    			  
	    			saveValues.put(fb.getFieldName()+"PYM",pym );
	    		}
	    	}
			
			/*初始化一些字段的基本信息*/
	        Result rs_initDBField = userMgt.initDBFieldInfo(bean, loginBean, saveValues, parentCode, 
	        										workFlow) ;	              
	        saveValues.put("isCatalog", "1");	        
	        int opertion=0;
	        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	        MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			Locale locale = this.getLocale(request);
			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
			
			String MOID=request.getParameter("MOID");
			MOperation mop = (MOperation)this.getLoginBean(request).getOperationMapKeyId().get(MOID);
			
			saveValues.put("workFlowNodeName", "draft") ;
			saveValues.put("workFlowNode", "-1");
			saveValues.put("checkPersons", "") ;
			saveValues.put("finishTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			
	        rs = new DynDBManager().add(tableName, BaseEnv.tableInfos, saveValues,loginBean.getId(), 
	        		"","",resources,locale,"saveDraft",loginBean,workFlow,"false",props);	    	
	        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	        	//直接修改为finish
	        	TreeMgt tmgt = new TreeMgt();
	        	tmgt.finish(tableName, saveValues.get("id")+"");
	        	/*添加系统日志 导入*/
	        	new DynDBManager().addLog(opertion, saveValues, null, bean, locale.toString(), loginBean.getId(), loginBean.getEmpFullName(), loginBean.getSunCmpClassCode(),"","");
	        }
		} catch (Exception e) {
			msg = e.getMessage();
		}
		
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			msg = "OK";
		}else{
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
			msg = saveErrrorObj.getMsg();
		}
    	request.setAttribute("msg", msg);
    	return getForward(request, mapping, "blank");
	}
	
	
    public static String getFieldDisplay(Hashtable allTables, String fieldName,String locale) {
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
            DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.
                                        getFieldInfos().get(i);
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
	protected ActionForward getLastValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String tableName = request.getParameter("tableName");
		String parentCode = request.getParameter("parentCode");
		String numberField = request.getParameter("numberField");
		Result rs=new DynDBManager().getLastValue(tableName, parentCode,numberField,"");
		String msg = "";
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			if(rs.getRetVal()!=null){
				msg = rs.getRetVal()+"";
			}
		}
    	request.setAttribute("msg", msg);
    	return getForward(request, mapping, "blank");
	}
	
	private void toTreeItem(String parentCode,int startLen,ArrayList treeItems,ArrayList<Object[]> list,ArrayList<Object[]> handlist,ArrayList<TreeItem> alllist){
		for(Object[] ss :list){
			if(ss[0].toString().startsWith(parentCode) && ss[0].toString().length() == (parentCode.length()==0?startLen: parentCode.length() +5)){
				TreeItem item = new TreeItem(ss[0].toString(),ss[1].toString(),ss[2].toString(),true,ss[3].toString());
				treeItems.add(item);
				alllist.add(item);
				handlist.add(ss);
				toTreeItem(ss[0].toString(),startLen,item.getNodes(),list,handlist,alllist);
			}
		}
	}
	
    protected ActionForward tree(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
    	String tableName = request.getParameter("tableName");
    	request.setAttribute("tableName", tableName);
    	String moduleType = request.getParameter("moduleType");
    	request.setAttribute("moduleType", moduleType);
    	String SysType = request.getParameter("SysType");  //报表中根据dataType 来设置这个参数
    	request.setAttribute("SysType", SysType);
    	String editable = request.getParameter("editable");
    	request.setAttribute("editable", editable);
    	String subSql= request.getParameter("subSql");
    	request.setAttribute("subSql", subSql);
    	
    	DBTableInfoBean bean = BaseEnv.tableInfos.get(tableName);
    	//找出行标识字段
    	DBFieldInfoBean nameField = null;
    	DBFieldInfoBean numberField = null;
    	boolean hasModuleType=false;
    	for(DBFieldInfoBean fb : bean.getFieldInfos()){
    		if("RowMarker".equals(fb.getFieldSysType())){
    			nameField = fb;
    		}else if("lastValueAdd".equals(fb.getFieldIdentityStr())){
    			numberField = fb;
    		}if("moduleType".equals(fb.getFieldName())){
    			hasModuleType = true;
    		}
    	}
    	if(nameField == null){
    		ActionForward forward = getForward(request, mapping, "message");
            EchoMessage.error().add("表"+tableName+"没有行标识字段").setRequest(request);
            return forward;
    	}
    	String loacle = this.getLocale(request).toString();
    	request.setAttribute("tableDisplay", bean.getDisplay().get(loacle));
    	request.setAttribute("nameDisaplay", nameField.getDisplay().get(loacle));
    	request.setAttribute("nameField", nameField.getFieldName());
    	if(numberField != null){
    		request.setAttribute("numberDisaplay", numberField.getDisplay().get(loacle));
        	request.setAttribute("numberField", numberField.getFieldName());
    	}
    	
    	String MOID = request.getParameter("MOID");
    	request.setAttribute("MOID", MOID);
    	
    	//取对应模块的权限
        MOperation mop =MOID==null?null: (MOperation)this.getLoginBean(request).getOperationMapKeyId().get(MOID);
            
    	ArrayList scopeRight = new ArrayList();
    	if(mop != null){
	        scopeRight.addAll(mop.getScope(MOperation.M_QUERY));      
	        //加入所有分类权限
	    	scopeRight.addAll(mop.classScope) ;
	      	ArrayList allScopeList = this.getLoginBean(request).getAllScopeRight();
	    	if(allScopeList!=null){        		
	    	   scopeRight.addAll(allScopeList) ;
			}
    	}
    	
    	if(moduleType != null && moduleType.length() > 0){
    		if(!hasModuleType){
    			moduleType = "";
    		}
    	}
    	//目录树除了公类权限要控制外不能控制创建人等，因为目录不能是谁创建的谁才能看
    	Result rs = mgt.getTree(tableName,moduleType,SysType, nameField.getFieldName(),numberField != null?numberField.getFieldName():"''",
    			nameField.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE,this.getLoginBean(request).getId(),subSql,scopeRight);
    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if ("0".equals(lsb.getFlag())) {
					String clstn = lsb.getTableName();
					if(clstn.equals(tableName)){
						request.setAttribute("hasScopeRight", "true");
					}
				}
    		}

    		
    		
    		ArrayList treeItems  = new ArrayList();
    		//根目录
    		TreeItem item = new TreeItem("","根目录","",true,"");
			treeItems.add(item);    
			ArrayList<TreeItem> alllist = new ArrayList<TreeItem>();//保存所有树结点线性的
			
			if (tableName.equals("tblEmployee")){
				Object[] os = (Object[])rs.retVal;
				ArrayList<Object[]> deptList = (ArrayList<Object[]>)os[0];
				ArrayList<Object[]> empList = (ArrayList<Object[]>)os[1];
				
				for(int i=0;i<100;i++){
					ArrayList<Object[]> handlist = new ArrayList<Object[]>();
					toTreeItem("",(i+1)*5,item.getNodes(),deptList,handlist,alllist);
					deptList.removeAll(handlist);
					if(deptList.size() == 0){
						break;
					}
				}
				for(Object[] o :empList){
					for(TreeItem ti :alllist){
						if(o[2].equals(ti.getClassCode())){
							ti.getNodes().add(new TreeItem("EMP_"+String.valueOf(o[0]),String.valueOf(o[1]),"",true,String.valueOf(o[4])));
						}
					}
				}
				
			}else{
				ArrayList<Object[]> list = (ArrayList<Object[]>)rs.retVal;
				
				for(int i=0;i<100;i++){
					ArrayList<Object[]> handlist = new ArrayList<Object[]>();
					toTreeItem("",(i+1)*5,item.getNodes(),list,handlist,alllist);
					list.removeAll(handlist);
					if(list.size() == 0){
						break;
					}
				}
			}
    		
    		String json = gson.toJson(treeItems);
    		request.setAttribute("treedata", json);
    		
    	}else{
    		ActionForward forward = getForward(request, mapping, "message");
            EchoMessage.error().add("执行错误").setRequest(request);
            return forward;
    	}
    	
    	return getForward(request, mapping, "tree"); 
    }	
}
