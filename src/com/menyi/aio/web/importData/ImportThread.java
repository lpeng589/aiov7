package com.menyi.aio.web.importData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.koron.crm.bean.ClientModuleViewBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BomBean;
import com.menyi.aio.bean.ImportDataBean;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.bom.BomMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.aio.web.iniSet.IniAccMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.PublicMgt;
public class ImportThread extends Thread {
	public boolean isRuning = false;

	public String fileName;
	public String mainTableDisplay;
	private ImportDataMgt importDataMgt = new ImportDataMgt();
	public ImportState importState = new ImportState(0, 0, 0);
	private PublicMgt publicMgt = new PublicMgt();
	ServletContext servletContext;

	String moduleType;
	String moduleParam;
	Locale locale;
	String importName;
	ImportForm importForm;
	String isCrmImport;
	String moduleId;
	LoginBean loginBean;
	JXLTOOL jxlTool;

	public ClientModuleViewBean moduelViewbean;

	MOperation mop;

	private List<HashMap<String, ExcelFieldInfoBean>> list = null;

	boolean isBom = false;

	Hashtable<String, DBTableInfoBean> allTables;

	DBTableInfoBean tableInfoBean;

	public ActionForward initImport(boolean isBom, ActionMapping mapping, String importName, Locale locale, HttpServletRequest request, String moduleType, String moduleParam, String isCrmImport,
			String moduleId, ImportForm importForm, LoginBean loginBean) {
		this.isBom = isBom;

		this.setName(importName + "ImportThread");
		this.importName = importName;
		this.locale = locale;
		mop = GlobalsTool.getMOperationMap(request);
		servletContext = request.getSession().getServletContext();

		allTables = (Hashtable<String, DBTableInfoBean>) servletContext.getAttribute(BaseEnv.TABLE_INFO);
		tableInfoBean = allTables.get(importName);
		mainTableDisplay = tableInfoBean.getDisplay().get(locale.toString()); //��¼�������������

		this.moduleType = moduleType;
		this.moduleParam = moduleParam;
		this.importForm = importForm;
		this.isCrmImport = isCrmImport;
		this.moduleId = moduleId;
		this.loginBean = loginBean;

		try {
			jxlTool = new JXLTOOL(importForm.getFileName());
			moduelViewbean = new ClientModuleViewBean();
			if (isCrmImport != null && "true".equals(isCrmImport)) {
				Result res = publicMgt.findmoduleViewByName(jxlTool.getSheetName(), moduleId);
				List<ClientModuleViewBean> moduleViewList = (List<ClientModuleViewBean>) res.retVal;
				if (moduleViewList != null && moduleViewList.size() == 0) {
					EchoMessage.error().add("û�д���ͼ,����").setBackUrl("/CRMClientAction.do?operation=91&NoBack=NoBack&moduleId=" + moduleId).setAlertRequest(request);
					return mapping.findForward("alert");
				} else {
					moduelViewbean = moduleViewList.get(0);
				}
				//��CRM�������,Ĭ��setAttributeģ��Id����ˢ��ҳ��ʱ��ת�ж�
				request.setAttribute("moduleId", moduleId);
			}else if(isCrmImport != null && "stockCheck".equals(isCrmImport)){
				//�̵㵥����ʱ
				request.setAttribute("moduleId", moduleId);
			}
			boolean blankTitle = false;
			if(importName.equals("tblIniAccDet")){
				blankTitle = true;
			}
			
			Result exceldatars = jxlTool.getExcelData(mainTableDisplay,blankTitle);
			
			if (exceldatars.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList<DBTableInfoBean> chileTables = DDLOperation.getChildTables(importName, allTables);
				if(jxlTool.totalCells==0){
					EchoMessage.error().add("����ģ����������Ϊ0����ȷ��ģ���������ݣ����븴�����ݵ�һ���µ�excel�ĵ������µ���").setBackUrl(
							"/importDataAction.do?fromPage=importList&operation=91&tableName=" + importName + "&parentTableName=&moduleType=" + moduleType).setAlertRequest(request);
					return mapping.findForward("alert");
				}
				//���ģ���б����Ƿ���ȷ	noTitle Ϊtrueһ���Ƿ��Զ���ģ�壬����Ҫ��ͷ������Ҳ���������Ƿ���ȷ	        
				for(int i=0;i<jxlTool.getTopTitle().size()&& !blankTitle;i++){ 
					String[] ss = jxlTool.getTopTitle().get(i);
					boolean isSame = false;
					
					if(mainTableDisplay.equals(ss[0])){
						isSame = true;
					}
					if(!isSame){
						for(DBTableInfoBean fb:chileTables){
							if(fb.getDisplay().get(locale.toString()).equals(ss[0])){
								isSame = true;
							}
				        }
					}
					if(!isSame){
						EchoMessage.error().add("���鵼��ģ���б���\""+ss[0]+"\"�Ƿ���ȷ").setBackUrl(
								"/importDataAction.do?fromPage=importList&operation=91&tableName=" + importName + "&parentTableName=&moduleType=" + moduleType).setAlertRequest(request);
						return mapping.findForward("alert");
					}
				}
				ArrayList<DBFieldInfoBean> fieldInfoList = new ArrayList<DBFieldInfoBean>();
		        
		        fieldInfoList.addAll(tableInfoBean.getFieldInfos());
		        for(DBTableInfoBean fb:chileTables){
		        	fieldInfoList.addAll(fb.getFieldInfos());
		        }
		        
		        //���ģ�����ֶ��Ƿ���ȷ
//				for (int i = 0; i < jxlTool.headers.size(); i++) {
//					ExcelFieldInfoBean ss = jxlTool.headers.get(i);
//					if(ss.getName().equals(mainTableDisplay + ":" + GlobalsTool.getMessage(locale, "importData.parent")) || ss.getName().equals("")){
//						continue;
//					}
//					boolean isSame = false;
//					for (DBFieldInfoBean fieldInfoBean : fieldInfoList) {
//						if (fieldInfoBean.getInputType() == 100 || fieldInfoBean.getFieldName().equals("id") || fieldInfoBean.getFieldName().equals("f_brother")) {
//							continue;
//						}
//						GoodsPropInfoBean tempProp = GlobalsTool.getPropBean(fieldInfoBean.getFieldName());
//						if ((fieldInfoBean.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE
//								|| (fieldInfoBean.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldInfoBean.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE) || (fieldInfoBean
//								.getInputType() == DBFieldInfoBean.INPUT_HIDDEN && fieldInfoBean.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE))
//								&& !(tempProp != null && tempProp.getIsSequence() == 1)/* ��������кŲ������� */
//								&& fieldInfoBean.getInputValue() != null) {
//							PopupSelectBean selectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(fieldInfoBean.getInputValue());
//							if (selectBean == null) {
//								continue;
//							}
//							for (PopField pop : (ArrayList<PopField>) selectBean.getDisplayField2()) {
//								String displayField = pop.getDisplay();
//		                		if(displayField==null || displayField.length()==0){
//		                			displayField = pop.getFieldName();
//		                		}
//		                		//����ֶ���saveField���Ƿ����,���Ƿ����أ�������ڲ���δ��������saveField�е�display
//		                		for(PopField spop:(ArrayList<PopField>)selectBean.getSaveFields()){
//		                			if(pop.getFieldName().equals(spop.getFieldName())&&spop.isHidden()==false){
//		                				displayField = spop.getParentName();
//		                			}
//		                		}
//		                		displayField = displayField.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
//		                		//ȡ���� �����бȽ�
//		                		String display =GlobalsTool.getFieldDisplay(allTables, displayField, locale.toString());
//								if (ss.getName().equals(fieldInfoBean.getTableBean().getDisplay().get(locale.toString()) + ":" + display )) {
//									isSame = true;
//									break;
//								}
//							}
//						} else {
//							if (ss.getName().equals(fieldInfoBean.getTableBean().getDisplay().get(locale.toString()) + ":" + fieldInfoBean.getDisplay().get(locale.toString()))) {
//								isSame = true;
//								break;
//							}
//						}
//					}
//					if (!isSame) {
//						EchoMessage.error().add("���鵼��ģ�����ֶ���\"" + ss.getName() + "\"�Ƿ���ȷ").setBackUrl(
//								"/importDataAction.do?fromPage=importList&operation=91&tableName=" + importName + "&parentTableName=&moduleType=" + moduleType).setAlertRequest(request);
//						return mapping.findForward("alert");
//					}
//				}
				
				list = (List<HashMap<String, ExcelFieldInfoBean>>) exceldatars.getRetVal();
				jxlTool.close();
			} else {
				jxlTool.close();
				// ����������Ϣ,��ȡ����ʧ��
				EchoMessage.error().add(GlobalsTool.getMessage(locale, "excel.lb.readerror")).setBackUrl(
						"/importDataAction.do?fromPage=importList&operation=91&tableName=" + importName + "&parentTableName=&moduleType=" + moduleType).setAlertRequest(request);
				return mapping.findForward("alert");
			}
		} catch (Exception e) {
			EchoMessage.error().add(GlobalsTool.getMessage(locale, "common.msg.error")).setBackUrl(
					"/importDataAction.do?fromPage=importList&operation=91&tableName=" + importName + "&parentTableName=&moduleType=" + moduleType).setAlertRequest(request);
			return mapping.findForward("alert");
		}
		request.getSession().setAttribute(importName+"ImportThread", this);
		isRuning = true;
		this.start();

		return mapping.findForward("importProcess");
	}

