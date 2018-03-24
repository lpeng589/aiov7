package com.menyi.aio.web.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.bean.ReportsDetBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PopSelectConfig;
import com.menyi.web.util.TestRW;

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
public class ReportSetAction extends MgtBaseAction {

    ReportSetMgt mgt = new ReportSetMgt();

    public ReportSetAction() {
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

    	request.setAttribute("JSESSIONID", request.getSession().getId());
        //���ݲ�ͬ�������ͷ������ͬ��������
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
            forward = addPrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_ADD:
            forward = add(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE_PREPARE:
            forward = updatePrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_COPY_PREPARE:
        	request.setAttribute("oper", "copy");
            forward = updatePrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE:
        	String type=request.getParameter("type");
        	if(type!=null&&type.length()>0){
        		forward = copyStyle(mapping, form, request, response);
        	}else{
        		forward = update(mapping, form, request, response);
        	}
            break;
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_SET_PREPARE:
        	forward = setPrepare(mapping, form, request, response);
        	break ;
        case OperationConst.OP_SET:
        	forward = set(mapping, form, request, response);
        	break ;
        case OperationConst.OP_QUERY:
        	String showSet = request.getParameter("showSet");
        	if("true".equals(showSet)){
        		forward = showSet(mapping, form, request, response);
        	}else{        		
        		forward = query(mapping, form, request, response);
        	}
            break;
        default:
        	String filterSQL = this.getParameter("filterSQL", request);
        	if("true".equals(filterSQL)){
        		forward = filterSQL(mapping, form, request, response);
        	}else{
        		forward = query(mapping, form, request, response);
        	}
        }
        return forward;
    }

    /**
     * ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
        Exception {
        request.removeAttribute("operation");
        return getForward(request, mapping, "reportSetAdd");
    }

    /**
     * ���
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward add(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

         ActionForward forward = getForward(request, mapping, "alert");
         ReportSetForm myForm = (ReportSetForm)form;
         Hashtable map=(Hashtable)request.getSession().getServletContext().getAttribute(
                 BaseEnv.TABLE_INFO);
         String locale = GlobalsTool.getLocale(request).toString();
         Result rs2=mgt.getReportSetInfo(myForm.getReportNumber(),locale);
         if(rs2.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
             if(rs2.getRetVal()!=null){
                 EchoMessage.error().add(getMessage(
                  request, "reportSet.msg.numberExists")).
                  setAlertRequest(request);

                 return forward;
             }
         }else{
             EchoMessage.error().add(getMessage(
                  request, "common.msg.addFailture")).
                  setAlertRequest(request);
         }
        
         /**
          * ��֤�������������ѡ����ǵ��ݱ��������ݿ����Ѿ�������ͬ�ĵ�������ʱ������
          * ��Ϊ��ͬһ�ŵ��ݲ�����Ӷ�����ݱ���
          */
//         rs2 = mgt.isExistBillMain(myForm.getBillTable(),myForm.getReportType(),myForm.getModuleType(),null);
//         if(rs2.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
//        	 String flag = String.valueOf(rs2.retVal);
//        	 if(flag != null && "true".equals(flag)){
//        		 EchoMessage.error().add("���������ظ�").setAlertRequest(request);
//                 return forward;
//        	 }
//         }else{
//        	 EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
//         }
         
