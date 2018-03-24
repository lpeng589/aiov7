package com.koron.crm.report;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.crm.bean.BrotherFieldDisplayBean;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.koron.crm.brotherSetting.CRMBrotherSettingMgt;
import com.koron.crm.client.CRMClientMgt;
import com.koron.crm.setting.ClientSetingMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.ReportField;

public class CRMReportAction extends BaseAction{
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	ClientSetingMgt moduleMgt = new ClientSetingMgt() ;
	CRMClientMgt clientMgt = new CRMClientMgt() ;
	CRMReportMgt mgt = new CRMReportMgt();
	@Override
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
		case OperationConst.OP_QUERY:
				if("compare".equals(type)){
					forward = compareQuery(mapping, form, request, response);
				}else if("weekMonth".equals(type)){
					forward = weekMonthQuery(mapping, form, request, response);
				}else if("weekMonthDetail".equals(type)){
					forward = weekMonthDetailQuery(mapping, form, request, response);
				}else if("checkUnit".equals(type)){
					forward = checkUnit(mapping, form, request, response);
				}else if("detailList".equals(type)){
					forward = detailList(mapping, form, request, response);
				}else{
					forward = query(mapping, form, request, response);
				}
				
			break;
		default:
			if("left".equals(type))
				forward = left(mapping, form, request, response);
			else
				forward = frame(mapping, form, request, response);
			break;
		}
		return forward;
	}

	/**
	 * 主框架
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward frame(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String compareFlag = getParameter("compareFlag", request);
		if(compareFlag == null){
			LoginBean login = getLoginBean(request) ;
			String moduleId = "";
			Result result = moduleMgt.queryMyModules(login) ;
			List<String[]> moduleList = (ArrayList<String[]>)result.retVal;//获取模板信息
			request.setAttribute("moduleList", moduleList) ;
			if(moduleList!=null && moduleList.size()>0){
				moduleId = moduleList.get(0)[1];
			}
			request.setAttribute("moduleId", moduleId);
		}
		request.setAttribute("compareFlag", compareFlag);
		return getForward(request, mapping, "frame");
	}
	
	/**
	 * 左半部
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward left(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String classesName = getParameter("classesName", request);//分类名称
		String tableName = getParameter("tableName", request);//表结构表名
		String compareFlag = getParameter("compareFlag", request);//是否同比环比报表
		//String firstEnter = getParameter("firstEnter", request);//是否第一次进入
		if(classesName ==null || "".equals(classesName)){
			classesName ="clientInfo";
		}
//		if(firstEnter == null){
//			firstEnter = "true";
//		}
		
		if("clientInfo".equals(classesName)){
			String moduleId = getParameter("moduleId", request);
			LoginBean login = getLoginBean(request) ;
			Result result = moduleMgt.queryMyModules(login) ;
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				List<String[]> moduleList = (ArrayList<String[]>)result.retVal;//获取模板信息
				if((moduleId ==null || "".equals(moduleId)) && moduleList!=null && moduleList.size()>0){
					moduleId =moduleList.get(0)[1];
				}
				ClientModuleBean moduleBean = (ClientModuleBean)moduleMgt.detailCrmModule(moduleId).retVal;
				tableName = moduleBean.getTableInfo().split(":")[0];
				
				
				result = moduleMgt.queryModuleGroupBy(login);//查询用户是否有权限观看模板
				HashMap<String,Integer> moduleCountMap= (HashMap<String,Integer>)result.retVal;
				request.setAttribute("moduleCountMap", moduleCountMap);
				request.setAttribute("moduleId", moduleId) ;
				request.setAttribute("moduleList", moduleList) ;
			}else{
				
			}
			
		}
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName);
		request.setAttribute("fieldInfos", tableInfo.getFieldInfos()) ;
		request.setAttribute("classesName", classesName) ;
		request.setAttribute("compareFlag", compareFlag) ;
		request.setAttribute("tableName", tableName) ;
		//request.setAttribute("firstEnter", firstEnter) ;
		return getForward(request, mapping, "left");
	}
	
	
	
	/**
	 * 销售跟单报表、合同/回款报表、售后服务报表公用查询模块
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String moduleId = getParameter("moduleId", request);//模板ID
		String tableName = getParameter("tableName", request);//表名
		String sumFieldName = getParameter("sumFieldName", request);//表示统计金额
		String fieldName = getParameter("fieldName", request);//统计的字段名
		String secondFieldName = getParameter("secondFieldName", request);// "/"的统计的字段名
		String top = getParameter("top", request);// 数据库TOP标识
		String searchSwfName = getParameter("searchSwfName", request);// 选择的统计图名称
		String isSearch = getParameter("isSearch", request);// 是否查询条件进入 ,用于客户资料报表
		String isAround = getParameter("isAround", request);// true表示查询环比的分组统计
		LoginBean login = getLoginBean(request) ;
		
		request.setAttribute("isAround", isAround);
		request.setAttribute("highSearch", getParameter("highSearch", request));//true表示高级查询
		request.setAttribute("top", top);//数据库TOP标识
		request.setAttribute("searchSwfName", request.getParameter("searchSwfName"));//searchSwfName表示用户自己选择了图表.不用默认的
		
		String chartSwfName = "Column3D";//默认的FusionCharts显示图
		//String iChartName = "Bar2D";//默认的iChart显示图
		
		String xName = "";//横坐标名称
		String yName = "数量";//纵坐标名称
		if(sumFieldName != null && !"".equals(sumFieldName)){
			yName = "金额";
		}	
		
		//caption名称 
		String captionName = getParameter("captionName", request); 
		if (StringUtils.isNotBlank(captionName) && !"true".equals(isSearch)){
			captionName = GlobalsTool.toChinseChar(captionName);
    	}
		
		DBFieldInfoBean fieldInfoBean = new DBFieldInfoBean();
		ClientModuleBean moduleBean = new ClientModuleBean();
		ClientModuleViewBean moduleViewBean = new ClientModuleViewBean();
		String dbTableName = tableName;//查询枚举sql时需要用到的tableName
		if(moduleId !=null && !"".equals(moduleId)){
			//客户资料报表
			moduleBean = (ClientModuleBean)moduleMgt.detailCrmModule(moduleId).retVal;
			moduleViewBean = (ClientModuleViewBean)moduleMgt.loadModuleView("1_"+moduleId).retVal;
			if(fieldName == null || "".equals(fieldName)){
				//第一次进入，默认取inputType==1第一个
				DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[0]);
				for(DBFieldInfoBean fieldInfo : tableInfo.getFieldInfos()){
					if(fieldInfo.getInputType() == 1 && !"Level".equals(fieldInfo.getFieldName()) && !"LastContractTime".equals(fieldInfo.getFieldName()) && !"extend1".equals(fieldInfo.getFieldName()) && !"extend2".equals(fieldInfo.getFieldName()) && !"extend3".equals(fieldInfo.getFieldName())){
						fieldInfoBean = fieldInfo;
						fieldName = fieldInfo.getFieldName();
						captionName = fieldInfoBean.getDisplay().get(getLocale(request).toString()) +"分布";
						break;
					}
				}
				request.setAttribute("firstEnter", "true");
			}else{
				fieldInfoBean = GlobalsTool.getField(fieldName, moduleBean.getTableInfo().split(":")[0], moduleBean.getTableInfo().split(":")[1]);
				dbTableName = moduleBean.getTableInfo().split(":")[0];
			}
			tableName = "CRMClientInfo";
			request.setAttribute("moduleViewBean", moduleViewBean);
			request.setAttribute("clientTableName",moduleBean.getTableInfo().split(":")[0]);
			request.setAttribute("contactTableName",moduleBean.getTableInfo().split(":")[1]);
		}else{
			fieldInfoBean = GlobalsTool.getFieldBean(tableName,fieldName);
		}
		
		if(fieldInfoBean == null){
			fieldInfoBean = new DBFieldInfoBean();
		}
		
		request.setAttribute("dbTableName", dbTableName);
		request.setAttribute("captionName", captionName);
		request.setAttribute("fieldInfoBean", fieldInfoBean);
		request.setAttribute("tableName", tableName);
		request.setAttribute("fieldName", fieldName);
		request.setAttribute("moduleId", moduleId);
		
		//SQL语句封装开始
		StringBuilder sql = new StringBuilder();//查询的sql语句
		LinkedHashMap<String,List<String[]>> msDataMap = new LinkedHashMap<String, List<String[]>>();//用于多类型查询封装的map 

		sql.append("select ");
		
		if(top!=null && !"".equals(top)){
			sql.append(" top ").append(top);
		}
		
		//处理省市,市报表的截取位置与条件
		String subEnd ="";//截取多少位数
		String districtCondition ="";//地区条件
		if("province".equals(fieldName)||"city".equals(fieldName)){
			if("province".equals(fieldName)){
				xName = "省份";
				subEnd ="10";
			}else{
				subEnd ="15";
				xName = "城市";
				districtCondition = "len(isnull(substring(CRMClientInfo.district,1,15),'')) !=10";
			}
		}
		 
		sql.append(" isnull(").append(tableName+"."+fieldName).append(",'') as ").append(fieldName);//select字段语句
		 
		if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6){
			//时间类型,统计月份
			xName = "月份";
			//iChartName ="LineBasic2D";
			chartSwfName ="Line";
			sql = new StringBuilder();
			sql.append("select isnull(substring(").append(tableName).append(".").append(fieldName).append(",1,7),'') as ").append(fieldName).append(",isnull(substring(").append(tableName).append(".").append(fieldName).append(",1,7),'') as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
		}else if(fieldInfoBean.getInputType() == 1 || "FollowStatus".equals(fieldInfoBean.getFieldName())){
			xName = "类型";
			//统计枚举类型
			sql.append(",tbllanguage.zh_CN as zh_CN,tblDBEnumerationItem.enumOrder,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join tblDBEnumerationItem  on ").append(tableName).append(".").append(fieldName).append("=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumId =(select tblDBEnumeration.id from tblDBEnumeration where enumName=(select refEnumerationName from tblDBfieldInfo where tableid=(select id from tbldbtableInfo where tablename='");
			//若是客户资料的根据模板的信息的表名
			if("CRMClientInfo".equals(tableName)){
				sql.append(moduleBean.getTableInfo().split(":")[0]);
			}else{
				sql.append(tableName);
			}
			sql.append("') and fieldName='").append(fieldName).append("')) left join tbllanguage  on tblDBEnumerationItem.languageId=tbllanguage.id ");
			//iChartName = "Column2D";
		}else if("employeeId".equals(fieldName)|| "createBy".equals(fieldName)){
			//统计人
			if("employeeId".equals(fieldName)){
				xName = "跟单人";
			}else{
				xName = "所有者";
			}
			chartSwfName ="Bar2D";
			sql.append(",tblEmployee.empfullname as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join tblemployee on ").append(tableName).append(".").append(fieldName).append("=tblemployee.id");
		}else if("goodsCode".equals(fieldName)){
			chartSwfName ="Pie3D";
			//统计商品
			xName ="商品";
			sql.append(",tblGoods.GoodsFullName+tblGoods.goodsNumber as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join tblGoods on ").append(tableName).append(".").append(fieldName).append("=tblGoods.classCode ");
		}else if("clientId".equals(fieldName) || "f_brother".equals(fieldName)){
			xName = "客户";
			String clientName = "";
			chartSwfName ="Bar2D";
			//统计客户
			sql.append(",CRMClientInfo.clientName as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join CRMClientInfo on ").append(tableName).append(".").append(fieldName).append("= CRMClientInfo.id ") ;
		}else if("Trade".equals(fieldName)){
			xName = "行业";
			chartSwfName="Bar2D";
			sql.append(",CRMWorkProfession.name as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join CRMWorkProfession on ").append(tableName).append(".").append(fieldName).append("=CRMWorkProfession.id ") ;
		}else if("province".equals(fieldName)||"city".equals(fieldName)){
			sql = new StringBuilder();
			sql.append("SELECT ");
			chartSwfName="Bar2D";
			if(top!=null && !"".equals(top)){
				sql.append(" top ").append(top);
			}
			sql.append(" isnull(substring(CRMClientInfo.district,1,").append(subEnd).append("),'') as ").append(fieldName).append(",tbldistrict.").append(fieldName).append(" as zh_CN,count(*) as counts from CRMClientInfo left join tbldistrict on CRMClientInfo.district=tbldistrict.classCode ");
		}
		
		//合同产品表进入的left join上主表
		if("CRMSaleContractDet".equals(tableName)){
			sql.append(" left join CRMSaleContract on CRMSaleContract.id=CRMSaleContractDet.f_ref");
		}
		
		//where语句
		sql.append(" where 1=1 ");
		
		
		
		//处理市报表"空"数据
		if(!"".equals(districtCondition)){
			sql.append(" and ").append(districtCondition);
		}
		
		//加上查看单据权限条件
		sql.append(this.getCheckBillScope(moduleViewBean,tableName,login,fieldInfoBean,secondFieldName,"",request)).append(" group by ");
		
		//group by语句
		if(fieldInfoBean.getFieldType() != 5 && fieldInfoBean.getFieldType() != 6 && !"province".equals(fieldName) && !"city".equals(fieldName)){
			//先判断是否日期类型或者省市,不是的进入.是的话单独处理
			sql.append(" isnull(").append(tableName+"."+fieldName).append(",'')");
		}
		
		if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6){
			sql.append(" isnull(substring(").append(tableName).append(".").append(fieldName).append(",1,7),'') ") ;
		}else if(fieldInfoBean.getInputType() == 1 || "FollowStatus".equals(fieldInfoBean.getFieldName())){
			sql.append(",tbllanguage.zh_CN,tblDBEnumerationItem.enumOrder ");
		}else if("employeeId".equals(fieldName) || "createBy".equals(fieldName)){
			sql.append(" ,tblemployee.empfullname");
		}else if("goodsCode".equals(fieldName)){
			sql.append(",tblGoods.goodsNumber,tblGoods.GoodsFullName");
		}else if("clientId".equals(fieldName) || "f_brother".equals(fieldName)){
			sql.append(",CRMClientInfo.clientName");
		}else if("Trade".equals(fieldName)){
			sql.append(",CRMWorkProfession.name");
		}else if("province".equals(fieldName)||"city".equals(fieldName)){
			//省份,城市
			sql.append(" isnull(substring(CRMClientInfo.district,1,").append(subEnd).append("),''),tbldistrict.").append(fieldName);
		}
		
		//存在多条件统计的字段名称
		if(secondFieldName !=null && !"".equals(secondFieldName)){
			DBFieldInfoBean secondfieldInfoBean = new DBFieldInfoBean();	
			if(moduleId!=null && !"".equals(moduleId)){
				secondfieldInfoBean = GlobalsTool.getField(secondFieldName, moduleBean.getTableInfo().split(":")[0], moduleBean.getTableInfo().split(":")[1]);
			}else{
				secondfieldInfoBean = GlobalsTool.getFieldBean(tableName,secondFieldName);
			}
			if(secondfieldInfoBean ==null){
				secondfieldInfoBean = new DBFieldInfoBean();
			}
			String tempTableName = tableName;//临时tableName
			if("SignUpDate".equals(secondFieldName)){
				tempTableName = "CRMSaleContract";
			}
			//iChartName ="Column3D";
			chartSwfName ="Column3D";
			String secondField ="";//secondField拼接
			if(secondfieldInfoBean.getFieldType()==5 || secondfieldInfoBean.getFieldType()==6){
				secondField = "isnull(substring("+tempTableName+"."+secondFieldName+",1,7),'') ";
			}else if("CRMSaleContractDet".equals(tableName)){
				secondField = "CRMSaleContract." + secondFieldName; 
			}else{
				secondField = tableName +"." + secondFieldName; 
			}
			
			//分割语句用于重新拼接
			StringBuilder countSql = new StringBuilder();//统计的sql语句
			String tempSql =sql.toString();//存放临时sql语句
			String fromSql = tempSql.substring(0,tempSql.indexOf("from"));//...from之前的sql语句
			String joinSql = tempSql.substring(tempSql.indexOf("from"),tempSql.indexOf("where"));//from...where语句
			String whereSql = tempSql.substring(tempSql.indexOf("where"));//where...语句
			
			countSql.append(fromSql).append(",");
			
			if(secondfieldInfoBean !=null && secondfieldInfoBean.getInputType()==1){
				countSql.append("tbllanguage.zh_CN ");
			}else if("clientId".equals(secondFieldName)){
				countSql.append("CRMClientInfo.clientName  ");
			}else{
				countSql.append(secondField).append(" ");
			}
			if(secondFieldName !=null && !"".equals(secondFieldName)){
				countSql.append(" as ").append(secondFieldName).append(" ");
			}
			countSql.append(joinSql);
			
			if(secondfieldInfoBean !=null && secondfieldInfoBean.getInputType()==1){
				xName = "类型";
				countSql.append(" left join tblDBEnumerationItem  on ").append(tableName).append(".").append(secondFieldName).append("=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumId =(select id from tblDBEnumeration where enumName=(select refEnumerationName from tblDBfieldInfo where tableid=(select id from tbldbtableInfo where tablename='");
				//若是客户资料的根据模板的信息的表名
				if("CRMClientInfo".equals(tableName)){
					countSql.append(moduleBean.getTableInfo().split(":")[0]);
				}else{
					countSql.append(tableName);
				}
				countSql.append("') and fieldName='").append(secondFieldName).append("')) left join tbllanguage  on tblDBEnumerationItem.languageId=tbllanguage.id ");
			}else if("clientId".equals(secondFieldName)){
				countSql.append(" left join CRMClientInfo on ").append(tableName).append(".").append(secondFieldName).append("= CRMClientInfo.id ") ;
			}else{
				xName = "月份";
			}

			countSql.append(whereSql).append(",");
			if(secondfieldInfoBean !=null && secondfieldInfoBean.getInputType()==1){
				countSql.append("tbllanguage.zh_CN ");
			}else if("clientId".equals(secondFieldName)){
				countSql.append("CRMClientInfo.clientName ");
			}else{
				countSql.append(secondField);
			}
			countSql.append(" order by ").append(secondFieldName);
			Result rest = mgt.queryFieldCount(countSql.toString(), fieldName, secondFieldName,sumFieldName);
			msDataMap = (LinkedHashMap<String,List<String[]>>)rest.retVal;
		}
		
		//时间类型按顺序排列
		if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6){
			sql.append(" order by isnull(substring(").append(tableName).append(".").append(fieldName).append(",1,7),'')");
		}else{
			sql.append(" order by ");
			if(fieldInfoBean.getInputType() == 1){
				sql.append(" tblDBEnumerationItem.enumOrder,counts desc");
			}else{
				sql.append(" counts desc");
			}
		}
		String chart = "";//封装chart统计图
		Result rs = mgt.queryFieldCount(sql.toString(),fieldName,null,sumFieldName);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if(secondFieldName ==null || "".equals(secondFieldName)){
				//处理单一条件
				List<String[]> dataList = (List<String[]>)rs.retVal;
				if(dataList == null || dataList.size()==0){
					request.setAttribute("count", 0);
				}else{
					chart = this.getChart(dataList,captionName, xName, yName,sumFieldName, chartSwfName,request);//获取但条件普通的统计图
					request.setAttribute("dataList", dataList);
					request.setAttribute("dataListStr", chart.toString());
				}
			}else{
				//处理多条件
				List<String[]> enumList = (List<String[]>)rs.retVal;//获得类型的总数
				if((msDataMap == null || msDataMap.size()==0)|| (enumList==null ||enumList.size()==0)){
					request.setAttribute("count", 0);
				}else{
					chart = this.getMSChart(msDataMap, enumList,request,captionName,xName,yName,secondFieldName);//获取MS类型的统计图
					request.setAttribute("enumList", enumList);
					//iChartName = "MSColumn3D";
					chartSwfName = "MSColumn3D";
					
				}
			}
			
			if(searchSwfName !=null && !"".equals(searchSwfName)){
				//iChartName = searchSwfName;
				chartSwfName = searchSwfName;
			}
			
			request.setAttribute("chart", chart);
			request.setAttribute("secondFieldName", secondFieldName);
			//request.setAttribute("iChartName", iChartName);
			request.setAttribute("chartSwfName", chartSwfName);
			request.setAttribute("xName", xName);
			request.setAttribute("yName", yName);
			request.setAttribute("sumFieldName", sumFieldName);
			return getForward(request, mapping, "charts");
		}else{
			//删除失败
			EchoMessage.error().add("查询失败").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}
	
	/**
	 * 销售跟单报表、合同/回款报表、售后服务报表公用查询模块
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private ActionForward detailList(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String moduleId = getParameter("moduleId", request);//模板ID
		String tableName = getParameter("tableName", request);//表名
		String fieldName = getParameter("fieldName", request);//统计的字段名
		String enumVal = getParameter("enumVal", request);//字段枚举值
		String condition = getParameter("condition", request) ==null ? "" : getParameter("condition", request) ;//明细条件
		
		//条件信息
		
		//点击分页不用转码
		try {
			condition =  URLDecoder.decode(condition,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//获取枚举空值查询条件
		String enumValStr = "'"+enumVal+"'";//默认查询条件
		if("".equals(enumVal) && !"province".equals(fieldName) && !"city".equals(fieldName)){
			enumValStr = this.getEmptyVal(fieldName, tableName, tableName);
		}
		
		//分页信息
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	pageSize = 30;
        }
        
        
        StringBuilder querySql = new StringBuilder();//查询语句
       
        
        //获取select语句信息
		if(moduleId == null || "".equals(moduleId)){
			 DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(tableName, fieldName);
			Result rs = new CRMBrotherSettingMgt().loadBrotherFieldDisplayBean(tableName);
			BrotherFieldDisplayBean fieldDisplayBean = (BrotherFieldDisplayBean)rs.retVal;
			
			if(fieldInfoBean==null){
				fieldInfoBean = new DBFieldInfoBean();
			}
			
			if(fieldDisplayBean == null ){
				//邻居表明细列表
				String reportTabelName="";//报表名称
				if("CRMSaleContractDet".equals(tableName)){
					reportTabelName ="CRMSaleContractDetail";
				}else{
					reportTabelName = tableName;
				}
				DefineReportBean reportBean = DefineReportBean.parse(reportTabelName+"SQL.xml", getLocale(request).toString(),getLoginBean(request).getId());//根据表名拿到报表设置信息
				querySql.append("SELECT DISTINCT id,");
				String sql = reportBean.getSql();//获取sql
				String orderName = "";//排顺序的fieldName,用于sqlListMaps查询
				String fieldSql = "";//查询的字段拼接
				sql = sql.substring(0,sql.indexOf("where 1=1")+"where 1=1".length());//截取报表设置的sql语句到where 1=1
				List<ReportField> fieldlist = reportBean.getDisFields();
				for(ReportField field :fieldlist){
					fieldSql += field.getAsFieldName() +",";
					if("".equals(orderName)){
						//根据第一个字段排顺序
						orderName = field.getAsFieldName();
					}
				}
				if(fieldSql.endsWith(",")){
					fieldSql = fieldSql.substring(0,fieldSql.length()-1);
				}
				//拼接sql语句
				querySql.append(fieldSql).append(",row_number() over(order by ").append(orderName).append(" desc) row_id from ( ").append(sql);
				if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6){
					querySql.append(" and isnull(substring(").append(tableName+"."+fieldName).append(",1,7),'')='").append(enumVal).append("'");
				}else{
					querySql.append(" and isnull(").append(tableName+"."+fieldName +",'') in (").append(enumValStr).append(")").append(condition);
				}
				querySql.append(" ) a ");
				request.setAttribute("fieldlist",fieldlist);
			}else{
				String listFields = tableName+".id,";
				for(String str : fieldDisplayBean.getListFields().split(",")){
					if("".equals(str)){
						continue;
					}
					listFields += tableName +"."+str+",";
				}
				
				querySql.append("SELECT CRMClientInfo.clientName,").append(listFields).append("row_number() over(order by ").append(tableName)
				.append(".lastUpdateTime desc) row_id FROM ").append(tableName).append(" LEFT JOIN CRMClientInfo ON ").append(tableName)
				.append(".ClientId = CRMClientInfo.id WHERE 1=1 ");
				if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6){
					querySql.append(" and isnull(substring(").append(tableName+"."+fieldName).append(",1,7),'')='").append(enumVal).append("'");
				}else{
					querySql.append(" and isnull(").append(tableName+"."+fieldName +",'') in (").append(enumValStr).append(")").append(condition);
				}
				request.setAttribute("fieldDisplayBean",fieldDisplayBean);
			}
		}else{
			//客户列表明细列表
			ClientModuleBean moduleBean = (ClientModuleBean)moduleMgt.detailCrmModule(moduleId).retVal;
			 DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(moduleBean.getTableInfo().split(":")[0], fieldName);
			if(moduleBean!=null){
				ClientModuleViewBean moduleViewBean = (ClientModuleViewBean)moduleMgt.loadModuleView("1_"+moduleId).retVal;
				request.setAttribute("moduleViewBean", moduleViewBean);
				request.setAttribute("clientTableName",moduleBean.getTableInfo().split(":")[0]);
				request.setAttribute("contactTableName",moduleBean.getTableInfo().split(":")[1]);
				
				querySql.append("SELECT id,");
				for(String strName : moduleViewBean.getListFields().split(",")){
					querySql.append(strName).append(",");
				}
				querySql.append(" row_number() over(order by  lastContractTime desc) row_id ").append(" from CRMClientInfo where 1=1 and ");
				
				
				if("province".equals(fieldName) || "city".equals(fieldName)){
					int len = 10;
					if("city".equals(fieldName)){
						len = 15;
					}
					
					if(!"".equals(enumVal)){
						//如果城市省份点击空进入详情不要这条件
						querySql.append("len(isnull(substring(CRMClientInfo.district,1,").append(len).append("),''))=").append(len).append(" and ");
					}
					querySql.append(" isnull(substring(CRMClientInfo.district,1,").append(len).append("),'') ");
				}else if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType()==6){
					querySql.append("isnull(substring(").append(fieldName).append(",1,7),'')");
				}else{
					querySql.append("isnull(").append(fieldName).append(",'')");
				}
				
				querySql.append(" in (").append(enumValStr).append(") ").append(condition);
			}
			
		}
		
		Result rs = mgt.queryDetails(querySql.toString(),pageNo,pageSize);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//成功
			request.setAttribute("rsList",rs.retVal) ;
			request.setAttribute("pageBar",pageBar2(rs, request));
			request.setAttribute("tableName",tableName);
			request.setAttribute("fieldName", fieldName);
			request.setAttribute("condition", condition);
			request.setAttribute("moduleId", moduleId);
			request.setAttribute("enumVal",enumVal);
			
		}else{
			//查询失败
			EchoMessage.error().add("查询明细失败").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		
		return getForward(request, mapping, "detail");
	}
	/**
	 * 同比反比查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward compareQuery(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String moduleId = getParameter("moduleId", request);//模板id
		String tableName = getParameter("tableName", request);//表名
		String fieldName = getParameter("fieldName", request);//字段名
		String phase = getParameter("phase", request); //历史阶段,用于同比
		String unit = getParameter("unit", request);//单位
		String sumFieldName = getParameter("sumFieldName", request);//sum(字段名)
		String isAround = getParameter("isAround", request);//true表示环比查询
		String captionName = getParameter("captionName", request);//统计图头名称
		String searchTime = getParameter("searchTime", request);//同比选择的时间
		String isSearch = getParameter("isSearch", request);// 是否查询条件进入 ,用于客户资料报表
		String secondFieldName = getParameter("secondFieldName", request);//多条件的fieldName
		String searchSwfName = getParameter("searchSwfName", request);//searchSwfName表示用户自己选择了图表.没有用默认
		String xName = getParameter("xName", request);//图形横坐标名称
		String type = getParameter("type", request);//compare表示是同比环比查询
		
		DBFieldInfoBean fieldInfoBean = new DBFieldInfoBean();
		ClientModuleBean moduleBean = new ClientModuleBean();
		ClientModuleViewBean moduleViewBean = new ClientModuleViewBean();
		String dbTableName = tableName;//查询枚举sql时需要用到的tableName
		//若存在模板id,dbTableName根据不同模板获取
		if(moduleId !=null && !"".equals(moduleId)){
			moduleBean = (ClientModuleBean)moduleMgt.detailCrmModule(moduleId).retVal;
			dbTableName =moduleBean.getTableInfo().split(":")[0];
			fieldInfoBean = GlobalsTool.getFieldBean(dbTableName, fieldName);
			moduleViewBean = (ClientModuleViewBean)moduleMgt.loadModuleView("1_"+moduleId).retVal;
			tableName="CRMClientInfo";
			request.setAttribute("moduleId", moduleId);
		}else{
			fieldInfoBean = GlobalsTool.getFieldBean(tableName, fieldName);
		}
		request.setAttribute("dbTableName", dbTableName);
		if (StringUtils.isNotBlank(captionName) && !"true".equals(isSearch)){
			captionName = GlobalsTool.toChinseChar(captionName);
    	}
		request.setAttribute("captionName", captionName);
		if(xName ==null || "".equals(xName)){
			xName ="月份";
		}
		
		if("quarter".equals(unit)){
			xName = "季度";
		}else if("month".equals(unit)){
			xName = "月份";
		}else if("week".equals(unit)){
			xName = "周期";
		}else if("day".equals(unit)){
			xName = "天数";
		}
		
		String yName = "数量";
		if(sumFieldName!=null && !"".equals(sumFieldName)){
			yName = "金额";
		}
		
		String chartSwfName = "MSColumn3D";
		if(searchSwfName !=null && !"".equals(searchSwfName) ){
			chartSwfName = searchSwfName;
		}
		
		String chart ="";//统计图信息
		
		if("true".equals(isAround)){
			//环比统计
			String startTime = getParameter("startTime", request);//开始时间
			String endTime = getParameter("endTime", request);//结束时间
			
			//默认开始时间为本年第一天
			if(startTime ==null || "".equals(startTime)){
				startTime = DateUtil.getCurrentYearFirst();
			}
			//默认结束时间为当天
			if(endTime ==null || "".equals(endTime)){
				endTime = DateUtil.getNowTime("yyyy-MM-dd");
			}
			//默认单位未月份
			if(unit ==null || "".equals(unit)){
				unit = "month";
			}
			
			String[] aroundInfo = new String[3];
			HashMap<String,String> aroundMap = new HashMap<String, String>();//环比Map,表示当前
			aroundInfo = this.getAroundInfo(tableName, fieldName, startTime, endTime, unit, sumFieldName,secondFieldName,dbTableName,fieldInfoBean,moduleViewBean,type,request);
			Result rs = mgt.queryCompareInfo(aroundInfo[1], aroundInfo[0], fieldName,secondFieldName, sumFieldName,"",unit,true);
			
			if(secondFieldName !=null && !"".equals(secondFieldName)){
				chartSwfName = "MSColumn3D";
				HashMap<String, HashMap<String,String>> mulFieldsMap = (HashMap<String, HashMap<String,String>>)rs.retVal;
				rs = mgt.queryFieldCount(aroundInfo[2], secondFieldName, null, sumFieldName);//根据枚举sql语句获得枚举信息
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					List<String[]> enumList = (List<String[]>)rs.retVal;
					if(enumList==null || enumList.size()==0){
						int count = 0;
						request.setAttribute("count", count);
					}else{
						chart = this.getAroundMsChart(mulFieldsMap, enumList, aroundInfo[0],unit, yName, captionName,request);
						request.setAttribute("mulFieldsMap", mulFieldsMap);
						request.setAttribute("enumList", enumList);
					}
					
					DBFieldInfoBean	secondFieldBean = GlobalsTool.getFieldBean(dbTableName, secondFieldName);
					request.setAttribute("secondFieldBean", secondFieldBean);
				}else{
					//查询失败
					EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
				
			}else{
				chartSwfName = "Column3D";
				double avg = 0;//求平均数
				aroundMap = (HashMap<String,String>)rs.retVal;
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					if(aroundMap.size()==0){
						request.setAttribute("count", 0);	
					}else{
						/*
						List dataList = new ArrayList();
						//String setName = "";//chart图列名称
						//chart = "<graph caption='"+captionName+"' xAxisName='"+xName+"' yAxisName='"+yName+"' baseFontSize='12' rotateYAxisName='1'> ";
						int i=1;
						String showName ="";
						for(String str : aroundInfo[0].split(",")){
							HashMap map = new HashMap();
							if("week".equals(unit)){
								showName = "第"+i+"周";
							}else{
								showName =str; 
							}
							//chart += "<set name='"+showName+"' value='"+aroundMap.get(str)+"' color='"+this.getRandomColor(i)+"'/>";
							map.put("name", showName);
							map.put("value", aroundMap.get(str));
							map.put("color", "#"+this.getRandomColor(i));
							dataList.add(map);
							i++;
							avg +=Double.parseDouble(aroundMap.get(str));
						}
						avg = avg/aroundInfo[0].split(",").length;
						//chart+= "</graph>";
						request.setAttribute("avg", avg);
						request.setAttribute("dataList", gson.toJson(dataList));
						*/
						if(aroundMap.size()==0){
							request.setAttribute("count", 0);	
						}else{
							String setName = "";//chart图列名称
							chart = "<graph caption='"+captionName+"' xAxisName='"+xName+"' yAxisName='"+yName+"' baseFontSize='12' rotateYAxisName='1' formatNumberScale='0'";
							if(sumFieldName ==null || "".equals(sumFieldName)){
								chart += " decimalPrecision='0' ";  
							}
							chart +=" >";
							int i=1;
							String showName ="";
							for(String str : aroundInfo[0].split(",")){
								if("week".equals(unit)){
									showName = "第"+i+"周";
								}else{
									showName =str; 
								}
								chart += "<set name='"+showName+"' value='"+aroundMap.get(str)+"' color='"+this.getRandomColor(i)+"'/>";
								i++;
								avg +=Double.parseDouble(aroundMap.get(str));
							}
							avg = avg/aroundInfo[0].split(",").length;
							chart+= "</graph>";
							request.setAttribute("avg", avg);
						}
					}
				}else{
					//查询失败
					EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}
			
			request.setAttribute("timeScope", aroundInfo[0]);
			request.setAttribute("aroundMap", aroundMap);
			request.setAttribute("unit", unit);
			request.setAttribute("isAround", isAround);
			request.setAttribute("startTime", startTime);
			request.setAttribute("endTime", endTime);
			request.setAttribute("secondFieldName", secondFieldName);
		}else{
			//同比统计
			String[] nowCompareInfo = new String[2];
			String[] prevCompareInfo = new String[2];
			Calendar prevDate = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(searchTime ==null || "".equals(searchTime)){
				searchTime = sdf.format(prevDate.getTime());
			}
			try {
				prevDate.setTime(sdf.parse(searchTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//默认同比阶段是年
			if(phase ==null || "".equals(phase)){
				phase = "year";
			}
			
			//默认同比单位是月
			if(unit ==null || "".equals(unit)){
				unit = "month";
			}
			
			
			//获取同比上一期的日期
			if("year".equals(phase)){
				prevDate.add(Calendar.YEAR, -1);
			}else if("quarter".equals(phase)){
				prevDate.add(Calendar.MONTH, -3);
			}else if("month".equals(phase)){
				if("day".equals(unit)){
					request.setAttribute("phaseMonth", prevDate.get(Calendar.MONTH)+1);
				}
				prevDate.add(Calendar.MONTH, -1);
			}else{
				prevDate.add(Calendar.DATE, -7);
			}
			
			
			HashMap<String,String> nowInfoMap = new HashMap<String, String>();//同比Map,表示当前
			HashMap<String,String> prevInfoMap = new HashMap<String, String>();//同比Map,表示上次
			
			nowCompareInfo = this.getCompareInfo(tableName, fieldName, phase, unit, searchTime, sumFieldName,fieldInfoBean,moduleViewBean,type,request);
			Result rs = mgt.queryCompareInfo(nowCompareInfo[1], nowCompareInfo[0], fieldName,null,sumFieldName,phase,unit,false);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				nowInfoMap = (HashMap<String,String>)rs.retVal;
				prevCompareInfo = this.getCompareInfo(tableName, fieldName, phase, unit, sdf.format(prevDate.getTime()), sumFieldName,fieldInfoBean,moduleViewBean,type,request);
				
				rs = mgt.queryCompareInfo(prevCompareInfo[1], prevCompareInfo[0], fieldName,null, sumFieldName,phase,unit,false);
				prevInfoMap = (HashMap<String,String>)rs.retVal;
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					if(prevInfoMap.size()==0 && nowInfoMap.size()==0){
						request.setAttribute("count", "0");
					}else{
						//如果阶段:季度 单位:月 与 阶段是:周 单位：天 取日期时用map处理
						HashMap<String,String> prevScope = new HashMap<String,String>();
						if(("quarter".equals(phase) && "month".equals(unit)) || ("week".equals(phase) && "day".equals(unit))){
							for(int i=0;i<prevCompareInfo[0].split(",").length;i++){
								prevScope.put(i+1+"", prevCompareInfo[0].split(",")[i]);
							}
							request.setAttribute("prevScope", prevScope);
						}
						String timeScope = nowCompareInfo[0].split(",").length >= prevCompareInfo[0].split(",").length ?nowCompareInfo[0]:prevCompareInfo[0];
						request.setAttribute("timeScope",timeScope);
						request.setAttribute("prevTimeScope", prevCompareInfo[0]);
						chart = this.getCompareChart(prevInfoMap, nowInfoMap, timeScope,prevScope, xName, yName, captionName,phase,unit,request);
						request.setAttribute("nowInfoMap", nowInfoMap);
						request.setAttribute("prevInfoMap", prevInfoMap);
					}
				}else{
					//查询失败
					EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}else{
				//查询失败
				EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
		}
		request.setAttribute("unit", unit);
		request.setAttribute("chart", chart);
		request.setAttribute("tableName", tableName);
		request.setAttribute("fieldName", fieldName);
		request.setAttribute("sumFieldName", sumFieldName);
		request.setAttribute("chartSwfName", chartSwfName);
		request.setAttribute("phase", phase);
		request.setAttribute("searchTime", searchTime);
		return getForward(request, mapping, "compareCharts");
	}
	
	
	/**
	 * 通用单据权限与客户条件查询
	 * @param moduleViewBean
	 * @param login
	 * @param request
	 * @return
	 */
	private String getCheckBillScope(ClientModuleViewBean moduleViewBean,String tableName,LoginBean login,DBFieldInfoBean fieldInfoBean,String secondFieldName,String type,HttpServletRequest request){
		HashMap<String, String> paramMap = new HashMap<String, String>();//存放参数的MAP
		String userId = login.getId();
		StringBuilder condition = new StringBuilder();
		String tempTableName=tableName;
		if("CRMSaleContractDet".equals(tableName)){
			tempTableName = "CRMSaleContract";
		}
		String dbTableName = request.getParameter("dbTableName");//查询枚举表中的表名
		String emptyIds = "";//存放枚举类型为空的值
		String selectedEnumIds = "";//存放页面选择的枚举ID
		String scopeEmployeeName = "employeeId";//权限默认查询人员fieldName
		String scopeDeptCode = "";//除了客户资料保存部门权限字符串
		if(!"1".equals(userId)){
			/*查看某字段值单据*/
			String fieldValueSQL = "" ;
			if(tableName.indexOf("CRMClientInfo") != -1){
				scopeEmployeeName = "createBy";
			}
			
			condition.append(" and (").append(tempTableName).append(".").append(scopeEmployeeName).append(" ='").append(userId);
			
			if(tableName.indexOf("CRMClientInfo") != -1){
				condition.append("' or CRMClientInfo.id in(select f_ref from CRMClientInfoEmp where employeeId ='").append(userId);
			}
			
			condition.append("' ") ;
			if(tableName.indexOf("CRMClientInfo") != -1){
				condition.append(")");
			}
			
			//获取权限路径
			String mopUrl = "/UserFunctionQueryAction.do?";
			if(!"CRMClientInfo".equals(tempTableName)){
				mopUrl +="parentTableName=CRMClientInfo&";
			}
			mopUrl +="tableName="+tempTableName;
			
			MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get(mopUrl);
			if(mop!=null){
				ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
				if (scopeRight != null && scopeRight.size()>0) {
					for (Object o : scopeRight) {
						String strUserIds = "" ;
						String strDeptIds = "" ;
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strUserIds += "'"+strId+"'," ;
							}
							strUserIds = strUserIds.substring(0, strUserIds.length()-1) ;
							condition.append(" or ").append(tempTableName).append(".").append(scopeEmployeeName).append(" in (").append(strUserIds).append(")");
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								if(tempTableName.indexOf("CRMClientInfo") > -1){
									//若是客户资料的直接用departmentCode like
									condition.append(" or ").append(tempTableName).append(".departmentCode like '").append(strId).append("%' ") ;
								}else{
									//把部门所有存放起来
									scopeDeptCode += "classcode like '" + strId + "%' or ";
								}
							}
						}
						
						if(lsb!=null && "6".equals(lsb.getFlag()) && tempTableName.equals(lsb.getTableName())){
							if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
								if(lsb.getScopeValue().contains(";")){
									String[] scopes = lsb.getScopeValue().split(";") ;
									String scopeSQL = "(" ;
									for(String str : scopes){
										scopeSQL += "'" + str + "'," ; 
									}
									scopeSQL = scopeSQL.substring(0, scopeSQL.length()-1) ;
									scopeSQL += ")" ;
									fieldValueSQL = lsb.getTableName() + "." + lsb.getFieldName() + " in " +scopeSQL ;
								}
							}
						}
					}
					
					//处理除了客户资料,其他表单的部门权限
					if(tempTableName.indexOf("CRMClientInfo") == -1){
						if(scopeDeptCode.endsWith("or ")){
							scopeDeptCode = scopeDeptCode.substring(0,scopeDeptCode.length()-3);
						}
						if(!"".equals(scopeDeptCode)){
							condition.append(" or ((select DepartmentCode from tblEmployee where tblEmployee.id=").append(tempTableName)
							.append(".employeeId) in (select classcode from tblDepartment where ").append(scopeDeptCode).append(" ))");
						}
					}

					
				}
			}
			condition.append(")") ;
			if(fieldValueSQL.length()>0){
				condition.append(" and (").append(fieldValueSQL).append(")") ;
			}
		}

		String removeSearch = getParameter("removeSearch", request);//解除搜索标识
		//true表示解除搜索
		if(!"true".equals(removeSearch)){
			if(!"weekMonthDetail".equals(type)){
				//客户资料报表查询条件
				if(tableName.indexOf("CRMClientInfo") != -1){
					condition.append(" and CRMClientInfo.moduleId='").append(moduleViewBean.getModuleId()).append("' ") ;
					if("public".equals(paramMap.get("isPublic"))){
						condition.append(" and status = '1' ") ;
					}else{
						condition.append(" and status != '1' ") ;
					}
					
					//模糊查询
					for(String str : moduleViewBean.getKeyFields().split(",")){
						String fieldValue = getParameter(str, request);
						if(fieldValue != null && !"".equals(fieldValue)){
							condition.append(" and " + str +" like '%" + fieldValue +"%' ");
							paramMap.put(str, fieldValue);
						}
					}
					
					//条件查询
					for(String str : moduleViewBean.getSearchFields().split(",")){
						String[] fieldValue = getParameters(str, request);
						String enumIds = "";
						selectedEnumIds = "";
						if(fieldValue != null && !"".equals(fieldValue)){
							if("LastContractTime".equals(str)){
								Calendar calendar = Calendar.getInstance() ;
								calendar.add(Calendar.DAY_OF_YEAR, -Integer.parseInt(fieldValue[0].replace("more", "").replace(",", ""))+1) ;
								condition.append(" and ").append(str).append(" < ").append(" '"+BaseDateFormat.format(calendar.getTime(), BaseDateFormat.yyyyMMddHHmmss)+"' ");
								paramMap.put(str, fieldValue[0]);
							}else{
								for(String enumVal : fieldValue){
									if("empty".equals(enumVal)){
										//若值为empty要查找其余有枚举值但无多语言的选项值
										emptyIds = this.getEmptyVal(str, tableName, dbTableName);
									}else{
										enumIds += "'"+enumVal+"',";
									}
									selectedEnumIds +=enumVal+",";//返回已选的id
								}
								enumIds +=emptyIds;
								if(enumIds.endsWith(",")){
									enumIds = enumIds.substring(0,enumIds.length()-1);
								}
								condition.append(" and isnull(" + str +",'') in (" + enumIds +") ");
								paramMap.put(str,selectedEnumIds);
							}
						}
					}
					String createTimeStart = getParameter("createTimeStart", request) ;
					String createTimeEnd = getParameter("createTimeEnd", request) ;
					String lastUpdateTimeStart = getParameter("lastUpdateTimeStart", request) ;
					String lastUpdateTimeEnd = getParameter("lastUpdateTimeEnd", request) ;
					
					if(!"compare".equals(type)){
						//默认第一次进来若按时间分类查本年的数据
						if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6 || "createTime".equals(secondFieldName)){
							if(createTimeStart == null){
								createTimeStart = DateUtil.getCurrentYearFirst();//默认获得本年第一天
							}
							if(createTimeEnd == null){
								createTimeEnd = DateUtil.getNowTime("yyyy-MM-dd");//当天时间;
							}
						}
					}
					
					if(createTimeStart != null && !"".equals(createTimeStart)){
						condition.append(" and CRMClientInfo.createTime >= '").append(createTimeStart).append("'");
						paramMap.put("createTimeStart", createTimeStart);
					}
					if(createTimeEnd != null && !"".equals(createTimeEnd)){
						condition.append(" and CRMClientInfo.createTime <= '").append(createTimeEnd +" 23:59:999").append("'");
						paramMap.put("createTimeEnd", createTimeEnd);
					}
					
					if(lastUpdateTimeStart != null && !"".equals(lastUpdateTimeStart)){
						condition.append(" and CRMClientInfo.lastUpdateTime >= '").append(lastUpdateTimeStart).append("'");
						paramMap.put("lastUpdateTimeStart", lastUpdateTimeStart);
					}
					if(lastUpdateTimeEnd != null && !"".equals(lastUpdateTimeEnd)){
						condition.append(" and CRMClientInfo.lastUpdateTime <= '").append(lastUpdateTimeEnd +" 23:59:999").append("'");
						paramMap.put("lastUpdateTimeEnd", lastUpdateTimeEnd);
					}
					
					//地区查询
					String district = getParameter("district", request) ;
					if(district != null && !"".equals(district) && !",".equals(district)){
						condition.append(" and");
						for(int i=0;i<district.split(",").length;i++){
							if(!"".equals(district.split(",")[i])){
								condition.append(" CRMClientInfo.district like '").append(district.split(",")[i]).append("%' or"); 
							}
						}
						if(condition.toString().endsWith(" or")){
							condition.delete(condition.length()-3,condition.length()) ;
						}
						paramMap.put("district", district);
					}
					
					//行业查询
					String trade = getParameter("trade", request) ;
					if(trade != null && !"".equals(trade) && !",".equals(trade)) {
						String str ="";
						for(int i=0;i<trade.split(",").length;i++){
							if(!"".equals(trade.split(",")[i])){
								str += "'" + trade.split(",")[i] +"',";
							}
						}
						if(str.endsWith(",")){
							str = str.substring(0,str.length()-1);
						}
						condition.append(" and CRMClientInfo.trade in (").append(str).append(")");
						paramMap.put("trade", trade);
					}
					if(paramMap.size()>0){
						request.setAttribute("hasCondition", "true");//表示有查询条件
					}
				}else{
					String searchTimeName = getParameter("searchTimeName", request) ;//搜索时间的fieldName
					String searchTimeStart = getParameter("searchTimeStart", request) ;//搜索开始时间
					String searchTimeEnd = getParameter("searchTimeEnd", request) ;//搜索结束时间
					
					//枚举值
					DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName);
					for(DBFieldInfoBean fieldBean : tableInfo.getFieldInfos()){
						if(fieldBean.getInputType() == 1){
							selectedEnumIds = "";
							String enumIds = "";
							String[] enumVal = request.getParameterValues(fieldBean.getFieldName());
							if(enumVal!=null && enumVal.length>0){
								for(String value : enumVal){
									if("empty".equals(value)){
										//若值为empty要查找其余有枚举值但无多语言的选项值
										emptyIds = this.getEmptyVal(fieldBean.getFieldName(), tableName, dbTableName);
									}else{
										enumIds += "'"+value+"',";
									}
									selectedEnumIds += value+",";//返回已选的ID
								}
								enumIds += emptyIds;
								if(enumIds.endsWith(",")){
									enumIds = enumIds.substring(0,enumIds.length()-1);
								}
								condition.append(" and isnull(").append(tableName).append(".").append(fieldBean.getFieldName()).append(",'') in (")
								.append(enumIds).append(") "); 
								
								request.setAttribute("hasCondition", "true");//表示有查询条件
							}
							paramMap.put(fieldBean.getFieldName(),selectedEnumIds);
						}
					}
					
					if(!"compare".equals(type)){
						//默认第一次进来若按时间分类查本年的数据
						if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6  || "SignUpDate".equals(secondFieldName) || "BillDate".equals(secondFieldName) || "createTime".equals(secondFieldName)){
							if(searchTimeStart == null){
								searchTimeStart = DateUtil.getCurrentYearFirst();//默认获得本年第一天
							}
							if(searchTimeEnd == null){
								searchTimeEnd = DateUtil.getNowTime("yyyy-MM-dd");//当天时间;
							}
						}
					}
					
					if(searchTimeStart != null && !"".equals(searchTimeStart)){
						condition.append(" and ").append(tempTableName).append(".");
						if("SignUpDate".equals(secondFieldName) || "BillDate".equals(secondFieldName) || "createTime".equals(secondFieldName)){
							condition.append(secondFieldName);
						}else{
							if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6){
								condition.append(fieldInfoBean.getFieldName());
							}else{
								if("CRMSaleContract".equals(tableName) && "clientId".equals(fieldInfoBean.getFieldName())){
									condition.append("SignUpDate");
								}else if("f_brother".equals(fieldInfoBean.getFieldName())){
									condition.append("BillDate");
								}else{
									condition.append(searchTimeName);
								}
							}
						}
						condition.append(" >= '").append(searchTimeStart).append("'");
						request.setAttribute("hasCondition", "true");//表示有查询条件
					}
					if(searchTimeEnd != null && !"".equals(searchTimeEnd)){
						condition.append(" and ").append(tempTableName).append(".");
						if("SignUpDate".equals(secondFieldName) || "BillDate".equals(secondFieldName) || "createTime".equals(secondFieldName)){
							condition.append(secondFieldName);
						}else{
							if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6){
								condition.append(fieldInfoBean.getFieldName());
							}else{
								condition.append(searchTimeName);
							}
						}
						condition.append(" <= '").append(searchTimeEnd).append(" 23:59:999'");
						request.setAttribute("hasCondition", "true");//表示有查询条件
					}
					paramMap.put("searchTimeStart",searchTimeStart);
					paramMap.put("searchTimeEnd",searchTimeEnd);
					request.setAttribute("searchTimeStart", searchTimeStart);
					request.setAttribute("searchTimeEnd", searchTimeEnd);
					
				}
			}
			//查找人员
			String userGroupIds = getParameter("userGroupIds", request);
			if(userGroupIds != null && !"".equals(userGroupIds) && !",".equals(userGroupIds)){
				String selectName = tempTableName +".employeeId";
				if("CRMClientInfo".equals(tableName)){
					selectName = "CRMClientInfo.createBy";
				}
				String ids = "";
				for(String str :userGroupIds.split(",")){
					ids += "'" + str +"',";
				}
				if(ids.endsWith(",")){
					ids = ids.substring(0,ids.length()-1);
				}
				condition.append(" and ").append(selectName).append(" in(").append(ids).append(") ");
				request.setAttribute("userGroupIds", userGroupIds);
				request.setAttribute("hasCondition", "true");//表示有查询条件
			}
			
			//部门查询
			String deptGroupIds = getParameter("deptGroupIds", request);
			if(deptGroupIds != null && !"".equals(deptGroupIds)){
				String selectName = tempTableName +".employeeId";
				if("CRMClientInfo".equals(tableName)){
					selectName = "CRMClientInfo.createBy";
				}
				String deptIds = "";
				for(String str :deptGroupIds.split(",")){
					deptIds += " departmentCode like '"+str+"%' or ";
				}
				if(deptIds.endsWith("or ")){
					deptIds = deptIds.substring(0,deptIds.length()-3);
				}
				if(!"".equals(deptIds)){
					condition.append(" and ").append(selectName).append(" in (").append("SELECT id FROM tblEmployee where ").append(deptIds).append(")");
				}
				request.setAttribute("deptGroupIds", deptGroupIds);
				request.setAttribute("hasCondition", "true");//表示有查询条件
			}
			
			//客户查询
			String clientIds = getParameter("CrmClickGroupIds", request);
			if(clientIds != null && !"".equals(clientIds)){
				
				String selectName = tempTableName;
				if("CRMClientInfo".equals(tableName)){
					selectName +=".id";
				}else{
					selectName +=".clientId";
				}
				String ids = "";
				for(String str :clientIds.split(",")){
					ids += "'" + str +"',";
				}
				if(ids.endsWith(",")){
					ids = ids.substring(0,ids.length()-1);
				}
				condition.append(" and ").append(selectName).append(" in(").append(ids).append(") ");
				
				
//				//把名称放入map中,KEY是clientId
//				if(clientNames!=null && !"".equals(clientNames)){
//					HashMap<String, String> clientMap = new HashMap<String, String>();
//					for(String client :clientNames.split(";")){
//						if(client !=null && !"".equals(client)){
//							clientMap.put(client.split(":")[0], client.split(":")[1]);
//						}
//					}
//					request.setAttribute("clientMap", clientMap);
//					request.setAttribute("clientNames", clientNames);
//				}
				Result rs = clientMgt.findClients(ids);//根据ID查找客户信息
				request.setAttribute("clientInfo", rs.retVal);
				request.setAttribute("clientIds", clientIds);
				request.setAttribute("hasCondition", "true");//表示有查询条件
			}
			
			//处理环比有枚举条件查询
			if("compare".equals(type) && secondFieldName !=null && !"".equals(secondFieldName)){
				String[] classes = request.getParameterValues(secondFieldName+"_isAround");
				if(classes !=null && classes.length>0){
					selectedEnumIds ="";
					String classIds = "";
					String ids = "";
					for(String str : classes){
						if("empty".equals(str)){
							//若值为empty要查找其余有枚举值但无多语言的选项值
							emptyIds = this.getEmptyVal(secondFieldName, tableName, dbTableName);
						}else{
							classIds += "'" + str +"',";
						}
						selectedEnumIds += str+",";//返回已选的id
					}
					classIds += emptyIds;
					if(classIds.endsWith(",")){
						classIds = classIds.substring(0,classIds.length()-1);
					}
					condition.append(" and isnull(").append(secondFieldName).append(",'') in (").append(classIds).append(") "); 
					request.setAttribute("classes",selectedEnumIds);
				}
			}
			request.setAttribute("paramMap", paramMap);//参数MAP
			request.setAttribute("condition", condition);//条件
		}else{
			//客户资料报表查询条件
			if(tableName.indexOf("CRMClientInfo") != -1){
				//周月统计报表不需要查找模板
				if(!"weekMonthDetail".equals(type)){
					condition.append(" and CRMClientInfo.moduleId='").append(moduleViewBean.getModuleId()).append("' ") ;
				}
				if("public".equals(paramMap.get("isPublic"))){
					condition.append(" and status = '1' ") ;
				}else{
					condition.append(" and status != '1' ") ;
				}
			}
			request.setAttribute("condition", condition);//条件
		}
		return condition.toString();
	}
	
	/**
	 * 获得统计图
	 * @param dataList
	 * @param captionName
	 * @param xName
	 * @param yAxisName
	 * @param isCount
	 * @return
	 */
	public String getChart(List<String[]> dataList,String captionName,String xName,String yAxisName,String sumFieldName,String ichartName,HttpServletRequest request){
		/*
		//封装chart格式
		String setName = "";//chart图列名称
		//String chart = "<graph caption='"+captionName+"' xAxisName='"+xName+"' yAxisName='"+yAxisName+"' baseFontSize='12' rotateYAxisName='1' decimals='0' > ";
		String name = "";
		List list = new ArrayList();
		List labelList = new ArrayList();
		for(int i=0;i<dataList.size();i++){
			name = dataList.get(i)[1];
			if(name == null || "".equals(name)){
				name="空";
			}
			if("LineBasic2D".equals(ichartName)){
				list.add(GlobalsTool.dealDoubleDigits(dataList.get(i)[2], sumFieldName));
				labelList.add(name);
			}else{
				HashMap map = new HashMap();
//			if(dataList.get(i)[1] ==null || "".equals(dataList.get(i)[1])){
//				setName = "空";
//			}else{
//				setName = dataList.get(i)[1];
//			}
//			chart += "<set name='"+setName+"' value='"+dataList.get(i)[2]+"' color='"+this.getRandomColor(i)+"' />";
				map.put("name", name);
				map.put("value",GlobalsTool.dealDoubleDigits(dataList.get(i)[2], sumFieldName));
				map.put("color","#"+this.getRandomColor(i));
				list.add(map);
			}
		}
		if("LineBasic2D".equals(ichartName)){
			HashMap map = new HashMap();
			List lineList = new ArrayList();
			map.put("name","");
			map.put("value", list);
			map.put("color", "#1f7e92");
			map.put("line_width", 3);
			lineList.add(map);
			request.setAttribute("labelList",gson.toJson(labelList));
			return gson.toJson(lineList);
		}else{
			return gson.toJson(list);
		}
		
//		chart+= "</graph>";
		* */
		
		
//		封装chart格式
		String setName = "";//chart图列名称
		String chart = "<graph caption='"+captionName+"' xAxisName='"+xName+"' yAxisName='"+yAxisName+"' baseFontSize='12'  decimals='0' formatNumberScale='0' ";
		if(sumFieldName ==null || "".equals(sumFieldName)){
			chart += " decimalPrecision='0' ";  
		}
		chart +=" >";
		for(int i=0;i<dataList.size();i++){
			if(dataList.get(i)[1] ==null || "".equals(dataList.get(i)[1])){
				setName = "空";
			}else{
				setName = dataList.get(i)[1];
			}
			chart += "<set name='"+setName+"' value='"+GlobalsTool.dealDoubleDigits(dataList.get(i)[2], sumFieldName)+"' color='"+this.getRandomColor(i)+"' />";
		}
		chart+= "</graph>";
		
		return chart;
	}
	
	/**
	 * 获取ms图
	 * @param msDataMap 数据
	 * @param enumList 枚举list
	 * @param request
	 * @param captionName caption名字
	 * @param xName 横坐标
	 * @param yName 纵坐标
	 * @param secondFieldName 第二个字段名称
	 * @return
	 */
	public String getMSChart(LinkedHashMap<String,List<String[]>> msDataMap,List<String[]> headList,HttpServletRequest request,String captionName,String xName,String yName,String secondFieldName){
		/*List<List<String>> msDatas = new ArrayList<List<String>>();//页面显示每行数据的集合
		HashMap<String,String> enumMap = new HashMap<String, String>();//页面显示总类的MAP
		String sumFieldName = getParameter("sumFieldName",request);
		String showName = "";
		String chart = "<graph xaxisname='"+xName+"' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12'>";
		//头部
		chart +="<categories font='Arial' fontSize='12' fontColor='000000' >";
		
		
		
		
		List labelList = new ArrayList();
		
		//统计图类型循环
		for(int i=0;i<headList.size();i++){
			enumMap.put(headList.get(i)[1], headList.get(i)[2]);			
		}
		
		Set keys = msDataMap.keySet();
		if(keys != null) {
			Iterator iterator = keys.iterator();
			while(iterator.hasNext()) {
				Object key = iterator.next();//拿KEY
				String categoryName = (String)key;
				if(categoryName == null || "".equals(categoryName)){
					categoryName = "空";
				}
				chart +="<category name='"+categoryName+"' fontSize='12' />";
				labelList.add(categoryName);
			}
			chart += "</categories>";
			
			
			iterator = keys.iterator();
			while(iterator.hasNext()) {
				double[] perEmpArr = new double[headList.size()];//每行的数据
				double count = 0.0;//统计总数
				List<String> datasList = new ArrayList<String>();//存放页面显示每行的数据
				Object key = iterator.next();//拿KEY
				List<String[]> perList = msDataMap.get(key);//根据KEY获取分类数据
				showName =(String)key;
				showName = showName == null ? "空" : showName;
				datasList.add(showName);
				
				//循环每个类型数据与类型LIST比,有的放入perEmpArr中
				for(String[] strArr : perList){
					for(int i=0;i<headList.size();i++){
						if(headList.get(i)[0].equals(strArr[1])){
							perEmpArr[i] = Double.parseDouble(strArr[2]);
							break;
						}
					}
				}
				
				//计算每行总和
				for(int i=0;i<perEmpArr.length;i++){
					count +=perEmpArr[i];
					datasList.add(perEmpArr[i] +"");
				}
				datasList.add(count+"");
				msDatas.add(datasList);
				
			}
			
			//封装dataset显示部分
			int i=1;//其实位置,0是secondFieldName
			String headName = "";
			
			
			
			List list = new ArrayList();
			for(String[] headArr : headList){
				HashMap map = new HashMap();
				List tempList = new ArrayList();
				if(headArr[1] ==null || "".equals(headArr[1])){
					headName = "空";
				}else{
					headName = headArr[1];
				}
				chart +="<dataset seriesname='"+headName+"' color='"+this.getRandomColor(i)+"'>";
				map.put("name",headName);
				map.put("color", "#"+this.getRandomColor(i));
				//循环msDatas页面显示的每一行
				for(List<String> perList : msDatas){
					//从get(1)开始取相应数据
					chart +="<set value='"+perList.get(i)+"' />";	
					tempList.add(GlobalsTool.dealDoubleDigits(perList.get(i), sumFieldName));
				}
				chart +="</dataset>";
				i++;
				map.put("value", tempList);
				list.add(map);
			}
			request.setAttribute("dataList", gson.toJson(list));
			request.setAttribute("labelList", gson.toJson(labelList));
		}
		
		request.setAttribute("msDatas", msDatas);
		request.setAttribute("enumMap", enumMap);
		chart +="</graph>";
		return chart;
		*/
		
		String sumFieldName = getParameter("sumFieldName",request);
		List<List<String>> msDatas = new ArrayList<List<String>>();//页面显示每行数据的集合
		HashMap<String,String> enumMap = new HashMap<String, String>();//页面显示总类的MAP
		String showName = "";
		String chart = "<graph xaxisname='"+xName+"' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12' formatNumberScale='0' >";
		//头部
		chart +="<categories font='Arial' fontSize='12' fontColor='000000' >";
		//统计图类型循环
		for(int i=0;i<headList.size();i++){
			enumMap.put(headList.get(i)[1], headList.get(i)[2]);			
		}
		
		Set keys = msDataMap.keySet();
		if(keys != null) {
			Iterator iterator = keys.iterator();
			while(iterator.hasNext()) {
				Object key = iterator.next();//拿KEY
				String categoryName = (String)key;
				if(categoryName == null || "".equals(categoryName)){
					categoryName = "空";
				}
				chart +="<category name='"+categoryName+"' fontSize='12' />";
			}
			chart += "</categories>";
			
			
			iterator = keys.iterator();
			while(iterator.hasNext()) {
				double[] perEmpArr = new double[headList.size()];//每行的数据
				double count = 0.0;//统计总数
				List<String> datasList = new ArrayList<String>();//存放页面显示每行的数据
				Object key = iterator.next();//拿KEY
				List<String[]> perList = msDataMap.get(key);//根据KEY获取分类数据
				showName =(String)key;
				if(showName == null || "".equals(showName) || "null".equals(showName)){
					showName = "空";
				}
				//showName = showName == null ? "空" : showName;
				datasList.add(showName);
				
				//循环每个类型数据与类型LIST比,有的放入perEmpArr中
				for(String[] strArr : perList){
					for(int i=0;i<headList.size();i++){
						if(headList.get(i)[0].equals(strArr[1])){
							perEmpArr[i] = Double.parseDouble(strArr[2]);
							break;
						}
					}
				}
				
				//计算每行总和
				for(int i=0;i<perEmpArr.length;i++){
					count +=perEmpArr[i];
					datasList.add(perEmpArr[i] +"");
				}
				datasList.add(count+"");
				msDatas.add(datasList);
				
			}
			
			//封装dataset显示部分
			int i=1;//其实位置,0是secondFieldName
			String headName = "";
			for(String[] headArr : headList){
				if(headArr[1] ==null || "".equals(headArr[1])){
					headName = "空";
				}else{
					headName = headArr[1];
				}
				chart +="<dataset seriesname='"+headName+"' color='"+this.getRandomColor(i)+"'>";
				
				//循环msDatas页面显示的每一行
				for(List<String> perList : msDatas){
					//从get(1)开始取相应数据
					chart +="<set value='"+perList.get(i)+"' />";	
				}
				chart +="</dataset>";
				i++;
			}
		}
		
		request.setAttribute("msDatas", msDatas);
		request.setAttribute("enumMap", enumMap);
		chart +="</graph>";
		return chart;
	}
	
	/**
	 * 获得同比统计图
	 * @param prevMap 去年数据
	 * @param nowMap 今年数据
	 * @param timeScope 时间段
	 * @param xName 横坐标名称
	 * @param yName 纵坐标名称
	 * @param phase 同比阶段
	 * @param unit 同比单位
	 * @param captionName 头名称
	 * @return
	 */
	public String getCompareChart(HashMap<String,String> prevMap,HashMap<String,String> nowMap,String timeScope,HashMap<String,String> prevScope,String xName,String yName,String captionName,String phase,String unit,HttpServletRequest request){
		/*
		List labelList = new  ArrayList();
		List dataList = new ArrayList();
		List prevList = new ArrayList();
		List nowList = new ArrayList();
		
		String chart = "<graph xaxisname='"+xName+"' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12'>";
		chart += "<categories font='Arial' fontSize='12' fontColor='000000'>";
		
		String prevSeriesname = "去年";
		String seriesname = "今年";
		if("quarter".equals(phase)){
			prevSeriesname = "上季度";
			seriesname = "本季度";
		}else if("month".equals(phase)){
			prevSeriesname = "上月";
			seriesname = "本月";
		}else if("week".equals(phase)){
			prevSeriesname = "上周";
			seriesname = "本周";
		}
		
		String prevDataSet = "<dataset seriesname='"+prevSeriesname+"' color='"+this.getRandomColor(0)+"'>";
		String nowDataSet = "<dataset seriesname='"+seriesname+"' color='"+this.getRandomColor(1)+"'>";
		
		String prevVal = "";//存放上一期显示数据
		String nowVal="";//存放本期显示数据
		int i =1;//用于累加
		String tempCount = "";
		for(String str : timeScope.split(",")){
			tempCount = i+"";
			if("quarter".equals(unit)){
				chart += "<category name='第"+i+"季' /> ";
				labelList.add("第"+i+"季");
			}else if("month".equals(unit)){
				if("quarter".equals(phase)){
					chart += "<category name='"+prevScope.get(tempCount)+"/"+str+"月' /> ";
					labelList.add(prevScope.get(tempCount)+"/"+str+"月");
				}else{
					chart += "<category name='"+str+"月' /> ";
					labelList.add(str+"月");
				}
			}else if("week".equals(unit)){
				chart += "<category name='第"+i+"周' /> ";
				labelList.add("第"+i+"周");
			}else{
				if("week".equals(phase)){
					chart += "<category name='"+prevScope.get(tempCount)+"/"+str+"' /> ";
					labelList.add(prevScope.get(tempCount)+"/"+str);
				}else{
					chart += "<category name='"+str+"' /> ";
					labelList.add(str);
				}
			}
			
			if(("quarter".equals(phase) && "month".equals(unit)) || ("week".equals(phase) && "day".equals(unit))){
				prevVal = prevMap.get(prevScope.get(tempCount)) == null ? "0" : prevMap.get(prevScope.get(tempCount));
			}else{
				prevVal = prevMap.get(str) == null ? "0" : prevMap.get(str);
			}
			nowVal = nowMap.get(str) == null ? "0" : nowMap.get(str);
			prevDataSet += "<set value='"+prevVal+"' />";
			nowDataSet += "<set value='"+nowVal+"' />";
			prevList.add(prevVal);
			nowList.add(nowVal);
			i++;
		}
		chart += "</categories>";
		chart += prevDataSet + "</dataset>";
		chart += nowDataSet + "</dataset>";
		chart += "</graph>";
		
		
		HashMap prevDataMap = new HashMap();
		HashMap nowDataMap = new HashMap();
		
		prevDataMap.put("name",prevSeriesname);
		prevDataMap.put("value",prevList);
		prevDataMap.put("color","#"+this.getRandomColor(0));
		
		nowDataMap.put("name",seriesname);
		nowDataMap.put("value",nowList);
		nowDataMap.put("color","#"+this.getRandomColor(1));
		
		dataList.add(prevDataMap);
		dataList.add(nowDataMap);
		
		request.setAttribute("labelList",gson.toJson(labelList));
		return gson.toJson(dataList);
		*/
		String chart = "<graph xaxisname='"+xName+"' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12' formatNumberScale='0'>";
		chart += "<categories font='Arial' fontSize='12' fontColor='000000'>";
		
		String prevSeriesname = "去年";
		String seriesname = "今年";
		if("quarter".equals(phase)){
			prevSeriesname = "上季度";
			seriesname = "本季度";
		}else if("month".equals(phase)){
			prevSeriesname = "上月";
			seriesname = "本月";
		}else if("week".equals(phase)){
			prevSeriesname = "上周";
			seriesname = "本周";
		}
		
		String prevDataSet = "<dataset seriesname='"+prevSeriesname+"' color='"+this.getRandomColor(0)+"'>";
		String nowDataSet = "<dataset seriesname='"+seriesname+"' color='"+this.getRandomColor(1)+"'>";
		
		String prevVal = "";//存放上一期显示数据
		String nowVal="";//存放本期显示数据
		int i =1;//用于累加
		String tempCount = "";
		for(String str : timeScope.split(",")){
			tempCount = i+"";
			if("quarter".equals(unit)){
				chart += "<category name='第"+i+"季' /> ";
			}else if("month".equals(unit)){
				if("quarter".equals(phase)){
					chart += "<category name='"+prevScope.get(tempCount)+"/"+str+"月' /> ";
				}else{
					chart += "<category name='"+str+"月' /> ";
				}
			}else if("week".equals(unit)){
				chart += "<category name='第"+i+"周' /> ";
			}else{
				if("week".equals(phase)){
					chart += "<category name='"+prevScope.get(tempCount)+"/"+str+"' /> ";
				}else{
					chart += "<category name='"+str+"' /> ";
				}
			}
			if(("quarter".equals(phase) && "month".equals(unit)) || ("week".equals(phase) && "day".equals(unit))){
				prevVal = prevMap.get(prevScope.get(tempCount)) == null ? "0" : prevMap.get(prevScope.get(tempCount));
			}else{
				prevVal = prevMap.get(str) == null ? "0" : prevMap.get(str);
			}
			nowVal = nowMap.get(str) == null ? "0" : nowMap.get(str);
			prevDataSet += "<set value='"+prevVal+"' />";
			nowDataSet += "<set value='"+nowVal+"' />";
			i++;
		}
		chart += "</categories>";
		chart += prevDataSet + "</dataset>";
		chart += nowDataSet + "</dataset>";
		chart += "</graph>";
		return chart;
	}
	/**
	 * 获取Sum还是count语句
	 * @param sumFieldName 判断是否累加 
	 * @param tableName 表名
	 * @return
	 */
	public String getSumOrCountSql(String sumFieldName,String tableName){
		StringBuilder sql = new StringBuilder();
		if(sumFieldName!=null && !"".equals(sumFieldName)){
			sql.append(" sum(").append(tableName).append(".").append(sumFieldName).append(")");
		}else{
			sql.append(" count(*)");
		}
		sql.append(" as counts from ").append(tableName);
		return sql.toString(); 
	}

	/**
	 * 随即获取18种颜色
	 * @return
	 */
	public String getRandomColor(int i){
		String[] color = {"47a91c", "db8933", "3796bf", "df7ba6", "e5acae","aedfa3", "f3d1a8", "a5daea", "f4c9df", "43bc97", "c7ad24","b54143", "cf69e2","cf6333", "d9d9d9", "abe7d9", "ece0a5", "efc0f6"};
		if(i+1>18){
			i = i%17;
		}
		return color[i];
	}
	
	/**
	 * 获得同比语句与时间段范围
	 * @param tableName 表名
	 * @param fieldName 字段名
	 * @param phase 历史阶段
	 * @param unit 单位
	 * @return
	 */
	public String[] getCompareInfo(String tableName,String fieldName,String phase,String unit,String searchTime,String sumFieldName,DBFieldInfoBean fieldInfoBean,ClientModuleViewBean moduleViewBean,String type,HttpServletRequest request){
		StringBuilder sql = new StringBuilder();
		String timeScope = "";//查询的所有时间段
		sql.append("SELECT ");
		String selectStr = "";//select条件
		String[] compareInfo = new String[2];//返回的信息

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar searchDate = Calendar.getInstance();
		try {
			searchDate.setTime(sdf.parse(searchTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int tempYear = searchDate.get(Calendar.YEAR);//查询时间的年份
		int tempMonth = searchDate.get(Calendar.MONTH)+1;//查询时间的月份
		int tempDay = searchDate.get(Calendar.DATE);//查询时间的几号
		
		
		//获取所有时间段
		if("month".equals(unit)){
			//单位为月份
			selectStr = "substring("+fieldName+",6,2)";
			if("year".equals(phase)){
				timeScope = "01,02,03,04,05,06,07,08,09,10,11,12";//12个月
			}else{
				if(tempMonth==1 || tempMonth==2 || tempMonth==3){
					timeScope = "01,02,03";
				}else if(tempMonth==4 || tempMonth==5 || tempMonth==6){
					timeScope = "04,05,06";
				}else if(tempMonth==7 || tempMonth==8 || tempMonth==9){
					timeScope = "07,08,09";
				}else{
					timeScope = "10,11,12";
				}
			}
		}else if("day".equals(unit)){
			selectStr = "substring("+fieldName+",6,5)";
		}else if("quarter".equals(unit)){
			//季度
			//String year = searchTime.substring(0,4);
			selectStr = "datediff(quarter, "+fieldName+",'"+tempYear+"')";
			timeScope = "0,-1,-2,-3";
		}else if("week".equals(unit)){
			String seasonDate = "";//统计phase的第一天与最后一天
			if("month".equals(phase)){
				 selectStr ="datediff(week,"+fieldName+",'"+tempYear+"-"+tempMonth+"-01')";//条件设置为查询时间的1号。方便统计
				 Calendar selTime = Calendar.getInstance();
				 selTime.set(tempYear, tempMonth-1,01); //Calendar的月从0-11，
				 seasonDate = DateUtil.getFirstDayOfMonth(tempYear, tempMonth) + ";" + DateUtil.getLastDateMonth(tempYear, tempMonth);//获得当月的第一天与最后一天用;隔开
			}else{
				selectStr ="datediff(week, createTime,'"+tempYear+"-";
				int month = tempMonth ;
				if(month == 1 || month ==2 || month ==3){
					selectStr+="01";
				}
				else if(month == 4 || month ==5 || month ==6){
					selectStr+="04";
				}
				else if(month == 7 || month ==8 || month ==9){
					selectStr+="07";
				}
				else if(month == 10 || month ==11 || month ==12){
					selectStr+="10";
				}
				selectStr +="-01')";
				seasonDate = DateUtil.getSeasonStartDayAndLastDay(tempYear, tempMonth);//获得本季的第一天与最后一天用;隔开
			}
			
			//获取有多少周
			int weekCount = 0;
			weekCount = DateUtil.getWeekCount(seasonDate.split(";")[0], seasonDate.split(";")[1]);//获取时间段的周数目
			int weekNo = 0;
			for(int i=0;i<weekCount;i++){
				timeScope += weekNo +",";
				weekNo = weekNo-1;
			}
		}
		
		LoginBean loginBean = getLoginBean(request);
		//查询的SQL语句
		sql.append(selectStr).append(" as ").append(fieldName).append(",").append(this.getSumOrCountSql(sumFieldName, tableName)).append(" WHERE 1=1 ");
		sql.append(this.getCheckBillScope(moduleViewBean,tableName,loginBean,fieldInfoBean,"",type,request)).append(" and ");
		
		
		
		if("day".equals(unit)){
			//处理同比单位:天
			sql.append(" substring(").append(fieldName).append(",1,10) ").append(" between '");
			if("month".equals(phase)){
				searchDate.set(Calendar.DATE, 1);
				String monthStartDay = sdf.format(searchDate.getTime());
				String monthEndDay = DateUtil.getLastDateMonth(tempYear, tempMonth);//获取本月最后一天
				long count = DateUtil.getTwoDay(monthEndDay,monthStartDay )+1;//计算相隔的天数
				//获取天数的时间段
				for(int i=0;i<count;i++){
					//String nowData = sdf.format(searchDate.getTime());
					//timeScope += nowData.substring(nowData.indexOf("-")+1) +",";
					timeScope += searchDate.get(Calendar.DATE)+",";
					searchDate.add(Calendar.DATE, 1);//天数加1
				}
				sql.append(monthStartDay).append("' and '").append(monthEndDay).append("'");
			}else{
				SimpleDateFormat df = new SimpleDateFormat("MM-dd");  
				sql.append(sdf.format(searchDate.getTime()));
				//查询时间起循环一周的时间
				for(int i=0;i<7;i++){
					timeScope +=  df.format(searchDate.getTime())+",";
					searchDate.add(Calendar.DATE, 1);//天数加1
				}
				searchDate.add(Calendar.DATE, -1);
				sql.append("' and '").append(sdf.format(searchDate.getTime())).append("'");
			}
		}else{
			sql.append(" datediff(").append(phase).append(",").append(fieldName).append(",'").append(searchTime).append("') =0 ");
		}
		
		sql.append(" GROUP BY ").append(selectStr).append(" ORDER BY ").append(selectStr);
		
		if("week".equals(unit) || "quarter".equals(unit)){
			sql.append(" DESC ");
		}
		
		compareInfo[0] = timeScope;
		compareInfo[1] = sql.toString();
		return compareInfo;
	}
	
	/**
	 * 获取统计数据sql语句,所有时间段信息,枚举SQL语句
	 * @param tableName 表名
	 * @param fieldName 字段名称
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param unit 单位
	 * @param sumFieldName sum(sumFieldName)合计字段名
	 * @param secondFieldName 多条件字段名
	 * @param dbTableName 枚举查询tableName
	 * @return
	 */
	public String[] getAroundInfo(String tableName,String fieldName,String startTime,String endTime,String unit,String sumFieldName,String secondFieldName,String dbTableName,DBFieldInfoBean fieldInfoBean,ClientModuleViewBean moduleViewBean,String type,HttpServletRequest request){
		String[] aroundInfo = new String[3];
		StringBuilder sql = new StringBuilder();//统计所有数据的sql语句
		StringBuilder secondSql = new StringBuilder();//枚举的sql语句
		String timeScope="";//所有的时间段
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String tempCondition = "";//select、group by、 order by条件
		String quarterCondition = "";//查询季度时需要多加一个select条件
		String order = "";//升序或降序
		LoginBean login = getLoginBean(request);
		sql.append("SELECT ");
		if("month".equals(unit)){
			SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
			int diffMonthCount = DateUtil.compareDate(startTime, endTime, 1)+1;//获得两个时间之间相差的月份数,+1加上结束时间的那个分月
			Calendar begin = Calendar.getInstance();
			try {
				begin.setTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//循环获得查询时间段的所有月份
			for(int i=0;i<diffMonthCount;i++){
				timeScope += sdfMonth.format(begin.getTime()) +",";
				begin.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
			}
			
			tempCondition = "substring("+fieldName+",1,7) ";
		}else if("quarter".equals(unit)){
			//获得当前时间的季度信息
			String startTimeSeason = DateUtil.getSeasonInfo(startTime);
			String endTimeSeason = DateUtil.getSeasonInfo(endTime);
			
			timeScope = startTimeSeason +",";//存放季度时间段
			String tempSeasonInfo=startTimeSeason;//临时存放开始时间的季度信息，如:2012年Q1
			int quarterNo=Integer.parseInt(startTimeSeason.substring(startTimeSeason.length()-1));//获得开始时间的季度编号
			int quarterYear=Integer.parseInt(startTime.substring(0,4));//获得开始时间的年份
			while(true){
				quarterNo ++;//从下一季度开始
				//若相等结束循环
				if(endTimeSeason.equals(tempSeasonInfo)){
					break;
				}
				//若%4==0表示Q4
				if(quarterNo%4 == 0){
					tempSeasonInfo = quarterYear+quarterNo/4-1 +"年Q4";
				}else{
					tempSeasonInfo = quarterYear + quarterNo/4 +"年Q"+quarterNo%4;
				}
				timeScope +=tempSeasonInfo+",";
			}
			
			//查询时间根据开始时间1月开始查询,才能获得准确的季度
			Calendar beginTime = Calendar.getInstance();
			try {
				beginTime.setTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			beginTime.set(Calendar.MONTH, 0);//设置为开始日期年份的1月
			tempCondition = "datediff(quarter, "+fieldName+",'"+sdf.format(beginTime.getTime())+"') ";
			quarterCondition = ",substring("+fieldName+",1,4) ";//查询季度加个sel条件
			order = "DESC";//倒叙排列
		}else if("week".equals(unit)){
			int weekCount = DateUtil.getWeekCount(startTime, endTime);//获得时间段之前有多少周
			int weekNo = 0;//从0开始
			for(int i=0;i<weekCount;i++){
				timeScope += weekNo +",";
				weekNo = weekNo-1;
			}
			tempCondition = "datediff(week, "+fieldName+",'"+startTime+"') ";
			order = "DESC";
		}else{
			long count = DateUtil.getTwoDay(startTime,endTime)+1;//获取两个日期期间的天数
			if(!startTime.equals(endTime)){
				count = Math.abs(count)+2;
			}
			Calendar day = Calendar.getInstance();//开始时间
			try {
				day.setTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//循环获得所有天数
			for(int i=0;i<count;i++){
				timeScope += sdf.format(day.getTime()) +",";
				day.add(Calendar.DATE, 1);//天数加1
			}
			tempCondition = "substring("+fieldName+",1,10) ";
		}
		
		
		//SQL语句
		sql.append(tempCondition).append(" as ").append(fieldName).append(quarterCondition);
		
		//若单位为季度时
		if("quarter".equals(unit)){
			sql.append(" as year ");
		}

		sql.append(",");
		
		String enumJoinSql = "";
		//若有多条件查询时。
		if(secondFieldName !=null && !"".equals(secondFieldName)){
			enumJoinSql = this.getEnumjoinSql(secondFieldName, tableName, dbTableName);
			sql.append("isnull(").append(tableName).append(".").append(secondFieldName).append(",'') as ").append(secondFieldName).append(",tbllanguage.zh_CN as zh_CN,");
			
			//若有类型查询,查询出枚举的语句
			secondSql.append("SELECT isnull(").append(tableName).append(".").append(secondFieldName).append(",'') as ").append(secondFieldName).append(",tbllanguage.zh_CN as zh_CN,tblDBEnumerationItem.enumOrder,").append(this.getSumOrCountSql(sumFieldName, tableName))
			.append(enumJoinSql).append(" where 1=1 ").append(this.getCheckBillScope(moduleViewBean, tableName, login, fieldInfoBean, secondFieldName,type, request))
			.append(" and ").append(fieldName).append(">='").append(startTime).append("' and ").append(fieldName).append(" <= '").append(endTime).append(" 23:59:999'")
			.append(" group by isnull(").append(tableName).append(".").append(secondFieldName).append(",''),tbllanguage.zh_CN,tblDBEnumerationItem.enumOrder order by  tblDBEnumerationItem.enumOrder,counts desc");
		}
		
		sql.append(this.getSumOrCountSql(sumFieldName, tableName));
		if(!"".equals(enumJoinSql)){
			sql.append(enumJoinSql);
		}
		sql.append(" where 1=1 ").append(this.getCheckBillScope(moduleViewBean, tableName, login, fieldInfoBean, secondFieldName, type, request))
		.append(" and ").append(fieldName).append(">='").append(startTime).append("' and ").append(fieldName).append(" <= '").append(endTime).append(" 23:59:999'")
		.append(" GROUP BY ").append(tempCondition).append(quarterCondition);
		if(secondFieldName !=null && !"".equals(secondFieldName)){
			sql.append(",zh_CN,").append(tableName).append(".").append(secondFieldName);
		}
		sql.append(" ORDER BY ").append(tempCondition).append(order);
		
		aroundInfo[0]=timeScope;//所有时间段
		aroundInfo[1]=sql.toString();//统计所有数据sql语句
		aroundInfo[2]=secondSql.toString();//枚举sql语句
		return aroundInfo;
	}
	
	
	/**
	 * 获取环比MS统计图与类型总和Map
	 * @param mulFieldsMap 多条件的数据MAP
	 * @param enumList 所有枚举的list
	 * @param timeScope 时间段
	 * @param unit 环比单位
	 * @param xName x轴名称
	 * @param yName y轴名称
	 * @param captionName 统计图头名称
	 * @param request
	 * @return
	 */
	public String getAroundMsChart(HashMap<String, HashMap<String,String>> mulFieldsMap,List<String[]> enumList,String timeScope,String unit,String yName,String captionName,HttpServletRequest request){
		/*List labelList = new ArrayList();
		List dataList = new ArrayList();
		//头部
		String chart = "<graph xaxisname='类型' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12'>";
		chart +="<categories font='Arial' fontSize='12' fontColor='000000'>";
		
		HashMap<String,String> totalMap = new HashMap<String, String>();//存放每种类型的总和,主要为了页面显示
		
		double count = 0;//求和
		String keyVal = "";//枚举的值
		String enumName ="";
		for(String[] enumInfo : enumList){
			if(enumInfo[1] == null || "".equals(enumInfo[1])){
				enumName = "空";
			}else{
				enumName = enumInfo[1];
			}
			chart +="<category name='"+enumName+"' />";
			labelList.add(enumName);
			for(String time : timeScope.split(",")){
				HashMap<String,String> map = mulFieldsMap.get(time);//根据时间段获取map
				if(map == null){
					keyVal = "0";//如果map为null,表示这个时间下没有任何枚举的值,默认给0
				}else{
					keyVal = map.get(enumInfo[0]) == null ? "0" : mulFieldsMap.get(time).get(enumInfo[0]);//若根据枚举key拿值为null,默认给0
				}
				count += Double.parseDouble(keyVal);//统计类型的总和
			}
			totalMap.put(enumInfo[0],count+"");
			count =0;
		}
		request.setAttribute("labelList",gson.toJson(labelList));
		request.setAttribute("totalMap",totalMap);
		request.setAttribute("timeScopeSize",timeScope.split(",").length);//时间段的个数,用于页面求平均值
		chart += "</categories>";

		int i=1;
		String seriesname = "";//dataset的seriesname名称
		for(String time : timeScope.split(",")){
			List list = new ArrayList();
			HashMap iChartMap = new HashMap();
			double rowCount = 0;
			if("week".equals(unit)){
				seriesname = "第"+i+"周";//单位为周,从第一周开始算
			}else{
				seriesname = time;
			}
			chart +="<dataset seriesname='"+seriesname+"' color='"+this.getRandomColor(i)+"'>";
			//循环获取dataset显示数据
			HashMap<String,String> map = mulFieldsMap.get(time);
			for(String[] enumInfo : enumList){
					if(map != null){
						keyVal = map.get(enumInfo[0])==null ? "0":map.get(enumInfo[0]);
						rowCount +=Double.parseDouble(keyVal);
					}else{
						keyVal = "0";
					}
					chart +="<set value='"+keyVal+"'/>";
					list.add(keyVal);
			}
			chart +="</dataset>";
			
			iChartMap.put("name", seriesname);
			iChartMap.put("value", list);
			iChartMap.put("color","#"+this.getRandomColor(i));
			dataList.add(iChartMap);
			i++;
			if(map!=null){
				map.put("rowCount", rowCount+"");
			}
		}
		chart += "</graph>";
		return gson.toJson(dataList);
		*/
		
//		头部
		String chart = "<graph xaxisname='类型' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12' formatNumberScale='0'>";
		chart +="<categories font='Arial' fontSize='12' fontColor='000000'>";
		
		HashMap<String,String> totalMap = new HashMap<String, String>();//存放每种类型的总和,主要为了页面显示
		
		double count = 0;//求和
		String keyVal = "";//枚举的值
		String enumName ="";
		for(String[] enumInfo : enumList){
			if(enumInfo[1] == null || "".equals(enumInfo[1])){
				enumName = "空";
			}else{
				enumName = enumInfo[1];
			}
			chart +="<category name='"+enumName+"' />";
			
			for(String time : timeScope.split(",")){
				HashMap<String,String> map = mulFieldsMap.get(time);//根据时间段获取map
				if(map == null){
					keyVal = "0";//如果map为null,表示这个时间下没有任何枚举的值,默认给0
				}else{
					keyVal = map.get(enumInfo[0]) == null ? "0" : mulFieldsMap.get(time).get(enumInfo[0]);//若根据枚举key拿值为null,默认给0
				}
				count += Double.parseDouble(keyVal);//统计类型的总和
			}
			totalMap.put(enumInfo[0],count+"");
			count =0;
		}
		request.setAttribute("totalMap",totalMap);
		request.setAttribute("timeScopeSize",timeScope.split(",").length);//时间段的个数,用于页面求平均值
		chart += "</categories>";

		int i=1;
		String seriesname = "";//dataset的seriesname名称
		for(String time : timeScope.split(",")){
			double rowCount = 0;
			if("week".equals(unit)){
				seriesname = "第"+i+"周";//单位为周,从第一周开始算
			}else{
				seriesname = time;
			}
			chart +="<dataset seriesname='"+seriesname+"' color='"+this.getRandomColor(i)+"'>";
			//循环获取dataset显示数据
			HashMap<String,String> map = mulFieldsMap.get(time);
			for(String[] enumInfo : enumList){
					if(map != null){
						keyVal = map.get(enumInfo[0])==null ? "0":map.get(enumInfo[0]);
						rowCount +=Double.parseDouble(keyVal);
					}else{
						keyVal = "0";
					}
					chart +="<set value='"+keyVal+"'/>";
			}
			chart +="</dataset>";
			i++;
			if(map!=null){
				map.put("rowCount", rowCount+"");
			}
		}
		chart += "</graph>";
		return chart;
		
	}
	
	/**
	 * 异步检查是季度是否大于时间段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward checkUnit(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String startTime = getParameter("startTime", request);
		String endTime = getParameter("endTime", request);
		//获得当前时间的季度信息
		String startTimeSeason = DateUtil.getSeasonInfo(startTime);
		String endTimeSeason = DateUtil.getSeasonInfo(endTime);
		if(startTimeSeason.equals(endTimeSeason)){
			request.setAttribute("msg","yes");//表示同一季度
		}else{
			request.setAttribute("msg","no");//表示不统一季度
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 获取枚举类型join语句
	 * @param fieldName
	 * @param tableName
	 * @param dbTableName
	 * @return
	 */
	public String getEnumjoinSql(String fieldName,String tableName,String dbTableName){
		StringBuilder sql = new StringBuilder();//枚举的sql语句
		sql.append(" left join tblDBEnumerationItem  on ").append(tableName).append(".").append(fieldName)
		.append("=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumId =(select id from tblDBEnumeration where enumName=(select refEnumerationName from tblDBfieldInfo where tableid=(select id from tbldbtableInfo where tablename='")
		.append(dbTableName).append("') and fieldName='")
		.append(fieldName).append("')) left join tbllanguage  on tblDBEnumerationItem.languageId=tbllanguage.id ");
		
		return sql.toString();
	}
	
	/**
	 * 查询枚举值为空
	 * @param fieldName
	 * @param tableName
	 * @param dbTableName
	 * @return
	 */
	public String getEmptyVal(String fieldName,String tableName,String dbTableName){
		String enumValStr="";
//		枚举值为空,需要查看是否存在有枚举值多语言为空的选项.
		Result rs = mgt.queryDetailEmpty(tableName, fieldName, dbTableName);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List<String> emptyList = (List<String>)rs.retVal;
			if(emptyList !=null && emptyList.size()>0){
				enumValStr = "";
				for(String value : emptyList){
					enumValStr +="'"+value+"',";
				}
				if(enumValStr.endsWith(",")){
					enumValStr = enumValStr.substring(0,enumValStr.length()-1);
				}
			}
		}
		
		//若没有值默认给'',不然会报错
		if("".equals(enumValStr)){
			enumValStr = "''";
		}
		return enumValStr;
	}
	
	private ActionForward weekMonthQuery(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String isWeekQuery = getParameter("isWeekQuery", request);//isWeekQuery == true 表示周查询
		if(isWeekQuery==null || "".equals(isWeekQuery)){
			Calendar nowDate = Calendar.getInstance();
			nowDate.setTime(new Date());
			request.setAttribute("year", nowDate.get(Calendar.YEAR));
			request.setAttribute("month", nowDate.get(Calendar.MONTH)+1);
		}
		
		request.setAttribute("isWeekQuery", isWeekQuery);
		return getForward(request, mapping, "weekMonth");
	}
	
	private ActionForward weekMonthDetailQuery(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String isWeekQuery = getParameter("isWeekQuery", request);//isWeekQuery == true 表示周查询
		String weekStartTime = getParameter("weekStartTime", request);//周查询开始时间
		String weekEndTime = getParameter("weekEndTime", request);//周查询结束时间
		String sortName = getParameter("sortName", request);//排序名称
		LoginBean login = getLoginBean(request);
		String type = getParameter("type", request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		
		StringBuilder querySql = new StringBuilder();//总和语句
		StringBuilder countSql = new StringBuilder();//总和语句
		StringBuilder publicSql = new StringBuilder();//公共查询语句
		
		String searchTime = "";
		if(isWeekQuery == null || "".equals(isWeekQuery)){
			int year = Integer.parseInt(getParameter("year", request));//月查询年份
			int month = Integer.parseInt(getParameter("month", request));//月查询月份
			Calendar selDate = Calendar.getInstance();
			selDate.set(year,month-1, 1);//月份查询取第一天
			searchTime = sdf.format(selDate.getTime());
			String endTime = DateUtil.getLastDateMonth(year, month);//获取分月最后一天。显示到页面
			request.setAttribute("year", year);
			request.setAttribute("month", month);
			request.setAttribute("startTime",searchTime);
			request.setAttribute("endTime",endTime);
		}else{
			String weekName = getParameter("weekName",request);//第几周名称	
			String isSearch = getParameter("isSearch", request);
			
			//若表单提交不用转编码
			if(isSearch==null || "".equals(isSearch)){
				weekName = GlobalsTool.toChinseChar(weekName);
			}
			
			//取周月份
			Calendar weekDate = Calendar.getInstance();
			try {
				weekDate.setTime(sdf.parse(weekStartTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			request.setAttribute("month",weekDate.get(Calendar.MONTH)+1);
			request.setAttribute("weekName",weekName.replaceAll(" ",""));
		}

		String selcondition = " isnull(employeeId,'') ";//select查询条件
		querySql.append("select isnull(a.employeeId,'') as employeeId,");
		countSql.append(" select ");
		
		String sort = "";
		if(sortName !=null && !"".equals(sortName)){
			sort = " ORDER BY ";
			if("employeeId".equals(sortName)){
				sort += " a.employeeId ";
			}else{
				sort += " sum(a."+sortName+") ";
			}
			sort += " DESC ";
		}
		
		//客户资料sql
		publicSql.append("sum(a.clientCount) clientCount,sum(a.followUpCount) followUpCount,sum(a.contractCount) contractCount,sum(a.contractSum) contractSum,sum(a.receiveCount) receiveCount,sum(a.receiveSum) receiveSum,sum(a.feeSum) feeSum from( select isnull(createBy,'') as employeeId,count(*) as clientCount,0 as followUpCount,0 as contractCount,0 as contractSum,0 as receiveCount,0 as receiveSum,0 as feeSum from crmclientinfo where 1=1 and status != '1' ").append(this.getCheckBillScope(null, "CRMClientInfo", login, null, null, type, request));
		publicSql.append(this.WeekMonthTimeScope(isWeekQuery, "createTime", weekStartTime, weekEndTime, searchTime)).append(" group by isnull(createBy,'') union all select");
		//销售跟单sql
		publicSql.append(selcondition).append(",0,count(*),0,0,0,0,0 from CRMSaleFollowUp where 1=1 ").append(this.getCheckBillScope(null, "CRMSaleFollowUp", login, null, "", type, request))
		.append(this.WeekMonthTimeScope(isWeekQuery, "VisitTime", weekStartTime, weekEndTime, searchTime)).append(" group by ").append(selcondition).append(" union all select");
		//合同sql
		publicSql.append(selcondition).append(",0,0,count(*),sum(TotalAmount),0,0,0 from CRMSaleContract where 1=1 ").append(this.getCheckBillScope(null, "CRMSaleContract", login, null, "", type, request))
		.append(this.WeekMonthTimeScope(isWeekQuery, "SignUpDate", weekStartTime, weekEndTime, searchTime)).append(" group by ").append(selcondition).append(" union all select");
		//回款
		publicSql.append(selcondition).append(",0,0,0,0,count(*),sum(ExeBalAmt),0 from CRMSaleReceive where 1=1 ").append(this.getCheckBillScope(null, "CRMSaleReceive", login, null, "", type, request))
		.append(this.WeekMonthTimeScope(isWeekQuery, "BillDate", weekStartTime, weekEndTime, searchTime)).append(" group by ").append(selcondition).append(" union all select");
		//费用
		publicSql.append(selcondition).append(",0,0,0,0,0,0,sum(TotalAmount) from CRMFee WHERE 1=1 ").append(this.getCheckBillScope(null, "CRMFee", login, null, "", type, request))
		.append(this.WeekMonthTimeScope(isWeekQuery, "createTime", weekStartTime, weekEndTime, searchTime)).append(" group by ").append(selcondition);
		
		
		querySql.append(publicSql.toString()).append("  ) a group by isnull(a.employeeId,'') ").append(sort);
		countSql.append(publicSql.toString()).append("  ) a ").append(sort);
		
		Result rs = mgt.queryWeekMonth(querySql.toString().toString());
		Result result = mgt.queryWeekMonth(countSql.toString().toString());
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//成功
			request.setAttribute("weekMonthList", rs.retVal);
			request.setAttribute("countList", result.retVal);
			request.setAttribute("isWeekQuery", isWeekQuery);
			request.setAttribute("weekStartTime", weekStartTime);
			request.setAttribute("weekEndTime", weekEndTime);
			request.setAttribute("sortName", sortName);
			return getForward(request, mapping, "weekMonthDetail");
		}else{
			//查询失败
			EchoMessage.error().add("查询失败").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}
	
	/**
	 * 周月报表时间段
	 * @param isWeekQuery 是否周查询
	 * @param timeFieldName 时间字段
	 * @param weekStartTime 开始时间
	 * @param weekEndTime 结束时间
	 * @param searchTime 月查询时间
	 * @return
	 */
	public String WeekMonthTimeScope(String isWeekQuery,String timeFieldName,String weekStartTime,String weekEndTime,String searchTime){
		String timeScope = " and ";
		if(isWeekQuery !=null && "true".equals(isWeekQuery)){
			timeScope += timeFieldName +" between '" + weekStartTime + "' and '" +weekEndTime +" 23:59:999' ";
		}else{
			timeScope += " datediff(month,"+timeFieldName+",'"+searchTime+"')=0 ";
		}
		return timeScope;
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		// TODO Auto-generated method stub
		return null;
	}
}