	private void importDataBom() {
		try {
//
//			//�����ǰ����ı��ĸ�ʽ���Ǵ˱��ĵ���ģ��ĸ�ʽ����ôҪ�ҵ�ӳ���ϵ
//			HashMap map = null;
//
//			//��¼����ʧ�ܵ�����
//			List<HashMap<String, ExcelFieldInfoBean>> errorList = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
//
//			//�������ӱ�����ݽ��з����������������ͬ����Ϊ�գ�˵����ͬһ����������Ϊ�ǲ������������ӱ��ϵ��
//			List<List<HashMap<String, ExcelFieldInfoBean>>> groupList = new ArrayList<List<HashMap<String, ExcelFieldInfoBean>>>();
//			String templateError = "";
//			String sameStr = null;
//			List<HashMap<String, ExcelFieldInfoBean>> group = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
//			//BOM������ֻҪ��Ʒ��źͣ�����һ�£�������Ϊ��һ��
//			for (HashMap<String, ExcelFieldInfoBean> excelMap : list) {
//				String gooldsNumber = excelMap.get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblGoods.GoodsNumber", locale.toString())).getValue();
//				String gooldsName = excelMap.get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblGoods.GoodsFullName", locale.toString())).getValue();
//				String version = excelMap.get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.VersionNO", locale.toString())).getValue();
//				if (sameStr == null) {
//					group.add(excelMap);
//					sameStr = gooldsNumber+ gooldsName+version;
//				} else if (gooldsNumber==null || gooldsNumber.length() ==0 || sameStr.equals(gooldsNumber+ gooldsName+version)) {
//					group.add(excelMap);
//				} else {
//					groupList.add(group);
//					group = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
//					group.add(excelMap);
//					sameStr = gooldsNumber+ gooldsName+version;
//				}
//			}
//
//			if (group.size() > 0)
//				groupList.add(group);
//
//			/**
//			 * session�н�����Ӧ������Ϣ
//			 */
//			importState = new ImportState(groupList.size(), 0, 0);
//
//			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, importName);
//			/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
//			OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(importName);
//			String addMessage = GlobalsTool.getMessage(locale, "common.lb.add");
//			// ��ȡ·��
//			String path = servletContext.getRealPath("DoSysModule.sql");
//			Hashtable props = (Hashtable) servletContext.getAttribute(BaseEnv.PROP_INFO);
//
//			HashMap saveValues = new HashMap();
//
//			HashMap cashMap = new HashMap();//���ڴ洢���Ѿ������ĵ��������ݣ����ⵯ�����ظ�ִ��
//			for (int i = 0; i < groupList.size(); i++) {
//				List<HashMap<String, ExcelFieldInfoBean>> groupItem = groupList.get(i);
//
//				if (importState.isCancel()) {
//					break;
//					//groupErrorHandler(importState, ImportDataMgt.USER_CANCEL, "", groupItem, errorList, allTables);
//				} else {
//					saveValues = new HashMap();
//					boolean isRight = true;
//
//					Object ob = servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
//					MessageResources resources = null;
//					if (ob instanceof MessageResources) {
//						resources = (MessageResources) ob;
//					}
//					BomMgt bomMgt = new BomMgt();
//
//					//������������
//					String BillDate = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.BillDate", locale.toString())).getValue();
//					if (!isShortDate(BillDate)) {
//						groupErrorHandler(importState, -1, "���ڸ�ʽ����ȷ", groupItem, errorList, allTables,false);
//						continue;
//					}
//					String BillNo = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.BillNo", locale.toString())).getValue();
//					String GoodsNumber = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblGoods.GoodsNumber", locale.toString())) == null ? "" : groupItem.get(
//							0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblGoods.GoodsNumber", locale.toString())).getValue();
//					String GoodsFullName = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblGoods.GoodsFullName", locale.toString())) == null ? "" : groupItem
//							.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblGoods.GoodsFullName", locale.toString())).getValue();
//					String GoodsSpec = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblGoods.GoodsSpec", locale.toString())) == null ? "" : groupItem.get(0)
//							.get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblGoods.GoodsSpec", locale.toString())).getValue();
//					String UnitName = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblUnit.UnitName", locale.toString())).getValue();
//
//					String Unit = "";
//					if (cashMap.get("UnitName:" + UnitName) != null) {
//						Unit = cashMap.get("UnitName:" + UnitName).toString();
//					} else {
//						Unit = bomMgt.queryField("tblUnit", "id", "UnitName", UnitName, null, null);
//						cashMap.put("UnitName:" + UnitName, Unit);
//					}
//					String GoodsCode = "";
//					if (cashMap.get("GoodsCode:" + GoodsNumber + ":" + GoodsFullName) != null) {
//						GoodsCode = cashMap.get("GoodsCode:" + GoodsNumber + ":" + GoodsFullName).toString();
//					} else {
//						GoodsCode = bomMgt.queryField("tblGoods", "classCode", "GoodsNumber", GoodsNumber, "GoodsFullName", GoodsFullName);
//						cashMap.put("GoodsCode:" + GoodsNumber + ":" + GoodsFullName, GoodsCode);
//					}
//
//					String BatchNo = "";
//					if (GlobalsTool.getPropBean("BatchNo").getIsUsed() == 1) {
//						BatchNo = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.BatchNo", locale.toString())).getValue();
//					}
//					String Inch = "";
//					if (GlobalsTool.getPropBean("Inch").getIsUsed() == 1) {
//						Inch = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.Inch", locale.toString())).getValue();
//					}
//					String HueName = "";
//					String Hue = "";
//					if (GlobalsTool.getPropBean("Hue").getIsUsed() == 1) {
//						HueName = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.Hue", locale.toString())).getValue();
//						if (cashMap.get("HueName:" + HueName) != null) {
//							Hue = cashMap.get("HueName:" + HueName).toString();
//						} else {
//							Hue = bomMgt.queryField("ViewAttribute", "enumValue", "PropEName", HueName, "propName", "Hue");
//							cashMap.put("HueName:" + HueName, Hue);
//						}
//					}
//					String yearNOName = "";
//					String yearNO = "";
//					if (GlobalsTool.getPropBean("yearNO").getIsUsed() == 1) {
//						yearNOName = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.yearNO", locale.toString())).getValue();
//						if (cashMap.get("yearNOName:" + yearNOName) != null) {
//							yearNO = cashMap.get("yearNOName:" + yearNOName).toString();
//						} else {
//							yearNO = bomMgt.queryField("ViewAttribute", "enumValue", "PropEName", yearNOName, "propName", "yearNo");
//							cashMap.put("yearNOName:" + yearNOName, yearNO);
//						}
//					}
//					String timeTotal = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.timeTotal", locale.toString())).getValue();
//					double timeTotalD = 0;
//					try {
//						timeTotalD = Double.parseDouble(timeTotal);
//					} catch (Exception e) {
//					}
//					String VersionNO = groupItem.get(0).get(mainTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.VersionNO", locale.toString())).getValue();
//					String Remark = "";
//					//����
//					BomBean bomBean = new BomBean();
//					bomBean.setId(IDGenerater.getId());
//					bomBean.setBatchNo(BatchNo);
//					bomBean.setBillDate(BillDate);
//					bomBean.setBillNo(BillNo);
//					bomBean.setGoodsCode(GoodsCode);
//					bomBean.setHue(Hue);
//					bomBean.setInch(Inch);
//					bomBean.setYearNO(yearNO);
//					bomBean.setTimeTotal(timeTotalD);
//					bomBean.setUnit(Unit);
//					bomBean.setRemark(Remark);
//					bomBean.setQty(1);
//					bomBean.setSCompanyID(loginBean.getSunCmpClassCode());
//					bomBean.setCreateBy(loginBean.getId());
//					bomBean.setLastUpdateBy(loginBean.getId());
//					bomBean.setModuleType(moduleType);
//
//					if (GoodsCode == null || GoodsCode.length() == 0) {
//						//���ݱ���ظ�
//						String eg = GoodsNumber + GoodsFullName + GoodsSpec;
//						groupErrorHandler(importState, -1, eg == null || eg.length() == 0 ? "��Ʒ����Ϊ��" : GoodsNumber + GoodsFullName + "ȡֵ����ȷ", groupItem, errorList, allTables,false);
//						continue;
//					}
//					//���billNo�Ƿ����
//					Result result = bomMgt.isExistBillNo(BillNo);
//					if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//						//���ݱ���ظ�
//						groupErrorHandler(importState, -1, resources.getMessage("tblCompany.add.error.ComNumber"), groupItem, errorList, allTables,false);
//						continue;
//					}
//					//�����Ʒ�Ƿ��ظ�
//					int version = 1;
//					if (VersionNO != null && VersionNO.length() > 0) {
//						try {
//							version = Integer.parseInt(VersionNO);
//						} catch (Exception e) {
//						}
//						result = bomMgt.existBomByVersion(bomBean.getGoodsCode(), bomBean.getBatchNo(), bomBean.getInch(), bomBean.getHue(), bomBean.getYearNO(), bomBean.getModuleType(), version);
//						if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//							//��Ʒ�ظ�
//							groupErrorHandler(importState, -1, resources.getMessage("tblBOM.VersionNoSame.error", GoodsNumber, GoodsFullName), groupItem, errorList, allTables,false);
//							continue;
//						}
//					} else {
//						//δ�ṩ�汾�ţ����Զ�����һ��
//						result = bomMgt.existBom(bomBean.getGoodsCode(), bomBean.getBatchNo(), bomBean.getInch(), bomBean.getHue(), bomBean.getYearNO(), "", bomBean.getModuleType());
//						if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
//							version = (Integer) result.retVal;
//						}
//					}
//					bomBean.setVersiohnNO(version);
//					DBFieldInfoBean billNO = DDLOperation.getFieldInfo(allTables, "tblBOM", "BillNO");
//					Result rs = bomMgt.addBom(bomBean, billNO.getDefaultValue(), tableInfo, loginBean.getId(), locale, resources);
//
//					//ִ��ʧ��
//					if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//						String msg = GlobalsTool.getMessage(locale, "common.msg.addFailture");
//						if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
//							//�Զ���sql��䶨�Ʒ��ؽ��
//							String[] str = (String[]) rs.getRetVal();
//							if (str != null) {
//								msg = getDefSQLMsg(str[0]);
//
//							}
//						} else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ALERT) {
//							msg = getDefSQLMsg((String) rs.getRetVal());
//						}
//						groupErrorHandler(importState, -1, msg, groupItem, errorList, allTables,false);
//						continue;
//					}
//
//					boolean detailSuccess = true;
//					for (HashMap<String, ExcelFieldInfoBean> excelMap : groupItem) {
//						String childTableDisplay = GlobalsTool.getTableInfoBean("tblBOMDet").getDisplay().get(locale.toString());
//						String cPARENTNAME = excelMap.get(childTableDisplay + ":" + "����").getValue();
//						cPARENTNAME = cPARENTNAME == null ? "" : cPARENTNAME.trim();
//						String cBOMTYPE = excelMap.get(childTableDisplay + ":" + "����").getValue();
//						String cGoodsNumber = excelMap.get(childTableDisplay + ":" + "���ϱ��") == null ? "" : excelMap.get(childTableDisplay + ":" + "���ϱ��").getValue();
//						String cGoodsFullName = excelMap.get(childTableDisplay + ":" + "��������") == null ? "" : excelMap.get(childTableDisplay + ":" + "��������").getValue();
//						String cGoodsSpec = excelMap.get(childTableDisplay + ":" + "���Ϲ��") == null ? "" : excelMap.get(childTableDisplay + ":" + "���Ϲ��").getValue();
//						String cUnitName = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblUnit.UnitName", locale.toString())).getValue();
//						String cVersionNO = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.VersionNO", locale.toString())).getValue();
//
//						String cBatchNo = "";
//						if (GlobalsTool.getPropBean("BatchNo").getIsUsed() == 1) {
//							cBatchNo = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.BatchNo", locale.toString())).getValue();
//							cBatchNo = cBatchNo == null ? "" : cBatchNo;
//						}
//						String cInch = "";
//						if (GlobalsTool.getPropBean("Inch").getIsUsed() == 1) {
//							cInch = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.Inch", locale.toString())).getValue();
//							cInch = cInch == null ? "" : cInch;
//						}
//
//						String cLoss = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOMDet.Loss", locale.toString())).getValue();
//
//						String cTheoryQty = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOMDet.TheoryQty", locale.toString())).getValue();
//						String cQty = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOMDet.Qty", locale.toString())).getValue();
//						String cPlace = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOMDet.Place", locale.toString())).getValue();
//						String cRemark = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOMDet.Remark", locale.toString())).getValue();
//						String cMaterielAttribute = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOMDet.MaterielAttribute", locale.toString())).getValue();
//						String cMaterielType = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOMDet.MaterielType", locale.toString())).getValue();
//
//						String cUnit = "";
//						if (cashMap.get("UnitName:" + cUnitName) != null) {
//							cUnit = cashMap.get("UnitName:" + cUnitName).toString();
//						} else {
//							cUnit = bomMgt.queryField("tblUnit", "id", "UnitName", cUnitName, null, null);
//							cashMap.put("UnitName:" + cUnitName, cUnit);
//						}
//						String cGoodsCode = "";
//						if (cashMap.get("GoodsCode:" + cGoodsNumber + ":" + cGoodsFullName) != null) {
//							cGoodsCode = cashMap.get("GoodsCode:" + cGoodsNumber + ":" + cGoodsFullName).toString();
//						} else {
//							cGoodsCode = bomMgt.queryField("tblGoods", "classCode", "GoodsNumber", cGoodsNumber, "GoodsFullName", cGoodsFullName);
//							cashMap.put("GoodsCode:" + cGoodsNumber + ":" + cGoodsFullName, cGoodsCode);
//						}
//						String cHueName = "";
//						String cHue = "";
//						if (GlobalsTool.getPropBean("Hue").getIsUsed() == 1) {
//							cHueName = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.Hue", locale.toString())).getValue();
//							if (cashMap.get("HueName:" + cHueName) != null) {
//								cHue = cashMap.get("HueName:" + cHueName).toString();
//							} else {
//								cHue = bomMgt.queryField("ViewAttribute", "enumValue", "PropEName", cHueName, "propName", "Hue");
//								cHue = cHue == null ? "" : cHue;
//								cashMap.put("HueName:" + cHueName, cHue);
//							}
//						}
//						String cyearNOName = "";
//						String cyearNO = "";
//						if (GlobalsTool.getPropBean("yearNO").getIsUsed() == 1) {
//							cyearNOName = excelMap.get(childTableDisplay + ":" + GlobalsTool.getFieldDisplay(allTables, "tblBOM.yearNO", locale.toString())).getValue();
//							if (cashMap.get("yearNOName:" + cyearNOName) != null) {
//								cyearNO = cashMap.get("yearNOName:" + cyearNOName).toString();
//							} else {
//								cyearNO = bomMgt.queryField("ViewAttribute", "enumValue", "PropEName", cyearNOName, "propName", "yearNo");
//								cyearNO = cyearNO == null ? "" : cyearNO;
//								cashMap.put("yearNOName:" + cyearNOName, cyearNO);
//							}
//						}
//						cMaterielAttribute = GlobalsTool.getEnumerationValue("MaterielAttribute", locale.toString(), cMaterielAttribute);
//						cMaterielType = GlobalsTool.getEnumerationValue("MaterielType", locale.toString(), cMaterielType);
//
//						//������ϸ��
//						//��ϸ��ĵ�һ�У�һ���Ǳ���Ʒ��������ϸ���ӵڶ�����ſ����ǰ��Ʒ�����Ա�����������У�����ǰ��Ʒ�����ñ�����ϸ���е�����������
//						SystemSettingBean bean = BaseEnv.systemSet.get("bomset");
//						String bomId = bomBean.getId();//����ϸ��bom����Ϊ������������
//						String CkeyId = IDGenerater.getId(); //��ϸ�������ID        			
//						if ("GoodsNumber".equals(bean.getSetting())) {
//							if (!GoodsNumber.equals(cPARENTNAME)) {
//								//������Ʒ��źͰ汾�Ų�ѯbomId
//								bomId = (String) cashMap.get("BOMID:" + cPARENTNAME);
//							}
//						} else if ("GoodsFullName".equals(bean.getSetting())) {
//							if (!GoodsFullName.equals(cPARENTNAME)) {
//								//������Ʒ���ƺͰ汾�Ų�ѯbomId
//								bomId = (String) cashMap.get("BOMID:" + cPARENTNAME);
//							}
//						} else {
//							if (!GoodsSpec.equals(cPARENTNAME)) {
//								//������Ʒ���Ͱ汾�Ų�ѯbomId
//								bomId = (String) cashMap.get("BOMID:" + cPARENTNAME);
//							}
//						}
//						String tableNameT = "����".equals(cBOMTYPE) ? "tblBOMDet" : "tblBOMDetail";
//						HashMap values = new HashMap();
//						ArrayList childList = new ArrayList();
//						values.put("TABLENAME_" + tableNameT, childList);
//						HashMap valueMap = new HashMap();
//						childList.add(valueMap);
//
//						valueMap.put("CkeyId", CkeyId);
//						valueMap.put("SCompanyID", loginBean.getSunCmpClassCode());
//						valueMap.put("GoodsCode", cGoodsCode);
//						valueMap.put("Qty", cQty);
//						//    				valueMap.put("Price",cPrice) ;
//						//    				valueMap.put("Amount",cAmount) ;
//						valueMap.put("BatchNo", cBatchNo);
//						valueMap.put("Inch", cInch);
//						valueMap.put("Hue", cHue);
//						valueMap.put("Loss", cLoss);
//						valueMap.put("TheoryQty", cTheoryQty);
//						valueMap.put("yearNO", cyearNO);
//						valueMap.put("MaterielAttribute", cMaterielAttribute);
//						valueMap.put("MaterielType", cMaterielType);
//						valueMap.put("Unit", cUnit);
//						valueMap.put("Remark", cRemark);
//						//    				valueMap.put("BomId",bomId) ;
//						valueMap.put("Place", cPlace);
//						valueMap.put("VersionNO", cVersionNO);
//						String bomType = "����".equals(cBOMTYPE) ? "bomDet" : "bomDetail";
//
//						if (cGoodsCode == null || cGoodsCode.length() == 0) {
//							//���ݱ���ظ�
//							String eg = cGoodsNumber + cGoodsFullName + cGoodsSpec;
//							groupErrorHandler(importState, -1, eg == null || eg.length() == 0 ? "��Ʒ����Ϊ��" : eg + "ȡֵ����ȷ", groupItem, errorList, allTables,false);
//							detailSuccess = false;
//							break;
//						}
//						try {
//							double iQty = Double.parseDouble(cQty);
//							if (iQty < 0) {
//								groupErrorHandler(importState, -1, "��������С��0", groupItem, errorList, allTables,false);
//								detailSuccess = false;
//								break;
//							}
//						} catch (Exception e) {
//							groupErrorHandler(importState, -1, "��������Ϊ����", groupItem, errorList, allTables,false);
//							detailSuccess = false;
//							break;
//						}
//						try {
//							double iLoss = Double.parseDouble(cLoss);
//							if (iLoss < 0) {
//								groupErrorHandler(importState, -1, "����ʲ���С��0", groupItem, errorList, allTables,false);
//								detailSuccess = false;
//								break;
//							}
//						} catch (Exception e) {
//							groupErrorHandler(importState, -1, "����ʱ���Ϊ����", groupItem, errorList, allTables,false);
//							detailSuccess = false;
//							break;
//						}
//
//						rs = bomMgt.addBomDet(values, tableNameT, bomId, billNO, loginBean, bomType, tableInfo, loginBean.getId(), locale, resources, moduleType);
//
//						if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//							String msg = "";
//							if (rs.retCode == ErrorCanst.MULTI_VALUE_ERROR) {
//								//���ʧ��
//								msg = "������Ӻͳ�Ʒ����ͬ���Ե���Ʒ";
//							} else if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//								if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
//									//�Զ���sql��䶨�Ʒ��ؽ��
//									String[] str = (String[]) rs.getRetVal();
//									if (str != null) {
//										msg = getDefSQLMsg(str[0]);
//									}
//								} else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ALERT) {
//									msg = getDefSQLMsg((String) rs.getRetVal());
//								}
//							}
//							groupErrorHandler(importState, rs.retCode, msg, groupItem, errorList, allTables,false);
//							detailSuccess = false;
//							break;
//						}
//						if (rs.retVal != null && !rs.retVal.equals("")) {
//							CkeyId = rs.retVal.toString();
//						}
//						if ("GoodsNumber".equals(bean.getSetting())) {
//							cashMap.put("BOMID:" + cGoodsNumber, CkeyId);
//						} else if ("GoodsFullName".equals(bean.getSetting())) {
//							cashMap.put("BOMID:" + cGoodsFullName, CkeyId);
//						} else {
//							cashMap.put("BOMID:" + cGoodsSpec, CkeyId);
//						}
//					}
//					if (detailSuccess) {
//						groupErrorHandler(importState, 0, "", groupItem, errorList, allTables,false);
//					} else {
//						bomMgt.deleteBom(new String[] { bomBean.getId() });
//					}
//				}
//			}
//
//			fileName = "../log/" + importName + "_" + loginBean.getId() + ".xls";
//			File file = new File(fileName);
//			if (!file.getParentFile().exists()) {
//				file.getParentFile().mkdirs();
//			}
//			String error = GlobalsTool.getMessage(locale, "excel.error.title");
//			jxlTool.writeExcel(fileName, errorList, error);
		} catch (Exception e) {
			BaseEnv.log.error("importBomDet",e);
		} finally {
			isRuning = false;
		}
	}