        //ִ����Ӳ���
        Result rs =mgt.add(myForm,request,this.getLoginBean(request).getId(),locale);
        

        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //��ӳɹ��󱣴�ű�
            String path=request.getSession().getServletContext().getRealPath("Report.sql").toString();
            ConvertToSql.saveReports(path,ReportSetMgt.reportId,1);
            ReportSetMgt.reportId="";
            //��ӳɹ�
            EchoMessage.success().add(getMessage(
                request, "common.msg.addSuccess"))
                .setBackUrl("/ReportSetQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                setAlertRequest(request);
            String reportName =myForm.getReportName();
            if(reportName != null &&reportName.length() > 0){
     		   int pos = reportName.indexOf(locale)+locale.length()+1;
     		   reportName = reportName.substring(pos,reportName.indexOf(";",pos));  
     	   }
            new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		reportName, "tblReport", "��������","");
        }else {
            //���ʧ��
            EchoMessage.error().add(getMessage(
                request, "common.msg.addFailture")).
                setAlertRequest(request);
        }
        return forward;
    }

    /**
     * �޸�ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward updatePrepare(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        String nstr = request.getParameter("keyId");
        String reportNames = request.getParameter("reportNames")  ;
        if(reportNames!=null){
        	reportNames=new String(reportNames.getBytes("ISO-8859-1"),"UTF-8");	
        	request.setAttribute("reportNames", reportNames) ;
        }
        if (nstr != null && nstr.length() != 0) {

            //ִ�в�ѯǰ�ļ���
            Result rs = mgt.detail(nstr);
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //���سɹ�

                request.setAttribute("result", (ReportsBean)rs.retVal);
                forward = getForward(request, mapping, "reportSetUpdate");
            }  else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
                //��¼�����ڴ���
                EchoMessage.error().add(getMessage(
                    request, "common.error.nodata")).setRequest(request);
            } else {
                //����ʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;
    }

    /**
     * ������ʽ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward copyStyle(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {
        ReportSetForm myForm = (ReportSetForm)form;
        ActionForward forward = null;
        //ִ���޸�
        String keyId=request.getParameter("keyId") ;
        String FormatFileName=request.getParameter("FormatFileName");
        String styleName=request.getParameter("styleName");
        styleName=new String(styleName.getBytes("iso-8859-1"), "UTF-8");
        String winCurIndex=request.getParameter("winCurIndex");
        Result rs =mgt.copyStyle(keyId,FormatFileName,styleName,this.getLoginBean(request).getId());
        forward = getForward(request, mapping, "message");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            /////////////////////
            //�޸ĳɹ�
            ReportsBean bean = (ReportsBean) rs.retVal;
            //�޸ĳɹ��󱣴�ű�
       	    String path=request.getSession().getServletContext().getRealPath("Report.sql").toString();
            String upReportId=ReportSetMgt.upReportId;
            ConvertToSql.saveReports(path,upReportId,2);
            ReportSetMgt.upReportId="";
            request.setAttribute("reportNames", bean.getReportNumber());
            EchoMessage.success().add(getMessage(
                request, "common.msg.updateSuccess"))
                .setBackUrl("/ReportSetQueryAction.do?winCurIndex="+winCurIndex).
                setAlertRequest(request);
        }  else {
            //�޸�ʧ��
            EchoMessage.error().add(getMessage(
                request, "common.msg.updateErro")).setAlertRequest(
                    request);
        }
        return forward;
    }
    /**
     * �޸�
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward update(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {
        ReportSetForm myForm = (ReportSetForm)form;
        ActionForward forward = getForward(request, mapping, "alert");
        
        String locale = GlobalsTool.getLocale(request).toString();
        Result rs2=mgt.getReportSetInfoUp(myForm.getReportNumber(),locale,myForm.getId());
        if(rs2.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
            if(rs2.getRetVal()!=null){
                EchoMessage.error().add(getMessage(
                 request, "reportSet.msg.numberExists")).
                 setAlertRequest(request);

                return forward;
            }
        }else{
            EchoMessage.error().add(getMessage(
                 request, "common.msg.addFailture")).
                 setAlertRequest(request);
        }
        
        /**
         * ��֤�������������ѡ����ǵ��ݱ��������ݿ����Ѿ�������ͬ�ĵ�������ʱ������
         * ��Ϊ��ͬһ�ŵ��ݲ�����Ӷ�����ݱ���
         */
       /* rs2 = mgt.isExistBillMain(myForm.getBillTable(), myForm.getReportType(),myForm.getModuleType(), myForm.getId());
		if (rs2.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String flag = String.valueOf(rs2.retVal);
			if (flag != null && "true".equals(flag)) {
				EchoMessage.error().add("���������ظ�").setAlertRequest(request);
				return forward;
			}
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
			return forward;
		}
		*/
        
        // ִ���޸�
        Result rs =mgt.update(myForm,request,this.getLoginBean(request).getId());
        
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {

            //�޸ĳɹ��󱣴�ű�
            String path=request.getSession().getServletContext().getRealPath("Report.sql").toString();
            String upReportId=ReportSetMgt.upReportId;
            ConvertToSql.saveReports(path,upReportId,2);
            ReportSetMgt.upReportId="";
            /////////////////////
            //�޸ĳɹ�
            String winCurIndex=request.getParameter("winCurIndex");
            EchoMessage.success().add(getMessage(
                request, "common.msg.updateSuccess"))
                .setBackUrl("/ReportSetQueryAction.do?winCurIndex="+request.getParameter("winCurIndex")).
                setAlertRequest(request);
            
            String reportName =myForm.getReportName();
            if(reportName != null &&reportName.length() > 0){
     		   int pos = reportName.indexOf(locale)+locale.length()+1;
     		   reportName = reportName.substring(pos,reportName.indexOf(";",pos));  
     	   }
            new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		reportName, "tblReport", "��������","");
        }  else {
            //�޸�ʧ��
            EchoMessage.error().add(getMessage(
                request, "common.msg.updateErro")).setAlertRequest(
                    request);
        }
        return forward;
    }
    
    /**
     * ��ʽ����
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward set(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        ActionForward forward = null;
        
        return forward;

    }
    
    /**
     * ��ʽ����
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward setPrepare(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        String reportId = getParameter("reportId", request) ;
        request.setAttribute("reportId", reportId);        
        String isbillType = getParameter("isbillType",request);
        request.setAttribute("isbillType", isbillType);
        Result result ;
        if("true".equals(isbillType)){
        	String tableName = getParameter("tableName",request);
        	request.setAttribute("tableName", tableName);
        	String moduleType = getParameter("moduleType",request);
        	request.setAttribute("moduleType", moduleType);
        	result = mgt.queryFormatStyle(tableName,moduleType, getLoginBean(request).toString(),this.getLocale(request).toString()) ;
        }else{
        	result = mgt.queryFormatStyle(reportId, getLoginBean(request).toString(),this.getLocale(request).toString()) ;
        }
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	request.setAttribute("styleFormat", result.retVal) ;
        	result = mgt.queryDeptMapName() ;
        	/*��ѯ������Ϣ*/
        	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		request.setAttribute("deptMap", result.retVal) ;
        	}
        }
        return getForward(request, mapping, "formatSet") ;

    }

    /**
     * 
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        String keyIds[] = request.getParameterValues("keyId");
        if (keyIds != null && keyIds.length != 0) {
        	ArrayList<ReportsBean> dellist = new ArrayList<ReportsBean>();
        	for(String key:keyIds){
        		Result rdel  = mgt.detail(key);
        		if(rdel.retCode== ErrorCanst.DEFAULT_SUCCESS &&rdel.retVal != null){
        			dellist.add((ReportsBean)rdel.retVal);
        		}
        	}
        	
            Result rs = mgt.delete(keyIds);

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //ɾ���ɹ��󱣴�ű�
               String path=request.getSession().getServletContext().getRealPath("Report.sql");
               List<String> list=ConvertToSql.getReportsDelSql(keyIds);
               for(int i=0;i<list.size();i++)
               {
                   TestRW.saveToSql(path,list.get(i));
               }
               String locale = this.getLocale(request).toString();
               for(ReportsBean bean:dellist){
            	   
            	   String reportName =bean.getReportName();
            	   if(reportName != null &&reportName.length() > 0){
            		   int pos = reportName.indexOf(locale)+locale.length()+1;
            		   reportName = reportName.substring(pos,reportName.indexOf(";",pos));  
            	   }
            	   new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            			   reportName, "tblReport", "��������","");
               }
               ///////////////////////
                //ɾ���ɹ�
                request.setAttribute("result", rs.retVal);
                forward = query(mapping, form, request, response);
            } else {
                //ɾ��ʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.delError")).
                    setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;

    }
    
    protected ActionForward showSet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String save = request.getParameter("save");
		String tableName = request.getParameter("tableName");
		request.setAttribute("tableName", tableName);
		if("true".equals(save)){
			String[] fNames = request.getParameterValues("fName");
			ArrayList<HashMap> fList = new ArrayList<HashMap>();
			for(String fName:fNames){
				HashMap map =new HashMap();
				String rv = request.getParameter("reportView_"+fName);
				if("1".equals(rv)){
					map.put("reportView", "1");	
				}
				rv = request.getParameter("billView_"+fName);
				if("1".equals(rv)){
					map.put("billView", "1");	
					map.put("popupView", "1");
				}
				rv = request.getParameter("popSel_"+fName);
				if("1".equals(rv)){
					map.put("popSel", "1");	
					map.put("popupView", "1");
				}
				rv = request.getParameter("keyword_"+fName);
				if("1".equals(rv)){
					map.put("keyword", "1");	
					map.put("popSel", "1");	
					map.put("popupView", "1");
				}
				rv = request.getParameter("popupView_"+fName);
				if("1".equals(rv)){
					map.put("popupView", "1");
				}
				if(map.size() > 0){
					fList.add(map);
					map.put("fieldName", fName);
				}
			}
			for (HashMap fmap : fList) {	
				String str = (String)fmap.get("fieldName");
				String fieldName = str.substring(str.lastIndexOf("_") + 1);
				String fn = tableName;
				fn = fn.indexOf("_")>-1?fn.substring(0,fn.indexOf("_")):fn;
				DBFieldInfoBean bean =GlobalsTool.getFieldBean(fn+"."+fieldName);
				if(bean==null){
					EchoMessage.error().add("�޸�ʧ�ܣ��ֶ�"+fn+"."+fieldName+"Ϊ��").setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}else if(bean.getInputTypeOld()==DBFieldInfoBean.INPUT_ENUMERATE && "1".equals(fmap.get("billView"))){
					EchoMessage.error().add("ö���ֶ�"+bean.getDisplay().get(this.getLocale(request).toString())+"����ѡ�񵥾���ʾ").setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}else if(bean.getInputTypeOld()==DBFieldInfoBean.INPUT_ENUMERATE && "1".equals(fmap.get("keyword"))){
					EchoMessage.error().add("ö���ֶ�"+bean.getDisplay().get(this.getLocale(request).toString())+"������Ϊ�ؼ���").setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}else if(bean.getFieldType()==DBFieldInfoBean.FIELD_PIC && ("1".equals(fmap.get("keyword")) || "1".equals(fmap.get("popSel")))){
					EchoMessage.error().add("ͼƬ�ֶ�"+bean.getDisplay().get(this.getLocale(request).toString())+"����������������ؼ�������").setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}
			
			
			mgt.updateShowSet(tableName,fList);
			
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),
    				"��ʾ���޸�", "tblReports", "����","");
			
			Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
			request.getSession().getServletContext().getAttribute("userSettingColConfig") ;
	        //��ʱ��ʼ��SQL�����ļ�
	        if (!PopSelectConfig.parseConfig()) {
	            BaseEnv.log.error(
	                    "--------Refresh PopSelectConfig Failture---------------");
	        }
			userColConfig.clear();
			
			request.setAttribute("dealAsyn", "true");	
			EchoMessage.success().add("�޸ĳɹ�!").setAlertAndClosePopWin("setId").setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		Result rs =mgt.getShowSet(tableName);
		request.setAttribute("result", rs.retVal);
		return getForward(request, mapping, "showSet");
	}
    
    protected ActionForward filterSQL(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sql = request.getParameter("sql");
		String id = this.getParameter("id", request);
		Result rs =mgt.saveFilterSQL(sql,id);
		String msg="����ɹ�";
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			msg = rs.retVal+"";
			if(msg==null || msg.length() > 0){
				msg = "����ʧ��";
			}
		}
		request.setAttribute("msg", msg);
		return getForward(request, mapping, "blank");
	}

    /**
     * ��ѯ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        String styleType = getParameter("styleType", request) ;
        request.setAttribute("styleType", styleType) ;
    	
        ReportSetSearchForm searchForm = (ReportSetSearchForm) form;
        if (searchForm != null) {
            //ִ�в�ѯ
        	String reportName=this.getParameter("reportNames",request);
        	if("GET".equals(request.getMethod()) && reportName!=null){
        		reportName=new String(reportName.getBytes("ISO-8859-1"),"UTF-8");
            }
            String statusId=this.getParameter("statusIds",request);
            String reportNumber=this.getParameter("reportNumber",request);
            String reportType=this.getParameter("reportType",request);
            String reportModule = this.getParameter("reportModule", request) ;
            //�������Ǳ���Ϣ��ʾ���ƣ�û�б������õģ������˱�ı�������
            String locale = GlobalsTool.getLocale(request).toString();
            Result rs2=mgt.getReportSetInfo(reportNumber,locale);
            String type=request.getParameter("type");
            if(type!=null&&type.equals("table")&&rs2.getRetVal()==null){
                ReportSetForm rf=new ReportSetForm();
                Hashtable map=(Hashtable)request.getSession().getServletContext().getAttribute(
                    BaseEnv.TABLE_INFO);
                DBTableInfoBean tableInfo=DDLOperation.getTableInfo(map,reportNumber);
                rf.setReportNumber(tableInfo.getTableName().trim());
                rf.setReportName(tableInfo.getDisplay().get(this.getLocale(request).toString()).toString().trim());
                rf.setReportType("TABLELIST");
                mgt.add(rf,request,this.getLoginBean(request).getId(),locale);
            }else if(type!=null&&type.equals("Ini")&&rs2.getRetVal()==null){
                ReportSetForm rf=new ReportSetForm();

                rf.setReportNumber(reportNumber);
                String reportDis="";
                if(reportNumber.equals("IniReportAcc")){
                   reportDis=this.getMessage(request,"iniAcc.lb.title");
                }else if(reportNumber.equals("IniReportreceive")){
                   reportDis=this.getMessage(request,"iniCompany.lb.receive");
                }else if(reportNumber.equals("IniReportpreReceive")){
                  reportDis=this.getMessage(request,"iniCompany.lb.preReceive");
                }else if(reportNumber.equals("IniReportpay")){
                  reportDis=this.getMessage(request,"iniCompany.lb.pay");
                }else if(reportNumber.equals("IniReportprePay")){
                  reportDis=this.getMessage(request,"iniCompany.lb.prePay");
                }else if(reportNumber.equals("IniReportGoods")){
                   reportDis=this.getMessage(request,"iniGoods.lb.title");
                }
                rf.setReportName(reportDis);
                rf.setReportType("NORMAL");
                mgt.add(rf,request,this.getLoginBean(request).getId(),locale);
            }
            Result rs =mgt.query(reportName,reportNumber,statusId,reportType,reportModule,searchForm.getPageNo(),searchForm.getPageSize(),locale,this.getLoginBean(request).getDefSys());
            
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //��ѯ�ɹ�
                request.setAttribute("reportNames",reportName);
                request.setAttribute("statusIds",statusId);
                request.setAttribute("reportType",reportType);
                request.setAttribute("result", rs.retVal);
                request.setAttribute("Language", this.getLocale(request));
                request.setAttribute("reportModule", reportModule) ;
                request.setAttribute("pageBar", pageBar(rs, request));
            } else {
                //��ѯʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                return getForward(request, mapping, "message");
            }
        }
        return getForward(request, mapping, "reportSetList");
    }

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		String type = getParameter("styleType", req) ;
		int opertion = getOperation(req) ;
		if("print".equals(type) || opertion == OperationConst.OP_SET_PREPARE){
			return null ;
		}
		return super.doAuth(req, mapping);
	}

    
}
