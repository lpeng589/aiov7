package com.menyi.aio.web.stockcheck;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.ImportDataBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.importData.*;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.ExportData;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:�̵�Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-10-17 ���� 10:30
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
public class StockCheckAction extends MgtBaseAction{
	DynDBManager dbmgt = new DynDBManager();
	StockCheckMgt mgt = new StockCheckMgt();
	
	String newDate = "";
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
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
		
		DBFieldInfoBean nameField = null;
    	for(DBFieldInfoBean fb : BaseEnv.tableInfos.get("tblStock").getFieldInfos()){
    		if("RowMarker".equals(fb.getFieldSysType())){
    			nameField = fb;
    		}
    	}
    	String StockMarkerName = "StockFullName";
    	if(nameField != null){
    		StockMarkerName = nameField.getFieldName();
    	}
    	request.setAttribute("StockMarkerName", StockMarkerName);
		
		/* ��ͬ���� */
		String optype = request.getParameter("optype");
		
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			if(optype != null && "importPreCheckBill".equals(optype)){
				//����ģ��͵����̵㵥ǰ
				forward = importPreCheckBill(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_ADD:
			if(optype != null && "exportTemplet".equals(optype)){
				//�����̵㵥���ߵ���ģ��
				forward = exportTemplet(mapping, form, request, response);
			}else if(optype != null && "importCheckBill".equals(optype)){
				//�����̵㵥
				forward = importCheckBill(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			if(optype != null && "checkDeal".equals(optype)){
				//�̵㴦��
				forward = checkDeal(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUERY:
			if(optype != null && "stockNext".equals(optype)){
				//��ѯҳ����ұֿ߲�����
				forward = queryStockNext(mapping, form, request, response);
			}else if(optype != null && "stockPrepare".equals(optype)){
				//�̵�׼��
				forward = stockPrepare(mapping, form, request, response);
			}else if(optype != null && "stockCancelPrepare".equals(optype)){
				//ȡ���̵�׼��
				forward = stockCancelPrepare(mapping, form, request, response);
			}else if(optype != null && "stockCheckDealList".equals(optype)){
				//�̵㴦���б�
				forward = stockCheckDealList(mapping, form, request, response);
			}else if(optype != null && "delStockCheck".equals(optype)){
				//ɾ���̵㵥
				forward = delStockCheck(mapping, form, request, response);
			}else if(optype != null && "dealFormSubmit".equals(optype)){
				//����������Զ����б������
				forward = dealFormSubmit(mapping, form, request, response);
			}else{
				//��ѯ����
				forward = queryStockCheck(mapping, form, request, response);
			}
			break;
		default:
			//��ѯ�б�
			forward = queryStockCheck(mapping, form, request, response);
		}
		return forward;
	}
	
	private String replace( String name){
		return  name.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		
	}
	/**
	 * ��ѯ�̵�����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryStockCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		DBFieldInfoBean nameField = null;
    	for(DBFieldInfoBean fb : BaseEnv.tableInfos.get("tblStock").getFieldInfos()){
    		if("RowMarker".equals(fb.getFieldSysType())){
    			nameField = fb;
    		}
    	}
    	String fieldName = "StockFullName";
    	if(nameField != null){
    		fieldName = nameField.getFieldName();
    	}
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/StockCheckAction.do");
		/**
		 * ��ѯ����
		 */
		String folderTree = "[";
		Result rs = mgt.queryAllStock(this.getLoginBean(request), mop);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			List stockList = (ArrayList)rs.retVal;
			int num =0;
			//�����ݽ��д����������ϳɽڵ���ʽ
			folderTree += "{ id:\"0\", pId:-1, name:\"�ֿ�Ŀ¼\"}";
			if(stockList.size()>0){
				folderTree += ",";
			}
			for(int i=0;i<stockList.size();i++){
				HashMap o = (HashMap)stockList.get(i);
				if(String.valueOf(o.get("classCode")).length() == 5){
					//���ڵ�
					folderTree += "{ id:\""+o.get("classCode")+"\", pId:0, name:\""+replace(String.valueOf(o.get(fieldName)))+"\"}";
				}else{
					String classC = ((String)o.get("classCode")).substring(0, ((String)o.get("classCode")).length()-5);
					folderTree += "{ id:\""+o.get("classCode")+"\", pId:\""+classC+"\", name:\""+replace(String.valueOf(o.get(fieldName)))+"\"}";
				}
				if(num != stockList.size()-1){
					folderTree+=",";
				}
				num ++;
			}
		}
		folderTree += "]";
		request.setAttribute("stockData", folderTree);
		return getForward(request, mapping, "stockCheckList");
	}
	
	/**
	 * ��ѯҳ����ұֿ߲�����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward queryStockNext(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		String searchType = request.getParameter("searchType");							//�������ͣ�����ֿ⣬�ؼ���������
		String searchValue = request.getParameter("searchValue");						//����ֵ���ֿ�classCode,�����֣�
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/StockCheckAction.do");
		//��ѯ����
		Result rs = mgt.queryStock(searchType, searchValue, this.getLoginBean(request), mop);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			List stockList = (ArrayList)rs.retVal;
			String[] s = new String[]{"id","classCode","StockNumber","StockName","StockFullName","stockPreId","CheckDate","statusId","stockPreStatus","stockInputStatus","stockDealStatus"};
			StringBuffer str = new StringBuffer("[");
			for(int i = 0; i<stockList.size();i++){
				HashMap map = (HashMap)stockList.get(i);
				str.append("{");
				for(int j =0;j<s.length;j++){
					Object o = map.get(s[j])==null?"":map.get(s[j]);
					String so  = GlobalsTool.replaceSpecLitter(o.toString());
					str.append("\""+s[j]+"\": \""+so+"\"");
					if(j<s.length-1){
						str.append(",");
					}
				}
				str.append("}");
				if(i<stockList.size()-1){
					str.append(",");
				}
			}
			str.append("]");
			request.setAttribute("msg", str.toString());
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * �̵�׼��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward stockPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		//�ֿ�classCode
		String stockCode = request.getParameter("stockCode");
		LoginBean loginBean = this.getLoginBean(request);
		
		Result rs = mgt.stockPre(stockCode,loginBean.getId(),loginBean.getSunCmpClassCode());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
			request.setAttribute("msg", "OK;");
		}else{
			//�洢���̴���
			String errorMessage = rs.getRetVal()==null?"":rs.getRetVal().toString();
			request.setAttribute("msg", "ERROR;"+errorMessage);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * ȡ���̵�׼��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward stockCancelPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		String stockPreId = request.getParameter("stockPreId");				//�̵�׼����ID
		String classCode = request.getParameter("classCode");				//�ֿ�classCode
		Result rs = mgt.queryStockFullName(classCode);
		LoginBean lg = getLoginBean(request);
		String stockName = "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			stockName = String.valueOf(rs.getRetVal());
		}
		rs = mgt.stockCancelPre(stockPreId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			new DynDBManager().addLog(14, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "ȡ���ֿ��̵�׼��:"+stockName, "tblStockCheckPrepare", "�̵�����", "ȡ���̵�׼��");
			request.setAttribute("msg", "OK");
		}else{
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * �̵㴦���б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward stockCheckDealList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		//�̵�׼����Id
		String stockPreId = request.getParameter("stockPreId");
		//�ֿ�classCode
		String stockCode = request.getParameter("stockCode");
		//�̵�����
		String stockCheckDate= request.getParameter("stockCheckDate");

		String workFlowNodeName = request.getParameter("workFlowNodeName");					//''δ���ˣ�finish�ѹ��ˣ�allȫ��
		
		//��ѯ�ֿ������
		Result rs = mgt.queryStockFullName(stockCode);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("stockName", rs.retVal);
		}
		
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		int pageNos = 1;
		int pageSizes = 100;
		if(pageNo != null && !"".equals(pageNo)){
			pageNos = Integer.parseInt(pageNo);
		}
		if(pageSize != null && !"".equals(pageSizes)){
			pageSizes = Integer.parseInt(pageSize);
		}
		
		//��ѯ�̵㵥������
		rs = mgt.stockCheckList(stockPreId ,stockCode, stockCheckDate, workFlowNodeName, pageNos, pageSizes);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List stockCheckList = (ArrayList)rs.retVal;
			request.setAttribute("pageBar", this.pageBar(rs, request));
			request.setAttribute("stockCheckList",stockCheckList);
		}
		request.setAttribute("stockPreId", stockPreId);
		request.setAttribute("stockCode", stockCode);
		request.setAttribute("stockCheckDate", stockCheckDate);
		request.setAttribute("workFlowNodeName", workFlowNodeName);
		return getForward(request, mapping, "stockCheckBillList");
	}
	
	/**
	 * �̵㴦��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param repsonse
	 * @return
	 */
	protected ActionForward checkDeal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse repsonse){
		
		String checkType = request.getParameter("checkType");				//�̵㷽ʽ
		
		String stockCode = request.getParameter("stockCode");				//�ֿ�
		String stockPreId = request.getParameter("stockPreId");				//׼����ID
		String stockCheckDate= request.getParameter("stockCheckDate");		//�̵�����
		
		LoginBean lg = this.getLoginBean(request);
		
		String SCompanyID = lg.getSunCmpClassCode();						//��֧����
		
		//�̵㴦��
		Result rs = mgt.checkDeal(this.getLocale(request).toString(),SCompanyID,lg.getId(),stockPreId,checkType);
		String url = "/StockCheckAction.do?operation=4&optype=stockCheckDealList&stockCode="+stockCode+"&stockPreId="+stockPreId+"&stockCheckDate="+stockCheckDate;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
			EchoMessage.success().add("�̵㴦��ɹ�").setBackUrl(url).setAlertRequest(request);
		}else if(rs.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL){
			EchoMessage.error().add("����������½��·ݵĵ���������").setBackUrl(url).setAlertRequest(request);
		}else{
			String errorMessage = rs.getRetVal().toString();
			EchoMessage.error().add("�̵㴦��ʧ��,"+errorMessage).setBackUrl(url).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * ɾ���̵㵥
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delStockCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String id= request.getParameter("id");			//�̵㵥Id
		Result rs = mgt.delCheckBill(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", "OK");
		}else if(rs.retCode == ErrorCanst.RET_HAS_AUDITING){
			//�޷�ɾ��
			request.setAttribute("msg", rs.getRetVal());
		}else{
			request.setAttribute("msg", "ERROR");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * �鿴��ť�����Զ���ҳ��ʱ����ת�ٽ��Զ����б����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward dealFormSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String paramStr = request.getQueryString();
        List list = new ArrayList();
        if(paramStr != null && !"".equals(paramStr)){
        	String[] paramSplit = paramStr.split("&");
        	for(String s : paramSplit){
        		if(s != null && !"".equals(s) && s.indexOf("=")!=-1){
        			HashMap map = new HashMap();
        			String val = URLDecoder.decode(s.split("=")[1],"UTF-8");
        			val  = val == null?"":GlobalsTool.replaceSpecLitter(val);
        			map.put(s.split("=")[0], val);
        			list.add(map);
        		}
        	}
        }
        request.setAttribute("list", list);
        return getForward(request, mapping, "dealForm");
	}
	
	/**
	 * �����̵㵥ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward importPreCheckBill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String stockCode = request.getParameter("stockCode");
		/**
		 * ����ֿ�Ϊ�գ����ѯ���ݿ�õ��ֿ������
		 */
		Result rs = mgt.queryStockCheckPre(stockCode);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("list", rs.getRetVal());
		}
		request.setAttribute("stockCode", stockCode);
		return getForward(request, mapping, "importCheckBill");
	}
	
	
	/**
	 * ����ģ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward exportTemplet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String stockPreId = request.getParameter("stockPreId");						//�̵�׼����ID
		String stockCode = request.getParameter("stockCode");						//�ֿ���
		String impmType = request.getParameter("impmoduleType");						//ģ�����ͣ�1��Ʒ��׼ģ��  2������ģ��  3���к�ģ�壩
		
		String isModule = request.getParameter("isModule");							//�Ƿ��ǵ���ģ�壨true��,Ϊ��ʱ�����̵㵥����
		
		File file = new File("../../AIOBillExport");
		if(!file.exists()){
			file.mkdirs() ;
		}
		//��ѯ�ֿ������
		Result rs = mgt.queryStockFullName(stockCode);
		String stockName = "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			stockName = String.valueOf(rs.retVal);
			request.setAttribute("stockName", stockName);
		}
		String moduleName = "";
		if("1".equals(impmType)){
			moduleName = "��Ʒ";
		}else if("2".equals(impmType)){
			moduleName = "������";
		}else if("3".equals(impmType)){
			moduleName = "���к�";
		}
		String fileName = file.getAbsolutePath()+"\\"+stockName+"-"+moduleName+".xls" ;
		
        FileOutputStream fos = new FileOutputStream(fileName);
		ArrayList<ExportField> exportList = new ArrayList<ExportField>();								//������ֶ�ͷ��
		ArrayList<HashMap> exportValueList = new ArrayList<HashMap>();									//����
		Object localeObj = request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (localeObj == null) {
			localeObj = request.getSession().getServletContext().getAttribute("DefaultLocale");
		}
		final String locale = localeObj == null ? "" : localeObj.toString();
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);	//��ѯ���б�ṹ
		List<DBFieldInfoBean> fieldInfos = ((DBTableInfoBean) allTables.get("tblStockCheckDet")).getFieldInfos();
		DBTableInfoBean mainTable = GlobalsTool.getTableInfoBean("tblStockCheckDet");
		/**
		 * ��Ҫ�������ֶν��д���
		 */
		ExportField ef = null;
		if("1".equals(impmType) ){
				//��Ʒ
//			tableName,fieldName,reportView,billView,popSel,keyword,popupView
            for (String[] shows : BaseEnv.reportShowSet) { 
				if ("tblGoods".equals(shows[0]) && "1".equals(shows[6]) && "1".equals(shows[3])) {
					ef = new ExportField("main", "", shows[1],0, GlobalsTool.getFieldDisplay(allTables, "tblGoods."+shows[1], locale), mainTable.getDisplay().get(locale),
							false);
					exportList.add(ef);
				}
            }
		}else if("3".equals(impmType)){
			ef = new ExportField("main", "", "Seq",0, GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Seq", locale), mainTable.getDisplay().get(locale),false);
			exportList.add(ef);
		} else if("2".equals(impmType)){
			ef = new ExportField("main", "", "BarCode",0, GlobalsTool.getFieldDisplay(allTables, "tblGoods.BarCode", locale), mainTable.getDisplay().get(locale)+" (����������ο����Բ���,�̵������Բ���Ĭ��Ϊ1)",false);
			exportList.add(ef);
		}
		//���к��̵㣬�������кţ�û�������κ���Ϣ
		if(!"3".equals(impmType)){
			for (DBFieldInfoBean row : fieldInfos) {
				if("GoodsField".equals(row.getFieldSysType()) && row.getInputType() != DBFieldInfoBean.INPUT_HIDDEN && !row.getFieldName().equals("Seq") ){
					//��Ʒ����
					ef = new ExportField("main", "", row.getFieldName(),0, row.getDisplay().get(locale), mainTable.getDisplay().get(locale),
							row.getFieldType()==DBFieldInfoBean.FIELD_INT||row.getFieldType()==DBFieldInfoBean.FIELD_DOUBLE?true:false);
					exportList.add(ef);
				}
			}
			
			ef = new ExportField("main", "", "lastQty",0, GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.LastQty", locale.toString()), mainTable.getDisplay().get(locale),false);
			exportList.add(ef);
			ef = new ExportField("main", "", "Qty",0, GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Qty", locale.toString()), mainTable.getDisplay().get(locale),false);
			exportList.add(ef);
		}
		/**
		 * ��ѯ����
		 */
		rs = mgt.queryStocksCheckGoodsId(stockPreId, Integer.parseInt(impmType));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			exportValueList = (ArrayList)rs.retVal;	
		}
		
		Result result = ExportData.billExport(fos, "�̵㵥", exportList, exportValueList,stockName+"-"+moduleName);
		fos.close();
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			if(isModule != null && "true".equals(isModule)){
				//�����̵㵥ģ��
				String downUrl = "/ReadFile?tempFile=export&fileName="+GlobalsTool.encode(stockName+"-"+moduleName+".xls");
				request.setAttribute("msg", downUrl);
				return getForward(request, mapping, "blank");
			}
		} else {
			request.setAttribute("msg", "����ʧ��"+result.getRetVal());
			return getForward(request, mapping, "blank");
		}
		request.setAttribute("msg", "����ʧ��"+result.getRetVal());
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * �����̵㵥
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward importCheckBill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		Locale locale = this.getLocale(request);
		String importName = request.getParameter("importName");							//�õ�����tblStockCheckDet
		String impmType = request.getParameter("impmoduleType");							//ѡ���ģ��
		String stockCodes = request.getParameter("stockCode");							//�ֿ�
		String urls = "/StockCheckAction.do?operation=6&optype=importPreCheckBill&stockCode="+stockCodes;
		/**
		 * ���ѯ���ݿ�õ��ֿ������
		 */
		
		
		Result rs = mgt.queryStockCheckPre(stockCodes);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("list", rs.getRetVal());
		}
		request.setAttribute("stockCode", stockCodes);
		String newDates= request.getParameter("newDate");
		if(!"".equals(newDate) && newDate.equals(newDates)){
			return mapping.findForward("importCheckBill");
		}
		newDate = newDates;
		Hashtable<String, DBTableInfoBean> allTables = (Hashtable<String, DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/StockCheckAction.do");
		//�õ���ṹ
		DBTableInfoBean tableInfoBean = allTables.get(importName);
		String mainTableDisplay = tableInfoBean.getDisplay().get(locale.toString()); //��¼�������������
		try{
			//����ģ������
	        ImportForm importForm = (ImportForm) form;
	        JXLTOOL jxlTool = new JXLTOOL(importForm.getFileName());
	        Result exceldatars = jxlTool.getExcelData(true);
	        if (exceldatars.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
	        	//��ȡ���ݳɹ�
	        	//���ģ���б�������ȷ		        
				
				ArrayList<DBFieldInfoBean> fieldInfoList = new ArrayList<DBFieldInfoBean>();
				
				fieldInfoList.addAll(tableInfoBean.getFieldInfos());
				List<HashMap<String, ExcelFieldInfoBean>> list = (List<HashMap<String, ExcelFieldInfoBean>>) exceldatars.getRetVal();
				jxlTool.close();
				
				//���������ݺ���ϸ���ݴ洢��values��
				HashMap values = new HashMap();
				/**
				 * ���ݲֿ��ѯ�ֿ�׼��������
				 */
				
				rs = mgt.queryStockCheckName(stockCodes);
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.error().add("���鵼��ģ��ֿ��Ƿ���ȷ��").setBackUrl(urls).setAlertRequest(request);
					return mapping.findForward("alert");
				}else{
					HashMap stockMap = (HashMap)rs.getRetVal();
					if(stockMap == null || stockMap.size()==0){
						EchoMessage.error().add("����ֿ��Ƿ�����׼��״̬��").setBackUrl(urls).setAlertRequest(request);
						return mapping.findForward("alert");
					}else{
						values = stockMap;
					}
				}
				
				/**
				 * ���봦��
				 */
				int success = 0;			//�ɹ�����
				rs = importDataDeal(values,request, tableInfoBean, list, jxlTool, mainTableDisplay, locale, mop, this.getLoginBean(request),Integer.parseInt(impmType));
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					String[] sr  = (String[])rs.retVal;
					request.setAttribute("fileName", sr[3]);
					request.setAttribute("totalimport", sr[0]);
					request.setAttribute("successimport", sr[1]);
					request.setAttribute("errorimport", sr[2]);
					request.setAttribute("stockCode", stockCodes);
					return mapping.findForward("importResult");
				}else{
					if(rs.getRetVal() == null){
						EchoMessage.error().add(getMessage(request, "import.failure.number")).setAlertRequest(request);
					}else if(rs.getRetVal() instanceof String[]){
						String[] str = (String[]) rs.getRetVal();
						String msg = new DynDBManager().getDefSQLMsg(locale.toString(), str[0]);
						EchoMessage.error().add(msg).setBackUrl(urls).setAlertRequest(request);
					}else{
						EchoMessage.error().add(rs.getRetVal().toString()).setBackUrl(urls).setAlertRequest(request);
					}
					return mapping.findForward("alert");
				}
	        } else {
				jxlTool.close();
				// ����������Ϣ,��ȡ����ʧ��
				EchoMessage.error().add(GlobalsTool.getMessage(locale, "excel.lb.readerror")).setBackUrl(urls).setAlertRequest(request);
				return mapping.findForward("alert");
			}
		}catch (Exception e) {
			EchoMessage.error().add(GlobalsTool.getMessage(locale, "common.msg.error")).setBackUrl(urls).setAlertRequest(request);
			return mapping.findForward("alert");
		}
	}
	

	
	private void rowErrorHandler(String msg, HashMap<String, ExcelFieldInfoBean> groupItem,List<HashMap<String, ExcelFieldInfoBean>> errorList) {
		ExcelFieldInfoBean eBean = new ExcelFieldInfoBean();
		eBean.setValue(msg);
		groupItem.put("Error", eBean);
		errorList.add(groupItem);
	}
	
	
	
	/**
	 * ���ݴ���������ݣ�
	 * @param values                �����ֵ
	 * @param request               request
	 * @param allTables             ���б�����
	 * @param list                  ���������
	 * @param jxlTool				excel�ܵ�����
	 * @param mainTableDisplay		������������
	 * @param locale                ����������
	 * @param mop					Ȩ��
	 * @param lg					��½��Ϣ
	 * @param impmType			ģ������
	 */
	public Result importDataDeal(HashMap values,HttpServletRequest request, DBTableInfoBean tableInfoBean,List<HashMap<String, ExcelFieldInfoBean>> list,JXLTOOL jxlTool, 
			String mainTableDisplay, Locale locale,MOperation mop,LoginBean lg,int impmType){
		
		//��¼����ʧ�ܵ�����
		List<HashMap<String, ExcelFieldInfoBean>> errorList = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
		Result rs = new Result();
		
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);	//��ѯ���б�ṹ
		
		//����ڼ�
		Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		
		values.put("Period", accPeriodBean.getAccPeriod());
		values.put("PeriodMonth", accPeriodBean.getAccMonth());
		values.put("PeriodYear", accPeriodBean.getAccYear());
		
		
		List rowList = new ArrayList();						//����������ֶ�
		if(impmType==1){
            for (String[] shows : BaseEnv.reportShowSet) { 
				if ("tblGoods".equals(shows[0]) && "1".equals(shows[6]) && "1".equals(shows[3])) {
					rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblGoods."+shows[1], locale.toString())+";"+shows[1]);					
				}
            }
			for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
				if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
					rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet."+gpBean.getPropName(), locale.toString())+ ";"+gpBean.getPropName());
				}
			}
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.LastQty", locale.toString())+";LastQty");
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Qty", locale.toString())+";Qty");
		}else if(impmType == 3){
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Seq", locale.toString())+";Seq");
		} else if(impmType == 2){
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblGoods.BarCode", locale.toString())+";BarCode");
			
			for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
				if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
					rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet."+gpBean.getPropName(), locale.toString())+ ";"+gpBean.getPropName());
				}
			}
			
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.Qty", locale.toString())+";Qty");
			rowList.add(GlobalsTool.getFieldDisplay(allTables, "tblStockCheckDet.LastQty", locale.toString())+";LastQty");
		}
		
		
		Object ob = request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		ImportDataBean importBean = new ImportDataBean();
		importBean.setName(mainTableDisplay);
		
		ArrayList childList = new ArrayList();
		values.put("TABLENAME_tblStockCheckDet", childList);
		for (HashMap<String, ExcelFieldInfoBean> excelMap : list) {
			//������ϸ������
			List<HashMap<String, ExcelFieldInfoBean>> group = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
			group.add(excelMap);
			HashMap fieldVal = new HashMap();
			for(int i=0;i<rowList.size();i++){
				String field = (String)rowList.get(i);
				String excelValue = excelMap.get(field.split(";")[0])==null?"":excelMap.get(field.split(";")[0]).getValue();
				fieldVal.put(field.split(";")[1], excelValue);
			}
			if(impmType == 3 && fieldVal.get("Seq") != null && !fieldVal.get("Seq").equals("")){
				//�������кţ������ݿ��в�ѯ��ص���Ʒ������
				Result rr = mgt.checkSeq((String)fieldVal.get("Seq"), childList, (String)values.get("PrepareId"), (String)values.get("StockCode"));
				if(rr.retCode != ErrorCanst.DEFAULT_SUCCESS){
					rowErrorHandler((String)rr.getRetVal(),excelMap,errorList);
				}
			}else if(impmType == 2 ) {
				//��֤��Ʒ�Ƿ����				
				if(fieldVal.get("LastQty") == null || "".equals(fieldVal.get("LastQty")) ||
						!Pattern.compile("[0-9]*").matcher(fieldVal.get("LastQty").toString()).matches()){
					fieldVal.put("LastQty","0");
				}
				//��һ�еı�ʶ��Ϊ�ղ����̵�������Ϊ��ʱ�����е���
				if(fieldVal.get("BarCode")!= null && !"".equals(fieldVal.get("BarCode"))){
					if(fieldVal.get("Qty") != null && !"".equals(fieldVal.get("Qty"))){
						//if(!Pattern.compile("[0-9]*").matcher(fieldVal.get("Qty").toString()).matches()){
						//	groupErrorHandler("�̵���������Ϊ��������", group, errorList);
						if(!Pattern.compile("^\\d+(\\.\\d+)?$").matcher(fieldVal.get("Qty").toString()).matches()){
							groupErrorHandler("�̵���������Ϊ���֣�", group, errorList);
							continue;
						}
					}else{ //����Ϊ��
						fieldVal.put("Qty","1");
					}
				}
				rs = mgt.exportValidate(fieldVal, impmType, (String)values.get("PrepareId"));
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){ 
						groupErrorHandler(""+rs.retVal, group, errorList);
						continue;
				}
				fieldVal.put("GoodsCode", rs.getRetVal());
				childList.add(fieldVal);
			}else{
				//��֤��Ʒ�Ƿ����
				
				if(fieldVal.get("LastQty") == null || "".equals(fieldVal.get("LastQty")) ||
						!Pattern.compile("[0-9]*").matcher(fieldVal.get("LastQty").toString()).matches()){
					fieldVal.put("LastQty","0");
				}
				//��һ�еı�ʶ��Ϊ�ղ����̵�������Ϊ��ʱ�����е���
				if(fieldVal.get("Qty") != null && !"".equals(fieldVal.get("Qty"))){
					//if(!Pattern.compile("[0-9]*").matcher(fieldVal.get("Qty").toString()).matches()){
					if(!Pattern.compile("^\\d+(\\.\\d+)?$").matcher(fieldVal.get("Qty").toString()).matches()){
						groupErrorHandler("�̵���������Ϊ���֣�", group, errorList);
						continue;
					}
				}else{ //����Ϊ��
					groupErrorHandler("�̵���������Ϊ�գ�", group, errorList);
					continue;
				}
				
				rs = mgt.exportValidate(fieldVal, impmType, (String)values.get("PrepareId"));
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){ 
						groupErrorHandler(""+rs.retVal, group, errorList);
						continue;
				}
				fieldVal.put("GoodsCode", rs.getRetVal());
				childList.add(fieldVal);
			}
		}
		
		if(impmType == 3){
			//�����Ʒ���̵�����
			mgt.inputLastQty(childList, (String)values.get("PrepareId"));
		}
		
		/* �������ڴ������б�ṹ����Ϣ */
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		
		/* ����̵㵥���� */
		Result result = mgt.addStockCheck(values, lg,(DBTableInfoBean)map.get("tblStockCheck"), locale, resources, map,request);
		rs.setRetCode(result.getRetCode());
		rs.setRetVal(result.getRetVal());
		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}else{
			String fileName = "";
			if(errorList.size() >0){
				fileName = "../log/stockCheck_" + lg.getId() + ".xls";
				File file = new File(fileName);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				String error = GlobalsTool.getMessage(locale, "excel.error.title");
				jxlTool.writeExcel(fileName, errorList, error);
			}
			rs.retVal = new String[]{""+list.size(),""+(list.size()-errorList.size()),""+errorList.size(),fileName};
		}
		
		return rs;
	}
	
	/**
	 * ʧ��
	 * @param importState
	 * @param errorCode
	 * @param errorValue
	 * @param group
	 * @param errorList
	 * @param allTables
	 * @param isAutoGoods
	 */
	private void groupErrorHandler(String errorValue, List<HashMap<String, ExcelFieldInfoBean>> group, List<HashMap<String, ExcelFieldInfoBean>> errorList) {
		errorList.addAll(group);
		if (group.get(0).get("Error") == null) {
			ExcelFieldInfoBean eBean = new ExcelFieldInfoBean();
			eBean.setValue(errorValue);
			group.get(0).put("Error", eBean);
		}
	}
}
