package com.koron.crm.serviceConsole;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;


/**
 * 
 * <p>Title:CRM�ֵܱ�</p> 
 * <p>Description: </p>
 *
 * @Date:2013.10.8
 * @Copyright: �����п���������޹�˾
 * @Author ��ܿ�
 */
public class ServiceConsoleAction extends MgtBaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	ServiceConsoleMgt mgt = new ServiceConsoleMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// ���ܷ���
		String type = getParameter("type", request);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		
		switch (operation) {
			case OperationConst.OP_QUERY:
				if("keyWord".equals(type)){
					forward = keyWordQuery(mapping, form, request, response);
				}else if("relate".equals(type)){
					forward = relateInfoQuery(mapping, form, request, response);
				}else if("clientInfo".equals(type)){
					forward = clientInfoQuery(mapping, form, request, response);
				}else{
					forward = query(mapping, form, request, response);	
				}
				break;
			default:
				forward = query(mapping, form, request, response);
				break;	
		}
		return forward;
	}

	/**
	 * �����ͻ���Ϣ��ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward relateInfoQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String clientId = getParameter("clientId",request);
		String tableName = getParameter("tableName",request);
		String sql = "";
		if("CRMClientInfoDet".equals(tableName)){
			//��ϵ�˲�ѯ
			sql = "SELECT id,userName,Mobile,ClientEmail,mainUser FROM CRMClientInfoDet WHERE f_ref='"+clientId+"' order by mainUser";
		}else{
			sql = "SELECT id,topic,substring(createTime,0,11) FROM "+tableName+" WHERE clientId='"+clientId+"' order by createTime desc";
			
		}
		System.out.println(sql);
		ArrayList param = new ArrayList();
		Result rs = mgt.publicSqlQuery(sql, param);
		request.setAttribute("msg",gson.toJson(rs.retVal));
		return mapping.findForward("blank");
	}


	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("index");
	}
	
	/**
	 * ģ����ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward keyWordQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyWord = request.getParameter("keyWord")==null ? "" : request.getParameter("keyWord");//�ؼ���
		
		if("".equals(keyWord)){
			request.setAttribute("msg","error");
		}else{
			try {
				keyWord = URLDecoder.decode(keyWord,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			StringBuilder condition = new StringBuilder();//����
			LoginBean loginBean = getLoginBean(request);
			String userId = loginBean.getId();
			
			String keyWordSql = "and (clientName like '%"+keyWord+"%' or clientNo like '%"+keyWord+"%')"+condition;//ģ����ѯ�ͻ�
			String detMobileSql = "and CRMClientInfo.id in(SELECT f_ref FROM CRMClientInfoDet WHERE Mobile = '"+keyWord+"')"+condition;//������ϵ�˵绰��ѯ
			Result rs = mgt.queryClientByKeyWord(keyWordSql,false);
			ArrayList<String[]> clientList = (ArrayList<String[]>)rs.retVal;
			rs = mgt.queryClientByKeyWord(detMobileSql,true);
			ArrayList<String[]> detClientList = (ArrayList<String[]>)rs.retVal;
			request.setAttribute("clientList", clientList);
			request.setAttribute("detClientList", detClientList);
			return mapping.findForward("keyWordQuery");
		}
		return mapping.findForward("blank");
	}

	/**
	 * �ͻ���Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward clientInfoQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		return null;
	}

	
	
}