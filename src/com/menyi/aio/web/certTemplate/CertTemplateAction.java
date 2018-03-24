package com.menyi.aio.web.certTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.UnitBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.certificate.CertificateBillBean;
import com.menyi.aio.web.certificate.CertificateTemplateBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;

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
public class CertTemplateAction extends MgtBaseAction {

	CertTemplateMgt mgt = new CertTemplateMgt();

    public CertTemplateAction() {
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
        case OperationConst.OP_UPDATE:
            forward = update(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_QUERY:
        	String type= request.getParameter("type");
        	if("getFieldList".equals(type)){
        		forward = getFieldList(mapping, form, request, response);
        	}else{
        		forward = query(mapping, form, request, response);
        	}
            break;
        default:
            forward = query(mapping, form, request, response);
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
        //��������
        request.setAttribute("tableList", mgt.queryModuleType(this.getLocale(request).toString()));
        return getForward(request, mapping, "CertTemplateInfo");
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

       	String locale = this.getLocale(request).toString();

        ActionForward forward = getForward(request, mapping, "alert");
        CertificateBillBean bean  = new CertificateBillBean();
        bean.setCredTypeID(request.getParameter("credTypeID"));
        bean.setId(IDGenerater.getId());
        bean.setPopupName(request.getParameter("popupName"));
        bean.setTableName(request.getParameter("tableName"));
        bean.setTempName(request.getParameter("tempName"));
        bean.setTempNumber(request.getParameter("tempNumber"));
        bean.setType(request.getParameter("type"));
        
        
        String[] comments = request.getParameterValues("comment");
        String[] dircs = request.getParameterValues("dirc");
        String[] accCodes = request.getParameterValues("accCode");
        String[] fieldNames = request.getParameterValues("fieldName");
        String[] curFieldNames = request.getParameterValues("curFieldName");
        String[] companyCodes = request.getParameterValues("companyCode");
        String[] departmentCodes = request.getParameterValues("departmentCode");
        String[] employeeIDs = request.getParameterValues("employeeID");
        String[] stockCodes = request.getParameterValues("stockCode");
        String[] goodsCodes = request.getParameterValues("goodsCode");
        String[] currencys = request.getParameterValues("currency");
        String[] currencyRates = request.getParameterValues("currencyRate");
        String[] projectCodes = request.getParameterValues("projectCode");
        bean.setTempList(new ArrayList());
        for(int i=0;i<dircs.length;i++){
        	if(accCodes[i]==null||accCodes[i].length()==0){
        		 EchoMessage.error().add("��Ŀ����Ϊ��").setAlertRequest(
        	                    request);
        		 return getForward(request, mapping, "alert");
        	}
        	if(fieldNames[i]==null||fieldNames[i].length()==0){
	       		 EchoMessage.error().add("��ʽ����Ϊ��").setAlertRequest(
	       	                    request);
	       		return getForward(request, mapping, "alert");
	       	}
        	
        	CertificateTemplateBean tb = new CertificateTemplateBean();
        	tb.setId(IDGenerater.getId());
        	tb.setTableName(bean.getTableName());
        	tb.setAccCode(accCodes[i]);
        	tb.setDirc(Integer.parseInt(dircs[i]));
        	
        	//��ժҪ���н���
        	Pattern pattern = Pattern.compile("\\[([^\\[\\]]*_|.{0})([^\\[\\]]+)\\]",Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(comments[i]);
			String rstr=comments[i];
			while (matcher.find()) {
				String all = matcher.group();
				String ctnd = matcher.group(1);
				String cfnd = matcher.group(2);
				String ctn="",cfn="";
				DBTableInfoBean tBean=null;
				if(ctnd==null || ctnd.length()==0){
					//����
					ctn = bean.getTableName();
					tBean = GlobalsTool.getTableInfoBean(ctn);
				}
				
				if(ctn==null || ctn.length() ==0){
					ctnd = ctnd.substring(0,ctnd.length() -1);
					ArrayList<DBTableInfoBean> ctlist = DDLOperation.getChildTables(bean.getTableName(), BaseEnv.tableInfos);
					for(DBTableInfoBean ctb:ctlist){
						if(ctb.getDisplay().get(locale).equals(ctnd)){
							ctn = ctb.getTableName();
							tBean = ctb;
						}
					}
				}
				
				if(tBean==null){
					EchoMessage.error().add("ժҪ����������["+ctnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}
				for(DBFieldInfoBean cfb:tBean.getFieldInfos()){
					if(cfb.getDisplay().get(locale).equals(cfnd)){
						cfn = cfb.getFieldName();
					}
				}
				if(cfn==null||cfn.length() ==0){
					EchoMessage.error().add("ժҪ�ֶβ�����["+cfnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}			
				String after = rstr.substring(rstr.indexOf(all)+all.length());
				rstr = rstr.substring(0,rstr.indexOf(all))+ctn+"."+cfn+(after.startsWith(" ")?after:" "+after);	
			}
			//��������Ƿ���δ��Ե�[]
			if(rstr.indexOf("[")> -1 || rstr.indexOf("]") > -1){
				EchoMessage.error().add("ժҪ�ֶθ�ʽ����ȷ"+comments[i]+"").setAlertRequest(
   	                    request);
				return forward;
			}
			tb.setComment(rstr);
        	//�Թ�ʽ���н���
        	pattern = Pattern.compile("\\[([^\\[\\]]*_|.{0})([^\\[\\]]+)\\]",Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(fieldNames[i]);
			rstr=fieldNames[i];
			
			//��ʱ���ƣ�ֻ�ܴ�һ�ű�ȡ���ݣ������������ȡ���ݣ��ִ���ϸ��ȡ��
			String ttn = "";				
			while (matcher.find()) {
				String all = matcher.group();
				String ctnd = matcher.group(1);
				String cfnd = matcher.group(2);
				String ctn="",cfn="";
				DBTableInfoBean tBean=null;
				if(ctnd==null || ctnd.length()==0){
					//����
					ctn = bean.getTableName();
					tBean = GlobalsTool.getTableInfoBean(ctn);
				}
				
				if(ctn==null || ctn.length() ==0){
					ctnd = ctnd.substring(0,ctnd.length() -1);
					ArrayList<DBTableInfoBean> ctlist = DDLOperation.getChildTables(bean.getTableName(), BaseEnv.tableInfos);
					for(DBTableInfoBean ctb:ctlist){
						if(ctb.getDisplay().get(locale).equals(ctnd)){
							ctn = ctb.getTableName();
							tBean = ctb;
						}
					}
				}				
				if(tBean==null){
					EchoMessage.error().add("��ʽ����������["+ctnd+"]").setAlertRequest(
       	                    request);
					return getForward(request, mapping, "alert");
				}
				if(ttn.length()==0){
					ttn = ctn;
				}else if(!ttn.equals(ctn)){
//					EchoMessage.error().add("��ʽ�������ͬһ����ȡ��").setAlertRequest(
//       	                    request);
//					return getForward(request, mapping, "alert");
				}
				for(DBFieldInfoBean cfb:tBean.getFieldInfos()){
					if(cfb.getDisplay().get(locale).equals(cfnd)){
						cfn = cfb.getFieldName();
						break;
					}
				}
				if(cfn==null||cfn.length() ==0){
					EchoMessage.error().add("����ֶβ�����["+cfnd+"]").setAlertRequest(
       	                    request);
					return getForward(request, mapping, "alert");
				}				
				String after = rstr.substring(rstr.indexOf(all)+all.length());
				rstr = rstr.substring(0,rstr.indexOf(all))+ctn+"."+cfn+(after.startsWith(" ")?after:" "+after);	
			}
			//��������Ƿ���δ��Ե�[]
			if(rstr.indexOf("[")> -1 || rstr.indexOf("]") > -1){
				EchoMessage.error().add("����ֶθ�ʽ����ȷ"+fieldNames[i]+"").setAlertRequest(
   	                    request);
				return getForward(request, mapping, "alert");
			}
			
        	tb.setFieldName(rstr);
        	
        	//����ҹ�ʽ���н���
        	pattern = Pattern.compile("\\[([^\\[\\]]*_|.{0})([^\\[\\]]+)\\]",Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(curFieldNames[i]);
			rstr=curFieldNames[i];
			ttn = "";
			while (matcher.find()) {
				String all = matcher.group();
				String ctnd = matcher.group(1);
				String cfnd = matcher.group(2);
				String ctn="",cfn="";
				DBTableInfoBean tBean=null;
				if(ctnd==null || ctnd.length()==0){
					//����
					ctn = bean.getTableName();
					tBean = GlobalsTool.getTableInfoBean(ctn);
				}
				
				if(ctn==null || ctn.length() ==0){
					ctnd = ctnd.substring(0,ctnd.length() -1);
					ArrayList<DBTableInfoBean> ctlist = DDLOperation.getChildTables(bean.getTableName(), BaseEnv.tableInfos);
					for(DBTableInfoBean ctb:ctlist){
						if(ctb.getDisplay().get(locale).equals(ctnd)){
							ctn = ctb.getTableName();
							tBean = ctb;
						}
					}
				}
				if(tBean==null){
					EchoMessage.error().add("��ҽ�ʽ����������["+ctnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}
				if(ttn.length()==0){
					ttn = ctn;
				}
//				else if(!ttn.equals(ctn)){
//					EchoMessage.error().add("��ҽ�ʽ�������ͬһ����ȡ��").setAlertRequest(
//       	                    request);
//					return getForward(request, mapping, "alert");
//				}
				for(DBFieldInfoBean cfb:tBean.getFieldInfos()){
					if(cfb.getDisplay().get(locale).equals(cfnd)){
						cfn = cfb.getFieldName();
					}
				}
				if(cfn==null||cfn.length() ==0){
					EchoMessage.error().add("��ҽ���ֶβ�����["+cfnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}			
				String after = rstr.substring(rstr.indexOf(all)+all.length());
				rstr = rstr.substring(0,rstr.indexOf(all))+ctn+"."+cfn+(after.startsWith(" ")?after:" "+after);	
			}
			//��������Ƿ���δ��Ե�[]
			if(rstr.indexOf("[")> -1 || rstr.indexOf("]") > -1){
				EchoMessage.error().add("��ҽ���ֶθ�ʽ����ȷ"+fieldNames[i]+"").setAlertRequest(
   	                    request);
				return forward;
			}
			
        	tb.setCurFieldName(rstr);
        	tb.setCompanyCode(companyCodes[i]);
        	tb.setDepartmentCode(departmentCodes[i]);
        	tb.setEmployeeID(employeeIDs[i]);
        	tb.setStockCode(stockCodes[i]);
        	tb.setGoodsCode(goodsCodes[i]);
        	tb.setCurrency(currencys[i]);
        	tb.setCurrencyRate(currencyRates[i]);
        	tb.setProjectCode(projectCodes[i]);
        	
        	bean.getTempList().add(tb);
        }        
        //ִ���޸�
        Result rs = mgt.save(bean, true);

        forward = getForward(request, mapping, "alert");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //��ӳɹ�
            EchoMessage.success().add(getMessage(
                request, "common.msg.addSuccess"))
                .setBackUrl("/CertTemplateAction.do?operation="+OperationConst.OP_UPDATE_PREPARE+"&winCurIndex="+request.getParameter("winCurIndex")+"&id="+bean.getId()+"&refresh=true").
                setAlertRequest(request);
            new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		bean.getTempName(), "certificateBill", "ƾ֤ģ��","");
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
        ActionForward forward = getForward(request, mapping, "CertTemplateInfo");
        String tempNumber = request.getParameter("tempNumber");
        String locale = this.getLocale(request).toString();
        if (tempNumber != null && tempNumber.length() != 0) {
            
            //ִ�в�ѯǰ�ļ���
            Result rs = mgt.detail(tempNumber);
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //���سɹ�
            	CertificateBillBean bean = (CertificateBillBean)rs.retVal;
            	//�ѽ���ժҪ�����������
            	for(CertificateTemplateBean tb :bean.getTempList()){
            		if(tb.getComment() != null &&tb.getComment().length() > 0){ 
            			//����ֶ�
	            		Pattern pattern = Pattern.compile("([\\w]+\\.[\\w]+)",Pattern.CASE_INSENSITIVE);
						Matcher matcher = pattern.matcher(tb.getComment());
						String rstr=tb.getComment();
						while (matcher.find()) {
							String f = matcher.group(1);
							DBFieldInfoBean fb =GlobalsTool.getFieldBean(f);
							if(fb != null){
								String fn = fb.getDisplay().get(locale);
								if(!fb.getTableBean().getTableName().equals(bean.getTableName())){
									//��ϸ��Ҫ���ϱ���
									fn = fb.getTableBean().getDisplay().get(locale)+"_"+fn;
								}
								fn = "["+fn+"]";
								rstr = rstr.replaceAll(f, fn);	
							}
						}
						tb.setComment(rstr);
            		}
            		if(tb.getFieldName() != null &&tb.getFieldName().length() > 0){ 
            			//����ֶ�
	            		Pattern pattern = Pattern.compile("([\\w]+\\.[\\w]+)",Pattern.CASE_INSENSITIVE);
						Matcher matcher = pattern.matcher(tb.getFieldName());
						String rstr=tb.getFieldName();
						while (matcher.find()) {
							String f = matcher.group(1);
							DBFieldInfoBean fb =GlobalsTool.getFieldBean(f);
							if(fb != null){
								String fn = fb.getDisplay().get(locale);
								if(!fb.getTableBean().getTableName().equals(bean.getTableName())){
									//��ϸ��Ҫ���ϱ���
									fn = fb.getTableBean().getDisplay().get(locale)+"_"+fn;
								}
								fn = "["+fn+"]";
								rstr = rstr.replaceAll(f, fn);	
							}
						}
						tb.setFieldName(rstr);
            		}
            		
            		if(tb.getCurFieldName() != null &&tb.getCurFieldName().length() > 0){ 
            			//����ֶ�
	            		Pattern pattern = Pattern.compile("([\\w]+\\.[\\w]+)",Pattern.CASE_INSENSITIVE);
						Matcher matcher = pattern.matcher(tb.getCurFieldName());
						String rstr=tb.getCurFieldName();
						while (matcher.find()) {
							String f = matcher.group(1);
							DBFieldInfoBean fb =GlobalsTool.getFieldBean(f);
							if(fb != null){
								String fn = fb.getDisplay().get(locale);
								if(!fb.getTableBean().getTableName().equals(bean.getTableName())){
									//��ϸ��Ҫ���ϱ���
									fn = fb.getTableBean().getDisplay().get(locale)+"_"+fn;
								}
								fn = "["+fn+"]";
								rstr = rstr.replaceAll(f, fn);	
							}
						}
						tb.setCurFieldName(rstr);
            		}
            	}
                request.setAttribute("result", bean);
                //��������
                request.setAttribute("tableList", mgt.queryModuleType(this.getLocale(request).toString()));
                
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
    	String locale = this.getLocale(request).toString();

        ActionForward forward = getForward(request, mapping, "alert");
        CertificateBillBean bean  = new CertificateBillBean();
        bean.setCredTypeID(request.getParameter("credTypeID"));
        bean.setId(request.getParameter("id"));
        bean.setPopupName(request.getParameter("popupName"));
        bean.setTableName(request.getParameter("tableName"));
        bean.setTempName(request.getParameter("tempName"));
        bean.setTempNumber(request.getParameter("tempNumber"));
        bean.setType(request.getParameter("type"));
        
        
        String[] comments = request.getParameterValues("comment");
        String[] dircs = request.getParameterValues("dirc");
        String[] accCodes = request.getParameterValues("accCode");
        String[] fieldNames = request.getParameterValues("fieldName");
        String[] curFieldNames = request.getParameterValues("curFieldName");
        String[] companyCodes = request.getParameterValues("companyCode");
        String[] departmentCodes = request.getParameterValues("departmentCode");
        String[] employeeIDs = request.getParameterValues("employeeID");
        String[] stockCodes = request.getParameterValues("stockCode");
        String[] goodsCodes = request.getParameterValues("goodsCode");
        String[] currencys = request.getParameterValues("currency");
        String[] currencyRates = request.getParameterValues("currencyRate");
        String[] projectCodes = request.getParameterValues("projectCode");
        bean.setTempList(new ArrayList());
        for(int i=0;i<dircs.length;i++){
        	if(accCodes[i]==null||accCodes[i].length()==0){
        		 EchoMessage.error().add("��Ŀ����Ϊ��").setAlertRequest(
        	                    request);
        		 return forward;
        	}
        	if(fieldNames[i]==null||fieldNames[i].length()==0){
	       		 EchoMessage.error().add("��ʽ����Ϊ��").setAlertRequest(
	       	                    request);
	       		return forward;
	       	}
        	
        	CertificateTemplateBean tb = new CertificateTemplateBean();
        	tb.setId(IDGenerater.getId());
        	tb.setTableName(bean.getTableName());
        	tb.setAccCode(accCodes[i]);
        	tb.setDirc(Integer.parseInt(dircs[i]));
        	
        	//��ժҪ���н���
        	Pattern pattern = Pattern.compile("\\[([^\\[\\]]*_|.{0})([^\\[\\]]+)\\]",Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(comments[i]);
			String rstr=comments[i];
			while (matcher.find()) {
				String all = matcher.group();
				String ctnd = matcher.group(1);
				String cfnd = matcher.group(2);
				String ctn="",cfn="";
				DBTableInfoBean tBean=null;
				if(ctnd==null || ctnd.length()==0){
					//����
					ctn = bean.getTableName();
					tBean = GlobalsTool.getTableInfoBean(ctn);
				}
				
				if(ctn==null || ctn.length() ==0){
					ctnd = ctnd.substring(0,ctnd.length() -1);
					ArrayList<DBTableInfoBean> ctlist = DDLOperation.getChildTables(bean.getTableName(), BaseEnv.tableInfos);
					for(DBTableInfoBean ctb:ctlist){
						if(ctb.getDisplay().get(locale).equals(ctnd)){
							ctn = ctb.getTableName();
							tBean = ctb;
						}
					}
				}
				
				if(tBean==null){
					EchoMessage.error().add("ժҪ����������["+ctnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}
				for(DBFieldInfoBean cfb:tBean.getFieldInfos()){
					if(cfb.getDisplay().get(locale).equals(cfnd)){
						cfn = cfb.getFieldName();
					}
				}
				if(cfn==null||cfn.length() ==0){
					EchoMessage.error().add("ժҪ�ֶβ�����["+cfnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}			
				String after = rstr.substring(rstr.indexOf(all)+all.length());
				rstr = rstr.substring(0,rstr.indexOf(all))+ctn+"."+cfn+(after.startsWith(" ")?after:" "+after);	
			}
			//��������Ƿ���δ��Ե�[]
			if(rstr.indexOf("[")> -1 || rstr.indexOf("]") > -1){
				EchoMessage.error().add("ժҪ�ֶθ�ʽ����ȷ"+comments[i]+"").setAlertRequest(
   	                    request);
				return forward;
			}
			tb.setComment(rstr);
        	//�Թ�ʽ���н���
        	pattern = Pattern.compile("\\[([^\\[\\]]*_|.{0})([^\\[\\]]+)\\]",Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(fieldNames[i]);
			rstr=fieldNames[i];
			String ttn="";
			while (matcher.find()) {
				String all = matcher.group();
				String ctnd = matcher.group(1);
				String cfnd = matcher.group(2);
				String ctn="",cfn="";
				DBTableInfoBean tBean=null;
				if(ctnd==null || ctnd.length()==0){
					//����
					ctn = bean.getTableName();
					tBean = GlobalsTool.getTableInfoBean(ctn);
				}
				
				if(ctn==null || ctn.length() ==0){
					ctnd = ctnd.substring(0,ctnd.length() -1);
					ArrayList<DBTableInfoBean> ctlist = DDLOperation.getChildTables(bean.getTableName(), BaseEnv.tableInfos);
					for(DBTableInfoBean ctb:ctlist){
						if(ctb.getDisplay().get(locale).equals(ctnd)){
							ctn = ctb.getTableName();
							tBean = ctb;
						}
					}
				}
				
				if(tBean==null){
					EchoMessage.error().add("��ʽ����������["+ctnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}
				
				if(ttn.length()==0){
					ttn = ctn;
				}else if(!ttn.equals(ctn)){
//					EchoMessage.error().add("��ʽ�������ͬһ����ȡ��").setAlertRequest(
//       	                    request);
//					return getForward(request, mapping, "alert");
				}
				
				for(DBFieldInfoBean cfb:tBean.getFieldInfos()){
					if(cfb.getDisplay().get(locale).equals(cfnd)){
						cfn = cfb.getFieldName();
					}
				}
				if(cfn==null||cfn.length() ==0){
					EchoMessage.error().add("����ֶβ�����["+cfnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}			
				String after = rstr.substring(rstr.indexOf(all)+all.length());
				rstr = rstr.substring(0,rstr.indexOf(all))+ctn+"."+cfn+(after.startsWith(" ")?after:" "+after);	
			}
			//��������Ƿ���δ��Ե�[]
			if(rstr.indexOf("[")> -1 || rstr.indexOf("]") > -1){
				EchoMessage.error().add("����ֶθ�ʽ����ȷ"+fieldNames[i]+"").setAlertRequest(
   	                    request);
				return forward;
			}
			
        	tb.setFieldName(rstr);
        	
        	//����ҹ�ʽ���н���
        	pattern = Pattern.compile("\\[([^\\[\\]]*_|.{0})([^\\[\\]]+)\\]",Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(curFieldNames[i]);
			rstr=curFieldNames[i];
			ttn="";
			while (matcher.find()) {
				String all = matcher.group();
				String ctnd = matcher.group(1);
				String cfnd = matcher.group(2);
				String ctn="",cfn="";
				DBTableInfoBean tBean=null;
				if(ctnd==null || ctnd.length()==0){
					//����
					ctn = bean.getTableName();
					tBean = GlobalsTool.getTableInfoBean(ctn);
				}
				
				if(ctn==null || ctn.length() ==0){
					ctnd = ctnd.substring(0,ctnd.length() -1);
					ArrayList<DBTableInfoBean> ctlist = DDLOperation.getChildTables(bean.getTableName(), BaseEnv.tableInfos);
					for(DBTableInfoBean ctb:ctlist){
						if(ctb.getDisplay().get(locale).equals(ctnd)){
							ctn = ctb.getTableName();
							tBean = ctb;
						}
					}
				}
				if(tBean==null){
					EchoMessage.error().add("��ҽ�ʽ����������["+ctnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}
				if(ttn.length()==0){
					ttn = ctn;
				}
//				else if(!ttn.equals(ctn)){
//					EchoMessage.error().add("��ҽ�ʽ�������ͬһ����ȡ��").setAlertRequest(
//       	                    request);
//					return getForward(request, mapping, "alert");
//				}
				
				for(DBFieldInfoBean cfb:tBean.getFieldInfos()){
					if(cfb.getDisplay().get(locale).equals(cfnd)){
						cfn = cfb.getFieldName();
					}
				}
				if(cfn==null||cfn.length() ==0){
					EchoMessage.error().add("��ҽ���ֶβ�����["+cfnd+"]").setAlertRequest(
       	                    request);
					return forward;
				}			
				String after = rstr.substring(rstr.indexOf(all)+all.length());
				rstr = rstr.substring(0,rstr.indexOf(all))+ctn+"."+cfn+(after.startsWith(" ")?after:" "+after);	
			}
			//��������Ƿ���δ��Ե�[]
			if(rstr.indexOf("[")> -1 || rstr.indexOf("]") > -1){
				EchoMessage.error().add("��ҽ���ֶθ�ʽ����ȷ"+fieldNames[i]+"").setAlertRequest(
   	                    request);
				return forward;
			}
			
        	tb.setCurFieldName(rstr);
        	
        	tb.setCompanyCode(companyCodes[i]);
        	tb.setDepartmentCode(departmentCodes[i]);
        	tb.setEmployeeID(employeeIDs[i]);
        	tb.setStockCode(stockCodes[i]);
        	tb.setGoodsCode(goodsCodes[i]);
        	tb.setCurrency(currencys[i]);
        	tb.setCurrencyRate(currencyRates[i]);
        	tb.setProjectCode(projectCodes[i]);
        	
        	bean.getTempList().add(tb);
        }        
        //ִ���޸�
        Result rs = mgt.save(bean, false);

        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //�޸ĳɹ�
            EchoMessage.success().add(getMessage(
                request, "common.msg.updateSuccess")).setBackUrl("/CertTemplateAction.do?operation="+OperationConst.OP_UPDATE_PREPARE+"&winCurIndex="+request.getParameter("winCurIndex")+"&id="+bean.getId()).
                setAlertRequest(request);
            new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
            		bean.getTempName(), "certificateBill", "ƾ֤ģ��","");
        }  else {
            //�޸�ʧ��
            EchoMessage.error().add(getMessage(
                request, "common.msg.updateErro")).setAlertRequest(
                    request);
        }
        return forward;
    }

    /**
     * ɾ��
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
        String nstr = request.getParameter("id");
        if (nstr != null && nstr.length() != 0) {        	
            Result rs = mgt.delete(nstr);

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //ɾ���ɹ�
                request.setAttribute("result", rs.retVal);
                forward = query(mapping, form, request, response);
                if(rs.retVal != null){
                	new DynDBManager().addLog(2, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
                			rs.retVal.toString(), "certificateBill", "ƾ֤ģ��","");
                }
            } else {
                //ɾ��ʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.delError")).
                    setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;

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
    protected ActionForward getFieldList(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
        Exception {
    	String tableName = request.getParameter("tableName");
    	String locale = this.getLocale(request).toString();
    	String fieldStr = "";
    	DBTableInfoBean mtb= GlobalsTool.getTableInfoBean(tableName);
    	if(mtb != null){
	    	//�Ȳ鱾�ҽ���ֶ�
			for(DBFieldInfoBean fb:mtb.getFieldInfos()){
				if(fb.getInputType() != 100 && !fb.getFieldName().equals("id") &&  
						!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit") &&
						!fb.getFieldName().equals("BaseQty")&& !fb.getFieldName().equals("SecQty") &&
						!"priceIdentifier".equals(fb.getFieldIdentityStr())&&!"SPriceIdentifier".equals(fb.getFieldIdentityStr())&&
						fb.getInputType() != DBFieldInfoBean.INPUT_ENUMERATE && fb.getInputTypeOld() != DBFieldInfoBean.INPUT_ENUMERATE &&
						
						(fb.getFieldType()==DBFieldInfoBean.FIELD_INT||fb.getFieldType()==DBFieldInfoBean.FIELD_DOUBLE)){
					fieldStr +="<option>["+fb.getDisplay().get(locale)+"]</option>";
				}
			}
			for(DBTableInfoBean ctb :DDLOperation.getChildTables(tableName, BaseEnv.tableInfos)){
				for(DBFieldInfoBean fb:ctb.getFieldInfos()){
					if(fb.getInputType() != 100 && !fb.getFieldName().equals("id")&&!fb.getFieldName().equals("Unit") &&
							!fb.getFieldName().equals("BaseQty")&& !fb.getFieldName().equals("SecQty") &&							
							!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType()) &&
							!"priceIdentifier".equals(fb.getFieldIdentityStr())&&!"SPriceIdentifier".equals(fb.getFieldIdentityStr())&&
							fb.getInputType() != DBFieldInfoBean.INPUT_ENUMERATE && fb.getInputTypeOld() != DBFieldInfoBean.INPUT_ENUMERATE &&
							(fb.getFieldType()==DBFieldInfoBean.FIELD_INT||fb.getFieldType()==DBFieldInfoBean.FIELD_DOUBLE)){
						fieldStr +="<option>["+ctb.getDisplay().get(locale)+"_"+fb.getDisplay().get(locale)+"]</option>";
					}
				}
			}
			fieldStr +="|";
			//����ҽ���ֶ�
			for(DBFieldInfoBean fb:mtb.getFieldInfos()){
				if(fb.getInputType() != 100 && !fb.getFieldName().equals("id") &&  
						!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit") &&
						!fb.getFieldName().equals("BaseQty")&& !fb.getFieldName().equals("SecQty") &&
						!"priceIdentifier".equals(fb.getFieldIdentityStr())&&!"SPriceIdentifier".equals(fb.getFieldIdentityStr())&&
						fb.getInputType() != DBFieldInfoBean.INPUT_ENUMERATE && fb.getInputTypeOld() != DBFieldInfoBean.INPUT_ENUMERATE &&
						
						(fb.getFieldType()==DBFieldInfoBean.FIELD_INT||fb.getFieldType()==DBFieldInfoBean.FIELD_DOUBLE)){
					fieldStr +="<option>["+fb.getDisplay().get(locale)+"]</option>";
				}
			}
			for(DBTableInfoBean ctb :DDLOperation.getChildTables(tableName, BaseEnv.tableInfos)){
				for(DBFieldInfoBean fb:ctb.getFieldInfos()){
					if(fb.getInputType() != 100 && !fb.getFieldName().equals("id") &&  
							!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit") &&
							!"BaseQty".equals(fb.getFieldName())&& !"SecQty".equals(fb.getFieldName()) &&
							!"priceIdentifier".equals(fb.getFieldIdentityStr())&&!"SPriceIdentifier".equals(fb.getFieldIdentityStr())&&
							fb.getInputType() != DBFieldInfoBean.INPUT_ENUMERATE && fb.getInputTypeOld() != DBFieldInfoBean.INPUT_ENUMERATE &&
							
							(fb.getFieldType()==DBFieldInfoBean.FIELD_INT||fb.getFieldType()==DBFieldInfoBean.FIELD_DOUBLE)){
						fieldStr +="<option>["+ctb.getDisplay().get(locale)+"_"+fb.getDisplay().get(locale)+"]</option>";
					}
				}
			}
			fieldStr +="|";
	    	//�������Ŀ�ֶ�
			for(DBFieldInfoBean fb:mtb.getFieldInfos()){
				//$row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime" && "$row.fieldName"!="moduleType"
				if((fb.getInputType() != 100 && !fb.getFieldName().equals("id")   && 
						(fb.getFieldType()==DBFieldInfoBean.FIELD_ANY||fb.getFieldType()==DBFieldInfoBean.FIELD_ENGLISH) &&
						!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit")&&						
						(fb.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)) ||
						(fb.getFieldName().toLowerCase().indexOf("stockcode" )> -1 ||fb.getFieldName().toLowerCase().indexOf("projectcode" )> -1 
						|| fb.getFieldName().toLowerCase().indexOf("currency" )> -1|| fb.getFieldName().toLowerCase().indexOf("currencyrate" )> -1
						|| fb.getFieldName().toLowerCase().indexOf("goodscode" )> -1) 
				){
					fieldStr +=mtb.getTableName()+"."+fb.getFieldName()+":"+fb.getDisplay().get(locale)+";";
				}
			}
			for(DBTableInfoBean ctb :DDLOperation.getChildTables(tableName, BaseEnv.tableInfos)){
				for(DBFieldInfoBean fb:ctb.getFieldInfos()){
					if((fb.getInputType() != 100 && !fb.getFieldName().equals("id")   &&
							(fb.getFieldType()==DBFieldInfoBean.FIELD_ANY||fb.getFieldType()==DBFieldInfoBean.FIELD_ENGLISH) &&
							!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit")&&
							(fb.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)) ||
							(fb.getFieldName().toLowerCase().indexOf("stockcode" )> -1 ||fb.getFieldName().toLowerCase().indexOf("projectcode" )> -1 
									|| fb.getFieldName().toLowerCase().indexOf("currency" )> -1|| fb.getFieldName().toLowerCase().indexOf("currencyrate" )> -1
									|| fb.getFieldName().toLowerCase().indexOf("goodscode" )> -1) 
					){
						fieldStr +=ctb.getTableName()+"."+fb.getFieldName()+":"+ctb.getDisplay().get(locale)+"_"+fb.getDisplay().get(locale)+";";
					}
				}
			}
	    	//�Ȳ��Ŀ��Դ�ֶ�
			fieldStr +="|";
			for(DBFieldInfoBean fb:mtb.getFieldInfos()){
				//$row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime" && "$row.fieldName"!="moduleType"
				if((fb.getInputType() != 100 && !fb.getFieldName().equals("id")   && 
						(fb.getFieldType()==DBFieldInfoBean.FIELD_ANY||fb.getFieldType()==DBFieldInfoBean.FIELD_ENGLISH) &&
						!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit")&&
						!fb.getFieldName().equalsIgnoreCase("companyCode")&&!fb.getFieldName().equalsIgnoreCase("departmentCode")&&!fb.getFieldName().equalsIgnoreCase("employee")&&
						!fb.getFieldName().equalsIgnoreCase("stockCode")&&!fb.getFieldName().equalsIgnoreCase("currency")&&!fb.getFieldName().equalsIgnoreCase("projectCode")&&
						!fb.getFieldName().equalsIgnoreCase("employeeId"))
				){
					fieldStr +=mtb.getTableName()+"."+fb.getFieldName()+":"+fb.getDisplay().get(locale)+";";
				}
			}
			for(DBTableInfoBean ctb :DDLOperation.getChildTables(tableName, BaseEnv.tableInfos)){
				for(DBFieldInfoBean fb:ctb.getFieldInfos()){
					if((fb.getInputType() != 100 && !fb.getFieldName().equals("id")   && 
							(fb.getFieldType()==DBFieldInfoBean.FIELD_ANY||fb.getFieldType()==DBFieldInfoBean.FIELD_ENGLISH)  &&
							!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit")&&
							!fb.getFieldName().equalsIgnoreCase("companyCode")&&!fb.getFieldName().equalsIgnoreCase("departmentCode")&&!fb.getFieldName().equalsIgnoreCase("employee")&&
							!fb.getFieldName().equalsIgnoreCase("stockCode")&&!fb.getFieldName().equalsIgnoreCase("currency")&&!fb.getFieldName().equalsIgnoreCase("projectCode")&&
							!fb.getFieldName().equalsIgnoreCase("employeeId"))
					){
						if(fb.getFieldName().equalsIgnoreCase("GoodsCode")){
							fieldStr +="tblGoodsAccProp.AccCode:"+ctb.getDisplay().get(locale)+"_"+"��Ʒ��������Ŀ;";
							fieldStr +="tblGoodsAccProp.IncomeAccCode:"+ctb.getDisplay().get(locale)+"_"+"��Ʒ���������Ŀ;";
							fieldStr +="tblGoodsAccProp.ExpendAccCode:"+ctb.getDisplay().get(locale)+"_"+"��Ʒ���۳ɱ���Ŀ;";
						}else{
							fieldStr +=ctb.getTableName()+"."+fb.getFieldName()+":"+ctb.getDisplay().get(locale)+"_"+fb.getDisplay().get(locale)+";";
						}
					}
				}
			}
			
	    	//ժҪ�ֶ�
			fieldStr +="|";		
			
			for(DBFieldInfoBean fb:mtb.getFieldInfos()){
				if(!fb.getFieldName().equals("id") &&  
						!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit") &&
						!fb.getFieldName().equals("BaseQty")&& !fb.getFieldName().equals("SecQty") && !fb.getFieldName().equals("workFlowNodeName") &&
						!fb.getFieldName().equals("workFlowNode") && !fb.getFieldName().equals("checkPersons") &&!fb.getFieldName().equals("finishTime") &&
						!"priceIdentifier".equals(fb.getFieldIdentityStr())&&!"SPriceIdentifier".equals(fb.getFieldIdentityStr())&&
						(fb.getInputTypeOld() != DBFieldInfoBean.INPUT_MAIN_TABLE
						||fb.getFieldName().indexOf("CompanyCode")>-1||fb.getFieldName().indexOf("GoodsCode")>-1||fb.getFieldName().indexOf("StockCode")>-1)
						){
					fieldStr +="<option>["+fb.getDisplay().get(locale)+"]</option>"; 
				}
			}
			for(DBTableInfoBean ctb :DDLOperation.getChildTables(tableName, BaseEnv.tableInfos)){
				for(DBFieldInfoBean fb:ctb.getFieldInfos()){
					if(!fb.getFieldName().equals("id") &&  
							!"GoodsField".equals(fb.getFieldSysType())&& !"AssUnit".equals(fb.getFieldSysType())&&!fb.getFieldName().equals("Unit") &&
							!fb.getFieldName().equals("BaseQty")&& !fb.getFieldName().equals("SecQty") && !fb.getFieldName().equals("workFlowNodeName") &&
							!fb.getFieldName().equals("workFlowNode") && !fb.getFieldName().equals("checkPersons") &&!fb.getFieldName().equals("finishTime") &&
							!"priceIdentifier".equals(fb.getFieldIdentityStr())&&!"SPriceIdentifier".equals(fb.getFieldIdentityStr())&&
							(fb.getInputTypeOld() != DBFieldInfoBean.INPUT_MAIN_TABLE
							||fb.getFieldName().indexOf("CompanyCode")>-1||fb.getFieldName().indexOf("GoodsCode")>-1||fb.getFieldName().indexOf("StockCode")>-1)
							){
						fieldStr +="<option>["+ctb.getDisplay().get(locale)+"_"+fb.getDisplay().get(locale)+"]</option>";
					}
				}
			}
			/*
			fieldStr +="<option>[���ݱ��]</option>";
			fieldStr +="<option>[��������]</option>";
			fieldStr +="<option>[����]</option>";
			fieldStr +="<option>[������]</option>";
			fieldStr +="<option>[������λ]</option>";
			fieldStr +="<option>[��Ʒ����]</option>";
			*/
    	}
		request.setAttribute("msg",fieldStr);
		
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
                                  HttpServletResponse response) throws
        Exception {
            //ִ�в�ѯ

            Result rs = mgt.query(null);

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //��ѯ�ɹ�
                request.setAttribute("result", rs.retVal);
            } else {
                //��ѯʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                return getForward(request, mapping, "message");
            }
        return getForward(request, mapping, "CertTemplate");
    }

  
}
