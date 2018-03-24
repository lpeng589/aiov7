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
		//���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// ���ܷ���
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
	 * �����
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
			List<String[]> moduleList = (ArrayList<String[]>)result.retVal;//��ȡģ����Ϣ
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
	 * ��벿
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward left(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String classesName = getParameter("classesName", request);//��������
		String tableName = getParameter("tableName", request);//��ṹ����
		String compareFlag = getParameter("compareFlag", request);//�Ƿ�ͬ�Ȼ��ȱ���
		//String firstEnter = getParameter("firstEnter", request);//�Ƿ��һ�ν���
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
				List<String[]> moduleList = (ArrayList<String[]>)result.retVal;//��ȡģ����Ϣ
				if((moduleId ==null || "".equals(moduleId)) && moduleList!=null && moduleList.size()>0){
					moduleId =moduleList.get(0)[1];
				}
				ClientModuleBean moduleBean = (ClientModuleBean)moduleMgt.detailCrmModule(moduleId).retVal;
				tableName = moduleBean.getTableInfo().split(":")[0];
				
				
				result = moduleMgt.queryModuleGroupBy(login);//��ѯ�û��Ƿ���Ȩ�޹ۿ�ģ��
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
	 * ���۸���������ͬ/�ؿ���ۺ���񱨱��ò�ѯģ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String moduleId = getParameter("moduleId", request);//ģ��ID
		String tableName = getParameter("tableName", request);//����
		String sumFieldName = getParameter("sumFieldName", request);//��ʾͳ�ƽ��
		String fieldName = getParameter("fieldName", request);//ͳ�Ƶ��ֶ���
		String secondFieldName = getParameter("secondFieldName", request);// "/"��ͳ�Ƶ��ֶ���
		String top = getParameter("top", request);// ���ݿ�TOP��ʶ
		String searchSwfName = getParameter("searchSwfName", request);// ѡ���ͳ��ͼ����
		String isSearch = getParameter("isSearch", request);// �Ƿ��ѯ�������� ,���ڿͻ����ϱ���
		String isAround = getParameter("isAround", request);// true��ʾ��ѯ���ȵķ���ͳ��
		LoginBean login = getLoginBean(request) ;
		
		request.setAttribute("isAround", isAround);
		request.setAttribute("highSearch", getParameter("highSearch", request));//true��ʾ�߼���ѯ
		request.setAttribute("top", top);//���ݿ�TOP��ʶ
		request.setAttribute("searchSwfName", request.getParameter("searchSwfName"));//searchSwfName��ʾ�û��Լ�ѡ����ͼ��.����Ĭ�ϵ�
		
		String chartSwfName = "Column3D";//Ĭ�ϵ�FusionCharts��ʾͼ
		//String iChartName = "Bar2D";//Ĭ�ϵ�iChart��ʾͼ
		
		String xName = "";//����������
		String yName = "����";//����������
		if(sumFieldName != null && !"".equals(sumFieldName)){
			yName = "���";
		}	
		
		//caption���� 
		String captionName = getParameter("captionName", request); 
		if (StringUtils.isNotBlank(captionName) && !"true".equals(isSearch)){
			captionName = GlobalsTool.toChinseChar(captionName);
    	}
		
		DBFieldInfoBean fieldInfoBean = new DBFieldInfoBean();
		ClientModuleBean moduleBean = new ClientModuleBean();
		ClientModuleViewBean moduleViewBean = new ClientModuleViewBean();
		String dbTableName = tableName;//��ѯö��sqlʱ��Ҫ�õ���tableName
		if(moduleId !=null && !"".equals(moduleId)){
			//�ͻ����ϱ���
			moduleBean = (ClientModuleBean)moduleMgt.detailCrmModule(moduleId).retVal;
			moduleViewBean = (ClientModuleViewBean)moduleMgt.loadModuleView("1_"+moduleId).retVal;
			if(fieldName == null || "".equals(fieldName)){
				//��һ�ν��룬Ĭ��ȡinputType==1��һ��
				DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(moduleBean.getTableInfo().split(":")[0]);
				for(DBFieldInfoBean fieldInfo : tableInfo.getFieldInfos()){
					if(fieldInfo.getInputType() == 1 && !"Level".equals(fieldInfo.getFieldName()) && !"LastContractTime".equals(fieldInfo.getFieldName()) && !"extend1".equals(fieldInfo.getFieldName()) && !"extend2".equals(fieldInfo.getFieldName()) && !"extend3".equals(fieldInfo.getFieldName())){
						fieldInfoBean = fieldInfo;
						fieldName = fieldInfo.getFieldName();
						captionName = fieldInfoBean.getDisplay().get(getLocale(request).toString()) +"�ֲ�";
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
		
		//SQL����װ��ʼ
		StringBuilder sql = new StringBuilder();//��ѯ��sql���
		LinkedHashMap<String,List<String[]>> msDataMap = new LinkedHashMap<String, List<String[]>>();//���ڶ����Ͳ�ѯ��װ��map 

		sql.append("select ");
		
		if(top!=null && !"".equals(top)){
			sql.append(" top ").append(top);
		}
		
		//����ʡ��,�б���Ľ�ȡλ��������
		String subEnd ="";//��ȡ����λ��
		String districtCondition ="";//��������
		if("province".equals(fieldName)||"city".equals(fieldName)){
			if("province".equals(fieldName)){
				xName = "ʡ��";
				subEnd ="10";
			}else{
				subEnd ="15";
				xName = "����";
				districtCondition = "len(isnull(substring(CRMClientInfo.district,1,15),'')) !=10";
			}
		}
		 
		sql.append(" isnull(").append(tableName+"."+fieldName).append(",'') as ").append(fieldName);//select�ֶ����
		 
		if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6){
			//ʱ������,ͳ���·�
			xName = "�·�";
			//iChartName ="LineBasic2D";
			chartSwfName ="Line";
			sql = new StringBuilder();
			sql.append("select isnull(substring(").append(tableName).append(".").append(fieldName).append(",1,7),'') as ").append(fieldName).append(",isnull(substring(").append(tableName).append(".").append(fieldName).append(",1,7),'') as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
		}else if(fieldInfoBean.getInputType() == 1 || "FollowStatus".equals(fieldInfoBean.getFieldName())){
			xName = "����";
			//ͳ��ö������
			sql.append(",tbllanguage.zh_CN as zh_CN,tblDBEnumerationItem.enumOrder,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join tblDBEnumerationItem  on ").append(tableName).append(".").append(fieldName).append("=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumId =(select tblDBEnumeration.id from tblDBEnumeration where enumName=(select refEnumerationName from tblDBfieldInfo where tableid=(select id from tbldbtableInfo where tablename='");
			//���ǿͻ����ϵĸ���ģ�����Ϣ�ı���
			if("CRMClientInfo".equals(tableName)){
				sql.append(moduleBean.getTableInfo().split(":")[0]);
			}else{
				sql.append(tableName);
			}
			sql.append("') and fieldName='").append(fieldName).append("')) left join tbllanguage  on tblDBEnumerationItem.languageId=tbllanguage.id ");
			//iChartName = "Column2D";
		}else if("employeeId".equals(fieldName)|| "createBy".equals(fieldName)){
			//ͳ����
			if("employeeId".equals(fieldName)){
				xName = "������";
			}else{
				xName = "������";
			}
			chartSwfName ="Bar2D";
			sql.append(",tblEmployee.empfullname as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join tblemployee on ").append(tableName).append(".").append(fieldName).append("=tblemployee.id");
		}else if("goodsCode".equals(fieldName)){
			chartSwfName ="Pie3D";
			//ͳ����Ʒ
			xName ="��Ʒ";
			sql.append(",tblGoods.GoodsFullName+tblGoods.goodsNumber as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join tblGoods on ").append(tableName).append(".").append(fieldName).append("=tblGoods.classCode ");
		}else if("clientId".equals(fieldName) || "f_brother".equals(fieldName)){
			xName = "�ͻ�";
			String clientName = "";
			chartSwfName ="Bar2D";
			//ͳ�ƿͻ�
			sql.append(",CRMClientInfo.clientName as zh_CN,").append(this.getSumOrCountSql(sumFieldName, tableName));
			sql.append(" left join CRMClientInfo on ").append(tableName).append(".").append(fieldName).append("= CRMClientInfo.id ") ;
		}else if("Trade".equals(fieldName)){
			xName = "��ҵ";
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
		
		//��ͬ��Ʒ������left join������
		if("CRMSaleContractDet".equals(tableName)){
			sql.append(" left join CRMSaleContract on CRMSaleContract.id=CRMSaleContractDet.f_ref");
		}
		
		//where���
		sql.append(" where 1=1 ");
		
		
		
		//�����б���"��"����
		if(!"".equals(districtCondition)){
			sql.append(" and ").append(districtCondition);
		}
		
		//���ϲ鿴����Ȩ������
		sql.append(this.getCheckBillScope(moduleViewBean,tableName,login,fieldInfoBean,secondFieldName,"",request)).append(" group by ");
		
		//group by���
		if(fieldInfoBean.getFieldType() != 5 && fieldInfoBean.getFieldType() != 6 && !"province".equals(fieldName) && !"city".equals(fieldName)){
			//���ж��Ƿ��������ͻ���ʡ��,���ǵĽ���.�ǵĻ���������
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
			//ʡ��,����
			sql.append(" isnull(substring(CRMClientInfo.district,1,").append(subEnd).append("),''),tbldistrict.").append(fieldName);
		}
		
		//���ڶ�����ͳ�Ƶ��ֶ�����
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
			String tempTableName = tableName;//��ʱtableName
			if("SignUpDate".equals(secondFieldName)){
				tempTableName = "CRMSaleContract";
			}
			//iChartName ="Column3D";
			chartSwfName ="Column3D";
			String secondField ="";//secondFieldƴ��
			if(secondfieldInfoBean.getFieldType()==5 || secondfieldInfoBean.getFieldType()==6){
				secondField = "isnull(substring("+tempTableName+"."+secondFieldName+",1,7),'') ";
			}else if("CRMSaleContractDet".equals(tableName)){
				secondField = "CRMSaleContract." + secondFieldName; 
			}else{
				secondField = tableName +"." + secondFieldName; 
			}
			
			//�ָ������������ƴ��
			StringBuilder countSql = new StringBuilder();//ͳ�Ƶ�sql���
			String tempSql =sql.toString();//�����ʱsql���
			String fromSql = tempSql.substring(0,tempSql.indexOf("from"));//...from֮ǰ��sql���
			String joinSql = tempSql.substring(tempSql.indexOf("from"),tempSql.indexOf("where"));//from...where���
			String whereSql = tempSql.substring(tempSql.indexOf("where"));//where...���
			
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
				xName = "����";
				countSql.append(" left join tblDBEnumerationItem  on ").append(tableName).append(".").append(secondFieldName).append("=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumId =(select id from tblDBEnumeration where enumName=(select refEnumerationName from tblDBfieldInfo where tableid=(select id from tbldbtableInfo where tablename='");
				//���ǿͻ����ϵĸ���ģ�����Ϣ�ı���
				if("CRMClientInfo".equals(tableName)){
					countSql.append(moduleBean.getTableInfo().split(":")[0]);
				}else{
					countSql.append(tableName);
				}
				countSql.append("') and fieldName='").append(secondFieldName).append("')) left join tbllanguage  on tblDBEnumerationItem.languageId=tbllanguage.id ");
			}else if("clientId".equals(secondFieldName)){
				countSql.append(" left join CRMClientInfo on ").append(tableName).append(".").append(secondFieldName).append("= CRMClientInfo.id ") ;
			}else{
				xName = "�·�";
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
		
		//ʱ�����Ͱ�˳������
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
		String chart = "";//��װchartͳ��ͼ
		Result rs = mgt.queryFieldCount(sql.toString(),fieldName,null,sumFieldName);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if(secondFieldName ==null || "".equals(secondFieldName)){
				//����һ����
				List<String[]> dataList = (List<String[]>)rs.retVal;
				if(dataList == null || dataList.size()==0){
					request.setAttribute("count", 0);
				}else{
					chart = this.getChart(dataList,captionName, xName, yName,sumFieldName, chartSwfName,request);//��ȡ��������ͨ��ͳ��ͼ
					request.setAttribute("dataList", dataList);
					request.setAttribute("dataListStr", chart.toString());
				}
			}else{
				//���������
				List<String[]> enumList = (List<String[]>)rs.retVal;//������͵�����
				if((msDataMap == null || msDataMap.size()==0)|| (enumList==null ||enumList.size()==0)){
					request.setAttribute("count", 0);
				}else{
					chart = this.getMSChart(msDataMap, enumList,request,captionName,xName,yName,secondFieldName);//��ȡMS���͵�ͳ��ͼ
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
			//ɾ��ʧ��
			EchoMessage.error().add("��ѯʧ��").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}
	
	/**
	 * ���۸���������ͬ/�ؿ���ۺ���񱨱��ò�ѯģ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	private ActionForward detailList(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String moduleId = getParameter("moduleId", request);//ģ��ID
		String tableName = getParameter("tableName", request);//����
		String fieldName = getParameter("fieldName", request);//ͳ�Ƶ��ֶ���
		String enumVal = getParameter("enumVal", request);//�ֶ�ö��ֵ
		String condition = getParameter("condition", request) ==null ? "" : getParameter("condition", request) ;//��ϸ����
		
		//������Ϣ
		
		//�����ҳ����ת��
		try {
			condition =  URLDecoder.decode(condition,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//��ȡö�ٿ�ֵ��ѯ����
		String enumValStr = "'"+enumVal+"'";//Ĭ�ϲ�ѯ����
		if("".equals(enumVal) && !"province".equals(fieldName) && !"city".equals(fieldName)){
			enumValStr = this.getEmptyVal(fieldName, tableName, tableName);
		}
		
		//��ҳ��Ϣ
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	pageSize = 30;
        }
        
        
        StringBuilder querySql = new StringBuilder();//��ѯ���
       
        
        //��ȡselect�����Ϣ
		if(moduleId == null || "".equals(moduleId)){
			 DBFieldInfoBean fieldInfoBean = GlobalsTool.getFieldBean(tableName, fieldName);
			Result rs = new CRMBrotherSettingMgt().loadBrotherFieldDisplayBean(tableName);
			BrotherFieldDisplayBean fieldDisplayBean = (BrotherFieldDisplayBean)rs.retVal;
			
			if(fieldInfoBean==null){
				fieldInfoBean = new DBFieldInfoBean();
			}
			
			if(fieldDisplayBean == null ){
				//�ھӱ���ϸ�б�
				String reportTabelName="";//��������
				if("CRMSaleContractDet".equals(tableName)){
					reportTabelName ="CRMSaleContractDetail";
				}else{
					reportTabelName = tableName;
				}
				DefineReportBean reportBean = DefineReportBean.parse(reportTabelName+"SQL.xml", getLocale(request).toString(),getLoginBean(request).getId());//���ݱ����õ�����������Ϣ
				querySql.append("SELECT DISTINCT id,");
				String sql = reportBean.getSql();//��ȡsql
				String orderName = "";//��˳���fieldName,����sqlListMaps��ѯ
				String fieldSql = "";//��ѯ���ֶ�ƴ��
				sql = sql.substring(0,sql.indexOf("where 1=1")+"where 1=1".length());//��ȡ�������õ�sql��䵽where 1=1
				List<ReportField> fieldlist = reportBean.getDisFields();
				for(ReportField field :fieldlist){
					fieldSql += field.getAsFieldName() +",";
					if("".equals(orderName)){
						//���ݵ�һ���ֶ���˳��
						orderName = field.getAsFieldName();
					}
				}
				if(fieldSql.endsWith(",")){
					fieldSql = fieldSql.substring(0,fieldSql.length()-1);
				}
				//ƴ��sql���
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
			//�ͻ��б���ϸ�б�
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
						//�������ʡ�ݵ���ս������鲻Ҫ������
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
			//�ɹ�
			request.setAttribute("rsList",rs.retVal) ;
			request.setAttribute("pageBar",pageBar2(rs, request));
			request.setAttribute("tableName",tableName);
			request.setAttribute("fieldName", fieldName);
			request.setAttribute("condition", condition);
			request.setAttribute("moduleId", moduleId);
			request.setAttribute("enumVal",enumVal);
			
		}else{
			//��ѯʧ��
			EchoMessage.error().add("��ѯ��ϸʧ��").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		
		return getForward(request, mapping, "detail");
	}
	/**
	 * ͬ�ȷ��Ȳ�ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward compareQuery(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String moduleId = getParameter("moduleId", request);//ģ��id
		String tableName = getParameter("tableName", request);//����
		String fieldName = getParameter("fieldName", request);//�ֶ���
		String phase = getParameter("phase", request); //��ʷ�׶�,����ͬ��
		String unit = getParameter("unit", request);//��λ
		String sumFieldName = getParameter("sumFieldName", request);//sum(�ֶ���)
		String isAround = getParameter("isAround", request);//true��ʾ���Ȳ�ѯ
		String captionName = getParameter("captionName", request);//ͳ��ͼͷ����
		String searchTime = getParameter("searchTime", request);//ͬ��ѡ���ʱ��
		String isSearch = getParameter("isSearch", request);// �Ƿ��ѯ�������� ,���ڿͻ����ϱ���
		String secondFieldName = getParameter("secondFieldName", request);//��������fieldName
		String searchSwfName = getParameter("searchSwfName", request);//searchSwfName��ʾ�û��Լ�ѡ����ͼ��.û����Ĭ��
		String xName = getParameter("xName", request);//ͼ�κ���������
		String type = getParameter("type", request);//compare��ʾ��ͬ�Ȼ��Ȳ�ѯ
		
		DBFieldInfoBean fieldInfoBean = new DBFieldInfoBean();
		ClientModuleBean moduleBean = new ClientModuleBean();
		ClientModuleViewBean moduleViewBean = new ClientModuleViewBean();
		String dbTableName = tableName;//��ѯö��sqlʱ��Ҫ�õ���tableName
		//������ģ��id,dbTableName���ݲ�ͬģ���ȡ
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
			xName ="�·�";
		}
		
		if("quarter".equals(unit)){
			xName = "����";
		}else if("month".equals(unit)){
			xName = "�·�";
		}else if("week".equals(unit)){
			xName = "����";
		}else if("day".equals(unit)){
			xName = "����";
		}
		
		String yName = "����";
		if(sumFieldName!=null && !"".equals(sumFieldName)){
			yName = "���";
		}
		
		String chartSwfName = "MSColumn3D";
		if(searchSwfName !=null && !"".equals(searchSwfName) ){
			chartSwfName = searchSwfName;
		}
		
		String chart ="";//ͳ��ͼ��Ϣ
		
		if("true".equals(isAround)){
			//����ͳ��
			String startTime = getParameter("startTime", request);//��ʼʱ��
			String endTime = getParameter("endTime", request);//����ʱ��
			
			//Ĭ�Ͽ�ʼʱ��Ϊ�����һ��
			if(startTime ==null || "".equals(startTime)){
				startTime = DateUtil.getCurrentYearFirst();
			}
			//Ĭ�Ͻ���ʱ��Ϊ����
			if(endTime ==null || "".equals(endTime)){
				endTime = DateUtil.getNowTime("yyyy-MM-dd");
			}
			//Ĭ�ϵ�λδ�·�
			if(unit ==null || "".equals(unit)){
				unit = "month";
			}
			
			String[] aroundInfo = new String[3];
			HashMap<String,String> aroundMap = new HashMap<String, String>();//����Map,��ʾ��ǰ
			aroundInfo = this.getAroundInfo(tableName, fieldName, startTime, endTime, unit, sumFieldName,secondFieldName,dbTableName,fieldInfoBean,moduleViewBean,type,request);
			Result rs = mgt.queryCompareInfo(aroundInfo[1], aroundInfo[0], fieldName,secondFieldName, sumFieldName,"",unit,true);
			
			if(secondFieldName !=null && !"".equals(secondFieldName)){
				chartSwfName = "MSColumn3D";
				HashMap<String, HashMap<String,String>> mulFieldsMap = (HashMap<String, HashMap<String,String>>)rs.retVal;
				rs = mgt.queryFieldCount(aroundInfo[2], secondFieldName, null, sumFieldName);//����ö��sql�����ö����Ϣ
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
					//��ѯʧ��
					EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
				
			}else{
				chartSwfName = "Column3D";
				double avg = 0;//��ƽ����
				aroundMap = (HashMap<String,String>)rs.retVal;
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					if(aroundMap.size()==0){
						request.setAttribute("count", 0);	
					}else{
						/*
						List dataList = new ArrayList();
						//String setName = "";//chartͼ������
						//chart = "<graph caption='"+captionName+"' xAxisName='"+xName+"' yAxisName='"+yName+"' baseFontSize='12' rotateYAxisName='1'> ";
						int i=1;
						String showName ="";
						for(String str : aroundInfo[0].split(",")){
							HashMap map = new HashMap();
							if("week".equals(unit)){
								showName = "��"+i+"��";
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
							String setName = "";//chartͼ������
							chart = "<graph caption='"+captionName+"' xAxisName='"+xName+"' yAxisName='"+yName+"' baseFontSize='12' rotateYAxisName='1' formatNumberScale='0'";
							if(sumFieldName ==null || "".equals(sumFieldName)){
								chart += " decimalPrecision='0' ";  
							}
							chart +=" >";
							int i=1;
							String showName ="";
							for(String str : aroundInfo[0].split(",")){
								if("week".equals(unit)){
									showName = "��"+i+"��";
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
					//��ѯʧ��
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
			//ͬ��ͳ��
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
			
			//Ĭ��ͬ�Ƚ׶�����
			if(phase ==null || "".equals(phase)){
				phase = "year";
			}
			
			//Ĭ��ͬ�ȵ�λ����
			if(unit ==null || "".equals(unit)){
				unit = "month";
			}
			
			
			//��ȡͬ����һ�ڵ�����
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
			
			
			HashMap<String,String> nowInfoMap = new HashMap<String, String>();//ͬ��Map,��ʾ��ǰ
			HashMap<String,String> prevInfoMap = new HashMap<String, String>();//ͬ��Map,��ʾ�ϴ�
			
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
						//����׶�:���� ��λ:�� �� �׶���:�� ��λ���� ȡ����ʱ��map����
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
					//��ѯʧ��
					EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}else{
				//��ѯʧ��
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
	 * ͨ�õ���Ȩ����ͻ�������ѯ
	 * @param moduleViewBean
	 * @param login
	 * @param request
	 * @return
	 */
	private String getCheckBillScope(ClientModuleViewBean moduleViewBean,String tableName,LoginBean login,DBFieldInfoBean fieldInfoBean,String secondFieldName,String type,HttpServletRequest request){
		HashMap<String, String> paramMap = new HashMap<String, String>();//��Ų�����MAP
		String userId = login.getId();
		StringBuilder condition = new StringBuilder();
		String tempTableName=tableName;
		if("CRMSaleContractDet".equals(tableName)){
			tempTableName = "CRMSaleContract";
		}
		String dbTableName = request.getParameter("dbTableName");//��ѯö�ٱ��еı���
		String emptyIds = "";//���ö������Ϊ�յ�ֵ
		String selectedEnumIds = "";//���ҳ��ѡ���ö��ID
		String scopeEmployeeName = "employeeId";//Ȩ��Ĭ�ϲ�ѯ��ԱfieldName
		String scopeDeptCode = "";//���˿ͻ����ϱ��沿��Ȩ���ַ���
		if(!"1".equals(userId)){
			/*�鿴ĳ�ֶ�ֵ����*/
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
			
			//��ȡȨ��·��
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
									//���ǿͻ����ϵ�ֱ����departmentCode like
									condition.append(" or ").append(tempTableName).append(".departmentCode like '").append(strId).append("%' ") ;
								}else{
									//�Ѳ������д������
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
					
					//������˿ͻ�����,�������Ĳ���Ȩ��
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

		String removeSearch = getParameter("removeSearch", request);//���������ʶ
		//true��ʾ�������
		if(!"true".equals(removeSearch)){
			if(!"weekMonthDetail".equals(type)){
				//�ͻ����ϱ����ѯ����
				if(tableName.indexOf("CRMClientInfo") != -1){
					condition.append(" and CRMClientInfo.moduleId='").append(moduleViewBean.getModuleId()).append("' ") ;
					if("public".equals(paramMap.get("isPublic"))){
						condition.append(" and status = '1' ") ;
					}else{
						condition.append(" and status != '1' ") ;
					}
					
					//ģ����ѯ
					for(String str : moduleViewBean.getKeyFields().split(",")){
						String fieldValue = getParameter(str, request);
						if(fieldValue != null && !"".equals(fieldValue)){
							condition.append(" and " + str +" like '%" + fieldValue +"%' ");
							paramMap.put(str, fieldValue);
						}
					}
					
					//������ѯ
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
										//��ֵΪemptyҪ����������ö��ֵ���޶����Ե�ѡ��ֵ
										emptyIds = this.getEmptyVal(str, tableName, dbTableName);
									}else{
										enumIds += "'"+enumVal+"',";
									}
									selectedEnumIds +=enumVal+",";//������ѡ��id
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
						//Ĭ�ϵ�һ�ν�������ʱ�����鱾�������
						if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6 || "createTime".equals(secondFieldName)){
							if(createTimeStart == null){
								createTimeStart = DateUtil.getCurrentYearFirst();//Ĭ�ϻ�ñ����һ��
							}
							if(createTimeEnd == null){
								createTimeEnd = DateUtil.getNowTime("yyyy-MM-dd");//����ʱ��;
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
					
					//������ѯ
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
					
					//��ҵ��ѯ
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
						request.setAttribute("hasCondition", "true");//��ʾ�в�ѯ����
					}
				}else{
					String searchTimeName = getParameter("searchTimeName", request) ;//����ʱ���fieldName
					String searchTimeStart = getParameter("searchTimeStart", request) ;//������ʼʱ��
					String searchTimeEnd = getParameter("searchTimeEnd", request) ;//��������ʱ��
					
					//ö��ֵ
					DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName);
					for(DBFieldInfoBean fieldBean : tableInfo.getFieldInfos()){
						if(fieldBean.getInputType() == 1){
							selectedEnumIds = "";
							String enumIds = "";
							String[] enumVal = request.getParameterValues(fieldBean.getFieldName());
							if(enumVal!=null && enumVal.length>0){
								for(String value : enumVal){
									if("empty".equals(value)){
										//��ֵΪemptyҪ����������ö��ֵ���޶����Ե�ѡ��ֵ
										emptyIds = this.getEmptyVal(fieldBean.getFieldName(), tableName, dbTableName);
									}else{
										enumIds += "'"+value+"',";
									}
									selectedEnumIds += value+",";//������ѡ��ID
								}
								enumIds += emptyIds;
								if(enumIds.endsWith(",")){
									enumIds = enumIds.substring(0,enumIds.length()-1);
								}
								condition.append(" and isnull(").append(tableName).append(".").append(fieldBean.getFieldName()).append(",'') in (")
								.append(enumIds).append(") "); 
								
								request.setAttribute("hasCondition", "true");//��ʾ�в�ѯ����
							}
							paramMap.put(fieldBean.getFieldName(),selectedEnumIds);
						}
					}
					
					if(!"compare".equals(type)){
						//Ĭ�ϵ�һ�ν�������ʱ�����鱾�������
						if(fieldInfoBean.getFieldType() == 5 || fieldInfoBean.getFieldType() == 6  || "SignUpDate".equals(secondFieldName) || "BillDate".equals(secondFieldName) || "createTime".equals(secondFieldName)){
							if(searchTimeStart == null){
								searchTimeStart = DateUtil.getCurrentYearFirst();//Ĭ�ϻ�ñ����һ��
							}
							if(searchTimeEnd == null){
								searchTimeEnd = DateUtil.getNowTime("yyyy-MM-dd");//����ʱ��;
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
						request.setAttribute("hasCondition", "true");//��ʾ�в�ѯ����
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
						request.setAttribute("hasCondition", "true");//��ʾ�в�ѯ����
					}
					paramMap.put("searchTimeStart",searchTimeStart);
					paramMap.put("searchTimeEnd",searchTimeEnd);
					request.setAttribute("searchTimeStart", searchTimeStart);
					request.setAttribute("searchTimeEnd", searchTimeEnd);
					
				}
			}
			//������Ա
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
				request.setAttribute("hasCondition", "true");//��ʾ�в�ѯ����
			}
			
			//���Ų�ѯ
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
				request.setAttribute("hasCondition", "true");//��ʾ�в�ѯ����
			}
			
			//�ͻ���ѯ
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
				
				
//				//�����Ʒ���map��,KEY��clientId
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
				Result rs = clientMgt.findClients(ids);//����ID���ҿͻ���Ϣ
				request.setAttribute("clientInfo", rs.retVal);
				request.setAttribute("clientIds", clientIds);
				request.setAttribute("hasCondition", "true");//��ʾ�в�ѯ����
			}
			
			//��������ö��������ѯ
			if("compare".equals(type) && secondFieldName !=null && !"".equals(secondFieldName)){
				String[] classes = request.getParameterValues(secondFieldName+"_isAround");
				if(classes !=null && classes.length>0){
					selectedEnumIds ="";
					String classIds = "";
					String ids = "";
					for(String str : classes){
						if("empty".equals(str)){
							//��ֵΪemptyҪ����������ö��ֵ���޶����Ե�ѡ��ֵ
							emptyIds = this.getEmptyVal(secondFieldName, tableName, dbTableName);
						}else{
							classIds += "'" + str +"',";
						}
						selectedEnumIds += str+",";//������ѡ��id
					}
					classIds += emptyIds;
					if(classIds.endsWith(",")){
						classIds = classIds.substring(0,classIds.length()-1);
					}
					condition.append(" and isnull(").append(secondFieldName).append(",'') in (").append(classIds).append(") "); 
					request.setAttribute("classes",selectedEnumIds);
				}
			}
			request.setAttribute("paramMap", paramMap);//����MAP
			request.setAttribute("condition", condition);//����
		}else{
			//�ͻ����ϱ����ѯ����
			if(tableName.indexOf("CRMClientInfo") != -1){
				//����ͳ�Ʊ�����Ҫ����ģ��
				if(!"weekMonthDetail".equals(type)){
					condition.append(" and CRMClientInfo.moduleId='").append(moduleViewBean.getModuleId()).append("' ") ;
				}
				if("public".equals(paramMap.get("isPublic"))){
					condition.append(" and status = '1' ") ;
				}else{
					condition.append(" and status != '1' ") ;
				}
			}
			request.setAttribute("condition", condition);//����
		}
		return condition.toString();
	}
	
	/**
	 * ���ͳ��ͼ
	 * @param dataList
	 * @param captionName
	 * @param xName
	 * @param yAxisName
	 * @param isCount
	 * @return
	 */
	public String getChart(List<String[]> dataList,String captionName,String xName,String yAxisName,String sumFieldName,String ichartName,HttpServletRequest request){
		/*
		//��װchart��ʽ
		String setName = "";//chartͼ������
		//String chart = "<graph caption='"+captionName+"' xAxisName='"+xName+"' yAxisName='"+yAxisName+"' baseFontSize='12' rotateYAxisName='1' decimals='0' > ";
		String name = "";
		List list = new ArrayList();
		List labelList = new ArrayList();
		for(int i=0;i<dataList.size();i++){
			name = dataList.get(i)[1];
			if(name == null || "".equals(name)){
				name="��";
			}
			if("LineBasic2D".equals(ichartName)){
				list.add(GlobalsTool.dealDoubleDigits(dataList.get(i)[2], sumFieldName));
				labelList.add(name);
			}else{
				HashMap map = new HashMap();
//			if(dataList.get(i)[1] ==null || "".equals(dataList.get(i)[1])){
//				setName = "��";
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
		
		
//		��װchart��ʽ
		String setName = "";//chartͼ������
		String chart = "<graph caption='"+captionName+"' xAxisName='"+xName+"' yAxisName='"+yAxisName+"' baseFontSize='12'  decimals='0' formatNumberScale='0' ";
		if(sumFieldName ==null || "".equals(sumFieldName)){
			chart += " decimalPrecision='0' ";  
		}
		chart +=" >";
		for(int i=0;i<dataList.size();i++){
			if(dataList.get(i)[1] ==null || "".equals(dataList.get(i)[1])){
				setName = "��";
			}else{
				setName = dataList.get(i)[1];
			}
			chart += "<set name='"+setName+"' value='"+GlobalsTool.dealDoubleDigits(dataList.get(i)[2], sumFieldName)+"' color='"+this.getRandomColor(i)+"' />";
		}
		chart+= "</graph>";
		
		return chart;
	}
	
	/**
	 * ��ȡmsͼ
	 * @param msDataMap ����
	 * @param enumList ö��list
	 * @param request
	 * @param captionName caption����
	 * @param xName ������
	 * @param yName ������
	 * @param secondFieldName �ڶ����ֶ�����
	 * @return
	 */
	public String getMSChart(LinkedHashMap<String,List<String[]>> msDataMap,List<String[]> headList,HttpServletRequest request,String captionName,String xName,String yName,String secondFieldName){
		/*List<List<String>> msDatas = new ArrayList<List<String>>();//ҳ����ʾÿ�����ݵļ���
		HashMap<String,String> enumMap = new HashMap<String, String>();//ҳ����ʾ�����MAP
		String sumFieldName = getParameter("sumFieldName",request);
		String showName = "";
		String chart = "<graph xaxisname='"+xName+"' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12'>";
		//ͷ��
		chart +="<categories font='Arial' fontSize='12' fontColor='000000' >";
		
		
		
		
		List labelList = new ArrayList();
		
		//ͳ��ͼ����ѭ��
		for(int i=0;i<headList.size();i++){
			enumMap.put(headList.get(i)[1], headList.get(i)[2]);			
		}
		
		Set keys = msDataMap.keySet();
		if(keys != null) {
			Iterator iterator = keys.iterator();
			while(iterator.hasNext()) {
				Object key = iterator.next();//��KEY
				String categoryName = (String)key;
				if(categoryName == null || "".equals(categoryName)){
					categoryName = "��";
				}
				chart +="<category name='"+categoryName+"' fontSize='12' />";
				labelList.add(categoryName);
			}
			chart += "</categories>";
			
			
			iterator = keys.iterator();
			while(iterator.hasNext()) {
				double[] perEmpArr = new double[headList.size()];//ÿ�е�����
				double count = 0.0;//ͳ������
				List<String> datasList = new ArrayList<String>();//���ҳ����ʾÿ�е�����
				Object key = iterator.next();//��KEY
				List<String[]> perList = msDataMap.get(key);//����KEY��ȡ��������
				showName =(String)key;
				showName = showName == null ? "��" : showName;
				datasList.add(showName);
				
				//ѭ��ÿ����������������LIST��,�еķ���perEmpArr��
				for(String[] strArr : perList){
					for(int i=0;i<headList.size();i++){
						if(headList.get(i)[0].equals(strArr[1])){
							perEmpArr[i] = Double.parseDouble(strArr[2]);
							break;
						}
					}
				}
				
				//����ÿ���ܺ�
				for(int i=0;i<perEmpArr.length;i++){
					count +=perEmpArr[i];
					datasList.add(perEmpArr[i] +"");
				}
				datasList.add(count+"");
				msDatas.add(datasList);
				
			}
			
			//��װdataset��ʾ����
			int i=1;//��ʵλ��,0��secondFieldName
			String headName = "";
			
			
			
			List list = new ArrayList();
			for(String[] headArr : headList){
				HashMap map = new HashMap();
				List tempList = new ArrayList();
				if(headArr[1] ==null || "".equals(headArr[1])){
					headName = "��";
				}else{
					headName = headArr[1];
				}
				chart +="<dataset seriesname='"+headName+"' color='"+this.getRandomColor(i)+"'>";
				map.put("name",headName);
				map.put("color", "#"+this.getRandomColor(i));
				//ѭ��msDatasҳ����ʾ��ÿһ��
				for(List<String> perList : msDatas){
					//��get(1)��ʼȡ��Ӧ����
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
		List<List<String>> msDatas = new ArrayList<List<String>>();//ҳ����ʾÿ�����ݵļ���
		HashMap<String,String> enumMap = new HashMap<String, String>();//ҳ����ʾ�����MAP
		String showName = "";
		String chart = "<graph xaxisname='"+xName+"' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12' formatNumberScale='0' >";
		//ͷ��
		chart +="<categories font='Arial' fontSize='12' fontColor='000000' >";
		//ͳ��ͼ����ѭ��
		for(int i=0;i<headList.size();i++){
			enumMap.put(headList.get(i)[1], headList.get(i)[2]);			
		}
		
		Set keys = msDataMap.keySet();
		if(keys != null) {
			Iterator iterator = keys.iterator();
			while(iterator.hasNext()) {
				Object key = iterator.next();//��KEY
				String categoryName = (String)key;
				if(categoryName == null || "".equals(categoryName)){
					categoryName = "��";
				}
				chart +="<category name='"+categoryName+"' fontSize='12' />";
			}
			chart += "</categories>";
			
			
			iterator = keys.iterator();
			while(iterator.hasNext()) {
				double[] perEmpArr = new double[headList.size()];//ÿ�е�����
				double count = 0.0;//ͳ������
				List<String> datasList = new ArrayList<String>();//���ҳ����ʾÿ�е�����
				Object key = iterator.next();//��KEY
				List<String[]> perList = msDataMap.get(key);//����KEY��ȡ��������
				showName =(String)key;
				if(showName == null || "".equals(showName) || "null".equals(showName)){
					showName = "��";
				}
				//showName = showName == null ? "��" : showName;
				datasList.add(showName);
				
				//ѭ��ÿ����������������LIST��,�еķ���perEmpArr��
				for(String[] strArr : perList){
					for(int i=0;i<headList.size();i++){
						if(headList.get(i)[0].equals(strArr[1])){
							perEmpArr[i] = Double.parseDouble(strArr[2]);
							break;
						}
					}
				}
				
				//����ÿ���ܺ�
				for(int i=0;i<perEmpArr.length;i++){
					count +=perEmpArr[i];
					datasList.add(perEmpArr[i] +"");
				}
				datasList.add(count+"");
				msDatas.add(datasList);
				
			}
			
			//��װdataset��ʾ����
			int i=1;//��ʵλ��,0��secondFieldName
			String headName = "";
			for(String[] headArr : headList){
				if(headArr[1] ==null || "".equals(headArr[1])){
					headName = "��";
				}else{
					headName = headArr[1];
				}
				chart +="<dataset seriesname='"+headName+"' color='"+this.getRandomColor(i)+"'>";
				
				//ѭ��msDatasҳ����ʾ��ÿһ��
				for(List<String> perList : msDatas){
					//��get(1)��ʼȡ��Ӧ����
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
	 * ���ͬ��ͳ��ͼ
	 * @param prevMap ȥ������
	 * @param nowMap ��������
	 * @param timeScope ʱ���
	 * @param xName ����������
	 * @param yName ����������
	 * @param phase ͬ�Ƚ׶�
	 * @param unit ͬ�ȵ�λ
	 * @param captionName ͷ����
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
		
		String prevSeriesname = "ȥ��";
		String seriesname = "����";
		if("quarter".equals(phase)){
			prevSeriesname = "�ϼ���";
			seriesname = "������";
		}else if("month".equals(phase)){
			prevSeriesname = "����";
			seriesname = "����";
		}else if("week".equals(phase)){
			prevSeriesname = "����";
			seriesname = "����";
		}
		
		String prevDataSet = "<dataset seriesname='"+prevSeriesname+"' color='"+this.getRandomColor(0)+"'>";
		String nowDataSet = "<dataset seriesname='"+seriesname+"' color='"+this.getRandomColor(1)+"'>";
		
		String prevVal = "";//�����һ����ʾ����
		String nowVal="";//��ű�����ʾ����
		int i =1;//�����ۼ�
		String tempCount = "";
		for(String str : timeScope.split(",")){
			tempCount = i+"";
			if("quarter".equals(unit)){
				chart += "<category name='��"+i+"��' /> ";
				labelList.add("��"+i+"��");
			}else if("month".equals(unit)){
				if("quarter".equals(phase)){
					chart += "<category name='"+prevScope.get(tempCount)+"/"+str+"��' /> ";
					labelList.add(prevScope.get(tempCount)+"/"+str+"��");
				}else{
					chart += "<category name='"+str+"��' /> ";
					labelList.add(str+"��");
				}
			}else if("week".equals(unit)){
				chart += "<category name='��"+i+"��' /> ";
				labelList.add("��"+i+"��");
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
		
		String prevSeriesname = "ȥ��";
		String seriesname = "����";
		if("quarter".equals(phase)){
			prevSeriesname = "�ϼ���";
			seriesname = "������";
		}else if("month".equals(phase)){
			prevSeriesname = "����";
			seriesname = "����";
		}else if("week".equals(phase)){
			prevSeriesname = "����";
			seriesname = "����";
		}
		
		String prevDataSet = "<dataset seriesname='"+prevSeriesname+"' color='"+this.getRandomColor(0)+"'>";
		String nowDataSet = "<dataset seriesname='"+seriesname+"' color='"+this.getRandomColor(1)+"'>";
		
		String prevVal = "";//�����һ����ʾ����
		String nowVal="";//��ű�����ʾ����
		int i =1;//�����ۼ�
		String tempCount = "";
		for(String str : timeScope.split(",")){
			tempCount = i+"";
			if("quarter".equals(unit)){
				chart += "<category name='��"+i+"��' /> ";
			}else if("month".equals(unit)){
				if("quarter".equals(phase)){
					chart += "<category name='"+prevScope.get(tempCount)+"/"+str+"��' /> ";
				}else{
					chart += "<category name='"+str+"��' /> ";
				}
			}else if("week".equals(unit)){
				chart += "<category name='��"+i+"��' /> ";
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
	 * ��ȡSum����count���
	 * @param sumFieldName �ж��Ƿ��ۼ� 
	 * @param tableName ����
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
	 * �漴��ȡ18����ɫ
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
	 * ���ͬ�������ʱ��η�Χ
	 * @param tableName ����
	 * @param fieldName �ֶ���
	 * @param phase ��ʷ�׶�
	 * @param unit ��λ
	 * @return
	 */
	public String[] getCompareInfo(String tableName,String fieldName,String phase,String unit,String searchTime,String sumFieldName,DBFieldInfoBean fieldInfoBean,ClientModuleViewBean moduleViewBean,String type,HttpServletRequest request){
		StringBuilder sql = new StringBuilder();
		String timeScope = "";//��ѯ������ʱ���
		sql.append("SELECT ");
		String selectStr = "";//select����
		String[] compareInfo = new String[2];//���ص���Ϣ

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar searchDate = Calendar.getInstance();
		try {
			searchDate.setTime(sdf.parse(searchTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int tempYear = searchDate.get(Calendar.YEAR);//��ѯʱ������
		int tempMonth = searchDate.get(Calendar.MONTH)+1;//��ѯʱ����·�
		int tempDay = searchDate.get(Calendar.DATE);//��ѯʱ��ļ���
		
		
		//��ȡ����ʱ���
		if("month".equals(unit)){
			//��λΪ�·�
			selectStr = "substring("+fieldName+",6,2)";
			if("year".equals(phase)){
				timeScope = "01,02,03,04,05,06,07,08,09,10,11,12";//12����
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
			//����
			//String year = searchTime.substring(0,4);
			selectStr = "datediff(quarter, "+fieldName+",'"+tempYear+"')";
			timeScope = "0,-1,-2,-3";
		}else if("week".equals(unit)){
			String seasonDate = "";//ͳ��phase�ĵ�һ�������һ��
			if("month".equals(phase)){
				 selectStr ="datediff(week,"+fieldName+",'"+tempYear+"-"+tempMonth+"-01')";//��������Ϊ��ѯʱ���1�š�����ͳ��
				 Calendar selTime = Calendar.getInstance();
				 selTime.set(tempYear, tempMonth-1,01); //Calendar���´�0-11��
				 seasonDate = DateUtil.getFirstDayOfMonth(tempYear, tempMonth) + ";" + DateUtil.getLastDateMonth(tempYear, tempMonth);//��õ��µĵ�һ�������һ����;����
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
				seasonDate = DateUtil.getSeasonStartDayAndLastDay(tempYear, tempMonth);//��ñ����ĵ�һ�������һ����;����
			}
			
			//��ȡ�ж�����
			int weekCount = 0;
			weekCount = DateUtil.getWeekCount(seasonDate.split(";")[0], seasonDate.split(";")[1]);//��ȡʱ��ε�����Ŀ
			int weekNo = 0;
			for(int i=0;i<weekCount;i++){
				timeScope += weekNo +",";
				weekNo = weekNo-1;
			}
		}
		
		LoginBean loginBean = getLoginBean(request);
		//��ѯ��SQL���
		sql.append(selectStr).append(" as ").append(fieldName).append(",").append(this.getSumOrCountSql(sumFieldName, tableName)).append(" WHERE 1=1 ");
		sql.append(this.getCheckBillScope(moduleViewBean,tableName,loginBean,fieldInfoBean,"",type,request)).append(" and ");
		
		
		
		if("day".equals(unit)){
			//����ͬ�ȵ�λ:��
			sql.append(" substring(").append(fieldName).append(",1,10) ").append(" between '");
			if("month".equals(phase)){
				searchDate.set(Calendar.DATE, 1);
				String monthStartDay = sdf.format(searchDate.getTime());
				String monthEndDay = DateUtil.getLastDateMonth(tempYear, tempMonth);//��ȡ�������һ��
				long count = DateUtil.getTwoDay(monthEndDay,monthStartDay )+1;//�������������
				//��ȡ������ʱ���
				for(int i=0;i<count;i++){
					//String nowData = sdf.format(searchDate.getTime());
					//timeScope += nowData.substring(nowData.indexOf("-")+1) +",";
					timeScope += searchDate.get(Calendar.DATE)+",";
					searchDate.add(Calendar.DATE, 1);//������1
				}
				sql.append(monthStartDay).append("' and '").append(monthEndDay).append("'");
			}else{
				SimpleDateFormat df = new SimpleDateFormat("MM-dd");  
				sql.append(sdf.format(searchDate.getTime()));
				//��ѯʱ����ѭ��һ�ܵ�ʱ��
				for(int i=0;i<7;i++){
					timeScope +=  df.format(searchDate.getTime())+",";
					searchDate.add(Calendar.DATE, 1);//������1
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
	 * ��ȡͳ������sql���,����ʱ�����Ϣ,ö��SQL���
	 * @param tableName ����
	 * @param fieldName �ֶ�����
	 * @param startTime ��ʼʱ��
	 * @param endTime ����ʱ��
	 * @param unit ��λ
	 * @param sumFieldName sum(sumFieldName)�ϼ��ֶ���
	 * @param secondFieldName �������ֶ���
	 * @param dbTableName ö�ٲ�ѯtableName
	 * @return
	 */
	public String[] getAroundInfo(String tableName,String fieldName,String startTime,String endTime,String unit,String sumFieldName,String secondFieldName,String dbTableName,DBFieldInfoBean fieldInfoBean,ClientModuleViewBean moduleViewBean,String type,HttpServletRequest request){
		String[] aroundInfo = new String[3];
		StringBuilder sql = new StringBuilder();//ͳ���������ݵ�sql���
		StringBuilder secondSql = new StringBuilder();//ö�ٵ�sql���
		String timeScope="";//���е�ʱ���
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String tempCondition = "";//select��group by�� order by����
		String quarterCondition = "";//��ѯ����ʱ��Ҫ���һ��select����
		String order = "";//�������
		LoginBean login = getLoginBean(request);
		sql.append("SELECT ");
		if("month".equals(unit)){
			SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
			int diffMonthCount = DateUtil.compareDate(startTime, endTime, 1)+1;//�������ʱ��֮�������·���,+1���Ͻ���ʱ����Ǹ�����
			Calendar begin = Calendar.getInstance();
			try {
				begin.setTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//ѭ����ò�ѯʱ��ε������·�
			for(int i=0;i<diffMonthCount;i++){
				timeScope += sdfMonth.format(begin.getTime()) +",";
				begin.add(Calendar.MONTH, 1);// ��һ���£���Ϊ���µ�1��
			}
			
			tempCondition = "substring("+fieldName+",1,7) ";
		}else if("quarter".equals(unit)){
			//��õ�ǰʱ��ļ�����Ϣ
			String startTimeSeason = DateUtil.getSeasonInfo(startTime);
			String endTimeSeason = DateUtil.getSeasonInfo(endTime);
			
			timeScope = startTimeSeason +",";//��ż���ʱ���
			String tempSeasonInfo=startTimeSeason;//��ʱ��ſ�ʼʱ��ļ�����Ϣ����:2012��Q1
			int quarterNo=Integer.parseInt(startTimeSeason.substring(startTimeSeason.length()-1));//��ÿ�ʼʱ��ļ��ȱ��
			int quarterYear=Integer.parseInt(startTime.substring(0,4));//��ÿ�ʼʱ������
			while(true){
				quarterNo ++;//����һ���ȿ�ʼ
				//����Ƚ���ѭ��
				if(endTimeSeason.equals(tempSeasonInfo)){
					break;
				}
				//��%4==0��ʾQ4
				if(quarterNo%4 == 0){
					tempSeasonInfo = quarterYear+quarterNo/4-1 +"��Q4";
				}else{
					tempSeasonInfo = quarterYear + quarterNo/4 +"��Q"+quarterNo%4;
				}
				timeScope +=tempSeasonInfo+",";
			}
			
			//��ѯʱ����ݿ�ʼʱ��1�¿�ʼ��ѯ,���ܻ��׼ȷ�ļ���
			Calendar beginTime = Calendar.getInstance();
			try {
				beginTime.setTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			beginTime.set(Calendar.MONTH, 0);//����Ϊ��ʼ������ݵ�1��
			tempCondition = "datediff(quarter, "+fieldName+",'"+sdf.format(beginTime.getTime())+"') ";
			quarterCondition = ",substring("+fieldName+",1,4) ";//��ѯ���ȼӸ�sel����
			order = "DESC";//��������
		}else if("week".equals(unit)){
			int weekCount = DateUtil.getWeekCount(startTime, endTime);//���ʱ���֮ǰ�ж�����
			int weekNo = 0;//��0��ʼ
			for(int i=0;i<weekCount;i++){
				timeScope += weekNo +",";
				weekNo = weekNo-1;
			}
			tempCondition = "datediff(week, "+fieldName+",'"+startTime+"') ";
			order = "DESC";
		}else{
			long count = DateUtil.getTwoDay(startTime,endTime)+1;//��ȡ���������ڼ������
			if(!startTime.equals(endTime)){
				count = Math.abs(count)+2;
			}
			Calendar day = Calendar.getInstance();//��ʼʱ��
			try {
				day.setTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//ѭ�������������
			for(int i=0;i<count;i++){
				timeScope += sdf.format(day.getTime()) +",";
				day.add(Calendar.DATE, 1);//������1
			}
			tempCondition = "substring("+fieldName+",1,10) ";
		}
		
		
		//SQL���
		sql.append(tempCondition).append(" as ").append(fieldName).append(quarterCondition);
		
		//����λΪ����ʱ
		if("quarter".equals(unit)){
			sql.append(" as year ");
		}

		sql.append(",");
		
		String enumJoinSql = "";
		//���ж�������ѯʱ��
		if(secondFieldName !=null && !"".equals(secondFieldName)){
			enumJoinSql = this.getEnumjoinSql(secondFieldName, tableName, dbTableName);
			sql.append("isnull(").append(tableName).append(".").append(secondFieldName).append(",'') as ").append(secondFieldName).append(",tbllanguage.zh_CN as zh_CN,");
			
			//�������Ͳ�ѯ,��ѯ��ö�ٵ����
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
		
		aroundInfo[0]=timeScope;//����ʱ���
		aroundInfo[1]=sql.toString();//ͳ����������sql���
		aroundInfo[2]=secondSql.toString();//ö��sql���
		return aroundInfo;
	}
	
	
	/**
	 * ��ȡ����MSͳ��ͼ�������ܺ�Map
	 * @param mulFieldsMap ������������MAP
	 * @param enumList ����ö�ٵ�list
	 * @param timeScope ʱ���
	 * @param unit ���ȵ�λ
	 * @param xName x������
	 * @param yName y������
	 * @param captionName ͳ��ͼͷ����
	 * @param request
	 * @return
	 */
	public String getAroundMsChart(HashMap<String, HashMap<String,String>> mulFieldsMap,List<String[]> enumList,String timeScope,String unit,String yName,String captionName,HttpServletRequest request){
		/*List labelList = new ArrayList();
		List dataList = new ArrayList();
		//ͷ��
		String chart = "<graph xaxisname='����' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12'>";
		chart +="<categories font='Arial' fontSize='12' fontColor='000000'>";
		
		HashMap<String,String> totalMap = new HashMap<String, String>();//���ÿ�����͵��ܺ�,��ҪΪ��ҳ����ʾ
		
		double count = 0;//���
		String keyVal = "";//ö�ٵ�ֵ
		String enumName ="";
		for(String[] enumInfo : enumList){
			if(enumInfo[1] == null || "".equals(enumInfo[1])){
				enumName = "��";
			}else{
				enumName = enumInfo[1];
			}
			chart +="<category name='"+enumName+"' />";
			labelList.add(enumName);
			for(String time : timeScope.split(",")){
				HashMap<String,String> map = mulFieldsMap.get(time);//����ʱ��λ�ȡmap
				if(map == null){
					keyVal = "0";//���mapΪnull,��ʾ���ʱ����û���κ�ö�ٵ�ֵ,Ĭ�ϸ�0
				}else{
					keyVal = map.get(enumInfo[0]) == null ? "0" : mulFieldsMap.get(time).get(enumInfo[0]);//������ö��key��ֵΪnull,Ĭ�ϸ�0
				}
				count += Double.parseDouble(keyVal);//ͳ�����͵��ܺ�
			}
			totalMap.put(enumInfo[0],count+"");
			count =0;
		}
		request.setAttribute("labelList",gson.toJson(labelList));
		request.setAttribute("totalMap",totalMap);
		request.setAttribute("timeScopeSize",timeScope.split(",").length);//ʱ��εĸ���,����ҳ����ƽ��ֵ
		chart += "</categories>";

		int i=1;
		String seriesname = "";//dataset��seriesname����
		for(String time : timeScope.split(",")){
			List list = new ArrayList();
			HashMap iChartMap = new HashMap();
			double rowCount = 0;
			if("week".equals(unit)){
				seriesname = "��"+i+"��";//��λΪ��,�ӵ�һ�ܿ�ʼ��
			}else{
				seriesname = time;
			}
			chart +="<dataset seriesname='"+seriesname+"' color='"+this.getRandomColor(i)+"'>";
			//ѭ����ȡdataset��ʾ����
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
		
//		ͷ��
		String chart = "<graph xaxisname='����' yaxisname='"+yName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0'  numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+captionName+"' baseFontSize='12' formatNumberScale='0'>";
		chart +="<categories font='Arial' fontSize='12' fontColor='000000'>";
		
		HashMap<String,String> totalMap = new HashMap<String, String>();//���ÿ�����͵��ܺ�,��ҪΪ��ҳ����ʾ
		
		double count = 0;//���
		String keyVal = "";//ö�ٵ�ֵ
		String enumName ="";
		for(String[] enumInfo : enumList){
			if(enumInfo[1] == null || "".equals(enumInfo[1])){
				enumName = "��";
			}else{
				enumName = enumInfo[1];
			}
			chart +="<category name='"+enumName+"' />";
			
			for(String time : timeScope.split(",")){
				HashMap<String,String> map = mulFieldsMap.get(time);//����ʱ��λ�ȡmap
				if(map == null){
					keyVal = "0";//���mapΪnull,��ʾ���ʱ����û���κ�ö�ٵ�ֵ,Ĭ�ϸ�0
				}else{
					keyVal = map.get(enumInfo[0]) == null ? "0" : mulFieldsMap.get(time).get(enumInfo[0]);//������ö��key��ֵΪnull,Ĭ�ϸ�0
				}
				count += Double.parseDouble(keyVal);//ͳ�����͵��ܺ�
			}
			totalMap.put(enumInfo[0],count+"");
			count =0;
		}
		request.setAttribute("totalMap",totalMap);
		request.setAttribute("timeScopeSize",timeScope.split(",").length);//ʱ��εĸ���,����ҳ����ƽ��ֵ
		chart += "</categories>";

		int i=1;
		String seriesname = "";//dataset��seriesname����
		for(String time : timeScope.split(",")){
			double rowCount = 0;
			if("week".equals(unit)){
				seriesname = "��"+i+"��";//��λΪ��,�ӵ�һ�ܿ�ʼ��
			}else{
				seriesname = time;
			}
			chart +="<dataset seriesname='"+seriesname+"' color='"+this.getRandomColor(i)+"'>";
			//ѭ����ȡdataset��ʾ����
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
	 * �첽����Ǽ����Ƿ����ʱ���
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
		//��õ�ǰʱ��ļ�����Ϣ
		String startTimeSeason = DateUtil.getSeasonInfo(startTime);
		String endTimeSeason = DateUtil.getSeasonInfo(endTime);
		if(startTimeSeason.equals(endTimeSeason)){
			request.setAttribute("msg","yes");//��ʾͬһ����
		}else{
			request.setAttribute("msg","no");//��ʾ��ͳһ����
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * ��ȡö������join���
	 * @param fieldName
	 * @param tableName
	 * @param dbTableName
	 * @return
	 */
	public String getEnumjoinSql(String fieldName,String tableName,String dbTableName){
		StringBuilder sql = new StringBuilder();//ö�ٵ�sql���
		sql.append(" left join tblDBEnumerationItem  on ").append(tableName).append(".").append(fieldName)
		.append("=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumId =(select id from tblDBEnumeration where enumName=(select refEnumerationName from tblDBfieldInfo where tableid=(select id from tbldbtableInfo where tablename='")
		.append(dbTableName).append("') and fieldName='")
		.append(fieldName).append("')) left join tbllanguage  on tblDBEnumerationItem.languageId=tbllanguage.id ");
		
		return sql.toString();
	}
	
	/**
	 * ��ѯö��ֵΪ��
	 * @param fieldName
	 * @param tableName
	 * @param dbTableName
	 * @return
	 */
	public String getEmptyVal(String fieldName,String tableName,String dbTableName){
		String enumValStr="";
//		ö��ֵΪ��,��Ҫ�鿴�Ƿ������ö��ֵ������Ϊ�յ�ѡ��.
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
		
		//��û��ֵĬ�ϸ�'',��Ȼ�ᱨ��
		if("".equals(enumValStr)){
			enumValStr = "''";
		}
		return enumValStr;
	}
	
	private ActionForward weekMonthQuery(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String isWeekQuery = getParameter("isWeekQuery", request);//isWeekQuery == true ��ʾ�ܲ�ѯ
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
		
		String isWeekQuery = getParameter("isWeekQuery", request);//isWeekQuery == true ��ʾ�ܲ�ѯ
		String weekStartTime = getParameter("weekStartTime", request);//�ܲ�ѯ��ʼʱ��
		String weekEndTime = getParameter("weekEndTime", request);//�ܲ�ѯ����ʱ��
		String sortName = getParameter("sortName", request);//��������
		LoginBean login = getLoginBean(request);
		String type = getParameter("type", request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		
		StringBuilder querySql = new StringBuilder();//�ܺ����
		StringBuilder countSql = new StringBuilder();//�ܺ����
		StringBuilder publicSql = new StringBuilder();//������ѯ���
		
		String searchTime = "";
		if(isWeekQuery == null || "".equals(isWeekQuery)){
			int year = Integer.parseInt(getParameter("year", request));//�²�ѯ���
			int month = Integer.parseInt(getParameter("month", request));//�²�ѯ�·�
			Calendar selDate = Calendar.getInstance();
			selDate.set(year,month-1, 1);//�·ݲ�ѯȡ��һ��
			searchTime = sdf.format(selDate.getTime());
			String endTime = DateUtil.getLastDateMonth(year, month);//��ȡ�������һ�졣��ʾ��ҳ��
			request.setAttribute("year", year);
			request.setAttribute("month", month);
			request.setAttribute("startTime",searchTime);
			request.setAttribute("endTime",endTime);
		}else{
			String weekName = getParameter("weekName",request);//�ڼ�������	
			String isSearch = getParameter("isSearch", request);
			
			//�����ύ����ת����
			if(isSearch==null || "".equals(isSearch)){
				weekName = GlobalsTool.toChinseChar(weekName);
			}
			
			//ȡ���·�
			Calendar weekDate = Calendar.getInstance();
			try {
				weekDate.setTime(sdf.parse(weekStartTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			request.setAttribute("month",weekDate.get(Calendar.MONTH)+1);
			request.setAttribute("weekName",weekName.replaceAll(" ",""));
		}

		String selcondition = " isnull(employeeId,'') ";//select��ѯ����
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
		
		//�ͻ�����sql
		publicSql.append("sum(a.clientCount) clientCount,sum(a.followUpCount) followUpCount,sum(a.contractCount) contractCount,sum(a.contractSum) contractSum,sum(a.receiveCount) receiveCount,sum(a.receiveSum) receiveSum,sum(a.feeSum) feeSum from( select isnull(createBy,'') as employeeId,count(*) as clientCount,0 as followUpCount,0 as contractCount,0 as contractSum,0 as receiveCount,0 as receiveSum,0 as feeSum from crmclientinfo where 1=1 and status != '1' ").append(this.getCheckBillScope(null, "CRMClientInfo", login, null, null, type, request));
		publicSql.append(this.WeekMonthTimeScope(isWeekQuery, "createTime", weekStartTime, weekEndTime, searchTime)).append(" group by isnull(createBy,'') union all select");
		//���۸���sql
		publicSql.append(selcondition).append(",0,count(*),0,0,0,0,0 from CRMSaleFollowUp where 1=1 ").append(this.getCheckBillScope(null, "CRMSaleFollowUp", login, null, "", type, request))
		.append(this.WeekMonthTimeScope(isWeekQuery, "VisitTime", weekStartTime, weekEndTime, searchTime)).append(" group by ").append(selcondition).append(" union all select");
		//��ͬsql
		publicSql.append(selcondition).append(",0,0,count(*),sum(TotalAmount),0,0,0 from CRMSaleContract where 1=1 ").append(this.getCheckBillScope(null, "CRMSaleContract", login, null, "", type, request))
		.append(this.WeekMonthTimeScope(isWeekQuery, "SignUpDate", weekStartTime, weekEndTime, searchTime)).append(" group by ").append(selcondition).append(" union all select");
		//�ؿ�
		publicSql.append(selcondition).append(",0,0,0,0,count(*),sum(ExeBalAmt),0 from CRMSaleReceive where 1=1 ").append(this.getCheckBillScope(null, "CRMSaleReceive", login, null, "", type, request))
		.append(this.WeekMonthTimeScope(isWeekQuery, "BillDate", weekStartTime, weekEndTime, searchTime)).append(" group by ").append(selcondition).append(" union all select");
		//����
		publicSql.append(selcondition).append(",0,0,0,0,0,0,sum(TotalAmount) from CRMFee WHERE 1=1 ").append(this.getCheckBillScope(null, "CRMFee", login, null, "", type, request))
		.append(this.WeekMonthTimeScope(isWeekQuery, "createTime", weekStartTime, weekEndTime, searchTime)).append(" group by ").append(selcondition);
		
		
		querySql.append(publicSql.toString()).append("  ) a group by isnull(a.employeeId,'') ").append(sort);
		countSql.append(publicSql.toString()).append("  ) a ").append(sort);
		
		Result rs = mgt.queryWeekMonth(querySql.toString().toString());
		Result result = mgt.queryWeekMonth(countSql.toString().toString());
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//�ɹ�
			request.setAttribute("weekMonthList", rs.retVal);
			request.setAttribute("countList", result.retVal);
			request.setAttribute("isWeekQuery", isWeekQuery);
			request.setAttribute("weekStartTime", weekStartTime);
			request.setAttribute("weekEndTime", weekEndTime);
			request.setAttribute("sortName", sortName);
			return getForward(request, mapping, "weekMonthDetail");
		}else{
			//��ѯʧ��
			EchoMessage.error().add("��ѯʧ��").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}
	
	/**
	 * ���±���ʱ���
	 * @param isWeekQuery �Ƿ��ܲ�ѯ
	 * @param timeFieldName ʱ���ֶ�
	 * @param weekStartTime ��ʼʱ��
	 * @param weekEndTime ����ʱ��
	 * @param searchTime �²�ѯʱ��
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
