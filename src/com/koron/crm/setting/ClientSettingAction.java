package com.koron.crm.setting;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.publicMsg.newsInfo.OANewsMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.FieldScopeSetBean;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.tab.TabMgt;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ChineseSpelling;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.OperationConst;

import org.apache.commons.beanutils.PropertyUtils; 

/**
 * 
 * <p>Title:客户设置</p> 
 * <p>Description: </p>
 *
 * @Date:Oct 24, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 李文祥
 */
public class ClientSettingAction  extends BaseAction{
	ClientSetingMgt mgt=new ClientSetingMgt();
	OANewsMgt omgt = new OANewsMgt();
	String moduleId = "";
	String viewId = "";
	String tableInfo = "";
	String oldPageFields = "";
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean=this.getLoginBean(request);
		request.setAttribute("loginName", loginBean.getName());
		int operation = getOperation(request);	
		moduleId = request.getParameter("moduleId");
		viewId = request.getParameter("viewId");
		if(moduleId ==null || "".equals(moduleId)){
			moduleId = "1";
		}
		if(viewId ==null || "".equals(viewId)){
			viewId = "1_" + moduleId;
		}
		request.setAttribute("moduleId", moduleId);
		request.setAttribute("viewId", viewId);
		Result rs = mgt.detailCrmModule(moduleId);
		ClientModuleBean bean = (ClientModuleBean)rs.retVal;
		tableInfo = bean.getTableInfo();
    	List<DBFieldInfoBean> fieldsList = GlobalsTool.getTableInfoBean(tableInfo.split(":")[0]).getFieldInfos() ;
    	List<DBFieldInfoBean> fieldsList2 = GlobalsTool.getTableInfoBean(tableInfo.split(":")[1]).getFieldInfos() ;
    	List<DBFieldInfoBean> allList=new ArrayList<DBFieldInfoBean>();
    	allList.addAll(fieldsList);
    	allList.addAll(fieldsList2);
        request.setAttribute("fieldList",allList); 
        
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO); 
        ArrayList tableList=DDLOperation.getBrotherTables("CRMClientInfo",map);
        request.setAttribute("brotherList", tableList);
        request.setAttribute("mainTableName", tableInfo.split(":")[0]);
    	request.setAttribute("childTableName", tableInfo.split(":")[1]);
    	
    	String queryType=request.getParameter("queryType");  //查询操作的标识
    	String addType=request.getParameter("addType");   //增加操作的标识
    	String updPreType=request.getParameter("updPreType");  //进入编辑页面的标识
    	String updType=request.getParameter("updType");  //编辑操作的标识
    	String delType=request.getParameter("delType");  //删除操作的标识
    	
		ActionForward forward = null;
		switch (operation) {
		// 新增操作
		case OperationConst.OP_ADD:
			if("copy".equals(addType)) //客户模版复制操作
				forward =updatePrepare(mapping,form,request,response);
			else if("fieldScopeSet".equals(addType)) //添加字段权限设置
				forward = addfieldScopeSet(mapping, form, request, response);
			else if("checkName".equals(addType)) //验证模版是否已经存在
				forward = checkModuleName(mapping,form,request,response);
			else if("moduleView".equals(addType)) //添加字段权限设置
				forward = addModuleView(mapping, form, request, response);
			else if("broTabSet".equals(addType)) //兄弟表排序管理
				forward = broTabSet(mapping, form, request, response);
			else  //添加客户模版
				forward = add(mapping, form, request, response);
			break;
		// 新增前的准备
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);	
			break;
		// 修改前的准备
		case OperationConst.OP_UPDATE_PREPARE:
			if("fieldScopeSet".equals(updPreType))  //进入修改字段权限设置页面
				forward = updateScopePrepare(mapping, form, request, response);
			else if("moduleView".equals(updPreType)){
				forward = viewUpdatePrepare(mapping, form, request, response);
			}else if("brotherSort".equals(updPreType)){
				forward = brotherSortUpdatePrepare(mapping, form, request, response);
			}else if("childDisplay".equals(updPreType)){
				forward = childDisplayUpdatePrepare(mapping, form, request, response);
			}else      //进入修改客户模版页面
				forward = updatePrepare(mapping, form, request, response);
			break;
			
		// 修改
		case OperationConst.OP_UPDATE:
			if("fieldsMtain".equals(updType))  //字段维护
				forward = updateField(mapping, form, request, response);
			else if("fieldScopeSet".equals(updType))  //修改字段权限设置
				forward = updateFieldScope(mapping, form, request, response);
			else if("updateCrmScope".equals(updType)) //修改客户打印、导入、导出权限设置
				forward = updateCrmScope(mapping, form, request, response);
			else if("defaultModule".equals(updType))
				forward = defaultModule(mapping,form,request,response);
			else if("moduleView".equals(updType))
				forward = updateModuleView(mapping,form,request,response);
			else if("updateTransfer".equals(updType))
				forward = updateTransfer(mapping,form,request,response);
			else if("brotherSort".equals(updType)){
				forward = updateBrotherSort(mapping, form, request, response);
			}else if("childDisplay".equals(updType)){
				forward = updateChildDisplay(mapping, form, request, response);
			}else //修改客户模版
				forward = update(mapping, form, request, response);
			
			
			
			break;
		// 查询
		case OperationConst.OP_QUERY:
		    if("fieldsMaintain".equals(queryType)) //打开字段维护页面
				forward = fieldsMtain(mapping, form, request, response);
		    else if("fieldScopeSet".equals(queryType))//打开字段权限设置列表
		    	forward =fieldScopeSet(mapping,form,request,response);
		    else if("crmEnumeration".equals(queryType))
		    	forward=crmEnumerationSet(mapping,form,request,response);  //打开客户选项数据管理页面
		    else if("crmScope".equals(queryType))
		    	forward=crmScope(mapping,form,request,response);  //打开打印导出设置页面
		    else if("reloadEnum".equals(queryType))
		    	forward=reloadEnum(mapping,form,request,response);  //重新加载枚举
		    else if("moduleView".equals(queryType))
		    	forward=queryModuleView(mapping,form,request,response);  //模板视图
		    else if("broTabSort".equals(queryType))
		    	forward= broTabSort(mapping,form,request,response); //邻居表排序
		    else if("checkFieldName".equals(queryType)){
		    	forward= checkFieldNameCN(mapping,form,request,response); //异步检测字段中文是否存在
		    }else if("clientTransfer".equals(queryType)){
		    	forward= clientTransferQuery(mapping,form,request,response); 
		    }else
		    	forward = query(mapping, form, request, response);	 //客户模版
			break;
		// 删除操作
		case OperationConst.OP_DELETE:
			if("fieldScopeSet".equals(delType))   //删除字段权限设置
				forward = delFieldScope(mapping, form, request, response);
			else if("delField".equals(delType))   //删除字段权限设置
				forward = delField(mapping, form, request, response);
			else if("moduleView".equals(delType))   //删除模板视图
				forward = delModuleView(mapping, form, request, response);
			else  //删除客户模版
				forward = delete(mapping, form, request, response);
			break;
		//默认
		default:
			String actionType=getParameter("actionType",request);
			if("toLeft".equals(actionType))
				forward = toLeft(mapping, form, request, response);
			else
				if("moduleView".equals(queryType)){
					forward = viewFrame(mapping, form, request, response);
				}else{
					forward = frame(mapping, form, request, response);
				}
		}
		return forward;
	}
	
	
	
	private ActionForward updateChildDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId",request);
		String viewId = getParameter("viewId",request);
		String childTableInfo = getParameter("childTableInfo",request);
		String childDisplayFields = getParameter("childDisplayFields",request);
		
		Result rs = mgt.loadModuleView(viewId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ClientModuleViewBean viewBean = (ClientModuleViewBean)rs.retVal;
			viewBean.setChildTableInfo(childTableInfo);
			viewBean.setChildDisplayFields(childDisplayFields);
			rs = mgt.updModuleView(viewBean);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				String url = "/ClientSettingAction.do?updPreType=childDisplay&operation=7&viewId="+viewId+"&moduleId="+moduleId;
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl(url).setAlertRequest(request);
		    	
			}else{
				
			}
		}else{
			
		}
		
		return getForward(request, mapping, "message");
	}



	private ActionForward childDisplayUpdatePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String moduleId = getParameter("moduleId",request);
		String viewId = getParameter("viewId",request);
		
		if(tableInfo!=null && !"".equals(tableInfo)){
			Hashtable allMap = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(tableInfo.split(":")[0], allMap);
			request.setAttribute("childTableList",childTableList);
			request.setAttribute("mainTableName",tableInfo.split(":")[0]);
		}
		
		ClientModuleViewBean moduleViewBean= (ClientModuleViewBean)mgt.loadModuleView(viewId).retVal;
		
		request.setAttribute("moduleViewBean",moduleViewBean);
		return getForward(request, mapping, "updateChildDisplay");
	}



	private ActionForward updateBrotherSort(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String viewId = getParameter("viewId",request);
		String orderTab = getParameter("orderTab",request);
		Result rs = mgt.updateBrotherSort(viewId, orderTab);
		String data = "";
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
    		data="true";
    	}else{
    		data="false";
    	}
    	request.setAttribute("msg", data);
    	return getForward(request, mapping, "blank");
	}



	/**
	 * 兄弟表排序更新前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward brotherSortUpdatePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String viewId = getParameter("viewId",request);//视图id
		LoginBean loginBean = getLoginBean(request);
		
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		ArrayList<DBTableInfoBean> brotherTableList = new DDLOperation().getBrotherTables("CRMClientInfo", allTables);//获取兄弟表信息
		
		String userBrotherInfo = "";//已选邻居表信息
		String noUserBrotherInfo = "";//未选邻居表信息
		
		//查看是否有排序记录
		Result rs = mgt.brotherSortQueryByViewId(viewId);
		ArrayList list = (ArrayList)rs.retVal;
		if(list!=null && list.size()>0){
			userBrotherInfo = GlobalsTool.get(list.get(0),1).toString();
			for(DBTableInfoBean tableBean : brotherTableList){
				if(userBrotherInfo.indexOf(tableBean.getTableName()) == -1){
					noUserBrotherInfo += tableBean.getTableName() + ",";//处理没选的邻居表信息
				}
			}
		}else{
			rs = mgt.addBrotherSort(viewId, loginBean);//数据库没有记录的默认新建一个空的 
		}

		brotherTableList = this.checkModuleUse(brotherTableList, request);//过滤未启用的邻居表
		
		HashMap<String,DBTableInfoBean> moduleMap = new HashMap<String, DBTableInfoBean>();//存放已启用的模板map
		for(DBTableInfoBean tableBean : brotherTableList){
			moduleMap.put(tableBean.getTableName(), tableBean);
		}
		
		request.setAttribute("userBrotherInfo", userBrotherInfo);
		request.setAttribute("noUserBrotherInfo", noUserBrotherInfo);
		request.setAttribute("brotherTableList", brotherTableList);
		request.setAttribute("moduleMap", moduleMap);
		request.setAttribute("viewId",viewId);
		return getForward(request, mapping, "brotherSort");
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
	    protected ActionForward frame(ActionMapping mapping,
	                                                ActionForm form,
	                                                HttpServletRequest request,
	                                                HttpServletResponse response) throws
	            Exception {
	    	request.setAttribute("moduleId", request.getParameter("moduleId"));
	        return getForward(request, mapping, "frame");
	    }
	    
	    /**
	     * 兄弟表排序管理
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    public ActionForward broTabSet(ActionMapping mapping,ActionForm form,
				HttpServletRequest request,HttpServletResponse response) throws Exception{
	    	String viewId=request.getParameter("viewId");
	    	String tabSort=request.getParameter("orderTab");
	    	String[] tabList=tabSort.split(",");
	    	LoginBean loginBean=this.getLoginBean(request);
	    	String f_ref=mgt.getNeighbourMainId(loginBean.getId(), "CRMClientInfo", viewId);
	    	Result rs=mgt.addNeighbour(tabList,f_ref);
	    	String data="";
	    	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    		data="true";
	    	}else{
	    		data="false";
	    	}
	    	request.setAttribute("msg", data);
	    	return getForward(request, mapping, "blank");
	    }
	    
	    /**
	     * 兄弟表排序
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    public ActionForward broTabSort(ActionMapping mapping,ActionForm form,
	    							HttpServletRequest request,HttpServletResponse response) throws Exception{
	    	/*查询用户是否设置邻居了*/
	    	String viewId=request.getParameter("viewId");
	    	LoginBean loginBean=this.getLoginBean(request);
	    	ArrayList childTabList = new ArrayList();
	    	ArrayList childTabList2 = new ArrayList();
	    	
			Result rs = mgt.loadModuleView(viewId);
			ClientModuleViewBean moduleViewBean = (ClientModuleViewBean)rs.retVal;
	    	
	    	Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
	        Result result = new TabMgt().selectBroTabByViewId(map,"CRMClientInfo",loginBean.getId(),viewId) ;
	        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
	          	ArrayList<DBTableInfoBean> listBrother = (ArrayList<DBTableInfoBean>) result.retVal ;
	        	if(listBrother.size()>0){ //判断用户是否给视图设置了兄弟表排序
	        		childTabList = listBrother ;
	        	}else{
	        		String[] tabList=moduleViewBean.getBrotherTables().split(",");
	      	        //向兄弟表排序中插入记录
	      	        String mainName="CRMClientInfo";
	      	        String f_ref=mgt.getNeighbourMainId(loginBean.getId(), mainName, viewId);
	      	        if(!"".equals(f_ref)){
	      	        	Result rsAddNeigh=mgt.addNeighbour(tabList,f_ref);
	      	        	if(rsAddNeigh.retCode!=ErrorCanst.DEFAULT_SUCCESS){
	      	        		log.debug("添加兄弟表排序记录失败");
	      	        	}
	      	        }
	      	       Result result2 = new TabMgt().selectBroTabByViewId(map,"CRMClientInfo",loginBean.getId(),viewId) ;
		   	        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
		   	          	ArrayList<DBTableInfoBean> listBrother2 = (ArrayList<DBTableInfoBean>) result2.retVal ;
		   	        	if(listBrother2.size()>0){ 
		   	        		childTabList = listBrother2 ;
		   	        	}
		        	}
	        	}
	        }
	    	String[] childArray = moduleViewBean.getBrotherTables().split(",");
       		for(String child : childArray){
       			DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(child) ;
       			if(tableInfo!=null && !childTabList.contains(tableInfo)){
       				childTabList2.add(tableInfo);
       			}
       		}
       		
       		//判断模块是否启用
       		childTabList = this.checkModuleUse(childTabList, request);
       		childTabList2 = this.checkModuleUse(childTabList2, request);
       		
       		request.setAttribute("viewId", viewId);
	        request.setAttribute("broTabList", childTabList); //用户设置的可以看到的兄弟表
	        request.setAttribute("broTabList2", childTabList2); //在页面不可以看到的兄弟表
	    	return  getForward(request,mapping,"broTabSort");
	    }
	    
	    /**
		    * 加载视图框架
		    * @param mapping
		    * @param form
		    * @param request
		    * @param response
		    * @return
		    * @throws Exception
		    */
		    protected ActionForward viewFrame(ActionMapping mapping,
		                                                ActionForm form,
		                                                HttpServletRequest request,
		                                                HttpServletResponse response) throws
		            Exception {
		    	request.setAttribute("viewId", viewId);
		    	
		        return getForward(request, mapping, "viewFrame");
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
	    protected ActionForward toLeft(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	
	    	request.setAttribute("moduleId", request.getParameter("moduleId"));
	    	request.setAttribute("leftFlag", request.getParameter("leftFlag"));
	    	request.setAttribute("viewEnter", request.getParameter("viewEnter"));
	    	request.setAttribute("viewId", viewId);
	        return getForward(request, mapping, "left");
	    }
	    
	    
	    
	   /**
	    * 进入打印导出设置页面
	    * @param mapping
	    * @param form
	    * @param request
	    * @param response
	    * @return
	    * @throws Exception
	    */
	    protected ActionForward crmScope(ActionMapping mapping,
						                ActionForm form,
						                HttpServletRequest request,
						                HttpServletResponse response) throws
						                Exception {
	    	try{
	    		GlobalsTool gt=new GlobalsTool();
	    		Result rs=mgt.getCRmScope(viewId);
	    		List crmScopeList=(List) rs.retVal;
	    		System.out.println(new Date().getTime());
	    	 	/*获取打印授权对象（部门、用户、组）*/
				List<Object> printDept = omgt.getDepartment(gt.get(crmScopeList.get(0),2).toString());
				List<EmployeeBean> printUsers =omgt.getEmployee(gt.get(crmScopeList.get(0),3).toString());
				List<Object> printEmpGroup = omgt.getEmpGroup(gt.get(crmScopeList.get(0),4).toString());
				request.setAttribute("printDept", printDept);
				request.setAttribute("printUsers", printUsers);
				request.setAttribute("printEmpGroup", printEmpGroup);
				
				/*获取导入授权对象（部门、用户、组）*/
				List<Object> importDept = omgt.getDepartment(gt.get(crmScopeList.get(1),2).toString());
				List<EmployeeBean> importUsers =omgt.getEmployee(gt.get(crmScopeList.get(1),3).toString());
				List<Object> importEmpGroup = omgt.getEmpGroup(gt.get(crmScopeList.get(1),4).toString());
				request.setAttribute("importDept", importDept);
				request.setAttribute("importUsers", importUsers);
				request.setAttribute("importEmpGroup", importEmpGroup);
				
				/*获取导出授权对象（部门、用户、组）*/
				List<Object> exportDept = omgt.getDepartment(gt.get(crmScopeList.get(2),2).toString());
				List<EmployeeBean> exportUsers =omgt.getEmployee(gt.get(crmScopeList.get(2),3).toString());
				List<Object> exportEmpGroup = omgt.getEmpGroup(gt.get(crmScopeList.get(2),4).toString());
				request.setAttribute("exportDept", exportDept);
				request.setAttribute("exportUsers", exportUsers);
				request.setAttribute("exportEmpGroup", exportEmpGroup);
	    		
	    		request.setAttribute("crmScopeList", rs.retVal);
	    		
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
			return getForward(request, mapping, "crmScope");
		}

	    /**
	     * 添加客户模版
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward add(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	
	    	//获取随机数
	    	Date date = new Date();
	    	String tempNo = date.getMinutes() + "" + date.getSeconds();
			while(true){
				List list = (List)mgt.findTabelInfoBean("CRMClientInfo" + tempNo).retVal;
				if(list==null || list.size() == 0){
					break;
				}
				tempNo = date.getMinutes() + "" + date.getSeconds();
			}
			
			
			String moduleId = getParameter("moduleId", request);//模板ID,如果有ID表示是拷贝模板
			String moduleName = request.getParameter("moduleName");//模板名称
			String moduleDesc = request.getParameter("moduleDesc");
			boolean copyFlag = false; //默认表示不是拷贝
			//默认选择的表结构
			String tableName = "CRMClientInfoCopy";
			String childTableName="CRMClientInfoDetCopy";
			
			if(moduleId!=null && !"".equals(moduleId)){
				//表示拷贝模板
				Result rs = mgt.detailCrmModule(moduleId);
				ClientModuleBean bean = (ClientModuleBean)rs.retVal;	
				tableName = bean.getTableInfo().split(":")[0];
				childTableName = bean.getTableInfo().split(":")[1];
				moduleDesc = "复制"+bean.getModuleName();
				copyFlag = true;
				moduleName = "复制"+bean.getModuleName();
				int i =0;
				
				//判断模板名称是否有重复
				while(true){
					String tempModuleName = moduleName + "("+i+")";//临时模板名称
					if(i==0){
						tempModuleName = moduleName;
					}
					rs = mgt.findmoduleByName(tempModuleName);
					ArrayList list = (ArrayList)rs.retVal;
					if(list == null || list.size()==0){
						moduleName = tempModuleName;//不存在返回
						break;
					}
					i++;
				}
				
			}
			
			
	    	//复制主表从表
			List<String> stateList = new ArrayList<String>();//存放插入sql语句
	    	Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
	    	DBTableInfoBean mainTableInfo = this.getTableInfo(allTables, tableName,tempNo,this.getLocale(request).toString(),stateList,copyFlag);
			DBTableInfoBean childTableInfo = this.getTableInfo(allTables, childTableName,tempNo,this.getLocale(request).toString(),stateList,copyFlag);
			childTableInfo.setPerantTableName(mainTableInfo.getTableName());
			
//			新增模板	  
			
			String addModuleId = IDGenerater.getId();
			Result rs = new Result();
			if(moduleId==null || "".equals(moduleId)){
				rs = mgt.detailCrmModule("0");
			}else{
				rs = mgt.detailCrmModule(moduleId);
			}
			ClientModuleBean defaultBean = (ClientModuleBean)rs.retVal;
			ClientModuleBean cBean = new ClientModuleBean();
			PropertyUtils.copyProperties(cBean, defaultBean);
			String tableInfo = mainTableInfo.getTableName() +":"+ childTableInfo.getTableName();
			cBean.setId(addModuleId);
			cBean.setModuleName(moduleName);
			cBean.setTableInfo(tableInfo);
			cBean.setModuleDesc(moduleDesc);
			cBean.setIsUserLabel("0");
			cBean.setIsUserTransfer("1");
			cBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			String defaultViewId="1_"+addModuleId;
			LoginBean loginBean=this.getLoginBean(request);
	    	HashMap map = this.getNewModuleView(defaultViewId,addModuleId,"默认视图", "",moduleId);
			
			
			//rs = mgt.copyModule(mainTableInfo,childTableInfo,tempNo,this.getLocale(request).toString(),allTables,cBean,stateList);
			rs = mgt.copyModule(mainTableInfo,childTableInfo,tempNo,this.getLocale(request).toString(),allTables,cBean,stateList,map);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				ClientModuleViewBean viewBean=(ClientModuleViewBean)map.get("bean");
					String[] tabList=viewBean.getBrotherTables().split(",");
			        //向兄弟表排序中插入记录
			        String mainName="CRMClientInfo";
			        String f_ref=mgt.getNeighbourMainId(loginBean.getId(), mainName, viewId);
			        if(!"".equals(f_ref)){
			        	Result rsAddNeigh=mgt.addNeighbour(tabList,f_ref);
			        	if(rsAddNeigh.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			        		log.debug("添加兄弟表排序记录失败");
			        	}
			        }
				
				//更新内存数据
		        InitMenData imd = new InitMenData();
		        //更新内存中枚举值
		        imd.initEnumerationInformation();
				//添加成功
		        request.setAttribute("dealAsyn", "true");
		        
		        String messageSuc = getMessage(request, "common.msg.addSuccess");//添加成功
		        if(moduleId!=null && !"".equals(moduleId)){
		        	messageSuc = getMessage(request, "CRM.module.copy.success");//拷贝成功
		        }
				EchoMessage.success().add(messageSuc).setBackUrl("/ClientSettingAction.do?operation=4").setAlertRequest(request);
			}else{
				String messageFail = getMessage(request, "common.msg.addFailture");//添加失败
				if(moduleId!=null && !"".equals(moduleId)){
		        	messageFail = getMessage(request, "CRM.module.copy.fail");//拷贝失败
		        }
				//添加失败
				EchoMessage.error().add(messageFail).setAlertRequest(request);
			}
	        return getForward(request, mapping, "alert");
	    }

	    /**
	     * 修改客户打印、导入、导出权限设置
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward updateCrmScope(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	/*start:从页面取值*/
	        String printdomDeptIds=request.getParameter("printdomDeptIds");
	        String printdomUserIds=request.getParameter("printdomUserIds");
	        String printdomEmpGroupIds=request.getParameter("printdomEmpGroupIds");
	        String importdomDeptIds=request.getParameter("importdomDeptIds");
	        String importdomUserIds=request.getParameter("importdomUserIds");
	        String importdomEmpGroupIds=request.getParameter("importdomEmpGroupIds");
	        String exportdomDeptIds=request.getParameter("exportdomDeptIds");
	        String exportdomUserIds=request.getParameter("exportdomUserIds");
	        String exportdomEmpGroupIds=request.getParameter("exportdomEmpGroupIds");
	        String printisAll=request.getParameter("printisAll");
	       
	        String importisAll=request.getParameter("importisAll");
	        String exportisAll=request.getParameter("exportisAll");
	        long time = new Date().getTime();
	        /*end:取值结束*/
	        String [] Ids=new String[]{"printScope_"+time,"importScope_"+time,"exportScope_"+time};
	        String [] isAll=new String[]{printisAll,importisAll,exportisAll};
	        String [] depts=new String[]{printdomDeptIds,importdomDeptIds,exportdomDeptIds};
	        String [] users=new String[]{printdomUserIds,importdomUserIds,exportdomUserIds};
	        String [] groups=new String[]{printdomEmpGroupIds,importdomEmpGroupIds,exportdomEmpGroupIds};
	        try{
		        Result rsUpdate=mgt.updCRMScope(Ids,isAll,depts,users,groups,viewId);
		    	if(rsUpdate.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/ClientSettingAction.do?operation=4&queryType=crmScope&viewId="+viewId).setAlertRequest(request);
	    			return getForward(request, mapping, "message"); 
	    				
	    		} else {
	    			// 修改失败
	    			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
	    			return getForward(request, mapping, "message");
	    		}
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        return getForward(request, mapping, "message");
	    }

	    
	    /**
	     * 添加字段权限设置
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward addfieldScopeSet(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	FieldScopeSetForm fForm=(FieldScopeSetForm)form;
	    	/* 获得登陆者ID */
	    	String userId = this.getLoginBean(request).getId();
	    	/* 创建时间 */
			String createTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMdd);			
			FieldScopeSetBean fBean = new FieldScopeSetBean();
			read(fForm, fBean);
			fBean.setId(IDGenerater.getId());
			fBean.setCreateBy(userId);
			fBean.setCreateTime(createTime);
			fBean.setViewId(viewId);
			fBean.setModuleId(moduleId);
			Result rs=mgt.addFieldScope(fBean);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				//添加成功
				EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/FieldScopeSetAction.do?operation=4&queryType=fieldScopeSet&viewId="+viewId).setAlertRequest(request);
			}else{
				//添加失败
				EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
			}
	        return getForward(request, mapping, "message");
	    }

	    /**
	     * 添加前的准备
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward addPrepare(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	String addPreType=request.getParameter("addPreType");
	    	String returnForward="";  
	    	if("fieldScopeSet".equals(addPreType)){  //添加字段权限设置
	    		returnForward="addfieldScopeSet"; 
	    	}else{  //添加客户模版
    			returnForward="to_addClientModule";
    			try{
    				Result rs=mgt.detailCrmModule("0");
    				request.setAttribute("clientModule", rs.retVal);
    			}catch(Exception e){
    				e.printStackTrace();
    			}
	    	}
	    	return getForward(request, mapping,returnForward);
			
	    }
	    
	    /**
	     * 修改客户模版
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward update(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	try{
	    		String type=this.getParameter("type", request);
	    		Result rsOp=null;
	    		if("copy".equals(type)){   //客户模版复制操作
	    			ClientModuleForm cForm=(ClientModuleForm)form;
	    			Result rs=mgt.detailCrmModule(viewId);
			    	ClientModuleBean cBean = (ClientModuleBean) rs.getRetVal();
					cBean.setId(IDGenerater.getId());
					read(cForm, cBean);
					rsOp=mgt.addCrmModule(cBean);
	    		}else{
		    		//客户模版编辑操作
		    		ClientModuleForm cForm=(ClientModuleForm)form;
		    		Result rs=mgt.detailCrmModule(moduleId);
		    		ClientModuleBean moduleBean=(ClientModuleBean)rs.getRetVal();
		    		read(cForm, moduleBean);
		    		rsOp=mgt.updateCrmModule(moduleBean);
		    		if(rsOp.retCode == ErrorCanst.DEFAULT_SUCCESS){
		    			if("1".equals(moduleBean.getWorkflow())){
		    				
		    			}
		    		}
	    		}
	    		if(!"copy".equals(type)){
		    		if(rsOp.retCode==ErrorCanst.DEFAULT_SUCCESS)
		    			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/ClientSettingAction.do?operation=7&keyId="+moduleId).setAlertRequest(request);
		    		else// 修改失败
		    			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
		    	}else{
		    		if(rsOp.retCode==ErrorCanst.DEFAULT_SUCCESS)//复制成功
						EchoMessage.success().add(getMessage(request, "com.bom.copy.scuess")).setBackUrl("/ClientSettingAction.do?operation=4").setAlertRequest(request);
					else//复制失败
						EchoMessage.error().add(getMessage(request, "com.bom.copy.failure")).setAlertRequest(request);
		    	}
	    		return getForward(request, mapping, "message");
	    	}catch (Exception e){
	    		e.printStackTrace();
				EchoMessage.error().add(getMessage(request, "news.not.find"))
						.setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
	    	}
	    }
	    
	    /**
	     * 修改模版视图
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward updateModuleView(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	try{
	    		Result rsOp=null;
	    		//模版视图编辑操作
	    		ClientModuleForm cForm=(ClientModuleForm)form;
	    		Result rs=mgt.loadModuleView(viewId);
	    		ClientModuleViewBean moduleViewBean=(ClientModuleViewBean)rs.getRetVal();
	    		read(cForm, moduleViewBean);
	    		if(moduleViewBean.getIsAlonePopedmon() !=null && "0".equals(moduleViewBean.getIsAlonePopedmon())){
	    			moduleViewBean.setPopedomDeptIds("");
	    			moduleViewBean.setPopedomEmpGroupIds("");
	    			moduleViewBean.setPopedomUserIds("");
	    		}
	    		rsOp=mgt.updModuleView(moduleViewBean);
	    		if(rsOp.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/ClientSettingAction.do?updPreType=moduleView&operation=7&viewId="+viewId+"&moduleId="+moduleId).setAlertRequest(request);
	    			String[] tabList=moduleViewBean.getBrotherTables().split(",");
	    			//向兄弟表排序中插入记录
	    			LoginBean loginBean=this.getLoginBean(request);
	    			String mainName="CRMClientInfo";
	    			String f_ref=mgt.getNeighbourMainId(loginBean.getId(), mainName, viewId);
	    			System.out.println("f_ref："+f_ref);
	    			if(!"".equals(f_ref)){
	    				Result rsAddNeigh=mgt.addNeighbour(tabList,f_ref);
	    				if(rsAddNeigh.retCode!=ErrorCanst.DEFAULT_SUCCESS){
	    					log.debug("添加兄弟表排序记录失败");
	    				}
	    			}
	    			
	    		}else{
	    			//修改失败
	    			EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
	    		}
	    		return getForward(request, mapping, "message");
	    	}catch (Exception e){
	    		e.printStackTrace();
				EchoMessage.error().add(getMessage(request, "news.not.find"))
						.setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
	    	}
	    }
	    
	    /**
	     * 修改字段权限设置
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward updateFieldScope(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	try{
	    		String cid=this.getParameter("fieldScopeId", request);
	    		FieldScopeSetForm cForm=(FieldScopeSetForm)form;
	    		Result rs=mgt.detailFieldScope(cid);
	    		FieldScopeSetBean cBean=(FieldScopeSetBean)rs.getRetVal();
	    		String createBy=cBean.getCreateBy();
	    		String createTime=cBean.getCreateTime();
	    		read(cForm, cBean);
	    		cBean.setCreateBy(createBy);
	    		cBean.setCreateTime(createTime);
	    		Result rsUpdate=mgt.updateFieldScope(cBean);
	    		if(rsUpdate.retCode==ErrorCanst.DEFAULT_SUCCESS){
	    			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/FieldScopeSetAction.do?operation=4&queryType=fieldScopeSet&viewId="+viewId).setAlertRequest(request);
	    			return getForward(request, mapping, "message"); 
	    				
	    		} else {
	    			// 修改失败
	    			EchoMessage.error().add(
	    					getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
	    			return getForward(request, mapping, "message");
	    		}
	    	}catch (Exception e){
	    		e.printStackTrace();
				EchoMessage.error().add(getMessage(request, "news.not.find"))
						.setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
	    	}
	    }
	    
	    /**
	    * 还原默认模版
	    * @param mapping
	    * @param form
	    * @param request
	    * @param response
	    * @return
	    * @throws Exception
	    */
	  	protected ActionForward defaultModule(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	try{
	    		/*
	    		String[] id={"1"};
	    		Result rsdel=mgt.deleteCrmModule(id);
	    		if(rsdel.retCode==ErrorCanst.DEFAULT_SUCCESS){
			    	ClientModuleBean cBean = (ClientModuleBean) mgt.detailCrmModule("0").getRetVal();
			    	cBean.setId("1");
			    	cBean.setModuleName("默认模版");
			    	Result rs=mgt.addCrmModule(cBean);
			    	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				    	Result rs2=mgt.detailCrmModule("1");
				    	ClientModuleBean cBean2 = (ClientModuleBean) rs2.getRetVal();
				    	request.setAttribute("clientModule", cBean2);
			    	}
	    		}
	    		*/
	    	}catch (Exception e) {
				e.printStackTrace();
			}
	        return getForward(request, mapping, "to_updateClientModule");
	    }
		  
	   /**
	    * 修改客户模版前的准备
	    * @param mapping
	    * @param form
	    * @param request
	    * @param response
	    * @return
	    * @throws Exception
	    */
	    protected ActionForward updatePrepare(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	try{
		    	String keyId=this.getParameter("keyId", request);
		    	Result rs=mgt.detailCrmModule(keyId);
		    	ClientModuleBean cBean = (ClientModuleBean) rs.getRetVal();
		    	LoginBean loginBean = getLoginBean(request);
				request.setAttribute("type", request.getParameter("addType"));
				request.setAttribute("clientModule", cBean);
				request.setAttribute("mainTableName", tableInfo.split(":")[0]);
		    	request.setAttribute("childTableName", tableInfo.split(":")[1]);
		    	request.setAttribute("loginBean", loginBean);
		    	OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get("CRMClientInfo") ;
		    	request.setAttribute("workFlow", workFlow);
	    	}catch (Exception e) {
				e.printStackTrace();
				EchoMessage.error().add(getMessage(request, "news.not.find"))
						.setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
	    	
	        return getForward(request, mapping, "to_updateClientModule");
	    }
	  
	    
	    /**
		    * 修改模板视图前的准备
		    * @param mapping
		    * @param form
		    * @param request
		    * @param response
		    * @return
		    * @throws Exception
		    */
		    protected ActionForward viewUpdatePrepare(ActionMapping mapping,
		                                   ActionForm form,
		                                   HttpServletRequest request,
		                                   HttpServletResponse response) throws
		            Exception {
		    	try{
			    	Result rs=mgt.loadModuleView(viewId);
			    	ClientModuleViewBean moduleViewBean = (ClientModuleViewBean) rs.getRetVal();
			    	/*获取授权对象（用户、部门、组）*/
					List<EmployeeBean> targetUsers =omgt.getEmployee(moduleViewBean.getPopedomUserIds());
					List<Object> targetDept = omgt.getDepartment(moduleViewBean.getPopedomDeptIds());
					List<Object> listEmpGroup = omgt.getEmpGroup(moduleViewBean.getPopedomEmpGroupIds());
					request.setAttribute("moduleViewBean", moduleViewBean);
					request.setAttribute("targetUsers", targetUsers);
					request.setAttribute("targetDept", targetDept);
					request.setAttribute("targetEmpGroup", listEmpGroup);
					request.setAttribute("moduleViewBean", moduleViewBean);
		    	}catch (Exception e) {
					e.printStackTrace();
					EchoMessage.error().add(getMessage(request, "news.not.find"))
							.setNotAutoBack().setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
		    	
		        return getForward(request, mapping, "to_updateClientModuleView");
		    }
		    
		    
	    /**
		    * 修改字段权限设置前的准备
		    * @param mapping
		    * @param form
		    * @param request
		    * @param response
		    * @return
		    * @throws Exception
		    */
	    protected ActionForward updateScopePrepare(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	try{
		    	String keyId=this.getParameter("keyId", request);
		    	Result rs=mgt.detailFieldScope(keyId);
		    	FieldScopeSetBean cBean = (FieldScopeSetBean) rs.getRetVal();
		    	
		    	/*获取字段只读权限分配（用户、部门）*/
				List<EmployeeBean> readtargetUsers =omgt.getEmployee(cBean.getReadpopedomUserIds());
				List<Object> readtargetDept = omgt.getDepartment(cBean.getReadpopedomDeptIds());

				/*获取字段隐藏权限分配（用户、部门）*/
				List<EmployeeBean> hiddentargetUsers =omgt.getEmployee(cBean.getHiddenpopedomUserIds());
				List<Object> hiddentargetDept = omgt.getDepartment(cBean.getHiddenpopedomDeptIds());
				
				request.setAttribute("fieldScope", cBean);
				request.setAttribute("readtargetUsers", readtargetUsers);
				request.setAttribute("readtargetDept", readtargetDept);
				request.setAttribute("hiddentargetUsers", hiddentargetUsers);
				request.setAttribute("hiddentargetDept", hiddentargetDept);
				
	    	}catch (Exception e) {
				e.printStackTrace();
				EchoMessage.error().add(getMessage(request, "news.not.find"))
						.setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
	        return getForward(request, mapping, "updfieldScopeSet");
	    }
    
	    /**
	     * 删除客户模版
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward delete(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	Result rs = null;
	    	Hashtable map = BaseEnv.enumerationMap;
	    	Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
	    	String[] keyIds = request.getParameter("keyId").split(";");
	    	List<String> tableIds = new ArrayList<String>();//存放tableBean
	    	List<String> enumerIds = new ArrayList<String>();//存放枚举
	    	List<String> delTableInfo = new ArrayList<String>();
	    	DBTableInfoBean tableBean = new DBTableInfoBean();
	    	DBTableInfoBean chlidBean = new DBTableInfoBean();
	    	for(int i=0;i<keyIds.length;i++){
	    		rs = mgt.detailCrmModule(keyIds[i]);
	    		ClientModuleBean bean = (ClientModuleBean)rs.retVal;
	    		if(!"CRMClientInfo".equals(bean.getTableInfo().split(":")[0])){
	    			tableBean = (DBTableInfoBean) allTables.get(bean.getTableInfo().split(":")[0]);
		    		chlidBean = (DBTableInfoBean) allTables.get(bean.getTableInfo().split(":")[1]);
		    		tableIds.add(tableBean.getId());
		    		tableIds.add(chlidBean.getId());
		    		delTableInfo.add(bean.getTableInfo());
		    		enumerIds.add(((EnumerateBean)map.get(bean.getTableInfo().split(":")[0])).getId());
		    		for(DBFieldInfoBean fieldBean : tableBean.getFieldInfos()){
		    			if(fieldBean.getInputType() == 1){
		    				enumerIds.add(((EnumerateBean)map.get(fieldBean.getRefEnumerationName())).getId());
		    			}
		    		}
		    		for(DBFieldInfoBean fieldInfo : chlidBean.getFieldInfos()){
		    			if(fieldInfo.getInputType() == 1){
		    				enumerIds.add(((EnumerateBean)map.get(fieldInfo.getRefEnumerationName())).getId());
		    			}
		    		}
	    		}
	    		
	    	}
	    	
	    	Result result = mgt.deleteCrmModule(keyIds,tableIds,enumerIds);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				Hashtable table2 = BaseEnv.tableInfos ;
				for(String tableName : delTableInfo){
					allTables.remove(tableName.split(":")[0]);
					allTables.remove(tableName.split(":")[1]);
					table2.remove(tableName.split(":")[0]);
					table2.remove(tableName.split(":")[1]);
					//更新内存数据
			        InitMenData imd = new InitMenData();
			        //更新内存中枚举值
			        imd.initEnumerationInformation();
				}
				request.getSession().getServletContext().setAttribute(BaseEnv.TABLE_INFO, allTables);
				BaseEnv.tableInfos = table2;
				// 删除成功
				return new ActionForward("/ClientSettingAction.do?operation=4",true);
			} else {
				// 删除失败
				EchoMessage.error().add(getMessage(request, "common.msg.delError"))
						.setAlertRequest(request);
			}
			return getForward(request, mapping, "message");
	        
	    }
	    
	    /**
	     * 删除字段权限设置
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward delFieldScope(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	String[] keyIds = request.getParameter("keyId").split(";");
	    	Result result = mgt.deleteFieldScope(keyIds);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {					
				// 删除成功
				EchoMessage.success().add(
						getMessage(request, "common.msg.delSuccess")).setBackUrl(
						"/FieldScopeSetAction.do?operation=4&queryType=fieldScopeSet&viewId="+viewId).setAlertRequest(request);
				return getForward(request, mapping, "message");
		
			} else {
				// 删除失败
				EchoMessage.error().add(getMessage(request, "common.msg.delError"))
						.setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
	        
	    }
	    
	    /**
	     * 字段维护
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */ 
	    @SuppressWarnings("unchecked")
		protected ActionForward fieldsMtain(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws Exception {
	    	Map map = BaseEnv.enumerationMap;
			EnumerateBean beans[] = (EnumerateBean[]) map.values().toArray(new EnumerateBean[0]);
			HashMap<String,String> enumerMap = new HashMap<String,String>();
			String enumerationId = "";
			for (int i = 0; i < beans.length; i++) {
				enumerMap.put(beans[i].getEnumName(), beans[i].getId());
			}
	        
			
			
			Hashtable allMap = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			//获取子表
			ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(tableInfo.split(":")[0], allMap);
			
	        request.setAttribute("cliTableName", tableInfo.split(":")[0]);
	    	request.setAttribute("enumerMap", enumerMap);
	    	request.setAttribute("mainTableName", tableInfo.split(":")[0]);
	    	request.setAttribute("childTableName", tableInfo.split(":")[1]);
	    	request.setAttribute("childTableList",childTableList);
	    	request.setAttribute("loginBean",getLoginBean(request));
	        return mapping.findForward("fieldsMtain");
	    }
	    
	    /**
	     * 字段维护修改操作
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    protected ActionForward updateField(ActionMapping mapping,
						                ActionForm form,
						                HttpServletRequest request,
						                HttpServletResponse response) throws Exception {
	    	
	    	//处理新增字段
	    	String[] fieldNameNew = request.getParameterValues("fieldNameNew");//字段名
	    	String[] inputTypeNew = request.getParameterValues("inputTypeNew");//输入类型
	    	String[] defineTableName = request.getParameterValues("defineTableName");//默认值
	    	String[] widthNew = request.getParameterValues("widthNew");//宽度
	    	String[] groupNameNew = request.getParameterValues("groupNameNew");//分组值
	    	String[] isNullVal = request.getParameterValues("isNullVal");//是否为空
	    	String[] statusIdVal = request.getParameterValues("statusIdVal");//启用
	    	String[] isUniqueVal = request.getParameterValues("isUniqueVal");//是否唯一
	    	String addChildTableName = "";//记录明细表名，用于操作明细表
	    	String saveReloadTableName = "";//存放需要重新加载的表名
	    	Boolean isReloadEnumer = false;//是否更新加载枚举
	    	Result rs= new Result();
	    	
	    	List<String> publicSqlList = new ArrayList<String>();//存放公用脚本
    		List<DBFieldInfoBean> saveBeanList = new ArrayList<DBFieldInfoBean>();//存放新增字段bean
    		
	    	String updatelistFields = "";//记录启用的新字段
	    	String tableName = tableInfo.split(":")[0];//主表
	    	String contactTableName = tableInfo.split(":")[1];//联系人表
	    	if(fieldNameNew !=null && fieldNameNew.length>0){
	    		DBTableInfoBean mainTableInfo = GlobalsTool.getTableInfoBean(tableName);
	    		DBTableInfoBean childTableInfo = new DBTableInfoBean();
	    		short mainCount = mainTableInfo.getFieldInfos().get(mainTableInfo.getFieldInfos().size()-1).getListOrder();//获得主表结构最后一个排序值用于新增字段的的排序值累加
	    		short childCount = 0;//获得从表结构最后一个排序值用于新增字段的的排序值累加
	    		for(int i=0;i<fieldNameNew.length;i++){
	    			if(!"".equals(fieldNameNew[i])){
	    				DBFieldInfoBean fieldBean = new DBFieldInfoBean();
	    				if(fieldNameNew[i]!=null && fieldNameNew[i].startsWith("change_")){
	    					addChildTableName = fieldNameNew[i].substring(7);//记录明细表名称，用于添加物理表字段与DBFieldInfo
	    					
	    					//记录明细表tableBean与最后一个字段序号
	    					childTableInfo =  GlobalsTool.getTableInfoBean(addChildTableName);
	    					childCount = childTableInfo.getFieldInfos().get(childTableInfo.getFieldInfos().size()-1).getListOrder();
	    					
	    					saveReloadTableName +=addChildTableName+",";//累加明细表名从新加载表结构
	    					continue;
	    				}
	    				
	    				int maxLength = 2000;//最大长度与物理表长度
	    				if("2".equals(inputTypeNew[i])){
	    					fieldBean.setInputType((byte)2);
	    					fieldBean.setFieldType((byte)2);
	    					fieldBean.setInputValue(defineTableName[i]);
	    				}else if(inputTypeNew[i].indexOf("fieldType") == -1){
	    					fieldBean.setInputType((byte)(Integer.parseInt(inputTypeNew[i])));
	    					fieldBean.setFieldType((byte)2);
	    				}else{
	    					if(Integer.parseInt(inputTypeNew[i].split(":")[1]) == 3){
	    						maxLength =8000;
	    					}
	    					fieldBean.setFieldType((byte)(Integer.parseInt(inputTypeNew[i].split(":")[1])));
	    					fieldBean.setInputType((byte)0);
	    					
	    				}
	    				String languageId = IDGenerater.getId();
	    				fieldBean.setId(IDGenerater.getId());
	    				
	    				//如果少于两个等于字母或汉字用全拼
	    				if(fieldNameNew[i].length()<=2){
	    					fieldBean.setFieldName(ChineseSpelling.getSelling(fieldNameNew[i]));
	    				}else{
	    					fieldBean.setFieldName(CustomizePYM.getFirstLetter(fieldNameNew[i]));
	    				}
	    				
	    				//判断fieldName是否存在
	    				if(!"change".equals(fieldBean.getFieldName())){
			    			int colNum = mgt.checkCols(fieldBean.getFieldName());//查看是否已经存在此字段
			    			if(colNum>0){
			    				colNum = colNum+i+1;
			    				while(true){
			    					int temp = mgt.checkCols(fieldBean.getFieldName()+String.valueOf(colNum));
			    					if(temp==0){
			    						fieldBean.setFieldName(fieldBean.getFieldName()+String.valueOf(colNum));//有的话.默认后面+i+1
			    						break;
			    					}else{
			    						colNum++;
			    					}
			    				}
			    			}else{
			    				int tempCount = 0;//临时变量,用于记录有多少个变量
			    				for(DBFieldInfoBean bean : saveBeanList){
			    					if(bean.getFieldName().indexOf(fieldBean.getFieldName()) != -1){
			    						tempCount ++;//存在+1
			    					}
			    				}
			    				if(tempCount != 0){
			    					fieldBean.setFieldName(fieldBean.getFieldName()+tempCount);
			    				}
			    			}
			    		}
	    				fieldBean.setIsNull((byte)(Integer.parseInt(isNullVal[i])));
	    				fieldBean.setIsUnique((byte)(Integer.parseInt(isUniqueVal[i])));
	    				fieldBean.setStatusId((Integer.parseInt(statusIdVal[i])));
	    				if((Integer.parseInt(statusIdVal[i])) == 1){
    						if("".equals(addChildTableName)){
    							//表示主表
    							updatelistFields += fieldBean.getFieldName() +",";
    						}else{
    							//明细，只有联系人才记录字段
    							if("CRMClientInfoDet".equals(addChildTableName)){
    								updatelistFields += "contact"+fieldBean.getFieldName() +",";
    							}
    						}
	    				}
	    				
	    				fieldBean.setLanguageId(languageId);
	    				fieldBean.setMaxLength(maxLength);
	    				publicSqlList.add("insert into tblLanguage(id,zh_TW,zh_CN,en) values('"+languageId+"','"+fieldNameNew[i]+"','"+fieldNameNew[i]+"','"+fieldNameNew[i]+"')");
	    				String fieldLength = maxLength==8000 ? "MAX" : maxLength+"";//物料表字段长度
	    				if("".equals(addChildTableName)){
	    					mainCount++;
	    					fieldBean.setListOrder(mainCount);
	    					fieldBean.setTableBean(mainTableInfo);
	    					fieldBean.setWidth(Integer.parseInt(widthNew[i]));
		    				fieldBean.setGroupName(groupNameNew[i]);
		    				publicSqlList.add("alter table crmclientInfo add "+fieldBean.getFieldName()+" varchar("+fieldLength+")");
	    				}else{
	    					childCount++;
	    					fieldBean.setListOrder(mainCount);
	    					fieldBean.setTableBean(childTableInfo);
	    					publicSqlList.add("alter table "+addChildTableName+" add "+fieldBean.getFieldName()+" varchar("+fieldLength+")");
	    				}
	    				if("1".equals(inputTypeNew[i]) || "5".equals(inputTypeNew[i])|| "10".equals(inputTypeNew[i])){
	    					fieldBean.setRefEnumerationName(this.getBeanEnumer(publicSqlList,fieldBean.getFieldName()));
	    					isReloadEnumer = true;//重新加载枚举
	    				}
	    				saveBeanList.add(fieldBean);
	    			}
	    		}
	    		if(saveBeanList.size()!=0){
	    			publicSqlList.add("update CRMClientModuleView set pageFields = pageFields + '" + updatelistFields + "' where moduleId ='"+moduleId+"'");
	    		}
	    	}
	    	
	    	//字段修改信息
	    	String changeField =request.getParameter("changeField");
	    	
	    	if(!"1".equals(changeField)){
	    		String[] fieldIdList=request.getParameter("changeField").split(";");  //获取修改的所有字段Id
				String[] valueList=request.getParameter("changeValue").split(";");  //获取修改后的字段值
				String[] nameList=request.getParameter("changeName").split(";"); //获取修改的字段名称
				//String pageFieldsTemp = oldPageFields;
				int num=0;
				for(int i=0;i<fieldIdList.length && !("").equals(fieldIdList[i]);i++){
					
					String fieldName = fieldIdList[i];
					String value = valueList[i];
					String name = nameList[i];
					
					if(fieldName.indexOf("statusId") !=-1 && (fieldName.indexOf("contact_")>-1 || fieldName.indexOf("_")==-1)){
						if(fieldName.indexOf("contact") == -1){
							if("1".equals(value)){
								publicSqlList.add("update CRMClientModuleView set pageFields = pageFields + '" + name + ",' where moduleId ='"+moduleId+"'");
							}else{
								publicSqlList.add("update CRMClientModuleView set pageFields = replace(pageFields,'"+name+",',''),listFields = replace(listFields,'"+name+",',''),searchFields = replace(searchFields,'"+name+",',''),detailFields = replace(detailFields,'"+name+",',''),keyFields = replace(keyFields,'"+name+",','') where moduleId ='"+moduleId+"'");
							}
						}else{
							if("1".equals(value)){
								publicSqlList.add("update CRMClientModuleView set pageFields = pageFields + 'contact" + name + ",' where moduleId ='"+moduleId+"'");
							}else{
								publicSqlList.add("update CRMClientModuleView set pageFields = replace(pageFields,'contact"+name+",',''),listFields = replace(listFields,'contact"+name+",',''),searchFields = replace(searchFields,'contact"+name+",',''),detailFields = replace(detailFields,'contact"+name+",',''),keyFields = replace(keyFields,'contact"+name+",','') where moduleId ='"+moduleId+"'");
							}
						}
					}
					
					if(!"update".equals(value)){
						if(fieldName.contains("languageId")){
							String locale=this.getLocale(request).toString();
							String Id=fieldName.split("%%")[1];
							publicSqlList.add("UPDATE tblLanguage set "+locale+"='"+value+"' where id='"+Id+"'");
						}else{
							String newfield=fieldName;
                     	    if(newfield.indexOf("contact_")>-1){
                     		    newfield=newfield.replaceAll("contact_", "");
                     		   publicSqlList.add("UPDATE tbldbfieldInfo SET "+newfield+"='"+value+"' where fieldName='"+name+"' and  tableId IN (SELECT id FROM tblDBTableInfo WHERE tblDBTableInfo.tableName IN ('"+tableInfo.split(":")[1] +"'))");
                     	    }if(newfield.indexOf("_")>-1){
                     	    	String[] newFieldInfo = newfield.split("_");
                      		    publicSqlList.add("UPDATE tbldbfieldInfo SET "+newFieldInfo[1]+"='"+value+"' where fieldName='"+name+"' and  tableId IN (SELECT id FROM tblDBTableInfo WHERE tblDBTableInfo.tableName IN ('"+newFieldInfo[0]+"'))");
                      	    }else{
                     	    	publicSqlList.add("UPDATE tbldbfieldInfo SET "+newfield+"='"+value+"' where fieldName='"+name+"' and  tableId IN (SELECT id FROM tblDBTableInfo WHERE tblDBTableInfo.tableName IN ('"+tableInfo.split(":")[0] +"'))");
                     	    }
						}
					}
				}
	    	}
	    	
	    	//更新分组
	    	String groupValues = request.getParameter("groupValues");
	    	String[] groupInfos = groupValues.split(",");
	    	if(groupValues !=null && !"".equals(groupValues)){
	    		String sql = "";
	    		for(int i=0;i<groupInfos.length;i++){
	    			sql = "update tblDBFieldInfo set groupName='"+groupInfos[i].split(":")[1]+"' where tableid =(select id from tblDbTableInfo where tableName='"+tableInfo.split(":")[0]+"') and fieldName='"+groupInfos[i].split(":")[0]+"'";
	    			publicSqlList.add(sql);
	    		}
	    	}
	    	
	    	rs = mgt.addFieldBeans(saveBeanList, publicSqlList);
	    	
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				
				//更新表结构
				new InitMenData().initDBInformation(request.getSession().getServletContext(),tableName);
				
				
				Hashtable allMap = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(tableName, allMap);
				
				//更新从表
				for(DBTableInfoBean tableBean:childTableList){
					new InitMenData().initDBInformation(request.getSession().getServletContext(),tableBean.getTableName());
				}

				if(isReloadEnumer){
					//更新内存数据
					InitMenData imd = new InitMenData();
					//更新内存中枚举值
					imd.initEnumerationInformation();
				}
				
				EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/ClientSettingAction.do?operation=4&queryType=fieldsMaintain&moduleId="+moduleId).setAlertRequest(request);
    			return getForward(request, mapping, "message"); 
			}else{
				EchoMessage.error().add(
    					getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
    			return getForward(request, mapping, "message");
			}
			
		}
	    /**
	     * 查询所有的客户模板
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */ 
	    protected ActionForward query(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws Exception {
	    	
	    	ClientModuleForm cForm =(ClientModuleForm)form;
	    	String firstEnter = request.getParameter("firstEnter");
	    	if(firstEnter==null){
	    		request.setAttribute("firstEnter", "true");
	    	}else{
	    		request.setAttribute("firstEnter", "false");
	    	}
	    	
	    	Result rs=mgt.queryAllModules();
	    	Result rst = mgt.infosGroupByModule();
	    	List<Object> list = (List<Object>)rst.retVal;
	    	HashMap moduleCountMap = new HashMap();
	    	if(list !=null && list.size()>0){
	    		for(int i=0;i<list.size();i++){
	    			moduleCountMap.put(((Object[])list.get(i))[0].toString(), ((Object[])list.get(i))[1].toString());
	    		}
	    	}
	    	
	    	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	    		request.setAttribute("moduleCountMap",moduleCountMap);
				request.setAttribute("moduleList", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
			}
	    	String type = request.getParameter("type");
	        return mapping.findForward("modules");
	    	
	    }
	    
	    
	    /**
	     * 查看所有的字段权限设置
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */ 
	    protected ActionForward fieldScopeSet(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws Exception {
	    	
	    	
	    	FieldScopeSetForm fForm =(FieldScopeSetForm)form;    	
	    	Result  rs=mgt.queryAllSet(fForm.getPageNo(),fForm.getPageSize(),viewId);
	    	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("setList", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
			}
	        return mapping.findForward("fieldScopeSetList");
	    }
	    
	    
	    /**
	     * 打开客户选项数据设置页面
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */ 
	    protected ActionForward crmEnumerationSet(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws Exception {
	    	try{
	    		TreeMap enumMap =mgt.queryCRMEnumerate(); //查询客户和客户从表所有的选项数据
		    	request.setAttribute("enumMap", enumMap);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	return mapping.findForward("crmEnumerateList");
	    }
	    
    /**
     * 验证模版是否已经存在
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward checkModuleName(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String moduleName = request.getParameter("moduleName");
		moduleName = URLDecoder.decode(moduleName,"UTF-8");
		String returnValue = "false";
		try {
			Result rs = mgt.findmoduleByName(moduleName);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				List<ClientModuleBean> clist = (ArrayList<ClientModuleBean>) rs.retVal;
				if (clist.size() > 0) {
					returnValue = "exist";
				}
				request.setAttribute("msg", returnValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward("blank");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}
    
	
	public DBTableInfoBean getTableInfo(Hashtable allTables,String tableName,String tempNo,String locale,List<String> stateList,boolean copyFlag) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		
		
		String defaultTableName = "CRMClientInfo";//新增的表结构名称
		if(tableName.indexOf("CRMClientInfoDet")>-1){
			defaultTableName = "CRMClientInfoDet";
		}
		
		//拷贝TABLEINFO属性
		DBTableInfoBean tableInfoNew = new DBTableInfoBean();
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		PropertyUtils.copyProperties(tableInfoNew, tableInfo);
		tableInfoNew.setId(IDGenerater.getId());
		tableInfoNew.setTableName(defaultTableName+tempNo);
		
		List<DBFieldInfoBean> list = tableInfo.getFieldInfos();
		List<DBFieldInfoBean> beanList = new ArrayList<DBFieldInfoBean>();
		List<KeyPair> enumerList = new ArrayList<KeyPair>();
		String enumerLanId = "";
		String enumerId = "";
		String enumerItemLanId = "";
		String enumerItemId = "";
		
		
		//主表需要插入分组信息,默认的枚举名称就是表名
		if(tableName.indexOf("CRMClientInfo")>-1 && tableName.indexOf("CRMClientInfoDet") == -1){
			//添加多语言分组枚举
			enumerLanId = IDGenerater.getId();
			enumerId = IDGenerater.getId();
			stateList.add("insert into tblLanguage(id,zh_TW,zh_CN,en) values('"+enumerLanId+"','分组信息','分组信息','分组信息')");
			stateList.add("insert into tblDBEnumeration values('"+enumerId+"','"+tableInfoNew.getTableName()+"','1','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"','1','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"',NULL,NULL,NULL,'finish','-1','0','"+enumerLanId+"','0','')");
			
			enumerList = GlobalsTool.getEnumerationItems("CRMClientInfo", locale);
			
			for(KeyPair keyPair : enumerList){
				//添加多语言枚举选项
				enumerItemLanId = IDGenerater.getId();
				stateList.add("insert into tblLanguage(id,zh_TW,zh_CN,en)values('"+enumerItemLanId+"','"+keyPair.getName()+"','"+keyPair.getName()+"','')");
				stateList.add("insert into tblDBEnumerationItem VALUES('"+IDGenerater.getId()+"','"+keyPair.getValue()+"','"+enumerId+"','','','','finish','-1','0','"+enumerItemLanId+"','','"+keyPair.getValue()+"')");
			}
		}
		
		//处理字段信息
		for(DBFieldInfoBean bean : list){
			
			//新增时只选择IsStat =1 的字段,或者 若是拷贝进入.选择所有字段
			if(bean.getIsStat() == 1 || copyFlag == true){
				enumerLanId = IDGenerater.getId();
				DBFieldInfoBean fieldBean = new DBFieldInfoBean();
				PropertyUtils.copyProperties(fieldBean, bean);
				fieldBean.setId(IDGenerater.getId());
				
				//表的多语言
				stateList.add("insert into tblLanguage(id,zh_TW,zh_CN,en) values('"+enumerLanId+"','"+fieldBean.getDisplay().get(locale)+"','"+fieldBean.getDisplay().get(locale)+"','"+fieldBean.getDisplay().get(locale)+"')");
				fieldBean.setLanguageId(enumerLanId);
				if(fieldBean.getInputType() == 1){
					enumerList = GlobalsTool.getEnumerationItems(bean.getRefEnumerationName(), locale);
					enumerLanId = IDGenerater.getId();
					enumerId = IDGenerater.getId();
					
					//枚举主表
					stateList.add("insert into tblLanguage(id,zh_TW,zh_CN,en) values('"+enumerLanId+"','"+fieldBean.getDisplay().get(locale)+"','"+fieldBean.getDisplay().get(locale)+"','"+fieldBean.getDisplay().get(locale)+"')");
					stateList.add("insert into tblDBEnumeration values('"+enumerId+"','"+fieldBean.getRefEnumerationName()+tempNo+"','1','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"','1','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"',NULL,NULL,NULL,'finish','-1','0','"+enumerLanId+"','0','')");
					int i =1;
					for(KeyPair keyPair : enumerList){
						enumerItemLanId = IDGenerater.getId();
						//枚举选项
						if(!"ROBOT".equals(keyPair.getName())){
							stateList.add("insert into tblLanguage(id,zh_TW,zh_CN,en)values('"+enumerItemLanId+"','"+keyPair.getName()+"','"+keyPair.getName()+"','')");
							stateList.add("insert into tblDBEnumerationItem VALUES('"+IDGenerater.getId()+"','"+keyPair.getValue()+"','"+enumerId+"','','','','finish','-1','0','"+enumerItemLanId+"','','"+i+++"')");
						}
					}
					fieldBean.setRefEnumerationName(fieldBean.getRefEnumerationName()+tempNo);
				}
				fieldBean.setTableBean(tableInfoNew);
				beanList.add(fieldBean);
			}
		}
		tableInfoNew.setFieldInfos(beanList);
		return tableInfoNew;
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
	
	/**
     * 重新加载选项数据
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	private ActionForward reloadEnum(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
//		更新内存数据
		InitMenData imd = new InitMenData();
		//更新内存中枚举值
		imd.initEnumerationInformation();
		String enumerName = request.getParameter("enumerName");
		String firstId = "";
		List<KeyPair> enumList = GlobalsTool.getEnumerationItems(enumerName, this.getLocale(request).toString());
		String str = "";
		String enumIds = "";
		for(KeyPair keyPair : enumList){
			str += "<option value='"+keyPair.getValue()+"' >"+keyPair.getName()+"</option>";
			enumIds += keyPair.getValue() + ",";
			if("".equals(firstId)){
				firstId = keyPair.getValue();
			}
		}
		
		Result result = mgt.selectGroup(enumerName);
		List<Object> list = (List<Object>)result.retVal;
		String delGroupNames = "";
		
		for(int i=0;i<list.size();i++){
			if(((Object[])list.get(i))[0].toString()!=null && !"".equals(((Object[])list.get(i))[0].toString())){
				if(enumIds.indexOf(((Object[])list.get(i))[0].toString()) == -1){
					delGroupNames += "'" + ((Object[])list.get(i))[0].toString() +"',"; 
				}else{
					if("".equals(firstId)){
						firstId = ((Object[])list.get(i))[0].toString();
					}
				}
			}
		}
		if(delGroupNames.endsWith(",")){
			delGroupNames = delGroupNames.substring(0,delGroupNames.length()-1);
		}
		
		if(!"".equals(delGroupNames)){
			Result rs = mgt.updateDelGroupName(enumerName, firstId, delGroupNames);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				new InitMenData().initDBInformation(request.getSession().getServletContext(),enumerName);
			}
		}
		
		
		request.setAttribute("msg", str);
		return mapping.findForward("blank");
	}
	
	/**
     * 删除字段
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	private ActionForward delField(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String fieldName = request.getParameter("fieldName");//字段名
		String tableName = request.getParameter("tableName");//表名

		//这里导致非默认模板，删除不了字段
		//tableName = new DynDBManager().getInsertTableName(tableName);//过滤表名
		String reltableName = new DynDBManager().getInsertTableName(tableName);//过滤表名
		
		String enumId = "";
		DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(tableName, fieldName);
		if(fieldBean.getInputType() == 1){
			Map map = BaseEnv.enumerationMap;
			enumId = ((EnumerateBean)map.get(fieldBean.getRefEnumerationName())).getId();
		}
		List<String> sqlList = new ArrayList<String>();
		
		String tableNameStr = "";//存放模板表结构表名
		if("CRMClientInfo".equals(tableName) || "CRMClientInfoDet".equals(tableName)){
			//处理多个模板是否有同一字段，删除物理表字段
			Result result=mgt.queryAllModules();
			ArrayList<ClientModuleBean> moduleList = (ArrayList<ClientModuleBean>)result.retVal;
			
			int arrIndex = 0;//取模板表名称的数组下标，主表为0
			if("CRMClientInfoDet".equals(tableName)){
				//子表下标为1
				arrIndex = 1;
			}
			for(ClientModuleBean bean : moduleList){
				tableNameStr += "'" + bean.getTableInfo().split(":")[arrIndex] +"',";
			}
		}else{
			tableNameStr += "'" + tableName +"'";
		}
		
		if(tableNameStr.endsWith(",")){
			tableNameStr = tableNameStr.substring(0,tableNameStr.length()-1);
		}
		
		//根据所有表结构表名与字段名查找字段个数
		Result result = mgt.checkFieldCount(tableNameStr, fieldBean.getFieldName());
		if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", "no");
			return mapping.findForward("blank");
		}
		ArrayList<Object> list = (ArrayList<Object>)result.retVal;
		int count = (Integer)GlobalsTool.get(list.get(0),0);//字段个数,只有一个模板用到此字段就删除物理表字段
		
		if("CRMClientInfoDet".equals(reltableName)){
			sqlList.add("update CRMClientModuleView set pageFields = replace(pageFields,'contact"+fieldName+",',''),listFields = replace(listFields,'contact"+fieldName+",',''),searchFields = replace(searchFields,'contact"+fieldName+",',''),detailFields = replace(detailFields,'contact"+fieldName+",',''),keyFields = replace(keyFields,'contact"+fieldName+",','') where moduleId ='"+moduleId+"'");
		}else if("CRMClientInfo".equals(reltableName)){
			sqlList.add("update CRMClientModuleView set pageFields = replace(pageFields,'"+fieldName+",',''),listFields = replace(listFields,'"+fieldName+",',''),searchFields = replace(searchFields,'"+fieldName+",',''),detailFields = replace(detailFields,'"+fieldName+",',''),keyFields = replace(keyFields,'"+fieldName+",','') where moduleId ='"+moduleId+"'");
		}
		
		if(count==1){
			sqlList.add("ALTER TABLE "+reltableName+" DROP COLUMN " + fieldName);
		}
		
		sqlList.add("delete from tblDBFieldInfo where id='"+fieldBean.getId()+"'");
		sqlList.add("delete from tbllanguage where id='"+fieldBean.getLanguageId()+"'");
		if(!"".equals(enumId) && fieldBean.getRefEnumerationName().startsWith("extent_")){ //只有extent_表头的枚举才是字段自动产生的，可以删除，否则有可能是公用枚举，删除可能会影响其它表
			sqlList.add("delete from tblDBEnumeration where id='"+enumId+"'");
			sqlList.add("delete from tblDBEnumerationItem where id='"+enumId+"'");
		}
		Result rs = mgt.delField(sqlList);
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//更新表结构
			new InitMenData().initDBInformation(request.getSession().getServletContext(),tableName);
			request.setAttribute("msg", "ok");
		}else{
			request.setAttribute("msg", "no");
		}
		return mapping.findForward("blank");
	}
	
	
	/**
     * 根据模板ID,查找所有视图
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */ 
    protected ActionForward queryModuleView(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
    	LoginBean login = getLoginBean(request) ;
    	ClientModuleForm cForm =(ClientModuleForm)form;
    	String firstEnter = request.getParameter("firstEnter");
    	if(firstEnter==null){
    		request.setAttribute("firstEnter", "true");
    	}else{
    		request.setAttribute("firstEnter", "false");
    	}
    	
    	Result rs=mgt.queryModuleViewsByModuleId(login,moduleId);
    	List<ClientModuleViewBean> moduleViewList = (List<ClientModuleViewBean>)rs.retVal;
    	if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("moduleViewList", moduleViewList);
		}
        return mapping.findForward("views");
    }
    
    /**
     * 添加客户模版视图
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward addModuleView(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
    	
    	
    	String viewName = request.getParameter("viewName");
    	String viewDesc = request.getParameter("viewDesc") == null ?"":request.getParameter("viewDesc");
    	String viewId=IDGenerater.getId();
    	HashMap map = this.getNewModuleView(viewId,moduleId,viewName, viewDesc,null);
    	ClientModuleViewBean viewBean=(ClientModuleViewBean)map.get("bean");
    	Result rs = mgt.addModuleView((List<String>)map.get("scopeSql"),viewBean);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
	        request.setAttribute("dealAsyn", "true");
	        String[] tabList=viewBean.getBrotherTables().split(",");
	        //向兄弟表排序中插入记录
	        LoginBean loginBean=this.getLoginBean(request);
	        String mainName="CRMClientInfo";
	        String f_ref=mgt.getNeighbourMainId(loginBean.getId(), mainName, viewId);
	        if(!"".equals(f_ref)){
	        	Result rsAddNeigh=mgt.addNeighbour(tabList,f_ref);
	        	if(rsAddNeigh.retCode!=ErrorCanst.DEFAULT_SUCCESS){
	        		log.debug("添加兄弟表排序记录失败");
	        	}
	        }
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("/ClientSettingAction.do?operation=4&queryType=moduleView").setAlertRequest(request);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
        return getForward(request, mapping, "alert");
    }
    
    /**
     * 删除客户模版视图
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward delModuleView(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
    	
    	Result rs = mgt.delModuleView(viewId);
    	System.out.println(moduleId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			return new ActionForward("/ClientSettingAction.do?operation=4&queryType=moduleView&moduleId="+moduleId,true);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
        return getForward(request, mapping, "alert");
    }
    /**
     * 获得视图BEAN与导入导出权限SQL语句
     * @param viewId
     * @param moduleId
     * @param viewName
     * @param viewDesc
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public HashMap getNewModuleView(String viewId,String moduleId,String viewName,String viewDesc,String copyModuleId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	
    	HashMap map = new HashMap();
//    	//新增视图,默认与本模板的默认视图相同
//    	String loadId = "1_"+moduleId;
//    	
//    	
//    	if(copyModuleId!=null && !"".equals(copyModuleId)){
//    		//如果copyModuleId有值 表示是拷贝，默认视图来源与拷贝模板的默认视图
//    		loadId = "1_"+copyModuleId;
//    	}else{
//    		if(viewId!=null && viewId.startsWith("1_")){
//    			loadId = "0";
//    		}
//    	}
    	
    	String loadId = "";//存放查询视图ID
    	if(viewId!=null && viewId.startsWith("1_")){
    		//表示新增或拷贝模板操作
    		if(copyModuleId!=null && !"".equals(copyModuleId)){
    			//如果copyModuleId有值 表示是拷贝，默认视图来源与拷贝模板的默认视图
    			loadId = "1_"+copyModuleId;
    		}else{
    			//新增取隐藏的默认模板
    			loadId = "0";
    		}
    	}else{
    		//新增视图操作
    		loadId = "1_"+moduleId;
    	}
    	
    	
    	ClientModuleViewBean defModuleViewBean = (ClientModuleViewBean)mgt.loadModuleView(loadId).retVal;
    	ClientModuleViewBean moduleViewBean = new ClientModuleViewBean();
    	PropertyUtils.copyProperties(moduleViewBean, defModuleViewBean);
    	moduleViewBean.setId(viewId);
    	moduleViewBean.setViewName(viewName);
    	moduleViewBean.setViewDesc(viewDesc);
    	moduleViewBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
    	moduleViewBean.setModuleId(moduleId);
    	
    	//新增控制导入导出权限语句
    	List<String> scopeList = new ArrayList<String>();
    	long time = new Date().getTime();
    	String [] ids=new String[]{"printScope_"+time,"importScope_"+time,"exportScope_"+time};
    	for(int i=0;i<ids.length;i++){
    		scopeList.add("insert into CRMCommonTable(id,fields1,fields2,fields3,fields4,viewId,moduleId)values ('"+ids[i]+"','1','','1,','','"+viewId+"','"+moduleId+"')");
    	}
    	map.put("bean", moduleViewBean);
    	map.put("scopeSql", scopeList);
    	return map;
    }
    
    private ActionForward checkFieldNameCN(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
    	String fieldName = getParameter("fieldName", request);
    	String checkFlag = getParameter("checkFlag", request);//是客户新增字段还是联系人
    	if(fieldName !=null){
    		try {
    			fieldName = URLDecoder.decode(fieldName,"UTF-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}

    		String tableName = tableInfo.split(":")[0];
    		if("contactRowModle".equals(checkFlag)){
    			tableName = tableInfo.split(":")[1];
    		}
    		DBTableInfoBean tableBean =  GlobalsTool.getTableInfoBean(tableName);
    		List<DBFieldInfoBean> fieldBeanList = tableBean.getFieldInfos();
    		
    		String hasEqualCn = "no";//标识是否有相同中文;
    		//循环表结构字段看是否有相同中文存在
    		for(DBFieldInfoBean fieldInfo : fieldBeanList){
    			if(fieldName.equals(fieldInfo.getDisplay().get(getLocale(request).toString()))){
    				hasEqualCn = "yes";
    				break;
    			}
    		}
			request.setAttribute("msg", hasEqualCn);
    	}
    	return getForward(request, mapping, "blank");
    }
    
    
    /**
     * 邻居表判断模块是否启用
     * @param childTabList
     * @param request
     * @return
     */
    public ArrayList checkModuleUse(ArrayList childTabList,HttpServletRequest request){
    	ArrayList moduleList = BaseEnv.allModule;
    	for(int i=0;i<childTabList.size();i++){
        	//判断模块是否启用
        	DBTableInfoBean tableIfno = (DBTableInfoBean) childTabList.get(i) ;
			Result result2 = new Result() ;
			GlobalsTool.moduleIsUsed(moduleList, tableIfno.getTableName(),result2) ;
			MOperation mop = (MOperation) getLoginBean(request).getOperationMap()
						.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName="+tableIfno.getTableName()) ;
			if(mop == null){
				//新路径
				mop = (MOperation) getLoginBean(request).getOperationMap().get("/CRMBrotherAction.do?tableName="+tableIfno.getTableName()) ;
			}
			
			if(mop == null){
				//erp路径
				mop = (MOperation) getLoginBean(request).getOperationMap()
				.get("/UserFunctionQueryAction.do?tableName="+tableIfno.getTableName()) ;
			}
			
			if(result2.retCode != ErrorCanst.DATA_ALREADY_USED || mop==null){
				childTabList.remove(i) ;
				i-- ;
			}
        }
    	return childTabList;
    }
    
    
    /**
     * CRM,ERP互转字段映射
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward clientTransferQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	Result rs = mgt.detailCrmModule(moduleId);
    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
    		//获得erp主表与从表的bean
    		DBTableInfoBean erpTableBean = GlobalsTool.getTableInfoBean("tblCompany");
    		DBTableInfoBean erpTableDetBean = GlobalsTool.getTableInfoBean("tblCompanyEmployeeDet");
    		//获得crm主表与从表的bean
        	DBTableInfoBean crmTableBean = GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[0]);
        	DBTableInfoBean crmTableDetBean = GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[1]);
        	
    		request.setAttribute("moduleBean", moduleBean);
    		request.setAttribute("erpTableBean", erpTableBean);
    		request.setAttribute("erpTableDetBean", erpTableDetBean);
    		request.setAttribute("crmTableBean", crmTableBean);
    		request.setAttribute("crmTableDetBean", crmTableDetBean);
    		return getForward(request, mapping, "clientTransfer");
    	}else{
    		EchoMessage.error().add("查询失败").setAlertRequest(request);
    	}
    	return getForward(request, mapping, "alert");
	}
    
    
    /**
     * 更新CRM,ERP互转字段映射
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward updateTransfer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String transferFields = getParameter("transferFields",request);//存放映射的fields
    	Result rs = mgt.detailCrmModule(moduleId);
    	ClientModuleBean moduleBean = new ClientModuleBean();
    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		moduleBean = (ClientModuleBean)rs.retVal;
    		moduleBean.setTransferFields(transferFields);
    	}
    	rs = mgt.updateCrmModule(moduleBean);
    	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
    		EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl("/ClientSettingAction.do?operation=4&queryType=clientTransfer&moduleId="+moduleId).setAlertRequest(request);
    	}else{
    		//修改失败
    		EchoMessage.error().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
    	}
    	return getForward(request, mapping, "alert");
		
	}
}