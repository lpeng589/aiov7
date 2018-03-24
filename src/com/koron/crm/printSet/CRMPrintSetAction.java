package com.koron.crm.printSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.tools.generic.MathTool;

import com.dbfactory.Result;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.koron.crm.setting.ClientSetingMgt;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:客户移交</p> 
 * <p>Description: </p>
 *
 * @Date:Oct 23, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author wyy
 */
public class CRMPrintSetAction extends BaseAction{
	CRMPrintSetMgt mgt = new CRMPrintSetMgt();
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean login = getLoginBean(req);
		if(login == null){
			BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
		}
		//进行唯一用户验证，如果有生复登陆的，则后进入用户踢出前进入用户
        if (!OnlineUserInfo.checkUser(req)) {
            //需踢出
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }
		return null;
	}

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
	
		int operation = getOperation(request);				
		ActionForward forward = null;			
		switch (operation) {		
		case OperationConst.OP_ADD:
			String addFlag = request.getParameter("addFlag");
			if("copy".equals(addFlag)){
				forward = copySetPrint(mapping, form, request, response);	
			}else{
				forward = saveSetPrint(mapping, form, request, response);	
			}
			
			break;
		//打印前操作
		case OperationConst.OP_URL_TO:
			//客户详情出打印设置
			forward = prePrint(mapping, form, request, response);	
			break;
		//打印操作
		case OperationConst.OP_PRINT:
			forward = crmPrint(mapping, form, request, response);	
			break;
		case OperationConst.OP_QUOTE:
			//打印预览时的提示
			forward = quoteSetPrint(mapping, form, request, response);	
			break;
		case OperationConst.OP_UPDATE:			
			forward = updateSetting(mapping, form, request, response);							
			break;
		case OperationConst.OP_DELETE:
			forward = dleSetPrint(mapping, form, request, response);	
			break;
        case OperationConst.OP_QUERY:   
        	String type = request.getParameter("styleFlag");
        	if("NEW".equals(type)){
        		//打印设置 新增和跟新钱
        		forward = preSetPrint(mapping, form, request, response);
        	}else if("PREVIEW".equals(type)){
        		//打印标尺和打印数据
        		forward = previewPrint(mapping, form, request, response);
        	}else if("TIAOZ".equals(type)){
        		//快递单打印模板设置方法说明
        		forward = viewPrint(mapping, form, request, response);
        	}else if("CONNET".equals(type)){
        		//根据模板Id获取该模板下的视图
        		forward = getView(mapping, form, request, response);
        	}else if("Filed".equals(type)){
        		//根据模板视图Id获取该模板视图所有字段
        		forward = getViewFiled(mapping, form, request, response);
        	}else{
        		forward = queryPrint(mapping, form, request, response);	
        	}       		           					
        	break;
		//默认
		default:	
				forward = queryPrint(mapping, form, request, response); 
		}
		return forward;
	}

	private ActionForward copySetPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");
		if(id !=null && !"".equals(id)){
			CRMPrintSetBean bean = (CRMPrintSetBean)mgt.loadPrintSet(id).retVal;
			bean.setId(IDGenerater.getId());
			bean.setStatus("0");
			bean.setCreateBy(getLoginBean(request).getId());
			
			Result printSet = mgt.savePrintSet(bean);
			if(printSet.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg", bean.getId());
			}
		}
		return getForward(request, mapping, "blank");
	}

	private ActionForward getViewFiled(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String moduleId = request.getParameter("moduleId");
		String moduleViewId = request.getParameter("moduleViewId");
		if(moduleViewId !=null && !"".equals(moduleViewId)){
			ClientModuleBean moduleBean = (ClientModuleBean)mgt.loadModule(moduleId).retVal;
			ClientModuleViewBean viewBean = (ClientModuleViewBean)mgt.loadViewFiled(moduleViewId).retVal;
			if(viewBean != null){
				request.setAttribute("viewBean", viewBean);			
				request.setAttribute("tableName", moduleBean.getTableInfo().split(":")[0]);
				request.setAttribute("contectTable", moduleBean.getTableInfo().split(":")[1]);
			}
		}
		return getForward(request, mapping, "selectFile");
	}

	private ActionForward getView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String moduleId = request.getParameter("moduleId");
		Result rs = mgt.getView(moduleId);
		ArrayList rsArr = (ArrayList)rs.retVal;		
		if(rsArr!=null && rsArr.size()>0){
			String msg = "<select name='ref_moduleViewId' id='ref_moduleViewId'>";
			for (int i = 0; i < rsArr.size(); i++) {
				msg+="<option value="+((Object[])rsArr.get(i))[0].toString()+">"+((Object[])rsArr.get(i))[1].toString()+"</option>";
			}
			msg += "</select>";
			request.setAttribute("msg", msg);
		}
		return getForward(request, mapping, "blank");
	}

	private ActionForward viewPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{	
		return getForward(request, mapping, "printDSetting");
	}

	private ActionForward crmPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setAttribute("flag_print", "true");
		String id = request.getParameter("mouldName");
		String moduleId = request.getParameter("moduleId");
		String moduleViewId = request.getParameter("moduleViewId");
		String contectId = request.getParameter("userName");
		//ArrayList rsDet = new ArrayList();
		HashMap<String, String> mapList = new HashMap<String, String>();
		if(contectId !=null && !"".equals(contectId)){			
			Result rs = mgt.getPtDet(contectId.split(";")[1]);
			mapList = (HashMap<String, String>) rs.retVal;
			//rsDet = (ArrayList)rs.retVal;
		}
		
		/*获取模板视图的参数名称*/
		HashMap<String, String> mapRs = new HashMap<String, String>();
		if(moduleId !=null && !"".equals(moduleId) && moduleViewId !=null && !"".equals(moduleViewId)){
			Result paramRs = mgt.getViewParam(moduleId, moduleViewId);
			ArrayList paramArr = (ArrayList)paramRs.retVal;
			if(paramArr !=null && paramArr.size()>0){				
				String tableName = ((Object[])paramArr.get(0))[0]==null?"":((Object[])paramArr.get(0))[0].toString();
				String fieldNames = ((Object[])paramArr.get(0))[1]==null?"":((Object[])paramArr.get(0))[1].toString();
				for (int i = 0; i < fieldNames.split(",").length; i++) {	
					if(!"".equals(fieldNames.split(",")[i])){
						if(fieldNames.split(",")[i].indexOf("contact") == -1){
							String fieldName = GlobalsTool.getFieldBean(tableName.split(":")[0], fieldNames.split(",")[i]).getDisplay().get(GlobalsTool.getLocale(request).toString());
							mapRs.put(fieldName,fieldNames.split(",")[i]);
						}else{
							String fieldName = GlobalsTool.getFieldBean(tableName.split(":")[1], fieldNames.split(",")[i].replace("contact","")).getDisplay().get(GlobalsTool.getLocale(request).toString());
							mapRs.put(fieldName,fieldNames.split(",")[i]);
						}						
					}			
				}							
			}
			
		}
				
		//load 
		if(id!=null && !"".equals(id)){
			CRMPrintSetBean bean = (CRMPrintSetBean)mgt.getPrintSet(id).retVal;		
			String param = bean.getSettingDetail();					
			//获取参数
			String[] paramRet = param.split(";");		
			int i=0;
			for (String key : paramRet) {
				if(key.indexOf("【")>-1){
					String str = paramRet[i].split("【")[1];					
					String keyName = mapRs.get(str.split("】")[0]);
					if(key.indexOf("目的地")>-1){
						paramRet[i] = paramRet[i].split("【")[0]+mapList.get("City");
					}else if(key.indexOf("省份")>-1){
						paramRet[i] = paramRet[i].split("【")[0]+mapList.get("Province");
					}else if(key.indexOf("城市")>-1){
						paramRet[i] = paramRet[i].split("【")[0]+mapList.get("City");
					}else if(key.indexOf("区、县")>-1){
						paramRet[i] = paramRet[i].split("【")[0]+mapList.get("Area");
					}else if(key.indexOf("电话")>-1 || key.indexOf("手机")>-1){
						paramRet[i] = paramRet[i].split("【")[0]+mapList.get("Telephone")+"     "+mapList.get("Mobile");
					}else{
						if(keyName.indexOf("contact") == -1){
							paramRet[i] = paramRet[i].split("【")[0]+mapList.get(keyName);
						}else{
							paramRet[i] = paramRet[i].split("【")[0]+mapList.get(keyName.replace("contact", ""));
						}
					}															
				}	
				i++;
			}
			request.setAttribute("paramRet", paramRet);
		}		
		return getForward(request, mapping, "printPreview");
	}

	private ActionForward prePrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		//获取客户详细信息
		String idSr = request.getParameter("id");
		String moduleId = "";
		String moduleViewId = "";
		if(idSr !=null && !"".equals(idSr) && !"undefined".equals(idSr)){
			String id = idSr.split(":")[1];
			Result rs = mgt.getPrintDet(id);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList rsRs = (ArrayList)rs.getRetVal();			
				moduleId = GlobalsTool.get(rsRs.get(0),12).toString();
				Result res = mgt.getView(moduleId);	
				ArrayList resRs = (ArrayList)res.retVal;
				moduleViewId= GlobalsTool.get(resRs.get(0),0).toString();
			}
			
			request.setAttribute("UserDet", rs.retVal);
		}
		Result rs = mgt.byPrintSet(getLoginBean(request).getId(),moduleId,moduleViewId);
		
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("mouldName", rs.retVal);
		}
		
		return getForward(request, mapping, "prePrintSet");
	}

	private ActionForward quoteSetPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{		
		return getForward(request, mapping, "printKnows");
	}

	private ActionForward updateSetting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{			
		String id = request.getParameter("id");
		CRMPrintSetBean bean = (CRMPrintSetBean)mgt.getPrintSet(id).retVal;
		String[] pName = getParameters("pName", request);//内容块名称
		String[] xAxle = getParameters("nameX", request);//x轴
		String[] yAxle = getParameters("nameY", request);//y轴
		String[] widthX = getParameters("widthX", request);//宽度
		String[] heightY = getParameters("heightY", request);//高度
		String[] fontSize = getParameters("fontSize", request);//字体大小
		String[] context = getParameters("context", request);//内容
		String ref_moduleId = getParameter("ref_moduleId", request);
		String ref_moduleViewId = getParameter("ref_moduleViewId", request);
		StringBuffer str1=new StringBuffer();
		for (int i = 0; i < 20; i++) {
			if(pName[i] != null && !"".equals(pName[i])){
				str1.append(pName[i]);
				str1.append(","+xAxle[i]);
				str1.append(","+yAxle[i]);
				str1.append(","+widthX[i]);
				str1.append(","+heightY[i]);
				str1.append(","+fontSize[i]);
				str1.append(","+context[i]+";");
			}
		}						
		String name = getParameter("name", request);//快递单名称
		if(name !=null && !"".equals(name)){				
			bean.setModuleName(name);			
			bean.setSettingDetail(str1.toString());
			bean.setRef_moduleId(ref_moduleId);
			bean.setRef_moduleViewId(ref_moduleViewId);
			Result rs = mgt.updatePrintSet(bean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(new DynDBManager().getDefSQLMsg(
						getLocale(request).toString(), "更新成功")).setBackUrl("/CRMPrintSetAction.do?operation=4&styleFlag=NEW&flag=BYID&id="+id).setAlertRequest(request);							
				}			
		}		
		return getForward(request, mapping, "message");
	}

	private ActionForward dleSetPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String delId = getParameter("delId", request);
		Result rs = mgt.delPrintSet(delId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","1");		//删除成功		
		}else{
			request.setAttribute("msg","0");
		}
		return getForward(request, mapping, "blank");
	}

	private ActionForward saveSetPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		CRMPrintSetBean bean = new CRMPrintSetBean();
		String[] pName = getParameters("pName", request);//内容块名称
		String[] xAxle = getParameters("nameX", request);//x轴
		String[] yAxle = getParameters("nameY", request);//y轴
		String[] widthX = getParameters("widthX", request);//宽度
		String[] heightY = getParameters("heightY", request);//高度
		String[] fontSize = getParameters("fontSize", request);//字体大小
		String[] context = getParameters("context", request);//内容
		String ref_moduleId = getParameter("ref_moduleId", request);
		String ref_moduleViewId = getParameter("ref_moduleViewId", request);
		StringBuffer str1=new StringBuffer();
		for (int i = 0; i < 20; i++) {
			if(pName[i] != null && !"".equals(pName[i])){
				str1.append(pName[i]);
				str1.append(","+xAxle[i]);
				str1.append(","+yAxle[i]);
				str1.append(","+widthX[i]);
				str1.append(","+heightY[i]);
				str1.append(","+fontSize[i]);
				str1.append(","+context[i]+";");
			}			
		}						
		String name = getParameter("name", request);//快递单名称
		if(name !=null && !"".equals(name)){
			String id = IDGenerater.getId();			
			bean.setId(id);
			bean.setModuleName(name);		
			bean.setStatus("0");
			bean.setCreateBy(getLoginBean(request).getId());
			bean.setSettingDetail(str1.toString());
			bean.setRef_moduleId(ref_moduleId);
			bean.setRef_moduleViewId(ref_moduleViewId);
			Result rs = mgt.savePrintSet(bean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(getMessage(
		                request, "common.msg.addSuccess")).setBackUrl("/CRMPrintSetAction.do?operation=4&styleFlag=NEW&flag=BYID&id="+id).setAlertRequest(request);							
			}else{
				EchoMessage.error().add(new DynDBManager().getDefSQLMsg(
						getLocale(request).toString(), "保存失败")).setBackUrl("/CRMPrintSetAction.do?operation=4&styleFlag=NEW").setAlertRequest(request);	
			}
		}else{
			EchoMessage.error().add(new DynDBManager().getDefSQLMsg(
					getLocale(request).toString(), "名称为空")).setBackUrl("/CRMPrintSetAction.do?operation=4&styleFlag=NEW").setAlertRequest(request);	
		}								
		return getForward(request, mapping, "message");
	}

	private ActionForward previewPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		return getForward(request, mapping, "printPreview");
	}

	private ActionForward preSetPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String flag = getParameter("flag", request);
		if(flag !=null && "BYID".equals(flag)){
			String id = getParameter("id", request);
			CRMPrintSetBean bean = (CRMPrintSetBean)mgt.getPrintSet(id).retVal;
			String[] param = bean.getSettingDetail().split(";");		
			request.setAttribute("param", param);
			request.setAttribute("flagId", id);
			request.setAttribute("name", bean.getModuleName());
			request.setAttribute("moduleId", bean.getRef_moduleId());
			request.setAttribute("moduleViewId", bean.getRef_moduleViewId());
			//获取视图
			if(bean.getRef_moduleId() !=null && !"".equals(bean.getRef_moduleId())){
				Result res = mgt.getView(bean.getRef_moduleId());
				if(res.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("flagShow", "yes");
					request.setAttribute("viewList", res.retVal);	
				}				
			}
			
		}else{
			String[] param =new String[20];
			request.setAttribute("param", param);
		}
		//获取模板
		LoginBean loginBean = getLoginBean(request);
		Result rs = new ClientSetingMgt().getFilterModules(loginBean);
		request.setAttribute("moduleList", rs.retVal);
		
		
		return getForward(request, mapping, "printDetail");
	}

	private ActionForward queryPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{	
		Result rs = mgt.queryPrintSet(getLoginBean(request).getId());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("printList", rs.retVal);
		}
		return getForward(request, mapping, "printDesign");
	}

}
