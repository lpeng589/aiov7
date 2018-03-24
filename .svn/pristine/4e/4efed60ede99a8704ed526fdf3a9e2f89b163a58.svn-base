package com.koron.crm.clientTransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
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
public class CRMClientTransferAction extends BaseAction{
	CRMClientTransferMgt ctmgt = new CRMClientTransferMgt();
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);		
		String type1 = request.getParameter("flag");
		ActionForward forward = null;
		//先进行填充下拉框所有生命周期，所有客户类型，所有状态，所有处理结果	
		List listStatus = getEnumerationItems("ClientStatus", request);
		List listStyle = getEnumerationItems("ClientStyle", request);
		List listSettle = getEnumerationItems("SettleStatus", request);
		List listReceive = getEnumerationItems("ReceiveStatus", request);
		List rs = fillmune(listStatus);
		request .setAttribute("listStatus", fillmune(listStatus));
		request .setAttribute("listStyle", fillmune(listStyle));
		request .setAttribute("listSettle", fillmune(listSettle));
		request .setAttribute("listReceive", fillmune(listReceive));
		
		switch (operation) {
		case OperationConst.OP_ADD:      	
            forward = insertResult(mapping, form, request, response);		           						
        	break;
        case OperationConst.OP_QUERY:
        	String type = request.getParameter("flay");
			if(null != type && !"".equals(type) && "detail".equals(type)){
            	forward = queryDetail(mapping, form, request, response);
            }else{
            	forward = queryTransfer(mapping, form, request, response);		
            }						
        	break;
		//默认
		default:	
				forward = queryTransfer(mapping, form, request, response); 
		}
		return forward;
	}
	
	protected ActionForward insertResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{	
				String flag = request.getParameter("flag");
			 	String keyId = getParameter("keyId", request);			 	 			 	
			 	String[] keyIds = keyId!=null?keyId.split(","):null;			 				 	
				HashMap values = new HashMap();
				LoginBean loginBean = getLoginBean(request);
				Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				MessageResources resources = null;
				if (ob instanceof MessageResources) {
				    resources = (MessageResources) ob;
				}
				String sqlName = "";
				if(flag.equals("1")){
					sqlName ="CRMClientTransfer_Receive";
				}
				if(flag.equals("2")){
					sqlName ="CRMClientTransfer_Reject";
				}
				
				Result rs = ctmgt.insertResult(allTables,keyIds, loginBean, resources, getLocale(request),sqlName);
				
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					request.setAttribute("msg","操作成功");
					
				}else {
		       	 	//添加失败
		        	SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
	            	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
						EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
					} else {
						EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
					}
		        }		
				return getForward(request, mapping, "blank");
	}
	
	protected ActionForward queryTransfer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{			
			CRMClientTransferForm lvForm = (CRMClientTransferForm)form;	
			LoginBean loginBean = getLoginBean(request);
			request.setAttribute("loginId",loginBean.getId());
			request.setAttribute("transMan",lvForm.getTransMan());
			request.setAttribute("transferTo",lvForm.getTransferTo());
			request.setAttribute("clientName",lvForm.getClientName());
			request.setAttribute("endTime",lvForm.getEndTime());
			request.setAttribute("beginTime",lvForm.getBeginTime());
			request.setAttribute("audit", lvForm.getAudit());
			String transferManId = this.getIdByName(lvForm.getTransMan());
			String transferToId = this.getIdByName(lvForm.getTransferTo());
			lvForm.setTransMan(transferManId !=null && transferManId !=""?transferManId:lvForm.getTransMan());
			lvForm.setTransferTo(transferToId !=null && transferToId !=""?transferToId:lvForm.getTransferTo());			
			String sortInfo  = getParameter("sortInfo", request) ;//排序信息 排序标识,排序名
			HashMap<String,String> conMap = (HashMap) request.getAttribute("conMap") ;
			if(conMap == null){
				conMap = new HashMap<String, String>() ;
			}
			conMap.put("sortInfo", sortInfo) ;
			request.setAttribute("conMap", conMap) ;			
			String loginId = loginBean.getId();
			Result rs = ctmgt.queryClientInfo(lvForm,loginId,null,sortInfo);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("logList", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
				lvForm.reset(mapping, request);
			}
			request.setAttribute("clientStatus", lvForm.getClientStatus());
			request.setAttribute("clientStyle", lvForm.getClientStyle());
			request.setAttribute("statusId", lvForm.getStatusId());			
			return getForward(request, mapping, "clientTransfer");								
			
	}
	protected ActionForward queryDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{			
			CRMClientTransferForm lvForm = (CRMClientTransferForm)form;
			String id = "'"+request.getParameter("id")+"'";
			String loginId = this.getLoginBean(request).getId();
			Result rs = ctmgt.queryClientInfo(lvForm,loginId,id,null);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("logList", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
			}	
			return getForward(request, mapping, "detail");								
			
	}
	/**
	 * 枚举填充数据
	 * @param list
	 * @return
	 */
	protected List fillmune(List list){
		ArrayList nList = new ArrayList();
		if(list.size()>0 && list !=null){
			for (int i = 0; i < list.size(); i++) {
				Object line = (Object)list.get(i);
				KeyPair obj = (KeyPair)line;
				Object[] ob = new Object[2];
				ob[1] = obj.getValue();
				ob[0] = obj.getName();							
				nList.add(ob);
			}
		}
		return nList;
	}
	public String getIdByName(String name){
		String fullname = "''";
		String id ="";
		if(name !=null && !"".equals(name)){
			/*String[] na = name.split(",");
			for (int i = 0; i < na.length; i++) {
				fullname += ",'"+na[i]+"'";
			}*/
			Result rs = ctmgt.getIdByName(name);
			ArrayList param = (ArrayList)rs.retVal;
			if(param !=null && param.size()>0){
				for (int i = 0; i < param.size(); i++) {
					Object nm = ((Object[])param.get(i))[0];
					id += ","+nm.toString();
				}				
			}
			return id.substring(1);
		}else{
			return id;
		}		
		
	}


	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}
	
}
