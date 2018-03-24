package com.menyi.aio.web.finance.voucher;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.*;

import org.apache.batik.dom.util.HashTable;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.customize.*;
import com.menyi.aio.web.importData.ExportField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:ƾ֤Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-03-13 ���� 11:30
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
public class VoucherAction extends MgtBaseAction {

	VoucherMgt mgt = new VoucherMgt();

	/**
	 * exe ��������ں���
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
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;

		String optype = request.getParameter("optype");

		// ���⣺���к��ָ�Ϊ������
		/*
		 * ע�⣺
		 * ��tblAccMain�ֶ�isAuditing�����Ƿ���ˣ�startδ��ˡ�finish����ˡ�noPass��˲�ͨ����,workFlowNodeName�����Ƿ���ˣ�notApproveδ���ˡ�finish�ѹ��ˣ�
		 */
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			// ����ǰ
			forward = addVoucherPre(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			if (optype != null && "addModule".equals(optype)) {
				// ����ƾ֤ģ��
				forward = addModule(mapping, form, request, response);
			} else {
				// ����ƾ֤��������ʱ��ӡƾ֤
				forward = addVoucher(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			// �޸�ƾ֤ǰ
			forward = updateVoucherPre(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			// �޸�ƾ֤
			forward = updateVoucher(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			if (optype != null && "exportVoucher".equals(optype)) {
				// �����б�
				forward = exportVoucher(mapping, form, request, response);
			} else if (optype != null && "voucherModule".equals(optype)) {
				// ��ѯƾ֤ģ��
				forward = voucherModuleList(mapping, form, request, response);
			} else {
				// ��ѯ�б�
				forward = queryVoucher(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DELETE:
			if (optype != null && "deleteModule".equals(optype)) {
				// ɾ��ƾ֤ģ��
				forward = deleteModule(mapping, form, request, response);
			} else {
				// ɾ��ƾ֤
				forward = deleteVoucher(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DETAIL:
			// ƾ֤����
			forward = detailVoucher(mapping, form, request, response);
			break;
		default:

			if (optype != null && !"".equals(optype)) {
				if (optype.equals("queryRecordComment")) {
					// ��ѯժҪ��
					forward = queryRecordComment(mapping, form, request, response);
				} else if(optype.equals("queryDescription")){
					//¼��ժҪʱ���в�ѯƥ������������ʾ���ı��������div��
					forward = queryDescription(mapping, form, request, response);
				} else if (optype.equals("addRecordComment")) {
					// ���ժҪ
					forward = addRecordComment(mapping, form, request, response);
				} else if (optype.equals("delRecordComment")) {
					// ɾ��ժҪ
					forward = delRecordComment(mapping, form, request, response);
				} else if (optype.equals("queryAccCode")) {
					// ��ѯ��ƿ�Ŀ(������)
					forward = queryAccCode(mapping, form, request, response);
				} else if (optype.equals("queryAccTemp")) {
					// ��ѯ��ƿ�Ŀ���
					forward = queryAccTemp(mapping, form, request, response);
				} else if (optype.equals("querySetExchange")) {
					// ���ݱ��ֲ�ѯ����
					forward = querySetExchange(mapping, form, request, response);
				} else if (optype.equals("queryComputeItem")) {
					// ���ݻ�ƿ�Ŀ��ѯ������
					forward = queryComputeItem(mapping, form, request, response);
				} else if (optype.equals("approveRemarkPre")) {
					// ��˲�ͨ��ʱ������עǰ
					forward = addRemarkPre(mapping, form, request, response);
				} else if (optype.equals("dealapprov")) {
					// ���ͨ������˲�ͨ���������
					forward = dealapprov(mapping, form, request, response);
				} else if (optype.equals("dealbinder")) {
					// ���ˣ�������
					forward = dealBinder(mapping, form, request, response);
				} else if (optype.equals("binderGuidePre")) {
					// ������ǰ
					forward = bindGuidePre(mapping, form, request, response);
				} else if (optype.equals("batchbinder")) {
					// ���ݹ����򵼽��й���
					forward = batchbinder(mapping, form, request, response);
				} else if (optype.equals("AuditeGuidePre")) {
					// �����ǰ
					forward = AuditeGuidePre(mapping, form, request, response);
				} else if (optype.equals("batchAudite")) {
					// ��������򵼽������
					forward = batchAudite(mapping, form, request, response);
				} else if (optype.equals("dealcheck")) {
					// ���ˣ�������
					forward = dealcheck(mapping, form, request, response);
				} else if (optype.equals("stopCredNoList")) {
					// �ϺŲ�ѯ
					forward = stopCredNoList(mapping, form, request, response);
				} else if (optype.equals("resetCredNoPre")) {
					// ƾ֤����ǰ
					forward = resetCredNoPre(mapping, form, request, response);
				} else if (optype.equals("resetCredNo")) {
					// ƾ֤����
					forward = resetCredNo(mapping, form, request, response);
				}else if (optype.equals("saveToAccTemp")) {
					// ���ģ��
					forward = saveToAccTemp(mapping, form, request, response);
				}
			} else {
				// ��ѯ�б�
				forward = queryVoucher(mapping, form, request, response);
			}
		}
		return forward;
	}

	/**
	 * ƾ֤¼��ǰ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addVoucherPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		/* ��ѯ���õĻ���ڼ� */
		LoginBean loginBean = this.getLoginBean(request);
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		request.setAttribute("isEspecial", request.getParameter("isEspecial"));
		
		//*****���ݵ�ǰ����ڼ�����ƾ֤����*****//
		if(accPeriodBean != null){
			Calendar cal = Calendar.getInstance();
	        //�������
	        cal.set(Calendar.YEAR,accPeriodBean.getAccYear());
	        //�����·�
	        cal.set(Calendar.MONTH, accPeriodBean.getAccMonth()-1);
	        //��ȡĳ���������
	        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        //�����������·ݵ��������
	        cal.set(Calendar.DAY_OF_MONTH, lastDay);
	        //��ʽ������
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String accDate = sdf.format(cal.getTime());	
	        request.setAttribute("accDate", accDate);
		}
		
		//***********end**********//
		
		/* ��ѯ��С������ڼ�������ҳ������֤�ڼ� */
		Result rs = mgt.queryMaxPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("periodStr", rs.retVal);
		}
		
		/* ��ѯ�ֽ�������Ŀ����Ŀ �� ����Ŀ  */
		Result mItems = mgt.queryMItems();
		if(mItems.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("mItems", new Gson().toJson(mItems.retVal));
		} else{
			request.setAttribute("mItems", "[]");
		}
		
		Result sItems = mgt.querySItems();
		if(sItems.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("sItems", new Gson().toJson(sItems.retVal));
		} else{
			request.setAttribute("sItems", "[]");
		}
	
			
		rs = mgt.queryAccPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("accPeriod", rs.retVal);
		}
		/* ƾ֤ģ�� */
		String moduleId = request.getParameter("moduleId");
		if (moduleId != null && !"".equals(moduleId)) {
			/* ����ģ�� */
			rs = mgt.queryAccMainTempleteId(moduleId);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				List list = (ArrayList) rs.retVal;
				if (list == null || list.size() == 0) {
					request.setAttribute("moduleId", "");
					return getForward(request, mapping, "addVoucher");
				}
			}
			boolean isLastSunCompany = Boolean.parseBoolean(hashSession.get("IsLastSCompany").toString());

			Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			String sunClassCode = loginBean.getSunCmpClassCode(); // ��LoginBean��ȡ����֧������classCode

			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);

			/* �û�Ȩ�� */
			MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/VoucherManageAction.do"));

			/* ��ѯ���� */
			rs = new DynDBManager().detail("tblAccMainTemplete", map, moduleId, sunClassCode, props, loginBean.getId(), isLastSunCompany,  "");
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				/* ��ѯ�ɹ� */
				request.setAttribute("values", rs.retVal);
			}
		} else {
			request.setAttribute("startTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
		}

		return getForward(request, mapping, "addVoucher");
	}

	/**
	 * ��ѯժҪ���б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryRecordComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		/* ��ѯժҪ����Ϣ */
		String searchValue = request.getParameter("searchValue");

		try {
			/* ����URL�д������� */
			searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/* ����������ѯ���� */
		Result result = mgt.queryRecord(searchValue);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("RecordList", result.retVal);
		}
		request.setAttribute("searchValue", searchValue);
		return getForward(request, mapping, "queryRecord");
	}

	/**
	 * ���������ժҪ��ѯ���ݿ���ƥ�������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryDescription(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String description = request.getParameter("description");
		try {
			description = java.net.URLDecoder.decode(description, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Result result = mgt.queryRecord(description);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<String[]> list = (ArrayList<String[]>)result.retVal;
			String value = "";
			for(String[] str : list){
				value += str[0]+","+str[1]+";|";
			}
			request.setAttribute("msg", value);
		}
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * ���ժҪ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addRecordComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String value = request.getParameter("value");
		LoginBean lg = this.getLoginBean(request);
		// ���
		Result result = mgt.addRecord(value, lg.getId());
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			new DynDBManager().addLog(0, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"���ƾ֤ժҪ:"+GlobalsTool.toChinseChar(value)+";","tblRecordComment", "ƾ֤����","");
			request.setAttribute("msg", "OK");
		} else {
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ɾ��ժҪ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delRecordComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoginBean lg = getLoginBean(request);
		String id = request.getParameter("id");
		// ɾ��
		Result result = mgt.queryRecordId(id);
		String name = String.valueOf(result.getRetVal());
		
		result = mgt.delRecord(id);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			new DynDBManager().addLog(2, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"ɾ��ƾ֤ժҪ:"+name+";","tblRecordComment", "ƾ֤����","");
			request.setAttribute("msg", "OK");
		} else {
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ��ѯ��ƿ�Ŀ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryAccCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String classCode = request.getParameter("classCode"); // ��ĿclassCode
		String keywordSearch = request.getParameter("keywordSearch"); // �ؼ���
		String accCodeSearch = request.getParameter("accCodeSearch"); // ��Ŀ��
		String accNameSearch = request.getParameter("accNameSearch"); // ��Ŀ����
		String accFullNameSearch = request.getParameter("accFullNameSearch"); // ��Ŀȫ��
		String selectType = request.getParameter("selectType"); // �������ͣ�chooseChildֻ����ѡ�����¼����ߣ�allȫ����
		String isParent = request.getParameter("isParent");

		ArrayList allScopeRightList = this.getLoginBean(request).getAllScopeRight();

		Result rs = mgt.queryAcc(classCode, keywordSearch, accCodeSearch, accNameSearch, accFullNameSearch, allScopeRightList);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ��ѯ�ɹ�
			request.setAttribute("accMainList", rs.retVal);
			ArrayList list = (ArrayList) rs.retVal;
			String folderTree = "[";
			for (int i = 0; i < list.size(); i++) {
				HashMap map = (HashMap) list.get(i);
				String classCodes = String.valueOf(map.get("classCode"));
				if (folderTree.indexOf("id:\"" + classCodes.substring(0, classCodes.length() - 5) + "\"") == -1) {
					folderTree += this.folderTree(list, String.valueOf(map.get("AccNumber")), String.valueOf(map.get("AccFullName")), classCodes, Integer
							.valueOf(map.get("counts").toString()), selectType);
				}
			}
			folderTree = folderTree.replaceAll("\\}\\{", "},{");
			folderTree += "]";
			request.setAttribute("folderTree", folderTree);
			request.setAttribute("selectType", selectType);
		}
		String strValue = request.getParameter("strValue");
		try {
			/* ����URL�д������� */
			if (strValue != null) {
				strValue = java.net.URLDecoder.decode(strValue, "UTF-8");
			}
			request.setAttribute("strValue", strValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setAttribute("isParent", isParent);
		request.setAttribute("classCode", classCode);
		request.setAttribute("keywordSearch", keywordSearch);
		request.setAttribute("accCodeSearch", accCodeSearch);
		request.setAttribute("accNameSearch", accNameSearch);
		request.setAttribute("accFullNameSearch", accFullNameSearch);
		return getForward(request, mapping, "accMainList");
	}

	private String folderTree(ArrayList list, String accNumber, String folderName, String classCode, Integer isCatalog, String selectType) {
		String folderTree = "";
		folderName = folderName.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		if (isCatalog != null && isCatalog > 0) {
			// �����¼�
			folderTree += "{ id:\"" + classCode + "\",tname:\"" + accNumber + ";" + folderName + ";|" + "\",name:\"" + accNumber + " - " + folderName
					+ "\",nocheck:true,nodes:[";
			for (int i = 0; i < list.size(); i++) {
				HashMap map = (HashMap) list.get(i);
				String classCodes = String.valueOf(map.get("classCode"));
				if (classCodes.substring(0, classCodes.length() - 5).equals(classCode)) {
					folderTree += this.folderTree(list, String.valueOf(map.get("AccNumber")), String.valueOf(map.get("AccFullName")), classCodes, Integer
							.valueOf(map.get("counts").toString()), selectType);
				}
			}
			folderTree += "]}";
		} else {
			folderTree += "{ id:\"" + classCode + "\",tname:\"" + accNumber + ";" + folderName + ";|" + "\",name:\"" + accNumber + " - " + folderName
					+ "\",nodes: []";
			if ("all".equals(selectType)) {
				folderTree += ",nocheck:true";
			}
			folderTree += "}";
		}
		return folderTree;
	}

	/**
	 * ���ݿ�Ŀ�����ѯ�ÿ�Ŀ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryAccTemp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		// ��ƿ�Ŀ����
		String accCode = request.getParameter("accCode");
		String queryMode = request.getParameter("queryMode");
		String json = "{";

		/* ���ݿ�Ŀ�����ѯ���� */
		Result result = mgt.queryCurrency(accCode, queryMode);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			json += "\"currency\":" + result.retVal;
		}

