package com.menyi.aio.web.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.menyi.web.util.*;

import org.apache.struts.action.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dbfactory.*;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.importData.ImportDataMgt;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.dyndb.*;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ImportDataBean;
import com.menyi.aio.bean.ReportsBean;

import javax.servlet.http.Cookie;

import com.menyi.aio.bean.*;
import com.dbfactory.hibernate.DBUtil;

import java.net.URLEncoder;
import java.sql.SQLException;

import org.hibernate.Session;

import java.sql.CallableStatement;

import com.menyi.aio.web.login.LoginBean;
import com.dbfactory.hibernate.IfDB;

import java.sql.Connection;

import org.hibernate.jdbc.Work;

import com.menyi.aio.web.role.RoleMgt;
import com.menyi.aio.web.tablemapped.TableMappedMgt;
import com.menyi.aio.web.userFunction.*;


/**
 * <p>Title: ��λ���������</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class ReportDataAction extends MgtBaseAction {

    ReportDataMgt mgt = new ReportDataMgt();
    RoleMgt roleMgt=new RoleMgt();
    public ReportDataAction() {
    }

    /**
     * exe ��������ں���
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
    	String noback=request.getParameter("noback");//���ܷ���
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		request.setAttribute("popWinName", request.getParameter("popWinName")); //��¼���ڴ��Լ��ĵ����������֣����ڹر��Լ�
        String operation=request.getParameter("operation")==null?"":request.getParameter("operation");
        if("printActiveX".equals(operation)){
            return showPrint(mapping,form,request,response);
        }else if("bossReport".equals(operation)){
        	return bossReport(mapping,form,request,response) ;
        }else if("advance".equals(operation)){
        	return advanceCondition(mapping,form,request,response) ;
        }
        return showData(mapping,form,request,response);
    }

    /**
     * �ϰ屨��
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward advanceCondition(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String userId = this.getLoginBean(request).getId();
    	String opType = request.getParameter("opType");
    	String reportNumber = request.getParameter("reportNumber");
    	request.setAttribute("reportNumber", reportNumber);
    	if("sort".equals(opType)){
    		return mapping.findForward("advanceSort") ;
    	}else if("add".equals(opType) || "update".equals(opType)){
    		String id=request.getParameter("keyId");
    		request.setAttribute("id", id);
    		if(id != null && id.length() > 0 && "update".equals(opType)){
    			Result rs = mgt.loadAddanceCond(id);
    			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
    				Object[] os = (Object[])rs.retVal;
    				String name = String.valueOf(os[0]);
    				ArrayList<String[]> list = (ArrayList<String[]>)os[1];
    				request.setAttribute("name", name);
    				HashMap<String, ArrayList> map = new HashMap<String, ArrayList>();
    				ArrayList condList =  new ArrayList();
    				//groupName,field,value,hide_value
    				for(String[] ls :list){
    					ArrayList groups = map.get(ls[0]);
    					if(groups == null){
    						groups = new ArrayList();
    						map.put(ls[0], groups);
    						condList.add(ls[0]);
    					}
    					groups.add(ls);
    				}
    				request.setAttribute("condList", condList);
    				request.setAttribute("condMap", map);
    			}
    		}
    		return mapping.findForward("advanceUpdateCondition") ;
    	}else if("save".equals(opType)){
    		String name = request.getParameter("name");
    		String id=request.getParameter("id");
    		
    		ArrayList<String[]> list =  new ArrayList<String[]>();
    		
    		
    		String[] conds= request.getParameterValues("cond");
    		for(String cond :conds){
    			if(cond==null || cond.length() ==0){
    				continue;
    			}
    			//condId,groupName,field,value,hide_value
				String field = "";
				if(cond.equals("workFlowNodeNameCond")){
					field =request.getParameter("cond_"+cond);
				}else if(cond.equals("dateConditions")){
					field =request.getParameter("cond_"+cond);
				}else{
					field= cond;
				}
				String[] fs = field.split(":");
				for(String f:fs){					
					String value = request.getParameter(f);
					value = value == null?"":value;
					String hide_value = request.getParameter("hide_"+f);
					hide_value = hide_value == null?"":hide_value;
					list.add(new String[]{cond,f,value,hide_value});
				}
    		}
    		Result rs = mgt.saveAdvanceCond(reportNumber, id, userId, name, list);
    		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			EchoMessage.success().add("����ɹ�").setBackUrl("/ReportDataAction.do?operation=advance&opType=update&keyId="+rs.retVal+"&reportNumber="+reportNumber).setRequest(request);
    		}else{
    			EchoMessage.error().add(rs.getRetCode(),request).setRequest(request);            	
    		}
    		return getForward(request, mapping, "message");
    	}else if("delete".equals(opType)){
    		String id=request.getParameter("id");
    		Result rs = mgt.deleteAdanceCond(id);
    		String msg = "";
    		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			msg = "ɾ���ɹ�";
    		}else{
    			msg = "ɾ��ʧ��";
    		}
    		request.setAttribute("msg", msg);
    	}else if("def".equals(opType)){
    		String id=request.getParameter("id");
    		Result rs = mgt.defaultAdanceCond(id, reportNumber, userId);
    		String msg = "";
    		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			msg = "ִ�гɹ�";
    		}else{
    			msg = "ִ��ʧ��";
    		}
    		return mapping.findForward("blank") ;
    	}
    	Result rs = mgt.queryAdanceCond(reportNumber,userId);
    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		ArrayList<String[]> list = (ArrayList)rs.retVal;
    		boolean noDef = true;
    		for(String[] ls : list){
    			if("1".equals(ls[2])){
    				noDef = false;
    			}
    		}
    		request.setAttribute("noDef", noDef);
    	}
    	request.setAttribute("result", rs.retVal);
    	return mapping.findForward("advanceCondition") ;
    }
    
    /**
     * �ϰ屨��
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward bossReport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	String startDate = getParameter("startDate", request) ;
    	String endDate = getParameter("endDate", request) ;
    	String deptName=getParameter("deptName", request)==null?"":getParameter("deptName", request);
    	if(startDate==null){
    		ArrayList list=new ArrayList();
    		list.add("defaultTime");
    		startDate=ConfigParse.getSystemParam(list, "00001").get("defaultTime").toString();
    		endDate = "";
    	}
    	
    	LoginBean login = getLoginBean(request) ;
    	Result result = mgt.queryBossReport(startDate, endDate, login.getDefLoc(), login.getId(), login.getSunCmpClassCode(),deptName) ;
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("values", result.retVal) ;
    	}
    	request.setAttribute("startDate", startDate) ;
    	request.setAttribute("endDate", endDate) ;
    	request.setAttribute("deptNames", deptName) ;
    	request.setAttribute("deptName", java.net.URLEncoder.encode(deptName,"UTF-8")) ;
    	return mapping.findForward("bossReport") ;
    }
    /**
     * ��ӡ����
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward showPrint(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        String reportNumber=this.getParameter("reportNumber",request);
        Result rs2=mgt.getFormatList(reportNumber,getLocale(request).toString(),getLoginBean(request).getId(),getLoginBean(request).getDepartCode());
        if(rs2.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
            EchoMessage.error().add(rs2.getRetVal().toString()).setRequest(request);
            return getForward(request, mapping, "message");
        }
            	
        Cookie []coks= request.getCookies();
        for(int i=0;i<coks.length;i++){
            Cookie cok=coks[i];
            if(cok.getName().equals("JSESSIONID")){
                request.setAttribute("JSESSIONID",cok.getValue());
                break;
            }
        }
        Result rs3 = mgt.getReportByReportNumber(reportNumber) ;
        ReportsBean reportBean = new ReportsBean() ;
        if(rs3.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
        	reportBean = (ReportsBean) rs3.getRetVal() ;
        }
        request.setAttribute("formatList",rs2.getRetVal());
        request.setAttribute("ReportNumber",reportNumber);
        request.setAttribute("NewFlag", reportBean.getNewFlag()) ;
        request.setAttribute("reportId", reportBean.getId()) ;
        request.setAttribute("SQLFileName", reportBean.getSQLFileName()) ;
        request.setAttribute("SQLSave",request.getSession().getAttribute("SQLSave"));
        return mapping.findForward("printActiveX");
   }
    
 

    public ActionForward showData(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
    	String useFlex = (String)request.getSession().getAttribute("UseFlex");
    	if(useFlex!=null&&useFlex.equals("true")){
    		//ת��flexҳ��
    		if("getFlexData".equals(this.getParameter("type",request))){
    			long curTime =  System.currentTimeMillis();
    	        String reportNumber=this.getParameter("reportNumber",request);
    	        MOperation mop =(MOperation)this.getLoginBean(request).getOperationMap().get("/ReportDataAction.do?reportNumber="+reportNumber);
    	        ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);

    	        ReportData reportData=new ReportData();
    	        int pageSize = getParameterInt("pageSize", request);
    	        int pageNo = getParameterInt("pageNo", request);
    	        if (pageNo == 0) {
    	            pageNo = 1;
    	        }

    	        //Result rs = reportData.showData(request,scopeRight,reportNumber,"",pageNo,pageSize);
    	        System.out.println("����ִ��ʱ����"+((System.currentTimeMillis() - curTime)));
    	        request.setAttribute("moduleName",getModuleNameByLinkAddr(request, mapping));//ҳ�������ʾģ������
    			return mapping.findForward("flexReportData");
    		}
    		return mapping.findForward("flexReportList");
    	}
        long curTime =  System.currentTimeMillis();
        String reportNumber=this.getParameter("reportNumber",request);
        if(request.getParameter("GoodsFullName")!=null && !"".equals(request.getParameter("GoodsFullName"))){
    		request.setAttribute("GoodsFullName", "GoodsFullName");
    	}
        LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
        DefineReportBean defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(request).toString(),loginBean.getId());

        
        String mainNumber=request.getParameter("mainNumber");
        request.setAttribute("mainNumber", mainNumber);
        
        //�жϴ�ĳһģ�����ӳ����ı����Ƿ��ж����Ĳ˵�������еĻ����ȡ�ñ����Ȩ�ޣ�����Ļ���ȡģ���Ȩ��
        String reportUrl="/ReportDataAction.do?reportNumber="+reportNumber;
        String reportModule=GlobalsTool.getModelNameByLinkAdd(reportUrl);   
        
        MOperation mop;
        String detTable_list="";
        if(request.getParameter("detTable_list") != null &&request.getParameter("detTable_list").length() > 0){
        	//�����ϸ����Ȩ�������ݱ��б�������
        	detTable_list=request.getParameter("detTable_list");
        	request.setAttribute("detTable_list", detTable_list);
        	String moduleType = request.getParameter("moduleType");
        	request.setAttribute("moduleType", moduleType);
        	String url="/UserFunctionQueryAction.do?tableName="+detTable_list;
        	if(moduleType != null && moduleType.length() > 0){
        		url += "&moduleType="+moduleType;
        	}
        	mop =(MOperation)this.getLoginBean(request).getOperationMap().get(url);
            
        }else if(request.getParameter("mainNumber") != null &&request.getParameter("mainNumber").length() > 0){
        	//�����ϸ����Ȩ�������ݱ��б�������
        	String moduleType = request.getParameter("moduleType");
        	request.setAttribute("moduleType", moduleType);
        	String url="/ReportDataAction.do?reportNumber="+mainNumber;
        	if(moduleType != null && moduleType.length() > 0){
        		url += "&moduleType="+moduleType;
        	}
        	mop =(MOperation)this.getLoginBean(request).getOperationMap().get(url);
            
        }else{
        	mop =(MOperation)this.getLoginBean(request).getOperationMap().get("/ReportDataAction.do?reportNumber="+((reportModule!=null&&reportModule.length()>0)?reportNumber:mainNumber));
        }
        if(mop==null){
        	EchoMessage.error().add("δ����Ȩ").setRequest(request);
        	return getForward(request, mapping, "message");
        }
        request.setAttribute("MOID", mop.getModuleId());
        request.setAttribute("MOPBean", mop);
        ArrayList scopeRight = new ArrayList();
        scopeRight.addAll(mop.getScope(MOperation.M_QUERY));      
        //�������з���Ȩ��
    	scopeRight.addAll(mop.classScope) ;
      	ArrayList allScopeList = this.getLoginBean(request).getAllScopeRight();
    	if(allScopeList!=null){        		
    	   scopeRight.addAll(allScopeList) ;
		}
    	
        if(defBean.getClassCode() !=null && !"Y".equals(request.getParameter("checkTab"))){
        	//�����Ƿ��������checkʱ����ת��Ŀ¼��ʾ����
        	String mainSrc = request.getRequestURI()+(request.getQueryString()==null?"?checkTab=Y":"?"+request.getQueryString()+"&checkTab=Y");
        	request.setAttribute("mainSrc", mainSrc);
        	//���ϵͳ��DataType�ֶΣ�����ö�����͵ģ����Ǹ�������ֶν��в�ͬ�ķ��飬Ҫ�л��ɲ�ͬ��Ŀ¼
        	ReportField dtField = null;
        	for(ReportField rf :defBean.getFieldInfo()){
        		if(rf.getAsFieldName().equals("DataType") || rf.getAsFieldName().equals("DateType")){
        			dtField = rf;
        			break;
        		}
        	}
        	String clName = "";
        	if(dtField != null && dtField.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE){
        		//��DataType�ֶΣ�����ö�����͵�
        		//ȡö��
        		String enumname = dtField.getPopUpName();
        		List el = GlobalsTool.getEnumerationItems(enumname, this.getLocale(request).toString());
        		KeyPair eib =(KeyPair)el.get(0);
        		clName = eib.getValue();
        		if (clName.equals("StockCode"))
        			clName = "tblStock";
				else if (clName.equals("DepartmentCode"))
					clName = "tblDepartment";
				else if (clName.equals("CompanyCode"))
					clName = "tblCompany";
				else if (clName.equals("EmployeeID"))
					clName = "tblEmployee";
        	}else{
        		String fn = defBean.getClassCode().getFieldName();
        		clName = fn.substring(0,fn.indexOf("."));
        	}
        	request.setAttribute("moduleType", request.getParameter("moduleType"));
        	request.setAttribute("SysType", defBean.getClassCode().getFieldIdentity()); //�����ֶε����ͣ������ǿͻ����ǹ�Ӧ��        	
        	request.setAttribute("tableName", clName);
        	String subSql = defBean.getClassCode().getSubSQL();
        	subSql = subSql==null?"":URLEncoder.encode(subSql,"UTF-8");
        	request.setAttribute("subSql", subSql);
        	ReportSetMgt setMgt = new ReportSetMgt();
        	String locale = GlobalsTool.getLocale(request).toString();
        	ReportsBean reportSetBean = (ReportsBean) setMgt.getReportSetInfo(reportNumber, locale).getRetVal();
        	request.setAttribute("reportName", reportSetBean==null?"":reportSetBean.getReportName());
        	return getForward(request, mapping, "frameSet");
        }
    	
        ReportData reportData=new ReportData();
        int pageSize = getParameterInt("pageSize", request);
        int pageNo = getParameterInt("pageNo", request);
        String noPageSize = (String) request.getAttribute("NoPageSize") ; 
        if (pageNo == 0 || "OK".equals(noPageSize)) {
            pageNo = 1;
        }
          
        
        
        Result rs = reportData.showData(mop,request,scopeRight,reportNumber,defBean,"",pageNo,pageSize,new ArrayList(),new ArrayList(),loginBean,detTable_list);
        
        //�����ɹ�
        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && request.getAttribute("ExportFileName") !=null && !request.getAttribute("ExportFileName").equals("")){
        	String fn =request.getAttribute("ExportFileName")+"";
        	String mime = request.getSession().getServletContext().getMimeType(fn.substring(fn.lastIndexOf(".")).toLowerCase());
        	if(mime == null || mime.length() == 0){
        		mime = "application/octet-stream; CHARSET=utf8";
        		response.setContentType(mime);
        		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
        	}else{
        		response.setContentType(mime);
        		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
        	}
        	FileHandler.readFile("../../AIOBillExport/"+fn,response);
        	return null;
        }
        if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS && rs.retCode!=ErrorCanst.RETURN_COL_CONFIG_SETTING){
        	if(rs.retCode==ErrorCanst.RET_LIST_NOCOLUMN){
            	EchoMessage.error().add(rs.getRetCode(),request).setRequest(request);
            	return getForward(request, mapping, "message");
        	}else if(rs.retCode==ErrorCanst.EXCEL_REPORT_EXPORT){
//        		zxy Ϊ�˽������ʱ�������ص����⣨��ѡ���ѯ�����������ɹ���������أ�������ʧ��,���ﲻֱ�ӷ��أ����Ǽ���ִ��������ѯ��������ˢ���ڼ��ExportMsg�����Ϊ�գ��������ҳ,�������ﲻ�����������ֵ
//        		rs.retCode = ErrorCanst.DEFAULT_SUCCESS;
//        		request.setAttribute("ExportMsg", rs.getRetVal().toString());
//        		request.setAttribute("noback", false);
//        		EchoMessage.success().add(rs.getRetVal().toString()).setBackUrl("/ReportDataAction.do?reportNumber="+reportNumber).setNotAutoBack().setRequest(request);
//            	return getForward(request, mapping, "message");
        	}else{
        		String popWinName = request.getParameter("popWinName");
        		if(popWinName != null && popWinName.length() > 0){
        			request.setAttribute("noback",false);
        			EchoMessage.error().add(rs.getRetVal().toString()).setClosePopWin(popWinName).setRequest(request);
        		}else{
        			String backUrl = "/ReportDataAction.do?reportNumber="+reportNumber+"&winCurIndex="+request.getParameter("winCurIndex")+"&checkTab="+request.getParameter("checkTab");
        			EchoMessage.error().add(rs.getRetVal().toString()).setBackUrl(backUrl).setRequest(request);
        		}
            	return getForward(request, mapping, "message");
        	}
        }
        
        if(rs.retCode==ErrorCanst.RETURN_COL_CONFIG_SETTING){
        	request.setAttribute("reportType", "reportList") ;
        	request.setAttribute("tableName", reportNumber) ;
        	return getForward(request, mapping, "listConfig") ;
        }
        
        System.out.println("����ִ��ʱ��"+((System.currentTimeMillis() - curTime)));
        if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
            EchoMessage.error().add(rs.getRetVal().toString()).
                setRequest(request);
            return  getForward(request, mapping, "message");
        }
        if(rs.getPageSize()>0){
        	request.setAttribute("pageBar", pageBar(rs, request));
        }
        request.setAttribute("moduleName",getModuleNameByLinkAddr(request, mapping));//ҳ�������ʾģ������
        request.setAttribute("isTran", request.getParameter("isTran")); //�Ƿ��Ǵ�͸ʽ����Ϊ�ǣ�����ʾ����������֮����κζ���
//        if("true".equals(request.getParameter("fromajax"))){
//        	return mapping.findForward("reportAjax");
//        }
        request.setAttribute("noback", request.getParameter("noback"));
        return mapping.findForward("reportList");
    }

}
