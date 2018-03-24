package com.menyi.aio.web.stockgoods;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.StockGoodsModuleBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:库存商品图片Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-08-08 上午 10:15
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class StockGoodsAction extends MgtBaseAction{
	
	StockGoodsMgt mgt = new StockGoodsMgt();
	
	/**
	 * exe 控制器入口函数
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
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		
		/* 不同操作 */
		String optype = request.getParameter("optype");
		
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			/* 添加模板前 */
			forward = addModulePre(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			/* 添加模板 */
			forward = addModule(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			/* 修改模板前 */
			forward = updateModulePre(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			/* 修改模板 */
			forward = updateModule(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			/* 删除模板 */
			forward = deleteModule(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			/* 查询库存商品图片 */
			forward = queryStockGoods(mapping, form, request, response);
			break;
		default:
			if(optype != null && "queryModule".equals(optype)){
				/* 查询保存的模板 */
				forward = queryModule(mapping, form, request, response);
			}else{
				/* 查询库存商品图片 */
				forward = queryStockGoods(mapping, form, request, response);
			}
		}
		return forward;
	}
	
	/**
	 * 查询库存商品图片
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryStockGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String moduleId = request.getParameter("moduleId");					//模板Id
		
		/* 查询所有模板 */
		Result rs = mgt.queryModule();
		StockGoodsModuleBean moduleBean = null;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List list = (ArrayList)rs.retVal;
			if(list != null && list.size()>0){
				if(moduleId == null || "".equals(moduleId)){
					moduleBean = (StockGoodsModuleBean)list.get(0);				//取默认模板
				}else{
					moduleBean = (StockGoodsModuleBean)mgt.loadModule(moduleId).retVal;
				}
			}
			request.setAttribute("moduleList", list);
		}
		
		request.setAttribute("moduleBean", moduleBean);
		/* 根据模板取用户设置的搜索项和显示项 */
		
		HashMap searchMap = new HashMap();									//搜索数据
		
		List searchList = new ArrayList();									//保存搜索的字段bean
		
		List showList = new ArrayList();									//要显示的字段数据
		
		//搜索项
		if(moduleBean.getSearchFields() != null){
			for(String searchField : moduleBean.getSearchFields().split(",")){
				//根据值获取字段信息（tblname.field）
				DBFieldInfoBean dbField = GlobalsTool.getFieldBean(searchField);
				if(dbField != null){
					//获取数据
					String parameValue = getParameter(dbField.getFieldName(), request) ;
					searchMap.put(dbField.getFieldName(), parameValue);
					if(dbField.getInputType() == 2){
						searchMap.put(dbField.getFieldName()+"_name", getParameter(dbField.getFieldName()+"_name", request));
					}
					searchList.add(dbField);
				}
			}
		}
		request.setAttribute("searchList", searchList);
		request.setAttribute("searchMap", searchMap);
		
		String fields = "";										//商品表的字段
		String otherField = "";
		//显示项
		if(moduleBean.getShowFields() != null){
			for(String showField : moduleBean.getShowFields().split(",")){
				if(!"".equals(showField)){
					DBFieldInfoBean dbField = GlobalsTool.getFieldBean(showField);
					if(dbField != null){
						if(dbField.getInputType() == 2){
							//弹出框
							if("tblGoods.BaseUnit".equals(showField)){
								//单位
								fields += "(select unitName from tblUnit where tblUnit.id=tblGoods.BaseUnit) as BaseUnit,";
							}else if("tblGoods.CompanyCode".equals(showField)){
								//供应商
								fields += "(select ComFullName from tblCompany where tblCompany.classCode=tblGoods.CompanyCode) as CompanyCode,";
							}
						}else{
							fields += showField+",";
						}
					}else{
						dbField = new DBFieldInfoBean();
						//库存数量,库存匹数,库存单价,金额处理
						if("LastQty".equals(showField) || "LastTwoQty".equals(showField) ||
								"lastAmount".equals(showField)){
							otherField += "isnull(sum("+showField+"),0) as "+showField+",";
						}else if("lastPrice".equals(showField)){
							otherField += "(CASE isnull(sum(LastQty),0) WHEN 0 THEN 0 ELSE isnull(sum(lastAmount),0)/isnull(sum(LastQty),0) END) as "+showField+",";
						}
						dbField.setFieldName(showField);
					}
					showList.add(dbField);
				}
			}
		}
		request.setAttribute("showList", showList);
		
		//排序
		String orders = request.getParameter("");
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	pageSize = 30;
        }
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("pageNo", pageNo);
		//查询数据
		Result result = mgt.queryStockGoods(searchMap,fields,otherField,orders,pageNo,pageSize);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			List list = (ArrayList)result.retVal;
			request.setAttribute("goodsList", list) ;
			request.setAttribute("pageBar",pageBar2(result, request)) ;
		}
		return getForward(request, mapping, "stockgoodsList");
	}
	
	
	/**
	 * 查询设置的显示模板
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward queryModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Result rs = mgt.queryModule();
		/* 查询成功 */
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List list = (ArrayList)rs.retVal;
			request.setAttribute("list", list);
		}
		return getForward(request, mapping, "moduleList");
	}
	
	/**
	 * 添加模板前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addModulePre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		/* 取商品表的字段数据 */
		String tableName = "tblGoods";
    	Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
    	List fields = tableInfo.getFieldInfos();
    	List searchList = new ArrayList();									//搜索的list
    	List showList = new ArrayList();									//显示的list
    	HashMap map = new HashMap();
    	HashMap showMap = new HashMap();
    	for(int i=0;i<fields.size();i++){
    		DBFieldInfoBean fieldBean = (DBFieldInfoBean)fields.get(i);
    		map = new HashMap();
    		showMap = new HashMap();
    		if(fieldBean.getInputType() == 0 || fieldBean.getInputType() == 2 || fieldBean.getInputType() == 7){
    			//保存字段类型为输入的数据
    			map.put("name", fieldBean.getDisplay().get(getLocale(request).toString()));
    			map.put("value", tableName+"."+fieldBean.getFieldName());
    			map.put("isnull", fieldBean.getIsNull());
    			searchList.add(map);
    			showMap.put("name", fieldBean.getDisplay().get(getLocale(request).toString()));
    			showMap.put("value", tableName+"."+fieldBean.getFieldName());
    			//必须显示图片
    			if("tblGoods.Picture".equals(tableName+"."+fieldBean.getFieldName())){
    				showMap.put("isnull", 1);
    			}else{
    				showMap.put("isnull", fieldBean.getIsNull());
    			}
    			showList.add(showMap);
    		}
    	}
    	
    	//显示库存数量和金额
    	String[] strName = new String[]{"库存数量","库存匹数","库存单价","金额"};
    	String[] strValue = new String[]{"LastQty","LastTwoQty","lastPrice","lastAmount"};
    	for(int i =0;i<strName.length;i++){
    		map = new HashMap();
    		map.put("name", strName[i]);
    		map.put("value", strValue[i]);
    		map.put("isnull", 0);
    		showList.add(map);
    	}
    	request.setAttribute("searchList", searchList);
    	request.setAttribute("showList", showList);
		
		return getForward(request, mapping, "dealModule");
	}
	
	
	/**
	 * 添加模板
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String moduleName = request.getParameter("moduleName");						//模板名称
		String moduleDesc = request.getParameter("moduleDesc");						//模板描述
		String searchFields = request.getParameter("searchFields");					//搜索字段
		String showFields = request.getParameter("showFields");						//显示字段
		
		StockGoodsModuleBean bean = new StockGoodsModuleBean();						//创建bean
		
		LoginBean lg = this.getLoginBean(request);									//获取保存在内存中的登陆用户信息
		
		/**
		 * 给bean中附值
		 */
		bean.setCreateBy(lg.getId());
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		bean.setId(IDGenerater.getId());
		bean.setModuleDesc(moduleDesc);
		bean.setModuleName(moduleName);
		bean.setSearchFields(searchFields);
		bean.setShowFields(showFields);
		
		
		/* 保存数据 */
		Result rs = mgt.addModule(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//添加成功
			request.setAttribute("dealAsyn", "true");
			String msg = "common.msg.addSuccess";
			EchoMessage.success().add(getMessage(
                    request, msg)).setBackUrl("/StockGoodsAction.do?optype=queryModule").
                    setAlertRequest(request);
		}else{
			//添加失败
			String msg = "common.msg.addFailture";
			EchoMessage.error().add(getMessage(request, msg)).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 模块修改前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updateModulePre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/* 取商品表的字段数据 */
		String tableName = "tblGoods";
    	Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
    	List fields = tableInfo.getFieldInfos();
    	List searchList = new ArrayList();
    	List showList = new ArrayList();									//显示的list
    	HashMap map = new HashMap();
    	HashMap showMap = new HashMap();
    	for(int i=0;i<fields.size();i++){
    		DBFieldInfoBean fieldBean = (DBFieldInfoBean)fields.get(i);
    		map = new HashMap();
        	showMap = new HashMap();
    		if(fieldBean.getInputType() == 0 || fieldBean.getInputType() == 2 || fieldBean.getInputType() == 7){
    			//保存字段类型为输入的数据
    			map.put("name", fieldBean.getDisplay().get(getLocale(request).toString()));
    			map.put("value", tableName+"."+fieldBean.getFieldName());
    			map.put("isnull", fieldBean.getIsNull());
    			searchList.add(map);
    			showMap.put("name", fieldBean.getDisplay().get(getLocale(request).toString()));
    			showMap.put("value", tableName+"."+fieldBean.getFieldName());
    			//必须显示图片
    			if("tblGoods.Picture".equals(tableName+"."+fieldBean.getFieldName())){
    				showMap.put("isnull", 1);
    			}else{
    				showMap.put("isnull", fieldBean.getIsNull());
    			}
    			showList.add(showMap);
    		}
    	}
    	
    	//显示库存数量和金额
    	String[] strName = new String[]{"库存数量","库存匹数","库存单价","金额"};
    	String[] strValue = new String[]{"LastQty","LastTwoQty","lastPrice","lastAmount"};
    	for(int i =0;i<strName.length;i++){
    		map = new HashMap();
    		map.put("name", strName[i]);
    		map.put("value", strValue[i]);
    		map.put("isnull", 0);
    		showList.add(map);
    	}
		
    	request.setAttribute("searchList", searchList);
    	request.setAttribute("showList", showList);
    	
    	/* 根据模板Id取模板数据 */
    	String id = request.getParameter("id");
    	Result rs = mgt.loadModule(id);
    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		//加载成功
    		request.setAttribute("moduleBean", rs.retVal);
    	}
		return getForward(request, mapping, "dealModule");
	}
	
	
	/**
	 * 修改模板数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updateModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String id = request.getParameter("id");										//id
		String moduleName = request.getParameter("moduleName");						//模板名称
		String moduleDesc = request.getParameter("moduleDesc");						//模板描述
		String searchFields = request.getParameter("searchFields");					//搜索字段
		String showFields = request.getParameter("showFields");						//显示字段
		
		StockGoodsModuleBean bean = (StockGoodsModuleBean)mgt.loadModule(id).retVal;//根据id获取信息
		
		/**
		 * 给bean中附值
		 */
		bean.setModuleDesc(moduleDesc);
		bean.setModuleName(moduleName);
		bean.setSearchFields(searchFields);
		bean.setShowFields(showFields);
		
		
		/* 保存数据 */
		Result rs = mgt.updateModule(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//修改成功
			request.setAttribute("dealAsyn", "true");
			String msg = "common.msg.updateSuccess";
			EchoMessage.success().add(getMessage(
                    request, msg)).setBackUrl("/StockGoodsAction.do?optype=queryModule").
                    setAlertRequest(request);
		}else{
			//修改失败
			String msg = "common.msg.updateFailture";
			EchoMessage.error().add(getMessage(request, msg)).setAlertRequest(request);
		}
		
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 删除模板
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward deleteModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String id = request.getParameter("id");
		Result rs = mgt.deleteModule(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			if(Integer.valueOf(rs.retVal.toString())>0){
				request.setAttribute("msg", "ok");
			}
		}
		return getForward(request, mapping, "blank");
	}
}