		json += "}";
		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	}

	/**
	 * ���ݿ�Ŀ�����ѯ�ÿ�Ŀ�º�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryComputeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		// ��ƿ�Ŀ����
		String accCode = request.getParameter("accCode");
		String queryMode = request.getParameter("queryMode");
		String json = "{";

		/* ���ݻ�ƿ�Ŀ��ѯ������ */

		Result result = mgt.queryIsBreed(accCode, queryMode);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			json += "\"isbreed\":" + result.retVal;
		}
		json += "}";

		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	}

	/**
	 * �������id��ѯ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward querySetExchange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoginBean loginBean = this.getLoginBean(request);
//		// ����ڼ�
//		Hashtable sessionSet = BaseEnv.sessionSet;
//		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
//		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		
		String exchange = request.getParameter("exchange");
		String newYearPeriod = request.getParameter("newYearPeriod");
		String newPeriod = request.getParameter("newPeriod");
		if(newYearPeriod == null || "".equals(newYearPeriod)){
			newYearPeriod = "0";
		}
		if(newPeriod == null || "".equals(newPeriod)){
			newPeriod = "0";
		}
		AccPeriodBean accPeriodBean = new AccPeriodBean();
		accPeriodBean.setAccYear(Integer.parseInt(newYearPeriod));
		accPeriodBean.setAccPeriod(Integer.parseInt(newPeriod));
		Result result = mgt.queryExchange(exchange, accPeriodBean);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg", result.retVal == null ? "" : result.retVal);
		} else {
			request.setAttribute("msg", "");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ���ƾ֤
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoginBean loginBean = this.getLoginBean(request);

		/* ���� */
		String attachment = request.getParameter("attachFiles");

		String parentCode = request.getParameter("parentCode");
		String moduleType = getParameter("moduleType", request);

		/* �������ڴ������б�ṹ����Ϣ */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		/* ��ṹ��Ϣ */
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, "tblAccMain");

		HashMap values = new HashMap();

		/* ȡ�������� */
		readMainTable(values, tableInfo, request);
		LoginBean lg = getLoginBean(request);

		/* ��ȡ�ӱ������� */
		List childTableList = DDLOperation.getChildTables("tblAccMain", map);
		if (childTableList != null && childTableList.size() > 0) {
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
				readChildTable(values, childTable, request, lg.getSunCmpClassCode());
			}
		}

		/* ���淽ʽ */
		String saveType = request.getParameter("button");
		if ("printSave".equals(saveType)) {
			values.put("isAuditing", "print");
			request.setAttribute("saveType", saveType);
		} else {
			values.put("isAuditing", "start");
		}

		values.put("RefBillType", "tblAccMain");
		values.put("createBy", loginBean.getId());
		values.put("lastUpdateBy", loginBean.getId());
		values.put("createTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("workFlowNode", "0");
		values.put("SCompanyID", "00001");
		values.put("Attachment", attachment);
		values.put("workFlowNodeName", "notApprove");
		values.put("AutoBillMarker", 0);

		// ���������ID
		String id = (String) values.get("id");
		if (id == null || id.trim().length() == 0) {
			id = IDGenerater.getId();
			values.put("id", id);
		}

		// ����ڼ�
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");

		Result result = new Result();

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		/* ɾ��del���ļ� */
		String delFiles = request.getParameter("delFiles");
		if (delFiles != null && !"".equals(delFiles)) {
			String[] delFile = delFiles.split(";");
			for (int i = 0; delFile != null && i < delFile.length; i++) {
				FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, delFile[i]);
			}
		}

		/* ����������ӱ������ */
		result = mgt.addAccMain("tblAccMain", map, values, id, lg, "", resources, this.getLocale(request), saveType);
		ActionForward forward = getForward(request, mapping, "alert");
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS || result.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
			if ("printSave".equals(saveType)) {
				request.setAttribute("BillId", values.get("id"));
				Result print = new ReportSetMgt().getBillTable("tblAccMain", moduleType);
				if (print.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					if (print.getRealTotal() > 0) {
						List list = (List) print.getRetVal();
						ReportsBean report = (ReportsBean) list.get(0);
						Map det = report.getReportDetBean();
						Collection con = det.values();
						Iterator iter = con.iterator();
						while (iter.hasNext()) {
							ReportsDetBean detBean = (ReportsDetBean) iter.next();
							if (detBean.getNewFlag().equals("OLD")) {
								request.setAttribute("print", "true");
								break;
							}
						}
						request.setAttribute("BillRepNumber", report.getReportNumber());
					}
				}
			}
			String message = getMessage(request, "common.msg.addSuccess");
			EchoMessage echo = null;
			if ("printSave".equals(saveType)) {
				echo = EchoMessage.success().add(message).setBackUrl(
						"/UserFunctionQueryAction.do?tableName=tblAccMain" + "&operation=" + OperationConst.OP_QUERY + "&parentCode=" + parentCode
								+ "&parentTableName=&f_brother=&checkTab=Y" + "&moduleType=" + moduleType + "&winCurIndex="
								+ request.getParameter("winCurIndex")+"&popWinName="+request.getParameter("popWinName"));
			} else {
				result = mgt.queryAccMain(values.get("id").toString());
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					/*���ϵͳ��־*/
					HashMap tblAccMainMap = (HashMap)result.getRetVal();
					new DynDBManager().addLog(0, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"���ƾ֤:"+tblAccMainMap.get("CredTypeID")+"-"+tblAccMainMap.get("OrderNo"),"tblAccMain", "ƾ֤����","");
				}
				/* ����ɹ� */
				String isEspecial = request.getParameter("isEspecial");
				String optype = request.getParameter("optype");
				if(optype!=null && "saveOrAdd".equals(optype)){
					echo = EchoMessage.success().add(message).setBackUrl("/VoucherManage.do?operation=6&popWinName="+request.getParameter("popWinName"));
				}else{
					echo = EchoMessage.success().add(message).setBackUrl("/VoucherManageAction.do?operation=5&tableName=tblAccMain&id="+id+"&number=&popWinName="+request.getParameter("popWinName"));
				}
			}
			if (echo != null) {
				echo.setAlertRequest(request);
			}
		} else {
			/* ����ʧ�� */
			// ɾ����ʽ�ļ�
			if (attachment != null && !"".equals(attachment)) {
				String[] uploadaffix = attachment.split(";");
				for (int i = 0; uploadaffix != null && i < uploadaffix.length; i++) {
					FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, uploadaffix[i]);
				}
			}
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(result, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}
		}
		return forward;
	}

	/**
	 * �޸�ƾ֤ǰ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateVoucherPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		/* ƾ֤id */
		String keyId = request.getParameter("id");
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");

		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());

		/* ��ѯ��С������ڼ�������ҳ������֤�ڼ� */
		Result rs = mgt.queryPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("periodStr", rs.retVal);
		}
		
		rs = mgt.queryAccPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("accPeriod", rs.retVal);
		}
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = lg.getSunCmpClassCode(); // ��LoginBean��ȡ����֧������classCode

		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);

		/* �û�Ȩ�� */
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/VoucherManageAction.do"));

		/* �������� */
		rs = new DynDBManager().detail("tblAccMain", map, keyId, sunClassCode, props, lg.getId(), isLastSunCompany,
				"");

		boolean refFlag = true;
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ���سɹ�
			request.setAttribute("values", rs.retVal);
			HashMap hm = (HashMap) rs.retVal;
			if (hm == null || hm.get("id") == null) {
				// δ�ҵ���¼
				EchoMessage.error().add(getMessage(request, "oaJob.not.find")).setRequest(request);
				return getForward(request, mapping, "message");
			}
			//*****�ж��Ƿ������������ƾ֤
			String RefBillType = (String)hm.get("RefBillType");
			String RefBillNo = (String)hm.get("RefBillNo");
			if(RefBillType != null && !"tblAccMain".equals(RefBillType) && RefBillNo != null && !"".equals(RefBillNo)){
				refFlag = false;
			}
			String number = request.getParameter("number");
			int num = 0;
			if (number != null && !"".equals(number)) {
				num = Integer.valueOf(number);
			}
			String isEspecial = request.getParameter("isEspecial");
			if (!"1".equals(isEspecial)) {
				/* ȡ�����浽�ڴ��е��б� */
				HashTable voucherListHash = (HashTable) request.getSession().getAttribute("voucherList");
				List list = (ArrayList) voucherListHash.get(lg.getId());
				if (list != null && list.size() > 0) {
					/* ��һ�� */
					HashMap hashmap = (HashMap) list.get(0);
					request.setAttribute("firstId", hashmap.get("id"));
					request.setAttribute("firstNumber", hashmap.get("counts"));
					/* ��һ�� */
					int tempnum = num;
					if (tempnum > 1) {
						tempnum = tempnum - 1;
					} else {
						tempnum = 0;
					}
					for (int i = tempnum; i > 0; i--) {
						hashmap = (HashMap) list.get(i);
						if (!keyId.equals(hashmap.get("id"))) {
							break;
						}
					}
					request.setAttribute("preId", hashmap.get("id"));
					request.setAttribute("preNumber", hashmap.get("counts"));
					/* ��һ�� */
					tempnum = num;
					if (tempnum == list.size()) {
						tempnum--;
					}

					for (int i = tempnum; i < list.size(); i++) {
						hashmap = (HashMap) list.get(i);
						if (!keyId.equals(hashmap.get("id"))) {
							break;
						}
					}
					request.setAttribute("nextId", hashmap.get("id"));
					request.setAttribute("nextNumber", hashmap.get("counts"));
					/* ���һ�� */
					hashmap = (HashMap) list.get(list.size() - 1);
					request.setAttribute("endId", hashmap.get("id"));
					request.setAttribute("endNumber", hashmap.get("counts"));
				}
			}

		} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// ��¼�����ڴ���
			EchoMessage.error().add(getMessage(request, "common.error.nodata")).setRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// ����ʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("isEspecial", request.getParameter("isEspecial"));
		
		/* ��ѯ�ֽ�������Ŀ����Ŀ �� ����Ŀ  */
		Result mItems = mgt.queryMItems();
		if(mItems.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("mItems", new Gson().toJson(mItems.retVal));
		}
		
		Result sItems = mgt.querySItems();
		if(sItems.retCode ==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("sItems", new Gson().toJson(sItems.retVal));
		}
		
		// ƾ֤����
		rs = mgt.queryVoucherSetting();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) rs.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		request.setAttribute("NoButton", request.getParameter("NoButton"));
		if(!refFlag){
			return getForward(request, mapping, "updateVoucherRef");
		} else{
			return getForward(request, mapping, "updateVoucher");
		}
	}

	/**
	 * ��ѯƾ֤�б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public ActionForward queryVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		VoucherSearchForm searchForm = (VoucherSearchForm) form;
		
		String opUrl = request.getParameter("opUrl");
		if(opUrl != null && "url".equals(opUrl)){
			//������Զ��屨�����ʱ�������ݽ��д���
			searchForm.setCreateBy(request.getParameter("createBy"));
			searchForm.setTblEmployee_EmpFullName(request.getParameter("tblEmployee_EmpFullName"));
			searchForm.setSearchCredYearStart(request.getParameter("searchCredYearStart"));
			searchForm.setSearchCredMonthStart(request.getParameter("searchCredMonthStart"));
			searchForm.setSearchCredYearEnd(request.getParameter("searchCredYearEnd"));
			searchForm.setSearchCredMonthEnd(request.getParameter("searchCredMonthEnd"));
			String searchRecordComment=request.getParameter("searchRecordComment")==null?"":request.getParameter("searchRecordComment");
			searchForm.setSearchRecordComment(new String(searchRecordComment.getBytes("iso-8859-1"), "UTF-8"));
			searchForm.setSearchStartTime(request.getParameter("searchStartTime"));
			searchForm.setSearchEndTime(request.getParameter("searchEndTime"));
			searchForm.setSearchAccCodeStart(request.getParameter("searchAccCodeStart"));
			searchForm.setSearchAccCodeEnd(request.getParameter("searchAccCodeEnd"));
			
			searchForm.setSearchMoneyEnd(request.getParameter("setSearchMoneyEnd"));
			searchForm.setSearchMoneyEnd(request.getParameter("setSearchMoneyEnd"));
			
			searchForm.setSearchwName(request.getParameter("searchwName"));
			
			//***���ƾ֤����ɸѡ����
			searchForm.setSearchAccType(request.getParameter("searchAccType"));
			
			
			String searchDeptName = request.getParameter("searchDeptName");
			if(searchDeptName != null && !"".equals(searchDeptName)){
				searchForm.setSearchDeptName(GlobalsTool.toChinseChar(searchDeptName));
			}
			String searchEmployeeName = request.getParameter("searchEmployeeName");
			if(searchEmployeeName != null && !"".equals(searchEmployeeName)){
				searchForm.setSearchEmployeeName(GlobalsTool.toChinseChar(searchEmployeeName));
			}
			String searchClientName= request.getParameter("searchClientName");
			if(searchClientName != null && !"".equals(searchClientName)){
				searchForm.setSearchClientName(GlobalsTool.toChinseChar(searchClientName));
			}
		}
		
		LoginBean bean = this.getLoginBean(request);
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/VoucherManageAction.do");
		/* ��ѯ�б� */
		Result result = mgt.queryList(searchForm, mop, bean);

		// ��ѯ�ɹ�
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {

			/* ���б�洢��session�� */
			HashTable voucherListHash = (HashTable) request.getSession().getAttribute("voucherList");
			if (voucherListHash == null) {
				voucherListHash = new HashTable();
			}
			Object[] object = (Object[]) result.retVal;
			if (object != null && object.length > 0) {
				request.setAttribute("AccMainList", object[0]);
				String saveSql = object[1]+"";
				saveSql = saveSql.replaceAll("%", "@CentSign:");
				while (saveSql.indexOf("+") > -1) {
					String t = saveSql.substring(0, saveSql.indexOf("+"));
					saveSql = t + "@AddSign:" + saveSql.substring(saveSql.indexOf("+") + 1);
				}
				saveSql =GlobalsTool.replaceSpecLitter("@SQL:"+saveSql+"@ParamList:@end:");
				request.setAttribute("saveSql", saveSql);
				voucherListHash.put(bean.getId(), object[0]);
				result.setRetVal(object[0]);
				request.setAttribute("pageBar", this.pageBar(result, request));
			}
			request.getSession().setAttribute("voucherList", voucherListHash);
		}

		// ƾ֤����
		result = mgt.queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(bean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriodBean", accPeriodBean);
		
		request.setAttribute("VoucherSearchForm", searchForm);
		
		
		Result rs2 = new ReportDataMgt().getBillFormatList("tblAccMain","",getLocale(request).toString(),getLoginBean(request).getId(),getLoginBean(request).getDepartCode());
        request.setAttribute("formatList", rs2.getRetVal());
        Cookie[] coks = request.getCookies();
        for (int i = 0; i < coks.length; i++) {
            Cookie cok = coks[i];
            if (cok.getName().equals("JSESSIONID")) {
                request.setAttribute("JSESSIONID", cok.getValue());
                break;
            }
        }
		
		return getForward(request, mapping, "voucherList");
	}

	/**
	 * ɾ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* ѡ��Ҫɾ������ID */
		String[] keyIds = request.getParameterValues("keyId");

		/* �������ڴ������б�ṹ����Ϣ */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

		/* �û���½��Ϣ */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");

		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		List logList = new ArrayList();
		for(String s : keyIds){
			Result result = mgt.queryAccMain(s);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				HashMap tblAccMainMap = (HashMap)result.getRetVal();
				logList.add(tblAccMainMap);
			}
		}
		
		/* ɾ�� */
		Result rs = mgt.deleteData(keyIds, "tblAccMain", map, new HashMap(), lg.getId(), "", resources, this.getLocale(request));

		ActionForward forward = null;

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			/* ɾ���ɹ� */

			String[] files = (String[]) rs.getRetVal();
			// ɾ������
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, files[i]);
				}
			}
			/*���ϵͳ��־*/
			for(int i =0;i<logList.size();i++){
				HashMap logMap = (HashMap)logList.get(i);
				new DynDBManager().addLog(2, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"ɾ��ƾ֤:"+logMap.get("CredTypeID")+"-"+logMap.get("OrderNo")+";","tblAccMain", "ƾ֤����","");
			}
			forward = queryVoucher(mapping, form, request, response);
		} else {
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}			
			forward = getForward(request, mapping, "message");
		}
		return forward;
	}

	/**
	 * ���ݵ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String[] keyIds = getParameters("keyId", request);
		String accName = GlobalsTool.getSysSetting("DefBackPath");
		File file = new File("..\\..\\AIOBillExport");
		if (!file.exists()) {
			file.mkdirs();
		}
		String tableDisplay = GlobalsTool.getTableInfoBean("tblAccMain").getDisplay().get(getLocale(request).toString());
		String fileName = file.getCanonicalPath() + "\\" + tableDisplay + ".xls";
		Result result = new Result();
		ArrayList<ExportField> exportList = new ArrayList<ExportField>();
		ArrayList<HashMap> exportValuesList = new ArrayList<HashMap>();
		LoginBean lg = getLoginBean(request);
		String sunClassCode = lg.getSunCmpClassCode();  //��LoginBean��ȡ����֧������classCode
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
        boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
        MOperation mop = GlobalsTool.getMOperationMap(request) ;
        //ִ�м�����ϸ
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        DBTableInfoBean dbTable = GlobalsTool.getTableInfoBean("tblAccMain");
        ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables("tblAccMain", map);
        request.setAttribute("mainTable",dbTable);
        request.setAttribute("fieldInfos", dbTable.getFieldInfos()) ;
        request.setAttribute("childTableList", childTableList) ;
        ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
        ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();
        Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;//�ӱ��������
        Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)request.getSession().getServletContext().getAttribute("userSettingColConfig") ;
        request.setAttribute("configList", configList);
        request.setAttribute("allConfigList",allConfigList);
        request.setAttribute("childTableConfigList",childTableConfigList);
        request.setAttribute("tableName", "tblAccMain");
        
        if(keyIds == null || keyIds.length ==0){
        	LoginBean bean = this.getLoginBean(request);
    		mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/VoucherManageAction.do");
    		/* ��ѯ�б� */
    		VoucherSearchForm searchForm = (VoucherSearchForm)form;
    		int pageSize = searchForm.getPageSize();
    		searchForm.setPageSize(-1111);
    		Result idresult = mgt.queryList(searchForm, mop, bean);
    		searchForm.setPageSize(pageSize);
    		if(idresult.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			ArrayList<HashMap> list= (ArrayList<HashMap>)((Object[])idresult.retVal)[0];
    			keyIds = new String[list.size()];
    			for(int i = 0;i<list.size();i++){
    				keyIds[i] = list.get(i).get("id")+"";
    			}
    		}
        }
        

		for (int z = 0; keyIds != null && z < keyIds.length; z++) {
			String keyex = keyIds[z];
			request.setAttribute("detail", "detail");
			request.setAttribute("detailOk", "1");
			Result rs = new DynDBManager().detail("tblAccMain", map, keyex, sunClassCode,props,lg.getId(),isLastSunCompany,"");
			HashMap hm = (HashMap) rs.retVal;
			request.setAttribute("values", hm);
			HashMap moreLan=(HashMap)hm.get("LANGUAGEQUERY");
			HashMap childFieldMap = new HashMap();
    		try {
    			if (childTableList != null) {
    				for(DBTableInfoBean dib :(ArrayList<DBTableInfoBean>)childTableList){
    					ArrayList<DBFieldInfoBean> dibFieldList = DynDBManager.getMainFieldList("tblAccMain", dib,null, childTableConfigList.get(dib.getTableName()), null, null, null, mop, "-1", true, "", lg, null);
    					childFieldMap.put(dib.getTableName(), dibFieldList);
    				}
    			}
    		} catch (Exception e) {
            	EchoMessage.error().add(e.getMessage()).setClosePopWin(request.getParameter("popWinName")).setRequest(request);
                return getForward(request, mapping, "message");
    		}   
			
			request.setAttribute("childFieldMap", childFieldMap) ;
			HashMap exportValues = new HashMap();
			exportValuesList.add(exportValues);
			exportList = new ArrayList<ExportField>();
			ExportData.export(request, exportList, exportValues);
		}

		FileOutputStream fos = new FileOutputStream(fileName);
		result = ExportData.billExport(fos, tableDisplay, exportList, exportValuesList, null);
		fos.close();
		/* �����ɹ� */
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String backURL = "/VoucherManageAction.do?operation=4";
			EchoMessage.success().add(
					getMessage(request, "com.export.success") + "<a href='/ReadFile?tempFile=export&fileName=" + GlobalsTool.encode(tableDisplay + ".xls")
							+ "'>" + getMessage(request, "com.export.success.download") + "</a>" + getMessage(request, "com.export.success.over") + fileName)
					.setBackUrl(backURL).setNotAutoBack().setAlertRequest(request);
		} else {
			EchoMessage.error().add(getMessage(request, "com.export.failure")).setNotAutoBack().setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}

	/**
	 * ƾ֤����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward detailVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* ƾ֤id */
		String keyId = request.getParameter("id");
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");

		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());

		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		String sunClassCode = lg.getSunCmpClassCode(); // ��LoginBean��ȡ����֧������classCode

		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);

		/* �û�Ȩ�� */
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/VoucherManageAction.do"));

		/* �������� */
		Result rs = new DynDBManager().detail("tblAccMain", map, keyId, sunClassCode, props, lg.getId(), isLastSunCompany, 
				"");		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ���سɹ�
			request.setAttribute("values", rs.retVal);			
			HashMap hm = (HashMap) rs.retVal;
			if (hm == null || hm.get("id") == null) {
				// δ�ҵ���¼
				EchoMessage.error().add(getMessage(request, "oaJob.not.find")).setRequest(request);
				return getForward(request, mapping, "message");
			}					
			
			String number = request.getParameter("number");
			int num = 0;
			if (number != null && !"".equals(number)) {
				num = Integer.valueOf(number);
			}
			/* ȡ�����浽�ڴ��е��б� */
			HashTable voucherListHash = (HashTable) request.getSession().getAttribute("voucherList");
			if (voucherListHash != null && voucherListHash.size() > 0) {
				List list = (ArrayList) voucherListHash.get(lg.getId());
				if (list != null && list.size() > 0) {
					/* ��һ�� */
					HashMap hashmap = (HashMap) list.get(0);
					request.setAttribute("firstId", hashmap.get("id"));
					request.setAttribute("firstNumber", hashmap.get("counts"));
					/* ��һ�� */
					int tempnum = num;
					if (tempnum > 1) {
						tempnum = tempnum - 1;
					} else {
						tempnum = 0;
					}
					for (int i = tempnum; i > 0; i--) {
						hashmap = (HashMap) list.get(i);
						if (!keyId.equals(hashmap.get("id"))) {
							break;
						}
					}
					request.setAttribute("preId", hashmap.get("id"));
					request.setAttribute("preNumber", hashmap.get("counts"));
					/* ��һ�� */
					tempnum = num;
					if (tempnum == list.size()) {
						tempnum--;
					}

					for (int i = tempnum; i < list.size(); i++) {
						hashmap = (HashMap) list.get(i);
						if (!keyId.equals(hashmap.get("id"))) {
							break;
						}
					}
					request.setAttribute("nextId", hashmap.get("id"));
					request.setAttribute("nextNumber", hashmap.get("counts"));
					/* ���һ�� */
					hashmap = (HashMap) list.get(list.size() - 1);
					request.setAttribute("endId", hashmap.get("id"));
					request.setAttribute("endNumber", hashmap.get("counts"));
					request.setAttribute("number", number);
				}
			}
		} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
			// ��¼�����ڴ���
			EchoMessage.error().add(getMessage(request, "common.error.nodata")).setRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// ����ʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("isEspecial", request.getParameter("isEspecial"));
		// ƾ֤����
		rs = mgt.queryVoucherSetting();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) rs.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		/* ����ڼ� */
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		request.setAttribute("NoButton", request.getParameter("NoButton"));
				
		return getForward(request, mapping, "detailVoucher");
		
	}

	/**
	 * �޸�ƾ֤
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateVoucher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginBean loginBean = this.getLoginBean(request);
		
		request.setAttribute("popWinName", request.getParameter("popWinName"));
		

		/* ��֤�Ƿ�����޸ģ�����ڼ�ǰ�����ݲ����޸ģ� */
		Integer credYear = Integer.valueOf(request.getParameter("CredYear"));
		Integer credMonth = Integer.valueOf(request.getParameter("CredMonth"));

		Integer periodMonth = -1;
		Integer periodYear = -1;
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		if (accPeriodBean != null) {
			periodMonth = accPeriodBean.getAccMonth();
			periodYear = accPeriodBean.getAccYear();
		}
		if ((credYear < periodYear || (credYear == periodYear && credMonth < periodMonth) || periodMonth < 0) && credMonth != 0) {
			EchoMessage.error().add(getMessage(request, "com.cantupdatebefbill")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}

		/* ���� */
		String attachment = request.getParameter("attachFiles");

		/* �������ڴ������б�ṹ����Ϣ */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		/* ��ṹ��Ϣ */
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, "tblAccMain");

		HashMap values = new HashMap();

		/* ȡ�������� */
		readMainTable(values, tableInfo, request);
		LoginBean lg = getLoginBean(request);

		/* ��ȡ�ӱ������� */
		List childTableList = DDLOperation.getChildTables("tblAccMain", map);
		if (childTableList != null && childTableList.size() > 0) {
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
				readChildTable(values, childTable, request, lg.getSunCmpClassCode());
			}
		}
		values.put("isAuditing", "start");
		values.put("lastUpdateBy", loginBean.getId());
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("Attachment", attachment);
		//values.put("OrderNo", 0);
		values.put("AutoBillMarker", 0);
		
		values.put("oldOrderNo", request.getParameter("oldOrderNo"));							//�ɵ�ƾ֤��
		values.put("oldCredTypeID", request.getParameter("oldCredTypeID"));						//�ɵ�ƾ֤��
		values.put("oldCredYear", request.getParameter("CredYear"));							//�ɵĻ���ڼ���
		values.put("oldPeriod", request.getParameter("Period"));								//�ɵĻ���ڼ�

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		/* ɾ��del���ļ� */
		String delFiles = request.getParameter("delFiles");
		if (delFiles != null && !"".equals(delFiles)) {
			String[] delFile = delFiles.split(";");
			for (int i = 0; delFile != null && i < delFile.length; i++) {
				FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, delFile[i]);
			}
		}

		/* �޸�������ӱ������ */
		Result result = mgt.updateAccMain("tblAccMain", map, values, loginBean, "", resources, this.getLocale(request));
		ActionForward forward = getForward(request, mapping, "alert");
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS || result.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
			/* �޸ĳɹ� */
			result = mgt.queryAccMain(values.get("id").toString());
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				/*���ϵͳ��־*/
				HashMap tblAccMainMap = (HashMap)result.getRetVal();
				new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"ƾ֤�޸�ǰ:"+tblAccMainMap.get("CredTypeID")+"-"+tblAccMainMap.get("OrderNo")+";�޸ĺ�:"+tblAccMainMap.get("CredTypeID")+"-"+tblAccMainMap.get("OrderNo")+";","tblAccMain", "ƾ֤����","");
			}
			String message = getMessage(request, "common.msg.updateSuccess");
			EchoMessage echo = EchoMessage.success().add(message).setBackUrl("/VoucherManageAction.do?operation=5&tableName=tblAccMain&id="+values.get("id")+"&number=&popWinName="+request.getParameter("popWinName"));
			echo.setAlertRequest(request);
		} else {
			/* �޸�ʧ�� */
			// ɾ����ʽ�ļ�
			if (attachment != null && !"".equals(attachment)) {
				String[] uploadaffix = attachment.split(";");
				for (int i = 0; uploadaffix != null && i < uploadaffix.length; i++) {
					FileHandler.delete("tblAccMain", FileHandler.TYPE_AFFIX, uploadaffix[i]);
				}
			}
			SaveErrorObj saveErrrorObj = new DynDBManager().saveError(result, getLocale(request).toString(), "");
        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
				EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl()).setAlertRequest(request);
			} else {
				EchoMessage.error().add(saveErrrorObj.getMsg()).setAlertRequest(request);
			}	
			
		}
		return forward;
	}

	/**
	 * ��ѯƾ֤ģ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward voucherModuleList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String searchValue = request.getParameter("searchValue");
		/* ��ѯƾ֤ģ�� */
		Result result = mgt.queryModuleList(searchValue);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ��ѯ�ɹ�
			request.setAttribute("moduleList", result.retVal);
		}
		request.setAttribute("searchValue", searchValue);
		return getForward(request, mapping, "moduleList");
	}

	/**
	 * ����ģ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward addModule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginBean loginBean = this.getLoginBean(request);

		/* ���� */
		String attachment = request.getParameter("Attachment");

		/* �������ڴ������б�ṹ����Ϣ */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		/* ��ṹ��Ϣ */
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, "tblAccMainTemplete");

		HashMap values = new HashMap();

		/* ȡ�������� */
		readMainTable(values, tableInfo, request);
		LoginBean lg = getLoginBean(request);

		/* ��ȡ�ӱ������� */
		List childTableList = DDLOperation.getChildTables("tblAccMainTemplete", map);
		if (childTableList != null && childTableList.size() > 0) {
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
				readChildTables(values, childTable, request, lg.getSunCmpClassCode());
			}
		}
		values.put("isAuditing", "start");
		values.put("RefBillType", "tblAccMainTemplete");
		values.put("createBy", loginBean.getId());
		values.put("DepartmentCode", loginBean.getDepartCode());
		values.put("lastUpdateBy", loginBean.getId());
		values.put("createTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("lastUpdateTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		values.put("workFlowNode", "0");
		values.put("SCompanyID", "00001");
		values.put("AutoBillMarker", 0);

		// ���������ID
		String id = (String) values.get("id");
		if (id == null || id.trim().length() == 0) {
			id = IDGenerater.getId();
			values.put("id", id);
		}

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		/* ɾ��del���ļ� */
		String delFiles = request.getParameter("delFiles");
		if (delFiles != null && !"".equals(delFiles)) {
			String[] delFile = attachment.split(";");
			for (int i = 0; delFile != null && i < delFile.length; i++) {
				FileHandler.delete("tblAccMainTemplete", FileHandler.TYPE_AFFIX, delFile[i]);
			}
		}

		/* ����ƾ֤ģ��������ӱ������ */
		Result result = mgt.addAccMainModule("tblAccMainTemplete", map, values, id, loginBean.getId(), "", resources, this.getLocale(request), "");

		// ��ӳɹ�
		request.setAttribute("noback", true);
		request.setAttribute("loadFresh", "true");
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setBackUrl("parent.asyRefure()").setAlertRequest(request);
		} else {
			// ���ƾ֤ģ��ʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}

	/**
	 * ɾ��ƾ֤ģ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteModule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* ɾ��ģ���Id���� */
		String[] keyIds = request.getParameterValues("keyId");

		/* ִ��ɾ������ */
		Result result = mgt.deleteModule(keyIds);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ɾ���ɹ�
			request.setAttribute("msg", "OK");

			String[] files = (String[]) result.getRetVal();
			// ɾ������
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					FileHandler.delete("tblAccMainTemplete", FileHandler.TYPE_AFFIX, files[i]);
				}
			}
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ��˲�ͨ��ʱ������עǰ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward addRemarkPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* ƾ֤Id */
		String voucherId = request.getParameter("voucherId");
		request.setAttribute("voucherId", voucherId);
		request.setAttribute("isList", request.getParameter("isList"));
		return getForward(request, mapping, "approveRemark");
	}

	/**
	 * ��˴������ͨ������˲�ͨ��������ˣ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward dealapprov(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* �������ͣ�noPass��˲�ͨ����yesPass���ͨ����reversePass����ˣ� */
		String dealType = request.getParameter("dealType");

		LoginBean loginBean = this.getLoginBean(request);
		/* ƾ֤Id���� */
		String[] keyIds = request.getParameterValues("keyId");

		/* ��ע */
		String remark = request.getParameter("remark");

		String isList = request.getParameter("isList");

		String isAuditing = ""; // ƾ֤״̬
		String approver = ""; // �����
		remark = remark == null ? "" : remark;
		if (dealType != null && "noPass".equals(dealType)) {
			remark += " " + getMessage(request, "oa.job.noApproved") + " ";
			isAuditing = "noPass";
		} else if (dealType != null && "yesPass".equals(dealType)) {
			remark = getMessage(request, "oa.job.approved") + " ";
			isAuditing = "finish";
			approver = loginBean.getId();
		} else if (dealType != null && "reversePass".equals(dealType)) {
			remark = getMessage(request, "common.turnAuditing") + " ";
			isAuditing = "noPass";
			approver = "";
		}
		EchoMessage echo = null;
		/* ��ע+�����+ʱ�� */

		remark += loginBean.getEmpFullName() + " " + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "<br />";
		if (dealType != null && keyIds.length > 0) {
			Result rs = mgt.approveUpdate(keyIds, remark, isAuditing, approver, dealType, getLocale(request), loginBean);
			String message = "";
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				/* �ɹ� */
				message = "com.auto.succeed";
				// ����֪ͨ��Ϣ
				rs = mgt.queryCreatePerson(keyIds);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List list = (ArrayList) rs.retVal;
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							String[] str = (String[]) list.get(i);
							String title = getMessage(request, "erp.voucher.my") + "[" + str[1] + "-" + str[2] + "]";
							if (dealType != null && "noPass".equals(dealType)) {
								title += getMessage(request, "erp.voucher.nopass");
							} else if (dealType != null && "yesPass".equals(dealType)) {
								title += getMessage(request, "erp.voucher.yesPass");
							} else if (dealType != null && "reversePass".equals(dealType)) {
								title += getMessage(request, "erp.voucher.reversePass");
							}
							String favoriteURL = "/VoucherManageAction.do?operation=5&tableName=tblAccMain&id=" + str[0] + "&isEspecial=1";
							String content = "<a href=javascript:mdiwin('" + favoriteURL + "','ƾ֤����')>" + title
									+ "</a>";
							new Thread(new NotifyFashion(loginBean.getId(), title, content, str[3], 4, "yes", str[0])).start();
							new DynDBManager().addLog(14, loginBean.getId(), loginBean.getName(), loginBean.getSunCmpClassCode(),"ƾ֤����:["+str[1]+"-"+str[2]+"]"+remark+";","tblAccMain", "ƾ֤����","ƾ֤����");
						}
					}
				}
				if (isList != null && !"".equals(isList)) {
					// �����޸�ҳ��
					if (dealType != null && "noPass".equals(dealType)) {
						// ��˲�ͨ��
						request.setAttribute("loadFresh", "true");
						echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl("parent.reloads()");
					} else {
						String url = "/VoucherManageAction.do?";
						if ("update".equals(isList)) {
							url += "operation=7";
						} else {
							url += "operation=5";
						}
						url += "&id=" + keyIds[0];
						url +="&isEspecial="+request.getParameter("isEspecial")+"&NoButton="+request.getParameter("NoButton");
						url +="&popWinName="+request.getParameter("popWinName");
						echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl(url);
					}
				} else {
					// ƾ֤�б�
					echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl("/VoucherManageAction.do?operation=4");
				}
			} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
				// ������֤����
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				echo = EchoMessage.error().add(errorMessage);
			} else {
				SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
	        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
	        		echo = EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl());
				} else {
					echo = EchoMessage.error().add(saveErrrorObj.getMsg());
				}	
				
			}
		}
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * ���ˡ�������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward dealBinder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* �������ͣ�binder���ˣ�reversebinder�����ˣ� */
		String dealType = request.getParameter("dealType");

		LoginBean loginBean = this.getLoginBean(request);
		/* ƾ֤Id���� */
		String[] keyIds = request.getParameterValues("keyId");

		String isList = request.getParameter("isList");

		String remark = ""; // ��ע
		String workFlowNodeName = "notApprove"; // �Ƿ���ˣ�notApproveδ���ˣ�finish�ѹ��ˣ�
		String postingUser = ""; // ������

		if (dealType != null && "binder".equals(dealType)) {
			remark = getMessage(request, "muduleFlow.lb.voucher") + " ";
			workFlowNodeName = "finish";
			postingUser = loginBean.getId();
		} else if (dealType != null && "reverseBinder".equals(dealType)) {
			remark = getMessage(request, "erp.voucher.reverseBinder") + " ";
			workFlowNodeName = "notApprove";
			postingUser = "";
		}
		String logMsg = remark;
		/* ��ע+������+ʱ�� */
		remark += loginBean.getEmpFullName() + " " + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "<br />";
		String message = "";
		String url = "";
		EchoMessage echo = null;
		if (dealType != null && keyIds.length > 0) {

			// ƾ֤����
			AccMainSettingBean settingBean = null;
			Result result = mgt.queryVoucherSetting();
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				settingBean = (AccMainSettingBean) result.retVal;
			}

			MessageResources resources = null;
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}

			// ���˻��߷�����
			Result rs = mgt.binderUpdate(keyIds, remark, workFlowNodeName, postingUser, dealType, settingBean, loginBean, resources, this.getLocale(request));
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				message = "com.auto.succeed";
				if (isList != null && !"".equals(isList)) {
					url = "/VoucherManageAction.do?";
					if ("update".equals(isList)) {
						url += "operation=7";
					} else {
						url += "operation=5";
					}
					url += "&id=" + keyIds[0];
				} else {
					// ƾ֤�б�
					url = "/VoucherManageAction.do?operation=4";
				}
				rs = mgt.queryCreatePerson(keyIds);
				List list = (ArrayList) rs.retVal;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String[] str = (String[]) list.get(i);
						new DynDBManager().addLog(14, loginBean.getId(), loginBean.getName(), loginBean.getSunCmpClassCode(),"ƾ֤����:"+"["+str[1]+"-"+str[2]+"]"+logMsg+"�ɹ�;","tblAccMain", "ƾ֤����","ƾ֤����");
					}
				}
				url +="&isEspecial="+request.getParameter("isEspecial")+"&NoButton="+request.getParameter("NoButton");
				url +="&popWinName="+request.getParameter("popWinName");
				echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl(url);
			} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
				// ��֤��������Ϣ
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				echo = EchoMessage.error().add(errorMessage);
			} else {
				SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, getLocale(request).toString(), "");
	        	if (saveErrrorObj.getBackUrl()!=null && saveErrrorObj.getBackUrl().length() > 0) {
	        		echo = EchoMessage.info().add(saveErrrorObj.getMsg()).setGoUrl(saveErrrorObj.getBackUrl());
				} else {
					echo = EchoMessage.error().add(saveErrrorObj.getMsg());
				}	
			}
		}
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * ������ǰ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward bindGuidePre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(this.getLoginBean(request).getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriodBean", accPeriodBean);
		return getForward(request, mapping, "binderGuide");
	}
	
	/**
	 * �����ǰ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward AuditeGuidePre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(this.getLoginBean(request).getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriodBean", accPeriodBean);
		return getForward(request, mapping, "AuditeGuide");
	}

	/**
	 * ���ݹ����򵼽��й���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward batchbinder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ƾ֤�Ų�����(ֹͣ����1����������2)
		String iscredNo = request.getParameter("iscredNo");

		// ���˷�������(ֹͣ����1����������2)
		String postingerror = request.getParameter("postingerror");

		// ƾ֤��Χ(ȫ��1��ָ����Χ֮ǰ2��vouchertimeʱ�䣬ָ���ڼ�3)
		String voucherarea = request.getParameter("voucherarea");
		String vouchertime = request.getParameter("vouchertime");
		String voucherPeriodYear = request.getParameter("voucherPeriodYear");
		String voucherPeriod = request.getParameter("voucherPeriod");

		// ƾ֤��
		String credTypeid = request.getParameter("credTypeid");

		// �û���½��Ϣ
		LoginBean loginBean = this.getLoginBean(request);
		// ƾ֤����
		AccMainSettingBean settingBean = null;
		Result result = mgt.queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			settingBean = (AccMainSettingBean) result.retVal;
		}

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}

		// �򵼹���
		Result rs = mgt.batchBinder(Integer.valueOf(iscredNo), Integer.valueOf(postingerror), Integer.valueOf(voucherarea), vouchertime, settingBean,
				loginBean, resources, this.getLocale(request), credTypeid,voucherPeriodYear,voucherPeriod);

		EchoMessage echo = null;
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			// ���˳ɹ�
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			echo = EchoMessage.success().add(errorMessage).setBackUrl("parent.reloads()");
		} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
			// ��֤��������Ϣ
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			echo = EchoMessage.error().add(errorMessage);
		} else {
			echo = EchoMessage.error().add(rs.retCode, request);
		}
		request.setAttribute("loadFresh", "true");
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * ��������򵼽������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward batchAudite(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ƾ֤��Χ(ȫ��1��ָ����Χ֮ǰ2��vouchertimeʱ�䣬ָ���ڼ�3)
		String voucherarea = request.getParameter("voucherarea");
		String vouchertime = request.getParameter("vouchertime");
		String voucherPeriodYear = request.getParameter("voucherPeriodYear");
		String voucherPeriod = request.getParameter("voucherPeriod");

		// �û���½��Ϣ
		LoginBean loginBean = this.getLoginBean(request);
		

		MessageResources resources = null;
		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(this.getLoginBean(request).getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriodBean", accPeriodBean);
		
		// �򵼹���
		String remark=loginBean.getEmpFullName() + " " + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "<br />";
		Result rs = mgt.batchAudite(Integer.valueOf(voucherarea),vouchertime,loginBean, resources, this.getLocale(request),voucherPeriodYear,voucherPeriod,accPeriodBean,remark);

		EchoMessage echo = null;
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			// ��˳ɹ�
			echo = EchoMessage.success().add("��˳ɹ�").setBackUrl("parent.reloads()");
		} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
			// ��֤��������Ϣ
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			echo = EchoMessage.error().add(errorMessage);
		} else {
			echo = EchoMessage.error().add(rs.retCode, request);
		}
		request.setAttribute("loadFresh", "true");
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * ����/������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward dealcheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* �������ͣ����ˣ������ˣ� */
		String dealType = request.getParameter("dealType");

		LoginBean loginBean = this.getLoginBean(request);
		/* ƾ֤Id���� */
		String[] keyIds = request.getParameterValues("keyId");

		/* ����źͽ��㷽ʽ */
		String acceptMode = request.getParameter("AcceptMode");
		String acceptNumber = request.getParameter("AcceptNumber");

		String isList = request.getParameter("isList");

		String remark = ""; // ��ע
		Integer isReview = 1; // �Ƿ񸴺ˣ�1δ���ˣ�2�Ѹ��ˣ�
		String reviewUser = ""; // ������

		if (dealType != null && "check".equals(dealType)) {
			remark = getMessage(request, "ReportBillView.button.Check") + " ";
			isReview = 2;
			reviewUser = loginBean.getId();
		} else if (dealType != null && "reverseCheck".equals(dealType)) {
			remark = getMessage(request, "erp.voucher.reverseCheck") + " ";
			isReview = 1;
			reviewUser = "";
		}
		/* ��ע+������+ʱ�� */
		remark += loginBean.getEmpFullName() + " " + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "<br />";
		String message = "";
		String url = "";
		EchoMessage echo = null;
		if (dealType != null && keyIds.length > 0) {

			// ƾ֤����
			AccMainSettingBean settingBean = null;
			Result result = mgt.queryVoucherSetting();
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				settingBean = (AccMainSettingBean) result.retVal;
			}

			// ���˻��߷�����
			Result rs = mgt.checkUpdate(keyIds, remark, isReview, reviewUser, dealType, settingBean, loginBean, acceptMode, acceptNumber, isList);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				message = "com.auto.succeed";
				if (isList != null && !"".equals(isList)) {
					url = "/VoucherManageAction.do?";
					if ("update".equals(isList)) {
						url += "operation=7";
					} else {
						url += "operation=5";
					}
					url += "&id=" + keyIds[0];
				} else {
					// ƾ֤�б�
					url = "/VoucherManageAction.do?operation=4";
				}
				echo = EchoMessage.success().add(getMessage(request, message)).setBackUrl(url);
			} else if (rs.retCode == ErrorCanst.RET_HAS_AUDITING) {
				// ��֤��������Ϣ
				String errorMessage = rs.getRetVal().toString();
				errorMessage = GlobalsTool.revertTextCode2(errorMessage);
				echo = EchoMessage.error().add(errorMessage);
			} else {
				echo = EchoMessage.error().add(rs.retCode, request);
			}
		}
		echo.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	/**
	 * �ϺŲ�ѯ�б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward stopCredNoList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* ƾ֤�� */
		String CredTypeID = request.getParameter("CredTypeID");

		if (CredTypeID != null && !"".equals(CredTypeID)) {
			String periodyear = request.getParameter("periodyear");
			String period = request.getParameter("period");
			AccPeriodBean accPeriodBean = new AccPeriodBean();
			accPeriodBean.setAccYear(Integer.valueOf(periodyear));
			accPeriodBean.setAccPeriod(Integer.valueOf(period));
			/* ��ѯ�Ϻ� */
			Result rs = mgt.queryStopCredNoList(CredTypeID, accPeriodBean, "queryCredNo");
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				map = (HashMap<Integer, Integer>) rs.retVal;
				Iterator iter = map.entrySet().iterator();
				List list = new ArrayList<String[]>();
				while (iter.hasNext()) {
					String[] str = new String[4];
					Entry entry = (Entry) iter.next();
					str[0] = CredTypeID;
					str[1] = String.valueOf(accPeriodBean.getAccYear());
					str[2] = String.valueOf(accPeriodBean.getAccPeriod());
					str[3] = String.valueOf(entry.getKey().toString());
					list.add(str);
				}
				request.setAttribute("accmainList", list);
			}
			request.setAttribute("periodyear", periodyear);
			request.setAttribute("period", period);
			request.setAttribute("CredTypeID", CredTypeID);
		}
		return getForward(request, mapping, "stopCredNoList");
	}

	/**
	 * ���ģ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveToAccTemp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* ��ѯ���õĻ���ڼ� */
		LoginBean loginBean = this.getLoginBean(request);
		Hashtable sessionSet = BaseEnv.sessionSet;
		String name=request.getParameter("name");
		String keyId = request.getParameter("keyId");
		Result rs = mgt.saveToAccTemp(name, keyId);
		String msg = "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			msg = "���ģ��ɹ�";
		}else{
			msg = "���ģ��ʧ��";
		}
		request.setAttribute("msg", msg);

		return getForward(request, mapping, "blank");
	}
	/**
	 * ƾ֤����ǰ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward resetCredNoPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/* ��ѯ���õĻ���ڼ� */
		LoginBean loginBean = this.getLoginBean(request);
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);

		return getForward(request, mapping, "resetCredNo");
	}

	/**
	 * ƾ֤����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward resetCredNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ƾ֤��
		String CredTypeID = request.getParameter("credTypeid");
		try {
			/* ����URL�д������� */
			CredTypeID = java.net.URLDecoder.decode(CredTypeID, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// �ڼ���
		String periodYear = request.getParameter("periodYear");
		// �ڼ�
		String period = request.getParameter("period");

		LoginBean lg = getLoginBean(request);
		// ƾ֤����
		Result rs = mgt.resetOrderNo(CredTypeID, Integer.parseInt(periodYear), Integer.parseInt(period));
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			new DynDBManager().addLog(14, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"ƾ֤����:"+CredTypeID+"-"+periodYear+"-"+period+";","tblAccMain", "ƾ֤����","ƾ֤����");
			EchoMessage.success().add(getMessage(request, "erp.voucher.credNo.success")).setBackUrl(
					"/VoucherManage.do?optype=resetCredNoPre&winCurIndex=" + request.getAttribute("winCurIndex")).setAlertRequest(request);
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setBackUrl(
					"/VoucherManage.do?optype=resetCredNoPre&winCurIndex=" + request.getAttribute("winCurIndex")).setRequest(request);
		}
		return getForward(request, mapping, "message");
	}

	/**
	 * ��ȡ�ӱ������
	 * 
	 * @param values
	 * @param tableInfo
	 * @param req
	 * @param sunCompany
	 */
	public void readChildTables(HashMap values, DBTableInfoBean tableInfo, HttpServletRequest req, String sunCompany) {
		ArrayList childList = new ArrayList();
		values.put("TABLENAME_" + tableInfo.getTableName(), childList);
		List list = tableInfo.getFieldInfos();
		// �ҳ���¼����

		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);
			if (fieldInfo.getDefaultValue() != null && fieldInfo.getDefaultValue().trim().length() > 0) {
				// ��Ĭ��ֵ�Ĳ��������
				continue;
			} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX) {
				continue;
			} else if ((fieldInfo.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE || fieldInfo.getFieldType() == DBFieldInfoBean.FIELD_INT)
					&& (fieldInfo.getFieldSysType() == null || (fieldInfo.getFieldSysType() != null && !fieldInfo.getFieldSysType().equals("RowMarker")))) {
				continue;
			}
			String[] str = req.getParameterValues("tblAccDetail_" + fieldInfo.getFieldName());
			int c = 0;
			for (int j = 0; str != null && j < str.length; j++) {
				if (str[j] != null && str[j].trim().length() > 0 && !"0".equals(str[j]) && !str[j].trim().equals("-100000")) {
					c = j + 1;
				}
			}
			if (c > count)
				count = c;
		}

		for (int i = 0; i < count; i++) {
			childList.add(new HashMap());
		}

		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);
			String[] str;
			if (fieldInfo.getFieldName().equals("SCompanyID")) {
				str = new String[count];
				for (int j = 0; j < count; j++) {
					str[j] = sunCompany;
				}
			} else {
				str = req.getParameterValues("tblAccDetail_" + fieldInfo.getFieldName());
			}
			if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX
					|| (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX)) {
				for (int j = 0; j < count; j++) {
					String[] checkValue = req.getParameterValues("tblAccDetail_" + fieldInfo.getFieldName() + (j + 1));
					String check = "";
					if (checkValue != null) {
						for (String strs : checkValue) {
							check += strs + ",";
						}
					}
					((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), check);
				}
			} else {
				for (int j = 0; j < count; j++) {
					if (str != null && str[j] != null) {
						if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
							// ������
							((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), KRLanguageQuery.create((Hashtable) req.getSession().getServletContext()
									.getAttribute("LocaleTable"), (Locale) req.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY), str[j]));
						} else {
							if (str[j].length() == 0 && (fieldInfo.getFieldType() == 1 || fieldInfo.getFieldType() == 0)) {
								if (fieldInfo.getDefaultValue() != null && fieldInfo.getDefaultValue().length() > 0) {
									((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), fieldInfo.getDefaultValue());
								} else {
									((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), 0);
								}
							} else {
								((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), str[j]);
							}
						}
					} else {
						if (fieldInfo.getFieldType() == 1 || fieldInfo.getFieldType() == 0) {
							if (fieldInfo.getDefaultValue() != null && fieldInfo.getDefaultValue().length() > 0) {
								((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), fieldInfo.getDefaultValue());
							} else {
								((HashMap) (childList.get(j))).put(fieldInfo.getFieldName(), 0);
							}
						}
					}
				}
			}
		}
	}
}
