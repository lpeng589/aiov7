package com.menyi.aio.web.finance.popupSelect;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
/**
 * 
 * 
 * <p>Title:�������õ������е�����Action</p> 
 * <p>Description: </p>
 *
 * @Date:2013-05-22
 * @Copyright: �������
 * @Author ���ҿ�
 */
public class PopupAction extends MgtBaseAction{

	PopupMgt mgt = new PopupMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		//���ݲ�ͬ�������ͷ������ͬ��������
		String operation = request.getParameter("operation");
		ActionForward forward = null;
		String popupName = request.getParameter("popupName");		//���������ƣ���ƿ�Ŀ������
		request.setAttribute("popupName", popupName);
		if("popDetail".equals(operation)){
			//�ұߵ�����
			if(popupName != null && !"".equals(popupName)){
				if("popAccTypeInfo".equals(popupName)){
					forward = popupDetailAcc(mapping, form, request, response);
				}
			}
		}else if("ajaxPopDetail".equals(operation)){
			//ajaxֱ�Ӳ�ѯ��Ŀ��Ϣ
			forward = ajaxPopDetail(mapping, form, request, response);
		}else{
			//��ҳ
			if(popupName != null && !"".equals(popupName)){
				if("popAccTypeInfo".equals(popupName)){
					forward = indexAcc(mapping, form, request, response);
				}
			}
		}
		return forward;
	}
	
	/**
	 * ��������ҳ���������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward indexAcc(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		
		PopupSearchForm searchForm = (PopupSearchForm)form;
		searchForm.setPageSize(1000);
		
		/* Ȩ�޿��� */
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(loginBean.getAllScopeRight());
		String scopeRightSql = DynDBManager.scopeRightHandler("tblAccTypeInfo", "TABLELIST", "", loginBean.getId(), scopeRight, "select * from tblAccTypeInfo where 1=1 ", "endClass","");
		scopeRightSql = scopeRightSql.substring(scopeRightSql.indexOf("where 1=1")+"where 1=1".length());

		if(searchForm.getSelectValue()!=null && !"".equals(searchForm.getSelectValue())){
			try {
				searchForm.setSelectValue(java.net.URLDecoder.decode(searchForm.getSelectValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Result rs = mgt.queryDataAcc(searchForm, scopeRightSql);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ��ѯ�ɹ�
			request.setAttribute("accMainList", rs.retVal);
			ArrayList list = (ArrayList) rs.retVal;
			StringBuffer folderTree = new StringBuffer("[");
			for (int i = 0; i < list.size(); i++) {
				HashMap map = (HashMap) list.get(i);
				String classCodes = String.valueOf(map.get("classCode"));
				if (folderTree.toString().indexOf("id:\"" + classCodes.substring(0, classCodes.length() - 5) + "\"") == -1) {
					folderTree.append(this.folderTree(list, String.valueOf(map.get("AccNumber")), String.valueOf(map.get("AccName") ), classCodes, Integer
							.valueOf(map.get("countnumber").toString()), searchForm.getChooseType()));
				}
			}
			folderTree = new StringBuffer(folderTree.toString().replaceAll("\\}\\{", "},{"));
			folderTree.append("]");
			request.setAttribute("folderTree", folderTree.toString());
		}
		return getForward(request, mapping, "indexPop");
	}
	
	/**
	 * ��֯����
	 * @param list
	 * @param accNumber
	 * @param folderName
	 * @param classCode
	 * @param isCatalog
	 * @param selectType
	 * @return
	 */
	private String folderTree(ArrayList list, String accNumber, String folderName, String classCode, Integer isCatalog, String chooseType) {
		folderName = folderName.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		StringBuffer folderTree = new StringBuffer("");
		if (isCatalog != null && isCatalog > 0) {
			// �����¼�
			folderTree.append("{ id:\"" + classCode + "\",tname:\"" + accNumber + ";" + folderName + ";|" + "\",name:\"" + accNumber + " - " + folderName
					+ "\",nocheck:true,nodes:[");
			for (int i = 0; i < list.size(); i++) {
				HashMap map = (HashMap) list.get(i);
				String classCodes = String.valueOf(map.get("classCode"));
				if (classCodes.substring(0, classCodes.length() - 5).equals(classCode)) {
					folderTree.append(this.folderTree(list, String.valueOf(map.get("AccNumber")), String.valueOf(map.get("AccName")), classCodes, Integer
							.valueOf(map.get("countnumber").toString()), chooseType));
				}
			}
			folderTree.append("]}");
		} else {
			folderTree.append("{ id:\"" + classCode + "\",tname:\"" + accNumber + ";" + folderName + ";|" + "\",name:\"" + accNumber + " - " + folderName
					+ "\",nodes: []");
			if ("all".equals(chooseType)) {
				folderTree.append(",nocheck:true");
			}
			folderTree.append("}");
		}
		return folderTree.toString();
	}
	
	/**
	 * ��ϸ���ұ���ʾ��Ϣ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward popupDetailAcc(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		
		PopupSearchForm searchForm = (PopupSearchForm)form;
		/* Ȩ�޿��� */
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(loginBean.getAllScopeRight());
		String scopeRightSql = DynDBManager.scopeRightHandler("tblAccTypeInfo", "TABLELIST", "", loginBean.getId(), scopeRight, "select * from tblAccTypeInfo where 1=1 ", "endClass","");
		scopeRightSql = scopeRightSql.substring(scopeRightSql.indexOf("where 1=1")+"where 1=1".length());

		Result rs = mgt.queryDataAcc(searchForm, scopeRightSql);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("detailList", rs.retVal);
			request.setAttribute("pageBar", this.pageBar(rs, request));
		}
		return getForward(request, mapping, "detailPop");
	}
	
	/**
	 * ajaxֱ�Ӳ�ѯ��Ŀ��Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward ajaxPopDetail(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		
		PopupSearchForm searchForm = (PopupSearchForm)form;
		searchForm.setSelectType("keyword");
		ArrayList allScopeRightList = this.getLoginBean(request).getAllScopeRight();
		if(searchForm.getSelectValue()!=null && !"".equals(searchForm.getSelectValue())){
			try {
				searchForm.setSelectValue(java.net.URLDecoder.decode(searchForm.getSelectValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Result rs = mgt.ajaxPopAccType(searchForm, allScopeRightList);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List list = (ArrayList)rs.retVal;
			if(list!= null && list.size() == 1){
				//����һ������ʱ��ֱ��ȡ���� ��ʽΪ��AccNumber;AccFullName;|
				HashMap map = (HashMap)list.get(0);
				String msg = map.get("AccNumber")+"#;#"+map.get("AccFullName")+"#;##|#";
				request.setAttribute("msg", msg);
			}
		}
		return getForward(request, mapping, "blank");
	}
	
	
	
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		
		int operation = getParameterInt("operation", req) ;
		if(operation == 0){
			return null ;
		}
		return super.doAuth(req, mapping);
	}
}