	private void importData() {
		try {
			ImportDataBean importBean = new ImportDataBean();
			importBean.setName(mainTableDisplay);
			importBean.setTargetTable(importName);

			HashMap<String, ExcelFieldInfoBean> impFields = new HashMap<String, ExcelFieldInfoBean>();//���ڴ洢��Щ���Ѿ��е��������ˣ���ʽ����ʱ�����ǵ�������ݲ�������
			//impFields.put(ifb.getTableName()+"_"+ifb.getFieldName(), excelMapObj.get(excelStr)); 
			//��������excel�������ݣ���û����Ӧ��ģ�����ݣ����Զ�����

			//��¼����ʧ�ܵ�����
			List<HashMap<String, ExcelFieldInfoBean>> errorList = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
			//�������ӱ�����ݽ��з����������������ͬ����Ϊ�գ�˵����ͬһ����������Ϊ�ǲ������������ӱ��ϵ��
			List<List<HashMap<String, ExcelFieldInfoBean>>> groupList = new ArrayList<List<HashMap<String, ExcelFieldInfoBean>>>();
			String templateError = "";
			HashMap sameMap = new HashMap();
			List<HashMap<String, ExcelFieldInfoBean>> group = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
			for (HashMap<String, ExcelFieldInfoBean> excelMap : list) {

				if (sameMap.size() == 0) {
					//��һ������
					group.add(excelMap);
					for (ExcelFieldInfoBean hBean : jxlTool.headers) {
						if (hBean.getName().startsWith(mainTableDisplay + ":")) {
							//ֻ��������ֶ���Ҫ�������Ƚ�
							sameMap.put(hBean.getName(), excelMap.get(hBean.getName()).getValue());
						}
					}
				} else {
					boolean same = true;
					for (ExcelFieldInfoBean hBean : jxlTool.headers) {
						if (hBean.getName().startsWith(mainTableDisplay + ":")) {
							//ֻ��������ֶ���Ҫ�������Ƚ�
							String excelStr = excelMap.get(hBean.getName()).getValue();
							if (excelStr != null && excelStr.length() > 0) {
								if (!excelStr.equals(sameMap.get(hBean.getName()))) {
									//���в����
									same = false;
								}
							}
						}
					}
					if (same) {
						group.add(excelMap);
					} else {
						groupList.add(group);
						group = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
						group.add(excelMap);
						for (ExcelFieldInfoBean hBean : jxlTool.headers) {
							if (hBean.getName().startsWith(mainTableDisplay + ":")) {
								//ֻ��������ֶ���Ҫ�������Ƚ�
								sameMap.put(hBean.getName(), excelMap.get(hBean.getName()).getValue());
							}
						}
					}

				}
			}
			if (group.size() > 0)
				groupList.add(group);

			/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
			

			ArrayList sqls = new ArrayList();
			// ��ȡ·��
			String path = servletContext.getRealPath("DoSysModule.sql");
			Hashtable props = (Hashtable) servletContext.getAttribute(BaseEnv.PROP_INFO);

			HashMap cashMap = new HashMap();//���ڴ洢���Ѿ������ĵ��������ݣ����ⵯ�����ظ�ִ��

			Object ob = servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}

			int orderNo = 0;
			importState = new ImportState(groupList.size(), 0, 0);

			HashMap saveValues = new HashMap();
			
			for (int i = 0; i < groupList.size(); i++) {
				//Thread.sleep(5000);
				
				List<HashMap<String, ExcelFieldInfoBean>> groupItem = groupList.get(i);

				if (importState.isCancel()) {
					break;
					//groupErrorHandler(importState, ImportDataMgt.USER_CANCEL, "", groupItem, errorList, allTables);
					
				} else {
					saveValues = new HashMap();
					//���ȸ���moduleType��moduleParamֵ,����ͬһ�ű��в�ͬģ��ĵ��빤��
					if (moduleType != null && moduleType.length() > 0) {
						saveValues.put("moduleType", moduleType);
						if (moduleParam != null && moduleParam.length() > 0) {
							String[] mParams = moduleParam.split("&");
							for (String mParam : mParams) {
								if (mParam.indexOf("=") > 0) {
									saveValues.put(mParam.split("=")[0], mParam.split("=")[1]);
								}
							}
						}
					}

					boolean isRight = true;
					String parentCode = "";
					//ȡ�����ֵ
					if (tableInfoBean.getClassFlag() == 1 && groupItem.size() > 0) {
						ExcelFieldInfoBean pef = ((HashMap<String, ExcelFieldInfoBean>) groupItem.get(0)).get(mainTableDisplay + ":" + GlobalsTool.getMessage(locale, "importData.parent"));
						String strParent = pef == null ? "" : pef.getValue();
						if (strParent != null && strParent.length() > 0) {
							Result result = importDataMgt.getParentCode(strParent, tableInfoBean);
							if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
								parentCode = (String) result.getRetVal();
							} else {
								groupErrorHandler(importState, ImportDataMgt.NOT_FIND_PARENTCODE, GlobalsTool.getMessage(locale, "com.import.data.error", strParent), groupItem, errorList, allTables,false);
								continue;
							}
						}
					}
					
					//������������õ���ʱ������Ʒ����,��Ϊ�ɹ�����������ⵥ
					if(BaseEnv.systemSet.get("importAddGoods") !=null && "true".equals(BaseEnv.systemSet.get("importAddGoods").getSetting()) && 
							("tblBuyInStock".equals(importName) || "tblOtherIn".equals(importName))){
						boolean goodsRight=true;
						for (int kk = 0; kk < groupItem.size(); kk++) {
							HashMap<String, ExcelFieldInfoBean> excelMap = groupItem.get(kk);
							String msgModuleNot = GlobalsTool.getMessage(locale, "importData.moduleNot");
							String msgDownload = GlobalsTool.getMessage(locale, "importData. downloadRightModule");
							  
							HashMap goodsValues = new HashMap();
							ImportDataBean goodsBean = new ImportDataBean();
							if("tblOtherIn".equals(importName)){
								goodsBean.setName(GlobalsTool.getTableInfoBean("tblOtherInDet").getDisplay().get(locale.toString()));
							}else if("tblBuyInStock".equals(importName)){
								goodsBean.setName(GlobalsTool.getTableInfoBean("tblBuyInStockDet").getDisplay().get(locale.toString()));
							}
							goodsBean.setTargetTable("tblGoods");
							ArrayList goodsSqls = new ArrayList();
							HashMap<String, ExcelFieldInfoBean> goodsImpFields = new HashMap<String, ExcelFieldInfoBean>();//���ڴ洢��Щ���Ѿ��е��������ˣ���ʽ����ʱ�����ǵ�������ݲ�������
							HashMap goodscashMap = new HashMap();//���ڴ洢���Ѿ������ĵ��������ݣ����ⵯ�����ظ�ִ��
							
							Result rs = jxlTool.replaceData(goodsBean, excelMap, goodsValues, locale.toString(), allTables, 
									goodsSqls, goodsImpFields, msgModuleNot, msgDownload, resources, loginBean, path, props,
									mop, goodscashMap, 0,true);
							if (rs.retCode == ErrorCanst.EXCEL_READ_FIELDNOTEXIST) {
								groupErrorHandler(importState, -99999999, GlobalsTool.getMessage(locale,"import.not.exist.column"), groupItem, errorList, allTables,true);
								goodsRight = false;
								break;
							}
							if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
								groupErrorHandler(importState, rs.getRetCode(), rs.getRetVal(), groupItem, errorList, allTables,true);
								goodsRight = false;
								break;
							}
							//��ѯ����Ƿ����
							String goodsNumber = (String)goodsValues.get("GoodsNumber");
							if(goodsNumber==null || goodsNumber.length() == 0){
								groupErrorHandler(importState, -1, "��Ʒ��Ų�����", groupItem, errorList, allTables,true);
								goodsRight = false;
								break;
							}
							boolean isExist = importDataMgt.goodsExist(goodsNumber);
							if(!isExist){
								//����
								if(!this.saveToDb(GlobalsTool.getTableInfoBean("tblGoods"),goodsValues, goodsSqls, groupItem, errorList, 0, goodsImpFields, "", resources,path,props,true)){
									goodsRight = false;
									continue;
								}
							}
						}
						if(!goodsRight){
							continue;
						}
					}

					for (int kk = 0; kk < groupItem.size(); kk++) {
						HashMap<String, ExcelFieldInfoBean> excelMap = groupItem.get(kk);
						String msgModuleNot = GlobalsTool.getMessage(locale, "importData.moduleNot");
						String msgDownload = GlobalsTool.getMessage(locale, "importData. downloadRightModule");
						Result rs;
						try{
							rs = jxlTool.replaceData(importBean, excelMap, saveValues, locale.toString(), allTables, sqls, impFields, msgModuleNot, msgDownload, resources, loginBean, path, props,
								mop, cashMap, kk,false);
						}catch(Exception e){
							BaseEnv.log.error("ImportThread.importData Error",e);
							String erm = e.getMessage();
							rowErrorHandler(-99999999, erm, excelMap, allTables,false);
							isRight = false;
							break;
						}
						if (rs.retCode == ErrorCanst.EXCEL_READ_FIELDNOTEXIST) {
							rowErrorHandler(-99999999, GlobalsTool.getMessage(locale,"import.not.exist.column"), excelMap, allTables,false);
							isRight = false;
							break;
						}
						if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
							rowErrorHandler(rs.getRetCode(), rs.getRetVal(), excelMap, allTables,false);
							isRight = false;
							break;
						}
					}
					
					if (isRight) { 

						
						/* ���ƾ֤ ƾ֤�����⴦��*/
						if ("tblAccMain".equals(tableInfoBean.getTableName())) {
							/* ��ѯƾ֤�����ֵ*/
							if (orderNo == 0) {
								Result result = new VoucherMgt().queryMaxOrderNo(String.valueOf(saveValues.get("CredTypeID")), String.valueOf(saveValues.get("BillDate")));
								if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									groupErrorHandler(importState, -999999999, GlobalsTool.getMessage(locale, "common.error.credNo"), groupItem, errorList, allTables,false);
									continue;
								} else {
									orderNo = Integer.valueOf(result.retVal.toString());
									saveValues.put("OrderNo", result.retVal);
								}
							} else {
								orderNo++;
								saveValues.put("OrderNo", orderNo);
							}
						}
						
						//����
						this.saveToDb(tableInfoBean,saveValues, sqls, groupItem, errorList, orderNo, impFields, parentCode, resources,path,props,false);
						/* �������ݻ��� */
						if ("tblDepartment".equals(tableInfoBean.getTableName()) || "tblEmployee".equals(tableInfoBean.getTableName())) {
							new MSGConnectCenter().init();
							UserMgt.initOnlineUser();
						}
					} else {
						groupErrorHandler(importState, -1, "", groupItem, errorList, allTables,false);
					}
				}
			}
			fileName = "../log/" + importName + "_" + loginBean.getId() + ".xls";
			File file = new File(fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			String error = GlobalsTool.getMessage(locale, "excel.error.title");
			jxlTool.writeExcel(fileName, errorList, error);
		} catch (Exception e) {
			BaseEnv.log.error("importData",e);
		} finally {
			isRuning = false;
		}
	}
	
	/**
	 * �����ڳ�
	 *
	 */
	private void importIniAccDet() {
		try {
			//��¼����ʧ�ܵ�����
			List<HashMap<String, ExcelFieldInfoBean>> errorList = new ArrayList<HashMap<String, ExcelFieldInfoBean>>();
			Object ob = servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}
			importState = new ImportState(list.size(), 0, 0);
			
			
			
			String accNumber = moduleParam;
			Result rs = new IniAccMgt().getAccTypeCal(accNumber);
			boolean hasCurrency = false; //�Ƿ��������
			List<String>  isItem; //��¼��Ŀ�ĺ�����
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//��ѯ�ɹ�
				Object[] obj = (Object[])rs.getRetVal();				
				isItem = (List<String>)obj[0];					
				String currStr = (String)obj[1];
				String[] str = currStr.split(";");
				//�����
				if("1".equals(str[0])){	
					hasCurrency = true;
				}
			}else {
				groupErrorHandler(importState, -999999999, "���δ����", list, errorList, allTables,false);
				return;
			}
			String impId = IDGenerater.getId();
			

			HashMap saveValues = new HashMap();
			
			for (int i = 0; i < list.size(); i++) {				
				HashMap<String, ExcelFieldInfoBean> excelMap = list.get(i);
				if (importState.isCancel()) {
					break;					
				} else {
					saveValues = new HashMap();
					boolean isRight = true;
					//��ȡ����
					saveValues.put("AccCode", accNumber);
					saveValues.put("impId", impId);
					
					for(String item:isItem){
						String[] str = item.split(";");
						String fieldName = str[1];
						String displayName = GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+str[1], locale.toString());
						Object val = ((ExcelFieldInfoBean)excelMap.get(displayName)).getValue();
						if(val==null || val.equals("")){
							groupErrorHandler(importState, -999999999, str[0]+"����Ϊ��", excelMap, errorList, allTables,false);
							isRight = false;
							break;
						}
						String mt = str.length>=5?str[4]:"";
						Result rr=new IniAccMgt().getCalName(accNumber, fieldName, mt, (String)val, locale.toString());
						if(rr.retCode != ErrorCanst.DEFAULT_SUCCESS){
							groupErrorHandler(importState, -999999999, str[0]+""+rr.retVal, excelMap, errorList, allTables,false);
							isRight = false;
							break;
						}
						saveValues.put(fieldName, rr.retVal);
					}
					if(!isRight){
						continue;
					}
					//�����
					if(hasCurrency){	
						String fieldName = "Currency";
						Object val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						
						String curvalue = (String)val;
						if(val==null || val.equals("") || val.equals("��λ��")){
							curvalue = "";
						}else{
							Result rr=new IniAccMgt().getCalName(accNumber, fieldName, "", (String)val, locale.toString());
							if(rr.retCode != ErrorCanst.DEFAULT_SUCCESS){
								groupErrorHandler(importState, -999999999, "���"+rr.retVal, excelMap, errorList, allTables,false);
								continue;
							}	
							curvalue = (String)rr.retVal;
						}
						saveValues.put(fieldName, curvalue);
						
						fieldName = "CurrencyRate";
						if(curvalue.equals("")){
							saveValues.put(fieldName, "1");
						}else{
							val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
							saveValues.put(fieldName, (String)val);
						}
						
						fieldName = "CurBeginAmount";
						val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						saveValues.put(fieldName, (String)val);
						fieldName = "CurTotalDebit";
						val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						saveValues.put(fieldName, (String)val);
						fieldName = "CurTotalLend";
						val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						saveValues.put(fieldName, (String)val);
						fieldName = "CurTotalRemain";
						val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						saveValues.put(fieldName, (String)val);							
					}else{
						String fieldName = "BeginAmount";
						Object val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						saveValues.put(fieldName, (String)val);
						fieldName = "TotalDebit";
						val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						saveValues.put(fieldName, (String)val);
						fieldName = "TotalLend";
						val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						saveValues.put(fieldName, (String)val);
						fieldName = "TotalRemain";
						val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
						saveValues.put(fieldName, (String)val);						
					}
					String fieldName = "Remark";
					Object val = ((ExcelFieldInfoBean)excelMap.get(GlobalsTool.getFieldDisplay(allTables, "tblIniAccDet."+fieldName, locale.toString()))).getValue();
					saveValues.put(fieldName, (String)val);
					 						
					//����
					rs = new Result();
					try {
						if(saveValues.get("AccCode") == null || "".equals(saveValues.get("AccCode"))){
							groupErrorHandler(importState, -999999999, "��ƿ�Ŀ����Ϊ�ջ�����д����ȷ", excelMap, errorList, allTables,false);
							continue;
						}
						/**
						 * ɾ����ֵ������
						 */
						if(hasCurrency){	
							if(isValue(saveValues.get("CurBeginAmount")) && isValue(saveValues.get("CurTotalDebit")) 
									&& isValue(saveValues.get("CurTotalLend")) && isValue(saveValues.get("CurTotalRemain"))){
								groupErrorHandler(importState, -999999999, "���δ����", excelMap, errorList, allTables,false);
								continue;
							}
						}else{							
							if(isValue(saveValues.get("BeginAmount")) && isValue(saveValues.get("TotalDebit")) 
									&& isValue(saveValues.get("TotalLend")) && isValue(saveValues.get("TotalRemain"))){
								groupErrorHandler(importState, -999999999, "���δ����", excelMap, errorList, allTables,false);
								continue;
							}
						}
						
						rs = new IniAccMgt().addIniAccItem(saveValues, accNumber, loginBean, locale, resources);
					} catch (Exception e) {
						BaseEnv.log.error("ImportThread.importData add Error:",e);
						groupErrorHandler(importState, -99999999, GlobalsTool.getMessage(locale, "common.msg.error"), excelMap, errorList, allTables,false);
						continue;
					}
					groupErrorHandler(importState, rs.getRetCode(), rs.getRetVal(), excelMap, errorList, allTables,false);
					
				}
			}
			//��¼�����¼
			if(importState.getSuccess() > 0){
				new IniAccMgt().addImportRecord(impId,accNumber, loginBean);
			}
			
			fileName = "../log/" + importName + "_" + loginBean.getId() + ".xls";
			File file = new File(fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			String error = GlobalsTool.getMessage(locale, "excel.error.title");
			jxlTool.writeExcel(fileName, errorList, error);
		} catch (Exception e) {
			BaseEnv.log.error("importIniAccDet",e);
		} finally {
			isRuning = false;
		}
	}
	
	private boolean saveToDb(DBTableInfoBean tBean,HashMap saveValues,ArrayList sqls,List<HashMap<String, ExcelFieldInfoBean>> groupItem,
			List<HashMap<String, ExcelFieldInfoBean>> errorList,int orderNo,HashMap<String, ExcelFieldInfoBean> impFields,
			String parentCode,MessageResources resources,String path,Hashtable props,boolean isAutoGoods){
		
		
		String addMessage = GlobalsTool.getMessage(locale, "common.lb.add");
		/**
		 * �ֶ������ǵ��ݱ���������õ�������
		 */
		String tableName = "";
		
		//����Ĭ��ֵ
		UserFunctionMgt userMgt = new UserFunctionMgt();
		DynDBManager mgt = new DynDBManager();
		try {							
			userMgt.setDefault(tBean, saveValues, loginBean.getId());
			//��ϸ������Ĭ��ֵ
			ArrayList<DBTableInfoBean> ct = DDLOperation.getChildTables(tBean.getTableName(), allTables);
			for (int j = 0; ct != null && j < ct.size(); j++) {
				DBTableInfoBean cbean = ct.get(j);
				ArrayList clist = (ArrayList) saveValues.get("TABLENAME_" + cbean.getTableName());
				for (int k = 0; clist != null && k < clist.size(); k++) {
					HashMap cmap = (HashMap) clist.get(k);
					userMgt.setDefault(cbean, cmap, loginBean.getId());
				}
			}
		} catch (Exception e) {
			groupErrorHandler(importState, -999999999, "Ĭ��ֵ���ô���", groupItem, errorList, allTables,isAutoGoods);
			return false ;
		}
		try {
			//��ʽ����
			BSFExec.calculate(saveValues, allTables, tBean, impFields,locale);
		} catch (Exception e) {
			String emsg = e.getMessage();
			//JavaScript Error: Internal Error: java.lang.RuntimeException: java.lang.Exception: ���(20)�빫ʽ��������һ��
			emsg = emsg.indexOf("java.lang.Exception:")>0?emsg.substring(emsg.indexOf("java.lang.Exception:")+"java.lang.Exception:".length()):emsg;
			groupErrorHandler(importState, -99999999, emsg, groupItem, errorList, allTables,isAutoGoods);
			return false;
		}

		

		/*��֤�ǿ�-----���ﲻ����Ҫ����֤�Ĺ�������Ϊ��add�������и�ȫ��Ľ�����*/ 
		/********************************************
		 ִ����ؽӿ�  saveValues Ϊ�������ݵ�HashMap ����˲�������Ӧ�ӿ�������ݲ���
		 ********************************************/
		
		
		saveValues.put("ModuleId", moduelViewbean.getModuleId());
		Result rs;
		try {
			rs = userMgt.add(tBean, saveValues, loginBean, parentCode, allTables, path, "", locale, addMessage, resources, props, mop, "10");
		} catch (Exception e) {
			BaseEnv.log.error("ImportThread.importData add Error:",e);
			groupErrorHandler(importState, -99999999, GlobalsTool.getMessage(locale, "common.msg.error"), groupItem, errorList, allTables,isAutoGoods);
			return false;
		}
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			rs = userMgt.batchExecSql(sqls);
			groupErrorHandler(importState, rs.getRetCode(), rs.getRetVal(), groupItem, errorList, allTables,isAutoGoods);
			return true;
		} else {
			groupErrorHandler(importState, rs.getRetCode(), rs.getRetVal(), groupItem, errorList, allTables,isAutoGoods);
			return false;
		}
	}

	/**
	 * �ж�ֵ�Ƿ���ڻ���Ϊ��
	 * @param o
	 * @return
	 */
	public boolean isValue(Object o){
		boolean isYes = false;
		if(o == null || "".equals(o) || Float.valueOf(o.toString())==0){
			isYes = true;
		}
		return isYes;
	}

	/**
	 * ���ݵ���ʱ�Ĵ�����
	 * @param errorCode int
	 * @param errorValue Object
	 * @param excelList List
	 * @param request HttpServletRequest
	 */
	private void groupErrorHandler(ImportState importState, int errorCode, Object errorValue, List<HashMap<String, ExcelFieldInfoBean>> group, List<HashMap<String, ExcelFieldInfoBean>> errorList,
			Hashtable allTables,boolean isAutoGoods) {
		if (errorCode == ErrorCanst.DEFAULT_SUCCESS) {
			importState.setSuccess(importState.getSuccess() + 1);
			return;
		}
		importState.setFail(importState.getFail() + 1);

		errorList.addAll(group);
		if (group.get(0).get("Error") == null) {
			ExcelFieldInfoBean eBean = new ExcelFieldInfoBean();
			eBean.setValue(errorMsg(errorCode, errorValue, allTables,isAutoGoods));
			group.get(0).put("Error", eBean);
		}
	}
	
	/**
	 * ���ݵ���ʱ�Ĵ�����
	 * @param errorCode int
	 * @param errorValue Object
	 * @param excelList List
	 * @param request HttpServletRequest
	 */
	private void groupErrorHandler(ImportState importState, int errorCode, Object errorValue, HashMap<String, ExcelFieldInfoBean> groupItem, List<HashMap<String, ExcelFieldInfoBean>> errorList,
			Hashtable allTables,boolean isAutoGoods) {
		if (errorCode == ErrorCanst.DEFAULT_SUCCESS) {
			importState.setSuccess(importState.getSuccess() + 1);
			return;
		}
		importState.setFail(importState.getFail() + 1);

		errorList.add(groupItem);
		if (groupItem.get("Error") == null) {
			ExcelFieldInfoBean eBean = new ExcelFieldInfoBean();
			eBean.setValue(errorMsg(errorCode, errorValue, allTables,isAutoGoods));
			groupItem.put("Error", eBean);
		}
	}

	private void rowErrorHandler(int errorCode, Object errorValue, HashMap<String, ExcelFieldInfoBean> groupItem, Hashtable allTables,boolean isAutoGoods) {
		ExcelFieldInfoBean eBean = new ExcelFieldInfoBean();
		eBean.setValue(errorMsg(errorCode, errorValue, allTables,isAutoGoods));
		groupItem.put("Error", eBean);
	}

	private String errorMsg(int errorCode, Object errorValue, Hashtable allTables,boolean isAutoGoods) {
		errorValue= errorValue==null?"":errorValue;
		String errorMsg = "";
		if (errorCode == ImportDataMgt.ENUM_VALUE_ERROR) {
			errorMsg = (String) errorValue + GlobalsTool.getMessage(locale, "importData.valueError");
		} else if (errorCode == ImportDataMgt.VALUE_NOT_NULL) {
			errorMsg = (String) errorValue + GlobalsTool.getMessage(locale, "common.validate.error.null") + "��ȡֵ����ȷ";
		} else if (errorCode == ImportDataMgt.SEQ_NUMBER_ERROR) {
			errorMsg = (String) errorValue + GlobalsTool.getMessage(locale, "common.seqNumber.error.null");
		} else if (errorCode == ImportDataMgt.MAIN_TABLE_ERROR) {
			errorMsg = (String) errorValue + GlobalsTool.getMessage(locale, "importData.valueError");
		} else if (errorCode == ImportDataMgt.USER_CANCEL) {
			errorMsg = (String) errorValue+GlobalsTool.getMessage(locale, "importData.userCancel");
		} else if (errorCode == ImportDataMgt.NOT_FIND_PARENTCODE) {
			errorMsg = (String) errorValue;
		} else if (errorCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
			errorMsg = (String) errorValue+GlobalsTool.getMessage(locale, "com.currentaccbefbill");
		} else if (errorCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC) {
			errorMsg = (String) errorValue+GlobalsTool.getMessage(locale, "billdate.not.exist.acc");
		} else if (errorCode == ErrorCanst.ADD_LOG_FAILURE) {
			errorMsg = (String) errorValue+GlobalsTool.getMessage(locale, "add.log.error");
		} else if (errorCode == ErrorCanst.ADD_AUDITING_LAST_NODE_FAILURE) {
			errorMsg = (String) errorValue+GlobalsTool.getMessage(locale, "add.return.audtiing.error");
		} else if (errorCode == ErrorCanst.MULTI_VALUE_ERROR) {
			if (errorValue != null) {
				String os = errorValue.toString();
            	String vl = "";
            	if(os.indexOf(":") > -1){
	            	vl = os.substring(os.indexOf(":")+1);
	            	os = os.substring(0,os.indexOf(":"));
            	}
                
				errorMsg = getFieldDisplay(allTables, os, locale.toString()) + " " + GlobalsTool.getMessage(locale, "common.msg.MULTI_VALUE_ERROR")+vl;
			}
		} else if (errorCode == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			if (errorValue != null) {
				String[] str = (String[]) errorValue;
				errorMsg = getDefSQLMsg(str[0]);
				if (errorMsg == null) {
					errorMsg = str[0];
				}
			}
		} else if (errorCode == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
			if (errorValue != null) {
				ConfirmBean confirm = (ConfirmBean) errorValue;
				errorMsg = getDefSQLMsg(confirm.getMsgContent());
			}
		} else if (errorCode == ErrorCanst.PROC_NEGATIVE_ERROR) {
			if (errorValue != null) {
				String[] str = errorValue.toString().split(";");
				if (str.length == 1) {
					errorMsg = GlobalsTool.getMessage(locale, "common.error.negative2", str[0]);
				} else {
					errorMsg = GlobalsTool.getMessage(locale, "common.error.negative", str[0], str[1]);
				}
			}
		} else if (errorCode == ErrorCanst.MULTI_VALUE_ERROR) {
			if (errorValue != null) {
				errorMsg = GlobalsTool.getFieldDisplay(allTables, errorValue.toString(), locale.toString()) + " " + GlobalsTool.getMessage(locale, "billexport.mainfieldname.more");
			}
		} else if (errorCode == ErrorCanst.RET_DEFINE_SENTENCE_ERROR) {
			if (errorValue != null) {
				errorMsg = errorValue.toString();
			}
		} else if (errorCode == ImportDataMgt.EXIST_SPECIAL_CHAR) {
			if (errorValue != null) {
				errorMsg = errorValue.toString() + GlobalsTool.getMessage(locale, "con.validate.contain.sc");
			}
		} else if (errorCode == importDataMgt.DATE_FORMAT_ERROR) {
			if (errorValue != null) {
				errorMsg = errorValue.toString() + GlobalsTool.getMessage(locale, "importData.formatError");
			}
		} else if (errorCode == importDataMgt.STRING_TO_NUMBER_ERROR) {
			if (errorValue != null) {
				errorMsg = errorValue.toString() + GlobalsTool.getMessage(locale, "string.to.number.error");
			}
		} else if (errorCode == importDataMgt.STR_SPEC_ERROR) { //���������ַ�
			if (errorValue != null) {
				errorMsg = errorValue.toString() + GlobalsTool.getMessage(locale, "con.validate.contain.sc");
			}
		} else {
			if (errorValue != null) {
				errorMsg = errorValue.toString();
			} else {
				errorMsg = "";
			}
		}
		if(errorMsg==null || errorMsg.length()==0){
			errorMsg = "����ʧ��";
		}
		errorMsg = errorMsg.replaceAll("\\n", " ");
		return isAutoGoods?"������Ʒ��"+errorMsg:errorMsg;
	}

	protected String getDefSQLMsg(String o) {
		String msg = "";
		String[] os = (String[]) o.split(",");
		if (os.length == 1) {
			String key = os[0].replace("@RepComma:", ",");
			msg = GlobalsTool.getMessage(locale, key);
			if (msg == null || msg.length() == 0) {
				msg = key;
			}
		} else if (os.length == 2) {
			msg = GlobalsTool.getMessage(locale, os[0].replace("@RepComma:", ","), os[1].replace("@RepComma:", ","));
		} else if (os.length == 3) {
			msg = GlobalsTool.getMessage(locale, os[0].replace("@RepComma:", ","), os[1].replace("@RepComma:", ","), os[2].replace("@RepComma:", ","));
		} else if (os.length >= 4) {
			msg = GlobalsTool.getMessage(locale, os[0].replace("@RepComma:", ","), os[1].replace("@RepComma:", ","), os[2].replace("@RepComma:", ","), os[3].replace("@RepComma:", ","));
		}
		return msg;
	}

	/**
	 * ȡ�ֶε���ʾ��
	 * @param fieldName String tableName.fieldName
	 * @return String
	 */
	public static String getFieldDisplay(Hashtable allTables, String fieldName, String locale) {
		if (fieldName == null || fieldName.trim().length() == 0) {
			return null;
		}
		if(fieldName.indexOf(".")==-1){
			return fieldName;
		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return null;
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(locale).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}

	protected String getParameter(String param, HttpServletRequest req) {
		String str = req.getParameter(param);
		if (str != null) {
			str = str.trim();
		}
		return str;
	}

	private boolean isShortDate(String str) {
		if (str != null && str.trim().length() > 0 && !GlobalsTool.isShortDate(str)) {
			return false;
		}
		return true;
	}

	public void run() {
		if (isBom) {
			this.importDataBom();
		}else if(importName.equals("tblIniAccDet")){
			this.importIniAccDet();
		}else{
			this.importData();
		}
	}

}
